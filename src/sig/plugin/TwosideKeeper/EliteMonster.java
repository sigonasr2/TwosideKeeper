package sig.plugin.TwosideKeeper;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.inventivetalent.glow.GlowAPI;

import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.SoundUtils;

public class EliteMonster {
	protected static float DEFAULT_MOVE_SPD = 0.3f;
	static int IGNORE_TARGET_DURATION = 20*20;
	static int RESTORE_HEALTH = 20*10;
	static int WAIT_TIME = 20*10;
	
	protected Monster m;
	Location myspawn = null;
	protected BossBar bar = null;
	protected BossBar willpower_bar = null;
	protected boolean first_willpower_notification=false;
	int scroll=0;
	protected int willpower=0;
	protected String arrow = "->";
	public Player my_only_target=null;
	protected double hp_before_burstcheck=0;
	protected long last_regen_time=0;
	public double last_ignoretarget_time=0;
	protected boolean leaping=false;
	protected boolean chasing=false;
	protected boolean enraged=false;
	protected boolean storingenergy=false;
	public double baseHP = 0.0;
	
	protected List<Player> targetlist = new ArrayList<Player>();
	protected List<Player> participantlist = new ArrayList<Player>();
	protected HashMap<String,Double> dpslist = new HashMap<String,Double>();
	//Contains all functionality specific to Elite Monsters.
	//These are checked every 5 ticks, so have very high control over the monster itself.
	public EliteMonster(Monster m) {
		this.m=m;
		m.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(DEFAULT_MOVE_SPD);
		this.hp_before_burstcheck=m.getHealth();
		this.myspawn=m.getLocation();
		bar = m.getServer().createBossBar(GenericFunctions.getDisplayName(m), BarColor.WHITE, BarStyle.SEGMENTED_6, BarFlag.CREATE_FOG);
		willpower_bar = m.getServer().createBossBar("Willpower", BarColor.PINK, BarStyle.SOLID, BarFlag.CREATE_FOG);
		this.baseHP = m.getMaxHealth();
	}
	
	public void runTick() {
		//This monster constantly gives itself its buffs as it may lose some (Debilitation mode).
		increaseBarTextScroll();
		dontDrown();
		regenerateHealth();
		resetToSpawn();
		createBossHealthbar();
		ignoreAllOtherTargets();
		if (m.isValid() && targetlist.size()>0) {
			retargetInAir();
			destroyLiquids(2);
			getGlow();
		}
	}

