package sig.plugin.TwosideKeeper.HelperStructures;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import sig.plugin.TwosideKeeper.AwakenedArtifact;
import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;

public enum ArtifactAbility {
	//Enum Structure:
	// "Friendly Name", "Description", base value (per point) (-1 means it's a TEMPORARY ability.), decay value (per point), max level, level requirement (The min level required to get this perk), item type
	//Temporary abilities: Work for 1 level and wear off afterward.
	
	//Weapon Abilities
	DAMAGE("Strike","Improves Base Damage by [VAL]",new double[]{1.0,2.0,3.0,4.0,5.0,6.0,7.0,8.0,9.0,10.0},
			new double[]{1.0,0.9,0.85,0.8,0.75,0.7,0.65,0.6,0.55,0.5},100,1,UpgradePath.BASIC),
	ARMOR_PEN("Piercing","[VAL]% of your damage is ignored by resistances. ([PENDMG] damage)",new double[]{1.0,2.0,3.0,4.0,5.0,6.0,7.0,8.0,9.0,10.0}, 
			new double[]{1.0,1.0,0.9,0.9,0.8,0.8,0.7,0.7,0.6,0.5},100,1,UpgradePath.BASIC),
	EXECUTION("Execute","Deals [VAL] extra damage for every 20% of target's missing health.",new double[]{0.125,0.25,1.375,0.5,0.625,0.75,0.875,1.0,1.125,1.25},
			new double[]{1.0,1.0,0.9,0.9,0.8,0.8,0.7,0.7,0.6,0.5},100,1,UpgradePath.BASIC),
	LIFESTEAL("Lifesteal","Heals [VAL]% of the damage dealt to targets back to your health pool.",new double[]{1.0,2.0,3.0,4.0,5.0,6.0,7.0,8.0,9.0,10.0},
			new double[]{1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0},100,1,UpgradePath.WEAPON),
	CRITICAL("Critical","[VAL]% chance to deal critical strikes.",new double[]{2.0,2.25,2.5,2.75,3.0,3.25,3.50,3.75,4.0,4.25},
			new double[]{1.0,1.0,0.9,0.9,0.8,0.8,0.7,0.7,0.6,0.5},100,1,UpgradePath.WEAPON),
	CRIT_DMG("Crit Damage","Critical Strikes deal [VAL]% damage.",new double[]{210.0,210.0,210.0,210.0,210.0,210.0,210.0,210.0,210.0,210.0},
			new double[]{5.0,5.0,5.0,5.0,5.0,5.0,5.0,5.0,5.0,5.0},100,1,UpgradePath.WEAPON),
	HIGHWINDER("Highwinder","While moving fast or sprinting, you deal [VAL] extra damage for every 1m of speed.",new double[]{0.18,0.225,0.27,0.315,0.36,0.405,0.45,0.495,0.54,0.61},
			new double[]{0.675,0.65,0.625,0.6,0.575,0.55,0.525,0.5,0.475,0.45},100,15,UpgradePath.WEAPON),
	
	//Bow Abilities
	MARKSMAN("Marksman","Increases headshot hitbox size by [VAL]% .",new double[]{10.0,15,20,25,30,35,40,45,50,55},
			new double[]{1.0,0.95,0.9,0.85,0.8,0.75,0.7,0.65,0.6,0.55},10,15,UpgradePath.BOW),
	SIEGESTANCE("Siege Stance",ChatColor.GRAY+"[Unimplemented] Activate by Sneaking for three seconds. Sneak again to de-activate.\n\n"
			+ "Applies Slowness V and Resistance VI. While in Siege Stance you fire clusters of 7 arrows per shot. Each arrow deals [VAL] damage.",new double[]{3.0,3.0,3.0,3.0,3.0,3.0,3.0,3.0,3.0,3.0},
			new double[]{1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0},100,20,UpgradePath.BOW),
	ARROWSHOWER("Arrow Shower",ChatColor.GRAY+"[Unimplemented] Shift-Left Click to activate. Applies Slowness X for three seconds while firing arrows into the sky and onto enemies in a large area in front of you. Each arrow deals [VAL] damage.",new double[]{0.7,0.7,0.7,0.7,0.7,0.7,0.7,0.7,0.7,0.7},
			new double[]{0.4,0.4,0.4,0.4,0.4,0.4,0.4,0.4,0.4,0.4},100,40,UpgradePath.BOW),
	TARGETING("Targeting",ChatColor.GRAY+"[Unimplemented] Left-click a mob to target them. Fire arrows to release homing missiles at your target. Each missile explodes and deals [VAL] damage.",new double[]{10,10,10,10,10,10,10,10,10,10},
			new double[]{0.3,0.3,0.3,0.3,0.3,0.3,0.3,0.3,0.3,0.3},100,75,UpgradePath.BOW),
	ENDERTURRET("Ender Turret",ChatColor.GRAY+"[Unimplemented] Place Eyes of Ender in your hotbar to use as ammo. Each eye fired launches forward and upward before releasing a barrage of homing missiles that lock onto enemy targets. Each missile explodes and deals [VAL] damage.",new double[]{25,25,25,25,25,25,25,25,25,25},
			new double[]{0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5},100,100,UpgradePath.BOW),
	
