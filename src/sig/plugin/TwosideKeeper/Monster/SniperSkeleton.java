package sig.plugin.TwosideKeeper.Monster;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Endermite;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Skeleton;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.LazyMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.inventivetalent.glow.GlowAPI.Color;

import aPlugin.DropRandomEnchantedBook;
import aPlugin.DropRandomFood;
import sig.plugin.TwosideKeeper.Artifact;
import sig.plugin.TwosideKeeper.Buff;
import sig.plugin.TwosideKeeper.ChargeZombie;
import sig.plugin.TwosideKeeper.CustomDamage;
import sig.plugin.TwosideKeeper.CustomMonster;
import sig.plugin.TwosideKeeper.GlobalLoot;
import sig.plugin.TwosideKeeper.LivingEntityStructure;
import sig.plugin.TwosideKeeper.MonsterController;
import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.TwosideKeeperAPI;
import sig.plugin.TwosideKeeper.Events.EntityChannelCastEvent;
import sig.plugin.TwosideKeeper.HelperStructures.ArtifactItem;
import sig.plugin.TwosideKeeper.HelperStructures.ArtifactItemType;
import sig.plugin.TwosideKeeper.HelperStructures.BuffTemplate;
import sig.plugin.TwosideKeeper.HelperStructures.Channel;
import sig.plugin.TwosideKeeper.HelperStructures.CustomItem;
import sig.plugin.TwosideKeeper.HelperStructures.ItemSet;
import sig.plugin.TwosideKeeper.HelperStructures.LivingEntityDifficulty;
import sig.plugin.TwosideKeeper.HelperStructures.PlayerMode;
import sig.plugin.TwosideKeeper.HelperStructures.Spell;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;
import sig.plugin.TwosideKeeper.HelperStructures.Effects.HighlightCircle;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.DebugUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.EntityUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.MovementUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.SoundUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.Classes.ColoredParticle;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.Classes.MixedDamage;

public class SniperSkeleton extends GenericBoss{
	
	MixedDamage[] BASIC_ATTACK_DAMAGE = new MixedDamage[]{MixedDamage.v(30),MixedDamage.v(50),MixedDamage.v(130, 0.02)};
	final Spell PIERCING_ARROW = new Spell("Piercing Arrow",new int[]{40,20,20},new int[]{240,200,160},new MixedDamage[]{MixedDamage.v(100,0.02),MixedDamage.v(140,0.04),MixedDamage.v(200,0.08)});
	final Spell BURNING_PLUME = new Spell("Burning Plume",new int[]{50,40,30},new int[]{300,240,200},new MixedDamage[]{MixedDamage.v(200,0.06),MixedDamage.v(300,0.08),MixedDamage.v(500,0.15)});
	final Spell CRIPPLING_INFECTION = new Spell("Crippling Infection",new int[]{30,20,20},new int[]{240,240,240});
	final Spell SIPHON_BURST = new Spell("Siphon Burst",new int[]{80,60,40},new int[]{0,0,0},new MixedDamage[]{MixedDamage.v(10,0.06),MixedDamage.v(40,0.1),MixedDamage.v(70,0.15)});
	final Spell ARROW_RAIN = new Spell("Arrow Rain",new int[]{160,160,160},new int[]{800,800,800},new MixedDamage[]{MixedDamage.v(100,0,5),MixedDamage.v(125,0.05,10),MixedDamage.v(150,0.10,20)});
	final Spell MODE_SHIFT = new Spell("Mode Shift",new int[]{20,20,20},new int[]{120,120,120});
	int randomness = 20;
	final Spell ENERGIZEDSHOTS = new Spell("Energized Shots",new int[]{60,40,40},new int[]{0,0,0});
	boolean phaseii = false;
	ShotMode mode = ShotMode.NORMAL;
	
	long lastFiredExtraArrow = 0;
	
	long shotmodeExpireTime = 0;
	
	
	long lastUsedDodge=0;
	final static int[] DODGE_COOLDOWN = new int[]{80,60,40}; 
	final int[] MODE_EXPIRE_TIME = new int[]{120,240,360};
	final static double[] BLOODMITE_HEALTH = new double[]{180,650,1200};
	
	List<LivingEntity> bloodmites = new ArrayList<LivingEntity>();

