package sig.plugin.TwosideKeeper;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import sig.plugin.TwosideKeeper.HelperStructures.RecyclingCenterNode;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.ItemUtils;

public class RecyclingCenter {
	//Each Recycling center has nodes which contain all the chests.
	List<RecyclingCenterNode> nodes;
	HashMap<Material,Integer> itemmap;
	int totalitems=0;
	final static int CONFIGFILE_VERSION = 2;
	
	boolean choosing = false;
	
	public RecyclingCenter() {
		nodes = new ArrayList<RecyclingCenterNode>();
		itemmap = new HashMap<Material,Integer>();
	}
	
	public void AddNode(World world, int locx,int locy,int locz,String name,boolean toolsAllowed,boolean itemsAllowed) {
		nodes.add(new RecyclingCenterNode(new Location(world,locx,locy,locz),name,toolsAllowed,itemsAllowed));
	}
	
	/**
	 * 
	 * @param numb The number in the list of the node you want.
	 * @return The Location of the node requested.
	 */
	public Location getNodeLocation(int numb) {
		return nodes.get(numb).getRecyclingCenterLocation();
	}
	
	public RecyclingCenterNode getRandomNode() {
		if (nodes.size()>0) {
			return nodes.get((int)(Math.floor(Math.random()*nodes.size())));
		} else {
			return null;
		}
	}
	
	public int getNumberOfNodes() {
		return nodes.size();
	}
	
	public boolean IsItemAllowed(ItemStack item) {
		//Artifact type of items are not allowed to be sent to the Recycling Center. Only artifact equipment will be sent over.
		if (ItemUtils.LoreContainsSubstring(item, "WorldShop Display Item") || (Artifact.isArtifact(item) && !GenericFunctions.isArtifactEquip(item) && !ItemUtils.isArtifactDust(item))) {
			return false;
		}
		return true;
	}
	
	public void loadConfig() {
		File config= new File(TwosideKeeper.filesave,"recyclingcenters.data");
		if (config.exists()) {
			TwosideKeeper.log("Config exists. Entering.",5);
			FileConfiguration workable = YamlConfiguration.loadConfiguration(config);
			if (workable.getInt("version",0)>=2) {
				int nodecount = workable.getInt("nodeCount",0);
				for (int i=0;i<nodecount;i++) {
					this.AddNode(Bukkit.getWorld(workable.getString("world"+i)), workable.getInt("blockx"+i), workable.getInt("blocky"+i), workable.getInt("blockz"+i), workable.getString("name"+i), workable.getBoolean("toolsAllowed"+i,true), workable.getBoolean("itemsAllowed"+i,true));
				}
			} else
			if (workable.getInt("version",0)>=1) { //Default version is 0. So if we can't find the version key, then we know we have to set it up.
				int nodecount = workable.getInt("nodeCount",0);
				for (int i=0;i<nodecount;i++) {
					this.AddNode(Bukkit.getWorld(workable.getString("world"+i)), workable.getInt("blockx"+i), workable.getInt("blocky"+i), workable.getInt("blockz"+i), "Recycling Center", workable.getBoolean("toolsAllowed"+i,true), workable.getBoolean("itemsAllowed"+i,true));
				}
			} else {
				for (int i=0;i<workable.getKeys(false).size()/4;i++) {
					this.AddNode(Bukkit.getWorld(workable.getString("world"+i)), workable.getInt("blockx"+i), workable.getInt("blocky"+i), workable.getInt("blockz"+i), "Recycling Center", true,true);
				}
			}
		}
	}
	
	public void setChoosingRecyclingCenter(boolean choosing) {
		this.choosing=choosing;
	}
	
	public boolean isChoosingRecyclingCenter() {
		return choosing;
	}
	
