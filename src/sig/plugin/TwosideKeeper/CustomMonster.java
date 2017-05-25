package sig.plugin.TwosideKeeper;

import java.io.File;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;

import sig.plugin.TwosideKeeper.Events.EntityChannelCastEvent;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.Classes.MixedDamage;

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
	
	public MixedDamage getBasicAttackDamage() {
		return MixedDamage.v(0,0,0);
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
	
	public static boolean recognizeMonsterConditions(LivingEntity m) {
		return false;
	}
	
	public void runTick() {
		
	}
	
	public void customHitHandler() {
		
	}
	public void customHitHandler(double dmg) {
		
	}
	public void onHitEvent(LivingEntity damager, double damage) {
		
	}
	public void onPlayerSlayEvent(Player p, String reason) {
		
	}
	
	public void cleanup() {
		
	}

	public void entityCleanup() {
		
	}
	
	public void runChannelCastEvent(EntityChannelCastEvent ev) {
		
	}
	
	public void onDeathEvent() {
		
	}
}
