package sig.plugin.TwosideKeeper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;
import org.inventivetalent.glow.GlowAPI;

import sig.plugin.TwosideKeeper.HelperStructures.Channel;
import sig.plugin.TwosideKeeper.HelperStructures.ItemSet;
import sig.plugin.TwosideKeeper.HelperStructures.LivingEntityDifficulty;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.DebugUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.EntityUtils;
import sig.plugin.TwosideKeeper.Monster.Knight;

public class LivingEntityStructure {
	public LivingEntity target;
	public String base_name="";
	public String difficulty_modifier="";
	public String prefix="";
	public String suffix="";
	public String suffix_bar="";
	public LivingEntity m;
	public boolean isLeader=false;
	public boolean isElite=false;
	public double original_movespd = 0.0d;
	public HashMap<UUID,Long> hitlist = new HashMap<UUID,Long>();
	public HashMap<UUID,GlowAPI.Color> glowcolorlist = new HashMap<UUID,GlowAPI.Color>();
	//public long lastSpiderBallThrow = 0;
	public BossMonster bm = null;
	public boolean checkedforcubes=false;
	public boolean hasRallied=false;
	public HashMap<String,Buff> buffs = new HashMap<String,Buff>();
	public long lastpotionparticles=0;
	public long lastPoisonTick=0;
	public long lastShrapnelTick=0;
	public long lastBleedingTick=0;
	public long lastInfectionTick=0;
	public long lastCrippleTick=0;
	public long lastBurnTick=0;
	public long lastHit=0;
	public long lastHitbyPlayer=0;
	public Player lastPlayerThatHit=null;
	public float MoveSpeedMultBeforeCripple=1f;
	public Channel currentChannel=null;
	public boolean isImportantGlowEnemy=true;
	public boolean isPet=false;
	public Player petOwner=null;
	public HashMap<UUID,Integer> aggro_table = new HashMap<UUID,Integer>();
	
	final static String MODIFIED_NAME_CODE = ChatColor.RESET+""+ChatColor.RESET+""+ChatColor.RESET;
	final static String MODIFIED_NAME_DELIMITER = ChatColor.RESET+";"+ChatColor.RESET;
	
	public LivingEntityStructure(LivingEntity m) {
		target=null;
		base_name=GetOriginalName(m);
		//TwosideKeeper.log("Original name is "+base_name, 0);
		this.m=m;
		this.original_movespd = m.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue();
	}
	public LivingEntityStructure(LivingEntity m, LivingEntity target) {
		this.target=target;
		base_name=GetOriginalName(m);
		//TwosideKeeper.log("Original name is "+base_name, 0);
		this.m=m;
		this.original_movespd = m.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue();
	}
	public LivingEntityStructure(LivingEntity m, LivingEntity target, BossMonster bm) {
		this.target=target;
		base_name=bm.getName();
		this.m=m;
		this.bm=bm;
		this.original_movespd = m.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue();
	}
	
	private String GetOriginalName(LivingEntity m) {
		if (m.getCustomName()!=null) {
			//TwosideKeeper.log("Custom Name is "+m.getCustomName(), 0);
			if (!isModifiedName(m.getCustomName())) {
				//TwosideKeeper.log("  NOT A MODIFIED NAME! "+m.getCustomName(), 0);
				//See if it's an old version of the difficulty naming system.
				LivingEntityDifficulty diff = MonsterController.getOldLivingEntityDifficulty(m);
				if (diff!=null) {
					String diffname = diff.getDifficultyString();
					String basename = m.getCustomName().replace(diffname+" ", "");
					difficulty_modifier = diffname;
					//TwosideKeeper.log("  Set Difficulty to "+difficulty_modifier, 0);
					//TwosideKeeper.log("  Set Base Name to "+basename, 0);
					return basename;
				} else {
					return m.getCustomName();
				}
			} else {
				String[] splitter = m.getCustomName().split(MODIFIED_NAME_DELIMITER);
				difficulty_modifier = splitter[0];
				//TwosideKeeper.log("  Set Difficulty to "+splitter[0], 0);
				//TwosideKeeper.log("  Set Base Name to "+splitter[1], 0);
				return splitter[1];
			}
		} else {
			return GenericFunctions.CapitalizeFirstLetters(m.getType().name().replace("_", " "));
		}
	}
	
