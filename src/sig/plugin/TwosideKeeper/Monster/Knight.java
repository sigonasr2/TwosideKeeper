package sig.plugin.TwosideKeeper.Monster;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.inventivetalent.glow.GlowAPI.Color;
import org.bukkit.entity.Spider;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import sig.plugin.TwosideKeeper.Buff;
import sig.plugin.TwosideKeeper.ChargeZombie;
import sig.plugin.TwosideKeeper.CustomMonster;
import sig.plugin.TwosideKeeper.LivingEntityStructure;
import sig.plugin.TwosideKeeper.MonsterController;
import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.Events.EntityChannelCastEvent;
import sig.plugin.TwosideKeeper.HelperStructures.Channel;
import sig.plugin.TwosideKeeper.HelperStructures.LivingEntityDifficulty;
import sig.plugin.TwosideKeeper.HelperStructures.Spell;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;
import sig.plugin.TwosideKeeper.HelperStructures.Effects.DarkSlash;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.BlockUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.EntityUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.ItemUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.MovementUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.SoundUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.Classes.MixedDamage;

public class Knight extends CustomMonster{
	
	DarkSpider spider_pet;
	protected String arrow = "->";
	protected List<Player> participantlist = new ArrayList<Player>();
	protected HashMap<String,Double> dpslist = new HashMap<String,Double>();
	BossBar healthbar;
	long lasthit;
	boolean startedfight=false;
	boolean isFlying=false;
	private long stuckTimer=0;
	int scroll=0;
	private Location lastLoc = null;

	final static int[] ASSASSINATE_COOLDOWN = new int[]{320,280,240};
	long lastusedassassinate = TwosideKeeper.getServerTickTime();
	final Spell DARKSLASH = new Spell("Dark Slash",new int[]{60,40,40},new int[]{400,300,200},new MixedDamage[]{MixedDamage.v(150),MixedDamage.v(300),MixedDamage.v(300,0.1)});
	final Spell LINEDRIVE = new Spell("Line Drive",new int[]{20,10,10},new int[]{800,700,600},new MixedDamage[]{MixedDamage.v(200),MixedDamage.v(400),MixedDamage.v(400, 0.2)});
	
	int randomness = 20;
	

	public Knight(LivingEntity m) {
		super(m);
		LivingEntityStructure les = LivingEntityStructure.GetLivingEntityStructure(m);
		les.setCustomLivingEntityName(m, ChatColor.GOLD+"Knight");
		LivingEntityDifficulty led = MonsterController.getLivingEntityDifficulty(m);
		switch (led) {
			case T1_MINIBOSS:{
				m.setMaxHealth(18000);
			}break;
			case T2_MINIBOSS:{
				m.setMaxHealth(47000);
			}break;
			case T3_MINIBOSS:{
				m.setMaxHealth(116000);
			}break;
		}
		m.setHealth(m.getMaxHealth());
		relinkToSpider();
		m.setAI(false);
		createBossHealthbar();
		//GenericFunctions.setGlowing(m, Color.AQUA);
		setupDarkSword();
	}

	public void runTick() {
		updateHealthbarForNearbyPlayers();
		relinkToSpider();
		displayDarkSwordParticles();
		updateTargetIfLost();
		regenerateHealthAndResetBossIfIdle();
		keepHealthbarUpdated();
		keepSpiderPetNearby();
		unstuckIfStuck();
		preventTargetFromBeingTheSameAsSpider();
		increaseBarTextScroll();
		performSpells();
	}
	
	public void runChannelCastEvent(EntityChannelCastEvent ev) {
		switch (ev.getAbilityName()) {
			case "Dark Slash":{
				TwosideKeeper.windslashes.add(
						new DarkSlash(m.getLocation(),m,DARKSLASH.getDamageValues()[getDifficultySlot()],20*20)
						);
				BlockFace[] dirs = MovementUtils.get45DegreeDirections(EntityUtils.getFacingDirection(m));
				for (BlockFace face : dirs) {
					TwosideKeeper.windslashes.add(
							new DarkSlash(m.getLocation().add(
									new Vector(face.getModX(),face.getModY(),face.getModZ()).multiply(3)
									),m,DARKSLASH.getDamageValues()[getDifficultySlot()],20*20)
							);
				}
				DARKSLASH.setLastCastedTime(TwosideKeeper.getServerTickTime());
			}break;
			case "Line Drive":{
				m.setVelocity(new Vector(0,0.6,0));
				Bukkit.getScheduler().runTaskLater(TwosideKeeper.plugin, ()->{
					m.setVelocity(m.getLocation().getDirection().multiply(8));
					int range = 8;
					double xspd = m.getLocation().getDirection().getX()*2;
					double zspd = m.getLocation().getDirection().getZ()*2;
					for (int i=0;i<range;i++) {
						Location particlepos = m.getLocation().add(i*xspd,0,i*zspd);
						for (int j=0;j<50;j++) {
							particlepos.add(j*(xspd/50),0,j*(zspd/50));
						}
						Location newpos = m.getLocation().add(i*xspd,0,i*zspd);
						if (!BlockUtils.isPassThrough(newpos)) {
							break;
						}
						Bukkit.getScheduler().runTaskLater(TwosideKeeper.plugin, 
								()->{
									
								},2);
					}
				}, 4);
			}break;
		}
	}
	
