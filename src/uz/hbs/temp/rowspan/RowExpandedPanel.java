package uz.hbs.temp.rowspan;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;

public class RowExpandedPanel extends Panel {
	private static final long serialVersionUID = 1L;

	public RowExpandedPanel(final String id, final Space space) {
		super(id);
		setOutputMarkupId(true);
		final List<String> states = space.getStates();
		add(new ListView<String>("rows", states) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<String> item) {
				if (item.getIndex() == 0) {
					Label name = new Label("name", space.getName());
					name.add(new AttributeModifier("rowspan", states.size() + 1 + ""));
					item.add(name);
					AjaxLink<String> link = new AjaxLink<String>("link") {
						private static final long serialVersionUID = 1L;

						@Override
						public void onClick(AjaxRequestTarget target) {
							RowCollapsedPanel collapsed = new RowCollapsedPanel(id, space);
							RowExpandedPanel.this.replaceWith(collapsed);
							target.add(collapsed);
						}
					};
					link.add(new AttributeModifier("rowspan", states.size() + ""));
					item.add(link);
				} else {
					item.add(new WebMarkupContainer("name").setVisible(false));
					item.add(new WebMarkupContainer("link").setVisible(false));
				}
				String state = item.getModelObject();
				item.add(new Label("state", state));
				item.add(new Label("count", space.getCountForState(state) + ""));
			}
		});
		add(new Label("totalCount", space.getTotalCount()));
	}

}
