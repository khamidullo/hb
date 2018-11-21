package uz.hbs.beans.reports;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.model.StringResourceModel;

import uz.hbs.beans.IdByteAndName;

public class HotelReservationReportType extends IdByteAndName {
	private static final long serialVersionUID = 1L;
	public static final byte ALL_RESERVATIONS = 0;
	public static final byte RESERVATION_BY_TA = 1;
	public static final byte GROUP_FROM_TA = 2;
	public static final byte REGISTERED_GUEST = 3;
	public static final byte LEAVING_GUESTS = 4;
	public static final byte LEFT_GUESTS = 5;
	public static final byte EXPECTED_GUESTS = 6;
	public static final byte STATISTIC_LIST_BY_TA = 7;
	public static final byte STATISTIC_BY_COUNTRIES = 8;
	public static final byte PAYMENT_TYPES = 9;
	public static final byte FORECAST_REPORT = 10;
	public static final byte FORECAST_REPORT_BY_TA = 11;
	public static final byte ISSUED_BILLS = 12;
	public static final byte COMMISSION_SEPARATED = 13;
	
	public static final String ALL_RESERVATIONS_REPORT = "hotels.reservation.report.all_reservations";
	public static final String RESERVATION_BY_TA_REPORT = "hotels.reservation.report.reservation_by_ta";
	public static final String GROUP_FROM_TA_REPORT = "hotels.reservation.report.group_from_ta";
	public static final String REGISTERED_GUEST_REPORT = "hotels.reservation.report.registered_guests";
	public static final String LEAVING_GUESTS_REPORT = "hotels.reservation.report.leaving_guests";
	public static final String LEFT_GUESTS_REPORT = "hotels.reservation.report.left_guests";
	public static final String EXPECTED_GUESTS_REPORT = "hotels.reservation.report.expected_guests";
	public static final String STATISTIC_LIST_BY_TA_REPORT = "hotels.reservation.report.statistic_list_by_ta";
	public static final String STATISTIC_BY_COUNTRIES_REPORT = "hotels.reservation.report.statistic_by_countries";
	public static final String PAYMENT_TYPES_REPORT = "hotels.reservation.report.payment_types";
	public static final String FORECAST_REPORT_REPORT = "hotels.reservation.report.forecast_report";
	public static final String FORECAST_REPORT_BY_TA_REPORT = "hotels.reservation.report.forecast_report_by_ta";
	public static final String ISSUED_BILLS_REPORT = "hotels.reservation.report.issued_bills";
	public static final String COMMISSION_SEPARATED_REPORT = "hotels.reservation.report.commission_separated";
	
	public HotelReservationReportType() {
	}
	
	public HotelReservationReportType(byte id) {
		this.id = id;
	}
	
	public HotelReservationReportType(byte id, String key) {
		this.id = id;
		this.name = new StringResourceModel(key, null).getString();
	}
	
	public static List<HotelReservationReportType> getReservationList(){
		List<HotelReservationReportType> list = new ArrayList<HotelReservationReportType>();
		list.add(new HotelReservationReportType(ALL_RESERVATIONS,ALL_RESERVATIONS_REPORT));
		list.add(new HotelReservationReportType(RESERVATION_BY_TA,RESERVATION_BY_TA_REPORT));
		list.add(new HotelReservationReportType(GROUP_FROM_TA,GROUP_FROM_TA_REPORT));
		list.add(new HotelReservationReportType(REGISTERED_GUEST,REGISTERED_GUEST_REPORT));
		list.add(new HotelReservationReportType(LEAVING_GUESTS,LEAVING_GUESTS_REPORT));
		list.add(new HotelReservationReportType(LEFT_GUESTS,LEFT_GUESTS_REPORT));
		list.add(new HotelReservationReportType(EXPECTED_GUESTS,EXPECTED_GUESTS_REPORT));
		list.add(new HotelReservationReportType(STATISTIC_LIST_BY_TA,STATISTIC_LIST_BY_TA_REPORT));
		list.add(new HotelReservationReportType(STATISTIC_BY_COUNTRIES,STATISTIC_BY_COUNTRIES_REPORT));
		list.add(new HotelReservationReportType(PAYMENT_TYPES,PAYMENT_TYPES_REPORT));
		list.add(new HotelReservationReportType(FORECAST_REPORT,FORECAST_REPORT_REPORT));
		list.add(new HotelReservationReportType(FORECAST_REPORT_BY_TA,FORECAST_REPORT_BY_TA_REPORT));
		list.add(new HotelReservationReportType(ISSUED_BILLS,ISSUED_BILLS_REPORT));
		list.add(new HotelReservationReportType(COMMISSION_SEPARATED,COMMISSION_SEPARATED_REPORT));
		return list;
	}
}
