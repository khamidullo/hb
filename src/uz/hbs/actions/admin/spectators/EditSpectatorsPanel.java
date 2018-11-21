package uz.hbs.actions.admin.spectators;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.apache.ibatis.session.SqlSession;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.beans.IdAndValue;
import uz.hbs.beans.Spectator;
import uz.hbs.beans.User;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;
import uz.hbs.session.MySession;
import uz.hbs.utils.CommonUtil;
import uz.hbs.utils.MessagesUtil;
import uz.hbs.utils.RequiredFieldAjaxCallListener;

public class EditSpectatorsPanel extends MyBreadCrumbPanel {
	private static final long serialVersionUID = 1L;

	public EditSpectatorsPanel(String id, IBreadCrumbModel breadCrumbModel, Spectator model) {
		super(id, breadCrumbModel);
		
		Spectator spectators = new MyBatisHelper().selectOne("selectSpectators", model);
		
		Label panelTitle = new Label("panelTitle", new StringResourceModel("spectators.edit.panel.title", new Model<Spectator>(model)));
		add(panelTitle);

		add(new MyForm("form", new CompoundPropertyModel<Spectator>(spectators)));
	}

	private class MyForm extends Form<Spectator> {
		private static final long serialVersionUID = 1L;

		public MyForm(String id, IModel<Spectator> model) {
			super(id, model);

			final MyFeedbackPanel feedback = new MyFeedbackPanel("feedback");
			feedback.setOutputMarkupId(true);
			add(feedback);

			RadioGroup<Byte> statusRadio = new RadioGroup<Byte>("status");
			statusRadio.setLabel(new StringResourceModel("touragents.status", null));
			statusRadio.setRequired(true);
			add(statusRadio);

			Radio<Byte> status_active = new Radio<Byte>("status_active", new Model<Byte>(User.STATUS_ACTIVE));
			Radio<Byte> status_disabled = new Radio<Byte>("status_disabled", new Model<Byte>(User.STATUS_NEW));
			statusRadio.add(status_active);
			statusRadio.add(status_disabled);

			TextField<String> name = new TextField<String>("name");
			name.setLabel(new StringResourceModel("spectators.agency.agency_name", null));
			add(name);

			TextField<String> primary_phone = new TextField<String>("primary_phone");
			primary_phone.setLabel(new StringResourceModel("touragents.company.primary_contact_number", null));
			add(primary_phone);

			EmailTextField email = new EmailTextField("email");
			email.setLabel(new StringResourceModel("touragents.company.email_address", null));
			add(email);

			TextField<String> first_name = new TextField<String>("first_name");
			first_name.setLabel(new StringResourceModel("touragents.accountant_details.first_name", null));
			add(first_name);

			TextField<String> middle_name = new TextField<String>("middle_name");
			middle_name.setLabel(new StringResourceModel("touragents.accountant_details.middle_name", null));
			add(middle_name);

			TextField<String> last_name = new TextField<String>("last_name");
			last_name.setLabel(new StringResourceModel("touragents.accountant_details.last_name", null));
			add(last_name);

			TextField<String> contact_number = new TextField<String>("contact_number");
			contact_number.setLabel(new StringResourceModel("touragents.accountant_details.contact_number", null));
			add(contact_number);

			EmailTextField contact_email = new EmailTextField("contact_email");
			contact_email.setLabel(new StringResourceModel("touragents.accountant_details.contact_email", null));
			add(contact_email);

			IndicatingAjaxButton resend = new IndicatingAjaxButton("resend") {
				private static final long serialVersionUID = 1L;
				
				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
					super.onError(target, form);
					target.add(feedback);
				}
				
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					super.onSubmit(target, form);
					StringTemplateGroup templateGroup = new StringTemplateGroup("mailgroup");
					templateGroup.setFileCharEncoding("UTF-8");

					Spectator spectator = (Spectator) form.getDefaultModelObject();
					
					StringTemplate st = templateGroup.getInstanceOf("uz/hbs/utils/email/templates/SpectatorAccountDetails");
					st.setAttribute("last_name", spectator.getLast_name());
					st.setAttribute("first_name", spectator.getFirst_name());
					st.setAttribute("middle_name", spectator.getMiddle_name());
					st.setAttribute("tour_agency", spectator.getName());
					
					if (MessagesUtil.createHtmlMessage(spectator.getEmail(), "Account details", st.toString(), ((MySession)getSession()).getUser().getId(), true)) {
						feedback.success("Email was sent");
					} else {
						feedback.error("Error, Email was not sent");
					}
					
					new MyBatisHelper().insert("updateSpectator", spectator);
					logger.debug("Spectator updated=" + spectator);
					
					target.add(feedback);
				}
			};
			add(resend);
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
						user.setId(model.getUsers_id());
						user.setName(model.getName());
						user.setEmail(model.getEmail());
						user.setStatus(new IdAndValue(model.getStatus().intValue()));
						user.setInitiator_user_id(((MySession) getSession()).getUser().getId());

						sql.update("updateUser", user);

						logger.debug("Step #1: User updated=" + user);
						
						sql.insert("updateSpectator", model);

						logger.debug("Step #2: Spectator updated=" + model);
						
						sql.commit();

						String resultTxt = new StringResourceModel("spectators.edit.success", new Model<Spectator>(model)).getString();

						logger.info(resultTxt);

						feedback.success(resultTxt);
					} catch (Exception e) {
						logger.error("Exception", e);
						feedback.error(new StringResourceModel("spectators.edit.error", new Model<Spectator>(model)).getString());
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
	public Class<?> implementedClass() {
		return this.getClass();
	}

	@Override
	public IModel<String> getTitle() {
		return new StringResourceModel("spectators.edit", null);
	}
}
