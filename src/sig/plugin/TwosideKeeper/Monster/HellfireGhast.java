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

public class HellfireGhast extends CustomMonster{
	
	long lastFireball = 0;
	
	public HellfireGhast(LivingEntity m) {
		super(m);
	}
	
	public long getLastFireball() {
		return lastFireball;
	}
	
	public void runTick() {
	}
	
}
