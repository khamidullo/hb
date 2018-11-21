package uz.hbs.actions.admin.hotels.b2c;

import java.util.ArrayList;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
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
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.beans.Hotel;
import uz.hbs.beans.IdAndValue;
import uz.hbs.beans.User;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.db.dataproviders.SortableHotelsB2CDataProvider;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;
import uz.hbs.markup.html.panel.ActionsPanel;
import uz.hbs.markup.html.panel.CheckBoxSlidePanel;
import uz.hbs.markup.navigation.BootstrapPagingNavigator.Size;
import uz.hbs.markup.navigation.BootstrapPagingNavigatorToolbar;
import uz.hbs.markup.navigation.repeater.data.table.MyNoRecordsToolbar;
import uz.hbs.markup.navigation.repeater.data.table.filter.MyChoiceFilteredPropertyColumn;
import uz.hbs.markup.navigation.repeater.data.table.filter.MyGoAndClearFilter;
import uz.hbs.markup.navigation.repeater.data.table.filter.MyTextFilteredPropertyColumn;
import uz.hbs.utils.FormatUtil;
import uz.hbs.utils.models.ReserveModels;
import uz.hbs.utils.models.UserModels;

public class ListHotelsB2CPanel extends MyBreadCrumbPanel {
	private static final long serialVersionUID = 1L;

