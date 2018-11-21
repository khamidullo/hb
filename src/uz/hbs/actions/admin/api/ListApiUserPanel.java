package uz.hbs.actions.admin.api;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
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
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.beans.IdAndValue;
import uz.hbs.beans.User;
import uz.hbs.db.dataproviders.SortableUsersDataProvider;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;
import uz.hbs.markup.html.panel.ActionsPanel;
import uz.hbs.markup.navigation.BootstrapPagingNavigator.Size;
import uz.hbs.markup.navigation.BootstrapPagingNavigatorToolbar;
import uz.hbs.markup.navigation.repeater.data.table.MyNoRecordsToolbar;
import uz.hbs.markup.navigation.repeater.data.table.filter.MyChoiceFilteredPropertyColumn;
import uz.hbs.markup.navigation.repeater.data.table.filter.MyGoAndClearFilter;
import uz.hbs.markup.navigation.repeater.data.table.filter.MyTextFilteredPropertyColumn;
import uz.hbs.utils.models.UserModels;

public class ListApiUserPanel  extends MyBreadCrumbPanel {
	private static final long serialVersionUID = 1L;

	public ListApiUserPanel(String id, IBreadCrumbModel breadCrumbModel) {
		super(id, breadCrumbModel);

		ArrayList<IColumn<User, String>> columns = new ArrayList<IColumn<User, String>>();

		columns.add(new MyTextFilteredPropertyColumn<User, User, String>(new StringResourceModel("id", null), "u.id", "id"));
		columns.add(new MyTextFilteredPropertyColumn<User, User, String>(new StringResourceModel("users.login", null), "u.login", "login"));
		columns.add(new MyTextFilteredPropertyColumn<User, User, String>(new StringResourceModel("users.name", null), "u.name", "name"));

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
					public Link<User> addPrintLink(IModel<User> model) {
						return new Link<User>("print") {
							private static final long serialVersionUID = 1L;

							@Override
							public void onClick() {
							}

							@Override
							public boolean isVisible() {
								return false;
							}
						};
					}

					@Override
					public Link<User> addEditLink(IModel<User> model) {
						return new Link<User>("edit") {
							private static final long serialVersionUID = 1L;

							@Override
							public void onClick() {
							}

							@Override
							public boolean isVisible() {
								return false;
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
										return new ViewApiUserPanel(componentId, breadCrumbModel, rowModel.getObject());
									}
								});
							}

							@Override
							public boolean isVisible() {
								return getMySession().getUser().getLogin().equalsIgnoreCase("admin");
							}
						};
					}

					@Override
					public Link<User> addDeleteLink(IModel<User> model) {
						return new Link<User>("delete") {
							private static final long serialVersionUID = 1L;

							@Override
							public void onClick() {
							}

							@Override
							public boolean isVisible() {
								return false;
							}
						};
					}

					@Override
					public Link<User> addCancelLink(IModel<User> model) {
						return new Link<User>("cancel") {
							private static final long serialVersionUID = 1L;

							@Override
							public void onClick() {
							}

							@Override
							public boolean isVisible() {
								return false;
							}
						};
					}
					
					@Override
					public Link<User> addViewLogLink(IModel<User> model) {
						return new Link<User>("viewLog") {
							private static final long serialVersionUID = 1L;

							public void onClick() {
							}

							@Override
							public boolean isVisible() {
								return false;
							}
						};
					}
					
					@Override
					public Link<User> addUserListLink(IModel<User> model) {
						return new Link<User>("userList") {
							private static final long serialVersionUID = 1L;

							public void onClick() {
							}

							@Override
							public boolean isVisible() {
								return false;
							}
						};
					}
				});
			}
		});
		User filter = new User();
		filter.setType(new IdAndValue((int) User.TYPE_API));

		SortableUsersDataProvider provider = new SortableUsersDataProvider();
		provider.setFilterState(filter);

		final DataTable<User, String> dataTable = new DataTable<User, String>("table", columns, provider, getMySession().getSettings().getTable_rows());
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

		form.add(dataTable);
		dataTable.addTopToolbar(new BootstrapPagingNavigatorToolbar(dataTable, Size.Small));
		dataTable.addTopToolbar(new HeadersToolbar<String>(dataTable, provider));
		dataTable.addTopToolbar(new FilterToolbar(dataTable, form, provider));
		dataTable.addBottomToolbar(new BootstrapPagingNavigatorToolbar(dataTable, Size.Small));
		dataTable.addBottomToolbar(new MyNoRecordsToolbar(dataTable));

		add(form);
	}

	@Override
	public IModel<String> getTitle() {
		return new StringResourceModel("users.api.list", null);
	}

	@Override
	public Class<?> implementedClass() {
		return getClass();
	}
}
