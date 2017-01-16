package sig.plugin.TwosideKeeper.HelperStructures;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Item;
import org.bukkit.entity.TippedArrow;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import sig.plugin.TwosideKeeper.Artifact;
import sig.plugin.TwosideKeeper.Recipes;
import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.HelperStructures.Common.ArrowQuiver;
import sig.plugin.TwosideKeeper.HelperStructures.Common.BaublePouch;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.ItemUtils;

public class CustomItem {
	ItemStack item;
	
	public CustomItem(ItemStack item) {
		this.item=item;
	}
	
	public ItemStack getItemStack() {
		return getItemStack(1);
	}
	
	public ItemStack getItemStack(int amt) {
		ItemStack temp = item.clone();
		temp.setAmount(amt);
		return temp;
	}

	public static void InitializeItemRecipes() {
		TwosideKeeper.ITEM_CUBE_RECIPE = ItemCubeRecipe();
		TwosideKeeper.LARGE_ITEM_CUBE_RECIPE = LargeItemCubeRecipe();
		TwosideKeeper.ENDER_ITEM_CUBE_RECIPE = EnderItemCubeRecipe();
		TwosideKeeper.DUPLICATE_ENDER_ITEM_CUBE_RECIPE = DuplicateEnderItemCubeRecipe();
		TwosideKeeper.VACUUM_CUBE_RECIPE = VacuumCubeRecipe();
		TwosideKeeper.FILTER_CUBE_RECIPE = FilterCubeRecipe();
		TwosideKeeper.ARROW_QUIVER_RECIPE = ArrowQuiverRecipe();
		TwosideKeeper.HARDENED_IRON_HELMET_RECIPE = HardenedRecipe(Material.IRON_HELMET,Material.IRON_BLOCK,"aaa","axa","xxx");
		TwosideKeeper.HARDENED_IRON_CHESTPLATE_RECIPE = HardenedRecipe(Material.IRON_CHESTPLATE,Material.IRON_BLOCK,"axa","aaa","aaa");
		TwosideKeeper.HARDENED_IRON_LEGGINGS_RECIPE = HardenedRecipe(Material.IRON_LEGGINGS,Material.IRON_BLOCK,"aaa","axa","axa");
		TwosideKeeper.HARDENED_IRON_BOOTS_RECIPE = HardenedRecipe(Material.IRON_BOOTS,Material.IRON_BLOCK,"axa","axa","xxx");
		TwosideKeeper.HARDENED_DIAMOND_HELMET_RECIPE = HardenedRecipe(Material.DIAMOND_HELMET,Material.DIAMOND_BLOCK,"aaa","axa","xxx");
		TwosideKeeper.HARDENED_DIAMOND_CHESTPLATE_RECIPE = HardenedRecipe(Material.DIAMOND_CHESTPLATE,Material.DIAMOND_BLOCK,"axa","aaa","aaa");
		TwosideKeeper.HARDENED_DIAMOND_LEGGINGS_RECIPE = HardenedRecipe(Material.DIAMOND_LEGGINGS,Material.DIAMOND_BLOCK,"aaa","axa","axa");
		TwosideKeeper.HARDENED_DIAMOND_BOOTS_RECIPE = HardenedRecipe(Material.DIAMOND_BOOTS,Material.DIAMOND_BLOCK,"axa","axa","xxx");
		TwosideKeeper.HARDENED_GOLD_HELMET_RECIPE = HardenedRecipe(Material.GOLD_HELMET,Material.GOLD_BLOCK,"aaa","axa","xxx");
		TwosideKeeper.HARDENED_GOLD_CHESTPLATE_RECIPE = HardenedRecipe(Material.GOLD_CHESTPLATE,Material.GOLD_BLOCK,"axa","aaa","aaa");
		TwosideKeeper.HARDENED_GOLD_LEGGINGS_RECIPE = HardenedRecipe(Material.GOLD_LEGGINGS,Material.GOLD_BLOCK,"aaa","axa","axa");
		TwosideKeeper.HARDENED_GOLD_BOOTS_RECIPE = HardenedRecipe(Material.GOLD_BOOTS,Material.GOLD_BLOCK,"axa","axa","xxx");
		TwosideKeeper.WOOL_RECOLOR_RECIPE = WoolRecolorRecipe();
		TwosideKeeper.SLAB_RECONSTRUCTION_RECIPE = SlabReconstructionRecipe();
		TwosideKeeper.ARTIFACT_RECIPE_T1_SWORD_RECIPE = ArtifactItemType.SWORD.defineBaseRecipe();
		TwosideKeeper.ARTIFACT_RECIPE_T1_AXE_RECIPE = ArtifactItemType.AXE.defineBaseRecipe();
		TwosideKeeper.ARTIFACT_RECIPE_T1_BOOTS_RECIPE = ArtifactItemType.BOOTS.defineBaseRecipe();
		TwosideKeeper.ARTIFACT_RECIPE_T1_BOW_RECIPE = ArtifactItemType.BOW.defineBaseRecipe();
		TwosideKeeper.ARTIFACT_RECIPE_T1_CHESTPLATE_RECIPE = ArtifactItemType.CHESTPLATE.defineBaseRecipe();
		TwosideKeeper.ARTIFACT_RECIPE_T1_FISHING_ROD_RECIPE = ArtifactItemType.FISHING_ROD.defineBaseRecipe();
		TwosideKeeper.ARTIFACT_RECIPE_T1_HELMET_RECIPE = ArtifactItemType.HELMET.defineBaseRecipe();
		TwosideKeeper.ARTIFACT_RECIPE_T1_SCYTHE_RECIPE = ArtifactItemType.HOE.defineBaseRecipe();
		TwosideKeeper.ARTIFACT_RECIPE_T1_LEGGINGS_RECIPE = ArtifactItemType.LEGGINGS.defineBaseRecipe();
		TwosideKeeper.ARTIFACT_RECIPE_T1_PICKAXE_RECIPE = ArtifactItemType.PICKAXE.defineBaseRecipe();
		TwosideKeeper.ARTIFACT_RECIPE_T1_SHOVEL_RECIPE = ArtifactItemType.SHOVEL.defineBaseRecipe();
		TwosideKeeper.ARTIFACT_RECIPE_T2 = UpgradeRecipe(2);
		TwosideKeeper.ARTIFACT_RECIPE_T3 = UpgradeRecipe(3);
		TwosideKeeper.ARTIFACT_RECIPE_T4 = UpgradeRecipe(4);
		TwosideKeeper.ARTIFACT_RECIPE_T5 = UpgradeRecipe(5);
		TwosideKeeper.ARTIFACT_RECIPE_T6 = UpgradeRecipe(6);
		TwosideKeeper.ARTIFACT_RECIPE_T7 = UpgradeRecipe(7);
		TwosideKeeper.ARTIFACT_RECIPE_T8 = UpgradeRecipe(8);
		TwosideKeeper.ARTIFACT_RECIPE_T9 = UpgradeRecipe(9);
		TwosideKeeper.ARTIFACT_RECIPE_T10 = UpgradeRecipe(10);
		TwosideKeeper.ARTIFACT_RECIPE_T11 = UpgradeRecipe(11);
		TwosideKeeper.ARTIFACT_RECIPE_T12 = UpgradeRecipe(12);
		TwosideKeeper.ARTIFACT_RECIPE_T13 = UpgradeRecipe(13);
		TwosideKeeper.ARTIFACT_RECIPE_T14 = UpgradeRecipe(14);
		TwosideKeeper.ARTIFACT_RECIPE_T15 = UpgradeRecipe(15);
		TwosideKeeper.ARTIFACT_RECIPE_T2_RECIPE = EssenceRecipe();
		TwosideKeeper.ARTIFACT_RECIPE_T3_RECIPE = CoreRecipe();
		TwosideKeeper.ARTIFACT_RECIPE_T4_RECIPE = BaseRecipe();
		TwosideKeeper.ARTIFACT_RECIPE_T5_RECIPE = EssenceRecipe();
		TwosideKeeper.ARTIFACT_RECIPE_T6_RECIPE = CoreRecipe();
		TwosideKeeper.ARTIFACT_RECIPE_T7_RECIPE = BaseRecipe();
		TwosideKeeper.ARTIFACT_RECIPE_T8_RECIPE = EssenceRecipe();
		TwosideKeeper.ARTIFACT_RECIPE_T9_RECIPE = CoreRecipe();
		TwosideKeeper.ARTIFACT_RECIPE_T10_RECIPE = BaseRecipe();
		TwosideKeeper.ARTIFACT_RECIPE_T11_RECIPE = EssenceRecipe();
		TwosideKeeper.ARTIFACT_RECIPE_T12_RECIPE = CoreRecipe();
		TwosideKeeper.ARTIFACT_RECIPE_T13_RECIPE = BaseRecipe();
		TwosideKeeper.ARTIFACT_RECIPE_T14_RECIPE = FinalRecipe();
		TwosideKeeper.INCREASE_ARTIFACT_CRAFTING_TIER_RECIPE = IncreaseTierRecipe();
		TwosideKeeper.DECREASE_ARTIFACT_CRAFTING_TIER_RECIPE = DecreaseTierRecipe();
		TwosideKeeper.EMPOWER_ARTIFACT_CRAFTING_ITEM_RECIPE = EmpowerTierRecipe();
		TwosideKeeper.MONEY_CHECK_RECIPE = CheckRecipe();
		TwosideKeeper.HANDMADE_ARROW_RECIPE = HandmadeArrowRecipe();
		TwosideKeeper.DIAMONDTIPPED_ARROW_RECIPE = DiamondTippedArrowRecipe();
		TwosideKeeper.TRAPPING_ARROW_RECIPE = TrappingArrowRecipe();
		TwosideKeeper.EXPLODING_ARROW_RECIPE = ExplodingArrowRecipe();
		TwosideKeeper.POISON_ARROW_RECIPE = PoisonArrowRecipe();
		TwosideKeeper.PIERCING_ARROW_RECIPE = PiercingArrowRecipe();
		TwosideKeeper.WORLD_SHOP_RECIPE = WorldShopRecipe();
		TwosideKeeper.WORLD_SHOP2_RECIPE = WorldShop2Recipe();
		TwosideKeeper.BAUBLE_POUCH_RECIPE = BaublePouchRecipe();
	}
	
