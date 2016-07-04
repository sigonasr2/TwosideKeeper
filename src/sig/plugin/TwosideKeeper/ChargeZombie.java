package sig.plugin.TwosideKeeper;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Monster;

import aPlugin.BlockUtils;
import aPlugin.Utils;
import net.minecraft.server.v1_9_R1.BlockPosition;
import sig.plugin.TwosideKeeper.HelperStructures.BlockToughness;

public class ChargeZombie {
	Monster m;
	
	public ChargeZombie(Monster m) {
		this.m=m;
	}
	
	public Monster GetZombie() {
		return m;
	}
	public boolean isAlive() {
		return !m.isDead();
	}
	public boolean hasTarget() {
		return (m.getTarget()!=null)?true:false;
	}
	
	public void BreakBlocksAroundArea(int radius) {
		for (int x=-radius;x<radius+1;x++) {
			for (int y=-radius;y<radius+2;y++) {
				for (int z=-radius;z<radius+1;z++) {
					if (!BlockUtils.isExplosionProof(m.getLocation().add(x,y,z).getBlock().getType()) ||
							m.getLocation().add(x,y,z).getBlock().getType()==Material.OBSIDIAN) {
						if (!(y==0 && m.getTarget().getLocation().getY()>m.getLocation().getY()) || !m.getLocation().add(x,y,z).getBlock().getType().isSolid()) { //Player is higher than zombie. Don't break blocks in front of it. Climb up them. Unless it's lava.
							if (!(y<0 && (m.getTarget().getLocation().getY()>m.getLocation().getY()-1))) { //Player is lower than zombie. Break blocks below it to get to the player.
								boolean brokeliquid = false;
								//Break it.
								if (ChanceToBreak(m.getLocation().add(x,y,z).getBlock())) {
									if (m.getLocation().add(x,y,z).getBlock().getType()==Material.WATER ||
											m.getLocation().add(x,y,z).getBlock().getType()==Material.STATIONARY_WATER ||
											m.getLocation().add(x,y,z).getBlock().getType()==Material.LAVA ||
											m.getLocation().add(x,y,z).getBlock().getType()==Material.STATIONARY_LAVA) {
											brokeliquid=true;
											if (m.getLocation().add(x,y,z).getBlock().getType()==Material.STATIONARY_LAVA) {
												m.getLocation().add(x,y,z).getBlock().setType(Material.OBSIDIAN);
												m.getLocation().getWorld().playSound(m.getLocation().add(x,y,z),Sound.BLOCK_FIRE_EXTINGUISH, 1f, 1f);
											}
									}
									if (!brokeliquid)	{
										m.getLocation().getWorld().playSound(m.getLocation().add(x,y,z),Sound.BLOCK_STONE_BREAK, 1.0f, 1.0f);
									}
									m.getLocation().add(x,y,z).getBlock().breakNaturally();
									Utils.sendBlockBreakAnimation(null, new BlockPosition(m.getLocation().add(x,y,z).getBlockX(),m.getLocation().add(x,y,z).getBlockY(),m.getLocation().add(x,y,z).getBlockZ()), -1, Utils.seedRandomID(m.getLocation().add(x,y,z).getBlock()));
								} else {
									Utils.sendBlockBreakAnimation(null, new BlockPosition(m.getLocation().add(x,y,z).getBlockX(),m.getLocation().add(x,y,z).getBlockY(),m.getLocation().add(x,y,z).getBlockZ()), 4, Utils.seedRandomID(m.getLocation().add(x,y,z).getBlock()));
								}
							}
						}
					}
				}
			}
		}
	}
	
	public boolean ChanceToBreak(Block b) {
		int blocktoughness = 0;
		switch (b.getType()) {
			case OBSIDIAN:{
				blocktoughness=100;
			}break;
			case ENDER_CHEST:{
				blocktoughness=44;
			}break;
			case ANVIL:{
				blocktoughness=10;
			}break;
			case COAL_BLOCK:{
				blocktoughness=10;
			}break;
			case DIAMOND_BLOCK:{
				blocktoughness=10;
			}break;
			case EMERALD_BLOCK:{
				blocktoughness=10;
			}break;
			case IRON_BLOCK:{
				blocktoughness=10;
			}break;
			case REDSTONE_BLOCK:{
				blocktoughness=10;
			}break;
			case ENCHANTMENT_TABLE:{
				blocktoughness=10;
			}break;
			case IRON_FENCE:{
				blocktoughness=10;
			}break;
			case IRON_DOOR:{
				blocktoughness=10;
			}break;
			case IRON_TRAPDOOR:{
				blocktoughness=10;
			}break;
			case MOB_SPAWNER:{
				blocktoughness=10;
			}break;
			case WEB:{
				blocktoughness=10;
			}break;
			case DISPENSER:{
				blocktoughness=10;
			}break;
			case DROPPER:{
				blocktoughness=10;
			}break;
			case FURNACE:{
				blocktoughness=10;
			}break;
			case BEACON:{
				blocktoughness=6;
			}break;
			case GOLD_BLOCK:{
				blocktoughness=6;
			}break;
			case COAL_ORE:{
				blocktoughness=6;
			}break;
			case DIAMOND_ORE:{
				blocktoughness=6;
			}break;
			case EMERALD_ORE:{
				blocktoughness=6;
			}break;
			case ENDER_STONE:{
				blocktoughness=6;
			}break;
			case GOLD_ORE:{
				blocktoughness=6;
			}break;
			case HOPPER:{
				blocktoughness=6;
			}break;
			case IRON_ORE:{
				blocktoughness=6;
			}break;
			case LAPIS_BLOCK:{
				blocktoughness=6;
			}break;
			case LAPIS_ORE:{
				blocktoughness=6;
			}break;
			case QUARTZ_ORE:{
				blocktoughness=6;
			}break;
			case REDSTONE_ORE:{
				blocktoughness=6;
			}break;
			case GLOWING_REDSTONE_ORE:{
				blocktoughness=6;
			}break;
			case TRAP_DOOR:{
				blocktoughness=6;
			}break;
			case WOODEN_DOOR:{
				blocktoughness=6;
			}break;
			case CHEST:
			case TRAPPED_CHEST:
			case WORKBENCH:{
				blocktoughness=5;
			}break;
			case BRICK_STAIRS:
			case BRICK:
			case CAULDRON:
			case COBBLESTONE:
			case COBBLESTONE_STAIRS:
			case COBBLE_WALL:
			case FENCE:
			case FENCE_GATE:
			case JUKEBOX:
			case MOSSY_COBBLESTONE:
			case NETHER_BRICK:
			case NETHER_FENCE:
			case NETHER_BRICK_STAIRS:
			case STONE_SLAB2:
			case LOG:
			case WOOD:
			case WOOD_STEP:{
				blocktoughness=4;
			}break;
			case STONE:
			case BOOKSHELF:
			case PRISMARINE:
			case STAINED_CLAY:
			case HARD_CLAY:{
				blocktoughness=3;
			}
		}
		
		if (Math.random()*((double)blocktoughness)<0.25) {
			return true;
		} else {
			return false;
		}
	}
}
