package uz.hbs.beans;

import java.lang.reflect.Field;
import java.util.Date;

import org.apache.wicket.util.io.IClusterable;

public class Contract implements IClusterable {
	private static final long serialVersionUID = 1L;
	private Long users_id;
	private String contract_number;
	private IdAndValue contract_type;
	private Integer commission_type;
	private Double commission_value;
	private Date contract_date;
	private Date create_date;
	private Date update_date;
	
	public static final byte CONTRACT_TYPE_DIRECT = 1;
	public static final byte CONTRACT_TYPE_AGENCY = 2;

	public static final byte COMMISSION_TYPE_PERCENTAGE = 0;
	public static final byte COMMISSION_TYPE_FIXED_AMOUNT = 1;
	
	public Contract() {
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

	public String getContract_number() {
		return contract_number;
	}

	public void setContract_number(String contract_number) {
		this.contract_number = contract_number;
	}

	public Date getContract_date() {
		return contract_date;
	}

	public void setContract_date(Date contract_date) {
		this.contract_date = contract_date;
	}

	public Long getUsers_id() {
		return users_id;
	}

	public void setUsers_id(Long users_id) {
		this.users_id = users_id;
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

	public Double getCommission_value() {
		return commission_value;
	}

	public void setCommission_value(Double commission_value) {
		this.commission_value = commission_value;
	}

	public IdAndValue getContract_type() {
		return contract_type;
	}

	public void setContract_type(IdAndValue contract_type) {
		this.contract_type = contract_type;
	}

	public Integer getCommission_type() {
		return commission_type;
	}

	public void setCommission_type(Integer commission_type) {
		this.commission_type = commission_type;
	}
}
