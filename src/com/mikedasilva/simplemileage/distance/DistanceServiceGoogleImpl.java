package com.mikedasilva.simplemileage.distance;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.util.Log;

/**
 * Implementation that uses the Google Distance API to calculate the distance
 * travelled between two coordinates.
 * 
 * @author mike
 * 
 */
public class DistanceServiceGoogleImpl implements DistanceService {

	/**
	 * Use the Google Distance API for distance in meters
	 */
	public double getDistanceTravelled(double originLatitude,
			double originLongitude, double destinationLatitude,
			double destinationLongitude) {

		double distanceInMeters = 0;
		
		// setup the URL that will be used for the request
		String url = "http://maps.googleapis.com/maps/api/distancematrix/json?origins="
				+ originLatitude
				+ ","
				+ originLongitude
				+ "&destinations="
				+ destinationLatitude
				+ ","
				+ destinationLongitude
				+ "&sensor=false";

		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		try {
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			
			// check that the request was good
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}

				// get a string of the response
				String jSonResponse = builder.toString();

				// parse the json response for the distance
				JSONObject object = (JSONObject) new JSONTokener(jSonResponse).nextValue();

				JSONArray rows = object.getJSONArray("rows");
				object = rows.getJSONObject(0);
				JSONArray elements = object.getJSONArray("elements");

				for (int i = 0; i < elements.length(); i++) {
					JSONObject jsonObject = elements.getJSONObject(i);
					if (jsonObject.has("distance")) {
						jsonObject = jsonObject.getJSONObject("distance");
						distanceInMeters = jsonObject.getInt("value");
						break; // got it - move along
					}
				}

			} else {
				Log.e(DistanceServiceGoogleImpl.class.toString(),
						"Failed to get distance.");
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace(); // TODO properly handle
		} catch (IOException e) {
			e.printStackTrace(); // TODO properly handle
		} catch (JSONException e) {
			e.printStackTrace(); // TODO properly handle
		}

		return distanceInMeters;

	}

}
