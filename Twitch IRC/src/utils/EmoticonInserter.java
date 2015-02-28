package utils;

import java.io.IOException;
import java.net.URL;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 * Class for inserting emoticons in a document
 * 
 * @author Frank
 */
public class EmoticonInserter {

	// Map containing the emoticons mapped to their regex
	private Map<String, ImageIcon> regexMap;
	
	/**
	 * Constructor
	 */
	public EmoticonInserter() {
		regexMap = new HashMap<>();
	}
	
	/**
	 * Add an emoticon to the listener
	 * @param regex The regex for the emoticon
	 * @param url The url to the image
	 */
	public void addEmoticon(String regex, String url) {
		try {
			if (!regexMap.containsKey(regex)) {
				regexMap.put(regex, new ImageIcon(ImageIO.read(new URL(url))));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void insertEmoticons(StyledDocument doc, int length) {	
		// Get the text that was inserted
		String insertion = null;
		int offset = doc.getLength() - length;
		
		try {
			insertion = doc.getText(offset, length);
		} catch (BadLocationException ex) {
			ex.printStackTrace();
		}
		
		// Go through all possible emoticons
		if (insertion != null) {
			try {
				Set<String> regexes = regexMap.keySet();
				for (String regex : regexes) {
					// Get the index of the emoticon in the insertion
					Pattern pattern = Pattern.compile(regex);
					Matcher matcher = pattern.matcher(insertion);
					
					int i = -1;
					if (matcher.find()) {
						try {
							i = matcher.start();
						} catch (IllegalStateException e) {
							
						}
					}
					
						// If the text contained the regex of the emoticon
						while (i >= 0) {
							// Make an attributeset with the attributes of the regex
							SimpleAttributeSet attributeSet = new SimpleAttributeSet(doc.getCharacterElement(offset + i).getAttributes());
							
							// Check if there is no icon yet
							if (StyleConstants.getIcon(attributeSet) == null) {
			
								// Set the icon to the corresponding regex
								StyleConstants.setIcon(attributeSet, regexMap.get(regex));
			
								// Remove the regex string and insert the icon
								doc.remove(offset + i, matcher.group().length());
								doc.insertString(offset + i, matcher.group(), attributeSet);
							}
							
							// Set i to the index of the next occurrence of the regex
							i = insertion.indexOf(regex, i + regex.length());
						}
				}
			} catch (BadLocationException | ConcurrentModificationException ex) {
				ex.printStackTrace();
			}
		}
	}
}
