package uz.hbs.actions.touragent.newbooking;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.beans.Booking;
import uz.hbs.beans.Booking.Age;
import uz.hbs.beans.Booking.RoomsCount;
import uz.hbs.utils.CommonUtil;

public class ChildrenAgePanel extends Panel {
	private static final long serialVersionUID = 1L;

	public ChildrenAgePanel(String id, final RoomsCount model, final boolean isSmallDesign) {
		super(id);

		LoadableDetachableModel<List<Age>> listModel = new LoadableDetachableModel<List<Age>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<Age> load() {

				int count = model.getChildren() == null ? 0 : model.getChildren();

				if (model.getAgesList() == null || (model.getAgesList() != null && model.getAgesList().size() != count)) {
					List<Age> list = new ArrayList<Age>();

					for (int i = 0; i < count; i++) {
						list.add(new Booking().new Age());
					}

					if (count > 0) {
						model.setAgesList(list);
					}
				}
				if (count == 0) {
					model.setAgesList(new ArrayList<Age>());
				}
				return model.getAgesList();
			}
		};

		ListView<Age> listView = new ListView<Age>("listView", listModel) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<Age> item) {
//				item.add(new Label("numberLabel", item.getIndex() + 1));

				DropDownChoice<Integer> age = new DropDownChoice<Integer>("age", new PropertyModel<Integer>(item.getModel(), "age"),
						CommonUtil.getIntegerList(Booking.COUNT_CHILD_AGE, true), new IChoiceRenderer<Integer>() {
							private static final long serialVersionUID = 1L;

							@Override
							public Object getDisplayValue(Integer object) {
								return object == 0 ? "<1" : object;
							}

							@Override
							public String getIdValue(Integer object, int index) {
								return object.toString();
							}
						});
				age.setNullValid(false);
				age.setRequired(true);
				age.setLabel(new StringResourceModel("touragents.newbooking.child_age", null));
				age.add(new AttributeModifier("required", "required"));
				item.add(age);
				item.add(new AttributeModifier("class", isSmallDesign ? "col-md-4" : "col-md-3"));
			}
		}.setReuseItems(true);

		add(listView);
	}
}
