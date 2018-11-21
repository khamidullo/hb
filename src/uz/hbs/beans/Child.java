package uz.hbs.beans;

import java.util.Date;

import org.apache.wicket.util.io.IClusterable;

public class Child implements IClusterable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private short child_index;
	private String first_name;
	private String last_name;
	private Date date_of_birth;
	
	private long guest_detail_id;
	private long reservations_id;
	private long initiator_user_id;
	
	private Long reservationrooms_id;
	private Short age;
	private boolean free;
	
	public Child() {
	}
	
	public Child(Guest guest) {
		this.age = guest.getChildAge().getAge();
		this.child_index = guest.getChildAge().getChild_index();
		this.first_name = guest.getFirst_name();
		this.last_name = guest.getLast_name();
	}
	
	public Child(short child_index) {
		this.child_index = child_index;
	}
	
	public Child(long reservations_id, long reservationrooms_id, short child_index, Short age) {
		this.reservations_id = reservations_id;
		this.reservationrooms_id = reservationrooms_id;
		this.age = age;
		this.child_index = child_index;
	}

	public Date getDate_of_birth() {
		return date_of_birth;
	}

	public void setDate_of_birth(Date date_of_birth) {
		this.date_of_birth = date_of_birth;
	}

	public long getGuest_detail_id() {
		return guest_detail_id;
	}

	public void setGuest_detail_id(long guest_detail_id) {
		this.guest_detail_id = guest_detail_id;
	}

	public short getChild_index() {
		return child_index;
	}

	public void setChild_index(short child_index) {
		this.child_index = child_index;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getReservations_id() {
		return reservations_id;
	}

	public void setReservations_id(long reservations_id) {
		this.reservations_id = reservations_id;
	}

	public long getInitiator_user_id() {
		return initiator_user_id;
	}

	public void setInitiator_user_id(long initiator_user_id) {
		this.initiator_user_id = initiator_user_id;
	}

	public Long getReservationrooms_id() {
		return reservationrooms_id;
	}

	public void setReservationrooms_id(Long reservationrooms_id) {
		this.reservationrooms_id = reservationrooms_id;
	}

	public Short getAge() {
		return age;
	}

	public void setAge(Short age) {
		this.age = age;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public boolean isFree() {
		return free;
	}

	public void setFree(boolean free) {
		this.free = free;
	}
}
