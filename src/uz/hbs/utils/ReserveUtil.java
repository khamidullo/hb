package uz.hbs.utils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.model.StringResourceModel;

import uz.hbs.beans.AdditionalServiceDetail;
import uz.hbs.beans.AdditionalServiceOrder;
import uz.hbs.beans.AdditionalServicePrice;
import uz.hbs.beans.ChildAge;
import uz.hbs.beans.Guest;
import uz.hbs.beans.IdAndName;
import uz.hbs.beans.IndividualReservation;
import uz.hbs.beans.MealOption;
import uz.hbs.beans.MyBean;
import uz.hbs.beans.PersonTitle;
import uz.hbs.beans.PriceType;
import uz.hbs.beans.RateSale;
import uz.hbs.beans.ReservationDetail;
import uz.hbs.beans.ReservationRoom;
import uz.hbs.beans.ReservationRuleType;
import uz.hbs.beans.ReservationStatus;
import uz.hbs.beans.RoomType;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.session.MySession;

public class ReserveUtil {
	public static final String EXTRA_BED = "EXTRA_BED";
	public static final String MEAL_LUNCH = "MEAL_LUNCH";
	public static final String MEAL_DINNER = "MEAL_DINNER";
	public static final String MEAL_LUNCH_DINNER = "MEAL_LUNCH_DINNER";
	public static final String CITY_VIEW = "CITY_VIEW";
	public static final String NO_SMOKER = "NO_SMOKER";

	public ReserveUtil() {
	}
	
	public static BigDecimal getGroupTotalAmount(ReservationDetail reserve, ReservationRuleType rule){
		BigDecimal total = new BigDecimal("0");
		BigDecimal totalrate = new BigDecimal("0");
		short adults = reserve.getAdults();
		for (ReservationRoom reserveroom : reserve.getReserverooms()){
			if (reserveroom.getRoom_count() != null) {
				RoomType roomtype = reserveroom.getRoomtype();
				HashMap<String, Serializable> param = new HashMap<String, Serializable>();
				param.put("hotel_id", reserve.getHotelsusers_id());
				param.put("roomtype_id", roomtype.getId());
				short holding_capacity = new MyBatisHelper().selectOne("selectHoldingCapacityRoomType", roomtype.getId());
				for (short sh = 1; sh <= reserveroom.getRoom_count(); sh++) {
					if (adults - holding_capacity > 0) {
						adults -= holding_capacity;
					} else
						holding_capacity = adults;
					
					reserveroom.setGuest_count(holding_capacity);
					
					param.put("person_number", holding_capacity);
					param.put("resident", reserve.isResident());
					param.put("check_in", reserve.getCheck_in());
					param.put("is_group", reserve.isIs_group());
					param.put("is_internal", false);

					RateSale ratesale = new MyBatisHelper().selectOne("selectRateSale", param);

					BigDecimal rate = new BigDecimal("0");

					if (ratesale != null) rate = CommonUtil.nvl(ratesale.getRate(reserve.getRateplane().isInternal()));

					if (!reserve.isResident()) {
						total = total.add(new BigDecimal(holding_capacity * rule.getcity_tax()));
					}
					totalrate = totalrate.add(rate);
				}
			} else return total; 
		}
		
		total = total.multiply(new BigDecimal(reserve.getNumber_of_nights()));
		total = total.add(getCheckInCharge(reserve, totalrate));
		total = total.add(getCheckOutCharge(reserve, totalrate));
		
		return total;
	}
	
