package sig.plugin.TwosideKeeper;

import org.bukkit.entity.Monster;

public class HellfireSpider {
	Monster m;
	
	public HellfireSpider(Monster m) {
		this.m=m;
	}
	
	public Monster GetSpider() {
		return m;
	}
	public boolean isAlive() {
		return !m.isDead();
	}
	public boolean hasTarget() {
		return (m.getTarget()!=null)?true:false;
	}
	
}
