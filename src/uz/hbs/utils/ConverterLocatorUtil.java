package uz.hbs.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.wicket.ConverterLocator;
import org.apache.wicket.util.convert.converter.DateConverter;

import uz.hbs.MyWebApplication;

public class ConverterLocatorUtil extends ConverterLocator {
	private static final long serialVersionUID = 1L;
	
	public ConverterLocatorUtil() {
		set(Date.class, new DateConverter() {
	        private static final long serialVersionUID = 1L;

			@Override
	        public DateFormat getDateFormat(Locale ignore) {
	            return new SimpleDateFormat(MyWebApplication.DATE_FORMAT);
	        }
	    });
	}
}
