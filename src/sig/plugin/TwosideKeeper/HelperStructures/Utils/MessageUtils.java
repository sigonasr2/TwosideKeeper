package sig.plugin.TwosideKeeper.HelperStructures.Utils;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import sig.plugin.TwosideKeeper.PartyManager;
import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.HelperStructures.ServerType;

public class MessageUtils {
	public static void announceMessage(String msg) {
		if (TwosideKeeper.SERVER_TYPE!=ServerType.QUIET) {
			aPlugin.API.discordSendRaw(msg);
		}
		Bukkit.broadcastMessage(msg);
	}

	public static void announceMessageToParty(Player p, String msg) {
		List<Player> partymembers = PartyManager.getPartyMembers(p);
		for (Player pl : partymembers) {
			pl.sendMessage(msg);
		}
	}
}
