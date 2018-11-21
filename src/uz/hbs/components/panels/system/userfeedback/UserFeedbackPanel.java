package uz.hbs.components.panels.system.userfeedback;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.attributes.IAjaxCallListener;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uz.hbs.MyWebApplication;
import uz.hbs.beans.Message;
import uz.hbs.beans.SystemFeedback;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;
import uz.hbs.session.MySession;
import uz.hbs.utils.CommonUtil;
import uz.hbs.utils.DateUtil;
import uz.hbs.utils.RequiredFieldAjaxCallListener;
import uz.hbs.utils.email.EmailUtil;

public class UserFeedbackPanel extends Panel {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(UserFeedbackPanel.class);

	public UserFeedbackPanel(String id) {
		super(id);

		final MyFeedbackPanel feedback = new MyFeedbackPanel("feedback");
		feedback.setEscapeModelStrings(false);
		add(feedback);
		feedback.setOutputMarkupId(true);
		final Form<SystemFeedback> form = new Form<SystemFeedback>("form", new CompoundPropertyModel<SystemFeedback>(new SystemFeedback()));
		add(form);

		Label title = new Label("title", new StringResourceModel("system.feedback.window_title", null));
		form.add(title);

		RequiredTextField<String> subject = new RequiredTextField<String>("subject");
		subject.setLabel(new StringResourceModel("system.feedback.subject", null));
		form.add(subject);

		TextArea<String> comment = new TextArea<String>("comment");
		comment.setRequired(true);
		form.add(comment);

		AjaxButton submit = new AjaxButton("submit") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				super.onError(target, form);
				target.add(feedback);
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				super.onSubmit(target, form);
				final SystemFeedback model = (SystemFeedback) form.getModelObject();
				model.setCreate_user_id(((MySession) getSession()).getUser().getId());
				model.setInitiator_user_id(((MySession) getSession()).getUser().getId());
				model.setPage_info("");
				new MyBatisHelper().insert("insertSystemFeedback", model);

				logger.debug("User Feedback saved, Subject=" + model.getSubject());

				feedback.success(new StringResourceModel("system.feedback.message", null).getString());
				target.add(feedback);
				target.appendJavaScript(" closeModalWin('" + getMarkupId() + "');");

				new Thread() {
					public void run() {
						SystemFeedback systemFeedback = new MyBatisHelper().selectOne("selectSystemFeedbackById", model.getId());
						Message msg = new Message();
						msg.setId((long) 0);
						msg.setRecipient("info@hotelios.uz");
						msg.setSubject("Отзыв №: " + systemFeedback.getId() + " - " + systemFeedback.getSubject());
						msg.setContent("Дата: " + DateUtil.toString(systemFeedback.getCreate_date(), MyWebApplication.DATE_TIME_FORMAT)
								+ "\nПользователь: " + systemFeedback.getLogin() + ", " + systemFeedback.getUser_name() + "\nЗаголовка: "
								+ systemFeedback.getSubject() + "\nКомментария: " + systemFeedback.getComment());
						EmailUtil.sendSimpleTextEmail(msg);
					};
				}.start();
			}

			@Override
			protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
				super.updateAjaxAttributes(attributes);
				attributes.getAjaxCallListeners().add(
						new RequiredFieldAjaxCallListener(form.getMarkupId(), ((MySession) getSession()).getLocale().getLanguage()));

				IAjaxCallListener listener = new AjaxCallListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public CharSequence getSuccessHandler(Component component) {
						return "return closeModalWin();";
					}
				};
				attributes.getAjaxCallListeners().add(listener);
			}
		};
		form.add(submit);

		CommonUtil.setFormComponentRequired(form);
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		response.render(JavaScriptHeaderItem.forScript(
				"function closeModalWin(){$('.modal').modal('hide');$('#userfeedbacksubject').val('');$('#userfeedbackcomment').val('');}", null));
		super.renderHead(response);
	}
}
