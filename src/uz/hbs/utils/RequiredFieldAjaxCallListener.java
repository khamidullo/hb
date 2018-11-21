package uz.hbs.utils;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.model.StringResourceModel;

public class RequiredFieldAjaxCallListener extends AjaxCallListener {
	private static final long serialVersionUID = 1L;
	private String formId;
	private String language;
	private boolean alert;

	public RequiredFieldAjaxCallListener(String formId, String language) {
		this(formId, language, false);
	}
	
	public RequiredFieldAjaxCallListener(String formId, String language, boolean alert) {
		this.formId = formId;
		this.language = language;
		this.alert = alert;
	}
	
	@Override
	public CharSequence getPrecondition(Component component) {
		if (alert) return "if (checkRequiredFields('" + formId +"','" + language + "')) { this.disabled=true; } else { alert('" + new StringResourceModel("required.fields.form", null).getString() + "'); findErrMsg('"+ formId +"'); return false;}" ;
		return "if (checkRequiredFields('" + formId +"','" + language + "')) { this.disabled=true; } else { return false; }" ;
	}
	
	@Override
	public CharSequence getSuccessHandler(Component component) {
		return "this.disabled=false;" ;
	}
	
	@Override
	public CharSequence getFailureHandler(Component component) {
		return "this.disabled=false;" ;
	}
}
