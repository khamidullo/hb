package uz.hbs.actions.hotel.reports.panels;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

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
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.value.ValueMap;

import uz.hbs.MyWebApplication;
import uz.hbs.actions.hotel.reports.ReservationReportPanel;
import uz.hbs.beans.reports.HotelReservationReport;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.session.MySession;
import uz.hbs.utils.AJAXDownload;
import uz.hbs.utils.FormatUtil;

public class ExpectedGuestsReportPanel extends ReservationReportPanel {
	private static final long serialVersionUID = 1L;

	public ExpectedGuestsReportPanel(String id, IBreadCrumbModel breadCrumbModel, final ValueMap model) {
		super(id, breadCrumbModel, model);
		
		model.put("hotelsusers_id", ((MySession) getSession()).getUser().getHotelsusers_id());
		final List<HotelReservationReport> list = new MyBatisHelper().selectList("selectExpectedGuestReport", model); 
		
		IModel<List<HotelReservationReport>> reportListModel = new LoadableDetachableModel<List<HotelReservationReport>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<HotelReservationReport> load() {
				return list;
			}
		};
		add(new ListView<HotelReservationReport>("reportlist", reportListModel) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<HotelReservationReport> item) {
				final HotelReservationReport report = (HotelReservationReport) item.getDefaultModelObject();
				item.add(new Label("guest_name", report.getFirst_name() +  " " + report.getLast_name()));
				item.add(new Label("room", report.getRoom_number()));
				item.add(new Label("roomtype", report.getRoomtype()));
				item.add(new Label("guests", report.getGuests()));
				item.add(new Label("check_in", FormatUtil.toString(report.getCheck_in(), MyWebApplication.DATE_TIME_SHORT_FORMAT)));
				item.add(new Label("check_out", FormatUtil.toString(report.getCheck_out(), MyWebApplication.DATE_TIME_SHORT_FORMAT)));
				item.add(new Label("check_out_in", report.getCheck_out_in()));
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
					        cell.setCellValue(getString("hotels.reservation.report.reservation.guest_name"));
					        
					        cell = row.createCell(_col++);
					        cell.setCellStyle(headerStyle);
					        cell.setCellValue(getString("hotels.reservation.report.reservation.room"));
							
					        cell = row.createCell(_col++);
					        cell.setCellStyle(headerStyle);
					        cell.setCellValue(getString("hotels.reservation.report.reservation.room.type"));
							
					        cell = row.createCell(_col++);
					        cell.setCellStyle(headerStyle);
					        cell.setCellValue(getString("hotels.reservation.report.reservation.number_of_guests"));
							
					        cell = row.createCell(_col++);
					        cell.setCellStyle(headerStyle);
					        cell.setCellValue(getString("hotels.reservation.report.reservation.check_in"));
							
					        cell = row.createCell(_col++);
					        cell.setCellStyle(headerStyle);
					        cell.setCellValue(getString("hotels.reservation.report.reservation.check_out"));
							
					        cell = row.createCell(_col++);
					        cell.setCellStyle(headerStyle);
					        cell.setCellValue(getString("hotels.reservation.report.reservation.check_out.in"));
							
					        _col = 0;
					        
					        for (HotelReservationReport report : list){
					        	row = s.createRow(_row++);
						        
						        cell = row.createCell(_col++);
						        cell.setCellStyle(countstyle);
						        cell.setCellValue(report.getFirst_name() +  " " + report.getLast_name());
						        
						        cell = row.createCell(_col++);
						        cell.setCellStyle(countstyle);
						        cell.setCellValue(report.getRoom_number());
						        
						        cell = row.createCell(_col++);
						        cell.setCellStyle(datetimestyle);
						        cell.setCellValue(report.getRoomtype());
						        
						        cell = row.createCell(_col++);
						        cell.setCellStyle(simplestyle);
						        cell.setCellValue(report.getGuests());
						        
						        cell = row.createCell(_col++);
						        cell.setCellStyle(datetimestyle);
						        cell.setCellValue(report.getCheck_in());
						        
						        cell = row.createCell(_col++);
						        cell.setCellStyle(datetimestyle);
						        cell.setCellValue(report.getCheck_out());
						        
						        cell = row.createCell(_col++);
						        cell.setCellStyle(datetimestyle);
						        cell.setCellValue(report.getCheck_out_in());
						        
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
				return "ExpectedGuests_" + FormatUtil.toString(new Date(), MyWebApplication.DATE_FORMAT) + ".xlsx";
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
}
