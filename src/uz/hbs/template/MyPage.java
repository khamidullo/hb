package uz.hbs.template;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.devutils.debugbar.DebugBar;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uz.hbs.components.panels.system.userfeedback.UserFeedbackPanel;
import uz.hbs.markup.navigation.MyBreadCrumbBar;
import uz.hbs.security.AuthenticatedWebPage;
import uz.hbs.session.MySession;

public abstract class MyPage extends WebPage implements AuthenticatedWebPage, IMyLogger {
	private static final long serialVersionUID = 1L;
	protected Logger logger = LoggerFactory.getLogger(implementedClass());
	protected MyBreadCrumbBar breadCrumbBar;

	public MyPage() {
		super();

		add(new Label("title", getTitle()));
		add(new MyHeader("header"));
		DebugBar debugBar = new DebugBar("debug");
		debugBar.add(new AttributeModifier("style", "background-color: #ffffff; color: #000000; z-index: 1000; white-space: nowrap; position: fixed; top: 300px; right:-240px; letter-spacing: 2px; -webkit-transform: rotate(-90deg); -moz-transform: rotate(-90deg); -ms-transform: rotate(-90deg); -o-transform: rotate(-90deg);"));
		debugBar.setVisible(getApplication().usesDevelopmentConfig());
		debugBar.setVisibilityAllowed(getApplication().usesDevelopmentConfig());
		add(debugBar);
		WebMarkupContainer breadCrumbContainer = new WebMarkupContainer("breadCrumbContainer");
		breadCrumbBar = new MyBreadCrumbBar("breadCrumbBar");

		breadCrumbContainer.add(breadCrumbBar);
		add(breadCrumbContainer);
		
		add(new UserFeedbackPanel("userFeedbackPanel"));
		
		add(new MyFooterPanel("footer", false));
	}

	public abstract String getTitle();

	public MySession getMySession() {
		return (MySession) getSession();
	}
}
