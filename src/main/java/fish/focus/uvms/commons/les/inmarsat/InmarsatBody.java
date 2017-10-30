package fish.focus.uvms.commons.les.inmarsat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class InmarsatBody {
	private static final Logger LOGGER = LoggerFactory.getLogger(InmarsatBody.class);
	protected byte[] body = null;

	public InmarsatBody() {}

	/**
	 * Given valid body a new Inmarsat body will be returned
	 *
	 * @param body bytes
	 * @throws IllegalArgumentException if not valid body
	 */
	public InmarsatBody(InmarsatHeader headerForBody, byte[] body) throws InmarsatException {
		this.body = body;
		if (!validate(headerForBody)) {
			throw new InmarsatException("Body is not valid");
		}

	}

	public boolean validate(InmarsatHeader headerForBody) {
		//Check length of body
		if (headerForBody.getDataLength() != body.length) {
			LOGGER.debug("Body [} is to short given the header datalength:{}", getBodyAsHexString(),
					headerForBody.getDataLength());
			return false;
		}
		return true;
	}

	public String getBodyAsHexString() {
		return InmarsatUtils.bytesArrayToHexString(body);
	}

	public abstract boolean validate();
}
