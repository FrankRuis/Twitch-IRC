package dataobjects;

/**
 * Emoticon class containing data for a single twitch emoticon
 * 
 * @author Frank
 */
public class Emoticons {

	private String width;
	private String height;
	private String regex;
	private String state;
	private String subscriber_only;
	private String url;
	
	/**
	 * @return the width
	 */
	public String getWidth() {
		return width;
	}
	
	/**
	 * @param width the width to set
	 */
	public void setWidth(String width) {
		this.width = width;
	}
	
	/**
	 * @return the height
	 */
	public String getHeight() {
		return height;
	}
	
	/**
	 * @param height the height to set
	 */
	public void setHeight(String height) {
		this.height = height;
	}
	
	/**
	 * @return the regex
	 */
	public String getRegex() {
		return regex;
	}
	
	/**
	 * @param regex the regex to set
	 */
	public void setRegex(String regex) {
		this.regex = regex;
	}
	
	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}
	
	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}
	
	/**
	 * @return the subscriber_only
	 */
	public String getSubscriber_only() {
		return subscriber_only;
	}
	
	/**
	 * @param subscriber_only the subscriber_only to set
	 */
	public void setSubscriber_only(String subscriber_only) {
		this.subscriber_only = subscriber_only;
	}
	
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
}