	private boolean isModifiedName(String customName) {
		return customName.contains(MODIFIED_NAME_DELIMITER);
	}
	public String getDifficultyAndMonsterName() {
		StringBuilder sb = new StringBuilder(difficulty_modifier);
		if (difficulty_modifier.length()>0) {
			sb.append(" ");
		}
		sb.append(base_name);
		if (suffix.length()>0) {
			sb.append(" ");
			sb.append(suffix);
		}
		return sb.toString();
	}
	public String getActualName() {
		StringBuilder sb = new StringBuilder(prefix);
		if (prefix.length()==0) {
			if (sb.length()>0 && difficulty_modifier.length()>0) {
				sb.append(" ");
			}
			sb.append(difficulty_modifier);
			if (sb.length()>0 && base_name.length()>0) {
				sb.append(" ");
			}
			sb.append(base_name);
			if (sb.length()>0 && suffix.length()>0) {
				sb.append(" ");
			}
		} else {
			sb.append(" ");
		}
		sb.append(suffix);
		if (sb.length()>0 && suffix_bar.length()>0) {
			sb.append(" ");
		}
		sb.append(suffix_bar);
		sb.append(MODIFIED_NAME_CODE);
		//TwosideKeeper.log(prefix+","+difficulty_modifier+","+base_name+","+suffix+","+suffix_bar, 0);
		return sb.toString();
	}
	
	public LivingEntity GetTarget() {
		if (this.target!=null &&
				!this.target.isDead()) {
			return this.target;
		} else {
			return null;
		}
	}
	public void SetTarget(LivingEntity target) {	
		this.target=target;
	}
	public void SetLeader(boolean leader) {	
		this.isLeader=leader;
		//suffix=(suffix.length()>0)?suffix+" Leader":"Leader";
	}
	public void SetElite(boolean elite) {	
		this.isElite=elite;
	}
	
	public boolean hasOriginalName() {
		return !this.base_name.equalsIgnoreCase("");
	}
	
	public String getOriginalName() {
		if (hasOriginalName()) {
			return this.base_name;
		} else {
			return "";
		}
	}
	
	public boolean getLeader() {
		return this.isLeader;
	}
	public boolean getElite() {
		return this.isElite;
	}
	
	public void setGlow(Player p, GlowAPI.Color col) {
		GlowAPI.setGlowing(m, col, p);
		glowcolorlist.put(p.getUniqueId(), col);
	}
	
