package sig.plugin.TwosideKeeper.HelperStructures;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

import sig.plugin.TwosideKeeper.HelperStructures.Common.ArmorStandProperties;

public class CustomModel {
	List<ArmorStandProperties> modelParts = new ArrayList<ArmorStandProperties>();
	List<ArmorStand> stands = new ArrayList<ArmorStand>();
	
	 public CustomModel(Location loc, ArmorStandProperties...modelParts) {
		 for (ArmorStandProperties asp : modelParts) {
			 this.modelParts.add(asp);
			 this.stands.add(setupArmorStand(loc, asp));
		 }
	 }
	 
	 private ArmorStand setupArmorStand(Location loc, ArmorStandProperties asp) {
		ArmorStand stand = (ArmorStand)loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
		stand.setArms(asp.isArms());
		stand.setBasePlate(asp.isBaseplate());
		stand.setBodyPose(asp.getBodyPose());
		stand.setBoots(asp.getBoots());
		stand.setChestplate(asp.getChestplate());
		stand.setHeadPose(asp.getHeadPose());
		stand.setHelmet(asp.getHelmet());
		stand.setItemInHand(asp.getHand());
		stand.setLeftArmPose(asp.getLeftArmPose());
		stand.setLeftLegPose(asp.getLeftLegPose());
		stand.setLeggings(asp.getLeggings());
		stand.setMarker(asp.isMarker());
		stand.setRightArmPose(asp.getRightArmPose());
		stand.setRightLegPose(asp.getRightLegPose());
		stand.setSmall(asp.isSmall());
		stand.setVisible(asp.isVisible());
		stand.setCustomNameVisible(asp.isCustomNameVisible());
		stand.setCustomName(asp.getCustomName());
		stand.teleport(loc.add(asp.getOffset()));
		return stand;
	}

	public void displayModel(Location loc) {
		for (int i=0;i<stands.size();i++) {
			if (stands.get(i)!=null && stands.get(i).isValid()) {
				stands.get(i).teleport(loc.add(modelParts.get(i).getOffset()));
			}
		}
	}
}
