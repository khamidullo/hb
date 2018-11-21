package uz.hbs.actions.touragent.newbooking;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.beans.Booking;
import uz.hbs.beans.Booking.RoomsCount;
import uz.hbs.utils.CommonUtil;

public class RoomsCountPanel extends Panel {
	private static final long serialVersionUID = 1L;
	private boolean isSmallDesign;

	public RoomsCountPanel(String id, final Booking model, final boolean isSmallDesign) {
		super(id);
		this.isSmallDesign = isSmallDesign;

		LoadableDetachableModel<List<RoomsCount>> listModel = new LoadableDetachableModel<List<RoomsCount>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<RoomsCount> load() {

				if (model.getRoomsCountsList() == null || (model.getRoomsCountsList() != null && model.getRoomsCountsList().size() != model.getRooms())) {
					int count = model.getRooms() == null ? 1 : model.getRooms();

					List<RoomsCount> list = new ArrayList<RoomsCount>();

					for (int i = 0; i < count; i++) {
						list.add(new Booking().new RoomsCount(1, 0));
					}
					model.setRoomsCountsList(list);
				}

				return model.getRoomsCountsList();
			}
		};

		ListView<RoomsCount> listView = new ListView<RoomsCount>("listView", listModel) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<RoomsCount> item) {
				Label roomLabel = new Label("roomLabel", new StringResourceModel("hotels.room", null).getString() + " " + (item.getIndex() + 1));
				item.add(roomLabel);

				DropDownChoice<Integer> adults = new DropDownChoice<Integer>("adults", new PropertyModel<Integer>(item.getModel(), "adults"),
						CommonUtil.getIntegerList(Booking.COUNT_ADULTS, false));
				adults.setNullValid(false);
				item.add(adults);

				DropDownChoice<Integer> children = new DropDownChoice<Integer>("children", new PropertyModel<Integer>(item.getModel(), "children"),
						CommonUtil.getIntegerList(Booking.COUNT_CHILDREN, true));
				children.setNullValid(false);
				item.add(children);

				final WebMarkupContainer childrenAgeContainer = new WebMarkupContainer("childrenAgeContainer") {
					private static final long serialVersionUID = 1L;

					@Override
					public boolean isVisible() {
						return item.getModelObject().getChildren() >= 0;
					}
				};
				childrenAgeContainer.add(new Label("childAgeLabel", new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject() {
						return item.getModelObject().getChildren() > 0 ? new StringResourceModel("touragents.newbooking.child" + (item.getModelObject().getChildren() > 1 ? "ren" : "")
								+ "_age", null).getString() : "";
					}
				}));
				childrenAgeContainer.setOutputMarkupPlaceholderTag(true);
				childrenAgeContainer.add(new ChildrenAgePanel("childrenAgePanel", item.getModelObject(), isSmallDesign));
				//childrenAgeContainer.setVisibilityAllowed(item.getModelObject().getChildren() >= 0);

				children.add(new AjaxFormComponentUpdatingBehavior("onchange") {
					private static final long serialVersionUID = 1L;

					@Override
					protected void onUpdate(AjaxRequestTarget target) {
						childrenAgeContainer.addOrReplace(new ChildrenAgePanel("childrenAgePanel", item.getModelObject(), isSmallDesign));
						childrenAgeContainer.setVisibilityAllowed(true);
						target.add(childrenAgeContainer);
					}
				});
				item.add(childrenAgeContainer);
			}
		}.setReuseItems(true);

		add(listView);
	}

	@Override
	public String getVariation() {
		return isSmallDesign ? "small" : "";
	}
}
