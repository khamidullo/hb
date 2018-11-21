package uz.hbs.beans;

public class ServiceType extends IdByteAndName {
	private static final long serialVersionUID = 1L;

	public ServiceType() {
	}
	
	public ServiceType(byte id) {
		this.id = id;
	}

	public ServiceType(byte id, String name) {
		this.id = id;
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
