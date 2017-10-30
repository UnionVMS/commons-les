package fish.focus.uvms.commons.les.inmarsat;


import fish.focus.uvms.commons.les.inmarsat.header.HeaderStruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class InmarsatFileHandler {
	public static final String ERROR_DIR_NAME = "error";

	private static final Logger LOGGER = LoggerFactory.getLogger(InmarsatFileHandler.class);
	private static final byte[] HEADER_PATTERN = ByteBuffer.allocate(4).put((byte) InmarsatDefintion.API_SOH)
			.put(InmarsatDefintion.API_LEAD_TEXT.getBytes()).array();
	private static final int PATTERN_LENGTH = HEADER_PATTERN.length;
	private final Path downloadDir;
	private final Path errorDir;

	public InmarsatFileHandler(Path downloadDir) {
		this.downloadDir = downloadDir;
		this.errorDir = Paths.get(downloadDir.getParent().toString(), ERROR_DIR_NAME);
	}

	public ConcurrentMap<Path, InmarsatMessage[]> createMessages() throws IOException {
		String pattern = new String(HEADER_PATTERN);
		List<Path> fileNames = listFiles(downloadDir);

		ConcurrentHashMap<Path, InmarsatMessage[]> output = new ConcurrentHashMap<>();

		if (fileNames != null) {

			for (Path file : fileNames) {
				LOGGER.debug("Handling file {}", file);
				InmarsatMessage[] inmarsatMessages;
				byte[] fileBytes;
				try {
					fileBytes = Files.readAllBytes(file);
				} catch (IOException ioe) {
					LOGGER.error("File could not be read :{}", file, ioe);
					continue;
				}

				String fileStr = new String(fileBytes);
				if (fileStr.contains(pattern)) {
					inmarsatMessages = byteToInmMessge(fileBytes, file);
					output.put(file, inmarsatMessages);
				} else {
					LOGGER.error("File is not a valid Inmarsat Message: {} - moved", file);
					moveFileToDir(file, errorDir);
				}

			}
		}

		return output;
	}

	public InmarsatMessage[] byteToInmMessge(final byte[] fileBytes, Path file) {
		byte[] bytes = insertMissingMemberNo(fileBytes);

		ArrayList<InmarsatMessage> messages = new ArrayList<>();

		if (bytes == null || bytes.length <= PATTERN_LENGTH) {
			LOGGER.error("File is not a valid Inmarsat Message: {} - moved", file.getFileName());
			moveFileToDir(file, errorDir);

			return new InmarsatMessage[] {};
		}


		//Parse bytes for messages
		for (int i = 0; i < (bytes.length - PATTERN_LENGTH); i++) {
			//Find message
			if (InmarsatHeader.isStartOfMessage(bytes, i)) {
				InmarsatMessage message;
				byte[] messageBytes = Arrays.copyOfRange(bytes, i, bytes.length);
				try {
					message = new InmarsatMessage(messageBytes);
				} catch (InmarsatException e) {
					LOGGER.error("Something wrong with file {}, contents: {}, errormsg:{}",
							file.getFileName().toString(), InmarsatUtils.bytesArrayToHexString(messageBytes),
							e.getMessage());
					continue;
				}

				if (message.validate()) {
					messages.add(message);
				} else {
					LOGGER.info("Message in file {} rejected:{}", file.getFileName(), message);
				}
			}
		}

		return messages.toArray(new InmarsatMessage[0]); // "new InmarsatMessage[0]" is used  instead of "new Inmarsat[messages.size()]" to get better performance
	}


	public List<Path> listFiles(Path dir) throws IOException {
		List<Path> result = new ArrayList<>();
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*.dat")) {
			for (Path entry : stream) {
				result.add(entry);
			}

		} catch (DirectoryIteratorException ex) {
			// I/O error encounted during the iteration, the cause is an IOException
			LOGGER.info("No dir {} , or unable to readBytesFromFile", dir);
			throw ex.getCause();
		}
		return result;
	}


	public void moveFileToDir(Path fromFile, Path toDir) {
		try {
			if (!Files.exists(toDir)) {
				Files.createDirectory(toDir);
			}
			Files.move(fromFile, Paths.get(toDir.toString(), fromFile.getFileName().toString()),
					StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			LOGGER.error("The file: {} could not be moved to dir {} ", new Object[] {fromFile, toDir}, e);
		}
	}

	/**
	 * If headers are missing member number this method insert a default memberno(0xFF)
	 *
	 * @param contents bytes that might contain headers with/without member number
	 * @return bytemessages with missing member number set to 0xFF
	 */
	public byte[] insertMissingMemberNo(byte[] contents) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		boolean insertFF = false;

		int insertPosition = 0;
		for (int i = 0; i < contents.length; i++) {
			//Find SOH
			if (InmarsatHeader.isStartOfMessage(contents, i)) {
				int headerLength = contents[i + HeaderStruct.POS_HEADER_LENGTH];
				int expectedEOHPosition = i + headerLength - 1;
				//Check if memberNo exits
				if ((expectedEOHPosition >= contents.length)
						|| ((contents[expectedEOHPosition - 1] == (byte) InmarsatDefintion.API_EOH)
								&& contents[expectedEOHPosition] != (byte) InmarsatDefintion.API_EOH)) {
					insertFF = true;
					insertPosition = expectedEOHPosition - 1;
				}

			}
			//Find EOH
			if (insertFF && (contents[i] == (byte) InmarsatDefintion.API_EOH) && (insertPosition == i)) {
				output.write((byte) 0xFF);
				insertFF = false;

			}
			output.write(contents[i]);
		}
		return output.toByteArray();
	}

}
