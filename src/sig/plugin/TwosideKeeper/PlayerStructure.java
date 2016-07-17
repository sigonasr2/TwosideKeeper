package sig.plugin.TwosideKeeper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

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
	
	public double prev_weapondmg=0.0;
	public double prev_buffdmg=0.0;
	public double prev_partydmg=0.0;
	public double prev_armordef=0.0;
	
	public int debuffcount=0;
	
	//Needs the instance of the player object to get all other info. Only to be called at the beginning.
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
			this.spleef_pts=0;
			this.spleef_wins=0;
			this.title_task=-1;
			this.sounds_enabled=true;
			this.debuffcount=0;
			this.last_deathmark=TwosideKeeper.getServerTickTime();
			this.last_shovelspell=TwosideKeeper.getServerTickTime()+300;
			this.swordcombo=0;
			this.last_swordhit=TwosideKeeper.getServerTickTime();
			this.highwinder=false;
			this.highwinderdmg=0.0;
			this.nextarrowxp=0;
			this.hasfullrangerset=false;
			this.last_strikerspell=TwosideKeeper.getServerTickTime();
			//Set defaults first, in case this is a new user.
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
		workable.addDefault("sounds_enabled", sounds_enabled);
		
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
		
		try {
			workable.save(config);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
