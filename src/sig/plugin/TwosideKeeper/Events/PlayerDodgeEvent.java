package sig.plugin.TwosideKeeper.Events;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class PlayerDodgeEvent extends Event implements Cancellable{
	private Player p;
	private Entity damager;
	private String reason;
	private int flags;
    private boolean cancelled;
	private static final HandlerList handlers = new HandlerList();
	
	public PlayerDodgeEvent(Player p, Entity damager, String reason, int flags) {
		this.p=p;
		this.damager=damager;
		this.reason=reason;
		this.flags=flags;
	}

	public Player getPlayer() {
		return p;
	}

	public Entity getDamager() {
		return damager;
	}

	public String getReason() {
		return reason;
	}

	public int getFlags() {
		return flags;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
	    return handlers;
	}

	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled=cancelled;
	}

}
