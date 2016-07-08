package sig.plugin.TwosideKeeper.HelperStructures;

import java.text.DecimalFormat;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.WorldShopManager;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;

public class ShopPurchase {
	String player;
	String customer;
	int shopID;
	double money;
	int amt;
	boolean sell;
	
	public ShopPurchase(String p, Player customer, int shopID, double money, int amt) {
		ShopPurchase(p,customer,shopID,money,amt,true); //Assume this is a selling purchase by default.
	}
	public ShopPurchase(String p, Player customer, int shopID, double money, int amt, boolean sell) {
		ShopPurchase(p,customer,shopID,money,amt,sell); //Assume this is a selling purchase by default.
	}
	
	public void ShopPurchase(String p, Player customer, int shopID, double money, int amt, boolean sell) {
		this.player = p;
		this.customer=customer.getName();
		this.shopID = shopID;
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
	
	public TextComponent announcementString() {
		DecimalFormat df = new DecimalFormat("0.00");
		if (sell) {
			WorldShop ss = TwosideKeeper.TwosideShops.LoadWorldShopData(shopID);
			TextComponent message1 = new TextComponent("Player "+ChatColor.BLUE+customer+ChatColor.WHITE+" has purchased "+ChatColor.YELLOW+amt+ChatColor.WHITE+" of your ");
			TextComponent message2 = new TextComponent(ChatColor.GREEN+"["+GenericFunctions.GetItemName(ss.GetItem())+ChatColor.RESET+""+ChatColor.GREEN+"]");
			message2.setHoverEvent(new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(GenericFunctions.GetItemName(ss.GetItem())+WorldShop.GetItemInfo(ss.GetItem())).create()));
			TextComponent message3 = new TextComponent(". You have earned $"+df.format(money)+". "+ChatColor.GRAY+""+ChatColor.ITALIC+"(See /money)");
			TextComponent finalmsg = message1;
			finalmsg.addExtra(message2);
			finalmsg.addExtra(message3);
			return finalmsg;
		} else {
			WorldShop ss = TwosideKeeper.TwosideShops.LoadWorldShopData(shopID);
			TextComponent message1 = new TextComponent("Player "+ChatColor.BLUE+customer+ChatColor.WHITE+" has sold "+ChatColor.YELLOW+amt+ChatColor.WHITE+" ");
			TextComponent message2 = new TextComponent(ChatColor.GREEN+"["+GenericFunctions.GetItemName(ss.GetItem())+ChatColor.RESET+""+ChatColor.GREEN+"]");
			message2.setHoverEvent(new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(GenericFunctions.GetItemName(ss.GetItem())+WorldShop.GetItemInfo(ss.GetItem())).create()));
			TextComponent message3 = new TextComponent(" to you. $"+df.format(money)+" has been deducted from your bank account. "+ChatColor.GRAY+""+ChatColor.ITALIC+"(Check your shop to collect your items.)");
			TextComponent finalmsg = message1;
			finalmsg.addExtra(message2);
			finalmsg.addExtra(message3);
			return finalmsg;
		}
	}
}