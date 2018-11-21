package uz.hbs.beans;

import org.apache.wicket.util.io.IClusterable;

public class PricesRange implements IClusterable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Double start_price;
	private Double end_price;

	public PricesRange() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getStart_price() {
		return start_price;
	}

	public void setStart_price(Double start_price) {
		this.start_price = start_price;
	}

	public Double getEnd_price() {
		return end_price;
	}

	public void setEnd_price(Double end_price) {
		this.end_price = end_price;
	}
}
