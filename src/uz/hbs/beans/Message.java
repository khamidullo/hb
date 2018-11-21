package uz.hbs.beans;

import java.util.Date;

import org.apache.wicket.util.io.IClusterable;

public class Message implements IClusterable {
	private static final long serialVersionUID = 1L;
	
	public static final byte STATUS_NEW = 0;
	public static final byte STATUS_INPROGRESS = 1;
	public static final byte STATUS_SUCCESS = 2;
	public static final byte STATUS_ERROR = 3;
	
	private Long id;
	private String recipient;
	private String recipient_bcc[];
	private String recipient_cc[];
	private String subject;
	private String content;
	private byte[] attachment;
	private String attachment_name;
	private Byte status;
	private Long initiator_user_id;
	private Date create_date;
	private Date update_date;

	public Message() {
	}

	@Override
	public String toString() {
		return "{id=" + id + ", subject=" + subject + ", content=" + content + ", attachment_name=" + attachment_name + ", attachment="
				+ (attachment != null ? attachment.length : 0) + ", status=" + status + "}";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public byte[] getAttachment() {
		return attachment;
	}

	public void setAttachment(byte[] attachment) {
		this.attachment = attachment;
	}

	public String getAttachment_name() {
		return attachment_name;
	}

	public void setAttachment_name(String attachment_name) {
		this.attachment_name = attachment_name;
	}

	public Long getInitiator_user_id() {
		return initiator_user_id;
	}

	public void setInitiator_user_id(Long initiator_user_id) {
		this.initiator_user_id = initiator_user_id;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public Date getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}

	public String[] getRecipient_bcc() {
		return recipient_bcc;
	}

	public void setRecipient_bcc(String recipient_bcc[]) {
		this.recipient_bcc = recipient_bcc;
	}

	public String[] getRecipient_cc() {
		return recipient_cc;
	}

	public void setRecipient_cc(String recipient_cc[]) {
		this.recipient_cc = recipient_cc;
	}
}
