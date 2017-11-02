package fish.focus.uvms.commons.les.inmarsat.header.body;

import fish.focus.uvms.commons.les.inmarsat.InmarsatDefinition;
import fish.focus.uvms.commons.les.inmarsat.InmarsatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Position date in the following format:
 * Day (5 bits): Value: 0 - 31 (Day of the month)
 * Hour (5 bits): Value: 0 - 23 (Hour of the day)
 * Minutes(5 bits): Value 0 - 29 (Minute within the hour given in units of 2 minutes)
 **/
public class PositionDate {

	private static final Logger LOGGER = LoggerFactory.getLogger(PositionDate.class);
	private final int day;
	private final int hour;
	private final int minute;
	private final PositionDateExtra extraDate;//optional

	/**
	 * Create a position date (without extradate)
	 *
	 * @param day    Value: 0 - 31 (Day of the month)
	 * @param hour   Value: 0 - 23 (Hour of the day)
	 * @param minute Value 0 - 29 (Minute within the hour given in units of 2 minutes)
	 * @throws InmarsatException if not valid day/hour/minute
	 */
	public PositionDate(int day, int hour, int minute) throws InmarsatException {
		this(day, hour, minute, null);
	}

	/**
	 * Create a position date with optional extradate
	 *
	 * @param day       Value: 0 - 31 (Day of the month)
	 * @param hour      Value: 0 - 23 (Hour of the day)
	 * @param minute    Value 0 - 29 (Minute within the hour given in units of 2 minutes)
	 * @param extraDate optional extradate
	 * @throws InmarsatException if not valid date
	 */
	public PositionDate(int day, int hour, int minute, PositionDateExtra extraDate) throws InmarsatException {
		this.day = day;
		this.hour = hour;
		this.minute = minute;
		this.extraDate = extraDate;

		if (!validate() || ((extraDate != null) && !(extraDate.validate()))) {
			LOGGER.warn("Not valid date for position - day:{}, hour:{}, min:{}", day, hour, minute);
			throw new InmarsatException("Not valid date for position");
		}

	}

	@SuppressWarnings("SimplifiableIfStatement")
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		PositionDate that = (PositionDate) o;

