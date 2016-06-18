package sig.plugin.TwosideKeeper;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Dye;

public class Recipes {
	public static void Initialize_ItemCube_Recipes() {
		ItemStack item_ItemCube = new ItemStack(Material.CHEST);
		
		List<String> item_ItemCube_lore = new ArrayList<String>();
		item_ItemCube_lore.add("A storage container that can");
		item_ItemCube_lore.add("be carried around. "+ChatColor.GOLD+"Open by");
		item_ItemCube_lore.add(ChatColor.GOLD+"right-clicking.");
		ItemMeta item_ItemCube_meta=item_ItemCube.getItemMeta();
		item_ItemCube_meta.setLore(item_ItemCube_lore);
		item_ItemCube_meta.setDisplayName("Item Cube");
		item_ItemCube.setItemMeta(item_ItemCube_meta);
		
		ShapedRecipe ItemCube = new ShapedRecipe(item_ItemCube);
		ItemCube.shape("ppp","pcp","ppp");
		ItemCube.setIngredient('p', Material.WOOD, -1);
		ItemCube.setIngredient('c', Material.CHEST);
		
		Bukkit.addRecipe(ItemCube);
		//------------------------------
		item_ItemCube = new ItemStack(Material.STORAGE_MINECART);
		item_ItemCube_meta=item_ItemCube.getItemMeta();
		item_ItemCube_meta.setLore(item_ItemCube_lore);
		item_ItemCube_meta.setDisplayName("Large Item Cube");
		item_ItemCube.setItemMeta(item_ItemCube_meta);
		
		ItemCube = new ShapedRecipe(item_ItemCube);
		ItemCube.shape("ppp","gcg","ppp");
		ItemCube.setIngredient('p', Material.WOOD, -1);
		ItemCube.setIngredient('g', Material.GOLD_BLOCK);
		ItemCube.setIngredient('c', Material.CHEST);
		
		Bukkit.addRecipe(ItemCube);
		//------------------------------
		item_ItemCube = new ItemStack(Material.ENDER_CHEST);
		item_ItemCube_meta=item_ItemCube.getItemMeta();
		item_ItemCube_meta.setLore(item_ItemCube_lore);
		item_ItemCube_meta.setDisplayName("Ender Item Cube");
		item_ItemCube.setItemMeta(item_ItemCube_meta);
		
		ItemCube = new ShapedRecipe(item_ItemCube);
		ItemCube.shape("ooo","ece","ooo");
		ItemCube.setIngredient('o', Material.OBSIDIAN);
		ItemCube.setIngredient('e', Material.EMERALD);
		ItemCube.setIngredient('c', Material.ENDER_CHEST);
		
		Bukkit.addRecipe(ItemCube);
		//------------------------------
	}
	public static void Initialize_ArrowQuiver_Recipe() {
		ItemStack arrow_quiver = new ItemStack(Material.TIPPED_ARROW);
		
		List<String> arrow_quiver_lore = new ArrayList<String>();
		arrow_quiver_lore.add("A quiver that holds many arrows.");
		arrow_quiver_lore.add(ChatColor.GRAY+"Arrows Remaining: "+ChatColor.YELLOW+"5");
		ItemMeta arrow_quiver_meta=arrow_quiver.getItemMeta();
		arrow_quiver_meta.setLore(arrow_quiver_lore);
		arrow_quiver_meta.setDisplayName(ChatColor.BLUE+"Arrow Quiver");
		arrow_quiver.setItemMeta(arrow_quiver_meta);
		
		arrow_quiver.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 5);
		
		arrow_quiver.setAmount(1);
		
		ShapedRecipe ArrowQuiver = new ShapedRecipe(arrow_quiver);
		ArrowQuiver.shape("xle","lsl","xlx");
		ArrowQuiver.setIngredient('s', Material.SPECTRAL_ARROW);
		ArrowQuiver.setIngredient('l', Material.LEATHER);
		ArrowQuiver.setIngredient('e', Material.EMERALD_BLOCK);
		
