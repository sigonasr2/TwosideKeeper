package sig.plugin.TwosideKeeper.HelperStructures.Utils;

import java.util.HashMap;
import java.util.List;

import org.bukkit.inventory.ItemStack;

public class ArrayUtils {
	public static String toString(Object[] items) {
		StringBuilder string = new StringBuilder();
		boolean first=false;
		for (Object i : items) {
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
