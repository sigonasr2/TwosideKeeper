package sig.plugin.TwosideKeeper;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import sig.plugin.TwosideKeeper.HelperStructures.ItemSet;
import sig.plugin.TwosideKeeper.HelperStructures.WorldShop;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;

public class ActionBarBuffUpdater{
	
	/*
	 * LIST OF BUFFS:
	 * basename / icon string
	 * 
	 * Poison ChatColor.YELLOW+"☣"
	 * DeathMark ChatColor.DARK_RED+"☠"
	 */

	public static String getActionBarPrefix(LivingEntity p) {
		StringBuilder actionbardisplay = new StringBuilder("");
		for (PotionEffect pe : p.getActivePotionEffects()) {
			if (pe.getAmplifier()>3) {
				actionbardisplay.append(ParseEffect(p,pe));
			}
		}
		actionbardisplay.append(AddAdditionalEffects(p));
		//TwosideKeeper.log(actionbardisplay.toString(), 0);
		if (actionbardisplay.toString().contains("  ")) {
			return actionbardisplay.toString().substring(0, actionbardisplay.toString().lastIndexOf("  "));
		} else {
			return actionbardisplay.toString();
		}
	}

	private static String AddAdditionalEffects(LivingEntity p) {
		StringBuilder effectString=new StringBuilder("");
		if (p instanceof Player) {
			PlayerStructure pd = PlayerStructure.GetPlayerStructure((Player)p);
			if (p.getFireTicks()>=20) {
				effectString.append(ChatColor.GOLD);
				effectString.append("⚠");
				effectString.append(AppendAmplifier((p.getFireTicks()/20)-1,false));
				effectString.append("  ");
			}
			if (pd.lifestealstacks>4) {
				effectString.append(ChatColor.AQUA);
				effectString.append("❣");
				effectString.append(AppendAmplifier(pd.lifestealstacks-1));
				effectString.append("  ");
			}
			if (pd.weaponcharges>4) {
				effectString.append(ChatColor.DARK_AQUA);
				effectString.append("☤");
				effectString.append(AppendAmplifier(pd.weaponcharges-1));
				effectString.append("  ");
			}
			if (pd.damagepool>4) {
				effectString.append(ChatColor.DARK_PURPLE);
				effectString.append("♥");
				effectString.append(AppendAmplifier((int)(pd.damagepool-1)));
				effectString.append("  ");
			}
			if (pd.lastvendettastack+200>TwosideKeeper.getServerTickTime() &&
					ItemSet.hasFullSet((Player)p, ItemSet.SONGSTEEL)) {
				effectString.append(ChatColor.GRAY);
				effectString.append("☉");
				effectString.append(AppendAmplifier(((int)((pd.lastvendettastack+200)-TwosideKeeper.getServerTickTime())/20)-1,false));
				effectString.append("  ");
			}
			if (pd.swiftaegisamt>4) {
				effectString.append(ChatColor.YELLOW);
				effectString.append("❈");
				effectString.append(AppendAmplifier((int)(GenericFunctions.getSwiftAegisAmt((Player)p)-1)));
				effectString.append("  ");
			}
			if (pd.regenpool>0) {
				effectString.append(ChatColor.BLUE);
				effectString.append(ChatColor.BOLD);
				effectString.append("✙");
				effectString.append(AppendAmplifier((int)(pd.regenpool)));
				effectString.append("  ");
			}
		}
		HashMap<String,Buff> buffMap = Buff.getBuffData(p);
		for (String s : buffMap.keySet()) {
			Buff b = buffMap.get(s);
			
			if (b.getRemainingBuffTime()>0) {
				effectString.append(b.getBuffIcon());
				effectString.append(" ");
				if (p instanceof Player) {
					effectString.append(b.getDisplayName());
				}
				effectString.append(ConvertBuffAmplifierToIcon(b.getAmplifier()));
				effectString.append(" ");
				if (b.getRemainingBuffTime()<=200) {
					effectString.append(ConvertBuffTimeToIcon(b.getRemainingBuffTime()));
				}
				effectString.append("  ");
			}
		}
		
		if (effectString.length()>0) {
			return effectString.toString()+ChatColor.RESET;
		} else {
			return "";
		}
	}

