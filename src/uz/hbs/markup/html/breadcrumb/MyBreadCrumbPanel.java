package uz.hbs.markup.html.breadcrumb;

import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.model.IModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uz.hbs.session.MySession;
import uz.hbs.template.IMyLogger;

public abstract class MyBreadCrumbPanel extends BreadCrumbPanel implements IMyLogger {
	private static final long serialVersionUID = 1L;
	protected Logger logger = LoggerFactory.getLogger(implementedClass());
	
	public MyBreadCrumbPanel(String id, IBreadCrumbModel breadCrumbModel) {
		super(id, breadCrumbModel);
	}

	public MyBreadCrumbPanel(String id, IBreadCrumbModel breadCrumbModel, IModel<?> model) {
		super(id, breadCrumbModel, model);
	}

	@Override
	public abstract IModel<String> getTitle();
	
	protected MySession getMySession() {
		return (MySession) getSession();
	}
}
