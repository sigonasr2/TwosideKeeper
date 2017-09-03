package sig.plugin.TwosideKeeper.HelperStructures;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import sig.plugin.TwosideKeeper.AwakenedArtifact;
import sig.plugin.TwosideKeeper.CustomDamage;
import sig.plugin.TwosideKeeper.PVP;
import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;
import sig.plugin.TwosideKeeper.HelperStructures.Common.PVPValue;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.ArtifactUtils;

public enum ArtifactAbility {
	//Enum Structure:
	// "Friendly Name", "Description", base value (per point) (-1 means it's a TEMPORARY ability.), decay value (per point), max level, level requirement (The min level required to get this perk), item type
	//Temporary abilities: Work for 1 level and wear off afterward.
	
	//Weapon Abilities
	DAMAGE("Strike","Improves Base Damage by [VAL]",new double[]{1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0},
			new PVPValue(15,1.0),10000,1,UpgradePath.BASIC,1),
	ARMOR_PEN("Piercing","[VAL]% of your damage is ignored by resistances. ([PENDMG] damage)",new double[]{1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0}, 
			new PVPValue(30,1.0),100,1,UpgradePath.BASIC,1),
	EXECUTION("Execute","Deals [VAL] extra damage for every 20% of target's missing health.",new double[]{0.3,0.3,0.3,0.3,0.3,0.3,0.3,0.3,0.3,0.3,0.3,0.3,0.3,0.3,0.3,0.3,0.3},
			new PVPValue(30,0.3),10000,1,UpgradePath.BASIC,1),
	LIFESTEAL("Lifesteal","Heals [VAL]% of the damage dealt to targets back to your health pool.",new double[]{0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1},
			new PVPValue(15,0.1),1000,1,UpgradePath.WEAPON,1),
	CRITICAL("Critical","[VAL]% chance to deal critical strikes.",new double[]{0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5},
			new PVPValue(30,0.5),100,1,UpgradePath.WEAPON,1),
	CRIT_DMG("Crit Damage","Critical Strikes deal [200VAL]% damage.",new double[]{1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0},
			new PVPValue(30,1.0),10000,1,UpgradePath.WEAPON,1),
	HIGHWINDER("Highwinder","While moving fast or sprinting, you deal [VAL] extra damage for every 1m of speed.",new double[]{0.05,0.05,0.05,0.05,0.05,0.05,0.05,0.05,0.05,0.05,0.05,0.05,0.05,0.05,0.05,0.05,0.05},
			new PVPValue(1,0.5),10000,15,UpgradePath.WEAPON,1),
	
	//Bow Abilities
	MARKSMAN("Marksman","Increases headshot hitbox size by [VAL]% .",new double[]{1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0},
			new PVPValue(30,1.0),10000,15,UpgradePath.BOW,1),
	/*SIEGESTANCE("Siege Stance",ChatColor.GRAY+"[Unimplemented] Activate by Sneaking for three seconds. Sneak again to de-activate.\n\n"
			+ "Applies Slowness V and Resistance VI. While in Siege Stance you fire clusters of 7 arrows per shot. Each arrow deals [VAL] damage.",new double[]{3.0,3.0,3.0,3.0,3.0,3.0,3.0,3.0,3.0,3.0},
			new double[]{1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0},100,1000,UpgradePath.BOW,1),
	ARROWSHOWER("Arrow Shower",ChatColor.GRAY+"[Unimplemented] Shift-Left Click to activate. Applies Slowness X for three seconds while firing arrows into the sky and onto enemies in a large area in front of you. Each arrow deals [VAL] damage.",new double[]{0.7,0.7,0.7,0.7,0.7,0.7,0.7,0.7,0.7,0.7},
			new double[]{0.4,0.4,0.4,0.4,0.4,0.4,0.4,0.4,0.4,0.4},100,1000,UpgradePath.BOW,1),
	TARGETING("Targeting",ChatColor.GRAY+"[Unimplemented] Left-click a mob to target them. Fire arrows to release homing missiles at your target. Each missile explodes and deals [VAL] damage.",new double[]{10,10,10,10,10,10,10,10,10,10},
			new double[]{0.3,0.3,0.3,0.3,0.3,0.3,0.3,0.3,0.3,0.3},100,1000,UpgradePath.BOW,1),
	ENDERTURRET("Ender Turret",ChatColor.GRAY+"[Unimplemented] Place Eyes of Ender in your hotbar to use as ammo. Each eye fired launches forward and upward before releasing a barrage of homing missiles that lock onto enemy targets. Each missile explodes and deals [VAL] damage.",new double[]{25,25,25,25,25,25,25,25,25,25},
			new double[]{0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5},100,1000,UpgradePath.BOW,1),*/
	
	//Armor abilities
	DAMAGE_REDUCTION("Defense","Increases Base Damage reduction by [VAL]%\n\n"+PlayerMode.RANGER.getColor()+PlayerMode.RANGER.getName()+" Mode "+ChatColor.WHITE+" only receives half the effect.",new double[]{0.245,0.245,0.245,0.245,0.245,0.245,0.245,0.245,0.245,0.245,0.245,0.245,0.245,0.245,0.245,0.245,0.245},
			new PVPValue(50,0.245),100,1,UpgradePath.ARMOR,1),
	HEALTH("Health","Increases Maximum Health by [VAL].\n\n"+PlayerMode.RANGER.getColor()+PlayerMode.RANGER.getName()+" Mode "+ChatColor.WHITE+" only receives half the effect.",new double[]{0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25},
			new PVPValue(30,0.25),10000,1,UpgradePath.ARMOR,1),
	HEALTH_REGEN("Regeneration","Regenerates an extra [VAL] health every 5 seconds.",new double[]{0.125,0.125,0.125,0.125,0.125,0.125,0.125,0.125,0.125,0.125,0.125,0.125,0.125,0.125,0.125,0.125},
			new PVPValue(1,0.125),10000,1,UpgradePath.ARMOR,1),
	STATUS_EFFECT_RESISTANCE("Resistance","When a debuff is applied, there is a [VAL]% chance to remove it.",new double[]{0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25,0.25},
			new PVPValue(40,0.25),100,1,UpgradePath.ARMOR,1),
	SHADOWWALKER("Shadow Walker","Increases your speed in dark areas. Damage Reduction increases by [VAL]% in dark areas. Dodge chance increases by [DODGEVAL]% in dark areas.",new double[]{0.2,0.2,0.2,0.2,0.2,0.2,0.2,0.2,0.2,0.2,0.2,0.2,0.2,0.2,0.2,0.2},
			new PVPValue(0,0.2),100,100,UpgradePath.ARMOR,1),
	SURVIVOR("Survivor","Taking fatal damage will not kill you and instead consumes this ability, removes all debuffs, and restores your health by [VAL]%"+TemporarySkill(true),new double[]{10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10},
			new PVPValue(0,10),10,25,UpgradePath.ARMOR,1),
	DODGE("Dodge","You have a [VAL]% chance to dodge incoming damage from any damage source."+LevelCost(2),new double[]{0.2,0.2,0.2,0.2,0.2,0.2,0.2,0.2,0.2,0.2,0.2,0.2,0.2,0.2,0.2,0.2},
			new PVPValue(0,0.2),100,40,UpgradePath.ARMOR,2),
	GRACEFULDODGE("Graceful Dodge","Whenever a dodge occurs, you will gain [GRACEFULVAL] seconds of invulnerability."+LevelCost(10),new double[]{0.05,0.05,0.05,0.05,0.05,0.05,0.05,0.05,0.05,0.05,0.05,0.05,0.05,0.05,0.05,0.05},
			new PVPValue(0,0.05),100,40,UpgradePath.ARMOR,10),
	
