package uz.hbs.actions.admin.hotels.tabbed.panels;

import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import uz.hbs.beans.AdditionalBed;
import uz.hbs.components.ajax.AjaxOnBlurEvent;
import uz.hbs.utils.CommonUtil;

public class AdditionalBedSwitchPanel extends FormComponentPanel<AdditionalBed> {
	private static final long serialVersionUID = 1L;
	private boolean checked;
	private CheckBox field;

	public AdditionalBedSwitchPanel(String id, IModel<AdditionalBed> model) {
		super(id, model);
		add(field = new CheckBox("myonoffswitch", new PropertyModel<Boolean>(this, "checked")));
		field.add(new AjaxOnBlurEvent());
	}
	
	@Override
	protected void convertInput() {
		Boolean bool = field.getConvertedInput();
		if (bool != null)
			setConvertedInput(new AdditionalBed(bool));
		else
			setConvertedInput(new AdditionalBed(AdditionalBed.INAPPLICABLE));
	}
	
	public boolean isChecked() {
		return checked;
	}
	
	@Override
	public String getInput() {
		return field.getInput();
	}
	
	public void setChecked(boolean checked) {
		this.checked = checked;
		setDefaultModelObject(new AdditionalBed(checked));
	}
	
	@Override
	protected void onBeforeRender() {
		field.setRequired(isRequired());
		AdditionalBed bool = (AdditionalBed) getDefaultModelObject();
		if (bool != null)
			checked = CommonUtil.nvl(bool.isId());
		else
			checked = AdditionalBed.INAPPLICABLE;
		super.onBeforeRender();
	}
}
