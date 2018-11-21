package uz.hbs.actions.hotel;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.MyWebApplication;
import uz.hbs.actions.hotel.roomsandrates.RoomsAndRatesPage;
import uz.hbs.beans.Contract;
import uz.hbs.beans.Hotel;
import uz.hbs.beans.ReservationStatus;
import uz.hbs.beans.ReservationType;
import uz.hbs.beans.User;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.session.MySession;
import uz.hbs.utils.DateUtil;
import uz.hbs.utils.HotelUtil;

public class HotelHomePanel extends Panel {
	private static final long serialVersionUID = 1L;

	public HotelHomePanel(String id) {
		super(id);
		final User user = ((MySession) getSession()).getUser();

		Link<Void> saleLink = new Link<Void>("saleLink") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new RoomsAndRatesPage(RoomsAndRatesPage.RATE_PLANE));
			}
		};
		add(saleLink);

		Link<Void> rateLink = new Link<Void>("roomsLink") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new RoomsAndRatesPage(RoomsAndRatesPage.AVAILABILITY_ROOM_LIST));
			}
		};
		add(rateLink);

		Label login = new Label("login", user.getLogin());
		add(login);

		Label pendingPayments = new Label("pendingPayments", 0);
		add(pendingPayments);

		Hotel hotel = new MyBatisHelper().selectOne("selectHotelByUserId", ((MySession) getSession()).getUser().getWork().getId());

		Label tourAgency = new Label("hotel", "(" + hotel.getName() + ")");
		add(tourAgency);

		Contract contract = new MyBatisHelper().selectOne("selectContract", ((MySession) getSession()).getUser().getWork().getId());

		Label contractDetails = new Label("contractDetails", new StringResourceModel("touragents.contract_details", null, new Object[] {
				contract != null ? contract.getContract_number() : "?",
				contract != null ? DateUtil.toString(contract.getContract_date(), MyWebApplication.DATE_FORMAT) : "?" }));
		add(contractDetails);

		Label reservationsCount = new Label("reservationsCount", HotelUtil.getReservationCount(null, null, null, ((MySession) getSession()).getUser()
				.getWork().getId(), User.TYPE_TOURAGENT_USER));
		add(reservationsCount);

		Label reservationsDefiniteCount = new Label("reservationsDefiniteCount", HotelUtil.getReservationCount(ReservationType.DEFINITE, null, null,
				((MySession) getSession()).getUser().getWork().getId(), User.TYPE_TOURAGENT_USER));
		add(reservationsDefiniteCount);

		Label reservationsTentativeCount = new Label("reservationsTentativeCount", HotelUtil.getReservationCount(ReservationType.TENTATIVE, null,
				null, ((MySession) getSession()).getUser().getWork().getId(), User.TYPE_TOURAGENT_USER));
		add(reservationsTentativeCount);

		Label reservationsCanceledCount = new Label("reservationsCanceledCount", HotelUtil.getReservationCount(null, ReservationStatus.CANCELLED,
				null, ((MySession) getSession()).getUser().getWork().getId(), User.TYPE_TOURAGENT_USER));
		add(reservationsCanceledCount);
	}
}
