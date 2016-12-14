package sig.plugin.TwosideKeeper.HelperStructures.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.HelperStructures.CubeType;
import sig.plugin.TwosideKeeper.HelperStructures.CustomItem;
import sig.plugin.TwosideKeeper.HelperStructures.ItemCube;

public class InventoryUtils {
	public static boolean isCarryingVacuumCube(Player p) {
		for (ItemStack items : p.getInventory().getContents()) {
			if (items!=null && CustomItem.isVacuumCube(items)) {
				return true;
			}
		}
		return false;
	}
	public static ItemStack[] insertItemsInVacuumCube(Player p,ItemStack...items) {
		ItemStack[] remaining = items;
		for (ItemStack itemStacks : p.getInventory().getContents()) {
			if (itemStacks!=null && CustomItem.isVacuumCube(itemStacks)) {
				//Insert as many items as possible in here.
				int id = Integer.parseInt(ItemUtils.GetLoreLineContainingString(itemStacks, ChatColor.DARK_PURPLE+"ID#").split("#")[1]);
				List<ItemStack> itemCubeContents = TwosideKeeper.itemCube_loadConfig(id);
				Inventory virtualinventory = Bukkit.createInventory(p, itemCubeContents.size());
				for (int i=0;i<virtualinventory.getSize();i++) {
					if (itemCubeContents.get(i)!=null) {
						virtualinventory.setItem(i, itemCubeContents.get(i));
					}
				}
				//TwosideKeeper.log("Items: "+ArrayUtils.toString(remaining), 0);
				HashMap<Integer,ItemStack> remainingitems = virtualinventory.addItem(remaining);
				List<ItemStack> itemslist = new ArrayList<ItemStack>();
				for (int i=0;i<virtualinventory.getSize();i++) {
					itemslist.add(virtualinventory.getItem(i));
				}
				ItemCube.addToViewersOfItemCube(id,remaining,null);
				TwosideKeeper.itemCube_saveConfig(id, itemslist, CubeType.VACUUM);

				/*for (ItemStack i : remainingitems.values()) {
					TwosideKeeper.log("Item "+i+" remains", 0);
				}*/
				remaining = remainingitems.values().toArray(new ItemStack[0]);
				//TwosideKeeper.log("Remaining items: "+ArrayUtils.toString(remaining), 0);
			}
		}
		return remaining;
	}
	public static boolean isCarryingFilterCube(Player p) {
		for (ItemStack items : p.getInventory().getContents()) {
			if (items!=null && CustomItem.isFilterCube(items)) {
				return true;
			}
		}
		return false;
	}
	public static ItemStack[] insertItemsInFilterCube(Player p,ItemStack...items) {
		ItemStack[] remaining = items;
		for (ItemStack itemStacks : p.getInventory().getContents()) {
			if (itemStacks!=null && CustomItem.isFilterCube(itemStacks)) {
				//Insert as many items as possible in here.
				int id = Integer.parseInt(ItemUtils.GetLoreLineContainingString(itemStacks, ChatColor.DARK_PURPLE+"ID#").split("#")[1]);
				List<ItemStack> itemCubeContents = TwosideKeeper.itemCube_loadConfig(id);
				Inventory virtualinventory = Bukkit.createInventory(p, 27);
				for (int i=0;i<virtualinventory.getSize();i++) {
					if (itemCubeContents.get(i)!=null) {
						virtualinventory.setItem(i, itemCubeContents.get(i));
					}
				}
				//THIS IS WHERE YOU DO THE FILTERING.
				HashMap<Integer,ItemStack> remainingitems = ItemCubeUtils.AttemptingToAddItemToFilterCube(id,virtualinventory,remaining);
				
				remaining = remainingitems.values().toArray(new ItemStack[0]);
			}
		}
		return remaining;
	}
	public static boolean InventoryContainSameMaterial(Inventory inv, ItemStack item) {
		for (ItemStack i : inv.getContents()) {
			if (i!=null && item!=null && i.getType()==item.getType()) {
				return true;
			}
		}
		return false;
	}
	public static boolean hasFullInventory(Player p) {
		ItemStack[] inv = p.getInventory().getStorageContents();
		for (ItemStack i : inv) {
			if (i==null || i.getType()==Material.AIR) {
				return false;
			}
		}
		return true;
	}
}
