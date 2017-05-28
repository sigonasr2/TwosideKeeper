package sig.plugin.TwosideKeeper.HelperStructures.Effects;

import org.bukkit.Color;
import org.bukkit.Location;

import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.Classes.ColoredParticle;

public class EffectPool {
	double radius = 1;
	Color col = null;
	Location loc = null;
	long expireTime=0;
	final int PARTICLE_DENSITY = 5;
	public EffectPool(Location loc, double radius, int duration, Color col) {
		this.loc=loc.clone();
		this.radius=radius;
		this.col=col;
		this.expireTime=TwosideKeeper.getServerTickTime()+duration;
		TwosideKeeper.effectpools.add(this);
	}
	
	public boolean runTick() {
		int density = (int)Math.pow(PARTICLE_DENSITY, radius);
		for (int i=0;i<density;i++) {
			Location particleloc = loc.clone().add(
					(Math.random()*(radius+1))-radius,
					0.66,
					(Math.random()*(radius+1))-radius
					);
			ColoredParticle.RED_DUST.send(particleloc, 50, col.getRed(), col.getGreen(), col.getBlue());
		}
		return !(expireTime<=TwosideKeeper.getServerTickTime());
	}
}
