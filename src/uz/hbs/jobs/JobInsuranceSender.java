package uz.hbs.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uz.hbs.utils.email.InsuranceEmailNotifier;

public class JobInsuranceSender implements Job {
	private static final Logger logger = LoggerFactory.getLogger(JobInsuranceSender.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// This job simply prints out its job name and the date and time that it is running
		JobKey jobKey = context.getJobDetail().getKey();
		logger.info("Executing: " + jobKey);
		
		InsuranceEmailNotifier.sendAttachedFileWithGuestList();
	}
}
