package sig.plugin.TwosideKeeper;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DropDeathItems implements Runnable{

	Player p = null;
	Location deathloc = null;
	List<ItemStack> contents;
	
	DropDeathItems(Player p, List<ItemStack> contents, Location deathloc) {
		this.p=p;
		this.deathloc=deathloc;
		this.contents=contents;
	}
	
	@Override
	public void run() {
		if (!AttemptToDropItems(p,deathloc)) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(TwosideKeeper.plugin,new DropDeathItems(p,contents,deathloc),1); //Keep trying until the chunk is loaded!!!
		}
	}

	public boolean AttemptToDropItems(Player p, Location deathloc) {
		deathloc.getWorld().loadChunk(deathloc.getChunk());
		if (deathloc.getChunk().isLoaded()) {
			TwosideKeeper.log("Respawn and Dropping...", 2);
			while (contents.size()>0) {
				if (deathloc.getChunk().isLoaded()) {
					Item it = deathloc.getWorld().dropItemNaturally(deathloc, contents.get(0));
					it.setInvulnerable(true);
					TwosideKeeper.log("Dropping "+contents.get(0).toString()+" at Death location "+deathloc,2);
					contents.remove(0);
				} else {
					deathloc.getWorld().loadChunk(deathloc.getChunk());
				}
			}
			DeathManager.removeDeathStructure(p);
			return true;
		}
		TwosideKeeper.log("CHUNK DID NOT LOAD! TRYING AGAIN SOON...", 0);
		return false;
	}
}
