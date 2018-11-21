package uz.hbs.beans;

import java.util.Date;
import java.util.Map;

import org.apache.wicket.util.io.IClusterable;

import uz.hbs.enums.ActionRight;

public class User implements IClusterable {
	private static final long serialVersionUID = 1L;

	public static final byte TYPE_UNDEFINED = 0;
	public static final byte TYPE_ADMIN_USER = 1;
	public static final byte TYPE_HOTEL_USER = 2;
	public static final byte TYPE_TOURAGENT_USER = 3;
	public static final byte TYPE_SPECTATOR_USER = 4;
//	public static final byte TYPE_CUSTOMER_USER = 5;//B2C
	public static final byte TYPE_HOTEL = 20;
	public static final byte TYPE_TOURAGENCY = 30;
	public static final byte TYPE_API = 50; //B2C

	public static final byte STATUS_ALL = -1;
	public static final byte STATUS_NEW = 0;
	public static final byte STATUS_ACTIVE = 1;
	public static final byte STATUS_DISABLED = 2;
	public static final byte STATUS_DELETED = 3;

	public static final String LOGIN_PREFIX_HOTEL = "hot";
	public static final String LOGIN_PREFIX_TOURAGENCY = "ta";
	public static final String LOGIN_PREFIX_TOURAGENCY_CORPORATE = "corp";
	public static final String LOGIN_PREFIX_SPECTATOR = "s";
	public static final String LOGIN_PREFIX_API = "api";

	public static final long USER_NIGHT_AUDIT = -1;
	public static final long USER_SYSTEM = -2;
	public static final long USER_INTERNAL_TA = -3;

	public static final String HOTELIOS_CORP = LOGIN_PREFIX_TOURAGENCY_CORPORATE + "-0000";
	public static final String HOTELIOS_CORP_TA = HOTELIOS_CORP + "-001";

	private Long id;
	private String login;
	private String password;
	private transient String password2;
	private String name;
	private String email;
	private IdAndValue type;
	private IdAndValue status;
	private Long initiator_user_id;
	private Date create_date;
	private Date update_date;
	private IdLongAndName work;
	private String org_name;
	private Boolean change_password_required;
	private String change_password_token;
	private Date change_password_expiry_date;
	private String phone_number;

	private Long hotelsusers_id;
	private Long touragency_user_id;

	private boolean isCorpUser = false;

	private Map<ActionRight, Action> actionMap;

	private boolean workable = true;

	private String access_key;
	
	public User() {
	}

	public User(long id) {
		this.id = id;
	}

	public User(long id, String name) {
		this.id = id;
		this.name = name;
	}

	public User(long id, String name, IdAndValue status) {
		this.id = id;
		this.name = name;
		this.status = status;
	}

	@Override
	public String toString() {
		return "{id=" + id + ", login=" + login + ", password=" + password + ", name=" + name + ", email=" + email + ", type="
				+ (type != null ? type.getId() : null) + ", status=" + (status != null ? status.getId() : null) + ", initiator_user_id="
				+ initiator_user_id + "}";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
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

	public IdAndValue getType() {
		return type;
	}

	public void setType(IdAndValue type) {
		this.type = type;
	}

	public IdAndValue getStatus() {
		return status;
	}

	public void setStatus(IdAndValue status) {
		this.status = status;
	}

	public Map<ActionRight, Action> getActionMap() {
		return actionMap;
	}

	public void setActionMap(Map<ActionRight, Action> actionMap) {
		this.actionMap = actionMap;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean hasRight(ActionRight action) {
		if (actionMap == null)
			return false;

		return actionMap.containsKey(action);
	}

	public Long getInitiator_user_id() {
		return initiator_user_id;
	}

	public void setInitiator_user_id(Long initiator_user_id) {
		this.initiator_user_id = initiator_user_id;
	}

	public long getHotelsusers_id() {
		return hotelsusers_id;
	}

	public void setHotelsusers_id(long hotelsusers_id) {
		this.hotelsusers_id = hotelsusers_id;
	}

	public IdLongAndName getWork() {
		return work;
	}

	public void setWork(IdLongAndName work) {
		this.work = work;
	}

	public String getPassword2() {
		return password2;
	}

	public void setPassword2(String password2) {
		this.password2 = password2;
	}

	public String getOrg_name() {
		return org_name;
	}

	public void setOrg_name(String org_name) {
		this.org_name = org_name;
	}

	public Long getTouragency_user_id() {
		return touragency_user_id;
	}

	public void setTouragency_user_id(Long touragency_user_id) {
		this.touragency_user_id = touragency_user_id;
	}

	public String getChange_password_token() {
		return change_password_token;
	}

	public void setChange_password_token(String change_password_token) {
		this.change_password_token = change_password_token;
	}

	public Boolean getChange_password_required() {
		return change_password_required;
	}

	public void setChange_password_required(Boolean change_password_required) {
		this.change_password_required = change_password_required;
	}

	public Date getChange_password_expiry_date() {
		return change_password_expiry_date;
	}

	public void setChange_password_expiry_date(Date change_password_expiry_date) {
		this.change_password_expiry_date = change_password_expiry_date;
	}

	public boolean isCorpUser() {
		return isCorpUser;
	}

	public void setCorpUser(boolean isCorpUser) {
		this.isCorpUser = isCorpUser;
	}

	public String getPhone_number() {
		return phone_number;
	}

	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}

	public boolean isWorkable() {
		return workable;
	}

	public void setWorkable(boolean workable) {
		this.workable = workable;
	}

	public String getAccess_key() {
		return access_key;
	}

	public void setAccess_key(String access_key) {
		this.access_key = access_key;
	}
}
