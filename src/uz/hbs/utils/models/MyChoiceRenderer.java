package uz.hbs.utils.models;

import org.apache.wicket.markup.html.form.ChoiceRenderer;

public class MyChoiceRenderer<T> extends ChoiceRenderer<T> {
	private static final long serialVersionUID = 1L;
	
	@Override
	public Object getDisplayValue(T object) {
		return object;
	}
	
	@Override
	public String getIdValue(T object, int index) {
		return String.valueOf(object);
	}
}
