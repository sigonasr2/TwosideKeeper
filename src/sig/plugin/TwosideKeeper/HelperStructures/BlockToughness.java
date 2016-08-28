package sig.plugin.TwosideKeeper.HelperStructures;

public enum BlockToughness {
	
	OBSIDIAN(20),
	ENDER_CHEST(20),
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
	
	int toughness=3;
	
	BlockToughness(int toughness) {
		this.toughness=toughness;
	}
	
	public int GetToughness() {
		return this.toughness;
	}
}
