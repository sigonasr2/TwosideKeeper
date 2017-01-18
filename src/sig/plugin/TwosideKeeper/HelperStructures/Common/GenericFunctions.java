package sig.plugin.TwosideKeeper.HelperStructures.Common;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Dropper;
import org.bukkit.block.Hopper;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_9_R1.block.CraftDropper;
import org.bukkit.craftbukkit.v1_9_R1.block.CraftHopper;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Wither;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.util.Vector;
import org.inventivetalent.glow.GlowAPI;
import org.inventivetalent.glow.GlowAPI.Color;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_9_R1.TileEntityDropper;
import net.minecraft.server.v1_9_R1.TileEntityHopper;
import sig.plugin.TwosideKeeper.ActionBarBuffUpdater;
import sig.plugin.TwosideKeeper.Artifact;
import sig.plugin.TwosideKeeper.AwakenedArtifact;
import sig.plugin.TwosideKeeper.CustomDamage;
import sig.plugin.TwosideKeeper.EliteMonster;
import sig.plugin.TwosideKeeper.MonsterController;
import sig.plugin.TwosideKeeper.LivingEntityStructure;
import sig.plugin.TwosideKeeper.PlayerStructure;
import sig.plugin.TwosideKeeper.Recipes;
import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.TwosideKeeperAPI;
import sig.plugin.TwosideKeeper.Boss.EliteGuardian;
import sig.plugin.TwosideKeeper.Boss.EliteZombie;
import sig.plugin.TwosideKeeper.Boss.MegaWither;
import sig.plugin.TwosideKeeper.Events.PlayerLineDriveEvent;
import sig.plugin.TwosideKeeper.Events.PlayerTumbleEvent;
import sig.plugin.TwosideKeeper.HelperStructures.ArrowBarrage;
import sig.plugin.TwosideKeeper.HelperStructures.ArtifactAbility;
import sig.plugin.TwosideKeeper.HelperStructures.BowMode;
import sig.plugin.TwosideKeeper.HelperStructures.CustomItem;
import sig.plugin.TwosideKeeper.HelperStructures.EliteMonsterLocationFinder;
import sig.plugin.TwosideKeeper.HelperStructures.ItemSet;
import sig.plugin.TwosideKeeper.HelperStructures.PlayerMode;
import sig.plugin.TwosideKeeper.HelperStructures.WorldShop;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.ArrayUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.ArtifactUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.ItemCubeUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.ItemUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.SoundUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.TextUtils;

public class GenericFunctions {

