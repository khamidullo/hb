package uz.hbs.actions.admin;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.actions.admin.preferences.systemfeedback.SystemFeedBackListPanel;
import uz.hbs.beans.ReservationStatus;
import uz.hbs.beans.ReservationType;
import uz.hbs.beans.User;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;
import uz.hbs.utils.CommonUtil;
import uz.hbs.utils.HotelUtil;

public class AdminHomePanel extends MyBreadCrumbPanel {
	private static final long serialVersionUID = 1L;

	public AdminHomePanel(String id, IBreadCrumbModel breadCrumbModel) {
		super(id, breadCrumbModel);

		Link<String> usersLink = new Link<String>("usersListLink") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new AdminHomePage(AdminHomePage.USER_LIST));
			}
		};
		add(usersLink);

		List<Byte> statusList = new ArrayList<Byte>();
		statusList.add(User.STATUS_ALL);
		statusList.add(User.STATUS_NEW);
		statusList.add(User.STATUS_ACTIVE);
		statusList.add(User.STATUS_DISABLED);

		ListView<Byte> usersCountList = new ListView<Byte>("usersCountList", statusList) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<Byte> item) {
				item.add(new Label("usersCount", CommonUtil.getUsersCount(User.TYPE_ADMIN_USER, item.getModelObject() == User.STATUS_ALL ? null
						: item.getModelObject())));
			}
		};
		add(usersCountList);

		Link<String> hotelsLink = new Link<String>("hotelsListLink") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new AdminHomePage(AdminHomePage.HOTEL_LIST));
			}
		};
		add(hotelsLink);

		ListView<Byte> hotelsCountList = new ListView<Byte>("hotelsCountList", statusList) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<Byte> item) {
				item.add(new Label("hotelsCount", CommonUtil.getUsersCount(User.TYPE_HOTEL,
						item.getModelObject() == User.STATUS_ALL ? null : item.getModelObject())));
			}
		};
		add(hotelsCountList);

		Link<String> tourAgenciesLink = new Link<String>("tourAgenciesListLink") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new AdminHomePage(AdminHomePage.TA_LIST));
			}
		};
		add(tourAgenciesLink);

		ListView<Byte> tourAgenciesCountList = new ListView<Byte>("tourAgenciesCountList", statusList) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<Byte> item) {
				item.add(new Label("tourAgenciesCount", CommonUtil.getUsersCount(User.TYPE_TOURAGENCY,
						item.getModelObject() == User.STATUS_ALL ? null : item.getModelObject())));
			}
		};
		add(tourAgenciesCountList);

		Link<String> spectatorsLink = new Link<String>("spectatorsListLink") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new AdminHomePage(AdminHomePage.SPECTATORS_LIST));
			}
		};
		add(spectatorsLink);

		ListView<Byte> spectatorsCountList = new ListView<Byte>("spectatorsCountList", statusList) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<Byte> item) {
				item.add(new Label("spectatorsCount", CommonUtil.getUsersCount(User.TYPE_SPECTATOR_USER,
						item.getModelObject() == User.STATUS_ALL ? null : item.getModelObject())));
			}
		};
		add(spectatorsCountList);
		
		Link<String> apiUserLink = new Link<String>("apiUserListLink") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new AdminHomePage(AdminHomePage.API_USER_LIST));
			}
		};
		add(apiUserLink);

		ListView<Byte> apiUserCountList = new ListView<Byte>("apiUserCountList", statusList) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void populateItem(ListItem<Byte> item) {
				item.add(new Label("apiUserCount", CommonUtil.getUsersCount(User.TYPE_API,
						item.getModelObject() == User.STATUS_ALL ? null : item.getModelObject())));
			}
		};
		add(apiUserCountList);

		statusList = new ArrayList<Byte>();
		statusList.add(User.STATUS_ALL);
		statusList.add(ReservationStatus.RESERVED);
		statusList.add(ReservationStatus.CHECKED_IN);
		statusList.add(ReservationStatus.CHECKED_OUT);
		statusList.add(ReservationStatus.NO_SHOW);
		statusList.add(ReservationStatus.CANCELLED);

		ListView<Byte> definiteCountList = new ListView<Byte>("definiteCountList", statusList) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<Byte> item) {
				item.add(new Label("definiteCount", HotelUtil.getReservationCount(ReservationType.DEFINITE,
						item.getModelObject() == User.STATUS_ALL ? null : item.getModelObject(), null, null, User.TYPE_TOURAGENT_USER)));
			}
		};
		add(definiteCountList);

		ListView<Byte> tentativeCountList = new ListView<Byte>("tentativeCountList", statusList) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<Byte> item) {
				item.add(new Label("tentativeCount", HotelUtil.getReservationCount(ReservationType.TENTATIVE,
						item.getModelObject() == User.STATUS_ALL ? null : item.getModelObject(), null, null, User.TYPE_TOURAGENT_USER)));
			}
		};
		add(tentativeCountList);
		
		ListView<Byte> definiteApiCountList = new ListView<Byte>("definiteApiCountList", statusList) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void populateItem(ListItem<Byte> item) {
				item.add(new Label("definiteApiCount", HotelUtil.getReservationCount(ReservationType.DEFINITE,
						item.getModelObject() == User.STATUS_ALL ? null : item.getModelObject(), null, null, User.TYPE_API)));
			}
		};
		add(definiteApiCountList);
		
		ListView<Byte> tentativeApiCountList = new ListView<Byte>("tentativeApiCountList", statusList) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void populateItem(ListItem<Byte> item) {
				item.add(new Label("tentativeApiCount", HotelUtil.getReservationCount(ReservationType.TENTATIVE,
						item.getModelObject() == User.STATUS_ALL ? null : item.getModelObject(), null, null, User.TYPE_API)));
			}
		};
		add(tentativeApiCountList);
		
		add(new SystemFeedBackListPanel("systemFeedbackListPanel"));
	}

	@Override
	public Class<?> implementedClass() {
		return this.getClass();
	}

	@Override
	public IModel<String> getTitle() {
		return new StringResourceModel("main_page", null);
	}
}
