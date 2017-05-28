package sig.plugin.TwosideKeeper.Monster;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.inventivetalent.glow.GlowAPI.Color;

import sig.plugin.TwosideKeeper.Buff;
import sig.plugin.TwosideKeeper.ChargeZombie;
import sig.plugin.TwosideKeeper.CustomDamage;
import sig.plugin.TwosideKeeper.CustomMonster;
import sig.plugin.TwosideKeeper.LivingEntityStructure;
import sig.plugin.TwosideKeeper.MonsterController;
import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.Events.EntityChannelCastEvent;
import sig.plugin.TwosideKeeper.HelperStructures.BuffTemplate;
import sig.plugin.TwosideKeeper.HelperStructures.Channel;
import sig.plugin.TwosideKeeper.HelperStructures.LivingEntityDifficulty;
import sig.plugin.TwosideKeeper.HelperStructures.Spell;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;
import sig.plugin.TwosideKeeper.HelperStructures.Effects.HighlightCircle;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.EntityUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.MovementUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.SoundUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.Classes.ColoredParticle;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.Classes.MixedDamage;

public class SniperSkeleton extends CustomMonster{
	
	BossBar healthbar;
	protected String arrow = "->";
	int scroll=0;
	protected List<Player> participantlist = new ArrayList<Player>();
	protected HashMap<String,Double> dpslist = new HashMap<String,Double>();
	long lasthit;
	boolean startedfight=false;
	private long stuckTimer=0;
	private Location lastLoc = null;
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
				m.setMaxHealth(16000);
			}break;
			case T2_MINIBOSS:{
				m.setMaxHealth(41000);
			}break;
			case T3_MINIBOSS:{
				m.setMaxHealth(108000);
			}break;
		}
		m.setHealth(m.getMaxHealth());
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
		updateHealthbarForNearbyPlayers();
		updateTargetIfLost();
		regenerateHealthAndResetBossIfIdle();
		keepHealthbarUpdated();
		unstuckIfStuck();
		increaseBarTextScroll();
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
		}
	}

	public void runProjectileLaunchEvent(ProjectileLaunchEvent ev) {
		Projectile proj = ev.getEntity();
		proj.setMetadata("SNIPER_"+mode.name(), new FixedMetadataValue(TwosideKeeper.plugin,true));
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
			return Color.WHITE;
		} else {
			switch (mode) {
				case NORMAL: return Color.AQUA;
				case POISON: return Color.YELLOW;
				case BLEED: return Color.RED;
				default: return Color.AQUA;
			}
		}
	}
	
	private void removeIfTooOld() {
		if (m.getTicksLived()>72000 && !startedfight) {
			m.remove();
		}
	}
	
	private void updateAI() {
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
					} else {
						mode=ShotMode.BLEED;
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

	private void announceMessageToParticipants(String msg) {
		for (Player p : participantlist) {
			p.sendMessage(msg);
		}
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
		healthbar.removeAll();
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
		return m.hasPotionEffect(PotionEffectType.INVISIBILITY);
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

	private void unstuckIfStuck() {
		if (!startedfight) {
			ChargeZombie.BreakBlocksAroundArea((Monster)m, 1);
		} else
		if (startedfight) {
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
			m.setHealth(m.getMaxHealth());
			announceFailedTakedown();
		}
	}

	private void updateTargetIfLost() {
		Monster mm = (Monster)m;
		LivingEntityStructure les = LivingEntityStructure.GetLivingEntityStructure(m);
		if (mm.getTarget()==null || !mm.getTarget().isValid() ||
				les.GetTarget()==null || !mm.getTarget().isValid() ||
				((mm.getTarget().getLocation().distanceSquared(mm.getLocation())>2500 ||
				les.GetTarget().getLocation().distanceSquared(mm.getLocation())>2500
				))) {
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

	public void announceFailedTakedown() {
		if (dpslist.size()>0 && !m.isDead()) {
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

	public void announceSuccessfulTakedown() {
		if (dpslist.size()>0 && !m.isDead()) {
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
	
	public void onHitEvent(LivingEntity damager, double damage) {
		addTarget(damager,damage);
		Bukkit.getScheduler().scheduleSyncDelayedTask(TwosideKeeper.plugin, ()->{
			m.setVelocity(m.getVelocity().multiply(0.33));
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
		}
	}

	public void addParticipant(Player p) {
		if (!participantlist.contains(p)) {
			participantlist.add(p);
		}
	}

	private void updateHealthbarForNearbyPlayers() {
		for (Player p : healthbar.getPlayers()) {
			if (p.getWorld().equals(m.getWorld()) && p.getLocation().distanceSquared(m.getLocation())>2500) {
				healthbar.removePlayer(p);
			}
		}
		for (Entity e : m.getNearbyEntities(50, 50, 50)) {
			if (e instanceof Player) {
				Player p = (Player)e;
				healthbar.addPlayer(p);
			}
		}
	}

	public static boolean randomlyConvertAsSniperSkeleton(LivingEntity m, boolean force) {
		if ((TwosideKeeper.MINIBOSSES_ACTIVATED &&
				TwosideKeeper.LAST_SPECIAL_SPAWN+(6000/Math.max(Bukkit.getOnlinePlayers().size(),1))<=TwosideKeeper.getServerTickTime() &&
				Math.random()<=0.01) || force) {
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
	
	enum ShotMode {
		NORMAL,
		POISON,
		BLEED
	}
}
