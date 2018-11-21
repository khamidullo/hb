package uz.hbs.components.panels.contract;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.MyWebApplication;
import uz.hbs.beans.Contract;
import uz.hbs.beans.IdAndValue;
import uz.hbs.markup.html.form.textfield.MyDatePicker;

public class ContractPanel extends Panel {
	private static final long serialVersionUID = 1L;

	public ContractPanel(String id, Contract contract) {
		super(id, new CompoundPropertyModel<Contract>(contract));
		
		LoadableDetachableModel<List<IdAndValue>> contractTypeListModel = new LoadableDetachableModel<List<IdAndValue>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<IdAndValue> load() {
				List<IdAndValue> list= new ArrayList<>();
				list.add(new IdAndValue(1, new StringResourceModel("touragents.contract.type.1", null).getString()));
				list.add(new IdAndValue(2, new StringResourceModel("touragents.contract.type.2", null).getString()));
				
				return list;
			}
		};
		
		DropDownChoice<IdAndValue> choice = new DropDownChoice<>("contract_type", contractTypeListModel, new ChoiceRenderer<>("value", "id"));
		choice.setRequired(true);
		choice.setLabel(new StringResourceModel("touragents.contract.type", null));
		add(choice);
		
		NumberTextField<Double> commission = new NumberTextField<>("commission_value");
		commission.setRequired(true);
		commission.setLabel(new StringResourceModel("touragents.contract.commission", null));
		add(commission);
		
		TextField<String> contract_number = new TextField<>("contract_number");
		contract_number.setLabel(new StringResourceModel("touragents.contract.number", null));
		contract_number.setRequired(true);
		add(contract_number);
		
		DateTextField contract_date = new DateTextField("contract_date", new PatternDateConverter(MyWebApplication.DATE_FORMAT, false));
		contract_date.add(new MyDatePicker());
		contract_date.setLabel(new StringResourceModel("touragents.contract.date", null));
		contract_date.setRequired(true);
		add(contract_date);
	}
}
