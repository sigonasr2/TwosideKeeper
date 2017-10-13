package sig.plugin.TwosideKeeper.HelperStructures;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.HelperStructures.Common.ArmorStandProperties;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.EntityUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.FileUtils;

public class CustomModel {
	public List<ArmorStandProperties> modelParts = new ArrayList<ArmorStandProperties>();
	public List<ArmorStand> stands = new ArrayList<ArmorStand>();
	Location origin;
	
	 public CustomModel(Location loc, ArmorStandProperties...modelParts) {
		 origin = loc.clone();
		 for (ArmorStandProperties asp : modelParts) {
			 AddModelPart(asp);
		 }
	 }
	 
	 public static ArmorStandProperties getPropertyFromStand(UUID id) {
		 for (CustomModel model : TwosideKeeper.models) {
			 for (int i=0;i<model.stands.size();i++) {
				 ArmorStand stand = model.stands.get(i);
				 if (EntityUtils.isValidEntity(stand) &&
						 stand.getUniqueId().equals(id)) {
					 return model.modelParts.get(i);
				 }
			 }
		 }
		 return null;
	 }
	 
	 public static ArmorStandProperties getPropertyFromStand(ArmorStand stand) {
		 return getPropertyFromStand(stand.getUniqueId());
	 }
	 
	 private ArmorStand setupArmorStand(ArmorStandProperties asp) {
		 return setupArmorStand(origin,asp);
	 }
	 
	 private ArmorStand setupArmorStand(Location defaultSpawn, ArmorStandProperties asp) {
		ArmorStand stand = (ArmorStand)origin.getWorld().spawnEntity(defaultSpawn.clone(), EntityType.ARMOR_STAND);
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
		stand.teleport(origin.add(asp.getOffset()));
		stand.setRemoveWhenFarAway(true);
		stand.setGravity(false);
		stand.setInvulnerable(true);
		return stand;
	}
	 
	public void AddModelPart(ArmorStandProperties aspOld) {
		AddModelPart(origin,aspOld);
	}
	 
	public void AddModelPart(Location defaultSpawn, ArmorStandProperties aspOld) {
		//ArmorStandProperties asp = aspOld.clone();
		//asp.setOffset(new Vector(loc.getX()-origin.getX(),loc.getY()-origin.getY(),loc.getZ()-origin.getZ()));
		this.modelParts.add(aspOld);
		this.stands.add(setupArmorStand(defaultSpawn,aspOld));
		TwosideKeeper.log("Added model! New part count: "+stands.size(), 1);
	}
	 
	public void displayModel() {
		for (int i=0;i<stands.size();i++) {
			if (stands.get(i)!=null && stands.get(i).isValid()) {
				Location newpos = origin.clone().add(modelParts.get(i).getOffset());
				stands.get(i).teleport(newpos);
			} else {
				Location oldPos = stands.get(i).getLocation().clone();
				stands.get(i).remove();
				stands.set(i, setupArmorStand(oldPos,modelParts.get(i)));
				TwosideKeeper.log("Recreated Part "+i, 1);
			}
		}
	}
	
	public void loadModel(Location loc, String modelName) {
		String[] properties = FileUtils.readFromFile(TwosideKeeper.plugin.getDataFolder()+"/models/"+modelName);
		int modelCount = properties[0].split(",").length-1;
		cleanup();
		for (int i=0;i<modelCount;i++) {
			String[] modelprops = new String[properties.length];
			for (int j=0;j<properties.length;j++) {
				modelprops[j] = properties[j].split(",")[i+1];
			}
			
			int k=0;
			ArmorStandProperties modelProp = new ArmorStandProperties();
			modelProp.setArms(Boolean.parseBoolean(modelprops[k++]));
			modelProp.setBaseplate(Boolean.parseBoolean(modelprops[k++]));
			modelProp.setBodyPose(parseEulerAngle(modelprops[k++]));
			modelProp.setBoots(parseItemStack(modelprops[k++]));
			modelProp.setChestplate(parseItemStack(modelprops[k++]));
			modelProp.setHeadPose(parseEulerAngle(modelprops[k++]));
			modelProp.setHelmet(parseItemStack(modelprops[k++]));
			modelProp.setHand(parseItemStack(modelprops[k++]));
			modelProp.setLeftArmPose(parseEulerAngle(modelprops[k++]));
			modelProp.setLeftLegPose(parseEulerAngle(modelprops[k++]));
			modelProp.setLeggings(parseItemStack(modelprops[k++]));
			modelProp.setMarker(Boolean.parseBoolean(modelprops[k++]));
			modelProp.setRightArmPose(parseEulerAngle(modelprops[k++]));
			modelProp.setRightLegPose(parseEulerAngle(modelprops[k++]));
			modelProp.setSmall(Boolean.parseBoolean(modelprops[k++]));
			modelProp.setVisible(Boolean.parseBoolean(modelprops[k++]));
			modelProp.setCustomNameVisible(Boolean.parseBoolean(modelprops[k++]));
			modelProp.setCustomName(modelprops[k++]);
			modelProp.setOffset(parseVector(modelprops[k++]));
			modelProp.setFacingDirection(parseVector(modelprops[k++]));
			modelProp.setGravity(Boolean.parseBoolean(modelprops[k++]));
			

			Bukkit.getScheduler().runTaskLater(TwosideKeeper.plugin, ()->{
				AddModelPart(origin.clone().add(modelProp.getOffset()),modelProp);
			},(i+1)*20);
			/*modelParts.add(modelProp);
			stands.add(setupArmorStand(loc,modelProp));*/
		}
	}

