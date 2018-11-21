package uz.hbs.actions.admin.users;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.HeadersToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilteredAbstractColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.beans.IdAndValue;
import uz.hbs.beans.User;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.db.dataproviders.SortableUsersDataProvider;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;
import uz.hbs.markup.html.panel.ActionsPanel;
import uz.hbs.markup.navigation.BootstrapPagingNavigator.Size;
import uz.hbs.markup.navigation.BootstrapPagingNavigatorToolbar;
import uz.hbs.markup.navigation.repeater.data.table.MyNoRecordsToolbar;
import uz.hbs.markup.navigation.repeater.data.table.filter.MyChoiceFilteredPropertyColumn;
import uz.hbs.markup.navigation.repeater.data.table.filter.MyGoAndClearFilter;
import uz.hbs.markup.navigation.repeater.data.table.filter.MyTextFilteredPropertyColumn;
import uz.hbs.utils.models.UserModels;

public class ListUsersPanel extends MyBreadCrumbPanel {
	private static final long serialVersionUID = 1L;
	private String ownerName;

	public ListUsersPanel(String id, final IBreadCrumbModel breadCrumbModel, final User owner, String ownerName) {
		this(id, breadCrumbModel, owner, ownerName, false);
	}

	public ListUsersPanel(String id, final IBreadCrumbModel breadCrumbModel, final User owner, String ownerName, boolean workable) {
		super(id, breadCrumbModel);

		this.ownerName = ownerName;

		// BreadCrumbLink addLink = new BreadCrumbPanelLink("addLink", this, AddUsersPanel.class);
		// addLink.setVisible(filter.getWork() == null);
		// add(addLink);

		final MyFeedbackPanel feedback = new MyFeedbackPanel("feedback");
		feedback.setOutputMarkupId(true);
		add(feedback);

		final ModalWindow dialog = new ModalWindow("dialog");
		dialog.setAutoSize(true);
		dialog.setMinimalHeight(100);
		dialog.setMinimalWidth(500);
		dialog.setCookieName("listUser");
		dialog.setInitialHeight(100);
		dialog.setInitialWidth(500);
		add(dialog);

		final WebMarkupContainer container = new WebMarkupContainer("container");
		container.setOutputMarkupId(true);
		add(container);

		Link<User> addLink = new Link<User>("addLink") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				activate(new IBreadCrumbPanelFactory() {
					private static final long serialVersionUID = 1L;

					@Override
					public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
						return new AddUserPanel(componentId, breadCrumbModel, owner, workable);
					}
				});
			}
		};
		container.add(addLink);

		ArrayList<IColumn<User, String>> columns = new ArrayList<IColumn<User, String>>();

		columns.add(new MyTextFilteredPropertyColumn<User, User, String>(new StringResourceModel("id", null), "u.id", "id"));
		columns.add(new MyTextFilteredPropertyColumn<User, User, String>(new StringResourceModel("users.login", null), "u.login", "login") {
			private static final long serialVersionUID = 1L;

			@Override
			public String getCssClass() {
				return "bold";
			}
		});
		columns.add(new MyTextFilteredPropertyColumn<User, User, String>(new StringResourceModel("users.name", null), "u.name", "name"));

		columns.add(new MyTextFilteredPropertyColumn<User, User, String>(new StringResourceModel("organization", null), "ou.name", "org_name"));

		LoadableDetachableModel<List<? extends IdAndValue>> typeList = UserModels.getTypeList(true,
				owner != null && owner.getType() != null ? owner.getType().getId() : null);

		columns.add(new MyChoiceFilteredPropertyColumn<User, IdAndValue, String>(new StringResourceModel("users.type", null), "u.type", "type",
				typeList) {
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<User>> cellItem, String componentId, IModel<User> rowModel) {
				cellItem.add(new Label(componentId, new StringResourceModel("users.type" + rowModel.getObject().getType().getId(), null)));
			}
		});

		LoadableDetachableModel<List<? extends IdAndValue>> statusList = UserModels.getStatusList();

		columns.add(new MyChoiceFilteredPropertyColumn<User, IdAndValue, String>(new StringResourceModel("users.status", null), "u.status", "status",
				statusList) {
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<User>> cellItem, String componentId, IModel<User> rowModel) {
				cellItem.add(new Label(componentId, new StringResourceModel("users.status" + rowModel.getObject().getStatus().getId(), null)));
			}
		});
		columns.add(new FilteredAbstractColumn<User, String>(new StringResourceModel("users.operation", null)) {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getFilter(String componentId, FilterForm<?> form) {
				return new MyGoAndClearFilter(componentId, form);
			}

			@Override
			public void populateItem(Item<ICellPopulator<User>> cellItem, String componentId, final IModel<User> rowModel) {
				cellItem.add(new ActionsPanel<User>(componentId, rowModel) {
					private static final long serialVersionUID = 1L;

					@Override
					public AjaxLink<User> addDeleteLink(IModel<User> model) {
						return new AjaxLink<User>("delete") {
							private static final long serialVersionUID = 1L;

							@Override
							public void onClick(AjaxRequestTarget target) {
								dialog.setTitle(new StringResourceModel("users.status.change.delete", null,
										new Object[] { rowModel.getObject().getLogin() }));
								dialog.setContent(new DeleteUserPanel(dialog.getContentId(), rowModel.getObject(), dialog, feedback, container));
								dialog.show(target);
							}

							@Override
							public boolean isVisible() {
								boolean canBeDeleted = true;
								User user = model.getObject();
								if (user.getType().getId() == User.TYPE_TOURAGENT_USER) {
									Map<String, Object> params = new HashMap<>();
									params.put("user_type", User.TYPE_TOURAGENT_USER);
									params.put("creator_user_id", user.getId());
									if ((Integer) new MyBatisHelper().selectOne("selectReservationsCountByTypeOrStatus", params) > 0)
										canBeDeleted = false;
									else
										canBeDeleted = true;
								}
								return canBeDeleted;
							}
						};
					}

					@Override
					public Link<User> addEditLink(IModel<User> model) {
						return new Link<User>("edit") {
							private static final long serialVersionUID = 1L;

							@Override
							public void onClick() {
								activate(new IBreadCrumbPanelFactory() {
									private static final long serialVersionUID = 1L;

									@Override
									public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
										return new EditUserPanel(componentId, breadCrumbModel, rowModel.getObject(), workable);
									}
								});
							}

							@Override
							public boolean isVisible() {
								return true;
							}
						};
					}

					@Override
					public Link<User> addViewLink(IModel<User> model) {
						return new Link<User>("view") {
							private static final long serialVersionUID = 1L;

							@Override
							public void onClick() {
								activate(new IBreadCrumbPanelFactory() {
									private static final long serialVersionUID = 1L;

									@Override
									public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
										return new ViewUserPanel(componentId, breadCrumbModel, rowModel.getObject());
									}
								});
							}

							@Override
							public boolean isVisible() {
								return true;
							}
						};
					}
				});
			}
		});

		User filter = new User();

		if (owner != null && owner.getType() != null) {
			filter.setType(owner.getType());
			filter.setWork(owner.getWork());
		}

		SortableUsersDataProvider provider = new SortableUsersDataProvider();
		provider.setFilterState(filter);

		final DataTable<User, String> dataTable = new DataTable<User, String>("table", columns, provider,
				getMySession().getSettings().getTable_rows());
		dataTable.setOutputMarkupId(true);

		final FilterForm<User> form = new FilterForm<User>("filterForm", provider) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				dataTable.setCurrentPage(0);
			}

			@Override
			public boolean isVisible() {
				return dataTable.size() > 0;
			}
		};

		dataTable.addTopToolbar(new BootstrapPagingNavigatorToolbar(dataTable, Size.Small));
		dataTable.addTopToolbar(new HeadersToolbar<String>(dataTable, provider));
		dataTable.addTopToolbar(new FilterToolbar(dataTable, form, provider));
		dataTable.addBottomToolbar(new BootstrapPagingNavigatorToolbar(dataTable, Size.Small));
		dataTable.addBottomToolbar(new MyNoRecordsToolbar(dataTable));

		form.add(dataTable);

		container.add(form);
	}

	@Override
	public IModel<String> getTitle() {
		return Model.of(new StringResourceModel("users.list", null).getString() + (ownerName == null ? "" : " [" + ownerName + "]"));
	}

	@Override
	public Class<?> implementedClass() {
		return this.getClass();
	}
}