	//Armor abilities
	DAMAGE_REDUCTION("Defense","Increases Base Damage reduction by [VAL]%",new double[]{1.0,1.5,2.0,2.5,3.0,3.5,4.0,4.5,5.0,5.5},
			new double[]{2.2,2.1,2.0,1.9,1.8,1.7,1.6,1.5,1.4,1.3},100,1,UpgradePath.ARMOR),
	HEALTH("Health","Increases Maximum Health by [VAL].",new double[]{0.35,0.70,1.05,1.40,1.70,2.0,2.3,2.6,2.9,3.2},
			new double[]{1.0,0.95,0.9,0.85,0.8,0.75,0.7,0.75,0.7,0.65},100,1,UpgradePath.ARMOR),
	HEALTH_REGEN("Regeneration","Regenerates an extra [VAL] health every 5 seconds.",new double[]{0.03125,0.0625,0.09375,0.125,0.15625,0.1875,0.29166666666666666666666666666667,0.33333333333333333333333333333333,0.375,0.41666666666666666666666666666667},
			new double[]{1.0,0.90,0.85,0.8,0.75,0.7,0.6,0.55,0.5,0.45},100,1,UpgradePath.ARMOR),
	STATUS_EFFECT_RESISTANCE("Resistance","When a debuff is applied, there is a [VAL]% chance to remove it.",new double[]{3,3.5,4,4.5,5,5.5,6,6.5,7,7.5},
			new double[]{4.0,3.85,3.70,3.55,3.40,3.25,3.10,2.95,2.80,2.65},100,1,UpgradePath.ARMOR),
	SHADOWWALKER("Shadow Walker","Increases your speed in dark areas. Damage Reduction increases by [VAL]% in dark areas. Dodge chance increases by [DODGEVAL]% in dark areas.",new double[]{5,5,5,5,5,5,5,5,5,5},
			new double[]{1.5,1.4,1.3,1.2,1.1,1.0,0.9,0.8,0.7,0.55},100,10,UpgradePath.ARMOR),
	/*SURVIVOR("Survivor",ChatColor.GRAY+"[Unimplemented] Taking fatal damage will not kill you and instead consume this ability, removes all debuffs, and restoring your health.",new double[]{-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0},
			new double[]{0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0},1,1000,UpgradePath.ARMOR),*/
	DODGE("Dodge",ChatColor.GRAY+"You have a [VAL]% chance to dodge incoming damage from any damage source.",new double[]{0.1,0.15,0.2,0.25,0.3,0.35,0.4,0.45,0.5,0.55},
			new double[]{1.0,1.0,1.0,0.8,0.7,0.6,0.5,0.4,0.3,0.2},100,40,UpgradePath.ARMOR),
	GRACEFULDODGE("Graceful Dodge","Whenever a dodge occurs, you will gain [VAL] seconds of invulnerability.",new double[]{0.5,0.6,0.7,0.8,0.9,1.0,1.1,1.2,1.3,1.5},
			new double[]{1.8,1.78,1.76,1.74,1.72,1.70,1.68,1.66,1.64,1.62},10,40,UpgradePath.ARMOR),
	
	//Sword abilities
	PROVOKE("Provoke","Your attacks provoke enemies for [VAL] seconds.",new double[]{3.0,4.0,5.0,6.0,7.0,8.0,9.0,10.0,11.0,12.0},
			new double[]{3.0,2.8,2.6,2.4,2.2,2.2,2.0,1.8,1.6,1.4},100,10,UpgradePath.SWORD),
	COMBO("Belligerent","[VAL]% more damage for each successive strike on a mob. Resets after 2 seconds of no combat.",new double[]{1.0,1.5,2.0,2.5,3.0,3.5,4.0,4.5,5.0,5.5},
			new double[]{1.0,0.95,0.9,0.85,0.8,0.75,0.7,0.65,0.6,0.55},100,40,UpgradePath.SWORD),
	
