package sig.plugin.TwosideKeeper.HelperStructures.Utils;

import org.bukkit.entity.Player;

import sig.plugin.TwosideKeeper.PlayerStructure;
import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;

public class PlayerUtils {
	public static boolean PlayerIsInCombat(Player p) {
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		return (pd.target!=null && pd.target.isValid() && !pd.target.isDead() && pd.target.getLocation().getWorld().equals(p.getWorld()) && pd.target.getLocation().distanceSquared(p.getLocation())<256);
	}
	public static boolean cooldownAvailable(long basetimer, int cooldown, Player p) {
		return (basetimer+GenericFunctions.GetModifiedCooldown(cooldown, p)<=TwosideKeeper.getServerTickTime());
	}
	public static long cooldownTimeRemaining(long basetimer, int cooldown, Player p) {
		return (basetimer+GenericFunctions.GetModifiedCooldown(cooldown, p))-TwosideKeeper.getServerTickTime();
	}
}
