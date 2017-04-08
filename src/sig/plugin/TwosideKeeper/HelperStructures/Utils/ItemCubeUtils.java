package sig.plugin.TwosideKeeper.HelperStructures.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Hopper;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jgrapht.Graph;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import sig.plugin.TwosideKeeper.PlayerStructure;
import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.HelperStructures.CubeType;
import sig.plugin.TwosideKeeper.HelperStructures.ItemCube;
import sig.plugin.TwosideKeeper.HelperStructures.Common.ItemContainer;

public class ItemCubeUtils {
	public final static String SUCTION_STRING = ChatColor.GRAY+"Block Collection: ";
	public final static String FILTER_STRING = ChatColor.GRAY+"Filter Blocks: ";
	public static int getItemCubeID(ItemStack item) {
		if (isItemCube(item)) {
			return Integer.parseInt(ItemUtils.GetLoreLineContainingSubstring(item, ChatColor.DARK_PURPLE+"ID#").split("#")[1]);
		}
		return -1;
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
						List<ItemContainer> itemcube_list = new ArrayList<ItemContainer>();
						for (int i=0;i<cube_inv.getSize();i++) {
							itemslist.add(cube_inv.getItem(i));
							if (ItemUtils.isValidItem(cube_inv.getItem(i))) {
		    					boolean found=false;
		        				for (int j=0;j<itemcube_list.size();j++) {
		        					if (itemcube_list.get(j).getItem().isSimilar(cube_inv.getItem(i))) {
		        						itemcube_list.get(j).setAmount(itemcube_list.get(j).getAmount()+cube_inv.getItem(i).getAmount());
		        						found=true;
		        						break;
		        					}
		        				}
		        				if (!found) {
		    						itemcube_list.add(new ItemContainer(cube_inv.getItem(i)));
		        				}
							}
						}
						ItemCube.addToViewersOfItemCube(id,remaining,null);
						if (!testing) {
							TwosideKeeper.itemCube_saveConfig(id, itemslist);
			        		TwosideKeeper.itemcube_updates.put(id, itemcube_list);//This Item Cube can be saved.
						}
						
		    			ItemCubeUtils.addItemCubeToGraphFromCube(id, it, (Player)cube_inv.getHolder());
					} else {
						for (ItemStack i : extras.values()) {
							reject_items.put(reject_items.size(), i);
							List<ItemStack> itemslist = new ArrayList<ItemStack>();
							List<ItemContainer> itemcube_list = new ArrayList<ItemContainer>();
							for (int j=0;j<cube_inv.getSize();j++) {
								itemslist.add(cube_inv.getItem(j));
								if (ItemUtils.isValidItem(cube_inv.getItem(j))) {
			    					boolean found=false;
			        				for (int k=0;k<itemcube_list.size();k++) {
			        					if (itemcube_list.get(k).getItem().isSimilar(cube_inv.getItem(j))) {
			        						itemcube_list.get(k).setAmount(itemcube_list.get(k).getAmount()+cube_inv.getItem(j).getAmount());
			        						found=true;
			        						break;
			        					}
			        				}
			        				if (!found) {
			    						itemcube_list.add(new ItemContainer(cube_inv.getItem(j)));
			        				}
								}
							}
							if (!testing) {
								TwosideKeeper.itemCube_saveConfig(id, itemslist);
				        		TwosideKeeper.itemcube_updates.put(id, itemcube_list);//This Item Cube can be saved.
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
			if (p.getOpenInventory()!=null && p.getOpenInventory().getTopInventory()!=null && (p.getOpenInventory().getTopInventory().getType()==InventoryType.HOPPER ||
					p.getOpenInventory().getTopInventory().getType()==InventoryType.DROPPER /*Keep open for Bauble Pouches*/)) {
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
	
	@Deprecated
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
	
	public static List<ItemStack> addItems(int id, ItemStack...items) {
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
	
	public static List<ItemStack> removeItems(int id, ItemStack...items) {
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
				HashMap<Integer,ItemStack> remaining = inv.removeItem(tempitem.clone());
				if (remaining.size()>0) {
					leftovers.add(remaining.get(0).clone());
				}
				ItemCube.removeFromViewersofItemCube(id,tempitem.clone(),null);
			}
		}
		saveConfig(id,InventoryUtils.ConvertInventoryToList(inv,slots),size);
		return leftovers;
	}
	
	public static ItemStack removeItemFromSlot(int id, int slot) {
		List<ItemStack> currentcontents = getItemCubeContents(id);
		//Attempt to add them to an inventory.
		CubeType size = getCubeType(id);
		int slots = size.getSize();
		Inventory inv = Bukkit.createInventory(null, slots);
		for (int i=0;i<slots;i++) {
			inv.setItem(i, currentcontents.get(i));
		}
		ItemStack slotitem = inv.getItem(slot).clone();
		inv.setItem(slot, new ItemStack(Material.AIR));
		ItemCube.removeFromViewersofItemCube(id,slotitem.clone(),null);
		saveConfig(id,InventoryUtils.ConvertInventoryToList(inv,slots),size);
		if (slotitem==null) {
			return new ItemStack(Material.AIR);
		} else {
			return slotitem.clone();
		}
	}
	
	public static void clearItems(int id) {
		CubeType size = getCubeType(id);
		int slots = size.getSize();
		Inventory inv = Bukkit.createInventory(null, slots);
		for (int i=0;i<slots;i++) {
			inv.setItem(i, new ItemStack(Material.AIR));
		}
		ItemCube.clearFromViewersofItemCube(id,null);
		saveConfig(id,InventoryUtils.ConvertInventoryToList(inv,slots),size);
	}
	public static boolean isSuctionOn(int id) {
		File config;
		config = new File(TwosideKeeper.filesave,"itemcubes/ItemCube"+id+".data");
		FileConfiguration workable = YamlConfiguration.loadConfiguration(config);

		CubeType type = CubeType.getCubeTypeFromID(workable.getInt("cubetype"));
		if (type==CubeType.VACUUM) {
			if (workable.contains("suction")) {
				return workable.getBoolean("suction");
			} else {
				workable.set("suction", true);
				try {
					workable.save(config);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return true;
			}
		} else {
			return false;
		}
	}
	public static boolean isFilterOn(int id) {
		File config;
		config = new File(TwosideKeeper.filesave,"itemcubes/ItemCube"+id+".data");
		FileConfiguration workable = YamlConfiguration.loadConfiguration(config);

		CubeType type = CubeType.getCubeTypeFromID(workable.getInt("cubetype"));
		if (type==CubeType.FILTER) {
			if (workable.contains("filter")) {
				return workable.getBoolean("filter");
			} else {
				workable.set("filter", true);
				try {
					workable.save(config);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return true;
			}
		} else {
			return false;
		}
	}
	public static void toggleSuction(int id) {
		File config;
		config = new File(TwosideKeeper.filesave,"itemcubes/ItemCube"+id+".data");
		FileConfiguration workable = YamlConfiguration.loadConfiguration(config);

		CubeType type = CubeType.getCubeTypeFromID(workable.getInt("cubetype"));
		if (type==CubeType.VACUUM) {
			if (isSuctionOn(id)) {
				workable.set("suction",false);
			} else {
				workable.set("suction",true);
			}
			try {
				workable.save(config);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public static void toggleFilter(int id) {
		File config;
		config = new File(TwosideKeeper.filesave,"itemcubes/ItemCube"+id+".data");
		FileConfiguration workable = YamlConfiguration.loadConfiguration(config);

		CubeType type = CubeType.getCubeTypeFromID(workable.getInt("cubetype"));
		if (type==CubeType.FILTER) {
			if (isSuctionOn(id)) {
				workable.set("filter",false);
			} else {
				workable.set("filter",true);
			}
			try {
				workable.save(config);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public static void updateVacuumCubeSuctionLoreLine(ItemStack item) {
		if (getCubeType(ItemCubeUtils.getItemCubeID(item))==CubeType.VACUUM) {
			ItemUtils.DeleteAllLoreLinesAtAndAfterLineContainingSubstring(item, ChatColor.WHITE+"Contents (");
			if (ItemUtils.LoreContainsSubstring(item, SUCTION_STRING)) {
				ItemUtils.ModifyLoreLineContainingSubstring(item, SUCTION_STRING, SUCTION_STRING+(ItemCubeUtils.isSuctionOn(ItemCubeUtils.getItemCubeID(item))?ChatColor.GREEN+"ON":ChatColor.RED+"OFF"));
			} else {
				ItemUtils.addLore(item, SUCTION_STRING+(ItemCubeUtils.isSuctionOn(ItemCubeUtils.getItemCubeID(item))?ChatColor.GREEN+"ON":ChatColor.RED+"OFF"));
			}
		}
	}
	public static void updateFilterCubeFilterLoreLine(ItemStack item) {
		if (getCubeType(ItemCubeUtils.getItemCubeID(item))==CubeType.FILTER) {
			ItemUtils.DeleteAllLoreLinesAtAndAfterLineContainingSubstring(item, ChatColor.WHITE+"Contents (");
			if (ItemUtils.LoreContainsSubstring(item, FILTER_STRING)) {
				ItemUtils.ModifyLoreLineContainingSubstring(item, FILTER_STRING, FILTER_STRING+(ItemCubeUtils.isFilterOn(ItemCubeUtils.getItemCubeID(item))?ChatColor.GREEN+"ON":ChatColor.RED+"OFF"));
			} else {
				ItemUtils.addLore(item, FILTER_STRING+(ItemCubeUtils.isFilterOn(ItemCubeUtils.getItemCubeID(item))?ChatColor.GREEN+"ON":ChatColor.RED+"OFF"));
			}
		}
	}
	public static void updateItemCubeUpdateList(ItemStack item) {
		List<ItemContainer> itemcube_list = new ArrayList<ItemContainer>();
		for (ItemStack items : ItemCubeUtils.getItemCubeContents(ItemCubeUtils.getItemCubeID(item))) {
			if (ItemUtils.isValidItem(items)) {
				boolean found=false;
				for (int j=0;j<itemcube_list.size();j++) {
					if (itemcube_list.get(j).getItem().isSimilar(items)) {
						itemcube_list.get(j).setAmount(itemcube_list.get(j).getAmount()+items.getAmount());
						found=true;
						break;
					}
				}
				if (!found) {
					itemcube_list.add(new ItemContainer(items));
				}
			}
		}
		TwosideKeeper.itemcube_updates.put(ItemCubeUtils.getItemCubeID(item), itemcube_list);
	}
	public static void populateItemCubeGraph(Player p) {
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		UndirectedGraph<Integer,DefaultEdge> graph = TwosideKeeper.itemCubeGraph;
		
		graph.addVertex(PlayerStructure.getPlayerNegativeHash(p)); //Root Vertex.
		
		for (ItemStack it : p.getInventory().getContents()) {
			if (ItemUtils.isValidItem(it) && isItemCube(it)) {
				int id = getItemCubeID(it);
				if (!graph.containsVertex(id)) {
					graph.addVertex(id);
					graph.addEdge(PlayerStructure.getPlayerNegativeHash(p), id);
					IterateAndAddToGraph(id,graph);
				}
			}
		}
		
		for (DefaultEdge edge : graph.edgeSet()) {
			TwosideKeeper.log(" "+edge.toString(), 0);
		}
	}
	
	public static void setupGraphForChest(Inventory inventory) {
		if (inventory!=null && inventory.getHolder() instanceof Player &&
				(PlayerStructure.GetPlayerStructure(((Player)inventory.getHolder())).isViewingItemCube ||
				PlayerStructure.GetPlayerStructure(((Player)inventory.getHolder())).opened_another_cube)&&
				inventory.getTitle()!=null && inventory.getTitle().contains("Item Cube #")) {
			return; //This is an item cube, don't setup a graph for it.
		}
		UndirectedGraph<Integer,DefaultEdge> graph = TwosideKeeper.itemCubeGraph;
		
		graph.addVertex(InventoryUtils.getInventoryNumberHash(inventory)); //Root Vertex.
		
		for (ItemStack it : inventory.getContents()) {
			if (ItemUtils.isValidItem(it) && isItemCube(it)) {
				int id = getItemCubeID(it);
				graph.addVertex(id);
				graph.addEdge(InventoryUtils.getInventoryNumberHash(inventory), id);
				IterateAndAddToGraph(id,graph);
			}
		}
		
		for (DefaultEdge edge : graph.edgeSet()) {
			TwosideKeeper.log(" "+edge, TwosideKeeper.GRAPH_DEBUG);
		}
	}
	
	public static void IterateAndAddToGraph(int id, UndirectedGraph<Integer, DefaultEdge> graph) {
		IterateAndAddToGraph(id,graph,new ArrayList<Integer>());
	}
	
	public static void IterateAndAddToGraph(int id, UndirectedGraph<Integer, DefaultEdge> graph, List<Integer> ids) {
		List<ItemStack> contents = getItemCubeContents(id);
		for (ItemStack it : contents) {
			if (ItemUtils.isValidItem(it) && isItemCube(it)) {
				int newid = getItemCubeID(it);
				if (id!=newid && !ids.contains(newid)) { //We don't need to link to itself.
					graph.addVertex(newid);
					graph.addEdge(id, newid);
					ids.add(newid);
					IterateAndAddToGraph(newid,graph,ids);
				}
			}
		}
	}
	
	public static void pickupAndAddItemCubeToGraph(ItemStack item, Player p) {
		if (ItemCubeUtils.isItemCube(item)) {
    		int id = ItemCubeUtils.getItemCubeID(item);
    		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
    		UndirectedGraph<Integer,DefaultEdge> graph = TwosideKeeper.itemCubeGraph;
    		graph.addVertex(id);
    		DefaultEdge edge = graph.addEdge(PlayerStructure.getPlayerNegativeHash(p), id);
    		TwosideKeeper.log("Added edge "+edge, TwosideKeeper.GRAPH_DEBUG);
    		ItemCubeUtils.IterateAndAddToGraph(id, graph);
    	}
	}
	
	public static void addItemCubeToGraphFromCube(int sourceCubeID, ItemStack item, Player p) {
		if (ItemCubeUtils.isItemCube(item)) {
    		int id = ItemCubeUtils.getItemCubeID(item);
    		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
    		UndirectedGraph<Integer,DefaultEdge> graph = TwosideKeeper.itemCubeGraph;
    		graph.addVertex(id);
    		DefaultEdge edge = graph.addEdge(sourceCubeID, id);
    		TwosideKeeper.log("Added edge "+edge, TwosideKeeper.GRAPH_DEBUG);
    		ItemCubeUtils.IterateAndAddToGraph(id, graph);
    	}
	}
	
	public static void removeAndUpdateAllEdgesDroppedItem(int id, Player p) {
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		UndirectedGraph<Integer,DefaultEdge> graph = TwosideKeeper.itemCubeGraph;
		DestroyAllSourceEdges(PlayerStructure.getPlayerNegativeHash(p), graph);
		reestablishAllRootEdges(p, graph);
	}
	
	public static void removeAndUpdateAllEdges(int id, Player p) {
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		UndirectedGraph<Integer,DefaultEdge> graph = TwosideKeeper.itemCubeGraph;
		DestroyAllSourceEdges(id, graph);
		reestablishAllEdges(id, graph);
	}
	
	private static void reestablishAllEdges(int id, UndirectedGraph<Integer, DefaultEdge> graph) {
		List<ItemStack> contents = getItemCubeContents(id);
		for (ItemStack it : contents) {
			if (ItemUtils.isValidItem(it) && isItemCube(it)) {
				int newid = getItemCubeID(it);
				if (id!=newid) { //We don't need to link to itself.
					graph.addVertex(newid);
					DefaultEdge edge = graph.addEdge(id, newid);
		    		TwosideKeeper.log("Reconnected edge "+edge, TwosideKeeper.GRAPH_DEBUG);
				}
			}
		}
	}
	
	private static void reestablishAllRootEdges(Player p, UndirectedGraph<Integer, DefaultEdge> graph) {
		for (ItemStack it : p.getInventory().getContents()) {
			if (ItemUtils.isValidItem(it) && isItemCube(it)) {
				int newid = getItemCubeID(it);
				graph.addVertex(newid);
				DefaultEdge edge = graph.addEdge(PlayerStructure.getPlayerNegativeHash(p), newid);
	    		TwosideKeeper.log("Reconnected edge "+edge, TwosideKeeper.GRAPH_DEBUG);
			}
		}
	}
	public static void DestroyAllSourceEdges(int id, UndirectedGraph<Integer, DefaultEdge> graph) {
		Set<DefaultEdge> edges = graph.edgesOf(id);
		List<DefaultEdge> destroyed = new ArrayList<DefaultEdge>();
		for (DefaultEdge e : edges) {
			if (graph.getEdgeSource(e)==id) {
				destroyed.add(e);
			}
		}
		while (destroyed.size()>0) {
			DefaultEdge edge = destroyed.remove(0);
    		TwosideKeeper.log("Destroyed edge "+edge, TwosideKeeper.GRAPH_DEBUG);
			graph.removeEdge(edge);
		}
	}
	public static void DestroyAllTargetEdges(int id, UndirectedGraph<Integer, DefaultEdge> graph) {
		Set<DefaultEdge> edges = graph.edgesOf(id);
		List<DefaultEdge> destroyed = new ArrayList<DefaultEdge>();
		for (DefaultEdge e : edges) {
			if (graph.getEdgeTarget(e)==id) {
				destroyed.add(e);
			}
		}
		while (destroyed.size()>0) {
			DefaultEdge edge = destroyed.remove(0);
    		TwosideKeeper.log("Destroyed edge "+edge, TwosideKeeper.GRAPH_DEBUG);
			graph.removeEdge(edge);
			
		}
	}
	
	/*public static void removeItemCubeFromGraph(int sourceCubeID, ItemStack item, Player p) {
		
	}*/
	/*public static void handleInventoryClickWithGraphs(InventoryClickEvent ev) {
		Player p = (Player)ev.getWhoClicked();
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		boolean isViewingItemCube = pd.isViewingItemCube;
		Inventory inv = ev.getClickedInventory();
		
		if (inv.getType()==InventoryType.CHEST) {
			if (isViewingItemCube && 
					inv.getTitle()!=null && inv.getTitle().contains("Item Cube #")) {
				
			}
		}
	}*/
	public static boolean isConnectedToRootNode(Graph<Integer,DefaultEdge> g, Integer vertex) {
		return isConnectedToRootNode(g,vertex,new ArrayList<DefaultEdge>());
	}
	
	private static boolean isConnectedToRootNode(Graph<Integer,DefaultEdge> g, Integer vertex, List<DefaultEdge> vals) {
		Set<DefaultEdge> edges = g.edgesOf(vertex);
		TwosideKeeper.log("Checking all edges connected to vertex "+vertex, TwosideKeeper.GRAPH_DEBUG2);
		for (DefaultEdge e : edges) {
			Integer newvertex = g.getEdgeSource(e);
			TwosideKeeper.log("EDGE: "+e+" || Vertex: "+vertex+" -> "+newvertex+" ",TwosideKeeper.GRAPH_DEBUG2);
			if (!vals.contains(e)) {
				TwosideKeeper.log(e.toString(),TwosideKeeper.GRAPH_DEBUG);
				vals.add(e);
				if (newvertex<0) {
					TwosideKeeper.log("Is connected to root node.",TwosideKeeper.GRAPH_DEBUG2);
					return true;
				} else {
					return isConnectedToRootNode(g,newvertex,vals);
				}
			}
		}
		return false;
	}
	public static int ParseItemCubeInventoryID(Inventory destination) {
		return Integer.parseInt(destination.getTitle().split("#")[1]);
	}
	public static boolean IsItemCubeInventory(Inventory destination) {
		return destination!=null && destination.getHolder() instanceof Player &&
				(PlayerStructure.GetPlayerStructure(((Player)destination.getHolder())).isViewingItemCube ||
				PlayerStructure.GetPlayerStructure(((Player)destination.getHolder())).opened_another_cube)&&
				destination.getTitle()!=null && destination.getTitle().contains("Item Cube #");
	}
}
