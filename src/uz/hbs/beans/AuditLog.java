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
	private Long user_id;
	private Long session_id;
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

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public Long getSession_id() {
		return session_id;
	}

	public void setSession_id(Long session_id) {
		this.session_id = session_id;
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

	@Override
	public String toString() {
		return "AuditLog [id=" + id + ", log_level=" + log_level + ", actions_id=" + actions_id + ", user_id=" + user_id
				+ ", session_id=" + session_id + ", description=" + description + ", create_date=" + create_date
				+ ", update_date=" + update_date + "]";
	}
}