package uz.hbs.actions.admin.touragents;

import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.beans.Contract;
import uz.hbs.beans.TourAgent;
import uz.hbs.beans.User;
import uz.hbs.components.panels.address.ViewAddressPanel;
import uz.hbs.components.panels.contract.ContractViewPanel;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;

public class ViewTourAgentsPanel extends MyBreadCrumbPanel {
	private static final long serialVersionUID = 1L;

	public ViewTourAgentsPanel(String id, IBreadCrumbModel breadCrumbModel, TourAgent model) {
		super(id, breadCrumbModel);

		TourAgent tourAgents = new MyBatisHelper().selectOne("selectTourAgents", model);

		Contract contract = new MyBatisHelper().selectOne("selectContract", tourAgents.getUsers_id());
		if (contract != null) {
			tourAgents.setContract(contract);
		}
		
		Label panelTitle = new Label("panelTitle", new StringResourceModel("touragents.view.panel.title", new Model<TourAgent>(model)));
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

			add(new Label("status", model.getObject().getStatus() == User.STATUS_ACTIVE ? new StringResourceModel("users.status1", null) : new StringResourceModel("users.status2", null)));
			
			add(new ViewAddressPanel("addressPanel", model.getObject().getAddresses(), false));

			add(new ContractViewPanel("contractPanel", model.getObject().getContract()));

			Label display_name = new Label("display_name");
			add(display_name);

			Label primary_phone = new Label("primary_phone");
			add(primary_phone);

			Label email = new Label("email");
			add(email);

			Label name = new Label("name");
			add(name);

			Label secondary_phone = new Label("secondary_phone");
			add(secondary_phone);

			add(new CheckBox("corp").setEnabled(false));
			
			Label accountants_first_name = new Label("accountants.first_name");
			add(accountants_first_name);

			Label accountants_middle_name = new Label("accountants.middle_name");
			add(accountants_middle_name);

			Label accountants_last_name = new Label("accountants.last_name");
			add(accountants_last_name);

			Label accountants_contact_number = new Label("accountants.contact_number");
			add(accountants_contact_number);

			Label accountants_contact_email = new Label("accountants.contact_email");
			add(accountants_contact_email);

			Label banks_primary_name = new Label("banks_primary.name");
			add(banks_primary_name);

			Label banks_primary_address = new Label("banks_primary.address");
			add(banks_primary_address);

			Label banks_primary_account_number = new Label("banks_primary.account_number");
			add(banks_primary_account_number);

			Label banks_primary_correspondent_account = new Label("banks_primary.correspondent_account");
			add(banks_primary_correspondent_account);

			Label banks_primary_identification_code = new Label("banks_primary.identification_code");
			add(banks_primary_identification_code);

			Label banks_secondary_name = new Label("banks_secondary.name");
			add(banks_secondary_name);

			Label banks_secondary_address = new Label("banks_secondary.address");
			add(banks_secondary_address);

			Label banks_secondary_account_number = new Label("banks_secondary.account_number");
			add(banks_secondary_account_number);

			Label banks_secondary_correspondent_account = new Label("banks_secondary.correspondent_account");
			add(banks_secondary_correspondent_account);

			Label banks_secondary_identification_code = new Label("banks_secondary.identification_code");
			add(banks_secondary_identification_code);

			Label license_id = new Label("license_id");
			add(license_id);

			Label cert_id = new Label("cert_id");
			add(cert_id);

			Label managers_first_name = new Label("managers.first_name");
			add(managers_first_name);

			Label managers_middle_name = new Label("managers.middle_name");
			add(managers_middle_name);

			Label managers_last_name = new Label("managers.last_name");
			add(managers_last_name);

			Label managers_contact_number = new Label("managers.contact_number");
			add(managers_contact_number);

			Label managers_contact_email = new Label("managers.contact_email");
			add(managers_contact_email);
		}
	}

	@Override
	public Class<?> implementedClass() {
		return this.getClass();
	}

	@Override
	public IModel<String> getTitle() {
		return new StringResourceModel("touragents.view", null);
	}
}
