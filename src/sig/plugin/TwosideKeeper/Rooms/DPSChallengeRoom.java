package sig.plugin.TwosideKeeper.Rooms;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Zombie;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import net.minecraft.server.v1_9_R1.Chunk;
import sig.plugin.TwosideKeeper.LivingEntityStructure;
import sig.plugin.TwosideKeeper.PlayerStructure;
import sig.plugin.TwosideKeeper.Room;
import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.Generators.DPSRoom;
import sig.plugin.TwosideKeeper.HelperStructures.PlayerMode;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.SoundUtils;
import sig.plugin.TwosideKeeper.Monster.ChallengeBlaze;
import sig.plugin.TwosideKeeper.Monster.ChallengeGhast;
import sig.plugin.TwosideKeeper.Monster.ChallengeSpider;
import sig.plugin.TwosideKeeper.Monster.ChallengeZombie;

public class DPSChallengeRoom extends Room{
	Player p;
	double dmg;
	long expireTime;
	boolean started=false;
	List<LivingEntity> mobs = new ArrayList<LivingEntity>();
	BossBar timer;
	HashMap<PlayerMode,Integer> modes = new HashMap<PlayerMode,Integer>();
	boolean roomFinished=false;

	public DPSChallengeRoom(Player p, ChunkGenerator generator) {
		super(generator);
		this.p=p;
		this.dmg=0;
		this.expireTime=0;
		this.started=false;
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		pd.locBeforeInstance = p.getLocation().clone();
		p.teleport(new Location(instance,ROOM_WIDTH/2,24,ROOM_LENGTH/2));
		GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.LEVITATION, 20*4, -30, p, true);
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
			keepHealthbarUpdated();
			if (roomFinished) {
				return false;
			}
			return true;
		} else {
			return false;
		}
	}
	
	private void keepHealthbarUpdated() {
		if (timer!=null) {
			if (expireTime-TwosideKeeper.getServerTickTime()>0) {
				timer.setProgress((expireTime-TwosideKeeper.getServerTickTime())/1200d);
				if (expireTime-TwosideKeeper.getServerTickTime()<400) {
					timer.setColor(BarColor.RED);
				}
			} else {
				//Time is up.
				timer.removeAll();
				timer=null;
				SoundUtils.playGlobalSound(new Location(instance,ROOM_WIDTH/2,1,ROOM_LENGTH/2), Sound.BLOCK_NOTE_PLING, 1.0f, 0.7f);
				instance.strikeLightningEffect(new Location(instance,0,20,0));
				instance.strikeLightningEffect(new Location(instance,ROOM_WIDTH,20,0));
				instance.strikeLightningEffect(new Location(instance,0,20,ROOM_LENGTH));
				instance.strikeLightningEffect(new Location(instance,ROOM_WIDTH,20,ROOM_LENGTH));
				PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
				pd.customtitle.modifyLargeCenterTitle(ChatColor.RED+"TIME UP!", 60);
				pd.dpstrackinglocked=false;
				p.sendMessage(pd.damagedata.OutputResults());
				pd.damagelogging=false;
				pd.damagepool=0;
				double dmg = GetHealthDifferences();
				p.sendMessage("");
				p.setFireTicks(0);
				DecimalFormat df = new DecimalFormat("0.00");
				TwosideKeeper.dpschallenge_recordsHOF.addRecord(p, dmg, getMostUsedPlayerMode(p));
				TwosideKeeper.dpschallenge_records.addRecord(p, dmg, getMostUsedPlayerMode(p));
				p.sendMessage("You dealt a total of "+ChatColor.YELLOW+""+df.format(dmg)+" dmg"+ChatColor.RESET+".");
				for (LivingEntity l : mobs) {
					l.remove();
				}
				Bukkit.getScheduler().runTaskLater(TwosideKeeper.plugin, ()->{
					if (p!=null && p.isValid()) {
						p.teleport(pd.locBeforeInstance);
						Room.awardSuccessfulClear(p, TwosideKeeper.dpschallenge_records.getName());
						pd.locBeforeInstance=null;
					}
					roomFinished=true;
				}, 20*5);
			}
		}
	}

	private double GetHealthDifferences() {
		double dmgdealt = 0;
		for (LivingEntity l : mobs) {
			dmgdealt += l.getMaxHealth()-l.getHealth();
		}
		return dmgdealt;
	}

	private void verifySign() {
		if (!started) {
			Block sign = instance.getBlockAt(ROOM_WIDTH/2, 1, ROOM_LENGTH/2);
			if (sign.getType()!=Material.SIGN_POST) {
				sign.setType(Material.SIGN_POST);
				Sign s = (Sign)sign.getState();
				s.setLine(0, "Click Here to");
				s.setLine(1, "begin the");
				s.setLine(3, ChatColor.DARK_RED+"DPS Challenge");
				s.update();
			}
		}
		if (!p.getWorld().equals(instance)) {
			cleanup();
		}
	}

	public void cleanup() {
		if (timer!=null) {
			timer.removeAll();
		}
		super.cleanup();
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
			if (s.getLine(3).equalsIgnoreCase(ChatColor.DARK_RED+"DPS Challenge")) {
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
				p.sendMessage(ChatColor.GREEN+""+ChatColor.ITALIC+"You will have 60 seconds to deal as much damage as possible.");
			}
		}, 5);
		Bukkit.getScheduler().runTaskLater(TwosideKeeper.plugin, ()->{
			if (p!=null && p.isValid()) {
				p.sendMessage(ChatColor.YELLOW+""+ChatColor.ITALIC+"Dying will fail the challenge. You only get one try per day.");
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
		expireTime = TwosideKeeper.getServerTickTime()+(20*60);
		timer = Bukkit.getServer().createBossBar("Time Remaining", BarColor.GREEN, BarStyle.SEGMENTED_6, BarFlag.CREATE_FOG);
		timer.setProgress(1.0);
		timer.addPlayer(p);
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		pd.dpstrackinglocked=true;
		pd.damagedata.startRecording();
		pd.damagelogging=true;
		for (int i=0;i<4;i++) {
			final int val = i;
			Bukkit.getScheduler().runTaskLater(TwosideKeeper.plugin, ()->{
				if (p!=null && p.isValid()) {
					SpawnWave(val);
				}
			}, 20*(15*i));
		}
	}

	private void SpawnWave(int i) {
		switch (i) {
			case 0:{
				for (int j=0;j<1;j++) {
					SpawnChallengeZombie();
				}
			}break;
			case 1:{
				for (int j=0;j<1;j++) {
					SpawnChallengeBlaze();
				}
			}break;
			case 2:{
				//if (Math.random()<=)
				/*for (int j=0;j<2;j++) {
					SpawnChallengeBabyZombie();
				}*/
				SpawnChallengeSpider();
			}break;
			case 3:{
				for (int j=0;j<4;j++) {
					SpawnChallengeGhast();
				}
			}break;
		}
	}

	public void onHitEvent(Player p, double dmg) {
		if (p.equals(this.p)) {
			PlayerMode mode = PlayerMode.getPlayerMode(p);
			if (modes.containsKey(mode)) {
				Integer i = modes.get(mode);
				modes.put(mode, ++i);
			} else {
				modes.put(mode, 1);
			}
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

	private void SpawnChallengeSpider() {
		Spider s = (Spider)instance.spawnEntity(new Location(instance,(Math.random()*(ROOM_WIDTH-4))+2,2,(Math.random()*(ROOM_LENGTH-4))+2), EntityType.SPIDER);
		s.setMaxHealth(25000000);
		s.setHealth(s.getMaxHealth());
		s.setTarget(p);
		//LivingEntityStructure les = LivingEntityStructure.GetLivingEntityStructure(z);
		LivingEntityStructure.setCustomLivingEntityName(s, ChatColor.RED+"Challenge Spider");
		TwosideKeeper.custommonsters.put(s.getUniqueId(), new ChallengeSpider(s));
		mobs.add(s);
	}

	private void SpawnChallengeBabyZombie() {
		Zombie z = (Zombie)instance.spawnEntity(new Location(instance,(Math.random()*(ROOM_WIDTH-4))+2,2,(Math.random()*(ROOM_LENGTH-4))+2), EntityType.ZOMBIE);
		z.setMaxHealth(25000000);
		z.setHealth(z.getMaxHealth());
		z.setBaby(true);
		z.setTarget(p);
		//LivingEntityStructure les = LivingEntityStructure.GetLivingEntityStructure(z);
		LivingEntityStructure.setCustomLivingEntityName(z, ChatColor.RED+"Challenge Zombie");
		TwosideKeeper.custommonsters.put(z.getUniqueId(), new ChallengeZombie(z));
		mobs.add(z);
	}

	private void SpawnChallengeGhast() {
		Ghast g = (Ghast)instance.spawnEntity(new Location(instance,(Math.random()*(ROOM_WIDTH-4))+2,Math.random()*6+4,(Math.random()*(ROOM_LENGTH-4))+2), EntityType.GHAST);
		g.setMaxHealth(25000000);
		g.setHealth(g.getMaxHealth());
		//g.setTarget(p);
		//LivingEntityStructure les = LivingEntityStructure.GetLivingEntityStructure(z);
		LivingEntityStructure.setCustomLivingEntityName(g, ChatColor.RED+"Challenge Ghast");
		TwosideKeeper.custommonsters.put(g.getUniqueId(), new ChallengeGhast(g));
		mobs.add(g);
	}

	private void SpawnChallengeBlaze() {
		Blaze b = (Blaze)instance.spawnEntity(new Location(instance,(Math.random()*(ROOM_WIDTH-4))+2,Math.random()*6+4,(Math.random()*(ROOM_LENGTH-4))+2), EntityType.BLAZE);
		b.setMaxHealth(25000000);
		b.setTarget(p);
		b.setHealth(b.getMaxHealth());
		//LivingEntityStructure les = LivingEntityStructure.GetLivingEntityStructure(z);
		LivingEntityStructure.setCustomLivingEntityName(b, ChatColor.RED+"Challenge Blaze");
		TwosideKeeper.custommonsters.put(b.getUniqueId(), new ChallengeBlaze(b));
		mobs.add(b);
	}

	private void SpawnChallengeZombie() {
		Zombie z = (Zombie)instance.spawnEntity(new Location(instance,(Math.random()*(ROOM_WIDTH-4))+2,2,(Math.random()*(ROOM_LENGTH-4))+2), EntityType.ZOMBIE);
		z.setMaxHealth(25000000);
		z.setHealth(z.getMaxHealth());
		z.setTarget(p);
		//LivingEntityStructure les = LivingEntityStructure.GetLivingEntityStructure(z);
		LivingEntityStructure.setCustomLivingEntityName(z, ChatColor.RED+"Challenge Zombie");
		TwosideKeeper.custommonsters.put(z.getUniqueId(), new ChallengeZombie(z));
		mobs.add(z);
	}
}
