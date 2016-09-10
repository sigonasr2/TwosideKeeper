package sig.plugin.TwosideKeeper.Boss;

import org.bukkit.entity.Monster;

import sig.plugin.TwosideKeeper.EliteMonster;

public class MegaWither extends EliteMonster{

	public MegaWither(Monster m) {
		super(m);
	}
	
	public void runTick() {
		increaseBarTextScroll();
		regenerateHealth();
		resetToSpawn();
		ignoreAllOtherTargets();
	}

}
