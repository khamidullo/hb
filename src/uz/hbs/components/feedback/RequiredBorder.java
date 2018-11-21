package uz.hbs.components.feedback;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.border.BorderBehavior;
import org.apache.wicket.markup.html.form.FormComponent;

public class RequiredBorder extends BorderBehavior {
	private static final long serialVersionUID = 1L;
	
	@Override
	public void afterRender(Component component) {
		FormComponent<?> fc = (FormComponent<?>) component; 
		if (fc.isRequired()) {
			super.afterRender(component);
		}
	}

}
