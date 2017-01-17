package sig.plugin.TwosideKeeper;

import java.text.DecimalFormat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import aPlugin.DiscordMessageSender;
import net.minecraft.server.v1_9_R1.MinecraftServer;

public class DiscordStatusUpdater implements Runnable{

	@Override
	public void run() {
		//DiscordMessageSender.setPlaying(ProduceMessage());
		aPlugin.API.discordSetPlaying(ProduceMessage());
	    Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(Bukkit.getPluginManager().getPlugin("TwosideKeeper"), this, 300l);
	}
	
	@SuppressWarnings("deprecation")
	String ProduceMessage() {
		DecimalFormat df = new DecimalFormat("0.00");
		return ChatColor.stripColor("TPS: "+df.format(MinecraftServer.getServer().recentTps[0])+" "+TwosideKeeper.getWeatherIcon()+" "+TwosideKeeper.getTimeOfDay()+" ("+Bukkit.getOnlinePlayers().size()+")");
	}

}
