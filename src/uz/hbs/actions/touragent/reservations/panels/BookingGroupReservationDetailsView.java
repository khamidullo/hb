package uz.hbs.actions.touragent.reservations.panels;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.validation.validator.RangeValidator;

import uz.hbs.beans.GroupReservation;
import uz.hbs.beans.MealOption;
import uz.hbs.beans.ReservationRoom;
import uz.hbs.beans.ReservationRuleType;
import uz.hbs.beans.ReservationType;
import uz.hbs.beans.RoomType;
import uz.hbs.components.ajax.AjaxOnBlurEvent;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;
import uz.hbs.session.MySession;
import uz.hbs.utils.CommonUtil;
import uz.hbs.utils.FormatUtil;
import uz.hbs.utils.models.HotelModels;
import uz.hbs.utils.models.MyChoiceRenderer;

public class BookingGroupReservationDetailsView extends MyBreadCrumbPanel {
	private static final long serialVersionUID = 1L;
	
	public BookingGroupReservationDetailsView(String id, IBreadCrumbModel breadCrumbModel, final GroupReservation reserve, final boolean withPrint) {
		super(id, breadCrumbModel);
		final MyFeedbackPanel feedback;
		add(feedback = new MyFeedbackPanel("feedback"));
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
				return new StringResourceModel("touragents.newbooking.total_price_label", null, new Object[]{ CommonUtil.currencyConverted(((MySession) getSession()).getCurrency(), reserve.getTotal()), ((MySession)getSession()).getCurrencyName() }).getString();
			}
		}));
		
		add(new Label("amountOfNightsLabel", new StringResourceModel("touragents.newbooking.price_of_nights", null, new Object[]{reserve.getNumber_of_nights()})));
		add(new Label("amountOfGuestsLabel", new StringResourceModel("touragents.newbooking.for_number_of_guests", null, new Object[]{reserve.getAdults()})));
