package sig.plugin.TwosideKeeper.HelperStructures.Utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemUtils {

	public static void addLore(ItemStack item, String string) {
		ItemMeta m = item.getItemMeta();
		List<String> lore = new ArrayList<String>();
		if (m.hasLore()) {
			lore.addAll(m.getLore());
		}
		lore.add(string);
		m.setLore(lore);
		item.setItemMeta(m);
	}

	public static void hideEnchantments(ItemStack item) {
		ItemMeta m = item.getItemMeta();
		m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(m);
	}
	
	public static void showEnchantments(ItemStack item) {
		ItemMeta m = item.getItemMeta();
		m.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(m);
	}

}
