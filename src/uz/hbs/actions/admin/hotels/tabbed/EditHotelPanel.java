package uz.hbs.actions.admin.hotels.tabbed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.actions.admin.AdminHomePage;
import uz.hbs.beans.Address;
import uz.hbs.beans.Condition;
import uz.hbs.beans.Contract;
import uz.hbs.beans.Equipment;
import uz.hbs.beans.Facility;
import uz.hbs.beans.Hotel;
import uz.hbs.beans.HotelDetail;
import uz.hbs.beans.HotelNearByPlace;
import uz.hbs.beans.IdAndValue;
import uz.hbs.beans.Image;
import uz.hbs.beans.MealOption;
import uz.hbs.beans.ReferenceInfo;
import uz.hbs.beans.ReservationCancellationPolicy;
import uz.hbs.beans.ReservationRule;
import uz.hbs.beans.ReservationRuleType;
import uz.hbs.beans.Room;
import uz.hbs.beans.RoomSetup;
import uz.hbs.beans.RoomType;
import uz.hbs.beans.Service;
import uz.hbs.beans.User;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;
import uz.hbs.markup.html.tabs.BootstrapAjaxTabbedPanel;
import uz.hbs.session.MySession;
import uz.hbs.utils.HotelUtil;
import uz.hbs.utils.models.RoomSetupModel;

public class EditHotelPanel extends MyBreadCrumbPanel {
	private static final long serialVersionUID = 1L;
	private Hotel hotel;
	
	public EditHotelPanel(String id, IBreadCrumbModel breadCrumbModel, long hotelsusers_id) {
		this(id, breadCrumbModel, (Hotel) new MyBatisHelper().selectOne("selectHotelByUserId", hotelsusers_id));
	}
	
