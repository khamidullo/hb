package uz.hbs.beans;


public class Condition extends IdAndName {
	private static final long serialVersionUID = 1L;
	private long hotelsusers_id;
	private int roomtypes_id;
	
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

	public Condition(int id, long hotelsusers_id, int roomtypes_id) {
		this.id = id;
		this.hotelsusers_id = hotelsusers_id;
		this.roomtypes_id = roomtypes_id;
	}
	public long getHotelsusers_id() {
		return hotelsusers_id;
	}

	public void setHotelsusers_id(long hotelsusers_id) {
		this.hotelsusers_id = hotelsusers_id;
	}

	public int getRoomtypes_id() {
		return roomtypes_id;
	}

	public void setRoomtypes_id(int roomtypes_id) {
		this.roomtypes_id = roomtypes_id;
	}
}
