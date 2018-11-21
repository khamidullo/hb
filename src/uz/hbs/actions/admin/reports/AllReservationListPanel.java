package uz.hbs.actions.admin.reports;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.attributes.IAjaxCallListener;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
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
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.util.string.Strings;

import uz.hbs.MyWebApplication;
import uz.hbs.actions.hotel.reservations.panels.ReservationActionPanel;
import uz.hbs.actions.hotel.reservations.panels.TourAgentDetailPanel;
import uz.hbs.actions.hotel.reservations.panels.ViewDetailsPanel;
import uz.hbs.actions.touragent.reports.ReservationLogPanel;
import uz.hbs.beans.City;
import uz.hbs.beans.IdLongAndName;
import uz.hbs.beans.ReservationDetail;
import uz.hbs.beans.ReservationStatus;
import uz.hbs.beans.ReservationType;
import uz.hbs.beans.User;
import uz.hbs.beans.filters.AllReservationFilter;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.db.dataproviders.SortableAllReservationDataProvider;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;
import uz.hbs.markup.html.form.SelectCityField;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;
import uz.hbs.markup.html.form.label.AjaxLinkLabel;
import uz.hbs.markup.html.form.textfield.DatePropertyColumn;
import uz.hbs.markup.html.form.textfield.MyDatePicker;
import uz.hbs.markup.navigation.BootstrapPagingNavigator.Size;
import uz.hbs.markup.navigation.BootstrapPagingNavigatorToolbar;
import uz.hbs.markup.navigation.repeater.data.table.MyNoRecordsToolbar;
import uz.hbs.markup.navigation.repeater.data.table.filter.MyChoiceFilteredPropertyColumn;
import uz.hbs.markup.navigation.repeater.data.table.filter.MyGoAndClearFilter;
import uz.hbs.session.MySession;
import uz.hbs.utils.CommonUtil;
import uz.hbs.utils.FormatUtil;
import uz.hbs.utils.HotelUtil;
import uz.hbs.utils.email.ReservationEmailNotifier;

public class AllReservationListPanel extends MyBreadCrumbPanel {
	private static final long serialVersionUID = 1L;
	private MyFeedbackPanel feedback;
	private ModalWindow dialog;
	private HashMap<String, Integer> total = new HashMap<String, Integer>();
	private boolean isAPI;

