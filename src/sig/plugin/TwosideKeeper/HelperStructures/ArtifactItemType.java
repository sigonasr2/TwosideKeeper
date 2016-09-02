package sig.plugin.TwosideKeeper.HelperStructures;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import sig.plugin.TwosideKeeper.Artifact;
import sig.plugin.TwosideKeeper.TwosideKeeper;

public enum ArtifactItemType {
	AXE(0,"AXE",TierType.ALL,UpgradePath.AXE,new String[]{"EEx","EEx","xEx"},
			new ItemStack[]{
					new ItemStack(Material.STONE_AXE), //T1
					new ItemStack(Material.STONE_AXE), //T2
					new ItemStack(Material.STONE_AXE), //T3
					new ItemStack(Material.IRON_AXE), //T4
					new ItemStack(Material.IRON_AXE), //T5
					new ItemStack(Material.IRON_AXE), //T6
					new ItemStack(Material.DIAMOND_AXE), //T7
					new ItemStack(Material.DIAMOND_AXE), //T8
					new ItemStack(Material.DIAMOND_AXE), //T9
					new ItemStack(Material.DIAMOND_AXE), //T10
					new ItemStack(Material.DIAMOND_AXE), //T11
					new ItemStack(Material.DIAMOND_AXE), //T12
					new ItemStack(Material.DIAMOND_AXE), //T13
					new ItemStack(Material.DIAMOND_AXE), //T14
					new ItemStack(Material.DIAMOND_AXE), //T15
			},
			new double[]{
					1.0d, //T1
					1.25d, //T2
					1.50d, //T3
					1.75d, //T4
					2.0d, //T5
					2.25d, //T6
					2.50d, //T7
					2.75d, //T8
					3.0d, //T9
					3.25d, //T10
					3.5d, //T11
					4.5d, //T12
					5.5d, //T13
					6.5d, //T14
					8.0d, //T15
			}),
	SWORD(1,"SWORD",TierType.ALL,UpgradePath.SWORD,new String[]{"EEE","EEE","EEE"},
			new ItemStack[]{
					new ItemStack(Material.STONE_SWORD), //T1
					new ItemStack(Material.STONE_SWORD), //T2
					new ItemStack(Material.STONE_SWORD), //T3
					new ItemStack(Material.IRON_SWORD), //T4
					new ItemStack(Material.IRON_SWORD), //T5
					new ItemStack(Material.IRON_SWORD), //T6
					new ItemStack(Material.DIAMOND_SWORD), //T7
					new ItemStack(Material.DIAMOND_SWORD), //T8
					new ItemStack(Material.DIAMOND_SWORD), //T9
					new ItemStack(Material.DIAMOND_SWORD), //T10
					new ItemStack(Material.DIAMOND_SWORD), //T11
					new ItemStack(Material.DIAMOND_SWORD), //T12
					new ItemStack(Material.DIAMOND_SWORD), //T13
					new ItemStack(Material.DIAMOND_SWORD), //T14
					new ItemStack(Material.DIAMOND_SWORD), //T15
			},
			new double[]{
					2.0d, //T1
					2.5d, //T2
					3.0d, //T3
					3.5d, //T4
					4.0d, //T5
					4.5d, //T6
					5.0d, //T7
					5.5d, //T8
					6.0d, //T9
					6.5d, //T10
					7.0d, //T11
					8.0d, //T12
					9.0d, //T13
					10.0d, //T14
					12.0d, //T15
			}),
	PICKAXE(2,"PICKAXE",TierType.ALL,UpgradePath.PICKAXE,new String[]{"EEE","xEx","xEx"},
			new ItemStack[]{
					new ItemStack(Material.STONE_PICKAXE), //T1
					new ItemStack(Material.STONE_PICKAXE), //T2
					new ItemStack(Material.STONE_PICKAXE), //T3
					new ItemStack(Material.IRON_PICKAXE), //T4
					new ItemStack(Material.IRON_PICKAXE), //T5
					new ItemStack(Material.IRON_PICKAXE), //T6
					new ItemStack(Material.DIAMOND_PICKAXE), //T7
					new ItemStack(Material.DIAMOND_PICKAXE), //T8
					new ItemStack(Material.DIAMOND_PICKAXE), //T9
					new ItemStack(Material.DIAMOND_PICKAXE), //T10
					new ItemStack(Material.DIAMOND_PICKAXE), //T11
					new ItemStack(Material.DIAMOND_PICKAXE), //T12
					new ItemStack(Material.DIAMOND_PICKAXE), //T13
					new ItemStack(Material.DIAMOND_PICKAXE), //T14
					new ItemStack(Material.DIAMOND_PICKAXE), //T15
			}),
	HOE(3,"SCYTHE",TierType.ALL,UpgradePath.SCYTHE,new String[]{"EEx","xEx","xEx"},
			new ItemStack[]{
					new ItemStack(Material.STONE_HOE), //T1
					new ItemStack(Material.STONE_HOE), //T2
					new ItemStack(Material.STONE_HOE), //T3
					new ItemStack(Material.IRON_HOE), //T4
					new ItemStack(Material.IRON_HOE), //T5
					new ItemStack(Material.IRON_HOE), //T6
					new ItemStack(Material.DIAMOND_HOE), //T7
					new ItemStack(Material.DIAMOND_HOE), //T8
					new ItemStack(Material.DIAMOND_HOE), //T9
					new ItemStack(Material.DIAMOND_HOE), //T10
					new ItemStack(Material.DIAMOND_HOE), //T11
					new ItemStack(Material.DIAMOND_HOE), //T12
					new ItemStack(Material.DIAMOND_HOE), //T13
					new ItemStack(Material.DIAMOND_HOE), //T14
					new ItemStack(Material.DIAMOND_HOE), //T15
			}),
	BOW(4,"BOW",TierType.ONE,UpgradePath.BOW,new String[]{"EEx","ExE","EEx"},
			new ItemStack[]{
					new ItemStack(Material.BOW), //T1
					new ItemStack(Material.BOW), //T2
					new ItemStack(Material.BOW), //T3
					new ItemStack(Material.BOW), //T4
					new ItemStack(Material.BOW), //T5
					new ItemStack(Material.BOW), //T6
					new ItemStack(Material.BOW), //T7
					new ItemStack(Material.BOW), //T8
					new ItemStack(Material.BOW), //T9
					new ItemStack(Material.BOW), //T10
					new ItemStack(Material.BOW), //T11
					new ItemStack(Material.BOW), //T12
					new ItemStack(Material.BOW), //T13
					new ItemStack(Material.BOW), //T14
					new ItemStack(Material.BOW), //T15
			}),
	SHOVEL(5,"SHOVEL",TierType.ALL,UpgradePath.SHOVEL,new String[]{"E","E","E"},
			new ItemStack[]{
					new ItemStack(Material.STONE_SPADE), //T1
					new ItemStack(Material.STONE_SPADE), //T2
					new ItemStack(Material.STONE_SPADE), //T3
					new ItemStack(Material.IRON_SPADE), //T4
					new ItemStack(Material.IRON_SPADE), //T5
					new ItemStack(Material.IRON_SPADE), //T6
					new ItemStack(Material.DIAMOND_SPADE), //T7
					new ItemStack(Material.DIAMOND_SPADE), //T8
					new ItemStack(Material.DIAMOND_SPADE), //T9
					new ItemStack(Material.DIAMOND_SPADE), //T10
					new ItemStack(Material.DIAMOND_SPADE), //T11
					new ItemStack(Material.DIAMOND_SPADE), //T12
					new ItemStack(Material.DIAMOND_SPADE), //T13
					new ItemStack(Material.DIAMOND_SPADE), //T14
					new ItemStack(Material.DIAMOND_SPADE), //T15
			}),
	HELMET(6,"HELMET",TierType.ARMOR,UpgradePath.ARMOR,new String[]{"EEE","ExE"},
			new ItemStack[]{
					new ItemStack(Material.LEATHER_HELMET), //T1
					new ItemStack(Material.LEATHER_HELMET), //T2
					new ItemStack(Material.LEATHER_HELMET), //T3
					new ItemStack(Material.LEATHER_HELMET), //T4
					new ItemStack(Material.LEATHER_HELMET), //T5
					new ItemStack(Material.LEATHER_HELMET), //T6
					new ItemStack(Material.LEATHER_HELMET), //T7
					new ItemStack(Material.LEATHER_HELMET), //T8
					new ItemStack(Material.LEATHER_HELMET), //T9
					new ItemStack(Material.LEATHER_HELMET), //T10
					new ItemStack(Material.LEATHER_HELMET), //T11
					new ItemStack(Material.LEATHER_HELMET), //T12
					new ItemStack(Material.LEATHER_HELMET), //T13
					new ItemStack(Material.LEATHER_HELMET), //T14
					new ItemStack(Material.LEATHER_HELMET), //T15
			},
			new double[]{
					3.75d, //T1
					5.0d, //T2
					6.25d, //T3
					7.50d, //T4
					8.75d, //T5
					11.25d, //T6
					13.75d, //T7
					16.25d, //T8
					18.75d, //T9
					22.50d //T10
			},
			new int[]{
					2, //T1
					3, //T2
					4, //T3
					5, //T4
					6, //T5
					7, //T6
					9, //T7
					10, //T8
					12, //T9
					14 //T10
			}),
	CHESTPLATE(7,"CHESTPLATE",TierType.ARMOR,UpgradePath.ARMOR,new String[]{"ExE","EEE","EEE"},
			new ItemStack[]{
					new ItemStack(Material.LEATHER_CHESTPLATE), //T1
					new ItemStack(Material.LEATHER_CHESTPLATE), //T2
					new ItemStack(Material.LEATHER_CHESTPLATE), //T3
					new ItemStack(Material.LEATHER_CHESTPLATE), //T4
					new ItemStack(Material.LEATHER_CHESTPLATE), //T5
					new ItemStack(Material.LEATHER_CHESTPLATE), //T6
					new ItemStack(Material.LEATHER_CHESTPLATE), //T7
					new ItemStack(Material.LEATHER_CHESTPLATE), //T8
					new ItemStack(Material.LEATHER_CHESTPLATE), //T9
					new ItemStack(Material.LEATHER_CHESTPLATE), //T10
					new ItemStack(Material.LEATHER_CHESTPLATE), //T11
					new ItemStack(Material.LEATHER_CHESTPLATE), //T12
					new ItemStack(Material.LEATHER_CHESTPLATE), //T13
					new ItemStack(Material.LEATHER_CHESTPLATE), //T14
					new ItemStack(Material.LEATHER_CHESTPLATE), //T15
			},
			new double[]{
					3.75d, //T1
					5.0d, //T2
					6.25d, //T3
					7.50d, //T4
					8.75d, //T5
					11.25d, //T6
					13.75d, //T7
					16.25d, //T8
					18.75d, //T9
					22.50d //T10
			},
			new int[]{
					2, //T1
					3, //T2
					4, //T3
					5, //T4
					6, //T5
					7, //T6
					9, //T7
					10, //T8
					12, //T9
					14 //T10
			}),
	LEGGINGS(8,"LEGGINGS",TierType.ARMOR,UpgradePath.ARMOR,new String[]{"EEE","ExE","ExE"},
			new ItemStack[]{
					new ItemStack(Material.LEATHER_LEGGINGS), //T1
					new ItemStack(Material.LEATHER_LEGGINGS), //T2
					new ItemStack(Material.LEATHER_LEGGINGS), //T3
					new ItemStack(Material.LEATHER_LEGGINGS), //T4
					new ItemStack(Material.LEATHER_LEGGINGS), //T5
					new ItemStack(Material.LEATHER_LEGGINGS), //T6
					new ItemStack(Material.LEATHER_LEGGINGS), //T7
					new ItemStack(Material.LEATHER_LEGGINGS), //T8
					new ItemStack(Material.LEATHER_LEGGINGS), //T9
					new ItemStack(Material.LEATHER_LEGGINGS), //T10
					new ItemStack(Material.LEATHER_LEGGINGS), //T11
					new ItemStack(Material.LEATHER_LEGGINGS), //T12
					new ItemStack(Material.LEATHER_LEGGINGS), //T13
					new ItemStack(Material.LEATHER_LEGGINGS), //T14
					new ItemStack(Material.LEATHER_LEGGINGS), //T15
			},
			new double[]{
					3.75d, //T1
					5.0d, //T2
					6.25d, //T3
					7.50d, //T4
					8.75d, //T5
					11.25d, //T6
					13.75d, //T7
					16.25d, //T8
					18.75d, //T9
					22.50d //T10
			},
			new int[]{
					2, //T1
					3, //T2
					4, //T3
					5, //T4
					6, //T5
					7, //T6
					9, //T7
					10, //T8
					12, //T9
					14 //T10
			}),
	BOOTS(9,"BOOTS",TierType.ARMOR,UpgradePath.ARMOR,new String[]{"ExE","ExE"},
			new ItemStack[]{
					new ItemStack(Material.LEATHER_BOOTS), //T1
					new ItemStack(Material.LEATHER_BOOTS), //T2
					new ItemStack(Material.LEATHER_BOOTS), //T3
					new ItemStack(Material.LEATHER_BOOTS), //T4
					new ItemStack(Material.LEATHER_BOOTS), //T5
					new ItemStack(Material.LEATHER_BOOTS), //T6
					new ItemStack(Material.LEATHER_BOOTS), //T7
					new ItemStack(Material.LEATHER_BOOTS), //T8
					new ItemStack(Material.LEATHER_BOOTS), //T9
					new ItemStack(Material.LEATHER_BOOTS), //T10
					new ItemStack(Material.LEATHER_BOOTS), //T11
					new ItemStack(Material.LEATHER_BOOTS), //T12
					new ItemStack(Material.LEATHER_BOOTS), //T13
					new ItemStack(Material.LEATHER_BOOTS), //T14
					new ItemStack(Material.LEATHER_BOOTS), //T15
			},
			new double[]{
					3.75d, //T1
					5.0d, //T2
					6.25d, //T3
					7.50d, //T4
					8.75d, //T5
					11.25d, //T6
					13.75d, //T7
					16.25d, //T8
					18.75d, //T9
					22.50d //T10
			},
			new int[]{
					2, //T1
					3, //T2
					4, //T3
					5, //T4
					6, //T5
					7, //T6
					9, //T7
					10, //T8
					12, //T9
					14 //T10
			}),
	FISHING_ROD(10,"FISHING ROD",TierType.ALL,UpgradePath.FISHING_ROD,new String[]{"xxE","xEE","ExE"},
			new ItemStack[]{
					new ItemStack(Material.FISHING_ROD), //T1
					new ItemStack(Material.FISHING_ROD), //T2
					new ItemStack(Material.FISHING_ROD), //T3
					new ItemStack(Material.FISHING_ROD), //T4
					new ItemStack(Material.FISHING_ROD), //T5
					new ItemStack(Material.FISHING_ROD), //T6
					new ItemStack(Material.FISHING_ROD), //T7
					new ItemStack(Material.FISHING_ROD), //T8
					new ItemStack(Material.FISHING_ROD), //T9
					new ItemStack(Material.FISHING_ROD), //T10
					new ItemStack(Material.FISHING_ROD), //T11
					new ItemStack(Material.FISHING_ROD), //T12
					new ItemStack(Material.FISHING_ROD), //T13
					new ItemStack(Material.FISHING_ROD), //T14
					new ItemStack(Material.FISHING_ROD), //T15
			},
			new double[]{
					3.75d, //T1
					5.0d, //T2
					6.25d, //T3
					7.50d, //T4
					8.75d, //T5
					11.25d, //T6
					13.75d, //T7
					16.25d, //T8
					18.75d, //T9
					22.50d //T10
			},
			new int[]{
					2, //T1
					3, //T2
					4, //T3
					5, //T4
					6, //T5
					7, //T6
					9, //T7
					10, //T8
					12, //T9
					14 //T10
			});
	
