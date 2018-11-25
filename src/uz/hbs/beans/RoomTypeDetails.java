package uz.hbs.beans;

import org.apache.wicket.util.io.IClusterable;

public class RoomTypeDetails implements IClusterable{
	private static final long serialVersionUID = 1L;
	private String name;
	private Double price;
	private Integer count;
	private Integer adults;
	private Integer children;
	private Double total_price;
	private Boolean extra_bed_needed;
	private Double extra_bed_price_type_value;
	private Byte meal_options;
	private Double meal_cost;
	private String guest_name;
	private Byte status;
	private Boolean selected = Boolean.FALSE;
	private Long reservationroom_id;
	private Long hotel_id;
	private boolean is_group;
	
	public RoomTypeDetails() {
	}

	public RoomTypeDetails(Integer count, Double total_price) {
		this.count = count;
		this.total_price = total_price;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Double getTotal_price() {
		return total_price;
	}

	public void setTotal_price(Double total_price) {
		this.total_price = total_price;
	}

	public Boolean getExtra_bed_needed() {
		return extra_bed_needed;
	}

	public void setExtra_bed_needed(Boolean extra_bed_needed) {
		this.extra_bed_needed = extra_bed_needed;
	}

	public Double getExtra_bed_price_type_value() {
		return extra_bed_price_type_value;
	}

	public void setExtra_bed_price_type_value(Double extra_bed_price_type_value) {
		this.extra_bed_price_type_value = extra_bed_price_type_value;
	}

	public Byte getMeal_options() {
		return meal_options;
	}

	public void setMeal_options(Byte meal_options) {
		this.meal_options = meal_options;
	}

	public Double getMeal_cost() {
		return meal_cost;
	}

	public void setMeal_cost(Double meal_cost) {
		this.meal_cost = meal_cost;
	}

	public Integer getAdults() {
		return adults;
	}

	public void setAdults(Integer adults) {
		this.adults = adults;
	}

	public Integer getChildren() {
		return children;
	}

	public void setChildren(Integer children) {
		this.children = children;
	}

	public String getGuest_name() {
		return guest_name;
	}

	public void setGuest_name(String guest_name) {
		this.guest_name = guest_name;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Boolean getSelected() {
		return selected;
	}

	public void setSelected(Boolean selected) {
		this.selected = selected;
	}

	public Long getReservationrooms_id() {
		return reservationroom_id;
	}

	public void setReservationrooms_id(Long reservationroom_id) {
		this.reservationroom_id = reservationroom_id;
	}

	public boolean isIs_group() {
		return is_group;
	}

	public void setIs_group(boolean is_group) {
		this.is_group = is_group;
	}

	public Long getHotelsusers_id() {
		return hotel_id;
	}

	public void setHotelsusers_id(Long hotel_id) {
		this.hotel_id = hotel_id;
	}
}