package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import dataobjects.ChatMessage;
import dataobjects.User;
import utils.ClickableListener;
import utils.EmoticonListener;
import utils.TextFieldKeyListener;

/**
 * Main graphical user interface for the chat
 * 
 * @author Frank
 */
public class MainGUI implements ActionListener, Observer {
	
	// Maximum amount of characters to display in a single JTextPane
	private final int MAX_CHARS = 5000;
	
	private JFrame frame;

	private JTabbedPane tabPanel;
	private Map<String, JTextPane> chatPanes;
	
	private JButton btnConnect;
	private JTextField inputField;
	
	private User currentUser;
	
	// Whether or not timestamps should be displayed for messages and notifications
	private boolean useTimestamps = true;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainGUI window = new MainGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Add a new tab to the JTabbedPane
	 * @param name The title of the tab
	 */
	public void newTab(String name) {
		// Create a JTextPane for the chat messages
		final JTextPane chatPane = new JTextPane();
		chatPane.setContentType("text/plain; charset=UTF-8");
		chatPane.setEditable(false);
		
		// Add a mouse listener to allow certain elements in the chat pane to be clickable
		chatPane.addMouseListener(new ClickableListener(chatPane));
		
		// Add a document listener to check for emoticons
		chatPane.getStyledDocument().addDocumentListener(new EmoticonListener());
		
		// Create a JScrollPane for the JTextPane
		JScrollPane chatScrollPane = new JScrollPane();
		chatScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		chatScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		chatScrollPane.setViewportView(chatPane);
		
		// Add the chat pane to the map with its name as key for future references
		chatPanes.put(name, chatPane);
		
		// Add the JScrollPane to a new tab
		tabPanel.addTab(name, null, chatScrollPane, null);
	}
	
	/**
	 * Return an AttributeSet with the given values
	 * @param color
	 * @param font
	 * @param size
	 * @param bold
	 * @param italic
	 * @return The attribute set
	 */
	public AttributeSet getTextStyle(Color color, String font, int size, boolean bold, boolean italic) {
		StyleContext styleContext = StyleContext.getDefaultStyleContext();
		AttributeSet attributeSet = styleContext.addAttribute(SimpleAttributeSet.EMPTY,StyleConstants.Foreground, color);
		attributeSet = styleContext.addAttribute(attributeSet, StyleConstants.FontFamily, font);
		attributeSet = styleContext.addAttribute(attributeSet, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);
		attributeSet = styleContext.addAttribute(attributeSet, StyleConstants.FontSize, size);
		attributeSet = styleContext.addAttribute(attributeSet, StyleConstants.Bold, bold);
		attributeSet = styleContext.addAttribute(attributeSet, StyleConstants.Italic, italic);
		
		// Return the created attribute set
		return attributeSet;
	}
	
	/**
	 * Give a given attribute set the clickable attribute with a given value
	 * @param aSet The old attribute set
	 * @param value The value for the clickable attribute
	 * @return The new attribute set
	 */
	public AttributeSet makeClickable(AttributeSet aSet, String value) {
		return StyleContext.getDefaultStyleContext().addAttribute(aSet, "clickable", value);
	}
	
