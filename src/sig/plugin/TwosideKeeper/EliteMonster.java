package sig.plugin.TwosideKeeper;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.util.Vector;
import org.inventivetalent.glow.GlowAPI;

import sig.plugin.TwosideKeeper.HelperStructures.Loot;
import sig.plugin.TwosideKeeper.HelperStructures.MonsterDifficulty;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;

public class EliteMonster {
	static int REFRESH_BUFFS = 20*30;
	static int RESTORE_HEALTH = 20*10;
	static float DEFAULT_MOVE_SPD = 0.4f;
	static float FAST_MOVE_SPD = 0.65f;
	static long BURST_TIME = 20*3;
	static float BURST_LIMIT = 10f;
	static int WEAKNESS_DURATION = 20*10;
	static int POISON_DURATION = 20*10;
	static int LEAP_COOLDOWN = 20*40;
	static int ENRAGE_COOLDOWN = 20*60;
	static int STORINGENERGY_COOLDOWN = 20*50;
	static int GLOW_TIME = 20*1;
	static int WAIT_TIME = 20*10;
	
	Monster m;
	long last_rebuff_time=0;
	long last_regen_time=0;
	long last_burstcheck_time=0;
	long last_applyglow_time=0;
	double hp_before_burstcheck=0;
	double last_leap_time=0;
	double last_enrage_time=0;
	double last_storingenergy_time=0;
	double last_storingenergy_health=0;
	double storingenergy_hit=0;
	boolean leaping=false;
	boolean chasing=false;
	boolean enraged=false;
	boolean storingenergy=false;
	Location target_leap_loc = null;
	Location myspawn = null;
	HashMap<Block,Material> storedblocks = new HashMap<Block,Material>();
	BossBar bar = null;
	
	List<Player> targetlist = new ArrayList<Player>();
	//Contains all functionality specific to Elite Monsters.
	//These are checked every 5 ticks, so have very high control over the monster itself.
	EliteMonster(Monster m) {
		this.m=m;
		m.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(DEFAULT_MOVE_SPD);
		this.hp_before_burstcheck=m.getHealth();
		this.myspawn=m.getLocation();
		bar = m.getServer().createBossBar(m.getCustomName(), BarColor.RED, BarStyle.SEGMENTED_6, BarFlag.CREATE_FOG);
	}
	
	public void runTick() {
		//This monster constantly gives itself its buffs as it may lose some (Debilitation mode).
		dontDrown();
		rebuff();
		regenerateHealth();
		moveFasterToTarget();
		resetToSpawn();
		createBossHealthbar();
		if (m.isValid() && targetlist.size()>0) {
			weakenTeam();
			retargetInAir();
			destroyLiquids(2);
			reapplyGlow();
		}
	}

	private void createBossHealthbar() {
		bar.removeAll();
		for (int i=0;i<targetlist.size();i++) {
			bar.addPlayer(targetlist.get(i));
			bar.setProgress(m.getHealth()/m.getMaxHealth());
		}
	}

	private void resetToSpawn() {
		if (targetlist.size()==0 && m.getLocation().distanceSquared(myspawn)>81) {
			while (myspawn.getBlock().getRelative(0, -1, 0).getType()==Material.AIR && myspawn.getY()>1) {
				myspawn = myspawn.add(0,-1,0);
			}
			m.teleport(myspawn);
			m.setHealth(m.getMaxHealth());
		}
	}

	private void dontDrown() {
		m.setRemainingAir(m.getMaximumAir());
	}

