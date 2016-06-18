package sig.plugin.TwosideKeeper.HelperStructures;

import java.text.DecimalFormat;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import sig.plugin.TwosideKeeper.TwosideKeeper;

public class ShopPurchase {
	String player;
	String customer;
	String itemname;
	double money;
	int amt;
	
	public ShopPurchase(String p, Player customer, ItemStack item, double money, int amt) {
		player = p;
		this.customer=customer.getName();
		itemname = TwosideKeeper.GetItemName(item);
		this.money = money;
		this.amt=amt;
	}
	
	public String getPlayer() {
		return player;
	}
	public String getCustomer() {
		return customer;
	}
	
	public String announcementString() {
		DecimalFormat df = new DecimalFormat("0.00");
		return "Player "+ChatColor.BLUE+customer+ChatColor.WHITE+" has purchased "+ChatColor.YELLOW+amt+ChatColor.WHITE+" of your "+ChatColor.YELLOW+itemname+". You have earned $"+df.format(money)+". "+ChatColor.GRAY+""+ChatColor.ITALIC+"(See /money)";
	}
}