package uz.hbs.actions.touragent.newbooking;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.MaskType;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uz.hbs.actions.touragent.newbooking.panel.NearbyPlacesPanel;
import uz.hbs.beans.Address;
import uz.hbs.beans.Booking;
import uz.hbs.beans.Booking.Age;
import uz.hbs.beans.Booking.RoomsCount;
import uz.hbs.beans.BookingSearchResult;
import uz.hbs.beans.Hotel;
import uz.hbs.beans.HotelStar;
import uz.hbs.beans.KeyAndValue;
import uz.hbs.beans.MealOption;
import uz.hbs.beans.Region;
import uz.hbs.beans.RoomType;
import uz.hbs.components.pages.GoogleMapPage;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;
import uz.hbs.markup.navigation.BootstrapPagingNavigator;
import uz.hbs.markup.navigation.BootstrapPagingNavigator.Size;
import uz.hbs.session.MySession;
import uz.hbs.utils.CommonUtil;
import uz.hbs.utils.DateUtil;
import uz.hbs.utils.HotelUtil;

public class SearchForBookingResultPanel extends Panel {
	private static Logger logger = LoggerFactory.getLogger(SearchForBookingResultPanel.class);
	private static final long serialVersionUID = 1L;

	// private Booking model;

	public SearchForBookingResultPanel(String id, final MyBreadCrumbPanel breadCrumbPanel, final IModel<Booking> filterModel) {
		super(id, filterModel);
		Form<Booking> form = new Form<Booking>("form");
		add(form);

		final ModalWindow modal = new ModalWindow("dialog");
		modal.setAutoSize(false);
		modal.setMaskType(MaskType.SEMI_TRANSPARENT);
		modal.setCssClassName(ModalWindow.CSS_CLASS_GRAY);
		modal.setInitialHeight(620);
		modal.setInitialWidth(1140);
		modal.setMinimalHeight(620);
		modal.setMinimalWidth(1140);
		modal.setOutputMarkupId(true);
		modal.setResizable(false);
		form.add(modal);

		final Booking model = filterModel.getObject();
		int adultsCount = 0, childrenCount = 0;
		
		String roomAdultsChildParams = "", childAges = "", childAgesForDisplay = "";
		for (RoomsCount roomsCount : model.getRoomsCountsList()) {
			roomAdultsChildParams += roomAdultsChildParams.isEmpty() ? roomsCount.getAdults() : "," + roomsCount.getAdults();
			adultsCount += roomsCount.getAdults();
			childAges = "";
			for (Age age : roomsCount.getAgesList()) {
				childAges += childAges.isEmpty() ? age.getAge().toString() : ";" + age.getAge().toString();
				childAgesForDisplay += childAgesForDisplay.isEmpty() ? age.getAge().toString() : ", " + age.getAge().toString();
				childrenCount++;
			}
			if (childAges != null && !childAges.isEmpty()) {
				roomAdultsChildParams += ":" + childAges;
			}
		}
		
		if (model.getCity() != null) model.setRegion(new Region(model.getCity().getRegions_id()));
		
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("params", roomAdultsChildParams);
		params.put("check_in", DateUtil.getDate(model.getFromDate(), model.getFromTime()));
		params.put("check_out", DateUtil.getDate(model.getToDate(), model.getToTime()));
		params.put("region_id", model.getRegion().getId());
		params.put("city_id", model.getCity().getId());
		params.put("resident", model.isResident());
		params.put("range_from", filterModel.getObject().getRange_from_value());
		params.put("range_to", filterModel.getObject().getRange_to_value());
		params.put("facilityIdList", model.getHotelFacilities());
		params.put("equipmentIdList", model.getRoomFacilities());
		params.put("starsIdList", model.getStars());
		params.put("room_count", model.getRoomsCountsList().size());
		params.put("sortFilter", model.getSortResults());
		params.put("nights", model.getNights());
		params.put("sortOrder", model.isSortOrder()?"DESC":"ASC");
		params.put("nearbyplaceList", model.getNearByPlaces());
		
		logger.debug("SearchParams=" + params);

		
		List<BookingSearchResult> resultList = new MyBatisHelper().selectList("selectSearchHotelsList", params);

		String adultsLabel = new StringResourceModel("touragents.newbooking.adults." + (adultsCount == 1 ? adultsCount : 2), null).getString();
		String childrenLabel = new StringResourceModel("touragents.newbooking.child." + (childrenCount == 1 ? childrenCount : 2), null).getString();
		String recommendedLabelAdults = new StringResourceModel("touragents.newbooking.recommended_title", null, new Object[] { adultsCount, adultsLabel }).getString();
		String recommendedLabelChildren = new StringResourceModel("touragents.newbooking.recommended_title_children", null, new Object[]{childrenCount, childrenLabel, childAgesForDisplay}).getString();
		final String recommendedLabel = recommendedLabelAdults + (childrenCount == 0 ? "" : recommendedLabelChildren);

		model.setAge_label(new StringResourceModel("touragents.newbooking.adults", null).getString() + ": " + adultsCount + ", " + new StringResourceModel("touragents.newbooking.children", null).getString() + ": " + childrenCount + (childrenCount > 0 ? " (" + childAgesForDisplay + ")" : ""));
		
		final PageableListView<BookingSearchResult> listView = new PageableListView<BookingSearchResult>("repeater", resultList, ((MySession)getSession()).getSettings().getTable_rows()) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<BookingSearchResult> item) {
				final BookingSearchResult modelObject = item.getModelObject();
				final BookingSearchResult parseRoomTypes = parseRoomTypes(modelObject.getRoomtypes_name());
				modelObject.setRoom_rate(parseRoomTypes.getRoom_rate());
				modelObject.setIs_group(parseRoomTypes.getIs_group());
				item.add(new Label("name", modelObject.getDisplay_name()));
				// String stars = "";
				// for (int i = 0; i < modelObject.getStars(); i++) {
				// stars += "<i class=\"fa fa-star fa-2x\"></i> ";
				// }
				item.add(new Label("stars", HotelUtil.starDecorator(modelObject.getStars(), HotelStar.MAX_STARS, false)).setEscapeModelStrings(false));
				item.add(new Label("recommended", recommendedLabel));

				String adults = "";
				for (int i = 0; i < 2; i++) {
					adults += "<i class=\"glyphicon glyphicon-user\"></i>";
				}
				item.add(new Label("adults", adults).setEscapeModelStrings(false).setVisible(false));

				item.add(new Label("roomType", parseRoomTypes.getRoomtypes_name()).setEscapeModelStrings(false));

				Map<String, Serializable> params = new HashMap<String, Serializable>();
				params.put("hotel_id", modelObject.getHotelsusers_id());
				// params.put("roomtype_id", modelObject.getRoomtypes_id());
				//
				// List<Equipment> roomEquipmentList = new MyBatisHelper().selectList("selectEquipmentListByRoomTypeHotel", params);
				//
				// String equipmentStr = "<ul class=\"nav nav-pills\">";
				//
				// for (Equipment equipment : roomEquipmentList) {
				// equipmentStr += "<li role=\"presentation\">" + equipment.getName() + "</li>";
				// }
				// equipmentStr += "</ul>";
				// item.add(new Label("equipment", equipmentStr).setEscapeModelStrings(false));
				//
				item.add(new Label("priceForNightsLabel", new StringResourceModel("touragents.newbooking.price_for_n_nights", null,
						new Object[] { filterModel.getObject().getNights()
								+ " "
								+ new StringResourceModel("touragents.newbooking.nights."
										+ (filterModel.getObject().getNights() > 5 ? 5 : filterModel.getObject().getNights()), null).getString() })));
//				HashMap<String, Object> param = new HashMap<String, Object>();
//				param.put("check_date", DateUtil.getDate(model.getFromDate(), model.getFromTime()));
//				param.put("resident", model.isResident());
//				param.put("internal", false);
//				param.put("is_group", modelObject.getIs_group());
//				param.put("hotel_id", modelObject.getHotelsusers_id());
				
//				ReservationRuleType rule = new MyBatisHelper().selectOne("selectReservationRuleByHotel", param);
				
//				Double first_rate = 0d;
//				
//				for (RoomType roomtype: parseRoomTypes.getRoomtypeslist()) {
//					param.put("roomtype_id", roomtype.getId());
//					param.put("holding_capacity", roomtype.getHolding_capacity().getId());
//					Double d = CommonUtil.nvl((Double) new MyBatisHelper().selectOne("selectRateSaleReserve", param));
//					if (roomtype.getAdditional_bed().isId()) d += CommonUtil.nvl(rule.getExtra_bed_price_type_value());
//					d = d * roomtype.getNumber_of_rooms();
//					first_rate += d; 
//				}
				
//				@SuppressWarnings("unused")
//				BigDecimal checkInEarly = CommonUtil.nvl(ReserveUtil.getCheckInCharge(modelObject.getHotelsusers_id(), DateUtil.getDate(model.getFromDate(), model.getFromTime()), modelObject.getIs_group(), BigDecimal.valueOf(first_rate)));
				
//				param.put("check_date", DateUtil.getDate(model.getToDate(), model.getToTime()));
//				Double last_rate = 0d;
//				
//				for (RoomType roomtype: parseRoomTypes.getRoomtypeslist()) {
//					param.put("roomtype_id", roomtype.getId());
//					param.put("holding_capacity", roomtype.getHolding_capacity().getId());
//					Double d = CommonUtil.nvl((Double) new MyBatisHelper().selectOne("selectRateSaleReserve", param));
//					if (roomtype.getAdditional_bed().isId()) d += CommonUtil.nvl(rule.getExtra_bed_price_type_value());
//					d = d * roomtype.getNumber_of_rooms();
//					last_rate += d; 
//				}
//				
//				@SuppressWarnings("unused")
//				BigDecimal checkOutLate = CommonUtil.nvl(ReserveUtil.getCheckOutCharge(modelObject.getHotelsusers_id(), DateUtil.getDate(model.getToDate(), model.getToTime()), modelObject.getIs_group(), BigDecimal.valueOf(last_rate)));
				BigDecimal rate = BigDecimal.valueOf(modelObject.getRoom_rate());//.add(checkInEarly).add(checkOutLate);
				BigDecimal avgRate = BigDecimal.valueOf(modelObject.getRoom_rate() / filterModel.getObject().getNights());//.add(checkInEarly).add(checkOutLate);
				
				item.add(new Label("roomRate", ((MySession) getSession()).getCurrencyName()
						+ " "
						+ (modelObject.getRoom_rate() == null ? "?" : CommonUtil.parseAmount(CommonUtil.currencyConverted(((MySession) getSession()).getCurrency(), rate.doubleValue())))));
				item.add(new Label("roomAvgRate", new StringResourceModel("touragents.newbooking.average_price", null, new Object[]{((MySession) getSession()).getCurrencyName()
						+ " "
						+ (modelObject.getRoom_rate() == null ? "?" : CommonUtil.parseAmount(CommonUtil.currencyConverted(((MySession) getSession()).getCurrency(), avgRate.doubleValue())))}).getString()){
					private static final long serialVersionUID = 1L;

					@Override
					public boolean isVisible() {
						return filterModel.getObject().getNights() > 1;
					}
				}); 
				item.add(new Label("freeCancelation", modelObject.getFree_cancelation() ? "<i class=\"fa fa-times-circle text-danger\"></i> "
						+ new StringResourceModel("touragents.newbooking.free_cancelation", null).getString() + "; " : "")
						.setEscapeModelStrings(false));
				item.add(new Label("breakfastIncluded",
						modelObject.getMeal_type() == MealOption.BREAKFAST ? "<i class=\"fa fa-coffee text-warning\"></i> "
								+ new StringResourceModel("touragents.newbooking.breakfast_included", null).getString() : "")
						.setEscapeModelStrings(false));
				item.add(new Link<String>("moreOptionsLink") {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						// target.add(feedback);
						breadCrumbPanel.activate(new IBreadCrumbPanelFactory() {
							private static final long serialVersionUID = 1L;

							@Override
							public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
								Hotel hotel = new MyBatisHelper().selectOne("selectHotelInfo", modelObject.getHotelsusers_id());
								return new BookingSelectedHotel(componentId, breadCrumbModel, modelObject, hotel, filterModel.getObject());
							}
						});
					}
				}.add(new Label("moreOptionsLabel", new StringResourceModel("touragents.newbooking.more_info", null)).setRenderBodyOnly(true)));
				
				Label addressLabel = new Label("regionName", modelObject.getCity() + (modelObject.getAddress() != null && !modelObject.getAddress().equals("") ? " - "
						+ modelObject.getAddress() : ""));
				addressLabel.setRenderBodyOnly(true);
				
				IndicatingAjaxLink<String> addressLink = new IndicatingAjaxLink<String>("showOnMapLink") {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						modal.setTitle(modelObject.getDisplay_name());
						final Address address = new Address();
						address.setLatitude(modelObject.getLatitude());
						address.setLongitude(modelObject.getLongitude());
						modal.setPageCreator(new ModalWindow.PageCreator() {
							private static final long serialVersionUID = 1L;

							@Override
							public Page createPage() {
								return new GoogleMapPage(address);
							}
						});
						modal.show(target);
					}
				};
				addressLink.add(new AttributeModifier("data-original-title", new StringResourceModel("touragents.newbooking.show_on_map", null).getString()));
				addressLink.add(addressLabel);
				item.add(addressLink);

				//item.add(new Label("address", modelObject.getAddress() != null && !modelObject.getAddress().equals("") ? " - " + modelObject.getAddress() : ""));
				// item.add(new Button("book") {
				// private static final long serialVersionUID = 1L;
				//
				// @Override
				// public void onSubmit() {
				// super.onSubmit();
				//
				// // target.add(feedback);
				// breadCrumbPanel.activate(new IBreadCrumbPanelFactory() {
				// private static final long serialVersionUID = 1L;
				//
				// @Override
				// public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
				// Hotel hotel = new MyBatisHelper().selectOne("selectHotelInfo", modelObject.getHotelsusers_id());
				// return new BookingSelectedHotel(componentId, breadCrumbModel, hotel);
				// }
				// });
				// }
				// });
				params.put("selectId", modelObject.getHotelsusers_id());
				params.put("selectType", 1);
				KeyAndValue link = new MyBatisHelper().selectOne("selectDefaultImage", params);
				item.add(link != null ? new ContextImage("img", link.getKey()) : new ContextImage("img").setVisible(false));
				item.add(new NearbyPlacesPanel("nearbyPlaces", modelObject));
			}
		};
		form.add(listView);
		form.add(new BootstrapPagingNavigator("navigator1", listView, Size.Default){
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible() {
				return listView.getPageCount() > 1;
			}
		});
		form.add(new NavigatorLabel("navigatorLabel1", listView) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible() {
				return listView.size() > 0;
			}
		});
		form.add(new BootstrapPagingNavigator("navigator2", listView, Size.Default){
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible() {
				return listView.getPageCount() > 1;
			}
		});
		form.add(new NavigatorLabel("navigatorLabel2", listView){
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible() {
				return listView.size() > 0;
			}
		});
		form.add(new Label("noDataFound", new StringResourceModel("touragents.newbooking.no_data_found", null)) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible() {
				return listView.size() < 1;
			}
		});
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		// bunday qilishga sabab, modal window orqali ochganida script yuklanmas ekan, shuning uchun page yuklanganida script yuklab olinadi va modal
		// windowda map yuklanadi
		// HeaderItemUtil.setGoogleMapUrlOnlyHeaderItem(response);
		super.renderHead(response);
	}
	//
	// public Booking getModel() {
	// return model;
	// }
	//
	// public void setModel(Booking model) {
	// this.model = model;
	// }
	
	public static void main(String[] args) {
		String s= "1 x Standard[#]1 (+1)[$]23.5<br>1 x Twin[#]1 (+1)[$]100<br>1 x Twin[#]2[$]55.7";
		String[] split = s.split("<br>");
		for (String token : split) {
			logger.debug("Token: " + token);
			String countStr = token.substring(token.indexOf("[#]") + 3, token.indexOf("[$]"));
			logger.debug("countStr: " + countStr);
			String rateStr = token.substring(token.indexOf("[$]") + 3);
			logger.debug("rateStr: " + rateStr);
			String hodingCapacity = "";
			boolean addBed = false;
			if (countStr.contains("(+1)")) {
				countStr = countStr.substring(0, countStr.indexOf("(+1)")).trim();
				addBed = true; 
			} 
			for (int i = 0; i < Integer.parseInt(countStr); i++) {
				hodingCapacity += "<i class=\"glyphicon glyphicon-user\"></i>";
			}
			if (addBed) {
				hodingCapacity += "<i class=\"fa fa-user\"></i>";
			}
			logger.debug("HoldingCapacity: " + hodingCapacity);
			String s1 = token.substring(0, token.indexOf("[#]"));
			logger.debug("S1: " + s1);
		}
	}

	
	private BookingSearchResult parseRoomTypes(String s) {
		String[] split = s.split("<br>");
		BookingSearchResult result = new BookingSearchResult();
		String roomTypeInfo = "";
		double roomRate = 0;
		boolean is_group = false;
		for (String token : split) {
			String countStr = token.substring(token.indexOf("[#]") + 3, token.indexOf("[$]"));
			String rateStr = token.substring(token.indexOf("[$]") + 3, token.indexOf("[*]"));
			String isGroupStr = token.substring(token.indexOf("[*]") + 3);
			roomRate += Double.parseDouble(rateStr);
			is_group = Boolean.parseBoolean(isGroupStr);
			String holdingCapacity = "<span style=\"font-size: 11px; color: #003580\">";
			boolean addBed = false;
			if (countStr.contains("(+1)")) {
				countStr = countStr.substring(0, countStr.indexOf("(+1)")).trim();
				addBed = true; 
			} 
			for (int i = 0; i < Integer.parseInt(countStr); i++) {
				holdingCapacity += "<i class=\"glyphicon glyphicon-user\"></i>";
			}
			if (addBed) {
				holdingCapacity += "<i style=\"font-size: 8px;\" class=\"glyphicon glyphicon-user\"></i>";
			}
			holdingCapacity += "</span>";
			String roomcount = token.substring(0, token.indexOf(" x "));
			int roomtype_id = Integer.parseInt(token.substring(token.indexOf(" x ") + 3, token.indexOf("[#]")).trim());
			String roomType = (!roomcount.equals("1") ? (roomcount + " x ") : "") + new MyBatisHelper().selectOne("selectRoomTypeNameById", roomtype_id);
			result.getRoomtypeslist().add(new RoomType(roomtype_id, roomType, Byte.parseByte(countStr), addBed, Integer.parseInt(roomcount.trim())));
			roomTypeInfo +=  (roomTypeInfo.isEmpty() ? "" : "<br>")  + (holdingCapacity + " " + roomType);
		}
		result.setIs_group(is_group);
		result.setRoomtypes_name(roomTypeInfo);
		result.setRoom_rate(roomRate);
		return result;
	}
}
