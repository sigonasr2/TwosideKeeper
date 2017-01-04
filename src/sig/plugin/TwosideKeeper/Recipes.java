package sig.plugin.TwosideKeeper;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import sig.plugin.TwosideKeeper.HelperStructures.ArtifactItem;
import sig.plugin.TwosideKeeper.HelperStructures.ArtifactItemType;

public class Recipes {
	public static void Initialize_ItemCube_Recipes() {
		
		
		Bukkit.addRecipe(TwosideKeeper.ITEM_CUBE_RECIPE);
		//------------------------------
		
		Bukkit.addRecipe(TwosideKeeper.LARGE_ITEM_CUBE_RECIPE);
		//------------------------------
		
		Bukkit.addRecipe(TwosideKeeper.ENDER_ITEM_CUBE_RECIPE);
		//------------------------------
		
		Bukkit.addRecipe(TwosideKeeper.DUPLICATE_ENDER_ITEM_CUBE_RECIPE);
		
		Bukkit.addRecipe(TwosideKeeper.VACUUM_CUBE_RECIPE);
		
		Bukkit.addRecipe(TwosideKeeper.FILTER_CUBE_RECIPE);
	}
	public static void Initialize_ArrowQuiver_Recipe() {
		Bukkit.addRecipe(TwosideKeeper.ARROW_QUIVER_RECIPE);
	}
	public static void Initialize_BlockArmor_Recipes() {
		//--------------------------------------------
		Bukkit.addRecipe(TwosideKeeper.HARDENED_IRON_HELMET_RECIPE);
		Bukkit.addRecipe(TwosideKeeper.HARDENED_IRON_CHESTPLATE_RECIPE);
		Bukkit.addRecipe(TwosideKeeper.HARDENED_IRON_LEGGINGS_RECIPE);
		Bukkit.addRecipe(TwosideKeeper.HARDENED_IRON_BOOTS_RECIPE);
		Bukkit.addRecipe(TwosideKeeper.HARDENED_DIAMOND_HELMET_RECIPE);
		Bukkit.addRecipe(TwosideKeeper.HARDENED_DIAMOND_CHESTPLATE_RECIPE);
		Bukkit.addRecipe(TwosideKeeper.HARDENED_DIAMOND_LEGGINGS_RECIPE);
		Bukkit.addRecipe(TwosideKeeper.HARDENED_DIAMOND_BOOTS_RECIPE);
		Bukkit.addRecipe(TwosideKeeper.HARDENED_GOLD_HELMET_RECIPE);
		Bukkit.addRecipe(TwosideKeeper.HARDENED_GOLD_CHESTPLATE_RECIPE);
		Bukkit.addRecipe(TwosideKeeper.HARDENED_GOLD_LEGGINGS_RECIPE);
		Bukkit.addRecipe(TwosideKeeper.HARDENED_GOLD_BOOTS_RECIPE);
		//--------------------------------------------
	}
	@SuppressWarnings("deprecation")
	public static void Initialize_WoolRecolor_Recipes() {
		for (int i=0;i<16;i++) {
			ShapedRecipe wool_recolor_recipe = new ShapedRecipe(new ItemStack(Material.WOOL,8,(byte)(15-i)));
			wool_recolor_recipe.shape("www","wdw","www");
			wool_recolor_recipe.setIngredient('w', Material.WOOL, -1);
			wool_recolor_recipe.setIngredient('d', Material.getMaterial(351), i);
			Bukkit.addRecipe(wool_recolor_recipe);
		}
	}
	@SuppressWarnings("deprecation")
	public static void Initialize_SlabReconstruction_Recipes() {
		for (int i=0;i<=5;i++) {
			ShapelessRecipe plank_construction_recipe = new ShapelessRecipe(new ItemStack(Material.WOOD,1,(byte)i));
			plank_construction_recipe.addIngredient(2, Material.getMaterial(126), i);
			Bukkit.addRecipe(plank_construction_recipe);
		}
		ShapelessRecipe sandstone_construction_recipe = new ShapelessRecipe(new ItemStack(Material.RED_SANDSTONE,1));
		sandstone_construction_recipe.addIngredient(2, Material.getMaterial(182));
		Bukkit.addRecipe(sandstone_construction_recipe);
		ShapelessRecipe stone_construction_recipe = new ShapelessRecipe(new ItemStack(Material.STONE,1));
		stone_construction_recipe.addIngredient(2, Material.getMaterial(44), 0);
		Bukkit.addRecipe(stone_construction_recipe);
		stone_construction_recipe = new ShapelessRecipe(new ItemStack(Material.SANDSTONE,1));
		stone_construction_recipe.addIngredient(2, Material.getMaterial(44), 1);
		Bukkit.addRecipe(stone_construction_recipe);
		stone_construction_recipe = new ShapelessRecipe(new ItemStack(Material.WOOD,1));
		stone_construction_recipe.addIngredient(2, Material.getMaterial(44), 2);
		Bukkit.addRecipe(stone_construction_recipe);
		stone_construction_recipe = new ShapelessRecipe(new ItemStack(Material.COBBLESTONE,1));
		stone_construction_recipe.addIngredient(2, Material.getMaterial(44), 3);
		Bukkit.addRecipe(stone_construction_recipe);
		stone_construction_recipe = new ShapelessRecipe(new ItemStack(Material.BRICK,1));
		stone_construction_recipe.addIngredient(2, Material.getMaterial(44), 4);
		Bukkit.addRecipe(stone_construction_recipe);
		stone_construction_recipe = new ShapelessRecipe(new ItemStack(Material.getMaterial(98),1));
		stone_construction_recipe.addIngredient(2, Material.getMaterial(44), 5);
		Bukkit.addRecipe(stone_construction_recipe);
		stone_construction_recipe = new ShapelessRecipe(new ItemStack(Material.NETHER_BRICK,1));
		stone_construction_recipe.addIngredient(2, Material.getMaterial(44), 6);
		Bukkit.addRecipe(stone_construction_recipe);
		stone_construction_recipe = new ShapelessRecipe(new ItemStack(Material.QUARTZ_BLOCK,1));
		stone_construction_recipe.addIngredient(2, Material.getMaterial(44), 7);
		Bukkit.addRecipe(stone_construction_recipe);
		stone_construction_recipe = new ShapelessRecipe(new ItemStack(Material.PURPUR_BLOCK,1));
		stone_construction_recipe.addIngredient(2, Material.PURPUR_SLAB);
		Bukkit.addRecipe(stone_construction_recipe);
		stone_construction_recipe = new ShapelessRecipe(new ItemStack(Material.QUARTZ,4));
		stone_construction_recipe.addIngredient(1, Material.QUARTZ_BLOCK);
		Bukkit.addRecipe(stone_construction_recipe);
		ItemStack modded_plank = new ItemStack(Material.STEP,1);
		modded_plank.setDurability((short)2);
		ItemMeta m = modded_plank.getItemMeta();
		m.setDisplayName("Fireproof Oak Wooden Slab");
		modded_plank.setItemMeta(m);
		stone_construction_recipe = new ShapelessRecipe(modded_plank);
		stone_construction_recipe.addIngredient(1, Material.WOOD_STEP);
		stone_construction_recipe.addIngredient(1, Material.SLIME_BALL);
		Bukkit.addRecipe(stone_construction_recipe);
	}
	@SuppressWarnings("deprecation")
	public static void Initialize_Artifact_Recipes() {
		//Essence Recipes.
		//T0
		for (int i=0;i<ArtifactItemType.values().length;i++) {
			Bukkit.addRecipe(ArtifactItemType.values()[i].defineBaseRecipe());
			//ArtifactItemType.values()[i].defineAllDecompRecipes();
			//T1,T4,T7 Recipes
			ShapelessRecipe newrecipe = new ShapelessRecipe(Artifact.convert(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)ArtifactItemType.values()[i].getDataValue())));
			newrecipe.addIngredient(2, Material.STAINED_GLASS_PANE, ArtifactItemType.values()[i].getDataValue());
			newrecipe.addIngredient(Material.SUGAR);
			Bukkit.addRecipe(newrecipe);
			newrecipe = new ShapelessRecipe(Artifact.convert(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)ArtifactItemType.values()[i].getDataValue())));
			newrecipe.addIngredient(2, Material.STAINED_GLASS_PANE, ArtifactItemType.values()[i].getDataValue());
			newrecipe.addIngredient(Material.MAGMA_CREAM);
			Bukkit.addRecipe(newrecipe);
			newrecipe = new ShapelessRecipe(Artifact.convert(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)ArtifactItemType.values()[i].getDataValue())));
			newrecipe.addIngredient(2, Material.STAINED_GLASS_PANE, ArtifactItemType.values()[i].getDataValue());
			newrecipe.addIngredient(Material.CLAY_BALL);
			Bukkit.addRecipe(newrecipe);
			newrecipe = new ShapelessRecipe(Artifact.convert(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)ArtifactItemType.values()[i].getDataValue())));
			newrecipe.addIngredient(2, Material.STAINED_GLASS_PANE, ArtifactItemType.values()[i].getDataValue());
			newrecipe.addIngredient(Material.SUGAR);
			newrecipe.addIngredient(Material.MAGMA_CREAM);
			newrecipe.addIngredient(Material.CLAY_BALL);
			Bukkit.addRecipe(newrecipe);
			
			//Recipe -> Base Item Recipe.
			newrecipe = new ShapelessRecipe(ArtifactItemType.values()[i].getTieredItem(1));
			newrecipe.addIngredient(1, Material.STAINED_GLASS_PANE, ArtifactItemType.values()[i].getDataValue());
			Bukkit.addRecipe(newrecipe);
			
			//Upgrade Recipe
			ArtifactItemType.values()[i].defineAllUpgradeRecipes();
		}
	}
	public static void Initialize_ArtifactHelper_Recipes() {
		ShapelessRecipe upgraderecipe = new ShapelessRecipe(Artifact.createArtifactItem(ArtifactItem.DIVINE_ESSENCE));
		upgraderecipe.addIngredient(Material.NETHER_STAR);
		upgraderecipe.addIngredient(Material.SUGAR);
		Bukkit.addRecipe(upgraderecipe);
		upgraderecipe = new ShapelessRecipe(Artifact.createArtifactItem(ArtifactItem.DIVINE_CORE));
		upgraderecipe.addIngredient(Material.NETHER_STAR);
		upgraderecipe.addIngredient(Material.MAGMA_CREAM);
		Bukkit.addRecipe(upgraderecipe);
		upgraderecipe = new ShapelessRecipe(Artifact.createArtifactItem(ArtifactItem.DIVINE_BASE));
		upgraderecipe.addIngredient(Material.NETHER_STAR);
		upgraderecipe.addIngredient(Material.CLAY_BALL);
		Bukkit.addRecipe(upgraderecipe);
		ItemStack newitem = Artifact.createArtifactItem(ArtifactItem.ARTIFACT_ESSENCE);
		newitem.setAmount(2);
		upgraderecipe = new ShapelessRecipe(newitem);
		upgraderecipe.addIngredient(Material.SUGAR);
		Bukkit.addRecipe(upgraderecipe);
		newitem = Artifact.createArtifactItem(ArtifactItem.ARTIFACT_CORE);
		newitem.setAmount(2);
		upgraderecipe = new ShapelessRecipe(newitem);
		upgraderecipe.addIngredient(Material.MAGMA_CREAM);
		Bukkit.addRecipe(upgraderecipe);
		newitem = Artifact.createArtifactItem(ArtifactItem.ARTIFACT_BASE);
		newitem.setAmount(2);
		upgraderecipe = new ShapelessRecipe(newitem);
		upgraderecipe.addIngredient(Material.CLAY_BALL);
		Bukkit.addRecipe(upgraderecipe);
		newitem = Artifact.createArtifactItem(ArtifactItem.ARTIFACT_ESSENCE);
		upgraderecipe = new ShapelessRecipe(newitem);
		upgraderecipe.addIngredient(2,Material.SUGAR);
		Bukkit.addRecipe(upgraderecipe);
		newitem = Artifact.createArtifactItem(ArtifactItem.ARTIFACT_CORE);
		upgraderecipe = new ShapelessRecipe(newitem);
		upgraderecipe.addIngredient(2,Material.MAGMA_CREAM);
		Bukkit.addRecipe(upgraderecipe);
		newitem = Artifact.createArtifactItem(ArtifactItem.ARTIFACT_BASE);
		upgraderecipe = new ShapelessRecipe(newitem);
		upgraderecipe.addIngredient(2,Material.CLAY_BALL);
		Bukkit.addRecipe(upgraderecipe);
	}
	public static void Initialize_Check_Recipe() {
		ItemStack check = new ItemStack(Material.PAPER);
		check.addUnsafeEnchantment(Enchantment.LUCK, 1);
		ItemMeta m = check.getItemMeta();
		m.setDisplayName("Money Cheque");
		List<String> lore = new ArrayList<String>();
		lore.add("An unsigned check. "+ChatColor.YELLOW+"Right-click");
		lore.add("the check while holding it to");
		lore.add("write a value and sign the check.");
		m.setLore(lore);
		check.setItemMeta(m);
		ShapelessRecipe checkrecipe = new ShapelessRecipe(check);
		checkrecipe.addIngredient(Material.INK_SACK);
		checkrecipe.addIngredient(Material.PAPER);
		checkrecipe.addIngredient(Material.FEATHER);
		Bukkit.addRecipe(checkrecipe);
	}
	@SuppressWarnings("deprecation")
	public static void Initialize_CustomArrow_Recipes() {
		
		ItemStack handmadearrow = getArrowFromMeta("DOUBLE_DAMAGE_ARR");
		ShapelessRecipe handmadearrow_recipe = new ShapelessRecipe(handmadearrow);
		handmadearrow_recipe.addIngredient(Material.FLINT);
		handmadearrow_recipe.addIngredient(Material.STICK);
		handmadearrow_recipe.addIngredient(Material.FEATHER);
		Bukkit.addRecipe(handmadearrow_recipe);
		
		ItemStack diamondtippedarrow = getArrowFromMeta("QUADRUPLE_DAMAGE_ARR");
		ShapelessRecipe diamondtippedarrow_recipe = new ShapelessRecipe(diamondtippedarrow);
		diamondtippedarrow_recipe.addIngredient(Material.TIPPED_ARROW);
		diamondtippedarrow_recipe.addIngredient(Material.DIAMOND);
		Bukkit.addRecipe(diamondtippedarrow_recipe);


		ItemStack poisonarrow = getArrowFromMeta("POISON_ARR");
		ShapelessRecipe poisonarrow_recipe = new ShapelessRecipe(poisonarrow);
		poisonarrow_recipe.addIngredient(Material.RAW_FISH,3);
		poisonarrow_recipe.addIngredient(Material.STICK);
		poisonarrow_recipe.addIngredient(Material.FEATHER);
		Bukkit.addRecipe(poisonarrow_recipe);


		ItemStack trappingarrow = getArrowFromMeta("TRAP_ARR");
		ShapelessRecipe trappingarrow_recipe = new ShapelessRecipe(trappingarrow);
		
		trappingarrow_recipe.addIngredient(Material.WEB);
		trappingarrow_recipe.addIngredient(Material.STICK);
		trappingarrow_recipe.addIngredient(Material.FEATHER);
		Bukkit.addRecipe(trappingarrow_recipe);
		
		
		ItemStack explosionarrow = getArrowFromMeta("EXPLODE_ARR");
		ShapelessRecipe explosionarrow_recipe = new ShapelessRecipe(explosionarrow);
		explosionarrow_recipe.addIngredient(Material.SULPHUR);
		explosionarrow_recipe.addIngredient(Material.STICK);
		explosionarrow_recipe.addIngredient(Material.FEATHER);
		Bukkit.addRecipe(explosionarrow_recipe);
		
		ItemStack piercingarrow = getArrowFromMeta("PIERCING_ARR");
		ShapelessRecipe piercingarrow_recipe = new ShapelessRecipe(piercingarrow);
		
		piercingarrow_recipe.addIngredient(Material.FLINT);
		piercingarrow_recipe.addIngredient(4,Material.REDSTONE);
		piercingarrow_recipe.addIngredient(Material.STICK);
		piercingarrow_recipe.addIngredient(Material.FEATHER);
		Bukkit.addRecipe(piercingarrow_recipe);
		
		ShapelessRecipe piercingarrow2_recipe = new ShapelessRecipe(piercingarrow);
		piercingarrow2_recipe.addIngredient(4,Material.REDSTONE);
		piercingarrow2_recipe.addIngredient(Material.TIPPED_ARROW);
		Bukkit.addRecipe(piercingarrow2_recipe);
	}

	public static void Initialize_NotchApple_Recipe() {
		ShapelessRecipe notchapple_recipe = new ShapelessRecipe(new ItemStack(Material.GOLDEN_APPLE,1,(short)1));
		notchapple_recipe.addIngredient(8,Material.GOLD_BLOCK);
		notchapple_recipe.addIngredient(Material.APPLE);
		Bukkit.addRecipe(notchapple_recipe);
	}
	
	public static ItemStack getArrowFromMeta(String string) {
		switch (string) {
			case "EXPLODE_ARR": {
				ItemStack explosionarrow = new ItemStack(Material.TIPPED_ARROW);
				PotionMeta pm = (PotionMeta)explosionarrow.getItemMeta();
				pm.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
				//pm.setBasePotionData(data);
				pm.addCustomEffect(new PotionEffect(PotionEffectType.INVISIBILITY,0,0),true);
				List<String> lore = new ArrayList<String>();
				lore.add(ChatColor.GRAY+"Explodes on Contact (+60 dmg)");
				pm.setLore(lore);
				pm.setDisplayName(ChatColor.GRAY+"Exploding Arrow");
				explosionarrow.setItemMeta(pm);
				return explosionarrow;
			}
			case "TRAP_ARR": {
				ItemStack trappingarrow = new ItemStack(Material.TIPPED_ARROW);
				PotionMeta pm = (PotionMeta)trappingarrow.getItemMeta();
				pm.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
				//pm.setBasePotionData(data);
				pm.addCustomEffect(new PotionEffect(PotionEffectType.WEAKNESS,0,0),true);
				List<String> lore = new ArrayList<String>();
				lore.add(ChatColor.GRAY+"Applies Stacking Slowness (0:05)");
				pm.setLore(lore);
				pm.setDisplayName(ChatColor.DARK_GREEN+"Trapping Arrow");
				trappingarrow.setItemMeta(pm);
				return trappingarrow;
			}
			case "POISON_ARR": {
				ItemStack poisonarrow = new ItemStack(Material.TIPPED_ARROW);
				PotionMeta pm = (PotionMeta)poisonarrow.getItemMeta();
				pm.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
				//pm.setBasePotionData(data);
				pm.addCustomEffect(new PotionEffect(PotionEffectType.POISON,0,0),true);
				List<String> lore = new ArrayList<String>();
				lore.add(ChatColor.GRAY+"Applies Poison I (0:20)");
				pm.setLore(lore);
				pm.setDisplayName(ChatColor.DARK_GREEN+"Poison-Tipped Arrow");
				poisonarrow.setItemMeta(pm);
				return poisonarrow;
			}
			case "QUADRUPLE_DAMAGE_ARR": {
				ItemStack diamondtippedarrow = new ItemStack(Material.TIPPED_ARROW);
				PotionMeta pm = (PotionMeta)diamondtippedarrow.getItemMeta();
				pm.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
				//pm.setBasePotionData(data);
				pm.addCustomEffect(new PotionEffect(PotionEffectType.SPEED,0,0),true);
				List<String> lore = new ArrayList<String>();
				lore.add(ChatColor.GRAY+"+15 Damage");
				pm.setLore(lore);
				pm.setDisplayName(ChatColor.AQUA+"Diamond-Tipped Arrow");
				diamondtippedarrow.setItemMeta(pm);
				return diamondtippedarrow;
			}
			case "DOUBLE_DAMAGE_ARR": {
				ItemStack handmadearrow = new ItemStack(Material.TIPPED_ARROW);
				PotionMeta pm = (PotionMeta)handmadearrow.getItemMeta();
				pm.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
				//pm.setBasePotionData(data);
				pm.addCustomEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,0,0),true);
				List<String> lore = new ArrayList<String>();
				lore.add(ChatColor.GRAY+"+5 Damage");
				pm.setLore(lore);
				pm.setDisplayName(ChatColor.YELLOW+"Handmade Arrow");
				handmadearrow.setItemMeta(pm);
				return handmadearrow;
			}
			case "PIERCING_ARR": {
				ItemStack piercingarrow = new ItemStack(Material.TIPPED_ARROW);
				PotionMeta pm = (PotionMeta)piercingarrow.getItemMeta();
				pm.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
				//pm.setBasePotionData(data);
				pm.addCustomEffect(new PotionEffect(PotionEffectType.HEAL,0,0),true);
				List<String> lore = new ArrayList<String>();
				lore.add(ChatColor.GRAY+"+5 Damage");
				lore.add(ChatColor.RED+"Goes through all targets.");
				pm.setLore(lore);
				pm.setDisplayName(ChatColor.RED+"Piercing Arrow");
				piercingarrow.setItemMeta(pm);
				return piercingarrow;
			}
		}
		return new ItemStack(Material.TIPPED_ARROW);
	}
	public static void Initialize_NewRedstoneLamp_Recipe() {
		ItemStack newredstonelamp = new ItemStack(Material.REDSTONE_LAMP_ON);
		ShapelessRecipe recipe = new ShapelessRecipe(newredstonelamp);
		recipe.addIngredient(4,Material.REDSTONE);
		recipe.addIngredient(4,Material.GLOWSTONE_DUST);
		recipe.addIngredient(Material.OBSIDIAN);
		Bukkit.addRecipe(recipe);
	}
}
