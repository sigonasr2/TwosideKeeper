package sig.plugin.TwosideKeeper.Monster;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;

import sig.plugin.TwosideKeeper.ChargeZombie;
import sig.plugin.TwosideKeeper.CustomMonster;
import sig.plugin.TwosideKeeper.MonsterController;
import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.HelperStructures.LivingEntityDifficulty;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.SoundUtils;

public class Wither extends CustomMonster{
	private long lastSkullShot = 0;
	private long lastWitherSkeletonSpawned=0;
	private long stuckTimer=0;
	private Location lastLoc = null;
	
	public Wither(LivingEntity m) {
		super(m);
		this.lastSkullShot=TwosideKeeper.getServerTickTime();
	}
	
	public long getLastSkullShot() {
		return lastSkullShot;
	}
	
	public void resetLastSkullShot() {
		this.lastSkullShot = TwosideKeeper.getServerTickTime();
	}
	
	public void runTick() {
		if (m instanceof Monster) {
			if (((Monster) m).getTarget()!=null) {
				ChargeZombie.BreakBlocksAroundArea((Monster)m, 2);
			}
			if (lastLoc!=null && lastLoc.distance(m.getLocation())<=0.4) {
				stuckTimer++;
				//TwosideKeeper.log("Stuck. "+stuckTimer, 0);
			} else {
				stuckTimer=0;
			}
			lastLoc = m.getLocation().clone();
			if (stuckTimer>5) {
				//Teleport randomly.
				double numb = Math.random();
				if (numb<=0.33) {
					SoundUtils.playGlobalSound(m.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 1.0f, 1.0f);
					m.teleport(m.getLocation().add(Math.random()*10-5,0,0));
				} else
				if (numb<=0.33) {
					SoundUtils.playGlobalSound(m.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 1.0f, 1.0f);
					m.teleport(m.getLocation().add(0,0,Math.random()*10-5));
				} else
				{
					SoundUtils.playGlobalSound(m.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 1.0f, 1.0f);
					m.teleport(m.getLocation().add(0,Math.random()*10-5,0));
				}
				stuckTimer=0;
			}
			if (m.getHealth()<m.getMaxHealth()) {
				m.setHealth(Math.min(m.getMaxHealth(), m.getHealth()+5));
			}
			if (m.getLocation().getY()>=128) {
				m.teleport(m.getLocation().add(0,-32,0));
			}
			if (m.getHealth()<86000 && lastWitherSkeletonSpawned+40<TwosideKeeper.getServerTickTime() &&
					GenericFunctions.GetNearbyMonsterCount(m, 32)<50) {
				lastWitherSkeletonSpawned = TwosideKeeper.getServerTickTime();
				Skeleton ws = (Skeleton)m.getWorld().spawnEntity(m.getLocation(), EntityType.SKELETON);
				ws.setSkeletonType(SkeletonType.WITHER);
				MonsterController.convertLivingEntity(ws, LivingEntityDifficulty.HELLFIRE);
				ws.setMaxHealth(ws.getMaxHealth()*6);
				ws.setHealth(ws.getMaxHealth());
				ws.setCustomName(ChatColor.RED+"Hellfire Wither Skeleton Minion");
				if (((Monster) m).getTarget()!=null && ((Monster) m).getTarget().isValid() &&
						!((Monster) m).getTarget().isDead()) {
					ws.setTarget(((Monster) m).getTarget());
				} else {
					ws.setTarget(FindClosestNearbyTarget());
				}
			}
		}
	}

	private Player FindClosestNearbyTarget() {
		Player closestplayer=null;
		double dist=999999;
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (p.getWorld().equals(m.getWorld()) && p.getLocation().distance(m.getLocation())<dist) {
				dist=p.getLocation().distance(m.getLocation());
				closestplayer=p;
			}
		}
		return closestplayer;
	}
}
