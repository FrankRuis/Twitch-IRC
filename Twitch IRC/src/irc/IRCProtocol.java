package irc;

/**
 * Class containing IRC protocol information
 * 
 * @author Frank
 */
public class IRCProtocol {
	
	/**
	 * IRC Port
	 */
	public static final int PORT = 6667;

	/**
	 * The version of the twitch client we want to use
	 */
	public static final String TWITCHCLIENT = "TWITCHCLIENT 3";
	
	/**
	 * The address of the twitch IRC server
	 */
	public static final String TWITCH_HOST = "irc.twitch.tv";
	
	/**
	 * Start of the message of the day
	 */
	public static final String MOTD_START = "375";
	
	/**
	 * The message of the day
	 */
	public static final String MOTD = "372";
	
	/**
	 * End of the message of the day
	 */
	public static final String MOTD_END = "376";
	
	/**
	 * Pong command, the reply to a ping
	 */
	public static final String PONG = "PONG";

	/**
	 * Ping command, server checking if we're still there, must be replied to with a pong
	 */
	public static final String PING = "PING";
	
	/**
	 * Protocol command for sending a user's password when logging in
	 */
	public static final String PASSWORD = "PASS";
	
	/**
	 * Protocol command for sending a user's nickname when logging in
	 */
	public static final String NICKNAME = "NICK";
	
	/**
	 * USER command for specifying the username, hostname, servername and realname of a new user
	 */
	public static final String USER = "USER";
	
	/**
	 * PRIVMSG command for sending a message to a channel
	 */
	public static final String MESSAGE = "PRIVMSG";
}
