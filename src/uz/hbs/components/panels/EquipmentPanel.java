package uz.hbs.components.panels;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;

import uz.hbs.markup.html.bootstrapmodal.BootstrapModalPanel;

public class EquipmentPanel extends BootstrapModalPanel {
	private static final long serialVersionUID = 1L;

	public EquipmentPanel(String id, String header) {
		super(id, header);
	}

	@Override
	public Panel getContent(String id) {
		return null;
	}

	@Override
	public void onCancel(AjaxRequestTarget target) {

	}

	@Override
	public void onSubmit(AjaxRequestTarget target) {

	}
}
