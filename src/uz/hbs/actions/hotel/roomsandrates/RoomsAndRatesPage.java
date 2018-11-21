package uz.hbs.actions.hotel.roomsandrates;

import java.util.Map;

import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;

import uz.hbs.actions.hotel.roomsandrates.rate.ListRatePlanePanel;
import uz.hbs.beans.Action;
import uz.hbs.enums.ActionRight;
import uz.hbs.template.MyPage;

public class RoomsAndRatesPage extends MyPage {
	private static final long serialVersionUID = 1L;
	public static final String ROOM_LIST = "ROOM_LIST";
	public static final String AVAILABILITY_ROOM_LIST = "AVAILABILITY_ROOM_LIST";
	public static final String RATE_PLANE = "RATE_PLANE";
	public static final String ADD = "ADD";

	public RoomsAndRatesPage(String panelId) {
		BreadCrumbPanel panel;
		switch (panelId) {
			case ROOM_LIST:
				panel = new ListRoom("panel", breadCrumbBar, getMySession().getUser().getHotelsusers_id());
			break;
			case AVAILABILITY_ROOM_LIST:
				panel = new ListAvailabilityRoom("panel", breadCrumbBar, null);
			break;
			case RATE_PLANE:
				panel = new ListRatePlanePanel("panel", breadCrumbBar, null);
			break;
			default:
				panel = new ListRoom("panel", breadCrumbBar, getMySession().getUser().getHotelsusers_id());
			break;
		}
		panel.setRenderBodyOnly(true);
		add(panel);
		breadCrumbBar.setActive(panel);
	}

	@Override
	public boolean isAuthorized(Map<ActionRight, Action> actionMap) {
		return actionMap.containsKey(ActionRight.HOTEL_USER_ROOM_AND_RATE_MANAGEMENT);
	}

	@Override
	public String getTitle() {
		return getString("hotels.room.list");
	}

	@Override
	public Class<?> implementedClass() {
		return getClass();
	}
}