package sig.plugin.TwosideKeeper;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

import sig.plugin.TwosideKeeper.HelperStructures.CubeType;
import sig.plugin.TwosideKeeper.HelperStructures.ItemCube;

public class ItemCubeWindow {
	public static void addItemCubeWindow(Player p, int id) {
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		pd.itemcubelist.add(id);
		TwosideKeeper.log("Added cube "+id+" to Item Cube List for Player "+p.getName()+". New list: "+pd.itemcubelist.toString(), 2);
	}
	public static void popItemCubeWindow(Player p) {
		//Opens the next possible item cube inventory from the list of inventories.
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		if (pd.itemcubelist.size()>0 && !pd.opened_another_cube) {
			int index = pd.itemcubelist.size()-1;
			Integer itemcubeid = pd.itemcubelist.get(index);
			TwosideKeeper.log("Popping Item Cube ID "+index+" from "+p.getName()+"'s list.", 2);
			pd.itemcubelist.remove(index);

			Bukkit.getScheduler().scheduleSyncDelayedTask(TwosideKeeper.plugin, new Runnable() {
				public void run() {
					if (!ItemCube.isSomeoneViewingItemCube(itemcubeid,p)) {
						//pd.itemcubeviews.add(p.getOpenInventory());
						CubeType size = TwosideKeeper.itemCube_getCubeType(itemcubeid);
						int inv_size = 9;
						if (size!=CubeType.NORMAL) {
							inv_size=27;
						}
						Inventory temp = Bukkit.getServer().createInventory(p, inv_size, "Item Cube #"+itemcubeid);
						TwosideKeeper.openItemCubeInventory(temp);
						InventoryView newinv = p.openInventory(temp);
						pd.isViewingItemCube=true;
						p.playSound(p.getLocation(),Sound.BLOCK_CHEST_OPEN,1.0f,1.0f);
					} else {
						p.openInventory(ItemCube.getViewingItemCubeInventory(itemcubeid, p));
						pd.isViewingItemCube=true;
		    			p.playSound(p.getLocation(), Sound.BLOCK_CHEST_OPEN, 1.0f, 1.0f);
					}
				}},1);
		}
	}
	public static void removeAllItemCubeWindows(Player p) {
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		pd.itemcubelist.clear();
	}
	/*int id = 0; //LEGACY CODE.
	int size = 0;
	
	public ItemCubeWindow(int id, int size) {
		this.id=id;
		this.size=size;
	}
	
	public static void addItemCubeWindow(Player p, int id, int size) {
		PlayerStructure pd = (PlayerStructure)TwosideKeeper.playerdata.get(p.getUniqueId());
		pd.openeditemcube.add(new ItemCubeWindow(id, size));
		pd.opened_inventory = true;
	}
	
	public static void popItemCubeWindow(Player p) {
		PlayerStructure pd = (PlayerStructure)TwosideKeeper.playerdata.get(p.getUniqueId());
		if (!pd.opened_inventory && 
				pd.openeditemcube.size()>0) {
			pd.opened_inventory=true;
			TwosideKeeper.log("Popped one.",2);
			ItemCubeWindow window = pd.openeditemcube.remove(pd.openeditemcube.size()-1);
			openItemCube(p,window.id,window.size,false); //Open this item cube without adding it to the list. We're not nesting this one.
		}
	}
	
	public static void removeAllItemCubeWindows(Player p) {
		PlayerStructure pd = (PlayerStructure)TwosideKeeper.playerdata.get(p.getUniqueId());
		pd.openeditemcube.clear();
	}
	
	//New open item cube method to handle all opening of item cubes.
	public static void openItemCube(Player p, int id, int size, boolean addToList) {
		PlayerStructure pd = (PlayerStructure)TwosideKeeper.playerdata.get(p.getUniqueId());
		if (addToList &&
				isViewingItemCubeInventory(p)) {
			addItemCubeWindow(p,getViewingItemCubeID(p),getViewingItemCubeInventorySize(p));
		}
		if (!ItemCube.isSomeoneViewingItemCube(id,p)) {
			TwosideKeeper.log("This should be activated",2);
			Inventory inv = Bukkit.getServer().createInventory(p, size, "Item Cube #"+id);
			InventoryView newinv = p.openInventory(inv);
			TwosideKeeper.loadItemCubeInventory(inv,newinv);
			pd.isViewingItemCube=true;
			p.playSound(p.getLocation(), Sound.BLOCK_CHEST_OPEN, 1.0f, 1.0f);
		} else {
			//ItemCube.displayErrorMessage(p);
			p.openInventory(ItemCube.getViewingItemCubeInventory(id, p));
			pd.isViewingItemCube=true;
			p.playSound(p.getLocation(), Sound.BLOCK_CHEST_OPEN, 1.0f, 1.0f);
		}
		pd.opened_inventory=false;
	}
	
	public static boolean isViewingItemCubeInventory(Player p) {
		PlayerStructure pd = (PlayerStructure)TwosideKeeper.playerdata.get(p.getUniqueId());
		return p.getOpenInventory().getTopInventory().getTitle().contains("Item Cube #");
	}
	
	public static int getViewingItemCubeID(Player p) {
		if (isViewingItemCubeInventory(p)) {
			String inventoryTitle = p.getOpenInventory().getTopInventory().getTitle();
			return Integer.parseInt(inventoryTitle.split("#")[1]);
		}
		return -1;
	}
	
	public static int getViewingItemCubeInventorySize(Player p) {
		if (isViewingItemCubeInventory(p)) {
			return p.getOpenInventory().getTopInventory().getSize();
		}
		return -1;
	}*/
}
