package sig.plugin.TwosideKeeper.HelperStructures;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import sig.plugin.TwosideKeeper.PlayerStructure;
import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.InventoryUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.ItemCubeUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.SoundUtils;

public class ItemCube {
	public static boolean isSomeoneViewingItemCube(int id, Player checker) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (!p.equals(checker)) {
				if (p.getOpenInventory()!=null && p.getOpenInventory().getTitle().contains("Item Cube #")) {
					//This is an item cube. Check if it's the same number.
					int ider = Integer.parseInt(p.getOpenInventory().getTitle().split("#")[1]);
					if (ider==id) {
						return true;
					}
				}
			}
		}
		return false; //Didn't find anyone, oh well..
	}
	public static Inventory getViewingItemCubeInventory(int id, Player checker) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (checker==null || !p.equals(checker)) {
				if (p.getOpenInventory()!=null && p.getOpenInventory().getTitle().contains("Item Cube #"+id)) {
					//TwosideKeeper.log("Updating inventory for player "+p.getName()+"; Inventory "+p.getOpenInventory().getTitle(), 0);
					//This is an item cube. Check if it's the same number.
					return p.getOpenInventory().getTopInventory();
				}
			}
		}
		return null; //Didn't find anything.
	}
	public static void displayErrorMessage(Player p) {
		SoundUtils.playLocalSound(p, Sound.BLOCK_NOTE_PLING, 0.6f, 4.0f);
		p.sendMessage("Someone is currently using this Item Cube! Please wait for them to finish.");
	}
	
	public static void addToViewersOfItemCube(int idnumb, ItemStack cursor, Player check) {
		Inventory inv = getViewingItemCubeInventory(idnumb, check);
		if (inv!=null && cursor!=null) {
			//TwosideKeeper.log("Adding items "+cursor+" to Inventory "+inv.getTitle(), 0);
			inv.addItem(cursor);
		}
	}
	
	public static void addToViewersOfItemCube(int idnumb, ItemStack[] cursor, Player check) {
		Inventory inv = getViewingItemCubeInventory(idnumb, check);
		cursor = InventoryUtils.RemoveAllNullItems(cursor);
		if (inv!=null) {
			//TwosideKeeper.log("Adding items "+Arrays.toString(cursor)+" to Inventory "+inv.getTitle(), 0);
			inv.addItem(cursor);
		}
	}
	public static void removeFromViewersofItemCube(int idnumb, ItemStack cursor, Player check) {
		Inventory inv = getViewingItemCubeInventory(idnumb, check);
		if (inv!=null && cursor!=null) {
			inv.removeItem(cursor);
		}
	}
	public static void removeFromViewersofItemCube(int idnumb, ItemStack[] cursor, Player check) {
		Inventory inv = getViewingItemCubeInventory(idnumb, check);
		cursor = InventoryUtils.RemoveAllNullItems(cursor);
		if (inv!=null) {
			inv.removeItem(cursor);
		}
	}
	public static void clearFromViewersofItemCube(int id, Player check) {
		Inventory inv = getViewingItemCubeInventory(id, check);
		if (inv!=null) {
			inv.clear();
		}
	}
}
