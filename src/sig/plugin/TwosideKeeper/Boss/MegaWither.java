package sig.plugin.TwosideKeeper.Boss;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.potion.PotionEffectType;

import sig.plugin.TwosideKeeper.ChargeZombie;
import sig.plugin.TwosideKeeper.EliteMonster;
import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;

public class MegaWither extends EliteMonster{
	static int DROPDOWN_DURATION = 40;
	static int DROPDOWN_COOLDOWN = 20*5;
	long last_dropdowntime=0;

	public MegaWither(Monster m) {
		super(m);
		m.setAI(false);
	}
	
	public void runTick() {
		increaseBarTextScroll();
		regenerateHealth();
		createBossHealthbar();
		resetToSpawn();
		ignoreAllOtherTargets();
		randomlyDropDown();
		destroyNearbyBlocks();
	}
	
	private void destroyNearbyBlocks() {
		if (targetlist.size()>0) {
			ChargeZombie.BreakBlocksAroundArea(m, 2);
		}
	}

	private void randomlyDropDown() {
		if (last_dropdowntime+DROPDOWN_COOLDOWN<TwosideKeeper.getServerTickTime()) {
			last_dropdowntime = TwosideKeeper.getServerTickTime();
			//GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.LEVITATION, 20, -9, m);
		}
		if (last_dropdowntime+60>TwosideKeeper.getServerTickTime()) {
			m.setVelocity(m.getVelocity().setY(-20f));
			//m.teleport(m.getLocation().add(0,-5,0));
		}
	}

	//Triggers when this mob is hit.
	public void runHitEvent(LivingEntity damager, double dmg) {
		super.runHitEvent(damager,dmg);
		m.setAI(true);
	}
	
	protected void resetToSpawn() {
		super.resetToSpawn();
		if (targetlist.size()==0) {
			m.setAI(false);
		}
	}

}
