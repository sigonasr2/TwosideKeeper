package sig.plugin.TwosideKeeper;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_9_R1.EnumParticle;

public class aPluginAPIWrapper {
	public static void sendParticle(Location loc, EnumParticle particle, float dx, float dy, float dz, float v, int particleCount) {
		utils.NMSUtils.sendParticle(loc,particle,dx,dy,dz,v,particleCount);
	}
	
	public static boolean isAFK(Player p) {
		return aPlugin.API.isAFK(p) || !playerIsActive(p);
	}

	public static boolean playerIsActive(Player p) {
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		if (pd.lastActiveActivity+20<TwosideKeeper.getServerTickTime() && (pd.lastLocationChange+(20*pd.afkLength)<TwosideKeeper.getServerTickTime() || pd.tooConsistentAdjustments)) {
			if (!pd.isAFKState) {
				pd.isAFKState=true;
				TwosideKeeper.log(">>Player "+p.getName()+" has been detected as AFK.", 2);
			}
			return false;
		}
		if (pd.isAFKState) {
			pd.isAFKState=false;
			pd.gracePeriod=3;
			TwosideKeeper.log(">>Player "+p.getName()+" is no longer AFK.", 2);
		}
		return true;
	}
}
