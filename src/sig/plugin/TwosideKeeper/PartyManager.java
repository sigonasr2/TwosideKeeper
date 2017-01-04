package sig.plugin.TwosideKeeper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;

public class PartyManager {
	static int totalparties=0;
	static HashMap<Integer, List<Player>> parties = new HashMap<Integer, List<Player>>();
	static HashMap<Integer, List<Player>> oldparties = new HashMap<Integer, List<Player>>();
	static HashMap<Integer, List<Player>> sortedorder = new HashMap<Integer, List<Player>>();
	public static void SetupParties() {
		//Place players into numbered parties.
		totalparties=0;
		ClearAllParties();
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (!IsInParty(p)) {
				//We only care about adding a player that's not in a party already.
				//We have to make a new party for this player.
				AddPlayerToParty(p,totalparties++);
				//Now find nearby players and add them to this party.
				AddNearbyPlayersToSameParty(p);
			}
		}
		UpdatePartyScoreboards();
	}
	
	private static void UpdatePartyScoreboards() {
		for (int i : parties.keySet()) {
			if (oldparties.containsKey(i)) {
				if (PartiesAreDifferent(parties.get(i),oldparties.get(i))) {
					UpdateScoreboard(i,parties.get(i),oldparties.get(i));
				} else {
					TwosideKeeper.log("Parties are the same!", 5);
				}
			} else {
				UpdateScoreboard(i,parties.get(i),oldparties.get(i));
			}
			sortPlayers(i, parties.get(i), sortedorder);
		}
	}

	private static void UpdateScoreboard(int party, List<Player> partymembers, List<Player> oldmembers) {
		String color = ConvertColor(party);
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "scoreboard objectives remove Party"+party); //Make sure the party is cleared out if it was used for something before...
		//Bukkit.getScoreboardManager().getMainScoreboard().registerNewObjective("Party"+color, "dummy");
		//Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "scoreboard objectives add Party"+party+" dummy "+ColorPartyListDisplay(partymembers)+"");
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "scoreboard objectives add Party"+party+" dummy "+ChatColor.BLUE+""+ChatColor.BOLD+ChatColor.UNDERLINE+"Party "+ChatColor.RESET+ChatColor.DARK_GRAY+ChatColor.UNDERLINE+ChatColor.ITALIC+PartyBonusDisplay(partymembers)+"");
		//Bukkit.getScoreboardManager().getMainScoreboard().getObjective("Party"+color).setDisplaySlot(DisplaySlot.SIDEBAR);
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "scoreboard objectives setdisplay sidebar.team."+color+" Party"+party);
		
		for (int i=0;i<partymembers.size();i++) {
			Player p = partymembers.get(i);
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			pd.partybonus = (partymembers.size()>=2)?partymembers.size()-1:0;
			TwosideKeeper.log("Party bonus is "+pd.partybonus, 5);
			TwosideKeeper.log("Adding Player "+p.getName()+" to Scoreboard..", 5);
			Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "scoreboard players set "+p.getName().toLowerCase()+" Party"+party+" "+((i+1)*-1));
			Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "scoreboard teams option "+p.getName().toLowerCase()+" color "+color);
			p.getScoreboard().getTeam(p.getName().toLowerCase()).setAllowFriendlyFire(true);
			p.getScoreboard().getTeam(p.getName().toLowerCase()).setSuffix(TwosideKeeper.createHealthbar(((p.getHealth())/p.getMaxHealth())*100,p));
			TwosideKeeper.setPlayerMaxHealth(p);
			p.getScoreboard().getTeam(p.getName().toLowerCase()).setPrefix(GenericFunctions.PlayerModePrefix(p));
		}
	}
	
	private static String PartyBonusDisplay(List<Player> partymembers) {
		int membercount = partymembers.size();
		StringBuilder partydisplay = new StringBuilder("");
		if (membercount>=2) {
			int dmgbonus=((membercount-1)<=6)?(membercount-1)*10:60;
			partydisplay.append(" +"+dmgbonus+"%DMG/DEF");
		}
		return partydisplay.toString();
	}

	public static void sortPlayers(int party, List<Player> partyplayers, HashMap<Integer,List<Player>> lastorder) {
		//Sorts the players on the scoreboard by proper health values.
		List<Player> sortedorder = new ArrayList<Player>();
		int lasti=-1; //The last player that had less health than you did.
		for (int i=0;i<partyplayers.size();i++) {
			for (int j=0;j<sortedorder.size();j++) {
				if (sortedorder.get(j).getHealth()>partyplayers.get(i).getHealth()) {
					lasti=-1;
				} else {
					lasti=j; //This means our health is bigger. We go here.
					break;
				}
			}
			if (lasti==-1) {
				//This just gets inserted.
				sortedorder.add(partyplayers.get(i));
			} else {
				sortedorder.add(lasti,partyplayers.get(i));
			}
		}
		TwosideKeeper.log("Sorted Order: "+sortedorder.toString(), 5);
		if (lastorder.get(party)==null) {
			lastorder.put(party,new ArrayList<Player>());
		}
		if (!lastorder.get(party).equals(sortedorder)) {
			for (int i=0;i<sortedorder.size();i++) {
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "scoreboard players set "+sortedorder.get(i).getName().toLowerCase()+" Party"+party+" "+((i+1)*-1));
			}
			lastorder.put(party,sortedorder);
		}
	}

	private static String ConvertColor(int i) {
		switch (i%16) {
			case 0:{
				return "white";
			}
			case 1:{
				return "yellow";
			}
			case 2:{
				return "light_purple";
			}
			case 3:{
				return "red";
			}
			case 4:{
				return "aqua";
			}
			case 5:{
				return "green";
			}
			case 6:{
				return "blue";
			}
			case 7:{
				return "dark_gray";
			}
			case 8:{
				return "gray";
			}
			case 9:{
				return "gold";
			}
			case 10:{
				return "dark_purple";
			}
			case 11:{
				return "dark_red";
			}
			case 12:{
				return "dark_aqua";
			}
			case 13:{
				return "dark_green";
			}
			case 14:{
				return "dark_blue";
			}
			case 15:{
				return "black";
			}
			default:{
				return "white";
			}
		}
	}

	private static boolean PartiesAreDifferent(List<Player> newparty, List<Player> oldparty) {
		if (newparty.size()==oldparty.size()) {
			return !newparty.containsAll(oldparty);
		} else {
			return true;
		}
	}

	private static void ClearAllParties() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			pd.previousparty=pd.currentparty;
			pd.currentparty=-1;
		}
		oldparties.clear();
		oldparties.putAll(parties);
		parties.clear();
	}
	
	private static void AddNearbyPlayersToSameParty(Player sourcep) {
		for (Player checkp : Bukkit.getOnlinePlayers()) {
			if (!IsInParty(checkp) && checkp.getWorld().equals(sourcep.getWorld()) && sourcep.getLocation().distanceSquared(checkp.getLocation())<=Math.pow(TwosideKeeper.PARTY_CHUNK_SIZE,2)) {
				AddPlayerToParty(checkp,GetCurrentParty(sourcep));
				AddNearbyPlayersToSameParty(checkp);
			}
		}
	}
	
	private static int GetCurrentParty(Player sourcep) {
		return PlayerStructure.GetPlayerStructure(sourcep).currentparty;
	}
	
	private static void AddPlayerToParty(Player p, int i) {
		TwosideKeeper.log("Added player to party "+i, 5);
		PlayerStructure.GetPlayerStructure(p).currentparty=i;
		AddToPartyMap(p,i);
	}
	
	private static void AddToPartyMap(Player p, int i) {
		List<Player> playerlist = parties.get(i);
		if (parties.get(i)==null) {
			playerlist = new ArrayList<Player>();
		}
		playerlist.add(p);
		parties.put(i, playerlist);
	}

	private static boolean IsInParty(Player p) {
		return PlayerStructure.GetPlayerStructure(p).currentparty!=-1;
	}
	
	public static boolean IsInSameParty(Player p, Player p2) {
		return PlayerStructure.GetPlayerStructure(p).currentparty==PlayerStructure.GetPlayerStructure(p2).currentparty;
	}

	public static List<Player> getPartyMembers(Player p) {
		int partynumb = GetCurrentParty(p);
		return parties.get(partynumb);
	}
}