	public static void recalcTAIndividualTotalAmount(ReservationDetail reserve, ReservationRuleType rule){
		reserve.setAdults((short) 0);
		reserve.setChildren((short) 0);
		reserve.setTotal(BigDecimal.valueOf(0));
		
		HashMap<String, Serializable> param = new HashMap<String, Serializable>();
		param.put("check_in", reserve.getCheck_in());
		param.put("check_out", reserve.getCheck_out());
		param.put("reserve_id", reserve.getId());
		
		for (ReservationRoom reserveroom : reserve.getReserverooms()) {//
			
			reserveroom.setRate(CommonUtil.nvl(getTAIndividualRate(reserve, reserveroom)));
			
			param.put("roomtype_id", reserveroom.getRoomtype().getId());

			Double d = 0d;
			if (reserveroom.getMeal_options() == 3) {
				Double d1 = CommonUtil.nvl((Double) new MyBatisHelper().selectOne("selectTAMealOptionValue",
						new MealOption(MealOption.LUNCH, reserve.getHotelsusers_id())));
				Double d2 = CommonUtil.nvl((Double) new MyBatisHelper().selectOne("selectTAMealOptionValue",
						new MealOption(MealOption.DINNER, reserve.getHotelsusers_id())));
				d = d1 + d2;
			} else {
				d = CommonUtil.nvl((Double) new MyBatisHelper().selectOne("selectTAMealOptionValue", new MealOption(
						reserveroom.getMeal_options(), reserve.getHotelsusers_id())));
			}
			reserveroom.setMeal_cost(BigDecimal.valueOf(d * reserve.getNumber_of_nights() * reserveroom.getAdults_count()));

			reserveroom.setTotal(reserveroom.getRate().add(reserveroom.getMeal_cost()));

			if (reserveroom.isExtra_bed_needed()) {
				reserveroom.setExtra_bed_cost(BigDecimal.valueOf(rule.getExtra_bed_price_type_value()
						* reserve.getNumber_of_nights()));
				reserveroom.setTotal(reserveroom.getTotal().add(reserveroom.getExtra_bed_cost()));
			}

			BigDecimal charge = ReserveUtil.getCheckInCharge(reserve, reserveroom.getRate());
			if (charge != null) {
				reserveroom.setTotal(reserveroom.getTotal().add(charge));
				reserveroom.setEarly_check_in_cost(charge);
				reserveroom.setCheck_in_rate(charge);
			}

			charge = ReserveUtil.getCheckOutCharge(reserve, reserveroom.getRate());
			if (charge != null) {
				reserveroom.setTotal(reserveroom.getTotal().add(charge));
				reserveroom.setLate_check_out_cost(charge);
				reserveroom.setCheck_out_rate(charge);
			}
			reserve.setAdults((short) (reserve.getAdults() + reserveroom.getAdults_count()));
			reserve.setTotal(reserve.getTotal().add(reserveroom.getTotal()));
			reserve.setChildren((short) (reserve.getChildren() + reserveroom.getChildren_count()));
		}
	}
	
	public static BigDecimal getTAIndividualTotalAmount(ReservationDetail reserve, ReservationRuleType rule){
		BigDecimal total = new BigDecimal("0");
		for (ReservationRoom reserveroom : reserve.getReserverooms()){
			RoomType roomtype = reserveroom.getRoomtype();
			HashMap<String, Serializable> param = new HashMap<String, Serializable>();
			param.put("hotel_id", reserve.getHotelsusers_id());
			param.put("roomtype_id", roomtype.getId());
			short holding_capacity = (short) reserveroom.getGuestlist().size();
			param.put("person_number", holding_capacity);
			param.put("resident",  reserve.isResident());
			param.put("check_in",  reserve.getCheck_in());
			param.put("check_out", reserve.getCheck_out());
			param.put("is_group",  reserve.isIs_group());
			
			BigDecimal extra_bed = new BigDecimal("0");
			if (reserveroom.isExtra_bed_needed()) {
				extra_bed = new BigDecimal(rule.getExtra_bed_price_type_value());
				if (rule.getExtra_bed_price_type() == PriceType.IN_PERCENT) {
					extra_bed = reserveroom.getRate().multiply(extra_bed).divide(BigDecimal.valueOf(100));
				}
			}
			
			BigDecimal meal_option_value = getMealAmount(reserve.getHotelsusers_id(), reserveroom.getMeal_options());
			meal_option_value = meal_option_value.multiply(new BigDecimal(holding_capacity)); //meal_count
		}
		BigDecimal checkInCharge = getCheckInCharge(reserve, null);
		
		total = total.add(checkInCharge);
		
		BigDecimal checkOutCharge = getCheckOutCharge(reserve, null);
		
		total = total.add(checkOutCharge);
		return total;
	}
	
