package sig.plugin.TwosideKeeper;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import sig.plugin.TwosideKeeper.HelperStructures.Common.BlockModifyQueue;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.Classes.ColoredParticle;

public class runServerTick implements Runnable{
	final int queuespd = 3;

	@Override
	public void run() {
		for (int i=queuespd;i>0;i--) {
			if (TwosideKeeper.blockqueue.size()>0) {
				BlockModifyQueue bmq = TwosideKeeper.blockqueue.remove(0);
				bmq.run();
			}
		}
		runServerHeartbeat.resetDamageQueue();
		/*if (Bukkit.getPlayer("sigonasr2")!=null) {
			Player p = Bukkit.getPlayer("sigonasr2");

			if (p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType()!=Material.AIR &&
					!p.getLocation().getBlock().getRelative(BlockFace.DOWN).isLiquid()) {
				PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
				pd.customtitle.modifyLargeCenterTitle(ChatColor.RED+"A", 5);
			}
		}*/
		/*
		/*if (Bukkit.getPlayer("sigonasr2")!=null) {
		 for (int i=0;i<200;i++) {
			ColoredParticle.RED_DUST.send(p.getEyeLocation().add(
				p.getLocation().getDirection()).add(0,-0.05*i,0)
				, 20, 0, 0, 0);
				}
				
		}*/
	}
	
}
