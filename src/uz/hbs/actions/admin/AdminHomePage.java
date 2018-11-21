package uz.hbs.actions.admin;

import java.util.Map;

import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;

import uz.hbs.actions.admin.addresses.city.ListCityPanel;
import uz.hbs.actions.admin.addresses.region.ListRegionPanel;
import uz.hbs.actions.admin.api.ListApiUserPanel;
import uz.hbs.actions.admin.hotels.ListHotelsPanel;
import uz.hbs.actions.admin.hotels.b2c.ListHotelsB2CPanel;
import uz.hbs.actions.admin.hotels.tabbed.AddHotelPanel;
import uz.hbs.actions.admin.preferences.additional.PriceAdditionalServicePanel;
import uz.hbs.actions.admin.preferences.pricerange.PriceRangePanel;
import uz.hbs.actions.admin.preferences.sortable.HotelSortablePanel;
import uz.hbs.actions.admin.reports.AdditionalServiceReportPanel;
import uz.hbs.actions.admin.reports.AllReservationListPanel;
import uz.hbs.actions.admin.reports.ClientReportPanel;
import uz.hbs.actions.admin.reports.FinanceAnnualReportPanel;
import uz.hbs.actions.admin.reports.FinanceTotalReportPanel;
import uz.hbs.actions.admin.reports.HotelReportForAnalysisPanel;
import uz.hbs.actions.admin.reports.HotelReportPanel;
import uz.hbs.actions.admin.reports.ReportB2CByReservationsPanel;
import uz.hbs.actions.admin.reports.ReportByReservationsPanel;
import uz.hbs.actions.admin.reports.ReservationVolumeReportPanel;
import uz.hbs.actions.admin.reports.manage.AdditionalServiceManageListPanel;
import uz.hbs.actions.admin.spectators.AddSpectatorsPanel;
import uz.hbs.actions.admin.spectators.ListSpectatorsPanel;
import uz.hbs.actions.admin.touragents.AddTourAgentsPanel;
import uz.hbs.actions.admin.touragents.ListTourAgentsPanel;
import uz.hbs.actions.admin.users.AddUserPanel;
import uz.hbs.actions.admin.users.ListUsersPanel;
import uz.hbs.beans.Action;
import uz.hbs.beans.User;
import uz.hbs.components.panels.hotel.ReferenceHotelPanel;
import uz.hbs.enums.ActionRight;
import uz.hbs.template.MyPage;

public class AdminHomePage extends MyPage {
	private static final long serialVersionUID = 1L;
	public static final String HOTEL_LIST = "HOTEL_LIST";
	public static final String HOTEL_B2C_LIST = "HOTEL_B2C_LIST";
	public static final String HOTEL_ADD = "HOTEL_ADD";
	public static final String HOTEL_PREFERENCE = "HOTEL_PREFERENCE";
	public static final String PREFERENCES_PRICE_RANGE = "PREFERENCES_PRICE_RANGE";
	public static final String PREFERENCES_ADDITIONAL_SERVICES = "PREFERENCES_ADDITIONAL_SERVICES";
	public static final String PREFERENCES_RECOMMENDED_SORT = "PREFERENCES_RECOMMENDED_SORT";
	public static final String SPECTATORS_LIST = "SPECTATORS_LIST";
	public static final String SPECTATORS_ADD = "SPECTATORS_ADD";
	public static final String TA_LIST = "TA_LIST";
	public static final String TA_ADD = "TA_ADD";
	public static final String USER_LIST = "USER_LIST";
	public static final String USER_ADD = "USER_ADD";
	public static final String REPORT_RESERVATIONS = "REPORT_RESERVATIONS";
	public static final String REPORT_RESERVATIONS_B2C = "REPORT_RESERVATIONS_B2C";
	public static final String REPORT_ADDITIONAL_SERVICES = "REPORT_ADDITIONAL_SERVICES";
	public static final String REPORT_CLIENTS = "REPORT_CLIENTS";
	public static final String REPORT_FINANCE_ANNUAL = "REPORT_FINANCE_ANNUAL";
	public static final String REPORT_FINANCE_TOTAL = "REPORT_FINANCE_TOTAL";
	public static final String REPORT_HOTELS = "REPORT_HOTELS";
	public static final String REPORT_ADDITIONAL_SERVICE_MANAGE = "REPORT_ADDITIONAL_SERVICE_MANAGE";
	public static final String REPORT_BY_RESERVATIONS = "REPORT_BY_RESERVATIONS";
	public static final String REPORT_BY_RESERVATIONS_B2C = "REPORT_BY_RESERVATIONS_B2C";
	public static final String API_USER_LIST = "API_USER_LIST";
	public static final String PREFERENCES_CITIES = "CITIES";
	public static final String PREFERENCES_REGIONS = "REGIONS";
	public static final String REPORT_HOTELS_FOR_ANALYSIS = "REPORT_HOTELS_FOR_ANALYSIS";
	public static final String REPORT_VOLUME_OF_RESERVATIONS = "REPORT_VOLUME_OF_RESERVATIONS";