	public ItemStack parseItemStack(String csvString) {
		String[] parse = csvString.split(";");
		ItemStack item = new ItemStack(Material.valueOf(parse[0]));
		boolean isEnchanted = Boolean.parseBoolean(parse[1]);
		if (isEnchanted) {
			item.addUnsafeEnchantment(Enchantment.values()[(int)(Math.random()*Enchantment.values().length)], 9);
		}
		return item;
	}
	
	public Vector parseVector(String csvString) {
		String[] parse = csvString.split(";");
		return new Vector(Double.parseDouble(parse[0]),Double.parseDouble(parse[1]),Double.parseDouble(parse[2]));
	}
	
	public EulerAngle parseEulerAngle(String csvString) {
		String[] parse = csvString.split(";");
		return new EulerAngle(Double.parseDouble(parse[0]),Double.parseDouble(parse[1]),Double.parseDouble(parse[2]));
	}
	
	public void saveModel(String modelName) {
		StringBuilder[] propertyName = new StringBuilder[]{new StringBuilder("Arms,"),
			new StringBuilder("BasePlate,"),
			new StringBuilder("BodyPose,"),
			new StringBuilder("Boots,"),
			new StringBuilder("Chestplate,"),
			new StringBuilder("HeadPose,"),
			new StringBuilder("Helmet,"),
			new StringBuilder("ItemInHand,"),
			new StringBuilder("LeftArmPose,"),
			new StringBuilder("LeftLegPose,"),
			new StringBuilder("Leggings,"),
			new StringBuilder("Marker,"),
			new StringBuilder("RighArmPose,"),
			new StringBuilder("RightLegPose,"),
			new StringBuilder("Small,"),
			new StringBuilder("Visible,"),
			new StringBuilder("CustomNameVisible,"),
			new StringBuilder("CustomName,"),
			new StringBuilder("Offset,"),
			new StringBuilder("Direction,"),
			new StringBuilder("Gravity,")};
		for (int j=0;j<stands.size();j++) {
			ArmorStand stand = stands.get(j);
			ArmorStandProperties asp = modelParts.get(j);
			int i=0;
			propertyName[i++].append(stand.hasArms()+",");
			propertyName[i++].append(stand.hasBasePlate()+",");
			propertyName[i++].append(ConvertEulerAngle(stand.getBodyPose())+",");
			propertyName[i++].append(ConvertItemStack(stand.getBoots())+",");
			propertyName[i++].append(ConvertItemStack(stand.getChestplate())+",");
			propertyName[i++].append(ConvertEulerAngle(stand.getHeadPose())+",");
			propertyName[i++].append(ConvertItemStack(stand.getHelmet())+",");
			propertyName[i++].append(ConvertItemStack(stand.getItemInHand())+",");
			propertyName[i++].append(ConvertEulerAngle(stand.getLeftArmPose())+",");
			propertyName[i++].append(ConvertEulerAngle(stand.getLeftLegPose())+",");
			propertyName[i++].append(ConvertItemStack(stand.getLeggings())+",");
			propertyName[i++].append(stand.isMarker()+",");
			propertyName[i++].append(ConvertEulerAngle(stand.getRightArmPose())+",");
			propertyName[i++].append(ConvertEulerAngle(stand.getRightLegPose())+",");
			propertyName[i++].append(stand.isSmall()+",");
			propertyName[i++].append(stand.isVisible()+",");
			propertyName[i++].append(stand.isCustomNameVisible()+",");
			propertyName[i++].append(stand.getCustomName()+",");
			propertyName[i++].append(ConvertVector(asp.getOffset())+",");
			propertyName[i++].append(ConvertVector(asp.getFacingDirection())+",");
			propertyName[i++].append(stand.hasGravity()+",");
		}
		String[] finalString = new String[propertyName.length];
		int j=0;
		for (int i=0;i<propertyName.length;i++) {
			StringBuilder builder = propertyName[i];
			finalString[j++] = builder.toString();
		}
		FileUtils.writetoFile(finalString, TwosideKeeper.plugin.getDataFolder()+"/models/"+modelName);
	}

	private String ConvertItemStack(ItemStack item) {
		return item.getType()+";"+((item.getEnchantments().size()>0)?Boolean.toString(true):Boolean.toString(false));
	}

	private String ConvertEulerAngle(EulerAngle angle) {
		return angle.getX()+";"+angle.getY()+";"+angle.getZ();
	}

	private String ConvertVector(Vector angle) {
		return angle.getX()+";"+angle.getY()+";"+angle.getZ();
	}
	
	public static void cleanup(CustomModel model) {
		model.cleanup();
		TwosideKeeper.models.remove(model);
		model=null;
	}
	
	public void cleanup() {
		for (ArmorStand stand : stands) {
			TwosideKeeper.log("Removing entity ("+GenericFunctions.getDisplayName(stand)+")"+stand.getUniqueId(), 1);
			stand.remove();
		}
		stands.clear();
		modelParts.clear();
	}
}
