package sig.plugin.TwosideKeeper;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.inventivetalent.glow.GlowAPI;

import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;

public class MonsterStructure {
	public LivingEntity target;
	public String original_name="";
	public Monster m;
	public boolean isLeader=false;
	public boolean isElite=false;
	public HashMap<UUID,Long> hitlist = new HashMap<UUID,Long>();
	public HashMap<Player,GlowAPI.Color> glowcolorlist = new HashMap<Player,GlowAPI.Color>();
	
	public MonsterStructure(Monster m) {
		target=null;
		original_name="";
		this.m=m;
	}
	public MonsterStructure(Monster m, LivingEntity target) {
		this.target=target;
		original_name="";
		this.m=m;
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
			if (getLeader() || GenericFunctions.isBossMonster(m)) {
				setGlow(p,GlowAPI.Color.DARK_RED);
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
			if (GenericFunctions.isIsolatedTarget(m, p)) {
				setGlow(p,GlowAPI.Color.WHITE);
			} else {
				//No glow.
				setGlow(p,null);
			}
		}
	}
	
	//Either gets a monster structure that exists or creates a new one.
	public static MonsterStructure getMonsterStructure(Monster m) {
		UUID id = m.getUniqueId();
		if (TwosideKeeper.monsterdata.containsKey(id)) {
			return TwosideKeeper.monsterdata.get(id);
		} else {
			MonsterStructure newstruct = new MonsterStructure(m);
			TwosideKeeper.monsterdata.put(id,newstruct);
			return TwosideKeeper.monsterdata.get(id);
		}
	}
}
