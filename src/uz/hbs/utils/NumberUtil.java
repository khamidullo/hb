package uz.hbs.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

public class NumberUtil {
	public NumberUtil() {
	}

	/* Berilgan string qiymatni sonlardan iboratligini tekshiradi */
	public static boolean isNumber(String nomer) {
		if (nomer == null)
			return false;
		if (nomer.length() <= 0)
			return false;

		if (nomer.indexOf("-") >= 0)
			nomer = nomer.substring(1);

		if (nomer.indexOf(".") == -1) {
			for (int i = 0; i < nomer.length(); ++i) {
				if (!Character.isDigit(nomer.charAt(i))) {
					return false;
				}
			}
		} else {
			String prefix = nomer.substring(0, nomer.indexOf("."));
			String suffix = nomer.substring(nomer.indexOf(".") + 1);
			for (int i = 0; i < prefix.length(); ++i) {
				if (!Character.isDigit(prefix.charAt(i))) {
					return false;
				}
			}
			for (int i = 0; i < suffix.length(); ++i) {
				if (!Character.isDigit(suffix.charAt(i))) {
					return false;
				}
			}
		}

		return true;
	}

	/* Berilgan sekund ni minut va secund formatiga o'tkazib beradi */
	public static String getFormattedMinute(String secoundStr) {
		long secound = Long.parseLong(secoundStr);
		long minute = TimeUnit.SECONDS.toMinutes(secound);
		return minute + "." + (secound - TimeUnit.MINUTES.toSeconds(minute));
	}

	/* Berilgan son double bo'lsa "." dan keyin faqat berilgan "length" dagi sonlar uzunligi qoldiriladi */
	synchronized public static double cutDigitAfterDot(double source) {
		DecimalFormat df = new DecimalFormat("#.##");
		df.setRoundingMode(RoundingMode.DOWN);
		return Double.parseDouble(df.format(source));
	}
	
	synchronized public static String roundUp(String source) {
		BigDecimal bd = new BigDecimal(source);
		DecimalFormat df = new DecimalFormat("#.##");
		df.setRoundingMode(RoundingMode.UP);
		return df.format(bd.doubleValue());
	}
}
