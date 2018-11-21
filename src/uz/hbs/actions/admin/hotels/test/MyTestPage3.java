package uz.hbs.actions.admin.hotels.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.LoadableDetachableModel;

import uz.hbs.beans.Action;
import uz.hbs.beans.IdAndValue;
import uz.hbs.enums.ActionRight;
import uz.hbs.template.MyPage;

public class MyTestPage3 extends MyPage {
	private static final long serialVersionUID = 1L;
	private int id = -1; 
	private int rowspan = 1;
	private WebMarkupContainer wmc;
	
	public MyTestPage3() {
		final List<IdAndValue> list = new ArrayList<IdAndValue>();
		list.add(new IdAndValue(1, "Value 1"));
		list.add(new IdAndValue(1, "Value 1"));
		list.add(new IdAndValue(1, "Value 1"));
		list.add(new IdAndValue(1, "Value 1"));
		list.add(new IdAndValue(1, "Value 1"));
		list.add(new IdAndValue(2, "Value 2"));
		list.add(new IdAndValue(2, "Value 2"));
		list.add(new IdAndValue(2, "Value 2"));
		list.add(new IdAndValue(3, "Value 3"));
		list.add(new IdAndValue(3, "Value 3"));
		list.add(new IdAndValue(3, "Value 3"));
		list.add(new IdAndValue(3, "Value 3"));
		list.add(new IdAndValue(4, "Value 4"));
		list.add(new IdAndValue(5, "Value 5"));
		
		add(new ListView<IdAndValue>("list", new LoadableDetachableModel<List<IdAndValue>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<IdAndValue> load() {
				return list;
			}
		}) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<IdAndValue> item) {
				try {
					final IdAndValue obj = (IdAndValue) item.getDefaultModelObject();
					WebMarkupContainer container = new WebMarkupContainer("td1");
					if (rowspan > 1 && id != obj.getId()) {
						wmc.add(new AttributeModifier("rowspan", rowspan));
					}
					if (id == -1 || id != obj.getId()) {
						container.add(new Label("id", obj.getId()));
						id = obj.getId();
						rowspan = 1;
						wmc = container;
					} else {
						container.add(new Label("id", obj.getId())).setVisible(false);
						rowspan++;
					}
					item.add(container);
					item.add(new Label("name", obj.getValue()));
				} finally {
					if (rowspan > 1) {
						wmc.add(new AttributeModifier("rowspan", rowspan));
					}
				}
			}
		});
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
		return "Test page 3";
	}
}
