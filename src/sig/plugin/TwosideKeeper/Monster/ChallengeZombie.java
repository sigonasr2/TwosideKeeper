package sig.plugin.TwosideKeeper.Monster;

import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;

import sig.plugin.TwosideKeeper.CustomMonster;
import sig.plugin.TwosideKeeper.LivingEntityStructure;
import sig.plugin.TwosideKeeper.MonsterController;
import sig.plugin.TwosideKeeper.HelperStructures.LivingEntityDifficulty;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.Classes.MixedDamage;

public class ChallengeZombie extends CustomMonster{

	public ChallengeZombie(LivingEntity m) {
		super(m);
	}
	
	public MixedDamage getBasicAttackDamage() {
		return MixedDamage.v(50,0.01,1);
	}

	public static boolean isChallengeZombie(LivingEntity m) {
		if (m instanceof Zombie &&
				LivingEntityStructure.getCustomLivingEntityName(m).equalsIgnoreCase(ChatColor.RED+"Challenge Zombie") &&
				m.getMaxHealth()==25000000 &&
				MonsterController.getLivingEntityDifficulty(m)==LivingEntityDifficulty.NORMAL) {
			return true;
		}
		return false;
	}
}