	public ListHotelsB2CPanel(String id, IBreadCrumbModel breadCrumbModel) {
		super(id, breadCrumbModel);
		
		MyFeedbackPanel feedback = new MyFeedbackPanel("feedback");
		feedback.setOutputMarkupId(true);
		add(feedback);

		ArrayList<IColumn<Hotel, String>> columns = new ArrayList<IColumn<Hotel, String>>();

		columns.add(new MyTextFilteredPropertyColumn<Hotel, Hotel, String>(new StringResourceModel("id", null), "u.id", "users_id"));
		columns.add(new MyChoiceFilteredPropertyColumn<Hotel, IdAndValue, String>(new StringResourceModel("hotels.b2c.status", null), "hp.is_enabled",
				"b2c_status", ReserveModels.getB2CStatusList()) {
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<Hotel>> cellItem, String componentId, IModel<Hotel> rowModel) {
				if (rowModel.getObject().getHotelstatus().getId() == User.STATUS_ACTIVE) {
					cellItem.add(new CheckBoxSlidePanel(componentId, rowModel.getObject().getUsers_id(), rowModel.getObject().getB2c_is_enabled()) {
						private static final long serialVersionUID = 1L;

						@Override
						public void update(AjaxRequestTarget target, boolean isChecked) {
							Hotel hotel = rowModel.getObject();
							hotel.setB2c_is_enabled(isChecked);
							try {
								if (new MyBatisHelper().update("updateHotelsB2CPrices", hotel) == 0) {
									hotel.setB2c_individual_price((double) 0);
									hotel.setB2c_group_price((double) 0);
									hotel.setB2c_is_enabled(true);
									new MyBatisHelper().update("insertHotelsB2CPrices", hotel);
									logger.debug("B2C status inserted: Id=" + hotel.getUsers_id() + ", IndividualPrice=" + hotel.getB2c_individual_price()
											+ ", GroupPrice=" + hotel.getB2c_group_price() + ", Status=" + (isChecked ? 1 : 0));
								} else {
									logger.debug("B2C status updated: Id=" + hotel.getUsers_id() + ", IndividualPrice=" + hotel.getB2c_individual_price()
											+ ", GroupPrice=" + hotel.getB2c_group_price() + ", Status=" + (isChecked ? 1 : 0));
								}
								feedback.success("Статус гостиницы \""+rowModel.getObject().getDisplay_name()+"\" для Б2С " + (isChecked ? "активирован" : "деактивирован"));
							} catch (Exception e) {
								logger.error("Exception", e);
								feedback.error("Ошибка, статус гостиницы \""+rowModel.getObject().getDisplay_name()+"\"  для Б2С неизменен");
							}
							target.add(feedback);
						}
					});
				} else {
					cellItem.add(new Label(componentId));
				}
			}
		});
		columns.add(new MyTextFilteredPropertyColumn<Hotel, Hotel, String>(new StringResourceModel("hotels.details.hotel_display_name", null),
				"hd.display_name", "display_name"));
		columns.add(new MyTextFilteredPropertyColumn<Hotel, Hotel, String>(new StringResourceModel("hotels.name", null), "u.name", "name"));
		columns.add(new MyTextFilteredPropertyColumn<Hotel, Hotel, String>(new StringResourceModel("hotels.details.city", null), "a.city", "city"));
		columns.add(new MyTextFilteredPropertyColumn<Hotel, Hotel, String>(new StringResourceModel("hotels.reservation.individual_reservation", null),
				"hp.individual_price", "b2c_individual_price") {
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<Hotel>> cellItem, String componentId, IModel<Hotel> rowModel) {
				cellItem.add(new Label(componentId, FormatUtil.roundUp(rowModel.getObject().getB2c_individual_price()) + "%"));
			}
		});
		columns.add(new MyTextFilteredPropertyColumn<Hotel, Hotel, String>(new StringResourceModel("hotels.reservation.group_reservation", null),
				"hp.group_price", "b2c_group_price") {
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<Hotel>> cellItem, String componentId, IModel<Hotel> rowModel) {
				cellItem.add(new Label(componentId, FormatUtil.roundUp(rowModel.getObject().getB2c_group_price()) + "%"));
			}
		});
		columns.add(new MyChoiceFilteredPropertyColumn<Hotel, IdAndValue, String>(new StringResourceModel("users.status", null), "u.status",
				"hotelstatus", UserModels.getStatusList()) {
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<Hotel>> cellItem, String componentId, IModel<Hotel> rowModel) {
				cellItem.add(new Label(componentId, new StringResourceModel("users.status" + rowModel.getObject().getHotelstatus().getId(), null)));
			}
		});
		columns.add(new FilteredAbstractColumn<Hotel, String>(new StringResourceModel("users.operation", null)) {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getFilter(String componentId, FilterForm<?> form) {
				return new MyGoAndClearFilter(componentId, form);
			}

			@Override
			public void populateItem(Item<ICellPopulator<Hotel>> cellItem, String componentId, IModel<Hotel> rowModel) {
				cellItem.add(new ActionsPanel<Hotel>(componentId, rowModel) {
					private static final long serialVersionUID = 1L;

					@Override
					public Link<Hotel> addEditLink(final IModel<Hotel> model) {
						return new Link<Hotel>("edit") {
							private static final long serialVersionUID = 1L;

							@Override
							public void onClick() {
								activate(new IBreadCrumbPanelFactory() {
									private static final long serialVersionUID = 1L;

									@Override
									public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
										return new EditHotelB2CPanel(componentId, breadCrumbModel, model.getObject());
									}
								});
							}

							@Override
							public boolean isVisible() {
								return rowModel.getObject().getHotelstatus().getId() == User.STATUS_ACTIVE;
							}
						};
					}

					@Override
					public Link<Hotel> addViewLink(final IModel<Hotel> model) {
						return new Link<Hotel>("view") {
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
					public Link<Hotel> addDeleteLink(IModel<Hotel> model) {
						return new Link<Hotel>("delete") {
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
					public Link<Hotel> addPrintLink(IModel<Hotel> model) {
						return new Link<Hotel>("print") {
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
					public Link<Hotel> addCancelLink(final IModel<Hotel> model) {
						return new Link<Hotel>("cancel") {
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
					public Link<Hotel> addViewLogLink(IModel<Hotel> model) {
						return new Link<Hotel>("viewLog") {
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
					public Link<Hotel> addUserListLink(final IModel<Hotel> model) {
						return new Link<Hotel>("userList") {
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
		Hotel filter = new Hotel();

		SortableHotelsB2CDataProvider provider = new SortableHotelsB2CDataProvider();
		provider.setFilterState(filter);

		final DataTable<Hotel, String> dataTable = new DataTable<Hotel, String>("table", columns, provider,
				getMySession().getSettings().getTable_rows());
		dataTable.setOutputMarkupId(true);

		final FilterForm<Hotel> form = new FilterForm<Hotel>("filterForm", provider) {
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
	public Class<?> implementedClass() {
		return getClass();
	}

	@Override
	public IModel<String> getTitle() {
		return new StringResourceModel("hotels.b2c.list", null);
	}
}
