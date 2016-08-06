package sig.plugin.TwosideKeeper.HelperStructures.Common;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Wool;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.inventivetalent.glow.GlowAPI;
import org.inventivetalent.glow.GlowAPI.Color;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import sig.plugin.TwosideKeeper.Artifact;
import sig.plugin.TwosideKeeper.AwakenedArtifact;
import sig.plugin.TwosideKeeper.MonsterController;
import sig.plugin.TwosideKeeper.MonsterStructure;
import sig.plugin.TwosideKeeper.NewCombat;
import sig.plugin.TwosideKeeper.PlayerStructure;
import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.HelperStructures.ArtifactAbility;
import sig.plugin.TwosideKeeper.HelperStructures.BowMode;
import sig.plugin.TwosideKeeper.HelperStructures.ItemSet;
import sig.plugin.TwosideKeeper.HelperStructures.WorldShop;

public class GenericFunctions {

	public static int getHardenedItemBreaks(ItemStack item) {
		if (item.hasItemMeta() &&
				item.getItemMeta().hasLore()) {
			ItemMeta item_meta = item.getItemMeta();
			int breaks_remaining=-1;
			int loreline=-1;
			for (int i=0;i<item_meta.getLore().size();i++) {
				if (item_meta.getLore().get(i).contains(ChatColor.GRAY+"Breaks Remaining: ")) {
					if (item_meta.getLore().get(i).contains(ChatColor.GRAY+"Breaks Remaining: "+ChatColor.MAGIC)) {
						TwosideKeeper.log("This is obscure. Breaks is "+(Integer.parseInt(item.getItemMeta().getLore().get(i).split(": "+ChatColor.MAGIC)[1])), 2);
						return getObscureHardenedItemBreaks(item);
					} else {
						return Integer.parseInt(item.getItemMeta().getLore().get(i).split(": "+ChatColor.YELLOW)[1]);
					}
				}
			}
			return 0;
		}
		return 0;
	}

	public static ItemStack breakHardenedItem(ItemStack item) {
		return breakHardenedItem(item,null);
	}

	public static ItemStack breakHardenedItem(ItemStack item, Player p) {
		int break_count = getHardenedItemBreaks(item);
		boolean is_magic = false;
		if (break_count>0) {
			ItemMeta m = item.getItemMeta();
			List<String> lore = item.getItemMeta().getLore();
			for (int i=0;i<lore.size();i++) {
				if (lore.get(i).contains(ChatColor.GRAY+"Breaks Remaining: ")) {
					if (lore.get(i).contains(ChatColor.GRAY+"Breaks Remaining: "+ChatColor.MAGIC)) {
						is_magic=true;
						TwosideKeeper.log("This is obscure.", 2);
						break_count--;
						if (p!=null && break_count==0) {
			    				p.sendMessage(ChatColor.GOLD+"WARNING!"+ChatColor.GREEN+ " Your "+ChatColor.YELLOW+GenericFunctions.UserFriendlyMaterialName(item)+ChatColor.WHITE+" is going to break soon! You should let it recharge by waiting 24 hours!");
						}
						p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
						return breakObscureHardenedItem(item);
					} else {
						lore.set(i, ChatColor.GRAY+"Breaks Remaining: "+ChatColor.YELLOW+(break_count-1));
						TwosideKeeper.log("Setting breaks remaining to "+(break_count-1),3);
					}
				}
			}
			m.setLore(lore);
			item.setItemMeta(m);
			item.setAmount(1);
			item.setDurability((short)0);
			TwosideKeeper.log("New item is "+item.toString(),4);
			break_count--;
			if (p!=null && break_count==0) {
    			p.sendMessage(ChatColor.GOLD+"WARNING!"+ChatColor.GREEN+ " Your "+ChatColor.YELLOW+GenericFunctions.UserFriendlyMaterialName(item)+ChatColor.WHITE+" is going to break soon!");
			}
			p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
			return item;
			//By setting the amount to 1, you refresh the item in the player's inventory.
		} else {
			//This item is technically destroyed.
			p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
			return new ItemStack(Material.AIR);
		}
	}
	
	public static ItemStack addHardenedItemBreaks(ItemStack item, int breaks) {
		if (isHardenedItem(item)) {
			//We can just modify the amount of breaks.
			return modifyBreaks(item, getHardenedItemBreaks(item)+breaks,false);
		} else {
			//We need to add a new line in regards to making this item hardened. Two lines if it's armor.
			ItemMeta m =item.getItemMeta();
			List<String> lore = new ArrayList<String>();
			if (item.hasItemMeta() &&
					item.getItemMeta().hasLore()) {
				lore = m.getLore();
			}
			if (!Artifact.isArtifact(item)) {
				if (isArmor(item)) {
					lore.add(ChatColor.BLUE+""+ChatColor.ITALIC+"Hardened Armor");
					lore.add(ChatColor.GRAY+"Twice as strong");
				} else
				if (isWeapon(item)) {
					lore.add(ChatColor.GRAY+"Twice as strong");
				}
			}
			lore.add(ChatColor.GRAY+"Breaks Remaining: "+ChatColor.YELLOW+breaks);
			m.setLore(lore);
			if (m.hasDisplayName()) {
				m.setDisplayName(ChatColor.BLUE+"Hardened "+m.getDisplayName());
			} else {
				m.setDisplayName(ChatColor.BLUE+"Hardened "+UserFriendlyMaterialName(item));
			}
			item.setItemMeta(m);
			return item;
		}
	}
	
	public static ItemStack addObscureHardenedItemBreaks(ItemStack item, int breaks) {
		if (isObscureHardenedItem(item)) {
			//We can just modify the amount of breaks.
			return modifyBreaks(item, getHardenedItemBreaks(item)+breaks,true);
		} else {
			//We need to add a new line in regards to making this item hardened. Two lines if it's armor.
			ItemMeta m =item.getItemMeta();
			List<String> lore = new ArrayList<String>();
			if (item.hasItemMeta() &&
					item.getItemMeta().hasLore()) {
				lore = m.getLore();
			}
			if (!Artifact.isArtifact(item)) {
				if (isArmor(item)) {
					lore.add(ChatColor.BLUE+""+ChatColor.ITALIC+"Hardened Armor");
					lore.add(ChatColor.GRAY+"Twice as strong");
				} else
				if (isWeapon(item)) {
					lore.add(ChatColor.GRAY+"Twice as strong");
				}
			}
			lore.add(ChatColor.GRAY+"Breaks Remaining: "+ChatColor.MAGIC+breaks);
			lore.add(ChatColor.BLUE+""+ChatColor.MAGIC+TwosideKeeper.getServerTickTime());
			m.setLore(lore);
			if (m.hasDisplayName()) {
				m.setDisplayName(ChatColor.BLUE+"Hardened "+m.getDisplayName());
			} else {
				m.setDisplayName(ChatColor.BLUE+"Hardened "+UserFriendlyMaterialName(item));
			}
			item.setItemMeta(m);
			item.setAmount(1);
			item.setDurability((short)0);
			return item;
		}
	}
	
	public static ItemStack modifyBreaks(ItemStack item, int newbreaks, boolean isObscure) {
		//Find the line with Breaks Remaining.
		if (item.hasItemMeta() &&
				item.getItemMeta().hasLore()) {
			ItemMeta item_meta = item.getItemMeta();
			int breaks_remaining=-1;
			int loreline=-1;
			for (int i=0;i<item_meta.getLore().size();i++) {
				TwosideKeeper.log("Line is "+item_meta.getLore().get(i),4);
				TwosideKeeper.log("Checking for "+ChatColor.GRAY+"Breaks Remaining: "+((!isObscure)?ChatColor.YELLOW:ChatColor.MAGIC),4);
				if (item_meta.getLore().get(i).contains(ChatColor.GRAY+"Breaks Remaining: "+((!isObscure)?ChatColor.YELLOW:ChatColor.MAGIC))) {
					TwosideKeeper.log("Line is "+item_meta.getLore().get(i),3);
					loreline = i;
					break;
				}
			}
			//Found it. Now we will modify it and return the new item.
			List<String> newlore = item_meta.getLore();
			newlore.set(loreline, ChatColor.GRAY+"Breaks Remaining: "+((!isObscure)?ChatColor.YELLOW:ChatColor.MAGIC)+newbreaks);
			item_meta.setLore(newlore);
			item.setItemMeta(item_meta);
			return item;
		} else {
			return null;
		}
	}
	
	public static int getMaxThornsLevel(LivingEntity e) {
		int maxlv = 0;
		ItemStack[] equips = e.getEquipment().getArmorContents();
		for (int i=0;i<equips.length;i++) {
			if (equips[i]!=null &&
					equips[i].getType()!=Material.AIR) {
				if (equips[i].getEnchantmentLevel(Enchantment.THORNS)>=maxlv) {
					maxlv = equips[i].getEnchantmentLevel(Enchantment.THORNS);
				}
			}
		}
		return maxlv;
	}
	

	public static int getObscureHardenedItemBreaks(ItemStack item) {
		if (item.hasItemMeta() &&
				item.getItemMeta().hasLore()) {
			ItemMeta item_meta = item.getItemMeta();
			int breaks_remaining=-1;
			int loreline=-1;
			int break_line=-1;
			int break_count=0;
			for (int i=0;i<item_meta.getLore().size();i++) {
				if (item_meta.getLore().get(i).contains(ChatColor.GRAY+"Breaks Remaining: "+ChatColor.MAGIC)) {
					breaks_remaining = Integer.parseInt(item.getItemMeta().getLore().get(i).split(": "+ChatColor.MAGIC)[1]);
				}
			}
			break_count = breaks_remaining;
			ItemMeta m = item.getItemMeta();
			List<String> lore = item.getItemMeta().getLore();
			for (int i=0;i<lore.size();i++) {
				if (lore.get(i).contains(ChatColor.GRAY+"Breaks Remaining: ")) {
					break_line = i;
				}
				if (lore.get(i).contains(ChatColor.BLUE+""+ChatColor.MAGIC)) {
					//See what the previous time was.
					long time = Long.parseLong(ChatColor.stripColor(lore.get(i)));
					TwosideKeeper.log("The old time was "+time, 2);
					if (TwosideKeeper.getServerTickTime()-time>=1728000) //1.7M ticks per day. 
						{
							int charges_stored = (int)((TwosideKeeper.getServerTickTime()-time)/1728000);
							TwosideKeeper.log(charges_stored+" charges stored. Adding them.", 2);
							break_count+=charges_stored;
							lore.set(i, ChatColor.BLUE+""+ChatColor.MAGIC+TwosideKeeper.getServerTickTime());
							TwosideKeeper.log("Setting time to "+TwosideKeeper.getServerTickTime(),3);
						}
				}
			}
			if (break_count>6) {break_count=6;}
			lore.set(break_line, ChatColor.GRAY+"Breaks Remaining: "+ChatColor.MAGIC+(break_count));
			TwosideKeeper.log("Setting breaks remaining to "+(break_count),3);
			m.setLore(lore);
			item.setItemMeta(m);
			return break_count;
		}
		return 0;
	}

	
	public static ItemStack breakObscureHardenedItem(ItemStack item) {
		int break_count = getObscureHardenedItemBreaks(item)-1;
		int break_line = -1;
		if (break_count>-1) {
			ItemMeta m = item.getItemMeta();
			List<String> lore = item.getItemMeta().getLore();
			for (int i=0;i<lore.size();i++) {
				if (lore.get(i).contains(ChatColor.GRAY+"Breaks Remaining: ")) {
					break_line = i;
				}
				if (lore.get(i).contains(ChatColor.BLUE+""+ChatColor.MAGIC)) {
					//See what the previous time was.
					long time = Long.parseLong(ChatColor.stripColor(lore.get(i)));
					TwosideKeeper.log("The old time was "+time, 2);
					if (TwosideKeeper.getServerTickTime()-time>=1728000) //1.7M ticks per day. 
						{
							int charges_stored = (int)((TwosideKeeper.getServerTickTime()-time)/1728000);
							TwosideKeeper.log(charges_stored+" charges stored. Adding them.", 2);
							break_count+=charges_stored;
							lore.set(i, ChatColor.BLUE+""+ChatColor.MAGIC+TwosideKeeper.getServerTickTime());
							TwosideKeeper.log("Setting time to "+TwosideKeeper.getServerTickTime(),3);
						}
				}
			}
			if (break_count>5) {break_count=5;}
			lore.set(break_line, ChatColor.GRAY+"Breaks Remaining: "+ChatColor.MAGIC+(break_count));
			TwosideKeeper.log("Setting breaks remaining to "+(break_count),3);
			m.setLore(lore);
			item.setItemMeta(m);
			item.setAmount(1);
			item.setDurability((short)0);
			if (isArtifactEquip(item)) {
				//Restore potential to 100%.
				AwakenedArtifact.addPotential(item, 100-AwakenedArtifact.getPotential(item));
			}
			TwosideKeeper.log("New item is "+item.toString(),2);
			return item;
			//By setting the amount to 1, you refresh the item in the player's inventory.
		} else {
			//This item is technically destroyed.
			return new ItemStack(Material.AIR);
		}
	}
	

	public static String UserFriendlyMaterialName(Material type) {
		return UserFriendlyMaterialName(new ItemStack(type,1,(byte)0));
	}
	public static String UserFriendlyMaterialName(Material type, byte b) {
		return UserFriendlyMaterialName(new ItemStack(type,1,b));
	}

	public static String UserFriendlyPotionEffectTypeName(PotionEffectType type) {
		switch (type.getName()) {

			case "UNLUCK":{
				return "Bad Luck";
			}
			case "SLOW_DIGGING":{
				return "Mining Fatigue";
			}
			case "SLOW":{
				return "Slowness";
			}
			case "JUMP":{
				return "Jump Boost";
			}
			case "INCREASE_DAMAGE":{
				return "Strength";
			}
			case "HEAL":{
				return "Instant Health";
			}
			case "HARM":{
				return "Harming";
			}
			case "FAST_DIGGING":{
				return "Haste";
			}
			case "DAMAGE_RESISTANCE":{
				return "Resistance";
			}
			case "CONFUSION":{
				return "Nausea";
			}
			default: {
				return GenericFunctions.CapitalizeFirstLetters(type.getName().replace("_", " "));
			}
		
		}
	}
	
