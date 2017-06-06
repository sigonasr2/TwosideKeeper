package sig.plugin.TwosideKeeper.HelperStructures;

import java.text.DecimalFormat;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;

public class ShopPurchase {
	UUID player;
	UUID customer;
	ItemStack item;
	double money;
	int amt;
	boolean sell;
	
	public ShopPurchase(UUID p, UUID customer, ItemStack item, double money, int amt) {
		this.player = p;
		this.customer=customer;
		this.item=item;
		this.money = money;
		this.amt=amt;
		this.sell=true;
	}
	
	public ShopPurchase(UUID p, UUID customer, ItemStack item, double money, int amt, boolean sell) {
		this.player = p;
		this.customer=customer;
		this.item=item;
		this.money = money;
		this.amt=amt;
		this.sell=sell;
	}
	
	public UUID getSeller() {
		return Bukkit.getOfflinePlayer(player).getUniqueId();
	}
	public UUID getCustomer() {
		return Bukkit.getOfflinePlayer(customer).getUniqueId();
	}
	public ItemStack getItem() {
		return item;
	}
	public double getMoney() {
		return money;
	}
	public int getAmt() {
		return amt;
	}
	public boolean getSell() {
		return sell;
	}
	
	public TextComponent announcementString() {
		DecimalFormat df = new DecimalFormat("0.00");
		if (sell) {
			TextComponent message1 = new TextComponent("Player "+ChatColor.BLUE+WorldShop.getFriendlyOwnerName(customer)+ChatColor.WHITE+" has purchased "+ChatColor.YELLOW+amt+ChatColor.WHITE+" of your ");
			TextComponent message2 = new TextComponent(ChatColor.GREEN+"["+GenericFunctions.GetItemName(this.item)+ChatColor.RESET+""+ChatColor.GREEN+"]");
			message2.setHoverEvent(new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(GenericFunctions.GetItemName(this.item)+WorldShop.GetItemInfo(this.item)).create()));
			TextComponent message3 = new TextComponent(". You have earned $"+df.format(money)+". "+ChatColor.GRAY+""+ChatColor.ITALIC+"(See /money)");
			TextComponent finalmsg = message1;
			finalmsg.addExtra(message2);
			finalmsg.addExtra(message3);
			return finalmsg;
		} else {
			TextComponent message1 = new TextComponent("Player "+ChatColor.BLUE+WorldShop.getFriendlyOwnerName(customer)+ChatColor.WHITE+" has sold "+ChatColor.YELLOW+amt+ChatColor.WHITE+" ");
			TextComponent message2 = new TextComponent(ChatColor.GREEN+"["+GenericFunctions.GetItemName(this.item)+ChatColor.RESET+""+ChatColor.GREEN+"]");
			message2.setHoverEvent(new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(GenericFunctions.GetItemName(this.item)+WorldShop.GetItemInfo(this.item)).create()));
			TextComponent message3 = new TextComponent(" to you. $"+df.format(money)+" has been deducted from your bank account. "+ChatColor.GRAY+""+ChatColor.ITALIC+"(Check your shop to collect your items.)");
			TextComponent finalmsg = message1;
			finalmsg.addExtra(message2);
			finalmsg.addExtra(message3);
			return finalmsg;
		}
	}
	
	public String toString() {
		return "ShopPurchase[Seller:"+player+",Customer:"+customer+",Item:"+item.toString()+",Money:"+money+",Amt:"+amt+",Sell:"+sell+"]";
		//return player;
	}
}