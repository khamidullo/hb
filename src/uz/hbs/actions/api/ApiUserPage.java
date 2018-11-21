package uz.hbs.actions.api;

import java.util.Map;

import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;

import uz.hbs.actions.api.reports.ReportsPanel;
import uz.hbs.actions.api.reports.ReservationsPanel;
import uz.hbs.beans.Action;
import uz.hbs.beans.User;
import uz.hbs.enums.ActionRight;
import uz.hbs.template.MyPage;

public class ApiUserPage  extends MyPage {
	private static final long serialVersionUID = 1L;
	public static final String RESERVATIONS = "RESERVATIONS";
	public static final String REPORTS = "REPORTS";

	public ApiUserPage(String panelId, boolean fromHomePage, Byte reservationType, Byte reservationStatus) {
		BreadCrumbPanel panel;
		switch (panelId) {
			case RESERVATIONS:
				panel = new ReservationsPanel("panel", breadCrumbBar, fromHomePage, reservationType, reservationStatus);
			break;
			case REPORTS:
				panel = new ReportsPanel("panel", breadCrumbBar);
			break;
			default:
				panel = new ReservationsPanel("panel", breadCrumbBar, fromHomePage, reservationType, reservationStatus);
			break;
		}
		panel.setRenderBodyOnly(true);
		add(panel);
		breadCrumbBar.setActive(panel);
	}

	@Override
	public boolean isAuthorized(Map<ActionRight, Action> actionMap) {
		return getMySession().getUser().getType().getId() == User.TYPE_API;
	}

	@Override
	public String getTitle() {
		return "";
	}

	@Override
	public Class<?> implementedClass() {
		return getClass();
	}
}
