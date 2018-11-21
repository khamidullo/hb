package uz.hbs.components.panels.contract;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import uz.hbs.MyWebApplication;
import uz.hbs.beans.Contract;
import uz.hbs.utils.DateUtil;

public class ContractViewPanel extends Panel {
	private static final long serialVersionUID = 1L;

	public ContractViewPanel(String id, Contract contract) {
		super(id, new CompoundPropertyModel<Contract>(contract));
		
		Label contract_number = new Label("contract_number", contract != null ? contract.getContract_number() : "");
		add(contract_number);
		
		Label contract_date = new Label("contract_date", contract != null ? DateUtil.toString(contract.getContract_date(), MyWebApplication.DATE_FORMAT) : "");
		add(contract_date);
	}
}
