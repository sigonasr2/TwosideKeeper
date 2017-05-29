package sig.plugin.TwosideKeeper;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;

public class GlobalLoot {
	Item item;
	String lootname;
	UUID item_uuid;
	HashMap<UUID,Inventory> drop_inventories = new HashMap<UUID,Inventory>();
	HashMap<UUID,Long> last_opened_loot = new HashMap<UUID,Long>();
	
	GlobalLoot(Location spawnLoc, String lootName) {
		item = (Item)spawnLoc.getWorld().dropItemNaturally(spawnLoc, new ItemStack(Material.CHEST));
		item_uuid = item.getUniqueId();
		item.setCustomName(lootName);
		item.setCustomNameVisible(true);
		item.setPickupDelay(Integer.MAX_VALUE);
		item.setInvulnerable(true);
		this.lootname = lootName;
	}
	
	public void runInventoryCloseEvent(InventoryCloseEvent ev) {
		Player p = (Player)ev.getPlayer();
		if (drop_inventories.containsKey(p.getUniqueId()) &&
				ev.getInventory().getTitle()!=null &&
				ev.getInventory().getTitle().equalsIgnoreCase(lootname)) {
			last_opened_loot.put(p.getUniqueId(), TwosideKeeper.getServerTickTime());
		}
	}
	
	public boolean runTick() {
		if ((item!=null && item.isValid())) {
			List<Player> players = GenericFunctions.getNearbyPlayers(item.getLocation(), 1.5);
			for (Player p : players) {
				if (p.getOpenInventory().getType()==InventoryType.CRAFTING &&
						drop_inventories.containsKey(p.getUniqueId())) {
					if (!last_opened_loot.containsKey(p.getUniqueId()) ||
							last_opened_loot.get(p.getUniqueId())+100<=TwosideKeeper.getServerTickTime()) {
						last_opened_loot.put(p.getUniqueId(), TwosideKeeper.getServerTickTime());
						p.openInventory(drop_inventories.get(p.getUniqueId()));
					}
				}
			}
			return true;
		} else {
			return false;
		}
	}
	
	public Item getItem() {
		return item;
	}
	
	public UUID getItemUniqueID() {
		return item_uuid;
	}
	
	public void addNewDropInventory(Player p, ItemStack...lootitems) {
		if (drop_inventories.containsKey(p.getUniqueId())) {
			Inventory inv = drop_inventories.get(p.getUniqueId());
			inv.addItem(lootitems);
		} else {
			Inventory newinv = Bukkit.createInventory(p, ((((lootitems.length-1)/9)+1)*9),this.lootname);
			newinv.addItem(lootitems);
			drop_inventories.put(p.getUniqueId(), newinv);
		}
	}
	
	public void openDropInventory(Player p) {
		if (drop_inventories.containsKey(p.getUniqueId())) {
			p.openInventory(drop_inventories.get(p.getUniqueId()));
		} else {
			TwosideKeeper.log("WARNING! Drop Inventory for Player with UUID <"+p.getUniqueId()+"> does not have an associated inventory with Global Loot <"+item.getUniqueId()+">. THIS SHOULD NOT BE HAPPENING!!", 1);
			p.sendMessage(ChatColor.RED+"Something terrible has happened! "+ChatColor.RESET+"Please let the server administrator know about this.");
		}
	}
	
	public static GlobalLoot spawnGlobalLoot(Location loc, String lootName) {
		GlobalLoot loot = new GlobalLoot(loc,lootName);
		TwosideKeeper.globalloot.put(loot.getItem().getUniqueId(), loot);
		return loot;
	}
}
