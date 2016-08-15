package sig.plugin.TwosideKeeper;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Spider;
import org.bukkit.entity.TippedArrow;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.inventivetalent.glow.GlowAPI;
import org.inventivetalent.glow.GlowAPI.Color;

import com.google.common.collect.Iterables;

import sig.plugin.TwosideKeeper.HelperStructures.ArtifactAbility;
import sig.plugin.TwosideKeeper.HelperStructures.ArtifactAbilityApplyEffects;
import sig.plugin.TwosideKeeper.HelperStructures.BowMode;
import sig.plugin.TwosideKeeper.HelperStructures.ItemSet;
import sig.plugin.TwosideKeeper.HelperStructures.MonsterDifficulty;
import sig.plugin.TwosideKeeper.HelperStructures.MonsterType;
import sig.plugin.TwosideKeeper.HelperStructures.Common.DamageType;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;

public class NewCombat {
	/**
	 * Returns the amount of damage dealt to target.
	 */
	public static double applyDamage(LivingEntity target, Entity damager) {
		return applyDamage(0,target,damager,false);
	}
	
	public static double applyDamage(double basedmg, LivingEntity target, Entity damager) {
		return applyDamage(basedmg, target,damager,false);
	}
	
	public static double applyDamage(double basedmg, LivingEntity target, Entity damager, boolean isCriticalStrike) {
		return applyDamage(basedmg, target,damager,false,"");
	}
	
	public static double applyDamage(double basedmg, LivingEntity target, Entity damager, boolean isCriticalStrike, String reason) {
		switch (DamageType.DetectType(target, damager)) {
			case MOBVSMOB: 
			case MOBPROJECTILEVSMOB: 
		 	case MOBPROJECTILEVSPLAYER:
			case MOBVSPLAYER: {
				return calculateMobDamage(basedmg, target, damager, isCriticalStrike);
			}
			case PLAYERPROJECTILEVSMOB:
			case PLAYERVSMOB: {
				return calculatePlayerDamage(basedmg, target, damager, isCriticalStrike, reason);
			}
			case OTHER:
			default: {
				return -1.0;
			}
		}
	}
	
	//Set all damage modifiers from the game to 0. We will calculate our own value.
	public static void setupTrueDamage(EntityDamageEvent ev) {
		for (int i=0;i<DamageModifier.values().length;i++) {
			if (ev.isApplicable(DamageModifier.values()[i])) {
				ev.setDamage(DamageModifier.values()[i],0);
			}
		}
		ev.setDamage(0);
	}

	public static double calculatePlayerDamage(LivingEntity target, Entity damager) {
		return calculatePlayerDamage(0,target,damager,false);
	}
	
	public static double calculatePlayerDamage(double basedmg,LivingEntity target, Entity damager) {
		return calculatePlayerDamage(basedmg,target,damager,false);
	}
	
	public static double calculatePlayerDamage(double basedmg,LivingEntity target, Entity damager, boolean isCriticalStrike) {
		return calculatePlayerDamage(basedmg,target,damager,isCriticalStrike,"");
	}
	
	public static double calculatePlayerDamage(double basedmg,LivingEntity target, Entity damager, boolean isCriticalStrike, String reason) {
		LivingEntity shooter = getDamagerEntity(damager);
		
		double finaldmg = 0.0; 
		if (shooter!=null) {
			finaldmg += calculateTotalDamage(basedmg, target, damager, isCriticalStrike, reason);
			if (shooter instanceof Player) {
				Player p = (Player)shooter;
				playerPerformMiscActions(p,target);
				if (target instanceof Monster) {
					Monster m = (Monster)target;
					setMonsterTarget(m,p);
					provokeMonster(m,p); 
				}
			}
		}

		if (shooter!=null) {
			if (shooter instanceof Player) {
				PlayerStructure pd = PlayerStructure.GetPlayerStructure((Player)shooter);
				pd.damagedata.addCalculatedTotalDamage(finaldmg);
			}
		}
		return finaldmg;
	}

	public static double calculateMobDamage(LivingEntity target, Entity damager) {
		return calculateMobDamage(0,target,damager);
	}

	public static double calculateMobDamage(double basedmg, LivingEntity target, Entity damager) {
		return calculateMobDamage(basedmg,target,damager,false);
	}
	
	public static double calculateMobDamage(double basedmg, LivingEntity target, Entity damager, boolean isCriticalStrike) {
		double totaldmg = 0.0;
		double bonusmult = 1.0;
		
		LivingEntity shooter = getDamagerEntity(damager);
		
		if (shooter!=null) {
			totaldmg += calculateMobBaseDamage((LivingEntity)shooter, target);
			totaldmg += CalculateWeaponDamage(basedmg, shooter, target);
			//bonusmult *= calculateMonsterDifficultyMultiplier(shooter);
			bonusmult *= (isCriticalStrike)?2.0:1.0;
		} else {
			totaldmg = 1.0;
		}

		double finaldmg = calculateBonusMultiplier(totaldmg,bonusmult);
		
		playerAddArtifactEXP(target,finaldmg);
		applyOnHitMobEffects(target,damager,finaldmg);

		finaldmg = CalculateDamageReduction(finaldmg,target,damager);
		return calculateAbsorptionHearts(target, finaldmg);
	}

	static double calculateTotalDamage(LivingEntity target, Entity damager) {
		return calculateTotalDamage(0, target,damager,false);
	}
	
	static double calculateTotalDamage(double basedmg, LivingEntity target, Entity damager) {
		return calculateTotalDamage(basedmg, target,damager,false);
	}
	
	static double calculateTotalDamage(double basedmg, LivingEntity target, Entity damager, boolean isCriticalStrike) {
		return calculateTotalDamage(basedmg,target,damager,isCriticalStrike,"");
	}
	
	static double calculateTotalDamage(double basedmg, LivingEntity target, Entity damager, boolean isCriticalStrike, String reason) {
		double totaldmg = 0.0; //Final damage dealt. It will be multiplied by mult at the end.
		double bonusmult = 1.0; //Bonus multiplier for damage dealt.
		double armorpendmg = 0.0;

		LivingEntity shooter = getDamagerEntity(damager);
		
		if (shooter!=null) {
			if (shooter instanceof Player) {
				Player p = (Player)shooter;
				ItemStack weapon = p.getEquipment().getItemInMainHand();
				
				totaldmg+=CalculateWeaponDamage(basedmg, damager, target, false, reason);
				double mult1 = calculatePlayerCriticalStrike(weapon,damager,isCriticalStrike);
				addMultiplierToPlayerLogger(damager,"Critical Strike Mult",mult1);
				if (mult1>1.0) {
					aPlugin.API.critEntity(target, 15);
					PlayerStructure.GetPlayerStructure(p).crit=true;
				}
				bonusmult*=mult1;
			}
		}

		if (shooter!=null) {
			if (shooter instanceof Player) {
				double dmg = calculateBonusMultiplier(totaldmg,bonusmult);
				PlayerStructure pd = PlayerStructure.GetPlayerStructure((Player)shooter);
				pd.damagedata.addCalculatedActualDamage(dmg);
			}
		}
		double finaldmg = calculateBonusMultiplier(totaldmg,bonusmult);

		if (damager instanceof Player) {
			Player p = (Player)damager;
			armorpendmg = calculateArmorPen(p, finaldmg);
		}
		
		return CalculateDamageReduction(finaldmg-armorpendmg,target,damager) +
				armorpendmg;
	}
	
