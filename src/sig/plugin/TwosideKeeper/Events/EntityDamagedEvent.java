package sig.plugin.TwosideKeeper.Events;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EntityDamagedEvent extends Event implements Cancellable{
	private static final HandlerList handlers = new HandlerList();
	private LivingEntity ent;
	private Entity damager;
	private double damage;
	private String reason;
	
	public LivingEntity getEntity() {
		return ent;
	}

	public Entity getDamager() {
		return damager;
	}

	public void setDamager(Entity damager) {
		this.damager = damager;
	}

	public double getDamage() {
		return damage;
	}

	public void setDamage(double damage) {
		this.damage = damage;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public int getFlags() {
		return flags;
	}

	public void setFlags(int flags) {
		this.flags = flags;
	}

	private int flags;
    private boolean cancelled=false;
    
    /**
     * Called anytime an Entity is damaged.
     * <br><br>
     * <b>This event is cancellable.</b> Cancelling the event prevents the damage from being applied to the damaged entity and all on-hit effects will not apply.
     * @param ent The entity being damaged.
     * @param damager The entity that caused the damage, can be null.
     * @param damage The amount of actual damage taken, after all calculations are applied.
     * @param reason The reason the damage was taken.
     * @param flags The flags set by this event.
     */
    public EntityDamagedEvent(LivingEntity ent, Entity damager, double damage, String reason, int flags) {
    	this.ent=ent;
    	this.damager=damager;
    	this.damage=damage;
    	this.reason=reason;
    	this.flags=flags;
    }

	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled=cancelled;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
	    return handlers;
	}
	
}
