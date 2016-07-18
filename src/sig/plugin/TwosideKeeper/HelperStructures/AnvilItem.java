package sig.plugin.TwosideKeeper.HelperStructures;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import sig.plugin.TwosideKeeper.TwosideKeeper;

public class AnvilItem {
	ItemStack olditem;
	ItemStack newitem;
	
	public AnvilItem(ItemStack olditem, ItemStack newitem) {
		this.olditem=olditem;
		this.newitem=newitem;
	}
	
	public static boolean validitem(ItemStack item) {
		return (item!=null && item.getType()!=Material.AIR &&
				item.hasItemMeta() && item.getItemMeta().hasDisplayName());
	}
	
	public String getColorCodes() {
		ItemMeta m = olditem.getItemMeta();
		return m.getDisplayName().replace(ChatColor.stripColor(m.getDisplayName()), "");
	}
	
	public ItemStack renameItemProperly() {
		if (validitem(olditem) && validitem(newitem)) {
			//Input Item : ᶲ2+Test   New Item: 2Jelly
			String var = getColorCodes(); //var = ChatColor.RED (ᶲ2)
			TwosideKeeper.log("var = "+var, 2);
			String newcol = newitem.getItemMeta().getDisplayName(); //2Jelly
			String colors = "";
			if (var.length()>=2) {
				colors = var.replace(ChatColor.COLOR_CHAR+"", ""); //2
			}
			
			if (newcol.indexOf(colors)==0) {
				//See if the new name starts with the color codes.
				ItemMeta m = newitem.getItemMeta();
				m.setDisplayName(newcol.replace(colors, var)); //ᶲ2+jelly
				newitem.setItemMeta(m);
				return newitem;
			}

		}
		
		return newitem;
	}
}
