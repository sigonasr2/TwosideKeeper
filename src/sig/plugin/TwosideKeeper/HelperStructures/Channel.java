package sig.plugin.TwosideKeeper.HelperStructures;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import sig.plugin.TwosideKeeper.LivingEntityStructure;
import sig.plugin.TwosideKeeper.PlayerStructure;
import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.Events.EntityChannelCastEvent;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;

public class Channel {
	LivingEntity l;
	String channelName;
	int duration;
	long channelStartTime;
	BossBar channelBar;
	boolean cancelled=false;
	
	public Channel(LivingEntity l, String spellName, int tickDuration) {
		this.l=l;
		this.channelName=spellName;
		this.duration=tickDuration;
		this.channelStartTime=TwosideKeeper.getServerTickTime();
		if (l instanceof Player) {
			Player p = (Player)l;
			channelBar = l.getServer().createBossBar(ChatColor.YELLOW+channelName, BarColor.YELLOW, BarStyle.SOLID, BarFlag.CREATE_FOG);
			channelBar.addPlayer(p);
			UpdateBossBar();
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			pd.currentChannel=this;
		} else {
			UpdateMobChannelingBar(l);
			l.setCustomNameVisible(true);
			LivingEntityStructure les = LivingEntityStructure.GetLivingEntityStructure(l);
			les.currentChannel=this;
		}
		GenericFunctions.addSuppressionTime(l, duration);
		AddToStructure();
	}
	
	public static Channel createNewChannel(LivingEntity l, String spellName, int channelDuration){
		return new Channel(l,spellName,channelDuration);
	}
	
	public static boolean isChanneling(LivingEntity l) {
		if (l instanceof Player) {
			PlayerStructure pd = PlayerStructure.GetPlayerStructure((Player)l);
			return pd.currentChannel!=null;
		} else {
			LivingEntityStructure les = LivingEntityStructure.GetLivingEntityStructure(l);
			return les.currentChannel!=null;
		}
	}
	
	public static void stopChanneling(LivingEntity l) {
		if (isChanneling(l)) {
			Channel c = getCurrentChannel(l);
			c.setCancelled(true);
		}
	}
	
	public static Channel getCurrentChannel(LivingEntity l) {
		if (isChanneling(l)) {
			if (l instanceof Player) {
				PlayerStructure pd = PlayerStructure.GetPlayerStructure((Player)l);
				return pd.currentChannel;
			} else {
				LivingEntityStructure les = LivingEntityStructure.GetLivingEntityStructure(l);
				return les.currentChannel;
			}
		} else {
			return null;
		}
	}

	private void AddToStructure() {
		TwosideKeeper.channels.add(this);
	}

	private void UpdateMobChannelingBar(LivingEntity l) {
		LivingEntityStructure.setChannelingBar(l, getMobChannelingBar());
	}

	private void UpdateBossBar() {
		long tickTimeChanneled = TwosideKeeper.getServerTickTime()-channelStartTime;
		double ratio = (double)tickTimeChanneled/duration;
		channelBar.setProgress(ratio);
	}
	
	private String getMobChannelingBar() {
		StringBuilder sb = new StringBuilder();
		sb.append(ChatColor.YELLOW);
		sb.append(channelName);
		sb.append("[");
		long tickTimeChanneled = TwosideKeeper.getServerTickTime()-channelStartTime;
		double ratio = (double)tickTimeChanneled/duration;
		int bars = (int)(ratio*10);
		for (int i=0;i<bars;i++) {
			sb.append("█");
		}
		for (int i=0;i<10-bars;i++) {
			sb.append("-");
		}
		sb.append("]");
		sb.append(ChatColor.RESET);
		return sb.toString();
	}
	
	public void setCancelled(boolean isCancelled) {
		cancelled=isCancelled;
	}
	
	public LivingEntity getLivingEntity() {
		return l;
	}

	public boolean runTick() {
		if (l==null || !l.isValid() || cancelled) {
			return false;
		}
		if (channelStartTime+duration<TwosideKeeper.getServerTickTime()) {
			if (channelBar!=null) {
				channelBar.removeAll();
			} else {
				LivingEntityStructure.setChannelingBar(l, "");
				LivingEntityStructure les = LivingEntityStructure.GetLivingEntityStructure(l);
				if (les.suffix_bar.length()==0 && les.prefix.length()>0) {
					l.setCustomNameVisible(false);
				}
			}
			EntityChannelCastEvent ev = new EntityChannelCastEvent(l,channelName);
			Bukkit.getPluginManager().callEvent(ev);
			return false;
		} else {
			if (channelBar!=null) {
				UpdateBossBar();
			} else {
				UpdateMobChannelingBar(l);
				LivingEntityStructure.UpdateMobName(l);
			}
			return true;
		}
	}
}
