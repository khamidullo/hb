package uz.hbs.beans;

import org.apache.wicket.util.io.IClusterable;

public class HotelStar extends IdAndName implements IClusterable {
	private static final long serialVersionUID = 1L;
	public static final int MAX_STARS = 5;

	public HotelStar() {
	}
	
	@Override
	public String toString() {
		return "{id=" + id + ", name=" + name + "}";
	}
}
