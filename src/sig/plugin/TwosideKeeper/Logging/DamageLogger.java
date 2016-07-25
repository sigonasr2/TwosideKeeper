package sig.plugin.TwosideKeeper.Logging;

import java.text.DecimalFormat;
import java.util.HashMap;

import org.bukkit.entity.Player;

import com.google.common.collect.Iterables;

import net.md_5.bungee.api.ChatColor;
import sig.plugin.TwosideKeeper.PlayerStructure;
import sig.plugin.TwosideKeeper.TwosideKeeper;

public class DamageLogger {
	HashMap<String,Double> breakdownlist;
	double totaldmg;
	double actualtotaldmg=0.0;
	double calculatedtotaldmg=0.0;
	double totalmult=0.0;
	int totalhits=0;
	String player;
	long recordtime;
	
	public DamageLogger(Player p) {
		this.breakdownlist=new HashMap<String,Double>();
		totaldmg=0;
		this.player=p.getName();
		this.recordtime=TwosideKeeper.getServerTickTime();
	}
	
	public void startRecording() {
		this.totaldmg=0;
		this.calculatedtotaldmg=0.0;
		this.actualtotaldmg=0.0;
		this.breakdownlist.clear();
		this.totalmult=0.0;
		this.totalhits=0;
		this.recordtime=TwosideKeeper.getServerTickTime();
	}

	public void addMultiplierToLogger(String name, double val) {
		if (val!=1.0) {
			if (breakdownlist.containsKey(name)) {
				//Add to the already existing value.
				double dmg = breakdownlist.get(name);
				dmg+=val;
				breakdownlist.put(name, dmg);
			} else {
				breakdownlist.put(name, val);
			}
			totalmult+=val;
		}
	}
	
	public void addEventToLogger(String name, double val) {
		if (val!=0.0) {
			if (breakdownlist.containsKey(name)) {
				//Add to the already existing value.
				double dmg = breakdownlist.get(name);
				dmg+=val;
				breakdownlist.put(name, dmg);
			} else {
				breakdownlist.put(name, val);
			}
			totaldmg+=val;
		}
	}

	public void addCalculatedActualDamage(double val) {
		this.actualtotaldmg+=val;
		this.totalhits++;
	}
	
	public void addCalculatedTotalDamage(double val) {
		this.calculatedtotaldmg+=val;
	}
	
	public String OutputResults() {
		StringBuilder finalstring = new StringBuilder();
		DecimalFormat df = new DecimalFormat("0.00");
		for (int i=0;i<breakdownlist.size();i++) {
			if (((String)breakdownlist.keySet().toArray()[i]).contains("Mult")) {
				if (breakdownlist.get(breakdownlist.keySet().toArray()[i])!=1.0d && ((this.actualtotaldmg-this.totaldmg)*(breakdownlist.get(breakdownlist.keySet().toArray()[i])/this.totalmult))>0) {
					finalstring.append(breakdownlist.keySet().toArray()[i]+": "+getPercentColor(breakdownlist.get(breakdownlist.keySet().toArray()[i]),totalmult)+"x"+df.format(breakdownlist.get(breakdownlist.keySet().toArray()[i])/this.totalhits)+" - "+df.format(((this.actualtotaldmg-this.totaldmg)*(breakdownlist.get(breakdownlist.keySet().toArray()[i])/this.totalmult)))+" dmg");
				}
			} else {
				if (breakdownlist.get(breakdownlist.keySet().toArray()[i])!=0.0d) {
					finalstring.append(breakdownlist.keySet().toArray()[i]+": "+getPercentColor(breakdownlist.get(breakdownlist.keySet().toArray()[i]),totaldmg)+df.format(breakdownlist.get(breakdownlist.keySet().toArray()[i])));
				}
			}
			finalstring.append("\n");
		}
		finalstring.append(ChatColor.GRAY+""+ChatColor.BOLD+"  Raw Damage: "+df.format(actualtotaldmg)+"\n");
		finalstring.append(ChatColor.GOLD+""+ChatColor.ITALIC+"  Final Damage: "+df.format(calculatedtotaldmg)+" (Average "+df.format((1-(this.calculatedtotaldmg/this.actualtotaldmg))*100)+"% Reduction)\n");
		double elapsedtime = ((TwosideKeeper.getServerTickTime()-recordtime)/20d);
		double dps = actualtotaldmg/elapsedtime;
		finalstring.append(ChatColor.YELLOW+"  Elapsed Time: "+ChatColor.AQUA+df.format(elapsedtime)+"s "+ChatColor.WHITE+"("+df.format(dps)+" damage/sec)");
		return finalstring.toString();
	}

	private ChatColor getPercentColor(Double val, Double total) {
		if (val/total>=0.9) {
			return ChatColor.DARK_RED;
		}else
		if (val/total>=0.8) {
			return ChatColor.RED;
		}else
		if (val/total>=0.7) {
			return ChatColor.GOLD;
		}else
		if (val/total>=0.5) {
			return ChatColor.YELLOW;
		}else
		if (val/total>=0.3) {
			return ChatColor.GREEN;
		}else
		if (val/total>=0.2) {
			return ChatColor.AQUA;
		}else
		if (val/total>=0.1) {
			return ChatColor.DARK_AQUA;
		}
		return ChatColor.GRAY;
	}
	
	public static void AddNewCalculation(Player p, String name, double val, double reducedval) {
		PlayerStructure pd = TwosideKeeper.playerdata.get(p.getUniqueId());
		pd.damagedata.addEventToLogger(name, val);
		pd.damagedata.addCalculatedActualDamage(val);
		pd.damagedata.addCalculatedTotalDamage(reducedval);
	}
}
