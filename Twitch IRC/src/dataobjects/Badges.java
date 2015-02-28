package dataobjects;

/**
 * Badges object containing badge data for a certain twitch channel
 * 
 * @author Frank
 */
public class Badges {

	private Global_mod global_mod;
	private Admin admin;
	private Mod mod;
	private Broadcaster broadcaster;
	private Subscriber subscriber;
	private Turbo turbo;
	
	/**
	 * @return the global_mod
	 */
	public Global_mod getGlobal_mod() {
		return global_mod;
	}
	
	/**
	 * @param global_mod the global_mod to set
	 */
	public void setGlobal_mod(Global_mod global_mod) {
		this.global_mod = global_mod;
	}
	
	/**
	 * @return the admin
	 */
	public Admin getAdmin() {
		return admin;
	}
	
	/**
	 * @param admin the admin to set
	 */
	public void setAdmin(Admin admin) {
		this.admin = admin;
	}
	
	/**
	 * @return the mod
	 */
	public Mod getMod() {
		return mod;
	}
	
	/**
	 * @param mod the mod to set
	 */
	public void setMod(Mod mod) {
		this.mod = mod;
	}
	
	/**
	 * @return the broadcaster
	 */
	public Broadcaster getBroadcaster() {
		return broadcaster;
	}
	
	/**
	 * @param broadcaster the broadcaster to set
	 */
	public void setBroadcaster(Broadcaster broadcaster) {
		this.broadcaster = broadcaster;
	}
	
	/**
	 * @return the subscriber
	 */
	public Subscriber getSubscriber() {
		return subscriber;
	}
	
	/**
	 * @param subscriber the subscriber to set
	 */
	public void setSubscriber(Subscriber subscriber) {
		this.subscriber = subscriber;
	}
	
	/**
	 * @return the turbo
	 */
	public Turbo getTurbo() {
		return turbo;
	}
	
	/**
	 * @param turbo the turbo to set
	 */
	public void setTurbo(Turbo turbo) {
		this.turbo = turbo;
	}
}
