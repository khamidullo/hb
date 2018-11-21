package uz.hbs.actions.admin.reports;

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
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByBorder;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;
import org.apache.wicket.util.resource.IResourceStream;

import uz.hbs.MyWebApplication;
import uz.hbs.beans.reports.AdditionalServiceReport;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.db.dataproviders.SortableAdditionalServiceReportDataProvider;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;
import uz.hbs.utils.AJAXDownload;
import uz.hbs.utils.CommonUtil;
import uz.hbs.utils.FormatUtil;

public class AdditionalServiceReportPanel extends MyBreadCrumbPanel {
	private static final long serialVersionUID = 1L;
	
	public AdditionalServiceReportPanel(String id, IBreadCrumbModel breadCrumbModel) {
		super(id, breadCrumbModel);
		
		final SortableAdditionalServiceReportDataProvider dp = new SortableAdditionalServiceReportDataProvider();
		
		final DataView<AdditionalServiceReport> dataView;
		add(dataView = new DataView<AdditionalServiceReport>("list", dp){
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(Item<AdditionalServiceReport> item) {
				final AdditionalServiceReport report = (AdditionalServiceReport) item.getDefaultModelObject();
				item.add(new Label("name", new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject() {
						switch (report.getName()) {
							case AdditionalServiceReport.INSURANCE: return getString("hotels.reservation.details.guests.insurance");
							case AdditionalServiceReport.AIR_ARRIVAL_GREEN_HALL: return getString("touragents.reservation.guest.service.airport.green_corridor.arrival");
							case AdditionalServiceReport.AIR_ARRIVAL_VIP_HALL: return getString("touragents.reservation.guest.service.airport.vip_hall.arrival");
							case AdditionalServiceReport.AIR_DEPARTURE_GREEN_HALL: return getString("touragents.reservation.guest.service.airport.green_corridor.departure");
							case AdditionalServiceReport.AIR_DEPARTURE_VIP_HALL: return getString("touragents.reservation.guest.service.airport.vip_hall.departure");
							case AdditionalServiceReport.ARRIVAL_TRANSFER: return getString("touragents.reservation.guest.service.arrive.airport");
							case AdditionalServiceReport.DEPARTURE_TRANSFER: return getString("touragents.reservation.guest.service.depart.airport");
						}
						return null;
					}
				}).setEscapeModelStrings(false));
				item.add(new Label("amount_1", report.getAmount_1()));
				item.add(new Label("count_1", report.getCount_1()));
				item.add(new Label("amount_3", report.getAmount_3()));
				item.add(new Label("count_3", report.getCount_3()));
				item.add(new Label("amount_6", report.getAmount_6()));
				item.add(new Label("count_6", report.getCount_6()));
				item.add(new Label("amount_12", report.getAmount_12()));
				item.add(new Label("count_12", report.getCount_12()));
				item.add(new Label("amount", report.getAmount()));
				item.add(new Label("count", report.getCount()));
			}
		});
		dataView.setItemsPerPage(getMySession().getSettings().getTable_rows());

