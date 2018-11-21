package uz.hbs.jobs;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uz.hbs.beans.ReservationStatus;

public class JobNoShowUpdater implements Job {
	private static Logger logger = LoggerFactory.getLogger(JobNoShowUpdater.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// This job simply prints out its job name and the date and time that it is running
		JobKey jobKey = context.getJobDetail().getKey();
		logger.info("Executing: " + jobKey);
		
		Map<String, Serializable> params = new HashMap<String, Serializable>();
		params.put("reservation_status", ReservationStatus.RESERVED);
		params.put("check_in", new Date());

//		List<ReservationDetail> list = new MyBatisHelper().selectList("selectReservationList", params);
//		for (ReservationDetail reservation : list) {
//			reservation.setStatus(new ReservationStatus(ReservationStatus.CHECKED_IN));
//			reservation.setInitiator_user_id(User.USER_SYSTEM);
//			new MyBatisHelper().update("updateReservationStatus", reservation);
//		}
	}
}
