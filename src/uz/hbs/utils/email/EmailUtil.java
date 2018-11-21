package uz.hbs.utils.email;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ResourceBundle;

import javax.activation.DataSource;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;
import org.apache.wicket.util.io.IClusterable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uz.hbs.MyWebApplication;
import uz.hbs.beans.Message;
import uz.hbs.db.MyBatisHelper;

public class EmailUtil {
	private static final Logger logger = LoggerFactory.getLogger(EmailUtil.class);

	public static boolean sendSimpleTextEmail(Long messageId) {
		Message msg = new MyBatisHelper().selectOne("selectMessages", messageId);
		if (msg != null) {
			return sendSimpleTextEmail(msg);
		} else {
			logger.error("SimpleText, Message not found: Id=" + messageId);
			return false;
		}
	}

	public static boolean sendSimpleTextEmail(Message msg) {
		try {
			if (msg != null) {
				msg.setStatus(Message.STATUS_INPROGRESS);
				new MyBatisHelper().update("updateMessages", msg);
				logger.debug("Message taked for process, Id=" + msg.getId());

				// Email server parameters
				EmailConf conf = new EmailUtil().getConfig();
				// Create the email message
				Email email = new SimpleEmail();
				email.setHostName(conf.getHostName());
				email.setSmtpPort(conf.getSmtpPort());
				email.setAuthenticator(new DefaultAuthenticator(conf.getUsername(), conf.getPassword()));
				email.setSSLOnConnect(conf.isSslOnConnect());
				email.setFrom(conf.getFrom());
				email.setSubject(msg.getSubject());
				email.setMsg(msg.getContent());
				email.addTo(msg.getRecipient());
				if (msg.getRecipient_bcc() != null) {
					email.addBcc(msg.getRecipient_bcc());
				}
				if (msg.getRecipient_cc() != null) {
					email.addCc(msg.getRecipient_cc());
				}
				email.send();
				
				msg.setStatus(Message.STATUS_SUCCESS);
				new MyBatisHelper().update("updateMessages", msg);
				
				logger.info("SimpleText, Message sent: Id=" + msg.getId() + ", To=" + msg.getRecipient() + ", Subjest=" + msg.getSubject());
				
				return true;
			} else {
				logger.error("SimpleText, Empty Message");
				return false;
			}
		} catch (Exception e) {
			if (msg != null) {
				msg.setStatus(Message.STATUS_ERROR);
				new MyBatisHelper().update("updateMessages", msg);
			}
			logger.error("SimpleText, Message was not sent: Id=" + msg.getId(), e);
			return false;
		}
	}

	public static boolean sendHtmlEmail(Long messageId) {
		Message msg = new MyBatisHelper().selectOne("selectMessages", messageId);

		if (msg != null) {
			return sendHtmlEmail(msg);
		} else {
			logger.error("Html, Message not found: Id=" + messageId);
			return false;
		}
	}

	public static boolean sendHtmlEmail(final Message msg) {
		try {
			if (msg != null) {
				msg.setStatus(Message.STATUS_INPROGRESS);
				if (msg.getId() != null) {
					new MyBatisHelper().update("updateMessages", msg);
				}
				logger.debug("Message taked for process, Id=" + msg.getId());

			
				// Email server parameters
				EmailConf conf = new EmailUtil().getConfig();
				DataSource attachment = new DataSource() {
					@Override
					public OutputStream getOutputStream() throws IOException {
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						baos.write(msg.getAttachment());
						return baos;
					}
					
					@Override
					public String getName() {
						return "logoimg";
					}
					
					@Override
					public InputStream getInputStream() throws IOException {
						return new ByteArrayInputStream(msg.getAttachment());
					}
					
					@Override
					public String getContentType() {
						return "image/png";
					}
				};
				// Create the email message
				HtmlEmail email = new HtmlEmail();
				//email.attach(attachment, "logoimg", "Logo image", EmailAttachment.INLINE);
				email.setHostName(conf.getHostName());
				email.setSmtpPort(conf.getSmtpPort());
				email.setAuthenticator(new DefaultAuthenticator(conf.getUsername(), conf.getPassword()));
				email.setSSLOnConnect(conf.isSslOnConnect());
				email.setFrom(conf.getFrom());
				email.addTo(msg.getRecipient());
				if (msg.getRecipient_bcc() != null) {
					email.addBcc(msg.getRecipient_bcc());
				}
				if (msg.getRecipient_cc() != null) {
					email.addCc(msg.getRecipient_cc());
				}
				email.setSubject(msg.getSubject());
				email.setHtmlMsg(msg.getContent().replaceAll("cid:logoimg", "cid:" + email.embed(attachment, "logoimg")));
				email.setCharset(conf.getCharset());
				//email.setBoolHasAttachments(true);
				email.send();
				
				msg.setStatus(Message.STATUS_SUCCESS);
				if (msg.getId() != null) {
					new MyBatisHelper().update("updateMessages", msg);
				}
				logger.info("Html, Message sent: Id=" + msg.getId() + ", To=" + msg.getRecipient() + ", Subjest=" + msg.getSubject());

				return true;
			} else {
				logger.error("Html, Empty Message");
				return false;
			}
		} catch (Exception e) {
			if (msg != null) {
				msg.setStatus(Message.STATUS_ERROR);
				if (msg.getId() != null) {
					new MyBatisHelper().update("updateMessages", msg);
				}
			}
			logger.error("Html, Message was not sent: Id=" + msg.getId(), e);
			return false;
		}
	}

