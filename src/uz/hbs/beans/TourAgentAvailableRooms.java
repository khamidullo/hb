package uz.hbs.beans;

import java.util.Date;

import org.apache.wicket.util.io.IClusterable;

import uz.hbs.utils.CommonUtil;

public class TourAgentAvailableRooms implements IClusterable {
	private static final long serialVersionUID = 1L;
	private Integer roomtype_id;
	private Integer available_count;
	private Date available_date;
	private Date create_date;
	private Date update_date;
	private Long initiator_user_id;
	private Integer no_free;

	public TourAgentAvailableRooms() {
	}

	public Integer getRoomtypes_id() {
		return roomtype_id;
	}

	public void setRoomtypes_id(Integer roomtype_id) {
		this.roomtype_id = roomtype_id;
	}

	public Date getAvailable_date() {
		return available_date;
	}

	public void setAvailable_date(Date available_date) {
		this.available_date = available_date;
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

	public Integer getNo_free() {
		return no_free;
	}

	public void setNo_free(Integer no_free) {
		this.no_free = no_free;
	}
	
	public Integer getFree() {
		return CommonUtil.nvl(available_count) - CommonUtil.nvl(no_free);
	}

	public Integer getAvailable_count() {
		return available_count;
	}

	public void setAvailable_count(Integer available_count) {
		this.available_count = available_count;
	}
}
