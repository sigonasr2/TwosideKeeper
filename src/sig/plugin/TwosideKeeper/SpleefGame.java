package sig.plugin.TwosideKeeper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import sig.plugin.TwosideKeeper.HelperStructures.SpleefArena;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;

public class SpleefGame implements Listener {
	/*
	 * Contains information about a spleef game.
	 */
	boolean active = false; //Whether or not a game is on-going.
	List<SpleefPlayerData> players = new ArrayList<SpleefPlayerData>(); //The players involved with this spleef game.
	List<SpleefPlayerData> registered_players = new ArrayList<SpleefPlayerData>(); //The players involved with this spleef game.
	TwosideKeeper plugin;
	SpleefArena id; //The ID of this spleef game.
	Location corner1; //The location of the first corner of the arena.
					//Going outside it automatically terminates the game.
	Location corner2; //The location of the second corner of the arena.
					//Going outside it automatically terminates the game.
	Location shovel_chest; //The location of the chest the spleef shovel is supposed to be in.
	Location shovel_chest2; //The location of the chest the spleef shovel is supposed to be in.
	Location sign; //The location of the sign to register for this spleef arena.
	long registrationtime; //If it takes more than 60 seconds for players to register for a spleef game, the registration will be cancelled.
	long starttime; //The starting time of the match.
	long last_destroyed; //Time the last block was destroyed.
	