	private static ShapelessRecipe BaublePouchRecipe() {
		ShapelessRecipe rec = new ShapelessRecipe(BaublePouch());
		rec.addIngredient(4, Material.LEATHER);
		rec.addIngredient(1, Material.CHORUS_FLOWER);
		rec.addIngredient(4, Material.LEATHER);
		return rec;
	}

	public static ItemStack BaublePouch() {
		ItemStack baublePouch = new ItemStack(Material.CHORUS_FLOWER);
		ItemUtils.addLore(baublePouch, ChatColor.AQUA+"A handy 9-slot pouch that");
		ItemUtils.addLore(baublePouch, ChatColor.AQUA+"can hold Baubles of any");
		ItemUtils.addLore(baublePouch, ChatColor.AQUA+"sort.");
		ItemUtils.addLore(baublePouch, ChatColor.AQUA+"");
		ItemUtils.addLore(baublePouch, BaublePouch.POUCHID_LINE+"0");
		ItemUtils.setDisplayName(baublePouch, ChatColor.GREEN+"Bauble Pouch");
		baublePouch.addUnsafeEnchantment(Enchantment.LUCK, 1);
		ItemUtils.hideEnchantments(baublePouch);
		return baublePouch.clone();
	}

	public static ItemStack VacuumCube() {
		ItemStack item_VacuumCube = new ItemStack(Material.ENDER_CHEST);
		List<String> item_VacuumCube_lore = new ArrayList<String>();
		item_VacuumCube_lore.add("A storage container that sucks");
		item_VacuumCube_lore.add("up blocks. Holds 54 block stacks.");
		item_VacuumCube_lore.add(" ");
		ItemMeta item_ItemCube_meta=item_VacuumCube.getItemMeta();
		item_ItemCube_meta.setLore(item_VacuumCube_lore);
		item_ItemCube_meta.setDisplayName(ChatColor.BLUE+""+ChatColor.BOLD+"Vacuum Cube");
		item_VacuumCube.setItemMeta(item_ItemCube_meta);
		return item_VacuumCube.clone();
	}
	
