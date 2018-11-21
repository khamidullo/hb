package uz.hbs.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uz.hbs.beans.AuditLog;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.session.MySession;

public class AuditLogsUtil {
	private static Logger logger = LoggerFactory.getLogger(AuditLogsUtil.class);

	public static void log(String actions_id, MySession session) {
		try {
			AuditLog logs = new AuditLog();
			logs.setActions_id(actions_id);
			logs.setInitiator_user_id(session.getUser().getId());
			logs.setLog_level(AuditLog.LEVEL_DEBUG);
			logs.setUsersessions_id(session.getUsersessions_id());
			
			insertLog(logs);
		} catch (Exception e) {
			logger.error("Exception", e);
		}
	}

	private static void insertLog(AuditLog logs) throws Exception {
		new MyBatisHelper().insert("insertAuditLogs", logs);
	}
}
