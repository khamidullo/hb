package uz.hbs.actions.admin.touragents;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.apache.ibatis.session.SqlSession;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.beans.Accountant;
import uz.hbs.beans.Address;
import uz.hbs.beans.Bank;
import uz.hbs.beans.Contract;
import uz.hbs.beans.IdAndValue;
import uz.hbs.beans.LicenseAndCert;
import uz.hbs.beans.Manager;
import uz.hbs.beans.TourAgent;
import uz.hbs.beans.User;
import uz.hbs.components.panels.address.AddressPanel;
import uz.hbs.components.panels.contract.ContractPanel;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;
import uz.hbs.session.MySession;
import uz.hbs.utils.CommonUtil;
import uz.hbs.utils.MessagesUtil;
import uz.hbs.utils.RequiredFieldAjaxCallListener;

public class EditTourAgentsPanel extends MyBreadCrumbPanel {
	private static final long serialVersionUID = 1L;

	public EditTourAgentsPanel(String id, IBreadCrumbModel breadCrumbModel, TourAgent model) {
		super(id, breadCrumbModel);

		TourAgent tourAgents = new MyBatisHelper().selectOne("selectTourAgents", model);
		
		Contract contract = new MyBatisHelper().selectOne("selectContract", tourAgents.getUsers_id());
		if (contract != null) {
			tourAgents.setContract(contract);
		}
		
		Label panelTitle = new Label("panelTitle", new StringResourceModel("touragents.edit.panel.title", new Model<TourAgent>(model)));
		add(panelTitle);

		add(new MyForm("form", new CompoundPropertyModel<TourAgent>(tourAgents)));
	}

	private class MyForm extends Form<TourAgent> {
		private static final long serialVersionUID = 1L;

