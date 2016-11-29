package sig.plugin.TwosideKeeper.Boss;

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

import sig.plugin.TwosideKeeper.CustomDamage;
import sig.plugin.TwosideKeeper.EliteMonster;
import sig.plugin.TwosideKeeper.MonsterController;
import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.HelperStructures.Loot;
import sig.plugin.TwosideKeeper.HelperStructures.MonsterDifficulty;
import sig.plugin.TwosideKeeper.HelperStructures.PlayerMode;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.SoundUtils;

public class EliteZombie extends EliteMonster{
	static int REFRESH_BUFFS = 20*30;
	static float FAST_MOVE_SPD = 0.65f;
	static long BURST_TIME = 20*3;
	static float BURST_LIMIT = 10f;
	static int WEAKNESS_DURATION = 20*10;
	static int POISON_DURATION = 20*10;
	static int LEAP_COOLDOWN = 20*40;
	static int ENRAGE_COOLDOWN = 20*60;
	static int STORINGENERGY_COOLDOWN = 20*50;
	static int WILLPOWER_COOLDOWN = 20*50;
	static int GLOW_TIME = 20*1;
	
	long last_rebuff_time=0;
	long last_burstcheck_time=0;
	long last_applyglow_time=0;
	long last_willpower_increase=0;
	double last_leap_time=0;
	double last_enrage_time=0;
	double last_storingenergy_time=0;
	double last_storingenergy_health=0;
	double storingenergy_hit=0;
	Location target_leap_loc = null;
	HashMap<Block,Material> storedblocks = new HashMap<Block,Material>();
	HashMap<Block,Byte> storedblockdata = new HashMap<Block,Byte>();

	public EliteZombie(Monster m) {
		super(m);
	}
	
	public void runTick() {
		//This monster constantly gives itself its buffs as it may lose some (Debilitation mode).
		increaseBarTextScroll();
		dontDrown();
		rebuff();
		regenerateHealth();
		moveFasterToTarget();
		resetToSpawn();
		createBossHealthbar();
		ignoreAllOtherTargets();
		if (m.isValid() && targetlist.size()>0) {
			adjustWillpower();
			weakenTeam();
			retargetInAir();
			destroyLiquids(2);
			getGlow();
		}
	}

	protected void createBossHealthbar() {
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
		currentplayers = willpower_bar.getPlayers();
		if ((last_willpower_increase+20*3)>TwosideKeeper.getServerTickTime()) {
			for (int i=0;i<targetlist.size();i++) {
				if (!currentplayers.contains(targetlist.get(i))) {
					willpower_bar.addPlayer(targetlist.get(i));
				}
			}
		} else {
			willpower_bar.removeAll();
		}
		willpower_bar.setProgress(Math.min(willpower/100d,1));
	}

