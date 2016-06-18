package sig.plugin.TwosideKeeper.HelperStructures;

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import sig.plugin.TwosideKeeper.TwosideKeeper;

/**
 * Represents a world shop transaction.
 * Has a 'state' it is in. Such as
 * "CREATE" - Asks how many of the item to put in the shop?
 * "PRICE" - How much each of the item will cost?
 * "EDIT" - Modify the price.
 * "PURCHASE" - Type in how many to purchase.
 * 
 * Holds the player performing the transaction.
 * Time the player started the transaction. Cancels out after nothing is said after 15 seconds.
 * 
 * Has methods to handle session checking. Such as
 * GetSessionType()
 * GetPlayer()
 * GetTime()
 *
 */
public class WorldShopSession {
	Player p;
	long time;
	SessionState status;
	Sign s;
	int amt;
	ItemStack item;
	public WorldShopSession(Player p, long server_time, SessionState status, Sign s) {
		this.p=p;
		this.time=server_time;
		this.status=status;
		this.s=s;
		this.amt=0;
		this.item = null;
	}
	public SessionState GetSessionType() {
		return this.status;
	}
	public Player GetPlayer() {
		return this.p;
	}
	public long GetTime() {
		return time; 
	}
	public Sign GetSign() {
		return s;
	}
	public int getAmt() {
		return amt;
	}
	public ItemStack getItem() {
		return item;
	}
	public void SetItem(ItemStack item) {
		this.item=item;
	}
	public void SetAmt(int amt) {
		this.amt=amt;
	}
	public void SetSession(SessionState type) {
		status = type;
	}
	public void UpdateTime() {
		time = TwosideKeeper.getServerTickTime();
	}
	public boolean IsTimeExpired() {
		if (time+TwosideKeeper.TERMINALTIME<=TwosideKeeper.getServerTickTime()) {
			return true;
		} else {
			return false;
		}
	}

	public String toString() {
		return "WorldShop:{STATUS:"+status+",Item:"+item.toString()+",Time:"+time+",Sign:"+s.toString()+",Amount:"+amt+"}";
	}
}