	//Sword abilities
	PROVOKE("Provoke","Your attacks increase Aggression by [VAL].",new double[]{5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5},
			new PVPValue(0,1.0),10000,10,UpgradePath.PROVOKE,1),
	COMBO("Belligerent","[VAL]% more damage for each successive strike on a mob. Resets after 2 seconds of no combat.",new double[]{0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1},
			new PVPValue(50,0.1),10000,40,UpgradePath.SWORD,1),
	
	//Pickaxe abilities
	/*SCAVENGE("Scavenge",ChatColor.GRAY+"[Unimplemented] Breaks off resources from armor. [VAL]% chance per hit.",new double[]{1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0},
			new double[]{1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0},100,1000,UpgradePath.PICKAXE,1),
	MINES("Land Mine",ChatColor.GRAY+"[Unimplemented]While in combat, throw your pickaxe to send land mines towards your enemies. On contact they deal [VAL] damage.",new double[]{1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0},
			new double[]{1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0},100,1000,UpgradePath.PICKAXE,1),*/
	MINES("Land Mine",ChatColor.GOLD+"Shift+Right-click"+ChatColor.RESET+" air to place down a land mine. Land mines detonate when enemies step near the mine location, dealing [VAL] damage. Mines will automatically detonate after 15 seconds of no activity.\n\nYou can place a maximum of "+ChatColor.GOLD+"[MINEAMT]"+ChatColor.RESET+" mine[MINEAMTPLURAL] at once.",new double[]{1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0},
			new PVPValue(100,1.0),10000,40,UpgradePath.PICKAXE,1),
	OREHARVESTER("Ore Harvester",ChatColor.GOLD+"Shift+Right-click"+ChatColor.RESET+" an ore block to convert the block into a temporary buff. The buff lasts for [VAL] seconds. Duration can be stacked for longer buffs.\n\n "+DisplayOreBonus("Coal Ore","+[COALORE_BONUS]% Critical Damage")+"\n"+DisplayOreBonus("Iron Ore","+[IRONORE_BONUS]% Block Chance")+"\n"+DisplayOreBonus("Gold Ore","+[GOLDORE_BONUS]% Critical Strike Chance")+"\n"+DisplayOreBonus("Redstone Ore","+[REDSTONEORE_BONUS] Maximum Health")+"\n"+DisplayOreBonus("Lapis Lazuli Ore","+[LAPISORE_BONUS] Health Regeneration")+"\n"+DisplayOreBonus("Diamond Ore","+[COALORE_BONUS]% Damage Reduction")+"\n"+DisplayOreBonus("Emerald Ore","+[COALORE_BONUS] Base Damage")+"\n"+LevelCost(40),new double[]{10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0},
			new PVPValue(100,10.0),10000,100,UpgradePath.PICKAXE,40),
	IMPACT("Impact","Damaging an enemy deals [VAL]% of an enemy's health as bonus physical damage on hit."+LevelCost(5),new double[]{1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0},
			new PVPValue(100,1.0),250,20,UpgradePath.PICKAXE,5),
	FORCESTRIKE("Force Strike","Perform an attack that slams an enemy against a wall. Enemies take [FORCESTRIKEVAL] damage on a successful slam, crumbling the walls behind them.\n\n"+ChatColor.YELLOW+"15 second cooldown",new double[]{2.0,2.0,2.0,2.0,2.0,2.0,2.0,2.0,2.0,2.0,2.0,2.0,2.0,2.0,2.0,2.0},
			new PVPValue(100,2.0),10000,350,UpgradePath.PICKAXE,1),

	//Shovel abilities
	SUPPRESS("Suppression","Suppresses a mob on hit for [VAL] seconds.\n\n"
			+ "Suppression prevents movement, attacking, exploding, and teleportation."+LevelCost(10),new double[]{0.02,0.02,0.02,0.02,0.02,0.02,0.02,0.02,0.02,0.02,0.02,0.02,0.02,0.02,0.02,0.02},
			new PVPValue(10,0.02),100,10,UpgradePath.SHOVEL,10),
	ERUPTION("Eruption","Sneak while Left-clicking a mob to damage mobs for [ERUPTIONVAL] damage and knock them up. The eruption also destroys the ground beneath you.",new double[]{3.0,3.0,3.0,3.0,3.0,3.0,3.0,3.0,3.0,3.0,3.0,3.0,3.0,3.0,3.0,3.0},
			new PVPValue(15,3.0),10000,40,UpgradePath.SHOVEL,1),
	EARTHWAVE("Earth Wave","While in mid-air, right-click to instantly slam into the ground and launch soft blocks. This attack ignores fall damage. The larger the fall, the larger the wave.\n\nDeals [EARTHWAVEVAL] damage to every enemy hit by the wave. Deals double damage and knocks up on soft blocks.",new double[]{2.0,2.0,2.0,2.0,2.0,2.0,2.0,2.0,2.0,2.0,2.0,2.0,2.0,2.0,2.0,2.0},
			new PVPValue(15,2.0),10000,100,UpgradePath.SHOVEL,1),
	
