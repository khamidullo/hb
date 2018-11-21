package uz.hbs.markup.html.bootstrapmodal;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;

public class BootstrapModalBehavior extends Behavior {
	private static final long serialVersionUID = 1L;
	private String modalWindowId;

	public BootstrapModalBehavior(String modalWindowId) {
		this.modalWindowId = modalWindowId;
	}

	@Override
	public void onComponentTag(Component component, ComponentTag tag) {
		tag.put("data-toggle", "modal");
		tag.put("data-target", "#" + modalWindowId);
		tag.remove("href");
		super.onComponentTag(component, tag);
	}
//
//	@Override
//	public void renderHead(Component component, IHeaderResponse response) {
//		response.render(JavaScriptHeaderItem.forScript("$('#" + modalWindowId + "').modal()", "dsfsdf"));
//		super.renderHead(component, response);
//	}
}
