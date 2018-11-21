package uz.hbs.beans;


public class AdditionalBed extends IdBoolAndName {
	private static final long serialVersionUID = 1L;
	public static final boolean APPLICABLE = true;
	public static final boolean INAPPLICABLE = false;
	
	public AdditionalBed() {
	}

	public AdditionalBed(boolean id) {
		this.id = id;
	}
	
	public AdditionalBed(boolean id, String name) {
		this.id = id;
		this.name = name;
	}
	
}
