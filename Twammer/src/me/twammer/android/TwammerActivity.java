package me.twammer.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * This class is the main welcome page the Twammer app.
 * 
 * @author btarlton
 *
 */
public class TwammerActivity extends Activity {
	
	// tag for logging for this class.
	private static final String tag = TwammerActivity.class.getName();
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button button = (Button)findViewById(R.id.twamsButton);
        
        button.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		// click the button and go to the screen to show the twams.
				Log.d(tag,"onClick.");
		    	Intent intent = new Intent(Constants.INTENT_ACTION_VIEW_LIST);
		    	//intent.putExtra(Constants.CUISINE_CHOICE, this.cuisine.getSelectedItem().toString());
		    	//intent.putExtra(Constants.LOCATION, this.location.getText().toString());
		    	startActivity(intent);
			}
		});
        
        
    }
}