package sig.plugin.TwosideKeeper.HelperStructures.Utils;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import sig.plugin.TwosideKeeper.TwosideKeeper;

public class ArtifactUtils {
	public static int getArtifactTier(ItemStack item) {
		String str = ChatColor.stripColor(ItemUtils.GetLoreLineContainingSubstring(item, ChatColor.GOLD+""+ChatColor.BOLD+"T"));
		TwosideKeeper.log("String is "+str+". Parse 1 is "+str.replace("T", "")+". Parse 2 is "+str.replace("T", "").split(" ")[0], 5);
		return Integer.parseInt(str.replace("T", "").split(" ")[0]);
	}
	public static ItemStack setArtifactTier(ItemStack item, int tier) {
		item=ItemUtils.ModifyLoreLineContainingSubstring(item, ChatColor.GOLD+""+ChatColor.BOLD+"T", ChatColor.GOLD+""+ChatColor.BOLD+"T"+tier+" Artifact");
		return item;
	}
	public static ItemStack addArtifactTier(ItemStack item, int incr) {
		item=setArtifactTier(item,getArtifactTier(item)+incr);
		return item;
	}
}
