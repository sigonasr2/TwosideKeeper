package sig.plugin.TwosideKeeper;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
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
		TwosideKeeper.log("Item Cube Window added. List is now size "+pd.openeditemcube.size(),2);
	}
	
	public static void popItemCubeWindow(Player p) {
		PlayerStructure pd = (PlayerStructure)TwosideKeeper.playerdata.get(p.getUniqueId());
		if (!pd.opened_inventory && 
				pd.openeditemcube.size()>0) {
			ItemCubeWindow window = pd.openeditemcube.remove(pd.openeditemcube.size()-1);
			TwosideKeeper.log("Item Cube Window removed. List is now size "+pd.openeditemcube.size(),2);
			pd.opened_inventory=true;
			openItemCube(p,window.id,window.size,false); //Open this item cube without adding it to the list. We're not nesting this one.
		}
		TwosideKeeper.log("pd.opened_inventory was "+pd.opened_inventory+". List size is "+pd.openeditemcube.size(),2);
	}
	
	public static void removeAllItemCubeWindows(Player p) {
		PlayerStructure pd = (PlayerStructure)TwosideKeeper.playerdata.get(p.getUniqueId());
		pd.openeditemcube.clear();
	}
	
	//New open item cube method to handle all opening of item cubes.
	public static void openItemCube(Player p, int id, int size, boolean addToList) {
		TwosideKeeper.log("Called.", 2);
		PlayerStructure pd = (PlayerStructure)TwosideKeeper.playerdata.get(p.getUniqueId());
		if (addToList &&
				isViewingItemCubeInventory(p)) {
			addItemCubeWindow(p,getViewingItemCubeID(p),getViewingItemCubeInventorySize(p));
		}
		if (!ItemCube.isSomeoneViewingItemCube(id,p)) {
			InventoryView newinv = p.openInventory(Bukkit.getServer().createInventory(p, size, "Item Cube #"+id));
			TwosideKeeper.loadItemCubeInventory(newinv.getTopInventory(),newinv);
			p.playSound(p.getLocation(), Sound.BLOCK_CHEST_OPEN, 1.0f, 1.0f);
		} else {
			//ItemCube.displayErrorMessage(p);
			p.openInventory(ItemCube.getViewingItemCubeInventory(id, p));
			p.playSound(p.getLocation(), Sound.BLOCK_CHEST_OPEN, 1.0f, 1.0f);
		}
		pd.isViewingItemCube=true;
		pd.opened_inventory=false;
	}
	
	public static boolean isViewingItemCubeInventory(Player p) {
		PlayerStructure pd = (PlayerStructure)TwosideKeeper.playerdata.get(p.getUniqueId());
		TwosideKeeper.log("Are we viewing it? "+pd.isViewingItemCube,2);
		return pd.isViewingItemCube;
	}
	
	public static int getViewingItemCubeID(Player p) {
		if (p.getOpenInventory().getTitle().contains("#")) {
			String inventoryTitle = p.getOpenInventory().getTitle();
			return Integer.parseInt(inventoryTitle.split("#")[1]);
		}
		return -1;
	}
	
	public static int getViewingItemCubeInventorySize(Player p) {
		return p.getOpenInventory().getTopInventory().getSize();
	}
}
