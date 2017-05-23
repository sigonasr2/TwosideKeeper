package sig.plugin.TwosideKeeper.HelperStructures;

import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.Classes.MixedDamage;

public class Spell {
	String name;
	int[] cast_time;
	int[] cooldown_time;
	MixedDamage[] damage; 
	long last_casted_spell;
	
	public Spell(String name, int[] cast_time, int[] cooldowns) {
		this.name=name;
		this.cast_time=cast_time;
		this.cooldown_time=cooldowns;
		this.damage=null;
		this.last_casted_spell=TwosideKeeper.getServerTickTime();
	}
	
	public Spell(String name, int[] cast_time, int[] cooldowns, MixedDamage[] damage) {
		this.name=name;
		this.cast_time=cast_time;
		this.cooldown_time=cooldowns;
		this.damage=damage;
		this.last_casted_spell=TwosideKeeper.getServerTickTime();
	}

	public String getName() {
		return name;
	}

	public int[] getCastTimes() {
		return cast_time;
	}

	public int[] getCooldowns() {
		return cooldown_time;
	}
	
	public MixedDamage[] getDamageValues() {
		return damage;
	}
	
	public long getLastCastedTime() {
		return last_casted_spell;
	}
	
	public void setLastCastedTime(long time) {
		last_casted_spell = time;
	}
}