//		add(new Label("serviceChargeIncludedLabel", new StringResourceModel("touragents.newbooking.service_charge_included", null, new Object[]{rule.getService_charge()})));
		add(new Label("cityTaxIncludedLabel", new StringResourceModel("touragents.newbooking.city_tax_inlcuded", null, new Object[]{rule.getcity_tax()})));
		
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
		
		final ReserveForm reserveform;
		add(reserveform = new ReserveForm("reserveform", reserve));
		reserveform.add(new WebMarkupContainer("print"){
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible() {
				return withPrint;
			}
		});
	}
	
	private class ReserveForm extends Form<GroupReservation>{
		private static final long serialVersionUID = 1L;
		private DropDownChoice<Short> room;

		public ReserveForm(String id, final GroupReservation reserve) {
			super(id, new CompoundPropertyModel<GroupReservation>(reserve));
			
			TextField<String> group_name;
			add(group_name = new TextField<String>("name"));
			group_name.setLabel(new StringResourceModel("hotels.reservation.details.group.name", null));
			group_name.setEnabled(false);
			
			TextField<String> responsible;
			add(responsible = new TextField<String>("responsible"));
			responsible.setLabel(new StringResourceModel("hotels.reservation.details.group.responsible", null));
			responsible.setEnabled(false);
			
			final TextField<BigDecimal> total;
			add(total = new TextField<BigDecimal>("total"));
			total.setLabel(new StringResourceModel("hotels.reservation.details.total", null));
			total.setOutputMarkupId(true);
			total.setEnabled(false);

			final WebMarkupContainer container;
			add(container = new WebMarkupContainer("container"));
			container.setOutputMarkupId(true);
			container.add(new ListView<ReservationRoom>("reservation_room_list", new LoadableDetachableModel<List<ReservationRoom>>() {
				private static final long serialVersionUID = 1L;

				@Override
				protected List<ReservationRoom> load() {
					return reserve.getReserverooms();
				}
			}) {
				private static final long serialVersionUID = 1L;

				@Override
				protected void populateItem(final ListItem<ReservationRoom> item) {
					HashMap<String, Object> param = new HashMap<String, Object>();
					param.put("hotelsusers_id", reserve.getHotelsusers_id());
					
					final List<RoomType> list = new MyBatisHelper().selectList("selectReservationRoomTypeByHotel", param);
					
					final IModel<List<RoomType>> roomtypesListModel = new LoadableDetachableModel<List<RoomType>>() {
						private static final long serialVersionUID = 1L;
						
						@Override
						protected List<RoomType> load() {
							return list;
						}
					};
					
					DropDownChoice<RoomType> roomtype;
					item.add(roomtype = new DropDownChoice<RoomType>("roomtype", new PropertyModel<RoomType>(item.getModel(), "roomtype"), roomtypesListModel, new ChoiceRenderer<RoomType>("name", "id")));
					roomtype.setOutputMarkupId(true);
					roomtype.setEnabled(false);
					
					final IModel<List<Short>> roomCountModel = new LoadableDetachableModel<List<Short>>() {
						private static final long serialVersionUID = 1L;

						@Override
						protected List<Short> load() {
							List<Short> list = new ArrayList<Short>();
							HashMap<String, Object> param = new HashMap<String, Object>();
							
							if (item.getModelObject().getRoomtype() != null) {
								param.put("roomtypes_id", item.getModelObject().getRoomtype().getId());
								param.put("check_in", reserve.getCheck_in());
								if (item.getModelObject().getRoomtype() != null) {
									short room_count = new MyBatisHelper().selectOne("selectTAReserveAvailableRoomsByRoomType", param);
									if (room_count == 0) room_count = item.getModelObject().getRoom_count();
									if (room_count > 0) {
										for (short sh = 1; sh <= room_count; sh++) {
											list.add(sh);
										}
									}
								}
							}
							return list;
						}
					};
					
					item.add(room = new DropDownChoice<Short>("room", new PropertyModel<Short>(item.getModel(), "room_count"), roomCountModel, new MyChoiceRenderer<Short>()));
					room.setLabel(new StringResourceModel("hotels.reservation.details.room", null));
					room.setEnabled(false);
					room.setOutputMarkupId(true);
					room.add(new AjaxOnBlurEvent());
					
					final HiddenField<Integer> roomtype_id;
					item.add(roomtype_id = new HiddenField<Integer>("roomtypes_id", new PropertyModel<Integer>(item.getModel(), "roomtypes_id")));
					roomtype_id.setOutputMarkupId(true);
					roomtype_id.setLabel(new StringResourceModel("hotels.reservation.details.room.type", null));
					roomtype_id.setEnabled(false);
				}
			}.setReuseItems(true).setOutputMarkupId(true));
			
			DropDownChoice<Short> adults;
			add(adults = new DropDownChoice<Short>("adults", HotelModels.getShortListModel((short) 100, false), new MyChoiceRenderer<Short>()));
			adults.setLabel(new StringResourceModel("hotels.reservation.details.adults", null));
			adults.setEnabled(false);
			adults.add(RangeValidator.minimum((short)1));
			
			final DropDownChoice<Short> children;
			add(children = new DropDownChoice<Short>("children", HotelModels.getShortListModel((short) 25, true), new MyChoiceRenderer<Short>()));
			children.setLabel(new StringResourceModel("hotels.reservation.details.children", null));
			children.setEnabled(false);
			
//			final WebMarkupContainer child_age_container;
//			add(child_age_container = new WebMarkupContainer("child_age_container"));
//			child_age_container.setOutputMarkupPlaceholderTag(true);
//			child_age_container.setVisible(true);
//			
//			child_age_container.add(new ListView<ChildAge>("childAgeList", reserve.getChildAgeList()) {
//				private static final long serialVersionUID = 1L;
//
//				@Override
//				protected void populateItem(ListItem<ChildAge> item) {
//					DropDownChoice<Short> age;
//					item.add(age = new DropDownChoice<Short>("childAge", new PropertyModel<Short>(item.getModel(), "age"), HotelModels.getShortListModel((short) 17, true), new MyChoiceRenderer<Short>()));
//				}
//			}.setReuseItems(true));
			
			WebMarkupContainer meal_container;
			add(meal_container = new WebMarkupContainer("meal_container"));
			
			final HiddenField<Byte> meal_options;
			meal_container.add(meal_options = new HiddenField<Byte>("meal_options"));
			meal_options.setLabel(new StringResourceModel("hotels.reservation.details.meal_options", null));
			meal_options.setEnabled(false);
			meal_options.setOutputMarkupId(true);
			
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
			
			final DropDownChoice<Short> extra_bed;
			add(extra_bed = new DropDownChoice<Short>("extra_bed", HotelModels.getShortListModel((short) 25, true), new MyChoiceRenderer<Short>()));
			extra_bed.setLabel(new StringResourceModel("hotels.reservation.details.additional_bed", null));
			extra_bed.setEnabled(false);
			
			add(new TextArea<String>("guest_comments").setEnabled(false));
			
//			final DropDownChoice<PaymentMethod> payment_method;
//			add(payment_method = new DropDownChoice<PaymentMethod>("payment_method", HotelModels.getPaymentMethodModel(), new ChoiceRenderer<PaymentMethod>("name", "id")));
//			payment_method.setLabel(new StringResourceModel("hotels.reservation.details.payment_method", null));
//			payment_method.setEnabled(false);
//			
//			final WebMarkupContainer payment_method_container;
//			add(payment_method_container = new WebMarkupContainer("payment_method_container"));
//			payment_method_container.setVisible(reserve.getPayment_method_other() != null);
//			payment_method_container.setOutputMarkupPlaceholderTag(true);
//			
//			final TextField<String> payment_method_other;
//			payment_method_container.add(payment_method_other = new TextField<String>("payment_method_other"));
//			payment_method_other.setLabel(new StringResourceModel("payment_method.other", null));
			
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
		return new StringResourceModel("hotels.reservation.details.group", null);
	}
}
