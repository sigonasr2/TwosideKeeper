package sig.plugin.TwosideKeeper.HelperStructures.Common;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.MetadataValue;

import sig.plugin.TwosideKeeper.TwosideKeeper;

public class ArrowQuiver {
	public final static String ARROW_QUIVER_IDENTIFIER = ChatColor.AQUA+"Arrow Quiver";
	public final static String ID_PREFIX = ChatColor.DARK_AQUA+""+ChatColor.BOLD+"ID ";
	public final static String FIRINGMODE_IDENTIFIER = ChatColor.BLACK+""+ChatColor.MAGIC+"MODE ";

	public static boolean isValidQuiver(ItemStack item) {
		return (item!=null && item.getType()==Material.TIPPED_ARROW &&
				item.hasItemMeta() && item.getItemMeta().hasLore() &&
				item.getItemMeta().getLore().contains(ARROW_QUIVER_IDENTIFIER));
	}
	
	public static int getID(ItemStack quiver) {
		//Try to find the ID line.
		List<String> lore = quiver.getItemMeta().getLore();
		for (int i=0;i<lore.size();i++) {
			if (lore.get(i).contains(ID_PREFIX)) {
				return Integer.parseInt(lore.get(i).replace(ID_PREFIX, ""));
			}
		}
		TwosideKeeper.log("Could not find ID for "+quiver.toString()+". Something went horribly wrong here!!!", 0);
		return -1;
	}
	
	public static void setID(ItemStack quiver) {
		List<String> lore = quiver.getItemMeta().getLore();
		for (int i=0;i<lore.size();i++) {
			if (lore.get(i).contains(ID_PREFIX)) {
				//This line needs to be replaced.
				lore.set(i, ID_PREFIX+TwosideKeeper.ARROWQUIVERID);
				TwosideKeeper.ARROWQUIVERID++;
				ItemMeta meta = quiver.getItemMeta();
				meta.setLore(lore);
				quiver.setItemMeta(meta);
				return;
			}
		}
		TwosideKeeper.log("Could not find ID for "+quiver.toString()+". Something went horribly wrong here!!!", 0);
		return;
	}
	
