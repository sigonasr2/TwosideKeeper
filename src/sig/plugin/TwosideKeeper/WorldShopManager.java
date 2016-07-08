package sig.plugin.TwosideKeeper;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import sig.plugin.TwosideKeeper.HelperStructures.SessionState;
import sig.plugin.TwosideKeeper.HelperStructures.ShopPurchase;
import sig.plugin.TwosideKeeper.HelperStructures.WorldShop;
import sig.plugin.TwosideKeeper.HelperStructures.WorldShopSession;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;

public class WorldShopManager {
	List<WorldShopSession> sessions = new ArrayList<WorldShopSession>();
	List<ShopPurchase> purchases = new ArrayList<ShopPurchase>();
	
	//Save itemstack in file.
	//Reference an ID.
	
	/**
	 * Creates a World shop.
	 * @param s The sign the shop is created on.
	 * @param item The item being sold.
	 * @param price The unit price of the item.
	 * @param owner Owner of the shop.
	 */
	public WorldShop CreateWorldShop(Sign s, ItemStack item, int amt, double price, String owner) {
		//Convert the sign.
		return CreateWorldShop(s,item,amt,price,owner,false);
	}
	
	public WorldShop CreateWorldShop(Sign s, ItemStack item, int amt, double price, String owner, boolean purchaseshop) {
		//Convert the sign.
		String[] lines = s.getLines();
		List<String> sign_lines = new ArrayList<String>();
		WorldShop newshop = new WorldShop(item, amt, 0, price, owner, TwosideKeeper.WORLD_SHOP_ID);
		if (lines[0].equalsIgnoreCase("shop") || lines[0].equalsIgnoreCase("buyshop")) {
			UpdateSign(newshop, TwosideKeeper.WORLD_SHOP_ID, s,purchaseshop);
		}
		TwosideKeeper.WORLD_SHOP_ID++;
		return newshop;
	}

	public static void UpdateSign(WorldShop shop, Sign s) {
		//Convert the sign.
		String[] lines = s.getLines();
		List<String> sign_lines = new ArrayList<String>();
		
		//Determine if it's a purchase shop by reading the sign.
		boolean purchaseshop=false;
		if (!lines[0].equalsIgnoreCase(ChatColor.BLUE+"-- SHOP --")) {
			purchaseshop=true;
		}
		
		//Create a shop out of this.
		if (purchaseshop) {
			if (shop.GetStoredAmount()>0) {
				sign_lines.add(ChatColor.YELLOW+""+ChatColor.BOLD+"-BUYING SHOP-");
			} else {
				sign_lines.add(ChatColor.BLUE+"- BUYING SHOP -");
			}
		} else {
			sign_lines.add(ChatColor.BLUE+"-- SHOP --");
		}
		if (shop.GetItem().hasItemMeta() &&
				shop.GetItem().getItemMeta().hasDisplayName()) {
			sign_lines.add(shop.GetItem().getItemMeta().getDisplayName());
		} else {
			sign_lines.add(GenericFunctions.UserFriendlyMaterialName(shop.GetItem()));
		}
		DecimalFormat df = new DecimalFormat("0.00");
		sign_lines.add("$"+df.format(shop.GetUnitPrice())+ChatColor.DARK_BLUE+" [x"+shop.GetAmount()+"]");
		DecimalFormat df2 = new DecimalFormat("000000");
		sign_lines.add(ChatColor.DARK_GRAY+df2.format(shop.getID()));
		for (int i=0;i<4;i++) {
			s.setLine(i, sign_lines.get(i));
		}
		s.update();
	}
	
	public static void UpdateSign(WorldShop shop, int id, Sign s, boolean purchaseshop) {
		//Convert the sign.
		String[] lines = s.getLines();
		List<String> sign_lines = new ArrayList<String>();
		//Create a shop out of this.
		if (purchaseshop) {
			if (shop.GetStoredAmount()>0) {
				sign_lines.add(ChatColor.YELLOW+""+ChatColor.BOLD+"-BUYING SHOP-");
			} else {
				sign_lines.add(ChatColor.BLUE+"- BUYING SHOP -");
			}
		} else {
			sign_lines.add(ChatColor.BLUE+"-- SHOP --");
		}
		if (shop.GetItem().hasItemMeta() &&
				shop.GetItem().getItemMeta().hasDisplayName()) {
			sign_lines.add(shop.GetItem().getItemMeta().getDisplayName());
		} else {
			sign_lines.add(GenericFunctions.UserFriendlyMaterialName(shop.GetItem()));
		}
		DecimalFormat df = new DecimalFormat("0.00");
		sign_lines.add("$"+df.format(shop.GetUnitPrice())+ChatColor.DARK_BLUE+" [x"+shop.GetAmount()+"]");
		DecimalFormat df2 = new DecimalFormat("000000");
		sign_lines.add(ChatColor.DARK_GRAY+df2.format(id));
		for (int i=0;i<4;i++) {
			s.setLine(i, sign_lines.get(i));
		}
		s.update();
	}
	
