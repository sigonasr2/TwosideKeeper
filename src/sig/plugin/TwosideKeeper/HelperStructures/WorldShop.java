package sig.plugin.TwosideKeeper.HelperStructures;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionType;
import org.bukkit.util.Vector;

import com.google.common.collect.Iterables;

import sig.plugin.TwosideKeeper.Artifact;
import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;

public class WorldShop {
	ItemStack item;
	String owner;
	double price;
	int amt;
	int storedamt = 0;
	int id;
	
	public WorldShop (ItemStack i, int amt, int storedamt, double p, String player, int shopID) {
		this.item=i;
		this.price=p;
		this.owner=player;
		this.amt = amt;
		this.storedamt = storedamt;
		this.id = shopID;
	}
	
	public String GetItemName() {
		if (this.item.hasItemMeta() &&
				this.item.getItemMeta().hasDisplayName()) {
			return this.item.getItemMeta().getDisplayName();
		} else {
			return GenericFunctions.UserFriendlyMaterialName(this.item);
		}
	}
	public int GetStoredAmount() {
		return this.storedamt;
	}
	public void UpdateAmount(int amt) {
		this.amt=amt;
	}
	public void UpdateStoredAmount(int newamt) {
		this.storedamt=newamt;
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
		return "WorldShop:{Item:"+item.toString()+",Price:"+price+",Amount:"+amt+",Stored Amount:"+storedamt+",Owner:"+owner+"}";
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
		
		if (item.getType().toString().contains("RECORD")) {
			switch (item.getType()) {
				case GREEN_RECORD:{
					message+=ChatColor.GRAY+"\nC418 - cat";
				}break;
				case GOLD_RECORD:{
					message+=ChatColor.GRAY+"\nC418 - 13";
				}break;
				case RECORD_3:{
					message+=ChatColor.GRAY+"\nC418 - blocks";
				}break;
				case RECORD_4:{
					message+=ChatColor.GRAY+"\nC418 - chirp";
				}break;
				case RECORD_5:{
					message+=ChatColor.GRAY+"\nC418 - far";
				}break;
				case RECORD_6:{
					message+=ChatColor.GRAY+"\nC418 - mall";
				}break;
				case RECORD_7:{
					message+=ChatColor.GRAY+"\nC418 - melohi";
				}break;
				case RECORD_8:{
					message+=ChatColor.GRAY+"\nC418 - stal";
				}break;
				case RECORD_9:{
					message+=ChatColor.GRAY+"\nC418 - strad";
				}break;
				case RECORD_10:{
					message+=ChatColor.GRAY+"\nC418 - ward";
				}break;
				case RECORD_11:{
					message+=ChatColor.GRAY+"\nC418 - 11";
				}break;
				case RECORD_12:{
					message+=ChatColor.GRAY+"\nC418 - wait";
				}break;
			}
		}
		
		if (item.getType().toString().contains("HELMET") ||
				item.getType().toString().contains("CHESTPLATE") ||
				item.getType().toString().contains("LEGGINGS") ||
				item.getType().toString().contains("BOOTS") ||
				item.getType().toString().contains("SHIELD") ||
				item.getType().toString().contains("BOW") ||
				item.getType().toString().contains("SWORD") ||
				item.getType().toString().contains("AXE") ||
				item.getType().toString().contains("HOE") ||
				item.getType().toString().contains("SPADE") ||
				item.getType().toString().contains("CARROT_STICK") ||
				item.getType().toString().contains("ELYTRA") ||
				item.getType().toString().contains("FISHING_ROD") ||
				item.getType().toString().contains("FLINT_AND_STEEL")) {
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
					maxdura = 337;
				}break;
				case BOW:{
					maxdura = 385;
				}break;
				case WOOD_SWORD:{
					maxdura = 60;
				}break;
				case STONE_SWORD:{
					maxdura = 132;
				}break;
				case IRON_SWORD:{
					maxdura = 251;
				}break;
				case DIAMOND_SWORD:{
					maxdura = 1562;
				}break;
				case GOLD_SWORD:{
					maxdura = 33;
				}break;
				case WOOD_PICKAXE:{
					maxdura = 60;
				}break;
				case STONE_PICKAXE:{
					maxdura = 132;
				}break;
				case IRON_PICKAXE:{
					maxdura = 251;
				}break;
				case DIAMOND_PICKAXE:{
					maxdura = 1562;
				}break;
				case GOLD_PICKAXE:{
					maxdura = 33;
				}break;
				case WOOD_AXE:{
					maxdura = 60;
				}break;
				case STONE_AXE:{
					maxdura = 132;
				}break;
				case IRON_AXE:{
					maxdura = 251;
				}break;
				case DIAMOND_AXE:{
					maxdura = 1562;
				}break;
				case GOLD_AXE:{
					maxdura = 33;
				}break;
				case WOOD_HOE:{
					maxdura = 60;
				}break;
				case STONE_HOE:{
					maxdura = 132;
				}break;
				case IRON_HOE:{
					maxdura = 251;
				}break;
				case DIAMOND_HOE:{
					maxdura = 1562;
				}break;
				case GOLD_HOE:{
					maxdura = 33;
				}break;
				case WOOD_SPADE:{
					maxdura = 60;
				}break;
				case STONE_SPADE:{
					maxdura = 132;
				}break;
				case IRON_SPADE:{
					maxdura = 251;
				}break;
				case DIAMOND_SPADE:{
					maxdura = 1562;
				}break;
				case GOLD_SPADE:{
					maxdura = 33;
				}break;
				case FISHING_ROD:{
					maxdura = 65;
				}break;
				case FLINT_AND_STEEL:{
					maxdura = 65;
				}break;
				case CARROT_STICK:{
					maxdura = 26;
				}break;
				case ELYTRA:{
					maxdura = 432;
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
	
	public static boolean isWorldShopSign(Sign s) {
		if (s.getLine(0).equalsIgnoreCase(ChatColor.BLUE+"-- SHOP --") ||
				s.getLine(0).equalsIgnoreCase(ChatColor.BLUE+"- BUYING SHOP -")) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean hasShopSignAttached(Block b) {
		//Returns true if there is a shop sign attached to this block.
		//Look on all four sides relative to this block.
		TwosideKeeper.log("Reference block is "+b.getLocation().toString()+" of type "+b.getType(),5);
		for (int x=-1;x<=1;x++) {
			for (int z=-1;z<=1;z++) {
				if ((x!=0 || z!=0) && Math.abs(x)!=Math.abs(z)) {
					Block checkblock = b.getRelative(x, 0, z);
					TwosideKeeper.log("This is a "+checkblock.getType(),5);
					if (checkblock.getType()==Material.WALL_SIGN) {
						org.bukkit.material.Sign s = (org.bukkit.material.Sign)(checkblock.getState().getData());
						//See if this sign is "facing" this block.
						TwosideKeeper.log("Checked block is a "+checkblock.getRelative(s.getAttachedFace()).getType()+" at Loc "+checkblock.getRelative(s.getAttachedFace()).getLocation().toString(), 4);
						if (checkblock.getRelative(s.getAttachedFace()).equals(b)) {
							TwosideKeeper.log("This is the block on the sign! Proceed.",5);
							//See if this sign is a world shop.
							if (WorldShop.isWorldShopSign((Sign)checkblock.getState())) {
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	public static void spawnShopItem(PlayerInteractEvent ev, Location loc, WorldShop shop) {
		//See if a drop entity is already here.
		boolean item_here=false;
		Collection<Entity> entities = ev.getPlayer().getLocation().getWorld().getNearbyEntities(loc, 1, 1, 1);
		for (int i=0;i<entities.size();i++) {
			Entity e = Iterables.get(entities, i);
			TwosideKeeper.log("Entity Location:"+e.getLocation().toString(),5);
			TwosideKeeper.log("Comparing locations: "+e.getLocation().toString()+":::"+loc.toString(),5);
			if (e.getType()==EntityType.DROPPED_ITEM) {
				Item it = (Item)e;
				if (
						it.getItemStack().getType()==shop.GetItem().getType() &&
						it.getItemStack().getDurability()==shop.GetItem().getDurability() &&
						it.getCustomName()!=null &&
						it.getCustomName().equalsIgnoreCase(""+shop.getID()) &&
						Math.abs(e.getLocation().getX()-loc.getX()) <= 1 &&
						Math.abs(e.getLocation().getZ()-loc.getZ()) <= 1 &&
						Math.abs(e.getLocation().getY()-loc.getY())<=1
						) {
					item_here=true;
				}
			}
		}
		if (!item_here) {
			TwosideKeeper.log("Spawning item!",5);
			ItemStack i = shop.GetItem().clone();
			ItemStack drop = Artifact.convert(i);
			drop.removeEnchantment(Enchantment.LUCK);
			Item it = ev.getPlayer().getWorld().dropItemNaturally(ev.getClickedBlock().getLocation().add(-ev.getBlockFace().getModX()+0.5, -ev.getBlockFace().getModY()+1.5, -ev.getBlockFace().getModZ()+0.5), drop);
			it.setPickupDelay(999999999);
			it.setVelocity(new Vector(0,0,0));
			it.setCustomName(""+shop.getID());
			it.setCustomNameVisible(false);
			it.setInvulnerable(true);
			//it.setGlowing(true);
			it.teleport(ev.getClickedBlock().getLocation().add(-ev.getBlockFace().getModX()+0.5, -ev.getBlockFace().getModY()+1.5, -ev.getBlockFace().getModZ()+0.5));
		}
	}
}
