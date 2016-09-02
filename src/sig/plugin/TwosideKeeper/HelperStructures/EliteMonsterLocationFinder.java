package sig.plugin.TwosideKeeper.HelperStructures;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;

import sig.plugin.TwosideKeeper.MonsterController;
import sig.plugin.TwosideKeeper.PlayerStructure;
import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;

public class EliteMonsterLocationFinder implements Runnable{
	
	Player p = null;
	String name = "";
	
	public EliteMonsterLocationFinder(Player p, String name) {
		this.p=p;
		this.name=name;
	}

	@Override
	public void run() {
		TwosideKeeper.ELITE_LOCATION = GenericFunctions.defineNewEliteLocation();
		if (TwosideKeeper.ELITE_LOCATION==null) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(TwosideKeeper.plugin, new EliteMonsterLocationFinder(p,name), 20l);
			if (p!=null) {
				PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
				if ((pd.lastcompassnotification+(20*10))<TwosideKeeper.getServerTickTime()) {
					pd.lastcompassnotification=TwosideKeeper.getServerTickTime();
					p.sendMessage("The "+name+ChatColor.WHITE+" is still searching...");
				} else {
					pd.lastrightclick=TwosideKeeper.getServerTickTime();
				}
			}
		} else {
			if (p!=null) {
				p.sendMessage("The "+name+ChatColor.WHITE+" is now properly calibrated!");
				p.sendMessage(ChatColor.ITALIC+"  Good luck on your adventure!");
				p.setCompassTarget(TwosideKeeper.ELITE_LOCATION);
			}
			Monster m = (Monster)TwosideKeeper.ELITE_LOCATION.getWorld().spawnEntity(TwosideKeeper.ELITE_LOCATION, EntityType.ZOMBIE);
			MonsterController.convertMonster(m, MonsterDifficulty.ELITE);
		}
	}

}
