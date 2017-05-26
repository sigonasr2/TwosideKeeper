package sig.plugin.TwosideKeeper.Events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import sig.plugin.TwosideKeeper.TwosideKeeper;

public class InventoryUpdateEvent extends Event{
	private Player p;
	private ItemStack item;
	private static final HandlerList handlers = new HandlerList();
	private UpdateReason reason;

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
	    return handlers;
	}
	
	public InventoryUpdateEvent(Player p, ItemStack item, UpdateReason reason) {
		this.p=p;
		this.item=item;
		this.reason=reason;
	}
	
	public Player getPlayer() {
		return p;
	}
	
	public ItemStack getItemStack() {
		return item;
	}
	
	public UpdateReason getReason() {
		return reason;
	}
	
	public static void TriggerUpdateInventoryEvent(Player p, ItemStack item, UpdateReason reason) {
		if (item!=null) {
			InventoryUpdateEvent ev = new InventoryUpdateEvent(p, item, reason);
			Bukkit.getPluginManager().callEvent(ev);
		}
		//TwosideKeeper.log("Triggered because of "+reason, 0);
	}

	public enum UpdateReason {
		INVENTORYUPDATE,
		PICKEDUPITEM,
		DROPPEDITEM
	}
}