package sig.plugin.TwosideKeeper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;

public class RecyclingCenter {
	//Each Recycling center has nodes which contain all the chests.
	List<Location> nodes;
	
	boolean choosing = false;
	
	public RecyclingCenter() {
		nodes = new ArrayList<Location>();
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
	
	public static boolean isRecyclingCenter(Block b) {
		return TwosideKeeper.TwosideRecyclingCenter.nodes.contains(new Location(b.getWorld(),b.getLocation().getBlockX(),b.getLocation().getBlockY(),b.getLocation().getBlockZ()));
	}
}
