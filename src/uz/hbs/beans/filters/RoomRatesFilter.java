package uz.hbs.beans.filters;

import org.apache.wicket.util.io.IClusterable;

public class RoomRatesFilter implements IClusterable {
	private static final long serialVersionUID = 1L;
	private String name;
	private String description;
	private long hotel_id;
	
	public RoomRatesFilter(long hotel_id) {
		this.hotel_id = hotel_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getHotelsusers_id() {
		return hotel_id;
	}

	public void setHotelsusers_id(long hotel_id) {
		this.hotel_id = hotel_id;
	}
}
