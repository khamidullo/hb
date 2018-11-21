package uz.hbs.beans;

import java.io.Serializable;

public class ChildAge implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final byte UNKNOWN = -1;
	private short age;
	private short child_index;
	private boolean minimum_free_age;
	private ReservationRoom reserveroom;
	
	public ChildAge() {
	}
	
	public ChildAge(short child_index, short age) {
		this.child_index = child_index;
		this.age = age;
	}
	
	public ChildAge(short age) {
		this.age = age;
	}

	public short getAge() {
		return age;
	}

	public void setAge(short age) {
		this.age = age;
	}

	public short getChild_index() {
		return child_index;
	}

	public void setChild_index(short child_index) {
		this.child_index = child_index;
	}

	public boolean isMinimum_free_age() {
		return minimum_free_age;
	}

	public void setMinimum_free_age(boolean minimum_free_age) {
		this.minimum_free_age = minimum_free_age;
	}

	public ReservationRoom getReserveroom() {
		return reserveroom;
	}

	public void setReserveroom(ReservationRoom reserveroom) {
		this.reserveroom = reserveroom;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ChildAge) {
			if (((ChildAge) obj).getChild_index() == child_index && ((ChildAge) obj).getAge() == age) return true; 
		}
		return super.equals(obj);
	}
	
	@Override
	public String toString() {
		return String.valueOf(age);
	}
}
