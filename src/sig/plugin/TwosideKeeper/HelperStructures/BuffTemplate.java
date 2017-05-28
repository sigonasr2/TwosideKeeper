package sig.plugin.TwosideKeeper.HelperStructures;

import org.bukkit.ChatColor;
import org.bukkit.Color;

import sig.plugin.TwosideKeeper.Buff;

public enum BuffTemplate {
	POISON("Poison","Poison",Color.YELLOW,ChatColor.YELLOW+"☣",false),
	SHRAPNEL("SHRAPNEL","Shrapnel",Color.RED,ChatColor.RED+"❂",false),
	WINDCHARGES("WINDCHARGE","Wind",Color.GRAY,"๑",true),
	BLEEDING("BLEEDING","Bleed",Color.MAROON,ChatColor.DARK_RED+"☠",false),
	REGENERATION("REGENERATION","Regeneration",Color.GREEN,ChatColor.GREEN+""+ChatColor.BOLD+"✙",true),
	INFECTION("INFECTION","Infection",Color.GRAY,ChatColor.GRAY+"❧",false),
	CRIPPLE("CRIPPLE","Cripple",Color.WHITE,ChatColor.WHITE+"☹",false),
	DARKSUBMISSION("DARKSUBMISSION",ChatColor.GRAY+"Dark Submission"+ChatColor.RESET,Color.BLACK,ChatColor.BLACK+""+ChatColor.MAGIC+"☁"+ChatColor.RESET,false),
	CONFUSION("CONFUSION","Confusion",Color.PURPLE,ChatColor.DARK_PURPLE+"๑"+ChatColor.RESET,false),
	
	UNDYINGRAGE_COOLDOWN("COOLDOWN_UNDYING_RAGE","Undying Rage Cooldown",null,ChatColor.WHITE+"",true,true),
	UNSTOPPABLETEAM_COOLDOWN("Unstoppable Team Unavailable","Unstoppable Team Unavailable",null,ChatColor.WHITE+"",true,true),
	;
	
	String keyName;
	String displayName;
	Color col;
	String icon;
	boolean isGoodBuff;
	boolean permanentBuff;
	boolean displayTimer;
	
	public String getKeyName() {
		return keyName;
	}
	public String getDisplayName() {
		return displayName;
	}
	public Color getParticleColor() {
		return col;
	}
	public String getIcon() {
		return icon;
	}
	public boolean isGoodBuff() {
		return isGoodBuff;
	}
	public boolean isPermanentBuff() {
		return permanentBuff;
	}
	public boolean isDisplayTimer() {
		return displayTimer;
	}
	
	BuffTemplate(String keyName, String displayName, Color particleColor, String icon, boolean isGoodBuff) {
		this.keyName=keyName;
		this.displayName=displayName;
		this.col=particleColor;
		this.icon=icon;
		this.isGoodBuff=isGoodBuff;
		this.permanentBuff=false;
		this.displayTimer=false;
	}
	BuffTemplate(String keyName, String displayName, Color particleColor, String icon, boolean isGoodBuff, boolean isPermanentBuff) {
		this.keyName=keyName;
		this.displayName=displayName;
		this.col=particleColor;
		this.icon=icon;
		this.isGoodBuff=isGoodBuff;
		this.permanentBuff=isPermanentBuff;
		this.displayTimer=false;
	}
	BuffTemplate(String keyName, String displayName, Color particleColor, String icon, boolean isGoodBuff, boolean isPermanentBuff, boolean displayTimer) {
		this.keyName=keyName;
		this.displayName=displayName;
		this.col=particleColor;
		this.icon=icon;
		this.isGoodBuff=isGoodBuff;
		this.permanentBuff=isPermanentBuff;
		this.displayTimer=displayTimer;
	}
	//new Buff("Poison",20*20,Integer.parseInt(args[1]),Color.YELLOW,ChatColor.YELLOW+"☣",false)
}
