package sig.plugin.TwosideKeeper;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

import sig.plugin.TwosideKeeper.HelperStructures.ItemCube;

public class ItemCubeWindow {
	int id = 0;
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
	}
}
