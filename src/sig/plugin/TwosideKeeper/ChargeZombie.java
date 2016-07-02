package sig.plugin.TwosideKeeper;

import org.bukkit.block.Block;
import org.bukkit.entity.Monster;

import aPlugin.BlockUtils;
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
			for (int y=0;y<radius+1;y++) {
				for (int z=-radius;z<radius+1;z++) {
					if (!BlockUtils.isExplosionProof(m.getLocation().add(x,y,z).getBlock().getType())) {
						//Break it.
						if (ChanceToBreak(m.getLocation().add(x,y,z).getBlock())) {
							m.getLocation().add(x,y,z).getBlock().breakNaturally();
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
				blocktoughness=20;
			}break;
			case ENDER_CHEST:{
				blocktoughness=20;
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
		}
		
		/*
		 * OBSIDIAN(50),
	ENDER_CHEST(23),
	ANVIL(10),
	COAL_BLOCK(10),
	DIAMOND_BLOCK(10),
	EMERALD_BLOCK(10),
	IRON_BLOCK(10),
	REDSTONE_BLOCK(10),
	ENCHANTMENT_TABLE(10),
	IRON_FENCE(10),
	IRON_DOOR(10),
	IRON_TRAPDOOR(10), 
	MONSTER_SPAWNER(10),
	WEB(10),
	DISPENSER(10),
	DROPPER(10),
	FURNACE(10),
	BEACON(6),
	BLOCK_OF_GOLD(6),
	COAL_ORE(6),
	DIAMOND_ORE(6),
	EMERALD_ORE(6),
	END_STONE(6),
	GOLD_ORE(6),
	HOPPER(6),
	IRON_ORE(6),
	LAPIS_BLOCK(6),
	LAPIS_ORE(6),
	NETHER_QUARTZ_ORE(6),
	REDSTONE_ORE(6),
	GLOWING_REDSTONE_ORE(6),
	TRAP_DOOR(6),
	WOODEN_DOOR(6);
		 */
		
		if (Math.random()*((double)blocktoughness/5)<1) {
			return true;
		} else {
			return false;
		}
	}
}
