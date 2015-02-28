package dataobjects;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

/**
 * Class for maintaining a list of users connected to the IRC channels
 * 
 * @author Frank
 */
public class ConnectedUsers {

	// The list of connected users
	private Map<String, User> users;
	
	// The default username colors used by twitch
	private String[] defaultColors = {"#FF0000","#0000FF","#00FF00",
									  "#B22222","#FF7F50","#9ACD32",
									  "#FF4500","#2E8B57","#DAA520",
									  "#D2691E","#5F9EA0","#1E90FF",
									  "#FF69B4","#8A2BE2","#00FF7F"};
	
	/**
	 * Constructor
	 */
	public ConnectedUsers() {
		users = new HashMap<>();
	}
	
	/**
	 * Add a user to the list
	 * @param name The username
	 * @param channel The channels this user is in
	 */
	public void addUser(String name, String channel) {
		if (!users.containsKey(name)) {
			// Choose the default color based on the ASCII values of the username's first and last character
			int n = (name.toUpperCase().charAt(0) + name.charAt(name.length() - 1));
			String colorString = defaultColors[n % defaultColors.length];
			Color color = new Color(Integer.valueOf(colorString.substring(1, 3), 16),
		            				Integer.valueOf(colorString.substring(3, 5), 16),
		            				Integer.valueOf(colorString.substring(5, 7), 16));
			
			// Create a new user and let it join the channel
			User user = new User(name, color);
			user.join(channel);
			
			users.put(name, user);
		} else {
			// Let the user join the channel
			users.get(name).join(channel);
		}
	}
	
	/**
	 * @param name The name of the user you wish to get
	 * @return The user object for the given username
	 */
	public User getUser(String name) {
		return users.get(name);
	}
	
	/**
	 * Remove a user from the list
	 * @param name
	 */
	public void removeUser(String name) {
		if (users.containsKey(name)) {
			users.remove(name);
		}
	}
	
	/**
	 * Add a channel to a user's connected channels list
	 * @param name The user
	 * @param channel The channel to add
	 */
	public void addChannel(String name, String channel) {
		if (users.containsKey(name)) {
			users.get(name).join(channel);
		}
	}
	
	/**
	 * Remove a channel from a user's connected channels list
	 * @param name The user
	 * @param channel The channel to remove
	 */
	public void removeChannel(String name, String channel) {
		if (users.containsKey(name)) {
			users.get(name).part(channel);
		}
	}
	
	/**
	 * Change a user's color
	 * @param name The user
	 * @param hexColor The color in hexadecimal representation
	 */
	public void setUserColor(String name, String hexColor) {
		if (users.containsKey(name)) {
			try {
				// Convert the hexadecimal color string to a Color object
				Color color = new Color(Integer.valueOf(hexColor.substring(1, 3), 16), Integer.valueOf( hexColor.substring(3, 5), 16), Integer.valueOf(hexColor.substring(5, 7), 16));
				users.get(name).setColor(color);
			} catch (NumberFormatException e) {
				// Sometimes twitch sends a word instead of a hex string as color
			}
		}
	}
}
