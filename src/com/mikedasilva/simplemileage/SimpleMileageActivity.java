package com.mikedasilva.simplemileage;

import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.mikedasilva.simplemileage.data.MileageData;
import com.mikedasilva.simplemileage.distance.DistanceService;
import com.mikedasilva.simplemileage.distance.DistanceServiceGoogleImpl;
import com.mikedasilva.simplemileage.model.MileageRecord;

/**
 * Main activity for the app
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
	
	
	// data handles
	protected MileageData mileageData;
	protected MileageRecord currentMileageRecord;
	
	// state
	protected boolean tracking = false;
	
	// location handles
	protected LocationManager mlocManager;
	protected LocationListener mlocListener;
	
	protected DistanceService distanceService = new DistanceServiceGoogleImpl();
	
	
	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		// get the distance text
		distanceText = (TextView) findViewById(R.id.text_distance);
		
		// get the buttons
		startTracking = (Button)findViewById(R.id.button_startTracking);
		stopTracking = (Button)findViewById(R.id.button_stopTracking);
		unitValue = (Spinner)findViewById(R.id.spinner_Unit);
		
		// add a listeners on the buttons
		startTracking.setOnClickListener(new View.OnClickListener() {
			
			// clicked on start/resume/pause
			public void onClick(View v) {
				// verify if it's a new track, pause, or resume
				if(currentMileageRecord != null) {
					if(tracking) {
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
		
		// data
		mileageData = new MileageData(this);
		
		// location
		mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		mlocListener = new MileageLocationListener();
		
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
		currentMileageRecord.setUnit((String)unitValue.getSelectedItem());
		
		// change the start to a pause
		startTracking.setText(R.string.pause_label);
		startTracking.setBackgroundResource(R.drawable.btn_yellow);
		
		// register for location updates
		mlocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 60000, 1000, mlocListener); // 60 seconds, 1km				
		
		// enable the start button
		startTracking.setEnabled(true);
		
		// allow to stop tracking
		stopTracking.setEnabled(true);
		stopTracking.setBackgroundResource(R.drawable.btn_red);
	}
	
	/**
	 * Pause tracking
	 */
	public void pauseTracking() {
		startTracking.setText("Resume");
		startTracking.setBackgroundResource(R.drawable.btn_green);
		tracking = false;
		
		// TODO update the distance
		
		// TODO track the pause coordinates
		
		// TODO pause the gps
		
	}
	
	/**
	 * Stop tracking
	 */
	public void stopTracking() {
		stopTracking.setEnabled(false);
		
		// stop getting location updates
		mlocManager.removeUpdates( mlocListener);
		
		// TODO do some calculations
		
		// TODO record the distance
		mileageData.insert(currentMileageRecord);
		
		// reset the current record
		currentMileageRecord = null;
		
		// allow new tracking
		startTracking.setText(R.string.start_label);
		startTracking.setEnabled(true);
		stopTracking.setBackgroundResource(R.drawable.btn_gray);
		startTracking.setBackgroundResource(R.drawable.btn_green);
		
		// allow changing the unit
		unitValue.setEnabled(true);
	}
	
	/**
	 * Resume tracking
	 */
	public void resumeTracking() {
		startTracking.setText("Pause");
		startTracking.setBackgroundResource(R.drawable.btn_yellow);
		tracking = true;
		
		// TODO update the distance
		
		// TODO reset the coordinates
		
		// TODO pause the gps
		
		
	}
	
	
	
	/**
	 * Listener for location updates
	 * @author mike
	 *
	 */
	public class MileageLocationListener implements LocationListener {

		/**
		 * Handle a change in location
		 */
		@Override
		public void onLocationChanged(Location loc) {
			double latitude = loc.getLatitude();
			double longitude = loc.getLongitude();

			// TODO remove this test data
			//latitude =  45.45622798029150;
			//longitude = -75.59166746970848;

			// log the coordinates
			Log.e(MileageLocationListener.class.toString(), "lat:"+latitude + " long:"+longitude);
			
			// TODO check if the starting point has been set
			
		
			
			// TODO make thread safe
			currentMileageRecord.addCordinate(latitude, longitude);
			
			// TODO perhaps switch to another service impl or use a combination of them
			// call the distance travelled service
			int distanceTravelled = distanceService.getDistanceTravelled(currentMileageRecord.getStartingLatitude(), currentMileageRecord.getStartingLongitude(), latitude, longitude);
			
			// distance in meters
			String distance = Integer.toString(distanceTravelled);

			// TODO format distance into selected Unit (km/mile)
			
			// update the UI
			distanceText.setText(distance);

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
}