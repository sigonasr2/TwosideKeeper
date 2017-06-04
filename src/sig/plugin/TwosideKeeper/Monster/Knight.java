package sig.plugin.TwosideKeeper.Monster;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.BlockFace;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.Endermite;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.inventivetalent.glow.GlowAPI.Color;

import aPlugin.DropRandomEnchantedBook;
import aPlugin.DropRandomFood;

import org.bukkit.entity.Spider;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.util.Vector;

import sig.plugin.TwosideKeeper.Artifact;
import sig.plugin.TwosideKeeper.Buff;
import sig.plugin.TwosideKeeper.ChargeZombie;
import sig.plugin.TwosideKeeper.CustomDamage;
import sig.plugin.TwosideKeeper.CustomMonster;
import sig.plugin.TwosideKeeper.GlobalLoot;
import sig.plugin.TwosideKeeper.LivingEntityStructure;
import sig.plugin.TwosideKeeper.MonsterController;
import sig.plugin.TwosideKeeper.PlayerStructure;
import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.TwosideKeeperAPI;
import sig.plugin.TwosideKeeper.Events.EntityChannelCastEvent;
import sig.plugin.TwosideKeeper.HelperStructures.ArtifactItem;
import sig.plugin.TwosideKeeper.HelperStructures.ArtifactItemType;
import sig.plugin.TwosideKeeper.HelperStructures.Channel;
import sig.plugin.TwosideKeeper.HelperStructures.CustomItem;
import sig.plugin.TwosideKeeper.HelperStructures.ItemSet;
import sig.plugin.TwosideKeeper.HelperStructures.LivingEntityDifficulty;
import sig.plugin.TwosideKeeper.HelperStructures.PlayerMode;
import sig.plugin.TwosideKeeper.HelperStructures.Spell;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;
import sig.plugin.TwosideKeeper.HelperStructures.Effects.DarkSlash;
import sig.plugin.TwosideKeeper.HelperStructures.Effects.HighlightCircle;
import sig.plugin.TwosideKeeper.HelperStructures.Effects.TemporaryBlock;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.BlockUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.DebugUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.EntityUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.ItemUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.MovementUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.SoundUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.Classes.ColoredParticle;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.Classes.MixedDamage;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.Classes.ParticleEffect;

public class Knight extends GenericBoss{
	
	DarkSpider spider_pet;
	BossBar shieldbar;
	Location lastlandedloc = null;
	final static double[] SHIELD_AMT = new double[]{1800,4700,16000};
	Location targetloc = null;
	List<Location> beamlocs = new ArrayList<Location>();

	final static int[] ASSASSINATE_COOLDOWN = new int[]{320,280,240};
	long lastusedassassinate = TwosideKeeper.getServerTickTime();
	final Spell DARKSLASH = new Spell("Dark Slash",new int[]{60,40,40},new int[]{400,300,200},new MixedDamage[]{MixedDamage.v(150),MixedDamage.v(300),MixedDamage.v(300,0.1)});
	final Spell LINEDRIVE = new Spell("Line Drive",new int[]{20,10,10},new int[]{800,700,600},new MixedDamage[]{MixedDamage.v(200),MixedDamage.v(400),MixedDamage.v(400, 0.2)});
	MixedDamage[] BASIC_ATTACK_DAMAGE = new MixedDamage[]{MixedDamage.v(50),MixedDamage.v(100),MixedDamage.v(200, 0.05)};
	final Spell DARKCLEANSE = new Spell("Dark Cleanse",new int[]{200,240,300},new int[]{1500,1200,1200},new MixedDamage[]{MixedDamage.v(100),MixedDamage.v(300),MixedDamage.v(500, 0.3)});
	long lastusedgrandslam = TwosideKeeper.getServerTickTime();
	final static int[] GRANDSLAM_COOLDOWN = new int[]{900,700,600};
	MixedDamage[] GRANDSLAM_DAMAGE = new MixedDamage[]{MixedDamage.v(450),MixedDamage.v(700),MixedDamage.v(700, 0.55)};
	final Spell DARKREVERIE = new Spell("Dark Reverie",new int[]{60,40,40},new int[]{600,600,600});
	final Spell PHASEII = new Spell("Phase II",new int[]{80,80,80},new int[]{0,0,0});
	final Spell LIGHTNINGBOLT = new Spell("Lightning Bolt",new int[]{80,60,40},new int[]{400,300,200},new MixedDamage[]{MixedDamage.v(100,0.02),MixedDamage.v(250,0.05),MixedDamage.v(400, 0.1)});
	final Spell DARKLIGHT = new Spell("The Dark Light",new int[]{60,60,60},new int[]{500,500,500},new MixedDamage[]{MixedDamage.v(200,0.05),MixedDamage.v(300,0.10),MixedDamage.v(400, 0.15)});
	final Spell MINDFIELD = new Spell("Mind Field",new int[]{120,80,80},new int[]{1200,1000,800},new MixedDamage[]{MixedDamage.v(0,0.04),MixedDamage.v(0,0.07),MixedDamage.v(0, 0.15)});
	
	int randomness = 20;
	boolean phaseii = false;
	long silverfishtimer = 0;
	long lastremoveddebufftime = 0;
	
	final static int DEBUFFREMOVAL_COOLDOWN = 20*30;
	
	List<LivingEntity> endermites = new ArrayList<LivingEntity>();
	LivingEntity silverfish = null;
	

