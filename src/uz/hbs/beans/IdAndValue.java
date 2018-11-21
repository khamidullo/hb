package uz.hbs.beans;

import org.apache.wicket.util.io.IClusterable;

public class IdAndValue implements IClusterable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	private String value;

	public IdAndValue() {
	}

	public IdAndValue(Integer id) {
		this.id = id;
	}

	public IdAndValue(Integer id, String value) {
		this.id = id;
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
