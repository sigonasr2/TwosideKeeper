package sig.plugin.TwosideKeeper;

import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import sig.plugin.TwosideKeeper.HelperStructures.WorldShop;

public class ActionBarBuffUpdater{

	public static String getActionBarPrefix(LivingEntity p) {
		StringBuilder actionbardisplay = new StringBuilder("");
		for (PotionEffect pe : p.getActivePotionEffects()) {
			if (pe.getAmplifier()>3) {
				actionbardisplay.append(ParseEffect(p,pe));
			}
		}
		if (actionbardisplay.toString().contains("  ")) {
			return actionbardisplay.toString().substring(0, actionbardisplay.toString().lastIndexOf("  "));
		} else {
			return actionbardisplay.toString();
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
				(pet.equals(PotionEffectType.BLINDNESS) && (p instanceof Monster))) {
			effectString.append(ChatColor.YELLOW+"☣");
		} else
		if ((pet.equals(PotionEffectType.UNLUCK) && p instanceof Monster)) {
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
		StringBuilder amp = new StringBuilder(" ");
		amp.append(ChatColor.GRAY+WorldShop.toRomanNumeral(amplifier+1));
		return amp.toString();
	}

}
