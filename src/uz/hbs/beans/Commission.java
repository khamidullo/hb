package uz.hbs.beans;

import org.apache.wicket.util.io.IClusterable;

public class Commission extends Reservation implements IClusterable {
	private static final long serialVersionUID = 1L;
	private String hotel_name;
	private Integer reservations_count;
	private Integer definite;
	private Integer tentative;
	private Integer canceled;
	private Double total;
	private Double commission;

	public Commission() {
	}

	public String getHotel_name() {
		return hotel_name;
	}

	public void setHotel_name(String hotel_name) {
		this.hotel_name = hotel_name;
	}

	public Integer getReservations_count() {
		return reservations_count;
	}

	public void setReservations_count(Integer reservations_count) {
		this.reservations_count = reservations_count;
	}

	public Integer getDefinite() {
		return definite;
	}

	public void setDefinite(Integer definite) {
		this.definite = definite;
	}

	public Integer getTentative() {
		return tentative;
	}

	public void setTentative(Integer tentative) {
		this.tentative = tentative;
	}

	public Integer getCanceled() {
		return canceled;
	}

	public void setCanceled(Integer canceled) {
		this.canceled = canceled;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Double getCommission() {
		return commission;
	}

	public void setCommission(Double commission) {
		this.commission = commission;
	}
}
