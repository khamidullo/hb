package uz.hbs.actions.touragent.newbooking;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.MaskType;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uz.hbs.actions.touragent.TourAgentPage;
import uz.hbs.actions.touragent.newbooking.group.TAGroupReservationPanel;
import uz.hbs.actions.touragent.newbooking.individual.TAIndividualReservationPanel;
import uz.hbs.actions.touragent.newbooking.panel.RoomTypeEquipmentPanel;
import uz.hbs.beans.BedType;
import uz.hbs.beans.Booking;
import uz.hbs.beans.Booking.Age;
import uz.hbs.beans.Booking.RoomsCount;
import uz.hbs.beans.BookingSearchResult;
import uz.hbs.beans.ChildAge;
import uz.hbs.beans.Facility;
import uz.hbs.beans.GroupReservation;
import uz.hbs.beans.Hotel;
import uz.hbs.beans.HotelNearByPlace;
import uz.hbs.beans.HotelStar;
import uz.hbs.beans.IndividualReservation;
import uz.hbs.beans.KeyAndValue;
import uz.hbs.beans.MealOption;
import uz.hbs.beans.PriceType;
import uz.hbs.beans.ReservationCancellationPolicy;
import uz.hbs.beans.ReservationRoom;
import uz.hbs.beans.ReservationRuleType;
import uz.hbs.beans.RoomType;
import uz.hbs.beans.Service;
import uz.hbs.components.panels.slider.ImageSliderPanel;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;
import uz.hbs.session.MySession;
import uz.hbs.utils.CommonUtil;
import uz.hbs.utils.DateUtil;
import uz.hbs.utils.FormatUtil;
import uz.hbs.utils.HotelUtil;
import uz.hbs.utils.ReserveUtil;
import uz.hbs.utils.models.HotelModels;

