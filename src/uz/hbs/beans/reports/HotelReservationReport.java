package uz.hbs.beans.reports;

import java.util.Date;

import org.apache.wicket.util.io.IClusterable;

public class HotelReservationReport implements IClusterable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Date create_date;
	private String first_name;
	private String last_name;
	private String middle_name;
	private String tour_agent;
	private Date check_in;
	private Date check_out;
	private String check_out_in;
	private byte status;
	private byte reserve_type;
	private Short guests; 
	private Double total;
	private String roomtype;
	private String room_number;

	public HotelReservationReport() {
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public String getTour_agent() {
		return tour_agent;
	}

	public void setTour_agent(String tour_agent) {
		this.tour_agent = tour_agent;
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

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Short getGuests() {
		return guests;
	}

	public void setGuests(Short guests) {
		this.guests = guests;
	}

	public String getRoomtype() {
		return roomtype;
	}

	public void setRoomtype(String roomtype) {
		this.roomtype = roomtype;
	}
	
	public String getRoom_number() {
		return room_number;
	}

	public void setRoom_number(String room_number) {
		this.room_number = room_number;
	}

	public String getCheck_out_in() {
		return check_out_in;
	}

	public void setCheck_out_in(String check_out_in) {
		this.check_out_in = check_out_in;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getMiddle_name() {
		return middle_name;
	}

	public void setMiddle_name(String middle_name) {
		this.middle_name = middle_name;
	}

	public byte getReserve_type() {
		return reserve_type;
	}

	public void setReserve_type(byte reserve_type) {
		this.reserve_type = reserve_type;
	}
}
