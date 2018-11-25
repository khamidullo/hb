package uz.hbs.actions.hotel.roomsandrates.rate;

import java.util.ArrayList;

import org.apache.ibatis.session.SqlSession;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.HeadersToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilteredAbstractColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.beans.Hotel;
import uz.hbs.beans.rate.RatePlane;
import uz.hbs.beans.rate.filters.RatePlanesFilter;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.db.dataproviders.SortableRatePlanesDataProvider;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;
import uz.hbs.markup.navigation.BootstrapPagingNavigator.Size;
import uz.hbs.markup.navigation.BootstrapPagingNavigatorToolbar;
import uz.hbs.markup.navigation.repeater.data.table.MyNoRecordsToolbar;
import uz.hbs.markup.navigation.repeater.data.table.filter.MyGoAndClearFilter;
import uz.hbs.markup.navigation.repeater.data.table.filter.MyTextFilteredPropertyColumn;
import uz.hbs.session.MySession;
import uz.hbs.utils.RatePlaneUtil;

public class ListRatePlanePanel extends MyBreadCrumbPanel {
	private static final long serialVersionUID = 1L;
	private MyFeedbackPanel feedback;
	private Hotel hotel;

	@SuppressWarnings("unchecked")
	public ListRatePlanePanel(String id, IBreadCrumbModel breadCrumbModel, Hotel hotel) {
		super(id, breadCrumbModel);
		this.hotel = hotel;

		long hotel_id;
		if (hotel == null) {
			hotel_id = getMySession().getUser().getHotelsusers_id();
		} else {
			hotel_id = hotel.getUsers_id();
		}

		add(feedback = new MyFeedbackPanel("feedback"));
		feedback.setOutputMarkupId(true);

		add(new Link<Void>("addWizardLink") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				activate(new IBreadCrumbPanelFactory() {
					private static final long serialVersionUID = 1L;

					@Override
					public BreadCrumbPanel create(String componentId, IBreadCrumbModel model) {
						return new RatePlanPanel(componentId, model, RatePlaneUtil.createRate(hotel_id));
					}
				});
			}
		});

		ArrayList<IColumn<RatePlane, String>> columns = new ArrayList<IColumn<RatePlane, String>>();

		columns.add(new MyTextFilteredPropertyColumn<RatePlane, RatePlane, String>(new StringResourceModel("hotels.rate.plane.name", null), "name",
				"name"));
		columns.add(new MyTextFilteredPropertyColumn<RatePlane, RatePlane, String>(new StringResourceModel("hotels.rate.plane.description", null),
				"description", "description"));
		columns.add(new PropertyColumn<RatePlane, String>(new StringResourceModel("hotels.rate.plane.internal", null), "internal", "internal") {
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<RatePlane>> item, String componentId, IModel<RatePlane> rowModel) {
				item.add(new Label(componentId, "")
						.add(new AttributeModifier("class", rowModel.getObject().isInternal() ? "fa fa-check" : "fa fa-times"))
						.add(new AttributeModifier("style",
								rowModel.getObject().isInternal() ? "text-align: center; color: green" : "text-align: center; color: red")));
			}
		});
		columns.add(new FilteredAbstractColumn<RatePlane, String>(new StringResourceModel("users.operation", null)) {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getFilter(String componentId, FilterForm<?> form) {
				return new MyGoAndClearFilter(componentId, form);
			}

			@Override
			public void populateItem(Item<ICellPopulator<RatePlane>> cellItem, String componentId, IModel<RatePlane> rowModel) {
				cellItem.add(new RateActionPanel<RatePlane>(componentId, rowModel) {
					private static final long serialVersionUID = 1L;

					@Override
					public Link<RatePlane> addEditLink(final IModel<RatePlane> model) {
						return new Link<RatePlane>("edit") {
							private static final long serialVersionUID = 1L;

							@Override
							public void onClick() {
								activate(new IBreadCrumbPanelFactory() {
									private static final long serialVersionUID = 1L;

									@Override
									public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
										return new RatePlanPanel(componentId, breadCrumbModel, RatePlaneUtil.loadRatePlane(model.getObject()));
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
					public Link<RatePlane> addViewLink(final IModel<RatePlane> model) {
						return new Link<RatePlane>("view") {
							private static final long serialVersionUID = 1L;

							@Override
							public void onClick() {
								activate(new IBreadCrumbPanelFactory() {
									private static final long serialVersionUID = 1L;

									@Override
									public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
										return new RatePlanPanel(componentId, breadCrumbModel, RatePlaneUtil.loadRatePlane(model.getObject()), false);
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
					public Link<RatePlane> addDeleteLink(final IModel<RatePlane> model) {
						return new Link<RatePlane>("delete") {
							private static final long serialVersionUID = 1L;

							@Override
							public void onClick() {
								SqlSession sql = MyBatisHelper.getSqlSessionFactory().openSession(false);
								try {
									sql.delete("deleteRateDetailsByPlane", model.getObject().getId());
									sql.delete("deleteRatePlaneSeasonByPlane", model.getObject().getId());
									sql.delete("deleteRatePlane", model.getObject().getId());
									sql.commit();
									logger.info("Rate plan '" + model.getObject().getName() + "' deleted. HotelId=" + hotel_id
											+ ", InitiatorUserId=" + ((MySession) getSession()).getUser().getId());
									feedback.success(getString("hotels.rate.plane.delete.success"));
								} catch (Exception e) {
									logger.error("Exception, Delete Room Rate", e);
									sql.rollback();
									feedback.error(getString("hotels.rate.plane.delete.fail"));
								} finally {
									sql.close();
								}
							}

							@Override
							public boolean isVisible() {
								return true;
							}
						};
					}

					@Override
					public Link<RatePlane> addSaleLink(final IModel<RatePlane> model) {
						return new Link<RatePlane>("sale") {
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
				});
			}
		});
		RatePlanesFilter filter = new RatePlanesFilter(hotel_id);

		SortableRatePlanesDataProvider provider = new SortableRatePlanesDataProvider();
		provider.setFilterState(filter);

		final DataTable<RatePlane, String> dataTable = new DataTable<RatePlane, String>("table", columns, provider,
				getMySession().getSettings().getTable_rows());
		dataTable.setOutputMarkupId(true);

		@SuppressWarnings("rawtypes")
		final FilterForm form = new FilterForm("filterForm", provider) {
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
		String title = hotel != null
				? new StringResourceModel("hotels.room_rates.list", null).getString() + ". \"" + hotel.getDisplay_name() + "\", ID: "
						+ hotel.getUsers_id()
				: new StringResourceModel("hotels.room_rates.list", null).getString();
		return Model.of(title);
	}

	@Override
	public Class<?> implementedClass() {
		return this.getClass();
	}
}