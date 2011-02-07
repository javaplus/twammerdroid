package me.twammer.android;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.twammer.domain.Twam;
import me.twammer.domain.TwamService;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ListView;

/**
 * This class is to display the list of Twams
 * 
 * @author btarlton
 * 
 */
public class TwamListActivity extends ListActivity {
	private static String tag = TwamListActivity.class.getName();

	
	// make this a hashSet so we don't add the same Twam twice.
	private LinkedHashSet<Twam> twams = new LinkedHashSet<Twam>();

	// service object to get Twams.(Not an Android service)
	private TwamService twamService = new TwamService();
	
	// The adapter that backs our list data.
	private TwamAdapter twamAdapter;
	
	// this is used to schedule async tasks(like polling for twams)
	private Timer timer;
	
	// need a timertask to check for new twams in a separate thread.
	class MyTimerTask extends TimerTask{
		@Override
		public void run() {
			twams.add(twamService.getTwam());
			// 	send a message to main UI thread.
			handler.sendEmptyMessage(0);
		
		}
	}
	private MyTimerTask task;
		
	
	// need a handler for the TimerTask to update  
	private Handler handler = new Handler(){
		
		public void handleMessage(android.os.Message msg) {
			// convert our list of twams to an array (maybe move this to background thread)
			List<Twam> twamList = new ArrayList<Twam>(twams);
			// reverse the collection to put newest at top.
			Collections.reverse(twamList);
			// add the new ordered list to the twamAdapter
			twamAdapter.setTwams(twamList);
			// notify UI that data has changed so it gets refreshed.
			twamAdapter.notifyDataSetChanged();
			// load Avatars in the back ground
			twamAdapter.loadAvatarsAsync();
		}
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(tag, "In TwamListActivity onCreate!");
		super.onCreate(savedInstanceState);

		ListView listView = getListView();

		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

	}

	
	@Override
	/**
	 * Called anytime our window is displayed including being brought 
	 *  back up(like after going to home) and then launching app again.
	 * 
	 */
	protected void onResume() {
		Log.i(tag, "Calling onResume");
		super.onResume();
		
		// first let's load our existing twams from the file
		loadTwamsFromFile();
		// now let's check for a new TWAM!
		Twam newTwam = twamService.getTwam();
		boolean added = this.twams.add(newTwam);
		if (added) {
			Log.d(tag, "Added new twam!");
		} else {
			Log.d(tag, "Did NOT add new twam for TWAM text:" + newTwam.getText());
		}
		List<Twam> twamList = new ArrayList<Twam>(this.twams);
		Collections.reverse(twamList);
		this.twamAdapter =new TwamAdapter(TwamListActivity.this, twamList); 
		setListAdapter(twamAdapter);
		task = new MyTimerTask();
		timer = new Timer();
		// So, start the task in 5 seconds and run it every 5 seconds there after.
		timer.scheduleAtFixedRate(task, 5000, 5000);
		
				
	}

	@Override
	/**
	 * Here's where you want to persist stuff before your activity is taken away.
	 * or do any clean up before your app is destroyed.
	 */
	protected void onPause() {
		super.onPause();
		Log.d(tag, "onPause() called!");
		
		// kill the task that polls for the Twams.
		task.cancel();
		// purge the canceled task
		timer.purge();
		// stop our timer so we don't keep running...
		timer.cancel();
		
		// Write our twams out to the file so we don't lose them.
		writeTwamstoFile();


	}

	
	/*
	 * Save our Twams to the file so we can pull them back
	 * when our app comes back.
	 * 
	 */
	private void writeTwamstoFile() {
		FileOutputStream fos = null;
		try {
			fos = openFileOutput(Constants.TWAMS_FILE, Context.MODE_PRIVATE);
			StringBuilder text = new StringBuilder();
			for (Twam twam : twams) {
				String json = twamService.getJSONFromTwam(twam);
				Log.d(tag, "json to serialize=" + json);
				text.append(json);
				text.append(Constants.newline);
			}
			Log.d(tag, "writing this to file:" + text.toString());
			fos.write(text.toString().getBytes());
		} catch (FileNotFoundException e) {
			Log.e(tag, e.getLocalizedMessage());
		} catch (IOException e) {
			Log.e(tag, e.getLocalizedMessage());
		} finally {
			if (fos != null) {
				try {
					fos.flush();
					fos.close();
				} catch (IOException e) {
					// swallow
				}
			}
		}
	}
	
	/*
	 * Loads are Twams from the file system.
	 * This will wipe the list of twams and re-create it from the file system.
	 * 
	 */
	private void loadTwamsFromFile(){
		this.twams = new LinkedHashSet<Twam>();
		
		FileInputStream fis = null;
		try {
			fis = openFileInput(Constants.TWAMS_FILE);
			// Get the object of DataInputStream
		    InputStreamReader in = new InputStreamReader(fis);
		    BufferedReader br = new BufferedReader(in);
		    String strLine=null;
		    //Read File Line By Line
		    while ((strLine = br.readLine()) != null)   {
		      Log.d(tag, "readline from file:" + strLine);
		      twams.add(twamService.buildTwamFromJSON2(strLine));
		      
		    }
		} catch (IOException e) {
			Log.e("ReadFile", e.getMessage(), e);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					// eat it
				}
			}
		}	
		
	}

	
}
