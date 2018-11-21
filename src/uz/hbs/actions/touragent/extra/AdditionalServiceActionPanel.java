package uz.hbs.actions.touragent.extra;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;

import uz.hbs.utils.models.MyAjaxCallListener;

public abstract class AdditionalServiceActionPanel<T> extends Panel {
	private static final long serialVersionUID = 1L;

	public AdditionalServiceActionPanel(String id) {
		super(id);
		add(new Link<Void>("insurance"){
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				onInsurance();
			}
		});
		add(new Link<Void>("taxi_order"){
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onClick() {
				onTaxiOrder();
			}
		});
		add(new Link<Void>("edit"){
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				onEdit();
			}
			
			@Override
			public boolean isVisible() {
				return isEditVisible();
			}
		});
		add(new Link<Void>("view"){
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onClick() {
				onView();
			}
			
		});
		add(new AjaxLink<Void>("cancel"){
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onClick(AjaxRequestTarget target) {
				onCancel(target);
			}
			
			@Override
			public boolean isVisible() {
				return isCancelVisible();
			}
			
			
			@Override
			protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
				super.updateAjaxAttributes(attributes);
				attributes.getAjaxCallListeners().add(new MyAjaxCallListener(true));
			}
		});
	}
	protected abstract void onInsurance(); 
	protected abstract void onTaxiOrder(); 
	protected abstract void onEdit(); 
	protected abstract boolean isEditVisible();
	protected abstract void onView(); 
	protected abstract void onCancel(AjaxRequestTarget target);
	protected abstract boolean isCancelVisible();
}
