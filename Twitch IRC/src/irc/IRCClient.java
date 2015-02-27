package irc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.OutQueue;

/**
 * Client for connecting to the Twitch IRC server
 * 
 * @author Frank
 */
public class IRCClient extends Observable implements Runnable {

	private String host;
	private int port;
	
	private Socket socket;
	private BufferedWriter out;
	private BufferedReader in;
	
	// Message queue to ensure we don't exceed the server's message limit
	private OutQueue outQueue;
	
	// Whether or not the client should keep running
	private boolean run ;
	
	// Whether or not logging should be enabled
	private boolean loggingEnabled = true;

	/**
	 * Constructor
	 * @param host The IRC server to connect to
	 */
	public IRCClient(String host) {
		this.host = host;
		this.port = IRCProtocol.PORT;
	}
	
	/**
	 * Connect to an IRC server
	 */
	public void connect() {
		try {
			// Connect to the server and get the in and out streams
			this.socket = new Socket(host, port);
			this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
			this.in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			
			// Create a queue for the outgoing messages
			outQueue = new OutQueue(out);
			Thread queueThread = new Thread(outQueue);
			queueThread.start();
			
			// Allow the loop to start
			run = true;
			
			// Notify the GUI
			setChanged();
			notifyObservers("NOTIFY Log * Succesfully connected to " + host);
			
			log("Connected to " + host);
		} catch (UnknownHostException e) {
			// Notify the GUI
			setChanged();
			notifyObservers("NOTIFY Log * Could not recognize the host " + host);
			log("Error: Unknown Host.");
		} catch (IOException e) {
			// Notify the GUI
			setChanged();
			notifyObservers("NOTIFY Log * Error while connecting to " + host);
			log("Error: IOException while connecting.");
		}
	}
	
	/**
	 * Log in to the server
	 */
	public void login(String nickname, String password) {
		this.sendMessage(IRCProtocol.PASSWORD + " " + password + "\r\n");
		this.sendMessage(IRCProtocol.NICKNAME + " " + nickname + "\r\n");
		this.sendMessage(IRCProtocol.USER + " " + nickname + " 0 * :" + nickname + "\r\n");
		this.sendMessage(IRCProtocol.TWITCHCLIENT + "\r\n");
	}
	
	/**
	 * Get the username from an IRC protocol message
	 * @param usernameString The IRC protocol messge containing the username
	 * @return
	 */
	public String parseUserName(String usernameString) {
		// Match the username and capture it with a regular expression
		Pattern pattern = Pattern.compile(":([[a-zA-Z0-9_]]+)!");
		Matcher matcher = pattern.matcher(usernameString);

		// Return the first captured string
		if (matcher.find()) {
			return matcher.group(1);
		} else {
			return null;
		}
	}
	
	@Override
	public void run() {	
		this.connect();
		
		// Keep running if run is set to true
		while (run) {
			try {
				String line = null;
				while ((line = in.readLine()) != null) {
					// Split the message on up to 4 whitespaces
					String[] message = line.split("\\s", 4);
					
					// If we received a ping, reply with a pong
					if (message[0].equals(IRCProtocol.PING)) {
						sendMessage(IRCProtocol.PONG + message[1] + "\r\n");
					}
					
					// Check the message type
					switch (message[1]) {
						case IRCProtocol.MOTD_END:
							// Message of the day end received, login was successful
							setChanged();
							notifyObservers("NOTIFY Log * You have logged in successfully.");
							break;
						case IRCProtocol.MOTD:
							// Show the message of the day in the GUI
							setChanged();
							notifyObservers("NOTIFY Log * The message of the day is: " + message[3].substring(1));
							break;
						case IRCProtocol.MESSAGE:
							// We received a message
							String username = parseUserName(message[0]);
							
							// If the user is not jtv
							if (!username.equals("jtv")) {
								// Let the GUI know we have received a message
								String channel = message[2].substring(1);
								String messageContents = message[3].substring(1);
								
								setChanged();
								notifyObservers("MESSAGE " + channel + " " + username + " " + messageContents);
							
							// If the user is jtv, parse the user information
							} else {
								
							}
							break;
					}
					
					log(line);
				}
			} catch (IOException e) {
				log("Error: IOException");
			}
		}
		
		log("IRC Client stopped.");
	}
	
	/**
	 * Send a message to the output stream
	 * @param message The message to send
	 */
	public void sendMessage(String message) {
		// Add the message to the out queue
		outQueue.addMessage(message);
		
		log(message);
	}
	
	/**
	 * Send a chat message to the given channel
	 * @param message
	 */
	public void sendChatMessage(String message, String channel) {
		outQueue.addMessage(IRCProtocol.MESSAGE + " " + channel + " :" + message + "\r\n");
		log(IRCProtocol.MESSAGE + " " + channel + " :" + message + "\r\n");
	}
	
	/**
	 * Add the given message to the log
	 * @param message The message to log
	 */
	public void log(String message) {
		// Don't do anything if logging is disabled
		if (loggingEnabled) {
			// Create a timestamp for the message
			SimpleDateFormat sdf = new SimpleDateFormat("[HH:mm]");
			String time = sdf.format(new Date());
			
			System.out.println(time + " " + message);
		}
	}
}