	public int GetShopID(Sign s) {
		return Integer.parseInt(s.getLines()[3].replace(ChatColor.DARK_GRAY+"", ""));
	}

	public WorldShop LoadWorldShopData(Sign s) {
		return LoadWorldShopData(GetShopID(s));
	}
	
	public WorldShop LoadWorldShopData(int id) {
		File config;
		config = new File(TwosideKeeper.filesave,"worldshop.data");
		FileConfiguration workable = YamlConfiguration.loadConfiguration(config);
		
		return new WorldShop(workable.getItemStack("item"+id),workable.getInt("amt"+id),workable.getInt("storedamt"+id),workable.getDouble("item_price"+id),workable.getString("owner"+id),id);
	}
	
	public void SaveWorldShopData(WorldShop shop) {
		File config;
		config = new File(TwosideKeeper.filesave,"worldshop.data");
		FileConfiguration workable = YamlConfiguration.loadConfiguration(config);
		
		int id = shop.getID();
		
		workable.set("item"+id,shop.GetItem());
		workable.set("item_price"+id,shop.GetUnitPrice());
		workable.set("amt"+id,shop.GetAmount());
		workable.set("owner"+id,shop.GetOwner());
		workable.set("storedamt"+id,shop.GetStoredAmount());
		
		try {
			workable.save(config);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public List<WorldShopSession> GetSessions() {
		return sessions;
	}
	public WorldShopSession GetSession(int session) {
		return sessions.get(session);
	}
	public WorldShopSession GetSession(Player p) {
		return sessions.get(GetPlayerTerminal(p));
	}
	public boolean IsPlayerUsingTerminal(Player p) {
		for (int i=0;i<sessions.size();i++) {
			if (sessions.get(i).GetPlayer().equals(p)) {
				return true;
			}
		}
		return false;
	} 
	int GetPlayerTerminal(Player p) {
		if (p!=null) {
			for (int i=0;i<sessions.size();i++) {
				if (sessions.get(i).GetPlayer().equals(p)) {
					return i;
				}
			}
		}
		return -1;
	} 
	public WorldShopSession AddSession(SessionState type, Player p, Sign s) {
		//If the player is in a session, simply update the session type.
		if (IsPlayerUsingTerminal(p)) {
			UpdateSession(type,p);
			WorldShopSession ss = GetSession(p);
			ss.SetSign(s);
			ss.UpdateTime();
			return ss;
		} else {
			WorldShopSession sss = new WorldShopSession(p, TwosideKeeper.getServerTickTime(), type, s);
			sessions.add(sss);
			return sss;
		}
	}
	public void UpdateSession(SessionState type, Player p) {
		int term = GetPlayerTerminal(p);
		if (term!=-1) {
			WorldShopSession ss = sessions.get(term);
			ss.SetSession(type);
		}
	}
	public void UpdateSession(WorldShopSession ss, SessionState type) {
		ss.SetSession(type);
	}
	public void RemoveSession(Player p) {
		int term = GetPlayerTerminal(p);
		if (term!=-1) {
			sessions.remove(term);
		}
	}
	public void RemoveSession(WorldShopSession ss) {
		sessions.remove(ss);
	}

	public void AddNewPurchase(String owner, Player purchaser, int shopID, double price, int amt) {
		purchases.add(new ShopPurchase(owner, purchaser, shopID, price, amt));
	}
	public void AddNewPurchase(String owner, Player purchaser, int shopID, double price, int amt, boolean sell) {
		purchases.add(new ShopPurchase(owner, purchaser, shopID, price, amt, sell));
	}
	public boolean PlayerHasPurchases(Player p) {
		for (int i=0;i<purchases.size();i++) {
			if (p.getName().equalsIgnoreCase(purchases.get(i).getPlayer())) {
				return true;
			}
		}
		return false;
	}
	public void PlayerSendPurchases(Player p) {
		for (int i=0;i<purchases.size();i++) {
			if (p.getName().equalsIgnoreCase(purchases.get(i).getPlayer())) {
				p.spigot().sendMessage(purchases.get(i).announcementString());
				purchases.remove(i);
				i--;
			}
		}
	}
	
	public void Cleanup() {
		//Removes all shop sessions.
		sessions.clear();
	}
}
