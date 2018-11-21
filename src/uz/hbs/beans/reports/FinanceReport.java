package uz.hbs.beans.reports;

import java.util.Date;

import org.apache.wicket.util.io.IClusterable;

import uz.hbs.beans.IdLongAndName;
import uz.hbs.utils.CommonUtil;

public class FinanceReport implements IClusterable {
	private static final long serialVersionUID = 1L;
	private Long users_id;
	private String manager;
	private String company;
	private Date regdate;
	private Double hotel_1;
	private Double service_1;
	private Double commission_1;
	private Double hotel_2;
	private Double service_2;
	private Double commission_2;
	private Double hotel_3;
	private Double service_3;
	private Double commission_3;
	private Double hotel_4;
	private Double service_4;
	private Double commission_4;
	private Double hotel_5;
	private Double service_5;
	private Double commission_5;
	private Double hotel_6;
	private Double service_6;
	private Double commission_6;
	private Double hotel_7;
	private Double service_7;
	private Double commission_7;
	private Double hotel_8;
	private Double service_8;
	private Double commission_8;
	private Double hotel_9;
	private Double service_9;
	private Double commission_9;
	private Double hotel_10;
	private Double service_10;
	private Double commission_10;
	private Double hotel_11;
	private Double service_11;
	private Double commission_11;
	private Double hotel_12;
	private Double service_12;
	private Double commission_12;
	private Double hotel;
	private Double service;
	
	public FinanceReport() {
	}
	
	public FinanceReport(IdLongAndName obj) {
		users_id = obj.getId();
		manager = obj.getName();
	}
	
	public FinanceReport(long users_id, String company, Date regdate) {
		this.users_id = users_id;
		this.company = company;
		this.regdate = regdate;
	}

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public Double getHotel_1() {
		return hotel_1;
	}

	public void setHotel_1(Double hotel_1) {
		this.hotel_1 = hotel_1;
	}

	public Double getService_1() {
		return service_1;
	}

	public void setService_1(Double service_1) {
		this.service_1 = service_1;
	}

	public Double getCommission_1() {
		return commission_1;
	}

	public void setCommission_1(Double commission_1) {
		this.commission_1 = commission_1;
	}

	public Double getHotel_2() {
		return hotel_2;
	}

	public void setHotel_2(Double hotel_2) {
		this.hotel_2 = hotel_2;
	}

	public Double getService_2() {
		return service_2;
	}

	public void setService_2(Double service_2) {
		this.service_2 = service_2;
	}

	public Double getCommission_2() {
		return commission_2;
	}

	public void setCommission_2(Double commission_2) {
		this.commission_2 = commission_2;
	}

	public Double getHotel_3() {
		return hotel_3;
	}

	public void setHotel_3(Double hotel_3) {
		this.hotel_3 = hotel_3;
	}

	public Double getService_3() {
		return service_3;
	}

	public void setService_3(Double service_3) {
		this.service_3 = service_3;
	}

	public Double getCommission_3() {
		return commission_3;
	}

	public void setCommission_3(Double commission_3) {
		this.commission_3 = commission_3;
	}

	public Double getHotel_4() {
		return hotel_4;
	}

	public void setHotel_4(Double hotel_4) {
		this.hotel_4 = hotel_4;
	}

	public Double getService_4() {
		return service_4;
	}

	public void setService_4(Double service_4) {
		this.service_4 = service_4;
	}

	public Double getCommission_4() {
		return commission_4;
	}

	public void setCommission_4(Double commission_4) {
		this.commission_4 = commission_4;
	}

	public Double getHotel_5() {
		return hotel_5;
	}

	public void setHotel_5(Double hotel_5) {
		this.hotel_5 = hotel_5;
	}

	public Double getService_5() {
		return service_5;
	}

	public void setService_5(Double service_5) {
		this.service_5 = service_5;
	}

	public Double getCommission_5() {
		return commission_5;
	}

	public void setCommission_5(Double commission_5) {
		this.commission_5 = commission_5;
	}

	public Double getHotel_6() {
		return hotel_6;
	}

	public void setHotel_6(Double hotel_6) {
		this.hotel_6 = hotel_6;
	}

	public Double getService_6() {
		return service_6;
	}

	public void setService_6(Double service_6) {
		this.service_6 = service_6;
	}

	public Double getCommission_6() {
		return commission_6;
	}

	public void setCommission_6(Double commission_6) {
		this.commission_6 = commission_6;
	}

	public Double getHotel_7() {
		return hotel_7;
	}

