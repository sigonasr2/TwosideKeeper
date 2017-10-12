package sig.plugin.TwosideKeeper.HelperStructures;

import org.bukkit.Location;

public class RecyclingCenterNode {
	private Location loc;
	private boolean toolsAllowed=true;
	private boolean itemsAllowed=true;
	private String recyclingCenterName="Recycling Center";
	
	public RecyclingCenterNode(Location loc, String name) {
		this(loc,name,true);
	}
	
	public RecyclingCenterNode(Location loc, String name, boolean toolsAllowed) {
		this(loc,name,toolsAllowed,true);
	}
	
	public RecyclingCenterNode(Location loc, String name, boolean toolsAllowed, boolean itemsAllowed) {
		this.loc=loc.clone();
		this.toolsAllowed=toolsAllowed;
		this.itemsAllowed=itemsAllowed;
		this.recyclingCenterName=name;
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
	
	public String getRecyclingCenterName() {
		return recyclingCenterName;
	}
	
	public void setRecyclingCenterName(String name) {
		this.recyclingCenterName = name;
	}

	public Location getRecyclingCenterLocation() {
		return loc;
	}
	
	public String toString() {
		return "RecyclingCenterNode(Name="+recyclingCenterName+",x="+loc.getBlockX()+",y="+loc.getBlockY()+",z="+loc.getBlockZ()+",tools="+toolsAllowed+",itemsAllowed="+itemsAllowed+")";
	}
}	
