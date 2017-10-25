package fish.focus.uvms.commons.les.inmarsat.header.body;

public class PositionReportData {

	private final int dataReportFormat;
	private final int latHemi;
	private final int latDeg;
	private final int latMin;
	private final int latMinFrac;
	private final int longHemi;
	private final int longDeg;
	private final int longMin;
	private final int longMinFrac;
	private final int mem;
	private final int monthRes;
	private final int day;
	private final int hour;
	private final int minute;
	private final double speed;
	private final int course;

	private PositionReportData(int dataReportFormat, int latHemi, int latDeg, int latMin, int latMinFrac, int longHemi,
			int longDeg, int longMin, int longMinFrac, int mem, int monthRes, int day, int hour, int minute,
			double speed, int course) {
		this.dataReportFormat = dataReportFormat;
		this.latHemi = latHemi;
		this.latDeg = latDeg;
		this.latMin = latMin;
		this.latMinFrac = latMinFrac;
		this.longHemi = longHemi;
		this.longDeg = longDeg;
		this.longMin = longMin;
		this.longMinFrac = longMinFrac;
		this.mem = mem;
		this.monthRes = monthRes;
		this.day = day;
		this.hour = hour;
		this.minute = minute;
		this.speed = speed;
		this.course = course;
	}

	public int getDataReportFormat() {
		return dataReportFormat;
	}

	public int getLatHemi() {
		return latHemi;
	}

	public int getLatDeg() {
		return latDeg;
	}

	public int getLatMin() {
		return latMin;
	}

	public int getLatMinFrac() {
		return latMinFrac;
	}

	public int getLongHemi() {
		return longHemi;
	}

	public int getLongDeg() {
		return longDeg;
	}

	public int getLongMin() {
		return longMin;
	}

	public int getLongMinFrac() {
		return longMinFrac;
	}

	public int getMem() {
		return mem;
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

	public double getSpeed() {
		return speed;
	}

	public int getCourse() {
		return course;
	}

	public static class Builder {
		private int dataReportFormat;
		private int latHemi;
		private int latDeg;
		private int latMin;
		private int latMinFrac;
		private int longHemi;
		private int longDeg;
		private int longMin;
		private int longMinFrac;
		private int mem;
		private int monthRes;
		private int day;
		private int hour;
		private int minute;
		private double speed;
		private int course;


		public Builder setDataReportFormat(int dataReportFormat) {
			this.dataReportFormat = dataReportFormat;
			return this;
		}

		public Builder setLatHemi(int latHemi) {
			this.latHemi = latHemi;
			return this;
		}

		public Builder setLatDeg(int latDeg) {
			this.latDeg = latDeg;
			return this;
		}

		public Builder setLatMin(int latMin) {
			this.latMin = latMin;
			return this;
		}

		public Builder setLatMinFrac(int latMinFrac) {
			this.latMinFrac = latMinFrac;
			return this;
		}

		public Builder setLongHemi(int longHemi) {
			this.longHemi = longHemi;
			return this;
		}

		public Builder setLongDeg(int longDeg) {
			this.longDeg = longDeg;
			return this;
		}

		public Builder setLongMin(int longMin) {
			this.longMin = longMin;
			return this;
		}

		public Builder setLongMinFrac(int longMinFrac) {
			this.longMinFrac = longMinFrac;
			return this;
		}

		public Builder setMem(int mem) {
			this.mem = mem;
			return this;
		}

		public Builder setMonthRes(int monthRes) {
			this.monthRes = monthRes;
			return this;
		}

		public Builder setDay(int day) {
			this.day = day;
			return this;
		}

		public Builder setHour(int hour) {
			this.hour = hour;
			return this;
		}

		public Builder setMinute(int minute) {
			this.minute = minute;
			return this;
		}

		public Builder setSpeed(double speed) {
			this.speed = speed;
			return this;
		}

		public Builder setCourse(int course) {
			this.course = course;
			return this;
		}

		public PositionReportData createPositionReportData() {
			return new PositionReportData(dataReportFormat, latHemi, latDeg, latMin, latMinFrac, longHemi, longDeg,
					longMin, longMinFrac, mem, monthRes, day, hour, minute, speed, course);
		}
	}
}
