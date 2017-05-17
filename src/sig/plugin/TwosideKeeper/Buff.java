package sig.plugin.TwosideKeeper;

import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import sig.plugin.TwosideKeeper.HelperStructures.Utils.TextUtils;

public class Buff {
	private String displayName;
	private long expireTime;
	private int level;
	private Color col;
	private String icon;
	private boolean isGoodBuff; //If false, it's a debuff.
	private boolean permanentBuff; //Whether or not this buff/debuff cannot be removed by normal means.
	private boolean displayTimer;
	
	/**
	 * Creates a new Buff structure.
	 * @param displayName The name that will show up in the action bar for players if they have this buff.
	 * @param duration The amount of time in ticks the buff will remain active.
	 * @param amplifier The amplifier/level/stack amount of this buff.
	 * @param buffcolor The color of the particles this buff creates.
	 * @param icon An icon that appears for the buff in the action bar and status bar for monster name tags. This typically includes a chat color code as well to distinguish this buff's color.
	 * @param isGoodBuff Whether or not this is a good buff. Debuffs should have this set to false.
	 */
	public Buff(String displayName, long duration, int amplifier, Color buffcolor, String icon, boolean isGoodBuff) {
		this.displayName=displayName;
		this.expireTime=TwosideKeeper.getServerTickTime()+duration;
		this.level=amplifier;
		this.col=buffcolor;
		this.icon=icon;
		this.isGoodBuff=isGoodBuff;
		this.permanentBuff=false;
		this.displayTimer=false;
	}

	/**
	 * Creates a new Buff structure.
	 * @param displayName The name that will show up in the action bar for players if they have this buff.
	 * @param duration The amount of time in ticks the buff will remain active.
	 * @param amplifier The amplifier/level/stack amount of this buff.
	 * @param buffcolor The color of the particles this buff creates.
	 * @param icon An icon that appears for the buff in the action bar and status bar for monster name tags. This typically includes a chat color code as well to distinguish this buff's color.
	 * @param isGoodBuff Whether or not this is a good buff. Debuffs should have this set to false.
	 * @param permanentBuff Whether or not this buff cannot be removed. When set to true, the method buffCanBeRemoved() returns false, notifying the programmers that this buff should not be removed. This make the use of removeBuff() for this buff do absolutely nothing.
	 */
	public Buff(String displayName, long duration, int amplifier, Color buffcolor, String icon, boolean isGoodBuff, boolean permanentBuff) {
		this.displayName=displayName;
		this.expireTime=TwosideKeeper.getServerTickTime()+duration;
		this.level=amplifier;
		this.col=buffcolor;
		this.icon=icon;
		this.isGoodBuff=isGoodBuff;
		this.permanentBuff=permanentBuff;
		this.displayTimer=false;
	}
	
	public static boolean hasBuffInHashMap(LivingEntity l, String name) {
		if (l instanceof Player) {
			Player p = (Player)l;
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			return pd.buffs.containsKey(name);
		} else {
			LivingEntityStructure les = LivingEntityStructure.GetLivingEntityStructure(l);
			return les.buffs.containsKey(name);
		}
	}

	public static boolean hasBuff(LivingEntity l, String name) {
		if (l instanceof Player) {
			Player p = (Player)l;
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			if (pd.buffs.containsKey(name)) {
				Buff b = pd.buffs.get(name);
				return hasBuffExpired(b);
			} else {
				return false;
			}
		} else {
			LivingEntityStructure les = LivingEntityStructure.GetLivingEntityStructure(l);
			if (les.buffs.containsKey(name)) {
				Buff b = les.buffs.get(name);
				return hasBuffExpired(b);
			} else {
				return false;
			}
		}
	}
	
	public static void outputBuffs(LivingEntity l) {
		if (l instanceof Player) {
			Player p = (Player)l;
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			TwosideKeeper.log(TextUtils.outputHashmap(pd.buffs),0);
		} else {
			LivingEntityStructure les = LivingEntityStructure.GetLivingEntityStructure(l);
			TwosideKeeper.log(TextUtils.outputHashmap(les.buffs),0);
		}
	}
	
	public static HashMap<String,Buff>getBuffData(LivingEntity l) {
		if (l instanceof Player) {
			PlayerStructure pd = PlayerStructure.GetPlayerStructure((Player)l);
			return pd.buffs;
		} else {
			LivingEntityStructure les = LivingEntityStructure.GetLivingEntityStructure(l);
			return les.buffs;
		}
	}
	
	/**
	 * Returns <b>null</b> if no buff found! Use <b>hasBuff()</b> to verify they have
	 * a buff beforehand.
	 */
	public static Buff getBuff(LivingEntity l, String name) {
		if (hasBuff(l,name)) {
			if (l instanceof Player) {
				Player p = (Player)l;
				PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
				if (pd.buffs.containsKey(name)) {
					return pd.buffs.get(name);
				} else {
					return null;
				}
			} else {
				LivingEntityStructure les = LivingEntityStructure.GetLivingEntityStructure(l);
				if (les.buffs.containsKey(name)) {
					Buff b = les.buffs.get(name);
					return b;
				} else {
					return null;
				}
			}
		} else {
			return null;
		}
	}
	
	public void setDisplayTimerAlways(boolean displayTimerAlways) {
		this.displayTimer=displayTimerAlways;
	}
	
	public boolean getDisplayTimerAlways() {
		return displayTimer;
	}
	
