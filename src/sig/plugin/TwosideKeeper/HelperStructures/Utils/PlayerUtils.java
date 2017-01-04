package sig.plugin.TwosideKeeper.HelperStructures.Utils;

import org.bukkit.entity.Player;

import sig.plugin.TwosideKeeper.PlayerStructure;

public class PlayerUtils {
	public static boolean PlayerIsInCombat(Player p) {
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		return (pd.target!=null && pd.target.isValid() && !pd.target.isDead() && pd.target.getLocation().getWorld().equals(p.getWorld()) && pd.target.getLocation().distanceSquared(p.getLocation())<256);
	}
}
