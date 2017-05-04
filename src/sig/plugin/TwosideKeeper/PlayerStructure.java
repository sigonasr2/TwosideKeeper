package sig.plugin.TwosideKeeper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.potion.PotionEffect;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import sig.plugin.TwosideKeeper.HelperStructures.BowMode;
import sig.plugin.TwosideKeeper.HelperStructures.DeathStructure;
import sig.plugin.TwosideKeeper.HelperStructures.FilterCubeItem;
import sig.plugin.TwosideKeeper.HelperStructures.PlayerMode;
import sig.plugin.TwosideKeeper.HelperStructures.ServerType;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;
import sig.plugin.TwosideKeeper.Logging.DamageLogger;

//import com.google.common.graph.*;

/*PLAYER STRUCTURE
 * 
 * Keeps external data and info about the player
 * and provides a format that can save this data
 * to a file when it's time. Updates the visual
 * healthbar on-screen and such.
 * 
 */
public class PlayerStructure {
	public String name;
	//public String displayName;
	public long joined; //When the player logged in today. Can track how long played, etc.
	public long firstjoined; //The first server tick the player joined the game.
	
	public double money; //Money stored on the player.
	public double bank_money; //Money in the bank.
	
	public boolean opened_inventory;
	public boolean isViewingItemCube=false;
	public boolean shootingArrowBarrage=false;
	
	public int saturation; //We will now track saturation manually to remove health healing from saturation.
	public long last_regen_time; //Last time a health regen took place.
	public double damagereduction;
	public double damagedealt;
	public int insertItem=-1; //The position in the chat message to insert an item to.
	//public double basedmg;
	public LivingEntity target; //The current entity this player is looking at. 
	public boolean enderdragon_spawned; //Whether or not an Ender Dragon has been spawned for this player. 
	public int partybonus; //Holds how many players are in the party, to account for party bonuses.
	public int currentparty; //Which party we are in.
	public int spleef_pts;
	public int spleef_wins;
	public int title_task; //Keeps track of the task last identified for updating titles.
	public int pickeditems=-1;
	public boolean sounds_enabled=true;
	public double velocity;
	public long last_deathmark=TwosideKeeper.getServerTickTime();
	public long last_shovelspell=TwosideKeeper.getServerTickTime()+300;
	public int swordcombo=0;
	public long last_swordhit=TwosideKeeper.getServerTickTime();
	public long last_strikerspell=TwosideKeeper.getServerTickTime();
	public boolean highwinder=false;
	public double highwinderdmg=0.0;
	public int nextarrowxp = 0; //How much bonus XP to give to an Artifact Bow.
	public boolean hasfullrangerset=false;
	public double lastarrowpower=0;
	public boolean lastarrowwasinrangermode=false; //true is ranger mode.
	public int headshotcombo=0;
	public List<ItemCubeWindow> openeditemcube;
	public boolean openinginventory=false;
	public boolean fulldodge=false;
	public long last_arrowbarrage=TwosideKeeper.getServerTickTime();
	public long last_laugh_time=TwosideKeeper.getServerTickTime();
	public long last_rejuvenate=TwosideKeeper.getServerTickTime();
	public DamageLogger damagedata;
	public boolean damagelogging=false;
	public boolean hasDied=false;
	public double deathloc_x = 0;
	public double deathloc_y = 0;
	public double deathloc_z = 0;
	public String deathloc_world = "";
	public List<ItemStack> deathloot = new ArrayList<ItemStack>();
	public double vendetta_amt = 0.0;
	public HashMap<UUID,Long> hitlist = new HashMap<UUID,Long>();
	public long lastdeath = 0;
	public int previousparty = -1;
	public long lastblock = 0;
	public List<Integer> itemcubelist = new ArrayList<Integer>();
	public int lasthitproperties=0;
	public String lasthitdesc="";
	public double lastdamagetaken=0;
	public double lastrawdamage=0;
	public long lastmodeupdate=0;
	public long lastsprintcheck=0;
	public int swiftaegisamt=0;
	public PlayerMode lastmode=PlayerMode.NORMAL;
	public List<PotionEffect> lasteffectlist=null;
	public boolean stealthmode=false;
	public long lastcompassnotification=0;
	public boolean endnotification=false;
	public long turnedonsneak=0;
	public double slayermodehp=0;
	public long lastassassinatetime=0;
	public long lastlifesavertime=0;
	public boolean slayermegahit=false;
	public double thorns_amt = 0.0;
	public long lastimportantactionbarmsg=0;
	public long lasthighwinderhit=0;
	public int lifestealstacks=0;
	public int weaponcharges=0;
	public double damagepool=0;
	public long lastattacked=0;
	public int lasthitfromdamagepool=0;
	public long lastvendettastack=0;
	public long lastlavaplume=0;
	public long usetimer=0;
	public boolean weatherwatch=false;
	public String weatherwatch_user="";
	public boolean falldamageimmunity=false;
	public double pctbonusregen = 0.0;
	public long pctbonusregentime = 0;
	public long lastlightningstrike = 0;
	public Player linkplayer = null;
	public long lastlinkteleport = 0;
	public int lastxsign = 0;
	public int lastzsign = 0;
	public long lastabsorptionhealthgiven = TwosideKeeper.getServerTickTime();
	public long ignoretargetarmor = TwosideKeeper.getServerTickTime();
	public long lastcandyconsumed = TwosideKeeper.getServerTickTime();
	public long lastrevivecandyconsumed = TwosideKeeper.getServerTickTime();
	public long icewandused = TwosideKeeper.getServerTickTime();
	public PlayerMode playermode_on_death=PlayerMode.NORMAL;
	public long lastusedearthwave = TwosideKeeper.getServerTickTime();
	public long lastusedwindslash = TwosideKeeper.getServerTickTime();
	public long lastusedbeastwithin = TwosideKeeper.getServerTickTime();
	
