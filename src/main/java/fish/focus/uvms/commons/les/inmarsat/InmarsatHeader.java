package fish.focus.uvms.commons.les.inmarsat;


import fish.focus.uvms.commons.les.inmarsat.header.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.ByteBuffer;
import java.util.Date;
import static fish.focus.uvms.commons.les.inmarsat.InmarsatDefintion.*;

public class InmarsatHeader {
	private static final Logger LOGGER = LoggerFactory.getLogger(InmarsatHeader.class);

	byte[] header;

	private InmarsatHeader() {}

	public static InmarsatHeader createHeader(byte[] header) throws InmarsatException {
		InmarsatHeader iHeader = new InmarsatHeader();
		iHeader.header = header;
		if (iHeader.validate()) {
			return iHeader;

		} else {
			LOGGER.debug("Not a valid header: {}", header);
			throw new InmarsatException("Not a valid header");
		}

	}

	public static InmarsatHeader createHeader(HeaderData headerData) throws InmarsatException {
		InmarsatHeader iHeader = new InmarsatHeader();

		byte[] h = new byte[headerData.getType().getHeaderLength()];

		h[HeaderStruct.POS_START_OF_HEADER_POS] = InmarsatDefintion.API_SOH;
		h[HeaderStruct.POS_LEAD_TEXT_0] = InmarsatDefintion.API_LEAD_TEXT.getBytes()[0];
		h[HeaderStruct.POS_LEAD_TEXT_1] = InmarsatDefintion.API_LEAD_TEXT.getBytes()[1];
		h[HeaderStruct.POS_LEAD_TEXT_2] = InmarsatDefintion.API_LEAD_TEXT.getBytes()[2];

		h[HeaderStruct.POS_TYPE] = (byte) headerData.getType().getValue();
		h[HeaderStruct.POS_HEADER_LENGTH] = (byte) headerData.getType().getHeaderLength();

		byte[] refNoBytes = ByteBuffer.allocate(4).putInt(headerData.getRefno()).array();
		h[HeaderStruct.POS_REF_NO_START] = refNoBytes[3];
		h[HeaderStruct.POS_REF_NO_START + 1] = refNoBytes[2];
		h[HeaderStruct.POS_REF_NO_START + 2] = refNoBytes[1];
		h[HeaderStruct.POS_REF_NO_END] = refNoBytes[0];
		h[headerData.getType().getHeaderLength() - 1] = InmarsatDefintion.API_EOH;

		if (headerData.getType().getHeaderStruct().isPresentation()) {
			h[headerData.getType().getHeaderStruct().getPostionPresentation()] =
					(byte) headerData.getDataPresentation().getValue();
		}
		if (headerData.getType().getHeaderStruct().isFailure()) {
			h[headerData.getType().getHeaderStruct().getPostionFailureReason()] = (byte) headerData.getFailureReason();
		}
		if (headerData.getType().getHeaderStruct().isDelivery()) {
			h[headerData.getType().getHeaderStruct().getPostionDeliveryAttempts()] =
					(byte) headerData.getDeliveryAttempts();
		}
		if (headerData.getType().getHeaderStruct().isSatIdAndLesId()) {
			h[headerData.getType().getHeaderStruct().getPostionSatIdAndLesId()] =
					createSatIdAndLesIdByte(headerData.getSatIdAndLesId());
		}

		if (headerData.getType().getHeaderStruct().isDataLength()) {
			byte[] bytes =
					InmarsatUtils.int2ByteArray(headerData.getDataLength(), HeaderByte.DATA_LENGTH.getNoOfBytes());
			h[headerData.getType().getHeaderStruct().getPostionDataLength()] = bytes[0];
			h[headerData.getType().getHeaderStruct().getPostionDataLength() + 1] = bytes[1];
		}

		byte[] storeTimeBytes = createStoreTimeByte(headerData.getStoredTime());
		h[headerData.getType().getHeaderStruct().getPostionStoredTime()] = storeTimeBytes[0];
		h[headerData.getType().getHeaderStruct().getPostionStoredTime() + 1] = storeTimeBytes[1];
		h[headerData.getType().getHeaderStruct().getPostionStoredTime() + 2] = storeTimeBytes[2];
		h[headerData.getType().getHeaderStruct().getPostionStoredTime() + 3] = storeTimeBytes[3];

		if (headerData.getType().getHeaderStruct().isDnid()) {
			byte[] bytes = InmarsatUtils.int2ByteArray(headerData.getDnid(), HeaderByte.DNID.getNoOfBytes());
			h[headerData.getType().getHeaderStruct().getPostionDnid()] = bytes[0];
			h[headerData.getType().getHeaderStruct().getPostionDnid() + 1] = bytes[1];
		}

		if (headerData.getType().getHeaderStruct().isMemberNo()) {
			h[headerData.getType().getHeaderStruct().getPostionMemberNo()] = (byte) headerData.getMemNo();
		}

		if (headerData.getType().getHeaderStruct().isMesMobNo()) {
			byte[] bytes = InmarsatUtils.int2ByteArray(headerData.getMesNo(), HeaderByte.MES_MOB_NO.getNoOfBytes());
			h[headerData.getType().getHeaderStruct().getPostionMesMobNo()] = bytes[0];
			h[headerData.getType().getHeaderStruct().getPostionMesMobNo() + 1] = bytes[1];
			h[headerData.getType().getHeaderStruct().getPostionMesMobNo() + 2] = bytes[2];
			h[headerData.getType().getHeaderStruct().getPostionMesMobNo() + 3] = bytes[3];

		}

		iHeader.header = h;

		if (iHeader.validate()) {
			return iHeader;

		} else {
			LOGGER.debug("Not a valid header: {}", h);
			throw new InmarsatException("Not a valid header");
		}

	}