	protected void CastSpell(Spell spell) {
		if (cooldownIsAvailable(spell.getLastCastedTime(),spell)) {
			//Face target.
			FaceTarget(m);
			Channel.createNewChannel(m, spell.getName(), spell.getCastTimes()[getDifficultySlot()]);
		}
	}
	
	private void FaceTarget(LivingEntity m) {
		if (((Monster)m).getTarget()!=null) {
			Location loc = m.getLocation();
			loc.setDirection(MovementUtils.pointTowardsLocation(loc, ((Monster)m).getTarget().getLocation()));
			m.teleport(loc);
		}
	}

	private boolean cooldownIsAvailable(long spell_timer, Spell spell) {
		return spell_timer+spell.getCooldowns()[getDifficultySlot()]<=TwosideKeeper.getServerTickTime();
	}
	
	private void performSpells() {
		final Runnable[] actions = new Runnable[]{
				()->{performAssassinate();},
				()->{CastSpell(DARKSLASH);},
				()->{changeAggroToRandomNewTarget();CastSpell(LINEDRIVE);}};
		if (canCastSpells()) {
			for (Runnable r : actions) {
				if (Math.random()<=1d/actions.length) {
					Bukkit.getScheduler().runTask(TwosideKeeper.plugin, r);
					break;
				}
			}
		}
	}

	private void performAssassinate() {
		if (lastusedassassinate+ASSASSINATE_COOLDOWN[getDifficultySlot()]<=TwosideKeeper.getServerTickTime()) {
			lastusedassassinate=TwosideKeeper.getServerTickTime();
			Player p = setAggroOnRandomTarget();
			Location teleloc = p.getLocation().add(p.getLocation().getDirection().multiply(-1d));
			if (teleloc.getBlock().getType().isSolid() ||
					teleloc.getBlock().getRelative(0,1,0).getType().isSolid()) {
				teleloc = p.getLocation();
			}
			m.teleport(teleloc);
			SoundUtils.playGlobalSound(m.getLocation(), Sound.BLOCK_NOTE_SNARE, 1.0f, 1.0f);
		}
	}

	private Player setAggroOnRandomTarget() {
		Player p = pickRandomTarget();
		setAggro((Monster)m,p);
		return p;
	}
	
	public LivingEntityDifficulty getDifficulty() {
		return MonsterController.getLivingEntityDifficulty(m);
	}
	
	public int getDifficultySlot() {
		switch (getDifficulty()) {
			case T1_MINIBOSS:{
				return 0;
			}
			case T2_MINIBOSS:{
				return 1;
			}
			case T3_MINIBOSS:{
				return 2;
			}
			default:{
				TwosideKeeper.log("WARNING! Could not get proper difficulty slot for Difficulty "+getDifficulty()+". Defaulting to slot 0.", 1);
				return 0;
			}
		}
	}
	
	private void changeAggroToRandomNewTarget() {
		if (Math.random()<=0.5) {
			Monster me = (Monster)m;
			Player newtarget = pickRandomTarget();
			setAggro(me, newtarget);
		}
	}

	private void setAggro(Monster me, Player newtarget) {
		if (newtarget!=null) {
			me.setTarget(newtarget);
			LivingEntityStructure les = LivingEntityStructure.GetLivingEntityStructure(m);
			les.SetTarget(me.getTarget());
		}
	}
	
	private Player pickRandomTarget() {
		if (participantlist.size()>0) {
			for (Player p : participantlist) {
				if (Math.random()<=1d/participantlist.size()) {
					return p;
				}
			}
			return participantlist.get(0);
		} else {
			return null;
		}
	}