	public EditHotelPanel(String id, IBreadCrumbModel breadCrumbModel, final Hotel hotel) {
		super(id, breadCrumbModel);
		
		this.hotel = hotel;
		
		final CompoundPropertyModel<Hotel> model = new CompoundPropertyModel<Hotel>(hotel);
		
		setDefaultModel(model);
		List<ITab> tabList = new ArrayList<ITab>();
		tabList.add(new AbstractTab(new StringResourceModel("hotels.title.account_details", null)) {
			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String componentId) {
				hotel.setStep(Hotel.HOTEL_ACCOUNT_DETAILS);
				
				Contract contract = new MyBatisHelper().selectOne("selectContract", hotel.getUsers_id());
				if (contract != null) {
					model.getObject().setContract(contract);
				}
				
				return new AccountDetailsTabbedPanel(componentId, model) {
					private static final long serialVersionUID = 1L;

					@Override
					protected void onSave(AjaxRequestTarget target, Form<?> form, MyFeedbackPanel feedback) {
						SqlSession sql = MyBatisHelper.getSqlSessionFactory().openSession(false);
						try {
							Hotel hotel = (Hotel) form.getDefaultModelObject();
							try {
								sql.update("updateHotel", hotel);
								logger.debug("Step #1: Hotel updated=" + hotel.getDisplay_name());
								sql.update("updateLegalName", new User(hotel.getUsers_id(), hotel.getLegal_name()));
								Contract contract = hotel.getContract();
								contract.setUsers_id(hotel.getUsers_id());
								if (sql.update("updateContract", contract) == 0) {
									sql.update("insertContract", contract);
									logger.debug("Step #2: Contract updated=" + contract);
								} else {
									logger.debug("Step #2: Contract updated=" + contract);
								}
								sql.commit();
								feedback.success(getString("hotels.account_details.update.success"));
							} catch (Exception e) {
								sql.rollback();
								feedback.error(getString("hotels.account_details.update.fail"));
								logger.error("Exception", e);
							} finally {
								sql.close();
							}
						} finally {
							target.add(feedback);
						}
					}
					
					@Override
					protected void onNext(AjaxRequestTarget target, Form<?> form, MyFeedbackPanel feedback) {
					}

					@Override
					protected boolean isVisibleNextButton() {
						return false;
					}
				};
			}
			
			@Override
			public boolean isVisible() {
				return hotel.getStep() >= Hotel.HOTEL_ACCOUNT_DETAILS; 
			}
		});
		tabList.add(new AbstractTab(new StringResourceModel("hotels.title.hotel_details", null)) {
			private static final long serialVersionUID = 1L;
			private HotelDetail details;
			
			@Override
			public WebMarkupContainer getPanel(String componentId) {
				details = new MyBatisHelper().selectOne("selectHotelDetailsByHotelId", hotel.getUsers_id());
				if (details != null) {
					hotel.setHotelsDetails(details);
					Address address = new MyBatisHelper().selectOne("selectAddressById", details.getAddresses_id());
					if (address != null) details.setAddress(address);
					List<Facility> facilities = new MyBatisHelper().selectList("selectHotelFacilities", hotel.getUsers_id());
					details.setFacilities(facilities);
					List<Service> services = new MyBatisHelper().selectList("selectHotelRoomService", hotel.getUsers_id());
					details.getService().addAll(services);
					List<HotelNearByPlace> places = new MyBatisHelper().selectList("selectHotelNearByPlacesByHotelId", hotel.getUsers_id());
					details.setHotelsnearbyplaces(places);
				}
				hotel.setStep(Hotel.HOTEL_DETAILS);
				
				return new HotelDetailsTabberPanel(componentId, model) {
					private static final long serialVersionUID = 1L;
					
					@Override
					protected void onSave(AjaxRequestTarget target, Form<?> form, MyFeedbackPanel feedback) {
						HotelDetail model = (HotelDetail) form.getDefaultModelObject();
						try {
							SqlSession sql = MyBatisHelper.getSqlSessionFactory().openSession(false);
								try {
									if (details != null){
										model.getAddress().setInitiator_user_id((((MySession) getSession()).getUser().getId()));
										model.setHotelsusers_id(hotel.getUsers_id());
										
										hotel.setHotelscategories_id(model.getHotelCategory().getId());
										sql.update("updateHotel", hotel);
										
										sql.update("updateAddresses", model.getAddress());
										
										sql.delete("deleteHotelFacilities", hotel.getUsers_id());
										for (Facility facility : model.getFacilities()) {
											facility.setHotelsusers_id(hotel.getUsers_id());
											sql.insert("insertHotelFacilities", facility);
										}
										sql.delete("deleteHotelsServices", hotel.getUsers_id());
										for (Service service : model.getService()) {
											service.setHotelsusers_id(hotel.getUsers_id());
											sql.insert("insertHotelServices", service);
										}
										sql.delete("deleteHotelsNearByPlaces", hotel.getUsers_id());
										for (HotelNearByPlace place : model.getHotelsnearbyplaces()) {
											place.setHotelsusers_id(hotel.getUsers_id());
											place.setInitiator_user_id((((MySession) getSession()).getUser().getId()));
											sql.insert("insertHotelsNearByPlaces", place);
										}
										sql.insert("updateHotelDetails", model);
										HotelUtil.saveImage(sql, details.getHotelImagesfield(), hotel.getUsers_id(), true, hotel.getUsers_id());
										sql.commit();
										feedback.success(getString("hotels.details.update.success"));
									} else {
										HotelUtil.addHotelDetails(sql, model, hotel, (MySession) getSession());
										feedback.success(getString("hotels.details.insert.success"));
									}	
								} catch (Exception e) {
									logger.error("Exception", e);
									feedback.error(getString("hotels.details.update.fail"));
									sql.rollback();
								} finally {
									sql.close();
								}
						} finally {
							target.add(feedback);
						}
					}

					@Override
					protected void onNext(AjaxRequestTarget target, Form<?> form, MyFeedbackPanel feedback) {
					}

					@Override
					protected boolean isVisibleNextButton() {
						return false;
					}
				};
			}
		});
		tabList.add(new AbstractTab(new StringResourceModel("hotels.title.reservation_rules", null)) {
			private static final long serialVersionUID = 1L;
			private ReservationRule rules;
			
			@Override
			public WebMarkupContainer getPanel(String componentId) {
				HashMap<String, Object> param = new HashMap<String, Object>();
				param.put("hotelsusers_id", model.getObject().getUsers_id());
				param.put("is_group", false);
				
				ReservationRuleType individual = new MyBatisHelper().selectOne("selectReservationRulesByHotelsId", param);
				
				rules = new ReservationRule();
				
				if (individual != null) {
					individual.setReservationcancellationpolicy((ReservationCancellationPolicy) new MyBatisHelper().selectOne("selectReservationCancellationPolicyById", individual.getReservationcancellationpolicy().getId()));
					rules.setIndividual(individual);
				} else {
					individual = new ReservationRuleType(false);
					individual.setHotelsusers_id(model.getObject().getUsers_id());
					rules.setIndividual(individual);
				}
				
				param.put("is_group", true);
				
				ReservationRuleType group = new MyBatisHelper().selectOne("selectReservationRulesByHotelsId", param);
				
				if (group != null) {
					group.setReservationcancellationpolicy((ReservationCancellationPolicy) new MyBatisHelper().selectOne("selectReservationCancellationPolicyById", group.getReservationcancellationpolicy().getId()));
					rules.setGroup(group);
				} else {
					group = new ReservationRuleType(true);
					group.setHotelsusers_id(model.getObject().getUsers_id());
					rules.setIndividual(group);
				}
				
				hotel.setReservationRules(rules);
				hotel.setStep(Hotel.RESERVATION_RULES);
				
				return new ReservationRulesTabbedPanel(componentId, model) {
					private static final long serialVersionUID = 1L;
					
					@Override
					protected void onSave(AjaxRequestTarget target, Form<?> form, MyFeedbackPanel feedback) {
						ReservationRule model = (ReservationRule) form.getDefaultModelObject();
						try {
							SqlSession sql = MyBatisHelper.getSqlSessionFactory().openSession(false);
							try {
								if (rules != null) {
									//************************ INDIVIDUAL **********************************************//
									ReservationRuleType reserve_rule = model.getIndividual();
									ReservationCancellationPolicy policy = reserve_rule.getReservationcancellationpolicy();

									if (! policy.isSupport_tentative_reservation_warn()) {
										policy.setNotify_ta_before_days(new IdAndValue(0));
									}

									if (sql.update("updateReservationCancellationPolicy", policy) == 0) {
										sql.insert("insertReservationCancellationPolicy", policy);
									}

									reserve_rule.setHotelsusers_id(hotel.getUsers_id());
									
									if (sql.update("updateReservationRules", reserve_rule) == 0) {
										sql.insert("insertReservationRules", reserve_rule);
									}

									//************************ GROUP **********************************************//									
									reserve_rule = model.getGroup();
									
									policy = reserve_rule.getReservationcancellationpolicy();

									if (! policy.isSupport_tentative_reservation_warn()) {
										policy.setNotify_ta_before_days(new IdAndValue(0));
									}

									if (sql.update("updateReservationCancellationPolicy", policy) == 0) {
										sql.insert("insertReservationCancellationPolicy", policy);
									}

									reserve_rule.setHotelsusers_id(hotel.getUsers_id());
									
									if (sql.update("updateReservationRules", reserve_rule) == 0) {
										sql.insert("insertReservationRules", reserve_rule);
									}

									feedback.success(getString("hotels.reservation.update.success"));
									
									sql.commit();
								} else {
									HotelUtil.addReservationRules(sql, model, hotel);
									feedback.success(getString("hotels.reservation.insert.success"));
								}
							} catch (Exception e) {
								logger.error("Exception", e);
								sql.rollback();
								feedback.error(getString("hotels.reservation.update.fail"));
							} finally {
								sql.close();
							}
						} finally {
							target.add(feedback);
						}
					}
					
					@Override
					protected void onNext(AjaxRequestTarget target, Form<?> form, MyFeedbackPanel feedback) {
					}

					@Override
					protected boolean isVisibleNextButton() {
						return false;
					}
				};
			}
		});
		tabList.add(new AbstractTab(new StringResourceModel("hotels.title.reference_info", null)) {
			private static final long serialVersionUID = 1L;
			
			@Override
			public WebMarkupContainer getPanel(String componentId) {
				ReferenceInfo info = new ReferenceInfo();
				info.setSelf_payment_possibility((byte) (hotel.getSelf_payment_possibility() == false ? 0 : 1));
				
				List<RoomType> roomtypelist = new MyBatisHelper().selectList("selectRoomTypesByHotel", hotel.getUsers_id());
				
				info.setRoom_types(roomtypelist);
				info.setFloors(hotel.getFloors());
				info.setRooms(hotel.getRooms());
				
				List<Condition> conditionlist = new MyBatisHelper().selectList("selectConditionsByHotelId", hotel.getUsers_id());
				info.setConditions(conditionlist);
				
				MealOption meal_options = new MealOption();
				
				MealOption temp = new MyBatisHelper().selectOne("selectMealOptions", new MealOption(MealOption.BREAKFAST, hotel.getUsers_id()));
				if (temp != null){
					meal_options.setBreakfast(true);
					meal_options.setBreakfast_included_to_room_rate(temp.isIncluded_to_room_rate());
					meal_options.setBreakfast_per_person_per_night(temp.getCost_per_person_per_night());
				}
				
				temp = new MyBatisHelper().selectOne("selectMealOptions", new MealOption(MealOption.LUNCH, hotel.getUsers_id()));
				if (temp != null){
					meal_options.setLunch(true);
					meal_options.setLunch_included_to_room_rate(temp.isIncluded_to_room_rate());
					meal_options.setLunch_per_person_per_night(temp.getCost_per_person_per_night());
				}
				
				temp = new MyBatisHelper().selectOne("selectMealOptions", new MealOption(MealOption.DINNER, hotel.getUsers_id()));
				if (temp != null){
					meal_options.setDinner(true);
					meal_options.setDinner_included_to_room_rate(temp.isIncluded_to_room_rate());
					meal_options.setDinner_per_person_per_night(temp.getCost_per_person_per_night());
				}
				
				info.setMeal_options(meal_options);
				
				hotel.setReferenceInfo(info);
				hotel.setStep(Hotel.REFERENCE_INFO);
				
				return new ReferenceInfoTabbedPanel(componentId, model) {
					private static final long serialVersionUID = 1L;
					
					@Override
					protected void onSave(AjaxRequestTarget target, Form<?> form, MyFeedbackPanel feedback) {
						ReferenceInfo model = (ReferenceInfo) form.getDefaultModelObject();
						try {
							if (model.getRoom_types() == null || model.getRoom_types().isEmpty()) {
								feedback.error("Room Types is required");
								return;
							}
							SqlSession sql = MyBatisHelper.getSqlSessionFactory().openSession(false);
							try {
								Hotel hotelSelfPaymentPossibility = new Hotel();
								hotelSelfPaymentPossibility.setUsers_id(hotel.getUsers_id());
								hotelSelfPaymentPossibility.setSelf_payment_possibility(model.getSelf_payment_possibility() == 0 ? false : true);
								
								sql.update("updateHotel", hotelSelfPaymentPossibility);

								for (Condition condition : model.getConditions()) {
									if (condition.getId() == null){
										condition.setHotelsusers_id(hotel.getUsers_id());
										sql.insert("insertConditions", condition);
									}
								}

								sql.delete("deleteHotelsEquipments", hotel.getUsers_id());
								for (Equipment equipment: model.getEquipments()) {
									equipment.setHotelsusers_id(hotel.getUsers_id());
									sql.insert("insertHotelsEquipments", equipment);
								}
								
								for (RoomType roomtype : model.getRoom_types()) {
									if (roomtype.getId() == null){
										roomtype.setStatus(RoomType.STATUS_ACTIVE);
										roomtype.setInitiator_user_id(((MySession) getSession()).getUser().getId());
										sql.insert("insertRoomType", roomtype);
										roomtype.setHotelsusers_id(hotel.getUsers_id());
										sql.insert("insertHotelRoomTypes", roomtype);
									} else {
										sql.update("updateHotelRoomTypes", roomtype);
									}
								}

								hotel.setFloors(model.getFloors());
								hotel.setRooms(model.getRooms());
								sql.update("updateHotelsFloorsAndRooms", hotel);
								
								MealOption meal_options = model.getMeal_options(); 

								if (meal_options.isBreakfast()) {
									meal_options.setMeal_type(MealOption.BREAKFAST);
									meal_options.setIncluded_to_room_rate(meal_options.isBreakfast_included_to_room_rate());
									meal_options.setHotelsusers_id(hotel.getUsers_id());
									meal_options.setCost_per_person_per_night(meal_options.getBreakfast_per_person_per_night());
									if (sql.update("updateMealOptions", meal_options) == 0)
									sql.insert("insertMealOptions", meal_options);
								} else {
									meal_options.setMeal_type(MealOption.BREAKFAST);
									meal_options.setHotelsusers_id(hotel.getUsers_id());
									sql.delete("deleteMealOptions", meal_options);
									logger.info("Meal Option (Breakfast) is deleted, User Id = " + ((MySession) getSession()).getUser().getId());
								}
								if (meal_options.isLunch()) {
									meal_options.setMeal_type(MealOption.LUNCH);
									meal_options.setIncluded_to_room_rate(meal_options.isLunch_included_to_room_rate());
									meal_options.setHotelsusers_id(hotel.getUsers_id());
									meal_options.setCost_per_person_per_night(meal_options.getLunch_per_person_per_night());
									if (sql.update("updateMealOptions", meal_options) == 0)
									sql.insert("insertMealOptions", meal_options);
								} else {
									meal_options.setMeal_type(MealOption.LUNCH);
									meal_options.setHotelsusers_id(hotel.getUsers_id());
									sql.delete("deleteMealOptions", meal_options);
									logger.info("Meal Option (Lunch) is deleted, User Id = " + ((MySession) getSession()).getUser().getId());
								}
								if (meal_options.isDinner()) {
									meal_options.setMeal_type(MealOption.DINNER);
									meal_options.setIncluded_to_room_rate(meal_options.isDinner_included_to_room_rate());
									meal_options.setHotelsusers_id(hotel.getUsers_id());
									meal_options.setCost_per_person_per_night(meal_options.getDinner_per_person_per_night());
									if (sql.update("updateMealOptions", meal_options) == 0)
									sql.insert("insertMealOptions", meal_options);
								} else {
									meal_options.setMeal_type(MealOption.DINNER);
									meal_options.setHotelsusers_id(hotel.getUsers_id());
									sql.delete("deleteMealOptions", meal_options);
									logger.info("Meal Option (Dinner) is deleted, User Id = " + ((MySession) getSession()).getUser().getId());
								}
								sql.commit();
								feedback.success(getString("hotels.title.reference_info.update.success"));
							} catch (Exception e) {
								logger.error("Exception", e);
								sql.rollback();
								feedback.error(getString("hotels.title.reference_info.update.fail"));
							} finally {
								sql.close();
							}
						} finally {
							target.add(feedback);
						}
					}

					@Override
					protected void onNext(AjaxRequestTarget target, Form<?> form, MyFeedbackPanel feedback) {
					}

					@Override
					protected boolean isVisibleNextButton() {
						return false;
					}
				};
			}
		});
		tabList.add(new AbstractTab(new StringResourceModel("hotels.title.room_setup", null)) {
			private static final long serialVersionUID = 1L;
			RoomSetup setup;
			@Override
			public WebMarkupContainer getPanel(String componentId) {
				List<RoomType> roomtypelist = new MyBatisHelper().selectList("selectRoomTypesByHotel", hotel.getUsers_id());
				HashMap<String, Object> param = new HashMap<String, Object>();
				param.put("hotelsusers_id", hotel.getUsers_id());
				if (! roomtypelist.isEmpty()) setup = new RoomSetup(); 
				
				for (RoomType roomtype : roomtypelist){
					param.put("roomtypes_id", roomtype.getId());
					List<Equipment> equipments = new MyBatisHelper().selectList("selectEquipmentListByRoomTypeHotel", param);
					List<Condition> conditions = new MyBatisHelper().selectList("selectConditionsListByRoomTypeHotel", param);
					List<Room> rooms = new MyBatisHelper().selectList("selectHotelRoomTypesRoomsByHotels", param);
					if (rooms.isEmpty()){
						for (int i = 1; i <= roomtype.getNumber_of_rooms(); i++){
							rooms.add(new Room());
						}
					} else if (! rooms.isEmpty() && rooms.size() != roomtype.getNumber_of_rooms()){
						if (rooms.size() < roomtype.getNumber_of_rooms()){
							int count = roomtype.getNumber_of_rooms() - rooms.size();
							for (int i = 1; i <= count; i++){
								rooms.add(new Room());
							}
						} else {
							for (int i = 1; i <= roomtype.getNumber_of_rooms(); i++){
								rooms.add(new Room());
							}
						}
					}
					List<Image> images = Collections.emptyList();
					setup.addMap(roomtype.getId(), new RoomType(roomtype, equipments, conditions, images, rooms, hotel.getUsers_id()));
				}
				hotel.setRoomSetup(setup);
				
				ReferenceInfo info = hotel.getReferenceInfo();
				if (info == null) info = new ReferenceInfo();
				info.setRoom_types(roomtypelist);
				info.setEquipments(RoomSetupModel.getAvailableEquipments());
				info.setFloors(hotel.getFloors());
				hotel.setReferenceInfo(info);
						
				hotel.setStep(Hotel.ROOM_SETUP);
				return new RoomSetupTabbedPanel(componentId, model) {
					private static final long serialVersionUID = 1L;

					@Override
					protected boolean isVisibleNextButton() {
						return false;
					}

					@Override
					protected void onSave(AjaxRequestTarget target, RoomSetup setup, MyFeedbackPanel feedback) {
						setResponsePage(new AdminHomePage(AdminHomePage.HOTEL_LIST));
					}

					@Override
					protected void onNext(AjaxRequestTarget target, RoomSetup setup, MyFeedbackPanel feedback) {
						setResponsePage(new AdminHomePage(AdminHomePage.HOTEL_LIST));
					}

					@Override
					protected boolean isVisibleSaveButton() {
						return false;
					}
				};
			}
		});
		add(new BootstrapAjaxTabbedPanel<ITab>("tabbed", tabList));
	}
	
	@Override
	protected void onAfterRender() {
		super.onAfterRender();
	}


	@Override
	public Class<?> implementedClass() {
		return this.getClass();
	}

	@Override
	public IModel<String> getTitle() {
		return new StringResourceModel("hotels.edit.title", new Model<Hotel>(hotel));
	}
}