	public static String UserFriendlyMaterialName(ItemStack type) {
		if (type!=null &&
				type.getType()!=Material.AIR) {
			if (type.hasItemMeta() &&
					type.getItemMeta().hasDisplayName()) {
				return type.getItemMeta().getDisplayName();
			}
			switch (type.getType()) {
				case ACACIA_DOOR_ITEM:{
					return "Acacia Door";
				}
				case JUNGLE_DOOR_ITEM:{
					return "Jungle Door";
				}
				case BIRCH_DOOR_ITEM:{
					return "Birch Door";
				}
				case DARK_OAK_DOOR_ITEM:{
					return "Dark Oak Door";
				}
				case SPRUCE_DOOR_ITEM:{
					return "Spruce Door";
				}
				case WOOD_DOOR:{
					return "Wooden Door";
				}
				case BED_BLOCK:{
					return "Bed";
				}
				case BOAT_ACACIA:{
					return "Acacia Boat";
				}
				case BOAT_BIRCH:{
					return "Birch Boat";
				}
				case BOAT_DARK_OAK:{
					return "Dark Oak Boat";
				}
				case BOAT_JUNGLE:{
					return "Jungle Boat";
				}
				case BOAT_SPRUCE:{
					return "Spruce Boat";
				}
				case BREWING_STAND_ITEM:{
					return "Brewing Stand";
				}
				case BURNING_FURNACE:{
					return "Furnace";
				}
				case CAKE_BLOCK:{
					return "Cake";
				}
				case CARROT_ITEM:{
					return "Carrot";
				}
				case CARROT_STICK:{
					return "Carrot on a Stick";
				}
				case CAULDRON_ITEM:{
					return "Cauldron";
				}
				case CHORUS_FRUIT_POPPED:{
					return "Popped Chorus Fruit";
				}
				case CLAY_BALL:{
					return "Clay";
				}
				case COBBLE_WALL:{
					return "Mossy Cobblestone Wall";
				}
				case COMMAND:{
					return "Command Block";
				}
				case COMMAND_CHAIN:{
					return "Chain Command Block";
				}
				case COMMAND_MINECART:{
					return "Minecart w/Command Block";
				}
				case COMMAND_REPEATING:{
					return "Repeating Command Block";
				}
				case CROPS:{
					return "Sugar Cane";
				}
				case DAYLIGHT_DETECTOR_INVERTED:{
					return "Daylight Detector";
				}
				case WOOD_SPADE:{
					return "Wooden Shovel";
				}
				case STONE_SPADE:{
					return "Stone Shovel";
				}
				case IRON_SPADE:{
					return "Iron Shovel";
				}
				case GOLD_SPADE:{
					return "Gold Shovel";
				}
				case DIAMOND_SPADE:{
					return "Diamond Shovel";
				}
				case IRON_BARDING:{
					return "Iron Horse Armor";
				}
				case GOLD_BARDING:{
					return "Gold Horse Armor";
				}
				case DIAMOND_BARDING:{
					return "Diamond Horse Armor";
				}
				case DIODE:{
					return "Redstone Repeater";
				}
				case DIODE_BLOCK_OFF:{
					return "Redstone Repeater";
				}
				case DIODE_BLOCK_ON:{
					return "Diamond Horse Armor";
				}
				case DRAGONS_BREATH:{
					return "Dragon's Breath";
				}
				case END_CRYSTAL:{
					return "Ender Crystal";
				}
				case ENDER_STONE:{
					return "End Stone";
				}
				case EXPLOSIVE_MINECART:{
					return "TNT Minecart";
				}
				case FLOWER_POT_ITEM:{
					return "Flower Pot";
				}
				case GLOWING_REDSTONE_ORE:{
					return "Redstone Ore";
				}
				case GRILLED_PORK:{
					return "Cooked Porkchop";
				}
				case HUGE_MUSHROOM_1:{
					return "Brown Mushroom";
				}
				case HUGE_MUSHROOM_2:{
					return "Red Mushroom";
				}
				case JACK_O_LANTERN:{
					return "Jack o'Lantern";
				}
				case LEAVES:{
					switch (type.getDurability()) {
						case 0:{
							return "Oak Leaves";
						}
						case 1:{
							return "Spruce Leaves";
						}
						case 2:{
							return "Birch Leaves";
						}
						case 3:{
							return "Jungle Leaves";
						}
					}
				}
				case LEAVES_2:{
					switch (type.getDurability()) {
						case 0:{
							return "Acacia Leaves";
						}
						case 1:{
							return "Dark Oak Leaves";
						}
					}
				}
				case LOG:{
					switch (type.getDurability()) {
						case 0:{
							return "Oak Wood";
						}
						case 1:{
							return "Spruce Wood";
						}
						case 2:{
							return "Birch Wood";
						}
						case 3:{
							return "Jungle Wood";
						}
					}
				}
				case LOG_2:{
					switch (type.getDurability()) {
						case 0:{
							return "Acacia Wood";
						}
						case 1:{
							return "Dark Oak Wood";
						}
					}
				}
				case WOOD:{
					switch (type.getDurability()) {
						case 0:{
							return "Oak Wood Planks";
						}
						case 1:{
							return "Spruce Wood Planks";
						}
						case 2:{
							return "Birch Wood Planks";
						}
						case 3:{
							return "Jungle Wood Planks";
						}
						case 4:{
							return "Acacia Wood Planks";
						}
						case 5:{
							return "Dark Oak Wood Planks";
						}
					}
				}
				case MILK_BUCKET:{
					return "Milk";
				}
				case NETHER_BRICK:{
					return "Nether Bricks";
				}
				case NETHER_BRICK_ITEM:{
					return "Nether Brick";
				}
				case NETHER_WARTS:{
					return "Nether Wart";
				}
				case NETHER_STALK:{
					return "Nether Wart";
				}
				case GOLD_PLATE:{
					return "Weighted Pressure Plate (Light)";
				}
				case PISTON_BASE:{
					return "Piston";
				}
				case PISTON_STICKY_BASE:{
					return "Sticky Piston";
				}
				case PORK:{
					return "Raw Porkchop";
				}
				case POTATO_ITEM:{
					return "Potato";
				}
				case POWERED_MINECART:{
					return "Minecart w/Furnace";
				}
				case RABBIT:{
					return "Raw Rabbit";
				}
				case RABBIT_FOOT:{
					return "Rabbit's Foot";
				}
				case RECORD_10:{
					return "Music Disc";
				}
				case RECORD_11:{
					return "Music Disc";
				}
				case RECORD_12:{
					return "Music Disc";
				}
				case RECORD_3:{
					return "Music Disc";
				}
				case RECORD_4:{
					return "Music Disc";
				}
				case RECORD_5:{
					return "Music Disc";
				}
				case RECORD_6:{
					return "Music Disc";
				}
				case RECORD_7:{
					return "Music Disc";
				}
				case RECORD_8:{
					return "Music Disc";
				}
				case RECORD_9:{
					return "Music Disc";
				}
				case REDSTONE_COMPARATOR:{
					return "Comparator";
				}
				case REDSTONE_COMPARATOR_OFF:{
					return "Comparator";
				}
				case REDSTONE_COMPARATOR_ON:{
					return "Comparator";
				}
				case REDSTONE_LAMP_OFF:{
					return "Redstone Lamp";
				}
				case REDSTONE_LAMP_ON:{
					return "Redstone Lamp";
				}
				case REDSTONE_TORCH_OFF:{
					return "Redstone Torch";
				}
				case REDSTONE_TORCH_ON:{
					return "Redstone Torch";
				}
				case REDSTONE_WIRE:{
					return "Redstone";
				}
				case SAPLING:{
					switch (type.getDurability()) {
						case 0:{
							return "Oak Sapling";
						}
						case 1:{
							return "Spruce Sapling";
						}
						case 2:{
							return "Birch Sapling";
						}
						case 3:{
							return "Jungle Sapling";
						}
						case 4:{
							return "Acacia Sapling";
						}
						case 5:{
							return "Dark Oak Sapling";
						}
					}
				}
				case SIGN_POST:{
					return "Sign";
				}
				case WALL_SIGN:{
					return "Sign";
				}
				case SKULL_ITEM:{
					return "Skull";
				}
				case SMOOTH_BRICK:{
					switch (type.getDurability()) {
						case 0:{
							return "Stone Brick";
						}
						case 1:{
							return "Mossy Stone Brick";
						}
						case 2:{
							return "Cracked Stone Brick";
						}
						case 3:{
							return "Chiseled Stone Brick";
						}
					}
				}
				case SMOOTH_STAIRS:{
					return "Stone Brick Stairs";
				}
				case LEATHER_HELMET:{
					return "Leather Cap";
				}
				case LEATHER_CHESTPLATE:{
					return "Leather Tunic";
				}
				case LEATHER_LEGGINGS:{
					return "Leather Pants";
				}
				case STEP:{
					switch (type.getDurability()) {
						case 0:{
							return "Stone Slab";
						}
						case 1:{
							return "Sandstone Slab";
						}
						case 2:{
							return "Fireproof Oak Wooden Slab";
						}
						case 3:{
							return "Cobblestone Slab";
						}
						case 4:{
							return "Bricks Slab";
						}
						case 5:{
							return "Stone Brick Slab";
						}
						case 6:{
							return "Nether Brick Slab";
						}
						case 7:{
							return "Quartz Slab";
						}
					}
				}
				case SULPHUR:{
					return "Gunpowder";
				}
				case TNT:{
					return "TNT";
				}
				case POTION:{
					PotionMeta pm = (PotionMeta)type.getItemMeta();
					/*
					return "Lingering Potion of "+CapitalizeFirstLetters(pm.getBasePotionData().getType().toString().toLowerCase());
					*/
					switch (pm.getBasePotionData().getType()) {
						case AWKWARD:
							return "Awkward Potion";
						case FIRE_RESISTANCE:
							return "Potion of Fire Resistance";
						case INSTANT_DAMAGE:
								if (pm.getBasePotionData().isUpgraded()) {
									return "Potion of Harming II";
								} else {
									return "Potion of Harming";
								}
						case INSTANT_HEAL:
							if (pm.getBasePotionData().isUpgraded()) {
								return "Potion of Instant Health II";
							} else {
								return "Potion of Instant Health";
							}
						case INVISIBILITY:
							return "Potion of Invisibility";
						case JUMP:
							if (pm.getBasePotionData().isUpgraded()) {
								return "Potion of Jump Boost II";
							} else {
								return "Potion of Jump Boost";
							}
						case LUCK:
							return "Potion of Luck";
						case MUNDANE:
							return "Mundane Potion";
						case NIGHT_VISION:
							return "Potion of Night Vision";
						case POISON:
							if (pm.getBasePotionData().isUpgraded()) {
								return "Potion of Poison II";
							} else {
								return "Potion of Poison";
							}
						case REGEN:
							if (pm.getBasePotionData().isUpgraded()) {
								return "Potion of Regeneration II";
							} else {
								return "Potion of Regeneration";
							}
						case SLOWNESS:
							if (pm.getBasePotionData().isUpgraded()) {
								return "Potion of Slowness II";
							} else {
								return "Potion of Slowness";
							}
						case SPEED:
							if (pm.getBasePotionData().isUpgraded()) {
								return "Potion of Speed II";
							} else {
								return "Potion of Speed";
							}
						case STRENGTH:
							if (pm.getBasePotionData().isUpgraded()) {
								return "Potion of Strength II";
							} else {
								return "Potion of Strength";
							}
						case THICK:
							return "Thick Potion";
						case UNCRAFTABLE:
							return "Potion";
						case WATER:
							return "Water Bottle";
						case WATER_BREATHING:
							return "Potion of Water Breathing";
						case WEAKNESS:
							if (pm.getBasePotionData().isUpgraded()) {
								return "Potion of Weakness II";
							} else {
								return "Potion of Weakness";
							}
						default:
							return "Potion";
					}
				}
				case SPLASH_POTION:{
					PotionMeta pm = (PotionMeta)type.getItemMeta();
					/*
					return "Lingering Potion of "+CapitalizeFirstLetters(pm.getBasePotionData().getType().toString().toLowerCase());
					*/
					switch (pm.getBasePotionData().getType()) {
						case AWKWARD:
							return "Awkward Splash Potion";
						case FIRE_RESISTANCE:
							return "Splash Potion of Fire Resistance";
						case INSTANT_DAMAGE:
								if (pm.getBasePotionData().isUpgraded()) {
									return "Splash Potion of Harming II";
								} else {
									return "Splash Potion of Harming";
								}
						case INSTANT_HEAL:
							if (pm.getBasePotionData().isUpgraded()) {
								return "Splash Potion of Instant Health II";
							} else {
								return "Splash Potion of Instant Health";
							}
						case INVISIBILITY:
							return "Splash Potion of Invisibility";
						case JUMP:
							if (pm.getBasePotionData().isUpgraded()) {
								return "Splash Potion of Jump Boost II";
							} else {
								return "Splash Potion of Jump Boost";
							}
						case LUCK:
							return "Splash Potion of Luck";
						case MUNDANE:
							return "Mundane Splash Potion";
						case NIGHT_VISION:
							return "Splash Potion of Night Vision";
						case POISON:
							if (pm.getBasePotionData().isUpgraded()) {
								return "Splash Potion of Poison II";
							} else {
								return "Splash Potion of Poison";
							}
						case REGEN:
							if (pm.getBasePotionData().isUpgraded()) {
								return "Splash Potion of Regeneration II";
							} else {
								return "Splash Potion of Regeneration";
							}
						case SLOWNESS:
							if (pm.getBasePotionData().isUpgraded()) {
								return "Splash Potion of Slowness II";
							} else {
								return "Splash Potion of Slowness";
							}
						case SPEED:
							if (pm.getBasePotionData().isUpgraded()) {
								return "Splash Potion of Speed II";
							} else {
								return "Splash Potion of Speed";
							}
						case STRENGTH:
							if (pm.getBasePotionData().isUpgraded()) {
								return "Splash Potion of Strength II";
							} else {
								return "Splash Potion of Strength";
							}
						case THICK:
							return "Thick Splash Potion";
						case UNCRAFTABLE:
							return "Splash Potion";
						case WATER:
							return "Water Bottle";
						case WATER_BREATHING:
							return "Splash Potion of Water Breathing";
						case WEAKNESS:
							if (pm.getBasePotionData().isUpgraded()) {
								return "Splash Potion of Weakness II";
							} else {
								return "Splash Potion of Weakness";
							}
						default:
							return "Splash Potion";
					}
				}
				case TIPPED_ARROW:{
					PotionMeta pm = (PotionMeta)type.getItemMeta();
					/*
					return "Lingering Potion of "+CapitalizeFirstLetters(pm.getBasePotionData().getType().toString().toLowerCase());
					*/
					switch (pm.getBasePotionData().getType()) {
						case AWKWARD:
							return "Awkward Arrow";
						case FIRE_RESISTANCE:
							return "Arrow of Fire Resistance";
						case INSTANT_DAMAGE:
								if (pm.getBasePotionData().isUpgraded()) {
									return "Arrow of Harming II";
								} else {
									return "Arrow of Harming";
								}
						case INSTANT_HEAL:
							if (pm.getBasePotionData().isUpgraded()) {
								return "Arrow of Instant Health II";
							} else {
								return "Arrow of Instant Health";
							}
						case INVISIBILITY:
							return "Arrow of Invisibility";
						case JUMP:
							if (pm.getBasePotionData().isUpgraded()) {
								return "Arrow of Jump Boost II";
							} else {
								return "Arrow of Jump Boost";
							}
						case LUCK:
							return "Arrow of Luck";
						case MUNDANE:
							return "Mundane Arrow";
						case NIGHT_VISION:
							return "Arrow of Night Vision";
						case POISON:
							if (pm.getBasePotionData().isUpgraded()) {
								return "Arrow of Poison II";
							} else {
								return "Arrow of Poison";
							}
						case REGEN:
							if (pm.getBasePotionData().isUpgraded()) {
								return "Arrow of Regeneration II";
							} else {
								return "Arrow of Regeneration";
							}
						case SLOWNESS:
							if (pm.getBasePotionData().isUpgraded()) {
								return "Arrow of Slowness II";
							} else {
								return "Arrow of Slowness";
							}
						case SPEED:
							if (pm.getBasePotionData().isUpgraded()) {
								return "Arrow of Speed II";
							} else {
								return "Arrow of Speed";
							}
						case STRENGTH:
							if (pm.getBasePotionData().isUpgraded()) {
								return "Arrow of Strength II";
							} else {
								return "Arrow of Strength";
							}
						case THICK:
							return "Thick Arrow";
						case UNCRAFTABLE:
							return "Arrow";
						case WATER:
							return "Water Bottle";
						case WATER_BREATHING:
							return "Arrow of Water Breathing";
						case WEAKNESS:
							if (pm.getBasePotionData().isUpgraded()) {
								return "Arrow of Weakness II";
							} else {
								return "Arrow of Weakness";
							}
						default:
							return "Arrow";
					}
				}
				case LINGERING_POTION:{
					PotionMeta pm = (PotionMeta)type.getItemMeta();
					/*
					return "Lingering Potion of "+CapitalizeFirstLetters(pm.getBasePotionData().getType().toString().toLowerCase());
					*/
					switch (pm.getBasePotionData().getType()) {
						case AWKWARD:
							return "Awkward Lingering Potion";
						case FIRE_RESISTANCE:
							return "Lingering Potion of Fire Resistance";
						case INSTANT_DAMAGE:
								if (pm.getBasePotionData().isUpgraded()) {
									return "Lingering Potion of Harming II";
								} else {
									return "Lingering Potion of Harming";
								}
						case INSTANT_HEAL:
							if (pm.getBasePotionData().isUpgraded()) {
								return "Lingering Potion of Instant Health II";
							} else {
								return "Lingering Potion of Instant Health";
							}
						case INVISIBILITY:
							return "Lingering Potion of Invisibility";
						case JUMP:
							if (pm.getBasePotionData().isUpgraded()) {
								return "Lingering Potion of Jump Boost II";
							} else {
								return "Lingering Potion of Jump Boost";
							}
						case LUCK:
							return "Lingering Potion of Luck";
						case MUNDANE:
							return "Mundane Lingering Potion";
						case NIGHT_VISION:
							return "Lingering Potion of Night Vision";
						case POISON:
							if (pm.getBasePotionData().isUpgraded()) {
								return "Lingering Potion of Poison II";
							} else {
								return "Lingering Potion of Poison";
							}
						case REGEN:
							if (pm.getBasePotionData().isUpgraded()) {
								return "Lingering Potion of Regeneration II";
							} else {
								return "Lingering Potion of Regeneration";
							}
						case SLOWNESS:
							if (pm.getBasePotionData().isUpgraded()) {
								return "Lingering Potion of Slowness II";
							} else {
								return "Lingering Potion of Slowness";
							}
						case SPEED:
							if (pm.getBasePotionData().isUpgraded()) {
								return "Lingering Potion of Speed II";
							} else {
								return "Lingering Potion of Speed";
							}
						case STRENGTH:
							if (pm.getBasePotionData().isUpgraded()) {
								return "Lingering Potion of Strength II";
							} else {
								return "Lingering Potion of Strength";
							}
						case THICK:
							return "Thick Lingering Potion";
						case UNCRAFTABLE:
							return "Lingering Potion";
						case WATER:
							return "Lingering Water Bottle";
						case WATER_BREATHING:
							return "Lingering Potion of Water Breathing";
						case WEAKNESS:
							if (pm.getBasePotionData().isUpgraded()) {
								return "Lingering Potion of Weakness II";
							} else {
								return "Lingering Potion of Weakness";
							}
						default:
							return "Lingering Potion";
					}
				}
				case WOOD_STEP:{
					switch (type.getDurability()) {
						case 0:{
							return "Oak Wood Slab";
						}
						case 1:{
							return "Spruce Wood Slab";
						}
						case 2:{
							return "Birch Wood Slab";
						}
						case 3:{
							return "Jungle Wood Slab";
						}
						case 4:{
							return "Acacia Wood Slab";
						}
						case 5:{
							return "Dark Oak Wood Slab";
						}
					}
				}
				case SAND:{
					switch (type.getDurability()) {
						case 0:{
							return "Sand";
						}
						case 1:{
							return "Red Sand";
						}
					}
				}
				case INK_SACK:{
					switch (type.getDurability()) {
						case 0:{
							return "Ink Sac";
						}
						case 1:{
							return "Rose Red";
						}
						case 2:{
							return "Cactus Green";
						}
						case 3:{
							return "Cocoa Beans";
						}
						case 4:{
							return "Lapis Lazuli";
						}
						case 5:{
							return "Purple Dye";
						}
						case 6:{
							return "Cyan Dye";
						}
						case 7:{
							return "Light Gray Dye";
						}
						case 8:{
							return "Gray Dye";
						}
						case 9:{
							return "Pink Dye";
						}
						case 10:{
							return "Lime Dye";
						}
						case 11:{
							return "Dandelion Yellow";
						}
						case 12:{
							return "Light Blue Dye";
						}
						case 13:{
							return "Magenta Dye";
						}
						case 14:{
							return "Orange Dye";
						}
						case 15:{
							return "Bone Meal";
						}
					}
				}
				case HARD_CLAY:{
					return "Hardened Clay";
				}
				case BANNER:{
					switch (15-type.getDurability()) {
						case 0:{
							return "White Banner";
						}
						case 1:{
							return "Orange Banner";
						}
						case 2:{
							return "Magenta Banner";
						}
						case 3:{
							return "Light Blue Banner";
						}
						case 4:{
							return "Yellow Banner";
						}
						case 5:{
							return "Lime Banner";
						}
						case 6:{
							return "Pink Banner";
						}
						case 7:{
							return "Gray Banner";
						}
						case 8:{
							return "Light Gray Banner";
						}
						case 9:{
							return "Cyan Banner";
						}
						case 10:{
							return "Purple Banner";
						}
						case 11:{
							return "Blue Banner";
						}
						case 12:{
							return "Brown Banner";
						}
						case 13:{
							return "Green Banner";
						}
						case 14:{
							return "Red Banner";
						}
						case 15:{
							return "Black Banner";
						}
					}
				}
				case STAINED_CLAY:{
					switch (type.getDurability()) {
						case 0:{
							return "White Stained Clay";
						}
						case 1:{
							return "Orange Stained Clay";
						}
						case 2:{
							return "Magenta Stained Clay";
						}
						case 3:{
							return "Light Blue Stained Clay";
						}
						case 4:{
							return "Yellow Stained Clay";
						}
						case 5:{
							return "Lime Stained Clay";
						}
						case 6:{
							return "Pink Stained Clay";
						}
						case 7:{
							return "Gray Stained Clay";
						}
						case 8:{
							return "Light Gray Stained Clay";
						}
						case 9:{
							return "Cyan Stained Clay";
						}
						case 10:{
							return "Purple Stained Clay";
						}
						case 11:{
							return "Blue Stained Clay";
						}
						case 12:{
							return "Brown Stained Clay";
						}
						case 13:{
							return "Green Stained Clay";
						}
						case 14:{
							return "Red Stained Clay";
						}
						case 15:{
							return "Black Stained Clay";
						}
					}
				}
				case WOOL:{
					switch (type.getDurability()) {
						case 0:{
							return "White Wool";
						}
						case 1:{
							return "Orange Wool";
						}
						case 2:{
							return "Magenta Wool";
						}
						case 3:{
							return "Light Blue Wool";
						}
						case 4:{
							return "Yellow Wool";
						}
						case 5:{
							return "Lime Wool";
						}
						case 6:{
							return "Pink Wool";
						}
						case 7:{
							return "Gray Wool";
						}
						case 8:{
							return "Light Gray Wool";
						}
						case 9:{
							return "Cyan Wool";
						}
						case 10:{
							return "Purple Wool";
						}
						case 11:{
							return "Blue Wool";
						}
						case 12:{
							return "Brown Wool";
						}
						case 13:{
							return "Green Wool";
						}
						case 14:{
							return "Red Wool";
						}
						case 15:{
							return "Black Wool";
						}
					}
				}
				case THIN_GLASS:{
					return "Glass Pane";
				}
				case STAINED_GLASS:{
					switch (type.getDurability()) {
						case 0:{
							return "White Stained Glass";
						}
						case 1:{
							return "Orange Stained Glass";
						}
						case 2:{
							return "Magenta Stained Glass";
						}
						case 3:{
							return "Light Blue Stained Glass";
						}
						case 4:{
							return "Yellow Stained Glass";
						}
						case 5:{
							return "Lime Stained Glass";
						}
						case 6:{
							return "Pink Stained Glass";
						}
						case 7:{
							return "Gray Stained Glass";
						}
						case 8:{
							return "Light Gray Stained Glass";
						}
						case 9:{
							return "Cyan Stained Glass";
						}
						case 10:{
							return "Purple Stained Glass";
						}
						case 11:{
							return "Blue Stained Glass";
						}
						case 12:{
							return "Brown Stained Glass";
						}
						case 13:{
							return "Green Stained Glass";
						}
						case 14:{
							return "Red Stained Glass";
						}
						case 15:{
							return "Black Stained Glass";
						}
					}
				}
				case STAINED_GLASS_PANE:{
					switch (type.getDurability()) {
						case 0:{
							return "White Stained Glass Pane";
						}
						case 1:{
							return "Orange Stained Glass Pane";
						}
						case 2:{
							return "Magenta Stained Glass Pane";
						}
						case 3:{
							return "Light Blue Stained Glass Pane";
						}
						case 4:{
							return "Yellow Stained Glass Pane";
						}
						case 5:{
							return "Lime Stained Glass Pane";
						}
						case 6:{
							return "Pink Stained Glass Pane";
						}
						case 7:{
							return "Gray Stained Glass Pane";
						}
						case 8:{
							return "Light Gray Stained Glass Pane";
						}
						case 9:{
							return "Cyan Stained Glass Pane";
						}
						case 10:{
							return "Purple Stained Glass Pane";
						}
						case 11:{
							return "Blue Stained Glass Pane";
						}
						case 12:{
							return "Brown Stained Glass Pane";
						}
						case 13:{
							return "Green Stained Glass Pane";
						}
						case 14:{
							return "Red Stained Glass Pane";
						}
						case 15:{
							return "Black Stained Glass Pane";
						}
					}
				}
				case YELLOW_FLOWER:{
					return "Dandelion";
				}
				case RED_ROSE:{
					switch (type.getDurability()) {
						case 0:{
							return "Poppy";
						}
						case 1:{
							return "Blue Orchid";
						}
						case 2:{
							return "Allium";
						}
						case 3:{
							return "Azure Bluet";
						}
						case 4:{
							return "Red Tulip";
						}
						case 5:{
							return "Orange Tulip";
						}
						case 6:{
							return "White Tulip";
						}
						case 7:{
							return "Pink Tulip";
						}
						case 8:{
							return "Oxeye Daisy";
						}
					}
				}
				case WATER_LILY:{
					return "Lily Pad";
				}
				case SUGAR_CANE_BLOCK:{
					return "Sugar Cane";
				}
				case DOUBLE_PLANT:{
					switch (type.getDurability()) {
						case 0:{
							return "Sunflower";
						}
						case 1:{
							return "Lilac";
						}
						case 2:{
							return "Double Tallgrass";
						}
						case 3:{
							return "Large Fern";
						}
						case 4:{
							return "Rose Bush";
						}
						case 5:{
							return "Peony";
						}
					}
				}
				case BOAT:{
					return "Oak Boat";
				}
				case CLAY_BRICK:{
					return "Brick";
				}
				case BRICK:{
					return "Bricks";
				}
				case FIREWORK:{
					return "Firework Rocket";
				}
				case FIREWORK_CHARGE:{
					return "Firework Star";
				}
				case EXP_BOTTLE:{
					return "Bottle o' Enchanting";
				}
				case GOLD_SWORD:{
					return "Golden Sword";
				}
				case HAY_BLOCK:{
					return "Hay Bale";
				}
				case SKULL:{
					return "Wither Skeleton Skull";
				}
				case SPECKLED_MELON:{
					return "Glistering Melon";
				}
				case WORKBENCH:{
					return "Crafting Table";
				}
				case CLAY:{
					return "Clay Block";
				}
				case WOOD_PLATE:{
					return "Wooden Pressure Plate";
				}
				case STONE_PLATE:{
					return "Stone Pressure Plate";
				}
				case IRON_PLATE:{
					return "Weighted Pressure Plate (Heavy)";
				}
				case MOSSY_COBBLESTONE:{
					return "Moss Stone";
				}
				case SANDSTONE:{
					switch (type.getDurability()) {
						case 0:{
							return "Sandstone";
						}
						case 1:{
							return "Chiseled Sandstone";
						}
						case 2:{
							return "Smooth Sandstone";
						}
					}
				}
				case RED_SANDSTONE:{
					switch (type.getDurability()) {
						case 0:{
							return "Red Sandstone";
						}
						case 1:{
							return "Chiseled Red Sandstone";
						}
						case 2:{
							return "Smooth Red Sandstone";
						}
					}
				}
				case TRAP_DOOR:{
					return "Wooden Trapdoor";
				}
				case IRON_FENCE:{
					return "Iron Bars";
				}
				case RAILS:{
					return "Rail";
				}
				case COAL:{
					switch (type.getDurability()) {
						case 0:{
							return "Coal";
						}
						case 1:{
							return "Charcoal";
						}
					}
				}
				case COAL_BLOCK:{
					return "Block of Coal";
				}
				case REDSTONE_BLOCK:{
					return "Block of Redstone";
				}
				case LAPIS_BLOCK:{
					return "Lapis Lazuli Block";
				}
				case EMERALD_BLOCK:{
					return "Block of Emerald";
				}
				case GOLD_BLOCK:{
					return "Block of Gold";
				}
				case IRON_BLOCK:{
					return "Block of Iron";
				}
				case DIAMOND_BLOCK:{
					return "Block of Diamond";
				}
				case QUARTZ:{
					return "Nether Quartz";
				}
				case COOKED_BEEF:{
					return "Steak";
				}
				default:{
					return GenericFunctions.CapitalizeFirstLetters(type.getType().toString().replace("_", " "));
				}
			}
		} else {
			if (Math.random()<=0.01) {
				switch ((int)((Math.random())*29)) {
					case 0: return "Pleased to meet you";
					case 1: return "They aren't gonna like this.";
					case 2: return "Dust'em, Pix!";
					case 3: return "A solid giggle should do the trick.";
					case 4: return "Let's use ALL the colors!";
					case 5: return "Too tall...much too tall";
					case 6: return "I recommend skipping.";
					case 7: return "Just a pinch!";
					case 8: return "You'll see more with your eyes closed.";
					case 9: return "Whoa...dizzy.";
					case 10: return "Nosey dewdrop...";
					case 11: return "Never look a tulip in the eye...";
					case 12: return "That squirrel looks familiar.";
					case 13: return "C'mon you, let's dance! Ha!";
					case 14: return "Let's go around again! Ha!";
					case 15: return "I could go for a twirl... Whoa, whoa whoa ah, wooh!";
					case 16: return "Let's put on our thinking caps! Hmm... hmmhmm, hmmhmm...Ah, I got it!";
					case 17: return "Adoribus!";
					case 18: return "Fuzzy!";
					case 19: return "Delightify!";
					case 20: return "Transmogulate!";
					case 21: return "Cuddly incoming!";
					case 22: return "Zippy!";
					case 23: return "Vroom vroom!";
					case 24: return "Tut tut!";
					case 25: return "Hot foot!";
					case 26: return "Hugeify!";
					case 27: return "Tremendo!";
					case 28: return "Enormibus!";
					default: return "Lulu";
				}
			} else {
				return "Air";
			}
		}
	}