	int data;
	String itemname;
	TierType tier;
	UpgradePath upgrade;
	String[] recipe;
	ItemStack[] itemtiers;
	double[] damageamt;
	int[] healthamt;
	
	ArtifactItemType(int dataval, String itemname, TierType tier, UpgradePath upgrade, String[] recipe, ItemStack[] itemtiers, double[] damageamt) {
		this.data=dataval;
		this.itemname=itemname;
		this.tier=tier;
		this.upgrade=upgrade;
		this.recipe=recipe;
		this.itemtiers=itemtiers;
		this.damageamt = damageamt;
		this.healthamt = new int[]{};
	}
	
	ArtifactItemType(int dataval, String itemname, TierType tier, UpgradePath upgrade, String[] recipe, ItemStack[] itemtiers) {
		this.data=dataval;
		this.itemname=itemname;
		this.tier=tier;
		this.upgrade=upgrade;
		this.recipe=recipe;
		this.itemtiers=itemtiers;
		this.damageamt = new double[]{};
		this.healthamt = new int[]{};
	}
	
	ArtifactItemType(int dataval, String itemname, TierType tier, UpgradePath upgrade, String[] recipe, ItemStack[] itemtiers, double[] damageamt, int[] healthamt) {
		this.data=dataval;
		this.itemname=itemname;
		this.tier=tier;
		this.upgrade=upgrade;
		this.recipe=recipe;
		this.itemtiers=itemtiers;
		this.damageamt = damageamt;
		this.healthamt = healthamt;
	};
	
