package sig.plugin.TwosideKeeper;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;

import com.google.common.collect.ImmutableList;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import sig.plugin.TwosideKeeper.HelperStructures.ArtifactItem;
import sig.plugin.TwosideKeeper.HelperStructures.CustomItem;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.DebugUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.ItemUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.TextUtils;

public class PVP {
	static HashMap<String,String> teams; //<Player Name, Team Name>
	HashMap<String,PVPPlayer> players = new HashMap<String,PVPPlayer>();
	List<String> losers = new ArrayList<String>();
	PVPOption style;
	PVPOption battlefield;
	PVPArena currentArena;
	CHOICEENGINE state;
	public static List<PVPArena> arenas;
	long timer;
	private long lastSelected=0;
	int scorelimit;
	int team1score=0;
	int team2score=0;
	long timelimit;
	long nextRoundTime=0;
	boolean scorematch = false; //If true, uses score limit. If false uses timer.
	BossBar matchTimer = null;
	int duration = 0;
	boolean isTeamMatch=false;
	String freshBloodPlayer;
	
	//NEUTRAL team
	//Team1
	//Team2 etc
	 
	public PVP(Player...players) {
		for (Player p : players) {
			SetupNewPVPPlayer(p);
			findFreshBloodPlayer();
			//Bukkit.getServer().broadcastMessage(ChatColor.GREEN+"Waiting for any additional players to join the PVP Match...");
			//Bukkit.getServer().broadcastMessage(ChatColor.GREEN+"Players must click on "+getParticipants()+" to join in.");
			p.sendMessage(ChatColor.GREEN+"Waiting for any additional players to join the PVP Match...");
		}
		Bukkit.getServer().broadcastMessage(ChatColor.GREEN+"A new PvP Match Request is underway. Click on "+getParticipants(true)+" to join in.");
		announceFreshBloodPlayer(true);
		showReadyChoice();
		state = CHOICEENGINE.WAITFORPLAYERS;
		timer = TwosideKeeper.getServerTickTime();
	}
	
	private void showReadyChoice() {
		for (String s : players.keySet()) {
			showReadyChoice(s);
		}
	}
	
