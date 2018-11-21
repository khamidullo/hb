package uz.hbs.temp;

import java.util.Map;

import uz.hbs.beans.Action;
import uz.hbs.enums.ActionRight;
import uz.hbs.template.MyPage;

public class LoadingIndicator extends MyPage {
	private static final long serialVersionUID = 1L;

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
		return "Testing loading indicator";
	}
}
