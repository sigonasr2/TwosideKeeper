package sig.plugin.TwosideKeeper.Logging;

import java.text.DecimalFormat;

import org.bukkit.ChatColor;

public class BowModeLogger {
	int mode_snipe = 0;
	int mode_close = 0;
	int mode_debilitation = 0;
	public BowModeLogger() {
		
	}
	public void AddSnipeMode() {
		mode_snipe++;
	}
	public void AddCloseMode() {
		mode_close++;
	}
	public void AddDebilitationMode() {
		mode_debilitation++;
	}
	public String GenerateReport() {
		DecimalFormat df = new DecimalFormat("0.0");
		int sum = mode_snipe+mode_close+mode_debilitation ;
		if (sum>0) {
			return "Total Kills in Sniper Mode: "+ChatColor.YELLOW+(mode_snipe)+ChatColor.GREEN+" ("+df.format(((double)mode_snipe/(sum))*100)+"%)\n"+ChatColor.WHITE
					+ "Total Kills in Close Range Mode: "+ChatColor.YELLOW+(mode_close)+ChatColor.GREEN+" ("+df.format(((double)mode_close/(sum))*100)+"%)\n"+ChatColor.WHITE
					+ "Total Kills in Debilitation Mode: "+ChatColor.YELLOW+(mode_debilitation)+ChatColor.GREEN+" ("+df.format(((double)mode_debilitation/(sum))*100)+"%)";
		} else {
			return "Not enough data yet!";
		}
	}
}
