package sig.plugin.TwosideKeeper.HelperStructures;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import sig.plugin.TwosideKeeper.PlayerStructure;
import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.PlayerUtils;

public class AdvancedTitle {
	TitlePart large_lefttitle = new TitlePart("",0);
	TitlePart large_centertitle = new TitlePart("",0);
	TitlePart large_righttitle = new TitlePart("",0);
	TitlePart small_centertitle = new TitlePart("",0);
	Player p;
	
	public AdvancedTitle(Player p) {
		this.p=p;
	}
	
	public void modifyLargeLeftTitle(String title, int duration) {
		large_lefttitle.setTitle(title);
		large_lefttitle.setDuration(duration);
		update();
	}
	public void modifyLargeCenterTitle(String title, int duration) {
		large_centertitle.setTitle(title);
		large_centertitle.setDuration(duration);
		update();
	}
	public void modifyLargeRightTitle(String title, int duration) {
		large_righttitle.setTitle(title);
		large_righttitle.setDuration(duration);
		update();
	}
	public void modifySmallCenterTitle(String title, int duration) {
		small_centertitle.setTitle(title);
		small_centertitle.setDuration(duration);
		update();
	}
	
	public void checkExpiredTitles() {
		boolean refreshnames=false;
		if (large_lefttitle.isExpired()) {
			large_lefttitle.clear();
			refreshnames=true;
		}
		if (large_centertitle.isExpired()) {
			large_centertitle.clear();
			refreshnames=true;
		}
		if (large_righttitle.isExpired()) {
			large_righttitle.clear();
			refreshnames=true;
		}
		if (small_centertitle.isExpired()) {
			small_centertitle.clear();
			refreshnames=true;
		}
		if (refreshnames) {
			update();
		}
	}

	public void update() {
		p.sendTitle(combineLargeTitles(), combineSmallTitles());
	}

	private String combineSmallTitles() {
		StringBuilder sb = new StringBuilder(small_centertitle.getTitle());
		return sb.toString();
	}

	private String combineLargeTitles() {
		StringBuilder sb = new StringBuilder(large_lefttitle.getTitle());
		sb.append(ChatColor.RESET);
		if (large_lefttitle.getTitle().length()>0 ||
				large_righttitle.getTitle().length()>0) {
			if (large_centertitle.getTitle().length()==0) {
				sb.append("         ");
			} else {
				sb.append("   ");
				sb.append(large_centertitle.getTitle());
				sb.append(ChatColor.RESET);
				sb.append("   ");
			}
		} else {
			sb.append(large_centertitle.getTitle());
			sb.append(ChatColor.RESET);
		}
		sb.append(large_righttitle.getTitle());
		return sb.toString();
	}

	public void updateCombatBar(Player p, LivingEntity target) {
		updateSideTitleStats(p);
		TwosideKeeper.updateHealthbarDisplay(p, target);
		update();
	}
	
	public void updateCombatBar(Player p, LivingEntity target, double damage, int flags) {
		updateSideTitleStats(p);
		TwosideKeeper.updateHealthbarDisplay(p, target, damage, flags);
		update();
	}

	public void updateSideTitleStats(Player p) {
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		if (PlayerMode.getPlayerMode(p)==PlayerMode.BARBARIAN) {
			modifyLargeLeftTitle(ChatColor.DARK_AQUA+"☤"+Integer.toString((int)pd.weaponcharges),100);
			modifyLargeRightTitle(ChatColor.DARK_PURPLE+Integer.toString((int)pd.damagepool)+"♥",100);
		}
		if (PlayerMode.getPlayerMode(p)==PlayerMode.DEFENDER &&
				ItemSet.hasFullSet(p, ItemSet.SONGSTEEL)) {
			modifyLargeLeftTitle(ChatColor.YELLOW+Integer.toString((int)pd.vendetta_amt),100);
			modifyLargeRightTitle(ChatColor.GOLD+Integer.toString((int)pd.thorns_amt),100);
		}
	}
	
	public String[] getTitles() {
		return new String[]{large_lefttitle.getTitle(),large_centertitle.getTitle(),large_righttitle.getTitle()};
	}
}

class TitlePart {
	String title;
	long expiretime;
	boolean hasExpired=false;
	
	TitlePart(String title, int duration) {
		this.title=title;
		this.expiretime=TwosideKeeper.getServerTickTime()+duration;
	}
	
	public void clear() {
		title="";
		hasExpired=true;
	}

	void setTitle(String title) {
		this.title=title;
	}
	
	void setDuration(int duration) {
		this.expiretime=TwosideKeeper.getServerTickTime()+duration;
		hasExpired=false;
	}
	
	boolean alreadyExpired() {
		return hasExpired;
	}
	
	boolean isExpired() {
		return !hasExpired && expiretime<=TwosideKeeper.getServerTickTime();
	}
	
	String getTitle() {
		return title;
	}
	
	long getExpirationTime() {
		return expiretime;
	}
}