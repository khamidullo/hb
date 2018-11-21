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
import uz.hbs.beans.reports.ClientReport;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.db.dataproviders.SortableClientReportDataProvider;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;
import uz.hbs.markup.navigation.BootstrapAjaxPagingNavigator;
import uz.hbs.markup.navigation.BootstrapPagingNavigator.Size;
import uz.hbs.utils.AJAXDownload;
import uz.hbs.utils.CommonUtil;
import uz.hbs.utils.FormatUtil;

public class ClientReportPanel extends MyBreadCrumbPanel {
	private static final long serialVersionUID = 1L;

	public ClientReportPanel(String id, IBreadCrumbModel breadCrumbModel) {
		super(id, breadCrumbModel);
		SortableClientReportDataProvider dp = new SortableClientReportDataProvider();
		
		final Double totalVolume = new MyBatisHelper().selectOne("selectTotalVolumeReservations");
		
		final DataView<ClientReport> dataView;
		add(dataView = new DataView<ClientReport>("list", dp){
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(Item<ClientReport> item) {
				final ClientReport report = (ClientReport) item.getDefaultModelObject();
				item.add(new Label("company", report.getCompany()));
				item.add(new Label("manager", report.getManager()));
				item.add(new Label("regdate", FormatUtil.toString(report.getRegdate(), MyWebApplication.DATE_TIME_FORMAT)));
				item.add(new Label("request_count", report.getRequest_count()));
				item.add(new Label("reserve_count", report.getReserve_count()));
				item.add(new Label("cancelled_count", report.getCancelled_count()));
				item.add(new Label("total", new AbstractReadOnlyModel<Double>() {
					private static final long serialVersionUID = 1L;

					@Override
					public Double getObject() {
						double d = new MyBatisHelper().selectOne("selectTotalVolumeReservations", report.getTouragent_id());
						return (d * 100) / totalVolume;
					}
				}));
				item.add(new Label("hotel_1", report.getHotel_1()));
				item.add(new Label("service_1", report.getService_1()));
				item.add(new Label("hotel_3", report.getHotel_3()));
				item.add(new Label("service_3", report.getService_3()));
				item.add(new Label("hotel_6", report.getHotel_6()));
				item.add(new Label("service_6", report.getService_6()));
				item.add(new Label("hotel_12", report.getHotel_12()));
				item.add(new Label("service_12", report.getService_12()));
				item.add(new Label("hotel", report.getHotel()));
				item.add(new Label("service", report.getService()));
			}
		});
		dataView.setItemsPerPage(getMySession().getSettings().getTable_rows());
        add(new OrderByBorder<String>("orderByCompany", "company", dp){
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSortChanged(){
                dataView.setCurrentPage(0);
            }
        });
        add(new OrderByBorder<String>("orderByManager", "manager", dp){
        	private static final long serialVersionUID = 1L;
        	
        	@Override
        	protected void onSortChanged(){
        		dataView.setCurrentPage(0);
        	}
        });
        add(new OrderByBorder<String>("orderByRegDate", "regdate", dp){
        	private static final long serialVersionUID = 1L;
        	
        	@Override
        	protected void onSortChanged(){
        		dataView.setCurrentPage(0);
        	}
        });
        add(new OrderByBorder<String>("orderByServiceRequest", "request_count", dp){
        	private static final long serialVersionUID = 1L;
        	
        	@Override
        	protected void onSortChanged(){
        		dataView.setCurrentPage(0);
        	}
        });
        add(new OrderByBorder<String>("orderByServiceReserve", "reserve_count", dp){
        	private static final long serialVersionUID = 1L;
        	
        	@Override
        	protected void onSortChanged(){
        		dataView.setCurrentPage(0);
        	}
        });
        add(new OrderByBorder<String>("orderByServiceCancelled", "cancelled_count", dp){
        	private static final long serialVersionUID = 1L;
        	
        	@Override
        	protected void onSortChanged(){
        		dataView.setCurrentPage(0);
        	}
        });
	        add(new OrderByBorder<String>("orderByHotel1", "hotel_1", dp){
        	private static final long serialVersionUID = 1L;
        	
        	@Override
        	protected void onSortChanged(){
        		dataView.setCurrentPage(0);
        	}
        });
        add(new OrderByBorder<String>("orderByService1", "service_1", dp){
        	private static final long serialVersionUID = 1L;
        	
        	@Override
        	protected void onSortChanged(){
        		dataView.setCurrentPage(0);
        	}
        });
        add(new OrderByBorder<String>("orderByHotel3", "hotel_3", dp){
        	private static final long serialVersionUID = 1L;
        	
        	@Override
        	protected void onSortChanged(){
        		dataView.setCurrentPage(0);
        	}
        });
        add(new OrderByBorder<String>("orderByService3", "service_3", dp){
        	private static final long serialVersionUID = 1L;
        	
        	@Override
        	protected void onSortChanged(){
        		dataView.setCurrentPage(0);
        	}
        });
        add(new OrderByBorder<String>("orderByHotel6", "hotel_6", dp){
        	private static final long serialVersionUID = 1L;
        	
        	@Override
        	protected void onSortChanged(){
        		dataView.setCurrentPage(0);
        	}
        });
        add(new OrderByBorder<String>("orderByService6", "service_6", dp){
        	private static final long serialVersionUID = 1L;
        	
        	@Override
        	protected void onSortChanged(){
        		dataView.setCurrentPage(0);
        	}
        });
        add(new OrderByBorder<String>("orderByHotel12", "hotel_12", dp){
        	private static final long serialVersionUID = 1L;
        	
        	@Override
        	protected void onSortChanged(){
        		dataView.setCurrentPage(0);
        	}
        });
        add(new OrderByBorder<String>("orderByService12", "service_12", dp){
        	private static final long serialVersionUID = 1L;
        	
        	@Override
        	protected void onSortChanged(){
        		dataView.setCurrentPage(0);
        	}
        });
        add(new OrderByBorder<String>("orderByHotel", "hotel", dp){
        	private static final long serialVersionUID = 1L;
        	
        	@Override
        	protected void onSortChanged(){
        		dataView.setCurrentPage(0);
        	}
        });
        add(new OrderByBorder<String>("orderByService", "service", dp){
        	private static final long serialVersionUID = 1L;
        	
        	@Override
        	protected void onSortChanged(){
        		dataView.setCurrentPage(0);
        	}
        });
		add(new BootstrapAjaxPagingNavigator("paging", dataView, Size.Default){
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible() {
				return dataView.getPageCount() > 1;
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
					        cell.setCellValue(getString("report.client.company.name"));
					        
					        s.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum() + 2, cell.getColumnIndex(), cell.getColumnIndex()));
					        
					        cell = row.createCell(_col++);
					        cell.setCellStyle(headerStyle);
					        cell.setCellValue(getString("report.client.company.manager"));
					        
					        s.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum() + 2, cell.getColumnIndex(), cell.getColumnIndex()));
					        
