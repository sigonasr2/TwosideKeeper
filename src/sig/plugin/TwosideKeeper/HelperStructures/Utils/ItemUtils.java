package sig.plugin.TwosideKeeper.HelperStructures.Utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import sig.plugin.TwosideKeeper.TwosideKeeper;

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

	public static void setDisplayName(ItemStack item, String name) {
		ItemMeta m = item.getItemMeta();
		m.setDisplayName(name);
		item.setItemMeta(m);
	}
	
	public static boolean isValidLoreItem(ItemStack item) {
		return (item!=null && item.hasItemMeta() && item.getItemMeta().hasLore());
	}

	public static boolean LoreContains(ItemStack item, String string) {
		if (isValidLoreItem(item)) {
			List<String> lore = item.getItemMeta().getLore();
			if (lore.contains(string)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean LoreContainsSubstring(ItemStack item, String string) {
		if (isValidLoreItem(item)) {
			List<String> lore = item.getItemMeta().getLore();
			for (String l : lore) {
				if (l.contains(string)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static String GetLoreLineContainingString(ItemStack item, String string) {
		if (isValidLoreItem(item)) {
			List<String> lore = item.getItemMeta().getLore();
			for (String l : lore) {
				if (l.contains(string)) {
					return l;
				}
			}
		}
		return "";
	}
	
	public static String GetLoreLine(ItemStack item, int line_numb) {
		if (isValidLoreItem(item)) {
			List<String> lore = item.getItemMeta().getLore();
			if (lore.size()>line_numb) {
				return lore.get(line_numb);
			} else {
				return "";
			}
		}
		return "";
	}

	public static void clearLore(ItemStack item) {
		if (isValidItem(item)) {
			ItemMeta m = item.getItemMeta();
			m.setLore(null);
			item.setItemMeta(m);
		}
	}

	private static boolean isValidItem(ItemStack item) {
		return (item!=null && item.hasItemMeta());
	}

}
