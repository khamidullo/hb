package uz.hbs.template;

import java.util.Iterator;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.MyWebApplication;
import uz.hbs.actions.BootstrapModalPage;
import uz.hbs.actions.admin.AdminHomePage;
import uz.hbs.actions.admin.hotels.test.MyTestPage;
import uz.hbs.actions.admin.hotels.test.MyTestPage4;
import uz.hbs.actions.admin.users.roles.RolesPage;
import uz.hbs.actions.api.ApiUserPage;
import uz.hbs.actions.hotel.reports.HotelInformationPage;
import uz.hbs.actions.hotel.reports.HotelReportPage;
import uz.hbs.actions.hotel.roomsandrates.RoomsAndRatesPage;
import uz.hbs.actions.settings.SettingsPage;
import uz.hbs.actions.spectator.SpectatorPage;
import uz.hbs.actions.touragent.TourAgentPage;
import uz.hbs.actions.touragent.extra.AdditionalServicePage;
import uz.hbs.beans.User;
import uz.hbs.enums.ActionRight;
import uz.hbs.security.Logout;
import uz.hbs.session.MySession;
import uz.hbs.temp.BootstrapSamples;
import uz.hbs.temp.FontAwesome;
import uz.hbs.temp.Home;
import uz.hbs.temp.LoadingIndicator;
import uz.hbs.temp.rowspan.DashrPage;
import uz.hbs.utils.DateUtil;

public class MyHeader extends Panel {
	private static final long serialVersionUID = 1L;

	public MyHeader(String id) {
		super(id);
		final MySession session = (MySession) getSession();
		final User user = session.getUser();

		/*******************************************************************************************/
		/******************************************* ADMIN *****************************************/
		/*******************************************************************************************/

		/*
		 * USERS
		 */
		WebMarkupContainer adminUsers = new WebMarkupContainer("admin.users");
		adminUsers.add(new Behavior() {
			private static final long serialVersionUID = 1L;

			@Override
			public void bind(Component component) {
				if (!(component instanceof MarkupContainer)) {
					throw new IllegalArgumentException("This behavior can only be used with markup containers");
				}
			}

			@Override
			public void onConfigure(Component component) {
				MarkupContainer container = (MarkupContainer) component;
				boolean hasVisibleChildren = false;
				for (Iterator<? extends Component> iter = container.iterator(); iter.hasNext();) {
					if (iter.next().isVisible()) {
						hasVisibleChildren = true;
						break;
					}
				}
				container.setVisible(hasVisibleChildren);
			}
		});
		add(adminUsers);

		adminUsers.add(new Link<String>("admin.users.list") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new AdminHomePage(AdminHomePage.USER_LIST));
			}