	/**
	 * Append a message to a chat pane
	 * @param message A ChatMessage object
	 */
	public void append(ChatMessage message) {
		// Get the styled document of the chat pane given by the ChatMessage object
		StyledDocument doc = chatPanes.get(message.getDestination()).getStyledDocument();
		
		// Create the attribute set for the username and make the username clickable
		AttributeSet unameAset = getTextStyle(message.getUser().getColor(), "Tahoma", 12, true, false);
		unameAset = makeClickable(unameAset, message.getUser().getName());
		
		// Create an attribute set with the parameters given by the ChatMessage object
		AttributeSet attributeSet = getTextStyle(message.getColor(), message.getFont(), message.getFontSize(), message.isBold(), message.isItalic());
		
		// Create an attribute set for the timestamps
		AttributeSet timeAset = getTextStyle(Color.gray, "Calibri", 12, false, false);
		
		// Create a timestamp
		SimpleDateFormat sdf = new SimpleDateFormat("[HH:mm]");
		String time = sdf.format(new Date());
		
		// Insert the chat message into the document
		try {
			if (useTimestamps) doc.insertString(doc.getLength(), time  + " ", timeAset);
			doc.insertString(doc.getLength(), message.getUser().getName()  + ": ", unameAset);
			doc.insertString(doc.getLength(), message.getMessage()  + "\n", attributeSet);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		
		// Remove first line if the character limit has been reached
		if (doc.getLength() > MAX_CHARS) {
			Element firstLine = doc.getDefaultRootElement().getElement(0);
			try {
				doc.remove(firstLine.getStartOffset(), firstLine.getEndOffset());
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
		
		// Make sure the scroll bar is set to the end of the text panel
		chatPanes.get(message.getDestination()).setCaretPosition(doc.getLength());
	}
	
	/**
	 * Print a notification with timestamp
	 * @param notification The text for the notification
	 * @param destination The destination of the notification
	 */
	public void notify(String notification, String destination) {
		// Get the styled document of the destination
		StyledDocument doc = chatPanes.get(destination).getStyledDocument();
		
		// Create the attributesets for the timestamp and the notification
		AttributeSet timeAset = getTextStyle(Color.gray, "Calibri", 12, false, false);
		AttributeSet attributeSet = getTextStyle(Color.gray, "Calibri", 16, false, true);
		
		// Create a timestamp for the notification
		SimpleDateFormat sdf = new SimpleDateFormat("[HH:mm]");
		String time = sdf.format(new Date());
		
		// Insert the notification into the document
		try {
			if (useTimestamps) doc.insertString(doc.getLength(), time + " ", timeAset);
			doc.insertString(doc.getLength(), notification + "\n", attributeSet);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		
		// Remove first line if the character limit has been reached
		if (doc.getLength() > MAX_CHARS) {
			Element firstLine = doc.getDefaultRootElement().getElement(0);
			try {
				doc.remove(firstLine.getStartOffset(), firstLine.getEndOffset());
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
		
		// Make sure the scroll bar is set to the end of the text panel
		chatPanes.get(destination).setCaretPosition(doc.getLength());
	}
	
	/**
	 * @return The title of the active tab
	 */
	public String getActiveTab() {
		return tabPanel.getTitleAt(tabPanel.getSelectedIndex());
	}

	/**
	 * @return The user object for the user using the chat
	 */
	public User getCurrentUser() {
		return currentUser;
	}
	
	/**
	 * Called when a button is pressed
	 * @param ActionEvent The action event
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// Save which button called the event
		Object source = e.getSource();
		
		if (source.equals(btnConnect)) {
			//TODO connect button action
			notify("Connecting to server", getActiveTab());
		}
	}
	
	@Override
	public void update(Observable o, Object arg) {
		// TODO update method
		
	}
	
	/**
	 * Create the application.
	 */
	public MainGUI() {
		// Initialize the GUI
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		chatPanes = new HashMap<>();

        try {
        	// Try to get the system's look and feel
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			
			// Make sure the JTabbedPane has no borders
			UIManager.getDefaults().put("TabbedPane.contentBorderInsets", new Insets(0,0,0,0));
			UIManager.getDefaults().put("TabbedPane.tabsOverlapBorder", true);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			// Keep the standard look and feel when an error occurs
		}
        
        // Initialize the JFrame
		frame = new JFrame();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("Images/chat.png"));
		frame.setTitle("Twitch IRC");
		frame.setSize(500, 500);
		frame.setMinimumSize(new Dimension(300,300));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		
		// Initialize the tabbed pane
		tabPanel = new JTabbedPane(JTabbedPane.TOP);
		tabPanel.setFocusable(false);
		frame.getContentPane().add(tabPanel, BorderLayout.CENTER);
		
		// Create and add the north panel
		JPanel northPanel = new JPanel();
		frame.getContentPane().add(northPanel, BorderLayout.NORTH);
		
		// Create and add the south panel
		JPanel southPanel = new JPanel();
		frame.getContentPane().add(southPanel, BorderLayout.SOUTH);
		southPanel.setLayout(new GridLayout(0, 1, 0, 0));
		
		// Create and add the input panel
		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new GridLayout(0, 1, 0, 0));
		inputPanel.setBorder(new EmptyBorder(5, 10, 5, 10));
		southPanel.add(inputPanel);
		
		// Create the input text field
		inputField = new JTextField();
		inputField.addKeyListener(new TextFieldKeyListener(this));
		inputPanel.add(inputField);
		
		// Create and add the button panel
		JPanel buttonPanel = new JPanel();
		southPanel.add(buttonPanel);
		
		// Create the connect button
		btnConnect = new JButton("Connect");
		buttonPanel.add(btnConnect);
		btnConnect.setFocusable(false);
		btnConnect.addActionListener(this);
		
		// Create and add the west panel
		JPanel westPanel = new JPanel();
		frame.getContentPane().add(westPanel, BorderLayout.WEST);
		
		// Create and add the east panel
		JPanel eastPanel = new JPanel();
		frame.getContentPane().add(eastPanel, BorderLayout.EAST);
		
		// Create the tab containing the main chat room
		newTab("Chat");
		
		currentUser = new User("TestUser", null);
	}
}
