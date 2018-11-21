package uz.hbs.actions.admin.hotels.test;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.attributes.IAjaxCallListener;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.util.value.ValueMap;

public class MyTestPage2 extends WebPage {
	private static final long serialVersionUID = 1L;

	public MyTestPage2() {
		Form<ValueMap> form;
		add(form = new Form<ValueMap>("defaultForm"));
		form.setMarkupId("defaultForm");
		
		form.add(new AjaxButton("ajaxbtn") {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				super.onSubmit(target, form);
			}
			
			@Override
			protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
				super.updateAjaxAttributes(attributes);
				IAjaxCallListener listener = new AjaxCallListener(){
					private static final long serialVersionUID = 1L;
					
					@Override
					public CharSequence getBeforeHandler(Component component) {
						return "valid();";					
					}
				};
				attributes.getAjaxCallListeners().add(listener);
			}
		});
	}
}
