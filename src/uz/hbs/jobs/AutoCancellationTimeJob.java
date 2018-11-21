package uz.hbs.jobs;

import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import uz.hbs.db.MyBatisHelper;
import uz.hbs.utils.BundleUtil;
import uz.hbs.utils.HotelUtil;

public class AutoCancellationTimeJob implements Job {
	
	public AutoCancellationTimeJob() {
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		List<Long> list = new MyBatisHelper().selectList("selectReservetionsIdToCancel");
		Long user_id = Long.parseLong(new BundleUtil().configValue("system_user_id"));
		for (Long id : list){
			HotelUtil.cancelReserve(id, user_id);
		}
	}

}
