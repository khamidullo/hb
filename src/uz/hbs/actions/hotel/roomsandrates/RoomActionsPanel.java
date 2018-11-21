package uz.hbs.actions.hotel.roomsandrates;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import uz.hbs.beans.Room;

public abstract class RoomActionsPanel extends Panel {
	private static final long serialVersionUID = 1L;

	public RoomActionsPanel(String id, IModel<Room> model) {
		super(id, model);
		add(new AjaxLink<Void>("cleanLink") {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible() {
				return isCleanLinkVisible();
			}

			@Override
			public void onClick(AjaxRequestTarget target) {
				onCleanLinkClick(target);
			}
		});
		add(new AjaxLink<Void>("onSaleLink") {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onClick(AjaxRequestTarget target) {
				onOnSaleLinkClick(target);
			}
			
			@Override
			public boolean isVisible() {
				return isOnSaleLinkVisible();
			}
		});
	}
	
	protected abstract void onCleanLinkClick(AjaxRequestTarget target);
	protected abstract boolean isCleanLinkVisible();
	protected abstract void onOnSaleLinkClick(AjaxRequestTarget target);
	protected abstract boolean isOnSaleLinkVisible();
}
