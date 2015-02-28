package TwitchAPI;

/**
 * Class containing the urls to the API pages
 * 
 * @author Frank
 */
public class APIUrls {

	/**
	 * The base URL of the twitch API
	 */
	public static String API_BASE = "https://api.twitch.tv/kraken";
	
	/**
	 * The url to get the emoticons for a certain channel.<br>
	 * Replace CHANNEL with channel name
	 */
	public static String EMOTICONS = "/chat/CHANNEL/emoticons";
	
	/**
	 * The url to get the badges for a certain channel.<br>
	 * Replace CHANNEL with channel name
	 */
	public static String BADGES = "/chat/CHANNEL/badges";
	
	/**
	 * The url to all emoticons with the set they belong to
	 */
	public static String EMOTESETS = "/chat/emoticons";
}
