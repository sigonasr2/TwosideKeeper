package sig.plugin.TwosideKeeper.HelperStructures;

import org.bukkit.Location;

public class RecyclingCenterNode {
	private Location loc;
	private boolean toolsAllowed=true;
	private boolean itemsAllowed=true;
	
	public RecyclingCenterNode(Location loc) {
		this.loc=loc.clone();
		this.toolsAllowed=true;
		this.itemsAllowed=true;
	}
	
	public RecyclingCenterNode(Location loc, boolean toolsAllowed) {
		this.loc=loc.clone();
		this.toolsAllowed=toolsAllowed;
		this.itemsAllowed=true;
	}
	
	public RecyclingCenterNode(Location loc, boolean toolsAllowed, boolean itemsAllowed) {
		this.loc=loc.clone();
		this.toolsAllowed=toolsAllowed;
		this.itemsAllowed=itemsAllowed;
	}

	public boolean areToolsAllowed() {
		return (itemsAllowed && toolsAllowed);
	}

	public void setToolsAllowed(boolean toolsAllowed) {
		this.toolsAllowed = toolsAllowed;
		if (!this.itemsAllowed) {this.itemsAllowed = toolsAllowed;}
	}

	public boolean areItemsAllowed() {
		return itemsAllowed;
	}

	public void setItemsAllowed(boolean itemsAllowed) {
		this.itemsAllowed = itemsAllowed;
	}

	public Location getRecyclingCenterLocation() {
		return loc;
	}
	
	
}	
