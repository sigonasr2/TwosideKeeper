package sig.plugin.TwosideKeeper.Monster;

import org.bukkit.entity.LivingEntity;

import sig.plugin.TwosideKeeper.CustomMonster;
import sig.plugin.TwosideKeeper.TwosideKeeper;

public class Blaze extends CustomMonster{
	
	private long lastFireball = 0;

	public Blaze(LivingEntity m) {
		super(m);
		this.lastFireball=TwosideKeeper.getServerTickTime();
	}
	
	public long getLastFireball() {
		return lastFireball;
	}

	public void resetLastFireball() {
		this.lastFireball = TwosideKeeper.getServerTickTime();
	}

	public void runTick() {
		
	}
	
}