	public long iframetime = 0;
	
	public double prev_weapondmg=0.0;
	public double prev_buffdmg=0.0;
	public double prev_partydmg=0.0;
	public double prev_armordef=0.0;
	
	public int debuffcount=0;
	public boolean isViewingInventory=false;
	public boolean destroyedminecart=false;
	public boolean headshot=false;
	public boolean preemptive=false;
	public boolean crit=false;
	public int storedbowxp=0;
	public long lasthittarget=0;
	public long lastbowmodeswitch=0;
	public long lastsneak=0;
	public long lastcombat=0;
	public long lastsantabox=0;
	public BowMode rangermode=BowMode.CLOSE;
	
	public boolean isPlayingSpleef=false;
	
	public long lastrightclick = 0;
	public boolean opened_another_cube=false;
	public long damagepooltime=0;
	public long last_siphon=0;
	public long last_dodge=0;
	public long last_mock=0;
	public long rage_time=0; //Set this to the last tick that rage is supposed to last. It'll wear off after this.
	public int rage_amt=0;
	public long swiftaegistime=0;
	public String lastActionBarMessage="";
	public Location lastStandingLoc = null;
	
	public boolean holidaychest1=false;
	public boolean holidaychest2=false;
	public boolean holidaychest3=false;
	public boolean holidaychest4=false;
	//public MutableGraph<Integer> graph = GraphBuilder.undirected().build();
	public HashMap<Material,Block> blockscanlist=new HashMap<Material,Block>();
	public long lastusedrocketbooster=0;
	public long lastActionBarMessageTime=0;
	public long lastsantabox2;
	public double regenpool=0;
	public boolean vacuumsuckup=true;
	public boolean equipweapons=true;
	public boolean equiparmor=true;
	public long lastpotionparticles=0;
	public Location restartLoc = null; //Set to a value when the player has to be re-teleported after being controlled by a camera.
	public long lastPoisonTick=0;
	
	List<ItemStack> equipmentset = new ArrayList<ItemStack>();
	
	public HashMap<Material,List<Integer>> filtercubestructure = new HashMap<Material, List<Integer>>();
	public List<UUID> ignoreItemsList = new ArrayList<UUID>();
	public HashMap<String,Buff> buffs = new HashMap<String,Buff>();
	public HashMap<String,HashMap<Integer,Integer>> itemsets = new HashMap<String,HashMap<Integer,Integer>>(); //HashMap<"Set Name",HashMap<"Tier","Amt">>
	
