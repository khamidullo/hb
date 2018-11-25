package uz.hbs.beans;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.wicket.util.io.IClusterable;

public class ReservationSale implements IClusterable {
	private static final long serialVersionUID = 1L;
	private Long reservation_id;
	private Long saleplanes_id;
	private Date sale_date;
	private BigDecimal sale;
	private Integer roomtype_id;
	private boolean is_group;
	private Short person_number;
	private Long hotel_id;
	
	
	public ReservationSale() {
	}
	
	public ReservationSale(Long reservation_id, Integer roomtype_id, Short person_number, boolean is_group, long hotel_id) {
		this.reservation_id = reservation_id;
		this.roomtype_id = roomtype_id;
		this.person_number = person_number;
		this.is_group = is_group;
		this.hotel_id = hotel_id;
	}
	
	public ReservationSale(IndividualReservation reserve, Integer roomtype_id, Short person_number) {
		this.reservation_id = reserve.getId();
		this.roomtype_id = roomtype_id;
		this.person_number = person_number;
		this.is_group = reserve.isIs_group();
		this.hotel_id = reserve.getHotelsusers_id();
	}

	public ReservationSale(GroupReservation reserve, Integer roomtype_id, Short person_number) {
		this.reservation_id = reserve.getId();
		this.roomtype_id = roomtype_id;
		this.person_number = person_number;
		this.is_group = reserve.isIs_group();
		this.hotel_id = reserve.getHotelsusers_id();
	}
	
	public Long getReservations_id() {
		return reservation_id;
	}

	public void setReservations_id(Long reservation_id) {
		this.reservation_id = reservation_id;
	}

	public Long getSaleplanes_id() {
		return saleplanes_id;
	}

	public void setSaleplanes_id(Long saleplanes_id) {
		this.saleplanes_id = saleplanes_id;
	}

	public Date getSale_date() {
		return sale_date;
	}

	public void setSale_date(Date sale_date) {
		this.sale_date = sale_date;
	}

	public BigDecimal getSale() {
		return sale;
	}

	public void setSale(BigDecimal sale) {
		this.sale = sale;
	}

	public Integer getRoomtypes_id() {
		return roomtype_id;
	}

	public void setRoomtypes_id(Integer roomtype_id) {
		this.roomtype_id = roomtype_id;
	}

	public boolean isIs_group() {
		return is_group;
	}

	public void setIs_group(boolean is_group) {
		this.is_group = is_group;
	}

	public Short getPerson_number() {
		return person_number;
	}

	public void setPerson_number(Short person_number) {
		this.person_number = person_number;
	}

	public Long getHotelsusers_id() {
		return hotel_id;
	}

	public void setHotelsusers_id(Long hotel_id) {
		this.hotel_id = hotel_id;
	}
}
