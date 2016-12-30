package sig.plugin.TwosideKeeper.HelperStructures.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Hopper;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.HelperStructures.CubeType;
import sig.plugin.TwosideKeeper.HelperStructures.ItemCube;

public class ItemCubeUtils {
	public static int getItemCubeID(ItemStack item) {
		return Integer.parseInt(ItemUtils.GetLoreLineContainingSubstring(item, ChatColor.DARK_PURPLE+"ID#").split("#")[1]);
	}
	public static Location getFilterCubeLoc(int id) {
		int posx = id % 960;
		int posy = 64;
		int posz = id / 960;
		return new Location(Bukkit.getWorld("FilterCube"),posx,posy,posz);
	}
	public static Block getFilterCubeBlock(int id) {
		Block b = Bukkit.getWorld("FilterCube").getBlockAt(getFilterCubeLoc(id));
		return b;
	}
	public static Hopper getFilterCubeHopper(int id) {
		Hopper h = (Hopper)Bukkit.getWorld("FilterCube").getBlockAt(getFilterCubeLoc(id)).getState();
		return h;
	}
	public static void createNewFilterCube(int id) {
		Block b = getFilterCubeBlock(id);
		b.getWorld().getBlockAt(getFilterCubeLoc(id)).setType(Material.HOPPER);
	}
	public static HashMap<Integer, ItemStack> AttemptingToAddItemToFilterCube(int id, Inventory cube_inv, ItemStack[] remaining) {
		return AttemptingToAddItemToFilterCube(id,cube_inv,remaining,false);
	}
	public static HashMap<Integer, ItemStack> AttemptingToAddItemToFilterCube(int id, Inventory cube_inv, ItemStack[] remaining, boolean testing) {
		Hopper h = getFilterCubeHopper(id);
		Inventory inv = h.getInventory();
		HashMap<Integer,ItemStack> reject_items = new HashMap<Integer,ItemStack>();
		for (ItemStack it : remaining) {
			if (it!=null) {
				if (InventoryUtils.InventoryContainSameMaterial(inv, it)) {
					HashMap<Integer,ItemStack> extras = cube_inv.addItem(it);
					if (extras.size()==0) {
						List<ItemStack> itemslist = new ArrayList<ItemStack>();
						for (int i=0;i<cube_inv.getSize();i++) {
							itemslist.add(cube_inv.getItem(i));
						}
						ItemCube.addToViewersOfItemCube(id,remaining,null);
						if (!testing) {
							TwosideKeeper.itemCube_saveConfig(id, itemslist);
						}
					} else {
						for (ItemStack i : extras.values()) {
							reject_items.put(reject_items.size(), i);
							List<ItemStack> itemslist = new ArrayList<ItemStack>();
							for (int j=0;j<cube_inv.getSize();j++) {
								itemslist.add(cube_inv.getItem(j));
							}
							if (!testing) {
								TwosideKeeper.itemCube_saveConfig(id, itemslist);
							}
						}
					}
				} else {
					reject_items.put(reject_items.size(), it);
				}
			}
		}
		return reject_items;
	}
	public static boolean SomeoneHasAFilterCubeOpen() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (p.getOpenInventory()!=null && p.getOpenInventory().getTopInventory()!=null && p.getOpenInventory().getTopInventory().getType()==InventoryType.HOPPER) {
				TwosideKeeper.log("Keep this open! "+p.getName()+" is using it!", 5);
				return true;
			}
		}
		return false;
	}
	public static boolean isItemCubeMaterial(Material mat) {
		if (mat==Material.CHEST ||
				mat==Material.ENDER_CHEST ||
				mat==Material.HOPPER_MINECART ||
				mat==Material.STORAGE_MINECART) {
			return true;
		} else {
			return false;
		}
	}
	public static boolean isItemCube(ItemStack it) {
		if (ItemUtils.isValidLoreItem(it) &&
				isItemCubeMaterial(it.getType()) &&
				ItemUtils.LoreContainsSubstring(it, ChatColor.DARK_PURPLE+"ID#")) {
			return true;
		}
		return false;
	}
	
	public static CubeType getCubeType(int id){
		File config;
		config = new File(TwosideKeeper.filesave,"itemcubes/ItemCube"+id+".data");
		FileConfiguration workable = YamlConfiguration.loadConfiguration(config);
		return CubeType.getCubeTypeFromID(workable.getInt("cubetype"));
	}
	
	public static List<ItemStack> getItemCubeContents(int id) {
		return loadConfig(id);
	}

	public static List<ItemStack> loadConfig(int id){
		List<ItemStack> ItemCube_items = new ArrayList<ItemStack>();
		File config;
		config = new File(TwosideKeeper.filesave,"itemcubes/ItemCube"+id+".data");
		FileConfiguration workable = YamlConfiguration.loadConfiguration(config);
		CubeType type = CubeType.getCubeTypeFromID(workable.getInt("cubetype"));
		for (int i=0;i<type.getSize();i++) {
			ItemCube_items.add(workable.getItemStack("item"+i, new ItemStack(Material.AIR)));
		}
		return ItemCube_items;
	}
	
	public static List<ItemStack> loadFilterConfig(int id){
		List<ItemStack> ItemCube_items = new ArrayList<ItemStack>();
		File config;
		config = new File(TwosideKeeper.filesave,"itemcubes/ItemCube"+id+".data");
		FileConfiguration workable = YamlConfiguration.loadConfiguration(config);
		
		for (int i=0;i<5;i++) {
			ItemCube_items.add(workable.getItemStack("filter"+i, new ItemStack(Material.AIR)));
		}
		return ItemCube_items;
	}
	
	public static void saveConfig(int id, List<ItemStack> items){
		saveConfig(id,items,null);
	}
	
	public static void saveConfig(int id, List<ItemStack> items, CubeType cubetype){
		File config;
		config = new File(TwosideKeeper.filesave,"itemcubes/ItemCube"+id+".data");
		FileConfiguration workable = YamlConfiguration.loadConfiguration(config);
		
		for (int i=0;i<items.size();i++) {
			workable.set("item"+i, items.get(i));
		}
		if (cubetype!=null) {
			workable.set("cubetype", cubetype.getID());
		}
		try {
			workable.save(config);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Collection<ItemStack> addItems(int id, ItemStack...items) {
		List<ItemStack> currentcontents = getItemCubeContents(id);
		List<ItemStack> leftovers = new ArrayList<ItemStack>();
		//Attempt to add them to an inventory.
		CubeType size = getCubeType(id);
		int slots = size.getSize();
		Inventory inv = Bukkit.createInventory(null, slots);
		for (int i=0;i<slots;i++) {
			inv.setItem(i, currentcontents.get(i));
		}
		for (ItemStack item : items) {
			if (item!=null) {
				ItemStack tempitem = item.clone();
				HashMap<Integer,ItemStack> remaining = inv.addItem(tempitem.clone());
				if (remaining.size()>0) {
					tempitem.setAmount(tempitem.getAmount()-remaining.get(0).getAmount());
					if (tempitem.getAmount()-remaining.get(0).getAmount()>0) {
						ItemCube.addToViewersOfItemCube(id,tempitem,null);
					}
					leftovers.add(remaining.get(0).clone());
				} else {
					ItemCube.addToViewersOfItemCube(id,tempitem,null);	
				}
			}
		}
		saveConfig(id,InventoryUtils.ConvertInventoryToList(inv,slots),size);
		return leftovers;
	}
}
