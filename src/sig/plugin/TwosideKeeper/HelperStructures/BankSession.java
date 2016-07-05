package sig.plugin.TwosideKeeper.HelperStructures;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import sig.plugin.TwosideKeeper.TwosideKeeper;

public class BankSession {
	Player p;
	long time;
	SessionState state;
	public BankSession(Player p, SessionState type) {
		this.p=p;
		this.time=TwosideKeeper.getServerTickTime();
		this.state=type;
	}
	public Player GetPlayer() {
		return p;
	}
	public SessionState GetState() {
		return state;
	}
	public boolean isSessionExpired() {
		if (time+TwosideKeeper.TERMINALTIME<TwosideKeeper.getServerTickTime()) {
			return true;
		} else {
			return false;
		}
	}
	public void SetState(SessionState newstate) {
		this.state=newstate;
	}
	public void refreshSession() {
		this.time=TwosideKeeper.getServerTickTime();
	}
	public String GetExpirationMessage() {
		return ChatColor.RED+"Bank session closed. "+ChatColor.WHITE+"Ran out of time!";
	}
}
