package uz.hbs.actions.admin.preferences.systemfeedback;

import java.util.List;

import org.apache.tools.ant.util.DateUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.MyWebApplication;
import uz.hbs.beans.SystemFeedback;
import uz.hbs.beans.User;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;
import uz.hbs.markup.html.panel.ActionsPanel;
import uz.hbs.markup.navigation.BootstrapPagingNavigator;
import uz.hbs.markup.navigation.BootstrapPagingNavigator.Size;
import uz.hbs.session.MySession;

public class SystemFeedBackListPanel extends Panel {

	private static final long serialVersionUID = 1L;

	public SystemFeedBackListPanel(String id) {
		super(id);
		
		final MyFeedbackPanel feedback = new MyFeedbackPanel("feedback");
		feedback.setOutputMarkupId(true);
		add(feedback);

		final ModalWindow dialog = new ModalWindow("dialog");
		dialog.setAutoSize(true);
		dialog.setMinimalHeight(100);
		dialog.setMinimalWidth(500);
		dialog.setInitialHeight(100);
		dialog.setInitialWidth(500);
		add(dialog);

		final WebMarkupContainer container = new WebMarkupContainer("container");
		container.setOutputMarkupId(true);
		add(container);
		
		LoadableDetachableModel<List<SystemFeedback>> systemFeedbacksList = new LoadableDetachableModel<List<SystemFeedback>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<SystemFeedback> load() {
				return new MyBatisHelper().selectList("selectSystemFeedback");
			}
		}; 
		
		final PageableListView<SystemFeedback> systemFeedbacks = new PageableListView<SystemFeedback>("systemFeedbacks", systemFeedbacksList, ((MySession)getSession()).getSettings().getTable_rows()) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<SystemFeedback> item) {
				item.add(new Label("id", item.getModelObject().getId()));
				item.add(new Label("login", item.getModelObject().getLogin()));
				item.add(new Label("subject", item.getModelObject().getSubject()));
				item.add(new Label("comment", item.getModelObject().getComment()));
				item.add(new Label("create_date", DateUtils.format(item.getModelObject().getCreate_date(), MyWebApplication.DATE_TIME_FORMAT)));
				item.add(new Label("status", new StringResourceModel("users.status" + item.getModelObject().getStatus(), null)));
				item.add(new ActionsPanel<SystemFeedback>("operations", item.getModel()) {
					private static final long serialVersionUID = 1L;

					@Override
					public AbstractLink addPrintLink(IModel<SystemFeedback> model) {
						return new AjaxLink<User>("print") {
							private static final long serialVersionUID = 1L;

							@Override
							public void onClick(AjaxRequestTarget target) {
							}

							@Override
							public boolean isVisible() {
								return false;
							}
						};
					}

					@Override
					public AbstractLink addEditLink(IModel<SystemFeedback> model) {
						return new AjaxLink<User>("edit") {
							private static final long serialVersionUID = 1L;

							@Override
							public void onClick(AjaxRequestTarget target) {
							}

							@Override
							public boolean isVisible() {
								return false;
							}
						};
					}

					@Override
					public AbstractLink addViewLink(IModel<SystemFeedback> model) {
						return new AjaxLink<User>("view") {
							private static final long serialVersionUID = 1L;

							@Override
							public void onClick(AjaxRequestTarget target) {
							}

							@Override
							public boolean isVisible() {
								return false;
							}
						};
					}

					@Override
					public AbstractLink addViewLogLink(IModel<SystemFeedback> model) {
						return new AjaxLink<User>("viewLog") {
							private static final long serialVersionUID = 1L;

							@Override
							public void onClick(AjaxRequestTarget target) {
							}

							@Override
							public boolean isVisible() {
								return false;
							}
						};
					}

					@Override
					public AbstractLink addDeleteLink(final IModel<SystemFeedback> model) {
						return new AjaxLink<User>("delete") {
							private static final long serialVersionUID = 1L;

							@Override
							public void onClick(AjaxRequestTarget target) {
								dialog.setTitle(new StringResourceModel("system.feedback.delete", null));
								dialog.setContent(new DeleteSystemFeedbackPanel(dialog.getContentId(), model.getObject(), dialog, feedback, container));
								dialog.show(target);
							}

							@Override
							public boolean isVisible() {
								return model.getObject().getStatus() == SystemFeedback.STATUS_NEW;
							}
						};
					}

					@Override
					public AbstractLink addCancelLink(IModel<SystemFeedback> model) {
						return new AjaxLink<User>("cancel") {
							private static final long serialVersionUID = 1L;

							@Override
							public void onClick(AjaxRequestTarget target) {
							}

							@Override
							public boolean isVisible() {
								return false;
							}
						};
					}

					@Override
					public AbstractLink addUserListLink(IModel<SystemFeedback> model) {
						return new AjaxLink<User>("userList") {
							private static final long serialVersionUID = 1L;

							@Override
							public void onClick(AjaxRequestTarget target) {
							}

							@Override
							public boolean isVisible() {
								return false;
							}
						};
					}
				});
			}
		}; 
		container.add(systemFeedbacks);
		
		container.add(new BootstrapPagingNavigator("systemFeedbacksNavigator", systemFeedbacks, Size.Small) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible() {
				return systemFeedbacks.size() > 20;
			}
		});
	}
}