	public static boolean sendEmailWithAttachment(Long messageId) {
		Message msg = new MyBatisHelper().selectOne("selectMessages", messageId);
		if (msg != null) {
			return sendEmailWithAttachment(msg);
		} else {
			logger.error("Html, Empty Message");
			return false;
		}
	}

	public static boolean sendEmailWithAttachment(final Message msg) {
		try {
			if (msg != null) {
				msg.setStatus(Message.STATUS_INPROGRESS);
				new MyBatisHelper().update("updateMessages", msg);
				logger.debug("Message taked for process, Id=" + msg.getId());
				// Create the attachment
				DataSource attachment = new DataSource() {

					@Override
					public OutputStream getOutputStream() throws IOException {
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						baos.write(msg.getAttachment());
						return baos;
					}

					@Override
					public String getName() {
						return msg.getAttachment_name();
					}

					@Override
					public InputStream getInputStream() throws IOException {
						return new ByteArrayInputStream(msg.getAttachment());
					}

					@Override
					public String getContentType() {
						return "application/octet-stream";
					}
				};

				// Email server parameters
				EmailConf conf = new EmailUtil().getConfig();
				// Create the email message
				MultiPartEmail email = new MultiPartEmail();
				email.setHostName(conf.getHostName());
				email.setSmtpPort(conf.getSmtpPort());
				email.setAuthenticator(new DefaultAuthenticator(conf.getUsername(), conf.getPassword()));
				email.setSSLOnConnect(conf.isSslOnConnect());
				email.setFrom(conf.getFrom());
				email.addTo(msg.getRecipient());
				if (msg.getRecipient_bcc() != null) {
					email.addBcc(msg.getRecipient_bcc());
				}
				if (msg.getRecipient_cc() != null) {
					email.addCc(msg.getRecipient_cc());
				}
				email.setSubject(msg.getSubject());
				email.setCharset(conf.getCharset());
				email.setMsg(msg.getContent());
				if (msg.getAttachment() != null) {
					email.attach(attachment, msg.getAttachment_name(), null, EmailAttachment.ATTACHMENT);
				}
				email.send();
				
				msg.setStatus(Message.STATUS_SUCCESS);
				new MyBatisHelper().update("updateMessages", msg);

				logger.info("Attachment, Message sent: Id=" + msg.getId() + ", To=" + msg.getRecipient() + ", Subjest=" + msg.getSubject());

				return true;
			} else {
				logger.error("Attachment, Empty Message");
				return false;
			}
		} catch (Exception e) {
			if (msg != null) {
				msg.setStatus(Message.STATUS_ERROR);
				new MyBatisHelper().update("updateMessages", msg);
			}
			logger.error("Attachment, Error, Message was not sent: Id=" + msg.getId(), e);
			return false;
		}
	}

	private EmailConf getConfig() {
		ResourceBundle configBundle = MyWebApplication.getConfigBundle();
		EmailConf conf = new EmailConf();
		conf.setFrom(configBundle.getString("from"));
		conf.setHostName(configBundle.getString("hostName"));
		conf.setSmtpPort(Integer.parseInt(configBundle.getString("smtpPort")));
		conf.setUsername(configBundle.getString("username"));
		conf.setPassword(configBundle.getString("password"));
		conf.setSslOnConnect(Boolean.parseBoolean(configBundle.getString("sslOnConnect")));
		conf.setCharset(configBundle.getString("charset"));
		return conf;
	}
}

class EmailConf implements IClusterable {
	private static final long serialVersionUID = 1L;
	private String from;
	private String hostName;
	private int smtpPort;
	private String username;
	private String password;
	private String charset;
	private boolean sslOnConnect;

	public EmailConf() {
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public int getSmtpPort() {
		return smtpPort;
	}

	public void setSmtpPort(int smtpPort) {
		this.smtpPort = smtpPort;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public boolean isSslOnConnect() {
		return sslOnConnect;
	}

	public void setSslOnConnect(boolean sslOnConnect) {
		this.sslOnConnect = sslOnConnect;
	}
}