	//Axe abilities
	/*BREAKDOWN("Break Down",ChatColor.GRAY+"[Unimplemented] Breaks down armor on mobs. Each hit has a [VAL]% chance to remove a piece of armor from a mob.",new double[]{3,3,3,3,3,3,3,3,3,3},
			new double[]{1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0},100,1000,UpgradePath.AXE,1),
	BUTCHERY("Butchery",ChatColor.GRAY+"[Unimplemented] Broken down armor have a [VAL]% chance to drop onto the ground.",new double[]{10,10,10,10,10,10,10,10,10,10},
			new double[]{0.8,0.8,0.8,0.8,0.8,0.8,0.8,0.8,0.8,0.8},100,1000,UpgradePath.AXE,1),*/
	DAMAGEPOOL("Damage Pool Recovery","Removes [VAL] points from Barbarian's Damage Pool with each attack.",
			new double[]{0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5},
			new PVPValue(30,0.5),10000,1,UpgradePath.AXE,1),
	LIFESTACK("Life Stack","Increases Barbarian's lifesteal stacks by [VAL] per hit.",
			new double[]{0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5},
			new PVPValue(30,0.5),10000,1,UpgradePath.AXE,1),
	LIFESUCK("Life Sucker","Directly heals [VAL]% of damage dealt as health, with a maximum of [LIFESUCKVAL] health healed per hit."+LevelCost(3),
			new double[]{0.8,0.8,0.8,0.8,0.8,0.8,0.8,0.8,0.8,0.8,0.8,0.8,0.8,0.8,0.8,0.8},
			new PVPValue(30,0.8),100,40,UpgradePath.AXE,3),
	HIGHDIVE("High Dive","Sneak while pressing the drop key to become rooted for 3 seconds, storing [VAL]% damage taken and gaining 100% knockback resistance. Then leap up high into the air and slam the ground. High Dive increases the base damage of Barbarian's Leaping Strike by the amount of damage stored."+LevelCost(3),
			new double[]{0.8,0.8,0.8,0.8,0.8,0.8,0.8,0.8,0.8,0.8,0.8,0.8,0.8,0.8,0.8,0.8},
			new PVPValue(30,0.8),100,100,UpgradePath.AXE,3),
	
	//Scythe abilities
	AOE("Area of Effect","Deals damage to targets up to [AOEVAL]m from the main target hit.",new double[]{0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1},
			new PVPValue(5,1.0),10000,1,UpgradePath.SCYTHE,1),
	DEATHMARK("Death Mark","Applies a Death Mark stack to enemies hit. Death mark stacks last for 5 seconds, and refresh on each hit.\n\nMarks can be detonated at any time by right-clicking. Targets killed with Death Mark resets the cooldown. Targets not killed lose half their Death Mark stacks.\n\n Each death mark stack applied deals [VAL] true damage.",new double[]{1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0},
			new PVPValue(10,1.0),10000,10,UpgradePath.SCYTHE,1),
	CRIPPLE("Cripple","Every 10 death marks applied on a monster increases damage dealt from all damage sources by [VAL]%.",new double[]{1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0},
			new PVPValue(1,1.0),100,1000,UpgradePath.SCYTHE,1),
	
	//General abilities
	AUTOREPAIR("Auto Repair","1% chance every second to repair [VAL] durability to the artifact item\n\nThe item must be sitting in your hotbar or must be equipped for this ability to work. This ability is less effective with no sunlight!",new double[]{1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0},
			new PVPValue(30,1.0),10000,1,UpgradePath.ALL,1),
	GREED("Greed","Increases Drop rate by [VAL]% . Health is halved, health regeneration is halved. Each kill has a [GREEDCHANCE]% chance to consume the Greed buff."+TemporarySkill(true),new double[]{10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,15.0,15.0,15.0,20.0,25.0,30.0,40.0},
			new PVPValue(0,10.0),10,10,UpgradePath.ALL,1),
	GROWTH("Growth","Sets the Potential of your Artifact to 20%."+TemporarySkill(false),new double[]{1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0},
			new PVPValue(0,1.0),1,10,UpgradePath.ALL,1),
	/*REMOVE_CURSE("Remove Curse",ChatColor.GRAY+"[Unimplemented] Removes a level of a curse from the Artifact.",new double[]{-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0},
			new double[]{0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0},1,1000,UpgradePath.ALL),*/
	PRESERVATION("Preservation","Potential decays [POTVAL]% slower.",new double[]{0.90,0.90,0.90,0.90,0.90,0.90,0.90,0.90,0.90,0.90,0.90,0.90,0.90,0.90,0.90,0.90},
			new PVPValue(100,0.9),100,1,UpgradePath.ALL,1),
	/*EXP_MULT("Mega XP",ChatColor.GRAY+"[Unimplemented] Increases experience dropped from monsters by [VAL]% .",new double[]{5,5,5,5,5,5,5,5,5,5,5,5,5,5,5},
			new double[]{1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0},100,1000,UpgradePath.ALL),*/
	
	//Bad stuff
	REDUCEDMG("Weakness","[VAL]% Decrease in Base Damage.",new double[]{8,8,8,8,8,8,8,8,8,8},
			new PVPValue(30,8),100,3,UpgradePath.ALL,1),
	REDUCEDEF("Imperil","[VAL]% Decrease in Damage Reduction",new double[]{8,8,8,8,8,8,8,8,8,8},
			new PVPValue(30,8),100,5,UpgradePath.ALL,1),
	LIFE_REDUCTION("Health Cut","[VAL]% decrease in maximum health.",new double[]{30,30,30,30,30,30,30,30,30,30},
			new PVPValue(30,30),100,5,UpgradePath.ALL,1),
	LOWER_DEFENSE("Debilitate","[VAL]% decrease in damage reduction.",new double[]{30,30,30,30,30,30,30,30,30,30},
			new PVPValue(30,30),100,5,UpgradePath.ALL,1),
	TELEPORT("Teleport","[VAL]% chance to teleport the player to a random location on artifact experience gain.",new double[]{3,3,3,3,3,3,3,3,3,3},
			new PVPValue(30,3),100,10,UpgradePath.ALL,1),
	DRAINING("Draining","[VAL]% chance to remove a level of experience on artifact experience gain.",new double[]{1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0},
			new PVPValue(30,1.0),100,10,UpgradePath.ALL,1),
	NOREGEN("Weary","No health regenerates.",new double[]{1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0},
			new PVPValue(30,1.0),100,15,UpgradePath.ALL,1),
	STARVATION("Starvation","[VAL]% chance to cause [HUNGERVAL] seconds of Hunger on experience gain.",new double[]{5,5,5,5,5,5,5,5,5,5},
			new PVPValue(30,5),100,15,UpgradePath.ALL,1),
	BURN("Flammable","All burn damage deals x[VAL] damage.",new double[]{4.0,4.0,4.0,4.0,4.0,4.0,4.0,4.0,4.0,4.0},
			new PVPValue(30,0.4),100,25,UpgradePath.ALL,1),
	FROZEN("Frozen","Player will be inflicted with increasing levels of slowness and fatigue until finally frozen and killed.",new double[]{1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0},
			new PVPValue(30,1.0),1,45,UpgradePath.ALL,1),
	PETRIFICATION("Petrification","Player will be inflicted with increasing levels of slowness and fatigue until finally petrified and killed.",new double[]{1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0},
			new PVPValue(30,1.0),1,45,UpgradePath.ALL,1),
	;

