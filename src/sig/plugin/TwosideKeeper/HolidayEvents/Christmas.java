package sig.plugin.TwosideKeeper.HolidayEvents;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Difficulty;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Dispenser;
import org.bukkit.block.Sign;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowman;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.FireworkMeta;

import aPlugin.API.Chests;
import aPlugin.DropItem;
import aPlugin.DropMaterial;
import sig.plugin.TwosideKeeper.CustomDamage;
import sig.plugin.TwosideKeeper.PlayerStructure;
import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.aPluginAPIWrapper;
import sig.plugin.TwosideKeeper.Drops.DropRandomFirework;
import sig.plugin.TwosideKeeper.Drops.SigDrop;
import sig.plugin.TwosideKeeper.HelperStructures.ItemSet;
import sig.plugin.TwosideKeeper.HelperStructures.LivingEntityDifficulty;
import sig.plugin.TwosideKeeper.HelperStructures.Loot;
import sig.plugin.TwosideKeeper.HelperStructures.PlayerMode;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.ArrayUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.BiomeUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.ItemUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.PlayerUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.SoundUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.TextUtils;

public class Christmas {
	public final static String CHRISTMAS_TAG = ChatColor.BLUE+"Christmas Event Item"+ChatColor.RESET;
	public final static String ROCKET_CHARGES_LINE = ChatColor.WHITE+"Charges: "+ChatColor.YELLOW;
	
	public static void insertChristmasTag(ItemStack item) {
		ItemUtils.addLore(item, CHRISTMAS_TAG);
	}
	
	public static ItemStack getRocketBoosterItem() {
		ItemStack item  = new ItemStack(Material.FIREWORK_CHARGE);
		ItemUtils.setDisplayName(item, ChatColor.DARK_AQUA+"Rocket Booster");
		insertChristmasTag(item);
		ItemUtils.addLore(item, ChatColor.WHITE+"A Launcher that shoots you");
		ItemUtils.addLore(item, ChatColor.WHITE+"extremely fast in your facing");
		ItemUtils.addLore(item, ChatColor.WHITE+"direction. Slowly recharges");
		ItemUtils.addLore(item, ChatColor.WHITE+"over time if kept on the hotbar.");
		ItemUtils.addLore(item, "");
		ItemUtils.addLore(item, ChatColor.AQUA+"Right-click to use.");
		ItemUtils.addLore(item, "");
		setRocketBoosterCharges(item,5);
		item.addUnsafeEnchantment(Enchantment.LUCK, 0);
		ItemUtils.hideEnchantments(item);
		return item.clone();
	}
	
	public static ItemStack setRocketBoosterCharges(ItemStack item, int amt) {
		if (isRocketBoosterItem(item)) {
			if (ItemUtils.LoreContainsSubstring(item, ROCKET_CHARGES_LINE)) {
				ItemUtils.ModifyLoreLineContainingSubstring(item, ROCKET_CHARGES_LINE, ROCKET_CHARGES_LINE+amt);
				//item.setDurability(getRocketBoosterData(item));
				item.setItemMeta(getRocketBoosterData(item));
			} else {
				ItemUtils.addLore(item, ROCKET_CHARGES_LINE+amt);
			}
		}
		return item;
	}
	
	public static ItemStack addRocketBoosterCharges(ItemStack item, int amt) {
		if (isRocketBoosterItem(item)) {
			int charges = getRocketBoosterCharges(item);
			setRocketBoosterCharges(item,amt+charges);
		}
		return item;
	}
	
	public static ItemStack removeRocketBoosterCharges(ItemStack item, int amt) {
		if (isRocketBoosterItem(item)) {
			int charges = getRocketBoosterCharges(item);
			setRocketBoosterCharges(item,(Math.max(charges-amt,0)));
			}
		return item;
	}

	public static int getRocketBoosterCharges(ItemStack item) {
		if (isRocketBoosterItem(item) && ItemUtils.LoreContainsSubstring(item, ROCKET_CHARGES_LINE)) {
			return Integer.parseInt(ItemUtils.GetLoreLineContainingSubstring(item, ROCKET_CHARGES_LINE).replace(ROCKET_CHARGES_LINE, ""));
		} else {
			return 0;
		}
	}
	
	public static FireworkEffectMeta getRocketBoosterData(ItemStack item) {
		if (isRocketBoosterItem(item) && ItemUtils.LoreContainsSubstring(item, ROCKET_CHARGES_LINE)) {
			FireworkEffectMeta fm = (FireworkEffectMeta)item.getItemMeta();
			//TwosideKeeper.log("Lore: "+ArrayUtils.toString(fm.getLore().toArray()), 0);
			int charges = getRocketBoosterCharges(item);
			FireworkEffect.Builder builder = FireworkEffect.builder();
			if (charges>=25) {
				builder.withColor(Color.PURPLE);
			} else
			if (charges>=15) {
				builder.withColor(Color.TEAL);
			} else
			if (charges>=10) {
				builder.withColor(Color.RED);
			} else
			if (charges>=5) {
				builder.withColor(Color.ORANGE);
			} else
			if (charges>=3) {
				builder.withColor(Color.GREEN);
			} else
			if (charges>=2) {
				builder.withColor(Color.YELLOW);
			} else
			if (charges>=1) {
				builder.withColor(Color.GRAY);
			} else
			{
				builder.withColor(Color.BLACK);
			}
			FireworkEffect fe = builder.build();
			fm.setEffect(fe);
			//fm.setLore(lore);
			return fm;
		}
		return null;
	}
	
	public static boolean isRocketBoosterItem(ItemStack item) {
		return ItemUtils.isValidLoreItem(item) && item.getType()==Material.FIREWORK_CHARGE && ItemUtils.LoreContains(item, CHRISTMAS_TAG);
	}
	
	public static ItemStack getCookieItem() {
		ItemStack item = new ItemStack(Material.COOKIE);
		ItemUtils.setDisplayName(item, ChatColor.AQUA+"Holiday Cookie");
		ItemUtils.addLore(item, ChatColor.WHITE+"Heals"+ChatColor.YELLOW+" 256 "+ChatColor.RESET+"health.");
		ItemUtils.addLore(item, ChatColor.GRAY+"- Strength II (15:00)");
		ItemUtils.addLore(item, ChatColor.GRAY+"- Resistance II (15:00)");
		ItemUtils.addLore(item, ChatColor.GRAY+"- Regeneration III (15:00)");
		ItemUtils.addLore(item, ChatColor.GRAY+"- Saturation (5:00)");
		ItemUtils.addLore(item, "");
		insertChristmasTag(item);
		ItemUtils.addLore(item, ChatColor.WHITE+"A sweet and delicious cookie");
		ItemUtils.addLore(item, ChatColor.WHITE+"that provides wonderful buffs.");
		item.addUnsafeEnchantment(Enchantment.LUCK, 0);
		ItemUtils.hideEnchantments(item);
		return item.clone();
	}
	
