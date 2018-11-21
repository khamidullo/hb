package uz.hbs.components.validators;

import java.math.BigDecimal;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.wicket.util.convert.converter.BigDecimalConverter;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

public abstract class BigDecimalMoneyValidator implements IValidator<BigDecimal> {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private void checkDecimalSeparator(String value, Locale sessionLocale) {
		char decimalSeperator = new DecimalFormatSymbols(sessionLocale).getDecimalSeparator();
		Pattern p = Pattern.compile("^\\d+(\\" + decimalSeperator + "\\d{1,2})?$");
		Matcher m = p.matcher(value);

		if (!m.find()) {
			throw new NumberFormatException("Invalid price.");
		}
	}

	abstract protected Locale getSessionLocale();

	@Override
	public void validate(IValidatable<BigDecimal> validatable) {
		try {
			@SuppressWarnings("unused")
			BigDecimal bigDecimal = parseBigDecimal(validatable.getValue().toString(), getSessionLocale());
//			checkDecimalSeparator(validatable.getValue().toString(), getSessionLocale());
		} catch (Exception e) {
			error(validatable,"BigDecimalMoneyValidator");
		}
	}
	
	public static BigDecimal parseBigDecimal(String s, Locale locale) {
		BigDecimalConverter converter = new BigDecimalConverter(){
			private static final long serialVersionUID = 1L;

			@Override
			public BigDecimal convertToObject(String value, Locale locale) {
				if ("ru".equals(locale.getLanguage())) {
		            value = value.replace('.', ',');
		        }
		        return super.convertToObject(value, locale);				
			}
		};
		BigDecimal bigDecimal = (BigDecimal) converter.convertToObject(s, locale);
		return bigDecimal;
	}
	
	private void error(IValidatable<BigDecimal> validatable, String errorKey) {
		ValidationError error = new ValidationError();
		error.addKey(errorKey);
		validatable.error(error);
	}
}
