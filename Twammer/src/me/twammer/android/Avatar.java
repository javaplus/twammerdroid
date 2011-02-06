package me.twammer.android;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class Avatar {

	// reference to the actual ImageView on the UI
	private ImageView avatarImageView;
	
	// Drawable resource to update ImageView
	private Drawable avatarDrawable;

	// URL location of avatar.
	private String urlString;
	
	
	
	public String getUrlString() {
		return urlString;
	}
	public void setUrlString(String urlString) {
		this.urlString = urlString;
	}
	public ImageView getAvatarImageView() {
		return avatarImageView;
	}
	public void setAvatarImageView(ImageView avatarImageView) {
		this.avatarImageView = avatarImageView;
	}
	public Drawable getAvatarDrawable() {
		return avatarDrawable;
	}
	public void setAvatarDrawable(Drawable avatarDrawable) {
		this.avatarDrawable = avatarDrawable;
	}
}