	public static BigDecimal getTAIndividualRate(ReservationDetail reserve, ReservationRuleType rule, Integer roomtype_id){
		BigDecimal total = new BigDecimal("0");
		for (ReservationRoom reserveroom : reserve.getReserverooms()){
			RoomType roomtype = reserveroom.getRoomtype();
			if (roomtype.getId() == roomtype_id) {
				HashMap<String, Serializable> param = new HashMap<String, Serializable>();
				param.put("hotel_id", reserve.getHotelsusers_id());
				param.put("roomtype_id", roomtype.getId());
				param.put("person_number", (short)(reserveroom.getHolding_capacity() - (reserveroom.isExtra_bed_needed()? 1 : 0)));
				param.put("resident", reserve.isResident());
				param.put("check_in", reserve.getCheck_in());
				param.put("check_out", reserve.getCheck_out());
				param.put("is_group", reserve.isIs_group());
				param.put("internal", false);
				BigDecimal rate = new MyBatisHelper().selectOne("selectRateSaleSum", param);
				if (rate != null) total = total.add(rate);
			}
		}	
		return total;
	}
	
	public static BigDecimal getTAIndividualRate(ReservationDetail reserve, ReservationRoom reserveroom){
		RoomType roomtype = reserveroom.getRoomtype();
		HashMap<String, Serializable> param = new HashMap<String, Serializable>();
		param.put("hotel_id", reserve.getHotelsusers_id());
		param.put("roomtype_id", roomtype.getId());
		if (reserveroom.isExtra_bed_needed()) param.put("person_number", (short) (reserveroom.getAdults_count() - 1));
		else param.put("person_number", reserveroom.getAdults_count());
		param.put("resident", reserve.isResident());
		param.put("check_in", reserve.getCheck_in());
		param.put("check_out", reserve.getCheck_out());
		param.put("is_group", reserve.isIs_group());
		param.put("internal", false);
		BigDecimal rate = new MyBatisHelper().selectOne("selectRateSaleSum", param);
		return rate;
	}
	
	public static RateSale getIndividualReserveRate(IndividualReservation reserve) {
		if (reserve.getRoomtype() != null) {
			HashMap<String, Serializable> param = new HashMap<String, Serializable>();
			param.put("hotel_id", reserve.getHotelsusers_id());
			param.put("person_number", reserve.getAdults());
			param.put("resident", reserve.isResident());
			param.put("check_in", reserve.getCheck_in());
			param.put("roomtype_id", reserve.getRoomtype().getId());
			param.put("is_group", reserve.isIs_group());
			param.put("is_internal", false);
			return (RateSale) new MyBatisHelper().selectOne("selectRateSale", param);
		} 
		return null;
	}
	
	public static BigDecimal getCheckInCharge(ReservationDetail reserve, BigDecimal rate) {
		HashMap<String, Serializable> param = new HashMap<String, Serializable>();
		param.put("hotel_id", reserve.getHotelsusers_id());
		param.put("is_group", reserve.isIs_group());
		param.put("check_in", reserve.getCheck_in());
		param.put("rate", rate);
		return (BigDecimal) new MyBatisHelper().selectOne("getCheckInCharge", param);
	}
	
	public static BigDecimal getCheckInCharge(long hotel_id, Date check_in, boolean is_group, BigDecimal rate) {
		HashMap<String, Serializable> param = new HashMap<String, Serializable>();
		param.put("hotel_id", hotel_id);
		param.put("is_group", is_group);
		param.put("check_in", check_in);
		param.put("rate", rate);
		return (BigDecimal) new MyBatisHelper().selectOne("getCheckInCharge", param);
	}
	
	public static BigDecimal getCheckOutCharge(ReservationDetail reserve, BigDecimal rate) {
		HashMap<String, Serializable> param = new HashMap<String, Serializable>();
		param.put("hotel_id", reserve.getHotelsusers_id());
		param.put("is_group", reserve.isIs_group());
		param.put("check_out", reserve.getCheck_out());
		param.put("rate", rate);
		return (BigDecimal) new MyBatisHelper().selectOne("getCheckOutCharge", param);
	}
	
	public static BigDecimal getCheckOutCharge(long hotel_id, Date check_out, boolean is_group, BigDecimal rate) {
		HashMap<String, Serializable> param = new HashMap<String, Serializable>();
		param.put("hotel_id", hotel_id);
		param.put("is_group", is_group);
		param.put("check_out", check_out);
		param.put("rate", rate);
		return (BigDecimal) new MyBatisHelper().selectOne("getCheckOutCharge", param);
	}
	
