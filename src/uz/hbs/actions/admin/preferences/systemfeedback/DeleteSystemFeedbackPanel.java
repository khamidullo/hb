package uz.hbs.actions.admin.preferences.systemfeedback;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.StringResourceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uz.hbs.beans.SystemFeedback;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;
import uz.hbs.session.MySession;

public class DeleteSystemFeedbackPanel extends Panel {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(DeleteSystemFeedbackPanel.class);
	
	public DeleteSystemFeedbackPanel(String id, final SystemFeedback model, final ModalWindow dialog, final MyFeedbackPanel feedback, final WebMarkupContainer container) {
		super(id);
		
		feedback.setEscapeModelStrings(false);
		
		Form<Void> form = new Form<Void>("form");
		add(form);

		form.add(new Label("title", new StringResourceModel("system.feedback.delete.confirmation", null, new Object[]{model.getSubject()})).setEscapeModelStrings(false));
		
		AjaxButton yes = new AjaxButton("yes") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				super.onSubmit(target, form);

				model.setStatus(SystemFeedback.STATUS_DELETED);
				model.setInitiator_user_id(((MySession) getSession()).getUser().getId());
				try {
					new MyBatisHelper().update("updateSystemFeedback", model);
					feedback.success(new StringResourceModel("system.feedback.delete.success", null, new Object[] { model.getLogin() }).getString());
				} catch (Exception e) {
					logger.error("Exception", e);
					feedback.error(new StringResourceModel("system.feedback.delete.error", null, new Object[]{model.getSubject()}).getString());
				}

				target.add(feedback);
				target.add(container);
				dialog.close(target);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				super.onError(target, form);
				target.add(feedback);
				dialog.close(target);
			}
		};
		form.add(yes);

		AjaxButton no = new AjaxButton("no") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				super.onSubmit(target, form);
				target.add(feedback);
				dialog.close(target);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				super.onError(target, form);
				target.add(feedback);
				dialog.close(target);
			}
		};
		form.add(no);
	}
}
