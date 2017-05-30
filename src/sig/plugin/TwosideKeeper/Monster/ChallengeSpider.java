package sig.plugin.TwosideKeeper.Monster;

import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;

import sig.plugin.TwosideKeeper.CustomMonster;
import sig.plugin.TwosideKeeper.LivingEntityStructure;
import sig.plugin.TwosideKeeper.MonsterController;
import sig.plugin.TwosideKeeper.HelperStructures.LivingEntityDifficulty;

public class ChallengeSpider extends HellfireSpider{

	public ChallengeSpider(LivingEntity m) {
		super(m);
	}

	public static boolean isChallengeSpider(LivingEntity m) {
		if (m instanceof org.bukkit.entity.Spider &&
				LivingEntityStructure.getCustomLivingEntityName(m).equalsIgnoreCase(ChatColor.RED+"Challenge Spider") &&
				m.getMaxHealth()==25000000 &&
				MonsterController.getLivingEntityDifficulty(m)==LivingEntityDifficulty.NORMAL) {
			return true;
		}
		return false;
	}

}
