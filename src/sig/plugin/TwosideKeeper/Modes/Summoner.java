package sig.plugin.TwosideKeeper.Modes;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import sig.plugin.TwosideKeeper.PlayerStructure;
import sig.plugin.TwosideKeeper.HelperStructures.PetState;

public class Summoner {
	/**
	 * 
	 * @return False means stop the action from passing onto other sections of code.
	 */
	public static boolean HandleSummonerInteraction(Event e) {
		if (e instanceof PlayerInteractEvent) {
			PlayerInteractEvent ev = (PlayerInteractEvent)e;
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(ev.getPlayer());
			if (ev.getAction()==Action.RIGHT_CLICK_BLOCK && pd.myPet!=null && pd.myPet.getState()==PetState.PASSIVE) {
				PetMoveCommand(ev.getClickedBlock().getLocation(),ev.getPlayer());
			}
		} else
		if (e instanceof PlayerInteractEntityEvent) {
			PlayerInteractEntityEvent ev = (PlayerInteractEntityEvent)e;
		}
		return false;
	}

	private static void PetMoveCommand(Location targetLoc, Player p) {
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		if (pd.myPet!=null) {
			pd.myPet.setTargetLocation(targetLoc);
			pd.myPet.setState(PetState.MOVING);
		}
	}
}
