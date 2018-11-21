package uz.hbs.actions.touragent.reservations.panels;

import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.beans.IndividualReservation;
import uz.hbs.beans.MealOption;
import uz.hbs.beans.ReservationRuleType;
import uz.hbs.beans.ReservationType;
import uz.hbs.beans.RoomType;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;
import uz.hbs.session.MySession;
import uz.hbs.utils.CommonUtil;
import uz.hbs.utils.FormatUtil;

public class BookingReservationDetailsView extends MyBreadCrumbPanel {
	private static final long serialVersionUID = 1L;
	private FeedbackPanel feedback;

	public BookingReservationDetailsView(String id, IBreadCrumbModel breadCrumbModel, final IndividualReservation reserve, final boolean withPrint) {
		super(id, breadCrumbModel);
		
		add(feedback = new FeedbackPanel("feedback"));
		feedback.setOutputMarkupId(true);
		
		add(new Label("hotelName", new Model<String>(){
			private static final long serialVersionUID = 1L;
			
			@Override
			public String getObject() {
				return new MyBatisHelper().selectOne("selectTAHotelName", reserve.getHotelsusers_id());
			}
		}));
		
		ReservationRuleType rule = new MyBatisHelper().selectOne("selectTAReservationRule", new ReservationRuleType(reserve.getHotelsusers_id(), reserve.isIs_group()));
		
		if (rule == null) feedback.error("Reservation Rule is not indicated.");
		
		add(new Label("totalPrice", new LoadableDetachableModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected String load() {
				return new StringResourceModel("touragents.newbooking.total_price_label", null, new Object[]{ CommonUtil.currencyConverted(((MySession) getSession()).getCurrency(), reserve.getTotal()), ((MySession)getSession()).getCurrencyName()}).getString();
			}
		}));
		
		if (reserve.getRoomtype() == null){
			if (reserve.getId() != null) reserve.setRoomtype((RoomType) new MyBatisHelper().selectOne("selectReservationRoomType", reserve.getId()));
			else reserve.setRoomtype(new RoomType("Unkhown"));
		} else {
			if (reserve.getRoomtype().getName() == null) {
				reserve.getRoomtype().setName((String) new MyBatisHelper().selectOne("selectRoomTypeNameById", reserve.getRoomtype().getId()));
			}
		}
		
		add(new Label("roomTypeLabel", new StringResourceModel("touragents.newbooking.roomtype_label", null, new Object[]{ 1, reserve.getRoomtype().getName() })));
		add(new Label("amountOfNightsLabel", new StringResourceModel("touragents.newbooking.price_of_nights", null, new Object[]{reserve.getNumber_of_nights()})));
		add(new Label("amountOfGuestsLabel", new StringResourceModel("touragents.newbooking.for_number_of_guests", null, new Object[]{reserve.getAdults()})));
//		add(new Label("serviceChargeIncludedLabel", new StringResourceModel("touragents.newbooking.service_charge_included", null, new Object[]{rule.getService_charge()})));
//		add(new Label("cityTaxIncludedLabel", new StringResourceModel("touragents.newbooking.city_tax_inlcuded", null, new Object[]{rule.getcity_tax() * ((MySession) getSession()).getCurrency().getValue(), ((MySession)getSession()).getCurrencyName()})));
		add(new Label("checkInLabel", new StringResourceModel("touragents.newbooking.check_in_label", null, new Object[]{
				new StringResourceModel(CommonUtil.getWeekDay(reserve.getCheck_in()), null).getString(),
				FormatUtil.toString(reserve.getCheck_in(), "dd/MM/yyyy"),
				CommonUtil.getHotelTime(rule.getCheck_in_from_time())
		})));
		add(new Label("checkOutLabel", new StringResourceModel("touragents.newbooking.check_out_label", null, new Object[]{
				new StringResourceModel(CommonUtil.getWeekDay(reserve.getCheck_out()), null).getString(),
				FormatUtil.toString(reserve.getCheck_out(), "dd/MM/yyyy"),
				CommonUtil.getHotelTime(rule.getCheck_out_to_time())
		})));
		
