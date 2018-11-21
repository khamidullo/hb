package uz.hbs.markup.html.panel;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

public abstract class CheckBoxSlidePanel extends Panel {
	private static final long serialVersionUID = 1L;

	public CheckBoxSlidePanel(String id, Long itemId, boolean isChecked) {
		super(id);
		
		add(new AjaxCheckBox("checkbox", new Model<Boolean>(isChecked)) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				update(target, getModelObject());
			}
			
		}.setMarkupId("checkboxSlideInput" + itemId));
		
		add(new Label("label").add(new AttributeModifier("for", "checkboxSlideInput" + itemId)));
	}
	
	public abstract void update(AjaxRequestTarget target, boolean isChecked);
}