	public static BigDecimal getChildrenPrice(ReservationDetail reserve, ReservationRuleType rule, BigDecimal rate){
		BigDecimal total = new BigDecimal("0");
		for (ChildAge age : reserve.getChildAgeList()){
			if (age.getAge() > rule.getMinimum_free_age()) {
				if (rule.getMaximum_discount_age() != null && age.getAge() <= rule.getMaximum_discount_age()) {
					BigDecimal discount;
					if (rule.getMaximum_discount_age_type() == PriceType.IN_PERCENT) {
						discount = rate.multiply(new BigDecimal(rule.getMaximum_discount_age_value())).divide(BigDecimal.valueOf(100));
					} else {
						discount = new BigDecimal(rule.getMaximum_discount_age_value());
					}
					total = total.add(discount);
				}
			}
		}
		return total;
	}
	
	public static BigDecimal getIdentifyingChildAge(short age, ReservationRuleType rule, BigDecimal rate, short person_number){
		if (age > rule.getMinimum_free_age()) {
			if (rule.getMaximum_discount_age() != null && age <= rule.getMaximum_discount_age()) {
				BigDecimal discount;
				if (rule.getMaximum_discount_age_type() == PriceType.IN_PERCENT) {
					discount = rate.divide(BigDecimal.valueOf(person_number)).multiply(new BigDecimal(rule.getMaximum_discount_age_value())).divide(BigDecimal.valueOf(100));
				} else {
					discount = new BigDecimal(rule.getMaximum_discount_age_value());
				}
				return discount;
			} else return rate;
		}
		return BigDecimal.valueOf(0);
	}
	
	public static BigDecimal getRateWithDiscount(ReservationDetail reserve, ReservationRoom reserveroom, ReservationRuleType rule){
		BigDecimal result = new BigDecimal("0");
		BigDecimal rate = getTAIndividualRate(reserve, reserveroom);
		int guests = reserveroom.getGuestlist().size();
		Double[] rates = new Double[guests + 1];
		for (int g = 1; g <= guests; g++){
			rates[g] = rate.divide(BigDecimal.valueOf(guests)).doubleValue();
			Guest guest = reserveroom.getGuestlist().get(g - 1);
			if (guest.getPerson_title().getTitle().equals(PersonTitle.CHILD)){
				if (rule.getMaximum_discount_age() != null && guest.getChildAge().getAge() <= rule.getMaximum_discount_age()) {
					if (rule.getMaximum_discount_age_type() == PriceType.IN_PERCENT) {
						rates[g] = (rates[g] * rule.getMaximum_discount_age_value())/100;
					} else {
						rates[g] = (double) rule.getMaximum_discount_age_value();
					}
				}
			}
			result = result.add(BigDecimal.valueOf(rates[g]));
		}
		return result;
	}
	
	
	@SuppressWarnings("unused")
	private static short countChildAge(List<ChildAge> list, short min_age){
		short result = 0;
		if (list != null) {
			for (ChildAge age : list) {
				if (age.getAge() > min_age)
					result += 1;
			}
		}
		return result;
	}
	
	private static BigDecimal getMealAmount(long hotel_id, byte meal_options){
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("hotel_id", hotel_id);
		param.put("meal_type", meal_options);
		BigDecimal result =  new MyBatisHelper().selectOne("selectReservationMealOption", param);
		return CommonUtil.nvl(result);
	}
	
	public static IndividualReservation getIndividualReserve(ReservationDetail reserve){
		IndividualReservation result = new IndividualReservation(reserve);
		List<ReservationRoom> reserveroomlist = new MyBatisHelper().selectList("selectReserveRoomListById", reserve.getId());
		result.setReserverooms(reserveroomlist);
		for (ReservationRoom reserveroom: reserveroomlist) {
			List<Guest> guestlist = new MyBatisHelper().selectList("selectGuestListByReserveRoomId", reserveroom.getId());
			if (reserveroom.getHolding_capacity() > guestlist.size()) {
				for (int guest = guestlist.size() + 1; guest <= reserveroom.getHolding_capacity(); guest++){
					guestlist.add(new Guest((short) guest));
				}
			}
			reserveroom.setGuestlist(guestlist);
			reserveroom.setGuest_count((short) reserveroom.getGuestlist().size());
		}
		return result;
	}
	
