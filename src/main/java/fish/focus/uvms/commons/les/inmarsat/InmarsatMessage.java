package fish.focus.uvms.commons.les.inmarsat;

public class InmarsatMessage {
	private InmarsatHeader header;
	private InmarsatBody body;

	public InmarsatMessage(InmarsatHeader header, InmarsatBody body) {
		this.header = header;
		this.body = body;
	}

	public boolean validate() {
		return header.isValidHeader() && body.validate();
	}
}
