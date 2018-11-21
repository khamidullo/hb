package uz.hbs.utils.email;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.stringtemplate.StringTemplate;
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
import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uz.hbs.MyWebApplication;
import uz.hbs.beans.AdditionalServicePrice;
import uz.hbs.beans.Insurance;
import uz.hbs.beans.Message;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.utils.CommonUtil;
import uz.hbs.utils.DateUtil;
import uz.hbs.utils.FormatUtil;

public class InsuranceEmailNotifier {
	private static final Logger logger = LoggerFactory.getLogger(InsuranceEmailNotifier.class);

	public static void send(final long additionalServiceOrderId) {
		List<Insurance> insuranceList = new MyBatisHelper().selectList("selectInsuranceListForEmail", additionalServiceOrderId);
		for (Insurance insurance : insuranceList) {
			try {
				sendInsuranceData(insurance);
			} catch (IOException e) {
				logger.error("IOException", e);
			}
		}
	}

	private static void sendInsuranceData(Insurance insurance) throws IOException {
		Map<String, String> attributes = new HashMap<String, String>();

		AdditionalServicePrice price = new MyBatisHelper().selectOne("selectCurrentAdditionalServicePrice");

		attributes.put("number", String.format("%05d", insurance.getEmail_sequence()));
		attributes.put("fio", insurance.getFullName());
		attributes.put("passport", insurance.getPassport_number());
		attributes.put("issue_date", DateUtil.toString(insurance.getPassport_issue_date(), MyWebApplication.DATE_FORMAT));
		attributes.put("birth_date", DateUtil.toString(insurance.getBirth_date(), MyWebApplication.DATE_FORMAT));
		attributes.put("from", DateUtil.toString(insurance.getPeriod_from_date(), MyWebApplication.DATE_FORMAT));
		attributes.put("till", DateUtil.toString(insurance.getPeriod_to_date(), MyWebApplication.DATE_FORMAT));
		attributes.put("days", String.valueOf(insurance.getDays()));
		attributes.put("amount", String.valueOf((price == null ? 1.5 : CommonUtil.nvl(price.getInsurance())) * insurance.getDays()));

		MyStringTemplateGroup templateGroup = new MyStringTemplateGroup("mailgroup");
		templateGroup.setFileCharEncoding("UTF-8");
		templateGroup.registerRenderer(Double.class, FormatUtil.getDoubleRenderer());
		templateGroup.registerRenderer(BigDecimal.class, FormatUtil.getBigDecimalRenderer());

		StringTemplate st = templateGroup.getInstanceOf("uz/hbs/utils/email/templates/insurance/Insurance.html", attributes);
		String html = st.toString();
		
		String creator_email = new MyBatisHelper().selectOne("selectCreatorEmail", insurance.getAdditionalserviceorders_id());

		sendEmail(price.getEmail_send_request_insurance(), html, String.valueOf(insurance.getEmail_sequence()), insurance.getFullName(), creator_email);
	}

