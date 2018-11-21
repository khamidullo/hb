package uz.hbs.beans;

import org.apache.wicket.util.io.IClusterable;

public class Nationality implements IClusterable {
	private static final long serialVersionUID = 1L;
	private String code;
	private String name;
	
	public Nationality() {
		// TODO Auto-generated constructor stub
	}
	
	public Nationality(String code) {
		this.code = code;
	}
	
	public Nationality(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
}
