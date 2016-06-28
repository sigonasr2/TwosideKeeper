package sig.plugin.TwosideKeeper.HelperStructures;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import com.google.common.collect.Iterables;

public class ItemCube {
	public static boolean isSomeoneViewingItemCube(int id, Player checker) {
		for (int i=0;i<Bukkit.getOnlinePlayers().size();i++) {
			Player p = Iterables.get(Bukkit.getOnlinePlayers(), i);
			if (!p.equals(checker)) {
				if (p.getOpenInventory()!=null && p.getOpenInventory().getTitle().contains("Item Cube #")) {
					//This is an item cube. Check if it's the same number.
					int ider = Integer.parseInt(p.getOpenInventory().getTitle().split("#")[1]);
					if (ider==id) {
						return true;
					}
				}
			}
		}
		return false; //Didn't find anyone, oh well..
	}
	public static void displayErrorMessage(Player p) {
		p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 0.6f, 4.0f);
		p.sendMessage("Someone is currently using this Item Cube! Please wait for them to finish.");
	}
}
