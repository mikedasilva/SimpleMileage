package com.mikedasilva.simplemileage.distance;

/**
 * Service used to get the distance between coordinates.
 * 
 * @author mike
 * 
 */
public interface DistanceService {

	/**
	 * Get the distance travelled between an origin and destination coordinate.
	 * 
	 * @param originLatitude
	 * @param originLongitude
	 * @param destinationLatitude
	 * @param destinationLongitude
	 * 
	 * @return int representing distance in meters
	 */
	public int getDistanceTravelled(double originLatitude,
			double originLongitude, double destinationLatitude,
			double destinationLongitude);

}
