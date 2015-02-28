package dataobjects;

import java.util.List;

/**
 * Container class for twitch emoticons
 * 
 * @author Frank
 */
public class EmoticonContainer {

	private List<Emoticons> emoticons;
	
	/**
	 * Add an emoticon to the list
	 * @param emoticon The emoticon to add
	 */
    public void addToList(Emoticons emoticon) {
    	emoticons.add(emoticon);
    }

    /**
     * Get the list of emoticons
     * @return The emoticon list
     */
    public List<Emoticons> getEmoticonsList() {
        return emoticons;
    }
	
}
