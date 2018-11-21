package uz.hbs.beans;

import java.math.BigDecimal;

import org.apache.wicket.util.io.IClusterable;

public class RateSale implements IClusterable {
	private static final long serialVersionUID = 1L;
	private BigDecimal rate;
	private BigDecimal sale;
	
	public RateSale() {
	}

	public BigDecimal getRate() {
		return rate;

	}
	
	public BigDecimal getRate(boolean internal) {
		if (internal) return rate;
		else {
			if (sale == null) return rate;
			else return sale;
		}
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public BigDecimal getSale() {
		return sale;
	}

	public void setSale(BigDecimal sale) {
		this.sale = sale;
	}
}
