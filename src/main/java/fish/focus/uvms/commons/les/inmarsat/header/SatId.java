package fish.focus.uvms.commons.les.inmarsat.header;


public enum SatId {
	AORE(1), AORW(0), POR(2), IOR(3);
	private final int value;

	SatId(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static SatId fromInt(int code) {
		for (SatId type : SatId.values()) {
			if (type.value == code) {
				return type;
			}
		}

		return null;
	}
}
