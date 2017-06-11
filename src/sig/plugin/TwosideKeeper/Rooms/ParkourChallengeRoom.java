package sig.plugin.TwosideKeeper.Rooms;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import sig.plugin.TwosideKeeper.PlayerStructure;
import sig.plugin.TwosideKeeper.Room;
import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.HelperStructures.CustomPotion;
import sig.plugin.TwosideKeeper.HelperStructures.PlayerMode;
import sig.plugin.TwosideKeeper.HelperStructures.Common.BlockModifyQueue;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.ItemUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.SoundUtils;

public class ParkourChallengeRoom extends Room{
	Player p;
	long startTime;
	long lastLavaTime;
	int lavaLevel;
	Inventory storedinv;
	boolean started=false;
	boolean roomFinished=false;
	boolean finished=false;
	
	public ParkourChallengeRoom(ChunkGenerator generator) {
		super(generator);
	}

	public ParkourChallengeRoom(Player p, ChunkGenerator generator) {
		super(generator);
		this.p=p;
		this.startTime=0;
		this.started=false;
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		pd.locBeforeInstance = p.getLocation().clone();
		pd.inParkourChallengeRoom=true;
		//GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.LEVITATION, 20*4, -30, p, true);
		p.teleport(new Location(instance,ROOM_WIDTH/2,4,ROOM_LENGTH/2));
		storedinv = Bukkit.createInventory(p, 63);
		for (int i=0;i<p.getInventory().getSize();i++) {
			storedinv.setItem(i, p.getInventory().getItem(i));
		}
		p.getInventory().clear();
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
		Vector[] points = new Vector[]{new Vector(1,80,1),new Vector(ROOM_WIDTH-2,80,1),new Vector(1,80,ROOM_LENGTH-2),new Vector(ROOM_WIDTH-2,80,ROOM_LENGTH-2)};
		for (Vector v : points) {
			Block b = instance.getBlockAt(v.getBlockX(),v.getBlockY(),v.getBlockZ());
			b.setType(Material.LAVA);
			b.setData((byte)8,true);
			b.getRelative(0, -1, 0).setType(Material.STATIONARY_LAVA);
			b.getRelative(0, -1, 0).setData((byte)0,true);
		}
	}
	
	public void onLeaveEvent(Player p) {
		if (p.equals(this.p)) {
			for (int i=0;i<p.getInventory().getSize();i++) {
				p.getInventory().setItem(i, storedinv.getItem(i));
			}
		}
	}

	public boolean runTick() {
		if (p!=null && p.isValid()) {
			verifySign();
			updateLava();
			updateBlockAboveChest();
			finishCourse();
			if (roomFinished) {
				return false;
			}
			return true;
		} else {
			return false;
		}
	}


	private void updateBlockAboveChest() {
		Block b = instance.getBlockAt(ROOM_WIDTH/2, 2, ROOM_LENGTH/2);
		if (b.getType()!=Material.AIR) {
			b.setType(Material.AIR);
		}
	}

	private void finishCourse() {
		//TwosideKeeper.log(p.getLocation().getBlockY()+","+p.getLocation().getBlock().getRelative(0, -1, 0).getType(), 0);
		if (p.getLocation().getBlockY()>=81 &&
				p.getLocation().getBlock().getRelative(0,-1,0).getType()==Material.QUARTZ_BLOCK &&
				!finished) {
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			pd.customtitle.modifyLargeCenterTitle(ChatColor.GREEN+"CHALLENGE OVER", 60);
			pd.inParkourChallengeRoom=false;
			finished=true;
			double secsTaken = (TwosideKeeper.getServerTickTime()-startTime)/20d;
			//double score = (1/secsTaken)*1000;
			DecimalFormat df = new DecimalFormat("0.00");
			p.sendMessage("You completed the course in a total time of "+ChatColor.YELLOW+""+df.format(secsTaken)+" secs"+ChatColor.RESET+".");
			TwosideKeeper.parkourchallenge_recordsHOF.addRecord(p, secsTaken, PlayerMode.NORMAL);
			TwosideKeeper.parkourchallenge_records.addRecord(p, secsTaken, PlayerMode.NORMAL);
			Bukkit.getScheduler().runTaskLater(TwosideKeeper.plugin, ()->{
				if (p!=null && p.isValid()) {
					p.teleport(pd.locBeforeInstance);
					Room.awardSuccessfulClear(p, TwosideKeeper.parkourchallenge_records.getName());
					p.setFireTicks(0);
					pd.locBeforeInstance=null;
					for (int i=0;i<p.getInventory().getSize();i++) {
						p.getInventory().setItem(i, storedinv.getItem(i));
					}
				}
				roomFinished=true;
			}, 20*5);
		}
	}

