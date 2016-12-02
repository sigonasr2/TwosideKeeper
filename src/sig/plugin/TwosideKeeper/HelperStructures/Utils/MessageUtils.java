package sig.plugin.TwosideKeeper.HelperStructures.Utils;

import org.bukkit.Bukkit;

import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.HelperStructures.ServerType;

public class MessageUtils {
	public static void announceMessage(String msg) {
		if (TwosideKeeper.SERVER_TYPE!=ServerType.QUIET) {
			aPlugin.API.discordSendRaw(msg);
		}
		Bukkit.broadcastMessage(msg);
	}
}
