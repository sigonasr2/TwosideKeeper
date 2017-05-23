package sig.plugin.TwosideKeeper.HelperStructures.Effects;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.Classes.ColoredParticle;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.Classes.MixedDamage;

public class DarkSlash extends WindSlash{
	
	final static int EFFECT_DENSITY = 10;
	final static Particle EFFECT_PARTICLE = Particle.ENCHANTMENT_TABLE;
	final static float SPEED_MULT = 2.5f;

	public DarkSlash(Location loc, LivingEntity l, MixedDamage dmg, int tick_duration) {
		super(loc, l, dmg, tick_duration);
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
	
	protected void createParticles() {
		//loc.getWorld().spawnParticle(Particle.END_ROD, loc.clone().add(0,-SLASH_SIZE/2,0), 4);
		for (int i=0;i<EFFECT_DENSITY;i++) {
			Location randloc = loc.clone();
			loc.getWorld().spawnParticle(EFFECT_PARTICLE, randloc.add(randloc.getDirection().setY(randloc.getDirection().getY()*2).multiply(Math.random())).clone().add(0,-SLASH_SIZE/2,0), 1);
		}
		//loc.getWorld().playEffect(loc.clone().add(0,-SLASH_SIZE/2,0), Effect.DRAGON_BREATH, 4);
		//loc.getWorld().playEffect(loc.clone().add(0,-SLASH_SIZE/2,0), Effect.COLOURED_DUST, 20);

		Location baseloc = loc.clone();
		
		final int density=100;
		final int height=30;
		
		for (int j=0;j<density;j++) {
			for (int i=0;i<height;i++) {
					ColoredParticle.RED_DUST.send(baseloc.clone().add(0,-SLASH_SIZE/2,0).add(0,(2d/height)*i,0)
							, 20, 0, 0, 255);
			}
			baseloc.add(baseloc.getDirection().multiply(SPEED_MULT/density));
		}
	}

	private void damageNearbyTargets() {
		//GenericFunctions.DealDamageToNearbyMobs(loc, dmg, SLASH_SIZE, false, 0, sourcep, sourcep.getEquipment().getItemInMainHand(), false, "Dark Slash");
		GenericFunctions.DealDamageToNearbyPlayers(loc, dmg.getDmgComponent(), SLASH_SIZE, false, true, 0, l, "Dark Slash", false, false);
		if (dmg.getTruePctDmgComponent()>0) {GenericFunctions.DealDamageToNearbyPlayers(loc, dmg.getTruePctDmgComponent(), SLASH_SIZE, false, true, 0, l, "Dark Slash", false, true);}
		if (dmg.getTrueDmgComponent()>0) {GenericFunctions.DealDamageToNearbyPlayers(loc, dmg.getTrueDmgComponent(), SLASH_SIZE, false, true, 0, l, "Dark Slash", true, false);}
	}
}
