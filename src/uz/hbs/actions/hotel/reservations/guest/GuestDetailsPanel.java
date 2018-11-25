package uz.hbs.actions.hotel.reservations.guest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.attributes.IAjaxCallListener;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.form.ListChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.MyWebApplication;
import uz.hbs.actions.hotel.reports.HotelReportPage;
import uz.hbs.beans.Address;
import uz.hbs.beans.Child;
import uz.hbs.beans.Country;
import uz.hbs.beans.Gender;
import uz.hbs.beans.Guest;
import uz.hbs.beans.GuestDetail;
import uz.hbs.beans.Nationality;
import uz.hbs.beans.Region;
import uz.hbs.beans.ReservationDetail;
import uz.hbs.beans.ReservationRoom;
import uz.hbs.beans.ReservationStatus;
import uz.hbs.beans.Room;
import uz.hbs.beans.RoomState;
import uz.hbs.beans.RoomType;
import uz.hbs.components.ajax.AjaxOnBlurEvent;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;
import uz.hbs.markup.html.form.textfield.MyDatePicker;
import uz.hbs.session.MySession;
import uz.hbs.utils.CommonUtil;
import uz.hbs.utils.RequiredFieldAjaxCallListener;
import uz.hbs.utils.models.GenderModel;
import uz.hbs.utils.models.HotelModels;
import uz.hbs.utils.models.MyChoiceRenderer;

public class GuestDetailsPanel extends MyBreadCrumbPanel {
	private static final long serialVersionUID = 1L;
	private GuestDetail details;
	private MyFeedbackPanel feedback;
	private boolean isDone = false;
	private List<Room> roomlist = new ArrayList<Room>();

	public GuestDetailsPanel(String id, IBreadCrumbModel breadCrumbModel, final ReservationDetail reserve) {
		super(id, breadCrumbModel);
		details = reserve.getDetails();
		
		add(feedback = new MyFeedbackPanel("feedback"));
		feedback.setOutputMarkupId(true);
		
		if (details == null){
			List<Guest> guestlist = new MyBatisHelper().selectList("selectGuestsList", reserve.getId());
			
			if (guestlist.isEmpty()){
				details = new GuestDetail(reserve.getAdults(), reserve.getChildren(), null, null);
			} else {
				details = new GuestDetail(guestlist);
				
				List<Child> childlist = new MyBatisHelper().selectList("selectChildrenList", reserve.getId());
				if (childlist != null) details.setChildlist(childlist);
			}
			reserve.setDetails(details);
			details.setSelectedGuest(details.getGuestlist().get(0));
		} else {
			if (details.getGuestlist().isEmpty()){
				List<Guest> guestlist = new MyBatisHelper().selectList("selectGuestsList", reserve.getId());
				if (guestlist.size() < reserve.getAdults()){
					for (int i = guestlist.size(); i < reserve.getAdults(); i++){
						guestlist.add(new Guest((short) (i + 1)));
					}
				}
				details.setGuestlist(guestlist);
			}
			if (details.getChildlist().isEmpty()){
				List<Child> childlist = new MyBatisHelper().selectList("selectChildrenList", reserve.getId());
				if (! childlist.isEmpty()) {
					if (childlist.size() < reserve.getChildren()) {
						for (int i = childlist.size(); i < reserve.getChildren(); i++) {
							childlist.add(new Child((byte) (i + 1)));
						}
					}
					details.setChildlist(childlist);
				}
			}
		}
		
		final Form<GuestDetail> form;
		add(form = new Form<GuestDetail>("guestdetailsform", new CompoundPropertyModel<GuestDetail>(details)));
		
		final ListChoice<Guest> guestlist;
		form.add(guestlist = new ListChoice<Guest>("selectedGuest",  new LoadableDetachableModel<List<Guest>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<Guest> load() {
				return details.getGuestlist();
			}
		}, new ChoiceRenderer<Guest>(){
			private static final long serialVersionUID = 1L;

			@Override
			public Object getDisplayValue(Guest object) {
				return new StringResourceModel("hotels.guest.details.guest", null).getString() + " #" + String.valueOf(object.getGuest_index());// object.getGuest_value();
			}
			
			@Override
			public String getIdValue(Guest object, int index) {
				return String.valueOf(object.getGuest_index());
			}
		}));
		guestlist.setMaxRows(10);
		
