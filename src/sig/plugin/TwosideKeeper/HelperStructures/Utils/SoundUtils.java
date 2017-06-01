package sig.plugin.TwosideKeeper.HelperStructures.Utils;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.Classes.SoundData;

public class SoundUtils {

	/**
	 * Plays a sound to everyone in the world. Nearby players should be able to hear it.
	 */
	public static void playGlobalSound(Location loc, Sound sound, float vol, float pitch) {
		loc.getWorld().playSound(loc, sound, vol, pitch);
		//playIndividualGlobalSound(loc,sound,vol,pitch);
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

	public static float DetermineItemPitch(ItemStack item) {
		double pitch = 1.0f;
		double variance = 0.1f;
		Random r = new Random();
		r.setSeed(item.getType().name().hashCode());
		double newnumb = r.nextFloat();
		double newpitch = pitch+variance-(newnumb*(variance*2d));
		TwosideKeeper.log("Next float:"+newnumb+" | Pitch: "+newpitch, 5);
		return (float)newpitch;
	}
	
	/**
	 * Plays sounds back to back with the tickdelay specified between each sound until all sounds have been played.
	 */
	public static void playLocalSoundsWithDelay(int tickdelay,Player p,SoundData...sounds) {
		for (int i=0;i<sounds.length;i++) {
			final int val = i;
			Bukkit.getScheduler().runTaskLater(TwosideKeeper.plugin, ()->{
				SoundUtils.playLocalSound(p, sounds[val].getSound(), sounds[val].getVolume(), sounds[val].getPitch());
			}, i*tickdelay);
		}
	}
}
