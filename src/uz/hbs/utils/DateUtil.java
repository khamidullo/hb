package uz.hbs.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.wicket.model.StringResourceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uz.hbs.MyWebApplication;

/**
 * Created by IntelliJ IDEA. User: mss Date: Apr 16, 2007 Time: 10:47:16 PM
 */
public class DateUtil {
	private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);
	public static String toString(Date date, String pattern) {
		if (date == null)
			return null;
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}

	public static Date parseDate(String date, String pattern) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.parse(date);
	}

	public static boolean isLastDayOfMonth(Date date) {
		int day = Integer.parseInt(toString(date, "dd"));
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int maxDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);

		if (day == maxDay)
			return true;

		return false;
	}

	public static int getWeekDay(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

		return dayOfWeek;
	}

	public static String getWeekDayName(Date date, boolean isShort) {
		return new StringResourceModel("week.day." + getWeekDay(date) + (isShort ? ".short" : ""), null).getString();
	}

	/**
	 * Bayram va haftaning dam olish kunlarini aniqlaydi
	 * 
	 * 1 января – Новый год
	 * 8 марта – День женщин
	 * 21 марта – Праздник Навруз
	 * 9 мая – День Памяти и Почестей
	 * 1 сентября – День Независимости
	 * 1 октября – День учителя и наставника
	 * 8 декабря – День Конституции
	 */
	public static boolean isHolidayAndWeekEnd(Date date) {
		int weekDay = getWeekDay(date);
		if (weekDay == Calendar.SATURDAY || weekDay == Calendar.SUNDAY) {
			return true;
		}

		String dateStr = toString(date, "ddMM");

		switch (dateStr) {
			case "0101":
			case "0803":
			case "2103":
			case "0905":
			case "0109":
			case "0110":
			case "0812":
				return true;
			default:
				return false;
		}
	}
	
	public static Date getDate(Date date, String time) {
		try {
			return DateUtil.parseDate(DateUtil.toString(date, MyWebApplication.DATE_FORMAT) + " " + time, MyWebApplication.DATE_TIME_SHORT_FORMAT);
		} catch (ParseException e) {
			logger.error("Exception", e);
			return null;
		}
	}
}
