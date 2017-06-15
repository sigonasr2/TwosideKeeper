package sig.plugin.TwosideKeeper.HelperStructures;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import sig.plugin.TwosideKeeper.PlayerStructure;
import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.aPluginAPIWrapper;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;

public class ArrowBarrage implements Runnable{
	int shots_left=20;
	Player p;
	int tick_spd = 3;
	
	public ArrowBarrage(int shots, Player p, int spd) {
		this.shots_left=shots;
		this.p = p;
		this.tick_spd=spd;
	}

	@Override
	public void run() {
		shots_left--;
		Arrow arr = p.launchProjectile(Arrow.class);
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		pd.rangermode=BowMode.SNIPE;
		arr.setVelocity(p.getLocation().getDirection().multiply(2));
		TwosideKeeper.ShootPiercingArrow(arr, p);
		arr.remove();
		GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.SLOW, 4, 9, p, true);
		if (shots_left>0) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(TwosideKeeper.plugin, this, 3);
		} else {
			aPluginAPIWrapper.sendCooldownPacket(p, p.getEquipment().getItemInMainHand(), GenericFunctions.GetRemainingCooldownTime(p, pd.last_arrowbarrage, 1));
			Bukkit.getScheduler().runTaskLater(TwosideKeeper.plugin, ()->{
				aPluginAPIWrapper.sendCooldownPacket(p, p.getEquipment().getItemInMainHand(), GenericFunctions.GetRemainingCooldownTime(p, pd.last_arrowbarrage, TwosideKeeper.ARROWBARRAGE_COOLDOWN));
			}, 1);
		}
	}
}
