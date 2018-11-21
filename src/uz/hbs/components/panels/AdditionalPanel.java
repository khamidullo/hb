package uz.hbs.components.panels;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.attributes.IAjaxCallListener;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;

public abstract class AdditionalPanel<T> extends Panel {
	private static final long serialVersionUID = 1L;

	public AdditionalPanel(String id, T object) {
		super(id);
		add(new MyForm("form", object));
	}
	
	private class MyForm extends Form<T>{
		private static final long serialVersionUID = 1L;
		private MyFeedbackPanel feedback;

		public MyForm(String id, T object) {
			super(id, new CompoundPropertyModel<T>(object));
			add(feedback = new MyFeedbackPanel("feedback"));
			RequiredTextField<String> name;
			add(name = new RequiredTextField<String>("name"));
			name.setLabel(new StringResourceModel("resource.name", null));
			add(new IndicatingAjaxButton("submit") {
				private static final long serialVersionUID = 1L;

				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
					super.onError(target, form);
					target.add(feedback);
				}
				
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					onSave(target, form);
				}
				
				@Override
				protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
					super.updateAjaxAttributes(attributes);
					IAjaxCallListener listener = new AjaxCallListener(){
						private static final long serialVersionUID = 1L;

						@Override
						public CharSequence getSuccessHandler(Component component) {
							String js = onJavaScript();
							if (js != null) return js;
							return super.getSuccessHandler(component);
						}
					};
					attributes.getAjaxCallListeners().add(listener);
				}
			});
		}
		
	}
	
	protected abstract void onSave(AjaxRequestTarget target, Form<?> form);
	protected abstract String onJavaScript();
}
