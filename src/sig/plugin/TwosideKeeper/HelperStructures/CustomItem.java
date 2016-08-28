package sig.plugin.TwosideKeeper.HelperStructures;

import org.bukkit.inventory.ItemStack;

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
	
}
