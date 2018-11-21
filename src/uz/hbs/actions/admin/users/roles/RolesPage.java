package uz.hbs.actions.admin.users.roles;

import java.util.Map;

import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;

import uz.hbs.beans.Action;
import uz.hbs.beans.User;
import uz.hbs.enums.ActionRight;
import uz.hbs.template.MyPage;

public class RolesPage extends MyPage {
	private static final long serialVersionUID = 1L;
	public static final String LIST = "LIST";
	public static final String ADD = "ADD";

	public RolesPage(String panelId) {
		this(panelId, null);
	}

	public RolesPage(String panelId, User user) {
		BreadCrumbPanel panel;
		switch (panelId) {
			case LIST:
				panel = new ListRolesPanel("panel", breadCrumbBar);
			break;
			case ADD:
				panel = new AddRolesPanel("panel", breadCrumbBar);
			break;
			default:
				panel = new ListRolesPanel("panel", breadCrumbBar);
			break;
		}
		panel.setRenderBodyOnly(true);
		add(panel);
		breadCrumbBar.setActive(panel);
	}

	@Override
	public boolean isAuthorized(Map<ActionRight, Action> actionMap) {
		return actionMap.containsKey(ActionRight.ROLE_LIST);
	}

	@Override
	public String getTitle() {
		return getString("roles.list");
	}

	@Override
	public Class<?> implementedClass() {
		return this.getClass();
	}
}
