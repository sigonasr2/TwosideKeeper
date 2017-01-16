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

public class BaublePouch {
	public final static String POUCHID_LINE = ChatColor.DARK_GREEN+"POUCH#";
	public static boolean isBaublePouch(ItemStack item) {
		return (ItemUtils.isValidItem(item) &&
				ItemUtils.LoreContainsSubstring(item, POUCHID_LINE));
	}
	public static Location getBaublePouchLoc(int id) {
		int posx = id % 960;
		int posy = 66; //Hoppers are at 64. So these cannot be directly above them as the items will get sucked in.
		int posz = id / 960;
		return new Location(Bukkit.getWorld("FilterCube"),posx,posy,posz);
	}
	public static Block getBaublePouchBlock(int id) {
		Block b = Bukkit.getWorld("FilterCube").getBlockAt(getBaublePouchLoc(id));
		return b;
	}
	public static Dropper getBaublePouchDropper(int id) {
		Dropper h = (Dropper)Bukkit.getWorld("FilterCube").getBlockAt(getBaublePouchLoc(id)).getState();
		return h;
	}
	public static void createNewBaublePouch(int id) {
		Block b = getBaublePouchBlock(id);
		b.getWorld().getBlockAt(getBaublePouchLoc(id)).setType(Material.DROPPER);
	}
	public static int getBaublePouchID(ItemStack item) {
		if (isBaublePouch(item)) {
			String id = ItemUtils.GetLoreLineContainingSubstring(item, POUCHID_LINE).split("#")[1];
			return Integer.parseInt(id);
		} else {
			return -1;
		}
	} 
	public static HashMap<Integer,ItemStack> insertItemsIntoBaublePouch(int id, ItemStack...items) {
		Dropper d = getBaublePouchDropper(id);
		Inventory inv = d.getInventory();
		return inv.addItem(items);
	}
	public static void openBaublePouch(Player p, ItemStack item) {
		if (isBaublePouch(item)) {
			int id = getBaublePouchID(item);
			Dropper d = getBaublePouchDropper(id);
			d.getChunk().load();
			p.openInventory(d.getInventory());
			SoundUtils.playLocalSound(p, Sound.ITEM_ARMOR_EQUIP_LEATHER, 1.0f, 1.0f);
		} else {
			p.sendMessage(ChatColor.RED+"Could not open Bauble Pouch! Please let the admin know this did not work.");
		}
	}
	public static List<ItemStack> getBaublePouchContents(int id) {
		List<ItemStack> itemlist = new ArrayList<ItemStack>();
		Dropper d = getBaublePouchDropper(id);
		Inventory inv = d.getInventory();
		for (ItemStack item : inv.getContents()) {
			if (ItemUtils.isValidItem(item)) {
				itemlist.add(item);
			}
		}
		return itemlist;
	}
}
