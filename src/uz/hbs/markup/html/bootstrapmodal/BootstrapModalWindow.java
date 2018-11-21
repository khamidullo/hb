package uz.hbs.markup.html.bootstrapmodal;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;

public class BootstrapModalWindow extends Panel {
	private static final long serialVersionUID = 1L;

	private WebMarkupContainer wmc;

	public static String MODAL_PANEL_ID = "modalPanel";

	public BootstrapModalWindow(String id, BootstrapModalPanel modalPanel) {
		super(id);

		wmc = new WebMarkupContainer("wmc") {
			private static final long serialVersionUID = 1L;

			protected void onComponentTag(ComponentTag tag) {
				tag.put("id", getMarkupId());

				super.onComponentTag(tag);
			};
		};
		add(wmc);

		wmc.add(modalPanel);
	}

	public String getModalWindowId() {
		return wmc.getMarkupId();
	}
}
