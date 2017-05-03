package sig.plugin.TwosideKeeper.HelperStructures.Utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.material.Chest;

import aPlugin.API.Chests;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;

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
	
	public static void AttemptToPlaceLootChest(Location refloc, int i, int j, int k, Chests chest) {
		int tries=0;
		while (tries<50) {
			Block rand = refloc.getBlock().getRelative((int)(i*((Math.random()*5)+1)), (int)(j*((Math.random()*5)+1)), (int)(k*((Math.random()*5)+1)));
			if (GenericFunctions.isNaturalBlock(rand)) {
				chest.placeChestAt(rand);
				return;
			} else {
				tries++;
			}
		}
	}
	
	/**
	 * Returns if a block can be passed through by regular means (A player can get past it).
	 * Usually used for detecting when abilities stop.
	 */
	public static boolean isPassThrough(Location l) {
		return l.getBlock().getType()==Material.AIR || 
				l.getBlock().isLiquid() ||
				l.getBlock().getType()==Material.STEP ||
				l.getBlock().getType()==Material.WOOD_STEP ||
				l.getBlock().getType()==Material.PURPUR_SLAB ||
				l.getBlock().getType()==Material.STONE_SLAB2;
	}
	
	public static boolean isSign(Block b) {
		return b.getType()==Material.SIGN ||
				b.getType()==Material.WALL_SIGN ||
				b.getType()==Material.SIGN_POST;
	}
}
