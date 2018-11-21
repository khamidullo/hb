package uz.hbs.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uz.hbs.beans.Message;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.utils.email.EmailUtil;

public class MessagesUtil {
	private static Logger logger = LoggerFactory.getLogger(MessagesUtil.class);

	public static Message createMessage(String recipient, String subject, String content, String attachmentName, byte[] attachment, Long initiator_user_id) {
		Message msg = new Message();
		msg.setRecipient(recipient);
		msg.setSubject(subject);
		msg.setContent(content);
		msg.setAttachment_name(attachmentName);
		msg.setAttachment(attachment);
		msg.setInitiator_user_id(initiator_user_id);

		new MyBatisHelper().insert("insertMessages", msg);
		logger.debug("Message created: " + msg);
		return msg;
	}

	public static boolean createTextMessage(String recipient, String subject, String content, Long initiator_user_id, boolean sendEmail) {
		try {
			Message msg = createMessage(recipient, subject, content, null, null, initiator_user_id);
			if (sendEmail) {
				EmailUtil.sendSimpleTextEmail(msg);
			}
			return true;
		} catch (Exception e) {
			logger.error("Exception", e);
			return false;
		}
	}
	
	public static boolean createHtmlMessage(String recipient, String subject, String content, Long initiator_user_id, boolean sendEmail) {
		try {
			Message msg = createMessage(recipient, subject, content, null, null, initiator_user_id);
			if (sendEmail) {
				EmailUtil.sendHtmlEmail(msg);
			}
			return true;
		} catch (Exception e) {
			logger.error("Exception", e);
			return false;
		}
	}

	public static boolean createMessageWithAttachment(String recipient, String subject, String content, String attachmentName, byte[] attachment, Long initiator_user_id, boolean sendEmail) {
		try {
			Message msg = createMessage(recipient, subject, content, attachmentName, attachment, initiator_user_id);
			if (sendEmail) {
				EmailUtil.sendEmailWithAttachment(msg);
			}
			return true;
		} catch (Exception e) {
			logger.error("Exception", e);
			return false;
		}
	}
}
