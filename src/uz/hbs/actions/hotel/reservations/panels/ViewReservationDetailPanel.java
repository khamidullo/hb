package uz.hbs.actions.hotel.reservations.panels;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import uz.hbs.beans.ReservationDetail;
import uz.hbs.beans.ReservationType;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.utils.FormatUtil;

public class ViewReservationDetailPanel extends Panel {
	private static final long serialVersionUID = 1L;

	public ViewReservationDetailPanel(String id, IModel<ReservationDetail> model) {
		super(id, model);
		final ReservationDetail detail = new MyBatisHelper().selectOne("selectReserveDetailsById", model.getObject().getId());
		
		add(new Label("full_name",  detail.getMain_guest().getFirst_name() + " " + detail.getMain_guest().getLast_name()));
		//add(new Label("room_number",  detail.getRoom_number()));
		add(new Label("roomtype", detail.getRoom_type()));
//		add(new Label("meal_options", new AbstractReadOnlyModel<String>() {
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public String getObject() {
////				switch (detail.getMeal_options()){
////				case MealOption.BB: return getString("meal_options.bb");
////				case MealOption.HB_LUNCH: return getString("meal_options.hb");
////				case MealOption.HB_DINNER: return getString("meal_options.hb");
////				case MealOption.FB: return getString("meal_options.fb");
////				}
//				return null;
//			}
//		}));
		add(new Label("adults", detail.getAdults()));
		add(new Label("children", detail.getChildren()));
//		add(new Label("extra_bed", detail.getExtra_bed()));
//		add(new Label("room_rate", detail.getRate()));
		add(new Label("reservation_type", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				switch (detail.getReservation_type()){
				case ReservationType.DEFINITE: return getString("hotels.reservation.details.reservation.type.definite");
				case ReservationType.TENTATIVE: return getString("hotels.reservation.details.reservation.type.tentative");
				}
				return null;
			}
		}));
		add(new Label("tour_agent", detail.getTour_agent().getName()));
		add(new Label("total", detail.getTotal()));
		add(new Label("check_in", FormatUtil.toString(detail.getCheck_in(), "dd/MM/yyyy HH:mm")));
//		add(new Label("actual_check_in", FormatUtil.toString(detail.getCheck_in(), "dd/MM/yyyy HH:mm")));//
		add(new Label("check_out", FormatUtil.toString(detail.getCheck_out(), "dd/MM/yyyy HH:mm")));
//		add(new Label("check_out_in", new AbstractReadOnlyModel<String>() {
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public String getObject() {
//				return new MyBatisHelper().selectOne("selectCheckOutIn", detail.getCheck_out());
//			}
//		}));
		add(new HiddenField<String>("comments", Model.of(detail.getReception_comments())));
		add(new Label("guest_comments", model.getObject().getGuest_comments()));
	}

}
