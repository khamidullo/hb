package uz.hbs.beans;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.wicket.util.io.IClusterable;

public class ReservationSale implements IClusterable {
	private static final long serialVersionUID = 1L;
	private Long reservations_id;
	private Long saleplanes_id;
	private Date sale_date;
	private BigDecimal sale;
	private Integer roomtypes_id;
	private boolean is_group;
	private Short person_number;
	private Long hotelsusers_id;
	
	
	public ReservationSale() {
	}
	
	public ReservationSale(Long reservations_id, Integer roomtypes_id, Short person_number, boolean is_group, long hotelsusers_id) {
		this.reservations_id = reservations_id;
		this.roomtypes_id = roomtypes_id;
		this.person_number = person_number;
		this.is_group = is_group;
		this.hotelsusers_id = hotelsusers_id;
	}
	
	public ReservationSale(IndividualReservation reserve, Integer roomtypes_id, Short person_number) {
		this.reservations_id = reserve.getId();
		this.roomtypes_id = roomtypes_id;
		this.person_number = person_number;
		this.is_group = reserve.isIs_group();
		this.hotelsusers_id = reserve.getHotelsusers_id();
	}

	public ReservationSale(GroupReservation reserve, Integer roomtypes_id, Short person_number) {
		this.reservations_id = reserve.getId();
		this.roomtypes_id = roomtypes_id;
		this.person_number = person_number;
		this.is_group = reserve.isIs_group();
		this.hotelsusers_id = reserve.getHotelsusers_id();
	}
	
	public Long getReservations_id() {
		return reservations_id;
	}

	public void setReservations_id(Long reservations_id) {
		this.reservations_id = reservations_id;
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
		return roomtypes_id;
	}

	public void setRoomtypes_id(Integer roomtypes_id) {
		this.roomtypes_id = roomtypes_id;
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
		return hotelsusers_id;
	}

	public void setHotelsusers_id(Long hotelsusers_id) {
		this.hotelsusers_id = hotelsusers_id;
	}
}
