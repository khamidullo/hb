package uz.hbs.markup.html.form.alertbox;

import org.apache.wicket.core.util.string.JavaScriptUtils;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.IFeedbackMessageFilter;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

public class MyFeedbackPanel extends FeedbackPanel {
	private static final long serialVersionUID = 1L;

	public MyFeedbackPanel(String id) {
		super(id);
		setOutputMarkupId(true);
	}

	public MyFeedbackPanel(String id, IFeedbackMessageFilter filter) {
		super(id, filter);
		setOutputMarkupId(true);
	}

	@Override
	protected String getCSSClass(FeedbackMessage message) {
		String level = "";
		switch (message.getLevelAsString().toUpperCase()) {
			case "UNDEFINED":
			case "ERROR":
			case "FATAL":
				level = "danger";
			break;
			case "DEBUG":
			case "WARNING":
				level = "warning";
			break;
			case "INFO":
			case "SUCCESS":
				level = message.getLevelAsString().toLowerCase();
			break;
			default:
				level = "danger";
			break;
		}
		return "alert alert-dismissible alert2-" + level;
	}

	@Override
	protected void onAfterRender() {
		super.onAfterRender();
		JavaScriptUtils.writeJavaScript(getResponse(), "setTimeout(function() { hideFeedback('" + getMarkupId() + "', 1500) }, 10000);");
	}
}
