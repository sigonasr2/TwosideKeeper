package sig.plugin.TwosideKeeper;

import org.bukkit.entity.LivingEntity;

public class MonsterStructure {
	public LivingEntity target;
	public String original_name;
	
	public MonsterStructure() {
		target=null;
		original_name="";
	}
	public MonsterStructure(LivingEntity target) {
		this.target=target;
		original_name="";
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
}
