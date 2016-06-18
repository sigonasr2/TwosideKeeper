package sig.plugin.TwosideKeeper;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import sig.plugin.TwosideKeeper.HelperStructures.ArtifactItem;

public class Artifact {
	public static ItemStack createArtifactItem(ArtifactItem type) {
		ItemStack i = null;
		switch (type) {
			case ANCIENT_BASE:
				i=new ItemStack(Material.CLAY_BALL);
				break;
			case ANCIENT_CORE:
				i=new ItemStack(Material.MAGMA_CREAM);
				break;
			case ANCIENT_ESSENCE:
				i=new ItemStack(Material.SUGAR);
				break;
			case ARTIFACT_BASE:
				i=new ItemStack(Material.CLAY_BALL);
				break;
			case ARTIFACT_CORE:
				i=new ItemStack(Material.MAGMA_CREAM);
				break;
			case ARTIFACT_ESSENCE:
				i=new ItemStack(Material.SUGAR);
				break;
			case DIVINE_BASE:
				i=new ItemStack(Material.CLAY_BALL);
				break;
			case DIVINE_CORE:
				i=new ItemStack(Material.MAGMA_CREAM);
				break;
			case DIVINE_ESSENCE:
				i=new ItemStack(Material.SUGAR);
				break;
			case LOST_BASE:
				i=new ItemStack(Material.CLAY_BALL);
				break;
			case LOST_CORE:
				i=new ItemStack(Material.MAGMA_CREAM);
				break;
			case LOST_ESSENCE:
				i=new ItemStack(Material.SUGAR);
				break;
			case MALLEABLE_BASE:
				i=new ItemStack(Material.INK_SACK,1,(short) 7);
				break;
			case MYSTERIOUS_ESSENCE:
				i=new ItemStack(Material.PUMPKIN_SEEDS);
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
				m.setDisplayName(ChatColor.BOLD+""+ChatColor.BLUE+"Ancient Base");
				break;
			case ANCIENT_CORE:
				m.setDisplayName(ChatColor.BOLD+""+ChatColor.BLUE+"Ancient Core");
				break;
			case ANCIENT_ESSENCE:
				m.setDisplayName(ChatColor.BOLD+""+ChatColor.BLUE+"Ancient Essence");
				break;
			case ARTIFACT_BASE:
				m.setDisplayName(ChatColor.BOLD+""+ChatColor.BLUE+"Artifact Base");
				break;
			case ARTIFACT_CORE:
				m.setDisplayName(ChatColor.BOLD+""+ChatColor.BLUE+"Artifact Core");
				break;
			case ARTIFACT_ESSENCE:
				m.setDisplayName(ChatColor.BOLD+""+ChatColor.BLUE+"Artifact Essence");
				break;
			case DIVINE_BASE:
				m.setDisplayName(ChatColor.BOLD+""+ChatColor.BLUE+"Divine Base");
				break;
			case DIVINE_CORE:
				m.setDisplayName(ChatColor.BOLD+""+ChatColor.BLUE+"Divine Core");
				break;
			case DIVINE_ESSENCE:
				m.setDisplayName(ChatColor.BOLD+""+ChatColor.BLUE+"Divine Essence");
				break;
			case LOST_BASE:
				m.setDisplayName(ChatColor.BOLD+""+ChatColor.BLUE+"Lost Base");
				break;
			case LOST_CORE:
				m.setDisplayName(ChatColor.BOLD+""+ChatColor.BLUE+"Lost Core");
				break;
			case LOST_ESSENCE:
				m.setDisplayName(ChatColor.BOLD+""+ChatColor.BLUE+"Lost Essence");
				break;
			case MALLEABLE_BASE:
				m.setDisplayName(ChatColor.BOLD+""+ChatColor.BLUE+"Malleable Base");
				break;
			case MYSTERIOUS_ESSENCE:
				m.setDisplayName(ChatColor.BOLD+""+ChatColor.LIGHT_PURPLE+"Mysterious Essence");
				break;
		}
		i.setItemMeta(m);
		return i;
	}
	public static ItemStack convert(ItemStack item, ArtifactItem type, boolean reprint_lore) {
		//Converts an item to an artifact.
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
	public static boolean isArtifact(ItemStack item) {
		if (item.hasItemMeta() &&
				item.getItemMeta().hasLore() &&
				item.getItemMeta().getLore().contains(ChatColor.GOLD+""+ChatColor.ITALIC+"Artifact Crafting Item")) {
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
}
