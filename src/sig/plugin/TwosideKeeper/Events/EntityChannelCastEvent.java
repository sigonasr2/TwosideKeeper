package sig.plugin.TwosideKeeper.Events;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EntityChannelCastEvent extends Event{
	private LivingEntity l;
	private String abilityName;
	private static final HandlerList handlers = new HandlerList();
	
	public EntityChannelCastEvent(LivingEntity l, String abilityName) {
		this.l=l;
		this.abilityName=abilityName;
	}
	
	public LivingEntity getLivingEntity() {
		return l;
	}
	
	public String getAbilityName() {
		return abilityName;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
	    return handlers;
	}
	
}
