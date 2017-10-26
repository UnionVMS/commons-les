package fish.focus.uvms.commons.les.inmarsat;

public class InmarsatMessage {
	private final InmarsatHeader header;
	private final InmarsatBody body;

	public InmarsatMessage(InmarsatHeader header, InmarsatBody body) {
		this.header = header;
		this.body = body;
	}

	public boolean validate() {
		return header.isValidHeader() && body.validate();
	}
}
