package uz.hbs.actions.admin.users.roles;

import org.apache.ibatis.session.SqlSession;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.StringResourceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uz.hbs.beans.Role;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;

public class DeleteRolesPanel extends Panel {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(DeleteRolesPanel.class);

	public DeleteRolesPanel(String id, final Role role, final ModalWindow dialog, final MyFeedbackPanel feedback,
			final WebMarkupContainer container) {
		super(id);

		feedback.setEscapeModelStrings(false);

		Form<Void> form = new Form<Void>("form");
		add(form);

		AjaxButton yes = new AjaxButton("yes") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				super.onSubmit(target, form);

				SqlSession sql = MyBatisHelper.getSqlSessionFactory().openSession(false);
				try {
					sql.delete("deleteRoleActions", role.getId());
					sql.delete("deleteRoles", role.getId());
					sql.commit();
					feedback.success(
							new StringResourceModel("roles.delete.success", null, new Object[] { role.getName() }).getString());
				} catch (Exception e) {
					sql.rollback();
					feedback.error(
							new StringResourceModel("roles.delete.unsuccess", null, new Object[] { role.getName() }).getString());
					logger.error("Exception", e);
				} finally {
					sql.close();
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
