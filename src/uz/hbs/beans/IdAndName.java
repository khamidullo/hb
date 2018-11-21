package uz.hbs.beans;

import org.apache.wicket.util.io.IClusterable;

public class IdAndName implements IClusterable {
	private static final long serialVersionUID = 1L;
	protected Integer id;
	protected String name;
	private Boolean filter_flag;
	
	public IdAndName() {
		
	}

	public IdAndName(int id) {
		this.id = id;
	}
	
	public IdAndName(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public IdAndName(String name) {
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
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null){
			if (id != null) return id.equals(((IdAndName) obj).getId()); 
			if (name != null) return name.equals(((IdAndName) obj).getName()); 
		}
		return super.equals(obj);
	}

	public Boolean getFilter_flag() {
		return filter_flag;
	}

	public void setFilter_flag(Boolean filter_flag) {
		this.filter_flag = filter_flag;
	}
}