	public void saveConfig() {
		File config;
		config = new File(TwosideKeeper.filesave,"recyclingcenters.data");
		FileConfiguration workable = YamlConfiguration.loadConfiguration(config);
		
		workable.set("version", CONFIGFILE_VERSION);
		workable.set("nodeCount", nodes.size());
		
		//workable.set("recycling_center.count", nodes.size());
		
		for (int i=0;i<nodes.size();i++) {
			workable.set("world"+i, nodes.get(i).getRecyclingCenterLocation().getWorld().getName());
			workable.set("blockx"+i, nodes.get(i).getRecyclingCenterLocation().getBlockX());
			workable.set("blocky"+i, nodes.get(i).getRecyclingCenterLocation().getBlockY());
			workable.set("blockz"+i, nodes.get(i).getRecyclingCenterLocation().getBlockZ());
			workable.set("name"+i, nodes.get(i).getRecyclingCenterName());
			workable.set("toolsAllowed"+i, nodes.get(i).areToolsAllowed());
			workable.set("itemsAllowed"+i, nodes.get(i).areItemsAllowed());
		}
		
		try {
			workable.save(config);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void populateItemListFromAllNodes() {
		for (int i=0;i<getNumberOfNodes();i++) {
			Location node = getNodeLocation(i);
			node.getWorld().loadChunk(node.getChunk());
			Block b = node.getBlock();
			if (b!=null && (b.getType()==Material.CHEST || b.getType()==Material.TRAPPED_CHEST)) {
				if (b.getState()!=null) {
					Chest c = (Chest)b.getState();
					for (int j=0;j<27;j++) {
						ItemStack item = c.getBlockInventory().getItem(j);
						if (item!=null) {
							populateItemList(item);
						}
					}
				}
			}
		}
		if (totalitems<100) {
			totalitems=100;
		}
		TwosideKeeper.log("Populated Recycled Item List with "+totalitems+" items.", 2);
	}
	

	public void populateItemListFromNode(Location node) {
		node.getWorld().loadChunk(node.getChunk());
		Block b = node.getBlock();
		if (b!=null && (b.getType()==Material.CHEST || b.getType()==Material.TRAPPED_CHEST)) {
			if (b.getState()!=null) {
				Chest c = (Chest)b.getState();
				for (int j=0;j<27;j++) {
					ItemStack item = c.getBlockInventory().getItem(j);
					if (item!=null) {
						populateItemList(item);
					}
				}
			}
		}
		TwosideKeeper.log("Populated Recycled Item List with "+totalitems+" items.", 2);
	}
	
	public void populateItemList(ItemStack item) {
		if (itemmap.containsKey(item.getType())) {
			int amt = itemmap.get(item.getType());
			itemmap.put(item.getType(), amt+item.getAmount());
			totalitems+=item.getAmount();
		} else {
			itemmap.put(item.getType(),1);
			totalitems++;
		}
	}
	
	public static boolean isRecyclingCenter(Block b) {
		for (RecyclingCenterNode node : TwosideKeeper.TwosideRecyclingCenter.nodes) {
			Block b2 = node.getRecyclingCenterLocation().getBlock();
			if (b2.equals(b)) {
				return true;
			}
		}
		return false;
		//return TwosideKeeper.TwosideRecyclingCenter.nodes.contains(new Location(b.getWorld(),b.getLocation().getBlockX(),b.getLocation().getBlockY(),b.getLocation().getBlockZ()));
	}
	
	public void AddItemToRecyclingCenter(ItemStack i) {
		//There is a % chance of it going to a recycling center.
    	if (IsItemAllowed(i)) {
    		//Recycle allowed. Now figure out which node to go to.
    		if (getNumberOfNodes()>0) {
    			boolean satisfies=false;
    			int trycount=0;
				RecyclingCenterNode rand_node=null;
				do {
					rand_node = getRandomNode();
					if (!rand_node.areItemsAllowed() || (GenericFunctions.isTool(i) && !rand_node.areToolsAllowed())) {
						trycount++;
					} else {
						satisfies=true;
					}
				}
    			while (trycount<100 && !satisfies);
	    		rand_node.getRecyclingCenterLocation().getWorld().loadChunk(rand_node.getRecyclingCenterLocation().getChunk()); //Load that chunk to make sure we can throw items into it.
	    		Block b = rand_node.getRecyclingCenterLocation().getWorld().getBlockAt(rand_node.getRecyclingCenterLocation());
	    		if (b!=null && b.getType()==Material.CHEST ||
	    				b.getType()==Material.TRAPPED_CHEST) {
	    			if (b.getState()!=null) {
	    				Chest c = (Chest) b.getState();
	    				int itemslot = RandomlyChooseEmptySpot(c.getBlockInventory());
	    				//ItemStack oldItem = c.getBlockInventory().getItem(itemslot);
	    				//There is also a chance to move this item to another random spot.
	    				if (!isCommon(i.getType()) || mustBeRecycled(i)) {
		    		    	/*if (oldItem!=null && (Math.random()*100<=TwosideKeeper.RECYCLECHANCE || mustBeRecycled(i))) {
		        				int itemslot2 = (int)Math.floor(Math.random()*27);
		        				c.getBlockInventory().setItem(itemslot2, oldItem);
		    		    	}*/
		    				c.getBlockInventory().setItem(itemslot, i);
		    	    		populateItemList(i);
		    	    		if (TwosideKeeper.LOGGING_LEVEL>=2) {
		    	    			TwosideKeeper.log("Sent "+ChatColor.AQUA+i.toString()+ChatColor.RESET+" to Recycling Center Node "+rand_node.toString(),2);
		    	    		} else {
		    	    		if (TwosideKeeper.LOGGING_LEVEL>=1) {
		    	    			TwosideKeeper.log("Sent "+ChatColor.AQUA+GenericFunctions.UserFriendlyMaterialName(i)+((i.getAmount()>1)?ChatColor.YELLOW+" x"+i.getAmount():"")+ChatColor.RESET+" to Recycling Center Node "+rand_node.toString(),1);
		    	    		} else {
		    	    			TwosideKeeper.log("Sent "+ChatColor.AQUA+GenericFunctions.UserFriendlyMaterialName(i)+((i.getAmount()>1)?ChatColor.YELLOW+" x"+i.getAmount():"")+ChatColor.RESET+" to Recycling Center.",0);
		    	    		}
	    				}
	    			}
	    		}
    		} else {
    			TwosideKeeper.log("No Recycling Center Nodes set! All dropped items will continue to be discarded. Use /recyclingcenter to define them.",1);
    		}
    		} 
    	}
	}
	
	private int RandomlyChooseEmptySpot(Inventory blockInventory) {
		int i=54;
		while (i>0) {
			int randomslot = (int)Math.floor(Math.random()*27);
			ItemStack item = blockInventory.getItem(randomslot);
			if (item==null || item.getType()==Material.AIR) {
				//This is empty.
				return randomslot;
			}
			i--;
		}
		return (int)Math.floor(Math.random()*27);
	}

	public static boolean mustBeRecycled(ItemStack it) {
		if ((GenericFunctions.isArtifactEquip(it) || ItemUtils.isArtifactDust(it))) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isCommon(Material m) {
		if (itemmap.containsKey(m)) {
			int amt = itemmap.get(m);
			double chance = (amt/(double)totalitems*100d);
			if (totalitems<200) {
				chance=1.00;
			}
			if (totalitems>0 && chance>=TwosideKeeper.COMMONITEMPCT) {
				DecimalFormat df = new DecimalFormat("0.00");
				TwosideKeeper.log(df.format(chance)+"% of items in nodes are "+GenericFunctions.UserFriendlyMaterialName(m)+". Common item detected...", 3);
				return true;
			}
		}
		return false;
	}
}
