package sig.plugin.TwosideKeeper.HelperStructures;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import sig.plugin.TwosideKeeper.PartyManager;
import sig.plugin.TwosideKeeper.PlayerStructure;
import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.HelperStructures.Common.ArrowQuiver;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;

public enum PlayerMode {
	STRIKER(ChatColor.RED,"S","Striker",Book.STRIKERGUIDE),
	RANGER(ChatColor.DARK_GREEN,"R","Ranger",Book.RANGERGUIDE),
	DEFENDER(ChatColor.GRAY,"D","Defender",Book.DEFENDERGUIDE),
	BARBARIAN(ChatColor.GOLD,"B","Barbarian",Book.BARBARIANGUIDE
			),
	SLAYER(ChatColor.DARK_BLUE,"SL","Slayer",Book.SLAYERGUIDE),
	/*SUMMONER(ChatColor.DARK_PURPLE,"SM","Summoner",
			ChatColor.DARK_PURPLE+""+ChatColor.BOLD+"Summoner mode Perks: "+ChatColor.RESET+"\n"),*/
	NORMAL(ChatColor.WHITE,"A","Adventurer",Book.ADVENTURERGUIDE);
	;
	
	final public static int UPDATE_GRACE_PERIOD=9; //How often to update the mode of the player.
	
