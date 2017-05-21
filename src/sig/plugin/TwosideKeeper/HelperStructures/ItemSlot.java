package sig.plugin.TwosideKeeper.HelperStructures;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import sig.plugin.TwosideKeeper.TwosideKeeper;

public enum ItemSlot {
	MAINHAND,
	OFFHAND;
	
	public ItemStack getItem(Player p) {
		switch (this) {
			case MAINHAND:
				return p.getEquipment().getItemInMainHand();
			case OFFHAND:
				return p.getEquipment().getItemInOffHand();
			default:
				TwosideKeeper.log("WARNING! Could not find proper enum for this item slot! Slot: "+this, 0);
				return p.getEquipment().getItemInMainHand();
		}
	}
	
	public void setItem(Player p, ItemStack item) {
		switch (this) {
		case MAINHAND:
			p.getEquipment().setItemInMainHand(item);
			break;
		case OFFHAND:
			p.getEquipment().setItemInOffHand(item);
			break;
		default:
			TwosideKeeper.log("WARNING! Could not find proper enum for this item slot! Slot: "+this, 0);
			p.getEquipment().setItemInMainHand(item);
		}
	}
}
