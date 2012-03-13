package com.mikedasilva.simplemileage.distance;

import android.location.Location;

/**
 * Implementation that uses the native distance calculation provided by android.
 * 
 * @author mike
 * 
 */
public class DistanceServiceNativeImpl implements DistanceService {

	/**
	 * Get the distance between the given coordinates
	 */
	public int getDistanceTravelled(double originLatitude,
			double originLongitude, double destinationLatitude,
			double destinationLongitude) {
		
		// origin location
		Location originLocation = new Location("");
		originLocation.setLatitude(originLatitude);
		originLocation.setLongitude(originLongitude);

		// destination location
		Location destinationLocation = new Location("");
		destinationLocation.setLatitude(destinationLatitude);
		destinationLocation.setLongitude(destinationLongitude);
		
		// calculate the distance between the two locations
		float distance = originLocation.distanceTo(destinationLocation);
		
		// round the float and return
		// TODO might need more here.. test cases will find that out
		return Math.round(distance);

	}

}
