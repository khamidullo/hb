package uz.hbs.actions.admin.users;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.model.util.CollectionModel;
import org.apache.wicket.model.util.ListModel;

import uz.hbs.beans.Role;
import uz.hbs.beans.User;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;
import uz.hbs.markup.html.form.palette.BootstrapPalette;

public class RoleUserPanel extends MyBreadCrumbPanel {
	private static final long serialVersionUID = 1L;
	private User user;

	public RoleUserPanel(String id, IBreadCrumbModel breadCrumbModel, User user) {
		super(id, breadCrumbModel);
		this.user = user;
		add(new MyForm("form"));
	}
	
	private class MyForm extends Form<User>{
		private static final long serialVersionUID = 1L;
		private BootstrapPalette<Role> palette;
		private MyFeedbackPanel feedback;

		public MyForm(String id) {
			super(id, new CompoundPropertyModel<User>(user));
			
			add((feedback = new MyFeedbackPanel("feedback")).setOutputMarkupId(true));
			
			ChoiceRenderer<Role> renderer = new ChoiceRenderer<Role>("name", "id");
			
			List<Role> available = new MyBatisHelper().selectList("selectUserAvailableRoles");
			List<Role> selected  = new MyBatisHelper().selectList("selectUserSelectedRoles", user.getId());
			

			palette = new BootstrapPalette<Role>("palette", new ListModel<Role>(selected), 
					                             		     new CollectionModel<Role>(available), renderer, 20, true, true);
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
					SqlSession sql = MyBatisHelper.getSqlSessionFactory().openSession(false);
					try {
						Iterator<Role> it = palette.getSelectedChoices();
						
						sql.delete("deleteUsersRole", user.getId());
						
						HashMap<String, Serializable> param = new HashMap<String, Serializable>();
						param.put("users_id", user.getId());
						while (it.hasNext()){
							param.put("roles_id", it.next().getId());
							sql.insert("insertUserRoles", param);
						}
						success(getString("users.role.success"));
						sql.commit();
					} catch (Exception e) {
						sql.rollback();
						logger.error("Exception", e);
						error(getString("users.role.fail"));
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
		return new StringResourceModel("users.role.assign", new Model<User>(user));
	}

}
