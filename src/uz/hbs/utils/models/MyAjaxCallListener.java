package uz.hbs.utils.models;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.model.StringResourceModel;

public class MyAjaxCallListener extends AjaxCallListener {
	private static final long serialVersionUID = 1L;
	private boolean confirm;
	
	public MyAjaxCallListener(boolean confirm) {
		this.confirm = confirm;
	}
	
	@Override
	public CharSequence getPrecondition(Component component) {
		if (confirm) return "return confirm('" + new StringResourceModel("confirm", null).getString() + "');";
		return super.getPrecondition(component);
	}
}
