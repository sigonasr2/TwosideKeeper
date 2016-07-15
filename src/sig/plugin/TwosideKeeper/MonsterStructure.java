package sig.plugin.TwosideKeeper;

import org.bukkit.entity.LivingEntity;

public class MonsterStructure {
	public LivingEntity target;
	
	public MonsterStructure() {
		target=null;
	}
	public MonsterStructure(LivingEntity target) {
		this.target=target;
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
}
