package sig.plugin.TwosideKeeper;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class DropDeathItems implements Runnable{

	Player p = null;
	Location deathloc = null;
	Inventory contents;
	
	DropDeathItems(Player p, Inventory contents, Location deathloc) {
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
			for (int i=0;i<contents.getSize();i++) {
				if (contents.getItem(i)!=null &&
						contents.getItem(i).getType()!=Material.AIR) {
					Item it = deathloc.getWorld().dropItemNaturally(deathloc, contents.getItem(i));
					it.setInvulnerable(true);
					TwosideKeeper.log("Dropping "+contents.getItem(i).toString()+" at Death location "+deathloc,2);
				}
			}
			DeathManager.removeDeathStructure(p);
			return true;
		}
		TwosideKeeper.log("CHUNK DID NOT LOAD! TRYING AGAIN SOON...", 0);
		return false;
	}
}
