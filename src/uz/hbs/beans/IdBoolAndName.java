package uz.hbs.beans;

import org.apache.wicket.util.io.IClusterable;

public class IdBoolAndName implements IClusterable {
	private static final long serialVersionUID = 1L;
	protected boolean id;
	protected String name;
	
	public IdBoolAndName() {
	}
	
	public IdBoolAndName(boolean id) {
		this.id = id;
	}
	
	public IdBoolAndName(boolean id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public boolean isId() {
		return id;
	}
	
	public void setId(boolean id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return "id = " + id + ", name = " + name;
	}
}