	public static List<RoomType> getRoomTypesByReserve(ReservationDetail reserve){
		HashMap<String, RoomType> roomtypemap = new HashMap<String, RoomType>();
		List<RoomType> list = new ArrayList<RoomType>();
		for (ReservationRoom reserveroom : reserve.getReserverooms()){
			RoomType roomtype = reserveroom.getRoomtype();
			if (roomtypemap.containsKey(roomtype.getName())) {
				RoomType temp =  ((RoomType) roomtypemap.get(roomtype.getName()));
				temp.setNumber_of_rooms(temp.getNumber_of_rooms() + 1);
				temp.setRoom_rate(temp.getRoom_rate().add(reserveroom.getRate()));
				temp.setCheck_in_rate(temp.getCheck_in_rate().add(reserveroom.getCheck_in_rate()));
				temp.setCheck_out_rate(temp.getCheck_out_rate().add(reserveroom.getCheck_out_rate()));
			} else roomtypemap.put(roomtype.getName(), new RoomType(roomtype.getId(), 1, roomtype.getName(), reserveroom.getRate(), reserveroom.getCheck_in_rate(), reserveroom.getCheck_out_rate()));
		}
		
		Iterator<String> iterator = roomtypemap.keySet().iterator();
		while (iterator.hasNext()){
			String key = iterator.next();
			list.add(roomtypemap.get(key));
		}
		return list;
	}
	
	public static List<IdAndName> getAdditionalServicesByReserve(ReservationDetail reserve){
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		List<IdAndName> list = new ArrayList<IdAndName>();
		for (ReservationRoom reserveroom : reserve.getReserverooms()){
			if (reserveroom.isExtra_bed_needed()) {
				if (map.containsKey(EXTRA_BED)) {
					map.put(EXTRA_BED, map.get(EXTRA_BED) + 1);
				} else {
					map.put(EXTRA_BED, 1);
				}
			}
			if (reserveroom.getMeal_options() > 0) {
				if (reserveroom.getMeal_options() == MealOption.HB_LUNCH) {
					if (map.containsKey(MEAL_LUNCH)) {
						map.put(MEAL_LUNCH, map.get(MEAL_LUNCH) + 1);
					} else {
						map.put(MEAL_LUNCH, 1);
					}
				} else if (reserveroom.getMeal_options() == MealOption.HB_DINNER) {
					if (map.containsKey(MEAL_DINNER)) {
						map.put(MEAL_DINNER, map.get(MEAL_DINNER) + 1);
					} else {
						map.put(MEAL_DINNER, 1);
					}
				} else if (reserveroom.getMeal_options() == MealOption.FB) {
					if (map.containsKey(MEAL_LUNCH_DINNER)) {
						map.put(MEAL_LUNCH_DINNER, map.get(MEAL_LUNCH_DINNER) + 1);
					} else {
						map.put(MEAL_LUNCH_DINNER, 1);
					}
				}
			}
		}
		
		Iterator<String> iterator = map.keySet().iterator();
		while (iterator.hasNext()){
			String key = iterator.next();
			list.add(new IdAndName(map.get(key), key));	
		}
		return list;
	}
	
