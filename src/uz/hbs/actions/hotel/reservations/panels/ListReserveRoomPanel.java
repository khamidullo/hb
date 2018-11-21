package uz.hbs.actions.hotel.reservations.panels;

import java.util.List;

import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.beans.ReservationDetail;
import uz.hbs.beans.ReservationRoom;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;

public class ListReserveRoomPanel extends MyBreadCrumbPanel {
	private static final long serialVersionUID = 1L;
	
	
	public ListReserveRoomPanel(String id, IBreadCrumbModel breadCrumbModel, final IModel<ReservationDetail> model) {
		super(id, breadCrumbModel, model);
		add(new ListView<ReservationRoom>("list", new LoadableDetachableModel<List<ReservationRoom>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<ReservationRoom> load() {
				return new MyBatisHelper().selectList("selectReserveRoomByGuestList", model.getObject().getId());
			}
		}) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<ReservationRoom> item) {
				final ReservationRoom reserveroom = (ReservationRoom) item.getDefaultModelObject();
				item.add(new Label("roomtype", reserveroom.getRoomtype().getName()));
				item.add(new Label("room_number", reserveroom.getRoom().getRoom_number()));
				item.add(new Label("guest_count", reserveroom.getGuest_count()));
				item.add(new Label("guests", reserveroom.getGuests()).setEscapeModelStrings(false));
				item.add(new Link<Void>("changeroom"){
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						activate(new IBreadCrumbPanelFactory() {
							private static final long serialVersionUID = 1L;

							@Override
							public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
								return new ChangeRoomPanel(componentId, breadCrumbModel, new Model<ReservationRoom>(reserveroom));
							}
						});
					}
				});
			}
		});
	}

	@Override
	public Class<?> implementedClass() {
		return this.getClass();
	}


	@Override
	public IModel<String> getTitle() {
		return new StringResourceModel("hotels.reservation.room.list", null);
	}
}
