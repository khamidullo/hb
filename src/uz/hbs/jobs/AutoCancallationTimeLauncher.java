package uz.hbs.jobs;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SchedulerMetaData;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AutoCancallationTimeLauncher {
	private static final Logger _log = LoggerFactory.getLogger(AutoCancallationTimeLauncher.class);
	private Scheduler scheduler;
	
	public AutoCancallationTimeLauncher() {
	}
	
	public void start(){
		SchedulerFactory schedulerFactory = new StdSchedulerFactory();
		try {
			scheduler = schedulerFactory.getScheduler();
			JobDetail job = JobBuilder.newJob(AutoCancellationTimeJob.class).withIdentity("AutoCancellationTimeJob").build();
			CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity("MyJob").withSchedule(CronScheduleBuilder.cronSchedule("0 0/1 * * * ?")).build();
			Date ft = scheduler.scheduleJob(job, trigger);
            _log.info(job.getKey() + " has been scheduled to run at: " + new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(ft)
                            + " and repeat based on expression: \"" + trigger.getCronExpression() + "\"");
            
            scheduler.start();
		} catch (SchedulerException e) {
			_log.error("SchedulerException", e);
		}
	}
	
	public int stop(int exitCode) {
        try {
            _log.info("shutting Down Scheduler");
            scheduler.shutdown(true);
            _log.info("shutting Down Scheduler Complete");
            SchedulerMetaData metaData = scheduler.getMetaData();
            _log.info("NumberOfJobsExecuted " + metaData.getNumberOfJobsExecuted() + " jobs.");
        } catch (Exception e) {
            _log.error("Exception", e);
        } 
        _log.info("*** EXIT ***");
        return exitCode;
	}
}
