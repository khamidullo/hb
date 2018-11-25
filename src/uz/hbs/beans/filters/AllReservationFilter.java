package uz.hbs.beans.filters;

import java.util.Date;

import org.apache.wicket.util.io.IClusterable;

import uz.hbs.beans.City;
import uz.hbs.beans.IdLongAndName;
import uz.hbs.beans.ReservationStatus;
import uz.hbs.beans.ReservationType;

public class AllReservationFilter implements IClusterable {
	private static final long serialVersionUID = 1L;
	private Long reservation_id;
	private Date created_from;
	private Date created_to;
	private Date checkin_from;
	private Date checkin_to;
	private Date checkout_from;
	private Date checkout_to;
	
	private Date check_in;
	private Date check_out;
	
	private String first_name;
	private String last_name;
	private String group_name;
	
	private ReservationType reservation_type;
	private ReservationStatus reservation_status;
	
	private City city;	
	private IdLongAndName tourAgent;
	private IdLongAndName hotel;
	
	private Byte user_type;
	
	private String payment_method;
	
	public AllReservationFilter() {
	}

	public Date getCreated_from() {
		return created_from;
	}

	public void setCreated_from(Date created_from) {
		this.created_from = created_from;
	}

	public Date getCreated_to() {
		return created_to;
	}

	public void setCreated_to(Date created_to) {
		this.created_to = created_to;
	}

	public Date getCheckin_from() {
		return checkin_from;
	}

	public void setCheckin_from(Date checkin_from) {
		this.checkin_from = checkin_from;
	}

	public Date getCheckin_to() {
		return checkin_to;
	}

	public void setCheckin_to(Date checkin_to) {
		this.checkin_to = checkin_to;
	}

	public Date getCheckout_from() {
		return checkout_from;
	}

	public void setCheckout_from(Date checkout_from) {
		this.checkout_from = checkout_from;
	}

	public Date getCheckout_to() {
		return checkout_to;
	}

	public void setCheckout_to(Date checkout_to) {
		this.checkout_to = checkout_to;
	}

	public IdLongAndName getTourAgent() {
		return tourAgent;
	}

	public void setTourAgent(IdLongAndName tourAgent) {
		this.tourAgent = tourAgent;
	}

	public IdLongAndName getHotel() {
		return hotel;
	}

	public void setHotel(IdLongAndName hotel) {
		this.hotel = hotel;
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

	public String getGroup_name() {
		return group_name;
	}

	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}

	public ReservationType getReservation_type() {
		return reservation_type;
	}

	public void setReservation_type(ReservationType reservation_type) {
		this.reservation_type = reservation_type;
	}

	public ReservationStatus getReservation_status() {
		return reservation_status;
	}

	public void setReservation_status(ReservationStatus reservation_status) {
		this.reservation_status = reservation_status;
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

	public Long getReservations_id() {
		return reservation_id;
	}

	public void setReservations_id(Long reservation_id) {
		this.reservation_id = reservation_id;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public Byte getUser_type() {
		return user_type;
	}

	public void setUser_type(Byte user_type) {
		this.user_type = user_type;
	}

	public String getPayment_method() {
		return payment_method;
	}

	public void setPayment_method(String payment_method) {
		this.payment_method = payment_method;
	}
}
