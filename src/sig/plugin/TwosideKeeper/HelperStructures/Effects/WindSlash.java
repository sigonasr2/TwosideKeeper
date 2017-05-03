package sig.plugin.TwosideKeeper.HelperStructures.Effects;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import sig.plugin.TwosideKeeper.CustomDamage;
import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.BlockUtils;

public class WindSlash {
	Location loc;
	Player sourcep;
	double dmg;
	long lasteffect;
	long death_time;
	final static int EFFECT_DENSITY = 20;
	final static int EFFECT_FREQUENCY = 4;
	final static int SLASH_SIZE = 3; //Radius.
	final static Particle EFFECT_PARTICLE = Particle.FIREWORKS_SPARK;
	final static float SPEED_MULT = 3.5f;
	
	public WindSlash(Location loc, Player p, double dmg, int tick_duration) {
		this.loc=loc.clone();
		this.sourcep=p;
		this.dmg=dmg;
		this.death_time = TwosideKeeper.getServerTickTime()+tick_duration;
		this.lasteffect=TwosideKeeper.getServerTickTime();
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
		GenericFunctions.DealDamageToNearbyMobs(loc, dmg, SLASH_SIZE, false, 0, sourcep, sourcep.getEquipment().getItemInMainHand(), false, "Wind Slash");
	}

	private boolean moveWindSlash() {
		Location origloc = loc.clone();
		Vector move = origloc.getDirection().setY(origloc.getDirection().getY()/1.4).multiply(SPEED_MULT);
		float dist = SPEED_MULT;
		loc.add(move);
		while (dist-->0) {
			if (!BlockUtils.isPassThrough(origloc.add(origloc.getDirection()))) {
				return false;
			}
		}
		return true;
		//TwosideKeeper.log("New Location: "+loc, 0);
	}

	private void createParticles() {
		loc.getWorld().spawnParticle(Particle.SWEEP_ATTACK, loc.clone().add(0,-SLASH_SIZE/2,0), 2);
		for (int i=0;i<EFFECT_DENSITY;i++) {
			Location randloc = loc.clone();
			loc.getWorld().spawnParticle(EFFECT_PARTICLE, randloc.add(randloc.getDirection().setY(randloc.getDirection().getY()/1.4).multiply(Math.random())).clone().add(0,-SLASH_SIZE/2,0), 1);
		}
	}
}
