package sig.plugin.TwosideKeeper.HelperStructures;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionType;

import sig.plugin.TwosideKeeper.TwosideKeeper;

public class WorldShop {
	ItemStack item;
	String owner;
	double price;
	int amt;
	int id;
	
	public WorldShop (ItemStack i, int amt, double p, String player, int shopID) {
		this.item=i;
		this.price=p;
		this.owner=player;
		this.amt = amt;
		this.id = shopID;
	}
	
	public String GetItemName() {
		if (this.item.hasItemMeta() &&
				this.item.getItemMeta().hasDisplayName()) {
			return this.item.getItemMeta().getDisplayName();
		} else {
			return TwosideKeeper.UserFriendlyMaterialName(this.item);
		}
	}
	public void UpdateAmount(int amt) {
		this.amt=amt;
	}
	public void UpdateUnitPrice(double price) {
		this.price=price;
	}
	
	public ItemStack GetItem() {
		return item;
	}
	public double GetUnitPrice() {
		return price;
	}
	public int getID() {
		return id;
	}
	public int GetAmount() {
		if (owner.equalsIgnoreCase("admin")) {
			return 10000;
		} else {
			return amt;
		}
	}
	public String GetOwner() {
		return owner;
	}
	
	public String toString() {
		return "WorldShop:{Item:"+item.toString()+",Price:"+price+",Amount:"+amt+",Owner:"+owner+"}";
	}
	
	public static String GetItemInfo(ItemStack item) {
		//Gets all the info about this item in one gigantic string. (Separated by new lines. Useful for tellraw()).
		String message = "";
		for (int i=0;i<Enchantment.values().length;i++) {
			if (item.containsEnchantment(Enchantment.values()[i])) {
				message+=((message.equals(""))?"":"\n")+ChatColor.GRAY+getRealName(Enchantment.values()[i])+" "+toRomanNumeral(item.getEnchantmentLevel(Enchantment.getByName(Enchantment.values()[i].getName()))); //This is an enchantment we have.
			}
		}
		if (item.getType()==Material.ENCHANTED_BOOK) {
			if (item.getItemMeta() instanceof EnchantmentStorageMeta) {
				EnchantmentStorageMeta e = (EnchantmentStorageMeta)item.getItemMeta();
				for (int i=0;i<Enchantment.values().length;i++) {
					if (e.hasStoredEnchant((Enchantment.values()[i]))) {
						message+="\n"+ChatColor.GRAY+getRealName(Enchantment.values()[i])+" "+toRomanNumeral(e.getStoredEnchantLevel(Enchantment.getByName(Enchantment.values()[i].getName()))); //This is an enchantment we have.
					}
				}
			}
		}
		if (item.hasItemMeta() &&
				item.getItemMeta().hasLore()) {
			for (int i=0;i<item.getItemMeta().getLore().size();i++) {
				message+="\n"+item.getItemMeta().getLore().get(i);
			}
		}
		
		if (item.getType().toString().contains("HELMET") ||
				item.getType().toString().contains("CHESTPLATE") ||
				item.getType().toString().contains("LEGGINGS") ||
				item.getType().toString().contains("BOOTS") ||
				item.getType().toString().contains("SHIELD")) {
			//Display the durability for these items.
			int maxdura = 0;
			switch (item.getType()) {
				case LEATHER_HELMET:{
					maxdura = 56;
				}break;
				case GOLD_HELMET:{
					maxdura = 78;
				}break;
				case IRON_HELMET:{
					maxdura = 166;
				}break;
				case CHAINMAIL_HELMET:{
					maxdura = 166;
				}break;
				case DIAMOND_HELMET:{
					maxdura = 364;
				}break;
				case LEATHER_CHESTPLATE:{
					maxdura = 81;
				}break;
				case GOLD_CHESTPLATE:{
					maxdura = 113;
				}break;
				case IRON_CHESTPLATE:{
					maxdura = 241;
				}break;
				case CHAINMAIL_CHESTPLATE:{
					maxdura = 241;
				}break;
				case DIAMOND_CHESTPLATE:{
					maxdura = 529;
				}break;
				case LEATHER_LEGGINGS:{
					maxdura = 76;
				}break;
				case GOLD_LEGGINGS:{
					maxdura = 106;
				}break;
				case IRON_LEGGINGS:{
					maxdura = 226;
				}break;
				case CHAINMAIL_LEGGINGS:{
					maxdura = 226;
				}break;
				case DIAMOND_LEGGINGS:{
					maxdura = 496;
				}break;
				case LEATHER_BOOTS:{
					maxdura = 66;
				}break;
				case GOLD_BOOTS:{
					maxdura = 92;
				}break;
				case IRON_BOOTS:{
					maxdura = 196;
				}break;
				case CHAINMAIL_BOOTS:{
					maxdura = 196;
				}break;
				case DIAMOND_BOOTS:{
					maxdura = 430;
				}break;
				case SHIELD:{
					maxdura = 430;
				}break;
			}
			message+="\n\n"+ChatColor.GRAY+"Durability: "+(maxdura-item.getDurability()-1)+"/"+(maxdura-1);
		}
		return message;
	}