	public static boolean validate(byte[] headerToValidate) {

		if (!isValidHeaderLength(headerToValidate)) {
			LOGGER.debug("header validation failed is either null or to short");
			return false;
		} else if (!isValidHeaderBase(headerToValidate)) {
			LOGGER.debug("header validation failed format check");
			return false;
		}

		return true;
	}

	private static boolean isValidHeaderBase(byte[] headerToValidate) {
		return headerToValidate != null && API_SOH == headerToValidate[HeaderStruct.POS_START_OF_HEADER_POS]
				&& API_LEAD_TEXT.getBytes()[0] == headerToValidate[HeaderStruct.POS_LEAD_TEXT_0]
				&& API_LEAD_TEXT.getBytes()[1] == headerToValidate[HeaderStruct.POS_LEAD_TEXT_1]
				&& API_LEAD_TEXT.getBytes()[2] == headerToValidate[HeaderStruct.POS_LEAD_TEXT_2]
				&& API_EOH == headerToValidate[headerToValidate.length - 1];

	}

	private static boolean isValidHeaderLength(byte[] headerToValidate) {
		if (headerToValidate != null) {
			HeaderType type = HeaderType.fromInt(headerToValidate[HeaderStruct.POS_TYPE]);
			if (type != null && (HeaderStruct.POS_REF_NO_END + 1) < headerToValidate.length //MIN LENGTH
					&& headerToValidate[HeaderStruct.POS_HEADER_LENGTH] == headerToValidate.length
					&& type.getHeaderLength() == headerToValidate.length) {
				return true;
			}
		}

		return false;
	}

	public static byte createSatIdAndLesIdByte(int satIdAndLesId) {
		int first = satIdAndLesId % 10;
		int second = (satIdAndLesId - first) % 100 / 10;
		int third = (satIdAndLesId - first - second) % 1000 / 100;

		return createSatIdAndLesIdByte(SatId.fromInt(third), satIdAndLesId - third * 100);
	}

	public static byte createSatIdAndLesIdByte(SatId satId, int lesId) {
		byte bSatId = (byte) satId.getValue();
		byte bLesId = (byte) lesId;
		String sSatId = InmarsatUtils.byteToZeroPaddedString(bSatId);
		String sLesId = InmarsatUtils.byteToZeroPaddedString(bLesId);
		String sSatIdAndLesId = sSatId.substring(6, 8) + sLesId.substring(2);

		return (byte) Integer.parseInt(sSatIdAndLesId, 2);
	}

	private static byte[] createStoreTimeByte(Date storedTime) {
		long seconds = storedTime.getTime() / 1000;
		return InmarsatUtils.int2ByteArray((int) seconds, 4);
	}

