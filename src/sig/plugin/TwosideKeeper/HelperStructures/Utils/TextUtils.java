package sig.plugin.TwosideKeeper.HelperStructures.Utils;

import org.bukkit.ChatColor;

public class TextUtils {

	public static ChatColor RandomColor() {
		ChatColor[] colors = {ChatColor.AQUA,
				ChatColor.BLACK,
				ChatColor.BLUE,ChatColor.DARK_AQUA,
				ChatColor.DARK_BLUE,ChatColor.DARK_GRAY,
				ChatColor.DARK_GREEN,ChatColor.DARK_PURPLE,
				ChatColor.DARK_RED,ChatColor.GOLD,
				ChatColor.GRAY,ChatColor.GREEN,
				ChatColor.LIGHT_PURPLE,ChatColor.RED,
				ChatColor.WHITE,ChatColor.YELLOW};
		
		return colors[((int)(Math.random()*colors.length))];
	}


	public static ChatColor RandomDarkColor() {
		ChatColor[] choices = new ChatColor[]{ChatColor.DARK_AQUA,ChatColor.DARK_BLUE,ChatColor.DARK_GRAY,ChatColor.DARK_GREEN,ChatColor.DARK_PURPLE,ChatColor.DARK_RED,ChatColor.GOLD};
		return choices[(int)(Math.random()*choices.length)];
	}
	
	public static ChatColor GetColorBasedOnPercent(double pct) {
		if (pct>0.75) {
			return ChatColor.DARK_GREEN;
		} else
		if (pct>0.5) {
			return ChatColor.GREEN;
		} else
		if (pct>0.33) {
			return ChatColor.YELLOW;
		} else
		if (pct>0.25) {
			return ChatColor.GOLD;
		} else
		if (pct>0.1) {
			return ChatColor.RED;
		} else
		{
			return ChatColor.DARK_RED;
		}
	}
}
