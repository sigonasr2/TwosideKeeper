package sig.plugin.TwosideKeeper.HelperStructures.Common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Dropper;
import org.bukkit.block.Hopper;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.ItemCubeUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.ItemUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.SoundUtils;

public class MagicWand {
	public static final String MAGICWANDID_LINE = ChatColor.DARK_GREEN+"MAGICWAND#";
	public static boolean isMagicWand(ItemStack item) {
		return (ItemUtils.isValidItem(item) &&
			    ItemUtils.LoreContainsSubstring(item, MAGICWANDID_LINE));
	}
	public static Location getMagicWandLoc(int id) {
		int posx = id % 960;
		int posy = 70; //This should be right above bauble pouches (67, 70 for testing), and shouldn't interact with them.
		int posz = id / 960;
		return new Location(Bukkit.getWorld("FilterCube"),posx,posy,posz);
	}
	public static Block getMagicWandBlock(int id) {
		Block b = Bukkit.getWorld("FilterCube").getBlockAt(getMagicWandLoc(id));
		return b;
	}
	public static Dropper getMagicWandDropper(int id) {
		Dropper h = (Dropper)Bukkit.getWorld("FilterCube").getBlockAt(getMagicWandLoc(id)).getState();
		return h;
	}
	public static void createNewMagicWand(int id) {
		Block b = getMagicWandBlock(id);
		b.getWorld().getBlockAt(getMagicWandLoc(id)).setType(Material.DROPPER);
	}
	public static int getMagicWandID(ItemStack item) {
		if (isMagicWand(item)) {
			String id = ItemUtils.GetLoreLineContainingSubstring(item, MAGICWANDID_LINE).split("#")[1];
			return Integer.parseInt(id);
		}
		else {
			return -1;
		}
	}
	public static HashMap<Integer, ItemStack> insertItemsIntoMagicWand(int id, ItemStack...items) {
		Dropper d = getMagicWandDropper(id);
		Inventory inv = d.getInventory();
		return inv.addItem(items);
	}
	public static void openMagicWand(Player p, ItemStack item) {
		if (isMagicWand(item)) {
			int id = getMagicWandID(item);
			Dropper d = getMagicWandDropper(id);
			d.getChunk().load();
			GenericFunctions.renameDropper(d, ChatColor.stripColor(((ItemUtils.hasDisplayName(item))?ItemUtils.getDisplayName(item):"Magic Wand #"+id)));
			p.openInventory(d.getInventory());
			SoundUtils.playLocalSound(p, Sound.ITEM_ARMOR_EQUIP_LEATHER, 1.0f, 1.0f);
		}
		else {
			p.sendMessage(ChatColor.RED+"Could not open Magic Wand Inventory! Please let the admin know this did not work.");
		}
	}
	public static List<ItemStack> getMagicWandContents(int id) {
		List<ItemStack> itemlist = new ArrayList<ItemStack>();
		Dropper d = getMagicWandDropper(id);
		Inventory inv = d.getInventory();
		for (ItemStack item : inv.getContents()) {
			if (ItemUtils.isValidItem(item)) {
				itemlist.add(item);
			}
		}
		return itemlist;
	}
}
//Will not use bauble pouch implementation due to enchanting tables not being containers.
//Go dive into the item cube spaghetti code.