	public SpleefGame(TwosideKeeper plug, SpleefArena id, Location corner1, Location corner2, Location shovel_chest, Location sign) {
		this.plugin=plug;
		this.id=id;
		this.corner1=corner1;
		this.corner2=corner2;
		this.shovel_chest=shovel_chest;
		this.sign=sign;
		
		this.registrationtime=0;
	}
	public SpleefGame(TwosideKeeper plug, SpleefArena id, Location corner1, Location corner2, Location shovel_chest, Location shovel_chest2, Location sign) {
		this.plugin=plug;
		this.id=id;
		this.corner1=corner1;
		this.corner2=corner2;
		this.shovel_chest=shovel_chest;
		this.shovel_chest2=shovel_chest2;
		this.sign=sign;
		
		this.registrationtime=0;
	}
	public String toString() {
		return "SpleefGame: "+id.toString()+" From "+this.corner1.toString()+" to "+this.corner2.toString()+".\n"
				+ "Shovel Chest Loc: "+shovel_chest.toString()+";"+((shovel_chest2!=null)?"Shovel Chest 2 Loc: "+shovel_chest2.toString()+";":"")+ " Registration Sign Loc: "+sign.toString();
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void EventListener(Event e) {

		//A listener for all events passed here.
		
		if (e instanceof BlockBreakEvent) {
			BlockBreakEvent ev = (BlockBreakEvent)e;
			
			if (active) {
				
				if (ev.getBlock().getLocation().getBlockX()==corner1.getBlockX() ||
						ev.getBlock().getLocation().getBlockX()==corner2.getBlockX() ||
						ev.getBlock().getLocation().getBlockZ()==corner1.getBlockZ() ||
						ev.getBlock().getLocation().getBlockZ()==corner2.getBlockZ()) {
						//We are not allowed to break blocks on the borders.
						//This is a border block. Prevent destruction.
						ev.setCancelled(true);
				}
				if (BlockIsInside(ev.getBlock().getLocation(),corner1,corner2)) {
					//This is a block inside the field. Now see if it's a registered player.
					boolean not_allowed=true;
					for (int i=0;i<registered_players.size();i++) {
						if (ev.getPlayer().getName().equalsIgnoreCase(registered_players.get(i).player)) {
							//This is allowed.
							not_allowed=false;
							break;
						}
					}
					if (not_allowed) {
						ev.setCancelled(true); //Deny it, since they are not an actual participant!
					}
				}
				if (!ev.isCancelled()) {
					for (int i=0;i<players.size();i++) {
						if (players.get(i).player.equalsIgnoreCase(ev.getPlayer().getName())) {
							players.get(i).blocks_destroyed++;
							last_destroyed=TwosideKeeper.getServerTickTime();
							break;
						}
					}
				}
			}
		}
		else
		if (e instanceof BlockPlaceEvent) {
			BlockPlaceEvent ev = (BlockPlaceEvent)e;
			
			if (active) {
				if (BlockIsInside(ev.getBlock().getLocation(),corner1,corner2)) {
					ev.setCancelled(true);
				}
			}
		}
		else
		if (e instanceof PlayerInteractEvent) {
			PlayerInteractEvent ev = (PlayerInteractEvent)e;
			TwosideKeeper.log("Interact Event received, checking...",5);
			if (ev.getAction()==Action.RIGHT_CLICK_BLOCK &&
				ev.getClickedBlock()!=null &&
					(ev.getClickedBlock().getType()==Material.SIGN ||
					ev.getClickedBlock().getType()==Material.WALL_SIGN ||
					ev.getClickedBlock().getType()==Material.SIGN_POST) && //Great, it's a sign. Make sure it's the correct sign.
					ev.getClickedBlock().getLocation().getBlockX()==sign.getBlockX() &&
					ev.getClickedBlock().getLocation().getBlockY()==sign.getBlockY() &&
					ev.getClickedBlock().getLocation().getBlockZ()==sign.getBlockZ() ) {
				TwosideKeeper.log("This is a sign event.",5);
				if (!active) {
					//We are going to register this player to this spleef arena.
					if (!IsSpleefPlayerRegistered(ev.getPlayer())) {
						registrationtime=TwosideKeeper.getServerTickTime();
						players.add(new SpleefPlayerData(plugin,ev.getPlayer()));
						registered_players.add(new SpleefPlayerData(plugin,ev.getPlayer()));
						//Determine if we are ready to play or not.
						if (players.size()>=2) {
							plugin.getServer().broadcastMessage(ChatColor.GOLD+"Player "+ChatColor.GREEN+ev.getPlayer().getName()+ChatColor.GOLD+" has registered to play in the "+ChatColor.AQUA+id.toString()+ChatColor.GOLD+" Spleef Arena!");
							plugin.getServer().broadcastMessage(ChatColor.BLUE+"   10 seconds left to register!");
							starttime=TwosideKeeper.getServerTickTime()+200;
						} else { //We only have one player. Wait for more.
							plugin.getServer().broadcastMessage(ChatColor.GOLD+"Player "+ChatColor.GREEN+ev.getPlayer().getName()+ChatColor.GOLD+" has registered to play in the "+ChatColor.AQUA+id.toString()+ChatColor.GOLD+" Spleef Arena!");
							plugin.getServer().broadcastMessage(ChatColor.BLUE+"   Looking for at least one more player!");
							//De-register from other Spleef Arenas.
							for (int i=0;i<TwosideKeeper.TwosideSpleefGames.GetSpleefGames().size();i++) {
								if (TwosideKeeper.TwosideSpleefGames.GetSpleefGames().get(i).id!=id) {
									//This is not ours. See if this player is registered here. De-register him/her.
									for (int j=0;j<TwosideKeeper.TwosideSpleefGames.GetSpleefGames().get(i).players.size();j++) {
										if (TwosideKeeper.TwosideSpleefGames.GetSpleefGames().get(i).players.get(j).player.equalsIgnoreCase(ev.getPlayer().getName())) {
											TwosideKeeper.TwosideSpleefGames.GetSpleefGames().get(i).players.remove(j);
											TwosideKeeper.TwosideSpleefGames.GetSpleefGames().get(i).registered_players.remove(j);
											break;
										}
									}
								}
							}
						}
					}
				} else {
					ev.getPlayer().sendMessage(ChatColor.GOLD+"Sorry! There is a game currently in progress. Registrations are closed.");
				}
			}
		}
		else
		if (e instanceof PlayerQuitEvent) {
			PlayerQuitEvent ev = (PlayerQuitEvent)e;
			//If this player leaves in the middle of a Spleef game, we have to resolve the game's winner.
			//Give this player back their stuff.
			if (active) {
				RemovePlayer(ev.getPlayer(), RemovePlayerReason.QUIT);
				if (players.size()==1) {
					//This means there's not enough players to continue the match. End the match, passing the remaining player.
					EndMatch(players.get(0));
				}
			}
		}
		
	}
	
	public void Tick() {
		//A tick that occurs once every second.
		if (!active && players.size()>=2 && starttime<=TwosideKeeper.getServerTickTime())
		{ //Start the game!
			//Teleport players to the arena at a random position, clear their inventories,
			//set their health, and their hunger.
			//Setup the arena with the proper blocks, if the dirt blocks are not filled in already.
			if (corner1.getBlockX()>corner2.getBlockX()) {
				if (corner1.getBlockZ()>corner2.getBlockZ()) {
					for (int i=corner2.getBlockX();i<=corner1.getBlockX();i++) {
						for (int j=corner2.getBlockZ();j<=corner1.getBlockZ();j++) {
							if (Bukkit.getWorld("world").getBlockAt(i,corner1.getBlockY(),j)!=null &&
									Bukkit.getWorld("world").getBlockAt(i,corner1.getBlockY(),j).getType()==Material.AIR) {
								Bukkit.getWorld("world").getBlockAt(i,corner1.getBlockY(),j).setType(Material.DIRT);
							}
							if (id==SpleefArena.LAYERED &&
									i!=corner2.getBlockX() &&
									i!=corner1.getBlockX() &&
									j!=corner1.getBlockZ() &&
									j!=corner2.getBlockZ()) {
								for (int k=0;k<3;k++) {
									Bukkit.getWorld("world").getBlockAt(i,corner1.getBlockY()+k+1,j).setType(Material.GRAVEL);
								}
							}
						}
					}
				} else {
					for (int i=corner2.getBlockX();i<=corner1.getBlockX();i++) {
						for (int j=corner1.getBlockZ();j<=corner2.getBlockZ();j++) {
							if (Bukkit.getWorld("world").getBlockAt(i,corner1.getBlockY(),j)!=null &&
									Bukkit.getWorld("world").getBlockAt(i,corner1.getBlockY(),j).getType()==Material.AIR) {
								Bukkit.getWorld("world").getBlockAt(i,corner1.getBlockY(),j).setType(Material.DIRT);
							}
							if (id==SpleefArena.LAYERED &&
									i!=corner2.getBlockX() &&
									i!=corner1.getBlockX() &&
									j!=corner1.getBlockZ() &&
									j!=corner2.getBlockZ()) {
								for (int k=0;k<3;k++) {
									Bukkit.getWorld("world").getBlockAt(i,corner1.getBlockY()+k+1,j).setType(Material.GRAVEL);
								}
							}
						}
					}
				}
			} else {
				if (corner1.getBlockZ()>corner2.getBlockZ()) {
					for (int i=corner1.getBlockX();i<=corner2.getBlockX();i++) {
						for (int j=corner2.getBlockZ();j<=corner1.getBlockZ();j++) {
							if (Bukkit.getWorld("world").getBlockAt(i,corner1.getBlockY(),j)!=null &&
									Bukkit.getWorld("world").getBlockAt(i,corner1.getBlockY(),j).getType()==Material.AIR) {
								Bukkit.getWorld("world").getBlockAt(i,corner1.getBlockY(),j).setType(Material.DIRT);
							}
							if (id==SpleefArena.LAYERED &&
									i!=corner2.getBlockX() &&
									i!=corner1.getBlockX() &&
									j!=corner1.getBlockZ() &&
									j!=corner2.getBlockZ()) {
								for (int k=0;k<3;k++) {
									Bukkit.getWorld("world").getBlockAt(i,corner1.getBlockY()+k+1,j).setType(Material.GRAVEL);
								}
							}
						}
					}
				} else {
					for (int i=corner1.getBlockX();i<=corner2.getBlockX();i++) {
						for (int j=corner1.getBlockZ();j<=corner2.getBlockZ();j++) {
							if (Bukkit.getWorld("world").getBlockAt(i,corner1.getBlockY(),j)!=null &&
									Bukkit.getWorld("world").getBlockAt(i,corner1.getBlockY(),j).getType()==Material.AIR) {
								Bukkit.getWorld("world").getBlockAt(i,corner1.getBlockY(),j).setType(Material.DIRT);
							}
							if (id==SpleefArena.LAYERED &&
									i!=corner2.getBlockX() &&
									i!=corner1.getBlockX() &&
									j!=corner1.getBlockZ() &&
									j!=corner2.getBlockZ()) {
								for (int k=0;k<3;k++) {
									Bukkit.getWorld("world").getBlockAt(i,corner1.getBlockY()+k+1,j).setType(Material.GRAVEL);
								}	
							}
						}
					}
				}
			}
			
			//For the block above the chest, turn it into a wooden plank.
			Bukkit.getWorld("world").getBlockAt(shovel_chest.clone().add(0,1,0)).setType(Material.WOOD);
			//Insert a Spleef shovel in the chest.
			//We will clear out the contents first.
			if (Bukkit.getWorld("world").getBlockAt(shovel_chest).getState() instanceof Chest) {
				Chest spleef_chest = (Chest)Bukkit.getWorld("world").getBlockAt(shovel_chest).getState();
				spleef_chest.getInventory().clear();
				spleef_chest.getInventory().setItem((int)(Math.random()*27), new ItemStack(Material.WOOD_SPADE));
			}
			//And a second one if it exists.
			if (shovel_chest2!=null) {
				Bukkit.getWorld("world").getBlockAt(shovel_chest2.clone().add(0,1,0)).setType(Material.WOOD);
				if (Bukkit.getWorld("world").getBlockAt(shovel_chest2).getState() instanceof Chest) {
					Chest spleef_chest2 = (Chest)Bukkit.getWorld("world").getBlockAt(shovel_chest2).getState();
					spleef_chest2.getInventory().clear();
					spleef_chest2.getInventory().setItem((int)(Math.random()*27), new ItemStack(Material.WOOD_SPADE));
				}
			}
			
			//Insert spleef shovel into the chest~!
			
			String matchup=""; //Set the string for the match-up.
			for (int i=0;i<players.size();i++) {
				players.get(i).SaveInventory();
				players.get(i).ClearInventory();
				
				if (matchup.equalsIgnoreCase("")) {
					matchup+=players.get(i).player;
				} else {
					if (players.size()==2) {
						matchup+=" and "+players.get(i).player;
					} else {
						matchup+=", "+players.get(i).player;
					}
				}
				
				PlayerStructure pd = PlayerStructure.GetPlayerStructure(Bukkit.getServer().getPlayer(players.get(i).player));
				pd.isPlayingSpleef=true;
				
				//Heal the player.
				Bukkit.getServer().getPlayer(players.get(i).player).setHealth(Bukkit.getServer().getPlayer(players.get(i).player).getMaxHealth());
				Bukkit.getServer().getPlayer(players.get(i).player).setFoodLevel(20);
				//Teleport to a random location inside the arena.
				Bukkit.getServer().getPlayer(players.get(i).player).teleport(new Location(
						Bukkit.getWorld("world"),
						(corner1.getBlockX()>corner2.getBlockX())?(corner1.getBlockX()-2-(Math.random()*(corner1.getBlockX()-corner2.getBlockX()-4))):(corner2.getBlockX()-2-(Math.random()*(corner2.getBlockX()-corner1.getBlockX()-4))),
						(corner1.getBlockY()>corner2.getBlockY())?(corner1.getBlockY()+4):(corner2.getBlockY()+4),
						(corner1.getBlockZ()>corner2.getBlockZ())?(corner1.getBlockZ()-2-(Math.random()*(corner1.getBlockZ()-corner2.getBlockZ()-4))):(corner2.getBlockZ()-2-(Math.random()*(corner2.getBlockZ()-corner1.getBlockZ()-4)))
								), TeleportCause.PLUGIN);
				//Give players Resistance 100 so they can never die.
				GenericFunctions.logAndApplyPotionEffectToPlayer(PotionEffectType.DAMAGE_RESISTANCE,99999,100,Bukkit.getServer().getPlayer(players.get(i).player));
			}
			active=true;
			starttime=TwosideKeeper.getServerTickTime();
			//registered_players=players; //Registered players include every single player that joined in. This is to manage ranking and
										//who is allowed to help break blocks after losing.
			last_destroyed=TwosideKeeper.getServerTickTime();
			
			Bukkit.getServer().broadcastMessage(ChatColor.GOLD+"The spleef matchup between "+ChatColor.GREEN+matchup+ChatColor.GOLD+" has begun!");
		}
		
		if (active && starttime+100<TwosideKeeper.getServerTickTime()) {
			for (int i=0;i<players.size();i++) {
				if (players.get(i).IsOutOfBounds(corner1, corner2)) {
					RemovePlayer(players.get(i), RemovePlayerReason.OUTOFBOUNDS);
					if (players.size()==1) {
						//This means there's not enough players to continue the match. End the match, passing the remaining player.
						EndMatch(players.get(0));
						break;
					}
				}
			}
		}
		if (active && last_destroyed+600<TwosideKeeper.getServerTickTime()) {
			//This match has come to a stall, determine the winner based on block destruction count.
			SpleefPlayerData winner = null;
			int highest_count=-1;
			for (int i=0;i<players.size();i++) {
				if (players.get(i).blocks_destroyed>highest_count) {
					highest_count=players.get(i).blocks_destroyed;
					winner=players.get(i);
				}
			}
			
			Bukkit.getServer().broadcastMessage(ChatColor.GOLD+"The Spleef Match has stalled, and the winner has been decided!");
			Bukkit.getServer().broadcastMessage(ChatColor.BLUE+""+ChatColor.ITALIC+"    With "+ChatColor.GREEN+highest_count+ChatColor.BLUE+" blocks destroyed...");
			//We will have the winner. End the Match.
			if (winner!=null) {
				EndMatch(winner);
			} else {
				EndMatch(players.get(0)); //Something went wrong??
			}
		}
	}
	
	void RemovePlayer(Player p, RemovePlayerReason rs) {
		for (int i=0;i<players.size();i++) {
			if (players.get(i).player.equalsIgnoreCase(p.getName())) {
				//Give this player their inventory contents back.
				RemovePlayer(players.get(i), rs);
				break;
			}
		}
	}
	void RemovePlayer(SpleefPlayerData p, RemovePlayerReason rs) {
		p.ClearInventory();
		p.RestoreInventory();
		GenericFunctions.logAndRemovePotionEffectFromPlayer(PotionEffectType.DAMAGE_RESISTANCE,Bukkit.getServer().getPlayer(p.player));
		for (int i=0;i<players.size();i++) {
			if (players.get(i).player.equalsIgnoreCase(p.player)) {
				players.remove(i);
				break;
			}
		}
		Bukkit.getServer().getPlayer(p.player).teleport(new Location(
				Bukkit.getWorld("world"),
				(corner1.getBlockX()>corner2.getBlockX())?(corner1.getBlockX()-(Math.random()*(corner1.getBlockX()-corner2.getBlockX()))):(corner2.getBlockX()-(Math.random()*(corner2.getBlockX()-corner1.getBlockX()))),
				(corner1.getBlockY()>corner2.getBlockY())?(corner1.getBlockY()-2):(corner2.getBlockY()-2),
				(corner1.getBlockZ()>corner2.getBlockZ())?(corner1.getBlockZ()-(Math.random()*(corner1.getBlockZ()-corner2.getBlockZ()))):(corner2.getBlockZ()-(Math.random()*(corner2.getBlockZ()-corner1.getBlockZ())))
						), TeleportCause.PLUGIN);
		if (rs==RemovePlayerReason.OUTOFBOUNDS ||
				rs==RemovePlayerReason.QUIT) {
	    	PlayerStructure pd = (PlayerStructure)TwosideKeeper.playerdata.get(Bukkit.getServer().getPlayer(p.player));
			pd.spleef_pts+=registered_players.size()-players.size()-1;
			Bukkit.getServer().broadcastMessage(ChatColor.GREEN+p.player+ChatColor.GOLD+" "+ChatColor.ITALIC+"has been knocked out of this round of Spleef!");
		}
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(Bukkit.getServer().getPlayer(p.player));
		pd.isPlayingSpleef=false;
	}
	
	void EndMatch(SpleefPlayerData winner) {
		//Ends the match, resolving all players, winners, and giving everything back.
		TwosideKeeper.log("There are "+players.size()+" players in the registered player list.",5);
		for (int i=0;i<players.size();i++) {
			//Give all players' items back. Remove the damage resistance buff.
			RemovePlayer(players.get(i), RemovePlayerReason.GENERAL);
			i--;
		}
    	PlayerStructure pd = (PlayerStructure)TwosideKeeper.playerdata.get(Bukkit.getServer().getPlayer(winner.player));
		pd.spleef_pts+=registered_players.size();
		pd.spleef_wins+=1;
		registered_players.clear();
		Bukkit.getServer().broadcastMessage(ChatColor.GOLD+"Congratulations to Player "+ChatColor.GREEN+winner.player+ChatColor.GOLD+" for winning this round of Spleef!");
		active=false;
	}
	
	boolean IsSpleefPlayerRegistered(Player p) {
		//Returns if the player specified is already registered to the Spleef Game or not.
		for (int i=0;i<players.size();i++) {
			if (players.get(i).player.equalsIgnoreCase(p.getName())) {
				return true;
			}
		}
		return false;
	}
	
	boolean BlockIsInside(Location l, Location b1, Location b2) {
		if (b1.getBlockX()>b2.getBlockX()) {
			if (b1.getBlockZ()>b2.getBlockZ()) {
				if (l.getBlockX()>b2.getBlockX() &&
					l.getBlockX()<b1.getBlockX() &&
					l.getBlockZ()>b2.getBlockZ() &&
					l.getBlockZ()<b1.getBlockZ()) {
					return true;
				} else {
					return false;
				}
			} else {
				if (l.getBlockX()>b2.getBlockX() &&
					l.getBlockX()<b1.getBlockX() &&
					l.getBlockZ()<b2.getBlockZ() &&
					l.getBlockZ()>b1.getBlockZ()) {
					return true;
				} else {
					return false;
				}
			}
		} else {
			if (b1.getBlockZ()>b2.getBlockZ()) {
				if (l.getBlockX()<b2.getBlockX() &&
					l.getBlockX()>b1.getBlockX() &&
					l.getBlockZ()>b2.getBlockZ() &&
					l.getBlockZ()<b1.getBlockZ()) {
					return true;
				} else {
					return false;
				}
			} else {
				if (l.getBlockX()<b2.getBlockX() &&
					l.getBlockX()>b1.getBlockX() &&
					l.getBlockZ()<b2.getBlockZ() &&
					l.getBlockZ()>b1.getBlockZ()) {
					return true;
				} else {
					return false;
				}
			}
		}
	}
}

enum RemovePlayerReason {
	QUIT,OUTOFBOUNDS,GENERAL;
}

class SpleefPlayerData {
	String player; //The name of the player.
	ItemStack player_equipment_helmet;
	ItemStack player_equipment_chestplate;
	ItemStack player_equipment_leggings;
	ItemStack player_equipment_boots;
	ItemStack player_equipment_shield;
	List<ItemStack> player_inventory = new ArrayList<ItemStack>();
	TwosideKeeper plug;
	int blocks_destroyed;
	SpleefPlayerData(TwosideKeeper plug, Player player) {
		this.plug=plug;
		this.player=player.getName();
		this.blocks_destroyed=0;
		/*
		*/
	}
	public void RestoreInventory() {
		//Gives the player back their inventory.
		for (int i=0;i<player_inventory.size();i++) {
			if (player_inventory.get(i)!=null) {
				plug.getServer().getPlayer(this.player).getInventory().setItem(i, player_inventory.get(i));
			}
		}
		/*
		plug.getServer().getPlayer(this.player).getInventory().setExtraContents(player_inven2);
		plug.getServer().getPlayer(this.player).getInventory().setStorageContents(player_inven1);
		plug.getServer().getPlayer(this.player).getInventory().setArmorContents(player_equipment);*/
		
	}
	public void SaveInventory() {
		File file = new File(this.plug.getDataFolder()+"/inventorybackup/inventory"+plug.getServer().getPlayer(this.player).getName()+".txt");

		// if file doesnt exists, then create it
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try(
			FileWriter fw = new FileWriter(this.plug.getDataFolder()+"/inventorybackup/inventory"+plug.getServer().getPlayer(this.player).getName()+".txt", true);
		    BufferedWriter bw = new BufferedWriter(fw);)
		{
			for (int i=0;i<plug.getServer().getPlayer(this.player).getInventory().getSize();i++) {
				player_inventory.add(plug.getServer().getPlayer(this.player).getInventory().getItem(i));
				if (plug.getServer().getPlayer(this.player).getInventory().getItem(i)!=null) {
					bw.write(plug.getServer().getPlayer(this.player).getInventory().getItem(i).toString());
					bw.newLine();
				}
			}
			bw.newLine();
			bw.write("---"+TwosideKeeper.getServerTickTime());
			bw.newLine();bw.newLine();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		    //exception handling left as an exercise for the reader
		}
	}
	public void ClearInventory() {
		plug.getServer().getPlayer(this.player).getInventory().clear();
	}
	public boolean IsOutOfBounds(Location corner1, Location corner2) {
		Player p = plug.getServer().getPlayer(this.player);
		int xbound1=(corner1.getBlockX()>corner2.getBlockX())?corner2.getBlockX():corner1.getBlockX(),
				xbound2=(corner1.getBlockX()>corner2.getBlockX())?corner1.getBlockX():corner2.getBlockX(),
				zbound1=(corner1.getBlockZ()>corner2.getBlockZ())?corner2.getBlockZ():corner1.getBlockZ(),
				zbound2=(corner1.getBlockZ()>corner2.getBlockZ())?corner1.getBlockZ():corner2.getBlockZ();
		if (p.getLocation().getBlockX()<=xbound1 ||
				p.getLocation().getBlockX()>=xbound2 ||
				p.getLocation().getBlockZ()<=zbound1 ||
				p.getLocation().getBlockZ()>=zbound2 ||
				p.getLocation().getBlockY()<corner1.getBlockY()+0.5) {
			return true;
		} else {
			return false;
		}
	}
}