	public static boolean isStartOfMessage(byte[] message, int i) {

		return API_SOH == message[i] //@formatter:off
                && API_LEAD_TEXT.getBytes()[0] == message[i + 1]
                && API_LEAD_TEXT.getBytes()[1] == message[i + 2]
                && API_LEAD_TEXT.getBytes()[2] == message[i + 3];
        //@formatter:on
	}

	public boolean validate() {
		return validate(header);
	}

	public String getHeaderAsHexString() {
		return InmarsatUtils.bytesArrayToHexString(header);
	}

	@SuppressWarnings("SameReturnValue")
	public byte getStartOfHeader() {
		return API_SOH;
	}

	public byte[] getLeadText() {
		return API_LEAD_TEXT.getBytes(API_CHARSET);
	}

	public HeaderType getType() {
		return HeaderType.fromInt(header[HeaderStruct.POS_TYPE]);
	}

	public int getHeaderLength() {
		return InmarsatUtils.byteToUnsignedInt(header[HeaderStruct.POS_HEADER_LENGTH]);
	}

	public int getRefNo() {
		return (int) InmarsatUtils.toUnsignedInt(header, HeaderStruct.POS_REF_NO_START, HeaderStruct.POS_REF_NO_END);
	}

	public HeaderDataPresentation getDataPresentation() {
		Integer position = getType().getHeaderStruct().getPostionPresentation();
		if (position == null) {
			return null;
		}

		return HeaderDataPresentation.fromInt(InmarsatUtils.byteToUnsignedInt(header[position]));
	}

	public int getFailureReason() {
		Integer position = getType().getHeaderStruct().getPostionFailureReason();
		if (position == null) {
			return 0;
		}
		return InmarsatUtils.byteToUnsignedInt(header[position]);
	}

	public int getDeliveryAttempts() {
		Integer position = getType().getHeaderStruct().getPostionDeliveryAttempts();
		if (position == null) {
			return 0;
		}
		return InmarsatUtils.byteToUnsignedInt(header[position]);
	}


	public int getSatIdAndLesId() {
		Integer position = getType().getHeaderStruct().getPostionSatIdAndLesId();
		if (position == null) {
			return 0;
		}
		return Integer.parseInt(String.format("%d%d", getSatId(position).getValue(), getLesId(position)));
	}

	public int getLesId(int position) {
		String bin = InmarsatUtils.byteToZeroPaddedString(header[position]);
		return Integer.parseInt(bin.substring(2), 2);
	}

	public SatId getSatId(int position) {
		String bin = InmarsatUtils.byteToZeroPaddedString(header[position]);
		return SatId.fromInt(Integer.parseInt(bin.substring(0, 2), 2));
	}

	public int getDataLength() {

		Integer position = getType().getHeaderStruct().getPostionDataLength();
		if (position == null) {
			return 0;
		}

		return InmarsatUtils.toUnsignedShort(header, position, position + HeaderByte.DATA_LENGTH.getNoOfBytes() - 1);
	}

	public Date getStoredTime() {
		Integer position = getType().getHeaderStruct().getPostionStoredTime();
		return new Date(
				InmarsatUtils.toUnsignedInt(header, position, position + HeaderByte.STORED_TIME.getNoOfBytes() - 1)
						* 1000);
	}


	public int getDnid() {
		Integer position = getType().getHeaderStruct().getPostionDnid();
		if (position == null) {
			return 0;
		}

		return InmarsatUtils.toUnsignedShort(header, position, position + HeaderByte.DNID.getNoOfBytes() - 1);
	}


	public int getMemberNo() {
		Integer position = getType().getHeaderStruct().getPostionMemberNo();
		if (position == null) {
			return 0;
		}

		return InmarsatUtils.byteToUnsignedInt(header[position]);
	}

	public long getMesMobNo() {
		Integer position = getType().getHeaderStruct().getPostionMesMobNo();
		if (position == null) {
			return 0;
		}

		return InmarsatUtils.toUnsignedInt(header, position, position + HeaderByte.MES_MOB_NO.getNoOfBytes() - 1);
	}

	@SuppressWarnings("SameReturnValue")
	public byte getEndOfHeader() {
		return API_EOH;
	}


}