	private boolean canCastSpells() {
		 return Math.random()<=1/20d && !Buff.hasBuff(m, "SILENCE") && startedfight && !Channel.isChanneling(m);
	}

	private void preventTargetFromBeingTheSameAsSpider() {
		if (isValidSpiderPet()) {
			Monster me = (Monster)m;
			Monster spider = (Monster)spider_pet.GetMonster();
			if (spider.getTarget()!=null && me.getTarget()!=null &&
					spider.getTarget().equals(me.getTarget())) {
				if (Math.random()<=0.5) {
					Location newloc = spider.getLocation().add(Math.random()*15-5,0,0);
					if (!newloc.getBlock().getType().isSolid() &&
							!newloc.getBlock().getRelative(0,1,0).getType().isSolid()) {
						//SoundUtils.playGlobalSound(spider.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 1.0f, 1.0f);
						spider.teleport(newloc);
						spider.setTarget(null);
					}
				} else {
					Location newloc = spider.getLocation().add(Math.random()*10-5,0,0);
					if (!newloc.getBlock().getType().isSolid() &&
							!newloc.getBlock().getRelative(0,1,0).getType().isSolid()) {
						//SoundUtils.playGlobalSound(spider.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 1.0f, 1.0f);
						spider.teleport(newloc);
						spider.setTarget(null);
					}
				}
			}
		}
	}

	private boolean isValidSpiderPet() {
		return spider_pet!=null && spider_pet.GetMonster()!=null &&
				spider_pet.GetMonster().isValid();
	}

	private void unstuckIfStuck() {
		if (!startedfight) {
			ChargeZombie.BreakBlocksAroundArea((Monster)m, 1);
		} else
		if (startedfight) {
			lastLoc = m.getLocation().clone();
			if (lastLoc!=null && lastLoc.distance(m.getLocation())<=0.4) {
				stuckTimer++;
				//TwosideKeeper.log("Stuck. "+stuckTimer, 0);
				ChargeZombie.BreakBlocksAroundArea((Monster)m, 1);
			} else {
				stuckTimer=0;
			}
			if (!Channel.isChanneling(m) && stuckTimer>5) {
				//Teleport randomly.
				double numb = Math.random();
				if (numb<=0.33) {
					SoundUtils.playGlobalSound(m.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 0.4f, 0.95f);
					m.teleport(m.getLocation().add(Math.random()*10-5,0,0));
				} else
				if (numb<=0.5) {
					SoundUtils.playGlobalSound(m.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 0.4f, 0.95f);
					m.teleport(m.getLocation().add(0,0,Math.random()*10-5));
				}
				stuckTimer=0;
			}
		}
	}

	private void keepSpiderPetNearby() {
		if (isValidSpiderPet()) {
			LivingEntityStructure les = LivingEntityStructure.GetLivingEntityStructure(spider_pet.GetMonster());
			les.SetTarget(m);
		}
	}

	private void keepHealthbarUpdated() {
		healthbar.setProgress(m.getHealth()/m.getMaxHealth());
		Monster me = (Monster)m;
		healthbar.setTitle(GenericFunctions.getDisplayName(m) + ((me.getTarget()!=null && (me.getTarget() instanceof Player))?(ChatColor.DARK_AQUA+" "+arrow+" "+ChatColor.YELLOW+((Player)me.getTarget()).getName()):""));
		if (isValidSpiderPet()) {
			spider_pet.GetMonster().setHealth(spider_pet.GetMonster().getMaxHealth());
		}
	}

	private void regenerateHealthAndResetBossIfIdle() {
		if (lasthit+20*15<=TwosideKeeper.getServerTickTime()) {
			GenericFunctions.HealEntity(m, m.getMaxHealth()*0.01);
			if (startedfight) {
				healthbar.setColor(BarColor.GREEN);
			}
		} else {
			if (startedfight) {
				healthbar.setColor(BarColor.BLUE);
			}
		}
		if (participantlist.size()==0 && startedfight) {
			startedfight=false;
			m.setAI(false);
			m.setHealth(m.getMaxHealth());
			announceFailedTakedown();
		}
	}

