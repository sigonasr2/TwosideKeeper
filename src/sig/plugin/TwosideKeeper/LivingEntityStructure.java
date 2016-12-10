package sig.plugin.TwosideKeeper;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.inventivetalent.glow.GlowAPI;

import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;

public class LivingEntityStructure {
	public LivingEntity target;
	public String original_name="";
	public LivingEntity m;
	public boolean isLeader=false;
	public boolean isElite=false;
	public double original_movespd = 0.0d;
	public HashMap<UUID,Long> hitlist = new HashMap<UUID,Long>();
	public HashMap<Player,GlowAPI.Color> glowcolorlist = new HashMap<Player,GlowAPI.Color>();
	//public long lastSpiderBallThrow = 0;
	public BossMonster bm = null;
	
	public LivingEntityStructure(LivingEntity m) {
		target=null;
		original_name="";
		this.m=m;
		this.original_movespd = m.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue();
	}
	public LivingEntityStructure(LivingEntity m, LivingEntity target) {
		this.target=target;
		original_name="";
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
		glowcolorlist.put(p, col);
		GlowAPI.setGlowing(m, col, p);
	}
	
	public void setGlobalGlow(GlowAPI.Color col) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			glowcolorlist.put(p, col);
			GlowAPI.setGlowing(m, col, p);
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
				setGlow(p,null);
			}
		}
	}
	
	//Either gets a monster structure that exists or creates a new one.
	public static LivingEntityStructure getLivingEntityStructure(LivingEntity m2) {
		UUID id = m2.getUniqueId();
		if (TwosideKeeper.livingentitydata.containsKey(id)) {
			return TwosideKeeper.livingentitydata.get(id);
		} else {
			LivingEntityStructure newstruct = new LivingEntityStructure(m2);
			TwosideKeeper.livingentitydata.put(id,newstruct);
			return TwosideKeeper.livingentitydata.get(id);
		}
	}
}