	private void showReadyChoice(String player) {
		TextComponent tca = new TextComponent(" ");
			TextComponent tc = new TextComponent(ChatColor.GREEN+"[Ready]");
			tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/pvp _READY_ YES"));
			tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ComponentBuilder("Click to ready up for the match. All players must be "+ChatColor.GREEN+"READY"+ChatColor.RESET+" to continue.").create()));
			tc.addExtra("   ");
			TextComponent tc2 = new TextComponent(ChatColor.RED+"[Leave]");
			tc2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/pvp _READY_ NO"));
			tc2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ComponentBuilder("Click to leave the match.").create()));
			tc.addExtra(tc2);
		tca.addExtra(tc);
		Player p = Bukkit.getPlayer(player);
		if (p!=null && p.isOnline()) {
			p.spigot().sendMessage(tca);
		}
	}

	private void announceFreshBloodPlayer(boolean announce) {
		if (freshBloodPlayer!=null) {
			if (announce) {
				Bukkit.getServer().broadcastMessage(ChatColor.GRAY+""+ChatColor.ITALIC+"   This match features a Fresh Blood Player, doubling the drop rate.");
			} else {
				for (String s : players.keySet()) {
					Player p = Bukkit.getPlayer(s);
					if (p!=null && p.isOnline()) {
						p.sendMessage(ChatColor.YELLOW+""+ChatColor.ITALIC+"   "+freshBloodPlayer+ChatColor.GRAY+" is a Fresh Blood player, doubling the drop rate of this match.");
					}
				}
			}
		}
	}

	private void findFreshBloodPlayer() {
		if (freshBloodPlayer==null) {
			for (String s : players.keySet()) {
				Player p = Bukkit.getPlayer(s);
				if (p!=null && p.isOnline()) {
					PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
					if (pd.freshBlood) {
						pd.freshBlood=false;
						freshBloodPlayer=s;
						break;
					}
				}
			}
		}
	}

	public void joinMatch(Player p) {
		if (!players.containsKey(p.getName())) {
			SetupNewPVPPlayer(p);
			for (String s : players.keySet()) {
				PVPPlayer pp = players.get(s);
				Player pl = Bukkit.getPlayer(s);
				if (pl!=null && pl.isValid() && pl.isOnline()) {
					pl.sendMessage(ChatColor.YELLOW+p.getName()+" has joined the match. Current Participants: "+ChatColor.YELLOW+getParticipants());
					if (!pp.isReady) {
						showReadyChoice(s);
					}
				} else {
					//pl.sendMessage(ChatColor.YELLOW+s+ChatColor.GOLD+" has left the PVP Match...");
					leaveMatch(s);				
				}
			}
			findFreshBloodPlayer();
			announceFreshBloodPlayer(false);
			timer = TwosideKeeper.getServerTickTime();
		}
	}

	private void SetupNewPVPPlayer(Player p) {
		PVPPlayer newpp = new PVPPlayer();
		players.put(p.getName(), newpp);
		newpp.original_inv = Bukkit.createInventory(p, 54);
		for (int i=0;i<p.getInventory().getSize();i++) {
			if (ItemUtils.isValidItem(p.getInventory().getItem(i))) {
				newpp.original_inv.setItem(i, p.getInventory().getItem(i).clone());
			}
		}
		filterInventory(p);
	}
	
	private void filterInventory(Player p) {
		for (int i=9;i<36;i++) {
			p.getInventory().setItem(i, new ItemStack(Material.AIR));
		}
		ReplaceGearWithLeatherVersionAndFilterItems(p);
	}

	private void ReplaceGearWithLeatherVersionAndFilterItems(Player p) {
		for (int i=0;i<p.getInventory().getSize();i++) {
			ItemStack item = p.getInventory().getItem(i);
			if (ItemUtils.isValidItem(item) &&
					GenericFunctions.isArmor(item)) {
				String[] split = item.getType().name().split("_");
				if (split.length==2) {
					Material newtype = Material.valueOf("LEATHER_"+split[1]);
					ItemStack newitem = new ItemStack(newtype);
					newitem.setItemMeta(item.getItemMeta());
					newitem = GenericFunctions.addHardenedItemBreaks(newitem, 100).clone();
					p.getInventory().setItem(i, newitem);
				}
			}
			if (ItemUtils.isValidItem(item) && 
					item.hasItemMeta() && item.getItemMeta() instanceof PotionMeta) {
				PotionMeta meta = (PotionMeta)item.getItemMeta();
				for (PotionEffect pe : meta.getCustomEffects()) {
					if (pe.getAmplifier()>4) { //This potion is too strong, and cannot be used in a PvP match.
						p.getInventory().setItem(i, new ItemStack(Material.AIR));
					}
				}
			}
			//Use of scrolls should also be forbidden.
			if (ItemUtils.isValidItem(item) &&
					item.getType()==Material.PAPER &&
					item.hasItemMeta() && item.getEnchantments().size()>0) {
				//This means it's magical paper and is not allowed.
				p.getInventory().setItem(i, new ItemStack(Material.AIR));
			}
		}
		RemoveAllPlayerBuffs(p);
	}

	private void RemoveAllPlayerBuffs(Player p) {
		for (PotionEffect pe : p.getActivePotionEffects()) {
			GenericFunctions.logAndRemovePotionEffectFromEntity(pe.getType(), p);
		}
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		pd.vendetta_amt=0;
		pd.weaponcharges=0;
		pd.damagepool=0;
		HashMap<String,Buff> buffs = Buff.getBuffData(p);
		for (String s : buffs.keySet()) {
			Buff b = buffs.get(s);
			Bukkit.getScheduler().runTaskLater(TwosideKeeper.plugin, ()->{
				b.removeBuff(p, s);
			}, 1);
		}
	}

	private void ColorGearBasedOnTeam(Player p, int team) {
		for (int i=0;i<p.getInventory().getSize();i++) {
			ItemStack item = p.getInventory().getItem(i);
			if (ItemUtils.isValidItem(item) &&
					GenericFunctions.isArmor(item) &&
					item.getType().name().startsWith("LEATHER_")) {
				Color col = Bukkit.getServer().getItemFactory().getDefaultLeatherColor();
				if (team==1) {
					col = Color.fromRGB(66, 134, 244);
				} else {
					col = Color.fromRGB(244, 65, 65);
				}
				LeatherArmorMeta meta = (LeatherArmorMeta)item.getItemMeta();
				meta.setColor(col);
				item.setItemMeta(meta);
				p.getInventory().setItem(i, item);
			}
		}
	}

	private void leaveMatch(String s) {
		if (players.containsKey(s)) {
			TwosideKeeper.ScheduleRemoval(players,s);
			Player p = Bukkit.getPlayer(s);
			if (p!=null && p.isOnline()) {
				p.sendMessage(ChatColor.YELLOW+s+" has left the match.");
	    		PVPPlayer pp =  players.get(p.getName());
	    		for (int i=0;i<p.getInventory().getSize();i++) {
	    			p.getInventory().setItem(i, pp.original_inv.getItem(i));
	    		}
	    		RemoveAllPlayerBuffs(p);
			}
			players.remove(s);
			Bukkit.getScheduler().runTaskLater(TwosideKeeper.plugin,()->{
				for (String ss : players.keySet()) {
					Player pl = Bukkit.getPlayer(ss);
					if (pl!=null && pl.isValid() && pl.isOnline() && !s.equalsIgnoreCase(ss)) {
						pl.sendMessage(ChatColor.YELLOW+s+" has left the match. Current Participants: "+ChatColor.YELLOW+getParticipants());
					}
					RemoveAllPlayerBuffs(pl);
				}
			},2);
		}
	}

	private String getParticipants() {
		return getParticipants(false);
	}
	
	private String getParticipants(boolean OR) {
		StringBuilder sb = new StringBuilder("");
		int count=0;
		for (String s : players.keySet()) {
			if (sb.length()==0) {
				sb.append((freshBloodPlayer!=null && freshBloodPlayer.equalsIgnoreCase(s)?"*":""));
				sb.append(s);
			} else {
				if (players.size()==2) {
					sb.append(" "+(OR?"or":"and")+" ");
					sb.append((freshBloodPlayer!=null && freshBloodPlayer.equalsIgnoreCase(s)?"*":""));
					sb.append(s);
				} else {
					if (count+1==players.size()) {
						sb.append(", "+(OR?"or":"and")+" ");
						sb.append((freshBloodPlayer!=null && freshBloodPlayer.equalsIgnoreCase(s)?"*":""));
						sb.append(s);
					} else {
						sb.append(", ");
						sb.append((freshBloodPlayer!=null && freshBloodPlayer.equalsIgnoreCase(s)?"*":""));
						sb.append(s);
					}
				}
			}
			count++;
		}
		return sb.toString();
	}

	public boolean runTick() {
		removeInactivePlayers();
		moveToActiveSpectatorTarget();
		movePlayersOutsideArenaBackIn();
		switch (state) {
			/*case ACCEPTREQUEST:{
				if (timer+200<=TwosideKeeper.getServerTickTime()) {
					return false; //Cancelled.
				}
			}break;*/
			case WAITFORPLAYERS:{
				//if (timer+200<=TwosideKeeper.getServerTickTime()) {
				if (everyoneIsReady()) { 
					if (players.size()>=2) {
						state = CHOICEENGINE.WAITFORROUNDCHOICES;
						timer=TwosideKeeper.getServerTickTime();
						resetPlayerChoices();
						DisplayRoundChoices();
					} else {
						return DisbandMatch(ChatColor.YELLOW+"Not enough participants!"+ChatColor.GREEN+" The PVP Match has been disbanded.");
					}
				}
				//}
			}break;
			case WAITFORROUNDCHOICES:{
				if (timer+400<=TwosideKeeper.getServerTickTime() || allPlayersPicked()) {
					if (players.size()>=2) {
						List<PVPOption> choices = new ArrayList<PVPOption>();
						for (String s : players.keySet()) {
							if (players.containsKey(s)) {
								PVPPlayer pp = players.get(s);
								if (pp.choice!=PVPOption.NONE) {
									choices.add(pp.choice);
								}
							}
						}
						if (choices.size()==0) {
							PVPOption[] options = new PVPOption[]{PVPOption.ROUNDS3,PVPOption.ROUNDS5,
									PVPOption.ROUNDS7,PVPOption.ROUNDS15,PVPOption.MIN3,
									PVPOption.MIN5,PVPOption.MIN10};
							style = options[(int)(Math.random()*options.length)];
						} else {
							style = choices.get((int)(Math.random()*choices.size()));
						}
						for (String s : players.keySet()) {
							Player p = Bukkit.getPlayer(s);
							if (p!=null && p.isValid() && p.isOnline()) {
								p.sendMessage(ChatColor.YELLOW+style.getTitle()+ChatColor.GREEN+" has been voted as the style for this PVP match!");
							}
						}
						if (players.size()>2 && style.name().contains("ROUNDS")) {
							state = CHOICEENGINE.WAITFORTEAMCHOICES;
							isTeamMatch=true;
							lastSelected=TwosideKeeper.getServerTickTime();
							resetPlayerChoices();
							DisplayTeamChoices();
						} else {
							state = CHOICEENGINE.WAITFORSTAGECHOICES;
							resetPlayerChoices();
							DisplayStageChoices();
						}
						timer=TwosideKeeper.getServerTickTime();
					} else {
						return DisbandMatch(ChatColor.YELLOW+"Not enough participants!"+ChatColor.GREEN+" The PVP Match has been disbanded.");
					}
				}
			}break;
			case WAITFORTEAMCHOICES:{
				if (lastSelected+100<=TwosideKeeper.getServerTickTime()) {
					if (allPlayersHaveChosenATeam() || lastSelected+4000<=TwosideKeeper.getServerTickTime()) {
						randomlyPickTeams();
						if (teamsAreInvalid()) {
							return DisbandMatch("Not enough players in both teams to begin a PvP Match! The match has been cancelled.");
						}
						SendTeamList();
						resetPlayerChoices();
						DisplayStageChoices();
						state = CHOICEENGINE.WAITFORSTAGECHOICES;
					}
				}
			}break;
			case WAITFORSTAGECHOICES:{
				if (timer+400<=TwosideKeeper.getServerTickTime() || allPlayersPicked()) {
					if (players.size()>=2) {
						List<Object> choices = new ArrayList<Object>();
						for (String s : players.keySet()) {
							if (players.containsKey(s)) {
								PVPPlayer pp = players.get(s);
								if (pp.arenaChoice!=-1) {
									choices.add(pp.arenaChoice);
								} else
								if (pp.choice!=PVPOption.NONE) {
									choices.add(pp.choice);
								}
							}
						}
						if (choices.size()==0) {
							PVPOption[] options = new PVPOption[]{PVPOption.OPENWORLD};
							battlefield = options[(int)(Math.random()*options.length)];
						} else {
							int choice = (int)(Math.random()*choices.size());
							if (choices.get(choice) instanceof PVPOption) {
								battlefield = PVPOption.OPENWORLD;
								currentArena=null;
							} else {
								currentArena = arenas.get((Integer)(choices.get(choice)));
							}
						}
						for (String s : players.keySet()) {
							Player p = Bukkit.getPlayer(s);
							if (p!=null && p.isValid() && p.isOnline()) {
								if (currentArena==null) {
									p.sendMessage(ChatColor.YELLOW+battlefield.getTitle()+ChatColor.GREEN+" has been voted as the battlefield for this PVP match!");
								} else {
									p.sendMessage(ChatColor.YELLOW+currentArena.getArenaName()+ChatColor.GREEN+" has been voted as the battlefield for this PVP match!");
								}
							}
						}
						state = CHOICEENGINE.PREPAREFORBATTLE;
						timer=TwosideKeeper.getServerTickTime();
						resetPlayerChoices();
						for (String s : players.keySet()) {
							Player pl = Bukkit.getPlayer(s);
							if (pl!=null && pl.isValid() && pl.isOnline()) {
								pl.sendMessage(ChatColor.YELLOW+"Prepare for Battle! "+ChatColor.RESET+ChatColor.ITALIC+"It will begin in 10 seconds... Good Luck!");
							}
						}
					} else {
						return DisbandMatch(ChatColor.YELLOW+"Not enough participants!"+ChatColor.GREEN+" The PVP Match has been disbanded.");
					}
				}
			}break;
			case PREPAREFORBATTLE:{
				if (timer+200<=TwosideKeeper.getServerTickTime()) {
					setupConditions();
					TransferPlayersToArena();
					state = CHOICEENGINE.FIGHTING;
				}
			}break;
			case FIGHTING:{
				updateBar();
				if (conditionsToWin() || notEnoughPlayers()) {
					if (players.size()>0) {
						StringBuilder sb = PrepareCurrentScores();
						Bukkit.getServer().broadcastMessage(sb.toString());
						aPlugin.API.discordSendRaw("```"+sb.toString()+"```");
					}
					giveBackInventories();
					computeWinner();
					//TwosideKeeper.log("Players: "+players, 1);
					announceWinner();
					resetTeams();
					if (matchTimer!=null) {
						matchTimer.removeAll();
					}
					return false;
				} else {
					setupNextRound();
					state = CHOICEENGINE.FINISHED;
					nextRoundTime = TwosideKeeper.getServerTickTime()+120;
				}
			}break;
			case FINISHED:{
				if (nextRoundTime<TwosideKeeper.getServerTickTime()) {
					state=CHOICEENGINE.FIGHTING;
				}
			}break;
		}
		return true;
	}

	private boolean DisbandMatch(String msg) {
		giveBackInventories();
		for (String s : players.keySet()) {
			Player pl = Bukkit.getPlayer(s);
			if (pl!=null && pl.isValid() && pl.isOnline()) {
				pl.sendMessage(msg);
				
			}
		}
		return false; //Cancelled.
	}

	private void giveBackInventories() {
		for (String s : players.keySet()) {
			PVPPlayer pp = players.get(s);
			Player p = Bukkit.getPlayer(s);
			if (p!=null && p.isOnline()) {
	    		for (int i=0;i<p.getInventory().getSize();i++) {
	    			p.getInventory().setItem(i, pp.original_inv.getItem(i));
	    		}
	    		InventoryView view = p.getOpenInventory();
	    		if (view!=null) {
	    			view.setCursor(new ItemStack(Material.AIR));
	    		}
			}
		}
	}

	private boolean everyoneIsReady() {
		for (String s : players.keySet()) {
			PVPPlayer pp = players.get(s);
			if (!pp.isReady) {
				return false;
			}
		}
		return true;
	}

	private void movePlayersOutsideArenaBackIn() {
		if (currentArena!=null) {
			for (String s : players.keySet()) {
				PVPPlayer pp = players.get(s);
				Player p = Bukkit.getPlayer(s);
				if (p!=null && pp.isAlive) {
					if (!currentArena.insideBounds(p.getLocation())) {
						p.teleport(currentArena.pickRandomLocation());
						p.sendMessage(ChatColor.RED+"You cannot leave the arena!");
					}
				}
			}
		} else {
			Location anchorpos = null;
			for (String s : players.keySet()) {
				if (anchorpos==null) {
					Player p = Bukkit.getPlayer(s);
					if (p!=null && p.isOnline()) {
						anchorpos=p.getLocation().clone();
					}
				}
				if (anchorpos!=null) {
					PVPPlayer pp = players.get(s);
					Player p = Bukkit.getPlayer(s);
					if (p!=null && pp.isAlive) {
						if (!anchorpos.getWorld().equals(p.getWorld()) || anchorpos.distanceSquared(p.getLocation())>1024) {
							p.teleport(anchorpos.add(Math.random()*32-16,0,Math.random()*32-16));
							Chunk c = anchorpos.getChunk();
							ChunkSnapshot cs = c.getChunkSnapshot();
							int highestY = cs.getHighestBlockYAt(Math.floorMod(anchorpos.getBlockX(),16), Math.floorMod(anchorpos.getBlockZ(),16));
							p.teleport(anchorpos.add(0, highestY-anchorpos.getBlockY()+2, 0));
							p.sendMessage(ChatColor.RED+"You cannot leave the arena!");
						}
					}
				}
			}
		}
	}

	private void moveToActiveSpectatorTarget() {
		for (String s : players.keySet()) {
			Player p = Bukkit.getPlayer(s);
			if (p!=null && p.getGameMode()==GameMode.SPECTATOR) {
				//p.setFlying(true);
				//p.setAllowFlight(true);
				//This is a spectator. Verify if they are watching an alive target.
				if (p.getSpectatorTarget()!=null &&
						p.getSpectatorTarget() instanceof Player) {
					Player watching = (Player)p.getSpectatorTarget();
					if (watching.isDead() && (players.containsKey(watching.getName()) &&
							!players.get(watching.getName()).isAlive)) {
						ChooseNewSpectatorTarget(p);
					}
				} else {
					ChooseNewSpectatorTarget(p);
				}
			}
		}
	}

	private void ChooseNewSpectatorTarget(Player p) {
		for (String s : players.keySet()) {
			PVPPlayer pp = players.get(s);
			if (pp.isAlive && Bukkit.getPlayer(s)!=null) {
				p.setSpectatorTarget(Bukkit.getPlayer(s));
			}
		}
	}

	private void updateBar() {
		if (matchTimer!=null) {
			if ((timelimit - TwosideKeeper.getServerTickTime())<=20*60) {
				matchTimer.setColor(BarColor.RED);
			}
			matchTimer.setProgress(Math.max((timelimit - TwosideKeeper.getServerTickTime()) / (double)duration,0));
			matchTimer.removeAll();
			for (String s : players.keySet()) {
				Player p = Bukkit.getPlayer(s);
				if (p!=null && p.isOnline()) {
					matchTimer.addPlayer(p);
				}
			}
		}
	}

	private void setupNextRound() {
		boolean deadTeam=false;
		if (scorematch) {
			if (players.size()>2) {
				if (AllPlayersOnTeamDead(1)) {
					team2score++;
					deadTeam=true;
				} else 
				if (AllPlayersOnTeamDead(2)){
					team1score++;
					deadTeam=true;
				}
			} else {
				for (String s : players.keySet()) {
					PVPPlayer pp = players.get(s);
					if (!pp.isAlive) {
						deadTeam=true;
					}
				}
			}
			if (deadTeam) {
				StringBuilder sb = PrepareCurrentScores();
				for (String s : players.keySet()) {
					PVPPlayer pp = players.get(s);
					Player p = Bukkit.getPlayer(s);
					PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
					p.setHealth(p.getMaxHealth());
					p.sendMessage(sb.toString());
					RemoveAllPlayerBuffs(p);
					pd.customtitle.modifySmallCenterTitle(ChatColor.GREEN+"Next match starting...", 100);
					for (int i=0;i<5;i++) {
						final int counter = i;
						Bukkit.getScheduler().runTaskLater(TwosideKeeper.plugin, ()->{
							if (p!=null && p.isOnline()) {
								pd.customtitle.modifyLargeCenterTitle(ChatColor.GREEN+Integer.toString(5-counter), 20);
								pd.customtitle.update();
							}
						}, 20*i);
					}
					Bukkit.getScheduler().runTaskLater(TwosideKeeper.plugin, ()->{
						if (p!=null && p.isOnline()) {
							pp.isAlive=true;
							p.setGameMode(GameMode.SURVIVAL);
							Location myLoc = p.getLocation().clone();
							//myLoc.setY(p.getLocation().getChunk().getChunkSnapshot().getHighestBlockYAt(Math.floorMod(p.getLocation().getBlockX(),16), Math.floorMod(p.getLocation().getBlockZ(),16)));
							if (currentArena==null) {
								p.teleport(pp.startingLoc.add(Math.random()*32-16,0,Math.random()*32-16));
								Chunk c = p.getLocation().getChunk();
								ChunkSnapshot cs = c.getChunkSnapshot();
								int highestY = cs.getHighestBlockYAt(Math.floorMod(p.getLocation().getBlockX(),16), Math.floorMod(p.getLocation().getBlockZ(),16));
								p.teleport(p.getLocation().add(0, highestY-p.getLocation().getBlockY()+2, 0));
							} else {
								respawnPlayer(p);
								//p.teleport(currentArena.pickRandomLocation());
							}
						}
					}, 120);
				}
			}
		}
	}

	private void respawnPlayer(Player p) {
		if (scorematch) {
			if (players.containsKey(p.getName())) {
				PVPPlayer pp = players.get(p.getName());
				TwosideKeeper.log("Team is "+pp.team, 1);
				if (pp.team!=0) {
					//TwosideKeeper.log("In here.", 1);
					p.teleport(currentArena.pickRandomTeamLocation(pp.team));
				} else {
					//This is not a team match.
					p.teleport(currentArena.pickRandomLocation());
				}
			} else {
				TwosideKeeper.log("WARNING! Could not find key "+p.getName()+" in active PVP Players! Just dropping them in...", 1);
				p.teleport(currentArena.pickRandomLocation());
			}
		} else {
			//This is Free For All.
			p.teleport(currentArena.pickRandomLocation());
		}
	}

	private StringBuilder PrepareCurrentScores() {
		StringBuilder sb = new StringBuilder("\n\n");
		sb.append("------- PVP Match -------\n");
		if ((scorematch && !isTeamMatch) || (!scorematch)) {
			DisplaySortedScoreboard(sb);
		} else {
			DisplayTeamScoreboard(sb);
		}
		sb.append("----- ");
		sb.append(style.getTitle());
		sb.append(" -----");
		return sb;
	}

	private void DisplayTeamScoreboard(StringBuilder sb) {
		List<Score> sortedscores = new ArrayList<Score>();
		if (team1score>team2score) {
			sortedscores.add(new Score(ChatColor.BLUE+"Team 1",team1score));
			sortedscores.add(new Score(ChatColor.RED+"Team 2",team2score));
		} else {
			sortedscores.add(new Score(ChatColor.RED+"Team 2",team2score));
			sortedscores.add(new Score(ChatColor.BLUE+"Team 1",team1score));
		}
		boolean alttext=false;
		for (Score s : sortedscores) {
			sb.append((alttext)?ChatColor.GRAY:ChatColor.WHITE);
			sb.append("  ");
			sb.append(s.name);
			sb.append(" ");
			for (int i=0;i<Math.max(16-s.name.length(), 1);i++) {
				sb.append(".");
			}
			sb.append(" ");
			sb.append(s.score);
			sb.append("  \n");
			alttext=!alttext;
		}
	}

	private void DisplaySortedScoreboard(StringBuilder sb) {
		List<Score> scorelist= new ArrayList<Score>();
		for (String s : players.keySet()) {
			PVPPlayer pp = players.get(s);
			scorelist.add(new Score(s,pp.score));
		}
		List<Score> sortedscores = new ArrayList<Score>();
		while (scorelist.size()>0) {
			boolean found=false;
			for (int i=0;i<sortedscores.size();i++) {
				if (scorelist.get(0).score>sortedscores.get(i).score) {
					sortedscores.add(i,scorelist.remove(0));
					found=true;
					break;
				}
			}
			if (!found) {
				sortedscores.add(scorelist.remove(0));
			}
		}
		boolean alttext=false;
		for (Score s : sortedscores) {
			sb.append((alttext)?ChatColor.GRAY:ChatColor.WHITE);
			sb.append("  ");
			sb.append(s.name);
			sb.append(" ");
			for (int i=0;i<Math.max(16-s.name.length(), 1);i++) {
				sb.append(".");
			}
			sb.append(" ");
			sb.append(s.score);
			sb.append("  \n");
			alttext=!alttext;
		}
	}

	private boolean AllPlayersOnTeamDead(int teamnumb) {
		List<String> members = getPlayersInTeam(teamnumb);
		for (String s : members) {
			if (players.containsKey(s)) {
				PVPPlayer pp = players.get(s);
				if (pp.isAlive) {
					return false;
				}
			} else {
				DebugUtils.showStackTrace();
				TwosideKeeper.log("WARNING! This PVP Player ("+s+") was on a team but is not valid!", 1);
				return false;
			}
		}
		return true;
	}

	private void resetTeams() {
		currentArena=null;
		for (String s : players.keySet()) {
			PVP.setTeam("NEUTRAL", s);
				Bukkit.getScheduler().runTaskLater(TwosideKeeper.plugin, ()->{
					if (Bukkit.getPlayer(s)!=null && Bukkit.getPlayer(s).isOnline()) {
						Bukkit.getPlayer(s).setGameMode(GameMode.SURVIVAL);
						Player p = Bukkit.getPlayer(s);
						PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
						TwosideKeeper.setPlayerMaxHealth(p, p.getHealth()/p.getMaxHealth(), true);
						p.teleport(pd.locBeforeInstance);
						pd.locBeforeInstance=null;
						pd.lastplayerHitBy=null;
						GenericFunctions.RevivePlayer(p, p.getMaxHealth());
					}
				}, 5);
		}
		for (String s : losers) {
			PVP.setTeam("NEUTRAL", s);
			Bukkit.getScheduler().runTaskLater(TwosideKeeper.plugin, ()->{
				if (Bukkit.getPlayer(s)!=null && Bukkit.getPlayer(s).isOnline()) {
					Bukkit.getPlayer(s).setGameMode(GameMode.SURVIVAL);
					Player p = Bukkit.getPlayer(s);
					PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
					if (p!=null && p.isOnline()) {
						TwosideKeeper.setPlayerMaxHealth(p, p.getHealth()/p.getMaxHealth(), true);
						if (pd.locBeforeInstance!=null) {
							p.teleport(pd.locBeforeInstance);
						}
						pd.lastplayerHitBy=null;
						pd.locBeforeInstance=null;
						GenericFunctions.RevivePlayer(p, p.getMaxHealth());
					}
				}
			}, 5);
		}
	}

	private void computeWinner() {
		if (scorematch) {
			if (team1score>team2score) {
				for (String s : getPlayersInTeam(2)) {
					losers.add(s);
					players.remove(s);
				}
			} else {
				for (String s : getPlayersInTeam(1)) {
					losers.add(s);
					players.remove(s);
				}
			}
		} else {
			int highestscore = Integer.MIN_VALUE;
			String highestscoreplayer = "";
			for (String s : players.keySet()) {
				PVPPlayer pp = players.get(s);
				if (pp.score>highestscore) {
					highestscoreplayer = s;
					highestscore = pp.score;
				}
			}
			List<String> removals = new ArrayList<String>();
			for (String s : players.keySet()) {
				if (!s.equalsIgnoreCase(highestscoreplayer)) {
					removals.add(s);
				}
			}
			for (String s : removals) {
				players.remove(s);
				losers.add(s);
			}
		}
	}

	private void RewardLoot(PVPOption matchStyle, boolean freshBlood, String player, boolean winner) {
		Player p = Bukkit.getPlayer(player);
		if (p!=null && p.isOnline()) {
			HashMap<ItemStack,Double> rewards = new HashMap<ItemStack,Double>();
			final ItemStack mysterious_essence = Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE);
			final ItemStack artifact_essence = Artifact.createArtifactItem(ArtifactItem.ARTIFACT_ESSENCE);
			final ItemStack artifact_core = Artifact.createArtifactItem(ArtifactItem.ARTIFACT_CORE);
			final ItemStack artifact_base = Artifact.createArtifactItem(ArtifactItem.ARTIFACT_BASE);
			final ItemStack battle_token = CustomItem.BattleToken();
			double moneymult = 1.0/(winner?1.0:2.0);
			switch (matchStyle) {
				case ROUNDS3:{
					if (winner) {
						addReward(rewards,mysterious_essence,1);
					} else {
						addReward(rewards,mysterious_essence,0.33);
					}
				}break;
				case ROUNDS5:
				case MIN3:{
					if (winner) {
						addReward(rewards,mysterious_essence,1);
						addReward(rewards,artifact_essence,0.25);
					} else {
						addReward(rewards,mysterious_essence,0.33);
					}
					moneymult = 1.2;
				}break;
				case ROUNDS7:
				case MIN5:{
					if (winner) {
						addReward(rewards,mysterious_essence,2);
						addReward(rewards,artifact_essence,0.5);
						addReward(rewards,artifact_core,0.25);
					} else {
						addReward(rewards,mysterious_essence,0.75);
					}
					moneymult = 1.4;
				}break;
				case ROUNDS15:
				case MIN10:{
					if (winner) {
						addReward(rewards,mysterious_essence,3);
						addReward(rewards,artifact_essence,1);
						addReward(rewards,artifact_core,0.50);
						addReward(rewards,artifact_base,0.25);
					} else {
						addReward(rewards,mysterious_essence,1);
					}
					moneymult = 2;
				}break;
				default:{
					if (winner) {
						addReward(rewards,mysterious_essence,1);
					} else {
						addReward(rewards,mysterious_essence,0.33);
					}
					TwosideKeeper.log("WARNING! Undefined rewards for PVP Style "+matchStyle.name()+". Giving default rewards.", 1);
				}
			}
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			applyFreshBloodMultiplier(rewards);
			if (pd.firstPVPMatch) {
				pd.firstPVPMatch=false;
			} else {
				applyAlreadyPlayedMultiplier(rewards);
			}
			if (winner) {
				addReward(rewards,battle_token,1);
			}
			List<ItemStack> rewardItems = new ArrayList<ItemStack>();
			giveRewards(rewards,rewardItems);
			if (rewardItems.size()>0) {
				GlobalLoot box = GlobalLoot.spawnGlobalLoot(pd.locBeforeInstance, ChatColor.RED+""+ChatColor.BOLD+"Battle Reward Chest");
				box.addNewDropInventory(p.getUniqueId(), rewardItems.toArray(new ItemStack[rewardItems.size()]));
			} else {
				double moneyEarned = Math.round(((((int)Math.random()*2)+2)*(players.size()+losers.size()))*moneymult);
				DecimalFormat df = new DecimalFormat("0.00");
				TwosideKeeperAPI.givePlayerMoney(p, moneyEarned);
				p.sendMessage(ChatColor.ITALIC+"You did not earn any loot this round, but you did earn $"+ChatColor.GREEN+df.format(moneyEarned)+ChatColor.RESET+".");
			}
		}
	}
	
	private void applyAlreadyPlayedMultiplier(HashMap<ItemStack, Double> rewards) {
		for (ItemStack i : rewards.keySet()) {
			rewards.put(i, rewards.get(i)*0.25);
		}
	}

	private void giveRewards(HashMap<ItemStack, Double> rewards, List<ItemStack> rewardItems) {
		for (ItemStack i : rewards.keySet()) {
			double amt = rewards.get(i);
			if (amt>=1) {
				ItemStack rewardItem = i.clone();
				rewardItem.setAmount((int)amt);
				rewardItems.add(rewardItem);
			}
			if (Math.random()<=(amt % 1)) {
				ItemStack rewardItem = i.clone();
				rewardItem.setAmount(1);
				rewardItems.add(rewardItem);
			}
		}
	}

	private void applyFreshBloodMultiplier(HashMap<ItemStack, Double> rewards) {
		if (freshBloodPlayer!=null) {
			for (ItemStack i : rewards.keySet()) {
				rewards.put(i, rewards.get(i)*2);
			}
		}
	}

	private void addReward(HashMap<ItemStack,Double> rewards, ItemStack reward, double reward_amt) {
		if (rewards.containsKey(reward)) {
			rewards.put(reward, rewards.get(reward)+reward_amt);
		} else {
			rewards.put(reward, reward_amt);
		}
	}

	private boolean conditionsToWin() {
		if (scorematch) {
			return (team1score>=scorelimit || team2score>=scorelimit) || (players.size()==2 && PlayerHasReachedScoreLimit());
		} else {
			return TwosideKeeper.getServerTickTime()>=timelimit;
		}
	}

	private boolean PlayerHasReachedScoreLimit() {
		for (String s : players.keySet()) {
			PVPPlayer pp = players.get(s);
			if (pp.score>=scorelimit) {
				return true;
			}
		}
		return false;
	}

	private void announceWinner() {
		String firstPlayer = null;
		determineWinnerByEliminatingLosers();
		for (String s : players.keySet()) {
			RewardLoot(style,freshBloodPlayer!=null,s,true);
		}
		for (String s : losers) {
			RewardLoot(style,freshBloodPlayer!=null,s,false);
		}
		TwosideKeeper.log("Players: "+players, 1);
		for (String s : players.keySet()) {
			firstPlayer = s;
			break;
		}
		if (firstPlayer!=null) {
			Player p = Bukkit.getPlayer(firstPlayer);
			List<Player> winners = PVP.getTeammates(p);
			List<String> winnernames = new ArrayList<String>();
			for (Player pl : winners) {
				//TwosideKeeper.log("Adding  "+pl.getName()+" to winners.", 1);
				winnernames.add(pl.getName());
			}
			StringBuilder sb = new StringBuilder(ChatColor.GREEN+"Congratulations to "+ChatColor.YELLOW);
			TextUtils.GenerateListOfItems(winnernames, sb);
			sb.append(ChatColor.GREEN+" for defeating "+ChatColor.RED);
			TextUtils.GenerateListOfItems(losers, sb);
			sb.append(ChatColor.GREEN+" in a PVP Match!");
			Bukkit.getServer().broadcastMessage(sb.toString());
			aPlugin.API.discordSendRawItalicized(sb.toString());
		} else {
			TwosideKeeper.log("WARNING! There were no winners!", 1);
		}
		freshBloodPlayer=null;
	}

	private void determineWinnerByEliminatingLosers() {
		if (!isTeamMatch) {
			String higherscoreplayer = "";
			int higherscore = Integer.MIN_VALUE;
			for (String s : players.keySet()) {
				PVPPlayer pp = players.get(s);
				if (pp.score>higherscore) {
					higherscoreplayer = s;
					higherscore = pp.score;
				}
			}
			List<String> removals = new ArrayList<String>();
			for (String s : players.keySet()) {
				if (!s.equalsIgnoreCase(higherscoreplayer)) {
					losers.add(s);
					removals.add(s);
				}
			}
			for (String s : removals) {
				players.remove(s);
			}
		} else {
			if (team1score>team2score) {
				for (String s : getPlayersInTeam(2)) {
					losers.add(s);
					players.remove(s);
				}
			} else {
				for (String s : getPlayersInTeam(1)) {
					losers.add(s);
					players.remove(s);
				}
			}
		}
	}

	public void removeInactivePlayers() {
		String removedPlayer = null;
		for (String s : players.keySet()) {
			Player p = Bukkit.getPlayer(s);
			if (p==null || !p.isOnline()) {
	    		PVPPlayer pp =  players.get(removedPlayer);
	    		/*for (int i=0;i<p.getInventory().getSize();i++) {
	    			if (ItemUtils.isValidItem(pp.original_inv.getItem(i))) {
	    				p.getInventory().setItem(i, pp.original_inv.getItem(i));
	    			}
	    		}*/
				removedPlayer = s;
				break;
			}
		}
		if (removedPlayer!=null) {
			losers.add(removedPlayer);
			players.remove(removedPlayer);
		}
	}

	private boolean notEnoughPlayers() {
		return players.size()<=1;
	}

	private void setupConditions() {
		if (style.name().contains("ROUNDS")) {
			scorelimit = Integer.parseInt(style.name().replace("ROUNDS", ""))/2+1;
			scorematch=true;
		} else {
			int minutes = Integer.parseInt(style.name().replaceAll("MIN", ""));
			duration = (20*60*minutes);
			timelimit = TwosideKeeper.getServerTickTime() + duration;
			matchTimer = Bukkit.createBossBar("Time Remaining", BarColor.GREEN, BarStyle.SEGMENTED_10, BarFlag.CREATE_FOG);
			matchTimer.setProgress((timelimit - TwosideKeeper.getServerTickTime()) / (double)duration);
			for (String s : players.keySet()) {
				Player p = Bukkit.getPlayer(s);
				if (p!=null && p.isOnline()) {
					matchTimer.addPlayer(p);
				}
			}
			scorematch=false;
		}
	}

	private boolean teamsAreInvalid() {
		if (getPlayersInTeam(1).size()==0 ||
				getPlayersInTeam(2).size()==0) {
			return true;
		} else {
			return false;
		}
	}

	private void randomlyPickTeams() {
		for (String s : players.keySet()) {
			PVPPlayer pp = players.get(s);
			if (pp.team==0) {
				if (getPlayersInTeam(1).size()<getPlayersInTeam(2).size()) {
					pp.team=1;
				} else {
					pp.team=2;
				}
			}
		}
	}

	private boolean allPlayersHaveChosenATeam() {
		for (String s : players.keySet()) {
			PVPPlayer pp = players.get(s);
			if (pp.team==0) {
				return false;
			}
		}
		return true;
	}

	private void TransferPlayersToArena() {
		Location baseloc = null;
		for (String s : players.keySet()) {
			PVPPlayer pp = players.get(s);
			if (baseloc==null && Bukkit.getPlayer(s)!=null) {
				pp.startingLoc =  Bukkit.getPlayer(s).getLocation().clone();
				baseloc = pp.startingLoc.clone();
			} else 
			if (baseloc!=null) {
				pp.startingLoc = baseloc;
			}
			if (Bukkit.getPlayer(s)!=null) {
				Player p = Bukkit.getPlayer(s);
				PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
				pd.locBeforeInstance = p.getLocation();
				p.setHealth(p.getMaxHealth());
				TwosideKeeper.setPlayerMaxHealth(p, p.getHealth()/p.getMaxHealth(), true);
				if (pp.team!=0) {
					String firstMember = GetFirstMemberOfTeam(pp.team);
					PVP.setTeam(firstMember+"_TEAM"+pp.team, Bukkit.getPlayer(s));
					if (pp.team==1) {
						pd.customtitle.modifySmallCenterTitle(ChatColor.BLUE+"You are on the blue team!", 60);
						pd.customtitle.update();
					} else {
						pd.customtitle.modifySmallCenterTitle(ChatColor.RED+"You are on the red team!", 60);
						pd.customtitle.update();
					}
				} else {
					PVP.setTeam(s+"_PVP", Bukkit.getPlayer(s));
				}
				pp.lastLoc = Bukkit.getPlayer(s).getLocation().clone();
				if (currentArena!=null) {
					respawnPlayer(p);
				}
				//TwosideKeeper.log("Set team of "+s+" to "+PVP.getTeam(Bukkit.getPlayer(s)), 2);
				Bukkit.getPlayer(s).sendMessage(ChatColor.GREEN+"The PVP Match between "+getParticipants()+" has begun!");
			}
		}
		aPlugin.API.discordSendRawItalicized("The PVP Match between **"+getParticipants()+"** has begun!");
	}

	private String GetFirstMemberOfTeam(int team) {
		for (String s : players.keySet()) {
			PVPPlayer pp = players.get(s);
			if (pp.team==team) {
				return s;
			}
		}
		TwosideKeeper.log("WARNING! Could not get first member of team. This should not be happening!",1);
		DebugUtils.showStackTrace();
		return null;
	}

	private void DisplayStageChoices() {
		for (String s : players.keySet()) {
			Player p = Bukkit.getPlayer(s);
			if (p!=null && p.isValid() && p.isOnline()) {
				p.sendMessage(ChatColor.GREEN+"Please pick a type of PVP Stage:");
				p.sendMessage("");
				TextComponent tc = PVPOption.OPENWORLD.getComponent();
				int arenaID = 0;
				for (PVPArena arena : arenas) {
					tc.addExtra(" ");tc.addExtra(arena.getComponent(arenaID++));
				}
				/*tc.addExtra(" ");tc.addExtra(PVPOption.SMALLBATTLEFIELD.getComponent());
				tc.addExtra(" ");tc.addExtra(PVPOption.AQUATICFORT.getComponent());
				tc.addExtra("\n");tc.addExtra(PVPOption.NETHERFORTRESS.getComponent());
				tc.addExtra(" ");tc.addExtra(PVPOption.THEEND.getComponent());*/
				p.spigot().sendMessage(tc);
			}
		}
	}

	private void DisplayTeamChoices() {
		for (String s : players.keySet()) {
			Player p = Bukkit.getPlayer(s);
			if (p!=null && p.isValid() && p.isOnline()) {
				p.sendMessage(ChatColor.GREEN+"Please pick a team:");
				TextComponent tc = PVPOption.TEAM1.getComponent();
				tc.addExtra(" ");tc.addExtra(PVPOption.TEAM2.getComponent());
				p.spigot().sendMessage(tc);
			}
		}
	}

	private void DisplayRoundChoices() {
		for (String s : players.keySet()) {
			Player p = Bukkit.getPlayer(s);
			if (p!=null && p.isValid() && p.isOnline()) {
				p.sendMessage(ChatColor.GREEN+"Please pick a type of PVP Round:");
				p.sendMessage("");
				TextComponent tc = PVPOption.ROUNDS3.getComponent();
				tc.addExtra(" ");tc.addExtra(PVPOption.ROUNDS5.getComponent());
				tc.addExtra(" ");tc.addExtra(PVPOption.ROUNDS7.getComponent());
				tc.addExtra("\n");tc.addExtra(PVPOption.ROUNDS15.getComponent());
				tc.addExtra(" ");tc.addExtra(PVPOption.MIN3.getComponent());
				tc.addExtra(" ");tc.addExtra(PVPOption.MIN5.getComponent());
				tc.addExtra("\n");tc.addExtra(PVPOption.MIN10.getComponent());
				p.spigot().sendMessage(tc);
			}
			GenericFunctions.deAggroNearbyTargets(p);
		}
	}
	
	private void resetPlayerChoices() {
		for (String p : players.keySet()) {
			PVPPlayer pp = players.get(p);
			pp.choice=PVPOption.NONE;
		}
	}
	
	private boolean allPlayersPicked() {
		for (String p : players.keySet()) {
			PVPPlayer pp = players.get(p);
			if (pp.choice==PVPOption.NONE && pp.arenaChoice==-1) {
				return false;
			}
		}
		return true;
	}

	public void addStageChoice(Player p, String choice) {
		if (players.containsKey(p.getName())) {
			PVPPlayer pp = players.get(p.getName());
			pp.arenaChoice = 9000-Integer.parseInt(choice);
			p.sendMessage(ChatColor.GRAY+""+ChatColor.ITALIC+"Your choice for "+ChatColor.GREEN+arenas.get(pp.arenaChoice).getArenaName()+ChatColor.RESET+" has been entered.");
		}
	}
	
	public void addChoice(Player p, String choice) {
		if (players.containsKey(p.getName())) {
			PVPPlayer pp = players.get(p.getName());
			PVPOption option = PVPOption.valueOf(choice);
			if (ValidateChoice(option)) {
				pp.choice = option;
				lastSelected=TwosideKeeper.getServerTickTime();
				if (state!=CHOICEENGINE.WAITFORTEAMCHOICES) {
					p.sendMessage(ChatColor.GRAY+""+ChatColor.ITALIC+"Your choice for "+ChatColor.GREEN+option.getTitle()+ChatColor.RESET+" has been entered.");
				} else {
					pp.team=pp.choice.selection_numb;
					SendTeamList();
					for (String s : players.keySet()) {
						Player pl = Bukkit.getPlayer(s);
						if (pl!=null) {
							pl.sendMessage(ChatColor.GREEN+"Please pick a team:");
							TextComponent tc = PVPOption.TEAM1.getComponent();
							tc.addExtra(" ");tc.addExtra(PVPOption.TEAM2.getComponent());
							pl.spigot().sendMessage(tc);
						}
					}
				}
				timer=TwosideKeeper.getServerTickTime();
			} else {
				p.sendMessage(ChatColor.RED+"That is not a valid option!");
			}
		}
	}

	private void SendTeamList() {
		List<String> team1 = getPlayersInTeam(1);
		List<String> team2 = getPlayersInTeam(2);
		StringBuilder sb = new StringBuilder();
		sb.append(ChatColor.BLUE+"\n"+ChatColor.UNDERLINE+"  Team 1   \n"+ChatColor.RESET+ChatColor.AQUA);
		if (team1.size()>0) {
			AppendTeam(team1, sb);
		} else {
			sb.append(ChatColor.ITALIC+"No One"+ChatColor.RESET);
		}
		sb.append("\n\n"+ChatColor.RED+""+ChatColor.UNDERLINE+"  Team 2   \n"+ChatColor.RESET+ChatColor.GOLD);
		if (team2.size()>0) {
			AppendTeam(team2, sb);
		} else {
			sb.append(ChatColor.ITALIC+"No One"+ChatColor.RESET);
		}
		sb.append("\n");
		for (String s : players.keySet()) {
			Player p = Bukkit.getPlayer(s);
			PVPPlayer pp = players.get(s);
			if (p!=null) {
				p.sendMessage(sb.toString());
				ColorGearBasedOnTeam(p,pp.team);
			}
		}
	}

	private void AppendTeam(List<String> team1, StringBuilder sb) {
		for (int i=0;i<team1.size();i++) {
			if (i==0) {
				sb.append(team1.get(i));
			} else {
				if (team1.size()==2) {
					sb.append(" and "+team1.get(i));
				} else {
					if (i==team1.size()-1) {
						sb.append(", and "+team1.get(i));
					} else {
						sb.append(", "+team1.get(i));
					}
				}
			}
		}
	}

	private List<String> getPlayersInTeam(int i) {
		List<String> teams = new ArrayList<String>();
		for (String s : players.keySet()) {
			PVPPlayer pp = players.get(s);
			if (pp.team==i) {
				teams.add(s);
			}
		}
		return teams;
	}

	private boolean ValidateChoice(PVPOption option) {
		List<PVPOption> options1 = ImmutableList.of(PVPOption.ROUNDS3,PVPOption.ROUNDS5,PVPOption.ROUNDS7,
				PVPOption.ROUNDS15,PVPOption.MIN3,PVPOption.MIN5,PVPOption.MIN10);
		List<PVPOption> options2 = ImmutableList.of(PVPOption.OPENWORLD,PVPOption.SMALLBATTLEFIELD,PVPOption.AQUATICFORT,
				PVPOption.NETHERFORTRESS,PVPOption.THEEND);
		List<PVPOption> options3 = ImmutableList.of(PVPOption.TEAM1,PVPOption.TEAM2);
		switch (state) {
			case WAITFORROUNDCHOICES:{
				return options1.contains(option);
			}
			case WAITFORSTAGECHOICES:{
				return options2.contains(option);
			}
			case WAITFORTEAMCHOICES:{
				return options3.contains(option);
			}
		}
		return true;
	}

	public static boolean isWaitingForPlayers(Player p) {
		for (PVP session : TwosideKeeper.pvpsessions) {
			if (session.state == CHOICEENGINE.WAITFORPLAYERS &&
					session.players.containsKey(p.getName())) {
				return true;
			}
		}
		return false;
	}
	
	public static PVP getMatch(Player p) {
		for (PVP session : TwosideKeeper.pvpsessions) {
			if (session.players.containsKey(p.getName())) {
				return session;
			}
		}
		return null;
	}
	
	public static void InitializeTeams() {
		teams = new HashMap<String,String>();
	}
	
	public static void addPlayerToTeamStructure(Player p) {
		if (!teams.containsKey(p.getName())) {
			teams.put(p.getName(), "NEUTRAL");
		}
	}
	
	public static void removePlayerFromTeamStructure(Player p) {
		if (teams.containsKey(p.getName())) {
			teams.remove(p.getName());
		}
	}
	
	public static String getTeam(Player p) {
		addPlayerToTeamStructure(p);
		return teams.get(p.getName());
	}
	
	public static boolean isPvPing(Player p) {
		if (p!=null) {
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			if (pd.temporaryPVP) {
				return true;
			} else {
				addPlayerToTeamStructure(p);
				return !teams.get(p.getName()).equalsIgnoreCase("NEUTRAL") || PVP.getMatch(p)!=null;
			}
		} else {
			return false;
		}
	}
	
	public static List<Player> getTeammates(Player p) {
		if (isPvPing(p)) {
			List<Player> teammates = new ArrayList<Player>();
			String myTeam = teams.get(p.getName());
			//teammates.add(p);
			for (String pl : teams.keySet()) {
				String team = teams.get(pl);
				if (myTeam.equalsIgnoreCase(team)) {
					teammates.add(Bukkit.getPlayer(pl));
				}
			}
			return teammates;
		} else {
			return PartyManager.getPartyMembers(p);
		}
	}
	
	public static List<Player> getEnemies(Player p) {
		if (isPvPing(p)) {
			List<Player> enemies = new ArrayList<Player>();
			String myTeam = teams.get(p.getName());
			enemies.add(p);
			for (String pl : teams.keySet()) {
				String team = teams.get(pl);
				if (!myTeam.equalsIgnoreCase(team)) {
					enemies.add(Bukkit.getPlayer(pl));
				}
			}
			return enemies;
		} else {
			return new ArrayList<Player>();
		}
	}
	
	public static void setTeam(String teamName, Player p) {
		teams.put(p.getName(), teamName);
	}

	private static void setTeam(String teamName, String s) {
		teams.put(s, teamName);
	}
	
	public static void setOnSameTeam(Player p, Player...allies) {
		addPlayerToTeamStructure(p);
		String myTeam = getTeam(p);
		for (Player pl : allies) {
			setTeam(myTeam,pl);
		}
	}
	
	public static boolean isFriendly(Player p, Player checkEnemy) {
		return !isEnemy(p,checkEnemy);
	}
	
	public static boolean isEnemy(Player p, Player checkEnemy) {
		addPlayerToTeamStructure(p);
		addPlayerToTeamStructure(checkEnemy);
		return !getTeam(p).equalsIgnoreCase(getTeam(checkEnemy)) && !getTeam(checkEnemy).equalsIgnoreCase("NEUTRAL") && !getTeam(p).equalsIgnoreCase("NEUTRAL");
	}

	public static void sendPvPRequest(Player attacker, Player defender) {
		attacker.sendMessage(ChatColor.GRAY+""+ChatColor.ITALIC+"Sending a PvP Request to "+ChatColor.YELLOW+defender.getName());
		defender.sendMessage(ChatColor.YELLOW+attacker.getName()+ChatColor.GREEN+" is requesting a PVP match. ");
		TextComponent base = new TextComponent("    ");
		TextComponent tc = new TextComponent(ChatColor.GREEN+"[Accept]");
		tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/pvp "+attacker.getName()));
		TextComponent tc2 = new TextComponent(ChatColor.RED+"[Deny]");
		tc2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/pvp _DENY_ "+attacker.getName()));
		base.addExtra(tc);
		base.addExtra("    ");
		base.addExtra(tc2);
		defender.spigot().sendMessage(base);
	}

	public void onDeathEvent(Player p) {
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		if (pd.lastplayerHitBy!=null &&
				players.containsKey(pd.lastplayerHitBy)) {
			String killedByPlayer = pd.lastplayerHitBy;
			PVPPlayer killer = players.get(killedByPlayer);
			killer.score++;
			PVPPlayer myself = players.get(p.getName());
			if (!scorematch) {
				myself.score--;
				StringBuilder sb = PrepareCurrentScores();
				//myself.respawnTimer = TwosideKeeper.getServerTickTime()+120;
				pd.customtitle.modifySmallCenterTitle(ChatColor.GREEN+"Respawning...", 100);
				for (int i=0;i<5;i++) {
					final int counter = i;
					Bukkit.getScheduler().runTaskLater(TwosideKeeper.plugin, ()->{
						if (p!=null && p.isOnline()) {
							pd.customtitle.modifyLargeCenterTitle(ChatColor.GREEN+Integer.toString(5-counter), 20);
							pd.customtitle.update();
						}
					}, 20*i);
				}
				Bukkit.getScheduler().runTaskLater(TwosideKeeper.plugin, ()->{
					if (p!=null && p.isOnline()) {
						myself.isAlive=true;
						p.setGameMode(GameMode.SURVIVAL);
						Location myLoc = p.getLocation().clone();
						RemoveAllPlayerBuffs(p);
						//myLoc.setY(p.getLocation().getChunk().getChunkSnapshot().getHighestBlockYAt(Math.floorMod(p.getLocation().getBlockX(),16), Math.floorMod(p.getLocation().getBlockZ(),16)));
						if (currentArena==null) {
							p.teleport(myself.startingLoc.add(Math.random()*32-16,0,Math.random()*32-16));
							Chunk c = p.getLocation().getChunk();
							ChunkSnapshot cs = c.getChunkSnapshot();
							int highestY = cs.getHighestBlockYAt(Math.floorMod(p.getLocation().getBlockX(),16), Math.floorMod(p.getLocation().getBlockZ(),16));
							p.teleport(p.getLocation().add(0, highestY-p.getLocation().getBlockY()+2, 0));
						} else {
							//p.teleport(currentArena.pickRandomLocation());
							respawnPlayer(p);
						}
					}
				}, 120);
				for (String s : players.keySet()) {
					Player pl = Bukkit.getPlayer(s);
					if (pl!=null && pl.isOnline()) {
						pl.sendMessage(sb.toString());
					}
				}
			}
			myself.isAlive=false;
			p.setGameMode(GameMode.SPECTATOR);
			p.setFallDistance(0.0f);
			p.setSpectatorTarget(Bukkit.getPlayer(killedByPlayer));
			StringBuilder sb = new StringBuilder("  Killed by ");
			StringBuilder sb2 = new StringBuilder("  Killed ");
			sb.append(ChatColor.RED);
			sb.append(killedByPlayer);
			sb.append(ChatColor.RESET);
			sb2.append(ChatColor.GREEN);
			sb2.append(p.getName());
			sb2.append(ChatColor.RESET);
			if (pd.lastPVPHitDamage>0) {
				DecimalFormat df = new DecimalFormat("0.00");
				sb.append(" for ");
				sb.append(ChatColor.RED);
				sb.append(df.format(pd.lastPVPHitDamage));
				sb.append(ChatColor.RESET);
				sb.append(" damage");
				sb2.append(" for ");
				sb2.append(ChatColor.RED);
				sb2.append(df.format(pd.lastPVPHitDamage));
				sb2.append(ChatColor.RESET);
				sb2.append(" damage");
			}
			if (pd.lastPVPHitReason!=null) {
				sb.append(" (");
				sb.append(pd.lastPVPHitReason);
				sb.append(").");
				sb2.append(" (");
				sb2.append(pd.lastPVPHitReason);
				sb2.append(").");
			} else {
				sb.append(".");
				sb2.append(".");
			}
			p.sendMessage(sb.toString());
			Player killerp = Bukkit.getPlayer(pd.lastplayerHitBy);
			if (killerp!=null) {
				killerp.sendMessage(sb2.toString());
				PlayerStructure killerpd = PlayerStructure.GetPlayerStructure(killerp);
				killerpd.damagepool=0;
				killerpd.customtitle.update();
			}
		} else {
			if (players.containsKey(p.getName())) {
				if (currentArena!=null) {
					//p.teleport(currentArena.pickRandomLocation());
					respawnPlayer(p);
				}
			}
		}
		/*if (getPlayersInTeam(1).contains(killer)) {
			team1score++;
		} else {
			team2score++;
		}*/
	}

	public void addReadyChoice(Player p, String string) {
		if (string.equalsIgnoreCase("YES")) {
			if (players.containsKey(p.getName())) {
				PVPPlayer pp = players.get(p.getName());
				if (!pp.isReady) {
					pp.isReady=true;
					for (String s : players.keySet()) {
						PVPPlayer pp2 = players.get(s);
						Player p2 = Bukkit.getPlayer(s);
						if (p2!=null && p2.isOnline()) {
							p2.sendMessage(" "+ChatColor.YELLOW+((freshBloodPlayer!=null && freshBloodPlayer.equalsIgnoreCase(p.getName()))?"*":"")+p.getName()+ChatColor.RESET+" is "+ChatColor.GREEN+"READY"+ChatColor.RESET+".");
							if (!pp2.isReady) {
								showReadyChoice(s);
							}
						}
					}
				}
			}
		} else {
			leaveMatch(p.getName());
		}
	}
}

