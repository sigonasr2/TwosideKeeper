package sig.plugin.TwosideKeeper.HelperStructures.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Hopper;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.HelperStructures.ItemCube;

public class ItemCubeUtils {
	public static int getItemCubeID(ItemStack item) {
		return Integer.parseInt(ItemUtils.GetLoreLineContainingString(item, ChatColor.DARK_PURPLE+"ID#").split("#")[1]);
	}
	public static Location getFilterCubeLoc(int id) {
		int posx = id % 960;
		int posy = 64;
		int posz = id / 960;
		return new Location(Bukkit.getWorld("FilterCube"),posx,posy,posz);
	}
	public static Block getFilterCubeBlock(int id) {
		Block b = Bukkit.getWorld("FilterCube").getBlockAt(getFilterCubeLoc(id));
		return b;
	}
	public static Hopper getFilterCubeHopper(int id) {
		Hopper h = (Hopper)Bukkit.getWorld("FilterCube").getBlockAt(getFilterCubeLoc(id)).getState();
		return h;
	}
	public static void createNewFilterCube(int id) {
		Block b = getFilterCubeBlock(id);
		b.getWorld().getBlockAt(getFilterCubeLoc(id)).setType(Material.HOPPER);
	}
	public static HashMap<Integer, ItemStack> AttemptingToAddItemToFilterCube(int id, Inventory cube_inv, ItemStack[] remaining) {
		Hopper h = getFilterCubeHopper(id);
		Inventory inv = h.getInventory();
		HashMap<Integer,ItemStack> reject_items = new HashMap<Integer,ItemStack>();
		for (ItemStack it : remaining) {
			if (it!=null) {
				if (InventoryUtils.InventoryContainSameMaterial(inv, it)) {
					HashMap<Integer,ItemStack> extras = cube_inv.addItem(it);
					if (extras.size()==0) {
						List<ItemStack> itemslist = new ArrayList<ItemStack>();
						for (int i=0;i<cube_inv.getSize();i++) {
							itemslist.add(cube_inv.getItem(i));
						}
						ItemCube.addToViewersOfItemCube(id,remaining,null);
						TwosideKeeper.itemCube_saveConfig(id, itemslist);
					} else {
						for (ItemStack i : extras.values()) {
							reject_items.put(reject_items.size(), i);
							List<ItemStack> itemslist = new ArrayList<ItemStack>();
							for (int j=0;j<cube_inv.getSize();j++) {
								itemslist.add(cube_inv.getItem(j));
							}
							TwosideKeeper.itemCube_saveConfig(id, itemslist);
						}
					}
				} else {
					reject_items.put(reject_items.size(), it);
				}
			}
		}
		return reject_items;
	}
	public static boolean SomeoneHasAFilterCubeOpen() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (p.getOpenInventory()!=null && p.getOpenInventory().getTopInventory()!=null && p.getOpenInventory().getTopInventory().getType()==InventoryType.HOPPER) {
				TwosideKeeper.log("Keep this open! "+p.getName()+" is using it!", 5);
				return true;
			}
		}
		return false;
	}
	public static boolean isItemCubeMaterial(Material mat) {
		if (mat==Material.CHEST ||
				mat==Material.ENDER_CHEST ||
				mat==Material.HOPPER_MINECART ||
				mat==Material.STORAGE_MINECART) {
			return true;
		} else {
			return false;
		}
	}
}
