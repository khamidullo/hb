package uz.hbs.beans;

import java.util.Date;

import org.apache.wicket.util.io.IClusterable;

public class PreviouslyRoom implements IClusterable {
	private static final long serialVersionUID = 1L;
	private Integer roomtype_id;
	private Date check_in;
	private Date check_out;
	private Short guests;
	
	public PreviouslyRoom() {
	}
	
	public PreviouslyRoom(Integer roomtype_id, Date check_in, Date check_out, Short guests) {
		this.roomtype_id = roomtype_id;
		this.check_in = check_in;
		this.check_out = check_out;
		this.guests = guests;
	}
	
	public PreviouslyRoom(IndividualReservation reserve, Short guests) {
		this.roomtype_id = reserve.getRoomtypes_id();
		this.check_in = reserve.getCheck_in();
		this.check_out = reserve.getCheck_out();
		this.guests = guests;
	}
	
	public PreviouslyRoom(GroupReservation reserve, Integer roomtype_id, Short guests) {
		this.roomtype_id = roomtype_id;
		this.check_in = reserve.getCheck_in();
		this.check_out = reserve.getCheck_out();
		this.guests = guests;
	}
	
	public PreviouslyRoom(IndividualReservation reserve, Integer roomtype_id, short guest) {
		this.roomtype_id = roomtype_id;
		this.check_in = reserve.getCheck_in();
		this.check_out = reserve.getCheck_out();
		this.guests = guest;
	}

	public Integer getRoomtypes_id() {
		return roomtype_id;
	}

	public void setRoomtypes_id(Integer roomtype_id) {
		this.roomtype_id = roomtype_id;
	}

	public Date getCheck_in() {
		return check_in;
	}

	public void setCheck_in(Date check_in) {
		this.check_in = check_in;
	}

	public Date getCheck_out() {
		return check_out;
	}

	public void setCheck_out(Date check_out) {
		this.check_out = check_out;
	}

	public Short getGuests() {
		return guests;
	}

	public void setGuests(Short guests) {
		this.guests = guests;
	}
}
