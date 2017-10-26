package fish.focus.uvms.commons.les.inmarsat.header.body;

public class PositionReportData {

	private final int dataReportFormat;
	private final Position latPosition;
	private final Position longPosition;
	private final int mem;
	private final PositionDate positionDate;
	private final SpeedAndCourse speedAndCourse;

	private PositionReportData(int dataReportFormat, Position latPosition, Position longPosition, int mem,
			PositionDate positionDate, SpeedAndCourse speedAndCourse) {
		this.dataReportFormat = dataReportFormat;
		this.latPosition = latPosition;
		this.longPosition = longPosition;
		this.mem = mem;
		this.positionDate = positionDate;
		this.speedAndCourse = speedAndCourse;

	}

	public int getDataReportFormat() {
		return dataReportFormat;
	}

	public int getLatHemi() {
		return latPosition.getHemi();
	}

	public int getLatDeg() {
		return latPosition.getDeg();
	}

	public int getLatMin() {
		return latPosition.getMin();
	}

	public int getLatMinFrac() {
		return latPosition.getMinFrac();
	}

	public int getLongHemi() {
		return longPosition.getHemi();
	}

	public int getLongDeg() {
		return longPosition.getDeg();
	}

	public int getLongMin() {
		return longPosition.getMin();
	}

	public int getLongMinFrac() {
		return longPosition.getMinFrac();
	}

	public int getMem() {
		return mem;
	}

	public int getMonthRes() {
		return positionDate.getMonthRes();
	}

	public int getDay() {
		return positionDate.getDay();
	}

	public int getHour() {
		return positionDate.getHour();
	}

	public int getMinute() {
		return positionDate.getMinute();
	}

	public double getSpeed() {
		return speedAndCourse.getSpeed();
	}

	public int getCourse() {
		return speedAndCourse.getCourse();
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
			return new PositionReportData(dataReportFormat, new Position(latHemi, latDeg, latMin, latMinFrac),
					new Position(longHemi, longDeg, longMin, longMinFrac), mem,
					new PositionDate(monthRes, day, hour, minute), new SpeedAndCourse(speed, course));
		}
	}
}
