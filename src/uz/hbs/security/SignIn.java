package uz.hbs.security;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.markup.html.captcha.CaptchaImageResource;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.protocol.http.request.WebClientInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uz.hbs.Home;
import uz.hbs.beans.Currencies;
import uz.hbs.beans.User;
import uz.hbs.beans.UserSession;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.enums.ActionRight;
import uz.hbs.session.MySession;
import uz.hbs.utils.AuditLogsUtil;
import uz.hbs.utils.DateUtil;
import uz.hbs.utils.DigestUtil;

public final class SignIn extends WebPage {
	private static final long serialVersionUID = 1L;

	private final static Logger logger = LoggerFactory.getLogger(SignIn.class);

	private boolean captcha_visible;

	public SignIn() {
		captcha_visible = false;
		getSession().setLocale(new Locale("ru"));
		add(new SignInForm("signInForm"));

		String copyrightYear = new StringResourceModel("copyright_year", null).getString();
		String currentYear = DateUtil.toString(new Date(), "yyyy");
		Label copyRight = new Label("copyRight", new StringResourceModel("signin.copyright", null,
				new Object[] { (currentYear.equals(copyrightYear) ? currentYear :  copyrightYear + "-" + currentYear) + " <a href=\"http://hotelios.uz/\">Hotelios</a>" }));
		copyRight.setEscapeModelStrings(false);
		add(copyRight);

	}

	@Override
	protected void onConfigure() {
		super.onConfigure();
		if (((MySession) getSession()).getUser() != null && ((MySession) getSession()).isSignedIn())
			setResponsePage(Home.class);
	}

	private class SignInForm extends StatelessForm<Object> {
		private static final long serialVersionUID = 1L;
		private final LoginModel model = new LoginModel();
		private CaptchaImageResource captchaImageResource;
		private String imagePass = randomString(5, 5);
		private WebMarkupContainer captchaBody;
		private WebMarkupContainer captchaInputWrapper;
		private WebMarkupContainer captchaBodyWrapper;
		private NonCachingImage img;
		//private FeedbackPanel feedback;

