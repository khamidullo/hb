package uz.hbs;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.io.File;
import java.util.Date;
import java.util.ResourceBundle;

import org.apache.wicket.Component;
import org.apache.wicket.IConverterLocator;
import org.apache.wicket.Page;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.Session;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authorization.IAuthorizationStrategy;
import org.apache.wicket.core.request.mapper.CryptoMapper;
import org.apache.wicket.markup.MarkupFactory;
import org.apache.wicket.markup.html.IPackageResourceGuard;
import org.apache.wicket.markup.html.SecurePackageResourceGuard;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.component.IRequestableComponent;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.util.file.Folder;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.util.time.Duration;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uz.hbs.errorpages.MyAccessDeniedPage;
import uz.hbs.errorpages.MyInternalErrorPage;
import uz.hbs.errorpages.MyPageExpiredErrorPage;
import uz.hbs.errorpages.MyPageNotFound;
import uz.hbs.jobs.JobCurrencyUpdater;
import uz.hbs.jobs.JobInsuranceSender;
import uz.hbs.jobs.JobNoShowUpdater;
import uz.hbs.security.AuthenticatedWebPage;
import uz.hbs.security.Logout;
import uz.hbs.security.MyAuthorization;
import uz.hbs.security.PasswordRestorePage;
import uz.hbs.security.SignIn;
import uz.hbs.session.MySession;
import uz.hbs.utils.ConverterLocatorUtil;
import uz.hbs.utils.DateUtil;

public class MyWebApplication extends WebApplication {
	private static final Logger logger = LoggerFactory.getLogger(MyWebApplication.class);
	private Folder reportFolder;
	private Folder uploadFolder;
	private static ResourceBundle configBundle = ResourceBundle.getBundle("Config");

	public static final String DATE_FORMAT = "dd/MM/yyyy";
	public static final String TIME_FORMAT = "HH:mm:ss";
	public static final String TIME_SHORT_FORMAT = "HH:mm";
	public static final String DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss";
	public static final String DATE_TIME_SHORT_FORMAT = "dd/MM/yyyy HH:mm";

	public static final boolean IS_REQUIRE = true;

	private Scheduler scheduler;

	@Override
	protected void init() {
		super.init();
		getApplicationSettings().setUploadProgressUpdatesEnabled(true);
		getApplicationSettings().setAccessDeniedPage(MyAccessDeniedPage.class);
		getApplicationSettings().setInternalErrorPage(MyInternalErrorPage.class);
		getApplicationSettings().setPageExpiredErrorPage(MyPageExpiredErrorPage.class);
		getApplicationSettings().setDefaultMaximumUploadSize(Bytes.megabytes(100));
		getDebugSettings().setAjaxDebugModeEnabled(getConfigurationType() == RuntimeConfigurationType.DEVELOPMENT);
		getDebugSettings().setDevelopmentUtilitiesEnabled(getConfigurationType() == RuntimeConfigurationType.DEVELOPMENT);
		getMarkupSettings().setAutomaticLinking(false);
		getMarkupSettings().setCompressWhitespace(true);
		getMarkupSettings().setDefaultMarkupEncoding("UTF-8");
		getMarkupSettings().setMarkupFactory(new MarkupFactory());
		getMarkupSettings().setStripComments(true);
		getMarkupSettings().setStripWicketTags(true);
		getMarkupSettings().setThrowExceptionOnMissingXmlDeclaration(false);
		getPageSettings().setRecreateMountedPagesAfterExpiry(true);
		getPageSettings().setVersionPagesByDefault(true);
		getRequestCycleSettings().setBufferResponse(true);
		getRequestCycleSettings().setGatherExtendedBrowserInfo(false);
		getRequestCycleSettings().setResponseRequestEncoding("UTF-8");
		getRequestCycleSettings().setTimeout(Duration.ONE_MINUTE);
		getRequestLoggerSettings().setRecordSessionSize(true);
		getRequestLoggerSettings().setRequestLoggerEnabled(false);
		// getRequestLoggerSettings().setRequestsWindowSize(0);
		getResourceSettings().setDefaultCacheDuration(WebResponse.MAX_CACHE_DURATION);
		getResourceSettings().setEncodeJSessionId(false);
		getResourceSettings().setThrowExceptionOnMissingResource(true);
		getResourceSettings().setUseDefaultOnMissingResource(true);
		getResourceSettings().setUseMinifiedResources(getConfigurationType() == RuntimeConfigurationType.DEPLOYMENT);
		if (getConfigurationType() == RuntimeConfigurationType.DEPLOYMENT) {
			setRootRequestMapper(new CryptoMapper(getRootRequestMapper(), this));
		}
		IPackageResourceGuard guard = getResourceSettings().getPackageResourceGuard();
		if (guard instanceof SecurePackageResourceGuard) {
			SecurePackageResourceGuard secureGuard = (SecurePackageResourceGuard) guard;
			secureGuard.addPattern("+*.js");
			secureGuard.addPattern("+*.css");
			secureGuard.addPattern("+*.gif");
			secureGuard.addPattern("+*.png");
			secureGuard.addPattern("+*.jpg");
			secureGuard.addPattern("+*.jpeg");
			secureGuard.addPattern("+*.map");
			secureGuard.addPattern("+*.otf");
			secureGuard.addPattern("+*.svg");
			secureGuard.addPattern("+*.woff");
			secureGuard.addPattern("+*.woff2");
			secureGuard.addPattern("+*.ttf");
		}
		// Register the authorization strategy
		getSecuritySettings().setAuthorizationStrategy(new IAuthorizationStrategy() {
			@Override
			public boolean isActionAuthorized(Component component, Action action) {
				// authorize
				return MyAuthorization.isActionAuthorized(component, action, ((MySession) Session.get()));
			}

			@Override
			public <T extends IRequestableComponent> boolean isInstantiationAuthorized(Class<T> componentClass) {
				// Check if the new Page requires authentication (implements the marker interface)
				if (AuthenticatedWebPage.class.isAssignableFrom(componentClass)) {
					// Is user signed in?
					if (((MySession) Session.get()).isSignedIn()) {
						// okay to proceed
						return true;
					}

					// Intercept the request, but remember the target for later.
					// Invoke Component.continueToOriginalDestination() after successful logon to
					// continue with the target remembered.
					throw new RestartResponseAtInterceptPageException(SignIn.class);
				}

				// okay to proceed
				return true;
			}
		});

		mountPage("/a", Home.class);
		mountPage("/signin", SignIn.class);
		mountPage("/logout", Logout.class);
		mountPage("/error404", MyPageNotFound.class);
		mountPage("/restore", PasswordRestorePage.class);

		reportFolder = new Folder(getServletContext().getRealPath("") + File.separator + "WEB-INF" + File.separator + "report");
		// Ensure folder exists
		reportFolder.mkdirs();

		uploadFolder = new Folder(getServletContext().getRealPath("") + File.separator + "upload");
		// Ensure folder exists
		uploadFolder.mkdirs();

		startScheduledJobs();

		logger.info("Server started");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			scheduler.shutdown(true);
			logger.info("The scheduler '" + scheduler.getSchedulerName() + "' is stopped");
		} catch (SchedulerException e) {
			logger.error("Exception", e);
		}

