package uz.hbs.jobs;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.ibatis.session.SqlSession;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uz.hbs.beans.Bill;
import uz.hbs.beans.ChildAge;
import uz.hbs.beans.Hotel;
import uz.hbs.beans.NightAuditLog;
import uz.hbs.beans.ReservationDetail;
import uz.hbs.beans.ReservationRuleType;
import uz.hbs.beans.ReservationStatus;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.utils.BundleUtil;

public class NightAuditProcess implements Runnable {
	private static final Logger _log = LoggerFactory.getLogger(NightAuditProcess.class);
	private Hotel hotel;
	
	public NightAuditProcess(Hotel hotel) {
		this.hotel = hotel;
	}

	@Override
	public void run() {
		List<ReservationDetail> reservlist = new MyBatisHelper().selectList("selectNightAuditRegisteredByHotelId", hotel);
		BundleUtil util = new BundleUtil();
		IModel<Locale> localeModel = new AbstractReadOnlyModel<Locale>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Locale getObject() {
				return new Locale("en");
			}
		};
		
		for (ReservationDetail reserve : reservlist){
			SqlSession sql = MyBatisHelper.getSqlSessionFactory().openSession(false);
			try {
				Bill bill = new Bill(reserve.getId(), reserve.getHotelsusers_id());
				bill.setInitiator_user_id(Long.parseLong(util.configValue("night_audit_user_id")));
				if (reserve.getCheck_in_day() == 0){ //Check_IN
					boolean check_in_normal = sql.selectOne("selectCheckInNormal", reserve);
					if (! check_in_normal){
						Double half_pinalty = sql.selectOne("selectCheckInHalfPinalty", reserve);
						if (half_pinalty != null && half_pinalty > 0){
							bill.setDescription(util.value("hotels.reservation.early_arrival_period_half", localeModel));
							bill.setCredit(new BigDecimal(half_pinalty));
							bill.setBill_date(hotel.getOperday());
							sql.insert("insertReservationBill", bill);
						} else {
							Double full_pinalty = sql.selectOne("selectCheckInFullPinalty", reserve);
							if (full_pinalty != null && full_pinalty > 0){
								bill.setDescription(util.value("hotels.reservation.early_arrival_period", localeModel));
								bill.setCredit(new BigDecimal(full_pinalty));
								bill.setBill_date(hotel.getOperday());
								sql.insert("insertReservationBill", bill);
							}
						}
					}
				}
				bill.setDescription(util.value("hotels.reservation.room.change", localeModel));
//				bill.setCredit(reserve.getRate());
				bill.setBill_date(hotel.getOperday());
				sql.insert("insertReservationBill", bill);
				
//				String meal_option = util.value("meal_options.bb.description", localeModel);
//				if (reserve.getMeal_options() == MealOption.HB_LUNCH){
//					meal_option = util.value("meal_options.hb.lunch.description", localeModel);
//				} else if (reserve.getMeal_options() == MealOption.HB_DINNER){
//					meal_option = util.value("meal_options.hb.dinner.description", localeModel);
//				} else if (reserve.getMeal_options() == MealOption.FB){
//					meal_option = util.value("meal_options.fb.description", localeModel);
//				}
				
				BigDecimal meal_option_value = sql.selectOne("selectIssueBillMealOption", reserve);
				
				ReservationRuleType rule = sql.selectOne("selectIssueBillReservationRule", reserve);
				
				if (meal_option_value != null) {
					int meal_count = reserve.getAdults();
					List<ChildAge> children_age_list = sql.selectList("selectIssueBillChildrenAgeList", reserve);
					for (ChildAge age : children_age_list) {
						if (age.getAge() > rule.getMinimum_free_age())
							meal_count += 1;
					}
//					bill.setDescription(meal_option);
					bill.setCredit(meal_option_value.multiply(new BigDecimal(meal_count)));
					bill.setBill_date(hotel.getOperday());
					sql.insert("insertReservationBill", bill);
				}
//				short extra_bed = CommonUtil.nvl(reserve.getExtra_bed());
//				
//				if (extra_bed > 0){
//					BigDecimal extra_bed_cost = new BigDecimal("0"); 
//					if (rule != null) extra_bed_cost = new BigDecimal(extra_bed).multiply(new BigDecimal(rule.getExtra_bed_price_type_value()));
//					bill.setDescription(util.value("additional.bed", localeModel));
//					bill.setCredit(extra_bed_cost);
//					bill.setBill_date(hotel.getOperday());
//					sql.insert("insertReservationBill", bill);
//				}
				if (reserve.getCheck_out_day() == 0) {
					reserve.setInitiator_user_id(Long.parseLong(util.configValue("night_audit_user_id")));
					if (sql.update("updateReserveCheckOutOneDay", reserve) == 0){
						new MyBatisHelper().insert("insertNightAuditLog", new NightAuditLog("CheckOut is not updated"));
					}
				}
				sql.update("updateHotelOperday", hotel);
				sql.commit();
			} catch (Exception e) {
				_log.error("Exception", e);
				sql.rollback();
			} finally {
				sql.close();
			}
		}
		reservlist = new MyBatisHelper().selectList("selectNightAuditReservedByHotelId", hotel);
		HashMap<String, Serializable> param = new HashMap<String, Serializable>();

		for (ReservationDetail reserve : reservlist){
			SqlSession sql = MyBatisHelper.getSqlSessionFactory().openSession(false);
			param.clear();
			param.put("old_status", ReservationStatus.RESERVED);
			param.put("new_status", ReservationStatus.CANCELLED);
			param.put("initiator_user_id", Integer.parseInt(util.configValue("night_audit_user_id")));
			param.put("id", reserve.getId());
			sql.update("updateReservationChangeStatus", param);
			
//			if (reserve.getRooms_id() != null){
//				param.clear();
//				param.put("old_state", RoomState.OCCUPIED);
//				param.put("new_state", RoomState.VACANT);
//				param.put("initiator_user_id", Integer.parseInt(util.configValue("night_audit_user_id")));
//				param.put("id", reserve.getRooms_id());
//				sql.update("updateReservationChangeOnSaleState", param);
//			}
		}
	}
}
