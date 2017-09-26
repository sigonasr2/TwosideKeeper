package sig.plugin.TwosideKeeper.HelperStructures;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import sig.plugin.TwosideKeeper.PVP;
import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.Events.InventoryUpdateEvent;
import sig.plugin.TwosideKeeper.Events.InventoryUpdateEvent.UpdateReason;
import sig.plugin.TwosideKeeper.HelperStructures.Common.ArrowQuiver;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.InventoryUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.SoundUtils;

public class ItemPickupStructure {
	PlayerPickupItemEvent ev;
	
	public ItemPickupStructure(PlayerPickupItemEvent itemEvent) {
		this.ev=itemEvent;
	}
	
	public void run() {
		//Arrow quiver code goes here.
				//TwosideKeeper.log("["+TwosideKeeper.getServerTickTime()+"] PlayerPickupItemEvent fired w/ "+ev.getItem().getItemStack(), 1);
				if (ev.isCancelled()) {
					return;
				}
		    	Player p = ev.getPlayer();
		    	if (PVP.isPvPing(p)) {
		    		ev.setCancelled(true);
		    		return;
		    	}
		    	//log("Item Right now: "+ev.getItem().getItemStack(),0);
				long time = System.nanoTime();
				long totaltime = System.nanoTime();
		    	
				Bukkit.getScheduler().runTaskLater(TwosideKeeper.plugin, ()->{
			    	InventoryUpdateEvent.TriggerUpdateInventoryEvent(p,ev.getItem().getItemStack(),UpdateReason.PICKEDUPITEM);
				}, 1);
		    	ItemStack newstack = InventoryUtils.AttemptToFillPartialSlotsFirst(p,ev.getItem().getItemStack());
				TwosideKeeper.PickupLogger.AddEntry("Fill Partial Slots First", (int)(System.nanoTime()-time));time=System.nanoTime();
		    	//TwosideKeeper.log(" New Stack is: "+newstack,0);
		    	if (newstack==null || newstack.getType()==Material.AIR) {
					SoundUtils.playGlobalSound(ev.getPlayer().getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.6f, SoundUtils.DetermineItemPitch(ev.getItem().getItemStack()));
		    		TwosideKeeper.PlayPickupParticle(ev.getPlayer(),ev.getItem());
					if (ev.getRemaining()>0) {
						Item it = ev.getItem();
						it.getItemStack().setAmount(ev.getRemaining());
						//GenericFunctions.giveItem(p, it.getItemStack());
						GenericFunctions.dropItem(it.getItemStack(), p.getLocation());
					}
		    		ev.getItem().remove();ev.setCancelled(true);return;}
				TwosideKeeper.PickupLogger.AddEntry("Pickup Item when it's null", (int)(System.nanoTime()-time));time=System.nanoTime();
		    	ev.getItem().setItemStack(newstack);
		    	//log("Pickup Metadata: "+ev.getItem().getItemStack().getItemMeta().toString(),0);
		    	//GenericFunctions.updateSetItems(p.getInventory());
		    	GenericFunctions.UpdateItemLore(ev.getItem().getItemStack());
				TwosideKeeper.PickupLogger.AddEntry("Update Item Lore", (int)(System.nanoTime()-time));time=System.nanoTime();
		    	/*//LEGACY CODE
		    	if (!ev.isCancelled()) {
			    	if (ev.getItem().getItemStack().getType()==Material.ARROW &&
			    			playerHasArrowQuiver(p)) {
			    			int arrowquiver_slot = playerGetArrowQuiver(p);
			    			playerInsertArrowQuiver(p, arrowquiver_slot, ev.getItem().getItemStack().getAmount());
			    			log("Added "+ev.getItem().getItemStack().getAmount()+" arrow"+((ev.getItem().getItemStack().getAmount()==1)?"":"s")+" to quiver in slot "+arrowquiver_slot+". New amount: "+playerGetArrowQuiverAmt(p,arrowquiver_slot),4);
			    			//If we added it here, we destroy the item stack.
			    			p.sendMessage(ChatColor.DARK_GRAY+""+ev.getItem().getItemStack().getAmount()+" arrow"+((ev.getItem().getItemStack().getAmount()==1)?"":"s")+" "+((ev.getItem().getItemStack().getAmount()==1)?"was":"were")+" added to your arrow quiver. Arrow Count: "+ChatColor.GRAY+playerGetArrowQuiverAmt(p,arrowquiver_slot));
			    			ev.getPlayer().playSound(ev.getPlayer().getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.6f, 1.0f);
			    			ev.getItem().remove();
			    			ev.setCancelled(true);
			    	}
		    	}*/
		    	
				TwosideKeeper.HandlePickupAchievements(ev.getPlayer(), ev.getItem().getItemStack());
				TwosideKeeper.PickupLogger.AddEntry("Pickup Achievements", (int)(System.nanoTime()-time));time=System.nanoTime();
		    	
		    	boolean handled = TwosideKeeper.AutoEquipItem(ev.getItem().getItemStack(), p);
				TwosideKeeper.PickupLogger.AddEntry("Auto Equip Item Check", (int)(System.nanoTime()-time));time=System.nanoTime();
		    	if (handled) {
		    		TwosideKeeper.PlayPickupParticle(ev.getPlayer(),ev.getItem());
		    		ev.getItem().remove();
		        	ev.setCancelled(handled);	
		        	return;
		    	}
		    	
		    	/*if (AutoConsumeItem(p,ev.getItem().getItemStack())) {
		    		SoundUtils.playGlobalSound(ev.getPlayer().getLocation(), Sound.ENTITY_GENERIC_EAT, 1.0f, 1.0f);
		    		PlayPickupParticle(ev.getPlayer(),ev.getItem());
		    		ev.getItem().remove();
		    		ev.setCancelled(true);
		    		return;
		    	}*/
				TwosideKeeper.PickupLogger.AddEntry("Auto Consume Item Check", (int)(System.nanoTime()-time));time=System.nanoTime();
		    	
		    	if (ev.getItem().hasMetadata("INFINITEARROW")) { //Not allowed to be picked up, this was an infinite arrow.
		    		TwosideKeeper.log("INFINITE PICKUP", 5);
		    		ev.setCancelled(true);
		    		return;
		    	}
				TwosideKeeper.PickupLogger.AddEntry("Infinite Arrow Check", (int)(System.nanoTime()-time));time=System.nanoTime();
		    	
		    	if (GenericFunctions.isValidArrow(ev.getItem().getItemStack()) && ArrowQuiver.getArrowQuiverInPlayerInventory(p)!=null) {
		    		ev.setCancelled(true);
					SoundUtils.playGlobalSound(ev.getPlayer().getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.6f, SoundUtils.DetermineItemPitch(ev.getItem().getItemStack()));
					TwosideKeeper.PlayPickupParticle(ev.getPlayer(),ev.getItem());
					ev.getItem().remove();
					TwosideKeeper.AddToPlayerInventory(ev.getItem().getItemStack(), p);
					return;
		    	}
				TwosideKeeper.PickupLogger.AddEntry("Valid Arrow check", (int)(System.nanoTime()-time));time=System.nanoTime();
		    	
		    	/**
		    	 * MUST BE HANDLED AFTER EVERYTHING ELSE.
		    	 */

				//TwosideKeeper.log("(1)Item is "+ev.getItem().getItemStack(), 0);
		    	if (InventoryUtils.isCarryingFilterCube(p)) {
		    		//Try to insert it into the Filter cube.
		    		//TwosideKeeper.log("(2)Item is "+ev.getItem().getItemStack(), 0);
		    		ItemStack[] remaining = InventoryUtils.insertItemsInFilterCube(p, ev.getItem().getItemStack());
		    		//TwosideKeeper.log("(3)Item is "+ev.getItem().getItemStack(), 0);
		    		if (remaining.length==0) {
		        		ev.setCancelled(true);
		    			SoundUtils.playGlobalSound(ev.getPlayer().getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.6f, SoundUtils.DetermineItemPitch(ev.getItem().getItemStack()));
		    			TwosideKeeper.PlayPickupParticle(ev.getPlayer(),ev.getItem());
		    			ev.getItem().remove();
		    			return;
		    		} else {
		    			ev.getItem().setItemStack(remaining[0]);
		    		}
		    	}
				TwosideKeeper.PickupLogger.AddEntry("Filter Cube Check", (int)(System.nanoTime()-time));time=System.nanoTime();

				//TwosideKeeper.log("(1)Item is "+ev.getItem().getItemStack(), 0);
		    	if (ev.getItem().getItemStack().getType().isBlock() && InventoryUtils.isCarryingVacuumCube(p)) {
		    		//Try to insert it into the Vacuum cube.
		    		ItemStack[] remaining = InventoryUtils.insertItemsInVacuumCube(p, ev.getItem().getItemStack());
		    		if (remaining.length==0) {
		        		ev.setCancelled(true);
		    			SoundUtils.playGlobalSound(ev.getPlayer().getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.6f, SoundUtils.DetermineItemPitch(ev.getItem().getItemStack()));
		    			TwosideKeeper.PlayPickupParticle(ev.getPlayer(),ev.getItem());
		    			ev.getItem().remove();
		    			return;
		    		} else {
		    			ev.getItem().setItemStack(remaining[0]);
		    		}
		    	}
				TwosideKeeper.PickupLogger.AddEntry("Vacuum Cube Check", (int)(System.nanoTime()-time));time=System.nanoTime();
		    	
		    	//ItemCubeUtils.pickupAndAddItemCubeToGraph(ev.getItem().getItemStack(), p);
		    	
		    	ev.setCancelled(true);
		    	ItemStack givenitem = ev.getItem().getItemStack().clone();
				GenericFunctions.giveItem(p, givenitem);
				if (ev.getRemaining()>0) {
					givenitem.setAmount(ev.getRemaining());
					GenericFunctions.giveItem(p, givenitem);
				}
				TwosideKeeper.PlayPickupParticle(ev.getPlayer(),ev.getItem());
				ev.getItem().remove();
				ItemSet.updateItemSets(ev.getPlayer());
				TwosideKeeper.PickupLogger.AddEntry("Update Item Sets", (int)(System.nanoTime()-time));time=System.nanoTime();
				return;
	}
}
