package sig.plugin.TwosideKeeper.HelperStructures.Utils;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.material.Chest;

public class BlockUtils {
	@SuppressWarnings("deprecation")
	public static boolean LocationInFrontOfBlockIsFree(Block b) {
		if (b.getType()==Material.CHEST ||
				b.getType()==Material.TRAPPED_CHEST ||
				b.getType()==Material.ENDER_CHEST) {
			Chest c = new Chest(0,b.getData());
			BlockFace bf = c.getFacing();
			//Check the block relative to here.
			Block b2 = b.getRelative(bf);
			return (b2.getType()==Material.AIR);
		} else {
			return false;
		}
	}

	@SuppressWarnings("deprecation")
	public static BlockFace GetBlockFacingDirection(Block b) {
		if (b.getType()==Material.CHEST ||
				b.getType()==Material.TRAPPED_CHEST ||
				b.getType()==Material.ENDER_CHEST) {
			Chest c = new Chest(0,b.getData());
			BlockFace bf = c.getFacing();
			return bf;
		} else {
			return null;
		}
	}
	
	@SuppressWarnings("deprecation")
	public static Block GetBlockInFrontOfBlock(Block b) {
		if (b.getType()==Material.CHEST ||
				b.getType()==Material.TRAPPED_CHEST ||
				b.getType()==Material.ENDER_CHEST) {
			Chest c = new Chest(0,b.getData());
			BlockFace bf = c.getFacing();
			//Check the block relative to here.
			return b.getRelative(bf);
		} else {
			return null;
		}
	}
}
