package dataobjects;

import java.awt.Color;
import java.util.Random;

/**
 * Contains information about the user connected to the chat
 * 
 * @author Frank
 */
public class User {

	private String name;
	private Color color;
	
	/**
	 * Constructor with default values
	 */
	public User() {
		this.name = "Anonymous";
		
		// Choose a random color for the user name
		Random random = new Random();
		float hue = random.nextFloat();
		float saturation = 1f;
		float luminance = 0.8f;
		this.color = Color.getHSBColor(hue, saturation, luminance);
	}
	
	/**
	 * Constructor with assignments
	 * @param name The username
	 * @param color The color for the username, random color is chosen if null
	 */
	public User(String name, Color color) {
		this.name = name;
		
		// Set the color or choose a random bright color if the given color is null
		Random random = new Random();
		float hue = random.nextFloat();
		float saturation = 1f;
		float brightness = 0.85f;
		
		this.color = color != null ? color : Color.getHSBColor(hue, saturation, brightness);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(Color color) {
		this.color = color;
	}
}