	private static void sendEmail(final String recipient, final String content, final String number, final String fio, String creator_email) {
		new Thread() {
			@Override
			public void run() {
				String serverType = MyWebApplication.getConfigBundle().getString("server_type");
				if (serverType.equals("production")) {
					serverType = "";
				} else {
					serverType = "Тест. ";
				}

				Message message = new Message();
				message.setRecipient(recipient);
				if (creator_email != null) 
					message.setRecipient_cc(new String[]{ creator_email });
				message.setRecipient_bcc(new String[] { "request@hotelios.uz", "maqsudjon@gmail.com" });
				message.setSubject(serverType + "Страховой полис №" + number + ". " + fio + ".");
				message.setContent(content);
				try {
					message.setAttachment(Base64
							.decode("iVBORw0KGgoAAAANSUhEUgAAAB4AAAAeCAYAAAA7MK6iAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsQAAA7EAZUrDhsAAAAZdEVYdFNvZnR3YXJlAEFkb2JlIEltYWdlUmVhZHlxyWU8AAADIGlUWHRYTUw6Y29tLmFkb2JlLnhtcAAAAAAAPD94cGFja2V0IGJlZ2luPSLvu78iIGlkPSJXNU0wTXBDZWhpSHpyZVN6TlRjemtjOWQiPz4gPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iQWRvYmUgWE1QIENvcmUgNS4wLWMwNjAgNjEuMTM0Nzc3LCAyMDEwLzAyLzEyLTE3OjMyOjAwICAgICAgICAiPiA8cmRmOlJERiB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPiA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtbG5zOnhtcE1NPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvbW0vIiB4bWxuczpzdFJlZj0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL3NUeXBlL1Jlc291cmNlUmVmIyIgeG1wOkNyZWF0b3JUb29sPSJBZG9iZSBQaG90b3Nob3AgQ1M1IFdpbmRvd3MiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6MEU0NjE0NjYxQzE1MTFFNEFBOTFCNURGRUY2NTlCODgiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6MEU0NjE0NjcxQzE1MTFFNEFBOTFCNURGRUY2NTlCODgiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDowRTQ2MTQ2NDFDMTUxMUU0QUE5MUI1REZFRjY1OUI4OCIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDowRTQ2MTQ2NTFDMTUxMUU0QUE5MUI1REZFRjY1OUI4OCIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/Pm5DouQAAAQNSURBVEhLtVddiFRlGH6+78zMur/uX6uropnlIqtBRUHghkYi1UWg0I1QF0HgXVAQdRvhlYJ4oRdGEAT9XRRRF5U3XqQSEWlZFrmsuurkuro/4+rMOd/X855zxnN2zs8MA73sMLvnfc/7fM/7972rTPWmrZ3dC0DxR/Ob4t2F6t6M4thh2NosaudfA0wV0KVA3yjuIgob34EefgHelaPwrn8GOCuSduIDDkpbj0CZe9ds9dS2hFFx/EM6ehHe5SNwL76XDhg+VV2PoPT4d/zLovrTBOy96Wx7C3Q8+RVIkUwbRA/shB7aTT8ezPwvuaBQBbJ9l0R6YO9chK2W8+11gW61ACfFGd3H8xRh3Xk6u5DPdsV66P4J38ZUzvOwbj6wxIWsE8CqtAqq74ngZXeODP7NdaSHnwcKvYFN7WZT0LpBErhnnGQfCPRmiYVWyQfue4p6cUMafvG0Jkng4khU3crxQ54rxYFInVbJGS8nc8zk10WVRqC6NjWhUHeh2ILjrdENY7TMWArKD5tIYSX0wLP5ziQdoejeR6E6H2oJPMHY3vk7llcNZ2QPWyUsnhSXduHX2EH74Yy+0iZw5QLM4m9RuLvHCP5SpjPvxpes/oVQr6BXvQzVMdoUPKWPDbxrH0f9yAHhrNufydpW/oSZOxWri2E4G95sB5hdMfMtzEI0sVTXwyhseCPdGQeGjNWo7ZSfHtX7WC546uSS/vWmDsX6kiNu9T5W+FiqMzN3Bh4Pe19YE4UH324DmK+Y2R/4+T4KYXGIzt7KzvXUQd5kM1GFD0xw3u9K2Mu4lEswnXFo7k6+v9zZ4C7O5e2p4HbpH4b8qBw50KsSnPWS6+UQciXJXMoFltYyVz+K2sXphrPm1WzWVz+AXfwjYs2+1oM7kvZpl0SjlXvlGOzSZCyEOzgkMqYZ57p7ibVhayHrIpy1ryfDzaDkMvbfcG/BK38ahZCFo4eey2RtZk+Q9bmoNnq2cgLG5jljbVsCFsjy59H1yATp/meyK5asvfIX9w+qeInoweTYbc5YMnx3GnbhbMSic2OiaOIn8QcK7/KgyIrQnH6N0hKwVKqZOx0LdyeT1JHJ2lb+Yl1cCvWMbTzU4dMWgWldve7vYL5IM9Z/T4O3VW5As5EmZR1qHTgO4N4mcLNto75EGqZqqt1Q8z3ezX7nS8Dnf84uLl+juXz2BTYsNnPrZPvAql5Q3KvMzDe5wKo0BHB78bMiK69snw3SWqh5NaruLYGjyu8wt3/MB+YWogqDYg1v+niqLYHDNSfPFUdlsNIYuHIFNsmv6trs/wsjI9crf7LcsxQaJwiBpQjki0te2ke0svIyx7LmmBtfZ9vK+5LflU/zjC7cyQMBaN1vHUceWUpu3P4n5X+JjFOKcYOV6gAAAABJRU5ErkJggg=="
									.getBytes("UTF-8")));
				} catch (UnsupportedEncodingException e) {
					logger.error("Exception", e);
				}
				EmailUtil.sendHtmlEmail(message);
			};
		}.start();
	}

