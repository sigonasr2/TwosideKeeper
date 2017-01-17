package sig.plugin.TwosideKeeper.HelperStructures.Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;

import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;

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
	
	public static String getDisplayName(ItemStack item) {
		ItemMeta m = item.getItemMeta();
		return m.getDisplayName();
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
	
	public static ItemStack addLoreLineUnderneathLineContainingSubstring(ItemStack item, String string, String newline) {
		if (isValidLoreItem(item)) {
			ItemMeta meta = item.getItemMeta();
			List<String> lore = meta.getLore();
			for (int i=0;i<lore.size();i++) {
				if (lore.get(i).contains(string)) {
					lore.add(i+1, newline);
					break;
				}
			}
			meta.setLore(lore);
			item.setItemMeta(meta);
		}
		return item;
	}
	
	public static ItemStack DeleteAllLoreLinesAtAndAfterLineContainingSubstring(ItemStack item, String string) {
		if (isValidLoreItem(item)) {
			ItemMeta meta = item.getItemMeta();
			List<String> lore = meta.getLore();
			boolean delete=false;
			for (int i=0;i<lore.size();i++) {
				if (lore.get(i).contains(string)) {
					delete=true;
				}
				if (delete) {
					lore.remove(i);
					i--;
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

	public static boolean isValidItem(ItemStack item) {
		return (item!=null && item.getType()!=Material.AIR);
	}
	
	public static boolean isArtifactDust(ItemStack item) {
		if (isValidLoreItem(item) &&
				LoreContainsSubstring(item,ChatColor.BLUE+""+ChatColor.MAGIC) &&
				item.getType()==Material.SULPHUR) {
			//TwosideKeeper.log("This is Artifact Dust.", 0);
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Returns time remaining for the Artifact Dust until auto-rebuild in ticks.
	 */
	public static long getArtifactDustTimeRemaining(ItemStack item) {
		if (isArtifactDust(item)) {
			long time = TwosideKeeper.getServerTickTime();
			List<String> oldlore = item.getItemMeta().getLore();
			for (int i=0;i<oldlore.size();i++) {
				if (oldlore.get(i).contains(ChatColor.BLUE+""+ChatColor.MAGIC)) {
					//See what the previous time was.
					time = Long.parseLong(ChatColor.stripColor(oldlore.get(i)));
					return (time + 12096000 - TwosideKeeper.getServerTickTime());
				}
			}
		}
		return 0;
	}
	

	/**
	 * Set a new amount of time in ticks required before the item will turn from dust back into an artifact.
	 * Returns a new modified version of the item. If the time remaining of the Artifact Dust is 0 or lower, it will automatically turn into a regular item again!
	 */
	public static ItemStack setArtifactDustTimeRemaining(ItemStack item, long newtime) {
		if (isArtifactDust(item)) {
			long time = TwosideKeeper.getServerTickTime();
			List<String> oldlore = item.getItemMeta().getLore();
			for (int i=0;i<oldlore.size();i++) {
				if (oldlore.get(i).contains(ChatColor.BLUE+""+ChatColor.MAGIC)) {
					//See what the previous time was.
					time = Long.parseLong(ChatColor.stripColor(oldlore.get(i)));
					oldlore.set(i, ChatColor.BLUE+""+ChatColor.MAGIC+(TwosideKeeper.getServerTickTime() - 12096000 + newtime));
				}
			}
			ItemMeta m = item.getItemMeta();
			m.setLore(oldlore);
			item.setItemMeta(m);
			if (newtime<=0) {
				item = GenericFunctions.convertArtifactDustToItem(item);
			}
		}
		return item;
	}
	
	/**
	 * This method will increase/decrease the amount of Artifact Dust Time remaining on the item.
	 * By providing a negative value for amt, you can make the artifact dust revive sooner, while a positive amount would extend the time required to repair the Artifact Dust.
	 * <br><br>
	 * If the negative value provided sets the time remaining of the Artifact Dust to 0 or lower, it will automatically turn into a regular item again!
	 * <br><br>
	 * Returns a modified version of the item.
	 */
	public static ItemStack addArtifactDustTime(ItemStack item, long amt) {
		if (isArtifactDust(item)) {
			long time = TwosideKeeper.getServerTickTime();
			List<String> oldlore = item.getItemMeta().getLore();
			for (int i=0;i<oldlore.size();i++) {
				if (oldlore.get(i).contains(ChatColor.BLUE+""+ChatColor.MAGIC)) {
					//See what the previous time was.
					time = Long.parseLong(ChatColor.stripColor(oldlore.get(i)));
					time += amt;
					oldlore.set(i, ChatColor.BLUE+""+ChatColor.MAGIC+time);
					TwosideKeeper.log("Time is "+time, 5);
					break;
				}
			}
			ItemMeta meta = item.getItemMeta();
			meta.setLore(oldlore);
			item.setItemMeta(meta);
			if (time+12096000<=TwosideKeeper.getServerTickTime()) {
				item = GenericFunctions.convertArtifactDustToItem(item);
			}
		}
		return item;
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

	public static boolean isValidItem(ItemStack[] equips) {
		// TODO Auto-generated method stub
		return false;
	}

	public static boolean hasDisplayName(ItemStack item) {
		return getDisplayName(item)!=null;
	}
}
