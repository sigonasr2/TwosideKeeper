package sig.plugin.TwosideKeeper.HelperStructures;

import org.bukkit.Material;

/**
 * Structure contains the following:
 * The Material of the item,
 * The Item's Default Base Price
 */
public enum WorldShopItem {
	
	APPLE(Material.APPLE,1.00),
	BEETROOT(Material.BEETROOT,4.00),
	BEETROOT_SEEDS(Material.BEETROOT_SEEDS,3.25),
	BLAZE_ROD(Material.BLAZE_POWDER,23.00),
	BONE(Material.BONE,1.00),
	BROWN_MUSHROOM(Material.BROWN_MUSHROOM,12.00),
	CACTUS(Material.CACTUS,4.00),
	CARROT_ITEM(Material.CARROT_ITEM,4.00),
	CHORUS_FlOWER(Material.CHORUS_FLOWER,20.00),
	CHORUS_FRUIT(Material.CHORUS_FRUIT,14.00),
	CHORUS_FRUIT_POPPED(Material.CHORUS_FRUIT_POPPED,16.00),
	CLAY_BALL(Material.CLAY_BALL,6.00),
	CLAY_BRICK(Material.CLAY_BRICK,8.00),
	COAL(Material.COAL,16.00),
	COAL_ORE(Material.COAL_ORE,48.00),
	COBBLESTONE(Material.COBBLESTONE,1.00),
	;

	Material mat;
	double price;
	short data;
	
	WorldShopItem(Material mat, double price) {
		this.mat=mat;
		this.price=price;
		this.data=0;
	}
	WorldShopItem(Material mat, short data, double price) {
		this.mat=mat;
		this.price=price;
		this.data=data;
	}
}
