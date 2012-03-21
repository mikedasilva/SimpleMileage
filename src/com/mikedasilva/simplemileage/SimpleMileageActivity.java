package com.mikedasilva.simplemileage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.mikedasilva.simplemileage.data.MileageData;
import com.mikedasilva.simplemileage.distance.DistanceService;
import com.mikedasilva.simplemileage.distance.DistanceServiceNativeImpl;
import com.mikedasilva.simplemileage.enums.UnitEnum;
import com.mikedasilva.simplemileage.model.MileageRecord;

/**
 * Main activity for the app
 * 
 * TODO This activity is starting to get bloated.. look into best practices for keeping it lean
 * 
 * @author mike
 * 
 */
public class SimpleMileageActivity extends Activity {

	// widgets used for this activity
	protected Button startTracking;
	protected Button stopTracking;
	protected Spinner unitValue;
	protected TextView distanceText;
	protected ArrayAdapter<String> unitDataAdapter;

	// data handles
	protected MileageData mileageData;
	protected MileageRecord currentMileageRecord;

	// state
	protected boolean tracking = false;

	// location handles
	protected LocationManager mlocManager;
	protected LocationListener mlocListener;

	protected DistanceService distanceService = new DistanceServiceNativeImpl();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// initialize the UI
		initializeUI();

		// initialize the tabs
		//initializeTabs();
		
		// update the current state
		updateUIState();

		// data to keep track of
		mileageData = new MileageData(this);

