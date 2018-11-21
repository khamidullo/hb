package uz.hbs.actions.admin.users;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.beans.User;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;
import uz.hbs.session.MySession;
import uz.hbs.utils.CommonUtil;
import uz.hbs.utils.DigestUtil;
import uz.hbs.utils.RequiredFieldAjaxCallListener;

public class UserChangePasswordPanel extends MyBreadCrumbPanel {
	private static final long serialVersionUID = 1L;
	private User user;
	
	public UserChangePasswordPanel(String id, IBreadCrumbModel breadCrumbModel, User user) {
		super(id, breadCrumbModel);
		this.user = user;
		add(new MyForm("form", user));
	}

	
	private class MyForm extends Form<User>{
		private static final long serialVersionUID = 1L;
		private MyFeedbackPanel feedback;

		public MyForm(String id, User user) {
			super(id, new CompoundPropertyModel<User>(new User(user.getId())));
			
			add(feedback = new MyFeedbackPanel("feedback"));
			feedback.setOutputMarkupId(true);
			
			PasswordTextField password, password2;
			add(password = new PasswordTextField("password"));
			password.setRequired(true);
			password.setLabel(new StringResourceModel("users.password", null));
			add(password2 = new PasswordTextField("password2"));
			password2.setRequired(true);
			password2.setLabel(new StringResourceModel("users.password.confirm", null));
			add(new EqualPasswordInputValidator(password, password2));
			add(new IndicatingAjaxButton("submit") {
				private static final long serialVersionUID = 1L;
				
				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
					super.onError(target, form);
					target.add(feedback);
				}
				
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					User user = (User) form.getDefaultModelObject();
					try {
						user.setPassword(DigestUtil.getDigest(user.getPassword()));
						user.setInitiator_user_id(((MySession) getSession()).getUser().getId());
						new MyBatisHelper().update("updateUserPassword", user);
						feedback.success(getString("user.change.password.success"));
					} catch (Exception e) {
						logger.error("Exception", e);
						feedback.error(getString("user.change.password.fail"));
					} finally {
						target.add(feedback);
					}
				}
				
				@Override
				protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
					super.updateAjaxAttributes(attributes);
					attributes.getAjaxCallListeners().add(new RequiredFieldAjaxCallListener(MyForm.this.getMarkupId(), ((MySession) getSession()).getLocale().getLanguage()));
				}
			});
			CommonUtil.setFormComponentRequired(this);
		}
	}

	@Override
	public IModel<String> getTitle() {
		return new StringResourceModel("users.change.password", new Model<User>(user));
	}

	@Override
	public Class<?> implementedClass() {
		return this.getClass();
	}
}
