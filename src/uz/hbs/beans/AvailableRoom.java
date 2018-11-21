package uz.hbs.beans;

import java.util.Date;

import org.apache.wicket.util.io.IClusterable;

public class AvailableRoom implements IClusterable {
	private static final long serialVersionUID = 1L;
	private Date date;
	private Integer total_rooms;
	private String roomtype;
	private Integer available_rooms;
	private Double rate;
	private byte onsale; 
	
	public AvailableRoom() {
	}
	
	public AvailableRoom(Date date) {
		this.date = date;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getTotal_rooms() {
		return total_rooms;
	}

	public void setTotal_rooms(Integer total_rooms) {
		this.total_rooms = total_rooms;
	}

	public String getRoomtype() {
		return roomtype;
	}

	public void setRoomtype(String roomtype) {
		this.roomtype = roomtype;
	}

	public Integer getAvailable_rooms() {
		return available_rooms;
	}

	public void setAvailable_rooms(Integer available_rooms) {
		this.available_rooms = available_rooms;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public byte getOnsale() {
		return onsale;
	}

	public void setOnsale(byte onsale) {
		this.onsale = onsale;
	}
}
