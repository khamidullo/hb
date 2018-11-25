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
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.value.ValueMap;

import uz.hbs.MyWebApplication;
import uz.hbs.actions.hotel.reports.ReservationReportPanel;
import uz.hbs.beans.ReservationStatus;
import uz.hbs.beans.ReservationType;
import uz.hbs.beans.TourAgent;
import uz.hbs.beans.reports.HotelReservationReport;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.session.MySession;
import uz.hbs.utils.AJAXDownload;
import uz.hbs.utils.FormatUtil;

public class ReservationByTAReportPanel extends ReservationReportPanel {
	private static final long serialVersionUID = 1L;

	public ReservationByTAReportPanel(String id, IBreadCrumbModel breadCrumbModel, final ValueMap model) {
		super(id, breadCrumbModel, model);
		
		model.put("hotel_id", ((MySession) getSession()).getUser().getHotelsusers_id());
		final List<TourAgent> touragentlist = new MyBatisHelper().selectList("selectReportTourAgentList", model); 
		
		add(new ListView<TourAgent>("touragentlist", new LoadableDetachableModel<List<TourAgent>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<TourAgent> load() {
				return touragentlist;
			}
		}) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<TourAgent> tourAgentItem) {
				tourAgentItem.add(new Label("tour_agent", tourAgentItem.getModelObject().getName()));
				IModel<List<HotelReservationReport>> reportListModel = new LoadableDetachableModel<List<HotelReservationReport>>() {
					private static final long serialVersionUID = 1L;

					@Override
					protected List<HotelReservationReport> load() {
						model.put("touragentuser_id", tourAgentItem.getModelObject().getUsers_id());
						model.put("hotel_id", ((MySession) getSession()).getUser().getHotelsusers_id());
						return new MyBatisHelper().selectList("selectReservationByTAReport", model);
					}
				};
				tourAgentItem.add(new ListView<HotelReservationReport>("reportlist", reportListModel) {
					private static final long serialVersionUID = 1L;

					@Override
					protected void populateItem(ListItem<HotelReservationReport> item) {
						final HotelReservationReport report = (HotelReservationReport) item.getDefaultModelObject();
						item.add(new Label("id", report.getId()));
						item.add(new Label("create_date", FormatUtil.toString(report.getCreate_date(), "dd/MM/yyyy")));
						item.add(new Label("guest_name", report.getFirst_name() +  " " + report.getLast_name()));
						item.add(new Label("check_in", FormatUtil.toString(report.getCheck_in(), "dd/MM/yyyy")));
						item.add(new Label("check_out", FormatUtil.toString(report.getCheck_out(), "dd/MM/yyyy")));
						item.add(new Label("status", new AbstractReadOnlyModel<String>() {
							private static final long serialVersionUID = 1L;

							@Override
							public String getObject() {
								if (report.getStatus() == ReservationStatus.CHECKED_IN) return  getString("hotels.reservation.details.reservation.status.active");
								else if (report.getStatus() == ReservationStatus.CANCELLED) return getString("hotels.reservation.details.reservation.status.cancelled");
								return null;
							}
						}));
						item.add(new Label("type", new AbstractReadOnlyModel<String>() {
							private static final long serialVersionUID = 1L;
							
							@Override
							public String getObject() {
								if (report.getReserve_type() == ReservationType.DEFINITE) return getString("hotels.reservation.details.reservation.type.definite");
								else if (report.getReserve_type() == ReservationType.TENTATIVE) return getString("hotels.reservation.details.reservation.type.tentative");
								return null;
							}
						}));
						item.add(new Label("total", FormatUtil.toString(report.getTotal())));
					}
				});
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
							
							Sheet s = wb.createSheet("ReservationByTA");
					        int _col = 0;
					        int _row = 0;

					        s.setColumnWidth(_col++, 5000); //Date/Time
					        s.setColumnWidth(_col++, 8000); //Provider
					        s.setColumnWidth(_col++, 8000); //Service
					        s.setColumnWidth(_col++, 6000); //Amount
					        s.setColumnWidth(_col++, 6000); //Count
					        
					        Row row = s.createRow(_row);
					        
					        _col = 0;
					        
					        Cell cell = row.createCell(_col++);
							cell.setCellStyle(headerStyle);
							cell.setCellValue(getString("hotels.reservation.report.reservation.id"));
							
							cell = row.createCell(_col++);
							cell.setCellStyle(headerStyle);
							cell.setCellValue(getString("hotels.reservation.report.reservation.created"));
							
							cell = row.createCell(_col++);
							cell.setCellStyle(headerStyle);
							cell.setCellValue(getString("hotels.reservation.report.reservation.guest_name"));
							
					        cell = row.createCell(_col++);
					        cell.setCellStyle(headerStyle);
					        cell.setCellValue(getString("hotels.reservation.report.reservation.check_in"));
							
					        cell = row.createCell(_col++);
					        cell.setCellStyle(headerStyle);
					        cell.setCellValue(getString("hotels.reservation.report.reservation.check_out"));
							
							cell = row.createCell(_col++);
							cell.setCellStyle(headerStyle);
							cell.setCellValue(getString("hotels.reservation.report.reservation.status"));
							
							cell = row.createCell(_col++);
							cell.setCellStyle(headerStyle);
							cell.setCellValue(getString("hotels.reservation.report.reservation.type"));
							
							cell = row.createCell(_col++);
							cell.setCellStyle(headerStyle);
							cell.setCellValue(getString("hotels.reservation.report.reservation.total"));
					        
							_col = 0;
							
							_row ++;
					        for (TourAgent touragent : touragentlist) {
						        row = s.createRow(_row++);
						        
								cell = row.createCell(_col++);
								cell.setCellStyle(headerStyle);
								cell.setCellValue(touragent.getName());
								s.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 0, 7));
								
