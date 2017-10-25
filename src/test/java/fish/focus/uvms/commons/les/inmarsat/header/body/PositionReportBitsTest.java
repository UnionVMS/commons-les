package fish.focus.uvms.commons.les.inmarsat.header.body;

import org.junit.Test;
import static fish.focus.uvms.commons.les.inmarsat.header.body.PositionReportBits.*;
import static org.junit.Assert.*;

public class PositionReportBitsTest {
	@Test
	public void getValue() throws Exception {
		assertEquals(16, COURSE.getValue());
	}

	@Test
	public void fromInt() throws Exception {
		assertEquals(LAT_MIN_FRAC, PositionReportBits.fromInt(LAT_MIN_FRAC.getValue()));
		assertNotEquals(LAT_MIN_FRAC, PositionReportBits.fromInt(LONG_MIN_FRAC.getValue()));
		assertNull(MemCode.fromInt(99));
	}
}
