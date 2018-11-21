package uz.hbs.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class GroupReservation extends ReservationDetail {
	private static final long serialVersionUID = 1L;
	private List<RoomType> roomtypes;
	private HashMap<Integer, List<Room>> roomtype_map;
	
	public GroupReservation() {
		reservation_type = ReservationType.DEFINITE;
		status = new ReservationStatus(ReservationStatus.RESERVED);
		is_group = ReservationDetail.GROUP;
		auto_cancel_time = "23:59";
		setChildAgeList(new ArrayList<ChildAge>());
	}

	public GroupReservation(long hotelsusers_id) {
		reserverooms = new ArrayList<ReservationRoom>();
		reserverooms.add(new ReservationRoom());
		reservation_type = ReservationType.DEFINITE;
		status = new ReservationStatus(ReservationStatus.RESERVED);
		is_group = ReservationDetail.GROUP;
		check_in = new Date();
		children = 0;
		adults = 0;
		auto_cancel_time = "23:59";
		setChildAgeList(new ArrayList<ChildAge>());
		this.hotelsusers_id = hotelsusers_id;
	}
	
	public GroupReservation(ReservationDetail reserve) {
		id = reserve.getId();
		group_name = reserve.getGroup_name();
		reservation_type = reserve.getReservation_type();
		status = reserve.getStatus();
		is_group = reserve.isIs_group();
		check_in = reserve.getCheck_in();
		check_out = reserve.getCheck_out();
		adults = reserve.getAdults();
		children = reserve.getChildren();
		additional_bed = reserve.getAdditional_bed();
		guest_comments = reserve.getGuest_comments();
		reception_comments = reserve.getReception_comments();
		hotelsusers_id = reserve.getHotelsusers_id(); 
		total = reserve.getTotal();
		tour_agent = reserve.getTour_agent();
		auto_cancel_time = reserve.getAuto_cancel_time();
		rateplane = reserve.getRateplane();
		setResident(reserve.isResident());
		setPayment_owner(reserve.isPayment_owner());
		setInsurance(reserve.isInsurance());
		setInsuranceList(reserve.getInsuranceList());
	}
	
	public HashMap<Integer, List<Room>> getRoomtype_map() {
		return roomtype_map;
	}

	public void setRoomtype_map(HashMap<Integer, List<Room>> roomtype_map) {
		this.roomtype_map = roomtype_map;
	}

	public List<RoomType> getRoomtypes() {
		return roomtypes;
	}

	public void setRoomtypes(List<RoomType> roomtypes) {
		this.roomtypes = roomtypes;
	}

	public void init_roomtype(){
		roomtype_map = new HashMap<Integer, List<Room>>();
		roomtypes = new ArrayList<RoomType>();
		if (reserverooms.size() > 0){
			for (ReservationRoom obj : reserverooms){
				if (! roomtype_map.containsKey(obj.getRoomtype().getId())){
					List<Room> rooms = new ArrayList<Room>();
					rooms.add(obj.getRoom());
					roomtype_map.put(obj.getRoomtype().getId(), rooms);
					roomtypes.add(obj.getRoomtype());
				} else {
					List<Room> rooms = roomtype_map.get(obj.getRoomtype().getId());
					rooms.add(obj.getRoom());
					roomtype_map.put(obj.getRoomtype().getId(), rooms);
				}
			}
		}
	}
}
