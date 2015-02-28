package utils;

import java.awt.Color;

import dataobjects.ChatMessage;
import dataobjects.User;

/**
 * Class for easy creation of ChatMessage objects
 * 
 * @author Frank
 */
public class ChatMessageBuilder {

	/**
	 * Create a regular chat message
	 * @param message The message
	 * @param destination The destination
	 * @param user The user
	 * @return A ChatMessage object
	 */
	public static ChatMessage getRegularMessage(String message, String destination, User user) {
		return new ChatMessage(user, Color.black, 16, "Calibri", false, false, message, destination);
	}
	
	/**
	 * Create a chat message for the ACTION command
	 * @param message The message
	 * @param destination The destination
	 * @param user The user
	 * @return A ChatMessage object
	 */
	public static ChatMessage getActionMessage(String message, String destination, User user) {
		return new ChatMessage(user, user.getColor(), 16, "Calibri", false, false, message, destination);
	}
	
}