	public static boolean isCookieItem(ItemStack item) {
		return ItemUtils.isValidLoreItem(item) && item.getType()==Material.COOKIE && ItemUtils.LoreContains(item, CHRISTMAS_TAG);
	}
	
	public static ItemStack getSmallCandyItem() {
		ItemStack item = new ItemStack(Material.RAW_FISH);
		ItemUtils.setDisplayName(item, ChatColor.AQUA+"Small Candy");
		ItemUtils.addLore(item, ChatColor.WHITE+"Heals"+ChatColor.YELLOW+" 16 "+ChatColor.RESET+"health.");
		ItemUtils.addLore(item, "");
		insertChristmasTag(item);
		ItemUtils.addLore(item, ChatColor.WHITE+"A small candy that can be consumed");
		ItemUtils.addLore(item, ChatColor.WHITE+"for a small amount of health. However,");
		ItemUtils.addLore(item, ChatColor.WHITE+"if left in your hotbar, it will");
		ItemUtils.addLore(item, ChatColor.WHITE+"automatically activate when below");
		ItemUtils.addLore(item, ChatColor.YELLOW+"half health.");
		ItemUtils.addLore(item, "");
		ItemUtils.addLore(item, ChatColor.AQUA+"Activation Ability:");
		ItemUtils.addLore(item, ChatColor.LIGHT_PURPLE+" Restores 10% of your health.");
		item.addUnsafeEnchantment(Enchantment.LUCK, 0);
		ItemUtils.hideEnchantments(item);
		return item.clone();
	}
	
	public static boolean isSmallCandyItem(ItemStack item) {
		return ItemUtils.isValidLoreItem(item) && item.getType()==Material.RAW_FISH && item.getDurability()==(short)0 && ItemUtils.LoreContains(item, CHRISTMAS_TAG);
	}
	
	public static ItemStack getLargeCandyItem() {
		ItemStack item = new ItemStack(Material.RAW_FISH);
		item.setDurability((short)1);
		ItemUtils.setDisplayName(item, ChatColor.AQUA+"Large Candy");
		ItemUtils.addLore(item, ChatColor.WHITE+"Heals"+ChatColor.YELLOW+" 36 "+ChatColor.RESET+"health.");
		ItemUtils.addLore(item, "");
		insertChristmasTag(item);
		ItemUtils.addLore(item, ChatColor.WHITE+"A large candy that can be consumed");
		ItemUtils.addLore(item, ChatColor.WHITE+"for a moderate amount of health.");
		ItemUtils.addLore(item, ChatColor.WHITE+"However, if left in your hotbar, it");
		ItemUtils.addLore(item, ChatColor.WHITE+"will automatically activate when below");
		ItemUtils.addLore(item, ChatColor.YELLOW+"half health.");
		ItemUtils.addLore(item, "");
		ItemUtils.addLore(item, ChatColor.AQUA+"Activation Ability:");
		ItemUtils.addLore(item, ChatColor.LIGHT_PURPLE+" Restores 50% of your health.");
		item.addUnsafeEnchantment(Enchantment.LUCK, 0);
		ItemUtils.hideEnchantments(item);
		return item.clone();
	}
	
	public static boolean isLargeCandyItem(ItemStack item) {
		return ItemUtils.isValidLoreItem(item) && item.getType()==Material.RAW_FISH && item.getDurability()==(short)1 && ItemUtils.LoreContains(item, CHRISTMAS_TAG);
	}
	
	
	public static ItemStack getSourCandyItem() {
		ItemStack item = new ItemStack(Material.RAW_FISH);
		item.setDurability((short)3);
		ItemUtils.setDisplayName(item, ChatColor.AQUA+"Sour Candy");
		ItemUtils.addLore(item, ChatColor.WHITE+"Heals"+ChatColor.YELLOW+" 8 "+ChatColor.RESET+"health.");
		ItemUtils.addLore(item, "");
		insertChristmasTag(item);
		ItemUtils.addLore(item, ChatColor.WHITE+"A sour candy that can be consumed");
		ItemUtils.addLore(item, ChatColor.WHITE+"for an insignificant amount of health.");
		ItemUtils.addLore(item, ChatColor.WHITE+"However, if left in your hotbar, it");
		ItemUtils.addLore(item, ChatColor.WHITE+"will automatically activate when below");
		ItemUtils.addLore(item, ChatColor.YELLOW+"half health.");
		ItemUtils.addLore(item, "");
		ItemUtils.addLore(item, ChatColor.AQUA+"Activation Ability:");
		ItemUtils.addLore(item, ChatColor.LIGHT_PURPLE+" Provides Regeneration V for 20 seconds.");
		item.addUnsafeEnchantment(Enchantment.LUCK, 0);
		ItemUtils.hideEnchantments(item);
		return item.clone();
	}
	public static boolean isSourCandyItem(ItemStack item) {
		return ItemUtils.isValidLoreItem(item) && item.getType()==Material.RAW_FISH && item.getDurability()==(short)3 && ItemUtils.LoreContains(item, CHRISTMAS_TAG);
	}
	
	public static ItemStack getSweetCandyItem() {
		ItemStack item = new ItemStack(Material.RAW_FISH);
		item.setDurability((short)2);
		ItemUtils.setDisplayName(item, ChatColor.AQUA+"Sweet Candy");
		ItemUtils.addLore(item, ChatColor.WHITE+"Heals"+ChatColor.YELLOW+" 24 "+ChatColor.RESET+"health.");
		ItemUtils.addLore(item, "");
		insertChristmasTag(item);
		ItemUtils.addLore(item, ChatColor.WHITE+"A sweet candy that can be consumed");
		ItemUtils.addLore(item, ChatColor.WHITE+"for a small amount of health.");
		ItemUtils.addLore(item, ChatColor.WHITE+"However, if left in your hotbar, it");
		ItemUtils.addLore(item, ChatColor.WHITE+"will automatically activate when killed.");
		ItemUtils.addLore(item, "");
		ItemUtils.addLore(item, ChatColor.AQUA+"Activation Ability:");
		ItemUtils.addLore(item, ChatColor.LIGHT_PURPLE+" If killed, all your items will automatically");
		ItemUtils.addLore(item, ChatColor.LIGHT_PURPLE+" be bought back for you.");
		item.addUnsafeEnchantment(Enchantment.LUCK, 0);
		ItemUtils.hideEnchantments(item);
		return item.clone();
	}
	
	public static boolean isSweetCandyItem(ItemStack item) {
		return ItemUtils.isValidLoreItem(item) && item.getType()==Material.RAW_FISH && item.getDurability()==(short)2 && ItemUtils.LoreContains(item, CHRISTMAS_TAG);
	}
	
