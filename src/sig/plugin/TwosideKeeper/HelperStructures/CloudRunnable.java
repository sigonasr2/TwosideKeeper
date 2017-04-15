package sig.plugin.TwosideKeeper.HelperStructures;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.EnderSignal;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;

import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.EntityUtils;

public class CloudRunnable implements Runnable{
	AreaEffectCloud aec;
	double spd;
	int duration;
	
	public CloudRunnable(AreaEffectCloud aec, double spd, int duration) {
		this.aec=aec;
		this.spd=spd;
		this.duration=duration;
	}

	@Override
	public void run() {
		if (aec.isValid()) {
			duration--;
			if (duration>0) {
				Bukkit.getScheduler().runTaskLater(TwosideKeeper.plugin, new CloudRunnableRemoveLabel(aec.getLocation().add(0,spd,0).clone(),aec.getCustomName(),spd,duration), 2);
			}
			aec.teleport(aec.getLocation().add(0,spd,0));
			aec.remove();
		}
	}
	
}
