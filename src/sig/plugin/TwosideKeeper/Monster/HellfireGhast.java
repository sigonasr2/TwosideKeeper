package sig.plugin.TwosideKeeper.Monster;

import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.metadata.FixedMetadataValue;

import sig.plugin.TwosideKeeper.CustomMonster;
import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.MovementUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.SoundUtils;

public class HellfireGhast extends CustomMonster{
	
	long lastFireball = 0;
	Player target = null;
	
	public HellfireGhast(LivingEntity m) {
		super(m);
	}
	
	public long getLastFireball() {
		return lastFireball;
	}
	
	public Player getTarget() {
		if (target!=null && !target.isDead() &&
				target.isValid()) {
			return target;
		} else {
			return null;
		}
	}
	
	public void setTarget(Player p) {
		this.target=p;
	}
	
	public void recordLastFireball() {
		lastFireball = TwosideKeeper.getServerTickTime();
	}
	
	public void runTick() {
		double pcthealth = m.getHealth()/m.getMaxHealth();
		if (pcthealth<=0.2 && target!=null) {
			//Begin charging towards player.
			m.setAI(false);
			m.setVelocity(MovementUtils.moveTowardsLocation(m.getLocation(), target.getLocation(), 2));
			if (m.getLocation().distanceSquared(target.getLocation())<49) {
				explode();
			}
		} else {
			m.setAI(true);
		}
	}

	private void explode() {
		aPlugin.API.sendSoundlessExplosion(m.getLocation(), 4);
		m.getWorld().createExplosion(m.getLocation().getBlockX(), m.getLocation().getBlockY(), m.getLocation().getBlockZ(), 6.0f, false, false);
		GenericFunctions.DealExplosionDamageToEntities(m.getLocation(), 1600, 5, m);
		GenericFunctions.RandomlyCreateFire(m.getLocation(), 5);
		m.remove();
	}
	
}
