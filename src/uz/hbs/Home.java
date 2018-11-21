package uz.hbs;

import java.util.Map;

import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;

import uz.hbs.beans.Action;
import uz.hbs.enums.ActionRight;
import uz.hbs.template.MyPage;

public class Home extends MyPage {
	private static final long serialVersionUID = 1L;

	public Home() {
		BreadCrumbPanel panel = new HomePanel("panel", breadCrumbBar);
		panel.setRenderBodyOnly(true);
		add(panel);
		breadCrumbBar.setActive(panel);
	}

	@Override
	public boolean isAuthorized(Map<ActionRight, Action> actionMap) {
		return getMySession().isSignedIn();
	}

	@Override
	public String getTitle() {
		return getString("main_page");
	}

	@Override
	public Class<?> implementedClass() {
		return this.getClass();
	}
}