					        cell = row.createCell(_col++);
					        cell.setCellStyle(headerStyle);
					        cell.setCellValue(getString("report.client.regdate"));
					        
					        s.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum() + 2, cell.getColumnIndex(), cell.getColumnIndex()));
					        
					        cell = row.createCell(_col++);
					        cell.setCellStyle(headerStyle);
					        cell.setCellValue(getString("report.client.count.request"));
					        
					        s.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum() + 2, cell.getColumnIndex(), cell.getColumnIndex()));
					        
					        cell = row.createCell(_col++);
					        cell.setCellStyle(headerStyle);
					        cell.setCellValue(getString("report.client.count.reservation"));
					        
					        s.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum() + 2, cell.getColumnIndex(), cell.getColumnIndex()));
					        
					        cell = row.createCell(_col++);
					        cell.setCellStyle(headerStyle);
					        cell.setCellValue(getString("report.client.count.canceled"));
					        
					        s.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum() + 2, cell.getColumnIndex(), cell.getColumnIndex()));
					        
					        cell = row.createCell(_col++);
					        cell.setCellStyle(headerStyle);
					        cell.setCellValue(getString("report.client.turnovers.last"));
					        
					        s.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), cell.getColumnIndex(), cell.getColumnIndex() + 10));
					        
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
					        
					        _col++;
					        
					        row = s.createRow(_row++);
					        
					        int i;
					        
					        for (i = _col; i <= _col + 10; i+=2) {
						        cell = row.createCell(i);
						        cell.setCellStyle(headerStyle);
						        cell.setCellValue(getString("report.client.count.hotel"));
						        s.setColumnWidth(i, 4000);

						        cell = row.createCell(i + 1);
						        cell.setCellStyle(headerStyle);
						        cell.setCellValue(getString("report.client.count.service"));
						        s.setColumnWidth((i + 1), 4000);
					        }
					        
					        _col = _col + i;
					        
					        row = s.getRow(0);
					        
					        cell = row.createCell(_col);
					        cell.setCellStyle(headerStyle);
					        cell.setCellValue(getString("report.client.count.percent_of_total"));
					        
					        _col = 0;
					        
					        List<ClientReport> list = new MyBatisHelper().selectList("selectClientReportList");
					        
					        for (ClientReport report : list){
					        	row = s.createRow(_row ++);
					        	
						        cell = row.createCell(_col++);
						        cell.setCellStyle(simplestyle);
						        cell.setCellValue(report.getCompany());
						        
						        cell = row.createCell(_col++);
						        cell.setCellStyle(simplestyle);
						        cell.setCellValue(report.getManager());
						        
						        cell = row.createCell(_col++);
						        cell.setCellStyle(datetimestyle);
						        cell.setCellValue(report.getRegdate());
						        
						        cell = row.createCell(_col++);
						        cell.setCellStyle(countstyle);
						        cell.setCellValue(report.getRequest_count());
						        
						        cell = row.createCell(_col++);
						        cell.setCellStyle(countstyle);
						        cell.setCellValue(report.getReserve_count());
						        
						        cell = row.createCell(_col++);
						        cell.setCellStyle(countstyle);
						        cell.setCellValue(report.getCancelled_count());
						        
						        cell = row.createCell(_col++);
						        cell.setCellStyle(countstyle);
						        cell.setCellValue(CommonUtil.nvl(report.getHotel_1()));
						        
						        cell = row.createCell(_col++);
						        cell.setCellStyle(countstyle);
						        cell.setCellValue(report.getService_1());
						        
						        cell = row.createCell(_col++);
						        cell.setCellStyle(countstyle);
						        cell.setCellValue(CommonUtil.nvl(report.getHotel_3()));
						        
						        cell = row.createCell(_col++);
						        cell.setCellStyle(countstyle);
						        cell.setCellValue(report.getService_3());
						        
						        cell = row.createCell(_col++);
						        cell.setCellStyle(countstyle);
						        cell.setCellValue(CommonUtil.nvl(report.getHotel_6()));
						        
						        cell = row.createCell(_col++);
						        cell.setCellStyle(countstyle);
						        cell.setCellValue(report.getService_6());
						        
						        cell = row.createCell(_col++);
						        cell.setCellStyle(countstyle);
						        cell.setCellValue(CommonUtil.nvl(report.getHotel_12()));
						        
						        cell = row.createCell(_col++);
						        cell.setCellStyle(countstyle);
						        cell.setCellValue(report.getService_12());
						        
						        cell = row.createCell(_col++);
						        cell.setCellStyle(countstyle);
						        cell.setCellValue(CommonUtil.nvl(report.getHotel()));
						        
						        cell = row.createCell(_col++);
						        cell.setCellStyle(countstyle);
						        cell.setCellValue(report.getService());
						        
						        cell = row.createCell(_col++);
						        cell.setCellStyle(countstyle);
						        cell.setCellValue(report.getTotal(totalVolume, (Double) new MyBatisHelper().selectOne("selectTotalVolumeReservations", report.getTouragent_id())));
						        
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
				return "ClientReport" + FormatUtil.toString(new Date(), MyWebApplication.DATE_FORMAT) + ".xlsx";
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
		return new StringResourceModel("admin.reports.clients", null);
	}
}
