package uz.hbs.beans.filters;

import org.apache.wicket.util.io.IClusterable;

import uz.hbs.beans.CleanState;
import uz.hbs.beans.OnSaleState;
import uz.hbs.beans.RoomState;

public class RoomFilter implements IClusterable {
	private static final long serialVersionUID = 1L;
	private CleanState clean_state;
	private OnSaleState onsale_state;
	private RoomState room_state;
	private String room_number;
	private long hotel_id;
	
	public RoomFilter(long hotel_id) {
		this.hotel_id = hotel_id;
	}

	public CleanState getClean_state() {
		return clean_state;
	}

	public void setClean_state(CleanState clean_state) {
		this.clean_state = clean_state;
	}

	public OnSaleState getOnsale_state() {
		return onsale_state;
	}

	public void setOnsale_state(OnSaleState onsale_state) {
		this.onsale_state = onsale_state;
	}

	public RoomState getRoom_state() {
		return room_state;
	}

	public void setRoom_state(RoomState room_state) {
		this.room_state = room_state;
	}

	public String getRoom_number() {
		return room_number;
	}

	public void setRoom_number(String room_number) {
		this.room_number = room_number;
	}

	public long getHotelsusers_id() {
		return hotel_id;
	}

	public void setHotelsusers_id(long hotel_id) {
		this.hotel_id = hotel_id;
	}
}
