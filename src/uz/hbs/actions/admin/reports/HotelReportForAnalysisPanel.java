package uz.hbs.actions.admin.reports;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.HeadersToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.DownloadLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.MyWebApplication;
import uz.hbs.beans.Address;
import uz.hbs.beans.City;
import uz.hbs.beans.Contract;
import uz.hbs.beans.Country;
import uz.hbs.beans.GeneratedReport;
import uz.hbs.beans.Hotel;
import uz.hbs.beans.Region;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.db.dataproviders.SortableGeneratedReportDataProvider;
import uz.hbs.markup.html.breadcrumb.MyBreadCrumbPanel;
import uz.hbs.markup.html.form.alertbox.MyFeedbackPanel;
import uz.hbs.markup.navigation.BootstrapPagingNavigator.Size;
import uz.hbs.markup.navigation.BootstrapPagingNavigatorToolbar;
import uz.hbs.markup.navigation.repeater.data.table.MyNoRecordsToolbar;
import uz.hbs.utils.DateUtil;
import uz.hbs.utils.FileUtil;
import uz.hbs.utils.FormatUtil;
import uz.hbs.utils.models.HotelModels;

public class HotelReportForAnalysisPanel extends MyBreadCrumbPanel {
	private static final long serialVersionUID = 1L;

	public HotelReportForAnalysisPanel(String id, IBreadCrumbModel breadCrumbModel) {
		super(id, breadCrumbModel);

		MyFeedbackPanel feedback = new MyFeedbackPanel("feedback");
		add(feedback);
		feedback.setOutputMarkupId(true);

		Address address = new Address();
		Form<Address> form = new Form<>("form", new CompoundPropertyModel<Address>(address));
		form.setOutputMarkupId(true);
		add(form);

		LoadableDetachableModel<List<? extends Country>> countriesList = HotelModels.getCountriesHasHotelList("ru");

		final DropDownChoice<Country> countries = new DropDownChoice<Country>("countries", countriesList, new ChoiceRenderer<Country>("name", "id"));
		countries.setLabel(new StringResourceModel("hotels.details.country", null));
		countries.setRequired(true);
		form.add(countries);

		LoadableDetachableModel<List<? extends Region>> regionsList = new LoadableDetachableModel<List<? extends Region>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<? extends Region> load() {
				Country country = form.getModelObject().getCountries();
				Map<String, Serializable> params = new HashMap<String, Serializable>();
				params.put("countries_id", country != null ? country.getId() : -1);
				params.put("sortField", "r.name");
				params.put("sortOrder", "ASC");
				return new MyBatisHelper().selectList("selectRegionsList", params);
			}
		};

		final DropDownChoice<Region> regions = new DropDownChoice<Region>("regions", regionsList, new ChoiceRenderer<Region>("name", "id"));
		regions.setLabel(new StringResourceModel("hotels.details.region", null));
		regions.setRequired(true);
		regions.setOutputMarkupId(true);
		form.add(regions);

