package uz.hbs.actions.hotel.reports;

import java.util.Map;

import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;

import uz.hbs.actions.hotel.reports.view.HotelInformationPanel;
import uz.hbs.beans.Action;
import uz.hbs.beans.User;
import uz.hbs.enums.ActionRight;
import uz.hbs.template.MyPage;

public class HotelInformationPage extends MyPage {
	private static final long serialVersionUID = 1L;
	
	public HotelInformationPage() {
		BreadCrumbPanel panel;
		panel = new HotelInformationPanel("panel", breadCrumbBar, getMySession().getUser().getHotelsusers_id());
		panel.setRenderBodyOnly(true);
		add(panel);
		breadCrumbBar.setActive(panel);
	}
	
	public HotelInformationPage(ReservationReportPanel panel) {
		panel.setRenderBodyOnly(true);
		add(panel);
		breadCrumbBar.setActive(panel);
	}

	@Override
	public boolean isAuthorized(Map<ActionRight, Action> actionMap) {
		return getMySession().getUser().getType().getId() == User.TYPE_HOTEL_USER;
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