	//Needs the instance of the player object to get all other info. Only to be called at the beginning.
	@SuppressWarnings("deprecation")
	public PlayerStructure(Player p, long serverTickTime) {
		if (p!=null && p.isOnline()) {
			this.velocity = 0d;
			this.name = p.getName();
			this.joined = serverTickTime;
			this.firstjoined = serverTickTime;
			this.money=100;
			this.bank_money=0;
			this.opened_inventory=false;
			this.saturation=20;
			this.last_regen_time=TwosideKeeper.SERVERTICK;
			this.target=null;
			this.damagereduction=1.0;
			this.damagedealt=1.0;
			//this.basedmg=0.0;
			this.partybonus=0;
			this.enderdragon_spawned=false;
			this.currentparty=-1;
			this.previousparty=-1;
			this.spleef_pts=0;
			this.spleef_wins=0;
			this.title_task=-1;
			this.sounds_enabled=true;
			this.debuffcount=0;
			this.lasteffectlist = new ArrayList<PotionEffect>(); 
			this.lasteffectlist.addAll(p.getActivePotionEffects());
			this.last_deathmark=(TwosideKeeper.getServerType()==ServerType.MAIN)?TwosideKeeper.getServerTickTime():0;
			this.last_shovelspell=(TwosideKeeper.getServerType()==ServerType.MAIN)?TwosideKeeper.getServerTickTime()+300:0;
			this.swordcombo=0;
			this.last_swordhit=(TwosideKeeper.getServerType()==ServerType.MAIN)?TwosideKeeper.getServerTickTime():0;
			this.highwinder=false;
			this.highwinderdmg=0.0;
			this.nextarrowxp=0;
			this.hasfullrangerset=false;
			this.last_strikerspell=(TwosideKeeper.getServerType()==ServerType.MAIN)?TwosideKeeper.getServerTickTime():0;
			this.openeditemcube = new ArrayList<ItemCubeWindow>();
			this.openinginventory = false;
			this.fulldodge=false;
			this.last_arrowbarrage=(TwosideKeeper.getServerType()==ServerType.MAIN)?TwosideKeeper.getServerTickTime():0;
			this.lastarrowwasinrangermode=false;
			this.isViewingInventory=false;
			this.destroyedminecart=false;
			this.last_laugh_time=(TwosideKeeper.getServerType()==ServerType.MAIN)?TwosideKeeper.getServerTickTime():0;
			this.last_rejuvenate=(TwosideKeeper.getServerType()==ServerType.MAIN)?TwosideKeeper.getServerTickTime():0;
			this.lastassassinatetime=(TwosideKeeper.getServerType()==ServerType.MAIN)?TwosideKeeper.getServerTickTime():0;
			this.lastlifesavertime=(TwosideKeeper.getServerType()==ServerType.MAIN)?TwosideKeeper.getServerTickTime():0;
			this.lastusedwindslash=(TwosideKeeper.getServerType()==ServerType.MAIN)?TwosideKeeper.getServerTickTime():0;
			this.icewandused=(TwosideKeeper.getServerType()==ServerType.MAIN)?TwosideKeeper.getServerTickTime():0;
			this.lastusedbeastwithin=(TwosideKeeper.getServerType()==ServerType.MAIN)?TwosideKeeper.getServerTickTime():0;
			this.damagedata = new DamageLogger(p);
			this.damagelogging=false;
			this.isPlayingSpleef=false;
			this.iframetime=TwosideKeeper.getServerTickTime();
			this.vacuumsuckup=true;
			this.equipweapons=true;
			this.equiparmor=true;
			//Set defaults first, in case this is a new user.
			loadConfig();
						p.getInventory().addItem(new ItemStack(Material.PORTAL));
			
			//Check if new player.
			if (this.firstjoined == serverTickTime) {
				//This is a new player! Let the whole world know!
				//Give the player free tools and items.
				Bukkit.getServer().broadcastMessage(ChatColor.GOLD+"Welcome to new player "+ChatColor.WHITE+""+this.name+"!");
				if (TwosideKeeper.SERVER_TYPE==ServerType.MAIN) {
					aPlugin.API.discordSendRaw("Welcome to new player **"+this.name+"**!");
				}
				p.sendMessage(ChatColor.GREEN+"Welcome to the server! Thanks for joining us.");
				p.sendMessage(ChatColor.GOLD+"  Here's a manual to get you started!");
				
				//Give starter pack.
				/*p.getInventory().addItem(new ItemStack(Material.STONE_SWORD,1));
				p.getInventory().addItem(new ItemStack(Material.STONE_PICKAXE,1));
				p.getInventory().addItem(new ItemStack(Material.STONE_AXE,1));
				p.getInventory().addItem(new ItemStack(Material.STONE_SPADE,1));
				p.getInventory().addItem(new ItemStack(Material.MINECART,1));
				p.getInventory().addItem(new ItemStack(Material.LEATHER_CHESTPLATE,1));
				p.getInventory().addItem(new ItemStack(Material.LEATHER_LEGGINGS,1));
				p.getInventory().addItem(new ItemStack(Material.TORCH,8));
				p.getInventory().addItem(new ItemStack(Material.BREAD,16));*/
				ItemStack manual = new ItemStack(Material.WRITTEN_BOOK);
				BookMeta bm = (BookMeta)manual.getItemMeta();
				bm.setAuthor("Sig's Minecraft");
				//bm.setPage(arg0, arg1);
				
				p.getInventory().addItem(manual);
				
				//Make sure it's not already there...?
				if (Bukkit.getServer().getScoreboardManager().getMainScoreboard().getTeam(this.name.toLowerCase())==null) {
					Bukkit.getServer().getScoreboardManager().getMainScoreboard().registerNewTeam(this.name.toLowerCase()).addPlayer(p);
				}
			}
			
			Bukkit.getScheduler().runTaskLater(TwosideKeeper.plugin, ()->{
				if (this.restartLoc!=null) {
					p.teleport(this.restartLoc);
					this.restartLoc=null;
				}
			}, 5);
			
			//Joined always gets set to new time.
			this.joined = serverTickTime;
		}
	}
	