	private static ShapelessRecipe VacuumCubeRecipe() {
		ItemStack item_VacuumCube = VacuumCube();
		
		ShapelessRecipe ItemCube = new ShapelessRecipe(item_VacuumCube);
		ItemCube.addIngredient(Material.ENDER_CHEST);
		ItemCube.addIngredient(Material.ENDER_PEARL);
		ItemCube.addIngredient(Material.ENDER_PEARL);
		ItemCube.addIngredient(Material.ELYTRA);
		ItemCube.addIngredient(4,Material.DIAMOND_BLOCK);
		ItemCube.addIngredient(Material.REDSTONE_BLOCK);
		return ItemCube;
	}
	
	private static ShapelessRecipe FilterCubeRecipe() {
		ItemStack item_FilterCube = FilterCube();
		
		ShapelessRecipe FilterCube = new ShapelessRecipe(item_FilterCube);
		FilterCube.addIngredient(Material.CHEST);
		FilterCube.addIngredient(Material.HOPPER);
		FilterCube.addIngredient(4,Material.DIAMOND_BLOCK);
		FilterCube.addIngredient(3,Material.IRON_BLOCK);
		return FilterCube;
	}

	public static ItemStack FilterCube() {
		ItemStack item_FilterCube = new ItemStack(Material.HOPPER_MINECART);
		List<String> item_FilterCube_lore = new ArrayList<String>();
		item_FilterCube_lore.add("A storage container that holds up");
		item_FilterCube_lore.add("to 27 items. Shift-Right click to");
		item_FilterCube_lore.add("open up a filtered item list.");
		ItemMeta item_FilterCube_meta=item_FilterCube.getItemMeta();
		item_FilterCube_meta.setLore(item_FilterCube_lore);
		item_FilterCube_meta.setDisplayName(ChatColor.GREEN+""+ChatColor.BOLD+"Filter Cube");
		item_FilterCube.setItemMeta(item_FilterCube_meta);
		return item_FilterCube.clone();
	}

