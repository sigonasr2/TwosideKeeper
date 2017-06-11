package sig.plugin.TwosideKeeper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import sig.plugin.TwosideKeeper.HelperStructures.WorldShop;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.InventoryUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.ItemUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.TextUtils;

public class GlobalLoot {
	Item item;
	public static List<Chunk> chunks = new ArrayList<Chunk>();
	Chunk c;
	String lootname;
	UUID item_uuid;
	HashMap<UUID,Inventory> drop_inventories = new HashMap<UUID,Inventory>();
	HashMap<UUID,Long> last_opened_loot = new HashMap<UUID,Long>();
	
	GlobalLoot(Location spawnLoc, String lootName) {
		Chunk c = spawnLoc.getChunk();
		if (!chunks.contains(c)) {
			chunks.add(c);
			c.load();
		}
		TwosideKeeper.temporary_chunks.add(c);
		item = (Item)spawnLoc.getWorld().dropItemNaturally(spawnLoc, new ItemStack(Material.CHEST));
		//item = GenericFunctions.dropItem(new ItemStack(Material.CHEST), spawnLoc);
		item_uuid = item.getUniqueId();
		item.setCustomName(TextUtils.RandomColor()+ChatColor.stripColor(lootName));
		item.setCustomNameVisible(true);
		item.setPickupDelay(Integer.MAX_VALUE);
		item.setInvulnerable(true);
		this.lootname = lootName;
		TwosideKeeper.temporary_chunks.remove(c);
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
					if ((!last_opened_loot.containsKey(p.getUniqueId()) ||
							last_opened_loot.get(p.getUniqueId())+100<=TwosideKeeper.getServerTickTime()) &&
						!InventoryUtils.hasEmptyInventory(drop_inventories.get(p.getUniqueId()))) {
						last_opened_loot.put(p.getUniqueId(), TwosideKeeper.getServerTickTime());
						//Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"tellraw @a [\"\",{\"text\":\"<"+p.getName()+"> \"},{\"text\":\""+ChatColor.GREEN+"A "+item.getCustomName()+" is nearby! "+ChatColor.BOLD+"[\"},{\"text\":\"[Click Here]"+ChatColor.RESET+ChatColor.GREEN+"\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\""+GenericFunctions.GetItemName(ev.getPlayer().getEquipment().getItemInMainHand())+""+WorldShop.GetItemInfo(ev.getPlayer().getEquipment().getItemInMainHand()).replace("\"", "\\\"")+"\"}},{\"text\":\""+ev.getMessage().substring(pos)+"\"}]");
						TextComponent tc = new TextComponent(ChatColor.GREEN+"A "+item.getCustomName()+ChatColor.RESET+ChatColor.GREEN+" is nearby! ");
						TextComponent tc2 = new TextComponent(ChatColor.YELLOW+""+ChatColor.BOLD+"[Click here]");
						tc2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/dailyloot "+item.getUniqueId()));
						tc.addExtra(tc2);
						tc2 = new TextComponent(ChatColor.RESET+""+ChatColor.GREEN+" to open its contents.");
						tc.addExtra(tc2);
						p.spigot().sendMessage(tc);
						//p.openInventory(drop_inventories.get(p.getUniqueId()));
					}
				}/* else {
					if (!drop_inventories.containsKey(p.getUniqueId())) {
						TwosideKeeper.log("WARNING! Could not find UUID "+p.getUniqueId()+". UUID List: "+TextUtils.outputHashmap(drop_inventories), 1);
					}
				}*/
			}
			return true;
		} else {
			chunks.remove(c);
			return false;
		}
	}
	
	public Item getItem() {
		return item;
	}
	
	public UUID getItemUniqueID() {
		return item_uuid;
	}
	
	public void addNewDropInventory(UUID id, ItemStack...lootitems) {
		if (drop_inventories.containsKey(id)) {
			Inventory inv = drop_inventories.get(id);
			inv.addItem(lootitems);
		} else {
			Inventory newinv = Bukkit.createInventory(null, ((((lootitems.length-1)/9)+1)*9),this.lootname);
			newinv.addItem(lootitems);
			drop_inventories.put(id, newinv);
		}
	}
	
	public void openDropInventory(Player p) {
		if (drop_inventories.containsKey(p.getUniqueId()) &&
				!InventoryUtils.hasEmptyInventory(drop_inventories.get(p.getUniqueId()))) {
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
