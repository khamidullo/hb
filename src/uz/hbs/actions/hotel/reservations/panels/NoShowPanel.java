package uz.hbs.actions.hotel.reservations.panels;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uz.hbs.beans.ReservationDetail;
import uz.hbs.beans.ReservationRoom;
import uz.hbs.beans.RoomTypeDetails;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;

public class NoShowPanel extends Panel {
	private static final Logger _log = LoggerFactory.getLogger(NoShowPanel.class);
	private static final long serialVersionUID = 1L;

	public NoShowPanel(String id, final ReservationDetail reservationDetail) {
		super(id);
		add(new MyForm("form", reservationDetail));
	}

	private class MyForm extends Form<RoomTypeDetails> {
		private static final long serialVersionUID = 1L;

		public MyForm(String id, final ReservationDetail reservationDetail) {
			super(id);

			final MyFeedbackPanel feedback = new MyFeedbackPanel("feedback");
			feedback.setOutputMarkupId(true);
			add(feedback);

			final WebMarkupContainer container = new WebMarkupContainer("container");
			container.setOutputMarkupId(true);
			add(container);

			LoadableDetachableModel<List<RoomTypeDetails>> listModel = new LoadableDetachableModel<List<RoomTypeDetails>>() {
				private static final long serialVersionUID = 1L;

				@Override
				protected List<RoomTypeDetails> load() {
					Map<String, Serializable> param = new HashMap<String, Serializable>();
					param.put("reservations_id", reservationDetail.getId());
					return new MyBatisHelper().selectList("selectReservationRoomsByReservationId", param);
				}
			};

			final ListView<RoomTypeDetails> list = new ListView<RoomTypeDetails>("list", listModel) {
				private static final long serialVersionUID = 1L;

				@Override
				protected void populateItem(ListItem<RoomTypeDetails> item) {
					item.add(new Label("roomType", item.getModelObject().getName()));
					item.add(new Label("guestName", item.getModelObject().getGuest_name()));
					item.add(new Label("status",
							item.getModelObject().getStatus() == 1
									? new StringResourceModel("hotels.reservation.details.reservation.status.no_show", null).getString()
									: new StringResourceModel("hotels.reservation.details.reservation.status.active", null).getString()));
					item.add(new CheckBox("selected", new PropertyModel<Boolean>(item.getModel(), "selected")).setEnabled(item.getModelObject().getStatus() != 1));
				}
			};
			//list.setReuseItems(true);

			container.add(list);

			IndicatingAjaxButton submit = new IndicatingAjaxButton("submit") {
				private static final long serialVersionUID = 1L;

				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
					super.onError(target, form);
					feedback.success(new StringResourceModel("hotels.reservation.no_show.error", null).getString());
					target.add(container);
					target.add(feedback);
				}

				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					super.onSubmit(target, form);
					List<RoomTypeDetails> modelList = list.getModelObject();
					for (RoomTypeDetails model : modelList) {
						if (model.getSelected()) {
							_log.debug(model.getReservationrooms_id() + ", " + model.getSelected() + ", " + model.getName() + ", "	+ model.getGuest_name());
							model.setStatus(ReservationRoom.STATUS_NO_SHOW);
							model.setHotelsusers_id(reservationDetail.getHotelsusers_id());
							new MyBatisHelper().update("updateReservationRoomsStatusById", model);
							feedback.success(new StringResourceModel("hotels.reservation.no_show.success", null).getString());
						}
					}
					target.add(container);
					target.add(feedback);
				}
			};

			add(submit);
		}
	}
}