	public static ItemStack getMysteryFlavorLollipopItem() {
		ItemStack item = new ItemStack(Material.GOLDEN_CARROT);
		ItemUtils.setDisplayName(item, ChatColor.AQUA+"Mystery Flavor Lollipop");
		ItemUtils.addLore(item, ChatColor.WHITE+"Heals"+ChatColor.YELLOW+" 17 "+ChatColor.RESET+"health.");
		ItemUtils.addLore(item, "");
		insertChristmasTag(item);
		ItemUtils.addLore(item, ChatColor.WHITE+"A lollipop that can be consumed");
		ItemUtils.addLore(item, ChatColor.WHITE+"for a small amount of health.");
		ItemUtils.addLore(item, ChatColor.WHITE+"However, if left in your hotbar, it");
		ItemUtils.addLore(item, ChatColor.WHITE+"will automatically activate when below");
		ItemUtils.addLore(item, ChatColor.YELLOW+"half health.");
		ItemUtils.addLore(item, "");
		ItemUtils.addLore(item, ChatColor.AQUA+"Activation Ability:");
		ItemUtils.addLore(item, ChatColor.LIGHT_PURPLE+" ???");
		item.addUnsafeEnchantment(Enchantment.LUCK, 0);
		ItemUtils.hideEnchantments(item);
		return item.clone();
	}
	
	public static boolean isMysteryFlavorLollipopItem(ItemStack item) {
		return ItemUtils.isValidLoreItem(item) && item.getType()==Material.GOLDEN_CARROT && ItemUtils.LoreContains(item, CHRISTMAS_TAG);
	}
	
	public static ItemStack getHolidayRageCandyBarItem() {
		ItemStack item = new ItemStack(Material.GOLDEN_APPLE);
		item.setDurability((short)1);
		ItemUtils.setDisplayName(item, ChatColor.AQUA+"Holiday Ragecandybar");
		ItemUtils.addLore(item, ChatColor.WHITE+"Heals"+ChatColor.YELLOW+" 420 "+ChatColor.RESET+"health.");
		ItemUtils.addLore(item, "");
		insertChristmasTag(item);
		ItemUtils.addLore(item, ChatColor.WHITE+"A candy bar that can be consumed");
		ItemUtils.addLore(item, ChatColor.WHITE+"for a blaze it amount of health.");
		ItemUtils.addLore(item, ChatColor.WHITE+"However, if left in your hotbar, it");
		ItemUtils.addLore(item, ChatColor.WHITE+"will automatically activate when killed.");
		ItemUtils.addLore(item, "");
		ItemUtils.addLore(item, ChatColor.AQUA+"Activation Ability:");
		ItemUtils.addLore(item, ChatColor.LIGHT_PURPLE+" When taking fatal damage, you will");
		ItemUtils.addLore(item, ChatColor.LIGHT_PURPLE+" revive instead.");
		item.addUnsafeEnchantment(Enchantment.LUCK, 0);
		ItemUtils.hideEnchantments(item);
		return item.clone();
	}
	
	public static boolean isHolidayRageCandyBarItem(ItemStack item) {
		return ItemUtils.isValidLoreItem(item) && item.getType()==Material.GOLDEN_APPLE && ItemUtils.LoreContains(item, CHRISTMAS_TAG);
	}
	
	public static ItemStack getChristmasEventToken() {
		ItemStack item = new ItemStack(Material.COMMAND_REPEATING);
		ItemUtils.setDisplayName(item, ChatColor.AQUA+"Christmas Token");
		insertChristmasTag(item);
		ItemUtils.addLore(item, ChatColor.WHITE+"A token used to obtain special");
		ItemUtils.addLore(item, ChatColor.WHITE+"treats and goodies for the Christmas");
		ItemUtils.addLore(item, ChatColor.WHITE+"event. Turn in tokens near the ");
		ItemUtils.addLore(item, ChatColor.WHITE+"Twoside Tree.");
		ItemUtils.addLore(item, "");
		item.addUnsafeEnchantment(Enchantment.LUCK, 0);
		ItemUtils.hideEnchantments(item);
		return item.clone();
	}
	
	public static boolean isChristmasEventToken(ItemStack item) {
		return ItemUtils.isValidLoreItem(item) && item.getType()==Material.COMMAND_REPEATING && ItemUtils.LoreContains(item, CHRISTMAS_TAG);
	}
	
	public static ItemStack getChristmasTreeSchematic() {
		ItemStack item = new ItemStack(Material.PAPER);
		ItemUtils.setDisplayName(item, ChatColor.AQUA+"Christmas Tree Schematic");
		insertChristmasTag(item);
		ItemUtils.addLore(item, ChatColor.WHITE+"A blueprint for the creation of an");
		ItemUtils.addLore(item, ChatColor.WHITE+"excellent Christmas Tree. Must be");
		ItemUtils.addLore(item, ChatColor.WHITE+"placed on a proper Tree plot to");
		ItemUtils.addLore(item, ChatColor.WHITE+"grow.");
		item.addUnsafeEnchantment(Enchantment.LUCK, 0);
		ItemUtils.hideEnchantments(item);
		return item.clone();
	}
	
	public static boolean isChristmasTreeSchematic(ItemStack item) {
		return ItemUtils.isValidLoreItem(item) && item.getType()==Material.PAPER && ItemUtils.LoreContains(item, CHRISTMAS_TAG);
	}
	
	public static ItemStack getChristmasBox() {
		ItemStack item = new ItemStack(Material.CHEST);
		ItemUtils.setDisplayName(item, ChatColor.AQUA+"Christmas Goodie Box");
		insertChristmasTag(item);
		ItemUtils.addLore(item, ChatColor.WHITE+"Contains wonderful goodies for the");
		ItemUtils.addLore(item, ChatColor.WHITE+"holiday season.");
		item.addUnsafeEnchantment(Enchantment.LUCK, 0);
		ItemUtils.hideEnchantments(item);
		return item.clone();
	}
	
	public static boolean isChristmasBox(ItemStack item) {
		return ItemUtils.isValidLoreItem(item) && item.getType()==Material.CHEST && ItemUtils.LoreContains(item, CHRISTMAS_TAG);
	}
	
	public static ItemStack getChristmasDecorationBox() {
		ItemStack item = new ItemStack(Material.TRAPPED_CHEST);
		ItemUtils.setDisplayName(item, ChatColor.AQUA+"Christmas Decoration Box");
		insertChristmasTag(item);
		ItemUtils.addLore(item, ChatColor.WHITE+"Contains decorations to brighten up");
		ItemUtils.addLore(item, ChatColor.WHITE+"your tree!");
		item.addUnsafeEnchantment(Enchantment.LUCK, 0);
		ItemUtils.hideEnchantments(item);
		return item.clone();
	}
	
	public static boolean isChristmasDecorationBox(ItemStack item) {
		return ItemUtils.isValidLoreItem(item) && item.getType()==Material.TRAPPED_CHEST && ItemUtils.LoreContains(item, CHRISTMAS_TAG);
	}
	