	public static int getHardenedItemBreaks(ItemStack item) {
		if (item.hasItemMeta() &&
				item.getItemMeta().hasLore()) {
			ItemMeta item_meta = item.getItemMeta();
			for (int i=0;i<item_meta.getLore().size();i++) {
				if (item_meta.getLore().get(i).contains(ChatColor.GRAY+"Breaks Remaining: ")) {
					if (item_meta.getLore().get(i).contains(ChatColor.GRAY+"Breaks Remaining: "+ChatColor.MAGIC)) {
						TwosideKeeper.log("This is obscure. Breaks is "+(Integer.parseInt(item.getItemMeta().getLore().get(i).split(": "+ChatColor.MAGIC)[1])), 2);
						return getObscureHardenedItemBreaks(item);
					} else {
						return Integer.parseInt(item.getItemMeta().getLore().get(i).split(": "+ChatColor.YELLOW)[1].split(ChatColor.MAGIC+"")[0]);
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

		/*StackTraceElement[] stacktrace = new Throwable().getStackTrace();
		StringBuilder stack = new StringBuilder("Mini stack tracer:");
		for (int i=0;i<Math.min(10, stacktrace.length);i++) {
			stack.append("\n"+stacktrace[i].getClassName()+": **"+stacktrace[i].getFileName()+"** "+stacktrace[i].getMethodName()+"():"+stacktrace[i].getLineNumber());
		}
		TwosideKeeper.log("Trace:"+stack, 0);*/
		int break_count = getHardenedItemBreaks(item);
		if (break_count>0) {
			ItemMeta m = item.getItemMeta();
			List<String> lore = item.getItemMeta().getLore();
			for (int i=0;i<lore.size();i++) {
				if (lore.get(i).contains(ChatColor.GRAY+"Breaks Remaining: ")) {
					if (lore.get(i).contains(ChatColor.GRAY+"Breaks Remaining: "+ChatColor.MAGIC)) {
						TwosideKeeper.log("This is obscure.", 2);
						break_count--;
						if (p!=null && break_count==0) {
			    				p.sendMessage(ChatColor.GOLD+"WARNING!"+ChatColor.GREEN+ " Your "+ChatColor.YELLOW+GenericFunctions.UserFriendlyMaterialName(item)+ChatColor.WHITE+" is going to break soon! You should let it recharge by waiting 24 hours!");
						}
						if (p!=null) {
							SoundUtils.playLocalSound(p, Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
						}
						return breakObscureHardenedItem(item);
					} else {
						lore.set(i, ChatColor.GRAY+"Breaks Remaining: "+ChatColor.YELLOW+(break_count-1));
						if ((break_count-1)<0) {
							break_count=0;
						}
						TwosideKeeper.log("Setting breaks remaining to "+(break_count-1),3);
						break;
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
    			SoundUtils.playLocalSound(p, Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
			}
			return item;
			//By setting the amount to 1, you refresh the item in the player's inventory.
		} else {
			//This item is technically destroyed.
			if (p!=null) {
				SoundUtils.playLocalSound(p, Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
			}
			if (isArtifactEquip(item)) {
				//We can turn it into dust!
				if (p!=null) {
					p.sendMessage(ChatColor.LIGHT_PURPLE+"You still feel the artifact's presence inside of you...");
				}
				return convertArtifactToDust(item);
			}
			return null;
		}
	}

	public static ItemStack convertArtifactToDust(ItemStack item) {
		//Add one line of lore to indicate it's broken dust.
		item = addObscureHardenedItemBreaks(item,1);
		ItemMeta m = item.getItemMeta();
		List<String> oldlore = m.getLore();
		oldlore.add(0,ChatColor.DARK_BLUE+""+ChatColor.MAGIC+item.getType());
		oldlore.add(1,ChatColor.GOLD+""+ChatColor.BOLD+"[ARTIFACT DUST]");
		oldlore.add(2,ChatColor.DARK_BLUE+""+ChatColor.MAGIC+item.getType());
		oldlore.add(3,ChatColor.DARK_PURPLE+"Its physical form may be lost");
		oldlore.add(4,ChatColor.DARK_PURPLE+"but there might still be some");
		oldlore.add(5,ChatColor.DARK_PURPLE+"power hidden within...");
		oldlore.add(6,"");
		
		for (int i=0;i<oldlore.size();i++) {
			if (oldlore.get(i).contains(ChatColor.BLUE+""+ChatColor.MAGIC)) {
				//See what the previous time was.
				oldlore.set(i, ChatColor.BLUE+""+ChatColor.MAGIC+TwosideKeeper.getServerTickTime());
			}
		}
		m.setLore(oldlore);
		item.setItemMeta(m);
		item.setType(Material.SULPHUR);
		item.setDurability((short)0); 
		//item.setAmount(1);
		return item;
	}
	
	public static ItemStack convertArtifactDustToItem(ItemStack item) {
		ItemMeta m = item.getItemMeta();
		long time = TwosideKeeper.getServerTickTime();
		List<String> oldlore = m.getLore();
		for (int i=0;i<oldlore.size();i++) {
			if (oldlore.get(i).contains(ChatColor.BLUE+""+ChatColor.MAGIC)) {
				//See what the previous time was.
				time = Long.parseLong(ChatColor.stripColor(oldlore.get(i)));
				oldlore.set(i, ChatColor.BLUE+""+ChatColor.MAGIC+TwosideKeeper.getServerTickTime());
			}
		}
		if (time+12096000<=TwosideKeeper.getServerTickTime()) {
			Material gettype = Material.valueOf(ChatColor.stripColor(oldlore.get(0)));
			oldlore.remove(6);
			oldlore.remove(5);
			oldlore.remove(4);
			oldlore.remove(3);
			oldlore.remove(2);
			oldlore.remove(1); 
			oldlore.remove(0);
			
			for (int i=0;i<oldlore.size();i++) {
				if (oldlore.get(i).contains(ChatColor.BLUE+""+ChatColor.MAGIC)) {
					//See what the previous time was.
					time = Long.parseLong(ChatColor.stripColor(oldlore.get(i)));
					oldlore.set(i, ChatColor.BLUE+""+ChatColor.MAGIC+TwosideKeeper.getServerTickTime());
				}
			}
			
			m.setLore(oldlore);
			item.setItemMeta(m);
			item.setType(gettype);
			item.setDurability((short)0);
			item = addObscureHardenedItemBreaks(item,5);
		}
		return item;
	}

	public static ItemStack addHardenedItemBreaks(ItemStack item, int breaks) {
		return addHardenedItemBreaks(item,breaks,false);
	}
	
	public static ItemStack addHardenedItemBreaks(ItemStack item, int breaks, boolean addname) {
		if (isHardenedItem(item)) {
			//We can just modify the amount of breaks.
			TwosideKeeper.log("We got here.",2);
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
			if (addname) {
				if (m.hasDisplayName()) {
					m.setDisplayName(ChatColor.BLUE+"Hardened "+m.getDisplayName());
				} else {
					m.setDisplayName(ChatColor.BLUE+"Hardened "+UserFriendlyMaterialName(item));
				}
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
			int loreline=-1;
			for (int i=0;i<item_meta.getLore().size();i++) {
				TwosideKeeper.log("Line is "+item_meta.getLore().get(i),3);
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
		for (ItemStack equip : e.getEquipment().getArmorContents()) {
			if (equip!=null &&
					equip.getType()!=Material.AIR) {
				if (equip.getEnchantmentLevel(Enchantment.THORNS)>=maxlv) {
					maxlv = equip.getEnchantmentLevel(Enchantment.THORNS);
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
			if ((break_count)<0) {
				break_count=0;
			}
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
							break;
						}
				}
			}
			if (break_count>5) {break_count=5;}
			lore.set(break_line, ChatColor.GRAY+"Breaks Remaining: "+ChatColor.MAGIC+(break_count));
			if ((break_count)<0) {
				break_count=0;
			}
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
			return null;
		}
	}
	

	public static String UserFriendlyMaterialName(Material type) {
		return UserFriendlyMaterialName(new ItemStack(type,1,(short)0));
	}
	@Deprecated
	public static String UserFriendlyMaterialName(Material type, byte b) {
		return UserFriendlyMaterialName(new ItemStack(type,1,(short)b));
	}
	public static String UserFriendlyMaterialName(Material type,short b) {
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
					return "Golden Shovel";
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
				case FIREBALL:{
					return "Fire Charge";
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
				case GOLD_RECORD:{
					return "Music Disc - 13";
				}
				case GREEN_RECORD:{
					return "Music Disc - cat";
				}
				case RECORD_10:{
					return "Music Disc - ward";
				}
				case RECORD_11:{
					return "Music Disc - 11";
				}
				case RECORD_12:{
					return "Music Disc - wait";
				}
				case RECORD_3:{
					return "Music Disc - blocks";
				}
				case RECORD_4:{
					return "Music Disc - chirp";
				}
				case RECORD_5:{
					return "Music Disc - far";
				}
				case RECORD_6:{
					return "Music Disc - mall";
				}
				case RECORD_7:{
					return "Music Disc - mellohi";
				}
				case RECORD_8:{
					return "Music Disc - stal";
				}
				case RECORD_9:{
					return "Music Disc - strad";
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
				case RAW_FISH:{
					switch (type.getDurability()) {
						case 0:{
							return "Raw Fish";
						}
						case 1:{
							return "Raw Salmon";
						}
						case 2:{
							return "Clownfish";
						}
						case 3:{
							return "Pufferfish";
						}
					}
				}
				case COOKED_FISH:{
					switch (type.getDurability()) {
						case 0:{
							return "Cooked Fish";
						}
						case 1:{
							return "Cooked Salmon";
						}
					}
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
				case ANVIL:{
					switch (type.getDurability()) {
						case 0:{
							return "Anvil";
						}
						case 1:{
							return "Slightly Damaged Anvil";
						}
						case 2:{
							return "Very Damaged Anvil";
						}
					}
				}
				case SKULL_ITEM:{
					switch (type.getDurability()) {
						case 0:{
							return "Skeleton Skull";
						}
						case 1:{
							return "Wither Skeleton Skull";
						}
						case 2:{
							return "Zombie Head";
						}
						case 3:{
							SkullMeta sm = (SkullMeta)type.getItemMeta();
							if (sm.hasOwner()) {
								return sm.getOwner()+"'s Head";
							} else {
								return "Head";
							}
						}
						case 4:{
							return "Creeper Head";
						}
						case 5:{
							return "Dragon Head";
						}
					}
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
							return "Tipped Arrow";
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
							return "Tipped Arrow";
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
				case SPONGE:{
					switch (type.getDurability()) {
						case 0:{
							return "Sponge";
						}
						case 1:{
							return "Wet Sponge";
						}
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
				case MYCEL:{
					return "Mycelium";
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
				case GOLDEN_APPLE:{
					switch (type.getDurability()) {
						case 0:{
							return "Golden Apple";
						}
						case 1:{
							return ChatColor.LIGHT_PURPLE+"Golden Apple";
						}
					}
				}
				case GOLD_HELMET:{
					return "Golden Helmet";
				}
				case GOLD_LEGGINGS:{
					return "Golden Leggings";
				}
				case GOLD_CHESTPLATE:{
					return "Golden Chestplate";
				}
				case GOLD_BOOTS:{
					return "Golden Boots";
				}
				case GOLD_AXE:{
					return "Golden Axe";
				}
				case GOLD_PICKAXE:{
					return "Golden Pickaxe";
				}
				case GOLD_HOE:{
					return "Golden Hoe";
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
		for (ItemStack i : it.getContents()) {
			if (i!=null &&
					i.isSimilar(item)) {
				totalcount+=i.getAmount();
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
		for (ItemStack i : it.getContents()) {
			if (i!=null &&
					(i.getType()==Material.AIR ||
					i.isSimilar(item))) {
				if (i.getAmount()!=item.getMaxStackSize()) {
					totalcount+=item.getMaxStackSize()-i.getAmount();
				} else {
					//TwosideKeeper.log("This is equivalent to max stack size of "+item.getMaxStackSize(), 2);
					//totalcount+=item.getMaxStackSize();
				}
			} else if (i==null) {
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
			for (String line : item.getItemMeta().getLore()) {
				TwosideKeeper.log("Lore line is: "+line, 5);
				if (line.contains(ChatColor.GRAY+"Breaks Remaining:")) {
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
			for (String line : item.getItemMeta().getLore()) {
				TwosideKeeper.log("Lore line is: "+line, 5);
				if (line.contains(ChatColor.GRAY+"Breaks Remaining: "+ChatColor.MAGIC)) {
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
				isEquip(item)) {
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
			item.getType().toString().contains("SHIELD") ||
			item.getType().toString().contains("CARROT_STICK") ||
			item.getType().toString().contains("ELYTRA"))) {
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
				item.getType()!=Material.AIR && isWeapon(item) &&
				Artifact.isArtifact(item)) {
			return true;
		} else {
			return false;
		}
	}
	public static boolean isArtifactArmor(ItemStack item) {
		if (item!=null &&
				item.getType()!=Material.AIR && isArmor(item) &&
				Artifact.isArtifact(item)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isArtifactTool(ItemStack item) {
		if (item!=null &&
				item.getType()!=Material.AIR && isHarvestingTool(item) &&
				Artifact.isArtifact(item)) {
			return true; 
		} else {
			return false;
		}
	}

	public static boolean AllLeatherArmor(Player p) {
		boolean leather=true;
		for (ItemStack equip : p.getEquipment().getArmorContents()) {
			if (equip!=null &&
					!equip.getType().toString().contains("LEATHER")) {
				leather=false;
				break;
			}
		}
		return leather;
	}
	
	public static String PlayerModePrefix(Player p) {
		PlayerMode pm = PlayerMode.getPlayerMode(p);
		//if (pm!=PlayerMode.NORMAL) {
			return pm.getColor()+""+ChatColor.ITALIC+"("+pm.getAbbreviation()+") "+ChatColor.RESET+pm.getColor();
		/*} else {
			return "";
		}*/
	}
	
	public static TextComponent PlayerModeName(Player p) {
		TextComponent tc = new TextComponent("");
		PlayerMode pm = PlayerMode.getPlayerMode(p);
		TextComponent tc1 = new TextComponent(pm.getColor()+""+ChatColor.BOLD+pm.getName()+ChatColor.RESET);
		tc1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ComponentBuilder("Click to view details about "+pm.getColor()+""+ChatColor.BOLD+pm.getName()+ChatColor.RESET+".").create()));
		tc1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/mode "+pm.name()));
		tc.addExtra(tc1);
		return tc;
	}
	
	public static String PlayerModeInformation(String mode) {
		PlayerMode pm = PlayerMode.valueOf(mode.toUpperCase());
		return pm.getDesription();
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
	
	public static boolean isBossMonster(LivingEntity m) {
		if (MonsterController.isZombieLeader(m) ||
			(m.getType()==EntityType.GUARDIAN &&
			((Guardian)m).isElder()) ||
			m.getType()==EntityType.ENDER_DRAGON ||
			m.getType()==EntityType.WITHER ||
			LivingEntityStructure.getLivingEntityStructure(m).getLeader() ||
			LivingEntityStructure.getLivingEntityStructure(m).getElite()) {
				return true;
			} else {
				return false;
			}
	}
	
	public static boolean isCoreMonster(LivingEntity m) {
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
			if (p.getInventory().getItem(i)!=null && p.getInventory().getItem(i).isSimilar(item)) {
				TwosideKeeper.log("Found item in slot "+i, 5);
				return i;
			}
		}
		
		//It might be in the armor slot.
		for (int i=0;i<p.getEquipment().getArmorContents().length;i++) {
			TwosideKeeper.log("Checking armor slot "+i, 5);
			if (p.getEquipment().getArmorContents()[i]!=null && p.getEquipment().getArmorContents()[i].isSimilar(item)) {
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
		for (PotionEffect pe : p.getActivePotionEffects()) {
			if (isBadEffect(pe.getType())) {
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
			for (PotionEffect pe : ent.getActivePotionEffects()) {
				if (pe.getType().equals(PotionEffectType.UNLUCK)) {
					int lv = pe.getAmplifier();
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
		if (ent instanceof LivingEntity) {
			LivingEntity m = (LivingEntity)ent;
			m.setCustomNameVisible(true);
			if (m.getCustomName()!=null) {
				m.setCustomName(getDeathMarkColor(stackamt)+ChatColor.stripColor(GenericFunctions.getDisplayName(m)));
			} else {
				m.setCustomName(getDeathMarkColor(stackamt)+CapitalizeFirstLetters(m.getType().toString().replace("_", " ")));
			}
		}
	}
	
	public static int GetDeathMarkAmt(LivingEntity ent) {
		if (ent.hasPotionEffect(PotionEffectType.UNLUCK)) {
			//Add to the current stack of unluck.
			for (PotionEffect pe : ent.getActivePotionEffects()) {
				if (pe.getType().equals(PotionEffectType.UNLUCK)) {
					return pe.getAmplifier()+1;
				}
			}
		}
		return 0;
	}
	
	public static void ResetMobName(LivingEntity ent) {
		if (ent instanceof LivingEntity) {
			LivingEntity m = (LivingEntity)ent;
			m.setCustomNameVisible(false);
			if (m.getCustomName()!=null) {
				m.setCustomName(ChatColor.stripColor(GenericFunctions.getDisplayName(m)));
				if (m.getCustomName().contains("Dangerous")) {
					m.setCustomName(ChatColor.DARK_AQUA+m.getCustomName());
				}
				if (m.getCustomName().contains("Deadly")) {
					m.setCustomName(ChatColor.GOLD+m.getCustomName());
				}
				if (m.getCustomName().contains("Hellfire")) {
					m.setCustomName(ChatColor.DARK_RED+m.getCustomName());
				}
				CustomDamage.appendDebuffsToName(m);
			}
		}
	}
	
	public static ItemStack RemovePermEnchantmentChance(ItemStack item, Player p) { 
		if (item!=null &&
				item.getType()!=Material.AIR) {
			int mendinglv = item.getEnchantmentLevel(Enchantment.MENDING);
			int infinitylv = item.getEnchantmentLevel(Enchantment.ARROW_INFINITE);
			//TwosideKeeper.log("["+TwosideKeeper.getServerTickTime()+"] Testing Mending...", 1);
			if (mendinglv>0 && Math.random()<=0.00048828125*(isHarvestingTool(item)?0.75:1d)) {
				//TwosideKeeper.log("Knockoff!", 0);
				mendinglv--;
				if (mendinglv>0) {
					item.addUnsafeEnchantment(Enchantment.MENDING, mendinglv);
				} else {
					item.removeEnchantment(Enchantment.MENDING);
				}
				p.sendMessage(ChatColor.DARK_AQUA+"A level of "+ChatColor.YELLOW+"Mending"+ChatColor.DARK_AQUA+" has been knocked off of your "+((item.hasItemMeta() && item.getItemMeta().hasDisplayName())?item.getItemMeta().getDisplayName():UserFriendlyMaterialName(item)));
			}
			if (infinitylv>0 && Math.random()<=0.005*(isHarvestingTool(item)?0.75:1d)) {
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
		return ItemSet.hasFullSet(GenericFunctions.getEquipment(p), p, ItemSet.ALIKAHN) ||
				ItemSet.hasFullSet(GenericFunctions.getEquipment(p), p, ItemSet.DARNYS) ||
				ItemSet.hasFullSet(GenericFunctions.getEquipment(p), p, ItemSet.JAMDAK) ||
				ItemSet.hasFullSet(GenericFunctions.getEquipment(p), p, ItemSet.LORASAADI);
		/*int rangerarmort1 = 0; //Count the number of each tier of sets. //LEGACY CODE.
		int rangerarmort2 = 0;
		int rangerarmort3 = 0;
		int rangerarmort4 = 0;
		
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
		
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		
		if (rangerarmort1==4 || rangerarmort2==4 || rangerarmort3==4 || rangerarmort4==4) {
			//Player has the full set.
			pd.hasfullrangerset=true;
		} else {
			pd.hasfullrangerset=false;
		}
		
		return pd.hasfullrangerset;*/
	}
	
	@Deprecated
	public static void applyModeName(ItemStack item) {
		/*if (item!=null &&
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
		item.setItemMeta(m);*/
		//return item;
	}
	
	public static BowMode getBowMode(Player p) {
		/*if (item!=null &&
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
		}*/
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		return pd.rangermode;
	}
	
	public static void setBowMode(Player p, BowMode mode) {
		/*if (item!=null &&
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
		*/
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		pd.rangermode = mode;
		GenericFunctions.sendActionBarMessage(p, ChatColor.BLUE+"Bow Mode: "+ChatColor.GOLD+mode.GetCoolName()+" Mode"+ChatColor.RESET, true);
	}
	
	public static void AutoRepairItems(Player p) {
		for (int i=0;i<9;i++) {
			if (ArtifactAbility.containsEnchantment(ArtifactAbility.AUTOREPAIR, p.getInventory().getItem(i))) {
				//Chance to auto repair.
				double repairamt = ArtifactAbility.calculateValue(ArtifactAbility.AUTOREPAIR, ArtifactUtils.getArtifactTier(p.getInventory().getItem(i)), ArtifactAbility.getEnchantmentLevel(ArtifactAbility.AUTOREPAIR, p.getInventory().getItem(i)));
				if (Math.random() <= repairamt%1) {
					repairamt++;
				}
				double chance = 0.5;
				if (p.getLocation().getY()>=0 && p.getLocation().getY()<=255 && p.getLocation().getBlock().getLightFromSky()==0) {
					repairamt/=2.0d;
					chance/=2d;
					//TwosideKeeper.log("In Darkness.",2);
				}
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
				double repairamt = ArtifactAbility.calculateValue(ArtifactAbility.AUTOREPAIR, ArtifactUtils.getArtifactTier(equip), ArtifactAbility.getEnchantmentLevel(ArtifactAbility.AUTOREPAIR, equip));
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
	
	
	public static void knockOffGreed(Player p) {
		// Chance: (11-tier)*5
		//Check for artifacts on all equips.
		boolean brokeone = false;
		for (ItemStack item : p.getEquipment().getArmorContents()) {
			if (isArtifactEquip(item) &&
					ArtifactAbility.containsEnchantment(ArtifactAbility.GREED, item)) {
					TwosideKeeper.log("Found one.",5);
					int tier = ArtifactUtils.getArtifactTier(item);
				if (Math.random()<=(8-(tier/2d))/100d) {
					item = ArtifactAbility.downgradeEnchantment(p, item, ArtifactAbility.GREED);
					p.sendMessage(ChatColor.DARK_AQUA+"A level of "+ChatColor.YELLOW+"Greed"+ChatColor.DARK_AQUA+" has been knocked off of your "+((item.hasItemMeta() && item.getItemMeta().hasDisplayName())?item.getItemMeta().getDisplayName():UserFriendlyMaterialName(item)));
					//AwakenedArtifact.setLV(item, AwakenedArtifact.getLV(item)-1, p);
					AwakenedArtifact.setMaxAP(item, AwakenedArtifact.getMaxAP(item)-1); //We knock off one Max AP because it's a temporary ability!!
					brokeone=true;
					return;
				}
			}
		}
		if (!brokeone) {
			//Try the main hand.
			//TwosideKeeper.log("Trying to break in here.", 0);
			ItemStack item = p.getEquipment().getItemInMainHand();
			if (isArtifactEquip(item) &&
					ArtifactAbility.containsEnchantment(ArtifactAbility.GREED, item)) {
				int tier = ArtifactUtils.getArtifactTier(item);
				//TwosideKeeper.log("Chance is "+((8-(tier/2d))/100d), 0);
				if (Math.random()<=(8-(tier/2d))/100d) {
					item = ArtifactAbility.downgradeEnchantment(p, item, ArtifactAbility.GREED);
					//AwakenedArtifact.setLV(item, AwakenedArtifact.getLV(item)-1, p);
					AwakenedArtifact.setMaxAP(item, AwakenedArtifact.getMaxAP(item)-1); //We knock off one Max AP because it's a temporary ability!!
					p.sendMessage(ChatColor.DARK_AQUA+"A level of "+ChatColor.YELLOW+"Greed"+ChatColor.DARK_AQUA+" has been knocked off of your "+((item.hasItemMeta() && item.getItemMeta().hasDisplayName())?item.getItemMeta().getDisplayName():UserFriendlyMaterialName(item)));
					brokeone=true;
					return;
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
			for (PotionEffect pe : ent.getActivePotionEffects()) {
				if (pe.getType().equals(type)) {
					//Get the level.
					return pe.getAmplifier();
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
			for (PotionEffect pe : ent.getActivePotionEffects()) {
				if (pe.getType().equals(type)) {
					//Get the level.
					return pe.getDuration();
				}
			}
			TwosideKeeper.log("Something went wrong while getting potion effect duration of "+type+" for Entity "+ent.getName()+"!", 1);
			return -1;
		} else {
			return -1;
		}
	}

	@SuppressWarnings("deprecation")
	public static void PerformDodge(Player p) {
		if (p.isOnGround() && PlayerMode.isRanger(p) &&
				(GenericFunctions.getBowMode(p)==BowMode.CLOSE)) {
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			if (pd.last_dodge+GetModifiedCooldown(TwosideKeeper.DODGE_COOLDOWN,p)<=TwosideKeeper.getServerTickTime()) {
				PlayerTumbleEvent ev = new PlayerTumbleEvent(p);
				Bukkit.getPluginManager().callEvent(ev);
				if (!ev.isCancelled()) {
					pd.last_dodge=TwosideKeeper.getServerTickTime();
					aPlugin.API.sendCooldownPacket(p, p.getEquipment().getItemInMainHand(), 100);
					SoundUtils.playLocalSound(p, Sound.ENTITY_DONKEY_CHEST, 1.0f, 1.0f);
					
					int dodgeduration = 20;
					
					if (GenericFunctions.HasFullRangerSet(p)) {
						dodgeduration = 30;
					}
					
					if (p.isSneaking()) { //Do a backwards dodge + jump.
						p.setVelocity(p.getLocation().getDirection().multiply(-0.7f));
					} else {
						p.setVelocity(p.getLocation().getDirection().multiply(1.4f));
					}
					ApplySwiftAegis(p);
					CustomDamage.addIframe(dodgeduration, p);
					
					logAndApplyPotionEffectToEntity(PotionEffectType.SPEED,dodgeduration,2,p);
				}
			}
		}
	}

	public static int GetModifiedCooldown(int cooldown, Player p) {
		double cdr = CustomDamage.calculateCooldownReduction(p); //0.0-1.0
		return (int)(cooldown*(1-cdr));
	}

	public static void logAndApplyPotionEffectToEntity(PotionEffectType type, int ticks, int amplifier, LivingEntity p) {
		logAndApplyPotionEffectToEntity(type,ticks,amplifier,p,false);
	}
	
	public static void logAndApplyPotionEffectToEntity(PotionEffectType type, int ticks, int amplifier, LivingEntity p, boolean force) {
		TwosideKeeper.log(ChatColor.WHITE+"Adding Potion Effect "+type.getName()+" "+WorldShop.toRomanNumeral((amplifier+1))+"("+amplifier+") to "+p.getName()+" with "+ticks+" tick duration. "+((force)?ChatColor.RED+"FORCED":""), 5);
		if (p.hasPotionEffect(type)) {
			TwosideKeeper.log(ChatColor.YELLOW+" Already had effect on Player "+p.getName()+". "+type.getName()+" "+WorldShop.toRomanNumeral((getPotionEffectLevel(type,p)+1))+"("+getPotionEffectLevel(type,p)+"), Duration: "+getPotionEffectDuration(type,p)+" ticks", TwosideKeeper.POTION_DEBUG_LEVEL);
			if (!force) {
				TwosideKeeper.log(ChatColor.RED+"   This should not be overwritten due to no FORCE!", TwosideKeeper.POTION_DEBUG_LEVEL);
			}
		}
		Bukkit.getScheduler().scheduleSyncDelayedTask(TwosideKeeper.plugin, new Runnable() {
 			public void run() {
 				p.addPotionEffect(new PotionEffect(type,ticks,amplifier),force);
 			}
 		},1);
		TwosideKeeper.log(ChatColor.GRAY+" Effect on Player "+p.getName()+" is now "+type.getName()+" "+WorldShop.toRomanNumeral((amplifier+1))+"("+amplifier+"), Duration: "+ticks+" ticks", TwosideKeeper.POTION_DEBUG_LEVEL);
		if (amplifier==-1 || ticks==0) {
			//Something really bad happened!!!
			TwosideKeeper.log("OUT OF PARAMETERS! Reporting", TwosideKeeper.POTION_DEBUG_LEVEL);
			StackTraceElement[] stacktrace = new Throwable().getStackTrace();
			StringBuilder stack = new StringBuilder("Mini stack tracer:");
			for (int i=0;i<Math.min(10, stacktrace.length);i++) {
				stack.append("\n"+stacktrace[i].getClassName()+": **"+stacktrace[i].getFileName()+"** "+stacktrace[i].getMethodName()+"():"+stacktrace[i].getLineNumber());
			}
			//DiscordMessageSender.sendToSpam(stack.toString());
		} 
	}
	
	public static void logAndRemovePotionEffectFromEntity(PotionEffectType type, LivingEntity p) {
		TwosideKeeper.log(ChatColor.WHITE+"Removing Potion Effect "+type+" "+WorldShop.toRomanNumeral((getPotionEffectLevel(type,p)+1))+"("+getPotionEffectLevel(type,p)+") on Player "+p.getName()+" Duration: "+getPotionEffectDuration(type,p)+" ticks by adding a 0 duration version of this effect.", TwosideKeeper.POTION_DEBUG_LEVEL);
		//p.removePotionEffect(type);
		logAndApplyPotionEffectToEntity(type,1,0,p,true);
		if (p.hasPotionEffect(type)) {
			TwosideKeeper.log(ChatColor.DARK_RED+" Effect on Player "+p.getName()+" is now "+type+" "+WorldShop.toRomanNumeral((getPotionEffectLevel(type,p)+1))+"("+getPotionEffectLevel(type,p)+"), Duration: "+getPotionEffectDuration(type,p)+" ticks", TwosideKeeper.POTION_DEBUG_LEVEL);
			TwosideKeeper.log(ChatColor.RED+"THIS SHOULD NOT BE HAPPENING! Reporting", TwosideKeeper.POTION_DEBUG_LEVEL);
			StackTraceElement[] stacktrace = new Throwable().getStackTrace();
			StringBuilder stack = new StringBuilder("Mini stack tracer:");
			for (int i=0;i<Math.min(10, stacktrace.length);i++) {
				stack.append("\n"+stacktrace[i].getClassName()+": **"+stacktrace[i].getFileName()+"** "+stacktrace[i].getMethodName()+"():"+stacktrace[i].getLineNumber());
			}
			//DiscordMessageSender.sendToSpam(stack.toString());
		}
		
	}
	
	public static String GetEntityDisplayName(Entity e) {
		if (e instanceof LivingEntity) {
			LivingEntity l = (LivingEntity)e;
			if (l.getCustomName()!=null) { 
				return GenericFunctions.getDisplayName(l);
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
					return finalname+"("+GenericFunctions.getDisplayName(l)+ChatColor.GRAY+")";
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
		if (isArtifactEquip(weapon)) {
			return ArtifactAbility.calculateValue(ab, ArtifactUtils.getArtifactTier(weapon), ArtifactAbility.getEnchantmentLevel(ab, weapon));
		} else {
			return 0.0;
		}	
	}

	public static boolean enoughTicksHavePassed(LivingEntity entity, Entity damager) {
		if (entity instanceof Player) {
			Player p = (Player)entity;
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			if (damager!=null) {
				if (damager instanceof Projectile) {
					if (CustomDamage.getDamagerEntity(damager)!=null) {
						damager = CustomDamage.getDamagerEntity(damager);
					}
				}
				if (pd.hitlist.containsKey(damager.getUniqueId())) {
					long time = pd.hitlist.get(damager.getUniqueId());
					if (time+10<TwosideKeeper.getServerTickTime()) {
						return true;
					} 
				} else {
					return true;
				}
			} else {
				TwosideKeeper.log("It's null.", 5);
				if (pd.hitlist.containsKey(p.getUniqueId())) {
					long time = pd.hitlist.get(p.getUniqueId());
					TwosideKeeper.log("->Last hit on "+time+". Current time: "+TwosideKeeper.getServerTickTime(), 5);
					if (time+10<TwosideKeeper.getServerTickTime()) {
						return true;
					}
				} else {
					return true;
				}
			}
		} else
		if (entity instanceof LivingEntity) {
			LivingEntity m = (LivingEntity)entity;
			LivingEntityStructure md = LivingEntityStructure.getLivingEntityStructure(m);
			if (damager!=null) {
				if (damager instanceof Projectile) {
					if (CustomDamage.getDamagerEntity(damager)!=null) {
						damager = CustomDamage.getDamagerEntity(damager);
					}
				}
				if (md.hitlist.containsKey(damager.getUniqueId())) {
					long time = md.hitlist.get(damager.getUniqueId());
					TwosideKeeper.log("Last hit on "+time+". Current time: "+TwosideKeeper.getServerTickTime(), 5);
					if (time+10<TwosideKeeper.getServerTickTime()) {
						return true;
					}
				} else {
					return true;
				}
			} else {
				if (md.hitlist.containsKey(m.getUniqueId())) {
					long time = md.hitlist.get(m.getUniqueId());
					TwosideKeeper.log("->Last hit on "+time+". Current time: "+TwosideKeeper.getServerTickTime(), 5);
					if (time+10<TwosideKeeper.getServerTickTime()) {
						return true;
					}
				} else {
					return true;
				}
			}
		}
		TwosideKeeper.log("Returning false... "+TwosideKeeper.getServerTickTime(), 5);
		return false;
	}

	public static void removeNoDamageTick(LivingEntity entity, Entity damager) {
		damager = CustomDamage.getDamagerEntity(damager);
		if (entity instanceof Player) {
			Player p = (Player)entity;
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			if (damager!=null) {
				pd.hitlist.remove(damager.getUniqueId());
			} else {
				pd.hitlist.remove(p.getUniqueId());
			}
		} else
		if (entity instanceof LivingEntity) {
			LivingEntity m = (LivingEntity)entity;
			LivingEntityStructure md = LivingEntityStructure.getLivingEntityStructure(m);
			if (damager!=null) {
				if (damager instanceof Player) {
					Player p = (Player)damager;
					if (GenericFunctions.getPotionEffectLevel(PotionEffectType.WEAKNESS, p)>=9) {
						p.removePotionEffect(PotionEffectType.WEAKNESS);
					}
				}
				md.hitlist.remove(damager.getUniqueId());
			} else {
				md.hitlist.remove(m.getUniqueId());
			}
		}
	}

	public static void updateNoDamageTickMap(LivingEntity entity, Entity damager) {
		updateNoDamageTickMap(entity,damager,0);
	}
	
	public static void updateNoDamageTickMap(LivingEntity entity, Entity damager, int extraticks) {
		if (entity instanceof Player) {
			Player p = (Player)entity;
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			if (damager!=null) {
				if (damager instanceof Projectile) {
					if (CustomDamage.getDamagerEntity(damager)!=null) {
						damager = CustomDamage.getDamagerEntity(damager);
					}
				}
				pd.hitlist.put(damager.getUniqueId(), TwosideKeeper.getServerTickTime()+extraticks);
			} else {
				TwosideKeeper.log("Adding one.", 5);
				pd.hitlist.put(p.getUniqueId(), TwosideKeeper.getServerTickTime()+extraticks);
			}
		} else
		if (entity instanceof LivingEntity) {
			LivingEntity m = (LivingEntity)entity;
			LivingEntityStructure md = LivingEntityStructure.getLivingEntityStructure(m);
			if (damager!=null) {
				if (damager instanceof Projectile) {
					if (CustomDamage.getDamagerEntity(damager)!=null) {
						damager = CustomDamage.getDamagerEntity(damager);
					}
				}
				md.hitlist.put(damager.getUniqueId(), TwosideKeeper.getServerTickTime()+extraticks);
			} else {
				md.hitlist.put(m.getUniqueId(), TwosideKeeper.getServerTickTime()+extraticks);
			}
		}
	}

	public static boolean isViewingInventory(Player p) {
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		return pd.isViewingInventory;
	}
	
	public static void addIFrame(Player p, int ticks) {
		CustomDamage.addIframe(ticks, p);
	}

	public static void PerformRejuvenate(Player player) {
		PlayerStructure pd = (PlayerStructure)TwosideKeeper.playerdata.get(player.getUniqueId());
		if (pd.last_rejuvenate+GetModifiedCooldown(TwosideKeeper.REJUVENATE_COOLDOWN,player)<=TwosideKeeper.getServerTickTime()) {
			SoundUtils.playGlobalSound(player.getLocation(), Sound.ENTITY_ZOMBIE_VILLAGER_CURE, 1.0f, 1.0f);
			addIFrame(player,40);
			GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.REGENERATION,200,9,player,true);
			aPlugin.API.sendCooldownPacket(player, player.getEquipment().getItemInMainHand(), GetModifiedCooldown(TwosideKeeper.REJUVENATE_COOLDOWN,player));
		}
	}
	
	public static boolean isArmoredMob(LivingEntity m) {
		if (m.getType()==EntityType.ZOMBIE ||
				m.getType()==EntityType.PIG_ZOMBIE ||
				m.getType()==EntityType.SKELETON) {
			return true;
		}
		return false;
	}

	public static boolean isSoftBlock(Block b) {
		return isSoftBlock(b.getType());
	}
	
	public static boolean isSoftBlock(Material b) {
		if (b==Material.SAND ||
				b==Material.DIRT ||
				b==Material.GRASS ||
				b==Material.GRAVEL ||
				b==Material.CLAY ||
				//b==Material.HARD_CLAY ||
				//b==Material.STAINED_CLAY ||
				b==Material.ENDER_STONE ||
				b==Material.SOIL ||
				b==Material.SNOW_BLOCK ||
				b==Material.SOUL_SAND ||
				b==Material.STONE ||
				b==Material.COBBLESTONE ||
				b==Material.NETHERRACK
				//b==Material.WOOL ||
				//b==Material.WOOD ||
				//b==Material.COAL_ORE ||
				//b==Material.DIAMOND_ORE ||
				//b==Material.GOLD_ORE ||
				//b==Material.IRON_ORE || 
				//b==Material.REDSTONE_ORE ||
				//b==Material.LAPIS_ORE || 
				//b==Material.EMERALD_ORE
				) {
			return true;
		} else { 
			return false;
		}
	}
	
	public static boolean isBankSign(Sign s) {
		return s.getLine(0).equalsIgnoreCase(ChatColor.AQUA+"-- BANK --");
	}

	public static boolean hasPermissionToBreakSign(Sign s, Player p) {
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
		return getEquipment(ent,false);
	}
	
	public static ItemStack[] getEquipment(LivingEntity ent, boolean offhand) {
		if (ent!=null) {
			if (offhand && (ent instanceof Player)) {
				Player p = (Player)ent;
				return new ItemStack[]{
						ent.getEquipment().getItemInMainHand(),
						p.getInventory().getExtraContents()[0],
						ent.getEquipment().getHelmet(),
						ent.getEquipment().getChestplate(),
						ent.getEquipment().getLeggings(),
						ent.getEquipment().getBoots()
					};
			} else {
				return new ItemStack[]{
						ent.getEquipment().getItemInMainHand(),
						ent.getEquipment().getHelmet(),
						ent.getEquipment().getChestplate(),
						ent.getEquipment().getLeggings(),
						ent.getEquipment().getBoots()
					};
			}
		} else {
			return new ItemStack[]{};
		}
	}
	
	public static ItemStack[] getArmor(LivingEntity ent) {
		return getArmor(ent,false);
	}
	
	public static ItemStack[] getArmor(LivingEntity ent, boolean offhand) {
		if (ent!=null) {
			if (offhand && (ent instanceof Player)) {
				Player p = (Player)ent;
				return new ItemStack[]{
						p.getInventory().getExtraContents()[0],
						ent.getEquipment().getHelmet(),
						ent.getEquipment().getChestplate(),
						ent.getEquipment().getLeggings(),
						ent.getEquipment().getBoots()
					};
			} else {
				return new ItemStack[]{
						ent.getEquipment().getHelmet(),
						ent.getEquipment().getChestplate(),
						ent.getEquipment().getLeggings(),
						ent.getEquipment().getBoots()
					};
			}
		} else {
			return new ItemStack[]{};
		}
	}

	public static void updateSetItemsInInventory(Inventory inv) {
		TwosideKeeper.log("Inventory is size "+inv.getSize(),5);
		for (ItemStack it : inv.getContents()) {
			if (it!=null) {
				TwosideKeeper.log("Checking "+it.toString(), 5);
				UpdateItemLore(it);
			}
		}
		if (inv.getHolder() instanceof Player) {
			Player p = (Player)inv.getHolder();
			for (ItemStack armor : GenericFunctions.getEquipment(p)) {GenericFunctions.UpdateArtifactItemType(armor);}
		}
	}
	
	public static ItemStack UpdateItemLore(ItemStack item) {
		if (RemoveInvalidItem(item)) {
			return item;
		}
		if (ItemSet.isSetItem(item)) {
			//Update the lore. See if it's hardened. If it is, we will save just that piece.
			//Save the tier and type as well.
			ItemSet set = ItemSet.GetSet(item);
			int tier = ItemSet.GetTier(item);
			item = UpdateSetLore(set,tier,item); 
		}
		UpdateOldRangerPieces(item);
		UpdateArtifactDust(item);
		UpdateVials(item);
		UpdateHuntersCompass(item);
		UpdateUpgradeShard(item);
		UpdateOldQuivers(item);
		UpdateItemCubeContentsList(item);
		UpdateArtifactTier(item);
		UpdateArtifactItemType(item);
		return item;
	}
	
	private static boolean RemoveInvalidItem(ItemStack item) {
		if (ItemUtils.isValidLoreItem(item) && ItemUtils.LoreContainsSubstring(item, "WorldShop Display Item")) {
			TwosideKeeper.log("Found an invalid item! DELETING", 0);
			item.setType(Material.AIR);
			item.setDurability((short)0);
			item.setAmount(0);
			return true;
		}
		return false;
	}

	private static void UpdateArtifactTier(ItemStack item) {
		TwosideKeeper.log("Checking "+item.toString(), 5);
		if (GenericFunctions.isOldArtifactEquip(item)) {
			TwosideKeeper.log("  OLD EQUIP DETECTED.", 1);
			//Remove the Luck of the Sea enchantment.
			int oldtier = item.getEnchantmentLevel(Enchantment.LUCK);
			item.removeEnchantment(Enchantment.LUCK);
			item=ItemUtils.addLoreLineUnderneathLineContainingSubstring(item, "Artifact Crafting Item", ChatColor.GOLD+""+ChatColor.BOLD+"T"+oldtier+" Artifact");
			TwosideKeeper.log("Converted an old artifact to "+item.toString(), 1);
		}
	}
	
	private static boolean isOldArtifactEquip(ItemStack item) {
		if (Artifact.isArtifact(item) &&
				isEquip(item) &&
				item.containsEnchantment(Enchantment.LUCK)) {
			return true;
		} else {
			return false;
		}
	}

	//TODO Item Cube Contents list.
	private static void UpdateItemCubeContentsList(ItemStack item) {
		if (ItemUtils.isValidLoreItem(item) &&
			item.getItemMeta().getLore().size()>=4 &&
			ItemUtils.LoreContainsSubstring(item, ChatColor.DARK_PURPLE+"ID#")) {
			//This is an item cube. Update its lore.
			int id = Integer.parseInt(ItemUtils.GetLoreLineContainingSubstring(item, ChatColor.DARK_PURPLE+"ID#").split("#")[1]);
			if (TwosideKeeper.itemcube_updates.containsKey(id)) {
				ItemUtils.DeleteAllLoreLinesAtAndAfterLineContainingSubstring(item, ChatColor.WHITE+"Contents (");
				ItemUtils.addLore(item, ChatColor.WHITE+"Contents ("+GetItemCubeSpace(id)+"):");
				for (ItemStack it : TwosideKeeper.itemcube_updates.get(id)) {
					ItemUtils.addLore(item, ChatColor.GRAY+" - "+GenericFunctions.UserFriendlyMaterialName(it)+(it.getAmount()>1?ChatColor.YELLOW+" x"+it.getAmount():""));
				}
			}

			if (item.getType()==Material.HOPPER_MINECART) {
				//Filter Cube. Add "Filtering" list.
				Hopper h = ItemCubeUtils.getFilterCubeHopper(id);
				Inventory inv = h.getInventory();
				ItemStack[] items = inv.getContents();
				ItemUtils.DeleteAllLoreLinesAtAndAfterLineContainingSubstring(item, ChatColor.AQUA+"               ");
				ItemUtils.addLore(item, ChatColor.AQUA+"               ");
				ItemUtils.addLore(item, ChatColor.AQUA+"Filtering:");
				for (ItemStack it : items) {
					if (ItemUtils.isValidItem(it)) {
						ItemUtils.addLore(item, ChatColor.DARK_AQUA+" - "+GenericFunctions.UserFriendlyMaterialName(it));
					}
				}
				return;
			}
			return;
		}
		if (BaublePouch.isBaublePouch(item)) {
			int id = BaublePouch.getBaublePouchID(item);
			List<ItemStack> items = BaublePouch.getBaublePouchContents(id);
			ItemUtils.DeleteAllLoreLinesAtAndAfterLineContainingSubstring(item, ChatColor.WHITE+"Contents (");
			ItemUtils.addLore(item, ChatColor.WHITE+"Contents ("+GetBaubleSpace(id)+"):");
			for (ItemStack it : items) {
				ItemUtils.addLore(item, ChatColor.GRAY+" - "+GenericFunctions.UserFriendlyMaterialName(it)+(it.getAmount()>1?ChatColor.YELLOW+" x"+it.getAmount():""));
			}
			return;
		}
	}

	private static String GetItemCubeSpace(int id) {
		List<ItemStack> items = ItemCubeUtils.getItemCubeContents(id);
		int count=0;
		for (ItemStack item : items) {
			if (ItemUtils.isValidItem(item)) {
				count++;
			}
		}
		return TextUtils.GetColorBasedOnPercent((items.size()-count)/(double)items.size())+""+count+ChatColor.RESET+"/"+items.size();
	}
	
	private static String GetBaubleSpace(int id) {
		int count=0;
		List<ItemStack> items = BaublePouch.getBaublePouchContents(id);
		for (ItemStack item : items) {
			if (ItemUtils.isValidItem(item)) {
				count++;
			}
		}
		return TextUtils.GetColorBasedOnPercent((items.size()-count)/9d)+""+count+ChatColor.RESET+"/9";
	}

	private static void UpdateOldQuivers(ItemStack item) {
		if (item!=null &&
				item.getType()==Material.TIPPED_ARROW &&
				item.getEnchantmentLevel(Enchantment.ARROW_INFINITE)==5) {
			//This might be an old arrow quiver.
			if (!ArrowQuiver.isValidQuiver(item)) {
				//Okay, we convert this with a brand new ID.
				int amt = playerGetOldArrowQuiverAmt(item);
				ItemMeta m = CustomItem.ArrowQuiver().getItemMeta();
				item.setItemMeta(m);
				ArrowQuiver.setID(item);
				item.addUnsafeEnchantments(CustomItem.ArrowQuiver().getEnchantments());
				ArrowQuiver.addContents(ArrowQuiver.getID(item), new ItemStack(Material.ARROW,amt));
				ArrowQuiver.updateQuiverLore(item);
			}
		}
	}
	
	/**
	 * Legacy code to help turn an old arrow quiver into a new one. Gets the amount of arrows in an old quiver.
	 */
	private static int playerGetOldArrowQuiverAmt(ItemStack ArrowQuiver) {
		int ArrowQuiver_amt = Integer.parseInt(ArrowQuiver.getItemMeta().getLore().get(1).split(": "+ChatColor.YELLOW)[1]);
		return ArrowQuiver_amt;
	}

	public static void UpdateArtifactItemType(ItemStack item) {
		if (isArtifactArmor(item) &&
				item.getType()!=Material.SULPHUR) {
			double durabilityratio = (double)item.getDurability()/item.getType().getMaxDurability(); 
			item.setType(Material.valueOf("LEATHER_"+item.getType().name().split("_")[1]));
			item.setDurability((short)(durabilityratio*item.getType().getMaxDurability()));
			UpdateDisplayedEnchantments(item);
		}
	}

	private static void UpdateDisplayedEnchantments(ItemStack item) {
		ItemMeta m = item.getItemMeta();
		m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		m.setLore(ClearAllPreviousEnchantmentLines(m.getLore()));
		item.setItemMeta(m);
		AddNewEnchantmentLines(item);
	}

	private static void AddNewEnchantmentLines(ItemStack item) {
		Set<Enchantment> map = item.getEnchantments().keySet();
		ItemMeta m = item.getItemMeta();
		List<String> lore = m.getLore();
		int artifact_lv = ArtifactUtils.getArtifactTier(item);
		for (Enchantment e : map) {
			int lv = item.getEnchantments().get(e);
			lore.add(0," "+ChatColor.BLACK+ChatColor.WHITE+ChatColor.GRAY+WorldShop.getRealName(e)+" "+WorldShop.toRomanNumeral(lv));
		}
		lore.add(0,ChatColor.GOLD+""+ChatColor.BLACK+ChatColor.GOLD+"Tier "+artifact_lv+ChatColor.RESET+ChatColor.GOLD+" "+GenericFunctions.UserFriendlyMaterialName(item.getType())+" Artifact");
		m.setLore(lore);
		item.setItemMeta(m);
	}

	private static List<String> ClearAllPreviousEnchantmentLines(List<String> lore) {
		for (int i=0;i<lore.size();i++) {
			if (lore.get(i).contains(" "+ChatColor.BLACK+ChatColor.WHITE+ChatColor.GRAY) ||
					lore.get(i).contains(ChatColor.GOLD+""+ChatColor.BLACK+ChatColor.GOLD+"Tier ")) {
				lore.remove(i);
				i--;
			}
		}
		return lore;
	}

	private static void UpdateHuntersCompass(ItemStack item) {
		if (item.getType()==Material.COMPASS &&
				item.containsEnchantment(Enchantment.LUCK)) {
			item.setItemMeta(TwosideKeeper.HUNTERS_COMPASS.getItemStack().getItemMeta());
		}
	}
	
	private static void UpdateUpgradeShard(ItemStack item) {
		if (isUpgradeShard(item)) {
			//item.setItemMeta(TwosideKeeper.UPGRADE_SHARD.getItemStack().getItemMeta());
			int tier = getUpgradeShardTier(item); //This forces the tier to appear.
			ItemMeta m = item.getItemMeta();
			m.setDisplayName(ChatColor.GREEN+"T"+tier+" Upgrade Shard");
			item.setItemMeta(m);
		}
	}

	private static void UpdateVials(ItemStack item) {
		if (item!=null && item.getType()==Material.POTION) {
			if (item.getItemMeta().hasLore() &&
					item.getItemMeta().getLore().contains("A fantastic potion, it comes straight")) {
				//This is a special potion. Attempt to update it.
				boolean newpotion=false;
				List<String> lore = item.getItemMeta().getLore();
				for (String lo : lore) {
					if (lo.contains(ChatColor.GRAY+"")) {
						newpotion=true;
						break;
					}
				}
				if (!newpotion) {
					item = AddCustomPotionTag(item);
				}
			}
		}
	}

	public static ItemStack AddCustomPotionTag(ItemStack item) {
		if (item!=null && 
				item.hasItemMeta() &&
				item.getItemMeta() instanceof PotionMeta) {
			List<String> lore = item.getItemMeta().getLore();
			PotionMeta pm = (PotionMeta)item.getItemMeta();
			for (PotionEffect pe : pm.getCustomEffects()) {
				lore.add(0,ChatColor.GRAY+UserFriendlyPotionEffectTypeName(pe.getType())+" "+WorldShop.toRomanNumeral(pe.getAmplifier()+1)+" ("+WorldShop.toReadableDuration(pe.getDuration())+")");
			}
			pm.setLore(lore);
			pm.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
			item.setItemMeta(pm);
		}
		return item;
	}

	private static void UpdateArtifactDust(ItemStack item) {
		if (Artifact.isArtifact(item) &&
				item.getType()==Material.SULPHUR) {
			item = convertArtifactDustToItem(item);
		}
	}

	private static ItemStack UpdateSetLore(ItemSet set, int tier, ItemStack item) {
		List<String> newlore = new ArrayList<String>();
		
		if (GenericFunctions.isHardenedItem(item)) {
			newlore.add(ChatColor.GRAY+"Breaks Remaining: "+ChatColor.YELLOW+GenericFunctions.getHardenedItemBreaks(item));
		}
		newlore.addAll(ItemSet.GenerateLore(set, tier));
		ItemMeta m = item.getItemMeta();
		m.setLore(newlore);
		item.setItemMeta(m);
		return item;
	}

	private static void UpdateOldRangerPieces(ItemStack item) {
		if (item!=null
				&& item.getType()!=Material.AIR &&
				item.hasItemMeta() && item.getItemMeta().hasLore()) {
			if (item.getItemMeta().getLore().contains(ChatColor.GOLD+""+ChatColor.BOLD+"Jamdak Set") ||
					item.getItemMeta().getLore().contains(ChatColor.GOLD+""+ChatColor.BOLD+"Darnys Set") ||
					item.getItemMeta().getLore().contains(ChatColor.GOLD+""+ChatColor.BOLD+"Alikahn Set") ||
					item.getItemMeta().getLore().contains(ChatColor.GOLD+""+ChatColor.BOLD+"Lorasaadi Set")) {
				//This is an old set item. Update it to the new set piece.
				ItemSet set = null;
				if (item.getItemMeta().getLore().contains(ChatColor.GOLD+""+ChatColor.BOLD+"Jamdak Set")) {
					set = ItemSet.JAMDAK;
				}
				if (item.getItemMeta().getLore().contains(ChatColor.GOLD+""+ChatColor.BOLD+"Darnys Set")) {
					set = ItemSet.DARNYS;
				}
				if (item.getItemMeta().getLore().contains(ChatColor.GOLD+""+ChatColor.BOLD+"Alikahn Set")) {
					set = ItemSet.ALIKAHN;
				}
				if (item.getItemMeta().getLore().contains(ChatColor.GOLD+""+ChatColor.BOLD+"Lorasaadi Set")) {
					set = ItemSet.LORASAADI;
				}
				int tier = 1;
				UpdateSetLore(set,tier,item);
			}
			if (TwosideKeeperAPI.getItemSet(item)!=null && item.getType()!=Material.LEATHER && item.getType().name().contains("LEATHER")) {
				TwosideKeeper.log("In here",5);
				LeatherArmorMeta lm = (LeatherArmorMeta)item.getItemMeta();
				if (lm.getColor().equals(Bukkit.getServer().getItemFactory().getDefaultLeatherColor())) {
					TwosideKeeper.log("->In here",5);
					ItemSet set = TwosideKeeperAPI.getItemSet(item);
					ConvertSetColor(item, set);
				}
			}
		}
	}

	public static void ConvertSetColor(ItemStack item, ItemSet set) {
		if (item.getType().name().contains("LEATHER_")) {
			if (set==ItemSet.JAMDAK) {
				LeatherArmorMeta lm = (LeatherArmorMeta)item.getItemMeta();
				lm.setColor(org.bukkit.Color.fromRGB(128, 64, 0));
				item.setItemMeta(lm);
			}
			if (set==ItemSet.DARNYS) {
				LeatherArmorMeta lm = (LeatherArmorMeta)item.getItemMeta();
				lm.setColor(org.bukkit.Color.fromRGB(224, 224, 224));
				item.setItemMeta(lm);
			}
			if (set==ItemSet.ALIKAHN) {
				LeatherArmorMeta lm = (LeatherArmorMeta)item.getItemMeta();
				lm.setColor(org.bukkit.Color.fromRGB(64, 0, 64));
				item.setItemMeta(lm);
			}
			if (set==ItemSet.LORASAADI) {
				LeatherArmorMeta lm = (LeatherArmorMeta)item.getItemMeta();
				lm.setColor(org.bukkit.Color.fromRGB(0, 64, 0));
				item.setItemMeta(lm);
			}
		}
	}

	public static ExperienceOrb spawnXP(Location location, int expAmount) {
        ExperienceOrb orb = location.getWorld().spawn(location, ExperienceOrb.class);
        orb.setExperience(orb.getExperience() + expAmount);
        return orb;
    }

	public static boolean AttemptRevive(Player p, double dmg, String reason) {
		boolean revived=false;
		if (p.getHealth()<=dmg) {
			//This means we would die from this attack. Attempt to revive the player.
			//Check all artifact armor for a perk.
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			pd.lastdamagetaken=dmg;
			pd.lasthitdesc=reason;
			pd.slayermodehp = p.getMaxHealth();
			
			ItemStack[] equips = p.getEquipment().getArmorContents();
			ItemStack[] hotbar = GenericFunctions.getBaubles(p);
			
			if (ItemSet.HasSetBonusBasedOnSetBonusCount(hotbar, p, ItemSet.GLADOMAIN, 5) && 
					pd.lastlifesavertime+GenericFunctions.GetModifiedCooldown(TwosideKeeper.LIFESAVER_COOLDOWN, p)<=TwosideKeeper.getServerTickTime()) {
				pd.lastlifesavertime=TwosideKeeper.getServerTickTime();
				RevivePlayer(p,p.getMaxHealth());
				if (PlayerMode.getPlayerMode(p)==PlayerMode.SLAYER) {GenericFunctions.applyStealth(p,false);}
				GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.SPEED, 20*10, 3, p, true);
				deAggroNearbyTargets(p);
				revived=true;
				Bukkit.broadcastMessage(ChatColor.GOLD+p.getName()+ChatColor.WHITE+" should've died but managed to live!");
				aPlugin.API.discordSendRawItalicized(ChatColor.GOLD+p.getName()+ChatColor.WHITE+" should've died but managed to live!");
				aPlugin.API.sendCooldownPacket(p, Material.SKULL_ITEM, GenericFunctions.GetModifiedCooldown(TwosideKeeper.LIFESAVER_COOLDOWN, p));
				aPlugin.API.sendCooldownPacket(p, Material.CHORUS_FLOWER, GenericFunctions.GetModifiedCooldown(TwosideKeeper.LIFESAVER_COOLDOWN, p));
				return true;
			}
			
			List<ItemStack> equips_with_survivor = new ArrayList<ItemStack>();
			for (int i=0;i<equips.length;i++) {
				if (isArtifactEquip(equips[i]) && ArtifactAbility.containsEnchantment(ArtifactAbility.SURVIVOR, equips[i])) {
					equips_with_survivor.add(equips[i]);
				}
			}
			if (equips_with_survivor.size()>0) {
				ItemStack equip = equips_with_survivor.get((int)(Math.random()*equips_with_survivor.size()));
				//We can revive!
				RevivePlayer(p, Math.min(p.getMaxHealth()*(getAbilityValue(ArtifactAbility.SURVIVOR,equip)/100d),p.getMaxHealth()));
				ArtifactAbility.removeEnchantment(ArtifactAbility.SURVIVOR, equip);
				//AwakenedArtifact.setLV(equip, AwakenedArtifact.getLV(equip)-1, p);
				AwakenedArtifact.setMaxAP(equip, AwakenedArtifact.getMaxAP(equip)-1);
				revived=true;
				Bukkit.broadcastMessage(ChatColor.GOLD+p.getName()+ChatColor.WHITE+" survived a brutal attack and managed to come back to life!");
				aPlugin.API.discordSendRawItalicized(ChatColor.GOLD+p.getName()+ChatColor.WHITE+" survived a brutal attack and managed to come back to life!");
				return true;
			}
			
			RandomlyBreakBaubles(p);
		}
		return revived;
	}

	//TODO Fix Bauble Breaking.
	public static void RandomlyBreakBaubles(Player p) {
		/*for (int i=0;i<9;i++) {
			ItemSet set = ItemSet.GetSet(hotbar[i]);
			if (set!=null) {
				if (set==ItemSet.GLADOMAIN ||
						set==ItemSet.MOONSHADOW) {
					if (Math.random()<=1/8d) {
						BreakBauble(p,i);
					}
				}
			}
		}*/
		ItemStack pouch = p.getEquipment().getItemInOffHand();
		TwosideKeeper.log("Checking "+pouch.toString(), 0);
		if (BaublePouch.isBaublePouch(pouch)) {
			Dropper d = BaublePouch.getBaublePouchDropper(BaublePouch.getBaublePouchID(pouch));
			Inventory inv = d.getInventory();
			for (int i=0;i<inv.getContents().length;i++) {
				ItemStack bauble = inv.getContents()[i];
					ItemSet set = ItemSet.GetSet(bauble);
					if (set!=null &&
							(set==ItemSet.GLADOMAIN ||
							set==ItemSet.MOONSHADOW ||
							set==ItemSet.ALUSTINE ||
							set==ItemSet.WOLFSBANE)) {
						double basechance = 1/8d;
						if (set==ItemSet.WOLFSBANE) {
							basechance += 0.0d * ItemSet.GetTier(bauble);
						}
						if (set==ItemSet.ALUSTINE) {
							basechance += 1/16d * ItemSet.GetTier(bauble);
						}
						if (set==ItemSet.MOONSHADOW) {
							basechance += 1/8d * ItemSet.GetTier(bauble);
						}
						if (set==ItemSet.GLADOMAIN) {
							basechance += 1/4d * ItemSet.GetTier(bauble);
						}
						if (Math.random()<=basechance) {
							if (GenericFunctions.isHardenedItem(bauble)) {
								int breaks = GenericFunctions.getHardenedItemBreaks(bauble);
								if (breaks>0) {
									inv.setItem(i, GenericFunctions.addHardenedItemBreaks(bauble, -1));
								} else {
									p.sendMessage(ChatColor.GOLD+""+ChatColor.BOLD+"Unlucky! "+ChatColor.RESET+ChatColor.DARK_RED+"Your "+ChatColor.YELLOW+((bauble.hasItemMeta() && bauble.getItemMeta().hasDisplayName())?bauble.getItemMeta().getDisplayName():GenericFunctions.UserFriendlyMaterialName(bauble))+ChatColor.DARK_RED+" has broken!");
									inv.setItem(i, new ItemStack(Material.AIR));
									GenericFunctions.UpdateItemLore(pouch);
								}
								SoundUtils.playLocalSound(p, Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
							} else {
								p.sendMessage(ChatColor.GOLD+""+ChatColor.BOLD+"Unlucky! "+ChatColor.RESET+ChatColor.DARK_RED+"Your "+ChatColor.YELLOW+((bauble.hasItemMeta() && bauble.getItemMeta().hasDisplayName())?bauble.getItemMeta().getDisplayName():GenericFunctions.UserFriendlyMaterialName(bauble))+ChatColor.DARK_RED+" has broken!");
								inv.setItem(i, new ItemStack(Material.AIR));
								GenericFunctions.UpdateItemLore(pouch);
								SoundUtils.playLocalSound(p, Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
							}
						}
					}
				}
			}
		}

	/*private static void BreakBauble(Player p, int i) {
		ItemStack item = p.getInventory().getContents()[i];
		if (GenericFunctions.isHardenedItem(item)) {
			int breaks = GenericFunctions.getHardenedItemBreaks(item);
			if (breaks>0) {
				p.getInventory().setItem(i,);
				return;
			}
		}
		p.getInventory().setItem(i, new ItemStack(Material.AIR));
		SoundUtils.playLocalSound(p, Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
		p.sendMessage(ChatColor.GOLD+""+ChatColor.BOLD+"Unlucky! "+ChatColor.RESET+ChatColor.DARK_RED+"Your "+ChatColor.YELLOW+((item.hasItemMeta() && item.getItemMeta().hasDisplayName())?item.getItemMeta().getDisplayName():GenericFunctions.UserFriendlyMaterialName(item))+ChatColor.DARK_RED+" has broken!");
	}*/

	public static void deAggroNearbyTargets(Player p) {
		//List<Monster> monsters = getNearbyMobs(p.getLocation(),8);
		List<Monster> monsters = CustomDamage.trimNonMonsterEntities(p.getNearbyEntities(24, 24, 24));
		for (Monster m : monsters) {
			if (m.getTarget()!=null &&
					m.getTarget().equals(p) &&
					!m.hasPotionEffect(PotionEffectType.GLOWING)) {
				m.setTarget(null);
			}
		}
	}

	public static void RevivePlayer(Player p, double healdmg) {
		p.setHealth(Math.min(healdmg,p.getMaxHealth()));
		SoundUtils.playLocalSound(p, Sound.BLOCK_REDSTONE_TORCH_BURNOUT, 1.0f, 1.5f);
		for (PotionEffect eff : p.getActivePotionEffects()) {
			if (isBadEffect(eff.getType())) {
				Bukkit.getScheduler().scheduleSyncDelayedTask(TwosideKeeper.plugin, 
	            () -> {
					logAndRemovePotionEffectFromEntity(eff.getType(),p);
	            }, 1); 
			}
		}
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		pd.slayermodehp = Math.min(healdmg,p.getMaxHealth());
		pd.vendetta_amt=0;
		pd.lastvendettastack=0;
		pd.thorns_amt=0;
		p.setFireTicks(0);
		CustomDamage.addIframe(40, p);
		GenericFunctions.sendActionBarMessage(p, "");
		//p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING,20,0));
		//TwosideKeeper.log("Added "+20+" glowing ticks to "+p.getName()+" for reviving.",3);
		//p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION,20,0));
	}

	public static void DealExplosionDamageToEntities(Location l, double basedmg, double range) {
		DealExplosionDamageToEntities(l, basedmg, range, null);
	}
	
	public static void DealExplosionDamageToEntities(Location l, double basedmg, double range, Entity damager) {
		//nearbyentities.addAll();
		final double rangeSquared=range*range;
		for (Entity ent: l.getWorld().getNearbyEntities(l, range, range, range)) {
			if (ent instanceof LivingEntity) {
				//double damage_mult = 2.0d/(l.distance(nearbyentities.get(i).getLocation())+1.0);
				double dmg;
				double damage_mult=Math.max(0d, 1 - l.distanceSquared(ent.getLocation())/rangeSquared);
				damage_mult*=TwosideKeeper.EXPLOSION_DMG_MULT;
				damage_mult*=CalculateBlastResistance((LivingEntity)ent);
				TwosideKeeper.log("dmg mult is "+damage_mult,4);
				dmg = basedmg * damage_mult;
				if (ent instanceof Player) {TwosideKeeper.log("Damage is "+dmg, 5);}
				CustomDamage.ApplyDamage(dmg, damager, (LivingEntity)ent, null, "Explosion", CustomDamage.NONE);
				//subtractHealth((LivingEntity)nearbyentities.get(i),null,NewCombat.CalculateDamageReduction(dmg, (LivingEntity)nearbyentities.get(i), null));
			}
		}
		TwosideKeeper.log("In here", 5);
		//We cleared the non-living entities, deal damage to the rest.
	}

	public static double CalculateFallResistance(LivingEntity l) {
		int featherfalllv = 0;
		ItemStack[] equips = GenericFunctions.getArmor(l);
		for (int i=0;i<equips.length;i++) {
			//TwosideKeeper.log("Checking piece "+equips[i], 0);
			if (equips[i]!=null && equips[i].getType()!=Material.AIR && equips[i].containsEnchantment(Enchantment.PROTECTION_FALL)) {
				featherfalllv+=equips[i].getEnchantmentLevel(Enchantment.PROTECTION_FALL)+1;
				//TwosideKeeper.log("Detected Feather Falling "+(equips[i].getEnchantmentLevel(Enchantment.PROTECTION_FALL)+1), 0);
			}
		}
		return 1-(featherfalllv*0.01);
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
				SoundUtils.playLocalSound(p, Sound.ITEM_ARMOR_EQUIP_LEATHER, 1.0f, 1.0f);
			}break;
			case IRON_HELMET:
			case IRON_CHESTPLATE:
			case IRON_LEGGINGS:
			case IRON_BOOTS:{
				SoundUtils.playLocalSound(p, Sound.ITEM_ARMOR_EQUIP_IRON, 1.0f, 1.0f);
			}break;
			case GOLD_HELMET:
			case GOLD_CHESTPLATE:
			case GOLD_LEGGINGS:
			case GOLD_BOOTS:{
				SoundUtils.playLocalSound(p, Sound.ITEM_ARMOR_EQUIP_GOLD, 1.0f, 1.0f);
			}break;
			case DIAMOND_HELMET:
			case DIAMOND_CHESTPLATE:
			case DIAMOND_LEGGINGS:
			case DIAMOND_BOOTS:{
				SoundUtils.playLocalSound(p, Sound.ITEM_ARMOR_EQUIP_DIAMOND, 1.0f, 1.0f);
			}break;
			default:{
				SoundUtils.playLocalSound(p, Sound.ITEM_ARMOR_EQUIP_GENERIC, 1.0f, 1.0f);
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

	public static void setGlowing(LivingEntity m, Color color) {
		/*
		for (Player p : Bukkit.getOnlinePlayers()) {
			GlowAPI.setGlowing(m, false, p);
			if (!GlowAPI.isGlowing(m, p)) {
				GlowAPI.setGlowing(m, color, p);
			}
		}*/
		LivingEntityStructure.getLivingEntityStructure(m).setGlobalGlow(color);
	}
	
	public static void DealDamageToNearbyPlayers(Location l, double basedmg, int range, boolean knockup, double knockupamt, Entity damager, String reason, boolean truedmg) {
		List<Player> players = getNearbyPlayers(l,range);
		//We cleared the non-living entities, deal damage to the rest.
		for (Player p : players) {
			//TwosideKeeperAPI.DealDamageToEntity(NewCombat.CalculateDamageReduction(((fullcalculation)?NewCombat.CalculateWeaponDamage(damager, p):1.0)*basedmg,p,null), (Player)players.get(i), damager);
			/*if (knockup && p.getHealth()>0) { //Prevent knockups if we die to the attack.
				p.setVelocity(new Vector(0,knockupamt,0));
			}*/
			if (CustomDamage.ApplyDamage(basedmg, damager, p, null, reason, (truedmg)?CustomDamage.TRUEDMG:CustomDamage.NONE)) {
				if (knockup && p.getHealth()>0) { //Prevent knockups if we die to the attack.
					p.setVelocity(new Vector(0,knockupamt,0));
				}
			}
		}
	}
	
	public static void DealDamageToNearbyMobs(Location l, double basedmg, int range, Entity damager, int flags) {
		List<LivingEntity> nearbyentities = getNearbyMobs(l,range);
		for (LivingEntity ent : nearbyentities) {
			if (!(ent instanceof Player)) {
				CustomDamage.ApplyDamage(basedmg, damager, ent, null, "Blitzen Lightning Strike", flags);
			}
		}
	}
	
	public static void DealDamageToNearbyMobs(Location l, double basedmg, int range, boolean knockup, double knockupamt, Entity damager, ItemStack weapon, boolean isLineDrive) {
		DealDamageToNearbyMobs(l,basedmg,range,knockup,knockupamt,damager,weapon,isLineDrive,(isLineDrive)?"Line Drive":null);
	}

	public static void DealDamageToNearbyMobs(Location l, double basedmg, double range, boolean knockup, double knockupamt, Entity damager, ItemStack weapon, boolean isLineDrive, String reason) {
		Collection<Entity> ents = l.getWorld().getNearbyEntities(l, range, range, range);
		//We cleared the non-living entities, deal damage to the rest.
		double origdmg = basedmg;
		for (Entity e : ents) {
			if (e instanceof LivingEntity && !(e instanceof Player) && !e.equals(damager)) {
				LivingEntity m = (LivingEntity)e;
				if (enoughTicksHavePassed(m,(Player)damager)) {
					basedmg=origdmg;
					boolean isForcefulStrike = (reason!=null && reason.equalsIgnoreCase("forceful strike"));
					boolean isSweepUp = (reason!=null && reason.equalsIgnoreCase("sweep up"));
					if (isSweepUp) {
						aPlugin.API.sendSoundlessExplosion(m.getLocation(), 1.5f);
						if (damager instanceof Player) {
							Player p = (Player)damager;
							p.playEffect(m.getLocation(), Effect.LAVA_POP, null);
						}
					}
					if (isForcefulStrike) {
						GenericFunctions.addSuppressionTime(m, 20*2);
					}
					if (isLineDrive) {
	    				basedmg*=1.0d+(4*((CustomDamage.getPercentHealthMissing(m))/100d));
						if (CustomDamage.ApplyDamage(basedmg, damager, m, weapon, "Line Drive")) {
							if (knockup) {
								m.setVelocity(new Vector(0,knockupamt,0));
							}
						}
					} else {
						if (CustomDamage.ApplyDamage(basedmg, damager, m, weapon, reason)) {
							if (knockup) {
								m.setVelocity(new Vector(0,knockupamt,0));
							}
						}
					}
					//TwosideKeeperAPI.DealDamageToEntity(basedmg, m, damager,"Line Drive");
					if (m.isDead() && isLineDrive) {
						Player p = (Player)damager;
						PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
						pd.last_strikerspell = pd.last_strikerspell-40;
						aPlugin.API.sendCooldownPacket(p, p.getEquipment().getItemInMainHand(), GetRemainingCooldownTime(p, pd.last_strikerspell, TwosideKeeper.LINEDRIVE_COOLDOWN));
					}
					updateNoDamageTickMap(m,(Player)damager);
				}
			}
		}
	}

	public static int GetRemainingCooldownTime(Player p, long current_cooldown, int cooldown_time) {
		return (int)(GetModifiedCooldown(cooldown_time,p)-(TwosideKeeper.getServerTickTime()-current_cooldown));
	}
	
	public static List<LivingEntity> getNearbyMobs(Location l, int range) {
		Collection<Entity> ents = l.getWorld().getNearbyEntities(l, range, range, range);
		List<LivingEntity> monsterlist = new ArrayList<LivingEntity>();
		for (Entity e : ents) {
			if ((e instanceof LivingEntity)) {
				monsterlist.add((LivingEntity)e);
			}
		}
		return monsterlist;
	}
	
	public static List<LivingEntity> getNearbyMonsters(Location l, int range) {
		Collection<Entity> ents = l.getWorld().getNearbyEntities(l, range, range, range);
		List<LivingEntity> monsterlist = new ArrayList<LivingEntity>();
		for (Entity e : ents) {
			if ((e instanceof LivingEntity) && !(e instanceof Player)) {
				monsterlist.add((LivingEntity)e);
			}
		}
		return monsterlist;
	}
	
	public static List<Player> getNearbyPlayers(Location l, int range) {
		List<Player> players = new ArrayList<Player>();
		Collection<Entity> nearbyentities = l.getWorld().getNearbyEntities(l, range, range, range);
		for (Entity i : nearbyentities) {
			if ((i instanceof Player)) {
				players.add((Player)i);
			}
		}
		return players;		
	}

	public static boolean isEliteMonster(LivingEntity m) {
		LivingEntityStructure md = LivingEntityStructure.getLivingEntityStructure(m);
		return md.getElite();
	}

	public static EliteMonster getEliteMonster(LivingEntity m) {
		for (EliteMonster em : TwosideKeeper.elitemonsters) {
			if (em.getMonster().equals(m)) {
				return em;
			}
		}
		return null;
	}
	
	public static Location defineNewEliteLocation() {
		int randomx = (int)((Math.random()*10000) - 5000); 
		int randomz = (int)((Math.random()*10000) - 5000);
		Location testloc = new Location(Bukkit.getWorld("world"),randomx,96,randomz);
		testloc.getChunk().load();
		randomx = (int)((Math.random()*10000) - 5000);
		randomz = (int)((Math.random()*10000) - 5000);
		testloc = new Location(Bukkit.getWorld("world"),randomx,96,randomz);
		testloc.getChunk().load();
		if ((testloc.getBlock().getType()!=Material.AIR || testloc.getBlock().getRelative(0, 1, 0).getType()!=Material.AIR) &&
				AllNaturalBlocks(testloc.getBlock(),16,8,16)) {
			return new Location(Bukkit.getWorld("world"),randomx,testloc.getBlockY(),randomz);
		} else {
			testloc.getChunk().unload(false);
			TwosideKeeper.log("Failed location "+testloc.toString(), 4);
		}
		return null;
	}
	
	public static void generateNewElite(Player p, String name) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(TwosideKeeper.plugin, new EliteMonsterLocationFinder(p,name), 2l);
	}
	
	public static boolean isHunterCompass(ItemStack item) {
		if (item!=null &&
				item.getType()==Material.COMPASS &&
				item.containsEnchantment(Enchantment.LUCK)) {
			return true;
		} else {
			return false;
		}
	}

	public static Entity getNearestMonster(LivingEntity ent) {
		List<Entity> entities = ent.getNearbyEntities(16, 16, 16);
		List<Monster> ents = CustomDamage.trimNonMonsterEntities(entities);
		double closest=9999999d;
		Monster m = null;
		for (Monster enti : ents) {
			double distance = enti.getLocation().distanceSquared(ent.getLocation());
			if (distance<closest) {
				closest = distance;
				m = enti;
			}
		}
		return m;
	}

	public static boolean AllNaturalBlocks(Block b, int x, int y, int z) {
		for (int i=-x/2;i<x/2+1;i++) {
			for (int j=-y/2;j<y/2+1;j++) {
				for (int k=-z/2;k<z/2+1;k++) {
					if (!isNaturalBlock(b.getRelative(i, j, k))) {
						return false;
					}
				}
			}
		}
		return true;
	}

	public static boolean isNaturalBlock(Block b) {
		if (b.getType()==Material.DIRT ||
				b.getType()==Material.SOIL ||
				b.getType()==Material.MYCEL ||
				b.getType()==Material.SAND ||
				b.getType()==Material.SANDSTONE ||
				b.getType()==Material.AIR ||
				b.getType()==Material.CLAY ||
				b.getType()==Material.GRASS ||
				b.getType()==Material.STONE ||
				/*b.getType()==Material.WATER ||
				b.getType()==Material.LAVA ||*/
				b.getType()==Material.NETHERRACK ||
				b.getType()==Material.ENDER_STONE ||
				b.getType()==Material.COBBLESTONE ||
				b.getType()==Material.MOSSY_COBBLESTONE ||
				b.getType()==Material.LOG ||
				b.getType()==Material.LOG_2 ||
				b.getType()==Material.LEAVES ||
				b.getType()==Material.LEAVES_2 ||
				/*b.getType()==Material.STATIONARY_LAVA ||
				b.getType()==Material.STATIONARY_WATER ||*/
				b.getType()==Material.SNOW ||
				b.getType()==Material.ICE ||
				b.getType()==Material.LONG_GRASS ||
				b.getType()==Material.YELLOW_FLOWER ||
				b.getType()==Material.PACKED_ICE) {
			return true;
		}
		return false;
	}
	
	public static boolean giveItem(Player p, ItemStack... items) {
        HashMap<Integer,ItemStack> remaining = p.getInventory().addItem(items);
        if (remaining.isEmpty()) {
        	if (items[0]!=null) {
        		SoundUtils.playGlobalSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.6f, SoundUtils.DetermineItemPitch(items[0]));
        	}
            return true;
        } else {
            for (Integer i : remaining.keySet()) {
                Item it = p.getWorld().dropItemNaturally(p.getLocation(), remaining.get(i));
                it.setInvulnerable(true);
                it.setPickupDelay(0);
            }
            return false;
        }
    }

	public static void RandomlyCreateFire(Location loc, int size) {
		Block b = loc.getBlock();
		//Pick a random block
		for (int i=-size;i<size+1;i++) {
			for (int j=-size;j<size+1;j++) {
				for (int k=-size;k<size+1;k++) {
					TwosideKeeper.log("Block "+i+","+j+","+k+" is "+b.getType(),5);
					Block testblock = b.getRelative(i, j, k);
					if (testblock!=null && testblock.getType()==Material.AIR &&
							testblock.getRelative(i,j-1,k)!=null &&
									testblock.getRelative(i,j-1,k).getType()!=Material.AIR) {
						//Random chance this will be set on fire.
						if (Math.random()<=0.03) {
							testblock.setType(Material.FIRE);
						}
					}
				}
			}
		}
	}
	
	public static Location FindRandomFreeLocation(Location loc) {
		Location testloc = loc;
		if ((testloc.getBlock().getType()==Material.PORTAL ||
				testloc.getBlock().getType()==Material.ENDER_PORTAL ||
				testloc.getBlock().getType()==Material.OBSIDIAN) ||
				(testloc.getBlock().getRelative(0, 0, 1).getType()==Material.PORTAL ||
				testloc.getBlock().getRelative(0, 0, 1).getType()==Material.ENDER_PORTAL ||
				testloc.getBlock().getRelative(0, 0, 1).getType()==Material.OBSIDIAN) ||
				(testloc.getBlock().getRelative(0, 0, -1).getType()==Material.PORTAL ||
				testloc.getBlock().getRelative(0, 0, -1).getType()==Material.ENDER_PORTAL ||
				testloc.getBlock().getRelative(0, 0, -1).getType()==Material.OBSIDIAN) ||
				(testloc.getBlock().getRelative(-1, 0, 0).getType()==Material.PORTAL ||
				testloc.getBlock().getRelative(-1, 0, 0).getType()==Material.ENDER_PORTAL ||
				testloc.getBlock().getRelative(-1, 0, 0).getType()==Material.OBSIDIAN) ||
				(testloc.getBlock().getRelative(1, 0, 0).getType()==Material.PORTAL ||
				testloc.getBlock().getRelative(1, 0, 0).getType()==Material.ENDER_PORTAL ||
				testloc.getBlock().getRelative(1, 0, 0).getType()==Material.OBSIDIAN) ||
				(testloc.getBlock().getRelative(0, 0, 2).getType()==Material.PORTAL ||
				testloc.getBlock().getRelative(0, 0, 2).getType()==Material.ENDER_PORTAL ||
				testloc.getBlock().getRelative(0, 0, 2).getType()==Material.OBSIDIAN) ||
				(testloc.getBlock().getRelative(0, 0, -2).getType()==Material.PORTAL ||
				testloc.getBlock().getRelative(0, 0, -2).getType()==Material.ENDER_PORTAL ||
				testloc.getBlock().getRelative(0, 0, -2).getType()==Material.OBSIDIAN) ||
				(testloc.getBlock().getRelative(-2, 0, 0).getType()==Material.PORTAL ||
				testloc.getBlock().getRelative(-2, 0, 0).getType()==Material.ENDER_PORTAL ||
				testloc.getBlock().getRelative(-2, 0, 0).getType()==Material.OBSIDIAN) ||
				(testloc.getBlock().getRelative(2, 0, 0).getType()==Material.PORTAL ||
				testloc.getBlock().getRelative(2, 0, 0).getType()==Material.ENDER_PORTAL ||
				testloc.getBlock().getRelative(2, 0, 0).getType()==Material.OBSIDIAN) ||
				testloc.getBlock().getRelative(0, 1, 0).getType()!=Material.AIR ||
				!testloc.getBlock().getRelative(0, 0, 0).getType().isSolid()) {
			do {
				testloc = testloc.add(0.5-Math.random()*1,0.5-Math.random()*1,0.5-Math.random()*1);
				TwosideKeeper.log("Testing block "+testloc.getBlock().getType(), 2);
			} while ((testloc.getBlock().getType()==Material.PORTAL ||
					testloc.getBlock().getType()==Material.ENDER_PORTAL) ||
			testloc.getBlock().getRelative(0, 1, 0).getType()!=Material.AIR);
		}
		return testloc.getBlock().getLocation().add(0.5, 1.0, 0.5);
	}
	
	public static boolean isUpgradeShard(ItemStack item) {
		return (item.getType()==Material.PRISMARINE_SHARD &&
				item.containsEnchantment(Enchantment.LUCK));
	}
	
	public static int getUpgradeShardTier(ItemStack item) {
		if (item!=null && isUpgradeShard(item)) {
			ItemMeta meta = item.getItemMeta();
			List<String> lore = item.getItemMeta().getLore();
			for (String lo : lore) {
				if (lo.contains(ChatColor.GOLD+""+ChatColor.BOLD+"T")) {
					return Integer.valueOf(ChatColor.stripColor(lo.split(" ")[0].replace("T", "")));
				}
			}
			lore.add(0,ChatColor.GOLD+""+ChatColor.BOLD+"T1 Set Upgrade Shard");
			lore.add(1,"");
			meta.setLore(lore);
			item.setItemMeta(meta);
			return 1;
		} else {
			return 0;
		}
	}
	
	public static void setUpgradeShardTier(ItemStack item, int tier) {
		if (item!=null && isUpgradeShard(item)) {
			ItemMeta meta = item.getItemMeta();
			List<String> lore = item.getItemMeta().getLore();
			boolean found=false;
			for (int i=0;i<lore.size();i++) {
				if (lore.get(i).contains(ChatColor.GOLD+""+ChatColor.BOLD+"T")) {
					//return Integer.valueOf(ChatColor.stripColor(lore.get(i).split(" ")[0].replace("T", "")));
					lore.set(i, ChatColor.GOLD+""+ChatColor.BOLD+"T"+tier+" Set Upgrade Shard");
					found=true;
					break;
				}
			}
			if (!found) {
				lore.add(0,ChatColor.GOLD+""+ChatColor.BOLD+"T"+tier+" Set Upgrade Shard");
				lore.add(1,"");
			}
			meta.setLore(lore);
			item.setItemMeta(meta);
		}
	}

	public static void ApplySwiftAegis(Player p) {
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		int swiftaegislv=(int)ItemSet.TotalBaseAmountBasedOnSetBonusCount(GenericFunctions.getEquipment(p), p, ItemSet.DARNYS, 4, 4);
		/*if (swiftaegislv>0) {
			TwosideKeeper.log("Applying "+swiftaegislv+" levels of Swift Aegis.",5);
			int resistancelv = 0;
			int resistance_duration = 0;
			if (p.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) {
				resistancelv = GenericFunctions.getPotionEffectLevel(PotionEffectType.DAMAGE_RESISTANCE, p);
				resistance_duration = GenericFunctions.getPotionEffectDuration(PotionEffectType.DAMAGE_RESISTANCE, p);
			}
			if (resistancelv<9) {
				//Try to apply as many levels as we can onto it.
				if (resistancelv+swiftaegislv<9) {
					//Apply it directly.
					pd.swiftaegisamt+=swiftaegislv;
					logAndApplyPotionEffectToEntity(PotionEffectType.DAMAGE_RESISTANCE, Math.max(20*20, resistance_duration), (resistancelv+swiftaegislv),p,true);
				} else {
					pd.swiftaegisamt+=Math.max(9-resistancelv,0);
					logAndApplyPotionEffectToEntity(PotionEffectType.DAMAGE_RESISTANCE, Math.max(20*20, resistance_duration),9,p,true);
				}
			}
			TwosideKeeper.log("New Aegis level: "+pd.swiftaegisamt,5);
			GenericFunctions.sendActionBarMessage(p, ChatColor.GRAY+"Resistance "+WorldShop.toRomanNumeral(GenericFunctions.getPotionEffectLevel(PotionEffectType.DAMAGE_RESISTANCE, p)+1));
		}*/
		if (swiftaegislv>0) {
			if (pd.swiftaegisamt<10) {
				pd.swiftaegistime=TwosideKeeper.getServerTickTime();
			}
			pd.swiftaegisamt=Math.min(10,getSwiftAegisAmt(p)+swiftaegislv);
			GenericFunctions.sendActionBarMessage(p, ChatColor.GRAY+"Resist "+WorldShop.toRomanNumeral(pd.swiftaegisamt));
		}
	}
	
	public static int getSwiftAegisAmt(Player p) {
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		if (pd.swiftaegistime+(20*20)>TwosideKeeper.getServerTickTime()) {
			return pd.swiftaegisamt;
		} else {
			pd.swiftaegisamt=0;
			return 0;
		}
	}
	
	/**
	 * Behavior is as follows:
	 * 	-> Sees if the potion effect exists on the player.
	 *  -> If current potion effect duration <= tick_duration
	 *  -> Overwrite with higher level buff.
	 *  
	 *  -> If potion effect does not exist on the player
	 *  -> Add it.
	 *  
	 *  -> If current potion effect == maxlv
	 *  -> If current potion effect duration <= tick_duration.
	 *  -> Overwrite / Renew with maxlv buff.
	 * @param p
	 * @param type
	 * @param maxlv The maximum level (Represented as a POTION level, not in-game displayed level.
	 */
	public static void addStackingPotionEffect(LivingEntity p, PotionEffectType type, int tick_duration, int maxlv) {
		addStackingPotionEffect(p,type,tick_duration,maxlv,1);
	}
	
	public static void addStackingPotionEffect(LivingEntity p, PotionEffectType type, int tick_duration, int maxlv, int incr_amt) {
		final int BUFFER = 20*20; //20 extra seconds difference required to prevent buffs from being overwritten by this method.
		if (p.hasPotionEffect(type)) {
			int duration = getPotionEffectDuration(type,p);
			int currentlv = getPotionEffectLevel(type,p);
			PotionEffect neweffect = new PotionEffect(type,tick_duration,(currentlv+incr_amt<maxlv)?(currentlv+incr_amt):maxlv);
			if (tick_duration+BUFFER >= duration) {
				logAndApplyPotionEffectToEntity(neweffect.getType(), neweffect.getDuration(),neweffect.getAmplifier(), p, true);
			}
		} else {
			PotionEffect neweffect = new PotionEffect(type,tick_duration,incr_amt-1); 
			logAndApplyPotionEffectToEntity(neweffect.getType(), neweffect.getDuration(),neweffect.getAmplifier(), p, true);
		}
	}

	public static boolean hasBaublePouchInOffHand(Player p) {
		return BaublePouch.isBaublePouch(p.getEquipment().getItemInOffHand());
	}

	public static boolean WearingNoArmor(Player p) {
		ItemStack[] armor = p.getEquipment().getArmorContents();
		boolean hasArmor=false;
		for (int i=0;i<armor.length;i++) {
			if (armor[i]!=null &&
					armor[i].getType()!=Material.AIR) {
				hasArmor=true;
				break;
			}
		}
		return !hasArmor;
	}

	public static void RemoveNewDebuffs(Player p) {
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		double removechance = CustomDamage.CalculateDebuffResistance(p);
		if (removechance>0) {
			if (pd.lasteffectlist.size()<p.getActivePotionEffects().size()) {
				int level=0;
				PotionEffectType type=null;
				for (PotionEffect pe : p.getActivePotionEffects()) {
					if (GenericFunctions.isBadEffect(pe.getType())) {
						type=pe.getType();
						level=pe.getAmplifier();
					}
				}
				if (Math.random()<=removechance/100) {
					if (type!=null && (!type.equals(PotionEffectType.WEAKNESS) || level<9)) {
						GenericFunctions.logAndRemovePotionEffectFromEntity(type,p);
						p.sendMessage(ChatColor.DARK_GRAY+"You successfully resisted the application of "+ChatColor.WHITE+GenericFunctions.CapitalizeFirstLetters(type.getName().replace("_", " ")));
					}
				}
			}
			pd.lasteffectlist.clear();
			pd.lasteffectlist.addAll(p.getActivePotionEffects());
		}
	}

	public static void createRandomWeb(Location l, int loop) {
		for (int x=-loop;x<=loop;x++) {
			for (int y=-loop;y<=loop;y++) {
				for (int z=-loop;z<=loop;z++) {
					Block b = l.getBlock().getRelative(x, y, z);
					if (b.getType()==Material.AIR ||
							b.getType()==Material.WATER ||
							b.getType()==Material.LAVA) {
						if (Math.random()<=0.45) {
							b.setType(Material.WEB);
							Bukkit.getScheduler().scheduleSyncDelayedTask(TwosideKeeper.plugin, new Runnable() {
								@Override
								public void run() {
									b.setType(Material.AIR);
								}
							}, 20*5);
						}
					}
				}
			}
		}
	}
	
	public static void logToFile(String message) {
		try {
			if (!TwosideKeeper.filesave.exists()) {
				TwosideKeeper.filesave.mkdir();
			}

		    File saveTo = new File(TwosideKeeper.filesave, "TwosideKeeperlogger.txt");
			if (!saveTo.exists()) {
				saveTo.createNewFile();
			}

			FileWriter fw = new FileWriter(saveTo, true);
			PrintWriter pw = new PrintWriter(fw);

			pw.println(message);
			pw.flush();
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void logToFile(String message, String filename) {
		try {
			if (!TwosideKeeper.filesave.exists()) {
				TwosideKeeper.filesave.mkdir();
			}

		    File saveTo = new File(TwosideKeeper.filesave, filename);
			if (!saveTo.exists()) {
				saveTo.createNewFile();
			}

			FileWriter fw = new FileWriter(saveTo, true);
			PrintWriter pw = new PrintWriter(fw);

			pw.println(message);
			pw.flush();
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean isSkullItem(ItemStack item) {
		if (item!=null &&
				item.getType()!=Material.AIR && (item.getType()==Material.SKULL_ITEM)) {
			return true;
		}
		return false;
	}

	public static void applyStealth(Player p, boolean blindness_effect) {
		GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 111, p, true);
		if (blindness_effect) {GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.BLINDNESS, 20*2, 111, p);}
		SoundUtils.playLocalSound(p, Sound.ENTITY_GENERIC_EXTINGUISH_FIRE, 1.0f, 0.5f);
	}

	public static void removeStealth(Player p) {
		GenericFunctions.logAndRemovePotionEffectFromEntity(PotionEffectType.INVISIBILITY, p);
		GenericFunctions.addIFrame(p, 10);
		SoundUtils.playLocalSound(p, Sound.BLOCK_REDSTONE_TORCH_BURNOUT, 1.0f, 1.0f);
	}
	
	public static boolean hasStealth(Player p) {
		return (PlayerMode.getPlayerMode(p)==PlayerMode.SLAYER &&
				p.hasPotionEffect(PotionEffectType.INVISIBILITY));
	}

	public static void SubtractSlayerModeHealth(Player p,double damage) {
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		pd.slayermodehp-=damage;
		TwosideKeeper.log("Slayer Mode HP: "+pd.slayermodehp, 5);
		//p.setHealth(pd.slayermodehp);
	}

	public static void PerformLineDrive(Player p, ItemStack weaponused, boolean second_charge) {
		PlayerLineDriveEvent ev = new PlayerLineDriveEvent(p);
		Bukkit.getPluginManager().callEvent(ev);
		if (!ev.isCancelled()) {
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			boolean ex_version = ItemSet.hasFullSet(GenericFunctions.getEquipment(p), p, ItemSet.PANROS);
			Vector facing = p.getLocation().getDirection();
			if (!second_charge) {
				facing = p.getLocation().getDirection().setY(0);
				logAndApplyPotionEffectToEntity(PotionEffectType.SLOW,(ex_version)?7:15,20,p);
			}
			if (!ex_version || second_charge) {
				aPlugin.API.sendCooldownPacket(p, weaponused, GetModifiedCooldown(TwosideKeeper.LINEDRIVE_COOLDOWN,p));
				pd.last_strikerspell=TwosideKeeper.getServerTickTime();
			}
			SoundUtils.playLocalSound(p, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
			aPlugin.API.damageItem(p.getInventory(), weaponused, (weaponused.getType().getMaxDurability()/10)+7);
			final Player p1 = p;
		
			int mult=2;
			final double xspd=p.getLocation().getDirection().getX()*mult;
			double tempyspd=0;
			final double yspd=tempyspd;
			final double zspd=p.getLocation().getDirection().getZ()*mult; 
			final double xpos=p.getLocation().getX();
			final double ypos=p.getLocation().getY();
			final double zpos=p.getLocation().getZ();
			
			final Vector facing1 = facing;
			Bukkit.getScheduler().scheduleSyncDelayedTask(TwosideKeeper.plugin, new Runnable() {
				public void run() {
					p.setVelocity(facing1.multiply(8));
					addIFrame(p, 10);
					SoundUtils.playLocalSound(p, Sound.ITEM_CHORUS_FRUIT_TELEPORT, 1.0f, 1.0f);
					final Location newpos=new Location(p.getWorld(),xpos,ypos,zpos);
					double dmgdealt=CustomDamage.getBaseWeaponDamage(weaponused, p, null);
					//List<Monster> monsters = getNearbyMobs(newpos, 2);
					List<Entity> ents = new ArrayList<Entity>(newpos.getWorld().getNearbyEntities(newpos, 2, 2, 2));
					List<Monster> monsters = CustomDamage.trimNonMonsterEntities(ents);
					for (int i=0;i<monsters.size();i++) {
						removeNoDamageTick(monsters.get(i), p);
					}
					for (int i=0;i<50;i++) { 
						newpos.getWorld().playEffect(newpos, Effect.FLAME, 60);
					}
					DealDamageToNearbyMobs(newpos, dmgdealt, 2, true, 0.8d, p, weaponused, true);
					//DecimalFormat df = new DecimalFormat("0.00");
					SoundUtils.playGlobalSound(p.getLocation(), Sound.ENTITY_ARMORSTAND_HIT, 1.0f, 0.5f);
					int range=8;
					for (int i=0;i<range;i++) {
						final double xpos2=p.getLocation().getX();
						final double ypos2=p.getLocation().getY();
						final double zpos2=p.getLocation().getZ();
						final Location newpos2=new Location(p.getWorld(),xpos2,ypos2,zpos2).add(i*xspd,i*yspd,i*zspd);
						for (int j=0;j<50;j++) {
							newpos.getWorld().playEffect(newpos, Effect.FLAME, 60);
						}
						Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("TwosideKeeper"), new Runnable() {
						public void run() {
								DealDamageToNearbyMobs(newpos2, dmgdealt, 2, true, 0.4d, p, weaponused, true);
			    				SoundUtils.playGlobalSound(newpos2, Sound.ENTITY_ARMORSTAND_HIT, 1.0f, 0.3f);
							}
						},1);
					}
				}
			},(ex_version)?7:15);
			if (ex_version) {
				Bukkit.getScheduler().scheduleSyncDelayedTask(TwosideKeeper.plugin, new Runnable() {
					public void run() {
			    		aPlugin.API.sendCooldownPacket(p, weaponused, GetModifiedCooldown(TwosideKeeper.LINEDRIVE_COOLDOWN,p));
			    		pd.last_strikerspell=TwosideKeeper.getServerTickTime();
					}
				},17);
			}
		}
	}

	public static void PerformAssassinate(Player player, Material name) {
		//Try to find a target to look at.
		//LivingEntity target = aPlugin.API.rayTraceTargetEntity(player, 100);
		Location originalloc = player.getLocation().clone();
		if (aPlugin.API.performAssassinate(player)) {
			SoundUtils.playGlobalSound(player.getLocation(), Sound.BLOCK_NOTE_SNARE, 1.0f, 1.0f);
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(player);
			LivingEntity target = aPlugin.API.getTargetEntity(player, 100);
			if (name!=Material.SKULL_ITEM || pd.lastlifesavertime+GetModifiedCooldown(TwosideKeeper.LIFESAVER_COOLDOWN,player)<TwosideKeeper.getServerTickTime()) { //Don't overwrite life saver cooldowns.
				aPlugin.API.sendCooldownPacket(player, name, GetModifiedCooldown(TwosideKeeper.ASSASSINATE_COOLDOWN,player));
			}
			pd.lastassassinatetime=TwosideKeeper.getServerTickTime();
			if (ItemSet.HasSetBonusBasedOnSetBonusCount(GenericFunctions.getBaubles(player), player, ItemSet.WOLFSBANE, 5)) {
				GenericFunctions.addIFrame(player, (int)ItemSet.TotalBaseAmountBasedOnSetBonusCount(GenericFunctions.getBaubles(player), player, ItemSet.WOLFSBANE, 5, 4));
			} else {
				GenericFunctions.addIFrame(player, 10);
			}
			if (ItemSet.HasSetBonusBasedOnSetBonusCount(GenericFunctions.getBaubles(player), player, ItemSet.WOLFSBANE, 3)) {
				GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.SPEED, 100, 4, player);
				GenericFunctions.addSuppressionTime(target, (int)ItemSet.TotalBaseAmountBasedOnSetBonusCount(GenericFunctions.getBaubles(player), player, ItemSet.WOLFSBANE, 3, 3));
			}
			if (ItemSet.HasSetBonusBasedOnSetBonusCount(GenericFunctions.getBaubles(player), player, ItemSet.WOLFSBANE, 7) &&
					target.getLocation().distanceSquared(originalloc)<=25) {
				pd.lastassassinatetime = TwosideKeeper.getServerTickTime()-TwosideKeeper.ASSASSINATE_COOLDOWN+40;
				if (name!=Material.SKULL_ITEM || pd.lastlifesavertime+GetModifiedCooldown(TwosideKeeper.LIFESAVER_COOLDOWN,player)<TwosideKeeper.getServerTickTime()) { //Don't overwrite life saver cooldowns.
					aPlugin.API.sendCooldownPacket(player, name, 40);
				}
			}
		}
		/*LivingEntity target = aPlugin.API.getTargetEntity(player, 100);
		if (target!=null && !target.isDead()) {
			//We found a target, try to jump behind them now.
			double mult = 0.0;
			double pitch = 0.0;
			if (target instanceof Spider || target instanceof CaveSpider) {
				mult += 2.0;
				pitch-=1.0;
			}
			Location originalloc = player.getLocation().clone();
			Location teleloc = target.getLocation().add(target.getLocation().getDirection().multiply(-1.0-mult));
			int i=0;
			while (!(teleloc.getBlock().getRelative(0, -1, 0).getType().isSolid() && teleloc.getBlock().getType()==Material.AIR && teleloc.getBlock().getRelative(0, 1, 0).getType()==Material.AIR)) {
				if (i==0) {
					teleloc=target.getLocation();
				} else 
				if (i%5==1){
					teleloc=teleloc.add(1,0,0);
				} else 
				if (i%5==2){
					teleloc=teleloc.add(0,0,1);
				} else 
				if (i%5==3){
					teleloc=teleloc.add(-1,0,0);
				} else 
				if (i%5==4){
					teleloc=teleloc.add(0,0,-1);
				} else {
					teleloc=teleloc.add(0,1,0);
				}
				i++;
			}
			int tries = 0;
			while (tries<2) {
				if ((TwosideKeeper.isNatural.contains(teleloc.getBlock().getType()) || teleloc.getBlock().getType()==Material.AIR) &&
						(TwosideKeeper.isNatural.contains(teleloc.getBlock().getRelative(BlockFace.UP).getType()) || teleloc.getBlock().getType()==Material.AIR)) {
					break;
				} else {
					//Try 1 higher.
					teleloc.add(0,1,0);
					tries++;
				}
			}
			if (TwosideKeeper.isNatural.contains(teleloc.getBlock().getType())) {
				teleloc.getBlock().breakNaturally();
			}
			if (TwosideKeeper.isNatural.contains(teleloc.getBlock().getRelative(BlockFace.UP).getType())) {
				teleloc.getBlock().getRelative(BlockFace.UP).breakNaturally();
			}
			SoundUtils.playGlobalSound(teleloc, Sound.BLOCK_NOTE_SNARE, 1.0f, 1.0f);
			teleloc.setPitch((float)pitch);
			
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(player);
			
			player.teleport(teleloc);
			Location newfacingdir = target.getLocation().setDirection(target.getLocation().getDirection());
			target.teleport(newfacingdir);
			if (name!=Material.SKULL_ITEM || pd.lastlifesavertime+GetModifiedCooldown(TwosideKeeper.LIFESAVER_COOLDOWN,player)<TwosideKeeper.getServerTickTime()) { //Don't overwrite life saver cooldowns.
				aPlugin.API.sendCooldownPacket(player, name, GetModifiedCooldown(TwosideKeeper.ASSASSINATE_COOLDOWN,player));
			}
			pd.lastassassinatetime=TwosideKeeper.getServerTickTime();
			if (ItemSet.HasSetBonusBasedOnSetBonusCount(GenericFunctions.getHotbarItems(player), player, ItemSet.WOLFSBANE, 5)) {
				GenericFunctions.addIFrame(player, (int)ItemSet.TotalBaseAmountBasedOnSetBonusCount(GenericFunctions.getHotbarItems(player), player, ItemSet.WOLFSBANE, 5, 4));
			} else {
				GenericFunctions.addIFrame(player, 10);
			}
			if (ItemSet.HasSetBonusBasedOnSetBonusCount(GenericFunctions.getHotbarItems(player), player, ItemSet.WOLFSBANE, 3)) {
				GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.SPEED, 100, 4, player);
				GenericFunctions.addSuppressionTime(target, (int)ItemSet.TotalBaseAmountBasedOnSetBonusCount(GenericFunctions.getHotbarItems(player), player, ItemSet.WOLFSBANE, 3, 3));
			}
			if (ItemSet.HasSetBonusBasedOnSetBonusCount(GenericFunctions.getHotbarItems(player), player, ItemSet.WOLFSBANE, 7) &&
					target.getLocation().distanceSquared(originalloc)<=25) {
				if (name!=Material.SKULL_ITEM || pd.lastlifesavertime+GetModifiedCooldown(TwosideKeeper.LIFESAVER_COOLDOWN,player)<TwosideKeeper.getServerTickTime()) { //Don't overwrite life saver cooldowns.
					pd.lastassassinatetime = TwosideKeeper.getServerTickTime()-TwosideKeeper.ASSASSINATE_COOLDOWN+40;
					aPlugin.API.sendCooldownPacket(player, name, 40);
				}
			}
		}*/
	}

	public static void DamageRandomTool(Player p) {
		if (!aPlugin.API.isAFK(p) && ItemSet.GetSetCount(GenericFunctions.getEquipment(p), ItemSet.LORASYS, p)==0) {
			ItemStack[] inv = p.getInventory().getContents();
			for (int i=0;i<9;i++) {
				if (inv[i]!=null &&
						isTool(inv[i]) && inv[i].getType()!=Material.BOW) {
					aPlugin.API.damageItem(p.getInventory(), inv[i], 1);
				}
			}
		}
	}
	
	public static int GetNearbyMonsterCount(LivingEntity ent, double range) {
		List<Entity> ents = ent.getNearbyEntities(range, range, range);
		int count=0;
		for (Entity e : ents) {
			if (e instanceof LivingEntity && !(e instanceof Player) && !e.equals(ent)) {
				count++;
			}
		}
		return count;
	}

	public static boolean isIsolatedTarget(LivingEntity m, Player p) {
		return (p.getWorld().equals(m.getWorld()) && p.getLocation().distanceSquared(m.getLocation())<=1024 && 
				GenericFunctions.GetNearbyMonsterCount(m, 12)==0) &&
				PlayerMode.getPlayerMode(p)==PlayerMode.SLAYER;
	}
	
	public static boolean isSpecialGlowMonster(Monster m) {
		return LivingEntityStructure.getLivingEntityStructure(m).isLeader ||
		LivingEntityStructure.getLivingEntityStructure(m).isElite;
	}
	
	public static boolean isSuppressed(Entity ent) {
		if (ent!=null && ent.hasMetadata("SuppressionTime")) {
			return getSuppressionTime(ent)>0;
		} else {
			return false;
		}
	}
	
	public static void setSuppressionTime(Entity ent, int ticks) {
		if (ent!=null) {
			ent.setMetadata("SuppressionTime", new FixedMetadataValue(TwosideKeeper.plugin,TwosideKeeper.getServerTickTime()+ticks));
			SetupSuppression(ent,ticks);
		}
	}

	public static void addSuppressionTime(Entity ent, int ticks) {
		if (ent!=null) {
			ent.setMetadata("SuppressionTime", new FixedMetadataValue(TwosideKeeper.plugin,TwosideKeeper.getServerTickTime()+getSuppressionTime(ent)+ticks));
			SetupSuppression(ent,ticks);
		}
	}

	public static double getSuppressionTime(Entity ent) {
		double suppressiontime=0;
		List<MetadataValue> vals = ent.getMetadata("SuppressionTime");
		for (MetadataValue val : vals) {
			if (val.getOwningPlugin().equals(TwosideKeeper.plugin)) {
				long time = val.asLong();
				TwosideKeeper.log("Value: "+val.asLong(), 5);
				if (time>TwosideKeeper.getServerTickTime()) {
					suppressiontime = time-TwosideKeeper.getServerTickTime();
				}
			}
		}
		return suppressiontime;
	}
	
	private static void SetupSuppression(Entity ent, int ticks) {
		if (!TwosideKeeper.suppressed_entities.contains(ent)) {
			TwosideKeeper.suppressed_entities.add(ent);
		}
		if (ent instanceof LivingEntity) {
			//MonsterStructure.getMonsterStructure((Monster)ent).setGlobalGlow(GlowAPI.Color.BLACK);
			LivingEntityStructure.getLivingEntityStructure((LivingEntity)ent).UpdateGlow();
		} else {
			GlowAPI.setGlowing(ent, GlowAPI.Color.BLACK, Bukkit.getOnlinePlayers());
		}
		if (ent instanceof LivingEntity) {
			LivingEntity l = (LivingEntity)ent;
			//l.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,ticks,99));
			TwosideKeeper.log("Base Value: "+l.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue(), 5);
			l.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0d);
			l.setAI(false);
		}
	}

	public static ItemStack[] getBaubles(LivingEntity p) {
		Player pl = (Player)p;
		ItemStack baublepouch = pl.getEquipment().getItemInOffHand();
		if (BaublePouch.isBaublePouch(baublepouch)) {
			List<ItemStack> baubles = BaublePouch.getBaublePouchContents(BaublePouch.getBaublePouchID(baublepouch));
			ItemStack[] array = new ItemStack[baubles.size()];
			array = baubles.toArray(array);
			TwosideKeeper.log("Baubles: "+ArrayUtils.toString(array), 5);
			return array;
		} else {
			return new ItemStack[]{	
			};
		}
	}
	
	public static ItemStack[] getHotbarItems(LivingEntity p) {
		Player pl = (Player)p;
		return new ItemStack[]{
				pl.getInventory().getContents()[0],
				pl.getInventory().getContents()[1],
				pl.getInventory().getContents()[2],
				pl.getInventory().getContents()[3],
				pl.getInventory().getContents()[4],
				pl.getInventory().getContents()[5],
				pl.getInventory().getContents()[6],
				pl.getInventory().getContents()[7],
				pl.getInventory().getContents()[8],
		};
	}
	
	public static void sendActionBarMessage(Player p, String message) {
		sendActionBarMessage(p,message,false);
	}
	
	//Automatically appends status effect buffs to the beginning of it.
	public static void sendActionBarMessage(Player p, String message, boolean important) {
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		String finalmsg = "";
		if (pd.rage_time>TwosideKeeper.getServerTickTime()) {
			message = ChatColor.RED+" !! RAGE ACTIVE !! "+message;
		}
		//TwosideKeeper.log("["+TwosideKeeper.getServerTickTime()+"] Preparing Message. Important? "+important+" Message: \""+message+"\"", 1);
		String prefix=ActionBarBuffUpdater.getActionBarPrefix(p);
		finalmsg=message+" "+prefix;
		if (important || (pd.lastimportantactionbarmsg+20<=TwosideKeeper.getServerTickTime())) {
			//TwosideKeeper.log("["+TwosideKeeper.getServerTickTime()+"] Sent Message", 0);
			if (prefix.length()>0 || aPlugin.API.getLastXPBar(p).length() > 2) {
				aPlugin.API.sendActionBarMessage(p, String.format(aPlugin.API.getLastXPBar(p), finalmsg));
			} else {
				if (message.length()>0) { 
					aPlugin.API.sendActionBarMessage(p, String.format(aPlugin.API.getLastXPBar(p), message));
					finalmsg=message;
				}
			}
			if (important) {
				pd.lastimportantactionbarmsg=TwosideKeeper.getServerTickTime();
			}
		}
		pd.lastActionBarMessage=finalmsg;
		pd.lastActionBarMessageTime=TwosideKeeper.getServerTickTime();
		/*if ((finalmsg.length()>0 && pd.lastActionBarMessageTime+100<TwosideKeeper.getServerTickTime()) || pd.lastActionBarMessageTime+100<TwosideKeeper.getServerTickTime()) {
			
			pd.lastActionBarMessageTime=TwosideKeeper.getServerTickTime();
		}*/
	}
	
	public static void sendLastImportantActionBarMsgTime(Player p, long last_important_msg_time) {
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		pd.lastimportantactionbarmsg=last_important_msg_time;
		//TwosideKeeper.log("["+TwosideKeeper.getServerTickTime()+"]Set time to "+last_important_msg_time, 1);
	}
	
	public static String getLastActionBarMessage(Player p) {
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		return pd.lastActionBarMessage;
	}
	
	public static String getDisplayName(LivingEntity ent) {
		//Strips off the suffix of a mob.
		if (ent.getCustomName()==null) {
			return GenericFunctions.CapitalizeFirstLetters(ent.getType().name().replace("_", " "));
		} else {
			return ent.getCustomName().split(ChatColor.RESET+" ")[0];
		}
	}
	
	public static void TransferItemsToInventory(Inventory from, Inventory to) {
		List<ItemStack> inventory = new ArrayList<ItemStack>();
		for (int i=0;i<from.getContents().length;i++) {
			if (from.getItem(i)!=null) {
				inventory.add(from.getItem(i));
			}
		}
		ItemStack[] list = inventory.toArray(new ItemStack[inventory.size()]);
		HashMap<Integer,ItemStack> items = to.addItem(list);
		for (int i=0;i<items.size();i++) {
			//from.addItem(items.get(i));
			TwosideKeeper.log("Could not add "+items.get(i).toString()+" to inventory. Recycling it... THIS SHOULD NOT HAPPEN THOUGH!", 0);
			TwosideKeeperAPI.addItemToRecyclingCenter(items.get(i));
		}
		from.clear();
	}

	public static EliteMonster getProperEliteMonster(Monster target) {
		if (target instanceof Zombie) {
			return new EliteZombie(target);
		}
		if (target instanceof Wither) {
			target.setMaxHealth(188000);
			target.setHealth(188000);
			return new MegaWither(target);
		}
		if (TwosideKeeper.ELITEGUARDIANS_ACTIVATED) {
			if (target instanceof Guardian) {
				target.setMaxHealth(120000);
				target.setHealth(120000);
				return new EliteGuardian(target);
			}
		}
		TwosideKeeper.log("Elite Monster for monster "+target.getName()+" UNDEFINED. Defaulting to EliteZombie type.", 0);
		return new EliteZombie(target);
	}

	public static boolean AllowedToBeEquippedToOffHand(Player p, ItemStack item, int clickedslot) {
		//TwosideKeeper.log("Slot:"+clickedslot, 0); 36-44 is hotbar.
		return (ArrowQuiver.isValidQuiver(item) || BaublePouch.isBaublePouch(item)); /*|| 
				(item.getType()==Material.SHIELD && (clickedslot<36 || !p.getEquipment().getItemInMainHand().equals(p.getInventory().getContents()[clickedslot-36])) && (PlayerMode.isDefender(p) || PlayerMode.isNormal(p))));*/
	}
	
	public static boolean isValidArrow(ItemStack item) {
		return isValidArrow(item,false);
	}
	
	public static boolean isValidArrow(ItemStack item, boolean includeQuivers) {
		if (item!=null && item.getType().name().contains("ARROW")) {
			if (ArrowQuiver.isValidQuiver(item) && includeQuivers) {
				return true;
			} else
			if (!ArrowQuiver.isValidQuiver(item)) {
				return true; 
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public static double calculateInfinityChance(Player p) {
		return calculateInfinityChance(p.getEquipment().getItemInMainHand());
	}

	public static double calculateInfinityChance(ItemStack bow) {
		if (bow.getType()==Material.BOW &&
				bow.containsEnchantment(Enchantment.ARROW_INFINITE)) {
			return bow.getEnchantmentLevel(Enchantment.ARROW_INFINITE)*0.1d;
		} else {
			return 0.0;
		}
	}

	public static int getBasePotionDuration(PotionData bpd) {
		boolean extended = bpd.isExtended();
		boolean upgraded = bpd.isUpgraded();
		if (bpd.getType()==PotionType.FIRE_RESISTANCE ||
			bpd.getType()==PotionType.INVISIBILITY ||
			bpd.getType()==PotionType.JUMP ||
			bpd.getType()==PotionType.NIGHT_VISION ||
			bpd.getType()==PotionType.POISON ||
			bpd.getType()==PotionType.REGEN ||
			bpd.getType()==PotionType.SPEED ||
			bpd.getType()==PotionType.STRENGTH ||
			bpd.getType()==PotionType.WATER_BREATHING) {
			return (extended?20*0+(1200*8):upgraded?20*30+(1200*1):20*0+(1200*3));
		}
		if (bpd.getType()==PotionType.LUCK) {
			return (20*0+(1200*5));
		}
		if (bpd.getType()==PotionType.POISON ||
			bpd.getType()==PotionType.SLOWNESS ||
			bpd.getType()==PotionType.WEAKNESS) {
			return (extended?20*0+(1200*4):20*30+(1200*1));
		}
		return 0;
	}

	public static PotionEffectType convertPotionTypeToPotionEffectType(PotionType pt) {
		switch (pt) {
		case FIRE_RESISTANCE:
			return PotionEffectType.FIRE_RESISTANCE;
		case INSTANT_DAMAGE:
			return PotionEffectType.HARM;
		case INSTANT_HEAL:
			return PotionEffectType.HEAL;
		case INVISIBILITY:
			return PotionEffectType.INVISIBILITY;
		case JUMP:
			return PotionEffectType.JUMP;
		case LUCK:
			return PotionEffectType.LUCK;
		case NIGHT_VISION:
			return PotionEffectType.NIGHT_VISION;
		case POISON:
			return PotionEffectType.POISON;
		case REGEN:
			return PotionEffectType.REGENERATION;
		case SLOWNESS:
			return PotionEffectType.SLOW;
		case SPEED:
			return PotionEffectType.SPEED;
		case STRENGTH:
			return PotionEffectType.INCREASE_DAMAGE;
		case WATER_BREATHING:
			return PotionEffectType.WATER_BREATHING;
		case WEAKNESS:
			return PotionEffectType.WEAKNESS;
		default:
			return PotionEffectType.HARM;
		}
	}

	/**
	 * Attempts to heal the entity provided. This verifies that we do not
	 * surpass the maximum health of the entity.
	 */
	public static void HealEntity(LivingEntity p, double healamt) {
		if (p.getHealth()+healamt<p.getMaxHealth()) {
			p.setHealth(p.getHealth()+healamt);
		} else {
			p.setHealth(p.getMaxHealth());
		}
	}

	public static boolean isAutoConsumeFood(ItemStack item) {
		if (item!=null &&
			(item.getType()==Material.ROTTEN_FLESH ||
			item.getType()==Material.SPIDER_EYE)) {
			return true;
		} else {
			return false;
		}
	}

	@SuppressWarnings("deprecation")
	public static void PerformArrowBarrage(Player p) {
		if (p.isOnGround() && PlayerMode.isRanger(p) &&
				(GenericFunctions.getBowMode(p)==BowMode.SNIPE)) {
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			if (pd.last_arrowbarrage+GetModifiedCooldown(TwosideKeeper.ARROWBARRAGE_COOLDOWN,p)<=TwosideKeeper.getServerTickTime()) {
				pd.last_arrowbarrage=TwosideKeeper.getServerTickTime();
				Bukkit.getScheduler().scheduleSyncDelayedTask(TwosideKeeper.plugin, new ArrowBarrage(26,p,3), 3);
				aPlugin.API.sendCooldownPacket(p, p.getEquipment().getItemInMainHand(), GetModifiedCooldown(TwosideKeeper.ARROWBARRAGE_COOLDOWN,p));
			}
		}
	}

	@SuppressWarnings("deprecation")
	public static void PerformSiphon(Player p) {
		if (p.isOnGround() && PlayerMode.isRanger(p) &&
				(GenericFunctions.getBowMode(p)==BowMode.DEBILITATION)) {
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			if (pd.last_siphon+GetModifiedCooldown(TwosideKeeper.SIPHON_COOLDOWN,p)<=TwosideKeeper.getServerTickTime()) {
				List<LivingEntity> list = GenericFunctions.getNearbyMobs(p.getLocation(), 16);
				List<LivingEntity> poisonlist = new ArrayList<LivingEntity>();
				int totalpoisonstacks = 0;
				for (LivingEntity ent : list) {
					if (!(ent instanceof Player)) {
						boolean haspoison=false;
						if (ent.hasPotionEffect(PotionEffectType.POISON)) {
							int poisonlv = GenericFunctions.getPotionEffectLevel(PotionEffectType.POISON, ent);
							totalpoisonstacks+=poisonlv+1;
							haspoison=true;
						}
						if (ent.hasPotionEffect(PotionEffectType.BLINDNESS)) {
							int blindnesslv = GenericFunctions.getPotionEffectLevel(PotionEffectType.BLINDNESS, ent);
							totalpoisonstacks+=blindnesslv+1;
							haspoison=true;
						}
						if (haspoison) {
							poisonlist.add(ent);
						}
					}
				}
				if (totalpoisonstacks>0) {
					pd.last_siphon=TwosideKeeper.getServerTickTime();
					SoundUtils.playLocalSound(p, Sound.BLOCK_FENCE_GATE_OPEN, 1.0f, 0.4f);
					aPlugin.API.sendCooldownPacket(p, p.getEquipment().getItemInMainHand(), GetModifiedCooldown(TwosideKeeper.SIPHON_COOLDOWN,p));
					for (LivingEntity ent : poisonlist) {
						//Refresh poison stacks if necessary.
						int totalpoisonlv = 0;
						if (ent.hasPotionEffect(PotionEffectType.POISON)) {
							int poisonlv = GenericFunctions.getPotionEffectLevel(PotionEffectType.POISON, ent);
							int poisondur = GenericFunctions.getPotionEffectDuration(PotionEffectType.POISON, ent);
							totalpoisonlv+=poisonlv+1;
							if (poisondur<20*15) {
								GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.POISON, 20*15, poisonlv, ent, true);
							}
							GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.SLOW, 20*15, poisonlv, ent);
						}
						if (ent.hasPotionEffect(PotionEffectType.BLINDNESS)) {
							int poisonlv = GenericFunctions.getPotionEffectLevel(PotionEffectType.BLINDNESS, ent);
							int poisondur = GenericFunctions.getPotionEffectDuration(PotionEffectType.BLINDNESS, ent); 
							totalpoisonlv+=poisonlv+1;
							if (poisondur<20*15) {
								GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.BLINDNESS, 20*15, poisonlv, ent, true);
							}
							GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.SLOW, 20*15, poisonlv, ent);
						}
						CustomDamage.ApplyDamage(totalpoisonlv*10, p, ent, null, "Siphon", CustomDamage.TRUEDMG|CustomDamage.IGNOREDODGE|CustomDamage.IGNORE_DAMAGE_TICK);
					}
					CustomDamage.setAbsorptionHearts(p, CustomDamage.getAbsorptionHearts(p)+totalpoisonstacks*4);
				}
			}
		}
	}
	
	public static void PopulatePlayerBlockList(Player p, int width, int length, int up, int down, boolean includeCurrentYLayer) {
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		if (pd.lastStandingLoc==null || 
				(
						!p.getLocation().getWorld().equals(pd.lastStandingLoc.getWorld()) ||
						p.getLocation().getX()!=pd.lastStandingLoc.getX() ||
						p.getLocation().getZ()!=pd.lastStandingLoc.getZ()
				)
			) {
			pd.lastStandingLoc=new Location(p.getLocation().getWorld(),p.getLocation().getX(),p.getLocation().getY(),p.getLocation().getZ());
			pd.blockscanlist.clear();
			for (int x=-width;x<=width;x++) {
				for (int z=-length;z<=length;z++) {
					for (int y=-down;y<=up;y++) {
						//pd.blockscanlist.add(pd.lastStandingLoc.add(x,y,z).getBlock());
						Block b = pd.lastStandingLoc.add(x,y,z).getBlock();
						pd.blockscanlist.put(b.getType(), b);
						TwosideKeeper.log("("+x+","+y+","+z+")"+"Added "+b.getType()+" to block list for player "+p.getName(), 0);
					}
				}
			}
		}
	}
	
	public static HashMap<Material,Block> GetPlayerBlockList(Player p) {
		PopulatePlayerBlockList(p,15,15,2,5,false);
		return PlayerStructure.GetPlayerStructure(p).blockscanlist;
	}

	public static boolean itemCanBeSuckedUp(Item ent) {
		ItemStack item = ent.getItemStack();
		//TwosideKeeper.log(item.toString()+": "+ent.getTicksLived()+".."+ent.getPickupDelay()+".."+((Item)ent).getName()+".."+((Item)ent).isCustomNameVisible()+".."+((Item)ent).getCustomName(), 0);
		if (ent.getPickupDelay()>=0) {
			return false;
		}
		return true;
	}
	
	public static void renameDropper(Dropper dropper, String title) {
		CraftDropper BukkitDropper = (CraftDropper) dropper;
		TileEntityDropper NMSDropper = (TileEntityDropper) BukkitDropper.getTileEntity();
		NMSDropper.a(title);
	}
	
	public static void renameHopper(Hopper hopper, String title) {
		CraftHopper BukkitHopper = (CraftHopper) hopper;
		TileEntityHopper NMSHopper = (TileEntityHopper) BukkitHopper.getTileEntity();
		NMSHopper.a(title);
	}
}
