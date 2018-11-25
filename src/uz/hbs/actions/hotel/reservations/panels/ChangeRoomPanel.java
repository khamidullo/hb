package uz.hbs.actions.hotel.reservations.panels;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.core.util.string.JavaScriptUtils;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.util.value.ValueMap;

import uz.hbs.beans.AdditionalBed;
import uz.hbs.beans.MealOption;
import uz.hbs.beans.ReservationDetail;
import uz.hbs.beans.ReservationRoom;
import uz.hbs.beans.Room;
import uz.hbs.beans.RoomState;
import uz.hbs.beans.RoomType;
import uz.hbs.beans.rate.RateDetails;
import uz.hbs.beans.rate.RatePlane;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;
import uz.hbs.session.MySession;
import uz.hbs.utils.CommonUtil;
import uz.hbs.utils.RequiredFieldAjaxCallListener;
import uz.hbs.utils.models.HotelModels;
import uz.hbs.utils.models.MyChoiceRenderer;
import uz.hbs.utils.models.RoomSetupModel;

public class ChangeRoomPanel extends MyBreadCrumbPanel {
	private static final long serialVersionUID = 1L;
	private ReservationDetail reserve;
	private MyFeedbackPanel feedback;
	private ReservationRoom reserveroom;
	
	public ChangeRoomPanel(String id, IBreadCrumbModel breadCrumbModel, final IModel<ReservationRoom> model) {
		super(id, breadCrumbModel);
		reserveroom = model.getObject();
		reserve = new MyBatisHelper().selectOne("selectCurrentReserveDetails", reserveroom.getReservations_id());
		
		add(feedback = new MyFeedbackPanel("feedback"));
		feedback.setOutputMarkupId(true);
		
		add(new Label("room_number", model.getObject().getRoom().getRoom_number()));
		add(new Label("roomtype", model.getObject().getRoomtype().getName()));
		add(new Label("meal_options", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
//				switch (reserve.getMeal_options()){
//				case MealOption.BB: return getString("meal_options.bb");
//				case MealOption.HB_LUNCH: return getString("meal_options.hb");
//				case MealOption.HB_DINNER: return getString("meal_options.hb");
//				case MealOption.FB: return getString("meal_options.fb");
//				}
				return null;
			}
		}));
//		add(new Label("adults", reserveroom.getGuest_count()));
//		add(new Label("children", reserveroom.getChildren()));
		add(new Label("additional_bed", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				return (reserve.getAdditional_bed().isId())?getString("yes"):getString("no");
			}
		}));
//		add(new Label("room_rate", reserve.getRate()));
		final Form<ValueMap> form;
		ValueMap roommodel = new ValueMap();
		roommodel.put("roomtype", reserveroom.getRoomtype());
		roommodel.put("room", reserveroom.getRoom());
		roommodel.put("roomtype_id", reserveroom.getRoomtype().getId());
		roommodel.put("rooms_id", reserveroom.getRoom().getId());
//		roommodel.put("rate", reserve.getRate());
//		roommodel.put("additional_bed", reserve.getAdditional_bed());
		//roommodel.put("extra_bed", CommonUtil.nvl(reserve.getExtra_bed()));
		roommodel.put("adults", reserve.getAdults());
		roommodel.put("children", reserve.getChildren());
