package sig.plugin.TwosideKeeper.HelperStructures;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;

public class Loot {
	
	static double HARDENED_ENCHANT_MULT = 1.4;
	static int MAX_ENCHANT_LEVEL = 10;

	public static ItemStack GenerateMegaPiece(Material mat_type, boolean hardened) {
		return GenerateMegaPiece(mat_type, hardened, false);
	}

	public static ItemStack GenerateMegaPiece(Material mat_type, boolean hardened, boolean setitem) {
		return GenerateMegaPiece(mat_type,hardened,setitem,0);
	}
	
	public static ItemStack GenerateMegaPiece(Material mat_type, boolean hardened, boolean setitem, int settier) {
		ItemStack raresword = new ItemStack(mat_type);
		ItemMeta sword_meta = raresword.getItemMeta();
		sword_meta.setDisplayName(ChatColor.AQUA+""+ChatColor.BOLD+"Mega "+GenericFunctions.UserFriendlyMaterialName(mat_type));
		List<String> fakelore = new ArrayList<String>();
		fakelore.add(" ");
		sword_meta.setLore(fakelore);
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

		if (setitem && (raresword.getType().toString().contains("SWORD") || GenericFunctions.isArmor(raresword))) {
			raresword = GenerateSetPiece(raresword,hardened,settier);
		}
		
		return raresword;
	}
	
