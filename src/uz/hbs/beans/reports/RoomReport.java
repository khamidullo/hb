package uz.hbs.beans.reports;

import java.util.Date;

import org.apache.wicket.util.io.IClusterable;

public class RoomReport implements IClusterable {
	private static final long serialVersionUID = 1L;
	private Date date;
	private Integer max_rooms;
	private Short under_repair;
	private Short closed;
	private Short available;
	private Short reservations;
	private Short definitive;
	private Short tentative;
	private Short availability;
	private Float load;
	private Short adults;
	private Short children;
	private Short guests_leave;
	private Short guests_arrive;
	
	public RoomReport() {
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getMax_rooms() {
		return max_rooms;
	}

	public void setMax_rooms(Integer max_rooms) {
		this.max_rooms = max_rooms;
	}

	public Short getUnder_repair() {
		return under_repair;
	}

	public void setUnder_repair(Short under_repair) {
		this.under_repair = under_repair;
	}

	public Short getClosed() {
		return closed;
	}

	public void setClosed(Short closed) {
		this.closed = closed;
	}

	public Short getAvailable() {
		return available;
	}

	public void setAvailable(Short available) {
		this.available = available;
	}

	public Short getReservations() {
		return reservations;
	}

	public void setReservations(Short reservations) {
		this.reservations = reservations;
	}

	public Short getDefinitive() {
		return definitive;
	}

	public void setDefinitive(Short definitive) {
		this.definitive = definitive;
	}

	public Short getAvailability() {
		return availability;
	}

	public void setAvailability(Short availability) {
		this.availability = availability;
	}

	public Float getLoad() {
		return load;
	}

	public void setLoad(Float load) {
		this.load = load;
	}

	public Short getAdults() {
		return adults;
	}

	public void setAdults(Short adults) {
		this.adults = adults;
	}

	public Short getGuests_leave() {
		return guests_leave;
	}

	public void setGuests_leave(Short guests_leave) {
		this.guests_leave = guests_leave;
	}

	public Short getGuests_arrive() {
		return guests_arrive;
	}

	public void setGuests_arrive(Short guests_arrive) {
		this.guests_arrive = guests_arrive;
	}

	public Short getTentative() {
		return tentative;
	}

	public void setTentative(Short tentative) {
		this.tentative = tentative;
	}

	public Short getChildren() {
		return children;
	}

	public void setChildren(Short children) {
		this.children = children;
	}
}
