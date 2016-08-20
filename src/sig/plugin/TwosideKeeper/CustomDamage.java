package sig.plugin.TwosideKeeper;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.google.common.collect.Iterables;

import sig.plugin.TwosideKeeper.HelperStructures.ArtifactAbility;
import sig.plugin.TwosideKeeper.HelperStructures.ArtifactAbilityApplyEffects;
import sig.plugin.TwosideKeeper.HelperStructures.BowMode;
import sig.plugin.TwosideKeeper.HelperStructures.ItemSet;
import sig.plugin.TwosideKeeper.HelperStructures.MonsterDifficulty;
import sig.plugin.TwosideKeeper.HelperStructures.MonsterType;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;

public class CustomDamage {
	

	public static final int NONE = 0;
	public static final int CRITICALSTRIKE = 1;
	public static final int IGNOREDODGE = 2;
	public static final int TRUEDMG = 4;
	
	//////////////////THE FLAGS BELOW ARE SYSTEM FLAGS!! DO NOT USE THEM!
	public static final int IS_CRIT = 1; //System Flag. Used for telling a player structure their last hit was a crit.
	public static final int IS_HEADSHOT = 2; //System Flag. Used for telling a player structure their last hit was a headshot.
	public static final int IS_PREEMPTIVE = 4; //System Flag. Used for telling a player structure their last hit was a preemptive strike.

