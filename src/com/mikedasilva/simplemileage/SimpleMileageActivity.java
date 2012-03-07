package com.mikedasilva.simplemileage;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SimpleMileageActivity extends Activity implements OnClickListener {
	
	// widgets used for this activity
	protected Button startTracking;
	protected Button stopTracking;
	protected Button resumeTracking;
	//protected Button stopTracking;
	
	
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		
		
		// get the buttons
		startTracking = (Button)findViewById(R.id.button_startTracking);
		stopTracking = (Button)findViewById(R.id.button_stopTracking);
		
		// add a listener on the buttons
		startTracking.setOnClickListener(this);
		stopTracking.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View view) {
		
		// check where the click came from
		if(view == startTracking) {
			startTracking.setText("Pause");
			//startTracking.setEnabled(false);
			
			// allow to stop tracking
			stopTracking.setEnabled(true);
			
		} else if(view == stopTracking) {
			stopTracking.setEnabled(false);
			
			// TODO record the distance
			
			// allow new tracking
			startTracking.setEnabled(true);
			
			
		}
			
	}
	
	
	public void startTracking() {
		
	}
	
	
	public void pauseTracking() {
		
	}
	
	public void stopTracking() {
		
	}
	
	public void resumeTracking() {
		
	}
}