	//Pickaxe abilities
	SCAVENGE("Scavenge",ChatColor.GRAY+"[Unimplemented] Breaks off resources from armor. [VAL]% chance per hit.",new double[]{5,5,5,5,5,5,5,5,5,5},
			new double[]{1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0},100,1000,UpgradePath.PICKAXE),
	MINES("Land Mine",ChatColor.GRAY+"[Unimplemented]While in combat, throw your pickaxe to send land mines towards your enemies. On contact they deal [VAL] damage.",new double[]{5,5,5,5,5,5,5,5,5,5},
			new double[]{1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0},100,1000,UpgradePath.PICKAXE),

	//Shovel abilities
	SUPPRESS("Suppression",ChatColor.GRAY+"[Unimplemented] Suppresses a mob on hit for [VAL] seconds.\n\n"
			+ "Suppression prevents movement, attacking, and teleportation.",new double[]{0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1},
			new double[]{1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0},100,1000,UpgradePath.SHOVEL),
	ERUPTION("Eruption","Sneak while Left-clicking a mob to damage mobs for [VAL] damage and knock them up. The eruption also destroys the ground beneath you.",new double[]{6.0,7,8.0,9,10.0,11,12.0,13,14.0,15},
			new double[]{1.0,0.925,0.85,0.775,0.7,0.625,0.55,0.475,0.4,0.325},100,40,UpgradePath.SHOVEL),
	EARTHWAVE("Earth Wave","While in combat, destroy a block to send a wave of earth towards your enemies. Enemies standing inside of the waves take [VAL] damage every second.",new double[]{8,9,10,11,12,13,14,15,16,18},
			new double[]{2.4,2.2,2.0,1.9,1.8,1.7,1.6,1.5,1.4,1.2},100,100,UpgradePath.SHOVEL),
	
	//Axe abilities
	BREAKDOWN("Break Down",ChatColor.GRAY+"[Unimplemented] Breaks down armor on mobs. Each hit has a [VAL]% chance to remove a piece of armor from a mob.",new double[]{3,3,3,3,3,3,3,3,3,3},
			new double[]{1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0},100,1000,UpgradePath.AXE),
	BUTCHERY("Butchery",ChatColor.GRAY+"[Unimplemented] Broken down armor have a [VAL]% chance to drop onto the ground.",new double[]{10,10,10,10,10,10,10,10,10,10},
			new double[]{0.8,0.8,0.8,0.8,0.8,0.8,0.8,0.8,0.8,0.8},100,1000,UpgradePath.AXE),
	
	//Scythe abilities
	AOE("Area of Effect","Deals damage to targets up to [VAL]m from the main target hit.",new double[]{0.4,0.45,0.5,0.55,0.6,0.65,0.70,0.75,0.80,0.85},
			new double[]{1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0},100,1,UpgradePath.SCYTHE),
	DEATHMARK("Death Mark","Applies a Death Mark stack to enemies hit. Death mark stacks last for 5 seconds, and refresh on each hit.\n\nMarks can be detonated at any time by right-clicking. Each death mark stack applied deals [VAL] true damage.",new double[]{0.1,0.2,0.3,0.4,0.5,0.6,0.7,0.8,0.9,1.0},
			new double[]{0.6,0.575,0.55,0.525,0.5,0.475,0.45,0.425,0.4,0.375},100,10,UpgradePath.SCYTHE),

