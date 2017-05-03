package sig.plugin.TwosideKeeper.HelperStructures.Utils;

import org.bukkit.Material;

import org.bukkit.block.Block;

public class APIUtils {
	public static boolean isExplosionProof(Block b) {
		if (b!=null) {
			return (aPlugin.API.isExplosionProof(b) || BlockUtils.isSign(b));
		} else {
			return false;
		}
	}
}
