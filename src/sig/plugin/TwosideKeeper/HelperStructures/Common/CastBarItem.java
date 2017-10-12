package sig.plugin.TwosideKeeper.HelperStructures.Common;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import sig.plugin.TwosideKeeper.HelperStructures.Utils.ItemUtils;

public class CastBarItem {
	ItemStack item;
	
	public CastBarItem(Material mat) {
		this(mat,(short)0,1,null);
	}
	
	public CastBarItem(Material mat, short data, String displayName) {
		this(mat,data,1,displayName);
	}
	
	public CastBarItem(Material mat, short data, int amt, String displayName) {
		item = new ItemStack(mat,amt,data);
		if (displayName!=null) {
			ItemUtils.setDisplayName(item, displayName);
		}
	}
	
	public CastBarItem(MaterialData matdata, String displayName) {
		item = new ItemStack(matdata.getItemType(),1,matdata.getData());
	}
	
	public ItemStack getItemStack() {
		return item;
	}
}
