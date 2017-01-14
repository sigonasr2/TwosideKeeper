package sig.plugin.TwosideKeeper.Boss;

import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import sig.plugin.TwosideKeeper.EliteMonster;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;

public class EliteGuardian extends EliteMonster{
	Location firstHitLoc = null;
	boolean cutsceneplayed=false;
	STATE state = STATE.WAITINGFORCUTSCENE;
	int cutscenetimer=0;

	public EliteGuardian(Monster m) {
		super(m);
	}

	public void runTick() {
		//This monster constantly gives itself its buffs as it may lose some (Debilitation mode).
		increaseBarTextScroll();
		dontDrown();
		regenerateHealth();
		resetToSpawn();
		createBossHealthbar();
		ignoreAllOtherTargets();
		runStateMachine();
		if (m.isValid() && targetlist.size()>0) {
			getGlow();
		}
	}
	
	private void runStateMachine() {
		switch (state) {
			case PASSIVE:
				m.setAI(true);
				break;
			case WAITINGFORCUTSCENE:
				List<Player> nearby = GenericFunctions.getNearbyPlayers(m.getLocation(), 4);
				if (nearby.size()>0) {
					List<Player> nearby2 = GenericFunctions.getNearbyPlayers(m.getLocation(), 16);
					//Play the cutscene for all of these players.
					for (Player p : nearby2) {
						p.setVelocity(new Vector(0,0,0));
						if (cutscenetimer==0) {
							p.setGameMode(GameMode.SPECTATOR);
							p.setSpectatorTarget(m);
						}
					}
					if (cutscenetimer==0) {
						GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.BLINDNESS, 40, 1, m);
					}
					cutscenetimer++;
					if (cutscenetimer>100) {
						state=STATE.PASSIVE;
						//Play the cutscene for all of these players.
						for (Player p : nearby2) {
							p.setGameMode(GameMode.SURVIVAL);
						}
					}
				}
				m.setAI(false);
				break;
			default:
				break;
		}
	}

	public void runHitEvent(LivingEntity damager, double dmg) {
		super.runHitEvent(damager,dmg);
	}

	enum STATE {
		PASSIVE, //Just works like vanilla Minecraft behavior.
		WAITINGFORCUTSCENE, //A mode where the game is waiting for a cutscene to occur. The Elite Guardian does not move during this time.
	}
}
