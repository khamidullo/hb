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
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;
import org.apache.wicket.util.resource.IResourceStream;

import uz.hbs.MyWebApplication;
import uz.hbs.beans.reports.HotelReport;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.db.dataproviders.SortableHotelReportDataProvider;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;
import uz.hbs.markup.navigation.BootstrapAjaxPagingNavigator;
import uz.hbs.markup.navigation.BootstrapPagingNavigator.Size;
import uz.hbs.utils.AJAXDownload;
import uz.hbs.utils.CommonUtil;
import uz.hbs.utils.FormatUtil;

public class HotelReportPanel extends MyBreadCrumbPanel {
	private static final long serialVersionUID = 1L;

	public HotelReportPanel(String id, IBreadCrumbModel breadCrumbModel) {
		super(id, breadCrumbModel);

		final long totalcount = new MyBatisHelper().selectOne("selectTotalCountReservations");

		SortableHotelReportDataProvider dp = new SortableHotelReportDataProvider();
		final DataView<HotelReport> dataView;
		add(dataView = new DataView<HotelReport>("list", dp) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(Item<HotelReport> item) {
				final HotelReport report = (HotelReport) item.getDefaultModelObject();
				item.add(new Label("hotel", report.getHotel()));
				item.add(new Label("city", report.getCity()));
				item.add(new Label("regdate", FormatUtil.toString(report.getRegdate(), MyWebApplication.DATE_TIME_FORMAT)));
				item.add(new Label("login", FormatUtil.toString(report.getLogin(), MyWebApplication.DATE_TIME_FORMAT)));
				item.add(new Label("putting_number", FormatUtil.toString(report.getPutting_number(), MyWebApplication.DATE_TIME_FORMAT)));
				item.add(new Label("manage_rate", FormatUtil.toString(report.getManage_rate(), MyWebApplication.DATE_TIME_FORMAT)));
				item.add(new Label("total", report.getMonth() * 100 / totalcount));
				item.add(new Label("month_1", report.getMonth_1()));
				item.add(new Label("month_3", report.getMonth_3()));
				item.add(new Label("month_6", report.getMonth_6()));
				item.add(new Label("month_12", report.getMonth_12()));
				item.add(new Label("month", report.getMonth()));
				item.add(new Label("tentative", report.isTentative() ? "Да" : "Нет"));
			}
		});
		dataView.setItemsPerPage(getMySession().getSettings().getTable_rows());
		add(new OrderByBorder<String>("orderByHotel", "hotel", dp) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSortChanged() {
				dataView.setCurrentPage(0);
			}
		});
		add(new OrderByBorder<String>("orderByCity", "city", dp) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSortChanged() {
				dataView.setCurrentPage(0);
			}
		});
		add(new OrderByBorder<String>("orderByRegDate", "regdate", dp) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSortChanged() {
				dataView.setCurrentPage(0);
			}
		});
		add(new OrderByBorder<String>("orderByLogin", "login", dp) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSortChanged() {
				dataView.setCurrentPage(0);
			}
		});
		add(new OrderByBorder<String>("orderByPuttingNumber", "putting_number", dp) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSortChanged() {
				dataView.setCurrentPage(0);
			}
		});
		add(new OrderByBorder<String>("orderByManageRate", "manage_rate", dp) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSortChanged() {
				dataView.setCurrentPage(0);
			}
		});
		add(new OrderByBorder<String>("orderByTentative", "tentative", dp) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSortChanged() {
				dataView.setCurrentPage(0);
			}
		});
		add(new OrderByBorder<String>("orderByMonth1", "month_1", dp) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSortChanged() {
				dataView.setCurrentPage(0);
			}
		});
		add(new OrderByBorder<String>("orderByMonth3", "month_3", dp) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSortChanged() {
				dataView.setCurrentPage(0);
			}
		});
		add(new OrderByBorder<String>("orderByMonth6", "month_6", dp) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSortChanged() {
				dataView.setCurrentPage(0);
			}
		});
		add(new OrderByBorder<String>("orderByMonth12", "month_12", dp) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSortChanged() {
				dataView.setCurrentPage(0);
			}
		});
		add(new OrderByBorder<String>("orderByMonth", "month", dp) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSortChanged() {
				dataView.setCurrentPage(0);
			}
		});
		add(new BootstrapAjaxPagingNavigator("paging", dataView, Size.Default) {
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

							_col = 0;

							Row row = s.createRow(_row++);

							Cell cell = row.createCell(_col++);
							cell.setCellStyle(headerStyle);
							cell.setCellValue(getString("report.hotel.name"));
							s.setColumnWidth(cell.getColumnIndex(), 5000); //Name

							s.addMergedRegion(
									new CellRangeAddress(row.getRowNum(), row.getRowNum() + 1, cell.getColumnIndex(), cell.getColumnIndex()));

							cell = row.createCell(_col++);
							cell.setCellStyle(headerStyle);
							cell.setCellValue(getString("report.hotel.city"));
							s.setColumnWidth(cell.getColumnIndex(), 6000); //Name

							s.addMergedRegion(
									new CellRangeAddress(row.getRowNum(), row.getRowNum() + 1, cell.getColumnIndex(), cell.getColumnIndex()));

							cell = row.createCell(_col++);
							cell.setCellStyle(headerStyle);
							cell.setCellValue(getString("report.client.regdate"));
							s.setColumnWidth(cell.getColumnIndex(), 6000); //Name

							s.addMergedRegion(
									new CellRangeAddress(row.getRowNum(), row.getRowNum() + 1, cell.getColumnIndex(), cell.getColumnIndex()));

							cell = row.createCell(_col++);
							cell.setCellStyle(headerStyle);
							cell.setCellValue(getString("report.hotel.date_of_last_activity"));

							s.addMergedRegion(
									new CellRangeAddress(row.getRowNum(), row.getRowNum(), cell.getColumnIndex(), cell.getColumnIndex() + 2));

							cell = row.createCell(_col + 2);
							cell.setCellStyle(headerStyle);
							cell.setCellValue(getString("report.hotel.count_of_last_reserve"));

							s.addMergedRegion(
									new CellRangeAddress(row.getRowNum(), row.getRowNum(), cell.getColumnIndex(), cell.getColumnIndex() + 5));

							row = s.createRow(_row++);
							row.setHeight((short) 600);

							_col = 3;

							cell = row.createCell(_col++);
							cell.setCellStyle(headerStyle);
							cell.setCellValue(getString("report.hotel.login"));
							s.setColumnWidth(cell.getColumnIndex(), 5000);

							cell = row.createCell(_col++);
							cell.setCellStyle(headerStyle);
							cell.setCellValue(getString("report.hotel.putting_number"));
							s.setColumnWidth(cell.getColumnIndex(), 5000);

							cell = row.createCell(_col++);
							cell.setCellStyle(headerStyle);
							cell.setCellValue(getString("report.hotel.manage_rates"));
							s.setColumnWidth(cell.getColumnIndex(), 5000);

							cell = row.createCell(_col);
							cell.setCellStyle(headerStyle);
							cell.setCellValue(getString("report.last.month.1"));
							s.setColumnWidth(cell.getColumnIndex(), 5000);

							_col = _col + 1;

							cell = row.createCell(_col);
							cell.setCellStyle(headerStyle);
							cell.setCellValue(getString("report.last.month.3"));
							s.setColumnWidth(cell.getColumnIndex(), 4000);

							_col = _col + 1;

							cell = row.createCell(_col);
							cell.setCellStyle(headerStyle);
							cell.setCellValue(getString("report.last.month.6"));
							s.setColumnWidth(cell.getColumnIndex(), 4000);

							_col = _col + 1;

							cell = row.createCell(_col);
							cell.setCellStyle(headerStyle);
							cell.setCellValue(getString("report.last.month.12"));
							s.setColumnWidth(cell.getColumnIndex(), 4000);

							_col = _col + 1;

							cell = row.createCell(_col);
							cell.setCellStyle(headerStyle);
							cell.setCellValue(getString("report.last.month.period"));
							s.setColumnWidth(cell.getColumnIndex(), 4000);

							_col = _col + 1;

							cell = row.createCell(_col);
							cell.setCellStyle(headerStyle);
							cell.setCellValue(getString("report.client.count.percent_of_total"));
							s.setColumnWidth(cell.getColumnIndex(), 6000);

							_col++;

							row = s.getRow(0);

							cell = row.createCell(_col);
							cell.setCellStyle(headerStyle);
							cell.setCellValue(getString("report.hotel.tentative"));
							s.setColumnWidth(cell.getColumnIndex(), 9000);

							s.addMergedRegion(
									new CellRangeAddress(row.getRowNum(), row.getRowNum() + 1, cell.getColumnIndex(), cell.getColumnIndex()));

							_col = 0;

							List<HotelReport> list = new MyBatisHelper().selectList("selectHotelReportList");

							for (HotelReport report : list) {
								row = s.createRow(_row++);

								cell = row.createCell(_col++);
								cell.setCellStyle(simplestyle);
								cell.setCellValue(report.getHotel());

								cell = row.createCell(_col++);
								cell.setCellStyle(simplestyle);
								cell.setCellValue(report.getCity());

								cell = row.createCell(_col++);
								cell.setCellStyle(datetimestyle);
								cell.setCellValue(report.getRegdate());

								cell = row.createCell(_col++);
								cell.setCellStyle(datetimestyle);
								cell.setCellValue(report.getLogin());

								cell = row.createCell(_col++);
								cell.setCellStyle(datetimestyle);
								if (report.getPutting_number() != null)
									cell.setCellValue(report.getPutting_number());

								cell = row.createCell(_col++);
								cell.setCellStyle(countstyle);
								if (report.getManage_rate() != null)
									cell.setCellValue(report.getManage_rate());

								cell = row.createCell(_col++);
								cell.setCellStyle(countstyle);
								cell.setCellValue(CommonUtil.nvl(report.getMonth_1()));

								cell = row.createCell(_col++);
								cell.setCellStyle(countstyle);
								cell.setCellValue(CommonUtil.nvl(report.getMonth_3()));

								cell = row.createCell(_col++);
								cell.setCellStyle(countstyle);
								cell.setCellValue(CommonUtil.nvl(report.getMonth_6()));

								cell = row.createCell(_col++);
								cell.setCellStyle(countstyle);
								cell.setCellValue(CommonUtil.nvl(report.getMonth_12()));

								cell = row.createCell(_col++);
								cell.setCellStyle(countstyle);
								cell.setCellValue(CommonUtil.nvl(report.getMonth()));

								cell = row.createCell(_col++);
								cell.setCellStyle(countstyle);
								cell.setCellValue(100 * CommonUtil.nvl(report.getMonth()) / totalcount);

								cell = row.createCell(_col++);
								cell.setCellStyle(simplestyle);
								cell.setCellValue(report.isTentative() ? "Yes" : "No");

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
				return "HotelReport" + FormatUtil.toString(new Date(), MyWebApplication.DATE_FORMAT) + ".xlsx";
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
		return new StringResourceModel("admin.reports.hotels", null);
	}
}
