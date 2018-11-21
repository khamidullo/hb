package uz.hbs.beans;

public class Facility extends IdAndName {
	private static final long serialVersionUID = 1L;
	private boolean paid;
	private Long hotelsusers_id;

	public Facility() {
		super();
	}

	public Facility(String name) {
		this.name = name;
	}

	public Facility(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public Facility(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "{id=" + id + ", name=" + name + ", hotelsusers_id=" + hotelsusers_id + ", paid=" + paid + "}";
	}

	public Long getHotelsusers_id() {
		return hotelsusers_id;
	}

	public void setHotelsusers_id(Long hotelsusers_id) {
		this.hotelsusers_id = hotelsusers_id;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null) {
			if (obj instanceof Facility) {
				return this.id.equals(((Facility) obj).getId());
			}
		}
		return false;
	}

	public boolean isPaid() {
		return paid;
	}

	public void setPaid(boolean paid) {
		this.paid = paid;
	}
}