	private void updateTargetIfLost() {
		Monster mm = (Monster)m;
		LivingEntityStructure les = LivingEntityStructure.GetLivingEntityStructure(m);
		if (mm.getTarget()==null || !mm.getTarget().isValid() ||
				les.GetTarget()==null || !mm.getTarget().isValid() ||
				(!isFlying && (mm.getTarget().getLocation().distanceSquared(mm.getLocation())>2500 ||
				les.GetTarget().getLocation().distanceSquared(mm.getLocation())>2500
				))) {
			//See if there's another participant in the list. Choose randomly.
			while (participantlist.size()>0) {
				Player p = participantlist.get((int)(Math.random()*participantlist.size()));
				if (p!=null && p.isValid() &&
						(isFlying || p.getLocation().distanceSquared(mm.getLocation())<=2500)) {
					mm.setTarget(p);
					les.SetTarget(p);
					break;
				} else {
					participantlist.remove(p);
				}
			}
			if (participantlist.size()==0 && startedfight) {
				//This fight has failed.
				announceFailedTakedown();
				startedfight=false;
			}
		}
	}

	public void announceFailedTakedown() {
		if (dpslist.size()>0 && !m.isDead()) {
			Bukkit.getServer().broadcastMessage(GenericFunctions.getDisplayName(m)+" Takedown Failed...");
			Bukkit.getServer().broadcastMessage(ChatColor.YELLOW+"DPS Breakdown:");
			Bukkit.getServer().broadcastMessage(generateDPSReport());
			aPlugin.API.discordSendRaw(GenericFunctions.getDisplayName(m)+" Takedown Failed...\n\n"+ChatColor.YELLOW+"DPS Breakdown:"+"\n```\n"+generateDPSReport()+"\n```");
			dpslist.clear();
			PerformSpiderCleanup();
			healthbar.setColor(BarColor.WHITE);
		}
	}

	private void PerformSpiderCleanup() {
		if (spider_pet!=null && spider_pet.GetMonster()!=null) {
			spider_pet.GetMonster().remove();
			spider_pet.cleanup();
		}
	}
	
	public String generateDPSReport() {
		//Sorts a list of players by DPS contribution.
		List<Double> sorted_dmg = new ArrayList<Double>();
		List<String> sorted_pl = new ArrayList<String>();
		double totaldmg = 0;
		for (String pl : dpslist.keySet()) {
			double dmg = dpslist.get(pl);
			int slot = 0;
			totaldmg+=dmg;
			for (int i=0;i<sorted_dmg.size();i++) {
				if (dmg>sorted_dmg.get(i)) {
					break;
				} else {
					slot++;
				}
			}
			sorted_pl.add(slot,pl);
			sorted_dmg.add(slot,dmg);
		}
		StringBuilder finalstr = new StringBuilder();
		DecimalFormat df = new DecimalFormat("0.00");
		for (int i=0;i<sorted_pl.size();i++) {
			if (finalstr.length()!=0) {
				finalstr.append("\n");
			}
			finalstr.append(sorted_pl.get(i)+": "+df.format(sorted_dmg.get(i))+" dmg ("+df.format((sorted_dmg.get(i)/totaldmg)*100)+"%)");
		}
		return finalstr.toString();
	}

	private void setupDarkSword() {
		ItemStack weap = new ItemStack(Material.STONE_SWORD);
		for (Enchantment ench : Enchantment.values()) {
			weap.addUnsafeEnchantment(ench, 10);
		}
		ItemUtils.setDisplayName(weap, ChatColor.DARK_PURPLE+"Dark Sword");
		m.getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
		m.getEquipment().setItemInOffHand(weap);
		m.getEquipment().setItemInOffHandDropChance(0.2f);
	}

	private void createBossHealthbar() {
		healthbar = Bukkit.getServer().createBossBar(GenericFunctions.getDisplayName(m), BarColor.WHITE, BarStyle.SEGMENTED_10, BarFlag.CREATE_FOG);
		healthbar.setProgress(m.getHealth()/m.getMaxHealth());
	}
	
	public void onHitEvent(LivingEntity damager, double damage) {
		addTarget(damager,damage);
		healthbar.setProgress(m.getHealth()/m.getMaxHealth());
		lasthit=TwosideKeeper.getServerTickTime();
		if (!startedfight) {
			startedfight=true;
			healthbar.setColor(BarColor.BLUE);
		}
		m.setAI(true);
	}

	private void addTarget(LivingEntity damager, double dmg) {
		if (damager instanceof Player) {
			Player p = (Player)damager;
			addParticipant(p);
			if (!dpslist.containsKey(p.getName())) {
				dpslist.put(p.getName(), dmg);
			} else {
				dpslist.put(p.getName(), dpslist.get(p.getName())+dmg);
			}
		}
	}

