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
	STRIKER(ChatColor.RED,"S","Striker",
			ChatColor.RED+""+ChatColor.BOLD+"Striker mode Perks: "+ChatColor.RESET+"\n"
					+ ChatColor.WHITE+"->Players are identified as 'Strikers' when they only carry a sword in their main hand. No off-hand items.\n"
					+ ChatColor.GRAY+"->10% passive damage increase.\n"
					+ ChatColor.WHITE+"->20% chance to critically strike.\n"
					+ ChatColor.WHITE+"->Getting hit increases Speed by 1 Level. Stacks up to Speed V (Lasts five seconds.)\n"
					+ ChatColor.GRAY+"->Swinging your weapon stops nearby flying arrows. Each arrow deflected will give you a Strength buff. Stacks up to Strength V (Lasts five seconds.)\n"
					+ ChatColor.WHITE+"->Press the drop key to perform a line drive. Enemies you charge through take x1-x5 damage, based on target's missing health. This costs 5% of your durability (Unbreaking decreases this amount.)\n"
					+ ChatColor.GRAY+"->Strikers have a 20% chance to dodge incoming attacks from any damage source while moving.\n"
					+ ChatColor.WHITE+"->Hitting a target when they have not noticed you yet does x3 normal damage.\n"),
	RANGER(ChatColor.DARK_GREEN,"R","Ranger",
			ChatColor.DARK_GREEN+""+ChatColor.BOLD+"Ranger mode Perks: "+ChatColor.RESET+"\n"
					+ ChatColor.WHITE+"->Players are identified as 'Rangers' when they carry a bow or a quiver in one of their hands. Off-hand items are permitted, except for a shield. Can only be wearing leather armor, or no armor.\n"
					+ ChatColor.GRAY+"->Left-clicking mobs will cause them to be knocked back extremely far, basically in headshot range, when walls permit.\n"
					+ ChatColor.WHITE+"->Base Arrow Damage increases from x2->x4.\n"
					+ ChatColor.GRAY+"->You can dodge 40% of all incoming attacks from any damage sources.\n"
					+ ChatColor.WHITE+"You have immunity to all Thorns damage.\n"
					+ ChatColor.GRAY+"Shift-Right Click to change Bow Modes.\n"
					+ ChatColor.WHITE+"- "+ChatColor.BOLD+"Close Range Mode (Default):"+ChatColor.RESET+ChatColor.WHITE+" \n"
					+ ChatColor.GRAY+"  You gain the ability to deal headshots from any distance, even directly onto an enemy's face. Each kill made in this mode gives you 100% dodge chance for the next hit taken. You can tumble and gain invulnerability for 1 second by dropping your bow. Sneak while dropping it to tumble backwards.\n"
					+ ChatColor.WHITE+"- "+ChatColor.BOLD+"Sniping Mode:"+ChatColor.RESET+ChatColor.WHITE+" \n"
					+ ChatColor.GRAY+"  Headshot collision area increases by x3. Headshots will deal an extra x0.125 damage for each headshot landed, up to a cap of 8 stacks. Each stack also increases your Slowness level by 1. You lose 10% dodge chance per Slowness stack, but gain one Resistance level and 10% critical chance per Slowness stack.\n"
					+ ChatColor.WHITE+"  Arrows are lightning-fast in Sniping Mode.\n"
					+ ChatColor.GRAY+"  Press the drop key in Sniping Mode to unleash 26 piercing arrows rapidly against your enemies.\n\n"
					+ ChatColor.WHITE+"- "+ChatColor.BOLD+"Debilitation Mode:"+ChatColor.RESET+ChatColor.WHITE+" \n"
					+ ChatColor.GRAY+"  Adds a stack of Poison when hitting non-poisoned targets (15 second duration). Headshots made in this mode will increase the level of Poison on the mob, making the mob more and more vulnerable.\n"
					+ ChatColor.WHITE+"  Press the drop key in Debilitation Mode when at least 1 poisoned target is nearby. Deals (Poison Level x 10) True Damage and Slows all targets the same level as the number of poison stacks applied to nearby targets for 15 seconds, and grants 4 Absorption health (2 hearts) to the Ranger per poison stack. Refreshes Poison duration on all nearby poisoned targets.\n"),
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
					+ ChatColor.GRAY+"->Dropping your shield will give you 10 seconds of massive health regeneration and 2 seconds of invulnerability. It also costs 400 shield durability!\n"),
	BARBARIAN(ChatColor.GOLD,"B","Barbarian",
			ChatColor.GOLD+""+ChatColor.BOLD+"Barbarian mode Perks: "+ChatColor.RESET+"\n"
			+ ChatColor.WHITE+"->Players are identified as 'Barbarians' by wielding an axe in both the main hand and the offhand.\n"
			+ ChatColor.GRAY+"->Barbarians swing their off-hand by right-clicking.\n"
			+ ChatColor.WHITE+"->Barbarians gain 2 HP (1 Heart) per 1% of Damage reduction.\n"
			+ ChatColor.GRAY+"->When Barbarians are hit, they take damage as if they had 0% Damage reduction.\n"
			+ ChatColor.WHITE+"->Barbarians deal 20% more damage for every 20% of an enemy's missing health.\n"
			+ ChatColor.GRAY+"->Barbarians gain Bonus Lifesteal stacks as they hit enemies. Each stack increases Lifesteal by 1%, up to a cap of 100% extra Lifesteal. The stacks refresh every hit, but wear off after 5 seconds.\n"
			+ ChatColor.WHITE+"->Barbarians do not instantly take full damage when hit. Instead, the HP is stored in a 'Damage Pool' and distributed every second.\n"
			+ ChatColor.GRAY+"->If Barbarians have points in their 'Damage Pool', they will take up to 15 damage (+3% of their damage pool) every second. The amount taken goes down by wearing Barbarian gear.\n"
			+ ChatColor.WHITE+"->When a monster is killed by a Barbarian, the amount of remaining damage in their Damage Pool is divided by 4.\n"
			+ ChatColor.GRAY+"->Extra health from Lifestealing that is not used for healing your health will heal up your Damage Pool instead."
			+ ChatColor.WHITE+"->Barbarians automatically consume Rotten Flesh and Spider Eyes that are picked up. Each one heals for 1% of their health. Rotten Flesh and Spider Eyes in a Barbarian's inventory will automatically be consumed as the Barbarian gets hungry.\n"
			+ ChatColor.GRAY+"->Barbarians build up Weapon Charges in two ways: +1 Charge for attacking an enemy with the main hand weapon and +2 Charges for taking damage.\n"
			+ ChatColor.WHITE+"->Barbarians have 70% knockback resistance.\n"
			+ ChatColor.GRAY+"->Barbarians can release their Weapon Charges by using a variety of commands:\n"
			+ ChatColor.WHITE+"->Right-Click (Costs 10 Charges): Power Swing - Swing your off-hand weapon to deal an attack with +100% Lifesteal and +100% Crit Chance bonus. Gives 10 Bonus Lifesteal stacks.\n"
			+ ChatColor.GRAY+"->Shift Left-Click (Costs 30 Charges): Forceful Strike - Hit all enemies in a line in front of you, dealing double damage and suppressing them for 3 seconds.\n"
			+ ChatColor.WHITE+"->Shift Right-Click (Costs 30 Charges): Sweep Up - Performs a sweeping attack which knocks up and damages all enemies within a 4m radius of you. Doubles your Bonus Lifesteal stacks. Lifesteal effects are doubled during this attack.\n"
			+ ChatColor.GRAY+"->Swap Item Key (100 Charges Minimum, Costs ALL Charges): Barbarian's Rage - Converts your missing health into Absorption Hearts and applies powerful buffs. This ability is stronger the more stacks consumed.\n"
			+ ChatColor.WHITE+"	Barbarian's Rage: \n"
			+ ChatColor.GRAY+"  -- Strength Level: +1 per 10 charges\n"
			+ ChatColor.WHITE+"  -- LifeSteal: +1% per 2 charges\n"
			+ ChatColor.GRAY+"  -- Speed V\n"
			+ ChatColor.WHITE+"  -- Duration of Rage: +1 second per 10 charges\n"
			+ ChatColor.GRAY+"  -- +2 seconds of invulnerability per 100 charges\n"
			+ ChatColor.WHITE+"During Rage you gain double the number of Bonus Lifesteal stacks. You do not gain Weapon Charges during Barbarian's Rage.\n"
			+ ChatColor.GRAY+"->Leaping Strike: Barbarians that take fall damage deal triple the damage taken from the fall as damage to all enemies nearby. The range of this attack increases based on how fast the Barbarian falls.\n"
			+ ChatColor.WHITE+"->Mock: Press the drop key to perform a Mock attack to all enemies near you. Affected enemies become aggro'd to the Barbarian for 15 seconds and receive 2 stacks of Weakness that lasts 15 seconds. This can stack up to Weakness VI. 20 second cooldown.\n"
			),
	SLAYER(ChatColor.DARK_BLUE,"SL","Slayer",
			ChatColor.DARK_BLUE+""+ChatColor.BOLD+"Slayer mode Perks: "+ChatColor.RESET+"\n"
					+ ChatColor.WHITE+"->Players are identified as 'Slayers' by wearing no armor, and wearing a Bauble Pouch in their off hand.\n"
					+ ChatColor.GRAY+"->Slayers can make use of up to 9 Baubles by placing them in their Bauble Pouch. Each Bauble adds a certain amount of stats to the Slayer, making them more efficient.\n"
					+ ChatColor.WHITE+"->Slayers take a maximum of 1 Heart (2 HP) in damage from all attacks, making this mode essentially 5 lives.\n"
					+ ChatColor.GRAY+"->Slayers are not affected by any Health Recovery and Health Regeneration effects. This mode only heals from kills, being out of combat for 1 minute, using the Amulet's set effect, or sleeping. However, Absorption will still work for a Slayer. Absorption hearts just get removed with normal damage calculation rules.\n"
					+ ChatColor.WHITE+"->Whenever a Slayer kills a target, they recover 1 Heart (2 HP). This can be modified by a special weapon.\n"
					+ ChatColor.GRAY+"->Slayers can enter Stealth mode by pressing Sneak. Once in Stealth mode, Slayers will not leave stealth until they take damage or Sneak again. Stealth mode drains 1 Durability every second from tools on your hotbar.\n"
					+ ChatColor.WHITE+"->While in Stealth mode, nothing will be able to detect you. Note this does not get rid of aggression from targets that have already aggro'd you.\n"
					+ ChatColor.GRAY+"->Slayers can Backstab targets by getting behind them and hitting them. A backstab does triple the normal damage of an attack.\n"
					+ ChatColor.WHITE+"->Whenever a Slayer critically strikes, it suppresses a target for 0.75 seconds. Suppression prevents movement, attacking, teleporting, and exploding. Suppressed targets glow Black.\n"
					+ ChatColor.GRAY+"->Slayers thrive in 1vs1 situations. If a target is completely alone, they will glow white to the Slayer. Isolated targets take 50% more damage from the Slayer. Slayer's Dodge Chance increases by 40% against isolated targets.\n"
					+ ChatColor.WHITE+"->Slayers can use the Assassination ability. Press the Drop key while looking at an enemy to perform an assassination: You jump directly behind the enemy, gaining 0.5 seconds of invulnerability. If the next hit after Assassination is performed kills the target, you gain a speed and strength buff. These buffs cap at Speed V and Strength X respectively and last 10 seconds. Assassination cooldown is reset whenever a target is instantly killed in this manner, and you get immediately put back into stealth, preventing further detection from other monsters.\n"),
	SUMMONER(ChatColor.DARK_PURPLE,"SM","Summoner",
			ChatColor.DARK_PURPLE+""+ChatColor.BOLD+"Summoner mode Perks: "+ChatColor.RESET+"\n"),
	NORMAL(ChatColor.WHITE,"A","Adventurer",
			ChatColor.WHITE+""+ChatColor.BOLD+"Adventurer mode Perks: "+ChatColor.RESET+"\n"
					+ ChatColor.WHITE+"->Players are identified as 'Adventurers' by default.\n"
					+ ChatColor.GRAY+"->Adventurers gain +10 Health.\n"
					+ ChatColor.WHITE+"->Adventurers gain +20% Damage Reduction.\n"
					+ ChatColor.GRAY+"->Adventurers gain +50% Health Regeneration.\n"
					+ ChatColor.WHITE+"->If Adventurers are killed, their Buy-Backs are 50% cheaper.\n"
					+ ChatColor.GRAY+"->Adventurers do not get exhausted when performing light activities.\n");
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
