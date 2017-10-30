package fish.focus.uvms.commons.les.inmarsat.header;

import org.junit.Test;
import static org.junit.Assert.*;

public class HeaderStructTest {


	@Test
	public void isPresentation() throws Exception {
		HeaderStruct headerStruct = new HeaderStructBuilder().createHeaderStruct();
		assertTrue(headerStruct.isPresentation());

		headerStruct = new HeaderStructBuilder().disablePresentation().createHeaderStruct();
		assertFalse(headerStruct.isPresentation());
	}

	@Test
	public void isFailure() throws Exception {
		HeaderStruct headerStruct = new HeaderStructBuilder().createHeaderStruct();
		assertTrue(headerStruct.isFailure());

		headerStruct = new HeaderStructBuilder().disableFailure().createHeaderStruct();
		assertFalse(headerStruct.isFailure());
	}

	@Test
	public void isDelivery() throws Exception {
		HeaderStruct headerStruct = new HeaderStructBuilder().createHeaderStruct();
		assertTrue(headerStruct.isDelivery());

		headerStruct = new HeaderStructBuilder().disableDelivery().createHeaderStruct();
		assertFalse(headerStruct.isDelivery());
	}

	@Test
	public void isSatIdAndLesId() throws Exception {
		HeaderStruct headerStruct = new HeaderStructBuilder().createHeaderStruct();
		assertTrue(headerStruct.isSatIdAndLesId());

		headerStruct = new HeaderStructBuilder().disableSatIdAndLesId().createHeaderStruct();
		assertFalse(headerStruct.isSatIdAndLesId());
	}

	@Test
	public void isDataLength() throws Exception {
		HeaderStruct headerStruct = new HeaderStructBuilder().createHeaderStruct();
		assertTrue(headerStruct.isDataLength());

		headerStruct = new HeaderStructBuilder().disableDataLength().createHeaderStruct();
		assertFalse(headerStruct.isDataLength());
	}

	@Test
	public void isDnid() throws Exception {
		HeaderStruct headerStruct = new HeaderStructBuilder().createHeaderStruct();
		assertTrue(headerStruct.isDnid());

		headerStruct = new HeaderStructBuilder().disableDnid().createHeaderStruct();
		assertFalse(headerStruct.isDnid());
	}

	@Test
	public void isMemberNo() throws Exception {
		HeaderStruct headerStruct = new HeaderStructBuilder().createHeaderStruct();
		assertTrue(headerStruct.isMemberNo());

		headerStruct = new HeaderStructBuilder().disableMemberNo().createHeaderStruct();
		assertFalse(headerStruct.isMemberNo());
	}

	@Test
	public void isMesMobNo() throws Exception {
		HeaderStruct headerStruct = new HeaderStructBuilder().createHeaderStruct();
		assertTrue(headerStruct.isMesMobNo());

		headerStruct = new HeaderStructBuilder().disableMesMobNo().createHeaderStruct();
		assertFalse(headerStruct.isMesMobNo());
	}


	@Test
	public void getLength() throws Exception {

		HeaderStruct headerStruct = new HeaderStructBuilder().createHeaderStruct();
		assertEquals(getAllLength(), headerStruct.getHeaderLength());

		headerStruct = new HeaderStructBuilder().disableMesMobNo().createHeaderStruct();
		assertEquals(getAllLength() - HeaderByte.MES_MOB_NO.getNoOfBytes(), headerStruct.getHeaderLength());

		headerStruct = new HeaderStructBuilder().disablePresentation().disableFailure().disableDelivery()
				.disableSatIdAndLesId().disableDataLength().disableDnid().disableMemberNo().disableMesMobNo()
				.createHeaderStruct();
		assertEquals(getBaseLength(), headerStruct.getHeaderLength());

		headerStruct = new HeaderStructBuilder().disableMesMobNo().disableFailure().createHeaderStruct();
		assertEquals(getAllLength() - HeaderByte.MES_MOB_NO.getNoOfBytes() - HeaderByte.FAILURE_RESON.getNoOfBytes(),
				headerStruct.getHeaderLength());

	}

	@Test
	public void getLengthAllEnabled() throws Exception {

		HeaderStruct headerStruct = new HeaderStructBuilder().enableAll().createHeaderStruct();
		assertEquals(getAllLength(), headerStruct.getHeaderLength());

	}

	private int getAllLength() {
		return getBaseLength() + HeaderByte.PRESENTATION.getNoOfBytes() + HeaderByte.FAILURE_RESON.getNoOfBytes()
				+ HeaderByte.DELIVERY_ATTEMPTS.getNoOfBytes() + HeaderByte.SATID_AND_LESID.getNoOfBytes()
				+ HeaderByte.DATA_LENGTH.getNoOfBytes() + HeaderByte.DNID.getNoOfBytes()
				+ HeaderByte.MEMBER_NO.getNoOfBytes() + HeaderByte.MES_MOB_NO.getNoOfBytes();
	}

	private int getBaseLength() {
		return HeaderByte.START_OF_HEADER.getNoOfBytes() + HeaderByte.LEAD_TEXT.getNoOfBytes()
				+ HeaderByte.TYPE.getNoOfBytes() + HeaderByte.HEADER_LENGTH.getNoOfBytes()
				+ HeaderByte.MSG_REF_NO.getNoOfBytes() + HeaderByte.STORED_TIME.getNoOfBytes()
				+ HeaderByte.END_OF_HEADER.getNoOfBytes();
	}


