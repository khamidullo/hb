package uz.hbs.actions.settings;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.actions.admin.users.UserChangePasswordPanel;
import uz.hbs.beans.User;
import uz.hbs.beans.UserSettings;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;

public class SettingsPanel extends MyBreadCrumbPanel {
	private static final long serialVersionUID = 1L;

	public SettingsPanel(String id, IBreadCrumbModel breadCrumbModel) {
		super(id, breadCrumbModel);

		add(new MyForm("form", new CompoundPropertyModel<UserSettings>(getMySession().getSettings())));
	}

	private class MyForm extends Form<UserSettings> {
		private static final long serialVersionUID = 1L;

		public MyForm(String id, IModel<UserSettings> formModel) {
			super(id, formModel);

			final MyFeedbackPanel feedback = new MyFeedbackPanel("feedback");
			feedback.setOutputMarkupId(true);
			add(feedback);

			NumberTextField<Integer> tableRows = new NumberTextField<Integer>("table_rows");
			tableRows.setRequired(true);
			tableRows.setMinimum(10);
			tableRows.setMaximum(200);
			tableRows.setLabel(new StringResourceModel("users.settings.table_rows", null));
			add(tableRows);

			IndicatingAjaxButton submit = new IndicatingAjaxButton("submit") {
				private static final long serialVersionUID = 1L;

				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
					super.onError(target, form);
					target.add(feedback);
				}

				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					super.onSubmit(target, form);
					UserSettings model = (UserSettings) form.getDefaultModelObject();
					model.setUsers_id(getMySession().getUser().getId());
					model.setTable_rows(tableRows.getModelObject().intValue());
					if (new MyBatisHelper().update("updateUserSettings", model) == 0) {
						new MyBatisHelper().insert("insertUserSettings", model);
					}
					logger.debug("User settings saved: " + model);
					feedback.success(new StringResourceModel("update.successfully", null).getString());
					target.add(feedback);
				}
			};
			add(submit);

			Link<Void> changePasswordLink = new Link<Void>("changePasswordLink") {
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick() {
					activate(new IBreadCrumbPanelFactory() {
						private static final long serialVersionUID = 1L;

						@Override
						public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
							return new UserChangePasswordPanel(componentId, breadCrumbModel, getMySession().getUser());
						}
					});
				}
			};
			changePasswordLink.setVisible(getMySession().getUser().getType().getId().intValue() != User.TYPE_API);
			add(changePasswordLink);
		}
	}

	@Override
	public Class<?> implementedClass() {
		return this.getClass();
	}

	@Override
	public IModel<String> getTitle() {
		return new StringResourceModel("settings", null);
	}
}
