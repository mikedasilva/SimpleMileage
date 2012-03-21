package com.mikedasilva.simplemileage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Activity for exporting the data.
 * 
 * @author mike
 *
 */
public class ExportActivity extends Activity {
	
	protected TextView emailText;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.export);
        
        emailText = (TextView) findViewById(R.id.text_email);
        
		Button email = (Button)findViewById(R.id.button_email);
		email.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO get the data and pass it along to email
				sendEmail(emailText.getText().toString());
			}
		});

    }
	
	// TODO get the data
	public void getMileageData() {
		
	}
	
	// call the email hook
	public void sendEmail(String emailTo) {
		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		String address = emailTo;
		String content = "testing";
		String subject = "android test";
		
		emailIntent.setType("plain/text");
        
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{ address});
 
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
 
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, content);

        // TODO actually use the data that should have been passed in
        //MileageData mileageData = new MileageData();
        
        this.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
	}
	


}
