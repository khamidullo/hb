package uz.hbs.utils;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.apache.ibatis.session.SqlSession;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uz.hbs.beans.Account;
import uz.hbs.beans.Bill;
import uz.hbs.beans.ChildAge;
import uz.hbs.beans.Condition;
import uz.hbs.beans.Contract;
import uz.hbs.beans.Equipment;
import uz.hbs.beans.Facility;
import uz.hbs.beans.Hotel;
import uz.hbs.beans.HotelDetail;
import uz.hbs.beans.HotelNearByPlace;
import uz.hbs.beans.IdAndValue;
import uz.hbs.beans.MealOption;
import uz.hbs.beans.ReferenceInfo;
import uz.hbs.beans.ReservationCancellationPolicy;
import uz.hbs.beans.ReservationDetail;
import uz.hbs.beans.ReservationRule;
import uz.hbs.beans.ReservationRuleType;
import uz.hbs.beans.ReservationStatus;
import uz.hbs.beans.Room;
import uz.hbs.beans.RoomSetup;
import uz.hbs.beans.RoomState;
import uz.hbs.beans.RoomType;
import uz.hbs.beans.Service;
import uz.hbs.beans.UploadedFile;
import uz.hbs.beans.User;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;
import uz.hbs.session.MySession;

public class HotelUtil {
	private static final Logger logger = LoggerFactory.getLogger(HotelUtil.class);

	public HotelUtil() {
	}

	public static void addNewHotel(SqlSession sql, Hotel hotel, MyFeedbackPanel feedback, MySession session) {
		User user = new User();
		
		
		user.setName(hotel.getLegal_name());
		user.setEmail(hotel.getCorporate_email());
		user.setType(new IdAndValue((int) User.TYPE_HOTEL));
		user.setPassword(PasswordGeneratorUtil.getPassword(6));
		user.setStatus(new IdAndValue((int) User.STATUS_NEW));
		user.setInitiator_user_id(session.getUser().getId());
		
		CommonUtil.generateLogin(user, sql);
		
		try {
			if (((Integer) sql.insert("insertUser", user)) > 0) {
				logger.debug("Step #1: User created=" + user);
				
				Account acc = CommonUtil.insertUserAccount(sql, user);
				
				logger.debug("Step #2: User Account created, Id=" + acc.getId());
				
				hotel.setUsers_id(user.getId());
				sql.insert("insertHotel", hotel);
				
				logger.debug("Step #3: Hotel created=" + hotel.getDisplay_name());
				
				Contract contract = hotel.getContract();
				contract.setUsers_id(hotel.getUsers_id());
				sql.update("insertContract", contract);
				
				logger.debug("Step #4: Contract created=" + contract);
			}
			sql.commit();
		} catch (Exception e) {
			logger.error("Exception", e);
			sql.rollback();
		} finally {
			sql.close();
		}
		StringTemplateGroup templateGroup = new StringTemplateGroup("mailgroup");
		templateGroup.setFileCharEncoding("UTF-8");

		StringTemplate st = templateGroup.getInstanceOf("uz/hbs/utils/email/templates/HotelAccountDetails");
		st.setAttribute("legal_name", hotel.getLegal_name());
		st.setAttribute("corporate_email", hotel.getCorporate_email());
		st.setAttribute("primary_contact", hotel.getPrimary_phone());
		st.setAttribute("secondary_contact", hotel.getSecondary_phone());
		// st.setAttribute("link", "");

		if (MessagesUtil.createHtmlMessage(hotel.getManager_email(), "Account details", st.toString(), session.getUser().getId(), false)) {
			feedback.success("Email is sent");
		} else {
			feedback.error("Error, Email is not sent");
		}
	}

	public static void addHotelDetails(SqlSession sql, HotelDetail details, Hotel hotel, MySession session) throws Exception {
		details.getAddress().setInitiator_user_id((session.getUser().getId()));
		if ((Integer) sql.insert("insertAddresses", details.getAddress()) > 0) {
			details.setHotelsusers_id(hotel.getUsers_id());
			for (Facility facility : details.getFacilities()) {
				facility.setHotelsusers_id(hotel.getUsers_id());
				sql.insert("insertHotelFacilities", facility);
			}
			for (Service service : details.getService()) {
				service.setHotelsusers_id(hotel.getUsers_id());
				sql.insert("insertHotelServices", service);
			}
			for (HotelNearByPlace place : details.getHotelsnearbyplaces()) {
				place.setHotelsusers_id(hotel.getUsers_id());
				place.setInitiator_user_id(hotel.getUsers_id());
				sql.insert("insertHotelsNearByPlaces", place);
			}
			sql.insert("insertHotelDetails", details);
			
			hotel.setHotelscategories_id(details.getHotelCategory().getId());
			sql.update("updateHotel", hotel);
			
			saveImage(sql, details.getHotelImagesfield(), hotel.getUsers_id(), true, hotel.getUsers_id());
		}
		sql.commit();
	}

