package uz.hbs.beans;

public class Equipment extends IdAndName {
	private static final long serialVersionUID = 1L;
	private int roomtype_id;
	private long hotel_id;

	public Equipment() {
		super();
	}

	public Equipment(String name) {
		super();
		this.name = name;
	}

	public Equipment(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	public Equipment(Integer id) {
		super();
		this.id = id;
	}

	@Override
	public String toString() {
		return "{id=" + id + ", name=" + name + ", hotel_id=" + hotel_id + ", roomtype_id=" + roomtype_id + "}";
	}

	public int getRoomtypes_id() {
		return roomtype_id;
	}

	public void setRoomtypes_id(int roomtype_id) {
		this.roomtype_id = roomtype_id;
	}

	public long getHotelsusers_id() {
		return hotel_id;
	}

	public void setHotelsusers_id(long hotel_id) {
		this.hotel_id = hotel_id;
	}
}
