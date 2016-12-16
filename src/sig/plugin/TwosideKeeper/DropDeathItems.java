package sig.plugin.TwosideKeeper;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import sig.plugin.TwosideKeeper.HelperStructures.Utils.InventoryUtils;

public class DropDeathItems implements Runnable{

	Player p = null;
	Location deathloc = null;
	List<ItemStack> contents;
	Inventory inv_contents;
	
	DropDeathItems(Player p, List<ItemStack> contents, Location deathloc) {
		this.p=p;
		this.deathloc=deathloc;
		this.contents=contents;
		this.inv_contents = Bukkit.createInventory(p, 36);
		for (ItemStack it : contents) {
			if (it!=null) {
				inv_contents.addItem(it);
			}
		}
	}
	
	@Override
	public void run() {
		if (!AttemptToDropItems(p,deathloc)) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(TwosideKeeper.plugin,new DropDeathItems(p,contents,deathloc),1); //Keep trying until the chunk is loaded!!!
			TwosideKeeper.temporary_chunks.clear();
		}
	}

	public boolean AttemptToDropItems(Player p, Location deathloc) {
		TwosideKeeper.temporary_chunks.add(deathloc.getChunk());
		deathloc.getChunk().load();
		if (deathloc.getChunk().isLoaded()) {
			TwosideKeeper.log("Respawn and Dropping...", 2);
			while (!InventoryUtils.hasEmptyInventory(inv_contents)) {
				if (deathloc.getChunk().isLoaded()) {
					Item it = deathloc.getWorld().dropItemNaturally(deathloc, InventoryUtils.getFirstItemThatIsNotEmpty(inv_contents));
					if (it!=null) {
						inv_contents.removeItem(it.getItemStack());
						TwosideKeeper.log("Dropping "+it.getItemStack().toString()+" at Death location "+deathloc,2);
					} else {
						return false;
					}
				} else {
					return false;
				}
			}
			/*while (contents.size()>0) {
				if (deathloc.getChunk().isLoaded()) {
					Item it = null;
					do {
						deathloc.getWorld().loadChunk(deathloc.getChunk());
						it=deathloc.getWorld().dropItemNaturally(deathloc, contents.get(0));
						TwosideKeeper.temporary_chunks.add(deathloc.getChunk());} while (it==null || !it.isValid());
					it.setInvulnerable(true);
					TwosideKeeper.log("Dropping "+contents.get(0).toString()+" at Death location "+deathloc,2);
					//contents.remove(0);
					
				} else {
					deathloc.getWorld().loadChunk(deathloc.getChunk());
				}
			}*/
			for (Chunk c : TwosideKeeper.temporary_chunks) {
				c.unload(true);
			}
			TwosideKeeper.temporary_chunks.clear();
			DeathManager.removeDeathStructure(p);
			return true;
		}
		TwosideKeeper.log("CHUNK DID NOT LOAD! TRYING AGAIN SOON...", 0);
		return false;
	}
}
