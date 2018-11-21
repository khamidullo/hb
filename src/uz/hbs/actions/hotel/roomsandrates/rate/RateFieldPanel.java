package uz.hbs.actions.hotel.roomsandrates.rate;

import java.math.BigDecimal;

import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import uz.hbs.beans.rate.RateDetails;

public class RateFieldPanel extends Panel {
	private static final long serialVersionUID = 1L;

	public RateFieldPanel(String id, IModel<RateDetails> model, boolean resident) {
		super(id, model);
		add(new RequiredTextField<BigDecimal>("rate", new PropertyModel<BigDecimal>(model, "rate")).setLabel(Model.of("Rate")));
		add(new RequiredTextField<BigDecimal>("rate_uz", new PropertyModel<BigDecimal>(model, "rate_uz")).setLabel(Model.of("Rate UZ")).setEnabled(resident));
	}
}
