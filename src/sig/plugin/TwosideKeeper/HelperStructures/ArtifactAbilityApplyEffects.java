package sig.plugin.TwosideKeeper.HelperStructures;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;

/**
 * Defines all the effects and calculations applied by artifact abilities.
 */
public class ArtifactAbilityApplyEffects {
	//Returns true if the player has this artifact ability on one of their equipment.
	public static boolean canExecuteArtifactAbility(ArtifactAbility ab, Player p) {
		//First check the main hand.
		ItemStack[] testitems = {
				p.getEquipment().getItemInMainHand(),
				p.getEquipment().getHelmet(),
				p.getEquipment().getChestplate(),
				p.getEquipment().getLeggings(),
				p.getEquipment().getBoots(),
		};
		for (int i=0;i<testitems.length;i++) {
			if (GenericFunctions.isArtifactEquip(testitems[i]) &&
					ArtifactAbility.containsEnchantment(ab, testitems[i])) {
				return true;
			}
		}
		return false;
	}
}
