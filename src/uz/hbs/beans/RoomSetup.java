package uz.hbs.beans;

import java.util.HashMap;

import org.apache.wicket.util.io.IClusterable;

public class RoomSetup implements IClusterable {
	private static final long serialVersionUID = 1L;
	private HashMap<Integer, RoomType> roomtypes;
	private String room_images; 
	
	
	public RoomSetup() {
		roomtypes = new HashMap<Integer, RoomType>();
	}
	
	public void addMap(int roomtypes_id, RoomType roomtype){
		roomtypes.put(roomtypes_id, roomtype);
	}
	
	public RoomType getMap(int roomtypes_id){
		return roomtypes.get(roomtypes_id);
	}

	public HashMap<Integer, RoomType> getRoom_types() {
		return roomtypes;
	}

	public String getRoom_images() {
		return room_images;
	}

	public void setRoom_images(String room_images) {
		this.room_images = room_images;
	}
}
