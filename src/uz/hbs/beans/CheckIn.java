package uz.hbs.beans;

import java.util.Date;

import org.apache.wicket.util.io.IClusterable;

public class CheckIn implements IClusterable {
	private static final long serialVersionUID = 1L;
	private Date periodFrom;
	private Date periodTo;

	private Date earlyArrivalPeriodHalfFrom;
	private Date earlyArrivalPeriodHalfTo;
	private Double earlyArrivalPeriodHalfServiceCharge;
	private Byte earlyArrivalPeriodHalfRoomRateType;
	
	private Date earlyArrivalPeriodFrom;
	private Date earlyArrivalPeriodTo;
	private Double earlyArrivalPeriodServiceCharge;
	private Byte earlyArrivalPeriodRoomRateType;
	
	public CheckIn() {
		
	}

	public Date getPeriodFrom() {
		return periodFrom;
	}

	public void setPeriodFrom(Date periodFrom) {
		this.periodFrom = periodFrom;
	}

	public Date getPeriodTo() {
		return periodTo;
	}

	public void setPeriodTo(Date periodTo) {
		this.periodTo = periodTo;
	}

	public Date getEarlyArrivalPeriodHalfFrom() {
		return earlyArrivalPeriodHalfFrom;
	}

	public void setEarlyArrivalPeriodHalfFrom(Date earlyArrivalPeriodHalfFrom) {
		this.earlyArrivalPeriodHalfFrom = earlyArrivalPeriodHalfFrom;
	}

	public Date getEarlyArrivalPeriodHalfTo() {
		return earlyArrivalPeriodHalfTo;
	}

	public void setEarlyArrivalPeriodHalfTo(Date earlyArrivalPeriodHalfTo) {
		this.earlyArrivalPeriodHalfTo = earlyArrivalPeriodHalfTo;
	}

	public Double getEarlyArrivalPeriodHalfServiceCharge() {
		return earlyArrivalPeriodHalfServiceCharge;
	}

	public void setEarlyArrivalPeriodHalfServiceCharge(
			Double earlyArrivalPeriodHalfServiceCharge) {
		this.earlyArrivalPeriodHalfServiceCharge = earlyArrivalPeriodHalfServiceCharge;
	}

	public Byte getEarlyArrivalPeriodHalfRoomRateType() {
		return earlyArrivalPeriodHalfRoomRateType;
	}

	public void setEarlyArrivalPeriodHalfRoomRateType(
			Byte earlyArrivalPeriodHalfRoomRateType) {
		this.earlyArrivalPeriodHalfRoomRateType = earlyArrivalPeriodHalfRoomRateType;
	}

	public Date getEarlyArrivalPeriodFrom() {
		return earlyArrivalPeriodFrom;
	}

	public void setEarlyArrivalPeriodFrom(Date earlyArrivalPeriodFrom) {
		this.earlyArrivalPeriodFrom = earlyArrivalPeriodFrom;
	}

	public Date getEarlyArrivalPeriodTo() {
		return earlyArrivalPeriodTo;
	}

	public void setEarlyArrivalPeriodTo(Date earlyArrivalPeriodTo) {
		this.earlyArrivalPeriodTo = earlyArrivalPeriodTo;
	}

	public Double getEarlyArrivalPeriodServiceCharge() {
		return earlyArrivalPeriodServiceCharge;
	}

	public void setEarlyArrivalPeriodServiceCharge(
			Double earlyArrivalPeriodServiceCharge) {
		this.earlyArrivalPeriodServiceCharge = earlyArrivalPeriodServiceCharge;
	}

	public Byte getEarlyArrivalPeriodRoomRateType() {
		return earlyArrivalPeriodRoomRateType;
	}

	public void setEarlyArrivalPeriodRoomRateType(
			Byte earlyArrivalPeriodRoomRateType) {
		this.earlyArrivalPeriodRoomRateType = earlyArrivalPeriodRoomRateType;
	}
}