	public static String CapitalizeFirstLetters(String s) {
		if (s.contains(" ")) {
			String[] temp = s.split(" ");
			String finalname = "";
			for (int i=0;i<temp.length;i++) {
				char first;
				if (temp[i].charAt(0)>='a') {
					first = (char)(temp[i].charAt(0)-32);
				} else {
					first = temp[i].charAt(0);
				}
				finalname+=(finalname.equals("")?"":" ")+first+temp[i].toLowerCase().substring(1);
			}
			return finalname;
		} else {
			if (s.charAt(0)>='a') {
				char first = (char)(s.charAt(0)-32);
				return first+s.toLowerCase().substring(1);
			} else {
				char first = (char)(s.charAt(0));
				return first+s.toLowerCase().substring(1);
			}
		}
	}

	public static String GetItemName(ItemStack item) {
		if (item.hasItemMeta() &&
				item.getItemMeta().hasDisplayName()) {
			return item.getItemMeta().getDisplayName();
		} else {
			return UserFriendlyMaterialName(item);
		}
	}

	/**
	 * This function will return the number of items of this type
	 * that exist in an inventory.
	 * @param it
	 * @param item
	 * @return
	 */
	public static int CountItems(Inventory it, ItemStack item) {
		int totalcount=0;
		for (int i=0;i<it.getSize();i++) {
			if (it.getItem(i)!=null &&
					it.getItem(i).isSimilar(item)) {
				totalcount+=it.getItem(i).getAmount();
			}
		}
		return totalcount;
	}
	