	private static String ConvertBuffAmplifierToIcon(int amplifier) {
		if (amplifier==1) {
			return "Ⅰ";
		} else
		if (amplifier==2) {
			return "Ⅱ";
		} else
		if (amplifier==3) {
			return "Ⅲ";
		} else
		if (amplifier==4) {
			return "Ⅳ";
		} else
		if (amplifier==5) {
			return "Ⅴ";
		} else
		if (amplifier==6) {
			return "Ⅵ";
		} else
		if (amplifier==7) {
			return "Ⅶ";
		} else
		if (amplifier==8) {
			return "Ⅷ";
		} else
		if (amplifier==9) {
			return "Ⅸ";
		} else
		if (amplifier==10) {
			return "Ⅹ";
		} else
		if (amplifier==11) {
			return "Ⅺ";
		} else
		if (amplifier==12) {
			return "Ⅻ";
		} else {
			if (amplifier!=0) {
				return Integer.toString(amplifier);
			} else {
				return "";
			}
		}
	}

	private static String ConvertBuffTimeToIcon(long remainingBuffTime) {
		if (remainingBuffTime>180) {
			return "➓";
		} else
		if (remainingBuffTime>160) {
			return "➒";
		} else
		if (remainingBuffTime>140) {
			return "➑";
		} else
		if (remainingBuffTime>120) {
			return "➐";
		} else
		if (remainingBuffTime>100) {
			return "➏";
		} else
		if (remainingBuffTime>80) {
			return "➎";
		} else
		if (remainingBuffTime>60) {
			return "➍";
		} else
		if (remainingBuffTime>40) {
			return "➌";
		} else
		if (remainingBuffTime>20) {
			return "➋";
		} else
		{
			return "➊";
		}
	}

	private static String ParseEffect(LivingEntity p, PotionEffect pe) {
		StringBuilder effectString=new StringBuilder("");
		PotionEffectType pet = pe.getType();
		if (pet.equals(PotionEffectType.INCREASE_DAMAGE)) {
			effectString.append(ChatColor.GOLD);
			effectString.append("⚔");
		} else
		if (pet.equals(PotionEffectType.DAMAGE_RESISTANCE)) {
			effectString.append(ChatColor.BLUE);
			effectString.append("❈");
		} else
		if (pet.equals(PotionEffectType.REGENERATION)) {
			effectString.append(ChatColor.GREEN);
			effectString.append("✙");
		} else
		if (pet.equals(PotionEffectType.SPEED)) {
			effectString.append(ChatColor.WHITE);
			effectString.append("➠");
		} else
		/*if (pet.equals(PotionEffectType.POISON) || 
				(pet.equals(PotionEffectType.BLINDNESS) && (p instanceof LivingEntity && !(p instanceof Player)))) {
			effectString.append(ChatColor.YELLOW);
			effectString.append("☣");
		} else*/
		/*if ((pet.equals(PotionEffectType.UNLUCK) && (p instanceof LivingEntity && !(p instanceof Player)))) {
			effectString.append(ChatColor.DARK_RED);
			effectString.append("☠");
		} else*/
		if (pet.equals(PotionEffectType.SLOW)) {
			effectString.append(ChatColor.DARK_AQUA);
			effectString.append("♒");
		} else
		if (pet.equals(PotionEffectType.WEAKNESS) || pet.equals(PotionEffectType.SLOW_DIGGING)) {
			effectString.append(ChatColor.RED);
			effectString.append("✘");
		}
		if (effectString.length()>0) {
			effectString.append(AppendAmplifier(pe.getAmplifier()));
			effectString.append("  ");
		}
		if (effectString.length()>0) {
			return effectString.toString()+ChatColor.RESET;
		} else {
			return "";
		}
	}

	private static String AppendAmplifier(int amplifier) {
		return AppendAmplifier(amplifier,true);
	}
	
	private static String AppendAmplifier(int amplifier, boolean romanNumerals) {
		StringBuilder amp = new StringBuilder(" ");
		if (amplifier+1<=10 && romanNumerals) {
			amp.append(ChatColor.GRAY+""+WorldShop.toRomanNumeral(amplifier+1));
		} else {
			amp.append(ChatColor.GRAY+""+(amplifier+1));
		}
		return amp.toString();
	}

}
