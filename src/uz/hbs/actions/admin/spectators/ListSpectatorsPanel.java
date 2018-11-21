package uz.hbs.actions.admin.spectators;

import java.util.ArrayList;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.breadcrumb.BreadCrumbLink;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanelLink;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.HeadersToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilteredAbstractColumn;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.beans.Spectator;
import uz.hbs.db.dataproviders.SortableSpectatorsDataProvider;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;
import uz.hbs.markup.html.panel.ActionsPanel;
import uz.hbs.markup.navigation.BootstrapPagingNavigator.Size;
import uz.hbs.markup.navigation.BootstrapPagingNavigatorToolbar;
import uz.hbs.markup.navigation.repeater.data.table.MyNoRecordsToolbar;
import uz.hbs.markup.navigation.repeater.data.table.filter.MyGoAndClearFilter;
import uz.hbs.markup.navigation.repeater.data.table.filter.MyTextFilteredPropertyColumn;

public class ListSpectatorsPanel extends MyBreadCrumbPanel {
	private static final long serialVersionUID = 1L;

	public ListSpectatorsPanel(String id, IBreadCrumbModel breadCrumbModel) {
		super(id, breadCrumbModel);

		BreadCrumbLink addLink = new BreadCrumbPanelLink("addLink", this, AddSpectatorsPanel.class);
		add(addLink);

		ArrayList<IColumn<Spectator, String>> columns = new ArrayList<IColumn<Spectator, String>>();

		columns.add(new MyTextFilteredPropertyColumn<Spectator, Spectator, String>(new StringResourceModel("id", null), "s.users_id", "users_id"));
		columns.add(new MyTextFilteredPropertyColumn<Spectator, Spectator, String>(new StringResourceModel("spectators.agency.agency_name", null),
				"u.name", "name"));
		columns.add(new PropertyColumn<Spectator, String>(new StringResourceModel("spectators.representative_name", null), "s.last_name",
				"representative_name"));
		columns.add(new MyTextFilteredPropertyColumn<Spectator, Spectator, String>(new StringResourceModel(
				"touragents.accountant_details.contact_number", null), "s.contact_number", "contact_number"));
		columns.add(new MyTextFilteredPropertyColumn<Spectator, Spectator, String>(new StringResourceModel(
				"touragents.accountant_details.contact_email", null), "s.contact_email", "contact_email"));
		columns.add(new FilteredAbstractColumn<Spectator, String>(new StringResourceModel("users.operation", null)) {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getFilter(String componentId, FilterForm<?> form) {
				return new MyGoAndClearFilter(componentId, form);
			}

			@Override
			public void populateItem(Item<ICellPopulator<Spectator>> cellItem, String componentId, final IModel<Spectator> rowModel) {
				cellItem.add(new ActionsPanel<Spectator>(componentId, rowModel) {
					private static final long serialVersionUID = 1L;

					@Override
					public Link<Spectator> addPrintLink(IModel<Spectator> model) {
						return new Link<Spectator>("print") {
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
					public Link<Spectator> addEditLink(IModel<Spectator> model) {
						return new Link<Spectator>("edit") {
							private static final long serialVersionUID = 1L;

							@Override
							public void onClick() {
								activate(new IBreadCrumbPanelFactory() {
									private static final long serialVersionUID = 1L;

									@Override
									public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
										return new EditSpectatorsPanel("panel", breadCrumbModel, rowModel.getObject());
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
					public Link<Spectator> addViewLink(IModel<Spectator> model) {
						return new Link<Spectator>("view") {
							private static final long serialVersionUID = 1L;

							@Override
							public void onClick() {
								activate(new IBreadCrumbPanelFactory() {
									private static final long serialVersionUID = 1L;

									@Override
									public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
										return new ViewSpectatorsPanel("panel", breadCrumbModel, rowModel.getObject());
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
					public Link<Spectator> addDeleteLink(IModel<Spectator> model) {
						return new Link<Spectator>("delete") {
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
					public Link<Spectator> addCancelLink(IModel<Spectator> model) {
						return new Link<Spectator>("cancel") {
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
					public Link<Spectator> addViewLogLink(IModel<Spectator> model) {
						return new Link<Spectator>("viewLog") {
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
					public Link<Spectator> addUserListLink(IModel<Spectator> model) {
						return new Link<Spectator>("userList") {
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
		Spectator filter = new Spectator();

		SortableSpectatorsDataProvider provider = new SortableSpectatorsDataProvider();
		provider.setFilterState(filter);

		final DataTable<Spectator, String> dataTable = new DataTable<Spectator, String>("table", columns, provider, getMySession().getSettings().getTable_rows());
		dataTable.setOutputMarkupId(true);

		final FilterForm<Spectator> form = new FilterForm<Spectator>("filterForm", provider) {
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
		return new StringResourceModel("spectators.list", null);
	}

	@Override
	public Class<?> implementedClass() {
		return this.getClass();
	}
}
