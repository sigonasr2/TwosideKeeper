package sig.plugin.TwosideKeeper.HelperStructures;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EnderSignal;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;

import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.EntityUtils;

public class DamageLabel{
	ArmorStand aec;
	double spd;
	int duration;
	Location loc;
	
	public DamageLabel(Location loc, ArmorStand aec, double spd, int duration) {
		this.aec=aec;
		this.spd=spd;
		this.duration=duration;
		this.loc=loc;
	}

	public boolean run() {
		if (aec.isValid()) {
			duration--;
			/*if (duration>0) {
				Bukkit.getScheduler().runTaskLater(TwosideKeeper.plugin, new CloudRunnableRemoveLabel(aec.getLocation().add(0,spd,0).clone(),aec.getCustomName(),spd,duration), 1);
			}*/
			if (loc!=null) {
				aec.teleport(loc.add(0,spd,0));
			} else {
				aec.teleport(aec.getLocation().add(0,spd,0));
			}
			if (duration<0) {
				aec.remove();
				return false;
			}
			return true;
		} else {
			return false;
		}
	}
	
	public void cleanup() {
		aec.remove();
	}
}
