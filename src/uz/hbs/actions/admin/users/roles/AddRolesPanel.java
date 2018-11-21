package uz.hbs.actions.admin.users.roles;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.beans.Role;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;

public class AddRolesPanel extends MyBreadCrumbPanel {
	private static final long serialVersionUID = 1L;

	public AddRolesPanel(String id, IBreadCrumbModel breadCrumbModel) {
		super(id, breadCrumbModel);
		add(new MyForm("form"));
	}

	private class MyForm extends Form<Role> {
		private static final long serialVersionUID = 1L;
		private MyFeedbackPanel feedback;

		public MyForm(String id) {
			super(id, new CompoundPropertyModel<Role>(new Role()));

			add(feedback = new MyFeedbackPanel("feedback"));
			feedback.setOutputMarkupId(true);

			RequiredTextField<String> name;
			add(name = new RequiredTextField<String>("name"));
			name.setLabel(new StringResourceModel("roles.name", null));

			IndicatingAjaxButton submit = new IndicatingAjaxButton("submit") {
				private static final long serialVersionUID = 1L;

				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
					super.onError(target, form);
					target.add(feedback);
				}

				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					Role role = (Role) form.getDefaultModelObject();
					try {
						new MyBatisHelper().insert("insertRoles", role);
						success(getString("roles.add.success"));
					} catch (Exception e) {
						logger.error("Exception", e);
						error(getString("roles.add.fail"));
					} finally {
						target.add(feedback);
					}
				}
			};
			add(submit);
		}
	}

	@Override
	public IModel<String> getTitle() {
		return new StringResourceModel("roles.add", null);
	}

	@Override
	public Class<?> implementedClass() {
		return this.getClass();
	}
}