package uz.hbs.beans;

import org.apache.wicket.util.io.IClusterable;

public class MyBean implements IClusterable {
	private static final long serialVersionUID = 1L;
	private double cost;
	private Integer count;
	private String type;
	
	public MyBean() {
		// TODO Auto-generated constructor stub
	}
	
	public MyBean(double cost, int count) {
		this.cost = cost;
		this.count = count;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
