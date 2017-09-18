package sig.plugin.TwosideKeeper;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.bukkit.Achievement;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.SpectralArrow;
import org.bukkit.entity.Spider;
import org.bukkit.entity.TippedArrow;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Wither;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.util.Vector;

import aPlugin.API;
import sig.plugin.TwosideKeeper.Boss.EliteZombie;
import sig.plugin.TwosideKeeper.Events.EntityDamagedEvent;
import sig.plugin.TwosideKeeper.Events.PlayerDodgeEvent;
import sig.plugin.TwosideKeeper.HelperStructures.AdvancedTitle;
import sig.plugin.TwosideKeeper.HelperStructures.ArtifactAbility;
import sig.plugin.TwosideKeeper.HelperStructures.BowMode;
import sig.plugin.TwosideKeeper.HelperStructures.BuffTemplate;
import sig.plugin.TwosideKeeper.HelperStructures.Channel;
import sig.plugin.TwosideKeeper.HelperStructures.DamageStructure;
import sig.plugin.TwosideKeeper.HelperStructures.ItemSet;
import sig.plugin.TwosideKeeper.HelperStructures.LivingEntityDifficulty;
import sig.plugin.TwosideKeeper.HelperStructures.MonsterDifficulty;
import sig.plugin.TwosideKeeper.HelperStructures.MonsterType;
import sig.plugin.TwosideKeeper.HelperStructures.PlayerMode;
import sig.plugin.TwosideKeeper.HelperStructures.WorldShop;
import sig.plugin.TwosideKeeper.HelperStructures.Common.BaublePouch;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;
import sig.plugin.TwosideKeeper.HelperStructures.Effects.EffectPool;
import sig.plugin.TwosideKeeper.HelperStructures.Effects.TemporaryBlock;
import sig.plugin.TwosideKeeper.HelperStructures.Effects.TemporaryBlockNode;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.ArtifactUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.DebugUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.EntityUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.IndicatorType;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.SoundUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.TextUtils;
import sig.plugin.TwosideKeeper.HolidayEvents.Christmas;
import sig.plugin.TwosideKeeper.Monster.ChallengeBlaze;
import sig.plugin.TwosideKeeper.Monster.ChallengeGhast;
import sig.plugin.TwosideKeeper.Monster.ChallengeSpider;
import sig.plugin.TwosideKeeper.Monster.ChallengeZombie;
import sig.plugin.TwosideKeeper.Monster.DarkSpider;
import sig.plugin.TwosideKeeper.Monster.DarkSpiderMinion;
import sig.plugin.TwosideKeeper.Monster.Dummy;
import sig.plugin.TwosideKeeper.Monster.GenericBoss;
import sig.plugin.TwosideKeeper.Monster.HellfireGhast;
import sig.plugin.TwosideKeeper.Monster.HellfireSpider;
import sig.plugin.TwosideKeeper.Monster.Knight;
import sig.plugin.TwosideKeeper.Monster.SniperSkeleton;
import sig.plugin.TwosideKeeper.PlayerStructures.DefenderStance;

public class CustomDamage {
	

	public static final int NONE = 0;
	public static final int CRITICALSTRIKE = 1;
	public static final int IGNOREDODGE = 2;
	public static final int TRUEDMG = 4;
	public static final int IGNORE_DAMAGE_TICK = 8; //Ignores damage ticks, which guarantees this attack will land regardless if the player's gotten hit by this before. 
	public static final int SPECIALATTACK = 16; //Used internally to specifically define a special attack.
	public static final int NOAOE = 32; //Prevents AoE from being applied again since this attack will be considered the AoE attack. Prevents recursion with AoE.
	public static final int CONTROLLED = 64; //If this damage application is under control from the damage queue.
	
	//////////////////THE FLAGS BELOW ARE SYSTEM FLAGS!! DO NOT USE THEM!
	public static final int IS_CRIT = 1; //System Flag. Used for telling a player structure their last hit was a crit.
	public static final int IS_HEADSHOT = 2; //System Flag. Used for telling a player structure their last hit was a headshot.
	public static final int IS_PREEMPTIVE = 4; //System Flag. Used for telling a player structure their last hit was a preemptive strike.
	public static final int IS_THORNS = 8; //System Flag. Used for telling a player structure their last hit was with thorns.

	static public boolean ApplyDamage(double damage, Entity damager, LivingEntity target, ItemStack weapon, String reason) {
		//TwosideKeeper.log("Weapon: "+weapon, 0);
		return ApplyDamage(damage,damager,target,weapon,reason,NONE);
	}

	/**
	 * Attempts to apply damage to a target. This method factors in all possibilities of dodging or damage not being applied,
	 * such as dodge chance, iframes, or not being allowed to be hit by the target due to nodamageticks, etc.<br><br>
	 * 
	 * Once the invulnerable check is successful, it proceeds to calculate the damage. If a weapon is provided, the DAMAGE is
	 * ignored and instead the WEAPON is used to calculate the damage instead. If a weapon is NOT provided, the DAMAGE value will
	 * be used unless it's 0. If the damage value is 0, this attack will automatically deal 1 damage (a punch).<br><br>
	 * 
	 * Finally, this method actually applies the damage properly by calling the correct event and dealing the correct damage. If a
	 * damager is not specified, the attack directly subtracts from the entity's health with .damage(double). Otherwise, .damage(double,Entity)
	 * is used to apply a proper damage event.<br><br>
	 * 
	 * If you want to ignore the dodge chance (an "always hit" attack) or you only want to calculate damage but not deal it, or
	 * you want to deal damage with no additional buffs, debuffs, multipliers, or damage reductions, you
	 * can call the separate pieces this method is composed of: InvulnerableCheck(), CalculateDamage(), and DealDamageToEntity()
	 * @param damage
	 * @param damager
	 * @param target
	 * @param weapon
	 * @param reason
	 * @param flags Specifies additional flags which modify the behavior of applying damage.
	 * 		Valid flags are:<br>
	 * &nbsp;&nbsp;&nbsp;&nbsp;NONE - Just a human-readable version of the value 0.<br>
	 * &nbsp;&nbsp;&nbsp;&nbsp;CRITICALSTRIKE - Force a Critical Strike.<br>
	 * &nbsp;&nbsp;&nbsp;&nbsp;IGNOREDODGE - Ignores all Dodge and invulnerability checks.<br>
	 * &nbsp;&nbsp;&nbsp;&nbsp;TRUEDMG - Ignores all additional calculations/reductions, applying the damage directly.<br>
	 * &nbsp;&nbsp;&nbsp;&nbsp;IGNORE_DAMAGE_TICK - Ignores the fact the entity is in an invulnerable state and applies the damage.<br>
	 * <br><b>Combining flags example:</b> CRITICALSTRIKE|IGNOREDODGE (Force a critical strike AND ignore invulnerability check)
	 * @return Whether or not this attack actually was applied. Returns false if it was dodged, nodamageticks, cancelled, etc.
	 */
	static public boolean ApplyDamage(double damage, Entity damager, LivingEntity target, ItemStack weapon, String reason, int flags) {
		long time = System.nanoTime();
		if (!isFlagSet(flags,CONTROLLED)) {
			TwosideKeeper.damagequeue++;
			if (TwosideKeeper.damagequeue>8) {
				flags = setFlag(flags,CONTROLLED);
				TwosideKeeper.damagequeuelist.add(new DamageStructure(damage,damager,target,weapon,reason,flags));
				return false; //Run it later.
			}
		}
		if (damage!=0.0 && weapon==null) {
			//Custom damage right here.
			flags = setFlag(flags,SPECIALATTACK);
		}
		if (getDamagerEntity(damager) instanceof Player) {
			Player p = (Player)getDamagerEntity(damager);
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			pd.lasthitproperties=NONE;
		}
		if (!InvulnerableCheck(damager,damage,target,weapon,reason,flags)) {
			double dmg = 0.0;
			if (isFlagSet(flags,TRUEDMG)) {
				//TwosideKeeper.log("Reason: "+reason, 0);
				if (reason!=null) {
					addToLoggerActual(damager,damage);
					dmg+=addToPlayerLogger(damager, target, reason, damage);
				} else {
					addToLoggerActual(damager,damage);
					dmg+=addToPlayerLogger(damager, target, ChatColor.GRAY+"Unknown", damage);
				}
			} else {
				dmg = CalculateDamage(damage, damager, target, weapon, reason, flags);
			}	
			dmg += CalculateBonusTrueDamage(damager, target, dmg);
			dmg += CalculatePVPDamageReduction(damager,target,dmg);
			if (damager!=null) {
				TwosideKeeper.logHealth(target,target.getHealth(),dmg,damager);
			}
			EntityDamagedEvent ev = new EntityDamagedEvent(target,damager,dmg,reason,flags);
			if (!Dummy.isDummy(target)) {
				Bukkit.getPluginManager().callEvent(ev);
			}

			setupDamagePropertiesForPlayer(damager,((reason!=null && reason.equalsIgnoreCase("thorns"))?IS_THORNS:0));
			if (!ev.isCancelled()) {
				//TwosideKeeper.log("Inside of here.", 0);
				LivingEntity shooter = getDamagerEntity(damager);
				if (shooter instanceof Player && target instanceof Player) {
					PlayerStructure pd = PlayerStructure.GetPlayerStructure((Player)target);
					pd.lastplayerHitBy=((Player)shooter).getName();
					TwosideKeeper.log("Set last hit by to "+pd.lastplayerHitBy+" for player "+((Player)target).getName(), 2);
				}
				DealDamageToEntity(dmg, damager, target, weapon, reason, flags);
				addToLoggerTotal(damager,dmg);
				TwosideKeeper.HeartbeatLogger.AddEntry("Damage Calculations", (int)(System.nanoTime()-time));time=System.nanoTime();
			} else {
				return false;
			}
			return true;
		} else {
			return false;
		}
	}

	private static double CalculatePVPDamageReduction(Entity damager, LivingEntity target, double dmg) {
		double dmgIncrease=0;
		LivingEntity shooter = getDamagerEntity(damager);
		if (shooter!=null && shooter instanceof Player) {
			Player p = (Player)shooter;
			if (PVP.isPvPing(p)) {
				dmgIncrease = -(dmg*0.6);
				//TwosideKeeper.log("Damage reduced due to PvP from "+dmg+" to "+(dmg+dmgIncrease), 2);
				return dmgIncrease; //Reduce damage by 60% in PVP.
			}
		}
		return dmgIncrease;
	}

	private static void UpdateWeaponUsedForShooting(Entity damager) {
		if (getDamagerEntity(damager) instanceof Player) {
			PlayerStructure pd = PlayerStructure.GetPlayerStructure((Player)(getDamagerEntity(damager)));
			pd.weaponUsedForShooting=null;
		}
	}

	private static double CalculateBonusTrueDamage(Entity damager, LivingEntity target, double dmg) {
		//TwosideKeeper.log("Run here. Damage: "+dmg, 0);
		if (getDamagerEntity(damager) instanceof Player) {
			LivingEntity shooter = getDamagerEntity(damager);
			double bonus_truedmg = 0;
			Player p = (Player)getDamagerEntity(damager);
			bonus_truedmg += API.getPlayerBonuses(p).getBonusTrueDamage();
			bonus_truedmg += ItemSet.HasSetBonusBasedOnSetBonusCount((Player)shooter, ItemSet.ALUSTINE, 7)?((Player)shooter).getLevel():0;
			if (PVP.isPvPing(p)) {
				bonus_truedmg += target.getMaxHealth()*0.01;
			}
			return bonus_truedmg;
		} else {
			double bonus_truedmg = 0;
			
			if (target instanceof Player) {
				if (TwosideKeeper.roominstances.size()>0) {
					Player p = (Player)target;
					PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
					if (pd.inTankChallengeRoom) {
						for (Room r: TwosideKeeper.roominstances) {
							bonus_truedmg+=r.getTankRoomTrueDamage();
							bonus_truedmg+=p.getMaxHealth()*r.getTankRoomTruePctDamage();
						}
					}
				}
			}
			
			return bonus_truedmg;
		}
	}

	public static double CalculateDamage(double damage, Entity damager, LivingEntity target, ItemStack weapon,
			String reason) {
		return CalculateDamage(damage,damager,target,weapon,reason,0);
	}
	
	public static double CalculateDamage(double damage, Entity damager, LivingEntity target, ItemStack weapon,
			String reason, int flags) {
		LivingEntity shooter = getDamagerEntity(damager);
		double dmg = 0.0;
		boolean headshot=false;
		boolean crit=false;
		boolean preemptive=false;
		if (shooter!=null && (shooter instanceof Player)) {
			if (weapon!=null) {
				dmg+=getBaseWeaponDamage(damage, weapon, damager, target, reason);
				//TwosideKeeper.log("Weapon: "+weapon, 0);
				//DebugUtils.showStackTrace();
				if (weapon.getType()==Material.BOW) {
					if ((damager instanceof Projectile)) {
						//TwosideKeeper.log("This is a projectile! Reason: "+reason+", Damager: "+damager.toString(), 0);
						dmg += addToPlayerLogger(damager,target,"Custom Arrow",calculateCustomArrowDamageIncrease(weapon,damager,target));
						dmg += addMultiplierToPlayerLogger(damager,target,"Ranger Mult",dmg * calculateRangerMultiplier(weapon,damager));
						double headshotdmg = addMultiplierToPlayerLogger(damager,target,"Headshot Mult",dmg * calculateHeadshotMultiplier(weapon,damager,target));
						if (headshotdmg!=0.0) {headshot=true;}
						dmg += headshotdmg;
						//TwosideKeeper.log("Damage currently is: "+dmg, 0);
						dmg += addMultiplierToPlayerLogger(damager,target,"Bow Drawback Mult",dmg * calculateBowDrawbackMultiplier(weapon,damager,target));
					}
				}
			} else {
				if (damage==0) { //This is a fist. (NULL weapon + 0 damage modifier)
					dmg += addToPlayerLogger(damager, target, "Fist", 1.0);
				} else { //This means we have custom damage. Use that instead.
					if (reason!=null) {
						dmg += addToPlayerLogger(damager, target, reason, damage);
					} else {
						dmg += addToPlayerLogger(damager, target, ChatColor.GRAY+"Unknown", damage);
					}
				}
			}
		} else {
			if (damage!=0.0) {
				dmg = damage;
				TwosideKeeper.log("Setting damage to "+dmg+" for "+reason+".", 5);
			} else 
			if (shooter instanceof LivingEntity) {
				dmg = calculateMobBaseDamage(shooter,target)*calculateMonsterDifficultyMultiplier(shooter);
			}
			if (damager instanceof Snowball) {
				Snowball sb = (Snowball)damager;
				if (sb.hasMetadata("SPIDERBALL")) {
					dmg = 10.0*10.0;
					reason = "Spider Ball";
					TwosideKeeper.log("Got here to damage.", 5);
					GenericFunctions.removeNoDamageTick(target, damager);
				}
			}
		}
		if (shooter!=null && shooter instanceof Player) {
			dmg += addToPlayerLogger(damager,target,"Execute",(((GenericFunctions.getAbilityValue(ArtifactAbility.EXECUTION, weapon, (Player)shooter)*5.0)*(1-(target.getHealth()/target.getMaxHealth())))));
		}
		dmg += addMultiplierToPlayerLogger(damager,target,"Challenge Base Damage Mult",calculateChallengeBaseDmgIncrease(shooter,target));
		if (shooter instanceof Player) {
			dmg += addToPlayerLogger(damager,target,"Tactics Bonus Damage",API.getPlayerBonuses((Player)shooter).getBonusDamage());
			dmg += addToPlayerLogger(damager,target,"Execute Set Bonus",(((ItemSet.TotalBaseAmountBasedOnSetBonusCount((Player)shooter, ItemSet.LORASAADI, 4, 4)*5.0)*(1-(target.getHealth()/target.getMaxHealth())))));
			if (!((Player)shooter).isOnGround()) {dmg += addToPlayerLogger(damager,target,"Prancer Set Bonus",ItemSet.GetTotalBaseAmount((Player)shooter, ItemSet.PRANCER));}
			if (PlayerMode.getPlayerMode((Player)shooter)==PlayerMode.BARBARIAN) {
				dmg += addMultiplierToPlayerLogger(damager,target,"Barbarian Execute Mult",dmg * (1-(target.getHealth()/target.getMaxHealth())));
			}
			dmg += addMultiplierToPlayerLogger(damager,target,"Tactics Bonus Mult",dmg * API.getPlayerBonuses((Player)shooter).getBonusOverallDamageMultiplier());
		}
		dmg += addMultiplierToPlayerLogger(damager,target,"Striker Mult",dmg * calculateStrikerMultiplier(shooter,target));
		if ((reason==null || !reason.equalsIgnoreCase("Test Damage"))) {
			double preemptivedmg = addMultiplierToPlayerLogger(damager,target,"Preemptive Strike Mult",dmg * calculatePreemptiveStrikeMultiplier(target,shooter));
			if (preemptivedmg!=0.0) {preemptive=true;}
			dmg += preemptivedmg;
			double backstabdmg = addMultiplierToPlayerLogger(damager,target,"Backstab Mult",dmg * calculateBackstabMultiplier(target,shooter));
			if (backstabdmg!=0.0 && (reason==null || !reason.equalsIgnoreCase("Test Damage"))) {preemptive=true;}
			dmg += backstabdmg;
		}
		dmg += addMultiplierToPlayerLogger(damager,target,"Isolation Damage Mult",dmg * calculateIsolationMultiplier(shooter,target));
		dmg += addMultiplierToPlayerLogger(damager,target,"STRENGTH Mult",dmg * calculateStrengthEffectMultiplier(shooter,target));
		dmg += addMultiplierToPlayerLogger(damager,target,"WEAKNESS Mult",dmg * calculateWeaknessEffectMultiplier(shooter,target));
		dmg += addMultiplierToPlayerLogger(damager,target,"POISON Mult",dmg * calculatePoisonEffectMultiplier(target));
		dmg += addMultiplierToPlayerLogger(damager,target,"Airborne Mult",dmg * calculateAirborneAttackMultiplier(shooter));
		dmg += addMultiplierToPlayerLogger(damager,target,"Dodge Chance Set Bonus Mult",dmg * calculateDodgeChanceSetBonusMultiplier(shooter));
		dmg += addMultiplierToPlayerLogger(damager,target,"Damage Reduction Set Bonus Mult",dmg * calculateDamageReductionSetBonusMultiplier(shooter));
		dmg += addMultiplierToPlayerLogger(damager,target,"Weapon Charge Bonus Mult",dmg * calculateWeaponChargeBonusMultiplier(shooter));
		dmg += addMultiplierToPlayerLogger(damager,target,"Damage Pool Bonus Mult",dmg * calculateDamagePoolBonusMultiplier(shooter));
		dmg += addMultiplierToPlayerLogger(damager,target,"Stealth Mult",dmg * calculateStealthMultiplier(shooter));
		dmg += addMultiplierToPlayerLogger(damager,target,"Dark Reverie Mult",dmg * calculateDarkReverieMultiplier(shooter));
		dmg += addMultiplierToPlayerLogger(damager,target,"Boss Mult",dmg * calculateBossDamageMultiplier(shooter));
		dmg += addMultiplierToPlayerLogger(damager,target,"Challenge Score Mult",dmg * calculateChallengeScoreMultiplier(shooter,target));
		if (reason==null || !reason.equalsIgnoreCase("Test Damage")) {
			double critdmg = addMultiplierToPlayerLogger(damager,target,"Critical Strike Mult",dmg * calculateCriticalStrikeMultiplier(weapon,shooter,target,reason,flags));
			if (critdmg!=0.0) {crit=true;
				aPlugin.API.critEntity(target, 15);}
			dmg += critdmg;
		}
		double armorpendmg = addToPlayerLogger(damager,target,"Armor Pen",calculateArmorPen(damager,dmg,target,weapon));
		if (!isFlagSet(flags, TRUEDMG) && (target instanceof Player && PlayerMode.getPlayerMode((Player)target)!=PlayerMode.BARBARIAN)) {
			dmg -= getDamageFromBarbarianSetBonus(target);
			dmg -= getDamageReduction(target);
		} else
		if (isFlagSet(flags, TRUEDMG) && (target instanceof Player && PlayerMode.getPlayerMode((Player)target)==PlayerMode.BARBARIAN)) {
			dmg -= getDamageFromBarbarianSetBonus(target);
			dmg -= getDamageReduction(target);
		}
		addToLoggerActual(damager,dmg);
		if (reason==null || !reason.equalsIgnoreCase("Test Damage")) {
			addToPlayerRawDamage(dmg,target);
		}
		//TwosideKeeper.log("Damage: "+dmg+", Armor Pen Damage: "+armorpendmg, 0);
		if (!isFlagSet(flags, TRUEDMG)) {
			if (target instanceof Player) {
				if (PlayerMode.getPlayerMode((Player)target)!=PlayerMode.BARBARIAN) {
					dmg = CalculateDamageReduction(dmg-armorpendmg,target,damager);
				}
			} else {
				dmg = CalculateDamageReduction(dmg-armorpendmg,target,damager);
			}
		}
		//TwosideKeeper.log("Damage: "+dmg+", Armor Pen Damage: "+armorpendmg, 0);
		
		setupDamagePropertiesForPlayer(damager,((crit)?IS_CRIT:0)|((headshot)?IS_HEADSHOT:0)|((preemptive)?IS_PREEMPTIVE:0),true);
		dmg = hardCapDamage(dmg+armorpendmg,target,reason);
		return dmg;
	}

	private static double calculateChallengeBaseDmgIncrease(LivingEntity shooter, LivingEntity target) {
		double dmg = 0.0;
		if (shooter!=null && shooter instanceof Player) {
			Player p = (Player)shooter;
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			if (pd.inTankChallengeRoom) {
				for (Room r : TwosideKeeper.roominstances) {
					dmg += r.getTankRoomBaseDamage();
				}
			}
		}
		return 0;
	}

	private static double calculateChallengeScoreMultiplier(LivingEntity shooter, LivingEntity target) {
		double mult = 0.0;
		if (shooter!=null && shooter instanceof Player) {
			if (ChallengeBlaze.isChallengeBlaze(target)) {
				mult += 0.33;
			} else
			if (ChallengeSpider.isChallengeSpider(target)) {
				mult += 0.25;
			} else
			if (ChallengeGhast.isChallengeGhast(target)) {
				mult += 0.5;
			}
			Player p = (Player)shooter;
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			if (pd.inTankChallengeRoom) {
				for (Room r : TwosideKeeper.roominstances) {
					mult += r.getTankRoomMultiplier();
				}
			}
		}
		return mult;
	}

	private static double calculateBossDamageMultiplier(LivingEntity shooter) {
		double mult = 0.0;
		if (shooter!=null && TwosideKeeper.custommonsters.containsKey(shooter.getUniqueId())) {
			CustomMonster cm = TwosideKeeper.custommonsters.get(shooter.getUniqueId());
			if (cm instanceof GenericBoss) {
				GenericBoss gb = (GenericBoss)cm;
				mult += (gb.getParticipants().size()>=4)?((gb.getParticipants().size()-3)*0.1):0.0;
			}
		}
		return mult;
	}

	private static double calculateDarkReverieMultiplier(LivingEntity shooter) {
		double mult = 0.0;
		if (shooter!=null) {
			if (Buff.hasBuff(shooter, "DARKSUBMISSION")) {
				Buff b = Buff.getBuff(shooter, "DARKSUBMISSION");
				if (b.getAmplifier()>=10) {
					mult -= 0.2;
				}
			}
		}
		return mult;
	}

	private static double calculateStealthMultiplier(LivingEntity shooter) {
		double mult = 0.0;
		if (shooter instanceof Player) {
			Player p = (Player)shooter;
			if (GenericFunctions.hasStealth(p) &&
					ItemSet.meetsSlayerSwordConditions(ItemSet.STEALTH, 18, 2, p)) {
				mult += 0.5;
			}
		}
		return mult;
	}

	private static double calculateDamagePoolBonusMultiplier(LivingEntity shooter) {
		double mult = 0.0;
		if (shooter instanceof Player) {
			Player p = (Player)shooter;
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			if (pd.damagepool>0) {
				mult+=(pd.damagepool/100d)*((ItemSet.TotalBaseAmountBasedOnSetBonusCount(p, ItemSet.LEGION, 4, 4)/100d));
			}
		}
		return mult;
	}

	private static double calculateWeaponChargeBonusMultiplier(LivingEntity shooter) {
		double mult = 0.0;
		if (shooter instanceof Player) {
			Player p = (Player)shooter;
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			if (pd.weaponcharges>0) {
				mult+=Math.min(pd.weaponcharges/10,20)*(ItemSet.TotalBaseAmountBasedOnSetBonusCount(p, ItemSet.LEGION, 3, 3)/100d);
			}
		}
		return mult;
	}

	private static double calculateDodgeChanceSetBonusMultiplier(LivingEntity shooter) {
		double mult = 0.0;
		if (shooter instanceof Player) {
			Player p = (Player)shooter;
			if (ItemSet.HasSetBonusBasedOnSetBonusCount(p, ItemSet.LUCI, 3)) {
				double dodgechance = CalculateDodgeChance((Player)shooter);
				mult = dodgechance*ItemSet.TotalBaseAmountBasedOnSetBonusCount(p, ItemSet.LUCI, 3, 3);
			}
		}
		return mult;
	}
	
