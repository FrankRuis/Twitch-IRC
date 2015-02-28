package dataobjects;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * Channel object containing the badges and emoticons of a twitch channel
 * 
 * @author Frank
 */
public class Channel {

	private String name;
	private EmoticonContainer emoticons;
	private Badges badges;
	
	private ImageIcon turboBadge;
	private ImageIcon subscriberBadge;
	private ImageIcon adminBadge;
	private ImageIcon broadcasterBadge;
	private ImageIcon modBadge;
	
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
		readBadges();
	}
	
	/**
	 * Read the urls for the badges and create image icons for each badge
	 */
	public void readBadges() {
		try {
			turboBadge = badges.getTurbo() != null ? new ImageIcon(ImageIO.read(new URL(badges.getTurbo().getImage()))) : null;
			subscriberBadge = badges.getSubscriber() != null ? new ImageIcon(ImageIO.read(new URL(badges.getSubscriber().getImage()))) : null;
			modBadge = badges.getMod() != null ? new ImageIcon(ImageIO.read(new URL(badges.getMod().getImage()))) : null;
			broadcasterBadge = badges.getBroadcaster() != null ? new ImageIcon(ImageIO.read(new URL(badges.getBroadcaster().getImage()))) : null;
			adminBadge = badges.getAdmin() != null ? new ImageIcon(ImageIO.read(new URL(badges.getAdmin().getImage()))) : null;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @return the turboBadge
	 */
	public ImageIcon getTurboBadge() {
		return turboBadge;
	}

	/**
	 * @return the subscriberBadge
	 */
	public ImageIcon getSubscriberBadge() {
		return subscriberBadge;
	}

	/**
	 * @return the adminBadge
	 */
	public ImageIcon getAdminBadge() {
		return adminBadge;
	}

	/**
	 * @return the broadcasterBadge
	 */
	public ImageIcon getBroadcasterBadge() {
		return broadcasterBadge;
	}

	/**
	 * @return the modBadge
	 */
	public ImageIcon getModBadge() {
		return modBadge;
	}

	@Override
	public String toString() {
		return name;
	}
}