	public static ItemStack GenerateRangerPiece(Material mat_type, boolean hardened, int tier) {
		ItemStack raresword = new ItemStack(mat_type);
		ItemMeta sword_meta = raresword.getItemMeta();
		List<String> fakelore = new ArrayList<String>();
		//Choose a green/yellow/white/brown color.
		// Brown - Low Tier (3% - 62% max): Jamdak
		// White - Middle Tier (5% - 70% max): Darnys
		// Yellow - High Tier (8% - 82% max): Alikahn
		// Green - Max Tier (11% - 94% max): Lorasaadi
		switch (tier) {
			default: {
				sword_meta.setDisplayName(ChatColor.AQUA+""+ChatColor.BOLD+"Mega Jamdak Ranger "+GenericFunctions.UserFriendlyMaterialName(mat_type).replace("Leather ", ""));
				if (mat_type.toString().contains("LEATHER")) {
					fakelore.add(ChatColor.LIGHT_PURPLE+"Ranger Gear");
					fakelore.add(ChatColor.GOLD+""+ChatColor.BOLD+"Jamdak Set");
					fakelore.add(ChatColor.YELLOW+"+3% Dodge Chance");
					fakelore.add("");
					fakelore.add(ChatColor.GOLD+""+ChatColor.ITALIC+"Set Bonus:");
					fakelore.add(ChatColor.DARK_AQUA+" 4 - "+ChatColor.WHITE+" Increases duration of Tumble to 3 seconds.");
					fakelore.add(ChatColor.DARK_AQUA+" 4 - "+ChatColor.WHITE+" Increases Damage Reduction by 20%.");
					fakelore.add(ChatColor.DARK_AQUA+" 4 - "+ChatColor.WHITE+" Increases Maximum Health by 20.");
				}
				LeatherArmorMeta lm = (LeatherArmorMeta)sword_meta;
				lm.setColor(Color.fromRGB(128, 64, 0));
			}break;
			case 2: {
				sword_meta.setDisplayName(ChatColor.AQUA+""+ChatColor.BOLD+"Mega Darnys Ranger "+GenericFunctions.UserFriendlyMaterialName(mat_type).replace("Leather ", ""));
				if (mat_type.toString().contains("LEATHER")) {
					fakelore.add(ChatColor.LIGHT_PURPLE+"Ranger Gear");
					fakelore.add(ChatColor.GOLD+""+ChatColor.BOLD+"Darnys Set");
					fakelore.add(ChatColor.YELLOW+"+5% Dodge Chance");
					fakelore.add("");
					fakelore.add(ChatColor.GOLD+""+ChatColor.ITALIC+"Set Bonus:");
					fakelore.add(ChatColor.DARK_AQUA+" 4 - "+ChatColor.WHITE+" Increases duration of Tumble to 3 seconds.");
					fakelore.add(ChatColor.DARK_AQUA+" 4 - "+ChatColor.WHITE+" Increases Damage Reduction by 20%.");
					fakelore.add(ChatColor.DARK_AQUA+" 4 - "+ChatColor.WHITE+" Increases Maximum Health by 20.");
					LeatherArmorMeta lm = (LeatherArmorMeta)sword_meta;
					lm.setColor(Color.fromRGB(224, 224, 224));
				}
			}break;
			case 3: {
				sword_meta.setDisplayName(ChatColor.LIGHT_PURPLE+""+ChatColor.BOLD+"Hardened Mega Alikahn Ranger "+GenericFunctions.UserFriendlyMaterialName(mat_type).replace("Leather ", ""));
				if (mat_type.toString().contains("LEATHER")) {
					fakelore.add(ChatColor.LIGHT_PURPLE+"Ranger Gear");
					fakelore.add(ChatColor.GOLD+""+ChatColor.BOLD+"Alikahn Set");
					fakelore.add(ChatColor.YELLOW+"+8% Dodge Chance");
					fakelore.add(ChatColor.GRAY+"Breaks Remaining: "+ChatColor.YELLOW+GetHardenedBreaks(mat_type));
					fakelore.add("");
					fakelore.add(ChatColor.GOLD+""+ChatColor.ITALIC+"Set Bonus (Ranger Only):");
					fakelore.add(ChatColor.DARK_AQUA+" 4 - "+ChatColor.WHITE+" Increases duration of Tumble to 3 seconds.");
					fakelore.add(ChatColor.DARK_AQUA+" 4 - "+ChatColor.WHITE+" Increases Damage Reduction by 20%.");
					fakelore.add(ChatColor.DARK_AQUA+" 4 - "+ChatColor.WHITE+" Increases Maximum Health by 20.");
					LeatherArmorMeta lm = (LeatherArmorMeta)sword_meta;
					lm.setColor(Color.fromRGB(64, 0, 64));
				}
			}break;
			case 4: {
				sword_meta.setDisplayName(ChatColor.LIGHT_PURPLE+""+ChatColor.BOLD+"Hardened Mega Lorasaadi Ranger "+GenericFunctions.UserFriendlyMaterialName(mat_type).replace("Leather ", ""));
				if (mat_type.toString().contains("LEATHER")) {
					fakelore.add(ChatColor.LIGHT_PURPLE+"Ranger Gear");
					fakelore.add(ChatColor.GOLD+""+ChatColor.BOLD+"Lorasaadi Set");
					fakelore.add(ChatColor.YELLOW+"+11% Dodge Chance");
					fakelore.add(ChatColor.GRAY+"Breaks Remaining: "+ChatColor.YELLOW+GetHardenedBreaks(mat_type));
					fakelore.add("");
					fakelore.add(ChatColor.GOLD+""+ChatColor.ITALIC+"Set Bonus:");
					fakelore.add(ChatColor.DARK_AQUA+" 4 - "+ChatColor.WHITE+" Increases duration of Tumble to 3 seconds.");
					fakelore.add(ChatColor.DARK_AQUA+" 4 - "+ChatColor.WHITE+" Increases Damage Reduction by 20%.");
					fakelore.add(ChatColor.DARK_AQUA+" 4 - "+ChatColor.WHITE+" Increases Maximum Health by 20.");
					LeatherArmorMeta lm = (LeatherArmorMeta)sword_meta;
					lm.setColor(Color.fromRGB(0, 64, 0));
				}
			}break;
		}
		sword_meta.setLore(fakelore);
		raresword.setItemMeta(sword_meta);
		raresword = addEnchantments(raresword,false);
		return raresword;
	}

