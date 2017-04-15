package sig.plugin.TwosideKeeper.HelperStructures.Utils;

import org.bukkit.ChatColor;

public enum IndicatorType {
	REGULAR(ChatColor.RED),
	CRIT(ChatColor.LIGHT_PURPLE),
	DOT(ChatColor.YELLOW),
	HEAL(ChatColor.GREEN);
	
	ChatColor col;
	
	IndicatorType(ChatColor col) {
		this.col=col;
	}

	public ChatColor getColor() {
		return col;
	}
}
