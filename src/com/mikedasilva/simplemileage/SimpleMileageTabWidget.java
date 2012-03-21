package com.mikedasilva.simplemileage;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

/**
 * Activity to handle the tabs that will be used in the app.
 * 
 * @author mike
 * 
 */
public class SimpleMileageTabWidget extends TabActivity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// initialize the tabs
		initializeTabs();
	}

	// set up the tabs for navigation in the app
	public void initializeTabs() {
		Resources res = getResources(); // Resource object to get Drawables
		TabHost tabHost = getTabHost(); // The activity TabHost
		TabHost.TabSpec spec; // Resusable TabSpec for each tab
		Intent intent; // Reusable Intent for each tab

		// Create an Intent to launch an Activity for the tab (to be reused)
		intent = new Intent().setClass(this, SimpleMileageActivity.class);

		// Initialize a TabSpec for each tab and add it to the TabHost
		spec = tabHost
				.newTabSpec("track")
				.setIndicator("Track Mileage", res.getDrawable(R.drawable.tab_settings))
				.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, ExportActivity.class);
		// add the export tab
		spec = tabHost
				.newTabSpec("export")
				.setIndicator("Export Data",
						res.getDrawable(R.drawable.tab_settings))
				.setContent(intent);
		tabHost.addTab(spec);

		// set the current tab to the tracker
		tabHost.setCurrentTab(0);

	}

}
