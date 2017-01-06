package sig.plugin.TwosideKeeper.Drops;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import aPlugin.Drop;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.ItemUtils;

public class DropRandomFirework extends Drop {

	public DropRandomFirework(int min, int max, int weight) {
		super(min,max,weight,"Holiday Firework");
	}

	@Override
	public ItemStack getItemStack() {
		return ItemUtils.createRandomFirework();
	}

}
