package sig.plugin.TwosideKeeper.Drops;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import aPlugin.Drop;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.ItemUtils;

public class DropRandomFirework extends Drop {
	
	int min,max;
	
	public DropRandomFirework(int min, int max, int weight) {
		super(min,max,weight,"Holiday Firework");
		this.min=min;
		this.max=max;
	}

	@Override
	public ItemStack getItemStack() {
		ItemStack randomfirework = ItemUtils.createRandomFirework();
		randomfirework.setAmount((int)(((Math.random()*(max-min))+min)));
		return randomfirework;
	}

}
