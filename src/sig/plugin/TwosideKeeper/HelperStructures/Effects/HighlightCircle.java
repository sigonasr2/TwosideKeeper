package sig.plugin.TwosideKeeper.HelperStructures.Effects;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.Classes.ParticleEffect;

public class HighlightCircle {
	final Particle PARTICLE = Particle.BLOCK_DUST;
	final int PARTICLE_DENSITY = 16;
	final int ANGLE_OFFSET = 10;
	Location center = null;
	double r = 0;
	double h = 0;
	long expireTime = 0;
	
	/**
	 * Creates a cylinder zone that will be outline by particles.
	 * @param l The centered location of the zone.
	 * @param radius The radius of the zone.
	 * @param height The height of the zone.
	 * @param duration The amount of time in ticks the zone will exist.
	 */
	public HighlightCircle(Location l, double radius, double height, int duration) {
		this.center=l.clone();
		this.r=radius;
		this.h=height;
		this.expireTime=TwosideKeeper.getServerTickTime()+duration;
		TwosideKeeper.circles.add(this);
	}

	public Location getCenter() {
		return center;
	}

	public double getRadius() {
		return r;
	}

	public double getHeight() {
		return h;
	}
	
	public boolean hasExpired() {
		return expireTime<TwosideKeeper.getServerTickTime();
	}
	
	public boolean runTick() {
		for (int j=0;j<h;j++) {
			for (int i=0;i<PARTICLE_DENSITY;i++) {
				double angle = ((1d/PARTICLE_DENSITY)*360d)*i+(ANGLE_OFFSET*j);
				double xamt = Math.sin(Math.toRadians(angle))*r;
				double zamt = Math.cos(Math.toRadians(angle))*r;
				Location particleloc = center.clone().add(new Location(center.getWorld(),
						xamt,
						j,
						zamt)
				);
				//TwosideKeeper.log("Location: "+particleloc, 0);
				ParticleEffect.CRIT.display(new Vector(0,0,0), 0f, particleloc, 50);
			}
		}
		return !hasExpired();
	}
}
