package sig.plugin.TwosideKeeper.HelperStructures.Utils;

import java.util.ArrayList;
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
import sig.plugin.TwosideKeeper.HelperStructures.ItemCube;
import sig.plugin.TwosideKeeper.HolidayEvents.Christmas;

public class InventoryUtils {
	public static boolean isCarryingVacuumCube(Player p) {
		for (ItemStack items : p.getInventory().getContents()) {
			if (items!=null && CustomItem.isVacuumCube(items)) {
				return true;
			}
		}
		return false;
	}
	public static ItemStack[] insertItemsInVacuumCube(Player p,ItemStack...items) {
		ItemStack[] remaining = items;
		for (ItemStack itemStacks : p.getInventory().getContents()) {
			if (itemStacks!=null && CustomItem.isVacuumCube(itemStacks)) {
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
				for (int i=0;i<virtualinventory.getSize();i++) {
					itemslist.add(virtualinventory.getItem(i));
				}
				ItemCube.addToViewersOfItemCube(id,remaining,null);
				TwosideKeeper.itemCube_saveConfig(id, itemslist, CubeType.VACUUM);

				/*for (ItemStack i : remainingitems.values()) {
					TwosideKeeper.log("Item "+i+" remains", 0);
				}*/
				remaining = remainingitems.values().toArray(new ItemStack[0]);
				//TwosideKeeper.log("Remaining items: "+ArrayUtils.toString(remaining), 0);
			}
		}
		return remaining;
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
		for (ItemStack itemStacks : p.getInventory().getContents()) {
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
			}
		}
		return remaining;
	}
	public static boolean InventoryContainSameMaterial(Inventory inv, ItemStack item) {
		for (ItemStack i : inv.getContents()) {
			if (i!=null && item!=null && i.getType()==item.getType()) {
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
}