	public static void addBuff(LivingEntity l, String name, Buff buff) {
		addBuff(l,name,buff,false);
	}
	/**
	 * Attempts to add a buff to the target. This will not necessarily add the buff if the amplifier
	 * is weaker than what is currently applied, or the amplifier is the same but the duration is less.
	 * This follows the same rules established by all other buff mechanics added previously to the server.
	 */
	public static void addBuff(LivingEntity l, String name, Buff buff, boolean stacking) {
		if (l instanceof Player) {
			Player p = (Player)l;
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			int oldlv = 0;
			long oldduration = 0;
			if (hasBuff(p,name)) {
				oldlv = pd.buffs.get(name).getAmplifier();
				oldduration = pd.buffs.get(name).getRemainingBuffTime();
				if (stacking) {
					buff.setStacks(buff.getAmplifier()+oldlv);
					pd.buffs.put(name, buff); 
					return;
				}
			} else {
				pd.buffs.put(name, buff);
				return;
			}
			if (buff.getAmplifier()>=oldlv) {
				if (buff.getAmplifier()==oldlv) { //Check if duration is longer or same.
					if (buff.getRemainingBuffTime()>=oldduration) {
						pd.buffs.put(name, buff); 
					}
				} else {
					pd.buffs.put(name, buff); //If Buff level is greater than old level.
				}
			}
		} else {
			LivingEntityStructure les = LivingEntityStructure.GetLivingEntityStructure(l);
			int oldlv = 0;
			long oldduration = 0;
			if (hasBuff(l,name)) {
				oldlv = les.buffs.get(name).getAmplifier();
				oldduration = les.buffs.get(name).getRemainingBuffTime();
				if (stacking) {
					buff.setStacks(buff.getAmplifier()+oldlv);
					les.buffs.put(name, buff); 
					return;
				}
			} else {
				les.buffs.put(name, buff);
				return;
			}
			if (buff.getAmplifier()>=oldlv) {
				if (buff.getAmplifier()==oldlv) { //Check if duration is longer or same.
					if (buff.getRemainingBuffTime()>=oldduration) {
						les.buffs.put(name, buff); 
					}
				} else {
					les.buffs.put(name, buff); //If Buff level is greater than old level.
				}
			}
		}
	}
	public static void removeBuff(LivingEntity l, String name) {
		if (l instanceof Player) {
			Player p = (Player)l;
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			Buff b = pd.buffs.remove(name);
			if (b!=null && !b.buffCanBeRemoved()) {
				pd.buffs.put(name, b);
			}
		} else {
			LivingEntityStructure les = LivingEntityStructure.GetLivingEntityStructure(l);
			Buff b = les.buffs.remove(name);
			if (b!=null && !b.buffCanBeRemoved()) {
				les.buffs.put(name, b);
			}
		}
	}
	
	/**
	 * Increases the level relative to the amt provided.
	 */
	public void increaseStacks(int amt) {
		level+=amt;
	}
	/**
	 * Decreases the level relative to the amt provided. Can go negative.
	 */
	public void decreaseStacks(int amt) {
		level-=amt;
	}
	/**
	 * Sets the level of the buff directly to amt.
	 */
	public void setStacks(int amt) {
		level=amt;
	}
	/**
	 * Increases the duration of the buff by <b>duration</b> number of ticks.
	 */
	public void increaseDuration(int duration) {
		expireTime+=duration;
	}
	/**
	 * Decreases duration of the buff by <b>duration</b> number of ticks.
	 */
	public void decreaseDuration(int duration) {
		expireTime-=duration;
	}
	/**
	 * Sets the duration of the buff to <b>duration</b> ticks.
	 */
	public void setDuration(int duration) {
		refreshDuration(duration);
	}
	/**
	 * Refreshes the buff's duration so time starts at the original duration again.
	 */
	public void refreshDuration(int duration) {
		expireTime=TwosideKeeper.getServerTickTime()+duration;
	}
	/**
	 * Whether or not this is considered a good buff (true) or a bad buff (false)
	 */
	public boolean isGoodBuff() {
		return isGoodBuff;
	}
	/**
	 * Whether or not this is considered a bad buff (true) or a good buff (false)
	 */
	public boolean isDebuff() {
		return !isGoodBuff;
	}
	/**
	 * Whether or not this buff has ran out of time.
	 */
	public static boolean hasBuffExpired(Buff b) {
		if (b.expireTime<TwosideKeeper.getServerTickTime()) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Gets the name that shows up in the action bar for the player.
	 */
	public String getDisplayName() {
		return displayName;
	}
	
	/**
	 * Gets the expiration time of the buff in ticks, to be compared with TwosideKeeper.getServerTickTime().
	 */
	public long getExpireTime() {
		return expireTime;
	}
	
	/**
	 * Gets the level/amplifier/stack amount of this buff.
	 */
	public int getAmplifier() {
		return level;
	}
	
	/**
	 * Gets the swirly particle colors that appear when this buff is applied.
	 */
	public Color getBuffParticleColor() {
		return col;
	}
	
	/**
	 * Gets the remaining amount of time this buff is still active on this entity. Returns 0 if it has already expired.
	 */
	public long getRemainingBuffTime() {
		return Math.max(expireTime-TwosideKeeper.getServerTickTime(),0);
	}
	
	/**
	 * Returns a print-friendly version of this structure.
	 */
	public String toString() {
		return "Buff(Name="+displayName+",Time="+expireTime+",Level="+level+",Color="+col+",Icon="+getBuffIcon()+")";
	}
	
	/**
	 * Returns the string that consistss of the buff icon for this buff. Usually includes a chat color code.
	 */
	public String getBuffIcon() {
		return icon;
	}
	
	/**
	 * Whether or not this buff can be removed.
	 */
	public boolean buffCanBeRemoved() {
		return !permanentBuff;
	}

	/**
	 * Whether or not the specified buff can be removed.
	 */
	public static boolean buffCanBeRemoved(Buff b) {
		return !b.permanentBuff;
	}
}
