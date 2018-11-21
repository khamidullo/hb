package uz.hbs.actions.api;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.MyWebApplication;
import uz.hbs.actions.api.additionalservices.AdditionalServicePage;
import uz.hbs.beans.ReservationStatus;
import uz.hbs.beans.ReservationType;
import uz.hbs.beans.User;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.session.MySession;
import uz.hbs.utils.DateUtil;
import uz.hbs.utils.HotelUtil;

public class ApiUserHomePanel extends Panel {
	private static final long serialVersionUID = 1L;

	public ApiUserHomePanel(String id) {
		super(id);

		final User user = ((MySession) getSession()).getUser();

		Label lastLogin = new Label("lastLogin", new StringResourceModel("user.last_login", null).getString() + ": "
				+ DateUtil.toString(((MySession) getSession()).getLastUserSession().getCreate_date(), MyWebApplication.DATE_TIME_FORMAT) + ", "
				+ new StringResourceModel("user.last_login.host", null).getString() + ": "
				+ ((MySession) getSession()).getLastUserSession().getClient_host());
		add(lastLogin);

		Label login = new Label("login", user.getLogin());
		add(login);

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

		Link<String> reservationsCountLink = new Link<String>("reservationsCountLink") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new ApiUserPage(ApiUserPage.RESERVATIONS, true, null, null));
			}
		};
		add(reservationsCountLink);

		Label reservationsCount = new Label("reservationsCount", HotelUtil.getReservationCount(null, null, ((MySession) getSession()).getUser()
				.getId(), null, User.TYPE_API));
		reservationsCountLink.add(reservationsCount);

		Link<String> reservationsDefiniteCountLink = new Link<String>("reservationsDefiniteCountLink") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new ApiUserPage(ApiUserPage.RESERVATIONS, true, ReservationType.DEFINITE, null));
			}
		};
		add(reservationsDefiniteCountLink);

		Label reservationsDefiniteCount = new Label("reservationsDefiniteCount", HotelUtil.getReservationCount(ReservationType.DEFINITE, null,
				((MySession) getSession()).getUser().getId(), null, User.TYPE_API));
		reservationsDefiniteCountLink.add(reservationsDefiniteCount);

		Link<String> reservationsTentativeCountLink = new Link<String>("reservationsTentativeCountLink") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new ApiUserPage(ApiUserPage.RESERVATIONS, true, ReservationType.TENTATIVE, null));
			}
		};
		add(reservationsTentativeCountLink);

		Label reservationsTentativeCount = new Label("reservationsTentativeCount", HotelUtil.getReservationCount(ReservationType.TENTATIVE, null,
				((MySession) getSession()).getUser().getId(), null, User.TYPE_API));
		reservationsTentativeCountLink.add(reservationsTentativeCount);

		Link<String> reservationsCanceledCountLink = new Link<String>("reservationsCanceledCountLink") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new ApiUserPage(ApiUserPage.RESERVATIONS, true, null, ReservationStatus.CANCELLED));
			}
		};
		add(reservationsCanceledCountLink);

		Label reservationsCanceledCount = new Label("reservationsCanceledCount", HotelUtil.getReservationCount(null, ReservationStatus.CANCELLED,
				((MySession) getSession()).getUser().getId(), null, User.TYPE_API));
		reservationsCanceledCountLink.add(reservationsCanceledCount);
	}
}