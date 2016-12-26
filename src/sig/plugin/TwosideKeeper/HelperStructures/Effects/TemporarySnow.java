package sig.plugin.TwosideKeeper.HelperStructures.Effects;

import org.bukkit.block.Block;

public class TemporarySnow {
	Block b;
	int lifetime;
	public TemporarySnow(Block b,int lifetime) {
		this.b=b;
		this.lifetime=lifetime;
	}
}
