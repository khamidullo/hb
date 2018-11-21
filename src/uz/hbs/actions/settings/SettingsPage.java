package uz.hbs.actions.settings;

import java.util.Map;

import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.actions.admin.users.UserChangePasswordPanel;
import uz.hbs.beans.Action;
import uz.hbs.enums.ActionRight;
import uz.hbs.template.MyPage;

public class SettingsPage extends MyPage {
	private static final long serialVersionUID = 1L;

	public static final String MAIN_PANEL = "MAIN_PANEL";
	public static final String CHANGE_PASSWORD = "CHANGE_PASSWORD";
	
	public SettingsPage(String panelId) {
		BreadCrumbPanel panel;
		switch (panelId) {
			case MAIN_PANEL:
				panel = new SettingsPanel("panel", breadCrumbBar);
			break;
			case CHANGE_PASSWORD:
				panel = new UserChangePasswordPanel("panel", breadCrumbBar, getMySession().getUser());
			break;
			default:
				panel = new SettingsPanel("panel", breadCrumbBar);
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
		return new StringResourceModel("settings", null).getString();
	}
}
