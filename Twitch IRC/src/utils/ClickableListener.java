package utils;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.Element;

/**
 * Mouse listener that checks if a clicked element in a JTextPane has the clickable attribute
 * 
 * @author Frank
 */
public class ClickableListener implements MouseListener {

	private JTextPane textPane;
	private String tag = "clickable";

	/**
	 * Constructor
	 * @param textPane The JTextPane to listen in
	 */
	public ClickableListener(JTextPane textPane) {
		this.textPane = textPane;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// Get the clicked element
		Element element = textPane.getStyledDocument().getCharacterElement(textPane.viewToModel(e.getPoint()));
		
		// Get the attribute set of the clicked element
		AttributeSet attributeSet = element.getAttributes();
		String value = (String) attributeSet.getAttribute(tag);
		
		// Check the element contained the clickable attribute
		if (value != null) {
			System.out.println("Clicked an element with value: " + value);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

}
