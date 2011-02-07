package me.twammer.android;

public class Constants {

	public final static String INTENT_ACTION_VIEW_LIST = "me.twammer.android.intent.action.SHOWLIST";
	// public final static String TWAMMER_EPA = "http://www.twammer.me/twams/active.html";
	// 10.0.2.2 is the emulators alias to the host machine it's on.
	// localhost is the loopback for the emulator not to your PC
	public final static String TWAMMER_EPA = "http://10.0.2.2:8080/twammer-web/active.html";
	// public final static String TWAMMER_BASE_URL = "http://www.twammer.me";
	public final static String TWAMMER_BASE_URL = "http://10.0.2.2:8080/twammer-web";
	public final static String TWAMS_FILE = "twams.txt";
	public static String newline = System.getProperty("line.separator");
}