	static public boolean ApplyDamage(double damage, Entity damager, LivingEntity target, ItemStack weapon, String reason) {
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
	 * <br><b>Combining flags example:</b> CRITICALSTRIKE|IGNOREDODGE (Force a critical strike AND ignore invulnerability check)
	 * @return Whether or not this attack actually was applied. Returns false if it was dodged, nodamageticks, etc.
	 */
	static public boolean ApplyDamage(double damage, Entity damager, LivingEntity target, ItemStack weapon, String reason, int flags) {
		if (!InvulnerableCheck(damager,target) || (isFlagSet(flags,IGNOREDODGE))) {
			double dmg = 0.0;
			if (isFlagSet(flags,TRUEDMG)) {
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
			if (damager!=null) {
				TwosideKeeper.logHealth(target,target.getHealth(),dmg,damager);
			}
			DealDamageToEntity(dmg, damager, target, weapon, reason, flags);
			addToLoggerTotal(damager,dmg);
			return true;
		} else {
			return false;
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
				dmg+=getBaseWeaponDamage(weapon, damager, target);
				if (weapon.getType()==Material.BOW) {
					if ((damager instanceof Projectile)) {
						dmg += addMultiplierToPlayerLogger(damager,target,"Ranger Mult",dmg * calculateRangerMultiplier(weapon,damager));
						double headshotdmg = addMultiplierToPlayerLogger(damager,target,"Headshot Mult",dmg * calculateHeadshotMultiplier(weapon,damager,target));
						if (headshotdmg!=0.0) {headshot=true;}
						dmg += headshotdmg;
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
			dmg += addToPlayerLogger(damager,target,"Execute",(((GenericFunctions.getAbilityValue(ArtifactAbility.EXECUTION, weapon)*5.0)*(1-(target.getHealth()/target.getMaxHealth())))));
			dmg += addMultiplierToPlayerLogger(damager,target,"Striker Mult",dmg * calculateStrikerMultiplier(shooter,target));
			double preemptivedmg = addMultiplierToPlayerLogger(damager,target,"Preemptive Strike Mult",dmg * calculatePreemptiveStrikeMultiplier(target,shooter));
			if (preemptivedmg!=0.0) {preemptive=true;}
			dmg += preemptivedmg;
			dmg += addMultiplierToPlayerLogger(damager,target,"STRENGTH Mult",dmg * calculateStrengthEffectMultiplier(shooter,target));
			dmg += addMultiplierToPlayerLogger(damager,target,"WEAKNESS Mult",dmg * calculateWeaknessEffectMultiplier(shooter,target));
			dmg += addMultiplierToPlayerLogger(damager,target,"POISON Mult",dmg * calculatePoisonEffectMultiplier(target));
			double critdmg = addMultiplierToPlayerLogger(damager,target,"Critical Strike Mult",dmg * calculateCriticalStrikeMultiplier(weapon,shooter,target,flags));
			if (critdmg!=0.0) {crit=true;}
			dmg += critdmg;
		}
		double armorpendmg = addToPlayerLogger(damager,target,"Armor Pen",calculateArmorPen(damager,dmg,weapon));
		addToLoggerActual(damager,dmg);
		dmg = CalculateDamageReduction(dmg-armorpendmg,target,damager);
		TwosideKeeper.log("Damage: "+dmg+", Armor Pen Damage: "+armorpendmg, 2);
		setupDamagePropertiesForPlayer(damager,((crit)?IS_CRIT:0)|((headshot)?IS_HEADSHOT:0)|((preemptive)?IS_PREEMPTIVE:0));
		dmg = hardCapDamage(dmg+armorpendmg);
		return dmg;
	}

	/**
	 * Returns how much damage comes from the WEAPON, and no other sources.
	 * @param damager Optional.
	 * @param target REQUIRED.
	 * @param weapon
	 * @return
	 */
	public static double getBaseWeaponDamage(ItemStack weapon, Entity damager, LivingEntity target) {
		double dmg = 0.0;
		LivingEntity shooter = getDamagerEntity(damager);
		if (weapon!=null) { //Calculate damage using the weapon.
			if (weapon.getType()==Material.BOW) {
				if ((damager instanceof Projectile)) {
					dmg += addToPlayerLogger(damager, target, "Weapon", grabNaturalWeaponDamage(weapon));
				} else {
					dmg += addToPlayerLogger(damager, target, "Weapon", 1.0);
				}
			} else {
				dmg += addToPlayerLogger(damager, target, "Weapon", grabNaturalWeaponDamage(weapon));
			}
			dmg += calculateEnchantmentDamageIncrease(weapon,damager,target);
			dmg += addToPlayerLogger(damager,target,"Strike",GenericFunctions.getAbilityValue(ArtifactAbility.DAMAGE, weapon));
			dmg += addToPlayerLogger(damager,target,"Highwinder",calculateHighwinderDamage(weapon,damager));
			dmg += addToPlayerLogger(damager,target,"Set Bonus",calculateSetBonusDamage(damager));
			dmg += addMultiplierToPlayerLogger(damager,target,"Belligerent Mult",dmg * calculateBeliggerentMultiplier(weapon,damager));
		}
		return dmg;
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
		applyOnHitEffects(damage,damager,target,weapon,reason,flags);
		if (getDamagerEntity(damager) instanceof Player) { //Player dealing damage to living entities does a custom damage modifier.
			TwosideKeeper.log("Dealing "+damage+" damage.", 5);
			TwosideKeeper.log("Sending out "+(damage+TwosideKeeper.CUSTOM_DAMAGE_IDENTIFIER)+" damage.",5);
			target.damage(damage+TwosideKeeper.CUSTOM_DAMAGE_IDENTIFIER,getDamagerEntity(damager));
		} else 
		if (!(getDamagerEntity(damager) instanceof LivingEntity)) {
			//TwosideKeeper.log("Sending out "+damage+" damage.",2);
			subtractHealth(damage,target);
			aPlugin.API.sendEntityHurtAnimation(target);
		}
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
	static void applyOnHitEffects(double damage, Entity damager, LivingEntity target, ItemStack weapon,
			String reason, int flags) {
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
					pd.vendetta_amt+=(damage-CalculateDamageReduction(damage,target,damager))*0.3;
					aPlugin.API.sendActionBarMessage(p, ChatColor.YELLOW+"Vendetta: "+ChatColor.GREEN+Math.round(pd.vendetta_amt)+" dmg stored");
				}
			}
			if (getDamagerEntity(damager) instanceof Enderman) {
	    		if (MonsterController.getMonsterDifficulty(((Monster)getDamagerEntity(damager)))==MonsterDifficulty.HELLFIRE) {
						for (int i=0;i<4;i++) {
			    			if (Math.random()<=0.2) {
							Monster mm = MonsterController.spawnAdjustedMonster(MonsterType.ENDERMITE, getDamagerEntity(damager).getLocation().add(0,1,0));
							mm.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,Integer.MAX_VALUE,2));
						}
					}
	    		}
			}
			if (getDamagerEntity(damager) instanceof Monster) {
				Monster m = (Monster)getDamagerEntity(damager);
				MonsterStructure md = MonsterStructure.getMonsterStructure(m);
				md.SetTarget(target);
			}
			increaseStrikerSpeed(p);
			increaseArtifactArmorXP(p,(int)damage);
			
		}
		LivingEntity shooter = getDamagerEntity(damager);
		if ((shooter instanceof Player) && target!=null) {
			Player p = (Player)shooter;
			if (damager instanceof TippedArrow) {
				TippedArrow a = (TippedArrow)damager;
				if (a.hasMetadata("EXPLODE_ARR")) {
					//Create an explosion.
					TwosideKeeper.log("In here", 5);
					Location hitloc = aPlugin.API.getArrowHitLocation(target, a);
					GenericFunctions.DealExplosionDamageToEntities(hitloc, getBaseWeaponDamage(weapon,damager,target)+40, 6);
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
			if (target instanceof Monster) {
				provokeMonster((Monster)target,p,weapon);
			}
			if (GenericFunctions.isArtifactEquip(weapon) &&
					GenericFunctions.isArtifactWeapon(weapon)) {		
				double ratio = 1.0-CalculateDamageReduction(1,target,p);
				if (p.getEquipment().getItemInMainHand().getType()!=Material.BOW) {
					AwakenedArtifact.addPotentialEXP(weapon, (int)((ratio*20)+5)*((isFlagSet(flags,IS_HEADSHOT))?2:1), p);
				} else {
					PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
					pd.storedbowxp+=(int)((ratio*20)+5)*((isFlagSet(flags,IS_HEADSHOT))?2:1);
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
						//GenericFunctions.DealDamageToMob(CalculateDamageReduction(dmg,target,damager), hitlist.get(i), shooter, weapon, "AoE Damage");
						ApplyDamage(damage,damager,hitlist.get(i),null,"AoE Damage",flags);
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
				
				increaseSwordComboCount(weapon, p);
			}
			healDefenderSaturation(p);
			reduceDefenderKnockback(p);
			removePermEnchantments(p,weapon);
			castEruption(p,target,weapon);
		}
	}

	public static void castEruption(Player p, Entity target, ItemStack weapon) {
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		if (GenericFunctions.isArtifactEquip(weapon) &&
				weapon.toString().contains("SPADE") && p.isSneaking() &&
				(target instanceof Monster)) {
			if (ArtifactAbility.containsEnchantment(ArtifactAbility.ERUPTION, weapon) &&
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
					//double finaldmg = CalculateDamageReduction(GenericFunctions.getAbilityValue(ArtifactAbility.ERUPTION, p.getEquipment().getItemInMainHand()),mon,null);
					//GenericFunctions.DealDamageToMob(finaldmg, mon, p, p.getEquipment().getItemInMainHand());
					TwosideKeeperAPI.removeNoDamageTick(p, (Monster)target);
					CustomDamage.ApplyDamage(GenericFunctions.getAbilityValue(ArtifactAbility.ERUPTION, weapon),
							p,mon,null,"Eruption",CustomDamage.NONE);
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
				pd.last_shovelspell=TwosideKeeper.getServerTickTime()+TwosideKeeper.ERUPTION_COOLDOWN;
			}
		}
	}
	
	private static void removePermEnchantments(Player p, ItemStack weapon) {
		if (GenericFunctions.isEquip(weapon)) {
			GenericFunctions.RemovePermEnchantmentChance(weapon, p);
		}
	}
	
	private static void reduceDefenderKnockback(Player p) {
		if (GenericFunctions.isDefender(p) && p.isBlocking()) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(TwosideKeeper.plugin, new Runnable() {
				@Override
				public void run() {
					p.setVelocity(p.getVelocity().multiply(0.25));
				}
			},1);
		}
	}

	static void setAggroGlowTickTime(Monster m, int duration) {
		m.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING,duration,0),true);
	}
	
