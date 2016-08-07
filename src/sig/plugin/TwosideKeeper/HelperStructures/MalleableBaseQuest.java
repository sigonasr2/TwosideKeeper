package sig.plugin.TwosideKeeper.HelperStructures;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import sig.plugin.TwosideKeeper.Artifact;
import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;

public class MalleableBaseQuest {
	
	/*LORE FORMAT:
	 * FORMING IN PROGRESS
	 * Base requires 'ITEM_NAME' to
	 * continue forming... (5/30)
	 * 4893210801 <--Server Tick Time
	 * 
	 * 
	 */
	
	public static ItemStack startQuest(ItemStack base) {
		//Formats the item lore in preparation for the quest.
		ItemMeta m = base.getItemMeta();
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.RED+"FORMING IN PROGRESS");
		lore.add(ChatColor.BLUE+"Base requires "+ChatColor.AQUA+"'"+GenericFunctions.UserFriendlyMaterialName(new ItemStack(selectItem()))+"'"+ChatColor.BLUE+" to");
		lore.add(ChatColor.BLUE+"continue forming... "+ChatColor.GREEN+"(0/30)");
		lore.add(ChatColor.BLUE+""+TwosideKeeper.getServerTickTime());
		m.setLore(lore);
		base.setItemMeta(m);
		return Artifact.convert(base);
		
	}
		
	public static ItemStack setTimeStarted(ItemStack base, long time) {
		//The time started is always on the third line.
		ItemMeta m = base.getItemMeta();
		List<String> lore = m.getLore();
		lore.remove(3);
		lore.add(3,ChatColor.BLUE+""+TwosideKeeper.getServerTickTime());
		m.setLore(lore);
		base.setItemMeta(m);
		return base;
	}
	public static ItemStack setItem(ItemStack base, Material mat) {
		//Sets the material to this item instead.
		ItemMeta m = base.getItemMeta();
		List<String> lore = m.getLore();
		lore.remove(1);
		lore.add(1,ChatColor.BLUE+"Base requires "+ChatColor.AQUA+"'"+mat.toString()+"'"+ChatColor.BLUE+" to");
		m.setLore(lore);
		base.setItemMeta(m);
		return base;
	}
	public static ItemStack advanceQuestProgress(ItemStack base) {
		//This should occur when the base quest is ready to proceed.
		//Advance the progress by one. 
		//Choose the next item randomly.
		ItemMeta m = base.getItemMeta();
		List<String> lore = m.getLore();
		lore.remove(1);
		lore.add(1,ChatColor.BLUE+"Base requires "+ChatColor.AQUA+"'"+GenericFunctions.UserFriendlyMaterialName(new ItemStack(selectItem()))+"'"+ChatColor.BLUE+" to");
		//Get the old quest progress.
		int progress = getCurrentProgress(base);
		lore.remove(2);
		lore.add(2,ChatColor.BLUE+"continue forming... "+ChatColor.GREEN+"("+(progress+1)+"/30)");
		m.setLore(lore);
		base.setItemMeta(m);
		return Artifact.convert(base,false);
	}
	public static ItemStack completeQuest(ItemStack base) {
		//Triggered when the quest is done. Turn into a base.
		//Get the time now, and the time when we started the quest.
		long starttime = getTimeStarted(base);
		long currenttime = TwosideKeeper.getServerTickTime();
		
		int amt = base.getAmount();
		
		if (currenttime-starttime<=36000) { //30 min passed. Divine tier.
			ItemStack newbase = Artifact.createArtifactItem(ArtifactItem.DIVINE_BASE);
			newbase.setAmount(amt);
			return newbase;
		} else
		if (currenttime-starttime<=72000) { //1 hour passed. Lost tier.
			ItemStack newbase = Artifact.createArtifactItem(ArtifactItem.LOST_BASE);
			newbase.setAmount(amt);
			return newbase;
		} else
		if (currenttime-starttime<=144000) { //2 hours passed. Ancient tier.
			ItemStack newbase = Artifact.createArtifactItem(ArtifactItem.ANCIENT_BASE);
			newbase.setAmount(amt);
			return newbase;
		} else
		{ //>2 hours passed. Artifact tier.
			ItemStack newbase = Artifact.createArtifactItem(ArtifactItem.ARTIFACT_BASE);
			newbase.setAmount(amt);
			return newbase;
		}/* else //Too harsh. We are not going to make the player start all over.
		{
			//This failed. Turn it back into a Malleable base.
			return Artifact.createArtifactItem(ArtifactItem.MALLEABLE_BASE);
		}*/
	}
	
	public static String getItem(ItemStack base) {
		//Get current item for this Base.
		ItemMeta m = base.getItemMeta();
		List<String> lore = m.getLore();
		String material_name = lore.get(1).split("'")[1];
		if (lore.get(1).contains("Rabbit")) {
			return "Rabbit's Foot";
		} else
		if (lore.get(1).contains("Jack o")) {
			return "Jack o'Lantern";
		} else {
			return material_name;
		}
	}
	public static int getCurrentProgress(ItemStack base) {
		//How many quest items have been completed already?
		ItemMeta m = base.getItemMeta();
		List<String> lore = m.getLore();
		String progress = lore.get(2).substring(lore.get(2).indexOf("(", 0)+1,lore.get(2).indexOf("/", 0));
		return Integer.parseInt(progress);
	}
	public static long getTimeStarted(ItemStack base) {
		//Returns the server tick time this quest was started on.
		ItemMeta m = base.getItemMeta();
		List<String> lore = m.getLore();
		String timelore = lore.get(3).replace(ChatColor.BLUE+"", "");
		return Long.parseLong(timelore);
		
	}
	public static QuestStatus getStatus(ItemStack base) {
		ItemMeta m = base.getItemMeta();
		if (m.getLore().contains(ChatColor.RED+"FORMING IN PROGRESS")) {
			return QuestStatus.IN_PROGRESS;
		} else {
			return QuestStatus.UNFORMED;
		}
	}
	
	public static void announceQuestItem(Plugin plug, final Player p, final ItemStack i) {
		/*Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			@Override
			public void run() {
		    	setPlayerMaxHealth(player);
			}
		},1);*/
		p.sendMessage(ChatColor.AQUA+"The item you must obtain...");
		Bukkit.getScheduler().scheduleSyncDelayedTask(plug, new Runnable() {
			@Override
			public void run() {
				p.sendMessage(ChatColor.AQUA+"is "+ChatColor.GREEN+getItem(i));
			}
		},40);
		Bukkit.getScheduler().scheduleSyncDelayedTask(plug, new Runnable() {
			@Override
			public void run() {
				p.sendMessage(ChatColor.AQUA+"Right-click the base again once you have the item in your hotbar!");
			}
		},80);
	}
	
	public static Material selectItem() {
		//Material.values().
		List<Material> blacklisted_items = new ArrayList<Material>();
		blacklisted_items.add(Material.ACACIA_DOOR);
		blacklisted_items.add(Material.AIR);
		blacklisted_items.add(Material.BARRIER);
		blacklisted_items.add(Material.BEACON);
		blacklisted_items.add(Material.BED_BLOCK);
		blacklisted_items.add(Material.BEDROCK);
		blacklisted_items.add(Material.BEETROOT_BLOCK);
		blacklisted_items.add(Material.BEETROOT);
		blacklisted_items.add(Material.BEETROOT_SEEDS);
		blacklisted_items.add(Material.BIRCH_DOOR);
		blacklisted_items.add(Material.BREWING_STAND);
		blacklisted_items.add(Material.BURNING_FURNACE);
		blacklisted_items.add(Material.CAKE_BLOCK);
		blacklisted_items.add(Material.CARROT);
		blacklisted_items.add(Material.CAULDRON);
		blacklisted_items.add(Material.CHAINMAIL_BOOTS);
		blacklisted_items.add(Material.CHAINMAIL_CHESTPLATE);
		blacklisted_items.add(Material.CHAINMAIL_HELMET);
		blacklisted_items.add(Material.CHAINMAIL_LEGGINGS);
		blacklisted_items.add(Material.CHAINMAIL_BOOTS);
		blacklisted_items.add(Material.CHORUS_PLANT);
		blacklisted_items.add(Material.COCOA);
		blacklisted_items.add(Material.COMMAND);
		blacklisted_items.add(Material.COMMAND_CHAIN);
		blacklisted_items.add(Material.COMMAND_MINECART);
		blacklisted_items.add(Material.COMMAND_REPEATING);
		blacklisted_items.add(Material.CROPS);
		blacklisted_items.add(Material.DARK_OAK_DOOR);
		blacklisted_items.add(Material.DAYLIGHT_DETECTOR_INVERTED);
		blacklisted_items.add(Material.DEAD_BUSH);
		blacklisted_items.add(Material.DIAMOND_BARDING);
		blacklisted_items.add(Material.IRON_BARDING);
		blacklisted_items.add(Material.GOLD_BARDING);
		blacklisted_items.add(Material.DIODE_BLOCK_OFF);
		blacklisted_items.add(Material.DIODE_BLOCK_ON);
		blacklisted_items.add(Material.DOUBLE_PLANT);
		blacklisted_items.add(Material.DOUBLE_STEP);
		blacklisted_items.add(Material.DOUBLE_STONE_SLAB2);
		blacklisted_items.add(Material.DRAGON_EGG);
		blacklisted_items.add(Material.DRAGONS_BREATH);
		blacklisted_items.add(Material.COAL_ORE);
		blacklisted_items.add(Material.END_GATEWAY);
		blacklisted_items.add(Material.ELYTRA);
		blacklisted_items.add(Material.ENDER_PORTAL);
		blacklisted_items.add(Material.ENDER_PORTAL_FRAME);
		blacklisted_items.add(Material.EXP_BOTTLE);
		blacklisted_items.add(Material.FIRE);
		blacklisted_items.add(Material.FIREBALL);
		blacklisted_items.add(Material.FLOWER_POT);
		blacklisted_items.add(Material.FROSTED_ICE);
		blacklisted_items.add(Material.GLOWING_REDSTONE_ORE);
		blacklisted_items.add(Material.GOLD_RECORD);
		blacklisted_items.add(Material.GRASS);
		blacklisted_items.add(Material.GRASS_PATH);
		blacklisted_items.add(Material.GREEN_RECORD);
		blacklisted_items.add(Material.HUGE_MUSHROOM_1);
		blacklisted_items.add(Material.HUGE_MUSHROOM_2);
		blacklisted_items.add(Material.JUNGLE_DOOR);
		blacklisted_items.add(Material.LAVA);
		blacklisted_items.add(Material.LINGERING_POTION);
		blacklisted_items.add(Material.LONG_GRASS);
		blacklisted_items.add(Material.MAP);
		blacklisted_items.add(Material.MELON_BLOCK);
		blacklisted_items.add(Material.MELON_STEM);
		blacklisted_items.add(Material.MOB_SPAWNER);
		blacklisted_items.add(Material.MONSTER_EGG);
		blacklisted_items.add(Material.MONSTER_EGGS);
		blacklisted_items.add(Material.MYCEL);
		blacklisted_items.add(Material.NAME_TAG);
		blacklisted_items.add(Material.NETHER_WARTS);
		blacklisted_items.add(Material.NETHER_STAR);
		blacklisted_items.add(Material.POTATO);
		blacklisted_items.add(Material.PISTON_EXTENSION);
		blacklisted_items.add(Material.PISTON_MOVING_PIECE);
		blacklisted_items.add(Material.PORTAL);
		blacklisted_items.add(Material.PUMPKIN_STEM);
		blacklisted_items.add(Material.PURPUR_DOUBLE_SLAB);
		blacklisted_items.add(Material.RECORD_10);
		blacklisted_items.add(Material.RECORD_11);
		blacklisted_items.add(Material.RECORD_12);
		blacklisted_items.add(Material.RECORD_3);
		blacklisted_items.add(Material.RECORD_4);
		blacklisted_items.add(Material.RECORD_5);
		blacklisted_items.add(Material.RECORD_6);
		blacklisted_items.add(Material.RECORD_7);
		blacklisted_items.add(Material.RECORD_8);
		blacklisted_items.add(Material.RECORD_9);
		blacklisted_items.add(Material.RED_ROSE);
		blacklisted_items.add(Material.REDSTONE_COMPARATOR_OFF);
		blacklisted_items.add(Material.REDSTONE_COMPARATOR_ON);
		blacklisted_items.add(Material.REDSTONE_LAMP_OFF);
		blacklisted_items.add(Material.REDSTONE_LAMP_ON);
		blacklisted_items.add(Material.REDSTONE_TORCH_OFF);
		blacklisted_items.add(Material.REDSTONE_WIRE);
		blacklisted_items.add(Material.COAL_ORE);
		blacklisted_items.add(Material.IRON_ORE);
		blacklisted_items.add(Material.GOLD_ORE);
		blacklisted_items.add(Material.REDSTONE_ORE);
		blacklisted_items.add(Material.EMERALD_ORE);
		blacklisted_items.add(Material.SAPLING);
		blacklisted_items.add(Material.SADDLE);
		blacklisted_items.add(Material.SIGN_POST);
		blacklisted_items.add(Material.WALL_SIGN);
		blacklisted_items.add(Material.SKULL);
		blacklisted_items.add(Material.SKULL_ITEM);
		blacklisted_items.add(Material.SNOW);
		blacklisted_items.add(Material.SOIL);
		blacklisted_items.add(Material.SPONGE);
		blacklisted_items.add(Material.SPRUCE_DOOR);
		blacklisted_items.add(Material.STANDING_BANNER);
		blacklisted_items.add(Material.STATIONARY_LAVA);
		blacklisted_items.add(Material.STATIONARY_WATER);
		blacklisted_items.add(Material.STEP);
		blacklisted_items.add(Material.STONE_SLAB2);
		blacklisted_items.add(Material.STRUCTURE_BLOCK);
		blacklisted_items.add(Material.SUGAR_CANE_BLOCK);
		blacklisted_items.add(Material.TRIPWIRE);
		blacklisted_items.add(Material.VINE);
		blacklisted_items.add(Material.WALL_BANNER);
		blacklisted_items.add(Material.WATCH);
		blacklisted_items.add(Material.WATER);
		blacklisted_items.add(Material.WOOD_DOUBLE_STEP);
		blacklisted_items.add(Material.WOODEN_DOOR);
		blacklisted_items.add(Material.WOOD_DOOR);
		blacklisted_items.add(Material.YELLOW_FLOWER);
		Material selectedMat = Material.values()[(int)(Math.random()*Material.values().length)];
		while (blacklisted_items.contains(selectedMat)) {
			//RE-roll if it's a black-listed item.
			selectedMat = Material.values()[(int)(Math.random()*Material.values().length)];
		}
		return selectedMat;
	}
}