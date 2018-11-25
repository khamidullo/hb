package uz.hbs.actions.admin.reports;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.attributes.IAjaxCallListener;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
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
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.string.Strings;

import uz.hbs.MyWebApplication;
import uz.hbs.actions.hotel.reservations.panels.ReservationActionPanel;
import uz.hbs.actions.touragent.reports.ReservationLogPanel;
import uz.hbs.beans.City;
import uz.hbs.beans.IdLongAndName;
import uz.hbs.beans.ReservationDetail;
import uz.hbs.beans.ReservationStatus;
import uz.hbs.beans.ReservationType;
import uz.hbs.beans.User;
import uz.hbs.beans.filters.AllReservationFilter;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.db.dataproviders.SortableReportByReservationForAdminDataProvider;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;
import uz.hbs.markup.html.form.SelectCityField;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;
import uz.hbs.markup.html.form.textfield.DatePropertyColumn;
import uz.hbs.markup.html.form.textfield.MyDatePicker;
import uz.hbs.markup.navigation.BootstrapPagingNavigator.Size;
import uz.hbs.markup.navigation.BootstrapPagingNavigatorToolbar;
import uz.hbs.markup.navigation.repeater.data.table.MyNoRecordsToolbar;
import uz.hbs.markup.navigation.repeater.data.table.filter.MyChoiceFilteredPropertyColumn;
import uz.hbs.markup.navigation.repeater.data.table.filter.MyGoAndClearFilter;
import uz.hbs.session.MySession;
import uz.hbs.utils.AJAXDownload;
import uz.hbs.utils.FormatUtil;
import uz.hbs.utils.HotelUtil;
import uz.hbs.utils.ReserveUtil;
import uz.hbs.utils.email.ReservationEmailNotifier;

public class ReportB2CByReservationsPanel extends MyBreadCrumbPanel {
	private static final long serialVersionUID = 1L;
	private MyFeedbackPanel feedback;
	private ModalWindow dialog;