//		roommodel.put("meal_options", reserve.getMeal_options());
		roommodel.put("hotel_id", reserve.getHotelsusers_id());
		
		add(form = new MoveGuestForm("form", roommodel));
		form.add(new IndicatingAjaxButton("move") {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				super.onError(target, form);
				target.add(feedback);
			}
			
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				ValueMap reserveroom2 = (ValueMap) form.getDefaultModelObject();
				try {
					SqlSession sql = MyBatisHelper.getSqlSessionFactory().openSession(false);
					try {
						reserve.setInitiator_user_id(((MySession) getSession()).getUser().getId());
//						reserve.setMeal_options((byte) reserveroom2.getInt("meal_options"));
//						reserve.setRate((BigDecimal) reserveroom2.get("rate"));
						
//						if (((AdditionalBed) reserveroom2.get("additional_bed")).isId()) 
//							reserve.setExtra_bed((short) 1); 
//						else 
//							reserve.setExtra_bed((short) 0);  
						
//						sql.update("updateReservationRoom", new ReservationRoom(reserve, reserveroom2.getInt("roomtype_id"), reserveroom2.getLong("rooms_id"), reserve.getInitiator_user_id()));
						sql.update("updateCurrentReserveDetail", reserve);
						if (reserveroom.getRoom().getId().longValue() != reserveroom2.getLong("rooms_id")) {
							HashMap<String, Serializable> param = new HashMap<String, Serializable>();
							param.put("id", reserveroom.getRoom().getId());
							param.put("old_state", RoomState.OCCUPIED);
							param.put("new_state", RoomState.VACANT);
							param.put("initiator_user_id", ((MySession) getSession()).getUser().getId());
							
							sql.update("updateRoomState", param);
							param.clear();
							
							param.put("id", reserveroom2.getLong("rooms_id"));
							param.put("old_state", RoomState.VACANT);
							param.put("new_state", RoomState.OCCUPIED);
							param.put("initiator_user_id", ((MySession) getSession()).getUser().getId());
							sql.update("updateRoomState", param);
						}

						sql.commit();
						feedback.success(getString("hotels.reservation.detail.change_room.success"));
					} catch (Exception e) {
						logger.error("Exception", e);
						feedback.error(getString("hotels.reservation.detail.change_room.fail"));
						sql.rollback();
					} finally {
						sql.close();
					}
				} catch (Exception e) {
					logger.error("Exception", e);
				} finally {
					target.add(feedback);
				}
			}
			
			@Override
			protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
				super.updateAjaxAttributes(attributes);
				attributes.getAjaxCallListeners().add(new RequiredFieldAjaxCallListener(form.getMarkupId(), ((MySession) getSession()).getLocale().getLanguage()));
			}
		});
	}
	
	private class MoveGuestForm extends Form<ValueMap>{
		private static final long serialVersionUID = 1L;
		
		public MoveGuestForm(String id, final ValueMap model) {
			super(id, new CompoundPropertyModel<ValueMap>(model));
			final HashMap<String, Serializable> param = new HashMap<String, Serializable>();
			param.put("hotel_id", reserve.getHotelsusers_id());
			param.put("check_out", reserve.getCheck_out());
			param.put("reserved_id", reserve.getId());
			param.put("isTA", Boolean.FALSE);
			param.put("adults", reserve.getAdults());
			param.put("roomtype_id", reserveroom.getRoomtype().getId());
			param.put("rooms_id", reserveroom.getRoom().getId());
			
			IModel<List<RoomType>> roomtypesListModel = new LoadableDetachableModel<List<RoomType>>() {
				private static final long serialVersionUID = 1L;
				
				@Override
				protected List<RoomType> load() {
					return new MyBatisHelper().selectList("selectReserveRoomTypeByHotel", param);
				}
			};
			final DropDownChoice<RoomType> roomtype;
			add(roomtype = new DropDownChoice<RoomType>("roomtype", roomtypesListModel, new ChoiceRenderer<RoomType>("name", "id")){
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isEnabled() {
					return ! reserve.isIs_group();
				}
			});
			
			final HiddenField<Integer> roomtype_id;
			add(roomtype_id = new HiddenField<Integer>("roomtype_id"));
			roomtype_id.setLabel(new StringResourceModel("hotels.reservation.details.room.type", null));
			roomtype_id.setRequired(true);
			roomtype_id.setOutputMarkupId(true);
			
			IModel<List<Room>> roomListModel = new LoadableDetachableModel<List<Room>>() {
				private static final long serialVersionUID = 1L;

				@Override
				protected List<Room> load() {
					ValueMap model = (ValueMap) getDefaultModelObject();
					model.put("roomtype_id", ((RoomType) model.get("roomtype")).getId());
					if (model.get("roomtype_id") != null) {
						param.put("roomtype_id", model.getInt("roomtype_id"));
						param.put("adults", (short) model.getInt("adults"));
						param.put("additional_bed", ((AdditionalBed) model.get("additional_bed")).isId());
						param.put("check_in", CommonUtil.now());
						if (reserveroom.getRoom() != null) param.put("rooms_id", reserveroom.getRoom().getId());
						return new MyBatisHelper().selectList("selectRoomReserveList", param);
					} else {
						return Collections.emptyList();
					}
				}
			};
			final DropDownChoice<Room> room;
			add(room = new DropDownChoice<Room>("room", roomListModel, new ChoiceRenderer<Room>(){
				private static final long serialVersionUID = 1L;
				
				@Override
				public String getIdValue(Room object, int index) {
					return String.valueOf(object.getId());
				}
				
				@Override
				public Object getDisplayValue(Room object) {
					return object.getRoom_number() + " (" + RoomSetupModel.getHoldingCapacity(object.getHolding_capacity().getId()) + ")";
				}
			}));
			room.setOutputMarkupId(true);
			room.setNullValid(true);
			
			final HiddenField<Long> room_id;
			add(room_id = new HiddenField<Long>("rooms_id"){
				private static final long serialVersionUID = 1L;

				@Override
				protected void onAfterRender() {
					super.onAfterRender();
					JavaScriptUtils.writeJavaScript(getResponse(), "if ($('#" + room.getMarkupId() + "').val() != undefined) {$('#" + getMarkupId() + "').val($('#" + room.getMarkupId() + "').val());}");
				}
			});
			room_id.setLabel(new StringResourceModel("hotels.reservation.details.room", null));
			room_id.setRequired(true);
			room_id.setOutputMarkupId(true);

			final TextField<BigDecimal> rate;
			add(rate = new TextField<BigDecimal>("rate", BigDecimal.class));
			rate.setLabel(new StringResourceModel("hotels.reservation.details.room.rate", null));
			rate.setRequired(true);
			rate.setOutputMarkupId(true);
			
			final DropDownChoice<AdditionalBed> additional_bed;
			add(additional_bed = new DropDownChoice<AdditionalBed>("additional_bed", RoomSetupModel.getAdditionalBed(), new ChoiceRenderer<AdditionalBed>("name", "id")));
			additional_bed.setLabel(new StringResourceModel("hotels.reservation.details.additional_bed", null));
			additional_bed.setOutputMarkupId(true);
			additional_bed.setRequired(true);
			
			final HiddenField<Short> extra_bed;
			add(extra_bed = new HiddenField<Short>("extra_bed"));
			extra_bed.setLabel(new StringResourceModel("hotels.reservation.details.additional_bed.extra_bed", null));
			extra_bed.setOutputMarkupId(true);

			additional_bed.add(new AjaxFormComponentUpdatingBehavior("onchange") {
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target) {
//					if (Strings.isTrue(additional_bed.getValue())){
//						reserve.setExtra_bed(ReservationDetail.EXTRA_BED_ONE);
//					} else {
//						reserve.setExtra_bed(null);
//					}
					target.add(extra_bed);
				}
			});
			
			IModel<List<Short>> adultsModel = new LoadableDetachableModel<List<Short>>() {
				private static final long serialVersionUID = 1L;

				@Override
				protected List<Short> load() {
					List<Short> list = new ArrayList<Short>();
					short max_person = new MyBatisHelper().selectOne("selectMaxHoldingCapacity", reserve.getHotelsusers_id());
					for (short b = reserve.getAdults(); b <= max_person; b++){ //reserv.getRoom().getHolding_capacity().getId()
						list.add(b);
					}
					return list;
				}
			};
			final DropDownChoice<Short> adults;
			add(adults = new DropDownChoice<Short>("adults", adultsModel, new MyChoiceRenderer<Short>()));
			adults.setLabel(new StringResourceModel("hotels.reservation.details.adults", null));
			adults.setOutputMarkupId(true);
			adults.setRequired(true);
			
			final DropDownChoice<Short> children;
			add(children = new DropDownChoice<Short>("children", HotelModels.getShortListModel(reserve.getChildren(), (short) 12), new MyChoiceRenderer<Short>()));
			children.setLabel(new StringResourceModel("hotels.reservation.details.children", null));
			children.setRequired(true);
			
			roomtype.add(new AjaxFormComponentUpdatingBehavior("onchange") {
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					ValueMap model = (ValueMap) getDefaultModelObject();
					if (model.get("roomtype") != null) model.put("roomtype_id", ((RoomType) model.get("roomtype")).getId());
					else model.put("roomtype_id", null);
					model.put("room", null);
					model.put("rooms_id", null);
					target.add(adults);
					target.add(roomtype_id);
					target.add(room);
				}
			});
			
			room.add(new AjaxFormComponentUpdatingBehavior("onchange") {
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					ValueMap model = (ValueMap) getDefaultModelObject();
					if (model.get("room") != null) model.put("rooms_id", ((Room) model.get("room")).getId()); else model.put("rooms_id", null);
					RateDetails roomrate = getCurrentRate(reserve.getHotelsusers_id(), (short) model.getInt("adults"), reserve.isIs_group(), model.getInt("roomtype_id"));
					if (roomrate != null) {
//						reserve.setRate(roomrate.getRate(reserve.isResident()));
						reserve.setRateplane(new RatePlane(roomrate.getRateplane_id()));
					}
					target.add(room);
					target.add(rate);
					target.add(additional_bed);
					target.add(room_id);
				}
			});
			
			WebMarkupContainer meal_container;
			add(meal_container = new WebMarkupContainer("meal_container"));
			
			final HiddenField<Byte> meal_options;
			meal_container.add(meal_options = new HiddenField<Byte>("meal_options"));
			meal_options.setLabel(new StringResourceModel("hotels.reservation.details.meal_options", null));
			meal_options.setRequired(true);
			meal_options.setOutputMarkupId(true);
			
			meal_container.add(new HiddenField<Double>("breakfast_cost", new Model<Double>(){
				private static final long serialVersionUID = 1L;
				
				@Override
				public Double getObject() {
					return CommonUtil.nvl((Double) new MyBatisHelper().selectOne("selectTAMealOptionValue", new MealOption(MealOption.BREAKFAST, reserve.getHotelsusers_id())));
				}
			}));
			meal_container.add(new HiddenField<Double>("lunch_cost", new Model<Double>(){
				private static final long serialVersionUID = 1L;
				
				@Override
				public Double getObject() {
					return CommonUtil.nvl((Double) new MyBatisHelper().selectOne("selectTAMealOptionValue", new MealOption(MealOption.LUNCH, reserve.getHotelsusers_id())));
				}
			}));
			meal_container.add(new HiddenField<Double>("dinner_cost", new Model<Double>(){
				private static final long serialVersionUID = 1L;
				
				@Override
				public Double getObject() {
					return CommonUtil.nvl((Double) new MyBatisHelper().selectOne("selectTAMealOptionValue", new MealOption(MealOption.DINNER, reserve.getHotelsusers_id())));
				}
			}));
			adults.add(new AjaxFormComponentUpdatingBehavior("onchange") {
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					ValueMap model = (ValueMap) getDefaultModelObject();
					RateDetails roomrate = getCurrentRate(reserve.getHotelsusers_id(), (short) model.getInt("adults"), reserve.isIs_group(), model.getInt("roomtype_id"));
					if (roomrate != null) model.put("rate", roomrate.getRate(reserve.isResident()));
					target.add(rate);
				}
			});
			
			CommonUtil.setFormComponentRequired(this);
		}
	}

	@Override
	public Class<?> implementedClass() {
		return this.getClass();
	}

	@Override
	public IModel<String> getTitle() {
		return new StringResourceModel("hotels.reservation.room.change.title", new Model<ReservationDetail>(reserve));
	}
	
	public RateDetails getCurrentRate(long hotel_id, short person_number, boolean is_group, int roomtype_id){
		HashMap<String, Serializable> param = new HashMap<String, Serializable>();
		param.put("hotel_id", hotel_id);
		param.put("person_number", person_number);
		param.put("is_group", is_group);
		param.put("roomtype_id", roomtype_id);
		return ((RateDetails) new MyBatisHelper().selectOne("selectCurrentRatePlane", param));
	}
}
