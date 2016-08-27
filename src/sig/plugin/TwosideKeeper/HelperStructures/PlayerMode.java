package sig.plugin.TwosideKeeper.HelperStructures;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import sig.plugin.TwosideKeeper.PlayerStructure;
import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;

public enum PlayerMode {
	STRIKER(ChatColor.RED,"S","Striker"),
	RANGER(ChatColor.GREEN,"R","Ranger"),
	DEFENDER(ChatColor.GRAY,"D","Defender"),
	BARBARIAN(ChatColor.GOLD,"B","Barbarian"),
	SLAYER(ChatColor.DARK_BLUE,"SL","Slayer"),
	SUMMONER(ChatColor.DARK_PURPLE,"SM","Summoner"),
	NORMAL(ChatColor.WHITE,"","");
	;
	
	final public static int UPDATE_GRACE_PERIOD=9; //How often to update the mode of the player.
	
	ChatColor col=ChatColor.WHITE;
	String symbol="";
	
	public ChatColor getColor() {
		return col;
	}

	public String getAbbreviation() {
		return symbol;
	}

	public String getName() {
		return name;
	}
	
	public static PlayerMode getPlayerMode(Player p) {
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		if (needsUpdating(pd)) {
			if (isStriker(p)) {
				pd.lastmode=PlayerMode.STRIKER;
			} else
			if (isDefender(p)) {
				pd.lastmode=PlayerMode.DEFENDER;
			} else
			if (isRanger(p)) {
				pd.lastmode=PlayerMode.RANGER;
			} else {
				pd.lastmode=PlayerMode.NORMAL;
			}
			return pd.lastmode;
		} else {
			return pd.lastmode;
		}
	}

	public static boolean needsUpdating(PlayerStructure pd) {
		return pd.lastmodeupdate+UPDATE_GRACE_PERIOD<=TwosideKeeper.getServerTickTime();
	}

	public static boolean isRanger(Player p) {
		if (p!=null && !p.isDead()) {
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			if (needsUpdating(pd)) {
				if ((((p.getEquipment().getItemInMainHand()!=null && p.getEquipment().getItemInMainHand().getType()==Material.BOW && (p.getInventory().getExtraContents()[0]==null || p.getInventory().getExtraContents()[0].getType()==Material.AIR)) || //Satisfy just a bow in main hand.
						(p.getEquipment().getItemInMainHand()!=null && p.getEquipment().getItemInMainHand().getType()==Material.BOW && p.getInventory().getExtraContents()[0]!=null && !GenericFunctions.isEquip(p.getInventory().getExtraContents()[0])) ||  /*Satisfy a bow in main hand and no shield in off-hand.*/
						(p.getEquipment().getItemInMainHand()!=null && !GenericFunctions.isEquip(p.getEquipment().getItemInMainHand()) && p.getInventory().getExtraContents()[0]!=null && p.getInventory().getExtraContents()[0].getType()==Material.BOW) ||  /*Satisfy a bow in off-hand and no shield in main hand.*/
						((p.getEquipment().getItemInMainHand()==null || p.getEquipment().getItemInMainHand().getType()==Material.AIR) && p.getInventory().getExtraContents()[0]!=null && p.getInventory().getExtraContents()[0].getType()==Material.BOW)) /*Satisfy just a bow in off-hand.*/ &&
						GenericFunctions.AllLeatherArmor(p))) {
					return true;
				} else {
					return false;
				}
			} else {
				return pd.lastmode==PlayerMode.RANGER;
			}
		} else {
			return false;
		}
	}

	public static boolean isDefender(Player p) {
		if (p!=null && !p.isDead()) {
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			if (needsUpdating(pd)) {
				if (p.getEquipment().getItemInMainHand()!=null && p.getEquipment().getItemInMainHand().getType()==Material.SHIELD) {
					return true;
				} else {
					return false;
				}
			} else {
				return pd.lastmode==PlayerMode.DEFENDER;
			}
		} else {
			return false;
		}
	}

	public static boolean isStriker(Player p) {
		if (p!=null && !p.isDead()) {
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			if (needsUpdating(pd)) {
				if (p.getEquipment().getItemInMainHand()!=null && p.getEquipment().getItemInMainHand().getType().toString().contains("SWORD") &&
						p.getInventory().getExtraContents()[0]==null) {
					return true;
				} else {
					return false;
				}
			} else {
				return pd.lastmode==PlayerMode.STRIKER;
			}
		} else {
			return false;
		}
	}

	String name="";
	
	PlayerMode(ChatColor col, String abbreviation, String fullname) {
		this.col=col;
		this.symbol=abbreviation;
		this.name=fullname;
	}
	
	
}
