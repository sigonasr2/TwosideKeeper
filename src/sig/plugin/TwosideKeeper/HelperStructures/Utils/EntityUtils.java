package sig.plugin.TwosideKeeper.HelperStructures.Utils;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import sig.plugin.TwosideKeeper.LivingEntityStructure;
import sig.plugin.TwosideKeeper.MonsterController;
import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.HelperStructures.CloudRunnable;
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
				LivingEntityStructure les = LivingEntityStructure.getLivingEntityStructure((LivingEntity)e);
				les.checkedforcubes=true;
				LivingEntityDifficulty diff = MonsterController.getLivingEntityDifficulty((LivingEntity)e);
				if (e!=null && e.getType()==type && (strongest==null || !strongest.isStronger(diff))) {
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
	
	public static void applyDamageIndicator(Entity e, double damage, IndicatorType type) {
		Location offsetloc = e.getLocation().add(Math.random()/2-0.25,0.5,Math.random()/2-0.25);
		if (damage>=1) {
			AreaEffectCloud aec = CreateOverlayText(offsetloc,((damage>=100)?ChatColor.BOLD+"  ":"")+type.getColor()+Integer.toString((int)damage)+((damage>=100)?"  ":""));
			Bukkit.getScheduler().runTaskLater(TwosideKeeper.plugin,new CloudRunnable(aec,0.15*Math.min(Math.max(1,(double)damage/50),2),(int)(10*Math.min(Math.max(1,(double)damage/50),2))),1);
		}
	}

	public static AreaEffectCloud CreateOverlayText(Location loc, String overlay) {
		AreaEffectCloud aec = (AreaEffectCloud)loc.getWorld().spawnEntity(loc, EntityType.AREA_EFFECT_CLOUD);
		aec.setCustomName(overlay);
		aec.setCustomNameVisible(true);
		aec.setRadius(0);
		aec.setParticle(Particle.ITEM_TAKE);
		//Bukkit.getScheduler().runTaskLater(TwosideKeeper.plugin,new CloudRunnable(aec,0.15,10),1);
		return aec;
	}
	
	public static void createPotionEffectSwirls(LivingEntity l,Color col) {
		AreaEffectCloud aec = (AreaEffectCloud)l.getWorld().spawnEntity(l.getLocation(), EntityType.AREA_EFFECT_CLOUD);
		aec.setColor(col);
		aec.setDuration(5);
		aec.setRadius(0.1f);
	}
}
