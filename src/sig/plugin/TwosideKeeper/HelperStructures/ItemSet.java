package sig.plugin.TwosideKeeper.HelperStructures;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;

public enum ItemSet {
	PANROS(1,2,3),
	SONGSTEEL(4,6,10),
	DAWNTRACKER(3,5,8),
	LORASYS(0,0,0);
	
	int val1;
	int val2;
	int val3;
	
	ItemSet(int val1,int val2, int val3) {
		this.val1=val1;
		this.val2=val2;
		this.val3=val3;
	} 
	
	public static boolean isSetItem(ItemStack item) {
		return GetSet(item)!=null;
	}

	public static ItemSet GetSet(ItemStack item) {
		if (GenericFunctions.isEquip(item) &&
				item.getItemMeta().hasLore()) {
			List<String> lore = item.getItemMeta().getLore();
			for (int i=0;i<lore.size();i++) {
				if (lore.get(i).contains(ChatColor.GOLD+""+ChatColor.BOLD+"T")) {
					//This is the tier line.
					return ItemSet.valueOf(lore.get(i).replace(ChatColor.GOLD+""+ChatColor.BOLD+"T", "").split(" ")[1].toUpperCase());
				}
			}
		} 
		return null;
	}
	
	public static int GetTier(ItemStack item) {
		if (GenericFunctions.isEquip(item) &&
				item.getItemMeta().hasLore()) {
			List<String> lore = item.getItemMeta().getLore();
			for (int i=0;i<lore.size();i++) {
				if (lore.get(i).contains(ChatColor.GOLD+""+ChatColor.BOLD+"T")) {
					//This is the tier line.
					return Integer.parseInt(lore.get(i).replace(ChatColor.GOLD+""+ChatColor.BOLD+"T", "").split(" ")[0]);
				}
			}
		} 
		TwosideKeeper.log(ChatColor.RED+"[ERROR] Could not detect proper tier of "+item.toString()+"!", 1);
		return 0;
	}
	
	public int GetBaseAmount(ItemStack item) {
		switch (GetTier(item)) {
			case 3:{
				return this.val2;
			}
			case 4:{
				return this.val3;
			}
			default:{
				return this.val1;
			}
		}
	}
	
	public static int GetSetCount(ItemSet set, LivingEntity ent) {
		int count = 0;
		for (int i=0;i<GenericFunctions.getEquipment(ent).length;i++) {
			ItemSet temp = ItemSet.GetSet(GenericFunctions.getEquipment(ent)[i]);
			if (temp!=null) {
				if (temp.equals(set)) {
					count++;
				}
			}
		}
		TwosideKeeper.log("Currently have "+count+" pieces from the "+set.name()+" set.", 5);
		return count;
	}
}
