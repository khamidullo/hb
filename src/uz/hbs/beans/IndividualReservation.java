package uz.hbs.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import uz.hbs.db.MyBatisHelper;

public class IndividualReservation extends ReservationDetail {
	private static final long serialVersionUID = 1L;
	private RoomType roomtype;
	private Integer roomtypes_id;
	
	public IndividualReservation() {
		reservation_type = ReservationType.DEFINITE;
		status = new ReservationStatus(ReservationStatus.RESERVED);
		is_group = ReservationDetail.INDIVIDUAL;
		additional_bed = new AdditionalBed(AdditionalBed.INAPPLICABLE);
		auto_cancel_time = "23:59";
		setChildAgeList(new ArrayList<ChildAge>());
	}
	
	public IndividualReservation(long hotelsusers_id) {
		reservation_type = ReservationType.DEFINITE;
		status = new ReservationStatus(ReservationStatus.RESERVED);
		is_group = ReservationDetail.INDIVIDUAL;
		additional_bed = new AdditionalBed(AdditionalBed.INAPPLICABLE);
		check_in = new Date();
		children = 0;
		adults = 0;
		auto_cancel_time = "23:59";
		setChildAgeList(new ArrayList<ChildAge>());
		this.hotelsusers_id = hotelsusers_id; 
	}
	
	public IndividualReservation(ReservationDetail reserve) {
		this.id = reserve.getId();
		this.reservation_type = reserve.getReservation_type();
		this.status = reserve.getStatus();
		this.is_group = reserve.isIs_group();
		this.check_in = reserve.getCheck_in();
		this.check_out = reserve.getCheck_out();
		this.adults = reserve.getAdults();
		this.children = reserve.getChildren();
		this.additional_bed = reserve.getAdditional_bed();
		this.guest_comments = reserve.getGuest_comments();
		this.reception_comments = reserve.getReception_comments();
		this.hotelsusers_id = reserve.getHotelsusers_id(); 
		this.total = reserve.getTotal();
		this.tour_agent = reserve.getTour_agent();
		this.auto_cancel_time = reserve.getAuto_cancel_time();
		this.rateplane = reserve.getRateplane();
		this.ta_comments = reserve.ta_comments;
		this.number_of_nights = reserve.getNumber_of_nights();
		setResident(reserve.isResident());
		setPayment_owner(reserve.isPayment_owner());
		setInsurance(reserve.isInsurance());
		setInsuranceList(reserve.getInsuranceList());
		init();
	}
	
	public IndividualReservation(Integer roomtypes_id, Date check_in, Date check_out, Short adults) {
		reservation_type = ReservationType.DEFINITE;
		this.roomtypes_id = roomtypes_id;
		this.check_in = check_in;
		this.check_out = check_out;
		this.adults = adults;
	}

	public RoomType getRoomtype() {
		return roomtype;
	}

	public void setRoomtype(RoomType roomtype) {
		this.roomtype = roomtype;
	}

	public Integer getRoomtypes_id() {
		return roomtypes_id;
	}

	public void setRoomtypes_id(Integer roomtypes_id) {
		this.roomtypes_id = roomtypes_id;
	}
	
	private void init(){
		if (this.id != null) {
			List<ReservationRoom> list = new MyBatisHelper().selectList("selectReserveRoomList", this.id);
			if (list.size() == 1) {
				ReservationRoom reserveroom = list.get(0);
				roomtype = new RoomType(reserveroom.getRoomtype().getId());
				roomtypes_id = reserveroom.getRoomtype().getId();
			}
		}
	}
}
