package sig.plugin.TwosideKeeper.HelperStructures;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;

public class Loot {
	
	static double HARDENED_ENCHANT_MULT = 1.4;
	static int MAX_ENCHANT_LEVEL = 10;
	
	public static ItemStack GenerateMegaPiece(Material mat_type, boolean hardened) {
		ItemStack raresword = new ItemStack(mat_type);
		ItemMeta sword_meta = raresword.getItemMeta();
		sword_meta.setDisplayName(ChatColor.AQUA+""+ChatColor.BOLD+"Mega "+GenericFunctions.UserFriendlyMaterialName(mat_type));
		raresword.setItemMeta(sword_meta);
		raresword = addEnchantments(raresword,false);
		if (hardened) {
			sword_meta.setDisplayName(ChatColor.LIGHT_PURPLE+""+ChatColor.BOLD+"Hardened Mega "+GenericFunctions.UserFriendlyMaterialName(mat_type));
			List<String> lore = new ArrayList<String>();
			lore.add(ChatColor.GRAY+"Breaks Remaining: "+ChatColor.YELLOW+GetHardenedBreaks(mat_type));
			sword_meta.setLore(lore);
			raresword.setItemMeta(sword_meta);
			raresword = addEnchantments(raresword,true);
		}
		return raresword;
	}
	
	private static int GetHardenedBreaks(Material type) {
		if (type.toString().contains("STONE")) {
			return (int)((Math.random()*3)+2); 
		} else
		if (type.toString().contains("IRON")) {
			return (int)((Math.random()*4)+3); 
		} else
		if (type.toString().contains("DIAMOND")) {
			return (int)((Math.random()*7)+5); 
		} else
		if (type.toString().contains("GOLD")) {
			return (int)((Math.random()*12)+10); 
		} else
		{
			return 5;
		}
	}

	private static int GetEnchantmentLevels(Material type) {
		return GetEnchantmentLevels(type, false);
	}
	
	private static int GetEnchantmentLevels(Material type, boolean hardened) {
		int enchantment_level = 0;
		if (type.toString().contains("STONE")) {
			enchantment_level = (int)(((Math.random()*3)+2)*((hardened)?HARDENED_ENCHANT_MULT:1)); 
		} else
		if (type.toString().contains("IRON")) {
			enchantment_level = (int)(((Math.random()*4)+3)*((hardened)?HARDENED_ENCHANT_MULT:1)); 
		} else
		if (type.toString().contains("DIAMOND")) {
			enchantment_level = (int)(((Math.random()*4)+5)*((hardened)?HARDENED_ENCHANT_MULT:1)); 
		} else
		if (type.toString().contains("GOLD")) {
			enchantment_level = (int)(((Math.random()*5)+6)*((hardened)?HARDENED_ENCHANT_MULT:1)); 
		} else
		if (type.toString().contains("BOW")) {
			enchantment_level = (int)(((Math.random()*5)+6)*((hardened)?HARDENED_ENCHANT_MULT:1)); 
		} else
		{
			enchantment_level = (int)(((Math.random()*9)+1)*((hardened)?HARDENED_ENCHANT_MULT:1)); 
		}
		
		if (enchantment_level>MAX_ENCHANT_LEVEL) {
			enchantment_level = 10;
		}
		
		return enchantment_level;
	}
	
