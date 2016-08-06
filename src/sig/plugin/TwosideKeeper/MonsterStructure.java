package sig.plugin.TwosideKeeper;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;

public class MonsterStructure {
	public LivingEntity target;
	public String original_name="";
	public Monster m;
	public boolean isLeader=false;
	public boolean isElite=false;
	public HashMap<UUID,Long> hitlist = new HashMap<UUID,Long>();
	
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
