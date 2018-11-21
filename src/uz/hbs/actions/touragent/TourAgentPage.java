package uz.hbs.actions.touragent;

import java.util.Map;

import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;

import uz.hbs.actions.touragent.newbooking.NewBookingPanel;
import uz.hbs.actions.touragent.reports.ReportByReservationsPanel;
import uz.hbs.actions.touragent.reports.ReportsPanel;
import uz.hbs.actions.touragent.reservations.ReservationsPanel;
import uz.hbs.beans.Action;
import uz.hbs.beans.User;
import uz.hbs.enums.ActionRight;
import uz.hbs.template.MyPage;

public class TourAgentPage extends MyPage {
	private static final long serialVersionUID = 1L;
	public static final String NEW_BOOKING = "NEW_BOOKING";
	public static final String RESERVATIONS = "RESERVATIONS";
	public static final String REPORTS = "REPORTS";
	public static final String REPORTS_BY_RESERVATIONS = "REPORTS_BY_RESERVATIONS";

	public TourAgentPage(String panelId, boolean fromHomePage, Byte reservationType, Byte reservationStatus) {
		BreadCrumbPanel panel;
		switch (panelId) {
			case NEW_BOOKING:
				panel = new NewBookingPanel("panel", breadCrumbBar);
			break;
			case RESERVATIONS:
				panel = new ReservationsPanel("panel", breadCrumbBar, fromHomePage, reservationType, reservationStatus);
			break;
			case REPORTS:
				panel = new ReportsPanel("panel", breadCrumbBar);
			break;
			case REPORTS_BY_RESERVATIONS:
				panel = new ReportByReservationsPanel("panel", breadCrumbBar);
			break;
			default:
				panel = new NewBookingPanel("panel", breadCrumbBar);
			break;
		}
		panel.setRenderBodyOnly(true);
		add(panel);
		breadCrumbBar.setActive(panel);
	}

	@Override
	public boolean isAuthorized(Map<ActionRight, Action> actionMap) {
		return getMySession().getUser().getType().getId() == User.TYPE_TOURAGENT_USER;
	}

	@Override
	public String getTitle() {
		return "";
	}

	@Override
	public Class<?> implementedClass() {
		return this.getClass();
	}
}
