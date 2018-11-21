package uz.hbs.beans;

import org.apache.wicket.util.io.IClusterable;

public class SelectedRooms implements IClusterable {
	private static final long serialVersionUID = 1L;
	private RoomType roomType;

	public SelectedRooms() {
	}

	public RoomType getRoomType() {
		return roomType;
	}

	public void setRoomType(RoomType roomType) {
		this.roomType = roomType;
	}
}
