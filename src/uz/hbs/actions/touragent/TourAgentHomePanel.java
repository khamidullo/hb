package uz.hbs.actions.touragent;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.MyWebApplication;
import uz.hbs.actions.touragent.extra.AdditionalServicePage;
import uz.hbs.beans.Account;
import uz.hbs.beans.Contract;
import uz.hbs.beans.ReservationStatus;
import uz.hbs.beans.ReservationType;
import uz.hbs.beans.TourAgent;
import uz.hbs.beans.User;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.session.MySession;
import uz.hbs.utils.DateUtil;
import uz.hbs.utils.FormatUtil;
import uz.hbs.utils.HotelUtil;

public class TourAgentHomePanel extends Panel {
	private static final long serialVersionUID = 1L;

	public TourAgentHomePanel(String id) {
		super(id);

		final User user = ((MySession) getSession()).getUser();

		Label lastLogin = new Label("lastLogin", new StringResourceModel("user.last_login", null).getString() + ": "
				+ DateUtil.toString(((MySession) getSession()).getLastUserSession().getCreate_date(), MyWebApplication.DATE_TIME_FORMAT) + ", "
				+ new StringResourceModel("user.last_login.host", null).getString() + ": "
				+ ((MySession) getSession()).getLastUserSession().getClient_host());
		add(lastLogin);

		Label login = new Label("login", user.getLogin());
		add(login);

		Link<Void> additionalServiceLink = new Link<Void>("additionalServiceLink") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new AdditionalServicePage(AdditionalServicePage.ADD));
			}
		};
		add(additionalServiceLink);

		Link<String> additionalServicesCountLink = new Link<String>("additionalServicesCountLink") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new AdditionalServicePage(AdditionalServicePage.LIST));
			}
		};
		add(additionalServicesCountLink);

		LoadableDetachableModel<Long> additionalServicesCountModel = new LoadableDetachableModel<Long>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected Long load() {
				Map<String, Serializable> param = new HashMap<String, Serializable>();
				param.put("creator_user_id", ((MySession) getSession()).getUser().getId());

				return new MyBatisHelper().selectOne("selectAdditionalServiceOrderListCount", param);
			}
		};

		Label additionalServicesCount = new Label("additionalServicesCount", additionalServicesCountModel);
		additionalServicesCountLink.add(additionalServicesCount);

		Link<Void> newBookingLink = new Link<Void>("newBookingLink") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new TourAgentPage(TourAgentPage.NEW_BOOKING, true, null, null));
			}
		};
		add(newBookingLink);

		Link<String> reservationsCountLink = new Link<String>("reservationsCountLink") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new TourAgentPage(TourAgentPage.RESERVATIONS, true, null, null));
			}
		};
		add(reservationsCountLink);

		Label reservationsCount = new Label("reservationsCount", HotelUtil.getReservationCount(null, null, ((MySession) getSession()).getUser()
				.getId(), null, User.TYPE_TOURAGENT_USER));
		reservationsCountLink.add(reservationsCount);

		Link<String> reservationsDefiniteCountLink = new Link<String>("reservationsDefiniteCountLink") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new TourAgentPage(TourAgentPage.RESERVATIONS, true, ReservationType.DEFINITE, null));
			}
		};
		add(reservationsDefiniteCountLink);

		Label reservationsDefiniteCount = new Label("reservationsDefiniteCount", HotelUtil.getReservationCount(ReservationType.DEFINITE, null,
				((MySession) getSession()).getUser().getId(), null, User.TYPE_TOURAGENT_USER));
		reservationsDefiniteCountLink.add(reservationsDefiniteCount);

		Link<String> reservationsTentativeCountLink = new Link<String>("reservationsTentativeCountLink") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new TourAgentPage(TourAgentPage.RESERVATIONS, true, ReservationType.TENTATIVE, null));
			}
		};
		add(reservationsTentativeCountLink);

		Label reservationsTentativeCount = new Label("reservationsTentativeCount", HotelUtil.getReservationCount(ReservationType.TENTATIVE, null,
				((MySession) getSession()).getUser().getId(), null, User.TYPE_TOURAGENT_USER));
		reservationsTentativeCountLink.add(reservationsTentativeCount);

		Link<String> reservationsCanceledCountLink = new Link<String>("reservationsCanceledCountLink") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new TourAgentPage(TourAgentPage.RESERVATIONS, true, null, ReservationStatus.CANCELLED));
			}
		};
		add(reservationsCanceledCountLink);

		Map<String, Serializable> params = new HashMap<String, Serializable>();
		params.put("users_id", user.getId());
		double balance = ((Account) new MyBatisHelper().selectOne("selectAccounts", params)).getBalance();

		Label reservationsCanceledCount = new Label("reservationsCanceledCount", HotelUtil.getReservationCount(null, ReservationStatus.CANCELLED,
				((MySession) getSession()).getUser().getId(), null, User.TYPE_TOURAGENT_USER));
		reservationsCanceledCountLink.add(reservationsCanceledCount);

		Label balanceLabel = new Label("balanceLabel", new StringResourceModel("touragents.balance_info", null, new Object[] {
				FormatUtil.format2(balance) }));
		balanceLabel.setVisible(false);
		add(balanceLabel);

		Label zeroBalance = new Label("zeroBalance", new StringResourceModel("touragents.zero_balance_info", null,
				new Object[] { FormatUtil.format2(balance) }));
		zeroBalance.setVisible(balance == 0);
		zeroBalance.setVisible(false);
		add(zeroBalance);

		Label pendingPayments = new Label("pendingPayments", "Ожидающие оплаты счета:" + 0);
		pendingPayments.setVisible(false);
		add(pendingPayments);

		TourAgent tourAgent = new MyBatisHelper().selectOne("selectTourAgents", ((MySession) getSession()).getUser().getWork().getId());

		Label tourAgency = new Label("tourAgency", "(" + tourAgent.getName() + ")");
		add(tourAgency);

		Contract contract = new MyBatisHelper().selectOne("selectContract", ((MySession) getSession()).getUser().getWork().getId());

		Label contractDetails = new Label("contractDetails", new StringResourceModel("touragents.contract_details", null, new Object[] {
				contract != null ? contract.getContract_number() : "?",
				contract != null ? DateUtil.toString(contract.getContract_date(), MyWebApplication.DATE_FORMAT) : "?" }));
		add(contractDetails);
	}
}
