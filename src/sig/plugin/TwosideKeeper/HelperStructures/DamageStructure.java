package sig.plugin.TwosideKeeper.HelperStructures;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import sig.plugin.TwosideKeeper.CustomDamage;

public class DamageStructure {
	double damage;
	Entity damager;
	LivingEntity target;
	ItemStack weapon;
	String reason;
	int flags;
	
	public DamageStructure(double damage, Entity damager, LivingEntity target, ItemStack weapon, String reason, int flags) {
		this.damage=damage;
		this.damager=damager;
		this.target=target;
		this.weapon=weapon;
		this.reason=reason;
		this.flags=flags;
	}
	
	public void run() {
		CustomDamage.ApplyDamage(damage, damager, target, weapon, reason, flags);
	}
}
