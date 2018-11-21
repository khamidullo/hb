package uz.hbs.actions.admin.users.roles;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.model.util.CollectionModel;
import org.apache.wicket.model.util.ListModel;

import uz.hbs.beans.Action;
import uz.hbs.beans.Role;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;
import uz.hbs.markup.html.form.palette.BootstrapPalette;

public class EditRolesPanel extends MyBreadCrumbPanel {
	private static final long serialVersionUID = 1L;
	private Role role;

	public EditRolesPanel(String id, IBreadCrumbModel breadCrumbModel, Role role) {
		super(id, breadCrumbModel);
		this.role = role;
		add(new MyForm("form"));
	}

	private class MyForm extends Form<Role> {
		private static final long serialVersionUID = 1L;

		public MyForm(String id) {
			super(id, new CompoundPropertyModel<Role>(role));

			MyFeedbackPanel feedback = new MyFeedbackPanel("feedback");
			add(feedback);
			feedback.setOutputMarkupId(true);

			RequiredTextField<String> name;
			add(name = new RequiredTextField<String>("name"));
			name.setLabel(new StringResourceModel("roles.name", null));

			ChoiceRenderer<Action> renderer = new ChoiceRenderer<Action>("value", "id");

			List<Action> available = new MyBatisHelper().selectList("selectAvailableActions");
			List<Action> selected = new MyBatisHelper().selectList("selectSelectedActions", role.getId());

			if (selected != null && selected.size() > 0)
				role.getActionslist().addAll(selected);

			BootstrapPalette<Action> palette = new BootstrapPalette<Action>("palette", new ListModel<Action>(role.getActionslist()),
					new CollectionModel<Action>(available), renderer, 20, false, true);
			add(palette);

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

					SqlSession sql = MyBatisHelper.getSqlSessionFactory().openSession(false);

					try {
						sql.insert("updateRoles", role);

						List<Action> list = role.getActionslist();

						sql.delete("deleteRoleActions", role.getId());

						HashMap<String, Serializable> param = new HashMap<String, Serializable>();
						param.put("roles_id", role.getId());
						if (list != null && list.size() > 0) {
							for (Action action : list) {
								param.put("actions_id", action.getId());
								sql.insert("insertRoleActions", param);
							}
						}
						success(getString("roles.edit.success"));
						sql.commit();
					} catch (Exception e) {
						sql.rollback();
						logger.error("Exception", e);
						error(getString("roles.edit.fail"));
					} finally {
						sql.close();
						target.add(feedback);
					}
				}
			};
			add(submit);
		}
	}

	@Override
	public Class<?> implementedClass() {
		return this.getClass();
	}

	@Override
	public IModel<String> getTitle() {
		return new StringResourceModel("roles.edit", new Model<Role>(role));
	}

}
