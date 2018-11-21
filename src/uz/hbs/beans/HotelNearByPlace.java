package uz.hbs.beans;


public class HotelNearByPlace extends IdAndName {
	private static final long serialVersionUID = 1L;
	public static final byte OTHERs = 9;  
	public static final byte AIROPORT = 1;  
	public static final byte TRAIN = 2;  
	public static final byte CITY_CENTER = 3;  
	public static final byte METRO = 4;  
	
	private long hotelsusers_id;
	private Float value;
	private byte type;
	private long initiator_user_id;
	
	public HotelNearByPlace() {
	}
	
	public HotelNearByPlace(String name, Float value, byte type) {
		this.name = name;
		this.type = type;
		this.value = value;
	}
	
	public HotelNearByPlace(long hotelsusers_id) {
		this.hotelsusers_id = hotelsusers_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getHotelsusers_id() {
		return hotelsusers_id;
	}

	public void setHotelsusers_id(long hotelsusers_id) {
		this.hotelsusers_id = hotelsusers_id;
	}

	public Float getValue() {
		return value;
	}

	public void setValue(Float value) {
		this.value = value;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public long getInitiator_user_id() {
		return initiator_user_id;
	}

	public void setInitiator_user_id(long initiator_user_id) {
		this.initiator_user_id = initiator_user_id;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HotelNearByPlace) {
			if (this.getId() != null && ((HotelNearByPlace) obj).getId().equals(this.getId())) return true;
		}
		return super.equals(obj);
	}
}
