package sig.plugin.TwosideKeeper.HelperStructures;

import java.text.DecimalFormat;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;

public class ShopPurchase {
	String player;
	String customer;
	String itemname;
	double money;
	int amt;
	boolean sell;
	
	public ShopPurchase(String p, Player customer, ItemStack item, double money, int amt) {
		ShopPurchase(p,customer,item,money,amt,true); //Assume this is a selling purchase by default.
	}
	public ShopPurchase(String p, Player customer, ItemStack item, double money, int amt, boolean sell) {
		ShopPurchase(p,customer,item,money,amt,sell); //Assume this is a selling purchase by default.
	}
	
	public void ShopPurchase(String p, Player customer, ItemStack item, double money, int amt, boolean sell) {
		player = p;
		this.customer=customer.getName();
		itemname = GenericFunctions.GetItemName(item);
		this.money = money;
		this.amt=amt;
		this.sell=sell;
	}
	
	public String getPlayer() {
		return player;
	}
	public String getCustomer() {
		return customer;
	}
	
	public String announcementString() {
		DecimalFormat df = new DecimalFormat("0.00");
		if (sell) {
			return "Player "+ChatColor.BLUE+customer+ChatColor.WHITE+" has purchased "+ChatColor.YELLOW+amt+ChatColor.WHITE+" of your "+ChatColor.YELLOW+itemname+". You have earned $"+df.format(money)+". "+ChatColor.GRAY+""+ChatColor.ITALIC+"(See /money)";
		} else {
			return "Player "+ChatColor.BLUE+customer+ChatColor.WHITE+" has sold "+ChatColor.YELLOW+amt+ChatColor.WHITE+" "+ChatColor.YELLOW+itemname+" to you. $"+df.format(money)+" has been deducted from your bank account. "+ChatColor.GRAY+""+ChatColor.ITALIC+"(Check your shop to collect your items.)";
		}
	}
}