	public void sendItemInfo(Player player) {
		//Returns all the lore and enchantments for this particular item to the player, so they know what this item is.
		String[] temp = GetItemInfo(item).split("\n");
		for (int i=0;i<temp.length;i++) {
			player.sendMessage("  "+temp[i]);
		}
	}
	
	static String getRealName(Enchantment enchant) {
		if (enchant.getName().equalsIgnoreCase("arrow_damage")) {return "Power";}
		if (enchant.getName().equalsIgnoreCase("arrow_fire")) {return "Flame";}
		if (enchant.getName().equalsIgnoreCase("arrow_infinite")) {return "Infinity";}
		if (enchant.getName().equalsIgnoreCase("arrow_knockback")) {return "Punch";}
		if (enchant.getName().equalsIgnoreCase("damage_all")) {return "Sharpness";}
		if (enchant.getName().equalsIgnoreCase("damage_arthropods")) {return "Bane of Arthropods";}
		if (enchant.getName().equalsIgnoreCase("damage_undead")) {return "Smite";}
		if (enchant.getName().equalsIgnoreCase("depth_strider")) {return "Depth Strider";}
		if (enchant.getName().equalsIgnoreCase("dig_speed")) {return "Efficiency";}
		if (enchant.getName().equalsIgnoreCase("durability")) {return "Unbreaking";}
		if (enchant.getName().equalsIgnoreCase("fire_aspect")) {return "Fire Aspect";}
		if (enchant.getName().equalsIgnoreCase("frost_walker")) {return "Frost Walker";}
		if (enchant.getName().equalsIgnoreCase("knockback")) {return "Knockback";}
		if (enchant.getName().equalsIgnoreCase("loot_bonus_blocks")) {return "Fortune";}
		if (enchant.getName().equalsIgnoreCase("loot_bonus_mobs")) {return "Looting";}
		if (enchant.getName().equalsIgnoreCase("luck")) {return "Luck of the Sea";}
		if (enchant.getName().equalsIgnoreCase("lure")) {return "Lure";}
		if (enchant.getName().equalsIgnoreCase("mending")) {return "Mending";}
		if (enchant.getName().equalsIgnoreCase("oxygen")) {return "Respiration";}
		if (enchant.getName().equalsIgnoreCase("protection_environmental")) {return "Protection";}
		if (enchant.getName().equalsIgnoreCase("protection_explosions")) {return "Blast Protection";}
		if (enchant.getName().equalsIgnoreCase("protection_fall")) {return "Feather Falling";}
		if (enchant.getName().equalsIgnoreCase("protection_fire")) {return "Fire Protection";}
		if (enchant.getName().equalsIgnoreCase("protection_projectile")) {return "Projectile Protection";}
		if (enchant.getName().equalsIgnoreCase("silk_touch")) {return "Silk Touch";}
		if (enchant.getName().equalsIgnoreCase("thorns")) {return "Thorns";}
		if (enchant.getName().equalsIgnoreCase("water_worker")) {return "Aqua Affinity";}
		return "";
	}
	
	static String toRomanNumeral(int val) {
		switch (val) {
			case 1:{
				return "I";
			}
			case 2:{
				return "II";
			}
			case 3:{
				return "III";
			}
			case 4:{
				return "IV";
			}
			case 5:{
				return "V";
			}
			case 6:{
				return "VI";
			}
			case 7:{
				return "VII";
			}
			case 8:{
				return "VIII";
			}
			case 9:{
				return "IX";
			}
			case 10:{
				return "X";
			}
			default:{
				return "";
			}
		}
	}
	
	public static boolean IsWorldSign(Sign s) {
		if (s.getLine(0).equalsIgnoreCase(ChatColor.BLUE+"-- SHOP --")) {
			return true;
		} else {
			return false;
		}
	}
}
