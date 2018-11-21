package uz.hbs.actions.hotel.reports;

import java.util.Map;

import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;

import uz.hbs.actions.admin.hotels.ListHotelsPanel;
import uz.hbs.beans.Action;
import uz.hbs.enums.ActionRight;
import uz.hbs.template.MyPage;

public class HotelReportPage extends MyPage {
	private static final long serialVersionUID = 1L;
	public static final String REPORTS_LIST = "REPORTS_LIST";
	public static final String REPORTS_BY_RESERVATIONS = "REPORTS_BY_RESERVATIONS";

	public HotelReportPage(String panelId) {
		BreadCrumbPanel panel;
		switch (panelId) {
			case REPORTS_LIST:
				panel = new ReservationReportPanel("panel", breadCrumbBar);
			break;
			case REPORTS_BY_RESERVATIONS:
				panel = new ReportByReservationsPanel("panel", breadCrumbBar);
			break;
			default:
				panel = new ListHotelsPanel("panel", breadCrumbBar);
			break;
		}
		panel.setRenderBodyOnly(true);
		add(panel);

		breadCrumbBar.setActive(panel);
	}
	
	public HotelReportPage(ReservationReportPanel panel) {
		panel.setRenderBodyOnly(true);
		add(panel);
		breadCrumbBar.setActive(panel);
	}

	@Override
	public boolean isAuthorized(Map<ActionRight, Action> actionMap) {
		return actionMap.containsKey(ActionRight.HOTEL_USER_REPORTS);
	}

	@Override
	public Class<?> implementedClass() {
		return this.getClass();
	}

	@Override
	public String getTitle() {
		return getString("hotels.reservation.report.title");
	}
}
