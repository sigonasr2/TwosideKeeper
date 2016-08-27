package sig.plugin.TwosideKeeper.Drops;

import org.bukkit.inventory.ItemStack;

import aPlugin.Drop;

public class GenericDrop extends Drop{
	
	ItemStack item;
	short data;

	public GenericDrop(int amount, int weight, String description, ItemStack item, short data) {
		super(amount, weight, description);
		this.item=item;
		this.data=data;
	}

	@Override
	public ItemStack getItemStack() {
		this.item.setDurability(data);
		return this.item;
	}
	
}
