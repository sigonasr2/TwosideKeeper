package sig.plugin.TwosideKeeper.HelperStructures;

import org.bukkit.entity.EntityType;

public enum MonsterType {
	BLAZE(EntityType.BLAZE),
	CAVESPIDER(EntityType.CAVE_SPIDER),
	CREEPER(EntityType.CREEPER),
	ENDERMAN(EntityType.ENDERMAN),
	ENDERMITE(EntityType.ENDERMITE),
	GIANT(EntityType.GIANT),
	GUARDIAN(EntityType.GUARDIAN),
	PIGZOMBIE(EntityType.PIG_ZOMBIE),
	SILVERFISH(EntityType.SILVERFISH),
	SKELETON(EntityType.SKELETON),
	SPIDER(EntityType.SPIDER),
	WITCH(EntityType.WITCH),
	WITHER(EntityType.WITHER),
	WITHER_SKELETON(EntityType.SKELETON),
	ZOMBIE(EntityType.ZOMBIE);
	
	EntityType type;
	
	MonsterType(EntityType type) {
		this.type=type;
	}

	public EntityType getEntityType() {
		return type;
	}
	
	
}