	private static ShapelessRecipe WorldShopRecipe() {
		ItemStack item_worldShop = WorldShop();
		
		ShapelessRecipe recipe_Worldshop = new ShapelessRecipe(item_worldShop);
		recipe_Worldshop.addIngredient(Material.CHEST);
		recipe_Worldshop.addIngredient(Material.SIGN);
		recipe_Worldshop.addIngredient(Material.DIRT);
		return recipe_Worldshop;
	}
	
	private static ShapelessRecipe WorldShop2Recipe() {
		ItemStack item_worldShop = WorldShop2();
		
		ShapelessRecipe recipe_Worldshop = new ShapelessRecipe(item_worldShop);
		recipe_Worldshop.addIngredient(Material.TRAPPED_CHEST);
		recipe_Worldshop.addIngredient(Material.SIGN);
		recipe_Worldshop.addIngredient(Material.DIRT);
		return recipe_Worldshop;
	}

	public static ItemStack DirtSubstitute() {
		ItemStack dirtSub = new ItemStack(Material.DIRT);
		ItemUtils.addLore(dirtSub, "You can substitute the dirt block");
		ItemUtils.addLore(dirtSub, "with any block to create any type");
		ItemUtils.addLore(dirtSub, "of World Shop!");
		return dirtSub;
	}
	
	public static ItemStack WorldShop() {
		ItemStack worldShop = new ItemStack(Material.CHEST);
		List<String> worldShopLore = new ArrayList<String>();
		worldShopLore.add("You can substitute the dirt block");
		worldShopLore.add("with any block to create any type");
		worldShopLore.add("of World Shop!");
		ItemMeta item_ItemCube_meta=worldShop.getItemMeta();
		item_ItemCube_meta.setLore(worldShopLore);
		item_ItemCube_meta.setDisplayName("Placeable World Shop");
		worldShop.setItemMeta(item_ItemCube_meta);
		return worldShop.clone();
	}

	public static ItemStack WorldShop2() {
		ItemStack worldShop = new ItemStack(Material.TRAPPED_CHEST);
		List<String> worldShopLore = new ArrayList<String>();
		worldShopLore.add("You can substitute the dirt block");
		worldShopLore.add("with any block to create any type");
		worldShopLore.add("of World Shop!");
		ItemMeta item_ItemCube_meta=worldShop.getItemMeta();
		item_ItemCube_meta.setLore(worldShopLore);
		item_ItemCube_meta.setDisplayName("Placeable World Shop");
		worldShop.setItemMeta(item_ItemCube_meta);
		return worldShop.clone();
	}

	private static ShapelessRecipe PiercingArrowRecipe() {
		ItemStack piercingarrow = Recipes.getArrowFromMeta("PIERCING_ARR");
		ShapelessRecipe piercingarrow_recipe = new ShapelessRecipe(piercingarrow);
		piercingarrow_recipe.addIngredient(Material.FLINT);
		piercingarrow_recipe.addIngredient(4,Material.REDSTONE);
		piercingarrow_recipe.addIngredient(Material.STICK);
		piercingarrow_recipe.addIngredient(Material.FEATHER);
		return piercingarrow_recipe;
	}
	
	private static ShapelessRecipe Piercing2ArrowRecipe() {
		ItemStack piercingarrow = Recipes.getArrowFromMeta("PIERCING_ARR");
		ShapelessRecipe piercingarrow_recipe = new ShapelessRecipe(piercingarrow);
		piercingarrow_recipe.addIngredient(Material.TIPPED_ARROW);
		piercingarrow_recipe.addIngredient(4,Material.REDSTONE);
		return piercingarrow_recipe;
	}

