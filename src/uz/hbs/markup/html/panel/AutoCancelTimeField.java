package uz.hbs.markup.html.panel;

import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import uz.hbs.beans.ReservationDetail;

public class AutoCancelTimeField extends Panel {
	private static final long serialVersionUID = 1L;

	public AutoCancelTimeField(String id, IModel<ReservationDetail> model) {
		super(id, model);
		add(new HiddenField<String>("auto_cancel_time"));
	}
}
