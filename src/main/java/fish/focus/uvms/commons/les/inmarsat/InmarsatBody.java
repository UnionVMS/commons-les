package fish.focus.uvms.commons.les.inmarsat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class InmarsatBody {
	protected byte[] body = null;

	public String getBodyAsHexString() {
		return InmarsatUtils.bytesArrayToHexString(body);
	}

	public abstract boolean validate();
}
