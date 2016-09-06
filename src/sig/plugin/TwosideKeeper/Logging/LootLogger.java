package sig.plugin.TwosideKeeper.Logging;

import java.text.DecimalFormat;

import org.bukkit.ChatColor;

public class LootLogger {
	int common_loot = 0;
	int rare_loot = 0;
	int legendary_loot = 0;
	int total_rolls = 0;
	public LootLogger() {
		
	}
	public void AddLootRoll() {
		total_rolls++;
	}
	public void AddCommonLoot() {
		common_loot++;
	}
	public void AddRareLoot() {
		rare_loot++;
	}
	public void AddLegendaryLoot() {
		legendary_loot++;
	}
	public String GenerateReport() {
		DecimalFormat df = new DecimalFormat("0.0");
		if (total_rolls>0) {
			return "Total Rolls: "+ChatColor.AQUA+(total_rolls)+"\n"+ChatColor.WHITE
					+ "Total Bonus Drops: "+ChatColor.YELLOW+(common_loot)+ChatColor.GREEN+" ("+df.format(((double)common_loot/(total_rolls))*100)+"%)\n"+ChatColor.WHITE
					/*+ "Total Rare Rolls: "+ChatColor.YELLOW+(rare_loot)+ChatColor.GREEN+" ("+df.format(((double)rare_loot/(total_rolls))*100)+"%)\n"+ChatColor.WHITE
					+ "Total Legendary Rolls: "+ChatColor.YELLOW+(legendary_loot)+ChatColor.GREEN+" ("+df.format(((double)legendary_loot/(total_rolls))*100)+"%)"*/;
		} else {
			return "Not enough data yet!";
		}
	}
}
