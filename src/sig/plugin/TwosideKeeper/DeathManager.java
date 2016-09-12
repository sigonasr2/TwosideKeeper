package sig.plugin.TwosideKeeper;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import sig.plugin.TwosideKeeper.HelperStructures.DeathStructure;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;

public class DeathManager {
	static String Pick5Text = "Mercy (Pick 5 Lost Items)";
	static String Pick20Text = "Mercy (Pick 20 Lost Items Randomly)";
	static String BuybackText = "Buyback (Pay for Lost Items)";
	static String DropText = "Drop Items (At death point)";
	public static List<DeathStructure> ds = new ArrayList<DeathStructure>();
	
	public static void addNewDeathStructure(List<ItemStack> deathinv, Location deathloc, Player p) {
		ds.add(new DeathStructure(deathinv,deathloc,p));
		TwosideKeeper.log("Added a new Death Structure: "+ds.get(ds.size()-1).toString(),5);
	}
	public static void removeDeathStructure(Player p) {
		ds.remove(getDeathStructure(p));
		Bukkit.getScheduler().scheduleSyncDelayedTask(TwosideKeeper.plugin, new Runnable() {
			@Override
			public void run() {
				GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.LEVITATION,15,-2,p,true);
				GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.JUMP,15,100,p,true);
				p.setVelocity(new Vector(0,0,0));
				CustomDamage.removeIframe(p);
				Location loc = p.getLocation();
				if (p.getBedSpawnLocation()!=null) {loc.setY(p.getBedSpawnLocation().getY());}
				p.teleport(loc);
			}},1);
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		//pd.deathloot.clear();
		pd.hasDied=false;
		p.setCollidable(true);
	}
	public static boolean deathStructureExists(Player p) {
		if (getDeathStructure(p)!=null) {
			return true;
		} else {
			return false;
		}
	}
	/*
	public static void givePlayerDeathChoices(Player p) {
		//Also stop the player from moving.
		ItemStack pick_five = new ItemStack(Material.COMMAND);
		ItemMeta meta = pick_five.getItemMeta();
		meta.setDisplayName(Pick5Text);
		pick_five.setItemMeta(meta);
		pick_five.addUnsafeEnchantment(Enchantment.LUCK, 1);
		ItemStack pick_twenty = new ItemStack(Material.COMMAND_CHAIN);
		meta = pick_twenty.getItemMeta();
		meta.setDisplayName(Pick20Text);
		pick_twenty.setItemMeta(meta);
		pick_twenty.addUnsafeEnchantment(Enchantment.LUCK, 1);
		ItemStack buyback = new ItemStack(Material.COMMAND_REPEATING);
		meta = buyback.getItemMeta();
		meta.setDisplayName(BuybackText);
		buyback.setItemMeta(meta);
		buyback.addUnsafeEnchantment(Enchantment.LUCK, 1);
		ItemStack normaldrop = new ItemStack(Material.BARRIER);
		meta = normaldrop.getItemMeta();
		meta.setDisplayName(DropText);
		normaldrop.setItemMeta(meta);
		normaldrop.addUnsafeEnchantment(Enchantment.LUCK, 1);
		

		p.getInventory().addItem(pick_five);
		p.getInventory().addItem(pick_twenty);
		p.getInventory().addItem(buyback);
		p.getInventory().addItem(normaldrop);
		
		p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,999999999,10));
		p.sendMessage(ChatColor.AQUA+"Place down the block indicating how you want to retrieve your lost items.");
	}*/
	public static boolean isDeathBlock(ItemStack b) {
		if (b.hasItemMeta() &&
				b.getItemMeta().hasDisplayName() &&
				b.containsEnchantment(Enchantment.LUCK)) {
			return true;
		} else {
			return false;
		}
	}
	
	public static DeathStructure getDeathStructure(Player p) {
		for (int i=0;i<ds.size();i++) {
			if (ds.get(i).p.equalsIgnoreCase(p.getName())) {
				return ds.get(i);
			}
		}
		return null;
	}
	
	public static void continueAction(Player p) {
		//Pick 5
		DeathStructure structure = getDeathStructure(p);
		
		Inventory deathinv = Bukkit.getServer().createInventory(p, 45, "Death Loot");
		GenericFunctions.TransferItemsToInventory(p.getInventory(), deathinv);
		double totalmoney = TwosideKeeper.getPlayerMoney(Bukkit.getPlayer(p.getName()))+TwosideKeeper.getPlayerBankMoney(Bukkit.getPlayer(p.getName()));
		int price = 1;
		if (structure.deathloc.getBlockY()<=60) {
			price += 24-(structure.deathloc.getBlockY()/2.5);
		}
		
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(Bukkit.getPlayer(p.getName()));
		pd.hasDied=false;
		
		p.openInventory(deathinv);
		p.sendMessage(ChatColor.AQUA+"You can buy back up to "+ChatColor.YELLOW+(int)(totalmoney/price)+ChatColor.AQUA+" items, costing $"+ChatColor.GREEN+price+ChatColor.WHITE+" per item.");
		p.sendMessage("  The rest will drop at your death location.");
		p.sendMessage(ChatColor.GRAY+"Close your inventory once you've picked your items.");
	}
		
	public static int CalculateDeathPrice(Player p) {
		DeathStructure ds = getDeathStructure(p);
		return (int)(1+((ds.deathloc.getBlockY()<=60)?(24-(ds.deathloc.getBlockY()/2.5)):0));
	}
	public static int CountOccupiedSlots(Inventory inv) {
		int occupiedslots = 0;
		for (int i=0;i<inv.getSize();i++) {
			if (inv.getItem(i)!=null &&
					inv.getItem(i).getType()!=Material.AIR) {
				occupiedslots++;
			}
		}
		return occupiedslots;
	}
}
