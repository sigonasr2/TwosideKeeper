package sig.plugin.TwosideKeeper.Rooms;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import sig.plugin.TwosideKeeper.CustomDamage;
import sig.plugin.TwosideKeeper.MonsterController;
import sig.plugin.TwosideKeeper.PlayerStructure;
import sig.plugin.TwosideKeeper.Room;
import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.HelperStructures.LivingEntityDifficulty;
import sig.plugin.TwosideKeeper.HelperStructures.PlayerMode;
import sig.plugin.TwosideKeeper.HelperStructures.WorldShop;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.SoundUtils;

public class TankChallengeRoom extends Room {
	Player p;
	long startTime;
	boolean started=false;
	boolean finished=false;
	HashMap<PlayerMode,Integer> modes = new HashMap<PlayerMode,Integer>();
	List<LivingEntity> mobs = new ArrayList<LivingEntity>();
	long lastmobspawn=0;
	boolean roomFinished=false;

	public TankChallengeRoom(ChunkGenerator generator) {
		super(generator);
	}

	public TankChallengeRoom(Player p, ChunkGenerator generator) {
		super(generator);
		this.p=p;
		this.startTime=0;
		this.started=false;
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		pd.locBeforeInstance = p.getLocation().clone();
		pd.inTankChallengeRoom=true;
		p.teleport(new Location(instance,ROOM_WIDTH/2,4,ROOM_LENGTH/2));
		GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.LEVITATION, 2, -30, p, true);
		Bukkit.getScheduler().runTaskLater(TwosideKeeper.plugin, ()->{
			setupChallengeRoom();
		}, 5);
	}

	private void setupChallengeRoom() {
		for (int i=1;i<ROOM_WIDTH/8-1;i++) {
			for (int j=1;j<ROOM_LENGTH/8-1;j++) { 
				Block b = instance.getBlockAt(i*8, 1, j*8);
				b.setType(Material.TORCH);
				if (i!=3 && j!=3) {
					b = instance.getBlockAt(i*8+4, 1, j*8+4);
					b.setType(Material.TORCH);
				}
			}
		}
		Vector[] points = new Vector[]{new Vector(1,5,1),new Vector(ROOM_WIDTH-2,5,1),new Vector(1,5,ROOM_LENGTH-2),new Vector(ROOM_WIDTH-2,5,ROOM_LENGTH-2)};
		for (Vector v : points) {
			Block b = instance.getBlockAt(v.getBlockX(),v.getBlockY(),v.getBlockZ());
			b.setType(Material.LAVA);
			b.setData((byte)8,true);
			b.getRelative(0, -1, 0).setType(Material.STATIONARY_LAVA);
			b.getRelative(0, -1, 0).setData((byte)0,true);
		}
	}

	public boolean runTick() {
		if (p!=null && p.isValid()) {
			verifySign();
			getMostUsedMode();
			spawnMobs();
			allMobsFocusTarget();
			if (roomFinished) {
				return false;
			}
			return true;
		} else {
			return false;
		}
	}


	private void allMobsFocusTarget() {
		for (LivingEntity l : mobs) {
			if (l!=null && l.isValid()) {
				if (l instanceof Monster) {
					Monster m = (Monster)l;
					m.setTarget(p);
				}
			} else {
				TwosideKeeper.ScheduleRemoval(mobs, l);
			}
		}
		if (started && !finished && !roomFinished) {
			if (p.isFlying()) {
				p.setFlying(false);
			}
			if (p.getLocation().getBlockY()>20) {
				p.teleport(p.getLocation().add(0,-19,0));
			}
		}
	}

	private void spawnMobs() {
		if (started && !finished) {
			if (startTime!=0 && lastmobspawn+200<=TwosideKeeper.getServerTickTime()) {
				spawnRandomMob();
				lastmobspawn=TwosideKeeper.getServerTickTime();
			}
		}
	}


	private void spawnRandomMob() {
		Location randomloc = new Location(instance,2+(Math.random()*(ROOM_WIDTH-4)),2,2+(Math.random()*(ROOM_LENGTH-4)));
		final EntityType[] mobtypes = new EntityType[]{EntityType.ZOMBIE,EntityType.PIG_ZOMBIE,EntityType.SKELETON,EntityType.GUARDIAN,
				EntityType.BLAZE,EntityType.GHAST,EntityType.ENDERMAN}; 
		double secsLived = (TwosideKeeper.getServerTickTime()-startTime)/20d;
		int spawnamt = (int)(Math.random()*(secsLived/20))+1;
		if (mobs.size()==0) {
			spawnamt=4;
		}
		for (int i=0;i<spawnamt;i++) {
			LivingEntity ent = (LivingEntity)instance.spawnEntity(randomloc, mobtypes[(int)(Math.random()*mobtypes.length)]);
			ent.setMaxHealth(25000000);
			ent.setHealth(ent.getHealth());
			if (r.nextDouble()<=0.02 && secsLived>60) {
				ent = (LivingEntity)instance.spawnEntity(randomloc, EntityType.SHULKER);
				ent.setMaxHealth(25000000);
				ent.setHealth(ent.getHealth());
			}
			if (ent instanceof Monster) {
				Monster m = (Monster)ent;
				m.setTarget(p);
			};
			MonsterController.convertLivingEntity(ent, LivingEntityDifficulty.HELLFIRE);
			CustomDamage.addToCustomStructures(ent);
			ent.setMaxHealth(ent.getMaxHealth()*getTankRoomMultiplier());
			ent.setHealth(ent.getMaxHealth());
			mobs.add(ent);
		}
	}


	private void getMostUsedMode() {
		if (started && startTime!=0 && !finished) {
			PlayerMode mode = PlayerMode.getPlayerMode(p);
			if (modes.containsKey(mode)) {
				Integer i = modes.get(mode);
				modes.put(mode, ++i);
			} else {
				modes.put(mode, 1);
			}
		}
	}


	public void cleanup() {
		super.cleanup();
	}

	private void verifySign() {
		if (!started) {
			Block sign = instance.getBlockAt(ROOM_WIDTH/2, 1, ROOM_LENGTH/2);
			if (sign.getType()!=Material.SIGN_POST) {
				sign.setType(Material.SIGN_POST);
				Sign s = (Sign)sign.getState();
				s.setLine(0, "Click Here to");
				s.setLine(1, "begin the");
				s.setLine(3, ChatColor.DARK_AQUA+"Tank Challenge");
				s.update();
			}
		}
		if (!p.getWorld().equals(instance)) {
			cleanup();
		}
	}

	public void runInteractEvent(PlayerInteractEvent ev) {
		if (ev.getClickedBlock()!=null &&
				ev.getClickedBlock().getType()==Material.SIGN_POST &&
				ev.getClickedBlock().getLocation().getBlockX()==ROOM_WIDTH/2 &&
				ev.getClickedBlock().getLocation().getBlockY()==1 &&
				ev.getClickedBlock().getLocation().getBlockZ()==ROOM_LENGTH/2 &&
				ev.getClickedBlock().getWorld().equals(instance) &&
				ev.getPlayer().equals(this.p)) {
			Sign s = (Sign)(ev.getClickedBlock().getState());
			if (s.getLine(3).equalsIgnoreCase(ChatColor.DARK_AQUA+"Tank Challenge")) {
				beginChallenge();
			}
		}
	}
	
	private void beginChallenge() {
		started=true;
		SoundUtils.playGlobalSound(new Location(instance,ROOM_WIDTH/2,1,ROOM_LENGTH/2), Sound.BLOCK_NOTE_PLING, 1.0f, 1.0f);
		Block sign = instance.getBlockAt(ROOM_WIDTH/2, 1, ROOM_LENGTH/2);
		sign.setType(Material.AIR);
		Bukkit.getScheduler().runTaskLater(TwosideKeeper.plugin, ()->{
			if (p!=null && p.isValid()) {
				p.sendMessage(ChatColor.GREEN+""+ChatColor.ITALIC+"Last for as long as possible, surviving waves of monsters.");
			}
		}, 5);
		Bukkit.getScheduler().runTaskLater(TwosideKeeper.plugin, ()->{
			if (p!=null && p.isValid()) {
				p.sendMessage(ChatColor.YELLOW+""+ChatColor.ITALIC+"The challenge will complete on your death. You only get one try per day.");
			}
		}, 60);
		Bukkit.getScheduler().runTaskLater(TwosideKeeper.plugin, ()->{
			if (p!=null && p.isValid()) {
				p.sendMessage(ChatColor.GREEN+""+ChatColor.ITALIC+"  Good Luck!");
			}
		}, 120);
		Bukkit.getScheduler().runTaskLater(TwosideKeeper.plugin, ()->{
			if (p!=null && p.isValid()) {
				StartChallenge();
			}
		}, 150);
	}


	private void StartChallenge() {
		startTime = TwosideKeeper.getServerTickTime();
		if (p!=null && p.isValid()) {		
			GenericFunctions.logAndRemovePotionEffectFromEntity(PotionEffectType.LEVITATION, p);
		}
	}


	public boolean onPlayerDeath(Player p) {
		if (p.equals(this.p) && started && !finished) {
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			pd.inTankChallengeRoom=false;
			pd.customtitle.modifyLargeCenterTitle(ChatColor.GREEN+"CHALLENGE OVER", 60);
			pd.damagepool=0;
			p.setFireTicks(0);
			long deathTime = TwosideKeeper.getServerTickTime();
			double secsLived = (deathTime-startTime)/20d;
			DecimalFormat df = new DecimalFormat("0.00");
			finished=true;
			for (LivingEntity l : mobs) {
				l.remove();
			}
			mobs.clear();
			instance.setDifficulty(Difficulty.PEACEFUL);
			p.sendMessage("You survived for a total time of "+ChatColor.YELLOW+""+df.format(secsLived)+" secs"+ChatColor.RESET+".");
			TwosideKeeper.tankchallenge_recordsHOF.addRecord(p, secsLived, getMostUsedPlayerMode(p));
			TwosideKeeper.tankchallenge_records.addRecord(p, secsLived, getMostUsedPlayerMode(p));
			Bukkit.getScheduler().runTaskLater(TwosideKeeper.plugin, ()->{
				if (p!=null && p.isValid()) {
					p.teleport(pd.locBeforeInstance);
					Room.awardSuccessfulClear(p, TwosideKeeper.tankchallenge_records.getName());
					pd.locBeforeInstance=null;
				}
				roomFinished=true;
			}, 20*5);
			return true;
		}
		return false;
	}

	public double getTankRoomMultiplier() {
		double secsLived = (TwosideKeeper.getServerTickTime()-startTime)/20d;
		return 4.0+(secsLived*0.01);
	}
	public double getTankRoomBaseDamage() {
		double secsLived = (TwosideKeeper.getServerTickTime()-startTime)/20d;
		return 50.0+(secsLived*2);
	}
	public double getTankRoomDamageReduction() {
		double secsLived = (TwosideKeeper.getServerTickTime()-startTime)/20d;
		return Math.min(0.95, 0.3+(secsLived*0.01));
	}
	public double getTankRoomTrueDamage() {
		double secsLived = (TwosideKeeper.getServerTickTime()-startTime)/20d;
		if (secsLived>60) {
			return secsLived-60;
		} else {
			return 0.0;
		}
	}
	public double getTankRoomTruePctDamage() {
		double secsLived = (TwosideKeeper.getServerTickTime()-startTime)/20d;
		if (secsLived>30) {
			return Math.min(0.5, ((secsLived-30)*0.002));
		} else {
			return 0.0;
		}
	}
	
	public PlayerMode getMostUsedPlayerMode(Player p) {
		int highestamt = 0;
		PlayerMode highestmode = null;
		for (PlayerMode pm : modes.keySet()) {
			int amt = modes.get(pm);
			if (highestamt<amt) {
				highestamt=amt;
				highestmode=pm;
			}
		}
		if (highestmode!=null) {
			return highestmode;
		} else {
			return PlayerMode.NORMAL;
		}
	}
}
