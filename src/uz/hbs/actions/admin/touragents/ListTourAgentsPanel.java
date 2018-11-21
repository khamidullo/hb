package uz.hbs.actions.admin.touragents;

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
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilteredAbstractColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.actions.admin.users.ListUsersPanel;
import uz.hbs.beans.IdAndValue;
import uz.hbs.beans.IdLongAndName;
import uz.hbs.beans.TourAgent;
import uz.hbs.beans.User;
import uz.hbs.db.dataproviders.SortableTourAgentsDataProvider;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;
import uz.hbs.markup.html.panel.ActionsPanel;
import uz.hbs.markup.navigation.BootstrapPagingNavigator.Size;
import uz.hbs.markup.navigation.BootstrapPagingNavigatorToolbar;
import uz.hbs.markup.navigation.repeater.data.table.MyNoRecordsToolbar;
import uz.hbs.markup.navigation.repeater.data.table.filter.MyGoAndClearFilter;
import uz.hbs.markup.navigation.repeater.data.table.filter.MyTextFilteredPropertyColumn;

public class ListTourAgentsPanel extends MyBreadCrumbPanel {
	private static final long serialVersionUID = 1L;

	public ListTourAgentsPanel(String id, IBreadCrumbModel breadCrumbModel) {
		super(id, breadCrumbModel);

		BreadCrumbLink addLink = new BreadCrumbPanelLink("addLink", this, AddTourAgentsPanel.class);
		add(addLink);
		
		ArrayList<IColumn<TourAgent, String>> columns = new ArrayList<IColumn<TourAgent, String>>();
		
		columns.add(new MyTextFilteredPropertyColumn<TourAgent, TourAgent, String>(new StringResourceModel("id", null), "ta.users_id", "users_id"));
		columns.add(new MyTextFilteredPropertyColumn<TourAgent, TourAgent, String>(new StringResourceModel("touragents.company.tour_agency_display_name", null), "ta.display_name", "display_name"));
		columns.add(new MyTextFilteredPropertyColumn<TourAgent, TourAgent, String>(new StringResourceModel("touragents.company.tour_agency_legal_name", null), "u.name", "name"));
		columns.add(new MyTextFilteredPropertyColumn<TourAgent, TourAgent, String>(new StringResourceModel("hotels.details.city", null), "a.city", "city"));
		columns.add(new MyTextFilteredPropertyColumn<TourAgent, TourAgent, String>(new StringResourceModel("touragents.contact_number", null), "ta.primary_phone", "primary_phone"){
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<TourAgent>> item, String componentId, IModel<TourAgent> rowModel) {
				item.add(new Label(componentId, rowModel.getObject().getPrimary_phone() != null ? rowModel.getObject().getPrimary_phone().replace(" ", "") : ""));
			}
		});
		columns.add(new MyTextFilteredPropertyColumn<TourAgent, TourAgent, String>(new StringResourceModel("touragents.contact_email", null), "u.email", "email"));
		columns.add(new FilteredAbstractColumn<TourAgent, String>(new StringResourceModel("users.operation", null)){
			private static final long serialVersionUID = 1L;

			@Override
			public Component getFilter(String componentId, FilterForm<?> form) {
				return new MyGoAndClearFilter(componentId, form);
			}

			@Override
			public void populateItem(Item<ICellPopulator<TourAgent>> cellItem, String componentId, final IModel<TourAgent> rowModel) {
				cellItem.add(new ActionsPanel<TourAgent>(componentId, rowModel){
					private static final long serialVersionUID = 1L;

					@Override
					public Link<TourAgent> addPrintLink(IModel<TourAgent> model) {
						return new Link<TourAgent>("print") {
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
					public Link<TourAgent> addEditLink(IModel<TourAgent> model) {
						return new Link<TourAgent>("edit") {
							private static final long serialVersionUID = 1L;

							@Override
							public void onClick() {
								activate(new IBreadCrumbPanelFactory() {
									private static final long serialVersionUID = 1L;

									@Override
									public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
										return new EditTourAgentsPanel("panel", breadCrumbModel, rowModel.getObject());
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
					public Link<TourAgent> addViewLink(IModel<TourAgent> model) {
						return new Link<TourAgent>("view") {
							private static final long serialVersionUID = 1L;

							@Override
							public void onClick() {
								activate(new IBreadCrumbPanelFactory() {
									private static final long serialVersionUID = 1L;

									@Override
									public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
										return new ViewTourAgentsPanel("panel", breadCrumbModel, rowModel.getObject());
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
					public Link<TourAgent> addDeleteLink(IModel<TourAgent> model) {
						return new Link<TourAgent>("delete") {
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
					public Link<TourAgent> addCancelLink(IModel<TourAgent> model) {
						return new Link<TourAgent>("cancel") {
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
					public Link<TourAgent> addViewLogLink(IModel<TourAgent> model) {
						return new Link<TourAgent>("viewLog") {
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
					public Link<TourAgent> addUserListLink(final IModel<TourAgent> model) {
						return new Link<TourAgent>("userList") {
							private static final long serialVersionUID = 1L;

							public void onClick() {
								activate(new IBreadCrumbPanelFactory() {
									private static final long serialVersionUID = 1L;

									@Override
									public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
										User user = new User();
										user.setWork(new IdLongAndName(model.getObject().getUsers_id()));
										user.setType(new IdAndValue((int) User.TYPE_TOURAGENT_USER));
										return new ListUsersPanel(componentId, breadCrumbModel, user, model.getObject().getName(), true);
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
		TourAgent filter = new TourAgent();

		SortableTourAgentsDataProvider provider = new SortableTourAgentsDataProvider();
		provider.setFilterState(filter);

		final DataTable<TourAgent, String> dataTable = new DataTable<TourAgent, String>("table", columns, provider, getMySession().getSettings().getTable_rows());
		dataTable.setOutputMarkupId(true);

		final FilterForm<TourAgent> form = new FilterForm<TourAgent>("filterForm", provider) {
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
		return new StringResourceModel("touragents.list", null);
	}

	@Override
	public Class<?> implementedClass() {
		return this.getClass();
	}
}
