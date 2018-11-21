package uz.hbs.beans;

import org.apache.wicket.util.io.IClusterable;

public class IdByteAndName implements IClusterable {
	private static final long serialVersionUID = 1L;
	protected Byte id;
	protected String name;
	
	public IdByteAndName() {
	}
	
	public IdByteAndName(byte id) {
		this.id = id;
	}

	public IdByteAndName(byte id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public Byte getId() {
		return id;
	}

	public void setId(Byte id) {
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
