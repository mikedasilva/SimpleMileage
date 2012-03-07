package com.mikedasilva.simplemileage.model;

import java.util.Date;

/**
 * Class representing a mileage record.
 * 
 * @author mike
 *
 */
public class MileageRecord {
	
	protected int distance;
	protected String unit;
	protected Date date;
	
	public MileageRecord() {
		
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	

}
