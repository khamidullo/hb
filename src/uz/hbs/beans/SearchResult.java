package uz.hbs.beans;

import org.apache.wicket.util.io.IClusterable;

public class SearchResult implements IClusterable {
	private static final long serialVersionUID = 1L;
	private long hotelsusers_id;
	private RoomType roomtype;
	private Short hotel_star;
	private String holding_capacity_text;
	
	public SearchResult() {
		// TODO Auto-generated constructor stub
	}

	public long getHotelsusers_id() {
		return hotelsusers_id;
	}

	public void setHotelsusers_id(long hotelsusers_id) {
		this.hotelsusers_id = hotelsusers_id;
	}

	public RoomType getRoomtype() {
		return roomtype;
	}

	public void setRoomtype(RoomType roomtype) {
		this.roomtype = roomtype;
	}

	public Short getHotel_star() {
		return hotel_star;
	}

	public void setHotel_star(Short hotel_star) {
		this.hotel_star = hotel_star;
	}

	public String getHolding_capacity_text() {
		return holding_capacity_text;
	}

	public void setHolding_capacity_text(String holding_capacity_text) {
		this.holding_capacity_text = holding_capacity_text;
	}
}
