package sig.plugin.TwosideKeeper.HelperStructures.Effects;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import sig.plugin.TwosideKeeper.CustomDamage;
import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.BlockUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.SoundUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.Classes.MixedDamage;

public class WindSlash {
	Location loc;
	Player sourcep;
	LivingEntity l;
	MixedDamage dmg;
	long lasteffect;
	long death_time;
	final static int EFFECT_DENSITY = 20;
	final static int EFFECT_FREQUENCY = 4;
	final static int SLASH_SIZE = 3; //Radius.
	final static Particle EFFECT_PARTICLE = Particle.FIREWORKS_SPARK;
	final static float SPEED_MULT = 3.5f;
	
	public WindSlash(Location loc, Player p, double dmg, int tick_duration) {
		this.loc=loc.clone().add(0,p.getEyeHeight(),0);
		this.sourcep=p;
		this.dmg=MixedDamage.v(dmg);
		this.death_time = TwosideKeeper.getServerTickTime()+tick_duration;
		this.lasteffect=TwosideKeeper.getServerTickTime();
		SoundUtils.playGlobalSound(loc,Sound.BLOCK_PORTAL_TRIGGER, 0.2f, 2.0f);
	}
	
	public WindSlash(Location loc, LivingEntity l, MixedDamage dmg, int tick_duration) {
		this.loc=loc.clone().add(0,l.getEyeHeight(),0);
		this.l=l;
		this.dmg=dmg;
		this.death_time = TwosideKeeper.getServerTickTime()+tick_duration;
		this.lasteffect=TwosideKeeper.getServerTickTime();
		SoundUtils.playGlobalSound(loc,Sound.BLOCK_PORTAL_TRIGGER, 0.2f, 2.0f);
	}
	
	public boolean runTick() {
		if (!moveWindSlash()) {
			return false;
		}
		createParticles();
		damageNearbyTargets();
		if (TwosideKeeper.getServerTickTime()>death_time) {
			return false;
		}
		return true;
	}

	private void damageNearbyTargets() {
		GenericFunctions.DealDamageToNearbyMobs(loc, dmg.getDmgComponent(), SLASH_SIZE, false, 0, sourcep, sourcep.getEquipment().getItemInMainHand(), false, "Wind Slash");
	}

	protected boolean moveWindSlash() {
		Location origloc = loc.clone();
		Vector move = origloc.getDirection().setY(origloc.getDirection().getY()/1.4).multiply(SPEED_MULT);
		float dist = SPEED_MULT;
		loc.add(move);
		SoundUtils.playGlobalSound(loc, Sound.ENTITY_PLAYER_ATTACK_NODAMAGE, 0.4f, 1.0f);
		while (dist-->0) {
			if (!BlockUtils.isPassThrough(origloc.add(origloc.getDirection()))) {
				return false;
			}
		}
		return true;
		//TwosideKeeper.log("New Location: "+loc, 0);
	}

	protected void createParticles() {
		loc.getWorld().spawnParticle(Particle.SWEEP_ATTACK, loc.clone().add(0,-SLASH_SIZE/2,0), 2);
		for (int i=0;i<EFFECT_DENSITY;i++) {
			Location randloc = loc.clone();
			loc.getWorld().spawnParticle(EFFECT_PARTICLE, randloc.add(randloc.getDirection().setY(randloc.getDirection().getY()/1.4).multiply(Math.random())).clone().add(0,-SLASH_SIZE/2,0), 1);
		}
	}
}
