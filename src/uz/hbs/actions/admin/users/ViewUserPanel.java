package uz.hbs.actions.admin.users;

import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.beans.User;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;
import uz.hbs.utils.DigestUtil;
import uz.hbs.utils.PasswordGeneratorUtil;

public class ViewUserPanel extends MyBreadCrumbPanel {
	private static final long serialVersionUID = 1L;
	private User user;

	public ViewUserPanel(String id, IBreadCrumbModel breadCrumbModel, final User user) {
		super(id, breadCrumbModel);
		this.user = user;
		final MyFeedbackPanel feedback;
		add(feedback = new MyFeedbackPanel("feedback"));
		feedback.setOutputMarkupId(true);
		WebMarkupContainer container = new WebMarkupContainer("container");
		add(container);
		container.add(new Label("name", new Model<String>(user.getName())));
		container.add(new Label("login", new Model<String>(user.getLogin())));
		container.add(new Label("email", new Model<String>(user.getEmail())));
		container.add(new Label("phone_number", new Model<String>(user.getPhone_number())));
		container.add(new Label("status", new StringResourceModel("users.status" + user.getStatus().getId(), null)));
		container.add(new Label("type", new StringResourceModel("users.type" + user.getType().getId(), null)));
		String workerName = user.getWork() != null && user.getWork().getId() != null ? ((User)new MyBatisHelper().selectOne("selectUserByUserId", user.getWork().getId())).getName() : "";
		container.add(new Label("worker", workerName));

		container.add(new Link<Void>("changePasswordLink"){
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				activate(new IBreadCrumbPanelFactory() {
					private static final long serialVersionUID = 1L;

					@Override
					public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
						return new UserChangePasswordPanel(componentId, breadCrumbModel, user);
					}
				});
			}
		});
		container.add(new Link<Void>("userRoleLink"){
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onClick() {
				activate(new IBreadCrumbPanelFactory() {
					private static final long serialVersionUID = 1L;
					
					@Override
					public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
						return new RoleUserPanel(componentId, breadCrumbModel, user);
					}
				});
			}
		});
		container.add(new Link<Void>("resetPasswordLink"){
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onClick() {
				String passphase = "";
				try {
					passphase = PasswordGeneratorUtil.getPassword(6);
					user.setPassword(DigestUtil.getDigest(passphase));
					new MyBatisHelper().update("updateUserPassword", user);
					feedback.success(new StringResourceModel("user.reset.password.success", null, new Object[]{passphase}).getString());
				} catch (Exception e) {
					logger.error("Exception", e);
					feedback.error(getString("user.reset.password.fail"));
				}
			}
		});
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
