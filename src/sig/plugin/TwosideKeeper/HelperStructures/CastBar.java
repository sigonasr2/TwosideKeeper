package sig.plugin.TwosideKeeper.HelperStructures;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import sig.plugin.TwosideKeeper.HelperStructures.Common.CastBarItem;

public class CastBar {
	List<CastBarItem> castbaritems = new ArrayList<CastBarItem>();
	
	public CastBar(CastBarItem...items) {
		castbaritems = Arrays.asList(items);
		while (castbaritems.size()<9) {
			castbaritems.add(new CastBarItem(Material.AIR));
		}
	}
	
	public void run(Player p) {
		aPlugin.API.setSlot(p, 9);
		for (int i=0;i<9;i++) {
			aPlugin.API.setItem(p, p.getOpenInventory(), i, castbaritems.get(i).getItemStack());
		}
	}
}
