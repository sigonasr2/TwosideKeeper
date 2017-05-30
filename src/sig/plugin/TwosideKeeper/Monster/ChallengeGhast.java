package sig.plugin.TwosideKeeper.Monster;

import org.bukkit.ChatColor;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.LivingEntity;

import sig.plugin.TwosideKeeper.CustomMonster;
import sig.plugin.TwosideKeeper.LivingEntityStructure;
import sig.plugin.TwosideKeeper.MonsterController;
import sig.plugin.TwosideKeeper.HelperStructures.LivingEntityDifficulty;

public class ChallengeGhast extends HellfireGhast{

	public ChallengeGhast(LivingEntity m) {
		super(m);
	}

	public static boolean isChallengeGhast(LivingEntity m) {
		if (m instanceof org.bukkit.entity.Ghast &&
				LivingEntityStructure.getCustomLivingEntityName(m).equalsIgnoreCase(ChatColor.RED+"Challenge Ghast") &&
				m.getMaxHealth()==25000000 &&
				MonsterController.getLivingEntityDifficulty(m)==LivingEntityDifficulty.NORMAL) {
			return true;
		}
		return false;
	}
}
