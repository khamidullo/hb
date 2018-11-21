package uz.hbs.actions.admin.spectators;

import java.security.SecureRandom;

import org.apache.ibatis.session.SqlSession;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.beans.Account;
import uz.hbs.beans.IdAndValue;
import uz.hbs.beans.Spectator;
import uz.hbs.beans.User;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;
import uz.hbs.session.MySession;
import uz.hbs.utils.CommonUtil;
import uz.hbs.utils.DigestUtil;
import uz.hbs.utils.RequiredFieldAjaxCallListener;

public class AddSpectatorsPanel extends MyBreadCrumbPanel {
	private static final long serialVersionUID = 1L;

	public AddSpectatorsPanel(String id, IBreadCrumbModel breadCrumbModel) {
		super(id, breadCrumbModel);
		add(new MyForm("form", new CompoundPropertyModel<Spectator>(new Spectator())));
	}

	private class MyForm extends Form<Spectator> {
		private static final long serialVersionUID = 1L;

		public MyForm(String id, IModel<Spectator> model) {
			super(id, model);

			final MyFeedbackPanel feedback = new MyFeedbackPanel("feedback");
			feedback.setOutputMarkupId(true);
			add(feedback);

			RequiredTextField<String> name = new RequiredTextField<String>("name");
			name.setLabel(new StringResourceModel("spectators.agency.agency_name", null));
			add(name);

			RequiredTextField<String> primary_phone = new RequiredTextField<String>("primary_phone");
			primary_phone.setLabel(new StringResourceModel("touragents.company.primary_contact_number", null));
			add(primary_phone);

			EmailTextField email = new EmailTextField("email");
			email.setLabel(new StringResourceModel("touragents.company.email_address", null));
			email.setRequired(true);
			add(email);

			RequiredTextField<String> first_name = new RequiredTextField<String>("first_name");
			first_name.setLabel(new StringResourceModel("touragents.accountant_details.first_name", null));
			add(first_name);

			TextField<String> middle_name = new TextField<String>("middle_name");
			middle_name.setLabel(new StringResourceModel("touragents.accountant_details.middle_name", null));
			add(middle_name);

			RequiredTextField<String> last_name = new RequiredTextField<String>("last_name");
			last_name.setLabel(new StringResourceModel("touragents.accountant_details.last_name", null));
			add(last_name);

			TextField<String> contact_number = new TextField<String>("contact_number");
			contact_number.setLabel(new StringResourceModel("touragents.accountant_details.contact_number", null));
			add(contact_number);

			EmailTextField contact_email = new EmailTextField("contact_email");
			contact_email.setLabel(new StringResourceModel("touragents.accountant_details.contact_email", null));
			add(contact_email);

			IndicatingAjaxButton submit = new IndicatingAjaxButton("submit") {
				private static final long serialVersionUID = 1L;

				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
					super.onError(target, form);
					target.add(feedback);
				}

				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					super.onSubmit(target, form);
					Spectator model = (Spectator) form.getDefaultModelObject();

					SqlSession sql = MyBatisHelper.getSqlSessionFactory().openSession(false);
					try {
						
						User user = new User();
						user.setPassword(DigestUtil.getDigest(String.valueOf(new SecureRandom().nextLong())));
						user.setName(model.getName());
						user.setEmail(model.getEmail());
						user.setType(new IdAndValue((int) User.TYPE_SPECTATOR_USER));
						user.setStatus(new IdAndValue((int) User.STATUS_ACTIVE));
						user.setInitiator_user_id(((MySession)getSession()).getUser().getId());
						
						CommonUtil.generateLogin(user, sql);
						
						sql.insert("insertUser", user);

						logger.debug("Step #1: User created=" + user);
						
						Account acc = CommonUtil.insertUserAccount(sql, user);
						
						logger.debug("Step #2: User Account created, Id=" + acc.getId());
						
						model.setUsers_id(user.getId());
						sql.insert("insertSpectator", model);

						logger.debug("Step #3: Spectator created=" + model);
						
						sql.commit();

						String resultTxt = new StringResourceModel("spectators.add.success", new Model<Spectator>(model)).getString();

						logger.info(resultTxt);

						feedback.success(resultTxt);
					} catch (Exception e) {
						logger.error("Exception", e);
						feedback.error(new StringResourceModel("spectators.add.error", null).getString());
						sql.rollback();
					} finally {
						sql.close();
					}
					target.add(feedback);
				}
				
				@Override
				protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
					super.updateAjaxAttributes(attributes);
					attributes.getAjaxCallListeners().add(new RequiredFieldAjaxCallListener(MyForm.this.getMarkupId(), ((MySession) getSession()).getLocale().getLanguage()));
				}
			};
			add(submit);
			CommonUtil.setFormComponentRequired(this);
		}
	}

	@Override
	public IModel<String> getTitle() {
		return new StringResourceModel("spectators.add", null);
	}

	@Override
	public Class<?> implementedClass() {
		return this.getClass();
	}
}