	protected void ignoreAllOtherTargets() {
		if (my_only_target!=null && targetlist.contains(my_only_target)) {
			m.setTarget(my_only_target);
			TwosideKeeper.log("I aim at "+m.getTarget()+"!!", 2);
			if (last_ignoretarget_time+IGNORE_TARGET_DURATION<TwosideKeeper.getServerTickTime()) {
				my_only_target=null;
				m.setTarget(ChooseRandomTarget());
			}
		}
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

	protected void createBossHealthbar() {
		if (m instanceof Wither || m instanceof EnderDragon) {
			bar.removeAll();
			willpower_bar.removeAll();
			return;
		}
		List<Player> currentplayers = bar.getPlayers();
		for (int i=0;i<currentplayers.size();i++) {
			if (!targetlist.contains(currentplayers.get(i))) {
				bar.removePlayer(currentplayers.get(i));
				willpower_bar.removePlayer(currentplayers.get(i));
			}
		}
		bar.setProgress(m.getHealth()/m.getMaxHealth());
		bar.setTitle(GenericFunctions.getDisplayName(m) + ((m.getTarget()!=null && (m.getTarget() instanceof Player))?(ChatColor.DARK_AQUA+" "+arrow+" "+ChatColor.YELLOW+((Player)m.getTarget()).getName()):""));
		for (int i=0;i<targetlist.size();i++) {
			if (!currentplayers.contains(targetlist.get(i))) {
				bar.addPlayer(targetlist.get(i));
			}
		}
	}

	protected void resetToSpawn() {
		if (!leaping && targetlist.size()==0 && m.getLocation().getWorld().equals(myspawn.getWorld()) && m.getLocation().distanceSquared(myspawn)>81) {
			while (myspawn.getBlock().getRelative(0, -1, 0).getType()==Material.AIR && myspawn.getY()>1) {
				myspawn = myspawn.add(0,-1,0);
			}
			m.teleport(myspawn);
			m.setHealth(m.getMaxHealth());
			AnnounceFailedTakedown();
			bar.setColor(BarColor.WHITE);
			first_willpower_notification=false;
			willpower=0;
			bar.removeAll();
			willpower_bar.removeAll();
		} else {
			createBossHealthbar();
		}
		if (!m.getLocation().getWorld().equals(myspawn.getWorld())) {
			myspawn = m.getLocation(); //Then this is my new spawn...
		}
		for (Player p : targetlist) {
			if (p.isDead()) {
				targetlist.remove(p);
			}
		}
		if (targetlist.size()==0) {
			participantlist.clear();
		}
	}

	public void AnnounceFailedTakedown() {
		if (dpslist.size()>0 && !m.isDead()) {
			Bukkit.getServer().broadcastMessage(GenericFunctions.getDisplayName(m)+" Takedown Failed...");
			Bukkit.getServer().broadcastMessage(ChatColor.YELLOW+"DPS Breakdown:");
			Bukkit.getServer().broadcastMessage(generateDPSReport());
			aPlugin.API.discordSendRaw(GenericFunctions.getDisplayName(m)+" Takedown Failed...\n\n"+ChatColor.YELLOW+"DPS Breakdown:"+"\n```\n"+generateDPSReport()+"\n```");
			dpslist.clear();
		}
	}

	protected void dontDrown() {
		m.setRemainingAir(m.getMaximumAir());
	}
	
	public void BreakBlocksAroundArea() {
		ChargeZombie.BreakBlocksAroundArea(2, m.getLocation());
	}

	public GlowAPI.Color getGlow() {
		GlowAPI.Color col = GlowAPI.Color.DARK_PURPLE;
		if (m.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
			col = GlowAPI.Color.YELLOW;
		}
		if (storingenergy) {
			col = GlowAPI.Color.GREEN;
		}
		//GenericFunctions.setGlowing(m, col);
		return col;
	}

	private void destroyLiquids(int radius) {
		for (int x=-radius;x<=radius;x++) {
			for (int y=-radius;y<=radius;y++) {
				for (int z=-radius;z<=radius;z++) {
					Block b = m.getLocation().add(0,-0.9,0).getBlock().getRelative(x,y,z);
					if (b.isLiquid()) {
						b.setType(Material.AIR);
					}
				}
			}
		}
	}

	private void retargetInAir() {
		if (Math.random()<=0.01) {
			Player p = ChooseRandomTarget();
			//p.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION,20*5,-31));
			GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.JUMP,20*5,-1,p);
			GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.BLINDNESS,20*1,7,p);
			if (Math.random()<=0.25) {
				m.setTarget(p);
			}
			p.setFlying(false);
			p.setVelocity(new Vector(0,-1,0));
			GenericFunctions.logAndRemovePotionEffectFromEntity(PotionEffectType.LEVITATION,p);
			GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.CONFUSION,(int)(20*2.25),0,p);
			SoundUtils.playLocalSound(p, Sound.BLOCK_ANVIL_FALL, 0.4f, 0.8f);
			SoundUtils.playLocalSound(p, Sound.ENTITY_MAGMACUBE_SQUISH, 1.0f, 1.0f);
		}
	}

	protected void regenerateHealth() {
		if (m.getHealth()<m.getMaxHealth() && last_regen_time+RESTORE_HEALTH<=TwosideKeeper.getServerTickTime()) {
			bar.setColor(BarColor.GREEN);
			m.setHealth(Math.min(m.getHealth()+1,m.getMaxHealth()));
		}
		if (m.getMaxHealth()>(baseHP+(baseHP*(0.25*((targetlist.size()>4)?(targetlist.size()-4):0))))) {
			m.setMaxHealth((baseHP+(baseHP*(0.25*((targetlist.size()>4)?(targetlist.size()-4):0)))));
			if (m.getHealth()>m.getMaxHealth()) {
				m.setHealth(m.getMaxHealth());
			}
		}
	}
	
	public void runPlayerLeaveEvent(Player p) {
		targetlist.remove(p);
		bar.removePlayer(p);
		willpower_bar.removePlayer(p);
	}
	
	//Triggers when this mob is hit.
	public void runHitEvent(LivingEntity damager, double dmg) {
		bar.setColor(BarColor.RED);
		if (!targetlist.contains(damager) && (damager instanceof Player)) {
			targetlist.add((Player)damager);
			if (targetlist.size()>4) {
				double hpgain = m.getMaxHealth()*(0.25*(targetlist.size()-4));
				m.setMaxHealth(baseHP+hpgain);
				m.setHealth(m.getHealth()+hpgain);
			}
		}
		if (!participantlist.contains(damager) && (damager instanceof Player)) {
			participantlist.add((Player)damager);
		}
		if (damager instanceof Player) {
			Player p = (Player)damager;
			double currentdps=0;
			if (dpslist.containsKey(p.getName())) {
				currentdps = dpslist.get(p.getName());
			}
			dpslist.put(p.getName(), currentdps+dmg);
			TwosideKeeper.log(p.getName()+"'s Damage: "+dpslist.get(p.getName()), 5);
		}
		last_regen_time=TwosideKeeper.getServerTickTime();
	}

	protected Location getNearbyFreeLocation(Location l) {
		return getNearbyFreeLocation(l,3);
	}
	
	private Location getNearbyFreeLocation(Location l, int range) {
		int tries = 0;
		while (tries<50) {
			Location testloc = l.add((Math.random()*(range*2))-(range),Math.random()*range,(Math.random()*(range*2))-(range));
			Block testblock = testloc.getBlock();
			TwosideKeeper.log("Trying "+testloc, 5);
			if (testblock.getType()==Material.AIR && testblock.getRelative(0, 1, 0).getType()==Material.AIR) {
				return testloc;
			}
		}
		return l;
	}

	//Triggers when this mob hits something.
	public void hitEvent(LivingEntity ent) {
		if (!targetlist.contains(ent) && (ent instanceof Player) && !ent.isDead()) {
			targetlist.add((Player)ent);
			if (targetlist.size()>4) {
				double hpgain = m.getMaxHealth()*(0.25*(targetlist.size()-4));
				m.setMaxHealth(baseHP+hpgain);
				m.setHealth(m.getHealth()+hpgain);
			}
		}
	}

	public Player ChooseRandomTarget() {
		if (targetlist.size()>0) {
			Player p = targetlist.get((int)(Math.random() * targetlist.size()));
			GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.BLINDNESS,20*1,7,p);
			m.setTarget(p);
			TwosideKeeper.log("Set new target to "+p.getName(), 2);
			return p;
		} else {
			return null;
		}
	}
	
	public Monster getMonster() {
		return m;
	}
	
	public List<Player> getTargetList() {
		return targetlist;
	}
	
	public List<Player> getParticipantList() {
		return participantlist;
	}
	
	public void randomlyTeleport() {
		Location l = getNearbyFreeLocation(m.getLocation(),24);
		m.teleport(l);
	}

	public void removeAllHealthbars() {
		bar.removeAll();
		willpower_bar.removeAll();
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

	public void Cleanup() {
		// Remove all healthbars before destroying.
		AnnounceFailedTakedown();
		removeAllHealthbars();
	}
}
