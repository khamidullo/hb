package uz.hbs.security;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.antlr.stringtemplate.StringTemplate;
import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.util.io.IClusterable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uz.hbs.MyWebApplication;
import uz.hbs.beans.Message;
import uz.hbs.beans.User;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.utils.CommonUtil;
import uz.hbs.utils.DateUtil;
import uz.hbs.utils.DigestUtil;
import uz.hbs.utils.MessagesUtil;
import uz.hbs.utils.email.EmailUtil;
import uz.hbs.utils.email.MyStringTemplateGroup;

public class ForgotPasswordPage extends WebPage {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(ForgotPasswordPage.class);

	public ForgotPasswordPage() {
		getSession().setLocale(new Locale("ru"));

		String copyrightYear = new StringResourceModel("copyright_year", null).getString();
		String currentYear = DateUtil.toString(new Date(), "yyyy");
		Label copyRight = new Label("copyRight",
				new StringResourceModel("signin.copyright", null,
						new Object[] { (currentYear.equals(copyrightYear) ? currentYear : copyrightYear + "-" + currentYear)
								+ " <a href=\"http://hotelios.uz/\">Hotelios</a>" }));
		copyRight.setEscapeModelStrings(false);
		add(copyRight);

		add(new MyForm("form"));
	}

	private class MyForm extends StatelessForm<Object> {
		private static final long serialVersionUID = 1L;
		private final ForgotPasswordModel model = new ForgotPasswordModel();

		public MyForm(String id) {
			super(id);
			final FeedbackPanel feedback = new FeedbackPanel("feedback");
			feedback.setOutputMarkupId(true);
			add(feedback);

			this.setOutputMarkupId(true);
			setDefaultModel(new CompoundPropertyModel<ForgotPasswordModel>(model));

			final RequiredTextField<String> username = new RequiredTextField<String>("username");
			username.setLabel(new StringResourceModel("signin.username", null));
			username.setOutputMarkupId(true);
			add(username);

			final Link<Void> back = new Link<Void>("back") {
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick() {
					setResponsePage(new SignIn());
				}
			};
			back.setOutputMarkupPlaceholderTag(true);
			back.setVisibilityAllowed(false);
			add(back);

			final AjaxButton submit = new AjaxButton("submit") {
				private static final long serialVersionUID = 1L;

				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
					super.onError(target, form);
					target.add(feedback);
				}

				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					super.onSubmit(target, form);

					Map<String, Serializable> param = new HashMap<>();
					param.put("login", model.getUsername());

					User user = new MyBatisHelper().selectOne("selectUsers", param);
					if (user != null) {
						if (user.getStatus().getId() == User.STATUS_ACTIVE) {
							String email = CommonUtil.getUserEmail(user);
							if (email != null) {
								String token = "";
								try {
									token = DigestUtil.getDigest(user.getLogin() + System.currentTimeMillis());
								} catch (Exception e) {
									logger.error("Exception", e);
								}
								String subject = new StringResourceModel("users.reset_password.email.subject", null).getString();
								String port = "";
								if (getApplication().getConfigurationType() == RuntimeConfigurationType.DEVELOPMENT) {
									port = ":8443";
								}
								String link = "https://system.hotelios.uz" + port + "/restore?id=" + user.getId() + "&code=" + token;

								Date change_password_expiry_date = new MyBatisHelper().selectOne("selectCustomDateTime", 3);

								MyStringTemplateGroup templateGroup = new MyStringTemplateGroup("mailgroup");
								templateGroup.setFileCharEncoding("UTF-8");
								StringTemplate st = templateGroup.getInstanceOf("uz/hbs/utils/email/templates/passwordrestore/ResetPassword_ru.st");
								st.setAttribute("username", user.getLogin());
								st.setAttribute("link", link);
								st.setAttribute("date", DateUtil.toString(change_password_expiry_date, MyWebApplication.DATE_FORMAT));
								st.setAttribute("time", DateUtil.toString(change_password_expiry_date, MyWebApplication.TIME_SHORT_FORMAT));

								final Message message = MessagesUtil.createMessage(email, subject, st.toString(), null, null, user.getId());

								new Thread() {
									public void run() {
										EmailUtil.sendSimpleTextEmail(message);
									};
								}.start();

								user.setChange_password_expiry_date(change_password_expiry_date);
								user.setChange_password_token(token);
								new MyBatisHelper().update("updateUserChangePasswordToken", user);

								feedback.success(new StringResourceModel("users.reset_password.success", null).getString());
							} else {
								logger.debug("Email address not assigned to user, Login=" + model.getUsername());
								feedback.error(new StringResourceModel("users.reset_password.error.user_not_active_or_not_found", null).getString());
							}
						} else {
							logger.debug("User is not active, Login=" + model.getUsername() + ", Status=" + user.getStatus().getId());
							feedback.error(new StringResourceModel("users.reset_password.error.user_not_active_or_not_found", null).getString());
						}
					} else {
						logger.debug("User not found, Login=" + model.getUsername());
						feedback.error(new StringResourceModel("users.reset_password.error.user_not_active_or_not_found", null).getString());
					}

					username.setVisible(false);
					back.setVisibilityAllowed(true);
					this.setVisible(false);
					target.add(feedback);
					target.add(username);
					target.add(back);
					target.add(this);
				}
			};
			submit.setOutputMarkupId(true);
			add(submit);
		}
	}

	@SuppressWarnings("unused")
	private class ForgotPasswordModel implements IClusterable {
		private static final long serialVersionUID = 1L;
		private String username;

		public ForgotPasswordModel() {
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}
	}
}
