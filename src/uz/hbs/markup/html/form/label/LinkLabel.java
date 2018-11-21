package uz.hbs.markup.html.form.label;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;

public abstract class LinkLabel extends Panel {
	private static final long serialVersionUID = 1L;

	public LinkLabel(String id, String label) {
		super(id);
		add(new Link<Void>("link"){
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				onLinkClick();
			}
		}.add(new Label("label", label)).setEnabled(isLinkEnabled()));
	}
	
	protected abstract void onLinkClick();
	protected abstract boolean isLinkEnabled();
}
