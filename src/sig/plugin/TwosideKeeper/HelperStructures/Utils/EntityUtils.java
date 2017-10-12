package sig.plugin.TwosideKeeper.HelperStructures.Utils;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.bukkit.block.BlockFace;

import sig.plugin.TwosideKeeper.Buff;
import sig.plugin.TwosideKeeper.LivingEntityStructure;
import sig.plugin.TwosideKeeper.MonsterController;
import sig.plugin.TwosideKeeper.PlayerStructure;
import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.HelperStructures.DamageLabel;
import sig.plugin.TwosideKeeper.HelperStructures.LivingEntityDifficulty;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.Classes.ColoredParticle;

public class EntityUtils {
	final public static BlockFace[] faces = new BlockFace[]{BlockFace.EAST,BlockFace.SOUTH_EAST,BlockFace.SOUTH,
			BlockFace.SOUTH_WEST,BlockFace.WEST,BlockFace.NORTH_WEST,BlockFace.NORTH,BlockFace.NORTH_EAST};
	
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
			if (e instanceof LivingEntity && !(e instanceof Player)) {
				LivingEntityStructure les = LivingEntityStructure.GetLivingEntityStructure((LivingEntity)e);
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
			ArmorStand aec = CreateOverlayText(offsetloc,((damage>=100)?ChatColor.BOLD+"  ":"")+type.getColor()+Integer.toString((int)damage)+((damage>=100)?"  ":""));
			TwosideKeeper.labelqueue.add(new DamageLabel(aec,0.1,(int)(10*Math.min(Math.max(1,(double)damage/50),2))));
		}
	}

	public static ArmorStand CreateOverlayText(Location loc, String overlay) {
		ArmorStand aec = (ArmorStand)loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
		aec.setCustomName(overlay);
		aec.setGravity(false);
		aec.setRemoveWhenFarAway(true);
		aec.setCustomNameVisible(true);
		aec.setVisible(false);
		aec.setMarker(true);
		aec.setInvulnerable(true);
		//aec.setRadius(0);
		//aec.setParticle(Particle.ITEM_TAKE);
		//Bukkit.getScheduler().runTaskLater(TwosideKeeper.plugin,new CloudRunnable(aec,0.15,10),1);
		return aec;
	}
	
	public static void createPotionEffectSwirls(LivingEntity l, Color col, int delay) {
		Bukkit.getScheduler().runTaskLater(TwosideKeeper.plugin, ()->{
			/*AreaEffectCloud aec = (AreaEffectCloud)l.getWorld().spawnEntity(l.getLocation(), EntityType.AREA_EFFECT_CLOUD);
			aec.setColor(col);
			aec.setDuration(5);
			aec.setRadiusOnUse(0.1f);
			aec.setRadiusPerTick(0f);
			aec.setRadius(0.1f);*/
			Location origloc = l.getLocation();
			for (int i=0;i<5;i++) {
				ColoredParticle.MOB_SPELL.send(origloc.clone().add(Math.random()*2-1,
						Math.random()*2-1,
						Math.random()*2-1), 10, col.getRed(), col.getGreen(), col.getBlue());
			}
		},delay);
	}

	/**
		Use Buff.addBuff() instead.
	 */
	@Deprecated
	public static void applyBuff(LivingEntity l, String buffname, Buff buff) {
	HashMap<String,Buff> buffMap;
		if (l instanceof Player) {
			PlayerStructure pd = PlayerStructure.GetPlayerStructure((Player)l);
			buffMap = pd.buffs;
		} else {
			LivingEntityStructure les = LivingEntityStructure.GetLivingEntityStructure(l);
			buffMap = les.buffs;
		}
		buffMap.put(buffname, buff);
		updateBuffDisplay(l);
	}

	/**
		Use Buff.addBuff() instead.
	 */
	@Deprecated
	public static void applyBuffs(LivingEntity l, String[] buffnames, Buff ... buffArr) {
		HashMap<String,Buff> buffMap;
		if (buffnames.length==buffArr.length) {
			if (l instanceof Player) {
				PlayerStructure pd = PlayerStructure.GetPlayerStructure((Player)l);
				buffMap = pd.buffs;
			} else {
				LivingEntityStructure les = LivingEntityStructure.GetLivingEntityStructure(l);
				buffMap = les.buffs;
			}
			for (int i=0;i<buffArr.length;i++) {
				buffMap.put(buffnames[i], buffArr[i]);
			}
			updateBuffDisplay(l);
		} else {
			TwosideKeeper.log("ERROR!! The number of buff names does not match the size of applied buffArr! Size of buffnames: "+buffnames.length+"; Size of buffArr: "+buffArr.length, 0);
			DebugUtils.showStackTrace();
		}
	}

	/**
		Use Buff.removeBuff() instead.
	 */
	@Deprecated
	public static void removeBuff(LivingEntity l, String buffName) {		
	HashMap<String,Buff> buffMap;
		if (l instanceof Player) {
			PlayerStructure pd = PlayerStructure.GetPlayerStructure((Player)l);
			buffMap = pd.buffs;
		} else {
			LivingEntityStructure les = LivingEntityStructure.GetLivingEntityStructure(l);
			buffMap = les.buffs;
		}
		buffMap.remove(buffName);
		updateBuffDisplay(l);
	}

	/**
		Use Buff.removeBuff() instead.
	 */
	@Deprecated
	public static void removeBuffs(LivingEntity l, String ... buffNames) {		
	HashMap<String,Buff> buffMap;
		if (l instanceof Player) {
			PlayerStructure pd = PlayerStructure.GetPlayerStructure((Player)l);
			buffMap = pd.buffs;
		} else {
			LivingEntityStructure les = LivingEntityStructure.GetLivingEntityStructure(l);
			buffMap = les.buffs;
		}
		for (int i=0;i<buffNames.length;i++) {
			buffMap.remove(buffNames[i]);
		}
		updateBuffDisplay(l);
	}
	
	private static void updateBuffDisplay(LivingEntity l) {
		if (l instanceof Player) {
			GenericFunctions.sendActionBarMessage((Player)l, "");			
		}
	}
	
	public static BlockFace getFacingDirection(LivingEntity l) {
		Vector direction = l.getLocation().getDirection();
		double rad = Math.atan2(direction.getZ(), direction.getX());
		double dir = Math.toDegrees(rad);
		if (dir<0) {
			dir=360-Math.abs(dir);
			//-90   180 + 90 = 270
			// -0  180
			//-180 360
		}
		//TwosideKeeper.log(Double.toString(dir), 0);
		//+Z: 90 degrees (South)
		//+X: 0 degrees (East)
		//-Z: -90 degrees (North) 
		//-X: -180/180 degrees (West)
		return faces[(int)((dir+22.5)/45)%faces.length];
	}
	
	public static boolean isValidEntity(Entity e) {
		return e!=null && e.isValid() && !e.isDead();
	}
}