	public void setHotel_7(Double hotel_7) {
		this.hotel_7 = hotel_7;
	}

	public Double getService_7() {
		return service_7;
	}

	public void setService_7(Double service_7) {
		this.service_7 = service_7;
	}

	public Double getCommission_7() {
		return commission_7;
	}

	public void setCommission_7(Double commission_7) {
		this.commission_7 = commission_7;
	}

	public Double getHotel_8() {
		return hotel_8;
	}

	public void setHotel_8(Double hotel_8) {
		this.hotel_8 = hotel_8;
	}

	public Double getService_8() {
		return service_8;
	}

	public void setService_8(Double service_8) {
		this.service_8 = service_8;
	}

	public Double getCommission_8() {
		return commission_8;
	}

	public void setCommission_8(Double commission_8) {
		this.commission_8 = commission_8;
	}

	public Double getHotel_9() {
		return hotel_9;
	}

	public void setHotel_9(Double hotel_9) {
		this.hotel_9 = hotel_9;
	}

	public Double getService_9() {
		return service_9;
	}

	public void setService_9(Double service_9) {
		this.service_9 = service_9;
	}

	public Double getCommission_9() {
		return commission_9;
	}

	public void setCommission_9(Double commission_9) {
		this.commission_9 = commission_9;
	}

	public Double getHotel_10() {
		return hotel_10;
	}

	public void setHotel_10(Double hotel_10) {
		this.hotel_10 = hotel_10;
	}

	public Double getService_10() {
		return service_10;
	}

	public void setService_10(Double service_10) {
		this.service_10 = service_10;
	}

	public Double getCommission_10() {
		return commission_10;
	}

	public void setCommission_10(Double commission_10) {
		this.commission_10 = commission_10;
	}

	public Double getHotel_11() {
		return hotel_11;
	}

	public void setHotel_11(Double hotel_11) {
		this.hotel_11 = hotel_11;
	}

	public Double getService_11() {
		return service_11;
	}

	public void setService_11(Double service_11) {
		this.service_11 = service_11;
	}

	public Double getCommission_11() {
		return commission_11;
	}

	public void setCommission_11(Double commission_11) {
		this.commission_11 = commission_11;
	}

	public Double getHotel_12() {
		return hotel_12;
	}

	public void setHotel_12(Double hotel_12) {
		this.hotel_12 = hotel_12;
	}

	public Double getService_12() {
		return service_12;
	}

	public void setService_12(Double service_12) {
		this.service_12 = service_12;
	}

	public Double getCommission_12() {
		return commission_12;
	}

	public void setCommission_12(Double commission_12) {
		this.commission_12 = commission_12;
	}

	public Double getHotel() {
		return hotel;
	}

	public void setHotel(Double hotel) {
		this.hotel = hotel;
	}

	public Double getService() {
		return service;
	}

	public void setService(Double service) {
		this.service = service;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public Date getRegdate() {
		return regdate;
	}

	public void setRegdate(Date regdate) {
		this.regdate = regdate;
	}

	public Long getUsers_id() {
		return users_id;
	}

	public void setUsers_id(Long users_id) {
		this.users_id = users_id;
	}
	
	public static double calcCommission(double hotel, double annual_volume, int managers){
		double d = hotel * 0.12 * 0.125;
		double d2 = annual_volume * 0.12 * 0.125 * 0.25;
		return ((d - d * 0.25) + (d2/managers));
	}
	
	public void calcTotal(){
		this.hotel = CommonUtil.nvl(hotel_1) + CommonUtil.nvl(hotel_2) + CommonUtil.nvl(hotel_3) +
				     CommonUtil.nvl(hotel_4) + CommonUtil.nvl(hotel_5) + CommonUtil.nvl(hotel_6) +
				     CommonUtil.nvl(hotel_7) + CommonUtil.nvl(hotel_8) + CommonUtil.nvl(hotel_9) + 
				     CommonUtil.nvl(hotel_10) + CommonUtil.nvl(hotel_11) + CommonUtil.nvl(hotel_12);
		this.service = CommonUtil.nvl(service_1) + CommonUtil.nvl(service_2) + CommonUtil.nvl(service_3) +
					   CommonUtil.nvl(service_4) + CommonUtil.nvl(service_5) + CommonUtil.nvl(service_6) +
					   CommonUtil.nvl(service_7) + CommonUtil.nvl(service_8) + CommonUtil.nvl(service_9) + 
					   CommonUtil.nvl(service_10) + CommonUtil.nvl(service_11) + CommonUtil.nvl(service_12);
		
	}
}