	public ReportB2CByReservationsPanel(String id, IBreadCrumbModel breadCrumbModel) {
		super(id, breadCrumbModel);

		final AllReservationFilter filter = new AllReservationFilter();
		filter.setUser_type(User.TYPE_API);

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

		final SortableReportByReservationForAdminDataProvider provider = new SortableReportByReservationForAdminDataProvider();
		provider.setFilterState(filter);

		ArrayList<IColumn<ReservationDetail, String>> columns = new ArrayList<IColumn<ReservationDetail, String>>();
		columns.add(new PropertyColumn<ReservationDetail, String>(new StringResourceModel("hotels.reservation.details.reservation.id", null), "id",
				"id") {
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<ReservationDetail>> item, String componentId, final IModel<ReservationDetail> rowModel) {
				/*
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
				*/
				item.add(new IdLabelPanel(componentId, dialog, rowModel.getObject()));
				item.add(new AttributeModifier("style", "white-space: nowrap"));
			}
		});
		columns.add(new DatePropertyColumn<ReservationDetail, String>(new StringResourceModel("hotels.reservation.details.create_date", null),
				"create_date", "create_date") {
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<ReservationDetail>> item, String componentId, IModel<ReservationDetail> rowModel) {
				item.add(new Label(componentId, FormatUtil.toString(rowModel.getObject().getCreate_date(), MyWebApplication.DATE_TIME_FORMAT)));
			}

		});
		/*
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
								dialog.setTitle(new StringResourceModel("hotels.reservation.details.tour_agent", null));
								dialog.setContent(
										new TourAgentDetailPanel(dialog.getContentId(), rowModel.getObject().getTour_agent().getUsers_id()));
								dialog.show(target);
							}

							@Override
							protected boolean isLinkEnabled() {
								return rowModel.getObject().getTour_agent().getUsers_id() > 0 && filter.getUser_type() == User.TYPE_TOURAGENT_USER;
							}
						});
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
		*/
		columns.add(new PropertyColumn<ReservationDetail, String>(new StringResourceModel("touragents.newbooking.hotel", null), "hd.display_name",
				"hotel_name"));
		columns.add(new PropertyColumn<ReservationDetail, String>(new StringResourceModel("hotels.legal.name", null), "hu.name", "hotel_legal_name"));
		columns.add(new PropertyColumn<ReservationDetail, String>(new StringResourceModel("touragents.newbooking.guest", null), "guest_name"));
		columns.add(new PropertyColumn<ReservationDetail, String>(
				new StringResourceModel("hotels.reservation.report.reservation.number_of_guests", null), "guest_count"));
		columns.add(new DatePropertyColumn<ReservationDetail, String>(new StringResourceModel("hotels.reservation.details.check_in", null),
				"check_in", "check_in") {
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<ReservationDetail>> item, String componentId, IModel<ReservationDetail> rowModel) {
				item.add(new Label(componentId, FormatUtil.toString(rowModel.getObject().getCheck_in(), MyWebApplication.DATE_TIME_SHORT_FORMAT)));
			}
		});
		columns.add(new DatePropertyColumn<ReservationDetail, String>(new StringResourceModel("hotels.reservation.details.check_out", null),
				"check_out", "check_out") {
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<ReservationDetail>> item, String componentId, IModel<ReservationDetail> rowModel) {
				item.add(new Label(componentId, FormatUtil.toString(rowModel.getObject().getCheck_out(), MyWebApplication.DATE_TIME_SHORT_FORMAT)));
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
		/*
		columns.add(new PropertyColumn<ReservationDetail, String>(new StringResourceModel("touragents.newbooking.payment_type", null),
				"r.payment_owner", "payment_owner") {
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<ReservationDetail>> item, String componentId, IModel<ReservationDetail> rowModel) {
				item.add(new Label(componentId, rowModel.getObject().isPayment_owner() ? "Selfpayment" : "Bank transfer"));
			}
		});
		*/
		columns.add(new PropertyColumn<ReservationDetail, String>(new StringResourceModel("touragents.reservation.payment_method", null),
				"r.payment_method", "payment_method"){
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<ReservationDetail>> item, String componentId, IModel<ReservationDetail> rowModel) {
				item.add(new Label(componentId, new StringResourceModel("reservation.payment_method." + (rowModel.getObject().isPayment_owner() ? "SELF_PAYMENT" : rowModel.getObject().getPayment_method()), null).getString()));
			}
		});
		columns.add(new PropertyColumn<ReservationDetail, String>(new StringResourceModel("touragents.reservation.payment_confirmation", null),
				"r.payment_confirmation", "payment_confirmation") {
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<ReservationDetail>> item, String componentId, IModel<ReservationDetail> rowModel) {
				item.add(new Label(componentId, new StringResourceModel(rowModel.getObject().getPayment_confirmation() ? "yes" : "no", null)));
			}
		});
		columns.add(new MyChoiceFilteredPropertyColumn<ReservationDetail, ReservationStatus, String>(
				new StringResourceModel("hotels.reservation.details.reservation.status", null), "reservation_status", reservationStatusModel) {
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<ReservationDetail>> item, String componentId, final IModel<ReservationDetail> rowModel) {
				item.add(new Label(componentId, ReserveUtil.getReservationStatusText(rowModel.getObject().getStatus().getId())));
			}
		});
		columns.add(
				new PropertyColumn<ReservationDetail, String>(new StringResourceModel("hotels.reservation.details.total", null), "r.total", "total"));
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

		final AJAXDownload download = new AJAXDownload() {
			private static final long serialVersionUID = 1L;

			@Override
			protected IResourceStream getResourceStream() {
				IResourceStream resourceStream = new AbstractResourceStreamWriter() {
					private static final long serialVersionUID = 1L;

					@SuppressWarnings("resource")
					@Override
					public void write(OutputStream os) throws IOException {
						SXSSFWorkbook wb = new SXSSFWorkbook(100);
						wb.setCompressTempFiles(true);
						try {
							CellStyle headerStyle, titleStyle, moneyStyle, countStyle, simpleStyle, datetimeshortStyle, datetimeStyle;

							headerStyle = wb.createCellStyle();
							titleStyle = wb.createCellStyle();
							moneyStyle = wb.createCellStyle();
							countStyle = wb.createCellStyle();
							simpleStyle = wb.createCellStyle();
							datetimeshortStyle = wb.createCellStyle();
							datetimeStyle = wb.createCellStyle();
							titleStyle.setAlignment(HorizontalAlignment.CENTER);

							Font font = wb.createFont();
							font.setFontHeightInPoints((short) 12);
							font.setColor((short) 0);
							font.setBold(false);

							Font boldfont = wb.createFont();
							boldfont.setFontHeightInPoints((short) 12);
							boldfont.setColor((short) 0);
							boldfont.setBold(true);

							Font titlefont = wb.createFont();
							titlefont.setFontHeightInPoints((short) 14);
							titlefont.setColor((short) 0);
							titlefont.setBold(true);

							headerStyle.setFont(boldfont);
							titleStyle.setFont(font);
							moneyStyle.setFont(font);
							countStyle.setFont(font);
							datetimeshortStyle.setFont(font);
							datetimeStyle.setFont(font);
							simpleStyle.setFont(font);

							DataFormat format = wb.createDataFormat();
							moneyStyle.setDataFormat(format.getFormat("###0.00"));
							countStyle.setDataFormat(format.getFormat("###0"));
							datetimeshortStyle.setDataFormat(format.getFormat(MyWebApplication.DATE_TIME_SHORT_FORMAT));
							datetimeStyle.setDataFormat(format.getFormat(MyWebApplication.DATE_TIME_FORMAT));

							headerStyle.setBorderBottom(BorderStyle.THIN);
							headerStyle.setBorderTop(BorderStyle.THIN);
							headerStyle.setBorderLeft(BorderStyle.THIN);
							headerStyle.setBorderRight(BorderStyle.THIN);
							headerStyle.setAlignment(HorizontalAlignment.CENTER);
							headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
							headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
							headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
							headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
							headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
							headerStyle.setWrapText(true);
							headerStyle.setShrinkToFit(true);
							//							
							//							simpleStyle.setWrapText(true);
							//							simpleStyle.setShrinkToFit(true);
							//							titleStyle.setWrapText(true);
							//							titleStyle.setShrinkToFit(true);
							//							moneyStyle.setWrapText(true);
							//							moneyStyle.setShrinkToFit(true);
							//							countStyle.setWrapText(true);
							//							countStyle.setShrinkToFit(true);
							//							datetimeshortStyle.setWrapText(true);
							//							datetimeshortStyle.setShrinkToFit(true);
							//							datetimeStyle.setWrapText(true);
							//							datetimeStyle.setShrinkToFit(true);
							//							simpleStyle.setWrapText(true);
							//							simpleStyle.setShrinkToFit(true);

							Sheet s = wb.createSheet(getString("report.title.report_by_reservations.b2c"));
							int cols = 0;
							int rows = 0;

							s.setColumnWidth(cols++, 3000); //ID
							s.setColumnWidth(cols++, 5000); //Create date
							//s.setColumnWidth(cols++, 8000); //Touragent name
							//s.setColumnWidth(cols++, 8000); //Creator name
							s.setColumnWidth(cols++, 8000); //Hotel name
							s.setColumnWidth(cols++, 8000); //Hotel legal name
							s.setColumnWidth(cols++, 8000); //Guest name
							s.setColumnWidth(cols++, 3000); //Guest count
							s.setColumnWidth(cols++, 5000); //Check-in
							s.setColumnWidth(cols++, 5000); //Check-out
							s.setColumnWidth(cols++, 5000); //Reservation type
							//s.setColumnWidth(cols++, 4000); //Payment type
							s.setColumnWidth(cols++, 4000); //Payment method
							s.setColumnWidth(cols++, 4000); //Payment confirmation
							s.setColumnWidth(cols++, 4000); //Status
							s.setColumnWidth(cols++, 5000); //Total

							cols = 0;

							Row row = s.createRow(rows++);
							row.setHeight((short) 600);
							s.addMergedRegion(new CellRangeAddress(0, 0, 0, 12));

							Cell cell = row.createCell(cols++);
							cell.setCellStyle(headerStyle);
							cell.setCellValue(getString("report.title.report_by_reservations.b2c"));

							cols = 0;
							row = s.createRow(rows++);
							row.setHeight((short) 800);

							cell = row.createCell(cols++);
							cell.setCellStyle(headerStyle);
							cell.setCellValue(getString("hotels.reservation.details.reservation.id"));

							cell = row.createCell(cols++);
							cell.setCellStyle(headerStyle);
							cell.setCellValue(getString("hotels.reservation.details.create_date"));
							/*
							cell = row.createCell(cols++);
							cell.setCellStyle(headerStyle);
							cell.setCellValue(getString("hotels.reservation.details.tour_agent"));

							cell = row.createCell(cols++);
							cell.setCellStyle(headerStyle);
							cell.setCellValue(getString("hotels.reservation.details.creator"));
							*/
							cell = row.createCell(cols++);
							cell.setCellStyle(headerStyle);
							cell.setCellValue(getString("touragents.newbooking.hotel"));

							cell = row.createCell(cols++);
							cell.setCellStyle(headerStyle);
							cell.setCellValue(getString("hotels.legal.name"));

							cell = row.createCell(cols++);
							cell.setCellStyle(headerStyle);
							cell.setCellValue(getString("touragents.newbooking.guest"));

							cell = row.createCell(cols++);
							cell.setCellStyle(headerStyle);
							cell.setCellValue(getString("hotels.reservation.report.reservation.number_of_guests"));

							cell = row.createCell(cols++);
							cell.setCellStyle(headerStyle);
							cell.setCellValue(getString("hotels.reservation.details.check_in"));

							cell = row.createCell(cols++);
							cell.setCellStyle(headerStyle);
							cell.setCellValue(getString("hotels.reservation.details.check_out"));

							cell = row.createCell(cols++);
							cell.setCellStyle(headerStyle);
							cell.setCellValue(getString("hotels.reservation.details.reservation.type"));
							/*
							cell = row.createCell(cols++);
							cell.setCellStyle(headerStyle);
							cell.setCellValue(getString("touragents.newbooking.payment_type"));
							*/
							cell = row.createCell(cols++);
							cell.setCellStyle(headerStyle);
							cell.setCellValue(getString("touragents.reservation.payment_method"));

							cell = row.createCell(cols++);
							cell.setCellStyle(headerStyle);
							cell.setCellValue(getString("touragents.reservation.payment_confirmation"));

							cell = row.createCell(cols++);
							cell.setCellStyle(headerStyle);
							cell.setCellValue(getString("hotels.reservation.details.reservation.status"));

							cell = row.createCell(cols++);
							cell.setCellStyle(headerStyle);
							cell.setCellValue(getString("hotels.reservation.details.total"));

							Map<String, Serializable> param = new HashMap<String, Serializable>();
							param.put("group_name", filter.getGroup_name());
							param.put("first_name", filter.getFirst_name());
							param.put("last_name", filter.getLast_name());
							param.put("created_from", filter.getCreated_from());
							param.put("created_to", filter.getCreated_to());
							param.put("checkin_from", filter.getCheckin_from());
							param.put("checkin_to", filter.getCheckin_to());
							param.put("checkout_from", filter.getCheckout_from());
							param.put("checkout_to", filter.getCheckout_to());
							param.put("check_in", filter.getCheck_in());
							param.put("check_out", filter.getCheck_out());
							param.put("touragent", filter.getTourAgent());
							param.put("hotel", filter.getHotel());
							param.put("city", filter.getCity());
							param.put("reservation_type", filter.getReservation_type());
							param.put("reservation_status", filter.getReservation_status());
							param.put("reservation_id", filter.getReservations_id());
							param.put("user_type", filter.getUser_type());
							param.put("offset", 0);
							param.put("limit", Long.MAX_VALUE);

							List<ReservationDetail> list = new MyBatisHelper().selectList("selectReservationsReportListForAdmin", param);

							cols = 0;

							for (ReservationDetail report : list) {
								row = s.createRow(rows++);

								cell = row.createCell(cols++);
								cell.setCellStyle(simpleStyle);
								cell.setCellValue(report.getId());

								cell = row.createCell(cols++);
								cell.setCellStyle(datetimeStyle);
								cell.setCellValue(report.getCreate_date());
								/*
								cell = row.createCell(cols++);
								cell.setCellStyle(simpleStyle);
								cell.setCellValue(report.getTour_agent().getName());

								cell = row.createCell(cols++);
								cell.setCellStyle(simpleStyle);
								cell.setCellValue(report.getCreator());
								*/
								cell = row.createCell(cols++);
								cell.setCellStyle(simpleStyle);
								cell.setCellValue(report.getHotel_name());

								cell = row.createCell(cols++);
								cell.setCellStyle(simpleStyle);
								cell.setCellValue(report.getHotel_legal_name());

								cell = row.createCell(cols++);
								cell.setCellStyle(simpleStyle);
								cell.setCellValue(report.getGuest_name());

								cell = row.createCell(cols++);
								cell.setCellStyle(countStyle);
								cell.setCellValue(report.getGuest_count());

								cell = row.createCell(cols++);
								cell.setCellStyle(datetimeshortStyle);
								cell.setCellValue(report.getCheck_in());

								cell = row.createCell(cols++);
								cell.setCellStyle(datetimeshortStyle);
								cell.setCellValue(report.getCheck_out());

								cell = row.createCell(cols++);
								cell.setCellStyle(simpleStyle);
								cell.setCellValue(report.getReservation_type() == ReservationType.DEFINITE
										? getString("hotels.reservation.details.reservation.type.definite")
										: getString("hotels.reservation.details.reservation.type.tentative"));
								
								/*
								cell = row.createCell(cols++);
								cell.setCellStyle(simpleStyle);
								cell.setCellValue(report.getPayment_method());
								*/
								cell = row.createCell(cols++);
								cell.setCellStyle(simpleStyle);
								cell.setCellValue(new StringResourceModel("reservation.payment_method." + (report.isPayment_owner() ? "SELF_PAYMENT" : report.getPayment_method()), null).getString());

								cell = row.createCell(cols++);
								cell.setCellStyle(simpleStyle);
								cell.setCellValue(new StringResourceModel(report.getPayment_confirmation() ? "yes" : "no", null).getString());

								cell = row.createCell(cols++);
								cell.setCellStyle(simpleStyle);
								cell.setCellValue(ReserveUtil.getReservationStatusText(report.getStatus().getId()));

								cell = row.createCell(cols++);
								cell.setCellStyle(moneyStyle);
								cell.setCellValue(report.getTotal().doubleValue());

								cols = 0;
							}
						} finally {
							wb.write(os);
						}
					}
				};
				return resourceStream;
			}

			@Override
			protected String getFileName() {
				return "ReportByReservations_" + FormatUtil.toString(new Date(), "dd_MM_yyyy") + ".xlsx";
			}
		};
		add(download);
		add(new AjaxLink<Void>("export") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				download.initiate(target);
			}
		});
	}

	@Override
	public Class<?> implementedClass() {
		return getClass();
	}

	@Override
	public IModel<String> getTitle() {
		return new StringResourceModel("report.title.report_by_reservations.b2c", null);
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
			TextField<String> lastName = new TextField<String>("last_name");
			add(lastName);//Guest index=1 olingani uchun boshqa indexdagi guestlarni qidirish mantiqsiz bo'ladi
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
			add(new TextField<String>("reservation_id"));
		}
	}
}
