package uz.hbs.actions.admin.spectators;

import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.beans.Spectator;
import uz.hbs.beans.User;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;

public class ViewSpectatorsPanel extends MyBreadCrumbPanel {
	private static final long serialVersionUID = 1L;

	public ViewSpectatorsPanel(String id, IBreadCrumbModel breadCrumbModel, Spectator model) {
		super(id, breadCrumbModel);

		Spectator spectators = new MyBatisHelper().selectOne("selectSpectators", model);

		Label panelTitle = new Label("panelTitle", new StringResourceModel("spectators.view.panel.title", new Model<Spectator>(model)));
		add(panelTitle);

		add(new MyForm("form", new CompoundPropertyModel<Spectator>(spectators)));
	}

	private class MyForm extends Form<Spectator> {
		private static final long serialVersionUID = 1L;

		public MyForm(String id, IModel<Spectator> model) {
			super(id, model);

			final MyFeedbackPanel feedback = new MyFeedbackPanel("feedback");
			feedback.setOutputMarkupId(true);
			add(feedback);

			add(new Label("status", model.getObject().getStatus() == User.STATUS_ACTIVE ? new StringResourceModel("users.status1", null)
					: new StringResourceModel("users.status2", null)));

			Label name = new Label("name");
			add(name);

			Label primary_phone = new Label("primary_phone");
			add(primary_phone);

			Label email = new Label("email");
			add(email);

			Label first_name = new Label("first_name");
			add(first_name);

			Label middle_name = new Label("middle_name");
			add(middle_name);

			Label last_name = new Label("last_name");
			add(last_name);

			Label contact_number = new Label("contact_number");
			add(contact_number);

			Label contact_email = new Label("contact_email");
			add(contact_email);
		}
	}

	@Override
	public Class<?> implementedClass() {
		return this.getClass();
	}

	@Override
	public IModel<String> getTitle() {
		return new StringResourceModel("spectators.view", null);
	}
}
