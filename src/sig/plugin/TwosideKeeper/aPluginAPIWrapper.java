package sig.plugin.TwosideKeeper;

import org.bukkit.Location;

import net.minecraft.server.v1_9_R1.EnumParticle;

public class aPluginAPIWrapper {
	public static void sendParticle(Location loc, EnumParticle particle, float dx, float dy, float dz, float v, int particleCount) {
		utils.NMSUtils.sendParticle(loc,particle,dx,dy,dz,v,particleCount);
	}
}
