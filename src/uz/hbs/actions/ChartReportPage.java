package uz.hbs.actions;

import java.util.Map;

import uz.hbs.beans.Action;
import uz.hbs.enums.ActionRight;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;
import uz.hbs.template.MyPage;

public class ChartReportPage extends MyPage {
	private static final long serialVersionUID = 1L;
	public ChartReportPage() {
		MyFeedbackPanel feedback = new MyFeedbackPanel("feedback");
		add(feedback);
	}
	
	@Override
	public boolean isAuthorized(Map<ActionRight, Action> actionMap) {
		return getMySession().isSignedIn();
	}

	@Override
	public String getTitle() {
		return "Chart of Daily payment report by minutes";
	}

	@Override
	public Class<?> implementedClass() {
		return this.getClass();
	}
}
