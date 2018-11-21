package uz.hbs.components.panels.hotel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.string.Strings;

public abstract class OnSaleStatePanel extends Panel {
	private static final long serialVersionUID = 1L;
	
	public OnSaleStatePanel(String id) {
		this(id, new Model<Boolean>(false));
	}
	
	public OnSaleStatePanel(String id, IModel<Boolean> model) {
		super(id, model);
		add(new AjaxCheckBox("state", model){
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				onChangeState(target, Strings.isTrue(getValue()));
			}
		});
		
	}
	protected abstract void onChangeState(AjaxRequestTarget target, boolean value);
}
