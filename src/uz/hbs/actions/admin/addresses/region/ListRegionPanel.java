package uz.hbs.actions.admin.addresses.region;

import java.util.ArrayList;

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
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.beans.Region;
import uz.hbs.db.dataproviders.SortableRegionDataProvider;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;
import uz.hbs.markup.html.panel.ActionsPanel;
import uz.hbs.markup.navigation.BootstrapPagingNavigator.Size;
import uz.hbs.markup.navigation.BootstrapPagingNavigatorToolbar;
import uz.hbs.markup.navigation.repeater.data.table.MyNoRecordsToolbar;
import uz.hbs.markup.navigation.repeater.data.table.filter.MyGoAndClearFilter;
import uz.hbs.markup.navigation.repeater.data.table.filter.MyTextFilteredPropertyColumn;

public class ListRegionPanel extends MyBreadCrumbPanel {
	private static final long serialVersionUID = 1L;

	public ListRegionPanel(String id, IBreadCrumbModel breadCrumbModel) {
		super(id, breadCrumbModel);

		final MyFeedbackPanel feedback = new MyFeedbackPanel("feedback");
		feedback.setOutputMarkupId(true);
		add(feedback);

		final ModalWindow dialog = new ModalWindow("dialog");
		dialog.setAutoSize(true);
		dialog.setMinimalHeight(100);
		dialog.setMinimalWidth(500);
		dialog.setCookieName("deleteRegion");
		dialog.setInitialHeight(100);
		dialog.setInitialWidth(500);
		add(dialog);

		final WebMarkupContainer container = new WebMarkupContainer("container");
		container.setOutputMarkupId(true);
		add(container);

		Link<Region> addLink = new Link<Region>("addLink") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				activate(new IBreadCrumbPanelFactory() {
					private static final long serialVersionUID = 1L;

					@Override
					public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
						return new AddRegionPanel(componentId, breadCrumbModel);
					}
				});
			}
		};
		container.add(addLink);

		ArrayList<IColumn<Region, String>> columns = new ArrayList<IColumn<Region, String>>();

		columns.add(new MyTextFilteredPropertyColumn<Region, Region, String>(new StringResourceModel("id", null), "r.id",
				"id"));
		columns.add(new MyTextFilteredPropertyColumn<Region, Region, String>(new StringResourceModel("hotels.details.country", null), "c.name",
				"country_name"));
		columns.add(new MyTextFilteredPropertyColumn<Region, Region, String>(new StringResourceModel("address.region", null, new Object[] { " (Ru)" }), "r.name", "name"));
		columns.add(new MyTextFilteredPropertyColumn<Region, Region, String>(
				new StringResourceModel("address.region", null, new Object[] { " (Uz)" }), "r.name_uz", "name_uz"));
		columns.add(new MyTextFilteredPropertyColumn<Region, Region, String>(new StringResourceModel("address.region", null, new Object[] { " (En)" }), "r.name_en",
				"name_en"));
		columns.add(new FilteredAbstractColumn<Region, String>(new StringResourceModel("users.operation", null)) {

			private static final long serialVersionUID = 1L;

			@Override
			public Component getFilter(String componentId, FilterForm<?> form) {
				return new MyGoAndClearFilter(componentId, form);
			}

			@Override
			public void populateItem(Item<ICellPopulator<Region>> cellItem, String componentId, final IModel<Region> rowModel) {
				cellItem.add(new ActionsPanel<Region>(componentId, rowModel) {
					private static final long serialVersionUID = 1L;

					@Override
					public AjaxLink<Region> addDeleteLink(IModel<Region> model) {
						return new AjaxLink<Region>("delete") {
							private static final long serialVersionUID = 1L;

							@Override
							public void onClick(AjaxRequestTarget target) {
								dialog.setTitle(new StringResourceModel("address.region.delete", null));
								dialog.setContent(new DeleteRegionPanel(dialog.getContentId(), rowModel.getObject(), dialog, feedback, container));
								dialog.show(target);
							}

							@Override
							public boolean isVisible() {
								return true;
							}
						};
					}

					@Override
					public Link<Region> addEditLink(IModel<Region> model) {
						return new Link<Region>("edit") {
							private static final long serialVersionUID = 1L;

							@Override
							public void onClick() {
								activate(new IBreadCrumbPanelFactory() {
									private static final long serialVersionUID = 1L;

									@Override
									public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
										return new EditRegionPanel(componentId, breadCrumbModel, rowModel.getObject());
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
					public Link<Region> addViewLink(IModel<Region> model) {
						return new Link<Region>("view") {
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
					public AjaxLink<Region> addPrintLink(IModel<Region> model) {
						return new AjaxLink<Region>("print") {
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
					public AjaxLink<Region> addCancelLink(IModel<Region> model) {
						return new AjaxLink<Region>("cancel") {
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
					public AbstractLink addViewLogLink(IModel<Region> model) {
						return new AjaxLink<Region>("viewLog") {
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
					public AbstractLink addUserListLink(IModel<Region> model) {
						return new AjaxLink<Region>("userList") {
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
		});

		Region filter = new Region();

		SortableRegionDataProvider provider = new SortableRegionDataProvider();
		provider.setFilterState(filter);

		final DataTable<Region, String> dataTable = new DataTable<Region, String>("table", columns, provider,
				getMySession().getSettings().getTable_rows());
		dataTable.setOutputMarkupId(true);

		final FilterForm<Region> form = new FilterForm<Region>("filterForm", provider) {
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
	public Class<?> implementedClass() {
		return getClass();
	}

	@Override
	public IModel<String> getTitle() {
		return new StringResourceModel("address.regions", null);
	}
}
