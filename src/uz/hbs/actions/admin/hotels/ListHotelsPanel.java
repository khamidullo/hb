package uz.hbs.actions.admin.hotels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.attributes.IAjaxCallListener;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
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
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.actions.admin.hotels.tabbed.AddHotelPanel;
import uz.hbs.actions.admin.hotels.tabbed.EditHotelPanel;
import uz.hbs.actions.admin.users.ListUsersPanel;
import uz.hbs.actions.hotel.roomsandrates.ListAvailabilityRoom;
import uz.hbs.actions.hotel.roomsandrates.rate.ListRatePlanePanel;
import uz.hbs.actions.touragent.newbooking.NewBookingPanel;
import uz.hbs.beans.Country;
import uz.hbs.beans.Hotel;
import uz.hbs.beans.IdAndValue;
import uz.hbs.beans.IdLongAndName;
import uz.hbs.beans.User;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.db.dataproviders.SortableHotelsDataProvider;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;
import uz.hbs.markup.html.panel.ActionsPanel;
import uz.hbs.markup.navigation.BootstrapPagingNavigator.Size;
import uz.hbs.markup.navigation.BootstrapPagingNavigatorToolbar;
import uz.hbs.markup.navigation.repeater.data.table.MyNoRecordsToolbar;
import uz.hbs.markup.navigation.repeater.data.table.filter.MyChoiceFilteredPropertyColumn;
import uz.hbs.markup.navigation.repeater.data.table.filter.MyGoAndClearFilter;
import uz.hbs.markup.navigation.repeater.data.table.filter.MyTextFilteredPropertyColumn;
import uz.hbs.utils.models.HotelModels;
import uz.hbs.utils.models.UserModels;

public class ListHotelsPanel extends MyBreadCrumbPanel {
	private static final long serialVersionUID = 1L;

