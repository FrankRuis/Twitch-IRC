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
			notifyObservers("NOTIFY Chat Succesfully connected to " + host);
			
			log("Connected.");
		} catch (UnknownHostException e) {
			// Notify the GUI
			setChanged();
			notifyObservers("NOTIFY Chat Could not recognize the host " + host);
			log("Error: Unknown Host.");
		} catch (IOException e) {
			// Notify the GUI
			setChanged();
			notifyObservers("NOTIFY Chat Error while connecting to " + host);
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
	
	@Override
	public void run() {	
		this.connect();
		
		// Keep running if run is set to true
		while (run) {
			try {
				String line = null;
				while ((line = in.readLine()) != null) {
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
