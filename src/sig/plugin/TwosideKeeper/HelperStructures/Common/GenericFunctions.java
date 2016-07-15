package sig.plugin.TwosideKeeper.HelperStructures.Common;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Wool;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.google.common.collect.Iterables;

import sig.plugin.TwosideKeeper.Artifact;
import sig.plugin.TwosideKeeper.AwakenedArtifact;
import sig.plugin.TwosideKeeper.MonsterController;
import sig.plugin.TwosideKeeper.MonsterStructure;
import sig.plugin.TwosideKeeper.PlayerStructure;
import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.HelperStructures.ArtifactAbility;
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
						return Integer.parseInt(item.getItemMeta().getLore().get(i).split(": "+ChatColor.MAGIC)[1]);
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
	

	public static int getObscureHardenedItemBreaks(ItemStack item) {
		if (item.hasItemMeta() &&
				item.getItemMeta().hasLore()) {
			ItemMeta item_meta = item.getItemMeta();
			int breaks_remaining=-1;
			int loreline=-1;
			for (int i=0;i<item_meta.getLore().size();i++) {
				if (item_meta.getLore().get(i).contains(ChatColor.GRAY+"Breaks Remaining: "+ChatColor.MAGIC)) {
					return Integer.parseInt(item.getItemMeta().getLore().get(i).split(": "+ChatColor.MAGIC)[1]);
				}
			}
			return 0;
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
				return "Cobblestone Wall";
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
			case NETHER_BRICK_ITEM:{
				return "Nether Bricks";
			}
			case NETHER_WARTS:{
				return "Nether Wart";
			}
			case NETHER_STALK:{
				return "Nether Wart";
			}
			case GOLD_PLATE:{
				return "Gold Pressure Plate";
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
				return "Stone Brick";
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
			case EXP_BOTTLE:{
				return "Bottle o' Enchanting";
			}
			default:{
				return GenericFunctions.CapitalizeFirstLetters(type.getType().toString().replace("_", " "));
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
			item.getType().toString().contains("ROD") ||
			item.getType().toString().contains("HOE") ||
			item.getType().toString().contains("BOOTS") ||
			item.getType().toString().contains("CHESTPLATE") ||
			item.getType().toString().contains("LEGGINGS") ||
			item.getType().toString().contains("HELMET") ||
			item.getType().toString().contains("FISHING_ROD"))) {
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
			item.getType().toString().contains("AXE") ||
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
		if (m.getType()==EntityType.ZOMBIE &&
			MonsterController.isZombieLeader(m) ||
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
	
	public static double CalculateDodgeChance(Player p) {
		double dodgechance = 0.0d;
		dodgechance+=(ArtifactAbility.calculateValue(ArtifactAbility.DODGE, p.getEquipment().getItemInMainHand().getEnchantmentLevel(Enchantment.LUCK), ArtifactAbility.getEnchantmentLevel(ArtifactAbility.DODGE, p.getEquipment().getItemInMainHand()))/100d);

		for (int i=0;i<p.getEquipment().getArmorContents().length;i++) {
			if (ArtifactAbility.containsEnchantment(ArtifactAbility.SHADOWWALKER, p.getEquipment().getArmorContents()[i]) &&
					p.isOnGround() && p.getLocation().getY()>=0 && p.getLocation().add(0,0,0).getBlock().getLightLevel()<=4) {
				dodgechance+=0.01*p.getEquipment().getArmorContents()[i].getEnchantmentLevel(Enchantment.LUCK);
			}
		}
		if (ArtifactAbility.containsEnchantment(ArtifactAbility.SHADOWWALKER, p.getEquipment().getItemInMainHand()) &&
				p.isOnGround() && p.getLocation().getY()>=0 && p.getLocation().add(0,0,0).getBlock().getLightLevel()<=4) {
			dodgechance+=0.01*p.getEquipment().getItemInMainHand().getEnchantmentLevel(Enchantment.LUCK);
		}
		
		PlayerStructure pd = (PlayerStructure)TwosideKeeper.playerdata.get(p.getUniqueId());
		if (isStriker(p) &&
				pd.velocity>0) {
			dodgechance+=0.2;
		}
		return dodgechance;
	}
	
	public static void AutoRepairItems(Player p) {
		for (int i=0;i<9;i++) {
			if (ArtifactAbility.containsEnchantment(ArtifactAbility.AUTOREPAIR, p.getInventory().getItem(i))) {
				//Chance to auto repair.
				double repairamt = ArtifactAbility.calculateValue(ArtifactAbility.AUTOREPAIR, p.getInventory().getItem(i).getEnchantmentLevel(Enchantment.LUCK), ArtifactAbility.getEnchantmentLevel(ArtifactAbility.AUTOREPAIR, p.getInventory().getItem(i)));
				if (Math.random() <= repairamt%1) {
					repairamt++;
				}
				if (p.getLocation().getY()>=0 && p.getLocation().getBlock().getLightFromSky()==0) {
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
						p.getInventory().getItem(i).setDurability((short)(equip.getDurability()-repairamt));
						TwosideKeeper.log("Repaired "+repairamt+" durability to "+equip.toString()+"", 5);
					}
				}
			}
		}
	}
	
	public static void DealDamageToMob(double dmg, LivingEntity target, LivingEntity damager, boolean truedmg) {
		if (damager!=null && (target instanceof Monster) && !target.isDead()) {
			Monster m = (Monster)target;
			m.setTarget(damager);
		}

		double finaldmg = 0;
		if (truedmg) {
			finaldmg = dmg;
		} else {
			finaldmg = TwosideKeeper.CalculateDamageReduction(dmg, target, damager);
		}
		if ((target instanceof Monster) && damager!=null) {
			Monster m = (Monster)target;
			m.setTarget(damager);
			if (TwosideKeeper.monsterdata.containsKey(m.getUniqueId())) {
				MonsterStructure ms = (MonsterStructure)TwosideKeeper.monsterdata.get(m.getUniqueId());
				ms.SetTarget(damager);
			} else {
				TwosideKeeper.monsterdata.put(m.getUniqueId(),new MonsterStructure(damager));
			}
		}
		if (target.getHealth()>finaldmg) {
			TwosideKeeper.log("NOT FULL HEALTH", 5);
			target.setHealth(target.getHealth()-finaldmg);
			target.damage(0.01);
			target.setNoDamageTicks(20);
		} else {
			//Bukkit.getPluginManager().callEvent(new EntityDamageByEntityEvent(damager,target,DamageCause.ENTITY_ATTACK,finaldmg));
			//target.setHealth(0);
			target.setHealth(0.1);
			target.damage(9999999);
			target.setNoDamageTicks(20);
		}
	}
}