	public static void sendAttachedFileWithGuestList() {
		List<Insurance> list = new MyBatisHelper().selectList("selectInsuranceListForEmailByDay");
		if (!list.isEmpty()) {
			FileOutputStream fos = null;
			SXSSFWorkbook wb = new SXSSFWorkbook(100);
			wb.setCompressTempFiles(true);
			try {
				CellStyle headerStyle, dateStyle, normalStyle;

				headerStyle = wb.createCellStyle();

				dateStyle = wb.createCellStyle();
				normalStyle = wb.createCellStyle();

				Font boldFont = wb.createFont();
				boldFont.setFontHeightInPoints((short) 12);
				boldFont.setColor((short) 0);
				boldFont.setBold(true);

				headerStyle.setFont(boldFont);

				headerStyle.setBorderBottom(BorderStyle.THIN);
				headerStyle.setBorderTop(BorderStyle.THIN);
				headerStyle.setBorderLeft(BorderStyle.THIN);
				headerStyle.setBorderRight(BorderStyle.THIN);
				headerStyle.setAlignment(HorizontalAlignment.CENTER);
				headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
				headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				headerStyle.setWrapText(true);

				Font font = wb.createFont();
				font.setFontHeightInPoints((short) 12);
				font.setColor((short) 0);
				font.setBold(false);

				dateStyle.setFont(font);
				dateStyle.setBorderBottom(BorderStyle.THIN);
				dateStyle.setBorderTop(BorderStyle.THIN);
				dateStyle.setBorderLeft(BorderStyle.THIN);
				dateStyle.setBorderRight(BorderStyle.THIN);

				normalStyle.setFont(font);
				normalStyle.setBorderBottom(BorderStyle.THIN);
				normalStyle.setBorderTop(BorderStyle.THIN);
				normalStyle.setBorderLeft(BorderStyle.THIN);
				normalStyle.setBorderRight(BorderStyle.THIN);

				DataFormat format = wb.createDataFormat();
				dateStyle.setDataFormat(format.getFormat(MyWebApplication.DATE_FORMAT));

				Sheet s = wb.createSheet("Transactions");

				int cols = 0;
				int rows = 0;

				s.setColumnWidth(cols++, 2000); // №
				s.setColumnWidth(cols++, 8000); // Фамилия, имя
				s.setColumnWidth(cols++, 4000); // Дата рождения
				s.setColumnWidth(cols++, 4000); // Страна
				s.setColumnWidth(cols++, 4000); // Паспортные данные (серия, номер)
				s.setColumnWidth(cols++, 4000); // начало
				s.setColumnWidth(cols++, 4000); // конец
				s.setColumnWidth(cols++, 4000); // Программа покрытия
				s.setColumnWidth(cols++, 4000); // Страховая сумма (долл. США)
				s.setColumnWidth(cols++, 4000); // № Сервисной карты 1
				s.setColumnWidth(cols++, 4000); // № Сервисной карты 2

				Date reportDate = new MyBatisHelper().selectOne("selectCurrentDate");

				SimpleDateFormat sdf = new SimpleDateFormat(MyWebApplication.DATE_FORMAT);

				String title = "Информация о лицах, подлежащих страхованию, от " + sdf.format(reportDate);

				cols = 0;

				Row row = s.createRow(rows++);
				row.setHeightInPoints(30);

				// Title
				s.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), cols, cols + 10));
				Cell cell = row.createCell(cols);
				cell.setCellStyle(headerStyle);
				cell.setCellValue(title);

				// Headers
				row = s.createRow(rows++);
				row.setHeightInPoints(20);

