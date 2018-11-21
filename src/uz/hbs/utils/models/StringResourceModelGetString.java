package uz.hbs.utils.models;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;

public class StringResourceModelGetString extends StringResourceModel {
	private static final long serialVersionUID = 1L;

	public StringResourceModelGetString(String resourceKey, IModel<?> model, String defaultValue, Object[] parameters) {
		super(resourceKey, model, defaultValue, parameters);
	}

	public StringResourceModelGetString(String resourceKey, IModel<?> model, Object[] parameters) {
		super(resourceKey, model, parameters);
	}

	public StringResourceModelGetString(String resourceKey, Component component, IModel<?> model, String defaultValue, Object[] parameters) {
		super(resourceKey, component, model, defaultValue, parameters);
	}

	public StringResourceModelGetString(String resourceKey, Component component, IModel<?> model, Object[] parameters) {
		super(resourceKey, component, model, parameters);
	}
	
	@Override
	public String toString() {
		return getString();
	}
}