	//General abilities
	AUTOREPAIR("Auto Repair","1% chance every second to repair [VAL] durability to the artifact item\n\nThe item must be sitting in your hotbar or must be equipped for this ability to work. This ability is less effective with no sunlight!",new double[]{3,3.5,4,4.5,5,5.5,6,6.5,7,7.5},
			new double[]{1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0},10,1,UpgradePath.ALL),
	GREED("Greed","Increases Drop rate by [VAL]% . Health is halved, health regeneration is halved, and damage reduction is halved. Consumes one level of Greed per level up.",new double[]{50,55,60,65,70,75,80,85,90,95},
			new double[]{2.0,2.0,2.0,2.0,2.0,2.0,2.0,2.0,2.0,2.0},100,10,UpgradePath.ALL),
	/*GROWTH("Growth",ChatColor.GRAY+"[Unimplemented] Increases artifact EXP gained by [VAL]% . Health is halved, health regeneration is halved, and damage reduction is halved. Consumes one level of Growth per level up.",new double[]{100,100,100,100,100,100,100,100,100,100},
			new double[]{2.0,2.0,2.0,2.0,2.0,2.0,2.0,2.0,2.0,2.0},100,1000,UpgradePath.ALL),*/
	REMOVE_CURSE("Remove Curse",ChatColor.GRAY+"[Unimplemented] Removes a level of a curse from the Artifact.",new double[]{-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0},
			new double[]{0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0},1,1000,UpgradePath.ALL),
	PRESERVATION("Preservation","Potential decays [VAL]% slower.",new double[]{1.0,1.5,2.0,2.5,3.0,3.5,4.0,4.5,5.0,7.0},
			new double[]{0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0},20,1,UpgradePath.ALL),
	EXP_MULT("Mega XP",ChatColor.GRAY+"[Unimplemented] Increases experience dropped from monsters by [VAL]% .",new double[]{5,5,5,5,5,5,5,5,5,5},
			new double[]{1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0},100,1000,UpgradePath.ALL),
	
	//Bad stuff
	REDUCEDMG("Weakness","[VAL]% Decrease in Base Damage.",new double[]{8,8,8,8,8,8,8,8,8,8},
			new double[]{1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0},100,3,UpgradePath.ALL),
	REDUCEDEF("Imperil","[VAL]% Decrease in Damage Reduction",new double[]{8,8,8,8,8,8,8,8,8,8},
			new double[]{1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0},100,5,UpgradePath.ALL),
	LIFE_REDUCTION("Health Cut","[VAL]% decrease in maximum health.",new double[]{30,30,30,30,30,30,30,30,30,30},
			new double[]{1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0},100,5,UpgradePath.ALL),
	LOWER_DEFENSE("Debilitate","[VAL]% decrease in damage reduction.",new double[]{30,30,30,30,30,30,30,30,30,30},
			new double[]{1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0},100,5,UpgradePath.ALL),
	TELEPORT("Teleport","[VAL]% chance to teleport the player to a random location on artifact experience gain.",new double[]{3,3,3,3,3,3,3,3,3,3},
			new double[]{1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0},100,10,UpgradePath.ALL),
	DRAINING("Draining","[VAL]% chance to remove a level of experience on artifact experience gain.",new double[]{1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0},
			new double[]{1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0},100,10,UpgradePath.ALL),
	NOREGEN("Weary","No health regenerates.",new double[]{1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0},
			new double[]{1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0},100,15,UpgradePath.ALL),
	STARVATION("Starvation","[VAL]% chance to cause [HUNGERVAL] seconds of Hunger on experience gain.",new double[]{5,5,5,5,5,5,5,5,5,5},
			new double[]{0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1},100,15,UpgradePath.ALL),
	BURN("Flammable","All burn damage deals x[VAL] damage.",new double[]{4.0,4.0,4.0,4.0,4.0,4.0,4.0,4.0,4.0,4.0},
			new double[]{1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0},100,25,UpgradePath.ALL),
	FROZEN("Frozen","Player will be inflicted with increasing levels of slowness and fatigue until finally frozen and killed.",new double[]{1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0},
			new double[]{1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0},1,45,UpgradePath.ALL),
	PETRIFICATION("Petrification","Player will be inflicted with increasing levels of slowness and fatigue until finally petrified and killed.",new double[]{1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0},
			new double[]{1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0},1,45,UpgradePath.ALL),
	
	;
	
	public static int LINE_SIZE=50;
	String name;
	String desc;
	double[] baseval;
	double[] decayval;
	int maxlv;
	int requirement;
	UpgradePath upgrade;
	
	ArtifactAbility(String name, String desc, double[] baseval, double[] decayval, int maxlv, int requirement, UpgradePath upgrade) {
		this.name=name;
		this.desc=desc;
		this.baseval=baseval;
		this.decayval=decayval;
		this.maxlv=maxlv;
		this.requirement=requirement;
		AwakenedArtifact.ability_map.put(this,this.name);
		AwakenedArtifact.name_map.put(this.name,this);
		this.upgrade=upgrade;
	}
	
