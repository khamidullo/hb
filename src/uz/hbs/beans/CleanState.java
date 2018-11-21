package uz.hbs.beans;

public class CleanState extends IdByteAndName {
	private static final long serialVersionUID = 1L;
	public static final byte OTHER = -1;
	public static final byte UNCLEAN = 0;
	public static final byte CLEAN = 1;
	
	public CleanState() {
		
	}
	
	public CleanState(CleanState state) {
		this.id = state.getId();
	}
	
	public CleanState(byte id) {
		this.id = id;
	}
	
	public CleanState(byte id, String name) {
		this.id = id;
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
