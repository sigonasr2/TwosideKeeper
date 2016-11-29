package sig.plugin.TwosideKeeper.HelperStructures.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SoundUtils {

	/**
	 * Plays a sound to everyone in the world. Nearby players should be able to hear it.
	 */
	public static void playGlobalSound(Location loc, Sound sound, float vol, float pitch) {
		loc.getWorld().playSound(loc, sound, vol, pitch);
	}
	/**
	 * Same as playGlobalSound. Just done for every single player locally.
	 */
	public static void playIndividualGlobalSound(Location loc, Sound sound, float vol, float pitch) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			p.playSound(loc, sound, vol, pitch);
		}
	}
	/**
	 * Plays a sound at the player's location, as if they were hearing a regular sound in the client.
	 */
	public static void playLocalSound(Player p, Sound sound, float vol, float pitch) {
		SoundUtils.playLocalSound(p,p.getLocation(), sound, vol, pitch);
	}
	/**
	 * Plays a sound at the specified location for a single player, as if they were hearing a regular sound in the client.
	 */
	public static void playLocalSound(Player p, Location loc, Sound sound, float vol, float pitch) {
		p.playSound(loc, sound, vol, pitch);
	}
	/**
	 * Plays a sound at the player's location for every player, as if they were hearing a regular sound in the client. Useful for notifications/pings.
	 */
	public static void playLocalGlobalSound(Sound sound, float vol, float pitch) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			SoundUtils.playLocalSound(p, sound, vol, pitch);
		}
	}
	
}
