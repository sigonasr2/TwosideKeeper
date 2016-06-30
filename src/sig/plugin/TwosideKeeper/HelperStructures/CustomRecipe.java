package sig.plugin.TwosideKeeper.HelperStructures;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import sig.plugin.TwosideKeeper.TwosideKeeper;

public enum CustomRecipe {
	ENDER_ITEM_CUBE_DUPLICATE;
	
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
					if (ev.getInventory().getItem(i)!=null &&
							ev.getInventory().getItem(i).getType()!=Material.AIR &&
							(ev.getInventory().getItem(i).getType()==Material.ENDER_CHEST)) {
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
}