	ChatColor col=ChatColor.WHITE;
	String symbol="";
	Book storedBook;
	
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
		if (p!=null && p.isValid() && p.isOnline() && !p.isDead()) {
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			if (needsUpdating(pd)) {
				if (Check_isSlayer(p)) {
					if (pd.lastmode!=PlayerMode.SLAYER) {pd.slayermodehp=p.getHealth();}
					pd.lastmode=PlayerMode.SLAYER;
				} else {
					if (pd.lastmode==PlayerMode.SLAYER) {
						GenericFunctions.removeStealth(p);
					}
					if (Check_isStriker(p)) {
						pd.lastmode=PlayerMode.STRIKER;
					} else
					if (Check_isBarbarian(p)) {
						pd.lastmode=PlayerMode.BARBARIAN;
					} else
					if (Check_isDefender(p)) {
						pd.lastmode=PlayerMode.DEFENDER;
					} else
					if (Check_isRanger(p)) {
						pd.lastmode=PlayerMode.RANGER;
					} else {
						pd.lastmode=PlayerMode.NORMAL;
					}
				}
			}
			return pd.lastmode;
		} else {
			return PlayerMode.NORMAL;
		}
	}

	public static boolean needsUpdating(PlayerStructure pd) {
		if (pd!=null) { 
			return pd.lastmodeupdate+UPDATE_GRACE_PERIOD<=TwosideKeeper.getServerTickTime();
		} else {
			return false;
		}
	}

	public static boolean isRanger(Player p) {
		if (p!=null && !p.isDead()) {
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			if (needsUpdating(pd)) {
				return getPlayerMode(p)==PlayerMode.RANGER;
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
				return getPlayerMode(p)==PlayerMode.DEFENDER;
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
				return getPlayerMode(p)==PlayerMode.STRIKER;
			} else {
				return pd.lastmode==PlayerMode.STRIKER;
			}
		} else {
			return false;
		}
	}
	
	public static boolean isSlayer(Player p) {
		if (p!=null && !p.isDead()) {
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			if (needsUpdating(pd)) {
				return getPlayerMode(p)==PlayerMode.SLAYER;
			} else {
				return pd.lastmode==PlayerMode.SLAYER;
			}
		} else {
			return false;
		}
	}	
	
	public static boolean isBarbarian(Player p) {
		if (p!=null && !p.isDead()) {
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			if (needsUpdating(pd)) {
				return getPlayerMode(p)==PlayerMode.BARBARIAN;
			} else {
				return pd.lastmode==PlayerMode.BARBARIAN;
			}
		} else {
			return false;
		}
	}
	
	public static boolean isNormal(Player p) {
		if (p!=null && !p.isDead()) {
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			if (needsUpdating(pd)) {
				return getPlayerMode(p)==PlayerMode.NORMAL;
			} else {
				return pd.lastmode==PlayerMode.NORMAL;
			}
		} else {
			return false;
		}
	}
	

	public static boolean Check_isRanger(Player p) {
		if (p!=null && !p.isDead()) {
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			if (needsUpdating(pd)) {
				if ((((p.getEquipment().getItemInMainHand()!=null && (p.getEquipment().getItemInMainHand().getType()==Material.BOW || ArrowQuiver.isValidQuiver(p.getEquipment().getItemInMainHand())) && (p.getInventory().getExtraContents()[0]==null || p.getInventory().getExtraContents()[0].getType()==Material.AIR)) || //Satisfy just a bow/quiver in main hand.
						(p.getEquipment().getItemInMainHand()!=null && (p.getEquipment().getItemInMainHand().getType()==Material.BOW || ArrowQuiver.isValidQuiver(p.getEquipment().getItemInMainHand())) && p.getInventory().getExtraContents()[0]!=null && !(GenericFunctions.isWeapon(p.getInventory().getExtraContents()[0])) && !(GenericFunctions.isArmor(p.getInventory().getExtraContents()[0]))) ||  /*Satisfy a bow/quiver in main hand and no shield in off-hand.*/
						(p.getEquipment().getItemInMainHand()!=null && !GenericFunctions.isWeapon(p.getEquipment().getItemInMainHand()) && !GenericFunctions.isArmor(p.getEquipment().getItemInMainHand()) && p.getInventory().getExtraContents()[0]!=null && (p.getInventory().getExtraContents()[0].getType()==Material.BOW || ArrowQuiver.isValidQuiver(p.getInventory().getExtraContents()[0]))) ||  /*Satisfy a bow/quiver in off-hand and no shield in main hand.*/
						((p.getEquipment().getItemInMainHand()==null || p.getEquipment().getItemInMainHand().getType()==Material.AIR) && p.getInventory().getExtraContents()[0]!=null && (p.getInventory().getExtraContents()[0].getType()==Material.BOW || ArrowQuiver.isValidQuiver(p.getInventory().getExtraContents()[0])))) /*Satisfy just a bow/quiver in off-hand.*/ &&
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

	public static boolean Check_isDefender(Player p) {
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

	public static boolean Check_isStriker(Player p) {
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
	
	public static boolean Check_isSlayer(Player p) {
		if (p!=null && !p.isDead()) {
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			if (needsUpdating(pd)) {
				if (p.getEquipment().getItemInMainHand()!=null && GenericFunctions.hasBaublePouchInOffHand(p) &&
						GenericFunctions.WearingNoArmor(p)) {
					return true;
				} else {
					return false;
				}
			} else {
				return pd.lastmode==PlayerMode.SLAYER;
			}
		} else {
			return false;
		}
	}

	public static boolean Check_isBarbarian(Player p) {
		if (p!=null && !p.isDead()) {
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			if (needsUpdating(pd)) {
				if (p.getEquipment().getItemInMainHand()!=null && p.getEquipment().getItemInMainHand().getType().toString().contains("_AXE") &&
						p.getInventory().getExtraContents()[0]!=null && p.getInventory().getExtraContents()[0].getType().toString().contains("_AXE")) {
					return true;
				} else {
					return false;
				}
			} else {
				return pd.lastmode==PlayerMode.BARBARIAN;
			}
		} else {
			return false;
		}
	}

	String name="";
	Book helperBook;
	
	public Book getBook() {
		return helperBook;
	}

	public void setBook(Book book) {
		this.helperBook = book;
	}

	PlayerMode(ChatColor col, String abbreviation, String fullname, Book descBook) {
		this.col=col;
		this.symbol=abbreviation;
		this.name=fullname;
		this.helperBook=descBook;
	}
	
	
}
