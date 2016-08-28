package sig.plugin.TwosideKeeper;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import sig.plugin.TwosideKeeper.HelperStructures.ArtifactItem;
import sig.plugin.TwosideKeeper.HelperStructures.ArtifactItemType;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;

public class Artifact {
	public static ItemStack createArtifactItem(ArtifactItem type) {
		return createArtifactItem(type,1);
	}
	public static ItemStack createArtifactItem(ArtifactItem type, int amt) {
		ItemStack i = null;
		switch (type) {
			case ANCIENT_BASE:
				i=new ItemStack(Material.CLAY_BALL,amt);
				break;
			case ANCIENT_CORE:
				i=new ItemStack(Material.MAGMA_CREAM,amt);
				break;
			case ANCIENT_ESSENCE:
				i=new ItemStack(Material.SUGAR,amt);
				break;
			case ARTIFACT_BASE:
				i=new ItemStack(Material.CLAY_BALL,amt);
				break;
			case ARTIFACT_CORE:
				i=new ItemStack(Material.MAGMA_CREAM,amt);
				break;
			case ARTIFACT_ESSENCE:
				i=new ItemStack(Material.SUGAR,amt);
				break;
			case DIVINE_BASE:
				i=new ItemStack(Material.CLAY_BALL,amt);
				break;
			case DIVINE_CORE:
				i=new ItemStack(Material.MAGMA_CREAM,amt);
				break;
			case DIVINE_ESSENCE:
				i=new ItemStack(Material.SUGAR,amt);
				break;
			case LOST_BASE:
				i=new ItemStack(Material.CLAY_BALL,amt);
				break;
			case LOST_CORE:
				i=new ItemStack(Material.MAGMA_CREAM,amt);
				break;
			case LOST_ESSENCE:
				i=new ItemStack(Material.SUGAR,amt);
				break;
			case MALLEABLE_BASE:
				i=new ItemStack(Material.INK_SACK,amt,(short) 7);
				break;
			case MYSTERIOUS_ESSENCE:
				i=new ItemStack(Material.PUMPKIN_SEEDS,amt);
    			TwosideKeeper.EssenceLogger.AddGeneralEssence();
				break;
			case ARTIFACT_RECIPE:
				i=new ItemStack(Material.STAINED_GLASS_PANE,amt);
				break;
			default:
				i=new ItemStack(Material.AIR);
				break;
		} 
		return convert(setName(i,type),type,true);
	}
	public static ItemStack setName(ItemStack i, ArtifactItem type) {
		ItemMeta m = i.getItemMeta();
		switch (type) {
			case ANCIENT_BASE:
				m.setDisplayName(ChatColor.BLUE+""+ChatColor.BOLD+"Ancient Base");
				break;
			case ANCIENT_CORE:
				m.setDisplayName(ChatColor.BLUE+""+ChatColor.BOLD+"Ancient Core");
				break;
			case ANCIENT_ESSENCE:
				m.setDisplayName(ChatColor.BLUE+""+ChatColor.BOLD+"Ancient Essence");
				break;
			case ARTIFACT_BASE:
				m.setDisplayName(ChatColor.BLUE+""+ChatColor.BOLD+"Artifact Base");
				break;
			case ARTIFACT_CORE:
				m.setDisplayName(ChatColor.BLUE+""+ChatColor.BOLD+"Artifact Core");
				break;
			case ARTIFACT_ESSENCE:
				m.setDisplayName(ChatColor.BLUE+""+ChatColor.BOLD+"Artifact Essence");
				break;
			case DIVINE_BASE:
				m.setDisplayName(ChatColor.BLUE+""+ChatColor.BOLD+"Divine Base");
				break;
			case DIVINE_CORE:
				m.setDisplayName(ChatColor.BLUE+""+ChatColor.BOLD+"Divine Core");
				break;
			case DIVINE_ESSENCE:
				m.setDisplayName(ChatColor.BLUE+""+ChatColor.BOLD+"Divine Essence");
				break;
			case LOST_BASE:
				m.setDisplayName(ChatColor.BLUE+""+ChatColor.BOLD+"Lost Base");
				break;
			case LOST_CORE:
				m.setDisplayName(ChatColor.BLUE+""+ChatColor.BOLD+"Lost Core");
				break;
			case LOST_ESSENCE:
				m.setDisplayName(ChatColor.BLUE+""+ChatColor.BOLD+"Lost Essence");
				break;
			case MALLEABLE_BASE:
				m.setDisplayName(ChatColor.BLUE+""+ChatColor.BOLD+"Malleable Base");
				break;
			case MYSTERIOUS_ESSENCE:
				m.setDisplayName(ChatColor.LIGHT_PURPLE+""+ChatColor.BOLD+"Mysterious Essence");
				break;
			default:
				break;
		}
		i.setItemMeta(m);
		return i;
	}
	public static ItemStack convert(ItemStack item, ArtifactItem type, boolean reprint_lore) {
		//Converts an item to an artifact.
		item = item.clone();
		ItemMeta m = item.getItemMeta();
		List<String> l = new ArrayList<String>();
		if (item.getItemMeta().hasLore()) {
			l = item.getItemMeta().getLore();
		}
		if (reprint_lore) {
			l.add(ChatColor.GOLD+""+ChatColor.ITALIC+"Artifact Crafting Item");
			if (type==ArtifactItem.MALLEABLE_BASE) {
				l.add(ChatColor.YELLOW+"  Right-click to activate");
				l.add(ChatColor.YELLOW+"  this base.");
			}
		}
		m.setLore(l);
		item.setItemMeta(m);
		if (type.toString().contains("ARTIFACT")) {
			item.addUnsafeEnchantment(Enchantment.LUCK, 1);
		} else
		if (type.toString().contains("ANCIENT")) {
			item.addUnsafeEnchantment(Enchantment.LUCK, 2);
		} else
		if (type.toString().contains("LOST")) {
			item.addUnsafeEnchantment(Enchantment.LUCK, 3);
		} else
		if (type.toString().contains("DIVINE")) {
			item.addUnsafeEnchantment(Enchantment.LUCK, 4);
		} else {
			item.addUnsafeEnchantment(Enchantment.LUCK, 10);
		}
		return item;
	}
	public static ItemStack convert(ItemStack item, boolean reprint_lore) {
		//Converts an item to an artifact.
		return convert(item, ArtifactItem.ARTIFACT_ESSENCE, reprint_lore);
	}
	public static ItemStack convert(ItemStack item) {
		//Converts an item to an artifact.
		return convert(item, ArtifactItem.ARTIFACT_ESSENCE, true);
	}

