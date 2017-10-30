package fish.focus.uvms.commons.les.inmarsat;

import fish.focus.uvms.commons.les.inmarsat.header.HeaderType;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class InmarsatFileHandlerTest {
	final String start = "444E494420313037343520310D0A52657472696576696E6720444E494420646174612E2E2E0A";
	final String end = "0A3E20";
	private final String headerDnid = "015426540116890b08000155140036372455c307e702".toUpperCase();
	private final String headerDnidNoMemberNo = "015426540116890b08000155140036372455c30702".toUpperCase();
	private final String headerDnidFFMemberNo = "015426540116890b08000155140036372455c307FF02".toUpperCase();
	private final String bodyPositionReportPart1 = "4969384c89ef1e7c".toUpperCase();
	private final String bodyPositionReportPart2 = "402f00000000000000000000".toUpperCase();
	private final String tempDir = "/tmp";
	private final Path downloadDir = Paths.get(tempDir, "/dummyuvmsjunittest");
	private final InmarsatFileHandler ifd = new InmarsatFileHandler(downloadDir);

	@SuppressWarnings("FieldCanBeLocal")
	private final boolean deleteDownloadDir = false;
	private Path file1, file2, file3, fileMoved;

	@Before
	public void setup() throws IOException {
		//Clear downloaddir...
		//noinspection ConstantConditions
		if (deleteDownloadDir) {

			Files.walkFileTree(downloadDir, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
					Files.delete(dir);
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					Files.delete(file);
					return FileVisitResult.CONTINUE;
				}
			});
		}

		//Create 2 dummy dat files
		file1 = Paths.get(downloadDir.toString(), "dummyuvmsjunittest1.dat");
		file2 = Paths.get(downloadDir.toString(), "/dummyuvmsjunittest2.dat");
		file3 = Paths.get(downloadDir.toString(), "/dummyuvmsjunittest3.dat");
		fileMoved = Paths.get(downloadDir.toString(), "error", "dummyuvmsjunittest1.dat");
		//Just in case we delete them..
		if (Files.exists(file1)) {
			Files.delete(file1);
		}
		if (Files.exists(file2)) {
			Files.delete(file2);
		}
		if (Files.exists(file3)) {
			Files.delete(file3);
		}

		if (Files.exists(fileMoved)) {
			Files.delete(fileMoved);
		}


		Files.createDirectories(file1.getParent());

	}

	@Test
	public void insertMissingMemberNo() throws Exception {
		//Header with no memberrNO
		byte[] message = ifd.insertMissingMemberNo(InmarsatUtils.hexStringToByteArray(headerDnidNoMemberNo));
		assertEquals((byte) 0xFF, message[20]);
		assertEquals(HeaderType.DNID.getHeaderLength(), message.length);
		assertEquals(headerDnidFFMemberNo, InmarsatUtils.bytesArrayToHexString(message));
	}

	@Test
	public void insertMissingMemberNoHasFF() throws Exception {
		//Header with default FF
		byte[] message = ifd.insertMissingMemberNo(InmarsatUtils.hexStringToByteArray(headerDnidFFMemberNo));
		assertEquals((byte) 0xFF, message[20]);
		assertEquals(HeaderType.DNID.getHeaderLength(), message.length);
		assertEquals(headerDnidFFMemberNo, InmarsatUtils.bytesArrayToHexString(message));
	}

	@Test
	public void insertMissingMemberNoHasMemberNo() throws Exception {
		//Header with member number but not FF
		byte[] message = ifd.insertMissingMemberNo(InmarsatUtils.hexStringToByteArray(headerDnid));
		assertEquals(HeaderType.DNID.getHeaderLength(), message.length);
		assertEquals(headerDnid, InmarsatUtils.bytesArrayToHexString(message));
	}

	@Test
	public void insertMissingMemberNoManyHeaderWithWithout() throws Exception {
		//Headers with/without member number
		String inputMes = headerDnid + headerDnidFFMemberNo + headerDnidNoMemberNo + headerDnidFFMemberNo + headerDnid
				+ headerDnidNoMemberNo;
		String expected = headerDnid + headerDnidFFMemberNo + headerDnidFFMemberNo + headerDnidFFMemberNo + headerDnid
				+ headerDnidFFMemberNo;
		byte[] message = ifd.insertMissingMemberNo(InmarsatUtils.hexStringToByteArray(inputMes));
		assertEquals(expected, InmarsatUtils.bytesArrayToHexString(message));

	}

	@Test
	public void insertMissingMemberNoWithMemberNoAndEOFInMessage() throws Exception {
		//Headers with/without member number
		String inputMes = "01542654011634630E0002440800511AA959F9290C02808A160000010000";
		byte[] message = ifd.insertMissingMemberNo(InmarsatUtils.hexStringToByteArray(inputMes));
		assertEquals(inputMes, InmarsatUtils.bytesArrayToHexString(message));

	}

	@Test
	public void createMessagesFromPath() throws Exception {
		String file1_msg1 = headerDnid + bodyPositionReportPart1 + bodyPositionReportPart2;
		String file2_msg1 = "01542654011634630E0002440800511AA959F9290C02808A160000010000";
		String file2_msg2 = "015426540116EF630800024408005A1AA959F92902808A170000010000"; //missing memberno!
		String file2_msg2FF = "015426540116EF630800024408005A1AA959F929FF02808A170000010000"; //membernoFF
		String file2_msg3 = "015426540116EF630800024408005A1AA959F9290B02808A170000010000";

		String file3_msg1 = "01542654011734630E0002440800511AA959F9290C02808A160000010000"; //Not valid
		String file3_msg2 = "015426540116EF630800024408005A1AA959F92902808A170000010000"; //missing memberno!
		String file3_msg2FF = "015426540116EF630800024408005A1AA959F929FF02808A170000010000"; //membernoFF
		String file3_msg3 = "015426540116EF630800024408005A1AA959F9290B02808A170000010000";


		Files.write(file1, InmarsatUtils.hexStringToByteArray(start), StandardOpenOption.CREATE_NEW);
		Files.write(file1, InmarsatUtils.hexStringToByteArray(file1_msg1), StandardOpenOption.APPEND);
		Files.write(file1, InmarsatUtils.hexStringToByteArray(end), StandardOpenOption.APPEND);

		Files.write(file2, InmarsatUtils.hexStringToByteArray(start), StandardOpenOption.CREATE_NEW);
		Files.write(file2, InmarsatUtils.hexStringToByteArray(file2_msg1), StandardOpenOption.APPEND);
		Files.write(file2, InmarsatUtils.hexStringToByteArray(file2_msg2), StandardOpenOption.APPEND);
		Files.write(file2, InmarsatUtils.hexStringToByteArray(file2_msg3), StandardOpenOption.APPEND);
		Files.write(file2, InmarsatUtils.hexStringToByteArray(end), StandardOpenOption.APPEND);

		Files.write(file3, InmarsatUtils.hexStringToByteArray(start), StandardOpenOption.CREATE_NEW);
		Files.write(file3, InmarsatUtils.hexStringToByteArray(file3_msg1), StandardOpenOption.APPEND);
		Files.write(file3, InmarsatUtils.hexStringToByteArray(file3_msg2), StandardOpenOption.APPEND);
		Files.write(file3, InmarsatUtils.hexStringToByteArray(file3_msg3), StandardOpenOption.APPEND);
		Files.write(file3, InmarsatUtils.hexStringToByteArray(end), StandardOpenOption.APPEND);
		ConcurrentMap<Path, InmarsatMessage[]> messagesFromPath = ifd.createMessages();
		assertEquals(3, messagesFromPath.size());
		assertTrue(messagesFromPath.containsKey(file1));
		assertTrue(messagesFromPath.containsKey(file2));
		assertTrue(messagesFromPath.containsKey(file3));
		int found = 0;
		for (Map.Entry<Path, InmarsatMessage[]> entry : messagesFromPath.entrySet()) {
			if (entry.getKey().equals(file1)) {
				assertEquals(file1_msg1, entry.getValue()[0].getMessageAsHexString());
				found++;
			}
			if (entry.getKey().equals(file2)) {
				assertEquals(file2_msg1, entry.getValue()[0].getMessageAsHexString());
				assertEquals(file2_msg2FF, entry.getValue()[1].getMessageAsHexString());
				assertEquals(file2_msg3, entry.getValue()[2].getMessageAsHexString());
				found++;
			}
			if (entry.getKey().equals(file3)) {
				//file3_msg1 not valid and skould not be pare----
				assertEquals(file3_msg2FF, entry.getValue()[0].getMessageAsHexString());
				assertEquals(file3_msg3, entry.getValue()[1].getMessageAsHexString());
				found++;
			}

		}
		assertTrue(found == 3);
	}

	@Test
	public void byteToInmMessge() throws Exception {
		String iMessageHex = start + headerDnidFFMemberNo + bodyPositionReportPart1 + bodyPositionReportPart2;
		InmarsatMessage iMessage = new InmarsatMessage(InmarsatUtils.hexStringToByteArray(iMessageHex));
		assertEquals(headerDnidFFMemberNo, iMessage.getHeader().getHeaderAsHexString());
		assertEquals(bodyPositionReportPart1 + bodyPositionReportPart2, iMessage.getBody().getBodyAsHexString());
		assertTrue(iMessage.validate());
	}

	@Test
	public void listFiles() throws Exception {
		Files.write(file1, "1".getBytes(), StandardOpenOption.CREATE_NEW);
		Files.write(file2, "2".getBytes(), StandardOpenOption.CREATE_NEW);

		List<Path> listOfFiles = ifd.listFiles(downloadDir);
		assertEquals(2, listOfFiles.size());

		assertEquals(file1, listOfFiles.get(0));
		assertEquals(file2, listOfFiles.get(1));

	}

	@Test
	public void moveFile() throws Exception {
		Files.write(file1, "1".getBytes(), StandardOpenOption.CREATE_NEW);
		Files.write(file2, "2".getBytes(), StandardOpenOption.CREATE_NEW);

		ifd.moveFileToDir(file1, Paths.get(tempDir, InmarsatFileHandler.ERROR_DIR_NAME));
		assertTrue(
				Files.exists(Paths.get(tempDir, InmarsatFileHandler.ERROR_DIR_NAME, file1.getFileName().toString())));

	}

}
