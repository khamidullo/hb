package uz.hbs.beans.filters;

import java.util.Date;

import org.apache.wicket.util.io.IClusterable;

public class SaleRateFilter implements IClusterable {
	private static final long serialVersionUID = 1L;
	private String name;
	private Date sale_from;
	private Date sale_to;
	private long roomrateplane_id;
	
	public SaleRateFilter(long roomrateplane_id) {
		this.roomrateplane_id = roomrateplane_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getSale_from() {
		return sale_from;
	}

	public void setSale_from(Date sale_from) {
		this.sale_from = sale_from;
	}

	public Date getSale_to() {
		return sale_to;
	}

	public void setSale_to(Date sale_to) {
		this.sale_to = sale_to;
	}

	public long getRoomrateplane_id() {
		return roomrateplane_id;
	}

	public void setRoomrateplane_id(long roomrateplane_id) {
		this.roomrateplane_id = roomrateplane_id;
	}
}
