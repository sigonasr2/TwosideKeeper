package sig.plugin.TwosideKeeper;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;

public class MonsterStructure {
	public LivingEntity target;
	public String original_name;
	public Monster m;
	public boolean isLeader;
	
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
}