	public static void addReservationRules(SqlSession sql, ReservationRule rules, Hotel hotel) throws Exception {
		// ************************ INDIVIDUAL **********************************************//
		ReservationRuleType reserve_rule = rules.getIndividual();
		ReservationCancellationPolicy policy = reserve_rule.getReservationcancellationpolicy();

		if (!policy.isSupport_tentative_reservation_warn()) {
			policy.setNotify_ta_before_days(new IdAndValue(0));
		}

		sql.insert("insertReservationCancellationPolicy", policy);

		reserve_rule.setHotelsusers_id(hotel.getUsers_id());

		sql.insert("insertReservationRules", reserve_rule);

		// ************************ GROUP **********************************************//
		reserve_rule = rules.getGroup();
		policy = reserve_rule.getReservationcancellationpolicy();

		if (!policy.isSupport_tentative_reservation_warn()) {
			policy.setNotify_ta_before_days(new IdAndValue(0));
		}

		sql.insert("insertReservationCancellationPolicy", policy);

		reserve_rule.setHotelsusers_id(hotel.getUsers_id());
		sql.insert("insertReservationRules", reserve_rule);

		sql.commit();
	}

	public static void addReferenceInfo(SqlSession sql, ReferenceInfo info, Hotel hotel, MySession session) throws Exception {
		for (Condition condition : info.getConditions()) {
			if (condition.getId() == null) {
				condition.setHotelsusers_id(hotel.getUsers_id());
				sql.insert("insertConditions", condition);
			}
		}

		sql.delete("deleteHotelsEquipments", hotel.getUsers_id());
		for (Equipment equipment: info.getEquipments()) {
			equipment.setHotelsusers_id(hotel.getUsers_id());
			sql.insert("insertHotelsEquipments", equipment);
		}
		
		for (RoomType roomtype : info.getRoom_types()) {
			if (roomtype.getId() == null) {
				roomtype.setStatus(RoomType.STATUS_ACTIVE);
				roomtype.setInitiator_user_id(session.getUser().getId());
				sql.insert("insertRoomType", roomtype);
				roomtype.setHotelsusers_id(hotel.getUsers_id());
				sql.insert("insertHotelRoomTypes", roomtype);
			}
		}

		hotel.setFloors(info.getFloors());
		hotel.setRooms(info.getRooms());
		sql.update("updateHotelsFloorsAndRooms", hotel);

		MealOption meal_options = info.getMeal_options();

		if (meal_options.isBreakfast()) {
			sql.insert(
					"insertMealOptions",
					new MealOption(MealOption.BREAKFAST, meal_options.isBreakfast_included_to_room_rate(), meal_options
							.getBreakfast_per_person_per_night(), hotel.getUsers_id()));
		}
		if (meal_options.isLunch()) {
			sql.insert("insertMealOptions",
					new MealOption(MealOption.LUNCH, meal_options.isLunch_included_to_room_rate(), meal_options.getLunch_per_person_per_night(),
							hotel.getUsers_id()));
		}
		if (meal_options.isDinner()) {
			sql.insert("insertMealOptions",
					new MealOption(MealOption.DINNER, meal_options.isDinner_included_to_room_rate(), meal_options.getDinner_per_person_per_night(),
							hotel.getUsers_id()));
		}
		
		Hotel hotelSelfPaymentPossibility = new Hotel();
		hotelSelfPaymentPossibility.setUsers_id(hotel.getUsers_id());
		hotelSelfPaymentPossibility.setSelf_payment_possibility(info.getSelf_payment_possibility() == 0 ? false : true);
		
		sql.update("updateHotel", hotelSelfPaymentPossibility);
		
		sql.commit();
	}

	public static void addRoomSetup(SqlSession sql, RoomSetup setup, Hotel hotel, MySession session) throws Exception {
		long user_id = session.getUser().getId();
		Iterator<RoomType> iterator = setup.getRoom_types().values().iterator();
		while (iterator.hasNext()) {
			RoomType roomtype = iterator.next();
			for (Room room : roomtype.getRooms()) {
				room.setInitiator_user_id(user_id);
				room.setHotelsusers_id(hotel.getUsers_id());
				room.setRoomtypes_id(roomtype.getId());
				sql.insert("insertRoom", room);
			}

			for (Condition condition : roomtype.getConditions()) {
				condition.setHotelsusers_id(hotel.getUsers_id());
				condition.setRoomtypes_id(roomtype.getId());
				sql.insert("insertHotelsRoomsConditions", condition);
			}

			for (Equipment equipment : roomtype.getEquipments()) {
				equipment.setHotelsusers_id(hotel.getUsers_id());
				equipment.setRoomtypes_id(roomtype.getId());
				sql.insert("insertHotelRoomsEquipments", equipment);
			}
			saveImage(sql, roomtype.getRoomImageField(), roomtype.getId(), false, hotel.getUsers_id());
		}
		sql.commit();
	}
	
