package sig.plugin.TwosideKeeper.HelperStructures.Common;

import org.bukkit.inventory.ItemStack;

//An Itemstack not bound by the limitation of 128 or more items in a stack.
public class ItemContainer {
	ItemStack item;
	int amt;
	
	public ItemContainer(ItemStack item) {
		this.item=item.clone();
		this.amt=this.item.getAmount();
		this.item.setAmount(1);
	}
	
	public void setAmount(int amt) {
		this.amt = amt;
	}
	
	public int getAmount() {
		return this.amt;
	}
	
	public ItemStack getItem() {
		return this.item;
	}
}
