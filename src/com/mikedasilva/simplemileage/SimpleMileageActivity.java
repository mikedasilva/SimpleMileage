package com.mikedasilva.simplemileage;

import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Spinner;

import com.mikedasilva.simplemileage.data.MileageData;
import com.mikedasilva.simplemileage.model.MileageRecord;

public class SimpleMileageActivity extends Activity implements OnClickListener {
	
	// widgets used for this activity
	protected Button startTracking;
	protected Button stopTracking;
	protected Spinner unitValue;
	
	// data handles
	protected MileageData mileageData;
	protected MileageRecord currentMileageRecord;
	
	// state
	protected boolean tracking = false;
	
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		
		
		// get the buttons
		startTracking = (Button)findViewById(R.id.button_startTracking);
		stopTracking = (Button)findViewById(R.id.button_stopTracking);
		unitValue = (Spinner)findViewById(R.id.spinner_Unit);
		
		// add a listener on the buttons
		startTracking.setOnClickListener(this);
		stopTracking.setOnClickListener(this);
		
		// data
		mileageData = new MileageData(this);
		
	}

	/**
	 * Handle the click events
	 * 
	 */
	public void onClick(View view) {
		
		// check where the click came from
		if(view == startTracking) {
			
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
			
		} else if(view == stopTracking) {
			// stop was pressed
			stopTracking();
			
			
		}
			
	}
	
	/**
	 * Start tracking the mileage
	 */
	public void startTracking() {
		tracking = true;
		
		// don't allow more than one click
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
		
		// enable the start button
		startTracking.setEnabled(true);
		
		// allow to stop tracking
		stopTracking.setEnabled(true);
	}
	
	/**
	 * Pause tracking
	 */
	public void pauseTracking() {
		startTracking.setText("Resume");
		tracking = false;
		
		// TODO update the distance
		
		// TODO track the pause cords
		
		// TODO pause the gps
		
	}
	
	/**
	 * Stop tracking
	 */
	public void stopTracking() {
		stopTracking.setEnabled(false);
		
		// TODO record the distance
		mileageData.insert(currentMileageRecord);
		
		// reset the current record
		currentMileageRecord = null;
		
		// allow new tracking
		startTracking.setText(R.string.start_label);
		startTracking.setEnabled(true);
		
		// allow changing the unit
		unitValue.setEnabled(true);
	}
	
	/**
	 * Resume tracking
	 */
	public void resumeTracking() {
		startTracking.setText("Pause");
		tracking = true;
		
		// TODO update the distance
		
		// TODO reset the cords
		
		// TODO pause the gps
	}
}