package sig.plugin.TwosideKeeper.HelperStructures;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import sig.plugin.TwosideKeeper.TwosideKeeper;

public enum CustomRecipe {
	ENDER_ITEM_CUBE_DUPLICATE();
	
	
	Recipe rec;
	ItemStack result;
	String listname;
	
	CustomRecipe() {
		rec=null;
		result=null;
		listname=null;
	}
	
	CustomRecipe(Recipe recipe, ItemStack result, String listname) {
		this.rec=recipe;
		this.result=result;
		this.listname=listname;
	}
	
	public static void DisplayRecipe(Player p, CustomRecipe recipe) {
		if (isNotGoingToMakeAMillionErrors(recipe)) {
			if (recipe.rec instanceof ShapedRecipe) {
				aPlugin.API.viewRecipe(p, (ShapedRecipe)recipe.rec);
			} else {
				aPlugin.API.viewRecipe(p, (ShapelessRecipe)recipe.rec);
			}
		}
	}
	
	private static boolean isNotGoingToMakeAMillionErrors(CustomRecipe recipe) {
		return (recipe.rec!=null && recipe.result!=null && recipe.listname!=null);
	}

	public boolean isSameRecipe(ItemStack item) {
		if (item!=null &&
				item.hasItemMeta() &&
				item.getItemMeta().hasDisplayName() &&
				item.getItemMeta().getDisplayName().equalsIgnoreCase(this.toString())) {
			return true;
		} else {
			return false;
		}
	}
	
	/*
	 * Adds this enum's name to the name of the item so that it will process through the
	 * CustomRecipe enum.
	 */
	public ItemStack setCustomRecipeItem(ItemStack item) {
		ItemMeta m = item.getItemMeta();
		m.setDisplayName(this.toString());
		item.setItemMeta(m);
		//TwosideKeeper.log("Item is "+item.toString(), 2);
		return item;
	}
	
	/*
	 * Validates the recipe, processing the result if it's allowed.
	 */
	public void ValidateRecipe(PrepareItemCraftEvent ev) {
		switch (this) {
			case ENDER_ITEM_CUBE_DUPLICATE:{
				int itemcount=0;
				ItemStack newitem = null;
				ItemStack netherstar = null;
				for (int i=1;i<10;i++) {
					if ((ev.getInventory().getItem(i)!=null &&
							ev.getInventory().getItem(i).getType()!=Material.AIR &&
							(ev.getInventory().getItem(i).getType()==Material.ENDER_CHEST)) && !CustomItem.isVacuumCube(ev.getInventory().getItem(i))) {
						ItemMeta inventory_itemMeta1=ev.getInventory().getItem(i).getItemMeta();
						if (inventory_itemMeta1.hasLore() && inventory_itemMeta1.getLore().size()==4) {
				    		String loreitem = inventory_itemMeta1.getLore().get(3);
				    		if (loreitem!=null && loreitem.contains(ChatColor.DARK_PURPLE+"ID#")) {
				    	    	//log("This is an Item Cube. Invalidate recipe.",4);
				    			//Now set the result to this item cube!
				    			newitem = ev.getInventory().getItem(i).clone();
				    			newitem.setAmount(2);
					    		itemcount++;
					    		TwosideKeeper.log("New Item found.", 2);
				    		}
						}

					}
					 else 
					if (ev.getInventory().getItem(i)!=null &&
						ev.getInventory().getItem(i).getType()==Material.NETHER_STAR) {
						netherstar = ev.getInventory().getItem(i).clone();
		        		itemcount++;
			    		TwosideKeeper.log("Nether star found.", 2);
					}
				}
				if (itemcount==2 && newitem!=null &&
						netherstar!=null &&
						newitem.hasItemMeta() &&
						newitem.getItemMeta().hasLore()) {
					//This is the correct recipe. Touch the result.
		    		TwosideKeeper.log("Recipe formed..", 2);
			    	ev.getInventory().setResult(newitem);
				} else {
					ev.getInventory().setResult(new ItemStack(Material.AIR));
				}
			}
		}
	}
	
