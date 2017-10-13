package sig.plugin.TwosideKeeper.HelperStructures;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

import sig.plugin.TwosideKeeper.HelperStructures.Common.ArmorStandProperties;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.EntityUtils;

public class ArmorStandLinker {
	ArmorStandProperties myModel;
	ArmorStand ent;
	Location loc;
	
	public ArmorStandLinker(Location loc) {
		ent = (ArmorStand)loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
		this.loc=loc;
	}
	
	public void run() {
		if (!EntityUtils.isValidEntity(ent)) {
			ent = (ArmorStand)loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
		}
	}
	
	public void setLocation(Location loc) {
		this.loc=loc.clone();
	}
	
	public Location getLocation() {
		return loc;
	}
}