		public SignInForm(final String id) {
			super(id);
			this.setOutputMarkupId(true);

			setDefaultModel(new CompoundPropertyModel<LoginModel>(model));

			final FeedbackPanel feedback = new FeedbackPanel("feedback");
			feedback.setOutputMarkupId(true);
			add(feedback);

			LoadableDetachableModel<String> langModel = new LoadableDetachableModel<String>() {
				private static final long serialVersionUID = 1L;

				@Override
				protected String load() {
					return "img/" + getSession().getLocale().getLanguage().toLowerCase() + ".png";
				}
			};
			ContextImage langIcon = new ContextImage("lang", langModel);
			langIcon.add(new AttributeModifier("title", new LoadableDetachableModel<String>() {

				private static final long serialVersionUID = 1L;

				@Override
				protected String load() {
					return new StringResourceModel("language." + getSession().getLocale().getLanguage().toLowerCase(), null).getString();
				}
			}));
			add(langIcon);

			List<Locale> langList = new ArrayList<Locale>();
			langList.add(new Locale("ru"));
			// langList.add(new Locale("uz"));
			langList.add(new Locale("en"));

			ListView<Locale> langs = new ListView<Locale>("langs", langList) {
				private static final long serialVersionUID = 1L;

				@Override
				protected void populateItem(ListItem<Locale> item) {
					final String lang = item.getModelObject().getLanguage().toLowerCase();
//					item.add(new AjaxLink<String>("langLink") {
//						private static final long serialVersionUID = 1L;
//
//						@Override
//						public void onClick(AjaxRequestTarget target) {
//							getSession().setLocale(new Locale(lang));
//							target.add(SignIn.this);
//						}
//					}.add(new ContextImage("langImg", "img/" + lang + ".png")).add(
//							new Label("langLabel", new StringResourceModel("language." + lang, null))));
					item.add(new Link<String>("langLink") {
						private static final long serialVersionUID = 1L;
						
						@Override
						public void onClick() {
							getSession().setLocale(new Locale(lang));
						}
					}.add(new ContextImage("langImg", "img/" + lang + ".png")).add(
							new Label("langLabel", new StringResourceModel("language." + lang, null))));
				}
			};
			add(langs);

			add(new RequiredTextField<String>("username").setLabel(new StringResourceModel("signin.username", null)));
			add(new PasswordTextField("password").setLabel(new StringResourceModel("signin.password", null)));

			captchaInputWrapper = new WebMarkupContainer("captchaInputWrapper") {
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isVisible() {
					return captcha_visible;
				};
			};
			captchaInputWrapper.setOutputMarkupPlaceholderTag(true);
			add(captchaInputWrapper);
			captchaBodyWrapper = new WebMarkupContainer("captchaBodyWrapper") {
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isVisible() {
					return captcha_visible;
				}
			};
			captchaBodyWrapper.setOutputMarkupPlaceholderTag(true);
			add(captchaBodyWrapper);

			captchaBody = new WebMarkupContainer("captchaBody");
			captchaBody.setOutputMarkupId(true);
			captchaBody.add(img = updateCaptcha());
			img.setOutputMarkupId(true);
			AjaxLink<String> link = new AjaxLink<String>("captchaLink") {
				private static final long serialVersionUID = 1L;

				public void onClick(final AjaxRequestTarget target) {
					NonCachingImage newimg = updateCaptcha();
					captchaBody.replace(newimg);
					target.add(newimg, img.getMarkupId());
					target.add(captchaBodyWrapper);
					target.appendJavaScript("document.getElementById('captcha').focus();");
				}
			};
			captchaBodyWrapper.add(link);
			link.add(captchaBody);

			RequiredTextField<String> captcha = new RequiredTextField<String>("captcha") {
				private static final long serialVersionUID = 1L;

				protected final void onComponentTag(final ComponentTag tag) {
					super.onComponentTag(tag);
					// FIXME this is original code, before deploy to production replace below code: tag.put("value", "");
					tag.put("value", getApplication().usesDevelopmentConfig() ? imagePass : "");
				}
			};
			captcha.setLabel(new StringResourceModel("signin.captcha", null));
			captchaInputWrapper.add(captcha);

			add(new AjaxButton("signin") {
				private static final long serialVersionUID = 1L;

				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					try {
						// getSession().replaceSession();
						MySession session = (MySession) getSession();

						captcha_visible = session.isCaptchaVisible();

						if (captcha_visible && (model.getCaptcha() == null || !model.getCaptcha().equals(imagePass))) {
							feedback.error(new StringResourceModel("signin.error.captcha", null).getString());
							captchaBody.replace(updateCaptcha());
							return;
						} else {
							User appUser = null;
							if (session.signIn(model.getUsername(), DigestUtil.getDigest(model.getPassword()))) {
								appUser = session.getUser();
								if (appUser.getStatus().getId() == User.STATUS_ACTIVE) {
									// Select last user session before inserting current user session
									UserSession lastUserSession = new MyBatisHelper().selectOne("selectUserSessions", appUser.getId());

									WebClientInfo clientInfo = (WebClientInfo) getSession().getClientInfo();

									UserSession userSessions = new UserSession();
									userSessions.setInitiator_user_id(session.getUser().getId());
									userSessions.setInterface_type(UserSession.INTERFACE_TYPE_WEB);
									userSessions.setApp_type(UserSession.APP_TYPE_BACK_OFFICE_WEB);
									userSessions.setSession_id(session.getId());
									userSessions.setClient_host(clientInfo.getProperties().getRemoteAddress());
									userSessions.setClient_info(clientInfo.getUserAgent());
									new MyBatisHelper().insert("insertUserSessions", userSessions);

									if (lastUserSession == null) {
										lastUserSession = new MyBatisHelper().selectOne("selectUserSessions", appUser.getId());
									}
									// set user session info into web session
									session.setLastUserSession(lastUserSession);
									session.setUsersessions_id(userSessions.getId());

									AuditLogsUtil.log(ActionRight.SIGN_IN.name(), session);

									Map<String, Serializable> params = new HashMap<String, Serializable>();
									params.put("is_default", true);

									Currencies currency = new Currencies();//new MyBatisHelper().selectOne("selectCurrencies", params);
									currency.setId(1);
									currency.setName("uzs");
									currency.setValue(1d);
									currency.setIs_default(true);
									currency.setStatus((byte) 1);
									
									session.setCurrency(currency);

									logger.info("User '" + model.getUsername() + "' successfully logged in. Session: " + session.getId()
											+ ", Currency: " + currency.getName());

									setResponsePage(new Home());
								} else {
									feedback.error(new StringResourceModel("signin.error.login.disabled", null).getString());
									captchaBody.replace(updateCaptcha());
								}
							} else {
								feedback.error(new StringResourceModel("signin.error.login", null).getString());
								// captcha_visible = true;
								captchaBody.replace(updateCaptcha());
							}
						}
					} catch (Exception e) {
						logger.error("Exception:", e);
						feedback.error(new StringResourceModel("signin.error.general", null).getString());
						captchaBody.replace(updateCaptcha());
					} finally {
						target.add(feedback);
						target.add(captchaBody);
						if (captcha_visible) {
							target.add(captchaInputWrapper);
							target.add(captchaBodyWrapper);
						}
					}
				}

				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
					super.onError(target, form);
					target.add(feedback);
				}
			});
			
			add(new Link<Void>("forgot_password") {

				private static final long serialVersionUID = 1L;

				@Override
				public void onClick() {
					setResponsePage(new ForgotPasswordPage());
				}
			});
		}

		@SuppressWarnings("unused")
		private class LoginModel implements Serializable {
			private static final long serialVersionUID = 1L;
			private String username;
			private String password;
			private String captcha;

			public String getUsername() {
				return username;
			}

			public void setUsername(String username) {
				this.username = username;
			}

			public String getPassword() {
				return password;
			}

			public void setPassword(String password) {
				this.password = password;
			}

			public void setCaptcha(String captcha) {
				this.captcha = captcha;
			}

			public String getCaptcha() {
				return captcha;
			}

		}

		public NonCachingImage updateCaptcha() {
			imagePass = randomString(5, 5);
			captchaImageResource = new CaptchaImageResource(imagePass, 50, 25);
			captchaImageResource.setFormat("png");
			NonCachingImage newimg = new NonCachingImage("captchaImage", captchaImageResource);
			newimg.setOutputMarkupId(true);
			return newimg;
		}
	}

	private static int randomInt(int min, int max) {
		return (int) (Math.random() * (max - min) + min);
	}

	private static String randomString(int min, int max) {
		int num = randomInt(min, max);
		byte b[] = new byte[num];
		for (int i = 0; i < num; i++)
			b[i] = (byte) randomInt('0', '9');
		return new String(b);
	}
}
