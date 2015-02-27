package utils;

import java.awt.EventQueue;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 * Document listener that checks for emoticons and inserts them in the document that was changed
 * 
 * @author Frank
 */
public class EmoticonListener implements DocumentListener {

	private Map<String, ImageIcon> regexMap;
	
	/**
	 * Constructor
	 */
	public EmoticonListener() {
		regexMap = new HashMap<>();
		
		// Add emoticons and their regexes to the regexMap
		//regexMap.put(":D", new ImageIcon(ImageIO.read(new File("Images/SmileyD.png"))));
	}

	@Override
	public void changedUpdate(DocumentEvent e) {	
	}
	
	/**
	 * Insert emoticons in users' messages if applicable
	 */
	@Override
	public void insertUpdate(final DocumentEvent e) {
		// SwingUtilities.invokeLater so we don't try to change the document before the lock is released 
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				// Get the document that changed
				StyledDocument doc = (StyledDocument) e.getDocument();
				
				// Get the text that was inserted
				String insertion = null;
				
				try {
					insertion = doc.getText(e.getOffset(), e.getLength());
				} catch (BadLocationException ex) {
					ex.printStackTrace();
				}
				
				// Go through all possible emoticons
				for (String regex : regexMap.keySet()) {
					// The index of the emoticon in the insertion
					int i = insertion.indexOf(regex);
					
					try {
						// If the text contained the regex of the emoticon
						while (i >= 0) {
							// Make an attributeset with the attributes of the regex
							SimpleAttributeSet attributeSet = new SimpleAttributeSet(doc.getCharacterElement(e.getOffset() + i).getAttributes());
							
							// Check if there is no icon yet
							if (StyleConstants.getIcon(attributeSet) == null) {
			
								// Set the icon to the corresponding regex
								StyleConstants.setIcon(attributeSet, regexMap.get(regex));
			
								// Remove the regex string and insert the icon
								doc.remove(e.getOffset() + i, regex.length());
								doc.insertString(e.getOffset() + i, regex, attributeSet);
							}
							
							// Set i to the index of the next occurrence of the regex
							i = insertion.indexOf(regex, i + regex.length());
						}
					} catch (BadLocationException ex) {
						
					}
				}
			}
		});
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
	}
}
