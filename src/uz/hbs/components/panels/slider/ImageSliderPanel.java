package uz.hbs.components.panels.slider;

import java.util.List;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;

import uz.hbs.beans.KeyAndValue;

public class ImageSliderPanel extends Panel {
	private static final long serialVersionUID = 1L;

	public ImageSliderPanel(String id, List<KeyAndValue> imgLinkList) {
		super(id);

		ListView<KeyAndValue> carouselElement = new ListView<KeyAndValue>("element", imgLinkList) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<KeyAndValue> item) {
				item.add(new ContextImage("image", item.getModelObject().getKey()));
				item.add(new ContextImage("thumb", item.getModelObject().getKey()));
			}
		};
		add(carouselElement);
		
		setVisible(imgLinkList.size() > 0);
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(CssHeaderItem.forUrl("css/jssor.css"));
		response.render(JavaScriptHeaderItem.forUrl("js/ie10-viewport-bug-workaround.js", "ie10_id"));
		// response.render(JavaScriptHeaderItem.forUrl("js/jssor.slider.mini.js"));
		response.render(JavaScriptHeaderItem.forUrl("js/jssor.js", "jssor_id"));
		response.render(JavaScriptHeaderItem.forUrl("js/jssor.slider.js", "jssor_slide_id"));
	}
}