	private static double calculateDamageReductionSetBonusMultiplier(LivingEntity shooter) {
		double mult = 0.0;
		if (shooter instanceof Player) {
			Player p = (Player)shooter;
			if (ItemSet.HasSetBonusBasedOnSetBonusCount(p, ItemSet.LUCI, 3)) {
				double damagered = 1-CalculateDamageReduction(1,(Player)shooter,null);
				mult = damagered*ItemSet.TotalBaseAmountBasedOnSetBonusCount(p, ItemSet.LUCI, 4, 4);
			}
		}
		return mult;
	}

	private static double getDamageReduction(LivingEntity target) {
		if (target instanceof Player) {
			double reduction = 0;
			Player p = (Player)target;
			reduction += API.getPlayerBonuses(p).getBonusFlatDamageReduction();
			return reduction;
		} else {
			return 0.0;
		}
	}

	private static double calculateAirborneAttackMultiplier(LivingEntity shooter) {
		if (shooter==null) {return 0.0;}
		if (shooter instanceof Player) {
			Player p = (Player)shooter;
			if (!p.isOnGround()) {
				if (p.isSprinting()) {
					return 0.2;
				} else {
					return -0.2;
				}
			}
		} else {
			if (!shooter.isOnGround()) {
				return -0.2;
			}
		}
		return 0.0;
	}

	private static double getDamageFromBarbarianSetBonus(LivingEntity target) {
		/*if (target instanceof Player) {
			Player p = (Player)target;
			return (ItemSet.GetTotalBaseAmount(p, ItemSet.DAWNTRACKER)+1)/3;
		}*/
		return 0.0;
	}

	private static void addToPlayerRawDamage(double damage, LivingEntity target) {
		if (target instanceof Player) {
			Player p = (Player)target;
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			pd.lastrawdamage=damage;
		}
	}
	
	public static double getBaseWeaponDamage(ItemStack weapon, Entity damager, LivingEntity target) {
		return getBaseWeaponDamage(0,weapon,damager,target,"");
	}

	/**
	 * Returns how much damage comes from the WEAPON, and no other sources.
	 * @param damager Optional.
	 * @param target REQUIRED.
	 * @param weapon
	 * @return
	 */
	public static double getBaseWeaponDamage(double damage, ItemStack weapon, Entity damager, LivingEntity target, String reason) {
		double dmg = 0.0;
		if (weapon!=null) { //Calculate damage using the weapon.
			if (damage == 0) {
				if (weapon.getType()==Material.BOW) {
					if ((damager instanceof Projectile)) {
						dmg += addToPlayerLogger(damager, target, "Weapon", grabNaturalWeaponDamage(weapon));
					} else {
						dmg += addToPlayerLogger(damager, target, "Weapon", 1.0);
					}
				} else {
					dmg += addToPlayerLogger(damager, target, "Weapon", grabNaturalWeaponDamage(weapon));
				}
			} else {
				if (reason!=null) {
					dmg += addToPlayerLogger(damager, target, reason, damage);
				} else {
					dmg += addToPlayerLogger(damager, target, ChatColor.GRAY+"Unknown", damage);
				}
			}
			dmg += calculateEnchantmentDamageIncrease(weapon,damager,target);
			LivingEntity shooter = getDamagerEntity(damager);
			if (shooter!=null && shooter instanceof Player) {
				dmg += addToPlayerLogger(damager,target,"Strike",GenericFunctions.getAbilityValue(ArtifactAbility.DAMAGE, weapon, (Player)shooter));
			}
			dmg += addToPlayerLogger(damager,target,"Highwinder",calculateHighwinderDamage(weapon,damager));
			dmg += addToPlayerLogger(damager,target,"Dancer Speed Bonus",calculateDancerSpeedDamage(weapon,damager));
			dmg += addToPlayerLogger(damager,target,"Set Bonus",calculateSetBonusDamage(damager));
			dmg += addMultiplierToPlayerLogger(damager,target,"Party Bonus Mult",dmg * calculatePartyBonusMultiplier(damager));
			dmg += addMultiplierToPlayerLogger(damager,target,"Set Bonus Mult",dmg * calculateSetBonusMultiplier(weapon,damager));
			dmg += addMultiplierToPlayerLogger(damager,target,"Belligerent Mult",dmg * calculateBeliggerentMultiplier(weapon,damager));
		}
		return dmg;
	}

	private static double calculateDancerSpeedDamage(ItemStack weapon, Entity damager) {
		double dmg = 0.0;
		if (damager instanceof Player) {
			Player p = (Player)damager;
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			if (ItemSet.GetTotalBaseAmount(p, ItemSet.DANCER)>0) {
				dmg += Math.min(93.182445*pd.velocity*ItemSet.GetTotalBaseAmount(p, ItemSet.DANCER),10);
				pd.lasthighwinderhit=TwosideKeeper.getServerTickTime();
				GenericFunctions.sendActionBarMessage(p, TwosideKeeper.drawVelocityBar(pd.velocity,ItemSet.GetTotalBaseAmount(p, ItemSet.DANCER)/10d),true);
			}
		}
		return dmg;
	}

	private static double calculatePartyBonusMultiplier(Entity damager) {
		if (getDamagerEntity(damager) instanceof Player) {
			Player p = (Player)getDamagerEntity(damager);
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			if (pd.partybonus>6) {
				return 6*0.1d;
			} else {
				return pd.partybonus*0.1;
			}
		}
		return 0.0;
	}

	public static void DealDamageToEntity(double damage, Entity damager, LivingEntity target, ItemStack weapon,
			String reason) {
		DealDamageToEntity(damage,damager,target,weapon,reason,0);
	}
	
	/**
	 * Does the actual damage application to the entity. Technically you can use this method to deal true damage
	 * as opposed to ApplyDamage(), but it ignores dodge chance and this method may not remain consistent with the
	 * damage dealing properties applied by other damage calculations. It is recommended to simply use ApplyDamage()
	 * with the TRUEDMG flag set instead.
	 * @param damage
	 * @param damager
	 * @param target 
	 * @param weapon
	 * @param reason
	 * @param flags
	 */
	public static void DealDamageToEntity(double damage, Entity damager, LivingEntity target, ItemStack weapon,
			String reason, int flags) {
		target.setLastDamage(0);
		target.setNoDamageTicks(0);
		target.setMaximumNoDamageTicks(0);
		damage = subtractAbsorptionHearts(damage,target);
		damage = applyOnHitEffects(damage,damager,target,weapon,reason,flags);
		if (getDamagerEntity(damager) instanceof Player) { //Player dealing damage to living entities does a custom damage modifier.
			TwosideKeeper.log("Dealing "+damage+" damage.", 5);
			TwosideKeeper.log("Sending out "+(damage+TwosideKeeper.CUSTOM_DAMAGE_IDENTIFIER)+" damage.",5);
			target.damage(damage+TwosideKeeper.CUSTOM_DAMAGE_IDENTIFIER,getDamagerEntity(damager));
			PlayerStructure pd = PlayerStructure.GetPlayerStructure((Player)getDamagerEntity(damager));
			EntityUtils.applyDamageIndicator(target, damage, (isFlagSet(pd.lasthitproperties,IS_CRIT))?IndicatorType.CRIT:IndicatorType.REGULAR);
		} else 
		if (!(getDamagerEntity(damager) instanceof LivingEntity) || (damage!=0 && isFlagSet(flags,SPECIALATTACK))) {
			//TwosideKeeper.log("Sending out "+damage+" damage.",2);
			subtractHealth(damage,target);
			aPlugin.API.sendEntityHurtAnimation(target);
		}
		
		if (damager==null && (isDot(reason)) && !(target instanceof Player)) {
			EntityUtils.applyDamageIndicator(target, damage, IndicatorType.DOT);
		}
		
		if (target instanceof Player) { //Update player health whenever hit.
			Player p = (Player)target;
			TwosideKeeper.setPlayerMaxHealth(p);
			//p.getScoreboard().getTeam(p.getName().toLowerCase()).setSuffix(TwosideKeeper.createHealthbar(((p.getHealth())/p.getMaxHealth())*100,p));
			//p.getScoreboard().getTeam(p.getName().toLowerCase()).setPrefix(GenericFunctions.PlayerModePrefix(p));
			runServerHeartbeat.UpdatePlayerScoreboardAndHealth(p);
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			pd.lasthitdesc = reason;
		}
		checkforDeath(damager, target, reason);
	}

	private static void checkforDeath(Entity damager, LivingEntity target, String reason) {
		if (target instanceof Player && target.getHealth()<=0) {
			//It's dead.
			LivingEntity shooter = getDamagerEntity(damager);
			if (shooter!=null) {
				if (TwosideKeeper.custommonsters.containsKey(shooter.getUniqueId())) {
					CustomMonster cm = TwosideKeeper.custommonsters.get(shooter.getUniqueId());
					cm.onPlayerSlayEvent((Player)target, reason);
				}
			}
		}
	}

	private static boolean isDot(String reason) {
		return reason.equalsIgnoreCase("POISON") || reason.equalsIgnoreCase("Shrapnel") ||
				reason.equalsIgnoreCase("BLEEDING") || reason.equalsIgnoreCase("CRIPPLE") ||
				reason.equalsIgnoreCase("BURN");
	}

	/**
	 * Applies all on-hit effects when something gets hit.
	 * @param damage
	 * @param damager
	 * @param target
	 * @param weapon
	 * @param reason
	 * @param flags
	 */
	static double applyOnHitEffects(double damage, Entity damager, LivingEntity target, ItemStack weapon,
			String reason, int flags) {
		triggerDummyHitEvent(target,damage);
		if (target instanceof Player) {
			Player p = (Player)target;
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			if (PlayerMode.isDefender(p)) {
				//GenericFunctions.addStackingPotionEffect(p, PotionEffectType.DAMAGE_RESISTANCE, 20*5, 4);
				if (p.isBlocking() && ItemSet.hasFullSet(p, ItemSet.SONGSTEEL)) {
					ApplyVendettaStackTimer(pd);
					pd.vendetta_amt+=((1-CalculateDamageReduction(1,target,damager))*pd.lastrawdamage)*0.40;
					if (TwosideKeeper.getMaxThornsLevelOnEquipment(target)>0) {
						pd.thorns_amt+=((1-CalculateDamageReduction(1,target,damager))*pd.lastrawdamage)*0.01;
					}
					DecimalFormat df = new DecimalFormat("0.00");
					GenericFunctions.sendActionBarMessage(p, "", true);
					//pd.customtitle.updateTitle(p);
					pd.customtitle.updateCombatBar(p, getDamagerEntity(damager));
					//GenericFunctions.sendActionBarMessage(p, ChatColor.YELLOW+"              Vendetta: "+ChatColor.GREEN+Math.round(pd.vendetta_amt)+((pd.thorns_amt>0)?"/"+ChatColor.GOLD+df.format(pd.thorns_amt):"")+ChatColor.GREEN+" dmg stored",true);
				}
			}
			if (getDamagerEntity(damager) instanceof Enderman) {
	    		if (MonsterController.getMonsterDifficulty(((Monster)getDamagerEntity(damager)))==MonsterDifficulty.HELLFIRE) {
					for (int i=0;i<4;i++) {
		    			if (Math.random()<=0.2) {
							LivingEntity mm = MonsterController.spawnAdjustedMonster(MonsterType.ENDERMITE, getDamagerEntity(damager).getLocation().add(0,1,0));
							mm.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,Integer.MAX_VALUE,2));
						}
					}
	    		}
			}
			if (getDamagerEntity(damager) instanceof CaveSpider) {
				applyCaveSpiderPoison(damager, p);
			}
			if (getDamagerEntity(damager) instanceof Skeleton) {
				applyWitherSkeletonWither(damager, p);
			}
			if (getDamagerEntity(damager) instanceof LivingEntity) {
				LivingEntity m = getDamagerEntity(damager);
				if (!(getDamagerEntity(damager) instanceof Player)) {
					LivingEntityStructure md = LivingEntityStructure.GetLivingEntityStructure(m);
					md.SetTarget(target);
				}
			}
			increaseStrikerSpeed(p);
			healDefenderSaturation(p);
			damage=increaseDamageDealtByFireTicks(p,damage,reason);
			dealRetaliationDamage(p,damager);
			aggroOntoMyPartyMembersInstead(p,damager);
			giveAbsorptionHealth(p);
			reduceKnockback(p);
			reduceSwiftAegisBuff(p);
			restoreHealthToPartyMembersWithProtectorSet(p);
			applySustenanceSetonHitEffects(p);
			reduceStrengthAmountForStealthSet(p);
			handleBlockStacks(p);
			if (!isFlagSet(flags,NOAOE)) {
				if (damage<p.getHealth()) {increaseArtifactArmorXP(p,(int)damage);}
			}
			aPlugin.API.showDamage(target, GetHeartAmount(damage));
			Bukkit.getScheduler().scheduleSyncDelayedTask(TwosideKeeper.plugin,new Runnable() {
				@Override
				public void run() {
					GenericFunctions.RemoveNewDebuffs(p);
				}
			},1);
			increaseBarbarianCharges(p);
			pd.slayermegahit=false;
			pd.lastcombat=TwosideKeeper.getServerTickTime();
			pd.lasthitdesc=reason;
			for (ItemStack item : GenericFunctions.getArmor(p)) {
				removePermEnchantments(p,item);
			}
			
			damage += IncreaseDamageFromDarkSubmission(p, damager, damage);
			
			damage = IncreaseDamageDealtByElites(p, damager, damage);
			
			damage = increaseTrueDamageAmount(p, damager, damage);
			
			damage = calculateDefenderAbsorption(p, damager, damage, reason);
			
			damage = sendDamageToDamagePool(p, damage, reason);
			
			damage = modifyFateBasedOnHolidayTreats(p, damage);
			
			damage = preventPoisonDamageFromKilling(p, damage, reason);
			
			if (PlayerMode.getPlayerMode(p)==PlayerMode.SLAYER) {
				TwosideKeeper.log("Is a Slayer.", 2);
				//PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
				if (damage>2) {
					damage=2;
				}
				if (pd.slayermodehp-damage>0) {
					GenericFunctions.SubtractSlayerModeHealth(p, damage);
					//p.setHealth(pd.slayermodehp);
					//damage=0;
					if (GenericFunctions.hasStealth(p)) {
						if (!ItemSet.meetsSlayerSwordConditions(ItemSet.STEALTH, 9, 1, p)) {
							GenericFunctions.removeStealth(p);
						}
					}
				} else {
					pd.slayermodehp=0;
					GenericFunctions.AttemptRevive(p, damager, damage, reason);
				}
				damage=0;
			} else
			if (damage>0 && GenericFunctions.AttemptRevive(p, damager, damage, reason)) {
				damage=0;
			}
			
			/*(if (p.getHealth()-damage<0) {
				p.damage(p.getHealth());
				final double DMG = damage;
				Bukkit.getScheduler().runTaskLater(TwosideKeeper.plugin, ()->{
					if (p!=null && !p.isDead()) {
						String damagesource = (damager!=null?GenericFunctions.GetEntityDisplayName(damager):"No Source");
						String reasoning = (reason!=null?reason:"Null");
						TwosideKeeper.log("WARNING!! Player "+p.getName()+" was supposed to die! ["+TwosideKeeper.getServerTickTime()+"] Damage Taken: "+DMG+" from "+damagesource+",Reason: "+reasoning, 1);
					}
				}, 1);
			}*/
			
