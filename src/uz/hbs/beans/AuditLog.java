package uz.hbs.beans;

import java.util.Date;

import org.apache.wicket.util.io.IClusterable;

public class AuditLog implements IClusterable {
	private static final long serialVersionUID = 1L;
	
	public static final byte LEVEL_INFO = 0;
	public static final byte LEVEL_DEBUG = 1;
	public static final byte LEVEL_TRACE = 2;
	public static final byte LEVEL_ERROR = 3;
	
	private Long id;
	private Byte log_level;
	private String actions_id;
	private Long initiator_user_id;
	private Long usersessions_id;
	private String description;
	private Date create_date;
	private Date update_date;
	
	public AuditLog() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Byte getLog_level() {
		return log_level;
	}

	public void setLog_level(Byte log_level) {
		this.log_level = log_level;
	}

	public String getActions_id() {
		return actions_id;
	}

	public void setActions_id(String actions_id) {
		this.actions_id = actions_id;
	}

	public Long getInitiator_user_id() {
		return initiator_user_id;
	}

	public void setInitiator_user_id(Long initiator_user_id) {
		this.initiator_user_id = initiator_user_id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public Long getUsersessions_id() {
		return usersessions_id;
	}

	public void setUsersessions_id(Long usersessions_id) {
		this.usersessions_id = usersessions_id;
	}
}
