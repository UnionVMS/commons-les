package fish.focus.uvms.commons.les.inmarsat;

import fish.focus.uvms.commons.les.inmarsat.header.*;
import fish.focus.uvms.commons.les.inmarsat.header.body.PositionReport;
import fish.focus.uvms.commons.les.inmarsat.header.body.PositionReportBits;
import fish.focus.uvms.commons.les.inmarsat.header.body.PositionReportData;
import org.junit.Test;
import java.util.Arrays;
import java.util.Calendar;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InmarsatMessageTest {

	@Test
	public void validate() {
		HeaderData headerData = new HeaderDataBuilder().setType(HeaderType.DNID).setRefno(1)
				.setStoredTime(Calendar.getInstance().getTime()).setDataPresentation(HeaderDataPresentation.TRANS_DATA)
				.createHeaderData();
		InmarsatHeader inmarsatHeader = InmarsatHeader.createHeader(headerData);
		PositionReportData bodyData = new PositionReportData.Builder().setLatHemi(0).setLatDeg(12).setLatMin(2)
				.setLatMinFrac(20).setSpeed(12.0).setCourse(90).createPositionReportData();
		InmarsatBody inmarsatBody = PositionReport.createPositionReport(bodyData);

		InmarsatMessage message = new InmarsatMessage(inmarsatHeader, inmarsatBody);
		assertTrue(message.validate());

		//Negative (inmarsatHeader)
		InmarsatHeader iHeaderClone = InmarsatHeader.createHeader(headerData);
		byte[] headerClone = iHeaderClone.header.clone();
		headerClone[HeaderStruct.POS_HEADER_LENGTH] = 34;
		iHeaderClone.header = headerClone;
		message = new InmarsatMessage(iHeaderClone, inmarsatBody);
		assertFalse(message.validate());

		//Negative (inmarsatBody)
		InmarsatBody inmarsatBodyClone = PositionReport.createPositionReport(bodyData);
		byte[] bodyClone = Arrays.copyOf(inmarsatBodyClone.body, inmarsatBodyClone.body.length - 2);
		inmarsatBodyClone.body = bodyClone;
		message = new InmarsatMessage(inmarsatHeader, inmarsatBodyClone);
		assertFalse(message.validate());

	}

}