		LoadableDetachableModel<List<? extends City>> citiesList = new LoadableDetachableModel<List<? extends City>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<? extends City> load() {
				Region region = form.getModelObject().getRegions();
				Map<String, Serializable> params = new HashMap<String, Serializable>();
				params.put("regions_id", region != null ? region.getId() : -1);
				params.put("sortField", "ct.name");
				params.put("sortOrder", "ASC");

				return new MyBatisHelper().selectList("selectCitiesList", params);
			}
		};

		final DropDownChoice<City> cities = new DropDownChoice<City>("cities", citiesList, new ChoiceRenderer<City>("name", "id"));
		cities.setLabel(new StringResourceModel("hotels.details.city", null));
		cities.setRequired(true);
		cities.setOutputMarkupId(true);
		form.add(cities);

		countries.add(new OnChangeAjaxBehavior() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				form.getModelObject().setRegions(null);
				form.getModelObject().setCities(null);
				target.add(regions);
				target.add(cities);
			}
		});

		regions.add(new OnChangeAjaxBehavior() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				form.getModelObject().setCities(null);
				target.add(cities);
			}
		});

		WebMarkupContainer container = new WebMarkupContainer("container");
		container.setOutputMarkupId(true);
		add(container);

		IndicatingAjaxButton submit = new IndicatingAjaxButton("submit") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				super.onSubmit(target, form);

				try {
					String fileExtension = ".xlsx";
					String fileName = "Hotels_" + ((Address) form.getModelObject()).getCities().getId() + "_" + FormatUtil.toString(new Date(), "yyyyMMddHHmmss");

					write(new FileOutputStream(FileUtil.getReportFolder() + "/" + fileName + fileExtension),
							((Address) form.getModelObject()).getCities().getId());

					GeneratedReport generatedReport = new GeneratedReport();
					generatedReport.setCities_id(((Address) form.getModelObject()).getCities().getId());
					generatedReport.setName(fileName);
					generatedReport.setInitiator_user_id(getMySession().getUser().getId());
					generatedReport.setLink(fileName + fileExtension);

					new MyBatisHelper().insert("insertGeneratedReport", generatedReport);

					feedback.success("Report has been generated successfully");
				} catch (Exception e) {
					feedback.error("Error, Report generation failed");
					logger.error("Exception", e);
				}

				target.add(container);
				target.add(feedback);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				super.onError(target, form);

				target.add(feedback);
			}
		};
		form.add(submit);

		ArrayList<IColumn<GeneratedReport, String>> columns = new ArrayList<IColumn<GeneratedReport, String>>();

		columns.add(new PropertyColumn<GeneratedReport, String>(new StringResourceModel("date", null), "rt.create_date", "create_date") {
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<GeneratedReport>> item, String componentId, IModel<GeneratedReport> rowModel) {
				item.add(new Label(componentId, DateUtil.toString(rowModel.getObject().getCreate_date(), MyWebApplication.DATE_TIME_FORMAT)));
			}
		});
		columns.add(new PropertyColumn<GeneratedReport, String>(new StringResourceModel("address.city", null, new Object[] { "" }), "city_name"));
		columns.add(new PropertyColumn<GeneratedReport, String>(new StringResourceModel("report.link", null), "rt.link", "link") {
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<GeneratedReport>> item, String componentId, IModel<GeneratedReport> rowModel) {
				item.add(new DownloadLinkPanel(componentId, rowModel));
			}
		});
		columns.add(new PropertyColumn<GeneratedReport, String>(new StringResourceModel("author", null), "u.name", "author"));

		GeneratedReport filter = new GeneratedReport();

		SortableGeneratedReportDataProvider provider = new SortableGeneratedReportDataProvider();
		provider.setFilterState(filter);

		final DataTable<GeneratedReport, String> dataTable = new DataTable<GeneratedReport, String>("table", columns, provider,
				getMySession().getSettings().getTable_rows());
		dataTable.setOutputMarkupId(true);

		final FilterForm<GeneratedReport> filterForm = new FilterForm<GeneratedReport>("filterForm", provider) {
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

		filterForm.add(dataTable);
		dataTable.addTopToolbar(new BootstrapPagingNavigatorToolbar(dataTable, Size.Small));
		dataTable.addTopToolbar(new HeadersToolbar<String>(dataTable, provider));
		//		dataTable.addTopToolbar(new FilterToolbar(dataTable, filterForm, provider));
		dataTable.addBottomToolbar(new BootstrapPagingNavigatorToolbar(dataTable, Size.Small));
		dataTable.addBottomToolbar(new MyNoRecordsToolbar(dataTable));

		container.add(filterForm);
	}

	public void write(FileOutputStream os, Integer cityId) throws IOException {
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
			row.setHeight((short) 1200);

			Cell cell = row.createCell(_col++);
			cell.setCellStyle(headerStyle);
			cell.setCellValue("ID");
			s.setColumnWidth(cell.getColumnIndex(), 5000);

			cell = row.createCell(_col++);
			cell.setCellStyle(headerStyle);
			cell.setCellValue("Статус");
			s.setColumnWidth(cell.getColumnIndex(), 6000);

			cell = row.createCell(_col++);
			cell.setCellStyle(headerStyle);
			cell.setCellValue("Название гостиницы");
			s.setColumnWidth(cell.getColumnIndex(), 6000);

			cell = row.createCell(_col++);
			cell.setCellStyle(headerStyle);
			cell.setCellValue("Юридическое название гостиницы");
			s.setColumnWidth(cell.getColumnIndex(), 5000);

			cell = row.createCell(_col++);
			cell.setCellStyle(headerStyle);
			cell.setCellValue("Город");
			s.setColumnWidth(cell.getColumnIndex(), 5000);

			cell = row.createCell(_col++);
			cell.setCellStyle(headerStyle);
			cell.setCellValue("Основной контактный номер");
			s.setColumnWidth(cell.getColumnIndex(), 5000);

			cell = row.createCell(_col++);
			cell.setCellStyle(headerStyle);
			cell.setCellValue("Электронный адрес");
			s.setColumnWidth(cell.getColumnIndex(), 5000);

			cell = row.createCell(_col++);
			cell.setCellStyle(headerStyle);
			cell.setCellValue("Тип конракта");
			s.setColumnWidth(cell.getColumnIndex(), 5000);

			cell = row.createCell(_col++);
			cell.setCellStyle(headerStyle);
			cell.setCellValue("Номер конракта");
			s.setColumnWidth(cell.getColumnIndex(), 5000);

			cell = row.createCell(_col++);
			cell.setCellStyle(headerStyle);
			cell.setCellValue("Дата контракта");
			s.setColumnWidth(cell.getColumnIndex(), 5000);

			cell = row.createCell(_col++);
			cell.setCellStyle(headerStyle);
			cell.setCellValue("Комиссия");
			s.setColumnWidth(cell.getColumnIndex(), 5000);

			cell = row.createCell(_col++);
			cell.setCellStyle(headerStyle);
			cell.setCellValue("ФИО Руководителя");
			s.setColumnWidth(cell.getColumnIndex(), 5000);

			cell = row.createCell(_col++);
			cell.setCellStyle(headerStyle);
			cell.setCellValue("Электронный адрес руководителя");
			s.setColumnWidth(cell.getColumnIndex(), 5000);

			cell = row.createCell(_col++);
			cell.setCellStyle(headerStyle);
			cell.setCellValue("Бухгалтер");
			s.setColumnWidth(cell.getColumnIndex(), 5000);

			cell = row.createCell(_col++);
			cell.setCellStyle(headerStyle);
			cell.setCellValue("Телефон бухгалтера");
			s.setColumnWidth(cell.getColumnIndex(), 5000);
			
			cell = row.createCell(_col++);
			cell.setCellStyle(headerStyle);
			cell.setCellValue("Отдел бронирования");
			s.setColumnWidth(cell.getColumnIndex(), 5000);
			
			cell = row.createCell(_col++);
			cell.setCellStyle(headerStyle);
			cell.setCellValue("Телефон отдела бронирования");
			s.setColumnWidth(cell.getColumnIndex(), 5000);
			
			cell = row.createCell(_col++);
			cell.setCellStyle(headerStyle);
			cell.setCellValue("Электронный адрес руководства");
			s.setColumnWidth(cell.getColumnIndex(), 5000);
			
			cell = row.createCell(_col++);
			cell.setCellStyle(headerStyle);
			cell.setCellValue("Электронный адрес отдела бронирования");
			s.setColumnWidth(cell.getColumnIndex(), 5000);
			
			cell = row.createCell(_col++);
			cell.setCellStyle(headerStyle);
			cell.setCellValue("Телефон отдела бронирования");
			s.setColumnWidth(cell.getColumnIndex(), 5000);

			cell = row.createCell(_col++);
			cell.setCellStyle(headerStyle);
			cell.setCellValue("Телефон ресепшена");
			s.setColumnWidth(cell.getColumnIndex(), 5000);

			cell = row.createCell(_col++);
			cell.setCellStyle(headerStyle);
			cell.setCellValue("Факс 1");
			s.setColumnWidth(cell.getColumnIndex(), 5000);

			cell = row.createCell(_col++);
			cell.setCellStyle(headerStyle);
			cell.setCellValue("Факс 2");
			s.setColumnWidth(cell.getColumnIndex(), 5000);

			List<Hotel> list = new MyBatisHelper().selectList("selectHotelsForAnalysis", cityId);

			for (Hotel report : list) {
				_col = 0;

				row = s.createRow(_row++);

				cell = row.createCell(_col++);
				cell.setCellStyle(simplestyle);
				cell.setCellValue(report.getUsers_id());

				cell = row.createCell(_col++);
				cell.setCellStyle(simplestyle);
				cell.setCellValue(getString("users.status" + report.getStatus()));

				cell = row.createCell(_col++);
				cell.setCellStyle(simplestyle);
				cell.setCellValue(report.getLegal_name());

				cell = row.createCell(_col++);
				cell.setCellStyle(simplestyle);
				cell.setCellValue(report.getDisplay_name());

				cell = row.createCell(_col++);
				cell.setCellStyle(simplestyle);
				cell.setCellValue(report.getCity());

				cell = row.createCell(_col++);
				cell.setCellStyle(simplestyle);
				cell.setCellValue(report.getPrimary_phone());

				cell = row.createCell(_col++);
				cell.setCellStyle(simplestyle);
				cell.setCellValue(report.getCorporate_email());

				cell = row.createCell(_col++);
				cell.setCellStyle(simplestyle);
				cell.setCellValue((report.getContract().getContract_type() != null
						? getString("touragents.contract.type." + report.getContract().getContract_type().getId())
						: null));

				cell = row.createCell(_col++);
				cell.setCellStyle(simplestyle);
				cell.setCellValue(report.getContract().getContract_number());

				cell = row.createCell(_col++);
				cell.setCellStyle(datestyle);
				cell.setCellValue(report.getContract().getContract_date());

				cell = row.createCell(_col++);
				cell.setCellStyle(simplestyle);
				cell.setCellValue(report.getContract().getCommission_value() != null ? report.getContract().getCommission_value()
						+ (report.getContract().getCommission_type() == Contract.COMMISSION_TYPE_PERCENTAGE ? "%" : "сум") : null);

				cell = row.createCell(_col++);
				cell.setCellStyle(simplestyle);
				cell.setCellValue(report.getManager());

				cell = row.createCell(_col++);
				cell.setCellStyle(simplestyle);
				cell.setCellValue(report.getManager_email());

				cell = row.createCell(_col++);
				cell.setCellStyle(simplestyle);
				cell.setCellValue(report.getAccountant());

				cell = row.createCell(_col++);
				cell.setCellStyle(simplestyle);
				cell.setCellValue(report.getAccountant_phone());

				cell = row.createCell(_col++);
				cell.setCellStyle(simplestyle);
				cell.setCellValue(report.getReservation_dep());

				cell = row.createCell(_col++);
				cell.setCellStyle(simplestyle);
				cell.setCellValue(report.getReservation_dep_phone());

				cell = row.createCell(_col++);
				cell.setCellStyle(simplestyle);
				cell.setCellValue(report.getHotelsDetails().getContact_email2());

				cell = row.createCell(_col++);
				cell.setCellStyle(simplestyle);
				cell.setCellValue(report.getHotelsDetails().getContact_email());

				cell = row.createCell(_col++);
				cell.setCellStyle(simplestyle);
				cell.setCellValue(report.getHotelsDetails().getContact_number());

				cell = row.createCell(_col++);
				cell.setCellStyle(simplestyle);
				cell.setCellValue(report.getHotelsDetails().getContact_number2());

				cell = row.createCell(_col++);
				cell.setCellStyle(simplestyle);
				cell.setCellValue(report.getHotelsDetails().getFax());

				cell = row.createCell(_col++);
				cell.setCellStyle(simplestyle);
				cell.setCellValue(report.getHotelsDetails().getFax2());

			}
		} finally {
			wb.write(os);
			wb.close();
		}
	}

	private class DownloadLinkPanel extends Panel {
		private static final long serialVersionUID = 1L;

		public DownloadLinkPanel(String id, IModel<GeneratedReport> model) {
			super(id, model);
			DownloadLink link = new DownloadLink("link", new File(FileUtil.getReportFolder() + "/" + model.getObject().getLink()),
					model.getObject().getLink());
			add(link);
			link.add(new Label("label", model.getObject().getLink()).setRenderBodyOnly(true));
		}
	}

	@Override
	public Class<?> implementedClass() {
		return this.getClass();
	}

	@Override
	public IModel<String> getTitle() {
		return new StringResourceModel("admin.reports.hotels_for_analysis", null);
	}
}
