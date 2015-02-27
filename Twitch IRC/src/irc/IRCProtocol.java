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
	 * Server welcome code
	 */
	public static final String WELCOME = "376";
	
	/**
	 * Online users code
	 */
	public static final String ONLINE_USERS = "353";
	
	/**
	 * Username already in use error code
	 */
	public static final String USERNAME_IN_USE = "433";
	
	/**
	 * Pong message, reply to a ping
	 */
	public static final String PONG = "PONG";

	/**
	 * Ping message, must be replied to with a pong
	 */
	public static final String PING = "PING";
}
