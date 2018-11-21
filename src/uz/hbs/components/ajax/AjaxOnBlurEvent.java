package uz.hbs.components.ajax;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;

public class AjaxOnBlurEvent extends AjaxFormComponentUpdatingBehavior {
	private static final long serialVersionUID = 1L;

	public AjaxOnBlurEvent() {
		super("onblur");
	}

	@Override
	protected void onUpdate(AjaxRequestTarget target) {
	}
}
