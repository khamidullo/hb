package uz.hbs.beans;

public class Service extends IdAndName {
	private static final long serialVersionUID = 1L;
	private Long hotelsusers_id;
	
	public Service() {
		super();
	}
	
	public Service(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	public Service(String name) {
		super();
		this.name = name;
	}

	public Long getHotelsusers_id() {
		return hotelsusers_id;
	}

	public void setHotelsusers_id(Long hotelsusers_id) {
		this.hotelsusers_id = hotelsusers_id;
	}
}
