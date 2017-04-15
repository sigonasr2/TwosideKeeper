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

public class CloudRunnableRemoveLabel implements Runnable{
	AreaEffectCloud aec;
	Location loc;
	String str;
	double spd;
	int duration;
	
	public CloudRunnableRemoveLabel(Location loc,String str,double spd,int duration) {
		this.loc=loc;
		this.str=str;
		this.spd=spd;
		this.duration=duration;
	}

	@Override
	public void run() {
		aec = EntityUtils.CreateOverlayText(loc, str);
		Bukkit.getScheduler().runTaskLater(TwosideKeeper.plugin, new CloudRunnable(aec,spd,duration), 1);
	}
	
}
