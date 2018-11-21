package uz.hbs.security;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.io.IClusterable;
import org.apache.wicket.util.string.StringValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uz.hbs.beans.User;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.utils.DateUtil;
import uz.hbs.utils.DigestUtil;

public class PasswordRestorePage extends WebPage {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(PasswordRestorePage.class);
	
	public PasswordRestorePage(PageParameters parameters) {
		getSession().setLocale(new Locale("ru"));
		
		String copyrightYear = new StringResourceModel("copyright_year", null).getString();
		String currentYear = DateUtil.toString(new Date(), "yyyy");
		Label copyRight = new Label("copyRight", new StringResourceModel("signin.copyright", null,
				new Object[] { (currentYear.equals(copyrightYear) ? currentYear :  copyrightYear + "-" + currentYear) + " <a href=\"http://hotelios.uz/\">Hotelios</a>" }));
		copyRight.setEscapeModelStrings(false);
		add(copyRight);
		
		StringValue id = parameters.get("id");
		StringValue token = parameters.get("code");
		
		long idValue = 0l;
		String tokenValue = ".";
		if (id != null && token != null && !id.isEmpty() && !token.isEmpty()) {
			idValue = id.toLong();
			tokenValue = token.toString();
		}

		Map<String, Serializable> param = new HashMap<String, Serializable>();
		param.put("id", idValue);
		param.put("change_password_token", tokenValue);
		User user = new MyBatisHelper().selectOne("selectUserForRestorePassword", param);
		
		add(new MyForm("form", user));
	}
	
	private class MyForm extends StatelessForm<Object> {
		private static final long serialVersionUID = 1L;
		public MyForm(String id, final User user) {
			super(id);
			
			final FeedbackPanel feedback = new FeedbackPanel("feedback");
			feedback.setOutputMarkupId(true);
			add(feedback);
			
			setOutputMarkupId(true);
			setDefaultModel(new CompoundPropertyModel<PasswordRestoreModel>(new PasswordRestoreModel()));
			
			if (user == null) {
				feedback.error(getString("users.password_recovery_link_expired"));
			}
			
			add(new Link<Void>("forgot_password") {
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick() {
					setResponsePage(new ForgotPasswordPage());
				}
				
				@Override
				public boolean isVisible() {
					return user == null;
				}
			}); 
			
			final Link<Void> signInLink = new Link<Void>("sign_in") {
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick() {
					setResponsePage(new SignIn());
				}
			};
			signInLink.setVisibilityAllowed(false);
			signInLink.setOutputMarkupPlaceholderTag(true);
			add(signInLink);
			
			Label loginLabel = new Label("loginLabel", (user != null ? getString("signin.username") + ": " + user.getLogin() : ""));
			add(loginLabel);
			
			final PasswordTextField password1 = new PasswordTextField("password1");
			password1.setRequired(true);
			password1.setLabel(new StringResourceModel("users.password", null));
			password1.setVisible(user != null);
			password1.setOutputMarkupId(true);
			add(password1);
			
			final PasswordTextField password2 = new PasswordTextField("password2");
			password2.setRequired(true);
			password2.setLabel(new StringResourceModel("users.password.confirm", null));
			password2.setVisible(user != null);
			password2.setOutputMarkupId(true);
			add(password2);
			
			add(new EqualPasswordInputValidator(password1, password2));
			
			final AjaxButton submit= new AjaxButton("submit") {
				private static final long serialVersionUID = 1L;
				
				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
					super.onError(target, form);
					target.add(feedback);
				}
				
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					super.onSubmit(target, form);
					
					PasswordRestoreModel model = (PasswordRestoreModel) form.getDefaultModelObject();
					
					try {
						user.setPassword(DigestUtil.getDigest(model.password1));
						user.setInitiator_user_id(user.getId());
						new MyBatisHelper().update("updateUserPassword", user);
						logger.debug("User password successfully reset, Login=" + user.getLogin());
						feedback.success(getString("user.change.password.success"));
					} catch (Exception e) {
						logger.error("Exception, User password not reset, Login=" + user.getLogin(), e);
						feedback.error(getString("user.change.password.fail"));
					} finally {
						password1.setEnabled(false);
						password2.setEnabled(false);
						signInLink.setVisibilityAllowed(true);
						this.setEnabled(false);

						target.add(password1);
						target.add(password2);
						target.add(signInLink);
						target.add(this);
						target.add(feedback);
					}
				}
				
				@Override
				public boolean isVisible() {
					return user != null;
				}
			};
			submit.setOutputMarkupId(true);
			add(submit);
		}
	}
	
	@SuppressWarnings("unused")
	private class PasswordRestoreModel implements IClusterable {
		private static final long serialVersionUID = 1L;
		private String password1;
		private String password2;
		
		public PasswordRestoreModel() {
		}

		public String getPassword1() {
			return password1;
		}

		public void setPassword1(String password1) {
			this.password1 = password1;
		}

		public String getPassword2() {
			return password2;
		}

		public void setPassword2(String password2) {
			this.password2 = password2;
		}
	}
}
