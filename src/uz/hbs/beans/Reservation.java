package uz.hbs.beans;

import java.util.Date;

import org.apache.wicket.util.io.IClusterable;

public class Reservation implements IClusterable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Date create_date;
	private String guest_name;
	private String ta_name;
	private String hotel_name;
	private Date check_in;
	private Date check_out;
	private Byte status;
	private Double total_sum;
	private Long hotel_id;
	private Date fromDate;
	private Date toDate;
	private boolean is_group;
	private long touragent_id;
	private Byte type;
	private int guest_count;
	private boolean payment_owner;
	private String hotel_legal_name;
	private String payment_method;

	public Reservation() {
	}
	
	public Reservation(long touragent_id) {
		this.touragent_id = touragent_id;
	}

	public Reservation(Long touragent_id, Date fromDate, Date toDate, Long hotel_id) {
		this.touragent_id = touragent_id;
		this.fromDate = fromDate;
		this.toDate = toDate;
		this.hotel_id = hotel_id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public String getGuest_name() {
		return guest_name;
	}

	public void setGuest_name(String guest_name) {
		this.guest_name = guest_name;
	}

	public String getHotel_name() {
		return hotel_name;
	}

	public void setHotel_name(String hotel_name) {
		this.hotel_name = hotel_name;
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

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Double getTotal_sum() {
		return total_sum;
	}

	public void setTotal_sum(Double total_sum) {
		this.total_sum = total_sum;
	}

	public Long getHotel_id() {
		return hotel_id;
	}

	public void setHotel_id(Long hotel_id) {
		this.hotel_id = hotel_id;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public boolean isIs_group() {
		return is_group;
	}

	public void setIs_group(boolean is_group) {
		this.is_group = is_group;
	}

	public long getTouragent_id() {
		return touragent_id;
	}

	public void setTouragent_id(long touragent_id) {
		this.touragent_id = touragent_id;
	}

	public Byte getType() {
		return type;
	}

	public void setType(Byte type) {
		this.type = type;
	}

	public String getTa_name() {
		return ta_name;
	}

	public void setTa_name(String ta_name) {
		this.ta_name = ta_name;
	}

	public int getGuest_count() {
		return guest_count;
	}

	public void setGuest_count(int guest_count) {
		this.guest_count = guest_count;
	}

	public boolean isPayment_owner() {
		return payment_owner;
	}

	public void setPayment_owner(boolean payment_owner) {
		this.payment_owner = payment_owner;
	}

	public String getHotel_legal_name() {
		return hotel_legal_name;
	}

	public void setHotel_legal_name(String hotel_legal_name) {
		this.hotel_legal_name = hotel_legal_name;
	}

	public String getPayment_method() {
		return payment_method;
	}

	public void setPayment_method(String payment_method) {
		this.payment_method = payment_method;
	}
}