	final static double[] decayvals = new double[]{0.5,0.588,0.6505,0.6990,0.7386,0.7720,0.8010,0.8266,0.8495,0.8702,0.8891,0.9225,0.9515,0.9771,1.0};
	
	public static int LINE_SIZE=50;
	String name;
	String desc;
	double[] baseval;
	double[] decayval;
	int maxlv;
	int requirement;
	UpgradePath upgrade;
	int apcost;
	PVPValue pvpval;
	
	ArtifactAbility(String name, String desc, double[] baseval, PVPValue pvpval, int maxlv, int requirement, UpgradePath upgrade, int apcost) {
		this.name=name;
		this.desc=desc;
		this.baseval=baseval;
		this.maxlv=maxlv;
		this.requirement=requirement;
		AwakenedArtifact.ability_map.put(this,this.name);
		AwakenedArtifact.name_map.put(this.name,this);
		this.upgrade=upgrade;
		this.apcost=apcost;
		this.pvpval=pvpval;
	}
	
	public PVPValue getPVPValue() {
		return pvpval;
	}
	
	private static String LevelCost(int i) {
		return "\n\n"+ChatColor.RED+"Costs "+i+" AP";
	}

	private static String TemporarySkill(boolean knockoff) {
		return "\n\n"+ChatColor.RED+"Consumes 1 Max AP Point"+((knockoff)?" when knocked off.":"");
	}
	
	public int getAPCost() {
		return this.apcost;
	}

	public String GetName() {
		return this.name;
	}
	
	public String GetDescription() {
		return this.desc;
	}
	
	public double GetBaseValue(int tier) {
		if (tier<=0) {tier=1;}
		if (tier-1<this.baseval.length) {
			return this.baseval[tier-1];
		} else {
			TwosideKeeper.log("WARNING! Base value for tier "+tier+" does not exist for ability "+this.name()+"! Falling back to highest possible value.", 1);
			return this.baseval[this.baseval.length-1];
		}
	}
	
	public double GetDecayValue(int tier) {
		if (tier<=0) {tier=1;}
		if (tier-1<decayvals.length) {
			return decayvals[tier-1];
		} else {
			TwosideKeeper.log("WARNING! Decay value for tier "+tier+" does not exist for decayvals array! Falling back to highest possible value.", 1);
			return decayvals[decayvals.length-1];
		}
	}
	
	public int GetMaxLevel() {
		return maxlv;
	}
	
	public int GetMinLevel() {
		return requirement;
	}
	
	public static HashMap<ArtifactAbility,Integer> getEnchantments(ItemStack item) {
		HashMap<ArtifactAbility,Integer> abilities = new HashMap<ArtifactAbility,Integer>();
		if (GenericFunctions.isArtifactEquip(item)) {
			List<String> lore = item.getItemMeta().getLore(); 
			//From Element 7 and onwards, we know these are abilities added to the item. Retrieve them.
			for (int i=AwakenedArtifact.findAPLine(lore)+1;i<lore.size();i++) {
				String[] splitstring = lore.get(i).split(" ");
				TwosideKeeper.log(splitstring.length+"",5);
				String newstring = "";
				TwosideKeeper.log(lore.get(i), 5);
				for (int j=1;j<splitstring.length-1;j++) {
					if (newstring.equalsIgnoreCase("")) {
						newstring+=splitstring[j];
					} else {
						newstring+=" "+splitstring[j];
					}
				}
				//This is the name of the enchantment. Now connect it with the name map we made.
				abilities.put(AwakenedArtifact.name_map.get(ChatColor.stripColor(newstring)),Integer.parseInt(splitstring[splitstring.length-1]));
			}
		}
		return abilities;
	}
	
	public static int getEnchantmentLevel(ArtifactAbility ability, ItemStack item) {
		//Get the enchantment level of a particular enchantment.
		HashMap<ArtifactAbility,Integer> enchants = getEnchantments(item);
		if (enchants.containsKey(ability)) {
			return (int)enchants.get(ability);
		} else {
			return 0;
		}
	}
	
	public static ItemStack applyEnchantment(ArtifactAbility ability, int lv, ItemStack item) {
		ItemMeta m = item.getItemMeta();
		List<String> lore = m.getLore();
		if (containsEnchantment(ability,item)) {
			//We just need to find the line and upgrade it then.
			boolean belowartifactline=false;
			for (int i=0;i<lore.size();i++) {
				if (!belowartifactline) {
					if (lore.get(i).contains(ChatColor.GOLD+"Ability Points:")) {
						belowartifactline=true;
					}
				} else {
					String filterstring = ChatColor.stripColor(lore.get(i).replaceFirst(" ", "").substring(0, lore.get(i).lastIndexOf(" ")-1));
					//TwosideKeeper.log("CHECKING _"+filterstring+"_ TO _"+ability.GetName()+"_", 0);
					if (filterstring.equalsIgnoreCase(ability.GetName())) {
						//This is the line! Modify it.
						lore.set(i, ChatColor.YELLOW+" "+ability.GetName()+" "+(lv));
						break;
					}
				}
			}
			m.setLore(lore);
			item.setItemMeta(m);
			return item;
		} else {
			//Otherwise we are just appending it.
			lore.add(ChatColor.YELLOW+" "+ability.GetName()+" "+lv);
			m.setLore(lore);
			item.setItemMeta(m);
			return item;
		}
	}
	
	public static ItemStack removeEnchantment(ArtifactAbility ability, ItemStack item) {
		ItemMeta m = item.getItemMeta(); 
		List<String> lore = m.getLore();
		if (containsEnchantment(ability,item)) {
			for (int i=0;i<lore.size();i++) {
				if (lore.get(i).contains(ability.GetName())) {
					//TwosideKeeper.log("Removed "+ability.GetName(), 2);
					//This is the line! Remove it.
					lore.remove(i);
					i--;
				}
			}
		}
		m.setLore(lore);
		item.setItemMeta(m);
		return item;
	}
	