		if (day != that.day)
			return false;
		if (hour != that.hour)
			return false;
		//noinspection SimplifiableIfStatement
		if (minute != that.minute)
			return false;
		return extraDate != null ? extraDate.equals(that.extraDate) : that.extraDate == null;
	}

	@Override
	public int hashCode() {
		int result = day;
		result = 31 * result + hour;
		result = 31 * result + minute;
		result = 31 * result + (extraDate != null ? extraDate.hashCode() : 0);
		return result;
	}

	/**
	 * Validate day/hour/min
	 *
	 * @return true if day/hour/minute is in correct range
	 */
	public boolean validate() {
		if ((0 <= day && day <= 31) || (0 <= hour && hour <= 23) || (0 <= minute && minute <= 29)) {
			return true;
		}

		LOGGER.debug("Not valid date for position - day:{}, hour:{}, min:{}", day, hour, minute);
		return false;
	}

	@SuppressWarnings("SameReturnValue")
	public int getMonthRes() {
		return 0;
	}

	/**
	 * Day (5 bits)
	 *
	 * @return Value: 0 - 31 (Day of the month)
	 */
	public int getDay() {
		return day;
	}

	/**
	 * Hour (5 bits)
	 *
	 * @return Value: 0 - 23 (Hour of the day)
	 */
	public int getHour() {
		return hour;
	}

	/**
	 * Minute (5 bits)
	 *
	 * @return Value 0 - 29 (Minute within the hour given in units of 2 minutes)
	 */
	public int getMinute() {
		return minute;
	}

	/**
	 * Minute * 2
	 *
	 * @return Value (0-58)
	 */
	public int getRealMinute() {
		return minute * 2;
	}

	public Calendar getNowUtc() {
		return Calendar.getInstance(InmarsatDefinition.API_TIMEZONE);
	}

	public Date getDate() {
		return getDate(getNowUtc());
	}


	public Date getDate(final Calendar now) {
		if (extraDate != null) {
			return extraDate.getDate(this);
		}

		Calendar dateTime = Calendar.getInstance();
		dateTime.clear();
		dateTime.setTimeZone(InmarsatDefinition.API_TIMEZONE);
		int nowYear = now.get(Calendar.YEAR);
		int nowMonth = now.get(Calendar.MONTH);
		int nowDay = now.get(Calendar.DAY_OF_MONTH);
		int nowHour = now.get(Calendar.HOUR);
		int nowMin = now.get(Calendar.MINUTE);

		int posYear = nowYear;
		int posMonth = nowMonth;
		final int posDay = getDay();
		final int posHour = getHour();
		final int posRealMin = getRealMinute();

		if (posDay > nowDay || ((posHour * 100 + posRealMin) > (nowHour * 100 + nowMin))) {
			//not in current month ("posdate>nowdate")
			if (posMonth == Calendar.JANUARY) {
				posYear--;
			} else {
				posMonth--;
				Calendar dummy = (Calendar) dateTime.clone();
				//noinspection MagicConstant
				dummy.set(posDay, posMonth, 1);
				if (dummy.getActualMaximum(Calendar.DAY_OF_MONTH) < posDay) {
					//Two month old or previous year
					if (posMonth == 0) {
						posYear--;
					} else {
						posMonth--;
					}
				}
			}
		}

		//noinspection MagicConstant
		dateTime.set(posYear, posMonth, posDay, posHour, posRealMin);
		return dateTime.getTime();

	}

	public PositionDateExtra getExtraDate() {
		return extraDate;
	}

	@Override
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		sdf.setTimeZone(InmarsatDefinition.API_TIMEZONE);
		return "PositionDate-day=" + day + ";hour=" + hour + ";minute=" + minute + ";extraDate="
				+ (extraDate != null ? extraDate : "") + sdf.format(getDate());
	}

	/**
	 * Extra date info
	 * <h2>Date Format 1</h2>
	 * <p>
	 * Uses two extra bytes in the position report.<br/>
	 * Adds additional information about year and month.<br/>
	 * Month uses 4 bits and the range is 1 – 12  (January to December)<br/>
	 * Year uses 6 bits and range is 0 – 63 calculated from year 1997 (1997 – 2060)</p>
	 * <hr/>
	 * <h2>Date Format 2</h2>
	 * <p>Uses four extra bytes in the position report.<br/>
	 * Adds additional information about year, month, day, hour and minutes.<br/>
	 * Year uses 7 bits and the range is 0 - 99 calculated from year 1998 (1998 - 2097)<br/>
	 * Month uses 4 bits and range is 1 – 12 (January – December)<br/>
	 * Day uses 5 bits and range is 1 – 31<br/>
	 * Hour uses 5 bits and range is 0 – 23<br/>
	 * Minute uses 6 bits and range is 0 – 59 </p>
	 * <hr/>
	 * <p>
	 * <hr/>
	 * <h2>Date Format 3</h2>
	 * <p>Uses five bytes up in the position report.<br/>
	 * Adds additional information about year, month, day, hour and minutes.<br/>
	 * Year uses 12 bits and the range is 0 – 4095<br/>
	 * Month uses 4 bits and range is 1 – 12 (January – December) <br/>
	 * Day uses 5 bits and range is 1 – 31<br/>
	 * Hour uses 5 bits and range is 0 – 23<br/>
	 * Minutes uses 6 bits and range is 0 – 59</p>
	 * <hr/>
	 **/
	static class PositionDateExtra {
		public static final int FORMAT1_YEARSTART = 1997;
		public static final int FORMAT2_YEARSTART = 1998;
		private final int dateFormat;//(1-3)
		private final int year;
		private final int month;
		private int day;
		private final int hour;
		private final int minute;

		/**
		 * Create extradate in dateformat 1
		 *
		 * @param year  0 – 63 calculated from year 1997 (1997 – 2060)
		 * @param month 1 – 12  (January to December)
		 */
		public PositionDateExtra(int year, int month) {
			this.dateFormat = 1;
			this.year = year;
			this.month = month;
			this.hour = 0;
			this.minute = 0;
		}



		/**
		 * Create extradate in dateformat 1, 2 or 3
		 *
		 * @param dateFormat 1,2 or 3
		 * @param year       0 – 63 (1997 – 2060) for format1, 0-99 (1998 - 2097) for format2, 0 – 4095 for firm
		 * @param month      1 – 12 (January – December) (format 2 & 3)
		 * @param day        1 – 31 (format 2 & 3)
		 * @param hour       0 – 23  (format 2 & 3)
		 * @param min        0 – 59  (format 2 & 3)
		 */
		public PositionDateExtra(int dateFormat, int year, int month, int day, int hour, int min) {
			this.dateFormat = dateFormat;
			this.year = year;
			this.month = month;
			this.day = day;
			this.hour = hour;
			this.minute = min;
		}

		public static boolean validMonth(int month) {
			return (1 <= month) && (month <= 12);
		}

		public static boolean validDayHourMinute(int day, int hour, int minute) {
			return ((1 <= day) && (day <= 31)) && ((0 <= hour) && (hour <= 23)) && ((0 <= minute) && (minute <= 59));
		}

		public static boolean validFormat1(int year, int month) {
			return ((0 <= year) && (year <= 63)) && validMonth(month);
		}

		public static boolean validFormat2(int year, int month, int day, int hour, int minute) {
			return ((0 <= year) && (year <= 99)) && validMonth(month) && validDayHourMinute(day, hour, minute);
		}

		public static boolean validFormat3(int year, int month, int day, int hour, int minute) {
			return ((0 <= year) && (year <= 4095)) && validMonth(month) && validDayHourMinute(day, hour, minute);
		}

		public int getDateFormat() {
			return dateFormat;
		}

		public boolean validate() {
			switch (dateFormat) {
				case 1:
					return validFormat1(year, month);
				case 2:
					return validFormat2(year, month, day, hour, minute);
				case 3:
					return validFormat3(year, month, day, hour, minute);
				default:
					return false;
			}

		}

		@SuppressWarnings("MagicConstant")
		public Date getDate(PositionDate baseDate) {
			Calendar dateTime = Calendar.getInstance();
			dateTime.clear();
			dateTime.setTimeZone(InmarsatDefinition.API_TIMEZONE);
			if (dateFormat == 1) {
				//Extra: Year, month
				dateTime.set(FORMAT1_YEARSTART + year, month - 1, baseDate.getDay(), baseDate.getHour(),
						baseDate.getRealMinute());

			} else if (dateFormat == 2) {
				//Extra: Year, month
				dateTime.set(FORMAT2_YEARSTART + year, month - 1, baseDate.getDay(), baseDate.getHour(),
						baseDate.getRealMinute());

			} else if (dateFormat == 3) {
				dateTime.set(year, month - 1, baseDate.getDay(), baseDate.getHour(), baseDate.getRealMinute());

			}
			return dateTime.getTime();
		}

		@SuppressWarnings("SimplifiableIfStatement")
		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;

			PositionDateExtra that = (PositionDateExtra) o;

			if (dateFormat != that.dateFormat)
				return false;
			if (year != that.year)
				return false;
			if (month != that.month)
				return false;
			if (day != that.day)
				return false;
			//noinspection SimplifiableIfStatement
			if (hour != that.hour)
				return false;
			return minute == that.minute;
		}

		@Override
		public int hashCode() {
			int result = dateFormat;
			result = 31 * result + year;
			result = 31 * result + month;
			result = 31 * result + day;
			result = 31 * result + hour;
			result = 31 * result + minute;
			return result;
		}

		@Override
		public String toString() {
			return "PositionDateExtra-dateFormat=" + dateFormat + ";year=" + year + ";month=" + month + ";day=" + day
					+ ";hour=" + hour + ";minute=" + minute;
		}
	}
}