	public String GetName() {
		return this.name;
	}
	
	public String GetDescription() {
		return this.desc;
	}
	
	public double GetBaseValue(int tier) {
		return this.baseval[tier-1];
	}
	
	public double GetDecayValue(int tier) {
		return this.decayval[tier-1];
	}
	
	public int GetMaxLevel() {
		return maxlv;
	}
	
	public int GetMinLevel() {
		return requirement;
	}
	
	
	
	public static double calculateValue(ArtifactAbility ability, int artifacttier, int abilitylevel) {
		double sum=0;
		if (artifacttier<=0) {artifacttier=1;}
		TwosideKeeper.log("Ability "+ability.GetName(), 4);
		for(int i=0;i<abilitylevel;i++){
			TwosideKeeper.log("Old Sum:"+sum+"::i:"+i, 5);
		    sum+=1d/(1d+(ability.GetDecayValue(artifacttier)*(double)i));
			TwosideKeeper.log("New Sum:"+sum+"::i:"+i, 5);
		}
		TwosideKeeper.log("Sum is "+sum, 5);
		TwosideKeeper.log("Base value is "+ability.GetBaseValue(artifacttier), 4);
		return sum*ability.GetBaseValue(artifacttier);
	}
	
	public static HashMap<ArtifactAbility,Integer> getEnchantments(ItemStack item) {
		HashMap<ArtifactAbility,Integer> abilities = new HashMap<ArtifactAbility,Integer>();
		if (GenericFunctions.isArtifactEquip(item)) {
			List<String> lore = item.getItemMeta().getLore(); 
			//From Element 7 and onwards, we know these are abilities added to the item. Retrieve them.
			for (int i=7;i<lore.size();i++) {
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
				TwosideKeeper.log(newstring,5);
				//This is the name of the enchantment. Now connect it with the name map we made.
				abilities.put(AwakenedArtifact.name_map.get(ChatColor.stripColor(newstring)),Integer.parseInt(splitstring[splitstring.length-1]));
			}
		}
		return abilities;
	}
	
