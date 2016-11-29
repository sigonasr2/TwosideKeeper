package sig.plugin.TwosideKeeper;

import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import sig.plugin.TwosideKeeper.HelperStructures.WorldShop;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;

public class ActionBarBuffUpdater{

	public static String getActionBarPrefix(LivingEntity p) {
		StringBuilder actionbardisplay = new StringBuilder("");
		for (PotionEffect pe : p.getActivePotionEffects()) {
			if (pe.getAmplifier()>3) {
				actionbardisplay.append(ParseEffect(p,pe));
			}
		}
		actionbardisplay.append(AddAdditionalEffects(p));
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
				effectString.append(ChatColor.GOLD+"⚠");
				effectString.append(AppendAmplifier((p.getFireTicks()/20)-1,false));
				effectString.append("  ");
			}
			if (pd.lifestealstacks>4) {
				effectString.append(ChatColor.AQUA+"❣");
				effectString.append(AppendAmplifier(pd.lifestealstacks-1));
				effectString.append("  ");
			}
			if (pd.weaponcharges>4) {
				effectString.append(ChatColor.DARK_AQUA+"☤");
				effectString.append(AppendAmplifier(pd.weaponcharges-1));
				effectString.append("  ");
			}
			if (pd.damagepool>4) {
				effectString.append(ChatColor.DARK_PURPLE+"♥");
				effectString.append(AppendAmplifier((int)(pd.damagepool-1)));
				effectString.append("  ");
			}
			if (pd.swiftaegisamt>4) {
				effectString.append(ChatColor.YELLOW+"❈");
				effectString.append(AppendAmplifier((int)(GenericFunctions.getSwiftAegisAmt((Player)p)-1)));
				effectString.append("  ");
			}
		}
		if (effectString.length()>0) {
			return effectString.toString()+ChatColor.RESET;
		} else {
			return "";
		}
	}

	private static String ParseEffect(LivingEntity p, PotionEffect pe) {
		StringBuilder effectString=new StringBuilder("");
		PotionEffectType pet = pe.getType();
		if (pet.equals(PotionEffectType.INCREASE_DAMAGE)) {
			effectString.append(ChatColor.GOLD+"⚔");
		} else
		if (pet.equals(PotionEffectType.DAMAGE_RESISTANCE)) {
			effectString.append(ChatColor.BLUE+"❈");
		} else
		if (pet.equals(PotionEffectType.REGENERATION)) {
			effectString.append(ChatColor.GREEN+"✙");
		} else
		if (pet.equals(PotionEffectType.SPEED)) {
			effectString.append(ChatColor.WHITE+"➠");
		} else
		if (pet.equals(PotionEffectType.POISON) || 
				(pet.equals(PotionEffectType.BLINDNESS) && (p instanceof LivingEntity && !(p instanceof Player)))) {
			effectString.append(ChatColor.YELLOW+"☣");
		} else
		if ((pet.equals(PotionEffectType.UNLUCK) && (p instanceof LivingEntity && !(p instanceof Player)))) {
			effectString.append(ChatColor.DARK_RED+"☠");
		} else
		if (pet.equals(PotionEffectType.SLOW)) {
			effectString.append(ChatColor.DARK_AQUA+"♒");
		} else
		if (pet.equals(PotionEffectType.WEAKNESS) || pet.equals(PotionEffectType.SLOW_DIGGING)) {
			effectString.append(ChatColor.RED+"✘");
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
