package sig.plugin.TwosideKeeper.Monster;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;

import sig.plugin.TwosideKeeper.ChargeZombie;
import sig.plugin.TwosideKeeper.CustomMonster;
import sig.plugin.TwosideKeeper.LivingEntityStructure;
import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.HelperStructures.Channel;
import sig.plugin.TwosideKeeper.HelperStructures.PlayerMode;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.SoundUtils;

public class GenericBoss extends CustomMonster{
	
	protected List<Player> participantlist = new ArrayList<Player>();
	protected HashMap<String,Double> dpslist = new HashMap<String,Double>();
	protected HashMap<String,HashMap<String,Integer>> modelist = new HashMap<String,HashMap<String,Integer>>();
	BossBar healthbar;
	protected String arrow = "->";
	int scroll=0;
	boolean startedfight=false;
	private Location lastLoc = null;
	private long stuckTimer=0;
	long lasthit;
	double baseHP;
	protected boolean isFlying=false;

	public GenericBoss(LivingEntity m) {
		super(m);
	}

	public List<Player> getParticipants() {
		return participantlist;
	}
	
	public void runTick() {
		updateHealthbarForNearbyPlayers();
		updateTargetIfLost();
		regenerateHealthAndResetBossIfIdle();
		keepHealthbarUpdated();
		unstuckIfStuck();
		increaseBarTextScroll();
		adjustHPBasedOnPartyMembers();
	}

	private void adjustHPBasedOnPartyMembers() {
		if (participantlist.size()>=4 &&
				m.getMaxHealth()<adjustedHPAmount()) {
			double prevhp = m.getMaxHealth();
			m.setMaxHealth(adjustedHPAmount());
			m.setHealth(m.getHealth()+(m.getMaxHealth()-prevhp));
		}
	}

	private double adjustedHPAmount() {
		double amt = baseHP;
		if (participantlist.size()>=4) {
			amt += (participantlist.size()-3)*(baseHP*0.25);
		}
		return amt;
	}

	protected void increaseBarTextScroll() {
		scroll++;
		switch (scroll%22) {
			case 11:{
				arrow="  -";
			}break;
			case 12:{
				arrow="   ";
			}break;
			case 13:{
				arrow=">  ";
			}break;
			case 14:{
				arrow="->";
			}break;
		}
	}

	private void unstuckIfStuck() {
		if (!startedfight) {
			//ChargeZombie.BreakBlocksAroundArea((Monster)m, 1);
		} else
		if (startedfight) {
			ChargeZombie.BreakBlocksAroundArea((Monster)m, 1);
			lastLoc = m.getLocation().clone();
			if (lastLoc!=null && lastLoc.distance(m.getLocation())<=0.4) {
				stuckTimer++;
				//TwosideKeeper.log("Stuck. "+stuckTimer, 0);
				ChargeZombie.BreakBlocksAroundArea((Monster)m, 1);
			} else {
				stuckTimer=0;
			}
			if (!Channel.isChanneling(m) && stuckTimer>5) {
				//Teleport randomly.
				double numb = Math.random();
				if (numb<=0.33) {
					Location newloc = m.getLocation().add(Math.random()*10-5,0,0);
					if (!newloc.getBlock().getType().isSolid() &&
							!newloc.getBlock().getRelative(0,1,0).getType().isSolid()) {
						SoundUtils.playGlobalSound(m.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 0.4f, 0.95f);
						m.teleport(newloc);
					}
				} else
				if (numb<=0.5) {
					Location newloc = m.getLocation().add(0,0,Math.random()*10-5);
					if (!newloc.getBlock().getType().isSolid() &&
							!newloc.getBlock().getRelative(0,1,0).getType().isSolid()) {
						SoundUtils.playGlobalSound(m.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 0.4f, 0.95f);
						m.teleport(newloc);
					}
				}
				stuckTimer=0;
			}
		}
	}

	private void keepHealthbarUpdated() {
		healthbar.setProgress(m.getHealth()/m.getMaxHealth());
		Monster me = (Monster)m;
		String healthbarfooter = ((me.getTarget()!=null && (me.getTarget() instanceof Player))?(ChatColor.DARK_AQUA+" "+arrow+" "+ChatColor.YELLOW+((Player)me.getTarget()).getName()):"");
		for (Player p : participantlist) {
			if (p.isFlying()) {
				p.setFlying(false);
			}
		}
		if (Channel.isChanneling(m)) {
			healthbar.setTitle(LivingEntityStructure.getChannelingBar(m)+healthbarfooter);
		} else {
			healthbar.setTitle(GenericFunctions.getDisplayName(m)+healthbarfooter);
		}
	}

