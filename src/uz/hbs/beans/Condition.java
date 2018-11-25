package uz.hbs.beans;


public class Condition extends IdAndName {
	private static final long serialVersionUID = 1L;
	private long hotel_id;
	private int roomtype_id;
	
	public Condition() {
	}
	
	public Condition(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public Condition(int id) {
		this.id = id;
	}
	
	public Condition(String name) {
		this.name = name;
	}

	public Condition(int id, long hotel_id, int roomtype_id) {
		this.id = id;
		this.hotel_id = hotel_id;
		this.roomtype_id = roomtype_id;
	}
	public long getHotelsusers_id() {
		return hotel_id;
	}

	public void setHotelsusers_id(long hotel_id) {
		this.hotel_id = hotel_id;
	}

	public int getRoomtypes_id() {
		return roomtype_id;
	}

	public void setRoomtypes_id(int roomtype_id) {
		this.roomtype_id = roomtype_id;
	}
}
