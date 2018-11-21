package uz.hbs.utils;

import java.io.Serializable;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.wicket.model.IModel;

public class BundleUtil implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public BundleUtil() {
	}

	public String value(String key, IModel<Locale> locale) {
		ResourceBundle rb = ResourceBundle.getBundle("uz/hbs/MyWebApplication", locale.getObject());
		return rb.getString(key);
	}
	
	public String configValue(String key) {
		ResourceBundle rb = ResourceBundle.getBundle("Config");
		return rb.getString(key);
	}
}
