package sig.plugin.TwosideKeeper.HelperStructures;

public enum UpgradePath {
	WEAPON, //Weapon Upgrade Path - Falls under the 'All' category.
	BOW, //Bow Upgrade Path - Falls under the 'Weapon' category.
	ARMOR, //Armor Upgrade Path - Falls under the 'All' category.
	TOOL, //Tool Upgrade Path - Falls under the 'All' category.
	SWORD, //Falls under the 'Weapon' category.
	AXE, //Falls under the 'Weapon' and 'Tool' category.
	PICKAXE, //Falls under the 'Tool' category.
	SHOVEL, //Falls under the 'Tool' category.
	SCYTHE, //Falls under the 'Weapon' and 'Tool' category.
	FISHING_ROD, //Falls under the 'Weapon' category.
	BASIC, //Every category that is not 'Armor'.
	ALL, //The base category.
	PROVOKE
}