		// location
		mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		mlocListener = new MileageLocationListener();

	}

	/**
	 * Called when a configuration change is made (ex: change in orientation)
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	// initialize all the UI components
	private void initializeUI() {
		// set the UI based on the 'main' layour file
		setContentView(R.layout.main);

		// get the distance text
		distanceText = (TextView) findViewById(R.id.text_distance);

		// get the buttons
		startTracking = (Button) findViewById(R.id.button_startTracking);
		stopTracking = (Button) findViewById(R.id.button_stopTracking);
		unitValue = (Spinner) findViewById(R.id.spinner_Unit);

		// populate unit spinner
		List<String> labels = new ArrayList<String>(UnitEnum.values().length);
		for (UnitEnum unit : UnitEnum.values()) {
			labels.add(unit.getLabel());
		}
		unitDataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, labels);
		unitDataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		unitValue.setAdapter(unitDataAdapter);
		unitDataAdapter.notifyDataSetChanged();

		// add a listeners on the buttons
		startTracking.setOnClickListener(new View.OnClickListener() {

			// clicked on start/resume/pause
			public void onClick(View v) {
				// verify if it's a new track, pause, or resume
				if (currentMileageRecord != null) {
					if (tracking) {
						// pause was pressed
						pauseTracking();
					} else {
						// resume was pressed
						resumeTracking();
					}
				} else {
					// start was pressed
					startTracking();
				}

			}
		});
		stopTracking.setOnClickListener(new View.OnClickListener() {

			// click on stop
			public void onClick(View v) {
				// stop was pressed
				stopTracking();

			}
		});
		
		
		
		
		

	}
	
	


	// update the current state of the tracker
	public void updateUIState() {
		if (tracking) {
			// tracking - enable pause
			enablePauseButton();

			// enable stop
			enableStopButton();

		} else {
			if (currentMileageRecord != null) {
				// enable resume
				enableResumeButton();

				// enable stop
				enableStopButton();
			} else {
				// enable start
				enableStartButton();

				// disable stop
				disableStopButton();
			}
		}
		
		// update the distance display
		if(currentMileageRecord != null)
			updateDistanceDriven(currentMileageRecord.getDistance());
	}

	// enable clicking on start
	private void enableStartButton() {
		startTracking.setEnabled(true);
		startTracking.setText(R.string.start_label);
		startTracking.setBackgroundResource(R.drawable.btn_green);
	}

	// enable clicking on pause
	private void enablePauseButton() {
		startTracking.setEnabled(true);
		startTracking.setText("Pause");
		startTracking.setBackgroundResource(R.drawable.btn_yellow);
	}

	// enable clicking on stop
	private void enableStopButton() {
		stopTracking.setEnabled(true);
		stopTracking.setBackgroundResource(R.drawable.btn_red);
	}

	// enable clicking on resume
	private void enableResumeButton() {
		startTracking.setEnabled(true);
		startTracking.setText("Resume");
		startTracking.setBackgroundResource(R.drawable.btn_green);
	}

	// disable clicking on the stop button
	private void disableStopButton() {
		stopTracking.setEnabled(false);
		stopTracking.setBackgroundResource(R.drawable.btn_gray);
	}
	
	// enable location updates from the gps
	public void enableLocationUpdates() {
		mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 0,
				mlocListener); // 90 seconds, 1km
	}
	
	// disable location updates from the gpc
	public void disableLocationUpdates() {
		mlocManager.removeUpdates(mlocListener);
	}

	/**
	 * Start tracking the mileage
	 */
	public void startTracking() {
		tracking = true;

		// disable to not allow more than one click
		startTracking.setEnabled(false);

		// don't allow changing the unit
		unitValue.setEnabled(false);

		// create a new record
		currentMileageRecord = new MileageRecord();
		currentMileageRecord.setDate(new Date(System.currentTimeMillis()));
		currentMileageRecord.setDistance(0);
		currentMileageRecord.setUnit((String) unitValue.getSelectedItem());

		// enable gps
		enableLocationUpdates();

		// change the start to a pause
		enablePauseButton();

		// allow to stop tracking
		enableStopButton();
	}

	/**
	 * Pause tracking - any travels while paused should NOT be included in the total distance travelled
	 */
	public void pauseTracking() {
		// change to resume
		enableResumeButton();

		// no longer tracking
		tracking = false;

		// get and add the current location
		updateWithCurrentLocation();
		
		// turn off the gps
		disableLocationUpdates();

	}

	/**
	 * Stop tracking and persist the current record
	 */
	public void stopTracking() {
		disableStopButton();
		
		// capture current location
		updateWithCurrentLocation();

		
		// TODO note -- the rest of this MUST execute only after a fix on the current location has been made. Therefore, this needs to be
		// refactored with some type of callback
		
		// disable gps
		disableLocationUpdates();

		// persist the distance
		mileageData.insert(currentMileageRecord);

		// reset the current record
		currentMileageRecord = null;

		// allow new tracking
		enableStartButton();

		// allow changing the unit
		unitValue.setEnabled(true);
	}

	/**
	 * Resume tracking from the current location
	 */
	public void resumeTracking() {
		// allow pause
		enablePauseButton();

		tracking = true;
		
		// add a location of 0 to essentially reset the starting point
		currentMileageRecord.addCordinate(0, 0);
		
		// track current location
		updateWithCurrentLocation();

		// turn gps back on
		enableLocationUpdates();
	}

	/**
	 * Update the value of the distance driven on the UI
	 * 
	 * @param meterDistance
	 */
	public void updateDistanceDriven(double meterDistance) {

		// determine how the distance should be displayed
		String selectedUnit = unitValue.getSelectedItem().toString();

		double adjustedDistance = 0;

		for (UnitEnum unit : UnitEnum.values()) {
			if (unit.getLabel().equals(selectedUnit)) {
				adjustedDistance = unit.convertFromMeters(meterDistance);
			}
		}

		// distance in correct unit
		String distance = Integer.toString((int)Math.round(adjustedDistance));

		distanceText.setText(distance);
	}

	
	/**
	 * Get and update the current location
	 */
	public void updateWithCurrentLocation() {
		
		/*
		 * 
		 * figure out how to include either a timer or callback of some sort before enabling this
		 * 
		// add a one-time listener to get the location quickly
		OneTimeLocationListener onetimeListener = new OneTimeLocationListener();
		mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, onetimeListener);
		mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, onetimeListener);
		*/
		
		// get the current location from the last known -- this will just be used as a backup source
		Location currentLocation = mlocManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		
		if(currentLocation != null) {
			updateLocation(currentLocation);
		}
	}
	
	
	
	
	/**
	 * Update the with the given location
	 * 
	 * @param loc
	 */
	public void updateLocation(Location loc) {
		if(loc != null) {
			double latitude = loc.getLatitude();
			double longitude = loc.getLongitude();
	
			// calculate the distance travelled
			double distanceTravelled = 0;
	
			// check if distance needs to be calculated
			if (currentMileageRecord.getPreviousLatitude() != 0
					&& currentMileageRecord.getPreviousLongitude() != 0) {
				// call the service
				distanceTravelled = distanceService.getDistanceTravelled(
						currentMileageRecord.getPreviousLatitude(),
						currentMileageRecord.getPreviousLongitude(), latitude,
						longitude);
			}
	
			// TODO look into if a UI thread is needed for the ui updates like in RCP
			
			
			// TODO make thread safe
			currentMileageRecord.addCordinate(latitude, longitude);
	
			// update the current distance that has been travelled
			currentMileageRecord.setDistance(currentMileageRecord.getDistance()
					+ distanceTravelled);
	
			// update the UI
			updateDistanceDriven(currentMileageRecord.getDistance());
		}
	}

	/**
	 * Listener for location updates
	 * 
	 * @author mike
	 * 
	 */
	public class MileageLocationListener implements LocationListener {

		/**
		 * Handle a change in location
		 */
		@Override
		public void onLocationChanged(Location loc) {
			
			// handle the change in location
			updateLocation(loc);

		}

		@Override
		public void onProviderDisabled(String arg0) {

		}

		@Override
		public void onProviderEnabled(String arg0) {

		}

		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {

		}

	}
	
	
	/**
	 * Used to get a location fix once
	 * 
	 * @author mike
	 *
	 */
	public class OneTimeLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			// update the location
			updateLocation(location);
			
			// remove this listener
			mlocManager.removeUpdates(this);
			
		}

		@Override
		public void onProviderDisabled(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			// TODO Auto-generated method stub
			
		}
		
	}
}