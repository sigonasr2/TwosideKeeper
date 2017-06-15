package sig.plugin.TwosideKeeper;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_9_R1.EnumParticle;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;

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
	
	public static void sendCooldownPacket(Player p, ItemStack item, int duration) {
		for (int i=0;i<2;i++) {
			aPlugin.API.sendCooldownPacket(p, item, duration);
		}
		p.getEquipment().setItemInMainHand(GenericFunctions.UpdateItemLore(p.getEquipment().getItemInMainHand()));
	}
	
	public static void sendCooldownPacket(Player p, int id, int duration) {
		for (int i=0;i<2;i++) {
			aPlugin.API.sendCooldownPacket(p, id, duration);
		}
		p.getEquipment().setItemInMainHand(GenericFunctions.UpdateItemLore(p.getEquipment().getItemInMainHand()));
	}
	
	public static void sendCooldownPacket(Player p, Material type, int duration) {
		for (int i=0;i<2;i++) {
			aPlugin.API.sendCooldownPacket(p, type, duration);
		}
		p.getEquipment().setItemInMainHand(GenericFunctions.UpdateItemLore(p.getEquipment().getItemInMainHand()));
	}
}
