package sig.plugin.TwosideKeeper.HelperStructures.Items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Scepter {
	ItemStack item;
	
	public Scepter(ItemStack item) {
		
	}
	
	public static boolean isScepter(ItemStack item) {
		return item.getType()==Material.BONE && (item.hasItemMeta() &&
				item.getItemMeta().hasLore() && 
				item.getItemMeta().getLore().contains(ChatColor.LIGHT_PURPLE+"Summoner Gear"));
	}
}
