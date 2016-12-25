package sig.plugin.TwosideKeeper.HelperStructures.Utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
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
	
	public static String GetLoreLineContainingSubstring(ItemStack item, String string) {
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
	
	public static ItemStack ModifyLoreLineContainingSubstring(ItemStack item, String string, String newstring) {
		if (isValidLoreItem(item)) {
			ItemMeta meta = item.getItemMeta();
			List<String> lore = meta.getLore();
			for (int i=0;i<lore.size();i++) {
				if (lore.get(i).contains(string)) {
					lore.set(i, newstring);
					break;
				}
			}
			meta.setLore(lore);
			item.setItemMeta(meta);
		}
		return item;
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
	
	public static boolean isArtifactDust(ItemStack item) {
		if (isValidLoreItem(item) &&
				LoreContainsSubstring(item,ChatColor.BLUE+""+ChatColor.MAGIC)) {
			//TwosideKeeper.log("This is Artifact Dust.", 0);
			return true;
		} else {
			return false;
		}
	}

	public static ItemStack createRandomFirework() {
		ItemStack firework = new ItemStack(Material.FIREWORK);
		FireworkMeta fm = (FireworkMeta)firework.getItemMeta();
		fm.setPower((int)(Math.random()*2+1));
		for (int i=0;i<((int)(Math.random()*6))+1;i++) {
			fm.addEffect(generateRandomFireworkEffect());
		}
		//fm.addEffect(generateRandomFireworkEffect());
		firework.setItemMeta(fm);
		return firework;
	}

	private static FireworkEffect generateRandomFireworkEffect() {
		FireworkEffect.Builder builder = FireworkEffect.builder();
		if (Math.random()<=0.33) {
			builder.flicker(true);
		}
		if (Math.random()<=0.33) {
			builder.trail(true);
		}
		builder.with(Type.values()[((int)(Math.random()*Type.values().length))]);
		Color newcol = RandomizeBrightColor();
		builder.withColor(newcol);
		if (Math.random()<=0.33) {
			builder.withFade(Color.fromRGB((int)(Math.random()*192+64), (int)(Math.random()*192+64), (int)(Math.random()*192+64)));
		}
		return builder.build();
	}

	public static Color RandomizeBrightColor() {
		double numb = Math.random();
		int r = 0;
		int g = 0;
		int b = 0;
		if (numb<=0.33) {
			r = (int)(Math.random()*64+192);
			g = (int)(Math.random()*256);
			b = (int)(Math.random()*256);
		} else
		if (numb<=0.66) {
			g = (int)(Math.random()*64+192);
			r = (int)(Math.random()*256);
			b = (int)(Math.random()*256);
		} else {
			b = (int)(Math.random()*64+192);
			r = (int)(Math.random()*256);
			g = (int)(Math.random()*256);
		}
		Color newcol = Color.fromRGB(r, g, b);
		return newcol;
	}
}