				s.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum() + 1, cols, cols));
				cell = row.createCell(cols++);
				cell.setCellStyle(headerStyle);
				cell.setCellValue("№");

				s.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum() + 1, cols, cols));
				cell = row.createCell(cols++);
				cell.setCellStyle(headerStyle);
				cell.setCellValue("Фамилия, имя");

				s.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum() + 1, cols, cols));
				cell = row.createCell(cols++);
				cell.setCellStyle(headerStyle);
				cell.setCellValue("Дата рождения");

				s.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum() + 1, cols, cols));
				cell = row.createCell(cols++);
				cell.setCellStyle(headerStyle);
				cell.setCellValue("Страна");

				s.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum() + 1, cols, cols));
				cell = row.createCell(cols++);
				cell.setCellStyle(headerStyle);
				cell.setCellValue("Паспортные данные (серия, номер)");

				s.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), cols, cols + 1));
				cell = row.createCell(cols++);
				cell.setCellStyle(headerStyle);
				cell.setCellValue("Период действия страхования");

				cols++;

				s.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum() + 1, cols, cols));
				cell = row.createCell(cols++);
				cell.setCellStyle(headerStyle);
				cell.setCellValue("Программа покрытия");

				s.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum() + 1, cols, cols));
				cell = row.createCell(cols++);
				cell.setCellStyle(headerStyle);
				cell.setCellValue("Страховая сумма (долл. США)");

				s.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum() + 1, cols, cols + 1));
				cell = row.createCell(cols++);
				cell.setCellStyle(headerStyle);
				cell.setCellValue("№ Сервисной карты");

				cols = 0;

				row = s.createRow(rows++);
				row.setHeightInPoints(20);

				cell = row.createCell((++cols) + 4);
				cell.setCellStyle(headerStyle);
				cell.setCellValue("начало");

				cell = row.createCell((++cols) + 4);
				cell.setCellStyle(headerStyle);
				cell.setCellValue("конец");

				int count = 1;
				for (Insurance insurance : list) {
					cols = 0;

					row = s.createRow(rows++);

					cell = row.createCell(cols++);
					cell.setCellStyle(normalStyle);
					cell.setCellValue(count);

					cell = row.createCell(cols++);
					cell.setCellStyle(normalStyle);
					cell.setCellValue(insurance.getLast_name() + " " + insurance.getFirst_name());

					cell = row.createCell(cols++);
					cell.setCellStyle(dateStyle);
					cell.setCellValue(insurance.getBirth_date());

					cell = row.createCell(cols++);
					cell.setCellStyle(normalStyle);
					cell.setCellValue(insurance.getNationality().getName());

					cell = row.createCell(cols++);
					cell.setCellStyle(normalStyle);
					cell.setCellValue(insurance.getPassport_number());

					cell = row.createCell(cols++);
					cell.setCellStyle(dateStyle);
					cell.setCellValue(insurance.getPeriod_from_date());

					cell = row.createCell(cols++);
					cell.setCellStyle(dateStyle);
					cell.setCellValue(insurance.getPeriod_to_date());

					cell = row.createCell(cols++);
					cell.setCellStyle(normalStyle);
					cell.setCellValue("Hotelios");

					cell = row.createCell(cols++);
					cell.setCellStyle(normalStyle);
					cell.setCellValue("2 000,00");

					cell = row.createCell(cols++);
					cell.setCellStyle(normalStyle);
					cell.setCellValue("0202/1500679-");

					cell = row.createCell(cols++);
					cell.setCellStyle(normalStyle);
					cell.setCellValue(String.format("%05d", insurance.getEmail_sequence()));

					count++;
				}

				ByteArrayOutputStream baos = new ByteArrayOutputStream();

				wb.write(baos);

				byte[] bytes = baos.toByteArray();

				baos.close();

				new Thread() {
					@Override
					public void run() {
						AdditionalServicePrice additionalServicePrice = new MyBatisHelper().selectOne("selectCurrentAdditionalServicePrice");
						Message message = new Message();
						message.setRecipient(additionalServicePrice.getEmail_send_request_insurance());
						message.setRecipient_bcc(new String[] { "request@hotelios.uz", "maqsudjon@gmail.com" });
						message.setSubject("Информация о лицах, подлежащих страхованию по программе Hotelios");
						message.setContent("Информация о лицах, подлежащих страхованию по программе Hotelios \n" + sdf.format(reportDate));
						try {
							message.setAttachment(bytes);
							sdf.applyPattern("yyyy-MM-dd");
							message.setAttachment_name("Insurance_list_" + sdf.format(reportDate) + ".xlsx");
						} catch (Exception e) {
							logger.error("Exception", e);
						}
						EmailUtil.sendEmailWithAttachment(message);
					};
				}.start();

				logger.info("Report generated: " + title);
			} catch (Exception e) {
				logger.error("Exception", e);
			} finally {
				try {
					wb.close();
					if (fos != null) {
						fos.close();
					}
				} catch (IOException e) {
					logger.error("Exception", e);
				}
			}
		} else {
			logger.debug("No info to send insurance company");
		}
	}

	public static void main(String[] args) {
		sendAttachedFileWithGuestList();
	}
}
