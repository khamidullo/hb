package uz.hbs.actions.hotel.reservations.panels;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import uz.hbs.beans.Guest;
import uz.hbs.beans.ReservationDetail;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.utils.FormatUtil;
import uz.hbs.utils.models.GenderModel;

public class ViewGuestDetailsPanel extends Panel {
	private static final long serialVersionUID = 1L;

	public ViewGuestDetailsPanel(String id, final IModel<ReservationDetail> model) {
		super(id, model);

		add(new ListView<Guest>("guestlist", new LoadableDetachableModel<List<Guest>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<Guest> load() {
				return new MyBatisHelper().selectList("selectGuestsList", model.getObject().getId());
			}
		}) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<Guest> item) {
				Guest guest = (Guest) item.getDefaultModelObject();
				WebMarkupContainer collapseHeadingContainer = new WebMarkupContainer("collapseHeadingContainer");
				collapseHeadingContainer.add(new AttributeModifier("id", "collapseHeading" + String.valueOf(item.getIndex() + 1)));

				Link<Void> collapseLink = new Link<Void>("collapseLink") {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
					}
				};
				collapseLink.add(new AttributeModifier("aria-controls", "collapseBody" + String.valueOf(item.getIndex() + 1)));
				collapseLink.add(new AttributeModifier("href", "#collapseBody" + String.valueOf(item.getIndex() + 1)));
				collapseLink.add(new Label("guest", new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject() {
						if (item.getIndex() == 0)
							return getString("hotels.guest.details.guest.main");
						return getString("hotels.guest.details.guest") + " #" + String.valueOf(item.getIndex() + 1);
					}
				}));
				collapseLink.add(new Label("name", guest.getFirst_name() + " " + guest.getLast_name()));

				collapseHeadingContainer.add(collapseLink);

				item.add(collapseHeadingContainer);

				WebMarkupContainer collapseBodyContainer = new WebMarkupContainer("collapseBodyContainer");
				collapseBodyContainer.add(new AttributeModifier("id", "collapseBody" + String.valueOf(item.getIndex() + 1)));
				collapseBodyContainer.add(new AttributeModifier("aria-labelledby", "collapseHeading" + String.valueOf(item.getIndex() + 1)));

				item.add(collapseBodyContainer);

				WebMarkupContainer relationship_container = new WebMarkupContainer("relationship_container");
				relationship_container.setVisible(item.getIndex() > 0);
				relationship_container.add(new Label("relationship", guest.getRelationship()));
				collapseBodyContainer.add(relationship_container);

				collapseBodyContainer.add(new Label("date_and_place_of_birth", guest.getDate_and_place_of_birth()));
				collapseBodyContainer.add(new Label("gender", GenderModel.getGenderValue(guest.getGender().getId())));
				collapseBodyContainer.add(new Label("nationality", guest.getNationality() != null ? guest.getNationality().getName() : ""));
				collapseBodyContainer.add(new Label("passport_number", guest.getPassport_number()));
				collapseBodyContainer.add(new Label("passport_date_of_issue", FormatUtil.toString(guest.getPassport_date_of_issue(), "dd/MM/yyyy")));
				collapseBodyContainer.add(new Label("passport_issue_place", guest.getPassport_issue_place()));
				collapseBodyContainer.add(new Label("address", guest.getAddress()));
				collapseBodyContainer.add(new Label("country", guest.getCountry() != null ? guest.getCountry().getName() : ""));
				collapseBodyContainer.add(new Label("region", guest.getRegion() != null ? guest.getRegion().getName() : ""));
				collapseBodyContainer.add(new Label("city", guest.getCity()));
				collapseBodyContainer.add(new Label("email", guest.getEmail()));
				collapseBodyContainer.add(new Label("company", guest.getCompany()));
				collapseBodyContainer.add(new Label("purpose_of_arrival", guest.getPurpose_of_arrival()));
				collapseBodyContainer.add(new Label("occupation", guest.getOccupation()));
				collapseBodyContainer.add(new Label("visa_number", guest.getVisa_number()));
				collapseBodyContainer.add(new Label("visa_valid_from", guest.getVisa_valid_from()));
				collapseBodyContainer.add(new Label("visa_valid_to", guest.getVisa_valid_to()));
			}
		});
	}
}