	public void addParticipant(Player p) {
		if (!participantlist.contains(p)) {
			participantlist.add(p);
		}
	}
	private void displayDarkSwordParticles() {
		Location sparkleloc = m.getEyeLocation().add(0,-0.25,0);
		sparkleloc.setDirection(m.getLocation().getDirection().multiply(3));
		m.getWorld().spawnParticle(Particle.SPELL, sparkleloc, 2);
	}

	private void relinkToSpider() {
		if (spider_pet==null ||
				spider_pet.GetMonster()==null || !spider_pet.GetMonster().isValid()) {
			findNewPet();
		}
	}

	private void findNewPet() {
		for (Entity e : m.getNearbyEntities(50, 50, 50)) {
			if (e instanceof Spider) {
				if (AttemptToFindCompanionSpider(e)) {
					return;
				}
			}
		}
		Spider s = DarkSpider.InitializeDarkSpider(m);
		SetupSpiderPet(s);
	}

	private boolean AttemptToFindCompanionSpider(Entity e) {
		Spider ss = (Spider)e;
		if (DarkSpider.isDarkSpider(ss)) {
			SetupSpiderPet(ss);
			return true;
		}
		return false;
	}

	private void updateHealthbarForNearbyPlayers() {
		for (Player p : healthbar.getPlayers()) {
			if (p.getLocation().distanceSquared(m.getLocation())>2500) {
				healthbar.removePlayer(p);
			}
		}
		for (Entity e : m.getNearbyEntities(50, 50, 50)) {
			if (e instanceof Player) {
				Player p = (Player)e;
				healthbar.addPlayer(p);
			}
		}
	}

	private void SetupSpiderPet(LivingEntity m) {
		if (!TwosideKeeper.custommonsters.containsKey(m)) {
			TwosideKeeper.custommonsters.put(m.getUniqueId(),new DarkSpider((LivingEntity)m));
		}
		spider_pet=(DarkSpider)TwosideKeeper.custommonsters.get(m.getUniqueId());
		spider_pet.GetMonster().setAI(false);
		spider_pet.linked_knight=this;
	}
	
	public static boolean randomlyConvertAsKnight(LivingEntity m) {
		return randomlyConvertAsKnight(m,false);
	}

	public static boolean randomlyConvertAsKnight(LivingEntity m, boolean force) {
		if ((TwosideKeeper.MINIBOSSES_ACTIVATED &&
				TwosideKeeper.LAST_SPECIAL_SPAWN+(6000/Math.max(Bukkit.getOnlinePlayers().size(),1))<=TwosideKeeper.getServerTickTime() &&
				Math.random()<=0.01) || force) {
			Skeleton s = (Skeleton)m;
			s.setSkeletonType(SkeletonType.WITHER);
			Spider ss = DarkSpider.InitializeDarkSpider(m);
			//ss.setPassenger(s);
			//Determine distance from Twoside for Difficulty.
			double chancer = TwosideKeeper.TWOSIDE_LOCATION.distanceSquared(m.getLocation());
			if (Math.random()*chancer<4000000) {
				MonsterController.convertLivingEntity(m, LivingEntityDifficulty.T1_MINIBOSS);
			} else
			if (Math.random()*chancer<25000000) {
				MonsterController.convertLivingEntity(m, LivingEntityDifficulty.T2_MINIBOSS);
			} else {
				MonsterController.convertLivingEntity(m, LivingEntityDifficulty.T3_MINIBOSS);
			}
			return true;
		}
		return false;
	}

	public static boolean isKnight(LivingEntity m) {
		return m instanceof Skeleton &&
				((Skeleton)m).getSkeletonType()==SkeletonType.WITHER &&
				(
						MonsterController.getLivingEntityDifficulty(m)==LivingEntityDifficulty.T1_MINIBOSS ||
						MonsterController.getLivingEntityDifficulty(m)==LivingEntityDifficulty.T2_MINIBOSS ||
						MonsterController.getLivingEntityDifficulty(m)==LivingEntityDifficulty.T3_MINIBOSS
				);
	}
	
	public static double getDamageReduction() {
		return 0.2;
	}
	
	public void cleanup() {
		healthbar.removeAll();
		if (startedfight) {
			announceFailedTakedown();
			startedfight=false;
		}
		PerformSpiderCleanup();
	}

	protected void increaseBarTextScroll() {
		scroll++;
		switch (scroll%22) {
			case 11:{
				arrow="  -";
			}break;
			case 12:{
				arrow="   ";
			}break;
			case 13:{
				arrow=">  ";
			}break;
			case 14:{
				arrow="->";
			}break;
		}
	}
}
