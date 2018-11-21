package uz.hbs.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.antlr.stringtemplate.AttributeRenderer;

public class FormatUtil {

	public static double format(double d) {
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		nf.setGroupingUsed(false);
		return Double.parseDouble(nf.format(d));
	}
	
	public static float format(float f) {
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		nf.setGroupingUsed(false);
		return Float.parseFloat(nf.format(f));
	}

	public static String toString(Date date, String pattern) {
		if (date == null || pattern == null)
			return "";
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}

	public static String toString(Double d) {
		if (d == null) return "";
		DecimalFormat nf = new DecimalFormat("#,##.##");
		DecimalFormatSymbols dfs = nf.getDecimalFormatSymbols();
		nf.setGroupingUsed(false);
		nf.setDecimalFormatSymbols(dfs);
		return nf.format(d);
	}

	public static String toString(Integer i) {
		if (i == null) return "";
		DecimalFormat nf = new DecimalFormat("#,##0");
		DecimalFormatSymbols dfs = nf.getDecimalFormatSymbols();
		nf.setGroupingUsed(false);
		nf.setDecimalFormatSymbols(dfs);
		return nf.format(i);
	}
	
	public static String toStringGrouping(double i) {
		DecimalFormat nf = new DecimalFormat("#,##0.00");
		DecimalFormatSymbols dfs = nf.getDecimalFormatSymbols();
		nf.setGroupingUsed(true);
		nf.setDecimalFormatSymbols(dfs);
		return nf.format(i);
	}
	

	public static String roundUp(Double d) {
		if (d == null) return "";
		DecimalFormat nf = new DecimalFormat("#,##.##");
		DecimalFormatSymbols dfs = nf.getDecimalFormatSymbols();
		nf.setGroupingUsed(false);
		nf.setRoundingMode(RoundingMode.CEILING);
		nf.setDecimalFormatSymbols(dfs);
		return nf.format(d);
	}

	public static String twoDigitFormat(byte d) {
		return (d < 10 ? "0" : "")+ d;
	}
	
	public static String format2(double d) {
		return toString(d);
	}
	
	public static AttributeRenderer getDoubleRenderer() {
		return new FormatUtil().new DoubleRenderer();
	}

	public static AttributeRenderer getBigDecimalRenderer() {
		return new FormatUtil().new BigDecimalRenderer();
	}
	
	private class DoubleRenderer implements AttributeRenderer {
		@Override
		public String toString(Object o) {
			if (o == null) {
				return null;
			} else if (o instanceof Double) {
				return FormatUtil.toString((Double) o);
			} else {
				return o.toString();
			}
		}

		@Override
		public String toString(Object o, String formatName) {
			return toString(o);
		}
	}

	private class BigDecimalRenderer implements AttributeRenderer {
		@Override
		public String toString(Object o) {
			if (o == null) {
				return null;
			} else if (o instanceof BigDecimal) {
				return FormatUtil.toString(((BigDecimal) o).doubleValue());
			} else {
				return o.toString();
			}
		}
		
		@Override
		public String toString(Object o, String formatName) {
			return toString(o);
		}
	}
}