	public static boolean roomSetup(RoomType roomtype, Hotel hotel, MySession session) throws Exception {
		SqlSession sql = MyBatisHelper.getSqlSessionFactory().openSession(false);
		try {
			long user_id = session.getUser().getId();
			for (Room room : roomtype.getRooms()) {
				room.setInitiator_user_id(user_id);
				room.setHotelsusers_id(hotel.getUsers_id());
				room.setRoomtypes_id(roomtype.getId());
				if (sql.update("updateRoom", room) == 0) {
					sql.insert("insertRoom", room);
				}
			}
			
			sql.delete("deleteHotelsRoomsConditions", roomtype);
			
			for (Condition condition : roomtype.getConditions()) {
				condition.setHotelsusers_id(hotel.getUsers_id());
				condition.setRoomtypes_id(roomtype.getId());
				sql.insert("insertHotelsRoomsConditions", condition);
			}
			
			sql.delete("deleteHotelRoomsEquipments", roomtype);
			
			for (Equipment equipment : roomtype.getEquipments()) {
				equipment.setHotelsusers_id(hotel.getUsers_id());
				equipment.setRoomtypes_id(roomtype.getId());
				sql.insert("insertHotelRoomsEquipments", equipment);
			}
			roomtype.setInitiator_user_id(user_id);
			sql.update("updateRoomTypesDescription", roomtype);
			
			saveImage(sql, roomtype.getRoomImageField(), roomtype.getId(), false, hotel.getUsers_id());
			sql.commit();
			return true;
		} catch (Exception e) {
			logger.error("Exception", e);
			sql.rollback();
		} finally {
			sql.close();
		}
		return false;
	}

	public static void saveImage(SqlSession sql, FileUploadField fileUploadField, long identify, boolean isHotel, long userId) {
		String link = null;
		if (fileUploadField != null) {
			try {
				for (FileUpload fileupload : fileUploadField.getFileUploads()) {
					SecureRandom secureRandom = new SecureRandom();
					String fileName = DateUtil.toString(new Date(), "yyyyMMddHHmmss") + "_"
							+ String.valueOf(secureRandom.nextLong()).replace("-", "") + "."
							+ FileUtil.getFileExtension(fileupload.getClientFileName());
					if ((link = FileUtil.saveFile(fileupload.getInputStream(), String.valueOf(userId), fileName)) != null) {
						UploadedFile file = new UploadedFile();
						file.setLink(link);
						file.setMime_type(CommonUtil.getImageMimeType(fileupload.getClientFileName()));
						if ((Integer) sql.insert("insertUploadFileDetail", file) > 0) {
							HashMap<String, Serializable> param = new HashMap<String, Serializable>();
							param.put("identify", identify);
							param.put("uploadedfiles_id", file.getId());
							if (isHotel)
								sql.insert("insertHotelImages", param);
							else
								sql.insert("insertHotelRoomTypeImages", param);
						}
					}
				}
			} catch (IOException e) {
				logger.error("IOException", e);
			}
		}
	}

