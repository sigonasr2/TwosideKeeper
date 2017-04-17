package sig.plugin.TwosideKeeper;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.inventivetalent.glow.GlowAPI;

import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.DebugUtils;

public class LivingEntityStructure {
	public LivingEntity target;
	public String original_name="";
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
	
	public LivingEntityStructure(LivingEntity m) {
		target=null;
		original_name=GetOriginalName(m);
		//TwosideKeeper.log("Original name is "+original_name, 0);
		this.m=m;
		this.original_movespd = m.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue();
	}
	public LivingEntityStructure(LivingEntity m, LivingEntity target) {
		this.target=target;
		original_name=GetOriginalName(m);
		this.m=m;
		this.original_movespd = m.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue();
	}
	public LivingEntityStructure(LivingEntity m, LivingEntity target, BossMonster bm) {
		this.target=target;
		original_name=bm.getName();
		this.m=m;
		this.bm=bm;
		this.original_movespd = m.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue();
	}
	
	private String GetOriginalName(LivingEntity m) {
		if (m.getCustomName()!=null) {
			return m.getCustomName();
		} else {
			return GenericFunctions.CapitalizeFirstLetters(m.getType().name().replace("_", " "));
		}
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
	}
	public void SetElite(boolean elite) {	
		this.isElite=elite;
	}
	
	public boolean hasOriginalName() {
		return !this.original_name.equalsIgnoreCase("");
	}
	
	public String getOriginalName() {
		if (hasOriginalName()) {
			return this.original_name;
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
		glowcolorlist.put(p.getUniqueId(), col);
	}
	
	public void setGlobalGlow(GlowAPI.Color col) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			glowcolorlist.put(p.getUniqueId(), col);
		}
	}
	
	public void UpdateGlow() {
		//Updates the glow color for all players. We base it on default statuses here. CALL THIS INSTEAD OF
		// SETTING THE GLOW DIRECTLY ANYMORE!
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (GenericFunctions.isSuppressed(m)) {
				setGlow(p,GlowAPI.Color.BLACK);
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
				setGlow(p,GlowAPI.Color.DARK_RED);
			} else
			if (GenericFunctions.isIsolatedTarget(m, p)) {
				setGlow(p,GlowAPI.Color.WHITE);
			} else {
				//No glow.
				//setGlow(p,null);
				if (glowcolorlist.containsKey(p.getUniqueId())) {
					GlowAPI.setGlowing(m, null, p);
					glowcolorlist.remove(p.getUniqueId());
				}
			}
			if (!GlowAPI.isGlowing(m, p) && glowcolorlist.containsKey(p.getUniqueId())) {
				GlowAPI.setGlowing(m, glowcolorlist.get(p.getUniqueId()), p);
			} else
			if (GlowAPI.isGlowing(m, p) && !glowcolorlist.get(p.getUniqueId()).equals(GlowAPI.getGlowColor(m, p))) {
				GlowAPI.setGlowing(m, glowcolorlist.get(p.getUniqueId()), p);
			}
		}
	}
	
	public static void UpdateMobName(LivingEntity ent) {
		if (ent instanceof LivingEntity) {
			LivingEntity m = (LivingEntity)ent;
			m.setCustomNameVisible(false);
			if (m.getCustomName()!=null) {
				m.setCustomName(ChatColor.stripColor(GenericFunctions.getDisplayName(m)));
				if (m.getCustomName().contains("Dangerous")) {
					m.setCustomName(ChatColor.DARK_AQUA+m.getCustomName());
				}
				if (m.getCustomName().contains("Deadly")) {
					m.setCustomName(ChatColor.GOLD+m.getCustomName());
				}
				if (m.getCustomName().contains("Hellfire")) {
					m.setCustomName(ChatColor.DARK_RED+m.getCustomName());
				}
				m.setCustomName(ChatColor.DARK_RED+m.getCustomName()+ChatColor.RESET+" ");
				if (Buff.hasBuff(m, "DeathMark")) {
					GenericFunctions.RefreshBuffColor(m, Buff.getBuff(m, "DeathMark").getAmplifier());
				}
				CustomDamage.appendDebuffsToName(m);
				if (m.getCustomName().contains("  ")) {
					m.setCustomNameVisible(true);
				}
			}
		}
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
}
