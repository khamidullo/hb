package uz.hbs.actions.admin.api;

import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.beans.User;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;

public class ViewApiUserPanel extends MyBreadCrumbPanel {
	private static final long serialVersionUID = 1L;
	private User user;

	public ViewApiUserPanel(String id, IBreadCrumbModel breadCrumbModel, final User user) {
		super(id, breadCrumbModel);
		this.user = user;
		final MyFeedbackPanel feedback;
		add(feedback = new MyFeedbackPanel("feedback"));
		feedback.setOutputMarkupId(true);
		WebMarkupContainer container = new WebMarkupContainer("container");
		add(container);
		container.add(new Label("name", user.getName()));
		container.add(new Label("login", user.getLogin()));
		container.add(new Label("email", user.getEmail()));
		container.add(new Label("phone_number", new Model<String>(user.getPhone_number())));
		container.add(new Label("status", new StringResourceModel("users.status" + user.getStatus().getId(), null)));
		container.add(new Label("type", new StringResourceModel("users.type" + user.getType().getId(), null)));
		container.add(new Label("password", user.getPassword()));
		container.add(new Label("access_key", user.getAccess_key()));
	}

	@Override
	public IModel<String> getTitle() {
		return new StringResourceModel("users.view", new Model<User>(user));
	}

	@Override
	public Class<?> implementedClass() {
		return this.getClass();
	}
}
