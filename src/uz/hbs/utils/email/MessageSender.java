package uz.hbs.utils.email;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uz.hbs.MyWebApplication;

public class MessageSender {
	private static Logger _log = LoggerFactory.getLogger(MessageSender.class);
	private static String SMTP_AUTH_USER = null;
	private static String SMTP_AUTH_PWD = null;

//	public boolean sendMail(String email, String subject, String body, String img) {
//		return send(email, subject, body, img);
//	}
//
//	public boolean sendMailInBackground(final String email, final String subject, final String body, final String img, final boolean convertToText) {
//		Thread thread = new Thread() {
//			public void run() {
//				send(email, subject, body, img);
//			}
//		};
//		thread.start();
//
//		return true;
//	}
	
	public boolean send(uz.hbs.beans.Message msg) {
		return send(msg.getRecipient(), msg.getRecipient_bcc(), msg.getRecipient_cc(), msg.getSubject(), msg.getContent(), msg.getAttachment());
	}

	private boolean send(String email, String bcc[], String cc[], String subject, String body, final byte[] img) {
		boolean result = true;
		try {
			ResourceBundle configBundle = MyWebApplication.getConfigBundle();

			String from = configBundle.getString("from");
			String smtp = configBundle.getString("hostName");
			String port = configBundle.getString("smtpPort");
			SMTP_AUTH_USER = configBundle.getString("username");
			SMTP_AUTH_PWD = configBundle.getString("password");
			String debug = "false";

			Properties props = System.getProperties();

			props.put("mail.smtp.host", smtp);
			props.put("mail.smtp.protocol", "smtp");
			props.put("mail.debug", debug);
			props.put("mail.smtp.port", port);
			props.put("mail.smtp.allow8bitmime", "true");

			Authenticator auth = null;

			if (SMTP_AUTH_USER != null) {
				props.put("mail.smtp.auth", "true");
				auth = new SMTPAuthenticator();
			}
			Session session = Session.getInstance(props, auth);

			// Define message
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
			if (bcc != null) {
				InternetAddress addresses[] = new InternetAddress[bcc.length];
				for (int i = 0; i < bcc.length; i++) {
					addresses[i] = new InternetAddress(bcc[i]);
				}
				message.addRecipients(Message.RecipientType.BCC, addresses);
			}
			if (cc != null) {
				InternetAddress addresses[] = new InternetAddress[cc.length];
				for (int i = 0; i < cc.length; i++) {
					addresses[i] = new InternetAddress(cc[i]);
				}
				message.addRecipients(Message.RecipientType.CC, addresses);
			}
			message.setSubject(subject, configBundle.getString("charset"));

			//
			// This HTML mail have to 2 part, the BODY and the embedded image
			//
			MimeMultipart multipart = new MimeMultipart("related");

			// first part (the html)
			BodyPart messageBodyPart = new MimeBodyPart();
//			String htmlText = "<H1>Hello</H1><img src=\"cid:image\">";
			messageBodyPart.setContent(body, "text/html; charset=UTF-8");

			// add it
			multipart.addBodyPart(messageBodyPart);
			//File imgFile = new File("../bin/com/mss/img/" + img + "_state.png");
			// second part (the image)
			messageBodyPart = new MimeBodyPart();
			//DataSource fds = new FileDataSource(imgFile);
			
			DataSource fds = new DataSource() {
				@Override
				public OutputStream getOutputStream() throws IOException {
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					baos.write(img);
					return baos;
				}
				
				@Override
				public String getName() {
					return "logoimg";
				}
				
				@Override
				public InputStream getInputStream() throws IOException {
					return new ByteArrayInputStream(img);
				}
				
				@Override
				public String getContentType() {
					return "application/octet-stream";
				}
			};
			
			messageBodyPart.setDataHandler(new DataHandler(fds));
			messageBodyPart.setHeader("Content-ID", "<logoimg>");
			messageBodyPart.setHeader("Content-Type", "image/png; name=\"logoimg.png\"");
			messageBodyPart.setDisposition(MimeBodyPart.INLINE);
			// add it
			multipart.addBodyPart(messageBodyPart);

			// put everything together
			message.setContent(multipart);

			// Send message
			Transport.send(message);
			_log.info("Mail sent: " + subject);
		} catch (Exception e) {
			_log.error("Exception", e);
			result = false;
		}
		return result;
	}

	private class SMTPAuthenticator extends javax.mail.Authenticator {
		public PasswordAuthentication getPasswordAuthentication() {
			String username = SMTP_AUTH_USER;
			String password = SMTP_AUTH_PWD;
			return new PasswordAuthentication(username, password);
		}
	}

	// private boolean send_withSSL(String email, String subject, String body) {
	// boolean result = true;
	// try {
	// Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
	//
	// ResourceBundle bundle = ResourceBundle.getBundle("com/mss/utils/mail/MessageSender");
	// String from = bundle.getString("from-address");
	// String smtp = bundle.getString("smtp-host");
	// String port = bundle.getString("smtp-port");
	// SMTP_AUTH_USER = bundle.getString("smtp-user");
	// SMTP_AUTH_PWD = bundle.getString("smtp-pwd");
	// String debug = bundle.getString("debug");
	// Properties props = System.getProperties();
	// props.put("mail.smtp.host", smtp);
	// props.put("mail.smtp.protocol", "smtp");
	// props.put("mail.transport.protocol", "smtp");
	// props.put("mail.debug", debug);
	// props.put("mail.smtp.port", port);
	// // props.put("mail.smtp.starttls.enable", "true");
	// props.put("mail.smtp.socketFactory.port", port);
	// props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
	// props.put("mail.smtp.allow8bitmime", "true");
	//
	// Authenticator auth = null;
	//
	// if(SMTP_AUTH_USER != null) {
	// props.put("mail.smtp.auth", "true");
	// auth = new SMTPAuthenticator();
	// }
	// Session session = Session.getInstance(props, auth);
	//
	// // Define message
	// MimeMessage message = new MimeMessage(session);
	// message.setFrom(new InternetAddress(from));
	// message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
	// message.setSubject(subject, bundle.getString("subject-encoding"));
	// message.setText(body, bundle.getString("text-encoding"));
	// // Send message
	// Transport.send(message);
	// }
	// catch (Exception e) {
	// _log.error("Error sending mail: " + e);
	// result = false;
	// }
	// return result;
	// }
	//
	//
}

/*
 * http://www.oracle.com/technetwork/java/faq-135477.html#attach
 */