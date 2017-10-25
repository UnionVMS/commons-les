package fish.focus.uvms.commons.les.inmarsat.header.body;

import org.junit.Test;
import static fish.focus.uvms.commons.les.inmarsat.header.body.MemCode.*;
import static org.junit.Assert.*;

public class MemCodeTest {
	@Test
	public void getValue() throws Exception {
		assertEquals(0x45, ANTENNA_BLOCKED_OR_JAMMED.getValue());
	}

	@Test
	public void fromInt() throws Exception {
		assertEquals(IO_PIN_EVENT, MemCode.fromInt(IO_PIN_EVENT.getValue()));
		assertNull(MemCode.fromInt(4));
	}
}
