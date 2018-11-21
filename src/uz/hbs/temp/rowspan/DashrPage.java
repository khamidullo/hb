package uz.hbs.temp.rowspan;

import java.util.List;
import java.util.Map;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.PropertyModel;

import uz.hbs.beans.Action;
import uz.hbs.enums.ActionRight;
import uz.hbs.template.MyPage;

public class DashrPage extends MyPage {
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private int total;

	public DashrPage() {
		List<Space> spaces = DashrService.getInstance().getSpace();
		add(new ListView<Space>("spaces", spaces) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<Space> item) {
				item.add(new RowCollapsedPanel("rowPanel", item.getModelObject()));
				total += item.getModelObject().getTotalCount();
			}
		});
		add(new Label("total", new PropertyModel<Integer>(this, "total")));
	}

	@Override
	public boolean isAuthorized(Map<ActionRight, Action> actionMap) {
		return getMySession().isSignedIn();
	}

	@Override
	public Class<?> implementedClass() {
		return this.getClass();
	}

	@Override
	public String getTitle() {
		return "Test rowspan";
	}
}
