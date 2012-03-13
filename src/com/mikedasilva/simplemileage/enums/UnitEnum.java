package com.mikedasilva.simplemileage.enums;

/**
 * Enum representing the units available
 * 
 * @author mike
 *
 */
public enum UnitEnum {
	KM("km",0.001), 
	MILES("Mi",0.000621371);

	private String label;
	private double conversion;
	
	UnitEnum(String label, double conversion) {
		this.label = label;
		this.conversion = conversion;
	}
	
	/**
	 * Convert meters into the correct unit.
	 * 
	 * @param meters
	 * @return
	 */
	public double convertFromMeters(double meters) {
		return meters * this.conversion;
	}
	
	public String getLabel() {
		return label;
	}

}

