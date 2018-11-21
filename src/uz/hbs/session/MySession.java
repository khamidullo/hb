package uz.hbs.session;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uz.hbs.beans.Action;
import uz.hbs.beans.Currencies;
import uz.hbs.beans.User;
import uz.hbs.beans.UserSession;
import uz.hbs.beans.UserSettings;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.enums.ActionRight;

public class MySession extends AuthenticatedWebSession {
	private static final long serialVersionUID = 1L;
	private final static Logger logger = LoggerFactory.getLogger(MySession.class);
	private User user;
	private Map<String, Integer> loginmap = new HashMap<String, Integer>();
	private int max_attempt = 3;
	private int attempt = 0;
	private Currencies currency;
	private String feedbackMessage;
	private UserSession lastUserSession;
	private UserSettings settings;
	private Long usersessions_id;
	
	public MySession(Request request) {
		super(request);
	}

	@Override
	public boolean authenticate(final String username, final String password) {
		if (user == null) {
			try {
				 Map<String, Serializable> map = new HashMap<String, Serializable>();
				 map.put("login", username);
				 map.put("password", password);
				 map.put("status", User.STATUS_ACTIVE);
				 if (username.toLowerCase().startsWith("api-")) {
					 map.put("type", User.TYPE_API);
				 }
				 user = (User) new MyBatisHelper().selectOne("selectUsers", map);
				 if (user != null) {
					 map.clear();
					 map.put("users_id", user.getId());
					 map.put("type", Action.TYPE_FOR_ROLE_OR_LOG);

					 Map<ActionRight, Action> actionsMap = new MyBatisHelper().selectMap("selectUserActions", map, "id");
					 
					 user.setActionMap(actionsMap);
					 
					 settings = new MyBatisHelper().selectOne("selectUserSettings", user.getId());
					 if (settings == null) {
						settings = new UserSettings();
						settings.setUsers_id(user.getId());
						settings.setTable_rows(UserSettings.DEFAULT_TABLE_ROWS);
						new MyBatisHelper().insert("insertUserSettings", settings);
					}
				 }
			} catch (Exception e) {
				logger.error("Exception", e);
			}
		}
		if (user == null) {
			if (loginmap.containsKey(username)) {
				attempt = loginmap.get(username);
			}
			attempt ++;
			loginmap.put(username, attempt);
		} else if (user != null) {
			loginmap.remove(username);
		}
		return user != null;
	}

	@Override
	public Roles getRoles() {
		return null;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean isCaptchaVisible(){
		return max_attempt <= (attempt + 1);
	}

	public Currencies getCurrency() {
		return currency;
	}

	public void setCurrency(Currencies currency) {
		this.currency = currency;
	}

	public String getCurrencyName() {
		return new StringResourceModel("currency." + currency.getName().toLowerCase(), null).getString();
	}

	public String getFeedbackMessage() {
		return feedbackMessage;
	}

	public void setFeedbackMessage(String feedbackMessage) {
		this.feedbackMessage = feedbackMessage;
	}

	public UserSession getLastUserSession() {
		return lastUserSession;
	}

	public void setLastUserSession(UserSession lastUserSession) {
		this.lastUserSession = lastUserSession;
	}

	public UserSettings getSettings() {
		return settings;
	}

	public void setSettings(UserSettings settings) {
		this.settings = settings;
	}

	public Long getUsersessions_id() {
		return usersessions_id;
	}

	public void setUsersessions_id(Long usersessions_id) {
		this.usersessions_id = usersessions_id;
	}
}