	private static ItemStack addEnchantments(ItemStack item, boolean hardened) {
		if (GenericFunctions.isHarvestingTool(item)) {
			item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, GetEnchantmentLevels(item.getType(),hardened));
			item.addUnsafeEnchantment(Enchantment.DIG_SPEED, GetEnchantmentLevels(item.getType(),hardened));
			item.addUnsafeEnchantment(Enchantment.DURABILITY, GetEnchantmentLevels(item.getType(),hardened));
			item.addUnsafeEnchantment(Enchantment.KNOCKBACK, 2);
			if (item.getType().toString().contains("HOE")) {item.addUnsafeEnchantment(Enchantment.KNOCKBACK, 10);}
			if (Math.random()<0.2*HARDENED_ENCHANT_MULT) {item.addUnsafeEnchantment(Enchantment.SILK_TOUCH, GetEnchantmentLevels(item.getType(),hardened));}
			if (Math.random()<0.001*HARDENED_ENCHANT_MULT) {item.addUnsafeEnchantment(Enchantment.MENDING, GetEnchantmentLevels(item.getType(),hardened));}
			if (Math.random()<0.001*HARDENED_ENCHANT_MULT) {item.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, GetEnchantmentLevels(item.getType(),hardened));}
		} else 
		if (item.getType()==Material.BOW) {
			item.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, GetEnchantmentLevels(item.getType(),hardened));
			item.addUnsafeEnchantment(Enchantment.ARROW_FIRE, GetEnchantmentLevels(item.getType(),hardened));
			item.addUnsafeEnchantment(Enchantment.DURABILITY, GetEnchantmentLevels(item.getType(),hardened));
			item.addUnsafeEnchantment(Enchantment.ARROW_KNOCKBACK, 2);
			if (Math.random()<0.2*HARDENED_ENCHANT_MULT) {item.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, GetEnchantmentLevels(item.getType(),hardened));}
			if (Math.random()<0.001*HARDENED_ENCHANT_MULT) {item.addUnsafeEnchantment(Enchantment.MENDING, GetEnchantmentLevels(item.getType(),hardened));}
		} else 
		if (GenericFunctions.isWeapon(item)) {
			item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, GetEnchantmentLevels(item.getType(),hardened));
			item.addUnsafeEnchantment(Enchantment.DAMAGE_ARTHROPODS, GetEnchantmentLevels(item.getType(),hardened));
			item.addUnsafeEnchantment(Enchantment.DAMAGE_UNDEAD, GetEnchantmentLevels(item.getType(),hardened));
			item.addUnsafeEnchantment(Enchantment.DURABILITY, GetEnchantmentLevels(item.getType(),hardened));
			item.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, GetEnchantmentLevels(item.getType(),hardened));
			item.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
			if (Math.random()<0.001*HARDENED_ENCHANT_MULT) {item.addUnsafeEnchantment(Enchantment.MENDING, GetEnchantmentLevels(item.getType(),hardened));}
			if (Math.random()<0.001*HARDENED_ENCHANT_MULT) {item.addUnsafeEnchantment(Enchantment.LOOT_BONUS_MOBS, GetEnchantmentLevels(item.getType(),hardened));}
		} else 
		if (GenericFunctions.isArmor(item)) {
			item.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, GetEnchantmentLevels(item.getType(),hardened));
			if (Math.random()<0.5*HARDENED_ENCHANT_MULT) {item.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, GetEnchantmentLevels(item.getType(),hardened));}
			if (Math.random()<0.5*HARDENED_ENCHANT_MULT) {item.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, GetEnchantmentLevels(item.getType(),hardened));}
			if (Math.random()<0.5*HARDENED_ENCHANT_MULT) {item.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, GetEnchantmentLevels(item.getType(),hardened));}
			if (Math.random()<0.5*HARDENED_ENCHANT_MULT) {item.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, GetEnchantmentLevels(item.getType(),hardened));}
			if (Math.random()<0.2*HARDENED_ENCHANT_MULT && item.getType().toString().contains("BOOTS")) {item.addUnsafeEnchantment(Enchantment.DEPTH_STRIDER, GetEnchantmentLevels(item.getType(),hardened));}
			if (Math.random()<0.2*HARDENED_ENCHANT_MULT && item.getType().toString().contains("HELMET")) {item.addUnsafeEnchantment(Enchantment.WATER_WORKER, 1);}
			if (Math.random()<0.2*HARDENED_ENCHANT_MULT && item.getType().toString().contains("HELMET")) {item.addUnsafeEnchantment(Enchantment.OXYGEN, 3);}
			if (Math.random()<0.2*HARDENED_ENCHANT_MULT && item.getType().toString().contains("BOOTS")) {item.addUnsafeEnchantment(Enchantment.FROST_WALKER, GetEnchantmentLevels(item.getType(),hardened));}
			if (Math.random()<0.08*HARDENED_ENCHANT_MULT) {item.addUnsafeEnchantment(Enchantment.THORNS, GetEnchantmentLevels(item.getType(),hardened));}
			item.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
			if (Math.random()<0.001*HARDENED_ENCHANT_MULT) {item.addUnsafeEnchantment(Enchantment.MENDING, GetEnchantmentLevels(item.getType(),hardened));}
		} else 
		if (item.getType()==Material.FISHING_ROD) {
			item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, GetEnchantmentLevels(item.getType(),hardened));
			item.addUnsafeEnchantment(Enchantment.DURABILITY, GetEnchantmentLevels(item.getType(),hardened));
			item.addUnsafeEnchantment(Enchantment.LUCK, GetEnchantmentLevels(item.getType(),hardened));
			item.addUnsafeEnchantment(Enchantment.LURE, GetEnchantmentLevels(item.getType(),hardened));
		} else {
			//Generic Random Enchantments.
			for (int i=0;i<Enchantment.values().length;i++) {
				if (Math.random()<1.0/Enchantment.values().length*HARDENED_ENCHANT_MULT) {
					item.addUnsafeEnchantment(Enchantment.values()[i], GetEnchantmentLevels(item.getType(),hardened));
				}
			}
		}
		return item;
	}
}
