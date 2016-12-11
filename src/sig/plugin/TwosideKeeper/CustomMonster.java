package sig.plugin.TwosideKeeper;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;

public class CustomMonster {
	protected LivingEntity m;
	
	public CustomMonster(LivingEntity m) {
		super();
		this.m=m;
	}
	
	public LivingEntity GetMonster() {
		return m;
	}
	
	public boolean isAlive() {
		return !m.isDead();
	}
	
	/*
	public boolean hasTarget() {
		return (m.getTarget()!=null)?true:false;
	}*/
	
	public static CustomMonster getCustomMonster(LivingEntity m) {
		if (TwosideKeeper.custommonsters.containsKey(m.getUniqueId())) {
			return TwosideKeeper.custommonsters.get(m.getUniqueId());
		} else {
			return null;
		}
	}
	
	public void runTick() {
		
	}
}
