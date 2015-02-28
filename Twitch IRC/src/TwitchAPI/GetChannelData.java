package TwitchAPI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.Gson;

import dataobjects.Badges;
import dataobjects.Channel;
import dataobjects.EmoticonContainer;

/**
 * Class for getting channel data from the twitch API
 * 
 * @author Frank
 */
public class GetChannelData {
	
	/**
	 * Get the emoticons that can be used in the given channel
	 * @param name The name of the channel
	 * @return An EmoticonContainer object containing the usable emoticons
	 */
	public static EmoticonContainer getEmoticons(String name) {
		try {
			// Connect to the API
			URL url = new URL(APIUrls.API_BASE + APIUrls.EMOTICONS.replace("CHANNEL", name));
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			
			// Create a stringbuilder and reader
			StringBuilder responseData = new StringBuilder();
			InputStreamReader in = new InputStreamReader((InputStream) connection.getContent());
			BufferedReader reader = new BufferedReader(in);
			
			// Read the Json data
			String line = null;
			while ((line = reader.readLine()) != null) {
				responseData.append(line);
			}
			
			// Create a Gson object and convert the Json to java objects
			Gson gson = new Gson();
			EmoticonContainer emoticons = gson.fromJson(responseData.toString(), EmoticonContainer.class);
			
			// Return the emoticon container
			return emoticons;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Get the badges for a given channel
	 * @param name The name of the channel
	 * @return A Badges object containing the channel's badges
	 */
	public static Badges getBadges(String name) {
		try {
			// Connect to the API
			URL url = new URL(APIUrls.API_BASE + APIUrls.BADGES.replace("CHANNEL", name));
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			
			// Create a stringbuilder and reader
			StringBuilder responseData = new StringBuilder();
			InputStreamReader in = new InputStreamReader((InputStream) connection.getContent());
			BufferedReader reader = new BufferedReader(in);
			
			// Read the Json data
			String line = null;
			while ((line = reader.readLine()) != null) {
				responseData.append(line);
			}
			
			// Create a Gson object and convert the Json to java objects
			Gson gson = new Gson();
			Badges badges = gson.fromJson(responseData.toString(), Badges.class);
			
			// Return the badges
			return badges;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Get a channel object for the given twitch channel
	 * @param name The channel name
	 * @return The channel object for the given twitch channel
	 */
	public static Channel getChannel(String name) {
		Channel channel = new Channel(name);
		channel.setBadges(GetChannelData.getBadges(name));
		channel.setEmoticons(GetChannelData.getEmoticons(name));
		
		return channel;
	}
	
	public static void main(String[] args) {
		Channel channel = GetChannelData.getChannel("massansc");
		System.out.println(channel.getBadges().getSubscriber().getImage());
	}
}
