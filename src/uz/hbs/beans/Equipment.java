package uz.hbs.beans;

public class Equipment extends IdAndName {
	private static final long serialVersionUID = 1L;
	private int roomtypes_id;
	private long hotelsusers_id;

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
		return "{id=" + id + ", name=" + name + ", hotelsusers_id=" + hotelsusers_id + ", roomtypes_id=" + roomtypes_id + "}";
	}

	public int getRoomtypes_id() {
		return roomtypes_id;
	}

	public void setRoomtypes_id(int roomtypes_id) {
		this.roomtypes_id = roomtypes_id;
	}

	public long getHotelsusers_id() {
		return hotelsusers_id;
	}

	public void setHotelsusers_id(long hotelsusers_id) {
		this.hotelsusers_id = hotelsusers_id;
	}
}
