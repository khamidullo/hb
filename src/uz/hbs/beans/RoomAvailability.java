package uz.hbs.beans;

import java.util.Date;

import org.apache.wicket.util.io.IClusterable;

public class RoomAvailability implements IClusterable {
	private static final long serialVersionUID = 1L;
	private Date date;
	private short max_rooms;
	private short under_repair;
	private short closed;
	private short available;
	private short reservations;
	private short definitive;
	private short tentative;
	private short availability;
	private float load;
	private short adults;
	private short children;
	private short guests_leave;
	private short guests_arrive;
	
	public RoomAvailability() {
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public short getMax_rooms() {
//		setMax_rooms((short)(available + under_repair + closed));
		return max_rooms;
	}

	public void setMax_rooms(short max_rooms) {
		this.max_rooms = max_rooms;
	}

	public short getUnder_repair() {
		return under_repair;
	}

	public void setUnder_repair(short under_repair) {
		this.under_repair = under_repair;
	}

	public short getClosed() {
		return closed;
	}

	public void setClosed(short closed) {
		this.closed = closed;
	}

	public short getAvailable() {
		return available;
	}

	public void setAvailable(short available) {
		this.available = available;
	}

	public short getReservations() {
		setReservations((short) (definitive + tentative));
		return reservations;
	}

	public void setReservations(short reservations) {
		this.reservations = reservations;
	}

	public short getDefinitive() {
		return definitive;
	}

	public void setDefinitive(short definitive) {
		this.definitive = definitive;
	}

	public short getTentative() {
		return tentative;
	}

	public void setTentative(short tentative) {
		this.tentative = tentative;
	}

	public short getAvailability() {
		setAvailability((short)(max_rooms - reservations)); 
		return availability;
	}

	public void setAvailability(short availability) {
		this.availability = availability;
	}

	public float getLoad() {
		setLoad(100 - ((availability * 100) / max_rooms));
		return load;
	}

	public void setLoad(float load) {
		this.load = load;
	}

	public short getAdults() {
		return adults;
	}

	public void setAdults(short adults) {
		this.adults = adults;
	}

	public short getChildren() {
		return children;
	}

	public void setChildren(short children) {
		this.children = children;
	}

	public short getGuests_leave() {
		return guests_leave;
	}

	public void setGuests_leave(short guests_leave) {
		this.guests_leave = guests_leave;
	}

	public short getGuests_arrive() {
		return guests_arrive;
	}

	public void setGuests_arrive(short guests_arrive) {
		this.guests_arrive = guests_arrive;
	}
}
