package sig.plugin.TwosideKeeper;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;

import sig.plugin.TwosideKeeper.HelperStructures.SpleefArena;

public class SpleefManager {
	/*
	 * Manages all spleef games.
	 * Contains an array for every type of spleef game available
	 * along with a SpleefGame structure.
	 */
	static List<SpleefGame> spleef_game_list;
	TwosideKeeper plugin;
	public SpleefManager(TwosideKeeper plug) {
		plugin = plug;
		spleef_game_list = new ArrayList<SpleefGame>();
	}
	public List<SpleefGame> GetSpleefGames() {
		return spleef_game_list;
	}
	public List<SpleefGame> GetActiveSpleefGames() {
		List<SpleefGame> active_spleef_game_list = new ArrayList<SpleefGame>();
		for (int i=0;i<spleef_game_list.size();i++) {
			if (spleef_game_list.get(i).isActive()) {
				active_spleef_game_list.add(spleef_game_list.get(i));
			}
		}
		return active_spleef_game_list;
	}
	public void SetupSpleefArena(SpleefArena id,Location corner1,Location corner2,Location shovel_block, Location register_sign) {
		SpleefGame newGame = new SpleefGame(plugin, id, corner1,corner2,shovel_block,register_sign);
		spleef_game_list.add(newGame);
		TwosideKeeper.log("Added new SpleefGame: "+newGame.toString(),3);
	}
	public void SetupSpleefArena(SpleefArena id, Location corner1, Location corner2, Location shovel_block, Location shovel_block2, Location register_sign) {
		SpleefGame newGame = new SpleefGame(plugin, id, corner1,corner2,shovel_block,shovel_block2,register_sign);
		spleef_game_list.add(newGame);
		TwosideKeeper.log("Added new SpleefGame: "+newGame.toString(),3);
	}
	public void PassEvent(Event e) {
		//Passes events in the world to all Spleef Games.
		for (int i=0;i<spleef_game_list.size();i++) {
			spleef_game_list.get(i).EventListener(e);
		}
	}
	public void TickEvent() {
		//Indicates a tick event from the server (once per second)
		for (int i=0;i<spleef_game_list.size();i++) {
			spleef_game_list.get(i).Tick();
		}
	}
	
	public static boolean playerIsPlayingSpleef(Player p) {
		for (int i=0;i<spleef_game_list.size();i++) {
			if (spleef_game_list.get(i).registered_players.contains(p)) {
				return true;
			}
		}
		return false;
	}
}