	public static ItemStack getFireworkShooterBox() {
		ItemStack item = new ItemStack(Material.DISPENSER);
		ItemUtils.setDisplayName(item, ChatColor.AQUA+"Christmas Firework Shooter");
		insertChristmasTag(item);
		ItemUtils.addLore(item, ChatColor.WHITE+"A Box that shoots infinite amounts");
		ItemUtils.addLore(item, ChatColor.WHITE+"of fireworks when powered!");
		item.addUnsafeEnchantment(Enchantment.LUCK, 0);
		ItemUtils.hideEnchantments(item);
		return item.clone();
	}
	
	public static boolean isFireworkShooterBox(ItemStack item) {
		return ItemUtils.isValidLoreItem(item) && item.getType()==Material.DISPENSER && ItemUtils.LoreContains(item, CHRISTMAS_TAG);
	}
	
	public static ItemStack getFireworkShooterToken() {
		ItemStack item = new ItemStack(Material.ACTIVATOR_RAIL);
		ItemUtils.setDisplayName(item, ChatColor.AQUA+"Christmas Firework Shooter Token");
		insertChristmasTag(item);
		ItemUtils.addLore(item, ChatColor.WHITE+"A token that powers the firework");
		ItemUtils.addLore(item, ChatColor.WHITE+"shooter.");
		item.addUnsafeEnchantment(Enchantment.LUCK, 0);
		ItemUtils.hideEnchantments(item);
		return item.clone();
	}
	
	public static boolean isFireworkShooterToken(ItemStack item) {
		return ItemUtils.isValidLoreItem(item) && item.getType()==Material.ACTIVATOR_RAIL && ItemUtils.LoreContains(item, CHRISTMAS_TAG);
	}
	
	public static ItemStack getSantaDimensionalBox() {
		ItemStack item = new ItemStack(Material.POWERED_MINECART);
		ItemUtils.setDisplayName(item, ChatColor.AQUA+"Santa's 4-Dimensional Box");
		insertChristmasTag(item);
		ItemUtils.addLore(item, ChatColor.WHITE+"The Holidays might be over... But");
		ItemUtils.addLore(item, ChatColor.WHITE+"Santa is only one dimension away...");
		ItemUtils.addLore(item, ChatColor.WHITE+"");
		ItemUtils.addLore(item, ChatColor.YELLOW+"Right-click to use.");
		item.addUnsafeEnchantment(Enchantment.LUCK, 0);
		ItemUtils.hideEnchantments(item);
		return item.clone();
	}
	
	public static boolean isSantaDimensionalBox(ItemStack item) {
		return ItemUtils.isValidLoreItem(item) && item.getType()==Material.POWERED_MINECART && ItemUtils.LoreContains(item, CHRISTMAS_TAG);
	}

	public static ItemStack getWinterSolsticeAugury() {
		ItemStack item = new ItemStack(Material.WATCH);
		ItemUtils.setDisplayName(item, ChatColor.AQUA+"Winter Solstice Crystal");
		insertChristmasTag(item);
		ItemUtils.addLore(item, ChatColor.AQUA+"Filled with limitless power, this");
		ItemUtils.addLore(item, ChatColor.AQUA+"special magic crystal will transform");
		ItemUtils.addLore(item, ChatColor.AQUA+"any being into solid ice. Their flesh");
		ItemUtils.addLore(item, ChatColor.AQUA+"becoming one with the hydrogen bonds");
		ItemUtils.addLore(item, ChatColor.AQUA+"of the cubes leads to tortuous methods");
		ItemUtils.addLore(item, ChatColor.AQUA+"of breaking such creatures, one ice");
		ItemUtils.addLore(item, ChatColor.AQUA+"block after another.");
		item.addUnsafeEnchantment(Enchantment.LUCK, 0);
		ItemUtils.hideEnchantments(item);
		return item.clone();
	}
	
	public static boolean isWinterSolsticeAugury(ItemStack item) {
		return ItemUtils.isValidLoreItem(item) && item.getType()==Material.WATCH && ItemUtils.LoreContains(item, CHRISTMAS_TAG);
	}
	
	public static void FillChristmasBox(Player p, ItemStack itemplaced, Block b) {
		if (b.getType()==Material.DISPENSER &&
				isFireworkShooterBox(itemplaced)) {
			Dispenser d = (Dispenser)b.getState();
			Inventory inv = d.getInventory();
			inv.addItem(getFireworkShooterToken());
		}
		/*if ((b.getType()==Material.CHEST ||
				b.getType()==Material.TRAPPED_CHEST) && (isChristmasBox(itemplaced) ||
				isChristmasDecorationBox(itemplaced))) {
			Chest c = (Chest)b.getState();
			Inventory inv = c.getBlockInventory();
			if (b.getType()==Material.CHEST) {
				Chests goodiedrops = aPlugin.API.Chests.LOOT_CUSTOM_2;
				//This is a christmas goodie Box.
				for (int i=0;i<27;i++) {
					ItemStack item = goodiedrops.getSingleDrop(p);
					if (item!=null) {
						inv.addItem(HandleGoodieBeforeAdding(inv,item));
					}
				}
			} else {
				Chests decorationdrops = aPlugin.API.Chests.LOOT_CUSTOM_3;
				//This is a christmas goodie Box.
				for (int i=0;i<27;i++) {
					ItemStack item = decorationdrops.getSingleDrop();
					if (item!=null) {
						inv.addItem(HandleDecorationBeforeAdding(inv,item));
					}
				}
			}
		}*/
	}
	
	private static ItemStack HandleGoodieBeforeAdding(Inventory inv, ItemStack item) {
		if (item!=null) {
			if (item.getType()==Material.FIREWORK) {
				//Create a custom firework.
				ItemStack newfirework = ItemUtils.createRandomFirework();
				newfirework.setAmount(item.getAmount());
				return newfirework;
			} else
			if (item.getType()==Material.NETHER_STAR) {
				inv.addItem(new ItemStack(Material.JUKEBOX));
			}
		}
		return item;
	}
	
	private static ItemStack HandleDecorationBeforeAdding(Inventory inv, ItemStack item) {
		if (item.getType()==Material.END_CRYSTAL) {
			inv.addItem(new ItemStack(Material.OBSIDIAN,16));
		}
		return item;
	}
	
	