		Bukkit.addRecipe(ArrowQuiver);
	}
	public static void Initialize_BlockArmor_Recipes() {
		ItemStack blockarmorpc = new ItemStack(Material.IRON_HELMET);
		
		List<String> blockarmorpc_lore = new ArrayList<String>();
		blockarmorpc_lore.add(ChatColor.BLUE+""+ChatColor.ITALIC+"Hardened Armor");
		blockarmorpc_lore.add(ChatColor.GRAY+"Twice as strong");
		blockarmorpc_lore.add(ChatColor.GRAY+"Breaks Remaining: "+ChatColor.YELLOW+"4");
		ItemMeta blockarmorpc_meta=blockarmorpc.getItemMeta();
		blockarmorpc_meta.setLore(blockarmorpc_lore);
		blockarmorpc_meta.setDisplayName(ChatColor.BLUE+"Hardened Iron Helmet");
		blockarmorpc.setItemMeta(blockarmorpc_meta);
		
		ShapedRecipe BlockArmor = new ShapedRecipe(blockarmorpc);
		BlockArmor.shape("aaa","axa","xxx");
		BlockArmor.setIngredient('a', Material.IRON_BLOCK);
		
		Bukkit.addRecipe(BlockArmor);
		//--------------------------------------------
		blockarmorpc = new ItemStack(Material.IRON_CHESTPLATE);
		
		blockarmorpc_lore = new ArrayList<String>();
		blockarmorpc_lore.add(ChatColor.BLUE+""+ChatColor.ITALIC+"Hardened Armor");
		blockarmorpc_lore.add(ChatColor.GRAY+"Twice as strong");
		blockarmorpc_lore.add(ChatColor.GRAY+"Breaks Remaining: "+ChatColor.YELLOW+"4");
		blockarmorpc_meta=blockarmorpc.getItemMeta();
		blockarmorpc_meta.setLore(blockarmorpc_lore);
		blockarmorpc_meta.setDisplayName(ChatColor.BLUE+"Hardened Iron Chestplate");
		blockarmorpc.setItemMeta(blockarmorpc_meta);
		
		BlockArmor = new ShapedRecipe(blockarmorpc);
		BlockArmor.shape("axa","aaa","aaa");
		BlockArmor.setIngredient('a', Material.IRON_BLOCK);
		
		Bukkit.addRecipe(BlockArmor);
		//--------------------------------------------
		blockarmorpc = new ItemStack(Material.IRON_LEGGINGS);
		
		blockarmorpc_lore = new ArrayList<String>();
		blockarmorpc_lore.add(ChatColor.BLUE+""+ChatColor.ITALIC+"Hardened Armor");
		blockarmorpc_lore.add(ChatColor.GRAY+"Twice as strong");
		blockarmorpc_lore.add(ChatColor.GRAY+"Breaks Remaining: "+ChatColor.YELLOW+"4");
		blockarmorpc_meta=blockarmorpc.getItemMeta();
		blockarmorpc_meta.setLore(blockarmorpc_lore);
		blockarmorpc_meta.setDisplayName(ChatColor.BLUE+"Hardened Iron Leggings");
		blockarmorpc.setItemMeta(blockarmorpc_meta);
		
		BlockArmor = new ShapedRecipe(blockarmorpc);
		BlockArmor.shape("aaa","axa","axa");
		BlockArmor.setIngredient('a', Material.IRON_BLOCK);
		
		Bukkit.addRecipe(BlockArmor);
		//--------------------------------------------
		blockarmorpc = new ItemStack(Material.IRON_BOOTS);
		
		blockarmorpc_lore = new ArrayList<String>();
		blockarmorpc_lore.add(ChatColor.BLUE+""+ChatColor.ITALIC+"Hardened Armor");
		blockarmorpc_lore.add(ChatColor.GRAY+"Twice as strong");
		blockarmorpc_lore.add(ChatColor.GRAY+"Breaks Remaining: "+ChatColor.YELLOW+"4");
		blockarmorpc_meta=blockarmorpc.getItemMeta();
		blockarmorpc_meta.setLore(blockarmorpc_lore);
		blockarmorpc_meta.setDisplayName(ChatColor.BLUE+"Hardened Iron Boots");
		blockarmorpc.setItemMeta(blockarmorpc_meta);
		
		BlockArmor = new ShapedRecipe(blockarmorpc);
		BlockArmor.shape("axa","axa");
		BlockArmor.setIngredient('a', Material.IRON_BLOCK);
		
		Bukkit.addRecipe(BlockArmor);
		//--------------------------------------------
		blockarmorpc = new ItemStack(Material.GOLD_HELMET);
		
		blockarmorpc_lore = new ArrayList<String>();
		blockarmorpc_lore.add(ChatColor.BLUE+""+ChatColor.ITALIC+"Hardened Armor");
		blockarmorpc_lore.add(ChatColor.GRAY+"Twice as strong");
		blockarmorpc_lore.add(ChatColor.GRAY+"Breaks Remaining: "+ChatColor.YELLOW+"4");
		blockarmorpc_meta=blockarmorpc.getItemMeta();
		blockarmorpc_meta.setLore(blockarmorpc_lore);
		blockarmorpc_meta.setDisplayName(ChatColor.BLUE+"Hardened Gold Helmet");
		blockarmorpc.setItemMeta(blockarmorpc_meta);
		
		BlockArmor = new ShapedRecipe(blockarmorpc);
		BlockArmor.shape("aaa","axa","xxx");
		BlockArmor.setIngredient('a', Material.GOLD_BLOCK);
		
		Bukkit.addRecipe(BlockArmor);
		//--------------------------------------------
		blockarmorpc = new ItemStack(Material.GOLD_CHESTPLATE);
		
		blockarmorpc_lore = new ArrayList<String>();
		blockarmorpc_lore.add(ChatColor.BLUE+""+ChatColor.ITALIC+"Hardened Armor");
		blockarmorpc_lore.add(ChatColor.GRAY+"Twice as strong");
		blockarmorpc_lore.add(ChatColor.GRAY+"Breaks Remaining: "+ChatColor.YELLOW+"4");
		blockarmorpc_meta=blockarmorpc.getItemMeta();
		blockarmorpc_meta.setLore(blockarmorpc_lore);
		blockarmorpc_meta.setDisplayName(ChatColor.BLUE+"Hardened Gold Chestplate");
		blockarmorpc.setItemMeta(blockarmorpc_meta);
		
		BlockArmor = new ShapedRecipe(blockarmorpc);
		BlockArmor.shape("axa","aaa","aaa");
		BlockArmor.setIngredient('a', Material.GOLD_BLOCK);
		
		Bukkit.addRecipe(BlockArmor);
		//--------------------------------------------
		blockarmorpc = new ItemStack(Material.GOLD_LEGGINGS);
		
		blockarmorpc_lore = new ArrayList<String>();
		blockarmorpc_lore.add(ChatColor.BLUE+""+ChatColor.ITALIC+"Hardened Armor");
		blockarmorpc_lore.add(ChatColor.GRAY+"Twice as strong");
		blockarmorpc_lore.add(ChatColor.GRAY+"Breaks Remaining: "+ChatColor.YELLOW+"4");
		blockarmorpc_meta=blockarmorpc.getItemMeta();
		blockarmorpc_meta.setLore(blockarmorpc_lore);
		blockarmorpc_meta.setDisplayName(ChatColor.BLUE+"Hardened Gold Leggings");
		blockarmorpc.setItemMeta(blockarmorpc_meta);
		
		BlockArmor = new ShapedRecipe(blockarmorpc);
		BlockArmor.shape("aaa","axa","axa");
		BlockArmor.setIngredient('a', Material.GOLD_BLOCK);
		
		Bukkit.addRecipe(BlockArmor);
		//--------------------------------------------
		blockarmorpc = new ItemStack(Material.GOLD_BOOTS);
		
		blockarmorpc_lore = new ArrayList<String>();
		blockarmorpc_lore.add(ChatColor.BLUE+""+ChatColor.ITALIC+"Hardened Armor");
		blockarmorpc_lore.add(ChatColor.GRAY+"Twice as strong");
		blockarmorpc_lore.add(ChatColor.GRAY+"Breaks Remaining: "+ChatColor.YELLOW+"4");
		blockarmorpc_meta=blockarmorpc.getItemMeta();
		blockarmorpc_meta.setLore(blockarmorpc_lore);
		blockarmorpc_meta.setDisplayName(ChatColor.BLUE+"Hardened Gold Boots");
		blockarmorpc.setItemMeta(blockarmorpc_meta);
		
		BlockArmor = new ShapedRecipe(blockarmorpc);
		BlockArmor.shape("axa","axa");
		BlockArmor.setIngredient('a', Material.GOLD_BLOCK);
		
		Bukkit.addRecipe(BlockArmor);
		//--------------------------------------------
		blockarmorpc = new ItemStack(Material.DIAMOND_HELMET);
		
		blockarmorpc_lore = new ArrayList<String>();
		blockarmorpc_lore.add(ChatColor.BLUE+""+ChatColor.ITALIC+"Hardened Armor");
		blockarmorpc_lore.add(ChatColor.GRAY+"Twice as strong");
		blockarmorpc_lore.add(ChatColor.GRAY+"Breaks Remaining: "+ChatColor.YELLOW+"4");
		blockarmorpc_meta=blockarmorpc.getItemMeta();
		blockarmorpc_meta.setLore(blockarmorpc_lore);
		blockarmorpc_meta.setDisplayName(ChatColor.BLUE+"Hardened Diamond Helmet");
		blockarmorpc.setItemMeta(blockarmorpc_meta);
		
		BlockArmor = new ShapedRecipe(blockarmorpc);
		BlockArmor.shape("aaa","axa","xxx");
		BlockArmor.setIngredient('a', Material.DIAMOND_BLOCK);
		
		Bukkit.addRecipe(BlockArmor);
		//--------------------------------------------
		blockarmorpc = new ItemStack(Material.DIAMOND_CHESTPLATE);
		
		blockarmorpc_lore = new ArrayList<String>();
		blockarmorpc_lore.add(ChatColor.BLUE+""+ChatColor.ITALIC+"Hardened Armor");
		blockarmorpc_lore.add(ChatColor.GRAY+"Twice as strong");
		blockarmorpc_lore.add(ChatColor.GRAY+"Breaks Remaining: "+ChatColor.YELLOW+"4");
		blockarmorpc_meta=blockarmorpc.getItemMeta();
		blockarmorpc_meta.setLore(blockarmorpc_lore);
		blockarmorpc_meta.setDisplayName(ChatColor.BLUE+"Hardened Diamond Chestplate");
		blockarmorpc.setItemMeta(blockarmorpc_meta);
		
		BlockArmor = new ShapedRecipe(blockarmorpc);
		BlockArmor.shape("axa","aaa","aaa");
		BlockArmor.setIngredient('a', Material.DIAMOND_BLOCK);
		
		Bukkit.addRecipe(BlockArmor);
		//--------------------------------------------
		blockarmorpc = new ItemStack(Material.DIAMOND_LEGGINGS);
		
		blockarmorpc_lore = new ArrayList<String>();
		blockarmorpc_lore.add(ChatColor.BLUE+""+ChatColor.ITALIC+"Hardened Armor");
		blockarmorpc_lore.add(ChatColor.GRAY+"Twice as strong");
		blockarmorpc_lore.add(ChatColor.GRAY+"Breaks Remaining: "+ChatColor.YELLOW+"4");
		blockarmorpc_meta=blockarmorpc.getItemMeta();
		blockarmorpc_meta.setLore(blockarmorpc_lore);
		blockarmorpc_meta.setDisplayName(ChatColor.BLUE+"Hardened Diamond Leggings");
		blockarmorpc.setItemMeta(blockarmorpc_meta);
		
		BlockArmor = new ShapedRecipe(blockarmorpc);
		BlockArmor.shape("aaa","axa","axa");
		BlockArmor.setIngredient('a', Material.DIAMOND_BLOCK);
		
		Bukkit.addRecipe(BlockArmor);
		//--------------------------------------------
		blockarmorpc = new ItemStack(Material.DIAMOND_BOOTS);
		
		blockarmorpc_lore = new ArrayList<String>();
		blockarmorpc_lore.add(ChatColor.BLUE+""+ChatColor.ITALIC+"Hardened Armor");
		blockarmorpc_lore.add(ChatColor.GRAY+"Twice as strong");
		blockarmorpc_lore.add(ChatColor.GRAY+"Breaks Remaining: "+ChatColor.YELLOW+"4");
		blockarmorpc_meta=blockarmorpc.getItemMeta();
		blockarmorpc_meta.setLore(blockarmorpc_lore);
		blockarmorpc_meta.setDisplayName(ChatColor.BLUE+"Hardened Diamond Boots");
		blockarmorpc.setItemMeta(blockarmorpc_meta);
		
		BlockArmor = new ShapedRecipe(blockarmorpc);
		BlockArmor.shape("axa","axa");
		BlockArmor.setIngredient('a', Material.DIAMOND_BLOCK);
		
		Bukkit.addRecipe(BlockArmor);
		//--------------------------------------------
	}
	public static void Initialize_ItemDeconstruction_Recipes() {
		ShapelessRecipe decons_recipe = new ShapelessRecipe(new ItemStack(Material.LEATHER,4));
		decons_recipe.addIngredient(Material.LEATHER_BOOTS);
		Bukkit.addRecipe(decons_recipe);
		decons_recipe = new ShapelessRecipe(new ItemStack(Material.LEATHER,7));
		decons_recipe.addIngredient(Material.LEATHER_LEGGINGS);
		Bukkit.addRecipe(decons_recipe);
		decons_recipe = new ShapelessRecipe(new ItemStack(Material.LEATHER,8));
		decons_recipe.addIngredient(Material.LEATHER_CHESTPLATE);
		Bukkit.addRecipe(decons_recipe);
		decons_recipe = new ShapelessRecipe(new ItemStack(Material.LEATHER,5));
		decons_recipe.addIngredient(Material.LEATHER_HELMET);
		Bukkit.addRecipe(decons_recipe);
		decons_recipe = new ShapelessRecipe(new ItemStack(Material.WOOD,2));
		decons_recipe.addIngredient(Material.WOOD_SWORD);
		Bukkit.addRecipe(decons_recipe);
		decons_recipe = new ShapelessRecipe(new ItemStack(Material.WOOD,3));
		decons_recipe.addIngredient(Material.WOOD_AXE);
		Bukkit.addRecipe(decons_recipe);
		decons_recipe = new ShapelessRecipe(new ItemStack(Material.WOOD,3));
		decons_recipe.addIngredient(Material.WOOD_PICKAXE);
		Bukkit.addRecipe(decons_recipe);
		decons_recipe = new ShapelessRecipe(new ItemStack(Material.WOOD,2));
		decons_recipe.addIngredient(Material.WOOD_HOE);
		Bukkit.addRecipe(decons_recipe);
		decons_recipe = new ShapelessRecipe(new ItemStack(Material.WOOD,1));
		decons_recipe.addIngredient(Material.WOOD_SPADE);
		Bukkit.addRecipe(decons_recipe);
		decons_recipe = new ShapelessRecipe(new ItemStack(Material.COBBLESTONE,2));
		decons_recipe.addIngredient(Material.STONE_SWORD);
		Bukkit.addRecipe(decons_recipe);
		decons_recipe = new ShapelessRecipe(new ItemStack(Material.COBBLESTONE,3));
		decons_recipe.addIngredient(Material.STONE_AXE);
		Bukkit.addRecipe(decons_recipe);
		decons_recipe = new ShapelessRecipe(new ItemStack(Material.COBBLESTONE,3));
		decons_recipe.addIngredient(Material.STONE_PICKAXE);
		Bukkit.addRecipe(decons_recipe);
		decons_recipe = new ShapelessRecipe(new ItemStack(Material.COBBLESTONE,2));
		decons_recipe.addIngredient(Material.STONE_HOE);
		Bukkit.addRecipe(decons_recipe);
		decons_recipe = new ShapelessRecipe(new ItemStack(Material.COBBLESTONE,1));
		decons_recipe.addIngredient(Material.STONE_SPADE);
		Bukkit.addRecipe(decons_recipe);
		decons_recipe = new ShapelessRecipe(new ItemStack(Material.IRON_INGOT,4));
		decons_recipe.addIngredient(Material.IRON_BOOTS);
		Bukkit.addRecipe(decons_recipe);
		decons_recipe = new ShapelessRecipe(new ItemStack(Material.IRON_INGOT,7));
		decons_recipe.addIngredient(Material.IRON_LEGGINGS);
		Bukkit.addRecipe(decons_recipe);
		decons_recipe = new ShapelessRecipe(new ItemStack(Material.IRON_INGOT,8));
		decons_recipe.addIngredient(Material.IRON_CHESTPLATE);
		Bukkit.addRecipe(decons_recipe);
		decons_recipe = new ShapelessRecipe(new ItemStack(Material.IRON_INGOT,5));
		decons_recipe.addIngredient(Material.IRON_HELMET);
		Bukkit.addRecipe(decons_recipe);
		decons_recipe = new ShapelessRecipe(new ItemStack(Material.IRON_INGOT,2));
		decons_recipe.addIngredient(Material.IRON_SWORD);
		Bukkit.addRecipe(decons_recipe);
		decons_recipe = new ShapelessRecipe(new ItemStack(Material.IRON_INGOT,3));
		decons_recipe.addIngredient(Material.IRON_AXE);
		Bukkit.addRecipe(decons_recipe);
		decons_recipe = new ShapelessRecipe(new ItemStack(Material.IRON_INGOT,3));
		decons_recipe.addIngredient(Material.IRON_PICKAXE);
		Bukkit.addRecipe(decons_recipe);
		decons_recipe = new ShapelessRecipe(new ItemStack(Material.IRON_INGOT,2));
		decons_recipe.addIngredient(Material.IRON_HOE);
		Bukkit.addRecipe(decons_recipe);
		decons_recipe = new ShapelessRecipe(new ItemStack(Material.IRON_INGOT,1));
		decons_recipe.addIngredient(Material.IRON_SPADE);
		Bukkit.addRecipe(decons_recipe);
		decons_recipe = new ShapelessRecipe(new ItemStack(Material.GOLD_INGOT,4));
		decons_recipe.addIngredient(Material.GOLD_BOOTS);
		Bukkit.addRecipe(decons_recipe);
		decons_recipe = new ShapelessRecipe(new ItemStack(Material.GOLD_INGOT,7));
		decons_recipe.addIngredient(Material.GOLD_LEGGINGS);
		Bukkit.addRecipe(decons_recipe);
		decons_recipe = new ShapelessRecipe(new ItemStack(Material.GOLD_INGOT,8));
		decons_recipe.addIngredient(Material.GOLD_CHESTPLATE);
		Bukkit.addRecipe(decons_recipe);
		decons_recipe = new ShapelessRecipe(new ItemStack(Material.GOLD_INGOT,5));
		decons_recipe.addIngredient(Material.GOLD_HELMET);
		Bukkit.addRecipe(decons_recipe);
		decons_recipe = new ShapelessRecipe(new ItemStack(Material.GOLD_INGOT,2));
		decons_recipe.addIngredient(Material.GOLD_SWORD);
		Bukkit.addRecipe(decons_recipe);
		decons_recipe = new ShapelessRecipe(new ItemStack(Material.GOLD_INGOT,3));
		decons_recipe.addIngredient(Material.GOLD_AXE);
		Bukkit.addRecipe(decons_recipe);
		decons_recipe = new ShapelessRecipe(new ItemStack(Material.GOLD_INGOT,3));
		decons_recipe.addIngredient(Material.GOLD_PICKAXE);
		Bukkit.addRecipe(decons_recipe);
		decons_recipe = new ShapelessRecipe(new ItemStack(Material.GOLD_INGOT,2));
		decons_recipe.addIngredient(Material.GOLD_HOE);
		Bukkit.addRecipe(decons_recipe);
		decons_recipe = new ShapelessRecipe(new ItemStack(Material.GOLD_INGOT,1));
		decons_recipe.addIngredient(Material.GOLD_SPADE);
		Bukkit.addRecipe(decons_recipe);
		decons_recipe = new ShapelessRecipe(new ItemStack(Material.DIAMOND,4));
		decons_recipe.addIngredient(Material.DIAMOND_BOOTS);
		Bukkit.addRecipe(decons_recipe);
		decons_recipe = new ShapelessRecipe(new ItemStack(Material.DIAMOND,7));
		decons_recipe.addIngredient(Material.DIAMOND_LEGGINGS);
		Bukkit.addRecipe(decons_recipe);
		decons_recipe = new ShapelessRecipe(new ItemStack(Material.DIAMOND,8));
		decons_recipe.addIngredient(Material.DIAMOND_CHESTPLATE);
		Bukkit.addRecipe(decons_recipe);
		decons_recipe = new ShapelessRecipe(new ItemStack(Material.DIAMOND,5));
		decons_recipe.addIngredient(Material.DIAMOND_HELMET);
		Bukkit.addRecipe(decons_recipe);
		decons_recipe = new ShapelessRecipe(new ItemStack(Material.DIAMOND,2));
		decons_recipe.addIngredient(Material.DIAMOND_SWORD);
		Bukkit.addRecipe(decons_recipe);
		decons_recipe = new ShapelessRecipe(new ItemStack(Material.DIAMOND,3));
		decons_recipe.addIngredient(Material.DIAMOND_AXE);
		Bukkit.addRecipe(decons_recipe);
		decons_recipe = new ShapelessRecipe(new ItemStack(Material.DIAMOND,3));
		decons_recipe.addIngredient(Material.DIAMOND_PICKAXE);
		Bukkit.addRecipe(decons_recipe);
		decons_recipe = new ShapelessRecipe(new ItemStack(Material.DIAMOND,2));
		decons_recipe.addIngredient(Material.DIAMOND_HOE);
		Bukkit.addRecipe(decons_recipe);
		decons_recipe = new ShapelessRecipe(new ItemStack(Material.DIAMOND,1));
		decons_recipe.addIngredient(Material.DIAMOND_SPADE);
		Bukkit.addRecipe(decons_recipe);
	}
	public static void Initialize_WoolRecolor_Recipes() {
		for (int i=0;i<16;i++) {
			ShapedRecipe wool_recolor_recipe = new ShapedRecipe(new ItemStack(Material.WOOL,8,(byte)(15-i)));
			wool_recolor_recipe.shape("www","wdw","www");
			wool_recolor_recipe.setIngredient('w', Material.WOOL, -1);
			wool_recolor_recipe.setIngredient('d', Material.getMaterial(351), i);
			Bukkit.addRecipe(wool_recolor_recipe);
		}
	}
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
		ItemStack modded_plank = new ItemStack(Material.STEP,1);
		modded_plank.setDurability((short)2);
		ItemMeta m = modded_plank.getItemMeta();
		m.setDisplayName("Fireproof Oak Wood Slab");
		modded_plank.setItemMeta(m);
		stone_construction_recipe = new ShapelessRecipe(modded_plank);
		stone_construction_recipe.addIngredient(1, Material.WOOD_STEP);
		stone_construction_recipe.addIngredient(1, Material.SLIME_BALL);
		Bukkit.addRecipe(stone_construction_recipe);
	}
}