	public static int getEnchantmentLevel(ArtifactAbility ability, ItemStack item) {
		//Get the enchantment level of a particular enchantment.
		HashMap enchants = getEnchantments(item);
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
			for (int i=0;i<lore.size();i++) {
				if (lore.get(i).contains(ability.GetName())) {
					//This is the line! Modify it.
					lore.set(i, ChatColor.YELLOW+" "+ability.GetName()+" "+(lv));
					break;
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
				case BASIC:{
					if (!item.getType().toString().contains("HELMET") &&
							!item.getType().toString().contains("CHESTPLATE") &&
							!item.getType().toString().contains("LEGGINGS") &&
							!item.getType().toString().contains("BOOTS")) {
						//This is a valid basic piece.
						return true;
					}
				}break;
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
				AwakenedArtifact.addAP(item, 1);
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
			if (AwakenedArtifact.getAP(item)>0) {
				if (ability.GetMaxLevel()>level && ability.GetMinLevel()<=AwakenedArtifact.getLV(item)) {
					//This is allowed. Proceed.
					item = applyEnchantment(ability,level+1,item);
					AwakenedArtifact.addAP(item, -1);
					p.sendMessage(ChatColor.AQUA+"Successfully applied "+ChatColor.BLUE+ability.GetName()+" "+(level+1)+ChatColor.AQUA+" to your artifact!");
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
						p.spigot().sendMessage(ArtifactAbility.GenerateMenu(ArtifactItemType.getArtifactItemTypeFromItemStack(p.getInventory().getItem(GenericFunctions.CalculateSlot(item,p))).getUpgradePath(), TwosideKeeper.CalculateWeaponDamage(p,null), item,GenericFunctions.CalculateSlot(item,p)));
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
	
	public static TextComponent DisplayAbility(ArtifactAbility ability, double playerdmgval, ItemStack targetitem, int slot) {
		boolean unlocked=true;
		String lockedreason = "";
		if (AwakenedArtifact.getLV(targetitem)<ability.GetMinLevel() || getEnchantmentLevel(ability,targetitem)>=ability.GetMaxLevel()) {
			unlocked=false;
			if (AwakenedArtifact.getLV(targetitem)<ability.GetMinLevel()) {
				lockedreason=ChatColor.GRAY+""+ChatColor.ITALIC+"Your Artifact needs to reach Level "+ability.GetMinLevel()+" to obtain this ability.";
			} else if (getEnchantmentLevel(ability,targetitem)>=ability.GetMaxLevel()) {
				lockedreason=ChatColor.GRAY+""+ChatColor.ITALIC+"Your Artifact has reached the maximum level for this ability!";
			}
		}
		int enchantlevel=0;
		if (containsEnchantment(ability,targetitem)) {
			enchantlevel=getEnchantmentLevel(ability,targetitem);
		}
		String displaystring = "";
		if (enchantlevel>0) {
			displaystring = displayDescriptionUpgrade(ability,targetitem.getEnchantmentLevel(Enchantment.LUCK),enchantlevel,enchantlevel+1,playerdmgval);
		} else {
			displaystring = displayDescription(ability,targetitem.getEnchantmentLevel(Enchantment.LUCK),enchantlevel+1,playerdmgval);
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
	
	public static TextComponent GenerateMenu(UpgradePath path, double playerdmgval, ItemStack targetitem) {
		return GenerateMenu(path,playerdmgval,targetitem,0);
	}
	
	public static TextComponent GenerateMenu(UpgradePath path, double playerdmgval, ItemStack targetitem, int slot) {
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
			text=DisplayAbility(DAMAGE,playerdmgval,targetitem,slot);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
			text=DisplayAbility(ARMOR_PEN,playerdmgval,targetitem,slot);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
			text=DisplayAbility(EXECUTION,playerdmgval,targetitem,slot);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
			if (path!=UpgradePath.BASIC) {
				text=DisplayAbility(LIFESTEAL,playerdmgval,targetitem,slot);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
				text=DisplayAbility(CRITICAL,playerdmgval,targetitem,slot);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
				text=DisplayAbility(CRIT_DMG,playerdmgval,targetitem,slot);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
				text=DisplayAbility(HIGHWINDER,playerdmgval,targetitem,slot);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
				if (path==UpgradePath.SWORD) {
					text=DisplayAbility(PROVOKE,playerdmgval,targetitem,slot);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
					text=DisplayAbility(COMBO,playerdmgval,targetitem,slot);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
				} else
				if (path==UpgradePath.AXE) {
					text=DisplayAbility(BREAKDOWN,playerdmgval,targetitem,slot);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
					text=DisplayAbility(BUTCHERY,playerdmgval,targetitem,slot);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
				} else
				if (path==UpgradePath.FISHING_ROD) {
				} else
				if (path==UpgradePath.BOW) {
					text=DisplayAbility(MARKSMAN,playerdmgval,targetitem,slot);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
					text=DisplayAbility(SIEGESTANCE,playerdmgval,targetitem,slot);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
					text=DisplayAbility(ARROWSHOWER,playerdmgval,targetitem,slot);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
					text=DisplayAbility(TARGETING,playerdmgval,targetitem,slot);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
					text=DisplayAbility(ENDERTURRET,playerdmgval,targetitem,slot);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
				} else
				if (path==UpgradePath.SCYTHE) {
					text=DisplayAbility(AOE,playerdmgval,targetitem,slot);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
					text=DisplayAbility(DEATHMARK,playerdmgval,targetitem,slot);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
				}
			}
		} else
		if (path==UpgradePath.ARMOR //Armor category.
		) {
			text=DisplayAbility(DAMAGE_REDUCTION,playerdmgval,targetitem,slot);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
			text=DisplayAbility(HEALTH,playerdmgval,targetitem,slot);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
			text=DisplayAbility(HEALTH_REGEN,playerdmgval,targetitem,slot);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
			text=DisplayAbility(STATUS_EFFECT_RESISTANCE,playerdmgval,targetitem,slot);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
			text=DisplayAbility(SHADOWWALKER,playerdmgval,targetitem,slot);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
			//text=DisplayAbility(SURVIVOR,playerdmgval,targetitem,slot);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
			text=DisplayAbility(DODGE,playerdmgval,targetitem,slot);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
			text=DisplayAbility(GRACEFULDODGE,playerdmgval,targetitem,slot);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
		} else
		if (path==UpgradePath.TOOL || //Tool category.
				path==UpgradePath.SHOVEL ||
				path==UpgradePath.PICKAXE
			) {
			text=DisplayAbility(DAMAGE,playerdmgval,targetitem,slot);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
			text=DisplayAbility(ARMOR_PEN,playerdmgval,targetitem,slot);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
			text=DisplayAbility(EXECUTION,playerdmgval,targetitem,slot);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
			if (path==UpgradePath.SHOVEL) {
				text=DisplayAbility(SUPPRESS,playerdmgval,targetitem,slot);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
				text=DisplayAbility(ERUPTION,playerdmgval,targetitem,slot);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
				text=DisplayAbility(EARTHWAVE,playerdmgval,targetitem,slot);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
			} else
			if (path==UpgradePath.PICKAXE) {
				text=DisplayAbility(SCAVENGE,playerdmgval,targetitem,slot);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
			}
		}

		text=DisplayAbility(GREED,playerdmgval,targetitem,slot);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
		text=DisplayAbility(AUTOREPAIR,playerdmgval,targetitem,slot);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
		text=DisplayAbility(REMOVE_CURSE,playerdmgval,targetitem,slot);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
		text=DisplayAbility(PRESERVATION,playerdmgval,targetitem,slot);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
		text=DisplayAbility(EXP_MULT,playerdmgval,targetitem,slot);msg1.addExtra(text);if(!text.getText().equalsIgnoreCase("")){++i;}if(i%4==0){msg1.addExtra("\n");}
		
		return msg1;
	}
	
	public static String displayDescription(ArtifactAbility ability, int tier, int abilitylv, double playerdmgval) { //Level to display information for.		
		String msg = ability.GetDescription();
		DecimalFormat df = new DecimalFormat("0.00");
		msg=msg.replace("[VAL]", ChatColor.BLUE+df.format(calculateValue(ability,tier,abilitylv))+ChatColor.RESET);
		msg=msg.replace("[PENDMG]", ChatColor.BLUE+df.format(calculateValue(ability,tier,abilitylv)/100*playerdmgval)+ChatColor.RESET); //Based on multiplying [VAL] by the base damage value.
		msg=msg.replace("[HUNGERVAL]", ChatColor.BLUE+df.format(10*abilitylv)+ChatColor.RESET);
		msg=msg.replace("[FATALDMG]", ChatColor.BLUE+df.format(120*abilitylv)+ChatColor.RESET);
		msg=msg.replace("[REPAIRCHANCE]", ChatColor.BLUE+df.format(tier/3)+ChatColor.RESET);
		msg=msg.replace("[DODGEVAL]", ChatColor.BLUE+df.format(tier)+ChatColor.RESET);
		return msg;
	}
	public static String displayDescriptionUpgrade(ArtifactAbility ability, int tier, int fromlv, int tolv, double playerdmgval) { //Level to display information for.		
		String msg = ability.GetDescription();
		DecimalFormat df = new DecimalFormat("0.00");
		msg=msg.replace("[VAL]", DisplayChangedValue(df.format(calculateValue(ability,tier,fromlv)),df.format(calculateValue(ability,tier,tolv))));
		msg=msg.replace("[PENDMG]", DisplayChangedValue(df.format(calculateValue(ability,tier,fromlv)/100*playerdmgval),df.format(calculateValue(ability,tier,tolv)/100*playerdmgval))); //Based on multiplying [VAL] by the base damage value.
		msg=msg.replace("[HUNGERVAL]", DisplayBadChangedValue(df.format(10*fromlv),df.format(10*tolv)));
		msg=msg.replace("[FATALDMG]", DisplayChangedValue(df.format(120-fromlv),df.format(120-tolv)));
		msg=msg.replace("[REPAIRCHANCE]", DisplayChangedValue(df.format(tier),df.format(tier/3)));
		msg=msg.replace("[DODGEVAL]", DisplayChangedValue(df.format(tier),df.format(tier)));
		return msg;
	}
	
	static String DisplayChangedValue(String val1,String val2) {
		return ChatColor.DARK_GRAY+""+ChatColor.STRIKETHROUGH+val1+ChatColor.RESET+ChatColor.GREEN+val2+ChatColor.DARK_GREEN+ChatColor.BOLD+"^"+ChatColor.RESET;
	}
	static String DisplayBadChangedValue(String val1,String val2) {
		return ChatColor.DARK_RED+""+ChatColor.STRIKETHROUGH+val1+ChatColor.RESET+ChatColor.RED+val2+ChatColor.DARK_RED+ChatColor.BOLD+"v"+ChatColor.RESET;
	}
	
}
