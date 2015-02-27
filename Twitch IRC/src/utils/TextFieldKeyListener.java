package utils;

import gui.MainGUI;

import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

import javax.swing.JTextField;

import dataobjects.ChatMessage;

/**
 * Custom key adapter for the chat GUI
 * 
 * @author Frank
 */
public class TextFieldKeyListener extends KeyAdapter {
	
	// Maximum amount of messages in the history
	private final int MAX_HISTORY = 10;
	
	private MainGUI gui;
	private LinkedList<String> messageHistory;
	
	// Current position in the message history
	private int historyPos;
	
	/**
	 * Initialize the key adapter
	 * @param gui MainGUI object for future reference
	 */
	public TextFieldKeyListener(MainGUI gui) {
		messageHistory = new LinkedList<>();
		messageHistory.add("");
		historyPos = 0;
		this.gui = gui;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		// Get the JTextField that triggered the event
		JTextField source = (JTextField) e.getSource();
		
		// Check which key has been pressed
		int keyCode = e.getKeyCode();

		// Handle the enter key
		if (keyCode == KeyEvent.VK_ENTER) {
			// Get the text from the text field
			String message = source.getText();
			
			// Add the message to the history
			if (!messageHistory.contains(message)) {
				messageHistory.push(message);
				
			// Add the message to the start of the history if it already exists
			} else {
				messageHistory.remove(message);
				messageHistory.push(message);
			}
			
			// Make sure the message history limit is not exceeded
			if (messageHistory.size() > MAX_HISTORY) {
				messageHistory.removeLast();
			}
			
			// Create a chat message object and send it to the GUI
			gui.append(new ChatMessage(gui.getCurrentUser(), Color.black, 16, "Calibri", false, false, message, gui.getActiveTab()));
			gui.getClient().sendChatMessage(message, "#" + gui.getActiveTab());
			
			source.setText("");
			historyPos = 0;
		}

		// Scroll up through the message history
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			if (messageHistory.size() > 0) {
				source.setText(messageHistory.get(historyPos));
				historyPos = historyPos < (messageHistory.size() - 1) ? historyPos + 1 : 0;
			}
		}
		
		// Scroll down through the message history
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			if (messageHistory.size() > 0) {
				source.setText(messageHistory.get(historyPos));
				historyPos = historyPos > 0 ? historyPos - 1 : messageHistory.size() - 1;
			}
		}
	}
}
