package sig.plugin.TwosideKeeper.Monster;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;

import sig.plugin.TwosideKeeper.ChargeZombie;
import sig.plugin.TwosideKeeper.CustomDamage;
import sig.plugin.TwosideKeeper.CustomMonster;
import sig.plugin.TwosideKeeper.MonsterController;
import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.TwosideKeeperAPI;
import sig.plugin.TwosideKeeper.HelperStructures.LivingEntityDifficulty;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.SoundUtils;

public class Wither extends CustomMonster{
	private long lastSkullShot = 0;
	private long lastWitherSkeletonSpawned=0;
	private long stuckTimer=0;
	private Location lastLoc = null;
	private List<Player> activeplayers = new ArrayList<Player>();
	private HashMap<String,Double> dmgbreakdown = new HashMap<String,Double>();
	
	public Wither(LivingEntity m) {
		super(m);
		this.lastSkullShot=TwosideKeeper.getServerTickTime();
	}
	
	public List<Player> getActiveParticipants() {
		return activeplayers;
	}
	
	public long getLastSkullShot() {
		return lastSkullShot;
	}
	
	public void resetLastSkullShot() {
		this.lastSkullShot = TwosideKeeper.getServerTickTime();
	}
	
	public void runHitEvent(Entity damager, double damage) { //Runs when this monster gets hit.
		LivingEntity shooter = CustomDamage.getDamagerEntity(damager);
		if (shooter instanceof Player) {
			Player p = (Player)shooter;
			if (dmgbreakdown.containsKey(p.getName())) {
				dmgbreakdown.put(p.getName(), dmgbreakdown.get(p.getName())+damage);
			} else {
				dmgbreakdown.put(p.getName(), damage);
			}
			if (!activeplayers.contains(p)) {
				activeplayers.add(p);
			}
		}
	}
	
	public void runTick() {
		if (m instanceof Monster) {
			RemoveInactivePlayers();
			setupName();
			
			if (activeplayers.size()==0 && dmgbreakdown.size()>0) {
				DisplayFailedDPSReport();
			}
			
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
				if (numb<=0.5) {
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
			if (m.getWorld().getName().equalsIgnoreCase("world_nether") && m.getLocation().getY()>=128) {
				m.teleport(m.getLocation().add(0,-4,0));
			} else 
			if (m.getWorld().getName().equalsIgnoreCase("world") && m.getLocation().getY()>=164) {
				Location newloc = m.getLocation().clone();
				newloc.setY(164);
				m.teleport(newloc);
			}
			if (m.getHealth()<86000 && lastWitherSkeletonSpawned+40<TwosideKeeper.getServerTickTime() &&
					GenericFunctions.GetNearbyMonsterCount(m, 32)<50) {
				lastWitherSkeletonSpawned = TwosideKeeper.getServerTickTime();
				Skeleton ws = (Skeleton)m.getWorld().spawnEntity(m.getLocation(), EntityType.SKELETON);
				ws.setSkeletonType(SkeletonType.WITHER);
				MonsterController.convertLivingEntity(ws, LivingEntityDifficulty.HELLFIRE);
				ws.setMaxHealth(ws.getMaxHealth()*6);
				ws.setHealth(ws.getMaxHealth());
				//ws.setCustomName(ChatColor.RED+"Hellfire Wither Skeleton Minion");
				TwosideKeeperAPI.setCustomLivingEntityName(ws, ChatColor.RED+"Wither Skeleton Minion");
				if (((Monster) m).getTarget()!=null && ((Monster) m).getTarget().isValid() &&
						!((Monster) m).getTarget().isDead()) {
					ws.setTarget(((Monster) m).getTarget());
				} else {
					ws.setTarget(FindClosestNearbyTarget());
				}
			}
		}
	}
	
	private void setupName() {
		TwosideKeeperAPI.setCustomLivingEntityName(m, ChatColor.DARK_RED+"Leader Wither");
	}

	private void RemoveInactivePlayers() {
		for (Player pl : activeplayers) {
			if (pl==null || !pl.isValid() || pl.isDead() || !pl.isOnline()) {
				TwosideKeeper.ScheduleRemoval(activeplayers, pl);
			}
		}
	}

	private void DisplayFailedDPSReport() {
		Bukkit.getServer().broadcastMessage(GenericFunctions.getDisplayName(m)+" Takedown Failed...");
		Bukkit.getServer().broadcastMessage(ChatColor.YELLOW+"DPS Breakdown:");
		Bukkit.getServer().broadcastMessage(generateDPSReport());
		aPlugin.API.discordSendRaw(GenericFunctions.getDisplayName(m)+" Takedown Failed...\n\n"+ChatColor.YELLOW+"DPS Breakdown:"+"\n```\n"+generateDPSReport()+"\n```");
		dmgbreakdown.clear();
	}
	
	public void DisplaySuccessfulDPSReport() {
		Bukkit.getServer().broadcastMessage(GenericFunctions.getDisplayName(m)+" has been defeated!");
		Bukkit.getServer().broadcastMessage(ChatColor.YELLOW+"DPS Breakdown:");
		Bukkit.getServer().broadcastMessage(generateDPSReport());
		aPlugin.API.discordSendRaw(GenericFunctions.getDisplayName(m)+" has been defeated!\n\n"+ChatColor.YELLOW+"DPS Breakdown:"+"\n```\n"+generateDPSReport()+"\n```");
		dmgbreakdown.clear();
	}
	

	public String generateDPSReport() {
		//Sorts a list of players by DPS contribution.
		List<Double> sorted_dmg = new ArrayList<Double>();
		List<String> sorted_pl = new ArrayList<String>();
		double totaldmg = 0;
		for (String pl : dmgbreakdown.keySet()) {
			double dmg = dmgbreakdown.get(pl);
			int slot = 0;
			totaldmg+=dmg;
			for (int i=0;i<sorted_dmg.size();i++) {
				if (dmg>sorted_dmg.get(i)) {
					break;
				} else {
					slot++;
				}
			}
			sorted_pl.add(slot,pl);
			sorted_dmg.add(slot,dmg);
		}
		StringBuilder finalstr = new StringBuilder();
		DecimalFormat df = new DecimalFormat("0.00");
		for (int i=0;i<sorted_pl.size();i++) {
			if (finalstr.length()!=0) {
				finalstr.append("\n");
			}
			finalstr.append(sorted_pl.get(i)+": "+df.format(sorted_dmg.get(i))+" dmg ("+df.format((sorted_dmg.get(i)/totaldmg)*100)+"%)");
		}
		return finalstr.toString();
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
	
	public void Cleanup() {
		if (dmgbreakdown.size()>0) {
			DisplayFailedDPSReport();
		}
	}
}