	public static List<MyBean> getOtherAdditionalServicesByReserve(ReservationDetail reserve){
		AdditionalServicePrice price = new MyBatisHelper().selectOne("selectCurrentAdditionalServicePrice");
		HashMap<String, MyBean> map = new HashMap<String, MyBean>();
		List<MyBean> list = new ArrayList<MyBean>();
		if (price != null && reserve.isInsurance()) map.put(AdditionalServicePrice.Insurance, new MyBean(CommonUtil.nvl(price.getInsurance()) * ((short) (reserve.getNumber_of_nights() + 1)) * reserve.getAdults(), reserve.getAdults()));
		
		if (reserve.getArrival().getTransport_type() != AdditionalServiceDetail.TRANSPORT_TYPE_UNKNOWN){
			if (reserve.getArrival().isTaxi_order()) {
				map.put(AdditionalServicePrice.ArrivalTransfer, new MyBean(CommonUtil.nvl(reserve.getArrival().getTaxi_order_cost()), reserve.getArrival().getTaxi_order_car()));
			}
			if (reserve.getArrival().getAir_service_type() != AdditionalServiceDetail.AIR_SERVICE_TYPE_UNKNOWN) {
				if (reserve.getArrival().getAir_service_type() == AdditionalServiceDetail.AIR_SERVICE_TYPE_GREEN_HALL) {
					map.put(AdditionalServicePrice.ArrivalAirServiceTypeGreenHall, new MyBean(reserve.getAdults() * price.getArrival_air_green_hall(), reserve.getAdults()));
				} else if (reserve.getArrival().getAir_service_type() == AdditionalServiceDetail.AIR_SERVICE_TYPE_VIP_HALL) {
					map.put(AdditionalServicePrice.ArrivalAirServiceTypeVipHall, new MyBean(reserve.getAdults() * price.getArrival_air_vip_hall(), reserve.getAdults()));
				}
			}
		}
		if (reserve.getDeparture().getTransport_type() != AdditionalServiceDetail.TRANSPORT_TYPE_UNKNOWN){
			if (reserve.getDeparture().isTaxi_order()) {
				map.put(AdditionalServicePrice.DepartureTransfer, new MyBean(CommonUtil.nvl(reserve.getDeparture().getTaxi_order_cost()), reserve.getDeparture().getTaxi_order_car()));
			}
			if (reserve.getDeparture().getAir_service_type() != AdditionalServiceDetail.AIR_SERVICE_TYPE_UNKNOWN) {
				if (reserve.getDeparture().getAir_service_type() == AdditionalServiceDetail.AIR_SERVICE_TYPE_GREEN_HALL) {
					map.put(AdditionalServicePrice.DepartureAirServiceTypeGreenHall, new MyBean(reserve.getAdults() * price.getDeparture_air_green_hall(), reserve.getAdults()));
				} else if (reserve.getDeparture().getAir_service_type() == AdditionalServiceDetail.AIR_SERVICE_TYPE_VIP_HALL) {
					map.put(AdditionalServicePrice.DepartureAirServiceTypeVipHall, new MyBean(reserve.getAdults() * price.getDeparture_air_vip_hall(), reserve.getAdults()));
				}
			}
		}
		Iterator<String> iterator = map.keySet().iterator();
		while (iterator.hasNext()){
			String key = iterator.next();
			MyBean bean = map.get(key);
			bean.setType(key);
			list.add(bean);	
		}
		return list;
	}
	
	public static List<MyBean> getOtherAdditionalServicesByReserve(AdditionalServiceOrder reserve){
		AdditionalServicePrice price = new MyBatisHelper().selectOne("selectCurrentAdditionalServicePrice");
		HashMap<String, MyBean> map = new HashMap<String, MyBean>();
		List<MyBean> list = new ArrayList<MyBean>();
		if (price != null && reserve.isInsurance()) map.put(AdditionalServicePrice.Insurance, new MyBean(CommonUtil.nvl(price.getInsurance()) * (reserve.getDays() * reserve.getPerson()), reserve.getPerson()));
		
		if (reserve.getArrival().getTransport_type() != AdditionalServiceDetail.TRANSPORT_TYPE_UNKNOWN){
			if (reserve.getArrival().isTaxi_order()) {
				map.put(AdditionalServicePrice.ArrivalTransfer, new MyBean(CommonUtil.nvl(reserve.getArrival().getTaxi_order_cost()), reserve.getArrival().getTaxi_order_car()));
			}
			if (reserve.getArrival().getAir_service_type() != AdditionalServiceDetail.AIR_SERVICE_TYPE_UNKNOWN) {
				if (reserve.getArrival().getAir_service_type() == AdditionalServiceDetail.AIR_SERVICE_TYPE_GREEN_HALL) {
					map.put(AdditionalServicePrice.ArrivalAirServiceTypeGreenHall, new MyBean(reserve.getArrival().getPerson() * price.getArrival_air_green_hall(), reserve.getArrival().getPerson()));
				} else if (reserve.getArrival().getAir_service_type() == AdditionalServiceDetail.AIR_SERVICE_TYPE_VIP_HALL) {
					map.put(AdditionalServicePrice.ArrivalAirServiceTypeVipHall, new MyBean(reserve.getArrival().getPerson() * price.getArrival_air_vip_hall(), reserve.getArrival().getPerson()));
				}
			}
		}
		if (reserve.getDeparture().getTransport_type() != AdditionalServiceDetail.TRANSPORT_TYPE_UNKNOWN){
			if (reserve.getDeparture().isTaxi_order()) {
				map.put(AdditionalServicePrice.DepartureTransfer, new MyBean(CommonUtil.nvl(reserve.getDeparture().getTaxi_order_cost()), reserve.getDeparture().getTaxi_order_car()));
			}
			if (reserve.getDeparture().getAir_service_type() != AdditionalServiceDetail.AIR_SERVICE_TYPE_UNKNOWN) {
				if (reserve.getDeparture().getAir_service_type() == AdditionalServiceDetail.AIR_SERVICE_TYPE_GREEN_HALL) {
					map.put(AdditionalServicePrice.DepartureAirServiceTypeGreenHall, new MyBean(reserve.getDeparture().getPerson() * price.getDeparture_air_green_hall(), reserve.getDeparture().getPerson()));
				} else if (reserve.getDeparture().getAir_service_type() == AdditionalServiceDetail.AIR_SERVICE_TYPE_VIP_HALL) {
					map.put(AdditionalServicePrice.DepartureAirServiceTypeVipHall, new MyBean(reserve.getDeparture().getPerson() * price.getDeparture_air_vip_hall(), reserve.getDeparture().getPerson()));
				}
			}
		}
		Iterator<String> iterator = map.keySet().iterator();
		while (iterator.hasNext()){
			String key = iterator.next();
			MyBean bean = map.get(key);
			bean.setType(key);
			list.add(bean);	
		}
		return list;
	}
	
