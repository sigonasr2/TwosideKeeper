package sig.plugin.TwosideKeeper.HelperStructures.Effects;

import org.bukkit.Location;
import org.bukkit.Particle;

public class TemporaryBlockNode {
	Location l;
	double range;
	int duration;
	String specialKey;
	Particle effect;
	int particleDensity; //Number of particles per second.
	
	public TemporaryBlockNode(Location l, double range, int duration, String key) {
		this.l=l;
		this.range=range;
		this.specialKey=key;
		this.duration=duration;
		this.effect=null;
		this.particleDensity=0;
	}	
	
	public TemporaryBlockNode(Location l, double range, int duration, String key, Particle effect, int density) {
		this.l=l;
		this.range=range;
		this.specialKey=key;
		this.duration=duration;
		this.effect=effect;
		this.particleDensity=density;
	}
	
	public boolean runTick() {
		if (duration--<0) {
			return false;
		} else {
			return true;
		}
	}
}
