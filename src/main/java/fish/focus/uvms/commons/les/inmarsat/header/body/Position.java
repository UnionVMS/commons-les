package fish.focus.uvms.commons.les.inmarsat.header.body;

public class Position {
	private final int hemisphere;
	private final int degree;
	private final int minute;
	private final int minuteFraction;

	public Position(int hemisphere, int degree, int minute, int minuteFraction) {
		this.hemisphere = hemisphere;
		this.degree = degree;
		this.minute = minute;
		this.minuteFraction = minuteFraction;
	}

	public int getHemi() {
		return hemisphere;
	}

	public int getDeg() {
		return degree;
	}

	public int getMin() {
		return minute;
	}

	public int getMinFrac() {
		return minuteFraction;
	}
}