	public static BigDecimal calcOtherAdditionalServicesByReserve(ReservationDetail reserve){
		BigDecimal total = new BigDecimal("0");
		AdditionalServicePrice price = new MyBatisHelper().selectOne("selectCurrentAdditionalServicePrice");
		if (price != null && reserve.isInsurance()) total = total.add(BigDecimal.valueOf(CommonUtil.nvl(price.getInsurance()) * reserve.getNumber_of_nights() * reserve.getAdults()));
		
		if (reserve.getArrival().getTransport_type() != AdditionalServiceDetail.TRANSPORT_TYPE_UNKNOWN){
			if (reserve.getArrival().isTaxi_order()) {
				total = total.add(BigDecimal.valueOf(CommonUtil.nvl(reserve.getArrival().getTaxi_order_cost())));
			}
			if (reserve.getArrival().getAir_service_type() != -1) {
				total = total.add(BigDecimal.valueOf(reserve.getArrival().getAir_service_value()));
			}
		}
		if (reserve.getDeparture().getTransport_type() != AdditionalServiceDetail.TRANSPORT_TYPE_UNKNOWN){
			if (reserve.getDeparture().isTaxi_order()) {
				total = total.add(BigDecimal.valueOf(CommonUtil.nvl(reserve.getDeparture().getTaxi_order_cost())));
			}
			if (reserve.getDeparture().getAir_service_type() != -1) {
				total = total.add(BigDecimal.valueOf(reserve.getDeparture().getAir_service_value()));
			}
		}
		return total;
	}
	
	public static BigDecimal calcOtherAdditionalServicesByOrder(AdditionalServiceOrder reserve){
		BigDecimal total = new BigDecimal("0");
		AdditionalServicePrice taxiorder = new MyBatisHelper().selectOne("selectCurrentAdditionalServicePrice");
		if (reserve.isInsurance()) total = total.add(BigDecimal.valueOf(CommonUtil.nvl(taxiorder.getInsurance()) * reserve.getDays() * reserve.getPerson()));
		
		if (reserve.getArrival().getTransport_type() != AdditionalServiceDetail.TRANSPORT_TYPE_UNKNOWN){
			if (reserve.getArrival().isTaxi_order()) {
				total = total.add(BigDecimal.valueOf(CommonUtil.nvl(reserve.getArrival().getTaxi_order_cost())));
			}
			if (reserve.getArrival().getAir_service_type() != -1) {
				total = total.add(BigDecimal.valueOf(reserve.getArrival().getAir_service_value()));
			}
		}
		if (reserve.getDeparture().getTransport_type() != AdditionalServiceDetail.TRANSPORT_TYPE_UNKNOWN){
			if (reserve.getDeparture().isTaxi_order()) {
				total = total.add(BigDecimal.valueOf(CommonUtil.nvl(reserve.getDeparture().getTaxi_order_cost())));
			}
			if (reserve.getDeparture().getAir_service_type() != -1) {
				total = total.add(BigDecimal.valueOf(reserve.getDeparture().getAir_service_value()));
			}
		}
		return total;
	}
	

