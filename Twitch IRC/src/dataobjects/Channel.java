package dataobjects;

/**
 * Channel object containing the badges and emoticons of a twitch channel
 * 
 * @author Frank
 */
public class Channel {

	private String name;
	private EmoticonContainer emoticons;
	private Badges badges;
	
	/**
	 * Constructor
	 * @param name The channel name
	 */
	public Channel(String name) {
		this.name = name;
	}
	
	/**
	 * @return the emoticons
	 */
	public EmoticonContainer getEmoticons() {
		return emoticons;
	}
	
	/**
	 * @param emoticons the emoticons to set
	 */
	public void setEmoticons(EmoticonContainer emoticons) {
		this.emoticons = emoticons;
	}
	
	/**
	 * @return the badges
	 */
	public Badges getBadges() {
		return badges;
	}
	
	/**
	 * @param badges the badges to set
	 */
	public void setBadges(Badges badges) {
		this.badges = badges;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
