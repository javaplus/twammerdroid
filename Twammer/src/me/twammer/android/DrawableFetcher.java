package me.twammer.android;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.drawable.Drawable;
import android.util.Log;

public class DrawableFetcher {
	
	private static final String tag = DrawableFetcher.class.getName();
	
	/*
	 * Gets a Drawable resource from a URL.
	 * 
	 * 
	 */
	public static Drawable getImageFromUrl(String url){
		try {
			InputStream is = (InputStream) fetch(url);
			if(is==null){
				Log.d(tag, "input stream is NULL");
			}
			Drawable d = Drawable.createFromStream(is, "src");
			Log.d(tag, "Did not hit exception");
			return d;
		} catch (MalformedURLException e) {
			Log.e(tag, "Caught MalformedURLException getting avatar:" + e.toString());
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			Log.e(tag, "Caught IOException getting avatar:" + e.toString());
			e.printStackTrace();
			return null;
		}catch(Exception e){
			Log.e(tag, "Caught Exception getting avatar:" + e.toString());
			e.printStackTrace();
			return null;
		}
		
	}
	
	/*
	 * Gets content from a URL.  This content is usually a InputStream, but
	 * Android docs say it returns Image for pictures, AudioClip for audio sequences
	 * and InputStream for all other data.
	 * 
	 * NOTE: Been seeing where url.getContent() returns null from time to time and don't know why
	 */
	public static Object fetch(String address) throws MalformedURLException,IOException {
		URL url = new URL(address);
		Object content = url.getContent();
		// Log.d(tag, "content is of type " + content.getClass());
		return content;
	}


}
