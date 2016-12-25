package sig.plugin.TwosideKeeper.HelperStructures.Utils;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

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
		LivingEntityDifficulty strongest = null;
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
	public static boolean PreventEnderCrystalDestruction(Entity ent) {
		if (ent instanceof EnderCrystal && ent.getWorld().getName().equalsIgnoreCase("world")) {
			return true;
		} else {
			return false;
		}
	}
	public static boolean ProperlyStoreEnderCrystal(Entity ent) {
		if (ent instanceof EnderCrystal && ent.getWorld().getName().equalsIgnoreCase("world")) {
			ent.getWorld().dropItemNaturally(ent.getLocation(), new ItemStack(Material.END_CRYSTAL));
			ent.remove();
			return true;
		} else {
			return false;
		}
	}
}
