package uz.hbs.actions.touragent.reports;

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
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.HeadersToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;
import org.apache.wicket.util.resource.IResourceStream;

import uz.hbs.MyWebApplication;
import uz.hbs.beans.Commission;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.db.dataproviders.SortableTouragentCommissionsDataProvider;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;
import uz.hbs.markup.navigation.BootstrapPagingNavigator.Size;
import uz.hbs.markup.navigation.BootstrapPagingNavigatorToolbar;
import uz.hbs.markup.navigation.repeater.data.table.MyNoRecordsToolbar;
import uz.hbs.session.MySession;
import uz.hbs.utils.AJAXDownload;
import uz.hbs.utils.FormatUtil;

public class CommissionsReportPanel extends Panel {
	private static final long serialVersionUID = 1L;

	public CommissionsReportPanel(String id) {
		super(id);

		final MyFeedbackPanel feedback = new MyFeedbackPanel("feedback");
		add(feedback);
		feedback.setOutputMarkupId(true);

		WebMarkupContainer wrapper = new WebMarkupContainer("wrapper");
		wrapper.setOutputMarkupId(true);
		add(wrapper);

		final Commission filter = new Commission();
		filter.setTouragent_id(((MySession) getSession()).getUser().getId());
		// filter.setStatus(ReservationStatus.CANCELLED);
		add(new ReportFilterPanel("searchFilterForm", filter, wrapper, feedback, false));

		// Filter model sifatida properties fayldagi '{}' property name bilan almashtirlishi kerak
		Label resultLabel = new Label("resultLabel", new StringResourceModel("touragents.reservation.result_label", new Model<Commission>(filter)));
		wrapper.add(resultLabel);

		ArrayList<IColumn<Commission, String>> columns = new ArrayList<IColumn<Commission, String>>();

		columns.add(new PropertyColumn<Commission, String>(new StringResourceModel("touragents.commissions.hotel_name", null), "c.hotel_name",
				"hotel_name"));
		columns.add(new PropertyColumn<Commission, String>(new StringResourceModel("touragents.commissions.reservations_count", null),
				"c.reservations_count", "reservations_count"));
		columns.add(new PropertyColumn<Commission, String>(new StringResourceModel("touragents.commissions.definite", null), "c.definite", "definite"));
		columns.add(new PropertyColumn<Commission, String>(new StringResourceModel("touragents.commissions.tentative", null), "c.tentative",
				"tentative"));
		columns.add(new PropertyColumn<Commission, String>(new StringResourceModel("touragents.commissions.canceled", null), "c.canceled", "canceled"));
		columns.add(new PropertyColumn<Commission, String>(new StringResourceModel("touragents.commissions.total", null), "c.total", "total") {
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<Commission>> item, String componentId, IModel<Commission> rowModel) {
				item.add(new Label(componentId, rowModel.getObject().getTotal()));
			}
		});
		columns.add(new PropertyColumn<Commission, String>(new StringResourceModel("touragents.commissions.commission", null), "c.commission",
				"commission") {
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<Commission>> item, String componentId, IModel<Commission> rowModel) {
				item.add(new Label(componentId, rowModel.getObject().getCommission()));
			}
		});

		SortableTouragentCommissionsDataProvider provider = new SortableTouragentCommissionsDataProvider();
		provider.setFilterState(filter);

		final DataTable<Commission, String> dataTable = new DataTable<Commission, String>("table", columns, provider, ((MySession)getSession()).getSettings().getTable_rows());
		dataTable.setOutputMarkupId(true);

		final FilterForm<Commission> form = new FilterForm<Commission>("filterForm", provider) {
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

		wrapper.add(form);
		
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
					        cell.setCellValue(getString("touragents.commissions.hotel_name"));
					        
					        cell = row.createCell(_col++);
					        cell.setCellStyle(headerStyle);
					        cell.setCellValue(getString("touragents.commissions.reservations_count"));
							
					        cell = row.createCell(_col++);	
					        cell.setCellStyle(headerStyle);
					        cell.setCellValue(getString("touragents.commissions.definite"));
							
					        cell = row.createCell(_col++);
					        cell.setCellStyle(headerStyle);
					        cell.setCellValue(getString("touragents.commissions.tentative"));
							
					        cell = row.createCell(_col++);
					        cell.setCellStyle(headerStyle);
					        cell.setCellValue(getString("touragents.commissions.canceled"));
							
					        cell = row.createCell(_col++);
					        cell.setCellStyle(headerStyle);
					        cell.setCellValue(getString("touragents.commissions.total"));
							
					        cell = row.createCell(_col++);
					        cell.setCellStyle(headerStyle);
					        cell.setCellValue(getString("touragents.commissions.commission"));
					        
					        _col = 0;
					        
							Map<String, Serializable> param = new HashMap<String, Serializable>();
							param.put("creator_user_id", filter.getTouragent_id());
							param.put("display_name", filter.getHotel_name());
							param.put("date_from", filter.getFromDate());
							param.put("date_to", filter.getToDate());
					        
					        List<Commission> list = new MyBatisHelper().selectList("selectTACommissionsExportList", param);
					        
					        for (Commission report : list){
					        	row = s.createRow(_row++);
						        
						        cell = row.createCell(_col++);
						        cell.setCellStyle(countstyle);
						        cell.setCellValue(report.getHotel_name());
						        
						        cell = row.createCell(_col++);
						        cell.setCellStyle(datetimestyle);
						        cell.setCellValue(report.getReservations_count());
						        
						        cell = row.createCell(_col++);
						        cell.setCellStyle(countstyle);
						        cell.setCellValue(report.getDefinite());
						        
						        cell = row.createCell(_col++);
						        cell.setCellStyle(countstyle);
						        cell.setCellValue(report.getTentative());
						        
						        cell = row.createCell(_col++);
						        cell.setCellStyle(countstyle);
						        cell.setCellValue(report.getCanceled());
						        
						        cell = row.createCell(_col++);
						        cell.setCellStyle(moneystyle);
						        cell.setCellValue(report.getTotal());
						        
						        cell = row.createCell(_col++);
						        cell.setCellStyle(moneystyle);
						        cell.setCellValue(report.getCommission());
						        
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
				return "CommissionReport_" + FormatUtil.toString(new Date(), MyWebApplication.DATE_FORMAT) + ".xlsx";
			}
		};
		add(download);
		wrapper.add(new AjaxLink<Void>("export") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				download.initiate(target);
			}
		});
	}
}
