package uz.hbs.actions.api.reports;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
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
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.HeadersToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;
import org.apache.wicket.util.resource.IResourceStream;

import uz.hbs.MyWebApplication;
import uz.hbs.beans.Reservation;
import uz.hbs.beans.ReservationStatus;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.db.dataproviders.SortableTouragentReservationsDataProvider;
import uz.hbs.markup.navigation.BootstrapPagingNavigator.Size;
import uz.hbs.markup.navigation.BootstrapPagingNavigatorToolbar;
import uz.hbs.markup.navigation.repeater.data.table.MyNoRecordsToolbar;
import uz.hbs.session.MySession;
import uz.hbs.utils.AJAXDownload;
import uz.hbs.utils.DateUtil;
import uz.hbs.utils.FormatUtil;
import uz.hbs.utils.ReserveUtil;

public class ReservationsResultPanel extends Panel {
	private static final long serialVersionUID = 1L;
	protected ArrayList<IColumn<Reservation, String>> columns = new ArrayList<IColumn<Reservation, String>>();

	public ReservationsResultPanel(String id, final Reservation filter) {
		this(id,filter,false);
	}	
	
	public ReservationsResultPanel(String id, final Reservation filter, final boolean isreport) {
		super(id);

		// Filter model sifatida properties fayldagi '{}' property name bilan almashtirlishi kerak
		Label resultLabel = new Label("resultLabel", new StringResourceModel("touragents.reservation.result_label", new Model<Reservation>(filter)));
		add(resultLabel);

		columns.add(new PropertyColumn<Reservation, String>(new StringResourceModel("touragents.reservation.id", null), "r.id", "id"));
		columns.add(new PropertyColumn<Reservation, String>(new StringResourceModel("touragents.reservation.created", null), "r.create_date", "create_date") {
			private static final long serialVersionUID = 1L;
			@Override
			public void populateItem(Item<ICellPopulator<Reservation>> item, String componentId, IModel<Reservation> rowModel) {
				item.add(new Label(componentId, DateUtil.toString(rowModel.getObject().getCreate_date(), MyWebApplication.DATE_TIME_FORMAT)));
			}
		});
		columns.add(new PropertyColumn<Reservation, String>(new StringResourceModel("touragents.reservation.guest_name", null), "r.name", "guest_name"));
		columns.add(new PropertyColumn<Reservation, String>(new StringResourceModel("touragents.reservation.hotel_name", null), "h.display_name", "hotel_name"));
		columns.add(new PropertyColumn<Reservation, String>(new StringResourceModel("touragents.reservation.check_in", null), "r.check_in", "check_in"){
			private static final long serialVersionUID = 1L;
			@Override
			public void populateItem(Item<ICellPopulator<Reservation>> item, String componentId, IModel<Reservation> rowModel) {
				item.add(new Label(componentId, DateUtil.toString(rowModel.getObject().getCheck_in(), MyWebApplication.DATE_TIME_SHORT_FORMAT)));
			}
		});
		columns.add(new PropertyColumn<Reservation, String>(new StringResourceModel("touragents.reservation.check_out", null), "r.check_out", "check_out"){
			private static final long serialVersionUID = 1L;
			@Override
			public void populateItem(Item<ICellPopulator<Reservation>> item, String componentId, IModel<Reservation> rowModel) {
				item.add(new Label(componentId, DateUtil.toString(rowModel.getObject().getCheck_out(), MyWebApplication.DATE_TIME_SHORT_FORMAT)));
			}
		});
		columns.add(new PropertyColumn<Reservation, String>(new StringResourceModel("touragents.reservation.status", null), "r.status", "status") {
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<Reservation>> item, String componentId, final IModel<Reservation> rowModel) {
				item.add(new Label(componentId, new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject() {
						return ReserveUtil.getReservationStatusText(rowModel.getObject().getStatus());
					}
				}));
			}
		});
		columns.add(new PropertyColumn<Reservation, String>(new StringResourceModel("touragents.reservation.total_sum", null), "r.total", "total_sum") {
			private static final long serialVersionUID = 1L;
			@Override
			public void populateItem(Item<ICellPopulator<Reservation>> item, String componentId, IModel<Reservation> rowModel) {
				item.add(new Label(componentId, rowModel.getObject().getTotal_sum()));
			}
		});
		columns.add(new PropertyColumn<Reservation, String>(new StringResourceModel("touragents.reservation.payment_method", null), "r.payment_method", "payment_method"));
		
		SortableTouragentReservationsDataProvider provider = new SortableTouragentReservationsDataProvider();
		provider.setFilterState(filter);

		final DataTable<Reservation, String> dataTable = new DataTable<Reservation, String>("table", columns, provider, ((MySession)getSession()).getSettings().getTable_rows()) {
			private static final long serialVersionUID = 1L;

			@Override
			protected Item<Reservation> newRowItem(final String id, final int index, final IModel<Reservation> model) {
				final Item<Reservation> rowItem = super.newRowItem(id, index, model);
				rowItem.add(new AttributeModifier("style", "cursor: pointer;"));
/*				rowItem.add(new AjaxEventBehavior("onclick") {		
					private static final long serialVersionUID = 1L;

					protected void onEvent(AjaxRequestTarget target) {
						List<Reservation> reservationsList = new MyBatisHelper().selectList("selectTAReservationRoomList", model.getObject().getId());

						String html = "";
						for (Reservation reserv : reservationsList) {
							html += "<tr class='forremove' style='background-color: #f5f5f5'>";
							html += "<td>";
							html += "&nbsp";
							html += "</td>";
							html += "<td>";
							html += "<div>"+
							//DateUtil.toString(model.getObject().getCreate_date(), MyWebApplication.DATE_TIME_FORMAT)+
							"</div>";
							html += "</td>";
							html += "<td>";
							html += "<div>"+reserv.getGuest_name()+"</div>";
							html += "</td>";
							html += "<td>";
							html += "<div>"+
							//model.getObject().getHotel_name()+
							"</div>";
							html += "</td>";
							html += "<td>";
							html += "<div>"+
							//DateUtil.toString(model.getObject().getCheck_in(), MyWebApplication.DATE_TIME_SHORT_FORMAT)+
							"</div>";
							html += "</td>";
							html += "<td>";
							html += "<div>"+
							//DateUtil.toString(model.getObject().getCheck_out(), MyWebApplication.DATE_TIME_SHORT_FORMAT)+
							"</div>";
							html += "</td>";
							html += "<td>";
							html += "<div>" + 
							//ReserveUtil.getReservationStatusText(model.getObject().getStatus()) + 
									"</div>";
							html += "</td>";
							html += "<td>";
							html += "<div>"+reserv.getTotal_sum()+"</div>";
							html += "</td>";
							html += "<td>";
							html += "</td>";
							html += "</tr>";
						}

						target.appendJavaScript("$(\"tr.forremove\").remove(); $(\"#"+rowItem.getMarkupId()+"\").after(\""+html+"\")");
					}
				});*/
				return rowItem;
			}
		};
		dataTable.setOutputMarkupId(true);

		final FilterForm<Reservation> form = new FilterForm<Reservation>("filterForm", provider) {
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
		dataTable.addBottomToolbar(new BootstrapPagingNavigatorToolbar(dataTable, Size.Small));
		dataTable.addBottomToolbar(new MyNoRecordsToolbar(dataTable));

		add(form);
		
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
							CellStyle headerStyle, titlestyle, moneystyle, percentstyle, countstyle, simplestyle, datestyle, datetimestyle;

							headerStyle = wb.createCellStyle();
							titlestyle = wb.createCellStyle();
							moneystyle = wb.createCellStyle();
							percentstyle = wb.createCellStyle();
							countstyle = wb.createCellStyle();
							simplestyle = wb.createCellStyle();
							datestyle = wb.createCellStyle();
							datetimestyle = wb.createCellStyle();
							titlestyle.setAlignment(HorizontalAlignment.CENTER);

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
							titlestyle.setFont(font);
							moneystyle.setFont(font);
							percentstyle.setFont(font);
							countstyle.setFont(font);
							datestyle.setFont(font);
							datetimestyle.setFont(font);
							simplestyle.setFont(font);

							DataFormat format = wb.createDataFormat();
							moneystyle.setDataFormat(format.getFormat("###0.00"));
							percentstyle.setDataFormat(format.getFormat("#,##0.00%"));
							countstyle.setDataFormat(format.getFormat("###0"));
							datestyle.setDataFormat(format.getFormat(MyWebApplication.DATE_FORMAT));
							datetimestyle.setDataFormat(format.getFormat(MyWebApplication.DATE_TIME_FORMAT));

							headerStyle.setBorderBottom(BorderStyle.THIN);
							headerStyle.setBorderTop(BorderStyle.THIN);
							headerStyle.setBorderLeft(BorderStyle.THIN);
							headerStyle.setBorderRight(BorderStyle.THIN);
							headerStyle.setAlignment(HorizontalAlignment.CENTER);
							headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
							headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
							headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
							headerStyle.setWrapText(true);
							headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
							headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
							
							Sheet s = wb.createSheet("ExpectedGuests");
					        int _col = 0;
					        int _row = 0;

					        s.setColumnWidth(_col++, 8000); //Отчет по ИД броней		  	
					        s.setColumnWidth(_col++, 8000); //Отчет по созданным броням
					        s.setColumnWidth(_col++, 8000); //Отчет по Ф.И.О. Гостей
					        s.setColumnWidth(_col++, 6000); //Отчет по туроператорам
					        s.setColumnWidth(_col++, 6000); //Отчет по времени заезда
					        s.setColumnWidth(_col++, 8000); //Отчет по времени выезда
					        s.setColumnWidth(_col++, 8000); //Отчет по статусу комнат
					        s.setColumnWidth(_col++, 8000); //Отчет по типам
					        s.setColumnWidth(_col++, 8000); //Общий отчет
					        
					        _col = 0;
					        
					        Row row = s.createRow(_row++);
					        
					        Cell cell = row.createCell(_col++);
					        cell.setCellStyle(headerStyle);
					        cell.setCellValue(getString("touragents.reservation.id"));
					        
					        cell = row.createCell(_col++);
					        cell.setCellStyle(headerStyle);
					        cell.setCellValue(getString("touragents.reservation.created"));
							
					        cell = row.createCell(_col++);	
					        cell.setCellStyle(headerStyle);
					        cell.setCellValue(getString("touragents.reservation.guest_name"));
							
					        cell = row.createCell(_col++);
					        cell.setCellStyle(headerStyle);
					        cell.setCellValue(getString("touragents.reservation.hotel_name"));
							
					        cell = row.createCell(_col++);
					        cell.setCellStyle(headerStyle);
					        cell.setCellValue(getString("touragents.reservation.check_in"));
							
					        cell = row.createCell(_col++);
					        cell.setCellStyle(headerStyle);
					        cell.setCellValue(getString("touragents.reservation.check_out"));
							
					        cell = row.createCell(_col++);
					        cell.setCellStyle(headerStyle);
					        cell.setCellValue(getString("touragents.reservation.status"));
					        
					        cell = row.createCell(_col++);
					        cell.setCellStyle(headerStyle);
					        cell.setCellValue(getString("touragents.reservation.total_sum"));
					        
					        _col = 0;
					        
							Map<String, Serializable> param = new HashMap<String, Serializable>();
							param.put("creator_user_id", filter.getTouragent_id());
							param.put("hotelsusers_id", filter.getHotel_id());
							param.put("display_name", filter.getHotel_name());
							param.put("date_from", filter.getFromDate());
							param.put("date_to", filter.getToDate());
					        
					        List<Reservation> list = new MyBatisHelper().selectList("selectTAReservationExportList", param);
					        
					        for (Reservation report : list){
					        	row = s.createRow(_row++);
						        
						        cell = row.createCell(_col++);
						        cell.setCellStyle(simplestyle);
						        cell.setCellValue(report.getId());
						        
						        cell = row.createCell(_col++);
						        cell.setCellStyle(datetimestyle);
						        cell.setCellValue(report.getCreate_date());
						        
						        cell = row.createCell(_col++);
						        cell.setCellStyle(simplestyle);
						        cell.setCellValue(report.getGuest_name());
						        
						        cell = row.createCell(_col++);
						        cell.setCellStyle(simplestyle);
						        cell.setCellValue(report.getHotel_name());
						        
						        cell = row.createCell(_col++);
						        cell.setCellStyle(datetimestyle);
						        cell.setCellValue(report.getCheck_in());

						        cell = row.createCell(_col++);
						        cell.setCellStyle(datetimestyle);
						        cell.setCellValue(report.getCheck_out());
						        
						        cell = row.createCell(_col++);
						        cell.setCellStyle(simplestyle);
						        cell.setCellValue(getStatus(report.getStatus()));
						        
						        cell = row.createCell(_col++);
						        cell.setCellStyle(moneystyle);
						        cell.setCellValue(report.getTotal_sum());
						        
						        _col = 0;
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
				return "ReservationReport_" + FormatUtil.toString(new Date(), MyWebApplication.DATE_FORMAT) + ".xlsx";
			}
		};
		add(download);
		add(new AjaxLink<Void>("export") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				download.initiate(target);
			}
			
			@Override
			public boolean isVisible() {
				return isreport;
			}
		});
	}
	
	private String getStatus(byte status) {
		switch (status) {
		case ReservationStatus.RESERVED:
			return getString("hotels.reservation.details.reservation.status.reserved");
		case ReservationStatus.CANCELLED:
			return getString("hotels.reservation.details.reservation.status.cancelled");
		case ReservationStatus.CHECKED_IN:
			return getString("hotels.reservation.details.reservation.status.checked.in");
		case ReservationStatus.CHECKED_OUT:
			return getString("hotels.reservation.details.reservation.status.checked.out");
		default:
			return getString("unkhown");
		}
	}	
}