	private void regenerateHealthAndResetBossIfIdle() {
		if (lasthit+20*15<=TwosideKeeper.getServerTickTime()) {
			GenericFunctions.HealEntity(m, m.getMaxHealth()*0.01);
			if (startedfight) {
				healthbar.setColor(BarColor.GREEN);
			}
		} else {
			if (startedfight) {
				healthbar.setColor(BarColor.BLUE);
			}
		}
		if (participantlist.size()==0 && startedfight) {
			startedfight=false;
			m.setAI(false);
			m.setMaxHealth(baseHP);
			m.setHealth(m.getMaxHealth());
			announceFailedTakedown();
		}
	}

	public void announceFailedTakedown() {
		/*m.setMaxHealth(baseHP);
		m.setHealth(m.getMaxHealth());*/
		if (dpslist.size()>0 && !m.isDead()) {
			Bukkit.getServer().broadcastMessage(GenericFunctions.getDisplayName(m)+" Takedown Failed...");
			Bukkit.getServer().broadcastMessage(ChatColor.YELLOW+"DPS Breakdown:");
			Bukkit.getServer().broadcastMessage(generateDPSReport());
			aPlugin.API.discordSendRaw(GenericFunctions.getDisplayName(m)+" Takedown Failed...\n\n"+ChatColor.YELLOW+"DPS Breakdown:"+"\n```\n"+generateDPSReport()+"\n```");
			dpslist.clear();
			healthbar.setColor(BarColor.WHITE);
			if (m instanceof Monster) {
				Monster me = (Monster)m;
				me.setTarget(null);
			}
		}
	}

	private void updateTargetIfLost() {
		Monster mm = (Monster)m;
		LivingEntityStructure les = LivingEntityStructure.GetLivingEntityStructure(m);
		if ((mm.getTarget()==null || !mm.getTarget().isValid() ||
				les.GetTarget()==null || !mm.getTarget().isValid() ||
				(((mm.getTarget().getLocation().distanceSquared(mm.getLocation())>2500 ||
				les.GetTarget().getLocation().distanceSquared(mm.getLocation())>2500))))
				&& !isFlying) {
			//See if there's another participant in the list. Choose randomly.
			while (participantlist.size()>0) {
				Player p = participantlist.get((int)(Math.random()*participantlist.size()));
				if (p!=null && p.isValid() && !p.isDead() &&
						(p.getLocation().distanceSquared(mm.getLocation())<=2500)) {
					mm.setTarget(p);
					les.SetTarget(p);
					break;
				} else {
					participantlist.remove(p);
				}
			}
			if (participantlist.size()==0 && startedfight) {
				//This fight has failed.
				announceFailedTakedown();
				startedfight=false;
			}
		}
	}

	private void updateHealthbarForNearbyPlayers() {
		for (Player p : healthbar.getPlayers()) {
			if (p.getWorld().equals(m.getWorld()) && p.getLocation().distanceSquared(m.getLocation())>576) {
				healthbar.removePlayer(p);
			}
		}
		for (Entity e : m.getNearbyEntities(24, 24, 24)) {
			if (e instanceof Player) {
				Player p = (Player)e;
				healthbar.addPlayer(p);
				//TwosideKeeper.log("Added Player "+p, 0);
			}
		}
	}
	