	/**
	 * This function will return the amount of empty space that can
	 * be filled with the specified item for the inventory. 
	 * Useful for buy shops.
	 * @param it
	 * @param item
	 * @return
	 */
	public static int CountEmptySpace(Inventory it, ItemStack item) {
		int totalcount=0;
		for (int i=0;i<it.getSize();i++) {
			if (it.getItem(i)!=null &&
					(it.getItem(i).getType()==Material.AIR ||
					it.getItem(i).isSimilar(item))) {
				if (it.getItem(i).getAmount()!=item.getMaxStackSize()) {
					totalcount+=item.getMaxStackSize()-it.getItem(i).getAmount();
				} else {
					//TwosideKeeper.log("This is equivalent to max stack size of "+item.getMaxStackSize(), 2);
					//totalcount+=item.getMaxStackSize();
				}
			} else if (it.getItem(i)==null) {
				totalcount+=item.getMaxStackSize();
			}
		}
		return totalcount;
	}

	/**
	 * This function will return the number of items of this type
	 * that exist in your inventory. It will not include your
	 * equipment.
	 * @param p
	 * @param item
	 * @return
	 */
	public static int CountItems(Player p, ItemStack item) {
		return CountItems(p.getInventory(),item);
	}

	public static ItemStack convertToHardenedPiece(ItemStack item, int breaks) {
		if (item!=null && item.hasItemMeta()) {
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(ChatColor.BLUE+"Hardened "+UserFriendlyMaterialName(item));
			List<String> lore = new ArrayList<String>();
			if (meta.hasLore()) {
				lore.addAll(meta.getLore());
			}
			if (GenericFunctions.isArmor(item)) {
				lore.add(ChatColor.BLUE+""+ChatColor.ITALIC+"Hardened Armor");
				lore.add(ChatColor.GRAY+"Twice as strong");
			} else
			if (GenericFunctions.isTool(item)) {
				lore.add(ChatColor.BLUE+""+ChatColor.ITALIC+"Hardened Weapon");
				lore.add(ChatColor.GRAY+"Twice as strong");
			}
			lore.add(ChatColor.GRAY+"Breaks Remaining: "+ChatColor.YELLOW+breaks);
			meta.setLore(lore);
			item.setItemMeta(meta);
		}
		return item;
	}

	public static boolean isHardenedItem(ItemStack item) {
		if (item.hasItemMeta() &&
				item.getItemMeta().hasLore()) {
			//TwosideKeeper.log("This item has lore...", 2);
			for (int i=0;i<item.getItemMeta().getLore().size();i++) {
				TwosideKeeper.log("Lore line is: "+item.getItemMeta().getLore().get(i), 5);
				if (item.getItemMeta().getLore().get(i).contains(ChatColor.GRAY+"Breaks Remaining:")) {
					TwosideKeeper.log("Item "+item.toString()+" is hardened. Return it!", 5);
					return true;
				}
			}
			return isObscureHardenedItem(item); //Since it's not hardened, see if it's obscure hardened.
		} else {
			return isObscureHardenedItem(item); //Since it's not hardened, see if it's obscure hardened.
		}
	}
	
