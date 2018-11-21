package uz.hbs.actions.api.additionalservices;

import java.util.Map;

import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;

import uz.hbs.beans.Action;
import uz.hbs.enums.ActionRight;
import uz.hbs.session.MySession;
import uz.hbs.template.MyPage;

public class AdditionalServicePage extends MyPage {
	private static final long serialVersionUID = 1L;
	public static final String LIST = "LIST";
	public static final String ADD = "ADD";

	
	public AdditionalServicePage(String panelId) {
		BreadCrumbPanel panel;
		switch (panelId) {
			case LIST:
				panel = new AdditionalServiceOrderListPanel("panel", breadCrumbBar, ((MySession) getSession()).getUser().getId());
			break;
			case ADD:
				panel = new AdditionalServiceOrderPanel("panel", breadCrumbBar);
			break;
			default:
				panel = new AdditionalServiceOrderListPanel("panel", breadCrumbBar,((MySession) getSession()).getUser().getId());
			break;
		}
		panel.setRenderBodyOnly(true);
		add(panel);
		breadCrumbBar.setActive(panel);
	}

	@Override
	public boolean isAuthorized(Map<ActionRight, Action> actionMap) {
		return getMySession().isSignedIn();
	}

	@Override
	public Class<?> implementedClass() {
		return this.getClass();
	}

	@Override
	public String getTitle() {
		return getString("additional_service.list");
	}
}