	public SniperSkeleton(LivingEntity m) {
		super(m);
		LivingEntityStructure les = LivingEntityStructure.GetLivingEntityStructure(m);
		les.setCustomLivingEntityName(m, ChatColor.GOLD+"Sniper Skeleton");
		LivingEntityDifficulty led = MonsterController.getLivingEntityDifficulty(m);
		switch (led) {
			case T1_MINIBOSS:{
				m.setMaxHealth(32000);
			}break;
			case T2_MINIBOSS:{
				m.setMaxHealth(105000);
			}break;
			case T3_MINIBOSS:{
				m.setMaxHealth(376000);
			}break;
		}
		m.setHealth(m.getMaxHealth());
		baseHP = m.getMaxHealth();
		m.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.3625f);
		m.setAI(false);
		m.setRemoveWhenFarAway(false);
		createBossHealthbar();
		setupBow();
		GenericFunctions.logAndRemovePotionEffectFromEntity(PotionEffectType.INVISIBILITY, m);
	}

	private void setupBow() {
		ItemStack bow = new ItemStack(Material.BOW);
		bow.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 10);
		bow.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 10);
		bow.addUnsafeEnchantment(Enchantment.ARROW_KNOCKBACK, 1);
		bow.addUnsafeEnchantment(Enchantment.ARROW_FIRE, 10);
		m.getEquipment().setItemInMainHand(bow);
		m.getEquipment().setItemInMainHandDropChance(0.2f);
	}

	private void createBossHealthbar() {
		healthbar = Bukkit.getServer().createBossBar(GenericFunctions.getDisplayName(m), BarColor.WHITE, BarStyle.SEGMENTED_10, BarFlag.CREATE_FOG);
		healthbar.setProgress(m.getHealth()/m.getMaxHealth());
	}

	public void runTick() {
		super.runTick();
		performSpells();
		updateAI();
		removeIfTooOld();
		giveHatProtectionAndFireResist();
		setModeBackToNormal();
	}
	
	private void setModeBackToNormal() {
		if (mode!=ShotMode.NORMAL &&
				shotmodeExpireTime<=TwosideKeeper.getServerTickTime()) {
			mode=ShotMode.NORMAL;
			LivingEntityStructure.setCustomLivingEntityName(m, "Sniper Skeleton");
		}
	}

	public void runProjectileLaunchEvent(ProjectileLaunchEvent ev) {
		Projectile proj = ev.getEntity();
		proj.setMetadata("SNIPER_"+mode.name(), new FixedMetadataValue(TwosideKeeper.plugin,true));
		if (phaseii && lastFiredExtraArrow+39<=TwosideKeeper.getServerTickTime()) {
			lastFiredExtraArrow=TwosideKeeper.getServerTickTime();
			Bukkit.getScheduler().runTaskLater(TwosideKeeper.plugin, ()->{
				if (m!=null && m.isValid()) {
					Projectile arrow = m.launchProjectile(Arrow.class);
					arrow.setCustomName("MIRRORED");
					//arrow.setMetadata("SNIPER_"+mode.name(), new FixedMetadataValue(TwosideKeeper.plugin,false));
					TwosideKeeper.log(TwosideKeeper.getServerTickTime()+": Shooting arrow", 0);
				}
			}, 20);
		}
	}
	
	private void giveHatProtectionAndFireResist() {
		ItemStack helm = new ItemStack(Material.LEATHER_HELMET);
		LeatherArmorMeta meta = (LeatherArmorMeta)(helm.getItemMeta());
		meta.setColor(org.bukkit.Color.fromRGB(255,255,0));
		helm.setItemMeta(meta);
		m.getEquipment().setHelmet(helm);
	}

	public Color getGlowColor() {
		if (isInIframe()) {
			return Color.GRAY;
		} else 
		if (Channel.isChanneling(m)) {
			return Color.YELLOW;
		} else
		{
			if (GenericFunctions.isSuppressed(m)) {
				return Color.BLACK;
			} else {
				return Color.AQUA;
			}
			/*switch (mode) {
				case NORMAL: return Color.AQUA;
				case POISON: return Color.YELLOW;
				case BLEED: return Color.RED;
				default: return Color.AQUA;
			}*/
		}
	}
	
	private void removeIfTooOld() {
		if (m.getTicksLived()>72000 && !startedfight) {
			m.remove();
		}
	}
	
	private void updateAI() {
		if (distanceToTarget()>75) {
			Monster me = (Monster)m;
			if (me.getTarget()!=null) {
				m.teleport(me.getTarget().getLocation().add(me.getTarget().getLocation().getDirection().multiply(4)));
			} else {
				pickRandomTarget();
			}
		} else
		if (distanceToTarget()>25) {
			m.setVelocity(m.getLocation().getDirection().multiply(0.5f));
		}
		if (!startedfight) {
			m.setAI(false);
		}
	}

	public boolean isImmuneToSuppression() {
		return phaseii;
	}
	
	public void runChannelCastEvent(EntityChannelCastEvent ev) {
		switch (ev.getAbilityName()) {
			case "Piercing Arrow":{
				final int NUMBER_OF_PARTICLES=40;
				final int RANGE = 20;
				Location baseloc = m.getEyeLocation().clone();
				SoundUtils.playGlobalSound(m.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0f, 1.6f);
				for (int i=0;i<NUMBER_OF_PARTICLES;i++) {
					baseloc.add(baseloc.getDirection().multiply((double)RANGE/NUMBER_OF_PARTICLES));
					aPlugin.API.displayEndRodParticle(baseloc, 0, 0, 0, 0, 1);
					MixedDamage dmg = PIERCING_ARROW.getDamageValues()[getDifficultySlot()];
					GenericFunctions.DealDamageToNearbyPlayers(baseloc, dmg.getDmgComponent(), 1, false, true, 0, m, "Piercing Arrow", false, false);
					if (dmg.getTruePctDmgComponent()>0) {GenericFunctions.DealDamageToNearbyPlayers(baseloc, dmg.getTruePctDmgComponent(), 1, false, true, 0, m, "Piercing Arrow", false, true);}
					if (dmg.getTrueDmgComponent()>0) {GenericFunctions.DealDamageToNearbyPlayers(baseloc, dmg.getTrueDmgComponent(), 1, false, true, 0, m, "Piercing Arrow", true, false);}
				}
				PIERCING_ARROW.setLastCastedTime(TwosideKeeper.getServerTickTime());
			}break;
			case "Mode Shift":{
				if (mode!=ShotMode.NORMAL) {
					mode=ShotMode.NORMAL;
				} else {
					if (Math.random()<=0.5) {
						mode=ShotMode.POISON;
						LivingEntityStructure.setCustomLivingEntityName(m, ChatColor.YELLOW+"Poison Skeleton");
					} else {
						mode=ShotMode.BLEED;
						LivingEntityStructure.setCustomLivingEntityName(m, ChatColor.RED+"Blood Skeleton");
					}
				}
				MODE_SHIFT.setLastCastedTime(TwosideKeeper.getServerTickTime());
				shotmodeExpireTime = TwosideKeeper.getServerTickTime()+MODE_EXPIRE_TIME[getDifficultySlot()];
			}break;
			case "Burning Plume":{
				final int RANGE = 12;
				final int PARTICLE_AMT = 100;
				BlockFace facingdir = EntityUtils.getFacingDirection(m);
				Location startingloc = m.getLocation().add(new Vector(facingdir.getModX(),0,facingdir.getModZ()));
				List<Location> firestarters = new ArrayList<Location>();
				int[] size = new int[]{1,3,3};
				for (int i=-size[getDifficultySlot()];i<=size[getDifficultySlot()];i++) {
					BlockFace[] newdir = MovementUtils.get90DegreeDirections(facingdir);
					if (i<0) {
						Location newloc = m.getLocation().add(new Vector(newdir[1].getModX()*Math.abs(i),0,newdir[1].getModZ()*Math.abs(i)));
						firestarters.add(newloc);
					} else 
					if (i>0){
						Location newloc = m.getLocation().add(new Vector(newdir[0].getModX()*Math.abs(i),0,newdir[0].getModZ()*Math.abs(i)));
						firestarters.add(newloc);
					}
				}
				firestarters.add(startingloc);
				Location centerloc = startingloc.clone();
				for (int i=0;i<RANGE;i++) {
					final int amt = i;
					Bukkit.getScheduler().runTaskLater(TwosideKeeper.plugin, ()-> {
						for (Location l : firestarters) {
							Block b = findFireBlock(l);
							//TwosideKeeper.log("Fire starter: "+l+". Block chosen: "+b, 0);
							if (b!=null) {
								b.setType(Material.FIRE);
							}
							l.add(new Vector(facingdir.getModX(),0,facingdir.getModZ()));
							
							for (int j=0;j<PARTICLE_AMT;j++) {
								ColoredParticle.RED_DUST.send(l.clone().add(facingdir.getModX()/2, -10+((double)20/PARTICLE_AMT)*j, facingdir.getModZ()/2), 50, 255, 128, 0);
							}
						}
						MixedDamage dmg = BURNING_PLUME.getDamageValues()[getDifficultySlot()];
						Location dmgloc = centerloc.clone().add(new Vector(facingdir.getModX()*amt,0,facingdir.getModZ()*amt));
						List<Player> players = GenericFunctions.DealDamageToNearbyPlayers(dmgloc, dmg.getDmgComponent(), size[getDifficultySlot()], false, true, 0, m, "Burning Plume", false, false);
						if (dmg.getTruePctDmgComponent()>0) {GenericFunctions.DealDamageToNearbyPlayers(dmgloc, dmg.getTruePctDmgComponent(), size[getDifficultySlot()], false, true, 0, m, "Burning Plume", false, true);}
						if (dmg.getTrueDmgComponent()>0) {GenericFunctions.DealDamageToNearbyPlayers(dmgloc, dmg.getTrueDmgComponent(), size[getDifficultySlot()], false, true, 0, m, "Burning Plume", true, false);}
						for (Player p : players) {
							p.setFireTicks(p.getFireTicks()+(20*10));
						}
						SoundUtils.playGlobalSound(dmgloc, Sound.BLOCK_FURNACE_FIRE_CRACKLE, 1.0f, 1.0f);
					}, (3*i)+3);
				}
				BURNING_PLUME.setLastCastedTime(TwosideKeeper.getServerTickTime());
			}break;
			case "Crippling Infection":{
				BlockFace facingdir = EntityUtils.getFacingDirection(m);
				Location newloc = m.getLocation().add(facingdir.getModX(),0,facingdir.getModZ());
				double dist = newloc.distance(m.getLocation());
				//Arrow a = m.launchProjectile(Arrow.class);
				List<Vector> speeds = new ArrayList<Vector>(); 
				for (BlockFace bf : MovementUtils.get45DegreeDirections(facingdir)) {
					speeds.add(new Vector(bf.getModX(),0,bf.getModZ()).multiply(dist));
				}
				for (BlockFace bf : MovementUtils.get90DegreeDirections(facingdir)) {
					speeds.add(new Vector(bf.getModX(),0,bf.getModZ()).multiply(dist));
				}
				for (int i=0;i<speeds.size();i++) {
					Arrow a = m.launchProjectile(Arrow.class);
					a.setVelocity(speeds.get(i));
					a.setMetadata("SNIPER_CRIPPLINGINFECTION", new FixedMetadataValue(TwosideKeeper.plugin,true));
				}
				Arrow a = m.launchProjectile(Arrow.class);
				a.setMetadata("SNIPER_CRIPPLINGINFECTION", new FixedMetadataValue(TwosideKeeper.plugin,true));
				CRIPPLING_INFECTION.setLastCastedTime(TwosideKeeper.getServerTickTime());
			}break;
			case "Siphon Burst":{
				removeDebuffsAndApplySiphonDamage();
			}break;
			case "Arrow Rain":{
				ARROW_RAIN.setLastCastedTime(TwosideKeeper.getServerTickTime());
			}break;
		}
	}

	private void removeDebuffsAndApplySiphonDamage() {
		MixedDamage dmg = SIPHON_BURST.getDamageValues()[getDifficultySlot()];
		for (Player p : participantlist) {
			int stackamt = 0;
			if (Buff.hasBuff(p, BuffTemplate.BLEEDING)) {
				stackamt += Buff.getBuff(p, BuffTemplate.BLEEDING).getAmplifier();
				Buff.removeBuff(p, BuffTemplate.BLEEDING);
			}
			if (Buff.hasBuff(p, BuffTemplate.INFECTION)) {
				stackamt += Buff.getBuff(p, BuffTemplate.INFECTION).getAmplifier();
				Buff.removeBuff(p, BuffTemplate.INFECTION);
			}
			if (Buff.hasBuff(p, BuffTemplate.POISON)) {
				stackamt += Buff.getBuff(p, BuffTemplate.POISON).getAmplifier();
				Buff.removeBuff(p, BuffTemplate.POISON);
			}
			CustomDamage.ApplyDamage(stackamt*dmg.getDmgComponent(), m, p, null, "Siphon Burst", CustomDamage.IGNORE_DAMAGE_TICK|CustomDamage.IGNOREDODGE);
			if (dmg.getTruePctDmgComponent()>0) {CustomDamage.ApplyDamage(p.getMaxHealth()*(stackamt*dmg.getTruePctDmgComponent()), m, p, null, "Siphon Burst", CustomDamage.TRUEDMG|CustomDamage.IGNORE_DAMAGE_TICK|CustomDamage.IGNOREDODGE);}
			if (dmg.getTrueDmgComponent()>0) {CustomDamage.ApplyDamage((stackamt*dmg.getTrueDmgComponent()), m, p, null, "Siphon Burst", CustomDamage.TRUEDMG|CustomDamage.IGNORE_DAMAGE_TICK|CustomDamage.IGNOREDODGE);}
			SoundUtils.playGlobalSound(p.getLocation(),Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 1.0f, 1.0f);
		}
	}

	private Block findFireBlock(Location l) {
		int diffy = 0;
		while (diffy<=5) {
			Block testb = l.getBlock().getRelative(0, diffy, 0);
			if (testb.getType()==Material.AIR &&
					testb.getRelative(0, -1, 0).getType().isSolid()) {
				return testb;
			}
			if (diffy<=0) {
				diffy--;
				if (diffy<-5) {
					diffy=1;
				}
			} else {
				diffy++;
			}
		}
		return null;
	}

	public void bloodPoolSpawnedEvent(LivingEntity target) {
		Endermite bloodmite = (Endermite)target.getWorld().spawnEntity(target.getLocation(), EntityType.ENDERMITE);
		Bloodmite bm = new Bloodmite(bloodmite);
		bm.setMainEntity(this);
		bm.GetMonster().setMaxHealth(BLOODMITE_HEALTH[getDifficultySlot()]);
		bm.GetMonster().setHealth(bm.GetMonster().getMaxHealth());
		bloodmite.setTarget(pickRandomTarget());
		TwosideKeeper.custommonsters.put(bloodmite.getUniqueId(), bm);
		bloodmites.add(bloodmite);
	}

	protected boolean attemptSpellCast(Spell spell) {
		if (cooldownIsAvailable(spell.getLastCastedTime(),spell)) {
			//Face target.
			Channel.createNewChannel(m, spell.getName(), (int)(spell.getCastTimes()[getDifficultySlot()]));
			Bukkit.getScheduler().runTaskLater(TwosideKeeper.plugin, ()->{FaceTarget(m);}, 5);
			return true;
		}
		return false;
	}
	
	public void cleanup() {
		super.cleanup();
		for (LivingEntity ent : bloodmites) {
			if (ent!=null && ent.isValid()) {
				ent.remove();
			}
		}
	}

	public MixedDamage getBasicAttackDamage() {
		return BASIC_ATTACK_DAMAGE[getDifficultySlot()];
	}
	
	private void FaceTarget(LivingEntity m) {
		if (((Monster)m).getTarget()!=null) {
			Location loc = m.getLocation();
			loc.setDirection(MovementUtils.pointTowardsLocation(loc, ((Monster)m).getTarget().getLocation()));
			m.teleport(loc);
		}
	}
	private boolean cooldownIsAvailable(long spell_timer, Spell spell) {
		return spell_timer+spell.getCooldowns()[getDifficultySlot()]<=TwosideKeeper.getServerTickTime();
	}

	public boolean isInIframe() {
		return m.hasPotionEffect(PotionEffectType.INVISIBILITY) || (Channel.isChanneling(m) && Channel.getCurrentChannel(m).getSpellName().equalsIgnoreCase("Energized Shots"));
	}
	
	private void performDodge() {
		if (lastUsedDodge+DODGE_COOLDOWN[getDifficultySlot()]<=TwosideKeeper.getServerTickTime()) {
			SoundUtils.playGlobalSound(m.getLocation(), Sound.ENTITY_DONKEY_CHEST, 1.0f, 1.0f);
			GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.INVISIBILITY, 20, 0, m, true);
			if (distanceToTarget()<9) {
				m.setVelocity(m.getLocation().getDirection().multiply(-0.4f));
			}
			lastUsedDodge=TwosideKeeper.getServerTickTime();
		}
	}
	
	private double distanceToTarget() {
		Monster me = (Monster)m;
		if (me.getTarget()!=null && me.getTarget().getWorld().equals(m.getWorld())) {
			return m.getLocation().distanceSquared(me.getTarget().getLocation());
		} else {
			return 0;
		}
	}

	private void performSpells() {
		final Runnable[] actions = new Runnable[]{
				()->{performDodge();},
				()->{attemptSpellCast(PIERCING_ARROW);},
				()->{attemptSpellCast(MODE_SHIFT);},
				()->{attemptSpellCast(BURNING_PLUME);},
				()->{attemptSpellCast(CRIPPLING_INFECTION);},
				};
		final Runnable[] actions2 = new Runnable[]{
				()->{performDodge();},
				()->{attemptSpellCast(MODE_SHIFT);},
				()->{attemptSpellCast(CRIPPLING_INFECTION);},
				()->{if (meetsConditionsForSiphon()) {
						attemptSpellCast(SIPHON_BURST);
					}},
				()->{if (attemptSpellCast(ARROW_RAIN)) {
					runArrowRain();}},
				};
		if (canCastSpells()) {
			if (phaseii) {
				for (Runnable r : actions2) {
					if (Math.random()<=1d/actions2.length) {
						Bukkit.getScheduler().runTask(TwosideKeeper.plugin, r);
						break;
					}
				}
			} else {
				performSpellFromFirstPhase(actions);
			}
		}
		if (!phaseii && m.getHealth()<=m.getMaxHealth()/2 && startedfight) {
			if (attemptSpellCast(ENERGIZEDSHOTS)) {
				phaseii=true;
			}
		}
	}
	
	public void announceFailedTakedown() {
		super.announceFailedTakedown();
		if (dpslist.size()>0 && !m.isDead()) {
			phaseii=false;

			for (LivingEntity ent : bloodmites) {
				if (ent!=null && ent.isValid()) {
					ent.remove();
				}
			}
			LivingEntityStructure.setCustomLivingEntityName(m, "Sniper Skeleton");
		}
	}

	private void runArrowRain() {
		new HighlightCircle(m.getLocation(),8,20,ARROW_RAIN.getCastTimes()[getDifficultySlot()]);
		for (int i=0;i<200;i++) {
			int randomTickTime=(int)(Math.random()*ARROW_RAIN.getCastTimes()[getDifficultySlot()]);
			Bukkit.getScheduler().runTaskLater(TwosideKeeper.plugin, 
					()->{
						Arrow a = m.launchProjectile(Arrow.class);
						BlockFace facingdir = EntityUtils.getFacingDirection(m);
						Location newloc = m.getLocation().add(facingdir.getModX(),1,facingdir.getModZ());
						double dist = newloc.distance(m.getLocation());
						a.setVelocity(new Vector(Math.random()*2-1,0.1,Math.random()*2-1).multiply(dist));
						switch ((int)(Math.random()*3)) {
							case 0:{
								a.setMetadata("SNIPER_POISON", new FixedMetadataValue(TwosideKeeper.plugin,true));
							}break;
							case 1:{
								a.setMetadata("SNIPER_BLEED", new FixedMetadataValue(TwosideKeeper.plugin,true));
							}break;
							case 2:{
								a.setMetadata("SNIPER_CRIPPLINGINFECTION", new FixedMetadataValue(TwosideKeeper.plugin,true));
							}break;
						}
						for (int j=0;j<5;j++)
						Bukkit.getScheduler().runTaskLater(TwosideKeeper.plugin, 
								()->{
									a.getWorld().spawnParticle(Particle.CRIT, a.getLocation(), 3);
								},j*1);
					}, randomTickTime);
		}
	}

	private boolean meetsConditionsForSiphon() {
		int debufflevels = 0;
		for (Player p : participantlist) {
			if (Buff.hasBuff(p, "BLEEDING")) {
				debufflevels++;
			}
			if (Buff.hasBuff(p, "INFECTION")) {
				debufflevels++;
			}
			if (Buff.hasBuff(p, "Poison")) {
				debufflevels++;
			}
			if (debufflevels>=2) {
				return true;
			}
		}
		return false;
	}

	private void performSpellFromFirstPhase(final Runnable[] actions) {
		for (Runnable r : actions) {
			if (Math.random()<=1d/actions.length) {
				Bukkit.getScheduler().runTask(TwosideKeeper.plugin, r);
				break;
			}
		}
	}
	
	public static double getDamageReduction() {
		return 0.0;
	}
	
	public static boolean randomlyConvertAsSniperSkeleton(LivingEntity m) {
		return randomlyConvertAsSniperSkeleton(m,false);
	}

	private Player setAggroOnRandomTarget() {
		Player p = pickRandomTarget();
		setAggro((Monster)m,p);
		return p;
	}
	
	public LivingEntityDifficulty getDifficulty() {
		return MonsterController.getLivingEntityDifficulty(m);
	}
	
	public int getDifficultySlot() {
		switch (getDifficulty()) {
			case T1_MINIBOSS:{
				return 0;
			}
			case T2_MINIBOSS:{
				return 1;
			}
			case T3_MINIBOSS:{
				return 2;
			}
			default:{
				TwosideKeeper.log("WARNING! Could not get proper difficulty slot for Difficulty "+getDifficulty()+". Defaulting to slot 0.", 1);
				return 0;
			}
		}
	}
	
	private Player changeAggroToRandomNewTarget() {
		if (Math.random()<=0.5) {
			Monster me = (Monster)m;
			Player newtarget = pickRandomTarget();
			setAggro(me, newtarget);
			return newtarget;
		} else {
			Monster me = (Monster)m;
			return (Player)me.getTarget();
		}
	}

	private void setAggro(Monster me, Player newtarget) {
		if (newtarget!=null) {
			me.setTarget(newtarget);
			LivingEntityStructure les = LivingEntityStructure.GetLivingEntityStructure(m);
			les.SetTarget(me.getTarget());
		}
	}
	
	private Player pickRandomTarget() {
		updateTargetList();
		if (participantlist.size()>0) {
			for (Player p : participantlist) {
				if (Math.random()<=1d/participantlist.size() &&
						!p.isDead() && p.isValid()) {
					return p;
				}
			}
			return participantlist.get(0);
		} else {
			return null;
		}
	}

	private void updateTargetList() {
		for (int i=0;i<participantlist.size();i++) {
			Player p = participantlist.get(i);
			if (p==null || !p.isValid() || p.isDead() ||
					p.getLocation().distanceSquared(m.getLocation())>2500) {
				participantlist.remove(i--);
			}
		}
	}

	private boolean canCastSpells() {
		 return Math.random()<=1/8d && !Buff.hasBuff(m, "SILENCE") && startedfight && !Channel.isChanneling(m) && !m.hasPotionEffect(PotionEffectType.INVISIBILITY);
	}

	public void announceSuccessfulTakedown() {
		if (dpslist.size()>0 && m.isDead()) {
			phaseii=false;
			Bukkit.getServer().broadcastMessage(GenericFunctions.getDisplayName(m)+" Takedown Failed...");
			Bukkit.getServer().broadcastMessage(ChatColor.YELLOW+"DPS Breakdown:");
			Bukkit.getServer().broadcastMessage(generateDPSReport());
			aPlugin.API.discordSendRaw(GenericFunctions.getDisplayName(m)+" Takedown Failed...\n\n"+ChatColor.YELLOW+"DPS Breakdown:"+"\n```\n"+generateDPSReport()+"\n```");
			dpslist.clear();
			healthbar.setColor(BarColor.WHITE);

			for (LivingEntity ent : bloodmites) {
				if (ent!=null && ent.isValid()) {
					ent.remove();
				}
			}
		}
	}
	
	public double getKnockbackMult() {
		return 0.33;
	}

	public static boolean randomlyConvertAsSniperSkeleton(LivingEntity m, boolean force) {
		if ((TwosideKeeper.MINIBOSSES_ACTIVATED &&
				TwosideKeeper.LAST_SPECIAL_SPAWN+(3000/Math.max(Bukkit.getOnlinePlayers().size(),1))<=TwosideKeeper.getServerTickTime() &&
				!m.getWorld().getName().contains("Instance") &&
				Math.random()<=0.015 &&
				TwosideKeeper.elitemonsters.size()==0 &&
				GenericBoss.bossCount()==0) || force) {
			Skeleton s = (Skeleton)m;
			s.setSkeletonType(SkeletonType.NORMAL);
			//Determine distance from Twoside for Difficulty.
			Location compareloc = TwosideKeeper.TWOSIDE_LOCATION;
			if (!compareloc.getWorld().equals(s.getWorld())) {
				compareloc = new Location(s.getWorld(),0,0,0);
			}
			double chancer = compareloc.distanceSquared(m.getLocation());
			if (Math.random()*chancer<4000000) {
				MonsterController.convertLivingEntity(m, LivingEntityDifficulty.T1_MINIBOSS);
			} else
			if (Math.random()*chancer<25000000) {
				MonsterController.convertLivingEntity(m, LivingEntityDifficulty.T2_MINIBOSS);
			} else {
				MonsterController.convertLivingEntity(m, LivingEntityDifficulty.T3_MINIBOSS);
			}
			return true;
		}
		return false;
	}

	public static boolean isSniperSkeleton(LivingEntity m) {
		return m instanceof Skeleton &&
				((Skeleton)m).getSkeletonType()==SkeletonType.NORMAL &&
				(
						MonsterController.getLivingEntityDifficulty(m)==LivingEntityDifficulty.T1_MINIBOSS ||
						MonsterController.getLivingEntityDifficulty(m)==LivingEntityDifficulty.T2_MINIBOSS ||
						MonsterController.getLivingEntityDifficulty(m)==LivingEntityDifficulty.T3_MINIBOSS
				);
	}
	
	enum ShotMode {
		NORMAL,
		POISON,
		BLEED
	}
	


	public void setupBonusLoot() {
		LivingEntityDifficulty diff = MonsterController.getLivingEntityDifficulty(m);
		LivingEntityStructure les = LivingEntityStructure.GetLivingEntityStructure(m);
		GlobalLoot gl = GlobalLoot.spawnGlobalLoot(m.getLocation(), ChatColor.AQUA+""+ChatColor.BOLD+les.getDifficultyAndMonsterName()+ChatColor.AQUA+""+ChatColor.BOLD+" Miniboss Loot");
		double lootrate=1.0;
		for (String s : dpslist.keySet()) {
			UUID id = Bukkit.getOfflinePlayer(s).getUniqueId();
				if (id!=null) {
				PlayerMode mode = getMostUsedPlayerMode(s);
				gl.addNewDropInventory(id,GetSetPiece(diff,mode));
				switch (diff) {
					case T1_MINIBOSS:{
						AttemptRoll(gl, 0.25*lootrate, id, Artifact.createArtifactItem(ArtifactItem.ARTIFACT_ESSENCE, (int)(Math.random()*3)+1));
						AttemptRoll(gl, 0.125*lootrate, id, Artifact.createArtifactItem(ArtifactItem.ARTIFACT_CORE, (int)(Math.random()*3)+1));
						AttemptRoll(gl, 0.0625*lootrate, id, Artifact.createArtifactItem(ArtifactItem.MALLEABLE_BASE, 1));
						AttemptRoll(gl, 0.75*lootrate, id, Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE, (int)(Math.random()*3)+1));
					}break;
					case T2_MINIBOSS:{
						AttemptRoll(gl, 0.5*lootrate, id, GetSetPiece(diff,PlayerMode.values()[(int)(Math.random()*PlayerMode.values().length)]));
						AttemptRoll(gl, 0.5*lootrate, id, Artifact.createArtifactItem(ArtifactItem.ARTIFACT_ESSENCE, (int)(Math.random()*3)+1));
						AttemptRoll(gl, 0.25*lootrate, id, Artifact.createArtifactItem(ArtifactItem.ARTIFACT_CORE, (int)(Math.random()*3)+1));
						AttemptRoll(gl, 0.125*lootrate, id, Artifact.createArtifactItem(ArtifactItem.MALLEABLE_BASE, 1));
						AttemptRoll(gl, 0.75*lootrate, id, Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE, (int)(Math.random()*6)+1));
					}break;
					case T3_MINIBOSS:{
						AttemptRoll(gl, 0.5*lootrate, id, GetSetPiece(diff,PlayerMode.values()[(int)(Math.random()*PlayerMode.values().length)]));
						AttemptRoll(gl, 0.5*lootrate, id, GetSetPiece(diff,PlayerMode.values()[(int)(Math.random()*PlayerMode.values().length)]));
						AttemptRoll(gl, lootrate, id, Artifact.createArtifactItem(ArtifactItem.ARTIFACT_ESSENCE, (int)(Math.random()*3)+1));
						AttemptRoll(gl, 0.5*lootrate, id, Artifact.createArtifactItem(ArtifactItem.ARTIFACT_CORE, (int)(Math.random()*3)+1));
						AttemptRoll(gl, 0.25*lootrate, id, Artifact.createArtifactItem(ArtifactItem.MALLEABLE_BASE, 1));
						AttemptRoll(gl, 0.75*lootrate, id, Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE, (int)(Math.random()*6)+1));
					}break;
				}
				AttemptRoll(gl, 0.02*lootrate, id, GetArtifactRecipe(diff)); 
				AttemptRoll(gl, 0.02*lootrate, id, GetMaterialKit(diff)); 
				AttemptRoll(gl, 0.5*lootrate, id, new DropRandomFood((int)(Math.random()*10)+1,0,0.5,100).getItemStack());
				AttemptRoll(gl, 0.33*lootrate, id, new DropRandomFood((int)(Math.random()*10)+1,0,0.5,500).getItemStack());
				AttemptRoll(gl, 0.1*lootrate, id, new DropRandomFood((int)(Math.random()*10)+1,0,0.5,1000).getItemStack());
				AttemptRoll(gl, 0.5*lootrate, id, new DropRandomEnchantedBook(0,2).getItemStack());
				AttemptRoll(gl, 0.33*lootrate, id, new DropRandomEnchantedBook(0,4).getItemStack());
				AttemptRoll(gl, 0.1*lootrate, id, new DropRandomEnchantedBook(0,6).getItemStack());
				AttemptRoll(gl, 0.15*lootrate, id, TwosideKeeper.HUNTERS_COMPASS.getItemStack());
				AttemptRoll(gl, 0.05*lootrate, id, getVial(diff));
				AttemptRoll(gl, 0.1*lootrate, id, CustomItem.DailyToken());
			}
		}
	}

	private ItemStack getVial(LivingEntityDifficulty diff) {
		switch (diff) {
			case T1_MINIBOSS:{
				return TwosideKeeper.STRENGTHENING_VIAL.getItemStack();
			}
			case T2_MINIBOSS:{
				return TwosideKeeper.LIFE_VIAL.getItemStack();
			}
			case T3_MINIBOSS:{
				return TwosideKeeper.HARDENING_VIAL.getItemStack();
			}
		}
		TwosideKeeper.log("WARNING! Something went terribly wrong while generating material kit. Diff: "+diff, 1);
		DebugUtils.showStackTrace();
		return null;
	}

	private ItemStack GetMaterialKit(LivingEntityDifficulty diff) {
		switch (diff) {
			case T1_MINIBOSS:{
				return CustomItem.IronMaterialKit();
			}
			case T2_MINIBOSS:{
				if (Math.random()<=0.5) {
					return CustomItem.DiamondMaterialKit();
				} else {
					return CustomItem.IronMaterialKit();
				}
			}
			case T3_MINIBOSS:{
				if (Math.random()<=0.33) {
					return CustomItem.IronMaterialKit();
				} else 
				if (Math.random()<=0.5){
					return CustomItem.DiamondMaterialKit();
				} else {
					return CustomItem.GoldMaterialKit();	
				}
			}
		}
		TwosideKeeper.log("WARNING! Something went terribly wrong while generating material kit. Diff: "+diff, 1);
		DebugUtils.showStackTrace();
		return null;
	}

	private ItemStack GetArtifactRecipe(LivingEntityDifficulty diff) {
		ArtifactItemType type = ArtifactItemType.values()[(int)(Math.random()*ArtifactItemType.values().length)];
		switch (diff) {
			case T1_MINIBOSS:{
				return Artifact.createRecipe(4, type);
			}
			case T2_MINIBOSS:{
				return Artifact.createRecipe(5, type);
			}
			case T3_MINIBOSS:{
				if (Math.random()<=0.1) {
					return Artifact.createRecipe(8, type);
				} else {
					return Artifact.createRecipe(7, type);
				}
			}
		}
		TwosideKeeper.log("WARNING! Something went terribly wrong while generating artifact recipe. Diff: "+diff, 1);
		DebugUtils.showStackTrace();
		return null;
	}

	private void AttemptRoll(GlobalLoot loot, double chance, UUID id,
			ItemStack item) {
		double lootamt = chance;
		if (Math.random()<=lootamt) {
			loot.addNewDropInventory(id,item); //Guaranteed Loot Piece.
		}
	}

	private ItemStack GetSetPiece(LivingEntityDifficulty diff, PlayerMode mode) {
		switch (diff) {
			case T1_MINIBOSS:{
				if (mode!=PlayerMode.SLAYER) {
					if (Math.random()<=0.5) {
						return TwosideKeeperAPI.generateSetPiece(Material.IRON_BOOTS, getModeSpecificSet(mode), (Math.random()<=0.1)?true:false, 1);
					} else
					if (Math.random()<=0.7) {
						return TwosideKeeperAPI.generateSetPiece(Material.DIAMOND_BOOTS, getModeSpecificSet(mode), (Math.random()<=0.1)?true:false, 1);
					} else {
						return TwosideKeeperAPI.generateSetPiece(Material.GOLD_BOOTS, getModeSpecificSet(mode), (Math.random()<=0.1)?true:false, 1);
					}
				} else {
					if (Math.random()<=0.4)	{
						if (Math.random()<=0.5) {
							return TwosideKeeperAPI.generateSetPiece(Material.IRON_SWORD, getModeSpecificSet(mode), (Math.random()<=0.1)?true:false, 1);
						} else
						if (Math.random()<=0.7) {
							return TwosideKeeperAPI.generateSetPiece(Material.DIAMOND_SWORD, getModeSpecificSet(mode), (Math.random()<=0.1)?true:false, 1);
						} else {
							return TwosideKeeperAPI.generateSetPiece(Material.GOLD_SWORD, getModeSpecificSet(mode), (Math.random()<=0.1)?true:false, 1);
						}
					} else {
						return TwosideKeeperAPI.generateSetPiece(Material.SKULL_ITEM, ItemSet.WOLFSBANE, (Math.random()<=0.1)?true:false, 1);
					}
				}
			}
			case T2_MINIBOSS:{
				if (Math.random()<=0.05) {
					//Weapon roll.
					return TwosideKeeperAPI.generateSetPiece(Material.IRON_SWORD, getModeSpecificSet(mode), (Math.random()<=0.1)?true:false, 1);
				} else {
					if (mode!=PlayerMode.SLAYER) {
						if (Math.random()<=0.5) {
							return TwosideKeeperAPI.generateSetPiece(Material.IRON_HELMET, getModeSpecificSet(mode), (Math.random()<=0.1)?true:false, 2);
						} else
						if (Math.random()<=0.7) {
							return TwosideKeeperAPI.generateSetPiece(Material.DIAMOND_HELMET, getModeSpecificSet(mode), (Math.random()<=0.1)?true:false, 1);
						} else {
							return TwosideKeeperAPI.generateSetPiece(Material.GOLD_HELMET, getModeSpecificSet(mode), (Math.random()<=0.1)?true:false, 1);
						}
					} else {
						if (Math.random()<=0.4)	{
							if (Math.random()<=0.5) {
								return TwosideKeeperAPI.generateSetPiece(Material.IRON_SWORD, getModeSpecificSet(mode), (Math.random()<=0.1)?true:false, 2);
							} else
							if (Math.random()<=0.7) {
								return TwosideKeeperAPI.generateSetPiece(Material.DIAMOND_SWORD, getModeSpecificSet(mode), (Math.random()<=0.1)?true:false, 1);
							} else {
								return TwosideKeeperAPI.generateSetPiece(Material.GOLD_SWORD, getModeSpecificSet(mode), (Math.random()<=0.1)?true:false, 1);
							}
						} else {
							ItemSet[] baublesets = new ItemSet[]{ItemSet.WOLFSBANE,ItemSet.ALUSTINE};
							return TwosideKeeperAPI.generateSetPiece(Material.SKULL_ITEM, baublesets[(int)(Math.random()*baublesets.length)], (Math.random()<=0.2)?true:false, 2);
						}
					}
				}
			}
			case T3_MINIBOSS:{
				if (Math.random()<=0.10) {
					//Weapon roll.
					switch ((int)(Math.random()*4)) {
						case 0:
						case 3:{
							return TwosideKeeperAPI.generateSetPiece(Material.IRON_SWORD, getModeSpecificSet(mode), (Math.random()<=0.1)?true:false, 3);
						}
						case 1:{
							return TwosideKeeperAPI.generateSetPiece(Material.DIAMOND_SWORD, getModeSpecificSet(mode), (Math.random()<=0.1)?true:false, 2);
						}
						case 2:{
							return TwosideKeeperAPI.generateSetPiece(Material.GOLD_SWORD, getModeSpecificSet(mode), (Math.random()<=0.1)?true:false, 1);
						}
					}
				} else {
					if (mode!=PlayerMode.SLAYER) {
						Material[] armor = new Material[]{Material.IRON_CHESTPLATE,Material.IRON_LEGGINGS};
							if (Math.random()<=0.5) {
								armor = new Material[]{Material.IRON_CHESTPLATE,Material.IRON_LEGGINGS};
								return TwosideKeeperAPI.generateSetPiece(armor[(int)(Math.random()*armor.length)], getModeSpecificSet(mode), (Math.random()<=0.1)?true:false, 3);
							} else
							if (Math.random()<=0.7) {
								armor = new Material[]{Material.DIAMOND_CHESTPLATE,Material.DIAMOND_LEGGINGS};
								return TwosideKeeperAPI.generateSetPiece(armor[(int)(Math.random()*armor.length)], getModeSpecificSet(mode), (Math.random()<=0.1)?true:false, 2);
							} else {
								armor = new Material[]{Material.GOLD_CHESTPLATE,Material.GOLD_LEGGINGS};
								return TwosideKeeperAPI.generateSetPiece(armor[(int)(Math.random()*armor.length)], getModeSpecificSet(mode), (Math.random()<=0.1)?true:false, 2);
							}
					} else {
						if (Math.random()<=0.4)	{
							if (Math.random()<=0.5) {
								return TwosideKeeperAPI.generateSetPiece(Material.IRON_SWORD, getModeSpecificSet(mode), (Math.random()<=0.1)?true:false, 3);
							} else
							if (Math.random()<=0.7) {
								return TwosideKeeperAPI.generateSetPiece(Material.DIAMOND_SWORD, getModeSpecificSet(mode), (Math.random()<=0.1)?true:false, 2);
							} else {
								return TwosideKeeperAPI.generateSetPiece(Material.GOLD_SWORD, getModeSpecificSet(mode), (Math.random()<=0.1)?true:false, 1);
							}
						} else {
							ItemSet[] baublesets = new ItemSet[]{ItemSet.WOLFSBANE,ItemSet.ALUSTINE,ItemSet.MOONSHADOW,ItemSet.GLADOMAIN};
							return TwosideKeeperAPI.generateSetPiece(Material.SKULL_ITEM, baublesets[(int)(Math.random()*baublesets.length)], true, 3);
						}
					}
				}
			}break;
		}
		TwosideKeeper.log("WARNING! Something went terribly wrong while generating set piece. Diff: "+diff+", Mode: "+mode, 1);
		DebugUtils.showStackTrace();
		return null;
	}
	
	public static ItemSet getModeSpecificSet(PlayerMode mode) {
		ItemSet[] allsets = new ItemSet[]{ItemSet.LUCI,ItemSet.TOXIN,ItemSet.SUSTENANCE,ItemSet.PRIDE,ItemSet.STEALTH,};
		switch (mode) {
			case BARBARIAN:
				return ItemSet.PRIDE;
			case DEFENDER:
				return ItemSet.SUSTENANCE;
			case NORMAL:
				return allsets[(int)(Math.random()*allsets.length)];
			case RANGER:
				return ItemSet.TOXIN;
			case SLAYER:
				return ItemSet.STEALTH;
			case STRIKER:
				return ItemSet.LUCI;
			default:
				TwosideKeeper.log("WARNING! Could not find loot entry for mode "+mode+". This should not be happening!!", 1);
				return allsets[(int)(Math.random()*allsets.length)];
		}
	}
}
