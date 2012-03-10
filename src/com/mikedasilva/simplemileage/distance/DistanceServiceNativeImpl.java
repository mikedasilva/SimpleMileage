package com.mikedasilva.simplemileage.distance;

import android.location.Location;

/**
 * Implementation that uses the native distance calculation provided by android.
 * 
 * @author mike
 * 
 */
public class DistanceServiceNativeImpl implements DistanceService {

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
		return Math.round(distance);

	}

}
