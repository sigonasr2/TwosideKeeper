package sig.plugin.TwosideKeeper.HelperStructures;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import sig.plugin.TwosideKeeper.TwosideKeeper;

public class VerifyItemWasMovedTask implements Runnable{
	ItemStack checkitem;
	ItemSlot slot;
	Player p;
	
	public VerifyItemWasMovedTask(ItemStack checkitem, Player p, ItemSlot slot) {
		this.checkitem=checkitem.clone();
		this.p=p;
		this.slot=slot;
	}

	@Override
	public void run() {
		if (!slot.getItem(p).isSimilar(checkitem)) {
			TwosideKeeper.log("WARNING! Item "+checkitem+" was not inserted in slot "+slot.name()+" properly! Item in slot atm: "+slot.getItem(p)+". Trying again in 1 tick!", 1);
			//This is bad. Try again on the next tick. Set the item.
			slot.setItem(p, checkitem);
			Bukkit.getScheduler().runTaskLater(TwosideKeeper.plugin, this, 1);
		}
	}

}
