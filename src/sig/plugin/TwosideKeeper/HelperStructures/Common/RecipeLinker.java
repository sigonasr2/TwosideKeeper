package sig.plugin.TwosideKeeper.HelperStructures.Common;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import sig.plugin.TwosideKeeper.Artifact;
import sig.plugin.TwosideKeeper.AwakenedArtifact;
import sig.plugin.TwosideKeeper.Recipes;
import sig.plugin.TwosideKeeper.HelperStructures.ArtifactItem;
import sig.plugin.TwosideKeeper.HelperStructures.ArtifactItemType;
import sig.plugin.TwosideKeeper.HelperStructures.CustomItem;

public enum RecipeLinker {
	ic("Containers",ChatColor.YELLOW,"Item Cube",
			new ItemStack[]{
					CustomItem.ItemCube(),
					new ItemStack(Material.WOOD),new ItemStack(Material.WOOD),new ItemStack(Material.WOOD),
					new ItemStack(Material.WOOD),new ItemStack(Material.CHEST),new ItemStack(Material.WOOD),
					new ItemStack(Material.WOOD),new ItemStack(Material.WOOD),new ItemStack(Material.WOOD),
			}),
	lic("Containers",ChatColor.YELLOW,"Large Item Cube",new ItemStack[]{
					CustomItem.LargeItemCube(),
					new ItemStack(Material.WOOD),new ItemStack(Material.WOOD),new ItemStack(Material.WOOD),
					new ItemStack(Material.GOLD_BLOCK),new ItemStack(Material.CHEST),new ItemStack(Material.GOLD_BLOCK),
					new ItemStack(Material.WOOD),new ItemStack(Material.WOOD),new ItemStack(Material.WOOD),
			}),
	eic("Containers",ChatColor.YELLOW,"Ender Item Cube",new ItemStack[]{
					CustomItem.EnderItemCube(),
					new ItemStack(Material.OBSIDIAN),new ItemStack(Material.OBSIDIAN),new ItemStack(Material.OBSIDIAN),
					new ItemStack(Material.EMERALD),new ItemStack(Material.CHEST),new ItemStack(Material.EMERALD),
					new ItemStack(Material.OBSIDIAN),new ItemStack(Material.OBSIDIAN),new ItemStack(Material.OBSIDIAN),
			}),
	dc("Containers",ChatColor.YELLOW,"Duplicate Ender Item Cube",new ItemStack[]{
			CustomItem.EnderItemCube(2),
			CustomItem.EnderItemCube(),new ItemStack(Material.NETHER_STAR)
	}),
	aq("Misc Items",ChatColor.RED,"Arrow Quiver",new ItemStack[]{
			CustomItem.ArrowQuiver(),
			null,new ItemStack(Material.LEATHER),new ItemStack(Material.EMERALD_BLOCK),
			new ItemStack(Material.LEATHER),new ItemStack(Material.SPECTRAL_ARROW),new ItemStack(Material.LEATHER),
			null,new ItemStack(Material.LEATHER)
	}),
	ihelm("Hardened Armor",ChatColor.WHITE,"Hardened Iron Helmet",new ItemStack[]{
			CustomItem.HardenedPiece(Material.IRON_HELMET),
			new ItemStack(Material.IRON_BLOCK),new ItemStack(Material.IRON_BLOCK),new ItemStack(Material.IRON_BLOCK),
			new ItemStack(Material.IRON_BLOCK),null,new ItemStack(Material.IRON_BLOCK)
	}),
	ichest("Hardened Armor",ChatColor.WHITE,"Hardened Iron Chestplate",new ItemStack[]{
			CustomItem.HardenedPiece(Material.IRON_CHESTPLATE),
			new ItemStack(Material.IRON_BLOCK),null,new ItemStack(Material.IRON_BLOCK),
			new ItemStack(Material.IRON_BLOCK),new ItemStack(Material.IRON_BLOCK),new ItemStack(Material.IRON_BLOCK),
			new ItemStack(Material.IRON_BLOCK),new ItemStack(Material.IRON_BLOCK),new ItemStack(Material.IRON_BLOCK)
	}),
	ileg("Hardened Armor",ChatColor.WHITE,"Hardened Iron Leggings",new ItemStack[]{
			CustomItem.HardenedPiece(Material.IRON_LEGGINGS),
			new ItemStack(Material.IRON_BLOCK),new ItemStack(Material.IRON_BLOCK),new ItemStack(Material.IRON_BLOCK),
			new ItemStack(Material.IRON_BLOCK),null,new ItemStack(Material.IRON_BLOCK),
			new ItemStack(Material.IRON_BLOCK),null,new ItemStack(Material.IRON_BLOCK)
	}),
	iboots("Hardened Armor",ChatColor.WHITE,"Hardened Iron Boots",new ItemStack[]{
			CustomItem.HardenedPiece(Material.IRON_BOOTS),
			new ItemStack(Material.IRON_BLOCK),null,new ItemStack(Material.IRON_BLOCK),
			new ItemStack(Material.IRON_BLOCK),null,new ItemStack(Material.IRON_BLOCK)
	}),
	dhelm("Hardened Armor",ChatColor.AQUA,"Hardened Diamond Helmet",new ItemStack[]{
			CustomItem.HardenedPiece(Material.DIAMOND_HELMET),
			new ItemStack(Material.DIAMOND_BLOCK),new ItemStack(Material.DIAMOND_BLOCK),new ItemStack(Material.DIAMOND_BLOCK),
			new ItemStack(Material.DIAMOND_BLOCK),null,new ItemStack(Material.DIAMOND_BLOCK)
	}),
	dchest("Hardened Armor",ChatColor.AQUA,"Hardened Diamond Chestplate",new ItemStack[]{
			CustomItem.HardenedPiece(Material.DIAMOND_CHESTPLATE),
			new ItemStack(Material.DIAMOND_BLOCK),null,new ItemStack(Material.DIAMOND_BLOCK),
			new ItemStack(Material.DIAMOND_BLOCK),new ItemStack(Material.DIAMOND_BLOCK),new ItemStack(Material.DIAMOND_BLOCK),
			new ItemStack(Material.DIAMOND_BLOCK),new ItemStack(Material.DIAMOND_BLOCK),new ItemStack(Material.DIAMOND_BLOCK)
	}),
	dleg("Hardened Armor",ChatColor.AQUA,"Hardened Diamond Leggings",new ItemStack[]{
			CustomItem.HardenedPiece(Material.DIAMOND_LEGGINGS),
			new ItemStack(Material.DIAMOND_BLOCK),new ItemStack(Material.DIAMOND_BLOCK),new ItemStack(Material.DIAMOND_BLOCK),
			new ItemStack(Material.DIAMOND_BLOCK),null,new ItemStack(Material.DIAMOND_BLOCK),null,
			new ItemStack(Material.DIAMOND_BLOCK),null,new ItemStack(Material.DIAMOND_BLOCK)
	}),
	dboots("Hardened Armor",ChatColor.AQUA,"Hardened Diamond Boots",new ItemStack[]{
			CustomItem.HardenedPiece(Material.DIAMOND_BOOTS),
			new ItemStack(Material.DIAMOND_BLOCK),null,new ItemStack(Material.DIAMOND_BLOCK),
			new ItemStack(Material.DIAMOND_BLOCK),null,new ItemStack(Material.DIAMOND_BLOCK)
	}),
	ghelm("Hardened Armor",ChatColor.GOLD,"Hardened Gold Helmet",new ItemStack[]{
			CustomItem.HardenedPiece(Material.GOLD_HELMET),
			new ItemStack(Material.GOLD_BLOCK),new ItemStack(Material.GOLD_BLOCK),new ItemStack(Material.GOLD_BLOCK),
			new ItemStack(Material.GOLD_BLOCK),null,new ItemStack(Material.GOLD_BLOCK)
	}),
	gchest("Hardened Armor",ChatColor.GOLD,"Hardened Gold Chestplate",new ItemStack[]{
			CustomItem.HardenedPiece(Material.GOLD_CHESTPLATE),
			new ItemStack(Material.GOLD_BLOCK),null,new ItemStack(Material.GOLD_BLOCK),
			new ItemStack(Material.GOLD_BLOCK),new ItemStack(Material.GOLD_BLOCK),new ItemStack(Material.GOLD_BLOCK),
			new ItemStack(Material.GOLD_BLOCK),new ItemStack(Material.GOLD_BLOCK),new ItemStack(Material.GOLD_BLOCK)
	}),
	gleg("Hardened Armor",ChatColor.GOLD,"Hardened Gold Leggings",new ItemStack[]{
			CustomItem.HardenedPiece(Material.GOLD_LEGGINGS),
			new ItemStack(Material.GOLD_BLOCK),new ItemStack(Material.GOLD_BLOCK),new ItemStack(Material.GOLD_BLOCK),
			new ItemStack(Material.GOLD_BLOCK),null,new ItemStack(Material.GOLD_BLOCK),
			new ItemStack(Material.GOLD_BLOCK),null,new ItemStack(Material.GOLD_BLOCK)
	}),
	gboots("Hardened Armor",ChatColor.GOLD,"Hardened Gold Boots",new ItemStack[]{
			CustomItem.HardenedPiece(Material.GOLD_BOOTS),
			new ItemStack(Material.GOLD_BLOCK),null,new ItemStack(Material.GOLD_BLOCK),
			new ItemStack(Material.GOLD_BLOCK),null,new ItemStack(Material.GOLD_BLOCK)
	}),
	@SuppressWarnings("deprecation")
	wool("Misc Items",ChatColor.BLUE,"Wool Recoloring",new ItemStack[]{
			new ItemStack(Material.WOOL,8,(short)11),
			new ItemStack(Material.WOOL,1,(short)2),new ItemStack(Material.WOOL,1,(short)3),new ItemStack(Material.WOOL,1,(short)5),
			new ItemStack(Material.WOOL,1,(short)7),new ItemStack(Material.getMaterial(351),1,(short)4),new ItemStack(Material.WOOL,1,(short)8),
			new ItemStack(Material.WOOL,1,(short)13),new ItemStack(Material.WOOL,1,(short)11),new ItemStack(Material.WOOL,1,(short)9)
	}),
	slab("Misc Items",ChatColor.BLUE,"Slab Reconstruction",new ItemStack[]{
			new ItemStack(Material.WOOD,1,(short)2),
			new ItemStack(Material.WOOD_STEP,1,(short)2),new ItemStack(Material.WOOD_STEP,1,(short)2)
	}),
	check("Misc Items",ChatColor.BLUE,"Money Check",new ItemStack[]{
			CustomItem.MoneyCheck(),
			new ItemStack(Material.FEATHER),new ItemStack(Material.INK_SACK),null,
			new ItemStack(Material.PAPER),
	}),
	sword("Artifact Recipes",ChatColor.LIGHT_PURPLE,"Artifact Sword Recipe",new ItemStack[]{
			Artifact.createRecipe(0, ArtifactItemType.SWORD),
			Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE),Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE),Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE),
			Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE),Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE),Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE),
			Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE),Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE),Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE)
	}),
	axe("Artifact Recipes",ChatColor.LIGHT_PURPLE,"Artifact Axe Recipe",new ItemStack[]{
			Artifact.createRecipe(0, ArtifactItemType.AXE),
			Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE),Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE),null,
			Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE),Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE),null,
			null,Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE)
	}),
	boots("Artifact Recipes",ChatColor.LIGHT_PURPLE,"Artifact Boots Recipe",new ItemStack[]{
			Artifact.createRecipe(0, ArtifactItemType.BOOTS),
			Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE),null,Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE),
			Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE),null,Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE),
	}),
	chest("Artifact Recipes",ChatColor.LIGHT_PURPLE,"Artifact Chestplate Recipe",new ItemStack[]{
			Artifact.createRecipe(0, ArtifactItemType.CHESTPLATE),
			Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE),null,Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE),
			Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE),Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE),Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE),
			Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE),Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE),Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE)
	}),
	leg("Artifact Recipes",ChatColor.LIGHT_PURPLE,"Artifact Leggings Recipe",new ItemStack[]{
			Artifact.createRecipe(0, ArtifactItemType.LEGGINGS),
			Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE),Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE),Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE),
			Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE),null,Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE),
			Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE),null,Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE)
	}),
	helm("Artifact Recipes",ChatColor.LIGHT_PURPLE,"Artifact Helmet Recipe",new ItemStack[]{
			Artifact.createRecipe(0, ArtifactItemType.HELMET),
			Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE),Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE),Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE),
			Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE),null,Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE)
	}),
	bow("Artifact Recipes",ChatColor.LIGHT_PURPLE,"Artifact Bow Recipe",new ItemStack[]{
			Artifact.createRecipe(0, ArtifactItemType.BOW),
			Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE),Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE),null,
			Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE),null,Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE),
			Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE),Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE)
	}),
	scythe("Artifact Recipes",ChatColor.LIGHT_PURPLE,"Artifact Scythe Recipe",new ItemStack[]{
			Artifact.createRecipe(0, ArtifactItemType.HOE),
			Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE),Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE),null,
			null,Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE),null,
			null,Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE)
	}),
	rod("Artifact Recipes",ChatColor.LIGHT_PURPLE,"Artifact Fishing Rod Recipe",new ItemStack[]{
			Artifact.createRecipe(0, ArtifactItemType.FISHING_ROD),
			null,null,Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE),
			null,Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE),Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE),
			Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE),null,Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE)
	}),
	pickaxe("Artifact Recipes",ChatColor.LIGHT_PURPLE,"Artifact Pickaxe Recipe",new ItemStack[]{
			Artifact.createRecipe(0, ArtifactItemType.PICKAXE),
			Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE),Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE),Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE),
			null,Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE),null,
			null,Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE),null
	}),
	shovel("Artifact Recipes",ChatColor.LIGHT_PURPLE,"Artifact Shovel Recipe",new ItemStack[]{
			Artifact.createRecipe(0, ArtifactItemType.SHOVEL),
			null,Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE),null,
			null,Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE),null,
			null,Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE),null
	}),
	recipe("Artifact Recipes",ChatColor.DARK_AQUA,"Recipe To Artifact",new ItemStack[]{
			AwakenedArtifact.convertToAwakenedArtifact(ArtifactItemType.CHESTPLATE.getTieredItem(1),1,ArtifactItemType.CHESTPLATE.getDataValue()),
			Artifact.createRecipe(0, ArtifactItemType.CHESTPLATE)
	}),
	t2("Upgrade Recipes",ChatColor.DARK_AQUA,"Upgrade to T2",new ItemStack[]{
			AwakenedArtifact.convertToAwakenedArtifact(ArtifactItemType.SHOVEL.getTieredItem(2),2,ArtifactItemType.SHOVEL.getDataValue()),
			AwakenedArtifact.convertToAwakenedArtifact(ArtifactItemType.SHOVEL.getTieredItem(1),1,ArtifactItemType.SHOVEL.getDataValue()),Artifact.createRecipe(0, ArtifactItemType.SHOVEL)
	}),
	t3("Upgrade Recipes",ChatColor.DARK_AQUA,"Upgrade to T3",new ItemStack[]{
			AwakenedArtifact.convertToAwakenedArtifact(ArtifactItemType.LEGGINGS.getTieredItem(3),3,ArtifactItemType.LEGGINGS.getDataValue()),
			AwakenedArtifact.convertToAwakenedArtifact(ArtifactItemType.LEGGINGS.getTieredItem(2),2,ArtifactItemType.LEGGINGS.getDataValue()),Artifact.createRecipe(2, ArtifactItemType.LEGGINGS)
	}),
	t4("Upgrade Recipes",ChatColor.DARK_AQUA,"Upgrade to T4",new ItemStack[]{
			AwakenedArtifact.convertToAwakenedArtifact(ArtifactItemType.BOOTS.getTieredItem(4),4,ArtifactItemType.BOOTS.getDataValue()),
			AwakenedArtifact.convertToAwakenedArtifact(ArtifactItemType.BOOTS.getTieredItem(3),3,ArtifactItemType.BOOTS.getDataValue()),Artifact.createRecipe(3, ArtifactItemType.BOOTS)
	}),
	t5("Upgrade Recipes",ChatColor.DARK_AQUA,"Upgrade to T5",new ItemStack[]{
			AwakenedArtifact.convertToAwakenedArtifact(ArtifactItemType.HELMET.getTieredItem(5),5,ArtifactItemType.HELMET.getDataValue()),
			AwakenedArtifact.convertToAwakenedArtifact(ArtifactItemType.HELMET.getTieredItem(4),4,ArtifactItemType.HELMET.getDataValue()),Artifact.createRecipe(4, ArtifactItemType.HELMET)
	}),
	t6("Upgrade Recipes",ChatColor.DARK_AQUA,"Upgrade to T6",new ItemStack[]{
			AwakenedArtifact.convertToAwakenedArtifact(ArtifactItemType.SWORD.getTieredItem(6),6,ArtifactItemType.SWORD.getDataValue()),
			AwakenedArtifact.convertToAwakenedArtifact(ArtifactItemType.SWORD.getTieredItem(5),5,ArtifactItemType.SWORD.getDataValue()),Artifact.createRecipe(5, ArtifactItemType.SWORD)
	}),
	t7("Upgrade Recipes",ChatColor.DARK_AQUA,"Upgrade to T7",new ItemStack[]{
			AwakenedArtifact.convertToAwakenedArtifact(ArtifactItemType.HOE.getTieredItem(7),7,ArtifactItemType.HOE.getDataValue()),
			AwakenedArtifact.convertToAwakenedArtifact(ArtifactItemType.HOE.getTieredItem(6),6,ArtifactItemType.HOE.getDataValue()),Artifact.createRecipe(6, ArtifactItemType.HOE)
	}),
	t8("Upgrade Recipes",ChatColor.DARK_AQUA,"Upgrade to T8",new ItemStack[]{
			AwakenedArtifact.convertToAwakenedArtifact(ArtifactItemType.AXE.getTieredItem(8),8,ArtifactItemType.AXE.getDataValue()),
			AwakenedArtifact.convertToAwakenedArtifact(ArtifactItemType.AXE.getTieredItem(7),7,ArtifactItemType.AXE.getDataValue()),Artifact.createRecipe(7, ArtifactItemType.AXE)
	}),
	t9("Upgrade Recipes",ChatColor.DARK_AQUA,"Upgrade to T9",new ItemStack[]{
			AwakenedArtifact.convertToAwakenedArtifact(ArtifactItemType.PICKAXE.getTieredItem(9),9,ArtifactItemType.PICKAXE.getDataValue()),
			AwakenedArtifact.convertToAwakenedArtifact(ArtifactItemType.PICKAXE.getTieredItem(8),8,ArtifactItemType.PICKAXE.getDataValue()),Artifact.createRecipe(8, ArtifactItemType.PICKAXE)
	}),
	t10("Upgrade Recipes",ChatColor.DARK_AQUA,"Upgrade to T10",new ItemStack[]{
			AwakenedArtifact.convertToAwakenedArtifact(ArtifactItemType.SHOVEL.getTieredItem(10),10,ArtifactItemType.SHOVEL.getDataValue()),
			AwakenedArtifact.convertToAwakenedArtifact(ArtifactItemType.SHOVEL.getTieredItem(9),9,ArtifactItemType.SHOVEL.getDataValue()),Artifact.createRecipe(9, ArtifactItemType.SHOVEL)
	}),
	t11("Upgrade Recipes",ChatColor.DARK_AQUA,"Upgrade to T11",new ItemStack[]{
			AwakenedArtifact.convertToAwakenedArtifact(ArtifactItemType.PICKAXE.getTieredItem(11),11,ArtifactItemType.PICKAXE.getDataValue()),
			AwakenedArtifact.convertToAwakenedArtifact(ArtifactItemType.PICKAXE.getTieredItem(10),10,ArtifactItemType.PICKAXE.getDataValue()),Artifact.createRecipe(10, ArtifactItemType.PICKAXE)
	}),
	t12("Upgrade Recipes",ChatColor.DARK_AQUA,"Upgrade to T12",new ItemStack[]{
			AwakenedArtifact.convertToAwakenedArtifact(ArtifactItemType.AXE.getTieredItem(12),12,ArtifactItemType.AXE.getDataValue()),
			AwakenedArtifact.convertToAwakenedArtifact(ArtifactItemType.AXE.getTieredItem(11),11,ArtifactItemType.AXE.getDataValue()),Artifact.createRecipe(11, ArtifactItemType.AXE)
	}),
	t13("Upgrade Recipes",ChatColor.DARK_AQUA,"Upgrade to T13",new ItemStack[]{
			AwakenedArtifact.convertToAwakenedArtifact(ArtifactItemType.HOE.getTieredItem(13),13,ArtifactItemType.HOE.getDataValue()),
			AwakenedArtifact.convertToAwakenedArtifact(ArtifactItemType.HOE.getTieredItem(12),12,ArtifactItemType.HOE.getDataValue()),Artifact.createRecipe(12, ArtifactItemType.HOE)
	}),
	t14("Upgrade Recipes",ChatColor.DARK_AQUA,"Upgrade to T14",new ItemStack[]{
			AwakenedArtifact.convertToAwakenedArtifact(ArtifactItemType.FISHING_ROD.getTieredItem(14),14,ArtifactItemType.FISHING_ROD.getDataValue()),
			AwakenedArtifact.convertToAwakenedArtifact(ArtifactItemType.FISHING_ROD.getTieredItem(13),13,ArtifactItemType.FISHING_ROD.getDataValue()),Artifact.createRecipe(13, ArtifactItemType.FISHING_ROD)
	}),
	t15("Upgrade Recipes",ChatColor.DARK_AQUA,"Upgrade to T15",new ItemStack[]{
			AwakenedArtifact.convertToAwakenedArtifact(ArtifactItemType.BOW.getTieredItem(15),15,ArtifactItemType.BOW.getDataValue()),
			AwakenedArtifact.convertToAwakenedArtifact(ArtifactItemType.BOW.getTieredItem(14),14,ArtifactItemType.BOW.getDataValue()),Artifact.createRecipe(14, ArtifactItemType.BOW)
	}),
	t2recipe("Artifact Recipes",ChatColor.WHITE,"T2 Artifact Recipe",new ItemStack[]{
			Artifact.createRecipe(2, ArtifactItemType.SHOVEL),
			Artifact.createRecipe(0, ArtifactItemType.SHOVEL),Artifact.createRecipe(0, ArtifactItemType.SHOVEL),null,
			Artifact.createArtifactItem(ArtifactItem.ARTIFACT_ESSENCE)
	}),
	t3recipe("Artifact Recipes",ChatColor.WHITE,"T3 Artifact Recipe",new ItemStack[]{
			Artifact.createRecipe(3, ArtifactItemType.SHOVEL),
			Artifact.createRecipe(2, ArtifactItemType.SHOVEL),Artifact.createRecipe(2, ArtifactItemType.SHOVEL),null,
			Artifact.createArtifactItem(ArtifactItem.ARTIFACT_CORE)
	}),
	t4recipe("Artifact Recipes",ChatColor.WHITE,"T4 Artifact Recipe",new ItemStack[]{
			Artifact.createRecipe(4, ArtifactItemType.SHOVEL),
			Artifact.createRecipe(3, ArtifactItemType.SHOVEL),Artifact.createRecipe(3, ArtifactItemType.SHOVEL),null,
			Artifact.createArtifactItem(ArtifactItem.ARTIFACT_BASE)
	}),
	t5recipe("Artifact Recipes",ChatColor.WHITE,"T5 Artifact Recipe",new ItemStack[]{
			Artifact.createRecipe(5, ArtifactItemType.SHOVEL),
			Artifact.createRecipe(4, ArtifactItemType.SHOVEL),Artifact.createRecipe(4, ArtifactItemType.SHOVEL),null,
			Artifact.createArtifactItem(ArtifactItem.ANCIENT_ESSENCE)
	}),
	t6recipe("Artifact Recipes",ChatColor.WHITE,"T6 Artifact Recipe",new ItemStack[]{
			Artifact.createRecipe(6, ArtifactItemType.SHOVEL),
			Artifact.createRecipe(5, ArtifactItemType.SHOVEL),Artifact.createRecipe(5, ArtifactItemType.SHOVEL),null,
			Artifact.createArtifactItem(ArtifactItem.ANCIENT_CORE)
	}),
	t7recipe("Artifact Recipes",ChatColor.WHITE,"T7 Artifact Recipe",new ItemStack[]{
			Artifact.createRecipe(7, ArtifactItemType.SHOVEL),
			Artifact.createRecipe(6, ArtifactItemType.SHOVEL),Artifact.createRecipe(6, ArtifactItemType.SHOVEL),null,
			Artifact.createArtifactItem(ArtifactItem.ANCIENT_BASE)
	}),
	t8recipe("Artifact Recipes",ChatColor.WHITE,"T8 Artifact Recipe",new ItemStack[]{
			Artifact.createRecipe(8, ArtifactItemType.SHOVEL),
			Artifact.createRecipe(7, ArtifactItemType.SHOVEL),Artifact.createRecipe(7, ArtifactItemType.SHOVEL),null,
			Artifact.createArtifactItem(ArtifactItem.LOST_ESSENCE)
	}),
	t9recipe("Artifact Recipes",ChatColor.WHITE,"T9 Artifact Recipe",new ItemStack[]{
			Artifact.createRecipe(9, ArtifactItemType.SHOVEL),
			Artifact.createRecipe(8, ArtifactItemType.SHOVEL),Artifact.createRecipe(8, ArtifactItemType.SHOVEL),null,
			Artifact.createArtifactItem(ArtifactItem.LOST_CORE)
	}),
	t10recipe("Artifact Recipes",ChatColor.WHITE,"T10 Artifact Recipe",new ItemStack[]{
			Artifact.createRecipe(10, ArtifactItemType.SHOVEL),
			Artifact.createRecipe(9, ArtifactItemType.SHOVEL),Artifact.createRecipe(9, ArtifactItemType.SHOVEL),null,
			Artifact.createArtifactItem(ArtifactItem.LOST_BASE)
	}),
	t11recipe("Artifact Recipes",ChatColor.WHITE,"T11 Artifact Recipe",new ItemStack[]{
			Artifact.createRecipe(11, ArtifactItemType.SHOVEL),
			Artifact.createRecipe(10, ArtifactItemType.SHOVEL),Artifact.createRecipe(10, ArtifactItemType.SHOVEL),null,
			Artifact.createArtifactItem(ArtifactItem.DIVINE_ESSENCE)
	}),
	t12recipe("Artifact Recipes",ChatColor.WHITE,"T12 Artifact Recipe",new ItemStack[]{
			Artifact.createRecipe(12, ArtifactItemType.SHOVEL),
			Artifact.createRecipe(11, ArtifactItemType.SHOVEL),Artifact.createRecipe(11, ArtifactItemType.SHOVEL),null,
			Artifact.createArtifactItem(ArtifactItem.DIVINE_CORE)
	}),
	t13recipe("Artifact Recipes",ChatColor.WHITE,"T13 Artifact Recipe",new ItemStack[]{
			Artifact.createRecipe(13, ArtifactItemType.SHOVEL),
			Artifact.createRecipe(12, ArtifactItemType.SHOVEL),Artifact.createRecipe(12, ArtifactItemType.SHOVEL),null,
			Artifact.createArtifactItem(ArtifactItem.DIVINE_BASE)
	}),
	t14recipe("Artifact Recipes",ChatColor.WHITE,"T14 Artifact Recipe",new ItemStack[]{
			Artifact.createRecipe(14, ArtifactItemType.SHOVEL),
			Artifact.createRecipe(13, ArtifactItemType.SHOVEL),Artifact.createRecipe(13, ArtifactItemType.SHOVEL),null,
			Artifact.createArtifactItem(ArtifactItem.DIVINE_ESSENCE),
			Artifact.createArtifactItem(ArtifactItem.DIVINE_CORE),
			Artifact.createArtifactItem(ArtifactItem.DIVINE_BASE)
	}),
	tierup("Artifact Recipes",ChatColor.YELLOW,"Increase Artifact Material Tier",new ItemStack[]{
			Artifact.createArtifactItem(ArtifactItem.LOST_ESSENCE),
			Artifact.createArtifactItem(ArtifactItem.ANCIENT_ESSENCE),
			Artifact.createArtifactItem(ArtifactItem.ANCIENT_ESSENCE)
	}),
	tierdown("Artifact Recipes",ChatColor.YELLOW,"Decrease Artifact Material Tier",new ItemStack[]{
			Artifact.createArtifactItem(ArtifactItem.ANCIENT_ESSENCE,2),
			Artifact.createArtifactItem(ArtifactItem.LOST_ESSENCE)
	}),
	empower("Artifact Recipes",ChatColor.YELLOW,"Empower Artifact Materials to Divine Tier",new ItemStack[]{
			Artifact.createArtifactItem(ArtifactItem.DIVINE_CORE),
			Artifact.createArtifactItem(ArtifactItem.ARTIFACT_CORE),new ItemStack(Material.NETHER_STAR),
	}),
	arrow2("Custom Arrows",ChatColor.GREEN,"Hand-made Arrow",new ItemStack[]{
			Recipes.getArrowFromMeta("DOUBLE_DAMAGE_ARR"),
			new ItemStack(Material.FLINT),new ItemStack(Material.STICK),null,
			new ItemStack(Material.FEATHER),
	}),
	arrow4("Custom Arrows",ChatColor.GREEN,"Diamond-Tipped Arrow",new ItemStack[]{
			Recipes.getArrowFromMeta("QUADRUPLE_DAMAGE_ARR"),
			Recipes.getArrowFromMeta("DOUBLE_DAMAGE_ARR"),new ItemStack(Material.DIAMOND)
	}),
	traparrow("Custom Arrows",ChatColor.GREEN,"Trapping Arrow",new ItemStack[]{
			Recipes.getArrowFromMeta("TRAP_ARR"),
			new ItemStack(Material.WEB),new ItemStack(Material.STICK),null,
			new ItemStack(Material.FEATHER),
	}),
	explodearrow("Custom Arrows",ChatColor.GREEN,"Exploding Arrow",new ItemStack[]{
			Recipes.getArrowFromMeta("EXPLODE_ARR"),
			new ItemStack(Material.SULPHUR),new ItemStack(Material.STICK),null,
			new ItemStack(Material.FEATHER),
	}),
	poisonarrow("Custom Arrows",ChatColor.GREEN,"Poison Arrow",new ItemStack[]{
			Recipes.getArrowFromMeta("POISON_ARR"),
			new ItemStack(Material.RAW_FISH,1,(short)3),new ItemStack(Material.STICK),null,
			new ItemStack(Material.FEATHER),
	});
	
	String name = "";
	ItemStack[] rec = null;
	ChatColor col = null;
	String category = "";
	
	RecipeLinker(String cat, ChatColor col, String name, ItemStack[] recipe) {
		this.col=col;
		this.name=name;
		this.rec=recipe;
		this.category=cat;
	}

	public String getCategory() {
		return category;
	}

	public String getName() {
		return name;
	}

	public ItemStack[] getRec() {
		return rec;
	}
	
	public ChatColor getColor() {
		return col;
	}
}