	public static List<ItemStack> getContents(int id) {
		File arrowquiver_dir = new File(TwosideKeeper.filesave+"/arrowquivers/");
		if (!arrowquiver_dir.exists()) {
			arrowquiver_dir.mkdir();
		}
		File config;
		config = new File(TwosideKeeper.filesave,"/arrowquivers/"+id+".data");
		if (!config.exists()) {
			try {
				config.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		List<ItemStack> inv = new ArrayList<ItemStack>();
		
		FileConfiguration workable = YamlConfiguration.loadConfiguration(config);
		for (String key : workable.getKeys(false)) {
			inv.add(workable.getItemStack(key));
		}
		return inv;
	}
	
	public static void addContents(ItemStack quiver, ItemStack...item) {
		addContents(getID(quiver),item);
	}
	
	public static void addContents(int id, ItemStack...item) {
		List<ItemStack> currentinv = getContents(id);
		for (ItemStack it : item) {
			if (it!=null) {
				addItemToQuiver(currentinv, it);
			}
		}
		saveInventory(id, currentinv);
	}

	public static void removeContents(ItemStack quiver, ItemStack...item) {
		removeContents(getID(quiver),item);
	}
	
	public static void removeContents(int id, ItemStack...item) {
		List<ItemStack> currentinv = getContents(id);
		for (ItemStack it : item) {
			if (it!=null) {
				removeItemFromQuiver(currentinv, it);
			}
		}
		saveInventory(id, currentinv);
	}

	private static void saveInventory(int id, List<ItemStack> currentinv) {
		File arrowquiver_dir = new File(TwosideKeeper.filesave+"/arrowquivers/");
		if (!arrowquiver_dir.exists()) {
			arrowquiver_dir.mkdir();
		}
		File config;
		config = new File(TwosideKeeper.filesave,"/arrowquivers/"+id+".data");
		if (config.exists()) {
			config.delete();
			try {
				config.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		FileConfiguration workable = YamlConfiguration.loadConfiguration(config);
		for (ItemStack item : currentinv) {
			workable.set(item.getType().name()+"_"+item.hashCode(), item);
		}
		
		try {
			workable.save(config);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//updateQuiverLore(id, currentinv);
	}
	
	private static List<String> getBaseQuiverLore(int id, int mode) {
		List<String> baselore = new ArrayList<String>();
		baselore.add(ARROW_QUIVER_IDENTIFIER);
		baselore.add(FIRINGMODE_IDENTIFIER+mode);
		baselore.add(ID_PREFIX+id);
		return baselore;
	}
	
	public static void updateQuiverLore(ItemStack quiver) {
		ItemMeta m = quiver.getItemMeta();
		List<ItemStack> contents = getContents(getID(quiver));
		List<String> lore = getBaseQuiverLore(getID(quiver), getArrowQuiverMode(quiver));
		if (contents.size()>0) {
			lore.add("");
			lore.add(ChatColor.WHITE+"Contains:");
			for (ItemStack item : contents) {
				lore.add(ChatColor.GRAY+""+ChatColor.ITALIC+" - "+GenericFunctions.UserFriendlyMaterialName(item)+" x"+item.getAmount());
			}
		} else {
			lore.add(ChatColor.WHITE+"This quiver is empty!");
			lore.add(ChatColor.AQUA+"Click arrows into the quiver or");
			lore.add(ChatColor.AQUA+"pick up arrows off the ground");
			lore.add(ChatColor.AQUA+"to load them up!");
		}
		m.setLore(lore);
		m.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		quiver.setItemMeta(m);
		quiver.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 5);
	}

	private static void removeItemFromQuiver(List<ItemStack> currentitems, ItemStack it) {
		//First compare all items that exist.
		int slot = getItemSlot(currentitems, it);
		if (slot!=-1) { //Remove from the previous amount in the list.
			if (currentitems.get(slot).getAmount()-it.getAmount()<1) { //Delete it if we run out of these arrows.
				currentitems.remove(slot);
			} else {
				currentitems.get(slot).setAmount(currentitems.get(slot).getAmount()-it.getAmount());
			}
		}
	}

	private static void addItemToQuiver(List<ItemStack> currentitems, ItemStack it) {
		//First compare all items that exist.
		int slot = getItemSlot(currentitems, it);
		if (slot==-1) { //No similar item found, add a new entry.
			TwosideKeeper.log("No slot found. Adding this way.", 5);
			currentitems.add(it);
		} else { //Add to the previous amount in the list.
			currentitems.get(slot).setAmount(currentitems.get(slot).getAmount()+it.getAmount());
		}
	}

	private static int getItemSlot(List<ItemStack> currentitems, ItemStack it) {
		for (int i=0;i<currentitems.size();i++) {
			if (currentitems.get(i).isSimilar(it)) {
				return i;
			}
		}
		return -1;
	}

	public static List<ItemStack> getContentsAPI(ItemStack i) {
		return getContents(getID(i));
	}
	
	public static int getArrowQuiverMode(ItemStack item) {
		//Arrow quiver mode will be determined by an integer dictating which item slot we are on.
		//If the slot is non-existent or out-of-bounds, we set it to 0 to indicate the first slot.
		List<String> lore = item.getItemMeta().getLore();
		List<ItemStack> contents = getContents(getID(item));
		for (String text : lore) {
			if (text.contains(FIRINGMODE_IDENTIFIER)) {
				int mode = Integer.parseInt(text.replace(FIRINGMODE_IDENTIFIER, ""));
				if (mode>=0 && contents.size()-1>=mode) {
					return setArrowQuiverMode(item,mode);
				} else {
					return setArrowQuiverMode(item,0);
				}
			}
		}
		return setArrowQuiverMode(item,0);
	}

	public static int setArrowQuiverMode(ItemStack item, int mode) {
		ItemMeta m = item.getItemMeta();
		List<String> lore = m.getLore();
		List<ItemStack> contents = getContents(getID(item));
		int arrow_quiver_identifier_line = 0;
		for (int i=0;i<lore.size();i++) {
			if (lore.get(i).contains(FIRINGMODE_IDENTIFIER)) {
				if (contents.size()-1>=mode) {
					lore.set(i,FIRINGMODE_IDENTIFIER+mode);
				} else {
					lore.set(i,FIRINGMODE_IDENTIFIER+0);
				}
				m.setLore(lore);
				item.setItemMeta(m);
				return mode;
			}
			if (lore.get(i).contains(ARROW_QUIVER_IDENTIFIER)) {
				arrow_quiver_identifier_line = i;
			}
		}
		//Firing mode does not exist in the lore yet. Add it under the arrow quiver line.
		TwosideKeeper.log("Adding a line @ "+arrow_quiver_identifier_line, 5);
		lore.add(arrow_quiver_identifier_line+1,FIRINGMODE_IDENTIFIER+mode);
		m.setLore(lore);
		item.setItemMeta(m);
		return mode;
	}
	
	public static boolean isQuiverEmpty(ItemStack quiver) {
		return getContents(getID(quiver)).size()==0;
	}
	
	public static ItemStack ReturnAndRemoveShotArrow(ItemStack quiver) {
		return ReturnAndRemoveShotArrow(quiver,null);
	}
	
	/**
	 * Returns the first arrow quiver in the player's inventory following the same
	 * arrow check rules as Minecraft does. Off-hand slot, then hotbar, then inventory.
	 * 
	 * Returns null if it cannot find an arrow quiver at all.
	 */
	public static ItemStack getArrowQuiverInPlayerInventory(Player p) {
		ItemStack offhandslot = p.getInventory().getExtraContents()[0];
		ItemStack[] storagecontents = p.getInventory().getStorageContents();
		if (offhandslot!=null && ArrowQuiver.isValidQuiver(offhandslot)) {
			return offhandslot;
		} else {
			for (int i=0;i<storagecontents.length;i++) {
				if (storagecontents[i]!=null && ArrowQuiver.isValidQuiver(storagecontents[i])) {
					return storagecontents[i];
				}
			}
		}
		return null;
	}

	/**
	 * Same as normal version of this method, but checks a bow item to determine if we really are supposed to remove
	 * an arrow by using Infinity as a formula.
	 */
	public static ItemStack ReturnAndRemoveShotArrow(ItemStack quiver, ItemStack bow) {
		List<ItemStack> contents = getContents(getID(quiver));
		if (contents.size()>0) {
			ItemStack arrow = contents.get(getArrowQuiverMode(quiver)).clone();
			arrow.setAmount(1);
			if (bow==null) {
				ArrowQuiver.removeContents(getID(quiver), arrow);
			}
			return arrow;
		} else {
			return null;
		}
	}
}