class PVPPlayer {
	int score;
	Location startingLoc;
	Location lastLoc;
	PVPOption choice;
	int arenaChoice;
	int team;
	boolean isAlive;
	long respawnTimer;
	boolean isReady;
	Inventory original_inv;
	
	PVPPlayer() {
		score=0;
		startingLoc=null;
		choice=PVPOption.NONE;
		lastLoc=null;
		team=0;
		isAlive=true;
		respawnTimer=0;
		arenaChoice=-1;
		isReady=false;
	}
}

enum CHOICEENGINE {
	WAITFORPLAYERS, //Waits 10 seconds for any additional players to join in.
	WAITFORROUNDCHOICES, //Asks all participants to submit a round choice. (10 seconds Max)
	WAITFORTEAMCHOICES, //Asks all participants to submit a team choice.
	WAITFORSTAGECHOICES, //Asks all participants to submit a stage choice. (10 seconds Max)
	PREPAREFORBATTLE, //Gives players 10 seconds before transferring them.
	FIGHTING, //The actual fighting
	FINISHED, //Any cleanup that has to be done. Warp all players back.
}

class Score{
	String name;
	int score;
	Score(String name, int score) {
		this.name=name;
		this.score=score;
	}
}

enum PVPOption {
	NONE(0,"No Choice","Hey look! It's my favorite streamer! The one and only >>Legendary<<, yes LEGENDARY Captain_Marrow aka Storm! We break ultimate plates, alchemize Artifacts, miss skillshots, break Master Swords, collect food because we cannot dodge the Ganon, deny our combat-buddies from keeping their legendary equipment, and jump off boats right before they leave ( cy@ VoHiYo )! Come join the fun!   Kappa"),
	ROUNDS3(1,ChatColor.WHITE+"Best of 3","The team that wins 2 of 3 total rounds wins the duel."),
	ROUNDS5(2,ChatColor.GRAY+"Best of 5","The team that wins 3 of 5 total rounds wins the duel."),
	ROUNDS7(3,ChatColor.WHITE+"Best of 7","The team that wins 4 of 7 total rounds wins the duel."),
	ROUNDS15(4,ChatColor.GRAY+"Best of 15","The team that wins 8 of 15 total rounds wins the duel."),
	MIN3(5,ChatColor.WHITE+"3 Min Deathmatch","The player that gets the highest score within 3 minutes wins the duel. "+ChatColor.GREEN+"+1 Point per Kill"+ChatColor.RESET+", "+ChatColor.RED+"-1 Point per Death",20*60*3),
	MIN5(6,ChatColor.GRAY+"5 Min Deathmatch","The player that gets the highest score within 5 minutes wins the duel. "+ChatColor.GREEN+"+1 Point per Kill"+ChatColor.RESET+", "+ChatColor.RED+"-1 Point per Death",20*60*5),
	MIN10(7,ChatColor.WHITE+"10 Min Deathmatch","The player that gets the highest score within 10 minutes wins the duel. "+ChatColor.GREEN+"+1 Point per Kill"+ChatColor.RESET+", "+ChatColor.RED+"-1 Point per Death",20*60*10),
	OPENWORLD(1,ChatColor.WHITE+"Open World","Fight in the current location you are located at."+ChatColor.RED+"\n  NOTE: "+ChatColor.WHITE+"You may not wander more than 16 blocks away from your starting battle location."),
	SMALLBATTLEFIELD(2,ChatColor.GRAY+"Small Battlefield","Fight in a small, instanced battlefield"),
	AQUATICFORT(3,ChatColor.WHITE+"Aquatic Fort","Fight in a small, decorated fort with a moat surrounding the area."),
	NETHERFORTRESS(4,ChatColor.GRAY+"Nether Fortress","Fight in a medium-sized fortress sitting upon the fiery flames of hell."),
	THEEND(5,ChatColor.WHITE+"The End","This is where all battles come to a spectacular end. A medium platform suspended high into the void. Falling off is inevitable."),
	TEAM1(1,ChatColor.GRAY+"Team 1",""),
	TEAM2(2,ChatColor.WHITE+"Team 2","");
	
	int selection_numb;
	String short_desc;
	String hover_desc;
	long time;
	
	PVPOption(int numb, String desc, String hover) {
		this.selection_numb=numb;
		this.short_desc=desc;
		this.hover_desc=hover;
		this.time=0;
	}

	PVPOption(int numb, String desc, String hover, long time) {
		this.selection_numb=numb;
		this.short_desc=desc;
		this.hover_desc=hover;
		this.time=time;
	}
	int getSelectionNumber() {
		return selection_numb;
	}
	
	String getTitle() {
		return short_desc;
	}
	
	String getHoverDescription() {
		return hover_desc;
	}
	
	TextComponent getComponent() {
		TextComponent tc = new TextComponent("["+short_desc+"]");
		tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/pvp _TYPE_ "+name()));
		tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ComponentBuilder(hover_desc).create()));
		return tc;
	}
}
