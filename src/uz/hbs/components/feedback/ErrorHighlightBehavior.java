package uz.hbs.components.feedback;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.FormComponent;

public class ErrorHighlightBehavior extends Behavior {
	private static final long serialVersionUID = 1L;

	public void onComponentTag(Component c, ComponentTag tag){
		FormComponent<?> fc = (FormComponent<?>) c;
		if (!fc.isValid()){
			tag.put("class", "error");
		}
	}
}
