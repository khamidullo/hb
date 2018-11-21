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
	private long reservations_id;
	private boolean manual;
	private long hotelsusers_id;
	private long initiator_user_id;
	private ReservationRoom reserveroom;
	
	public Bill() {
	}
	
	public Bill(long reservations_id) {
		this.setReservations_id(reservations_id);
	}
	
	public Bill(long reservations_id, long hotelsusers_id) {
		this.setReservations_id(reservations_id);
		this.hotelsusers_id = hotelsusers_id;
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

	public long getReservations_id() {
		return reservations_id;
	}

	public void setReservations_id(long reservations_id) {
		this.reservations_id = reservations_id;
	}

	public long gethotelsusers_id() {
		return hotelsusers_id;
	}

	public void setHotelsusers_id(long hotelsusers_id) {
		this.hotelsusers_id = hotelsusers_id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getInitiator_user_id() {
		return initiator_user_id;
	}

	public void setInitiator_user_id(long initiator_user_id) {
		this.initiator_user_id = initiator_user_id;
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
}