	public static void InitializeChristmasBox() {
		Chests c = aPlugin.API.Chests.LOOT_CUSTOM_2;
		c.setName(getChristmasBox().getItemMeta().getDisplayName());
		c.setLore(getChristmasBox().getItemMeta().getLore());
		c.setProbability(1.0);
		c.addDrop(new DropMaterial(Material.COAL,1,4,10));
		c.addDrop(new DropMaterial(Material.IRON_INGOT,1,4,10));
		c.addDrop(new DropMaterial(Material.GOLD_INGOT,1,4,10));
		c.addDrop(new DropMaterial(Material.REDSTONE,1,4,10));
		c.addDrop(new DropMaterial(Material.GLOWSTONE_DUST,1,4,10));
		c.addDrop(new DropMaterial(Material.QUARTZ,1,4,10));
		c.addDrop(new DropMaterial(Material.INK_SACK,1,4,10,(short)4));
		c.addDrop(new DropMaterial(Material.DIAMOND,1,4,10));
		c.addDrop(new DropMaterial(Material.EMERALD,1,4,10));
		c.addDrop(new DropMaterial(Material.COAL_BLOCK,1,4,5));
		c.addDrop(new DropMaterial(Material.IRON_BLOCK,1,4,5));
		c.addDrop(new DropMaterial(Material.GOLD_BLOCK,1,4,5));
		c.addDrop(new DropMaterial(Material.REDSTONE_BLOCK,1,4,5));
		c.addDrop(new DropMaterial(Material.GLOWSTONE,1,4,5));
		c.addDrop(new DropMaterial(Material.QUARTZ_BLOCK,1,4,5));
		c.addDrop(new DropMaterial(Material.LAPIS_BLOCK,1,4,5));
		c.addDrop(new DropMaterial(Material.DIAMOND_BLOCK,1,4,5));
		c.addDrop(new DropMaterial(Material.EMERALD_BLOCK,1,4,5));
		c.addDrop(new DropRandomFirework(32,64,30));
		c.addDrop(new DropMaterial(Material.NETHER_STAR,1));
		c.addDrop(new DropItem(getCookieItem(),1,3,20));
		c.addDrop(new DropItem(getSmallCandyItem(),1,3,5));
		c.addDrop(new DropItem(getLargeCandyItem(),1,3,5));
		c.addDrop(new DropItem(getSourCandyItem(),1,3,5));
		c.addDrop(new DropItem(getSweetCandyItem(),1,3,5));
		c.addDrop(new DropItem(getMysteryFlavorLollipopItem(),1,3,5));
		c.addDrop(new DropItem(getHolidayRageCandyBarItem(),1,3,3));
		c.addDrop(new DropItem(getChristmasEventToken(),10));
		c.addDrop(new DropItem(getRocketBoosterItem(),1));
		c.addDrop(new DropItem(getFireworkShooterBox(),1));
		c.addDrop(new SigDrop(1,10,"Holiday Set Item",true,true,0,LivingEntityDifficulty.NORMAL));
		c.addDrop(new SigDrop(1,10,"Holiday Set Item",true,true,0,LivingEntityDifficulty.DANGEROUS));
		c.addDrop(new SigDrop(1,10,"Holiday Set Item",true,true,0,LivingEntityDifficulty.DEADLY));
		c.addDrop(new SigDrop(1,10,"Holiday Set Item",true,true,0,LivingEntityDifficulty.HELLFIRE));
		
		Chests Mini_Box = aPlugin.API.Chests.LOOT_CUSTOM_4;
		Mini_Box.addDrop(new DropItem(getCookieItem(),1,3,20));
		Mini_Box.addDrop(new DropItem(getSmallCandyItem(),1,3,5));
		Mini_Box.addDrop(new DropItem(getLargeCandyItem(),1,3,5));
		Mini_Box.addDrop(new DropItem(getSourCandyItem(),1,3,5));
		Mini_Box.addDrop(new DropItem(getSweetCandyItem(),1,3,5));
		Mini_Box.addDrop(new DropItem(getMysteryFlavorLollipopItem(),1,3,5));
		Mini_Box.addDrop(new DropItem(getHolidayRageCandyBarItem(),1,3,3));
		Mini_Box.addDrop(new SigDrop(1,10,"Holiday Set Item",true,true,0,LivingEntityDifficulty.NORMAL));
		Mini_Box.addDrop(new SigDrop(1,10,"Holiday Set Item",true,true,0,LivingEntityDifficulty.DANGEROUS));
		Mini_Box.addDrop(new SigDrop(1,10,"Holiday Set Item",true,true,0,LivingEntityDifficulty.DEADLY));
		Mini_Box.addDrop(new SigDrop(1,10,"Holiday Set Item",true,true,0,LivingEntityDifficulty.HELLFIRE));
		//aPlugin.API.getChestItem(c);
		Chests dec_box = aPlugin.API.Chests.LOOT_CUSTOM_3;
		dec_box.setName(getChristmasDecorationBox().getItemMeta().getDisplayName());
		dec_box.setLore(getChristmasDecorationBox().getItemMeta().getLore());
		for (int i=0;i<16;i++) {
			dec_box.addDrop(new DropMaterial(Material.STAINED_GLASS,32,64,12,(short)i));
		}
		for (int i=0;i<16;i++) {
			dec_box.addDrop(new DropMaterial(Material.STAINED_GLASS_PANE,32,64,10,(short)i));
		}
		dec_box.addDrop(new DropMaterial(Material.LEAVES,32,64,10));
		for (int i=0;i<4;i++) {
			dec_box.addDrop(new DropMaterial(Material.LOG,32,64,10,(short)i));
		}
		for (int i=0;i<2;i++) {
			dec_box.addDrop(new DropMaterial(Material.LOG_2,32,64,10,(short)i));
		}
		for (int i=0;i<3;i++) {
			dec_box.addDrop(new DropMaterial(Material.PRISMARINE,32,64,10,(short)i));
		}
		dec_box.addDrop(new DropMaterial(Material.GLOWSTONE,16,10));
		dec_box.addDrop(new DropMaterial(Material.REDSTONE_LAMP_ON,16,10));
		dec_box.addDrop(new DropMaterial(Material.REDSTONE_BLOCK,16,10));
		dec_box.addDrop(new DropMaterial(Material.BOAT,16,2));
		dec_box.addDrop(new DropMaterial(Material.BOAT_ACACIA,16,2));
		dec_box.addDrop(new DropMaterial(Material.BOAT_JUNGLE,16,2));
		dec_box.addDrop(new DropMaterial(Material.BOAT_SPRUCE,16,2));
		dec_box.addDrop(new DropMaterial(Material.BOAT_BIRCH,16,2));
		dec_box.addDrop(new DropMaterial(Material.BOAT_DARK_OAK,16,2));
		dec_box.addDrop(new DropMaterial(Material.REDSTONE_BLOCK,16,10));
		dec_box.addDrop(new DropMaterial(Material.END_CRYSTAL,10));
		dec_box.addDrop(new DropMaterial(Material.END_CRYSTAL,10));
		dec_box.addDrop(new DropMaterial(Material.END_CRYSTAL,10));
		//dec_box.addDrop(new DropMaterial(Material.TORCH,64,10));
		dec_box.addDrop(new DropMaterial(Material.END_ROD,64,20));
		dec_box.addDrop(new DropMaterial(Material.SIGN,32,64,10));
		dec_box.addDrop(new DropMaterial(Material.PURPUR_BLOCK,32,64,10));
		dec_box.addDrop(new DropMaterial(Material.QUARTZ_BLOCK,32,64,10));
		dec_box.addDrop(new DropMaterial(Material.SMOOTH_BRICK,32,64,10,(short)3));
		dec_box.addDrop(new DropMaterial(Material.SNOW,64,10));
	}
	
