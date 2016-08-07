package sig.plugin.TwosideKeeper;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Server.Spigot;
import org.bukkit.Sound;
import org.bukkit.WorldCreator;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Animals;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Bat;
import org.bukkit.entity.ComplexLivingEntity;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Variant;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.LingeringPotion;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.entity.Witch;
import org.bukkit.entity.EnderDragon.Phase;
import org.bukkit.entity.Enderman;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.AreaEffectCloudApplyEvent; 
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityCombustByBlockEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerAchievementAwardedEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupArrowEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkPopulateEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.metadata.Metadatable;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;
import org.inventivetalent.glow.GlowAPI;

import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;

import aPlugin.DiscordMessageSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_9_R1.AttributeInstance;
import net.minecraft.server.v1_9_R1.EntityInsentient;
import net.minecraft.server.v1_9_R1.EntityLiving;
import net.minecraft.server.v1_9_R1.GenericAttributes;
import net.minecraft.server.v1_9_R1.MinecraftServer;
import net.minecraft.server.v1_9_R1.Vector3f;
import sig.plugin.TwosideKeeper.HelperStructures.AnvilItem;
import sig.plugin.TwosideKeeper.HelperStructures.ArtifactAbility;
import sig.plugin.TwosideKeeper.HelperStructures.ArtifactItem;
import sig.plugin.TwosideKeeper.HelperStructures.ArtifactItemType;
import sig.plugin.TwosideKeeper.HelperStructures.BankSession;
import sig.plugin.TwosideKeeper.HelperStructures.BowMode;
import sig.plugin.TwosideKeeper.HelperStructures.CubeType;
import sig.plugin.TwosideKeeper.HelperStructures.CustomRecipe;
import sig.plugin.TwosideKeeper.HelperStructures.DeathStructure;
import sig.plugin.TwosideKeeper.HelperStructures.ItemCube;
import sig.plugin.TwosideKeeper.HelperStructures.ItemRarity;
import sig.plugin.TwosideKeeper.HelperStructures.ItemSet;
import sig.plugin.TwosideKeeper.HelperStructures.MalleableBaseQuest;
import sig.plugin.TwosideKeeper.HelperStructures.MonsterDifficulty;
import sig.plugin.TwosideKeeper.HelperStructures.MonsterType;
import sig.plugin.TwosideKeeper.HelperStructures.QuestStatus;
import sig.plugin.TwosideKeeper.HelperStructures.ServerType;
import sig.plugin.TwosideKeeper.HelperStructures.SessionState;
import sig.plugin.TwosideKeeper.HelperStructures.SpleefArena;
import sig.plugin.TwosideKeeper.HelperStructures.UpgradePath;
import sig.plugin.TwosideKeeper.HelperStructures.WorldShop;
import sig.plugin.TwosideKeeper.HelperStructures.WorldShopSession;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;
import sig.plugin.TwosideKeeper.HelperStructures.Common.Habitation;
import sig.plugin.TwosideKeeper.Logging.BowModeLogger;
import sig.plugin.TwosideKeeper.Logging.DamageLogger;
import sig.plugin.TwosideKeeper.Logging.LootLogger;
import sig.plugin.TwosideKeeper.Logging.MysteriousEssenceLogger;
import net.minecraft.server.v1_9_R1.MinecraftServer;


public class TwosideKeeper extends JavaPlugin implements Listener {

	public final static int CUSTOM_DAMAGE_IDENTIFIER = 1000000;
	
	public static long SERVERTICK=0; //This is the SERVER's TOTAL TICKS when first loaded.
	public static long STARTTIME=0;
	public static long LASTSERVERCHECK=0;
	public static int SERVERCHECKERTICKS=18000;
	public static int TERMINALTIME=220; //How much time in ticks to use the bank withdraw/deposit terminals.
	public static double DEATHPENALTY=50.0; //Percent of holding money that will be taken from the player on death.
	public static double RECYCLECHANCE=65.0; //65% chance to save despawned items. Can be adjusted via config.
	public static double RECYCLEDECAYAMT=20.0; //% chance lost when there's another item of the same type already in there.
	public static double DAYMULT=2.0; //How much the day and night length will be multiplied by.
	public static int ITEMCUBEID=0; //The current number of Item Cubes in existence.
	public static String MOTD=""; //The MOTD announcement to be announced every hour.
	public static double ARMOR_LEATHER_HP=0.5f;
	public static double ARMOR_IRON_HP=1f;
	public static double ARMOR_GOLD_HP=3f;
	public static double ARMOR_DIAMOND_HP=2f;
	public static double ARMOR_IRON2_HP=2f;
	public static double ARMOR_GOLD2_HP=6f;
	public static double ARMOR_DIAMOND2_HP=4f;
	public static long HEALTH_REGENERATION_RATE=100; //The amount of ticks between each health regeneration event.
	public static long FOOD_HEAL_AMT=2; //1 Heart per food item consumed.
	public static double ENEMY_DMG_MULT=1.0f; //
	public static double EXPLOSION_DMG_MULT=0.6f; //
	public static double HEADSHOT_ACC=1.0f; //How accurate headshots have to be. Lower values means more leniency on headshots. Higher values means more strict.
	public static double COMMON_DROP_RATE=0.1; // 1/10 chance
	public static double RARE_DROP_RATE=0.0078125; // 1/128 chance
	public static double LEGENDARY_DROP_RATE=0.00390625;  // 1/256 chance
	public static int PARTY_CHUNK_SIZE=16; //The number of chunks each party spans.
	public double XP_CONVERSION_RATE=0.01; //How much money per exp point?
	public static int WORLD_SHOP_ID=0; //The shop ID number we are on.
	public static int LOGGING_LEVEL=0; //The logging level the server will output in for the console. 0 = No Debug Messages. Toggled with /log.
	public static double ARTIFACT_RARITY=1.5; //The multiplier of artifact drops.
	public static double ELITE_MONSTER_CHANCE=0.01; //The chance an elite monster will be considered.
	public static double ELITE_MONSTER_AREA=0.75; //The percentage of area around the monster that has to be AIR to be considered open enough to spawn.
	public static ServerType SERVER_TYPE=ServerType.TEST; //The type of server this is running on.
	public static int COMMONITEMPCT=3;
	public static long LAST_ELITE_SPAWN = 0;
	public static Location ELITE_LOCATION = null;
	public static List<ArtifactAbility> TEMPORARYABILITIES = new ArrayList<ArtifactAbility>();
	
	public static final int DODGE_COOLDOWN=100;
	public static final int DEATHMARK_COOLDOWN=240;
	public static final int EARTHWAVE_COOLDOWN=300;
	public static final int ERUPTION_COOLDOWN=100;
	public static final int LINEDRIVE_COOLDOWN=240;
	public static final int REJUVENATE_COOLDOWN=2400;
	
	public static File filesave;
	public static HashMap<UUID,PlayerStructure> playerdata;	
	public static HashMap<UUID,MonsterStructure> monsterdata;	
	public static SpleefManager TwosideSpleefGames;
	public static WorldShopManager TwosideShops;
	public static MysteriousEssenceLogger EssenceLogger; //The logger for Essences.
	public static BowModeLogger BowLogger; //The logger for Bow Modes.
	public static LootLogger Loot_Logger; //The logger for Loot.
	public static AutoUpdatePlugin pluginupdater;
	public static boolean restarting_server=false;
	public static List<String> log_messages=new ArrayList<String>();
	
	long LastClearStructureTime = 0;
	 
	public int TeamCounter = 0; 
	public static int time_passed = 0; //The total amount of time lost due to modifications to FullTime().
	public static List<Party> PartyList = new ArrayList<Party>();
	public List<Integer> colors_used = new ArrayList<Integer>();
	public static List<ChargeZombie> chargezombies = new ArrayList<ChargeZombie>();
	public static List<EliteMonster> elitemonsters = new ArrayList<EliteMonster>();
	
	public static RecyclingCenter TwosideRecyclingCenter;
	
	//Bank timers and users.
	public static HashMap banksessions;
	public static Habitation habitat_data;

	public static Plugin plugin;
	public int sleepingPlayers=0;
	
	boolean reloadedchunk=false;
	
	int[] lampblocks = {1626,71,-255, //List of all lamp blocks in the city to be  lit.
			1628,70,-223,
			1626,70,-265,
			1624,70,-267,
			1635,71,-269,
			1612,69,-269,
			1610,69,-271,
			1595,67,-267,
			1595,66,-269,
			1577,66,-271,
			1610,71,-282,
			1608,71,-295,
			1610,67,-317,
			1610,67,-338,
			1610,67,-340,
			1632,71,-342,
			1601,67,-342,
			1587,67,-340};
	
	boolean lamps_are_turned_on = false;

	@Override
    public void onEnable() {
        // TODO Insert logic to be performed when the plugin is enabled
		Bukkit.getPluginManager().registerEvents(this, this);
		
		plugin=this;
		
		loadConfig();
		
		sig.plugin.TwosideKeeper.Recipes.Initialize_ItemCube_Recipes();
		sig.plugin.TwosideKeeper.Recipes.Initialize_ArrowQuiver_Recipe();
		sig.plugin.TwosideKeeper.Recipes.Initialize_BlockArmor_Recipes();
		//sig.plugin.TwosideKeeper.Recipes.Initialize_ItemDeconstruction_Recipes();
		sig.plugin.TwosideKeeper.Recipes.Initialize_WoolRecolor_Recipes();
		sig.plugin.TwosideKeeper.Recipes.Initialize_SlabReconstruction_Recipes();
		sig.plugin.TwosideKeeper.Recipes.Initialize_Artifact_Recipes();
		sig.plugin.TwosideKeeper.Recipes.Initialize_ArtifactHelper_Recipes();
		sig.plugin.TwosideKeeper.Recipes.Initialize_Check_Recipe();
		//sig.plugin.TwosideKeeper.Recipes.Initialize_HunterCompass_Recipe();
		
		//Bukkit.createWorld(new WorldCreator("ItemCube"));
		
		filesave=getDataFolder(); //Store the location of where our data folder is.
		log("Data folder at "+filesave+".",3);
		
		time_passed+=-Bukkit.getWorld("world").getFullTime();
		LASTSERVERCHECK=getServerTickTime();
		
		EssenceLogger = new MysteriousEssenceLogger();
		BowLogger = new BowModeLogger();
		Loot_Logger = new LootLogger();
		
		chargezombies = new ArrayList<ChargeZombie>();
		habitat_data = new Habitation();
		habitat_data.loadLocationHashesFromConfig();
		
		LastClearStructureTime = getServerTickTime();
		
		TwosideRecyclingCenter = new RecyclingCenter();
		TwosideRecyclingCenter.loadConfig();
		TwosideRecyclingCenter.populateItemListFromAllNodes();
		log("Recycling Centers Loaded: "+TwosideRecyclingCenter.getNumberOfNodes(),3);
		
		pluginupdater = new AutoUpdatePlugin(this);
		pluginupdater.AddPlugin("TwosideKeeper", "https://dl.dropboxusercontent.com/s/lcdl1hr6u1ohjx0/TwosideKeeper.jar");
		pluginupdater.AddPlugin("aPlugin", "https://dl.dropboxusercontent.com/u/62434995/aPlugin.jar");
		
		//Create Spleef Games.
		TwosideSpleefGames = new SpleefManager(this);
		
		TwosideSpleefGames.SetupSpleefArena(
				SpleefArena.SMALL, //Spleef Arena Type
				new Location(Bukkit.getWorld("world"),1616,86,53), //Corner 1
				new Location(Bukkit.getWorld("world"),1627,86,64), //Corner 2
				new Location(Bukkit.getWorld("world"),1622,85,58), //Shovel Chest
				new Location(Bukkit.getWorld("world"),1620,83,45) //Registration Sign
				);
		TwosideSpleefGames.SetupSpleefArena( 
				SpleefArena.LARGE, //Spleef Arena Type
				new Location(Bukkit.getWorld("world"),1585,86,24), //Corner 1
				new Location(Bukkit.getWorld("world"),1600,86,39), //Corner 2
				new Location(Bukkit.getWorld("world"),1593,85,34), //Shovel Chest
				new Location(Bukkit.getWorld("world"),1593,85,29), //Shovel Chest 2
				new Location(Bukkit.getWorld("world"),1608,83,34) //Registration Sign
				);
		TwosideSpleefGames.SetupSpleefArena(
				SpleefArena.LAYERED, //Spleef Arena Type
				new Location(Bukkit.getWorld("world"),1658,87,27), //Corner 1
				new Location(Bukkit.getWorld("world"),1646,87,39), //Corner 2
				new Location(Bukkit.getWorld("world"),1652,86,34), //Shovel Chest
				new Location(Bukkit.getWorld("world"),1635,83,31) //Registration Sign
				);
		
		TwosideShops = new WorldShopManager();
		
		//Initialize Player Data structure.
		playerdata = new HashMap<UUID,PlayerStructure>();
		banksessions = new HashMap();
		monsterdata = new HashMap<UUID,MonsterStructure>(); 
		
		TEMPORARYABILITIES.add(ArtifactAbility.GREED);
		TEMPORARYABILITIES.add(ArtifactAbility.SURVIVOR);
		
		//tpstracker = new Lag();
		
		//Let's not assume there are no players online. Load their data.
    	for (int i=0;i<Bukkit.getOnlinePlayers().toArray().length;i++) {
    		playerdata.put(((Player)Bukkit.getOnlinePlayers().toArray()[i]).getUniqueId(), new PlayerStructure((Player)Bukkit.getOnlinePlayers().toArray()[i],getServerTickTime()));
        	//playerdata.add(new PlayerStructure((Player)Bukkit.getOnlinePlayers().toArray()[i],getServerTickTime()));
    	}
    	Player p;
    	//Announce the server has restarted soon after.

		getServer().getScheduler().scheduleSyncRepeatingTask(this, new  Runnable(){
			public void run(){
			//Control charge zombies..
			for (int i=0;i<chargezombies.size();i++) {
				ChargeZombie cz = chargezombies.get(i);
				if (!cz.isAlive() || !cz.hasTarget() || cz.GetZombie().getLocation().getY()>32) {
					//This has to be removed...
					chargezombies.remove(i);
					i--;
				} else {
					//This is fine! Clear away blocks.
					Monster m = cz.GetZombie();
					if (m.getTarget().getLocation().getY()>=m.getLocation().getY()+2 &&
							Math.abs(m.getTarget().getLocation().getX()-m.getLocation().getX())<1 &&
							Math.abs(m.getTarget().getLocation().getZ()-m.getLocation().getZ())<1) {
						//This target is higher than we can reach... Let's pillar.
						Random r = new Random();
						r.setSeed(m.getUniqueId().getMostSignificantBits());
						//Block type is chosen based on the seed. Will be cobblestone, dirt, or gravel.
						if (m.getLocation().getBlock().getType()==Material.AIR &&
								m.getLocation().add(0,-1,0).getBlock().getType()!=Material.AIR &&
								!m.getLocation().add(0,-1,0).getBlock().isLiquid()) {
							m.setVelocity(new Vector(0,0.5,0));
							switch (r.nextInt(3)) {
								case 0:{
									m.getLocation().getBlock().setType(Material.DIRT);
									m.getLocation().getWorld().playSound(m.getLocation(), Sound.BLOCK_GRAVEL_PLACE, 1.0f, 1.0f);
								}break;
								case 1:{
									m.getLocation().getBlock().setType(Material.COBBLESTONE);
									m.getLocation().getWorld().playSound(m.getLocation(), Sound.BLOCK_STONE_PLACE, 1.0f, 1.0f);
								}break;
								case 2:{
									m.getLocation().getBlock().setType(Material.GRAVEL);
									m.getLocation().getWorld().playSound(m.getLocation(), Sound.BLOCK_GRAVEL_PLACE, 1.0f, 1.0f);
								}break;
							}
						}
					}
					cz.BreakBlocksAroundArea(1);
				}
			}
			//Control elite monsters.
			for (int i=0;i<elitemonsters.size();i++) {
				EliteMonster em = elitemonsters.get(i);
				if (!em.m.isValid()) {
					elitemonsters.remove(i);
					i--;
				} else {
					em.runTick();
				}
			}
		}}, 5l, 5l);
		
		if (SERVER_TYPE==ServerType.MAIN) { //Only perform this on the official servers. Test servers do not require constant updating.
			//Every 5 minutes, check for a plugin update.
			if (!restarting_server) {
				Bukkit.getScheduler().runTaskTimerAsynchronously(this, pluginupdater, 6000l, 6000l);
			}
		}
		
	    getServer().getScheduler().runTaskLaterAsynchronously(this, new DiscordStatusUpdater(), 300l);

		getServer().getScheduler().scheduleSyncRepeatingTask(this, new  Runnable(){
			public void run(){
				for (int i=0;i<Bukkit.getOnlinePlayers().size();i++) {
					Player p = (Player)(Bukkit.getOnlinePlayers().toArray()[i]);
					double absorption_amt = ItemSet.TotalBaseAmountBasedOnSetBonusCount(p, ItemSet.SONGSTEEL, 3, 3);
					if (absorption_amt>0) {
						if (p.hasPotionEffect(PotionEffectType.ABSORPTION)) {
							int oldlv = GenericFunctions.getPotionEffectLevel(PotionEffectType.ABSORPTION, p)+1;
							p.removePotionEffect(PotionEffectType.ABSORPTION);
							p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION,599,(int)(absorption_amt/4)+oldlv));
						} else {
							p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION,599,(int)(absorption_amt/4)));
						}
					}
				}
			}
		},0l,600l);
		//This is the constant timing method.
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new  Runnable(){
			public void run(){
			  log("Server time passed: "+(Bukkit.getWorld("world").getFullTime()-STARTTIME)+". New Server Time: "+(Bukkit.getWorld("world").getFullTime()-STARTTIME+SERVERTICK),5);
				//Bukkit.getWorld("world").setFullTime(Bukkit.getWorld("world").getFullTime()-10); //LEGACY CODE.
			  	adjustServerTime(10);
				//WORK IN PROGRESS: Lamp updating code TO GO HERE.
			  	
			  	sendAllLoggedMessagesToSpam();
				
				//SAVE SERVER SETTINGS.
				if (getServerTickTime()-LASTSERVERCHECK>=SERVERCHECKERTICKS) { //15 MINUTES (DEFAULT)
					saveOurData();
					
					//Advertisement messages could go here.
					//MOTD: "Thanks for playing on Sig's Minecraft!\n*bCheck out http://z-gamers.net/mc for update info!\n*aReport any bugs you find at http://zgamers.domain.com/mc/"
					getMOTD();
					getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('*', MOTD));
					habitat_data.increaseHabitationLevels();
					habitat_data.startinglocs.clear();
					for (int i=0;i<Bukkit.getOnlinePlayers().size();i++) {
						Player p = (Player)(Bukkit.getOnlinePlayers().toArray()[i]);
						PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
						pd.hitlist.clear();
					}
					/*
					getServer().broadcastMessage("Thanks for playing on Sig's Minecraft!");
					getServer().broadcastMessage(ChatColor.AQUA+"Check out http://z-gamers.net/mc for update info!");
					getServer().broadcastMessage(" ");
					*/
					//End Advertisements.
					LASTSERVERCHECK=getServerTickTime();
				}
				
				if (Bukkit.getWorld("world").getTime()>=12000) {
					Collection<Player> players = (Collection<Player>) getServer().getOnlinePlayers();
					//Count the number of players sleeping. Compare to "sleepingplayers" count.
					log("[DEBUG] Time: "+Bukkit.getWorld("world").getTime()+" Full Time: "+Bukkit.getWorld("world").getFullTime() + " SERVERTICKTIME: "+getServerTickTime(),4);
					 //This functionality only makes sense when two or more players are on.
					int sleeping=0;
					for (int i=0;i<players.size();i++) {
						if (Iterables.get(players,i).isSleeping()) {
							Iterables.get(players, i).setHealth(Iterables.get(players, i).getMaxHealth());
							sleeping++;
						}
					}
					if (sleepingPlayers!=sleeping) {
						sleepingPlayers=sleeping;
						if (players.size()>1) {
							getServer().broadcastMessage(ChatColor.GOLD+""+sleepingPlayers+" Player"+(sleepingPlayers!=1?"s are":" is")+" in bed "+ChatColor.WHITE+"("+sleepingPlayers+"/"+(players.size()/2)+")");
						}
					}
					if (sleepingPlayers>=Math.max(players.size()/2,1)) {
						//Make it the next day.
						if (players.size()>1) {
							getServer().broadcastMessage(ChatColor.GOLD+"Enough Players sleeping! It's now morning!");
						}
						/*Bukkit.getWorld("world").setFullTime(Bukkit.getWorld("world").getFullTime()+10);
						
						SERVERTICK=getServerTickTime();*/
						long temptime = Bukkit.getWorld("world").getFullTime();
						Bukkit.getWorld("world").setTime(0);
						time_passed+=temptime-Bukkit.getWorld("world").getFullTime();
						Bukkit.getWorld("world").setThundering(false);
						/*
						STARTTIME=Bukkit.getWorld("world").getFullTime();
						LASTSERVERCHECK=getServerTickTime();*/
						//Make sure we keep SERVERTICK in check.
						sleepingPlayers=0;
					}
				}
				
				if (getServerTickTime()-LastClearStructureTime>=100) {
					//Perform a clear of Monster Structure.
					for(UUID id : monsterdata.keySet()) {
						MonsterStructure mon = monsterdata.get(id);
						Monster m = mon.m;
						if (!m.isValid()) {
							Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, 
					            () -> {
									monsterdata.remove(id);
									log("Removed one from Structure",5);
					            }, 1);
						}
						if (mon.isLeader) {
							//Make it glow red.
							GenericFunctions.setGlowing(m, GlowAPI.Color.DARK_RED);
						}
						if (mon.isElite) {
							//Make it glow dark purple.
							GenericFunctions.setGlowing(m, GlowAPI.Color.DARK_PURPLE);
							boolean hasstruct = false;
							for (int i=0;i<elitemonsters.size();i++) {
								if (elitemonsters.get(i).m.equals(m)) {
									hasstruct=true;
								}
							}
							if (!hasstruct) {
								elitemonsters.add(new EliteMonster(m));
							}
						}
					}
				}
				
				//See if each player needs to regenerate their health.
				for (int i=0;i<Bukkit.getOnlinePlayers().size();i++) {
					Player p = (Player)(Bukkit.getOnlinePlayers().toArray()[i]);
					if (!p.isDead()) {
						PlayerStructure pd = (PlayerStructure)playerdata.get(p.getUniqueId());
						log(pd.velocity+"",5);
						if (GenericFunctions.CountDebuffs(p)>pd.debuffcount) {
							ItemStack[] equips = p.getEquipment().getArmorContents();
							double removechance = 0.0;
							log("Debuffcount went up...",5);
							for (int i1=0;i1<equips.length;i1++) {
								if (GenericFunctions.isArtifactEquip(equips[i1])) {
									double resistamt = GenericFunctions.getAbilityValue(ArtifactAbility.STATUS_EFFECT_RESISTANCE, equips[i1]);
									log("Resist amount is "+resistamt,5);
									removechance+=resistamt;
								}
							}
							log("Remove chance is "+removechance,5);
							int longestdur=0;
							PotionEffectType type=null;
							for (int i1=0;i1<p.getActivePotionEffects().size();i1++) {
								if (GenericFunctions.isBadEffect(Iterables.get(p.getActivePotionEffects(), i1).getType()) && Iterables.get(p.getActivePotionEffects(), i1).getDuration()>longestdur) {
									longestdur=Iterables.get(p.getActivePotionEffects(), i1).getDuration();
									type=Iterables.get(p.getActivePotionEffects(), i1).getType();
								}
							}
							if (Math.random()<=removechance/100) {
								p.removePotionEffect(type);
								p.sendMessage(ChatColor.DARK_GRAY+"You successfully resisted the application of "+ChatColor.WHITE+GenericFunctions.CapitalizeFirstLetters(type.getName().replace("_", " ")));
							}
							
						}
						pd.debuffcount=GenericFunctions.CountDebuffs(p);
						
						if (banksessions.containsKey(p.getUniqueId())) {
							//See if it expired.
							BankSession bs = (BankSession)banksessions.get(p.getUniqueId());
							if (bs.isSessionExpired()) {
								banksessions.remove(p.getUniqueId());
							}
						}
						
						/*
						if (GenericFunctions.isRanger(p) &&
								GenericFunctions.getBowMode(p.getEquipment().getItemInMainHand())==BowMode.SNIPE) {
							p.removePotionEffect(PotionEffectType.SLOW);
							p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,20,5));
						}*/
						
						if (TwosideShops.PlayerHasPurchases(p)) {
							TwosideShops.PlayerSendPurchases(p);
						}
						
						if (TwosideShops.IsPlayerUsingTerminal(p) &&
								(TwosideShops.GetSession(p).GetSign().getBlock()==null || TwosideShops.GetSession(p).IsTimeExpired())) {
							p.sendMessage(ChatColor.RED+"Ran out of time! "+ChatColor.WHITE+"Shop session closed.");
							TwosideShops.RemoveSession(p);
						}
	
						pd.highwinder=ArtifactAbility.containsEnchantment(ArtifactAbility.HIGHWINDER, p.getEquipment().getItemInMainHand());
						if (pd.highwinder) {
							pd.highwinderdmg=GenericFunctions.getAbilityValue(ArtifactAbility.HIGHWINDER, p.getEquipment().getItemInMainHand());
						}
						if (93.182445*pd.velocity>4.317) {
							pd.velocity/=2;
						} else {
							pd.velocity=0;
						}
						if (pd.highwinder && pd.target!=null && !pd.target.isDead()) {
							aPlugin.API.sendActionBarMessage(p, drawVelocityBar(pd.velocity,pd.highwinderdmg));
						}
	    				if (pd.target!=null && !pd.target.isDead() && pd.target.getLocation().getWorld().equals(p.getWorld()) && pd.target.getLocation().distanceSquared(p.getLocation())>256) {
	    					pd.target=null;
	    				}
	
	    				p.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(20*(1.0d-NewCombat.CalculateDamageReduction(1,p,p))+subtractVanillaArmorBar(p.getEquipment().getArmorContents()));
	    				
						if (pd.last_regen_time+HEALTH_REGENERATION_RATE<=getServerTickTime()) {
							pd.last_regen_time=getServerTickTime();
							//See if this player needs to be healed.
							if (p!=null &&
									!p.isDead() && //Um, don't heal them if they're dead...That's just weird.
									p.getHealth()<p.getMaxHealth() &&
									p.getFoodLevel()>=16) {
								
								double totalregen = 1+(p.getMaxHealth()*0.05);
								ItemStack[] equips = p.getEquipment().getArmorContents();
								double bonusregen = 0.0;
								for (int i1=0;i1<equips.length;i1++) {
									if (GenericFunctions.isArtifactEquip(equips[i1])) {
										double regenamt = GenericFunctions.getAbilityValue(ArtifactAbility.HEALTH_REGEN, equips[i1]);
										 bonusregen += regenamt;
										 log("Bonus regen increased by "+regenamt,5);
										 
									}
								}
								
								
								totalregen += bonusregen;
								
								for (int i1=0;i1<equips.length;i1++) {
									if (ArtifactAbility.containsEnchantment(ArtifactAbility.GREED, equips[i1])) {
										totalregen /= ArtifactAbility.containsEnchantment(ArtifactAbility.GREED, equips[i1])?2:1;
									}
								}
								if (ArtifactAbility.containsEnchantment(ArtifactAbility.GREED, p.getEquipment().getItemInMainHand())) {
									totalregen /= ArtifactAbility.containsEnchantment(ArtifactAbility.GREED, p.getEquipment().getItemInMainHand())?2:1;
								}
								
								p.setHealth((p.getHealth()+totalregen>p.getMaxHealth())?p.getMaxHealth():p.getHealth()+totalregen);
								
							}
						}
						//See if this player is sleeping.
						if (p.isSleeping()) {
							p.setHealth(Bukkit.getPlayer(pd.name).getMaxHealth()); //Heals the player fully when sleeping.
						}
						//We need to see if this player's damage reduction has changed recently. If so, notify them.
						//Check damage reduction by sending an artifical "1" damage to the player.
						if (!p.isDead()) {log("Player is not dead.",5); setPlayerMaxHealth(p);}
						p.getScoreboard().getTeam(p.getName().toLowerCase()).setSuffix(createHealthbar(((p.getHealth())/p.getMaxHealth())*100,p));
						p.getScoreboard().getTeam(p.getName().toLowerCase()).setPrefix(GenericFunctions.PlayerModePrefix(p));
						/*double old_weapondmg = pd.prev_weapondmg;
						double old_buffdmg = pd.prev_buffdmg;
						double old_partydmg = pd.prev_partydmg;
						double old_armordef = pd.prev_armordef;
						double store1=CalculateDamageReduction(1,p,p);
	
						double store2=old_weapondmg;
						if (GenericFunctions.isWeapon(p.getEquipment().getItemInMainHand())) {
							store2 = CalculateWeaponDamage(p,null);
						}
						if (store1!=pd.damagereduction || store2!=pd.damagedealt) {
							log("Values: "+old_weapondmg+","
									+old_buffdmg+","
									+old_partydmg+","
									+old_armordef+"::"+pd.prev_weapondmg+","
											+pd.prev_buffdmg+","
											+pd.prev_partydmg+","
											+pd.prev_armordef,5);
							pd.damagereduction = store1;
							pd.damagedealt = store2;
							DecimalFormat df = new DecimalFormat("0.0");
							if (((old_weapondmg != pd.prev_weapondmg && GenericFunctions.isWeapon(p.getEquipment().getItemInMainHand())) ||
									(old_armordef != pd.prev_armordef))
									 && old_partydmg == pd.prev_partydmg && old_buffdmg == pd.prev_buffdmg) {
								p.sendMessage(ChatColor.GRAY+""+ChatColor.ITALIC+"Base Damage: "+ChatColor.RESET+""+ChatColor.DARK_PURPLE+df.format(pd.damagedealt)+"  "+ChatColor.GRAY+ChatColor.ITALIC+"Damage Reduction: "+ChatColor.RESET+""+ChatColor.DARK_AQUA+Math.round((1.0-pd.damagereduction)*100)+"%");
							}
						}*/
						for (int i3=0;i3<p.getEquipment().getArmorContents().length;i3++) {
							if (ArtifactAbility.containsEnchantment(ArtifactAbility.SHADOWWALKER, p.getEquipment().getArmorContents()[i3]) &&
									p.isOnGround() && p.getLocation().getY()>=0 && p.getLocation().getY()<=255 && p.getLocation().add(0,0,0).getBlock().getLightLevel()<=4) {
								p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,20,1));
							}
						}
						if (ArtifactAbility.containsEnchantment(ArtifactAbility.SHADOWWALKER, p.getEquipment().getItemInMainHand()) &&
								p.isOnGround() && p.getLocation().getY()>=0 && p.getLocation().getY()<=255 && p.getLocation().add(0,0,0).getBlock().getLightLevel()<=4) {
							p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,20,1));
							//log("Apply speed. The light level here is "+p.getLocation().add(0,-1,0).getBlock().getLightLevel(),2);
						}
						
						if (ArtifactAbility.containsEnchantment(ArtifactAbility.COMBO, p.getEquipment().getItemInMainHand()) &&
								pd.last_swordhit+40<getServerTickTime()) {
							pd.swordcombo=0; //Reset the sword combo meter since the time limit expired.
						}
						
						GenericFunctions.AutoRepairItems(p);
						
						//Try to fit into an already existing party.
						boolean inParty=false;
						for (int j=0;j<PartyList.size();j++) {
							if (!PartyList.get(j).IsInParty(p) &&
									PartyList.get(j).IsInSameRegion(p)) {
								PartyList.get(j).addPlayer(p);
								inParty=true;
							} else
							if (PartyList.get(j).IsInParty(p) &&
								PartyList.get(j).IsInSameRegion(p)) {
								inParty=true;
								//Do party cleanups.
							}
						}
						
						//Alright, none exist. Try to make a new party.
						if (!inParty) {
							//Find an available color.
							int coloravailable=-1;
							for (int j=0;j<15;j++) {
								if (!colors_used.contains(j+1)) {
									coloravailable=j;
									break;
								}
							}
							if (coloravailable==-1) {
								coloravailable=15;
							}
							Party part = new Party(coloravailable+1,p.getLocation());
							part.addPlayer(p);
							PartyList.add(part);
							colors_used.add(coloravailable+1);
							log("Add Party color "+(coloravailable+1),5);
							//TeamCounter++;
						}
					}
				}
				
				for (int j=0;j<PartyList.size();j++) {
					PartyList.get(j).RemoveStrayMembers();
					if (PartyList.get(j).PlayerCountInParty()==0) {
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "scoreboard objectives remove Party"+PartyList.get(j).TeamNumber());
						for (int l=0;l<colors_used.size();l++) {
							if (colors_used.get(l)==PartyList.get(j).TeamNumber()) {
								log("Remove Party color "+colors_used.get(l),5);
								colors_used.remove(l);
								l--;
								break;
							}
						}

						PartyList.remove(j);
						j--;
					} else {
						PartyList.get(j).sortPlayers();
					}
				}
				
				TwosideSpleefGames.TickEvent();
			}

			private void sendAllLoggedMessagesToSpam() {
				StringBuilder finalstring = new StringBuilder();
				for (int i=0;i<TwosideKeeper.log_messages.size();i++) {
					finalstring.append(TwosideKeeper.log_messages.get(i)+"\n");
				}
				TwosideKeeper.log_messages.clear();
				if (finalstring.length()>0) {
					DiscordMessageSender.sendToSpam(finalstring.toString());
				}
			}

			private double subtractVanillaArmorBar(ItemStack[] armorContents) {
				double lostamt = 0.0d;
				for (int i=0;i<armorContents.length;i++) {
					ItemStack equip = armorContents[i];
					if (equip!=null &&
							equip.getType()!=Material.AIR) {
						switch (equip.getType()) {
							case LEATHER_HELMET:{
								lostamt-=1;
							}break;
							case LEATHER_CHESTPLATE:{
								lostamt-=3;
							}break;
							case LEATHER_LEGGINGS:{
								lostamt-=2;
							}break;
							case LEATHER_BOOTS:{
								lostamt-=1;
							}break;
							case GOLD_HELMET:{
								lostamt-=2;
							}break;
							case GOLD_CHESTPLATE:{
								lostamt-=5;
							}break;
							case GOLD_LEGGINGS:{
								lostamt-=3;
							}break;
							case GOLD_BOOTS:{
								lostamt-=1;
							}break;
							case CHAINMAIL_HELMET:{
								lostamt-=2;
							}break;
							case CHAINMAIL_CHESTPLATE:{
								lostamt-=5;
							}break;
							case CHAINMAIL_LEGGINGS:{
								lostamt-=4;
							}break;
							case CHAINMAIL_BOOTS:{
								lostamt-=1;
							}break;
							case IRON_HELMET:{
								lostamt-=2;
							}break;
							case IRON_CHESTPLATE:{
								lostamt-=6;
							}break;
							case IRON_LEGGINGS:{
								lostamt-=5;
							}break;
							case IRON_BOOTS:{
								lostamt-=2;
							}break;
							case DIAMOND_HELMET:{
								lostamt-=3;
							}break;
							case DIAMOND_CHESTPLATE:{
								lostamt-=8;
							}break;
							case DIAMOND_LEGGINGS:{
								lostamt-=6;
							}break;
							case DIAMOND_BOOTS:{
								lostamt-=3;
							}break;
						}
					}
				}
				return lostamt;
			}
		}, 20l, 20l);
    }
	
    @Override
    public void onDisable() {
        // TODO Insert logic to be performed when the plugin is disabled
    	//Clear out remaining parties.
    	for (int i=0;i<TeamCounter;i++) {
    		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "scoreboard objectives remove Party"+i);
    	}
    	saveOurData(); //Saves all of our server variables and closes down.
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	if (cmd.getName().equalsIgnoreCase("log")) {
    		LOGGING_LEVEL = (LOGGING_LEVEL+1) % 6;
    		sender.sendMessage("Debugging Log Level is now "+ChatColor.RED+LOGGING_LEVEL+".");
    		return true;
    	} else
		if (cmd.getName().equalsIgnoreCase("servertype")) {
			if (args.length==1) {
				if (ServerType.valueOf(args[0].toUpperCase())!=null) {
					SERVER_TYPE = ServerType.valueOf(args[0].toUpperCase());
					return true;
				}
			} else {
				sender.sendMessage("Wrong arguments!");
			}
		} else
		if (cmd.getName().equalsIgnoreCase("ess")) {
			sender.sendMessage(EssenceLogger.GenerateReport());
			return true; 
		} else 
		if (cmd.getName().equalsIgnoreCase("bow")) {
			sender.sendMessage(BowLogger.GenerateReport());
			return true;
		} else 
		if (cmd.getName().equalsIgnoreCase("loot")) {
			sender.sendMessage(Loot_Logger.GenerateReport());
			return true;
		} else 
		if (cmd.getName().equalsIgnoreCase("stats")) {
			if (args.length>=1) {
    			if (Bukkit.getPlayer(args[0])!=null) {
    				//If we can grab their stats, then calculate it.
    				Player p = Bukkit.getPlayer(args[0]);
    				sender.sendMessage("Displaying stats for "+ChatColor.YELLOW+p.getName());
    				showPlayerStats(p,sender);
    			} else {
    				sender.sendMessage("Player "+ChatColor.YELLOW+args[0]+" is not online!");
    			}
				return true;
			} else {
				showPlayerStats((Player)sender);
				return true;
			}
		} else 
    	if (sender instanceof Player) {
			DecimalFormat df = new DecimalFormat("0.00");
	    	if (cmd.getName().equalsIgnoreCase("fix")) {
    			Player p = (Player)sender;
    			if (Artifact.isMalleableBase(p.getEquipment().getItemInMainHand()) &&
    					MalleableBaseQuest.getTimeStarted(p.getEquipment().getItemInMainHand())<=213747510) {
    				p.getEquipment().setItemInMainHand(MalleableBaseQuest.setTimeStarted(p.getEquipment().getItemInMainHand(), getServerTickTime()));
    			}
    			if (GenericFunctions.isArtifactEquip(p.getEquipment().getItemInMainHand()) &&
    					p.getEquipment().getItemInMainHand().getEnchantmentLevel(Enchantment.LUCK)<=3) {
    				//Change the tool to Stone. If possible.
    				if (p.getEquipment().getItemInMainHand().getType().toString().contains("SWORD")) {
    					p.getEquipment().getItemInMainHand().setType(Material.STONE_SWORD);
    				}else
    				if (p.getEquipment().getItemInMainHand().getType().toString().contains("PICKAXE")) {
    					p.getEquipment().getItemInMainHand().setType(Material.STONE_PICKAXE);
    				}else
    				if (p.getEquipment().getItemInMainHand().getType().toString().contains("AXE")) {
    					p.getEquipment().getItemInMainHand().setType(Material.STONE_AXE);
    				}else
    				if (p.getEquipment().getItemInMainHand().getType().toString().contains("HOE")) {
    					p.getEquipment().getItemInMainHand().setType(Material.STONE_HOE);
    				}else
    				if (p.getEquipment().getItemInMainHand().getType().toString().contains("SPADE")) {
    					p.getEquipment().getItemInMainHand().setType(Material.STONE_SPADE);
    				}
    			}
    			if (p.getLocation().add(0,0,0).getBlock().getType()==Material.PISTON_MOVING_PIECE) {
    				p.getLocation().add(0,0,0).getBlock().setType(Material.AIR);
    			}
    			if (SERVER_TYPE==ServerType.TEST || SERVER_TYPE==ServerType.QUIET) {
    				Monster m = MonsterController.convertMonster((Monster)p.getWorld().spawnEntity(p.getLocation(),EntityType.ZOMBIE), MonsterDifficulty.ELITE);
    				m.setHealth(m.getMaxHealth()/16d);
    				//TwosideKeeperAPI.spawnAdjustedMonster(MonsterType.GIANT, p.getLocation());
    				//TwosideKeeper.log("This is from set "+ItemSet.GetSet(p.getEquipment().getItemInMainHand())+" T"+ItemSet.GetTier(p.getEquipment().getItemInMainHand()),2);
    				/*Skeleton s = (Skeleton)p.getWorld().spawnEntity(p.getLocation(), EntityType.SKELETON);
    				Horse h = (Horse)p.getWorld().spawnEntity(p.getLocation(), EntityType.HORSE);
    				
    				s.setSkeletonType(SkeletonType.NORMAL);
    				s.getEquipment().setItemInMainHand(new ItemStack(Material.IRON_SWORD));
    				h.setVariant(Variant.SKELETON_HORSE);
    				h.setAdult(); 
    				h.setTamed(true);
    				h.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.385f);
    				h.getInventory().setArmor(new ItemStack(Material.DIAMOND_BARDING));
    				h.getInventory().setItem(0, new ItemStack(Material.DIAMOND_BARDING));
    				h.setPassenger(s);*/
    				
        			//Arrow newar = p.getWorld().spawnArrow(p.getLocation(), p.getLocation().getDirection(), 1f, 12f);
    				//GenericFunctions.setBowMode(p.getEquipment().getItemInMainHand(), BowMode.SNIPE);
    				//p.sendMessage("This is bow mode "+GenericFunctions.getBowMode(p.getEquipment().getItemInMainHand()));
    	    		/*for (int i=0;i<p.getEquipment().getArmorContents().length;i++) {
    	    			if (GenericFunctions.isArtifactEquip(p.getEquipment().getArmorContents()[i]) &&
    	        				GenericFunctions.isArtifactArmor(p.getEquipment().getArmorContents()[i])) {
    	    				AwakenedArtifact.addPotentialEXP(p.getEquipment().getArmorContents()[i], 500, p);
    	    			}
    	    		}
        			ItemStack item = p.getEquipment().getItemInMainHand();
        			AwakenedArtifact.addPotentialEXP(item, 50000, p);*/
        			//p.sendMessage(tpstracker.getTPS()+"");
        			//GenericFunctions.addObscureHardenedItemBreaks(p.getEquipment().getItemInMainHand(), 4);
    			}
	    		return true;
	    	} else
	    	if (cmd.getName().equalsIgnoreCase("money")) {
	    		sender.sendMessage("You are currently holding "+ChatColor.GREEN+"$"+df.format(getPlayerMoney(Bukkit.getPlayer(sender.getName()))));
	    		return true;
	    	} else 
    		if (cmd.getName().equalsIgnoreCase("enchant_advanced")) {
	    		//Make sure we are holding an item.
    			Player p = (Player)sender;
    			if (p.getEquipment().getItemInMainHand()!=null) {
    				//sender.sendMessage(args[0]);
    				if (args.length==2) {
    					p.getEquipment().getItemInMainHand().addUnsafeEnchantment(Enchantment.getById(Integer.parseInt(args[0])), Integer.parseInt(args[1]));
        				sender.sendMessage("Enchantment applied!");
        	    		return true;
    				} else {
    					sender.sendMessage("Wrong arguments!");
    				}
    			} else {
    				sender.sendMessage("Cannot enchant nothing!");
    	    		return true;
    			}
	    	} else 
    		if (cmd.getName().equalsIgnoreCase("harden_armor")) {
	    		//sender.sendMessage("You are currently holding "+ChatColor.GREEN+"$"+getPlayerMoney((Player)sender));
    			Player p = (Player)sender;
    			if (p.getEquipment().getItemInMainHand()!=null) {
    				if (args.length==1) {
	    				ItemStack item = p.getEquipment().getItemInMainHand();
    					ItemMeta meta = item.getItemMeta();
    					if (meta.hasLore()) {
    						List<String> lore = meta.getLore();
    						lore.add(ChatColor.GRAY+"Breaks Remaining: "+ChatColor.YELLOW+args[0]);
        					meta.setLore(lore);
    					} else {
    						List<String> lore = new ArrayList<String>();
    						lore.add(ChatColor.GRAY+"Breaks Remaining: "+ChatColor.YELLOW+args[0]);
        					meta.setLore(lore);
        					if (lore.size()>=1) {
            					sender.sendMessage("Updated Lore.");
        					}
    					}
    					item.setItemMeta(meta);
	    				p.getEquipment().setItemInMainHand(item);
	    	    		return true;
    				} else {
    					sender.sendMessage("Wrong arguments!");
    				}
    			} else {
    				sender.sendMessage("Cannot harden nothing!");
    	    		return true;
    			}
	    	} else 
    		if (cmd.getName().equalsIgnoreCase("item_cube")) {
	    		//sender.sendMessage("You are currently holding "+ChatColor.GREEN+"$"+getPlayerMoney((Player)sender));
	    		//sender.sendMessage("You are currently holding "+ChatColor.GREEN+"$"+getPlayerMoney((Player)sender));
    			Player p = (Player)sender;
    			if (p.getEquipment().getItemInMainHand()!=null) {
    				if (args.length==1) {
	    				ItemStack item = p.getEquipment().getItemInMainHand();
	    					ItemMeta meta = item.getItemMeta();
	    					if (meta.hasLore()) {
	    						List<String> lore = meta.getLore();
	    						lore.clear();
	    						lore.add(" ");
	    						lore.add(" ");
	    						lore.add(" ");
	    						lore.add(ChatColor.DARK_PURPLE+"ID#"+args[0]);
	        					meta.setLore(lore);
	    					} else {
	    						List<String> lore = new ArrayList<String>();
	    						lore.clear();
	    						lore.add(" ");
	    						lore.add(" ");
	    						lore.add(" ");
	    						lore.add(ChatColor.DARK_PURPLE+"ID#"+args[0]);
	        					meta.setLore(lore);
		    				}
	    				item.setItemMeta(meta);
	    				p.getEquipment().setItemInMainHand(item);
	    	    		return true;
    				} else {
    					sender.sendMessage("Wrong arguments!");
    				}
    			} else {
    				sender.sendMessage("Cannot convert nothing!");
    	    		return true;
    			}
	    	} else 
    		if (cmd.getName().equalsIgnoreCase("artifact")) {
    			Player p = (Player)sender;
    			if (args.length==2) {
    				ItemStack newartifact = Artifact.createArtifactItem(ArtifactItem.valueOf(args[0]));
    				newartifact.setAmount(Integer.parseInt((args[1])));
    				p.getInventory().addItem(newartifact);
    				return true;
				} else 
				if (args.length==1) {
    				p.getInventory().addItem(Artifact.createArtifactItem(ArtifactItem.valueOf(args[0])));
    				return true;
				} else {
					sender.sendMessage("Wrong arguments!");
				}
    		} else 
    		if (cmd.getName().equalsIgnoreCase("recyclingcenter")) {
    			Player p = (Player)sender;
				TwosideRecyclingCenter.setChoosingRecyclingCenter(!TwosideRecyclingCenter.isChoosingRecyclingCenter());
				if (TwosideRecyclingCenter.isChoosingRecyclingCenter()) {
					p.sendMessage(ChatColor.GREEN+"Click on a Chest to set up a new Recycling Center Node.");
				} else {
					p.sendMessage(ChatColor.RED+"Cancelled Recycling Center selection mode.");
				}
				return true;
    		} else 
    		if (cmd.getName().equalsIgnoreCase("sound")) {
    			Player p = (Player)sender;
    			PlayerStructure pd = (PlayerStructure)playerdata.get(p.getUniqueId());
				pd.sounds_enabled=!pd.sounds_enabled;
				if (pd.sounds_enabled) {
					p.sendMessage(ChatColor.DARK_AQUA+"Chat sounds are now enabled.");
				} else {
					p.sendMessage(ChatColor.DARK_RED+"Chat sounds are now disabled.");
				}
				return true;
    		} else 
    		if (cmd.getName().equalsIgnoreCase("tp_world")) {
				if (args.length==4) {
	    			Player p = (Player)sender;
	    			p.teleport(new Location(Bukkit.getWorld(args[0]),Double.parseDouble(args[1]),Double.parseDouble(args[2]),Double.parseDouble(args[3])));
    				return true;
				} else {
					sender.sendMessage("Wrong arguments!");
				}
    			return true;
    		} else 
    		if (cmd.getName().equalsIgnoreCase("awakenedartifact")) {
				if (args.length==2 && args[0].equalsIgnoreCase("menu")) {
    				Player p = Bukkit.getPlayer(sender.getName());
	    			if (Integer.parseInt(args[1])>=900) {
	    				if (p.getInventory().getArmorContents()[Integer.parseInt(args[1])-900]!=null) {
	    					p.spigot().sendMessage(ArtifactAbility.GenerateMenu(ArtifactItemType.getArtifactItemTypeFromItemStack(p.getInventory().getArmorContents()[Integer.parseInt(args[1])-900]).getUpgradePath(), NewCombat.CalculateWeaponDamage(p, null), p.getInventory().getArmorContents()[Integer.parseInt(args[1])-900],Integer.parseInt(args[1])));
	    				}
	    			} else {
	    				if (p.getEquipment().getItemInMainHand()!=null && GenericFunctions.isArtifactEquip(p.getEquipment().getItemInMainHand())) {
	    					p.spigot().sendMessage(ArtifactAbility.GenerateMenu(ArtifactItemType.getArtifactItemTypeFromItemStack(p.getEquipment().getItemInMainHand()).getUpgradePath(), NewCombat.CalculateWeaponDamage(p, null), p.getEquipment().getItemInMainHand()));
	    				}
	    			}
				} else
				if (args.length==2 && args[0].equalsIgnoreCase("levelup")) {
	    			Player p = (Player)sender;
	    			//Argument0 is "levelup"
	    			//Argument1 is the enum of the ability.
	    			//See if we can level this item up!
    				//Upgrade!
    				ArtifactAbility.upgradeEnchantment(p,p.getEquipment().getItemInMainHand(),ArtifactAbility.valueOf(args[1]));
				} else
				if (args.length==3 && args[0].equalsIgnoreCase("levelup")) { 
					//argument2 is the equip slot to apply it to.
	    			Player p = (Player)sender;
	    			if (Integer.parseInt(args[2])>=900) {
	    				ArtifactAbility.upgradeEnchantment(p,p.getInventory().getArmorContents()[Integer.parseInt(args[2])-900],ArtifactAbility.valueOf(args[1]));
	    			} else {
	    				ArtifactAbility.upgradeEnchantment(p,p.getInventory().getItem(Integer.parseInt(args[2])),ArtifactAbility.valueOf(args[1]));
	    			}
				} else {
					//Display the generic levelup message.
    				Player p = Bukkit.getPlayer(sender.getName());
    				if (p.getEquipment().getItemInMainHand()!=null &&
    						p.getEquipment().getItemInMainHand().getType()!=Material.AIR
    						&& GenericFunctions.isArtifactEquip(p.getEquipment().getItemInMainHand())) {
	    				p.sendMessage("");p.sendMessage("");
						p.spigot().sendMessage(ArtifactAbility.GenerateMenu(ArtifactItemType.getArtifactItemTypeFromItemStack(p.getEquipment().getItemInMainHand()).getUpgradePath(), NewCombat.CalculateDamageReduction(1,p,p), p.getEquipment().getItemInMainHand()));
    				}
				}
    			return true;
    		} else 
    		if (cmd.getName().equalsIgnoreCase("awakenedartifact_ability")) {
				if (args.length==2) {
	    			Player p = (Player)sender;
	    			p.getEquipment().setItemInMainHand(ArtifactAbility.applyEnchantment(ArtifactAbility.valueOf(args[0]), Integer.parseInt(args[1]), p.getEquipment().getItemInMainHand()));
	    			return true;
				} else {
					sender.sendMessage("Wrong arguments!");
				}
    		} else 
    		if (cmd.getName().equalsIgnoreCase("mode")) {
				if (args.length==1) {
					sender.sendMessage(GenericFunctions.PlayerModeInformation(args[0]));
	    			return true;
				} else {
					sender.sendMessage("Wrong arguments!");
				}
    		} else 
    		if (cmd.getName().equalsIgnoreCase("dps")) {
    			PlayerStructure pd = PlayerStructure.GetPlayerStructure((Player)sender);
    			if (pd.damagelogging) {
    				((Player)sender).sendMessage(pd.damagedata.OutputResults());
    			} else {
    				pd.damagedata.startRecording();
    				((Player)sender).sendMessage("Damage tracking has begun...");
    			}
    			pd.damagelogging=!pd.damagelogging;
    			return true;
    		} else 
    		if (cmd.getName().equalsIgnoreCase("muchlogsmuchwow")) {
    			Player p = (Player)sender;
    			for (int i=0;i<64;i++) {
    				Item it = p.getWorld().dropItemNaturally(p.getLocation(), new ItemStack(Material.LOG,64,(short)3));
    				it.setPickupDelay(0);
    			}
    			return true;
    		} else 
    		if (cmd.getName().equalsIgnoreCase("make_set_item")) {
    			Player p = (Player)sender;
    			List<String> lore = new ArrayList<String>();
    			if (p.getEquipment().getItemInMainHand().getItemMeta().hasLore()) {
    				lore = p.getEquipment().getItemInMainHand().getItemMeta().getLore();
    			}
    			lore.add(ChatColor.GOLD+""+ChatColor.BOLD+"T"+Integer.parseInt(args[1])+" "+ItemSet.valueOf(args[0])+" Set");
    			ItemMeta m = p.getEquipment().getItemInMainHand().getItemMeta();
    			m.setLore(lore);
    			p.getEquipment().getItemInMainHand().setItemMeta(m);
    			return true;
    		}
    			
    	} else {
    		//Implement console/admin version later (Let's you check any name's money.)
    	}
    	return false; 
    }

    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void onPlayerCommand(PlayerCommandPreprocessEvent ev) {
    	log("  "+ChatColor.DARK_GRAY+ev.getPlayer().getName()+" is Executing Command: "+ChatColor.GOLD+ev.getMessage(),3);
    	if (ev.getMessage().contains("/time")) {
    		//Attempt to set the time difference.

			long temptime = Bukkit.getWorld("world").getFullTime();
	    	Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
				@Override
				public void run() {
					time_passed+=temptime-Bukkit.getWorld("world").getFullTime();
				}},1);
    	}
    }
    
    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void onServerCommand(ServerCommandEvent ev) {
    	log(ev.getSender().getName()+" is Executing Command: "+ev.getCommand(),3);
    	String msg = "";
    	if (ev.getCommand().equalsIgnoreCase("stop") || ev.getCommand().equalsIgnoreCase("restart")) {
    		msg = "Server is shutting down...";
    	}/* else
    	if (ev.getCommand().equalsIgnoreCase("reload")) {
    		msg = "Server plugins have been reloaded.";
    	}*/ //
    	if (!msg.equalsIgnoreCase("")) {
        	if (SERVER_TYPE==ServerType.MAIN) {
        		DiscordMessageSender.sendItalicizedRawMessageDiscord(SERVER_TYPE.GetServerName()+msg);
        	}
			Bukkit.broadcastMessage(msg);
    	}
    }
    
    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void onWorldSave(WorldSaveEvent ev) {
    	if (ev.getWorld().getName().equalsIgnoreCase("world")) {
    		saveOurData();
    	}
    }
    
    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent ev) {
    	
    	//Remove stray members from the player's party.

		for (int j=0;j<PartyList.size();j++) {
			PartyList.get(j).RemoveStrayMembers();
		}
    	
    	for (int i=0;i<Bukkit.getOnlinePlayers().toArray().length;i++) {
    		Player p = (Player)Bukkit.getOnlinePlayers().toArray()[i];
    		if (p!=null) {
    			p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 10, 1);
    			//LEAVE: Sound.NOTE_PLING, 8, 0.7f);
    			//MESSAGE: Sound.NOTE_STICKS, 0.6f, 0.85f);
    		}
    	}  
    	
    	if (SERVER_TYPE==ServerType.MAIN && !restarting_server) {
    		Bukkit.getScheduler().runTaskAsynchronously(this, pluginupdater);
    	}
    	playerdata.put(ev.getPlayer().getUniqueId(), new PlayerStructure(ev.getPlayer(),getServerTickTime()));
    	log("[TASK] New Player Data has been added. Size of array: "+playerdata.size(),4);
    	
    	GenericFunctions.updateSetItems(ev.getPlayer());
    	
    	//Update player max health. Check equipment too.
    	setPlayerMaxHealth(ev.getPlayer());
    	ev.getPlayer().getScoreboard().getTeam(ev.getPlayer().getName().toLowerCase()).setSuffix(createHealthbar(((ev.getPlayer().getHealth())/ev.getPlayer().getMaxHealth())*100,ev.getPlayer()));
    	ev.getPlayer().getScoreboard().getTeam(ev.getPlayer().getName().toLowerCase()).setPrefix(GenericFunctions.PlayerModePrefix(ev.getPlayer()));
		ev.getPlayer().getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4.0d);
    }
    
    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void onPlayerLeave(PlayerQuitEvent ev) {
    	TwosideSpleefGames.PassEvent(ev);
    	
    	for (int i=0;i<Bukkit.getOnlinePlayers().toArray().length;i++) {
    		Player p = (Player)Bukkit.getOnlinePlayers().toArray()[i];
    		if (p!=null) {
    			p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 8, 0.7f);
    		}
    	}
    	Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			@Override
			public void run() {
		    	if (Bukkit.getOnlinePlayers().size()==0 && restarting_server) {
					Bukkit.savePlayers();
					DiscordMessageSender.sendItalicizedRawMessageDiscord("All players have disconnected. Server is shutting down...");
					for (int i=0;i<Bukkit.getWorlds().size();i++) {
						Bukkit.getWorlds().get(i).save();
					}
					Bukkit.shutdown();
		    	}
			}
		},5);
    	
    	//Find the player that is getting removed.
    	PlayerStructure pd = (PlayerStructure)playerdata.get(ev.getPlayer().getUniqueId());
		//Make sure to save the config for this player.
		pd.saveConfig();
    	playerdata.remove(ev.getPlayer().getUniqueId());
    	log("[TASK] Player Data for "+ev.getPlayer().getName()+" has been removed. Size of array: "+playerdata.size(),4);
    }
    
    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true) 
    public void onPlayerChat(final AsyncPlayerChatEvent ev) {
    	if (ev.getMessage().length()>=1) {
    		//See if we're using a bank terminal.
    		Player thisp = ev.getPlayer();
    		if (banksessions.containsKey(thisp.getUniqueId())) {
    			switch (((BankSession)banksessions.get(ev.getPlayer().getUniqueId())).GetState()) {
	    			case WITHDRAW:{
	    				//See if this message is a number.
		        		if (isNumeric(ev.getMessage())) {
			    			DecimalFormat df = new DecimalFormat("0.00");
			    			Double value=Double.parseDouble(ev.getMessage());
			    			value=Double.parseDouble(df.format(Double.valueOf(value)));
			    			if (value>=0.01) {
			    				if (getPlayerBankMoney(thisp)>=value) {
			    					//Withdraw the money. Credit it to the player.
			    					givePlayerBankMoney(thisp,-value);
			    					givePlayerMoney(thisp,value);
			    					ev.getPlayer().sendMessage(ChatColor.GOLD+"WITHDRAW COMPLETE!");
			    					ev.getPlayer().sendMessage("  Now Holding: "+ChatColor.GREEN+"$"+df.format(getPlayerMoney(ev.getPlayer())));
			    				} else {
			        				thisp.sendMessage(ChatColor.RED+"Your account does not have that much money! You can withdraw a maximum of "+ChatColor.WHITE+"$"+getPlayerBankMoney(thisp));
			        				thisp.sendMessage(ChatColor.WHITE+"  Cancelled out of Withdraw terminal.");
			    				}
			    			} else {
			    				thisp.sendMessage(ChatColor.RED+"You must withdraw at least "+ChatColor.WHITE+"$0.01 or more.");
			    				thisp.sendMessage(ChatColor.WHITE+"  Cancelled out of Withdraw terminal.");
			    			}
		        		} else {
		    				thisp.sendMessage(ChatColor.RED+"Invalid Number!");
		    				thisp.sendMessage(ChatColor.WHITE+"  Cancelled out of Withdraw terminal.");
		        		}
		    			ev.setMessage("");
		    			ev.setCancelled(true);
	    			}break;
	    			case DEPOSIT:{
	    				if (isNumeric(ev.getMessage())) {
			    			DecimalFormat df = new DecimalFormat("0.00");
			    			Double value=Double.parseDouble(ev.getMessage());
			    			value=Double.parseDouble(df.format(Double.valueOf(value)));
			    			if (value>=0.01) {
			    				if (getPlayerMoney(thisp)>=value) {
			    					//Withdraw the money. Credit it to the player.
			    					givePlayerBankMoney(thisp,value);
			    					givePlayerMoney(thisp,-value);
			    					ev.getPlayer().sendMessage(ChatColor.GOLD+"DEPOSIT COMPLETE!");
			    					ev.getPlayer().sendMessage("  Now In Bank: "+ChatColor.BLUE+"$"+df.format(getPlayerBankMoney(ev.getPlayer())));
			    				} else {
			        				thisp.sendMessage(ChatColor.RED+"You are not holding that much money! You can deposit a maximum of "+ChatColor.WHITE+"$"+getPlayerMoney(thisp));
			        				thisp.sendMessage(ChatColor.WHITE+"  Cancelled out of Deposit terminal.");
			    				}
			    			} else {
			    				thisp.sendMessage(ChatColor.RED+"You must deposit at least "+ChatColor.WHITE+"$0.01 or more.");
			    				thisp.sendMessage(ChatColor.WHITE+"  Cancelled out of Deposit terminal.");
			    			}
		        		} else {
		    				thisp.sendMessage(ChatColor.RED+"Invalid Number!");
		    				thisp.sendMessage(ChatColor.WHITE+"  Cancelled out of Deposit terminal.");
		        		}
		    			ev.setMessage("");
		    			ev.setCancelled(true);
	    			}break;
	    			case CONVERT:{if (isNumeric(ev.getMessage()) && isInteger(ev.getMessage())) {
		    			DecimalFormat df = new DecimalFormat("0.00");
		    			Integer value=Integer.parseInt(ev.getMessage());	    			
		    			if (value>=1) {
		    				if (aPlugin.API.getTotalExperience(thisp)>=value) {
		    					double amtgained=value/100d;
		    					givePlayerMoney(thisp,amtgained);
		    					aPlugin.API.setTotalExperience(thisp, aPlugin.API.getTotalExperience(thisp)-value);
		    					ev.getPlayer().sendMessage(ChatColor.GOLD+"CONVERSION COMPLETE!"+ChatColor.WHITE+" Converted "+value+" experience points into "+ChatColor.YELLOW+"$"+df.format(amtgained)+ChatColor.WHITE+".");
		    					ev.getPlayer().sendMessage("  Now Holding: "+ChatColor.BLUE+"$"+df.format(getPlayerMoney(ev.getPlayer())));
		    				} else {
		        				thisp.sendMessage(ChatColor.RED+"You do not have that many experience points. You can convert up to "+ChatColor.WHITE+thisp.getLevel()+ChatColor.RED+" experience points.");
		        				thisp.sendMessage(ChatColor.WHITE+"  Cancelled out of Conversion terminal.");
		    				}
		    			} else {
		    				thisp.sendMessage(ChatColor.RED+"You must convert at least "+ChatColor.WHITE+"1 experience point.");
		    				thisp.sendMessage(ChatColor.WHITE+"  Cancelled out of Conversion terminal.");
		    			}
	        		} else {
	    				thisp.sendMessage(ChatColor.RED+"Invalid Number!");
	    				thisp.sendMessage(ChatColor.WHITE+"  Cancelled out of Conversion terminal.");
	        		}
	    			ev.setMessage("");
	    			ev.setCancelled(true);
	    			}break;
	    			case SIGN_CHECK:{
	    				//See if this message is a number.
		        		if (isNumeric(ev.getMessage())) {
			    			DecimalFormat df = new DecimalFormat("0.00");
			    			Double value=Double.parseDouble(ev.getMessage());
			    			value=Double.parseDouble(df.format(Double.valueOf(value)));
			    			if (value>=0.01) {
		    					//Write the check.
		    					final ItemStack check = ev.getPlayer().getEquipment().getItemInMainHand();
		    					if (Check.isUnsignedBankCheck(check)) {
			    					ev.getPlayer().sendMessage(ChatColor.GOLD+"SIGNING COMPLETE!");
			    					ev.getPlayer().sendMessage(ChatColor.AQUA+"  You have successfully written a check for "+ChatColor.YELLOW+"$"+df.format(value)+ChatColor.WHITE+".");
			    					final ItemStack finalcheck = Check.createSignedBankCheckItem(value, ev.getPlayer().getName());
									Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
										@Override
										public void run() {
					    					if (check.getAmount()>1) {
					    						check.setAmount(check.getAmount()-1);
					    						ev.getPlayer().getLocation().getWorld().dropItem(ev.getPlayer().getLocation(), finalcheck);
					    					} else {
					    						ev.getPlayer().getEquipment().setItemInMainHand(finalcheck);
					    					}
										}
									},1);
		    					} else {
		    						ev.getPlayer().sendMessage(ChatColor.YELLOW+"You are not holding a properly signed check!");
				    				thisp.sendMessage(ChatColor.WHITE+"  Cancelled out of Check signing.");
		    					}
			    			} else {
			    				thisp.sendMessage(ChatColor.RED+"You must sign a check with at least "+ChatColor.WHITE+"$0.01 or more.");
			    				thisp.sendMessage(ChatColor.WHITE+"  Cancelled out of Check signing.");
			    			}
		        		} else {
		    				thisp.sendMessage(ChatColor.RED+"Invalid Number!");
		    				thisp.sendMessage(ChatColor.WHITE+"  Cancelled out of Check signing.");
		        		}
		    			ev.setMessage("");
		    			ev.setCancelled(true);
	    			}break;
    			}
	    		banksessions.remove(ev.getPlayer().getUniqueId());
    		} else
    		if (TwosideShops.IsPlayerUsingTerminal(ev.getPlayer())) {
    			final WorldShopSession current_session = TwosideShops.GetSession(ev.getPlayer());
    			current_session.UpdateTime(); //Make sure our session does not expire.
    			switch (current_session.GetSessionType()) {
					/*case CREATE: //OBSOLETE.
						if (ev.getMessage().length()<=9 && isNumeric(ev.getMessage()) && isInteger(ev.getMessage())) {
							int amt = Integer.parseInt(ev.getMessage());
							if (amt<=GenericFunctions.CountItems(ev.getPlayer(), current_session.getItem()) && amt>0) {
								current_session.SetAmt(amt);
								ev.getPlayer().sendMessage("Input how much each "+ChatColor.GREEN+GenericFunctions.GetItemName(current_session.getItem())+ChatColor.WHITE+" will cost:");
								current_session.SetSession(SessionState.PRICE);
							} else {
								if (amt<=0) {
									ev.getPlayer().sendMessage("You cannot sell a non-existent amount of items.");
									TwosideShops.RemoveSession(ev.getPlayer());
								} else {
									ev.getPlayer().sendMessage("You only have "+GenericFunctions.CountItems(ev.getPlayer(), current_session.getItem())+" of "+ChatColor.GREEN+GenericFunctions.GetItemName(current_session.getItem())+ChatColor.WHITE+". Please try again with a lower amount.");
								}
							}
						} else {
							ev.getPlayer().sendMessage("That is not a valid number!");
							TwosideShops.RemoveSession(ev.getPlayer());
						}
						break;*/
					/*case BUY_CREATE: //OBSOLETE.
						if (ev.getMessage().length()<=9 && isNumeric(ev.getMessage()) && isInteger(ev.getMessage())) {
							int amt = Integer.parseInt(ev.getMessage());
							if (amt>0) {
								current_session.SetAmt(amt);
								ev.getPlayer().sendMessage("Input how much you will pay for each "+ChatColor.GREEN+GenericFunctions.GetItemName(current_session.getItem())+ChatColor.WHITE+":");
								current_session.SetSession(SessionState.BUY_PRICE);
							} else {
								ev.getPlayer().sendMessage("You cannot purchase 0 of an item.");
								TwosideShops.RemoveSession(ev.getPlayer());
							}
						} else {
							ev.getPlayer().sendMessage("That is not a valid number!");
							TwosideShops.RemoveSession(ev.getPlayer());
						}
						break;*/
					case PRICE:
						if (isNumeric(ev.getMessage())) {
							final DecimalFormat df = new DecimalFormat("0.00");
							final double amt = Double.parseDouble(ev.getMessage());
							if (amt>=0.01 && amt<=999999999999.99) {
								ev.getPlayer().sendMessage(ChatColor.DARK_BLUE+"Shop has been successfully created!");
								Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
									@Override
									public void run() {
										WorldShop newshop = TwosideShops.CreateWorldShop(current_session.GetSign(), current_session.getItem(), current_session.getAmt(), Double.parseDouble(df.format(amt)), ev.getPlayer().getName());
										WorldShop.spawnShopItem(current_session.GetSign().getLocation(), newshop);
										TwosideShops.SaveWorldShopData(newshop);
										//RemoveItemAmount(ev.getPlayer(), current_session.getItem(), current_session.getAmt()); //We now handle items via chest.
										TwosideShops.RemoveSession(ev.getPlayer());
									}
								},1);
							} else {
								if (amt>999999999999.99) {
									ev.getPlayer().sendMessage("You cannot sell an item for that ridiculous amount.");
								} else {
									ev.getPlayer().sendMessage("You cannot sell an item for free.");
								}
								TwosideShops.RemoveSession(ev.getPlayer());
							}
						} else {
							ev.getPlayer().sendMessage("That is not a valid number!");
							TwosideShops.RemoveSession(ev.getPlayer());
						}
						break;
					case BUY_PRICE:
						if (isNumeric(ev.getMessage())) {
							final DecimalFormat df = new DecimalFormat("0.00");
							final double amt = Double.parseDouble(ev.getMessage());
							if (amt>=0.01 && amt<=999999999999.99) {
								ev.getPlayer().sendMessage(ChatColor.DARK_BLUE+"Purchase Shop has been successfully created!");
								Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
									@Override
									public void run() {
										WorldShop newshop = TwosideShops.CreateWorldShop(current_session.GetSign(), current_session.getItem(), current_session.getAmt(), Double.parseDouble(df.format(amt)), ev.getPlayer().getName(),true);
										TwosideShops.SaveWorldShopData(newshop);
										WorldShop.spawnShopItem(current_session.GetSign().getLocation(), newshop);
										TwosideShops.RemoveSession(ev.getPlayer());
									}
								},1);
								
							} else {
								if (amt>999999999999.99) {
									ev.getPlayer().sendMessage("You cannot buy an item for that ridiculous amount.");
								} else {
									ev.getPlayer().sendMessage("You cannot buy an item for free.");
								}
								TwosideShops.RemoveSession(ev.getPlayer());
							}
						} else {
							ev.getPlayer().sendMessage("That is not a valid number!");
							TwosideShops.RemoveSession(ev.getPlayer());
						}
						break;
					case EDIT:
						if (ev.getMessage().length()<=9 &&isNumeric(ev.getMessage()) && isInteger(ev.getMessage())) {
							int amt = Integer.parseInt(ev.getMessage());
							DecimalFormat df = new DecimalFormat("0.00");
							WorldShop shop = TwosideShops.LoadWorldShopData(TwosideShops.GetShopID(current_session.GetSign())); 
							if (amt>=0) {
								if (amt<=GenericFunctions.CountItems(ev.getPlayer(), shop.GetItem())) {
									shop.UpdateAmount(shop.GetAmount()+amt);
									RemoveItemAmount(ev.getPlayer(), shop.GetItem(), amt);
									TwosideShops.SaveWorldShopData(shop);
									TwosideShops.UpdateSign(shop, TwosideShops.GetShopID(current_session.GetSign()), current_session.GetSign(),false);
									ev.getPlayer().sendMessage("Added "+ChatColor.AQUA+amt+ChatColor.WHITE+" more "+ChatColor.GREEN+shop.GetItemName()+ChatColor.WHITE+" to your shop!");
									ev.getPlayer().sendMessage("Input how much each "+ChatColor.GREEN+shop.GetItemName()+ChatColor.WHITE+" will cost (Old value - "+ChatColor.YELLOW+"$"+df.format(shop.GetUnitPrice())+ChatColor.WHITE+"):");
									
									current_session.SetSession(SessionState.UPDATE);
								} else {
									if (amt<=0) {
										ev.getPlayer().sendMessage("You cannot add a non-existent amount of items.");
									} else {
										ev.getPlayer().sendMessage("You only have "+GenericFunctions.CountItems(ev.getPlayer(), shop.GetItem())+" of "+ChatColor.GREEN+shop.GetItemName()+ChatColor.WHITE+".");
									}
									TwosideShops.RemoveSession(ev.getPlayer());
								}
							} else {
								if (-amt<=shop.GetAmount()) {
									//Take out these items from the shop.
									amt*=-1;
									shop.UpdateAmount(shop.GetAmount()-amt);
									ItemStack drop = shop.GetItem();
									int dropAmt = amt;
									//ev.getPlayer().getWorld().dropItemNaturally(ev.getPlayer().getLocation(), drop).setPickupDelay(0);
									final Player p = ev.getPlayer();
									while (dropAmt>0) {
										if (dropAmt>shop.GetItem().getMaxStackSize()) {
											drop.setAmount(shop.GetItem().getMaxStackSize());
											final ItemStack dropitem = drop.clone();
											Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
												@Override
												public void run() {
													p.getWorld().dropItem(p.getLocation(), dropitem).setPickupDelay(0);
												}
											},1);
											dropAmt-=shop.GetItem().getMaxStackSize();
										} else {
											drop.setAmount(dropAmt);
											final ItemStack dropitem = drop.clone();
											Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
												@Override
												public void run() {
													p.getWorld().dropItem(p.getLocation(), dropitem).setPickupDelay(0);
												}
											},1);
											dropAmt=0;
										}
									}
									log("Dropped shop item.",5);
									//ev.getPlayer().getWorld().dropItemNaturally(ev.getPlayer().getLocation(), drop).setPickupDelay(0);
									TwosideShops.SaveWorldShopData(shop);
									TwosideShops.UpdateSign(shop, TwosideShops.GetShopID(current_session.GetSign()), current_session.GetSign(),false);
									
									if (shop.GetAmount()>0) {
										current_session.SetSession(SessionState.UPDATE);
										ev.getPlayer().sendMessage("Input how much each "+ChatColor.GREEN+shop.GetItemName()+ChatColor.WHITE+" will cost (Old value - "+ChatColor.YELLOW+"$"+df.format(shop.GetUnitPrice())+ChatColor.WHITE+"):");
									} else {
										ev.getPlayer().sendMessage(ChatColor.DARK_BLUE+"Shop successfully updated!");
										TwosideShops.RemoveSession(ev.getPlayer());
									}
								} else {
									ev.getPlayer().sendMessage("You only have "+shop.GetAmount()+" of "+ChatColor.GREEN+shop.GetItemName()+ChatColor.WHITE+" in the shop. Please try again.");
								}
							}
						} else {
							ev.getPlayer().sendMessage("That is not a valid number!");
							TwosideShops.RemoveSession(ev.getPlayer());
						}
						break;
					/*case BUY_EDIT: //LEGACY CODE.
						if (ev.getMessage().length()<=9 && isNumeric(ev.getMessage()) && isInteger(ev.getMessage())) {
							int amt = Integer.parseInt(ev.getMessage());
							DecimalFormat df = new DecimalFormat("0.00");
							WorldShop shop = TwosideShops.LoadWorldShopData(TwosideShops.GetShopID(current_session.GetSign())); 
							if (amt>=0) { //This means we want to add more to the amount.
								shop.UpdateAmount(shop.GetAmount()+amt);
								TwosideShops.SaveWorldShopData(shop);
								TwosideShops.UpdateSign(shop, TwosideShops.GetShopID(current_session.GetSign()), current_session.GetSign(),true);
								ev.getPlayer().sendMessage("Requested "+ChatColor.AQUA+amt+ChatColor.WHITE+" more "+ChatColor.GREEN+shop.GetItemName()+ChatColor.WHITE+" to your shop!");
								ev.getPlayer().sendMessage("Input how much you will pay for each "+ChatColor.GREEN+shop.GetItemName()+ChatColor.WHITE+" (Old value - "+ChatColor.YELLOW+"$"+df.format(shop.GetUnitPrice())+ChatColor.WHITE+":");
								
								current_session.SetSession(SessionState.BUY_UPDATE);
							} else {
								if (-amt<=shop.GetStoredAmount()) {
									//Take out these items from the shop.
									amt*=-1;
									shop.UpdateStoredAmount(shop.GetStoredAmount()-amt);
									ItemStack drop = shop.GetItem();
									int dropAmt = amt;
									//ev.getPlayer().getWorld().dropItemNaturally(ev.getPlayer().getLocation(), drop).setPickupDelay(0);
									final Player p = ev.getPlayer();
									while (dropAmt>0) {
										if (dropAmt>shop.GetItem().getMaxStackSize()) {
											drop.setAmount(shop.GetItem().getMaxStackSize());
											final ItemStack dropitem = drop.clone();
											Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
												@Override
												public void run() {
													p.getWorld().dropItemNaturally(p.getLocation(), dropitem).setPickupDelay(0);
												}
											},1);
											dropAmt-=shop.GetItem().getMaxStackSize();
										} else {
											drop.setAmount(dropAmt);
											final ItemStack dropitem = drop.clone();
											Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
												@Override
												public void run() {
													p.getWorld().dropItemNaturally(p.getLocation(), dropitem).setPickupDelay(0);
												}
											},1);
											dropAmt=0;
										}
									}
									log("Dropped shop item.",5);
									//ev.getPlayer().getWorld().dropItemNaturally(ev.getPlayer().getLocation(), drop).setPickupDelay(0);
									TwosideShops.SaveWorldShopData(shop);
									TwosideShops.UpdateSign(shop, TwosideShops.GetShopID(current_session.GetSign()), current_session.GetSign(),true);
									
									TwosideShops.RemoveSession(p);
								} else {
									ev.getPlayer().sendMessage("You only have "+shop.GetStoredAmount()+" of "+ChatColor.GREEN+shop.GetItemName()+ChatColor.WHITE+" stored in the shop. Please try again.");
								}
							}
						} else {
							ev.getPlayer().sendMessage("That is not a valid number!");
							TwosideShops.RemoveSession(ev.getPlayer());
						}
						break;*/
					case UPDATE:
						if (isNumeric(ev.getMessage())) {
							double amt = Double.parseDouble(ev.getMessage());
							WorldShop shop = TwosideShops.LoadWorldShopData(TwosideShops.GetShopID(current_session.GetSign())); 
							if (amt>=0.01 && amt<=999999999999.99) {
								shop.UpdateUnitPrice(amt);
								TwosideShops.SaveWorldShopData(shop);
								TwosideShops.UpdateSign(shop, TwosideShops.GetShopID(current_session.GetSign()), current_session.GetSign(),false);
								ev.getPlayer().sendMessage(ChatColor.DARK_BLUE+"Shop successfully updated!");
								TwosideShops.RemoveSession(ev.getPlayer());
							} else {
								if (amt>999999999999.99) {
									ev.getPlayer().sendMessage("You cannot sell an item for that ridiculous amount.");
								} else {
									ev.getPlayer().sendMessage("You cannot sell an item for free.");
								}
								TwosideShops.RemoveSession(ev.getPlayer());
							}
						} else {
							ev.getPlayer().sendMessage("That is not a valid number!");
							TwosideShops.RemoveSession(ev.getPlayer());
						}
						break;
					case BUY_UPDATE:
						if (isNumeric(ev.getMessage())) {
							double amt = Double.parseDouble(ev.getMessage());
							WorldShop shop = TwosideShops.LoadWorldShopData(TwosideShops.GetShopID(current_session.GetSign())); 
							if (amt>=0.01 && amt<=999999999999.99) {
								shop.UpdateUnitPrice(amt);
								TwosideShops.SaveWorldShopData(shop);
								TwosideShops.UpdateSign(shop, TwosideShops.GetShopID(current_session.GetSign()), current_session.GetSign(),true);
								ev.getPlayer().sendMessage(ChatColor.DARK_BLUE+"Shop successfully updated!");
								TwosideShops.RemoveSession(ev.getPlayer());
							} else {
								if (amt>999999999999.99) {
									ev.getPlayer().sendMessage("You cannot buy an item for that ridiculous amount.");
								} else {
									ev.getPlayer().sendMessage("You cannot buy an item for free.");
								}
								TwosideShops.RemoveSession(ev.getPlayer());
							}
						} else {
							ev.getPlayer().sendMessage("That is not a valid number!");
							TwosideShops.RemoveSession(ev.getPlayer());
						}
						break;
					case PURCHASE:
						if (ev.getMessage().length()<=9 && isNumeric(ev.getMessage()) && isInteger(ev.getMessage())) {
							DecimalFormat df = new DecimalFormat("0.00");
							int amt = Integer.parseInt(ev.getMessage());
							if (amt>0) {
								int shopID = TwosideShops.GetShopID(current_session.GetSign());
								WorldShop shop = TwosideShops.LoadWorldShopData(shopID);
								if (amt<=shop.GetAmount()) {
									if (getPlayerMoney(ev.getPlayer())>=amt*shop.GetUnitPrice()) {
										ev.getPlayer().sendMessage(ChatColor.DARK_BLUE+"Successfully bought "+amt+" "+ChatColor.GREEN+shop.GetItemName()+ChatColor.WHITE+"!");
										shop.UpdateAmount(shop.GetAmount()-amt);
										//We have to remove that amount from the chest shop.
										Chest c = (Chest)WorldShop.getBlockShopSignAttachedTo(current_session.GetSign()).getState();
										final Chest cc = c;
										ItemStack shopItem = shop.GetItem();
										int dropAmt = amt;
										while (dropAmt>0) {
											if (dropAmt>shop.GetItem().getMaxStackSize()) {
											shopItem.setAmount(shop.GetItem().getMaxStackSize());
											final ItemStack dropitem = shopItem.clone();
											Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
												@Override
												public void run() {
													ev.getPlayer().getWorld().dropItem(ev.getPlayer().getLocation(), dropitem).setPickupDelay(0);
													cc.getInventory().removeItem(dropitem);
												}
											},1);
											dropAmt-=shop.GetItem().getMaxStackSize();
											} else {
												shopItem.setAmount(dropAmt);
												final ItemStack dropitem = shopItem.clone();
												Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
													@Override
													public void run() {
														ev.getPlayer().getWorld().dropItem(ev.getPlayer().getLocation(), dropitem).setPickupDelay(0);
														cc.getInventory().removeItem(dropitem);
													}
												},1);
												dropAmt=0;
											}
										}
										TwosideShops.UpdateSign(shop, shop.getID(), current_session.GetSign(),false);
										TwosideShops.SaveWorldShopData(shop);
										TwosideShops.AddNewPurchase(shop.GetOwner(), ev.getPlayer(), shop.GetItem(), amt*shop.GetUnitPrice(), amt);
										final int ID = shopID;
										Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
											@Override
											public void run() {
												WorldShop shop = TwosideShops.SetupNextItemShop(TwosideShops.LoadWorldShopData(ID), cc, current_session.GetSign());
												TwosideShops.UpdateSign(shop, shop.getID(), current_session.GetSign(),false);
												TwosideShops.SaveWorldShopData(shop);
											}},1);
										TwosideShops.RemoveSession(ev.getPlayer());
										givePlayerMoney(ev.getPlayer(), -amt*shop.GetUnitPrice());
										if (Bukkit.getPlayer(shop.GetOwner())!=null) {
											givePlayerMoney(Bukkit.getPlayer(shop.GetOwner()), amt*shop.GetUnitPrice());
										} else {
											givePlayerMoney(shop.GetOwner(), amt*shop.GetUnitPrice());
										}
									} else {
										ev.getPlayer().sendMessage("You do not have enough money to buy that many (You can buy "+ChatColor.GREEN+(int)(getPlayerMoney(ev.getPlayer())/shop.GetUnitPrice())+ChatColor.WHITE+" of them)! Please try again.");
									}
								} else {
									ev.getPlayer().sendMessage("There are only "+shop.GetAmount()+" of this item in the shop! Please try again.");
								}
							} else {
								ev.getPlayer().sendMessage(ChatColor.RED+"Decided not to buy anything.");
								TwosideShops.RemoveSession(ev.getPlayer());
							}
						} else {
							ev.getPlayer().sendMessage("That is not a valid number!");
							TwosideShops.RemoveSession(ev.getPlayer());
						}
						break;
					case SELL:
						if (ev.getMessage().length()<=9 && isNumeric(ev.getMessage()) && isInteger(ev.getMessage())) {
							DecimalFormat df = new DecimalFormat("0.00");
							int amt = Integer.parseInt(ev.getMessage());
							if (amt>0) {
								int shopID = TwosideShops.GetShopID(current_session.GetSign());
								WorldShop shop = TwosideShops.LoadWorldShopData(shopID);
								if (amt<=GenericFunctions.CountItems(ev.getPlayer(), shop.GetItem())) { //Make sure we are holding enough of this item.
									//Make sure we are only requesting as many items as the shop owner wants.
									if (amt<=shop.GetAmount()) {
										if (getPlayerBankMoney(shop.GetOwner())>=amt*shop.GetUnitPrice()) {
											ev.getPlayer().sendMessage(ChatColor.DARK_BLUE+"Successfully sold "+amt+" "+ChatColor.GREEN+shop.GetItemName()+ChatColor.WHITE+" and earned "+ChatColor.YELLOW+"$"+df.format(amt*shop.GetUnitPrice())+ChatColor.WHITE+"!");
											shop.UpdateAmount(shop.GetAmount()-amt);
											shop.UpdateStoredAmount(shop.GetStoredAmount()+amt);
											final Chest c = (Chest)WorldShop.getBlockShopSignAttachedTo(current_session.GetSign()).getState();
											ItemStack shopItem = shop.GetItem();
											RemoveItemAmount(ev.getPlayer(),shop.GetItem(),amt);
											//Add it to the chest.
											int amt_to_add = amt;
											while (amt_to_add>0) {
												if (amt_to_add<shop.GetItem().getMaxStackSize()) {
													ItemStack drop = shop.GetItem().clone();
													drop.setAmount(amt_to_add);
													c.getInventory().addItem(drop);
													amt_to_add=0;
												} else {
													ItemStack drop = shop.GetItem().clone();
													drop.setAmount(shop.GetItem().getMaxStackSize());
													c.getInventory().addItem(drop);
													amt_to_add-=shop.GetItem().getMaxStackSize();
												}
											}
											TwosideShops.UpdateSign(shop, shopID, current_session.GetSign(),true);
											TwosideShops.SaveWorldShopData(shop);
											TwosideShops.RemoveSession(ev.getPlayer());
											givePlayerMoney(ev.getPlayer(), amt*shop.GetUnitPrice());
											givePlayerBankMoney(shop.GetOwner(), -amt*shop.GetUnitPrice());
											TwosideShops.AddNewPurchase(shop.GetOwner(), ev.getPlayer(), shop.GetItem(), amt*shop.GetUnitPrice(), amt, false);
										} else {
											ev.getPlayer().sendMessage(ChatColor.LIGHT_PURPLE+shop.GetOwner()+ChatColor.WHITE+" only has enough money in their bank to buy "+ChatColor.GREEN+(int)(getPlayerBankMoney(shop.GetOwner())/shop.GetUnitPrice())+ChatColor.WHITE+" of "+ChatColor.GREEN+shop.GetItemName()+ChatColor.WHITE+"! Please try again.");
										}
									} else {
										ev.getPlayer().sendMessage("The shop owner is only requesting "+ChatColor.GREEN+shop.GetAmount()+ChatColor.WHITE+" of "+ChatColor.GREEN+shop.GetItemName()+ChatColor.WHITE+"! Please try again.");
									}
								} else {
									ev.getPlayer().sendMessage("You are not holding that many "+ChatColor.GREEN+shop.GetItemName()+ChatColor.WHITE+"! Please try again.");
								}
							} else {
								ev.getPlayer().sendMessage(ChatColor.RED+"Decided not to sell.");
								TwosideShops.RemoveSession(ev.getPlayer());
							}
						} else {
							ev.getPlayer().sendMessage("That is not a valid number!");
							TwosideShops.RemoveSession(ev.getPlayer());
						}
						break; 
					default:
						break;
    			}
    			ev.setMessage("");
    			ev.setCancelled(true);
    		} else
    		{
    			if (ev.getMessage().equalsIgnoreCase("()") || ev.getMessage().indexOf(" ()")>-1 || (ev.getMessage().indexOf("() ")>-1 && ev.getMessage().indexOf("() ")==0)) {
    				ev.setMessage(ev.getMessage().replace("()", "("+ev.getPlayer().getLocation().getBlockX()+","+ev.getPlayer().getLocation().getBlockY()+","+ev.getPlayer().getLocation().getBlockZ()+")"));
    			}
		    	playMessageNotification(ev.getPlayer());
	    		int pos = -1;
	    		log(ev.getMessage()+" "+ev.getMessage().indexOf(" []"),5);
	    		if (ev.getMessage().equalsIgnoreCase("[]") || ev.getMessage().indexOf(" []")>-1 || (ev.getMessage().indexOf("[] ")>-1 && ev.getMessage().indexOf("[] ")==0)) {
	    			pos = ev.getMessage().indexOf("[]");
	    			ev.setMessage(ev.getMessage().replace("[]", ""));
	        		log("pos is "+pos+" message is: {"+ev.getMessage()+"}",5);
	        		DiscordMessageSender.sendRawMessageDiscord(("**"+ev.getPlayer().getName()+"** "+ev.getMessage().substring(0, pos)+"**["+ChatColor.stripColor(GenericFunctions.GetItemName(ev.getPlayer().getEquipment().getItemInMainHand()))+((ev.getPlayer().getEquipment().getItemInMainHand().getAmount()>1)?" x"+ev.getPlayer().getEquipment().getItemInMainHand().getAmount():"")+"]**"+"\n```"+WorldShop.GetItemInfo(ev.getPlayer().getEquipment().getItemInMainHand())+" ```\n"+ev.getMessage().substring(pos)));
	    			Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"tellraw @a [\"\",{\"text\":\"<"+ev.getPlayer().getName()+"> \"},{\"text\":\""+ev.getMessage().substring(0, pos)+"\"},{\"text\":\""+ChatColor.GREEN+"["+GenericFunctions.GetItemName(ev.getPlayer().getEquipment().getItemInMainHand())+ChatColor.RESET+ChatColor.YELLOW+((ev.getPlayer().getEquipment().getItemInMainHand().getAmount()>1)?" x"+ev.getPlayer().getEquipment().getItemInMainHand().getAmount():"")+ChatColor.GREEN+"]"+ChatColor.WHITE+"\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\""+GenericFunctions.GetItemName(ev.getPlayer().getEquipment().getItemInMainHand())+""+WorldShop.GetItemInfo(ev.getPlayer().getEquipment().getItemInMainHand()).replace("\"", "\\\"")+"\"}},{\"text\":\""+ev.getMessage().substring(pos)+"\"}]");
	    			
	    			ev.setCancelled(true);
	    		}
	    		//Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"tellraw @a [\"\",{\"text\":\""+ChatColor.GREEN+"[Item]"+ChatColor.WHITE+"\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\""+(ev.getPlayer().getEquipment().getItemInMainHand().getType())+"\"}},{\"text\":\" "+ev.getMessage().substring(0, pos)+" \"}]");
	    		
    		}
    	}
    }
    
    public static void playMessageNotification(Player player) {
    	for (int i=0;i<Bukkit.getOnlinePlayers().toArray().length;i++) {
    		Player p = (Player)Bukkit.getOnlinePlayers().toArray()[i];
        	PlayerStructure pd = (PlayerStructure)playerdata.get(p.getUniqueId());
			if (pd.sounds_enabled) {
    			p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BASEDRUM, 0.6f, 0.85f);
			}
    	}
	}

	@EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void onArrowHitBlock(ProjectileHitEvent ev) {
		if (ev.getEntity() instanceof Arrow) {
			Arrow a = (Arrow)ev.getEntity();
			a.setCustomName("HIT");
		}
	}
	
	@EventHandler(priority=EventPriority.LOW)
    public void onPlayerInteract(PlayerInteractEntityEvent ev) {
		log("Clicked with "+ ev.getHand().name(),5);
		log("Clicked on: "+ev.getRightClicked().getName(),5);
		MonsterDifficulty md = null;
		if (ev.getPlayer().getEquipment().getItemInMainHand().getType()==Material.NAME_TAG && (ev.getRightClicked() instanceof Monster)) {
			//TwosideKeeper.log("Check this out.", 2);
			Monster m = (Monster)ev.getRightClicked();
			//MonsterController.convertMonster(m,md);
			final String oldname = m.getCustomName();
			Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
				@Override
				public void run() {
					if (MonsterController.getMonsterDifficulty(m)!=MonsterDifficulty.NORMAL) {
						log("It's not normal!",5);
						m.setCustomName(oldname);
						ev.getPlayer().getEquipment().getItemInMainHand().setAmount(ev.getPlayer().getEquipment().getItemInMainHand().getAmount()+1);
						}
				}
			},1);
		}
		///if (ev.getHand()==EquipmentSlot.OFF_HAND) {aPlugin.API.swingOffHand(ev.getPlayer());};
	}
	
	@EventHandler(priority=EventPriority.LOW)
    public void onPlayerInteract(PlayerInteractEvent ev) {
	  if (ev.isCancelled() && ev.getAction() == Action.RIGHT_CLICK_BLOCK) {
            return;
        }  else {
        	Block b = ev.getClickedBlock();
	    	log("Interaction type: "+ev.getAction().toString(),5);
	    	
	    	//Pass along this event to Spleef Games.
	    	TwosideSpleefGames.PassEvent(ev);
	    	
	    	final Player player = ev.getPlayer();
			Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
				@Override
				public void run() {
			    	setPlayerMaxHealth(player);
				}
			},1);
			if (ev.getClickedBlock()!=null && ev.getClickedBlock().getType()==Material.CHEST &&
					TwosideRecyclingCenter.isChoosingRecyclingCenter() &&
					ev.getPlayer().hasPermission("TwosideKeeper.recyclingcenter")) {
				TwosideRecyclingCenter.setChoosingRecyclingCenter(false);
				//Create a new Recycling Center.
				TwosideRecyclingCenter.AddNode(ev.getClickedBlock().getWorld(), ev.getClickedBlock().getLocation().getBlockX(), ev.getClickedBlock().getLocation().getBlockY(), ev.getClickedBlock().getLocation().getBlockZ());
				TwosideRecyclingCenter.populateItemListFromNode(ev.getClickedBlock().getLocation());
				ev.getPlayer().sendMessage(ChatColor.DARK_BLUE+"New Recycling Center successfully created at "+ev.getClickedBlock().getLocation().toString());
			}
			
			//Check for a bow shift-right click.
			if (ev.getAction()==Action.RIGHT_CLICK_AIR || ev.getAction()==Action.RIGHT_CLICK_BLOCK) {
				Player p = ev.getPlayer();
				if (GenericFunctions.isRanger(p) && p.isSneaking()) {
					//Rotate Bow Modes.
					p.removePotionEffect(PotionEffectType.SLOW);
					BowMode mode = GenericFunctions.getBowMode(p.getEquipment().getItemInMainHand());
					switch (mode) {
						case CLOSE:{
							p.playSound(p.getLocation(), Sound.ENTITY_ZOMBIE_INFECT, 0.5f, 0.1f);
							GenericFunctions.setBowMode(p.getEquipment().getItemInMainHand(),BowMode.SNIPE);
						}break;
						case SNIPE:{
							p.playSound(p.getLocation(), Sound.BLOCK_BREWING_STAND_BREW, 0.5f, 0.1f);
							GenericFunctions.setBowMode(p.getEquipment().getItemInMainHand(),BowMode.DEBILITATION);
						}break;
						case DEBILITATION:{
							p.playSound(p.getLocation(), Sound.BLOCK_CHEST_LOCKED, 0.5f, 3.5f);
							GenericFunctions.setBowMode(p.getEquipment().getItemInMainHand(),BowMode.CLOSE);
						}break;
					}
					GenericFunctions.applyModeName(p.getEquipment().getItemInMainHand());
				}
			}
			
			//Check for a Sword left click.
			if (ev.getAction()==Action.LEFT_CLICK_AIR || ev.getAction()==Action.LEFT_CLICK_BLOCK) {
				Player p = ev.getPlayer();
	    		if (GenericFunctions.isStriker(p)) {
	    			//Check for nearby arrows to deflect.
	    			List<Entity> nearby = p.getNearbyEntities(3.5, 3.5, 3.5);
	    			for (int i=0;i<nearby.size();i++) {
	    				if (nearby.get(i) instanceof Arrow &&
	    						((Arrow)nearby.get(i)).getCustomName()==null) {
							int currentStrengthLevel = -1;
							for (int j=0;j<p.getActivePotionEffects().size();j++) {
								if (Iterables.get(p.getActivePotionEffects(), j).getType().equals(PotionEffectType.INCREASE_DAMAGE)) {
									//Get the level.
									currentStrengthLevel = Iterables.get(p.getActivePotionEffects(), j).getAmplifier();
									p.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
									log("Resistance level is "+currentStrengthLevel,5);
									break;
								}
							}
							p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,100,(currentStrengthLevel+1<5)?currentStrengthLevel+1:4));
							p.playSound(p.getLocation(), Sound.ITEM_SHIELD_BLOCK, 1.0f, 3.0f);
							Arrow a = (Arrow)nearby.get(i);
							a.setCustomName("HIT");
							a.setVelocity(new Vector(0,0,0));
	    				} 
	    			}
	    		}
			}
			
			//Check for a Scythe right click here.
			if ((ev.getAction()==Action.RIGHT_CLICK_AIR || ev.getAction()==Action.RIGHT_CLICK_BLOCK) &&
					GenericFunctions.isArtifactEquip(player.getEquipment().getItemInMainHand())) {
				PlayerStructure pd = (PlayerStructure)playerdata.get(player.getUniqueId()); //Make sure it's off cooldown.
				if (pd.last_deathmark+DEATHMARK_COOLDOWN<getServerTickTime()) {
					boolean bursted=false;
					if (ArtifactAbility.getEnchantmentLevel(ArtifactAbility.DEATHMARK, player.getEquipment().getItemInMainHand())>0) {
						double dmg = GenericFunctions.getAbilityValue(ArtifactAbility.DEATHMARK, player.getEquipment().getItemInMainHand());
						//Look for nearby mobs up to 10 blocks away.
						List<Entity> nearby = player.getNearbyEntities(10, 10, 10);
						for (int i=0;i<nearby.size();i++) {
							if (nearby.get(i) instanceof Monster) {
								Monster m = (Monster)nearby.get(i);
								if (m.hasPotionEffect(PotionEffectType.UNLUCK) && !m.isDead()) {
									//This has stacks, burst!
									bursted=true;
									aPlugin.API.sendCooldownPacket(player, player.getEquipment().getItemInMainHand(), 240);
									aPlugin.API.sendCooldownPacket(player, player.getEquipment().getItemInMainHand(), 240);
									pd.last_deathmark = getServerTickTime();
									int stackamt = GenericFunctions.GetDeathMarkAmt(m);
					    			m.setLastDamage(0);
					    			m.setNoDamageTicks(0);
					    			m.setMaximumNoDamageTicks(0);
									GenericFunctions.DealDamageToMob(stackamt*dmg, m, player, null, "Death Mark");
									m.removePotionEffect(PotionEffectType.UNLUCK);
									player.playSound(m.getLocation(), Sound.ENTITY_ZOMBIE_BREAK_DOOR_WOOD, 1.0f, 1.0f);
								}
							}
						}
					}
					if (bursted) {
						//Cancel this then, because we decided to burst our stacks instead.
						ev.setCancelled(true);
					}
				}
			}
			
			//Check stuff here.
			if (ev.getAction()==Action.RIGHT_CLICK_AIR ||
					ev.getAction()==Action.RIGHT_CLICK_BLOCK) {
				//See if the player is holding a valid bank check. Unsigned.
				ItemStack check = ev.getPlayer().getEquipment().getItemInMainHand();
				if (Check.isUnsignedBankCheck(check)) {
					//This is an unwritten check.
					BankSession bs = null;
					if (banksessions.containsKey(ev.getPlayer().getUniqueId())) {
						bs = (BankSession)banksessions.get(ev.getPlayer().getUniqueId());
						bs.refreshSession();
						if (bs.GetState()!=SessionState.SIGN_CHECK) { //Don't keep announcing the message if we are already in the state.
							ev.getPlayer().sendMessage("Input how much you want to sign this "+ChatColor.YELLOW+"check"+ChatColor.WHITE+" for:");
						}
						bs.SetState(SessionState.SIGN_CHECK);
					} else {
						bs = new BankSession(ev.getPlayer(),SessionState.SIGN_CHECK);
						banksessions.put(ev.getPlayer().getUniqueId(), bs);
						ev.getPlayer().sendMessage("Input how much you want to sign this "+ChatColor.YELLOW+"check"+ChatColor.WHITE+" for:");
					}
				}
			}
			
			//Shield related stuff in here.
			if (ev.getAction()==Action.RIGHT_CLICK_AIR ||
					ev.getAction()==Action.RIGHT_CLICK_BLOCK) {
				//See if this player is blocking. If so, give them absorption.
				//Player p = ev.getPlayer();
				Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
					@Override
					public void run() {
						if (player.isBlocking()) {
							//Give absorption hearts.
							if (GenericFunctions.isDefender(player)) {
								player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION,200,1));
								List<Entity> entities = player.getNearbyEntities(16, 16, 16);
								for (int i=0;i<entities.size();i++) { 
									if (entities.get(i) instanceof Monster) {
										Monster m = (Monster)(entities.get(i));
										m.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING,100,5,true,true,Color.NAVY));
										m.setTarget(player);
									}
								}
							} else {
								player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION,200,0));
							}
							DecimalFormat df = new DecimalFormat("0.0");
							player.sendMessage(ChatColor.GRAY+"Damage Reduction: "+ChatColor.DARK_AQUA+df.format(((1-NewCombat.CalculateDamageReduction(1,player,player))*100))+"%");
						}
					}
				},8);
			}
			
			//Check if we're allowed to open a shop chest.
			if (ev.getAction()==Action.RIGHT_CLICK_BLOCK &&
					(ev.getClickedBlock().getType()==Material.CHEST ||
					ev.getClickedBlock().getType()==Material.TRAPPED_CHEST)) {
				//Now check if it's a shop chest.
				Sign shopsign = WorldShop.grabShopSign(ev.getClickedBlock());
				if (shopsign!=null) {
					//Now grab the owner of the shop.
					WorldShop shop = TwosideShops.LoadWorldShopData(shopsign);
					if (!shop.GetOwner().equalsIgnoreCase(ev.getPlayer().getName())) {
	    				ev.getPlayer().sendMessage("This shop belongs to "+ChatColor.LIGHT_PURPLE+shop.GetOwner()+ChatColor.WHITE+"! You cannot look at other's shops!");
						ev.setCancelled(true);
					}
				}
			}
			
			//Check for a Malleable Base right-click.
			if ((ev.getAction()==Action.RIGHT_CLICK_AIR ||
					ev.getAction()==Action.RIGHT_CLICK_BLOCK) &&
					Artifact.isMalleableBase(ev.getPlayer().getEquipment().getItemInMainHand())) {
				//Start a Malleable Base quest.
				if (MalleableBaseQuest.getStatus(ev.getPlayer().getEquipment().getItemInMainHand())==QuestStatus.UNFORMED) {
					ItemStack MalleableBase = ev.getPlayer().getEquipment().getItemInMainHand();
					ev.getPlayer().getEquipment().setItemInMainHand(MalleableBaseQuest.startQuest(MalleableBase));
					//Start the quest.
					ev.getPlayer().sendMessage(ChatColor.YELLOW+"Malleable Base Forming Quest has begun!");
					MalleableBaseQuest.announceQuestItem(this,ev.getPlayer(),MalleableBase);
				} else {
					//If quest is in progress, we will check if the item we need is in our inventory.
					//0-8 are the hotbar slots.
					for (int i=0;i<=8;i++) {
						if (ev.getPlayer().getInventory().getItem(i)!=null) {
							log("Malleable Base Quest: Comparing "+GenericFunctions.UserFriendlyMaterialName(ev.getPlayer().getInventory().getItem(i).getType())+" to "+MalleableBaseQuest.getItem(ev.getPlayer().getEquipment().getItemInMainHand()),2);
						}
						if (ev.getPlayer().getInventory().getItem(i)!=null && GenericFunctions.hasNoLore(ev.getPlayer().getInventory().getItem(i)) && !Artifact.isArtifact(ev.getPlayer().getInventory().getItem(i)) && GenericFunctions.UserFriendlyMaterialName(ev.getPlayer().getInventory().getItem(i).getType()).equalsIgnoreCase(MalleableBaseQuest.getItem(ev.getPlayer().getEquipment().getItemInMainHand()))) {
							//This is good. Take one away from the player to continue the quest.
							log(ChatColor.YELLOW+"Success! Next Item...",5);
							ItemStack newitem = ev.getPlayer().getInventory().getItem(i);
							newitem.setAmount(ev.getPlayer().getInventory().getItem(i).getAmount()-1);
							ev.getPlayer().getInventory().setItem(i, newitem);
							//Check if we have completed all the quests. Otherwise, generate the next quest.
							ev.getPlayer().getEquipment().setItemInMainHand(MalleableBaseQuest.advanceQuestProgress(ev.getPlayer().getEquipment().getItemInMainHand()));
							if (MalleableBaseQuest.getCurrentProgress(ev.getPlayer().getEquipment().getItemInMainHand())==30) {
								//The quest is completed. Proceed to turn it into a Base.
								ev.getPlayer().getEquipment().setItemInMainHand(MalleableBaseQuest.completeQuest(ev.getPlayer().getEquipment().getItemInMainHand()));
								if (!Artifact.isMalleableBase(ev.getPlayer().getEquipment().getItemInMainHand())) {
									ev.getPlayer().sendMessage(ChatColor.DARK_BLUE+"Quest Complete! "+ChatColor.GREEN+"You obtained "+ev.getPlayer().getEquipment().getItemInMainHand().getItemMeta().getDisplayName()+ChatColor.GREEN+"!");
								} else {
									ev.getPlayer().sendMessage(ChatColor.DARK_RED+"Quest Failed! "+ChatColor.RED+"You did not successfully mold the Malleable Base. You will have to re-activate it by right-clicking it again.");
								}
							} else {
								//The quest is in progress. Announce the next item to the player.
								MalleableBaseQuest.announceQuestItem(this,ev.getPlayer(),ev.getPlayer().getEquipment().getItemInMainHand());
							}
							break;
						}
					}
				}
			}
			
			//Check for a bed right-click. Set the new bed save point.
			if (ev.getAction()==Action.RIGHT_CLICK_BLOCK &&
					ev.getClickedBlock().getType()==Material.BED_BLOCK) {
				Location BedPos=ev.getClickedBlock().getLocation();
				Location oldBedPos = ev.getPlayer().getBedSpawnLocation();
				log(ev.getPlayer()+" Right-clicked bed. Set bed spawn to "+BedPos.toString(),3);
				ev.getPlayer().setBedSpawnLocation(BedPos);
				if (oldBedPos!=null && ev.getPlayer().getBedSpawnLocation()!=null) {
					log(oldBedPos.toString()+"::"+ev.getPlayer().getBedSpawnLocation().toString(),5);
					if (oldBedPos.getBlockX()!=ev.getPlayer().getBedSpawnLocation().getBlockX() ||
							oldBedPos.getBlockY()!=ev.getPlayer().getBedSpawnLocation().getBlockY() ||
							oldBedPos.getBlockZ()!=ev.getPlayer().getBedSpawnLocation().getBlockZ())
					ev.getPlayer().sendMessage(ChatColor.BLUE+""+ChatColor.ITALIC+"New bed respawn location set.");
				} else {
					ev.getPlayer().sendMessage(ChatColor.BLUE+""+ChatColor.ITALIC+"New bed respawn location set.");
				}
			}
			if (ev.getAction()==Action.RIGHT_CLICK_BLOCK &&
					ev.getClickedBlock().getType().toString().contains("RAIL") &&
					ev.getPlayer().getInventory().getItemInMainHand().hasItemMeta() &&
					ev.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasLore() &&
					ev.getPlayer().getInventory().getItemInMainHand().getItemMeta().getLore().size()==4 &&
					ev.getPlayer().getInventory().getItemInMainHand().getItemMeta().getLore().get(3).contains(ChatColor.DARK_PURPLE+"ID#")) {
				ev.setCancelled(true); //Do not place minecarts on rails -.-
				ev.getPlayer().updateInventory();
			}
	    	if (ev.getAction()==Action.RIGHT_CLICK_AIR || (ev.getPlayer().isSneaking() && ev.getAction()==Action.RIGHT_CLICK_AIR) || (ev.getPlayer().isSneaking() && ev.getAction()==Action.RIGHT_CLICK_BLOCK && !GenericFunctions.isDumpableContainer(ev.getClickedBlock().getType()))) {
	    		if (ev.getPlayer().getInventory().getItemInMainHand().hasItemMeta() &&
	    				ev.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasLore() &&
	    				ev.getPlayer().getInventory().getItemInMainHand().getItemMeta().getLore().size()==4 &&
	    				ev.getPlayer().getInventory().getItemInMainHand().getItemMeta().getLore().get(3).contains(ChatColor.DARK_PURPLE+"ID#")) {
	    			//This is an item cube.
	    			log("In we are",5);
	    			ev.setCancelled(true);
	    			int itemcube_id=Integer.parseInt(ev.getPlayer().getInventory().getItemInMainHand().getItemMeta().getLore().get(3).split("#")[1]);
	    			int size=0;
	    			if (ev.getPlayer().getInventory().getItemInMainHand().getType()==Material.CHEST) {
	    				size=9;
	    			} else {
	    				size=27;
	    			}
	    			if (!ItemCube.isSomeoneViewingItemCube(itemcube_id,ev.getPlayer())) {
		    			InventoryView newinv = ev.getPlayer().openInventory(Bukkit.getServer().createInventory(ev.getPlayer(), size, "Item Cube #"+itemcube_id));
		    			PlayerStructure pd = (PlayerStructure) playerdata.get(ev.getPlayer().getUniqueId());
		    			pd.isViewingItemCube=true;
		    			openItemCubeInventory(newinv.getTopInventory(),newinv);
		    			ev.getPlayer().playSound(ev.getPlayer().getLocation(), Sound.BLOCK_CHEST_OPEN, 1.0f, 1.0f);
	    			} else {
	    				//ItemCube.displayErrorMessage(ev.getPlayer());
	    				ev.getPlayer().openInventory(ItemCube.getViewingItemCubeInventory(itemcube_id, ev.getPlayer()));
	    				PlayerStructure pd = (PlayerStructure) playerdata.get(ev.getPlayer().getUniqueId());
	    				pd.isViewingItemCube=true;
		    			ev.getPlayer().playSound(ev.getPlayer().getLocation(), Sound.BLOCK_CHEST_OPEN, 1.0f, 1.0f);
	    			}
	    		}
	    	} else
	    	if (ev.getPlayer().isSneaking() && ev.getAction()==Action.RIGHT_CLICK_BLOCK && GenericFunctions.isDumpableContainer(ev.getClickedBlock().getType())) {
	    		//This is an attempt to insert an item cube into a container. See what item cube we're holding.
	    		if (ev.getPlayer().getInventory().getItemInMainHand().hasItemMeta() &&
	    				ev.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasLore() &&
	    				ev.getPlayer().getInventory().getItemInMainHand().getItemMeta().getLore().size()==4 &&
	    				ev.getPlayer().getInventory().getItemInMainHand().getItemMeta().getLore().get(3).contains(ChatColor.DARK_PURPLE+"ID#")) {
	
	        		ev.setCancelled(true);
	        		ev.getPlayer().updateInventory();
					int itemcube_id=Integer.parseInt(ev.getPlayer().getInventory().getItemInMainHand().getItemMeta().getLore().get(3).split("#")[1]);
					int size=0;
					if (ev.getPlayer().getInventory().getItemInMainHand().getType()==Material.CHEST) {
						size=9;
					} else {
						size=27;
					}
					//Now that we have the item cube. Dump whatever contents we can into the container.
					
					//We need to make sure the chest is not a world shop. If it is, we can see if we're the owner of it.
					boolean allowed=true;
					String owner="";
					if (WorldShop.hasShopSignAttached(ev.getClickedBlock())) {
						WorldShop s = TwosideShops.LoadWorldShopData(WorldShop.grabShopSign(ev.getClickedBlock()));
						if (!s.GetOwner().equalsIgnoreCase(ev.getPlayer().getName())) {
							allowed=false;
							owner=s.GetOwner();
						}
					}
					
					if (allowed) {
						//Get the inventory of the chest we are inserting into.
						Chest c = (Chest)ev.getClickedBlock().getState();
						Inventory chest_inventory = c.getBlockInventory();
						if (c.getInventory().getHolder() instanceof DoubleChest) {
							chest_inventory = ((DoubleChest)c.getInventory().getHolder()).getInventory();
		
							log("This is a double chest",5);
						}
						
						//Get the inventory we are dumping out.
						Inventory virtualinventory = ItemCube.getViewingItemCubeInventory(itemcube_id, ev.getPlayer());
						if (virtualinventory==null) {
							//Then we will make one.
							virtualinventory = Bukkit.createInventory(ev.getPlayer(), size);
							//Load up this inventory.
							List<ItemStack> items = itemCube_loadConfig(itemcube_id);
							for (int i=0;i<virtualinventory.getSize();i++) {
								if (items.get(i)!=null) {
									virtualinventory.setItem(i, items.get(i));
									log("Load up with "+items.get(i).toString(),5);
								}
							}
						}
						List<ItemStack> save_items = new ArrayList<ItemStack>();
						
						for (int i=0;i<size;i++) {
							save_items.add(new ItemStack(Material.AIR));
						}
						
						boolean fit=true;
						int count=0;
						//Now that we have our items. Dump what we can into the chest inventory.
						for (int i=0;i<virtualinventory.getSize();i++) {
							if (virtualinventory.getItem(i)!=null &&
									virtualinventory.getItem(i).getType()!=Material.AIR) {
								HashMap result = chest_inventory.addItem(virtualinventory.getItem(i));
								if (result.size()>0) {
									save_items.set(i,(ItemStack)result.get(0));
									fit=false;
									log("This item "+(ItemStack)result.get(0)+" (slot "+i+") could not fit!",4);
								} else {
									count++;
								}
							}
						}
						
						//The rest of the hashmap goes back in the original inventory.
						if (!fit) {
							ev.getPlayer().sendMessage(ChatColor.RED+"Attempted to store your items, not all of them could fit!"+ChatColor.WHITE+" Stored "+ChatColor.AQUA+count+ChatColor.WHITE+" items.");
						} else {
							if (count>0) {
								ev.getPlayer().sendMessage("Stored "+ChatColor.AQUA+count+ChatColor.WHITE+" items inside the chest.");
							}
						}
						virtualinventory.clear();
						
						
						//Save the Item Cube.
						itemCube_saveConfig(itemcube_id,save_items);
						//This may have been a shop. Update the shop too.
						WorldShop.updateShopSign(ev.getClickedBlock());
					} else {
						ev.getPlayer().sendMessage("This shop is owned by "+ChatColor.LIGHT_PURPLE+owner+ChatColor.WHITE+". You cannot dump item cubes into others' shops!");
						//ev.setCancelled(true);
					}
	    		}
	    	}
	    	if (b!=null && (b.getType() == Material.SIGN ||
	    			b.getType() == Material.SIGN_POST ||
	    			b.getType() == Material.WALL_SIGN) && b.getState()!=null && (b.getState() instanceof Sign)) {
	        	log(b.getLocation().toString()+": This is a sign.",5);
	    		Sign s = (Sign)(b.getState());
	    		
	    		//Determine if this is a shop sign.
	    		if (b.getType()==Material.WALL_SIGN) { //Shop signs can only be wall signs.
	    			log("This is a wall sign.",5);
	    			//Make sure it is on a chest. Or trapped chest.
	    			org.bukkit.material.Sign s1 = (org.bukkit.material.Sign)(b.getState().getData());
	    			Block chest = b.getRelative(s1.getAttachedFace());
	    			if (chest.getType()==Material.CHEST ||
	    					chest.getType()==Material.TRAPPED_CHEST) {
		    			if (s.getLine(0).equalsIgnoreCase("shop")) {
		    				if (!WorldShop.shopSignExists(chest)) {
		    					log("This is a shop sign.",5);
			    				//Create a new shop.
			    				ItemStack item = player.getEquipment().getItemInMainHand();
			    				if (item.getType()!=Material.AIR) {
			        				WorldShopSession ss = TwosideShops.AddSession(SessionState.PRICE, player, s);
			        				TextComponent message1 = new TextComponent("Creating a shop to sell ");
									TextComponent message2 = new TextComponent(ChatColor.GREEN+"["+GenericFunctions.GetItemName(item)+ChatColor.RESET+""+ChatColor.GREEN+"]");
									message2.setHoverEvent(new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(GenericFunctions.GetItemName(item)+WorldShop.GetItemInfo(item)).create()));
									TextComponent message3 = new TextComponent(".");
									TextComponent finalmsg = message1;
									finalmsg.addExtra(message2);
									finalmsg.addExtra(message3);
									ev.getPlayer().spigot().sendMessage(finalmsg);
			    					int totalcount = 0;
			    					totalcount = GenericFunctions.CountItems(player, item);
			    					log("We have "+totalcount+" items in our inventory.",4);
			    					ss.SetItem(item);
			    					//player.sendMessage("Specify how much  "+ChatColor.GREEN+"(MAX: "+ChatColor.YELLOW+totalcount+ChatColor.GREEN+")");
			    					Chest c = (Chest)chest.getState();
			    					ss.SetAmt(GenericFunctions.CountItems(c.getInventory(), item));
			        				message1 = new TextComponent("Input how much each ");
									message2 = new TextComponent(ChatColor.GREEN+"["+GenericFunctions.GetItemName(ss.getItem())+ChatColor.RESET+""+ChatColor.GREEN+"]");
									message2.setHoverEvent(new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(GenericFunctions.GetItemName(ss.getItem())+WorldShop.GetItemInfo(ss.getItem())).create()));
									message3 = new TextComponent(" will cost:");
									finalmsg = message1;
									finalmsg.addExtra(message2);
									finalmsg.addExtra(message3);
									ev.getPlayer().spigot().sendMessage(finalmsg);
			    				} else {
			    					player.sendMessage(ChatColor.RED+"Cannot create a shop with nothing! "+ChatColor.WHITE+"Right-click the sign"
			    							+ " with the item you want to sell in your hand.");
			    				}
		    				} else {
		    					player.sendMessage(ChatColor.RED+"Sorry! "+ChatColor.WHITE+" A shop has already been setup here!");
		    				}
		    			} else 
		    			if (s.getLine(0).equalsIgnoreCase(ChatColor.BLUE+"-- SHOP --")) {
		        			log("This is a buy shop sign.",5);
		    				int shopID = TwosideShops.GetShopID(s);
		        			WorldShop shop = TwosideShops.LoadWorldShopData(shopID);
	    					Chest c = (Chest)chest.getState();
		        			shop.UpdateAmount(GenericFunctions.CountItems(c.getInventory(), shop.GetItem()));
		        			TwosideShops.UpdateSign(shop, shop.getID(),s,shop.isPurchaseShopSign(s));
							TwosideShops.SaveWorldShopData(shop);
		        			Location newloc = ev.getClickedBlock().getLocation().add(-ev.getBlockFace().getModX()+0.5, -ev.getBlockFace().getModY()+1.5, -ev.getBlockFace().getModZ()+0.5);
		        			
		        			WorldShop.spawnShopItem(ev,newloc,shop);
		        			
		        			if (shop.GetOwner().equalsIgnoreCase(ev.getPlayer().getName())) {
		        				player.sendMessage(ChatColor.DARK_PURPLE+"Editing shop...");
		        				//player.sendMessage("Insert more "+ChatColor.GREEN+shop.GetItemName()+ChatColor.WHITE+" by typing a positive amount "+ChatColor.GREEN+"(MAX:"+GenericFunctions.CountItems(player,shop.GetItem())+")"+ChatColor.WHITE+". Or withdraw "+ChatColor.GREEN+shop.GetItemName()+ChatColor.WHITE+" by typing a negative amount "+ChatColor.GREEN+"(MAX:"+shop.GetAmount()+")"+ChatColor.WHITE+"."); //OBSOLETE!
								DecimalFormat df = new DecimalFormat("0.00");
		        				//ev.getPlayer().sendMessage("Input how much each "+ChatColor.GREEN+"["+shop.GetItemName()+"]"+ChatColor.WHITE+" will cost (Old value - "+ChatColor.YELLOW+"$"+df.format(shop.GetUnitPrice())+ChatColor.WHITE+"):");
								TextComponent message1 = new TextComponent("Input how much each ");
								TextComponent message2 = new TextComponent(ChatColor.GREEN+"["+shop.GetItemName()+ChatColor.RESET+""+ChatColor.GREEN+"]");
								message2.setHoverEvent(new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(shop.GetItemName()+WorldShop.GetItemInfo(shop.GetItem())).create()));
								TextComponent message3 = new TextComponent(ChatColor.WHITE+" will cost (Old value - "+ChatColor.YELLOW+"$"+df.format(shop.GetUnitPrice())+ChatColor.WHITE+"):");
								TextComponent finalmsg = message1;
								finalmsg.addExtra(message2);
								finalmsg.addExtra(message3);
								ev.getPlayer().spigot().sendMessage(finalmsg);
			    				TwosideShops.AddSession(SessionState.UPDATE, player, s);
		        			} else {
			        			if (shop.GetAmount()>0) {
				        			//player.sendMessage("How many "+Cha tColor.GREEN+shop.GetItemName()+ChatColor.WHITE+" would you like to buy? "+ChatColor.GREEN+"(MAX: "+((getPlayerMoney(player)<(shop.GetAmount()*shop.GetUnitPrice()))?(int)(getPlayerMoney(player)/shop.GetUnitPrice()):shop.GetAmount())+")");
				        			TextComponent message1 = new TextComponent("How many ");
									TextComponent message2 = new TextComponent(ChatColor.GREEN+"["+shop.GetItemName()+ChatColor.RESET+""+ChatColor.GREEN+"]");
									message2.setHoverEvent(new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(shop.GetItemName()+WorldShop.GetItemInfo(shop.GetItem())).create()));
									TextComponent message3 = new TextComponent(ChatColor.WHITE+" would you like to buy? "+ChatColor.GREEN+"(MAX: "+((getPlayerMoney(player)<(shop.GetAmount()*shop.GetUnitPrice()))?(int)(getPlayerMoney(player)/shop.GetUnitPrice()):shop.GetAmount())+")");
									TextComponent finalmsg = message1;
									finalmsg.addExtra(message2);
									finalmsg.addExtra(message3);
									ev.getPlayer().spigot().sendMessage(finalmsg);
				    				
				    				//Initiate buying session.
				    				TwosideShops.AddSession(SessionState.PURCHASE, player, s);
				        			log("Added a shop session for "+player.getName()+".",4);
				        			//shop.sendItemInfo(player);
			        			} else {
			        				player.sendMessage(ChatColor.GOLD+"Sorry! "+ChatColor.WHITE+"This shop is sold out! Let "+ChatColor.LIGHT_PURPLE+shop.GetOwner()+ChatColor.WHITE+" know to restock the shop!");
			        			}
		        			}
		    			} else
		    			if (s.getLine(0).equalsIgnoreCase("buyshop")) {
		    				if (!WorldShop.shopSignExists(chest)) {
			    				//Create a new buy shop.
			    				ItemStack item = player.getEquipment().getItemInMainHand();
			    				if (item.getType()!=Material.AIR) {
			        				WorldShopSession ss = TwosideShops.AddSession(SessionState.BUY_PRICE, player, s);
			        				TextComponent message1 = new TextComponent("Creating a shop to buy ");
									TextComponent message2 = new TextComponent(ChatColor.GREEN+"["+GenericFunctions.GetItemName(item)+ChatColor.RESET+""+ChatColor.GREEN+"]");
									message2.setHoverEvent(new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(GenericFunctions.GetItemName(item)+WorldShop.GetItemInfo(item)).create()));
									TextComponent message3 = new TextComponent(".");
									TextComponent finalmsg = message1;
									finalmsg.addExtra(message2);
									finalmsg.addExtra(message3);
									ev.getPlayer().spigot().sendMessage(finalmsg);
			    					int totalcount = 0;
			    					//totalcount = GenericFunctions.CountItems(player, item);
			    					Chest c = (Chest)chest.getState();
			    					ss.SetAmt(GenericFunctions.CountEmptySpace(c.getInventory(), item));
			    					ss.SetItem(item);
	
			        				message1 = new TextComponent("Input how much you will pay for each ");
									message2 = new TextComponent(ChatColor.GREEN+"["+GenericFunctions.GetItemName(ss.getItem())+ChatColor.RESET+""+ChatColor.GREEN+"]");
									message2.setHoverEvent(new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(GenericFunctions.GetItemName(ss.getItem())+WorldShop.GetItemInfo(ss.getItem())).create()));
									message3 = new TextComponent(":");
									finalmsg = message1;
									finalmsg.addExtra(message2);
									finalmsg.addExtra(message3);
									ev.getPlayer().spigot().sendMessage(finalmsg);
									
									//player.sendMessage("How many of this item do you want to buy?");
			    				} else {
			    					player.sendMessage(ChatColor.RED+"Cannot create a shop with nothing! "+ChatColor.WHITE+"Right-click the sign"
			    							+ " with the item you want to buy in your hand.");
			    				}
		    				} else {
		    					player.sendMessage(ChatColor.RED+"Sorry! "+ChatColor.WHITE+" A shop has already been setup here!");
		    				}
		    			} else 
		    			if (s.getLine(0).equalsIgnoreCase(ChatColor.BLUE+"- BUYING SHOP -") ||
		    					s.getLine(0).equalsIgnoreCase(ChatColor.YELLOW+""+ChatColor.BOLD+"-BUYING SHOP-") ||
		    					s.getLine(0).equalsIgnoreCase(ChatColor.GREEN+""+ChatColor.BOLD+"-BUYING SHOP-")) {
		    				//This is a buy shop.
		    				int shopID = TwosideShops.GetShopID(s);
		        			WorldShop shop = TwosideShops.LoadWorldShopData(shopID);
	    					Chest c = (Chest)chest.getState();
		        			shop.UpdateAmount(GenericFunctions.CountEmptySpace(c.getInventory(), shop.GetItem()));
		        			TwosideShops.UpdateSign(shop, shop.getID(),s,shop.isPurchaseShopSign(s));
							TwosideShops.SaveWorldShopData(shop);
		        			Location newloc = ev.getClickedBlock().getLocation().add(-ev.getBlockFace().getModX()+0.5, -ev.getBlockFace().getModY()+1.5, -ev.getBlockFace().getModZ()+0.5);
		    				WorldShop.spawnShopItem(ev,newloc,shop);
		    				
		
		        			if (shop.GetOwner().equalsIgnoreCase(ev.getPlayer().getName())) {
		        				player.sendMessage(ChatColor.DARK_PURPLE+"Editing shop...");
								DecimalFormat df = new DecimalFormat("0.00");
		        				//player.sendMessage("Request more "+ChatColor.GREEN+shop.GetItemName()+ChatColor.WHITE+" by typing a positive amount "+ChatColor.WHITE+". Or withdraw stored "+ChatColor.GREEN+shop.GetItemName()+ChatColor.WHITE+" by typing a negative amount "+ChatColor.GREEN+"(MAX:"+shop.GetStoredAmount()+")"+ChatColor.WHITE+".");
		        				//ev.getPlayer().sendMessage("Input how much you will pay for each "+ChatColor.GREEN+shop.GetItemName()+ChatColor.WHITE+");
	
		        				TextComponent message1 = new TextComponent("Input how much you will pay for each ");
		        				TextComponent message2 = new TextComponent(ChatColor.GREEN+"["+GenericFunctions.GetItemName(shop.GetItem())+ChatColor.RESET+""+ChatColor.GREEN+"]");
		        				message2.setHoverEvent(new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(GenericFunctions.GetItemName(shop.GetItem())+WorldShop.GetItemInfo(shop.GetItem())).create()));
		        				TextComponent message3 = new TextComponent(" (Old value - "+ChatColor.YELLOW+"$"+df.format(shop.GetUnitPrice())+ChatColor.WHITE+"):");
		        				TextComponent finalmsg = message1;
								finalmsg.addExtra(message2);
								finalmsg.addExtra(message3);
								ev.getPlayer().spigot().sendMessage(finalmsg);
			    				TwosideShops.AddSession(SessionState.BUY_UPDATE, player, s);
		        			} else {
			        			if (shop.GetAmount()>0) {
				        			//player.sendMessage(+ChatColor.GREEN+shop.GetItemName()+ChatColor.WHITE+);
	
			        				TextComponent message1 = new TextComponent("How many ");
									TextComponent message2 = new TextComponent(ChatColor.GREEN+"["+shop.GetItemName()+ChatColor.RESET+""+ChatColor.GREEN+"]");
									message2.setHoverEvent(new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(shop.GetItemName()+WorldShop.GetItemInfo(shop.GetItem())).create()));
									TextComponent message3 = new TextComponent(" would you like to sell? "+ChatColor.GREEN+"(MAX: "+(shop.GetUnitPrice()*GenericFunctions.CountItems(player, shop.GetItem())<=getPlayerBankMoney(shop.GetOwner())?((GenericFunctions.CountItems(player, shop.GetItem())<=shop.GetAmount())?(GenericFunctions.CountItems(player, shop.GetItem())):shop.GetAmount()):(int)(getPlayerBankMoney(shop.GetOwner())/shop.GetUnitPrice()))+")");
									TextComponent finalmsg = message1;
									finalmsg.addExtra(message2);
									finalmsg.addExtra(message3);
									ev.getPlayer().spigot().sendMessage(finalmsg);
				    				//Initiate buying session.
				    				TwosideShops.AddSession(SessionState.SELL, player, s);
				        			log("Added a shop session for "+player.getName()+".",4);
				        			//shop.sendItemInfo(player);
			        			} else {
			        				player.sendMessage(ChatColor.GOLD+"Sorry! "+ChatColor.WHITE+"This shop is not buying anymore items! "+ChatColor.LIGHT_PURPLE+shop.GetOwner()+ChatColor.WHITE+" needs to edit the shop!");
			        			}
		        			}
		    			}
	    			}
	    		}
	    		//Determine if this is a bank sign.
	    		if (s.getLine(0).equalsIgnoreCase(ChatColor.AQUA+"-- BANK --")) {
	    			//This is indeed a bank sign. Now figure out which one.
	    			if (s.getLine(1).equalsIgnoreCase(ChatColor.GREEN+"CHECK BALANCE")) {
	    				//Display the balance to the user.
	        			DecimalFormat df = new DecimalFormat("0.00");
	    				ev.getPlayer().sendMessage("Your Bank Account currently has: "+ChatColor.BLUE+"$"+df.format(getPlayerBankMoney(ev.getPlayer())));
	    			} else
					if (s.getLine(1).equalsIgnoreCase(ChatColor.DARK_RED+"WITHDRAW")) {
						BankSession bs = null;
						SessionState thissession = SessionState.WITHDRAW;
						if (banksessions.containsKey(ev.getPlayer().getUniqueId())) {
							bs = (BankSession)banksessions.get(ev.getPlayer().getUniqueId());
							bs.refreshSession();
							bs.SetState(thissession);
						} else {
							bs = new BankSession(ev.getPlayer(),thissession);
							banksessions.put(ev.getPlayer().getUniqueId(), bs);
						}
						ev.getPlayer().sendMessage(ChatColor.GOLD+"Say/Type the amount you want to WITHDRAW today.");
	        			DecimalFormat df = new DecimalFormat("0.00");
	    				ev.getPlayer().sendMessage("  In Bank: "+ChatColor.BLUE+"$"+df.format(getPlayerBankMoney(ev.getPlayer())));
	    			} else
					if (s.getLine(1).equalsIgnoreCase(ChatColor.DARK_BLUE+"DEPOSIT")) {
						BankSession bs = null;
						SessionState thissession = SessionState.DEPOSIT;
						if (banksessions.containsKey(ev.getPlayer().getUniqueId())) {
							bs = (BankSession)banksessions.get(ev.getPlayer().getUniqueId());
							bs.refreshSession();
							bs.SetState(thissession);
						} else {
							bs = new BankSession(ev.getPlayer(),thissession);
							banksessions.put(ev.getPlayer().getUniqueId(), bs);
						}
						ev.getPlayer().sendMessage(ChatColor.GOLD+"Say/Type the amount you want to DEPOSIT today.");
	        			DecimalFormat df = new DecimalFormat("0.00");
						ev.getPlayer().sendMessage("  Currently Holding: "+ChatColor.GREEN+"$"+df.format(getPlayerMoney(ev.getPlayer())));
	    			} else
					if (s.getLine(1).equalsIgnoreCase(ChatColor.DARK_BLUE+"EXP CONVERSION")) {
						BankSession bs = null;
						SessionState thissession = SessionState.CONVERT;
						if (banksessions.containsKey(ev.getPlayer().getUniqueId())) {
							bs = (BankSession)banksessions.get(ev.getPlayer().getUniqueId());
							bs.refreshSession();
							bs.SetState(thissession);
						} else {
							bs = new BankSession(ev.getPlayer(),thissession);
							banksessions.put(ev.getPlayer().getUniqueId(), bs);
						}
						ev.getPlayer().sendMessage(ChatColor.GOLD+"Say/Type the amount of experience you want to convert today.");
						ev.getPlayer().sendMessage("  Currently Have: "+ChatColor.GREEN+aPlugin.API.getTotalExperience(ev.getPlayer())+" experience points");
	    			} else
					if (s.getLine(1).equalsIgnoreCase(ChatColor.DARK_GREEN+"CASH CHECK")) {
						if (Check.isSignedBankCheck(ev.getPlayer().getEquipment().getItemInMainHand())) {
							//Make sure the player that signed has enough money for this check. Otherwise don't allow it!
							Check c = new Check(ev.getPlayer().getEquipment().getItemInMainHand());
							if (c.player!=null) {
								//We found a player for this check. See if they have enough money.
								if (c.amt<=getPlayerBankMoney(c.player)) {
									//We're good. Subtract money from that player's bank account. And Add money to the player with the check. Destroy the check.
									givePlayerBankMoney(c.player,-c.amt);
									givePlayerBankMoney(ev.getPlayer(),c.amt);
									DecimalFormat df = new DecimalFormat("0.00");
									ev.getPlayer().sendMessage(ChatColor.AQUA+"Cashed in the check for "+ChatColor.YELLOW+"$"+df.format(c.amt)+ChatColor.WHITE+".");
			    					ev.getPlayer().sendMessage("  Now In Bank: "+ChatColor.BLUE+"$"+df.format(getPlayerBankMoney(ev.getPlayer())));
									if (ev.getPlayer().getEquipment().getItemInMainHand().getAmount()>1) {
										ev.getPlayer().getEquipment().getItemInMainHand().setAmount(ev.getPlayer().getEquipment().getItemInMainHand().getAmount()-1);
									} else {
										ev.getPlayer().getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
									}
								} else {
									DecimalFormat df = new DecimalFormat("0.00");
									ev.getPlayer().sendMessage(ChatColor.RED+"We're sorry! "+ChatColor.WHITE+"But the check cannot be processed since the check signer, "+ChatColor.LIGHT_PURPLE+c.player+ChatColor.WHITE+" has poor money management skills and does not have "+ChatColor.YELLOW+"$"+df.format(c.amt)+ChatColor.WHITE+" available in their account!");
									ev.getPlayer().sendMessage(ChatColor.AQUA+"We are sorry about this inconvenience. "+ChatColor.WHITE+"Have a nice day!");
								}
							} else {
								GenericFunctions.produceError(1,ev.getPlayer());
							}
						} else {
							ev.getPlayer().sendMessage(ChatColor.YELLOW+"You are not holding a properly signed check!");
						}
	    			}
	    		}
	    	}
        }
    }
    
    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent ev) {
    	
    	TwosideSpleefGames.PassEvent(ev);
    	
    	if (ev.getBlockPlaced().getType()==Material.CHEST ||
    			ev.getBlockPlaced().getType()==Material.TRAPPED_CHEST) {
    		//Check for a chest or trapped chest around each side of the block.
    		for (int x=-1;x<2;x++) {
    			for (int z=-1;z<2;z++) {
    				if ((x!=0)^(z!=0) && ev.getBlockPlaced().getLocation().add(x,0,z).getBlock().getType()==ev.getBlockPlaced().getType()) {
    					//This is the same type of block. Make sure there's no shop sign attached to it.
    					if (WorldShop.hasShopSignAttached(ev.getBlockPlaced().getRelative(x,0,z))) {
    						Sign s = WorldShop.grabShopSign(ev.getBlockPlaced().getRelative(x,0,z));
    						WorldShop shop = TwosideShops.LoadWorldShopData(s);
    						if (!shop.GetOwner().equalsIgnoreCase(ev.getPlayer().getName())) {
    							//This is not allowed! We can't expand shops that are not ours.
    		    				ev.getPlayer().sendMessage("There's a shop owned by "+ChatColor.LIGHT_PURPLE+shop.GetOwner()+ChatColor.WHITE+" right next to your chest! You cannot expand others' shops!");
    		    				ev.setCancelled(true);
    						}
    					}
    				}
    			}
    		}
    	}
    	
    	if (GenericFunctions.isArtifactEquip(ev.getItemInHand()) &&
    			ev.getItemInHand().getType().toString().contains("HOE")) {
			AwakenedArtifact.addPotentialEXP(ev.getItemInHand(), 4, ev.getPlayer());
    	}
    	
    	if (ev.getItemInHand().hasItemMeta() &&
    			ev.getItemInHand().getItemMeta().hasLore() &&
    			ev.getItemInHand().getItemMeta().getLore().size()==4 &&
    			ev.getItemInHand().getItemMeta().getLore().get(3).contains(ChatColor.DARK_PURPLE+"ID#")) {
    		//This is an item cube.
    		ev.setCancelled(true);
    	}
    	
    	if (Artifact.isArtifact(ev.getItemInHand()) && !GenericFunctions.isArtifactEquip(ev.getItemInHand())) {
    		ev.setCancelled(true);
    	}
    }
    
    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent ev) {
    	//Modify the death message. This is a fix for getting rid of the healthbar from the player name.
    	final Player p = ev.getEntity();
    	if (!DeathManager.deathStructureExists(p)) {
	    	String[] parsed_msg = ev.getDeathMessage().split(" ");
	    	//Get rid of the name.
	    	//NOTE: If you change how the suffix looks YOU MUST UPDATE THIS!
	    	String newDeathMsg="";
	    	for (int i=2;i<parsed_msg.length;i++) {
	    		if (newDeathMsg.equals("")) {
	    			newDeathMsg=parsed_msg[i];
	    		} else {
	    			newDeathMsg+=" "+parsed_msg[i];
	    		}
	    	}
	    	newDeathMsg=p.getName()+" "+newDeathMsg;
	    	ev.setDeathMessage(newDeathMsg);
	    	log("Death Message: "+ev.getDeathMessage(),5);
	    	if (p!=null) {
	    		p.sendMessage(ChatColor.GRAY+"Due to death, you lost "+DEATHPENALTY+"% of your holding money. ");
	    		givePlayerMoney(p,-Math.round(getPlayerMoney(p)/2));
				DecimalFormat df = new DecimalFormat("0.00");
	    		p.sendMessage("  Now Holding: "+ChatColor.GREEN+"$"+df.format(getPlayerMoney(p)));
	    	}
	    	
	    	ev.setKeepInventory(true);
	    	log("Y position is "+p.getLocation().getY(), 4);
	    	DeathManager.addNewDeathStructure(ev.getDrops(), (p.getLocation().getY()<0)?p.getLocation().add(0,-p.getLocation().getY()+256,0) //This means they fell into the void. Might as well put it way higher.
	    			:p.getLocation(), p);
	    	PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
	    	pd.hasDied=true;
	    	pd.vendetta_amt=0.0;
	    	p.getInventory().clear();
    	}
    }
    
    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void onSignChange(SignChangeEvent ev) {
    	Player p = ev.getPlayer();
    	Block b = ev.getBlock();
    	String line1 = ev.getLine(0);
    	String line2 = ev.getLine(2);
    	//-- BANK --
    	if (p.isOp() || p.hasPermission("TwosideKeeper.bank")) {
    		//Make sure we're Op, otherwise we're not allowed to do this.
    		if (line1.equalsIgnoreCase("-- bank --") &&
    				line2.equalsIgnoreCase("Check Balance")) {
    			//Turn it into a bank sign.
    			ev.setLine(0, ChatColor.AQUA+"-- BANK --");
    			ev.setLine(1, ChatColor.GREEN+"CHECK BALANCE");
    			ev.setLine(2, "Right-Click");
    			ev.setLine(3, "to use");
    			p.sendMessage("Successfully created a Balance Bank Sign.");
    		} else
			if (line1.equalsIgnoreCase("-- bank --") &&
    				line2.equalsIgnoreCase("Withdraw")) {
    			//Turn it into a bank sign.
    			ev.setLine(0, ChatColor.AQUA+"-- BANK --");
    			ev.setLine(1, ChatColor.DARK_RED+"WITHDRAW");
    			ev.setLine(2, "Right-Click");
    			ev.setLine(3, "to use");
    			p.sendMessage("Successfully created a Withdraw Bank Sign.");
    		} else
			if (line1.equalsIgnoreCase("-- bank --") &&
    				line2.equalsIgnoreCase("Deposit")) {
    			//Turn it into a bank sign.
    			ev.setLine(0, ChatColor.AQUA+"-- BANK --");
    			ev.setLine(1, ChatColor.DARK_BLUE+"DEPOSIT");
    			ev.setLine(2, "Right-Click");
    			ev.setLine(3, "to use");
    			p.sendMessage("Successfully created a Deposit Bank Sign.");
    		} else
			if (line1.equalsIgnoreCase("-- bank --") &&
    				line2.equalsIgnoreCase("EXP CONVERSION")) {
    			//Turn it into a bank sign.
    			ev.setLine(0, ChatColor.AQUA+"-- BANK --");
    			ev.setLine(1, ChatColor.DARK_BLUE+"EXP CONVERSION");
    			ev.setLine(2, "Right-Click");
    			ev.setLine(3, "to use");
    			p.sendMessage("Successfully created an EXP Conversion Bank Sign.");
    		} else
			if (line1.equalsIgnoreCase("-- bank --") &&
    				line2.equalsIgnoreCase("Cash Check")) {
    			//Turn it into a bank sign.
    			ev.setLine(0, ChatColor.AQUA+"-- BANK --");
    			ev.setLine(1, ChatColor.DARK_GREEN+"CASH CHECK");
    			ev.setLine(2, "Right-Click");
    			ev.setLine(3, "to use");
    			p.sendMessage("Successfully created a Cash Check Bank Sign.");
    		}
    	}
    }
    
    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void onItemCraft(CraftItemEvent ev) {
    	//log(ev.getCurrentItem().getItemMeta().toString(),5);
    	
    	if (ev.getInventory().getResult()!=null &&
    			ev.getInventory().getResult().getType()!=Material.AIR &&
    			Artifact.isArtifact(ev.getInventory().getResult()) && GenericFunctions.isEquip(ev.getInventory().getResult())) {
    		Player p = (Player)(Bukkit.getPlayer(ev.getWhoClicked().getName()));
    		p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_USE, 1.0f, 1.0f);
    	}
    	if (ev.getCurrentItem().hasItemMeta()) {
	    	ItemMeta item_meta = ev.getCurrentItem().getItemMeta();
	    	if (item_meta.getDisplayName()!=null && 
	    			item_meta.getDisplayName().contains("Item Cube")) {
	    		if (ev.isShiftClick()) {
	    			ev.setCancelled(true);
	    		} else {
		    		//We have verified this is an Item Cube. Setup an ID for this cube.
		    		List<String> item_lore = ev.getCurrentItem().getItemMeta().getLore();
		    		if (item_lore.size()!=4) { 
		    			//Make sure it doesn't already have an ID.
			    		item_lore.add(ChatColor.DARK_PURPLE+"ID#"+ITEMCUBEID);
			    		item_meta.setLore(item_lore);
			    		ev.getCurrentItem().setItemMeta(item_meta);
			    		if (ev.getCurrentItem().getItemMeta().getDisplayName().contains("Ender Item Cube")) {
			    			ev.getCurrentItem().setAmount(2);
			    		}
			    		CubeType cubetype;
			    		if (ev.getCurrentItem().getItemMeta().getDisplayName().contains("Ender Item Cube")) {
			    			cubetype=CubeType.ENDER;
			    		} else if (ev.getCurrentItem().getItemMeta().getDisplayName().contains("Large Item Cube")) {
			    			cubetype=CubeType.LARGE;
			    		} else {
			    			cubetype=CubeType.NORMAL;
			    		}
			    			itemCube_saveConfig(ITEMCUBEID, new ArrayList<ItemStack>(), cubetype);
			    		ITEMCUBEID++;
		    		}
	    		} 
	    	}
    	}
    }
    
    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void onFallingBlock(EntityChangeBlockEvent ev) {
    	if (ev.getEntity() instanceof FallingBlock) {
    		FallingBlock fb = (FallingBlock)ev.getEntity();
    		if (fb.hasMetadata("FAKE")) {
    			final Block b = ev.getBlock();
    			Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
					@Override
					public void run() {
						b.getLocation().getWorld().playSound(b.getLocation(), Sound.BLOCK_CHORUS_FLOWER_DEATH, 1.0f, 1.0f);
						b.breakNaturally();
					}
				},1);
    		}
    	}
    }
    
    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void onPlayerDropItem(PlayerDropItemEvent ev) {
    	
    	if (GenericFunctions.isArtifactEquip(ev.getItemDrop().getItemStack())) {
    		ev.getItemDrop().setInvulnerable(true);
    	}
    	
    	if (ev.getItemDrop().getItemStack().hasItemMeta()) {
    		if (ev.getItemDrop().getItemStack().getItemMeta().hasLore()) {
    			if (ev.getItemDrop().getItemStack().getItemMeta().getLore().size()==4) {
    				if (ev.getItemDrop().getItemStack().getItemMeta().getLore().get(3).contains(ChatColor.DARK_PURPLE+"ID#")) {
    					//We have an item cube.
    					int itemcube_id=Integer.parseInt(ev.getItemDrop().getItemStack().getItemMeta().getLore().get(3).split("#")[1]);
    					//Now we need to see if an item cube inventory of that ID is opened.
    					if (ev.getPlayer().getOpenInventory().getTitle().split("#").length>1 && itemcube_id==Integer.parseInt(ev.getPlayer().getOpenInventory().getTitle().split("#")[1])) {
    						//We have the same item cube opened. Save and close it immediately.
    						List<ItemStack> itemlist = new ArrayList<ItemStack>();
    						for (int i=0;i<ev.getPlayer().getOpenInventory().getTopInventory().getSize();i++) {
    							itemlist.add(ev.getPlayer().getOpenInventory().getTopInventory().getItem(i));
    						}
    						final Player p = ev.getPlayer();
    						itemCube_saveConfig(itemcube_id,itemlist,((ev.getItemDrop().getItemStack().getType()==Material.CHEST)?CubeType.NORMAL:(ev.getItemDrop().getItemStack().getType()==Material.STORAGE_MINECART)?CubeType.LARGE:CubeType.ENDER));
    						Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
    							@Override
    							public void run() {
    								p.closeInventory();
    							}
    						},1); 
    					}
    				}
    			}
    		}
    	} 
    	
    	if (ev.getItemDrop().getItemStack().getType()==Material.SHIELD && !GenericFunctions.isViewingInventory(ev.getPlayer())) {
    		ev.setCancelled(true);
    		if (ev.getPlayer().getEquipment().getItemInMainHand()==null || ev.getPlayer().getEquipment().getItemInMainHand().getType()==Material.AIR) {
	    		ev.getPlayer().getEquipment().setItemInMainHand(ev.getItemDrop().getItemStack());
	    		PlayerStructure pd = PlayerStructure.GetPlayerStructure(ev.getPlayer());
	    		if (pd.last_rejuvenate+TwosideKeeper.REJUVENATE_COOLDOWN<=TwosideKeeper.getServerTickTime()) {
	    			GenericFunctions.PerformRejuvenate(ev.getPlayer());
	    			pd.last_rejuvenate = TwosideKeeper.getServerTickTime();
	    			aPlugin.API.damageItem(ev.getPlayer(), ev.getItemDrop().getItemStack(), 400);
	    		}
	    		ev.getPlayer().getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
    		}
    	}
    	
    	if (ev.getItemDrop().getItemStack().getType()==Material.BOW && !GenericFunctions.isViewingInventory(ev.getPlayer())) {
    		ev.setCancelled(true);
    		if (ev.getPlayer().getEquipment().getItemInMainHand()==null || ev.getPlayer().getEquipment().getItemInMainHand().getType()==Material.AIR) {
	    		ev.getPlayer().getEquipment().setItemInMainHand(ev.getItemDrop().getItemStack());
	    		GenericFunctions.PerformDodge(ev.getPlayer());
	    		ev.getPlayer().getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
    		}
    	}
    	
    	if (GenericFunctions.holdingNoShield(ev.getPlayer()) &&
    			ev.getItemDrop().getItemStack().getType().toString().contains("SWORD") &&
    			!GenericFunctions.isViewingInventory(ev.getPlayer())) {
    		ev.setCancelled(true);
    		PlayerStructure pd = (PlayerStructure)playerdata.get(ev.getPlayer().getUniqueId());
			boolean second_charge = (ev.getPlayer().hasPotionEffect(PotionEffectType.GLOWING) || (ev.getPlayer().hasPotionEffect(PotionEffectType.SLOW) && GenericFunctions.getPotionEffectLevel(PotionEffectType.SLOW, ev.getPlayer())==20));
    		if ((ev.getPlayer().isOnGround() || second_charge) &&
    				pd.last_strikerspell+LINEDRIVE_COOLDOWN<getServerTickTime()) {
    			if (pd.target!=null &&
    					!pd.target.isDead()) {
    				pd.target.setNoDamageTicks(0);
    			}
    			ev.getPlayer().getEquipment().setItemInMainHand(ev.getItemDrop().getItemStack());
    			boolean ex_version = ItemSet.hasFullSet(ev.getPlayer(), ItemSet.PANROS);
	    		ev.getPlayer().getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
	    		ev.getItemDrop().setPickupDelay(0);
	    		Vector facing = ev.getPlayer().getLocation().getDirection();
	    		if (!second_charge) {
	    			facing = ev.getPlayer().getLocation().getDirection().setY(0);
		    		ev.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW,(ex_version)?7:15,20));
	    		}
	    		if (!ex_version || second_charge) {
		    		aPlugin.API.sendCooldownPacket(ev.getPlayer(), ev.getItemDrop().getItemStack(), LINEDRIVE_COOLDOWN);
		    		aPlugin.API.sendCooldownPacket(ev.getPlayer(), ev.getItemDrop().getItemStack(), LINEDRIVE_COOLDOWN);
		    		pd.last_strikerspell=getServerTickTime();
	    		}
	    		ev.getPlayer().playSound(ev.getPlayer().getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
	    		aPlugin.API.damageItem(ev.getPlayer(), ev.getItemDrop().getItemStack(), (ev.getItemDrop().getItemStack().getType().getMaxDurability()/10)+7);
	    		final PlayerDropItemEvent ev1 = ev;
	    		final Player p1 = ev1.getPlayer();

    			int mult=2;
    			final double xspd=ev1.getPlayer().getLocation().getDirection().getX()*mult;
				double tempyspd=0;
    			final double yspd=tempyspd;
    			final double zspd=ev1.getPlayer().getLocation().getDirection().getZ()*mult; 
    			final double xpos=ev1.getPlayer().getLocation().getX();
    			final double ypos=ev1.getPlayer().getLocation().getY();
    			final double zpos=ev1.getPlayer().getLocation().getZ();
    			
    			final Vector facing1 = facing;
				Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
					public void run() {
			    		ev1.getPlayer().setVelocity(facing1.multiply(8));
			    		GenericFunctions.addIFrame(ev1.getPlayer(), 10);
			    		ev1.getPlayer().playSound(ev1.getPlayer().getLocation(), Sound.ITEM_CHORUS_FRUIT_TELEPORT, 1.0f, 1.0f);

	    				final Location newpos=new Location(ev1.getPlayer().getWorld(),xpos,ypos,zpos);
	        			final double xpos=ev1.getPlayer().getLocation().getX();
	        			final double ypos=ev1.getPlayer().getLocation().getY();
	        			final double zpos=ev1.getPlayer().getLocation().getZ();
						AreaEffectCloud lp = (AreaEffectCloud)ev1.getPlayer().getWorld().spawnEntity(newpos, EntityType.AREA_EFFECT_CLOUD);
						lp.setColor(Color.OLIVE);
						DecimalFormat df = new DecimalFormat("0.00");
						lp.setCustomName("LD "+df.format(NewCombat.CalculateWeaponDamage(ev1.getPlayer(),null))+" "+ev1.getPlayer().getName());
						lp.setRadius(2f);
						lp.setDuration(1);
						lp.setReapplicationDelay(5);
						lp.setBasePotionData(new PotionData(PotionType.INSTANT_DAMAGE));
						lp.setParticle(Particle.FLAME);
						ev1.getPlayer().playSound(ev1.getPlayer().getLocation(), Sound.ENTITY_ARMORSTAND_HIT, 1.0f, 0.5f);
		
		    			int range=8;
		    			final String customname = lp.getCustomName();
		    			for (int i=0;i<range;i++) {
		    				final int tempi=i;
		    				final double xpos2=ev1.getPlayer().getLocation().getX();
		    				final double ypos2=ev1.getPlayer().getLocation().getY();
		    				final double zpos2=ev1.getPlayer().getLocation().getZ();
		    				final Location newpos2=new Location(ev1.getPlayer().getWorld(),xpos2,ypos2,zpos2).add(i*xspd,i*yspd,i*zspd);
		    				Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("TwosideKeeper"), new Runnable() {
		    				public void run() {
			        				AreaEffectCloud lp = (AreaEffectCloud)newpos2.getWorld().spawnEntity(newpos2, EntityType.AREA_EFFECT_CLOUD);
			        				lp.setColor(Color.OLIVE);
			        				lp.setCustomName(customname);
			        				lp.setBasePotionData(new PotionData(PotionType.INSTANT_DAMAGE));
			        				lp.setRadius(2f);
			        				lp.setDuration(1);
				    				lp.setReapplicationDelay(5);
			        				lp.setParticle(Particle.FLAME);
			        				p1.playSound(lp.getLocation(), Sound.ENTITY_ARMORSTAND_HIT, 1.0f, 0.3f);
		    					}
		    				},1);
		    			}
					}
				},(ex_version)?7:15);
				if (ex_version) {
					Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
						public void run() {
				    		aPlugin.API.sendCooldownPacket(ev.getPlayer(), ev.getItemDrop().getItemStack(), LINEDRIVE_COOLDOWN);
				    		aPlugin.API.sendCooldownPacket(ev.getPlayer(), ev.getItemDrop().getItemStack(), LINEDRIVE_COOLDOWN);
				    		pd.last_strikerspell=getServerTickTime();
						}
					},17);
				}
    		}
    	} else {
    		if (ev.getItemDrop().getItemStack().getType().toString().contains("SWORD") &&
        			!GenericFunctions.isViewingInventory(ev.getPlayer())) {
    			ev.setCancelled(true);
    		}
    	}
    } 

    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void onInventoryOpen(InventoryOpenEvent ev) {
    	PlayerStructure pd = (PlayerStructure)playerdata.get(ev.getPlayer().getUniqueId());
    	pd.isViewingInventory=true;
    }
    
    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void onInventoryClose(InventoryCloseEvent ev) {
    	if (ev.getPlayer() instanceof Player) {
    		Player p = (Player)ev.getPlayer();
    		//log("Location of inventory: "+ev.getInventory().getLocation().toString(),2);
        	if (DeathManager.deathStructureExists(p) && ev.getInventory().getTitle().contains("Death Loot")) {
        		Location deathloc = DeathManager.getDeathStructure(p).deathloc;
        		//Whatever is left drops at the death location.
        		if (DeathManager.CountOccupiedSlots(p.getOpenInventory().getTopInventory())>0) {
        			p.sendMessage(ChatColor.GOLD+"The rest of your items have dropped at your death location!");
        		}
        		double amounttotake = DeathManager.CountOccupiedSlots(p.getInventory())*DeathManager.CalculateDeathPrice(p);
        		if (getPlayerMoney(p)>=amounttotake) {
        			givePlayerMoney(p,-amounttotake);
        		} else {
        			double diff = amounttotake-getPlayerMoney(p);
        			givePlayerMoney(p,-getPlayerMoney(p));
        			amounttotake = diff;
            		givePlayerBankMoney(p,-amounttotake);
        		}
				deathloc.getWorld().loadChunk(deathloc.getChunk());
        		for (int i=0;i<p.getOpenInventory().getTopInventory().getSize();i++) {
        			if (p.getOpenInventory().getTopInventory().getItem(i)!=null &&
        					p.getOpenInventory().getTopInventory().getItem(i).getType()!=Material.AIR) {
        				deathloc.getWorld().dropItemNaturally(deathloc, p.getOpenInventory().getTopInventory().getItem(i));
        				log("Dropping "+p.getOpenInventory().getTopInventory().getItem(i).toString()+" at Death location "+deathloc,3);
        			}
        		}
        		DeathManager.removeDeathStructure(p);
        	}
        	
    		PlayerStructure pd = (PlayerStructure) playerdata.get(p.getUniqueId());
        	pd.isViewingInventory=false;
        	log("Closed Inventory.",5);
        	//Check if this is an Item Cube inventory.
        	if (pd.isViewingItemCube && ev.getInventory().getTitle().contains("Item Cube")) {
        		//p.sendMessage("This is an Item Cube inventory.");
        		int id = Integer.parseInt(ev.getInventory().getTitle().split("#")[1]);
        		
        		List<ItemStack> itemcube_contents = new ArrayList<ItemStack>();
        		for (int i=0;i<p.getOpenInventory().getTopInventory().getSize();i++) {
        			if (p.getOpenInventory().getTopInventory().getItem(i)!=null) {
                		//p.sendMessage("Saving item "+p.getOpenInventory().getTopInventory().getItem(i).toString()+" in slot "+i);
        				itemcube_contents.add(p.getOpenInventory().getTopInventory().getItem(i));
        			} else {
                		//p.sendMessage("Saving item AIR in slot "+i);
        				itemcube_contents.add(new ItemStack(Material.AIR));
        			}
        		}
        		p.playSound(p.getLocation(), Sound.BLOCK_CHEST_CLOSE, 1.0f, 1.0f);
        		itemCube_saveConfig(id,itemcube_contents);
        		pd.isViewingItemCube=false;
        	}
        	if (ev.getInventory().getLocation()!=null) {
        		Block b = ev.getInventory().getLocation().getBlock();
        		if (b.getType()==Material.CHEST || b.getType()==Material.TRAPPED_CHEST) {
        			//This is a valid shop. Now update the shop sign for it.
            		final Chest c = (Chest)b.getState();
        			WorldShop.updateShopSign(b);
        			final Sign s = WorldShop.grabShopSign(b);
        			if (s!=null) {
						final int ID = TwosideShops.GetShopID(s);
						Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
						@Override
						public void run() {
							WorldShop shop = TwosideShops.SetupNextItemShop(TwosideShops.LoadWorldShopData(ID), c, s);
							TwosideShops.UpdateSign(shop, shop.getID(), s,shop.isPurchaseShopSign(s));
							TwosideShops.SaveWorldShopData(shop);
						}},1);
        			}
        		}
        	}
    	}
    }
    
    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void onInventoryDrag(InventoryDragEvent ev) {
    	//You are not allowed to drag arrow quivers.
    	if (ev.getOldCursor().getType()==Material.TIPPED_ARROW &&
    			ev.getOldCursor().getEnchantmentLevel(Enchantment.ARROW_INFINITE)==5) {
    		ev.setCancelled(true);
    	}
    }

    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void onItemChange(PlayerItemHeldEvent ev) {
    	final Player player = ev.getPlayer();
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			@Override
			public void run() {
		    	setPlayerMaxHealth(player);
			}
		},1);
		if (GenericFunctions.isHunterCompass(player.getInventory().getItem(ev.getNewSlot()))) {
			player.sendMessage("Calibrating "+player.getInventory().getItem(ev.getNewSlot()).getItemMeta().getDisplayName()+ChatColor.WHITE+"...");
			String name = player.getInventory().getItem(ev.getNewSlot()).getItemMeta().getDisplayName();
	    	if (Math.random()<=0.5) {
	    		if (player.getInventory().getItem(ev.getNewSlot()).getAmount()<=1) {
	    			player.getInventory().getItem(ev.getNewSlot()).setType(Material.AIR);
	    		} else {
	    			player.getInventory().getItem(ev.getNewSlot()).setAmount(player.getInventory().getItem(ev.getNewSlot()).getAmount()-1);
	    		}
				Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
					@Override
					public void run() {
						player.sendMessage("The "+name+ChatColor.WHITE+" is now...");
					}
				},15);
				Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
					@Override
					public void run() {
						player.playSound(player.getLocation(), Sound.BLOCK_METAL_BREAK, 1.0f, 1.0f);
					}
				},20);
				Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
					@Override
					public void run() {
						player.sendMessage(ChatColor.ITALIC+"  Oh my! It appears to have broke!");
					}
				},45);
	    	} else {
				Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
					@Override
					public void run() {
						player.sendMessage("The "+name+ChatColor.WHITE+" is now properly calibrated!");
					}
				},15);
				Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
					@Override
					public void run() {
						player.sendMessage(ChatColor.ITALIC+"  Good luck on your adventure!");
					}
				},45);
				player.setCompassTarget(TwosideKeeper.ELITE_LOCATION);
	    	}
		}
    }
    
    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void onRegainHealth(EntityRegainHealthEvent ev) {
    	if (ev.getRegainReason()==RegainReason.SATIATED && ev.getEntityType()==EntityType.PLAYER) {
    		ev.setCancelled(true);
    		Player p = (Player)ev.getEntity();
	    	//Find the player that is losing food level.
        	PlayerStructure pd = (PlayerStructure)playerdata.get(p.getUniqueId());
			if (((Player)ev.getEntity()).getSaturation()>0 && pd.saturation<20) {
				pd.saturation+=2;
				((Player)ev.getEntity()).setSaturation(((Player)ev.getEntity()).getSaturation()-1);
	    		log("Saturation increased to "+pd.saturation+". Old saturation: "+((Player)ev.getEntity()).getSaturation(),4);
	    	}
    	}
    }
    
    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true) 
    public void onFoodLevelChange(FoodLevelChangeEvent ev){
    	if (ev.getEntityType()==EntityType.PLAYER) {
    		Player p = (Player)ev.getEntity();
    		if (p.getFoodLevel()>ev.getFoodLevel()) {
		    	//Find the player that is losing food level.
		    		PlayerStructure pd = (PlayerStructure)playerdata.get(p.getUniqueId());
		    		if (pd.name.equalsIgnoreCase(p.getName())) {
		    			if (pd.saturation>0) {
		    				pd.saturation--;
		    				ev.setFoodLevel(ev.getFoodLevel()+1);
		    				log("Saturation level is now "+(pd.saturation)+". Food level is now "+p.getFoodLevel(),4);
		    			}
		    		}
    		}
    	}
    }
    
    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void onAnvilPrepareCraftEvent(PrepareAnvilEvent ev) {
    	//The results slot was clicked. We should set the result's item name properly back to what it was.
		if (ev.getResult()!=null &&
				ev.getInventory().getItem(0)!=null &&
				ev.getInventory().getItem(0).getType()!=Material.AIR &&
				ev.getInventory().getItem(0).getItemMeta().hasDisplayName() &&
				ev.getInventory().getItem(1)!=null &&
				ev.getInventory().getItem(1).getType()!=Material.AIR &&
				ev.getResult()!=null &&
				ev.getResult().getType()!=Material.AIR &&
				ev.getResult().hasItemMeta()) {
			//This means we don't rename the item and copy over the old name, since
			//They are repairing it.
			String oldname = ev.getInventory().getItem(0).getItemMeta().getDisplayName();
			//Bukkit.broadcastMessage(oldname);
			ItemMeta m = ev.getResult().getItemMeta();
			m.setDisplayName(oldname);
			ev.getResult().setItemMeta(m);
			ev.setResult(ev.getResult());
		} else { 
			AnvilItem item = new AnvilItem(ev.getInventory().getItem(0),ev.getResult());
			ev.setResult(item.renameItemProperly());
			/*if (ev.getResult()!=null &&
				ev.getInventory().getItem(0)!=null &&
				ev.getInventory().getItem(0).getItemMeta().hasDisplayName()) {
				String oldname = ev.getInventory().getItem(0).getItemMeta().getDisplayName();
				String strippedname = ChatColor.stripColor(oldname);
				String colorcodes = oldname.replace(strippedname, "");
				ItemMeta m = ev.getResult().getItemMeta();
				m.setDisplayName(strippedname.replace(colorcodes, ""));
				ev.getResult().setItemMeta(m);
			}*/
		}
    }
    
    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent ev) {
    	final Player player = (Player)ev.getWhoClicked();
    	PlayerStructure pd = (PlayerStructure)playerdata.get(player.getUniqueId());
    	pd.isViewingInventory=true;
    	log("Raw Slot Clicked: "+ev.getRawSlot(),5); //5,6,7,8 for gear slots.
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			@Override
			public void run() {
		    	setPlayerMaxHealth(player);
			}
		},1);
		
		if (DeathManager.deathStructureExists(player) && ev.getInventory().getTitle().equalsIgnoreCase("Death Loot")) {
			//See how many items are in our inventory. Determine final balance.
			//Count the occupied slots.
			if (getPlayerMoney(player)+getPlayerBankMoney(player)-DeathManager.CalculateDeathPrice(player)*DeathManager.CountOccupiedSlots(player.getInventory())>=DeathManager.CalculateDeathPrice(player)) {
				//player.getInventory().addItem(ev.getCurrentItem());
				if (ev.getCurrentItem()!=null &&
						ev.getCurrentItem().getType()!=Material.AIR) {
					player.getLocation().getWorld().dropItemNaturally(player.getLocation(), ev.getCurrentItem()).setPickupDelay(0);
					ev.setCurrentItem(new ItemStack(Material.AIR));
		
					final DecimalFormat df = new DecimalFormat("0.00");
					Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
						@Override
						public void run() {
							player.sendMessage(ChatColor.BLUE+"New Balance: "+ChatColor.GREEN+"$"+df.format((getPlayerMoney(player)+getPlayerBankMoney(player)-DeathManager.CalculateDeathPrice(player)*DeathManager.CountOccupiedSlots(player.getInventory()))));
						}
					},5);
				}
			} else {
				player.sendMessage(ChatColor.RED+"You cannot afford to salvage any more items!");
			}
			ev.setCancelled(true);
		}
    	//Check for a left click for an arrow quiver.
    	if (ev.getClick()==ClickType.LEFT) {
    		//Tries to take out 1 stack of arrows.
    		//We're going to try to deposit arrows.
    		if (ev.getCursor()!=null && ev.getCursor().getAmount()>0 &&
    				ev.getCursor().getType()==Material.ARROW) {
	    		Player p = (Player)ev.getWhoClicked();
	    		if (playerHasArrowQuiver(p)) {
	    			boolean foundquiver=false;
	    			int slot=-1;
					if (p.getInventory().getItem(ev.getSlot())!=null &&
							p.getInventory().getItem(ev.getSlot()).getType()==Material.TIPPED_ARROW &&
							p.getInventory().getItem(ev.getSlot()).getEnchantmentLevel(Enchantment.ARROW_INFINITE)==5) {
						//This is an arrow quiver.
						foundquiver=true;
						slot=ev.getSlot();
					}
	    			if (foundquiver) {
	    				log("An arrow quiver was right clicked.",4);
	    				//Continue.
	    				//Deposit the arrows we are holding.
	    				
	    				int amt=playerGetArrowQuiverAmt(p, slot);
						playerInsertArrowQuiver(p, slot , ev.getCursor().getAmount());
						p.sendMessage(ChatColor.DARK_GRAY+""+ev.getCursor().getAmount()+" arrow"+((ev.getCursor().getAmount()==1)?"":"s")+" "+((ev.getCursor().getAmount()==1)?"was":"were")+" added to your arrow quiver. Arrow Count: "+ChatColor.GRAY+playerGetArrowQuiverAmt(p,playerGetArrowQuiver(p)));
						ev.setCursor(new ItemStack(Material.AIR));
						//Cancel this click event.
						ev.setCancelled(true);
						ev.setResult(Result.DENY);
						
	    			}
	    		}
    		}
    	}
    	
    	//Check for a right click for an arrow quiver.
    	if (ev.getClick()==ClickType.RIGHT &&
    			ev.getCursor().getType()==Material.AIR) {
    		//Tries to take out 1 stack of arrows.
    		Player p = (Player)ev.getWhoClicked();
    		if (playerHasArrowQuiver(p)) {
    			boolean foundquiver=false;
    			int slot=-1;
				if (p.getInventory().getItem(ev.getSlot())!=null &&
						p.getInventory().getItem(ev.getSlot()).getType()==Material.TIPPED_ARROW &&
						p.getInventory().getItem(ev.getSlot()).getEnchantmentLevel(Enchantment.ARROW_INFINITE)==5) {
					//This is an arrow quiver.
					foundquiver=true;
					slot=ev.getSlot();
				}
    			if (foundquiver) {
    				log("An arrow quiver was right clicked.",4);
    				//Continue.
    				//Try to withdraw 64 arrows.
    				int amt=playerGetArrowQuiverAmt(p, slot);
					playerRemoveArrowQuiver(p, slot , (amt>64)?64:amt);
					//Cancel this click event so we can grab the arrows inside.
					ev.setCancelled(true);
					ev.setResult(Result.DENY);
					ItemStack arrow = new ItemStack(Material.ARROW,(amt>64)?64:amt);
					ev.setCursor(arrow);
    			}
    		}
    	}
    	
    	//LEFT CLICK STUFF.
    	//WARNING! This only happens for ITEM CUBES! Do not add other items in here!
    	pd = (PlayerStructure) playerdata.get(ev.getWhoClicked().getUniqueId());
    	final InventoryClickEvent store = ev;
    	if (pd.isViewingItemCube &&
    			(ev.getInventory().getType()!=InventoryType.WORKBENCH ||
    			(ev.getInventory().getType()==InventoryType.WORKBENCH && ev.getRawSlot()>9)) && ev.getInventory().getTitle().contains("Item Cube #")) {
    		log("Item Cube window identified.",5);
    		final int id=Integer.parseInt(ev.getInventory().getTitle().split("#")[1]);
			//Check to see if the cursor item is an item cube.
    		/* OLD ITEM CUBE DUPLICATION CHECK
			Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
				@Override
				public void run() {
					log("Click detected inside an item cube... Scan the inventory for anything strange.",5);
					int siz=9;
					if (itemCube_getCubeType(id)!=CubeType.NORMAL) {
						siz=27;
					}
					//See if any of the top slots contain an item cube of the same number... They should NOT!
					for (int i=0;i<store.getInventory().getSize();i++) {
						if (store.getInventory().getItem(i)!=null &&
								store.getInventory().getItem(i).hasItemMeta() &&
								store.getInventory().getItem(i).getItemMeta().hasLore()) {
							for (int j=0;j<store.getInventory().getItem(i).getItemMeta().getLore().size();j++) {
								if (store.getInventory().getItem(i).getItemMeta().getLore().get(j).contains(ChatColor.DARK_PURPLE+"ID#")) {
									//Get the ID...
									int clicked_id = Integer.parseInt(store.getInventory().getItem(i).getItemMeta().getLore().get(j).split("#")[1]);
									if (clicked_id==id) {
										//This is the same ID as the one we are viewing...Kick that out of there!
										store.getWhoClicked().getWorld().dropItem(store.getWhoClicked().getLocation(), store.getInventory().getItem(i));
										store.getInventory().setItem(i, new ItemStack(Material.AIR));
										break;
									}
								}
							}
						}
					}
				}},1);
				*/
			/*//OLD ENDER ITEM CUBE CODE.
    		if (itemCube_getCubeType(id)==CubeType.ENDER) {
        		log("Ender Item Cube verified.",4);
    			//We are going to look at all players and see if they have this inventory open.
    			final List<ItemStack> itemlist = new ArrayList<ItemStack>();
				Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
					@Override
					public void run() {
		    	    	for (int i=0;i<store.getInventory().getSize();i++) {
		    	    		if (store.getInventory().getItem(i)!=null) {
			    	    		itemlist.add(store.getInventory().getItem(i));
		    	    		} else {
		    	    			itemlist.add(new ItemStack(Material.AIR));
		    	    		}
		    	    	}
						itemCube_saveConfig(id,itemlist,CubeType.ENDER);
					}
				},2);
    			for (int i=0;i<Bukkit.getServer().getOnlinePlayers().toArray().length;i++) {
    				//Make sure the player we are checking is not ourself.
    				final Player p = (Player)Bukkit.getServer().getOnlinePlayers().toArray()[i];
    				if (p.getOpenInventory()!=null &&
    						!p.getName().equalsIgnoreCase(ev.getWhoClicked().getName()) &&
    						p.getOpenInventory().getTitle().contentEquals(ev.getInventory().getTitle())) {

						p.closeInventory();
    					Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
							@Override
							public void run() {
								p.openInventory(Bukkit.getServer().createInventory(p, 27, "Item Cube #"+id));
								p.playSound(p.getLocation(),Sound.BLOCK_CHEST_OPEN,1.0f,1.0f);
							}
						},10);
    				}
    			}
    		}*/
    	}
    	
    	if ((ev.getInventory().getType()!=InventoryType.WORKBENCH ||
    			(ev.getInventory().getType()==InventoryType.WORKBENCH && ev.getRawSlot()>9)) && ev.getCurrentItem()!=null) {
	    	if (ev.getCurrentItem().hasItemMeta() && (ev.getCurrentItem().getType()!=Material.AIR)) {
	    		ItemMeta item_meta = ev.getCurrentItem().getItemMeta();
	    		if (item_meta.hasLore()) {
	    			List<String> item_meta_lore = item_meta.getLore();
	    			if (item_meta_lore.size()==4 && item_meta_lore.get(3).contains(ChatColor.DARK_PURPLE+"ID#")) {
	    				int idnumb = Integer.parseInt(item_meta_lore.get(3).split("#")[1]);
	    				int itemcubeid = -1;
	    				if (((PlayerStructure)playerdata.get(ev.getWhoClicked().getUniqueId())).isViewingItemCube &&
	    						ev.getWhoClicked().getOpenInventory().getTitle().contains("Item Cube #")) {
	    					itemcubeid = Integer.parseInt(ev.getWhoClicked().getOpenInventory().getTitle().split("#")[1]); //This is the ID of the window we are looking at, if one exists.
	    				} else {
	    					itemcubeid = -1;
	    				}
	    				CubeType cubetype = CubeType.NORMAL;
	    				//This is an Item Cube.
						//Check to see if the cursor item is an item cube.
						if ((ev.getCurrentItem().getType()==Material.CHEST ||
								ev.getCurrentItem().getType()==Material.STORAGE_MINECART ||
								ev.getCurrentItem().getType()==Material.ENDER_CHEST) &&
								ev.getCurrentItem().hasItemMeta() &&
								ev.getCurrentItem().getItemMeta().hasLore()) {
							log("The clicked item has lore...",5);
							for (int i=0;i<ev.getCurrentItem().getItemMeta().getLore().size();i++) {
								if (ev.getCurrentItem().getItemMeta().getLore().get(i).contains(ChatColor.DARK_PURPLE+"ID#")) {
									log("We clicked an item cube, checking ID.",5);
									//We clicked an item cube. Check its ID.
									int clicked_id = Integer.parseInt(ev.getCurrentItem().getItemMeta().getLore().get(i).split("#")[1]);
									log("ID is "+clicked_id+" and we are viewing "+itemcubeid,5);
									if (clicked_id==itemcubeid) {
										//The inventory we are viewing is the same as the item cube we have clicked on!
										//Stop this before the player does something dumb!
										//Player p = ((Player)ev.getWhoClicked());
										//p.playSound(p.getLocation(), Sound.BLOCK_NOTE_HARP, 0.4f, 0.2f);
										ev.setCancelled(true);
										break;
									}
								}
							}
						}
		}}}}

    	//WARNING! This only happens for ITEM CUBES! Do not add other items in here!
    	if ((ev.getInventory().getType()!=InventoryType.WORKBENCH ||
    			(ev.getInventory().getType()==InventoryType.WORKBENCH && ev.getRawSlot()>9)) && ev.isLeftClick() && ev.getCurrentItem()!=null && ev.getCursor()!=null) {
	    	if (ev.getCurrentItem().hasItemMeta() && (ev.getCursor().getType()!=Material.AIR)) {
	    		ItemMeta item_meta = ev.getCurrentItem().getItemMeta();
	    		if (item_meta.hasLore()) {
	    			List<String> item_meta_lore = item_meta.getLore();
	    			if (item_meta_lore.size()==4 && item_meta_lore.get(3).contains(ChatColor.DARK_PURPLE+"ID#")) {
	    				int idnumb = Integer.parseInt(item_meta_lore.get(3).split("#")[1]);
	    				int itemcubeid = -1; //This is the ID of the window we are looking at, if one exists.
	    				CubeType cubetype = CubeType.NORMAL;
	    				//This is an Item Cube.
    					ev.setCancelled(true);
    					//ev.setResult(Result.DENY);
    					
    					int size;
    					if (ev.getCurrentItem().getType()==Material.CHEST) {
    						size=9;
    						cubetype=CubeType.NORMAL;
    					} else {
    						size=27;
        					if (ev.getCurrentItem().getType()==Material.STORAGE_MINECART) {
        						cubetype=CubeType.LARGE;
        					} else {
        						cubetype=CubeType.ENDER;
        					}
    					}
    					

    					int clicked_size;
    					if (ev.getCurrentItem().getType()==Material.CHEST) {
    						clicked_size=9;
    						cubetype=CubeType.NORMAL;
    					} else {
    						clicked_size=27;
        					if (ev.getCurrentItem().getType()==Material.STORAGE_MINECART) {
        						cubetype=CubeType.LARGE;
        					} else {
        						cubetype=CubeType.ENDER;
        					}
    					}
    					
    					//See if we're looking at an Item Cube inventory already.
    					if (((PlayerStructure)playerdata.get(ev.getWhoClicked().getUniqueId())).isViewingItemCube && ev.getInventory().getTitle().contains("Item Cube")) {
    						//Check to see what the Item Cube ID is.
    						itemcubeid=Integer.parseInt(ev.getInventory().getTitle().split("#")[1]);
    					}
    					

    					
    					/* OLD ENDER ITEM CUBE CODE
    		    		if (itemCube_getCubeType(idnumb)==CubeType.ENDER) {
    		        		log("This is an Ender Item Cube transfer click.",5);
    		    			//We are going to look at all players and see if they have this inventory open.
    		    			final int id = idnumb;
    		    			for (int i=0;i<Bukkit.getServer().getOnlinePlayers().toArray().length;i++) {
    		    				//Make sure the player we are checking is not ourself.
    		    				final Player p = (Player)Bukkit.getServer().getOnlinePlayers().toArray()[i];
    		    				if (p.getOpenInventory()!=null &&
    		    						!p.getName().equalsIgnoreCase(ev.getWhoClicked().getName()) &&
    		    						p.getOpenInventory().getTitle().contentEquals("Item Cube #"+idnumb)) {
    		    					Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
    									@Override
    									public void run() {
    										p.openInventory(Bukkit.getServer().createInventory(p, 27, "Item Cube #"+id));
    										p.playSound(p.getLocation(),Sound.BLOCK_CHEST_OPEN,1.0f,1.0f);
    									}
    								},10);
    		    				}
    		    			}
    		    		}*/
    					
    					//Check if cursor is similar.
    					if (ev.getCursor().isSimilar(ev.getCurrentItem()) &&
    							cubetype==CubeType.ENDER) {
    						//Don't allow it.
    						ev.setCancelled(true);
    						ev.setCursor(ev.getCurrentItem());
    					}
    					else
    					//Make sure we are not already inside the cube we're placing into.
    					if (idnumb!=itemcubeid) {
    							//See if someone has this inventory opened already.
    							Inventory virtualinventory = null;
    							virtualinventory = ItemCube.getViewingItemCubeInventory(idnumb, (Player)ev.getWhoClicked());
    							if (virtualinventory==null) {
    								virtualinventory = Bukkit.createInventory((Player)ev.getWhoClicked(), clicked_size);
        							log("Inventory size is "+clicked_size,5);
    	    						List<ItemStack> items = itemCube_loadConfig(idnumb);
    	    						for (int i=0;i<virtualinventory.getSize();i++) {
    	    							if (items.get(i)!=null) {
    	    								virtualinventory.setItem(i, items.get(i));
    	        							log("Load up with "+items.get(i).toString(),5);
    	    							}
    	    						}
    	    						ItemCube.addToViewersOfItemCube(idnumb,ev.getCursor(),(Player)ev.getWhoClicked());
    							}
	    						HashMap result = virtualinventory.addItem(ev.getCursor());
	    						log("Clicked ID number "+idnumb,5);
	    						//Set whatever's left back to the cursor.
	    						if (result.size()>0) {
	    							ev.setCursor((ItemStack)result.get(0));
	    						} else {
	    							ev.setCursor(new ItemStack(Material.AIR));
	    							log("Cursor should be air.",5);
	    						}
	    						List<ItemStack> itemslist = new ArrayList<ItemStack>();
	    						for (int i=0;i<virtualinventory.getSize();i++) {
	    							itemslist.add(virtualinventory.getItem(i));
	    						}
	    						itemCube_saveConfig(idnumb,itemslist);
    						} else {
    						//Well, we're already in here, I don't know why they didn't just use the
    						//minecraft inventory management system. Now I have to do math...
							//Add it to the inventory being viewed.
							HashMap result = ev.getWhoClicked().getOpenInventory().getTopInventory().addItem(ev.getCursor());
							//Add it to everyone viewing the cube.
							//ItemCube.addToViewersOfItemCube(idnumb, ev.getCursor(), (Player)ev.getWhoClicked());

    						if (result.size()>0) {
    							ev.setCursor((ItemStack)result.get(0));
    						} else {
    							ev.setCursor(new ItemStack(Material.AIR));
    						}
    						List<ItemStack> itemslist = new ArrayList<ItemStack>();
    						for (int i=0;i<ev.getWhoClicked().getOpenInventory().getTopInventory().getSize();i++) {
    							itemslist.add(ev.getWhoClicked().getOpenInventory().getTopInventory().getItem(i));
    						}
    						itemCube_saveConfig(idnumb,itemslist);
    					}
	    			}
	    		}
	    	}
    	}
    	
    	//RIGHT CLICK STUFF DOWN HERE.
    	log("Inventory click.",5);
    	//WARNING! This only happens for ITEM CUBES! Do not add other items in here!
    	if ((ev.getInventory().getType()!=InventoryType.WORKBENCH ||
    			(ev.getInventory().getType()==InventoryType.WORKBENCH && ev.getRawSlot()>9)) && ev.isRightClick() && ev.getCurrentItem()!=null && ev.getCurrentItem().getAmount()==1) {
	    	log("Clicked Item: "+ev.getCurrentItem().toString(),5);
	    	if (ev.getCurrentItem().hasItemMeta()) {
	        	log("Item Meta: "+ev.getCurrentItem().getItemMeta().toString(),5);
	    		ItemMeta item_meta = ev.getCurrentItem().getItemMeta();
	    		if (item_meta.hasLore()) {
	    			List<String> item_meta_lore = item_meta.getLore();
	    			if (item_meta_lore.size()==4 && item_meta_lore.get(3).contains(ChatColor.DARK_PURPLE+"ID#")) {
	    				int idnumb = Integer.parseInt(item_meta_lore.get(3).split("#")[1]);
	    				log("This is an Item Cube.",5);
	    				Player p = (Player)ev.getWhoClicked();
    					int inventory_size;
    					if (ev.getCurrentItem().getType()==Material.CHEST) {
    						inventory_size=9;
    					} else {
    						inventory_size=27;
    					}
	    				if (!ItemCube.isSomeoneViewingItemCube(idnumb,p)) {
	    		    		log("Attempting to open",5);
	    					ev.setCancelled(true);
	    					ev.setResult(Result.DENY);
	    					//pd.itemcubeviews.add(p.getOpenInventory());
    						InventoryView newinv = p.openInventory(Bukkit.getServer().createInventory(p, inventory_size, "Item Cube #"+idnumb));
    						openItemCubeInventory(newinv.getTopInventory(),newinv);
    						pd.isViewingItemCube=true;
    						p.playSound(p.getLocation(),Sound.BLOCK_CHEST_OPEN,1.0f,1.0f);
						} else {
	    					ev.setCancelled(true);
	    					ev.setResult(Result.DENY);
	    					//ItemCube.displayErrorMessage(p);
	    					//pd.itemcubeviews.add(p.getOpenInventory());
	    					p.openInventory(ItemCube.getViewingItemCubeInventory(idnumb, p));
	        				pd.isViewingItemCube=true;
	    	    			p.playSound(p.getLocation(), Sound.BLOCK_CHEST_OPEN, 1.0f, 1.0f);
						}
	    			}
	    		}
	    	}
    	}
    }

    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void onItemSpawn(ItemSpawnEvent ev) {
    	//If the item is of a rare type, we will highlight it for emphasis.
    	Item it = ev.getEntity();
		if ((Artifact.isArtifact(it.getItemStack()) /*&&
				!Artifact.isMysteriousEssence(it.getItemStack())*/ ||
				GenericFunctions.isRareItem(it.getItemStack()))) {
			it.setCustomName((it.getItemStack().getItemMeta().hasDisplayName())?it.getItemStack().getItemMeta().getDisplayName():GenericFunctions.UserFriendlyMaterialName(it.getItemStack()));
			it.setCustomNameVisible(true);
		}
    	if (GenericFunctions.isArtifactEquip(it.getItemStack())) {
    		it.setInvulnerable(true);
    	}
    }
    
    /**
     * RECYCLING CENTER CODE!
     */
    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void onItemDespawn(ItemDespawnEvent ev) {
    	Item i = ev.getEntity();
    	//If the item is a display item, respawn it.
    	
    	if (Artifact.isMysteriousEssence(i.getItemStack())) {
    		EssenceLogger.AddEssenceDespawn();
    	}
    	
    	if (i!=null && i.getCustomName()!=null &&
    			i.getItemStack().hasItemMeta() &&
    			i.getItemStack().getItemMeta().hasLore() &&
    			i.getItemStack().getItemMeta().getLore().contains("WorldShop Display Item")) {
		    		if (WorldShop.hasShopSignAttached(i.getLocation().add(0,-0.5,-0.5).getBlock())) {
			    		Item e = (Item)(i.getWorld().dropItem(i.getLocation(), i.getItemStack()));
			    		e.setCustomName(i.getCustomName());
			    		e.setGlowing(i.isGlowing());
			    		e.setItemStack(i.getItemStack());
			    		e.setCustomNameVisible(i.isCustomNameVisible());
			    		e.setVelocity(new Vector(0,0,0));
			    		e.setPickupDelay(Integer.MAX_VALUE);
			    		e.teleport(i.getLocation());
			    		log("Respawn this shop item.",5);
    		}
    	}
    	TwosideRecyclingCenter.AddItemToRecyclingCenter(i);
    }
    
    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void onChunkLoadEvent(ChunkLoadEvent ev) {
    	//Grab all entities. Create monster structures for all monsters. Detect elites and leaders and set their status accordingly.
    	Entity[] entities = ev.getChunk().getEntities();
    	for (int i=0;i<entities.length;i++) {
    		if (entities[i]!=null && entities[i].isValid() && (entities[i] instanceof Monster)) {
    			Monster m = (Monster)entities[i];
    			MonsterStructure ms = MonsterStructure.getMonsterStructure(m);
    			MonsterDifficulty md = MonsterController.getMonsterDifficulty(m);
    			if (md == MonsterDifficulty.ELITE) {
    				ms.SetElite(true);
    			}
    			if (MonsterController.isZombieLeader(m)) {
    				ms.SetLeader(true);
    			}
    		}
    	}
    }
    
    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void MonsterSpawnEvent(CreatureSpawnEvent ev) {

    	if (ev.getEntity() instanceof Monster) {
    		Monster m = (Monster)ev.getEntity();
    		if (!habitat_data.addNewStartingLocation(ev.getEntity())) {
    			ev.getEntity().remove();
    			ev.setCancelled(true);
    		}
    		MonsterStructure.getMonsterStructure(m);
    	}
    	
    	
    	if ((ev.getSpawnReason().equals(SpawnReason.DISPENSE_EGG) || 
    			ev.getSpawnReason().equals(SpawnReason.EGG)) &&
    			NewCombat.trimNonLivingEntities(ev.getEntity().getNearbyEntities(8, 8, 8)).size()>20) {
    		ev.setCancelled(true);
    		log("Denied chicken spawn.",4);
    	}
    	
    	if ((ev.getSpawnReason().equals(SpawnReason.NATURAL) ||
    			ev.getSpawnReason().equals(SpawnReason.SPAWNER_EGG) ||
    			ev.getSpawnReason().equals(SpawnReason.REINFORCEMENTS) ||
    			ev.getSpawnReason().equals(SpawnReason.VILLAGE_INVASION)) &&
    			ev.getEntity() instanceof Monster) {
    		if (ev.getSpawnReason().equals(SpawnReason.REINFORCEMENTS) || ev.getSpawnReason().equals(SpawnReason.VILLAGE_INVASION)) {
    			//Remove this one and spawn another one.
    			Location loc = ev.getEntity().getLocation().clone();
    			Monster m = (Monster)loc.getWorld().spawnEntity(loc, ev.getEntityType());
    			m.setTarget(((Monster)ev.getEntity()).getTarget());
    			MonsterController.MobHeightControl(m,true);
    			if (m.getCustomName()!=null) {
    				m.setCustomName(m.getCustomName()+" Minion");
    			} else {
    				m.setCustomName("Zombie Minion");
    			}
    			ev.getEntity().remove();
    		} else
    		if (!MonsterController.MobHeightControl(ev.getEntity(),false)) {
    			ev.setCancelled(true);
    			//This spawn was not allowed by the mob height controller.
    		}
    	} else {
        	log("Reason for spawn: "+ev.getSpawnReason().toString(),4);
    	}
    	if (ev.getLocation().getWorld().getName().equalsIgnoreCase("world") &&
    			ev.getEntityType()==EntityType.HORSE) {
    		Horse h = (Horse)ev.getEntity();
    		if (h.getVariant().equals(Variant.SKELETON_HORSE)) {
    			//This is a skeleton horse in the overworld. We are going to disable these for now. Future plans for them...
    			ev.getEntity().remove();
    			log("Prevented a skeleton horse from spawning at Location "+ev.getLocation().toString()+".",3);
    		}
    	}
    }
    
    //A fix to make achievemnt announcements not show the healthbar!
    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void playerGetAchievementEvent(PlayerAchievementAwardedEvent ev) {
    	final Player p = ev.getPlayer();
    	ev.getPlayer().getScoreboard().getTeam(ev.getPlayer().getName().toLowerCase()).setSuffix("");
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			public void run() {
				if (p!=null) {
					p.getScoreboard().getTeam(p.getName().toLowerCase()).setSuffix(createHealthbar(((p.getHealth())/p.getMaxHealth())*100,p));
					p.getScoreboard().getTeam(p.getName().toLowerCase()).setPrefix(GenericFunctions.PlayerModePrefix(p));
				}
			}}
		,5);
    }

    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void PotionSplash(PotionSplashEvent ev) {
    	ThrownPotion tp = (ThrownPotion)ev.getEntity();
    	LivingEntity ps = (LivingEntity)tp.getShooter();
    	if (ps instanceof Witch) {
    		//We know a witch threw this. Apply Poison IV to all affected entities.
    		Witch w = (Witch)ps;
    		boolean isPoison=false;
    		int duration=0;
			for (int j=0;j<ev.getPotion().getEffects().size();j++) {
				if (Iterables.get(ev.getPotion().getEffects(), j).getType().equals(PotionEffectType.POISON)) {
					isPoison=true;
					duration=Iterables.get(ev.getPotion().getEffects(), j).getDuration();
					break;
				}
			}
			if (isPoison) {
				for (int i=0;i<ev.getAffectedEntities().size();i++) {
					switch (MonsterController.getMonsterDifficulty(w)) {
						case DANGEROUS:{
							Iterables.get(ev.getAffectedEntities(), i).addPotionEffect(new PotionEffect(PotionEffectType.POISON,duration+1,1)); //Poison II
						}break;
						case DEADLY:{
							Iterables.get(ev.getAffectedEntities(), i).addPotionEffect(new PotionEffect(PotionEffectType.POISON,duration+1,2)); //Poison III
						}break;
						case HELLFIRE:{
							Iterables.get(ev.getAffectedEntities(), i).addPotionEffect(new PotionEffect(PotionEffectType.POISON,duration+1,3)); //Poison IV
						}break;
					}
				}
			}
    	}
    }
    
    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void updateHealthbarDamageEvent(EntityDamageEvent ev) {
    	Entity e = ev.getEntity();
    	
    	log(ev.getCause().toString(),5);
    	
    	log(ev.getDamage()+"",5);
		
		if (ev.getCause()==DamageCause.FIRE || ev.getCause()==DamageCause.FIRE_TICK ||
				ev.getCause()==DamageCause.WITHER || ev.getCause()==DamageCause.POISON
				 || ev.getCause()==DamageCause.THORNS) {
			if (ev.getEntity() instanceof LivingEntity) {
        		ev.setDamage(DamageModifier.MAGIC,0);
        		ev.setDamage(DamageModifier.RESISTANCE,0);
        		ev.setDamage(DamageModifier.ARMOR,0);
        		//Calculate as true damage.
        		if (ev.getEntity() instanceof Player) {
        			Player p = (Player)ev.getEntity();
        			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
        			if (pd.hitlist.containsKey(p.getUniqueId())) {
        				if (pd.hitlist.get(p.getUniqueId())+10>TwosideKeeper.getServerTickTime()) {
        					ev.setCancelled(true);
        				} else {
        					pd.hitlist.put(p.getUniqueId(), TwosideKeeper.getServerTickTime());
        				}
        			} else {
        				pd.hitlist.put(p.getUniqueId(), TwosideKeeper.getServerTickTime());
        			}
        		}
        		if (ev.getEntity() instanceof Monster) {
        			Monster m = (Monster)ev.getEntity();
        			MonsterStructure md = MonsterStructure.getMonsterStructure(m);
        			if (md.hitlist.containsKey(m.getUniqueId())) {
        				if (md.hitlist.get(m.getUniqueId())+10>TwosideKeeper.getServerTickTime()) {
        					ev.setCancelled(true);
        				} else {
        					md.hitlist.put(m.getUniqueId(), TwosideKeeper.getServerTickTime());
        				}
        			} else {
            			md.hitlist.put(m.getUniqueId(), TwosideKeeper.getServerTickTime());	
        			}
        		}
			}
		}
		
		if (!ev.isCancelled()) {
			if (e instanceof LivingEntity) {
				LivingEntity l = (LivingEntity)e;
				
				
	    		int poisonlv = 0;
	    		if (l.hasPotionEffect(PotionEffectType.POISON)) {
					for (int j=0;j<l.getActivePotionEffects().size();j++) {
						if (Iterables.get(l.getActivePotionEffects(), j).getType().equals(PotionEffectType.POISON)) {
							poisonlv = Iterables.get(l.getActivePotionEffects(), j).getAmplifier()+1;
							break;
						}
					}
					if (poisonlv>0 && ev.getCause()!=DamageCause.POISON) {
						ev.setDamage(ev.getDamage()+(ev.getDamage()*poisonlv*0.5));
						log("New damage set to "+ev.getDamage()+" from Poison "+poisonlv,5);
					}
	    		}
	    		
	    		if (l instanceof Monster) {
	
	        		if (l.hasPotionEffect(PotionEffectType.BLINDNESS)) {
	    				for (int j=0;j<l.getActivePotionEffects().size();j++) {
	    					if (Iterables.get(l.getActivePotionEffects(), j).getType().equals(PotionEffectType.BLINDNESS)) {
	    						poisonlv = Iterables.get(l.getActivePotionEffects(), j).getAmplifier()+1;
	    						break;
	    					}
	    				}
	    				if (poisonlv>0) {
	    					ev.setDamage(ev.getDamage()+(ev.getDamage()*poisonlv*0.5));
	    					log("New damage set to "+ev.getDamage()+" from Poison "+poisonlv,5);
	    				}
	        		}
	    		}
			}
			
	    	if (e instanceof Player) {
	        	log("Damage reason is "+ev.getCause().toString(),4);
	    		final Player p = (Player)e;
	    		
	    		if (GenericFunctions.isDefender(p) && p.isBlocking()) {
	            	log("Reducing knockback...",3);
	        		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
	        			public void run() {
	        				if (p!=null) {
	        	            	p.setVelocity(p.getVelocity().multiply(0.25));
	        				}
	        			}}
	        		,1);
	    		}
	    		
	    		if (ev.getCause()==DamageCause.BLOCK_EXPLOSION) {
	        		//Calculate new damage based on armor worn.
	        		//Remove all other damage modifiers since we will calculate it manually.
	        		ev.setDamage(DamageModifier.BLOCKING,0);
	        		ev.setDamage(DamageModifier.MAGIC,0);
	        		ev.setDamage(DamageModifier.RESISTANCE,0);
	        		ev.setDamage(DamageModifier.ARMOR,0);
	        		
	        		//Damage reduction is also set based on how much blast resistance we have.
	
	        		ItemStack[] armor = p.getEquipment().getArmorContents();
	        		
	        		int protectionlevel = 0;
	        		for (int i=0;i<armor.length;i++) {
	        			if (armor[i]!=null &&
	        					armor[i].getType()!=Material.AIR) {
	        				protectionlevel+=armor[i].getEnchantmentLevel(Enchantment.PROTECTION_EXPLOSIONS);
	        			}
	        		}
	        		
	        		ev.setDamage(DamageModifier.BASE,NewCombat.CalculateDamageReduction(ev.getDamage()*EXPLOSION_DMG_MULT*((100-protectionlevel)*0.01),p,null));
	        		ev.setDamage(NewCombat.CalculateDamageReduction(ev.getDamage()*EXPLOSION_DMG_MULT*((100-protectionlevel)*0.01),p,null));
	    			log("Explosion Damage is "+ev.getDamage(),5);
	        		//ev.setDamage(CalculateDamageReduction(ev.getDamage()*EXPLOSION_DMG_MULT,p,null));
	    		}
	    		
	    		if (ev.getCause()==DamageCause.VOID) {
	    			Location p_loc = p.getLocation();
	    			double totalmoney = getPlayerMoney(p);
	    			if (totalmoney>=0.01) {
		    			p_loc.setY(0);
		    			p.teleport(p_loc);
		    			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,20*2 /*Approx 2 sec of no movement.*/,10));
		    			p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*18 /*Approx 18 sec to reach height 100*/,6));
		    			p.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION,20*18 /*Approx 18 sec to reach height 100*/,6));
		    			p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,20*26 /*Reduces fall damage temporarily.*/,500));
		    			DecimalFormat df = new DecimalFormat("0.00");
		    			double rand_amt = 0.0;
		    			if (totalmoney>5) {
		    				rand_amt = Math.random()*5;
		    			} else {
		    				rand_amt = Math.random()*getPlayerMoney(p);
		    			}
		    			p.sendMessage("A Mysterious Entity forcefully removes "+ChatColor.YELLOW+"$"+df.format(rand_amt)+ChatColor.WHITE+" from your pockets.");
		    			givePlayerMoney(p, -rand_amt);
		        		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
		        			public void run() {
		        				if (p!=null) {
		        					p.sendMessage(ChatColor.AQUA+""+ChatColor.ITALIC+"  \"Enjoy the ride!\"");
		        				}
		        			}}
		        		,40);
	    			} else {
	    				PlayerStructure pd = (PlayerStructure)playerdata.get(p.getUniqueId());
	    				if (pd.last_laugh_time+400<getServerTickTime()) {
	    					p.sendMessage(ChatColor.RED+""+ChatColor.ITALIC+"A Mysterious Entity looks at your empty pockets with disdain, then laughs chaotically as you fall to your doom.");
	    					pd.last_laugh_time=getServerTickTime();
	    				}
	    			}
	    		}
	    		
	    		//See if we're in a party with a defender.
	    		for (int i=0;i<PartyList.size();i++) {
	    			Party check = PartyList.get(i);
	    			if (check.IsInParty(p)) {
		    			for (int j=0;j<check.partyplayers.size();j++) {
		    				//See if there's a defender blocking in there.
		    				Player pcheck = check.partyplayers.get(j);
		    				if (GenericFunctions.isDefender(pcheck) &&
		    						pcheck.isBlocking() &&
		    						!p.equals(check.partyplayers.get(j))) {
		    					//This is a defender. Transfer half the damage to them!
		    					ev.setDamage(ev.getDamage()/2);
		    					//Send the rest of the damage to the defender.
		    					double dmg = ev.getDamage()/2;
		    					dmg=NewCombat.CalculateDamageReduction(dmg, pcheck, ev.getEntity());
		    					if (pcheck.getHealth()-dmg<0) {
		    						pcheck.setHealth(0);
		    					} else {
		    						pcheck.setHealth(pcheck.getHealth()-dmg);
		    					}
		    					log("Damage was absorbed by "+pcheck.getName()+". Tanked "+dmg+" damage. Original damage: "+ev.getDamage()/2,4);
		    					break;
		    				}
		    			}
		    			break;
	    			}
	    		} 
	
	    		//If glowing, the player is invulnerable.
	    		if (p.hasPotionEffect(PotionEffectType.GLOWING)) {
	    			p.setNoDamageTicks(20);
	    			ev.setCancelled(true);
	    		} else {
	    			//Dodge should not activate when we have invincibility frames.
	    			
		    		//final double pcthp = ((p.getHealth())/p.getMaxHealth())*100;
		
		    		double dodgechance = NewCombat.CalculateDodgeChance(p);
		    		
		    		if (ev.getCause()==DamageCause.THORNS &&
		    				GenericFunctions.isRanger(p)) {
		    			dodgechance=1;
		    			double dmg = p.getHealth()-0.25;
		    			if (!GenericFunctions.AttemptRevive(p,dmg)) {
	                		ev.setDamage(dmg);
	    	    			p.playSound(p.getLocation(), Sound.ENCHANT_THORNS_HIT, 0.8f, 3.0f);
	            		} else {
	            			ev.setCancelled(true);
	            		}
		    		}
		    		
		    		PlayerStructure pd = (PlayerStructure)playerdata.get(p.getUniqueId());
		    		
		    		if (pd.fulldodge) {
		    			pd.fulldodge=false;
		    		}
		    		
		    		if (Math.random()<=dodgechance) {
		    			//Cancel this event, we dodged the attack.
		    			p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 3.0f, 1.0f);
		    			log("Triggered Dodge.",3);
		    			for (int i=0;i<p.getEquipment().getArmorContents().length;i++) {
		    				ItemStack equip = p.getEquipment().getArmorContents()[i];
		    				if (ArtifactAbility.containsEnchantment(ArtifactAbility.GRACEFULDODGE, equip)) {
		    					p.addPotionEffect(
		    							new PotionEffect(PotionEffectType.GLOWING,
		    									(int)(GenericFunctions.getAbilityValue(ArtifactAbility.GRACEFULDODGE, equip)*20),
		    									0)
		    							);
			    				}
			    			}
		    			p.setNoDamageTicks(10);
		    			ev.setCancelled(true);
		    		}
					log("Dodge chance is "+dodgechance,5);
				
	    		}
	    		
				if (!ev.isCancelled() && GenericFunctions.AttemptRevive(p,ev.getFinalDamage())) {
	    			ev.setCancelled(true);
	    		}
	    		
	    		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
	    			public void run() {
	    				if (p!=null) {
	    					p.getScoreboard().getTeam(p.getName().toLowerCase()).setSuffix(createHealthbar(((p.getHealth())/p.getMaxHealth())*100,p));
	    					p.getScoreboard().getTeam(p.getName().toLowerCase()).setPrefix(GenericFunctions.PlayerModePrefix(p));
	    				}
	    			}}
	    		,2);
	    	} else {
	    		if (e instanceof Monster) {
	        		final Monster m = (Monster)e;
	        		if (ev.getCause()==DamageCause.ENTITY_EXPLOSION ||
	        				ev.getCause()==DamageCause.BLOCK_EXPLOSION) {
	            		//Calculate new damage based on armor worn.
	            		//Remove all other damage modifiers since we will calculate it manually.
	            		//ev.setDamage(DamageModifier.BLOCKING,0);
	            		ev.setDamage(DamageModifier.MAGIC,0);
	            		ev.setDamage(DamageModifier.RESISTANCE,0);
	            		ev.setDamage(DamageModifier.ARMOR,0);
	            		
	            		//Damage reduction is also set based on how much blast resistance we have.
	
	            		ItemStack[] armor = m.getEquipment().getArmorContents();
	            		
	            		int protectionlevel = 0;
	            		for (int i=0;i<armor.length;i++) {
	            			if (armor[i]!=null &&
	            					armor[i].getType()!=Material.AIR) {
	            				protectionlevel+=armor[i].getEnchantmentLevel(Enchantment.PROTECTION_EXPLOSIONS);
	            			}
	            		}
	            		
	            		double dmg = NewCombat.CalculateDamageReduction(ev.getDamage()*EXPLOSION_DMG_MULT*((100-protectionlevel)*0.01),m,null);
	            		ev.setDamage(dmg);
	        			//log("Damage is "+ev.getDamage(),4);
	            		//ev.setDamage(CalculateDamageReduction(ev.getDamage()*EXPLOSION_DMG_MULT,p,null));
	        		}
	    		}
	    	}
	    	
	    	if (ev.getCause()==DamageCause.CUSTOM) {
	    		if (ev.getEntity() instanceof LivingEntity) {
	    			//NewCombat.setupTrueDamage(ev);
	    			log("Dealing "+ev.getFinalDamage()+" damage.",4);
	    		}
	    	}
		}
    }
    
    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void onEndermanTeleport(EntityTeleportEvent ev) {
    	if (ev.getEntity().isDead()) {
    		ev.setCancelled(true);
    	}
    	if (ev.getEntityType()==EntityType.ENDERMAN) {
    		//There is a small chance to drop a Mysterious Essence.
    		if (/*Math.random()<=0.0625*ARTIFACT_RARITY &&*/ ((Monster)ev.getEntity()).getTarget()==null &&
    				((!monsterdata.containsKey(ev.getEntity().getUniqueId())) ||
    						monsterdata.get(ev.getEntity().getUniqueId()).GetTarget()==null)) { //We won't drop it when they are targeting a player, only when they are doing their own thing.
    			Block block_teleported_on = ev.getFrom().add(0,0,0).getBlock();
    			log("Teleported on "+block_teleported_on.getType()+".",5);
    			if (block_teleported_on.isLiquid()) {
	    			if (MonsterController.getMonsterDifficulty(((Monster)ev.getEntity()))==MonsterDifficulty.HELLFIRE) {
	    				ItemStack i=new ItemStack(Material.PUMPKIN_SEEDS,1);
	    				Item it = ev.getFrom().getWorld().dropItem(ev.getFrom().add(0,200,0), Artifact.convert(Artifact.setName(i,ArtifactItem.MYSTERIOUS_ESSENCE),ArtifactItem.MYSTERIOUS_ESSENCE,true));
	    				it.setVelocity(new Vector(0,0,0));
	    				EssenceLogger.AddHellfireEssence();
	    			} else {
	    				EssenceLogger.AddGeneralEssence();
	    				Item it = ev.getFrom().getWorld().dropItem(ev.getFrom().add(0,200,0), Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE));
	    				it.setVelocity(new Vector(0,0,0));
	    			}
    			}
    		}
    		Monster m = (Monster)ev.getEntity();
    		if (m.getTarget() instanceof Player) {
    			Player p = (Player)m.getTarget();
    			if (GenericFunctions.isRanger(p)) {
    				//Teleport it into oblivion.
    				log("Going into another dimension...",5);
    				m.teleport(new Location(Bukkit.getWorld("world_nether"),m.getLocation().getX(),m.getLocation().getY(),m.getLocation().getZ()));
    			}
    		}
    	}
    }
    
    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void creeperExplodeEvent(ExplosionPrimeEvent ev) {
		log("Explosion Entity Type: "+ev.getEntityType().toString(),5);
    	if (ev.getEntity() instanceof Creeper) {
    		log("This is a creeper.",5);
    		final Creeper c = (Creeper)ev.getEntity();
    		if (c.getCustomName()!=null) {
    			log("Custom name is "+c.getCustomName(),4); 
    			if (c.getCustomName().contains("Dangerous")) {
    				log("Preparing to explode.",5);
    	    		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
    	    			public void run() {
    	    				c.getLocation().getWorld().createExplosion(c.getLocation().getX(),c.getLocation().getY(),c.getLocation().getZ(),4.0f,false,false);
	    					GenericFunctions.DealExplosionDamageToEntities(c.getLocation(), 16, 4);
    	    			}}
    	    		,10); 
    			} else 
    			if (c.getCustomName().contains("Deadly")) {
    				log("Preparing to explode.",5);
    	    		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
    	    			public void run() {
    	    				c.getLocation().getWorld().createExplosion(c.getLocation().getX(),c.getLocation().getY(),c.getLocation().getZ(),6.0f,true,false);
	    					GenericFunctions.DealExplosionDamageToEntities(c.getLocation(), 32, 6);
    	    			}}
    	    		,10);
    			} else 
    			if (c.getCustomName().contains("Hellfire")) {
    				log("Preparing to explode.",5);
    	    		c.getLocation().getWorld().playSound(c.getLocation(), Sound.ENTITY_CREEPER_PRIMED, 1.0f, 1.0f);
    				Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
    	    			public void run() {
    	    				c.getLocation().getWorld().createExplosion(c.getLocation().getX(),c.getLocation().getY(),c.getLocation().getZ(),8.0f,true,false);
	    					GenericFunctions.DealExplosionDamageToEntities(c.getLocation(), 64, 8);
    	    			}}
    	    		,30);
    			}
    		}
    	}
    }
    
    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void expEvent(PlayerExpChangeEvent ev) {  
    	double val = Math.random(); 
    	log("ExpChange event: "+val,5);
    	int amt = ev.getAmount();
    	int testamt = amt;
    	if (amt>500) {
    		testamt=500;
    	}
    	if (val<=((double)testamt/(double)65)*(0.00125)*ARTIFACT_RARITY) {
    		Item it = ev.getPlayer().getWorld().dropItemNaturally(ev.getPlayer().getLocation(), Artifact.createArtifactItem(ArtifactItem.MALLEABLE_BASE));
    		it.setPickupDelay(0);
    		it.setInvulnerable(true);
    		ev.getPlayer().sendMessage(ChatColor.LIGHT_PURPLE+"A strange item has appeared nearby.");
    	}
    }
    
    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void entityTargetEvent(EntityTargetLivingEntityEvent ev) {
		if ((ev.getEntity() instanceof Monster)) {
			Monster m = (Monster)ev.getEntity();
			MonsterStructure ms = MonsterStructure.getMonsterStructure(m);
			if (ms.getElite()) {
				EliteMonster em = null;
				for (int i=0;i<elitemonsters.size();i++) {
					if (elitemonsters.get(i).m.equals(ev.getEntity())) {
						em = elitemonsters.get(i);
						break;
					}
				}
				if (em!=null && (ev.getTarget() instanceof Player) && !em.targetlist.contains((Player)ev.getTarget())) {
					em.targetlist.add((Player)ev.getTarget());
				}
				m.setTarget(ev.getTarget()); 
	    		ev.setCancelled(true);
			}
		}
    	if (ev.getEntity() instanceof LivingEntity &&
    			ev.getReason()==TargetReason.PIG_ZOMBIE_TARGET) {
    		LivingEntity l = (LivingEntity)ev.getEntity();
    		if (l.hasPotionEffect(PotionEffectType.GLOWING)) {
    			if (monsterdata.containsKey(l.getUniqueId())) {
    				ev.setTarget(monsterdata.get(l.getUniqueId()).target);
    			}
    		}
    	}
    	if ((ev.getEntity() instanceof Monster) && GenericFunctions.isBossMonster((Monster)ev.getEntity())) {
    		Monster m = (Monster)ev.getEntity();
			GlowAPI.setGlowing(m, GlowAPI.Color.DARK_RED, Bukkit.getOnlinePlayers());
    	}
    }
    
    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void entityHitEvent(EntityDamageByEntityEvent ev) {
    	if (ev.getDamage()>=CUSTOM_DAMAGE_IDENTIFIER) {
    		log("Damage Breakdown:",4);
    		double storeddmg=ev.getDamage(DamageModifier.BASE);
    		for (int i=0;i<DamageModifier.values().length;i++) {
    			if (ev.isApplicable(DamageModifier.values()[i])) {
    	    		log("  "+DamageModifier.values()[i].name()+": "+ev.getDamage(DamageModifier.values()[i]),4);
    				ev.setDamage(DamageModifier.values()[i],0);
    			}
    		}
    		log("Stored Damage is "+storeddmg+". CUSTOM_DAMAGE_IDENTIFIER:"+CUSTOM_DAMAGE_IDENTIFIER+"\n...Subtracted damage is "+(storeddmg-CUSTOM_DAMAGE_IDENTIFIER),4);
    		ev.setDamage(DamageModifier.BASE,storeddmg-CUSTOM_DAMAGE_IDENTIFIER);
    		ev.setDamage(storeddmg-CUSTOM_DAMAGE_IDENTIFIER);
    		
    		if (ev.getEntity() instanceof Monster &&
    				monsterdata.containsKey(ev.getEntity().getUniqueId())) {
    			MonsterStructure ms = MonsterStructure.getMonsterStructure((Monster)ev.getEntity());
    			if (ms.getElite()) {
		    		boolean exists=false;
		    		for (int i=0;i<elitemonsters.size();i++) {
		    			if (elitemonsters.get(i).m.equals(ev.getEntity())) {
		    				exists=true;
		    				elitemonsters.get(i).runHitEvent((Player)ev.getDamager());
		    			}
		    		}
		    		if (!exists) {
		    			elitemonsters.add(new EliteMonster((Monster)ev.getEntity()));
		    		}
    			}
    		}
    		
    		log("New Damage: "+ev.getFinalDamage(),4);
    	} else {
	    	double dmg = 0.0;
			boolean hitallowed=true;
			if (ev.getEntity() instanceof LivingEntity) {
				hitallowed =GenericFunctions.enoughTicksHavePassed((LivingEntity)ev.getEntity(),NewCombat.getDamagerEntity(ev.getDamager()));
			}
			ev.setCancelled(!hitallowed);
	    	if (ev.getEntity() instanceof Player) {
	    		Player p = (Player)ev.getEntity();
	    		if (p.hasPotionEffect(PotionEffectType.GLOWING)) {
	    			ev.setCancelled(true);
	    		}
	    		if (!ev.isCancelled()) {
		    		double dodgechance = NewCombat.CalculateDodgeChance(p);
		    		if (ev.getCause()==DamageCause.THORNS &&
		    				GenericFunctions.isRanger(p)) { 
		    			dodgechance=1;
		    			p.setHealth(p.getHealth()-0.25);
		    			p.playSound(p.getLocation(), Sound.ENCHANT_THORNS_HIT, 0.8f, 3.0f);
		    		}
		    		
		    		PlayerStructure pd = (PlayerStructure)playerdata.get(p.getUniqueId());
		    		
		    		if (pd.fulldodge) {
		    			pd.fulldodge=false;
		    		}
		    		
		    		if (Math.random()<=dodgechance) {
		    			//Cancel this event, we dodged the attack.
		    			p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 3.0f, 1.0f);
		    			log("Triggered Dodge.",3);
		    			for (int i=0;i<p.getEquipment().getArmorContents().length;i++) {
		    				ItemStack equip = p.getEquipment().getArmorContents()[i];
		    				if (ArtifactAbility.containsEnchantment(ArtifactAbility.GRACEFULDODGE, equip)) {
		    					p.addPotionEffect(
		    							new PotionEffect(PotionEffectType.GLOWING,
		    									(int)(GenericFunctions.getAbilityValue(ArtifactAbility.GRACEFULDODGE, equip)*20),
		    									0)
		    							);
			    				}
			    			}
		    			p.setNoDamageTicks(10);
		    			ev.setCancelled(true);
		    		}
	    		}
	    	}
	    	if (!ev.isCancelled()) {
		    	if (ev.getEntity() instanceof LivingEntity) {
		    		dmg = NewCombat.applyDamage((LivingEntity)ev.getEntity(), ev.getDamager());
		    		if (!(ev.getEntity() instanceof Monster) || !(ev.getDamager() instanceof Monster)) {
		    			log(GenericFunctions.GetEntityDisplayName(ev.getDamager())+ChatColor.GRAY+"->"+
		    				GenericFunctions.GetEntityDisplayName(ev.getEntity())+ChatColor.GRAY+": Damage dealt was "+dmg,4);
		    		}
		    	}
		    	if (ev.getCause()==DamageCause.THORNS) {
		    		if (ev.getEntity() instanceof LivingEntity) {
			    		NewCombat.setupTrueDamage(ev); //Apply this as true damage.
		    			((LivingEntity)ev.getEntity()).setNoDamageTicks(10);
		    			((LivingEntity)ev.getEntity()).damage(Math.min(GenericFunctions.getMaxThornsLevel((LivingEntity)ev.getDamager()),((LivingEntity)ev.getEntity()).getHealth()/0.05));
		    		}
		    	} else
		    	if (dmg>=0) {
		    		NewCombat.setupTrueDamage(ev); //Apply this as true damage.
		    		ev.setDamage(0);
		    		//ev.setCancelled(true);  
		    		if (ev.getEntity() instanceof LivingEntity) {
		    			((LivingEntity)ev.getEntity()).setLastDamage(0);
		    			((LivingEntity)ev.getEntity()).setNoDamageTicks(0);
		    			((LivingEntity)ev.getEntity()).setMaximumNoDamageTicks(0);
		    			final double oldhp=((LivingEntity)ev.getEntity()).getHealth(); 
		    			
		    			if (ev.getEntity() instanceof Player) {
			    			if (!GenericFunctions.AttemptRevive((Player)ev.getEntity(), dmg)) {
				    			ev.setDamage(DamageModifier.BASE,dmg);
				    			ev.setDamage(dmg);
			    			} else {
			    				ev.setCancelled(true);
			    			}
		    			}
		    			if (NewCombat.getDamagerEntity(ev.getDamager()) instanceof Monster &&
		    					ev.getEntity() instanceof LivingEntity) {
		    	    		for (int i=0;i<elitemonsters.size();i++) {
		    	    			if (elitemonsters.get(i).m.equals(NewCombat.getDamagerEntity(ev.getDamager()))) {
		    	    				elitemonsters.get(i).hitEvent((LivingEntity)ev.getEntity());
		    	    			}
		    	    		}
		    			}
		    			if (NewCombat.getDamagerEntity(ev.getDamager()) instanceof Player) {
		    				if (ev.getDamager() instanceof Projectile) {
		    					ev.getDamager().remove();
		    				}
		    				GenericFunctions.subtractHealth((LivingEntity)ev.getEntity(), NewCombat.getDamagerEntity(ev.getDamager()), dmg);
		    				ev.setCancelled(true);
		    			}
						Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
	    					public void run() {
	    						if (oldhp != ((LivingEntity)ev.getEntity()).getHealth()) {
	    							log(ChatColor.BLUE+"  "+oldhp+"->"+((LivingEntity)ev.getEntity()).getHealth()+" HP",3);
	    						}
	    					}},1);
						if (ev.getDamager() instanceof Player) {
							Player p = (Player)ev.getDamager();
							if (GenericFunctions.isEquip(p.getEquipment().getItemInMainHand())) {
								aPlugin.API.damageItem(p, p.getEquipment().getItemInMainHand(), 1);
							}
							GenericFunctions.knockOffGreed(p);
						}
						//aPlugin.API.showDamage((LivingEntity)ev.getEntity(), (int)(dmg/10));
		    		}
		    	} //Negative damage doesn't make sense. We'd apply it normally.
	    	}
    	}
    }
    
    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void onLightningStrike(LightningStrikeEvent ev) {
    	LightningStrike lightning = ev.getLightning();
    	for (int i=0;i<4;i++) {
	    	Item it = lightning.getLocation().getWorld().dropItemNaturally(lightning.getLocation().add(0,2,0), Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE));
	    	it.setVelocity(new Vector(Math.random()*10-15,Math.random()*5,Math.random()*10-15));
	    	//Make them move in a direction violently and spontaneously.
    	}
    }
    
    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void monsterDeathEvent(final EntityDeathEvent ev) {
    	log("Has died.",5);
    	if (monsterdata.containsKey(ev.getEntity().getUniqueId())){ev.setDroppedExp(ev.getDroppedExp()+5);}
    	if (ev.getEntity() instanceof Bat) {
    		//Drop an essence.
    		if (Math.random()<=0.3) {
    			//Rarely drop a lost essence.
    			ev.getEntity().getLocation().getWorld().dropItemNaturally(ev.getEntity().getLocation(), Artifact.createArtifactItem(ArtifactItem.LOST_ESSENCE));
    		}
			ev.getEntity().getLocation().getWorld().dropItemNaturally(ev.getEntity().getLocation(), Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE));
    	}
    	if (ev.getEntity() instanceof Monster) {
    		List<ItemStack> droplist = ev.getDrops();
    		Monster m = (Monster)ev.getEntity();
    		
    		double dropmult = 0.0d;
    		boolean isBoss=false;
    		boolean isElite=false;
    		boolean killedByPlayer = false;
    		final Location deathloc = m.getLocation();
    		MonsterStructure ms = null;
    		if (monsterdata.containsKey(m.getUniqueId())) {
    			ms = (MonsterStructure)monsterdata.get(m.getUniqueId());
    			if (ms.hasOriginalName()) {
    				m.setCustomName(ms.getOriginalName());
    			}
    		}
    		
			if (ms!=null && (ms.GetTarget() instanceof Player)) {
				habitat_data.addKillToLocation(m);
				habitat_data.startinglocs.remove(m.getUniqueId());
    			log("Killed by a player.",5);
    			killedByPlayer = true;
				Player p = (Player)ms.GetTarget();
				if (p!=null && p.isOnline()) {
			    	if (GenericFunctions.isArtifactEquip(p.getEquipment().getItemInMainHand()) &&
			    			GenericFunctions.isArtifactWeapon(p.getEquipment().getItemInMainHand()) &&
			    			p.getEquipment().getItemInMainHand().getType()==Material.BOW) {
			    		PlayerStructure pd = (PlayerStructure)playerdata.get(p.getUniqueId());
			    		if (pd.nextarrowxp>0) {
			    			AwakenedArtifact.addPotentialEXP(p.getEquipment().getItemInMainHand(), pd.nextarrowxp, p);
			    			pd.nextarrowxp=0;
			    		}
			    	}
			    	
		        	PlayerStructure pd = (PlayerStructure)playerdata.get(p.getUniqueId());
		        	
		        	if (GenericFunctions.isRanger(p) &&
		        			GenericFunctions.getBowMode(p.getEquipment().getItemInMainHand())==BowMode.CLOSE) {
		        		pd.fulldodge=true;
		        	}
		        	
					dropmult+=pd.partybonus*0.33; //Party bonus increases drop rate by 33% per party member.
					ItemStack item = p.getEquipment().getItemInMainHand();
					if (item!=null &&
							item.getType()!=Material.AIR &&
							GenericFunctions.isWeapon(item)) {
						dropmult+=item.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS)*0.1; //Looting increases drop rate by 10% per level.
					}
					for (int i=0;i<p.getEquipment().getArmorContents().length;i++) {
						ItemStack equip = p.getEquipment().getArmorContents()[i];
						if (ArtifactAbility.containsEnchantment(ArtifactAbility.GREED, equip)) {
							dropmult+=GenericFunctions.getAbilityValue(ArtifactAbility.GREED, equip)/100d;
						}
					}
					if (ArtifactAbility.containsEnchantment(ArtifactAbility.GREED, p.getEquipment().getItemInMainHand())) {
						dropmult+=GenericFunctions.getAbilityValue(ArtifactAbility.GREED, p.getEquipment().getItemInMainHand())/100d;
					}
				} else {
					killedByPlayer=false;
				}
			}
			
    		
    		isBoss=GenericFunctions.isBossMonster(m);
    		isElite=GenericFunctions.isEliteMonster(m);
    		
			if (killedByPlayer && GenericFunctions.isCoreMonster(m) && Math.random()<RARE_DROP_RATE*dropmult*ARTIFACT_RARITY) {
				switch ((int)(Math.random()*4)) {
					case 0:{
						droplist.add(Artifact.createArtifactItem(ArtifactItem.LOST_CORE));
					}break;
					case 1:{
						droplist.add(Artifact.createArtifactItem(ArtifactItem.ANCIENT_CORE));
					}break;
					case 2:{
						droplist.add(Artifact.createArtifactItem(ArtifactItem.ARTIFACT_CORE));
					}break;
					case 3:{
						droplist.add(Artifact.createArtifactItem(ArtifactItem.DIVINE_CORE));
					}break;
				}
			}
    		
			if (killedByPlayer) {
				//Get the player that killed the monster.
				int luckmult = 0;
				int unluckmult = 0;
    			ms = (MonsterStructure)monsterdata.get(m.getUniqueId());
				Player p = (Player)ms.GetTarget();
				
				boolean isRanger=GenericFunctions.isRanger(p);
				
				if (isRanger) {
					switch (GenericFunctions.getBowMode(p.getEquipment().getItemInMainHand())) {
						case CLOSE:{
							BowLogger.AddCloseMode();
						}break;
						case SNIPE:{
							BowLogger.AddSnipeMode();
						}break;
						case DEBILITATION:{
							BowLogger.AddDebilitationMode();
						}break;
					}
				}
				
				if (p.hasPotionEffect(PotionEffectType.LUCK) ||
						p.hasPotionEffect(PotionEffectType.UNLUCK)) {
					for (int i=0;i<p.getActivePotionEffects().size();i++) {
						if (Iterables.get(p.getActivePotionEffects(), i).getType().equals(PotionEffectType.LUCK)) {
							luckmult = Iterables.get(p.getActivePotionEffects(), i).getAmplifier()+1;
						} else
						if (Iterables.get(p.getActivePotionEffects(), i).getType().equals(PotionEffectType.UNLUCK)) {
							unluckmult = Iterables.get(p.getActivePotionEffects(), i).getAmplifier()+1;
						}
					}
				}
				
				if (isElite) {
					dropmult+=50;
					EliteMonster em = GenericFunctions.getEliteMonster(m);
					//For each target, drop additional loot and exp.
					List<Player> participants = em.getTargetList();
					StringBuilder participants_list = new StringBuilder();
					for (int i=0;i<participants.size();i++) {
						Player pl = participants.get(i);
						ExperienceOrb exp = GenericFunctions.spawnXP(pl.getLocation(), ev.getDroppedExp()*300);
						exp.setInvulnerable(true);
						List<ItemStack> generatedloot = MonsterController.getMonsterDifficulty((Monster)ev.getEntity()).RandomizeDrops(dropmult/participants.size(),false,false);
						for (int j=0;j<generatedloot.size();j++) {
							Item it = pl.getWorld().dropItemNaturally(pl.getLocation(),generatedloot.get(j));
							it.setInvulnerable(true);
							log("Dropping "+generatedloot.get(j).toString(),2);
						}
						if (participants_list.length()<1) {
							participants_list.append(pl.getName());
						} else {
							if (i==participants.size()-1) {
								if (participants.size()==2) {
									participants_list.append(" and "+pl.getName());
								} else {
									participants_list.append(", and "+pl.getName());
								}
							} else {
								participants_list.append(","+pl.getName());
							}
						}
					}
					Bukkit.getServer().broadcastMessage(ChatColor.GREEN+participants_list.toString()+ChatColor.WHITE+" have successfully slain "+m.getCustomName()+ChatColor.WHITE+"!");
					aPlugin.API.discordSendRaw(ChatColor.GREEN+participants_list.toString()+ChatColor.WHITE+" have successfully slain **"+m.getCustomName()+ChatColor.WHITE+"**!");
					GenericFunctions.generateNewElite();
				}
					
				dropmult = dropmult + (luckmult * 0.5) - (unluckmult * 0.5);
				
				if (luckmult>0 || unluckmult>0) {
					log("Modified luck rate is now "+dropmult,3);
				}
				
				List<ItemStack> originaldroplist = new ArrayList<ItemStack>();
				for (int i=0;i<droplist.size();i++) {
					originaldroplist.add(droplist.get(i));
					droplist.remove(i);
					i--;
				}
				droplist.addAll(MonsterController.getMonsterDifficulty((Monster)ev.getEntity()).RandomizeDrops(dropmult, isBoss, isRanger));
	    		final List<ItemStack> drop = new ArrayList<ItemStack>(); 
	    		drop.addAll(droplist);
	    		
				
				int totalexp = 0;
				
	    		//Determine EXP amount and explosion type.
	    		switch (MonsterController.getMonsterDifficulty(m)) {
	    			case NORMAL:
						droplist.addAll(originaldroplist);
	    			break;
					case DANGEROUS:
						totalexp=ev.getDroppedExp()*4;
						droplist.addAll(originaldroplist);
						break;
					case DEADLY:
						m.getWorld().playSound(m.getLocation(), Sound.ENTITY_CREEPER_PRIMED, 1.0f, 1.0f);
						totalexp=ev.getDroppedExp()*8;
						ev.setDroppedExp((int)(totalexp*0.75));
						final Monster mer = m;
						final int expdrop = totalexp;
						droplist.clear(); //Clear the drop list. We are going to delay the drops.
						droplist.addAll(originaldroplist);
						Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
	    					public void run() {
	    	    				if (mer.getLocation().getBlockY()<48) {
	    	    					mer.getWorld().createExplosion(mer.getLocation().getBlockX(), mer.getLocation().getBlockY(), mer.getLocation().getBlockZ(), 3.0f, false, true);
	    	    					GenericFunctions.DealExplosionDamageToEntities(mer.getLocation(), 8, 3);
	    	    				} else {
	    	    					mer.getWorld().createExplosion(mer.getLocation().getBlockX(), mer.getLocation().getBlockY(), mer.getLocation().getBlockZ(), 6.0f, false, false);
	    	    					GenericFunctions.DealExplosionDamageToEntities(mer.getLocation(), 8, 6);
	    	    				}
	    					}}
	    				,30);
						Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
	    					public void run() {
			    				for (int i=0;i<drop.size();i++) {
			    					Item it = deathloc.getWorld().dropItemNaturally(mer.getLocation(), drop.get(i));
			    					it.setInvulnerable(true);
			    				}
			    				GenericFunctions.spawnXP(mer.getLocation(), (int)(expdrop*0.25));
	    					}}
	    				,50);
						break;
					case HELLFIRE:
						m.getWorld().playSound(m.getLocation(), Sound.ENTITY_CREEPER_PRIMED, 1.0f, 1.0f);
						totalexp=ev.getDroppedExp()*20;
						ev.setDroppedExp((int)(totalexp*0.75));
						final Monster mer1 = m;
						final int expdrop1 = totalexp;
						droplist.clear(); //Clear the drop list. We are going to delay the drops.
						droplist.addAll(originaldroplist);
						Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
	    					public void run() {
	    	    				if (mer1.getLocation().getBlockY()<48) {
	    	    					mer1.getWorld().createExplosion(mer1.getLocation().getBlockX(), mer1.getLocation().getBlockY(), mer1.getLocation().getBlockZ(), 5.0f, true, true);
	    	    					GenericFunctions.DealExplosionDamageToEntities(mer1.getLocation(), 12, 5);
	    	    				} else {
	    	    					mer1.getWorld().createExplosion(mer1.getLocation().getBlockX(), mer1.getLocation().getBlockY(), mer1.getLocation().getBlockZ(), 6.0f, true, false);
	    	    					GenericFunctions.DealExplosionDamageToEntities(mer1.getLocation(), 12, 6);
	    	    				}
	    					}}
	    				,30);
						Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
	    					public void run() {
			    				for (int i=0;i<drop.size();i++) {
			    					Item it = deathloc.getWorld().dropItemNaturally(mer1.getLocation(), drop.get(i));
			    					it.setInvulnerable(true);
			    				}
			    				GenericFunctions.spawnXP(mer1.getLocation(), (int)(expdrop1*0.25));
	    					}}
	    				,50);
						break;
					case ELITE:
						totalexp=ev.getDroppedExp()*300;
						final Monster mer2 = m;
	    				for (int i=0;i<drop.size();i++) {
	    					Item it = deathloc.getWorld().dropItemNaturally(mer2.getLocation(), drop.get(i));
	    					it.setInvulnerable(true);
	    				}
						break;
	    		}
	    		log("Drop list contains "+(droplist.size()+originaldroplist.size())+" elements.",5);
	    		log("  Drops "+"["+(drop.size()+originaldroplist.size())+"]: "+ChatColor.GOLD+ChatColor.stripColor(originaldroplist.toString())+ChatColor.WHITE+","+ChatColor.LIGHT_PURPLE+ChatColor.stripColor(drop.toString()),2);
			}
			
			if (monsterdata.containsKey(m.getUniqueId())) {
				monsterdata.remove(m.getUniqueId());
			}
    	}
    }
    
    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void updateHealthbarRespawnEvent(PlayerRespawnEvent ev) {
    	final Player p = ev.getPlayer();
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			public void run() {
				if (p!=null) {
					p.getScoreboard().getTeam(p.getName().toLowerCase()).setSuffix(createHealthbar(((p.getHealth())/p.getMaxHealth())*100,p));
					setPlayerMaxHealth(p);
					p.getScoreboard().getTeam(p.getName().toLowerCase()).setPrefix(GenericFunctions.PlayerModePrefix(p));
				}
			}}
		,5);

		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			@Override
			public void run() {
				//Look for a death structure for this player. If found, continue.
				if (DeathManager.getDeathStructure(p)!=null) {
					DeathManager.continueAction(p);
				}
				p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION,1,0),true);
				p.removePotionEffect(PotionEffectType.ABSORPTION);
			}
		},1);
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		pd.hasDied=false;
		GenericFunctions.addIFrame(p, 20*10);
    }
    
    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void updateHealthbarHealEvent(EntityRegainHealthEvent ev) {
    	Entity e = ev.getEntity();
    	if (e instanceof Player) {
    		final Player p = (Player)e;
    		//final double pcthp = ((p.getHealth())/p.getMaxHealth())*100;
    		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
    			public void run() {
    				if (p!=null) {
    					p.getScoreboard().getTeam(p.getName().toLowerCase()).setSuffix(createHealthbar(((p.getHealth())/p.getMaxHealth())*100,p));
    					p.getScoreboard().getTeam(p.getName().toLowerCase()).setPrefix(GenericFunctions.PlayerModePrefix(p));
    				}
    			}}
    		,2);
    	}
    }

    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void onBrokenItem(PlayerItemBreakEvent ev) {
    	//When an item breaks, check if it has the ChatColor.GRAY+"Breaks Remaining: " line.
    	//If it does, that means it can still be alive longer and not break.
    	Player p = ev.getPlayer();
    	ItemStack item = ev.getBrokenItem();
    	//See if this item has lore.
    	if (GenericFunctions.getHardenedItemBreaks(item)>0) {
    		//item.setAmount(1);
    		GenericFunctions.breakHardenedItem(item,p);
    	} else
    	{
    		breakdownItem(item,p);
    	}

    }
     
    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent ev) {
    	/*if (SERVER_TYPE==ServerType.TEST || SERVER_TYPE==ServerType.QUIET) {
	    	Player p = ev.getPlayer();
	    	TwosideKeeperAPI.addArtifactEXP(p.getEquipment().getItemInMainHand(), 100, p);
    	}*/
    	if (ev.getPlayer().isOnGround()) {
	    	PlayerStructure pd = (PlayerStructure)playerdata.get(ev.getPlayer().getUniqueId());
	    	pd.velocity = new Vector(ev.getFrom().getX(),0,ev.getFrom().getZ()).distanceSquared(new Vector(ev.getTo().getX(),0,ev.getTo().getZ()));
			if (pd.highwinder && pd.target!=null && !pd.target.isDead()) {
				aPlugin.API.sendActionBarMessage(ev.getPlayer(), drawVelocityBar(pd.velocity,pd.highwinderdmg));
			}
    	}
    }
    
    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void onFishEvent(PlayerFishEvent ev)  {
    	if (ev.getState().equals(State.CAUGHT_FISH)) {
	    	Player p = ev.getPlayer();
	    	if (p!=null) {
	    		if (GenericFunctions.isArtifactEquip(p.getEquipment().getItemInMainHand()) &&
	    				GenericFunctions.isArtifactWeapon(p.getEquipment().getItemInMainHand())) {
	    			AwakenedArtifact.addPotentialEXP(p.getEquipment().getItemInMainHand(), 12, p);
	    		}
	    		if (GenericFunctions.isWeapon(p.getEquipment().getItemInMainHand())) {
	    			GenericFunctions.RemovePermEnchantmentChance(p.getEquipment().getItemInMainHand(), p);
	    		}
	    	}
    	}
    }
    
    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void onAreaCloudApply(AreaEffectCloudApplyEvent ev) {
    	List<LivingEntity> affected = ev.getAffectedEntities();
    	for (int i=0;i<affected.size();i++) {
	    	if (affected.get(i) instanceof Monster) {
	    		if (ev.getEntity().getCustomName()!=null) {
	    	    	log("Custom name is "+ev.getEntity().getCustomName(),5);
	    			if (ev.getEntity().getCustomName().contains("EW ")) {
	    				double dmgdealt=Double.parseDouble(ev.getEntity().getCustomName().split(" ")[1]);
	    				log("Dealing "+dmgdealt+" damage. Player is "+Bukkit.getPlayer(ev.getEntity().getCustomName().split(" ")[2]).getName(),4);
	    				double reduceddmg = CalculateDamageReduction(dmgdealt,affected.get(i),null);
	    				DamageLogger.AddNewCalculation(Bukkit.getPlayer(ev.getEntity().getCustomName().split(" ")[2]), ChatColor.GREEN+"Earth Wave", dmgdealt, reduceddmg);
	    				GenericFunctions.DealDamageToMob(reduceddmg, affected.get(i), Bukkit.getPlayer(ev.getEntity().getCustomName().split(" ")[2]), Bukkit.getPlayer(ev.getEntity().getCustomName().split(" ")[2]).getEquipment().getItemInMainHand());
	    			} else
	    			if (ev.getEntity().getCustomName().contains("LD ")) {
	    				Player p = Bukkit.getPlayer(ev.getEntity().getCustomName().split(" ")[2]);
	    				double dmgdealt=Double.parseDouble(ev.getEntity().getCustomName().split(" ")[1]);
	    				dmgdealt*=1.0d+(4*((NewCombat.getPercentHealthMissing((Monster)affected.get(i)))/100d));
	    				log("Dealing "+dmgdealt+" damage. Player is "+p.getName(),5);
	    				double reduceddmg = CalculateDamageReduction(dmgdealt,affected.get(i),null);
	    				DamageLogger.AddNewCalculation(Bukkit.getPlayer(ev.getEntity().getCustomName().split(" ")[2]), ChatColor.GREEN+"Line Drive", dmgdealt, reduceddmg);
	    				GenericFunctions.DealDamageToMob(reduceddmg, affected.get(i), Bukkit.getPlayer(ev.getEntity().getCustomName().split(" ")[2]), Bukkit.getPlayer(ev.getEntity().getCustomName().split(" ")[2]).getEquipment().getItemInMainHand());
	    				((Monster)affected.get(i)).addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION,2,100));
	    				if (affected.get(i).isDead()) {
	    					//Restore the cooldown a little for the player. Refresh the cooldown amount.
	    					PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
	    					pd.last_strikerspell = pd.last_strikerspell-40;
	    					aPlugin.API.sendCooldownPacket(p, p.getEquipment().getItemInMainHand(), (int)(LINEDRIVE_COOLDOWN-(TwosideKeeper.getServerTickTime()-pd.last_strikerspell)));
	    				}
	    			} else {
	    	    		affected.remove(i);
	    	    		i--;
	    			}
	    		} else {
		    		affected.remove(i);
		    		i--;
	    		}
	    	} else 
	    	if (affected.get(i) instanceof Player) {
    			if (ev.getEntity().getCustomName().contains("WEAK")) {
    				Player p = (Player)(affected.get(i));
    				int weaknesslv = Integer.parseInt(ev.getEntity().getCustomName().split(" ")[1]);
    				int duration = Integer.parseInt(ev.getEntity().getCustomName().split(" ")[2]);
    				p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS,duration,-weaknesslv),true);
    				log("Weakness Level: "+GenericFunctions.getPotionEffectLevel(PotionEffectType.WEAKNESS, p),5);
    			} else {
    	    		affected.remove(i);
    	    		i--;
    			}
	    	} else {
	    		affected.remove(i);
	    		i--;
	    	}
    	}
    }
    
    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent ev) {
    	
    	TwosideSpleefGames.PassEvent(ev);
    	
    	Player p = ev.getPlayer();
    	PlayerStructure pd = (PlayerStructure)playerdata.get(p.getUniqueId()); 
    	if (p!=null) {
    		log(p.getName()+" has broken block "+GenericFunctions.UserFriendlyMaterialName(new ItemStack(ev.getBlock().getType())),3);
    		if (GenericFunctions.isArtifactEquip(p.getEquipment().getItemInMainHand()) &&
    				GenericFunctions.isArtifactTool(p.getEquipment().getItemInMainHand())) {
    			AwakenedArtifact.addPotentialEXP(p.getEquipment().getItemInMainHand(), 4, p);
    		}
    		if (GenericFunctions.isTool(p.getEquipment().getItemInMainHand())) {
    			GenericFunctions.RemovePermEnchantmentChance(p.getEquipment().getItemInMainHand(), p);
    			if (ArtifactAbility.containsEnchantment(ArtifactAbility.EARTHWAVE, p.getEquipment().getItemInMainHand()) &&
    					pd.target!=null && !pd.target.isDead() && pd.last_shovelspell<getServerTickTime()) {
    				
    				/*if (pd.target.getLocation().getWorld().equals(p.getWorld()) && pd.target.getLocation().distanceSquared(p.getLocation())<=256) {
	    				final Player p1 = p;
	    				AreaEffectCloud lp = (AreaEffectCloud)p.getWorld().spawnEntity(p.getLocation(), EntityType.AREA_EFFECT_CLOUD);
	    				lp.setColor(Color.OLIVE);
	    				DecimalFormat df = new DecimalFormat("0.00");
	    				lp.setCustomName("EW "+df.format(GenericFunctions.getAbilityValue(ArtifactAbility.EARTHWAVE, p.getEquipment().getItemInMainHand()))+" "+p.getName());
	    				lp.setRadius(3f);
	    				lp.setDuration(80);
	    				lp.setReapplicationDelay(20);
	    				lp.setBasePotionData(new PotionData(PotionType.INSTANT_DAMAGE));
	    				lp.setParticle(Particle.SMOKE_NORMAL);
	    				p.playSound(p.getLocation(), Sound.ENTITY_SHULKER_SHOOT, 1.0f, 0.5f);
	    				pd.last_shovelspell=getServerTickTime()+EARTHWAVE_COOLDOWN;
						aPlugin.API.sendCooldownPacket(p, p.getEquipment().getItemInMainHand(), 300);
						aPlugin.API.sendCooldownPacket(p, p.getEquipment().getItemInMainHand(), 300);
		
		    			int mult=2;
		    			double xspd=p.getLocation().getDirection().getX()*mult;
		    			double yspd=p.getLocation().getDirection().getY()/2;
		    			double zspd=p.getLocation().getDirection().getZ()*mult;
		    			double xpos=p.getLocation().getX();
		    			double ypos=p.getLocation().getY();
		    			double zpos=p.getLocation().getZ();
		    			int range=8;
		    			final String customname = lp.getCustomName();
		    			for (int i=0;i<range;i++) {
		    				final int tempi=i;
		    				final Location newpos=new Location(p.getWorld(),xpos,ypos,zpos).add(i*xspd,i*yspd,i*zspd);
		    				Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
		    				public void run() {
			        				AreaEffectCloud lp = (AreaEffectCloud)newpos.getWorld().spawnEntity(newpos, EntityType.AREA_EFFECT_CLOUD);
			        				lp.setColor(Color.OLIVE);
			        				lp.setCustomName(customname);
			        				lp.setBasePotionData(new PotionData(PotionType.INSTANT_DAMAGE));
			        				lp.setRadius(3f);
			        				lp.setDuration(80);
				    				lp.setReapplicationDelay(20);
				    				lp.setBasePotionData(new PotionData(PotionType.INSTANT_DAMAGE));
			        				if (tempi%2==0) {
			        				lp.setParticle(Particle.SMOKE_LARGE);} else {
				        				lp.setParticle(Particle.SMOKE_NORMAL);
			        				}
			        				p1.playSound(lp.getLocation(), Sound.ENTITY_CAT_HISS, 1.0f, 0.3f);
		    					}
		    				},i*16);
		    			}
	    			} else {
	    				pd.target=null;
	    			}*/
    			}
    		}
    	}
    	
    	if (ev.getBlock().getType()==Material.WALL_SIGN ||
    			ev.getBlock().getType()==Material.CHEST ||
    			ev.getBlock().getType()==Material.TRAPPED_CHEST) {
    		//We're going to make sure if it's a shop or not.
    		Sign s = null;
    		if (ev.getBlock().getType()==Material.WALL_SIGN) {
    			s = (Sign)(ev.getBlock().getState());
    		} else {
    			s = WorldShop.grabShopSign(ev.getBlock());
    		}
    		if (s!=null) {
	    		if (s.getLine(0).equalsIgnoreCase(ChatColor.BLUE+"-- SHOP --")) {
	    			//This is a shop. Let's find out who the owner is.
	    			int shopID = TwosideShops.GetShopID(s);
	    			WorldShop shop = TwosideShops.LoadWorldShopData(shopID);
	    			String owner = shop.GetOwner();
	    			if (owner.equalsIgnoreCase(p.getName()) || p.isOp()) {
	    				//We are going to see if this shop had items in it.
	    				/*if (shop.GetAmount()>0) { //LEGACY CODE.
	    					//It did, we are going to release those items.
	    					ItemStack drop = shop.GetItem();
							int dropAmt = shop.GetAmount();
							while (dropAmt>0) {
								if (dropAmt>shop.GetItem().getMaxStackSize()) {
									drop.setAmount(shop.GetItem().getMaxStackSize());
									ev.getPlayer().getWorld().dropItemNaturally(ev.getPlayer().getLocation(), drop).setPickupDelay(0);
									dropAmt-=shop.GetItem().getMaxStackSize();
								} else {
									drop.setAmount(dropAmt);
									ev.getPlayer().getWorld().dropItemNaturally(ev.getPlayer().getLocation(), drop).setPickupDelay(0);
									dropAmt=0;
								}
							}
	    					//ev.getPlayer().getLocation().getWorld().dropItemNaturally(ev.getPlayer().getLocation(), drop).setPickupDelay(0);
	    				}*/
	    				//Remove the itemstack that represented this item.
	    				WorldShop.removeShopItem(s,shop);
						TwosideKeeper.TwosideShops.RemoveSession(p);
	    			} else {
	    				//They are not the owner! Do not allow this shop to be broken.
	    				p.sendMessage("This shop belongs to "+ChatColor.LIGHT_PURPLE+owner+ChatColor.WHITE+"! You cannot break others' shops!");
	    				ev.setCancelled(true);
	    			}
	    		} else
	    		if (s.getLine(0).equalsIgnoreCase(ChatColor.BLUE+"- BUYING SHOP -") ||
	    				s.getLine(0).equalsIgnoreCase(ChatColor.YELLOW+""+ChatColor.BOLD+"-BUYING SHOP-") ||
    					s.getLine(0).equalsIgnoreCase(ChatColor.GREEN+""+ChatColor.BOLD+"-BUYING SHOP-")) {
	    			//This is a shop. Let's find out who the owner is.
	    			int shopID = TwosideShops.GetShopID(s);
	    			WorldShop shop = TwosideShops.LoadWorldShopData(shopID);
	    			String owner = shop.GetOwner();
	    			if (owner.equalsIgnoreCase(p.getName()) || p.isOp()) {
	    				//We are going to see if this shop had items in it.
	    				/*if (shop.GetStoredAmount()>0) { //LEGACY CODE.
	    					//It did, we are going to release those items.
	    					ItemStack drop = shop.GetItem();
							int dropAmt = shop.GetStoredAmount();
							while (dropAmt>0) {
								if (dropAmt>shop.GetItem().getMaxStackSize()) {
									drop.setAmount(shop.GetItem().getMaxStackSize());
									ev.getPlayer().getWorld().dropItemNaturally(ev.getPlayer().getLocation(), drop).setPickupDelay(0);
									dropAmt-=shop.GetItem().getMaxStackSize();
								} else {
									drop.setAmount(dropAmt);
									ev.getPlayer().getWorld().dropItemNaturally(ev.getPlayer().getLocation(), drop).setPickupDelay(0);
									dropAmt=0;
								}
							}
	    					//ev.getPlayer().getLocation().getWorld().dropItemNaturally(ev.getPlayer().getLocation(), drop).setPickupDelay(0);
	    				}*/
	    				//Remove the itemstack that represented this item.
	    				Collection<Entity> nearby = WorldShop.getBlockShopSignAttachedTo(s).getWorld().getNearbyEntities(WorldShop.getBlockShopSignAttachedTo(s).getLocation().add(0.5,0,0.5), 0.3, 1, 0.3);
	    				for (int i=0;i<nearby.size();i++) {
	    					Entity e = Iterables.get(nearby, i);
	    					if (e.getType()==EntityType.DROPPED_ITEM) {
	    						log("Found a drop.",5);
	    						Item it = (Item)e;
	    						
	    						ItemStack checkdrop = shop.GetItem().clone();
	    						checkdrop = Artifact.convert(checkdrop);
	    						checkdrop.removeEnchantment(Enchantment.LUCK);
	    						ItemMeta m = checkdrop.getItemMeta();
	
	    						List<String> lore = new ArrayList<String>();
	    						if (m.hasLore()) {
	    							lore = m.getLore();
	    						}
	    						lore.add("WorldShop Display Item");
	    						m.setLore(lore);
	    						checkdrop.setItemMeta(m);
	    						log("Comparing item "+it.getItemStack().toString()+" to "+checkdrop.toString(),5);
	    						if (it.getItemStack().isSimilar(checkdrop)) {
	    							log("Same type.",5);
	    							e.remove();
	    							e.setCustomNameVisible(false);
	    							e.setCustomName(null);
	    							TwosideShops.RemoveSession(p);
	    						}
	    					}
	    				}
	    			} else {
	    				//They are not the owner! Do not allow this shop to be broken.
	    				p.sendMessage("This shop belongs to "+ChatColor.LIGHT_PURPLE+owner+ChatColor.WHITE+"! You cannot break others' shops!");
	    				ev.setCancelled(true);
	    			}
	    		}
    		}
    	} else {
    		//Make sure there's no world sign on this block.
    		if (WorldShop.hasShopSignAttached(ev.getBlock())) {
    			//Do not allow this. The shop signs have to be destroyed first!
				p.sendMessage("This block has shops on it! The shops must be destroyed before you can break this block!");
				ev.setCancelled(true);
    		}
    	}
    }
    
    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void onItemPickup(PlayerPickupItemEvent ev) {
    	//Arrow quiver code goes here.
    	log("Pickup Metadata: "+ev.getItem().getItemStack().getItemMeta().toString(),5);
    	Player p = ev.getPlayer();
    	if (ev.getItem().getItemStack().getType()==Material.ARROW &&
    			playerHasArrowQuiver(p)) {
    			int arrowquiver_slot = playerGetArrowQuiver(p);
    			playerInsertArrowQuiver(p, arrowquiver_slot, ev.getItem().getItemStack().getAmount());
    			log("Added "+ev.getItem().getItemStack().getAmount()+" arrow"+((ev.getItem().getItemStack().getAmount()==1)?"":"s")+" to quiver in slot "+arrowquiver_slot+". New amount: "+playerGetArrowQuiverAmt(p,arrowquiver_slot),4);
    			//If we added it here, we destroy the item stack.
    			p.sendMessage(ChatColor.DARK_GRAY+""+ev.getItem().getItemStack().getAmount()+" arrow"+((ev.getItem().getItemStack().getAmount()==1)?"":"s")+" "+((ev.getItem().getItemStack().getAmount()==1)?"was":"were")+" added to your arrow quiver. Arrow Count: "+ChatColor.GRAY+playerGetArrowQuiverAmt(p,arrowquiver_slot));
    			ev.getItem().remove();
    			ev.setCancelled(true);
    	}
    	if (ev.getItem().getItemStack().getType().toString().contains("BOOTS") ||
    			ev.getItem().getItemStack().getType().toString().contains("LEGGINGS") ||
    			ev.getItem().getItemStack().getType().toString().contains("CHESTPLATE") ||
    			ev.getItem().getItemStack().getType().toString().contains("HELMET") ||
    			ev.getItem().getItemStack().getType().toString().contains("SHIELD")) {
    		ItemStack armor = ev.getItem().getItemStack();
    		//See if this armor type is not being worn by the player.
    		if (armor.getType().toString().contains("BOOTS") &&
    				p.getEquipment().getBoots()==null &&
    				(!GenericFunctions.isRanger(p) || (armor.getType().toString().contains("LEATHER")))) {
    			p.getEquipment().setBoots(armor);
    			p.sendMessage(ChatColor.DARK_AQUA+"Automatically equipped "+ChatColor.YELLOW+(ev.getItem().getItemStack().getItemMeta().hasDisplayName()?ev.getItem().getItemStack().getItemMeta().getDisplayName():GenericFunctions.UserFriendlyMaterialName(ev.getItem().getItemStack())));
    			p.playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1.0f, 1.0f);
    			GenericFunctions.playProperEquipSound(p,armor.getType());
    			ev.getItem().remove();
    			ev.setCancelled(true);
    		} else
    		if (armor.getType().toString().contains("LEGGINGS") &&
    				p.getEquipment().getLeggings()==null &&
    				(!GenericFunctions.isRanger(p) || (armor.getType().toString().contains("LEATHER")))) {
    			p.getEquipment().setLeggings(armor);
    			p.sendMessage(ChatColor.DARK_AQUA+"Automatically equipped "+ChatColor.YELLOW+(ev.getItem().getItemStack().getItemMeta().hasDisplayName()?ev.getItem().getItemStack().getItemMeta().getDisplayName():GenericFunctions.UserFriendlyMaterialName(ev.getItem().getItemStack())));
    			p.playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1.0f, 1.0f);
    			GenericFunctions.playProperEquipSound(p,armor.getType());
    			ev.getItem().remove();
    			ev.setCancelled(true);
    		} else
    		if (armor.getType().toString().contains("CHESTPLATE") &&
    				p.getEquipment().getChestplate()==null &&
    				(!GenericFunctions.isRanger(p) || (armor.getType().toString().contains("LEATHER")))) {
    			p.getEquipment().setChestplate(armor);
    			p.sendMessage(ChatColor.DARK_AQUA+"Automatically equipped "+ChatColor.YELLOW+(ev.getItem().getItemStack().getItemMeta().hasDisplayName()?ev.getItem().getItemStack().getItemMeta().getDisplayName():GenericFunctions.UserFriendlyMaterialName(ev.getItem().getItemStack())));
    			p.playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1.0f, 1.0f);
    			GenericFunctions.playProperEquipSound(p,armor.getType());
    			ev.getItem().remove();
    			ev.setCancelled(true);
    		} else
    		if (armor.getType().toString().contains("HELMET") &&
    				p.getEquipment().getHelmet()==null &&
    				(!GenericFunctions.isRanger(p) || (armor.getType().toString().contains("LEATHER")))) {
    			p.getEquipment().setHelmet(armor);
    			p.sendMessage(ChatColor.DARK_AQUA+"Automatically equipped "+ChatColor.YELLOW+(ev.getItem().getItemStack().getItemMeta().hasDisplayName()?ev.getItem().getItemStack().getItemMeta().getDisplayName():GenericFunctions.UserFriendlyMaterialName(ev.getItem().getItemStack())));
    			p.playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1.0f, 1.0f);
    			GenericFunctions.playProperEquipSound(p,armor.getType());
    			ev.getItem().remove();
    			ev.setCancelled(true);
    		} else
    		if (armor.getType().toString().contains("SHIELD") &&
    				p.getInventory().getExtraContents()[0]==null &&
    				!GenericFunctions.isStriker(p) &&
    				(!GenericFunctions.isRanger(p) || (armor.getType().toString().contains("LEATHER")))) {
    			p.getInventory().setExtraContents(new ItemStack[]{armor});
    			p.sendMessage(ChatColor.DARK_AQUA+"Automatically equipped "+ChatColor.YELLOW+(ev.getItem().getItemStack().getItemMeta().hasDisplayName()?ev.getItem().getItemStack().getItemMeta().getDisplayName():GenericFunctions.UserFriendlyMaterialName(ev.getItem().getItemStack())));
    			p.playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1.0f, 1.0f);
    			GenericFunctions.playProperEquipSound(p,armor.getType());
    			ev.getItem().remove();
    			ev.setCancelled(true);
    		}
    	}
    }

    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void onHopperSuction(InventoryMoveItemEvent ev) {
    	Inventory source = ev.getSource();
    	Location l = source.getLocation();
    	//See if this block is a world shop.
    	if (WorldShop.grabShopSign(l.getBlock())!=null) {
    		//This is a world shop. DO NOT allow this to happen.
    		ev.setCancelled(true);
    		final Location l1=ev.getDestination().getLocation();
    		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
				public void run() {
		    		l1.getBlock().breakNaturally();
				}}
			,1);
    	}
    	Inventory destination = ev.getDestination();
    	l = destination.getLocation();
    	//See if this block is a world shop.
    	if (WorldShop.grabShopSign(l.getBlock())!=null) {
    		//This is a world shop. DO NOT allow this to happen.
    		ev.setCancelled(true);
    		final Location l1=source.getLocation();
    		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
				public void run() {
		    		l1.getBlock().breakNaturally();
				}}
			,1);
    	}
    }
    
    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void onHopperSuction(InventoryPickupItemEvent ev) {
    	//Check the item getting sucked in.
    	if (ev.getItem().getItemStack().hasItemMeta() &&
    			ev.getItem().getItemStack().getItemMeta().hasLore() &&
    			ev.getItem().getItemStack().getItemMeta().getLore().contains("WorldShop Display Item")) {
    		ev.setCancelled(true);
    		ev.getItem().remove();
    	}
    }
    
    
    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void onArrowShoot(ProjectileLaunchEvent ev) {
    	if (ev.getEntity() instanceof Projectile) {
    		Projectile arr = (Projectile)ev.getEntity();
    		//Arrow newarrow = arr.getLocation().getWorld().spawnArrow(arr.getLocation(), arr.getVelocity(), 1, 12);
    		if (arr.getShooter() instanceof Player &&
    				arr.getCustomName()==null && (arr instanceof Arrow)) {
    			Player p = (Player)arr.getShooter();
    			LivingEntity checkent = aPlugin.API.getTargetEntity(p, 100);
        		if (checkent!=null && (checkent instanceof Monster)) {
        			if (!monsterdata.containsKey(checkent.getUniqueId())) {
        				MonsterStructure newstruct = new MonsterStructure((Monster)checkent);
        				newstruct.SetTarget(p);
        				monsterdata.put(checkent.getUniqueId(), newstruct);
        				Monster m = (Monster)checkent;
        				m.setTarget(p);
        			}
        			log("Setup new target: "+p.getName(),5);
        		}
    			if (GenericFunctions.isRanger(p)) {
    				if (GenericFunctions.getBowMode(p.getEquipment().getItemInMainHand())==BowMode.SNIPE) {arr.setVelocity(arr.getVelocity().multiply(1000));}
    				log(arr.getVelocity().lengthSquared()+"",5);
    				//arr.setVelocity(arr.getVelocity().multiply(3.0/arr.getVelocity().lengthSquared()));
    				if (GenericFunctions.getBowMode(p.getEquipment().getItemInMainHand())==BowMode.SNIPE) {
    					aPlugin.API.damageItem(p, p.getEquipment().getItemInMainHand(), 3);
    					//p.getEquipment().getItemInMainHand().setDurability((short)(p.getEquipment().getItemInMainHand().getDurability()+1));
    				}
    				//p.getWorld().spawnArrow(arr.getLocation(), arr.getLocation().getDirection(), 20, 1);
    			}
				PlayerStructure pd = (PlayerStructure)playerdata.get(p.getUniqueId());
				pd.lastarrowpower=arr.getVelocity().lengthSquared();
				pd.lastarrowwasinrangermode=(GenericFunctions.isRanger(p)&&GenericFunctions.getBowMode(p.getEquipment().getItemInMainHand())==BowMode.SNIPE);
				log("Arrow velocity is "+arr.getVelocity().lengthSquared(),5);
    		}
    	}
    }

    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void onArrowLand(ProjectileHitEvent ev) {
    	if (ev.getEntity() instanceof Arrow) {
    		Arrow ar = (Arrow)ev.getEntity();
    		if (ar.getShooter()!=null &&
    				ar.getCustomName()==null &&
    				(ar.getShooter() instanceof Player)) {
    			Player p = (Player)ar.getShooter();
    		}
    	}
    }
    
    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void onArrowShot(EntityShootBowEvent ev) {
    	//Check if it's a player.
    	if (ev.getEntityType()==EntityType.PLAYER &&
    			(ev.getProjectile().getType()==EntityType.ARROW ||
    			ev.getProjectile().getType()==EntityType.TIPPED_ARROW)) {
    		//Now we know this is a player who shot a regular old arrow.

    		final Player p = (Player)ev.getEntity();
    		//We need to give one back to them.
    		if (ev.getProjectile().getType()==EntityType.ARROW) {
    			//This was an arrow quiver. We need to verify that, check the player's inventory for one.
    			//Then queue a delayed event to add it back in if it's gone next tick.
    			if (playerHasArrowQuiver(p)) {
        			log("A tipped arrow was shot. This could've been the arrow quiver. We will verify in 1 tick.",5);
        			final int ArrowQuiver_amt = playerGetArrowQuiverAmt(p,playerGetArrowQuiver(p));
        			boolean temp=false; //Check if it went in the off-hand slot. If so, put it back there.
        			if (p.getInventory().getItemInOffHand().equals(p.getInventory().getItem(playerGetArrowQuiver(p)))) {
        				temp=true;
        			}
        			if ((ArrowQuiver_amt-1)<=0 || (ArrowQuiver_amt%10+1)==0) {
        				p.sendMessage(ChatColor.DARK_GRAY+"You have "+ChatColor.YELLOW+(ArrowQuiver_amt-1)+ChatColor.DARK_GRAY+" arrows left in your quiver.");
        			}
        			final boolean offhand=temp;
        			if (ArrowQuiver_amt==0){ /*Cancel this event...*/ ev.getProjectile().remove(); ev.setCancelled(true); }
        			Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
        				public void run() {
        					if (p!=null) {
        						if (!playerHasArrowQuiver(p)) {
        							log("This player does not have a quiver! Let's give them one back!",5);
        							//We have to give one back.
        							ItemStack ArrowQuiver = new ItemStack(Material.TIPPED_ARROW);
        							List<String> arrow_quiver_lore = new ArrayList<String>();
        							arrow_quiver_lore.add("A quiver that holds many arrows.");
        							arrow_quiver_lore.add(ChatColor.GRAY+"Arrows Remaining: "+ChatColor.YELLOW+(ArrowQuiver_amt-1));
        							ItemMeta arrow_quiver_meta=ArrowQuiver.getItemMeta();
        							arrow_quiver_meta.setLore(arrow_quiver_lore);
        							arrow_quiver_meta.setDisplayName(ChatColor.BLUE+"Arrow Quiver");
        							ArrowQuiver.setItemMeta(arrow_quiver_meta);
        							
        							ArrowQuiver.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 5);
        							
        							ArrowQuiver.setAmount(1);
        							if (!offhand) {p.getInventory().addItem(ArrowQuiver);} else {p.getInventory().setItemInOffHand(ArrowQuiver);}
        						} else {
        							p.sendMessage(ChatColor.ITALIC+""+ChatColor.GRAY+"If you are trying to shoot a regular arrow, put it inside your quiver and shoot again.");
        						}
        					}
        				}}
        			,1);
    			}
    		}
    		
    	}
    }
    
    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void onItemCraftEvent(PrepareItemCraftEvent ev) {
    	ItemStack result = ev.getInventory().getResult();
    	if (result.getType()==Material.TNT) {
    		result.setAmount(result.getAmount()*5); //TNT recipes are 5 times as effective.
    	}

    	//Check if we are using an item cube in a non-item cube recipe.

    	//Item cube should be in slot 4.
    	if (ev.getInventory().getItem(5)!=null) {
			ItemMeta inventory_itemMeta=ev.getInventory().getItem(5).getItemMeta();
			if (inventory_itemMeta.hasLore() && inventory_itemMeta.getLore().size()==4) {
		    	log("4 Elements detected.",5);
	    		String loreitem = inventory_itemMeta.getLore().get(3);
		    	log("Lore data is: "+loreitem,5);
	    		if (loreitem!=null && loreitem.contains(ChatColor.DARK_PURPLE+"ID#")) {
	    	    	log("This is an Item Cube. Invalidate recipe.",4);
	    			//This is an item cube. Invalidate the recipe.
	    			ev.getInventory().setResult(new ItemStack(Material.AIR));
	    			//ev.getWhoClicked().sendMessage(ChatColor.RED+"You cannot craft items with an Item Cube!");
	    	    	//ev.setCurrentItem(new ItemStack(Material.AIR));
	    		}
			}
    	}
		
		//This could be our duplication recipe...
    	if (CustomRecipe.ENDER_ITEM_CUBE_DUPLICATE.isSameRecipe(ev.getRecipe().getResult())) {
    		CustomRecipe.ENDER_ITEM_CUBE_DUPLICATE.ValidateRecipe(ev);
    	}
    	
    	//We are looking for an artifact conversion recipe.
    	if ((result.getType()==Material.SUGAR ||
    			result.getType()==Material.MAGMA_CREAM ||
    			result.getType()==Material.CLAY_BALL) && Artifact.isArtifact(result)) { 
    		//This is a conversion recipe. Decide how to handle it.
    		log("Handling this recipe...",4);
    		boolean nether_star_found=false;
    		boolean using_artifact_item=false;
    		int valid_items=0;
    		List<ItemStack> items_found = new ArrayList<ItemStack>();
    		for (int i=1;i<ev.getInventory().getSize();i++) {
    			if (ev.getInventory().getItem(i)!=null &&
    					ev.getInventory().getItem(i).getType()==Material.NETHER_STAR) {
    	    		log(" Nether Star Found.",5);
    				nether_star_found=true;
    				valid_items++;
    			} else
    			if (ev.getInventory().getItem(i)!=null &&
    					(ev.getInventory().getItem(i).getType()==Material.SUGAR ||
    					ev.getInventory().getItem(i).getType()==Material.MAGMA_CREAM ||
    					ev.getInventory().getItem(i).getType()==Material.CLAY_BALL)) {
    	    		log(" Other Item Found.",5);
    	    		items_found.add(ev.getInventory().getItem(i));
    	    		if (ev.getInventory().getItem(i).getEnchantmentLevel(Enchantment.LUCK)!=1) {
    	    			//Invalid.
        	    		log("  Not a valid tier: "+ev.getInventory().getItem(i).getEnchantmentLevel(Enchantment.LUCK),4);
    	    			ev.getInventory().setResult(new ItemStack(Material.AIR));
    	    		} else {
        				valid_items++;
    	    		}
    			} else
    			if (ev.getInventory().getItem(i)!=null &&
    					ev.getInventory().getItem(i).getType()!=Material.AIR &&
    					!Artifact.isArtifact(ev.getInventory().getItem(i))) {
    	    		log(" This is "+ev.getInventory().getItem(i).getType(),5);
    		    	ev.getInventory().setResult(new ItemStack(Material.AIR));
    			}
    		}
    		
    		if (!nether_star_found) {
    			//This is a conversion recipe.
    			if (items_found.size()==2) {
    				//This is a conversion UP recipe.
    				if (items_found.get(0).equals(items_found.get(1))) {
    					//Now that we know these are the same. We can figure out which recipe to make.
    					String mat_suffix = "";
        				if (items_found.get(0).getType()==Material.SUGAR) {
        					mat_suffix = "ESSENCE";
        				} else
        				if (items_found.get(0).getType()==Material.MAGMA_CREAM) {
        					mat_suffix = "CORE";
        				} else
        				if (items_found.get(0).getType()==Material.CLAY_BALL) {
        					mat_suffix = "BASE";
        				}
        				if (items_found.get(0).getEnchantmentLevel(Enchantment.LUCK)==1) {
        					//This will give us Ancient tier.
        					ev.getInventory().setResult(Artifact.createArtifactItem(ArtifactItem.valueOf("ANCIENT_"+mat_suffix)));
        				} else
        				if (items_found.get(0).getEnchantmentLevel(Enchantment.LUCK)==2) {
        					//This will give us Lost tier.
        					ev.getInventory().setResult(Artifact.createArtifactItem(ArtifactItem.valueOf("LOST_"+mat_suffix)));
        				} else
        				if (items_found.get(0).getEnchantmentLevel(Enchantment.LUCK)==3) {
        					//This will give us  tier.
        					ev.getInventory().setResult(Artifact.createArtifactItem(ArtifactItem.valueOf("DIVINE_"+mat_suffix)));
        				} else
        				if (items_found.get(0).getEnchantmentLevel(Enchantment.LUCK)==4) {
        					//This produces nothing... No tier above Divine.
        					ev.getInventory().setResult(new ItemStack(Material.AIR));
        				}
    				}
    			}if (items_found.size()==1) {
    				//This is a conversion DOWN recipe.
    				//Find out what tier this is supposed to be.
    				String mat_suffix = "";
    				if (items_found.get(0).getType()==Material.SUGAR) {
    					mat_suffix = "ESSENCE";
    				} else
    				if (items_found.get(0).getType()==Material.MAGMA_CREAM) {
    					mat_suffix = "CORE";
    				} else
    				if (items_found.get(0).getType()==Material.CLAY_BALL) {
    					mat_suffix = "BASE";
    				}
    				if (items_found.get(0).getEnchantmentLevel(Enchantment.LUCK)==1) {
    					//No result. This is the lowest tier.
    					ev.getInventory().setResult(new ItemStack(Material.AIR));
    				} else
    				if (items_found.get(0).getEnchantmentLevel(Enchantment.LUCK)==2) {
    					//This produces Artifact Essence.
    					ItemStack newitem1 = Artifact.createArtifactItem(ArtifactItem.valueOf("ARTIFACT_"+mat_suffix));
    					newitem1.setAmount(2);
    					ev.getInventory().setResult(newitem1);
    				} else
    				if (items_found.get(0).getEnchantmentLevel(Enchantment.LUCK)==3) {
    					//This produces Ancient Essence.
    					ItemStack newitem1 = Artifact.createArtifactItem(ArtifactItem.valueOf("ANCIENT_"+mat_suffix));
    					newitem1.setAmount(2);
    					ev.getInventory().setResult(newitem1);
    				} else
    				if (items_found.get(0).getEnchantmentLevel(Enchantment.LUCK)==4) {
    					//This produces Lost Essence.
    					ItemStack newitem1 = Artifact.createArtifactItem(ArtifactItem.valueOf("LOST_"+mat_suffix));
    					newitem1.setAmount(2);
    					ev.getInventory().setResult(newitem1);
    				}
    			}
    		}
    	}
    	else
    	//We are looking for an artifact recipe.
    	if (ev.getInventory().getResult()!=null &&
    			ev.getInventory().getResult().getType()!=Material.AIR && Artifact.isArtifact(ev.getInventory().getResult())) {
    		//We are looking for an artifact piece.
    		int items_found=0;
    		int slot_found=0;
    		int tier_found=0;
    		int tier_recipe=0;
    		int artifact_tier=-1;
    		int essence_tier=-1;
    		int core_tier=-1;
    		int base_tier=-1;
    		ItemStack artifact_item=null;
    		boolean pumpkin_seeds=false;
			for (int i=1;i<ev.getInventory().getSize();i++) {
				if (ev.getInventory().getItem(i)!=null &&
						ev.getInventory().getItem(i).getType()!=Material.AIR &&
						Artifact.isArtifact(ev.getInventory().getItem(i))) {
			    	items_found++;
			    	TwosideKeeper.log("Items Found: "+items_found, 5);
			    	if (ev.getInventory().getItem(i).getType()==Material.PUMPKIN_SEEDS) {
			    		//We are not supposed to be in here!
			    		pumpkin_seeds=true;
			    	}
			    	if (ev.getInventory().getItem(i).getType()!=Material.STAINED_GLASS_PANE) {
			    		switch (ev.getInventory().getItem(i).getType()) {
				    		case SUGAR:{
				    			tier_recipe+=1+(ev.getInventory().getItem(i).getEnchantmentLevel(Enchantment.LUCK)-1)*3;
				    			essence_tier=ev.getInventory().getItem(i).getEnchantmentLevel(Enchantment.LUCK);
				    		}break;
				    		case MAGMA_CREAM:{
				    			tier_recipe+=2+(ev.getInventory().getItem(i).getEnchantmentLevel(Enchantment.LUCK)-1)*3;
				    			core_tier=ev.getInventory().getItem(i).getEnchantmentLevel(Enchantment.LUCK);
				    		}break;
				    		case CLAY_BALL:{
				    			tier_recipe+=3+(ev.getInventory().getItem(i).getEnchantmentLevel(Enchantment.LUCK)-1)*3;
				    			base_tier=ev.getInventory().getItem(i).getEnchantmentLevel(Enchantment.LUCK);
				    		}break;
			    		}
			    	} else
			    	if (tier_found==0 && ev.getInventory().getItem(i).getType()==Material.STAINED_GLASS_PANE) {
			    		tier_found=ev.getInventory().getItem(i).getEnchantmentLevel(Enchantment.LUCK);
			    		slot_found=i;
			    	} else
			    	if (ev.getInventory().getItem(i).getEnchantmentLevel(Enchantment.LUCK)!=tier_found && ev.getInventory().getItem(i).getType()==Material.STAINED_GLASS_PANE) {
			    		//Cancel this recipe, no good.
			    		ev.getInventory().setResult(new ItemStack(Material.AIR));
			    	}
			    	
			    	if (GenericFunctions.isEquip(ev.getInventory().getItem(i))) {
			    		artifact_item = ev.getInventory().getItem(i);
			    		artifact_tier = ev.getInventory().getItem(i).getEnchantmentLevel(Enchantment.LUCK);
			    	}
				} else
				if (ev.getInventory().getItem(i)!=null &&
						ev.getInventory().getItem(i).getType()!=Material.AIR &&
						!Artifact.isArtifact(ev.getInventory().getItem(i))) {
					log("One of these is not an artifact",5);
		    		ev.getInventory().setResult(new ItemStack(Material.AIR)); //Don't allow it, an item is not an artifact!
				}
			}
			if (items_found==1 && ev.getInventory().getResult().getType()!=null && ev.getInventory().getResult().getType()!=Material.AIR) {
				//This is a recipe->Base item conversion.
				ItemStack newitem = ArtifactItemType.getTypeFromData(ev.getInventory().getItem(slot_found).getDurability()).getTieredItem(tier_found);
				
				//Add more information for this.
				ev.getInventory().setResult(AwakenedArtifact.convertToAwakenedArtifact(newitem, tier_found, ev.getInventory().getItem(slot_found).getDurability()));
			}
			if (items_found==2 && slot_found!=0 && ev.getInventory().getResult().getType()!=null && ev.getInventory().getResult().getType()!=Material.AIR) {
				log("Artifact tier: "+artifact_tier+", Tier Found: "+tier_found,2);
				if (artifact_tier!=tier_found || tier_found==15) {
					ev.getInventory().setResult(new ItemStack(Material.AIR));
				} else {
					ItemStack newitem = ArtifactItemType.getTypeFromData(ev.getInventory().getItem(slot_found).getDurability()).getTieredItem(tier_found+1);
					
					//Add more information for this.
					
					ItemStack newartifact = AwakenedArtifact.convertToAwakenedArtifact(newitem, tier_found+1, ev.getInventory().getItem(slot_found).getDurability()).clone();
					List<String> transferlore = artifact_item.getItemMeta().getLore();
					ItemMeta m = newartifact.getItemMeta();
					m.setLore(transferlore);
					newartifact.setItemMeta(m);
					GenericFunctions.addObscureHardenedItemBreaks(newartifact, 5-GenericFunctions.getObscureHardenedItemBreaks(newartifact));
					//Lines can all be transferred over. No lines need to be preserved.
					
					//Transfer over all old enchantments. Don't transfer enchantments weaker than current enchantments.
					ItemStack resultitem = ev.getInventory().getResult().clone();
					for (int i=0;i<resultitem.getEnchantments().size();i++) {
						Enchantment e = (Enchantment)resultitem.getEnchantments().keySet().toArray()[i];
						if (newartifact.containsEnchantment(e) && artifact_item.getEnchantmentLevel(e)>newartifact.getEnchantmentLevel(e)) {
							log("Contains "+e.toString()+" "+newartifact.getEnchantmentLevel(e), 5);
							//These are the enchantments that clash. If the resultitem ones are greater, apply them to the new item.
							newartifact.addUnsafeEnchantment(e, artifact_item.getEnchantmentLevel(e));
							log("Applied "+e.getName()+" "+artifact_item.getEnchantmentLevel(e)+" to the artifact",5);
						}
					}
					for (int i=0;i<artifact_item.getEnchantments().size();i++) {
						Enchantment e = (Enchantment)artifact_item.getEnchantments().keySet().toArray()[i];
						if (!newartifact.containsEnchantment(e)) {
							//log("Contains "+e.toString()+" "+newartifact.getEnchantmentLevel(e), 2);
							//These are the enchantments that clash. If the resultitem ones are greater, apply them to the new item.
							newartifact.addUnsafeEnchantment(e, artifact_item.getEnchantmentLevel(e));
							log("Applied "+e.getName()+" "+artifact_item.getEnchantmentLevel(e)+" to the artifact",5);
						}
					}

					newartifact.setDurability((short)(newartifact.getType().getMaxDurability()*(artifact_item.getDurability()/artifact_item.getType().getMaxDurability())));
					ev.getInventory().setResult(newartifact);
				}
			}
			if (items_found==3 && !pumpkin_seeds && ev.getInventory().getResult().getType()!=null && ev.getInventory().getResult().getType()!=Material.AIR) {
				int tier = ev.getInventory().getItem(slot_found).getEnchantmentLevel(Enchantment.LUCK);
				log("This is tier "+tier+". Enchantment level of "+ev.getInventory().getItem(slot_found).toString(),5);
				//Decompose this into a higher tier of the next item.
				if (tier==tier_recipe && tier<14) {
					ItemStack newitem1 = Artifact.convert(new ItemStack(Material.STAINED_GLASS_PANE,1,ev.getInventory().getItem(slot_found).getDurability()));
					ItemMeta m = newitem1.getItemMeta();
					List<String> lore = m.getLore();
					lore.add(0,ChatColor.GOLD+""+ChatColor.BOLD+"T"+(tier+1)+" Crafting Recipe");
					//lore.add(1,ChatColor.GOLD+""+ChatColor.BOLD+"T"+tier+ChatColor.RESET+ChatColor.GOLD+" "+GenericFunctions.CapitalizeFirstLetters(item.getItemName())+" Recipe");
					
					m.setLore(lore);
					m.setDisplayName(ChatColor.GOLD+""+ChatColor.BOLD+"T"+(tier+1)+" Artifact "+GenericFunctions.CapitalizeFirstLetters(ArtifactItemType.getTypeFromData(ev.getInventory().getItem(slot_found).getDurability()).getItemName())+" Recipe");
					newitem1.setItemMeta(m);
					newitem1.addUnsafeEnchantment(Enchantment.LUCK, tier+1);
					ev.getInventory().setResult(newitem1);
				} else {
					ev.getInventory().setResult(new ItemStack(Material.AIR));
				}
			}
			if (items_found==5 && !pumpkin_seeds && ev.getInventory().getResult().getType()!=null && ev.getInventory().getResult().getType()!=Material.AIR
					) {
				if (essence_tier==4 && core_tier==4 && base_tier==4) {
					//It's allowed! Set the result to T10 recipe.
					TwosideKeeper.log("It is found.", 2);
					ItemStack newitem1 = Artifact.convert(new ItemStack(Material.STAINED_GLASS_PANE,1,ev.getInventory().getItem(slot_found).getDurability()));
					ItemMeta m = newitem1.getItemMeta();
					List<String> lore = m.getLore();
					int tier=14;
					lore.add(0,ChatColor.GOLD+""+ChatColor.BOLD+"T"+(tier)+" Crafting Recipe");
					//lore.add(1,ChatColor.GOLD+""+ChatColor.BOLD+"T"+tier+ChatColor.RESET+ChatColor.GOLD+" "+GenericFunctions.CapitalizeFirstLetters(item.getItemName())+" Recipe");
					
					m.setLore(lore);
					m.setDisplayName(ChatColor.GOLD+""+ChatColor.BOLD+"T"+(tier)+" Artifact "+GenericFunctions.CapitalizeFirstLetters(ArtifactItemType.getTypeFromData(ev.getInventory().getItem(slot_found).getDurability()).getItemName())+" Recipe");
					newitem1.setItemMeta(m);
					newitem1.addUnsafeEnchantment(Enchantment.LUCK, tier);
					ev.getInventory().setResult(newitem1);
				} else {
					ev.getInventory().setResult(new ItemStack(Material.AIR));
				}
			}
    	}
    	

    	//We are looking for an artifact recipe.
    	if (result.getType()==Material.STAINED_GLASS_PANE && Artifact.isArtifact(result)) { 
    		for (int i=0;i<ev.getInventory().getSize();i++) {
    			if (ev.getInventory().getItem(i)!=null &&
    					ev.getInventory().getItem(i).getType()!=Material.AIR &&
    					!Artifact.isArtifact(ev.getInventory().getItem(i))) {
    		    	ev.getInventory().setResult(new ItemStack(Material.AIR));
    				break;
    			}
    		}
    	}//A general clear recipe table check for any non-artifact items.
    	
    }

    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void MinecartBreakEvent(VehicleDestroyEvent ev) {
    	if (ev.getVehicle().getType()==EntityType.MINECART ||
    			ev.getVehicle().getType()==EntityType.MINECART_FURNACE ||
    			ev.getVehicle().getType()==EntityType.MINECART_TNT) {
    		ev.setCancelled(true);
    		ev.getVehicle().remove();
    		switch (ev.getVehicle().getType()) {
	    		case MINECART:{
	    			ev.getVehicle().getLocation().getWorld().dropItemNaturally(ev.getVehicle().getLocation(), new ItemStack(Material.MINECART));
	    		}break;
	    		case MINECART_FURNACE:{
	    			ev.getVehicle().getLocation().getWorld().dropItemNaturally(ev.getVehicle().getLocation(), new ItemStack(Material.MINECART));
	    			ev.getVehicle().getLocation().getWorld().dropItemNaturally(ev.getVehicle().getLocation(), new ItemStack(Material.FURNACE));
	    		}break;
	    		case MINECART_TNT:{
	    			ev.getVehicle().getLocation().getWorld().dropItemNaturally(ev.getVehicle().getLocation(), new ItemStack(Material.MINECART));
	    			ev.getVehicle().getLocation().getWorld().dropItemNaturally(ev.getVehicle().getLocation(), new ItemStack(Material.TNT));
	    		}break;
    		}
    	}
    }

    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void MinecartDestroyEvent(VehicleDestroyEvent ev) {
    	if (ev.getAttacker() instanceof Player && 
    			ev.getVehicle().getType()==EntityType.MINECART) {
    		Player p = (Player)(ev.getAttacker());
    		PlayerStructure pd = (PlayerStructure)playerdata.get(p.getUniqueId());
    		//p.sendMessage("Off.");
    		//Drop a minecart at the position.
    		pd.destroyedminecart=true;
    		ev.setCancelled(true);
    		ev.getVehicle().remove();
    		ev.getVehicle().getWorld().dropItemNaturally(ev.getVehicle().getLocation().add(0,1,0), new ItemStack(Material.MINECART));
    	}
    }
    
    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void MinecartExitEvent(VehicleExitEvent ev) {
    	if (ev.getExited()!=null) {
	    	if (ev.getExited() instanceof Player &&
	    			ev.getVehicle().getType()==EntityType.MINECART) {
	    		Player p = (Player)(ev.getExited());
	    		PlayerStructure pd = (PlayerStructure)playerdata.get(p.getUniqueId());
	    		//p.sendMessage("Off.");
	    		//Drop a minecart at the position.
	    		if (!pd.destroyedminecart) {
		    		ev.getVehicle().remove();
		    		ev.getVehicle().getWorld().dropItemNaturally(ev.getExited().getLocation().add(0,1,0), new ItemStack(Material.MINECART));
	    		} else {
	    			pd.destroyedminecart=false;
	    		}
	    	}
    	}
    }
    
    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void onTeleportEvent(PlayerTeleportEvent ev) {
    	if (ev.getCause().equals(TeleportCause.END_PORTAL)) {
	    	Player p = ev.getPlayer();
        	PlayerStructure pd = (PlayerStructure)playerdata.get(p.getUniqueId());
			//This is the player data structure we are looking for.
    	}
    	final Player p = ev.getPlayer();
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			public void run() {
				setPlayerMaxHealth(p);
			}
		},20);
    }
    
    public static String getWeatherIcon() {
    	long time = Bukkit.getWorld("world").getTime();
    	String weather = "";
    	if (Bukkit.getWorld("world").hasStorm()) {weather="\u2602";} else {if (time>=10000) {weather="\u263D";} else {weather="\u2600";}}
    	return weather;
    }
    
    public static String getTimeOfDay() {
    	long time = Bukkit.getWorld("world").getTime();
    	String tod = "";
    	if (time>0 && time<=3000) {
    		tod="\u00A7eMORNING";
    	} else
    	if (time>3000 && time<=10000) {
    		tod="\u00A76AFTERNOON";
    	} else
    	if (time>10000 && time<=13000) {
    		tod="\u00A73EVENING";
    	} else
    	if (time>13000 && time<23000) {
    		tod="\u00A79NIGHT";
    	} else {
    		tod="\u00A7dDAWN";
    	}
    	return tod;
    }
    
    public String getServerListPingString() {
    	long time = Bukkit.getWorld("world").getTime();
    	return "\u00A7bsig's Minecraft!\n"+getWeatherIcon()+"  \u00A7fCurrently: "+getTimeOfDay();
    }
    
    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void onServerListPing(ServerListPingEvent ev) {
    	ev.setMotd(getServerListPingString());
    }

	public void saveOurData(){
		getConfig().set("SERVERTICK", getServerTickTime()); //Add to how many ticks we've used.
		//getConfig().set("DAYMULT", DAYMULT);
		getConfig().set("SERVERCHECKERTICKS", SERVERCHECKERTICKS);
		//getConfig().set("TERMINALTIME", TERMINALTIME);
		//getConfig().set("DEATHPENALTY", DEATHPENALTY);
		//getConfig().set("RECYCLECHANCE", RECYCLECHANCE);
		//getConfig().set("RECYCLEDECAYAMT", RECYCLEDECAYAMT);
		getConfig().set("ITEMCUBEID", ITEMCUBEID);
		//getConfig().set("ARMOR/ARMOR_LEATHER_HP", ARMOR_LEATHER_HP);
		//getConfig().set("ARMOR/ARMOR_IRON_HP", ARMOR_IRON_HP);
		//getConfig().set("ARMOR/ARMOR_GOLD_HP", ARMOR_GOLD_HP);
		//getConfig().set("ARMOR/ARMOR_DIAMOND_HP", ARMOR_DIAMOND_HP);
		//getConfig().set("ARMOR/ARMOR_IRONBLOCK_HP", ARMOR_IRON2_HP);
		//getConfig().set("ARMOR/ARMOR_GOLDBLOCK_HP", ARMOR_GOLD2_HP);
		//getConfig().set("ARMOR/ARMOR_DIAMONDBLOCK_HP", ARMOR_DIAMOND2_HP);
		//getConfig().set("HEALTH/HEALTH_REGENERATION_RATE", HEALTH_REGENERATION_RATE);
		//getConfig().set("HEALTH/FOOD_HEAL_AMT", FOOD_HEAL_AMT);
		//getConfig().set("ENEMY/ENEMY_DMG_MULT", ENEMY_DMG_MULT);
		//getConfig().set("ENEMY/EXPLOSION_DMG_MULT", EXPLOSION_DMG_MULT);
		//getConfig().set("ENEMY/HEADSHOT_ACC", HEADSHOT_ACC);
		//getConfig().set("ITEM/RARE_DROP_RATE", RARE_DROP_RATE);
		//getConfig().set("ITEM/COMMON_DROP_RATE", COMMON_DROP_RATE);
		//getConfig().set("ITEM/LEGENDARY_DROP_RATE", LEGENDARY_DROP_RATE);
		//getConfig().set("PARTY_CHUNK_SIZE", PARTY_CHUNK_SIZE);
		//getConfig().set("XP_CONVERSION_RATE", XP_CONVERSION_RATE);
		getConfig().set("WORLD_SHOP_ID", WORLD_SHOP_ID);
		getConfig().set("LOGGING_LEVEL", LOGGING_LEVEL);
		//getConfig().set("ARTIFACT_RARITY", ARTIFACT_RARITY);
		getConfig().set("SERVER_TYPE", SERVER_TYPE.GetValue());
		getConfig().set("LAST_ELITE_SPAWN", LAST_ELITE_SPAWN);
		getConfig().set("ELITE_LOCATION_X", ELITE_LOCATION.getBlockX());
		getConfig().set("ELITE_LOCATION_Z", ELITE_LOCATION.getBlockZ());
		//getConfig().set("MOTD", MOTD); //It makes no sense to save the MOTD as it will never be modified in-game.
		saveConfig();
		
		TwosideRecyclingCenter.saveConfig();
		
		//Save user configs here too.
    	saveAllUserConfigs();
    	
    	habitat_data.saveLocationHashesToConfig();
    	
		log("[TASK] Configurations have been saved successfully.",3);
	}
	
	public void saveAllUserConfigs() {
		for (int i=0;i<Bukkit.getOnlinePlayers().size();i++) {
			Player p = (Player)(Bukkit.getOnlinePlayers().toArray()[i]);
        	PlayerStructure pd = (PlayerStructure)playerdata.get(p.getUniqueId());
    		pd.saveConfig();
    	}
	}
	
	//Configuration stuff.
	public void loadConfig(){
		getConfig().addDefault("SERVERTICK", SERVERTICK);
		getConfig().addDefault("DAYMULT", DAYMULT);
		getConfig().addDefault("SERVERCHECKERTICKS", SERVERCHECKERTICKS);
		getConfig().addDefault("TERMINALTIME", TERMINALTIME);
		getConfig().addDefault("DEATHPENALTY", DEATHPENALTY);
		getConfig().addDefault("RECYCLECHANCE", RECYCLECHANCE);
		getConfig().addDefault("RECYCLEDECAYAMT", RECYCLEDECAYAMT);
		getConfig().addDefault("ITEMCUBEID", ITEMCUBEID);
		getConfig().addDefault("MOTD", MOTD);
		getConfig().addDefault("ARMOR/ARMOR_LEATHER_HP", ARMOR_LEATHER_HP);
		getConfig().addDefault("ARMOR/ARMOR_IRON_HP", ARMOR_IRON_HP);
		getConfig().addDefault("ARMOR/ARMOR_GOLD_HP", ARMOR_GOLD_HP);
		getConfig().addDefault("ARMOR/ARMOR_DIAMOND_HP", ARMOR_DIAMOND_HP);
		getConfig().addDefault("ARMOR/ARMOR_IRONBLOCK_HP", ARMOR_IRON2_HP);
		getConfig().addDefault("ARMOR/ARMOR_GOLDBLOCK_HP", ARMOR_GOLD2_HP);
		getConfig().addDefault("ARMOR/ARMOR_DIAMONDBLOCK_HP", ARMOR_DIAMOND2_HP); 
		getConfig().addDefault("HEALTH/HEALTH_REGENERATION_RATE", HEALTH_REGENERATION_RATE);
		getConfig().addDefault("HEALTH/FOOD_HEAL_AMT", FOOD_HEAL_AMT);
		getConfig().addDefault("ENEMY/ENEMY_DMG_MULT", ENEMY_DMG_MULT);
		getConfig().addDefault("ENEMY/EXPLOSION_DMG_MULT", EXPLOSION_DMG_MULT);
		getConfig().addDefault("ENEMY/HEADSHOT_ACC", HEADSHOT_ACC);
		getConfig().addDefault("ITEM/RARE_DROP_RATE", RARE_DROP_RATE);
		getConfig().addDefault("ITEM/COMMON_DROP_RATE", COMMON_DROP_RATE);
		getConfig().addDefault("ITEM/LEGENDARY_DROP_RATE", LEGENDARY_DROP_RATE);
		getConfig().addDefault("PARTY_CHUNK_SIZE", PARTY_CHUNK_SIZE);
		getConfig().addDefault("XP_CONVERSION_RATE", XP_CONVERSION_RATE);
		getConfig().addDefault("WORLD_SHOP_ID", WORLD_SHOP_ID);
		getConfig().addDefault("LOGGING_LEVEL", LOGGING_LEVEL);
		getConfig().addDefault("ARTIFACT_RARITY", ARTIFACT_RARITY);
		getConfig().addDefault("SERVER_TYPE", SERVER_TYPE.GetValue());
		getConfig().addDefault("LAST_ELITE_SPAWN", LAST_ELITE_SPAWN);
		getConfig().options().copyDefaults(true);
		saveConfig();
		SERVERTICK = getConfig().getLong("SERVERTICK");
		DAYMULT =  getConfig().getDouble("DAYMULT");
		SERVERCHECKERTICKS = getConfig().getInt("SERVERCHECKERTICKS");
		TERMINALTIME = getConfig().getInt("TERMINALTIME"); 
		DEATHPENALTY = getConfig().getDouble("DEATHPENALTY");
		RECYCLECHANCE = getConfig().getDouble("RECYCLECHANCE"); 
		RECYCLEDECAYAMT = getConfig().getDouble("RECYCLEDECAYAMT");
		ITEMCUBEID = getConfig().getInt("ITEMCUBEID");
		ARMOR_LEATHER_HP = getConfig().getDouble("ARMOR/ARMOR_LEATHER_HP");
		ARMOR_IRON_HP = getConfig().getDouble("ARMOR/ARMOR_IRON_HP");
		ARMOR_GOLD_HP = getConfig().getDouble("ARMOR/ARMOR_GOLD_HP");
		ARMOR_DIAMOND_HP = getConfig().getDouble("ARMOR/ARMOR_DIAMOND_HP");
		ARMOR_IRON2_HP = getConfig().getDouble("ARMOR/ARMOR_IRONBLOCK_HP");
		ARMOR_GOLD2_HP = getConfig().getDouble("ARMOR/ARMOR_GOLDBLOCK_HP");
		ARMOR_DIAMOND2_HP = getConfig().getDouble("ARMOR/ARMOR_DIAMONDBLOCK_HP");
		HEALTH_REGENERATION_RATE = getConfig().getLong("HEALTH/HEALTH_REGENERATION_RATE");
		FOOD_HEAL_AMT = getConfig().getInt("HEALTH/FOOD_HEAL_AMT");
		ENEMY_DMG_MULT = getConfig().getDouble("ENEMY/ENEMY_DMG_MULT");
		EXPLOSION_DMG_MULT = getConfig().getDouble("ENEMY/EXPLOSION_DMG_MULT")/2;
		HEADSHOT_ACC = getConfig().getDouble("ENEMY/HEADSHOT_ACC");
		RARE_DROP_RATE = getConfig().getDouble("ITEM/RARE_DROP_RATE");
		COMMON_DROP_RATE = getConfig().getDouble("ITEM/COMMON_DROP_RATE");
		LEGENDARY_DROP_RATE = getConfig().getDouble("ITEM/LEGENDARY_DROP_RATE");
		PARTY_CHUNK_SIZE = getConfig().getInt("PARTY_CHUNK_SIZE");
		XP_CONVERSION_RATE = getConfig().getDouble("XP_CONVERSION_RATE");
		WORLD_SHOP_ID = getConfig().getInt("WORLD_SHOP_ID");
		LOGGING_LEVEL = getConfig().getInt("LOGGING_LEVEL");
		ARTIFACT_RARITY = getConfig().getDouble("ARTIFACT_RARITY");
		SERVER_TYPE = ServerType.GetTypeFromValue(getConfig().getInt("SERVER_TYPE"));
		LAST_ELITE_SPAWN = getConfig().getLong("LAST_ELITE_SPAWN");
		if (getConfig().contains("ELITE_LOCATION_X")) {
			int x = getConfig().getInt("ELITE_LOCATION_X");
			int z = getConfig().getInt("ELITE_LOCATION_Z");
			TwosideKeeper.ELITE_LOCATION=new Location(Bukkit.getWorld("world"),x,0,z);
		} else {
			log("Did not find a valid Elite Location! Creating a new one!",2);
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				@Override
				public void run() {
					GenericFunctions.generateNewElite();
				}
			},20);
		}
		getMOTD();
		
		//Informational reports to the console.
		log("[CONFIG] Server Type is set to "+SERVER_TYPE+".",2);
		log("[CONFIG] SERVERTICK set to "+SERVERTICK+".",3);
		log("[CONFIG] SPEED of Day/Night Cycles are x"+DAYMULT,3);
		log("[CONFIG] Server will auto-save configs every "+SERVERCHECKERTICKS+" ticks.",3);
		log("[CONFIG] Withdraw/Deposit terminals can be used for "+TERMINALTIME+" ticks.",4);
		log("[CONFIG] Death Penalty is "+DEATHPENALTY+"% of holding money.",4);
		log("[CONFIG] Chance to Recycle an Item on Despawn: "+RECYCLECHANCE+"%. Decay AMT: "+RECYCLEDECAYAMT+"%.",4);
		log("[CONFIG] Item Cube ID is currently at "+ITEMCUBEID+". World Shop ID is at "+WORLD_SHOP_ID,5);
		log("[CONFIG] Health Regeneration Rate is  "+HEALTH_REGENERATION_RATE+" ticks per heal.",4);
		log("[CONFIG] Enemy Damage Multiplier x"+ENEMY_DMG_MULT+". Explosion Damage Multiplier x"+EXPLOSION_DMG_MULT,3);
		log("[CONFIG] Headshots have to be "+(HEADSHOT_ACC*100)+"% accurate.",4);
		log("[CONFIG] Drop Rates are currently ("+(COMMON_DROP_RATE*100)+"%,"+(RARE_DROP_RATE*100)+"%,"+(LEGENDARY_DROP_RATE*100)+"%). Artifact Drop Rarity is x"+ARTIFACT_RARITY,5);
		log("[CONFIG] Party Chunk Size Detection is set to "+(PARTY_CHUNK_SIZE)+" chunks.",5);
		log("[CONFIG] XP Conversion rate is $"+XP_CONVERSION_RATE+" per XP Point.",5);
		getLogger().info("[CONFIG] Console Logging Level set to "+LOGGING_LEVEL+".");
		log("----------TwosideKeeper----------",5);
		log("You are running version _"+Bukkit.getPluginManager().getPlugin("TwosideKeeper").getDescription().getVersion()+"_ of TwosideKeeper.",5);
		log("---------------------------------",5);
	}
	
	public void getMOTD() {
		//We want to grab it from a file we can easily modify.
		File motd_config;
		motd_config = new File(TwosideKeeper.filesave,"motd.data");
		FileConfiguration workable = YamlConfiguration.loadConfiguration(motd_config);
		workable.addDefault("MOTD", "");
		MOTD=workable.getString("MOTD");
		try {
			workable.save(motd_config);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Item Cube Loading.
	public static List<ItemStack> itemCube_loadConfig(int id){
		List<ItemStack> ItemCube_items = new ArrayList<ItemStack>();
		File config;
		config = new File(TwosideKeeper.filesave,"itemcubes/ItemCube"+id+".data");
		FileConfiguration workable = YamlConfiguration.loadConfiguration(config);
		
		for (int i=0;i<27;i++) {
			ItemCube_items.add(workable.getItemStack("item"+i, new ItemStack(Material.AIR)));
		}
		return ItemCube_items;
	}
	public CubeType itemCube_getCubeType(int id){
		List<ItemStack> ItemCube_items = new ArrayList<ItemStack>();
		File config;
		config = new File(TwosideKeeper.filesave,"itemcubes/ItemCube"+id+".data");
		FileConfiguration workable = YamlConfiguration.loadConfiguration(config);
		
		switch (workable.getInt("cubetype")) {
			case 0:{return CubeType.NORMAL;} 
			case 1:{return CubeType.LARGE;}
			case 2:{return CubeType.ENDER;}
			default:{return CubeType.NORMAL;}
		}
	}
	
	//Item Cube Saving.
	public void itemCube_saveConfig(int id, List<ItemStack> items){
		List<ItemStack> ItemCube_items = new ArrayList<ItemStack>();
		File config;
		config = new File(TwosideKeeper.filesave,"itemcubes/ItemCube"+id+".data");
		FileConfiguration workable = YamlConfiguration.loadConfiguration(config);
		
		for (int i=0;i<items.size();i++) {
			workable.set("item"+i, items.get(i));
		}
		//workable.set("cubetype", cubetype);
		try {
			workable.save(config);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void itemCube_saveConfig(int id, List<ItemStack> items, CubeType cubetype){
		List<ItemStack> ItemCube_items = new ArrayList<ItemStack>();
		File config;
		config = new File(TwosideKeeper.filesave,"itemcubes/ItemCube"+id+".data");
		FileConfiguration workable = YamlConfiguration.loadConfiguration(config);
		
		for (int i=0;i<items.size();i++) {
			workable.set("item"+i, items.get(i));
		}
		switch (cubetype) { //We have to convert it to a number because it's using the old version. We can't take advantage of the enum at this point.
			case NORMAL:{workable.set("cubetype", 0);}break;
			case LARGE:{workable.set("cubetype", 1);}break;
			case ENDER:{workable.set("cubetype", 2);}break;
		}
		try {
			workable.save(config);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static final void adjustServerTime(long amt) {
		Bukkit.getWorld("world").setFullTime(Bukkit.getWorld("world").getFullTime()-amt);
		time_passed+=amt;
	}
	
	public static long getServerTickTime() {
		//As the SERVERTICK variable is never actually updated,
		//we have to recalculate the actual value of it if we want to use it.
		long time = SERVERTICK + time_passed + Bukkit.getWorld("world").getFullTime();
		log("Server Tick Time: "+time,4);
		return time;
	}
	
	public static boolean isInteger(String s) {
	    return isInteger(s,10);
	}

	public static boolean isInteger(String s, int radix) {
	    if(s.isEmpty()) return false;
	    for(int i = 0; i < s.length(); i++) {
	        if(i == 0 && s.charAt(i) == '-') {
	            if(s.length() == 1) return false;
	            else continue;
	        }
	        if(Character.digit(s.charAt(i),radix) < 0) return false;
	    }
	    return true;
	}
	
	public static boolean isNumeric(String str)
	{
	  return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
	}
	
	public static String createHealthbar(double pcthp, Player p) {
		//IF YOU EVER UPDATE THIS YOU MUST UPDATE THE PLAYERDEATHEVENT DEATH MSG METHOD!!!!
		//Heart character: 2665
		/*String bar = ChatColor.AQUA+" ";
		int length = 0;
		if (pcthp>100) {pcthp=100;}
		while (pcthp>=20) {
			pcthp-=20;
			bar+=Character.toString((char)0x2588);
			length++;
		}
		if (pcthp>=16) {
			bar+=Character.toString((char)0x2588);
			length++;
		} else 
		if (pcthp>=12) {
			bar+=Character.toString((char)0x2593);
			length++;
		} else 
		if (pcthp>=8) {
			bar+=Character.toString((char)0x2592);
			length++;
		} else 
		if (pcthp>=1) {
			bar+=Character.toString((char)0x2591);
			length++;
		} else {
			bar+=" ";
			length++;
		}
		while (length<6) {
			bar+=" ";
			length++;
		}*/
		
		boolean hasDebuff=false;
		boolean isHungry=(p.getFoodLevel()<=16)?true:false;
		boolean inNether=(p.getWorld().getName().equalsIgnoreCase("world_nether"))?true:false;
		boolean inEnd=(p.getWorld().getName().equalsIgnoreCase("world_the_end"))?true:false;
		int absorptionlv=0;
		
		for (int i=0;i<p.getActivePotionEffects().size();i++) {
			if (Iterables.get(p.getActivePotionEffects(), i).getType().equals(PotionEffectType.BLINDNESS) ||
					Iterables.get(p.getActivePotionEffects(), i).getType().equals(PotionEffectType.CONFUSION) ||
					Iterables.get(p.getActivePotionEffects(), i).getType().equals(PotionEffectType.HARM) ||
					Iterables.get(p.getActivePotionEffects(), i).getType().equals(PotionEffectType.POISON) ||
					Iterables.get(p.getActivePotionEffects(), i).getType().equals(PotionEffectType.SLOW) ||
					Iterables.get(p.getActivePotionEffects(), i).getType().equals(PotionEffectType.SLOW_DIGGING) ||
					Iterables.get(p.getActivePotionEffects(), i).getType().equals(PotionEffectType.WEAKNESS) ||
					Iterables.get(p.getActivePotionEffects(), i).getType().equals(PotionEffectType.WITHER) ||
					Iterables.get(p.getActivePotionEffects(), i).getType().equals(PotionEffectType.UNLUCK)) {
				hasDebuff=true;
			}
			if (Iterables.get(p.getActivePotionEffects(), i).getType().equals(PotionEffectType.ABSORPTION)) {
				absorptionlv=Iterables.get(p.getActivePotionEffects(), i).getAmplifier()+1;
			}
		}
		
		String bar = " ";
		int length = 0;
		if (pcthp==100) {bar += ((isHungry)?ChatColor.BLUE:ChatColor.AQUA)+""+Math.round(p.getHealth())+""+Character.toString((char)0x2665);} else
		if (pcthp>66) {bar += ((isHungry)?ChatColor.DARK_GREEN:ChatColor.GREEN)+""+Math.round(p.getHealth())+""+Character.toString((char)0x2665);}
		else if (pcthp>33) {bar += ((isHungry)?ChatColor.GOLD:ChatColor.YELLOW)+""+Math.round(p.getHealth())+""+Character.toString((char)0x2665);}
		else {bar += ((isHungry)?ChatColor.DARK_RED:ChatColor.RED)+""+Math.round(p.getHealth())+""+Character.toString((char)0x2665);}
		
		/*
		if (absorptionlv>0) {
			bar+=" "+ChatColor.ITALIC+""+ChatColor.LIGHT_PURPLE+"+"+(absorptionlv*4)+Character.toString((char)0x2665);
		}*/
		
		if (hasDebuff||isHungry||inNether) {
			bar+=" ";
		}
		
		if (hasDebuff) {
			bar+=ChatColor.GRAY+"!";
		}
		/*if (isHungry) {
			bar+=ChatColor.RED+""+ChatColor.BOLD+"!";
		}*/
		if (inNether) {
			/* 058D:Counter-clockwise portal
			 * 058E:Clockwise portal
			 */
			bar+=ChatColor.DARK_PURPLE+""+Character.toString((char)0x25CA);
			//bar+=ChatColor.DARK_PURPLE+""+"�";
		} else
		if (inEnd) {
			/* 058D:Counter-clockwise portal
			 * 058E:Clockwise portal
			 */
			bar+=ChatColor.DARK_BLUE+""+Character.toString((char)0x25CA);
			//bar+=ChatColor.DARK_PURPLE+""+"�";
		}
		
		return bar;
	}
	
	//Returns if a player has an arrow quiver in their inventory.
	public boolean playerHasArrowQuiver(Player p) {
		log("Checking an inventory of size "+p.getInventory().getSize()+" for arrow quiver.",5);
		for (int i=0;i<p.getInventory().getSize();i++) {
			if (p.getInventory().getItem(i)!=null &&
					p.getInventory().getItem(i).getType()==Material.TIPPED_ARROW &&
					p.getInventory().getItem(i).getEnchantmentLevel(Enchantment.ARROW_INFINITE)==5) {
				//This is an arrow quiver.
				log("Found an arrow quiver in slot "+i,4);
				return true;
			}
		}
		return false;
	}
	
	//Returns the slot of the first arrow quiver in a player's inventory. Returns -1 if not found. Recommended to use playerHasArrowQuiver() first.
	public int playerGetArrowQuiver(Player p) {
		for (int i=0;i<p.getInventory().getSize();i++) {
			if (p.getInventory().getItem(i)!=null &&
					p.getInventory().getItem(i).getType()==Material.TIPPED_ARROW &&
					p.getInventory().getItem(i).getEnchantmentLevel(Enchantment.ARROW_INFINITE)==5) {
				//This is an arrow quiver.
				return i;
			}
		}
		return -1;
	}
	
	//Inserts amt arrows in the arrow quiver in slot slot.
	public void playerInsertArrowQuiver(Player p, int slot, int amt) {
		ItemStack ArrowQuiver = p.getInventory().getItem(slot);
		ItemMeta ArrowQuiver_meta = ArrowQuiver.getItemMeta();
		int ArrowQuiver_amt = Integer.parseInt(ArrowQuiver.getItemMeta().getLore().get(1).split(": "+ChatColor.YELLOW)[1]);
		ArrowQuiver_amt += amt;
		//Insert new amount.
		List<String> newlore = ArrowQuiver.getItemMeta().getLore();
		newlore.set(1, ChatColor.GRAY+"Arrows Remaining: "+ChatColor.YELLOW+ArrowQuiver_amt);
		ArrowQuiver_meta.setLore(newlore);
		ArrowQuiver.setItemMeta(ArrowQuiver_meta);
	}
	
	public int playerGetArrowQuiverAmt(Player p, int slot) {
		ItemStack ArrowQuiver = p.getInventory().getItem(slot);
		ItemMeta ArrowQuiver_meta = ArrowQuiver.getItemMeta();
		int ArrowQuiver_amt = Integer.parseInt(ArrowQuiver.getItemMeta().getLore().get(1).split(": "+ChatColor.YELLOW)[1]);
		return ArrowQuiver_amt;
	}

	//Removes amt arrows in the arrow quiver in slot slot. If there's not enough arrows, sets the amount to 0.
	//Returns how many arrows are left, or 0 if it's now empty.
	public int playerRemoveArrowQuiver(Player p, int slot, int amt) {
		ItemStack ArrowQuiver = p.getInventory().getItem(slot);
		ItemMeta ArrowQuiver_meta = ArrowQuiver.getItemMeta();
		int ArrowQuiver_amt = Integer.parseInt(ArrowQuiver.getItemMeta().getLore().get(1).split(": "+ChatColor.YELLOW)[1]);
		ArrowQuiver_amt -= amt;
		if (ArrowQuiver_amt<0) {ArrowQuiver_amt=0;}
		List<String> newlore = ArrowQuiver.getItemMeta().getLore();
		newlore.set(1, ChatColor.GRAY+"Arrows Remaining: "+ChatColor.YELLOW+ArrowQuiver_amt);
		ArrowQuiver_meta.setLore(newlore);
		ArrowQuiver.setItemMeta(ArrowQuiver_meta);
		return ArrowQuiver_amt;
	}
	
	
	
	///////////////ALL PLAYER RELATED FUNCTIONS GO DOWN HERE.
	public static double getPlayerMoney(Player p) {
		//Tells a player how much money they have.
    	PlayerStructure pd = (PlayerStructure)playerdata.get(p.getUniqueId());
    	//Found it. Read money and quit.
		Double d = Double.valueOf(pd.money);
		DecimalFormat df = new DecimalFormat("0.00");
		return Double.parseDouble(df.format(d));
	}
	public static double getPlayerMoney(String p) {
		//See if the data file exists, open it.
		if (Bukkit.getPlayer(p)!=null) {
			DecimalFormat df = new DecimalFormat("0.00");
			return Double.parseDouble(df.format(getPlayerMoney(Bukkit.getPlayer(p))));
		} else {
			File config;
			config = new File(TwosideKeeper.filesave,"users/"+p+".data");
			FileConfiguration workable = YamlConfiguration.loadConfiguration(config);
			if (!config.exists()) {
				//Something bad happened if we got here.
				log("[WARNING] Could not find the correct player data file for "+p+" to get money data from.",1);
				return -1;
			}
			return workable.getDouble("money");
		}
	}
	
	public static double getPlayerBankMoney(Player p) {
		//Tells a player how much money they have.
    	PlayerStructure pd = (PlayerStructure)playerdata.get(p.getUniqueId());
		//Found it. Read money and quit.
		Double d = Double.valueOf(pd.bank_money);
		DecimalFormat df = new DecimalFormat("0.00");
		return Double.parseDouble(df.format(d));
	}

	public static double getPlayerBankMoney(String p) {
		if (Bukkit.getPlayer(p)!=null) {
			DecimalFormat df = new DecimalFormat("0.00");
			return Double.parseDouble(df.format(getPlayerBankMoney(Bukkit.getPlayer(p))));
		} else {
			//See if the data file exists, open it.
			File config;
			config = new File(TwosideKeeper.filesave,"users/"+p+".data");
			FileConfiguration workable = YamlConfiguration.loadConfiguration(config);
	
			if (!config.exists()) {
				//Something bad happened if we got here.
				log("[WARNING] Could not find the correct player data file for "+p+" to get money data from.",1);
				return -1;
			}
			
			return workable.getDouble("bank_money");
		}
	}
	
	public static void givePlayerMoney(Player p, double amt) {
		boolean found=false;
    	PlayerStructure pd = (PlayerStructure)playerdata.get(p.getUniqueId());
		//Found it. Read money and quit.
		pd.money+=amt;
	}
	public static void givePlayerMoney(String p, double amt) {
		if (Bukkit.getPlayer(p)!=null) {
			givePlayerMoney(Bukkit.getPlayer(p),amt);
		} else {
			//See if the data file exists, open it.
			File config;
			config = new File(TwosideKeeper.filesave,"users/"+p+".data");
			FileConfiguration workable = YamlConfiguration.loadConfiguration(config);
	
			if (!config.exists()) {
				//Something bad happened if we got here.
				log("[WARNING] Could not find the correct player data file for "+p+" to get money data from.",1);
			} else {
				
				double money = workable.getDouble("money");
				money+=amt;
				
				workable.set("money", money);
				
				try {
					workable.save(config);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					log("[WARNING] Could not find the correct player data file for "+p+" to get money data from.",1);
					e.printStackTrace();
				}
			}
		}
	}
	public static void givePlayerBankMoney(Player p, double amt) {
		boolean found=false;
    	PlayerStructure pd = (PlayerStructure)playerdata.get(p.getUniqueId());
		//Found it. Read money and quit.
		pd.bank_money+=amt;
	}
	public static void givePlayerBankMoney(String p, double amt) {
		if (Bukkit.getPlayer(p)!=null) {
			givePlayerBankMoney(Bukkit.getPlayer(p),amt);
		} else {
			//See if the data file exists, open it.
			File config;
			config = new File(TwosideKeeper.filesave,"users/"+p+".data");
			FileConfiguration workable = YamlConfiguration.loadConfiguration(config);
			
			double money = workable.getDouble("bank_money");
			
			money+=amt;
			
			workable.set("bank_money", money);
			
			try {
				workable.save(config);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				log("[WARNING] Could not find the correct player data file for "+p+" to get bank money data from.",1);
				e.printStackTrace();
			}
		}
	}
	
	public static void setPlayerMaxHealth(Player p) {
		//Determine player max HP based on armor being worn.
		double hp=10; //Get the base max health.
		//Get all equips.
		ItemStack[] equipment = {p.getInventory().getHelmet(),p.getInventory().getChestplate(),p.getInventory().getLeggings(),p.getInventory().getBoots()};
		double maxdeduction=1;
		for (int i=0;i<equipment.length;i++) {
			if (equipment[i]!=null) {
				boolean is_block_form=false;
				//Determine if the piece is block form.
				//If this is an artifact armor, we totally override the base damage reduction.
				if (GenericFunctions.isArmor(equipment[i]) && Artifact.isArtifact(equipment[i])) {
					//Let's change up the damage.
					log("This is getting through",5);
					/*int dmgval = ArtifactItemType.valueOf(Artifact.returnRawTool(equipment[i].getType())).getHealthAmt(equipment[i].getEnchantmentLevel(Enchantment.LUCK));
					if (dmgval!=-1) {
						hp += dmgval;
					}*/
				} else {
					if (equipment[i].hasItemMeta() &&
							equipment[i].getItemMeta().hasLore()) {
						for (int j=0;j<equipment[i].getItemMeta().getLore().size();j++) {
							if (equipment[i].getItemMeta().getLore().get(j).contains(ChatColor.GRAY+"Breaks Remaining:")) {
								//This is a block version.
								is_block_form=true;
								break;
							}
						}
					}
					if (equipment[i].getType().toString().contains("LEATHER")) {
						//This is a leather piece.
						hp+=ARMOR_LEATHER_HP;
					} else if (equipment[i].getType().toString().contains("IRON")) {
						//This is an iron piece.
						hp+=(is_block_form)?ARMOR_IRON2_HP:ARMOR_IRON_HP;
					} else  if (equipment[i].getType().toString().contains("GOLD")) {
						//This is a gold piece.
						hp+=(is_block_form)?ARMOR_GOLD2_HP:ARMOR_GOLD_HP;
					} else  if (equipment[i].getType().toString().contains("DIAMOND")) {
						//This is a diamond piece.
						hp+=(is_block_form)?ARMOR_DIAMOND2_HP:ARMOR_DIAMOND_HP;
					}
				}
				if (GenericFunctions.isArtifactEquip(equipment[i])) {
					log("Add in "+GenericFunctions.getAbilityValue(ArtifactAbility.HEALTH, equipment[i]),5);
					hp += (double)GenericFunctions.getAbilityValue(ArtifactAbility.HEALTH, equipment[i]);
					
					if (ArtifactAbility.containsEnchantment(ArtifactAbility.GREED, equipment[i])) {
						maxdeduction /= ArtifactAbility.containsEnchantment(ArtifactAbility.GREED, equipment[i])?2:1;
					}
				}
			}
		}
		log("Health is now "+hp,5);
		if (ArtifactAbility.containsEnchantment(ArtifactAbility.GREED, p.getEquipment().getItemInMainHand())) {
			maxdeduction /= ArtifactAbility.containsEnchantment(ArtifactAbility.GREED, p.getEquipment().getItemInMainHand())?2:1;
		}
		log("maxdeduction is "+maxdeduction,5);
		
		if (GenericFunctions.isDefender(p)) {
			hp+=10;
			p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,60,(p.isBlocking())?1:0));
		}
		

		for (int i=0;i<GenericFunctions.getEquipment(p).length;i++) {
			ItemSet set = ItemSet.GetSet(GenericFunctions.getEquipment(p)[i]);
			if (set!=null) {
				if (set==ItemSet.SONGSTEEL) {
					hp += set.GetBaseAmount(GenericFunctions.getEquipment(p)[i]);
				}
			}
		}
		hp+=ItemSet.TotalBaseAmountBasedOnSetBonusCount(p, ItemSet.SONGSTEEL, 2, 2);
		
		/*
		if (p.hasPotionEffect(PotionEffectType.ABSORPTION)) {
			Collection<PotionEffect> player_effects = p.getActivePotionEffects();
			for (int i=0;i<player_effects.size();i++) {
				if (Iterables.get(player_effects, i).getType().equals(PotionEffectType.ABSORPTION)) {
					hp += (Iterables.get(player_effects, i).getAmplifier()+1)*4;
				}
			}
		}*/
		
		if (GenericFunctions.HasFullRangerSet(p)) {
			hp += 20;
		}
		
		hp*=maxdeduction;
		
		if (p.getHealth()>=hp) {
			p.setHealth(hp);
		}
		p.setMaxHealth(hp);
		if (!p.isDead()) {
			p.setHealth(p.getHealth());
		}
		p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(hp);
	}
	

	
	public static void updateTitle(final Player p, boolean headshot, boolean preemptive) {
		if (preemptive) {
			updateTitle(p,ChatColor.BLUE+"!");
			/*
			Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
				public void run() {
					updateTitle(p);
				}
			},15);*/
		} else {
			updateTitle(p, headshot);
		}
	}
	
	
	public static void updateTitle(final Player p, boolean headshot) {
		if (headshot) {
			updateTitle(p,ChatColor.DARK_RED+"HEADSHOT !");
			TwosideKeeper.log("Run here.", 5);
			/*
			Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
				public void run() {
					updateTitle(p);
				}
			},15);*/
		} else {
			updateTitle(p);
		} 
	}
	
	public static void updateTitle(final Player p, PlayerStructure pd) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(TwosideKeeper.plugin, new Runnable() {
			public void run() {
				DecimalFormat df = new DecimalFormat("0.0");
				updateTitle(p,ChatColor.AQUA+""+df.format(pd.damagedata.getLastDamageDealt()));
			}
		},1);
	}

	public static void updateTitle(final Player p) {
		updateTitle(p, "");
	}
	
	public static void updateTitle(final Player p, final String message1) {
		//Updates the target title for this player.
    	PlayerStructure pd = (PlayerStructure)playerdata.get(p.getUniqueId());
		final PlayerStructure pd2=pd;
		Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("TwosideKeeper"), new Runnable() {
			public void run() {
				if (Bukkit.getPlayer(pd2.name)!=null && pd2.target!=null) {
					String MonsterName = pd2.target.getType().toString().toLowerCase();
					if (pd2.target.getCustomName()!=null) {
						MonsterName = pd2.target.getCustomName();
						if (MonsterName.contains(ChatColor.DARK_RED+"Hellfire") &&
								pd2.target.getType()!=EntityType.ENDERMAN) {
							pd2.target.setFireTicks(99999);
						}
						if (pd2.target.getCustomName()!=null &&
								!pd2.target.getCustomName().contains("Leader") &&
								MonsterController.isZombieLeader(pd2.target)) {
							pd2.target.setCustomName(pd2.target.getCustomName()+" Leader");
							MonsterName = pd2.target.getCustomName();
						}
					} else {
						MonsterName = GenericFunctions.CapitalizeFirstLetters(MonsterName.replace("_", " "));
					}
					final String finalMonsterName = MonsterName;
					String heartdisplay = "", remainingheartdisplay = "";
					int color1=0,color2=1;
					double health=pd2.target.getHealth();
					double maxhealth=pd2.target.getMaxHealth();
					if (health>20) {
						while (health>20) {
							color1++;
							color2++;
							health-=20;
						}
					}
					for (int i=0;i<health/2;i++) {
						remainingheartdisplay+=Character.toString((char)0x2665);
					}
					if (maxhealth>20) {
						for (int i=0;i<10;i++) {
							heartdisplay+=Character.toString((char)0x2665);
						}
					} else {
						for (int i=0;i<maxhealth/2;i++) {
							heartdisplay+=Character.toString((char)0x2665);
						}
					}
					
					ChatColor finalcolor = GetHeartColor(color1);
					ChatColor finalcolor2 = GetHeartColor(color2);
					final String finalheartdisplay=finalcolor2+((finalcolor2==ChatColor.MAGIC)?remainingheartdisplay.replace((char)0x2665, 'A'):remainingheartdisplay)+finalcolor+((finalcolor==ChatColor.MAGIC)?heartdisplay.substring(0, heartdisplay.length()-remainingheartdisplay.length()).replace((char)0x2665, 'A'):heartdisplay.substring(0, heartdisplay.length()-remainingheartdisplay.length()));
					TwosideKeeper.log("Message 1 is "+message1, 5);
					p.sendTitle(message1, finalMonsterName+" "+finalheartdisplay+" "+ChatColor.RESET+ChatColor.DARK_GRAY+"x"+(int)(pd2.target.getHealth()/20+1));
				}}}
		,1);
		if (Bukkit.getPlayer(pd2.name)!=null) {
			if (pd.title_task!=-1) {
				Bukkit.getScheduler().cancelTask(pd.title_task);
				pd.title_task=-1;
			}
			pd.title_task=Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("TwosideKeeper"), new Runnable() {
				public void run() {
					if (Bukkit.getPlayer(pd2.name)!=null) {
			    	PlayerStructure pd = (PlayerStructure)playerdata.get(p.getUniqueId());
						pd.title_task=-1;
						p.sendTitle("","");
					}
				}
			},15);
		}
	}
	
	/*
    			
    			final double basedmg = 0.0; //Sets the damage we will be dealing before calculating defenses.
    			final double dmgamt = 0.0; //New calculated damage amount.
        		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
        			public void run() {
        				if (m!=null) {
        					m.damage(dmgamt);
        				}
        			}}
        		,1);*/
	
	static public void openItemCubeInventory(Inventory inv, InventoryView inv_view) {
    	//Check if this is an Item Cube inventory.
		//p.sendMessage("This is an Item Cube inventory.");
		int id = Integer.parseInt(inv.getTitle().split("#")[1]);
		List<ItemStack> itemcube_contents = itemCube_loadConfig(id);
		for (int i=0;i<inv_view.getTopInventory().getSize();i++) {
			if (itemcube_contents.get(i)!=null) {
        		log("Loading item "+itemcube_contents.get(i).toString()+" in slot "+i,5);
				if (itemcube_contents.get(i).getAmount()>0) {
					inv.setItem(i, itemcube_contents.get(i));
				} else {
					inv.setItem(i, new ItemStack(Material.AIR));
				}
			}
		}
	}
	
	/**
	 * Removes amt amount of items from a player's inventory, given
	 * an item. 
	 */
	public void RemoveItemAmount(Player p, ItemStack item, int amt) {
		int amount_to_remove = amt;
		for (int i=0;i<p.getInventory().getSize();i++) {
			if (p.getInventory().getItem(i)!=null &&
					p.getInventory().getItem(i).isSimilar(item)) {
				//This is an item to subtract.
				amount_to_remove -= p.getInventory().getItem(i).getAmount();
				if (amount_to_remove<0) {
					ItemStack it = p.getInventory().getItem(i).clone();
					it.setAmount(-amount_to_remove);
					p.getInventory().setItem(i, it);
					break;
				} else {
					p.getInventory().setItem(i, new ItemStack(Material.AIR));
				}
			}
		}
	}
	
	public static ChatColor GetHeartColor(int colorval) {
		switch (colorval % 10) {
			case 0:{
				return ChatColor.DARK_GRAY;
			}
			case 1:{ //21-40HP
				return ChatColor.RED;
			}
			case 2:{ //41-60HP
				return ChatColor.BLUE;
			}
			case 3:{ //61-80HP
				return ChatColor.GREEN;
			}
			case 4:{ //81-100HP
				return ChatColor.YELLOW;
			}
			case 5:{ //101-120HP
				return ChatColor.GOLD;
			}
			case 6:{ //121-140HP
				return ChatColor.LIGHT_PURPLE; 
			}
			case 7:{ //141-160HP
				return ChatColor.DARK_PURPLE;
			}
			case 8:{ //161-180HP
				return ChatColor.AQUA;
			}
			case 9:{ //181-200HP
				return ChatColor.WHITE;
			}
			default:{ //201+HP
				return ChatColor.MAGIC;
			}
		}
	}
	
	public static void log(String logmessage, int loglv) {
		if (LOGGING_LEVEL>=loglv) {
			log_messages.add(ChatColor.stripColor(logmessage));
			switch (loglv) {
				case 0: {
					//Only game breaking messages appear in level 0.
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED+ChatColor.stripColor(logmessage));
				}break;
				case 1: {
					//Only warning messages appear in level 1.
					Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW+ChatColor.stripColor(logmessage));
				}break;
				case 2: {
					//Regular Gameplay information can appear here.
					Bukkit.getConsoleSender().sendMessage(logmessage);
				}break;
				case 3: {
					//Debug messages that generalize the events happening in the world.
					Bukkit.getConsoleSender().sendMessage(logmessage);
				}break;
				case 4: {
					//Debug messages that define the specifics of an event happening, including logic / number calculations.
					Bukkit.getConsoleSender().sendMessage(logmessage);
				}break;
				case 5: {
					//All messages possible. This may include messages that have very distinct purposes.
					Bukkit.getConsoleSender().sendMessage(logmessage);
				}break;
			}
		}
	}
	
	public static void updateServer() {
		if (Bukkit.getOnlinePlayers().size()!=0) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("TwosideKeeper"), new Runnable() {
				@Override
				public void run() {
					Bukkit.getScheduler().runTaskAsynchronously(Bukkit.getPluginManager().getPlugin("TwosideKeeper"), pluginupdater);
					DiscordMessageSender.sendItalicizedRawMessageDiscord("The server is restarting in 1 minute for a plugin update!");
					Bukkit.broadcastMessage(ChatColor.YELLOW+"The server is restarting in 1 minute for a plugin update!");
				}
			},20*120);
			Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("TwosideKeeper"), new Runnable() {
				@Override
				public void run() {
					Bukkit.getScheduler().runTaskAsynchronously(Bukkit.getPluginManager().getPlugin("TwosideKeeper"), pluginupdater);
					DiscordMessageSender.sendItalicizedRawMessageDiscord("The server is restarting in 10 seconds!");
					Bukkit.broadcastMessage(ChatColor.RED+"The server is restarting in 10 seconds!");
				}
			},20*170);
		}
		Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("TwosideKeeper"), new Runnable() {
			@Override
			public void run() {
				Bukkit.getScheduler().runTask(Bukkit.getPluginManager().getPlugin("TwosideKeeper"), pluginupdater);
				Bukkit.savePlayers();
				DiscordMessageSender.sendItalicizedRawMessageDiscord("Server is shutting down...");
				for (int i=0;i<Bukkit.getWorlds().size();i++) {
					Bukkit.getWorlds().get(i).save();
				}
				Bukkit.shutdown();
			}
		},20*180*((Bukkit.getOnlinePlayers().size()==0)?0:1)+1);
		
	}
	
	public void showPlayerStats(Player p) {
		showPlayerStats(p,p);
	}
	
	public void showPlayerStats(Player p, CommandSender receiver) {
		PlayerStructure pd = (PlayerStructure)playerdata.get(p.getUniqueId());
		double old_weapondmg = pd.prev_weapondmg;
		double old_buffdmg = pd.prev_buffdmg;
		double old_partydmg = pd.prev_partydmg;
		double old_armordef = pd.prev_armordef;
		double store1=NewCombat.CalculateDamageReduction(1,p,p);
		double store2=NewCombat.CalculateWeaponDamage(p,null,true);
		pd.damagedealt=store2;
		pd.damagereduction=store1;
		DecimalFormat df = new DecimalFormat("0.0");
		receiver.sendMessage("Habitat Quality: "+habitat_data.getHabitationLevel(p.getLocation()));
		receiver.sendMessage(ChatColor.GRAY+""+ChatColor.ITALIC+"Base Damage: "+ChatColor.RESET+""+ChatColor.DARK_PURPLE+df.format(store2));
		receiver.sendMessage(ChatColor.GRAY+""+ChatColor.ITALIC+"Damage Reduction: "+ChatColor.RESET+""+ChatColor.DARK_AQUA+df.format((1.0-store1)*100)+"%");
		receiver.sendMessage(ChatColor.GRAY+""+ChatColor.ITALIC+"Life Steal: "+ChatColor.RESET+""+ChatColor.DARK_AQUA+df.format(NewCombat.calculateLifeStealAmount(p)*100)+"%");
		receiver.sendMessage(ChatColor.GRAY+""+ChatColor.ITALIC+"Critical Strike Chance: "+ChatColor.RESET+""+ChatColor.DARK_AQUA+df.format((NewCombat.calculateCriticalStrikeChance(p.getEquipment().getItemInMainHand(), p))*100)+"%");
		receiver.sendMessage(ChatColor.GRAY+""+ChatColor.ITALIC+"Dodge Chance: "+ChatColor.RESET+""+ChatColor.DARK_AQUA+df.format((NewCombat.CalculateDodgeChance(p))*100)+"%");
		TextComponent f = new TextComponent(ChatColor.GRAY+""+ChatColor.ITALIC+"Current Mode: ");
		f.addExtra(GenericFunctions.PlayerModeName(p));
		if (receiver instanceof Player) {
			((Player)receiver).spigot().sendMessage(f);
		} else {
			receiver.sendMessage(f.toPlainText());
		}
		TextComponent msg = DisplayPerks(p.getEquipment().getItemInMainHand(),"Weapon",p,0);if (!msg.toPlainText().equalsIgnoreCase("")) {
			if (receiver instanceof Player) {((Player)receiver).spigot().sendMessage(msg);} else {receiver.sendMessage(msg.toPlainText());}};
		msg = DisplayPerks(p.getEquipment().getHelmet(),"Helmet",p,903);if (!msg.toPlainText().equalsIgnoreCase("")) {if (receiver instanceof Player) {((Player)receiver).spigot().sendMessage(msg);} else {receiver.sendMessage(msg.toPlainText());}};
		msg = DisplayPerks(p.getEquipment().getChestplate(),"Chestplate",p,902);if (!msg.toPlainText().equalsIgnoreCase("")) {if (receiver instanceof Player) {((Player)receiver).spigot().sendMessage(msg);} else {receiver.sendMessage(msg.toPlainText());}};
		msg = DisplayPerks(p.getEquipment().getLeggings(),"Legging",p,901);if (!msg.toPlainText().equalsIgnoreCase("")) {if (receiver instanceof Player) {((Player)receiver).spigot().sendMessage(msg);} else {receiver.sendMessage(msg.toPlainText());}};
		msg = DisplayPerks(p.getEquipment().getBoots(),"Boot",p,900);if (!msg.toPlainText().equalsIgnoreCase("")) {if (receiver instanceof Player) {((Player)receiver).spigot().sendMessage(msg);} else {receiver.sendMessage(msg.toPlainText());}};

		if (receiver instanceof Player) {
			((Player)receiver).sendMessage("----------");
		} else {
			receiver.sendMessage("----------");
		}
	}
	
	public static TextComponent DisplayPerks(ItemStack item,String type,Player p, int slot) {
		TextComponent tc = new TextComponent("");
		if (GenericFunctions.isArtifactEquip(item) &&
				(ArtifactAbility.getEnchantments(item).size()>0 || AwakenedArtifact.getAP(item)>0)) {
			//log("Getting perks...",2);
			HashMap enchants = ArtifactAbility.getEnchantments(item);
			tc.addExtra("");
			tc.addExtra(ChatColor.GRAY+""+ChatColor.ITALIC+type+" Perks: ");
			if (AwakenedArtifact.getAP(item)>0) {
				TextComponent tc1 = new TextComponent(ChatColor.GREEN+"["+Character.toString((char)0x25b2)+"]");
				tc1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ComponentBuilder("Click to upgrade abilities on this artifact. "+ChatColor.GREEN+"Available AP: "+ChatColor.BLUE+AwakenedArtifact.getAP(item)).create()));
				tc1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/awakenedartifact menu "+slot));
				tc.addExtra(tc1);
			}
			
			tc.addExtra("\n");
			int j=0;
			for (int i=0;i<enchants.size();i++) {
				//log("Getting perks...",2);
				ArtifactAbility ab = (ArtifactAbility)enchants.keySet().toArray()[i];
				//p.sendMessage(ChatColor.BLUE+ab.GetName()+" "+(int)enchants.values().toArray()[i]);
				TextComponent tc1 = new TextComponent(ChatColor.GREEN+"["+ab.GetName()+" "+(int)enchants.values().toArray()[i]+"] ");
				tc1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ComponentBuilder(WordUtils.wrap(ArtifactAbility.displayDescription(ab, item.getEnchantmentLevel(Enchantment.LUCK), (int)enchants.values().toArray()[i], NewCombat.CalculateWeaponDamage(p,null)),ArtifactAbility.LINE_SIZE,"\n",true)).create()));
				j++;
				if (j>=4 && i!=enchants.size()-1) {
					tc1.addExtra("\n");
					j=0;
				}
				tc.addExtra(tc1);
			}
		}
		return tc;
	}
	
	/**
	 *  Old Code. We now use GenericFunctions.DealDamageToMob().
	 */
	@Deprecated
	public static void DealDamageToMob(ItemStack weapon, LivingEntity damager, LivingEntity target) {
		GenericFunctions.DealDamageToMob(NewCombat.CalculateWeaponDamage(damager,target), target, damager);
	}
	
	public static ServerType getServerType() {
		return SERVER_TYPE;
	}
	
	public static String drawVelocityBar(double vel,double additionaldmg) {
		DecimalFormat df = new DecimalFormat("0.00");
		StringBuilder finalstring = new StringBuilder(ChatColor.BLUE+"Velocity - |||||||||||||||||||| "+(((vel*93.182445)>4.317)?ChatColor.BLUE:ChatColor.RED)+df.format(vel*93.182445)+"m/sec "+ChatColor.YELLOW+"(+"+df.format(additionaldmg*(vel*93.182445))+" dmg)");
		finalstring.insert(((vel*93.182445)<20)?(int)((vel*93.182445)+12):31, ChatColor.GRAY+"");
		return finalstring.toString();
	}
	
	public static void announcePluginVersions() {
		if (SERVER_TYPE!=ServerType.QUIET) {
    		DiscordMessageSender.sendItalicizedRawMessageDiscord(SERVER_TYPE.GetServerName()+"Server has been restarted.\nRunning v."+Bukkit.getPluginManager().getPlugin("TwosideKeeper").getDescription().getVersion()+" of TwosideKeeper\nRunning v"+Bukkit.getPluginManager().getPlugin("aPlugin").getDescription().getVersion()+" of Jobs.");
    	}
	}
	
	public static void breakdownItem(ItemStack item, Player p) {
		p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
		p.sendMessage(ChatColor.DARK_RED+"Your "+ChatColor.YELLOW+((item.hasItemMeta() && item.getItemMeta().hasDisplayName())?item.getItemMeta().getDisplayName():GenericFunctions.UserFriendlyMaterialName(item))+ChatColor.DARK_RED+" has broken!");
	}

	public static double CalculateDamageReduction(double dmg_amt, LivingEntity target, LivingEntity damager) {
		return NewCombat.CalculateDamageReduction(dmg_amt, target, damager);
	}
}