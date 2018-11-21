package uz.hbs.beans;

import java.lang.reflect.Field;
import java.util.Date;

import org.apache.wicket.util.io.IClusterable;

public class UserSettings implements IClusterable {
	private static final long serialVersionUID = 1L;
	
	public static final int DEFAULT_TABLE_ROWS = 20;
	
	private Long users_id;
	private Integer table_rows;
	private Date create_date;
	private Date update_date;

	public UserSettings() {
	}

	@Override
	public String toString() {
		Field[] fields = this.getClass().getDeclaredFields();
		Field[] superFields = this.getClass().getSuperclass().getDeclaredFields();
		String s = null;
		try {
			s = "{";
			for (int i = 0; i < superFields.length; i++) {
				if (!superFields[i].getName().equals("serialVersionUID") && fields[i].getModifiers() != 25 && fields[i].get(this) != null)
					s += superFields[i].getName() + "=" + superFields[i].get(this) + ", ";
			}
			for (int i = 0; i < fields.length; i++) {
				if (!fields[i].getName().equals("serialVersionUID") && fields[i].getModifiers() != 25 && fields[i].get(this) != null)
					s += fields[i].getName() + "=" + fields[i].get(this) + (i < fields.length - 1 ? ", " : "");
			}
			s += "}";
			s = s.lastIndexOf(", }") != -1 ? s.substring(0, s.lastIndexOf(", }")) + "}" : s;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}

	public Long getUsers_id() {
		return users_id;
	}

	public void setUsers_id(Long users_id) {
		this.users_id = users_id;
	}

	public Integer getTable_rows() {
		return table_rows;
	}

	public void setTable_rows(Integer table_rows) {
		this.table_rows = table_rows;
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
}
