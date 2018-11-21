package uz.hbs.beans;

import java.util.Date;

import org.apache.wicket.util.io.IClusterable;

public class UserSession implements IClusterable {
	private static final long serialVersionUID = 1L;

	public static final byte INTERFACE_TYPE_WEB = 0;
	public static final byte INTERFACE_TYPE_MOBILE = 1;
	
	public static final byte APP_TYPE_BACK_OFFICE_WEB = 0;
	public static final byte APP_TYPE_BACK_OFFICE_MOBILE_ANDROID = 1;
	public static final byte APP_TYPE_BACK_OFFICE_MOBILE_IOS = 2;
	
	private Long id;
	private String session_id;
	private Byte interface_type;
	private Byte app_type;
	private Long initiator_user_id;
	private String client_host;
	private String client_info;
	private Date create_date;

	public UserSession() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSession_id() {
		return session_id;
	}

	public void setSession_id(String session_id) {
		this.session_id = session_id;
	}

	public Byte getInterface_type() {
		return interface_type;
	}

	public void setInterface_type(Byte interface_type) {
		this.interface_type = interface_type;
	}

	public Byte getApp_type() {
		return app_type;
	}

	public void setApp_type(Byte app_type) {
		this.app_type = app_type;
	}

	public Long getInitiator_user_id() {
		return initiator_user_id;
	}

	public void setInitiator_user_id(Long initiator_user_id) {
		this.initiator_user_id = initiator_user_id;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public String getClient_info() {
		return client_info;
	}

	public void setClient_info(String client_info) {
		this.client_info = client_info;
	}

	public String getClient_host() {
		return client_host;
	}

	public void setClient_host(String client_host) {
		this.client_host = client_host;
	}
}