	public void setGlobalGlow(GlowAPI.Color col) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			GlowAPI.setGlowing(m, col, p);
			glowcolorlist.put(p.getUniqueId(), col);
		}
	}
	
	public void UpdateGlow() {
		//Updates the glow color for all players. We base it on default statuses here. CALL THIS INSTEAD OF
		// SETTING THE GLOW DIRECTLY ANYMORE!
		for (Player p : Bukkit.getOnlinePlayers()) {
			//if (p!=null && p.isValid() && !p.isDead()) {
			if (p!=null && p.isOnline()) {
				if (isImportantGlowEnemy) {
					if (TwosideKeeper.custommonsters.containsKey(m.getUniqueId()) &&
							TwosideKeeper.custommonsters.get(m.getUniqueId()).getGlowColor()!=null) {
						CustomMonster cm = TwosideKeeper.custommonsters.get(m.getUniqueId());
						if (cm.getGlowColor()!=null) {
							setGlow(p,cm.getGlowColor());
						}
					}
					else 
					if (GenericFunctions.isSuppressed(m)) {
						setGlow(p,GlowAPI.Color.BLACK);
					} else
					if (Channel.isChanneling(m)) {
						setGlow(p,GlowAPI.Color.YELLOW);
					} else
					if (getElite()) {
						boolean handled=false;
						for (EliteMonster em : TwosideKeeper.elitemonsters) {
							if (em.getMonster().equals(m)) {
								setGlow(p,em.getGlow());
								handled=true;
							}
						}
						if (!handled) {
							setGlow(p,GlowAPI.Color.DARK_PURPLE);
						}
					} else
					if (getLeader() || (m instanceof Monster && GenericFunctions.isBossMonster((Monster)m))) {
						//TwosideKeeper.log("Monster "+GenericFunctions.getDisplayName(m)+" is a Leader. Set the Glow.", 0);
						setGlow(p,GlowAPI.Color.DARK_RED);
						//TwosideKeeper.log("Is glowing? "+GlowAPI.isGlowing(m, p)+", Glow color list contains key? "+glowcolorlist.containsKey(p.getUniqueId()), 0);
					} else
					if (GenericFunctions.isIsolatedTarget(m, p)) {
						setGlow(p,GlowAPI.Color.WHITE);
					} else
					{
						//No glow.
						//setGlow(p,null);
						if (glowcolorlist.containsKey(p.getUniqueId())) {
							GlowAPI.setGlowing(m, null, p);
							glowcolorlist.remove(p.getUniqueId());
						}
						isImportantGlowEnemy=false;
					}
				//}
				}
				if (!GlowAPI.isGlowing(m, p) && glowcolorlist.containsKey(p.getUniqueId())) {
					//TwosideKeeper.log("Set glow of "+GenericFunctions.getDisplayName(m)+" to "+glowcolorlist.get(p.getUniqueId()), 0);
					GlowAPI.setGlowing(m, glowcolorlist.get(p.getUniqueId()), p);
				} else
				try {
					if (m!=null && p!=null && GlowAPI.isGlowing(m, p) && (GlowAPI.getGlowColor(m, p)==null || !glowcolorlist.get(p.getUniqueId()).equals(GlowAPI.getGlowColor(m, p)))) {
						if (GlowAPI.getGlowColor(m, p)==null) {
							GlowAPI.setGlowing(m, null, p);
						} else {
							GlowAPI.setGlowing(m, glowcolorlist.get(p.getUniqueId()), p);
						}
					}
				}catch (NullPointerException npe) {
					GlowAPI.setGlowing(m, false, p);
				}
			}
		}
	}
	
	public static void UpdateMobName(LivingEntity ent) {
		if (ent instanceof LivingEntity && !(ent instanceof Player)) {
			LivingEntity m = (LivingEntity)ent;
			LivingEntityStructure les = LivingEntityStructure.GetLivingEntityStructure(m);
			m.setCustomNameVisible(false);
			if (les.GetTarget()!=null && m.hasLineOfSight(les.GetTarget()) ||
					hasLineOfSightWithAPlayer(m)) {
				String actualName = les.getActualName();
				if (actualName.length()>0) {
					//m.setCustomName(ChatColor.stripColor(GenericFunctions.getDisplayName(m)));
					/*if (m.getCustomName().contains("Dangerous")) {
						m.setCustomName(ChatColor.DARK_AQUA+m.getCustomName());
					} else
					if (m.getCustomName().contains("Deadly")) {
						m.setCustomName(ChatColor.GOLD+m.getCustomName());
					} else
					if (m.getCustomName().contains("Hellfire")) {
						m.setCustomName(ChatColor.DARK_RED+m.getCustomName());
					} else {
						m.setCustomName(ChatColor.WHITE+m.getCustomName()+ChatColor.RESET+" ");
					}*/
					if (Buff.hasBuff(m, "DeathMark")) {
						GenericFunctions.RefreshBuffColor(m, Buff.getBuff(m, "DeathMark").getAmplifier());
					}
					CustomDamage.appendDebuffsToName(m);
					if (les.suffix_bar.length()>0 || les.prefix.length()>0) {
						m.setCustomNameVisible(true);
					}
					m.setCustomName(actualName);
				}
			}
		}
	}
	private static boolean hasLineOfSightWithAPlayer(LivingEntity ent) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (p.getWorld().equals(ent.getWorld()) && p.getLocation().distanceSquared(ent.getLocation())<=625 &&
					p.hasLineOfSight(ent)) {
				return true;
			}
		}
		return false;
	}
	//Either gets a monster structure that exists or creates a new one.
	public static LivingEntityStructure GetLivingEntityStructure(LivingEntity m) {
		if (m instanceof Player) {
			TwosideKeeper.log("ERROR!! We are trying to retrieve a LivingEntityStructure for a Player!", 0);
			DebugUtils.showStackTrace();
			return null;
		} else {
			UUID id = m.getUniqueId();
			if (TwosideKeeper.livingentitydata.containsKey(id)) {
				return TwosideKeeper.livingentitydata.get(id);
			} else {
				LivingEntityStructure newstruct = new LivingEntityStructure(m);
				TwosideKeeper.livingentitydata.put(id,newstruct);
				return TwosideKeeper.livingentitydata.get(id);
			}
		}
	}
	public String getUnloadedName() {
		StringBuilder sb = new StringBuilder(difficulty_modifier);
		sb.append(ChatColor.RESET);
		sb.append(";");
		sb.append(ChatColor.RESET);
		sb.append(base_name);
		return sb.toString();
	}
	public static void setCustomLivingEntityName(LivingEntity l, String name) {
		LivingEntityStructure les = LivingEntityStructure.GetLivingEntityStructure(l);
		les.base_name = name;
	}
	public static String getCustomLivingEntityName(LivingEntity l) {
		LivingEntityStructure les = LivingEntityStructure.GetLivingEntityStructure(l);
		return les.base_name;
	}
	public static void setChannelingBar(LivingEntity l, String barString) {
		LivingEntityStructure les = LivingEntityStructure.GetLivingEntityStructure(l);
		les.prefix = barString;
	}
	public static String getChannelingBar(LivingEntity l) {
		LivingEntityStructure les = LivingEntityStructure.GetLivingEntityStructure(l);
		return les.prefix;
	}
	
	public static int getAggroRating(LivingEntity l, Entity targetEntity) {
		LivingEntityStructure les = LivingEntityStructure.GetLivingEntityStructure(l);
		if (les.aggro_table.containsKey(targetEntity.getUniqueId())) {
			return les.aggro_table.get(targetEntity.getUniqueId());
		} else {
			return 0;
		}
	}
	
	public int getAggroRating(Entity targetEntity) {
		return getAggroRating(m, targetEntity);
	}
	
	/**
	 * May return null if there is no currently aggro'd target.
	 */
	public static LivingEntity getAggroTarget(LivingEntity l) {
		LivingEntityStructure les = LivingEntityStructure.GetLivingEntityStructure(l);
		int highest_aggroRating = 0;
		UUID bestAggroTarget = null;
		for (UUID ent : les.aggro_table.keySet()) {
			if (les.aggro_table.get(ent)>highest_aggroRating) {
				highest_aggroRating = les.aggro_table.get(ent);
				bestAggroTarget = ent;
			}
		}
		for (Entity e : l.getWorld().getEntities()) {
			if (e instanceof LivingEntity) {
				if (e.getUniqueId().equals(bestAggroTarget) && EntityUtils.isValidEntity(e)) {
					return (LivingEntity)e;
				}
			}
		}
		les.aggro_table.remove(bestAggroTarget);
		return null;
	}

	
	/**
	 * May return null if there is no currently aggro'd target.
	 */
	public LivingEntity getAggroTarget() {
		return getAggroTarget(m);
	}
	
	public void setAggro(LivingEntity target, int aggroValue) {
		if (isValidTarget(target)) {
			UUID key = target.getUniqueId();
			aggro_table.put(key, Math.max(aggroValue, 0));
		}
	}
	
	public void increaseAggro(LivingEntity target, int amt) {
		amt = getNewAggroBasedOnAggroMultipliers(target,amt);
		setAggro(target,getAggroRating(target)+amt);
	}
	private int getNewAggroBasedOnAggroMultipliers(LivingEntity target, int amt) {
		if (target instanceof Player) {
			Player p = (Player)target;
			amt += amt * ItemSet.GetTotalBaseAmount(p, ItemSet.SONGSTEEL);
			if (ItemSet.hasFullSet(p, ItemSet.PRIDE)) {
				return amt * ItemSet.getHighestTierInSet(p, ItemSet.PRIDE);
			}
		}
		return amt;
	}
	public void decreaseAggro(LivingEntity target, int amt) {
		increaseAggro(target,-amt);
	}
	/**
	 * Increases aggro of selected Target, multiplies all other aggro by multiplier.
	 */
	public void increaseAggroWhileMultiplyingAllOthers(LivingEntity target, int amt, double multiplier) {
		if (isValidTarget(target)) {
			for (UUID id : aggro_table.keySet()) {
				if (id!=target.getUniqueId()) {
					//setAggro(target,-(int)(getAggroRating(target)*multiplier));
					if (aggro_table.containsKey(id)) {
						aggro_table.put(id, (int)(aggro_table.get(id)*multiplier));
					}
				} else {
					increaseAggro(target,amt);
				}
			}
		}
	}
	
	public void increaseAggroWhileMultiplyingAllOthers(List<LivingEntity> targets, int amt, double multiplier) {
		List<UUID> uuid_list = new ArrayList<UUID>();
		for (LivingEntity ent : targets) {
			uuid_list.add(ent.getUniqueId());
		}
		if (isValidTarget(target)) {
			for (UUID id : aggro_table.keySet()) {
				if (!uuid_list.contains(id)) {
					//setAggro(target,-(int)(getAggroRating(target)*multiplier));
					if (aggro_table.containsKey(id)) {
						aggro_table.put(id, (int)(aggro_table.get(id)*multiplier));
					}
				} else {
					increaseAggro(target,amt);
				}
			}
		}
	}
	
	private boolean isValidTarget(LivingEntity target) {
		return target!=null && EntityUtils.isValidEntity(target) && target!=m;
	}
	
	public double getAggroPercentage(LivingEntity target) {
		int highestAggro = 0;
		if (!aggro_table.containsKey(target.getUniqueId())) {
			return 0.0;
		} else {
			for (UUID id : aggro_table.keySet()) {
				if (aggro_table.get(id)>highestAggro) {
					highestAggro = aggro_table.get(id);
				}
			}
			//TwosideKeeper.log("Aggro is "+aggro_table.get(target.getUniqueId())+" / "+highestAggro, 0);
			return ((double)aggro_table.get(target.getUniqueId()))/highestAggro;
		}
	}
	
	public String displayAggroTable() {
		StringBuilder sb = new StringBuilder("Aggro Table for Entity "+GenericFunctions.GetEntityDisplayName(m)+": \n");
		for (UUID id : aggro_table.keySet()) {
			int aggroRating = aggro_table.get(id);
			sb.append("  "+id+" : "+aggroRating+"\n");
		}
		return sb.toString();
	}
	public void UpdateAggroTarget() {
		LivingEntity target = getAggroTarget();
		if (target!=null) {
			if (m instanceof Monster) {
				Monster mm = (Monster)m;
				mm.setTarget(target);
				EntityTargetEvent ev = new EntityTargetEvent(m,target,TargetReason.CUSTOM);
				Bukkit.getPluginManager().callEvent(ev);
			}
			SetTarget(target);
			if (lastHit+40<=TwosideKeeper.getServerTickTime()) {
				decreaseAggro(target,getAggroRating(target)/2);
				//TwosideKeeper.log("Decreased aggro due to no attacking to "+getAggroRating(target), 0);
			}
		}
		
		if (target!=null && !EntityUtils.isValidEntity(target)) {
			aggro_table.remove(target.getUniqueId());
		}		
	}
	
	public static boolean isFriendly(LivingEntity ent1, LivingEntity ent2) {
		if (!(ent1 instanceof Player) &&
				!(ent2 instanceof Player)) {
			LivingEntityStructure les1 = LivingEntityStructure.GetLivingEntityStructure(ent1);
			LivingEntityStructure les2 = LivingEntityStructure.GetLivingEntityStructure(ent2);
			if (les1.isPet && les2.isPet) {
				if (les1.petOwner!=null && les2.petOwner!=null) {
					return PVP.isFriendly(les1.petOwner, les2.petOwner);
				} else {
					return true;
				}
			} else {
				return false;
			}
		} else
		if (!(ent1 instanceof Player) ^
				!(ent2 instanceof Player)) {
			if (ent1 instanceof Player) { //ent2 is the pet.
				LivingEntityStructure les = LivingEntityStructure.GetLivingEntityStructure(ent2);
				if (les.isPet && les.petOwner!=null) {
					return PVP.isFriendly(les.petOwner,(Player)ent1);
				} else {
					return false;
				}
			} else { //ent1 is the pet.
				LivingEntityStructure les = LivingEntityStructure.GetLivingEntityStructure(ent1);
				if (les.isPet && les.petOwner!=null) {
					return PVP.isFriendly(les.petOwner,(Player)ent2);
				} else {
					return false;
				}
			}
		} else
		{
			return PVP.isFriendly((Player)ent1,(Player)ent2);
		}
	}
}