		final WebMarkupContainer container;
		form.add(container = new WebMarkupContainer("guest_container"));
		container.setOutputMarkupId(true);
		
		container.add(new GuestData("guest_data", new Model<Guest>(details.getGuestlist().get(0)), (short) 0, reserve));
		
		guestlist.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				short guest_id = Short.parseShort(guestlist.getValue());
				container.addOrReplace(new GuestData("guest_data", new Model<Guest>(details.getGuestlist().get(guest_id - 1)), (short) (guest_id - 1), reserve));
				target.add(container);
			}
			
			@Override
			protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
				super.updateAjaxAttributes(attributes);
				IAjaxCallListener listener = new AjaxCallListener(){
					private static final long serialVersionUID = 1L;
					
					@Override
					public CharSequence getBeforeSendHandler(Component component) {
						return "setter();";
					}
					
					@Override
					public CharSequence getCompleteHandler(Component component) {
						return "getter();";
					}
				};
				attributes.getAjaxCallListeners().add(listener);
			}
		});
		
		final Link<Void> done;
		
		form.add(new ListView<Child>("childlist", new LoadableDetachableModel<List<Child>>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected List<Child> load() {
				return details.getChildlist();
			}
		}){
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void populateItem(ListItem<Child> item) {
				item.add(new ChildData("child_data", item.getModel(), (byte) item.getIndex()));
			}
		}.setReuseItems(true));
		
		
		form.add(done = new Link<Void>("done"){
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onClick() {
				setResponsePage(new HotelReportPage(HotelReportPage.REPORTS_BY_RESERVATIONS));
			}
			
			@Override
			public boolean isVisible() {
				return isDone;
			}
		});
		done.setOutputMarkupPlaceholderTag(true);

		form.add(new IndicatingAjaxButton("register") {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				super.onError(target, form);
				target.add(feedback);
			}
			
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				List<Guest> guestlist = details.getGuestlist();
				SqlSession sql = MyBatisHelper.getSqlSessionFactory().openSession(false);
				byte status = sql.selectOne("selectReservationStatusById", reserve.getId());
				if (status == ReservationStatus.RESERVED) {
					try {
						try {
							HashMap<String, Serializable> param = new HashMap<String, Serializable>();
							for (Guest guest : guestlist) {
								if (guest.getId() == null) {
									guest.setReservations_id(reserve.getId());

									Address address = new Address(guest.getCountry(), guest.getRegion(), guest.getCity(), guest.getPostal_index(), guest.getAddress());
									address.setInitiator_user_id(((MySession) getSession()).getUser().getId());
									sql.insert("insertAddresses", address);
									
									if (guest.getRooms_id() != null){
										ReservationRoom reserveroom = new ReservationRoom(reserve.getId(), guest.getRoomtypes_id(), guest.getRooms_id());

										Long reserveroom_id = sql.selectOne("selectReserveRoomIdByParam", reserveroom);
										
										if (reserveroom_id != null){
											guest.setReservationrooms_id(reserveroom_id);
										} else {
											reserveroom.setRoom(null);
											reserveroom_id = sql.selectOne("selectReserveRoomIdByParam", reserveroom);
											if (reserveroom_id != null){
												reserveroom.setRoom(guest.getRoom());
												reserveroom.setId(reserveroom_id);
												reserveroom.setInitiator_user_id(((MySession) getSession()).getUser().getId());
												sql.update("updateReserveRoomById", reserveroom);
												guest.setReservationrooms_id(reserveroom_id);
											}
										}
										guest.setGuest_address(address);
										guest.setInitiator_user_id(((MySession) getSession()).getUser().getId());
										sql.insert("insertGuest", guest);
										param.clear();
										param.put("old_state", RoomState.VACANT);
										param.put("new_state", RoomState.OCCUPIED);
										param.put("initiator_user_id", ((MySession) getSession()).getUser().getId());
										param.put("id", guest.getRooms_id());
										
										sql.update("updateRoomState", param);
									}
								}
								param.clear();
								param.put("old_status", ReservationStatus.RESERVED);
								param.put("new_status", ReservationStatus.CHECKED_IN);
								param.put("initiator_user_id", ((MySession) getSession()).getUser().getId());
								param.put("id", reserve.getId());
								sql.update("updateReservationChangeStatus", param);
							}

							for (Child child : reserve.getDetails().getChildlist()) {
								child.setReservations_id(reserve.getId());
								sql.insert("insertChild", child);
							}
							sql.commit();
							feedback.success(getString("hotels.reservation.details.guest.registered.add.success"));
							isDone = true;
							this.setEnabled(false);
						} catch (Exception e) {
							feedback.error(getString("hotels.reservation.details.guest.registered.add.fail"));
							logger.error("Exception", e);
							sql.rollback();
						} finally {
							sql.close();
						}
					} catch (Exception e) {
						feedback.error(getString("hotels.reservation.details.guest.registered.add.fail"));
						logger.error("Exception", e);
					} finally {
						target.add(feedback);
						target.add(this);
						target.add(done);
					}
				} else if (status == ReservationStatus.CHECKED_IN) {
					try {
						try {
							HashMap<String, Serializable> param = new HashMap<String, Serializable>();

							for (Guest guest : guestlist) {
								if (guest.getId() != null) {
									guest.setReservations_id(reserve.getId());

									Address address = new Address(guest.getCountry(), guest.getRegion(), guest.getCity(), guest.getPostal_index(), guest.getAddress());
									address.setInitiator_user_id(((MySession) getSession()).getUser().getId());
									address.setId(guest.getAddress_id());
									sql.update("updateAddresses", address);
									guest.setGuest_address(address);
									guest.setInitiator_user_id(((MySession) getSession()).getUser().getId());
									sql.update("updateGuest", guest);
								} else if (guest.getId() == null) {
									guest.setReservations_id(reserve.getId());

									Address address = new Address(guest.getCountry(), guest.getRegion(), guest.getCity(), guest.getPostal_index(), guest.getAddress());
									address.setInitiator_user_id(((MySession) getSession()).getUser().getId());
									sql.insert("insertAddresses", address);
									guest.setGuest_address(address);
									guest.setInitiator_user_id(((MySession) getSession()).getUser().getId());
									sql.insert("insertGuest", guest);
									
									if (guest.getRooms_id() != null){
										ReservationRoom reserveroom = new ReservationRoom(reserve.getId(), guest.getRoomtypes_id(), guest.getRooms_id());
										sql.update("updateReserveRoom", reserveroom);
										param.clear();
										param.put("old_state", RoomState.VACANT);
										param.put("new_state", RoomState.OCCUPIED);
										param.put("initiator_user_id", ((MySession) getSession()).getUser().getId());
										param.put("id", guest.getRooms_id());
										
										sql.update("updateRoomState", param);

										param.clear();
										param.put("old_status", ReservationStatus.RESERVED);
										param.put("new_status", ReservationStatus.CHECKED_IN);
										param.put("initiator_user_id", ((MySession) getSession()).getUser().getId());
										param.put("id", reserve.getId());
										sql.update("updateReservationChangeStatus", param);
									}
								}
							}

							for (Child child : reserve.getDetails().getChildlist()) {
								child.setReservations_id(reserve.getId());
								sql.insert("insertChild", child);
								if (child.getId() == null) sql.insert("insertChild", child);
								else sql.update("updateChild", child);
							}
							sql.commit();
							feedback.success(getString("hotels.reservation.details.guest.registered.add.success"));
							isDone = true;
							this.setEnabled(false);
						} catch (Exception e) {
							feedback.error(getString("hotels.reservation.details.guest.registered.add.fail"));
							logger.error("Exception", e);
							sql.rollback();
						} finally {
							sql.close();
						}
					} catch (Exception e) {
						feedback.error(getString("hotels.reservation.details.guest.registered.add.fail"));
						logger.error("Exception", e);
					} finally {
						target.add(feedback);
						target.add(this);
						target.add(done);
					}
				}
			}
			
			@Override
			protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
				super.updateAjaxAttributes(attributes);
				attributes.getAjaxCallListeners().add(new RequiredFieldAjaxCallListener(form.getMarkupId(), ((MySession) getSession()).getLocale().getLanguage()));
			}
			
			@Override
			public boolean isEnabled() {
				return reserve.getStatus().getId() == ReservationStatus.RESERVED || reserve.getStatus().getId() == ReservationStatus.CHECKED_IN;
			}
			
			
			@Override
			public boolean isVisible() {
				return ! isDone;
			}
		});
		CommonUtil.setFormComponentRequired(form);
	}
	
	
	private class GuestData extends Panel{
		private static final long serialVersionUID = 1L;

		public GuestData(String id, final IModel<Guest> model, final short guest, final ReservationDetail reserve) {
			super(id, model);
//			if (reserve.getRoomtype() != null && reserve.getRoomtype().getId() != null) {
//				model.getObject().setRoomtype(reserve.getRoomtype());
//				model.getObject().setRoomtypes_id(reserve.getRoomtype().getId());
//			}
			if (reserve.isResident()) {
				if (model.getObject().getNationality() == null) {
					model.getObject().setNationality(new Nationality("uz"));
				}
			}
			add(new Label("guest", new AbstractReadOnlyModel<String>() {
				private static final long serialVersionUID = 1L;

				@Override
				public String getObject() {
					if (guest == 0) return getString("hotels.guest.details.guest.main");
					return getString("hotels.guest.details.guest") + " #" + String.valueOf(guest + 1);
				}
			}));
			
			IModel<List<RoomType>> roomtypesListModel = new LoadableDetachableModel<List<RoomType>>() {
				private static final long serialVersionUID = 1L;
				
				@Override
				protected List<RoomType> load() {
					return new MyBatisHelper().selectList("selectReservationRoomType", reserve.getId());
				}
			};
			DropDownChoice<RoomType> roomtype;
			add(roomtype = new DropDownChoice<RoomType>("roomtype", new PropertyModel<RoomType>(model, "roomtype"), roomtypesListModel, new ChoiceRenderer<RoomType>("name", "id")){
				private static final long serialVersionUID = 1L;

				@Override
				protected void onComponentTag(ComponentTag tag) {
					super.onComponentTag(tag);
					tag.put("data-bind", "roomtype" + guest);
				}
			});
			roomtype.setNullValid(true);
			roomtype.setEnabled(reserve.getStatus().getId() == ReservationStatus.RESERVED);
			
			IModel<List<Room>> roomListModel = new LoadableDetachableModel<List<Room>>() {
				private static final long serialVersionUID = 1L;

				@Override
				protected List<Room> load() {
					if (reserve.isIs_group()) {
						HashMap<String, Serializable> param = new HashMap<String, Serializable>();
						if (model.getObject().getRoomtypes_id() != null) {
							param.put("roomtype_id", model.getObject().getRoomtypes_id());
							param.put("check_in", reserve.getCheck_in());
							param.put("check_out", reserve.getCheck_out());
							param.put("adults", 0);
							if (model.getObject().getRooms_id() != null) param.put("rooms_id", model.getObject().getRooms_id());
							return new MyBatisHelper().selectList("selectRoomReserveList", param);
						} else {
							return Collections.emptyList();
						}
					} else {
						if (guest == 0){
							HashMap<String, Serializable> param = new HashMap<String, Serializable>();
							if (model.getObject().getRoomtypes_id() != null) {
								param.put("roomtype_id", model.getObject().getRoomtypes_id());
								param.put("check_in", reserve.getCheck_in());
								param.put("check_out", reserve.getCheck_out());
								param.put("adults", reserve.getAdults());
								if (model.getObject().getRooms_id() != null) param.put("rooms_id", model.getObject().getRooms_id());
								return new MyBatisHelper().selectList("selectRoomReserveList", param);
							} else {
								return Collections.emptyList();
							}
						} else {
							if (model.getObject().getRooms_id() != null && roomlist.isEmpty()) {
								roomlist = new MyBatisHelper().selectList("selectRoomById", model.getObject().getRooms_id());
							}
							return roomlist;
						}
					}
				}
			};
			final DropDownChoice<Room> room;
			add(room = new DropDownChoice<Room>("room", new PropertyModel<Room>(model, "room"), roomListModel, new ChoiceRenderer<Room>("room_number", "id")){
				private static final long serialVersionUID = 1L;

				@Override
				protected void onComponentTag(ComponentTag tag) {
					super.onComponentTag(tag);
					tag.put("data-bind", "room" + guest);
				}
			});
			room.setOutputMarkupId(true);
			room.setNullValid(true);
			room.setEnabled(reserve.getStatus().getId() == ReservationStatus.RESERVED);
			
			final HiddenField<Integer> roomtype_id;
			add(roomtype_id = new HiddenField<Integer>("roomtype_id", new PropertyModel<Integer>(model, "roomtype_id")){
				private static final long serialVersionUID = 1L;

				@Override
				protected void onComponentTag(ComponentTag tag) {
					super.onComponentTag(tag);
					tag.put("data-bind", "roomtype_id" + guest);
				}
			});
			roomtype_id.setLabel(new StringResourceModel("hotels.reservation.details.room.type", null));
			roomtype_id.setRequired(true);
			roomtype_id.setOutputMarkupId(true);
			
			roomtype.add(new AjaxFormComponentUpdatingBehavior("onchange") {
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					model.getObject().setRoomtypes_id(null);
					if (model.getObject().getRoomtype() != null && model.getObject().getRoomtype().getId() != null){
						model.getObject().setRoomtypes_id(model.getObject().getRoomtype().getId());
					}
					target.add(roomtype_id);
					target.add(room);
				}
			});
			
			final HiddenField<Long> room_id;
			add(room_id = new HiddenField<Long>("rooms_id", new PropertyModel<Long>(model, "rooms_id")){
				private static final long serialVersionUID = 1L;

				@Override
				protected void onComponentTag(ComponentTag tag) {
					super.onComponentTag(tag);
					tag.put("data-bind", "rooms_id" + guest);
				}
			});
			room_id.setLabel(new StringResourceModel("hotels.reservation.details.room", null));
			room_id.setRequired(true);
			room_id.setOutputMarkupId(true);
			
			room.add(new AjaxFormComponentUpdatingBehavior("onchange") {
				private static final long serialVersionUID = 1L;
				
				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					model.getObject().setRooms_id(null);
					if (model.getObject().getRoom() != null && model.getObject().getRoom().getId() != null){
						model.getObject().setRooms_id(model.getObject().getRoom().getId());
						if (! reserve.isIs_group() && guest == 0){
							roomlist.clear();
							roomlist.add(new Room(model.getObject().getRoom()));
						}
					}
					target.add(room_id);
				}
			});
			
			add(new DropDownChoice<String>("person_title", new PropertyModel<String>(model, "person_title"), new LoadableDetachableModel<List<String>>() {
				private static final long serialVersionUID = 1L;

				@Override
				protected List<String> load() {
					List<String> list = Arrays.asList(new String[]{"Mr.", "Ms.", "Mrs."});
					return list;
				}
			}, new MyChoiceRenderer<String>()).setNullValid(true).add(new AttributeModifier("data-bind", "person_title" + guest)).add(new AjaxOnBlurEvent()));
			
			TextField<String> first_name;
			add(first_name = new TextField<String>("first_name", new PropertyModel<String>(model, "first_name")));
			first_name.setRequired(true);
			first_name.setLabel(new StringResourceModel("hotels.guest.details.guest.name.first", null));
			first_name.add(new AttributeModifier("data-bind", "first_name" + guest));
			first_name.add(new AjaxOnBlurEvent());
			
			TextField<String> last_name;
			add(last_name = new TextField<String>("last_name", new PropertyModel<String>(model, "last_name")));
			last_name.setRequired(true);
			last_name.setLabel(new StringResourceModel("hotels.guest.details.guest.name.last", null));
			last_name.add(new AttributeModifier("data-bind", "last_name" + guest));
			last_name.add(new AjaxOnBlurEvent());
			
			WebMarkupContainer relationship_container;
			add(relationship_container = new WebMarkupContainer("relationship_container"));
			relationship_container.setVisible(model.getObject().getGuest_index() > 1);
			
			TextField<String> relationship;
			relationship_container.add(relationship = new TextField<String>("relationship", new PropertyModel<String>(model, "relationship")));
			relationship.setRequired(relationship_container.isVisible());
			relationship.add(new AttributeModifier("data-bind", "relationship" + guest));
			relationship.setLabel(new StringResourceModel("hotels.guest.details.relationship", null));
			relationship.add(new AjaxOnBlurEvent());

			TextField<String> date_and_place_of_birth;
			add(date_and_place_of_birth = new TextField<String>("date_and_place_of_birth", new PropertyModel<String>(model, "date_and_place_of_birth")));
			date_and_place_of_birth.add(new AttributeModifier("data-bind", "date_and_place_of_birth" + guest));
			date_and_place_of_birth.setLabel(new StringResourceModel("hotels.guest.details.date_and_place_of_birth", null));
			date_and_place_of_birth.add(new AjaxOnBlurEvent());
			
			DropDownChoice<Gender> gender;
			add(gender = new DropDownChoice<Gender>("gender", new PropertyModel<Gender>(model, "gender"), GenderModel.getGenderListModel(), new ChoiceRenderer<Gender>("name", "id")){
				private static final long serialVersionUID = 1L;

				@Override
				protected void onComponentTag(ComponentTag tag) {
					super.onComponentTag(tag);
					tag.put("data-bind", "gender" + guest);
				}
			});
			gender.setRequired(true);
			gender.add(new AttributeModifier("data-bind", "gender" + guest));
			gender.setLabel(new StringResourceModel("hotels.guest.details.gender", null));
			gender.add(new AjaxOnBlurEvent());
			gender.setNullValid(true);
			
			DropDownChoice<Nationality> nationality;
			add(nationality = new DropDownChoice<Nationality>("nationality", new PropertyModel<Nationality>(model, "nationality"), new LoadableDetachableModel<List<Nationality>>() {
				private static final long serialVersionUID = 1L;

				@Override
				protected List<Nationality> load() {
					return new MyBatisHelper().selectList("selectNationalityList", (reserve.isResident() ? "uz" : null));
				}
			}, new ChoiceRenderer<Nationality>("name", "code")));
			nationality.setNullValid(true);
			nationality.setLabel(new StringResourceModel("hotels.guest.details.nationality", null));
			
			nationality.setNullValid(true);
			nationality.add(new AttributeModifier("data-bind", "nationality" + guest));
			nationality.setLabel(new StringResourceModel("hotels.guest.details.nationality", null));
			nationality.add(new AjaxOnBlurEvent());
			
			TextField<String> passport_number;
			add(passport_number = new TextField<String>("passport_number", new PropertyModel<String>(model, "passport_number")));
			passport_number.setRequired(true);
			passport_number.add(new AttributeModifier("data-bind", "passport_number" + guest));
			passport_number.setLabel(new StringResourceModel("hotels.guest.details.passport.number", null));
			passport_number.add(new AjaxOnBlurEvent());
			
			DateTextField passport_date_of_issue;
			add(passport_date_of_issue = new DateTextField("passport_date_of_issue", new PropertyModel<Date>(model, "passport_date_of_issue"), new PatternDateConverter(MyWebApplication.DATE_FORMAT, false)));
			passport_date_of_issue.add(new MyDatePicker());
			passport_date_of_issue.add(new AttributeModifier("data-date-format", MyWebApplication.DATE_FORMAT));
			passport_date_of_issue.add(new AttributeModifier("data-bind", "passport_date_of_issue" + guest));
			passport_date_of_issue.setLabel(new StringResourceModel("hotels.guest.details.passport.date_of_issue", null));

			TextField<String> passport_issue_place;
			add(passport_issue_place = new TextField<String>("passport_issue_place", new PropertyModel<String>(model, "passport_issue_place")));
			passport_issue_place.add(new AttributeModifier("data-bind", "passport_issue_place" + guest));
			passport_issue_place.setLabel(new StringResourceModel("hotels.guest.details.passport.issue_place", null));
			passport_issue_place.add(new AjaxOnBlurEvent());
						
			TextField<String> postal_index;
			add(postal_index = new TextField<String>("postal_index", new PropertyModel<String>(model, "postal_index")));
			postal_index.add(new AttributeModifier("data-bind", "postal_index" + guest));
			postal_index.setLabel(new StringResourceModel("hotels.details.postal_index", null));
			postal_index.add(new AjaxOnBlurEvent());
			
			TextField<String> address;
			add(address = new TextField<String>("address", new PropertyModel<String>(model, "address")));
			address.add(new AttributeModifier("data-bind", "address" + guest));
			address.setLabel(new StringResourceModel("hotels.guest.details.address", null));
			address.add(new AjaxOnBlurEvent());
			
			DropDownChoice<Country> country;
			add(country = new DropDownChoice<Country>("country", new PropertyModel<Country>(model, "country"), HotelModels.getCountriesList(), new ChoiceRenderer<Country>("name", "id")){
				private static final long serialVersionUID = 1L;

				@Override
				protected void onComponentTag(ComponentTag tag) {
					super.onComponentTag(tag);
					tag.put("data-bind", "country" + guest);
				}
			});
			country.setRequired(true);
			country.setNullValid(true);
			country.setLabel(new StringResourceModel("hotels.guest.details.country", null));
			
			final LoadableDetachableModel<List<? extends Region>> regionsList = new LoadableDetachableModel<List<? extends Region>>() {
				private static final long serialVersionUID = 1L;

				@Override
				protected List<? extends Region> load() {
					Country country = model.getObject().getCountry();
					Map<String, Serializable> params = new HashMap<String, Serializable>();
					params.put("countries_id", country != null ? country.getId() : -1);
					return new MyBatisHelper().selectList("selectRegionsList", params);
				}
			};
			
			final DropDownChoice<Region> region;
			add(region = new DropDownChoice<Region>("region", new PropertyModel<Region>(model, "region"), regionsList, new ChoiceRenderer<Region>("name", "id")){
				private static final long serialVersionUID = 1L;

				@Override
				protected void onComponentTag(ComponentTag tag) {
					super.onComponentTag(tag);
					tag.put("data-bind", "region" + guest);
				}
			});
			region.setOutputMarkupId(true);
			region.setLabel(new StringResourceModel("hotels.guest.details.region", null));
			region.add(new AjaxOnBlurEvent());
			region.setNullValid(true);
			
			country.add(new AjaxFormComponentUpdatingBehavior("onchange") {
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					target.add(region);
				}
			});
			
			TextField<String> city;
			add(city = new TextField<String>("city", new PropertyModel<String>(model, "city")));
			city.add(new AttributeModifier("data-bind", "city" + guest));
			city.setLabel(new StringResourceModel("hotels.guest.details.city", null));
			city.add(new AjaxOnBlurEvent());
			
			EmailTextField email;
			add(email = new EmailTextField("email", new PropertyModel<String>(model, "email")));
			email.add(new AttributeModifier("data-bind", "email" + guest));
			email.setLabel(new StringResourceModel("hotels.guest.details.email", null));
			email.add(new AjaxOnBlurEvent());
			
			TextField<String> company;
			add(company = new TextField<String>("company", new PropertyModel<String>(model, "company")));
			company.add(new AttributeModifier("data-bind", "company" + guest));
			company.setLabel(new StringResourceModel("hotels.guest.details.company", null));
			company.add(new AjaxOnBlurEvent());
			
			TextField<String> purpose_of_arrival;
			add(purpose_of_arrival = new TextField<String>("purpose_of_arrival", new PropertyModel<String>(model, "purpose_of_arrival")));
			purpose_of_arrival.add(new AttributeModifier("data-bind", "purpose_of_arrival" + guest));
			purpose_of_arrival.setLabel(new StringResourceModel("hotels.guest.details.purpose_of_arrival", null));
			purpose_of_arrival.add(new AjaxOnBlurEvent());
			
			DropDownChoice<String> visa_type;
			add(visa_type = new DropDownChoice<String>("visa_type", new PropertyModel<String>(model, "visa_type"), new LoadableDetachableModel<List<String>>() {
				private static final long serialVersionUID = 1L;

				@Override
				protected List<String> load() {
					return new MyBatisHelper().selectList("selectVisaTypeList");
				}
			}, new MyChoiceRenderer<String>()));
			visa_type.setLabel(new StringResourceModel("hotels.guest.details.visa.type", null));
			visa_type.add(new AttributeModifier("data-bind", "visa_type" + guest));
			visa_type.add(new AjaxOnBlurEvent());
			visa_type.setNullValid(true);

			TextField<String> visa_number;
			add(visa_number = new TextField<String>("visa_number", new PropertyModel<String>(model, "visa_number")));
			visa_number.setLabel(new StringResourceModel("hotels.guest.details.visa.number", null));
			visa_number.add(new AttributeModifier("data-bind", "visa_number" + guest));
			visa_number.add(new AjaxOnBlurEvent());
			
			DateTextField visa_valid_from;
			add(visa_valid_from = new DateTextField("visa_valid_from", new PropertyModel<Date>(model, "visa_valid_from"), new PatternDateConverter(MyWebApplication.DATE_FORMAT, false)));
			visa_valid_from.add(new MyDatePicker());
			visa_valid_from.add(new AttributeModifier("data-bind", "visa_valid_from" + guest));
			visa_valid_from.add(new AttributeModifier("data-date-format", MyWebApplication.DATE_FORMAT));
			visa_valid_from.setLabel(new StringResourceModel("hotels.guest.details.visa.valid.from", null));
			visa_valid_from.add(new AjaxOnBlurEvent());

			DateTextField visa_valid_to;
			add(visa_valid_to = new DateTextField("visa_valid_to", new PropertyModel<Date>(model, "visa_valid_to"), new PatternDateConverter(MyWebApplication.DATE_FORMAT, false)));
			visa_valid_to.add(new MyDatePicker());
			visa_valid_to.setLabel(new StringResourceModel("hotels.guest.details.visa.valid.till", null));
			visa_valid_to.add(new AttributeModifier("data-bind", "visa_valid_to" + guest));
			visa_valid_to.add(new AttributeModifier("data-date-format", MyWebApplication.DATE_FORMAT));
			visa_valid_to.add(new AjaxOnBlurEvent());
			
			TextField<String> source_of_reservation;
			add(source_of_reservation = new TextField<String>("source_of_reservation", new PropertyModel<String>(model, "source_of_reservation")));
			source_of_reservation.add(new AttributeModifier("data-bind", "source_of_reservation" + guest));
			source_of_reservation.setLabel(new StringResourceModel("hotels.guest.details.source", null));
			source_of_reservation.add(new AjaxOnBlurEvent());
			
			TextField<String> occupation;
			add(occupation = new TextField<String>("occupation", new PropertyModel<String>(model, "occupation")));
			occupation.add(new AttributeModifier("data-bind", "occupation" + guest));
			occupation.setLabel(new StringResourceModel("hotels.guest.details.occupation", null));
			occupation.add(new AjaxOnBlurEvent());
			CommonUtil.setFormComponentRequiredPanel(this);
		}
	}
	
	private class ChildData extends Panel {
		private static final long serialVersionUID = 1L;

		public ChildData(String id, IModel<Child> model, final byte child) {
			super(id, model);
			add(new Label("child", new AbstractReadOnlyModel<String>() {
				private static final long serialVersionUID = 1L;

				@Override
				public String getObject() {
					return getString("hotels.guest.details.child") + " #" + String.valueOf(child + 1);
				}
			}));
			
			TextField<String> name;
			add(name = new TextField<String>("name", new PropertyModel<String>(model, "name")));
			name.setRequired(true);
			name.add(new AjaxOnBlurEvent());
			name.add(new AttributeModifier("data-bind", "child_name" + child));
			name.setLabel(new StringResourceModel("hotels.guest.details.child.name", null));
			
			DateTextField date_of_birth;
			add(date_of_birth = new DateTextField("date_of_birth", new PropertyModel<Date>(model, "date_of_birth"), new PatternDateConverter(MyWebApplication.DATE_FORMAT, false)));
			date_of_birth.add(new MyDatePicker());
			date_of_birth.setRequired(true);
			date_of_birth.add(new AjaxOnBlurEvent());
			date_of_birth.add(new AttributeModifier("data-bind", "child_birthday" + child));
			date_of_birth.setLabel(new StringResourceModel("hotels.guest.details.child.date_of_birth", null));
			CommonUtil.setFormComponentRequiredPanel(this);
		}
	}

	@Override
	public Class<?> implementedClass() {
		return this.getClass();
	}

	@Override
	public IModel<String> getTitle() {
		return new StringResourceModel("hotels.reservation.guest.details", null);
	}
}
