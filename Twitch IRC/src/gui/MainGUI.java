package gui;

import irc.IRCClient;
import irc.IRCProtocol;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
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

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
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

import utils.ChatMessageBuilder;
import utils.ClickableListener;
import utils.EmoticonInserter;
import utils.TextFieldKeyListener;
import TwitchAPI.ChannelData;
import dataobjects.Channel;
import dataobjects.ChatMessage;
import dataobjects.ConnectedUsers;
import dataobjects.Emoticons;
import dataobjects.User;

/**
 * Main graphical user interface for the chat
 * 
 * @author Frank
 */
public class MainGUI implements ActionListener, Observer {
	
	// Maximum amount of characters to display in a single JTextPane
	private final int MAX_CHARS = 5000;
	
	private final String LOGTAB = "Log";
	
	private JFrame frame;

	private JTabbedPane tabPanel;
	private Map<String, JTextPane> chatPanes;
	
	private JMenuBar menuBar;
	private JMenu mActions;
	private JMenuItem miConnect;
	private JMenuItem miLogin;
	private JMenuItem miJoinChannel;
	private JMenuItem miLeaveChannel;
	
	private JTextField inputField;
	
	private User currentUser;
	
	private EmoticonInserter emoticonInserter;
	
	private IRCClient client;
	private ConnectedUsers userList;
	private Map<String, Channel> channels;
	
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
	 * Create the application.
	 */
	public MainGUI() {
		// Initialize the GUI
		initialize();
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
		chatPane.setCursor(new Cursor(Cursor.TEXT_CURSOR));
		
		// Add a mouse listener to allow certain elements in the chat pane to be clickable
		chatPane.addMouseListener(new ClickableListener(chatPane));
		
		// Create a JScrollPane for the JTextPane
		JScrollPane chatScrollPane = new JScrollPane();
		chatScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		chatScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		chatScrollPane.setViewportView(chatPane);
		
		// Add the chat pane to the map with its name as key for future references
		chatPanes.put(name, chatPane);
		
		// Add the JScrollPane to a new tab
		tabPanel.addTab(name, null, chatScrollPane, null);
		
		tabPanel.setSelectedIndex(tabPanel.getTabCount() - 1);
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
	 * Get the badge for the given role from the given channel
	 * @param role The role
	 * @param channel The channel
	 * @return An attributeset containing the badge
	 */
	public SimpleAttributeSet getBadge(String role, String channel) {
		SimpleAttributeSet attributeSet = new SimpleAttributeSet();
		if (channels.containsKey(channel)) {
			switch (role) {
			case "subscriber":
				StyleConstants.setIcon(attributeSet, channels.get(channel).getSubscriberBadge());
				break;
			case "turbo":
				StyleConstants.setIcon(attributeSet, channels.get(channel).getTurboBadge());
				break;
			case "mod":
				StyleConstants.setIcon(attributeSet, channels.get(channel).getModBadge());
				break;
			case "broadcaster":
				StyleConstants.setIcon(attributeSet, channels.get(channel).getBroadcasterBadge());
				break;
			case "admin":
				StyleConstants.setIcon(attributeSet, channels.get(channel).getAdminBadge());
				break;
			}
		}
		
		return attributeSet;
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
			// Capitalize the first letter of the username
			String name = message.getUser().getName().substring(0, 1).toUpperCase() + message.getUser().getName().substring(1);
			
			if (useTimestamps) doc.insertString(doc.getLength(), time  + " ", timeAset);
			
			// Add a badge for each role this user has
			for (String role : message.getUser().getRoles(message.getDestination())) {
				doc.insertString(doc.getLength(), role, getBadge(role, message.getDestination()));
				doc.insertString(doc.getLength()," ", unameAset);
			}
			
			doc.insertString(doc.getLength(),name  + ": ", unameAset);
			doc.insertString(doc.getLength(), message.getMessage()  + "\n", attributeSet);
			
			emoticonInserter.insertEmoticons(doc, message.getMessage().length() + 1);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		
		// Remove first line if the character limit has been reached
		if (doc.getLength() > MAX_CHARS) {
			Element firstLine = doc.getDefaultRootElement().getElement(0);
			try {
				doc.remove(firstLine.getStartOffset(), firstLine.getEndOffset());
			} catch (BadLocationException ex) {
				ex.printStackTrace();
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
			} catch (BadLocationException ex) {
				ex.printStackTrace();
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
	 * @return The IRC client object
	 */
	public IRCClient getClient() {
		return client;
	}
	
	/**
	 * Called when a button is pressed
	 * @param ActionEvent The action event
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// Save which button called the event
		Object source = e.getSource();
		
		// If the connect menu item was pressed
		if (source.equals(miConnect)) {
			if (client == null) {
				// Create the client and add the GUI as an observer
				client = new IRCClient(IRCProtocol.TWITCH_HOST, userList);
				client.addObserver(this);
				
				Thread clientThread = new Thread(client);
				clientThread.start();
			} else {
				notify("You are already connected.", LOGTAB);
			}
		}
		
		// If the login menu item was pressed
		if (source.equals(miLogin)) {
			if (client != null && client.isConnected() && !client.isLoggedIn()) {
				new LoginScreen(client, this);
				
				inputField.setEnabled(true);
			} else {
				notify("The client is not connected or you are already logged in.", LOGTAB);
			}
		}
		
		// If the join menu item was pressed
		if (source.equals(miJoinChannel)) {
			if (client != null && client.isLoggedIn()) {
				String channel = (String) JOptionPane.showInputDialog(frame, "Enter the channel you wish to join:\n", "Join a channel", JOptionPane.PLAIN_MESSAGE, null, null, "");
				
				if (channel != null) {
					channel = channel.toLowerCase();
					client.sendMessage("JOIN #" + channel + "\r\n");
					newTab(channel);
					
					// Get a channel object via the twitch api and add it to the channel list
					Channel channelObject = ChannelData.getChannel(channel);
					channels.put(channel, channelObject);
					
					// Add the channel's emoticons to the emoticon listener
					for (Emoticons emoticon : channelObject.getEmoticons().getEmoticonsList()) {
						emoticonInserter.addEmoticon(emoticon.getRegex(), emoticon.getUrl());
					}
					
					notify("You have joined the channel " + channel + ".", LOGTAB);
				}
			}
		}
		
		// If the leave menu item was pressed
		if (source.equals(miLeaveChannel)) {
			String channel = getActiveTab();
			
			// Don't leave the log channel
			if (!channel.equals(LOGTAB)) {
				client.sendMessage("PART #" + channel + "\n\r");
				this.tabPanel.removeTabAt(tabPanel.getSelectedIndex());
				channels.remove(channel);

				notify("You have left the channel " + channel + ".", LOGTAB);
			}
		}
	}
	
	/**
	 * Set the currently logged in user
	 * @param u The user to set
	 */
	public void setCurrentUser(User u) {
		this.currentUser = u;
	}
	
	/**
	 * @return An object containing a list of connected users
	 */
	public ConnectedUsers getUserList() {
		return this.userList;
	}
	
	@Override
	public void update(Observable o, Object arg) {
		// Split the command on up to 4 whitespaces
		String[] command = ((String) arg).split("\\s", 4);

		// Check the command type
		switch (command[0]) {
			case "NOTIFY":
				// Received a notification from the IRC client
				notify(command[3], command[1]);
				break;
			case "DISCONNECTED":
				// The client has disconnected
				client = null;
				
				// Remove the all tabs except for the log tab
				for (int i = 1; i < tabPanel.getTabCount(); i++) {
					tabPanel.removeTabAt(i);
				}
				
				notify(command[3], command[1]);
				break;
			case "MESSAGE":
				// Received a message from the IRC client
				if (channels.containsKey(command[1])) {
					if (command[3].startsWith(IRCProtocol.ACTION)) {
						append(ChatMessageBuilder.getActionMessage(command[3].substring(IRCProtocol.ACTION.length()), command[1], userList.getUser(command[2])));
					} else {
						append(ChatMessageBuilder.getRegularMessage(command[3], command[1], userList.getUser(command[2])));
					}
				}
				break;
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		userList = new ConnectedUsers();
		chatPanes = new HashMap<>();
		channels = new HashMap<>();
		emoticonInserter = new EmoticonInserter();

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
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("Images/TwitchIcon.png"));
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
		
		//Create the menu bar
		menuBar = new JMenuBar();
		mActions = new JMenu("Actions");
		
		miConnect = new JMenuItem("Connect");
		miConnect.addActionListener(this);
		miLogin = new JMenuItem("Login");
		miLogin.addActionListener(this);
		miJoinChannel = new JMenuItem("Join channel");
		miJoinChannel.addActionListener(this);
		miLeaveChannel = new JMenuItem("Leave channel");
		miLeaveChannel.addActionListener(this);
		
		mActions.add(miConnect);
		mActions.add(miLogin);
		mActions.add(miJoinChannel);
		mActions.add(miLeaveChannel);
		
		menuBar.add(mActions);
		frame.setJMenuBar(menuBar);
		
		// Create the input text field
		inputField = new JTextField();
		inputField.addKeyListener(new TextFieldKeyListener(this));
		inputField.setEnabled(false);
		inputPanel.add(inputField);
		
		// Create and add the west panel
		JPanel westPanel = new JPanel();
		frame.getContentPane().add(westPanel, BorderLayout.WEST);
		
		// Create and add the east panel
		JPanel eastPanel = new JPanel();
		frame.getContentPane().add(eastPanel, BorderLayout.EAST);
		
		// Create a tab
		newTab(LOGTAB);
	}
}