		logger.info("Server stopped");
	}

	@Override
	protected IConverterLocator newConverterLocator() {
		return new ConverterLocatorUtil();
	}

	@Override
	public Class<? extends Page> getHomePage() {
		return Home.class;
	}

	@Override
	public Session newSession(Request request, Response response) {
		return new MySession(request);
	}

	public Folder getReportFolder() {
		return reportFolder;
	}

	public Folder getUploadFolder() {
		return uploadFolder;
	}

	public static ResourceBundle getConfigBundle() {
		return configBundle;
	}

	private void startScheduledJobs() {
		try {
			// First we must get a reference to a scheduler
			SchedulerFactory schedulerFactory = new StdSchedulerFactory();
			scheduler = schedulerFactory.getScheduler();

			logger.info("Initialization of scheduler completed: ID=" + scheduler.getSchedulerInstanceId() + ", Name=" + scheduler.getSchedulerName());

			logger.info("Start to scheduling Jobs");

			/*********************** No show updater JOB *******************************/
			JobDetail job1 = newJob(JobNoShowUpdater.class).withIdentity("NoShowUpdater", "Group1").build();

			CronTrigger trigger1 = newTrigger().withIdentity("NoShowUpdaterTrigger", "Group1")
					.withSchedule(cronSchedule(configBundle.getString("cronJobNoShowUpdater"))).build();

			Date ft1 = scheduler.scheduleJob(job1, trigger1);

			logger.info(job1.getKey() + " has been scheduled to run at: " + DateUtil.toString(ft1, "dd.MM.yyyy HH:mm:ss")
					+ " and repeat based on expression: \"" + trigger1.getCronExpression() + "\"");
			/************************************************************************/

			/*********************** Currency updater JOB *******************************/
			JobDetail job2 = newJob(JobCurrencyUpdater.class).withIdentity("CurrencyUpdater", "Group1").build();

			CronTrigger trigger2 = newTrigger().withIdentity("CurrencyUpdaterTrigger", "Group1")
					.withSchedule(cronSchedule(usesDeploymentConfig() ? configBundle.getString("cronJobNoCurrencyUpdater") : "0 0 */2 * * ?")).build();

			Date ft2 = scheduler.scheduleJob(job2, trigger2);

			logger.info(job2.getKey() + " has been scheduled to run at: " + DateUtil.toString(ft2, "dd.MM.yyyy HH:mm:ss")
					+ " and repeat based on expression: \"" + trigger2.getCronExpression() + "\"");
			/************************************************************************/

			/*********************** Currency updater JOB *******************************/
			JobDetail job3 = newJob(JobInsuranceSender.class).withIdentity("InsuranceListSender", "Group1").build();
			
			CronTrigger trigger3 = newTrigger().withIdentity("InsuranceListSenderTrigger", "Group1")
					.withSchedule(cronSchedule(configBundle.getString("cronJobInsuranceListSender"))).build();
			
			Date ft3 = scheduler.scheduleJob(job3, trigger3);
			
			logger.info(job3.getKey() + " has been scheduled to run at: " + DateUtil.toString(ft3, "dd.MM.yyyy HH:mm:ss")
			+ " and repeat based on expression: \"" + trigger3.getCronExpression() + "\"");
			/************************************************************************/

			logger.info("All jobs scheduled");

			logger.info("Starting the scheduler '" + scheduler.getSchedulerName() + "'");

			scheduler.start();

			logger.info("The scheduler '" + scheduler.getSchedulerName() + "' started");
		} catch (SchedulerException e) {
			logger.error("Exception", e);
		}
	}
}
