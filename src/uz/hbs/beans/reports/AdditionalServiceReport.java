package uz.hbs.beans.reports;

import org.apache.wicket.util.io.IClusterable;

public class AdditionalServiceReport implements IClusterable {
	private static final long serialVersionUID = 1L;
	
	public final static String INSURANCE = "INSURANCE";
	public final static String AIR_ARRIVAL_GREEN_HALL = "AIR_ARRIVAL_GREEN_HALL";
	public final static String AIR_ARRIVAL_VIP_HALL = "AIR_ARRIVAL_VIP_HALL"; 
	public final static String AIR_DEPARTURE_GREEN_HALL = "AIR_DEPARTURE_GREEN_HALL";
	public final static String AIR_DEPARTURE_VIP_HALL = "AIR_DEPARTURE_VIP_HALL";
	public final static String ARRIVAL_TRANSFER = "ARRIVAL_TRANSFER";
	public final static String DEPARTURE_TRANSFER = "DEPARTURE_TRANSFER";
	
	private String name;
	private Integer count_1;
	private Double amount_1;
	private Integer count_3;
	private Double amount_3;
	private Integer count_6;
	private Double amount_6;
	private Integer count_12;
	private Double amount_12;
	private Integer count;
	private Double amount;
	
	public AdditionalServiceReport() {
		// TODO Auto-generated constructor stub
	}

	public Integer getCount_1() {
		return count_1;
	}

	public void setCount_1(Integer count_1) {
		this.count_1 = count_1;
	}

	public Double getAmount_1() {
		return amount_1;
	}

	public void setAmount_1(Double amount_1) {
		this.amount_1 = amount_1;
	}

	public Integer getCount_3() {
		return count_3;
	}

	public void setCount_3(Integer count_3) {
		this.count_3 = count_3;
	}

	public Double getAmount_3() {
		return amount_3;
	}

	public void setAmount_3(Double amount_3) {
		this.amount_3 = amount_3;
	}

	public Integer getCount_6() {
		return count_6;
	}

	public void setCount_6(Integer count_6) {
		this.count_6 = count_6;
	}

	public Double getAmount_6() {
		return amount_6;
	}

	public void setAmount_6(Double amount_6) {
		this.amount_6 = amount_6;
	}

	public Integer getCount_12() {
		return count_12;
	}

	public void setCount_12(Integer count_12) {
		this.count_12 = count_12;
	}

	public Double getAmount_12() {
		return amount_12;
	}

	public void setAmount_12(Double amount_12) {
		this.amount_12 = amount_12;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
