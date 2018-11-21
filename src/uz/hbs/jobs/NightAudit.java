package uz.hbs.jobs;

import java.util.List;

import uz.hbs.beans.Hotel;
import uz.hbs.db.MyBatisHelper;

public class NightAudit implements Runnable {

	@Override
	public void run() {
		List<Hotel> hotellist = new MyBatisHelper().selectList("selectNightAuditHotelList");
		for (Hotel hotel : hotellist){
			new Thread(new NightAuditProcess(hotel)).start();
		}
	}
}
