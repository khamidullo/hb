package uz.hbs.beans;

import org.apache.wicket.util.io.IClusterable;

public class KeyAndValue implements IClusterable {
	private static final long serialVersionUID = 1L;

	private String key;
	private String value;

	public KeyAndValue() {
	}

	public KeyAndValue(String key) {
		this.key = key;
	}

	public KeyAndValue(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "{key=" + key + ", value=" + value + "}";
	}
}
