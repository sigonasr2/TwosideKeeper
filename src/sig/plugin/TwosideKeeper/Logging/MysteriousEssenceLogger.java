package sig.plugin.TwosideKeeper.Logging;

import java.text.DecimalFormat;

import org.bukkit.ChatColor;

public class MysteriousEssenceLogger {
	int essence_hellfire = 0;
	int essence_other = 0;
	int essence_despawned = 0;
	public MysteriousEssenceLogger() {
		
	}
	public void AddHellfireEssence() {
		essence_hellfire++;
	}
	public void AddGeneralEssence() {
		essence_other++;
	}
	public void AddEssenceDespawn() {
		essence_despawned++;
	}
	public String GenerateReport() {
		DecimalFormat df = new DecimalFormat("0.0");
		if (essence_hellfire+essence_other>0) {
			return "Total Essences Spawned: "+ChatColor.YELLOW+(essence_hellfire+essence_other)+ChatColor.WHITE+"\n"+
					"Total Essences from Hellfires: "+ChatColor.YELLOW+essence_hellfire+ChatColor.RED+" ("+df.format(((double)essence_hellfire/(essence_other+essence_hellfire))*100)+"%)\n"+ChatColor.WHITE
					+ "Total Essences Despawned: "+ChatColor.YELLOW+essence_despawned+ChatColor.BLUE+" ("+df.format(((double)essence_despawned/(essence_other+essence_hellfire))*100)+"%)";
		} else {
			return "Not enough data yet!";
		}
	}
}
