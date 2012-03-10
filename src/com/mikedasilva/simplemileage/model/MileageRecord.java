package com.mikedasilva.simplemileage.model;

import java.util.ArrayList;
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
	protected ArrayList<Double> originsLatitudes;
	protected ArrayList<Double> originsLongitudes;
	
	public MileageRecord() {
		originsLatitudes = new ArrayList<Double>();
		originsLongitudes = new ArrayList<Double>();
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
	
	/**
	 * Add a coordinate to the list
	 * 
	 * @param latitude
	 * @param longitude
	 */
	public void addCordinate(double latitude, double longitude) {
		originsLatitudes.add(latitude);
		originsLongitudes.add(longitude);
	}
	
	/**
	 * Get the starting latitude
	 * 
	 * @return
	 */
	public double getStartingLatitude() {
		return originsLatitudes.get(0);
	}
	
	/**
	 * Get the starting longitude coordinate
	 * 
	 * @return
	 */
	public double getStartingLongitude() {
		return originsLongitudes.get(0);
	}
	
	public double getPreviousLatitude() {
		return originsLatitudes.size() > 0 ? originsLatitudes.get(originsLatitudes.size()-1) : 0;
	}
	
	public double getPreviousLongitude() {
		return originsLongitudes.size() > 0 ? originsLongitudes.get(originsLongitudes.size()-1) : 0;
	}

}
