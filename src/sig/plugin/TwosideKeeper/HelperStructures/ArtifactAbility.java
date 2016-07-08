package sig.plugin.TwosideKeeper.HelperStructures;

import java.text.DecimalFormat;

import org.bukkit.ChatColor;

public enum ArtifactAbility {
	//Enum Structure:
	// "Friendly Name", "Description", base value (per point) (-1 means it's a TEMPORARY ability.), decay value (per point), max level, level requirement (The min level required to get this perk)
	//Temporary abilities: Work for 1 level and wear off afterward.
	
	//Weapon Abilities
	DAMAGE("Strike","Improves Base Damage by [VAL]",1.0,1.0,100),
	ARMOR_PEN("Piercing","[VAL]% of your damage is ignored by resistances. ([PENDMG] damage)",1.0,1.0,100,1),
	EXECUTION("Execute","Deals [VAL] extra damage for every 20% of target's missing health.",2.0,1.0,100,1),
	LIFESTEAL("Lifesteal","Heals [VAL]% of the damage dealt to targets back to your health pool.",1.0,1.0,100,1),
	
	//Armor abilities
	DAMAGE_REDUCTION("Defense","Increases Base Damage reduction by [VAL]%",1.0,2.0,100,1),
	HEALTH("Health","Increases Maximum Health by [VAL].",1.0,1.0,100,1),
	HEALTH_REGEN("Regeneration","Regenerates an extra [VAL] health every 5 seconds.",0.5,1.0,100,1),
	STATUS_EFFECT_RESISTANCE("Resistance","When a debuff is applied",0.5,1.0,100,1),
	SURVIVOR("Survivor","Taking fatal damage will not kill you and instead consume this ability, removes all debuffs, and leaving you with 1 HP.",-1,0,1,10),
	
	;
	
	String name;
	String desc;
	double baseval;
	double decayval;
	int maxlv;
	
	ArtifactAbility(String name, String desc, double baseval, double decayval, int maxlv) {
		this.name=name;
		this.desc=desc;
		this.baseval=baseval;
		this.decayval=decayval;
		this.maxlv=maxlv;
	}
	
	public double calculateValue(int lv) {
		double sum=0;
		for(int i=0;i<lv;i++){
		    sum+=1/(1+this.decayval*i);
		}
		return sum*this.baseval;
	}
	
	public String displayDescription(int lv, double playerdmgval) { //Level to display information for.		
		String msg = this.desc;
		DecimalFormat df = new DecimalFormat("0.00");
		msg.replace("[VAL]", df.format(calculateValue(lv)));
		msg.replace("[PENDMG]", df.format(calculateValue(lv)/100*playerdmgval)); //Based on multiplying [VAL] by the base damage value.
		return msg;
	}
	public String displayDescriptionUpgrade(int fromlv, int tolv, double playerdmgval) { //Level to display information for.		
		String msg = this.desc;
		DecimalFormat df = new DecimalFormat("0.00");
		msg.replace("[VAL]", DisplayChangedValue(df.format(calculateValue(fromlv)),df.format(calculateValue(tolv))));
		msg.replace("[PENDMG]", DisplayChangedValue(df.format(calculateValue(fromlv)/100*playerdmgval),df.format(calculateValue(tolv)/100*playerdmgval))); //Based on multiplying [VAL] by the base damage value.
		return msg;
	}
	
	String DisplayChangedValue(String val1,String val2) {
		return ChatColor.DARK_GRAY+""+ChatColor.STRIKETHROUGH+val1+ChatColor.RESET+ChatColor.GREEN+val2+ChatColor.DARK_GREEN+ChatColor.BOLD+"^"+ChatColor.RESET;
	}
	
}
