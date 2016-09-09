package sig.plugin.TwosideKeeper.HelperStructures.Common;

import java.util.HashMap;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryManagement {
	public static void transferInventoryToInventory(Inventory from, Inventory to) {
		HashMap<Integer,ItemStack> remaining = to.addItem(from.getContents());
		from.clear();
		for (int i=0;i<remaining.size();i++) {
			from.addItem(remaining.get(i));
		}
	}
}
