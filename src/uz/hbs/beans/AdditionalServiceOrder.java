package uz.hbs.beans;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.wicket.util.io.IClusterable;

public class AdditionalServiceOrder implements IClusterable {
	private static final long serialVersionUID = 1L;
	public static final byte STATUS_NEW = 0;
	public static final byte STATUS_CANCEL = 1;
	public static final byte STATUS_CLOSED = 2;
	
	private Long id;
	private Short person;
	private boolean insurance;
	private Date arrival_date;
	private Date departure_date;
	private Long creator_user_id;
	private Date create_date;
	private Date update_date;
	private Long initiator_user_id;
	private Long reservations_id;
	private BigDecimal total;
	private String fullname;
	private byte status;
	
	private AdditionalServiceDetail arrival = new AdditionalServiceDetail(AdditionalServiceDetail.SERVICE_TYPE_ARRIVED);
	private AdditionalServiceDetail departure = new AdditionalServiceDetail(AdditionalServiceDetail.SERVICE_TYPE_DEPARTED);
	private List<Insurance> insuranceList = new ArrayList<Insurance>();
	
	public AdditionalServiceOrder() {
		
	}

	public AdditionalServiceOrder(long reservations_id) {
		this.reservations_id = reservations_id;
	}
	
	public AdditionalServiceDetail getArrival() {
		return arrival;
	}

	public void setArrival(AdditionalServiceDetail arrival) {
		this.arrival = arrival;
	}

	public List<Insurance> getInsuranceList() {
		return insuranceList;
	}

	public void setInsuranceList(List<Insurance> insuranceList) {
		this.insuranceList = insuranceList;
	}

	public Short getPerson() {
		return person;
	}

	public void setPerson(Short person) {
		this.person = person;
	}

	public boolean isInsurance() {
		return insurance;
	}

	public void setInsurance(boolean insurance) {
		this.insurance = insurance;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public AdditionalServiceDetail getDeparture() {
		return departure;
	}

	public void setDeparture(AdditionalServiceDetail departure) {
		this.departure = departure;
	}

	public Date getArrival_date() {
		return arrival_date;
	}

	public void setArrival_date(Date arrival_date) {
		this.arrival_date = arrival_date;
	}

	public Date getDeparture_date() {
		return departure_date;
	}

	public void setDeparture_date(Date departure_date) {
		this.departure_date = departure_date;
	}

	public Long getCreator_user_id() {
		return creator_user_id;
	}

	public void setCreator_user_id(Long creator_user_id) {
		this.creator_user_id = creator_user_id;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public Date getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}

	public Long getInitiator_user_id() {
		return initiator_user_id;
	}

	public void setInitiator_user_id(Long initiator_user_id) {
		this.initiator_user_id = initiator_user_id;
	}

	public Long getReservations_id() {
		return reservations_id;
	}

	public void setReservations_id(Long reservations_id) {
		this.reservations_id = reservations_id;
	}
	
	public short getDays(){
		return ((short) (((departure_date.getTime() - arrival_date.getTime()) / (24 * 60 * 60 * 1000)) + 1));
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}
}
