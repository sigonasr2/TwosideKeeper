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
	
	public Buff(String displayName, long duration, int amplifier, Color buffcolor, String icon, boolean isGoodBuff) {
		this.displayName=displayName;
		this.expireTime=TwosideKeeper.getServerTickTime()+duration;
		this.level=amplifier;
		this.col=buffcolor;
		this.icon=icon;
		this.isGoodBuff=isGoodBuff;
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
	
	public static void addBuff(LivingEntity l, String name, Buff buff) {
		addBuff(l,name,buff,false);
	}
	
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
			pd.buffs.remove(name);
		} else {
			LivingEntityStructure les = LivingEntityStructure.GetLivingEntityStructure(l);
			les.buffs.remove(name);
		}
	}
	
	public void increaseStacks(int amt) {
		level+=amt;
	}
	public void decreaseStacks(int amt) {
		level-=amt;
	}
	public void setStacks(int amt) {
		level=amt;
	}
	public void increaseDuration(int duration) {
		expireTime+=duration;
	}
	public void decreaseDuration(int duration) {
		expireTime-=duration;
	}
	public void setDuration(int duration) {
		refreshDuration(duration);
	}
	public void refreshDuration(int duration) {
		expireTime=TwosideKeeper.getServerTickTime()+duration;
	}
	
	public boolean isGoodBuff() {
		return isGoodBuff;
	}
	public boolean isDebuff() {
		return !isGoodBuff;
	}

	private static boolean hasBuffExpired(Buff b) {
		if (b.expireTime<TwosideKeeper.getServerTickTime()) {
			return false;
		} else {
			return true;
		}
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public long getExpireTime() {
		return expireTime;
	}
	
	public int getAmplifier() {
		return level;
	}
	
	public Color getBuffParticleColor() {
		return col;
	}
	
	public long getRemainingBuffTime() {
		return Math.max(expireTime-TwosideKeeper.getServerTickTime(),0);
	}
	
	public String toString() {
		return "Buff(Name="+displayName+",Time="+expireTime+",Level="+level+",Color="+col+",Icon="+getBuffIcon()+")";
	}
	
	public String getBuffIcon() {
		return icon;
	}

	public static boolean buffCanBeRemoved() {
		//For now, there are no buffs that cannot be removed.
		return true;
	}
}