	private void updateLava() {
		if (startTime!=0 && started && lastLavaTime+200<=TwosideKeeper.getServerTickTime()) {
			lastLavaTime = TwosideKeeper.getServerTickTime();
			increaseLavaLevel(++lavaLevel);
		}
	}

	private void increaseLavaLevel(int lava) {
		for (int i=1;i<ROOM_WIDTH-1;i++) {
			for (int j=1;j<ROOM_LENGTH-1;j++) {
				Block b = instance.getBlockAt((int)(i), lava, (int)(j));
				BlockModifyQueue bmq = new BlockModifyQueue(b,Material.AIR,Material.STATIONARY_LAVA);
				TwosideKeeper.blockqueue.add(bmq);
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
				s.setLine(3, ChatColor.DARK_PURPLE+"Parkour Challenge");
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
			if (s.getLine(3).equalsIgnoreCase(ChatColor.DARK_PURPLE+"Parkour Challenge")) {
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
				p.sendMessage(ChatColor.GREEN+""+ChatColor.ITALIC+"Climb to the top of this room using any means. The lava level will start rising.");
			}
		}, 5);
		Bukkit.getScheduler().runTaskLater(TwosideKeeper.plugin, ()->{
			if (p!=null && p.isValid()) {
				p.sendMessage(ChatColor.YELLOW+""+ChatColor.ITALIC+"The challenge completes when you reach the top. You will fail if you die. You only get one try per day.");
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
		lastLavaTime = TwosideKeeper.getServerTickTime()+200;
		Block chest = instance.getBlockAt(ROOM_WIDTH/2, 1, ROOM_LENGTH/2);
		chest.setType(Material.CHEST);
		Chest c = (Chest)(chest.getState());
		Inventory inv = c.getBlockInventory();
		populateChest(inv);
		createBlockGenerator();
	}

	private void createBlockGenerator() {
		double x=ROOM_WIDTH/2;
		int y=3;
		double z=ROOM_LENGTH/2;
		int xdir=(r.nextDouble()<=0.5)?1:-1;
		int zdir=(r.nextDouble()<=0.5)?1:-1;
		final double DEFAULT_DISTANCE = 2.5;
		double distance=DEFAULT_DISTANCE;
		while (y<80) {
			if (r.nextDouble()<=0.6) {
				x+=xdir*distance;
			}
			if (r.nextDouble()<=0.9) {
				z+=zdir*distance;
			}
			if (r.nextDouble()<=0.4) {
				y++;
			}
			if (x<2) {
				x=2;
				xdir*=-1;
			}
			if (z<2) {
				z=2;
				zdir*=-1;
			}
			if (x>ROOM_WIDTH-3) {
				x=ROOM_WIDTH-3;
				xdir*=-1;
			}
			if (z>ROOM_LENGTH-3) {
				z=ROOM_LENGTH-3;
				zdir*=-1;
			}
			distance=DEFAULT_DISTANCE;
			distance+=createBlock(x,y,z);
		}
		x+=xdir*distance;
		z+=zdir*distance;
		Block b = instance.getBlockAt((int)(x), y, (int)(z));
		BlockModifyQueue bmq = new BlockModifyQueue(b,b.getType(),Material.QUARTZ_BLOCK);
		TwosideKeeper.blockqueue.add(bmq);
	}

	private double createBlock(double x, int y, double z) {
		Material blocktype = Material.OBSIDIAN;
		double dist = 0.0;
		if (r.nextDouble()<=0.2) {
			final int OBSTACLE_COUNT=3;
			switch ((int)(r.nextDouble()*OBSTACLE_COUNT)) {
				case 0:{
					blocktype=Material.PACKED_ICE;
					dist-=0.5;
				}break;
				case 1:{
					blocktype=Material.SLIME_BLOCK;
					dist-=0.5;
				}break;
				case 2:{
					blocktype=Material.SOUL_SAND;
					dist-=1.5;
				}break;
			}
			/* Invisible/Blinking Block
			 * Piston Block 
			 * 
			 */
		}
		if (r.nextDouble()<=0.2) {
			for (int i=-1;i<=1;i++) {
				for (int j=-1;j<=1;j++) {
					if (r.nextDouble()<=0.5) {
						/*
						b.setType(Material.STONE);*/
						Block b = instance.getBlockAt((int)(x+i), y, (int)(z+j));
						BlockModifyQueue bmq = new BlockModifyQueue(b,b.getType(),blocktype);
						TwosideKeeper.blockqueue.add(bmq);
					}
				}
			}
		} else
		if (r.nextDouble()<=0.5) {
			for (int i=-1;i<=0;i++) {
				for (int j=-1;j<=0;j++) {
					if (r.nextDouble()<=0.9) {
						/*
						b.setType(Material.STONE);*/
						Block b = instance.getBlockAt((int)(x+i), (r.nextDouble()<=0.33)?y:(r.nextDouble()<=0.5)?y+1:y-1, (int)(z+j));
						BlockModifyQueue bmq = new BlockModifyQueue(b,b.getType(),blocktype);
						TwosideKeeper.blockqueue.add(bmq);
					}
				}
			}
		}
		Block b = instance.getBlockAt((int)(x), y, (int)(z));
		//b.setType(blocktype);
		BlockModifyQueue bmq = new BlockModifyQueue(b,b.getType(),blocktype);
		TwosideKeeper.blockqueue.add(bmq);
		return dist;
	}

	private void populateChest(Inventory inv) {
		ItemStack fireresistpotion = createCustomPotion(PotionEffectType.FIRE_RESISTANCE,20*25,9);
		inv.addItem(fireresistpotion);
		inv.addItem(fireresistpotion);
		inv.addItem(fireresistpotion);
		ItemStack leapingpotion = createCustomPotion(PotionEffectType.JUMP,20*75,1);
		inv.addItem(leapingpotion);
		inv.addItem(leapingpotion);
		inv.addItem(leapingpotion);
		inv.addItem(leapingpotion);
		ItemStack levitationpotion = createCustomPotion(PotionEffectType.LEVITATION,20*10,1);
		inv.addItem(levitationpotion);
		ItemStack speedpotion = createCustomPotion(PotionEffectType.SPEED,20*60,2);
		inv.addItem(speedpotion);
		inv.addItem(new ItemStack(Material.PUMPKIN_PIE,64));
	}

	private ItemStack createCustomPotion(PotionEffectType type, int duration, int amplifier) {
		List<PotionEffect> effects = new ArrayList<PotionEffect>();
		effects.add(new PotionEffect(type, duration, amplifier));
		CustomPotion cp = new CustomPotion(new ItemStack(Material.POTION),effects,amplifier,amplifier);
		ItemStack finalpotion = cp.getItemStack();
		ItemUtils.setDisplayName(finalpotion, "Potion of "+GenericFunctions.CapitalizeFirstLetters(effects.get(0).getType().getName().replaceAll("_", "")));
		return finalpotion;
	}
	public boolean onPlayerDeath(Player p) {
		if (p.equals(this.p) && started) {
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			pd.inParkourChallengeRoom=false;
			pd.customtitle.modifyLargeCenterTitle(ChatColor.GREEN+"CHALLENGE OVER", 60);
			Bukkit.getScheduler().runTaskLater(TwosideKeeper.plugin, ()->{
				if (p!=null && p.isValid()) {
					p.teleport(pd.locBeforeInstance);
					p.setFireTicks(0);
					pd.locBeforeInstance=null;
					for (int i=0;i<p.getInventory().getSize();i++) {
						p.getInventory().setItem(i, storedinv.getItem(i));
					}
				}
				roomFinished=true;
			}, 5);
			return true;
		}
		return false;
	}
}