	public String generateDPSReport() {
		//Sorts a list of players by DPS contribution.
		List<Double> sorted_dmg = new ArrayList<Double>();
		List<String> sorted_pl = new ArrayList<String>();
		double totaldmg = 0;
		for (String pl : dpslist.keySet()) {
			double dmg = dpslist.get(pl);
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

	public void AnnounceDPSBreakdown() {
		List<Player> participants = getParticipants();
		StringBuilder participants_list = new StringBuilder();
		for (int i=0;i<participants.size();i++) {
			Player pl = participants.get(i);
			if (pl!=null && pl.isOnline()) {
				/*ExperienceOrb exp = GenericFunctions.spawnXP(pl.getLocation(), ev.getDroppedExp()*300);
				exp.setInvulnerable(true);  
				if (m instanceof Zombie) {
					Zombie z = (Zombie)m;
					if (z.isBaby()) {
						GenericFunctions.giveItem(pl,aPlugin.API.getChestItem(Chests.ELITE));
					}
				}*/
				//GenericFunctions.giveItem(pl,aPlugin.API.getChestItem(Chests.ELITE));
				//log("Dropping "+aPlugin.API.getChestItem(Chests.ELITE).toString(),2);
				if (participants_list.length()<1) { 
					participants_list.append(pl.getName());
				} else {
					if (i==participants.size()-1) {
						if (participants.size()==2) {
							participants_list.append(" and "+pl.getName());
						} else {
							participants_list.append(", and "+pl.getName());
						}
					} else {
						participants_list.append(", "+pl.getName());
					}
				}
			} else {
				/*Item it = m.getWorld().dropItemNaturally(m.getLocation(), aPlugin.API.getChestItem(Chests.ELITE));
		        it.setInvulnerable(true);
		        it.setPickupDelay(0);*/
			}
		}
		Bukkit.getServer().broadcastMessage(ChatColor.GREEN+participants_list.toString()+ChatColor.WHITE+" "+(participants.size()==1?"has single-handedly taken down the ":"have successfully slain ")+GenericFunctions.getDisplayName(m)+ChatColor.WHITE+"!");
		aPlugin.API.discordSendRaw(ChatColor.GREEN+participants_list.toString()+ChatColor.WHITE+" "+(participants.size()==1?"has single-handedly taken down the ":"have successfully slain ")+"**"+GenericFunctions.getDisplayName(m)+ChatColor.WHITE+"**!");

		Bukkit.getScheduler().scheduleSyncDelayedTask(TwosideKeeper.plugin, new Runnable() {
			public void run() {
				Bukkit.getServer().broadcastMessage(ChatColor.YELLOW+"DPS Breakdown:");
				Bukkit.getServer().broadcastMessage(generateDPSReport());
				aPlugin.API.discordSendRaw(ChatColor.YELLOW+"DPS Breakdown:"+"\n```\n"+generateDPSReport()+"\n```");
				cleanup();
			}},1);
	}
	
	public void cleanup() {
		healthbar.removeAll();
	}

	protected void announceMessageToParticipants(String msg) {
		for (Player p : participantlist) {
			p.sendMessage(msg);
		}
	}
	
	public void onHitEvent(LivingEntity damager, double damage) {
		addTarget(damager,damage);
		Bukkit.getScheduler().scheduleSyncDelayedTask(TwosideKeeper.plugin, ()->{
			m.setVelocity(m.getVelocity().multiply(getKnockbackMult()));
		}, 1);
		healthbar.setProgress(m.getHealth()/m.getMaxHealth());
		lasthit=TwosideKeeper.getServerTickTime();
		if (!startedfight) {
			startedfight=true;
			healthbar.setColor(BarColor.BLUE);
		}
		m.setAI(true);
	}

	private void addTarget(LivingEntity damager, double dmg) {
		if (damager instanceof Player) {
			Player p = (Player)damager;
			addParticipant(p);
			if (!dpslist.containsKey(p.getName())) {
				dpslist.put(p.getName(), dmg);
			} else {
				dpslist.put(p.getName(), dpslist.get(p.getName())+dmg);
			}
			if (!modelist.containsKey(p.getName())) {
				HashMap<String,Integer> modestruct = new HashMap<String,Integer>();
				modestruct.put(PlayerMode.getPlayerMode(p).name(), 1);
				modelist.put(p.getName(), modestruct);
			} else {
				HashMap<String,Integer> modestruct = modelist.get(p.getName());
				if (modestruct.containsKey(PlayerMode.getPlayerMode(p).name())) {
					Integer amt = modestruct.get(PlayerMode.getPlayerMode(p).name());
					modestruct.put(PlayerMode.getPlayerMode(p).name(), ++amt);
				} else {
					modestruct.put(PlayerMode.getPlayerMode(p).name(), 1);
				}
			}
		}
	}
	
	public PlayerMode getMostUsedPlayerMode(Player p) {
		return getMostUsedPlayerMode(p.getName());
	}
	
	public PlayerMode getMostUsedPlayerMode(String s) {
		int highestamt = 0;
		PlayerMode highestmode = null;
		if (modelist.containsKey(s)) {
			HashMap<String,Integer> modestruct = modelist.get(s);
			for (String ss : modestruct.keySet()) {
				PlayerMode pm = PlayerMode.valueOf(ss);
				int amt = modestruct.get(ss);
				if (highestamt<amt) {
					highestamt=amt;
					highestmode=pm;
				}
			}
		}
		if (highestmode!=null) {
			return highestmode;
		} else {
			return PlayerMode.NORMAL;
		}
	}

	public void addParticipant(Player p) {
		if (!participantlist.contains(p)) {
			participantlist.add(p);
		}
	}

	public void setupBonusLoot() {
	}

	public static int bossCount() {
		int amt = 0;
		for (UUID id : TwosideKeeper.custommonsters.keySet()) {
			CustomMonster cm = TwosideKeeper.custommonsters.get(id);
			if (cm instanceof GenericBoss) {
				amt++;
			}
		}
		return amt;
	}

	//Returns the number of nearby bosses in the specified location (That inherit this class).
	public static int nearbyBosses(Location loc, int range) {
		int amt=0;
		for (UUID id : TwosideKeeper.custommonsters.keySet()) {
			CustomMonster cm = TwosideKeeper.custommonsters.get(id);
			if (cm instanceof GenericBoss) {
				GenericBoss gb = (GenericBoss)cm;
				if (gb.GetMonster().getLocation().distanceSquared(loc)<2500) {
					amt++;
				}
			}
		}
		return amt;
	}
}