	protected void weakenTeam() {
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

	protected void createWeaknessCloud(Location loc, int weaknesslv) {
		AreaEffectCloud lp = (AreaEffectCloud)loc.getWorld().spawnEntity(loc, EntityType.AREA_EFFECT_CLOUD);
		lp.setColor(Color.BLACK);
		lp.setCustomName("WEAK "+weaknesslv+" "+WEAKNESS_DURATION);
		lp.setRadius(2f);
		lp.setRadiusPerTick(0.5f/20);
		lp.setDuration(20*5);
		lp.setReapplicationDelay(20);
		lp.setBasePotionData(new PotionData(PotionType.POISON));
		lp.setParticle(Particle.SPELL);
		SoundUtils.playGlobalSound(loc, Sound.ENTITY_HOSTILE_SPLASH, 1.0f, 1.0f);
	}

	private void adjustWillpower() {
		//Check for nearby mobs. Each mob increases willpower by 1.
		if (Math.random()<=0.3 && !leaping && !storingenergy) {
			int mobcount=0;
			List<Monster> monsterlist = CustomDamage.trimNonMonsterEntities(m.getNearbyEntities(10, 10, 10));
			mobcount=monsterlist.size()-1;
			TwosideKeeper.log("Detected mob count: "+mobcount, 5);
			if (mobcount>0) {
				willpower+=mobcount;
				last_willpower_increase=TwosideKeeper.getServerTickTime();
				if (!first_willpower_notification && willpower>20) {
					for (int i=0;i<targetlist.size();i++) {
						targetlist.get(i).sendMessage(ChatColor.ITALIC+"The "+GenericFunctions.getDisplayName(m)+ChatColor.RESET+ChatColor.ITALIC+" gains morale and the will to fight from its minions!");
					}
					first_willpower_notification=true;
				}
				if (willpower>=100) {
					for (int i=0;i<targetlist.size();i++) {
						targetlist.get(i).sendMessage(ChatColor.RED+"The "+GenericFunctions.getDisplayName(m)+ChatColor.RED+" unleashes its Willpower!");
					}
					if (m.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
						final int previous_str_level = GenericFunctions.getPotionEffectLevel(PotionEffectType.INCREASE_DAMAGE, m);
						final int previous_str_duration = GenericFunctions.getPotionEffectDuration(PotionEffectType.INCREASE_DAMAGE, m);
						m.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,20*5,previous_str_level+15),true);	
						Bukkit.getScheduler().scheduleSyncDelayedTask(TwosideKeeper.plugin, new Runnable() {
							public void run() {
								m.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,previous_str_duration,previous_str_level),true);
							}
						},50);					
					} else {
						m.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,20*5,14),true);
					}
					Bukkit.getScheduler().scheduleSyncDelayedTask(TwosideKeeper.plugin, new Runnable() {
						public void run() {
							for (int i=0;i<targetlist.size();i++) {
								targetlist.get(i).sendMessage(ChatColor.DARK_RED+"The "+GenericFunctions.getDisplayName(m)+ChatColor.DARK_RED+" is now focused on its target!");
							}	
							my_only_target = ChooseRandomTarget();
							last_ignoretarget_time = TwosideKeeper.getServerTickTime();
						}
					},50);	
					Bukkit.getScheduler().scheduleSyncDelayedTask(TwosideKeeper.plugin, new Runnable() {
						public void run() {
							performLeap();
							last_leap_time=0;
							Bukkit.getScheduler().scheduleSyncDelayedTask(TwosideKeeper.plugin, new Runnable() {
								public void run() {
									performLeap();
									last_leap_time=0;
									Bukkit.getScheduler().scheduleSyncDelayedTask(TwosideKeeper.plugin, new Runnable() {
										public void run() {
											performLeap();
											last_leap_time=0;
											Bukkit.getScheduler().scheduleSyncDelayedTask(TwosideKeeper.plugin, new Runnable() {
												public void run() {
													performLeap();
													last_leap_time=0;
													Bukkit.getScheduler().scheduleSyncDelayedTask(TwosideKeeper.plugin, new Runnable() {
														public void run() {
															performLeap();
															last_leap_time=0;
														}
													},5);
												}
											},5);
										}
									},5);
								}
							},10);
						}
					},10);
					willpower=0;
				}
			} else {
				if (willpower>0) {
					willpower--;
				}
			}
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

	private void moveFasterToTarget() {
		if (m.isInsideVehicle()) {
			m.eject();
		}
		LivingEntity l = m.getTarget();
		if (l!=null && l.getWorld().equals(m.getWorld())) {
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
				if (storingenergy_hit>0) {
					storingenergy_hit/=1.04f;
					if (storingenergy_hit<10) {
						storingenergy_hit=0;
					}
				} 
				if (l.getLocation().distanceSquared(m.getLocation())>4096 && !leaping) {
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
					SoundUtils.playGlobalSound(l.getLocation(), Sound.ENTITY_CAT_HISS, 1.0f, 1.0f);
					chasing=true;
					Bukkit.getScheduler().scheduleSyncDelayedTask(TwosideKeeper.plugin, new Runnable() {
						public void run() {
							m.teleport(l.getLocation().add(Math.random(),Math.random(),Math.random()));
							l.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,20*5,7));
							l.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,20*1,7));
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
			ItemStack helm = new ItemStack(Material.GOLD_AXE);
			m.getEquipment().setItemInMainHand(helm);
			m.getEquipment().setItemInMainHandDropChance(1.0f);
			helm = new ItemStack(Material.GOLD_HELMET);
			m.getEquipment().setHelmet(helm);
			m.getEquipment().setHelmet(Loot.GenerateMegaPiece(helm.getType(), true, true, 1));
			m.getEquipment().setHelmetDropChance(1.0f);
			if (!leaping) {
				m.removePotionEffect(PotionEffectType.LEVITATION);
			}
			if (!enraged) {
				if (m.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
					m.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
				}
			}
		}
	}
	
	//Triggers when this mob is hit.
	public void runHitEvent(LivingEntity damager, double dmg) {
		bar.setColor(BarColor.RED);
		if (!targetlist.contains(damager) && (damager instanceof Player)) {
			targetlist.add((Player)damager);
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
			if ((!p.hasPotionEffect(PotionEffectType.WEAKNESS) || GenericFunctions.getPotionEffectLevel(PotionEffectType.WEAKNESS, p)<9) &&
					!PlayerMode.isRanger(p)) {
				GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.WEAKNESS,35,9,p,true);
			}
		}
		last_regen_time=TwosideKeeper.getServerTickTime();
		double randomrate = 0d;
		if (!chasing && CustomDamage.getPercentHealthRemaining(m)<=50) {
			if (last_leap_time+LEAP_COOLDOWN<=TwosideKeeper.getServerTickTime()) {
				performLeap();
			}
		}
		if (CustomDamage.getPercentHealthRemaining(m)<=25) {
			if (!leaping && !chasing &&
					last_storingenergy_time+STORINGENERGY_COOLDOWN<=TwosideKeeper.getServerTickTime()) {
				last_storingenergy_time=TwosideKeeper.getServerTickTime();
				storingenergy=true;
				for (int i=0;i<targetlist.size();i++) {
					targetlist.get(i).sendMessage(ChatColor.GOLD+"The "+GenericFunctions.getDisplayName(m)+ChatColor.GOLD+" is absorbing energy!");
				}
				m.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0f);
				Bukkit.getScheduler().scheduleSyncDelayedTask(TwosideKeeper.plugin, new Runnable() {
					public void run() {
						last_storingenergy_health=m.getHealth();
					}},20*1);
				Bukkit.getScheduler().scheduleSyncDelayedTask(TwosideKeeper.plugin, new Runnable() {
					public void run() {
						Player target = ChooseRandomTarget();
						if (target!=null) {
							if (last_storingenergy_health-m.getHealth()>0) {
								storingenergy_hit=(last_storingenergy_health-m.getHealth())*500d;
								for (int i=0;i<targetlist.size();i++) {
									targetlist.get(i).sendMessage(ChatColor.GOLD+"The "+GenericFunctions.getDisplayName(m)+ChatColor.GOLD+"'s next hit is stronger!");
									targetlist.get(i).sendMessage(ChatColor.DARK_RED+""+ChatColor.ITALIC+" \"DIE "+target.getName()+ChatColor.DARK_RED+"! DIEE!\"");
								}
								m.setTarget(target);
								storingenergy=false;
							} else {
								storingenergy=false;
							}
						} else {
							storingenergy=false;
						}
					}
				},6*20);
			}
		}
		if (CustomDamage.getPercentHealthRemaining(m)<=10) {
			if (last_enrage_time+ENRAGE_COOLDOWN<=TwosideKeeper.getServerTickTime()) {
				last_enrage_time=TwosideKeeper.getServerTickTime();
				for (int i=0;i<targetlist.size();i++) {
					targetlist.get(i).sendMessage(ChatColor.BOLD+""+ChatColor.YELLOW+"WARNING!"+ChatColor.RESET+ChatColor.GREEN+"The "+GenericFunctions.getDisplayName(m)+ChatColor.GREEN+" is going into a tantrum!");
				}
				Bukkit.getScheduler().scheduleSyncDelayedTask(TwosideKeeper.plugin, new Runnable() {
					public void run() {
						if (!m.isDead()) {
							for (int i=0;i<targetlist.size();i++) {
								targetlist.get(i).sendMessage(ChatColor.RED+"The "+GenericFunctions.getDisplayName(m)+ChatColor.RED+" becomes much stronger!");
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
		if (CustomDamage.getPercentHealthRemaining(m)<=75 &&
				CustomDamage.getPercentHealthRemaining(m)>50) {
			randomrate = 1/16d;
		} else
		if (CustomDamage.getPercentHealthRemaining(m)<=50 &&
				CustomDamage.getPercentHealthRemaining(m)>25) {
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
		if (CustomDamage.getPercentHealthRemaining(m)<10) {
			Player target = targetlist.get((int)(Math.random() * targetlist.size()));
			Creeper nm = (Creeper)m.getWorld().spawnEntity(target.getLocation().add(0,30,0),EntityType.CREEPER);
			if (Math.random()<=0.5) {
				nm.setPowered(true);
			}
			nm.setTarget(target);
			MonsterController.convertMonster(nm, MonsterDifficulty.HELLFIRE);
		}
	}
	
	@SuppressWarnings("deprecation")
	private void performLeap() {
		last_leap_time = TwosideKeeper.getServerTickTime();
		int radius = (int)(6*(CustomDamage.getPercentHealthMissing(m)/100d))+1;
		//Choose a target randomly.
		Player target = ChooseRandomTarget();
		m.setTarget(target);
		Bukkit.getScheduler().scheduleSyncDelayedTask(TwosideKeeper.plugin, new Runnable() {
			public void run() {
				m.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION,Integer.MAX_VALUE,60));
			}
		},8);
		target_leap_loc = target.getLocation().clone();
		m.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION,Integer.MAX_VALUE,20));
		for (int x=-radius;x<radius+1;x++) {
			for (int z=-radius;z<radius+1;z++) {
				Block b = target.getLocation().add(x,-0.9,z).getBlock();
				int origy = b.getLocation().getBlockY();
				while ((b.getType()==Material.AIR ^ b.isLiquid()) || (b.getRelative(0, 1, 0).getType()!=Material.AIR && !b.getRelative(0,1,0).isLiquid())
						&& b.getLocation().getBlockY()>0) {
					if (b.getRelative(0, 1, 0).getType()!=Material.AIR && !b.getRelative(0,1,0).isLiquid()) {
						b = b.getRelative(0, 1, 0); //Try going up, not down.
					} else {
						b = b.getRelative(0, -1, 0);
					}
					if (Math.abs(b.getLocation().getBlockY()-origy)>4) {
						break;
					}
				}
				TwosideKeeper.log("Selected block "+b.toString(), 5);
				if (!aPlugin.API.isExplosionProof(b) && b.getType()!=Material.STAINED_GLASS) {
					Material type = b.getType();
					Byte data = b.getData();
					storedblocks.put(b, type);
					storedblockdata.put(b, data);
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
					if (GenericFunctions.isSoftBlock(storedblocks.get(b))) {
						FallingBlock fb = (FallingBlock)b.getLocation().getWorld().spawnFallingBlock(b.getLocation(), storedblocks.get(b), storedblockdata.get(b));
						fb.setMetadata("FAKE", new FixedMetadataValue(TwosideKeeper.plugin,true));
						fb.setVelocity(new Vector(0,Math.random()*1.7,0));
						b.setType(Material.AIR);
					} else {
						b.setType(storedblocks.get(b));
					}
					aPlugin.API.sendSoundlessExplosion(target_leap_loc, 4);
					SoundUtils.playGlobalSound(b.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 0.7f, 1.2f);
				}
				storedblocks.clear();
				SoundUtils.playGlobalSound(m.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 0.7f, 1.2f);
				List<Player> nearbyplayers = GenericFunctions.getNearbyPlayers(target_leap_loc, radius);
				for (int i=0;i<nearbyplayers.size();i++) {
					GenericFunctions.removeNoDamageTick(nearbyplayers.get(i), m);
					boolean applied = CustomDamage.ApplyDamage(1000, m, nearbyplayers.get(i), null, "Leap", CustomDamage.NONE);
					if (applied && nearbyplayers.get(i).getHealth()>0) {
						nearbyplayers.get(i).setVelocity(new Vector(0,2,0));
					}
				}
				//GenericFunctions.DealDamageToNearbyPlayers(target_leap_loc, 5*200, radius, true, 2, m, "Leap",false);
				//GenericFunctions.getNear
			}
		},(int)(((20*3)*(CustomDamage.getPercentHealthRemaining(m)/100d))+30));
	}

	//Triggers when this mob hits something.
	public void hitEvent(LivingEntity ent) {
		if (!targetlist.contains(ent) && (ent instanceof Player)) {
			targetlist.add((Player)ent);
		}
		if (Math.random()<=0.33) {
			if (ent.hasPotionEffect(PotionEffectType.POISON)) {
				int poisonlv = GenericFunctions.getPotionEffectLevel(PotionEffectType.POISON, ent);
				ent.addPotionEffect(new PotionEffect(PotionEffectType.POISON,POISON_DURATION,poisonlv+1),true);
				ent.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING,POISON_DURATION,poisonlv+1));
			} else {
				ent.addPotionEffect(new PotionEffect(PotionEffectType.POISON,POISON_DURATION,0));
				ent.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING,POISON_DURATION,0));
			}
		}
		if (ent instanceof Player) {
			Player p = (Player)ent;
			if (storingenergy_hit>0) {
				SoundUtils.playGlobalSound(p.getLocation(), Sound.ENTITY_ZOMBIE_BREAK_DOOR_WOOD, 1.0f, 1.0f);
				GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.CONFUSION,20*4,0,p);
				TwosideKeeper.log("Got hit for "+storingenergy_hit+" damage!", 2);
				GenericFunctions.removeNoDamageTick(p, m);
				if (CustomDamage.ApplyDamage(storingenergy_hit, m, p, null, "Stored Energy", CustomDamage.IGNOREDODGE)) {
					//TwosideKeeperAPI.DealDamageToEntity(.CalculateDamageReduction(storingenergy_hit,p,m),p,m);
					storingenergy_hit=0;
				}
			}
		}
	}
}