	static void applyProvokeAggro(Monster m, ItemStack weapon) {
		if (ArtifactAbility.containsEnchantment(ArtifactAbility.PROVOKE, weapon)) {
			//This is allowed, get the level on the weapon.
			int provokelv = ArtifactAbility.getEnchantmentLevel(ArtifactAbility.PROVOKE, weapon);
			setAggroGlowTickTime(m,(int)(GenericFunctions.getAbilityValue(ArtifactAbility.PROVOKE, weapon)*20));
		}
	}
	
	static void provokeMonster(Monster m, Player p, ItemStack weapon) {
		applyDefenderAggro(m,p);
		applyProvokeAggro(m,weapon);
		if (!m.hasPotionEffect(PotionEffectType.GLOWING)) {
			setMonsterTarget(m,p);
		}
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
		if (GenericFunctions.isDefender(p)) {
			setMonsterTarget(m,p);
			setAggroGlowTickTime(m,100);
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

	private static void healDefenderSaturation(Player p) {
		if (GenericFunctions.isDefender(p) && p.getSaturation()<20) {
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
		for (int i=0;i<p.getEquipment().getArmorContents().length;i++) {
			if (GenericFunctions.isArtifactEquip(p.getEquipment().getArmorContents()[i]) &&
    				GenericFunctions.isArtifactArmor(p.getEquipment().getArmorContents()[i])) {
				AwakenedArtifact.addPotentialEXP(p.getEquipment().getArmorContents()[i], exp, p);
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

	/**
	 * Determines if the target is invulnerable.
	 * @param damager
	 * @param target
	 * @return Returns true if the target cannot be hit. False otherwise.
	 */
	static public boolean InvulnerableCheck(Entity damager, LivingEntity target) {
		if (GenericFunctions.enoughTicksHavePassed(target, damager)) {
			TwosideKeeper.log("Enough ticks have passed.", 5);
			if (!PassesDodgeCheck(target,damager)) {
				GenericFunctions.updateNoDamageTickMap(target, damager);
				TwosideKeeper.log("Did not dodge attack.", 5);
				if (!PassesIframeCheck(target,damager)) {
					TwosideKeeper.log("Not in an iframe.", 5);
					return false;
				}
			} else {
				GenericFunctions.updateNoDamageTickMap(target, damager);
			}
		}
		return true;
	}
	
	private static boolean PassesIframeCheck(LivingEntity target, Entity damager) {
		if ((target instanceof Player) && isInIframe((Player)target)) {
			return true;
		}
		return false;
	}

	private static boolean PassesDodgeCheck(LivingEntity target, Entity damager) {
		if ((target instanceof Player) && Math.random()<CalculateDodgeChance((Player)target)) {
			return true;
		}
		return false;
	}

	/**
	 * 0 = 0% dodge chance
	 * 1 = 100% dodge chance
	 * @param p
	 * @return
	 */
	public static double CalculateDodgeChance(Player p) {
		double dodgechance = 0.0d;
		dodgechance+=(ArtifactAbility.calculateValue(ArtifactAbility.DODGE, p.getEquipment().getItemInMainHand().getEnchantmentLevel(Enchantment.LUCK), ArtifactAbility.getEnchantmentLevel(ArtifactAbility.DODGE, p.getEquipment().getItemInMainHand()))/100d);
		
		for (int i=0;i<p.getEquipment().getArmorContents().length;i++) {
			if (ArtifactAbility.containsEnchantment(ArtifactAbility.SHADOWWALKER, p.getEquipment().getArmorContents()[i]) &&
					p.isOnGround() && p.getLocation().getY()>=0 && p.getLocation().add(0,0,0).getBlock().getLightLevel()<=4) {
				dodgechance+=0.01*p.getEquipment().getArmorContents()[i].getEnchantmentLevel(Enchantment.LUCK);
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
		dodgechance+=ItemSet.GetTotalBaseAmount(p, ItemSet.ALIKAHN)/100d;
		dodgechance+=ItemSet.GetTotalBaseAmount(p, ItemSet.DARNYS)/100d;
		dodgechance+=ItemSet.GetTotalBaseAmount(p, ItemSet.JAMDAK)/100d;
		dodgechance+=ItemSet.GetTotalBaseAmount(p, ItemSet.LORASAADI)/100d;
	
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
	
	static public double CalculateDamageReduction(double basedmg,LivingEntity target,Entity damager) {
		
		double dmgreduction = 0.0;
		
		int protectionlevel = 0;
		int projectileprotectionlevel = 0;
		int explosionprotectionlevel = 0;
		int resistlevel = 0;
		int partylevel = 0;
		double rangerdmgdiv = 0;
		
		if (target instanceof LivingEntity) {
			ItemStack[] armor = target.getEquipment().getArmorContents();
			if (target instanceof Player) {
				rangerdmgdiv += ItemSet.TotalBaseAmountBasedOnSetBonusCount((Player)target, ItemSet.ALIKAHN, 2, 2)/100d;
				rangerdmgdiv += ItemSet.TotalBaseAmountBasedOnSetBonusCount((Player)target, ItemSet.JAMDAK, 2, 2)/100d;
				rangerdmgdiv += ItemSet.TotalBaseAmountBasedOnSetBonusCount((Player)target, ItemSet.DARNYS, 2, 2)/100d;
				rangerdmgdiv += ItemSet.TotalBaseAmountBasedOnSetBonusCount((Player)target, ItemSet.LORASAADI, 2, 2)/100d;
			}
			
			for (int i=0;i<armor.length;i++) {
				if (armor[i]!=null) {
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
				*(1d-((resistlevel*10d)/100d))
				*(1d-((protectionlevel)/100d))
				*(1d-((projectileprotectionlevel)/100d))
				*(1d-((explosionprotectionlevel)/100d))
				*(1d-rangerdmgdiv)
				//*((10-partylevel)*0.1)
				*setbonus
				*((target instanceof Player && ((Player)target).isBlocking())?(GenericFunctions.isDefender((Player)target))?0.30:0.50:1)
				*((target instanceof Player)?((GenericFunctions.isDefender((Player)target))?0.9:(target.getEquipment().getItemInOffHand()!=null && target.getEquipment().getItemInOffHand().getType()==Material.SHIELD)?0.95:1):1);
	
		if (basedmg!=finaldmg) {
			TwosideKeeper.log("Original damage was: "+basedmg,5);
			TwosideKeeper.log(finaldmg+" damage calculated for: "+target.getName()+".",5);
		}
		
		if (target instanceof Player) {
			Player p = (Player)target;
	    	PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			pd.prev_armordef = finaldmg;
		}
		return finaldmg;
	}
	
	/**
	 * Adds a iframe with the specified amount of ticks in duration.
	 * @param ticks
	 * @param p
	 */
	public static void addIframe(int ticks, Player p) {
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		if (pd.iframetime<TwosideKeeper.getServerTickTime()+ticks) {
			pd.iframetime=TwosideKeeper.getServerTickTime()+ticks;
			int level = GenericFunctions.getPotionEffectLevel(PotionEffectType.NIGHT_VISION, p);
			if (level==64) {
				p.removePotionEffect(PotionEffectType.NIGHT_VISION);
			}
			p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING,ticks,0),true);
			p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION,ticks,64));
		}
	}
	
	public static boolean isInIframe(Player p) {
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		return pd.iframetime>TwosideKeeper.getServerTickTime();
	}
	
	public static void removeIframe(Player p) {
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		pd.iframetime=0;
		p.removePotionEffect(PotionEffectType.GLOWING);
		int level = GenericFunctions.getPotionEffectLevel(PotionEffectType.NIGHT_VISION, p);
		if (level==64) {
			p.removePotionEffect(PotionEffectType.NIGHT_VISION);
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
		LivingEntity shooter = getDamagerEntity(damager);
		boolean isBow = (weapon!=null && weapon.getType()==Material.BOW); //An exception for melee'ing with bows.
		if (isBow && (damager instanceof Arrow)) {
			dmg+=addToPlayerLogger(damager,target,"POWER",(weapon.containsEnchantment(Enchantment.ARROW_DAMAGE))?1.0+weapon.getEnchantmentLevel(Enchantment.ARROW_DAMAGE)*0.5:0.0);
		} else {
			dmg+=addToPlayerLogger(damager,target,"SHARPNESS",(weapon.containsEnchantment(Enchantment.DAMAGE_ALL))?1.0+weapon.getEnchantmentLevel(Enchantment.DAMAGE_ALL)*0.5:0.0);
			if (weapon.containsEnchantment(Enchantment.DAMAGE_ARTHROPODS) &&
					(target instanceof Spider)) {
				dmg+=addToPlayerLogger(damager,target,"BANE OF ARTHROPODS",weapon.getEnchantmentLevel(Enchantment.DAMAGE_ARTHROPODS)*2.5);
			}
			if (weapon.containsEnchantment(Enchantment.DAMAGE_UNDEAD) &&
					(target instanceof Monster) && MonsterController.isUndead((Monster)target)) {
				dmg+=addToPlayerLogger(damager,target,"SMITE",weapon.getEnchantmentLevel(Enchantment.DAMAGE_UNDEAD)*2.5);
			}
		}
		return dmg;
	}
	
	static double calculateStrengthEffectMultiplier(LivingEntity damager, LivingEntity target) {
		double mult = 0.0;
		if (damager.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
			mult += (GenericFunctions.getPotionEffectLevel(PotionEffectType.INCREASE_DAMAGE, damager)+1)*0.1;
		}
		return mult;
	}
	
	static double calculateWeaknessEffectMultiplier(LivingEntity damager, LivingEntity target) {
		double mult = 0.0;
		if (damager.hasPotionEffect(PotionEffectType.WEAKNESS)) {
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
			mult -= 1-(((pd.lastarrowwasinrangermode)?(1.0):(pd.lastarrowpower/9d)));
		}
		TwosideKeeper.log("mult is "+mult,5); 
		return mult;
	}
	
	public static double calculateHighwinderDamage(ItemStack weapon, Entity damager) {
		double dmg = 0.0;
		if (damager instanceof Player) {
			Player p = (Player)damager;
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			dmg += 93.182445*pd.velocity*GenericFunctions.getAbilityValue(ArtifactAbility.HIGHWINDER, weapon);
		}
		return dmg;
	}
	
	public static double calculateBeliggerentMultiplier(ItemStack weapon, Entity damager) {
		double mult=0.0;
		LivingEntity shooter = getDamagerEntity(damager);
		if (shooter instanceof Player) {
			Player p = (Player)shooter;
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			mult+=pd.swordcombo*GenericFunctions.getAbilityValue(ArtifactAbility.COMBO, weapon)/100d;
		}
		return mult;
	}

	private static double calculateSetBonusDamage(Entity damager) {
		double dmg = 0.0;
		
		LivingEntity shooter = getDamagerEntity(damager);
		
		if (shooter instanceof Player) {
			dmg += ItemSet.GetTotalBaseAmount(shooter,ItemSet.PANROS);
			dmg += ItemSet.TotalBaseAmountBasedOnSetBonusCount((Player)shooter, ItemSet.PANROS, 2, 2);
			dmg += ItemSet.TotalBaseAmountBasedOnSetBonusCount((Player)shooter, ItemSet.DAWNTRACKER, 4, 4);
			dmg += ItemSet.TotalBaseAmountBasedOnSetBonusCount((Player)shooter, ItemSet.JAMDAK, 3, 3);
			dmg += ItemSet.TotalBaseAmountBasedOnSetBonusCount((Player)shooter, ItemSet.DARNYS, 3, 3);
			dmg += ItemSet.TotalBaseAmountBasedOnSetBonusCount((Player)shooter, ItemSet.ALIKAHN, 3, 3);
			dmg += ItemSet.TotalBaseAmountBasedOnSetBonusCount((Player)shooter, ItemSet.LORASAADI, 3, 3);
		}
		
		return dmg;
	}

	private static double calculateRangerMultiplier(ItemStack weapon, Entity damager) {
		LivingEntity shooter = getDamagerEntity(damager);
		if (shooter instanceof Player) {
			if (GenericFunctions.isRanger((Player)shooter)) {
				return 4.0;
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
								mult+=headshotincrease;
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
								mult+=2.0;
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
		}
		return mult;
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
						defenderdmg=CalculateDamageReduction(dmg, check, entity);
						ApplyDamage(defenderdmg, p, check, null, "Defender Tank");
						TwosideKeeper.log("Damage was absorbed by "+check.getName()+". Took "+defenderdmg+" reduced damage. Original damage: "+dmg,2);
						break;
					}
				}
			} 
			TwosideKeeper.log("In here",5);
		}
		return dmg;
	}

	private static double calculateCriticalStrikeMultiplier(ItemStack weapon, LivingEntity shooter,
			LivingEntity target, int flags) {
		boolean criticalstrike=false;
		double critchance = 0.0;
		critchance += calculateCriticalStrikeChance(weapon, shooter);
		TwosideKeeper.log("Crit Strike chance is "+critchance,4);
		criticalstrike = isCriticalStrike(critchance);
		if (isFlagSet(flags,CRITICALSTRIKE)) {
			criticalstrike=true;
		}
		if (shooter instanceof Player && criticalstrike) {
			Player p = (Player)shooter;
			p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 1.0f, 1.0f);
		}
		return criticalstrike?(calculateCriticalStrikeMultiplier(shooter,weapon)):0.0;
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
	
	static double calculateCriticalStrikeMultiplier(Entity damager, ItemStack weapon) {
		double critdmg=1.0;
		if (ArtifactAbility.containsEnchantment(ArtifactAbility.CRIT_DMG, weapon)) {
			critdmg+=(GenericFunctions.getAbilityValue(ArtifactAbility.CRIT_DMG,weapon))/100d;
		}
		if (getDamagerEntity(damager) instanceof Player) {
			Player p = (Player)getDamagerEntity(damager);
			if (GenericFunctions.HasFullRangerSet(p) &&
					GenericFunctions.isRanger(p) &&
					GenericFunctions.getBowMode(weapon)==BowMode.SNIPE) {
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
				mult += (GenericFunctions.getPotionEffectLevel(PotionEffectType.POISON, target)+1)*0.5;
			}
			if (target.hasPotionEffect(PotionEffectType.BLINDNESS)) {
				mult += (GenericFunctions.getPotionEffectLevel(PotionEffectType.BLINDNESS, target)+1)*0.5;
			}
		}
		TwosideKeeper.log("Mult is "+mult, 5);
		return mult;
	}

	public static double calculateStrikerMultiplier(Entity damager, LivingEntity target) {
		double mult=0.0;
		if (damager instanceof Player) {
			Player p = (Player)damager;
			if (GenericFunctions.isStriker(p)) {
				mult+=0.1;
			}
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
	
	static double calculatePreemptiveStrikeMultiplier(LivingEntity m, Entity p) {
		double mult = 0.0;
		if (p instanceof Player) {
			Player pl = (Player)p;
			if (isPreemptiveStrike(m,pl)) {
				//Deal triple damage.
				TwosideKeeper.log("Triple damage!",5);
				pl.playSound(pl.getLocation(), Sound.ENTITY_SHULKER_TELEPORT, 1f, 3.65f);
				mult+=3.0;
			}
		}
		return mult;
	}

	private static void subtractHealth(double damage, LivingEntity target) {
		if (target.getHealth()<damage) {
			target.setHealth(0.00001);
			target.damage(Integer.MAX_VALUE);
		} else {
			target.setHealth(target.getHealth()-damage);
		}
	}

	public static void setAbsorptionHearts(LivingEntity l, float new_absorption_val) {
		if (l instanceof LivingEntity) {
			((org.bukkit.craftbukkit.v1_9_R1.entity.CraftLivingEntity)l).getHandle().setAbsorptionHearts(new_absorption_val);
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
	
	private static double calculateArmorPen(Entity pl, double dmg, ItemStack weapon) {
		double finaldmg = 0.0;
		if (pl instanceof Player) {
			Player p = (Player)pl;
			if (GenericFunctions.isArtifactEquip(weapon) &&
					ArtifactAbility.containsEnchantment(ArtifactAbility.ARMOR_PEN, weapon)) {
				finaldmg += dmg*(GenericFunctions.getAbilityValue(ArtifactAbility.ARMOR_PEN, weapon)/100d);
			}
			if (GenericFunctions.HasFullRangerSet(p) &&
					GenericFunctions.isRanger(p) &&
					GenericFunctions.getBowMode(weapon)==BowMode.DEBILITATION) {
				finaldmg += dmg*0.5;
			}
		}
		if (finaldmg>=dmg) {
			return dmg;
		} else {
			return finaldmg;
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
	
	private static void increaseStrikerSpeed(Player p) {
		int speedlv = 0;
		if (p.hasPotionEffect(PotionEffectType.SPEED)) {
			speedlv = GenericFunctions.getPotionEffectLevel(PotionEffectType.SPEED, p)+1;
		}
	}
	
	/*0.0-1.0*/
	public static double calculateLifeStealAmount(Player p) {
		double lifestealpct = GenericFunctions.getAbilityValue(ArtifactAbility.LIFESTEAL, p.getEquipment().getItemInMainHand())/100;
		lifestealpct += ItemSet.TotalBaseAmountBasedOnSetBonusCount(p, ItemSet.DAWNTRACKER, 3, 3)/100d;
		return lifestealpct;
	}

	private static double hardCapDamage(double damage) {
		return Math.min(damage, TwosideKeeper.CUSTOM_DAMAGE_IDENTIFIER-1);
	}

	public static boolean isFlagSet(int flags, int check) {
		return (flags&check)>0;
	}
	
	public static int setFlag(int flags, int add) {
		return flags|add;
	}

	private static void setupDamagePropertiesForPlayer(Entity damager, int i) {
		if (getDamagerEntity(damager) instanceof Player) {
			Player pl = (Player)getDamagerEntity(damager);
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(pl);
			pd.lasthitproperties = i;
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
	static double getPercentHealthRemaining(LivingEntity target) {
		return ((target.getHealth()/target.getMaxHealth())*100);
	}
	public static double getPercentHealthMissing(LivingEntity target) {
		return 100-getPercentHealthRemaining(target);
	}

	public static void executeVoidSurvival(Player p) {
		Location p_loc = p.getLocation();
		double totalmoney = TwosideKeeper.getPlayerMoney(p);
		if (totalmoney>=0.01) {
			p_loc.setY(0);
			p.teleport(p_loc);
			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,20*2 /*Approx 2 sec of no movement.*/,10));
			p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*18 /*Approx 18 sec to reach height 100*/,6));
			p.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION,20*18 /*Approx 18 sec to reach height 100*/,6));
			p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,20*26 /*Reduces fall damage temporarily.*/,500));
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