	public AllReservationListPanel(String id, IBreadCrumbModel breadCrumbModel, boolean isAPI) {
		super(id, breadCrumbModel);
		this.isAPI = isAPI;

		final AllReservationFilter filter = new AllReservationFilter();
		filter.setUser_type(isAPI ? User.TYPE_API : User.TYPE_TOURAGENT_USER);

		add(dialog = new ModalWindow("dialog"));
		dialog.setCloseButtonCallback(new ModalWindow.CloseButtonCallback() {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean onCloseButtonClicked(AjaxRequestTarget target) {
				return true;
			}
		});

		add(feedback = new MyFeedbackPanel("feedback"));
		feedback.setOutputMarkupId(true);
		final Form<AllReservationFilter> searchForm = new SearchForm("form", new CompoundPropertyModel<AllReservationFilter>(filter));
		searchForm.setOutputMarkupId(true);
		add(searchForm);

		WebMarkupContainer containerInfo;
		add(containerInfo = new WebMarkupContainer("container_info"));
		containerInfo.setOutputMarkupId(true);
		containerInfo.add(new Label("reserved", new AbstractReadOnlyModel<Integer>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Integer getObject() {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("datefrom", filter.getCreated_from());
				map.put("dateto", filter.getCreated_to());
				map.put("check_type", 1);
				map.put("user_type", filter.getUser_type());
				total.put("RESERVED", CommonUtil.nvl((Integer) new MyBatisHelper().selectOne("selectAllReservationManageCount", map)));
				return total.get("RESERVED");
			}
		}));
		containerInfo.add(new Label("registred", new AbstractReadOnlyModel<Integer>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Integer getObject() {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("datefrom", filter.getCreated_from());
				map.put("dateto", filter.getCreated_to());
				map.put("check_type", 2);
				map.put("user_type", filter.getUser_type());
				total.put("REGISTRED", CommonUtil.nvl((Integer) new MyBatisHelper().selectOne("selectAllReservationManageCount", map)));
				return total.get("REGISTRED");
			}
		}));
		containerInfo.add(new Label("no_show", new AbstractReadOnlyModel<Integer>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Integer getObject() {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("datefrom", filter.getCreated_from());
				map.put("dateto", filter.getCreated_to());
				map.put("check_type", 3);
				map.put("user_type", filter.getUser_type());
				total.put("NO_SHOW", CommonUtil.nvl((Integer) new MyBatisHelper().selectOne("selectAllReservationManageCount", map)));
				return total.get("NO_SHOW");
			}
		}));
		containerInfo.add(new Label("cancelled", new AbstractReadOnlyModel<Integer>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Integer getObject() {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("datefrom", filter.getCreated_from());
				map.put("dateto", filter.getCreated_to());
				map.put("check_type", 4);
				map.put("user_type", filter.getUser_type());
				total.put("CANCELLED", CommonUtil.nvl((Integer) new MyBatisHelper().selectOne("selectAllReservationManageCount", map)));
				return total.get("CANCELLED");
			}
		}));
		containerInfo.add(new Label("definite", new AbstractReadOnlyModel<Integer>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Integer getObject() {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("datefrom", filter.getCreated_from());
				map.put("dateto", filter.getCreated_to());
				map.put("check_type", 5);
				map.put("user_type", filter.getUser_type());
				//				total.put("DEFINITE", CommonUtil.nvl((Integer) new MyBatisHelper().selectOne("selectAllReservationManageCount", map)));
				return CommonUtil.nvl((Integer) new MyBatisHelper().selectOne("selectAllReservationManageCount", map));//total.get("DEFINITE");
			}
		}));
		containerInfo.add(new Label("tentative", new AbstractReadOnlyModel<Integer>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Integer getObject() {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("datefrom", filter.getCreated_from());
				map.put("dateto", filter.getCreated_to());
				map.put("check_type", 6);
				map.put("user_type", filter.getUser_type());
				//				total.put("TENTATIVE", CommonUtil.nvl((Integer) new MyBatisHelper().selectOne("selectAllReservationManageCount", map)));
				return CommonUtil.nvl((Integer) new MyBatisHelper().selectOne("selectAllReservationManageCount", map)); //total.get("TENTATIVE");
			}
		}));
		containerInfo.add(new Label("total", new AbstractReadOnlyModel<Integer>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Integer getObject() {
				Iterator<Integer> it = total.values().iterator();
				int totalValue = 0;
				while (it.hasNext())
					totalValue += CommonUtil.nvl(it.next());

				return totalValue;
			}
		}));

		final WebMarkupContainer container;
		add(container = new WebMarkupContainer("container"));
		container.setOutputMarkupId(true);

		final IModel<List<? extends ReservationType>> reservationTypeModel = new LoadableDetachableModel<List<? extends ReservationType>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<ReservationType> load() {
				return Arrays.asList(new ReservationType(ReservationType.DEFINITE, getString("hotels.reservation.details.reservation.type.definite")),
						new ReservationType(ReservationType.TENTATIVE, getString("hotels.reservation.details.reservation.type.tentative")));
			}
		};

		final IModel<List<? extends ReservationStatus>> reservationStatusModel = new LoadableDetachableModel<List<? extends ReservationStatus>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<ReservationStatus> load() {
				return Arrays.asList(
						new ReservationStatus(ReservationStatus.RESERVED, getString("hotels.reservation.details.reservation.status.reserved")),
						new ReservationStatus(ReservationStatus.CHECKED_IN, getString("hotels.reservation.details.reservation.status.checked.in")),
						new ReservationStatus(ReservationStatus.NO_SHOW, getString("hotels.reservation.details.reservation.status.no_show")),
						new ReservationStatus(ReservationStatus.CANCELLED, getString("hotels.reservation.details.reservation.status.cancelled")),
						new ReservationStatus(ReservationStatus.CHECKED_OUT, getString("hotels.reservation.details.reservation.status.checked.out")));
			}
		};

		final SortableAllReservationDataProvider provider = new SortableAllReservationDataProvider();
		provider.setFilterState(filter);

		ArrayList<IColumn<ReservationDetail, String>> columns = new ArrayList<IColumn<ReservationDetail, String>>();
		columns.add(new PropertyColumn<ReservationDetail, String>(new StringResourceModel("hotels.reservation.details.reservation.id", null), "id",
				"id") {
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<ReservationDetail>> item, String componentId, final IModel<ReservationDetail> rowModel) {
				item.add(new AjaxLinkLabel(componentId, ("<i class=\"fa fa-" + (rowModel.getObject().isIs_group() ? "group" : "user") + "\"></i> ")
						+ String.valueOf(rowModel.getObject().getId())) {
					private static final long serialVersionUID = 1L;

					@Override
					protected void onLinkClick(AjaxRequestTarget target) {
						dialog.setMinimalWidth(800);
						dialog.setMinimalHeight(600);
						dialog.setAutoSize(true);
						dialog.setTitle(
								new StringResourceModel("hotels.reservation.details.title", new Model<ReservationDetail>(rowModel.getObject())));
						dialog.setContent(new ViewDetailsPanel(dialog.getContentId(), null, rowModel));
						dialog.show(target);
					}

					@Override
					protected boolean isLinkEnabled() {
						return true;
					}
				});
				item.add(new AttributeModifier("style", "white-space: nowrap"));
			}
		});
		columns.add(new DatePropertyColumn<ReservationDetail, String>(new StringResourceModel("hotels.reservation.details.create_date", null),
				"create_date", "create_date") {
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<ReservationDetail>> item, String componentId, IModel<ReservationDetail> rowModel) {
				item.add(new Label(componentId, FormatUtil.toString(rowModel.getObject().getCreate_date(), MyWebApplication.DATE_TIME_FORMAT)))
						.add(new AttributeModifier("style", "white-space: nowrap"));
			}

		});
		columns.add(
				new PropertyColumn<ReservationDetail, String>(new StringResourceModel("hotels.reservation.details.tour_agent", null), "tour_agent") {
					private static final long serialVersionUID = 1L;

					@Override
					public void populateItem(Item<ICellPopulator<ReservationDetail>> item, String componentId,
							final IModel<ReservationDetail> rowModel) {
						item.add(new AjaxLinkLabel(componentId, rowModel.getObject().getTour_agent().getName()) {
							private static final long serialVersionUID = 1L;

							@Override
							protected void onLinkClick(AjaxRequestTarget target) {
								dialog.setMinimalWidth(600);
								dialog.setMinimalHeight(400);
								dialog.setTitle(
										new StringResourceModel("hotels.reservation.name", new Model<ReservationDetail>(rowModel.getObject())));
								dialog.setContent(
										new TourAgentDetailPanel(dialog.getContentId(), rowModel.getObject().getTour_agent().getUsers_id()));
								dialog.show(target);
							}

							@Override
							protected boolean isLinkEnabled() {
								return rowModel.getObject().getTour_agent().getUsers_id() > 0 && filter.getUser_type() == User.TYPE_TOURAGENT_USER;
							}
						}).add(new AttributeModifier("style", "white-space: nowrap"));
					}
				});
		columns.add(new PropertyColumn<ReservationDetail, String>(new StringResourceModel("touragents.newbooking.hotel", null), "hotelsusers_id") {
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<ReservationDetail>> item, String componentId, final IModel<ReservationDetail> rowModel) {
				item.add(new Label(componentId, new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject() {
						return new MyBatisHelper().selectOne("selectHotelAndCityName", rowModel.getObject().getHotelsusers_id());
					}
				}));
			}
		});
		columns.add(new PropertyColumn<ReservationDetail, String>(new StringResourceModel("hotels.reservation.details.creator", null),
				"creator_user_id") {
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<ReservationDetail>> item, String componentId, final IModel<ReservationDetail> rowModel) {
				item.add(new Label(componentId, new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject() {
						return rowModel.getObject().getCreator();
					}
				}));
			}
		});
		columns.add(new DatePropertyColumn<ReservationDetail, String>(new StringResourceModel("hotels.reservation.details.check_in", null),
				"check_in", "check_in") {
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<ReservationDetail>> item, String componentId, IModel<ReservationDetail> rowModel) {
				item.add(new Label(componentId, FormatUtil.toString(rowModel.getObject().getCheck_in(), MyWebApplication.DATE_TIME_SHORT_FORMAT)))
						.add(new AttributeModifier("style", "white-space: nowrap"));
			}
		});
		columns.add(new DatePropertyColumn<ReservationDetail, String>(new StringResourceModel("hotels.reservation.details.check_out", null),
				"check_out", "check_out") {
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<ReservationDetail>> item, String componentId, IModel<ReservationDetail> rowModel) {
				item.add(new Label(componentId, FormatUtil.toString(rowModel.getObject().getCheck_out(), MyWebApplication.DATE_TIME_SHORT_FORMAT)))
						.add(new AttributeModifier("style", "white-space: nowrap"));
			}
		});
		columns.add(new MyChoiceFilteredPropertyColumn<ReservationDetail, ReservationType, String>(
				new StringResourceModel("hotels.reservation.details.reservation.type", null), "reservation_type", reservationTypeModel) {
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<ReservationDetail>> item, String componentId, final IModel<ReservationDetail> rowModel) {
				item.add(new Label(componentId, new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject() {
						switch (rowModel.getObject().getReservation_type()) {
							case ReservationType.TENTATIVE:
								return getString("hotels.reservation.details.reservation.type.tentative");
							case ReservationType.DEFINITE:
								return getString("hotels.reservation.details.reservation.type.definite");
							default:
								return getString("unkhown");
						}
					}
				}));
			}
		});
		columns.add(new MyChoiceFilteredPropertyColumn<ReservationDetail, ReservationStatus, String>(
				new StringResourceModel("hotels.reservation.details.reservation.status", null), "reservation_status", reservationStatusModel) {
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<ReservationDetail>> item, String componentId, final IModel<ReservationDetail> rowModel) {
				item.add(new Label(componentId, new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject() {
						switch (rowModel.getObject().getStatus().getId()) {
							case ReservationStatus.RESERVED:
								return getString("hotels.reservation.details.reservation.status.reserved");
							case ReservationStatus.CHECKED_IN:
								return getString("hotels.reservation.details.reservation.status.checked.in");
							case ReservationStatus.NO_SHOW:
								return getString("hotels.reservation.details.reservation.status.no_show");
							case ReservationStatus.CANCELLED:
								return getString("hotels.reservation.details.reservation.status.cancelled");
							case ReservationStatus.CHECKED_OUT:
								return getString("hotels.reservation.details.reservation.status.checked.out");
							default:
								return getString("unkhown");
						}
					}
				}));
			}
		});
		columns.add(
				new PropertyColumn<ReservationDetail, String>(new StringResourceModel("hotels.reservation.details.total", null), "total", "total"));
		columns.add(new FilteredAbstractColumn<ReservationDetail, String>(new StringResourceModel("users.operation", null)) {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getFilter(String componentId, final FilterForm<?> form) {
				return new MyGoAndClearFilter(componentId, form) {
					private static final long serialVersionUID = 1L;

					@Override
					protected void onClearSubmit(Button button) {
						super.onClearSubmit(button);
						provider.getFilterState().setCreated_from(filter.getCreated_from());
						provider.getFilterState().setCreated_to(filter.getCreated_to());
					}

					@Override
					protected void onGoSubmit(Button button) {
						super.onGoSubmit(button);
						provider.getFilterState().setCreated_from(filter.getCreated_from());
						provider.getFilterState().setCreated_to(filter.getCreated_to());
					}
				};
			}

			@Override
			public void populateItem(Item<ICellPopulator<ReservationDetail>> cellItem, String componentId, final IModel<ReservationDetail> rowModel) {
				cellItem.add(new ReservationActionPanel<ReservationDetail>(componentId, rowModel) {
					private static final long serialVersionUID = 1L;

					@Override
					protected void onViewDetails() {
					}

					@Override
					protected void onChangeRoom() {
					}

					@Override
					protected void onIssueBill() {
					}

					@Override
					protected void onRegisterGuest() {
					}

					@Override
					protected void onCancel(AjaxRequestTarget target) {
						try {
							if (HotelUtil.cancelReserve(rowModel.getObject().getId(), ((MySession) getSession()).getUser().getId())) {
								ReservationEmailNotifier.cancelReservation(rowModel.getObject().getId());
								feedback.success(getString("hotels.reservation.cancel.success"));
								target.add(container);
							} else {
								feedback.error(getString("hotels.reservation.cancel.fail"));
							}
						} finally {
							target.add(feedback);
						}
					}

					@Override
					protected boolean isVisibleViewDetails() {
						return false;
					}

					@Override
					protected boolean isVisibleChangeRoom() {
						return false;
					}

					@Override
					protected boolean isVisibleIssueBill() {
						return false;
					}

					@Override
					protected boolean isVisibleRegisterGuest() {
						return false;
					}

					@Override
					protected boolean isVisibleCancel() {
						return rowModel.getObject().getStatus().getId() == ReservationStatus.RESERVED;
					}

					@Override
					protected void onNoShow(AjaxRequestTarget target) {
					}

					@Override
					protected boolean isVisibleNoShow() {
						return false;
					}

					@Override
					protected void onViewReserveLogs() {
						activate(new IBreadCrumbPanelFactory() {
							private static final long serialVersionUID = 1L;

							@Override
							public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
								return new ReservationLogPanel(componentId, breadCrumbModel, rowModel.getObject().getId());
							}
						});
					}

					@Override
					protected boolean isVisibleViewReserveLogs() {
						return true;
					}
				});
			}
		});

		final DataTable<ReservationDetail, String> dataTable = new DataTable<ReservationDetail, String>("table", columns, provider,
				getMySession().getSettings().getTable_rows());
		dataTable.setOutputMarkupId(true);

		final FilterForm<AllReservationFilter> form = new FilterForm<AllReservationFilter>("filterForm", provider) {
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

		searchForm.add(new IndicatingAjaxButton("submit") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				super.onError(target, form);
				target.add(feedback);
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				provider.getFilterState().setCreated_from(filter.getCreated_from());
				provider.getFilterState().setCreated_to(filter.getCreated_to());
				target.add(container);
				target.add(containerInfo);
			}

			@Override
			protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
				super.updateAjaxAttributes(attributes);
				IAjaxCallListener listener = new AjaxCallListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public CharSequence getBeforeSendHandler(Component component) {
						return "this.disabled = true;";
					}

					@Override
					public CharSequence getCompleteHandler(Component component) {
						return "this.disabled = false;";
					}
				};
				attributes.getAjaxCallListeners().add(listener);
			}
		});
	}

	@Override
	public Class<?> implementedClass() {
		return this.getClass();
	}

	@Override
	public IModel<String> getTitle() {
		return new StringResourceModel(isAPI ? "hotels.reservation.pending.title.b2c" : "hotels.reservation.pending.title", null);
	}

	private class SearchForm extends Form<AllReservationFilter> {
		private static final long serialVersionUID = 1L;

		public SearchForm(String id, final IModel<AllReservationFilter> model) {
			super(id, model);
			model.getObject().setCreated_from(new Date());
			Date dateTo = new MyBatisHelper().selectOne("selectCustomDate", 30);
			model.getObject().setCreated_to(dateTo);
			DateTextField date_from;
			add(date_from = new DateTextField("created_from", new PatternDateConverter(MyWebApplication.DATE_FORMAT, false)));
			date_from.add(new MyDatePicker());
			DateTextField date_to;
			add(date_to = new DateTextField("created_to", new PatternDateConverter(MyWebApplication.DATE_FORMAT, false)));
			date_to.add(new MyDatePicker());
			DateTextField checkin_from;
			add(checkin_from = new DateTextField("checkin_from", new PatternDateConverter(MyWebApplication.DATE_FORMAT, false)));
			checkin_from.add(new MyDatePicker());
			DateTextField checkin_to;
			add(checkin_to = new DateTextField("checkin_to", new PatternDateConverter(MyWebApplication.DATE_FORMAT, false)));
			checkin_to.add(new MyDatePicker());
			DateTextField checkout_from;
			add(checkout_from = new DateTextField("checkout_from", new PatternDateConverter(MyWebApplication.DATE_FORMAT, false)));
			checkout_from.add(new MyDatePicker());
			DateTextField checkout_to;
			add(checkout_to = new DateTextField("checkout_to", new PatternDateConverter(MyWebApplication.DATE_FORMAT, false)));
			checkout_to.add(new MyDatePicker());
			add(new TextField<String>("last_name"));
			final DropDownChoice<IdLongAndName> touragentfield;
			add(touragentfield = new DropDownChoice<IdLongAndName>("tourAgent", new LoadableDetachableModel<List<IdLongAndName>>() {
				private static final long serialVersionUID = 1L;

				@Override
				protected List<IdLongAndName> load() {
					return new MyBatisHelper().selectList("selectAllTourAgentList");
				}
			}, new ChoiceRenderer<IdLongAndName>("name", "id")));
			touragentfield.setVisible(!isAPI);
			//			final WebMarkupContainer addt_serv_container;
			//			add(addt_serv_container = new WebMarkupContainer("addt_serv_container"));
			//			addt_serv_container.setOutputMarkupId(true);
			//			addt_serv_container.add(new Link<Void>("addt_serv"){
			//				private static final long serialVersionUID = 1L;
			//
			//				@Override
			//				public void onClick() {
			//					activate(new IBreadCrumbPanelFactory() {
			//						private static final long serialVersionUID = 1L;
			//
			//						@Override
			//						public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
			//							return new AdditionalServiceOrderListPanel(componentId, breadCrumbModel, new AdditionalServiceOrderFilter(model.getObject().getTourAgent().getId(), true));
			//						}
			//					});
			//				}
			//				
			//				@Override
			//				public boolean isVisible() {
			//					return model.getObject().getTourAgent() != null;
			//				}
			//			});
			touragentfield.add(new AjaxFormComponentUpdatingBehavior("change") {
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					if (!Strings.isEmpty(touragentfield.getValue()))
						model.getObject().setTourAgent(new IdLongAndName(Long.parseLong(touragentfield.getValue())));
					else
						model.getObject().setTourAgent(null);
					//					target.add(addt_serv_container);
				}
			});

			final SelectCityField cityField;
			add(cityField = new SelectCityField("city"));
			final DropDownChoice<IdLongAndName> hotelField;
			add(hotelField = new DropDownChoice<IdLongAndName>("hotel", new LoadableDetachableModel<List<IdLongAndName>>() {
				private static final long serialVersionUID = 1L;

				@Override
				protected List<IdLongAndName> load() {
					return new MyBatisHelper().selectList("selectAllHotelList", model.getObject());
				}
			}, new ChoiceRenderer<IdLongAndName>("name", "id")));
			hotelField.setOutputMarkupId(true);

			cityField.getDropDownChoice().add(new AjaxFormComponentUpdatingBehavior("change") {
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					if (!Strings.isEmpty(cityField.getDropDownChoice().getValue())) {
						model.getObject().setCity(new City(Integer.parseInt(cityField.getDropDownChoice().getValue())));
					} else {
						model.getObject().setCity(null);
						model.getObject().setHotel(null);
					}
					target.add(hotelField);
				}
			});
			add(new TextField<String>("reservations_id"));
		}
	}
}
