package utils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Ensure that we don't send more than MAX_BUFFERSIZE messages every MAX_TIME milliseconds
 * 
 * @author Frank
 */
public class OutQueue implements Runnable {

	// The maximum amount of messages in the buffer
	public final int MAX_BUFFERSIZE;
	
	// The time in milliseconds for which the message limit counts
	public final int MAX_TIME;
	
	// The buffer
	private List<Long> buffer = new LinkedList<>();
	
	// The message queue
	private List<String> queue = new LinkedList<>();
	
	// The out stream
	private BufferedWriter out;
	
	private boolean run;

	/**
	 * Constructor
	 * @param out The output stream
	 * @param size The buffer size
	 * @param time The time for which the limit counts in milliseconds
	 */
	public OutQueue(BufferedWriter out, int size, int time) {
		this.out = out;
		this.MAX_BUFFERSIZE = size;
		this.MAX_TIME = time;
		
		run = true;
	}
	
	/**
	 * Send the data if the buffer is not full
	 */
	@Override
	public void run() {
		while (run && !Thread.interrupted()) {
			// Check if there is a message in the queue and the buffer is not full
			if (queue.size() > 0 && buffer.size() < MAX_BUFFERSIZE) {
				try {
					// Write the message to the out stream
					out.write(queue.get(0));
					out.flush();
					
					// Remove the message from the queue and add a time stamp to the buffer
					queue.remove(0);
					buffer.add(System.currentTimeMillis());
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			// If there is a message in the queue and the buffer has reached its maximum size
			} else if (queue.size() > 0 && buffer.size() >= MAX_BUFFERSIZE) {
				// Check if the first entry was added to the buffer more than MAX_TIME milliseconds ago
				if ((System.currentTimeMillis() - buffer.get(0)) > MAX_TIME) {
					try {
						// Remove the time stamp from the buffer
						buffer.remove(0);
						
						// Write the message to the out stream
						out.write(queue.get(0));
						out.flush();
						
						// Remove the message from the queue and add a time stamp to the buffer
						queue.remove(0);
						buffer.add(System.currentTimeMillis());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}	
	
	/**
	 * Add a message to the queue
	 */
	public void addMessage(String m) {
		this.queue.add(m);
	}
	
}