	static double calculateMobBaseDamage(LivingEntity damager, LivingEntity target) {
		double dmg = 0.0;
		Difficulty diff = damager.getWorld().getDifficulty();
		double[] difficulty_damage = {0.0,0.0,0.0}; //Damager per difficulty. {EASY,MEDIUM,HARD}
		switch (damager.getType()) {
		case BLAZE:
			difficulty_damage=new double[]{4.0,8.0,12.0};
			break;
		case CAVE_SPIDER:
			difficulty_damage=new double[]{2.0,2.0,3.0};
			break;
		case CREEPER:
		case LIGHTNING:
			if (damager instanceof Creeper) {
				Creeper c = (Creeper)damager;
				double damage_mult = 2.0d/(c.getLocation().distance(target.getLocation())+1.0);
				damage_mult*=TwosideKeeper.EXPLOSION_DMG_MULT;
				difficulty_damage = (c.isPowered())?new double[]{48.0*damage_mult,72.0*damage_mult,98.0*damage_mult}:new double[]{24.0*damage_mult,36.0*damage_mult,49.0*damage_mult};
			} else {
				difficulty_damage=new double[]{12.0,18.0,24.0};
			}
			break;
		case DRAGON_FIREBALL:
		case ENDER_DRAGON:
			difficulty_damage=new double[]{6.0,12.0,24.0};
			break;
		case ENDERMAN:
			difficulty_damage=new double[]{4.0,7.0,12.0};
			break;
		case ENDERMITE:
			difficulty_damage=new double[]{2.0,3.0,4.0};
			break;
		case ENDER_CRYSTAL:
			difficulty_damage=new double[]{24.0,36.0,49.0};
			break;
		case FALLING_BLOCK:
			difficulty_damage=new double[]{2.0,4.0,6.0};
			break;
		case FIREBALL:
		case SMALL_FIREBALL:
			difficulty_damage=new double[]{9.0,17.0,25.0};
			break;
		case GHAST:
			difficulty_damage=new double[]{10.0,20.0,30.0};
			break;
		case GUARDIAN:
			difficulty_damage=new double[]{4.0,7.0,10.0};
			break;
		case MAGMA_CUBE:
			MagmaCube mc = (MagmaCube)damager;
			difficulty_damage=new double[]{mc.getSize(),mc.getSize()*2.0,mc.getSize()*4.0};
			break;
		case PIG_ZOMBIE:
			difficulty_damage=new double[]{5.0,9.0,13.0};
			break;
		case RABBIT:
			difficulty_damage=new double[]{6.0,12.0,20.0};
			break;
		case SHULKER:
			difficulty_damage=new double[]{8.0,16.0,24.0};
			break;
		case SHULKER_BULLET:
			difficulty_damage=new double[]{8.0,16.0,24.0};
			break;
		case SILVERFISH:
			difficulty_damage=new double[]{1.0,2.0,4.0};
			break;
		case SKELETON:
			Skeleton s = (Skeleton)damager;
			switch (s.getSkeletonType()) {
				case NORMAL:
					difficulty_damage=new double[]{2.0,2.0,4.0};
					break;
				case WITHER:
					difficulty_damage=new double[]{4.0,8.0,12.0};
					break;
				default:
					difficulty_damage=new double[]{8.0,10.0,12.0};
					break;
			}
			break;
		case SLIME:
			Slime sl = (Slime)damager;
			difficulty_damage=new double[]{sl.getSize(),sl.getSize()*3.0,sl.getSize()*5.0};
			break;
		case SPIDER:
			difficulty_damage=new double[]{2.0,4.0,6.0};
			break;
		case WOLF:
		case WITCH:
			difficulty_damage=new double[]{3.0,5.0,7.0};
			break;
		case WITHER:
			difficulty_damage=new double[]{10.0,16.0,36.0};
			break;
		case WITHER_SKULL:
			difficulty_damage=new double[]{10.0,16.0,36.0};
			break;
		case GIANT:
		case ZOMBIE:
			difficulty_damage=new double[]{2.0,3.0,5.0};
			break;
		default:
			difficulty_damage=new double[]{1.0,1.5,2.0};
			break;
		}
		switch (diff) {
		case EASY:
				dmg+=difficulty_damage[0];
			break;
		case HARD:
				dmg+=difficulty_damage[2];
			break;
		case NORMAL:
				dmg+=difficulty_damage[1];
			break;
		}
		
		return dmg;
	}
	
	static double calculateMonsterDifficultyMultiplier(LivingEntity damager) {
		double mult = 1.0;
		if (damager instanceof Monster) {
			switch (MonsterController.getMonsterDifficulty((Monster)damager)) {
			case NORMAL:
				mult*=1.0;
				break;
			case DANGEROUS:
				mult*=2.0;
				break;
			case DEADLY:
				mult*=5.0;
				break;
			case HELLFIRE:
				mult*=10.0;
				break;
			case ELITE:
				mult*=40.0;
				break;
			default:
				mult*=1.0;
				break;
			}
		}
		return mult;
	}
	
	private static double calculateArmorPen(Player p, double dmg) {
		double finaldmg = 0.0;
		if (GenericFunctions.isArtifactEquip(p.getEquipment().getItemInMainHand()) &&
				ArtifactAbility.containsEnchantment(ArtifactAbility.ARMOR_PEN, p.getEquipment().getItemInMainHand())) {
			finaldmg += dmg*GenericFunctions.getAbilityValue(ArtifactAbility.ARMOR_PEN, p.getEquipment().getItemInMainHand())/100d;
			addToPlayerLogger(p,"Armor Pen",finaldmg);
		}
		return finaldmg;
	}
	
	static void playerPerformMiscActions(Player p, Entity target) {
		//GenericFunctions.PerformDodge(p);
		castEruption(p, target);
		removePermEnchantments(p);
	}
	
	private static void removePermEnchantments(Player p) {
		if (GenericFunctions.isWeapon(p.getEquipment().getItemInMainHand())) {
			GenericFunctions.RemovePermEnchantmentChance(p.getEquipment().getItemInMainHand(), p);
		}
	}

