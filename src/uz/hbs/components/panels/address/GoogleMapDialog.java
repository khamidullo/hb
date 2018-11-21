package uz.hbs.components.panels.address;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.beans.Address;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;
import uz.hbs.utils.HeaderItemUtil;

public abstract class GoogleMapDialog extends WebPage {
	private static final long serialVersionUID = 1L;
	private MyFeedbackPanel feedback;
	private Address address;

	public GoogleMapDialog(Address address) {
		this.address = address;
		add(feedback = new MyFeedbackPanel("feedback"));
		feedback.setOutputMarkupId(true);
		
		Form<Address> form = new Form<Address>("form", new CompoundPropertyModel<Address>(new Address(address.getLatitude(), address.getLongitude())));
		
		NumberTextField<Double> latitude;
		form.add(latitude = new NumberTextField<Double>("latitude"));
		latitude.setLabel(new StringResourceModel("latitude", null));
		latitude.setMarkupId("latitude");

		NumberTextField<Double> longitude;
		form.add(longitude = new NumberTextField<Double>("longitude"));
		longitude.setLabel(new StringResourceModel("longitude", null));
		longitude.setMarkupId("longitude");
		
		form.add(new AjaxButton("indicate") {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onIndicate(target, form);
			}
			
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				super.onError(target, form);
				target.add(feedback);
			}
		});
		add(form);
	}

	protected abstract void onIndicate(AjaxRequestTarget target, Form<?> form); 
	
	
	@Override
	public void renderHead(IHeaderResponse response) {
		if (address.getLatitude() != null && address.getLongitude() != null) {
			HeaderItemUtil.setGoogleMapHeaderItem(response, "map-canvas", false, address.getLatitude(), address.getLongitude());
		} else HeaderItemUtil.setGoogleMapHeaderItem(response, false);
		super.renderHead(response);
	}
}
