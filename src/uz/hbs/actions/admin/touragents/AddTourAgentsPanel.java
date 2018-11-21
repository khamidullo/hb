package uz.hbs.actions.admin.touragents;

import java.security.SecureRandom;

import org.apache.ibatis.session.SqlSession;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.beans.Account;
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
import uz.hbs.utils.DigestUtil;
import uz.hbs.utils.RequiredFieldAjaxCallListener;

public class AddTourAgentsPanel extends MyBreadCrumbPanel {
	private static final long serialVersionUID = 1L;

	public AddTourAgentsPanel(String id, IBreadCrumbModel breadCrumbModel) {
		super(id, breadCrumbModel);
		add(new MyForm("form", new CompoundPropertyModel<TourAgent>(new TourAgent())));
	}

	private class MyForm extends Form<TourAgent> {
		private static final long serialVersionUID = 1L;

		public MyForm(String id, IModel<TourAgent> model) {
			super(id, model);

			setMultiPart(true);

			final MyFeedbackPanel feedback = new MyFeedbackPanel("feedback");
			feedback.setOutputMarkupId(true);
			add(feedback);

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
			
			add(new CheckBox("corp"));

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
						user.setPassword(DigestUtil.getDigest(String.valueOf(new SecureRandom().nextLong())));
						user.setName(model.getName());
						user.setEmail(model.getEmail());
						user.setType(new IdAndValue((int) User.TYPE_TOURAGENCY));
						user.setCorpUser(model.isCorp());
						
						user.setStatus(new IdAndValue((int) User.STATUS_ACTIVE));
						user.setInitiator_user_id(((MySession)getSession()).getUser().getId());
						
						CommonUtil.generateLogin(user, sql);
						
						sql.insert("insertUser", user);

						logger.debug("Step #1: User created=" + user);
						
						Account acc = CommonUtil.insertUserAccount(sql, user);
						
						logger.debug("Step #2: User Account created, Id=" + acc.getId());
						
						Address address = model.getAddresses();
						address.setInitiator_user_id(((MySession)getSession()).getUser().getId());

						sql.insert("insertAddresses", address);
						
						logger.debug("Step #3: Address created=" + address);

						Contract contract = model.getContract();
						contract.setUsers_id(user.getId());
						
						sql.insert("insertContract", contract);
						
						logger.debug("Step #3.2: Contract created=" + contract);

						TourAgent tourAgent = model;
						tourAgent.setUsers_id(user.getId());
						tourAgent.setAddresses_id(address.getId());
						tourAgent.setCreator_user_id(((MySession)getSession()).getUser().getId());

						sql.insert("insertTourAgents", tourAgent);

						logger.debug("Step #4: TourAgent created=" + tourAgent);

						
						if (model.getAccountants() != null) {
							Accountant accountants = model.getAccountants();
							accountants.setUsers_id(user.getId());
							sql.insert("insertAccountants", accountants);
							logger.debug("Step #5: Accountant created=" + accountants);
						}

						if (model.getManagers() != null) {
							Manager managers = model.getManagers();
							managers.setUsers_id(user.getId());
							sql.insert("insertManagers", managers);
							logger.debug("Step #6: Manager created=" + managers);
							
							/*Users managerUser = new Users();
							managerUser.setLogin(managers.getContact_email());
							managerUser.setName(managers.getFirst_name() + " " + managers.getLast_name());
							managerUser.setEmail(managers.getContact_email());
							managerUser.setStatus(new IdAndValue((int) Users.STATUS_ACTIVE));
							managerUser.setType(new IdAndValue((int) Users.TYPE_TOURAGENT));
							managerUser.setInitiator_user_id(((MySession) getSession()).getUser().getId());
							
							sql.insert("insertUser", managerUser);
							
							logger.debug("Step #6.1: Manager user created=" + managerUser);
							
							Account acc2 = CommonUtil.insertUserAccount(sql, managerUser);
						
							logger.debug("Step #6.2: Manager User Account created, Id=" + acc2.getId());
							
							StringTemplateGroup templateGroup = new StringTemplateGroup("mailgroup");
							templateGroup.setFileCharEncoding("UTF-8");

							StringTemplate st = templateGroup.getInstanceOf("uz/hbs/utils/email/templates/TAManagerAccountDetails");
							st.setAttribute("last_name", managers.getLast_name());
							st.setAttribute("first_name", managers.getFirst_name());
							st.setAttribute("middle_name", managers.getMiddle_name());
							//st.setAttribute("tour_agency", tourAgents.getName());
							
							if (MessagesUtil.createHtmlMessage(managers.getContact_email(), "Account details", st.toString(), ((MySession)getSession()).getUser().getId(), true)) {
								feedback.success("Email is sent");
							} else {
								feedback.error("Error, Email was not sent");
							}*/
						}
						
						if (model.getBanks_primary() != null) {
							Bank bank = model.getBanks_primary();
							bank.setUsers_id(user.getId());
							bank.setType(new IdAndValue(Bank.TYPE_PRIMARY));
							sql.insert("insertBanks", bank);
							logger.debug("Step #7: Primary bank created=" + bank);
						}
						
						if (model.getBanks_primary() != null) {
							Bank bank = model.getBanks_secondary();
							bank.setUsers_id(user.getId());
							bank.setType(new IdAndValue(Bank.TYPE_SECONDARY));
							sql.insert("insertBanks", bank);
							logger.debug("Step #8: Secondary bank created=" + bank);
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
							sql.insert("insertLicensesandcerts", licenses);
							logger.debug("Step #9: License created=" + licenses);
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
							sql.insert("insertLicensesandcerts", certs);
							logger.debug("Step #10: Cert created=" + certs);
						}
						
						sql.commit();

						String resultTxt = new StringResourceModel("touragents.add.success", new Model<TourAgent>(model)).getString();
						
						logger.info(resultTxt);
						
						feedback.success(resultTxt);
						form.setEnabled(false);
						target.add(form);
					} catch (Exception e) {
						logger.error("Exception", e);
						feedback.error(new StringResourceModel("touragents.add.error", null).getString());
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
		return new StringResourceModel("touragents.add", null);
	}

	@Override
	public Class<?> implementedClass() {
		return this.getClass();
	}
}
