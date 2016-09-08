package sig.plugin.TwosideKeeper.HelperStructures;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DeathStructure {

	//public List<ItemStack> deathinventory;
	public Location deathloc;
	public String p;
	
	public DeathStructure(List<ItemStack> di, Location dl, Player p) {
		//this.deathinventory=di;
		this.deathloc=dl;
		this.p=p.getName();
	}
	
	public String toString() {
		return "Belongs to Player "+p+". Death location is "+deathloc.toString();
	}
}