	public static BigDecimal getAdditionalServicesAmountByReserve(ReservationDetail reserve, ReservationRuleType rule, String type, MySession session){
		if (type.equals(EXTRA_BED)) {
			if (rule.getExtra_bed_price_type() == PriceType.FIXED_AMOUNT) {
				return BigDecimal.valueOf(rule.getExtra_bed_price_type_value());
			}
		} else if (type.equals(MEAL_LUNCH)) {
			Double d = CommonUtil.nvl((Double) new MyBatisHelper().selectOne("selectTAMealOptionValue", new MealOption(MealOption.LUNCH, reserve.getHotelsusers_id())));
			d = d * ReserveUtil.calcMealOption(reserve.getCheck_in(), reserve.getCheck_out(), MealOption.LUNCH, 1d);
			return BigDecimal.valueOf(d);
		} else if (type.equals(MEAL_DINNER)) {
			Double d = CommonUtil.nvl((Double) new MyBatisHelper().selectOne("selectTAMealOptionValue", new MealOption(MealOption.DINNER, reserve.getHotelsusers_id())));
			d = d * ReserveUtil.calcMealOption(reserve.getCheck_in(), reserve.getCheck_out(), MealOption.DINNER, 1d);
			return BigDecimal.valueOf(d);
		} else if (type.equals(MEAL_LUNCH_DINNER)) {
			Double d1 = CommonUtil.nvl((Double) new MyBatisHelper().selectOne("selectTAMealOptionValue", new MealOption(MealOption.DINNER, reserve.getHotelsusers_id())));
			d1 = d1 * ReserveUtil.calcMealOption(reserve.getCheck_in(), reserve.getCheck_out(), MealOption.LUNCH, 1d);
			Double d2 = CommonUtil.nvl((Double) new MyBatisHelper().selectOne("selectTAMealOptionValue", new MealOption(MealOption.LUNCH, reserve.getHotelsusers_id())));
			d2 = d2 * ReserveUtil.calcMealOption(reserve.getCheck_in(), reserve.getCheck_out(), MealOption.DINNER, 1d);
			return BigDecimal.valueOf(d1 + d2);
		}
		return BigDecimal.valueOf(0);
	}
	
	public static List<IdAndName> getTaxByReserve(IndividualReservation reserve){
		List<IdAndName> list = new ArrayList<IdAndName>();
		list.add(new IdAndName(reserve.getAdults(), new StringResourceModel("hotels.reservation.details.bill.details.city_tax", null).getString()));
		return list;
	}
	
	public static List<String> getPreferencesByReserve(ReservationDetail reserve){
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		List<String> list = new ArrayList<String>();
		for (ReservationRoom reserveroom : reserve.getReserverooms()){
			if (reserveroom.isCity_view()) {
				if (! map.containsKey(CITY_VIEW)) {
					map.put(CITY_VIEW, 1);
				}
			}
			if (reserveroom.isNon_smokers()) {
				if (! map.containsKey(NO_SMOKER)) {
					map.put(NO_SMOKER, 1);
				}
			}
		}
		Iterator<String> iterator = map.keySet().iterator();
		while (iterator.hasNext()){
			list.add(iterator.next());
		}
		return list;
	}
	
	public static String getReservationStatusText(Byte status) {
		switch (status) {
			case ReservationStatus.RESERVED:
				return new StringResourceModel("hotels.reservation.details.reservation.status.reserved", null).getString();
			case ReservationStatus.NO_SHOW:
				return new StringResourceModel("hotels.reservation.details.reservation.status.no_show", null).getString();
			case ReservationStatus.CANCELLED:
				return new StringResourceModel("hotels.reservation.details.reservation.status.cancelled", null).getString();
			case ReservationStatus.CHECKED_IN:
				return new StringResourceModel("hotels.reservation.details.reservation.status.checked.in", null).getString();
			case ReservationStatus.CHECKED_OUT:
				return new StringResourceModel("hotels.reservation.details.reservation.status.checked.out", null).getString();
			default:
				return new StringResourceModel("unkhown", null).getString();
		}
	}
	
	
	public static float calcMealOption(Date check_in, Date check_out, byte mo_type, double mo_value){
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("check_in", check_in);
		param.put("check_out", check_out);
		param.put("mo_type", mo_type);
		param.put("mo_value", mo_value);
		return CommonUtil.nvl((Float)new MyBatisHelper().selectOne("selectCalcMealOption", param));
	}
	
	public static void main(String[] args) {
		getIndividualReserve(new ReservationDetail(195l));
	}
}
