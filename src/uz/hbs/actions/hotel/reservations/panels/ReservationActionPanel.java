package uz.hbs.actions.hotel.reservations.panels;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import uz.hbs.utils.models.MyAjaxCallListener;

public abstract class ReservationActionPanel<T> extends Panel {
	private static final long serialVersionUID = 1L;

	public ReservationActionPanel(String id, IModel<T> model) {
		super(id, model);
		
		add(new Link<Void>("view_details"){
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				onViewDetails();
			}
			
			@Override
			public boolean isVisible() {
				return isVisibleViewDetails();
			}
		});
		add(new Link<Void>("change_room"){
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onClick() {
				onChangeRoom();
			}
			
			@Override
			public boolean isVisible() {
				return isVisibleChangeRoom();
			}
		});
		add(new Link<Void>("issue_bill"){
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onClick() {
				onIssueBill();
			}
			
			@Override
			public boolean isVisible() {
				return isVisibleIssueBill();
			}
		});
		add(new Link<Void>("register_guest"){
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onClick() {
				onRegisterGuest();
			}
			
			@Override
			public boolean isVisible() {
				return isVisibleRegisterGuest();
			}
		});
		add(new Link<Void>("view_log"){
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onClick() {
				onViewReserveLogs();
			}
			
			@Override
			public boolean isVisible() {
				return isVisibleViewReserveLogs();
			}
		});
		add(new AjaxLink<Void>("cancel"){
			private static final long serialVersionUID = 1L;
			
			@Override
			public boolean isVisible() {
				return isVisibleCancel();
			}

			@Override
			public void onClick(AjaxRequestTarget target) {
				onCancel(target);
			}
			
			@Override
			protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
				super.updateAjaxAttributes(attributes);
				attributes.getAjaxCallListeners().add(new MyAjaxCallListener(true));
			}
		});
		add(new AjaxLink<Void>("no_show"){
			private static final long serialVersionUID = 1L;
			
			@Override
			public boolean isVisible() {
				return isVisibleNoShow();
			}
			
			@Override
			public void onClick(AjaxRequestTarget target) {
				onNoShow(target);
			}
		});
	}
	
	protected abstract void onViewDetails(); 
	protected abstract void onViewReserveLogs(); 
	protected abstract void onChangeRoom(); 
	protected abstract void onIssueBill(); 
	protected abstract void onRegisterGuest();
	protected abstract void onCancel(AjaxRequestTarget target);
	protected abstract void onNoShow(AjaxRequestTarget target);
	
	protected abstract boolean isVisibleViewDetails(); 
	protected abstract boolean isVisibleChangeRoom(); 
	protected abstract boolean isVisibleIssueBill(); 
	protected abstract boolean isVisibleRegisterGuest(); 
	protected abstract boolean isVisibleCancel(); 
	protected abstract boolean isVisibleNoShow(); 
	protected abstract boolean isVisibleViewReserveLogs();
}
