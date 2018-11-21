package uz.hbs.actions.admin.addresses.city;

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

import uz.hbs.beans.City;
import uz.hbs.db.dataproviders.SortableCityDataProvider;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;
import uz.hbs.markup.html.panel.ActionsPanel;
import uz.hbs.markup.navigation.BootstrapPagingNavigator.Size;
import uz.hbs.markup.navigation.BootstrapPagingNavigatorToolbar;
import uz.hbs.markup.navigation.repeater.data.table.MyNoRecordsToolbar;
import uz.hbs.markup.navigation.repeater.data.table.filter.MyGoAndClearFilter;
import uz.hbs.markup.navigation.repeater.data.table.filter.MyTextFilteredPropertyColumn;

public class ListCityPanel extends MyBreadCrumbPanel {
	private static final long serialVersionUID = 1L;

	public ListCityPanel(String id, IBreadCrumbModel breadCrumbModel) {
		super(id, breadCrumbModel);

		final MyFeedbackPanel feedback = new MyFeedbackPanel("feedback");
		feedback.setOutputMarkupId(true);
		add(feedback);

		final ModalWindow dialog = new ModalWindow("dialog");
		dialog.setAutoSize(true);
		dialog.setMinimalHeight(100);
		dialog.setMinimalWidth(500);
		dialog.setCookieName("deleteCity");
		dialog.setInitialHeight(100);
		dialog.setInitialWidth(500);
		add(dialog);

		final WebMarkupContainer container = new WebMarkupContainer("container");
		container.setOutputMarkupId(true);
		add(container);

		Link<City> addLink = new Link<City>("addLink") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				activate(new IBreadCrumbPanelFactory() {
					private static final long serialVersionUID = 1L;

					@Override
					public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
						return new AddCityPanel(componentId, breadCrumbModel);
					}
				});
			}
		};
		container.add(addLink);

		ArrayList<IColumn<City, String>> columns = new ArrayList<IColumn<City, String>>();

		columns.add(new MyTextFilteredPropertyColumn<City, City, String>(new StringResourceModel("id", null), "ct.id",
				"id"));
		columns.add(new MyTextFilteredPropertyColumn<City, City, String>(new StringResourceModel("hotels.details.country", null), "c.name",
				"country_name"));
		columns.add(new MyTextFilteredPropertyColumn<City, City, String>(new StringResourceModel("hotels.details.region", null), "r.name",
				"region_name"));
		columns.add(new MyTextFilteredPropertyColumn<City, City, String>(new StringResourceModel("address.city", null, new Object[] { " (Ru)" }), "ct.name", "name"));
		columns.add(new MyTextFilteredPropertyColumn<City, City, String>(
				new StringResourceModel("address.city", null, new Object[] { " (Uz)" }), "ct.name_uz", "name_uz"));
		columns.add(new MyTextFilteredPropertyColumn<City, City, String>(new StringResourceModel("address.city", null, new Object[] { " (En)" }), "ct.name_en",
				"name_en"));
		columns.add(new FilteredAbstractColumn<City, String>(new StringResourceModel("users.operation", null)) {

			private static final long serialVersionUID = 1L;

			@Override
			public Component getFilter(String componentId, FilterForm<?> form) {
				return new MyGoAndClearFilter(componentId, form);
			}

			@Override
			public void populateItem(Item<ICellPopulator<City>> cellItem, String componentId, final IModel<City> rowModel) {
				cellItem.add(new ActionsPanel<City>(componentId, rowModel) {
					private static final long serialVersionUID = 1L;

					@Override
					public AjaxLink<City> addDeleteLink(IModel<City> model) {
						return new AjaxLink<City>("delete") {
							private static final long serialVersionUID = 1L;

							@Override
							public void onClick(AjaxRequestTarget target) {
								dialog.setTitle(new StringResourceModel("address.city.delete", null));
								dialog.setContent(new DeleteCityPanel(dialog.getContentId(), rowModel.getObject(), dialog, feedback, container));
								dialog.show(target);
							}

							@Override
							public boolean isVisible() {
								return true;
							}
						};
					}

					@Override
					public Link<City> addEditLink(IModel<City> model) {
						return new Link<City>("edit") {
							private static final long serialVersionUID = 1L;

							@Override
							public void onClick() {
								activate(new IBreadCrumbPanelFactory() {
									private static final long serialVersionUID = 1L;

									@Override
									public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
										return new EditCityPanel(componentId, breadCrumbModel, rowModel.getObject());
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
					public Link<City> addViewLink(IModel<City> model) {
						return new Link<City>("view") {
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
					public AjaxLink<City> addPrintLink(IModel<City> model) {
						return new AjaxLink<City>("print") {
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
					public AjaxLink<City> addCancelLink(IModel<City> model) {
						return new AjaxLink<City>("cancel") {
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
					public AbstractLink addViewLogLink(IModel<City> model) {
						return new AjaxLink<City>("viewLog") {
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
					public AbstractLink addUserListLink(IModel<City> model) {
						return new AjaxLink<City>("userList") {
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

		City filter = new City();

		SortableCityDataProvider provider = new SortableCityDataProvider();
		provider.setFilterState(filter);

		final DataTable<City, String> dataTable = new DataTable<City, String>("table", columns, provider,
				getMySession().getSettings().getTable_rows());
		dataTable.setOutputMarkupId(true);

		final FilterForm<City> form = new FilterForm<City>("filterForm", provider) {
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
		return new StringResourceModel("address.cities", null);
	}
}
