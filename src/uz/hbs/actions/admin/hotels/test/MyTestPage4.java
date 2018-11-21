package uz.hbs.actions.admin.hotels.test;

import java.util.Map;

import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;

import uz.hbs.actions.hotel.reports.view.HotelInformationPanel;
import uz.hbs.beans.Action;
import uz.hbs.enums.ActionRight;
import uz.hbs.template.MyPage;

public class MyTestPage4 extends MyPage {
	private static final long serialVersionUID = 1L;
	
	public MyTestPage4() {
		
		BreadCrumbPanel panel = new HotelInformationPanel("panel", breadCrumbBar, 191l); //, 43L);
		panel.setRenderBodyOnly(true);
		add(panel);
		breadCrumbBar.setActive(panel);
	}

	@Override
	public boolean isAuthorized(Map<ActionRight, Action> actionMap) {
		return true;
	}

	@Override
	public Class<?> implementedClass() {
		return this.getClass();
	}

	@Override
	public String getTitle() {
		return "Test Page 4";
	}
}
