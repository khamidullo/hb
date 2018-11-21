package uz.hbs.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.StringResourceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uz.hbs.beans.IdAndName;
import uz.hbs.beans.rate.RateDetails;
import uz.hbs.beans.rate.RatePlane;
import uz.hbs.beans.rate.RateSeason;
import uz.hbs.db.MyBatisHelper;

public class RatePlaneUtil {
	private static final Logger _log = LoggerFactory.getLogger(RatePlaneUtil.class);
	public static final long ONE_DAY = 24 * 60 * 60 * 1000;
	
	public static RatePlane createRate(long hotelsusers_id){
		RatePlane plane = new RatePlane(hotelsusers_id);
		plane.getSeasonlist().add(createSeason(plane.getId(), hotelsusers_id, RateSeason.FIRST_SEASON));
		return plane;
	}
	
	public static RateSeason createSeason(Integer plane_id, long hotelsusers_id, short season_number) {
		RateSeason season = new RateSeason(plane_id, season_number);
		season.getDetails().put(season_number, getNewRateDetails(hotelsusers_id, season_number));
		return season;
	}
	
	public static HashMap<Integer, List<RateDetails>> getNewRateDetails(long hotelsusers_id, short season_number){
		HashMap<Integer, List<RateDetails>> result = new HashMap<Integer, List<RateDetails>>();
		List<Integer> roomtypelist = new MyBatisHelper().selectList("selectRatePlaneRoomTypeId", hotelsusers_id);
		for (Integer roomtype_id : roomtypelist) {
			List<RateDetails> list = new ArrayList<RateDetails>();
			short max_holding_capacity = new MyBatisHelper().selectOne("selectMaxHoldingCapacity", hotelsusers_id);
			for (short person = 1; person <= max_holding_capacity; person++){
				list.add(new RateDetails(season_number, roomtype_id, person, RateDetails.INDIVIDUAL));
			}
			for (short person = 1; person <= max_holding_capacity; person++){
				list.add(new RateDetails(season_number, roomtype_id, person, RateDetails.GROUP));
			}
			result.put(roomtype_id, list);
		}
		return result;
	}
	
	public static List<Short> personlist(long hotelsusers_id){
		HashMap<String, Serializable> param = new HashMap<String, Serializable>();
		param.put("hotelsusers_id", hotelsusers_id);
		List<Short> list = new ArrayList<Short>();
		short max_holding_capacity = new MyBatisHelper().selectOne("selectMaxHoldingCapacity", hotelsusers_id);
		for (short person = 1; person <= max_holding_capacity; person++){
			list.add(person);
		}
		for (short person = 1; person <= max_holding_capacity; person++){
			list.add(person);
		}
		return list;
	}
	
	public static List<IdAndName> getRoomTypeListByHotel(long hotelsusers_id) {
		return new MyBatisHelper().selectList("selectRatePlaneRoomType", hotelsusers_id);
	}
	
	public static List<RateDetails> getRateDetailList(List<RateDetails> list, short max_person){
		int count = list.size();
		for (short sh = 0; sh < max_person - count; sh++){
			list.add(new RateDetails((short)(list.size() + sh)));
		}
		return list;
	}
	
	private static boolean checkRatePlane(RatePlane plane){
		HashMap<String, Serializable> param = new HashMap<String, Serializable>();
		param.put("plane_id", plane.getId());
		param.put("hotelsusers_id", plane.getHotelsusers_id());
		param.put("internal", plane.isInternal());
		List<RateSeason> list = plane.getSeasonlist();
		param.put("season_from", ((RateSeason) list.get(0)).getSeason_from());
		param.put("season_to", ((RateSeason) list.get(list.size() - 1)).getSeason_to());
		return (Boolean) new MyBatisHelper().selectOne("selectCheckRatePlanePeriod", param);
	}
	
	public static boolean checkRatePeriod(RatePlane plane){
		HashMap<String, Serializable> param = new HashMap<String, Serializable>();
		List<RateSeason> list = plane.getSeasonlist();
		param.put("season_from", ((RateSeason) list.get(0)).getSeason_from());
		param.put("season_to", ((RateSeason) list.get(list.size() - 1)).getSeason_to());
		return checkRatePeriod((Date) param.get("season_from"), (Date) param.get("season_to"));
	}
	
	private static boolean checkRatePeriod(Date datefrom, Date dateto){
		if ((dateto.getTime() - datefrom.getTime()) <= 365 * ONE_DAY) return true;
		return false;
	}
	