	public static CustomPotion DefineHardeningVial() {
		ItemStack HARDENING_VIAL = new ItemStack(Material.POTION);
		List<PotionEffect> effects = new ArrayList<PotionEffect>();
		effects.add(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,20*60*15,0));
		List<String> lore = new ArrayList<String>();
		lore.add("A fantastic potion, it comes straight");
		lore.add("from the elixir of the gods.");
		PotionMeta pm = (PotionMeta)HARDENING_VIAL.getItemMeta();
		pm.setLore(lore);
		pm.setDisplayName(ChatColor.GREEN+"Hardening Vial");
		pm.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		HARDENING_VIAL.setItemMeta(pm);
		return new CustomPotion(HARDENING_VIAL,effects,6,9);
	}

	public static CustomPotion DefineLifeVial() {
		ItemStack LIFE_VIAL = new ItemStack(Material.POTION);
		List<PotionEffect> effects = new ArrayList<PotionEffect>();
		effects.add(new PotionEffect(PotionEffectType.ABSORPTION,20*60*15,0));
		List<String> lore = new ArrayList<String>();
		lore.add("A fantastic potion, it comes straight");
		lore.add("from the elixir of the gods.");
		PotionMeta pm = (PotionMeta)LIFE_VIAL.getItemMeta();
		pm.setLore(lore);
		pm.setDisplayName(ChatColor.GREEN+"Life Vial");
		pm.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		LIFE_VIAL.setItemMeta(pm);
		return new CustomPotion(LIFE_VIAL,effects,10,20);
	}

	public static CustomPotion DefineStrengtheningVial() {
		ItemStack STRENGTHENING_VIAL = new ItemStack(Material.POTION);
		List<PotionEffect> effects = new ArrayList<PotionEffect>();
		effects.add(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,20*60*15,0));
		List<String> lore = new ArrayList<String>();
		lore.add("A fantastic potion, it comes straight");
		lore.add("from the elixir of the gods.");
		PotionMeta pm = (PotionMeta)STRENGTHENING_VIAL.getItemMeta();
		pm.setLore(lore);
		pm.setDisplayName(ChatColor.GREEN+"Strengthing Vial");
		pm.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		STRENGTHENING_VIAL.setItemMeta(pm);
		return new CustomPotion(STRENGTHENING_VIAL,effects,20,40);
		/*//LEGACY CODE
		ItemStack STRENGTHENING_VIAL = new ItemStack(Material.POTION);
		PotionMeta pm = (PotionMeta)STRENGTHENING_VIAL.getItemMeta();
		int val=(int)(Math.random()*20+20);
		pm.addCustomEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,20*60*15,val+1), true);
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.GRAY+"Strength "+WorldShop.toRomanNumeral(val)+" ("+WorldShop.toReadableDuration(20*60*15)+")");
		lore.add("");
		lore.add("A fantastic potion, it comes straight");
		lore.add("from the elixir of the gods.");
		pm.setLore(lore);
		pm.setDisplayName("Strengthing Vial");
		pm.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		STRENGTHENING_VIAL.setItemMeta(pm);
		return new CustomItem(STRENGTHENING_VIAL);*/
	}

	public static CustomItem DefineUpgradeShard() {
		ItemStack UPGRADE_SHARD = new ItemStack(Material.PRISMARINE_SHARD);
		ItemMeta meta = UPGRADE_SHARD.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN+"Upgrade Shard");
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		List<String> UPGRADE_SHARD_lore = new ArrayList<String>();
		UPGRADE_SHARD_lore.add("An eerie glow radiates from");
		UPGRADE_SHARD_lore.add("this item. It seems to possess");
		UPGRADE_SHARD_lore.add("some other-worldly powers.");
		meta.setLore(UPGRADE_SHARD_lore);
		UPGRADE_SHARD.setItemMeta(meta);
		UPGRADE_SHARD.addUnsafeEnchantment(Enchantment.LUCK, 1);
		return new CustomItem(UPGRADE_SHARD);
	}

	public static CustomItem DefineHuntersCompass() {
		ItemStack temp = new ItemStack(Material.COMPASS);
		temp.addUnsafeEnchantment(Enchantment.LUCK, 1);
		ItemMeta m = temp.getItemMeta();
		m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		m.setDisplayName(ChatColor.RED+"Hunter's Compass");
		List<String> lore = new ArrayList<String>();
		lore.add("A compass for the true hunter.");
		lore.add("Legends tell of hunters that have");
		lore.add("come back with great treasures and");
		lore.add("much wealth from following the.");
		lore.add("directions of the guided arrow.");
		lore.add("");
		lore.add("You may need to calibrate it by");
		lore.add("right-clicking with it first.");
		lore.add("");
		lore.add("The compass appears to be slightly");
		lore.add("unstable...");
		m.setLore(lore);
		temp.setItemMeta(m);
		temp.addUnsafeEnchantment(Enchantment.LUCK, 1);
		return new CustomItem(temp);
	}
}
