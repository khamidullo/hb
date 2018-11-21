package uz.hbs.actions.admin.hotels.b2c;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.validation.validator.RangeValidator;

import uz.hbs.beans.Hotel;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;

public class EditHotelB2CPanel extends MyBreadCrumbPanel{
	private static final long serialVersionUID = 1L;
	private Hotel hotel;
	public EditHotelB2CPanel(String id, IBreadCrumbModel breadCrumbModel, Hotel hotel) {
		super(id, breadCrumbModel);
		this.hotel = hotel;
		
		add(new EditForm("form", new CompoundPropertyModel<>(hotel)));
	}
	
	private class EditForm extends Form<Hotel> {
		private static final long serialVersionUID = 1L;

		public EditForm(String id, IModel<Hotel> model) {
			super(id, model);
			
			final MyFeedbackPanel feedback = new MyFeedbackPanel("feedback");
			feedback.setOutputMarkupId(true);
			feedback.setEscapeModelStrings(false);
			add(feedback);
			
			add(new Label("title", new StringResourceModel("hotels.b2c.edit.title", new Model<Hotel>(hotel))));
			RequiredTextField<Double> individualPrice = new RequiredTextField<Double>("b2c_individual_price");
			individualPrice.add(new RangeValidator<Double>((double)0, (double)100));
			individualPrice.setLabel(new StringResourceModel("hotels.reservation.individual_reservation", null));
			add(individualPrice);
			
			RequiredTextField<Double> groupPrice = new RequiredTextField<Double>("b2c_group_price");
			groupPrice.add(new RangeValidator<Double>((double)0, (double)100));
			groupPrice.setLabel(new StringResourceModel("hotels.reservation.group_reservation", null));
			add(groupPrice);
			
			add(new IndicatingAjaxButton("submit", this) {
				private static final long serialVersionUID = 1L;
				
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					super.onSubmit(target, form);
					
					try {
						Hotel hotelModel = (Hotel) form.getModelObject();
						hotelModel.setUsers_id(hotel.getUsers_id());
						hotelModel.setB2c_is_enabled(null);//edit vaqtida status o'zgarmasligi kerak
						if (new MyBatisHelper().update("updateHotelsB2CPrices", hotelModel) == 0) {
							hotelModel.setB2c_is_enabled(false);
							new MyBatisHelper().update("insertHotelsB2CPrices", hotelModel);
							logger.debug("B2C price inserted: Id=" + hotel.getUsers_id() + ", IndividualPrice=" + hotel.getB2c_individual_price()
									+ ", GroupPrice=" + hotel.getB2c_group_price());
						} else {
							logger.debug("B2C price updated: Id=" + hotel.getUsers_id() + ", IndividualPrice=" + hotel.getB2c_individual_price()
									+ ", GroupPrice=" + hotel.getB2c_group_price());
						}
						feedback.success("Цены гостиницы \""+hotel.getDisplay_name()+"\" для Б2С изменен");
					} catch (Exception e) {
						logger.error("Exception", e);
						feedback.error("Ошибка, цены гостиницы \""+hotel.getDisplay_name()+"\" для Б2С неизменен");
					}

					target.add(feedback);
				}
				
				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
					super.onError(target, form);
					target.add(feedback);
				}
			});
		}
	}

	@Override
	public Class<?> implementedClass() {
		return getClass();
	}

	@Override
	public IModel<String> getTitle() {
		return new StringResourceModel("hotels.b2c.edit.title", new Model<Hotel>(hotel));
	}
}
