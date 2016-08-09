package sig.plugin.TwosideKeeper;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;

public class Party {
	public List<Player> partyplayers;
	public List<Player> lastorder;
	int color;
	Location region;
	
	Party(int color, Location rawPos) {
		partyplayers = new ArrayList<Player>();
		lastorder = new ArrayList<Player>();
		rawPos.setX((int)(rawPos.getX()/(16*TwosideKeeper.PARTY_CHUNK_SIZE))*(16*TwosideKeeper.PARTY_CHUNK_SIZE));
		rawPos.setZ((int)(rawPos.getZ()/(16*TwosideKeeper.PARTY_CHUNK_SIZE))*(16*TwosideKeeper.PARTY_CHUNK_SIZE));
		region=rawPos;
		TwosideKeeper.log("Party Region Position: "+region.toString(),5);
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "scoreboard objectives remove Party"+color); //Make sure the party is cleared out if it was used for something before...
		//Bukkit.getScoreboardManager().getMainScoreboard().registerNewObjective("Party"+color, "dummy");
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "scoreboard objectives add Party"+color+" dummy Party");
		//Bukkit.getScoreboardManager().getMainScoreboard().getObjective("Party"+color).setDisplaySlot(DisplaySlot.SIDEBAR);
		String color_txt = "";
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "scoreboard objectives setdisplay sidebar.team."+ConvertColor(color)+" Party"+color);
		this.color=color;
	}
	
	String ConvertColor(int val) {

		switch (val%16) {
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
	
	public void addPlayer(Player p) {
		partyplayers.add(p);
		for (int l=0;l<partyplayers.size();l++) {
		    	PlayerStructure pd = (PlayerStructure)TwosideKeeper.playerdata.get(partyplayers.get(l).getUniqueId());
				pd.currentparty=TeamNumber();
				pd.partybonus=partyplayers.size()-1;
				//TwosideKeeper.playerdata.get(k).partybonus=10; //JUST FOR TESTING PURPOSES.
				if (partyplayers.size()>=2) {
					//partyplayers.get(l).sendMessage(ChatColor.ITALIC+""+ChatColor.GOLD+"Party Bonuses Applied: "+ChatColor.BLUE+"+"+(partyplayers.size()-1)+"0% damage + defense for "+partyplayers.size()+" party members. Drop Rate +"+(partyplayers.size()-1)+"0%");
				}
		}
		Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "scoreboard players set "+p.getName().toLowerCase()+" Party"+color+" "+partyplayers.size()*-1);
		Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "scoreboard teams option "+p.getName().toLowerCase()+" color "+ConvertColor(color));
		p.getScoreboard().getTeam(p.getName().toLowerCase()).setAllowFriendlyFire(false);
		p.getScoreboard().getTeam(p.getName().toLowerCase()).setSuffix(TwosideKeeper.createHealthbar(((p.getHealth())/p.getMaxHealth())*100,p));
		TwosideKeeper.setPlayerMaxHealth(p);
		p.getScoreboard().getTeam(p.getName().toLowerCase()).setPrefix(GenericFunctions.PlayerModePrefix(p));
	}
	
	public void sortPlayers() {
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
		if (!lastorder.equals(sortedorder)) {
			for (int i=0;i<sortedorder.size();i++) {
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "scoreboard players set "+sortedorder.get(i).getName().toLowerCase()+" Party"+color+" "+(i+1)*-1);
			}
			lastorder.clear();
			lastorder.addAll(sortedorder);
		}
	}
	
	public Location getRegion() {
		return region;
	}
	public boolean IsInParty(Player p) {
		if (partyplayers.contains(p)) {
			return true;
		} else {
			return false;
		}
	}
	public boolean IsInSameRegion(Player p) {
		if (((int)(p.getLocation().getX()/(16*TwosideKeeper.PARTY_CHUNK_SIZE))*(16*TwosideKeeper.PARTY_CHUNK_SIZE)) == (int)region.getX() &&
				((int)(p.getLocation().getZ()/(16*TwosideKeeper.PARTY_CHUNK_SIZE))*(16*TwosideKeeper.PARTY_CHUNK_SIZE)) == (int)region.getZ() &&
				p.getLocation().getWorld() == region.getWorld()) {
			return true;
		} else {
			return false;
		}
	}
	public int PlayerCountInParty() {
		return partyplayers.size();
	}
	public int TeamNumber() {
		return color;
	}
	public void UpdateRegion(Location newloc) {
		newloc.setX((int)(newloc.getX()/(16*TwosideKeeper.PARTY_CHUNK_SIZE))*(16*TwosideKeeper.PARTY_CHUNK_SIZE));
		newloc.setZ((int)(newloc.getZ()/(16*TwosideKeeper.PARTY_CHUNK_SIZE))*(16*TwosideKeeper.PARTY_CHUNK_SIZE));
		region = newloc;
		TwosideKeeper.log("Region Updated: "+region.toString(),5);
	}
	public void RemoveStrayMembers() {
		int prevsiz=partyplayers.size();
		TwosideKeeper.log("Previous party size was "+prevsiz,5);
		for (int i=0;i<partyplayers.size();i++) {
			if (partyplayers.get(i)==null ||
					!partyplayers.get(i).isOnline() ||
					partyplayers.get(i).getWorld() != region.getWorld() ||
					((int)(partyplayers.get(i).getLocation().getX()/(16*TwosideKeeper.PARTY_CHUNK_SIZE))*(16*TwosideKeeper.PARTY_CHUNK_SIZE)) != (int)region.getX() ||
					((int)(partyplayers.get(i).getLocation().getZ()/(16*TwosideKeeper.PARTY_CHUNK_SIZE))*(16*TwosideKeeper.PARTY_CHUNK_SIZE)) != (int)region.getZ()) {
				TwosideKeeper.log(((int)(partyplayers.get(i).getLocation().getX()/(16*TwosideKeeper.PARTY_CHUNK_SIZE))*(16*TwosideKeeper.PARTY_CHUNK_SIZE))+" ;; "+((int)(partyplayers.get(i).getLocation().getZ()/(16*TwosideKeeper.PARTY_CHUNK_SIZE))*(16*TwosideKeeper.PARTY_CHUNK_SIZE))+" - DID NOT MATCH",4);
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "scoreboard players reset "+partyplayers.get(i).getName().toLowerCase()+" Party"+color);
				for (int l=0;l<partyplayers.size();l++) {
			    	PlayerStructure pd = (PlayerStructure)TwosideKeeper.playerdata.get(partyplayers.get(l).getUniqueId());
					if (pd!=null && pd.currentparty == TeamNumber()) {
						if (partyplayers.size()-2<0) {
							pd.partybonus=0;
						} else {
							pd.partybonus=partyplayers.size()-2;
						}
						pd.currentparty=-1;
					}
				}
				if (partyplayers.size()>=2) {
					//partyplayers.get(i).sendMessage(ChatColor.DARK_GRAY+""+ChatColor.ITALIC+"Party buffs removed.");
				}
				if (partyplayers.get(i)!=null &&
						partyplayers.get(i).isOnline()) {
					partyplayers.get(i).getScoreboard().getTeam(partyplayers.get(i).getName().toLowerCase()).setSuffix(TwosideKeeper.createHealthbar(((partyplayers.get(i).getHealth())/partyplayers.get(i).getMaxHealth())*100,partyplayers.get(i)));
					TwosideKeeper.setPlayerMaxHealth(partyplayers.get(i));
					partyplayers.get(i).getScoreboard().getTeam(partyplayers.get(i).getName().toLowerCase()).setPrefix(GenericFunctions.PlayerModePrefix(partyplayers.get(i)));
				}
				partyplayers.remove(i);
				i--;
			}
		}
		if (prevsiz!=partyplayers.size()) {
			for (int i=0;i<partyplayers.size();i++) {
				if (partyplayers.size()==1) {
					//partyplayers.get(i).sendMessage(ChatColor.DARK_GRAY+""+ChatColor.ITALIC+"Party buffs removed.");
				} else {
			    	PlayerStructure pd = (PlayerStructure)TwosideKeeper.playerdata.get(partyplayers.get(i).getUniqueId());
					pd.partybonus=partyplayers.size()-1;
					//partyplayers.get(i).sendMessage(ChatColor.ITALIC+""+ChatColor.GOLD+"Party Bonuses Applied: "+ChatColor.BLUE+"+"+(partyplayers.size()-1)+"0% damage + defense for "+partyplayers.size()+" party members. Drop Rate +"+(partyplayers.size()-1)+"0%");
				}
			}
		}
	}
	public static List<Player> getPartyMembers(Player p) {
		//Find the party this player is in.
		Party part = null;
		for (int i=0;i<TwosideKeeper.PartyList.size();i++) {
			if (TwosideKeeper.PartyList.get(i).IsInParty(p)) {
				return TwosideKeeper.PartyList.get(i).partyplayers;
			}
		}
		return null;
	}
}
