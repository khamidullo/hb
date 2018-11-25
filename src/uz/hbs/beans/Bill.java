package uz.hbs.beans;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.wicket.util.io.IClusterable;

public class Bill implements IClusterable {
	private static final long serialVersionUID = 1L;
	public static final byte ADVANCE_PAYMENT = 0;
	public static final byte OTHER_BILL = -1;
	public static final boolean MANUAL = true;
	public static final boolean NIGHT_AUDIT = false;
	
	private long id;
	private Date bill_date;
	private IdAndName service; 
	private BigDecimal charge;
	private String note;
	private String description;
	
	private BigDecimal debit;
	private BigDecimal credit;
	
	private Float service_charge;
	private Float city_tax;
	private BigDecimal subtotal;
	private BigDecimal total;
	private BigDecimal paid;
	private BigDecimal total_due;
	private long reserve_id;
	private boolean manual;
	private long hotel_id;
	private long user_id;
	private ReservationRoom reserveroom;
	
	public Bill() {
	}
	
	public Bill(long reserve_id) {
		this.reserve_id = reserve_id;
	}
	
	public Bill(long reserve_id, long hotel_id) {
		this.reserve_id = reserve_id;
		this.hotel_id = hotel_id;
	}

	public Date getBill_date() {
		return bill_date;
	}

	public void setBill_date(Date bill_date) {
		this.bill_date = bill_date;
	}

	public IdAndName getService() {
		return service;
	}

	public void setService(IdAndName service) {
		this.service = service;
	}

	public BigDecimal getCharge() {
		return charge;
	}

	public void setCharge(BigDecimal charge) {
		this.charge = charge;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public BigDecimal getDebit() {
		return debit;
	}

	public void setDebit(BigDecimal debit) {
		this.debit = debit;
	}

	public BigDecimal getCredit() {
		return credit;
	}

	public void setCredit(BigDecimal credit) {
		this.credit = credit;
	}

	public Float getService_charge() {
		return service_charge;
	}

	public void setService_charge(Float service_charge) {
		this.service_charge = service_charge;
	}

	public Float getCity_tax() {
		return city_tax;
	}

	public void setCity_tax(Float city_tax) {
		this.city_tax = city_tax;
	}

	public BigDecimal getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public BigDecimal getPaid() {
		return paid;
	}

	public void setPaid(BigDecimal paid) {
		this.paid = paid;
	}

	public BigDecimal getTotal_due() {
		return total_due;
	}

	public void setTotal_due(BigDecimal total_due) {
		this.total_due = total_due;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean isManual() {
		return manual;
	}

	public void setManual(boolean manual) {
		this.manual = manual;
	}

	public ReservationRoom getReserveroom() {
		return reserveroom;
	}

	public void setReserveroom(ReservationRoom reserveroom) {
		this.reserveroom = reserveroom;
	}

	public long getReserve_id() {
		return reserve_id;
	}

	public void setReserve_id(long reserve_id) {
		this.reserve_id = reserve_id;
	}

	public long getHotel_id() {
		return hotel_id;
	}

	public void setHotel_id(long hotel_id) {
		this.hotel_id = hotel_id;
	}

	public long getUser_id() {
		return user_id;
	}

	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}

	@Override
	public String toString() {
		return "Bill [id=" + id + ", bill_date=" + bill_date + ", service=" + service + ", charge=" + charge + ", note="
				+ note + ", description=" + description + ", debit=" + debit + ", credit=" + credit
				+ ", service_charge=" + service_charge + ", city_tax=" + city_tax + ", subtotal=" + subtotal
				+ ", total=" + total + ", paid=" + paid + ", total_due=" + total_due + ", reserve_id=" + reserve_id
				+ ", manual=" + manual + ", hotel_id=" + hotel_id + ", user_id=" + user_id + ", reserveroom="
				+ reserveroom + "]";
	}
}