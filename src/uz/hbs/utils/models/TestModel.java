package uz.hbs.utils.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import uz.hbs.beans.ChildAge;
import uz.hbs.beans.GroupReservation;
import uz.hbs.beans.IndividualReservation;
import uz.hbs.beans.ReservationDetail;
import uz.hbs.beans.ReservationRoom;
import uz.hbs.beans.RoomType;
import uz.hbs.beans.rate.RatePlane;

public class TestModel {
	
	public static IndividualReservation getIndReserve(){
		List<ChildAge> list = new ArrayList<ChildAge>();
		IndividualReservation reserve = new IndividualReservation();
		reserve.setCheck_in(new Date());
		reserve.setTotal(new BigDecimal("0"));
		reserve.setRateplane(new RatePlane(2));
		
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, 2);
		reserve.setCheck_out(calendar.getTime());
		
		reserve.setHotelsusers_id(3);
		reserve.setAdults((short) 4);
		List<ReservationRoom> reserveroomlist = new ArrayList<ReservationRoom>();
		ReservationRoom reserveroom = new ReservationRoom();
//		reserveroom.setRoomtype(new RoomType(1, "Single"));
//		reserveroom.setRoom_count((short) 1);
//		reserveroomlist.add(reserveroom);
		
//		reserveroom = new ReservationRoom();
//		reserveroom.setRoomtype(new RoomType(1, "Single"));
//		reserveroom.setRoom_count((short) 1);
//		reserveroomlist.add(reserveroom);
		
		reserveroom = new ReservationRoom();
		reserveroom.setRoomtype(new RoomType(2, "Double"));
		reserveroom.setRoom_count((short) 1);
		reserveroomlist.add(reserveroom);
		list = new ArrayList<ChildAge>();
		list.add(new ChildAge((short) 1, (short) 11)); 
		list.add(new ChildAge((short) 2, (short) 12)); 
		reserveroom.setChildAgeList(list);
		reserve.getChildAgeList().addAll(list);
		reserve.setChildren((short) reserve.getChildAgeList().size());
		
//		reserveroom = new ReservationRoom();
//		reserveroom.setRoomtype(new RoomType(2, "Double"));
//		reserveroom.setRoom_count((short) 1);
//		reserveroomlist.add(reserveroom);
//		list = new ArrayList<ChildAge>();
//		list.add(new ChildAge((short) 1, (short) 2)); 
//		list.add(new ChildAge((short) 3, (short) 11)); 
//		list.add(new ChildAge((short) 4, (short) 11));
//		reserveroom.setChildAgeList(list);
		
		reserve.getReserverooms().addAll(reserveroomlist);
		return reserve;
	}
	
	public static GroupReservation getGroupReserve(){
		List<ChildAge> list = new ArrayList<ChildAge>();
		GroupReservation reserve = new GroupReservation();
		reserve.setCheck_in(new Date());
		reserve.setTotal(new BigDecimal("0"));
		reserve.setRateplane(new RatePlane(2));
		
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, 2);
		reserve.setCheck_out(calendar.getTime());
		
		reserve.setHotelsusers_id(40);
		reserve.setAdults((short) 4);
		List<ReservationRoom> reserveroomlist = new ArrayList<ReservationRoom>();
		ReservationRoom reserveroom = new ReservationRoom();
//		reserveroom.setRoomtype(new RoomType(1, "Single"));
//		reserveroom.setRoom_count((short) 1);
//		reserveroomlist.add(reserveroom);
		
//		reserveroom = new ReservationRoom();
//		reserveroom.setRoomtype(new RoomType(1, "Single"));
//		reserveroom.setRoom_count((short) 1);
//		reserveroomlist.add(reserveroom);
		
		reserveroom = new ReservationRoom();
		reserveroom.setRoomtype(new RoomType(2, "Double"));
		reserveroom.setRoom_count((short) 1);
		reserveroomlist.add(reserveroom);
		list = new ArrayList<ChildAge>();
		list.add(new ChildAge((short) 1, (short) 11)); 
		list.add(new ChildAge((short) 2, (short) 12)); 
		reserveroom.setChildAgeList(list);
		reserve.getChildAgeList().addAll(list);
		reserve.setChildren((short) reserve.getChildAgeList().size());
		
//		reserveroom = new ReservationRoom();
//		reserveroom.setRoomtype(new RoomType(2, "Double"));
//		reserveroom.setRoom_count((short) 1);
//		reserveroomlist.add(reserveroom);
//		list = new ArrayList<ChildAge>();
//		list.add(new ChildAge((short) 1, (short) 2)); 
//		list.add(new ChildAge((short) 3, (short) 11)); 
//		list.add(new ChildAge((short) 4, (short) 11));
//		reserveroom.setChildAgeList(list);
		
		reserve.getReserverooms().addAll(reserveroomlist);
		return reserve;
	}

	public static GroupReservation getGroupReserve2(){
		GroupReservation reserve = new GroupReservation();
		reserve.setCheck_in(new Date());
		reserve.setTotal(new BigDecimal("0"));
		reserve.setRateplane(new RatePlane(2));
		reserve.setIs_group(ReservationDetail.GROUP);
		
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, 2);
		reserve.setCheck_out(calendar.getTime());
		
		reserve.setHotelsusers_id(3);
		reserve.setAdults((short) 4);
		List<ReservationRoom> reserveroomlist = new ArrayList<ReservationRoom>();
		ReservationRoom reserveroom = new ReservationRoom();
		reserveroom.setRoomtype(new RoomType(1, "Single"));
		reserveroom.setRoom_count((short) 1);
		reserveroomlist.add(reserveroom);
		
		reserveroom = new ReservationRoom();
		reserveroom.setRoomtype(new RoomType(1, "Single"));
		reserveroom.setRoom_count((short) 1);
		reserveroomlist.add(reserveroom);
		
		reserveroom = new ReservationRoom();
		reserveroom.setRoomtype(new RoomType(2, "Double"));
		reserveroom.setRoom_count((short) 1);
		reserveroomlist.add(reserveroom);
		
		reserve.getReserverooms().addAll(reserveroomlist);
		return reserve;
	}
	
}