	public static void setDefaultCooldowns(Player p) {
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		aPlugin.API.sendCooldownPacket(p, Material.BOW, GenericFunctions.GetRemainingCooldownTime(p, pd.last_dodge, TwosideKeeper.DODGE_COOLDOWN));
		applyCooldownToAllTypes(p,"HOE",GenericFunctions.GetRemainingCooldownTime(p, pd.last_deathmark, TwosideKeeper.DEATHMARK_COOLDOWN));
		applyCooldownToAllTypes(p,"SPADE",GenericFunctions.GetRemainingCooldownTime(p, pd.lastusedearthwave, TwosideKeeper.EARTHWAVE_COOLDOWN));
		applyCooldownToAllTypes(p,"SWORD",GenericFunctions.GetRemainingCooldownTime(p, pd.last_strikerspell, TwosideKeeper.LINEDRIVE_COOLDOWN));
		aPlugin.API.sendCooldownPacket(p, Material.SHIELD, GenericFunctions.GetRemainingCooldownTime(p, pd.last_rejuvenate, TwosideKeeper.REJUVENATE_COOLDOWN));
		aPlugin.API.sendCooldownPacket(p, Material.SKULL_ITEM, GenericFunctions.GetRemainingCooldownTime(p, pd.lastlifesavertime, TwosideKeeper.LIFESAVER_COOLDOWN));
		aPlugin.API.sendCooldownPacket(p, Material.CHORUS_FLOWER, GenericFunctions.GetRemainingCooldownTime(p, pd.lastlifesavertime, TwosideKeeper.LIFESAVER_COOLDOWN));
		aPlugin.API.sendCooldownPacket(p, Material.WATCH, GenericFunctions.GetRemainingCooldownTime(p, pd.icewandused, TwosideKeeper.ICEWAND_COOLDOWN));
		aPlugin.API.sendCooldownPacket(p, Material.RAW_FISH, GenericFunctions.GetRemainingCooldownTime(p, pd.lastcandyconsumed, 40));
		aPlugin.API.sendCooldownPacket(p, Material.GOLDEN_APPLE, GenericFunctions.GetRemainingCooldownTime(p, pd.lastrevivecandyconsumed, 200));
		applyCooldownToAllTypes(p,"SWORD",GenericFunctions.GetRemainingCooldownTime(p, pd.lastusedwindslash, TwosideKeeper.WINDSLASH_COOLDOWN));
		applyCooldownToAllTypes(p,"SWORD",GenericFunctions.GetRemainingCooldownTime(p, pd.lastusedbeastwithin, TwosideKeeper.BEASTWITHIN_COOLDOWN));
	}