	public Knight(LivingEntity m) {
		super(m);
		LivingEntityStructure les = LivingEntityStructure.GetLivingEntityStructure(m);
		les.setCustomLivingEntityName(m, ChatColor.GOLD+"Knight");
		LivingEntityDifficulty led = MonsterController.getLivingEntityDifficulty(m);
		switch (led) {
			case T1_MINIBOSS:{
				m.setMaxHealth(36000);
			}break;
			case T2_MINIBOSS:{
				m.setMaxHealth(96000);
			}break;
			case T3_MINIBOSS:{
				m.setMaxHealth(332000);
			}break;
		}
		m.setHealth(m.getMaxHealth());
		baseHP = m.getMaxHealth();
		m.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.31f);
		relinkToSpider();
		m.setAI(false);
		m.setRemoveWhenFarAway(false);
		createBossHealthbar();
		//GenericFunctions.setGlowing(m, Color.AQUA);
		setupDarkSword();
		GenericFunctions.logAndRemovePotionEffectFromEntity(PotionEffectType.INVISIBILITY, m);
	}

	public boolean isInIframe() {
		return (Channel.isChanneling(m) && Channel.getCurrentChannel(m).getSpellName().equalsIgnoreCase("Phase II"));
	}

	public void runTick() {
		super.runTick();
		relinkToSpider();
		displayDarkSwordParticles();
		keepSpiderPetNearby();
		preventTargetFromBeingTheSameAsSpider();
		performSpells();
		performSilverfishNotification();
		removeDebuffs();
		updateAI();
		removeIfTooOld();
		updateShieldBar();
	}
	
	private void updateShieldBar() {
		if (shieldbar!=null) {
			shieldbar.setProgress(Math.min(1,CustomDamage.getAbsorptionHearts(m)/SHIELD_AMT[getDifficultySlot()]));
		}
	}

	public Color getGlowColor() {
		if (isInIframe()) {
			return Color.GRAY;
		} else 
		if (Channel.isChanneling(m)) {
			Channel c = Channel.getCurrentChannel(m);
			if (c.getSpellName().equalsIgnoreCase("Dark Cleanse")) {
				return Color.PURPLE;
			} else {
				return Color.YELLOW;
			}
		} else {
			if (GenericFunctions.isSuppressed(m)) {
				return Color.BLACK;
			} else {
				return Color.AQUA;
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

	private void removeDebuffs() {
		if (phaseii || lastremoveddebufftime+DEBUFFREMOVAL_COOLDOWN<=TwosideKeeper.getServerTickTime()) {
			removeADebuff();
		}
	}

	public boolean isImmuneToSuppression() {
		return phaseii;
	}

	private void removeADebuff() {
		for (PotionEffect pe : m.getActivePotionEffects()) {
			if (GenericFunctions.isBadEffect(pe.getType())) {
				GenericFunctions.logAndRemovePotionEffectFromEntity(pe.getType(), m);
				return;
			}
		}
		for (String s : Buff.getBuffData(m).keySet()) {
			Buff b = Buff.getBuffData(m).get(s);
			if (b.isDebuff()) {
				/*TwosideKeeper.ScheduleRemoval(Buff.getBuffData(m), s);
				return;*/
				Bukkit.getScheduler().runTaskLater(TwosideKeeper.plugin, ()->{
					Buff.removeBuff(m, s);
				}, 1);
				return;
			}
		}
	}

	private void performSilverfishNotification() {
		if (silverfish!=null &&
				silverfishtimer+(MINDFIELD.getCooldowns()[getDifficultySlot()])<=TwosideKeeper.getServerTickTime()) {
			SoundUtils.playLocalGlobalSound(Sound.BLOCK_REDSTONE_TORCH_BURNOUT, 1.0f, 1.2f);
			for (Player p : participantlist) {
				Buff.addBuff(m, "DARKSUBMISSION", new Buff("Dark Submission",20*20,50,org.bukkit.Color.BLACK,ChatColor.BLACK+""+ChatColor.MAGIC+"☁"+ChatColor.RESET,false), true);
				TwosideKeeper.ApplyDarkSubmissionEffects(p, Math.min((Buff.getBuff(p, "DARKSUBMISSION").getAmplifier()/10)*10,50));
			}
			silverfish.remove();
			for (LivingEntity ent : endermites) {
				ent.remove();
			}
			endermites.clear();
			silverfish=null;
		} else 
		if (silverfish!=null/* && silverfishtimer+(MINDFIELD.getCooldowns()[getDifficultySlot()]/2)<=TwosideKeeper.getServerTickTime()*/) {
			silverfish.setGlowing(true);
		}
	}

	public void onPlayerSlayEvent(Player p, String reason) {
		if (reason.equalsIgnoreCase("Line Drive Knight")) {
			changeAggroToRandomNewTarget();
			LINEDRIVE.setLastCastedTime(0);
			attemptSpellCast(LINEDRIVE);
			SoundUtils.playGlobalSound(m.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 0.9f);
		}
	}
	
	public double getDarkSubmissionMultiplier(Player p) {
		double mult = 0.0;
		if (Buff.hasBuff(p, "DARKSUBMISSION")) {
			Buff b = Buff.getBuff(p, "DARKSUBMISSION");
			mult += 0.01 * b.getAmplifier();
		}
		return mult;
	}
	
	public boolean isOnGround(Player p) {
		return (p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType()!=Material.AIR &&
				!p.getLocation().getBlock().getRelative(BlockFace.DOWN).isLiquid());
	}
	
	public void runChannelCastEvent(EntityChannelCastEvent ev) {
		switch (ev.getAbilityName()) {
			case "Dark Slash":{
				TwosideKeeper.windslashes.add(
						new DarkSlash(m.getLocation(),m,DARKSLASH.getDamageValues()[getDifficultySlot()],20*20)
						);
				BlockFace[] dirs = MovementUtils.get45DegreeDirections(EntityUtils.getFacingDirection(m));
				for (BlockFace face : dirs) {
					TwosideKeeper.windslashes.add(
							new DarkSlash(m.getLocation().add(
									new Vector(face.getModX(),face.getModY(),face.getModZ()).multiply(3)
									),m,DARKSLASH.getDamageValues()[getDifficultySlot()],20*20)
							);
				}
				DARKSLASH.setLastCastedTime(TwosideKeeper.getServerTickTime());
			}break;
			case "Line Drive":{
				m.setVelocity(new Vector(0,0.15,0));
				Bukkit.getScheduler().runTaskLater(TwosideKeeper.plugin, ()->{
					m.setVelocity(m.getLocation().getDirection().multiply(6));
					int range = 6;
					double xspd = m.getLocation().getDirection().getX()*2;
					double zspd = m.getLocation().getDirection().getZ()*2;
					for (int i=0;i<range;i++) {
						Location particlepos = m.getLocation().add(i*xspd,0,i*zspd);
						for (int j=0;j<50;j++) {
							particlepos.add(j*(xspd/50),0,j*(zspd/50));
							aPlugin.API.displayEndRodParticle(particlepos.add(0,-0.5,0), 0, 0, 0, 0, 1);
						}
						Location newpos = m.getLocation().add(i*xspd,0,i*zspd);
						if (!BlockUtils.isPassThrough(newpos)) {
							break;
						}
						Bukkit.getScheduler().runTaskLater(TwosideKeeper.plugin, 
							()->{
								MixedDamage dmg = LINEDRIVE.getDamageValues()[getDifficultySlot()];
								List<Player> players = GenericFunctions.DealDamageToNearbyPlayers(newpos, dmg.getDmgComponent(), 2, true, true, 0.4d, m, "Line Drive", false, false);
								if (dmg.getTruePctDmgComponent()>0) {GenericFunctions.DealDamageToNearbyPlayers(newpos, dmg.getTruePctDmgComponent(), 2, true, true, 0.4d, m, "Line Drive", false, true);}
								if (dmg.getTrueDmgComponent()>0) {GenericFunctions.DealDamageToNearbyPlayers(newpos, dmg.getTrueDmgComponent(), 2, true, true, 0.4d, m, "Line Drive", true, false);}
								for (Player p : players) {
									double missinghp=p.getMaxHealth()-p.getHealth();
									switch (getDifficultySlot()) {
										case 0:{
											CustomDamage.ApplyDamage(missinghp*0.15, m, p, null, "Line Drive Knight", CustomDamage.IGNOREDODGE|CustomDamage.IGNORE_DAMAGE_TICK);
										}break;
										case 1:{
											CustomDamage.ApplyDamage(missinghp*0.25, m, p, null, "Line Drive Knight", CustomDamage.IGNOREDODGE|CustomDamage.IGNORE_DAMAGE_TICK);
										}break;
										case 2:{
											CustomDamage.ApplyDamage(missinghp*0.3, m, p, null, "Line Drive Knight", CustomDamage.IGNOREDODGE|CustomDamage.IGNORE_DAMAGE_TICK);
										}break;
									}
									
								}
							},2);
					}
				}, 4);
				LINEDRIVE.setLastCastedTime(TwosideKeeper.getServerTickTime());
			}break;
			case "Dark Cleanse":{
				double shieldremaining = CustomDamage.getAbsorptionHearts(m);
				if (shieldbar!=null) {
					shieldbar.removeAll();
					shieldbar=null;
				}
				if (shieldremaining>0) {
					//Failed to clear the shield.
					removeAllBuffsFromPlayers();
					MixedDamage dmgvalues = DARKCLEANSE.getDamageValues()[getDifficultySlot()];
					List<Player> players = GenericFunctions.DealDamageToNearbyPlayers(m.getLocation(), dmgvalues.getDmgComponent(), 50, false, false, 0, m, "Dark Cleanse Attack", false, false);
					if (dmgvalues.getTruePctDmgComponent()>0) {GenericFunctions.DealDamageToNearbyPlayers(m.getLocation(), dmgvalues.getTruePctDmgComponent(), 50, false, false, 0, m, "Dark Cleanse Attack", false, true);}
					if (dmgvalues.getTrueDmgComponent()>0) {GenericFunctions.DealDamageToNearbyPlayers(m.getLocation(), dmgvalues.getTrueDmgComponent(), 50, false, false, 0, m, "Dark Cleanse Attack", true, false);}
					for (Player p : players) {
						Buff.addBuff(p, "DARKSUBMISSION", new Buff("Dark Submission",20*20,10,org.bukkit.Color.BLACK,ChatColor.BLACK+""+ChatColor.MAGIC+"☁"+ChatColor.RESET,false), true);
						TwosideKeeper.ApplyDarkSubmissionEffects(p, Math.min((Buff.getBuff(p, "DARKSUBMISSION").getAmplifier()/10)*10,50));
					}
					announceMessageToParticipants(ChatColor.RED+"The "+GenericFunctions.getDisplayName(m)+ChatColor.RESET+""+ChatColor.RED+" screams "+ChatColor.BOLD+"\"SUBMIT TO DARKNESS\"!");
					for (int i=0;i<3;i++) {
						Bukkit.getScheduler().runTaskLater(TwosideKeeper.plugin, ()->{
							SoundUtils.playGlobalSound(m.getLocation(), Sound.ENTITY_WITCH_AMBIENT, 1.0f, 0.6f);
						}, i*3);
					for (Player p : participantlist) {
						GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.BLINDNESS, 20*3, 1, p, true);
					}
					}
				}
				CustomDamage.setAbsorptionHearts(m, 0);
				DARKCLEANSE.setLastCastedTime(TwosideKeeper.getServerTickTime());
			}break;
			case "Dark Reverie":{
				for (Player p : participantlist) {
					CreateDarkReveriePool(p.getLocation(),getDifficultySlot()+1);
				}
				CreateDarkReveriePool(m.getLocation(),getDifficultySlot()+1);
				DARKREVERIE.setLastCastedTime(TwosideKeeper.getServerTickTime());
			}break;
			case "Lightning Bolt":{
				for (int i=0;i<4;i++) {
					m.getWorld().strikeLightningEffect(targetloc);
				}
				MixedDamage dmg = LIGHTNINGBOLT.getDamageValues()[getDifficultySlot()];
				List<Player> players = GenericFunctions.DealDamageToNearbyPlayers(targetloc, dmg.getDmgComponent(), 2, false, true, 0, m, "Lightning Bolt", false, false);
				if (dmg.getTruePctDmgComponent()>0) {GenericFunctions.DealDamageToNearbyPlayers(targetloc, dmg.getTruePctDmgComponent(), 2, false, true, 0, m, "Lightning Bolt", false, true);}
				if (dmg.getTrueDmgComponent()>0) {GenericFunctions.DealDamageToNearbyPlayers(targetloc, dmg.getTrueDmgComponent(), 2, false, true, 0, m, "Lightning Bolt", true, false);}
				for (Player p : players) {
					GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.SLOW, 20*30, 6, p, true);
				}
				LIGHTNINGBOLT.setLastCastedTime(TwosideKeeper.getServerTickTime());
			}break;
			case "The Dark Light":{
				MixedDamage dmg = DARKLIGHT.getDamageValues()[getDifficultySlot()];
				for (int i=0;i<5;i++) {
					Bukkit.getScheduler().runTaskLater(TwosideKeeper.plugin, ()->{
						SoundUtils.playGlobalSound(m.getLocation(), Sound.BLOCK_REDSTONE_TORCH_BURNOUT, 1.0f, 1.2f);
					}, i*3);
				}
				for (Location l : beamlocs) {
					List<Player> players = GenericFunctions.DealDamageToNearbyPlayers(l, dmg.getDmgComponent(), 1, true, true, 1.5, m, "Dark Light", false, false);
					if (dmg.getTruePctDmgComponent()>0) {GenericFunctions.DealDamageToNearbyPlayers(l, dmg.getTruePctDmgComponent(), 1, true, true, 1.5, m, "Dark Light", false, true);}
					if (dmg.getTrueDmgComponent()>0) {GenericFunctions.DealDamageToNearbyPlayers(l, dmg.getTrueDmgComponent(), 1, true, true, 1.5, m, "Dark Light", true, false);}
					for (Player p : players) {
						Buff.addBuff(p, "CONFUSION", new Buff("Confusion",20*15,1,org.bukkit.Color.PURPLE,ChatColor.DARK_PURPLE+"๑"+ChatColor.RESET,false));
						GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.WEAKNESS, 20*15, 2, p);
						GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.BLINDNESS, 20*3, 2, p);
					}
					double yoffset=0;
					for (int i=0;i<50;i++) {
						ColoredParticle.RED_DUST.send(l.clone().add(0,yoffset,0), 50, 0, 0, 0);
						yoffset+=0.2;
					}
				}
				DARKLIGHT.setLastCastedTime(TwosideKeeper.getServerTickTime());
			}break;
			case "Mind Field":{
				spawnEndermiteAndSilverfishNearby();
			}break;
		}
	}
	
	public void triggerEndermiteKill(LivingEntity endermite) {
		if (endermites.contains(endermite)) {
			List<Player> players = GenericFunctions.DealDamageToNearbyPlayers(m.getLocation(), MINDFIELD.getDamageValues()[getDifficultySlot()].getTruePctDmgComponent(), 50, false, false, 0, m, "Endermite Popped", false, true);
			for (Player p : players) {
				GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.BLINDNESS, 20*3, 0, p, true);
				GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.SLOW, 20*3, 0, p, true);
			}
			SoundUtils.playLocalGlobalSound(Sound.ENTITY_ZOMBIE_BREAK_DOOR_WOOD, 1.0f, 1.0f);
		}
	}
	
	public void triggerSilverfishKill(LivingEntity silverfish) {
		silverfishtimer = 0;
		for (LivingEntity ent : endermites) {
			ent.remove();
		}
		endermites.clear();
		this.silverfish=null;
		List<Entity> ents = m.getNearbyEntities(50, 50, 50);
		for (Entity e : ents) {
			if (e instanceof Silverfish ||
					e instanceof Endermite ||
					e instanceof Spider) {
				e.remove();
			}
		}
	}

	private void spawnEndermiteAndSilverfishNearby() {
		silverfish=null;
		endermites.clear();
		silverfishtimer = TwosideKeeper.getServerTickTime();

		final int[] ENDERMITE_COUNT = new int[]{20,40,50};
		for (int i=0;i<ENDERMITE_COUNT[getDifficultySlot()];i++) {
			Location spawnloc = GetFreeRandomLocationAroundPoint(10);
			Endermite end = (Endermite)spawnloc.getWorld().spawnEntity(m.getLocation(), EntityType.ENDERMITE);
			end.setTarget(pickRandomTarget());
			endermites.add(end);
			TwosideKeeper.custommonsters.put(end.getUniqueId(), new ExplosiveMite(end));
		}

		Location spawnloc = GetFreeRandomLocationAroundPoint(10);
		silverfish=(LivingEntity)spawnloc.getWorld().spawnEntity(m.getLocation(), EntityType.SILVERFISH);
		silverfish.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0f);
		announceMessageToParticipants(ChatColor.RED+""+ChatColor.ITALIC+"\"Let's see how you handle my PETS!\"");
		for (int i=0;i<3;i++) {
			Bukkit.getScheduler().runTaskLater(TwosideKeeper.plugin, ()->{
				SoundUtils.playGlobalSound(m.getLocation(), Sound.ENTITY_WITCH_AMBIENT, 1.0f, 0.6f);
			}, i*3);
		};
	}

	private Location GetFreeRandomLocationAroundPoint(int range) {
		int randomx = (int)(Math.random()*(range+1))-range;
		int randomz = (int)(Math.random()*(range+1))-range;
		Location spawnloc = m.getLocation().clone().add(
				randomx,
				determineValidYBlock(m.getLocation(),randomx,randomz),
				randomz);
		return spawnloc;
	}

	private double determineValidYBlock(Location loc, int offsetx, int offsetz) {
		boolean found=false;
		Location currentloc = loc.clone();
		while (currentloc.getBlockY()<255) {
			if (currentloc.getBlock().getType()!=Material.AIR &&
					currentloc.getBlock().getType()!=Material.STATIONARY_WATER &&
					currentloc.getBlock().getType()!=Material.WATER) {
				currentloc.add(0,1,0);
			} else {
				break;
			}
		}
		return currentloc.getBlockY();
	}

	private void removeAllBuffsFromPlayers() {
		for (Player p : participantlist) {
			for (PotionEffect pe : p.getActivePotionEffects()) {
				if (!GenericFunctions.isBadEffect(pe.getType())) {
					GenericFunctions.logAndRemovePotionEffectFromEntity(pe.getType(), p);
				}
			}
		}
		for (String s : Buff.getBuffData(m).keySet()) {
			Buff b = Buff.getBuff(m, s);
			if (b!=null && b.hasBuff(m, s) && b.isGoodBuff()) {
				Bukkit.getScheduler().runTaskLater(TwosideKeeper.plugin, ()->{
					Buff.removeBuff(m, s);
				}, 1);
			}
		}
	}

	protected boolean attemptSpellCast(Spell spell) {
		if (cooldownIsAvailable(spell.getLastCastedTime(),spell)) {
			//Face target.
			Channel.createNewChannel(m, spell.getName(), (int)(spell.getCastTimes()[getDifficultySlot()]*getCastTimeMultiplier()));
			Bukkit.getScheduler().runTaskLater(TwosideKeeper.plugin, ()->{FaceTarget(m);}, 5);
			return true;
		}
		return false;
	}
	
	private double getCastTimeMultiplier() {
		double mult = 1.0;
		if (phaseii) {
			mult=0.5;
		}
		return mult;
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
	private void CreateDarkReveriePool(Location l, int tier) {
		AreaEffectCloud aec = (AreaEffectCloud)l.getWorld().spawnEntity(l, EntityType.AREA_EFFECT_CLOUD);
		aec.setColor(org.bukkit.Color.BLACK);
		aec.setRadius(5f);
		aec.setBasePotionData(new PotionData(PotionType.INSTANT_DAMAGE));
		aec.setDuration(1200);
		//aec.setReapplicationDelay(1);
		aec.setCustomName("DARKSUBMISSION "+tier);
		if (tier>=2) {
			aec.setRadiusPerTick(0.0042f);
		}
	}

	private boolean cooldownIsAvailable(long spell_timer, Spell spell) {
		return spell_timer+spell.getCooldowns()[getDifficultySlot()]<=TwosideKeeper.getServerTickTime();
	}
	
	private void performSpells() {
		final Runnable[] actions = new Runnable[]{
				()->{performAssassinate();},
				()->{attemptSpellCast(DARKSLASH);},
				()->{
					if (attemptSpellCast(LINEDRIVE)) {changeAggroToRandomNewTarget();
					SoundUtils.playGlobalSound(m.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 0.9f);}},
				()->{
					if (attemptSpellCast(DARKCLEANSE)) {
						LivingEntityStructure les = LivingEntityStructure.GetLivingEntityStructure(m);
						shieldbar = Bukkit.getServer().createBossBar(les.getDifficultyAndMonsterName()+"'s Shield", BarColor.PURPLE, BarStyle.SEGMENTED_6, BarFlag.CREATE_FOG);
					for (Player p : participantlist) {
						shieldbar.addPlayer(p);
					}
					CustomDamage.setAbsorptionHearts(m, (float)SHIELD_AMT[getDifficultySlot()]);}},
				()->{
					performGrandSlam();},
				()->{
					if (attemptSpellCast(DARKREVERIE)) {SoundUtils.playGlobalSound(m.getLocation(), Sound.ENTITY_CAT_HISS, 0.7f, 1.2f);}}
		};
		final Runnable[] actions2 = new Runnable[]{
			()->{if (attemptSpellCast(LIGHTNINGBOLT)) {Player p = changeAggroToRandomNewTarget();
				createLightningWarning(p);}},	
			()->{if (attemptSpellCast(DARKLIGHT)) {
				spawnBeams();
			}},
			()->{attemptSpellCast(MINDFIELD);},
			()->{if (attemptSpellCast(DARKREVERIE)) {SoundUtils.playGlobalSound(m.getLocation(), Sound.ENTITY_CAT_HISS, 0.7f, 1.2f);}},
			()->{performSpellFromFirstPhase(actions);},
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
			if (attemptSpellCast(PHASEII)) {
				phaseii=true;
			}
		}
	}

	private void spawnBeams() {
		final int[] beamcount = new int[]{25,40,50};
		beamlocs.clear();
		for (int i=0;i<beamcount[getDifficultySlot()];i++) {
			int beamDuration = (int)(Math.random()*(50));
			int randomx = (int)(Math.random()*21)-10;
			int randomz = (int)(Math.random()*21)-10;
			Bukkit.getScheduler().runTaskLater(TwosideKeeper.plugin, ()->{
				Location beamloc = new Location(m.getWorld(),
						randomx,
						0,
						randomz);
				Location finalbeamloc = m.getLocation().add(beamloc);
				beamlocs.add(finalbeamloc);
				new HighlightCircle(finalbeamloc, 1, 15, 60-beamDuration);
				TemporaryBlock.createTemporaryBlockCircle(finalbeamloc, 1, Material.STAINED_CLAY, (byte)14, 60-beamDuration, "BEAM");
			}, beamDuration);
		}
	}

	private void createLightningWarning(Player p) {
		TemporaryBlock.createTemporaryBlockCircle(p.getLocation(), 2, Material.STAINED_GLASS, (byte)8, LIGHTNINGBOLT.getCastTimes()[getDifficultySlot()], "LIGHTNINGBOLT");
		targetloc = p.getLocation().clone();
		new HighlightCircle(targetloc,2,30,LIGHTNINGBOLT.getCastTimes()[getDifficultySlot()]);
	}

	private void performSpellFromFirstPhase(final Runnable[] actions) {
		for (Runnable r : actions) {
			if (Math.random()<=1d/actions.length) {
				Bukkit.getScheduler().runTask(TwosideKeeper.plugin, r);
				break;
			}
		}
	}

	private void performGrandSlam() {
		if (lastusedgrandslam+GRANDSLAM_COOLDOWN[getDifficultySlot()]<=TwosideKeeper.getServerTickTime()) {
			isFlying=true;
			lastlandedloc=m.getLocation().clone();
			Bukkit.getScheduler().scheduleSyncDelayedTask(TwosideKeeper.plugin, new Runnable() {
				public void run() {
					m.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION,Integer.MAX_VALUE,60));
				}
			},8);
			m.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION,Integer.MAX_VALUE,20));
			for (Player p : participantlist) {
				PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
				pd.customtitle.modifyLargeCenterTitle(ChatColor.RED+"WARNING!", 20);
			}
			Bukkit.getScheduler().runTaskLater(TwosideKeeper.plugin,()->{
				for (int i=0;i<3;i++) {
					Bukkit.getScheduler().runTaskLater(TwosideKeeper.plugin, ()->{
						SoundUtils.playGlobalSound(m.getLocation(), Sound.ENTITY_WITCH_AMBIENT, 1.0f, 0.6f);
					}, i*3);
				}
				announceMessageToParticipants(ChatColor.YELLOW+""+ChatColor.ITALIC+"\"Hehe, I'm going to "+ChatColor.RESET+""+ChatColor.DARK_RED+""+ChatColor.BOLD+"CRUSH YOU!"+ChatColor.RESET+""+ChatColor.YELLOW+""+ChatColor.ITALIC+"\"");
				//SoundUtils.playGlobalSound(loc,Sound.BLOCK_PORTAL_TRIGGER, 0.2f, 2.0f);
				SoundUtils.playLocalGlobalSound(Sound.BLOCK_PORTAL_TRIGGER, 0.2f, 2.0f);
			},40);
			Bukkit.getScheduler().runTaskLater(TwosideKeeper.plugin,()->{
				for (Player p : participantlist) {
					GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.BLINDNESS, 20, 0, p, true);
				}
				SoundUtils.playLocalGlobalSound(Sound.ENTITY_GENERIC_EXTINGUISH_FIRE, 1.0f, 0.5f);
			},80);
			Bukkit.getScheduler().runTaskLater(TwosideKeeper.plugin,()->{
				for (Player p : participantlist) {
					GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.NIGHT_VISION, 10, 0, p, true);
				}
				SoundUtils.playLocalGlobalSound(Sound.ENTITY_GENERIC_EXTINGUISH_FIRE, 1.0f, 0.5f);
			},90);
			Bukkit.getScheduler().runTaskLater(TwosideKeeper.plugin,()->{
				GenericFunctions.logAndRemovePotionEffectFromEntity(PotionEffectType.LEVITATION, m);
				m.teleport(lastlandedloc);
				for (int i=0;i<5;i++) {
					Bukkit.getScheduler().runTaskLater(TwosideKeeper.plugin, ()->{
						//SoundUtils.playGlobalSound(m.getLocation(), Sound.ENTITY_WITCH_AMBIENT, 1.0f, 0.6f);
						SoundUtils.playLocalGlobalSound(Sound.ENTITY_GENERIC_EXPLODE, (float)(Math.random()*0.5+0.5), (float)(0.8+Math.random()*0.2));
					}, i*4);
				}
				for (Player p : participantlist) {
					//if (p.isOnGround() || !(p.getLocation().getBlock().isLiquid() || p.getLocation().getBlock().getType()==Material.AIR)) {
					if (isOnGround(p)) {
						MixedDamage dmg = GRANDSLAM_DAMAGE[getDifficultySlot()];
						CustomDamage.ApplyDamage(dmg.getDmgComponent(), m, p, null, "Grand Slam",CustomDamage.IGNORE_DAMAGE_TICK|CustomDamage.IGNOREDODGE);
						if (dmg.getTruePctDmgComponent()>0) {CustomDamage.ApplyDamage(dmg.getTruePctDmgComponent()*p.getMaxHealth(), m, p, null, "Grand Slam",CustomDamage.IGNORE_DAMAGE_TICK|CustomDamage.IGNOREDODGE|CustomDamage.TRUEDMG);}
						if (dmg.getTrueDmgComponent()>0) {CustomDamage.ApplyDamage(dmg.getTrueDmgComponent()*p.getMaxHealth(), m, p, null, "Grand Slam",CustomDamage.IGNORE_DAMAGE_TICK|CustomDamage.IGNOREDODGE|CustomDamage.TRUEDMG);}
					}
				}
			},100);
			Bukkit.getScheduler().runTaskLater(TwosideKeeper.plugin,()->{
				isFlying=false;
			},105);
			lastusedgrandslam = TwosideKeeper.getServerTickTime();
		}
	}

	private void performAssassinate() {
		if (lastusedassassinate+ASSASSINATE_COOLDOWN[getDifficultySlot()]<=TwosideKeeper.getServerTickTime()) {
			lastusedassassinate=TwosideKeeper.getServerTickTime();
			Player p = setAggroOnRandomTarget();
			Location teleloc = p.getLocation().add(p.getLocation().getDirection().multiply(-1d));
			if (teleloc.getBlock().getType().isSolid() ||
					teleloc.getBlock().getRelative(0,1,0).getType().isSolid() &&
					teleloc.distanceSquared(m.getLocation())<=2500) {
				teleloc = p.getLocation();
			}
			m.teleport(teleloc);
			SoundUtils.playGlobalSound(m.getLocation(), Sound.BLOCK_NOTE_SNARE, 1.0f, 1.0f);
		}
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
		if (!isFlying) {
			for (int i=0;i<participantlist.size();i++) {
				Player p = participantlist.get(i);
				if (p==null || !p.isValid() || p.isDead() ||
						p.getLocation().distanceSquared(m.getLocation())>2500) {
					participantlist.remove(i--);
				}
			}
		}
	}

	private boolean canCastSpells() {
		 return Math.random()<=1/16d && !Buff.hasBuff(m, "SILENCE") && startedfight && !Channel.isChanneling(m) && !isFlying;
	}

	private void preventTargetFromBeingTheSameAsSpider() {
		if (isValidSpiderPet()) {
			Monster me = (Monster)m;
			Monster spider = (Monster)spider_pet.GetMonster();
			if (spider.getTarget()!=null && me.getTarget()!=null &&
					spider.getTarget().equals(me.getTarget())) {
				if (Math.random()<=0.5) {
					Location newloc = spider.getLocation().add(Math.random()*15-5,0,0);
					if (!newloc.getBlock().getType().isSolid() &&
							!newloc.getBlock().getRelative(0,1,0).getType().isSolid()) {
						//SoundUtils.playGlobalSound(spider.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 1.0f, 1.0f);
						spider.teleport(newloc);
						spider.setTarget(null);
					}
				} else {
					Location newloc = spider.getLocation().add(Math.random()*10-5,0,0);
					if (!newloc.getBlock().getType().isSolid() &&
							!newloc.getBlock().getRelative(0,1,0).getType().isSolid()) {
						//SoundUtils.playGlobalSound(spider.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 1.0f, 1.0f);
						spider.teleport(newloc);
						spider.setTarget(null);
					}
				}
			}
		}
	}

	private boolean isValidSpiderPet() {
		return spider_pet!=null && spider_pet.GetMonster()!=null &&
				spider_pet.GetMonster().isValid();
	}

	private void keepSpiderPetNearby() {
		if (isValidSpiderPet()) {
			LivingEntityStructure les = LivingEntityStructure.GetLivingEntityStructure(spider_pet.GetMonster());
			if (spider_pet.GetMonster().getLocation().distanceSquared(m.getLocation())>625) {
				spider_pet.GetMonster().teleport(m);
			}
			les.SetTarget(m);
		}
	}

	public void announceFailedTakedown() {
		super.announceFailedTakedown();
		if (dpslist.size()>0 && !m.isDead()) {
			if (shieldbar!=null) {
				shieldbar.removeAll();
				shieldbar=null;
			}
			phaseii=false;
			PerformSpiderCleanup();
			PerformSilverfishAndEndermiteCleanup();
		}
	}

	public void announceSuccessfulTakedown() {
		if (dpslist.size()>0 && m.isDead()) {
			phaseii=false;
			Bukkit.getServer().broadcastMessage(GenericFunctions.getDisplayName(m)+" Takedown Failed...");
			Bukkit.getServer().broadcastMessage(ChatColor.YELLOW+"DPS Breakdown:");
			Bukkit.getServer().broadcastMessage(generateDPSReport());
			aPlugin.API.discordSendRaw(GenericFunctions.getDisplayName(m)+" Takedown Failed...\n\n"+ChatColor.YELLOW+"DPS Breakdown:"+"\n```\n"+generateDPSReport()+"\n```");
			dpslist.clear();
			PerformSpiderCleanup();
			PerformSilverfishAndEndermiteCleanup();
			healthbar.setColor(BarColor.WHITE);
		}
	}

	private void PerformSpiderCleanup() {
		if (spider_pet!=null && spider_pet.GetMonster()!=null) {
			spider_pet.GetMonster().remove();
			spider_pet.cleanup();
		}
	}

	private void setupDarkSword() {
		ItemStack weap = new ItemStack(Material.STONE_SWORD);
		for (Enchantment ench : Enchantment.values()) {
			weap.addUnsafeEnchantment(ench, 10);
		}
		ItemUtils.setDisplayName(weap, ChatColor.DARK_PURPLE+"Dark Sword");
		m.getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
		m.getEquipment().setItemInOffHand(weap);
		m.getEquipment().setItemInOffHandDropChance(0.2f);
	}

	private void createBossHealthbar() {
		healthbar = Bukkit.getServer().createBossBar(GenericFunctions.getDisplayName(m), BarColor.WHITE, BarStyle.SEGMENTED_10, BarFlag.CREATE_FOG);
		healthbar.setProgress(m.getHealth()/m.getMaxHealth());
	}
	
	public double getKnockbackMult() {
		return 0.1;
	}
	
	private void displayDarkSwordParticles() {
		Location sparkleloc = m.getEyeLocation().add(0,-0.25,0);
		sparkleloc.setDirection(m.getLocation().getDirection().multiply(3));
		m.getWorld().spawnParticle(Particle.SPELL, sparkleloc, 2);
	}

	private void relinkToSpider() {
		if (spider_pet==null ||
				spider_pet.GetMonster()==null || !spider_pet.GetMonster().isValid()) {
			findNewPet();
		}
	}

	private void findNewPet() {
		for (Entity e : m.getNearbyEntities(50, 50, 50)) {
			if (e instanceof Spider) {
				if (AttemptToFindCompanionSpider(e)) {
					return;
				}
			}
		}
		Spider s = DarkSpider.InitializeDarkSpider(m);
		SetupSpiderPet(s);
	}

	private boolean AttemptToFindCompanionSpider(Entity e) {
		Spider ss = (Spider)e;
		if (DarkSpider.isDarkSpider(ss)) {
			SetupSpiderPet(ss);
			return true;
		}
		return false;
	}

	private void SetupSpiderPet(LivingEntity m) {
		if (!TwosideKeeper.custommonsters.containsKey(m)) {
			TwosideKeeper.custommonsters.put(m.getUniqueId(),new DarkSpider((LivingEntity)m));
		}
		spider_pet=(DarkSpider)TwosideKeeper.custommonsters.get(m.getUniqueId());
		spider_pet.GetMonster().setAI(false);
		spider_pet.linked_knight=this;
	}
	
	public static boolean randomlyConvertAsKnight(LivingEntity m) {
		return randomlyConvertAsKnight(m,false);
	}

	public static boolean randomlyConvertAsKnight(LivingEntity m, boolean force) {
		if ((TwosideKeeper.MINIBOSSES_ACTIVATED &&
				TwosideKeeper.LAST_SPECIAL_SPAWN+(3000/Math.max(Bukkit.getOnlinePlayers().size(),1))<=TwosideKeeper.getServerTickTime() &&
				!m.getWorld().getName().contains("Instance") &&
				Math.random()<=0.015 &&
				TwosideKeeper.elitemonsters.size()==0 &&
				GenericBoss.bossCount()==0) || force) {
			Skeleton s = (Skeleton)m;
			s.setSkeletonType(SkeletonType.WITHER);
			Spider ss = DarkSpider.InitializeDarkSpider(m);
			//ss.setPassenger(s);
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

	public static boolean isKnight(LivingEntity m) {
		return m instanceof Skeleton &&
				((Skeleton)m).getSkeletonType()==SkeletonType.WITHER &&
				(
						MonsterController.getLivingEntityDifficulty(m)==LivingEntityDifficulty.T1_MINIBOSS ||
						MonsterController.getLivingEntityDifficulty(m)==LivingEntityDifficulty.T2_MINIBOSS ||
						MonsterController.getLivingEntityDifficulty(m)==LivingEntityDifficulty.T3_MINIBOSS
				);
	}
	
	public static double getDamageReduction() {
		return 0.2;
	}
	
	public void cleanup() {
		super.cleanup();
		if (shieldbar!=null) {
			shieldbar.removeAll();
			shieldbar=null;
		}
		if (startedfight) {
			announceFailedTakedown();
			startedfight=false;
		}
		PerformSpiderCleanup();
		PerformSilverfishAndEndermiteCleanup();
	}

	private void PerformSilverfishAndEndermiteCleanup() {
		if (silverfish!=null &&
				silverfish.isValid()) {
			silverfish.remove();
			silverfish=null;
		}
		for (LivingEntity ent : endermites) {
			if (ent!=null &&
					ent.isValid()) {
				ent.remove();
			}
		}
		endermites.clear();
		List<Entity> ents = m.getNearbyEntities(50, 50, 50);
		for (Entity e : ents) {
			if (e instanceof Silverfish ||
					e instanceof Endermite ||
					e instanceof Spider) {
				e.remove();
			}
		}
	}

	public void setupBonusLoot() {
		LivingEntityDifficulty diff = MonsterController.getLivingEntityDifficulty(m);
		LivingEntityStructure les = LivingEntityStructure.GetLivingEntityStructure(m);
		GlobalLoot gl = GlobalLoot.spawnGlobalLoot(m.getLocation(), ChatColor.AQUA+""+ChatColor.BOLD+les.getDifficultyAndMonsterName()+ChatColor.AQUA+""+ChatColor.BOLD+" Miniboss Loot");
		double lootrate=1.0;
		for (String s : dpslist.keySet()) {
			UUID id = Bukkit.getOfflinePlayer(s).getUniqueId();
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
		ItemSet[] allsets = new ItemSet[]{ItemSet.WINDRY,ItemSet.SHARD,ItemSet.PROTECTOR,ItemSet.LEGION,ItemSet.ASSASSIN,};
		switch (mode) {
			case BARBARIAN:
				return ItemSet.LEGION;
			case DEFENDER:
				return ItemSet.PROTECTOR;
			case NORMAL:
				return allsets[(int)(Math.random()*allsets.length)];
			case RANGER:
				return ItemSet.SHARD;
			case SLAYER:
				return ItemSet.ASSASSIN;
			case STRIKER:
				return ItemSet.WINDRY;
			default:
				TwosideKeeper.log("WARNING! Could not find loot entry for mode "+mode+". This should not be happening!!", 1);
				return allsets[(int)(Math.random()*allsets.length)];
		}
	}
}
