package sig.plugin.TwosideKeeper.HelperStructures.Effects;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import sig.plugin.TwosideKeeper.Buff;
import sig.plugin.TwosideKeeper.CustomDamage;
import sig.plugin.TwosideKeeper.TwosideKeeper;

public class TemporaryBlockNode {
	Location l;
	double range;
	int duration;
	String specialKey;
	Particle effect;
	int particleDensity; //Number of particles per second.
	long lastAppliedEffect=TwosideKeeper.getServerTickTime();
	
	public TemporaryBlockNode(Location l, double range, int duration, String key) {
		this.l=l;
		this.range=range;
		this.specialKey=key;
		this.duration=duration;
		this.effect=null;
		this.particleDensity=0;
		TwosideKeeper.blocknodes.add(this);
	}	
	
	public TemporaryBlockNode(Location l, double range, int duration, String key, Particle effect, int density) {
		this.l=l;
		this.range=range;
		this.specialKey=key;
		this.duration=duration;
		this.effect=effect;
		this.particleDensity=density;
		TwosideKeeper.blocknodes.add(this);
	}
	
	public boolean runTick() {
		playParticleEffects();
		affectEntitiesBasedOnKey();
		duration-=5;
		if (duration<0) {
			return false;
		} else {
			return true;
		}
	}

	private void affectEntitiesBasedOnKey() {
		switch (specialKey) {
			case "TESTNODE":{
				for (Entity e : getNonPlayersOnNode()) {
					if (e instanceof LivingEntity) {
						CustomDamage.ApplyDamage(1, null, (LivingEntity)e, null, "Test Damage");
					}
				}
			}break;
			case "FIREPOOL":{
				if (lastAppliedEffect+20<TwosideKeeper.getServerTickTime()) {
					lastAppliedEffect = TwosideKeeper.getServerTickTime();
					for (Entity e : getNonPlayersOnNode()) {
						if (e instanceof LivingEntity) {
							Buff.addBuff((LivingEntity)e, "BURN", new Buff("Burn",100,1,Color.ORANGE,ChatColor.GOLD+"❂",false),true);
						}
					}
				}
			}break;
		}
	}

	private void playParticleEffects() {
		if (effect!=null) {
			for (int i=0;i<particleDensity/4;i++) {
				SpawnParticleInRandomLocation();
			}
			if (Math.random()<=(particleDensity%20)*0.05) {
				SpawnParticleInRandomLocation();
			}
		}
	}

	private void SpawnParticleInRandomLocation() {
		l.getWorld().spawnParticle(effect, new Location(l.getWorld(),l.getX()+Math.random()*range-(Math.random()*(range*2))
				,l.getY()+Math.random()*range-(Math.random()*(range*2))
				,l.getZ()+Math.random()*range-(Math.random()*(range*2))), 2);
	}
	
	public Entity[] getEntitiesOnNode() {
		Collection<Entity> ents = l.getWorld().getNearbyEntities(l, range, range, range); 
		return ents.toArray(new Entity[ents.size()]);
	}
	
	public List<Entity> getPlayersOnNode() {
		long time = System.nanoTime();
		Collection<Entity> ents = l.getWorld().getNearbyEntities(l, range, range, range);
		List<Entity> list = new ArrayList<Entity>();
		for (Entity e : ents) {
			if (e instanceof Player) {
				list.add(e);
			}
		}
		TwosideKeeper.log("Calculation time via nearby entities: "+(System.nanoTime()-time), TwosideKeeper.TIMINGS_DEBUG);
		return list;
	}
	
	public List<Entity> getNonPlayersOnNode() {
		long time = System.nanoTime();
		Collection<Entity> ents = l.getWorld().getNearbyEntities(l, range, range, range);
		List<Entity> list = new ArrayList<Entity>();
		for (Entity e : ents) {
			if (!(e instanceof Player)) {
				list.add(e);
			}
		}
		TwosideKeeper.log("Calculation time via nearby entities: "+(System.nanoTime()-time), TwosideKeeper.TIMINGS_DEBUG);
		return list;
	}
	@Deprecated
	public List<Entity> getPlayersOnNodeViaDistanceSearch() {
		long time = System.nanoTime();
		List<Entity> list = new ArrayList<Entity>();
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (p.getLocation().distance(l)<=range) {
				list.add(p);
			}
		}
		TwosideKeeper.log("Calculation time via distance search: "+(System.nanoTime()-time), TwosideKeeper.TIMINGS_DEBUG);
		return list;
	}
}
