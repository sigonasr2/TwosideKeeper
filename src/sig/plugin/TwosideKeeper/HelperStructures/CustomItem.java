package sig.plugin.TwosideKeeper.HelperStructures;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.md_5.bungee.api.ChatColor;

public class CustomItem {
	ItemStack item;
	
	public CustomItem(ItemStack item) {
		this.item=item;
	}
	
	public ItemStack getItemStack() {
		return getItemStack(1);
	}
	
	public ItemStack getItemStack(int amt) {
		ItemStack temp = item.clone();
		temp.setAmount(amt);
		return temp;
	}
	
}
