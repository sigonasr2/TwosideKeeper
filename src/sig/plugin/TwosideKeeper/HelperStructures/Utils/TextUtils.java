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
}
