package uz.hbs.actions.admin.users;

import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.beans.IdAndValue;
import uz.hbs.beans.User;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;

public class DeleteUserPanel extends Panel {
	private static final long serialVersionUID = 1L;

	public DeleteUserPanel(String id, final User user, final ModalWindow dialog, final MyFeedbackPanel feedback, final WebMarkupContainer container) {
		super(id);

		feedback.setEscapeModelStrings(false);

		Form<Void> form = new Form<Void>("form");
		add(form);

		AjaxButton yes = new AjaxButton("yes") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				super.onSubmit(target, form);

				boolean canBeDeleted = true;
				if (user.getType().getId() == User.TYPE_TOURAGENT_USER) {
					Map<String, Object> params = new HashMap<>();
					params.put("user_type", User.TYPE_TOURAGENT_USER);
					params.put("creator_user_id", user.getId());
					if ((Integer) new MyBatisHelper().selectOne("selectReservationsCountByTypeOrStatus", params) > 0)
						canBeDeleted = false;
					else
						canBeDeleted = true;
				}

				if (canBeDeleted) {
					user.setStatus(new IdAndValue((int) User.STATUS_DELETED));

					int updated = new MyBatisHelper().update("updateUserStatus", user);
					if (updated > 0) {
						feedback.success(
								new StringResourceModel("users.status.change.delete.success", null, new Object[] { user.getLogin() }).getString());
					} else {
						feedback.error(
								new StringResourceModel("users.status.change.delete.unsuccess", null, new Object[] { user.getLogin() }).getString());

					}
				} else {
					feedback.error(
							new StringResourceModel("users.status.change.delete.notallowed", null, new Object[] { user.getLogin() }).getString());
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
