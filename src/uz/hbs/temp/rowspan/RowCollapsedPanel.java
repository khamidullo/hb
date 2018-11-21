package uz.hbs.temp.rowspan;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

public class RowCollapsedPanel extends Panel {
	private static final long serialVersionUID = 1L;

	public RowCollapsedPanel(final String id, final Space space) {
		super(id);
		setOutputMarkupId(true);
		add(new Label("name", space.getName()));
		add(new AjaxLink<String>("link") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				RowExpandedPanel expanded = new RowExpandedPanel(id, space);
				RowCollapsedPanel.this.replaceWith(expanded);
				target.add(expanded);
			}
		});
		add(new Label("totalCount", space.getTotalCount() + ""));
	}
}