			@Override
			public boolean isVisible() {
				return user.hasRight(ActionRight.USER_LIST);
			}
		});

		adminUsers.add(new Link<String>("admin.users.add") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new AdminHomePage(AdminHomePage.USER_ADD));
			}

			@Override
			public boolean isVisible() {
				return user.hasRight(ActionRight.USER_ADD);
			}
		});
		adminUsers.add(new Link<String>("admin.roles.list") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new RolesPage(RolesPage.LIST));
			}

			@Override
			public boolean isVisible() {
				return user.getLogin().equalsIgnoreCase("admin") && user.hasRight(ActionRight.ROLE_LIST);
			}
		});

		adminUsers.add(new Link<String>("admin.roles.add") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new RolesPage(RolesPage.ADD));
			}

			@Override
			public boolean isVisible() {
				return user.getLogin().equalsIgnoreCase("admin") && user.hasRight(ActionRight.ROLE_ADD);
			}
		});

		adminUsers.add(new Link<String>("admin.users.api.list") {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onClick() {
				setResponsePage(new AdminHomePage(AdminHomePage.API_USER_LIST));
			}
			
			@Override
			public boolean isVisible() {
				return user.getLogin().equalsIgnoreCase("admin")  && user.hasRight(ActionRight.API_USER_LIST);//Can see only Admin user with login 'admin'
			}
		});
		
		/*
		 * HOTELS
		 */
		WebMarkupContainer adminHotels = new WebMarkupContainer("admin.hotels");
		adminHotels.add(new Behavior() {
			private static final long serialVersionUID = 1L;

			@Override
			public void bind(Component component) {
				if (!(component instanceof MarkupContainer)) {
					throw new IllegalArgumentException("This behavior can only be used with markup containers");
				}
			}

			@Override
			public void onConfigure(Component component) {
				MarkupContainer container = (MarkupContainer) component;
				boolean hasVisibleChildren = false;
				for (Iterator<? extends Component> iter = container.iterator(); iter.hasNext();) {
					if (iter.next().isVisible()) {
						hasVisibleChildren = true;
						break;
					}
				}
				container.setVisible(hasVisibleChildren);
			}
		});
		add(adminHotels);

		adminHotels.add(new Link<String>("admin.hotels.list") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new AdminHomePage(AdminHomePage.HOTEL_LIST));
			}

			@Override
			public boolean isVisible() {
				return user.hasRight(ActionRight.HOTEL_LIST);
			}
		});

		adminHotels.add(new Link<String>("admin.hotels.add") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new AdminHomePage(AdminHomePage.HOTEL_ADD));
			}

			@Override
			public boolean isVisible() {
				return user.hasRight(ActionRight.HOTEL_ADD);
			}
		});

		adminHotels.add(new Link<String>("admin.hotels.preference") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new AdminHomePage(AdminHomePage.HOTEL_PREFERENCE));
			}

			@Override
			public boolean isVisible() {
				return user.getType().getId() == User.TYPE_ADMIN_USER;
			}
		});

		adminHotels.add(new Link<String>("admin.hotels.b2c.list") {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onClick() {
				setResponsePage(new AdminHomePage(AdminHomePage.HOTEL_B2C_LIST));
			}
			
			@Override
			public boolean isVisible() {
				return user.getType().getId() == User.TYPE_ADMIN_USER;
			}
		});
		
		adminHotels.add(new Link<String>("admin.hotels.regions") {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onClick() {
				setResponsePage(new AdminHomePage(AdminHomePage.PREFERENCES_REGIONS));
			}
			
			@Override
			public boolean isVisible() {
				return user.getType().getId() == User.TYPE_ADMIN_USER;
			}
		});

		adminHotels.add(new Link<String>("admin.hotels.cities") {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onClick() {
				setResponsePage(new AdminHomePage(AdminHomePage.PREFERENCES_CITIES));
			}
			
			@Override
			public boolean isVisible() {
				return user.getType().getId() == User.TYPE_ADMIN_USER;
			}
		});
		
		/*
		 * TOUR AGENTS
		 */
		WebMarkupContainer adminTouragents = new WebMarkupContainer("admin.touragents");
		adminTouragents.add(new Behavior() {
			private static final long serialVersionUID = 1L;

			@Override
			public void bind(Component component) {
				if (!(component instanceof MarkupContainer)) {
					throw new IllegalArgumentException("This behavior can only be used with markup containers");
				}
			}

			@Override
			public void onConfigure(Component component) {
				MarkupContainer container = (MarkupContainer) component;
				boolean hasVisibleChildren = false;
				for (Iterator<? extends Component> iter = container.iterator(); iter.hasNext();) {
					if (iter.next().isVisible()) {
						hasVisibleChildren = true;
						break;
					}
				}
				container.setVisible(hasVisibleChildren);
			}
		});
		add(adminTouragents);

		adminTouragents.add(new Link<String>("admin.touragents.list") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new AdminHomePage(AdminHomePage.TA_LIST));
			}

			@Override
			public boolean isVisible() {
				return user.getType().getId() == User.TYPE_ADMIN_USER;
			}
		});

		adminTouragents.add(new Link<String>("admin.touragents.add") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new AdminHomePage(AdminHomePage.TA_ADD));
			}

			@Override
			public boolean isVisible() {
				return user.getType().getId() == User.TYPE_ADMIN_USER;
			}
		});

		/*
		 * SPECTATORS
		 */
		WebMarkupContainer adminSpectators = new WebMarkupContainer("admin.spectators");
		adminSpectators.add(new Behavior() {
			private static final long serialVersionUID = 1L;

			@Override
			public void bind(Component component) {
				if (!(component instanceof MarkupContainer)) {
					throw new IllegalArgumentException("This behavior can only be used with markup containers");
				}
			}

			@Override
			public void onConfigure(Component component) {
				MarkupContainer container = (MarkupContainer) component;
				boolean hasVisibleChildren = false;
				for (Iterator<? extends Component> iter = container.iterator(); iter.hasNext();) {
					if (iter.next().isVisible()) {
						hasVisibleChildren = true;
						break;
					}
				}
				container.setVisible(hasVisibleChildren);
			}
		});
		add(adminSpectators);

		adminSpectators.add(new Link<String>("admin.spectators.list") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new AdminHomePage(AdminHomePage.SPECTATORS_LIST));
			}

			@Override
			public boolean isVisible() {
				return user.getType().getId() == User.TYPE_ADMIN_USER;
			}
		});

		adminSpectators.add(new Link<String>("admin.spectators.add") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new AdminHomePage(AdminHomePage.SPECTATORS_ADD));
			}

			@Override
			public boolean isVisible() {
				return user.getType().getId() == User.TYPE_ADMIN_USER;
			}
		});

		/*
		 * REPORTS
		 */
		WebMarkupContainer adminReports = new WebMarkupContainer("admin.reports");
		adminReports.add(new Behavior() {
			private static final long serialVersionUID = 1L;

			@Override
			public void bind(Component component) {
				if (!(component instanceof MarkupContainer)) {
					throw new IllegalArgumentException("This behavior can only be used with markup containers");
				}
			}

			@Override
			public void onConfigure(Component component) {
				MarkupContainer container = (MarkupContainer) component;
				boolean hasVisibleChildren = false;
				for (Iterator<? extends Component> iter = container.iterator(); iter.hasNext();) {
					if (iter.next().isVisible()) {
						hasVisibleChildren = true;
						break;
					}
				}
				container.setVisible(hasVisibleChildren);
			}
		});
		add(adminReports);

		adminReports.add(new Link<String>("admin.reports.by_reservations") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new AdminHomePage(AdminHomePage.REPORT_BY_RESERVATIONS));
			}

			@Override
			public boolean isVisible() {
				return user.getType().getId() == User.TYPE_ADMIN_USER;
			}
		});

		adminReports.add(new Link<String>("admin.reports.by_reservations.b2c") {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onClick() {
				setResponsePage(new AdminHomePage(AdminHomePage.REPORT_BY_RESERVATIONS_B2C));
			}
			
			@Override
			public boolean isVisible() {
				return user.getType().getId() == User.TYPE_ADMIN_USER;
			}
		});
		
		adminReports.add(new Link<String>("admin.reports.reservations") {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onClick() {
				setResponsePage(new AdminHomePage(AdminHomePage.REPORT_RESERVATIONS));
			}
			
			@Override
			public boolean isVisible() {
				return user.getType().getId() == User.TYPE_ADMIN_USER;
			}
		});
		
		adminReports.add(new Link<String>("admin.reports.reservations.b2c") {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onClick() {
				setResponsePage(new AdminHomePage(AdminHomePage.REPORT_RESERVATIONS_B2C));
			}
			
			@Override
			public boolean isVisible() {
				return false;//user.getType().getId() == User.TYPE_ADMIN_USER;
			}
		});
		
		adminReports.add(new Link<String>("admin.reports.additional_services.manage") {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onClick() {
				setResponsePage(new AdminHomePage(AdminHomePage.REPORT_ADDITIONAL_SERVICE_MANAGE));
			}
			
			@Override
			public boolean isVisible() {
				return user.getType().getId() == User.TYPE_ADMIN_USER;
			}
		});
		
		adminReports.add(new Link<String>("admin.reports.additional_services") {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onClick() {
				setResponsePage(new AdminHomePage(AdminHomePage.REPORT_ADDITIONAL_SERVICES));
			}
			
			@Override
			public boolean isVisible() {
				return user.getType().getId() == User.TYPE_ADMIN_USER;
			}
		});
		
		adminReports.add(new Link<String>("admin.reports.clients") {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onClick() {
				setResponsePage(new AdminHomePage(AdminHomePage.REPORT_CLIENTS));
			}
			
			@Override
			public boolean isVisible() {
				return user.getType().getId() == User.TYPE_ADMIN_USER;
			}
		});
		
		adminReports.add(new Link<String>("admin.reports.finance_annual") {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onClick() {
				setResponsePage(new AdminHomePage(AdminHomePage.REPORT_FINANCE_ANNUAL));
			}
			
			@Override
			public boolean isVisible() {
				return user.getType().getId() == User.TYPE_ADMIN_USER;
			}
		});
		
		adminReports.add(new Link<String>("admin.reports.finance_total") {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onClick() {
				setResponsePage(new AdminHomePage(AdminHomePage.REPORT_FINANCE_TOTAL));
			}
			
			@Override
			public boolean isVisible() {
				return user.getType().getId() == User.TYPE_ADMIN_USER;
			}
		});
		
		adminReports.add(new Link<String>("admin.reports.hotels") {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onClick() {
				setResponsePage(new AdminHomePage(AdminHomePage.REPORT_HOTELS));
			}
			
			@Override
			public boolean isVisible() {
				return user.getType().getId() == User.TYPE_ADMIN_USER;
			}
		});
		
		adminReports.add(new Link<String>("admin.reports.hotels_for_analysis") {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onClick() {
				setResponsePage(new AdminHomePage(AdminHomePage.REPORT_HOTELS_FOR_ANALYSIS));
			}
			
			@Override
			public boolean isVisible() {
				return user.getType().getId() == User.TYPE_ADMIN_USER;
			}
		});
		
		adminReports.add(new Link<String>("admin.reports.volume_of_reservations") {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onClick() {
				setResponsePage(new AdminHomePage(AdminHomePage.REPORT_VOLUME_OF_RESERVATIONS));
			}
			
			@Override
			public boolean isVisible() {
				return false;//user.getType().getId() == User.TYPE_ADMIN_USER;
			}
		});
		
		/*******************************************************************************************/
		/******************************************* HOTEL *****************************************/
		/*******************************************************************************************/
		/*
		 * Hotel Interface
		 */
		WebMarkupContainer hotelInterface = new WebMarkupContainer("hotelInterface");
		hotelInterface.add(new Behavior() {
			private static final long serialVersionUID = 1L;

			@Override
			public void bind(Component component) {
				if (!(component instanceof MarkupContainer)) {
					throw new IllegalArgumentException("This behavior can only be used with markup containers");
				}
			}

			@Override
			public void onConfigure(Component component) {
				MarkupContainer container = (MarkupContainer) component;
				boolean hasVisibleChildren = false;
				for (Iterator<? extends Component> iter = container.iterator(); iter.hasNext();) {
					if (iter.next().isVisible()) {
						hasVisibleChildren = true;
						break;
					}
				}
				container.setVisible(hasVisibleChildren);
			}
		});
		add(hotelInterface);
		
		Link<String> hotelReportsByReservations = new Link<String>("hotel.reports.by_reservations") {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onClick() {
				setResponsePage(new HotelReportPage(HotelReportPage.REPORTS_BY_RESERVATIONS));
			}
			
			@Override
			public boolean isVisible() {
				return user.getType().getId() == User.TYPE_HOTEL_USER;
			}
		};
		hotelInterface.add(hotelReportsByReservations);

		/*
		 * Hotel Rooms and Rates Interface
		 */
		WebMarkupContainer hotelRoomsAndRates = new WebMarkupContainer("hotel.roomsandrates");
		hotelRoomsAndRates.add(new Behavior() {
			private static final long serialVersionUID = 1L;

			@Override
			public void bind(Component component) {
				if (!(component instanceof MarkupContainer)) {
					throw new IllegalArgumentException("This behavior can only be used with markup containers");
				}
			}

			@Override
			public void onConfigure(Component component) {
				MarkupContainer container = (MarkupContainer) component;
				boolean hasVisibleChildren = false;
				for (Iterator<? extends Component> iter = container.iterator(); iter.hasNext();) {
					if (iter.next().isVisible()) {
						hasVisibleChildren = true;
						break;
					}
				}
				container.setVisible(hasVisibleChildren);
			}
		});
		hotelInterface.add(hotelRoomsAndRates);

		Link<String> room_list = new Link<String>("hotel.roomsandrates.room_list") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new RoomsAndRatesPage(RoomsAndRatesPage.ROOM_LIST));
			}

			@Override
			public boolean isVisible() {
				return false;
			}
		};
		hotelRoomsAndRates.add(room_list);

		Link<String> availibility_room_list = new Link<String>("hotel.roomsandrates.availibility_room_list") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new RoomsAndRatesPage(RoomsAndRatesPage.AVAILABILITY_ROOM_LIST));
			}

			@Override
			public boolean isVisible() {
				return user.getType().getId() == User.TYPE_HOTEL_USER;
			}
		};
		hotelRoomsAndRates.add(availibility_room_list);

		Link<String> room_rate_sale = new Link<String>("hotel.roomsandrates.room_rate_sale") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new RoomsAndRatesPage(RoomsAndRatesPage.RATE_PLANE));
			}

			@Override
			public boolean isVisible() {
				return user.getType().getId() == User.TYPE_HOTEL_USER;
			}
		};
		hotelRoomsAndRates.add(room_rate_sale);

		Link<String> hotelInformation = new Link<String>("hotel.info") {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onClick() {
				setResponsePage(new HotelInformationPage());
			}
			
			@Override
			public boolean isVisible() {
				return user.getType().getId() == User.TYPE_HOTEL_USER;
			}
		};
		hotelInterface.add(hotelInformation);

		/*******************************************************************************************/
		/******************************************* TOUR AGENT ************************************/
		/*******************************************************************************************/
		/*
		 * Touragent Interface
		 */
		WebMarkupContainer touragentInterface = new WebMarkupContainer("touragentInterface");
		touragentInterface.add(new Behavior() {
			private static final long serialVersionUID = 1L;

			@Override
			public void bind(Component component) {
				if (!(component instanceof MarkupContainer)) {
					throw new IllegalArgumentException("This behavior can only be used with markup containers");
				}
			}

			@Override
			public void onConfigure(Component component) {
				MarkupContainer container = (MarkupContainer) component;
				boolean hasVisibleChildren = false;
				for (Iterator<? extends Component> iter = container.iterator(); iter.hasNext();) {
					if (iter.next().isVisible()) {
						hasVisibleChildren = true;
						break;
					}
				}
				container.setVisible(hasVisibleChildren);
			}
		});
		add(touragentInterface);

		Link<String> touragentNewBooking = new Link<String>("touragent.newbooking") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new TourAgentPage(TourAgentPage.NEW_BOOKING, false, null, null));
			}

			@Override
			public boolean isVisible() {
				return user.getType().getId() == User.TYPE_TOURAGENT_USER;
			}
		};
		touragentInterface.add(touragentNewBooking);

		Link<String> touragentReservations = new Link<String>("touragent.reservations") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new TourAgentPage(TourAgentPage.RESERVATIONS, false, null, null));
			}

			@Override
			public boolean isVisible() {
				return user.getType().getId() == User.TYPE_TOURAGENT_USER;
			}
		};
		touragentInterface.add(touragentReservations);

		/*
		 * Additional services
		 */
		WebMarkupContainer touragentAdditionalServices = new WebMarkupContainer("touragent.additional_services");
		touragentAdditionalServices.add(new Behavior() {
			private static final long serialVersionUID = 1L;

			@Override
			public void bind(Component component) {
				if (!(component instanceof MarkupContainer)) {
					throw new IllegalArgumentException("This behavior can only be used with markup containers");
				}
			}

			@Override
			public void onConfigure(Component component) {
				MarkupContainer container = (MarkupContainer) component;
				boolean hasVisibleChildren = false;
				for (Iterator<? extends Component> iter = container.iterator(); iter.hasNext();) {
					if (iter.next().isVisible()) {
						hasVisibleChildren = true;
						break;
					}
				}
				container.setVisible(hasVisibleChildren);
			}
		});
		touragentInterface.add(touragentAdditionalServices);

		Link<String> touragentAdditionalServicesAdd = new Link<String>("touragent.additional_services_add") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new AdditionalServicePage(AdditionalServicePage.ADD));
			}

			@Override
			public boolean isVisible() {
				return user.getType().getId() == User.TYPE_TOURAGENT_USER;
			}
		};
		touragentAdditionalServices.add(touragentAdditionalServicesAdd);

		Link<String> touragentAdditionalServicesList = new Link<String>("touragent.additional_services_list") {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onClick() {
				setResponsePage(new AdditionalServicePage(AdditionalServicePage.LIST));
			}
			
			@Override
			public boolean isVisible() {
				return user.getType().getId() == User.TYPE_TOURAGENT_USER;
			}
		};
		touragentAdditionalServices.add(touragentAdditionalServicesList);
		
		WebMarkupContainer touragentReports = new WebMarkupContainer("touragent.reports");
		touragentReports.add(new Behavior() {
			private static final long serialVersionUID = 1L;

			@Override
			public void bind(Component component) {
				if (!(component instanceof MarkupContainer)) {
					throw new IllegalArgumentException("This behavior can only be used with markup containers");
				}
			}

			@Override
			public void onConfigure(Component component) {
				MarkupContainer container = (MarkupContainer) component;
				boolean hasVisibleChildren = false;
				for (Iterator<? extends Component> iter = container.iterator(); iter.hasNext();) {
					if (iter.next().isVisible()) {
						hasVisibleChildren = true;
						break;
					}
				}
				container.setVisible(hasVisibleChildren);
			}
		});
		touragentInterface.add(touragentReports);

		Link<String> touragentReportsByReservations = new Link<String>("touragent.reports.by_reservations") {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onClick() {
				setResponsePage(new TourAgentPage(TourAgentPage.REPORTS_BY_RESERVATIONS, false, null, null));
			}
			
			@Override
			public boolean isVisible() {
				return user.getType().getId() == User.TYPE_TOURAGENT_USER;
			}
		};
		touragentReports.add(touragentReportsByReservations);
		
		Link<String> touragentReportsList = new Link<String>("touragent.reports.reports") {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onClick() {
				setResponsePage(new TourAgentPage(TourAgentPage.REPORTS, false, null, null));
			}
			
			@Override
			public boolean isVisible() {
				return user.getType().getId() == User.TYPE_TOURAGENT_USER;
			}
		};
		touragentReports.add(touragentReportsList);

		/*******************************************************************************************/
		/******************************************* SPECTATOR *************************************/
		/*******************************************************************************************/
		/*
		 * Touragent Interface
		 */
		WebMarkupContainer spectatorInterface = new WebMarkupContainer("spectatorInterface");
		spectatorInterface.add(new Behavior() {
			private static final long serialVersionUID = 1L;

			@Override
			public void bind(Component component) {
				if (!(component instanceof MarkupContainer)) {
					throw new IllegalArgumentException("This behavior can only be used with markup containers");
				}
			}

			@Override
			public void onConfigure(Component component) {
				MarkupContainer container = (MarkupContainer) component;
				boolean hasVisibleChildren = false;
				for (Iterator<? extends Component> iter = container.iterator(); iter.hasNext();) {
					if (iter.next().isVisible()) {
						hasVisibleChildren = true;
						break;
					}
				}
				container.setVisible(hasVisibleChildren);
			}
		});
		add(spectatorInterface);

		Link<String> spectatorReports = new Link<String>("spectator.reports") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new SpectatorPage(SpectatorPage.REPORTS));
			}

			@Override
			public boolean isVisible() {
				return user.getType().getId() == User.TYPE_SPECTATOR_USER;
			}
		};
		spectatorInterface.add(spectatorReports);

		/*******************************************************************************************/
		/******************************************** API USER *************************************/
		/*******************************************************************************************/
		/*
		 * API User Interface
		 */
		WebMarkupContainer apiUserInterface = new WebMarkupContainer("apiUserInterface");
		apiUserInterface.add(new Behavior() {
			private static final long serialVersionUID = 1L;

			@Override
			public void bind(Component component) {
				if (!(component instanceof MarkupContainer)) {
					throw new IllegalArgumentException("This behavior can only be used with markup containers");
				}
			}

			@Override
			public void onConfigure(Component component) {
				MarkupContainer container = (MarkupContainer) component;
				boolean hasVisibleChildren = false;
				for (Iterator<? extends Component> iter = container.iterator(); iter.hasNext();) {
					if (iter.next().isVisible()) {
						hasVisibleChildren = true;
						break;
					}
				}
				container.setVisible(hasVisibleChildren);
			}
		});
		add(apiUserInterface);

		Link<String> apiUserReservations = new Link<String>("apiUser.reservations") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new ApiUserPage(ApiUserPage.RESERVATIONS, false, null, null));
			}

			@Override
			public boolean isVisible() {
				return user.getType().getId() == User.TYPE_API;
			}
		};
		apiUserInterface.add(apiUserReservations);

		Link<String> apiUserReports = new Link<String>("apiUser.reports") {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onClick() {
				setResponsePage(new ApiUserPage(ApiUserPage.REPORTS, false, null, null));
			}
			
			@Override
			public boolean isVisible() {
				return user.getType().getId() == User.TYPE_API;
			}
		};
		apiUserInterface.add(apiUserReports);
		
		/*******************************************************************************************/
		/******************************************* PREFERENCES ***********************************/
		/*******************************************************************************************/
		/*
		 * Touragent Interface
		 */
		WebMarkupContainer preferences = new WebMarkupContainer("preferences");
		preferences.add(new Behavior() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void bind(Component component) {
				if (!(component instanceof MarkupContainer)) {
					throw new IllegalArgumentException("This behavior can only be used with markup containers");
				}
			}
			
			@Override
			public void onConfigure(Component component) {
				MarkupContainer container = (MarkupContainer) component;
				boolean hasVisibleChildren = false;
				for (Iterator<? extends Component> iter = container.iterator(); iter.hasNext();) {
					if (iter.next().isVisible()) {
						hasVisibleChildren = true;
						break;
					}
				}
				container.setVisible(hasVisibleChildren);
			}
		});
		add(preferences);
		
		Link<String> preferencesPriceRange = new Link<String>("preferences.price_range") {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onClick() {
				setResponsePage(new AdminHomePage(AdminHomePage.PREFERENCES_PRICE_RANGE));
			}
			
			@Override
			public boolean isVisible() {
				return user.getType().getId() == User.TYPE_ADMIN_USER;
			}
		};
		preferences.add(preferencesPriceRange);

		Link<String> preferencesAdditionalServices = new Link<String>("preferences.additional_services") {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onClick() {
				setResponsePage(new AdminHomePage(AdminHomePage.PREFERENCES_ADDITIONAL_SERVICES));
			}
			
			@Override
			public boolean isVisible() {
				return user.getType().getId() == User.TYPE_ADMIN_USER;
			}
		};
		preferences.add(preferencesAdditionalServices);
		
		Link<String> preferencesRecommendedSort = new Link<String>("preferences.recommended_sort") {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onClick() {
				setResponsePage(new AdminHomePage(AdminHomePage.PREFERENCES_RECOMMENDED_SORT));
			}
			
			@Override
			public boolean isVisible() {
				return user.getType().getId() == User.TYPE_ADMIN_USER;
			}
		};
		preferences.add(preferencesRecommendedSort);

		/*******************************************************************************************/
		/******************************************* SETTINGS **************************************/
		/*******************************************************************************************/
		add(new Label("user", user.getLogin()));
		add(new Label("user.name", user.getName()));
		add(new Label("user.lastLogin.time", new StringResourceModel("user.last_login.time", null).getString() + ": " + DateUtil.toString(((MySession) getSession()).getLastUserSession().getCreate_date(), MyWebApplication.DATE_TIME_FORMAT)));
		add(new Label("user.lastLogin.host", new StringResourceModel("user.last_login.host", null).getString() + ": " + ((MySession) getSession()).getLastUserSession().getClient_host()));

		add(new Link<String>("settings") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new SettingsPage(SettingsPage.MAIN_PANEL));
			}
		});

		add(new Link<String>("logout") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(Logout.class);
			}

			@Override
			public CharSequence getOnClickScript(CharSequence url) {
				return "return confirm('" + new StringResourceModel("users.logout.confirmation", null).getString() + "')";
			}
		});

		add(new Link<String>("homepage") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new Home());
			}

			@Override
			public boolean isVisible() {
				return getApplication().usesDevelopmentConfig();
			}
		});

		add(new Link<String>("fontawesome") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new FontAwesome());
			}

			@Override
			public boolean isVisible() {
				return getApplication().usesDevelopmentConfig();
			}
		});

		add(new Link<String>("bootstrap") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new BootstrapSamples());
			}

			@Override
			public boolean isVisible() {
				return getApplication().usesDevelopmentConfig();
			}
		});

		add(new Link<String>("modal") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new BootstrapModalPage());
			}

			@Override
			public boolean isVisible() {
				return getApplication().usesDevelopmentConfig();
			}
		});

		add(new Link<String>("test") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new MyTestPage4());
			}

			@Override
			public boolean isVisible() {
				return getApplication().usesDevelopmentConfig();
			}
		});
		add(new Link<String>("test2") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new MyTestPage());
			}

			@Override
			public boolean isVisible() {
				return getApplication().usesDevelopmentConfig();
			}
		});
		add(new Link<String>("rowspan") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new DashrPage());
			}

			@Override
			public boolean isVisible() {
				return getApplication().usesDevelopmentConfig();
			}
		});
		add(new Link<String>("loading_indicator") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new LoadingIndicator());
			}

			@Override
			public boolean isVisible() {
				return getApplication().usesDevelopmentConfig();
			}
		});
	}
}