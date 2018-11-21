package uz.hbs.beans;

import org.apache.wicket.util.io.IClusterable;

public class Country implements IClusterable {
	private static final long serialVersionUID = 1L;
	public static final int UZBEKISTAN = 1;
	private Integer id;
	private String name;
	private String code;
	private Integer phone_code;
	
	public Country() {
	}
	
	public Country(Integer id) {
		this.id = id;
	}

	public Country(String code, String name) {
		this.code = code;
		this.name = name;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getPhone_code() {
		return phone_code;
	}

	public void setPhone_code(Integer phone_code) {
		this.phone_code = phone_code;
	}
}