        add(new OrderByBorder<String>("orderByName", "name", dp){
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSortChanged(){
                dataView.setCurrentPage(0);
            }
        });
        add(new OrderByBorder<String>("orderByAmount1", "amount_1", dp){
        	private static final long serialVersionUID = 1L;
        	
        	@Override
        	protected void onSortChanged(){
        		dataView.setCurrentPage(0);
        	}
        });
        add(new OrderByBorder<String>("orderByCount1", "count_1", dp){
        	private static final long serialVersionUID = 1L;
        	
        	@Override
        	protected void onSortChanged(){
        		dataView.setCurrentPage(0);
        	}
        });
        add(new OrderByBorder<String>("orderByAmount3", "amount_3", dp){
        	private static final long serialVersionUID = 1L;
        	
        	@Override
        	protected void onSortChanged(){
        		dataView.setCurrentPage(0);
        	}
        });
        add(new OrderByBorder<String>("orderByCount3", "count_3", dp){
        	private static final long serialVersionUID = 1L;
        	
        	@Override
        	protected void onSortChanged(){
        		dataView.setCurrentPage(0);
        	}
        });
        add(new OrderByBorder<String>("orderByAmount6", "amount_6", dp){
        	private static final long serialVersionUID = 1L;
        	
        	@Override
        	protected void onSortChanged(){
        		dataView.setCurrentPage(0);
        	}
        });
        add(new OrderByBorder<String>("orderByCount6", "count_6", dp){
        	private static final long serialVersionUID = 1L;
        	
        	@Override
        	protected void onSortChanged(){
        		dataView.setCurrentPage(0);
        	}
        });
        add(new OrderByBorder<String>("orderByAmount12", "amount_12", dp){
        	private static final long serialVersionUID = 1L;
        	
        	@Override
        	protected void onSortChanged(){
        		dataView.setCurrentPage(0);
        	}
        });
        add(new OrderByBorder<String>("orderByCount12", "amount_12", dp){
        	private static final long serialVersionUID = 1L;
        	
        	@Override
        	protected void onSortChanged(){
        		dataView.setCurrentPage(0);
        	}
        });
        add(new OrderByBorder<String>("orderByAmount", "amount", dp){
        	private static final long serialVersionUID = 1L;
        	
        	@Override
        	protected void onSortChanged(){
        		dataView.setCurrentPage(0);
        	}
        });
        add(new OrderByBorder<String>("orderByCount", "count", dp){
        	private static final long serialVersionUID = 1L;
        	
        	@Override
        	protected void onSortChanged(){
        		dataView.setCurrentPage(0);
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
							
							Sheet s = wb.createSheet("Report");
					        int _col = 0;
					        int _row = 0;

					        s.setColumnWidth(_col++, 8000); //Name
					        
					        _col = 0;
					        
					        Row row = s.createRow(_row++);
					        
					        Cell cell = row.createCell(_col++);
					        cell.setCellStyle(headerStyle);
					        cell.setCellValue(getString("touragents.reservation.guest.service.name"));
					        
					        s.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum() + 2, 0, 0));
					        
					        cell = row.createCell(_col++);
					        cell.setCellStyle(headerStyle);
					        cell.setCellValue(getString("touragents.reservation.guest.service.report.last"));
					        
					        s.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 1, 10));
					        
					        row = s.createRow(_row++);
					        
					        _col = 1;
							
					        cell = row.createCell(_col);
					        cell.setCellStyle(headerStyle);
					        cell.setCellValue(getString("report.last.month.1"));
					        
					        s.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), _col, _col + 1));
					        
					        _col = _col + 2;
					        
					        cell = row.createCell(_col);
					        cell.setCellStyle(headerStyle);
					        cell.setCellValue(getString("report.last.month.3"));
					        
					        s.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), _col, _col + 1));
					        
					        _col = _col + 2;
					        
					        cell = row.createCell(_col);
					        cell.setCellStyle(headerStyle);
					        cell.setCellValue(getString("report.last.month.6"));
					        
					        s.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), _col, _col + 1));
					        
					        _col = _col + 2;
					        
					        cell = row.createCell(_col);
					        cell.setCellStyle(headerStyle);
					        cell.setCellValue(getString("report.last.month.12"));
					        
					        s.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), _col, _col + 1));
					        
					        _col = _col + 2;
					        
					        cell = row.createCell(_col);
					        cell.setCellStyle(headerStyle);
					        cell.setCellValue(getString("report.last.month.period"));
					        
					        s.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), _col, _col + 1));
					        
					        _col = 1;
					        
					        row = s.createRow(_row++);
					        
					        for (int i = 1; i <= 10; i+=2) {
						        cell = row.createCell(i);
						        cell.setCellStyle(headerStyle);
						        cell.setCellValue(getString("report.amount"));
						        s.setColumnWidth(i, 4000);

						        cell = row.createCell(i + 1);
						        cell.setCellStyle(headerStyle);
						        cell.setCellValue(getString("report.count"));
						        s.setColumnWidth((i + 1), 4000);
					        }
					        
					        _col = 0;
					        
					        List<AdditionalServiceReport> list = new MyBatisHelper().selectList("selectAdditionalServiceReportList");
					        
					        for (AdditionalServiceReport report : list){
					        	row = s.createRow(_row++);
					        	
						        cell = row.createCell(_col++);
						        cell.setCellStyle(simplestyle);
						        cell.setCellValue(getName(report.getName()));
						        
						        cell = row.createCell(_col++);
						        cell.setCellStyle(moneystyle);
						        cell.setCellValue(CommonUtil.nvl(report.getAmount_1()));
						        
						        cell = row.createCell(_col++);
						        cell.setCellStyle(countstyle);
						        cell.setCellValue(report.getCount_1());
						        
						        cell = row.createCell(_col++);
						        cell.setCellStyle(moneystyle);
						        cell.setCellValue(CommonUtil.nvl(report.getAmount_3()));
						        
						        cell = row.createCell(_col++);
						        cell.setCellStyle(countstyle);
						        cell.setCellValue(report.getCount_3());
						        
						        cell = row.createCell(_col++);
						        cell.setCellStyle(moneystyle);
						        cell.setCellValue(CommonUtil.nvl(report.getAmount_6()));
						        
						        cell = row.createCell(_col++);
						        cell.setCellStyle(countstyle);
						        cell.setCellValue(report.getCount_6());
						        
						        cell = row.createCell(_col++);
						        cell.setCellStyle(moneystyle);
						        cell.setCellValue(CommonUtil.nvl(report.getAmount_12()));
						        
						        cell = row.createCell(_col++);
						        cell.setCellStyle(countstyle);
						        cell.setCellValue(report.getCount_12());
						        
						        cell = row.createCell(_col++);
						        cell.setCellStyle(moneystyle);
						        cell.setCellValue(CommonUtil.nvl(report.getAmount()));
						        
						        cell = row.createCell(_col++);
						        cell.setCellStyle(countstyle);
						        cell.setCellValue(report.getCount());
						        
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
				return "AdditionalServiceReport" + FormatUtil.toString(new Date(), MyWebApplication.DATE_FORMAT) + ".xlsx";
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
		return this.getClass();
	}

	@Override
	public IModel<String> getTitle() {
		return new StringResourceModel("admin.reports.additional_services", null);
	}
	
	public String getName(String name){
		switch (name) {
			case AdditionalServiceReport.INSURANCE: return getString("hotels.reservation.details.guests.insurance");
			case AdditionalServiceReport.AIR_ARRIVAL_GREEN_HALL: return getString("touragents.reservation.guest.service.airport.green_corridor.arrival");
			case AdditionalServiceReport.AIR_ARRIVAL_VIP_HALL: return getString("touragents.reservation.guest.service.airport.vip_hall.arrival");
			case AdditionalServiceReport.AIR_DEPARTURE_GREEN_HALL: return getString("touragents.reservation.guest.service.airport.green_corridor.departure");
			case AdditionalServiceReport.AIR_DEPARTURE_VIP_HALL: return getString("touragents.reservation.guest.service.airport.vip_hall.departure");
			case AdditionalServiceReport.ARRIVAL_TRANSFER: return getString("touragents.reservation.guest.service.arrive.airport");
			case AdditionalServiceReport.DEPARTURE_TRANSFER: return getString("touragents.reservation.guest.service.depart.airport");
		}
		return null;
	}
}