	public static ItemStack GenerateSetPiece(ItemStack item, boolean hardened) {
		return GenerateSetPiece(item,hardened,0);
	}
	
	public static ItemStack GenerateSetPiece(ItemStack item, boolean hardened, int tierbonus) {
		List<String> lore = new ArrayList<String>();
		int type = (int)(Math.random()*3);
		String set_name = "";
		String prefix = "";
		prefix = (hardened)?(ChatColor.LIGHT_PURPLE+""+ChatColor.BOLD+"Hardened Mega "):(ChatColor.AQUA+""+ChatColor.BOLD+"Mega ");
		ItemSet set = null;
		switch (type) {
			case 0:{
				set = ItemSet.PANROS;
				set_name = prefix+"Panros Striker "+GenericFunctions.UserFriendlyMaterialName(item.getType()); //Striker set.
			}break;
			case 1:{
				set = ItemSet.SONGSTEEL;
				if (item.getType().toString().contains("SWORD")) {
					item.setType(Material.SHIELD);
				}
				set_name = prefix+"Songsteel Defender "+GenericFunctions.UserFriendlyMaterialName(item.getType()); //Defender set.
			}break;
			case 2:{
				set = ItemSet.DAWNTRACKER;
				if (item.getType().toString().contains("SWORD")) {
					item.setType(Material.valueOf(item.getType().toString().replace("SWORD","")+"AXE"));
				}
				set_name = prefix+"Dawntracker Barbarian "+GenericFunctions.UserFriendlyMaterialName(item.getType());
			}break;
			case 3:{
				set = ItemSet.LORASYS;
				if (item.getType().toString().contains("SWORD")) {
					//Convert Slayer weapon here. ???
				}
				set_name = prefix+"Lorasys Slayer "+GenericFunctions.UserFriendlyMaterialName(item.getType());
			}break;
		}
		if (item.getItemMeta().hasLore()) {
			lore = item.getItemMeta().getLore();
		}
		int tier = tierbonus;
		do {tier++;} while(Math.random()<=0.25);
		lore.addAll(ItemSet.GenerateLore(set,tier));
		ItemMeta m = item.getItemMeta();
		m.setLore(lore);
		m.setDisplayName(set_name);
		item.setItemMeta(m);
		return item;
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
		if (type.toString().contains("LEATHER")) {
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
			enchantment_level = (int)(((Math.random()*6)+3)*((hardened)?HARDENED_ENCHANT_MULT:1)); 
		}
		
		if (enchantment_level>MAX_ENCHANT_LEVEL) {
			enchantment_level = 10;
		}
		
		return enchantment_level;
	}
	
	public static ItemStack addEnchantments(ItemStack item, boolean hardened) {
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
			//item.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
			if (Math.random()<0.001*HARDENED_ENCHANT_MULT) {item.addUnsafeEnchantment(Enchantment.MENDING, GetEnchantmentLevels(item.getType(),hardened));}
		} else {
			//Generic Random Enchantments.
			for (int i=0;i<Enchantment.values().length;i++) {
				if (Math.random()<1.0/Enchantment.values().length*HARDENED_ENCHANT_MULT) {
					item.addUnsafeEnchantment(Enchantment.values()[i], GetEnchantmentLevels(item.getType(),hardened));
				}
			}
		}
		if (item.getType()==Material.FISHING_ROD) {
			item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, GetEnchantmentLevels(item.getType(),hardened));
			item.addUnsafeEnchantment(Enchantment.DURABILITY, GetEnchantmentLevels(item.getType(),hardened));
			item.addUnsafeEnchantment(Enchantment.LUCK, GetEnchantmentLevels(item.getType(),hardened));
			item.addUnsafeEnchantment(Enchantment.LURE, (int)(((Math.random()*3)+2)*((hardened)?HARDENED_ENCHANT_MULT:1)));
		}
		return item;
	}
}
