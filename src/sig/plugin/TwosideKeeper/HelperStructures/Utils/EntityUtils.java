package sig.plugin.TwosideKeeper.HelperStructures.Utils;

import java.util.List;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import sig.plugin.TwosideKeeper.MonsterController;
import sig.plugin.TwosideKeeper.HelperStructures.LivingEntityDifficulty;

public class EntityUtils {
	public static int CountNearbyEntityType(EntityType type, Entity ent, double range) {
		List<Entity> ents = ent.getNearbyEntities(range, range, range);
		int count=0;
		for (Entity e : ents) {
			if (e.getType()==type) {
				count++;
			}
		}
		return count;
	}
	public static LivingEntityDifficulty GetStrongestNearbyEntityDifficulty(EntityType type, Entity ent, double range) {
		List<Entity> ents = ent.getNearbyEntities(range, range, range);
		LivingEntityDifficulty strongest = LivingEntityDifficulty.NORMAL;
		for (Entity e : ents) {
			if (e instanceof LivingEntity) {
				LivingEntityDifficulty diff = MonsterController.getLivingEntityDifficulty((LivingEntity)e);
				if (e.getType()==type && !strongest.isStronger(diff)) {
					strongest = diff;
				}
			}
		}
		return strongest;
	}
}
