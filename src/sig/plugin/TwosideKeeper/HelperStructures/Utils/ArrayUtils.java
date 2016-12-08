package sig.plugin.TwosideKeeper.HelperStructures.Utils;

import java.util.HashMap;

import org.bukkit.inventory.ItemStack;

public class ArrayUtils {
	public static String toString(ItemStack[] items) {
		StringBuilder string = new StringBuilder();
		boolean first=false;
		for (ItemStack i : items) {
			if (i!=null) {
				if (!first) {
					string.append(i.toString());
					first=true;
				} else {
					string.append(","+i.toString());
				}
			}
		}
		return string.toString();
	}
}