	public AdminHomePage(String panelId) {
		BreadCrumbPanel panel;
		switch (panelId) {
			case HOTEL_LIST:
				panel = new ListHotelsPanel("panel", breadCrumbBar);
			break;
			case HOTEL_ADD:
				panel = new AddHotelPanel("panel", breadCrumbBar);
			break;
			case HOTEL_PREFERENCE:
				panel = new ReferenceHotelPanel("panel", breadCrumbBar);
			break;
			case PREFERENCES_PRICE_RANGE:
				panel = new PriceRangePanel("panel", breadCrumbBar);
			break;
			case PREFERENCES_ADDITIONAL_SERVICES:
				panel = new PriceAdditionalServicePanel("panel", breadCrumbBar);
			break;
			case PREFERENCES_RECOMMENDED_SORT:
				panel = new HotelSortablePanel("panel", breadCrumbBar);
			break;
			case PREFERENCES_REGIONS:
				panel = new ListRegionPanel("panel", breadCrumbBar);
			break;
			case PREFERENCES_CITIES:
				panel = new ListCityPanel("panel", breadCrumbBar);
			break;
			case SPECTATORS_LIST:
				panel = new ListSpectatorsPanel("panel", breadCrumbBar);
			break;
			case SPECTATORS_ADD:
				panel = new AddSpectatorsPanel("panel", breadCrumbBar);
			break;
			case TA_LIST:
				panel = new ListTourAgentsPanel("panel", breadCrumbBar);
			break;
			case TA_ADD:
				panel = new AddTourAgentsPanel("panel", breadCrumbBar);
			break;
			case USER_LIST:
				panel = new ListUsersPanel("panel", breadCrumbBar, null, null);
			break;
			case USER_ADD:
				panel = new AddUserPanel("panel", breadCrumbBar, null);
			break;
			case REPORT_RESERVATIONS:
				panel = new AllReservationListPanel("panel", breadCrumbBar, false);
			break;
			case REPORT_RESERVATIONS_B2C:
				panel = new AllReservationListPanel("panel", breadCrumbBar, true);
				break;
			case REPORT_ADDITIONAL_SERVICE_MANAGE:
				panel = new AdditionalServiceManageListPanel("panel", breadCrumbBar);
				break;
			case REPORT_ADDITIONAL_SERVICES:
				panel = new AdditionalServiceReportPanel("panel", breadCrumbBar);
			break;
			case REPORT_CLIENTS:
				panel = new ClientReportPanel("panel", breadCrumbBar);
			break;
			case REPORT_FINANCE_ANNUAL:
				panel = new FinanceAnnualReportPanel("panel", breadCrumbBar);
			break;
			case REPORT_FINANCE_TOTAL:
				panel = new FinanceTotalReportPanel("panel", breadCrumbBar);
			break;
			case REPORT_HOTELS:
				panel = new HotelReportPanel("panel", breadCrumbBar);
			break;
			case REPORT_HOTELS_FOR_ANALYSIS:
				panel = new HotelReportForAnalysisPanel("panel", breadCrumbBar);
				break;
			case HOTEL_B2C_LIST:
				panel = new ListHotelsB2CPanel("panel", breadCrumbBar);
			break;
			case API_USER_LIST:
				panel = new ListApiUserPanel("panel", breadCrumbBar);
			break;
			case REPORT_BY_RESERVATIONS:
				panel = new ReportByReservationsPanel("panel", breadCrumbBar);
			break;
			case REPORT_BY_RESERVATIONS_B2C:
				panel = new ReportB2CByReservationsPanel("panel", breadCrumbBar);
			break;
			case REPORT_VOLUME_OF_RESERVATIONS:
				panel = new ReservationVolumeReportPanel("panel", breadCrumbBar);
				break;
			default:
				panel = new AdminHomePanel("panel", breadCrumbBar);
			break;
		}
		panel.setRenderBodyOnly(true);
		add(panel);
		breadCrumbBar.setActive(panel);
	}

	@Override
	public boolean isAuthorized(Map<ActionRight, Action> actionMap) {
		return getMySession().getUser().getType().getId() == User.TYPE_ADMIN_USER;
	}

	@Override
	public Class<?> implementedClass() {
		return this.getClass();
	}

	@Override
	public String getTitle() {
		return "";
	}
}