	@SuppressWarnings({ "rawtypes" })
	public static boolean saveRatePlane(RatePlane plane, boolean isNew, final long initiator_user_id, FeedbackPanel feedback){
		if (! checkRatePeriod(plane)){
			feedback.warn(new StringResourceModel("hotels.rate.plane.outofperiod", null).getString());
			return false;
		}
		
		if (checkRatePlane(plane)) {
			plane.setInitiator_user_id(initiator_user_id);
			if (isNew) {
				SqlSession sql = MyBatisHelper.getSqlSessionFactory().openSession(false);
				try {
					sql.insert("insertRatePlane", plane);
					for (RateSeason season : plane.getSeasonlist()) {
						season.setRateplane_id(plane.getId());
						sql.insert("insertRatePlaneSeason", season);
						HashMap<Integer, List<RateDetails>> map = season.getDetails().get(season.getSeason_number());
						Iterator it = map.keySet().iterator();
						while (it.hasNext()) {
							List<RateDetails> list = map.get(it.next());
							for (RateDetails detail : list) {
								detail.setRateplaneseasons_id(season.getId());
								if (detail.isCorrect()) {
									if (sql.update("updateRateDetails2", detail) == 0)
									sql.insert("insertRateDetails", detail);
								}
							}
						}
					}
					sql.commit();
					feedback.success(new StringResourceModel("hotels.rate.plane.add.success", null).getString());
					return true;
				} catch (Exception e) {
					_log.error("Exception", e);
					sql.rollback();
					feedback.error(new StringResourceModel("hotels.rate.plane.add.fail", null).getString());
				} finally {
					sql.close();
				}
			} else {
				SqlSession sql = MyBatisHelper.getSqlSessionFactory().openSession(false);
				try {
					sql.update("updateRatePlane", plane);
					for (RateSeason season : plane.getSeasonlist()) {
						if (sql.update("updateRatePlaneSeason", season) == 0) {
							season.setRateplane_id(plane.getId());
							sql.insert("insertRatePlaneSeason", season);
						}
						HashMap<Integer, List<RateDetails>> map = season.getDetails().get(season.getSeason_number());
						Iterator it = map.keySet().iterator();
						while (it.hasNext()) {
							List<RateDetails> list = map.get(it.next());
							for (RateDetails detail : list) {
								detail.setRateplaneseasons_id(season.getId());
								if (detail.isCorrect()) {
									if (sql.update("updateRateDetails2", detail) == 0)
									sql.insert("insertRateDetails", detail);
								}
							}
						}
					}
					sql.commit();
					feedback.success(new StringResourceModel("hotels.rate.plane.edit.success", null).getString());
					return true;
				} catch (Exception e) {
					_log.error("Exception", e);
					sql.rollback();
					feedback.error(new StringResourceModel("hotels.rate.plane.edit.fail", null).getString());
				} finally {
					sql.close();
				}
			}
		} else {
			feedback.warn(new StringResourceModel("hotels.rate.plane.overlaps", null).getString());
		}
		return false;
	}
	
	public static RatePlane loadRatePlane(RatePlane plane){
		List<RateSeason> seasonlist = new MyBatisHelper().selectList("selectRateSeasonByPlaneId", plane.getId());
		plane.setSeasonlist(seasonlist);
		List<Integer> roomtypelist = new MyBatisHelper().selectList("selectRatePlaneRoomTypeId", plane.getHotelsusers_id());
		HashMap<String, Serializable> param = new HashMap<String, Serializable>();
		for (RateSeason season : seasonlist) {
			HashMap<Integer, List<RateDetails>> detailmap = new HashMap<Integer, List<RateDetails>>();
			param.put("rateplaneseasons_id", season.getId());
			for (Integer roomtype_id : roomtypelist) {
				short holding_capacity = new MyBatisHelper().selectOne("selectMaxHoldingCapacity", plane.getHotelsusers_id());
				param.put("roomtypes_id", roomtype_id);
				param.put("holding_capacity", holding_capacity);
				List<RateDetails> list = new MyBatisHelper().selectList("selectRatePlaneDetails", param);
				if (list.isEmpty()) {
					list = new ArrayList<RateDetails>();
					for (short person = 1; person <= holding_capacity; person++) {
						list.add(new RateDetails(season.getSeason_number(), roomtype_id, person, RateDetails.INDIVIDUAL));
					}
					for (short person = 1; person <= holding_capacity; person++) {
						list.add(new RateDetails(season.getSeason_number(), roomtype_id, person, RateDetails.GROUP));
					}
				} else {
					holding_capacity = new MyBatisHelper().selectOne("selectHoldingCapacityRoomType", roomtype_id);

					param.put("roomtypes_id", roomtype_id);
					param.put("holding_capacity", holding_capacity);
					list = new MyBatisHelper().selectList("selectRatePlaneDetails", param);
					
					List<RateDetails> indvllist = new ArrayList<RateDetails>();
					List<RateDetails> grouplist = new ArrayList<RateDetails>();
					for (RateDetails detail : list) {
						if (! detail.isIs_group()) {
							indvllist.add(detail);
						} else {
							grouplist.add(detail);
						}
					}
					
					if ((list.size() / 2) >= holding_capacity) holding_capacity = new MyBatisHelper().selectOne("selectMaxHoldingCapacity", plane.getHotelsusers_id());
					short size = (short) ((list.size() / 2) + 1);
					for (short person = size; person <= holding_capacity; person++) indvllist.add(new RateDetails(season.getSeason_number(), roomtype_id, person, RateDetails.INDIVIDUAL));
					for (short person = size; person <= holding_capacity; person++) grouplist.add(new RateDetails(season.getSeason_number(), roomtype_id, person, RateDetails.GROUP));
					list.clear();
					list.addAll(indvllist);
					list.addAll(grouplist);
				}
				detailmap.put(roomtype_id, list);
			}
			season.getDetails().put(season.getSeason_number(), detailmap);
		}
		return plane;
	}

}