	public static ArtifactItemType getTypeFromData(int dataval) {
		for (int i=0;i<ArtifactItemType.values().length;i++) {
			if (ArtifactItemType.values()[i].getDataValue()==dataval) {
				return ArtifactItemType.values()[i];
			}
		}
		return null;
	}
	
	public static ArtifactItemType getArtifactItemTypeFromItemStack(ItemStack item) {
		String check = item.getType().toString();
		String[] checktypes = new String[]{"PICKAXE","AXE","SWORD","PICKAXE","HOE",
				"BOW","SPADE","HELMET","CHESTPLATE","LEGGINGS","BOOTS","FISHING_ROD"};
		for (int i=0;i<checktypes.length;i++) {
			if (check.contains(checktypes[i])) {
				if (checktypes[i].equalsIgnoreCase("SPADE")) {checktypes[i]="SHOVEL";}
				return ArtifactItemType.valueOf(checktypes[i]);
			}
		}
		return null;
	}
	
	public int getDataValue() {
		return this.data;
	}
	public String getItemName() {
		return this.itemname;
	}
	public TierType getTier() {
		return this.tier;
	}
	public UpgradePath getUpgradePath() {
		return this.upgrade;
	}
	public double getDamageAmt(int tier) {
		if (this.damageamt.length>=tier) {
			TwosideKeeper.log("Returning value "+this.damageamt[tier-1], 5);
			return this.damageamt[tier-1];
		} else {
			return -1;
		}
	}
	public int getHealthAmt(int tier) {
		if (this.healthamt.length>=tier) {
			return this.healthamt[tier-1];
		} else {
			return -1;
		}
	}
	
