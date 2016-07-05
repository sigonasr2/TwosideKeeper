package sig.plugin.TwosideKeeper;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Check {
	double amt;
	String player;
	public Check(ItemStack item) {
		//Parse the check.
		if (Check.isSignedBankCheck(item)) { 
			amt = Double.parseDouble(item.getItemMeta().getLore().get(0).split("\\$")[1]);
			player = item.getItemMeta().getLore().get(1).split(" "+ChatColor.LIGHT_PURPLE)[1];
		} else {
			this.amt=0;
			this.player=null;
		}
	}
	public static boolean isUnsignedBankCheck(ItemStack item) {
		if (item!=null &&
				item.getType()==Material.PAPER &&
				item.getEnchantmentLevel(Enchantment.LUCK)==1 &&
				item.getItemMeta().hasLore() &&
				item.getItemMeta().getLore().size()==3) {
			return true;
		} else {
			return false;
		}
	}
	public static boolean isSignedBankCheck(ItemStack item) {
		if (item!=null &&
				item.getType()==Material.PAPER &&
				item.getEnchantmentLevel(Enchantment.LUCK)==1 &&
				item.getItemMeta().hasLore() &&
				item.getItemMeta().getLore().size()==5 &&
				item.getItemMeta().getLore().get(3).equalsIgnoreCase(ChatColor.ITALIC+"Cash into any local bank") &&
				item.getItemMeta().getLore().get(4).equalsIgnoreCase(ChatColor.ITALIC+"for money!")) {
			return true;
		} else {
			return false;
		}
	}
	public static ItemStack createSignedBankCheckItem(double amt, String signedby) {
		DecimalFormat df = new DecimalFormat("0.00");
		ItemStack check = new ItemStack(Material.PAPER);
		check.addUnsafeEnchantment(Enchantment.LUCK, 1);
		ItemMeta m = check.getItemMeta();
		m.setDisplayName("Signed Money Cheque");
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.ITALIC+""+ChatColor.WHITE+"Check for "+ChatColor.YELLOW+"$"+df.format(amt));
		lore.add(ChatColor.BLUE+"Signed by "+ChatColor.LIGHT_PURPLE+signedby);
		lore.add("");
		lore.add(ChatColor.ITALIC+"Cash into any local bank");
		lore.add(ChatColor.ITALIC+"for money!");
		m.setLore(lore);
		check.setItemMeta(m);
		return check;
	}
}