	public static boolean isObscureHardenedItem(ItemStack item) {
		if (item.hasItemMeta() &&
				item.getItemMeta().hasLore()) {
			//TwosideKeeper.log("This item has lore...", 2);
			for (int i=0;i<item.getItemMeta().getLore().size();i++) {
				TwosideKeeper.log("Lore line is: "+item.getItemMeta().getLore().get(i), 5);
				if (item.getItemMeta().getLore().get(i).contains(ChatColor.GRAY+"Breaks Remaining: "+ChatColor.MAGIC)) {
					TwosideKeeper.log("Item "+item.toString()+" is obscured and hardened. Return it!", 5);
					return true;
				}
			}
			return false; //Nothing found. Return false.
		} else {
			return false;
		}
	}
	public static boolean isArtifactEquip(ItemStack item) {
		if (Artifact.isArtifact(item) &&
				isEquip(item) &&
				item.containsEnchantment(Enchantment.LUCK)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isEquip(ItemStack item) {
		if (item!=null &&
				item.getType()!=Material.AIR && (item.getType().toString().contains("SPADE") ||
			item.getType().toString().contains("AXE") ||
			item.getType().toString().contains("SWORD") ||
			item.getType().toString().contains("BOW") ||
			item.getType().toString().contains("FISHING_ROD") ||
			item.getType().toString().contains("HOE") ||
			item.getType().toString().contains("BOOTS") ||
			item.getType().toString().contains("CHESTPLATE") ||
			item.getType().toString().contains("LEGGINGS") ||
			item.getType().toString().contains("HELMET") ||
			item.getType().toString().contains("FISHING_ROD") ||
			item.getType().toString().contains("SHIELD"))) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isTool(ItemStack item) {
		if (item!=null &&
				item.getType()!=Material.AIR && (item.getType().toString().contains("SPADE") ||
			item.getType().toString().contains("AXE") ||
			item.getType().toString().contains("SWORD") ||
			item.getType().toString().contains("HOE") ||
			item.getType().toString().contains("FISHING_ROD") ||
			item.getType().toString().contains("BOW"))) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isHarvestingTool(ItemStack item) {
		if (item!=null &&
				item.getType()!=Material.AIR && (item.getType().toString().contains("SPADE") ||
			item.getType().toString().contains("AXE") ||
			item.getType().toString().contains("HOE"))) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isWeapon(ItemStack item) {
		if (item!=null &&
				item.getType()!=Material.AIR && (item.getType().toString().contains("BOW") ||
			item.getType().toString().contains("AXE") ||
			item.getType().toString().contains("SWORD") ||
			item.getType().toString().contains("FISHING_ROD") ||
			item.getType().toString().contains("HOE"))) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isArmor(ItemStack item) {
		if (item!=null &&
			item.getType()!=Material.AIR && (item.getType().toString().contains("BOOTS") ||
			item.getType().toString().contains("CHESTPLATE") ||
			item.getType().toString().contains("LEGGINGS") ||
			item.getType().toString().contains("HELMET"))) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isArtifactWeapon(ItemStack item) {
		if (item!=null &&
				item.getType()!=Material.AIR && (item.getType().toString().contains("BOW") ||
			(item.getType().toString().contains("AXE") && !item.getType().toString().contains("PICKAXE")) ||
			item.getType().toString().contains("SWORD") ||
			item.getType().toString().contains("FISHING_ROD") ||
			item.getType().toString().contains("HOE"))) {
			return true;
		} else {
			return false;
		}
	}
	public static boolean isArtifactArmor(ItemStack item) {
		if (item!=null &&
				item.getType()!=Material.AIR && (item.getType().toString().contains("BOOTS") ||
			item.getType().toString().contains("CHESTPLATE") ||
			item.getType().toString().contains("LEGGINGS") ||
			item.getType().toString().contains("HELMET"))) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isArtifactTool(ItemStack item) {
		if (item!=null &&
				item.getType()!=Material.AIR && (item.getType().toString().contains("SPADE") ||
			item.getType().toString().contains("AXE")||
			item.getType().toString().contains("HOE"))) {
			return true; 
		} else {
			return false;
		}
	}

	public static boolean isDefender(Player p) {
		if (p.getEquipment().getItemInMainHand()!=null && p.getEquipment().getItemInMainHand().getType()==Material.SHIELD) {
			return true;
		} else {
			return false;
		}
	}
	public static boolean isStriker(Player p) {
		if (p.getEquipment().getItemInMainHand()!=null && p.getEquipment().getItemInMainHand().getType().toString().contains("SWORD") &&
				p.getInventory().getExtraContents()[0]==null) {
			return true;
		} else {
			return false;
		}
	}
	public static boolean isRanger(Player p) {
		if (p!=null && !p.isDead() && (((p.getEquipment().getItemInMainHand()!=null && p.getEquipment().getItemInMainHand().getType()==Material.BOW && (p.getInventory().getExtraContents()[0]==null || p.getInventory().getExtraContents()[0].getType()==Material.AIR)) || //Satisfy just a bow in main hand.
				(p.getEquipment().getItemInMainHand()!=null && p.getEquipment().getItemInMainHand().getType()==Material.BOW && p.getInventory().getExtraContents()[0]!=null && p.getInventory().getExtraContents()[0].getType()!=Material.SHIELD) ||  /*Satisfy a bow in main hand and no shield in off-hand.*/
				(p.getEquipment().getItemInMainHand()!=null && p.getEquipment().getItemInMainHand().getType()!=Material.SHIELD && p.getInventory().getExtraContents()[0]!=null && p.getInventory().getExtraContents()[0].getType()==Material.BOW) ||  /*Satisfy a bow in off-hand and no shield in main hand.*/
				((p.getEquipment().getItemInMainHand()==null || p.getEquipment().getItemInMainHand().getType()==Material.AIR) && p.getInventory().getExtraContents()[0]!=null && p.getInventory().getExtraContents()[0].getType()==Material.BOW)) /*Satisfy just a bow in off-hand.*/ &&
				AllLeatherArmor(p))) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean AllLeatherArmor(Player p) {
		ItemStack[] equipment = p.getEquipment().getArmorContents();
		boolean leather=true;
		for (int i=0;i<equipment.length;i++) {
			if (equipment[i]!=null &&
					!equipment[i].getType().toString().contains("LEATHER")) {
				leather=false;
				break;
			}
		}
		return leather;
	}
	
	public static String PlayerModePrefix(Player p) {
		if (isDefender(p)) {
			return ChatColor.GRAY+""+ChatColor.ITALIC+"(D) "+ChatColor.RESET+ChatColor.GRAY;
		} else if (isStriker(p)) {
			return ChatColor.RED+""+ChatColor.ITALIC+"(S) "+ChatColor.RESET+ChatColor.RED;
		} else if (isRanger(p)) {
			return ChatColor.DARK_GREEN+""+ChatColor.ITALIC+"(R) "+ChatColor.RESET+ChatColor.DARK_GREEN;
		} else {
			return "";
		}
	}
	
	public static TextComponent PlayerModeName(Player p) {
		TextComponent tc = new TextComponent("");
		if (isDefender(p)) {
			TextComponent tc1 = new TextComponent(ChatColor.GRAY+""+ChatColor.BOLD+"Defender"+ChatColor.RESET);
			tc1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ComponentBuilder("Click to view details about "+ChatColor.GRAY+""+ChatColor.BOLD+"Defender"+ChatColor.RESET+".").create()));
			tc1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/mode Defender"));
			tc.addExtra(tc1);
		} else if (isStriker(p)) {
			TextComponent tc1 = new TextComponent(ChatColor.RED+""+ChatColor.BOLD+"Striker"+ChatColor.RESET);
			tc1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ComponentBuilder("Click to view details about "+ChatColor.RED+""+ChatColor.BOLD+"Strikers"+ChatColor.RESET+".").create()));
			tc1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/mode Striker"));
			tc.addExtra(tc1);
		} else if (isRanger(p)) {
			TextComponent tc1 = new TextComponent(ChatColor.DARK_GREEN+""+ChatColor.BOLD+"Ranger"+ChatColor.RESET);
			tc1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ComponentBuilder("Click to view details about "+ChatColor.DARK_GREEN+""+ChatColor.BOLD+"Ranger"+ChatColor.RESET+".").create()));
			tc1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/mode Ranger"));
			tc.addExtra(tc1);
		} else {
			TextComponent tc1 = new TextComponent(ChatColor.WHITE+"Normal"+ChatColor.RESET);
			tc1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ComponentBuilder("Click to view details about "+ChatColor.WHITE+"Normal"+ChatColor.RESET+".").create()));
			tc1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/mode Normal"));
			tc.addExtra(tc1);
		}
		return tc;
	}
	
	public static String PlayerModeInformation(String mode) {
		switch (mode.toLowerCase()) {
			case "defender":{
				return ChatColor.GRAY+""+ChatColor.BOLD+mode+" mode Perks: "+ChatColor.RESET+"\n"
						+ ChatColor.WHITE+"->Players are identified as 'Defenders' when they use a shield in their main hand.\n"
						+ ChatColor.GRAY+"->Base Damage reduction from shields increases from 5%->10%\n"
						+ ChatColor.WHITE+"->Blocking damage reduction increases from 50->70%\n"
						+ ChatColor.GRAY+"->When not blocking, you have Regeneration I. Blocking applies Regeneration II.\n"
						+ ChatColor.WHITE+"->Blocking gives 8 health (4 hearts) of Absorption damage.\n"
						+ ChatColor.GRAY+"->When hit while blocking, you build up Resistance, one level per hit, up to Resistance V (lasts 2 seconds)\n"
						+ ChatColor.WHITE+"->While blocking, you absorb 50% of all damage taken by party members.\n"
						+ ChatColor.GRAY+"->Blocking will aggro all nearby mobs to the blocking defender. They will glow indicate the aggro shift.\n"
						+ ChatColor.WHITE+"->Base Health increased by 10 (5 hearts)\n"
						+ ChatColor.GRAY+"->Getting hit as a defender increases saturation.\n"
						+ ChatColor.WHITE+"->Hitting mobs as a Defender aggros them to you.\n"
						+ ChatColor.GRAY+"->Knockback from attacks reduced by 75% while blocking.\n"
						+ ChatColor.WHITE+"- "+ChatColor.BOLD+"Rejuvenation"+ChatColor.RESET+ChatColor.WHITE+"\n"
						+ ChatColor.GRAY+"->Dropping your shield will give you Regeneration X for 10 seconds and 2 seconds of invulnerability. It also costs 400 shield durability!\n"
						;
			}
			case "striker":{
				return ChatColor.RED+""+ChatColor.BOLD+mode+" mode Perks: "+ChatColor.RESET+"\n"
						+ ChatColor.WHITE+"->Players are identified as 'Strikers' when they only carry a sword in their main hand. No off-hand items.\n"
						+ ChatColor.GRAY+"->10% passive damage increase.\n"
						+ ChatColor.WHITE+"->20% chance to critically strike.\n"
						+ ChatColor.GRAY+"->Every 10% of missing health increases your damage by 10%. (Ex. 99% damage increase at 99% lost hp.)\n"
						+ ChatColor.WHITE+"->Getting hit increases Speed by 1 Level. Stacks up to Speed V (Lasts five seconds.)\n"
						+ ChatColor.GRAY+"->Swinging your weapon stops nearby flying arrows. Each arrow deflected will give you a Strength buff. Stacks up to Strength V (Lasts five seconds.)\n"
						+ ChatColor.WHITE+"->Dropping your weapon will perform a line drive. Enemies you charge through take x7 your base damage. This costs 5% of your durability (Unbreaking decreases this amount.)\n"
						+ ChatColor.GRAY+"->Strikers have a 20% chance to dodge incoming attacks from any damage source while moving.\n"
						+ ChatColor.WHITE+"->Hitting a target when they have not noticed you yet does x3 normal damage.\n"
						;
			}
			case "ranger":{
				return ChatColor.DARK_GREEN+""+ChatColor.BOLD+mode+" mode Perks: "+ChatColor.RESET+"\n"
						+ ChatColor.WHITE+"->Players are identified as 'Rangers' when they carry a bow in their main hand. Off-hand items are permitted, except for a shield. Can only be wearing leather armor, or no armor.\n"
						+ ChatColor.GRAY+"->Left-clicking mobs will cause them to be knocked back extremely far, basically in headshot range, when walls permit.\n"
						+ ChatColor.WHITE+"->Base Arrow Damage increases from x1->x2.\n"
						+ ChatColor.GRAY+"->You can dodge 50% of all incoming attacks from any damage sources.\n"
						+ ChatColor.WHITE+"You have immunity to all Thorns damage.\n"
						+ ChatColor.GRAY+"Shift-Right Click to change Bow Modes.\n"
						+ ChatColor.WHITE+"- "+ChatColor.BOLD+"Close Range Mode (Default):"+ChatColor.RESET+ChatColor.WHITE+" \n"
						+ ChatColor.GRAY+"  You gain the ability to deal headshots from any distance, even directly onto an enemy's face. Each kill made in this mode gives you 100% dodge chance for the next hit taken. You can tumble and gain invulnerability for 1 second by dropping your bow. Sneak while dropping it to tumble backwards.\n"
						+ ChatColor.WHITE+"- "+ChatColor.BOLD+"Sniping Mode:"+ChatColor.RESET+ChatColor.WHITE+" \n"
						+ ChatColor.GRAY+"  Headshot collision area increases by x3. Headshots will deal an extra x0.25 damage for each headshot landed, up to a cap of 8 stacks. Each stack also increases your Slowness level by 1.\n"
						+ ChatColor.WHITE+"  Arrows are lightning-fast in Sniping Mode.\n"
						+ ChatColor.GRAY+"- "+ChatColor.BOLD+"Debilitation Mode:"+ChatColor.RESET+ChatColor.WHITE+" \n"
						+ ChatColor.WHITE+"  Adds a stack of Poison when hitting non-poisoned targets (20 second duration). Hitting mobs in this mode refreshes the duration of the poison stacks. Headshots made in this mode will increase the level of Poison on the mob, making the mob more and more vulnerable.\n"
						+ ChatColor.GRAY+"  Headshots also remove one level of a buff (does not affect debuffs) applied to the mob at random.\n"
						;
			}
			default:{
				return "This mode either does not exist or has no perks!";
			}
		}
	}
	
	public static boolean holdingNoShield(Player p) {
		return p.getInventory().getExtraContents()[0]==null;
	}
	
	public static boolean isRareItem(ItemStack it) {
		if (it!=null &&
				it.getType()!=Material.AIR &&
				it.hasItemMeta() &&
				it.getItemMeta().hasDisplayName() && 
				it.getItemMeta().hasLore()
						) {
			TwosideKeeper.log("Returning it!", 5);
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isBossMonster(Monster m) {
		if (MonsterController.isZombieLeader(m) ||
			(m.getType()==EntityType.GUARDIAN &&
			((Guardian)m).isElder()) ||
			m.getType()==EntityType.ENDER_DRAGON ||
			m.getType()==EntityType.WITHER) {
				return true;
			} else {
				return false;
			}
	}
	
	public static boolean isCoreMonster(Monster m) {
		if (m.getType()==EntityType.GUARDIAN ||
			m.getType()==EntityType.SKELETON) {
			if (m.getType()==EntityType.SKELETON) {
				Skeleton s = (Skeleton)m;
				if (s.getSkeletonType()==SkeletonType.WITHER) {
					return true;
				}
			} else {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isEdible(ItemStack it) {
		if (it.getType()==Material.GOLDEN_CARROT ||
			it.getType()==Material.GOLDEN_APPLE ||
			it.getType()==Material.GRILLED_PORK ||
			it.getType()==Material.COOKED_BEEF ||
			it.getType()==Material.COOKED_MUTTON ||
			it.getType()==Material.COOKED_FISH ||
			it.getType()==Material.SPIDER_EYE ||
			it.getType()==Material.CARROT_ITEM ||
			it.getType()==Material.BAKED_POTATO ||
			it.getType()==Material.COOKED_CHICKEN ||
			it.getType()==Material.COOKED_RABBIT ||
			it.getType()==Material.RABBIT_STEW ||
			it.getType()==Material.MUSHROOM_SOUP ||
			it.getType()==Material.BREAD ||
			it.getType()==Material.RAW_FISH ||
			it.getType()==Material.BEETROOT ||
			it.getType()==Material.BEETROOT_SOUP ||
			it.getType()==Material.PUMPKIN_PIE ||
			it.getType()==Material.APPLE ||
			it.getType()==Material.RAW_BEEF ||
			it.getType()==Material.PORK ||
			it.getType()==Material.MUTTON ||
			it.getType()==Material.RAW_CHICKEN ||
			it.getType()==Material.RABBIT ||
			it.getType()==Material.POISONOUS_POTATO ||
			it.getType()==Material.MELON ||
			it.getType()==Material.POTATO ||
			it.getType()==Material.CHORUS_FRUIT ||
			it.getType()==Material.COOKIE ||
			it.getType()==Material.ROTTEN_FLESH ||
			it.getType()==Material.RAW_FISH
		) {
			return true;
		} else {
			return false;
		}
	}
	
	
	public static boolean hasNoLore(ItemStack item) {
		if (item!=null &&
				item.getType()!=Material.AIR &&
				item.hasItemMeta() &&
				item.getItemMeta().hasLore()) {
			return false;
		} else {
			return true;
		}
	}
	
	public static boolean isDumpableContainer(Material mat) {
		if (mat==Material.CHEST ||
				mat==Material.TRAPPED_CHEST) {
			return true;
		}
		return false;
	}
	
	public static ItemStack moveItemStack(ItemStack item, Inventory target) {
		//First see if there are any incomplete stacks in the target inventory.
		if (item!=null &&
				item.getType()!=Material.AIR) {
			for (int i=0;i<target.getSize();i++) {
				ItemStack targetitem = target.getItem(i);
				if (targetitem!=null &&
						targetitem.getType()!=Material.AIR &&
						item.isSimilar(targetitem) &&
						targetitem.getAmount()<targetitem.getMaxStackSize()) {
					//We have some room!
					int space = targetitem.getMaxStackSize()-targetitem.getAmount();
					if (item.getAmount()<space) {
						targetitem.setAmount(targetitem.getAmount()+item.getAmount());
						//That means we are done!
						return new ItemStack(Material.AIR);
					} else {
						//Insert what we can. Handle the rest elsewhere.
						targetitem.setAmount(targetitem.getMaxStackSize());
						item.setAmount(item.getAmount()-space);
					}
				} else
				if (targetitem==null) {
					//This is an empty spot. Insert the item here.
					int space = item.getMaxStackSize();
					if (item.getAmount()<space) {
						ItemStack newslot = item.clone();
						target.setItem(i, newslot);
						return new ItemStack(Material.AIR);
					} else {
						//Insert what we can. Handle the rest elsewhere.
						ItemStack newslot = item.clone();
						newslot.setAmount(item.getMaxStackSize());
						target.setItem(i, newslot);
						item.setAmount(item.getAmount()-space);
					}
				} else if (targetitem.getType()==Material.AIR)
				{
					//This is an empty spot. Insert the item here.
					int space = item.getMaxStackSize();
					if (item.getAmount()<space) {
						ItemStack newslot = item.clone();
						target.setItem(i, newslot);
						return new ItemStack(Material.AIR);
					} else {
						//Insert what we can. Handle the rest elsewhere.
						ItemStack newslot = item.clone();
						newslot.setAmount(item.getMaxStackSize());
						target.setItem(i, newslot);
						item.setAmount(item.getAmount()-space);
					}
				}
			}
			return item;
		} else {
			return new ItemStack(Material.AIR);
		}
	}
	
	public static int CalculateSlot(ItemStack item, Player p) {
		//Check all equipment slots for this item.
		for (int i=0;i<p.getInventory().getSize();i++) {
			TwosideKeeper.log("Checking item slot "+i, 5);
			if (p.getInventory().getItem(i)!=null && p.getInventory().getItem(i).equals(item)) {
				TwosideKeeper.log("Found item in slot "+i, 5);
				return i;
			}
		}
		
		//It might be in the armor slot.
		for (int i=0;i<p.getEquipment().getArmorContents().length;i++) {
			TwosideKeeper.log("Checking armor slot "+i, 5);
			if (p.getEquipment().getArmorContents()[i]!=null && p.getEquipment().getArmorContents().equals(item)) {
				TwosideKeeper.log("Found item in slot "+(i+900), 5);
				return i+900;
			}
		}
		return -1;
	}
	
	public static boolean isBadEffect(PotionEffectType pet) {
		if (pet.equals(PotionEffectType.BLINDNESS) ||
				pet.equals(PotionEffectType.CONFUSION) ||
				pet.equals(PotionEffectType.HARM) ||
				pet.equals(PotionEffectType.HUNGER) ||
				pet.equals(PotionEffectType.POISON) ||
				pet.equals(PotionEffectType.SLOW) ||
				pet.equals(PotionEffectType.SLOW_DIGGING) ||
				pet.equals(PotionEffectType.UNLUCK) ||
				pet.equals(PotionEffectType.WITHER)) {
			return true;
		} else {
			return false;
		}
	}
	
	public static int CountDebuffs(Player p) {
		int debuffcount=0;
		for (int i1=0;i1<p.getActivePotionEffects().size();i1++) {
			if (isBadEffect(Iterables.get(p.getActivePotionEffects(), i1).getType())) {
				debuffcount++;
			}
		}
		return debuffcount;
	}
	
	public static void produceError(int errorCode, CommandSender sender) {
		String ErrorMessage = ChatColor.RED+"(ERRCODE "+errorCode+") A Fatal Error has occured! "+ChatColor.WHITE+"Please let the server administrator know about this.";
		sender.sendMessage(ErrorMessage);
		TwosideKeeper.log(ErrorMessage, 1);
	}
	
	public static ChatColor getDeathMarkColor(int stacks) {
		if (stacks<3) {
			return ChatColor.DARK_GREEN;
		} else
		if (stacks<6) {
			return ChatColor.GREEN;
		} else
		if (stacks<10) {
			return ChatColor.YELLOW;
		} else
		if (stacks<15) {
			return ChatColor.GOLD;
		} else
		if (stacks<20) {
			return ChatColor.RED;
		} else
		if (stacks<30) {
			return ChatColor.DARK_RED;
		} else {
			return ChatColor.DARK_GRAY;
		}
	}
	
	public static void ApplyDeathMark(LivingEntity ent) {
		int stackamt = 0;
		if (ent.hasPotionEffect(PotionEffectType.UNLUCK)) {
			//Add to the current stack of unluck.
			for (int i1=0;i1<ent.getActivePotionEffects().size();i1++) {
				if (Iterables.get(ent.getActivePotionEffects(), i1).getType().equals(PotionEffectType.UNLUCK)) {
					int lv = Iterables.get(ent.getActivePotionEffects(), i1).getAmplifier();
					ent.removePotionEffect(PotionEffectType.UNLUCK);
					TwosideKeeper.log("Death mark stack is now T"+(lv+1), 5);
					stackamt=lv+2;
					ent.addPotionEffect(new PotionEffect(PotionEffectType.UNLUCK,99,lv+1));
					break;
				}
			}
		} else {
			ent.removePotionEffect(PotionEffectType.UNLUCK);
			TwosideKeeper.log("Death mark stack is now T1", 5);
			ent.addPotionEffect(new PotionEffect(PotionEffectType.UNLUCK,99,0));
			stackamt=1;
		}
		//Modify the color of the name of the monster.
		if (ent instanceof Monster) {
			Monster m = (Monster)ent;
			m.setCustomNameVisible(true);
			if (m.getCustomName()!=null) {
				m.setCustomName(getDeathMarkColor(stackamt)+ChatColor.stripColor(m.getCustomName()));
			} else {
				m.setCustomName(getDeathMarkColor(stackamt)+CapitalizeFirstLetters(m.getType().toString().replace("_", " ")));
			}
		}
	}
	
	public static int GetDeathMarkAmt(LivingEntity ent) {
		if (ent.hasPotionEffect(PotionEffectType.UNLUCK)) {
			//Add to the current stack of unluck.
			for (int i1=0;i1<ent.getActivePotionEffects().size();i1++) {
				if (Iterables.get(ent.getActivePotionEffects(), i1).getType().equals(PotionEffectType.UNLUCK)) {
					return Iterables.get(ent.getActivePotionEffects(), i1).getAmplifier()+1;
				}
			}
		}
		return 0;
	}
	
	public static void ResetMobName(LivingEntity ent) {
		if (ent instanceof Monster) {
			Monster m = (Monster)ent;
			m.setCustomNameVisible(false);
			if (m.getCustomName()!=null) {
			m.setCustomName(ChatColor.stripColor(m.getCustomName()));
				if (m.getCustomName().contains("Dangerous")) {
					m.setCustomName(ChatColor.DARK_AQUA+m.getCustomName());
				}
				if (m.getCustomName().contains("Deadly")) {
					m.setCustomName(ChatColor.GOLD+m.getCustomName());
				}
				if (m.getCustomName().contains("Hellfire")) {
					m.setCustomName(ChatColor.DARK_RED+m.getCustomName());
				}
			}
		}
	}
	
	public static ItemStack RemovePermEnchantmentChance(ItemStack item, Player p) {
		if (item!=null &&
				item.getType()!=Material.AIR) {
			int mendinglv = item.getEnchantmentLevel(Enchantment.MENDING);
			int infinitylv = item.getEnchantmentLevel(Enchantment.ARROW_INFINITE);
			if (mendinglv>0 && Math.random()<=0.00048828125*(isHarvestingTool(item)?0.75:1d)) {
				mendinglv--;
				if (mendinglv>0) {
					item.addUnsafeEnchantment(Enchantment.MENDING, mendinglv);
				} else {
					item.removeEnchantment(Enchantment.MENDING);
				}
				p.sendMessage(ChatColor.DARK_AQUA+"A level of "+ChatColor.YELLOW+"Mending"+ChatColor.DARK_AQUA+" has been knocked off of your "+((item.hasItemMeta() && item.getItemMeta().hasDisplayName())?item.getItemMeta().getDisplayName():UserFriendlyMaterialName(item)));
			}
			if (infinitylv>0 && Math.random()<=0.00048828125*(isHarvestingTool(item)?0.75:1d)) {
				infinitylv--;
				if (infinitylv>0) {
					item.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, infinitylv);
				} else {
					item.removeEnchantment(Enchantment.ARROW_INFINITE);
				}
				p.sendMessage(ChatColor.DARK_AQUA+"A level of "+ChatColor.YELLOW+"Infinity"+ChatColor.DARK_AQUA+" has been knocked off of your "+((item.hasItemMeta() && item.getItemMeta().hasDisplayName())?item.getItemMeta().getDisplayName():UserFriendlyMaterialName(item)));
			}
		}
		return item;
	}
	
	public static boolean HasFullRangerSet(Player p) {

		int rangerarmort1 = 0; //Count the number of each tier of sets.
		int rangerarmort2 = 0;
		int rangerarmort3 = 0;
		int rangerarmort4 = 0;
		
		if (isRanger(p)) {
			for (int i=0;i<p.getEquipment().getArmorContents().length;i++) {
				ItemStack equip = p.getEquipment().getArmorContents()[i];
				if (equip!=null
						&& equip.getType()!=Material.AIR &&
						equip.hasItemMeta() && equip.getItemMeta().hasLore()) {
					if (equip.getItemMeta().getLore().contains(ChatColor.GOLD+""+ChatColor.BOLD+"Jamdak Set")) {
						rangerarmort1++;
					} else
					if (equip.getItemMeta().getLore().contains(ChatColor.GOLD+""+ChatColor.BOLD+"Darnys Set")) {
						rangerarmort2++;
					} else
					if (equip.getItemMeta().getLore().contains(ChatColor.GOLD+""+ChatColor.BOLD+"Alikahn Set")) {
						rangerarmort3++;
					} else
					if (equip.getItemMeta().getLore().contains(ChatColor.GOLD+""+ChatColor.BOLD+"Lorasaadi Set")) {
						rangerarmort4++;
					}
				}
			}
		}
		
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		
		if (rangerarmort1==4 || rangerarmort2==4 || rangerarmort3==4 || rangerarmort4==4) {
			//Player has the full set.
			pd.hasfullrangerset=true;
		} else {
			pd.hasfullrangerset=false;
		}
		
		return pd.hasfullrangerset;
	}
	
	public static ItemStack applyModeName(ItemStack item) {
		if (item!=null &&
				item.getType()!=Material.AIR &&
				item.hasItemMeta()) {
			ItemMeta m = item.getItemMeta();
			if (m.hasDisplayName()) {
				String name = m.getDisplayName();
				if (name.contains(" Mode)"+ChatColor.WHITE) && name.contains(ChatColor.GREEN+"(")) {
					String newname = name.split(ChatColor.GREEN+"\\(")[0]+ChatColor.GREEN+"("+CapitalizeFirstLetters(getBowMode(item).GetCoolName())+" Mode)"+ChatColor.WHITE;
					m.setDisplayName(newname);
					item.setItemMeta(m);
					return item;
				} else {
					String newname = name+" "+ChatColor.GREEN+"("+CapitalizeFirstLetters(getBowMode(item).name())+" Mode)"+ChatColor.WHITE;
					m.setDisplayName(newname);
					item.setItemMeta(m);
					return item;
				}
			} else {
				String newname = UserFriendlyMaterialName(item)+" "+ChatColor.GREEN+"("+CapitalizeFirstLetters(getBowMode(item).GetCoolName())+" Mode)"+ChatColor.WHITE;
				m.setDisplayName(newname);
				item.setItemMeta(m);
				return item;
			}
		}
		ItemMeta m = item.getItemMeta();
		String newname = UserFriendlyMaterialName(item)+" "+ChatColor.GREEN+"("+CapitalizeFirstLetters(getBowMode(item).GetCoolName())+" Mode)"+ChatColor.WHITE;
		m.setDisplayName(newname);
		item.setItemMeta(m);
		return item;
	}
	
	public static BowMode getBowMode(ItemStack item) {
		if (item!=null &&
				item.getType()!=Material.AIR &&
				item.hasItemMeta()) {
			if (!item.getItemMeta().hasLore()) {
				return BowMode.CLOSE; //The default.
			} else {
				ItemMeta m = item.getItemMeta();
				List<String> oldlore = m.getLore();
				if (oldlore.size()>=1 && oldlore.get(0).contains(ChatColor.MAGIC+" BM")) {
					String secondpart = oldlore.get(0).split(ChatColor.MAGIC+" BM")[1];
					return BowMode.valueOf(secondpart);
				} else {
					return BowMode.CLOSE; //The default.
				}
			}
		} else {
			return BowMode.CLOSE;
		}
	}
	
	public static ItemStack setBowMode(ItemStack item, BowMode mode) {
		if (item!=null &&
				item.getType()!=Material.AIR &&
				item.hasItemMeta()) {
			ItemMeta m = item.getItemMeta();
			if (m.hasLore()) {
				List<String> oldlore = m.getLore();
				if (oldlore.size()>=1) {
					if (oldlore.get(0).contains(ChatColor.MAGIC+" BM")) {
						oldlore.set(0, oldlore.get(0).split(ChatColor.MAGIC+" BM")[0]+ChatColor.MAGIC+" BM"+mode.name());
					} else {
						oldlore.set(0, oldlore.get(0)+ChatColor.MAGIC+" BM"+mode.name());
					}
				} else {
					oldlore.add(ChatColor.MAGIC+" BM"+mode.name());
				}
				m.setLore(oldlore);
				item.setItemMeta(m);
			} else {
				List<String> newlore = new ArrayList<String>();
				newlore.add(ChatColor.MAGIC+" BM"+mode.name());
				m.setLore(newlore);
				item.setItemMeta(m);
			}
		}
		return item;
	}
	
	public static void AutoRepairItems(Player p) {
		for (int i=0;i<9;i++) {
			if (ArtifactAbility.containsEnchantment(ArtifactAbility.AUTOREPAIR, p.getInventory().getItem(i))) {
				//Chance to auto repair.
				double repairamt = ArtifactAbility.calculateValue(ArtifactAbility.AUTOREPAIR, p.getInventory().getItem(i).getEnchantmentLevel(Enchantment.LUCK), ArtifactAbility.getEnchantmentLevel(ArtifactAbility.AUTOREPAIR, p.getInventory().getItem(i)));
				if (Math.random() <= repairamt%1) {
					repairamt++;
				}
				if (p.getLocation().getY()>=0 && p.getLocation().getY()<=255 && p.getLocation().getBlock().getLightFromSky()==0) {
					repairamt/=2.0d;
					//TwosideKeeper.log("In Darkness.",2);
				}
				double chance = 1;
				if (Math.random()<=chance/100d) { 
					if (p.getInventory().getItem(i).getDurability()-repairamt<0) {
						p.getInventory().getItem(i).setDurability((short)0);
						TwosideKeeper.log("Repaired "+p.getInventory().getItem(i).toString()+" to full durability.", 5);
					} else {
						p.getInventory().getItem(i).setDurability((short)(p.getInventory().getItem(i).getDurability()-repairamt));
						TwosideKeeper.log("Repaired "+repairamt+" durability to "+p.getInventory().getItem(i).toString()+"", 5);
					}
				}
			}
		}
		ItemStack[] contents = {p.getEquipment().getHelmet(),p.getEquipment().getChestplate(),p.getEquipment().getLeggings(),p.getEquipment().getBoots()};
		for (int i=0;i<contents.length;i++) {
			ItemStack equip = contents[i];
			if (ArtifactAbility.containsEnchantment(ArtifactAbility.AUTOREPAIR, equip)) {
				//Chance to auto repair.
				double repairamt = ArtifactAbility.calculateValue(ArtifactAbility.AUTOREPAIR, equip.getEnchantmentLevel(Enchantment.LUCK), ArtifactAbility.getEnchantmentLevel(ArtifactAbility.AUTOREPAIR, equip));
				if (Math.random() <= repairamt%1) {
					repairamt++;
				}
				if (p.getLocation().getY()>=0 && p.getLocation().getBlock().getLightFromSky()==0) {
					repairamt/=2.0d;
					//TwosideKeeper.log("In Darkness.",2);
				}
				double chance = 1;
				if (Math.random()<=chance/100d) {
					if (equip.getDurability()-repairamt<0) {
						equip.setDurability((short)0);
						TwosideKeeper.log("Repaired "+equip.toString()+" to full durability.", 5);
					} else {
						contents[i].setDurability((short)(equip.getDurability()-repairamt));
						TwosideKeeper.log("Repaired "+repairamt+" durability to "+equip.toString()+"", 5);
					}
				}
			}
		}
	}
	
	@Deprecated
	public static void DealDamageToMob(double dmg, LivingEntity target, LivingEntity damager, boolean truedmg) {
		DealDamageToMob(dmg,target,damager,null,"");
	} 


	public static void DealDamageToMob(double dmg, LivingEntity target, Entity damager) {
		DealDamageToMob(dmg,target,NewCombat.getDamagerEntity(damager),null,"");
	}
	
	public static void DealDamageToMob(double dmg, LivingEntity target, Entity damager, ItemStack artifact) {
		DealDamageToMob(dmg,target,damager,artifact,"");
	}
	
	public static void DealDamageToMob(double dmg, LivingEntity target, Entity damager, ItemStack artifact, String reason) {
		LivingEntity shooter = NewCombat.getDamagerEntity(damager);
		if (enoughTicksHavePassed(target,shooter)) {
			if (damager!=null && (target instanceof Monster)) {
				Monster m = (Monster)target;
				if (damager instanceof Player) {
					NewCombat.addMonsterToTargetList(m, (Player)damager);
				}
	    		TwosideKeeper.habitat_data.addNewStartingLocation(target);
			}
			aPlugin.API.sendEntityHurtAnimation(target);
			TwosideKeeper.log("Call event with "+dmg, 5);
			if (shooter!=null) {
				if (!(shooter instanceof Monster) || !(target instanceof Monster)) {
					TwosideKeeper.log(GenericFunctions.GetEntityDisplayName(shooter)+"->"+
						GenericFunctions.GetEntityDisplayName(target)+ChatColor.WHITE+": Damage dealt was "+dmg,2);
				}
			} else {
				if (!(target instanceof Monster)) {
					TwosideKeeper.log(reason+"->"+
						GenericFunctions.GetEntityDisplayName(target)+ChatColor.WHITE+": Damage dealt was "+dmg,2);
				}
			}
			double oldhp=((LivingEntity)target).getHealth();
			LivingEntity le = NewCombat.getDamagerEntity(damager);
			if (le!=null) {
				GenericFunctions.subtractHealth(target, le, dmg, artifact);
				if (artifact!=null &&
						GenericFunctions.isArtifactEquip(artifact) &&
						(le instanceof Player)) {
					Player p = (Player)le;
					double ratio = 1.0-NewCombat.CalculateDamageReduction(1,target,p);
					AwakenedArtifact.addPotentialEXP(le.getEquipment().getItemInMainHand(), (int)((ratio*20)+5), p);
					NewCombat.increaseArtifactArmorXP(p,(int)(ratio*10)+1);
				}		
	
				if (le instanceof Player) {
					Player p = (Player)le;
					if (GenericFunctions.isEquip(p.getEquipment().getItemInMainHand())) {
						aPlugin.API.damageItem(p, p.getEquipment().getItemInMainHand(), 1);
					}
					knockOffGreed(p);
				}
			}
			
			TwosideKeeper.log(ChatColor.BLUE+"  "+oldhp+"->"+((LivingEntity)target).getHealth()+" HP",3);
		}
	 }
	
	public static void knockOffGreed(Player p) {
		// Chance: (11-tier)*5
		//Check for artifacts on all equips.
		boolean brokeone = false;
		for (int i=0;i<p.getEquipment().getArmorContents().length;i++) {
			ItemStack item = p.getEquipment().getArmorContents()[i];
			if (isArtifactEquip(item) &&
					ArtifactAbility.containsEnchantment(ArtifactAbility.GREED, item)) {
					TwosideKeeper.log("Found one.",2);
					int tier = item.getEnchantmentLevel(Enchantment.LUCK);
					item = ArtifactAbility.downgradeEnchantment(p, item, ArtifactAbility.GREED);
					if (Math.random()<=((11-tier)*5)/100d) {p.sendMessage(ChatColor.DARK_AQUA+"A level of "+ChatColor.YELLOW+"Greed"+ChatColor.DARK_AQUA+" has been knocked off of your "+((item.hasItemMeta() && item.getItemMeta().hasDisplayName())?item.getItemMeta().getDisplayName():UserFriendlyMaterialName(item)));
					brokeone=true;
					break;
				}
			}
		}
		if (!brokeone) {
			//Try the main hand.
			ItemStack item = p.getEquipment().getItemInMainHand();
			if (isArtifactEquip(item) &&
					ArtifactAbility.containsEnchantment(ArtifactAbility.GREED, item)) {
					int tier = item.getEnchantmentLevel(Enchantment.LUCK);
					item = ArtifactAbility.downgradeEnchantment(p, item, ArtifactAbility.GREED);
					if (Math.random()<=((11-tier)*5)/100d) {p.sendMessage(ChatColor.DARK_AQUA+"A level of "+ChatColor.YELLOW+"Greed"+ChatColor.DARK_AQUA+" has been knocked off of your "+((item.hasItemMeta() && item.getItemMeta().hasDisplayName())?item.getItemMeta().getDisplayName():UserFriendlyMaterialName(item)));
					brokeone=true;
				}
			}
		}
	}

	public static boolean searchfor(List<String> stringy, String searchfor) {
		for (int i=0;i<stringy.size();i++) {
			if (stringy.get(i).contains(searchfor)) {
				return true;
			}
		}
		return false;
	}
	
	public static int getPotionEffectLevel(PotionEffectType type, LivingEntity ent) {
		if (ent.hasPotionEffect(type)) {
			for (int j=0;j<ent.getActivePotionEffects().size();j++) {
				if (Iterables.get(ent.getActivePotionEffects(), j).getType().equals(type)) {
					//Get the level.
					return Iterables.get(ent.getActivePotionEffects(), j).getAmplifier();
				}
			}
			TwosideKeeper.log("Something went wrong while getting potion effect level of "+type+" for Entity "+ent.getName()+"!", 1);
			return -1;
		} else {
			return -1;
		}
	}

	public static int getPotionEffectDuration(PotionEffectType type, LivingEntity ent) {
		if (ent.hasPotionEffect(type)) {
			for (int j=0;j<ent.getActivePotionEffects().size();j++) {
				if (Iterables.get(ent.getActivePotionEffects(), j).getType().equals(type)) {
					//Get the level.
					return Iterables.get(ent.getActivePotionEffects(), j).getDuration();
				}
			}
			TwosideKeeper.log("Something went wrong while getting potion effect duration of "+type+" for Entity "+ent.getName()+"!", 1);
			return -1;
		} else {
			return -1;
		}
	}

	public static void PerformDodge(Player p) {
		if (p.isOnGround() && GenericFunctions.isRanger(p) &&
				(GenericFunctions.getBowMode(p.getEquipment().getItemInMainHand())==BowMode.CLOSE)) {
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			if (pd.last_dodge+TwosideKeeper.DODGE_COOLDOWN<=TwosideKeeper.getServerTickTime()) {
				pd.last_dodge=TwosideKeeper.getServerTickTime();
				aPlugin.API.sendCooldownPacket(p, p.getEquipment().getItemInMainHand(), 100);
				aPlugin.API.sendCooldownPacket(p, p.getEquipment().getItemInMainHand(), 100);
				p.playSound(p.getLocation(), Sound.ENTITY_DONKEY_CHEST, 1.0f, 1.0f);
				
				int dodgeduration = 20;
				
				if (GenericFunctions.HasFullRangerSet(p)) {
					dodgeduration=60;
				}
				
				if (p.isSneaking()) { //Do a backwards dodge + jump.
					p.setVelocity(p.getLocation().getDirection().multiply(-0.7f));
				} else {
					p.setVelocity(p.getLocation().getDirection().multiply(1.4f));
				}
				
				p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION,dodgeduration,0));
				p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,dodgeduration,2));
				p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING,dodgeduration,0));
			}
		}
	}
	
	public static String GetEntityDisplayName(Entity e) {
		if (e instanceof LivingEntity) {
			LivingEntity l = (LivingEntity)e;
			if (l.getCustomName()!=null) { 
				return l.getCustomName();
			}
			if (l instanceof Player) {
				Player p = (Player)l;
				return p.getName();
			}
		}
		if (e instanceof Projectile) {
			Projectile proj = (Projectile)e;
			String finalname = CapitalizeFirstLetters(proj.getType().name().replace("_", " "));
			if (proj.getShooter() instanceof LivingEntity) {
				LivingEntity l = (LivingEntity)proj.getShooter();
				if (l.getCustomName()!=null) { 
					return finalname+"("+l.getCustomName()+ChatColor.GRAY+")";
				}
				if (l instanceof Player) {
					Player p = (Player)l;
					return finalname+"("+p.getName()+ChatColor.GRAY+")";
				}
			}
		}
		return e.getType().name()+ChatColor.WHITE;
	}
	
	//Returns player velocity in m/sec.
	public static double GetPlayerVelocity(Player p) {
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		return 93.182445*pd.velocity;
	}

	public static double getAbilityValue(ArtifactAbility ab, ItemStack weapon) {
		return ArtifactAbility.calculateValue(ab, weapon.getEnchantmentLevel(Enchantment.LUCK), ArtifactAbility.getEnchantmentLevel(ab, weapon));
	}
	public static void subtractHealth(LivingEntity entity, LivingEntity damager, double dmg) {
		subtractHealth(entity,damager,dmg,null);
	}	
			
	public static void subtractHealth(LivingEntity entity, LivingEntity damager, double dmg, ItemStack artifact) {
		entity.setLastDamage(0);
		entity.setNoDamageTicks(0);
		entity.setMaximumNoDamageTicks(0);
		boolean hitallowed=enoughTicksHavePassed(entity,damager);
		if (hitallowed) {
			updateNoDamageTickMap(entity,damager);
			if (damager instanceof Player) {
				Player p = (Player)damager;
				
				TwosideKeeper.log("Damage goes from "+dmg+"->"+(dmg+TwosideKeeper.CUSTOM_DAMAGE_IDENTIFIER),5);
				entity.damage(dmg+TwosideKeeper.CUSTOM_DAMAGE_IDENTIFIER,damager);
				aPlugin.API.showDamage(entity, GetHeartAmount(dmg));
				
				PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
				if (pd.damagelogging) { 
					pd.target=entity;
					DecimalFormat df = new DecimalFormat("0.0");
					TwosideKeeper.updateTitle(p,ChatColor.AQUA+df.format(dmg));
					TwosideKeeper.log("In here",2);
				} else {
					pd.target=entity; 
					TwosideKeeper.updateTitle(p);
				}
				//Bukkit.getPluginManager().callEvent(new EntityDamageByEntityEvent(damager,entity,DamageCause.CUSTOM,dmg+TwosideKeeper.CUSTOM_DAMAGE_IDENTIFIER));
			} else {
				if (entity instanceof Player) {
		    		double dodgechance = NewCombat.CalculateDodgeChance((Player)entity);
		    		Player p = (Player)entity;
		    		if (!p.hasPotionEffect(PotionEffectType.GLOWING)) {
			    		if (Math.random()<=dodgechance) {
			    			p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 3.0f, 1.0f);
			    			for (int i=0;i<p.getEquipment().getArmorContents().length;i++) {
			    				ItemStack equip = p.getEquipment().getArmorContents()[i];
			    				if (ArtifactAbility.containsEnchantment(ArtifactAbility.GRACEFULDODGE, equip)) {
			    					p.addPotionEffect(
			    							new PotionEffect(PotionEffectType.GLOWING,
			    									(int)(GenericFunctions.getAbilityValue(ArtifactAbility.GRACEFULDODGE, equip)*20),
			    									0)
			    							);
				    				}
				    			}
			    			p.setNoDamageTicks(10);
		
							
			    		} else {
						//Use old system if we cannot get a valid damager.
						if (entity.getHealth()>dmg && entity instanceof Player) {
									if (!AttemptRevive((Player)entity,dmg)) {
										entity.setHealth(((Player)entity).getHealth()-dmg);
										aPlugin.API.showDamage(entity, GetHeartAmount(dmg));
										aPlugin.API.sendEntityHurtAnimation((Player)entity);
									}
				    			}
						
						 else {
								//List<ItemStack> drops = new ArrayList<ItemStack>();
								//EntityDeathEvent ev = new EntityDeathEvent(entity,drops);
								//Bukkit.getPluginManager().callEvent(ev);
								//entity.setHealth(0);
								if (entity instanceof Player && !AttemptRevive((Player)entity,Integer.MAX_VALUE)) {
									entity.damage(Integer.MAX_VALUE);
								}
							}
			    		}
					}
				} else {
					if (entity.getHealth()>dmg && entity instanceof Player) {
						if (!AttemptRevive((Player)entity,dmg)) {
							entity.setHealth(((Player)entity).getHealth()-dmg);
							aPlugin.API.sendEntityHurtAnimation((Player)entity);
						}
	    			}
					
					 else {
							//List<ItemStack> drops = new ArrayList<ItemStack>();
							//EntityDeathEvent ev = new EntityDeathEvent(entity,drops);
							//Bukkit.getPluginManager().callEvent(ev);
							//entity.setHealth(0);
							if (entity instanceof Player && !AttemptRevive((Player)entity,Integer.MAX_VALUE)) {
								entity.damage(Integer.MAX_VALUE);
						}
					 }
				}
			}
		}
	}

	public static boolean enoughTicksHavePassed(LivingEntity entity, LivingEntity damager) {
		if (entity instanceof Player) {
			Player p = (Player)entity;
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			if (pd.hitlist.containsKey(damager.getUniqueId())) {
				long time = pd.hitlist.get(damager.getUniqueId());
				if (time+10<TwosideKeeper.getServerTickTime()) {
					return true;
				}
			} else {
				return true;
			}
		}
		if (entity instanceof Monster) {
			Monster m = (Monster)entity;
			MonsterStructure md = MonsterStructure.getMonsterStructure(m);
			if (md.hitlist.containsKey(damager.getUniqueId())) {
				long time = md.hitlist.get(damager.getUniqueId());
				if (time+10<TwosideKeeper.getServerTickTime()) {
					return true;
				}
			} else {
				return true;
			}
		}
		return false;
	}

	private static void updateNoDamageTickMap(LivingEntity entity, LivingEntity damager) {
		if (entity instanceof Player) {
			Player p = (Player)entity;
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			pd.hitlist.put(damager.getUniqueId(), TwosideKeeper.getServerTickTime());
		}
		if (entity instanceof Monster) {
			Monster m = (Monster)entity;
			MonsterStructure md = MonsterStructure.getMonsterStructure(m);
			md.hitlist.put(damager.getUniqueId(), TwosideKeeper.getServerTickTime());
		}
	}

	private static int GetHeartAmount(double dmg) {
		int heartcount = 1;
		double dmgamountcopy = dmg;
		while (dmgamountcopy>10) {
			dmgamountcopy/=2;
			heartcount++;
		}
		return heartcount;
	}

	public static boolean isViewingInventory(Player p) {
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		return pd.isViewingInventory;
	}
	
	public static void addIFrame(Player p, int ticks) {
		p.removePotionEffect(PotionEffectType.GLOWING);
		p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING,ticks,0));
		p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION,ticks,0));
	}

	public static void PerformRejuvenate(Player player) {
		PlayerStructure pd = (PlayerStructure)TwosideKeeper.playerdata.get(player.getUniqueId());
		if (pd.last_rejuvenate+TwosideKeeper.REJUVENATE_COOLDOWN<=TwosideKeeper.getServerTickTime()) {
			player.playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_VILLAGER_CURE, 1.0f, 1.0f);
			addIFrame(player,40);
			player.removePotionEffect(PotionEffectType.REGENERATION);
			player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,200,9));
			aPlugin.API.sendCooldownPacket(player, player.getEquipment().getItemInMainHand(), TwosideKeeper.REJUVENATE_COOLDOWN);
			aPlugin.API.sendCooldownPacket(player, player.getEquipment().getItemInMainHand(), TwosideKeeper.REJUVENATE_COOLDOWN);
		}
	}
	
	public static boolean isArmoredMob(Monster m) {
		if (m.getType()==EntityType.ZOMBIE ||
				m.getType()==EntityType.PIG_ZOMBIE ||
				m.getType()==EntityType.SKELETON) {
			return true;
		}
		return false;
	}
	
	public static boolean isSoftBlock(Block b) {
		if (b.getType()==Material.SAND ||
				b.getType()==Material.DIRT ||
				b.getType()==Material.GRASS ||
				b.getType()==Material.GRAVEL ||
				b.getType()==Material.CLAY ||
				b.getType()==Material.SOIL ||
				b.getType()==Material.SNOW ||
				b.getType()==Material.SOUL_SAND) {
			return true;
		} else { 
			return false;
		}
	}
	
	public static boolean isBankSign(Sign s) {
		return s.getLine(0).equalsIgnoreCase(ChatColor.AQUA+"-- BANK --");
	}

	public static boolean hasPermissionToBreakSign(Sign s, Player p) {
		String[] lines = s.getLines();
		if (WorldShop.isWorldShopSign(s)) {
			WorldShop shop = TwosideKeeper.TwosideShops.LoadWorldShopData(s);
			if (shop.GetOwner().equalsIgnoreCase(p.getName()) || p.isOp()) {
				return true;
			} else {
				return false;
			}
		} else
		if (GenericFunctions.isBankSign(s) && !p.isOp()) {
			return false;
		} else {
			return true;
		}
	}
	
	public static boolean containsLore(ItemStack item) {
		if (item!=null &&
				item.hasItemMeta() &&
				item.getItemMeta().hasLore()) {
			return true;
		}
		return false;
	}

	public static ItemStack[] getEquipment(LivingEntity ent) {
		return new ItemStack[]{
				ent.getEquipment().getItemInMainHand(),
				ent.getEquipment().getHelmet(),
				ent.getEquipment().getChestplate(),
				ent.getEquipment().getLeggings(),
				ent.getEquipment().getBoots()
			};
	}

	public static void updateSetItems(Player player) {
		TwosideKeeper.log("Inventory is size "+player.getInventory().getSize(),2);
		for (int i=0;i<player.getInventory().getSize();i++) {
			if (ItemSet.isSetItem(player.getInventory().getItem(i))) {
				//Update the lore. See if it's hardened. If it is, we will save just that piece.
				//Save the tier and type as well.
				ItemSet set = ItemSet.GetSet(player.getInventory().getItem(i));
				int tier = ItemSet.GetTier(player.getInventory().getItem(i));
				
				List<String> newlore = new ArrayList<String>();
				
				if (GenericFunctions.isHardenedItem(player.getInventory().getItem(i))) {
					newlore.add(ChatColor.GRAY+"Breaks Remaining: "+ChatColor.YELLOW+GenericFunctions.getHardenedItemBreaks(player.getInventory().getItem(i)));
				}
				newlore.addAll(ItemSet.GenerateLore(set, tier));
				ItemMeta m = player.getInventory().getItem(i).getItemMeta();
				m.setLore(newlore);
				player.getInventory().getItem(i).setItemMeta(m);
			}
		}
	}
	
	public static void spawnXP(Location location, int expAmount) {
        ExperienceOrb orb = location.getWorld().spawn(location, ExperienceOrb.class);
        orb.setExperience(orb.getExperience() + expAmount);
    }

	public static boolean AttemptRevive(Player p, double dmg) {
		boolean revived=false;
		if (p.getHealth()<=dmg) {
			//This means we would die from this attack. Attempt to revive the player.
			//Check all artifact armor for a perk.
			ItemStack[] equips = p.getEquipment().getArmorContents();
			for (int i=0;i<equips.length;i++) {
				if (isArtifactEquip(equips[i]) && ArtifactAbility.containsEnchantment(ArtifactAbility.SURVIVOR, equips[i])) {
					//We can revive!
					RevivePlayer(p, p.getMaxHealth()*(getAbilityValue(ArtifactAbility.SURVIVOR,equips[i])/100d));
					ArtifactAbility.removeEnchantment(ArtifactAbility.SURVIVOR, equips[i]);
					revived=true;
					Bukkit.broadcastMessage(ChatColor.GOLD+p.getName()+ChatColor.WHITE+" almost died... But came back to life!");
					aPlugin.API.discordSendRawItalicized(ChatColor.GOLD+p.getName()+ChatColor.WHITE+" almost died... But came back to life!");
					break;
				}
			}
		}
		return revived;
	}

	private static void RevivePlayer(Player p, double healdmg) {
		p.setHealth(healdmg);
		p.playSound(p.getLocation(), Sound.BLOCK_REDSTONE_TORCH_BURNOUT, 1.0f, 1.5f);
		for (PotionEffect eff : p.getActivePotionEffects()) {
			if (isBadEffect(eff.getType())) {
				Bukkit.getScheduler().scheduleSyncDelayedTask(TwosideKeeper.plugin, 
	            () -> {
					p.removePotionEffect(eff.getType());
	            }, 1); 
			}
		}
		p.setFireTicks(0);
		p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING,20,0));
		p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION,20,0));
	}
	
	public static void DealExplosionDamageToEntities(Location l, double basedmg, double range) {
		List<Entity> nearbyentities = new ArrayList<Entity>(); 
		nearbyentities.addAll(l.getWorld().getNearbyEntities(l, range, range, range));
		for (int i=0;i<nearbyentities.size();i++) {
			Entity ent = nearbyentities.get(i);
			if (!(ent instanceof LivingEntity)) {
				nearbyentities.remove(i);
				i--;
			}
		}
		//We cleared the non-living entities, deal damage to the rest.
		for (int i=0;i<nearbyentities.size();i++) {
			double damage_mult = 2.0d/(l.distance(nearbyentities.get(i).getLocation())+1.0);
			damage_mult*=TwosideKeeper.EXPLOSION_DMG_MULT;
			damage_mult*=CalculateBlastResistance((LivingEntity)nearbyentities.get(i));
			double dmg = basedmg * damage_mult;
			double dodgechance = 0.0;
			if (nearbyentities.get(i) instanceof Player) {
				Player p = (Player)nearbyentities.get(i);
				dodgechance = NewCombat.CalculateDodgeChance(p);
			}
			DealDamageToMob(dmg,(LivingEntity)nearbyentities.get(i),null,null,"Explosion");
		}
	}

	private static double CalculateBlastResistance(LivingEntity l) {
		int explosionlv = 0;
		ItemStack[] equips = l.getEquipment().getArmorContents();
		for (int i=0;i<equips.length;i++) {
			if (equips[i]!=null && equips[i].getType()!=Material.AIR && equips[i].containsEnchantment(Enchantment.PROTECTION_EXPLOSIONS)) {
				explosionlv+=equips[i].getEnchantmentLevel(Enchantment.PROTECTION_EXPLOSIONS);
			}
		}
		return 1-(explosionlv*0.01);
	}

	public static void playProperEquipSound(Player p, Material type) {
		switch (type) {
			case LEATHER_HELMET:
			case LEATHER_CHESTPLATE:
			case LEATHER_LEGGINGS:
			case LEATHER_BOOTS:{
				p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_LEATHER, 1.0f, 1.0f);
			}break;
			case IRON_HELMET:
			case IRON_CHESTPLATE:
			case IRON_LEGGINGS:
			case IRON_BOOTS:{
				p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_IRON, 1.0f, 1.0f);
			}break;
			case GOLD_HELMET:
			case GOLD_CHESTPLATE:
			case GOLD_LEGGINGS:
			case GOLD_BOOTS:{
				p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_GOLD, 1.0f, 1.0f);
			}break;
			case DIAMOND_HELMET:
			case DIAMOND_CHESTPLATE:
			case DIAMOND_LEGGINGS:
			case DIAMOND_BOOTS:{
				p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_DIAMOND, 1.0f, 1.0f);
			}break;
			default:{
				p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, 1.0f, 1.0f);
			}
		}
	}

	//Returns 0.0-100.0.
	public static double PercentBlocksAroundArea(Block b, Material matchType, int x, int y, int z) {
		int totalblocks = 0;
		int matchedblocks = 0;
		for (int i=-x/2;i<x/2+1;i++) {
			for (int j=-y/2;j<y/2+1;j++) {
				for (int k=-z/2;k<z/2+1;k++) {
					if (b.getRelative(i, j, k)!=null && b.getRelative(i, j, k).getType()==matchType) {
						matchedblocks++;
					}
					totalblocks++;
				}
			}
		}
		double pct = (((double)matchedblocks)/totalblocks)*100d;
		TwosideKeeper.log("Checking a "+x/2+"x"+y/2+"x"+z/2+" area for block type "+matchType.name()+": "+pct+"%.", 4);
		return pct;
	}

	public static void setGlowing(Monster m, Color color) {
		Object[] players = Bukkit.getOnlinePlayers().toArray();
		for (int i=0;i<players.length;i++) {
			Player p = (Player)players[i];
			GlowAPI.setGlowing(m, false, p);
			if (!GlowAPI.isGlowing(m, p)) {
				GlowAPI.setGlowing(m, color, p);
			}
		}
	}
}
