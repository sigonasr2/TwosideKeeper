package sig.plugin.TwosideKeeper.Monster;

import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Snowball;
import org.bukkit.metadata.FixedMetadataValue;

import sig.plugin.TwosideKeeper.CustomMonster;
import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.SoundUtils;

public class HellfireSpider extends CustomMonster{
	
	long lastSpiderBallThrow = 0;
	
	public HellfireSpider(LivingEntity m) {
		super(m);
	}
	
	public long getLastSpiderBallThrow() {
		return lastSpiderBallThrow;
	}
	
	public void runTick() {
		if (Math.random()<=0.24 && lastSpiderBallThrow+(20*4)<TwosideKeeper.getServerTickTime()) {
			//Fire a sticky web.
			Snowball sb = (Snowball)m.getLocation().getWorld().spawnEntity(m.getLocation().add(0,0.3,0), EntityType.SNOWBALL);
			sb.setVelocity(m.getLocation().getDirection().multiply(1.3f));
			sb.setMetadata("SPIDERBALL", new FixedMetadataValue(TwosideKeeper.plugin,true));
			sb.setShooter(m);
			SoundUtils.playGlobalSound(sb.getLocation(), Sound.ENTITY_SNOWBALL_THROW, 1.0f, 1.0f);
			lastSpiderBallThrow = TwosideKeeper.getServerTickTime();
		}
	}
	
}