	@SuppressWarnings("deprecation")
	private static ShapelessRecipe PoisonArrowRecipe() {
		ItemStack poisonarrow = Recipes.getArrowFromMeta("POISON_ARR");
		ShapelessRecipe poisonarrow_recipe = new ShapelessRecipe(poisonarrow);
		poisonarrow_recipe.addIngredient(Material.RAW_FISH,3);
		poisonarrow_recipe.addIngredient(Material.STICK);
		poisonarrow_recipe.addIngredient(Material.FEATHER);
		return poisonarrow_recipe;
	}

	private static ShapelessRecipe ExplodingArrowRecipe() {
		ItemStack explosionarrow = Recipes.getArrowFromMeta("EXPLODE_ARR");
		ShapelessRecipe explosionarrow_recipe = new ShapelessRecipe(explosionarrow);
		explosionarrow_recipe.addIngredient(Material.SULPHUR);
		explosionarrow_recipe.addIngredient(Material.STICK);
		explosionarrow_recipe.addIngredient(Material.FEATHER);
		return explosionarrow_recipe;
	}

	private static ShapelessRecipe TrappingArrowRecipe() {
		ItemStack trappingarrow = Recipes.getArrowFromMeta("TRAP_ARR");
		ShapelessRecipe trappingarrow_recipe = new ShapelessRecipe(trappingarrow);
		trappingarrow_recipe.addIngredient(Material.WEB);
		trappingarrow_recipe.addIngredient(Material.STICK);
		trappingarrow_recipe.addIngredient(Material.FEATHER);
		return trappingarrow_recipe;
	}

	private static ShapelessRecipe DiamondTippedArrowRecipe() {
		ItemStack diamondtippedarrow = Recipes.getArrowFromMeta("QUADRUPLE_DAMAGE_ARR");
		ShapelessRecipe diamondtippedarrow_recipe = new ShapelessRecipe(diamondtippedarrow);
		diamondtippedarrow_recipe.addIngredient(Material.TIPPED_ARROW);
		diamondtippedarrow_recipe.addIngredient(Material.DIAMOND);
		return diamondtippedarrow_recipe;
	}

	private static ShapelessRecipe HandmadeArrowRecipe() {
		ItemStack handmadearrow = Recipes.getArrowFromMeta("DOUBLE_DAMAGE_ARR");
		ShapelessRecipe handmadearrow_recipe = new ShapelessRecipe(handmadearrow);
		handmadearrow_recipe.addIngredient(Material.FLINT);
		handmadearrow_recipe.addIngredient(Material.STICK);
		handmadearrow_recipe.addIngredient(Material.FEATHER);
		return handmadearrow_recipe;
	}
	
	//Returns null if none found. This means you should probably just leave the arrow as it is.
	public static ItemStack convertArrowEntityFromMeta(Arrow arrow) {
		if (arrow.hasMetadata("DOUBLE_DAMAGE_ARR")) {
			return Recipes.getArrowFromMeta("DOUBLE_DAMAGE_ARR");
		}
		if (arrow.hasMetadata("QUADRUPLE_DAMAGE_ARR")) {
			return Recipes.getArrowFromMeta("QUADRUPLE_DAMAGE_ARR");
		}
		if (arrow.hasMetadata("TRAP_ARR")) {
			return Recipes.getArrowFromMeta("TRAP_ARR");
		}
		if (arrow.hasMetadata("EXPLODE_ARR")) {
			return Recipes.getArrowFromMeta("EXPLODE_ARR");
		}
		if (arrow.hasMetadata("POISON_ARR")) {
			return Recipes.getArrowFromMeta("POISON_ARR");
		}
		if (arrow.hasMetadata("PIERCING_ARR")) {
			return Recipes.getArrowFromMeta("PIERCING_ARR");
		}
		if (arrow.hasMetadata("SPECTRAL_ARROW")) {
			return new ItemStack(Material.SPECTRAL_ARROW);
		}
		if (arrow.hasMetadata("TIPPED_ARROW") || arrow.hasMetadata("BASE_ARROW") || arrow instanceof TippedArrow) {
			if (arrow instanceof TippedArrow) {
				ItemStack item = new ItemStack(Material.TIPPED_ARROW);
				PotionMeta pm = (PotionMeta)item.getItemMeta();
				for (PotionEffect eff : ((TippedArrow)arrow).getCustomEffects()) {
					pm.addCustomEffect(eff, true);
				}
				pm.setBasePotionData(((TippedArrow)arrow).getBasePotionData());
				item.setItemMeta(pm);
				TwosideKeeper.log("This item is "+item.toString(), 5);
				return item;
			} else {
				ItemStack item = new ItemStack(Material.TIPPED_ARROW);
				PotionMeta pm = (PotionMeta)item.getItemMeta();
				if (arrow.hasMetadata("TIPPED_ARROW")) {
					String effects = arrow.getMetadata("TIPPED_ARROW").get(0).asString();
					for (String vals : effects.split(";")) {
						String[] pieces = vals.split(","); 
						pm.addCustomEffect(new PotionEffect(PotionEffectType.getByName(pieces[0]),Integer.parseInt(pieces[1]),Integer.parseInt(pieces[2])),true);
					}
				}
				if (arrow.hasMetadata("BASE_ARROW")) {
					String[] pieces = arrow.getMetadata("BASE_ARROW").get(0).asString().split(",");
					TwosideKeeper.log("Found Base Arrow. Pieces: "+pieces.toString(), 5);
					if (PotionType.valueOf(pieces[0])!=PotionType.WATER) {
						PotionData pd = new PotionData(PotionType.valueOf(pieces[0]),Boolean.parseBoolean(pieces[1]),Boolean.parseBoolean(pieces[2]));
						TwosideKeeper.log("Applying"+pd.toString(), 5);
						pm.setBasePotionData(pd);
					}
				}
				item.setItemMeta(pm);
				TwosideKeeper.log("This item is "+item.toString(), 5);
				return item;
			}
		}
		return null;
	}