	public static List<ItemStack> getItemsForTopInventory(Player p) {
		List<ItemStack> items = new ArrayList<ItemStack>();
		Chests Mini_Box = aPlugin.API.Chests.LOOT_CUSTOM_4;
		for (int i=0;i<6;i++) {
			if (Math.random()<=0.8) {
				items.add(Mini_Box.getSingleDrop(p));
			}
		}
		items.add(getChristmasEventToken());
		if (Math.random()<=0.2) {
			items.add(getChristmasEventToken());
		}
		return items;
	}
	
	public static boolean RunPlayerInteractEvent(PlayerInteractEvent ev) {
		Player p = ev.getPlayer();
		if (TwosideKeeper.CHRISTMASEVENT_ACTIVATED) {
			if (ev.getAction()==Action.RIGHT_CLICK_BLOCK) {
				Block b = ev.getClickedBlock();
				if (b.getType()==Material.SMOOTH_BRICK && isChristmasTreeSchematic(p.getEquipment().getItemInMainHand())) {
					return UseTreeSchematic(ev, p, b);
				}
			}
			
			if (ev.getAction()==Action.RIGHT_CLICK_BLOCK && ev.getClickedBlock().getType()==Material.CHEST) {
				Block b = ev.getClickedBlock();
				PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
				if (b.getLocation().getBlockX()==1641 &&
						b.getLocation().getBlockY()==72 &&
						b.getLocation().getBlockZ()==-287 &&
						!pd.holidaychest1) {
					pd.holidaychest1=true;
					//Open up a new inventory. Fill it with stuff.
					return SetupHolidayChest(ev, p);
				} else 
				if (b.getLocation().getBlockX()==1649 &&
						b.getLocation().getBlockY()==74 &&
						b.getLocation().getBlockZ()==-287 &&
						!pd.holidaychest2) {
					pd.holidaychest2=true;
					//Open up a new inventory. Fill it with stuff.
					return SetupHolidayChest(ev, p);
				} else 
				if (b.getLocation().getBlockX()==1642 &&
						b.getLocation().getBlockY()==70 &&
						b.getLocation().getBlockZ()==-285 &&
						!pd.holidaychest3) {
					pd.holidaychest3=true;
					//Open up a new inventory. Fill it with stuff.
					return SetupHolidayChest(ev, p);
				} else 
				if (b.getLocation().getBlockX()==1663 &&
						b.getLocation().getBlockY()==62 &&
						b.getLocation().getBlockZ()==-285 &&
						!pd.holidaychest4) {
					pd.holidaychest4=true;
					//Open up a new inventory. Fill it with stuff.
					return SetupHolidayChest(ev, p);
				}
			}
		}
		if ((ev.getAction()==Action.RIGHT_CLICK_AIR ||
				ev.getAction()==Action.RIGHT_CLICK_BLOCK) && ev.useInteractedBlock()==Result.DENY) {
			UseRocketBooster(ev, p);
		}
		if ((ev.getAction()==Action.RIGHT_CLICK_AIR || ev.getAction()==Action.RIGHT_CLICK_BLOCK) &&
				isSantaDimensionalBox(p.getEquipment().getItemInMainHand())) {
			UseSantaDimensionalBox(p);
			ev.setUseInteractedBlock(Result.DENY);
			ev.setUseItemInHand(Result.DENY);
			ev.setCancelled(true);
			return false;
		}
		return true;
	}

	private static void UseSantaDimensionalBox(Player p) {
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		if (pd.lastsantabox2+1296000<TwosideKeeper.getServerTickTime()) {
			pd.lastsantabox2=TwosideKeeper.getServerTickTime();
			p.sendMessage("You dig into the box and pull out...");
			Bukkit.getScheduler().scheduleSyncDelayedTask(TwosideKeeper.plugin, ()->{
				ItemStack itemchosen = aPlugin.API.Chests.LOOT_CUSTOM_2.getSingleDrop(p);
				SoundUtils.playLocalSound(p, Sound.ENTITY_CHICKEN_EGG, 1.0f, 1.0f);
				p.sendMessage(ChatColor.AQUA+" "+GenericFunctions.UserFriendlyMaterialName(itemchosen)+((itemchosen.getAmount()>1)?" x"+itemchosen.getAmount():""));
				GenericFunctions.giveItem(p, itemchosen);},20);
		} else {
			p.sendMessage(ChatColor.RED+"You must wait 24 hours for Santa's Box to recharge!");
		}
	}

	public static boolean SetupHolidayChest(PlayerInteractEvent ev, Player p) {
		Inventory newinv = Bukkit.createInventory(p, 27);
		List<ItemStack> newitems = Christmas.getItemsForTopInventory(p);
		int randomslot = 0;
		for (ItemStack item : newitems) {
			do{
				randomslot = (int)(Math.random()*27);
			}while(newinv.getItem(randomslot)!=null && newinv.getItem(randomslot).getType()!=Material.AIR);
			newinv.setItem(randomslot, item);
		}
		p.openInventory(newinv);
		SoundUtils.playLocalSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
		p.sendMessage(ChatColor.GREEN+" You found a special hidden holiday crate!");
		SoundUtils.playLocalSound(p, Sound.BLOCK_CHEST_OPEN, 1.0f, 1.0f);
		ev.setUseInteractedBlock(Result.DENY);
		ev.setCancelled(true);
		return false;
	}

	public static void UseRocketBooster(PlayerInteractEvent ev, Player p) {
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		if (isRocketBoosterItem(ev.getItem()) && getRocketBoosterCharges(ev.getItem())>0 && pd.lastusedrocketbooster+5<TwosideKeeper.getServerTickTime()) {
			pd.falldamageimmunity=true;
			pd.lastusedrocketbooster=TwosideKeeper.getServerTickTime();
			removeRocketBoosterCharges(ev.getItem(),1);
			if (PlayerUtils.PlayerIsInCombat(p)) {p.setVelocity(p.getLocation().getDirection().multiply(1.25f));} else {p.setVelocity(p.getLocation().getDirection().multiply(6.0f));}
			SoundUtils.playGlobalSound(p.getLocation(), Sound.ENTITY_FIREWORK_SHOOT, 1.0f, 1.5f);
			SoundUtils.playGlobalSound(p.getLocation(), Sound.ENTITY_FIREWORK_LAUNCH, 1.0f, 1.5f);
			GenericFunctions.sendActionBarMessage(p, ChatColor.WHITE+"Charges Remaining: "+ChatColor.YELLOW+getRocketBoosterCharges(ev.getItem()), true);
		}
	}

