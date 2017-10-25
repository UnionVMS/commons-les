package fish.focus.uvms.commons.les.inmarsat.header;

import fish.focus.uvms.commons.les.inmarsat.header.HeaderType;
import org.junit.Test;
import static fish.focus.uvms.commons.les.inmarsat.header.HeaderType.*;
import static org.junit.Assert.*;

public class HeaderTypeTest {
	@Test
	public void getValue() throws Exception {
		assertEquals(1, DNID.getValue());


	}

	@Test
	public void getLength() throws Exception {
		assertEquals(22, DNID.getLength());
	}


	@Test
	public void fromInt() throws Exception {
		assertEquals(HeaderType.fromInt(1), HeaderType.DNID);
		assertNotEquals(HeaderType.fromInt(2), HeaderType.DNID);

	}

}
