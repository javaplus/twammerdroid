package me.twammer.domain;

/**
 * Represents the data in a Twam message
 * 
 * 
 * @author btarlton
 *
 */
public class Twam {
	
	private String text;
	private String image;
	private String user;
	private String time;
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	
	@Override
	public boolean equals(Object o) {
		Twam otherTwam = (Twam) o;
		
		// the text and the user strings are the same they are equal.
		
		return (otherTwam.getText().equalsIgnoreCase(text) && otherTwam.user.equalsIgnoreCase(user));
	}
	
	@Override
	public int hashCode() {
		int hash = text.hashCode() + user.hashCode();
		return hash;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}

}
