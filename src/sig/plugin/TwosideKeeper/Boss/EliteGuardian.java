package sig.plugin.TwosideKeeper.Boss;

import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import sig.plugin.TwosideKeeper.EliteMonster;
import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.HelperStructures.Common.Camera;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;

public class EliteGuardian extends EliteMonster{
	Location firstHitLoc = null;
	boolean cutsceneplayed=false;
	STATE state = STATE.WAITINGFORCUTSCENE;
	int cutscenetimer=0;
	Camera cam;
	Arena arena;

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
		runArenaTick();
	}
	
	private void runArenaTick() {
		if (arena!=null) {
			arena.runTick();
		}
	}

	private void runStateMachine() {
		switch (state) {
			case PASSIVE:
				m.setAI(true);
				m.setInvulnerable(false);
				break;
			case WAITINGFORCUTSCENE:
				m.setInvulnerable(true);
				List<Player> nearby = GenericFunctions.getNearbyPlayers(m.getLocation(), 4);
				if (nearby.size()>0) {
					boolean hasLineOfSight=false;
					for (Player p : nearby) {
						if (p.hasLineOfSight(m)) {
							hasLineOfSight=true;
							break;
						}
					}
					if (hasLineOfSight) {
						List<Player> nearby2 = GenericFunctions.getNearbyPlayers(m.getLocation(), 16);
						//Play the cutscene for all of these players.
						/*for (Player p : nearby2) {
							p.setVelocity(new Vector(0,0,0));
							targetlist.add(p);
							if (cutscenetimer==0) {
								p.setGameMode(GameMode.SPECTATOR);
								p.setSpectatorTarget(m);
							}
						}
						if (cutscenetimer==0) {
							GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.BLINDNESS, 40, 1, m);
						}*/
						cam = new Camera(m.getLocation(),nearby2.toArray(new Player[nearby2.size()]));
						targetlist.addAll(nearby2);
						TwosideKeeper.cameras.add(cam);
						cutscenetimer=0;
						state = STATE.RUNNINGCUTSCENE;
						TwosideKeeper.log("Monster Location: "+m.getLocation(),5);
						cam.rotateAroundPointWithZoom(0.55, 350, m.getLocation(), 8, 0.04, 1, 4);
						//cam.rotateAroundPoint(0.55, 200, m.getLocation(), 8, 4);
						arena = new Arena(m.getWorld(),
								m.getLocation().getBlockX()-10,m.getLocation().getBlockY()-2,m.getLocation().getBlockZ()-10,
								20,7,20,
								Material.PRISMARINE);
						arena.AddPlayers(nearby2.toArray(new Player[nearby2.size()]));
						arena.AssembleArena();
						TwosideKeeper.arenas.add(arena);
					}
				}
				m.setAI(false);
				break;
			case RUNNINGCUTSCENE:
				cutscenetimer++;
				if (cutscenetimer>60) {
					state=STATE.PASSIVE;
					cam.Cleanup();
				}
				m.setAI(false);
				break;
			default:
				break;
		}
		
		for (Player p : targetlist) {
			adjustMiningFatigue(p);
		}
	}

	private void adjustMiningFatigue(Player p) {
		if (p.hasPotionEffect(PotionEffectType.SLOW_DIGGING) && ((GenericFunctions.getPotionEffectLevel(PotionEffectType.SLOW_DIGGING, p)==2 && GenericFunctions.getPotionEffectDuration(PotionEffectType.SLOW_DIGGING, p)>=4800) || (GenericFunctions.getPotionEffectLevel(PotionEffectType.SLOW_DIGGING, p)==20 && GenericFunctions.getPotionEffectDuration(PotionEffectType.SLOW_DIGGING, p)<4800))) {
			GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.SLOW_DIGGING, 6000, 20, p, true);
		}
	}

	public void runHitEvent(LivingEntity damager, double dmg) {
		super.runHitEvent(damager,dmg);
	}
	
	public void Cleanup() {
		super.Cleanup();
		if (arena!=null) {
			arena.DisassembleArena();
			TwosideKeeper.arenas.remove(arena);
		}
	}
	
	public void AnnounceFailedTakedown() {
		super.AnnounceFailedTakedown();
		if (dpslist.size()>0 && !m.isDead()) {
			if (arena!=null) {
				arena.DisassembleArena();
				TwosideKeeper.arenas.remove(arena);
				arena=null;
			}
			state=STATE.WAITINGFORCUTSCENE;
			cutscenetimer=0;
		}
	}

	enum STATE {
		PASSIVE, //Just works like vanilla Minecraft behavior.
		WAITINGFORCUTSCENE, //A mode where the game is waiting for a cutscene to occur. The Elite Guardian does not move during this time.
		RUNNINGCUTSCENE,
	}
}