public class BookingSelectedHotel extends MyBreadCrumbPanel {
	private static final long serialVersionUID = 1L;
	private static final Logger _log = LoggerFactory.getLogger(BookingSelectedHotel.class);
	private String title;
	private int rowId = -1;
	private int rowspan = 1;
	private int roomIndex = 1;
	private WebMarkupContainer wmc;
	private List<BookingSearchResult> availableRoomsList = new ArrayList<BookingSearchResult>();
	@SuppressWarnings("unused")
	private BigDecimal checkInEarly;
	@SuppressWarnings("unused")
	private BigDecimal checkOutLate; 


	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		rowId = -1;
		rowspan = 1;
		roomIndex = 1;
	}

	@Override
	protected void onAfterRender() {
		super.onAfterRender();
		rowId = -1;
		rowspan = 1;
		roomIndex = 1;
	}
	
	public BookingSelectedHotel(String id, IBreadCrumbModel breadCrumbModel, final BookingSearchResult modelObject, final Hotel hotel, final Booking filter) {
		super(id, breadCrumbModel);

		this.title = hotel.getDisplay_name();

		add(new Label("hotelName", hotel.getDisplay_name()));

		Label starLabel = new Label("starLabel", HotelUtil.starDecorator(hotel.getHotelstars_id(), HotelStar.MAX_STARS, true));
		starLabel.setEscapeModelStrings(false);
		add(starLabel);

		add(new Label("address", hotel.getAddress()));
		add(new Label("city", hotel.getCity()));
		add(new Label("region_name", hotel.getRegion_name()).setVisible(false));
		add(new Label("country_name", hotel.getCountry_name()).setVisible(false));
		add(new Label("contact_number", hotel.getHotelsDetails() != null ? hotel.getHotelsDetails().getContact_number() : ""));

		add(new Label("checkIn", FormatUtil.toString(filter.getFromDate(), "dd/MM/yyyy") + " " + filter.getFromTime()));
		add(new Label("checkOut", FormatUtil.toString(filter.getToDate(), "dd/MM/yyyy") + " " + filter.getToTime()));
		add(new Label("nightsLabel", filter.getNights()));
		add(new Label("nightsSuffixLabel", new StringResourceModel("touragents.newbooking.nights." + (filter.getNights() > 5 ? 5 : filter.getNights()), null)));
		add(new Label("ageLabel", filter.getAge_label()));

//		add(new Label("adults", filter.getAdults()));
//		add(new Label("children", filter.getChildren()));

		Link<Void> newBookingLink = new Link<Void>("newBookingLink") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new TourAgentPage(TourAgentPage.NEW_BOOKING, false, null, null));
			}
		};
		add(newBookingLink);
		
		/******* BEGIN Image Slider *******/
		final Map<String, Object> param = new HashMap<String, Object>();
		param.put("hotelsusers_id", modelObject.getHotelsusers_id());
		// params.put("limit", 1);
		List<KeyAndValue> imgLinkList = new MyBatisHelper().selectList("selectHotelUploadedFiles", param);

		add(new ImageSliderPanel("imageSlider", imgLinkList));
		/******* END Image Slider *******/

		/*
		 * First tab
		 */

		final Form<Void> form = new Form<Void>("form");
		add(form);
		form.setOutputMarkupId(true);

		final ModalWindow modal = new ModalWindow("dialog");
		modal.setInitialHeight(600);
		modal.setInitialWidth(800);
		modal.setMinimalHeight(600);
		modal.setMinimalWidth(800);
		modal.setAutoSize(true);
		modal.setMaskType(MaskType.SEMI_TRANSPARENT);
		modal.setCssClassName(ModalWindow.CSS_CLASS_GRAY);

		logger.debug("BEGIN");

		String roomAdultsChildParams = "", childAges = "", childAgesForDisplay = "";
		for (RoomsCount roomsCount : filter.getRoomsCountsList()) {
			roomAdultsChildParams += roomAdultsChildParams.isEmpty() ? roomsCount.getAdults() : "," + roomsCount.getAdults();
			childAges = "";
			for (Age age : roomsCount.getAgesList()) {
				childAges += childAges.isEmpty() ? age.getAge().toString() : ";" + age.getAge().toString();
				childAgesForDisplay += childAgesForDisplay.isEmpty() ? age.getAge().toString() : ", " + age.getAge().toString();
			}
			if (!childAges.isEmpty()) {
				roomAdultsChildParams += ":" + childAges;
			}
		}

		param.clear();
		param.put("params", roomAdultsChildParams);
		param.put("check_in", DateUtil.getDate(filter.getFromDate(), filter.getFromTime()));
		param.put("check_out", DateUtil.getDate(filter.getToDate(), filter.getToTime()));
		param.put("region_id", filter.getRegion().getId());
		param.put("resident", filter.isResident());
		param.put("range_from", filter.getRange_from_value());
		param.put("range_to", filter.getRange_to_value());
		param.put("facilityIdList", filter.getRoomFacilities());
		param.put("equipmentIdList", filter.getHotelFacilities());
		param.put("starsIdList", filter.getStars());
		param.put("reserve_is_group", modelObject.getIs_group());
		param.put("hotelsusers_id", modelObject.getHotelsusers_id());

		logger.debug("SearchParams=" + param);

		
		availableRoomsList = new MyBatisHelper().selectList("selectSelectedSearchHotels", param);

		form.add(modal);
		form.add(new Label("priceForNightsTitle", new StringResourceModel("touragents.newbooking.price_for_n_nights", null, new Object[] { filter
				.getNights()
				+ " "
				+ new StringResourceModel("touragents.newbooking.nights." + (filter.getNights() > 5 ? 5 : filter.getNights()), null).getString() })));
		
		
		form.add(new ListView<Integer>("roomlist", new LoadableDetachableModel<List<Integer>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<Integer> load() {
				List<Integer> list = new ArrayList<Integer>();
				int roomcount = filter.getRoomsCountsList().size();
				for (int i = 1; i <= roomcount; i++) list.add(i); 
				return list;
			}
		}) {
			private static final long serialVersionUID = 1L;
			private RoomsCount roomsCount;
//availableRoomsList
			@Override
			protected void populateItem(final ListItem<Integer> parentItem) {
				parentItem.add(new Label("room_index", new StringResourceModel("hotels.reservation.details.room.index", null, new Object[]{parentItem.getModelObject()})));
				
				roomsCount = filter.getRoomsCountsList().get(parentItem.getIndex());
				
				final ListView<BookingSearchResult> list = new ListView<BookingSearchResult>("list", new LoadableDetachableModel<List<BookingSearchResult>>() {
					private static final long serialVersionUID = 1L;

					@Override
					protected List<BookingSearchResult> load() {
						List<BookingSearchResult> list = new ArrayList<BookingSearchResult>();
						for (BookingSearchResult bookingSearchResult : availableRoomsList){
							if (bookingSearchResult.getRoom_index().equals(parentItem.getModelObject())){
								list.add(bookingSearchResult);
							}
						}
						return list;
					}
				}) {
					private static final long serialVersionUID = 1L;

					@Override
					protected void populateItem(final ListItem<BookingSearchResult> item) {
						final BookingSearchResult model = item.getModelObject();
						model.setRoomsCount(roomsCount);
						AjaxLink<String> roomTypeLink = new AjaxLink<String>("roomTypeLink") {
							private static final long serialVersionUID = 1L;

							@Override
							public void onClick(AjaxRequestTarget target) {
								modal.setTitle(model.getRoomtypes_name());
								modal.setPageCreator(new ModalWindow.PageCreator() {
									private static final long serialVersionUID = 1L;

									@Override
									public Page createPage() {
										return new RoomTypeDetailPanel(modal.getContentId(), model.getHotelsusers_id(), model.getRoomtypes_id()) {
											private static final long serialVersionUID = 1L;

											@Override
											protected void onClose(AjaxRequestTarget target) {
												modal.close(target);
											}
										};
									}
								});
								modal.show(target);
							}
						};
						roomTypeLink.add(new Label("roomType", model.getRoomtypes_name()));
						
						Map<String, Serializable> params = new HashMap<String, Serializable>();
						params.put("roomtypes_id", model.getRoomtypes_id());
						params.put("selectId", model.getRoomtypes_id());
						params.put("selectType", 2);
						//params.put("limit", 1);
						KeyAndValue roomTypeImage = new MyBatisHelper().selectOne("selectDefaultImage", params);

						roomTypeLink.add(roomTypeImage != null ? new ContextImage("roomTypeImg", roomTypeImage.getKey()) : new ContextImage("roomTypeImg").setVisible(false));

						item.add(roomTypeLink);
						try {
							int tmpRoomIndex = parentItem.getModelObject();
							if (roomIndex != tmpRoomIndex) {
								roomIndex = tmpRoomIndex;
								rowId = -1;
								rowspan = 1;
							}
							WebMarkupContainer container = new WebMarkupContainer("column");
							if (rowspan > 1 && rowId != model.getRoomtypes_id()) {
								wmc.add(new AttributeModifier("rowspan", rowspan));
							}
							if (rowId == -1 || rowId != model.getRoomtypes_id()) {
								container.add(roomTypeLink);
								rowId = model.getRoomtypes_id();
								rowspan = 1;
								wmc = container;
							} else {
								container.add(roomTypeLink).setVisible(false);
								rowspan++;
							}
							container.add(new RoomTypeEquipmentPanel("equipment", model.getHotelsusers_id(), model.getRoomtypes_id(), false));
							WebMarkupContainer dBed;
							container.add(dBed = new WebMarkupContainer("dbed"){
								private static final long serialVersionUID = 1L;
								
								@Override
								public boolean isVisible() {
									return model.getBedType() == BedType.DOUBLE || model.getBedType() == BedType.TWIN;
								};
							});
							dBed.add(new WebMarkupContainer("double"){
								private static final long serialVersionUID = 1L;

								public boolean isVisible() {
									return model.getBedType() == BedType.DOUBLE;
								};
							});
							dBed.add(new WebMarkupContainer("twin"){
								private static final long serialVersionUID = 1L;

								public boolean isVisible() {
									return model.getBedType() == BedType.TWIN;
								};
							});
							
							item.add(container);
						} finally {
							if (rowspan > 1) {
								wmc.add(new AttributeModifier("rowspan", rowspan));
							}
						}
						
						CheckBox checkRoomType;
						item.add(checkRoomType = new CheckBox("check", new PropertyModel<Boolean>(model, "selected")));
						checkRoomType.add(new AttributeModifier("data-room-index", model.getRoom_index()));
						checkRoomType.add(new AttributeModifier("data-guest-index", model.getGuests()));
						checkRoomType.add(new AttributeModifier("data-prev-room", model.getPrev_room_id()));
						checkRoomType.add(new AttributeModifier("onclick", "checkRoom('" + checkRoomType.getMarkupId()  + "','" + item.getModelObject().getRoom_index() + "');"));

						item.add(new Label("breakfastIncluded", model.getMeal_type() == MealOption.BREAKFAST ? "<i class=\"fa fa-coffee text-warning\"></i> "
								+ new StringResourceModel("touragents.newbooking.breakfast_included", null).getString() + "<br>" : "")
								.setEscapeModelStrings(false));

						Integer noPenaltyBeforeDays = new MyBatisHelper().selectOne("selectHotelRoomCancelationState", model.getHotelsusers_id());
						model.setFree_cancelation(noPenaltyBeforeDays == null || noPenaltyBeforeDays == -1 ? true : false);
						item.add(new Label("freeCancelation",
								model.getFree_cancelation().booleanValue() ? "<i class=\"fa fa-times-circle text-danger\"></i> "
										+ new StringResourceModel("touragents.newbooking.free_cancelation", null).getString() : "")
								.setEscapeModelStrings(false));

						item.add(new Label("numberOfGuests", new StringResourceModel("touragents.newbooking.holding_capacity_title", null, new Object[]{ model.getGuests()})).add(new AttributeModifier("data-item", model.getHolding_capacity_text())));
						item.add(new WebMarkupContainer("withExtraBed") {
							private static final long serialVersionUID = 1L;

							@Override
							public boolean isVisible() {
								return model.getHolding_capacity_text().contains("(+1)");
							}
						});

						HashMap<String, Object> param = new HashMap<String, Object>();
						param.put("resident", filter.isResident());
						param.put("internal", false);
						param.put("is_group", modelObject.getIs_group());
						param.put("hotelsusers_id", modelObject.getHotelsusers_id());
						
						ReservationRuleType rule = new MyBatisHelper().selectOne("selectReservationRuleByHotel", param);
						
						RoomType roomtype = new MyBatisHelper().selectOne("selectHotelRoomTypeById", model.getRoomtypes_id());

						Double first_rate = 0d;
						
						param.put("check_date", DateUtil.getDate(filter.getFromDate(), filter.getFromTime()));
						param.put("roomtypes_id", roomtype.getId());
						param.put("holding_capacity", model.getGuests());
						first_rate = CommonUtil.nvl((Double) new MyBatisHelper().selectOne("selectRateSaleReserve", param)); 

						checkInEarly = CommonUtil.nvl(ReserveUtil.getCheckInCharge(modelObject.getHotelsusers_id(), DateUtil.getDate(filter.getFromDate(), filter.getFromTime()), modelObject.getIs_group(), BigDecimal.valueOf(first_rate)));
						
						Double last_rate = 0d;
						
						param.put("check_date", DateUtil.getDate(filter.getToDate(), filter.getToTime()));
						param.put("roomtypes_id", roomtype.getId());
						param.put("holding_capacity", model.getGuests());
						last_rate = CommonUtil.nvl((Double) new MyBatisHelper().selectOne("selectRateSaleReserve", param));
						
						Double extraBed = 0d;
						
						if (model.getHolding_capacity_text().contains("(+1)")) {
							extraBed = CommonUtil.nvl(rule.getExtra_bed_price_type_value());
							first_rate += extraBed;
							last_rate += extraBed;
						}
						
						if (extraBed > 0) extraBed = extraBed * filter.getNights();
						
						checkOutLate = CommonUtil.nvl(ReserveUtil.getCheckOutCharge(modelObject.getHotelsusers_id(), DateUtil.getDate(filter.getToDate(), filter.getToTime()), modelObject.getIs_group(), BigDecimal.valueOf(last_rate)));
						
						BigDecimal rate = BigDecimal.valueOf(model.getRoom_rate()).add(BigDecimal.valueOf(extraBed));//.add(checkInEarly).add(checkOutLate);
						
						BigDecimal avgRate = BigDecimal.valueOf((model.getRoom_rate() + extraBed) / filter.getNights());
						
						item.add(new Label("priceForNights", ((MySession) getSession()).getCurrencyName()
								+ " "
								+ (model.getRoom_rate() == null ? "?" : CommonUtil.parseAmount(CommonUtil.currencyConverted(((MySession) getSession()).getCurrency(), rate.doubleValue())))));
						item.add(new Label("avgPriceForNights", new StringResourceModel("touragents.newbooking.average_price", null, new Object[]{((MySession) getSession()).getCurrencyName()
								+ " "
								+ (model.getRoom_rate() == null ? "?" : CommonUtil.parseAmount(CommonUtil.currencyConverted(((MySession) getSession()).getCurrency(), avgRate.doubleValue())))}).getString()){
							private static final long serialVersionUID = 1L;

							@Override
							public boolean isVisible() {
								return filter.getNights() > 1;
							}
						}); 
					}
				}.setReuseItems(true);

				parentItem.add(list);
			}
		});
		


		Button submit = new Button("submit") {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected String getOnClickScript() {
				return "return checkOnChecked();";
			}

			@Override
			public void onSubmit() {
				final List<RoomType> roomtypes = new ArrayList<RoomType>();
				for (BookingSearchResult result : availableRoomsList) {
					if (result.isSelected()) {
						roomtypes.add(new RoomType(result.getRoomtypes_id(), result.getRoomtypes_name()));
					}
				}
				if (!roomtypes.isEmpty()) {
					// target.add(feedback);
					activate(new IBreadCrumbPanelFactory() {
						private static final long serialVersionUID = 1L;

						@Override
						public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
							List<ReservationRoom> reserveroomlist = new ArrayList<ReservationRoom>();
							List<ChildAge> agetotallist = new ArrayList<ChildAge>();
							short adults = 0;
							Boolean is_group = null;
							BigDecimal total = new BigDecimal("0");
							for (BookingSearchResult model : availableRoomsList) {
								if (model.isSelected()) {
									ReservationRoom reservationRoom = new ReservationRoom(model.getRoomtypes_id(), model.getRoomtypes_name(), model.getHolding_capacity().byteValue());
									reservationRoom.setPreviously_rooms_id(model.getPrev_room_id());
									List<ChildAge> agelist = new ArrayList<ChildAge>();
									short index = 1;
									if (model.getChildAges() != null && ! model.getChildAges().isEmpty()){
										String [] ages = model.getChildAges().split(";");
										for (String age : ages){
											agelist.add(new ChildAge(index++, Short.valueOf(age)));
										}
									}
									
									adults += model.getGuests();
									reservationRoom.setChildAgeList(agelist);
									agetotallist.addAll(agelist);
									reservationRoom.setHolding_capacity(model.getGuests());
									reservationRoom.setRoomtype(new RoomType(model.getRoomtypes_id(), model.getRoomtypes_name()));
									reservationRoom.setRate(BigDecimal.valueOf(model.getRoom_rate()));
									reservationRoom.setTotal(BigDecimal.valueOf(model.getRoom_rate()));
									reservationRoom.setExtra_bed_needed(model.getHolding_capacity_text().contains("(+1)"));
									
									HashMap<String, Object> param = new HashMap<String, Object>();
									param.put("resident", filter.isResident());
									param.put("internal", false);
									param.put("is_group", modelObject.getIs_group());
									param.put("hotelsusers_id", modelObject.getHotelsusers_id());
									param.put("roomtypes_id", reservationRoom.getRoomtype().getId());
									param.put("holding_capacity", model.getGuests() - ((model.getHolding_capacity_text().contains("(+1)"))?1:0));
									
									ReservationRuleType rule = new MyBatisHelper().selectOne("selectReservationRuleByHotel", param);
									
									param.put("check_date", DateUtil.getDate(filter.getFromDate(), filter.getFromTime()));
									Double first_rate = CommonUtil.nvl((Double) new MyBatisHelper().selectOne("selectRateSaleReserve", param));
									if (model.getHolding_capacity_text().contains("(+1")) first_rate += rule.getExtra_bed_price_type_value();
									reservationRoom.setCheck_in_rate(ReserveUtil.getCheckInCharge(modelObject.getHotelsusers_id(), (Date) param.get("check_date"), modelObject.getIs_group(), BigDecimal.valueOf(first_rate)));
									
									param.put("check_date", DateUtil.getDate(filter.getToDate(), filter.getToTime()));
									Double last_rate = CommonUtil.nvl((Double) new MyBatisHelper().selectOne("selectRateSaleReserve", param));
									if (model.getHolding_capacity_text().contains("(+1")) last_rate += rule.getExtra_bed_price_type_value();
									reservationRoom.setCheck_out_rate(ReserveUtil.getCheckOutCharge(modelObject.getHotelsusers_id(), (Date) param.get("check_date"), modelObject.getIs_group(), BigDecimal.valueOf(last_rate)));

									if (reservationRoom.isExtra_bed_needed()) reservationRoom.setTotal(reservationRoom.getTotal().add((BigDecimal.valueOf(CommonUtil.nvl(model.getExtra_bed_cost() * filter.getNights()))))); 
									reserveroomlist.add(reservationRoom);
									
									total = total.add(reservationRoom.getTotal());
									
									_log.debug("Total: " + total);
									
									if (is_group == null) is_group = model.getIs_group();
								}
							}
							
							if (!reserveroomlist.isEmpty()) {
								if (! is_group) {
									IndividualReservation reserve = new IndividualReservation();
									reserve.setChildAgeList(agetotallist);
									reserve.setChildren((short) agetotallist.size());
									reserve.setAdults(adults);
									reserve.setHotelsusers_id(modelObject.getHotelsusers_id());
									reserve.setNumber_of_nights(filter.getNights().shortValue());
									reserve.setCheck_in(DateUtil.getDate(filter.getFromDate(), filter.getFromTime()));
									reserve.setCheck_out(DateUtil.getDate(filter.getToDate(), filter.getToTime()));
									reserve.setReserverooms(reserveroomlist);
									reserve.setTotal(total);
									return new TAIndividualReservationPanel(componentId, breadCrumbModel, new Model<IndividualReservation>(reserve), filter);
								} else {
									GroupReservation reserve = new GroupReservation();
									reserve.setHotelsusers_id(modelObject.getHotelsusers_id());
									reserve.setNumber_of_nights(filter.getNights().shortValue());
									reserve.setAdults(adults);
									reserve.setChildAgeList(agetotallist);
									reserve.setChildren((short) agetotallist.size());
									reserve.setNumber_of_nights(filter.getNights().shortValue());
									reserve.setCheck_in(DateUtil.getDate(filter.getFromDate(), filter.getFromTime()));
									reserve.setCheck_out(DateUtil.getDate(filter.getToDate(), filter.getToTime()));
									reserve.setReserverooms(reserveroomlist);
									reserve.setTotal(total);
									return new TAGroupReservationPanel(componentId, breadCrumbModel, new Model<GroupReservation>(reserve), filter);
								}
							}
							return null;
						}
					});
				}
			}
		};
		form.add(submit);
		/*
		 * Second tab
		 */


		add(new ListView<Facility>("facilities", HotelModels.getHotelFacilities(modelObject.getHotelsusers_id())) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<Facility> item) {
				item.add(new Label("name", item.getModelObject().getName()));
			}
		});

		add(new ListView<Service>("services", HotelModels.getServicesInRooms(modelObject.getHotelsusers_id())) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<Service> item) {
				item.add(new Label("name", item.getModelObject().getName()));
			}
		});

		final LoadableDetachableModel<List<? extends HotelNearByPlace>> hotelNearbyPlacesList = HotelModels.getHotelNearbyPlaces(modelObject.getHotelsusers_id());

		add(new ListView<HotelNearByPlace>("nearbyplaces", hotelNearbyPlacesList) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<HotelNearByPlace> item) {
				item.add(new Label("name", HotelModels.getNearByPlacesType(item.getModelObject().getType()) + " - "
						+ FormatUtil.format2(item.getModelObject().getValue()) + " " + getString("touragents.newbooking.near_by_places.unit")));
			}

			@Override
			public boolean isVisible() {
				return hotelNearbyPlacesList.getObject().size() > 0;
			}
		});
		
		add(new ListView<ReservationCancellationPolicy>("cancelation_policy", new LoadableDetachableModel<List<ReservationCancellationPolicy>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<ReservationCancellationPolicy> load() {
				Map<String, Serializable> params = new HashMap<String, Serializable>();
				params.put("hotelsusers_id", modelObject.getHotelsusers_id());
				params.put("is_group", false);
				return new MyBatisHelper().selectList("selectReservationCancellationPolicyByHotelId", params);
			}
		}) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<ReservationCancellationPolicy> item) {
				item.add(new Label("no_penalty_before_days", new StringResourceModel("touragents.newbooking.cancelation_before_days", null,
						new Object[] { item.getModelObject().getNo_penalty_before_days().getId() })));
				
				StringResourceModel late_cancel_penalty = null;
				
				if (item.getModelObject().isLate_cancel_penalty_first_night()) {
					late_cancel_penalty = new StringResourceModel("touragents.newbooking.cancelation_charge_value.first_night", null,
							new Object[] { item.getModelObject().getNo_penalty_before_days().getId() });
				} else {
					late_cancel_penalty = new StringResourceModel("touragents.newbooking.cancelation_charge_value", null,
							new Object[] { item.getModelObject().getNo_penalty_before_days().getId(), item.getModelObject().getLate_cancel_penalty().getId() });
				}
				item.add(new Label("late_cancel_penalty", late_cancel_penalty));
				
				StringResourceModel no_show_penalty = null;
				
				if (item.getModelObject().isNo_show_penalty_first_night()){
					no_show_penalty = new StringResourceModel("touragents.newbooking.no_show_policy.first_night", null);
				} else {
					no_show_penalty = new StringResourceModel("touragents.newbooking.no_show_policy", null,
							new Object[] { item.getModelObject().getNo_show_penalty().getId() });
				}
				
				item.add(new Label("no_show", no_show_penalty));
			}
		});

		LoadableDetachableModel<ReservationRuleType> reservationRuleModel = new LoadableDetachableModel<ReservationRuleType>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected ReservationRuleType load() {
				Map<String, Serializable> params = new HashMap<String, Serializable>();
				params.put("hotelsusers_id", modelObject.getHotelsusers_id());
				params.put("is_group", false);
				return new MyBatisHelper().selectOne("selectReservationRulesByHotelsId", params);
			}
		};

		add(new Label("check_in_period", CommonUtil.getHotelTime(reservationRuleModel.getObject().getCheck_in_from_time()) + " - "
				+ CommonUtil.getHotelTime(reservationRuleModel.getObject().getCheck_in_to_time())));

		add(new Label("earlyFullArrival", new StringResourceModel("touragents.newbooking.early_arrival.value", null,
				new Object[] { CommonUtil.getHotelTime(reservationRuleModel.getObject().getCheck_in_full_charge_from_time()),
						       CommonUtil.getHotelTime(reservationRuleModel.getObject().getCheck_in_full_charge_to_time())})));

		add(new Label("earlyFullArrivalCharge", new StringResourceModel("touragents.newbooking.early_arrival.value.charge", null,
				new Object[] { reservationRuleModel.getObject().getCheck_in_full_charge_service_charge_type() == PriceType.IN_PERCENT ? FormatUtil
						.format2(reservationRuleModel.getObject().getCheck_in_full_charge_service_charge()) + "%" : "$" + FormatUtil
						.format2(reservationRuleModel.getObject().getCheck_in_full_charge_service_charge()) })));
		
		add(new Label("earlyHalfArrival", new StringResourceModel("touragents.newbooking.early_arrival.value", null,
				new Object[] { CommonUtil.getHotelTime(reservationRuleModel.getObject().getCheck_in_half_charge_from_time()),
						CommonUtil.getHotelTime(reservationRuleModel.getObject().getCheck_in_half_charge_to_time())})));
		
		add(new Label("earlyHalfArrivalCharge", new StringResourceModel("touragents.newbooking.early_arrival.value.charge", null,
				new Object[] { reservationRuleModel.getObject().getCheck_in_half_charge_service_charge_type() == PriceType.IN_PERCENT ? FormatUtil
						.format2(reservationRuleModel.getObject().getCheck_in_half_charge_service_charge()) + "%" : "$"+FormatUtil
						.format2(reservationRuleModel.getObject().getCheck_in_half_charge_service_charge()) })));

		add(new Label("check_out_period", CommonUtil.getHotelTime(reservationRuleModel.getObject().getCheck_out_from_time()) + " - "
				+ CommonUtil.getHotelTime(reservationRuleModel.getObject().getCheck_out_to_time())));
		
		add(new Label("lateFullDeparture", new StringResourceModel("touragents.newbooking.late_departure.value", null,
				new Object[] { CommonUtil.getHotelTime(reservationRuleModel.getObject().getCheck_out_full_charge_from_time()),
						       CommonUtil.getHotelTime(reservationRuleModel.getObject().getCheck_out_full_charge_to_time())})));
		
		add(new Label("lateFullDepartureCharge", new StringResourceModel("touragents.newbooking.late_departure.value.charge", null,
				new Object[] { reservationRuleModel.getObject().getCheck_out_full_charge_service_charge_type() == PriceType.IN_PERCENT ? FormatUtil
						.format2(reservationRuleModel.getObject().getCheck_out_full_charge_service_charge()) + "%" : "$"+FormatUtil
						.format2(reservationRuleModel.getObject().getCheck_out_full_charge_service_charge()) })));

		add(new Label("lateHalfDeparture", new StringResourceModel("touragents.newbooking.late_departure.value", null,
				new Object[] { CommonUtil.getHotelTime(reservationRuleModel.getObject().getCheck_out_half_charge_from_time()),
						       CommonUtil.getHotelTime(reservationRuleModel.getObject().getCheck_out_half_charge_to_time())})));

		add(new Label("lateHalfDepartureCharge", new StringResourceModel("touragents.newbooking.late_departure.value.charge", null,
				new Object[] { reservationRuleModel.getObject().getCheck_out_half_charge_service_charge_type() == PriceType.IN_PERCENT ? FormatUtil
						.format2(reservationRuleModel.getObject().getCheck_out_half_charge_service_charge()) + "%" : "$"+FormatUtil
						.format2(reservationRuleModel.getObject().getCheck_out_half_charge_service_charge()) })));
		

		add(new Label("description", hotel.getHotelsDetails().getDescription()).setEscapeModelStrings(false));
		// add(new Label("service_charge", reservationRuleModel.getObject().getService_charge() + "%"));
		// add(new Label("city_tax", new StringResourceModel("touragents.newbooking.tax_per_person_per_night", null, reservationRuleModel.getObject().getcity_tax())));
	}

	@Override
	public Class<?> implementedClass() {
		return this.getClass();
	}

	@Override
	public IModel<String> getTitle() {
		return new StringResourceModel("touragents.newbooking.selected_hotel_name", null, new Object[] { title });
	}
}
