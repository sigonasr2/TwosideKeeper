package sig.plugin.TwosideKeeper;

import java.io.File;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.inventivetalent.glow.GlowAPI.Color;

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
	
	public boolean isImmuneToSuppression() {
		return false;
	}
	
	public void cleanup() {
		
	}

	public void entityCleanup() {
		
	}
	
	public void runChannelCastEvent(EntityChannelCastEvent ev) {
		
	}
	
	public void runProjectileLaunchEvent(ProjectileLaunchEvent ev) {
		
	}
	
	public void onDeathEvent() {
		
	}

	public Color getGlowColor() {
		return null;
	}

	public boolean isInIframe() {
		return false;
	}

	public void bloodPoolSpawnedEvent(LivingEntity target) {
	}
	
	public void AnnounceDPSBreakdown() {
	}
	
	/**
	 * 0.0 means cannot be moved, 1.0 means normal knockback.
	 */
	public double getKnockbackMult() {
		return 1.0;
	}
}