	private static ShapelessRecipe CheckRecipe() {
		ItemStack check = MoneyCheck();
		ShapelessRecipe checkrecipe = new ShapelessRecipe(check);
		checkrecipe.addIngredient(Material.INK_SACK);
		checkrecipe.addIngredient(Material.PAPER);
		checkrecipe.addIngredient(Material.FEATHER);
		return checkrecipe;
	}

	public static ItemStack MoneyCheck() {
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
		return check;
	}

	private static ShapelessRecipe EmpowerTierRecipe() {
		ShapelessRecipe upgraderecipe = new ShapelessRecipe(Artifact.createArtifactItem(ArtifactItem.DIVINE_BASE));
		upgraderecipe.addIngredient(Material.NETHER_STAR);
		upgraderecipe.addIngredient(Material.CLAY_BALL);
		return upgraderecipe;
	}

	private static ShapelessRecipe DecreaseTierRecipe() {
		ItemStack newitem = Artifact.createArtifactItem(ArtifactItem.ARTIFACT_ESSENCE);
		newitem.setAmount(2);
		ShapelessRecipe upgraderecipe = new ShapelessRecipe(newitem);
		upgraderecipe.addIngredient(Material.SUGAR);
		return upgraderecipe;
	}

	private static ShapelessRecipe IncreaseTierRecipe() {
		ItemStack newitem = Artifact.createArtifactItem(ArtifactItem.ARTIFACT_ESSENCE);
		ShapelessRecipe upgraderecipe = new ShapelessRecipe(newitem);
		upgraderecipe.addIngredient(2,Material.SUGAR);
		return upgraderecipe;
	}

