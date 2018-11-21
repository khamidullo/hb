package uz.hbs.beans;

import org.apache.wicket.util.io.IClusterable;

public class IdLongAndName implements IClusterable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	
	public IdLongAndName() {
	}

	public IdLongAndName(Long id, String name) {
		setId(id);
		setName(name);
	}

	public IdLongAndName(Long id) {
		setId(id);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