	public static ItemStack convert_equip(ItemStack item, int tier, ArtifactItemType ait) {
		//Converts an item to an artifact.
		ItemMeta m = item.getItemMeta();
		List<String> l = new ArrayList<String>();
		if (item.getItemMeta().hasLore()) {
			l = item.getItemMeta().getLore();
		}
		l.add(ChatColor.GOLD+""+ChatColor.BOLD+"T"+tier+ChatColor.RESET+ChatColor.GOLD+" "+GenericFunctions.CapitalizeFirstLetters(ait.getItemName())+" Artifact");
		l.add(ChatColor.GOLD+""+ChatColor.ITALIC+"Artifact Item");
		m.setLore(l);
		item.setItemMeta(m);
		item.addUnsafeEnchantment(Enchantment.LUCK, tier);
		return item;
	}
	public static boolean isArtifact(ItemStack item) {
		if (item!=null &&
				item.getType()!=Material.AIR &&
				item.hasItemMeta() &&
				item.getItemMeta().hasLore() &&
				(GenericFunctions.searchfor(item.getItemMeta().getLore(),ChatColor.GOLD+""+ChatColor.ITALIC+"Artifact Crafting Item") ||
					item.getItemMeta().getLore().contains(ChatColor.GOLD+""+ChatColor.ITALIC+"Artifact Item"))) {
			//This is an artifact.
			return true;
		} else {
			return false;
		}
	}
	public static boolean isMalleableBase(ItemStack item) {
		//Check for type of item, and if it's an artifact.
		if (isArtifact(item) && 
				item.getType()==Material.INK_SACK &&
				item.getDurability()==7) {
			return true;
		} else {
			return false;
		}
	}
	public static boolean isMysteriousEssence(ItemStack item) {
		//Check for type of item, and if it's an artifact.
		if (isArtifact(item) && 
				item.getType()==Material.PUMPKIN_SEEDS) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * This method adds in related information based on what type of recipe the item is.
	 * @param tier
	 * @param item
	 */
	public static ItemStack createRecipe(int tier, ArtifactItemType item) {
		if (tier==0) {
			ItemStack newitem = convert(new ItemStack(Material.STAINED_GLASS_PANE,1,(short)item.getDataValue()));
			ItemMeta m = newitem.getItemMeta();
			List<String> lore = m.getLore();
			lore.add(0,ChatColor.YELLOW+"Base Crafting Recipe");
			//lore.add(1,ChatColor.GOLD+""+ChatColor.BOLD+"T"+tier+ChatColor.RESET+ChatColor.GOLD+" "+GenericFunctions.CapitalizeFirstLetters(item.getItemName())+" Recipe");
			m.setLore(lore);
			m.setDisplayName(ChatColor.BOLD+"Base Artifact "+GenericFunctions.CapitalizeFirstLetters(item.getItemName())+" Recipe");
			newitem.setItemMeta(m);
			return newitem;
		} else
		{
			ItemStack newitem = convert(new ItemStack(Material.STAINED_GLASS_PANE,1,(short)item.getDataValue()));
			ItemMeta m = newitem.getItemMeta();
			List<String> lore = m.getLore();
			lore.add(0,ChatColor.YELLOW+GenericFunctions.CapitalizeFirstLetters(item.getItemName())+" Crafting Recipe");
			lore.add(1,ChatColor.GOLD+""+ChatColor.BOLD+"T"+tier+ChatColor.RESET+ChatColor.GOLD+" "+GenericFunctions.CapitalizeFirstLetters(item.getItemName())+" Recipe");
			m.setLore(lore);
			m.setDisplayName(ChatColor.GOLD+""+ChatColor.BOLD+"T"+tier+ChatColor.RESET+ChatColor.GOLD+" Artifact "+GenericFunctions.CapitalizeFirstLetters(item.getItemName())+" Recipe");
			newitem.setItemMeta(m);
			return newitem;
		}
	}
	
	public static String returnRawTool(Material type) {
		if (type.toString().contains("PICKAXE")) {
			return "PICKAXE";
		} else 
		if (type.toString().contains("AXE")) {
			return "AXE";
		} else 
		if (type.toString().contains("SPADE")) {
			return "SHOVEL";
		} else 
		if (type.toString().contains("SWORD")) {
			return "SWORD";
		} else 
		if (type.toString().contains("BOW") ||
				type.toString().equalsIgnoreCase("BOW")) {
			return "BOW";
		} else 
		if (type.toString().contains("HOE")) {
			return "HOE";
		} else 
		if (type.toString().contains("CHESTPLATE")) {
			return "CHESTPLATE";
		} else 
		if (type.toString().contains("HELMET")) {
			return "HELMET";
		} else 
		if (type.toString().contains("LEGGINGS")) {
			return "LEGGINGS";
		} else 
		if (type.toString().contains("BOOTS")) {
			return "BOOTS";
		}  else 
		if (type.toString().contains("ROD")) {
			return "FISHING ROD";
		} else {
			return "";
		}
	}
}
