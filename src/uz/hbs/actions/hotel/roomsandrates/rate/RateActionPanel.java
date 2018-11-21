package uz.hbs.actions.hotel.roomsandrates.rate;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public abstract class RateActionPanel<T> extends Panel {
	private static final long serialVersionUID = 1L;

	public RateActionPanel(String id, final IModel<T> model) {
		super(id, model);

		add(addEditLink(model));
		add(addViewLink(model));
		add(addDeleteLink(model));
		add(addSaleLink(model));
	}

	public abstract Link<T> addEditLink(final IModel<T> model);

	public abstract Link<T> addViewLink(final IModel<T> model);

	public abstract Link<T> addDeleteLink(final IModel<T> model);

	public abstract Link<T> addSaleLink(final IModel<T> model);


	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