	public static boolean UseTreeSchematic(PlayerInteractEvent ev, Player p, Block b) {
		//Remove the item from the player.
		p.getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
		//Plant a tree.
		TreeBuilder.BuildNewTree(b.getLocation().add(15,1,15), 12, 13, 0);
		p.sendMessage(ChatColor.AQUA+"You place the schematic down...");
		Bukkit.getScheduler().scheduleSyncDelayedTask(TwosideKeeper.plugin, ()->{p.sendMessage(ChatColor.GREEN+" Magic seems to activate inside the ground.");},5);
		Bukkit.getScheduler().scheduleSyncDelayedTask(TwosideKeeper.plugin, ()->{b.setType(Material.HOPPER);},2);
		Block babove = b.getRelative(0, 1, 0);
		Block bbelow = b.getRelative(0, -1, 0);
		bbelow.setType(Material.CHEST);
		babove.setType(Material.SIGN_POST);
		Sign s = (Sign)babove.getState();
		s.setLine(1, TextUtils.RandomDarkColor()+p.getName()+"'s");
		s.setLine(2, "Tree");
		s.setRawData((byte)4);
		s.update();
		ev.setUseInteractedBlock(Result.DENY);
		ev.setCancelled(true);
		return false;
	}

	public static void ChristmasHeartbeat() {
		int range = 8;
		for (Player p : Bukkit.getOnlinePlayers()) {
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			//Check a random block around the player.
			int blockx = (int)(Math.random()*(range*2))-range;
			int blockz = (int)(Math.random()*(range*2))-range;
			Block b = p.getWorld().getHighestBlockAt(p.getLocation().getBlockX()+blockx, p.getLocation().getBlockZ()+blockz);
			Block bbelow = b.getRelative(0, -1, 0);
			if (p.isOnGround()) {
				if (!aPlugin.API.isAFK(p)) {
					AddRocketBoosterCharges(p);
				}
				if (pd.falldamageimmunity && pd.lastusedrocketbooster+20<TwosideKeeper.getServerTickTime()) {
					pd.falldamageimmunity=false;
				}
			}
			if (TwosideKeeper.CHRISTMASEVENT_ACTIVATED) {
				if (b.getWorld().getName().equalsIgnoreCase("world") && b.getType()==Material.AIR && bbelow.getType().isSolid() && !bbelow.getType().name().contains("STEP") && bbelow.getType()!=Material.OBSIDIAN && bbelow.getType()!=Material.SNOW && bbelow.getType()!=Material.ICE && bbelow.getType()!=Material.PACKED_ICE && bbelow.getType()!=Material.FROSTED_ICE && GenericFunctions.isNaturalBlock(bbelow) && b.getTemperature()<=0.95) {
					b.setType(Material.SNOW);
					b.setData((byte)0);
					if (TwosideKeeper.last_snow_golem+TwosideKeeper.SNOW_GOLEM_COOLDOWN<TwosideKeeper.getServerTickTime() && Math.random()<=0.01) {
						//There might be a chance to spawn one of these.
						List<Entity> nearbyents = p.getNearbyEntities(32, 24, 32);
						boolean snowmannearby=false;
						for (Entity ent : nearbyents) {
							if (ent instanceof Snowman) {
								snowmannearby=true;
								break;
							}
						}
						if (!snowmannearby) {
							//Spawn one on the snow.
							Snowman snowy = (Snowman)p.getWorld().spawnEntity(b.getLocation().add(0,2,0), EntityType.SNOWMAN);
							snowy.setCustomName(RandomSnowmanName());
							TwosideKeeper.SnowmanHuntList.add(ChatColor.stripColor(snowy.getCustomName()));
							snowy.setMaxHealth(Math.random()*100+20);
							snowy.setHealth(snowy.getMaxHealth());
							snowy.setRemoveWhenFarAway(false);
							snowy.setAI(true);
							ItemStack cookies = getCookieItem();
							cookies.setAmount((int)(Math.random()*5)+1);
							snowy.getWorld().dropItemNaturally(snowy.getLocation(), cookies);
						}
					}
				} else
				if (b.getType()==Material.SNOW && b.getData()<7) {
					b.setData((byte)1);
				}
			} else { //Unnecessary. Snow will automatically melt.
				if (b.getType()==Material.SNOW && b.getTemperature()>0.15) {
					b.setType(Material.AIR);
				}
			}
			//aPluginAPIWrapper.sendParticle(loc, EnumParticle.SNO, dx, dy, dz, v, particleCount);
		}
	}

	public static void AddRocketBoosterCharges(Player p) {
		for (int i=0;i<9;i++) {
			ItemStack item = p.getInventory().getStorageContents()[i];
			if (isRocketBoosterItem(item)) {
				int charges = getRocketBoosterCharges(item);
				if (Math.random()<(1d/(charges+10))) {
					addRocketBoosterCharges(item,1);
					if (p.getEquipment().getItemInMainHand().isSimilar(item)) {
						GenericFunctions.sendActionBarMessage(p, ChatColor.WHITE+"Charges Remaining: "+ChatColor.YELLOW+getRocketBoosterCharges(item), true);
					}
				}
			}
		}
	}

	private static String RandomSnowmanName() {
		String[] name1 = {"Jingle","Merry","Bells",
				"Tinkle","Angel","Twinkle",
				"Rosie","Holly","Berry",
				"Festive","Candy","Magic",
				"Sparkle","Sugarplum","Joy",
				"Tinsel","Robin","Cookie",
				"Hope","Sweetie","Teddy",
				"Jolly","Cosy","Sherry",
				"Eve","Pinky"};
		String[] name2 = {"McSnowy","McSlushy","McChilly",
				"McGlisten","McSparkle","McFrosty",
				"McFreeze","McSnowballs","McIcicles",
				"McBlizzard","McSparkles","McSnowflake"};
		
		ChatColor col = TextUtils.RandomColor();
		
		return col+name1[(int)(Math.random()*name1.length)]+" "+name2[(int)(Math.random()*name2.length)];
	}

	public static boolean ChristmasDamageEvent(EntityDamageEvent ev) {
		if (ev.getCause()==DamageCause.FALL && (ev.getEntity() instanceof Player)) {
			PlayerStructure pd = PlayerStructure.GetPlayerStructure((Player)ev.getEntity());
			if (pd.falldamageimmunity) {
				ev.setCancelled(true);
				return false;
			}
		}
		if (TwosideKeeper.CHRISTMASEVENT_ACTIVATED) {
			if ((ev.getCause()==DamageCause.MELTING || ev.getCause()==DamageCause.DROWNING) && (ev.getEntity() instanceof Snowman)) {
				ev.setCancelled(true);
				return false;
			}
		}
		return true;
	}

	public static void SetupChristmas() {
		InitializeChristmasBox();
		if (TwosideKeeper.CHRISTMASEVENT_ACTIVATED) {
			Bukkit.getWorld("world").setDifficulty(Difficulty.NORMAL);
		} else {
			Bukkit.getWorld("world").setDifficulty(Difficulty.HARD);
		}
	}

	public static boolean ChristmasPlaceEvent(BlockPlaceEvent ev) {
		if (isChristmasEventToken(ev.getItemInHand()) ||
				isFireworkShooterToken(ev.getItemInHand())) {
			ev.setBuild(false);
			ev.setCancelled(true);
			return false;
		}
		return true;
	}

