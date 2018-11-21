package uz.hbs.beans;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.util.io.IClusterable;

public class GuestDetail implements IClusterable {
	private static final long serialVersionUID = 1L;
	private long reservations_id;
	private Guest selectedGuest;
	
	private List<Guest> guestlist = new ArrayList<Guest>();
	private List<Child> childlist = new ArrayList<Child>();
	
	public GuestDetail() {
	}
	
	public GuestDetail(List<Guest> guestlist, List<Child> childlist) {
		this.guestlist.addAll(guestlist);
		this.childlist.addAll(childlist);
	}
	
	public GuestDetail(List<Guest> guestlist) {
		this.guestlist.addAll(guestlist);
	}

	public GuestDetail(short guest_count, short child_count, String first_name, String last_name) {
		guestlist = new ArrayList<Guest>(guest_count);
		childlist = new ArrayList<Child>(child_count);
		init(guest_count, child_count, first_name, last_name);
	}
	
	public long getReservations_id() {
		return reservations_id;
	}

	public void setReservations_id(long reservations_id) {
		this.reservations_id = reservations_id;
	}
	
	public List<Guest> getGuestlist() {
		return guestlist;
	}

	public void setGuestlist(List<Guest> guestlist) {
		this.guestlist = guestlist;
	}

	public List<Child> getChildlist() {
		return childlist;
	}

	public void setChildlist(List<Child> childlist) {
		this.childlist = childlist;
	}

	private void init(short guest_count, short child_count, String first_name, String last_name){
		for (short guest = 0; guest < guest_count; guest++){
			if (guest == 0) guestlist.add(new Guest((byte) (guest + 1), first_name, last_name, null));
			else guestlist.add(new Guest((byte) (guest + 1)));
		}
		for (short child = 0; child < child_count; child++){
			childlist.add(new Child((byte) (child + 1)));
		}
	}

	public Guest getSelectedGuest() {
		return selectedGuest;
	}

	public void setSelectedGuest(Guest selectedGuest) {
		this.selectedGuest = selectedGuest;
	}
	
	public static short nvl(Short sh){
		if (sh == null) return 0;
		return sh;
	}
}