	@SuppressWarnings("deprecation")
	private static ShapelessRecipe FinalRecipe() {
		ShapelessRecipe newrecipe = new ShapelessRecipe(Artifact.convert(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)ArtifactItemType.PICKAXE.getDataValue())));
		newrecipe.addIngredient(2, Material.STAINED_GLASS_PANE, ArtifactItemType.PICKAXE.getDataValue());
		newrecipe.addIngredient(Material.SUGAR);
		newrecipe.addIngredient(Material.MAGMA_CREAM);
		newrecipe.addIngredient(Material.CLAY_BALL);
		return newrecipe;
	}

	@SuppressWarnings("deprecation")
	private static ShapelessRecipe BaseRecipe() {
		ShapelessRecipe newrecipe = new ShapelessRecipe(Artifact.convert(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)ArtifactItemType.CHESTPLATE.getDataValue())));
		newrecipe.addIngredient(2, Material.STAINED_GLASS_PANE, ArtifactItemType.CHESTPLATE.getDataValue());
		newrecipe.addIngredient(Material.CLAY_BALL);
		return newrecipe;
	}

	@SuppressWarnings("deprecation")
	private static ShapelessRecipe CoreRecipe() {
		ShapelessRecipe newrecipe = new ShapelessRecipe(Artifact.convert(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)ArtifactItemType.SHOVEL.getDataValue())));
		newrecipe.addIngredient(2, Material.STAINED_GLASS_PANE, ArtifactItemType.SHOVEL.getDataValue());
		newrecipe.addIngredient(Material.MAGMA_CREAM);
		return newrecipe;
	}

	@SuppressWarnings("deprecation")
	private static ShapelessRecipe EssenceRecipe() {
		ShapelessRecipe newrecipe = new ShapelessRecipe(Artifact.convert(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)ArtifactItemType.SWORD.getDataValue())));
		newrecipe.addIngredient(2, Material.STAINED_GLASS_PANE, ArtifactItemType.SWORD.getDataValue());
		newrecipe.addIngredient(Material.SUGAR);
		return newrecipe;
	}

	@SuppressWarnings("deprecation")
	private static ShapelessRecipe UpgradeRecipe(int i) {
		ShapelessRecipe upgrade_recipe = new ShapelessRecipe(ArtifactItemType.SWORD.getTieredItem(i));
		upgrade_recipe.addIngredient(Material.STAINED_GLASS_PANE, ArtifactItemType.SWORD.getDataValue());
		upgrade_recipe.addIngredient(ArtifactItemType.SWORD.getTieredItem(i).getType(),-1);
		return upgrade_recipe;
	}

	@SuppressWarnings("deprecation")
	private static ShapelessRecipe SlabReconstructionRecipe() {
		ShapelessRecipe plank_construction_recipe = new ShapelessRecipe(new ItemStack(Material.WOOD,1,(byte)2));
		plank_construction_recipe.addIngredient(2, Material.getMaterial(126), (short)2);
		return plank_construction_recipe;
	}

	@SuppressWarnings("deprecation")
	private static ShapedRecipe WoolRecolorRecipe() {
		ShapedRecipe wool_recolor_recipe = new ShapedRecipe(new ItemStack(Material.WOOL,8,(short)4));
		wool_recolor_recipe.shape("wxy","zda","bce");
		wool_recolor_recipe.setIngredient('w', Material.WOOL, (short)2);
		wool_recolor_recipe.setIngredient('x', Material.WOOL, (short)4);
		wool_recolor_recipe.setIngredient('y', Material.WOOL, (short)6);
		wool_recolor_recipe.setIngredient('z', Material.WOOL, (short)8);
		wool_recolor_recipe.setIngredient('a', Material.WOOL, (short)10);
		wool_recolor_recipe.setIngredient('b', Material.WOOL, (short)12);
		wool_recolor_recipe.setIngredient('c', Material.WOOL, (short)14);
		wool_recolor_recipe.setIngredient('e', Material.WOOL, (short)11);
		wool_recolor_recipe.setIngredient('d', Material.getMaterial(351), (short)4);
		return wool_recolor_recipe;
	}

	private static ShapedRecipe HardenedRecipe(Material name, Material resource, String line1, String line2, String line3) {
		ItemStack blockarmorpc = HardenedPiece(name);
		
		ShapedRecipe BlockArmor = new ShapedRecipe(blockarmorpc);
		BlockArmor.shape(line1,line2,line3);
		BlockArmor.setIngredient('a', resource);
		return BlockArmor;
	}

	public static ItemStack HardenedPiece(Material item) {
		ItemStack blockarmorpc = new ItemStack(item);
		return GenericFunctions.addHardenedItemBreaks(blockarmorpc, 5, true);
	}

	private static ShapedRecipe ArrowQuiverRecipe() {
		ShapedRecipe ArrowQuiver = new ShapedRecipe(ArrowQuiver());
		ArrowQuiver.shape("xle","lsl","xlx");
		ArrowQuiver.setIngredient('s', Material.SPECTRAL_ARROW);
		ArrowQuiver.setIngredient('l', Material.LEATHER);
		ArrowQuiver.setIngredient('e', Material.EMERALD_BLOCK);
		
		return ArrowQuiver;
	}

	public static ItemStack ArrowQuiver() {
		ItemStack arrow_quiver = new ItemStack(Material.TIPPED_ARROW);
	
		List<String> arrow_quiver_lore = new ArrayList<String>();
		arrow_quiver_lore.add(ArrowQuiver.ARROW_QUIVER_IDENTIFIER);
		arrow_quiver_lore.add(ArrowQuiver.ID_PREFIX+"1");
		ItemMeta arrow_quiver_meta=arrow_quiver.getItemMeta();
		arrow_quiver_meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		arrow_quiver_meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		arrow_quiver_meta.setLore(arrow_quiver_lore);
		arrow_quiver_meta.setDisplayName(ChatColor.BLUE+"Arrow Quiver");
		arrow_quiver.setItemMeta(arrow_quiver_meta);
		
		arrow_quiver.setAmount(1);
		arrow_quiver.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 5);
		return arrow_quiver;
	}
	
	private static ShapelessRecipe DuplicateEnderItemCubeRecipe() {
		ShapelessRecipe ItemCube1 = new ShapelessRecipe(CustomRecipe.ENDER_ITEM_CUBE_DUPLICATE.setCustomRecipeItem(new ItemStack(Material.ENDER_CHEST,2)));
		ItemCube1.addIngredient(Material.ENDER_CHEST);
		ItemCube1.addIngredient(Material.NETHER_STAR);
		return ItemCube1;
	}

	private static ShapedRecipe EnderItemCubeRecipe() {
		ShapedRecipe ItemCube = new ShapedRecipe(EnderItemCube());
		ItemCube.shape("ooo","ece","ooo");
		ItemCube.setIngredient('o', Material.OBSIDIAN);
		ItemCube.setIngredient('e', Material.EMERALD);
		ItemCube.setIngredient('c', Material.ENDER_CHEST);
		return ItemCube;
	}

	public static ItemStack EnderItemCube() {
		return EnderItemCube(1);
	}
	
	public static ItemStack EnderItemCube(int amt) {
		ItemStack item_ItemCube = new ItemStack(Material.ENDER_CHEST);
		item_ItemCube.setAmount(amt);
		ItemMeta item_ItemCube_meta=item_ItemCube.getItemMeta();
		List<String> item_ItemCube_lore = new ArrayList<String>();
		item_ItemCube_lore.add("A storage container that can");
		item_ItemCube_lore.add("be carried around. "+ChatColor.GOLD+"Open by");
		item_ItemCube_lore.add(ChatColor.GOLD+"right-clicking.");
		item_ItemCube_meta.setLore(item_ItemCube_lore);
		item_ItemCube_meta.setDisplayName("Ender Item Cube");
		item_ItemCube.setItemMeta(item_ItemCube_meta);
		return item_ItemCube.clone();
	}

	@SuppressWarnings("deprecation")
	private static ShapedRecipe LargeItemCubeRecipe() {
		ItemStack item_ItemCube = LargeItemCube();
		
		ShapedRecipe ItemCube = new ShapedRecipe(item_ItemCube);
		ItemCube.shape("ppp","gcg","ppp");
		ItemCube.setIngredient('p', Material.WOOD, -1);
		ItemCube.setIngredient('g', Material.GOLD_BLOCK);
		ItemCube.setIngredient('c', Material.CHEST);
		return ItemCube;
	}

	public static ItemStack LargeItemCube() {
		ItemStack item_ItemCube = new ItemStack(Material.STORAGE_MINECART);
		ItemMeta item_ItemCube_meta=item_ItemCube.getItemMeta();
		List<String> item_ItemCube_lore = new ArrayList<String>();
		item_ItemCube_lore.add("A storage container that can");
		item_ItemCube_lore.add("be carried around. "+ChatColor.GOLD+"Open by");
		item_ItemCube_lore.add(ChatColor.GOLD+"right-clicking.");
		item_ItemCube_meta.setLore(item_ItemCube_lore);
		item_ItemCube_meta.setDisplayName("Large Item Cube");
		item_ItemCube.setItemMeta(item_ItemCube_meta);
		return item_ItemCube.clone();
	}

	@SuppressWarnings("deprecation")
	private static ShapedRecipe ItemCubeRecipe() {
		ItemStack item_ItemCube = ItemCube();
		
		ShapedRecipe ItemCube = new ShapedRecipe(item_ItemCube);
		ItemCube.shape("ppp","pcp","ppp");
		ItemCube.setIngredient('p', Material.WOOD, -1);
		ItemCube.setIngredient('c', Material.CHEST);
		return ItemCube;
	}

	public static ItemStack ItemCube() {
		ItemStack item_ItemCube = new ItemStack(Material.CHEST);
				List<String> item_ItemCube_lore = new ArrayList<String>();
				item_ItemCube_lore.add("A storage container that can");
				item_ItemCube_lore.add("be carried around. "+ChatColor.GOLD+"Open by");
				item_ItemCube_lore.add(ChatColor.GOLD+"right-clicking.");
				ItemMeta item_ItemCube_meta=item_ItemCube.getItemMeta();
				item_ItemCube_meta.setLore(item_ItemCube_lore);
				item_ItemCube_meta.setDisplayName("Item Cube");
				item_ItemCube.setItemMeta(item_ItemCube_meta);
		return item_ItemCube.clone();
	}
	
	public static boolean isVacuumCube(ItemStack item) {
		if (ItemUtils.isValidLoreItem(item) && ItemUtils.LoreContains(item, "A storage container that sucks")) {
			return true;
		}
		return false;
	}
	
	public static boolean isFilterCube(ItemStack item) {
		if (ItemUtils.isValidLoreItem(item) && ItemUtils.LoreContains(item, "open up a filtered item list.")) {
			return true;
		}
		return false;
	}
	
}