	@Test
	public void getPostionStartOfHeader() {
		HeaderStruct headerStruct = new HeaderStructBuilder().disableAll().createHeaderStruct();
		assertEquals(HeaderByte.START_OF_HEADER.getNoOfBytes(),
				headerStruct.getPostionStartOfHeader() + HeaderByte.START_OF_HEADER.getNoOfBytes());

	}

	@Test
	public void getPostionLeadText() {
		HeaderStruct headerStruct = new HeaderStructBuilder().disableAll().createHeaderStruct();
		assertEquals(headerStruct.getPostionStartOfHeader() + HeaderByte.START_OF_HEADER.getNoOfBytes(),
				headerStruct.getPostionLeadText());
	}

	@Test
	public void getPostionType() {
		HeaderStruct headerStruct = new HeaderStructBuilder().disableAll().createHeaderStruct();
		assertEquals(headerStruct.getPostionLeadText() + HeaderByte.LEAD_TEXT.getNoOfBytes(),
				headerStruct.getPostionType());

	}

	@Test
	public void getPostionHeaderLength() {
		HeaderStruct headerStruct = new HeaderStructBuilder().disableAll().createHeaderStruct();
		assertEquals(headerStruct.getPostionType() + HeaderByte.TYPE.getNoOfBytes(),
				headerStruct.getPostionHeaderLength().intValue());

	}

	@Test
	public void getPostionMsgRefNo() {
		HeaderStruct headerStruct = new HeaderStructBuilder().disableAll().createHeaderStruct();
		assertEquals(headerStruct.getPostionHeaderLength() + HeaderByte.HEADER_LENGTH.getNoOfBytes(),
				headerStruct.getPostionMsgRefNo().intValue());

	}

	@Test
	public void getPostionPresentation() {
		HeaderStruct headerStruct = new HeaderStructBuilder().disableAll().enablePresentation().createHeaderStruct();
		assertEquals(headerStruct.getPostionMsgRefNo() + HeaderByte.MSG_REF_NO.getNoOfBytes(),
				headerStruct.getPostionPresentation().intValue());

	}

	@Test
	public void getPostionFailureReason() {
		HeaderStruct headerStruct = new HeaderStructBuilder().disableAll().enableFailure().createHeaderStruct();
		assertEquals(headerStruct.getPostionMsgRefNo() + HeaderByte.MSG_REF_NO.getNoOfBytes(),
				headerStruct.getPostionFailureReason().intValue());
	}

	@Test
	public void getPostionDeliveryAttempt() {
		HeaderStruct headerStruct = new HeaderStructBuilder().disableAll().enableDelivery().createHeaderStruct();
		assertEquals(headerStruct.getPostionMsgRefNo() + HeaderByte.MSG_REF_NO.getNoOfBytes(),
				headerStruct.getPostionDeliveryAttempts().intValue());
	}

	@Test
	public void getPostionSatIdAndLesId() {
		HeaderStruct headerStruct = new HeaderStructBuilder().disableAll().enableSatIdAndLesId().createHeaderStruct();
		assertEquals(headerStruct.getPostionMsgRefNo() + HeaderByte.MSG_REF_NO.getNoOfBytes(),
				headerStruct.getPostionSatIdAndLesId().intValue());
	}

	@Test
	public void getPostionSatIdAndLesIdNotEnabled() {
		HeaderStruct headerStruct = new HeaderStructBuilder().disableAll().createHeaderStruct();
		assertNull(headerStruct.getPostionSatIdAndLesId());
	}

	@Test
	public void getPostionDataLength() {
		HeaderStruct headerStruct = new HeaderStructBuilder().disableAll().enableDataLength().createHeaderStruct();
		assertEquals(headerStruct.getPostionMsgRefNo() + HeaderByte.MSG_REF_NO.getNoOfBytes(),
				headerStruct.getPostionDataLength().intValue());
	}

	@Test
	public void getPostionStoredTime() {
		HeaderStruct headerStruct = new HeaderStructBuilder().disableAll().createHeaderStruct();
		assertEquals(headerStruct.getPostionMsgRefNo() + HeaderByte.MSG_REF_NO.getNoOfBytes(),
				headerStruct.getPostionStoredTime().intValue());
	}

	@Test
	public void getPostionDnid() {
		HeaderStruct headerStruct = new HeaderStructBuilder().disableAll().enableDnid().createHeaderStruct();
		assertEquals(headerStruct.getPostionStoredTime() + HeaderByte.STORED_TIME.getNoOfBytes(),
				headerStruct.getPostionDnid().intValue());
	}

	@Test
	public void getPostionMemberNo() {
		HeaderStruct headerStruct = new HeaderStructBuilder().disableAll().enableMemberNo().createHeaderStruct();
		assertEquals(headerStruct.getPostionStoredTime() + HeaderByte.STORED_TIME.getNoOfBytes(),
				headerStruct.getPostionMemberNo().intValue());
	}

	@Test
	public void getPostionMesMobNo() {
		HeaderStruct headerStruct = new HeaderStructBuilder().disableAll().enableMesMobNo().createHeaderStruct();
		assertEquals(headerStruct.getPostionStoredTime() + HeaderByte.STORED_TIME.getNoOfBytes(),
				headerStruct.getPostionMesMobNo().intValue());
	}

	@Test
	public void getPostionEndOfHeader() {
		HeaderStruct headerStruct = new HeaderStructBuilder().disableAll().createHeaderStruct();
		assertEquals(headerStruct.getPostionStoredTime() + HeaderByte.STORED_TIME.getNoOfBytes(),
				headerStruct.getPostionEndOfHeader().intValue());
	}
}
