package sig.plugin.TwosideKeeper.Monster;

import org.bukkit.entity.LivingEntity;
import org.inventivetalent.glow.GlowAPI;

import sig.plugin.TwosideKeeper.CustomMonster;

public class ExplosiveMite extends CustomMonster{

	public ExplosiveMite(LivingEntity m) {
		super(m);
	}

	public GlowAPI.Color getGlowColor() {
		return GlowAPI.Color.RED;
	}
}
