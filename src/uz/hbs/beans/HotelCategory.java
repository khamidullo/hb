package uz.hbs.beans;

import java.util.Date;

public class HotelCategory extends IdAndName {
	private static final long serialVersionUID = 1L;
	private Date create_date; 
	private Date update_date;
	private Long initiator_user_id;
	
	public HotelCategory() {
	}
	
	public HotelCategory(Integer id) {
		this.id = id;
	}

	public HotelCategory(Integer id, String name) {
		this.id = id;
		this.name = name;
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
}