	private void reapplyGlow() {
		if (last_applyglow_time+GLOW_TIME<=TwosideKeeper.getServerTickTime()) {
			GlowAPI.Color col = GlowAPI.Color.DARK_PURPLE;
			if (m.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
				col = GlowAPI.Color.YELLOW;
			}
			if (storingenergy) {
				col = GlowAPI.Color.GREEN;
			}
			GenericFunctions.setGlowing(m, col);
		}
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
		Player p = ChooseRandomTarget();
		if (p!=null) {
			if (Math.random()<=0.2 && !p.isOnGround()) {
				//p.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION,20*5,-31));
				p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP,20*5,-1));
				p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,10*1,7));
				m.setTarget(p);
				p.setFlying(false);
				p.setVelocity(new Vector(0,-1,0));
				p.removePotionEffect(PotionEffectType.LEVITATION);
				p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION,(int)(20*2.25),0));
				p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_FALL, 0.4f, 0.8f);
				p.playSound(p.getLocation(), Sound.ENTITY_MAGMACUBE_SQUISH, 1.0f, 1.0f);
			}
		}
	}

	private void weakenTeam() {
		if (last_burstcheck_time+BURST_TIME<=TwosideKeeper.getServerTickTime()) {
			if (hp_before_burstcheck-BURST_LIMIT>m.getHealth()) {
				//Apply a Weakness debuff aura based on how much stronger the team is.
				int weaknesslv = Math.min(8,(int)((hp_before_burstcheck-BURST_LIMIT)/BURST_LIMIT));
				createWeaknessCloud(m.getLocation(),weaknesslv);
			}
			last_burstcheck_time=TwosideKeeper.getServerTickTime();
			hp_before_burstcheck=m.getHealth();
		}
	}

	private void createWeaknessCloud(Location loc, int weaknesslv) {
		AreaEffectCloud lp = (AreaEffectCloud)loc.getWorld().spawnEntity(loc, EntityType.AREA_EFFECT_CLOUD);
		lp.setColor(Color.BLACK);
		DecimalFormat df = new DecimalFormat("0.00");
		lp.setCustomName("WEAK "+weaknesslv+" "+WEAKNESS_DURATION);
		lp.setRadius(2f);
		lp.setRadiusPerTick(0.5f/20);
		lp.setDuration(20*5);
		lp.setReapplicationDelay(20);
		lp.setBasePotionData(new PotionData(PotionType.POISON));
		lp.setParticle(Particle.SPELL);
		loc.getWorld().playSound(loc, Sound.ENTITY_HOSTILE_SPLASH, 1.0f, 1.0f);
	}

	private void regenerateHealth() {
		if (m.getHealth()<m.getMaxHealth() && last_regen_time+RESTORE_HEALTH<=TwosideKeeper.getServerTickTime()) {
			m.setHealth(Math.min(m.getHealth()+1,m.getMaxHealth()));
		}
	}

	private void moveFasterToTarget() {
		if (m.isInsideVehicle()) {
			m.eject();
		}
		LivingEntity l = m.getTarget();
		if (l!=null) {
			if (l.isDead()) {
				targetlist.remove(l);
				if (targetlist.size()>0) {
					m.setTarget(ChooseRandomTarget());
				} else {
					m.setTarget(null);
					resetToSpawn();
				}
			}
			if (!storingenergy) {
				if (l.getLocation().distanceSquared(m.getLocation())>4096) {
					//Lose the target.
					targetlist.remove(l);
					if (targetlist.size()>0) {
						m.setTarget(ChooseRandomTarget());
					} else {
						m.setTarget(null);
						resetToSpawn();
					}
				} else
				if (l.getLocation().distanceSquared(m.getLocation())>100 && !leaping) {
					l.getWorld().playSound(l.getLocation(), Sound.ENTITY_CAT_HISS, 1.0f, 1.0f);
					chasing=true;
					Bukkit.getScheduler().scheduleSyncDelayedTask(TwosideKeeper.plugin, new Runnable() {
						public void run() {
							m.teleport(l.getLocation().add(Math.random(),Math.random(),Math.random()));
							l.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,20*5,7));
							l.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,10*1,7));
							chasing=false;
						}
					},20*2);
				} else if (l.getLocation().distanceSquared(m.getLocation())>4) {
					m.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(FAST_MOVE_SPD);
				} else {
					m.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(DEFAULT_MOVE_SPD);
				}
			}
			
			if (l.getLocation().getY()>m.getLocation().getY()+1) {
				//Jump up to compensate. Move towards the player too.
				m.setVelocity((m.getLocation().getDirection()).add(new Vector(0,0.2*(l.getLocation().getY()-m.getLocation().getY()),0)));
			}
		} else {
			targetlist.remove(l);
			m.setTarget(null);
		}
	}

	private void rebuff() {
		if (last_rebuff_time+REFRESH_BUFFS<=TwosideKeeper.getServerTickTime()) {
			last_rebuff_time=TwosideKeeper.getServerTickTime();
			m.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,Integer.MAX_VALUE,8),true);
			m.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,Integer.MAX_VALUE,8),true);
			//m.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,Integer.MAX_VALUE,2),true);
			ItemStack helm = new ItemStack(Material.GOLD_HELMET);
			m.getEquipment().setHelmet(helm);
			m.getEquipment().setHelmet(Loot.GenerateMegaPiece(helm.getType(), true, true, 1));
			m.getEquipment().setHelmetDropChance(1.0f);
			if (!enraged) {
				if (m.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
					m.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
				}
			}
		}
	}
	
	//Triggers when this mob is hit.
	public void runHitEvent(LivingEntity damager) {
		if (!targetlist.contains(damager) && (damager instanceof Player)) {
			targetlist.add((Player)damager);
		}
		if (damager instanceof Player) {
			Player p = (Player)damager;
			p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS,20*2,9),true);
		}
		last_regen_time=TwosideKeeper.getServerTickTime();
		double randomrate = 0d;
		if (!chasing && NewCombat.getPercentHealthRemaining(m)<=50) {
			if (last_leap_time+LEAP_COOLDOWN<=TwosideKeeper.getServerTickTime()) {
				performLeap();
			}
		}
		if (NewCombat.getPercentHealthRemaining(m)<=25) {
			if (!leaping && !chasing &&
					last_storingenergy_time+STORINGENERGY_COOLDOWN<=TwosideKeeper.getServerTickTime()) {
				last_storingenergy_time=TwosideKeeper.getServerTickTime();
				storingenergy=true;
				for (int i=0;i<targetlist.size();i++) {
					targetlist.get(i).sendMessage(ChatColor.GOLD+"The "+m.getCustomName()+ChatColor.GOLD+" is absorbing energy!");
				}
				m.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0f);
				last_storingenergy_health=m.getHealth();
				Bukkit.getScheduler().scheduleSyncDelayedTask(TwosideKeeper.plugin, new Runnable() {
					public void run() {
						Player target = ChooseRandomTarget();
						if (target!=null) {
							if (last_storingenergy_health-m.getHealth()>0) {
								storingenergy_hit=(last_storingenergy_health-m.getHealth())*1000d;
								for (int i=0;i<targetlist.size();i++) {
									targetlist.get(i).sendMessage(ChatColor.GOLD+"The "+m.getCustomName()+ChatColor.GOLD+"'s next hit is stronger!");
									targetlist.get(i).sendMessage(ChatColor.DARK_RED+""+ChatColor.ITALIC+" \"DIE "+target.getName()+ChatColor.DARK_RED+"! DIEE!\"");
								}
								m.setTarget(target);
								storingenergy=false;
							}
						}
					}
				},5*20);
			}
		}
		if (NewCombat.getPercentHealthRemaining(m)<=10) {
			if (last_enrage_time+ENRAGE_COOLDOWN<=TwosideKeeper.getServerTickTime()) {
				last_enrage_time=TwosideKeeper.getServerTickTime();
				for (int i=0;i<targetlist.size();i++) {
					targetlist.get(i).sendMessage(ChatColor.BOLD+""+ChatColor.YELLOW+"WARNING!"+ChatColor.RESET+ChatColor.GREEN+"The "+m.getCustomName()+ChatColor.GREEN+" is going into a tantrum!");
				}
				Bukkit.getScheduler().scheduleSyncDelayedTask(TwosideKeeper.plugin, new Runnable() {
					public void run() {
						if (!m.isDead()) {
							for (int i=0;i<targetlist.size();i++) {
								targetlist.get(i).sendMessage(ChatColor.RED+"The "+m.getCustomName()+ChatColor.RED+" becomes much stronger!");
							}
							enraged=true;
							if (m.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
								m.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,ENRAGE_COOLDOWN*2,GenericFunctions.getPotionEffectLevel(PotionEffectType.INCREASE_DAMAGE, m)+15));
							} else {
								m.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,ENRAGE_COOLDOWN*2,14));
							}
						}
					}},20*20);
			}
		}
		if (NewCombat.getPercentHealthRemaining(m)<=75 &&
				NewCombat.getPercentHealthRemaining(m)>50) {
			randomrate = 1/16d;
		} else
		if (NewCombat.getPercentHealthRemaining(m)<=50 &&
			NewCombat.getPercentHealthRemaining(m)>25) {
			randomrate = 1/8d;
		} else
		{
			randomrate = 1/4d;
		}
		if (Math.random()<=randomrate) {
			EntityType choice = null;
			switch ((int)(Math.random()*4)) {
				case 0 :{
					choice = EntityType.ZOMBIE;
				}break;
				case 1 :{
					choice = EntityType.SKELETON;
				}break;
				case 2 :{
					choice = EntityType.CREEPER;
				}break;
				case 3 :{
					choice = EntityType.ENDERMAN;
				}break;
				default:{
					choice = EntityType.ZOMBIE;
				}
			}
			Monster nm = (Monster)m.getWorld().spawnEntity(getNearbyFreeLocation(m.getLocation()),choice);
			Player target = targetlist.get((int)(Math.random() * targetlist.size()));
			nm.setTarget(target);
			MonsterController.convertMonster(nm, MonsterDifficulty.HELLFIRE);
		}
		if (NewCombat.getPercentHealthRemaining(m)<10) {
			Player target = targetlist.get((int)(Math.random() * targetlist.size()));
			Creeper nm = (Creeper)m.getWorld().spawnEntity(target.getLocation().add(0,30,0),EntityType.CREEPER);
			if (Math.random()<=0.5) {
				nm.setPowered(true);
			}
			nm.setTarget(target);
			MonsterController.convertMonster(nm, MonsterDifficulty.HELLFIRE);
		}
	}
	
	private void performLeap() {
		last_leap_time = TwosideKeeper.getServerTickTime();
		int radius = (int)(6*(NewCombat.getPercentHealthMissing(m)/100d));
		//Choose a target randomly.
		Player target = ChooseRandomTarget();
		m.setTarget(target);
		Bukkit.getScheduler().scheduleSyncDelayedTask(TwosideKeeper.plugin, new Runnable() {
			public void run() {
				m.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION,Integer.MAX_VALUE,60));
			}
		},8);
		target_leap_loc = target.getLocation();
		m.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION,Integer.MAX_VALUE,20));
		for (int x=-radius;x<radius+1;x++) {
			for (int z=-radius;z<radius+1;z++) {
				Block b = target.getLocation().add(x,-0.9,z).getBlock();
				if (b.getType()!=Material.AIR && aPlugin.API.isDestroyable(b) && GenericFunctions.isSoftBlock(b)) {
					storedblocks.put(b, b.getType());
					b.setType(Material.STAINED_GLASS);
					b.setData((byte)4);
				}
			}
		}
		leaping=true;
		Bukkit.getScheduler().scheduleSyncDelayedTask(TwosideKeeper.plugin, new Runnable() {
			public void run() {
				restoreBlocks();
				m.teleport(target_leap_loc);
				leaping=false;
				m.removePotionEffect(PotionEffectType.LEVITATION);
			}

			private void restoreBlocks() {
				for (Block b : storedblocks.keySet()) {
					FallingBlock fb = (FallingBlock)b.getLocation().getWorld().spawnFallingBlock(b.getLocation(), storedblocks.get(b), b.getData());
					fb.setMetadata("FAKE", new FixedMetadataValue(TwosideKeeper.plugin,true));
					fb.setVelocity(new Vector(0,Math.random()*1.7,0));
					//b.setType(storedblocks.get(b));
					b.setType(Material.AIR);
					aPlugin.API.sendSoundlessExplosion(target_leap_loc, 4);
					b.getLocation().getWorld().playSound(b.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 0.7f, 1.2f);
				}
				storedblocks.clear();
				GenericFunctions.DealDamageToNearbyPlayers(target_leap_loc, 5, radius, true, 2, m, true);
				//GenericFunctions.getNear
			}
		},(int)(((20*4)*(NewCombat.getPercentHealthRemaining(m)/100d))+10));
	}

	private Player ChooseRandomTarget() {
		if (targetlist.size()>0) {
			Player p = targetlist.get((int)(Math.random() * targetlist.size()));
			p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,10*1,7));
			m.setTarget(p);
			return p;
		} else {
			return null;
		}
	}

	private Location getNearbyFreeLocation(Location l) {
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
		if (!targetlist.contains(ent) && (ent instanceof Player)) {
			targetlist.add((Player)ent);
		}
		if (ent.hasPotionEffect(PotionEffectType.POISON)) {
			int poisonlv = GenericFunctions.getPotionEffectLevel(PotionEffectType.POISON, ent);
			ent.addPotionEffect(new PotionEffect(PotionEffectType.POISON,POISON_DURATION,poisonlv+1),true);
			ent.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING,POISON_DURATION,poisonlv+1));
		} else {
			ent.addPotionEffect(new PotionEffect(PotionEffectType.POISON,POISON_DURATION,0));
			ent.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING,POISON_DURATION,0));
		}
		if (ent instanceof Player) {
			Player p = (Player)ent;
			if (storingenergy_hit>0) {
				p.playSound(p.getLocation(), Sound.ENTITY_ZOMBIE_BREAK_DOOR_WOOD, 1.0f, 1.0f);
				p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION,20*4,0));
				TwosideKeeper.log("Got hit for "+storingenergy_hit+" damage!", 2);
				TwosideKeeperAPI.DealDamageToEntity(NewCombat.CalculateDamageReduction(storingenergy_hit,p,m),p,m);
				storingenergy_hit=0;
			}
		}
	}
	
	public Monster getMonster() {
		return m;
	}
	
	public List<Player> getTargetList() {
		return targetlist;
	}
	
	public void randomlyTeleport() {
		Location l = getNearbyFreeLocation(m.getLocation(),24);
		m.teleport(l);
	}

	public void removeAllHealthbars() {
		bar.removeAll();
	}
}
