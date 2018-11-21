package uz.hbs.markup.html.form.label;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

public abstract class AjaxLinkLabel extends Panel {
	private static final long serialVersionUID = 1L;

	public AjaxLinkLabel(String id, String label) {
		super(id);
		add(new AjaxLink<Void>("link") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				onLinkClick(target);
			}
		}.add(new Label("label", label).setEscapeModelStrings(false)).setEnabled(isLinkEnabled()));
	}
	
	protected abstract void onLinkClick(AjaxRequestTarget target); 
	protected abstract boolean isLinkEnabled();
}