		add(new ReserveForm("form", reserve).add(new WebMarkupContainer("print"){
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible() {
				return withPrint;
			}
		}));
	}
	
	private class ReserveForm extends Form<IndividualReservation>{
		private static final long serialVersionUID = 1L;

		public ReserveForm(String id, final IndividualReservation reserve) {
			super(id, new CompoundPropertyModel<IndividualReservation>(reserve));
			
			TextField<String> person_title;
			add(person_title = new TextField<String>("person_title", new AbstractReadOnlyModel<String>() {
				private static final long serialVersionUID = 1L;

				@Override
				public String getObject() {
					return null; //PersonTitleModel.toString(reserve.getPerson_title());
				}
			}));
			person_title.setLabel(new StringResourceModel("hotels.reservation.details.appeal", null)).setEnabled(false);
			
			TextField<String> first_name, last_name, citizenship;
			
			add(first_name = new TextField<String>("first_name"));
			first_name.setLabel(new StringResourceModel("touragents.accountant_details.first_name", null));
			first_name.setEnabled(false);
			first_name.setOutputMarkupId(true);
			
			add(last_name = new TextField<String>("last_name"));
			last_name.setLabel(new StringResourceModel("touragents.accountant_details.last_name", null));
			last_name.setEnabled(false);
			last_name.setOutputMarkupId(true);
			
			add(citizenship = new TextField<String>("citizenship"));
			citizenship.setLabel(new StringResourceModel("touragents.accountant_details.last_name", null));
			citizenship.setEnabled(false);
			citizenship.setOutputMarkupId(true);
			
//			final HiddenField<Short> extra_bed;
//			add(extra_bed = new HiddenField<Short>("extra_bed"));
//			extra_bed.setOutputMarkupId(true);
			
			add(new CheckBox("extra_bed_needed").setEnabled(false));
			add(new CheckBox("non_smokers").setEnabled(false));
			add(new CheckBox("city_view").setEnabled(false));
			
			final HiddenField<Byte> meal_options;
			add(meal_options = new HiddenField<Byte>("meal_options"));
			meal_options.setLabel(new StringResourceModel("hotels.reservation.details.meal_options", null));
			meal_options.setEnabled(false);
			meal_options.setOutputMarkupId(true);

			Label meal_options_cost_label = new Label("meal_options_cost_label", new StringResourceModel(
					"hotels.reservation.details.meal_options_cost_label", null, new Object[] { ((MySession) getSession()).getCurrencyName() }));
			add(meal_options_cost_label);
			
			add(new HiddenField<Double>("breakfast_cost", new Model<Double>(){
				private static final long serialVersionUID = 1L;
				
				@Override
				public Double getObject() {
					return CommonUtil.nvl((Double) new MyBatisHelper().selectOne("selectTAMealOptionValue", new MealOption(MealOption.BREAKFAST, reserve.getHotelsusers_id())));
				}
			}));
			add(new HiddenField<Double>("lunch_cost", new Model<Double>(){
				private static final long serialVersionUID = 1L;
				
				@Override
				public Double getObject() {
					return CommonUtil.nvl((Double) new MyBatisHelper().selectOne("selectTAMealOptionValue", new MealOption(MealOption.LUNCH, reserve.getHotelsusers_id())));
				}
			}));
			add(new HiddenField<Double>("dinner_cost", new Model<Double>(){
				private static final long serialVersionUID = 1L;
				
				@Override
				public Double getObject() {
					return CommonUtil.nvl((Double) new MyBatisHelper().selectOne("selectTAMealOptionValue", new MealOption(MealOption.DINNER, reserve.getHotelsusers_id())));
				}
			}));
			
			RadioGroup<Byte> reservation_type;
			add(reservation_type = new RadioGroup<Byte>("reservation_type"));
			reservation_type.setEnabled(false);
			reservation_type.add(new Radio<Byte>("definite", new Model<Byte>(ReservationType.DEFINITE)));
			reservation_type.add(new Radio<Byte>("tentative", new Model<Byte>(ReservationType.TENTATIVE)));
			add(new TextArea<String>("ta_comments").setEnabled(false));
		}
	}

	@Override
	public Class<?> implementedClass() {
		return this.getClass();
	}

	@Override
	public IModel<String> getTitle() {
		return new StringResourceModel("touragents.newbooking.reservation_details", null);
	}
}