			//pd.customtitle.updateTitle(p);
			pd.customtitle.updateCombatBar(p, getDamagerEntity(damager));
			pd.lastattack=TwosideKeeper.getServerTickTime();
		}
		LivingEntity shooter = getDamagerEntity(damager);
		if ((shooter instanceof Player) && target!=null) {
			Player p = (Player)shooter;
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			//TwosideKeeper.log("Exploding Arrow 2. Damager is "+GenericFunctions.GetEntityDisplayName(damager), 0);
			if (damager instanceof Arrow || damager instanceof TippedArrow) {
				Arrow a = (Arrow)damager;
				//TwosideKeeper.log("Exploding Arrow 1", 0);
				if (a.hasMetadata("EXPLODE_ARR")) {
					//Create an explosion.
					TwosideKeeper.log("In here", 5);
					//TwosideKeeper.log("Exploding Arrow", 0);
					Location hitloc = aPlugin.API.getArrowHitLocation(target, a);
					GenericFunctions.DealExplosionDamageToEntities(hitloc, getBaseWeaponDamage(weapon,damager,target)+60, 6, damager);
					SoundUtils.playGlobalSound(hitloc, Sound.ENTITY_ENDERDRAGON_FIREBALL_EXPLODE, 0.5f, 1.0f);
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
					//target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,20*20,0));
					Buff.addBuff(target,"Poison",new Buff("Poison",20*20,1,Color.YELLOW,ChatColor.YELLOW+"☣",false));
				}
			}
			provokeMonster(target,p,weapon);
			if (GenericFunctions.isArtifactEquip(weapon)) {		
				double ratio = 1.0-CalculateDamageReduction(1,target,p);
				if (p.getEquipment().getItemInMainHand().getType()!=Material.BOW) {
					//Do this with a 1 tick delay, that way it can account for items that are dropped one tick earlier and still work.
					if (!isFlagSet(flags,NOAOE) && !Dummy.isDummy(target)) {
						Bukkit.getScheduler().scheduleSyncDelayedTask(TwosideKeeper.plugin, new Runnable() {
							@Override
							public void run() {
								AwakenedArtifact.addPotentialEXP(p.getEquipment().getItemInMainHand(), (int)((ratio*20)+5)*((isFlagSet(flags,IS_HEADSHOT))?2:1), p);
							}
						},1);
					}
				} else {
					pd.storedbowxp+=(int)((ratio*20)+5)*((isFlagSet(flags,IS_HEADSHOT))?2:1);
					pd.lasthittarget=TwosideKeeper.getServerTickTime();
				}
				List<LivingEntity> hitlist = new ArrayList<LivingEntity>();
				if (!isFlagSet(flags,NOAOE)) {
					if (!Dummy.isDummy(target)) {
						increaseArtifactArmorXP(p,(int)(ratio*10)+1);
					}
					hitlist = getAOEList(weapon,target,p);
					//TwosideKeeper.log("AOE List: "+hitlist, 0);
				}
				
				boolean applyDeathMark=false;
				
				if (ArtifactAbility.getEnchantmentLevel(ArtifactAbility.DEATHMARK, p.getEquipment().getItemInMainHand())>0 &&
						pd.last_deathmark+GenericFunctions.GetModifiedCooldown(TwosideKeeper.DEATHMARK_COOLDOWN,p)<TwosideKeeper.getServerTickTime()) {
					applyDeathMark=true;
					//TwosideKeeper.log("Also apply Death Mark.", 0);
				}
				
				for (LivingEntity ent : hitlist) {
					boolean allow=true;
					if (ent instanceof Player && PVP.isFriendly((Player)ent, p)) {
						allow=false;
					}
					if (allow) {
						if (applyDeathMark) {
							GenericFunctions.ApplyDeathMark(ent);
						}
						if (!ent.equals(target)) {
							//hitlist.get(i).damage(dmg);
							//GenericFunctions.DealDamageToMob(CalculateDamageReduction(dmg,target,damager), hitlist.get(i), shooter, weapon, "AoE Damage");
							//TwosideKeeper.log("Apply AOE Damage.", 0);
							ApplyDamage(0,damager,ent,weapon,"AoE Damage",setFlag(flags,NOAOE));
						};
					}
				}
				
				//final List<LivingEntity> finallist = hitlist;
				/*Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("TwosideKeeper"), new Runnable() {
					public void run() {
						for (LivingEntity le : finallist) {
							if (le!=null && !le.isDead() && !Buff.hasBuff(le, "DeathMark")) {
								LivingEntityStructure.UpdateMobName(le);
								//They don't have death marks anymore, so we just remove their name color.
							}
						}
					}}
				,100);*/ 
				
				increaseSwordComboCount(weapon, p);
			}
			performMegaKnockback(damager,target);
			if ((damager!=null && damager instanceof Arrow) || (weapon!=null && weapon.getType()!=Material.BOW)) { 
				//TwosideKeeper.log("Entered here.", 0);
				removePermEnchantments(p,weapon);
				applyShrapnel(damager,p,target,reason);
				applyDoTs(damager,p,target);
			}
			//GenericFunctions.knockOffGreed(p);
			castEruption(p,target,weapon);
			addRegenPoolFromLifesteal(p,damage,weapon,reason);
			triggerEliteHitEvent(p,target,damage);
			subtractWeaponDurability(p,weapon);
			aPlugin.API.showDamage(target, GetHeartAmount(damage));
			suppressTarget(p,weapon,target);
			causeSpectralGlowAndAggro(damager,p,target);
			removeExperienceFromAlustineSetBonus(p);
			applyLightningStriketoFoe(p,target);
			pd.slayermegahit=false;
			pd.lastcombat=TwosideKeeper.getServerTickTime();
			increaseBarbarianStacks(p,weapon,reason);
			damage = applyBarbarianBonuses(p,target,weapon,damage,reason);
			increaseWindCharges(p);
			applyWindSlashEffects(p,target,damage,reason);
			createFirePool(p,damager,target,damage,reason);
			addSweepupBonus(p,damage,reason);
			
			if (PlayerMode.getPlayerMode(p)==PlayerMode.SLAYER) {
				if (isFlagSet(pd.lasthitproperties,IS_CRIT)) {
					GenericFunctions.addSuppressionTime(target, 15);
				}
				if (isFlagSet(pd.lasthitproperties,IS_PREEMPTIVE)) {
					if (ItemSet.HasSetBonusBasedOnSetBonusCount(p, ItemSet.WOLFSBANE, 7)) {
						if (pd.slayermodehp+2<p.getMaxHealth()) {
							pd.slayermodehp+=2;
							p.setHealth(pd.slayermodehp);
						} else {
							pd.slayermodehp=p.getMaxHealth();
							p.setHealth(pd.slayermodehp);
						}
					}
					if (ItemSet.HasSetBonusBasedOnSetBonusCount(p, ItemSet.ALUSTINE, 5)) {
						GenericFunctions.spawnXP(target.getLocation(), (int)ItemSet.TotalBaseAmountBasedOnSetBonusCount(p, ItemSet.ALUSTINE, 5, 4));
					}
				}
			}
			if (ItemSet.HasSetBonusBasedOnSetBonusCount(p, ItemSet.MOONSHADOW, 2)) {
				int poisonlv = (int)ItemSet.TotalBaseAmountBasedOnSetBonusCount(p, ItemSet.MOONSHADOW, 2, 2);
				/*if (target.hasPotionEffect(PotionEffectType.BLINDNESS) && GenericFunctions.getPotionEffectLevel(PotionEffectType.BLINDNESS, target)<=poisonlv) { 
					GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.BLINDNESS, 20*15, (int)poisonlv, target);
				} else {
					GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.BLINDNESS, 20*15, (int)poisonlv, target, true);
				}*/
				Buff.addBuff(target, "Poison", new Buff("Poison",20*15,(int)poisonlv,Color.YELLOW,ChatColor.YELLOW+"☣",false));
			}
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(TwosideKeeper.plugin,new Runnable() {
				@Override
				public void run() {
					GenericFunctions.RemoveNewDebuffs(p);
				}
			},1);
			
			AwardDamageAchievement(p,damage);
			
			appendDebuffsToName(target);

			/*if ((reason!=null && !reason.equalsIgnoreCase("thorns")) || pd.customtitle.getTitles()[1].length()==0) {
				pd.customtitle.updateCombatBar(p, target, damage, pd.lasthitproperties);
			}*/
			pd.lastattack=TwosideKeeper.getServerTickTime();
		}
		if (target instanceof Monster) {
			if (reason!=null && reason.equalsIgnoreCase("SUFFOCATION")) {
				triggerEliteBreakEvent(target);
			}
		}
		if (target!=null && damager instanceof Arrow) {
			Arrow proj = (Arrow)damager;
			if (proj.hasMetadata("TIPPED_ARROW")) {
				String effects = proj.getMetadata("TIPPED_ARROW").get(0).asString();
				for (String vals : effects.split(";")) {
					String[] pieces = vals.split(","); 
					GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.getByName(pieces[0]),Integer.parseInt(pieces[1]),Integer.parseInt(pieces[2])/8, target);
				}
				
			}
			if (proj.hasMetadata("BASE_ARROW")) {
				String[] pieces = proj.getMetadata("BASE_ARROW").get(0).asString().split(",");
				PotionEffectType type = GenericFunctions.convertPotionTypeToPotionEffectType(PotionType.valueOf(pieces[0]));
				PotionData pd = new PotionData(PotionType.valueOf(pieces[0]),Boolean.parseBoolean(pieces[1]),Boolean.parseBoolean(pieces[2]));
				if (pd.getType()!=PotionType.WATER) {
					GenericFunctions.logAndApplyPotionEffectToEntity(type,GenericFunctions.getBasePotionDuration(pd)/8, (pd.isUpgraded())?1:0, target);
				}
			}
			if (proj.hasMetadata("SNIPER_NORMAL") ||
					proj.hasMetadata("SNIPER_POISON") ||
					proj.hasMetadata("SNIPER_BLEED") ||
					proj.hasMetadata("SNIPER_CRIPPLINGINFECTION")) {
				if (proj.hasMetadata("SNIPER_POISON")) {
					//Apply a stacking Poison.
					Buff.addBuff(target, "Poison", new Buff("Poison",20*15,1,Color.YELLOW,ChatColor.YELLOW+"☣",false),true);
					createPoisonPool(proj, target);
				}
				if (proj.hasMetadata("SNIPER_BLEED")) {
					Buff.addBuff(target, "BLEEDING", new Buff("Bleed",20*15,1,Color.MAROON,ChatColor.DARK_RED+"☠",false),true);
					createBloodPool(proj,target);
					if (TwosideKeeper.custommonsters.containsKey(shooter.getUniqueId())) {
						CustomMonster cm = TwosideKeeper.custommonsters.get(shooter.getUniqueId());
						cm.bloodPoolSpawnedEvent(target);
					}
				}
				if (proj.hasMetadata("SNIPER_CRIPPLINGINFECTION")) {
					Buff.addBuff(target, 20*30, 1, BuffTemplate.INFECTION);
				}
				GenericFunctions.removeNoDamageTick(target, damager);
			}
		}
		updateAggroValues(getDamagerEntity(damager),target,damage,reason);
		return damage;
	}

	private static double handleBlockStacks(Player p, double damage) {
		if (PlayerMode.getPlayerMode(p)==PlayerMode.DEFENDER) {
			DefenderStance ds = DefenderStance.getDefenderStance(p);
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			if (ds == DefenderStance.BLOCK) {
				SoundUtils.playLocalSound(p, Sound.ENTITY_PLAYER_ATTACK_KNOCKBACK, 1, 1);
				pd.blockStacks = Math.min(pd.blockStacks+1, 10);
				GenericFunctions.sendActionBarMessage(p, "", true);
				pd.customtitle.updateSideTitleStats(p);
			} else
			if (ds == DefenderStance.TANK) {
				if (pd.blockStacks>0) {
					pd.blockStacks--;
					GenericFunctions.sendActionBarMessage(p, "", true);
					pd.customtitle.updateSideTitleStats(p);
				}
			}
		}
		return damage;
	}

	private static void updateAggroValues(LivingEntity damager, LivingEntity target, double damage, String reason) {
		if (getDamagerEntity(damager)!=null && EntityUtils.isValidEntity(getDamagerEntity(damager))) {
			if (target!=null && EntityUtils.isValidEntity(target) && !(target instanceof Player)) {
				LivingEntity damaging_ent = getDamagerEntity(damager);
				LivingEntityStructure les = LivingEntityStructure.GetLivingEntityStructure(target);
				double mult = 1.0;
				if (damaging_ent instanceof Player) {
					if (PlayerMode.getPlayerMode((Player)damaging_ent)==PlayerMode.BARBARIAN) {
						mult += 4-((damaging_ent.getHealth()/damaging_ent.getMaxHealth())*4d);
					}
					if (PlayerMode.getPlayerMode((Player)damaging_ent)==PlayerMode.DEFENDER) {
						les.increaseAggro(getDamagerEntity(damager), 100);
					}
				}
				les.increaseAggro(getDamagerEntity(damager), (int)(damage*mult));
				//TwosideKeeper.log(les.displayAggroTable(),0);
				if (les.GetTarget()!=null &&
						les.GetTarget() == getDamagerEntity(damager)) {
					les.lastHit = TwosideKeeper.getServerTickTime();
				}
			}
		}
	}

	private static void createBloodPool(Arrow proj, LivingEntity target) {
		TemporaryBlock.createTemporaryBlockCircle(target.getLocation(), 1, Material.WOOL, (byte)14, 20*30, "BLOODPOOL");
		new EffectPool(target.getLocation(),1,20*30,Color.fromRGB(255, 0, 0));
	}

	private static void createPoisonPool(Arrow proj, LivingEntity target) {
		TemporaryBlock.createTemporaryBlockCircle(target.getLocation(), 1, Material.WOOL, (byte)4, 20*30, "POISONPOOL");
		new EffectPool(target.getLocation(),1,20*30,Color.fromRGB(255, 255, 0));
	}

	private static double IncreaseDamageFromDarkSubmission(Player p, Entity damager, double damage) {
		LivingEntity shooter = getDamagerEntity(damager);
		double bonusdmg = 0;
		if (shooter!=null && TwosideKeeper.custommonsters.containsKey(shooter.getUniqueId())) {
			CustomMonster cm = TwosideKeeper.custommonsters.get(shooter.getUniqueId());
			//TwosideKeeper.log("Custom Monster here. Damage: "+damage, 0);
			if (cm instanceof Knight) {
				//TwosideKeeper.log("In here.", 0);
				Knight k = (Knight)cm;
				if (k.getParticipants().contains(p)) {
					bonusdmg += damage*k.getDarkSubmissionMultiplier(p);
					//TwosideKeeper.log(">Bonus True Damage set to "+bonusdmg, 0);
				}
			}
		}
		return bonusdmg;
	}

	private static double increaseTrueDamageAmount(Player p, Entity damager, double dmg) {
		LivingEntity shooter = getDamagerEntity(damager);
		if (shooter!=null) {
			if (TwosideKeeper.custommonsters.containsKey(shooter.getUniqueId())) {
				CustomMonster cm = TwosideKeeper.custommonsters.get(shooter.getUniqueId());
				dmg+=cm.getBasicAttackDamage().getTrueDmgComponent();
				dmg+=cm.getBasicAttackDamage().getTruePctDmgComponent()*p.getMaxHealth();
			}
		}
		return dmg;
	}

	private static void reduceStrengthAmountForStealthSet(Player p) {
		if (ItemSet.meetsSlayerSwordConditions(ItemSet.STEALTH, 27, 3, p)) {
			GenericFunctions.addStackingPotionEffect(p, PotionEffectType.INCREASE_DAMAGE, 20*60, 19,-1);
		}
	}

	private static void addSweepupBonus(Player p, double damage, String reason) {
		if (reason!=null && reason.equalsIgnoreCase("sweep up")) {
			if (ItemSet.HasSetBonusBasedOnSetBonusCount(p, ItemSet.PRIDE, 5)) {
				//TwosideKeeper.log("In here. Damage: "+damage, 0);
				GenericFunctions.HealEntity(p, damage/2);
			}
		}
	}

	private static void applySustenanceSetonHitEffects(Player p) {
		if (ItemSet.HasSetBonusBasedOnSetBonusCount(p, ItemSet.SUSTENANCE, 2)) {
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			pd.regenpool+=ItemSet.TotalBaseAmountBasedOnSetBonusCount(p, ItemSet.SUSTENANCE, 2, 2);
			hardCapRegenPool(p, pd);
			GenericFunctions.sendActionBarMessage(p, "", true);
		}
		if (ItemSet.HasSetBonusBasedOnSetBonusCount(p, ItemSet.SUSTENANCE, 3)) {
			if (Buff.hasBuff(p, "REGENERATION")) {
				Buff b = Buff.getBuff(p, "REGENERATION");
				b.setStacks(b.getAmplifier()+1);
				b.setDuration(b.getAmplifier()*20+1);
				//Buff.addBuff(p, "REGENERATION", new Buff("Regeneration",,(int)ItemSet.TotalBaseAmountBasedOnSetBonusCount(p, ItemSet.SUSTENANCE, 3, 3),Color.GREEN,ChatColor.GREEN+""+ChatColor.BOLD+"✙",true), true);
			} else {
				Buff.addBuff(p, "REGENERATION", new Buff("Regeneration",20,(int)ItemSet.TotalBaseAmountBasedOnSetBonusCount(p, ItemSet.SUSTENANCE, 3, 3),Color.GREEN,ChatColor.GREEN+""+ChatColor.BOLD+"✙",true), true);
			}
			Buff b = Buff.getBuff(p, "REGENERATION");
			int tier = ItemSet.getHighestTierInSet(p, ItemSet.SUSTENANCE);
			if (b.getAmplifier()>Math.min(2*tier, 10)) {
				b.setStacks(Math.min(2*tier, 10));
			}
		}
		if (ItemSet.hasFullSet(p, ItemSet.SUSTENANCE)) {
			int regenamt = ItemSet.getHighestTierInSet(p, ItemSet.SUSTENANCE);
			for (Player pl : PartyManager.getPartyMembers(p)) {
				if (!pl.equals(p)) {
					PlayerStructure pd = PlayerStructure.GetPlayerStructure(pl);
					pd.regenpool+=regenamt;
					hardCapRegenPool(pl,pd);
					GenericFunctions.sendActionBarMessage(pl, "", true);
				}
			}
		}
	}

	private static void restoreHealthToPartyMembersWithProtectorSet(Player p) {
		if (ItemSet.HasSetBonusBasedOnSetBonusCount(p, ItemSet.PROTECTOR, 4)) {
			double healamt = ItemSet.TotalBaseAmountBasedOnSetBonusCount(p, ItemSet.PROTECTOR, 4, 4);
			for (Player pl : PartyManager.getPartyMembers(p)) {
				if (!pl.equals(p)) {
					GenericFunctions.HealEntity(pl, healamt);
				}
			}
		}
	}

	private static void createFirePool(Player p, Entity damager, LivingEntity target, double damage, String reason) {
		if (damager instanceof Projectile) {
			if (ItemSet.hasFullSet(p, ItemSet.TOXIN)) {
				new TemporaryBlockNode(target.getLocation(),3,100,"FIREPOOL",Particle.DRIP_LAVA,60);
			}
		}
	}

	private static void applyDoTs(Entity damager, Player p, LivingEntity target) {
		if (damager instanceof Projectile) {
			if (ItemSet.HasSetBonusBasedOnSetBonusCount(p, ItemSet.TOXIN, 2)) {
				double basechance = ItemSet.TotalBaseAmountBasedOnSetBonusCount(p, ItemSet.TOXIN, 2, 2)/100d;
				int tier = ItemSet.getHighestTierInSet(p, ItemSet.TOXIN);
				if (Math.random()<=(basechance + calculateDebuffChance(p))) {
					Buff.addBuff(target, "BLEEDING", new Buff("Bleeding",20*15,tier,Color.MAROON,ChatColor.DARK_RED+"☠",false));
				}
			}
			if (ItemSet.HasSetBonusBasedOnSetBonusCount(p, ItemSet.TOXIN, 3)) {
				double basechance = ItemSet.TotalBaseAmountBasedOnSetBonusCount(p, ItemSet.TOXIN, 3, 3)/100d;
				int tier = ItemSet.getHighestTierInSet(p, ItemSet.TOXIN);
				if (Math.random()<=(basechance + calculateDebuffChance(p))) {
					Buff.addBuff(target, "INFECTION", new Buff("Infection",20*10,tier,Color.GRAY,ChatColor.GRAY+"❧",false));
				}
			}
			if (ItemSet.HasSetBonusBasedOnSetBonusCount(p, ItemSet.TOXIN, 4)) {
				double basechance = ItemSet.TotalBaseAmountBasedOnSetBonusCount(p, ItemSet.TOXIN, 4, 4)/100d;
				int tier = ItemSet.getHighestTierInSet(p, ItemSet.TOXIN);
				if (Math.random()<=(basechance + calculateDebuffChance(p))) {
					Buff.addBuff(target, "CRIPPLE", new Buff("Cripple",20*10,tier,Color.WHITE,ChatColor.WHITE+"☹",false));
				}
			}
		}
	}

	private static void triggerDummyHitEvent(LivingEntity target, double damage) {
		if (target instanceof Villager) {
			Villager v = (Villager)target;
			/*for (UUID id : TwosideKeeper.custommonsters.keySet()) {
				if (id.equals(v.getUniqueId())) {
					sig.plugin.TwosideKeeper.Monster.Wither wi = (sig.plugin.TwosideKeeper.Monster.Wither)TwosideKeeper.custommonsters.get(id);
					wi.runHitEvent(p, dmg);
				}
			}*/
			if (TwosideKeeper.custommonsters.containsKey(v.getUniqueId())) {
				Dummy dm = (Dummy)TwosideKeeper.custommonsters.get(v.getUniqueId());
				dm.customHitHandler(damage);
			}
		}
	}

	private static void applyShrapnel(Entity damager, Player p, LivingEntity target, String reason) {
		if (damager instanceof Projectile && (reason==null || !reason.equalsIgnoreCase("Shrapnel Explosion"))) {
			if (ItemSet.hasFullSet(p, ItemSet.SHARD)) {
				int shrapnellv = ItemSet.getHighestTierInSet(p, ItemSet.SHARD);
				Buff.addBuff(target, "SHRAPNEL", new Buff("Shrapnel",20*10,shrapnellv,Color.RED,ChatColor.RED+"❂",false), true);
				GenericFunctions.DealExplosionDamageToEntities(target.getLocation(), 40f+target.getHealth()*0.1, 2, damager, "Shrapnel Explosion");
				aPlugin.API.sendSoundlessExplosion(target.getLocation(), 1);
				SoundUtils.playGlobalSound(target.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 0.6f, 0.5f);
			}
		}
	}

	private static void applyWindSlashEffects(Player p, LivingEntity target, double damage, String reason) {
		if (reason!=null && reason.equalsIgnoreCase("Wind Slash")) {
			GenericFunctions.knockupEntities(0.4d, target);
			if (damage>target.getHealth()) {
				//Target killed.
				int settier = ItemSet.GetItemTier(p.getEquipment().getItemInMainHand());
				Buff.addBuff(p, "WINDCHARGE", new Buff("Wind",20*60,settier*5,Color.GRAY,"๑",true), true);
				CustomDamage.setAbsorptionHearts(p, CustomDamage.getAbsorptionHearts(p)+settier);
			}
		}
	}
	
	private static void increaseWindCharges(Player p) {
		if (ItemSet.HasSetBonusBasedOnSetBonusCount(p, ItemSet.WINDRY, 2)) {
			int windchargeamt = (int)ItemSet.TotalBaseAmountBasedOnSetBonusCount(p, ItemSet.WINDRY, 2, 2);
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			Buff.addBuff(p, "WINDCHARGE", new Buff("Wind",20*60,windchargeamt,Color.GRAY,"๑",true),true);
			Buff b = Buff.getBuff(p, "WINDCHARGE");
			int maxWindStacks = ItemSet.getHighestTierInSet(p,ItemSet.WINDRY)*10;
			if (b.getAmplifier()>maxWindStacks) {
				b.setStacks(maxWindStacks);
			}
			GenericFunctions.sendActionBarMessage(p, "", true);
		}
	}

	private static double IncreaseDamageDealtByElites(Player p, Entity damager, double damage) {
		LivingEntity shooter = getDamagerEntity(damager);
		if (shooter!=null && !(shooter instanceof Player)) {
			LivingEntityStructure les = LivingEntityStructure.GetLivingEntityStructure(shooter);
			if (les.isElite) {
				for (EliteMonster bm : TwosideKeeper.elitemonsters) {
					if (bm.getMonster().getUniqueId().equals(shooter.getUniqueId())) {
						if (bm.getTargetList().size()>4) {
							damage += damage*((bm.getTargetList().size()-4)*0.05);
						}
					}
				}
			}
		}
		return damage;
	}

	private static double preventPoisonDamageFromKilling(Player p, double damage, String reason) {
		if (reason!=null && reason.equalsIgnoreCase("POISON") && p.getHealth()<=damage) {
			p.setHealth(1);
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			pd.slayermodehp=1;
			return 0;
		}
		return damage;
	}

	private static double modifyFateBasedOnHolidayTreats(Player p, double damage) {
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		boolean consumed=false;
		if (p.getHealth()-damage<=0 && pd.lastrevivecandyconsumed+400<TwosideKeeper.getServerTickTime()) {
			for (int i=0;i<9;i++) {
				ItemStack item = p.getInventory().getItem(i);
				if (item!=null) {
					if (Christmas.isHolidayRageCandyBarItem(item)) {
						//TwosideKeeper.log(ChatColor.AQUA+"You prepare to eat a "+GenericFunctions.UserFriendlyMaterialName(item), 0);
						p.sendMessage(ChatColor.AQUA+"You munch on a "+GenericFunctions.UserFriendlyMaterialName(item));
						RemoveOneItem(p.getInventory(),item,i);
						Bukkit.broadcastMessage(ChatColor.GOLD+p.getName()+ChatColor.WHITE+" almost died... But came back to life!");
						aPlugin.API.discordSendRawItalicized(ChatColor.GOLD+p.getName()+ChatColor.WHITE+" almost died... But came back to life!");
						SoundUtils.playLocalSound(p, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 0.6f);
						p.setHealth(p.getMaxHealth());
						GenericFunctions.RevivePlayer(p,p.getMaxHealth());
						ItemStack[] hotbar = GenericFunctions.getBaubles(p);
						GenericFunctions.RandomlyBreakBaubles(p);
						SoundUtils.playLocalSound(p, Sound.ENTITY_GENERIC_EAT, 1.0f, 1.0f);
						pd.lastrevivecandyconsumed=TwosideKeeper.getServerTickTime();
						aPluginAPIWrapper.sendCooldownPacket(p, Material.GOLDEN_APPLE, 400);
						return 0;
					}
				}
			}
		} else
		if (p.getHealth()-damage<p.getMaxHealth()/2 && pd.lastcandyconsumed+40<TwosideKeeper.getServerTickTime()) {
			//See if we can activate any treats. Check the hotbar.
			for (int i=0;i<9;i++) {
				ItemStack item = p.getInventory().getItem(i);
				if (item!=null) {
					if (Christmas.isSmallCandyItem(item)) {
						//TwosideKeeper.log(ChatColor.AQUA+"You prepare to eat a "+GenericFunctions.UserFriendlyMaterialName(item), 0);
						p.sendMessage(ChatColor.AQUA+"You munch on a "+GenericFunctions.UserFriendlyMaterialName(item));
						RemoveOneItem(p.getInventory(),item,i);
						Bukkit.getScheduler().scheduleSyncDelayedTask(TwosideKeeper.plugin, ()->{
							if (!p.isDead()) {
								SoundUtils.playLocalSound(p, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 0.6f);
								p.setHealth(Math.min(p.getHealth()+(0.1*p.getMaxHealth()), p.getMaxHealth()));
								p.sendMessage(ChatColor.GREEN+"   "+Math.round(p.getMaxHealth()*0.1)+ChatColor.WHITE+" health has been restored!");
							}
						},10);
						consumed=true;break;
					} else
					if (Christmas.isLargeCandyItem(item)) {
						//TwosideKeeper.log(ChatColor.AQUA+"You prepare to eat a "+GenericFunctions.UserFriendlyMaterialName(item), 0);
						p.sendMessage(ChatColor.AQUA+"You munch on a "+GenericFunctions.UserFriendlyMaterialName(item));
						RemoveOneItem(p.getInventory(),item,i);
						Bukkit.getScheduler().scheduleSyncDelayedTask(TwosideKeeper.plugin, ()->{
							if (!p.isDead()) {
								SoundUtils.playLocalSound(p, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 0.6f);
								p.setHealth(Math.min(p.getHealth()+(0.5*p.getMaxHealth()), p.getMaxHealth()));
								p.sendMessage(ChatColor.GREEN+"   "+Math.round(p.getMaxHealth()*0.5)+ChatColor.WHITE+" health has been restored!");
							}
						},10);
						consumed=true;break;
					} else
					if (Christmas.isSourCandyItem(item)) {
						//TwosideKeeper.log(ChatColor.AQUA+"You prepare to eat a "+GenericFunctions.UserFriendlyMaterialName(item), 0);
						p.sendMessage(ChatColor.AQUA+"You munch on a "+GenericFunctions.UserFriendlyMaterialName(item));
						RemoveOneItem(p.getInventory(),item,i);
						Bukkit.getScheduler().scheduleSyncDelayedTask(TwosideKeeper.plugin, ()->{
							if (!p.isDead()) {
								SoundUtils.playLocalSound(p, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 0.6f);
								GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.REGENERATION, 400, 4, p, true);
								p.sendMessage(ChatColor.GREEN+"   You feel a rejuvenating feeling inside of you.");
							}
						},10);
						consumed=true;break;
					} else
					if (Christmas.isMysteryFlavorLollipopItem(item)) {
						//TwosideKeeper.log(ChatColor.AQUA+"You prepare to eat a "+GenericFunctions.UserFriendlyMaterialName(item), 0);
						p.sendMessage(ChatColor.AQUA+"You munch on a "+GenericFunctions.UserFriendlyMaterialName(item));
						RemoveOneItem(p.getInventory(),item,i);
						Bukkit.getScheduler().scheduleSyncDelayedTask(TwosideKeeper.plugin, ()->{
							if (!p.isDead()) {
								if (Bukkit.getOnlinePlayers().size()>1) {
									GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.CONFUSION, 60, 4, p, true);
									TeleportToARandomPlayer(p);
									SoundUtils.playLocalSound(p, Sound.BLOCK_PORTAL_TRAVEL, 1.0f, 0.6f);
									p.sendMessage(ChatColor.YELLOW+"   You suddenly become disoriented.");
								} else {
									SoundUtils.playLocalSound(p, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 0.6f);
									double randompct = Math.random();
									p.setHealth(Math.min(p.getHealth()+(randompct*p.getMaxHealth()), p.getMaxHealth()));
									p.sendMessage(ChatColor.GREEN+"   "+Math.round(p.getMaxHealth()*randompct)+ChatColor.WHITE+" health has been restored!");
								}
							}
						},10);
						consumed=true;break;
					}
				}
			}
		}
		if (consumed) {
			SoundUtils.playLocalSound(p, Sound.ENTITY_GENERIC_EAT, 1.0f, 1.0f);
			pd.lastcandyconsumed=TwosideKeeper.getServerTickTime();
			aPluginAPIWrapper.sendCooldownPacket(p, Material.GOLDEN_CARROT, 40);
			aPluginAPIWrapper.sendCooldownPacket(p, Material.RAW_FISH, 40);
		}
		return damage;
	}

	private static void TeleportToARandomPlayer(Player p) {
		int tries = 0;
		while (true && tries<50) {
			tries++;
			for (Player pl : Bukkit.getOnlinePlayers()) {
				if (Math.random()<=1d/(Bukkit.getOnlinePlayers().size())) {
					p.teleport(pl);
					return;
				}
			}
		}
	}

	public static void RemoveOneItem(PlayerInventory inventory, ItemStack item, int i) {
		if (item.getAmount()>1) {
			item.setAmount(item.getAmount()-1);
			inventory.setItem(i, item);
		} else {
			inventory.setItem(i, new ItemStack(Material.AIR));
		}
	}

	private static void giveAbsorptionHealth(Player p) {
		if (ItemSet.HasSetBonusBasedOnSetBonusCount(p, ItemSet.OLIVE, 4)) {
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			if (pd.lastabsorptionhealthgiven+600<TwosideKeeper.getServerTickTime()) {
				pd.lastabsorptionhealthgiven=TwosideKeeper.getServerTickTime();
				CustomDamage.setAbsorptionHearts(p, Math.min(CustomDamage.getAbsorptionHearts(p)+20,20));
			}
		}
	}

	private static void aggroOntoMyPartyMembersInstead(Player p, Entity damager) {
		if (damager instanceof Monster) {
			Monster m = (Monster)damager;
			List<Player> partymembers = PartyManager.getPartyMembers(p);
			for (Player pl : partymembers) {
				if (!pl.equals(p) && ItemSet.HasSetBonusBasedOnSetBonusCount(pl, ItemSet.DONNER, 4)) {
					//Aggro to them instead.
					LivingEntityStructure les = LivingEntityStructure.GetLivingEntityStructure(m);
					if (!m.hasPotionEffect(PotionEffectType.GLOWING)) {
						setMonsterTarget(m,pl);
						setAggroGlowTickTime(m,100);
					}
					les.increaseAggro(pl, 500*ItemSet.getHighestTierInSet(pl, ItemSet.DONNER));
					break;
				}
			}
		}
	}

	private static void applyLightningStriketoFoe(Player p, LivingEntity target) {
		if (ItemSet.HasSetBonusBasedOnSetBonusCount(p,ItemSet.BLITZEN,4)) {
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			if (pd.lastlightningstrike+100<TwosideKeeper.getServerTickTime()) {
				p.getWorld().strikeLightningEffect(target.getLocation());
				p.getWorld().strikeLightningEffect(target.getLocation());
				pd.lastlightningstrike=TwosideKeeper.getServerTickTime();
				GenericFunctions.DealBlitzenLightningStrikeToNearbyMobs(target.getLocation(), 12, 1, p, TRUEDMG|IGNORE_DAMAGE_TICK);
			}
		}
	}

	private static void dealRetaliationDamage(Player p, Entity damager) {
		LivingEntity shooter = getDamagerEntity(damager);
		if (shooter!=null) {
			if (p.isBlocking() &&
					ItemSet.GetTotalBaseAmount(p, ItemSet.OLIVE)>0) {
				CustomDamage.ApplyDamage(ItemSet.GetTotalBaseAmount(p, ItemSet.OLIVE), p, shooter, null, "Retaliation", TRUEDMG);
			}
		}
	}

	private static void ApplyVendettaStackTimer(PlayerStructure pd) {
		if (pd.vendetta_amt<=0) {pd.lastvendettastack=TwosideKeeper.getServerTickTime();}
	}

	private static void AwardDamageAchievement(Player p, double dmg) {
		if (p.hasAchievement(Achievement.ENCHANTMENTS) && dmg>18 && !p.hasAchievement(Achievement.OVERKILL)) {
			p.awardAchievement(Achievement.OVERKILL);
		}
	}

	@SuppressWarnings("deprecation")
	private static void applyWitherSkeletonWither(Entity damager, Player p) {
		Skeleton sk = (Skeleton)getDamagerEntity(damager);
		if (sk.getSkeletonType()==SkeletonType.WITHER) {
			int witherlv=1;
			MonsterDifficulty md = MonsterController.getMonsterDifficulty(sk);
			if (md.equals(MonsterDifficulty.DANGEROUS)) {
				witherlv=2;
			} else
			if (md.equals(MonsterDifficulty.DEADLY)) {
				witherlv=3;
			} else
			if (md.equals(MonsterDifficulty.HELLFIRE)) {
				witherlv=4;
			} else 
			if (md.equals(MonsterDifficulty.END)) {
				witherlv=5;
			} else 
			if (md.equals(MonsterDifficulty.ELITE)) {
				witherlv=9;
			} else 
			if (p.hasPotionEffect(PotionEffectType.WITHER)) {
				int currentWITHERlv = GenericFunctions.getPotionEffectLevel(PotionEffectType.WITHER, p);
				if (currentWITHERlv<witherlv) {
					GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.WITHER, 20*20, witherlv, p, true);
				} else { //Refresh it.
					if (GenericFunctions.getPotionEffectDuration(PotionEffectType.WITHER, p)<(20*20)) {
						GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.WITHER, 20*20, currentWITHERlv, p, true);
					}
				}
			} else {
				GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.WITHER, 20*20, witherlv, p, true);
			}
		}
	}

	@SuppressWarnings("deprecation")
	public static void applyCaveSpiderPoison(Entity damager, Player p) {
		int poisonlv=1;
		MonsterDifficulty md = MonsterController.getMonsterDifficulty((CaveSpider)getDamagerEntity(damager));
		if (md.equals(MonsterDifficulty.DANGEROUS)) {
			poisonlv=2;
		} else
		if (md.equals(MonsterDifficulty.DEADLY)) {
			poisonlv=3;
		} else
		if (md.equals(MonsterDifficulty.HELLFIRE)) {
			poisonlv=4;
		} else 
		if (md.equals(MonsterDifficulty.END)) {
			poisonlv=5;
		} else 
		if (md.equals(MonsterDifficulty.ELITE)) {
			poisonlv=9;
		} else 
		if (p.hasPotionEffect(PotionEffectType.POISON)) {
			int currentpoisonlv = GenericFunctions.getPotionEffectLevel(PotionEffectType.POISON, p);
			if (currentpoisonlv<poisonlv) {
				GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.POISON, 20*20, poisonlv, p, true);
			} else { //Refresh it.
				if (GenericFunctions.getPotionEffectDuration(PotionEffectType.POISON, p)<(20*20)) {
					GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.POISON, 20*20, currentpoisonlv, p, true);
				}
			}
		} else {
			GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.POISON, 20*20, poisonlv, p, true);
		}
	}

	private static double applyBarbarianBonuses(Player p, LivingEntity target, ItemStack weapon, double dmg, String reason) {
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		if (PlayerMode.getPlayerMode(p)==PlayerMode.BARBARIAN) {
			if (reason!=null) {
				if (reason.equalsIgnoreCase("power swing")) {
					double lifestealstack_incramt = 10;
					if (ItemSet.HasSetBonusBasedOnSetBonusCount(p, ItemSet.PRIDE, 3)) {
						lifestealstack_incramt *= 2;
						GenericFunctions.addStackingPotionEffect(p, PotionEffectType.REGENERATION, 20*15, 9, (int)ItemSet.TotalBaseAmountBasedOnSetBonusCount(p, ItemSet.PRIDE, 3, 3));
					}
					IncreaseLifestealStacks(p,(int)lifestealstack_incramt);
					pd.weaponcharges-=10;
					//GenericFunctions.sendActionBarMessage(p, "");
					pd.customtitle.updateSideTitleStats(p);
				}
			}
			if (p.isSneaking() && pd.weaponcharges>=30 && (reason==null || !reason.equalsIgnoreCase("forceful strike")) &&
					weapon!=null && weapon.equals(p.getEquipment().getItemInMainHand())) {
				SoundUtils.playGlobalSound(p.getLocation(), Sound.BLOCK_WOOD_BUTTON_CLICK_ON, 3.0f, 0.6f);
				//Apply 10 strikes across the field.
				dmg*=2;
				double suppressDurationMult=1.0;
				if (target instanceof Player && 
						PVP.isEnemy((Player)target, p)) {
					suppressDurationMult=0.5;
				}
				GenericFunctions.addSuppressionTime(target, (int)(20*3*suppressDurationMult));
				double xspd=p.getLocation().getDirection().getX();
				double zspd=p.getLocation().getDirection().getZ();
				Location attackloc = p.getLocation().clone();
				boolean appliesPoisonDebuff=false;
				if (ItemSet.HasSetBonusBasedOnSetBonusCount(p, ItemSet.PRIDE, 4)) {
					appliesPoisonDebuff=true;
				}
				for (int i=0;i<10;i++) {
					attackloc = attackloc.add(xspd,0,zspd);
					SoundUtils.playGlobalSound(p.getLocation(), Sound.ENTITY_ENDERDRAGON_FIREBALL_EXPLODE, 0.1f, 1.4f);
					aPlugin.API.sendSoundlessExplosion(attackloc, 0.6f);
					List<LivingEntity> ents = GenericFunctions.DealDamageToNearbyMobs(attackloc, dmg, 1, true, 0.6, p, weapon, false, "Forceful Strike");
						int poisonlv = (int)ItemSet.TotalBaseAmountBasedOnSetBonusCount(p, ItemSet.PRIDE, 4, 4);
						for (Entity e : ents) {
							if (e instanceof LivingEntity) {
								boolean allowed=true;
								suppressDurationMult=1.0;
								if (e instanceof Player) {
									if (PVP.isFriendly((Player)e, p)) {
										allowed=false;	
									} else {
										suppressDurationMult=0.5;
									}
								}
								if (allowed) {
									GenericFunctions.addSuppressionTime((LivingEntity)e, (int)(20*3*suppressDurationMult));
									if (appliesPoisonDebuff) {
								//GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.POISON, 20*15, poisonlv-1, (LivingEntity)e);
									Buff.addBuff((LivingEntity)e, "Poison", new Buff("Poison",20*15,poisonlv,Color.YELLOW,ChatColor.YELLOW+"☣",false));
								}
							}
						}
					}
					
				}
				pd.weaponcharges-=30;
				pd.customtitle.updateSideTitleStats(p);
			}
		}
		return dmg;
	}

	private static void increaseBarbarianCharges(Player p) {
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		if (PlayerMode.getPlayerMode(p)==PlayerMode.BARBARIAN) {
			IncreaseWeaponCharges(p,2);
		}
	}

	public static void IncreaseWeaponCharges(Player p, int amt) {
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		if (pd.rage_time<=TwosideKeeper.getServerTickTime()) {
			if (ItemSet.hasFullSet(p, ItemSet.DAWNTRACKER)) {
				amt*=2;
			}
			pd.weaponcharges=Math.min(pd.weaponcharges+amt,500);
			pd.customtitle.updateSideTitleStats(p);
		}
	}
	
	public static void IncreaseLifestealStacks(Player p, int amt) {
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		if (ItemSet.hasFullSet(p, ItemSet.DAWNTRACKER)) {
			amt*=2;
		}
		double pct = ItemSet.GetTotalBaseAmount(p, ItemSet.PRIDE)/100d;
		amt+=(int)(amt*pct);
		//TwosideKeeper.log("pct is "+pct+". Trying RNG for roll "+(pct%1.0), 0);
		if (Math.random()<=pct%1.0) {
			amt+=1;
		}
		pd.lifestealstacks=Math.min(100,pd.lifestealstacks+amt*((pd.rage_time>TwosideKeeper.getServerTickTime())?2:1));
	}

	private static double sendDamageToDamagePool(Player p, double damage, String reason) {
		if (reason==null || !reason.equalsIgnoreCase("damage pool")) {
			if (PlayerMode.getPlayerMode(p)==PlayerMode.BARBARIAN) {
				PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
				if (pd.damagepool==0) {
					pd.damagepooltime=TwosideKeeper.getServerTickTime();
				}
				if (damage>getTransferDamage(p)) {
					if ((damage-getTransferDamage(p))*getDamagePoolMult(p)>0) {
						pd.damagepool+=(damage-getTransferDamage(p))*getDamagePoolMult(p);
					}
					return getTransferDamage(p);
				} else {
					//pd.damagepool=0;
					return damage;
				}
			}
		}
		return damage;
	}

	public static double getDamagePoolMult(Player p) {
		double mult = 1.0;
		mult -= ItemSet.GetTotalBaseAmount(p, ItemSet.LEGION)/100d;
		return mult;
	}

	private static void increaseBarbarianStacks(Player p, ItemStack weapon, String reason) {
		if (PlayerMode.getPlayerMode(p)==PlayerMode.BARBARIAN && 
				(reason==null || !reason.equalsIgnoreCase("forceful strike"))) {
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			if (pd.rage_time>TwosideKeeper.getServerTickTime()) {
				IncreaseLifestealStacks(p,2);
			} else {
				IncreaseLifestealStacks(p,1);
			}
			pd.lastattacked=TwosideKeeper.getServerTickTime();
			if (p.getEquipment().getItemInMainHand().equals(weapon)) {
				double charge_amt = 1;
				charge_amt += ItemSet.TotalBaseAmountBasedOnSetBonusCount(p, ItemSet.PRIDE, 2, 2)/2;
				IncreaseWeaponCharges(p,(int)charge_amt);
			}
		}
	}

	private static void removeExperienceFromAlustineSetBonus(Player p) {
		if (ItemSet.HasSetBonusBasedOnSetBonusCount(p, ItemSet.ALUSTINE, 7)) {
			aPlugin.API.setTotalExperience(p, aPlugin.API.getTotalExperience(p)-p.getLevel());
		}
	}

	private static void causeSpectralGlowAndAggro(Entity damager, Player p, LivingEntity target) {
		if (damager instanceof SpectralArrow) {
			if (target instanceof Monster) {
				provokeMonster((Monster)target,p,p.getEquipment().getItemInMainHand());
				setAggroGlowTickTime((Monster)target,100);
			} else {
				GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.GLOWING, 20*5, 0, target);
			}
		}
	}

	public static void appendDebuffsToName(LivingEntity target) {
		/*if (target.getCustomName()==null) {
			//Setup name.
			target.setCustomName(GenericFunctions.CapitalizeFirstLetters(target.getType().name().replace("_", " ")));
		}
		if (!target.getCustomName().contains(ChatColor.RESET+" ")) { //Append our separator character.
			target.setCustomName(target.getCustomName()+ChatColor.RESET+" ");
		}
		//Now split it using that as our separator.
		String[] split = target.getCustomName().split(ChatColor.RESET+" ");
		
		String suffix = ActionBarBuffUpdater.getActionBarPrefix(target);
		
		if (suffix.length()>0) {
			target.setCustomName(split[0]+ChatColor.RESET+" "+suffix);
		} else {
			target.setCustomName(split[0]);
		}*/
		if (!(target instanceof Player)) {
			LivingEntityStructure les = LivingEntityStructure.GetLivingEntityStructure(target);
			les.suffix_bar=ActionBarBuffUpdater.getActionBarPrefix(target);
		}
	}

	private static void reduceSwiftAegisBuff(Player p) {
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		/*if (pd.swiftaegisamt>0) {
			if (p.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) {
				int resistancelv = GenericFunctions.getPotionEffectLevel(PotionEffectType.DAMAGE_RESISTANCE, p);
				int resistance_duration = GenericFunctions.getPotionEffectDuration(PotionEffectType.DAMAGE_RESISTANCE, p);
				if (resistancelv>0) {
					GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.DAMAGE_RESISTANCE, Math.max(resistance_duration,20*20), resistancelv-1, p, true);
				} else {
					GenericFunctions.logAndRemovePotionEffectFromEntity(PotionEffectType.DAMAGE_RESISTANCE,p);
				}
				pd.swiftaegisamt--;
				TwosideKeeper.log(pd.swiftaegisamt+" stacks of Aegis remaining.", 5);
			} else {
				pd.swiftaegisamt=0;
				TwosideKeeper.log(pd.swiftaegisamt+" stacks of Aegis remaining.", 5);
			}
			if (p.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) {
				GenericFunctions.sendActionBarMessage(p, ChatColor.GRAY+"Resistance "+WorldShop.toRomanNumeral(GenericFunctions.getPotionEffectLevel(PotionEffectType.DAMAGE_RESISTANCE, p)+1));
			} else {
				GenericFunctions.sendActionBarMessage(p, ChatColor.GRAY+"Swift Aegis Resistance Removed.");
			}
		}*/
		if (GenericFunctions.getSwiftAegisAmt(p)>0) {
			pd.swiftaegisamt=Math.max(0, GenericFunctions.getSwiftAegisAmt(p)-1);
			if (GenericFunctions.getSwiftAegisAmt(p)>0) {
				GenericFunctions.sendActionBarMessage(p, ChatColor.GRAY+"Resist "+WorldShop.toRomanNumeral(GenericFunctions.getSwiftAegisAmt(p)));
			} else {
				GenericFunctions.sendActionBarMessage(p, ChatColor.GRAY+"Swift Aegis Resist Removed.");
			}
		}
	}

	private static void triggerEliteBreakEvent(LivingEntity target) {
		if (target instanceof Monster &&
				TwosideKeeper.livingentitydata.containsKey(target.getUniqueId())) {
			LivingEntityStructure ms = LivingEntityStructure.GetLivingEntityStructure((LivingEntity)target);
			if (ms.getElite()) {
	    		boolean exists=false;
	    		for (int i=0;i<TwosideKeeper.elitemonsters.size();i++) {
	    			if (TwosideKeeper.elitemonsters.get(i).m.equals(target)) {
	    				exists=true;
	    				TwosideKeeper.elitemonsters.get(i).BreakBlocksAroundArea();
	    			}
	    		}
	    		if (!exists) {
	    			TwosideKeeper.elitemonsters.add(GenericFunctions.getProperEliteMonster((Monster)target));
	    		}
			}
		}
	}

	public static int GetHeartAmount(double dmg) {
		int heartcount = 1;
		double dmgamountcopy = dmg;
		//TwosideKeeper.log("Starting Damage: "+dmgamountcopy, 0);
		while (dmgamountcopy>10) {
			dmgamountcopy/=2;
			heartcount++;
			//TwosideKeeper.log("Hearts: "+heartcount, 0);
			//TwosideKeeper.log("Remaining Damage: "+dmgamountcopy, 0);
		}
		//TwosideKeeper.log(ChatColor.RED+"Final Heart Count: "+heartcount, 0);
		return heartcount;
	}

	private static void subtractWeaponDurability(Player p,ItemStack weapon) {
		//aPlugin.API.damageItem(p.getInventory(), weapon, 1);
		aPlugin.API.damageItem(p, weapon, 1);
	}

	static void triggerEliteEvent(Player p, Entity damager) {
		for (int i=0;i<TwosideKeeper.elitemonsters.size();i++) {
			if (TwosideKeeper.elitemonsters.get(i).m.equals(getDamagerEntity(damager))) {
				TwosideKeeper.elitemonsters.get(i).hitEvent(p);
			}
		}
	}
	
	private static void triggerEliteHitEvent(Player p, LivingEntity target, double dmg) {
		if (target instanceof Monster &&
				TwosideKeeper.livingentitydata.containsKey(target.getUniqueId())) {
			LivingEntityStructure ms = LivingEntityStructure.GetLivingEntityStructure((Monster)target);
			if (ms.getElite()) {
	    		boolean exists=false;
	    		for (int i=0;i<TwosideKeeper.elitemonsters.size();i++) {
	    			if (TwosideKeeper.elitemonsters.get(i).m.equals(target)) {
	    				exists=true;
	    				TwosideKeeper.elitemonsters.get(i).runHitEvent(p,dmg);
	    			}
	    		}
	    		if (!exists) {
	    			TwosideKeeper.elitemonsters.add(GenericFunctions.getProperEliteMonster((Monster)target));
	    		}
			}
		}
		if (target instanceof Wither) {
			Wither w = (Wither)target;
			/*for (UUID id : TwosideKeeper.custommonsters.keySet()) {
				if (id.equals(w.getUniqueId())) {
					sig.plugin.TwosideKeeper.Monster.Wither wi = (sig.plugin.TwosideKeeper.Monster.Wither)TwosideKeeper.custommonsters.get(id);
					wi.runHitEvent(p, dmg);
				}
			}*/
			if (TwosideKeeper.custommonsters.containsKey(w.getUniqueId())) {
				sig.plugin.TwosideKeeper.Monster.Wither wi = (sig.plugin.TwosideKeeper.Monster.Wither)TwosideKeeper.custommonsters.get(w.getUniqueId());
				wi.runHitEvent(p, dmg);
			}
		}
		if (TwosideKeeper.custommonsters.containsKey(target.getUniqueId())) {
			CustomMonster cm = TwosideKeeper.custommonsters.get(target.getUniqueId());
			cm.onHitEvent(p, dmg);
		}
		for (Room r : TwosideKeeper.roominstances) {
			r.onHitEvent(p,dmg);
		}
		if (target instanceof Villager) {
			Villager v = (Villager)target;
			/*for (UUID id : TwosideKeeper.custommonsters.keySet()) {
				if (id.equals(v.getUniqueId())) {
					sig.plugin.TwosideKeeper.Monster.Wither wi = (sig.plugin.TwosideKeeper.Monster.Wither)TwosideKeeper.custommonsters.get(id);
					wi.runHitEvent(p, dmg);
				}
			}*/
			if (TwosideKeeper.custommonsters.containsKey(v.getUniqueId())) {
				Dummy dm = (Dummy)TwosideKeeper.custommonsters.get(v.getUniqueId());
				dm.addPlayerToHitList(p);
				//dm.customHitHandler(dmg);
			}
		}
	}

	private static void addRegenPoolFromLifesteal(Player p, double damage, ItemStack weapon, String reason) {
		double lifestealamt = damage*calculateLifeStealAmount(p,weapon,reason);
		/*if ((p.getMaxHealth()-p.getHealth())<lifestealamt) {
			double remaining = lifestealamt - (p.getMaxHealth()-p.getHealth());
			if (PlayerMode.getPlayerMode(p)==PlayerMode.BARBARIAN) {
				PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
				//TwosideKeeper.log("Extra "+remaining+" overkill health added to damage pool.", 0);
				pd.damagepool=Math.max(0,pd.damagepool-remaining);
				GenericFunctions.sendActionBarMessage(p, "");
			}
			p.setHealth(p.getMaxHealth());
		} else {
			p.setHealth(p.getHealth()+lifestealamt);
		}*/
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		if (PlayerMode.getPlayerMode(p)==PlayerMode.BARBARIAN) {
			if ((p.getMaxHealth()-p.getHealth())<lifestealamt) {
				double remaining = lifestealamt - (p.getMaxHealth()-p.getHealth());
				p.setHealth(p.getMaxHealth());
				lifestealamt = remaining;
			} else {
				//p.setHealth(p.getHealth()+lifestealamt);
				GenericFunctions.HealEntity(p, lifestealamt);
				lifestealamt=0;
			}
			if (pd.damagepool>0) {
				if (pd.regenpool>pd.damagepool) {
					pd.regenpool-=pd.damagepool;
				}
				pd.damagepool = Math.max(pd.damagepool-pd.regenpool, 0);
				pd.customtitle.updateSideTitleStats(p);
			} else {
				pd.regenpool += lifestealamt;
			}
			if (p.getMaxHealth()==p.getHealth()) {
				pd.damagepool -= Math.max(pd.damagepool-lifestealamt, 0);
			}
		} else {
			pd.regenpool += lifestealamt;
		}
		hardCapRegenPool(p, pd);
		DecimalFormat df = new DecimalFormat("0.00");
		GenericFunctions.sendActionBarMessage(p, "");
		TwosideKeeper.log(p.getName()+" healed "+df.format(lifestealamt)+" dmg from Lifesteal.", 5);
	}

	private static void hardCapRegenPool(Player p, PlayerStructure pd) {
		if (pd.regenpool>p.getMaxHealth()) {
			pd.regenpool=p.getMaxHealth();
		}
	}

	public static void castEruption(Player p, Entity target, ItemStack weapon) {
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		if (GenericFunctions.isArtifactEquip(weapon) &&
				weapon.toString().contains("SPADE") && p.isSneaking() &&
				(target instanceof LivingEntity)) {
			if (ArtifactAbility.containsEnchantment(ArtifactAbility.ERUPTION, weapon) &&
					pd.last_shovelspell<TwosideKeeper.getServerTickTime()) {
				//Detect all nearby mobs and knock them up. Deal damage to them as well.
				List<Entity> finallist = new ArrayList<Entity>();
				List<Entity> nearby = target.getNearbyEntities(2, 2, 2);
				for (int i=0;i<nearby.size();i++) {
					boolean allowed=true;
					if (nearby.get(i) instanceof LivingEntity) {
						if (nearby.get(i) instanceof Player &&
								PVP.isFriendly(p, (Player)nearby.get(i))) {
							allowed=false;
						}
						if (allowed) {
							finallist.add(nearby.get(i));
						}
					}
				}
				finallist.add(target);
				for (int i=0;i<finallist.size();i++) {
					LivingEntity mon = (LivingEntity)finallist.get(i);
					//double finaldmg = CalculateDamageReduction(GenericFunctions.getAbilityValue(ArtifactAbility.ERUPTION, p.getEquipment().getItemInMainHand()),mon,null);
					//GenericFunctions.DealDamageToMob(finaldmg, mon, p, p.getEquipment().getItemInMainHand());
					TwosideKeeperAPI.removeNoDamageTick(p, (LivingEntity)mon);
					CustomDamage.ApplyDamage(35+GenericFunctions.getAbilityValue(ArtifactAbility.ERUPTION, weapon, p),
							p,mon,null,"Eruption",CustomDamage.NONE);
					mon.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION,20,15));
					//Attempt to dig out the blocks below.
					for (int x=-1;x<2;x++) {
						for (int z=-1;z<2;z++) {
							Block b = mon.getLocation().add(x,-1,z).getBlock();
							if (aPlugin.API.isDestroyable(b) && GenericFunctions.isSoftBlock(b)) {
								//log(b.getType()+" is destroyable.",2);
								if (!PVP.isPvPing(p)) {
									@SuppressWarnings("deprecation")
									FallingBlock fb = (FallingBlock)b.getLocation().getWorld().spawnFallingBlock(b.getLocation().add(0,0.1,0),b.getType(),(byte)0);
									fb.setVelocity(new Vector(0,Math.random()*1.35,0));
									fb.setMetadata("FAKE", new FixedMetadataValue(TwosideKeeper.plugin,true));
									//b.breakNaturally();
									b.setType(Material.AIR); 
								}
								aPlugin.API.sendSoundlessExplosion(b.getLocation(), 1);
							}
						}
					}
					SoundUtils.playGlobalSound(mon.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f);
				}
				SoundUtils.playLocalSound(p, Sound.ENTITY_FIREWORK_LARGE_BLAST, 1.0f, 1.0f);
				
				aPluginAPIWrapper.sendCooldownPacket(p, p.getEquipment().getItemInMainHand(), GenericFunctions.GetModifiedCooldown(TwosideKeeper.ERUPTION_COOLDOWN,p));
				pd.last_shovelspell=TwosideKeeper.getServerTickTime()+GenericFunctions.GetModifiedCooldown(TwosideKeeper.ERUPTION_COOLDOWN,p);
			}
		}
	}
	
	private static void removePermEnchantments(Player p, ItemStack weapon) {
		if (GenericFunctions.isEquip(weapon)) {
			GenericFunctions.RemovePermEnchantmentChance(weapon, p);
		}
	}
	
	private static void reduceKnockback(Player p) {
		if (PlayerMode.isBarbarian(p)) {
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			if (pd.rage_time>TwosideKeeper.getServerTickTime()) {
				Bukkit.getScheduler().scheduleSyncDelayedTask(TwosideKeeper.plugin, new Runnable() {
					@Override
					public void run() {
						p.setVelocity(p.getVelocity().multiply(0));
					}
				},1);
			} else {
				Bukkit.getScheduler().scheduleSyncDelayedTask(TwosideKeeper.plugin, new Runnable() {
					@Override
					public void run() {
						p.setVelocity(p.getVelocity().multiply(0.3));
					}
				},1);
			}
			return;
		}
		if (PlayerMode.isDefender(p)) {
			if (DefenderStance.getDefenderStance(p)==DefenderStance.TANK) {
				Bukkit.getScheduler().scheduleSyncDelayedTask(TwosideKeeper.plugin, new Runnable() {
					@Override
					public void run() {
						p.setVelocity(p.getVelocity().multiply(0));
					}
				},1);
			}
		}
		/*if (PlayerMode.isDefender(p) && p.isBlocking()) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(TwosideKeeper.plugin, new Runnable() {
				@Override
				public void run() {
					p.setVelocity(p.getVelocity().multiply(0.25));
				}
			},1);
		}*/
	}

	static void setAggroGlowTickTime(Monster m, int duration) {
		//m.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING,duration,0,true,true),true);
		GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.GLOWING, duration, 0, m);
	}
	
	static void applyProvokeAggro(Monster m, Player p, ItemStack weapon) {
		if (ArtifactAbility.containsEnchantment(ArtifactAbility.PROVOKE, weapon)) {
			//This is allowed, get the level on the weapon.
			setMonsterTarget(m,p);
			//TwosideKeeper.log("Aggro tick time: "+(int)(GenericFunctions.getAbilityValue(ArtifactAbility.PROVOKE, weapon)*20), 0);
			//setAggroGlowTickTime(m,(int)(GenericFunctions.getAbilityValue(ArtifactAbility.PROVOKE, weapon, p)*20));
			LivingEntityStructure les = LivingEntityStructure.GetLivingEntityStructure(m);
			les.increaseAggro(p, (int)GenericFunctions.getAbilityValue(ArtifactAbility.PROVOKE, weapon, p));
		}
	}
	
	static void provokeMonster(LivingEntity m, Player p, ItemStack weapon) {
		if (!m.hasPotionEffect(PotionEffectType.GLOWING)) {
			setMonsterTarget(m,p);
		}
		if (m instanceof Monster) {
			applyDefenderAggro((Monster)m,p);
			applyProvokeAggro((Monster)m,p,weapon);
			leaderRallyNearbyMonsters((Monster)m,p);
			applyDonnerSetAggro((Monster)m,p);
		}
	}
	
	private static void applyDonnerSetAggro(Monster m, Player p) {
		int aggroamt = ItemSet.GetTotalBaseAmount(p, ItemSet.DONNER);
		//setAggroGlowTickTime(m,(int)aggrotime);
		LivingEntityStructure les = LivingEntityStructure.GetLivingEntityStructure(m);
		les.increaseAggro(p, aggroamt);
	}

	static void leaderRallyNearbyMonsters(Monster m, Player p) {
		LivingEntityStructure les = LivingEntityStructure.GetLivingEntityStructure(m);
		if ((MonsterController.isZombieLeader(m) || (
				m.getCustomName()!=null && m.getCustomName().contains(ChatColor.MAGIC+"")
				)) &&
			!m.hasPotionEffect(PotionEffectType.GLOWING) && !les.hasRallied) {
			rallyNearbyMonsters(m,p,24);
			les.hasRallied=true;
		}
	}
	
	private static void rallyNearbyMonsters(Monster m, Player p, double range) {
		Collection<Entity> nearby =m.getLocation().getWorld().getNearbyEntities(m.getLocation(), range, range, range);
		for (Entity ent : nearby) {
			if (ent instanceof Monster) {
				Monster mm = (Monster)ent;
				mm.setTarget(p);
				LivingEntityStructure ms = LivingEntityStructure.GetLivingEntityStructure(mm);
				ms.SetTarget(p);
				ms.hasRallied=true;
			}
		}
	}
	
	static void applyDefenderAggro(Monster m, Player p) {
		if (PlayerMode.isDefender(p)) {
			//TwosideKeeper.log("In here.", 0);
			RefreshVendettaStackTimer(p);
			//setMonsterTarget(m,p);
			//setAggroGlowTickTime(m,100);
			DefenderStance ds = DefenderStance.getDefenderStance(p);
			if (ds == DefenderStance.AGGRESSION) {
				LivingEntityStructure les = LivingEntityStructure.GetLivingEntityStructure(m);
				les.increaseAggro(p, 100);
			}
		}
	}
	
	private static void RefreshVendettaStackTimer(Player p) {
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		if (pd.lastvendettastack+100<TwosideKeeper.getServerTickTime()) {
			pd.lastvendettastack=TwosideKeeper.getServerTickTime()-100;
			GenericFunctions.sendActionBarMessage(p, "");
		}
	}

	static void setMonsterTarget(LivingEntity m, Player p) {
		if (TwosideKeeper.chargezombies.size()<16) {
			addChargeZombieToList(m);
		}
		addToCustomStructures(m,true);
		addMonsterToTargetList(m,p);
	}
	
	public static void addToCustomStructures(LivingEntity m) {
		addToCustomStructures(m,false);
	}

	public static void addToCustomStructures(LivingEntity m, boolean alreadyExisting) {
		removeStraySpiderMinions(m);
		
		
		if (addHellfireSpiderToList(m)) {return;}
		if (addHellfireGhastToList(m)) {return;}
		if (addBlazeToList(m)) {return;}
		if (addWitherToList(m)) {return;}
		
		if (!alreadyExisting) {
			if (m instanceof Skeleton) {
				if (Math.random()<=0.5) {
					if (addKnighttoList(m)) {return;} else {
						addSniperSkeletontoList(m);
					}
				} else {
					if (addSniperSkeletontoList(m)) {return;} else {
						addKnighttoList(m);
					}
				}	
			}
		} else {
			if (SniperSkeleton.isSniperSkeleton(m) && !TwosideKeeper.custommonsters.containsKey(m.getUniqueId())) {
				TwosideKeeper.custommonsters.put(m.getUniqueId(),new SniperSkeleton(m));
			} else
			if (Knight.isKnight(m) && !TwosideKeeper.custommonsters.containsKey(m.getUniqueId())) {
				TwosideKeeper.custommonsters.put(m.getUniqueId(),new Knight(m));
			}
		}

		if (addChallengeZombieToList(m)) {return;}
		if (addChallengeBlazeToList(m)) {return;}
		if (addChallengeSpiderToList(m)) {return;}
		if (addChallengeGhastToList(m)) {return;}
	}
	
	private static boolean addChallengeGhastToList(LivingEntity m) {
		if (!TwosideKeeper.custommonsters.containsKey(m.getUniqueId()) &&
				ChallengeGhast.isChallengeGhast(m)) {
			TwosideKeeper.custommonsters.put(m.getUniqueId(), new ChallengeGhast(m));
			return true;
		}
		return false;
	}
	
	private static boolean addChallengeSpiderToList(LivingEntity m) {
		if (!TwosideKeeper.custommonsters.containsKey(m.getUniqueId()) &&
				ChallengeSpider.isChallengeSpider(m)) {
			TwosideKeeper.custommonsters.put(m.getUniqueId(), new ChallengeSpider(m));
			return true;
		}
		return false;
	}
	
	private static boolean addChallengeBlazeToList(LivingEntity m) {
		if (!TwosideKeeper.custommonsters.containsKey(m.getUniqueId()) &&
				ChallengeBlaze.isChallengeBlaze(m)) {
			TwosideKeeper.custommonsters.put(m.getUniqueId(), new ChallengeBlaze(m));
			return true;
		}
		return false;
	}

	private static boolean addChallengeZombieToList(LivingEntity m) {
		if (!TwosideKeeper.custommonsters.containsKey(m.getUniqueId()) &&
				ChallengeZombie.isChallengeZombie(m)) {
			TwosideKeeper.custommonsters.put(m.getUniqueId(), new ChallengeZombie(m));
			return true;
		}
		return false;
	}

	private static void removeStraySpiderMinions(LivingEntity m) {
		if (!TwosideKeeper.custommonsters.containsKey(m.getUniqueId()) &&
				DarkSpiderMinion.isDarkSpiderMinion(m)) {
			m.remove();
		}
	}

	public static boolean addSniperSkeletontoList(LivingEntity m) {
		if (!TwosideKeeper.custommonsters.containsKey(m.getUniqueId()) &&
				(SniperSkeleton.isSniperSkeleton(m) ||
				(m instanceof Skeleton &&
				SniperSkeleton.randomlyConvertAsSniperSkeleton(m)))) {
			TwosideKeeper.custommonsters.put(m.getUniqueId(),new SniperSkeleton(m));
			TwosideKeeper.log("Spawned a new "+LivingEntityStructure.getCustomLivingEntityName(m), 2);
			TwosideKeeper.LAST_SPECIAL_SPAWN=TwosideKeeper.getServerTickTime();
			return true;
		}
		return false;
	}

	private static boolean addKnighttoList(LivingEntity m) {
		if (!TwosideKeeper.custommonsters.containsKey(m.getUniqueId()) &&
				(Knight.isKnight(m) ||
				(m instanceof Skeleton &&
				Knight.randomlyConvertAsKnight(m)))) {
			TwosideKeeper.custommonsters.put(m.getUniqueId(),new Knight(m));
			TwosideKeeper.log("Spawned a new "+LivingEntityStructure.getCustomLivingEntityName(m), 2);
			TwosideKeeper.LAST_SPECIAL_SPAWN=TwosideKeeper.getServerTickTime();
			return true;
		}
		return false;
	}

	private static boolean addWitherToList(LivingEntity m) {
		if (!TwosideKeeper.custommonsters.containsKey(m.getUniqueId()) &&
				m instanceof Wither) {
			TwosideKeeper.custommonsters.put(m.getUniqueId(),new sig.plugin.TwosideKeeper.Monster.Wither((Monster)m));
			return true;
		}
		return false;
	}

	static boolean addChargeZombieToList(LivingEntity m) {
		if (!TwosideKeeper.chargezombies.containsKey(m.getUniqueId()) &&
				MonsterController.isChargeZombie(m)) {
			TwosideKeeper.chargezombies.put(m.getUniqueId(),new ChargeZombie((Monster)m));
			return true;
		}
		return false;
	}
	
	static boolean addHellfireSpiderToList(LivingEntity m) {
		if (!TwosideKeeper.custommonsters.containsKey(m.getUniqueId()) &&
				MonsterController.isHellfireSpider(m)) {
			TwosideKeeper.custommonsters.put(m.getUniqueId(),new HellfireSpider((Monster)m));
			TwosideKeeper.log("Added Hellfire Spider.", 5);
			return true;
		}
		return false;
	}
	
	static boolean addBlazeToList(LivingEntity m) {
		if (!TwosideKeeper.custommonsters.containsKey(m.getUniqueId()) &&
				m instanceof Blaze) {
			TwosideKeeper.custommonsters.put(m.getUniqueId(),new sig.plugin.TwosideKeeper.Monster.Blaze((Monster)m));
			return true;
		}
		return false;
	}
	
	static boolean addHellfireGhastToList(LivingEntity m) {
		if (!TwosideKeeper.custommonsters.containsKey(m.getUniqueId()) &&
				MonsterController.isHellfireGhast(m)) {
			TwosideKeeper.custommonsters.put(m.getUniqueId(),new HellfireGhast(m));
			return true;
		}
		return false;
	}
	
	public static void addMonsterToTargetList(LivingEntity m,Player p) {
		//if (m instanceof Monster && !m.hasPotionEffect(PotionEffectType.GLOWING)) {((Monster)m).setTarget(p);}
		if (TwosideKeeper.livingentitydata.containsKey(m.getUniqueId())) {
			LivingEntityStructure ms = (LivingEntityStructure)TwosideKeeper.livingentitydata.get(m.getUniqueId());
			//ms.SetTarget(p);
		} else {
			LivingEntityStructure ms = new LivingEntityStructure(m,p);
			TwosideKeeper.livingentitydata.put(m.getUniqueId(),ms);
			//ms.SetTarget(p);
		}
	}

	private static void healDefenderSaturation(Player p) {
		if (PlayerMode.isDefender(p) && p.getSaturation()<20) {
			p.setSaturation(p.getSaturation()+1);
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

	public static void increaseArtifactArmorXP(Player p, int exp) {
		if (p.getHealth()>0) {
			for (ItemStack armor : GenericFunctions.getArmor(p)) {
				if (GenericFunctions.isArtifactEquip(armor) &&
	    				GenericFunctions.isArtifactArmor(armor)) {
					AwakenedArtifact.addPotentialEXP(armor, exp, p);
				}
			}
		}
	}
	
	static List<LivingEntity> getAOEList(ItemStack weapon, LivingEntity target, Player p) {
		List<LivingEntity> list = new ArrayList<LivingEntity>();
		if (ArtifactAbility.containsEnchantment(ArtifactAbility.AOE, weapon)) {
			//TwosideKeeper.log("Weapon contains AOE", 0);
			double aoerange = 1+GenericFunctions.getAbilityValue(ArtifactAbility.AOE, weapon, p); 
			//TwosideKeeper.log("AOE Range: "+aoerange, 0);
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
			if ((entitylist.get(i) instanceof LivingEntity)) {
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

	static public boolean InvulnerableCheck(Entity damager, LivingEntity target) {
		return InvulnerableCheck(damager,0,target,null);
	}
	
	static public boolean InvulnerableCheck(Entity damager, double damage, LivingEntity target, ItemStack weapon) {
		return InvulnerableCheck(damager,damage,target,weapon,"",NONE);
	}
	
	/**
	 * Determines if the target is invulnerable.
	 * @param damager
	 * @param target 
	 * @return Returns true if the target cannot be hit. False otherwise.
	 */
	static public boolean InvulnerableCheck(Entity damager, double damage, LivingEntity target, ItemStack weapon, String reason, int flags) {
		if (target.isDead() || target.getHealth()<=0) {
			return true; //Cancel all damage events if they are dead.
		}
		LivingEntity shooter = getDamagerEntity(damager);
		if ((shooter!=null && shooter.isDead()) || (target!=null && target.isDead())) {
			return true;
		}
		target.setLastDamage(0);
		target.setNoDamageTicks(0);
		target.setMaximumNoDamageTicks(0);
		if (shooter instanceof Player && target instanceof Player) { //PvP Checks
			//!((Player)target).isOnline()
			//!damager.getWorld().getPVP()
			Player attacker = (Player)shooter;
			Player defender = (Player)target;
			if (attacker.getGameMode()==GameMode.SPECTATOR ||
					attacker.getGameMode()==GameMode.CREATIVE ||
					defender.getGameMode()==GameMode.SPECTATOR ||
					defender.getGameMode()==GameMode.CREATIVE) {
				return true;
			}
			if (attacker.getWorld().getPVP() && (!defender.isOnline() ||
					PVP.isFriendly(attacker,defender) || !PVP.isPvPing(defender))) {
				if (PVP.isWaitingForPlayers(defender)) {
					PVP session = PVP.getMatch(defender);
					session.joinMatch(attacker);
				} else
				if (TwosideKeeper.PVPISENABLED && !PVP.isPvPing(defender) && (weapon==null || (weapon.getType()==Material.AIR && weapon.isSimilar(attacker.getEquipment().getItemInMainHand())))) {
					PlayerStructure pd = PlayerStructure.GetPlayerStructure(attacker);
					PlayerStructure pd2 = PlayerStructure.GetPlayerStructure(defender);
					if (pd.lastStartedPlayerClicks+200<=TwosideKeeper.getServerTickTime() &&
							pd2.lastStartedPlayerClicks+200<=TwosideKeeper.getServerTickTime() &&
							!PVP.isPvPing(attacker)) {
						pd.lastStartedPlayerClicks=TwosideKeeper.getServerTickTime();
						PVP.sendPvPRequest(attacker,defender);
					}
				}
				return true; //Cancel all PvP related events.
			}
		}
		if (shooter instanceof Player && !(target instanceof Player)) {
			if (PVP.isPvPing((Player)shooter)) {
				return true; //Can't hit non-PVP targets while PVP'ing.
			}
		}
		if (target instanceof Player && (((Player)target).getGameMode()==GameMode.SPECTATOR || ((Player)target).getGameMode()==GameMode.CREATIVE)) {
			return true; //Cancel any damage events in Spectator mode or Creative Mode.
		}
		if (target.isInvulnerable()) {
			return true; //Cancel any damage events when the target is invulnerable.
		}
		if (isFlagSet(flags,IGNORE_DAMAGE_TICK)) {
			GenericFunctions.removeNoDamageTick(target, damager);
		}
		if (isFlagSet(flags,IGNORE_DAMAGE_TICK) || (GenericFunctions.enoughTicksHavePassed(target, damager) && canHitMobDueToWeakness(damager) && (!GenericFunctions.isSuppressed(getDamagerEntity(damager)) || damager instanceof Projectile) && !target.isDead())) {
			TwosideKeeper.log("Enough ticks have passed.", 5);

			if (CanResistExplosionsWithExperienceSet(damager, target, reason)) {
				aPlugin.API.setTotalExperience((Player)target, (int)Math.max(aPlugin.API.getTotalExperience((Player)target)-ItemSet.TotalBaseAmountBasedOnSetBonusCount((Player)target, ItemSet.ALUSTINE, 2, 2),0));
				SoundUtils.playGlobalSound(((Player)target).getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 3.0f, 1.0f);
				((Player)target).playSound(((Player)target).getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 0.5f);
				GenericFunctions.updateNoDamageTickMap(target, damager);
				return true;
			}
			if (CanResistDotsWithExperienceSet(damager, target, reason)) {
				aPlugin.API.setTotalExperience((Player)target, (int)Math.max(aPlugin.API.getTotalExperience((Player)target)-ItemSet.TotalBaseAmountBasedOnSetBonusCount((Player)target, ItemSet.ALUSTINE, 3, 3),0));
				SoundUtils.playGlobalSound(((Player)target).getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 3.0f, 1.0f);
				((Player)target).playSound(((Player)target).getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 0.5f);
				GenericFunctions.updateNoDamageTickMap(target, damager);
				return true;
			}
			
			if (damager instanceof Monster) {
				Monster m = (Monster)damager;
				LivingEntityStructure les = LivingEntityStructure.GetLivingEntityStructure(m);
				if (les.isElite) {
					for (EliteMonster em : TwosideKeeper.elitemonsters) {
						if (em.m.equals(m)) {
							if (em instanceof EliteZombie) {
								EliteZombie ez = (EliteZombie)em;
								if (ez.storingenergy) {
									return true; //Cancel it, we're storing energy right now.
								}
							}
						}
					}
				}
			}
			
			
			if (isFlagSet(flags,IGNOREDODGE) || !PassesIframeCheck(target,damager)) {
				TwosideKeeper.log("Not in an iframe.", 5);
				if (isFlagSet(flags,IGNOREDODGE) || !PassesDodgeCheck(target,damager)) {
					GenericFunctions.updateNoDamageTickMap(target, damager, (int)-(10-(10/(1.0+GetAttackRate(damager)))));
					TwosideKeeper.log("Did not dodge attack.", 5);
						return false;
				} else {
					if (target instanceof Player) {
						Player p = (Player)target;
						PlayerDodgeEvent ev = new PlayerDodgeEvent(p,damage,damager,reason,flags);
						Bukkit.getPluginManager().callEvent(ev);
						if (ev.isCancelled()) {
							return false;
						}
						if (!p.isBlocking()) {
							SoundUtils.playGlobalSound(p.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 3.0f, 1.0f);
						} else {
							refundRejuvenationCooldown(p);
							SoundUtils.playGlobalSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 0.2f, 3.0f);
						}
						PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
						pd.fulldodge=false;
						pd.slayermegahit=false;
						calculateGracefulDodgeTicks(target);
						GenericFunctions.updateNoDamageTickMap(target, damager);
					} else {
						calculateGracefulDodgeTicks(target);
						GenericFunctions.updateNoDamageTickMap(target, damager);
					}
				}
			}
		}
		return true;
	}

	private static void refundRejuvenationCooldown(Player p) {
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		pd.last_rejuvenate-=40;
		int remainingtime = GenericFunctions.GetRemainingCooldownTime(p, pd.last_rejuvenate, TwosideKeeper.REJUVENATE_COOLDOWN);
		if (remainingtime>0) {
			aPluginAPIWrapper.sendCooldownPacket(p, Material.SHIELD, remainingtime);
		}
	}

	private static double GetAttackRate(Entity damager) {
		double attackrate = 0.0;
		if (damager instanceof Player) {
			Player p = (Player)damager;
			attackrate += ItemSet.GetTotalBaseAmount(p, ItemSet.BLITZEN)/100d;
			if (PlayerMode.getPlayerMode(p)==PlayerMode.DEFENDER) {
				DefenderStance ds = DefenderStance.getDefenderStance(p);
				if (ds == DefenderStance.AGGRESSION) {
					attackrate+=0.25;
				}
			}
			for (Player pp : PartyManager.getPartyMembers(p)) {
				if (PlayerMode.getPlayerMode(pp) == PlayerMode.DEFENDER &&
						DefenderStance.getDefenderStance(pp)==DefenderStance.AGGRESSION) {
					attackrate+=0.25;
				}
			}
		}
		//	TwosideKeeper.log("Attack rate: "+attackrate,0);
		return attackrate;
	}

	private static boolean LowEnoughToResistPoison(LivingEntity target, double damage, String reason) {
		TwosideKeeper.log("Target health: "+target.getHealth(), 5);
		if (reason!=null && reason.equalsIgnoreCase("POISON") && target.getHealth()<=2) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean CanResistExplosionsWithExperienceSet(Entity damager, LivingEntity target, String reason) {
		return target instanceof Player && ItemSet.HasSetBonusBasedOnSetBonusCount((Player)target, ItemSet.ALUSTINE, 2) &&
				((reason!=null && (reason.equalsIgnoreCase("explosion") || reason.equalsIgnoreCase("entity_explosion")))
				|| damager instanceof Creeper) &&
				aPlugin.API.getTotalExperience((Player)target)>=ItemSet.TotalBaseAmountBasedOnSetBonusCount((Player)target, ItemSet.ALUSTINE, 2, 2);
	}
	
	public static boolean CanResistDotsWithExperienceSet(Entity damager, LivingEntity target, String reason) {
		return target instanceof Player && ItemSet.HasSetBonusBasedOnSetBonusCount((Player)target, ItemSet.ALUSTINE, 3) &&
				((reason!=null && (reason.equalsIgnoreCase("shrapnel") ||reason.equalsIgnoreCase("bleeding") ||reason.equalsIgnoreCase("infection") ||reason.equalsIgnoreCase("burn") || reason.equalsIgnoreCase("poison") || reason.equalsIgnoreCase("wither") || reason.equalsIgnoreCase("fire_tick") || reason.equalsIgnoreCase("lava") || reason.equalsIgnoreCase("fire")))) &&
				aPlugin.API.getTotalExperience((Player)target)>=ItemSet.TotalBaseAmountBasedOnSetBonusCount((Player)target, ItemSet.ALUSTINE, 3, 3);
	}
	
	private static boolean canHitMobDueToWeakness(Entity damager) {
		LivingEntity shooter = getDamagerEntity(damager);
		if (shooter!=null &&
				shooter instanceof Player) {
			Player p = (Player)shooter;
			if (p.hasPotionEffect(PotionEffectType.WEAKNESS)) {
				int weaknesslv = GenericFunctions.getPotionEffectLevel(PotionEffectType.WEAKNESS, p);
				if (weaknesslv>=9) {
					SoundUtils.playLocalSound(p, Sound.BLOCK_ANVIL_LAND, 0.3f, 3.6f);
					return false;
				}
			}
		}
		return true;
	}

	private static void calculateGracefulDodgeTicks(LivingEntity target) {
		if (target instanceof Player) {
			ItemStack[] equip = GenericFunctions.getEquipment(target);
			double duration = 0.0;
			for (int i=0;i<equip.length;i++) {
				if (ArtifactAbility.containsEnchantment(ArtifactAbility.GRACEFULDODGE, equip[i])) {
					duration += 0.1+GenericFunctions.getAbilityValue(ArtifactAbility.GRACEFULDODGE, equip[i], (Player)target);
				}
			}
			duration+=ItemSet.TotalBaseAmountBasedOnSetBonusCount((Player)target,ItemSet.JAMDAK,4,4)/20d;
			//Convert from seconds to ticks.
			int tick_duration = (int)(duration*20);
			//Apply iframes.
			if (tick_duration>0) {
				addIframe(tick_duration,(Player)target);
			}
		}
	}

	private static boolean PassesIframeCheck(LivingEntity target, Entity damager) {
		if ((target instanceof Player) && isInIframe((Player)target)) {
			return true;
		} else 
		if (TwosideKeeper.custommonsters.containsKey(target.getUniqueId())) {
			CustomMonster cm = TwosideKeeper.custommonsters.get(target.getUniqueId());
			return cm.isInIframe();
		}
		return false;
	}

	private static boolean PassesDodgeCheck(LivingEntity target, Entity damager) {
		if ((target instanceof Player) && !PVP.isPvPing((Player)target) && Math.random()<CalculateDodgeChance((Player)target)) {
			Player p = (Player)target;
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			double rawdmg = CalculateDamage(0,damager,target,null,null,NONE)*(1d/CalculateDamageReduction(1,target,damager));
			if (p.isBlocking() && ItemSet.hasFullSet(p, ItemSet.SONGSTEEL)) {
				ApplyVendettaStackTimer(pd);
				pd.vendetta_amt+=((1-CalculateDamageReduction(1,target,damager))*(rawdmg*0.40));
				if (TwosideKeeper.getMaxThornsLevelOnEquipment(target)>0) {
					pd.thorns_amt+=((1-CalculateDamageReduction(1,target,damager))*(rawdmg*0.01));
				}
				DecimalFormat df = new DecimalFormat("0.00");
				GenericFunctions.sendActionBarMessage(p, "", true);
				pd.customtitle.updateSideTitleStats(p);
				//GenericFunctions.sendActionBarMessage(p, ChatColor.YELLOW+"              Vendetta: "+ChatColor.GREEN+Math.round(pd.vendetta_amt)+((pd.thorns_amt>0)?"/"+ChatColor.GOLD+df.format(pd.thorns_amt):"")+ChatColor.GREEN+" dmg stored",true);
			}
			if (ItemSet.HasSetBonusBasedOnSetBonusCount(p, ItemSet.VIXEN, 4)) {
				p.setHealth(Math.min(p.getHealth()+(p.getMaxHealth()*0.1), p.getMaxHealth()));
			}
			if (PlayerMode.getPlayerMode(p)==PlayerMode.DEFENDER) {
				GenericFunctions.HealEntity(p, p.getMaxHealth()*0.05);
			}
			return true;
		}
		return false;
	}

	public static double CalculateDodgeChance(Player p) {
		return CalculateDodgeChance(p,null);
	}

	/**
	 * 0 = 0% dodge chance
	 * 1 = 100% dodge chance
	 * @param p
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static double CalculateDodgeChance(Player p, Entity damager) {
		double dodgechance = 0.0d;
		for (ItemStack it : GenericFunctions.getArmor(p)) {
			if (it!=null) {
				if (ArtifactAbility.containsEnchantment(ArtifactAbility.SHADOWWALKER, it) &&
						p.isOnGround() && p.getLocation().getY()>=0 && p.getLocation().add(0,0,0).getBlock().getLightLevel()<=7) {
					dodgechance=addMultiplicativeValue(dodgechance,0.01*ArtifactUtils.getArtifactTier(it));
				}
				if (ArtifactAbility.containsEnchantment(ArtifactAbility.DODGE, it)) {
					dodgechance=addMultiplicativeValue(dodgechance,(ArtifactAbility.calculateValue(ArtifactAbility.DODGE, ArtifactUtils.getArtifactTier(it), ArtifactAbility.getEnchantmentLevel(ArtifactAbility.DODGE, it),PVP.isPvPing(p))/100d));
				}
				
				/*ItemStack equip = p.getEquipment().getArmorContents()[i];
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
				}*/
			}
		}
		dodgechance=addMultiplicativeValue(dodgechance,API.getPlayerBonuses(p).getBonusDodgeChance());
		
		dodgechance=addMultiplicativeValue(dodgechance,ItemSet.GetMultiplicativeTotalBaseAmount(p, ItemSet.ALIKAHN));
		dodgechance=addMultiplicativeValue(dodgechance,ItemSet.GetMultiplicativeTotalBaseAmount(p, ItemSet.DARNYS));
		dodgechance=addMultiplicativeValue(dodgechance,ItemSet.GetMultiplicativeTotalBaseAmount(p, ItemSet.JAMDAK));
		dodgechance=addMultiplicativeValue(dodgechance,ItemSet.GetMultiplicativeTotalBaseAmount(p, ItemSet.LORASAADI));
		//TwosideKeeper.log("Dodge Chance: "+dodgechance, 0);
		dodgechance=addMultiplicativeValue(dodgechance,ItemSet.GetMultiplicativeTotalBaseAmount(p, ItemSet.SHARD));
		//TwosideKeeper.log("Dodge Chance: "+dodgechance, 0);
		if (ItemSet.HasSetBonusBasedOnSetBonusCount(p, ItemSet.LUCI, 2)) {
			dodgechance=addMultiplicativeValue(dodgechance,ItemSet.TotalBaseAmountBasedOnSetBonusCount(p, ItemSet.LUCI, 2, 2)/100d);
		}
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		
		if (ArtifactAbility.containsEnchantment(ArtifactAbility.SHADOWWALKER, p.getEquipment().getItemInMainHand()) &&
				p.isOnGround() && p.getLocation().getY()>=0 && p.getLocation().add(0,0,0).getBlock().getLightLevel()<=7) {
			dodgechance=addMultiplicativeValue(dodgechance,0.01*ArtifactUtils.getArtifactTier(p.getEquipment().getItemInMainHand()));
		}
			
		dodgechance=addMultiplicativeValue(dodgechance,ItemSet.TotalBaseAmountBasedOnSetBonusCount(p,ItemSet.PANROS,3,3)/100d);
		/*if (p.isBlocking() || pd.lastblock+20*5<=TwosideKeeper.getServerTickTime()) {
			dodgechance=addMultiplicativeValue(dodgechance,ItemSet.GetMultiplicativeTotalBaseAmount(p, ItemSet.SONGSTEEL)/3);
		}*/
		dodgechance=addMultiplicativeValue(dodgechance,ItemSet.TotalBaseAmountBasedOnSetBonusCount(p,ItemSet.JAMDAK,2,2)/100d);
		dodgechance=addMultiplicativeValue(dodgechance,ItemSet.TotalBaseAmountBasedOnSetBonusCount(p,ItemSet.JAMDAK,3,3)/100d);
		
		if (ItemSet.HasSetBonusBasedOnSetBonusCount(p, ItemSet.VIXEN, 4)) {
			dodgechance=addMultiplicativeValue(dodgechance,0.2);
		}
		
		LivingEntity shooter = getDamagerEntity(damager);
		if (shooter!=null && shooter instanceof LivingEntity) {
			LivingEntity m = (LivingEntity)shooter;
			if (GenericFunctions.isIsolatedTarget(m, p)) {
				dodgechance=addMultiplicativeValue(dodgechance,0.4);
			}
		}
		
		dodgechance=addMultiplicativeValue(dodgechance,ItemSet.TotalBaseAmountBasedOnSetBonusCount(p, ItemSet.GLADOMAIN, 3, 3)/100d);
		if (ItemSet.HasSetBonusBasedOnSetBonusCount(p, ItemSet.GLADOMAIN, 7)) {
			dodgechance=addMultiplicativeValue(dodgechance,(93.182445*pd.velocity)*(0.05+(0.01*ItemSet.TotalBaseAmountBasedOnSetBonusCount(p, ItemSet.GLADOMAIN, 7, 4)))); //For every 1m, give 5%.
		}
		if (ItemSet.HasSetBonusBasedOnSetBonusCount(p, ItemSet.MOONSHADOW, 7) &&
				GenericFunctions.hasStealth(p)) {
			dodgechance=addMultiplicativeValue(dodgechance,0.4);
		}
		
		if (PlayerMode.isStriker(p) &&
				93.182445*pd.velocity>4.317) {
			dodgechance=addMultiplicativeValue(dodgechance,0.2);
		}
		if (PlayerMode.isRanger(p)) {
			dodgechance=addMultiplicativeValue(dodgechance,0.4);
		}
		
		if (dodgechance>0.95) {
			dodgechance=0.95;
		}
		
		if ((pd.fulldodge || pd.slayermegahit || 
				Buff.hasBuff(p, "BEASTWITHIN")) && !PVP.isPvPing(p)) {
			dodgechance = 1.0;
		}
		
		return dodgechance;  
	}
	
	public static double addMultiplicativeValue(double numb, double val) {
		numb += (1-numb)*val;
		return numb;
	}

	@SuppressWarnings("deprecation")
	static public double CalculateDamageReduction(double basedmg,LivingEntity target,Entity damager) {
		
		double dmgreduction = 0.0;
		
		int protectionlevel = 0;
		int projectileprotectionlevel = 0;
		int explosionprotectionlevel = 0;
		int resistlevel = 0;
		int partylevel = 0;
		int rangeraegislevel = 0;
		double magmacubediv = 0;
		double dmgreductiondiv = 0;
		double tacticspct = 0;
		double darknessdiv = 0;
		double playermodediv = 0;
		double witherdiv = 0;
		double setbonusdiv = 0;
		double tankydiv = 0;
		double artifactmult = 0;
		double dodgechancemult = 0;
		double defenderstancemult = 0;
		
		if (getDamagerEntity(damager) instanceof Player) {
			Player p = (Player)getDamagerEntity(damager);
			if (PlayerMode.getPlayerMode(p) == PlayerMode.DEFENDER) {
				DefenderStance ds = DefenderStance.getDefenderStance(p);
				if (ds==DefenderStance.AGGRESSION) {
					return basedmg;
				} else
				if (ds==DefenderStance.BLOCK) {
					defenderstancemult = 0.25;
				} else
				if (ds==DefenderStance.CHARGE) {
					defenderstancemult = 0.1;
				} else
				if (ds==DefenderStance.TANK) {
					defenderstancemult = 0.5;
				}
			}
		}
		
		if (target instanceof LivingEntity) {
			ItemStack[] armor = GenericFunctions.getEquipment(target,true);
			if (target instanceof Player) {
				Player p = (Player)target;
				dmgreductiondiv += ItemSet.TotalBaseAmountBasedOnSetBonusCount(p, ItemSet.DARNYS, 2, 2)/100d;
				dmgreductiondiv += ItemSet.TotalBaseAmountBasedOnSetBonusCount(p, ItemSet.DARNYS, 3, 3)/100d;
				/*rangerdmgdiv += ItemSet.TotalBaseAmountBasedOnSetBonusCount(p, ItemSet.ALIKAHN, 2, 2)/100d;
				rangerdmgdiv += ItemSet.TotalBaseAmountBasedOnSetBonusCount(p, ItemSet.JAMDAK, 2, 2)/100d;
				rangerdmgdiv += ItemSet.TotalBaseAmountBasedOnSetBonusCount(p, ItemSet.DARNYS, 2, 2)/100d;
				rangerdmgdiv += ItemSet.TotalBaseAmountBasedOnSetBonusCount(p, ItemSet.LORASAADI, 2, 2)/100d;*/
				rangeraegislevel += GenericFunctions.getSwiftAegisAmt(p);
				if ((p).getLocation().getY()>=0 && (p).getLocation().getY()<=255 && (p).getLocation().add(0,0,0).getBlock().getLightLevel()<=7) {
					darknessdiv += ItemSet.GetTotalBaseAmount(p, ItemSet.RUDOLPH)/100d;
				}
				dmgreductiondiv += ItemSet.GetTotalBaseAmount(p, ItemSet.LUCI)/100d;
				dmgreductiondiv += ItemSet.GetTotalBaseAmount(p, ItemSet.PROTECTOR)/100d;
				for (Player pl : PartyManager.getPartyMembers(p)) {
					if (pl!=null && p!=null && !pl.equals(p)) {
						if (PlayerMode.getPlayerMode(pl)==PlayerMode.DEFENDER &&
								ItemSet.HasSetBonusBasedOnSetBonusCount(pl, ItemSet.PROTECTOR, 2)) {
							setbonusdiv += 0.1*ItemSet.GetPlayerModeSpecificMult(p);
						}
					}
				}
			} else {
				LivingEntityStructure les = LivingEntityStructure.GetLivingEntityStructure(target);
				if (!les.checkedforcubes) {
					LivingEntityDifficulty diff = EntityUtils.GetStrongestNearbyEntityDifficulty(EntityType.MAGMA_CUBE, target, 4);
					double reduction = 0.0d;
					if (diff!=null) {
						switch (diff) {
							case DANGEROUS:{
								reduction=0.4d;
							}break;
							case DEADLY:{
								reduction=0.6d;
							}break;
							case HELLFIRE:{
								reduction=0.8d;
							}break;
							default:{
								reduction=0.2d;
							}
						}
						magmacubediv+=Math.min(reduction,1);
					}
					les.checkedforcubes=true;
				}
				if (Knight.isKnight(target)) {
					dmgreductiondiv += Knight.getDamageReduction();
				} else 
				if (DarkSpider.isDarkSpider(target)){
					dmgreductiondiv += DarkSpider.getDamageReduction();
				}
				if (TwosideKeeper.roominstances.size()>0 && 
						getDamagerEntity(damager) instanceof Player) {
					Player p = (Player)getDamagerEntity(damager);
					PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
					if (pd.inTankChallengeRoom) {
						for (Room r : TwosideKeeper.roominstances) {
							tankydiv+=r.getTankRoomDamageReduction();
						}
					}
				}
			}
			
			for (int i=0;i<armor.length;i++) {
				if (armor[i]!=null && GenericFunctions.isArmor(armor[i])) {
					//Check for Protection enchantment.
					//Each Protection level gives 1% extra damage reduction.
					if (armor[i].getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL)>0) {
						protectionlevel+=armor[i].getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL);
					}
					if ((damager instanceof Projectile) && armor[i].getEnchantmentLevel(Enchantment.PROTECTION_PROJECTILE)>0) {
						projectileprotectionlevel+=armor[i].getEnchantmentLevel(Enchantment.PROTECTION_PROJECTILE);
					}
					if ((damager instanceof Creeper) && armor[i].getEnchantmentLevel(Enchantment.PROTECTION_EXPLOSIONS)>0) {
						explosionprotectionlevel+=armor[i].getEnchantmentLevel(Enchantment.PROTECTION_EXPLOSIONS);
					}
					
					boolean isBlockArmor = GenericFunctions.isHardenedItem(armor[i]);
					
					if (target instanceof LivingEntity) {
						isBlockArmor=true;
					}
		
					//If this is an artifact armor, we totally override the base damage reduction.
					if (GenericFunctions.isArmor(armor[i]) && Artifact.isArtifact(armor[i])) {
						//Let's change up the damage.
						//double dmgval = ArtifactItemType.valueOf(Artifact.returnRawTool(armor[i].getType())).getDamageAmt(armor[i].getEnchantmentLevel(Enchantment.LUCK));
						double dmgval=-1;
						if (dmgval!=-1) {
							dmgreduction += dmgval;
							artifactmult += 0.08;
						}
					} else {
						switch (armor[i].getType()) {
							case LEATHER_BOOTS:
							case LEATHER_LEGGINGS:
							case LEATHER_CHESTPLATE:
							case LEATHER_HELMET:
							case CHAINMAIL_BOOTS:
							case CHAINMAIL_LEGGINGS:
							case CHAINMAIL_CHESTPLATE:
							case CHAINMAIL_HELMET:  {
								if (target instanceof Player) {
									Player p = (Player)target;
									if (PVP.isPvPing(p)) {
										dmgreduction+=10*((isBlockArmor)?2:1);
										break;
									}
								}
								dmgreduction+=6*((isBlockArmor)?2:1);
							}break;
							case IRON_BOOTS:
							case IRON_LEGGINGS:
							case IRON_CHESTPLATE:
							case IRON_HELMET: {
								dmgreduction+=7*((isBlockArmor)?2:1);
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
							default:{
								
							}
						}
					}

					if (target instanceof Player) {
						Player p = (Player)target;
						if (GenericFunctions.isArtifactEquip(armor[i])) {
							double reductionamt = GenericFunctions.getAbilityValue(ArtifactAbility.DAMAGE_REDUCTION, armor[i], p);
							if (target instanceof Player &&
									PlayerMode.getPlayerMode((Player)target)==PlayerMode.RANGER) {
								dmgreduction+=reductionamt/2;
							} else {
								dmgreduction+=reductionamt;
							}
							
							TwosideKeeper.log("Reducing damage by "+reductionamt+"%",5);
							/*if (ArtifactAbility.containsEnchantment(ArtifactAbility.GREED, armor[i])) {
								dmgreduction /= ArtifactAbility.containsEnchantment(ArtifactAbility.GREED, armor[i])?2:1;
							}*/
						}
					}
				}
			}
			
			if (target instanceof Wither) {
				witherdiv += 0.2;
			}
			
			
			//Check for resistance effect.
			Collection<PotionEffect> target_effects = target.getActivePotionEffects();
			for (PotionEffect pe : target_effects) {
				if (pe.getType().equals(PotionEffectType.DAMAGE_RESISTANCE)) {
					resistlevel = pe.getAmplifier()+1;
					TwosideKeeper.log("Resistance level is "+resistlevel,5);
				}
			}
		}
		
		double setbonus = 1.0;
		
		if (target instanceof Player) {
			Player p = (Player)target;
	    	PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
	    	partylevel = pd.partybonus;
			if (partylevel>6) {partylevel=6;}
			if (p.getLocation().getY()>=0) {TwosideKeeper.log("Light level: "+p.getLocation().add(0,0,0).getBlock().getLightLevel(),5);}
			for (int i=0;i<p.getEquipment().getArmorContents().length;i++) {
				if (ArtifactAbility.containsEnchantment(ArtifactAbility.SHADOWWALKER, p.getEquipment().getArmorContents()[i]) &&
						p.getLocation().getY()>=0 &&
						p.isOnGround() && p.getLocation().add(0,0,0).getBlock().getLightLevel()<=7) {
					double dmgreduce = 1d-(GenericFunctions.getAbilityValue(ArtifactAbility.SHADOWWALKER, p.getEquipment().getArmorContents()[i], p)/100d);
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
			tacticspct = API.getPlayerBonuses(p).getBonusPercentDamageReduction();
			setbonus = ((100-ItemSet.TotalBaseAmountBasedOnSetBonusCount(p, ItemSet.SONGSTEEL, 4, 4))/100d);

			playermodediv=(PlayerMode.getPlayerMode(p)==PlayerMode.NORMAL)?0.2d:0;
			
			if (PVP.isPvPing(p)) {
				dodgechancemult=CalculateDodgeChance(p,damager)*0.25;
			}
		}
		
		//Blocking: -((p.isBlocking())?ev.getDamage()*0.33:0) //33% damage will be reduced if we are blocking.
		//Shield: -((p.getEquipment().getItemInOffHand()!=null && p.getEquipment().getItemInOffHand().getType()==Material.SHIELD)?ev.getDamage()*0.05:0) //5% damage will be reduced if we are holding a shield.

		TwosideKeeper.log("Protection level: "+protectionlevel, 5);

		
		dmgreduction = ShredDamageReduction(dmgreduction, target,damager);
		
		resistlevel=(resistlevel>10)?10:resistlevel;
		protectionlevel=(protectionlevel>100)?100:protectionlevel;
		partylevel=(partylevel>9)?9:partylevel;
		/*if (damager!=null && target!=null) {
		TwosideKeeper.log(GenericFunctions.GetEntityDisplayName(damager)+"-> "+GenericFunctions.GetEntityDisplayName(target)+":\n"
				+ "Damage Reduction: "+dmgreduction+"\n"
				+ "Resist Level: "+(1d-((resistlevel*10d)/100d))+"\n"
				+ "Ranger Aegis Level: "+(1d-((rangeraegislevel*10d)/100d))+"\n"
				+ "Protection Level: "+(1d-((protectionlevel)/100d))+"\n"
				+ "Projective Protection Level: "+(1d-((projectileprotectionlevel)/100d))+"\n"
				+ "Explosion Protection Level: "+(1d-((explosionprotectionlevel)/100d))+"\n"
				+ "Ranger Damage Divider: "+(1d-rangerdmgdiv)+"\n"
				+ "Magma Cube Divider: "+(1d-magmacubediv)+"\n"
				+ "Party Level: "+(1d-((partylevel*10d)/100d))+"\n"
				+ "Tactics Percent: "+(1d-tacticspct)+"\n"
				+ "Set Bonus: "+setbonus, 0);}*/
		double finaldmg=(basedmg-(basedmg*(dmgreduction/100.0d)))
				*(1d-((resistlevel*10d)/100d))
				*(1d-((rangeraegislevel*10d)/100d))
				*(1d-((protectionlevel)/100d))
				*(1d-((projectileprotectionlevel)/100d))
				*(1d-((explosionprotectionlevel)/100d))
				*(1d-dmgreductiondiv)
				*(1d-magmacubediv)
				*(1d-darknessdiv)
				*(1d-setbonusdiv)
				*(1d-((partylevel*10d)/100d))
				*(1d-tacticspct)
				*(1d-playermodediv)
				*(1d-witherdiv)
				*(1d-tankydiv)
				*(1d-artifactmult)
				*(1d-dodgechancemult)
				*(1d-defenderstancemult)
				*setbonus
				*((target instanceof Player && ((Player)target).isBlocking())?(PlayerMode.isDefender((Player)target))?0.30:0.50:1)
				*((target instanceof Player)?((PlayerMode.isDefender((Player)target))?(target.getEquipment().getItemInOffHand()!=null && target.getEquipment().getItemInOffHand().getType()==Material.SHIELD)?0.8:0.9:(target.getEquipment().getItemInOffHand()!=null && target.getEquipment().getItemInOffHand().getType()==Material.SHIELD)?0.95:1):1);
	
		if (basedmg!=finaldmg) {
			TwosideKeeper.log("Original damage was: "+basedmg,5);
			TwosideKeeper.log(finaldmg+" damage calculated for: "+target.getName()+".",5);
		}
		
		if (target instanceof Player) {
			Player p = (Player)target;
	    	PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			pd.prev_armordef = finaldmg;
			if (Buff.hasBuff(p, "BEASTWITHIN")) {
				finaldmg = 0;
			}
		}
		
		return finaldmg;
	}
	
	private static double ShredDamageReduction(double dmgreduction, LivingEntity target, Entity damager) {
		LivingEntity shooter = getDamagerEntity(damager);
		if (shooter!=null && (shooter instanceof Player) &&
				ItemSet.HasSetBonusBasedOnSetBonusCount((Player)shooter, ItemSet.PRANCER, 4)) {
			PlayerStructure pd = PlayerStructure.GetPlayerStructure((Player)shooter);
			if (pd.ignoretargetarmor+200<TwosideKeeper.getServerTickTime()) {
				pd.ignoretargetarmor=TwosideKeeper.getServerTickTime();
				return Math.max(0, dmgreduction-50d);
			}
		}
		return dmgreduction;
	}

	/**
	 * Adds a iframe with the specified amount of ticks in duration.
	 * @param ticks
	 * @param p
	 */
	public static void addIframe(int ticks, Player p) {
		if (p!=null) {
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			if (pd.iframetime<TwosideKeeper.getServerTickTime()+ticks) {
				pd.iframetime=TwosideKeeper.getServerTickTime()+ticks;
				GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.GLOWING, ticks, 0, p, true);
				GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.NIGHT_VISION,ticks,64, p);
			}
		}
	}
	
	public static boolean isInIframe(Player p) {
		if (p!=null) {
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			return pd.iframetime>TwosideKeeper.getServerTickTime();
		} else {
			return false;
		}
	}
	
	public static void removeIframe(Player p) {
		if (p!=null) {
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			pd.iframetime=0;
			GenericFunctions.logAndRemovePotionEffectFromEntity(PotionEffectType.GLOWING,p);
			int level = GenericFunctions.getPotionEffectLevel(PotionEffectType.NIGHT_VISION, p);
			if (level==64) {
				GenericFunctions.logAndRemovePotionEffectFromEntity(PotionEffectType.NIGHT_VISION,p);
			}
		}
	}
	
	/**
	 * Returns the shooter of a projectile.
	 * @param damager
	 * @return
	 */
	public static LivingEntity getDamagerEntity(Entity damager) {
		return (damager instanceof LivingEntity)?((LivingEntity)damager):
			((damager instanceof Projectile) && (((Projectile)damager).getShooter() instanceof LivingEntity))?(LivingEntity)((Projectile)damager).getShooter():null;
	}
	
	private static double grabNaturalWeaponDamage(ItemStack weapon) {
		if (GenericFunctions.isArtifactEquip(weapon)) {
			return getBaseArtifactDamageByType(weapon);
		} else {
			return getBaseDamageByType(weapon);
		}
	}
	
	/**
	 * Base values for all weapons in the game.
	 * @param weapon
	 * @return
	 */
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
	
	/**
	 * Base weapon damage for artifact items.
	 * @param weapon
	 * @return
	 */
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
	
	static double calculateEnchantmentDamageIncrease(ItemStack weapon, Entity damager, LivingEntity target) {
		double dmg = 0.0;
		boolean isBow = (weapon!=null && weapon.getType()==Material.BOW); //An exception for melee'ing with bows.
		if (isBow && (damager instanceof Arrow)) {
			dmg+=addToPlayerLogger(damager,target,"POWER",(weapon.containsEnchantment(Enchantment.ARROW_DAMAGE))?1.0+weapon.getEnchantmentLevel(Enchantment.ARROW_DAMAGE)*0.5:0.0);
		} else {
			dmg+=addToPlayerLogger(damager,target,"SHARPNESS",(weapon.containsEnchantment(Enchantment.DAMAGE_ALL))?1.0+weapon.getEnchantmentLevel(Enchantment.DAMAGE_ALL)*0.5:0.0);
			if (weapon.containsEnchantment(Enchantment.DAMAGE_ARTHROPODS) &&
					((target instanceof Spider))) {
				dmg+=addToPlayerLogger(damager,target,"BANE OF ARTHROPODS",weapon.getEnchantmentLevel(Enchantment.DAMAGE_ARTHROPODS)*2.5);
			}
			if (weapon.containsEnchantment(Enchantment.DAMAGE_UNDEAD) &&
					(target instanceof LivingEntity) && MonsterController.isUndead((LivingEntity)target)) {
				dmg+=addToPlayerLogger(damager,target,"SMITE",weapon.getEnchantmentLevel(Enchantment.DAMAGE_UNDEAD)*2.5);
			}
		}
		return dmg;
	}
	
	static double calculateStrengthEffectMultiplier(LivingEntity damager, LivingEntity target) {
		double mult = 0.0;
		if (damager!=null && damager.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
			mult += (GenericFunctions.getPotionEffectLevel(PotionEffectType.INCREASE_DAMAGE, damager)+1)*0.1;
		}
		return mult;
	}
	
	static double calculateWeaknessEffectMultiplier(LivingEntity damager, LivingEntity target) {
		double mult = 0.0;
		if (damager!=null && damager.hasPotionEffect(PotionEffectType.WEAKNESS)) {
			int weaknesslv = GenericFunctions.getPotionEffectLevel(PotionEffectType.WEAKNESS, damager)+1;
			if (weaknesslv>10) {
				mult -= 1.0;
			} else {
				mult -= weaknesslv*0.1;
			}
		}
		return mult;
	}
	
	@Deprecated
	static double calculatePotionEffectMultiplier(LivingEntity damager, LivingEntity target) {
		double mult = 0.0;
		if (damager.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
			mult += addMultiplierToPlayerLogger(damager,target,"STRENGTH Mult",(GenericFunctions.getPotionEffectLevel(PotionEffectType.INCREASE_DAMAGE, damager)+1)*0.1);
		}

		if (damager.hasPotionEffect(PotionEffectType.WEAKNESS)) {
			int weaknesslv = Math.abs(GenericFunctions.getPotionEffectLevel(PotionEffectType.WEAKNESS, damager))+1;
			if (weaknesslv<=10) {
				mult *= addMultiplierToPlayerLogger(damager,target,ChatColor.RED+"WEAKNESS Mult",(1.0-(weaknesslv*0.1)));
			} else {
				mult = addMultiplierToPlayerLogger(damager,target,ChatColor.RED+"WEAKNESS Mult",0.0);
			}
		}
		return mult;
	}
	
	static double calculateBowDrawbackMultiplier(ItemStack weapon, Entity damager, LivingEntity target) {
		double mult=0.0;
		LivingEntity shooter = getDamagerEntity(damager);
		if (weapon.getType()==Material.BOW &&
			damager instanceof Arrow &&
			shooter instanceof Player) {
			Player p = (Player)shooter;
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			mult -= 1-(((pd.rangermode==BowMode.SNIPE)?(1.0):(pd.lastarrowpower/9d)));
		}
		TwosideKeeper.log("mult is "+mult,5); 
		return mult;
	}
	
	public static double calculateHighwinderDamage(ItemStack weapon, Entity damager) {
		double dmg = 0.0;
		if (damager instanceof Player) {
			Player p = (Player)damager;
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			if (ArtifactAbility.containsEnchantment(ArtifactAbility.HIGHWINDER, weapon)) {
				dmg += Math.min(93.182445*pd.velocity*GenericFunctions.getAbilityValue(ArtifactAbility.HIGHWINDER, weapon, p),10);
				pd.lasthighwinderhit=TwosideKeeper.getServerTickTime();
				GenericFunctions.sendActionBarMessage(p, TwosideKeeper.drawVelocityBar(pd.velocity,pd.highwinderdmg),true);
			}
		}
		return dmg;
	}
	
	public static double calculateBeliggerentMultiplier(ItemStack weapon, Entity damager) {
		double mult=0.0;
		LivingEntity shooter = getDamagerEntity(damager);
		if (shooter instanceof Player) {
			Player p = (Player)shooter;
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			mult+=pd.swordcombo*GenericFunctions.getAbilityValue(ArtifactAbility.COMBO, weapon, p)/100d;
		}
		return mult;
	}

	private static double calculateSetBonusDamage(Entity damager) {
		double dmg = 0.0;
		
		LivingEntity shooter = getDamagerEntity(damager);
		
		if (shooter instanceof Player) {
			dmg += ItemSet.GetTotalBaseAmount((Player)shooter,ItemSet.PANROS);
			dmg += ItemSet.TotalBaseAmountBasedOnSetBonusCount((Player)shooter, ItemSet.PANROS, 2, 2);
			//dmg += ItemSet.TotalBaseAmountBasedOnSetBonusCount((Player)shooter, ItemSet.DAWNTRACKER, 4, 4);
			dmg += ItemSet.TotalBaseAmountBasedOnSetBonusCount((Player)shooter, ItemSet.LORASAADI, 2, 2);
			dmg += ItemSet.TotalBaseAmountBasedOnSetBonusCount((Player)shooter, ItemSet.LORASAADI, 3, 3);
			dmg += ItemSet.GetTotalBaseAmount((Player)shooter, ItemSet.LORASYS);
			//dmg += ItemSet.HasSetBonusBasedOnSetBonusCount(GenericFunctions.getHotbarItems(shooter), (Player)shooter, ItemSet.ALUSTINE, 7)?((Player)shooter).getLevel():0;
			/*dmg += ItemSet.TotalBaseAmountBasedOnSetBonusCount((Player)shooter, ItemSet.JAMDAK, 3, 3);
			dmg += ItemSet.TotalBaseAmountBasedOnSetBonusCount((Player)shooter, ItemSet.DARNYS, 3, 3);
			dmg += ItemSet.TotalBaseAmountBasedOnSetBonusCount((Player)shooter, ItemSet.ALIKAHN, 3, 3);
			dmg += ItemSet.TotalBaseAmountBasedOnSetBonusCount((Player)shooter, ItemSet.LORASAADI, 3, 3);*/
			dmg += ItemSet.TotalBaseAmountBasedOnSetBonusCount((Player)shooter, ItemSet.DASHER, 2, 2);
			dmg += ItemSet.TotalBaseAmountBasedOnSetBonusCount((Player)shooter, ItemSet.DANCER, 2, 2);
			dmg += ItemSet.TotalBaseAmountBasedOnSetBonusCount((Player)shooter, ItemSet.PRANCER, 2, 2);
			dmg += ItemSet.TotalBaseAmountBasedOnSetBonusCount((Player)shooter, ItemSet.VIXEN, 2, 2);
			dmg += ItemSet.TotalBaseAmountBasedOnSetBonusCount((Player)shooter, ItemSet.BLITZEN, 2, 2);
			dmg += ItemSet.TotalBaseAmountBasedOnSetBonusCount((Player)shooter, ItemSet.COMET, 3, 3);
			dmg += ItemSet.TotalBaseAmountBasedOnSetBonusCount((Player)shooter, ItemSet.CUPID, 3, 3);
			dmg += ItemSet.TotalBaseAmountBasedOnSetBonusCount((Player)shooter, ItemSet.DONNER, 3, 3);
			dmg += ItemSet.TotalBaseAmountBasedOnSetBonusCount((Player)shooter, ItemSet.RUDOLPH, 3, 3);
			dmg += ItemSet.TotalBaseAmountBasedOnSetBonusCount((Player)shooter, ItemSet.OLIVE, 3, 3);
			if (ItemSet.HasSetBonusBasedOnSetBonusCount((Player)shooter, ItemSet.PANROS, 5) ||
					ItemSet.HasSetBonusBasedOnSetBonusCount((Player)shooter, ItemSet.DAWNTRACKER, 5) ||
					ItemSet.HasSetBonusBasedOnSetBonusCount((Player)shooter, ItemSet.LUCI, 5) ||
					ItemSet.HasSetBonusBasedOnSetBonusCount((Player)shooter, ItemSet.WINDRY, 5) ||
					ItemSet.HasSetBonusBasedOnSetBonusCount((Player)shooter, ItemSet.SHARD, 5) ||
					ItemSet.HasSetBonusBasedOnSetBonusCount((Player)shooter, ItemSet.TOXIN, 5) ||
					ItemSet.HasSetBonusBasedOnSetBonusCount((Player)shooter, ItemSet.PROTECTOR, 5) ||
					ItemSet.HasSetBonusBasedOnSetBonusCount((Player)shooter, ItemSet.SUSTENANCE, 5) ||
					ItemSet.HasSetBonusBasedOnSetBonusCount((Player)shooter, ItemSet.LEGION, 5) ||
					ItemSet.HasSetBonusBasedOnSetBonusCount((Player)shooter, ItemSet.PRIDE, 5) ||
					(ItemSet.meetsSlayerSwordConditions(ItemSet.LORASYS, 9, 1, (Player)shooter)) ||
					(ItemSet.meetsSlayerSwordConditions(ItemSet.ASSASSIN, 9, 1, (Player)shooter)) ||
					(ItemSet.meetsSlayerSwordConditions(ItemSet.STEALTH, 9, 1, (Player)shooter)) ||
					GenericFunctions.HasFullRangerSet((Player)shooter)) {
				dmg += 15;
			}
			if (ItemSet.meetsSlayerSwordConditions(ItemSet.LORASYS, 40, 4, (Player)shooter)) {
				dmg += 55;
			}
			dmg += ItemSet.GetTotalBaseAmount((Player)shooter, ItemSet.WINDRY);
		}
		
		return dmg;
	}

	private static double calculateRangerMultiplier(ItemStack weapon, Entity damager) {
		LivingEntity shooter = getDamagerEntity(damager);
		if (shooter instanceof Player) {
			if (PlayerMode.isRanger((Player)shooter)) {
				if (PVP.isPvPing((Player)shooter)) {
					return 0.0;
				} else {
					return 3.0;
				}
			}
		}
		return 0.0;
	}
	
	private static double calculateHeadshotMultiplier(ItemStack weapon, Entity damager, LivingEntity target) {
		double mult=0;
		if (damager instanceof Projectile) {
			Projectile arrow = (Projectile)damager;
			if (target instanceof LivingEntity &&
					arrow instanceof Projectile) {
				LivingEntity m = (LivingEntity)target;
				Projectile proj = (Projectile)arrow;
				Location arrowLoc = proj.getLocation();
				if (proj instanceof Arrow) {
					arrowLoc = aPlugin.API.getArrowHitLocation(target, (Arrow)proj); 
				}
	    		Location monsterHead = m.getEyeLocation();
	    		TwosideKeeper.log("Distance: "+(arrowLoc.distanceSquared(monsterHead)), 5);
	    		boolean isheadshot=false;
				double headshotvaly=0.22/TwosideKeeper.HEADSHOT_ACC;
				TwosideKeeper.log("In here.", 5);
				if (proj.getShooter() instanceof Player) {
					//TwosideKeeper.log("We somehow made it to here???", 0);
					Player p = (Player)proj.getShooter();
					if (PlayerMode.isRanger(p) && 
						GenericFunctions.getBowMode(p)==BowMode.SNIPE) {
						aPlugin.API.sendSoundlessExplosion(arrowLoc, 1);
						headshotvaly *= 4;
					}
					
					if (GenericFunctions.isArtifactEquip(weapon) &&
							ArtifactAbility.containsEnchantment(ArtifactAbility.MARKSMAN, weapon)) {
						headshotvaly *= 1+(GenericFunctions.getAbilityValue(ArtifactAbility.MARKSMAN, weapon, p)/100d);
					}
					
					if (proj.getTicksLived()>=4 || PlayerMode.isRanger(p)) {
						if (arrowLoc.distanceSquared(monsterHead)<=0.3*headshotvaly) {
							
							PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
							
							if (PlayerMode.isRanger(p) && GenericFunctions.getBowMode(p)==BowMode.SNIPE) {
								if (pd.headshotcombo<8) {pd.headshotcombo++;}
								double headshotincrease = (2+(pd.headshotcombo*0.125));
								mult+=headshotincrease;
				    			p.sendMessage(ChatColor.DARK_RED+"Headshot! x"+(headshotincrease)+" Damage");
				    			if (p.hasPotionEffect(PotionEffectType.SLOW)) {
				    				//Add to the current stack of SLOW.
				    				/*for (PotionEffect pe : p.getActivePotionEffects()) {
				    					if (pe.getType().equals(PotionEffectType.SLOW)) {
				    						int lv = pe.getAmplifier();
				    						TwosideKeeper.log("New Slowness level: "+lv,5);
				    						GenericFunctions.logAndRemovePotionEffectFromPlayer(PotionEffectType.SLOW,p);
				    						GenericFunctions.logAndApplyPotionEffectToPlayer(PotionEffectType.SLOW,99,lv+1,p);
				    						GenericFunctions.logAndRemovePotionEffectFromPlayer(PotionEffectType.DAMAGE_RESISTANCE,p);
				    						GenericFunctions.logAndApplyPotionEffectToPlayer(PotionEffectType.DAMAGE_RESISTANCE,99,lv+1,p);
				    						break;
				    					}
				    				}*/
				    				GenericFunctions.addStackingPotionEffect(p, PotionEffectType.SLOW, 99, 7);
				    				GenericFunctions.addStackingPotionEffect(p, PotionEffectType.DAMAGE_RESISTANCE, 99, 7);
				    			} else {
				    				GenericFunctions.addStackingPotionEffect(p, PotionEffectType.SLOW, 99, 7);
				    				GenericFunctions.addStackingPotionEffect(p, PotionEffectType.DAMAGE_RESISTANCE, 99, 7);
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
				    			SoundUtils.playLocalSound(p, Sound.ENTITY_LIGHTNING_IMPACT, 0.1f, 0.24f);
							} else {
								mult+=2.0;
								if (ItemSet.HasSetBonusBasedOnSetBonusCount(p, ItemSet.SHARD, 2)) {
									mult+=ItemSet.TotalBaseAmountBasedOnSetBonusCount(p, ItemSet.SHARD, 2, 2)/100d;
								}
				    			p.sendMessage(ChatColor.DARK_RED+"Headshot! x2 Damage");
							}
							isheadshot=true;
						}

						if (PlayerMode.isRanger(p) && GenericFunctions.getBowMode(p)==BowMode.DEBILITATION) {
							if (isheadshot) {
								/*if (m.hasPotionEffect(PotionEffectType.BLINDNESS)) {
				    				//Add to the current stack of BLINDNESS.
				    				for (PotionEffect pe : m.getActivePotionEffects()) {
				    					if (pe.getType().equals(PotionEffectType.BLINDNESS)) {
				    						int lv = pe.getAmplifier();
				    						TwosideKeeper.log("New BLINDNESS level: "+lv,5);
				    						SoundUtils.playLocalSound(p, Sound.ENTITY_RABBIT_ATTACK, 0.1f, 0.1f+((lv+1)*0.5f));
				    						m.removePotionEffect(PotionEffectType.BLINDNESS);
				    						m.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,400,lv+1),true);
				    						break;
				    					}
				    				}
				    			} else {
				    				m.removePotionEffect(PotionEffectType.BLINDNESS);
				    				m.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,400,0));
									SoundUtils.playLocalSound(p, Sound.ENTITY_RABBIT_ATTACK, 0.1f, 0.1f);
				    			}*/
								if (Buff.hasBuff(target, "Poison")) {
									int oldlv = Buff.getBuff(target, "Poison").getAmplifier();
									long oldexpiretime = Buff.getBuff(target, "Poison").getExpireTime();
									int olddur = (int)(oldexpiretime - TwosideKeeper.getServerTickTime());
		    						SoundUtils.playLocalSound(p, Sound.ENTITY_RABBIT_ATTACK, 0.1f, 0.1f+((oldlv+1)*0.5f));
									Buff.addBuff(target, "Poison", new Buff("Poison",olddur,++oldlv,Color.YELLOW,ChatColor.YELLOW+"☣",false));
								} else {
									SoundUtils.playLocalSound(p, Sound.ENTITY_RABBIT_ATTACK, 0.1f, 0.1f);
									Buff.addBuff(target, "Poison", new Buff("Poison",20*15,1,Color.YELLOW,ChatColor.YELLOW+"☣",false));
								}
							} else {
								SoundUtils.playLocalSound(p, Sound.ENTITY_RABBIT_ATTACK, 0.1f, 0.1f);
								Buff.addBuff(target, "Poison", new Buff("Poison",20*15,1,Color.YELLOW,ChatColor.YELLOW+"☣",false));
							}
						}
					}

					PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
					if (isheadshot) {
						pd.customtitle.modifyLargeCenterTitle(ChatColor.DARK_RED+"HEADSHOT !", 20);
						//pd.customtitle.update();
					}
				}
			}
		}
		return mult;
	}
	
	public static double calculateDefenderAbsorption(LivingEntity entity, Entity damager, double dmg, String reason) {
		//See if we're in a party with a defender.
		if (entity instanceof Player) {
			Player p = (Player)entity;
			LivingEntity shooter = getDamagerEntity(damager);
			List<Player> partymembers = TwosideKeeperAPI.getPartyMembers(p);
			if (partymembers!=null) {
				for (int i=0;i<partymembers.size();i++) {
					Player check = partymembers.get(i);
					if (PartyManager.IsInSameParty(p, check)) {
		    			/*TwosideKeeper.log("In here",5);
						if (!PlayerMode.isDefender(p) && PlayerMode.isDefender(check) &&
								check.isBlocking() &&
								!p.equals(check) && NotTankReason(reason)) {
							//This is a defender. Transfer half the damage to them!
							dmg = dmg/2;
							//Send the rest of the damage to the defender.
							double defenderdmg = dmg;
							//defenderdmg=CalculateDamageReduction(dmg, check, entity);
							ApplyDamage(defenderdmg, shooter, check, null, "Defender Tank", IGNOREDODGE|IGNORE_DAMAGE_TICK);
							//TwosideKeeper.log("Damage was absorbed by "+check.getName()+". Took "+defenderdmg+" reduced damage. Original damage: "+dmg,0);
							break;
						} else*/
						if (!isCupidTank(p) && isCupidTank(check) &&
								!p.equals(check) && NotTankReason(reason)) {
							//This is a cupid tank. Transfer half the damage to them!
							double origdmg = dmg;
							dmg = origdmg-(origdmg*(ItemSet.GetTotalBaseAmount(check, ItemSet.CUPID)/100d));
							//Send the rest of the damage to the cupid tanker.
							double defenderdmg = origdmg*(ItemSet.GetTotalBaseAmount(check, ItemSet.CUPID)/100d);
							//defenderdmg=CalculateDamageReduction(dmg, check, entity);
							ApplyDamage(defenderdmg, shooter, check, null, "Cupid Set Tank", IGNOREDODGE|IGNORE_DAMAGE_TICK);
							//TwosideKeeper.log("Damage was absorbed by "+check.getName()+". Took "+defenderdmg+" reduced damage. Original damage: "+dmg,0);
							break;
						}
					}
				} 
			}
			TwosideKeeper.log("In here",5);
		}
		return dmg;
	}

	public static boolean NotTankReason(String reason) {
		return reason==null || !reason.equalsIgnoreCase("Cupid Set Tank") || !reason.equalsIgnoreCase("Defender Tank");
	}

	private static boolean isCupidTank(Player p) {
		if (ItemSet.GetTotalBaseAmount(p, ItemSet.CUPID)>0) {
			return true;
		} else {
			return false;
		}
	}

	private static double calculateCriticalStrikeMultiplier(ItemStack weapon, LivingEntity shooter,
			LivingEntity target, String reason, int flags) {
		boolean criticalstrike=false;
		double critchance = 0.0;
		critchance += calculateCriticalStrikeChance(weapon, shooter, reason);
		TwosideKeeper.log("Crit Strike chance is "+critchance,4);
		criticalstrike = isCriticalStrike(critchance);
		if (isFlagSet(flags,CRITICALSTRIKE)) {
			criticalstrike=true;
		}
		if (shooter instanceof Player && criticalstrike) {
			Player p = (Player)shooter;
			SoundUtils.playLocalSound(p, Sound.ENTITY_PLAYER_ATTACK_CRIT, 1.0f, 1.0f);
		}
		return criticalstrike?(calculateCriticalStrikeMultiplier(shooter,weapon)):0.0;
	}

	static double calculateCriticalStrikeChance(ItemStack weapon, Entity damager) {
		return calculateCriticalStrikeChance(weapon,damager,null);
	}
	
	static double calculateCriticalStrikeChance(ItemStack weapon, Entity damager, String reason) {
		double critchance = 0.0;
		LivingEntity shooter = getDamagerEntity(damager);
		if (shooter!=null) {
			if (shooter instanceof Player) {
				Player p = (Player)shooter;
				PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
				critchance = addMultiplicativeValue(critchance,(PlayerMode.isStriker(p)?0.2:0.0));
				critchance = addMultiplicativeValue(critchance,ItemSet.TotalBaseAmountBasedOnSetBonusCount(p,ItemSet.PANROS,4,4)/100d);
				critchance = addMultiplicativeValue(critchance,(PlayerMode.isRanger(p)?(GenericFunctions.getPotionEffectLevel(PotionEffectType.SLOW, p)+1)*0.1:0.0));
				critchance = addMultiplicativeValue(critchance,ItemSet.TotalBaseAmountBasedOnSetBonusCount(p, ItemSet.MOONSHADOW, 5, 4)/100d);
				critchance = addMultiplicativeValue(critchance,ItemSet.GetMultiplicativeTotalBaseAmount(p, ItemSet.WOLFSBANE));
				critchance = addMultiplicativeValue(critchance,API.getPlayerBonuses(p).getBonusCriticalChance());
				critchance = addMultiplicativeValue(critchance,(pd.slayermegahit)?1.0:0.0);
				critchance = addMultiplicativeValue(critchance,0.01*GenericFunctions.getAbilityValue(ArtifactAbility.CRITICAL,weapon,p));
				if (reason!=null && reason.equalsIgnoreCase("power swing")) {
					critchance = addMultiplicativeValue(critchance,1.0d);
				}
				if (ItemSet.HasSetBonusBasedOnSetBonusCount((Player)shooter, ItemSet.LORASYS, 1)) {
					int baubletier = ItemSet.GetBaubleTier((Player)shooter);
					int swordtier = ItemSet.GetItemTier(shooter.getEquipment().getItemInMainHand());
					if (baubletier>=18 && swordtier>=2) {
						critchance = addMultiplicativeValue(critchance,0.1d);
						TwosideKeeper.log("Crit Chance: "+critchance, 1);
						if (baubletier>=27 && swordtier>=3) {
							critchance = addMultiplicativeValue(critchance,0.2d);
							TwosideKeeper.log("Crit Chance: "+critchance, 1); 
							if (baubletier>=40 && swordtier>=4) {
								critchance = addMultiplicativeValue(critchance,0.45d);
								TwosideKeeper.log("Crit Chance: "+critchance, 1); 
							}
						}
					}
				}
				if (ItemSet.meetsSlayerSwordConditions(ItemSet.ASSASSIN, 40, 4, p)) {
					critchance = addMultiplicativeValue(critchance,0.3d);
				}
				if (Buff.hasBuff(p, "WINDCHARGE") &&
						ItemSet.HasSetBonusBasedOnSetBonusCount(p, ItemSet.WINDRY, 4)) {
					critchance = addMultiplicativeValue(critchance,Buff.getBuff(p, "WINDCHARGE").getAmplifier()*0.01);
				}
			}
		}
		return critchance;
	}
	
	//0.0-1.0. 0% meaning no resistance. 100% meaning full resistance.
	public static double getPoisonResistance(LivingEntity target) {
		double resist=0.0d;
		if (target instanceof Player) {
			//Nothing here yet.
		} else {
			if (target instanceof Zombie || 
					target instanceof Skeleton ||
					target instanceof Wither) {
				resist+=0.5d;
			}
		}
		return resist;
	}
	
	//0.0-1.0. 0% meaning no resistance. 100% meaning full resistance.
	public static double getFireResistance(LivingEntity target) {
		double resist=0.0d;
		resist+=GenericFunctions.getPotionEffectLevel(PotionEffectType.FIRE_RESISTANCE, target)*0.1d;
		if (target instanceof Player) {
			//Nothing here yet.
		} else {
			if (target instanceof Blaze || 
					target instanceof MagmaCube) {
				resist+=0.5d;
			}
		}
		return resist;
	}

	//Chance is between 0.0-1.0. 1.0 is 100%.
	static boolean isCriticalStrike(double chance) {
		return isCriticalStrike(chance,false);
	}
	
	static boolean isCriticalStrike(double chance, boolean isCriticalStrike) {
		return (Math.random()<=chance || isCriticalStrike);
	}
	
	public static double calculateCriticalStrikeMultiplier(Entity damager, ItemStack weapon) {
		double critdmg=1.0;
		if (getDamagerEntity(damager) instanceof Player) {
			Player p = (Player)getDamagerEntity(damager);
			if (GenericFunctions.HasFullRangerSet(p) &&
					PlayerMode.isRanger(p) &&
					GenericFunctions.getBowMode(p)==BowMode.SNIPE) {
				critdmg+=1.0;
			}
			if (ArtifactAbility.containsEnchantment(ArtifactAbility.CRIT_DMG, weapon)) {
				critdmg+=(GenericFunctions.getAbilityValue(ArtifactAbility.CRIT_DMG,weapon, p))/100d;
			}
			critdmg+=API.getPlayerBonuses(p).getBonusCriticalDamage();
			critdmg+=ItemSet.GetTotalBaseAmount(p, ItemSet.MOONSHADOW)/100d;
			if (ItemSet.HasSetBonusBasedOnSetBonusCount(p, ItemSet.SHARD, 3)) {
				critdmg+=ItemSet.TotalBaseAmountBasedOnSetBonusCount(p, ItemSet.SHARD, 3, 3)/100d;
			}
			if (ItemSet.meetsSlayerSwordConditions(ItemSet.ASSASSIN, 40, 4, p)) {
				critdmg+=1.0;
			}
		}
		TwosideKeeper.log("Crit Damage is "+critdmg, 5);
		return critdmg;
	}
	
	public static double calculatePoisonEffectMultiplier(LivingEntity target) {
		double mult = 0.0;
		if (target!=null) {
			if (target.hasPotionEffect(PotionEffectType.POISON)) {
				mult += (GenericFunctions.getPotionEffectLevel(PotionEffectType.POISON, target)+1)*getPoisonMult(target);
			}
			/*if (target.hasPotionEffect(PotionEffectType.BLINDNESS)) {
				mult += (GenericFunctions.getPotionEffectLevel(PotionEffectType.BLINDNESS, target)+1)*0.5;
			}*/
			if (Buff.hasBuff(target, "Poison")) {
				mult += Buff.getBuff(target, "Poison").getAmplifier()*getPoisonMult(target);
			}
		}
		TwosideKeeper.log("Mult is "+mult, 5);
		return mult;
	}

	private static double getPoisonMult(LivingEntity target) {
		double mult = 0.5;
		if (!(target instanceof Player)) {
			LivingEntityStructure les = LivingEntityStructure.GetLivingEntityStructure(target);
			if (les.isElite || les.isLeader ||
					MonsterController.getLivingEntityDifficulty(target)==LivingEntityDifficulty.T1_MINIBOSS ||
					MonsterController.getLivingEntityDifficulty(target)==LivingEntityDifficulty.T2_MINIBOSS ||
					MonsterController.getLivingEntityDifficulty(target)==LivingEntityDifficulty.T3_MINIBOSS) {
				mult = 0.1;
			}
		} else {
			if (PVP.isPvPing((Player)target)) {
				mult = 0.1;
			}
		}
		return mult;
	}

	public static double calculateStrikerMultiplier(Entity damager, LivingEntity target) {
		double mult=0.0;
		if (damager instanceof Player) {
			Player p = (Player)damager;
			if (PlayerMode.isStriker(p)) {
				mult+=0.1;
			}
		}
		return mult;
	}
	
	static boolean isPreemptiveStrike(LivingEntity m,Player p) {
		if (PlayerMode.isStriker(p) &&
				m!=null &&
				(m instanceof Monster) &&
				((Monster)m).getTarget()==null) {
			return true;
		} else {
			return false;
		}
	}
	
	static double calculatePreemptiveStrikeMultiplier(LivingEntity m, Entity p) {
		double mult = 0.0;
		if (p instanceof Player) {
			Player pl = (Player)p;
			if (isPreemptiveStrike(m,pl)) {
				//Deal triple damage.
				TwosideKeeper.log("Triple damage!",5);
				pl.playSound(pl.getLocation(), Sound.ENTITY_SHULKER_TELEPORT, 1f, 3.65f);
				mult+=2.0;
			}
		}
		return mult;
	}

	private static void subtractHealth(double damage, LivingEntity target) {
		//if (target instanceof ArmorStand) {TwosideKeeper.log("Going to subtract "+damage+" damage", 0);}
		if (target.getHealth()>0) {
			if (target.getHealth()<damage) {
				target.setHealth(0.00001);
				target.damage(Integer.MAX_VALUE);
			} else {
				if (target.getHealth()-damage>target.getMaxHealth()) {
					target.setHealth(target.getMaxHealth());
				} else {
					target.setHealth(target.getHealth()-damage);
				}
			}
		}
	}

	public static void setAbsorptionHearts(LivingEntity l, float new_absorption_val) {
		if (l instanceof LivingEntity) {
			if (l instanceof Player) {
				Player p = (Player)l;
				if (PVP.isPvPing(p)) {
					((org.bukkit.craftbukkit.v1_9_R1.entity.CraftLivingEntity)l).getHandle().setAbsorptionHearts(new_absorption_val*0.25f);
				} else {
					((org.bukkit.craftbukkit.v1_9_R1.entity.CraftLivingEntity)l).getHandle().setAbsorptionHearts(new_absorption_val);
				}
			} else {
				((org.bukkit.craftbukkit.v1_9_R1.entity.CraftLivingEntity)l).getHandle().setAbsorptionHearts(new_absorption_val);
			}
		}
	}
	
	public static float getAbsorptionHearts(LivingEntity l) {
		float absorption = 0.0f;
		if (l instanceof LivingEntity) {
			absorption = ((org.bukkit.craftbukkit.v1_9_R1.entity.CraftLivingEntity)l).getHandle().getAbsorptionHearts();
		}
		return absorption;
	}

	public static double subtractAbsorptionHearts(double damage, LivingEntity target) {
		double finaldmg = damage;
		TwosideKeeper.log("Incoming damage is "+finaldmg, 5);
		if (target instanceof LivingEntity) {
			double absorption = Double.valueOf(getAbsorptionHearts(target));
			if (absorption<finaldmg) {
				finaldmg-=absorption;
				setAbsorptionHearts(target,0);
			} else {
				setAbsorptionHearts(target,(float)(absorption-finaldmg));
				finaldmg=0;
			}
			absorption=getAbsorptionHearts(target);
		}
		return finaldmg;
	}
	
	public static double calculateArmorPen(Entity pl, double dmg, LivingEntity target, ItemStack weapon) {
		double finaldmg = 0.0;
		LivingEntity damager = getDamagerEntity(pl);
		double armorpenmult = 1.0;
		if (damager instanceof Player) {
			Player p = (Player)damager;
			if (target instanceof Player &&
					PVP.isEnemy(p, (Player)target)) {
				armorpenmult = 0.2;
			}
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			if (GenericFunctions.isArtifactEquip(weapon) &&
					ArtifactAbility.containsEnchantment(ArtifactAbility.ARMOR_PEN, weapon)) {
				finaldmg += dmg*(GenericFunctions.getAbilityValue(ArtifactAbility.ARMOR_PEN, weapon, p)/100d)*armorpenmult;
			}
			TextUtils.outputHashmap(pd.itemsets);
			if (GenericFunctions.HasFullRangerSet(p)
					)  {
					if (PlayerMode.isRanger(p) && GenericFunctions.getBowMode(p)==BowMode.DEBILITATION) {
						finaldmg += dmg*0.5*armorpenmult;
					}
					finaldmg += dmg*0.5*armorpenmult; 
			}
			if (ItemSet.HasSetBonusBasedOnSetBonusCount(p, ItemSet.PANROS, 5)) {
				finaldmg += dmg*0.5*armorpenmult;
			} else
			if (ItemSet.HasSetBonusBasedOnSetBonusCount(p, ItemSet.DAWNTRACKER, 5)) {
				finaldmg += dmg*0.5*armorpenmult;
			} else
			if (ItemSet.HasSetBonusBasedOnSetBonusCount(p, ItemSet.WINDRY, 5)) {
				finaldmg += dmg*0.5*armorpenmult;
			} else
			if (ItemSet.HasSetBonusBasedOnSetBonusCount(p, ItemSet.LUCI, 5)) {
				finaldmg += dmg*0.5*armorpenmult;
			} else
			if (ItemSet.HasSetBonusBasedOnSetBonusCount(p, ItemSet.SHARD, 5)) {
				finaldmg += dmg*0.5*armorpenmult;
			} else
			if (ItemSet.HasSetBonusBasedOnSetBonusCount(p, ItemSet.TOXIN, 5)) {
				finaldmg += dmg*0.5*armorpenmult;
			} else
			if (ItemSet.HasSetBonusBasedOnSetBonusCount(p, ItemSet.PROTECTOR, 5)) {
				finaldmg += dmg*0.5*armorpenmult;
			} else
			if (ItemSet.HasSetBonusBasedOnSetBonusCount(p, ItemSet.SUSTENANCE, 5)) {
				finaldmg += dmg*0.5*armorpenmult;
			} else
			if (ItemSet.HasSetBonusBasedOnSetBonusCount(p, ItemSet.LEGION, 5)) {
				finaldmg += dmg*0.5*armorpenmult;
			} else
			if (ItemSet.HasSetBonusBasedOnSetBonusCount(p, ItemSet.PRIDE, 5)) {
				finaldmg += dmg*0.5*armorpenmult;
			} else
			if (ItemSet.meetsSlayerSwordConditions(ItemSet.LORASYS, 9, 1, p)) {
				finaldmg += dmg*0.5*armorpenmult;
			} else
			if (ItemSet.meetsSlayerSwordConditions(ItemSet.ASSASSIN, 9, 1, p)) {
				finaldmg += dmg*0.5*armorpenmult;
			} else
			if (ItemSet.meetsSlayerSwordConditions(ItemSet.STEALTH, 9, 1, p)) {
				finaldmg += dmg*0.5*armorpenmult;
			}
			finaldmg += dmg*aPlugin.API.getPlayerBonuses(p).getBonusArmorPenetration();
			if (Buff.hasBuff(p, "WINDCHARGE") &&
					ItemSet.HasSetBonusBasedOnSetBonusCount(p, ItemSet.WINDRY, 3)) {
				finaldmg += dmg*(Buff.getBuff(p, "WINDCHARGE").getAmplifier()*0.01)*armorpenmult;
			}
		}
		if (finaldmg*armorpenmult>=dmg) {
			return dmg;
		} else {
			return finaldmg*armorpenmult;
		}
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
				//double damage_mult = 2.0d/(c.getLocation().distance(target.getLocation())+1.0);
				double damage_mult = Math.max(0d, 1 - damager.getLocation().distanceSquared(target.getLocation())/49);
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
		case PRIMED_TNT:
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
			difficulty_damage=new double[]{8.0,16.0,960.0};
			break;
		case SHULKER_BULLET:
			difficulty_damage=new double[]{8.0,16.0,960.0};
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
			difficulty_damage=new double[]{10.0,16.0,650.0};
			break;
		case WITHER_SKULL:
			difficulty_damage=new double[]{10.0,16.0,650.0};
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
		case PEACEFUL:
				dmg=0;
			break;
		}
		
		if (TwosideKeeper.custommonsters.containsKey(damager.getUniqueId())) {
			CustomMonster cm = TwosideKeeper.custommonsters.get(damager.getUniqueId());
			dmg += cm.getBasicAttackDamage().getDmgComponent();
		}
		
		return dmg;
	}
	
	static double calculateMonsterDifficultyMultiplier(LivingEntity damager) {
		double mult = 1.0;
		switch (MonsterController.getLivingEntityDifficulty(damager)) {
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
		case END:
			mult*=24.0;
			break;
		default:
			mult*=1.0;
			break;
		}
		return mult;
	}
	
	private static void performMegaKnockback(Entity damager,final LivingEntity target) {
		if (damager instanceof Player) {
			Player p = (Player)damager;
			if (PlayerMode.isRanger(p)) {
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
	
	private static void increaseStrikerSpeed(Player p) {
		if (PlayerMode.getPlayerMode(p)==PlayerMode.STRIKER) {
			GenericFunctions.addStackingPotionEffect(p, PotionEffectType.SPEED, 20*5, 4);
		}
	}

	public static double calculateLifeStealAmount(Player p, ItemStack weapon) {
		return calculateLifeStealAmount(p,weapon,null);
	}
	
	/*0.0-1.0*/
	public static double calculateLifeStealAmount(Player p, ItemStack weapon, String reason) {
		double lifestealpct = GenericFunctions.getAbilityValue(ArtifactAbility.LIFESTEAL, weapon, p)/100;
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		lifestealpct += ItemSet.TotalBaseAmountBasedOnSetBonusCount(p, ItemSet.DAWNTRACKER, 3, 3)/100d;
		lifestealpct += pd.lifestealstacks/100d;
		lifestealpct += API.getPlayerBonuses(p).getBonusLifesteal();
		if (reason!=null && reason.equalsIgnoreCase("power swing")) {
			lifestealpct += 1.0d;
		}
		if (pd.rage_time>TwosideKeeper.getServerTickTime()) {
			lifestealpct += (pd.rage_amt/2)*0.01;
		}
		if (ItemSet.HasSetBonusBasedOnSetBonusCount(p, ItemSet.DAWNTRACKER,6) ||
				ItemSet.HasSetBonusBasedOnSetBonusCount(p, ItemSet.LEGION,6) ||
				ItemSet.HasSetBonusBasedOnSetBonusCount(p, ItemSet.PRIDE,6)) {
			lifestealpct+=0.1d*ItemSet.GetItemTier(p.getEquipment().getItemInMainHand());
		}
		lifestealpct+=ItemSet.TotalBaseAmountBasedOnSetBonusCount(p, ItemSet.LEGION, 2, 2)/100d;
		if (reason!=null && reason.equalsIgnoreCase("sweep up")) {
			lifestealpct*=2;
		}
		return lifestealpct;
	}

	private static double hardCapDamage(double damage, LivingEntity target, String reason) {
		if (damage<0) {
			damage=0;
		}
		return Math.min(damage, TwosideKeeper.CUSTOM_DAMAGE_IDENTIFIER-1);
	}

	public static boolean isFlagSet(int flags, int check) {
		return (flags&check)>0;
	}
	
	public static int setFlag(int flags, int add) {
		return flags|add;
	}

	private static void setupDamagePropertiesForPlayer(Entity damager, int i) {
		setupDamagePropertiesForPlayer(damager,i,false);
	}
	
	private static void setupDamagePropertiesForPlayer(Entity damager, int i, boolean overwrite) {
		if (getDamagerEntity(damager) instanceof Player) {
			Player pl = (Player)getDamagerEntity(damager);
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(pl);
			if (overwrite) {
				pd.lasthitproperties = i;
			} else {
				pd.lasthitproperties |= i;
			}
			TwosideKeeper.log("Flag properties set to "+pd.lasthitproperties,5);
		}
	}
	
	public static void setupTrueDamage(EntityDamageEvent ev) {
		for (int i=0;i<DamageModifier.values().length;i++) {
			if (ev.isApplicable(DamageModifier.values()[i])) {
				ev.setDamage(DamageModifier.values()[i],0);
			}
		}
		ev.setDamage(0);
	}
	
	public static double addToPlayerLogger(Entity p, LivingEntity target, String event, double val) {
		LivingEntity l = getDamagerEntity(p);
		if (l!=null && l instanceof Player) {
			PlayerStructure pd = PlayerStructure.GetPlayerStructure((Player)l);
			if (pd.damagelogging && target!=null) {
				pd.damagedata.addEventToLogger(event, val);
				//pd.damagedata.addCalculatedActualDamage(val);
			}
		}
		return val;
	}

	public static void addToLoggerActual(Entity p, double val) {
		LivingEntity l = getDamagerEntity(p);
		if (l!=null && l instanceof Player) {
			PlayerStructure pd = PlayerStructure.GetPlayerStructure((Player)l);
			if (pd.damagelogging) {
				pd.damagedata.addCalculatedActualDamage(val);
				//pd.damagedata.addCalculatedTotalDamage(val);
			}
		}
	}
	
	public static void addToLoggerTotal(Entity p, double val) {
		LivingEntity l = getDamagerEntity(p);
		if (l!=null && l instanceof Player) {
			PlayerStructure pd = PlayerStructure.GetPlayerStructure((Player)l);
			if (pd.damagelogging) {
				//pd.damagedata.addCalculatedActualDamage(val);
				pd.damagedata.addCalculatedTotalDamage(val);
			}
		}
	}
	
	private static double addMultiplierToPlayerLogger(Entity p, LivingEntity target, String event, double val) {
		LivingEntity l = getDamagerEntity(p);
		if (l!=null && l instanceof Player) {
			PlayerStructure pd = PlayerStructure.GetPlayerStructure((Player)l);
			if (pd.damagelogging && target!=null) {
				pd.damagedata.addMultiplierToLogger(event, val);
				//pd.damagedata.addCalculatedActualDamage(val);
			}
		}
		return val;
	}
	
	//Returns between 0-100.
	public static double getPercentHealthRemaining(LivingEntity target) {
		return ((target.getHealth()/target.getMaxHealth())*100);
	}
	public static double getPercentHealthMissing(LivingEntity target) {
		return 100-getPercentHealthRemaining(target);
	}

	public static void executeVoidSurvival(Player p) {
		if (p!=null && p.isOnline() && p.getGameMode()==GameMode.SURVIVAL) {
			Location p_loc = p.getLocation();
			double totalmoney = TwosideKeeper.getPlayerMoney(p);
			if (totalmoney>=0.01) {
				p_loc.setY(0);
				p.teleport(p_loc);
				GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.SLOW,20*2,10,p);
				GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.REGENERATION,20*16,6,p);
				GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.LEVITATION,20*18,6,p);
				GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.DAMAGE_RESISTANCE,20*26,50,p);
				DecimalFormat df = new DecimalFormat("0.00");
				double rand_amt = 0.0;
				if (totalmoney>5) {
					rand_amt = Math.random()*5;
				} else {
					rand_amt = Math.random()*TwosideKeeper.getPlayerMoney(p);
				}
				p.sendMessage("A Mysterious Entity forcefully removes "+ChatColor.YELLOW+"$"+df.format(rand_amt)+ChatColor.WHITE+" from your pockets.");
				TwosideKeeper.givePlayerMoney(p, -rand_amt);
	    		Bukkit.getScheduler().scheduleSyncDelayedTask(TwosideKeeper.plugin, new Runnable() {
	    			public void run() {
	    				if (p!=null) {
	    					p.sendMessage(ChatColor.AQUA+""+ChatColor.ITALIC+"  \"Enjoy the ride!\"");
	    				}
	    			}}
	    		,40);
			} else {
				PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
				if (pd.last_laugh_time+400<TwosideKeeper.getServerTickTime()) {
					p.sendMessage(ChatColor.RED+""+ChatColor.ITALIC+"A Mysterious Entity looks at your empty pockets with disdain, then laughs chaotically as you fall to your doom.");
					pd.last_laugh_time=TwosideKeeper.getServerTickTime();
				}
			}
		}
	}
	
	public static String getLastDamageReason(Player p) {
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		return pd.lasthitdesc;
	}
	
	/*Returns the amount of cooldown reduction the player has.
	 * 0.0 meaning cooldowns are not reduced at all. 1.0 meaning cooldowns should be non-existent.
	 */
	public static double calculateCooldownReduction(Player p) {
		double cooldown = 0.0;
		if (!PVP.isPvPing(p)) {
			cooldown=addMultiplicativeValue(cooldown,ItemSet.TotalBaseAmountBasedOnSetBonusCount(p, ItemSet.GLADOMAIN, 2, 2)/100d);
			cooldown=addMultiplicativeValue(cooldown,ItemSet.GetMultiplicativeTotalBaseAmount(p, ItemSet.VIXEN));
			if (ItemSet.meetsSlayerSwordConditions(ItemSet.LORASYS, 40, 4, p)) {
				cooldown = addMultiplicativeValue(cooldown,0.45d); 
			}
			cooldown=addMultiplicativeValue(cooldown,ItemSet.GetMultiplicativeTotalBaseAmount(p, ItemSet.ASSASSIN));
			if (ItemSet.meetsSlayerSwordConditions(ItemSet.ASSASSIN, 40, 4, p)) {
				cooldown = addMultiplicativeValue(cooldown,0.3d);
			}
		}
		return cooldown;
	}

	//Returns 0-100.
	public static double CalculateDebuffResistance(Player p) {
		TwosideKeeper.log("Debuffcount went up...",5);
		double removechance = 0.0;
		ItemStack[] equips = p.getEquipment().getArmorContents();
		for (ItemStack equip : equips) {
			if (GenericFunctions.isArtifactEquip(equip)) {
				double resistamt = GenericFunctions.getAbilityValue(ArtifactAbility.STATUS_EFFECT_RESISTANCE, equip, p)/100d;
				TwosideKeeper.log("Resist amount is "+resistamt,5);
				removechance=addMultiplicativeValue(removechance,resistamt);
			}
		}
		removechance=addMultiplicativeValue(removechance,ItemSet.TotalBaseAmountBasedOnSetBonusCount(p, ItemSet.DAWNTRACKER, 2, 2)/100d);
		return removechance*100d;
	}
	

	private static double calculateBackstabMultiplier(LivingEntity target, LivingEntity shooter) {
		double mult = 0.0;
		if (target!=null && shooter!=null && isBackstab(target,shooter) &&
				(shooter instanceof Player) && PlayerMode.getPlayerMode((Player)shooter)==PlayerMode.SLAYER) {
			if (ItemSet.meetsSlayerSwordConditions(ItemSet.ASSASSIN, 27, 3, (Player)shooter)) {
				mult+=7.0;
			} else {
				mult+=3.0;
			}
			if (PVP.isPvPing((Player)shooter)) {
				mult /= 3.0;
			}
			if (ItemSet.meetsSlayerSwordConditions(ItemSet.ASSASSIN, 18, 2, (Player)shooter)) {
				Material name = ((Player)shooter).getEquipment().getItemInMainHand().getType();
				PlayerStructure pd = PlayerStructure.GetPlayerStructure((Player)shooter);
				pd.lastassassinatetime-=(int)(GenericFunctions.GetModifiedCooldown(TwosideKeeper.ASSASSINATE_COOLDOWN,(Player)shooter)*0.5);
				//TwosideKeeper.log("Subtracted "+(int)(GenericFunctions.GetModifiedCooldown(TwosideKeeper.ASSASSINATE_COOLDOWN,(Player)shooter)*0.5)+" ticks from Last Assassinate.", 0);
				if (name!=Material.SKULL_ITEM || pd.lastlifesavertime+GenericFunctions.GetModifiedCooldown(TwosideKeeper.LIFESAVER_COOLDOWN,(Player)shooter)<TwosideKeeper.getServerTickTime()) { //Don't overwrite life saver cooldowns.
					//aPluginAPIWrapper.sendCooldownPacket((Player)shooter, name, (int)(GenericFunctions.GetModifiedCooldown((TwosideKeeper.ASSASSINATE_COOLDOWN),(Player)shooter)*0.5));
					aPluginAPIWrapper.sendCooldownPacket((Player)shooter, name, GenericFunctions.GetRemainingCooldownTime((Player)shooter, pd.lastassassinatetime, TwosideKeeper.ASSASSINATE_COOLDOWN));
				}
			}
		}
		return mult;
	}

	private static boolean isBackstab(LivingEntity target, LivingEntity shooter) {
		if (target.getLocation().getDirection()!=null &&
				shooter.getLocation().getDirection()!=null &&
				Math.signum(target.getLocation().getDirection().getX())==Math.signum(shooter.getLocation().getDirection().getX()) &&
				Math.signum(target.getLocation().getDirection().getZ())==Math.signum(shooter.getLocation().getDirection().getZ()))
		{
			if (shooter instanceof Player) {
				Player p = (Player)shooter;
				if (PlayerMode.getPlayerMode(p)==PlayerMode.SLAYER) {
					SoundUtils.playLocalSound(p, Sound.ENTITY_SHULKER_TELEPORT, 1f, 3.65f);
				}
			}
			return true;
		} else {
			return false;
		}
	}

	private static double calculateCustomArrowDamageIncrease(ItemStack weapon, Entity damager, LivingEntity target) {
		double dmg = 0.0;
		if (damager instanceof Arrow) {
			Arrow a = (Arrow)damager;
			if (a.hasMetadata("QUADRUPLE_DAMAGE_ARR")) {
				dmg += 15.0;
			}
			if (a.hasMetadata("DOUBLE_DAMAGE_ARR")) {
				dmg += 5.0;
			}
			if (a.hasMetadata("PIERCING_ARR")) {
				dmg += 5.0;
			}
		}
		return dmg;
	}

	private static double calculateIsolationMultiplier(LivingEntity shooter, LivingEntity target) {
		double mult = 0.0;
		if (shooter instanceof Player && target instanceof LivingEntity) {
			Player p = (Player)shooter;
			LivingEntity m = (LivingEntity)target;
			if (GenericFunctions.isIsolatedTarget(m, p)) {
				mult += 0.5;
			}
		}
		return mult;
	}

	private static double calculateSetBonusMultiplier(ItemStack weapon, Entity damager) {
		double mult = 0.0;
		LivingEntity shooter = getDamagerEntity(damager);
		if (shooter instanceof Player) {
			mult += ItemSet.TotalBaseAmountBasedOnSetBonusCount((Player)shooter, ItemSet.MOONSHADOW, 3, 3)/100;
		}
		return mult;
	}
	
	private static void suppressTarget(Player p, ItemStack weapon, LivingEntity target) {
		if (ArtifactAbility.containsEnchantment(ArtifactAbility.SUPPRESS, weapon)) {
			double suppressDurationMult=1.0;
			if (target instanceof Player && 
					PVP.isEnemy((Player)target, p)) {
				suppressDurationMult=0.5;
			}
			GenericFunctions.addSuppressionTime(target, (int)(GenericFunctions.getAbilityValue(ArtifactAbility.SUPPRESS, weapon, p)*20*suppressDurationMult));
		}
	}

	public static double getTransferDamage(Player p) {
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		double subtracted = 0;
		if (pd.regenpool>0) {
			subtracted = Math.max(15-GetDamageReductionFromDawntrackerPieces(p),1);
			pd.regenpool-=subtracted;
			if (pd.regenpool<1) {
				pd.regenpool=0;
			}
		}
		return Math.max(1,15-GetDamageReductionFromDawntrackerPieces(p)-subtracted);
	}

	public static int GetDamageReductionFromDawntrackerPieces(Player p) {
		//return (ItemSet.GetTotalBaseAmount(p, ItemSet.DAWNTRACKER))/3;
		return 0;
	}

	private static double increaseDamageDealtByFireTicks(Player p, double damage, String reason) {
		if (reason!=null && (reason.equalsIgnoreCase("LAVA") || reason.equalsIgnoreCase("FIRE"))) {
			p.setFireTicks(p.getFireTicks()+80);
			TwosideKeeper.log("Increasing Fire Ticks to "+p.getFireTicks(), 4);
		}
		if (reason!=null && reason.equalsIgnoreCase("FIRE_TICK")) {
			TwosideKeeper.log("Burning by fire. Fire Ticks remaining: "+p.getFireTicks(), 4);
		}
		return damage+(Math.max(p.getFireTicks()/(80*(((TotalFireProtectionLevel(p))/10)+1)),0));
	}
	
	//0.0-1.0
	public static double calculateDebuffChance(Player p) {
		double chance = 0.0d;
		chance += ItemSet.GetTotalBaseAmount(p, ItemSet.TOXIN)/100d;
		return chance;
	}

	private static int TotalFireProtectionLevel(Player p) {
		ItemStack[] items = GenericFunctions.getArmor(p);
		int fireprot_lv = 0;
		for (ItemStack item : items) {
			if (item!=null && item.containsEnchantment(Enchantment.PROTECTION_FIRE)) {
				fireprot_lv += item.getEnchantmentLevel(Enchantment.PROTECTION_FIRE);
			}
		}
		return fireprot_lv;
	}
}
