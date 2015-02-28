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
	
	/**
	 * JOIN command, sent when a user joins the channel
	 */
	public static final String JOIN = "JOIN";
	
	/**
	 * PART command, sent when a user leaves a channel
	 */
	public static final String PART = "PART";
	
	/**
	 * MODE command showing which users are moderators in the channel
	 */
	public static final String MODERATOR = "MODE";
	
	/**
	 * USERCOLOR command for specifying the color of a username
	 */
	public static final String USERCOLOR = "USERCOLOR";
	
	/**
	 * EMOTESET command for specifying the emote sets the user may use
	 */
	public static final String EMOTESET = "EMOTESET";
	
	/**
	 * SPECIALUSER command for specifying the special user type
	 */
	public static final String SPECIALUSER = "SPECIALUSER";
	
	/**
	 * CLEARCHAT command, sent when a moderator has cleared the chat or deleted a single message
	 */
	public static final String CLEAR = "CLEARCHAT";
	
	/**
	 * HOSTTARGET command specifying the channel the current channel is hosting
	 */
	public static final String HOSTTARGET = "HOSTTARGET";
	
	/**
	 * HISTORYEND command
	 */
	public static final String HISTORYEND = "HISTORYEND";
	
	/**
	 * ACTION command, user message should be same color as their user name
	 */
	public static final String ACTION = "ACTION";
	
	/**
	 * The IRC account for announcing things like new subscribers
	 */
	public static final String TWITCHNOTIFY = "twitchnotify";
	
	/**
	 * The IRC account for sending user information and channel status messages
	 */
	public static final String JTV = "jtv";
}
