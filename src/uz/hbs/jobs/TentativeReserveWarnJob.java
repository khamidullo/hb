package uz.hbs.jobs;

import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import uz.hbs.beans.ReservationDetail;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.utils.BundleUtil;
import uz.hbs.utils.MessagesUtil;

public class TentativeReserveWarnJob implements Job {
	private long initiator_user_id;
	
	public TentativeReserveWarnJob() {
		initiator_user_id = Long.parseLong(new BundleUtil().configValue("system.user.id"));
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		List<ReservationDetail> list = new MyBatisHelper().selectList("selectTentativeReserveListWarn");
		for (ReservationDetail reserve : list){
			if (MessagesUtil.createTextMessage("recipient", "subject", "content", initiator_user_id, true)){
				new MyBatisHelper().update("updateTentativeWarn", reserve.getId());
			}
		}
	}

}
