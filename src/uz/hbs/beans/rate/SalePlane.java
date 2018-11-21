package uz.hbs.beans.rate;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.wicket.util.io.IClusterable;

import uz.hbs.beans.IdAndName;

public class SalePlane implements IClusterable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Date create_date;
	private Date sale_date;
	private BigDecimal sale;
	private BigDecimal sale_uz;
	private Date update_date;
	private long initiator_user_id;
	private Long hotelsusers_id;
	private boolean status;
	private boolean checked;
	
	private Integer roomtypes_id;
	private IdAndName roomtype;
	private boolean is_group;
	private boolean resident;
	
	private Short person_number;
	
	public SalePlane() {
	}
	
	public SalePlane(long hotelsusers_id, int roomtypes_id, short person_number, boolean is_group) {
		this.hotelsusers_id = hotelsusers_id;
		this.roomtypes_id = roomtypes_id;
		this.person_number = person_number;
		this.is_group = is_group;
	}
	
	public SalePlane(long hotelsusers_id, int roomtypes_id, short person_number, Date sale_date, boolean is_group) {
		this.hotelsusers_id = hotelsusers_id;
		this.roomtypes_id = roomtypes_id;
		this.person_number = person_number;
		this.is_group = is_group;
		this.sale_date = sale_date;
	}
	
	public SalePlane(long hotelsusers_id, int roomtypes_id, boolean is_group) {
		this.hotelsusers_id = hotelsusers_id;
		this.roomtypes_id = roomtypes_id;
		this.is_group = is_group;
	}
	
	public SalePlane(SalePlane plane, Date sale_date, long initiator_user_id) {
		this.hotelsusers_id = plane.getHotelsusers_id();
		this.roomtypes_id = plane.getRoomtypes_id();
		this.person_number = plane.getPerson_number();
		this.is_group = plane.isIs_group();
		this.sale = plane.getSale();
		this.sale_uz = plane.getSale_uz();
		this.sale_date = sale_date;
		this.initiator_user_id = initiator_user_id;
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

	public Date getSale_date() {
		return sale_date;
	}

	public void setSale_date(Date sale_date) {
		this.sale_date = sale_date;
	}

	public BigDecimal getSale() {
		return sale;
	}

	public void setSale(BigDecimal sale) {
		this.sale = sale;
	}

	public BigDecimal getSale_uz() {
		return sale_uz;
	}

	public void setSale_uz(BigDecimal sale_uz) {
		this.sale_uz = sale_uz;
	}

	public Date getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}

	public long getInitiator_user_id() {
		return initiator_user_id;
	}

	public void setInitiator_user_id(long initiator_user_id) {
		this.initiator_user_id = initiator_user_id;
	}

	public Long getHotelsusers_id() {
		return hotelsusers_id;
	}

	public void setHotelsusers_id(Long hotelsusers_id) {
		this.hotelsusers_id = hotelsusers_id;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public Integer getRoomtypes_id() {
		return roomtypes_id;
	}

	public void setRoomtypes_id(Integer roomtypes_id) {
		this.roomtypes_id = roomtypes_id;
	}

	public IdAndName getRoomtype() {
		return roomtype;
	}

	public void setRoomtype(IdAndName roomtype) {
		this.roomtype = roomtype;
	}

	public boolean isIs_group() {
		return is_group;
	}

	public void setIs_group(boolean is_group) {
		this.is_group = is_group;
	}

	public boolean isResident() {
		return resident;
	}

	public void setResident(boolean resident) {
		this.resident = resident;
	}

	public Short getPerson_number() {
		return person_number;
	}

	public void setPerson_number(Short person_number) {
		this.person_number = person_number;
	}
	
	public BigDecimal getSale(boolean resident) {
		return resident ? (sale_uz != null ? sale_uz : sale) : sale;
	}
}
