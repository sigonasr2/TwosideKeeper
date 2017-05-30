package sig.plugin.TwosideKeeper.Monster;

import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;

import sig.plugin.TwosideKeeper.CustomMonster;
import sig.plugin.TwosideKeeper.LivingEntityStructure;
import sig.plugin.TwosideKeeper.MonsterController;
import sig.plugin.TwosideKeeper.HelperStructures.LivingEntityDifficulty;

public class ChallengeBlaze extends Blaze{

	public ChallengeBlaze(LivingEntity m) {
		super(m);
	}


	public static boolean isChallengeBlaze(LivingEntity m) {
		if (m instanceof org.bukkit.entity.Blaze &&
				LivingEntityStructure.getCustomLivingEntityName(m).equalsIgnoreCase(ChatColor.RED+"Challenge Blaze") &&
				m.getMaxHealth()==25000000 &&
				MonsterController.getLivingEntityDifficulty(m)==LivingEntityDifficulty.NORMAL) {
			return true;
		}
		return false;
	}
}
