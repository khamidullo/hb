package uz.hbs.beans;

import java.util.Date;

import org.apache.wicket.util.io.IClusterable;

public class ReservationTemplate implements IClusterable {
	private static final long serialVersionUID = 1L;

	private Long reservation_id;
	private String hotel_name_region;
	private String order_number;
	private String touragency_login;
	private String touragency_name;
	private String touragency_address;
	private String touragency_phone;
	private String touragency_email;
	private String ta_login;
	private String ta_name;
	private Date check_in;
	private Date check_out;
	private Date create_date;
	private Integer nights;
	private String first_name;
	private String last_name;
	private String guest_nationality;
	private Integer adults;
	private Integer children;
	private Long hotelsusers_id;
	private Integer meal_options;
	private String ta_email;
	private String hotel_email;
	private Double total;
	private Double additional_service_cost;
	private Double grand_total;
	private Byte reservation_type;
	private Boolean payment_owner;
	private String ta_phone;
	private String hotel_phone;
	private String ta_comments;
	private String hotel_address;
	private boolean resident;
	private Long creator_user_id;
	
	public ReservationTemplate() {
	}

	public String getHotel_name_region() {
		return hotel_name_region;
	}

	public void setHotel_name_region(String hotel_name_region) {
		this.hotel_name_region = hotel_name_region;
	}

	public String getOrder_number() {
		return order_number;
	}

	public void setOrder_number(String order_number) {
		this.order_number = order_number;
	}

	public String getTouragency_name() {
		return touragency_name;
	}

	public void setTouragency_name(String touragency_name) {
		this.touragency_name = touragency_name;
	}

	public Date getCheck_in() {
		return check_in;
	}

	public void setCheck_in(Date check_in) {
		this.check_in = check_in;
	}

	public Date getCheck_out() {
		return check_out;
	}

	public void setCheck_out(Date check_out) {
		this.check_out = check_out;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public Integer getNights() {
		return nights;
	}

	public void setNights(Integer nights) {
		this.nights = nights;
	}

	public String getTa_name() {
		return ta_name;
	}

	public void setTa_name(String ta_name) {
		this.ta_name = ta_name;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public Long getReservation_id() {
		return reservation_id;
	}

	public void setReservation_id(Long reservation_id) {
		this.reservation_id = reservation_id;
	}

	public String getGuest_nationality() {
		return guest_nationality;
	}

	public void setGuest_nationality(String guest_nationality) {
		this.guest_nationality = guest_nationality;
	}

	public String getTouragency_phone() {
		return touragency_phone;
	}

	public void setTouragency_phone(String touragency_phone) {
		this.touragency_phone = touragency_phone;
	}

	public String getTouragency_email() {
		return touragency_email;
	}

	public void setTouragency_email(String touragency_email) {
		this.touragency_email = touragency_email;
	}

	public String getTouragency_address() {
		return touragency_address;
	}

	public void setTouragency_address(String touragency_address) {
		this.touragency_address = touragency_address;
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

	public Long getHotelsusers_id() {
		return hotelsusers_id;
	}

	public void setHotelsusers_id(Long hotelsusers_id) {
		this.hotelsusers_id = hotelsusers_id;
	}

	public Integer getMeal_options() {
		return meal_options;
	}

	public void setMeal_options(Integer meal_options) {
		this.meal_options = meal_options;
	}

	public String getTa_email() {
		return ta_email;
	}

	public void setTa_email(String ta_email) {
		this.ta_email = ta_email;
	}

	public String getHotel_email() {
		return hotel_email;
	}

	public void setHotel_email(String hotel_email) {
		this.hotel_email = hotel_email;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Double getAdditional_service_cost() {
		return additional_service_cost;
	}

	public void setAdditional_service_cost(Double additional_service_cost) {
		this.additional_service_cost = additional_service_cost;
	}

	public Double getGrand_total() {
		return grand_total;
	}

	public void setGrand_total(Double grand_total) {
		this.grand_total = grand_total;
	}

	public Byte getReservation_type() {
		return reservation_type;
	}

	public void setReservation_type(Byte reservation_type) {
		this.reservation_type = reservation_type;
	}

	public Boolean getPayment_owner() {
		return payment_owner;
	}

	public void setPayment_owner(Boolean payment_owner) {
		this.payment_owner = payment_owner;
	}

	public String getTa_phone() {
		return ta_phone;
	}

	public void setTa_phone(String ta_phone) {
		this.ta_phone = ta_phone;
	}

	public String getTa_comments() {
		return ta_comments;
	}

	public void setTa_comments(String ta_comments) {
		this.ta_comments = ta_comments;
	}

	public String getHotel_phone() {
		return hotel_phone;
	}

	public void setHotel_phone(String hotel_phone) {
		this.hotel_phone = hotel_phone;
	}

	public String getHotel_address() {
		return hotel_address;
	}

	public void setHotel_address(String hotel_address) {
		this.hotel_address = hotel_address;
	}

	public String getTa_login() {
		return ta_login;
	}

	public void setTa_login(String ta_login) {
		this.ta_login = ta_login;
	}

	public String getTouragency_login() {
		return touragency_login;
	}

	public void setTouragency_login(String touragency_login) {
		this.touragency_login = touragency_login;
	}

	public boolean isResident() {
		return resident;
	}

	public void setResident(boolean resident) {
		this.resident = resident;
	}

	public Long getCreator_user_id() {
		return creator_user_id;
	}

	public void setCreator_user_id(Long creator_user_id) {
		this.creator_user_id = creator_user_id;
	}
}