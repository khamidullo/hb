package uz.hbs.utils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uz.hbs.MyWebApplication;
import uz.hbs.beans.Account;
import uz.hbs.beans.Currencies;
import uz.hbs.beans.Guest;
import uz.hbs.beans.Hotel;
import uz.hbs.beans.Insurance;
import uz.hbs.beans.Manager;
import uz.hbs.beans.User;
import uz.hbs.db.MyBatisHelper;

public class CommonUtil {
	private static final Logger _log = LoggerFactory.getLogger(CommonUtil.class);
	public static Date getLastDateOfYear() {
		return getLastDateOfYear(new Date());
	}

	public static Date getLastDateOfYear(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MONTH, Calendar.DECEMBER);
		calendar.set(Calendar.DAY_OF_MONTH, 31);
		return calendar.getTime();
	}
	
	public static Date getLastDateOfMonth() {
		return getLastDateOfMonth(new Date());
	}

	public static Date getLastDateOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return calendar.getTime();
	}
	
	public static Date getNextDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		return calendar.getTime();
	}

	public static Date today() {
		Calendar calendar = Calendar.getInstance();
		Date date = new MyBatisHelper().selectOne("selectNow");
		calendar.setTime(date);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}

	public static Date getManualDate(Date date, int interval_day, int hour, int minute) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, interval_day);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	public static Date getManualDate(Date date, int interval_day, String time) {
		String dt = DateUtil.toString(date, MyWebApplication.DATE_FORMAT) + " " + time;
		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTime(DateUtil.parseDate(dt, MyWebApplication.DATE_TIME_FORMAT));
			calendar.add(Calendar.DAY_OF_MONTH, interval_day);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			return calendar.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Date now() {
		return new MyBatisHelper().selectOne("selectNow");
	}

	public static String getImageMimeType(String filename) {
		if (filename.toLowerCase().endsWith(".gif")) {
			return "gif";
		} else if (filename.toLowerCase().endsWith(".png")) {
			return "png";
		} else if (filename.toLowerCase().endsWith(".jpeg")) {
			return "jpeg";
		} else if (filename.toLowerCase().endsWith(".jpg")) {
			return "jpg";
		} else if (filename.toLowerCase().endsWith(".bmp")) {
			return "bmp";
		}
		return null;
	}

	public static List<Integer> getIntegerList(Integer count, boolean startFromZero) {
		List<Integer> list = new ArrayList<Integer>();
		int start = 1;
		if (startFromZero) {
			start = 0;
		}
		for (int i = start; i <= count; i++) {
			list.add(i);
		}
		return list;
	}
	
	public static List<Short> getShortList(short count, boolean startFromZero) {
		List<Short> list = new ArrayList<Short>();
		short start = 1;
		if (startFromZero) start = 0;
		
		for (Short i = start; i <= count; i++) {
			list.add(i);
		}
		return list;
	}

	public static short nvl(Short s) {
		if (s == null)
			return 0;
		return s;
	}

	public static String nvl(String s) {
		if (s == null)
			return "";
		if ("null".equalsIgnoreCase(s))
			return "";
		return s;
	}

	public static byte nvl(Byte b) {
		if (b == null)
			return 0;
		return b;
	}
	
	public static byte nvl(Byte b, byte default_byte) {
		if (b == null) return default_byte;
		return b;
	}

	public static boolean nvl(Boolean b) {
		if (b == null)
			return false;
		return b;
	}

	public static double nvl(Double d) {
		if (d == null)
			return 0;
		return d;
	}

	public static int nvl(Integer i) {
		if (i == null)
			return 0;
		return i;
	}

	public static float nvl(Float f) {
		if (f == null)
			return 0;
		return f;
	}

	public static BigDecimal nvl(BigDecimal d) {
		if (d == null)
			return new BigDecimal("0");
		return d;
	}

	public static String getWeekDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		return "week.day." + calendar.get(Calendar.DAY_OF_WEEK);
	}

	public static Date date_trunc(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	public static boolean isTodayCheckIn(Date date, Date date2) {
		return date_trunc(date).equals(date_trunc(date2));
	}

	public static void setFormComponentRequired(Form<?> form) {
		form.visitChildren(new IVisitor<Component, Void>() {

			@Override
			public void component(Component component, IVisit<Void> visit) {
				if (component instanceof FormComponent) {
					FormComponent<?> formComponent = (FormComponent<?>) component;
					if (formComponent.isRequired()) {
						formComponent.add(new AttributeModifier("required", true));
					}
				}
			}
		});
	}

	public static void setFormComponentDisabled(Form<?> form) {
		form.visitChildren(new IVisitor<Component, Void>() {

			@Override
			public void component(Component component, IVisit<Void> visit) {
				if (component instanceof FormComponent) {
					FormComponent<?> formComponent = (FormComponent<?>) component;
					if (formComponent instanceof TextField) {
						formComponent.setEnabled(false);
					} else if (formComponent instanceof DropDownChoice) {
						formComponent.setEnabled(false);
					} else if (formComponent instanceof RadioChoice) {
						formComponent.setEnabled(false);
					} else if (formComponent instanceof CheckBox) {
						formComponent.setEnabled(false);
					} else if (formComponent instanceof CheckGroup) {
						formComponent.setEnabled(false);
					}
				}
			}
		});
	}

	public static void setFormComponentRequired(MarkupContainer container) {
		container.visitChildren(FormComponent.class, new IVisitor<Component, Void>() {

			@Override
			public void component(Component component, IVisit<Void> visit) {
				FormComponent<?> formComponent = (FormComponent<?>) component;
				if (formComponent.isRequired()) {
					formComponent.add(new AttributeModifier("required", true));
				} else {
					formComponent.add(new AttributeModifier("required", false));
				}
			}
		});
	}

	public static void setFormComponentRequiredPanel(Panel panel) {
		panel.visitChildren(FormComponent.class, new IVisitor<Component, Void>() {

			@Override
			public void component(Component component, IVisit<Void> visit) {
				FormComponent<?> formComponent = (FormComponent<?>) component;
				if (formComponent.isRequired()) {
					formComponent.add(new AttributeModifier("required", true));
				}
			}
		});
	}

	public static String setFormValidator(Form<?> form) {
		final StringBuffer jscript = new StringBuffer();
		jscript.append("$(document).ready(function() {\n");
		jscript.append("$('#" + form.getMarkupId() + "').formValidation({\n");
		// jscript.append("framework: 'bootstrap',\n");
		jscript.append("icon: { valid: 'fa fa-check', invalid: 'fa fa-times', validating: 'fa fa-refresh', feedback: 'fv-control-feedback'},\n");
		jscript.append("fields: {\n");
		form.visitChildren(FormComponent.class, new IVisitor<Component, Void>() {
			int i = 0;

			@Override
			public void component(Component component, IVisit<Void> visit) {
				FormComponent<?> fc = (FormComponent<?>) component;
				if (fc.isRequired()) {
					if (i > 0)
						jscript.append(",\n");

					jscript.append(fc.getId() + ": {\n");
					jscript.append("validators: {\n");
					if (fc.getLabel() != null)
						jscript.append("notEmpty: { message: '"
								+ new StringResourceModel("required", null, new Object[] { fc.getLabel().getObject() }).getString() + "' }\n");
					else
						jscript.append("notEmpty: { message: '"
								+ new StringResourceModel("required", null, new Object[] { "this field" }).getString() + "' }\n");
					if (fc instanceof EmailTextField) {
						jscript.append(",\n");
						jscript.append("emailAddress: { message: 'The input is not a valid email address' }\n");
					}
					jscript.append("}\n");
					jscript.append("}\n");
					i++;
				}
				visit.dontGoDeeper();
			}
		});
		jscript.append("}\n");
		jscript.append("});\n");
		jscript.append("});\n");
		return jscript.toString();
	}

	public static String lpad(Integer i, int size, char c) {
		return String.format("%" + c + size + "d", i);
	}

	public static String lpadZero(Integer i) {
		return lpad(nvl(i), 2, '0');
	}

	public static String lpadZeroB(Byte b) {
		return lpad((int) nvl(b), 2, '0');
	}

	public static String getHotelTime(String time) {
		return time.substring(0, time.lastIndexOf(":"));
	}

	/**
	 * Hotel (users also), ToutAgency (users also) and Spectator login generator<br>
	 * <br>
	 * Предлагаю поступить следующим образом (возможно, ты предложишь другой более простой но удобный вариант):<br>
	 * Все аккаунты делить на три категории:<br>
	 * - Отели<br>
	 * - Тур фирмы<br>
	 * - Корпоративные Клиенты<br>
	 * <br>
	 * Каждой категории присвоить ID<br>
	 * - hot<br>
	 * - ta<br>
	 * - corp<br>
	 * <br>
	 * Дальше каждому Клиенту присваивать номер<br>
	 * <br>
	 * hot-0001<br>
	 * ta-001<br>
	 * corp-001<br>
	 * <br>
	 * Теперь, чтобы иметь возможность подключать на один отель/тур.фирму/корпорацию несколько человек, присваивать им еще номер<br>
	 * <br>
	 * hot-0001-01, hot-0001-02, hot-0001-03 и т.д.<br>
	 * ta-001-01, ta-001-02, ta-001-03 и т.д.<br>
	 * corp-001-01, corp-001-02, corp-001-04 и т.д<br>
	 * <br>
	 * А в системе мы как администраторы должны видеть весь объем corp-001 а не только одного их оператора.<br>
	 * 
	 * @param user
	 *            - User instance
	 * @param sql
	 *            - SqlSession instance
	 */
	public static void generateLogin(User user, SqlSession sql) {
		if (user.getType().getId().byteValue() == User.TYPE_TOURAGENCY) {
			Long sequence = sql.selectOne("selectTourAgencySequence");
			user.setLogin((user.isCorpUser() ? User.LOGIN_PREFIX_TOURAGENCY_CORPORATE : User.LOGIN_PREFIX_TOURAGENCY) + "-" + String.format("%04d", sequence));
		} else if (user.getType().getId().byteValue() == User.TYPE_HOTEL) {
			Long hotelsSequence = sql.selectOne("selectHotelsSequence");
			user.setLogin(User.LOGIN_PREFIX_HOTEL + "-" + String.format("%04d", hotelsSequence));
		} else if (user.getType().getId().byteValue() == User.TYPE_SPECTATOR_USER) {
			Long sequence = sql.selectOne("selectSpectatorSequence");
			user.setLogin(User.LOGIN_PREFIX_SPECTATOR + "-" + String.format("%04d", sequence));
		} else if (user.getType().getId().byteValue() == User.TYPE_HOTEL_USER || user.getType().getId().byteValue() == User.TYPE_TOURAGENT_USER) {
			Map<String, Serializable> params = new HashMap<String, Serializable>();
			params.put("workusers_id", user.getWork().getId());
			params.put("statusAll", "yes");
			List<User> usersList = sql.selectList("selectUsers", params);
			int sequence = 1;
			if (!usersList.isEmpty()) {
				List<Integer> userSequenceList = new ArrayList<Integer>();
				for (User user2 : usersList) {
					userSequenceList.add(Integer.parseInt(user2.getLogin().substring(user2.getLogin().lastIndexOf("-") + 1)));
				}
				sequence = Collections.max(userSequenceList) + 1;
			}
			User ownerUser = new MyBatisHelper().selectOne("selectUserByUserId", user.getWork().getId().longValue());
			user.setLogin(ownerUser.getLogin() + "-" + String.format("%03d", sequence));
		}
	}

	public static int getUsersCount(Byte type, Byte status) {
		Map<String, Byte> params = new HashMap<String, Byte>();
		params.put("type", type);
		params.put("status", status);

		Integer result = new MyBatisHelper().selectOne("selectUserTypeStatusCount", params);

		return result == null ? 0 : result;
	}

	public static Account insertUserAccount(SqlSession sql, User user) {
		Account acc = new Account();
		acc.setUsers_id(user.getId());
		acc.setBalance((double) 0);
		acc.setStatus(Account.STATUS_ACTIVE);

		sql.insert("insertAccounts", acc);

		return acc;
	}

	public static <T> String listToString(List<T> list) {
		StringBuffer sb = new StringBuffer();
		for (T t : list) {
			sb.append(t).append(",");
		}
		String s = sb.toString();
		return s.substring(0, s.length() - 1);
	}

	public static boolean equals(String a, String b) {
		return (a == b || (a != null && a.equals(b)));
	}
	
	public static boolean equals2(String a, String b) {
		return a == null ? b == null : a.equals(b);
	}
	
	public static boolean equalsIgnoreCase(String a, String b) {
		return (a == b || (a != null && a.equalsIgnoreCase(b)));
	}
	
	public static String getUserEmail(User user) {
		String result = null;
		if (user.getType().getId() == User.TYPE_ADMIN_USER) {
			result = user.getEmail();
		} else if (user.getType().getId() == User.TYPE_HOTEL_USER) {
			Hotel hotel = new MyBatisHelper().selectOne("selectHotelFullyByUserId", user.getWork().getId());
			if (hotel != null)
				result = hotel.getManager_email();
		} else if (user.getType().getId() == User.TYPE_TOURAGENT_USER) {
			Manager manager = new MyBatisHelper().selectOne("selectManagers", user.getWork().getId());
			if (manager != null)
				result = manager.getContact_email();
		} else if (user.getType().getId() == User.TYPE_SPECTATOR_USER) {
			result = user.getEmail();//TODO aniqlashtirish kerak
		} else if (user.getType().getId() == User.TYPE_API) {
			result = user.getEmail();
		}
		return result;
	}
	
	public static void main(String[] args) {
		_log.debug(parseAmount(111111.58));
		_log.debug(parseAmount(111111));
	}
	
	public static List<Float> getNearByPlaceList(){
		List<Float> list = new ArrayList<Float>();
		for (float f = 0; f <= 200; f += ((f < 1)?0.1:(f < 25)?0.5:(f < 100)?5:25)) {
			list.add(FormatUtil.format(f));
		}
		return list;
	}
	
	public static String getTimeWithOutSec(String s) {
		if (s == null || s.length() <= 5) return "";
		return s.substring(0, 5);
	}
	
	public static String parseAmount(double amount) {
		DecimalFormat df = new DecimalFormat("#,##0.##");
		DecimalFormatSymbols dfs = df.getDecimalFormatSymbols();
		dfs.setGroupingSeparator(' ');
		df.setGroupingUsed(true);
		df.setDecimalFormatSymbols(dfs);
		return df.format(amount);
	}
	
	public static List<Insurance> getInsuranceList(short count, long user_id){
		List<Insurance> list = new ArrayList<Insurance>();
		if (count >= 1) {
			for (short sh = 1; sh <= count; sh++) {
				list.add(new Insurance(user_id));
			}
		}
		return list;
	}
	
	public static List<Guest> getGuestList(List<Guest> list, int person){
		int size = list.size(); 
		if (size < person) {
			for (int i = 1; i <= person - size; i++){
				list.add(new Guest((short) (size + i)));
			}
		} else if (size > person) {
			for (int i = size - 1; i >= person; i--) {
				Long guestId = ((Guest) list.get(i)).getId();
				if (guestId == null || (guestId != null && new MyBatisHelper().delete("deleteGuestById", guestId) != 0)) list.remove(i);
			}
		}
		return list;
	}
	
	public static List<Insurance> getInsuranceList(List<Insurance> list, int person, long user_id){
		int size = list.size(); 
		if (size < person) {
			for (int i = 1; i <= person - size; i++){
				list.add(new Insurance(user_id));
			}
		} else if (size > person) {
			for (int i = size - 1; i >= person; i--) {
				list.remove(i);
			}
		}
		return list;
	}
	
	public static double currencyConverted(Currencies currency, Double value) {
		return nvl(value * currency.getValue());
	}

	public static BigDecimal currencyConverted(Currencies currency, BigDecimal value) {
		return nvl(value.multiply(new BigDecimal(currency.getValue())));
	}
}
