package fish.focus.uvms.commons.les.inmarsat.header.body;

import fish.focus.uvms.commons.les.inmarsat.InmarsatConfig;
import fish.focus.uvms.commons.les.inmarsat.InmarsatException;
import fish.focus.uvms.commons.les.inmarsat.InmarsatUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class PositionReportTest {


	private final PositionReport positionReport;
	private final byte[] body;
	private final PositionReportData positionReportData;
	private final String bodyHex;


	public PositionReportTest(String bodyHex, PositionReportData positionReportData) throws InmarsatException {
		this.bodyHex = bodyHex;
		body = InmarsatUtils.hexStringToByteArray(bodyHex);
		positionReport = PositionReport.createPositionReport(body);
		this.positionReportData = positionReportData;

	}

	@Parameterized.Parameters
	public static Collection<Object[]> data() throws InmarsatException {
		return Arrays
				.asList(new Object[][] {
						//@formatter:off
                        {"4969384c89ef1e7c402f00006100000000000000", new PositionReportDataBuilder().setDataReportFormat(1)
                                .setLatPosition(new Position(0, 37, 41, 28))
                                .setLongPosition(new Position(0, 19, 8, 76))
                                .setMem(111).setPositionDate(new PositionDate(7, 19, 56, new PositionDate.PositionDateExtra(2005 - PositionDate.PositionDateExtra.FORMAT1_YEARSTART, Calendar.JUNE+1)))
                                .setSpeedAndCourse(new SpeedAndCourse(12.8, 94)).createPositionReportData()},
                        {"01e1908b7469007a000000000763B14000000000", new PositionReportDataBuilder().setDataReportFormat(0)
                                .setLatPosition(new Position(0, 7, 33, 72))
                                .setLongPosition(new Position(0, 34, 55, 32))
                                .setMem(105).setPositionDate(new PositionDate(0, 3, 52,  new PositionDate.PositionDateExtra(2, 2005 - PositionDate.PositionDateExtra.FORMAT2_YEARSTART, Calendar.JUNE+1,7,12,20)))
                                .setSpeedAndCourse(new SpeedAndCourse(0, 0)).createPositionReportData()},
                        {"4def7831f48b1d8a38ba00003EAB1D8A00000000", new PositionReportDataBuilder().setDataReportFormat(1)
                                .setLatPosition(new Position(0, 55, 47, 60))
                                .setLongPosition(new Position(0, 12, 31, 36))
                                .setMem(11).setPositionDate(new PositionDate(7, 12, 20, new PositionDate.PositionDateExtra(3, 2005, Calendar.JUNE+1,7,12,20)))
                                .setSpeedAndCourse(new SpeedAndCourse(11.2, 372)).createPositionReportData()}
                        //@formatter:on
		});

	}

	@Before
	public void setup() {
		InmarsatConfig.getInstance().setToDefault();
	}

	@After
	public void tearDown() {
		InmarsatConfig.getInstance().setToDefault();
	}

	@Test
	public void createBodyFromData() throws Exception {
		String bodyFromDataHex = PositionReport.createPositionReport(positionReportData, true).getBodyAsHexString();
		assertEquals("Body of position report should be same", bodyHex.toUpperCase().substring(0, 12 * 2),
				bodyFromDataHex.substring(0, 12 * 2)); //ignore extradate

	}

	@Test
	public void createBody() throws Exception {
		assertNotNull(positionReport);
	}

	@Test(expected = InmarsatException.class)
	public void createHeaderNegative() throws InmarsatException {
		byte[] cloneBody =
				Arrays.copyOf(body, PositionReport.DATA_PACKET_1_BYTES + PositionReport.DATA_PACKET_2_BYTES + -1);

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
	public void getSpeed() throws InmarsatException {
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
	public void getLongitude() {
		assertEquals("Longitude should be correct",
				new Position(positionReportData.getLongHemi(), positionReportData.getLongDeg(),
						positionReportData.getLongMin(), positionReportData.getLongMinFrac()).getAsDouble(),
				positionReport.getLongitude().getAsDouble(), 0.0005);

	}

	@Test
	public void getLatitude() {

		assertEquals("Latitude should be correct",
				new Position(positionReportData.getLatHemi(), positionReportData.getLatDeg(),
						positionReportData.getLatMin(), positionReportData.getLatMinFrac()).getAsDouble(),
				positionReport.getLatitude().getAsDouble(), 0);
	}


	@Test
	public void getPositionDateExtra() throws InmarsatException {
		InmarsatConfig.getInstance().setExtraDataEnabled(true);
		PositionDate.PositionDateExtra extraDate = null;
		if (positionReportData.getPositionDate() != null) {
			extraDate = positionReportData.getPositionDate().getExtraDate();
		}
		if (extraDate != null) {
			InmarsatConfig.getInstance().setExtraDataFormat(extraDate.getDateFormat());
		}

		PositionDate.PositionDateExtra positionDateExtra = positionReport.getPositionDateExtra();
		if (positionDateExtra != null) {
			assertEquals(extraDate, positionDateExtra);
			assertEquals(positionReportData.getPositionDate().getDate().getTime(),
					positionDateExtra.getDate(positionReport.getPositionDate()).getTime());

		}
	}

	@Test
	public void toStringTest() {
		String out = positionReport.toString();
		assertNotNull(out);
		assertTrue(out.contains(";speed=" + positionReportData.getSpeed()));
	}


}
