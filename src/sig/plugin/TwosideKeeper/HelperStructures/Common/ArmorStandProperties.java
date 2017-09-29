package sig.plugin.TwosideKeeper.HelperStructures.Common;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import sig.plugin.TwosideKeeper.HelperStructures.Utils.MathUtils;

public class ArmorStandProperties {
	final public static ArmorStandProperties SCEPTERBASE = new ArmorStandProperties();
	final public static ArmorStandProperties SCEPTERTOP = new ArmorStandProperties();
	
	boolean arms = false;
	boolean baseplate = false;
	EulerAngle bodyPose = EulerAngle.ZERO;
	ItemStack boots = new ItemStack(Material.AIR);
	ItemStack chestplate = new ItemStack(Material.AIR);
	EulerAngle headPose = EulerAngle.ZERO;
	ItemStack helmet = new ItemStack(Material.AIR);
	ItemStack hand = new ItemStack(Material.AIR);
	EulerAngle leftArmPose = EulerAngle.ZERO;
	EulerAngle leftLegPose = EulerAngle.ZERO;
	ItemStack leggings = new ItemStack(Material.AIR);
	boolean marker = false;
	EulerAngle rightArmPose = EulerAngle.ZERO;
	EulerAngle rightLegPose = EulerAngle.ZERO;

	boolean small = false;
	boolean visible=true;
	boolean customNameVisible=false;
	String customName="";
	Vector offset = new Vector();
	
	public ArmorStandProperties() {
		
	}

	public boolean isArms() {
		return arms;
	}

	public void setArms(boolean arms) {
		this.arms = arms;
	}

	public boolean isBaseplate() {
		return baseplate;
	}

	public void setBaseplate(boolean baseplate) {
		this.baseplate = baseplate;
	}

	public EulerAngle getBodyPose() {
		return bodyPose;
	}

	public void setBodyPose(EulerAngle bodyPose) {
		this.bodyPose = bodyPose;
	}

	public ItemStack getBoots() {
		return boots;
	}

	public void setBoots(ItemStack boots) {
		this.boots = boots;
	}

	public ItemStack getChestplate() {
		return chestplate;
	}

	public void setChestplate(ItemStack chestplate) {
		this.chestplate = chestplate;
	}

	public EulerAngle getHeadPose() {
		return headPose;
	}

	public void setHeadPose(EulerAngle headPose) {
		this.headPose = headPose;
	}

	public ItemStack getHelmet() {
		return helmet;
	}

	public void setHelmet(ItemStack helmet) {
		this.helmet = helmet;
	}

	public ItemStack getHand() {
		return hand;
	}

	public void setHand(ItemStack hand) {
		this.hand = hand;
	}

	public EulerAngle getLeftArmPose() {
		return leftArmPose;
	}

	public void setLeftArmPose(EulerAngle leftArmPose) {
		this.leftArmPose = leftArmPose;
	}

	public EulerAngle getLeftLegPose() {
		return leftLegPose;
	}

	public void setLeftLegPose(EulerAngle leftLegPose) {
		this.leftLegPose = leftLegPose;
	}

	public ItemStack getLeggings() {
		return leggings;
	}

	public void setLeggings(ItemStack leggings) {
		this.leggings = leggings;
	}

	public boolean isMarker() {
		return marker;
	}

	public void setMarker(boolean marker) {
		this.marker = marker;
	}

	public EulerAngle getRightArmPose() {
		return rightArmPose;
	}

	public void setRightArmPose(EulerAngle rightArmPose) {
		this.rightArmPose = rightArmPose;
	}

	public boolean isSmall() {
		return small;
	}

	public void setSmall(boolean small) {
		this.small = small;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isCustomNameVisible() {
		return customNameVisible;
	}

	public void setCustomNameVisible(boolean customNameVisible) {
		this.customNameVisible = customNameVisible;
	}

	public String getCustomName() {
		return customName;
	}

	public void setCustomName(String customName) {
		this.customName = customName;
	}
	public EulerAngle getRightLegPose() {
		return rightLegPose;
	}

	public void setRightLegPose(EulerAngle rightLegPose) {
		this.rightLegPose = rightLegPose;
	}

	public Vector getOffset() {
		return offset;
	}

	public void setOffset(Vector offset) {
		this.offset = offset;
	}

	public static void defineAllModels() {
		SetupScepterBase();
		SetupScepterTop();
	}

	private static void SetupScepterTop() {
		SCEPTERTOP.rightArmPose = MathUtils.getEulerAngleDegrees(-90, 0, 0);
		SCEPTERTOP.hand = new ItemStack(Material.DOUBLE_PLANT);
		SCEPTERTOP.offset = new Vector(-0.7,0,0.1);
	}

	private static void SetupScepterBase() {
		SCEPTERBASE.rightArmPose = MathUtils.getEulerAngleDegrees(-90, 90, 0);
		SCEPTERBASE.small = true;
		SCEPTERBASE.hand = new ItemStack(Material.BONE);
	}
}

