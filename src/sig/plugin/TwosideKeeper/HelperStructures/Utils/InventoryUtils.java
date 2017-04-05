package sig.plugin.TwosideKeeper.HelperStructures.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.HelperStructures.CubeType;
import sig.plugin.TwosideKeeper.HelperStructures.CustomItem;
import sig.plugin.TwosideKeeper.HelperStructures.FilterCubeItem;
import sig.plugin.TwosideKeeper.HelperStructures.ItemCube;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;
import sig.plugin.TwosideKeeper.HelperStructures.Common.ItemContainer;
import sig.plugin.TwosideKeeper.HolidayEvents.Christmas;

public class InventoryUtils {
	public static boolean isCarryingVacuumCube(Player p) {
		for (ItemStack items : p.getInventory().getContents()) {
			if (items!=null && CustomItem.isVacuumCube(items) && ItemCubeUtils.isSuctionOn(ItemCubeUtils.getItemCubeID(items))) {
				return true;
			}
		}
		return false;
	}
	public static ItemStack[] insertItemsInVacuumCube(Player p,ItemStack...items) {
		ItemStack[] remaining = items;
		for (ItemStack itemStacks : p.getInventory().getContents()) {
			if (itemStacks!=null && CustomItem.isVacuumCube(itemStacks) && ItemCubeUtils.isSuctionOn(ItemCubeUtils.getItemCubeID(itemStacks))) {
				//Insert as many items as possible in here.
				int id = Integer.parseInt(ItemUtils.GetLoreLineContainingSubstring(itemStacks, ChatColor.DARK_PURPLE+"ID#").split("#")[1]);
				List<ItemStack> itemCubeContents = TwosideKeeper.itemCube_loadConfig(id);
				Inventory virtualinventory = Bukkit.createInventory(p, itemCubeContents.size());
				for (int i=0;i<virtualinventory.getSize();i++) {
					if (itemCubeContents.get(i)!=null) {
						virtualinventory.setItem(i, itemCubeContents.get(i));
					}
				}
				//TwosideKeeper.log("Items: "+ArrayUtils.toString(remaining), 0);
				HashMap<Integer,ItemStack> remainingitems = virtualinventory.addItem(remaining);
				List<ItemStack> itemslist = new ArrayList<ItemStack>();
				List<ItemContainer> itemcube_list = new ArrayList<ItemContainer>();
				for (int i=0;i<virtualinventory.getSize();i++) {
					itemslist.add(virtualinventory.getItem(i));
					
					if (ItemUtils.isValidItem(virtualinventory.getItem(i))) {
    					boolean found=false;
        				for (int j=0;j<itemcube_list.size();j++) {
        					if (itemcube_list.get(j).getItem().isSimilar(virtualinventory.getItem(i))) {
        						itemcube_list.get(j).setAmount(itemcube_list.get(j).getAmount()+virtualinventory.getItem(i).getAmount());
        						found=true;
        						break;
        					}
        				}
        				if (!found) {
    						itemcube_list.add(new ItemContainer(virtualinventory.getItem(i)));
        				}
					}
				}
				
				Inventory collectionOfItems = AddItemsThatHaveBeenAddedToOurInventoryForOtherVacuumCubeViewers(p,
						remaining, itemCubeContents, remainingitems);

				if (remainingitems.size()==0) {
	        		ItemCubeUtils.addItemCubeToGraphFromCube(id,remaining[0],p);
				}
				//TwosideKeeper.log(Arrays.toString(collectionOfItems.getContents()), 0);
				
				ItemCube.addToViewersOfItemCube(id,collectionOfItems.getContents(),null);
				TwosideKeeper.itemCube_saveConfig(id, itemslist, CubeType.VACUUM);
        		TwosideKeeper.itemcube_updates.put(id, itemcube_list);//This Item Cube can be saved.
        		
        		/*for (ItemStack i : remainingitems.values()) {
					TwosideKeeper.log("Item "+i+" remains", 0);
				}*/
				remaining = remainingitems.values().toArray(new ItemStack[0]);
				//TwosideKeeper.log("Remaining items: "+ArrayUtils.toString(remaining), 0);
				GenericFunctions.UpdateItemLore(itemStacks);
			}
		}
		return remaining;
	}
	public static Inventory AddItemsThatHaveBeenAddedToOurInventoryForOtherVacuumCubeViewers(Player p,
			ItemStack[] remaining, List<ItemStack> itemCubeContents, HashMap<Integer, ItemStack> remainingitems) {
		Inventory collectionOfItems = Bukkit.createInventory(p, itemCubeContents.size());
		
		for (int i=0;i<remaining.length;i++) {
			collectionOfItems.addItem(remaining[i].clone());
		}
		
		for (int number : remainingitems.keySet()) {
			ItemStack it = remainingitems.get(number);
			collectionOfItems.removeItem(it);
		}
		return collectionOfItems;
	}
	public static boolean isCarryingFilterCube(Player p) {
		for (ItemStack items : p.getInventory().getContents()) {
			if (items!=null && CustomItem.isFilterCube(items)) {
				return true;
			}
		}
		return false;
	}
	public static ItemStack[] insertItemsInFilterCube(Player p,ItemStack...items) {
		ItemStack[] remaining = items;
		/*for (ItemStack itemStacks : p.getInventory().getContents()) {
			if (itemStacks!=null && CustomItem.isFilterCube(itemStacks)) {
				//Insert as many items as possible in here.
				int id = Integer.parseInt(ItemUtils.GetLoreLineContainingSubstring(itemStacks, ChatColor.DARK_PURPLE+"ID#").split("#")[1]);
				List<ItemStack> itemCubeContents = TwosideKeeper.itemCube_loadConfig(id);
				Inventory virtualinventory = Bukkit.createInventory(p, 27);
				for (int i=0;i<virtualinventory.getSize();i++) {
					if (itemCubeContents.get(i)!=null) {
						virtualinventory.setItem(i, itemCubeContents.get(i));
					}
				}
				//THIS IS WHERE YOU DO THE FILTERING.
				HashMap<Integer,ItemStack> remainingitems = ItemCubeUtils.AttemptingToAddItemToFilterCube(id,virtualinventory,remaining);
				
				remaining = remainingitems.values().toArray(new ItemStack[0]);
				GenericFunctions.UpdateItemLore(itemStacks);
			}
		}*/
		for (int j=0;j<remaining.length;j++) {
			if (FilterCubeItem.ItemHasFilterCube(remaining[j], p)) {
				for (Integer id : FilterCubeItem.getFilterCubeIDsToInsertItem(remaining[j], p)) {
					//Insert as many items as possible in here.
					List<ItemStack> itemCubeContents = TwosideKeeper.itemCube_loadConfig(id);
					Inventory virtualinventory = Bukkit.createInventory(p, 27);
					for (int i=0;i<virtualinventory.getSize();i++) {
						if (itemCubeContents.get(i)!=null) {
							virtualinventory.setItem(i, itemCubeContents.get(i));
						}
					}
					//THIS IS WHERE YOU DO THE FILTERING.
					HashMap<Integer,ItemStack> remainingitems = ItemCubeUtils.AttemptingToAddItemToFilterCube(id,virtualinventory,remaining);

					GenericFunctions.UpdateItemLore(remaining[j]);
					remaining = remainingitems.values().toArray(new ItemStack[0]);
				}
			}
		}
		return remaining;
	}
	public static boolean InventoryContainSameMaterial(Inventory inv, ItemStack item) {
		for (ItemStack i : inv.getContents()) {
			if (i!=null && item!=null && i.getType()==item.getType() && (
					(i.getItemMeta().hasLore() && item.getItemMeta().hasLore()) ||
					(!i.getItemMeta().hasLore() && !item.getItemMeta().hasLore()))) {
				return true;
			}
		}
		return false;
	}
	public static boolean hasFullInventory(Player p) {
		ItemStack[] inv = p.getInventory().getStorageContents();
		for (ItemStack i : inv) {
			if (i==null || i.getType()==Material.AIR) {
				return false;
			}
		}
		return true;
	}
	public static boolean hasFullInventory(Inventory inv) {
		ItemStack[] inventory = inv.getContents();
		for (ItemStack i : inventory) {
			if (i==null || i.getType()==Material.AIR) {
				return false;
			}
		}
		return true;
	}
	public static boolean hasEmptyInventory(Inventory inv) {
		ItemStack[] inventory = inv.getContents();
		for (ItemStack i : inventory) {
			if (i!=null && i.getType()!=Material.AIR) {
				TwosideKeeper.log("Item is "+i, 0);
				return false;
			}
		}
		return true;
	}
	public static boolean hasEmptyInventory(Player p) {
		ItemStack[] inv = p.getInventory().getStorageContents();
		for (ItemStack i : inv) {
			if (i!=null && i.getType()!=Material.AIR) {
				return false;
			}
		}
		return true;
	}
	public static ItemStack getFirstItemThatIsNotEmpty(Inventory inv) {
		ItemStack[] inventory = inv.getContents();
		for (ItemStack it : inventory) {
			if (it!=null && it.getType()!=Material.AIR) {
				return it;
			}
		}
		return null;
	}
	public static DirtBlockReply onlyHoldingFiveDirtBlocks(Player p) {
		Inventory inv = p.getInventory();
		int dirtblockcount=0;
		for (ItemStack i : inv.getContents()) {
			if (i!=null) {
				if (i.getType()==Material.DIRT) {
					dirtblockcount+=i.getAmount();
				} else {
					if (!Christmas.isCookieItem(i)) {
						return DirtBlockReply.NOTEMPTYINVENTORY;
					}
				}
			}
		}
		if (dirtblockcount==5) {
			return DirtBlockReply.HOLDING5DIRT;
		} else 
		if (dirtblockcount<5) {
			return DirtBlockReply.NOTENOUGHDIRT;
		} else 
		{
			return DirtBlockReply.TOOMUCHDIRT;
		}
	}
	public static boolean onlyHoldingRacingItems(Player p) {
		Inventory inv = p.getInventory();
		for (ItemStack i : inv.getContents()) {
			if (i!=null && i.getType()!=Material.AIR) {
				if (!(i.getType()==Material.BOW ||
						i.getType().name().contains("SWORD") ||
						Christmas.isCookieItem(i))) {
					return false;
				}
			}
		}
		return true;
	}
	public static ItemStack AttemptToFillPartialSlotsFirst(Player p, ItemStack itemStackAdded) {
		Inventory inv = p.getInventory();
		for (int i=0;i<inv.getSize();i++) {
			if (inv.getItem(i)!=null) {
				ItemStack itemStackInventory = inv.getItem(i);
				if (itemStackInventory.isSimilar(itemStackAdded) && itemStackInventory.getAmount()<itemStackInventory.getMaxStackSize()) {
					int amt = itemStackInventory.getMaxStackSize()-itemStackInventory.getAmount();
					if (itemStackAdded.getAmount()>=amt) {
						int remaining = itemStackAdded.getAmount()-amt;
						itemStackInventory.setAmount(itemStackInventory.getMaxStackSize());
						itemStackAdded.setAmount(remaining);
					} else {
						itemStackInventory.setAmount(itemStackInventory.getAmount()+itemStackAdded.getAmount());
						itemStackAdded=null;
						break; //Ran out, we're done here.
					}
					inv.setItem(i, itemStackInventory);
				}
			}
		}
		//TwosideKeeper.log("Item: "+itemStackAdded, 1);
		return itemStackAdded;
	}
	public static List<ItemStack> ConvertInventoryToList(Inventory inv, int size) {
		List<ItemStack> newlist = new ArrayList<ItemStack>();
		for (int i=0;i<size;i++) {
			newlist.add(new ItemStack(Material.AIR));
		}
		for (int i=0;i<inv.getSize();i++) {
			ItemStack item = inv.getItem(i);
			if (item!=null) {
				newlist.set(i, item);
			}
		}
		return newlist;
	}
	public static String getInventoryHash(Inventory destination) {
		return destination.getLocation().getX()+destination.getLocation().getY()+destination.getLocation().getZ()+destination.getLocation().getWorld().getName();
	}
	
	public static int getInventoryNumberHash(Inventory destination) {
		String hash = "-"+(Math.signum(destination.getLocation().getBlockX()+destination.getLocation().getBlockZ())>0?1:0)+Integer.toString(Math.abs(destination.getLocation().getBlockX())%1000)+Integer.toString(Math.abs(destination.getLocation().getBlockY())%1000)+Integer.toString(Math.abs(destination.getLocation().getBlockZ())%1000);
		return Integer.parseInt(hash);
	}
	
	public static ItemStack[] RemoveAllNullItems(ItemStack[] contents) {
		List<ItemStack> items = new ArrayList<ItemStack>();
		for (int i=0;i<contents.length;i++) {
			if (contents[i]!=null) {
				items.add(contents[i]);
			}
		}
		return items.toArray(new ItemStack[items.size()]);
	}
}
