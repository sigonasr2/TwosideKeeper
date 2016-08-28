package sig.plugin.TwosideKeeper.HelperStructures.Common;

import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;

import sig.plugin.TwosideKeeper.TwosideKeeper;

public enum DamageType {
	PLAYERVSMOB,
	MOBVSPLAYER,
	MOBPROJECTILEVSPLAYER,
	MOBPROJECTILEVSMOB,
	MOBVSMOB,
	SPELLVSMOB,
	PLAYERPROJECTILEVSMOB,
	OTHER;
	
	public static DamageType DetectType(LivingEntity target, Entity damager) {
		if ((damager instanceof Player) &&
				(target instanceof LivingEntity)) {
			return PLAYERVSMOB;
		} else 
		if ((target instanceof Player) &&
				(damager instanceof LivingEntity)) {
			return MOBVSPLAYER;
		} else
		if ((damager instanceof Projectile) &&
				((Projectile)damager).getShooter()!=null &&
				(((Projectile)damager).getShooter() instanceof LivingEntity) &&
				(target instanceof Player)) {
			return MOBPROJECTILEVSPLAYER;
		} else
		if ((damager instanceof Projectile) &&
				((Projectile)damager).getShooter()!=null &&
				(((Projectile)damager).getShooter() instanceof Player) &&
				(target instanceof LivingEntity)) {
			return PLAYERPROJECTILEVSMOB;
		} else
		if ((damager instanceof Projectile) &&
				((Projectile)damager).getShooter()!=null &&
				(((Projectile)damager).getShooter() instanceof LivingEntity) &&
				(target instanceof LivingEntity)) {
			return MOBPROJECTILEVSMOB;
		} else
			if ((target instanceof LivingEntity) &&
					(damager instanceof LivingEntity)) {
			return MOBVSMOB;
		} else
			if ((target instanceof LivingEntity) &&
					(damager instanceof AreaEffectCloud)) {
			return SPELLVSMOB;
		} else
		{
			TwosideKeeper.log("Detected a OTHER event. Target was "+target.getType().name()+" and damager was "+damager.getType().name(), 2);
			return OTHER;
		}
	}
}
