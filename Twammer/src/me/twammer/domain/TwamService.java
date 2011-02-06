package me.twammer.domain;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import me.twammer.android.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class TwamService {
	private static final String tag = TwamService.class.getName();
	
	public Twam getTwam(){
		
		Twam twam = null;
		
		// Log.i(tag, "Twammer EPA=" + Constants.TWAMMER_EPA);
		
		String html = getHTML(Constants.TWAMMER_EPA);
		Log.i(tag, "Html=" + html);
		
		twam = buildTwamFromJSON2(html);
		
		return twam;
		
	}
	
	/**
	 * Builds Twam object from jsonString using native
	 * Android JSON apis.
	 * 
	 * @deprecated
	 * @param jsonString
	 * @return
	 */
	private Twam buildTwamFromJSON(String jsonString){
		Twam twam = new Twam();
		try {
			JSONObject jsonObject = new JSONObject(jsonString);
			
			twam.setText(jsonObject.getString("text"));
			twam.setUser(jsonObject.getString("user"));
			twam.setImage(jsonObject.getString("image"));
		} catch (JSONException e) {
			Log.e(tag, "Got JSON Exception trying to create Twam:"+e.getMessage());
			e.printStackTrace();
		}
		
		
		
		return twam;
	}
	
	/**
	 * Builds Twam instance from JSON using
	 * GSON (Google) json libraries.
	 * 
	 * @param jsonString
	 * @return
	 */
	public Twam buildTwamFromJSON2(String jsonString){
		Twam twam = null;
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.create();
		try{	
			twam = (Twam)gson.fromJson(jsonString, Twam.class);
		}catch (Exception e) {
			Log.e(tag, "Caught exception trying to convert JSON to TWAM. JSON[" + jsonString + "]. ERROR:"+e.toString());
			e.printStackTrace();
		}
			
		return twam;
	}
	
	
	public String getHTML(String urlString){
    	String result = "";
    	URL url = null;
    	try{
    		url = new URL(urlString);
    	}catch(MalformedURLException e){
    		Log.e(tag, "Caught error creating url:" + e.toString());
    	}
    	
    	BufferedReader in = null;
    	HttpURLConnection urlConn = null;
    	if(url!=null){
    		try{
    			urlConn = (HttpURLConnection) url.openConnection();
    			in = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
    			String inputLine;
    			int lineCount = 0; // limit lines for now.
    			while((lineCount< 10) && ((inputLine=in.readLine())!=null)){
    				lineCount++;
    				result+="\n" + inputLine;
    			}
    			
    		}catch(IOException e){
    			
    		}finally{
    			if(in!=null){
    				try{
    					in.close();
    				}catch(IOException e){
    					// eat it.
    				}
    					
    			}
    			if(urlConn!=null){
    				urlConn.disconnect();
    			}
    		}
    	}else{
    		Log.i(tag, "URL was null");
    		result="Could not get URL";
    	}
    	
    	return result;
    }
	
	public String getJSONFromTwam(Twam twam){
		String json = null;
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.create();
		try{	
			json= gson.toJson(twam);
		}catch (Exception e) {
			Log.e(tag, "Caught exception trying to convert TWAM to JSON. ERROR:"+e.toString());
			e.printStackTrace();
		}
		
		return json;
	}
	
}