	public ShapedRecipe defineBaseRecipe() {
		ShapedRecipe axe_layout_recipe = new ShapedRecipe(Artifact.createRecipe(0, this));
		axe_layout_recipe.shape(recipe);
		axe_layout_recipe.setIngredient('E', Material.PUMPKIN_SEEDS);
		return axe_layout_recipe;
	}
	public void defineAllDecompRecipes() {
		for (int i=0;i<10;i++) {
			ShapelessRecipe decomp_recipe = new ShapelessRecipe(Artifact.createRecipe(i+1, this));
			decomp_recipe.addIngredient(itemtiers[i].getType());
			Bukkit.addRecipe(decomp_recipe);
		}
	}
	@SuppressWarnings("deprecation")
	public void defineAllUpgradeRecipes() {
		for (int i=0;i<10;i++) {
			ShapelessRecipe upgrade_recipe = new ShapelessRecipe(this.getTieredItem(i+1));
			upgrade_recipe.addIngredient(Material.STAINED_GLASS_PANE, this.getDataValue());
			upgrade_recipe.addIngredient(this.getTieredItem(i+1).getType(),-1);
			Bukkit.addRecipe(upgrade_recipe);
		}
	}
	
	public ItemStack getTieredItem(int tier) {
		ItemStack ouritem = itemtiers[tier-1];
		switch (tier) {
			case 1:{
				if (upgrade==UpgradePath.WEAPON ||
						upgrade==UpgradePath.SWORD ||
						upgrade==UpgradePath.AXE ||
						upgrade==UpgradePath.FISHING_ROD ||
						upgrade==UpgradePath.SCYTHE) {
					ouritem.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
					ouritem.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
					if (upgrade==UpgradePath.FISHING_ROD) {
					}
				}
				if (upgrade==UpgradePath.ARMOR) {
					ouritem.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
					ouritem.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
				}
				if (upgrade==UpgradePath.TOOL ||
						upgrade==UpgradePath.SHOVEL ||
						upgrade==UpgradePath.SCYTHE ||
						upgrade==UpgradePath.PICKAXE) {
					ouritem.addUnsafeEnchantment(Enchantment.DIG_SPEED, 1);
					ouritem.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
				}
				if (upgrade==UpgradePath.BOW) {
					ouritem.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 1);
					ouritem.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
				}
			}break;
			case 2:{
				if (upgrade==UpgradePath.WEAPON ||
						upgrade==UpgradePath.SWORD ||
						upgrade==UpgradePath.AXE ||
						upgrade==UpgradePath.FISHING_ROD ||
						upgrade==UpgradePath.SCYTHE) {
					ouritem.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 2);
					ouritem.addUnsafeEnchantment(Enchantment.DURABILITY, 2);
					if (upgrade==UpgradePath.FISHING_ROD) {
					}
				}
				if (upgrade==UpgradePath.ARMOR) {
					ouritem.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
					ouritem.addUnsafeEnchantment(Enchantment.DURABILITY, 2);
				}
				if (upgrade==UpgradePath.TOOL ||
						upgrade==UpgradePath.SHOVEL ||
						upgrade==UpgradePath.SCYTHE ||
						upgrade==UpgradePath.PICKAXE) {
					ouritem.addUnsafeEnchantment(Enchantment.DIG_SPEED, 2);
					ouritem.addUnsafeEnchantment(Enchantment.DURABILITY, 2);
				}
				if (upgrade==UpgradePath.BOW) {
					ouritem.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 2);
					ouritem.addUnsafeEnchantment(Enchantment.DURABILITY, 2);
				}
			}break;
			case 3:
			case 4:{
				if (upgrade==UpgradePath.WEAPON ||
						upgrade==UpgradePath.SWORD ||
						upgrade==UpgradePath.AXE ||
						upgrade==UpgradePath.FISHING_ROD ||
						upgrade==UpgradePath.SCYTHE) {
					ouritem.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 3);
					ouritem.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
					ouritem.addUnsafeEnchantment(Enchantment.LOOT_BONUS_MOBS, 1);
					if (upgrade==UpgradePath.FISHING_ROD) {
					}
				}
				if (upgrade==UpgradePath.ARMOR) {
					ouritem.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
					ouritem.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 1);
					ouritem.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 1);
					ouritem.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, 1);
					ouritem.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, 1);
					ouritem.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
				}
				if (upgrade==UpgradePath.TOOL ||
						upgrade==UpgradePath.SHOVEL ||
						upgrade==UpgradePath.SCYTHE ||
						upgrade==UpgradePath.PICKAXE) {
					ouritem.addUnsafeEnchantment(Enchantment.DIG_SPEED, 3);
					ouritem.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
				}
				if (upgrade==UpgradePath.BOW) {
					ouritem.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 3);
					ouritem.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
				}
			}break;
			case 5:
			case 6:{
				if (upgrade==UpgradePath.WEAPON ||
						upgrade==UpgradePath.SWORD ||
						upgrade==UpgradePath.AXE ||
						upgrade==UpgradePath.FISHING_ROD ||
						upgrade==UpgradePath.SCYTHE) {
					ouritem.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 4);
					ouritem.addUnsafeEnchantment(Enchantment.DURABILITY, 4);
					ouritem.addUnsafeEnchantment(Enchantment.LOOT_BONUS_MOBS, 1);
					if (upgrade==UpgradePath.FISHING_ROD) {
					}
				}
				if (upgrade==UpgradePath.ARMOR) {
					ouritem.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
					ouritem.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 2);
					ouritem.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 1);
					ouritem.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, 1);
					ouritem.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, 2);
					ouritem.addUnsafeEnchantment(Enchantment.DURABILITY, 4);
				}
				if (upgrade==UpgradePath.TOOL ||
						upgrade==UpgradePath.SHOVEL ||
						upgrade==UpgradePath.SCYTHE ||
						upgrade==UpgradePath.PICKAXE) {
					ouritem.addUnsafeEnchantment(Enchantment.DIG_SPEED, 4);
					ouritem.addUnsafeEnchantment(Enchantment.DURABILITY, 4);
				}
				if (upgrade==UpgradePath.BOW) {
					ouritem.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 4);
					ouritem.addUnsafeEnchantment(Enchantment.DURABILITY, 4);
				}
			}break;
			case 7:
			case 8:{
				if (upgrade==UpgradePath.WEAPON ||
						upgrade==UpgradePath.SWORD ||
						upgrade==UpgradePath.AXE ||
						upgrade==UpgradePath.FISHING_ROD ||
						upgrade==UpgradePath.SCYTHE) {
					ouritem.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 5);
					ouritem.addUnsafeEnchantment(Enchantment.DURABILITY, 5);
					ouritem.addUnsafeEnchantment(Enchantment.LOOT_BONUS_MOBS, 2);
					if (upgrade==UpgradePath.FISHING_ROD) {
						ouritem.addUnsafeEnchantment(Enchantment.LURE, 1);
					}
				}
				if (upgrade==UpgradePath.ARMOR) {
					ouritem.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 5);
					ouritem.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 4);
					ouritem.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 2);
					ouritem.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, 2);
					ouritem.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, 4);
					ouritem.addUnsafeEnchantment(Enchantment.DURABILITY, 5);
				}
				if (upgrade==UpgradePath.TOOL ||
						upgrade==UpgradePath.SHOVEL ||
						upgrade==UpgradePath.SCYTHE ||
						upgrade==UpgradePath.PICKAXE) {
					ouritem.addUnsafeEnchantment(Enchantment.DIG_SPEED, 5);
					ouritem.addUnsafeEnchantment(Enchantment.DURABILITY, 5);
				}
				if (upgrade==UpgradePath.BOW) {
					ouritem.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 5);
					ouritem.addUnsafeEnchantment(Enchantment.DURABILITY, 5);
				}
			}break;
			case 9:
			case 10:{
				if (upgrade==UpgradePath.WEAPON ||
						upgrade==UpgradePath.SWORD ||
						upgrade==UpgradePath.AXE ||
						upgrade==UpgradePath.FISHING_ROD ||
						upgrade==UpgradePath.SCYTHE) {
					ouritem.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 6);
					ouritem.addUnsafeEnchantment(Enchantment.DURABILITY, 6);
					ouritem.addUnsafeEnchantment(Enchantment.LOOT_BONUS_MOBS, 3);
					if (upgrade==UpgradePath.FISHING_ROD) {
						ouritem.addUnsafeEnchantment(Enchantment.LURE, 2);
					}
				}
				if (upgrade==UpgradePath.ARMOR) {
					ouritem.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 6);
					ouritem.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 6);
					ouritem.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 4);
					ouritem.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, 4);
					ouritem.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, 6);
					ouritem.addUnsafeEnchantment(Enchantment.DURABILITY, 6);
				}
				if (upgrade==UpgradePath.TOOL ||
						upgrade==UpgradePath.SHOVEL ||
						upgrade==UpgradePath.SCYTHE ||
						upgrade==UpgradePath.PICKAXE) {
					ouritem.addUnsafeEnchantment(Enchantment.DIG_SPEED, 6);
					ouritem.addUnsafeEnchantment(Enchantment.DURABILITY, 6);
					ouritem.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 1);
				}
				if (upgrade==UpgradePath.BOW) {
					ouritem.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 6);
					ouritem.addUnsafeEnchantment(Enchantment.DURABILITY, 6);
				}
			}break;
			case 11:{
				if (upgrade==UpgradePath.WEAPON ||
						upgrade==UpgradePath.SWORD ||
						upgrade==UpgradePath.AXE ||
						upgrade==UpgradePath.FISHING_ROD ||
						upgrade==UpgradePath.SCYTHE) {
					ouritem.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 7);
					ouritem.addUnsafeEnchantment(Enchantment.DURABILITY, 7);
					ouritem.addUnsafeEnchantment(Enchantment.LOOT_BONUS_MOBS, 5);
					ouritem.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
					if (upgrade==UpgradePath.FISHING_ROD) {
						ouritem.addUnsafeEnchantment(Enchantment.LURE, 3);
					}
				}
				if (upgrade==UpgradePath.ARMOR) {
					ouritem.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 7);
					ouritem.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 7);
					ouritem.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 7);
					ouritem.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, 7);
					ouritem.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, 7);
					ouritem.addUnsafeEnchantment(Enchantment.DURABILITY, 7);
				}
				if (upgrade==UpgradePath.TOOL ||
						upgrade==UpgradePath.SHOVEL ||
						upgrade==UpgradePath.SCYTHE ||
						upgrade==UpgradePath.PICKAXE) {
					ouritem.addUnsafeEnchantment(Enchantment.DIG_SPEED, 7);
					ouritem.addUnsafeEnchantment(Enchantment.DURABILITY, 7);
					ouritem.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 2);
					ouritem.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 2);
				}
				if (upgrade==UpgradePath.BOW) {
					ouritem.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 7);
					ouritem.addUnsafeEnchantment(Enchantment.DURABILITY, 7);
					ouritem.addUnsafeEnchantment(Enchantment.ARROW_KNOCKBACK, 1);
				}
			}break;
			case 12:
			case 13:{
				if (upgrade==UpgradePath.WEAPON ||
						upgrade==UpgradePath.SWORD ||
						upgrade==UpgradePath.AXE ||
						upgrade==UpgradePath.FISHING_ROD ||
						upgrade==UpgradePath.SCYTHE) {
					ouritem.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 8);
					ouritem.addUnsafeEnchantment(Enchantment.DURABILITY, 8);
					ouritem.addUnsafeEnchantment(Enchantment.LOOT_BONUS_MOBS, 7);
					ouritem.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
					if (upgrade==UpgradePath.FISHING_ROD) {
						ouritem.addUnsafeEnchantment(Enchantment.LURE, 4);
					}
				}
				if (upgrade==UpgradePath.ARMOR) {
					ouritem.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 8);
					ouritem.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 8);
					ouritem.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 8);
					ouritem.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, 8);
					ouritem.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, 8);
					ouritem.addUnsafeEnchantment(Enchantment.DURABILITY, 8);
				}
				if (upgrade==UpgradePath.TOOL ||
						upgrade==UpgradePath.SHOVEL ||
						upgrade==UpgradePath.SCYTHE ||
						upgrade==UpgradePath.PICKAXE) {
					ouritem.addUnsafeEnchantment(Enchantment.DIG_SPEED, 8);
					ouritem.addUnsafeEnchantment(Enchantment.DURABILITY, 8);
					ouritem.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 4);
					ouritem.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 6);
				}
				if (upgrade==UpgradePath.BOW) {
					ouritem.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 8);
					ouritem.addUnsafeEnchantment(Enchantment.DURABILITY, 8);
					ouritem.addUnsafeEnchantment(Enchantment.ARROW_KNOCKBACK, 1);
				}
			}break;
			case 14:{
				if (upgrade==UpgradePath.WEAPON ||
						upgrade==UpgradePath.SWORD ||
						upgrade==UpgradePath.AXE ||
						upgrade==UpgradePath.FISHING_ROD ||
						upgrade==UpgradePath.SCYTHE) {
					ouritem.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 9);
					ouritem.addUnsafeEnchantment(Enchantment.DURABILITY, 9);
					ouritem.addUnsafeEnchantment(Enchantment.LOOT_BONUS_MOBS, 9);
					ouritem.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
					if (upgrade==UpgradePath.FISHING_ROD) {
						ouritem.addUnsafeEnchantment(Enchantment.LURE, 5);
					}
				}
				if (upgrade==UpgradePath.ARMOR) {
					ouritem.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 9);
					ouritem.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 9);
					ouritem.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 9);
					ouritem.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, 9);
					ouritem.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, 9);
					ouritem.addUnsafeEnchantment(Enchantment.DURABILITY, 9);
				}
				if (upgrade==UpgradePath.TOOL ||
						upgrade==UpgradePath.SHOVEL ||
						upgrade==UpgradePath.SCYTHE ||
						upgrade==UpgradePath.PICKAXE) {
					ouritem.addUnsafeEnchantment(Enchantment.DIG_SPEED, 9);
					ouritem.addUnsafeEnchantment(Enchantment.DURABILITY, 9);
					ouritem.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 7);
					ouritem.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 9);
				}
				if (upgrade==UpgradePath.BOW) {
					ouritem.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 9);
					ouritem.addUnsafeEnchantment(Enchantment.DURABILITY, 9);
					ouritem.addUnsafeEnchantment(Enchantment.ARROW_KNOCKBACK, 1);
					ouritem.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1);
				}
			}break;
			default:{
				if (upgrade==UpgradePath.WEAPON ||
						upgrade==UpgradePath.SWORD ||
						upgrade==UpgradePath.AXE ||
						upgrade==UpgradePath.FISHING_ROD ||
						upgrade==UpgradePath.SCYTHE) {
					ouritem.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 10);
					ouritem.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
					ouritem.addUnsafeEnchantment(Enchantment.LOOT_BONUS_MOBS, 10);
					ouritem.addUnsafeEnchantment(Enchantment.KNOCKBACK, 2);
					if (upgrade==UpgradePath.FISHING_ROD) {
						ouritem.addUnsafeEnchantment(Enchantment.LURE, 6);
					}
				}
				if (upgrade==UpgradePath.ARMOR) {
					ouritem.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 10);
					ouritem.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 10);
					ouritem.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 10);
					ouritem.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, 10);
					ouritem.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, 10);
					ouritem.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
				}
				if (upgrade==UpgradePath.TOOL ||
						upgrade==UpgradePath.SHOVEL ||
						upgrade==UpgradePath.SCYTHE ||
						upgrade==UpgradePath.PICKAXE) {
					ouritem.addUnsafeEnchantment(Enchantment.DIG_SPEED, 10);
					ouritem.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
					ouritem.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 10);
					ouritem.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 10);
				}
				if (upgrade==UpgradePath.BOW) {
					ouritem.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 10);
					ouritem.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
					ouritem.addUnsafeEnchantment(Enchantment.ARROW_KNOCKBACK, 2);
					ouritem.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 10);
				}
			}
		}
		return Artifact.convert(ouritem,true);
	}
	
}
	
enum TierType {
	ALL, //Contains Wooden, Stone, Iron, Gold, Diamond Variants of this item.
	ONE, //Only the specified item exists for this type of Artifact.
	ARMOR //The armor type contains Leather, Iron, Gold, and Diamond Variants.
}

