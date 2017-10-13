package sig.plugin.TwosideKeeper.HelperStructures;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;

import sig.plugin.TwosideKeeper.HelperStructures.Common.ArmorStandProperties;
import utils.Utils.Vector3D;

public class Model {
	List<ArmorStandProperties> modelParts = new ArrayList<ArmorStandProperties>();
	List<ArmorStandLinker> models = new ArrayList<ArmorStandLinker>();
	
	Location loc;
	Vector3D offset = new Vector3D(0,0,0); 
	double degreeRotation = 0;
	
	public Model(Location loc, ArmorStandProperties...modelParts) {
		this.modelParts = Arrays.asList(modelParts);
		for (ArmorStandProperties prop : modelParts) {
			models.add(new ArmorStandLinker(loc));
		}
		this.loc=loc;
	}
	
	public void run() {
		for (ArmorStandLinker parts : models) {
			parts.run();
			parts.setLocation(loc);
		}
	}
	
	public void setLocation(Location loc) {
		this.loc = loc.clone();
	}
	
	public Location getLocation() {
		return loc;
	}
}