	public static ItemStack removeAllEnchantments(ItemStack item) {
		HashMap<ArtifactAbility,Integer> enchants = getEnchantments(item);
		for (ArtifactAbility ab : enchants.keySet()) {
			//TwosideKeeper.log("Checking for enchantment "+ab.GetName(), 2);
			item = removeEnchantment(ab,item);
		}
		item = AwakenedArtifact.setAP(item, AwakenedArtifact.getMaxAP(item));
		return item;
	}
	
	static boolean hasCurse(ItemStack item) {
		HashMap<ArtifactAbility,Integer> map = getEnchantments(item);
		if (map.containsKey(REDUCEDMG) ||
				map.containsKey(REDUCEDEF) ||
				map.containsKey(LIFE_REDUCTION) ||
				map.containsKey(LOWER_DEFENSE) ||
				map.containsKey(TELEPORT) ||
				map.containsKey(DRAINING) ||
				map.containsKey(NOREGEN) ||
				map.containsKey(STARVATION) ||
				map.containsKey(BURN) ||
				map.containsKey(FROZEN) ||
				map.containsKey(PETRIFICATION)) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean containsEnchantment(ArtifactAbility ability, ItemStack item) {
		//TwosideKeeper.log("Enchantment list: "+getEnchantments(item), 0);
		return getEnchantments(item).containsKey(ability);
	}
	
	public static boolean isCompatibleWithUpgrade(ItemStack item, UpgradePath upgrade) {
		TwosideKeeper.log("Checking compatibility with "+upgrade, 4);
		if (upgrade!=UpgradePath.ALL) {
			switch (upgrade) {
				case WEAPON:{
						if (item.getType().toString().contains("SWORD") ||
							(item.getType().toString().contains("AXE") && !item.getType().toString().contains("PICKAXE")) ||
							item.getType().toString().contains("FISHING_ROD") ||
							item.getType().toString().contains("HOE") ||
							item.getType().toString().contains("BOW")) {
							//This is a valid weapon.
						return true;
					}
				}break;
				case ARMOR:{
					if (item.getType().toString().contains("HELMET") ||
						item.getType().toString().contains("CHESTPLATE") ||
						item.getType().toString().contains("LEGGINGS") ||
						item.getType().toString().contains("BOOTS")) {
						//This is a valid armor piece.
						return true;
					}
				}break;
				case TOOL:{
					if (item.getType().toString().contains("AXE") ||
						item.getType().toString().contains("HOE") ||
						item.getType().toString().contains("SPADE")) {
						//This is a valid tool.
						return true;
					}
				}break;
				case SWORD:{
					if (item.getType().toString().contains("SWORD")) {
						//This is a valid sword.
						return true;
					}
				}break;
				case AXE:{
					if (item.getType().toString().contains("AXE") && !item.getType().toString().contains("PICKAXE")) {
						//This is a valid axe.
						return true;
					}
				}break;
				case PICKAXE:{
					if (item.getType().toString().contains("PICKAXE")) {
						//This is a valid pickaxe.
						return true;
					}
				}break;
				case SCYTHE:{
					if (item.getType().toString().contains("HOE")) {
						//This is a valid hoe.
						TwosideKeeper.log("Valid???", 5);
						return true;
					}
				}break;
				case SHOVEL:{
					if (item.getType().toString().contains("SPADE")) {
						//This is a valid shovel.
						return true;
					}
				}break;
				case BOW:{
					if (item.getType().toString().contains("BOW")) {
						//This is a valid bow.
						return true;
					}
				}break;
				case FISHING_ROD:{
					if (item.getType().toString().contains("FISHING_ROD")) {
						//This is a valid fishing rod.
						return true;
					}
				}break;
				case PROVOKE:{
					if ((item.getType().toString().contains("AXE") && !item.getType().toString().contains("PICKAXE"))
							|| item.getType().toString().contains("SWORD")) {
						//This is an item that can upgrade with Provoke.
						return true;
					}
				}break;
				case BASIC:{
					if (!item.getType().toString().contains("HELMET") &&
							!item.getType().toString().contains("CHESTPLATE") &&
							!item.getType().toString().contains("LEGGINGS") &&
							!item.getType().toString().contains("BOOTS")) {
						//This is a valid basic piece.
						return true;
					}
				}break;
				case ALL:{
					return true;
				}
				default:{
					return true;
				}
			}
		} else {
			return true;
		}
		return false;
	}
	
	public static ItemStack downgradeEnchantment(Player p, ItemStack item, ArtifactAbility ability) {
		if (isCompatibleWithUpgrade(item,ability.upgrade)) {
			if (getEnchantmentLevel(ability,item)>1) { //This is more than 1 level, so we just remove one level from it.
				//This is allowed. Proceed.
				item = applyEnchantment(ability,getEnchantmentLevel(ability,item)-1,item);
				//AwakenedArtifact.addAP(item, 1);
			} else {
				//Just remove it completely.
				removeEnchantment(ability,item);
			}
		} else {
			p.sendMessage(ChatColor.RED+"This upgrade is not compatible with this item!");
		}
		return item;
	}

	public static ItemStack upgradeEnchantment(Player p, ItemStack item, ArtifactAbility ability) {
		//Verifies that the enchantment can be upgraded firstly.
		//Get the level we are upgrading to.
		int level = getEnchantmentLevel(ability,item);
		//Make sure this item is compatible with the enchantment being applied.
		if (isCompatibleWithUpgrade(item,ability.upgrade)) {
			if (AwakenedArtifact.getAP(item)>=ability.getAPCost()) {
				if (ability.GetMaxLevel()>level && ability.GetMinLevel()<=AwakenedArtifact.getLV(item)) {
					//This is allowed. Proceed.
					item = applyEnchantment(ability,level+1,item);
					AwakenedArtifact.addAP(item, -ability.getAPCost());
					p.sendMessage(ChatColor.AQUA+"Successfully applied "+ChatColor.BLUE+ability.GetName()+" "+(level+1)+ChatColor.AQUA+" to your artifact!");
					/*if (ability.equals(ArtifactAbility.GRACEFULDODGE) ||
							ability.equals(ArtifactAbility.SUPPRESS)) {
						//Remove a level from using a temporary ability.
						AwakenedArtifact.addAP(item, -9);
					}*/
					if (ability.equals(ArtifactAbility.GROWTH)) {
						removeEnchantment(ArtifactAbility.GROWTH,item);
						AwakenedArtifact.setPotential(item, 20);
						AwakenedArtifact.setMaxAP(item, AwakenedArtifact.getMaxAP(item)-1);
					}
					int apamt = AwakenedArtifact.getAP(item);
					if (apamt>0) {
						TextComponent tc = new TextComponent("   You have "+ChatColor.GREEN+apamt+ChatColor.WHITE+" ability point"+((apamt==1)?"":"s")+" remaining!");
						/*TextComponent tc = new TextComponent("   You have "+ChatColor.GREEN+apamt+ChatColor.WHITE+" ability point"+((apamt==1)?"":"s")+" remaining! Click ");
						TextComponent ac = new TextComponent(ChatColor.GREEN+"[HERE]"+ChatColor.WHITE);
						ac.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ComponentBuilder(ChatColor.ITALIC+"Click to add another skill point!").create()));
						ac.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/awakenedartifact"));
						tc.addExtra(ac);
						tc.addExtra(" to open up the ability upgrade menu.");;*/
						p.spigot().sendMessage(tc);
						p.spigot().sendMessage(ArtifactAbility.GenerateMenu(ArtifactItemType.getArtifactItemTypeFromItemStack(p.getInventory().getItem(GenericFunctions.CalculateSlot(item,p))).getUpgradePath(), CustomDamage.getBaseWeaponDamage(item, p, null), item,GenericFunctions.CalculateSlot(item,p),p));
					}
				} else {
					if (ability.GetMaxLevel()<=level) {
						p.sendMessage(ChatColor.RED+"This ability cannot be upgraded any further!");
					} else {
						p.sendMessage(ChatColor.RED+"You need to reach level "+ability.GetMinLevel()+" on your artifact item first!");
					}
				}
			} else {
				p.sendMessage(ChatColor.RED+"Insufficient AP to level up this upgrade! Earn more AP first!");
			}
		} else {
			p.sendMessage(ChatColor.RED+"This upgrade is not compatible with this item!");
		}
		return item;
	}
	
	public static TextComponent DisplayAbility(ArtifactAbility ability, double playerdmgval, ItemStack targetitem, int slot, Player p) {
		boolean unlocked=true;
		String lockedreason = "";
		if (AwakenedArtifact.getLV(targetitem)<ability.GetMinLevel() || getEnchantmentLevel(ability,targetitem)>=ability.GetMaxLevel() || AwakenedArtifact.getAP(targetitem)<ability.getAPCost()) {
			unlocked=false;
			if (AwakenedArtifact.getLV(targetitem)<ability.GetMinLevel()) {
				lockedreason=ChatColor.GRAY+""+ChatColor.ITALIC+"Your Artifact needs to reach Level "+ability.GetMinLevel()+" to obtain this ability.";
			} else if (getEnchantmentLevel(ability,targetitem)>=ability.GetMaxLevel()) {
				lockedreason=ChatColor.GRAY+""+ChatColor.ITALIC+"Your Artifact has reached the maximum level for this ability!";
			} else if (AwakenedArtifact.getAP(targetitem)<ability.getAPCost()) {
				lockedreason=ChatColor.GRAY+""+ChatColor.ITALIC+"Your Artifact does not have enough ability points to upgrade "+ChatColor.GREEN+ability.GetName()+ChatColor.GRAY+"!";
			}
		}
		int enchantlevel=0;
		if (containsEnchantment(ability,targetitem)) {
			enchantlevel=getEnchantmentLevel(ability,targetitem);
		}
		String displaystring = "";
		if (enchantlevel>0) {
			displaystring = displayDescriptionUpgrade(ability,ArtifactUtils.getArtifactTier(targetitem),enchantlevel,enchantlevel+1,playerdmgval, PVP.isPvPing(p));
		} else {
			displaystring = displayDescription(ability,ArtifactUtils.getArtifactTier(targetitem),enchantlevel+1,playerdmgval, PVP.isPvPing(p));
		}
		TextComponent tc = new TextComponent(((unlocked)?ChatColor.GREEN:ChatColor.RED)+"["+ability.GetName()+" "+(enchantlevel+1)+"] ");
		tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ComponentBuilder(WordUtils.wrap(ChatColor.BLUE+ability.GetName()+"\n\n"+displaystring+((lockedreason.equalsIgnoreCase(""))?"":"\n\n"),LINE_SIZE,"\n",true)+WordUtils.wrap(lockedreason,LINE_SIZE,"\n"+net.md_5.bungee.api.ChatColor.GRAY,true)).create()));
		if (slot!=0) {
			//Apply the enchantment to the proper item slot.
			tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/awakenedartifact levelup "+ability.name()+" "+slot));
		} else {
			tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/awakenedartifact levelup "+ability.name()));
		}
		if (getEnchantmentLevel(ability,targetitem)<ability.GetMaxLevel()) {
			return tc;
		} else {
			return new TextComponent(""); //We don't want to display enchantments that are capped in the menu.
		}
	}
	
	public static TextComponent GenerateMenu(UpgradePath path, double playerdmgval, ItemStack targetitem, Player p) {
		return GenerateMenu(path,playerdmgval,targetitem,0,p);
	}
	
	public static TextComponent GenerateMenu(UpgradePath path, double playerdmgval, ItemStack targetitem, int slot, Player p) {
		TextComponent msg1 = new TextComponent("Choose an ability to upgrade "+((targetitem.hasItemMeta() && targetitem.getItemMeta().hasDisplayName())?targetitem.getItemMeta().getDisplayName():GenericFunctions.UserFriendlyMaterialName(targetitem))+ChatColor.RESET+":\n\n");
		int i=0;
		TextComponent text = new TextComponent("");
		TwosideKeeper.log("Checking path "+path, 5);
		if (path==UpgradePath.BOW || //Weapon category.
				path==UpgradePath.WEAPON ||
				path==UpgradePath.SWORD ||
				path==UpgradePath.AXE ||
				path==UpgradePath.FISHING_ROD ||
				path==UpgradePath.SCYTHE ||
				path==UpgradePath.BASIC) {
			text=DisplayAbility(DAMAGE,playerdmgval,targetitem,slot,p);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
			text=DisplayAbility(ARMOR_PEN,playerdmgval,targetitem,slot,p);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
			text=DisplayAbility(EXECUTION,playerdmgval,targetitem,slot,p);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
			if (path!=UpgradePath.BASIC) {
				text=DisplayAbility(LIFESTEAL,playerdmgval,targetitem,slot,p);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
				text=DisplayAbility(CRITICAL,playerdmgval,targetitem,slot,p);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
				text=DisplayAbility(CRIT_DMG,playerdmgval,targetitem,slot,p);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
				text=DisplayAbility(HIGHWINDER,playerdmgval,targetitem,slot,p);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
				if (path==UpgradePath.SWORD) {
					text=DisplayAbility(PROVOKE,playerdmgval,targetitem,slot,p);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
					text=DisplayAbility(COMBO,playerdmgval,targetitem,slot,p);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
				} else
				if (path==UpgradePath.AXE) {
					text=DisplayAbility(PROVOKE,playerdmgval,targetitem,slot,p);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
					//text=DisplayAbility(BREAKDOWN,playerdmgval,targetitem,slot);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
					//text=DisplayAbility(BUTCHERY,playerdmgval,targetitem,slot);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
					if (TwosideKeeper.NEWARTIFACTABILITIES_ACTIVATED) {
						text=DisplayAbility(DAMAGEPOOL,playerdmgval,targetitem,slot,p);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
						text=DisplayAbility(LIFESTACK,playerdmgval,targetitem,slot,p);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
						text=DisplayAbility(LIFESUCK,playerdmgval,targetitem,slot,p);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
						text=DisplayAbility(HIGHDIVE,playerdmgval,targetitem,slot,p);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
					}
				} else
				if (path==UpgradePath.FISHING_ROD) {
				} else
				if (path==UpgradePath.BOW) {
					text=DisplayAbility(MARKSMAN,playerdmgval,targetitem,slot,p);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
					/*text=DisplayAbility(SIEGESTANCE,playerdmgval,targetitem,slot);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
					text=DisplayAbility(ARROWSHOWER,playerdmgval,targetitem,slot);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
					text=DisplayAbility(TARGETING,playerdmgval,targetitem,slot);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
					text=DisplayAbility(ENDERTURRET,playerdmgval,targetitem,slot);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}*/
				} else
				if (path==UpgradePath.SCYTHE) {
					text=DisplayAbility(AOE,playerdmgval,targetitem,slot,p);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
					text=DisplayAbility(DEATHMARK,playerdmgval,targetitem,slot,p);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
				}
			}
		} else
		if (path==UpgradePath.ARMOR //Armor category.
		) {
			text=DisplayAbility(DAMAGE_REDUCTION,playerdmgval,targetitem,slot,p);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
			text=DisplayAbility(HEALTH,playerdmgval,targetitem,slot,p);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
			text=DisplayAbility(HEALTH_REGEN,playerdmgval,targetitem,slot,p);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
			text=DisplayAbility(STATUS_EFFECT_RESISTANCE,playerdmgval,targetitem,slot,p);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
			text=DisplayAbility(SHADOWWALKER,playerdmgval,targetitem,slot,p);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
			text=DisplayAbility(SURVIVOR,playerdmgval,targetitem,slot,p);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
			text=DisplayAbility(DODGE,playerdmgval,targetitem,slot,p);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
			text=DisplayAbility(GRACEFULDODGE,playerdmgval,targetitem,slot,p);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
		} else
		if (path==UpgradePath.TOOL || //Tool category.
				path==UpgradePath.SHOVEL ||
				path==UpgradePath.PICKAXE
			) {
			text=DisplayAbility(DAMAGE,playerdmgval,targetitem,slot,p);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
			text=DisplayAbility(ARMOR_PEN,playerdmgval,targetitem,slot,p);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
			text=DisplayAbility(EXECUTION,playerdmgval,targetitem,slot,p);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
			if (path==UpgradePath.SHOVEL) {
				text=DisplayAbility(SUPPRESS,playerdmgval,targetitem,slot,p);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
				text=DisplayAbility(ERUPTION,playerdmgval,targetitem,slot,p);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
				text=DisplayAbility(EARTHWAVE,playerdmgval,targetitem,slot,p);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
			} else
			if (path==UpgradePath.PICKAXE) {
				//text=DisplayAbility(SCAVENGE,playerdmgval,targetitem,slot);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
				if (TwosideKeeper.NEWARTIFACTABILITIES_ACTIVATED) {
					text=DisplayAbility(MINES,playerdmgval,targetitem,slot,p);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
					text=DisplayAbility(OREHARVESTER,playerdmgval,targetitem,slot,p);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
					text=DisplayAbility(IMPACT,playerdmgval,targetitem,slot,p);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
					text=DisplayAbility(FORCESTRIKE,playerdmgval,targetitem,slot,p);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
				}
			}
		}

		text=DisplayAbility(GREED,playerdmgval,targetitem,slot,p);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
		text=DisplayAbility(AUTOREPAIR,playerdmgval,targetitem,slot,p);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
		text=DisplayAbility(GROWTH,playerdmgval,targetitem,slot,p);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
		//text=DisplayAbility(REMOVE_CURSE,playerdmgval,targetitem,slot);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
		text=DisplayAbility(PRESERVATION,playerdmgval,targetitem,slot,p);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
		//text=DisplayAbility(EXP_MULT,playerdmgval,targetitem,slot);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
		
		return msg1;
	}
	
	static String DisplayOreBonus(String oretype, String bonus) {
		return ChatColor.LIGHT_PURPLE+oretype+": "+ChatColor.YELLOW+" "+bonus;
	}
	
	public static String displayDescription(ArtifactAbility ability, int tier, int abilitylv, double playerdmgval, boolean pvp) { //Level to display information for.		
		String msg = ability.GetDescription();
		DecimalFormat df = new DecimalFormat("0.00");
		msg=msg.replace("[VAL]", ChatColor.BLUE+df.format(calculateValue(ability,tier,abilitylv,pvp))+ChatColor.RESET);
		msg=msg.replace("[200VAL]", ChatColor.BLUE+df.format(200+calculateValue(ability,tier,abilitylv,pvp))+ChatColor.RESET);
		msg=msg.replace("[PENDMG]", ChatColor.BLUE+df.format(calculateValue(ability,tier,abilitylv,pvp)/100*playerdmgval)+ChatColor.RESET); //Based on multiplying [VAL] by the base damage value.
		msg=msg.replace("[HUNGERVAL]", ChatColor.BLUE+df.format(10*abilitylv)+ChatColor.RESET);
		msg=msg.replace("[FATALDMG]", ChatColor.BLUE+df.format(120*abilitylv)+ChatColor.RESET);
		msg=msg.replace("[REPAIRCHANCE]", ChatColor.BLUE+df.format(tier/3)+ChatColor.RESET);
		msg=msg.replace("[DODGEVAL]", ChatColor.BLUE+df.format(tier)+ChatColor.RESET);
		msg=msg.replace("[GREEDCHANCE]", ChatColor.BLUE+df.format(8-(tier/2d))+ChatColor.RESET);
		msg=msg.replace("[ERUPTIONVAL]", ChatColor.BLUE+df.format(35+calculateValue(ability,tier,abilitylv,pvp))+ChatColor.RESET);
		msg=msg.replace("[EARTHWAVEVAL]", ChatColor.BLUE+df.format(20+calculateValue(ability,tier,abilitylv,pvp))+ChatColor.RESET);
		msg=msg.replace("[AOEVAL]", ChatColor.BLUE+df.format(1+calculateValue(ability,tier,abilitylv,pvp))+ChatColor.RESET);
		msg=msg.replace("[POTVAL]", ChatColor.BLUE+df.format(5+calculateValue(ability,tier,abilitylv,pvp))+ChatColor.RESET);
		msg=msg.replace("[GRACEFULVAL]", ChatColor.BLUE+df.format(0.1+calculateValue(ability,tier,abilitylv,pvp))+ChatColor.RESET);
		msg=msg.replace("[FORCESTRIKEVAL]", ChatColor.BLUE+df.format(60+calculateValue(ability,tier,abilitylv,pvp))+ChatColor.RESET);
		return msg;
	}
	public static String displayDescriptionUpgrade(ArtifactAbility ability, int tier, int fromlv, int tolv, double playerdmgval, boolean pvp) { //Level to display information for.		
		String msg = ability.GetDescription();
		DecimalFormat df = new DecimalFormat("0.00");
		msg=msg.replace("[VAL]", DisplayChangedValue(df.format(calculateValue(ability,tier,fromlv,pvp)),df.format(calculateValue(ability,tier,tolv,pvp))));
		msg=msg.replace("[200VAL]", ChatColor.BLUE+DisplayChangedValue(df.format(200+calculateValue(ability,tier,fromlv,pvp)),df.format(200+calculateValue(ability,tier,tolv,pvp)))+ChatColor.RESET);
		msg=msg.replace("[PENDMG]", DisplayChangedValue(df.format(calculateValue(ability,tier,fromlv,pvp)/100*playerdmgval),df.format(calculateValue(ability,tier,tolv,pvp)/100*playerdmgval))); //Based on multiplying [VAL] by the base damage value.
		msg=msg.replace("[HUNGERVAL]", DisplayBadChangedValue(df.format(10*fromlv),df.format(10*tolv)));
		msg=msg.replace("[FATALDMG]", DisplayChangedValue(df.format(120-fromlv),df.format(120-tolv)));
		msg=msg.replace("[REPAIRCHANCE]", df.format(tier/3));
		msg=msg.replace("[DODGEVAL]", df.format(tier));
		msg=msg.replace("[GREEDCHANCE]", ChatColor.BLUE+df.format(8-(tier/2d))+ChatColor.RESET);
		msg=msg.replace("[ERUPTIONVAL]", DisplayChangedValue(df.format(35+calculateValue(ability,tier,fromlv,pvp))+ChatColor.RESET,df.format(35+calculateValue(ability,tier,tolv,pvp))+ChatColor.RESET));
		msg=msg.replace("[EARTHWAVEVAL]", DisplayChangedValue(df.format(20+calculateValue(ability,tier,fromlv,pvp))+ChatColor.RESET,df.format(20+calculateValue(ability,tier,tolv,pvp))+ChatColor.RESET));
		msg=msg.replace("[AOEVAL]", DisplayChangedValue(df.format(1+calculateValue(ability,tier,fromlv,pvp))+ChatColor.RESET,df.format(1+calculateValue(ability,tier,tolv,pvp))+ChatColor.RESET));
		msg=msg.replace("[POTVAL]", DisplayChangedValue(df.format(5+calculateValue(ability,tier,fromlv,pvp))+ChatColor.RESET,df.format(5+calculateValue(ability,tier,tolv,pvp))+ChatColor.RESET));
		msg=msg.replace("[GRACEFULVAL]", DisplayChangedValue(df.format(0.1+calculateValue(ability,tier,fromlv,pvp))+ChatColor.RESET,df.format(0.1+calculateValue(ability,tier,tolv,pvp))+ChatColor.RESET));
		msg=msg.replace("[FORCESTRIKEVAL]", DisplayChangedValue(df.format(60+calculateValue(ability,tier,fromlv,pvp))+ChatColor.RESET,df.format(60+calculateValue(ability,tier,tolv,pvp))+ChatColor.RESET));
		return msg;
	}
	
	static String DisplayChangedValue(String val1,String val2) {
		return ChatColor.DARK_GRAY+""+ChatColor.STRIKETHROUGH+val1+ChatColor.RESET+ChatColor.GREEN+val2+ChatColor.DARK_GREEN+ChatColor.BOLD+"^"+ChatColor.RESET;
	}
	static String DisplayBadChangedValue(String val1,String val2) {
		return ChatColor.DARK_RED+""+ChatColor.STRIKETHROUGH+val1+ChatColor.RESET+ChatColor.RED+val2+ChatColor.DARK_RED+ChatColor.BOLD+"v"+ChatColor.RESET;
	}

	public static double calculateValue(ArtifactAbility ab, int artifactTier, int enchantmentLevel, boolean pvp) {
		double sum=0;
		TwosideKeeper.log("Ability "+ab.GetName(), 4);
		/*for(int i=0;i<abilitylevel;i++){
			TwosideKeeper.log("Old Sum:"+sum+"::i:"+i, 5);
		    sum+=1d/(1d+(ability.GetDecayValue(artifacttier)*(double)i));
			TwosideKeeper.log("New Sum:"+sum+"::i:"+i, 5);
		}
		TwosideKeeper.log("Sum is "+sum, 5);
		TwosideKeeper.log("Base value is "+ability.GetBaseValue(artifacttier), 4);
		return sum*ability.GetBaseValue(artifacttier);*/
		//return Math.pow(ability.GetBaseValue(artifacttier)*abilitylevel, ability.GetDecayValue(artifacttier));
		if (pvp) {
			return ab.pvpval.getBaseValue() * Math.pow(ab.pvpval.getPointValue(),ab.GetDecayValue(15));
		} else {
			return ab.GetBaseValue(artifactTier) * Math.pow(enchantmentLevel, ab.GetDecayValue(artifactTier));
		}
	}
	
}