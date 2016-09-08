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
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;

public class RecyclingCenter {
	//Each Recycling center has nodes which contain all the chests.
	List<Location> nodes;
	HashMap<Material,Integer> itemmap;
	int totalitems=0;
	
	boolean choosing = false;
	
	public RecyclingCenter() {
		nodes = new ArrayList<Location>();
		itemmap = new HashMap<Material,Integer>();
	}
	
	public void AddNode(World world, int locx,int locy,int locz) {
		nodes.add(new Location(world,locx,locy,locz));
	}
	
	/**
	 * 
	 * @param numb The number in the list of the node you want.
	 * @return The Location of the node requested.
	 */
	public Location getNodeLocation(int numb) {
		return nodes.get(numb);
	}
	
	public Location getRandomNode() {
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
		if (Artifact.isArtifact(item) && !GenericFunctions.isArtifactEquip(item)) {
			return false;
		}
		return true;
	}
	
	public void loadConfig() {
		File config= new File(TwosideKeeper.filesave,"recyclingcenters.data");
		if (config.exists()) {
			TwosideKeeper.log("Config exists. Entering.",5);
			FileConfiguration workable = YamlConfiguration.loadConfiguration(config);
			for (int i=0;i<workable.getKeys(false).size()/4;i++) {
				this.AddNode(Bukkit.getWorld(workable.getString("world"+i)), workable.getInt("blockx"+i), workable.getInt("blocky"+i), workable.getInt("blockz"+i));
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
		
		//workable.set("recycling_center.count", nodes.size());
		
		for (int i=0;i<nodes.size();i++) {
			workable.set("world"+i, nodes.get(i).getWorld().getName());
			workable.set("blockx"+i, nodes.get(i).getBlockX());
			workable.set("blocky"+i, nodes.get(i).getBlockY());
			workable.set("blockz"+i, nodes.get(i).getBlockZ());
		}
		
		try {
			workable.save(config);
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
		return TwosideKeeper.TwosideRecyclingCenter.nodes.contains(new Location(b.getWorld(),b.getLocation().getBlockX(),b.getLocation().getBlockY(),b.getLocation().getBlockZ()));
	}
	
	public void AddItemToRecyclingCenter(ItemStack i) {
		//There is a % chance of it going to a recycling center.
    	if (IsItemAllowed(i)) {
    		//Recycle allowed. Now figure out which node to go to.
    		if (getNumberOfNodes()>0) {
	    		Location rand_node=getRandomNode();
	    		rand_node.getWorld().loadChunk(rand_node.getChunk()); //Load that chunk to make sure we can throw items into it.
	    		Block b = rand_node.getWorld().getBlockAt(rand_node);
	    		if (b!=null && b.getType()==Material.CHEST ||
	    				b.getType()==Material.TRAPPED_CHEST) {
	    			if (b.getState()!=null) {
	    				Chest c = (Chest) b.getState();
	    				for (int j=0;j<27;j++) {
	    					if (c.getBlockInventory().getItem(j)!=null && c.getBlockInventory().getItem(j).getType()==i.getType()) {
	    					}
	    				}
	    				int itemslot = (int)Math.floor(Math.random()*27);
	    				ItemStack oldItem = c.getBlockInventory().getItem(itemslot);
	    				//There is also a chance to move this item to another random spot.
	    				if (!isCommon(i.getType())) {
		    		    	if (oldItem!=null && Math.random()*100<=TwosideKeeper.RECYCLECHANCE) {
		        				int itemslot2 = (int)Math.floor(Math.random()*27);
		        				c.getBlockInventory().setItem(itemslot2, oldItem);
		    		    	}
		    				c.getBlockInventory().setItem(itemslot, i);
		    	    		populateItemList(i);
		    				TwosideKeeper.log("Sent "+ChatColor.AQUA+GenericFunctions.UserFriendlyMaterialName(i)+((i.getAmount()>1)?ChatColor.YELLOW+" x"+i.getAmount():"")+ChatColor.RESET+" to Recycling Center Node "+rand_node.toString(),2);
	    				}
	    			}
	    		}
    		} else {
    			TwosideKeeper.log("No Recycling Center Nodes set! All dropped items will continue to be discarded. Use /recyclingcenter to define them.",1);
    		}
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
