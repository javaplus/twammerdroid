package me.twammer.android;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import me.twammer.domain.Twam;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
 * This is the class that assists the UI by backing the Data
 * in the list. 
 * 
 * @author btarlton
 *
 */
public class TwamAdapter extends BaseAdapter {

	private static final String tag = TwamAdapter.class.getName();
	// theLayout is a linear layout that defines each item in the list.
	// So we ultimately end up with a long list of these layouts.
	// using the layout allows us to create a complex list with multiple items
	// in each row.
	private static LayoutInflater layoutInflator = null;
	
	private List<Twam> twams;
	// a cache of images that are the avatars
	// using ConcurrentHash map so it's thread safe and we can be updating this while we read from it.
	private Map<String, Drawable> avatars = new ConcurrentHashMap<String, Drawable>();
	// I believe context is what tells you what UI to apply thing to.
	private final Context context;
	// let's create a handler to be able to update the Avatars
	// since the updating of the view has to be in this thread.
	private final Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			notifyDataSetChanged();
		}
			
	};
	
	public TwamAdapter(Context context, List<Twam> twamList){
		this.twams = twamList;
		this.context = context;
		
		// I guess you can't use the setContentView to inflate the layout in the XML.
		// So you must inflate the layout manually to get our LinearLayout that's defined in XML.
		if(layoutInflator==null){
			layoutInflator = LayoutInflater.from(context);
			
		}
		
		// start loading avatars now.
		// update the UI in a few seconds...hopefully the Threads would have been cached.
		new Thread(){
			public void run() {
				loadAvatars();
				handler.sendEmptyMessage(0);
			}
		}.start();

		
		
	}
	
	public int getCount() {
		// our count is just how many Twams there are.
		return this.twams.size();
	}

	public Object getItem(int position) {
		// get the item for this postion
		return twams.get(position);
	}

	public long getItemId(int position) {
		// our unique item id is just the position for now.
		// assumes they pass in valid position???
		return position;
	}

	/*
	 * The getView(int,View, ViewGroup) method allows us to control the actual
	 * view we return for each item in the list.
	 * This way we can display multiple things in a single row of the list.
	 * In our case we will display the Avatar(image), the text of the twam, 
	 * the user name, and the time is was posted.
	 * 
	 * This will return a LinearLayout containing each of these items.
	 *  
	 * (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		
		LinearLayout theLayout = (LinearLayout)layoutInflator.inflate(R.layout.list, null);
		
		// once we have our layout from XML we can get the individual pieces to update them.
		TextView user = (TextView)theLayout.findViewById(R.id.user);
		TextView text = (TextView)theLayout.findViewById(R.id.text);
		TextView time = (TextView)theLayout.findViewById(R.id.time);
		ImageView pic = (ImageView)theLayout.findViewById(R.id.avatar);
		// add a list of the ImageViews so we can load these later when
		// we get the actual picture. Starting off we'll load a default image.
		
		Twam twam = twams.get(position);
		
		Log.d(tag, "trying to get Twam for postion:" + position);
		Log.d(tag, "twams size =" + twams.size());
		
				// see if we have a cached version of the image:
		Drawable drawable =	avatars.get(twam.getImage());
		if(drawable!=null){
			pic.setImageDrawable(drawable);
			Log.d(tag, "have Drawable!");
		}else{
			pic.setImageResource(R.drawable.missing);
		}
		
		user.setText(twam.getUser());
		text.setText(twam.getText());
		time.setText(twam.getTime());
		
		return theLayout;
		
	}
	
	/*
	 * Here we get the avatars Drawable from their URLs.
	 * Then we put the drawable back in the map.
	 */
	public void loadAvatars(){
		Log.d(tag, "LoadAvatars called");
		for (Twam twam : this.twams) {
			// if we don't have the image for this one get it.
			if(avatars.get(twam.getImage())==null){
				// get picture from twammer
				String picUrl = Constants.TWAMMER_BASE_URL + "/"+ twam.getImage();
				Log.d(tag, "picURL=" + picUrl);
				Drawable theDrawable = DrawableFetcher.getImageFromUrl(picUrl);
				if(theDrawable!=null){
					avatars.put(twam.getImage(),theDrawable);
				}else{
					// we could cache a missing image drawable
					// for now we won't.
					// but this way it may retry to get it later
				}
			}
		}
	}

	
	
}
