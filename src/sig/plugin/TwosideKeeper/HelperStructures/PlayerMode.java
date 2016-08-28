package sig.plugin.TwosideKeeper.HelperStructures;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import sig.plugin.TwosideKeeper.PlayerStructure;
import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;

public enum PlayerMode {
	STRIKER(ChatColor.RED,"S","Striker",
			ChatColor.RED+""+ChatColor.BOLD+"Striker mode Perks: "+ChatColor.RESET+"\n"
					+ ChatColor.WHITE+"->Players are identified as 'Strikers' when they only carry a sword in their main hand. No off-hand items.\n"
					+ ChatColor.GRAY+"->10% passive damage increase.\n"
					+ ChatColor.WHITE+"->20% chance to critically strike.\n"
					+ ChatColor.WHITE+"->Getting hit increases Speed by 1 Level. Stacks up to Speed V (Lasts five seconds.)\n"
					+ ChatColor.GRAY+"->Swinging your weapon stops nearby flying arrows. Each arrow deflected will give you a Strength buff. Stacks up to Strength V (Lasts five seconds.)\n"
					+ ChatColor.WHITE+"->Dropping your weapon will perform a line drive. Enemies you charge through take x7 your base damage. This costs 5% of your durability (Unbreaking decreases this amount.)\n"
					+ ChatColor.GRAY+"->Strikers have a 20% chance to dodge incoming attacks from any damage source while moving.\n"
					+ ChatColor.WHITE+"->Hitting a target when they have not noticed you yet does x3 normal damage.\n"),
	RANGER(ChatColor.DARK_GREEN,"R","Ranger",
			ChatColor.DARK_GREEN+""+ChatColor.BOLD+"Ranger mode Perks: "+ChatColor.RESET+"\n"
					+ ChatColor.WHITE+"->Players are identified as 'Rangers' when they carry a bow in their main hand. Off-hand items are permitted, except for a shield. Can only be wearing leather armor, or no armor.\n"
					+ ChatColor.GRAY+"->Left-clicking mobs will cause them to be knocked back extremely far, basically in headshot range, when walls permit.\n"
					+ ChatColor.WHITE+"->Base Arrow Damage increases from x2->x4.\n"
					+ ChatColor.GRAY+"->You can dodge 50% of all incoming attacks from any damage sources.\n"
					+ ChatColor.WHITE+"You have immunity to all Thorns damage.\n"
					+ ChatColor.GRAY+"Shift-Right Click to change Bow Modes.\n"
					+ ChatColor.WHITE+"- "+ChatColor.BOLD+"Close Range Mode (Default):"+ChatColor.RESET+ChatColor.WHITE+" \n"
					+ ChatColor.GRAY+"  You gain the ability to deal headshots from any distance, even directly onto an enemy's face. Each kill made in this mode gives you 100% dodge chance for the next hit taken. You can tumble and gain invulnerability for 1 second by dropping your bow. Sneak while dropping it to tumble backwards.\n"
					+ ChatColor.WHITE+"- "+ChatColor.BOLD+"Sniping Mode:"+ChatColor.RESET+ChatColor.WHITE+" \n"
					+ ChatColor.GRAY+"  Headshot collision area increases by x3. Headshots will deal an extra x0.25 damage for each headshot landed, up to a cap of 8 stacks. Each stack also increases your Slowness level by 1. You lose 10% dodge chance per Slowness stack, but gain one Resistance level and 10% critical chance per Slowness stack.\n"
					+ ChatColor.WHITE+"  Arrows are lightning-fast in Sniping Mode.\n"
					+ ChatColor.GRAY+"- "+ChatColor.BOLD+"Debilitation Mode:"+ChatColor.RESET+ChatColor.WHITE+" \n"
					+ ChatColor.WHITE+"  Adds a stack of Poison when hitting non-poisoned targets (20 second duration). Hitting mobs in this mode refreshes the duration of the poison stacks. Headshots made in this mode will increase the level of Poison on the mob, making the mob more and more vulnerable.\n"
					+ ChatColor.GRAY+"  Headshots also remove one level of a buff (does not affect debuffs) applied to the mob at random.\n"),
	DEFENDER(ChatColor.GRAY,"D","Defender",
			ChatColor.GRAY+""+ChatColor.BOLD+"Defender mode Perks: "+ChatColor.RESET+"\n"
					+ ChatColor.WHITE+"->Players are identified as 'Defenders' when they use a shield in their main hand.\n"
					+ ChatColor.GRAY+"->Base Damage reduction from shields increases from 5%->10%\n"
					+ ChatColor.WHITE+"->Blocking damage reduction increases from 50->70%\n"
					+ ChatColor.GRAY+"->When not blocking, you have Regeneration I. Blocking applies Regeneration II.\n"
					+ ChatColor.WHITE+"->Blocking gives 8 health (4 hearts) of Absorption damage.\n"
					+ ChatColor.GRAY+"->When hit while blocking, you build up Resistance, one level per hit, up to Resistance V (lasts 2 seconds)\n"
					+ ChatColor.WHITE+"->While blocking, you absorb 50% of all damage taken by party members.\n"
					+ ChatColor.GRAY+"->Blocking will aggro all nearby mobs to the blocking defender. They will glow indicate the aggro shift.\n"
					+ ChatColor.WHITE+"->Base Health increased by 10 (5 hearts)\n"
					+ ChatColor.GRAY+"->Getting hit as a defender increases saturation.\n"
					+ ChatColor.WHITE+"->Hitting mobs as a Defender aggros them to you.\n"
					+ ChatColor.GRAY+"->Knockback from attacks reduced by 75% while blocking.\n"
					+ ChatColor.WHITE+"- "+ChatColor.BOLD+"Rejuvenation"+ChatColor.RESET+ChatColor.WHITE+"\n"
					+ ChatColor.GRAY+"->Dropping your shield will give you Regeneration X for 10 seconds and 2 seconds of invulnerability. It also costs 400 shield durability!\n"),
	BARBARIAN(ChatColor.GOLD,"B","Barbarian",
			ChatColor.GOLD+""+ChatColor.BOLD+"Barbarian mode Perks: "+ChatColor.RESET+"\n"),
	SLAYER(ChatColor.DARK_BLUE,"SL","Slayer",
			ChatColor.DARK_BLUE+""+ChatColor.BOLD+"Slayer mode Perks: "+ChatColor.RESET+"\n"
					+ ChatColor.WHITE+"->Players are identified as 'Slayers' by wearing no armor, and wearing a Bauble in your hotbar.\n"
					+ ChatColor.GRAY+"->Slayers can make use of up to 9 Baubles by placing them on their hotbar (Ideally you would want to use one slot for a weapon). Each Bauble adds a certain amount of stats to the Slayer, making them more efficient.\n"
					+ ChatColor.WHITE+"->Slayers lose 2 HP from every hit regardless of damage taken, making this mode essentially have 5 lives.\n"
					+ ChatColor.GRAY+"->Slayers are not affected by any Health Recovery and Health Regeneration effects. This mode only heals from kills or by using the Amulet's set effect. However, Absorption will still work for a Slayer. Absorption hearts just get removed with normal damage calculation rules.\n"
					+ ChatColor.WHITE+"->Slayers can enter Stealth mode by pressing Sneak. Once in Stealth mode, Slayers will not leave stealth until they hit a monster or Sneak again. Stealth mode drains either 1% Durability or 1 Durability, whichever is larger, from a tool on your hotbar.\n"
					+ ChatColor.GRAY+"->Slayers can Backstab targets by getting behind them and hitting them. A backstab does triple the normal damage of an attack.\n"
					+ ChatColor.WHITE+"->Whenever a Slayer critically strikes, it suppresses a target for 0.25 seconds. Suppression prevents movement, attacking, teleporting, and exploding. Suppressed targets glow Black.\n"
					+ ChatColor.GRAY+"->Slayers thrive in 1vs1 situations. If a target is completely alone, they will glow white to the Slayer. Isolated targets take 50% more damage from the Slayer. Slayer's Dodge Chance increases by 40% against isolated targets.\n"
					+ ChatColor.WHITE+"->Slayers can use the Assassination ability. Press the Drop key while looking at an enemy to perform an assassination: You jump directly behind the enemy, gaining 0.5 seconds of invulnerability. If the next hit after Assassination is performed kills the target, you gain 1 Heart (2 Health) back along with a speed and strength buff. These buffs cap at Speed V and Strength X respectively. Assassination cooldown is reset whenever a target is instantly killed in this manner, and you get immediately put back into stealth, preventing further detection from other monsters.\n"),
	SUMMONER(ChatColor.DARK_PURPLE,"SM","Summoner",
			ChatColor.DARK_PURPLE+""+ChatColor.BOLD+"Summoner mode Perks: "+ChatColor.RESET+"\n"),
	NORMAL(ChatColor.WHITE,"","",
			"This mode has no perks!");
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
			if (isSlayer(p)) {
				pd.lastmode=PlayerMode.SLAYER;
			} else 
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
	
	public static boolean isSlayer(Player p) {
		if (p!=null && !p.isDead()) {
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			if (needsUpdating(pd)) {
				if (p.getEquipment().getItemInMainHand()!=null && GenericFunctions.hasSlayerSetItemOnHotbar(p) &&
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

	String name="";
	String desription="";
	
	public String getDesription() {
		return desription;
	}

	public void setDesription(String desription) {
		this.desription = desription;
	}

	PlayerMode(ChatColor col, String abbreviation, String fullname, String desc) {
		this.col=col;
		this.symbol=abbreviation;
		this.name=fullname;
		this.desription=desc;
	}
	
	
}