	private static void applyCooldownToAllTypes(Player p, String item, int cooldown) {
		aPlugin.API.sendCooldownPacket(p, Material.valueOf("WOOD_"+item), cooldown);
		aPlugin.API.sendCooldownPacket(p, Material.valueOf("IRON_"+item), cooldown);
		aPlugin.API.sendCooldownPacket(p, Material.valueOf("STONE_"+item), cooldown);
		aPlugin.API.sendCooldownPacket(p, Material.valueOf("DIAMOND_"+item), cooldown);
		aPlugin.API.sendCooldownPacket(p, Material.valueOf("GOLD_"+item), cooldown);
	}

	//Save the configuration.
	public void saveConfig() {
		File config;
		config = new File(TwosideKeeper.filesave,"users/"+Bukkit.getPlayer(name).getUniqueId()+".data");
		FileConfiguration workable = YamlConfiguration.loadConfiguration(config);
		
		workable.set("name", name);
		//workable.set("displayName", displayName);
		workable.set("joined", joined);
		workable.set("firstjoined", firstjoined);
		workable.set("money", money);
		workable.set("bank_money", bank_money);
		workable.set("saturation", saturation);
		workable.set("damagereduction", damagereduction);
		workable.set("enderdragon_spawned", enderdragon_spawned);
		workable.set("spleef_pts", spleef_pts);
		workable.set("spleef_wins", spleef_wins);
		workable.set("sounds_enabled", sounds_enabled);
		workable.set("hasDied", hasDied);
		workable.set("lifestealstacks", lifestealstacks);
		workable.set("weaponcharges", weaponcharges);
		workable.set("damagepool", damagepool);
		workable.set("vendetta_amt", vendetta_amt);
		workable.set("weatherwatch", weatherwatch);
		workable.set("weatherwatch_user", weatherwatch_user);
		workable.set("holidaychest1", holidaychest1);
		workable.set("holidaychest2", holidaychest2);
		workable.set("holidaychest3", holidaychest3);
		workable.set("holidaychest4", holidaychest4);
		workable.set("lastsantabox2", lastsantabox2);
		workable.set("vacuumsuckup", vacuumsuckup);
		workable.set("equipweapons", equipweapons);
		workable.set("equiparmor", equiparmor);
		workable.set("playermode_on_death", playermode_on_death.name());
		//ConfigurationSection deathlootlist = workable.createSection("deathloot");
		if (DeathManager.deathStructureExists(Bukkit.getPlayer(name))) {
			DeathStructure ds = DeathManager.getDeathStructure(Bukkit.getPlayer(name));
			deathloc_x = ds.deathloc.getX();
			deathloc_y = ds.deathloc.getY();
			deathloc_z = ds.deathloc.getZ();
			deathloc_world = ds.deathloc.getWorld().getName();
 			/*for (int i=0;i<ds.deathinventory.size();i++) {
 				if (ds.deathinventory.get(i)!=null) {
 					deathlootlist.set("item"+i, ds.deathinventory.get(i));
 				}
 			}*/
		}
		workable.set("deathloc_x", deathloc_x);
		workable.set("deathloc_y", deathloc_y);
		workable.set("deathloc_z", deathloc_z);
		workable.set("deathloc_world", deathloc_world);
		workable.set("COOLDOWN_deathmark", last_deathmark);
		workable.set("COOLDOWN_shovelspell", last_shovelspell);
		workable.set("COOLDOWN_strikerspell", last_strikerspell);
		workable.set("COOLDOWN_usedearthwave", lastusedearthwave);
		workable.set("COOLDOWN_arrowbarrage", last_arrowbarrage);
		workable.set("COOLDOWN_laughtime", last_laugh_time);
		workable.set("COOLDOWN_rejuvenate", last_rejuvenate);
		workable.set("COOLDOWN_swordhit", last_swordhit);
		workable.set("COOLDOWN_strikerspell", last_strikerspell);
		workable.set("COOLDOWN_absorptionhealthgiven", lastabsorptionhealthgiven);
		workable.set("COOLDOWN_ignoretargetarmor", ignoretargetarmor);
		workable.set("COOLDOWN_lastrevivecandyconsumed", lastrevivecandyconsumed);
		workable.set("COOLDOWN_lastcandyconsumed", lastcandyconsumed);
		workable.set("COOLDOWN_icewandused", icewandused);
		workable.set("COOLDOWN_lastdodge", last_dodge);
		workable.set("COOLDOWN_lastsiphon", last_siphon);
		workable.set("COOLDOWN_lastmock", last_mock);
		workable.set("COOLDOWN_lastassassinatetime", lastassassinatetime);
		workable.set("COOLDOWN_lastlifesavertime", lastlifesavertime);
		workable.set("COOLDOWN_lastusedwindslash", lastusedwindslash);
		workable.set("COOLDOWN_lastusedbeastwithin", lastusedbeastwithin);
		int buffcounter=0;
		for (String key : buffs.keySet()) {
			Buff b = buffs.get(key);
			SaveBuff(workable, buffcounter, key, b);
			buffcounter++;
		}
		workable.set("BUFFCOUNT", buffcounter);
		if (restartLoc!=null) {
			workable.set("restartloc_x", restartLoc.getX());
			workable.set("restartloc_y", restartLoc.getY());
			workable.set("restartloc_z", restartLoc.getZ());
			workable.set("restartloc_world", restartLoc.getWorld().getName());
		} else {
			workable.set("restartloc_world", "null");
		}
		
		try {
			workable.save(config);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void SaveBuff(FileConfiguration workable, int buffcounter, String key, Buff b) {
		workable.set("BUFF"+(buffcounter)+"_key", key);
		workable.set("BUFF"+(buffcounter)+"_name", b.getDisplayName());
		workable.set("BUFF"+(buffcounter)+"_duration", b.getRemainingBuffTime());
		workable.set("BUFF"+(buffcounter)+"_amplifier", b.getAmplifier());
		workable.set("BUFF"+(buffcounter)+"_color", b.getBuffParticleColor().asRGB());
		workable.set("BUFF"+(buffcounter)+"_icon", b.getBuffIcon());
		workable.set("BUFF"+(buffcounter)+"_isGoodBuff", b.isGoodBuff());
	}

	//Create a config for the player.
	public void loadConfig(){
		Player p = Bukkit.getPlayer(name);
		File config,testconfig;
		testconfig = new File(TwosideKeeper.filesave,"users/"+name+".data");
		config = new File(TwosideKeeper.filesave,"users/"+p.getUniqueId()+".data");
		if (testconfig.exists()) {
			TwosideKeeper.log("Renaming old config for player "+ChatColor.YELLOW+name+ChatColor.RESET+" to UUID "+ChatColor.YELLOW+p.getUniqueId(), 1);
			testconfig.renameTo(config);
		}
		FileConfiguration workable = YamlConfiguration.loadConfiguration(config);
		
		//Add all our default settings here.
		workable.addDefault("name", name);
		//workable.addDefault("displayName", displayName);
		workable.addDefault("joined", joined);
		workable.addDefault("firstjoined", firstjoined);
		workable.addDefault("money", money);
		workable.addDefault("bank_money", bank_money);
		workable.addDefault("saturation", saturation);
		workable.addDefault("damagereduction", damagereduction);
		workable.addDefault("enderdragon_spawned", enderdragon_spawned);
		workable.addDefault("spleef_pts", spleef_pts);
		workable.addDefault("spleef_wins", spleef_wins);
		workable.addDefault("hasDied", hasDied);
		workable.addDefault("damagepool", damagepool);
		workable.addDefault("weaponcharges", weaponcharges);
		workable.addDefault("lifestealstacks", lifestealstacks);
		workable.addDefault("vendetta_amt", vendetta_amt);
		workable.addDefault("lastvendettastack", lastvendettastack);
		workable.addDefault("weatherwatch", weatherwatch);
		workable.addDefault("weatherwatch_user", weatherwatch_user);
		workable.addDefault("holidaychest1", holidaychest1);
		workable.addDefault("holidaychest2", holidaychest2);
		workable.addDefault("holidaychest3", holidaychest3);
		workable.addDefault("holidaychest4", holidaychest4);
		workable.addDefault("lastsantabox2", lastsantabox2);
		workable.addDefault("vacuumsuckup", vacuumsuckup);
		workable.addDefault("equipweapons", equipweapons);
		workable.addDefault("equiparmor", equiparmor);
		workable.addDefault("playermode_on_death", playermode_on_death.name());
		workable.addDefault("COOLDOWN_deathmark", last_deathmark);
		workable.addDefault("COOLDOWN_shovelspell", last_shovelspell);
		workable.addDefault("COOLDOWN_strikerspell", last_strikerspell);
		workable.addDefault("COOLDOWN_usedearthwave", lastusedearthwave);
		workable.addDefault("COOLDOWN_arrowbarrage", last_arrowbarrage);
		workable.addDefault("COOLDOWN_laughtime", last_laugh_time);
		workable.addDefault("COOLDOWN_rejuvenate", last_rejuvenate);
		workable.addDefault("COOLDOWN_swordhit", last_swordhit);
		workable.addDefault("COOLDOWN_strikerspell", last_strikerspell);
		workable.addDefault("COOLDOWN_absorptionhealthgiven", lastabsorptionhealthgiven);
		workable.addDefault("COOLDOWN_ignoretargetarmor", ignoretargetarmor);
		workable.addDefault("COOLDOWN_lastrevivecandyconsumed", lastrevivecandyconsumed);
		workable.addDefault("COOLDOWN_lastcandyconsumed", lastcandyconsumed);
		workable.addDefault("COOLDOWN_icewandused", icewandused);
		workable.addDefault("COOLDOWN_lastdodge", last_dodge);
		workable.addDefault("COOLDOWN_lastsiphon", last_siphon);
		workable.addDefault("COOLDOWN_lastmock", last_mock);
		workable.addDefault("COOLDOWN_lastassassinatetime", lastassassinatetime);
		workable.addDefault("COOLDOWN_lastlifesavertime", lastlifesavertime);
		workable.addDefault("COOLDOWN_lastusedwindslash", lastusedwindslash);
		workable.addDefault("COOLDOWN_lastusedbeastwithin", lastusedbeastwithin);
		workable.addDefault("BUFFCOUNT", 0);
		
		workable.options().copyDefaults();
		
		//Set all variables.
		
		//this.name = workable.getString("name");
		//this.displayName = workable.getString("displayName");
		this.joined = workable.getLong("joined");
		this.firstjoined = workable.getLong("firstjoined");
		this.money = workable.getDouble("money");
		this.bank_money = workable.getDouble("bank_money");
		this.saturation = workable.getInt("saturation");
		this.damagereduction = workable.getDouble("damagereduction");
		this.enderdragon_spawned = workable.getBoolean("enderdragon_spawned");
		this.spleef_pts = workable.getInt("spleef_pts");
		this.spleef_wins = workable.getInt("spleef_wins");
		this.sounds_enabled = workable.getBoolean("sounds_enabled");
		this.hasDied = workable.getBoolean("hasDied");
		this.deathloc_x = workable.getDouble("deathloc_x");
		this.deathloc_y = workable.getDouble("deathloc_y");
		this.deathloc_z = workable.getDouble("deathloc_z");
		this.deathloc_world = workable.getString("deathloc_world");
		this.damagepool = workable.getDouble("damagepool");
		this.lifestealstacks = workable.getInt("lifestealstacks");
		this.weaponcharges = workable.getInt("weaponcharges");
		this.lastattacked = TwosideKeeper.getServerTickTime();
		this.lastcombat = TwosideKeeper.getServerTickTime();
		this.weatherwatch = workable.getBoolean("weatherwatch");
		this.weatherwatch_user = workable.getString("weatherwatch_user");
		this.holidaychest1 = workable.getBoolean("holidaychest1");
		this.holidaychest2 = workable.getBoolean("holidaychest2");
		this.holidaychest3 = workable.getBoolean("holidaychest3");
		this.holidaychest4 = workable.getBoolean("holidaychest4");
		this.lastsantabox2 = workable.getLong("lastsantabox2");
		this.lastvendettastack = workable.getLong("lastvendettastack");
		this.playermode_on_death = PlayerMode.valueOf(workable.getString("playermode_on_death"));
		this.last_deathmark = workable.getLong("COOLDOWN_deathmark");
		this.last_shovelspell = workable.getLong("COOLDOWN_shovelspell");
		this.last_strikerspell = workable.getLong("COOLDOWN_strikerspell");
		this.lastusedearthwave = workable.getLong("COOLDOWN_usedearthwave");
		this.last_arrowbarrage = workable.getLong("COOLDOWN_arrowbarrage");
		this.last_laugh_time = workable.getLong("COOLDOWN_laughtime");
		this.last_rejuvenate = workable.getLong("COOLDOWN_rejuvenate");
		this.last_swordhit = workable.getLong("COOLDOWN_swordhit");
		this.last_strikerspell = workable.getLong("COOLDOWN_strikerspell");
		this.lastabsorptionhealthgiven = workable.getLong("COOLDOWN_absorptionhealthgiven");
		this.ignoretargetarmor = workable.getLong("COOLDOWN_ignoretargetarmor");
		this.lastrevivecandyconsumed = workable.getLong("COOLDOWN_lastrevivecandyconsumed");
		this.lastcandyconsumed = workable.getLong("COOLDOWN_lastcandyconsumed");
		this.icewandused = workable.getLong("COOLDOWN_icewandused");
		this.last_dodge = workable.getLong("COOLDOWN_lastdodge");
		this.last_siphon = workable.getLong("COOLDOWN_lastsiphon");
		this.last_mock = workable.getLong("COOLDOWN_lastmock");
		this.lastassassinatetime = workable.getLong("COOLDOWN_lastassassinatetime");
		this.lastlifesavertime = workable.getLong("COOLDOWN_lastlifesavertime");
		this.lastusedwindslash = workable.getLong("COOLDOWN_lastusedwindslash");
		this.lastusedbeastwithin = workable.getLong("COOLDOWN_lastusedbeastwithin");
		this.vacuumsuckup = workable.getBoolean("vacuumsuckup");
		this.equipweapons = workable.getBoolean("equipweapons");
		this.equiparmor = workable.getBoolean("equiparmor");
		String tempworld = workable.getString("restartloc_world");
		if (tempworld!=null && !tempworld.equalsIgnoreCase("null")) {
			this.restartLoc = new Location(Bukkit.getWorld(tempworld),workable.getDouble("restartloc_x"),workable.getDouble("restartloc_y"),workable.getDouble("restartloc_z"));
		}
		
		int buffcount = workable.getInt("BUFFCOUNT");
		for (int i=0;i<buffcount;i++) {
			/*Buff.addBuff(p, workable.getString("BUFF"+i+"_key"), new Buff(
					workable.getString("BUFF"+i+"_name"),
					workable.getLong("BUFF"+i+"_duration"),
					workable.getInt("BUFF"+i+"_amplifier"),
					Color.fromRGB(workable.getInt("BUFF"+i+"_color")),
					workable.getString("BUFF"+i+"_icon"),
					workable.getBoolean("BUFF"+i+"_isGoodBuff")
					));*/
			buffs.put(workable.getString("BUFF"+i+"_key"), new Buff(
					workable.getString("BUFF"+i+"_name"),
					workable.getLong("BUFF"+i+"_duration"),
					workable.getInt("BUFF"+i+"_amplifier"),
					Color.fromRGB(workable.getInt("BUFF"+i+"_color")),
					workable.getString("BUFF"+i+"_icon"),
					workable.getBoolean("BUFF"+i+"_isGoodBuff")
					));
		}
		
		if (this.hasDied) {
			List<ItemStack> deathlootlist = new ArrayList<ItemStack>();
			//ConfigurationSection deathlootsection = workable.getConfigurationSection("deathloot");
 			/*for (int i=0;i<deathlootsection.getKeys(false).size();i++) {
 				ItemStack item = deathlootsection.getItemStack((String)(deathlootsection.getKeys(false).toArray()[i]));
 				deathlootlist.add(item);
 			}*/
			DeathManager.addNewDeathStructure(deathlootlist, new Location(Bukkit.getWorld(this.deathloc_world),this.deathloc_x,this.deathloc_y,this.deathloc_z), Bukkit.getPlayer(name));
		}
		
		try {
			workable.save(config);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static PlayerStructure GetPlayerStructure(Player p) {
		if (TwosideKeeper.playerdata.containsKey(p.getUniqueId())) {
			return TwosideKeeper.playerdata.get(p.getUniqueId());
		} else {
			TwosideKeeper.log(ChatColor.DARK_RED+"[ERROR] Player Structure for player "+p.getName()+" was not initialized! Now creating one...",0);
			return TwosideKeeper.playerdata.put(p.getUniqueId(), new PlayerStructure(p,TwosideKeeper.getServerTickTime()));
		}
	}
	
	public static int getPlayerNegativeHash(Player p) {
		return Math.min(p.getUniqueId().hashCode(), -p.getUniqueId().hashCode());
	}
}
