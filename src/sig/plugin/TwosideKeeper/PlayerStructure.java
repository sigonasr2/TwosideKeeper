package sig.plugin.TwosideKeeper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import sig.plugin.TwosideKeeper.HelperStructures.DeathStructure;
import sig.plugin.TwosideKeeper.HelperStructures.PlayerMode;
import sig.plugin.TwosideKeeper.HelperStructures.ServerType;
import sig.plugin.TwosideKeeper.Logging.DamageLogger;

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
	public long last_dodge=TwosideKeeper.getServerTickTime();
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
	
	public boolean isPlayingSpleef=false;
	
	public long lastrightclick = 0;
	public boolean opened_another_cube=false;
	
	//Needs the instance of the player object to get all other info. Only to be called at the beginning.
	@SuppressWarnings("deprecation")
	public PlayerStructure(Player p, long serverTickTime) {
		if (p!=null) {
			this.velocity = 0d;
			this.name = p.getName();
			this.joined = serverTickTime;
			this.firstjoined=serverTickTime;
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
			this.last_dodge=(TwosideKeeper.getServerType()==ServerType.MAIN)?TwosideKeeper.getServerTickTime():0;
			this.lastarrowwasinrangermode=false;
			this.isViewingInventory=false;
			this.destroyedminecart=false;
			this.last_laugh_time=(TwosideKeeper.getServerType()==ServerType.MAIN)?TwosideKeeper.getServerTickTime():0;
			this.last_rejuvenate=(TwosideKeeper.getServerType()==ServerType.MAIN)?TwosideKeeper.getServerTickTime():0;
			this.damagedata = new DamageLogger(p);
			this.damagelogging=false;
			this.isPlayingSpleef=false;
			this.iframetime=TwosideKeeper.getServerTickTime();
			//Set defaults first, in case this is a new user.
			setDefaultCooldowns(p);
			loadConfig();
						p.getInventory().addItem(new ItemStack(Material.PORTAL));
			
			//Check if new player.
			if (this.firstjoined == serverTickTime) {
				//This is a new player! Let the whole world know!
				//Give the player free tools and items.
				Bukkit.getServer().broadcastMessage(ChatColor.GOLD+"Welcome to new player "+ChatColor.WHITE+""+this.name+"!");
				p.sendMessage(ChatColor.GREEN+"Welcome to the server! Thanks for joining us.");
				p.sendMessage(ChatColor.GOLD+"  Here's some tools to get you started.");
				
				//Give starter pack.
				p.getInventory().addItem(new ItemStack(Material.STONE_SWORD,1));
				p.getInventory().addItem(new ItemStack(Material.STONE_PICKAXE,1));
				p.getInventory().addItem(new ItemStack(Material.STONE_AXE,1));
				p.getInventory().addItem(new ItemStack(Material.STONE_SPADE,1));
				p.getInventory().addItem(new ItemStack(Material.MINECART,1));
				p.getInventory().addItem(new ItemStack(Material.LEATHER_CHESTPLATE,1));
				p.getInventory().addItem(new ItemStack(Material.LEATHER_LEGGINGS,1));
				p.getInventory().addItem(new ItemStack(Material.TORCH,8));
				p.getInventory().addItem(new ItemStack(Material.BREAD,16));
				
				//Make sure it's not already there...?
				if (Bukkit.getServer().getScoreboardManager().getMainScoreboard().getTeam(this.name.toLowerCase())==null) {
					Bukkit.getServer().getScoreboardManager().getMainScoreboard().registerNewTeam(this.name.toLowerCase()).addPlayer(p);
				}
			}
			
			//Joined always gets set to new time.
			this.joined = serverTickTime;
		}
	}
	
	private void setDefaultCooldowns(Player p) {
		aPlugin.API.sendCooldownPacket(p, Material.BOW, TwosideKeeper.DODGE_COOLDOWN);
		applyCooldownToAllTypes(p,"HOE",TwosideKeeper.DEATHMARK_COOLDOWN);
		applyCooldownToAllTypes(p,"SPADE",TwosideKeeper.EARTHWAVE_COOLDOWN);
		applyCooldownToAllTypes(p,"SWORD",TwosideKeeper.LINEDRIVE_COOLDOWN);
		aPlugin.API.sendCooldownPacket(p, Material.SHIELD, TwosideKeeper.REJUVENATE_COOLDOWN);
	}

	private void applyCooldownToAllTypes(Player p, String item, int cooldown) {
		aPlugin.API.sendCooldownPacket(p, Material.valueOf("WOOD_"+item), cooldown);
		aPlugin.API.sendCooldownPacket(p, Material.valueOf("IRON_"+item), cooldown);
		aPlugin.API.sendCooldownPacket(p, Material.valueOf("STONE_"+item), cooldown);
		aPlugin.API.sendCooldownPacket(p, Material.valueOf("DIAMOND_"+item), cooldown);
		aPlugin.API.sendCooldownPacket(p, Material.valueOf("GOLD_"+item), cooldown);
	}

	//Save the configuration.
	public void saveConfig() {
		File config;
		config = new File(TwosideKeeper.filesave,"users/"+name+".data");
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
		ConfigurationSection deathlootlist = workable.createSection("deathloot");
		if (DeathManager.deathStructureExists(Bukkit.getPlayer(name))) {
			DeathStructure ds = DeathManager.getDeathStructure(Bukkit.getPlayer(name));
			deathloc_x = ds.deathloc.getX();
			deathloc_y = ds.deathloc.getY();
			deathloc_z = ds.deathloc.getZ();
			deathloc_world = ds.deathloc.getWorld().getName();
 			for (int i=0;i<ds.deathinventory.size();i++) {
 				if (ds.deathinventory.get(i)!=null) {
 					deathlootlist.set("item"+i, ds.deathinventory.get(i));
 				}
 			}
		}
		workable.set("deathloc_x", deathloc_x);
		workable.set("deathloc_y", deathloc_y);
		workable.set("deathloc_z", deathloc_z);
		workable.set("deathloc_world", deathloc_world);
		
		try {
			workable.save(config);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//Create a config for the player.
	public void loadConfig(){
		File config;
		config = new File(TwosideKeeper.filesave,"users/"+name+".data");
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
		
		workable.options().copyDefaults();
		
		//Set all variables.
		
		this.name = workable.getString("name");
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
		
		if (this.hasDied) {
			List<ItemStack> deathlootlist = new ArrayList<ItemStack>();
			ConfigurationSection deathlootsection = workable.getConfigurationSection("deathloot");
 			for (int i=0;i<deathlootsection.getKeys(false).size();i++) {
 				ItemStack item = deathlootsection.getItemStack((String)(deathlootsection.getKeys(false).toArray()[i]));
 				deathlootlist.add(item);
 			}
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
}
