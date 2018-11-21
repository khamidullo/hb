package uz.hbs.beans.rate.filters;

import java.util.Date;

import org.apache.wicket.util.io.IClusterable;

public class RatePlaneSaleFilter implements IClusterable {
	private static final long serialVersionUID = 1L;
	private String name;
	private Date sale_from;
	private Date sale_to;
	private int rateplanes_id;
	
	public RatePlaneSaleFilter() {
	}
	
	public RatePlaneSaleFilter(int rateplanes_id) {
		this.rateplanes_id = rateplanes_id;
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

	public int getRateplanes_id() {
		return rateplanes_id;
	}

	public void setRateplanes_id(int rateplanes_id) {
		this.rateplanes_id = rateplanes_id;
	}
}
