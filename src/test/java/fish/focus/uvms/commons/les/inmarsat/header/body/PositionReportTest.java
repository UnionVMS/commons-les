package fish.focus.uvms.commons.les.inmarsat.header.body;

import fish.focus.uvms.commons.les.inmarsat.InmarsatUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Arrays;
import java.util.Collection;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

@RunWith(Parameterized.class)
public class PositionReportTest {
	private final static Logger LOGGER = LoggerFactory.getLogger(PositionReportTest.class);


	private final PositionReport positionReport;
	private final byte[] body;
	private final PositionReportData positionReportData;
	private final String bodyHex;


	public PositionReportTest(String bodyHex, PositionReportData positionReportData) {
		this.bodyHex = bodyHex;
		body = InmarsatUtils.hexStringToByteArray(bodyHex);
		positionReport = PositionReport.createPositionReport(body);
		this.positionReportData = positionReportData;

	}

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] {
				{"4969384c89ef1e7c402f00000000000000000000",
						new PositionReportData.Builder().setDataReportFormat(1).setLatHemi(0).setLatDeg(37)
								.setLatMin(41).setLatMinFrac(28).setLongHemi(0).setLongDeg(19).setLongMin(8)
								.setLongMinFrac(76).setMem(111).setMonthRes(0).setDay(7).setHour(19).setMinute(56)
								.setSpeed(12.8).setCourse(94).createPositionReportData()},
				{"01e1908b7469007a000000000000000000000000",
						new PositionReportData.Builder().setDataReportFormat(0).setLatHemi(0).setLatDeg(7).setLatMin(33)
								.setLatMinFrac(72).setLongHemi(0).setLongDeg(34).setLongMin(55).setLongMinFrac(32)
								.setMem(105).setMonthRes(0).setDay(0).setHour(3).setMinute(52).setSpeed(0).setCourse(0)
								.createPositionReportData()},
				{"4def7831f48b1d8a38ba00000000000000000000",
						new PositionReportData.Builder().setDataReportFormat(1).setLatHemi(0).setLatDeg(55)
								.setLatMin(47).setLatMinFrac(60).setLongHemi(0).setLongDeg(12).setLongMin(31)
								.setLongMinFrac(36).setMem(11).setMonthRes(0).setDay(7).setHour(12).setMinute(20)
								.setSpeed(11.2).setCourse(372).createPositionReportData()}});
	}


	@Test
	public void createBodyFromData() throws Exception {
		String bodyFromDataHex = PositionReport.createPositionReport(positionReportData).getBodyAsHexString();
		assertEquals("Body of position report should be same", bodyHex.toUpperCase(), bodyFromDataHex);

	}

	@Test
	public void createBody() throws Exception {
		assertNotNull(positionReport);
	}

	@Test(expected = IllegalArgumentException.class)
	public void createHeaderNegative() {
		byte[] cloneBody = Arrays.copyOf(body, PositionReport.DATA_LENGTH - 1);

		PositionReport positionReportClone = PositionReport.createPositionReport(cloneBody);
		assertFalse(positionReportClone.validate());
	}


	@Test
	public void getDataReportFormat() throws Exception {
		assertEquals(positionReportData.getDataReportFormat(), positionReport.getDataReportFormat());
	}

	@Test
	public void getLatitudeHemisphere() throws Exception {
		assertEquals(positionReportData.getLatHemi(), positionReport.getLatitudeHemisphere());
	}

	@Test
	public void getLatitudeDegrees() {
		assertEquals(positionReportData.getLatDeg(), positionReport.getLatitudeDegrees());
	}

	@Test
	public void getLatitudeMinutes() {
		assertEquals(positionReportData.getLatMin(), positionReport.getLatitudeMinutes());
	}

	@Test
	public void getLatitudeMinuteFraction() {
		assertEquals(positionReportData.getLatMinFrac(), positionReport.getLatitudeMinuteFractions());
	}


	@Test
	public void getLongitudeHemisphere() throws Exception {
		assertEquals(positionReportData.getLongHemi(), positionReport.getLongitudeHemisphere());
	}

	@Test
	public void getLongitudeDegrees() {
		assertEquals(positionReportData.getLongDeg(), positionReport.getLongitudeDegrees());

	}

	@Test
	public void getLongitudeMinutes() {
		assertEquals(positionReportData.getLongMin(), positionReport.getLongitudeMinutes());
	}

	@Test
	public void getLongitudeMinuteFraction() {
		assertEquals(positionReportData.getLongMinFrac(), positionReport.getLongitudeMinuteFractions());
	}

	@Test
	public void getMacroEncodedMessage() {
		assertEquals(positionReportData.getMem(), positionReport.getMacroEncodedMessage());
	}

	@Test
	public void getMonthReserved() {
		assertEquals(positionReportData.getMonthRes(), positionReport.getMonthReserved());
	}

	@Test
	public void getDayOfMonth() {
		assertEquals(positionReportData.getDay(), positionReport.getDayOfMonth());
	}

	@Test
	public void getHour() {
		assertEquals(positionReportData.getHour(), positionReport.getHour());
	}

	@Test
	public void getMinutes() {
		assertEquals(positionReportData.getMinute(), positionReport.getMinutes());
	}

	@Test
	public void getSpeed() {
		assertEquals("Speed should be same", positionReportData.getSpeed(), positionReport.getSpeed(), 0);
		byte[] cloneBody = body.clone();
		cloneBody[8] = (byte) 0xFF;
		PositionReport positionReportClone = PositionReport.createPositionReport(cloneBody);

		assertEquals("Speed should be notdefined", 0, positionReportClone.getSpeed(), 0);
	}

	@Test
	public void getCourse() {
		assertEquals("Course should be same", positionReportData.getCourse(), positionReport.getCourse(), 0);
	}

	@Test
	public void toStringTest() {
		String out = positionReport.toString();
		LOGGER.info(out);
		assertNotNull(out);

	}
}
