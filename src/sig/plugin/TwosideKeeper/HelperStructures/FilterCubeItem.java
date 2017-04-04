package sig.plugin.TwosideKeeper.HelperStructures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Hopper;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import sig.plugin.TwosideKeeper.PlayerStructure;
import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.ItemCubeUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.TextUtils;

public class FilterCubeItem {
	public static void populateFilterCubeItemList(Player p) {
		HashMap<Material, List<Integer>> structure = GrabFilterCubeStructure(p);
		structure.clear();

		for (ItemStack item : p.getInventory().getContents()) {
			if (item!=null && CustomItem.isFilterCube(item)) {
				Hopper h = ItemCubeUtils.getFilterCubeHopper(ItemCubeUtils.getItemCubeID(item));
				for (ItemStack it : h.getInventory()) {
					if (it!=null) {
						int id = ItemCubeUtils.getItemCubeID(item);
						if (structure.containsKey(it.getType())) {
							List<Integer> filterCubes = structure.get(it.getType());
							filterCubes.add(id);
						} else {
							List<Integer> filterCubeList = new ArrayList<Integer>();
							filterCubeList.add(id);
							structure.put(it.getType(), filterCubeList);
						}
					}
				}
			}
		}
		
		//TwosideKeeper.log("New Map: \n"+TextUtils.outputHashmap(structure), 0);
	}

	public static HashMap<Material, List<Integer>> GrabFilterCubeStructure(Player p) {
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		HashMap<Material,List<Integer>> structure = pd.filtercubestructure;
		return structure;
	}
	
	public static boolean ItemHasFilterCube(ItemStack item, Player p) {
		HashMap<Material, List<Integer>> structure = GrabFilterCubeStructure(p);
		return structure.containsKey(item.getType());
	}
	
	public static List<Integer> getFilterCubeIDsToInsertItem(ItemStack item, Player p) {
		HashMap<Material, List<Integer>> structure = GrabFilterCubeStructure(p);
		if (structure.containsKey(item.getType())) {
			return structure.get(item.getType());
		} else {
			return new ArrayList<Integer>();
		}
	}
}