	public ListHotelsPanel(String id, IBreadCrumbModel breadCrumbModel) {
		super(id, breadCrumbModel);

		MyFeedbackPanel feedback = new MyFeedbackPanel("feedback");
		feedback.setOutputMarkupId(true);
		add(feedback);

		WebMarkupContainer container = new WebMarkupContainer("container");
		container.setOutputMarkupId(true);
		add(container);

		Map<String, Object> params = new HashMap<>();
		params.put("code", "uz");
		params.put("locale", getMySession().getLocale().getLanguage());

		Hotel filter = new Hotel();
		filter.setCountries(new MyBatisHelper().selectOne("selectCountryByCode", params));

		final Form<Hotel> filterForm = new Form<Hotel>("form", new CompoundPropertyModel<Hotel>(filter));
		filterForm.setOutputMarkupId(true);
		add(filterForm);

		LoadableDetachableModel<List<? extends Country>> countriesList = HotelModels.getCountriesHasHotelList(getMySession().getLocale().getLanguage());

		DropDownChoice<Country> countries = new DropDownChoice<>("countries", countriesList, new ChoiceRenderer<Country>("name", "code"));
		countries.setRequired(true);
		filterForm.add(countries);

		countries.add(new OnChangeAjaxBehavior() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				filter.setCountries((Country) countries.getDefaultModelObject());
				target.add(container);
			}
		});

		BreadCrumbLink addWizardLink = new BreadCrumbPanelLink("addWizardLink", this, AddHotelPanel.class);
		add(addWizardLink);

		ArrayList<IColumn<Hotel, String>> columns = new ArrayList<IColumn<Hotel, String>>();

		columns.add(new MyTextFilteredPropertyColumn<Hotel, Hotel, String>(new StringResourceModel("id", null), "u.id", "users_id"));
		columns.add(new MyTextFilteredPropertyColumn<Hotel, Hotel, String>(new StringResourceModel("hotels.details.hotel_display_name", null),
				"hd.display_name", "display_name"));
		columns.add(new MyTextFilteredPropertyColumn<Hotel, Hotel, String>(new StringResourceModel("hotels.legal.name", null), "u.name", "name"));
		columns.add(new MyTextFilteredPropertyColumn<Hotel, Hotel, String>(new StringResourceModel("hotels.details.city", null), "a.city", "city"));
		columns.add(new MyTextFilteredPropertyColumn<Hotel, Hotel, String>(new StringResourceModel("hotels.contact_number", null), "h.primary_phone",
				"primary_phone") {
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<Hotel>> item, String componentId, IModel<Hotel> rowModel) {
				item.add(new Label(componentId,
						rowModel.getObject().getPrimary_phone() != null ? rowModel.getObject().getPrimary_phone().replace(" ", "") : ""));
			}
		});
		columns.add(new MyTextFilteredPropertyColumn<Hotel, Hotel, String>(new StringResourceModel("hotels.contact_email", null), "h.corporate_email",
				"corporate_email"));
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
										return new EditHotelPanel(componentId, breadCrumbModel, model.getObject());
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
					public Link<Hotel> addViewLink(final IModel<Hotel> model) {
						return new Link<Hotel>("view") {
							private static final long serialVersionUID = 1L;

							@Override
							public void onClick() {
								activate(new IBreadCrumbPanelFactory() {
									private static final long serialVersionUID = 1L;

									@Override
									public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
										return new ViewHotelsPanel(componentId, breadCrumbModel, model);
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
					public AjaxLink<Hotel> addDeleteLink(IModel<Hotel> model) {
						return new AjaxLink<Hotel>("delete") {
							private static final long serialVersionUID = 1L;

							@Override
							public void onClick(AjaxRequestTarget target) {
								if (getMySession().getUser().getLogin().equalsIgnoreCase("admin")) {
									SqlSession sql = MyBatisHelper.getSqlSessionFactory().openSession(false);
									try {
										int deleted = 0;
										deleted += sql.delete("deleteHotelsDetailsByAdmin", model.getObject().getUsers_id());
										deleted += sql.delete("deleteHotelsEquipmentsByAdmin", model.getObject().getUsers_id());
										deleted += sql.delete("deleteHotelsFacilitiesByAdmin", model.getObject().getUsers_id());
										deleted += sql.delete("deleteHotelsNearbyPlacesByAdmin", model.getObject().getUsers_id());
										deleted += sql.delete("deleteHotelsReservationRulesByAdmin", model.getObject().getUsers_id());
										deleted += sql.delete("deleteHotelsRoomTypesByAdmin", model.getObject().getUsers_id());
										deleted += sql.delete("deleteHotelsServicesInRoomsByAdmin", model.getObject().getUsers_id());
										deleted += sql.delete("deleteHotelsUploadedFilesByAdmin", model.getObject().getUsers_id());
										deleted += sql.delete("deleteMealOptionsByAdmin", model.getObject().getUsers_id());
										deleted += sql.delete("deleteRatePlaneDetailsHistoryByAdmin", model.getObject().getUsers_id());
										deleted += sql.delete("deleteRatePlaneDetailsByAdmin", model.getObject().getUsers_id());
										deleted += sql.delete("deleteRatePlaneSeasonsHistoryByAdmin", model.getObject().getUsers_id());
										deleted += sql.delete("deleteRatePlaneSeasonsByAdmin", model.getObject().getUsers_id());
										deleted += sql.delete("deleteRatePlanesHistoryByAdmin", model.getObject().getUsers_id());
										deleted += sql.delete("deleteRatePlanesByAdmin", model.getObject().getUsers_id());
										deleted += sql.delete("deleteGuestsByAdmin", model.getObject().getUsers_id());
										deleted += sql.delete("deleteRoomsByAdmin", model.getObject().getUsers_id());
										deleted += sql.delete("deleteRoomsEquipmentsByAdmin", model.getObject().getUsers_id());
										deleted += sql.delete("deleteHotelsB2CPricesByAdmin", model.getObject().getUsers_id());
										deleted += sql.delete("deleteSalePlanesHistoryByAdmin", model.getObject().getUsers_id());
										deleted += sql.delete("deleteSalePlanesByAdmin", model.getObject().getUsers_id());
										deleted += sql.delete("deleteHotelsByAdmin", model.getObject().getUsers_id());
										deleted += sql.delete("deleteAccountsByAdmin", model.getObject().getUsers_id());
										deleted += sql.delete("deleteContractsByAdmin", model.getObject().getUsers_id());
										deleted += sql.delete("deleteUsersByAdmin", model.getObject().getUsers_id());

										sql.commit();

										feedback.success("Hotel '" + model.getObject().getDisplay_name() + "' successfully deleted!");
										logger.info("Hotel '" + model.getObject().getDisplay_name() + "' successfully deleted! (Deleted items: "
												+ deleted + ") Initiator: " + getMySession().getUser().getLogin());
									} catch (Exception e) {
										feedback.error("Error, Hotel '" + model.getObject().getDisplay_name() + "' was not deleted!");
										logger.error("Hotel '" + model.getObject().getDisplay_name() + "' was not deleted! Initiator: "
												+ getMySession().getUser().getLogin());
										logger.error("Exception", e);
									} finally {
										sql.close();

										target.add(feedback);
										target.add(container);
									}
								}
							}

							@Override
							public boolean isVisible() {
								return getMySession().getUser().getLogin().equalsIgnoreCase("admin")
										&& model.getObject().getReservations_count() == 0;
							}

							@Override
							protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
								super.updateAjaxAttributes(attributes);
								IAjaxCallListener listener = new AjaxCallListener() {
									private static final long serialVersionUID = 1L;

									@Override
									public CharSequence getPrecondition(Component component) {
										return "return confirm(\"Are you sure to delete permanently hotel '" + model.getObject().getDisplay_name()
												+ "'?\");";
									}
								};
								attributes.getAjaxCallListeners().add(listener);
							}
						};
					}

					@Override
					public Link<Hotel> addUserListLink(final IModel<Hotel> model) {
						return new Link<Hotel>("userList") {
							private static final long serialVersionUID = 1L;

							public void onClick() {
								activate(new IBreadCrumbPanelFactory() {
									private static final long serialVersionUID = 1L;

									@Override
									public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
										User user = new User();
										user.setWork(new IdLongAndName(model.getObject().getUsers_id()));
										user.setType(new IdAndValue((int) User.TYPE_HOTEL_USER));
										return new ListUsersPanel(componentId, breadCrumbModel, user, model.getObject().getLegal_name());
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
					public Link<Hotel> viewRates(final IModel<Hotel> model) {
						return new Link<Hotel>("viewRates") {
							private static final long serialVersionUID = 1L;

							@Override
							public void onClick() {
								activate(new IBreadCrumbPanelFactory() {
									private static final long serialVersionUID = 1L;

									@Override
									public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
										User user = new User();
										user.setWork(new IdLongAndName(model.getObject().getUsers_id()));
										user.setType(new IdAndValue((int) User.TYPE_HOTEL_USER));
							
										return new ListRatePlanePanel("panel", breadCrumbModel, model.getObject());
									}
								});
							}
						};
					}
					@Override
					public Link<Hotel> viewSales(final IModel<Hotel> model) {
						return new Link<Hotel>("viewSales") {
							private static final long serialVersionUID = 1L;
							
							@Override
							public void onClick() {
								activate(new IBreadCrumbPanelFactory() {
									private static final long serialVersionUID = 1L;

									@Override
									public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
										User user = new User();
										user.setWork(new IdLongAndName(model.getObject().getUsers_id()));
										user.setType(new IdAndValue((int) User.TYPE_HOTEL_USER));
							
										return new ListAvailabilityRoom("panel", breadCrumbModel, model.getObject());
									}
								});
							}
						};
					}
					@Override
					public Link<Hotel> makeReservation(final IModel<Hotel> model) {
						return new Link<Hotel>("makeReservation") {
							private static final long serialVersionUID = 1L;
							
							@Override
							public void onClick() {
								activate(new IBreadCrumbPanelFactory() {
									private static final long serialVersionUID = 1L;
									
									@Override
									public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
										return new NewBookingPanel("panel", breadCrumbModel);
									}
								});
							}

							@Override
							public boolean isVisible() {
								return false;//model.getObject().getHotelstatus().getId().byteValue() == User.STATUS_ACTIVE;
							}
						};
					}
				});
			}
		});

		SortableHotelsDataProvider provider = new SortableHotelsDataProvider();
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

		container.add(form);
	}

	@Override
	public IModel<String> getTitle() {
		return new StringResourceModel("hotels.list", null);
	}

	@Override
	public Class<?> implementedClass() {
		return this.getClass();
	}
}