	public static boolean cancelReserve(Long reserve_id, long user_id) {
		SqlSession sql = MyBatisHelper.getSqlSessionFactory().openSession(false);
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.clear();
			Long room_id = sql.selectOne("selectRoomIdByPendingReserveId", reserve_id);
			if (room_id != null) {
				param.clear();
				param.put("old_state", RoomState.OCCUPIED);
				param.put("new_state", RoomState.VACANT);
				param.put("initiator_user_id", user_id);
				param.put("id", room_id);
				sql.update("updateRoomState", param);
			}

			param.put("old_status", ReservationStatus.RESERVED);
			param.put("new_status", ReservationStatus.CANCELLED);
			param.put("id", reserve_id);
			param.put("initiator_user_id", user_id);
			sql.update("updateReservationChangeStatus", param);
			sql.commit();
			return true;
		} catch (Exception e) {
			logger.error("Exception", e);
			sql.rollback();
		} finally {
			sql.close();
		}
		return false;
	}
	
	public static boolean noShowReserve(Long reserve_id, long user_id) {
		SqlSession sql = MyBatisHelper.getSqlSessionFactory().openSession(false);
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.clear();
			Long room_id = sql.selectOne("selectRoomIdByPendingReserveId", reserve_id);
			if (room_id != null) {
				param.clear();
				param.put("old_state", RoomState.OCCUPIED);
				param.put("new_state", RoomState.VACANT);
				param.put("initiator_user_id", user_id);
				param.put("id", room_id);
				sql.update("updateRoomState", param);
			}
			
			param.put("old_status", ReservationStatus.RESERVED);
			param.put("new_status", ReservationStatus.NO_SHOW);
			param.put("id", reserve_id);
			param.put("initiator_user_id", user_id);
			sql.update("updateReservationChangeStatus", param);
			sql.commit();
			return true;
		} catch (Exception e) {
			logger.error("Exception", e);
			sql.rollback();
		} finally {
			sql.close();
		}
		return false;
	}

	public static boolean checkOutReserve(final ReservationDetail reserve, long initiator_user_id, MyFeedbackPanel feedback) {
		BundleUtil util = new BundleUtil();
		IModel<Locale> localeModel = new AbstractReadOnlyModel<Locale>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Locale getObject() {
				return new Locale("en");
			}
		};
		SqlSession sql = MyBatisHelper.getSqlSessionFactory().openSession(false);
		Hotel hotel;
		try {
			hotel = sql.selectOne("selectHotelByReserveId", reserve.getId());
			Bill bill = new Bill(reserve.getId(), reserve.getHotelsusers_id());
			bill.setInitiator_user_id(Long.parseLong(util.configValue("night_audit_user_id")));
			bill.setDescription(util.value("hotels.reservation.room.change", localeModel));
//			bill.setCredit(reserve.getRate());
			bill.setBill_date(hotel.getOperday());
			sql.insert("insertReservationBill", bill);

//			String meal_option = util.value("meal_options.bb.description", localeModel);
//			if (reserve.getMeal_options() == MealOption.HB_LUNCH) {
//				meal_option = util.value("meal_options.hb.lunch.description", localeModel);
//			} else if (reserve.getMeal_options() == MealOption.HB_DINNER) {
//				meal_option = util.value("meal_options.hb.dinner.description", localeModel);
//			} else if (reserve.getMeal_options() == MealOption.FB) {
//				meal_option = util.value("meal_options.fb.description", localeModel);
//			}

			BigDecimal meal_option_value = sql.selectOne("selectIssueBillMealOption", reserve);
			meal_option_value = CommonUtil.nvl(meal_option_value);
			
			ReservationRuleType rule = sql.selectOne("selectIssueBillReservationRule", reserve);

			int meal_count = reserve.getAdults();

			List<ChildAge> children_age_list = sql.selectList("selectIssueBillChildrenAgeList", reserve);

			for (ChildAge age : children_age_list) {
				if (age.getAge() > rule.getMinimum_free_age())
					meal_count += 1;
			}

//			bill.setDescription(meal_option);
			bill.setCredit(CommonUtil.nvl(meal_option_value).multiply(new BigDecimal(meal_count)));
			bill.setBill_date(hotel.getOperday());
			if (bill.getCredit().doubleValue() > 0)
				sql.insert("insertReservationBill", bill);

//			short extra_bed = CommonUtil.nvl(reserve.getExtra_bed());
//
//			if (extra_bed > 0) {
//				BigDecimal extra_bed_cost = new BigDecimal("0");
//				if (rule != null)
//					extra_bed_cost = new BigDecimal(extra_bed).multiply(new BigDecimal(rule.getExtra_bed_price_type_value()));
//				bill.setDescription(util.value("additional.bed", localeModel));
//				bill.setCredit(extra_bed_cost);
//				bill.setBill_date(hotel.getOperday());
//				sql.insert("insertReservationBill", bill);
//			}

			reserve.setInitiator_user_id(initiator_user_id);
			sql.update("updateReserveCheckOut", reserve);
			boolean check_out_normal = sql.selectOne("selectCheckOutNormal", reserve);
			if (!check_out_normal) {
				Double half_pinalty = sql.selectOne("selectCheckOutHalfPinalty", reserve);
				if (half_pinalty != null && half_pinalty > 0) {
					bill.setDescription(util.value("hotels.reservation.early_arrival_period_half", localeModel));
					bill.setCredit(new BigDecimal(half_pinalty));
					bill.setBill_date(hotel.getOperday());
					sql.insert("insertReservationBill", bill);
				} else {
					Double full_pinalty = sql.selectOne("selectCheckOutFullPinalty", reserve);
					if (full_pinalty != null && full_pinalty > 0) {
						bill.setDescription(util.value("hotels.reservation.early_arrival_period", localeModel));
						bill.setCredit(new BigDecimal(full_pinalty));
						bill.setBill_date(hotel.getOperday());
						sql.insert("insertReservationBill", bill);
					}
				}
			}
			sql.commit();
			return true;
		} catch (Exception e) {
			logger.error("Exception", e);
			sql.rollback();
			String s = e.getMessage();
			if (s.contains("Bill is exists")) {
				feedback.error(s.substring(s.indexOf("Bill is exists"), s.indexOf("Hint")));
			}
		} finally {
			sql.close();
		}
		return false;
	}

	public static void checkInReserve(final ReservationDetail reserve, Hotel hotel, final long initiator_user_id) {
		BundleUtil util = new BundleUtil();
		IModel<Locale> localeModel = new AbstractReadOnlyModel<Locale>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Locale getObject() {
				return new Locale("en");
			}
		};

		SqlSession sql = MyBatisHelper.getSqlSessionFactory().openSession(false);
		try {
			Bill bill = new Bill(reserve.getId(), reserve.getHotelsusers_id());
			bill.setInitiator_user_id(initiator_user_id);
			boolean check_in_normal = sql.selectOne("selectCheckInNormal", reserve);
			if (!check_in_normal) {
				Double half_pinalty = sql.selectOne("selectCheckInHalfPinalty", reserve);
				if (half_pinalty != null && half_pinalty > 0) {
					bill.setDescription(util.value("hotels.reservation.early_arrival_period_half", localeModel));
					bill.setCredit(new BigDecimal(half_pinalty));
					bill.setBill_date(hotel.getOperday());
					sql.insert("insertReservationBill", bill);
				} else {
					Double full_pinalty = sql.selectOne("selectCheckInFullPinalty", reserve);
					if (full_pinalty != null && full_pinalty > 0) {
						bill.setDescription(util.value("hotels.reservation.early_arrival_period", localeModel));
						bill.setCredit(new BigDecimal(full_pinalty));
						bill.setBill_date(hotel.getOperday());
						sql.insert("insertReservationBill", bill);
					}
				}
			}
		} catch (Exception e) {
			logger.error("Exception", e);
			sql.rollback();
		} finally {
			sql.close();
		}
	}

	public static String starDecorator(Integer stars, int maxStars, boolean forFilter) {
		String result = "<span data-toggle=\"tooltip\" data-placement=\"right\" data-original-title=\"" + new StringResourceModel("touragents.newbooking.hotel_class.stars" + stars, null).getString() + "\">";
		String enabledStart = "<i class=\"fa fa-star"+(forFilter ? " fa-lg" : "") + "\" style=\"padding-left: 1px; color: #ffd500;\"></i>";
		String disabledStart = "<i class=\"fa fa-star"+(forFilter ? " fa-lg" : "") + "\" style=\"padding-left: 1px; color: #e7e7e7;\"></i>";
		if (forFilter && (stars == 0 || stars == -1)) {
//			String s = "<span class=\"fa-stack\" style=\"width:1em; height:1em; line-height:1em;\">";
//			s += "<i class=\"fa fa-star fa-stack-1x\" style=\"font-size: 12px;padding-left: 1px; color: "+(stars == 0 ? "#ffd500" : "#e7e7e7")+";\"></i>";
//			s += "<i class=\"fa fa-ban fa-stack-1x text-danger\" style=\"font-size: 18px;\"></i>";
//			s += "</span>";
			result += new StringResourceModel("touragents.newbooking.hotel_class.stars" + stars, null).getString();
		} else {
			for (int i = 1; i <= maxStars; i++) {
				if (i <= stars) {
					result += enabledStart;
				} else {
					result += disabledStart;
				}
			}
		}
		return result + "</span>";
	}
	
	public static List<String> getTimeList() {
		List<String> result = new ArrayList<String>();
		
		for (int i = 0; i < 24; i++) {
			result.add((i >= 10 ? "" : "0") + i +":00");
		}
		
		return result;
	}
	
	public static int getReservationCount(Byte type, Byte status, Long touragentsusers_id, Long hotel_id, Byte userType) {
		Map<String, Serializable> params = new HashMap<String, Serializable>();
		params.put("reservation_type", type);
		params.put("status", status);
		params.put("creator_user_id", touragentsusers_id);
		params.put("hotel_id", hotel_id);
		params.put("user_type", userType);
		Integer result = new MyBatisHelper().selectOne("selectReservationsCountByTypeOrStatus", params);
		
		return result == null ? 0 : result;
	}
}
