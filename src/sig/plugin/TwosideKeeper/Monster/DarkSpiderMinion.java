package sig.plugin.TwosideKeeper.Monster;

import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Spider;

import sig.plugin.TwosideKeeper.CustomMonster;
import sig.plugin.TwosideKeeper.LivingEntityStructure;
import sig.plugin.TwosideKeeper.MonsterController;
import sig.plugin.TwosideKeeper.HelperStructures.LivingEntityDifficulty;
import sig.plugin.TwosideKeeper.HelperStructures.Spell;

public class DarkSpiderMinion extends DarkSpider{

	public DarkSpiderMinion(LivingEntity m) {
		super(m);
		LivingEntityStructure les = LivingEntityStructure.GetLivingEntityStructure(m);
		les.setCustomLivingEntityName(m, ChatColor.DARK_PURPLE+"Dark Spider Minion");
		m.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.5f);
	}

	public void runTick() {
		super.runTick();
		if (canCastSpells()) {
			CastSpell(ULTRABURST);
		}
	}

	public static boolean isDarkSpiderMinion(LivingEntity m) {
		return (m instanceof CaveSpider) &&
				(m.getMaxHealth()==MINION_HEALTH[0] ||
				m.getMaxHealth()==MINION_HEALTH[1] ||
				m.getMaxHealth()==MINION_HEALTH[2]) &&
				MonsterController.getLivingEntityDifficulty(m)==LivingEntityDifficulty.NORMAL;
	}
}
