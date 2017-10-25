package fish.focus.uvms.commons.les.inmarsat.header.body;

public class PositionDate {
	private final int monthRes;
	private final int day;
	private final int hour;
	private final int minute;

	public PositionDate(int monthRes, int day, int hour, int minute) {
		this.monthRes = monthRes;
		this.day = day;
		this.hour = hour;
		this.minute = minute;
	}

	public int getMonthRes() {
		return monthRes;
	}

	public int getDay() {
		return day;
	}

	public int getHour() {
		return hour;
	}

	public int getMinute() {
		return minute;
	}
}
