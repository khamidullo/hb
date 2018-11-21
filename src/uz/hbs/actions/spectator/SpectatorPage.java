package uz.hbs.actions.spectator;

import java.util.Map;

import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.actions.spectator.report.ReportsPanel;
import uz.hbs.beans.Action;
import uz.hbs.enums.ActionRight;
import uz.hbs.template.MyPage;

public class SpectatorPage extends MyPage {
	private static final long serialVersionUID = 1L;
	public static final String REPORTS = "REPORTS";

	public SpectatorPage(String panelId) {
		BreadCrumbPanel panel;
		switch (panelId) {
			case REPORTS:
				panel = new ReportsPanel("panel", breadCrumbBar);
			break;
			default:
				panel = new ReportsPanel("panel", breadCrumbBar);
			break;
		}
		panel.setRenderBodyOnly(true);
		add(panel);
		breadCrumbBar.setActive(panel);
	}

	@Override
	public boolean isAuthorized(Map<ActionRight, Action> actionMap) {
		return actionMap.containsKey(ActionRight.SPECTATOR_USER_REPORTS);
	}

	@Override
	public Class<?> implementedClass() {
		return this.getClass();
	}

	@Override
	public String getTitle() {
		return new StringResourceModel("touragents.reports", null).getString();
	}
}
