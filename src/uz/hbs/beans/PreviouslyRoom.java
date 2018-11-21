package uz.hbs.beans;

import java.util.Date;

import org.apache.wicket.util.io.IClusterable;

public class PreviouslyRoom implements IClusterable {
	private static final long serialVersionUID = 1L;
	private Integer roomtypes_id;
	private Date check_in;
	private Date check_out;
	private Short guests;
	
	public PreviouslyRoom() {
	}
	
	public PreviouslyRoom(Integer roomtypes_id, Date check_in, Date check_out, Short guests) {
		this.roomtypes_id = roomtypes_id;
		this.check_in = check_in;
		this.check_out = check_out;
		this.guests = guests;
	}
	
	public PreviouslyRoom(IndividualReservation reserve, Short guests) {
		this.roomtypes_id = reserve.getRoomtypes_id();
		this.check_in = reserve.getCheck_in();
		this.check_out = reserve.getCheck_out();
		this.guests = guests;
	}
	
	public PreviouslyRoom(GroupReservation reserve, Integer roomtypes_id, Short guests) {
		this.roomtypes_id = roomtypes_id;
		this.check_in = reserve.getCheck_in();
		this.check_out = reserve.getCheck_out();
		this.guests = guests;
	}
	
	public PreviouslyRoom(IndividualReservation reserve, Integer roomtypes_id, short guest) {
		this.roomtypes_id = roomtypes_id;
		this.check_in = reserve.getCheck_in();
		this.check_out = reserve.getCheck_out();
		this.guests = guest;
	}

	public Integer getRoomtypes_id() {
		return roomtypes_id;
	}

	public void setRoomtypes_id(Integer roomtypes_id) {
		this.roomtypes_id = roomtypes_id;
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