	public static boolean HandleDispenseEvent(BlockDispenseEvent ev) {
    	ItemStack item = ev.getItem();
    	if (isFireworkShooterToken(item)) {
    		ev.setItem(ItemUtils.createRandomFirework());
    		return false;
    	}
    	return true;
	}

	public static ItemStack GeneratePrize() {
		int val = (int)(Math.random()*100);
		if (val<=39) {
			ItemStack item = getCookieItem();
			item.setAmount((int)((Math.random()*6)+1));
			return item;
		} else
		if (val<=50) {
			ItemStack item = getRandomHolidayTreat();
			item.setAmount((int)((Math.random()*17)+16));
			return item;
		} else
		if (val<=68) {
			ItemStack item = getRandomSetGear();
			return item;
		} else
		if (val<=72) {
			ItemStack item = new ItemStack(Material.PAPER);
			return item;
		} else
		if (val<=85) {
			ItemStack item = getChristmasBox();
			if (Math.random()<=0.1) {
				item.setAmount(2);
			} else
			if (Math.random()<=0.01) {
				item.setAmount(3);
			}
			return item;
		} else
		if (val<=91) {
			ItemStack item = getRocketBoosterItem();
			return item;
		} else
		if (val<=93) {
			ItemStack item = getFireworkShooterBox();
			return item;
		} else
		if (val<=95) {
			ItemStack item = GenerateRandomSetItem();
			return item;
		} else
		{
			ItemStack item = generateUniqueHolidayWeapon();
			return item;
		}
	}

	private static ItemStack getRandomHolidayTreat() {
		ItemStack[] treats = new ItemStack[]{
				getCookieItem(),
				getCookieItem(),
				getSmallCandyItem(),
				getSmallCandyItem(),
				getLargeCandyItem(),
				getLargeCandyItem(),
				getSourCandyItem(),
				getSourCandyItem(),
				getSweetCandyItem(),
				getSweetCandyItem(),
				getMysteryFlavorLollipopItem(),
				getMysteryFlavorLollipopItem(),
				getHolidayRageCandyBarItem(),
				};
		return treats[(int)(Math.random()*treats.length)];
	}

	private static ItemStack generateUniqueHolidayWeapon() {
		return getWinterSolsticeAugury();
	}

	public static ItemStack GenerateRandomSetItem() {
		ItemStack item = new ItemStack(PickRandomMaterial());
		ItemSet set = LivingEntityDifficulty.PickAnItemSet(PlayerMode.values()[(int)(Math.random()*PlayerMode.values().length)], (Math.random()<=0.5)?LivingEntityDifficulty.DEADLY:LivingEntityDifficulty.HELLFIRE);
		item = LivingEntityDifficulty.ConvertSetPieceIfNecessary(item, set);
		item = Loot.GenerateSetPiece(item, set, true, 2);
		return item;
	}

	private static Material PickRandomMaterial() {
		Material[] materials = {
				Material.LEATHER_HELMET,
				Material.LEATHER_CHESTPLATE,
				Material.LEATHER_LEGGINGS,
				Material.LEATHER_BOOTS,
				Material.IRON_HELMET,
				Material.IRON_CHESTPLATE,
				Material.IRON_LEGGINGS,
				Material.IRON_BOOTS,
				Material.DIAMOND_HELMET,
				Material.DIAMOND_CHESTPLATE,
				Material.DIAMOND_LEGGINGS,
				Material.DIAMOND_BOOTS,
				Material.GOLD_HELMET,
				Material.GOLD_CHESTPLATE,
				Material.GOLD_LEGGINGS,
				Material.GOLD_BOOTS,
				Material.BOW,
				Material.BOW,
				Material.BOW,
				Material.SKULL_ITEM,
				Material.SKULL_ITEM,
				Material.SKULL_ITEM,
				Material.IRON_SWORD,
				Material.DIAMOND_SWORD,
				Material.GOLD_SWORD,
				Material.IRON_AXE,
				Material.DIAMOND_AXE,
				Material.GOLD_AXE,
				Material.SHIELD,
				Material.SHIELD,
				Material.SHIELD,
		};
		return materials[(int)(Math.random()*materials.length)];
	}

	public static Material PickRandomArmorMaterial() {
		Material[] materials = {
				Material.LEATHER_HELMET,
				Material.LEATHER_CHESTPLATE,
				Material.LEATHER_LEGGINGS,
				Material.LEATHER_BOOTS,
				Material.IRON_HELMET,
				Material.IRON_CHESTPLATE,
				Material.IRON_LEGGINGS,
				Material.IRON_BOOTS,
				Material.DIAMOND_HELMET,
				Material.DIAMOND_CHESTPLATE,
				Material.DIAMOND_LEGGINGS,
				Material.DIAMOND_BOOTS,
				Material.GOLD_HELMET,
				Material.GOLD_CHESTPLATE,
				Material.GOLD_LEGGINGS,
				Material.GOLD_BOOTS,
		};
		return materials[(int)(Math.random()*materials.length)];
	}

	private static ItemStack getRandomSetGear() {
		ItemStack item = new ItemStack(PickRandomArmorMaterial());
		ItemSet set = LivingEntityDifficulty.PickAHolidayItemSet(PlayerMode.values()[(int)(Math.random()*PlayerMode.values().length)], (Math.random()<=0.5)?LivingEntityDifficulty.DEADLY:LivingEntityDifficulty.HELLFIRE);
		item = LivingEntityDifficulty.ConvertSetPieceIfNecessary(item, set);
		item = Loot.GenerateSetPiece(item, set, true, 2);
		return item;
	}

	public static void RunPlayerItemHeldEvent(PlayerItemHeldEvent ev) {
		Player p = ev.getPlayer();
		ItemStack item = p.getInventory().getItem(ev.getNewSlot());
		if (isRocketBoosterItem(item)) {
			GenericFunctions.sendActionBarMessage(p, ChatColor.WHITE+"Charges Remaining: "+ChatColor.YELLOW+getRocketBoosterCharges(item), true);
		}
	}

	public static boolean NoSweetCandyInInventory(Player p) {
		for (int i=0;i<p.getInventory().getContents().length;i++) {
			if (Christmas.isSweetCandyItem(p.getInventory().getItem(i))) {
				p.sendMessage(ChatColor.GREEN+"A "+GenericFunctions.UserFriendlyMaterialName(p.getInventory().getItem(i))+" was consumed to save your items!");
				SoundUtils.playLocalSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
				CustomDamage.RemoveOneItem(p.getInventory(), p.getInventory().getItem(i), i);
				return false;
			}
		}
		return true;
	}

	public static boolean runInventoryClickEvent(InventoryClickEvent ev) {
		if ((ev.getClick()==ClickType.RIGHT) &&
				isSantaDimensionalBox(ev.getCurrentItem())) {
			UseSantaDimensionalBox((Player)ev.getWhoClicked());
			ev.setCancelled(true);
			return false;
		}
		return true;
	}
}