								model.put("touragentuser_id", touragent.getUsers_id());
								model.put("hotel_id", ((MySession) getSession()).getUser().getHotelsusers_id());
								List<HotelReservationReport> list = new MyBatisHelper().selectList("selectReservationByTAReport", model);
								
								_col = 0;
								
								for (HotelReservationReport report : list) {
									row = s.createRow(_row++);

									cell = row.createCell(_col++);
									cell.setCellStyle(countstyle);
									cell.setCellValue(report.getId());

									cell = row.createCell(_col++);
									cell.setCellStyle(datetimestyle);
									cell.setCellValue(report.getCreate_date());

									cell = row.createCell(_col++);
									cell.setCellStyle(simplestyle);
									cell.setCellValue(report.getFirst_name() +  " " + report.getLast_name());
									
							        cell = row.createCell(_col++);
							        cell.setCellStyle(datetimestyle);
							        cell.setCellValue(report.getCheck_in());
							        
							        cell = row.createCell(_col++);
							        cell.setCellStyle(datetimestyle);
							        cell.setCellValue(report.getCheck_out());

									if (report.getStatus() == ReservationStatus.CHECKED_IN) {
										cell = row.createCell(_col++);
										cell.setCellStyle(simplestyle);
										cell.setCellValue(getString("hotels.reservation.details.reservation.status.active"));
									} else if (report.getStatus() == ReservationStatus.CANCELLED) {
										cell = row.createCell(_col++);
										cell.setCellStyle(simplestyle);
										cell.setCellValue(getString("hotels.reservation.details.reservation.status.cancelled"));
									} else {
										cell = row.createCell(_col++);
										cell.setCellStyle(simplestyle);
										cell.setCellValue("");
									}
									if (report.getReserve_type() == ReservationType.DEFINITE) {
										cell = row.createCell(_col++);
										cell.setCellStyle(simplestyle);
										cell.setCellValue(getString("hotels.reservation.details.reservation.type.definite"));
									} else {
										cell = row.createCell(_col++);
										cell.setCellStyle(simplestyle);
										cell.setCellValue(getString("hotels.reservation.details.reservation.type.tentative"));
									}

									cell = row.createCell(_col++);
									cell.setCellStyle(moneystyle);
									cell.setCellValue(report.getTotal());

									_col = 0;
								}
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
				return "ReservationByTA" + FormatUtil.toString(new Date(), MyWebApplication.DATE_FORMAT) + ".xlsx";
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
