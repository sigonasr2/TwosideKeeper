package sig.plugin.TwosideKeeper.Boss;

import org.bukkit.Bukkit;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import sig.plugin.TwosideKeeper.EliteMonster;
import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;

public class SendMiningFatigueToAllNearbyElderGuardians extends BukkitRunnable {

	@Override
	public void run() {
		for (EliteMonster em : TwosideKeeper.elitemonsters) {
			if (em.getMonster() instanceof Guardian) {
				//Apply Mining Fatigue 100 to all players.
				for (Player p : Bukkit.getOnlinePlayers()) {
					if (!p.hasPotionEffect(PotionEffectType.SLOW_DIGGING) || GenericFunctions.getPotionEffectDuration(PotionEffectType.SLOW_DIGGING,p)<1200 ||
							GenericFunctions.getPotionEffectLevel(PotionEffectType.SLOW_DIGGING, p)<20) {
						//p.getWorld().playEffect(p.getLocation(), Effect., arg2);
						GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.SLOW_DIGGING, 6000, 20, p, true);
					}
				}
			}
		}
	}

}