	private static void castEruption(Player p, Entity target) {
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		if (GenericFunctions.isArtifactEquip(p.getEquipment().getItemInMainHand()) &&
				p.getEquipment().getItemInMainHand().getType().toString().contains("SPADE") && p.isSneaking()) {
			if (ArtifactAbility.containsEnchantment(ArtifactAbility.ERUPTION, p.getEquipment().getItemInMainHand()) &&
					pd.last_shovelspell<TwosideKeeper.getServerTickTime()) {
				//Detect all nearby mobs and knock them up. Deal damage to them as well.
				List<Entity> finallist = new ArrayList<Entity>();
				List<Entity> nearby = target.getNearbyEntities(2, 2, 2);
				for (int i=0;i<nearby.size();i++) {
					if (nearby.get(i) instanceof Monster) {
						finallist.add(nearby.get(i));
					}
				}
				finallist.add(target);
				for (int i=0;i<finallist.size();i++) {
					Monster mon = (Monster)finallist.get(i);
					double finaldmg = CalculateDamageReduction(GenericFunctions.getAbilityValue(ArtifactAbility.ERUPTION, p.getEquipment().getItemInMainHand()),mon,null);
					addToPlayerLogger(p,ChatColor.BLUE+"Eruption",finaldmg);
					GenericFunctions.DealDamageToMob(finaldmg, mon, p, p.getEquipment().getItemInMainHand());
					mon.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION,20,15));
					//Attempt to dig out the blocks below.
					for (int x=-1;x<2;x++) {
						for (int z=-1;z<2;z++) {
							Block b = mon.getLocation().add(x,-1,z).getBlock();
							if (aPlugin.API.isDestroyable(b) && GenericFunctions.isSoftBlock(b)) {
								//log(b.getType()+" is destroyable.",2);
								FallingBlock fb = (FallingBlock)b.getLocation().getWorld().spawnFallingBlock(b.getLocation().add(0,0.1,0),b.getType(),(byte)0);
								fb.setVelocity(new Vector(0,Math.random()*1.35,0));
								fb.setMetadata("FAKE", new FixedMetadataValue(TwosideKeeper.plugin,true));
								//b.breakNaturally();
								b.setType(Material.AIR);
								aPlugin.API.sendSoundlessExplosion(b.getLocation(), 1);
								p.playSound(mon.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f);
							}
						}
					} 
				}
				p.playSound(p.getLocation(), Sound.ENTITY_FIREWORK_LARGE_BLAST, 1.0f, 1.0f);
				
				aPlugin.API.sendCooldownPacket(p, p.getEquipment().getItemInMainHand(), 100);
				aPlugin.API.sendCooldownPacket(p, p.getEquipment().getItemInMainHand(), 100);
				pd.last_shovelspell=TwosideKeeper.getServerTickTime()+TwosideKeeper.ERUPTION_COOLDOWN;
			}
		}
	}

	static void setMonsterTarget(Monster m, Player p) {
		addChargeZombieToList(m);
		addMonsterToTargetList(m,p);
	}
	
	static void addChargeZombieToList(Monster m) {
		if (MonsterController.isChargeZombie(m)) {
			TwosideKeeper.chargezombies.add(new ChargeZombie((Monster)m));
		}
	}
	
	public static void addMonsterToTargetList(Monster m,Player p) {
		if (!m.hasPotionEffect(PotionEffectType.GLOWING)) {m.setTarget(p);}
		if (TwosideKeeper.monsterdata.containsKey(m.getUniqueId())) {
			MonsterStructure ms = (MonsterStructure)TwosideKeeper.monsterdata.get(m.getUniqueId());
			ms.SetTarget(p);
		} else {
			TwosideKeeper.monsterdata.put(m.getUniqueId(),new MonsterStructure(m,p));
		}
	}
	
	static void provokeMonster(Monster m, Player p) {
		applyDefenderAggro(m,p);
		applyProvokeAggro(m,p);
		leaderRallyNearbyMonsters(m,p);
	}
	
	static void leaderRallyNearbyMonsters(Monster m, Player p) {
		if (MonsterController.isZombieLeader(m) &&
			!m.hasPotionEffect(PotionEffectType.GLOWING)) {
			rallyNearbyMonsters(m,p,10);
		}
	}

	private static void rallyNearbyMonsters(Monster m, Player p, double range) {
		Collection<Entity> nearby =m.getLocation().getWorld().getNearbyEntities(m.getLocation(), range, range, range);
		for (int i=0;i<nearby.size();i++) {
			if (Iterables.get(nearby, i) instanceof Monster) {
				Monster mm = (Monster)(Iterables.get(nearby, i));
				mm.setTarget(p);
				MonsterStructure ms = MonsterStructure.getMonsterStructure(mm);
				ms.SetTarget(p);
			}
		}
	}

	static void applyDefenderAggro(Monster m, Player p) {
		if (!m.hasPotionEffect(PotionEffectType.GLOWING) && GenericFunctions.isDefender(p)) {
			setMonsterTarget(m,p);
			setAggroGlowTickTime(m,100);
		}
	}
	
	static void setAggroGlowTickTime(Monster m, int duration) {
		m.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING,duration,0));
	}
	
	static void applyProvokeAggro(Monster m, Player p) {
		if (ArtifactAbilityApplyEffects.canExecuteArtifactAbility(ArtifactAbility.PROVOKE, p)) {
			//This is allowed, get the level on the weapon.
			int provokelv = ArtifactAbility.getEnchantmentLevel(ArtifactAbility.PROVOKE, p.getEquipment().getItemInMainHand());
			setAggroGlowTickTime(m,ArtifactAbility.getEnchantmentLevel(ArtifactAbility.PROVOKE, p.getEquipment().getItemInMainHand())*20);
		}
	}
	
	static double calculatePreemptiveStrikeMultiplier(LivingEntity m, Player p) {
		double mult = 1.0;
		if (isPreemptiveStrike(m,p)) {
			//Deal triple damage.
			TwosideKeeper.log("Triple damage!",5);
			p.playSound(p.getLocation(), Sound.ENTITY_SHULKER_TELEPORT, 1f, 3.65f);
			mult*=3.0;
			addMultiplierToPlayerLogger(p,ChatColor.YELLOW+"Preemptive Strike Mult",mult);
		}
		return mult;
	}
	
	static boolean isPreemptiveStrike(LivingEntity m,Player p) {
		if (GenericFunctions.isStriker(p) &&
				m!=null &&
				(m instanceof Monster) &&
				((Monster)m).getTarget()==null) {
			return true;
		} else {
			return false;
		}
	}
	
	static double calculateBonusMultiplier(double dmg,double mult) {
		//If we have a multiplier, use that. Otherwise use the base damage value.
		return dmg*mult;
	}

	public static double CalculateWeaponDamage(Entity damager, LivingEntity target) {
		return CalculateWeaponDamage(0,damager,target,false);
	}
	
	public static double CalculateWeaponDamage(Entity damager, LivingEntity target,boolean useBow) {
		return CalculateWeaponDamage(0,damager,target,useBow,"");
	}

	public static double CalculateWeaponDamage(double basedmg, Entity damager, LivingEntity target) {
		return CalculateWeaponDamage(basedmg, damager,target,false);
	}
	
	public static double CalculateWeaponDamage(double basedmg, Entity damager, LivingEntity target, boolean useBow) {
		return CalculateWeaponDamage(basedmg, damager,target,false,"");
	}
	
	public static double CalculateWeaponDamage(double suppliedDmg, Entity damager, LivingEntity target, boolean useBow, String reason) {

		double basedmg = 0.0; 
		double basemult = 1.0;
		boolean headshot=false;
		boolean preemptive=false;

		LivingEntity shooter = getDamagerEntity(damager);
		
		if (shooter!=null) {
			LivingEntity ent = shooter;
			ItemStack weapon = ent.getEquipment().getItemInMainHand();
			
			if (suppliedDmg!=0) {
				basedmg += suppliedDmg;
				//addToPlayerLogger(ent,reason,suppliedDmg);
			} else {
				if (GenericFunctions.isArtifactEquip(weapon)) {
					double dmg = getBaseArtifactDamageByType(weapon);
					if (reason.length()>0) {
						addToPlayerLogger(ent,reason,dmg);
					} else {
						addToPlayerLogger(ent,"Weapon Base Damage",dmg);
					}
					basedmg += dmg;
				} else {
					double dmg = getBaseDamageByType(weapon);
					if (reason.length()>0) {
						addToPlayerLogger(ent,reason,dmg);
					} else {
						addToPlayerLogger(ent,"Weapon Base Damage",dmg);
					}
					basedmg += dmg;
				}
			}
			
			for (int i=0;i<GenericFunctions.getEquipment(shooter).length;i++) {
				ItemSet set = ItemSet.GetSet(GenericFunctions.getEquipment(shooter)[i]);
				if (set!=null) {
					if (set==ItemSet.PANROS) {
						double dmg = ItemSet.GetBaseAmount(set,ItemSet.GetTier(GenericFunctions.getEquipment(shooter)[i]),1);
						addToPlayerLogger(ent,"Set Piece Damage",dmg);
						basedmg += dmg;
					}
				}
			}
			
			if (shooter instanceof Player) {
				Player p = (Player)shooter;
				if (GenericFunctions.isDefender(p) && ItemSet.GetSetCount(ItemSet.SONGSTEEL, p)>=5) {
					PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
					if (pd.vendetta_amt>0.0) {
						p.playSound(p.getLocation(), Sound.BLOCK_GLASS_BREAK, 1.0f, 0.5f);
						double dmg = pd.vendetta_amt;
						addToPlayerLogger(ent,"Vendetta",dmg);
						basedmg += dmg;
						pd.vendetta_amt=0.0;
						aPlugin.API.sendActionBarMessage(p, ChatColor.YELLOW+"Vendetta: "+ChatColor.GREEN+Math.round(pd.vendetta_amt)+" dmg stored");
					}
				}
				double dmg = ItemSet.TotalBaseAmountBasedOnSetBonusCount(p,ItemSet.PANROS,2,2);
				addToPlayerLogger(ent,"Set Bonus",dmg);
				basedmg += dmg;
			
				dmg = ItemSet.TotalBaseAmountBasedOnSetBonusCount(p,ItemSet.DAWNTRACKER,2,2);
				addToPlayerLogger(ent,"Set Bonus",dmg);
				basedmg += dmg;
			}
			
			if (GenericFunctions.isHardenedItem(weapon) && !GenericFunctions.isArtifactEquip(weapon)) {
				double mult = 2.0;
				addMultiplierToPlayerLogger(ent,"Hardened Item Mult",mult);
				basemult*=mult;
			}
			
			if (ent instanceof Player) {
				Player pl = (Player)ent;
				basemult*=calculatePartyAttackMultiplier(pl);
				basemult*=calculatePreemptiveStrikeMultiplier(target,pl);
				if (target instanceof LivingEntity) {
					preemptive = isPreemptiveStrike(target,pl);
				}
			}
			basedmg+=calculateEnchantmentDamageIncrease(weapon,damager,target);
			basedmg+=calculateArtifactAbilityDamageIncrease(weapon,damager,target);
			basemult*=calculateArtifactAbilityMultiplier(weapon,damager,target);
			basemult*=calculateStrikerMultiplier(damager);
			basemult*=calculateRangerMultiplier(damager);
			basemult*=calculatePotionEffectMultiplier(ent);
			double mult = calculatePoisonEffectMultiplier(target);
			addMultiplierToPlayerLogger(ent,"Poison Mult",mult);
			basemult*=mult;
			
			if (meleeWithBow(weapon,damager) && !useBow) {
				basedmg = 2.0;
				basemult = 1.0 * calculateEnchantmentDamageIncrease(weapon,damager,target);
				performMegaKnockback(damager,target);
			} else {
				if (!useBow) {
					basemult*=calculateBowDrawbackMultiplier(weapon,damager);
					
					double headshot_mult = calculateHeadshotMultiplier(damager,weapon,target);
					headshot = headshot_mult>1.0;
					addMultiplierToPlayerLogger(damager,"Headshot Mult",headshot_mult);
					basemult*=headshot_mult;
					double arrow_mult = calculateArrowMultiplier(damager,weapon,target);
					basemult*=arrow_mult;
					//This is an arrow shot from a bow.
				}
			}
			
			applyOnHitEffects(weapon,basedmg * basemult,damager,target,headshot);
		} else {
			if (damager instanceof Arrow) {
				return 4.5; //This is a basic arrow with no shooter. Deal some damage.
			} else {
				return 1.0;
			}
		}
		
		setPlayerTarget(damager,target,headshot,preemptive);
		
		if (shooter instanceof Monster) {
			if (suppliedDmg!=0) {
				basedmg = suppliedDmg;
			} else {
				basedmg = 1.0 *calculateMonsterDifficultyMultiplier(shooter);
			}
			TwosideKeeper.log("New Base damage is "+basedmg, 4);
		}
	
		return basedmg * basemult;
	}

	private static double calculateArrowMultiplier(Entity damager, ItemStack weapon, LivingEntity target) {
		double mult = 1.0;
		LivingEntity shooter = getDamagerEntity(damager);
		if (shooter instanceof Player) {
			Player p = (Player)shooter;
	
			if (damager instanceof TippedArrow) {
				TippedArrow a = (TippedArrow)damager;
				if (a.hasMetadata("DOUBLE_DAMAGE_ARR")) {
					mult*=2.0;
					addMultiplierToPlayerLogger(damager,"Handmade Arrow Mult",mult);
				}
				if (a.hasMetadata("QUADRUPLE_DAMAGE_ARR")) {
					mult*=4.0;
					addMultiplierToPlayerLogger(damager,"Diamond-Tipped Arrow Mult",mult);
				}
			}
		}
		return mult;
	}

	private static void setPlayerTarget(Entity damager, LivingEntity target, boolean headshot, boolean preemptive) {

		LivingEntity shooter = getDamagerEntity(damager);
		
		if (shooter instanceof Player) {
			Player pl = (shooter instanceof Player)?(Player)shooter:(Player)target;
			PlayerStructure pd = (PlayerStructure)TwosideKeeper.playerdata.get(pl.getUniqueId());
			if (shooter instanceof Player) {
				pd.target=getDamagerEntity(target);
			}

			pd.headshot=headshot;
			pd.preemptive=preemptive;
			if (pd.damagelogging) {
				DecimalFormat df = new DecimalFormat("0.0");			
				//TwosideKeeper.updateTitle(pl,pd);
			} else {
				TwosideKeeper.updateTitle(pl,headshot,preemptive);
			}
		}
	}

	private static void performMegaKnockback(Entity damager,final LivingEntity target) {
		if (damager instanceof Player) {
			Player p = (Player)damager;
			if (GenericFunctions.isRanger(p)) {
    			Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("TwosideKeeper"), new Runnable() {
					public void run() {
						if (target!=null) {
							target.setVelocity(target.getVelocity().setY(0.1).multiply(8));
						}
					}}
				,1);
    		}
		}
	}

	private static double calculatePoisonEffectMultiplier(LivingEntity target) {
		double mult = 1.0;
		if (target!=null) {
			mult += (GenericFunctions.getPotionEffectLevel(PotionEffectType.POISON, target)+1)*0.5;
			mult += (GenericFunctions.getPotionEffectLevel(PotionEffectType.BLINDNESS, target)+1)*0.5;
		}
		return mult;
	}

	static double calculatePartyAttackMultiplier(Player p) {
		int partylevel = 0;
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		if (pd.partybonus>0) {
			return 1.0+(pd.partybonus*0.1);
		}
		return 1.0;
	}

	static double getBaseArtifactDamageByType(ItemStack weapon) {
		if (weapon.getType().toString().contains("SWORD")) {
			return 3.0;
		} else
		if (weapon.getType().toString().contains("PICKAXE")) {
			return 1.25;
		} else
		if (weapon.getType().toString().contains("PICKAXE")) {
			return 1.25;
		} else
		if (weapon.getType().toString().contains("AXE")) {
			return 4.0;
		} else
		if (weapon.getType().toString().contains("SPADE")) {
			return 1.5;
		} else
		if (weapon.getType().toString().contains("BOW")) {
			return 4.5;
		}
		return 1.0;
	}
	
	static double getBaseDamageByType(ItemStack weapon) {
		switch (weapon.getType()) {
			case WOOD_SWORD:{
				return 3.0;
			}
			case STONE_SWORD:{
				return 4.0;
			}
			case GOLD_SWORD:{ 
				return 10.0;
			}
			case IRON_SWORD:{
				return 7.0;
			}
			case DIAMOND_SWORD:{
				return 9.0;
			}
			case WOOD_SPADE:{
				return 1.5;
			}
			case STONE_SPADE:{
				return 2.5;
			}
			case GOLD_SPADE:{
				return 8.0;
			}
			case IRON_SPADE:{
				return 5.0;
			}
			case DIAMOND_SPADE:{
				return 7.0;
			}
			case WOOD_PICKAXE:{
				return 1.25;
			}
			case STONE_PICKAXE:{
				return 2.0;
			}
			case GOLD_PICKAXE:{
				return 7.5;
			}
			case IRON_PICKAXE:{
				return 4.5;
			}
			case DIAMOND_PICKAXE:{
				return 6.0;
			}
			case WOOD_AXE:{
				return 4.0;
			}
			case STONE_AXE:{
				return 5.0;
			}
			case GOLD_AXE:{
				return 11.0;
			}
			case IRON_AXE:{
				return 8.0;
			}
			case DIAMOND_AXE:{
				return 9.0;
			}
			case BOW:{
				return 4.5;
			}
			default:{
				return 1.0;
			}
		}
	}
	
	public static LivingEntity getDamagerEntity(Entity damager) {
		return (damager instanceof LivingEntity)?((LivingEntity)damager):
			((damager instanceof Projectile) && (((Projectile)damager).getShooter() instanceof LivingEntity))?(LivingEntity)((Projectile)damager).getShooter():null;
	}
	
	static double calculateEnchantmentDamageIncrease(ItemStack weapon, Entity damager, LivingEntity target) {
		double dmg = 0.0;
		boolean isBow = (weapon!=null && weapon.getType()==Material.BOW); //An exception for melee'ing with bows.
		if (isBow && (damager instanceof Arrow)) {
			double dmg1 = (weapon.containsEnchantment(Enchantment.ARROW_DAMAGE))?1.0+weapon.getEnchantmentLevel(Enchantment.ARROW_DAMAGE)*0.5:0.0;
			addToPlayerLogger(damager,"POWER",dmg1);
			dmg+=dmg1;
		} else {
			double mult1 = (weapon.containsEnchantment(Enchantment.DAMAGE_ALL))?1.0+weapon.getEnchantmentLevel(Enchantment.DAMAGE_ALL)*0.5:0.0;
			addToPlayerLogger(damager,"SHARPNESS",mult1);
			dmg+=mult1;
			if (weapon.containsEnchantment(Enchantment.DAMAGE_ARTHROPODS) &&
					(target instanceof Spider)) {
				mult1 = 1.0+weapon.getEnchantmentLevel(Enchantment.DAMAGE_ARTHROPODS)*2.5;
				addToPlayerLogger(damager,"BANE OF ARTHROPODS",mult1);
				dmg+=mult1;
			}
			if (weapon.containsEnchantment(Enchantment.DAMAGE_UNDEAD) &&
					(target instanceof Monster) && MonsterController.isUndead((Monster)target)) {
				mult1 = 1.0+weapon.getEnchantmentLevel(Enchantment.DAMAGE_UNDEAD)*2.5;
				addToPlayerLogger(damager,"SMITE",mult1);
				dmg+=mult1;
			}
		}
		return dmg;
	}
	
	static double calculateArtifactAbilityDamageIncrease(ItemStack weapon, Entity damager,
			LivingEntity target) {
		double dmg = 0.0;
		double dmg1 = GenericFunctions.getAbilityValue(ArtifactAbility.DAMAGE,weapon);
		addToPlayerLogger(damager,"Artifact Ability - DAMAGE",dmg1);
		dmg+=dmg1;
		dmg1 = calculateExecutionDamage(weapon,target);
		addToPlayerLogger(damager,"Artifact Ability - EXECUTION",dmg1);
		dmg+=dmg1;
		dmg1 = calculateHighwinderDamage(weapon,damager);
		addToPlayerLogger(damager,"Artifact Ability - HIGHWINDER",dmg1);
		dmg+=dmg1;
		return dmg;
	}
	
	private static double calculateHighwinderDamage(ItemStack weapon, Entity damager) {
		double dmg = 0.0;
		if (damager instanceof Player) {
			Player p = (Player)damager;
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			dmg += 93.182445*pd.velocity*GenericFunctions.getAbilityValue(ArtifactAbility.HIGHWINDER, weapon);
		}
		return dmg;
	}

	static double calculateExecutionDamage(ItemStack weapon, LivingEntity target) {
		if (target!=null) {
			return getPercentHealthMissing(target)
					/20
					*GenericFunctions.getAbilityValue(ArtifactAbility.EXECUTION,weapon);
		}
		return 0.0;
	}
	
	//Returns between 0-100.
	static double getPercentHealthRemaining(LivingEntity target) {
		return ((target.getHealth()/target.getMaxHealth())*100);
	}
	public static double getPercentHealthMissing(LivingEntity target) {
		return 100-getPercentHealthRemaining(target);
	}

	static double calculateArtifactAbilityMultiplier(ItemStack weapon, Entity damager, LivingEntity target) {
		double mult = 1.0;
		double mult1 = calculateBeliggerentMultiplier(weapon,damager);
		addMultiplierToPlayerLogger(damager,"Belliggerent Mult",mult1);
		mult*=mult1;
		return mult;
	}

	static double calculatePlayerCriticalStrike(ItemStack weapon, Entity damager) {
		return calculatePlayerCriticalStrike(weapon,damager,false);
	}
	
	static double calculatePlayerCriticalStrike(ItemStack weapon, Entity damager, boolean isCriticalStrike) {
		boolean criticalstrike=false;
		double critchance = 0.0;
		critchance += calculateCriticalStrikeChance(weapon, damager);
		TwosideKeeper.log("Crit Strike chance is "+critchance,4);
		criticalstrike = isCriticalStrike(critchance,isCriticalStrike);
		if (damager instanceof Player && criticalstrike) {
			Player p = (Player)damager;
			p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 1.0f, 1.0f);
		}
		return criticalstrike?(calculateCriticalStrikeMultiplier(weapon)):1.0;
	}
	
	static double calculateCriticalStrikeChance(ItemStack weapon, Entity damager) {
		double critchance = 0.0;
		critchance += 0.01*GenericFunctions.getAbilityValue(ArtifactAbility.CRITICAL,weapon);
		LivingEntity shooter = getDamagerEntity(damager);
		if (shooter!=null) {
			if (shooter instanceof Player) {
				Player p = (Player)shooter;
				critchance += (GenericFunctions.isStriker(p)?0.2:0.0);
				critchance += ItemSet.TotalBaseAmountBasedOnSetBonusCount(p,ItemSet.PANROS,4,4)/100d;
				critchance += (GenericFunctions.isRanger(p)?(GenericFunctions.getPotionEffectLevel(PotionEffectType.SLOW, p)+1)*0.1:0.0);
			}
		}
		return critchance;
	}

	//Chance is between 0.0-1.0. 1.0 is 100%.
	static boolean isCriticalStrike(double chance) {
		return isCriticalStrike(chance,false);
	}
	
	static boolean isCriticalStrike(double chance, boolean isCriticalStrike) {
		return (Math.random()<=chance || isCriticalStrike);
	}
	
	static double calculateCriticalStrikeMultiplier(ItemStack weapon) {
		double critdmg=2.0;
		if (ArtifactAbility.containsEnchantment(ArtifactAbility.CRIT_DMG, weapon)) {
			critdmg+=(GenericFunctions.getAbilityValue(ArtifactAbility.CRIT_DMG,weapon))/100d;
		}
		return critdmg;
	}
	
	static double calculateBeliggerentMultiplier(ItemStack weapon, Entity damager) {
		double mult=1.0;
		if (damager instanceof Player) {
			Player p = (Player)damager;
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			mult+=pd.swordcombo*GenericFunctions.getAbilityValue(ArtifactAbility.COMBO, weapon)/100d;
		}
		return mult;
	}
	
	static double calculateStrikerMultiplier(Entity damager) {
		double mult=1.0;
		if (damager instanceof Player) {
			Player p = (Player)damager;
			if (GenericFunctions.isStriker(p)) {
				double mult1 = (1-(p.getHealth()/p.getMaxHealth()));
				addMultiplierToPlayerLogger(damager,"Striker Missing Health Mult",mult1);
				mult += mult1;
				mult1 = 0.10;
				addMultiplierToPlayerLogger(damager,"Striker Passive Mult",mult1);
				mult += mult1; //10% damage increase - striker passive.
			}
		}
		return mult;
	}

	static double calculateRangerMultiplier(Entity damager) {
		double mult=1.0;
		LivingEntity shooter = getDamagerEntity(damager);
		if (shooter instanceof Player) { 
			Player p = (Player)shooter;
			if (GenericFunctions.isRanger(p)) { 
				double mult1 = 4.0;
				addMultiplierToPlayerLogger(damager,"Ranger Passive Mult",mult1);
				mult *= mult1; //x4 damage - Ranger passive.
			}
		}
		return mult;
	}
	
	static double calculatePotionEffectMultiplier(LivingEntity damager) {
		double mult = 1.0;
		if (damager.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
			double mult1 = 1.0+(GenericFunctions.getPotionEffectLevel(PotionEffectType.INCREASE_DAMAGE, damager)+1)*0.1;
			addMultiplierToPlayerLogger(damager,"STRENGTH Mult",mult1);
			mult *= mult1;
		}

		if (damager.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
			int weaknesslv = Math.abs(GenericFunctions.getPotionEffectLevel(PotionEffectType.WEAKNESS, damager))+1;
			if (weaknesslv<=10) {
				double mult1 = 1.0-(weaknesslv*0.1);
				addMultiplierToPlayerLogger(damager,ChatColor.RED+"WEAKNESS Mult",-mult1);
				mult *= 1.0-(weaknesslv*0.1);
			} else {
				addMultiplierToPlayerLogger(damager,ChatColor.RED+"WEAKNESS Mult",0.0);
				mult = 0.0;
			}
		}
		return mult;
	}
	
	static boolean meleeWithBow(ItemStack weapon, Entity damager) {
		if (weapon!=null && 
			weapon.getType()==Material.BOW &&
			(damager instanceof Player)) {
			//That means this player melee'd with a bow.
			TwosideKeeper.log("Melee with bow",5);
			return true;
		} else {
			return false;
		}
	}

	static double calculateBowDrawbackMultiplier(ItemStack weapon, Entity damager) {
		double mult=1.0;
		LivingEntity shooter = getDamagerEntity(damager);
		if (weapon.getType()==Material.BOW &&
			shooter instanceof Player) {
			Player p = (Player)shooter;
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			double mult1 = (pd.lastarrowwasinrangermode)?(pd.lastarrowpower/9000000d):(pd.lastarrowpower/9d);
			addMultiplierToPlayerLogger(damager,"Base Arrow Damage Mult",mult1);
			mult = mult1;
		}
		TwosideKeeper.log("mult is "+mult,5); 
		return mult;
	}

	private static void applyOnHitEffects(ItemStack weapon, double dmg, Entity damager, LivingEntity target) {
		applyOnHitEffects(weapon,dmg,damager,target,false);
	}

	static void applyOnHitEffects(ItemStack weapon, double dmg, Entity damager, LivingEntity target, boolean headshot) {
		LivingEntity shooter = getDamagerEntity(damager);
		if ((shooter instanceof Player) && target!=null) {
			Player p = (Player)shooter;
			if (damager instanceof TippedArrow) {
				TippedArrow a = (TippedArrow)damager;
				if (a.hasMetadata("EXPLODE_ARR")) {
					//Create an explosion.
					TwosideKeeper.log("In here", 5);
					Location hitloc = aPlugin.API.getArrowHitLocation(target, a);
					GenericFunctions.DealExplosionDamageToEntities(hitloc, NewCombat.CalculateWeaponDamage(p,null)+40, 6);
					p.playSound(hitloc, Sound.ENTITY_ENDERDRAGON_FIREBALL_EXPLODE, 0.5f, 1.0f);
					aPlugin.API.sendSoundlessExplosion(hitloc, 2);
				}
				if (a.hasMetadata("TRAP_ARR")) {
					int slownesslv=0;
					if (target.hasPotionEffect(PotionEffectType.SLOW)) {
						slownesslv = GenericFunctions.getPotionEffectLevel(PotionEffectType.SLOW, target)+1;
						target.removePotionEffect(PotionEffectType.SLOW);
					}
					target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,20*5,slownesslv));
				}
				if (a.hasMetadata("POISON_ARR")) {
					int poisonlv=0;
					target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,20*20,0));
				}
			}
			if (GenericFunctions.isArtifactEquip(p.getEquipment().getItemInMainHand()) &&
					GenericFunctions.isArtifactWeapon(p.getEquipment().getItemInMainHand())) {		
				double ratio = 1.0-CalculateDamageReduction(1,target,p);
				if (p.getEquipment().getItemInMainHand().getType()!=Material.BOW) {
					AwakenedArtifact.addPotentialEXP(p.getEquipment().getItemInMainHand(), (int)((ratio*20)+5)*((headshot)?2:1), p);
				} else {
					PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
					pd.storedbowxp+=(int)((ratio*20)+5)*((headshot)?2:1);
					pd.lasthittarget=TwosideKeeper.getServerTickTime();
				}
				increaseArtifactArmorXP(p,(int)(ratio*10)+1);
				List<LivingEntity> hitlist = getAOEList(weapon,target);
				
				boolean applyDeathMark=false;
				
				if (ArtifactAbility.getEnchantmentLevel(ArtifactAbility.DEATHMARK, p.getEquipment().getItemInMainHand())>0) {
					applyDeathMark=true;
				}
				
				for (int i=0;i<hitlist.size();i++) {
					if (!hitlist.get(i).equals(target)) {
						//hitlist.get(i).damage(dmg);
						GenericFunctions.DealDamageToMob(CalculateDamageReduction(dmg,target,damager), hitlist.get(i), shooter, weapon, "AoE Damage");
					};
					if (applyDeathMark) {
						GenericFunctions.ApplyDeathMark(hitlist.get(i));
					}
				}
				
				final List<LivingEntity> finallist = hitlist;
				Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("TwosideKeeper"), new Runnable() {
					public void run() {
						for (int i=0;i<finallist.size();i++) {
							LivingEntity le = finallist.get(i);
							if (le!=null && !le.isDead() && !le.hasPotionEffect(PotionEffectType.UNLUCK)) {
								GenericFunctions.ResetMobName(le);
								//They don't have death marks anymore, so we just remove their name color.
							}
						}
					}}
				,100);
				
				healDefenderSaturation(p);
						
				increaseSwordComboCount(weapon, p);
			}
		}
	}
	
	private static void healDefenderSaturation(Player p) {
		if (GenericFunctions.isDefender(p) && p.getSaturation()<20) {
			p.setSaturation(p.getSaturation()+1);
		}
	}

	public static void increaseArtifactArmorXP(Player p, int exp) {
		for (int i=0;i<p.getEquipment().getArmorContents().length;i++) {
			if (GenericFunctions.isArtifactEquip(p.getEquipment().getArmorContents()[i]) &&
    				GenericFunctions.isArtifactArmor(p.getEquipment().getArmorContents()[i])) {
				AwakenedArtifact.addPotentialEXP(p.getEquipment().getArmorContents()[i], exp, p);
			}
		}
	}

	static void increaseSwordComboCount(ItemStack weapon, Player p) {
		if (ArtifactAbility.containsEnchantment(ArtifactAbility.COMBO, p.getEquipment().getItemInMainHand())) {
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			if (pd.last_swordhit+40>=TwosideKeeper.getServerTickTime()) {
				pd.last_swordhit=TwosideKeeper.getServerTickTime();
				pd.swordcombo++;
			} else {
				pd.last_swordhit=TwosideKeeper.getServerTickTime();
				pd.swordcombo=1;
			} 
		}
	}
	
	static List<LivingEntity> getAOEList(ItemStack weapon, LivingEntity target) {
		List<LivingEntity> list = new ArrayList<LivingEntity>();
		if (ArtifactAbility.containsEnchantment(ArtifactAbility.AOE, weapon)) {
			double aoerange = GenericFunctions.getAbilityValue(ArtifactAbility.AOE, weapon); 
			if (target!=null) {
				List<Entity> nearbylist=target.getNearbyEntities(aoerange,aoerange,aoerange);
				list = trimNonLivingEntities(nearbylist);
				//list.remove(target);
			}
		}
		list.add(target);
		return list;
	}
	
	public static List<LivingEntity> trimNonLivingEntities(List<Entity> entitylist) {
		List<LivingEntity> livinglist = new ArrayList<LivingEntity>();
		for (int i=0;i<entitylist.size();i++) {
			if ((entitylist.get(i) instanceof LivingEntity) && !(entitylist.get(i) instanceof Player)) {
				livinglist.add((LivingEntity)entitylist.get(i));
			}
		}
		return livinglist;
	}
	
	public static List<Monster> trimNonMonsterEntities(List<Entity> entitylist) {
		List<Monster> livinglist = new ArrayList<Monster>();
		for (int i=0;i<entitylist.size();i++) {
			if ((entitylist.get(i) instanceof Monster) && !(entitylist.get(i) instanceof Player)) {
				livinglist.add((Monster)entitylist.get(i));
			}
		}
		return livinglist;
	}

	static public double CalculateDamageReduction(double basedmg,LivingEntity target,Entity damager) {
		
		double dmgreduction = 0.0;
		
		int protectionlevel = 0;
		int resistlevel = 0;
		int partylevel = 0;
		
		if (target instanceof LivingEntity) {
			ItemStack[] armor = target.getEquipment().getArmorContents();
			if (target instanceof Player) {
				if (GenericFunctions.HasFullRangerSet((Player)target)) {
					dmgreduction+=20.0;
				}
			}
			
			for (int i=0;i<armor.length;i++) {
				if (armor[i]!=null) {
					//Check for Protection enchantment.
					//Each Protection level gives 1% extra damage reduction.
					if (armor[i].getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL)>0) {
						protectionlevel+=armor[i].getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL);
					}
					if ((damager instanceof Projectile) && armor[i].getEnchantmentLevel(Enchantment.PROTECTION_PROJECTILE)>0) {
						protectionlevel+=armor[i].getEnchantmentLevel(Enchantment.PROTECTION_PROJECTILE);
					}
					if ((damager instanceof Creeper) && armor[i].getEnchantmentLevel(Enchantment.PROTECTION_EXPLOSIONS)>0) {
						protectionlevel+=armor[i].getEnchantmentLevel(Enchantment.PROTECTION_EXPLOSIONS);
					}
					
					boolean isBlockArmor = GenericFunctions.isHardenedItem(armor[i]);
					
					if (target instanceof Monster) {
						isBlockArmor=true;
					}
		
					//If this is an artifact armor, we totally override the base damage reduction.
					if (GenericFunctions.isArmor(armor[i]) && Artifact.isArtifact(armor[i])) {
						//Let's change up the damage.
						//double dmgval = ArtifactItemType.valueOf(Artifact.returnRawTool(armor[i].getType())).getDamageAmt(armor[i].getEnchantmentLevel(Enchantment.LUCK));
						double dmgval=-1;
						if (dmgval!=-1) {
							dmgreduction += dmgval;
						}
					} else {
						switch (armor[i].getType()) {
							case LEATHER_BOOTS:
							case LEATHER_LEGGINGS:
							case LEATHER_CHESTPLATE:
							case LEATHER_HELMET: {
								dmgreduction+=3*((isBlockArmor)?2:1);
							}break;
							case IRON_BOOTS:
							case IRON_LEGGINGS:
							case IRON_CHESTPLATE:
							case IRON_HELMET: {
								dmgreduction+=5*((isBlockArmor)?2:1);
							}break;
							case GOLD_BOOTS:
							case GOLD_LEGGINGS:
							case GOLD_CHESTPLATE:
							case GOLD_HELMET: {
								dmgreduction+=10*((isBlockArmor)?2:1);
							}break;
							case DIAMOND_BOOTS:
							case DIAMOND_LEGGINGS:
							case DIAMOND_CHESTPLATE:
							case DIAMOND_HELMET: {
								dmgreduction+=8*((isBlockArmor)?2:1);
							}break;
						}
					}
					
					if (GenericFunctions.isArtifactEquip(armor[i])) {
						double reductionamt = GenericFunctions.getAbilityValue(ArtifactAbility.DAMAGE_REDUCTION, armor[i]);
						dmgreduction+=reductionamt;
						TwosideKeeper.log("Reducing damage by "+reductionamt+"%",5);
						/*if (ArtifactAbility.containsEnchantment(ArtifactAbility.GREED, armor[i])) {
							dmgreduction /= ArtifactAbility.containsEnchantment(ArtifactAbility.GREED, armor[i])?2:1;
						}*/
					}
				}
			}
			
			
			//Check for resistance effect.
			Collection<PotionEffect> target_effects = target.getActivePotionEffects();
			for (int i=0;i<target_effects.size();i++) {
				if (Iterables.get(target_effects, i).getType().equals(PotionEffectType.DAMAGE_RESISTANCE)) {
					resistlevel = Iterables.get(target_effects, i).getAmplifier()+1;
					TwosideKeeper.log("Resistance level is "+resistlevel,5);
				}
			}
			
		}
		
		double setbonus = 1.0;
		
		if (target instanceof Player) {
			Player p = (Player)target;
	    	PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			partylevel = pd.partybonus;
			if (partylevel>9) {partylevel=9;}
			if (p.getLocation().getY()>=0) {TwosideKeeper.log("Light level: "+p.getLocation().add(0,0,0).getBlock().getLightLevel(),5);}
			for (int i=0;i<p.getEquipment().getArmorContents().length;i++) {
				if (ArtifactAbility.containsEnchantment(ArtifactAbility.SHADOWWALKER, p.getEquipment().getArmorContents()[i]) &&
						p.getLocation().getY()>=0 &&
						p.isOnGround() && p.getLocation().add(0,0,0).getBlock().getLightLevel()<=4) {
					double dmgreduce = 1d-(GenericFunctions.getAbilityValue(ArtifactAbility.SHADOWWALKER, p.getEquipment().getArmorContents()[i])/100d);
					basedmg *= dmgreduce; 
					TwosideKeeper.log("Base damage became "+(dmgreduce*100)+"% of original amount.",5);
				}
				/*if (ArtifactAbility.containsEnchantment(ArtifactAbility.GREED, p.getEquipment().getArmorContents()[i])) {
					dmgreduction /= ArtifactAbility.containsEnchantment(ArtifactAbility.GREED, p.getEquipment().getArmorContents()[i])?2:1;
				}*/
			}
			/*if (ArtifactAbility.containsEnchantment(ArtifactAbility.GREED, p.getEquipment().getItemInMainHand())) {
				dmgreduction /= ArtifactAbility.containsEnchantment(ArtifactAbility.GREED, p.getEquipment().getItemInMainHand())?2:1;
			}*/
			setbonus = ((100-ItemSet.TotalBaseAmountBasedOnSetBonusCount(p, ItemSet.SONGSTEEL, 4, 4))/100d);
		}
		
		//Blocking: -((p.isBlocking())?ev.getDamage()*0.33:0) //33% damage will be reduced if we are blocking.
		//Shield: -((p.getEquipment().getItemInOffHand()!=null && p.getEquipment().getItemInOffHand().getType()==Material.SHIELD)?ev.getDamage()*0.05:0) //5% damage will be reduced if we are holding a shield.

		TwosideKeeper.log("Protection level: "+protectionlevel, 5);
		
		resistlevel=(resistlevel>10)?10:resistlevel;
		protectionlevel=(protectionlevel>100)?100:protectionlevel;
		//partylevel=(partylevel>9)?9:partylevel;
		double finaldmg=(basedmg-(basedmg*(dmgreduction/100.0d)))
				*((10-resistlevel)*0.1)
				*((100-protectionlevel)*0.01)
				//*((10-partylevel)*0.1)
				*setbonus
				*((target instanceof Player && ((Player)target).isBlocking())?(GenericFunctions.isDefender((Player)target))?0.30:0.50:1)
				*((target instanceof Player)?((GenericFunctions.isDefender((Player)target))?0.9:(target.getEquipment().getItemInOffHand()!=null && target.getEquipment().getItemInOffHand().getType()==Material.SHIELD)?0.95:1):1);
	
		if (basedmg!=finaldmg) {
			TwosideKeeper.log("Original damage was: "+basedmg,5);
			TwosideKeeper.log(finaldmg+" damage calculated for: "+target.getName()+".",5);
		}
	
		if (damager instanceof Player) {
			Player p = (Player)damager; 
			double lifestealpct = calculateLifeStealAmount(p);
			
			double healamt = finaldmg*lifestealpct;
			//log("Healed "+healamt+" damage.",2);
			double newhealth = p.getHealth()+healamt;
			if (newhealth>p.getMaxHealth()) {
				p.setHealth(p.getMaxHealth());
			} else {
				p.setHealth(newhealth);
			}
		}
		if (target instanceof Player) {
			Player p = (Player)target;
	    	PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			pd.prev_armordef = finaldmg;
		}
		return finaldmg;
	}
	
	public static double calculateLifeStealAmount(Player p) {
		double lifestealpct = GenericFunctions.getAbilityValue(ArtifactAbility.LIFESTEAL, p.getEquipment().getItemInMainHand())/100;
		lifestealpct += ItemSet.TotalBaseAmountBasedOnSetBonusCount(p, ItemSet.DAWNTRACKER, 4, 4)/100d;
		return lifestealpct;
	}

	private static void playerAddArtifactEXP(LivingEntity target, double dmg) {
		if (target instanceof Player) {
			Player p = (Player)target;
			increaseArtifactArmorXP(p,(int)dmg);
		}
	}
	
	private static double calculateHeadshotMultiplier(Entity arrow, ItemStack weapon, LivingEntity target) {
		double mult = 1.0;
		if (target instanceof Monster &&
				arrow instanceof Projectile) {
			//Headshot conditions:
    		/*
    		 * If Target is WEARING Helmet:
    		 * ->Direction difference has to be anywhere between 110-250 degrees.
    		 * 
    		 * In Addition, for ALL targets:
    		 * ->Y must be greater than or equal to Eye Height - 0.22f .
    		 * ->Perks or abilities with increased headshot hitbox size will multiply the 0.08 further, giving
    		 * 	more access to the target's body.
    		 */
			Monster m = (Monster)target;
			Projectile proj = (Projectile)arrow;
			Location arrowLoc = proj.getLocation();
			if (proj instanceof Arrow) {
				arrowLoc = aPlugin.API.getArrowHitLocation(target, (Arrow)proj); 
			}
    		Location monsterHead = m.getEyeLocation();
    		TwosideKeeper.log("Distance: "+(arrowLoc.distanceSquared(monsterHead)), 3);
    		
			double headshotvaly=0.22/TwosideKeeper.HEADSHOT_ACC;
			double directionvaly=0.25/TwosideKeeper.HEADSHOT_ACC;
			
			if (proj.getShooter() instanceof Player) {
				Player p = (Player)proj.getShooter();
				if (GenericFunctions.isRanger(p) && 
					GenericFunctions.getBowMode(weapon)==BowMode.SNIPE) {
					aPlugin.API.sendSoundlessExplosion(arrowLoc, 1);
					headshotvaly *= 4;
				}
				
				if (GenericFunctions.isArtifactEquip(weapon) &&
						ArtifactAbility.containsEnchantment(ArtifactAbility.MARKSMAN, weapon)) {
					headshotvaly *= 1+(GenericFunctions.getAbilityValue(ArtifactAbility.MARKSMAN, weapon)/100d);
				}
				
				if (proj.getTicksLived()>=4 || GenericFunctions.isRanger(p)) {
					if (arrowLoc.distanceSquared(monsterHead)<=0.3*headshotvaly) {
						
						PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
						
						if (GenericFunctions.isRanger(p) && GenericFunctions.getBowMode(p.getEquipment().getItemInMainHand())==BowMode.SNIPE) {
							if (pd.headshotcombo<8) {pd.headshotcombo++;}
							double headshotincrease = (2+(pd.headshotcombo*0.25));
							mult*=headshotincrease;
			    			p.sendMessage(ChatColor.DARK_RED+"Headshot! x"+(headshotincrease)+" Damage");
			    			if (p.hasPotionEffect(PotionEffectType.SLOW)) {
			    				//Add to the current stack of SLOW.
			    				for (int i1=0;i1<p.getActivePotionEffects().size();i1++) {
			    					if (Iterables.get(p.getActivePotionEffects(), i1).getType().equals(PotionEffectType.SLOW)) {
			    						int lv = Iterables.get(p.getActivePotionEffects(), i1).getAmplifier();
			    						TwosideKeeper.log("New Slowness level: "+lv,5);
			    						p.removePotionEffect(PotionEffectType.SLOW);
			    						p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,99,lv+1));
			    						p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
			    						p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,99,lv+1));
			    						break;
			    					}
			    				}
			    			} else {
			    				p.removePotionEffect(PotionEffectType.SLOW);
			    				p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,99,0));
			    				p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
			    				p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,99,0));
			    			}
    						final Player pl = p;
    						Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("TwosideKeeper"), new Runnable() {
    	    					public void run() {
    	    	    				if (pl!=null && pl.isOnline() && !pl.hasPotionEffect(PotionEffectType.SLOW)) {
    	    	    					PlayerStructure pd = (PlayerStructure)TwosideKeeper.playerdata.get(pl.getUniqueId());
    	    	    					pd.headshotcombo=0;
    	    	    				}
    	    					}}
    	    				,100);
			    			p.playSound(p.getLocation(), Sound.ENTITY_LIGHTNING_IMPACT, 0.1f, 0.24f);
						} else {
							mult*=2.0;
			    			p.sendMessage(ChatColor.DARK_RED+"Headshot! x2 Damage");

							if (GenericFunctions.isRanger(p) && GenericFunctions.getBowMode(p.getEquipment().getItemInMainHand())==BowMode.DEBILITATION) {
								if (m.hasPotionEffect(PotionEffectType.BLINDNESS)) {
				    				//Add to the current stack of BLINDNESS.
				    				for (int i1=0;i1<m.getActivePotionEffects().size();i1++) {
				    					if (Iterables.get(m.getActivePotionEffects(), i1).getType().equals(PotionEffectType.BLINDNESS)) {
				    						int lv = Iterables.get(m.getActivePotionEffects(), i1).getAmplifier();
				    						TwosideKeeper.log("New BLINDNESS level: "+lv,5);
				    						p.playSound(p.getLocation(), Sound.ENTITY_RABBIT_ATTACK, 0.1f, 0.1f+((lv+1)*0.5f));
				    						m.removePotionEffect(PotionEffectType.BLINDNESS);
				    						m.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,400,lv+1));
				    						break;
				    					}
				    				}
				    			} else {
				    				m.removePotionEffect(PotionEffectType.BLINDNESS);
				    				m.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,400,0));
									p.playSound(p.getLocation(), Sound.ENTITY_RABBIT_ATTACK, 0.1f, 0.1f);
				    			}
							}
						}

					}
				}
			}
		}
		return mult;
	}
	
	private static boolean arrowWithinHelmetBounds(Location arrowLoc, Monster m, double direction_acc) {
		double dir_diff = (arrowLoc.getDirection().setX(0.0).setZ(0.0)).subtract((m.getLocation().getDirection().setX(0.0).setZ(0.0))).getY();
		TwosideKeeper.log("Directional difference: "+dir_diff,4);
		if (dir_diff<=direction_acc || m.getEquipment().getHelmet()==null || !m.getEquipment().getHelmet().getType().toString().contains("HELMET")) {
			return true;
		}
		return false;
	}

	private static boolean arrowWithinYBounds(Location arrowLoc,Location monsterHead,double headshot_acc) {
		TwosideKeeper.log("Y diff is "+(arrowLoc.getY()-monsterHead.getY()), 4);
		return Math.abs(arrowLoc.getY()-monsterHead.getY())<=headshot_acc || arrowLoc.getY()>monsterHead.getY();
	}

	private static void applyOnHitMobEffects(LivingEntity target, Entity damager, double dmg) {
		if (target instanceof Player) {
			Player p = (Player)target;
			if (GenericFunctions.isDefender(p)) {
				int resistlevel = GenericFunctions.getPotionEffectLevel(PotionEffectType.DAMAGE_RESISTANCE, p);
				if (resistlevel<4) {
					p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
					p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,100,resistlevel+1));
				} else {
					p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
					p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,100,resistlevel));
				}
				if (p.isBlocking() && ItemSet.hasFullSet(p, ItemSet.SONGSTEEL)) {
					PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
					pd.vendetta_amt+=(dmg-CalculateDamageReduction(dmg,target,damager))*0.3;
					aPlugin.API.sendActionBarMessage(p, ChatColor.YELLOW+"Vendetta: "+ChatColor.GREEN+Math.round(pd.vendetta_amt)+" dmg stored");
				}
			}
			if (damager instanceof Enderman) {
	    		if (MonsterController.getMonsterDifficulty(((Monster)damager))==MonsterDifficulty.HELLFIRE) {
						for (int i=0;i<4;i++) {
			    			if (Math.random()<=0.2) {
							Monster mm = MonsterController.spawnAdjustedMonster(MonsterType.ENDERMITE, damager.getLocation().add(0,1,0));
							mm.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,Integer.MAX_VALUE,2));
						}
					}
	    		}
			}
		}
	}
	
	public static void addToPlayerLogger(Entity p, String event, double val) {
		LivingEntity l = getDamagerEntity(p);
		if (l!=null && l instanceof Player) {
			PlayerStructure pd = PlayerStructure.GetPlayerStructure((Player)l);
			if (pd.damagelogging) {
				pd.damagedata.addEventToLogger(event, val);
			}
		}
	}
	
	public static void addToLoggerTotal(Entity p, double val) {
		LivingEntity l = getDamagerEntity(p);
		if (l!=null && l instanceof Player) {
			PlayerStructure pd = PlayerStructure.GetPlayerStructure((Player)l);
			if (pd.damagelogging) {
				pd.damagedata.addCalculatedActualDamage(val);
				pd.damagedata.addCalculatedTotalDamage(val);
			}
		}
	}
	
	private static void addMultiplierToPlayerLogger(Entity p, String event, double val) {
		LivingEntity l = getDamagerEntity(p);
		if (l!=null && l instanceof Player) {
			PlayerStructure pd = PlayerStructure.GetPlayerStructure((Player)l);
			if (pd.damagelogging) {
				pd.damagedata.addMultiplierToLogger(event, val);
			}
		}
	}

	public static double calculateAbsorptionHearts(LivingEntity target, double finaldmg) {
		if (target.hasPotionEffect(PotionEffectType.ABSORPTION)) {
			int abslv = GenericFunctions.getPotionEffectLevel(PotionEffectType.ABSORPTION, target)+1;
			double healthabs = abslv*4; //The amount of health absorbed per level.
			if (DamageIsSmallerThanAbsorptionAmount(finaldmg,healthabs)) {
				SubtractDamageFromAbsorption(target, finaldmg,healthabs);
				return 0.0; //Final damage becomes 0.
			} else {
				SubtractDamageFromAbsorption(target, finaldmg,healthabs);
				return finaldmg-healthabs;
			}
		}
		TwosideKeeper.log("New final damage is "+finaldmg, 4);
		return finaldmg;
	}

	private static void SubtractDamageFromAbsorption(LivingEntity target, double finaldmg, double healthabs) {
		int lvsToRemove = (int)(finaldmg/4);
		if (finaldmg % 4 > 0) {
			lvsToRemove++;
		}
		RemoveAbsorptionLevels(target,2);
	}

	private static void RemoveAbsorptionLevels(LivingEntity target, int amountOfAbsorptionLevels) {
		int duration = GenericFunctions.getPotionEffectDuration(PotionEffectType.ABSORPTION, target);
		int lv = GenericFunctions.getPotionEffectLevel(PotionEffectType.ABSORPTION, target);
		target.removePotionEffect(PotionEffectType.ABSORPTION);
		if (amountOfAbsorptionLevels<lv) {
			target.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION,duration,lv-amountOfAbsorptionLevels));
		}
	}

	private static boolean DamageIsSmallerThanAbsorptionAmount(double finaldmg, double healthabs) {
		return finaldmg<=healthabs;
	}

	public static double CalculateDodgeChance(Player p) {
		double dodgechance = 0.0d;
		dodgechance+=(ArtifactAbility.calculateValue(ArtifactAbility.DODGE, p.getEquipment().getItemInMainHand().getEnchantmentLevel(Enchantment.LUCK), ArtifactAbility.getEnchantmentLevel(ArtifactAbility.DODGE, p.getEquipment().getItemInMainHand()))/100d);
		
		for (int i=0;i<p.getEquipment().getArmorContents().length;i++) {
			if (ArtifactAbility.containsEnchantment(ArtifactAbility.SHADOWWALKER, p.getEquipment().getArmorContents()[i]) &&
					p.isOnGround() && p.getLocation().getY()>=0 && p.getLocation().add(0,0,0).getBlock().getLightLevel()<=4) {
				dodgechance+=0.01*p.getEquipment().getArmorContents()[i].getEnchantmentLevel(Enchantment.LUCK);
			}
			ItemStack equip = p.getEquipment().getArmorContents()[i];
			if (GenericFunctions.isRanger(p) && equip!=null
					&& equip.getType()!=Material.AIR &&
					equip.hasItemMeta() && equip.getItemMeta().hasLore()) {
				if (equip.getItemMeta().getLore().contains(ChatColor.GOLD+""+ChatColor.BOLD+"Jamdak Set")) {
					dodgechance+=0.03;
				} else
				if (equip.getItemMeta().getLore().contains(ChatColor.GOLD+""+ChatColor.BOLD+"Darnys Set")) {
					dodgechance+=0.05;
				} else
				if (equip.getItemMeta().getLore().contains(ChatColor.GOLD+""+ChatColor.BOLD+"Alikahn Set")) {
					dodgechance+=0.08;
				} else
				if (equip.getItemMeta().getLore().contains(ChatColor.GOLD+""+ChatColor.BOLD+"Lorasaadi Set")) {
					dodgechance+=0.11;
				}
			}
		}
	
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		
		if (ArtifactAbility.containsEnchantment(ArtifactAbility.SHADOWWALKER, p.getEquipment().getItemInMainHand()) &&
				p.isOnGround() && p.getLocation().getY()>=0 && p.getLocation().add(0,0,0).getBlock().getLightLevel()<=4) {
			dodgechance+=0.01*p.getEquipment().getItemInMainHand().getEnchantmentLevel(Enchantment.LUCK);
		}
			
		dodgechance+=ItemSet.TotalBaseAmountBasedOnSetBonusCount(p,ItemSet.PANROS,3,3)/100d;
		if (p.isBlocking()) {
			dodgechance+=ItemSet.GetTotalBaseAmount(p, ItemSet.SONGSTEEL)/100d;
		}
		
		if (GenericFunctions.isStriker(p) &&
				93.182445*pd.velocity>4.317) {
			dodgechance+=0.2;
		}
		if (GenericFunctions.isRanger(p)) {
			dodgechance+=0.5;
		}
		if (pd.fulldodge) {
			dodgechance = 1.0;
		}
		return dodgechance;
	}

	public static double calculateDefenderAbsorbtion(LivingEntity entity, double dmg) {
		//See if we're in a party with a defender.
		if (entity instanceof Player) {
			Player p = (Player)entity;
			List<Player> partymembers = TwosideKeeperAPI.getPartyMembers(p);
			for (int i=0;i<partymembers.size();i++) {
				Player check = partymembers.get(i);
				if (PartyManager.IsInSameParty(p, check)) {
	    			TwosideKeeper.log("In here",5);
					if (!GenericFunctions.isDefender(p) && GenericFunctions.isDefender(check) &&
							check.isBlocking() &&
							!p.equals(check)) {
						//This is a defender. Transfer half the damage to them!
						
						dmg = dmg/2;
						//Send the rest of the damage to the defender.
						double defenderdmg = dmg;
						defenderdmg=NewCombat.CalculateDamageReduction(dmg, check, entity);
						GenericFunctions.DealDamageToMob(defenderdmg, check, p);
						TwosideKeeper.log("Damage was absorbed by "+check.getName()+". Took "+defenderdmg+" reduced damage. Original damage: "+dmg,2);
						break;
					}
				}
			} 
			TwosideKeeper.log("In here",5);
		}
		return dmg;
	}

	public static int CalculateGracefulDodgeTicks(Player p) {
		ItemStack[] equips = GenericFunctions.getEquipment(p);
		double totalduration = 0.0;
		for (int i=0;i<equips.length;i++) {
			totalduration+=GenericFunctions.getAbilityValue(ArtifactAbility.GRACEFULDODGE, equips[i]);
		}
		return (int)(totalduration*20);
	}
	
	
}
