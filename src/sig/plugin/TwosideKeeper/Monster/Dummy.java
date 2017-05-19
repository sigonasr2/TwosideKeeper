package sig.plugin.TwosideKeeper.Monster;

import java.util.List;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.util.Vector;

import sig.plugin.TwosideKeeper.CustomMonster;
import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.TwosideKeeperAPI;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.TextUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.Classes.MovementModifier;

public class Dummy extends CustomMonster{
	final static int maxhp = 1000000;
	double dmgdealt = 0;
	long timeStartedHitting = 0;
	long lastHitTime = 0;
	int numbOfHits = 0;
	Location spawnLoc;
	List<Player> playerHitList = new ArrayList<Player>();

	public Dummy(LivingEntity m) {
		super(m);
		//m.setCustomName(ChatColor.MAGIC+" "+ChatColor.RESET+"Test Dummy"+ChatColor.MAGIC+" ");
		ChatColor randcolor = TextUtils.RandomColor();
		TwosideKeeperAPI.setCustomLivingEntityName(m, randcolor+""+ChatColor.MAGIC+" "+ChatColor.RESET+randcolor+"Test Dummy"+ChatColor.MAGIC+" ");
		m.setCustomNameVisible(true);
		this.spawnLoc = m.getLocation();
		//m.setCollidable(false);
		m.setMaxHealth(maxhp);
		m.setHealth(m.getMaxHealth());
		m.setRemoveWhenFarAway(false);
		if (m instanceof Villager) {
			Villager v = (Villager)m;
			v.setAdult();
			v.setProfession(Profession.PRIEST);
		}
	}
	
	public static boolean isDummy(LivingEntity m) {
		boolean isDummy = (m.getMaxHealth()==maxhp && m instanceof Villager && ((Villager)m).getProfession()==Profession.PRIEST);
		return isDummy;
	}
	
	public void runTick() {
		if (numbOfHits>0 && lastHitTime+20*5<=TwosideKeeper.getServerTickTime()) {
			for (Player p : playerHitList) {
				p.sendMessage(getDamageOutput());
			}
			numbOfHits=0;
		}
		m.setHealth(maxhp);
		m.teleport(spawnLoc);
		Bukkit.getScheduler().runTaskLater(TwosideKeeper.plugin, new MovementModifier(m,new Vector(0,0,0)), 1);
	}
	
	private String getDamageOutput() {
		StringBuilder sb = new StringBuilder();
		sb.append(m.getCustomName());
		sb.append(" Results:");
		sb.append("\n");
		sb.append(ChatColor.GREEN);
		sb.append("Total Damage: ");
		DecimalFormat df = new DecimalFormat("0.00");
		sb.append(df.format(dmgdealt));
		sb.append("\n");
		sb.append(ChatColor.YELLOW);
		sb.append("DPS: ");
		long duration = (TwosideKeeper.getServerTickTime()-timeStartedHitting)/20; //In seconds.
		sb.append(df.format(dmgdealt/duration));
		sb.append("\n");
		sb.append(ChatColor.GRAY);
		sb.append("Hits: ");
		sb.append(numbOfHits);
		sb.append(" (");
		sb.append(" Avg ");
		sb.append(df.format(dmgdealt/numbOfHits));
		sb.append(" dmg)");
		return sb.toString();
	}
	
	public void addPlayerToHitList(Player p) {
		if (!playerHitList.contains(p)) {
			playerHitList.add(p);
		}
	}

	public void customHitHandler(double dmg) {
		super.customHitHandler(dmg);
		
		if (numbOfHits==0) {
			timeStartedHitting=TwosideKeeper.getServerTickTime();
			dmgdealt=0;
		}

		lastHitTime=TwosideKeeper.getServerTickTime();
		numbOfHits++;
		dmgdealt+=dmg;
	}
}
