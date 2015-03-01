package gui;

import irc.IRCClient;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Class for displaying the login screen
 * 
 * @author Frank
 */
public class LoginScreen implements ActionListener {

	private JFrame frame;
	private JButton loginButton;
	
	private JTextField unameField;
	private JTextField passwordField;
	
	private IRCClient client;
	private MainGUI gui;
	
	/**
	 * Initialize the login screen
	 */
	public LoginScreen(IRCClient client, MainGUI gui) {
		this.client = client;
		this.gui = gui;
		
        try {
        	// Try to get the system's look and feel
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			// Keep the standard look and feel if an error occurs
		}
        
        // Create the frame
		frame = new JFrame();
		frame.setTitle("Login");
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("Images/TwitchIcon.png"));
		frame.setMinimumSize(new Dimension(200,200));
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		
		// Create the login panel
		JPanel loginPanel = new JPanel(new GridLayout(3, 1));
		
		// Create the username panel
		JPanel unamePanel = new JPanel(new GridBagLayout());
		JLabel unameLabel = new JLabel("User name: ");
		unameField = new JTextField();
		unameField.setSize(new Dimension(200, 30));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridy = 0;
		unamePanel.add(unameLabel, gbc);
		gbc.gridy = 1;
		unamePanel.add(unameField, gbc);
		loginPanel.add(unamePanel);
		
		// Create the password panel
		JPanel passwordPanel = new JPanel(new GridBagLayout());
		JLabel passwordLabel = new JLabel("Password: ");
		passwordField = new JTextField();
		unameField.setSize(new Dimension(100, 30));
		gbc.gridy = 0;
		passwordPanel.add(passwordLabel, gbc);
		gbc.gridy = 1;
		passwordPanel.add(passwordField, gbc);
		loginPanel.add(passwordPanel);
		
		// Create the button panel
		JPanel buttonPanel = new JPanel();
		loginButton = new JButton("Login");
		loginButton.setFocusable(false);
		loginButton.addActionListener(this);
		buttonPanel.add(loginButton);
		loginPanel.add(buttonPanel);
		
		frame.getContentPane().add(loginPanel);
		
		frame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		
		if (source.equals(loginButton)) {
			client.login(unameField.getText(), passwordField.getText());
			gui.getUserList().addUser(unameField.getText(), "Log");
			gui.setCurrentUser(gui.getUserList().getUser(unameField.getText()));
			frame.dispose();
		}
	}
}
