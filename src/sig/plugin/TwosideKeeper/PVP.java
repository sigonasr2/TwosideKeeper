package sig.plugin.TwosideKeeper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.google.common.collect.ImmutableList;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class PVP {
	static HashMap<String,String> teams; //<Player Name, Team Name>
	HashMap<String,PVPPlayer> players = new HashMap<String,PVPPlayer>();
	PVPOption style;
	PVPOption battlefield;
	CHOICEENGINE state;
	long timer;
	private long lastSelected=0;
	int scorelimit;
	long timelimit;
	boolean scorematch = false; //If true, uses score limit. If false uses timer.
	
	//NEUTRAL team
	//Team1
	//Team2 etc
	
	public PVP(Player...players) {
		for (Player p : players) {
			this.players.put(p.getName(),new PVPPlayer());
			p.sendMessage(ChatColor.GREEN+"Waiting for any additional players to join the PVP Match...");
			p.sendMessage(ChatColor.GREEN+"Players must click on a participant to join in.");
		}
		state = CHOICEENGINE.WAITFORPLAYERS;
		timer = TwosideKeeper.getServerTickTime();
	}
	
	public void joinMatch(Player p) {
		if (!players.containsKey(p.getName())) {
			players.put(p.getName(), new PVPPlayer());
			for (String s : players.keySet()) {
				Player pl = Bukkit.getPlayer(s);
				if (pl!=null && pl.isValid() && pl.isOnline()) {
					pl.sendMessage(ChatColor.YELLOW+p.getName()+" has joined the match. Current Participants: "+ChatColor.YELLOW+getParticipants());
				} else {
					//pl.sendMessage(ChatColor.YELLOW+s+ChatColor.GOLD+" has left the PVP Match...");
					leaveMatch(s);				
				}
			}
			timer = TwosideKeeper.getServerTickTime();
		}
	}
	
	private void leaveMatch(String s) {
		TwosideKeeper.ScheduleRemoval(players,s);
		Bukkit.getScheduler().runTaskLater(TwosideKeeper.plugin, ()->{
			for (String ss : players.keySet()) {
				Player pl = Bukkit.getPlayer(ss);
				if (pl!=null && pl.isValid() && pl.isOnline()) {
					pl.sendMessage(ChatColor.YELLOW+ss+" has left the match. Current Participants: "+ChatColor.YELLOW+getParticipants());
				} else {
					leaveMatch(ss);			
				}
			}
		}, 2);
	}

	private String getParticipants() {
		StringBuilder sb = new StringBuilder("");
		int count=0;
		for (String s : players.keySet()) {
			if (sb.length()==0) {
				sb.append(s);
			} else {
				if (players.size()==2) {
					sb.append(" and ");
					sb.append(s);
				} else {
					if (count+1==players.size()) {
						sb.append(", and ");
						sb.append(s);
					} else {
						sb.append(", ");
						sb.append(s);
					}
				}
			}
			count++;
		}
		return sb.toString();
	}

	public boolean runTick() {
		switch (state) {
			/*case ACCEPTREQUEST:{
				if (timer+200<=TwosideKeeper.getServerTickTime()) {
					return false; //Cancelled.
				}
			}break;*/
			case WAITFORPLAYERS:{
				if (timer+200<=TwosideKeeper.getServerTickTime()) {
					if (players.size()>=2) {
						state = CHOICEENGINE.WAITFORROUNDCHOICES;
						timer=TwosideKeeper.getServerTickTime();
						resetPlayerChoices();
						DisplayRoundChoices();
					} else {
						for (String s : players.keySet()) {
							Player pl = Bukkit.getPlayer(s);
							if (pl!=null && pl.isValid() && pl.isOnline()) {
								pl.sendMessage(ChatColor.YELLOW+"Not enough participants!"+ChatColor.GREEN+" The PVP Match has been disbanded.");
							}
						}
						return false; //Cancelled.
					}
				}
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
						for (String s : players.keySet()) {
							Player pl = Bukkit.getPlayer(s);
							if (pl!=null && pl.isValid() && pl.isOnline()) {
								pl.sendMessage(ChatColor.YELLOW+"Not enough participants!"+ChatColor.GREEN+" The PVP Match has been disbanded.");
							}
						}
						return false; //Cancelled.
					}
				}
			}break;
			case WAITFORTEAMCHOICES:{
				if (lastSelected+100<=TwosideKeeper.getServerTickTime()) {
					if (allPlayersHaveChosenATeam() || lastSelected+4000<=TwosideKeeper.getServerTickTime()) {
						randomlyPickTeams();
						if (teamsAreInvalid()) {
							for (String s : players.keySet()) {
								Player p = Bukkit.getPlayer(s);
								if (p!=null) {
									p.sendMessage("Not enough players in both teams to begin a PvP Match! The match has been cancelled.");
								}
							}
							return false;
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
							PVPOption[] options = new PVPOption[]{PVPOption.OPENWORLD,PVPOption.SMALLBATTLEFIELD,
									PVPOption.AQUATICFORT,PVPOption.NETHERFORTRESS,PVPOption.THEEND};
							battlefield = options[(int)(Math.random()*options.length)];
						} else {
							battlefield = choices.get((int)(Math.random()*choices.size()));
						}
						for (String s : players.keySet()) {
							Player p = Bukkit.getPlayer(s);
							if (p!=null && p.isValid() && p.isOnline()) {
								p.sendMessage(ChatColor.YELLOW+battlefield.getTitle()+ChatColor.GREEN+" has been voted as the battlefield for this PVP match!");
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
						for (String s : players.keySet()) {
							Player pl = Bukkit.getPlayer(s);
							if (pl!=null && pl.isValid() && pl.isOnline()) {
								pl.sendMessage(ChatColor.YELLOW+"Not enough participants!"+ChatColor.GREEN+" The PVP Match has been disbanded.");
							}
						}
						return false; //Cancelled.
					}
				}
			}break;
			case PREPAREFORBATTLE:{
				if (timer+200<=TwosideKeeper.getServerTickTime()) {
					TransferPlayersToArena();
					state = CHOICEENGINE.FIGHTING;
				}
			}break;
		}
		return true;
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
				if (pp.team!=0) {
					PVP.setTeam(s+"_TEAM"+pp.team, Bukkit.getPlayer(s));
				} else {
					PVP.setTeam(s+"_PVP", Bukkit.getPlayer(s));
				}
				pp.lastLoc = Bukkit.getPlayer(s).getLocation().clone();
				Bukkit.getPlayer(s).sendMessage(ChatColor.GREEN+"The PVP Match between "+getParticipants()+" has begun!");
				aPlugin.API.discordSendRawItalicized("The PVP Match between **"+getParticipants()+"** has begun!");
			}
		}
	}

	private void DisplayStageChoices() {
		for (String s : players.keySet()) {
			Player p = Bukkit.getPlayer(s);
			if (p!=null && p.isValid() && p.isOnline()) {
				p.sendMessage(ChatColor.GREEN+"Please pick a type of PVP Stage:");
				p.sendMessage("");
				TextComponent tc = PVPOption.OPENWORLD.getComponent();
				tc.addExtra(" ");tc.addExtra(PVPOption.SMALLBATTLEFIELD.getComponent());
				tc.addExtra(" ");tc.addExtra(PVPOption.AQUATICFORT.getComponent());
				tc.addExtra("\n");tc.addExtra(PVPOption.NETHERFORTRESS.getComponent());
				tc.addExtra(" ");tc.addExtra(PVPOption.THEEND.getComponent());
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
			if (pp.choice==PVPOption.NONE) {
				return false;
			}
		}
		return true;
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
			if (p!=null) {
				p.sendMessage(sb.toString());
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
		addPlayerToTeamStructure(p);
		return !teams.get(p.getName()).equalsIgnoreCase("NEUTRAL") || PVP.getMatch(p)!=null; 
	}
	
	public static List<Player> getTeammates(Player p) {
		if (isPvPing(p)) {
			List<Player> teammates = new ArrayList<Player>();
			String myTeam = teams.get(p.getName());
			teammates.add(p);
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
		return !getTeam(p).equalsIgnoreCase(getTeam(checkEnemy)) && !getTeam(checkEnemy).equalsIgnoreCase("NEUTRAL");
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
}

class PVPPlayer {
	int score;
	Location startingLoc;
	Location lastLoc;
	PVPOption choice;
	int team;
	
	PVPPlayer() {
		score=0;
		startingLoc=null;
		choice=PVPOption.NONE;
		lastLoc=null;
		team=0;
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

enum PVPOption {
	NONE(0,"No Choice","Hey look! It's my favorite streamer! The one and only >>Legendary<<, yes LEGENDARY Captain_Marrow aka Storm! We break ultimate plates, alchemize Artifacts, miss skillshots, break Master Swords, collect food because we cannot dodge the Ganon, deny our combat-buddies from keeping their legendary equipment, and jump off boats right before they leave ( cy@ VoHiYo )! Come join the fun!   Kappa"),
	ROUNDS3(1,ChatColor.WHITE+"Best of 3","The team that wins 2 of 3 total rounds wins the duel."),
	ROUNDS5(2,ChatColor.GRAY+"Best of 5","The team that wins 3 of 5 total rounds wins the duel."),
	ROUNDS7(3,ChatColor.WHITE+"Best of 7","The team that wins 4 of 7 total rounds wins the duel."),
	ROUNDS15(4,ChatColor.GRAY+"Best of 15","The team that wins 8 of 15 total rounds wins the duel."),
	MIN3(5,ChatColor.WHITE+"3 Min Deathmatch","The player that gets the highest score within 3 minutes wins the duel. "+ChatColor.GREEN+"+1 Point per Kill"+ChatColor.RESET+", "+ChatColor.RED+"-1 Point per Death",20*60*3),
	MIN5(6,ChatColor.GRAY+"5 Min Deathmatch","The player that gets the highest score within 5 minutes wins the duel. "+ChatColor.GREEN+"+1 Point per Kill"+ChatColor.RESET+", "+ChatColor.RED+"-1 Point per Death",20*60*5),
	MIN10(7,ChatColor.WHITE+"10 Min Deathmatch","The player that gets the highest score within 10 minutes wins the duel. "+ChatColor.GREEN+"+1 Point per Kill"+ChatColor.RESET+", "+ChatColor.RED+"-1 Point per Death",20*60*10),
	OPENWORLD(1,ChatColor.WHITE+"Open World","Fight in the current location you are located at."+ChatColor.RED+"\n  NOTE: "+ChatColor.WHITE+"You may not wander more than 32 blocks away from your starting battle location."),
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