		public MyForm(String id, final IModel<TourAgent> model) {
			super(id, model);

			setMultiPart(true);

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

			add(new AddressPanel("addressPanel", model.getObject().getAddresses()));

			add(new ContractPanel("contractPanel", model.getObject().getContract()));

			RequiredTextField<String> display_name = new RequiredTextField<String>("display_name");
			display_name.setLabel(new StringResourceModel("touragents.company.tour_agency_display_name", null));
			add(display_name);

			RequiredTextField<String> primary_phone = new RequiredTextField<String>("primary_phone");
			primary_phone.setLabel(new StringResourceModel("touragents.company.primary_contact_number", null));
			add(primary_phone);

			EmailTextField email = new EmailTextField("email");
			email.setLabel(new StringResourceModel("touragents.company.email_address", null));
			email.setRequired(true);
			add(email);

			RequiredTextField<String> name = new RequiredTextField<String>("name");
			name.setLabel(new StringResourceModel("touragents.company.tour_agency_legal_name", null));
			add(name);

			RequiredTextField<String> secondary_phone = new RequiredTextField<String>("secondary_phone");
			secondary_phone.setLabel(new StringResourceModel("touragents.company.secondary_contact_number", null));
			add(secondary_phone);

			add(new CheckBox("corp").setEnabled(false));
			
			TextField<String> accountants_first_name = new TextField<String>("accountants.first_name");
			accountants_first_name.setLabel(new StringResourceModel("touragents.accountant_details.first_name", null));
			add(accountants_first_name);

			TextField<String> accountants_middle_name = new TextField<String>("accountants.middle_name");
			accountants_middle_name.setLabel(new StringResourceModel("touragents.accountant_details.middle_name", null));
			add(accountants_middle_name);

			TextField<String> accountants_last_name = new TextField<String>("accountants.last_name");
			accountants_last_name.setLabel(new StringResourceModel("touragents.accountant_details.last_name", null));
			add(accountants_last_name);

			TextField<String> accountants_contact_number = new TextField<String>("accountants.contact_number");
			accountants_contact_number.setLabel(new StringResourceModel("touragents.accountant_details.contact_number", null));
			add(accountants_contact_number);

			EmailTextField accountants_contact_email = new EmailTextField("accountants.contact_email");
			accountants_contact_email.setLabel(new StringResourceModel("touragents.accountant_details.contact_email", null));
			add(accountants_contact_email);

			RequiredTextField<String> banks_primary_name = new RequiredTextField<String>("banks_primary.name");
			banks_primary_name.setLabel(new StringResourceModel("touragents.bank.name", null));
			add(banks_primary_name);

			RequiredTextField<String> banks_primary_address = new RequiredTextField<String>("banks_primary.address");
			banks_primary_address.setLabel(new StringResourceModel("touragents.bank.address", null));
			add(banks_primary_address);

			RequiredTextField<String> banks_primary_account_number = new RequiredTextField<String>("banks_primary.account_number");
			banks_primary_account_number.setLabel(new StringResourceModel("touragents.bank.account_number", null));
			add(banks_primary_account_number);

			RequiredTextField<String> banks_primary_correspondent_account = new RequiredTextField<String>("banks_primary.correspondent_account");
			banks_primary_correspondent_account.setLabel(new StringResourceModel("touragents.bank.correspondent_account", null));
			add(banks_primary_correspondent_account);

			RequiredTextField<String> banks_primary_identification_code = new RequiredTextField<String>("banks_primary.identification_code");
			banks_primary_identification_code.setLabel(new StringResourceModel("touragents.bank.identification_code", null));
			add(banks_primary_identification_code);

			TextField<String> banks_secondary_name = new TextField<String>("banks_secondary.name");
			banks_secondary_name.setLabel(new StringResourceModel("touragents.bank.name", null));
			add(banks_secondary_name);

			TextField<String> banks_secondary_address = new TextField<String>("banks_secondary.address");
			banks_secondary_address.setLabel(new StringResourceModel("touragents.bank.address", null));
			add(banks_secondary_address);

			TextField<String> banks_secondary_account_number = new TextField<String>("banks_secondary.account_number");
			banks_secondary_account_number.setLabel(new StringResourceModel("touragents.bank.account_number", null));
			add(banks_secondary_account_number);

			TextField<String> banks_secondary_correspondent_account = new TextField<String>("banks_secondary.correspondent_account");
			banks_secondary_correspondent_account.setLabel(new StringResourceModel("touragents.bank.correspondent_account", null));
			add(banks_secondary_correspondent_account);

			TextField<String> banks_secondary_identification_code = new TextField<String>("banks_secondary.identification_code");
			banks_secondary_identification_code.setLabel(new StringResourceModel("touragents.bank.identification_code", null));
			add(banks_secondary_identification_code);

			final FileUploadField license = new FileUploadField("licenseFileInput");
			license.setLabel(new StringResourceModel("touragents.license_and_cert.license", null));
			add(license);

			TextField<String> license_id = new TextField<String>("license_id");
			license_id.setLabel(new StringResourceModel("touragents.license_and_cert.license_id", null));
			add(license_id);

			final FileUploadField cert = new FileUploadField("certFileInput");
			cert.setLabel(new StringResourceModel("touragents.license_and_cert.cert", null));
			add(cert);

			TextField<String> cert_id = new TextField<String>("cert_id");
			cert_id.setLabel(new StringResourceModel("touragents.license_and_cert.cert_id", null));
			add(cert_id);

			RequiredTextField<String> managers_first_name = new RequiredTextField<String>("managers.first_name");
			managers_first_name.setLabel(new StringResourceModel("touragents.accountant_details.first_name", null));
			add(managers_first_name);

			TextField<String> managers_middle_name = new TextField<String>("managers.middle_name");
			managers_middle_name.setLabel(new StringResourceModel("touragents.accountant_details.middle_name", null));
			add(managers_middle_name);

			RequiredTextField<String> managers_last_name = new RequiredTextField<String>("managers.last_name");
			managers_last_name.setLabel(new StringResourceModel("touragents.accountant_details.last_name", null));
			add(managers_last_name);

			RequiredTextField<String> managers_contact_number = new RequiredTextField<String>("managers.contact_number");
			managers_contact_number.setLabel(new StringResourceModel("touragents.accountant_details.contact_number", null));
			add(managers_contact_number);

			EmailTextField managers_contact_email = new EmailTextField("managers.contact_email");
			managers_contact_email.setLabel(new StringResourceModel("touragents.accountant_details.contact_email", null));
			managers_contact_email.setRequired(true);
			add(managers_contact_email);

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

					TourAgent tourAgents = (TourAgent) form.getDefaultModelObject();
					Manager manager = tourAgents.getManagers();
					
					StringTemplate st = templateGroup.getInstanceOf("uz/hbs/utils/email/templates/TAManagerAccountDetails");
					st.setAttribute("last_name", manager.getLast_name());
					st.setAttribute("first_name", manager.getFirst_name());
					st.setAttribute("middle_name", manager.getMiddle_name());
					st.setAttribute("tour_agency", tourAgents.getName());
					
					if (MessagesUtil.createHtmlMessage(manager.getContact_email(), "Account details", st.toString(), ((MySession)getSession()).getUser().getId(), true)) {
						feedback.success("Email is sent");
					} else {
						feedback.error("Error, Email is not sent");
					}
					
					new MyBatisHelper().insert("updateManagers", manager);
					logger.debug("Manager updated=" + manager);
					
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
					TourAgent model = (TourAgent) form.getDefaultModelObject();

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

						sql.insert("updateTourAgents", model);

						logger.debug("Step #2: TourAgent updated=" + model);

						if (model.getAccountants() != null) {
							Accountant accountants = model.getAccountants();
							sql.insert("updateAccountants", accountants);
							logger.debug("Step #3: Accountant updated=" + accountants);
						}

						Address address = model.getAddresses();
						address.setInitiator_user_id(((MySession) getSession()).getUser().getId());

						sql.update("updateAddresses", address);

						logger.debug("Step #4: Address updated=" + address);

						Contract contract = model.getContract();
						contract.setUsers_id(model.getUsers_id());
						
						if (sql.update("updateContract", contract) == 0){
							sql.update("insertContract", contract);
							logger.debug("Step #4.2: Contract inserted=" + contract);
						} else {
							logger.debug("Step #4.2: Contract updated=" + contract);
						}						
						
						if (model.getManagers() != null) {
							Manager managers = model.getManagers();
							sql.update("updateManagers", managers);
							logger.debug("Step #5: Manager updated=" + managers);
						}

						if (model.getBanks_primary() != null) {
							Bank bank = model.getBanks_primary();
							// bank.setUsers_id(user.getId());
							// bank.setType(new IdAndValue(Banks.TYPE_PRIMARY));
							sql.update("updateBanks", bank);
							logger.debug("Step #6: Primary bank update=" + bank);
						}

						if (model.getBanks_secondary() != null) {
							Bank bank = model.getBanks_secondary();
							// bank.setUsers_id(user.getId());
							// bank.setType(new IdAndValue(Banks.TYPE_SECONDARY));
							sql.update("updateBanks", bank);
							logger.debug("Step #7: Secondary bank update=" + bank);
						}

						if (license != null) {
							FileUpload fileUpload = license.getFileUpload();
							LicenseAndCert licenses = new LicenseAndCert();
							licenses.setUsers_id(user.getId());
							licenses.setDoc_number(model.getLicense_id());
							licenses.setType(new IdAndValue(LicenseAndCert.TYPE_LICENSE));
							if (fileUpload != null) {
								licenses.setFile_name(fileUpload.getClientFileName());
								licenses.setMime_type(fileUpload.getContentType());
								licenses.setContent(fileUpload.getBytes());
							}
							sql.update("updateLicensesandcerts", licenses);
							logger.debug("Step #8: License updated=" + licenses);
						}

						if (cert != null) {
							FileUpload fileUpload = cert.getFileUpload();
							LicenseAndCert certs = new LicenseAndCert();
							certs.setUsers_id(user.getId());
							certs.setDoc_number(model.getCert_id());
							certs.setType(new IdAndValue(LicenseAndCert.TYPE_CERT));
							if (fileUpload != null) {
								certs.setFile_name(fileUpload.getClientFileName());
								certs.setMime_type(fileUpload.getContentType());
								certs.setContent(fileUpload.getBytes());
							}
							sql.update("updateLicensesandcerts", certs);
							logger.debug("Step #9: Cert updated=" + certs);
						}

						sql.commit();

						String resultTxt = new StringResourceModel("touragents.edit.success", new Model<TourAgent>(model)).getString();

						logger.info(resultTxt);

						feedback.success(resultTxt);
					} catch (Exception e) {
						logger.error("Exception", e);
						feedback.error(new StringResourceModel("touragents.edit.error", new Model<TourAgent>(model)).getString());
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
		return new StringResourceModel("touragents.edit", null);
	}
}
