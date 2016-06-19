package sig.plugin.TwosideKeeper;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.ComplexLivingEntity;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.entity.EnderDragon.Phase;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerAchievementAwardedEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupArrowEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.metadata.Metadatable;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;

import aPlugin.DiscordMessageSender;
import net.minecraft.server.v1_9_R1.Vector3f;
import sig.plugin.TwosideKeeper.HelperStructures.ArtifactItem;
import sig.plugin.TwosideKeeper.HelperStructures.CubeType;
import sig.plugin.TwosideKeeper.HelperStructures.DeathStructure;
import sig.plugin.TwosideKeeper.HelperStructures.ItemRarity;
import sig.plugin.TwosideKeeper.HelperStructures.MalleableBaseQuest;
import sig.plugin.TwosideKeeper.HelperStructures.MonsterDifficulty;
import sig.plugin.TwosideKeeper.HelperStructures.MonsterType;
import sig.plugin.TwosideKeeper.HelperStructures.QuestStatus;
import sig.plugin.TwosideKeeper.HelperStructures.SessionState;
import sig.plugin.TwosideKeeper.HelperStructures.SpleefArena;
import sig.plugin.TwosideKeeper.HelperStructures.WorldShop;
import sig.plugin.TwosideKeeper.HelperStructures.WorldShopSession;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;


public class TwosideKeeper extends JavaPlugin implements Listener {

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
	public static double EXPLOSION_DMG_MULT=1.2f; //
	public static double HEADSHOT_ACC=1.0f; //How accurate headshots have to be. Lower values means more leniency on headshots. Higher values means more strict.
	public static double RARE_DROP_RATE=0.0052;
	public static int PARTY_CHUNK_SIZE=16; //The number of chunks each party spans.
	public double XP_CONVERSION_RATE=0.01; //How much money per exp point?
	public static int WORLD_SHOP_ID=0; //The shop ID number we are on.
	public static int LOGGING_LEVEL=0; //The logging level the server will output in for the console. 0 = No Debug Messages. Toggled with /log.
	public static File filesave;
	public static List<PlayerStructure> playerdata;	
	public static SpleefManager TwosideSpleefGames;
	public WorldShopManager TwosideShops;
	
	public int TeamCounter = 0; 
	public List<Party> PartyList = new ArrayList<Party>();
	public List<Integer> colors_used = new ArrayList<Integer>();
	
	public RecyclingCenter TwosideRecyclingCenter;
	
	//Bank timers and users.
	public String withdrawUser="";
	public long withdrawTime=0;
	public String depositUser="";
	public long depositTime=0;
	public String conversionUser="";
	public long conversionTime=0;
	public int sleepingPlayers=0;
	
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
		
		loadConfig();
		
		sig.plugin.TwosideKeeper.Recipes.Initialize_ItemCube_Recipes();
		sig.plugin.TwosideKeeper.Recipes.Initialize_ArrowQuiver_Recipe();
		sig.plugin.TwosideKeeper.Recipes.Initialize_BlockArmor_Recipes();
		sig.plugin.TwosideKeeper.Recipes.Initialize_ItemDeconstruction_Recipes();
		sig.plugin.TwosideKeeper.Recipes.Initialize_WoolRecolor_Recipes();
		sig.plugin.TwosideKeeper.Recipes.Initialize_SlabReconstruction_Recipes();
		
		filesave=getDataFolder(); //Store the location of where our data folder is.
		log("Data folder at "+filesave+".",3);
		
		STARTTIME=Bukkit.getWorld("world").getFullTime();
		LASTSERVERCHECK=getServerTickTime();
		
		TwosideRecyclingCenter = new RecyclingCenter();
		TwosideRecyclingCenter.loadConfig();
		log("Recycling Centers Loaded: "+TwosideRecyclingCenter.getNumberOfNodes(),3);
		
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
		playerdata = new ArrayList<PlayerStructure>();
		
		//Let's not assume there are no players online. Load their data.
    	for (int i=0;i<Bukkit.getOnlinePlayers().toArray().length;i++) {
        	playerdata.add(new PlayerStructure((Player)Bukkit.getOnlinePlayers().toArray()[i],getServerTickTime()));
    	}
		
		//This is the constant timing method.
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new  Runnable(){
			public void run(){
			  log("Server time passed: "+(Bukkit.getWorld("world").getFullTime()-STARTTIME)+". New Server Time: "+(Bukkit.getWorld("world").getFullTime()-STARTTIME+SERVERTICK),5);
				Bukkit.getWorld("world").setFullTime(Bukkit.getWorld("world").getFullTime()-10);
				//WORK IN PROGRESS: Lamp updating code TO GO HERE.
				
				//SAVE SERVER SETTINGS.
				if (getServerTickTime()-LASTSERVERCHECK>=SERVERCHECKERTICKS) { //15 MINUTES (DEFAULT)
					saveOurData();
					
					
					//Advertisement messages could go here.
					//MOTD: "Thanks for playing on Sig's Minecraft!\n*bCheck out http://z-gamers.net/mc for update info!\n*aReport any bugs you find at http://zgamers.domain.com/mc/"
					getMOTD();
					getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('*', MOTD));
					/*
					getServer().broadcastMessage("Thanks for playing on Sig's Minecraft!");
					getServer().broadcastMessage(ChatColor.AQUA+"Check out http://z-gamers.net/mc for update info!");
					getServer().broadcastMessage(" ");
					*/
					//End Advertisements.
					
					LASTSERVERCHECK=getServerTickTime();
				}
				
				//Check our deposit and withdraw terminals for lingering timers.
				if (!withdrawUser.equalsIgnoreCase("") && getServerTickTime()-withdrawTime>TERMINALTIME) {
					//This is occupied still when it's supposed to time out.
					Player p = Bukkit.getPlayer(withdrawUser);
					if (p!=null) {
						p.sendMessage(ChatColor.RED+"RAN OUT OF TIME! "+ChatColor.WHITE+"Cancelled out of Withdraw terminal.");
					}
					withdrawUser="";
				}
				if (!depositUser.equalsIgnoreCase("") && getServerTickTime()-depositTime>TERMINALTIME) {
					//This is occupied still when it's supposed to time out.
					Player p = Bukkit.getPlayer(depositUser);
					if (p!=null) {
						p.sendMessage(ChatColor.RED+"RAN OUT OF TIME! "+ChatColor.WHITE+"Cancelled out of Deposit terminal.");
					}
					depositUser="";
				}
				if (!conversionUser.equalsIgnoreCase("") && getServerTickTime()-conversionTime>TERMINALTIME) {
					//This is occupied still when it's supposed to time out.
					Player p = Bukkit.getPlayer(conversionUser);
					if (p!=null) {
						p.sendMessage(ChatColor.RED+"RAN OUT OF TIME! "+ChatColor.WHITE+"Cancelled out of Conversion terminal.");
					}
					conversionUser="";
				}
				
				if (Bukkit.getWorld("world").getTime()>=12000) {
					Collection<Player> players = (Collection<Player>) getServer().getOnlinePlayers();
					//Count the number of players sleeping. Compare to "sleepingplayers" count.
					log("[DEBUG] Time: "+Bukkit.getWorld("world").getTime()+" Full Time: "+Bukkit.getWorld("world").getFullTime() + " SERVERTICKTIME: "+getServerTickTime(),4);
					if (players.size()>=2) { //This functionality only makes sense when two or more players are on.
						int sleeping=0;
						for (int i=0;i<players.size();i++) {
							if (Iterables.get(players,i).isSleeping()) {
								Iterables.get(players, i).setHealth(Iterables.get(players, i).getMaxHealth());
								sleeping++;
							}
						}
						if (sleepingPlayers!=sleeping) {
							sleepingPlayers=sleeping;
							getServer().broadcastMessage(ChatColor.GOLD+""+sleepingPlayers+" Player"+(sleepingPlayers!=1?"s are":" is")+" in bed "+ChatColor.WHITE+"("+sleepingPlayers+"/"+(players.size()/2)+")");
						}
						if (sleepingPlayers>=players.size()/2) {
							//Make it the next day.
							getServer().broadcastMessage(ChatColor.GOLD+"Enough Players sleeping! It's now morning!");
							
							SERVERTICK=getServerTickTime();
							Bukkit.getWorld("world").setTime(0);
							STARTTIME=Bukkit.getWorld("world").getFullTime();
							LASTSERVERCHECK=getServerTickTime();
							//Make sure we keep SERVERTICK in check.
							sleepingPlayers=0;
						}
					}
				}
				
				//See if each player needs to regenerate their health.
				for (int i=0;i<playerdata.size();i++) {
					PlayerStructure pd = playerdata.get(i);
					Player p = Bukkit.getPlayer(pd.name);
					
					if (TwosideShops.PlayerHasPurchases(p)) {
						TwosideShops.PlayerSendPurchases(p);
					}
					
					if (TwosideShops.IsPlayerUsingTerminal(p) &&
							(TwosideShops.GetSession(p).GetSign().getBlock()==null || TwosideShops.GetSession(p).IsTimeExpired())) {
						p.sendMessage(ChatColor.RED+"Ran out of time! "+ChatColor.WHITE+"Shop session closed.");
						TwosideShops.RemoveSession(p);
					}
					
					if (pd.last_regen_time+HEALTH_REGENERATION_RATE<=getServerTickTime()) {
						pd.last_regen_time=getServerTickTime();
						//See if this player needs to be healed.
						if (p!=null &&
								!p.isDead() && //Um, don't heal them if they're dead...That's just weird.
								p.getHealth()<p.getMaxHealth() &&
								p.getFoodLevel()>=16) {
							p.setHealth((p.getHealth()+1+(p.getMaxHealth()*0.05)>p.getMaxHealth())?p.getMaxHealth():p.getHealth()+1+(p.getMaxHealth()*0.05));
						}
					}
					//See if this player is sleeping.
					if (p.isSleeping()) {
						p.setHealth(Bukkit.getPlayer(pd.name).getMaxHealth()); //Heals the player fully when sleeping.
					}
					//We need to see if this player's damage reduction has changed recently. If so, notify them.
					//Check damage reduction by sending an artifical "1" damage to the player.
					if (!p.isDead()) {setPlayerMaxHealth(p);}
					p.getScoreboard().getTeam(p.getName().toLowerCase()).setSuffix(createHealthbar(((p.getHealth())/p.getMaxHealth())*100,p));
					if (CalculateDamageReduction(1,p,p)!=pd.damagereduction) {
						pd.damagereduction=CalculateDamageReduction(1,p,p);
						p.sendMessage(ChatColor.GRAY+""+ChatColor.ITALIC+"Damage Reduction modified: "+ChatColor.RESET+""+ChatColor.DARK_AQUA+Math.round((1.0-pd.damagereduction)*100)+"%");
					}
					
					
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
						} /*else 
						if (PartyList.get(j).IsInParty(p) &&
							!PartyList.get(j).IsInSameRegion(p) &&
							PartyList.get(j).PlayerCountInParty()==1) {
							//We're the only one in the party...why not
							//just move it?
							inParty=true;
							PartyList.get(j).UpdateRegion(p.getLocation());
						}*/
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
			}}, 20l, 20l);
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
    	}
    	if (sender instanceof Player) {
			DecimalFormat df = new DecimalFormat("0.00");
	    	if (cmd.getName().equalsIgnoreCase("money")) {
	    		sender.sendMessage("You are currently holding "+ChatColor.GREEN+"$"+df.format(getPlayerMoney((Player)sender)));
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
    				} else {
    					sender.sendMessage("Wrong arguments!");
    				}
    			} else {
    				sender.sendMessage("Cannot enchant nothing!");
    			}
	    		return true;
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
    				} else {
    					sender.sendMessage("Wrong arguments!");
    				}
    			} else {
    				sender.sendMessage("Cannot harden nothing!");
    			}
	    		return true;
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
    				} else {
    					sender.sendMessage("Wrong arguments!");
    				}
    			} else {
    				sender.sendMessage("Cannot convert nothing!");
    			}
	    		return true;
	    	} else 
    		if (cmd.getName().equalsIgnoreCase("artifact")) {
    			Player p = (Player)sender;
				if (args.length==1) {
    				p.getInventory().addItem(Artifact.createArtifactItem(ArtifactItem.valueOf(args[0])));
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
    		} else 
        		if (cmd.getName().equalsIgnoreCase("sound")) {
        			Player p = (Player)sender;
    				for (int i=0;i<playerdata.size();i++) {
    					if (playerdata.get(i).name.equalsIgnoreCase(p.getName())) {
    						playerdata.get(i).sounds_enabled=!playerdata.get(i).sounds_enabled;
    						if (playerdata.get(i).sounds_enabled) {
    							p.sendMessage(ChatColor.DARK_AQUA+"Chat sounds are now enabled.");
    						} else {
    							p.sendMessage(ChatColor.DARK_RED+"Chat sounds are now disabled.");
    						}
    					}
    				}
        		}
    	} else {
    		//Implement console/admin version later (Let's you check any name's money.)
    	}
    	return false; 
    }
    
    @EventHandler(priority=EventPriority.LOW)
    public void onWorldSave(WorldSaveEvent ev) {
    	saveOurData();
    }
    
    @EventHandler(priority=EventPriority.LOW)
    public void onPlayerJoin(PlayerJoinEvent ev) {
    	for (int i=0;i<Bukkit.getOnlinePlayers().toArray().length;i++) {
    		Player p = (Player)Bukkit.getOnlinePlayers().toArray()[i];
    		if (p!=null) {
    			p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 10, 1);
    			//LEAVE: Sound.NOTE_PLING, 8, 0.7f);
    			//MESSAGE: Sound.NOTE_STICKS, 0.6f, 0.85f);
    		}
    	}
    	playerdata.add(new PlayerStructure(ev.getPlayer(),getServerTickTime()));
    	log("[TASK] New Player Data has been added. Size of array: "+playerdata.size(),4);
    	
    	//Update player max health. Check equipment too.
    	setPlayerMaxHealth(ev.getPlayer());
    	ev.getPlayer().getScoreboard().getTeam(ev.getPlayer().getName().toLowerCase()).setSuffix(createHealthbar(((ev.getPlayer().getHealth())/ev.getPlayer().getMaxHealth())*100,ev.getPlayer()));
    }
    
    @EventHandler(priority=EventPriority.LOW)
    public void onPlayerLeave(PlayerQuitEvent ev) {
    	TwosideSpleefGames.PassEvent(ev);
    	
    	for (int i=0;i<Bukkit.getOnlinePlayers().toArray().length;i++) {
    		Player p = (Player)Bukkit.getOnlinePlayers().toArray()[i];
    		if (p!=null) {
    			p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 8, 0.7f);
    		}
    	}
    	
    	//Find the player that is getting removed.
    	for (int i=0;i<playerdata.size();i++) {
    		PlayerStructure pd = playerdata.get(i);
    		if (pd.name.equalsIgnoreCase(ev.getPlayer().getName())) {
    			//Make sure to save the config for this player.
    			pd.saveConfig();
    	    	playerdata.remove(i);
    	    	log("[TASK] Player Data for "+ev.getPlayer().getName()+" has been removed. Size of array: "+playerdata.size(),4);
    	    	break;
    		}
    	}
    }
    
    @EventHandler(priority=EventPriority.LOW) 
    public void onPlayerChat(final AsyncPlayerChatEvent ev) {
    	if (ev.getMessage().length()>=1) {
    		//See if we're using a bank terminal.
    		Player thisp = ev.getPlayer();
    		if (withdrawUser.equalsIgnoreCase(thisp.getName())) {
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
    			withdrawUser="";
    			ev.setMessage("");
    			ev.setCancelled(true);
    		} else
			if (depositUser.equalsIgnoreCase(thisp.getName())) {
				//See if this message is a number.
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
    			depositUser="";
    			ev.setMessage("");
    			ev.setCancelled(true);
    		} else
			if (conversionUser.equalsIgnoreCase(thisp.getName())) {
				//See if this message is a number.
        		if (isNumeric(ev.getMessage())) {
	    			DecimalFormat df = new DecimalFormat("0.00");
	    			Integer value=Integer.parseInt(ev.getMessage());	    			
	    			if (value>=1) {
	    				if (thisp.getLevel()>=value) {
	    					//Take that amount of exp away from the player. Give them money in return.
	    					int startlv = thisp.getLevel();
	    					for (int i=startlv;i>=startlv-value;i--) {
	    						switch (i) {
    								case 0:
	    							case 1:
	    							case 2:
	    							case 3:
	    							case 4:
	    							case 5:
	    							case 6:
	    							case 7:
	    							case 8:
	    							case 9:
	    							case 10:
	    							case 11:
	    							case 12:
	    							case 13:
	    							case 14:
	    							case 15:
	    							case 16:
	    								{
	    									givePlayerMoney(thisp,(2*i+7)*XP_CONVERSION_RATE);
	    								}break;
	    							case 17:
	    							case 18:
	    							case 19:
	    							case 20:
	    							case 21:
	    							case 22:
	    							case 23:
	    							case 24:
	    							case 25:
	    							case 26:
	    							case 27:
	    							case 28:
	    							case 29:
	    							case 30:
	    							case 31:
	    								{
	    									givePlayerMoney(thisp,(5*i-38)*XP_CONVERSION_RATE);
	    								}break;
	    							default:{
											givePlayerMoney(thisp,(9*i-158)*XP_CONVERSION_RATE);
	    								}
	    						}
	    					}
	    					thisp.setLevel(thisp.getLevel()-value);
	    					ev.getPlayer().sendMessage(ChatColor.GOLD+"CONVERSION COMPLETE!");
	    					ev.getPlayer().sendMessage("  Now Holding: "+ChatColor.BLUE+"$"+df.format(getPlayerMoney(ev.getPlayer())));
	    				} else {
	        				thisp.sendMessage(ChatColor.RED+"You do not have that many levels. You can convert as many as "+ChatColor.WHITE+thisp.getLevel()+ChatColor.RED+" levels.");
	        				thisp.sendMessage(ChatColor.WHITE+"  Cancelled out of Conversion terminal.");
	    				}
	    			} else {
	    				thisp.sendMessage(ChatColor.RED+"You must convert at least "+ChatColor.WHITE+"1 level.");
	    				thisp.sendMessage(ChatColor.WHITE+"  Cancelled out of Conversion terminal.");
	    			}
        		} else {
    				thisp.sendMessage(ChatColor.RED+"Invalid Number!");
    				thisp.sendMessage(ChatColor.WHITE+"  Cancelled out of Conversion terminal.");
        		}
    			conversionUser="";
    			ev.setMessage("");
    			ev.setCancelled(true);
    		} else
    		if (TwosideShops.IsPlayerUsingTerminal(ev.getPlayer())) {
    			final WorldShopSession current_session = TwosideShops.GetSession(ev.getPlayer());
    			current_session.UpdateTime(); //Make sure our session does not expire.
    			switch (current_session.GetSessionType()) {
					case CREATE:
						if (isNumeric(ev.getMessage())) {
							int amt = Integer.parseInt(ev.getMessage());
							if (amt<=GenericFunctions.CountItems(ev.getPlayer(), current_session.getItem()) && amt>0) {
								current_session.SetAmt(amt);
								ev.getPlayer().sendMessage("Input how much each "+ChatColor.GREEN+GenericFunctions.GetItemName(current_session.getItem())+ChatColor.WHITE+" will cost:");
								current_session.SetSession(SessionState.PRICE);
							} else {
								if (amt<=0) {
									ev.getPlayer().sendMessage("You cannot sell a non-existent amount of items. Please try again.");
								} else {
									ev.getPlayer().sendMessage("You only have "+GenericFunctions.CountItems(ev.getPlayer(), current_session.getItem())+" of "+ChatColor.GREEN+GenericFunctions.GetItemName(current_session.getItem())+ChatColor.WHITE+". Please try again with a lower amount.");
								}
							}
						} else {
							ev.getPlayer().sendMessage("That is not a valid number! Please try again.");
						}
						break;
					case PRICE:
						if (isNumeric(ev.getMessage())) {
							final DecimalFormat df = new DecimalFormat("0.00");
							final double amt = Double.parseDouble(ev.getMessage());
							if (amt>0 && amt<=999999999999.99) {
								ev.getPlayer().sendMessage(ChatColor.DARK_BLUE+"Shop has been successfully created!");
								Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
									@Override
									public void run() {
										TwosideShops.SaveWorldShopData(
												TwosideShops.CreateWorldShop(current_session.GetSign(), current_session.getItem(), current_session.getAmt(), Double.parseDouble(df.format(amt)), ev.getPlayer().getName())
											);
										RemoveItemAmount(ev.getPlayer(), current_session.getItem(), current_session.getAmt());
										TwosideShops.RemoveSession(ev.getPlayer());
									}
								},1);
								
							} else {
								if (amt>999999999999.99) {
									ev.getPlayer().sendMessage("You cannot sell an item for that ridiculous amount. Please try again.");
								} else {
									ev.getPlayer().sendMessage("You cannot sell an item for free. Please try again.");
								}
							}
						} else {
							ev.getPlayer().sendMessage("That is not a valid number! Please try again.");
						}
						break;
					case EDIT:
						if (isNumeric(ev.getMessage())) {
							int amt = Integer.parseInt(ev.getMessage());
							DecimalFormat df = new DecimalFormat("0.00");
							WorldShop shop = TwosideShops.LoadWorldShopData(TwosideShops.GetShopID(current_session.GetSign())); 
							if (amt>=0) {
								if (amt<=GenericFunctions.CountItems(ev.getPlayer(), shop.GetItem())) {
									shop.UpdateAmount(shop.GetAmount()+amt);
									RemoveItemAmount(ev.getPlayer(), shop.GetItem(), amt);
									TwosideShops.SaveWorldShopData(shop);
									TwosideShops.UpdateSign(shop, TwosideShops.GetShopID(current_session.GetSign()), current_session.GetSign());
									ev.getPlayer().sendMessage("Added "+ChatColor.AQUA+amt+ChatColor.WHITE+" more "+ChatColor.GREEN+shop.GetItemName()+ChatColor.WHITE+" to your shop!");
									ev.getPlayer().sendMessage("Input how much each "+ChatColor.GREEN+shop.GetItemName()+ChatColor.WHITE+" will cost (Old value - "+ChatColor.YELLOW+"$"+df.format(shop.GetUnitPrice())+ChatColor.WHITE+":");
									
									current_session.SetSession(SessionState.UPDATE);
								} else {
									if (amt<=0) {
										ev.getPlayer().sendMessage("You cannot add a non-existent amount of items. Please try again.");
									} else {
										ev.getPlayer().sendMessage("You only have "+GenericFunctions.CountItems(ev.getPlayer(), shop.GetItem())+" of "+ChatColor.GREEN+shop.GetItemName()+ChatColor.WHITE+". Please try again with a lower amount.");
									}
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
											final ItemStack dropitem = drop;
											Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
												@Override
												public void run() {
													p.getWorld().dropItemNaturally(p.getLocation(), dropitem).setPickupDelay(0);
												}
											},1);
											dropAmt-=shop.GetItem().getMaxStackSize();
										} else {
											drop.setAmount(dropAmt);
											final ItemStack dropitem = drop;
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
									TwosideShops.UpdateSign(shop, TwosideShops.GetShopID(current_session.GetSign()), current_session.GetSign());
									
									if (shop.GetAmount()>0) {
										current_session.SetSession(SessionState.UPDATE);
										ev.getPlayer().sendMessage("Input how much each "+ChatColor.GREEN+shop.GetItemName()+ChatColor.WHITE+" will cost (Old value - "+ChatColor.YELLOW+"$"+df.format(shop.GetUnitPrice())+ChatColor.WHITE+":");
									} else {
										ev.getPlayer().sendMessage(ChatColor.DARK_BLUE+"Shop successfully updated!");
										TwosideShops.RemoveSession(ev.getPlayer());
									}
								} else {
									ev.getPlayer().sendMessage("You only have "+shop.GetAmount()+" of "+ChatColor.GREEN+shop.GetItemName()+ChatColor.WHITE+" in the shop. Please try again.");
								}
							}
						} else {
							ev.getPlayer().sendMessage("That is not a valid number! Please try again.");
						}
						break;
					case UPDATE:
						if (isNumeric(ev.getMessage())) {
							double amt = Double.parseDouble(ev.getMessage());
							WorldShop shop = TwosideShops.LoadWorldShopData(TwosideShops.GetShopID(current_session.GetSign())); 
							if (amt>0 && amt<=999999999999.99) {
								shop.UpdateUnitPrice(amt);
								TwosideShops.SaveWorldShopData(shop);
								TwosideShops.UpdateSign(shop, TwosideShops.GetShopID(current_session.GetSign()), current_session.GetSign());
								ev.getPlayer().sendMessage(ChatColor.DARK_BLUE+"Shop successfully updated!");
								TwosideShops.RemoveSession(ev.getPlayer());
							} else {
								if (amt>999999999999.99) {
									ev.getPlayer().sendMessage("You cannot sell an item for that ridiculous amount. Please try again.");
								} else {
									ev.getPlayer().sendMessage("You cannot sell an item for free. Please try again.");
								}
							}
						} else {
							ev.getPlayer().sendMessage("That is not a valid number! Please try again.");
						}
						break;
					case PURCHASE:
						if (isNumeric(ev.getMessage())) {
							DecimalFormat df = new DecimalFormat("0.00");
							int amt = Integer.parseInt(ev.getMessage());
							if (amt>0) {
								int shopID = TwosideShops.GetShopID(current_session.GetSign());
								WorldShop shop = TwosideShops.LoadWorldShopData(shopID);
								if (amt<=shop.GetAmount()) {
									if (getPlayerMoney(ev.getPlayer())>=amt*shop.GetUnitPrice()) {
										ev.getPlayer().sendMessage(ChatColor.DARK_BLUE+"Successfully bought "+amt+" "+ChatColor.GREEN+shop.GetItemName()+ChatColor.WHITE+"!");
										shop.UpdateAmount(shop.GetAmount()-amt);
										ItemStack shopItem = shop.GetItem();
										int dropAmt = amt;
										while (dropAmt>0) {
											if (dropAmt>shop.GetItem().getMaxStackSize()) {
											shopItem.setAmount(shop.GetItem().getMaxStackSize());
											final ItemStack dropitem = shopItem;
											Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
												@Override
												public void run() {
													ev.getPlayer().getWorld().dropItemNaturally(ev.getPlayer().getLocation(), dropitem).setPickupDelay(0);
												}
											},1);
											dropAmt-=shop.GetItem().getMaxStackSize();
											} else {
												shopItem.setAmount(dropAmt);
												final ItemStack dropitem = shopItem;
												Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
													@Override
													public void run() {
														ev.getPlayer().getWorld().dropItemNaturally(ev.getPlayer().getLocation(), dropitem).setPickupDelay(0);
													}
												},1);
												dropAmt=0;
											}
										}
										TwosideShops.UpdateSign(shop, shopID, current_session.GetSign());
										TwosideShops.SaveWorldShopData(shop);
										TwosideShops.RemoveSession(ev.getPlayer());
										givePlayerMoney(ev.getPlayer(), -amt*shop.GetUnitPrice());
										TwosideShops.AddNewPurchase(shop.GetOwner(), ev.getPlayer(), shop.GetItem(), amt*shop.GetUnitPrice(), amt);
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
							ev.getPlayer().sendMessage("That is not a valid number! Please try again.");
						}
						break;
					default:
						break;
    			}
    			ev.setMessage("");
    			ev.setCancelled(true);
    		} else
	    	for (int i=0;i<Bukkit.getOnlinePlayers().toArray().length;i++) {
	    		Player p = (Player)Bukkit.getOnlinePlayers().toArray()[i];
	    		if (p!=null) {
	    			for (int j=0;j<playerdata.size();j++) {
	    				if (playerdata.get(j).name.equalsIgnoreCase(p.getName())) {
	    					if (playerdata.get(j).sounds_enabled) {
	    		    			p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BASEDRUM, 0.6f, 0.85f);
	    					}
	    					break;
	    				}
	    			}
	    			
	    		}
	    	}
    		int pos = -1;
    		log(ev.getMessage()+" "+ev.getMessage().indexOf("[]"),5);
    		if (ev.getMessage().indexOf("[]")>-1) {
    			pos = ev.getMessage().indexOf("[]");
    			ev.setMessage(ev.getMessage().replace("[]", ""));
        		log("pos is "+pos+" message is: {"+ev.getMessage()+"}",5);
        		DiscordMessageSender.sendRawMessageDiscord("**"+ev.getPlayer().getName()+"** "+ev.getMessage().substring(0, pos)+"**["+ChatColor.stripColor(GenericFunctions.GetItemName(ev.getPlayer().getEquipment().getItemInMainHand()))+"]**"+"\n```"+WorldShop.GetItemInfo(ev.getPlayer().getEquipment().getItemInMainHand())+"```\n"+ev.getMessage().substring(pos));
    			Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"tellraw @a [\"\",{\"text\":\"<"+ev.getPlayer().getName()+"> \"},{\"text\":\""+ev.getMessage().substring(0, pos)+"\"},{\"text\":\""+ChatColor.GREEN+"["+ChatColor.stripColor(GenericFunctions.GetItemName(ev.getPlayer().getEquipment().getItemInMainHand()))+ChatColor.GREEN+"]"+ChatColor.WHITE+"\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\""+GenericFunctions.GetItemName(ev.getPlayer().getEquipment().getItemInMainHand())+"\n"+WorldShop.GetItemInfo(ev.getPlayer().getEquipment().getItemInMainHand()).replace("\"", "\\\"")+"\"}},{\"text\":\""+ev.getMessage().substring(pos)+"\"}]");
    			ev.setCancelled(true);
    		}
    		//Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"tellraw @a [\"\",{\"text\":\""+ChatColor.GREEN+"[Item]"+ChatColor.WHITE+"\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\""+(ev.getPlayer().getEquipment().getItemInMainHand().getType())+"\"}},{\"text\":\" "+ev.getMessage().substring(0, pos)+" \"}]");

    	}
    }
    
    @EventHandler(priority=EventPriority.LOW)
    public void onPlayerInteract(PlayerInteractEvent ev) {
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
			ev.getPlayer().sendMessage(ChatColor.DARK_BLUE+"New Recycling Center successfully created at "+ev.getClickedBlock().getLocation().toString());
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
									m.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING,100,0));
									m.setTarget(player);
								}
							}
						} else {
							player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION,200,0));
						}
					}
				}
			},8);
			/*
			if (p.isBlocking()) {
				//Give absorption hearts.
				if (isDefender(p)) {
					p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION,10,2));
				} else {
					p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION,10,1));
				}
			}*/
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
						log("Malleable Base Quest: Comparing "+ev.getPlayer().getInventory().getItem(i).getType()+" to "+ev.getPlayer().getInventory().getItem(i).getType(),4);
					}
					if (ev.getPlayer().getInventory().getItem(i)!=null && GenericFunctions.UserFriendlyMaterialName(ev.getPlayer().getInventory().getItem(i)).equalsIgnoreCase(MalleableBaseQuest.getItem(ev.getPlayer().getEquipment().getItemInMainHand()))) {
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
			log(ev.getPlayer()+" Right-clicked bed. Set bed spawn to "+BedPos.toString(),3);
			ev.getPlayer().setBedSpawnLocation(BedPos);
			ev.getPlayer().sendMessage(ChatColor.BLUE+""+ChatColor.ITALIC+"New bed respawn location set.");
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
    	if (ev.getAction()==Action.RIGHT_CLICK_AIR || (ev.getPlayer().isSneaking() && ev.getAction()==Action.RIGHT_CLICK_AIR) || (ev.getPlayer().isSneaking() && ev.getAction()==Action.RIGHT_CLICK_BLOCK)) {
    		if (ev.getPlayer().getInventory().getItemInMainHand().hasItemMeta() &&
    				ev.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasLore() &&
    				ev.getPlayer().getInventory().getItemInMainHand().getItemMeta().getLore().size()==4 &&
    				ev.getPlayer().getInventory().getItemInMainHand().getItemMeta().getLore().get(3).contains(ChatColor.DARK_PURPLE+"ID#")) {
    			//This is an item cube.
    			ev.setCancelled(true);
    			int itemcube_id=Integer.parseInt(ev.getPlayer().getInventory().getItemInMainHand().getItemMeta().getLore().get(3).split("#")[1]);
    			int size=0;
    			if (ev.getPlayer().getInventory().getItemInMainHand().getType()==Material.CHEST) {
    				size=9;
    			} else {
    				size=27;
    			}
    			ev.getPlayer().openInventory(Bukkit.getServer().createInventory(ev.getPlayer(), size, "Item Cube #"+itemcube_id));
    			ev.getPlayer().playSound(ev.getPlayer().getLocation(), Sound.BLOCK_CHEST_OPEN, 1.0f, 1.0f);
    		}
    	}
    	if (b!=null && (b.getType() == Material.SIGN ||
    			b.getType() == Material.SIGN_POST ||
    			b.getType() == Material.WALL_SIGN) && b.getState()!=null && (b.getState() instanceof Sign)) {
        	log(b.getLocation().toString()+": This is a sign.",5);
    		Sign s = (Sign)(b.getState());
    		
    		//Determine if this is a shop sign.
    		if (b.getType()==Material.WALL_SIGN &&
    				!TwosideShops.IsPlayerUsingTerminal(player)) { //Shop signs can only be wall signs.
    			log("This is a wall sign.",5);
    			if (s.getLine(0).equalsIgnoreCase("shop")) {
        			log("This is a shop sign.",5);
    				//Create a new shop.
    				ItemStack item = player.getEquipment().getItemInMainHand();
    				if (item.getType()!=Material.AIR) {
        				WorldShopSession ss = TwosideShops.AddSession(SessionState.CREATE, player, s);
    					player.sendMessage("Creating a shop to sell "+ChatColor.GREEN+GenericFunctions.GetItemName(item)+ChatColor.WHITE+".");
    					int totalcount = 0;
    					totalcount = GenericFunctions.CountItems(player, item);
    					log("We have "+totalcount+" items in our inventory.",4);
    					ss.SetItem(item);
    					player.sendMessage("How many of this item do you want to sell? "+ChatColor.GREEN+"(MAX: "+ChatColor.YELLOW+totalcount+ChatColor.GREEN+")");
    				} else {
    					player.sendMessage(ChatColor.RED+"Cannot create a shop with nothing! "+ChatColor.WHITE+"Right-click the sign"
    							+ " with the item you want to sell in your hand.");
    				}
    			} else 
    			if (s.getLine(0).equalsIgnoreCase(ChatColor.BLUE+"-- SHOP --")) {
        			log("This is a buy shop sign.",5);
    				int shopID = TwosideShops.GetShopID(s);
        			WorldShop shop = TwosideShops.LoadWorldShopData(shopID);
        			
        			Location newloc = ev.getClickedBlock().getLocation().add(-ev.getBlockFace().getModX()+0.5, -ev.getBlockFace().getModY()+1.5, -ev.getBlockFace().getModZ()+0.5);
        			//See if a drop entity is already here.
        			boolean item_here=false;
    				Collection<Entity> entities = ev.getPlayer().getWorld().getNearbyEntities(newloc, 1, 1, 1);
        			for (int i=0;i<entities.size();i++) {
        				Entity e = Iterables.get(entities, i);
        				log("Entity Location:"+e.getLocation().toString(),5);
        				log("Comparing locations: "+e.getLocation().toString()+":::"+newloc.toString(),5);
        				if (e.getType()==EntityType.DROPPED_ITEM) {
        					Item it = (Item)e;
	        				if (
	        						it.getItemStack().getType()==shop.GetItem().getType() &&
	        						it.getItemStack().getDurability()==shop.GetItem().getDurability() &&
	        						Math.abs(e.getLocation().getX()-newloc.getX()) <= 1 &&
	        						Math.abs(e.getLocation().getZ()-newloc.getZ()) <= 1 &&
	        						Math.abs(e.getLocation().getY()-newloc.getY())<=1
	        						) {
	        					item_here=true;
	        				}
        				}
        			}
        			if (!item_here) {
        				log("Spawning item!",5);
        				ItemStack i = shop.GetItem().clone();
        				ItemStack drop = Artifact.convert(i);
        				drop.removeEnchantment(Enchantment.LUCK);
        				Item it = ev.getPlayer().getWorld().dropItemNaturally(ev.getClickedBlock().getLocation().add(-ev.getBlockFace().getModX()+0.5, -ev.getBlockFace().getModY()+1.5, -ev.getBlockFace().getModZ()+0.5), drop);
        				it.setPickupDelay(999999999);
        				it.setVelocity(new Vector(0,0,0));
        				it.setCustomName(ChatColor.values()[(int)(Math.random()*15.0)]+ChatColor.stripColor(shop.GetItemName()));
        				it.setCustomNameVisible(false);
        				it.setInvulnerable(true);
        				//it.setGlowing(true);
        				it.teleport(ev.getClickedBlock().getLocation().add(-ev.getBlockFace().getModX()+0.5, -ev.getBlockFace().getModY()+1.5, -ev.getBlockFace().getModZ()+0.5));
        			}
        			if (shop.GetOwner().equalsIgnoreCase(ev.getPlayer().getName())) {
        				player.sendMessage(ChatColor.DARK_PURPLE+"Editing shop...");
        				player.sendMessage("Insert more "+ChatColor.GREEN+shop.GetItemName()+ChatColor.WHITE+" by typing a positive amount "+ChatColor.GREEN+"(MAX:"+GenericFunctions.CountItems(player,shop.GetItem())+")"+ChatColor.WHITE+". Or withdraw "+ChatColor.GREEN+shop.GetItemName()+ChatColor.WHITE+" by typing a negative amount "+ChatColor.GREEN+"(MAX:"+shop.GetAmount()+")"+ChatColor.WHITE+".");
	    				TwosideShops.AddSession(SessionState.EDIT, player, s);
        			} else {
	        			if (shop.GetAmount()>0) {
		        			player.sendMessage("How many "+ChatColor.GREEN+shop.GetItemName()+ChatColor.WHITE+" would you like to buy? "+ChatColor.GREEN+"(MAX: "+((getPlayerMoney(player)<(shop.GetAmount()*shop.GetUnitPrice()))?(int)(getPlayerMoney(player)/shop.GetUnitPrice()):shop.GetAmount())+")");
		
		    				//Initiate buying session.
		    				TwosideShops.AddSession(SessionState.PURCHASE, player, s);
		        			log("Added a shop session for "+player.getName()+".",4);
		        			shop.sendItemInfo(player);
	        			} else {
	        				player.sendMessage(ChatColor.GOLD+"Sorry! "+ChatColor.WHITE+"This shop is sold out! Let "+ChatColor.LIGHT_PURPLE+shop.GetOwner()+ChatColor.WHITE+" know to restock the shop!");
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
    				if (withdrawUser.equalsIgnoreCase("")) {
    					if (!depositUser.equalsIgnoreCase(ev.getPlayer().getName()) &&
    							!conversionUser.equalsIgnoreCase(ev.getPlayer().getName())) {
							ev.getPlayer().sendMessage(ChatColor.GOLD+"Say/Type the amount you want to WITHDRAW today.");
		    				ev.getPlayer().sendMessage("  In Bank: "+ChatColor.BLUE+"$"+getPlayerBankMoney(ev.getPlayer()));
		    				//Activate the terminal.
		    				withdrawUser=ev.getPlayer().getName();
		    				withdrawTime=getServerTickTime();
    					} else {
    						//Can't use if we're already using another terminal.
    						ev.getPlayer().sendMessage(ChatColor.RED+"You're already using another terminal. "+ChatColor.WHITE+"Please finish that operation first.");
    					}
    				} else {
        				if (!withdrawUser.equalsIgnoreCase(ev.getPlayer().getName())) {
        					ev.getPlayer().sendMessage(ChatColor.RED+"TERMINAL IS BEING USED. "+ChatColor.WHITE+"Please try again later.");
        				}
    				}
    			} else
				if (s.getLine(1).equalsIgnoreCase(ChatColor.DARK_BLUE+"DEPOSIT")) {
    				if (depositUser.equalsIgnoreCase("")) {
    					if (!withdrawUser.equalsIgnoreCase(ev.getPlayer().getName()) &&
    							!conversionUser.equalsIgnoreCase(ev.getPlayer().getName())) {
							ev.getPlayer().sendMessage(ChatColor.GOLD+"Say/Type the amount you want to DEPOSIT today.");
							ev.getPlayer().sendMessage("  Currently Holding: "+ChatColor.GREEN+"$"+getPlayerMoney(ev.getPlayer()));
		    				//Activate the terminal.
		    				depositUser=ev.getPlayer().getName();
		    				depositTime=getServerTickTime();
    					} else {
    						//Can't use if we're already using another terminal.
    						ev.getPlayer().sendMessage(ChatColor.RED+"You're already using another terminal. "+ChatColor.WHITE+"Please finish that operation first.");
    					}
    				} else {
    					if (!depositUser.equalsIgnoreCase(ev.getPlayer().getName())) {
    						ev.getPlayer().sendMessage(ChatColor.RED+"TERMINAL IS BEING USED. "+ChatColor.WHITE+"Please try again later.");
    					}
    				}
    			} else
				if (s.getLine(1).equalsIgnoreCase(ChatColor.DARK_BLUE+"EXP CONVERSION")) {
    				if (conversionUser.equalsIgnoreCase("")) {
    					if (!withdrawUser.equalsIgnoreCase(ev.getPlayer().getName()) &&
    							!depositUser.equalsIgnoreCase(ev.getPlayer().getName())) {
							ev.getPlayer().sendMessage(ChatColor.GOLD+"Say/Type the amount of experience you want to convert today.");
							ev.getPlayer().sendMessage("  Currently Have: "+ChatColor.GREEN+ev.getPlayer().getLevel()+" levels");
		    				//Activate the terminal.
		    				conversionUser=ev.getPlayer().getName();
		    				conversionTime=getServerTickTime();
    					} else {
    						//Can't use if we're already using another terminal.
    						ev.getPlayer().sendMessage(ChatColor.RED+"You're already using another terminal. "+ChatColor.WHITE+"Please finish that operation first.");
    					}
    				} else {
    					if (!conversionUser.equalsIgnoreCase(ev.getPlayer().getName())) {
    						ev.getPlayer().sendMessage(ChatColor.RED+"TERMINAL IS BEING USED. "+ChatColor.WHITE+"Please try again later.");
    					}
    				}
    			}
    		}
    	}
    }
    
    @EventHandler(priority=EventPriority.LOW)
    public void onBlockPlace(BlockPlaceEvent ev) {
    	
    	TwosideSpleefGames.PassEvent(ev);
    	
    	if (ev.getItemInHand().hasItemMeta() &&
    			ev.getItemInHand().getItemMeta().hasLore() &&
    			ev.getItemInHand().getItemMeta().getLore().size()==4 &&
    			ev.getItemInHand().getItemMeta().getLore().get(3).contains(ChatColor.DARK_PURPLE+"ID#")) {
    		//This is an item cube.
    		ev.setCancelled(true);
    	}
    }
    
    @EventHandler(priority=EventPriority.LOW)
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
	    	DeathManager.addNewDeathStructure(ev.getDrops(), p.getLocation(), p);
	    	p.getInventory().clear();
    	}
    }
    
    @EventHandler(priority=EventPriority.LOW)
    public void onSignChange(SignChangeEvent ev) {
    	Player p = ev.getPlayer();
    	Block b = ev.getBlock();
    	String line1 = ev.getLine(0);
    	String line2 = ev.getLine(2);
    	//-- BANK --
    	if (p.isOp()) {
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
    		}
    	}
    }
    
    @EventHandler(priority=EventPriority.LOW)
    public void onItemCraft(CraftItemEvent ev) {
    	log(ev.getCurrentItem().getItemMeta().toString(),5);
    	log("Original Result item is "+ev.getInventory().getItem(0).toString(),4);
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
	    			ev.getInventory().getItem(0).setType(Material.AIR);
	    			ev.getWhoClicked().sendMessage(ChatColor.RED+"You cannot craft items with an Item Cube!");
	    	    	ev.setCurrentItem(new ItemStack(Material.AIR));
	    		}
    		}
    	}
    	if (ev.getCurrentItem().hasItemMeta()) {
	    	ItemMeta item_meta = ev.getCurrentItem().getItemMeta();
	    	if (item_meta.getDisplayName().contains("Item Cube")) {
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
    
    @EventHandler(priority=EventPriority.LOW)
    public void onPlayerDropItem(PlayerDropItemEvent ev) {
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
    }
    
    @EventHandler(priority=EventPriority.LOW)
    public void onInventoryOpen(InventoryOpenEvent ev) {
    	if (ev.getPlayer() instanceof Player) {
    		final Player p = (Player)ev.getPlayer();
    		
    		/*
        	for (int i=0;i<playerdata.size();i++) {
        		PlayerStructure pd = playerdata.get(i);
        		if (pd.name.equalsIgnoreCase(p.getName())) {
        			if (!pd.opened_inventory) {
        				final InventoryView view = p.getOpenInventory();
        				p.closeInventory();
        				Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
        					 @Override
   					      public void run() {
        	        			p.openInventory(view);
        					 }
        				}
        				,1);
        				pd.opened_inventory=true;
        			}
        		}
        	}
        	*/
        	
        	//Check if this is an Item Cube inventory.
        	if (ev.getInventory().getTitle().contains("Item Cube")) {
        		//p.sendMessage("This is an Item Cube inventory.");
        		int id = Integer.parseInt(ev.getInventory().getTitle().split("#")[1]);
        		List<ItemStack> itemcube_contents = itemCube_loadConfig(id);
        		for (int i=0;i<ev.getView().getTopInventory().getSize();i++) {
        			if (itemcube_contents.get(i)!=null) {
                		log("Loading item "+itemcube_contents.get(i).toString()+" in slot "+i,5);
        				if (itemcube_contents.get(i).getAmount()>0) {
        					ev.getInventory().setItem(i, itemcube_contents.get(i));
        				} else {
        					ev.getInventory().setItem(i, new ItemStack(Material.AIR));
        				}
        			}
        		}
        	}
    	}
    }
    
    @EventHandler(priority=EventPriority.LOW)
    public void onInventoryClose(InventoryCloseEvent ev) {
    	if (ev.getPlayer() instanceof Player) {
    		Player p = (Player)ev.getPlayer();
        	if (ev.getInventory().getTitle().contains("Death Loot")) {
        		Location deathloc = DeathManager.getDeathStructure(p).deathloc;
        		//Whatever is left drops at the death location.
        		if (DeathManager.CountOccupiedSlots(p.getOpenInventory().getTopInventory())>0) {
        			p.sendMessage(ChatColor.GOLD+"The rest of your items have dropped at your death location!");
        		}
        		double amounttotake = DeathManager.CountOccupiedSlots(p.getInventory())*DeathManager.CalculateDeathPrice(p);
        		if (getPlayerMoney(p)>=amounttotake) {
        			givePlayerMoney(p,getPlayerMoney(p)-amounttotake);
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
        	//Check if this is an Item Cube inventory.
        	if (ev.getInventory().getTitle().contains("Item Cube")) {
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
        	}
    	}
    }

    @EventHandler(priority=EventPriority.LOW)
    public void onInventoryDrag(InventoryDragEvent ev) {
    	//You are not allowed to drag arrow quivers.
    	if (ev.getOldCursor().getType()==Material.TIPPED_ARROW &&
    			ev.getOldCursor().getEnchantmentLevel(Enchantment.ARROW_INFINITE)==5) {
    		ev.setCancelled(true);
    	}
    	if (ev.getInventory().getTitle().contains("Item Cube #")) {
    		log("Item Cube window identified.",5);
    		final int id=Integer.parseInt(ev.getInventory().getTitle().split("#")[1]);
    		if (itemCube_getCubeType(id)==CubeType.ENDER) {
    			ev.setCancelled(true);
    		}
    	}
    }

    @EventHandler(priority=EventPriority.LOW)
    public void onItemChange(PlayerItemHeldEvent ev) {
    	final Player player = ev.getPlayer();
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			@Override
			public void run() {
		    	setPlayerMaxHealth(player);
			}
		},1);
    }
    
    @EventHandler(priority=EventPriority.LOW)
    public void onRegainHealth(EntityRegainHealthEvent ev) {
    	if (ev.getRegainReason()==RegainReason.SATIATED && ev.getEntityType()==EntityType.PLAYER) {
    		ev.setCancelled(true);
    		Player p = (Player)ev.getEntity();
	    	//Find the player that is losing food level.
	    	for (int i=0;i<playerdata.size();i++) {
	    		PlayerStructure pd = playerdata.get(i);
	    		if (pd.name.equalsIgnoreCase(p.getName())) {
	    			if (((Player)ev.getEntity()).getSaturation()>0 && pd.saturation<20) {
	    				pd.saturation+=2;
	    				((Player)ev.getEntity()).setSaturation(((Player)ev.getEntity()).getSaturation()-1);
	    	    		log("Saturation increased to "+pd.saturation+". Old saturation: "+((Player)ev.getEntity()).getSaturation(),4);
	    			}
	    		}
	    	}
    		//((Player)ev.getEntity()).setSaturation(((Player)ev.getEntity()).getSaturation()+1.0f);
    		//We will never use saturation to heal.
    	}
    }
    
    @EventHandler(priority=EventPriority.LOW)
    public void onFoodLevelChange(FoodLevelChangeEvent ev) {
    	if (ev.getEntityType()==EntityType.PLAYER) {
    		Player p = (Player)ev.getEntity();
    		if (p.getFoodLevel()<ev.getFoodLevel()) {
    			//If we are eating food, restore health.
    			if (p.getHealth()<p.getMaxHealth() && !p.isDead()) {
    				if (p.getHealth()+FOOD_HEAL_AMT>p.getMaxHealth()) {
    					p.setHealth(p.getMaxHealth());
    				} else {
    					p.setHealth(p.getHealth()+FOOD_HEAL_AMT);
    				}
    			}
    			p.setSaturation(p.getSaturation()*2);
    		}
    		if (p.getFoodLevel()>ev.getFoodLevel()) {
		    	//Find the player that is losing food level.
		    	for (int i=0;i<playerdata.size();i++) {
		    		PlayerStructure pd = playerdata.get(i);
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
    }
    
    @EventHandler(priority=EventPriority.LOW)
    public void onInventoryClick(InventoryClickEvent ev) {
    	final Player player = (Player)ev.getWhoClicked();
    	log("Raw Slot Clicked: "+ev.getRawSlot(),5);
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			@Override
			public void run() {
		    	setPlayerMaxHealth(player);
			}
		},1);
		
		if (ev.getInventory().getType()==InventoryType.ANVIL &&
				ev.getRawSlot()==2) {
			//The results slot was clicked. We should set the result's item name properly back to what it was.
			if (ev.getCurrentItem()!=null &&
					ev.getInventory().getItem(0)!=null &&
					ev.getInventory().getItem(0).getItemMeta().hasDisplayName()) {
				//It's possible we may have to fix the color code for this item. Check the first two characters.
				String oldname = ev.getInventory().getItem(0).getItemMeta().getDisplayName();
				ChatColor first = ChatColor.getByChar(oldname.charAt(1));
				log("First character is "+first,4);
				ChatColor second = ChatColor.getByChar(oldname.charAt(0));
				log("Second character is "+second,4);
				if (first!=null) {
					if (second!=null) {
						ItemMeta m = ev.getCurrentItem().getItemMeta();
						m.setDisplayName(first+""+second+ev.getCurrentItem().getItemMeta().getDisplayName().substring(2));
						ev.getCurrentItem().setItemMeta(m);
					} else {
						ItemMeta m = ev.getCurrentItem().getItemMeta();
						m.setDisplayName(first+ev.getCurrentItem().getItemMeta().getDisplayName().substring(1));
						ev.getCurrentItem().setItemMeta(m);
					}
				}
			}
		}
		
		if (ev.getInventory().getTitle().equalsIgnoreCase("Death Loot")) {
			//See how many items are in our inventory. Determine final balance.
			//Count the occupied slots.
			if (getPlayerMoney(player)+getPlayerBankMoney(player)-DeathManager.CalculateDeathPrice(player)*DeathManager.CountOccupiedSlots(player.getInventory())>=DeathManager.CalculateDeathPrice(player)) {
				//player.getInventory().addItem(ev.getCurrentItem());
				player.getLocation().getWorld().dropItemNaturally(player.getLocation(), ev.getCurrentItem()).setPickupDelay(0);
				ev.setCurrentItem(new ItemStack(Material.AIR));
	
				final DecimalFormat df = new DecimalFormat("0.00");
				Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
					@Override
					public void run() {
						player.sendMessage(ChatColor.BLUE+"New Balance: "+ChatColor.GREEN+"$"+df.format((getPlayerMoney(player)+getPlayerBankMoney(player)-DeathManager.CalculateDeathPrice(player)*DeathManager.CountOccupiedSlots(player.getInventory()))));
					}
				},5);
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
    	final InventoryClickEvent store = ev;
    	if ((ev.getInventory().getType()!=InventoryType.WORKBENCH ||
    			(ev.getInventory().getType()==InventoryType.WORKBENCH && ev.getRawSlot()>9)) && ev.getInventory().getTitle().contains("Item Cube #")) {
    		log("Item Cube window identified.",5);
    		final int id=Integer.parseInt(ev.getInventory().getTitle().split("#")[1]);
			//Check to see if the cursor item is an item cube.
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
    		}
    	}

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
    					ev.setResult(Result.DENY);
    					
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
    					
    					//See if we're looking at an Item Cube inventory already.
    					if (ev.getInventory().getTitle().contains("Item Cube")) {
    						//Check to see what the Item Cube ID is.
    						itemcubeid=Integer.parseInt(ev.getInventory().getTitle().split("#")[1]);
    					}
    					
    					//Check to see if the cursor item is an item cube.
    					if ((ev.getCursor().getType()==Material.CHEST ||
    							ev.getCursor().getType()==Material.STORAGE_MINECART ||
    							ev.getCursor().getType()==Material.ENDER_CHEST) &&
    							ev.getCursor().hasItemMeta() &&
    							ev.getCursor().getItemMeta().hasLore()) {
    						log("The clicked item has lore...",5);
    						for (int i=0;i<ev.getCursor().getItemMeta().getLore().size();i++) {
    							if (ev.getCursor().getItemMeta().getLore().get(i).contains(ChatColor.DARK_PURPLE+"ID#")) {
    	    						log("We clicked an item cube, checking ID.",5);
    								//We clicked an item cube. Check its ID.
    								int clicked_id = Integer.parseInt(ev.getCursor().getItemMeta().getLore().get(i).split("#")[1]);
    	    						log("ID is "+clicked_id+" and we are viewing "+itemcubeid,5);
    								if (clicked_id==itemcubeid) {
    									//The inventory we are viewing is the same as the item cube we have clicked on!
    									//Stop this before the player does something dumb!
    									ev.setCancelled(true);
    									break;
    								}
    							}
    						}
    					}
    					

    					
    					
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
    		    		}
    					
    					//Make sure we are not already inside the cube we're placing into.
    					if (idnumb!=itemcubeid) {
    						log(idnumb+" does not match "+itemcubeid,5);
	    					//Try to insert item inside this item cube.
	    					List<ItemStack> virtual_inventory = itemCube_loadConfig(idnumb);
	    					boolean stack_available=false;
    						//Now we will see if there are any places to stack blocks on.
    						int quantity=ev.getCursor().getAmount();
    						log("Amount held: "+quantity,5);
	    					for (int i=0;i<size;i++) {
	    						if (virtual_inventory.get(i).getType()==ev.getCursor().getType() &&
	    								virtual_inventory.get(i).getItemMeta().equals(ev.getCursor().getItemMeta()) &&
	    								virtual_inventory.get(i).getMaxStackSize()>virtual_inventory.get(i).getAmount()) {
		    							log("Entered Loop",5);
    									//This is the same, and we have room to throw some in.
    									int space=virtual_inventory.get(i).getMaxStackSize()-virtual_inventory.get(i).getAmount(); //How much space is here.
    									log("There is space for "+space+" blocks.",5);
    									if (space>=quantity) {
    										//We are done, because we can store everything.
    										virtual_inventory.get(i).setAmount(virtual_inventory.get(i).getAmount()+quantity);
    										quantity=0;
				    		    			final int ider = idnumb;
				    		    			final List<ItemStack> items = virtual_inventory;
				    		    			final CubeType type = cubetype;

				    		    			if (itemCube_getCubeType(idnumb)==CubeType.ENDER) {
					    		        		log("This is an Ender Item Cube transfer click.",5);
					    		    			//We are going to look at all players and see if they have this inventory open.
					    		    			final int id = idnumb;
					    		    			for (int j=0;j<Bukkit.getServer().getOnlinePlayers().toArray().length;j++) {
					    		    				//Make sure the player we are checking is not ourself.
					    		    				final Player p = (Player)Bukkit.getServer().getOnlinePlayers().toArray()[j];
					    		    				if (p.getOpenInventory()!=null &&
					    		    						!p.getName().equalsIgnoreCase(ev.getWhoClicked().getName()) &&
					    		    						p.getOpenInventory().getTitle().contentEquals("Item Cube #"+idnumb)) {

					    								p.closeInventory();
					    		    				}
					    		    			}
					    		    			
			    		    					Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			    									@Override
			    									public void run() {
														itemCube_saveConfig(ider,items,type);
			    									}
			    								},2);
				    		    			} else {
												itemCube_saveConfig(ider,items,type);
				    		    			}
    										ev.setCursor(new ItemStack(Material.AIR));
    										break;
	    								} else {
	    									//We still have more. Store what we can.
	    									quantity-=space;
	    									log("Still have "+quantity+" blocks left.",5);
	    									virtual_inventory.get(i).setAmount(virtual_inventory.get(i).getMaxStackSize());
    										itemCube_saveConfig(idnumb,virtual_inventory,cubetype);
	    								}
	    							}
	    						}
		    					if (quantity>0) {
		    						//We can't fit this anywhere else. So we put the rest back to the cursor.
		    						ev.getCursor().setAmount(quantity);
		    					} else {
		    						stack_available=true;
		    					}
	    		        	
	    					if (stack_available) {
		    					ev.setCursor(new ItemStack(Material.AIR));
		    					itemCube_saveConfig(idnumb,virtual_inventory,cubetype);
	    					} else {
		    						//Look for an empty space.
			    					for (int i=0;i<size;i++) {
			    						if (virtual_inventory.get(i).getType()==Material.AIR) {
			    							//WE have found an empty space. Throw it in there.
			    							virtual_inventory.set(i, ev.getCursor());
			    							log("Set item slot "+i+" to "+ev.getCursor().toString(),5);
					    					ev.setCursor(new ItemStack(Material.AIR));
				    		    			final int ider = idnumb;
				    		    			final List<ItemStack> items = virtual_inventory;
				    		    			final CubeType type = cubetype;

				    		    			if (itemCube_getCubeType(idnumb)==CubeType.ENDER) {
					    		        		log("This is an Ender Item Cube transfer click.",5);
					    		    			//We are going to look at all players and see if they have this inventory open.
					    		    			final int id = idnumb;
					    		    			for (int j=0;j<Bukkit.getServer().getOnlinePlayers().toArray().length;j++) {
					    		    				//Make sure the player we are checking is not ourself.
					    		    				final Player p = (Player)Bukkit.getServer().getOnlinePlayers().toArray()[j];
					    		    				if (p.getOpenInventory()!=null &&
					    		    						!p.getName().equalsIgnoreCase(ev.getWhoClicked().getName()) &&
					    		    						p.getOpenInventory().getTitle().contentEquals("Item Cube #"+idnumb)) {

					    								p.closeInventory();
					    		    				}
					    		    			}
					    		    			
			    		    					Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			    									@Override
			    									public void run() {
														itemCube_saveConfig(ider,items,type);
			    									}
			    								},2);
				    							break;
				    		    			} else {
												itemCube_saveConfig(ider,items,type);
				    		    			}
			    						}
			    					}
		    					}
    						} else {
    						//Well, we're already in here, I don't know why they didn't just use the
    						//minecraft inventory management system. Now I have to do math...
    						
	    					boolean stack_available=false;
	    					
	    					//Now we will see if there are any places to stack blocks on.
    						int quantity=ev.getCursor().getAmount();
    						log("Amount held: "+quantity,5);
	    					for (int i=0;i<size;i++) {
	    						if (ev.getView().getTopInventory().getItem(i)!=null &&
	    							ev.getView().getTopInventory().getItem(i).getType()==ev.getCursor().getType() &&
    								ev.getView().getTopInventory().getItem(i).getItemMeta().equals(ev.getCursor().getItemMeta()) &&
    								ev.getView().getTopInventory().getItem(i).getMaxStackSize()>ev.getView().getTopInventory().getItem(i).getAmount()) {
	    							log("Entered Loop",5);
									//This is the same, and we have room to throw some in.
									int space=ev.getView().getTopInventory().getItem(i).getMaxStackSize()-ev.getView().getTopInventory().getItem(i).getAmount(); //How much space is here.
									log("There is space for "+space+" blocks.",5);
									if (space>=quantity) {
										//We are done, because we can store everything.
										ev.getView().getTopInventory().getItem(i).setAmount(ev.getView().getTopInventory().getItem(i).getAmount()+quantity);
										quantity=0;
										List<ItemStack> itemlist = new ArrayList<ItemStack>();
										for (int j=0;j<ev.getView().getTopInventory().getSize();j++) {
											itemlist.add(ev.getView().getTopInventory().getItem(j));
										}
			    		    			
			    		    			final int ider = idnumb;
			    		    			final List<ItemStack> items = itemlist;
			    		    			final CubeType type = cubetype;
			    		    			if (itemCube_getCubeType(idnumb)==CubeType.ENDER) {
				    		        		log("This is an Ender Item Cube transfer click.",5);
				    		    			//We are going to look at all players and see if they have this inventory open.
				    		    			final int id = idnumb;
				    		    			for (int j=0;j<Bukkit.getServer().getOnlinePlayers().toArray().length;j++) {
				    		    				//Make sure the player we are checking is not ourself.
				    		    				final Player p = (Player)Bukkit.getServer().getOnlinePlayers().toArray()[j];
				    		    				if (p.getOpenInventory()!=null &&
				    		    						!p.getName().equalsIgnoreCase(ev.getWhoClicked().getName()) &&
				    		    						p.getOpenInventory().getTitle().contentEquals("Item Cube #"+idnumb)) {

				    								p.closeInventory();
				    		    				}
				    		    			}
				    		    			
		    		    					Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
		    									@Override
		    									public void run() {
													itemCube_saveConfig(ider,items,type);
		    									}
		    								},2);
			    		    			} else {
											itemCube_saveConfig(ider,items,type);
			    		    			}
										ev.setCursor(new ItemStack(Material.AIR));
										break;
    								} else {
    									//We still have more. Store what we can.
    									quantity-=space;
    									log("Still have "+quantity+" blocks left.",5);
    									ev.getView().getTopInventory().getItem(i).setAmount(ev.getView().getTopInventory().getItem(i).getMaxStackSize());
										List<ItemStack> itemlist = new ArrayList<ItemStack>();
										for (int j=0;j<ev.getView().getTopInventory().getSize();j++) {
											itemlist.add(ev.getView().getTopInventory().getItem(j));
										}
				    		    		if (itemCube_getCubeType(idnumb)==CubeType.ENDER) {
				    		        		log("This is an Ender Item Cube transfer click.",5);
				    		    			//We are going to look at all players and see if they have this inventory open.
				    		    			final int id = idnumb;
				    		    			for (int j=0;j<Bukkit.getServer().getOnlinePlayers().toArray().length;j++) {
				    		    				//Make sure the player we are checking is not ourself.
				    		    				final Player p = (Player)Bukkit.getServer().getOnlinePlayers().toArray()[j];
				    		    				if (p.getOpenInventory()!=null &&
				    		    						!p.getName().equalsIgnoreCase(ev.getWhoClicked().getName()) &&
				    		    						p.getOpenInventory().getTitle().contentEquals("Item Cube #"+idnumb)) {

				    								p.closeInventory();
				    		    				}
				    		    			}
				    		    			
				    		    			final int ider = idnumb;
				    		    			final List<ItemStack> items = itemlist;
				    		    			final CubeType type = cubetype;
		    		    					Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
		    									@Override
		    									public void run() {
													itemCube_saveConfig(ider,items,type);
		    									}
		    								},2);
				    		    		}
    								}
    							}
    						}
	    					if (quantity>0) {
	    						//We can't fit this anywhere else. So we put the rest back to the cursor.
	    						ev.getCursor().setAmount(quantity);
	    					} else {
	    						stack_available=true;
	    					}
	    					
	    					if (stack_available) {
		    					ev.setCursor(new ItemStack(Material.AIR));
	    					} else {
	    						for (int i=0;i<size;i++) {
		    						if (ev.getView().getTopInventory().getItem(i)==null || ev.getView().getTopInventory().getItem(i).getType()==Material.AIR) {
		    							//WE have found an empty space. Throw it in there.
		    							ev.getView().getTopInventory().setItem(i, ev.getCursor());
				    					ev.setCursor(new ItemStack(Material.AIR));
		    							break;
		    						}
		    					}
	    					}
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
	    				List<HumanEntity> viewers = ev.getViewers();
	    				for (int i=0;i<viewers.size();i++) {
	    					log("Viewer "+viewers.get(i).getName()+" found.",5);
	    					int inventory_size;
	    					if (ev.getCurrentItem().getType()==Material.CHEST) {
	    						inventory_size=9;
	    					} else {
	    						inventory_size=27;
	    					}
    						Player p = (Player)viewers.get(i);
	    					//We're going to check if the currently opened inventory is not an ender item cube. Otherwise we cannot proceed.
	    					if (p.getOpenInventory().getTitle().contains("Item Cube #") &&
	    							itemCube_getCubeType(Integer.parseInt(p.getOpenInventory().getTitle().split("#")[1]))==CubeType.ENDER &&
	    							ev.getRawSlot()<27) {
	    						p.sendMessage("Cannot access another item cube due to being inside an ender item cube.");
	    						//p.openInventory(Bukkit.getServer().createInventory(p, inventory_size, "Item Cube #"+Integer.parseInt(p.getOpenInventory().getTitle().split("#")[1])));
	    					} else {
		    					ev.setCancelled(true);
		    					ev.setResult(Result.DENY);
	    						p.openInventory(Bukkit.getServer().createInventory(p, inventory_size, "Item Cube #"+idnumb));
	    						p.playSound(p.getLocation(),Sound.BLOCK_CHEST_OPEN,1.0f,1.0f);
	    					}
	    				}
	    			}
	    		}
	    	}
    	}
    }
    
    /**
     * RECYCLING CENTER CODE!
     */
    @EventHandler(priority=EventPriority.LOW)
    public void onItemDespawn(ItemDespawnEvent ev) {
    	Item i = ev.getEntity();
    	//If the item is a display item, respawn it.
    	
    	if (i.getCustomName()!=null) {
    		if (hasShopSignAttached(i.getLocation().add(0,-0.5,-0.5).getBlock())) {
	    		Item e = (Item)(i.getWorld().dropItemNaturally(i.getLocation(), i.getItemStack()));
	    		e.setCustomName(i.getCustomName());
	    		e.setGlowing(i.isGlowing());
	    		e.setItemStack(i.getItemStack());
	    		e.setCustomNameVisible(i.isCustomNameVisible());
	    		e.setVelocity(new Vector(0,0,0));
	    		e.setPickupDelay(999999999);
	    		e.teleport(i.getLocation());
	    		log("Respawn this shop item.",5);
    		}
    	}
    	//There is a % chance of it going to a recycling center.
    	if (Math.random()*100<=RECYCLECHANCE &&
    			TwosideRecyclingCenter.IsItemAllowed(i.getItemStack())) {
    		//Recycle allowed. Now figure out which node to go to.
    		if (TwosideRecyclingCenter.getNumberOfNodes()>0) {
	    		Location rand_node=TwosideRecyclingCenter.getRandomNode();
	    		Block b = Bukkit.getWorld("world").getBlockAt(rand_node);
	    		if (b!=null && b.getType()==Material.CHEST ||
	    				b.getType()==Material.TRAPPED_CHEST) {
	    			if (b.getState()!=null) {
	    				Chest c = (Chest) b.getState();
	    				//Choose a random inventory slot and copy the vanished item into it.
	    				double chancer = 100.0;
	    				for (int j=0;j<27;j++) {
	    					if (c.getBlockInventory().getItem(j)!=null && c.getBlockInventory().getItem(j).getType()==i.getItemStack().getType()) {
	    						chancer-=RECYCLEDECAYAMT;
	    					}
	    				}
	    				int itemslot = (int)Math.floor(Math.random()*27);
	    				ItemStack oldItem = c.getBlockInventory().getItem(itemslot);
	    				//There is also a chance to move this item to another random spot.
	    				if (chancer>0 && Math.random()*100<chancer) {
		    		    	if (oldItem!=null && Math.random()*100<=RECYCLECHANCE) {
		        				int itemslot2 = (int)Math.floor(Math.random()*27);
		        				c.getBlockInventory().setItem(itemslot2, oldItem);
		    		    	}
		    				c.getBlockInventory().setItem(itemslot, i.getItemStack());
		    				log("Sent "+GenericFunctions.UserFriendlyMaterialName(i.getItemStack())+" to Recycling Center Node "+rand_node.toString(),3);
	    				}
	    			}
	    		}
    		} else {
    			log("No Recycling Center Nodes set! All dropped items will continue to be discarded. Use /recyclingcenter to define them.",1);
    		}
    	}
    }
    
    @EventHandler(priority=EventPriority.LOW)
    public void MonsterSpawnEvent(CreatureSpawnEvent ev) {
    	log("Reason for spawn: "+ev.getSpawnReason().toString(),5);
    	if (ev.getSpawnReason().equals(SpawnReason.NATURAL) &&
    			ev.getEntity() instanceof Monster) {
    		if (!MonsterController.MobHeightControl(ev.getEntity())) {
    			ev.setCancelled(true);
    			//This spawn was not allowed by the mob height controller.
    		}
    	}
    }
    
    //A fix to make achievemnt announcements not show the healthbar!
    @EventHandler(priority=EventPriority.LOW)
    public void playerGetAchievementEvent(PlayerAchievementAwardedEvent ev) {
    	final Player p = ev.getPlayer();
    	ev.getPlayer().getScoreboard().getTeam(ev.getPlayer().getName().toLowerCase()).setSuffix("");
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			public void run() {
				if (p!=null) {
					p.getScoreboard().getTeam(p.getName().toLowerCase()).setSuffix(createHealthbar(((p.getHealth())/p.getMaxHealth())*100,p));
				}
			}}
		,5);
    }
    
    @EventHandler(priority=EventPriority.LOW)
    public void updateHealthbarDamageEvent(EntityDamageEvent ev) {
    	Entity e = ev.getEntity();
    	if (e instanceof Player) {
    		final Player p = (Player)e;
    		if (ev.getCause()==DamageCause.ENTITY_EXPLOSION ||
    				ev.getCause()==DamageCause.BLOCK_EXPLOSION) {
    			ev.setDamage(ev.getDamage()*EXPLOSION_DMG_MULT);
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
	    					dmg=CalculateDamageReduction(dmg, pcheck, ev.getEntity());
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
    		
    		//final double pcthp = ((p.getHealth())/p.getMaxHealth())*100;
    		
    		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
    			public void run() {
    				if (p!=null) {
    					p.getScoreboard().getTeam(p.getName().toLowerCase()).setSuffix(createHealthbar(((p.getHealth())/p.getMaxHealth())*100,p));
    				}
    			}}
    		,5);
    	}
    }
    
    @EventHandler(priority=EventPriority.LOW)
    public void onEndermanTeleport(EntityTeleportEvent ev) {
    	if (ev.getEntityType()==EntityType.ENDERMAN) {
    		//There is a small chance to drop a Mysterious Essence.
    		if (Math.random()<=0.0625) {
    			ev.getEntity().getLocation().getWorld().dropItemNaturally(ev.getEntity().getLocation(), Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE));
    		}
    	}
    }
    
    @EventHandler(priority=EventPriority.LOW)
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
    	    				c.getLocation().getWorld().createExplosion(c.getLocation().getX(),c.getLocation().getY(),c.getLocation().getZ(),8.0f,false,false);
    	    			}}
    	    		,10);
    			} else 
    			if (c.getCustomName().contains("Deadly")) {
    				log("Preparing to explode.",5);
    	    		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
    	    			public void run() {
    	    				c.getLocation().getWorld().createExplosion(c.getLocation().getX(),c.getLocation().getY(),c.getLocation().getZ(),12.0f,true,false);
    	    			}}
    	    		,10);
    			} else 
    			if (c.getCustomName().contains("Hellfire")) {
    				log("Preparing to explode.",5);
    	    		c.getLocation().getWorld().playSound(c.getLocation(), Sound.ENTITY_CREEPER_PRIMED, 1.0f, 1.0f);
    				for (int i=0;i<6;i++) {
    					final int val = i;
    					final Location offset = c.getLocation().add((i==0||i==1)?(i==0)?6:-6:0,(i==2||i==3)?(i==2)?6:-6:0,(i==4||i==5)?(i==4)?6:-6:0);
	    	    		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
	    	    			public void run() {
	    	    				c.getLocation().getWorld().createExplosion(c.getLocation().getX()+offset.getX(),c.getLocation().getY()+offset.getY(),c.getLocation().getZ()+offset.getZ(),8.0f,true,false);
	    	    			}}
	    	    		,val+4);
    				}
    				Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
    	    			public void run() {
    	    				c.getLocation().getWorld().createExplosion(c.getLocation().getX(),c.getLocation().getY(),c.getLocation().getZ(),24.0f,true,false);
    	    			}}
    	    		,30);
    			}
    		}
    	}
    }
    
    @EventHandler(priority=EventPriority.LOW)
    public void expEvent(PlayerExpChangeEvent ev) {
    	double val = Math.random(); 
    	log("ExpChange event: "+val,5);
    	if (val<=0.00125) {
    		ev.getPlayer().getWorld().dropItemNaturally(ev.getPlayer().getLocation(), Artifact.createArtifactItem(ArtifactItem.MALLEABLE_BASE));
    		ev.getPlayer().sendMessage(ChatColor.LIGHT_PURPLE+"A strange item has appeared nearby.");
    	}
    }
    
    @EventHandler(priority=EventPriority.LOW)
    public void entityHitEvent(EntityDamageByEntityEvent ev) {
    	if ((ev.getDamager() instanceof LivingEntity &&
    			ev.getEntityType()==EntityType.PLAYER)) {
    		Player p = (Player)ev.getEntity();
    		LivingEntity m = (LivingEntity)ev.getDamager();
    		
    		//Calculate new damage based on armor worn.
    		//Remove all other damage modifiers since we will calculate it manually.
    		ev.setDamage(DamageModifier.BLOCKING,0);
    		ev.setDamage(DamageModifier.MAGIC,0);
    		ev.setDamage(DamageModifier.RESISTANCE,0);
    		ev.setDamage(DamageModifier.ARMOR,0);
    		
    		int dmgmult = 1;
    		
    		if (ev.getDamager() instanceof Monster &&
    				((Monster)(ev.getDamager())).getCustomName()!=null) {
    			Monster mm = (Monster)ev.getDamager();
    			if (mm.getCustomName().contains("Dangerous")) {
    				dmgmult=2;
    			} else 
    			if (mm.getCustomName().contains("Deadly")) {
    				dmgmult=3;
    			} else 
    			if (mm.getCustomName().contains("Hellfire")) {
    				dmgmult=4;
    			}
    		}
    		
    		ev.setDamage(CalculateDamageReduction(ev.getDamage()*dmgmult*ENEMY_DMG_MULT,p,m));
    		
    		log("Final dmg is "+ev.getFinalDamage(),4);
    		
    		//Make this monster the player's new target.
    		for (int i=0;i<playerdata.size();i++) {
    			PlayerStructure pd = playerdata.get(i);
    			if (pd.name.equalsIgnoreCase(p.getName())) {
    				//Found the player structure. Set the target.
    				pd.target=m;
    				if (GenericFunctions.isDefender(p)) {
    					if (pd.saturation<20) {
    						pd.saturation++;
    					}
    					int currentResistanceLevel = -1;
    					for (int j=0;j<p.getActivePotionEffects().size();j++) {
    						if (Iterables.get(p.getActivePotionEffects(), j).getType().equals(PotionEffectType.DAMAGE_RESISTANCE)) {
    							//Get the level.
    							currentResistanceLevel = Iterables.get(p.getActivePotionEffects(), j).getAmplifier();
    							p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
    							log("Resistance level is "+currentResistanceLevel,5);
    							break;
    						}
    					}
    					p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,100,(currentResistanceLevel+1<5)?currentResistanceLevel+1:4));
    				}
    				updateTitle(p);
    				break;
    			}
    		}
    	} else
    	if ((ev.getDamager() instanceof Player &&
    			ev.getEntity() instanceof LivingEntity)) {
    		final Player p = (Player)ev.getDamager();
    		final LivingEntity m = (LivingEntity)ev.getEntity();
    		
    		//Damage dealt by the player is calculated differently, therefore we will cancel the normal damage calculation in favor
    		//of a new custom damage calculation.
    		DealCalculatedDamage(p.getInventory().getItemInMainHand(),p,m);
    		if (m instanceof Monster) {
    			if (!m.hasPotionEffect(PotionEffectType.GLOWING) || GenericFunctions.isDefender(p)) {
    				if (GenericFunctions.isDefender(p)) {
    					m.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING,100,0));
    				}
    				((Monster)m).setTarget(p);
    			}
    		}
    		//ev.setCancelled(true);
    		ev.setDamage(0.01);
    		m.setNoDamageTicks(0);
    		
    		//Make this monster the player's new target.
    		for (int i=0;i<playerdata.size();i++) {
    			PlayerStructure pd = playerdata.get(i);
    			if (pd.name.equalsIgnoreCase(p.getName())) {
    				//Found the player structure. Set the target.
    				pd.target=m;
    				updateTitle(p);
    				break;
    			}
    		}
    	} else
		if ((ev.getDamager() instanceof Arrow &&
    			ev.getEntity() instanceof Player)) {
    		if (((Arrow)(ev.getDamager())).getShooter() instanceof LivingEntity) {
        		Player p = (Player)ev.getEntity();
        		LivingEntity m = (LivingEntity)((Arrow)(ev.getDamager())).getShooter();

        		//Calculate new damage based on armor worn.
        		//Remove all other damage modifiers since we will calculate it manually.
        		ev.setDamage(DamageModifier.BLOCKING,0);
        		ev.setDamage(DamageModifier.MAGIC,0);
        		ev.setDamage(DamageModifier.RESISTANCE,0);
        		ev.setDamage(DamageModifier.ARMOR,0);

        		
        		int dmgmult = 1;
        		
        		if (m instanceof Monster &&
        				((Monster)(m)).getCustomName()!=null) {
        			Monster mm = (Monster)m;
        			if (mm.getCustomName().contains("Dangerous")) {
        				dmgmult=2;
        			} else 
        			if (mm.getCustomName().contains("Deadly")) {
        				dmgmult=3;
        			} else 
        			if (mm.getCustomName().contains("Hellfire")) {
        				dmgmult=4;
        			}
        		}
        		
        		log("Original damage: "+ev.getDamage(),5);
        		log("Modified damage: "+ev.getDamage()*dmgmult*ENEMY_DMG_MULT,4);
        		
        		ev.setDamage(CalculateDamageReduction(ev.getDamage()*dmgmult*ENEMY_DMG_MULT,p,ev.getDamager()));
        		
        		
        		//Make this monster the player's new target.
        		for (int i=0;i<playerdata.size();i++) {
        			PlayerStructure pd = playerdata.get(i);
        			if (pd.name.equalsIgnoreCase(p.getName())) {
        				//Found the player structure. Set the target.
        				pd.target=m;
        				updateTitle(p);
        				break;
        			}
        		}
        	}
    	}
    	else
		if ((ev.getDamager() instanceof Arrow &&
    			ev.getEntity() instanceof LivingEntity)) {
    		if (((Arrow)(ev.getDamager())).getShooter() instanceof Player) {
	    		Player p = (Player)((Arrow)(ev.getDamager())).getShooter();
	    		LivingEntity m = (LivingEntity)ev.getEntity();
	    		if (m instanceof Monster) {
	    			((Monster)m).setTarget(p);
	    		}
	    		
	    		//Headshot detection.
	    		log("Abs() subtraction: "+(((Arrow)(ev.getDamager())).getLocation().subtract(m.getEyeLocation())).toString(),4);
	    		
	    		//Headshot conditions:
	    		/*
	    		 * X and Z have to be within abs(2).
	    		 * Y has to be within abs(0.15).
	    		 */
	    		
	    		Location arrowLoc = ((Arrow)(ev.getDamager())).getLocation();
	    		Location monsterHead = m.getEyeLocation().add(0,0.075,0);
	    		boolean headshot=false;
	    		
	    		if (ev.getDamager().getTicksLived()>=4) {
		    		if (Math.abs(arrowLoc.getY()-monsterHead.getY())<=0.165/HEADSHOT_ACC) {
		    			log("Height discrepancy is good.",5);
			    		if (Math.abs(arrowLoc.getZ()-monsterHead.getZ())<=3.0/HEADSHOT_ACC &&
			    				Math.abs(arrowLoc.getX()-monsterHead.getX())<=3.0/HEADSHOT_ACC) {
			    			ev.setDamage(ev.getDamage()*8.0);
			    			p.sendMessage(ChatColor.DARK_RED+"Headshot! x8 Damage");
			    			headshot=true;
			    		}
		    		}
	    		}
	    		
	    		//Make this monster the player's new target.
	    		for (int i=0;i<playerdata.size();i++) {
	    			PlayerStructure pd = playerdata.get(i);
	    			if (pd.name.equalsIgnoreCase(p.getName())) {
	    				//Found the player structure. Set the target.
	    				pd.target=m;
	    				updateTitle(p,headshot);
	    				break;
	    			}
	    		}
	    	}
    	} 
    	
    }
    
    @EventHandler(priority=EventPriority.LOW)
    public void monsterDeathEvent(final EntityDeathEvent ev) {
    	if (ev.getEntity() instanceof Monster) {
    		final Monster m = (Monster)ev.getEntity();
			double dropmult=1;
			if (m.getKiller() instanceof Player) {
				Player p = (Player)m.getKiller();
				for (int i=0;i<playerdata.size();i++) {
					if (playerdata.get(i).name.equalsIgnoreCase(p.getName())) {
						dropmult+=playerdata.get(i).partybonus*0.1;
						break;
					}
				}
			}
			if (Math.random()<0.00390625*dropmult) {
				ev.getDrops().add(Artifact.createArtifactItem(ArtifactItem.ARTIFACT_ESSENCE));
			}
			if (m.getType()==EntityType.ZOMBIE &&
					MonsterController.isZombieLeader(m)) {
				ev.setDroppedExp(ev.getDroppedExp()*2);
			}
			if (m.getType()==EntityType.GUARDIAN ||
					m.getType()==EntityType.SKELETON) {
				boolean allowed=false;
				if (m.getType()==EntityType.SKELETON) {
					Skeleton s = (Skeleton)m;
					if (s.getSkeletonType()==SkeletonType.WITHER) {
						allowed=true;
					}
				} else {
					allowed=true;
					if (allowed && Math.random()<0.00390625*dropmult) {
						switch ((int)(Math.random()*4)) {
							case 0:{
								ev.getDrops().add(Artifact.createArtifactItem(ArtifactItem.LOST_CORE));
							}break;
							case 1:{
								ev.getDrops().add(Artifact.createArtifactItem(ArtifactItem.ANCIENT_CORE));
							}break;
							case 2:{
							ev.getDrops().add(Artifact.createArtifactItem(ArtifactItem.ARTIFACT_CORE));
							}break;
							case 3:{
							ev.getDrops().add(Artifact.createArtifactItem(ArtifactItem.DIVINE_CORE));
							}break;
						}
					}
				}
			}
			if (m.getType()==EntityType.ENDER_DRAGON ||
					m.getType()==EntityType.WITHER) {
				if (Math.random()<0.125*dropmult) {
					switch ((int)(Math.random()*4)) {
						case 0:{
							ev.getDrops().add(Artifact.createArtifactItem(ArtifactItem.LOST_CORE));
						}break;
						case 1:{
							ev.getDrops().add(Artifact.createArtifactItem(ArtifactItem.ANCIENT_CORE));
						}break;
						case 2:{
						ev.getDrops().add(Artifact.createArtifactItem(ArtifactItem.ARTIFACT_CORE));
						}break;
						case 3:{
						ev.getDrops().add(Artifact.createArtifactItem(ArtifactItem.DIVINE_CORE));
						}break;
					}
				}
			}
    		if (m.getType()==EntityType.ENDERMAN) {
				if (Math.random()<0.00390625*dropmult) {
					switch ((int)(Math.random()*12)) {
					case 0:{
						ev.getDrops().add(Artifact.createArtifactItem(ArtifactItem.ARTIFACT_ESSENCE));
					}break;
					case 1:{
						ev.getDrops().add(Artifact.createArtifactItem(ArtifactItem.LOST_ESSENCE));
					}break;
					case 2:{
						ev.getDrops().add(Artifact.createArtifactItem(ArtifactItem.ANCIENT_ESSENCE));
					}break;
					case 3:{
						ev.getDrops().add(Artifact.createArtifactItem(ArtifactItem.DIVINE_ESSENCE));
					}break;
					case 4:{
						ev.getDrops().add(Artifact.createArtifactItem(ArtifactItem.ARTIFACT_CORE));
					}break;
					case 5:{
						ev.getDrops().add(Artifact.createArtifactItem(ArtifactItem.ANCIENT_CORE));
					}break;
					case 6:{
						ev.getDrops().add(Artifact.createArtifactItem(ArtifactItem.LOST_CORE));
					}break;
					case 7:{
						ev.getDrops().add(Artifact.createArtifactItem(ArtifactItem.DIVINE_CORE));
					}break;
					case 8:{
						ev.getDrops().add(Artifact.createArtifactItem(ArtifactItem.ARTIFACT_BASE));
					}break;
					case 9:{
						ev.getDrops().add(Artifact.createArtifactItem(ArtifactItem.ANCIENT_BASE));
					}break;
					case 10:{
						ev.getDrops().add(Artifact.createArtifactItem(ArtifactItem.LOST_BASE));
					}break;
					case 11:{
						ev.getDrops().add(Artifact.createArtifactItem(ArtifactItem.DIVINE_BASE));
					}break;
					}
				}
    		}
    		if (m.getCustomName()!=null) {
    			if (m.getCustomName().contains("Dangerous")) {
    				if (m.getKiller()!=null) { //Make sure a player actually killed this.
    					ev.setDroppedExp(ev.getDroppedExp()*4);
	    				if (Math.random()<0.5*dropmult) {
	    					ev.getDrops().add(new ItemStack(Material.IRON_INGOT));
	    				}
	    				if (Math.random()<RARE_DROP_RATE*dropmult) {
	    					ev.getDrops().add(new ItemStack(Material.IRON_BLOCK));
	    				}
	    				if (Math.random()<RARE_DROP_RATE*dropmult) {
	    					ItemStack raresword = new ItemStack(Material.STONE_SWORD);
	    					ItemMeta sword_meta = raresword.getItemMeta();
	    					sword_meta.setDisplayName(ChatColor.AQUA+""+ChatColor.BOLD+"Mega Stone Sword");
	    					if (Math.random()<0.1) {
		    					sword_meta.setDisplayName(ChatColor.AQUA+""+ChatColor.BOLD+"Hardened Mega Stone Sword");
		    					List<String> lore = new ArrayList<String>();
		    					lore.add(ChatColor.GRAY+"Breaks Remaining: "+ChatColor.YELLOW+"2");
		    					sword_meta.setLore(lore);
	    					}
	    					raresword.setItemMeta(sword_meta);
	    					raresword.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 10);
	    					raresword.addUnsafeEnchantment(Enchantment.DAMAGE_ARTHROPODS, 10);
	    					raresword.addUnsafeEnchantment(Enchantment.DAMAGE_UNDEAD, 10);
	    					ev.getDrops().add(raresword);
	    				}
	    				if (Math.random()<0.00390625*dropmult) {
	    					ev.getDrops().add(Artifact.createArtifactItem(ArtifactItem.ANCIENT_ESSENCE));
	    				}
    				}
    			}
    			if (m.getCustomName().contains("Deadly")) {
					m.getWorld().playSound(m.getLocation(), Sound.ENTITY_CREEPER_PRIMED, 1.0f, 1.0f);
    				Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
    					public void run() {
    	    				if (m.getLocation().getBlockY()<48) {
    	    					m.getWorld().createExplosion(m.getLocation().getBlockX(), m.getLocation().getBlockY(), m.getLocation().getBlockZ(), 3.0f, false, true);
    	    				} else {
    	    					m.getWorld().createExplosion(m.getLocation().getBlockX(), m.getLocation().getBlockY(), m.getLocation().getBlockZ(), 6.0f, false, false);
    	    				}
    					}}
    				,20);
    				if (m.getKiller()!=null) { //Make sure a player actually killed this.
	    				ev.setDroppedExp(ev.getDroppedExp()*8);
	    					
	    				if (Math.random()<0.5*dropmult) {
	    					//m.getWorld().dropItemNaturally(m.getLocation(), new ItemStack(Material.DIAMOND));
	    					ev.getDrops().add(new ItemStack(Material.DIAMOND));
	    				}
	    				if (Math.random()<RARE_DROP_RATE*dropmult) {
	    					//m.getWorld().dropItemNaturally(m.getLocation(), new ItemStack(Material.DIAMOND_BLOCK));
	    					ev.getDrops().add(new ItemStack(Material.DIAMOND_BLOCK));
	    				}
	    				if (Math.random()<RARE_DROP_RATE*dropmult) {
	    					ItemStack raresword = new ItemStack(Material.IRON_SWORD);
	    					ItemMeta sword_meta = raresword.getItemMeta();
	    					sword_meta.setDisplayName(ChatColor.AQUA+""+ChatColor.BOLD+"Mega Iron Sword");
	    					if (Math.random()<0.1) {
		    					sword_meta.setDisplayName(ChatColor.AQUA+""+ChatColor.BOLD+"Hardened Mega Iron Sword");
		    					List<String> lore = new ArrayList<String>();
		    					lore.add(ChatColor.GRAY+"Breaks Remaining: "+ChatColor.YELLOW+((int)(Math.random()*6)+2));
		    					sword_meta.setLore(lore);
	    					}
	    					raresword.setItemMeta(sword_meta);
	    					raresword.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 10);
	    					raresword.addUnsafeEnchantment(Enchantment.DAMAGE_ARTHROPODS, 10);
	    					raresword.addUnsafeEnchantment(Enchantment.DAMAGE_UNDEAD, 10);
	    					ev.getDrops().add(raresword);
	    				}
	    				if (Math.random()<RARE_DROP_RATE*dropmult) {
	    					ItemStack raresword = new ItemStack(Material.IRON_SWORD);
	    					ItemMeta sword_meta = raresword.getItemMeta();
	    					sword_meta.setDisplayName(ChatColor.AQUA+""+ChatColor.BOLD+"Mega Iron Sword");
	    					if (Math.random()<0.1) {
		    					sword_meta.setDisplayName(ChatColor.AQUA+""+ChatColor.BOLD+"Hardened Mega Iron Sword");
		    					List<String> lore = new ArrayList<String>();
		    					lore.add(ChatColor.GRAY+"Breaks Remaining: "+ChatColor.YELLOW+((int)(Math.random()*6)+2));
		    					sword_meta.setLore(lore);
	    					}
	    					raresword.setItemMeta(sword_meta);
	    					raresword.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 10);
	    					raresword.addUnsafeEnchantment(Enchantment.DAMAGE_ARTHROPODS, 10);
	    					raresword.addUnsafeEnchantment(Enchantment.DAMAGE_UNDEAD, 10);
	    					raresword.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
	    					ev.getDrops().add(raresword);
	    				}
	    				if (Math.random()<RARE_DROP_RATE*dropmult) {
	    					ItemStack raresword = new ItemStack(Material.IRON_CHESTPLATE);
	    					ItemMeta sword_meta = raresword.getItemMeta();
	    					sword_meta.setDisplayName(ChatColor.AQUA+""+ChatColor.BOLD+"Mega Iron Chestplate");
	    					if (Math.random()<0.1) {
		    					sword_meta.setDisplayName(ChatColor.AQUA+""+ChatColor.BOLD+"Hardened Mega Iron Chestplate");
		    					List<String> lore = new ArrayList<String>();
		    					lore.add(ChatColor.BLUE+""+ChatColor.ITALIC+"Hardened Armor");
		    					lore.add(ChatColor.GRAY+"Twice as strong");
		    					lore.add(ChatColor.GRAY+"Breaks Remaining: "+ChatColor.YELLOW+((int)(Math.random()*6)+2));
		    					sword_meta.setLore(lore);
	    					}
	    					raresword.setItemMeta(sword_meta);
	    					raresword.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 10);
	    					raresword.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 10);
	    					raresword.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 10);
	    					raresword.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, 10);
	    					raresword.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, 10);
	    					raresword.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
	    					ev.getDrops().add(raresword);
	    				}
	    				if (Math.random()<RARE_DROP_RATE*dropmult) {
	    					ItemStack raresword = new ItemStack(Material.IRON_LEGGINGS);
	    					ItemMeta sword_meta = raresword.getItemMeta();
	    					sword_meta.setDisplayName(ChatColor.AQUA+""+ChatColor.BOLD+"Mega Iron Leggings");
	    					if (Math.random()<0.1) {
		    					sword_meta.setDisplayName(ChatColor.AQUA+""+ChatColor.BOLD+"Hardened Mega Iron Leggings");
		    					List<String> lore = new ArrayList<String>();
		    					lore.add(ChatColor.BLUE+""+ChatColor.ITALIC+"Hardened Armor");
		    					lore.add(ChatColor.GRAY+"Twice as strong");
		    					lore.add(ChatColor.GRAY+"Breaks Remaining: "+ChatColor.YELLOW+((int)(Math.random()*6)+2));
		    					sword_meta.setLore(lore);
	    					}
	    					raresword.setItemMeta(sword_meta);
	    					raresword.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 10);
	    					raresword.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 10);
	    					raresword.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 10);
	    					raresword.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, 10);
	    					raresword.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, 10);
	    					raresword.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
	    					ev.getDrops().add(raresword);
	    				}
	    				if (Math.random()<RARE_DROP_RATE*dropmult) {
	    					ItemStack raresword = new ItemStack(Material.IRON_BOOTS);
	    					ItemMeta sword_meta = raresword.getItemMeta();
	    					sword_meta.setDisplayName(ChatColor.AQUA+""+ChatColor.BOLD+"Mega Iron Boots");
	    					if (Math.random()<0.1) {
		    					sword_meta.setDisplayName(ChatColor.AQUA+""+ChatColor.BOLD+"Hardened Mega Iron Boots");
		    					List<String> lore = new ArrayList<String>();
		    					lore.add(ChatColor.BLUE+""+ChatColor.ITALIC+"Hardened Armor");
		    					lore.add(ChatColor.GRAY+"Twice as strong");
		    					lore.add(ChatColor.GRAY+"Breaks Remaining: "+ChatColor.YELLOW+((int)(Math.random()*6)+2));
		    					sword_meta.setLore(lore);
	    					}
	    					raresword.setItemMeta(sword_meta);
	    					raresword.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 10);
	    					raresword.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 10);
	    					raresword.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 10);
	    					raresword.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, 10);
	    					raresword.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, 10);
	    					raresword.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
	    					ev.getDrops().add(raresword);
	    				}
	    				if (Math.random()<RARE_DROP_RATE*dropmult) {
	    					ItemStack raresword = new ItemStack(Material.IRON_HELMET);
	    					ItemMeta sword_meta = raresword.getItemMeta();
	    					sword_meta.setDisplayName(ChatColor.AQUA+""+ChatColor.BOLD+"Mega Iron Helmet");
	    					if (Math.random()<0.1) {
		    					sword_meta.setDisplayName(ChatColor.AQUA+""+ChatColor.BOLD+"Hardened Mega Iron Helmet");
		    					List<String> lore = new ArrayList<String>();
		    					lore.add(ChatColor.BLUE+""+ChatColor.ITALIC+"Hardened Armor");
		    					lore.add(ChatColor.GRAY+"Twice as strong");
		    					lore.add(ChatColor.GRAY+"Breaks Remaining: "+ChatColor.YELLOW+((int)(Math.random()*6)+2));
		    					sword_meta.setLore(lore);
	    					}
	    					raresword.setItemMeta(sword_meta);
	    					raresword.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 10);
	    					raresword.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 10);
	    					raresword.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 10);
	    					raresword.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, 10);
	    					raresword.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, 10);
	    					raresword.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
	    					ev.getDrops().add(raresword);
	    				}
	    				if (Math.random()<RARE_DROP_RATE*dropmult) {
	    					ItemStack raresword = new ItemStack(Material.IRON_SPADE);
	    					ItemMeta sword_meta = raresword.getItemMeta();
	    					sword_meta.setDisplayName(ChatColor.AQUA+""+ChatColor.BOLD+"Mega Iron Shovel");
	    					if (Math.random()<0.1) {
		    					sword_meta.setDisplayName(ChatColor.AQUA+""+ChatColor.BOLD+"Hardened Mega Iron Shovel");
		    					List<String> lore = new ArrayList<String>();
		    					lore.add(ChatColor.GRAY+"Breaks Remaining: "+ChatColor.YELLOW+((int)(Math.random()*6)+2));
		    					sword_meta.setLore(lore);
	    					}
	    					raresword.setItemMeta(sword_meta);
	    					raresword.addUnsafeEnchantment(Enchantment.DIG_SPEED, 10);
	    					raresword.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 10);
	    					raresword.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 10);
	    					raresword.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
	    					raresword.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
	    					ev.getDrops().add(raresword);
	    				}
	    				if (Math.random()<RARE_DROP_RATE*dropmult) {
	    					ItemStack raresword = new ItemStack(Material.IRON_AXE);
	    					ItemMeta sword_meta = raresword.getItemMeta();
	    					sword_meta.setDisplayName(ChatColor.AQUA+""+ChatColor.BOLD+"Mega Iron Axe");
	    					if (Math.random()<0.1) {
		    					sword_meta.setDisplayName(ChatColor.AQUA+""+ChatColor.BOLD+"Hardened Mega Iron Axe");
		    					List<String> lore = new ArrayList<String>();
		    					lore.add(ChatColor.GRAY+"Breaks Remaining: "+ChatColor.YELLOW+((int)(Math.random()*6)+2));
		    					sword_meta.setLore(lore);
	    					}
	    					raresword.setItemMeta(sword_meta);
	    					raresword.addUnsafeEnchantment(Enchantment.DIG_SPEED, 10);
	    					raresword.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 10);
	    					raresword.addUnsafeEnchantment(Enchantment.LOOT_BONUS_MOBS, 10);
	    					raresword.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
	    					raresword.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
	    					ev.getDrops().add(raresword);
	    				}
	    				if (Math.random()<RARE_DROP_RATE*dropmult) {
	    					ItemStack raresword = new ItemStack(Material.IRON_PICKAXE);
	    					ItemMeta sword_meta = raresword.getItemMeta();
	    					sword_meta.setDisplayName(ChatColor.AQUA+""+ChatColor.BOLD+"Mega Iron Pickaxe");
	    					if (Math.random()<0.1) {
		    					sword_meta.setDisplayName(ChatColor.AQUA+""+ChatColor.BOLD+"Hardened Mega Iron Pickaxe");
		    					List<String> lore = new ArrayList<String>();
		    					lore.add(ChatColor.GRAY+"Breaks Remaining: "+ChatColor.YELLOW+((int)(Math.random()*6)+2));
		    					sword_meta.setLore(lore);
	    					}
	    					raresword.setItemMeta(sword_meta);
	    					raresword.addUnsafeEnchantment(Enchantment.DIG_SPEED, 10);
	    					raresword.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 10);
	    					raresword.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 10);
	    					raresword.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
	    					raresword.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
	    					ev.getDrops().add(raresword);
	    				}
	    				if (Math.random()<RARE_DROP_RATE*dropmult) {
	    					ItemStack raresword = new ItemStack(Material.IRON_HOE);
	    					ItemMeta sword_meta = raresword.getItemMeta();
	    					sword_meta.setDisplayName(ChatColor.AQUA+""+ChatColor.BOLD+"Mega Iron Hoe");
	    					if (Math.random()<0.1) {
		    					sword_meta.setDisplayName(ChatColor.AQUA+""+ChatColor.BOLD+"Hardened Mega Iron Hoe");
		    					List<String> lore = new ArrayList<String>();
		    					lore.add(ChatColor.GRAY+"Breaks Remaining: "+ChatColor.YELLOW+((int)(Math.random()*6)+2));
		    					sword_meta.setLore(lore);
	    					}
	    					raresword.setItemMeta(sword_meta);
	    					raresword.addUnsafeEnchantment(Enchantment.DIG_SPEED, 10);
	    					raresword.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 10);
	    					raresword.addUnsafeEnchantment(Enchantment.LOOT_BONUS_MOBS, 10);
	    					raresword.addUnsafeEnchantment(Enchantment.KNOCKBACK, 10);
	    					raresword.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
	    					ev.getDrops().add(raresword);
	    				}
	    				if (Math.random()<0.00390625*dropmult) {
	    					ev.getDrops().add(Artifact.createArtifactItem(ArtifactItem.LOST_ESSENCE));
	    				}
	    				final List<ItemStack> drops = new ArrayList<ItemStack>();
	    				drops.addAll(ev.getDrops());
	    				ev.getDrops().clear();
	    				Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
	    					public void run() {
	    						for (int i=0;i<drops.size();i++) {
	    							m.getWorld().dropItemNaturally(m.getLocation(), drops.get(i));
	    						}
	    					}}
	    				,40);
    				}
    			}
    			if (m.getCustomName().contains("Hellfire")) {
					m.getWorld().playSound(m.getLocation(), Sound.ENTITY_CREEPER_PRIMED, 1.0f, 1.0f);
    				Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
    					public void run() {
    	    				if (m.getLocation().getBlockY()<32) {
    	    					m.getWorld().createExplosion(m.getLocation().getBlockX(), m.getLocation().getBlockY(), m.getLocation().getBlockZ(), 5.0f, true, true);
    	    				} else {
    	    					m.getWorld().createExplosion(m.getLocation().getBlockX(), m.getLocation().getBlockY(), m.getLocation().getBlockZ(), 5.0f, true, false);
    	    				}
    					}}
    				,20);
    				if (m.getKiller()!=null) { //Make sure a player actually killed this.
	    				ev.setDroppedExp(ev.getDroppedExp()*20);
						for (int j=0;j<3;j++) {
		    				if (Math.random()<0.5*dropmult) {
		    					/*
								m.getWorld().dropItemNaturally(m.getLocation(), new ItemStack(Material.DIAMOND));
								m.getWorld().dropItemNaturally(m.getLocation(), new ItemStack(Material.GOLD_INGOT));
								m.getWorld().dropItemNaturally(m.getLocation(), new ItemStack(Material.EMERALD));*/
		    					ev.getDrops().add(new ItemStack(Material.DIAMOND));
		    					ev.getDrops().add(new ItemStack(Material.GOLD_INGOT));
		    					ev.getDrops().add(new ItemStack(Material.EMERALD));
		    				}
		    				if (Math.random()<0.5*dropmult) {
		    					//m.getWorld().dropItemNaturally(m.getLocation(), new ItemStack(Material.DIAMOND));
		    					ev.getDrops().add(new ItemStack(Material.DIAMOND));
		    				}
		    				if (Math.random()<0.5*dropmult) {
		    					//m.getWorld().dropItemNaturally(m.getLocation(), new ItemStack(Material.DIAMOND));
		    					ev.getDrops().add(new ItemStack(Material.EMERALD));
		    				}
		    				if (Math.random()<0.5*dropmult) {
		    					//m.getWorld().dropItemNaturally(m.getLocation(), new ItemStack(Material.DIAMOND));
		    					ev.getDrops().add(new ItemStack(Material.GOLD_INGOT));
		    				}
		    				if (Math.random()<RARE_DROP_RATE*dropmult) {
		    					//m.getWorld().dropItemNaturally(m.getLocation(), new ItemStack(Material.DIAMOND_BLOCK));
		    					ev.getDrops().add(new ItemStack(Material.DIAMOND_BLOCK));
		    				}
		    				if (Math.random()<RARE_DROP_RATE*dropmult) {
		    					//m.getWorld().dropItemNaturally(m.getLocation(), new ItemStack(Material.EMERALD_BLOCK));
		    					ev.getDrops().add(new ItemStack(Material.EMERALD_BLOCK));
		    				}
		    				if (Math.random()<RARE_DROP_RATE*dropmult) {
		    					//m.getWorld().dropItemNaturally(m.getLocation(), new ItemStack(Material.GOLD_BLOCK));
		    					ev.getDrops().add(new ItemStack(Material.GOLD_BLOCK));
		    				}
		    				if (Math.random()<RARE_DROP_RATE*dropmult) {
		    					ItemStack raresword = new ItemStack(Material.DIAMOND_SWORD);
		    					ItemMeta sword_meta = raresword.getItemMeta();
		    					sword_meta.setDisplayName(ChatColor.AQUA+""+ChatColor.BOLD+"Mega Diamond Sword");
		    					if (Math.random()<0.1) {
			    					sword_meta.setDisplayName(ChatColor.AQUA+""+ChatColor.BOLD+"Hardened Mega Diamond Sword");
			    					List<String> lore = new ArrayList<String>();
			    					lore.add(ChatColor.GRAY+"Breaks Remaining: "+ChatColor.YELLOW+((int)(Math.random()*7)+4));
			    					sword_meta.setLore(lore);
		    					}
		    					raresword.setItemMeta(sword_meta);
		    					raresword.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 10);
		    					raresword.addUnsafeEnchantment(Enchantment.DAMAGE_ARTHROPODS, 10);
		    					raresword.addUnsafeEnchantment(Enchantment.DAMAGE_UNDEAD, 10);
		    					raresword.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
		    					ev.getDrops().add(raresword);
		    				}
		    				if (Math.random()<RARE_DROP_RATE*dropmult) {
		    					ItemStack raresword = new ItemStack(Material.DIAMOND_CHESTPLATE);
		    					ItemMeta sword_meta = raresword.getItemMeta();
		    					sword_meta.setDisplayName(ChatColor.AQUA+""+ChatColor.BOLD+"Mega Diamond Chestplate");
		    					if (Math.random()<0.1) {
			    					sword_meta.setDisplayName(ChatColor.AQUA+""+ChatColor.BOLD+"Hardened Mega Diamond Chestplate");
			    					List<String> lore = new ArrayList<String>();
			    					lore.add(ChatColor.BLUE+""+ChatColor.ITALIC+"Hardened Armor");
			    					lore.add(ChatColor.GRAY+"Twice as strong");
			    					lore.add(ChatColor.GRAY+"Breaks Remaining: "+ChatColor.YELLOW+((int)(Math.random()*7)+4));
			    					sword_meta.setLore(lore);
		    					}
		    					raresword.setItemMeta(sword_meta);
		    					raresword.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 10);
		    					raresword.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 10);
		    					raresword.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 10);
		    					raresword.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, 10);
		    					raresword.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, 10);
		    					raresword.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
		    					ev.getDrops().add(raresword);
		    				}
		    				if (Math.random()<RARE_DROP_RATE*dropmult) {
		    					ItemStack raresword = new ItemStack(Material.DIAMOND_LEGGINGS);
		    					ItemMeta sword_meta = raresword.getItemMeta();
		    					sword_meta.setDisplayName(ChatColor.AQUA+""+ChatColor.BOLD+"Mega Diamond Leggings");
		    					if (Math.random()<0.1) {
			    					sword_meta.setDisplayName(ChatColor.AQUA+""+ChatColor.BOLD+"Hardened Mega Diamond Leggings");
			    					List<String> lore = new ArrayList<String>();
			    					lore.add(ChatColor.BLUE+""+ChatColor.ITALIC+"Hardened Armor");
			    					lore.add(ChatColor.GRAY+"Twice as strong");
			    					lore.add(ChatColor.GRAY+"Breaks Remaining: "+ChatColor.YELLOW+((int)(Math.random()*7)+4));
			    					sword_meta.setLore(lore);
		    					}
		    					raresword.setItemMeta(sword_meta);
		    					raresword.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 10);
		    					raresword.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 10);
		    					raresword.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 10);
		    					raresword.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, 10);
		    					raresword.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, 10);
		    					raresword.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
		    					ev.getDrops().add(raresword);
		    				}
		    				if (Math.random()<RARE_DROP_RATE*dropmult) {
		    					ItemStack raresword = new ItemStack(Material.DIAMOND_BOOTS);
		    					ItemMeta sword_meta = raresword.getItemMeta();
		    					sword_meta.setDisplayName(ChatColor.AQUA+""+ChatColor.BOLD+"Mega Diamond Boots");
		    					if (Math.random()<0.1) {
			    					sword_meta.setDisplayName(ChatColor.AQUA+""+ChatColor.BOLD+"Hardened Mega Diamond Boots");
			    					List<String> lore = new ArrayList<String>();
			    					lore.add(ChatColor.BLUE+""+ChatColor.ITALIC+"Hardened Armor");
			    					lore.add(ChatColor.GRAY+"Twice as strong");
			    					lore.add(ChatColor.GRAY+"Breaks Remaining: "+ChatColor.YELLOW+((int)(Math.random()*7)+4));
			    					sword_meta.setLore(lore);
		    					}
		    					raresword.setItemMeta(sword_meta);
		    					raresword.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 10);
		    					raresword.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 10);
		    					raresword.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 10);
		    					raresword.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, 10);
		    					raresword.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, 10);
		    					raresword.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
		    					ev.getDrops().add(raresword);
		    				}
		    				if (Math.random()<RARE_DROP_RATE*dropmult) {
		    					ItemStack raresword = new ItemStack(Material.DIAMOND_HELMET);
		    					ItemMeta sword_meta = raresword.getItemMeta();
		    					sword_meta.setDisplayName(ChatColor.AQUA+""+ChatColor.BOLD+"Mega Diamond Helmet");
		    					if (Math.random()<0.1) {
			    					sword_meta.setDisplayName(ChatColor.AQUA+""+ChatColor.BOLD+"Hardened Mega Diamond Helmet");
			    					List<String> lore = new ArrayList<String>();
			    					lore.add(ChatColor.BLUE+""+ChatColor.ITALIC+"Hardened Armor");
			    					lore.add(ChatColor.GRAY+"Twice as strong");
			    					lore.add(ChatColor.GRAY+"Breaks Remaining: "+ChatColor.YELLOW+((int)(Math.random()*7)+4));
			    					sword_meta.setLore(lore);
		    					}
		    					raresword.setItemMeta(sword_meta);
		    					raresword.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 10);
		    					raresword.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 10);
		    					raresword.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 10);
		    					raresword.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, 10);
		    					raresword.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, 10);
		    					raresword.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
		    					ev.getDrops().add(raresword);
		    				}
		    				if (Math.random()<RARE_DROP_RATE*dropmult) {
		    					ItemStack raresword = new ItemStack(Material.GOLD_CHESTPLATE);
		    					ItemMeta sword_meta = raresword.getItemMeta();
		    					sword_meta.setDisplayName(ChatColor.AQUA+""+ChatColor.BOLD+"Mega Gold Chestplate");
		    					if (Math.random()<0.1) {
			    					sword_meta.setDisplayName(ChatColor.AQUA+""+ChatColor.BOLD+"Hardened Mega Gold Chestplate");
			    					List<String> lore = new ArrayList<String>();
			    					lore.add(ChatColor.BLUE+""+ChatColor.ITALIC+"Hardened Armor");
			    					lore.add(ChatColor.GRAY+"Twice as strong");
			    					lore.add(ChatColor.GRAY+"Breaks Remaining: "+ChatColor.YELLOW+((int)(Math.random()*14)+8));
			    					sword_meta.setLore(lore);
		    					}
		    					raresword.setItemMeta(sword_meta);
		    					raresword.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 10);
		    					raresword.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 10);
		    					raresword.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 10);
		    					raresword.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, 10);
		    					raresword.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, 10);
		    					raresword.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
		    					ev.getDrops().add(raresword);
		    				}
		    				if (Math.random()<RARE_DROP_RATE*dropmult) {
		    					ItemStack raresword = new ItemStack(Material.GOLD_LEGGINGS);
		    					ItemMeta sword_meta = raresword.getItemMeta();
		    					sword_meta.setDisplayName(ChatColor.AQUA+""+ChatColor.BOLD+"Mega Gold Leggings");
		    					if (Math.random()<0.1) {
			    					sword_meta.setDisplayName(ChatColor.AQUA+""+ChatColor.BOLD+"Hardened Mega Gold Leggings");
			    					List<String> lore = new ArrayList<String>();
			    					lore.add(ChatColor.BLUE+""+ChatColor.ITALIC+"Hardened Armor");
			    					lore.add(ChatColor.GRAY+"Twice as strong");
			    					lore.add(ChatColor.GRAY+"Breaks Remaining: "+ChatColor.YELLOW+((int)(Math.random()*14)+8));
			    					sword_meta.setLore(lore);
		    					}
		    					raresword.setItemMeta(sword_meta);
		    					raresword.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 10);
		    					raresword.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 10);
		    					raresword.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 10);
		    					raresword.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, 10);
		    					raresword.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, 10);
		    					raresword.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
		    					ev.getDrops().add(raresword);
		    				}
		    				if (Math.random()<RARE_DROP_RATE*dropmult) {
		    					ItemStack raresword = new ItemStack(Material.GOLD_BOOTS);
		    					ItemMeta sword_meta = raresword.getItemMeta();
		    					sword_meta.setDisplayName(ChatColor.AQUA+""+ChatColor.BOLD+"Mega Gold Boots");
		    					if (Math.random()<0.1) {
			    					sword_meta.setDisplayName(ChatColor.AQUA+""+ChatColor.BOLD+"Hardened Mega Gold Boots");
			    					List<String> lore = new ArrayList<String>();
			    					lore.add(ChatColor.BLUE+""+ChatColor.ITALIC+"Hardened Armor");
			    					lore.add(ChatColor.GRAY+"Twice as strong");
			    					lore.add(ChatColor.GRAY+"Breaks Remaining: "+ChatColor.YELLOW+((int)(Math.random()*14)+8));
			    					sword_meta.setLore(lore);
		    					}
		    					raresword.setItemMeta(sword_meta);
		    					raresword.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 10);
		    					raresword.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 10);
		    					raresword.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 10);
		    					raresword.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, 10);
		    					raresword.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, 10);
		    					raresword.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
		    					ev.getDrops().add(raresword);
		    				}
		    				if (Math.random()<RARE_DROP_RATE*dropmult) {
		    					ItemStack raresword = new ItemStack(Material.GOLD_HELMET);
		    					ItemMeta sword_meta = raresword.getItemMeta();
		    					sword_meta.setDisplayName(ChatColor.AQUA+""+ChatColor.BOLD+"Mega Gold Helmet");
		    					if (Math.random()<0.1) {
			    					sword_meta.setDisplayName(ChatColor.AQUA+""+ChatColor.BOLD+"Hardened Mega Gold Helmet");
			    					List<String> lore = new ArrayList<String>();
			    					lore.add(ChatColor.BLUE+""+ChatColor.ITALIC+"Hardened Armor");
			    					lore.add(ChatColor.GRAY+"Twice as strong");
			    					lore.add(ChatColor.GRAY+"Breaks Remaining: "+ChatColor.YELLOW+((int)(Math.random()*14)+8));
			    					sword_meta.setLore(lore);
		    					}
		    					raresword.setItemMeta(sword_meta);
		    					raresword.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 10);
		    					raresword.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 10);
		    					raresword.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 10);
		    					raresword.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, 10);
		    					raresword.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, 10);
		    					raresword.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
		    					ev.getDrops().add(raresword);
		    				}
		    				if (Math.random()<RARE_DROP_RATE*dropmult) {
		    					ItemStack raresword = new ItemStack(Material.GOLD_SWORD);
		    					ItemMeta sword_meta = raresword.getItemMeta();
		    					sword_meta.setDisplayName(ChatColor.AQUA+""+ChatColor.BOLD+"Mega Gold Sword");
		    					if (Math.random()<0.1) {
			    					sword_meta.setDisplayName(ChatColor.AQUA+""+ChatColor.BOLD+"Hardened Mega Gold Sword");
			    					List<String> lore = new ArrayList<String>();
			    					lore.add(ChatColor.GRAY+"Breaks Remaining: "+ChatColor.YELLOW+((int)(Math.random()*14)+8));
			    					sword_meta.setLore(lore);
		    					}
		    					raresword.setItemMeta(sword_meta);
		    					raresword.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 10);
		    					raresword.addUnsafeEnchantment(Enchantment.DAMAGE_ARTHROPODS, 10);
		    					raresword.addUnsafeEnchantment(Enchantment.DAMAGE_UNDEAD, 10);
		    					raresword.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
		    					ev.getDrops().add(raresword);
		    				}

		    				if (Math.random()<RARE_DROP_RATE*dropmult) {
		    					ItemStack raresword = new ItemStack(Material.DIAMOND_SPADE);
		    					ItemMeta sword_meta = raresword.getItemMeta();
		    					sword_meta.setDisplayName(ChatColor.AQUA+""+ChatColor.BOLD+"Mega Diamond Shovel");
		    					if (Math.random()<0.1) {
			    					sword_meta.setDisplayName(ChatColor.AQUA+""+ChatColor.BOLD+"Hardened Mega Diamond Shovel");
			    					List<String> lore = new ArrayList<String>();
			    					lore.add(ChatColor.GRAY+"Breaks Remaining: "+ChatColor.YELLOW+((int)(Math.random()*6)+2));
			    					sword_meta.setLore(lore);
		    					}
		    					raresword.setItemMeta(sword_meta);
		    					raresword.addUnsafeEnchantment(Enchantment.DIG_SPEED, 10);
		    					raresword.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 10);
		    					raresword.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 10);
		    					raresword.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
		    					raresword.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
		    					ev.getDrops().add(raresword);
		    				}
		    				if (Math.random()<RARE_DROP_RATE*dropmult) {
		    					ItemStack raresword = new ItemStack(Material.DIAMOND_AXE);
		    					ItemMeta sword_meta = raresword.getItemMeta();
		    					sword_meta.setDisplayName(ChatColor.AQUA+""+ChatColor.BOLD+"Mega Diamond Axe");
		    					if (Math.random()<0.1) {
			    					sword_meta.setDisplayName(ChatColor.AQUA+""+ChatColor.BOLD+"Hardened Mega Diamond Axe");
			    					List<String> lore = new ArrayList<String>();
			    					lore.add(ChatColor.GRAY+"Breaks Remaining: "+ChatColor.YELLOW+((int)(Math.random()*6)+2));
			    					sword_meta.setLore(lore);
		    					}
		    					raresword.setItemMeta(sword_meta);
		    					raresword.addUnsafeEnchantment(Enchantment.DIG_SPEED, 10);
		    					raresword.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 10);
		    					raresword.addUnsafeEnchantment(Enchantment.LOOT_BONUS_MOBS, 10);
		    					raresword.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
		    					raresword.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
		    					ev.getDrops().add(raresword);
		    				}
		    				if (Math.random()<RARE_DROP_RATE*dropmult) {
		    					ItemStack raresword = new ItemStack(Material.DIAMOND_PICKAXE);
		    					ItemMeta sword_meta = raresword.getItemMeta();
		    					sword_meta.setDisplayName(ChatColor.AQUA+""+ChatColor.BOLD+"Mega Diamond Pickaxe");
		    					if (Math.random()<0.1) {
			    					sword_meta.setDisplayName(ChatColor.AQUA+""+ChatColor.BOLD+"Hardened Mega Diamond Pickaxe");
			    					List<String> lore = new ArrayList<String>();
			    					lore.add(ChatColor.GRAY+"Breaks Remaining: "+ChatColor.YELLOW+((int)(Math.random()*6)+2));
			    					sword_meta.setLore(lore);
		    					}
		    					raresword.setItemMeta(sword_meta);
		    					raresword.addUnsafeEnchantment(Enchantment.DIG_SPEED, 10);
		    					raresword.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 10);
		    					raresword.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 10);
		    					raresword.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
		    					raresword.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
		    					ev.getDrops().add(raresword);
		    				}
		    				if (Math.random()<RARE_DROP_RATE*dropmult) {
		    					ItemStack raresword = new ItemStack(Material.DIAMOND_HOE);
		    					ItemMeta sword_meta = raresword.getItemMeta();
		    					sword_meta.setDisplayName(ChatColor.AQUA+""+ChatColor.BOLD+"Mega Diamond Hoe");
		    					if (Math.random()<0.1) {
			    					sword_meta.setDisplayName(ChatColor.AQUA+""+ChatColor.BOLD+"Hardened Mega Diamond Hoe");
			    					List<String> lore = new ArrayList<String>();
			    					lore.add(ChatColor.GRAY+"Breaks Remaining: "+ChatColor.YELLOW+((int)(Math.random()*6)+2));
			    					sword_meta.setLore(lore);
		    					}
		    					raresword.setItemMeta(sword_meta);
		    					raresword.addUnsafeEnchantment(Enchantment.DIG_SPEED, 10);
		    					raresword.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 10);
		    					raresword.addUnsafeEnchantment(Enchantment.LOOT_BONUS_MOBS, 10);
		    					raresword.addUnsafeEnchantment(Enchantment.KNOCKBACK, 10);
		    					raresword.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
		    					ev.getDrops().add(raresword);
		    				}
		    				if (Math.random()<RARE_DROP_RATE*dropmult) {
		    					ItemStack raresword = new ItemStack(Material.BOW);
		    					ItemMeta sword_meta = raresword.getItemMeta();
		    					sword_meta.setDisplayName(ChatColor.AQUA+""+ChatColor.BOLD+"Mega Bow");
		    					if (Math.random()<0.1) {
			    					sword_meta.setDisplayName(ChatColor.AQUA+""+ChatColor.BOLD+"Hardened Mega Bow");
			    					List<String> lore = new ArrayList<String>();
			    					lore.add(ChatColor.GRAY+"Breaks Remaining: "+ChatColor.YELLOW+((int)(Math.random()*7)+4));
			    					sword_meta.setLore(lore);
		    					}
		    					raresword.setItemMeta(sword_meta);
		    					raresword.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 10);
		    					raresword.addUnsafeEnchantment(Enchantment.ARROW_KNOCKBACK, 2);
		    					raresword.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
		    					ev.getDrops().add(raresword);
		    				}
		    				if (Math.random()<0.00390625*dropmult) {
		    					ev.getDrops().add(Artifact.createArtifactItem(ArtifactItem.DIVINE_ESSENCE));
		    				}
						}
	    				final List<ItemStack> drops = new ArrayList<ItemStack>();
	    				drops.addAll(ev.getDrops());
	    				ev.getDrops().clear();
	    				Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
	    					public void run() {
	    						for (int i=0;i<drops.size();i++) {
	    							m.getWorld().dropItemNaturally(m.getLocation(), drops.get(i));
	    						}
	    					}}
	    				,40);
    				}
    			}
    		}
    	}
    }
    
    @EventHandler(priority=EventPriority.LOW)
    public void updateHealthbarRespawnEvent(PlayerRespawnEvent ev) {
    	final Player p = ev.getPlayer();
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			public void run() {
				if (p!=null) {
					p.getScoreboard().getTeam(p.getName().toLowerCase()).setSuffix(createHealthbar(((p.getHealth())/p.getMaxHealth())*100,p));
					setPlayerMaxHealth(p);
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
			}
		},20);
    }
    
    @EventHandler(priority=EventPriority.LOW)
    public void updateHealthbarHealEvent(EntityRegainHealthEvent ev) {
    	Entity e = ev.getEntity();
    	if (e instanceof Player) {
    		final Player p = (Player)e;
    		//final double pcthp = ((p.getHealth())/p.getMaxHealth())*100;
    		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
    			public void run() {
    				if (p!=null) {
    					p.getScoreboard().getTeam(p.getName().toLowerCase()).setSuffix(createHealthbar(((p.getHealth())/p.getMaxHealth())*100,p));
    				}
    			}}
    		,2);
    	}
    }

    @EventHandler(priority=EventPriority.LOW)
    public void onBrokenItem(PlayerItemBreakEvent ev) {
    	//When an item breaks, check if it has the ChatColor.GRAY+"Breaks Remaining: " line.
    	//If it does, that means it can still be alive longer and not break.
    	Player p = ev.getPlayer();
    	ItemStack item = ev.getBrokenItem();
    	//See if this item has lore.
    	if (GenericFunctions.getHardenedItemBreaks(item)>0) {
    		//item.setAmount(1);
    		GenericFunctions.breakHardenedItem(item);
    	} else
    	{
    		p.sendMessage(ChatColor.DARK_RED+"Your "+ChatColor.YELLOW+GenericFunctions.UserFriendlyMaterialName(item)+ChatColor.DARK_RED+" has broken!");
    	}

    }
    
    @EventHandler(priority=EventPriority.LOW)
    public void onBlockBreak(BlockBreakEvent ev) {
    	
    	TwosideSpleefGames.PassEvent(ev);
    	
    	Player p = ev.getPlayer();
    	if (p!=null) {
    		log(p.getName()+" has broken block "+GenericFunctions.UserFriendlyMaterialName(new ItemStack(ev.getBlock().getType())),3);
    	}
    	
    	if (ev.getBlock().getType()==Material.WALL_SIGN) {
    		//We're going to make sure if it's a shop or not.
    		Sign s = (Sign)(ev.getBlock().getState());
    		if (s.getLine(0).equalsIgnoreCase(ChatColor.BLUE+"-- SHOP --")) {
    			//This is a shop. Let's find out who the owner is.
    			int shopID = TwosideShops.GetShopID(s);
    			WorldShop shop = TwosideShops.LoadWorldShopData(shopID);
    			String owner = shop.GetOwner();
    			if (owner.equalsIgnoreCase(p.getName()) || p.isOp()) {
    				//We are going to see if this shop had items in it.
    				if (shop.GetAmount()>0) {
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
    				}
    				//Remove the itemstack that represented this item.
    				Collection<Entity> nearby = ev.getPlayer().getWorld().getNearbyEntities(ev.getBlock().getLocation(), 3, 3, 3);
    				for (int i=0;i<nearby.size();i++) {
    					Entity e = Iterables.get(nearby, i);
    					if (e.getType()==EntityType.DROPPED_ITEM) {
    						log("Found a drop.",5);
    						Item it = (Item)e;
    						if (it.getItemStack().getType()==shop.GetItem().getType() &&
    								Artifact.isArtifact(it.getItemStack())) {
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
    	} else {
    		//Make sure there's no world sign on this block.
    		if (hasShopSignAttached(ev.getBlock())) {
    			//Do not allow this. The shop signs have to be destroyed first!
				p.sendMessage("This block has shops on it! The shops must be destroyed before you can break this block!");
				ev.setCancelled(true);
    		}
    	}
    }
    
    @EventHandler(priority=EventPriority.LOW)
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
    				p.getEquipment().getBoots()==null) {
    			p.getEquipment().setBoots(armor);
    			p.sendMessage(ChatColor.DARK_AQUA+"Automatically equipped "+ChatColor.YELLOW+GenericFunctions.UserFriendlyMaterialName(ev.getItem().getItemStack()));
    			ev.getItem().remove();
    			ev.setCancelled(true);
    		} else
    		if (armor.getType().toString().contains("LEGGINGS") &&
    				p.getEquipment().getLeggings()==null) {
    			p.getEquipment().setLeggings(armor);
    			p.sendMessage(ChatColor.DARK_AQUA+"Automatically equipped "+ChatColor.YELLOW+GenericFunctions.UserFriendlyMaterialName(ev.getItem().getItemStack()));
    			ev.getItem().remove();
    			ev.setCancelled(true);
    		} else
    		if (armor.getType().toString().contains("CHESTPLATE") &&
    				p.getEquipment().getChestplate()==null) {
    			p.getEquipment().setChestplate(armor);
    			p.sendMessage(ChatColor.DARK_AQUA+"Automatically equipped "+ChatColor.YELLOW+GenericFunctions.UserFriendlyMaterialName(ev.getItem().getItemStack()));
    			ev.getItem().remove();
    			ev.setCancelled(true);
    		} else
    		if (armor.getType().toString().contains("HELMET") &&
    				p.getEquipment().getHelmet()==null) {
    			p.getEquipment().setHelmet(armor);
    			p.sendMessage(ChatColor.DARK_AQUA+"Automatically equipped "+ChatColor.YELLOW+GenericFunctions.UserFriendlyMaterialName(ev.getItem().getItemStack()));
    			ev.getItem().remove();
    			ev.setCancelled(true);
    		} else
    		if (armor.getType().toString().contains("SHIELD") &&
    				p.getEquipment().getItemInOffHand()==null) {
    			p.getEquipment().setItemInOffHand(armor);
    			p.sendMessage(ChatColor.DARK_AQUA+"Automatically equipped "+ChatColor.YELLOW+GenericFunctions.UserFriendlyMaterialName(ev.getItem().getItemStack()));
    			ev.getItem().remove();
    			ev.setCancelled(true);
    		}
    	}
    }
    
    @EventHandler(priority=EventPriority.LOW)
    public void onArrowShot(EntityShootBowEvent ev) {
    	//Check if it's a player.
    	if (ev.getEntityType()==EntityType.PLAYER &&
    			(ev.getProjectile().getType()==EntityType.ARROW ||
    			ev.getProjectile().getType()==EntityType.TIPPED_ARROW)) {
    		//Now we know this is a player who shot a regular old arrow.
    		//We need to give one back to them.
    		final Player p = (Player)ev.getEntity();
    		if (ev.getProjectile().getType()==EntityType.ARROW) {
    			//This was an arrow quiver. We need to verify that, check the player's inventory for one.
    			//Then queue a delayed event to add it back in if it's gone next tick.
    			if (playerHasArrowQuiver(p)) {
        			log("A tipped arrow was shot. This could've been the arrow quiver. We will verify in 5 ticks.",5);
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
        			,5);
    			}
    		}
    		
    	}
    }
    
    
    @EventHandler(priority=EventPriority.LOW)
    public void onItemCraftEvent(PrepareItemCraftEvent ev) {
    	ItemStack result = ev.getInventory().getResult();
    	if (result.getType()==Material.TNT) {
    		result.setAmount(result.getAmount()*5); //TNT recipes are 5 times as effective.
    	}
    }
    
    @EventHandler(priority=EventPriority.LOW)
    public void MinecartExitEvent(VehicleExitEvent ev) {
    	if (ev.getExited() instanceof Player &&
    			ev.getVehicle().getType()==EntityType.MINECART) {
    		Player p = (Player)(ev.getExited());
    		//p.sendMessage("Off.");
    		//Drop a minecart at the position.
    		ev.getVehicle().getWorld().dropItemNaturally(ev.getVehicle().getLocation().add(0,1,0), new ItemStack(Material.MINECART));
    		ev.getVehicle().remove();
    	}
    }
    
    @EventHandler(priority=EventPriority.LOW)
    public void onTeleportEvent(PlayerTeleportEvent ev) {
    	if (ev.getCause().equals(TeleportCause.END_PORTAL)) {
	    	Player p = ev.getPlayer();
	    	for (int i=0;i<playerdata.size();i++) {
	    		PlayerStructure pd = playerdata.get(i);
	    		if (pd.name.equalsIgnoreCase(p.getName())) {
	    			//This is the player data structure we are looking for.
	    			if (!pd.enderdragon_spawned) {
	    				pd.enderdragon_spawned=true;
	    				//Spawn an ender dragon...
	    				EnderDragon dragon = (EnderDragon)(ev.getTo().getWorld().spawnEntity(ev.getTo().add(new Location(ev.getTo().getWorld(),0,64,0)), EntityType.ENDER_DRAGON));
	    				dragon.setPhase(Phase.CIRCLING);
	    			}
	    		}
	    	}
    	}
    	final Player p = ev.getPlayer();
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			public void run() {
				setPlayerMaxHealth(p);
			}
		},20);
    }
    
    @EventHandler(priority=EventPriority.LOW)
    public void onServerListPing(ServerListPingEvent ev) {
    	long time = Bukkit.getWorld("world").getTime();
    	String weather = "";
    	if (Bukkit.getWorld("world").hasStorm()) {weather="\u2602";} else {if (time>=10000) {weather="\u263D";} else {weather="\u2600";}}
    	if (time>0 && time<=3000) {
    		ev.setMotd("\u00A7bsig's Minecraft!\n"+weather+"  \u00A7fCurrently: \u00A7eMORNING");
    	} else
    	if (time>3000 && time<=10000) {
    		ev.setMotd("\u00A7bsig's Minecraft!\n"+weather+"  \u00A7fCurrently: \u00A76AFTERNOON");
    	} else
    	if (time>10000 && time<=13000) {
    		ev.setMotd("\u00A7bsig's Minecraft!\n"+weather+"  \u00A7fCurrently: \u00A73EVENING");
    	} else
    	if (time>13000 && time<23000) {
    		ev.setMotd("\u00A7bsig's Minecraft!\n"+weather+"  \u00A7fCurrently: \u00A79NIGHT");
    	} else {
    		ev.setMotd("\u00A7bsig's Minecraft!\n"+weather+"  \u00A7fCurrently: \u00A7dDAWN");
    	}
    }

	public void saveOurData(){
		getConfig().set("SERVERTICK", getServerTickTime()); //Add to how many ticks we've used.
		getConfig().set("DAYMULT", DAYMULT);
		getConfig().set("SERVERCHECKERTICKS", SERVERCHECKERTICKS);
		getConfig().set("TERMINALTIME", TERMINALTIME);
		getConfig().set("DEATHPENALTY", DEATHPENALTY);
		getConfig().set("RECYCLECHANCE", RECYCLECHANCE);
		getConfig().set("RECYCLEDECAYAMT", RECYCLEDECAYAMT);
		getConfig().set("ITEMCUBEID", ITEMCUBEID);
		getConfig().set("ARMOR/ARMOR_LEATHER_HP", ARMOR_LEATHER_HP);
		getConfig().set("ARMOR/ARMOR_IRON_HP", ARMOR_IRON_HP);
		getConfig().set("ARMOR/ARMOR_GOLD_HP", ARMOR_GOLD_HP);
		getConfig().set("ARMOR/ARMOR_DIAMOND_HP", ARMOR_DIAMOND_HP);
		getConfig().set("ARMOR/ARMOR_IRONBLOCK_HP", ARMOR_IRON2_HP);
		getConfig().set("ARMOR/ARMOR_GOLDBLOCK_HP", ARMOR_GOLD2_HP);
		getConfig().set("ARMOR/ARMOR_DIAMONDBLOCK_HP", ARMOR_DIAMOND2_HP);
		getConfig().set("HEALTH/HEALTH_REGENERATION_RATE", HEALTH_REGENERATION_RATE);
		getConfig().set("HEALTH/FOOD_HEAL_AMT", FOOD_HEAL_AMT);
		getConfig().set("ENEMY/ENEMY_DMG_MULT", ENEMY_DMG_MULT);
		getConfig().set("ENEMY/EXPLOSION_DMG_MULT", EXPLOSION_DMG_MULT);
		getConfig().set("ENEMY/HEADSHOT_ACC", HEADSHOT_ACC);
		getConfig().set("ITEM/RARE_DROP_RATE", RARE_DROP_RATE);
		getConfig().set("PARTY_CHUNK_SIZE", PARTY_CHUNK_SIZE);
		getConfig().set("XP_CONVERSION_RATE", XP_CONVERSION_RATE);
		getConfig().set("WORLD_SHOP_ID", WORLD_SHOP_ID);
		getConfig().set("LOGGING_LEVEL", LOGGING_LEVEL);
		//getConfig().set("MOTD", MOTD); //It makes no sense to save the MOTD as it will never be modified in-game.
		saveConfig();
		
		TwosideRecyclingCenter.saveConfig();
		
		//Save user configs here too.
    	saveAllUserConfigs();
    	
		log("[TASK] Configurations have been saved successfully.",3);
	}
	
	public void saveAllUserConfigs() {
		for (int i=0;i<playerdata.size();i++) {
    		PlayerStructure pd = playerdata.get(i);
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
		getConfig().addDefault("PARTY_CHUNK_SIZE", PARTY_CHUNK_SIZE);
		getConfig().addDefault("XP_CONVERSION_RATE", XP_CONVERSION_RATE);
		getConfig().addDefault("WORLD_SHOP_ID", WORLD_SHOP_ID);
		getConfig().addDefault("LOGGING_LEVEL", LOGGING_LEVEL);
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
		EXPLOSION_DMG_MULT = getConfig().getDouble("ENEMY/EXPLOSION_DMG_MULT");
		HEADSHOT_ACC = getConfig().getDouble("ENEMY/HEADSHOT_ACC");
		RARE_DROP_RATE = getConfig().getDouble("ITEM/RARE_DROP_RATE");
		PARTY_CHUNK_SIZE = getConfig().getInt("PARTY_CHUNK_SIZE");
		XP_CONVERSION_RATE = getConfig().getDouble("XP_CONVERSION_RATE");
		WORLD_SHOP_ID = getConfig().getInt("WORLD_SHOP_ID");
		LOGGING_LEVEL = getConfig().getInt("LOGGING_LEVEL");
		getMOTD();
		
		//Informational reports to the console.
		getLogger().info("[CONFIG] SERVERTICK set to "+SERVERTICK+".");
		getLogger().info("[CONFIG] SPEED of Day/Night Cycles are x"+DAYMULT);
		getLogger().info("[CONFIG] Server will auto-save configs every "+SERVERCHECKERTICKS+" ticks.");
		getLogger().info("[CONFIG] Withdraw/Deposit terminals can be used for "+TERMINALTIME+" ticks.");
		getLogger().info("[CONFIG] Death Penalty is "+DEATHPENALTY+"% of holding money.");
		getLogger().info("[CONFIG] Chance to Recycle an Item on Despawn: "+RECYCLECHANCE+"%. Decay AMT: "+RECYCLEDECAYAMT+"%.");
		getLogger().info("[CONFIG] Item Cube ID is currently at "+ITEMCUBEID+". World Shop ID is at "+WORLD_SHOP_ID);
		getLogger().info("[CONFIG] Health Regeneration Rate is  "+HEALTH_REGENERATION_RATE+" ticks per heal.");
		getLogger().info("[CONFIG] Food healing amount is "+FOOD_HEAL_AMT+" health per food item.");
		getLogger().info("[CONFIG] Enemy Damage Multiplier x"+ENEMY_DMG_MULT+". Explosion Damage Multiplier x"+EXPLOSION_DMG_MULT);
		getLogger().info("[CONFIG] Headshots have to be "+(HEADSHOT_ACC*100)+"% accurate.");
		getLogger().info("[CONFIG] Rare Drop Rate is currently "+(RARE_DROP_RATE*100)+"%");
		getLogger().info("[CONFIG] Party Chunk Size Detection is set to "+(PARTY_CHUNK_SIZE)+" chunks.");
		getLogger().info("[CONFIG] XP Conversion rate is $"+XP_CONVERSION_RATE+" per XP Point.");
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
	public List<ItemStack> itemCube_loadConfig(int id){
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
	
	public static long getServerTickTime() {
		//As the SERVERTICK variable is never actually updated,
		//we have to recalculate the actual value of it if we want to use it.
		return  Math.round((Bukkit.getWorld("world").getFullTime()-STARTTIME)*DAYMULT+SERVERTICK);
	}
	
	public static boolean isNumeric(String str)
	{
	  return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
	}
	
	public String createHealthbar(double pcthp, Player p) {
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
					Iterables.get(p.getActivePotionEffects(), i).getType().equals(PotionEffectType.WITHER)) {
				hasDebuff=true;
			}
			if (Iterables.get(p.getActivePotionEffects(), i).getType().equals(PotionEffectType.ABSORPTION)) {
				absorptionlv=Iterables.get(p.getActivePotionEffects(), i).getAmplifier()+1;
			}
		}
		
		String bar = " ";
		int length = 0;
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
			//bar+=ChatColor.DARK_PURPLE+""+"¤";
		} else
		if (inEnd) {
			/* 058D:Counter-clockwise portal
			 * 058E:Clockwise portal
			 */
			bar+=ChatColor.DARK_BLUE+""+Character.toString((char)0x25CA);
			//bar+=ChatColor.DARK_PURPLE+""+"¤";
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
		for (int i=0;i<playerdata.size();i++) {
    		PlayerStructure pd = playerdata.get(i);
    		if (pd.name.equalsIgnoreCase(p.getName())) {
    			//Found it. Read money and quit.
    			Double d = Double.valueOf(pd.money);
    			DecimalFormat df = new DecimalFormat("0.00");
    			return Double.parseDouble(df.format(d));
    		}
    	}
		//Something bad happened if we got here.
		log("[WARNING] Could not find the correct player data file for "+p.getName()+" to get money data from.",1);
		return -1;
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
		for (int i=0;i<playerdata.size();i++) {
    		PlayerStructure pd = playerdata.get(i);
    		if (pd.name.equalsIgnoreCase(p.getName())) {
    			//Found it. Read money and quit.
    			Double d = Double.valueOf(pd.bank_money);
    			DecimalFormat df = new DecimalFormat("0.00");
    			return Double.parseDouble(df.format(d));
    		}
    	}
		//Something bad happened if we got here.
		log("[WARNING] Could not find the correct player data file for "+p.getName()+" to get money bank data from.",1);
		return -1;
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
		for (int i=0;i<playerdata.size();i++) {
    		PlayerStructure pd = playerdata.get(i);
    		if (pd.name.equalsIgnoreCase(p.getName())) {
    			//Found it. Read money and quit.
    			pd.money+=amt;
    			found=true;
    			break;
    		}
    	}
		//Something bad happened if we got here.
		if (!found) {
			log("[WARNING] Could not find the correct player data file for "+p.getName()+" to get money data from.",1);
		}
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
		for (int i=0;i<playerdata.size();i++) {
    		PlayerStructure pd = playerdata.get(i);
    		if (pd.name.equalsIgnoreCase(p.getName())) {
    			//Found it. Read money and quit.
    			pd.bank_money+=amt;
    			found=true;
    		}
    	}
		//Something bad happened if we got here.
		if (!found) {
			log("[WARNING] Could not find the correct player data file for "+p.getName()+" to get money data from.",1);
		}
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
	
	public void setPlayerMaxHealth(Player p) {
		//Determine player max HP based on armor being worn.
		int hp=10; //Get the base max health.
		//Get all equips.
		ItemStack[] equipment = {p.getInventory().getHelmet(),p.getInventory().getChestplate(),p.getInventory().getLeggings(),p.getInventory().getBoots()};
		
		for (int i=0;i<equipment.length;i++) {
			if (equipment[i]!=null) {
				boolean is_block_form=false;
				//Determine if the piece is block form.
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
		}
		
		if (GenericFunctions.isDefender(p)) {
			hp+=10;
			p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,40,(p.isBlocking())?1:0));
		}
		
		p.setMaxHealth(hp);
		if (!p.isDead()) {
			p.setHealth(p.getHealth());
		}
	}
	
	
	
	
	public void updateTitle(final Player p, boolean headshot) {
		if (headshot) {
			updateTitle(p,ChatColor.DARK_RED+"HEADSHOT !");
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

	public void updateTitle(final Player p) {
		updateTitle(p, "");
	}
	
	public void updateTitle(final Player p, final String message1) {
		//Updates the target title for this player.
		PlayerStructure pd=null;
		for (int i=0;i<playerdata.size();i++) {
			if (playerdata.get(i).name.equalsIgnoreCase(p.getName())) {
				pd=playerdata.get(i);
				break;
			}
		}
		String MonsterName = pd.target.getType().toString().toLowerCase();
		if (pd.target.getCustomName()!=null) {
			MonsterName = pd.target.getCustomName();
			if (MonsterName.contains(ChatColor.DARK_RED+"Hellfire")) {
				pd.target.setFireTicks(99999);
			}
			if (pd.target.getCustomName()!=null &&
					!pd.target.getCustomName().contains("Leader") &&
					MonsterController.isZombieLeader(pd.target)) {
				pd.target.setCustomName(pd.target.getCustomName()+" Leader");
				MonsterName = pd.target.getCustomName();
			}
		} else {
			MonsterName = GenericFunctions.CapitalizeFirstLetters(MonsterName.replace("_", " "));
		}
		final String finalMonsterName = MonsterName;
		final PlayerStructure finalpd = pd;
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			public void run() {
				String heartdisplay = "", remainingheartdisplay = "";
				int color1=0,color2=1;
				double health=finalpd.target.getHealth();
				double maxhealth=finalpd.target.getMaxHealth();
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

				p.sendTitle(message1, finalMonsterName+" - "+finalheartdisplay);
			}}
		,1);
		if (pd.title_task!=-1) {
			Bukkit.getScheduler().cancelTask(pd.title_task);
			pd.title_task=-1;
		}
		pd.title_task=Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			public void run() {
				PlayerStructure pd=null;
				for (int i=0;i<playerdata.size();i++) {
					if (playerdata.get(i).name.equalsIgnoreCase(p.getName())) {
						pd=playerdata.get(i);
						break;
					}
				}
				pd.title_task=-1;
				p.sendTitle("","");
			}
		},15);
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
	
	public void DealCalculatedDamage(ItemStack weapon, LivingEntity p, LivingEntity target) {
		//Deals custom calculated damage to a given target.
		//Because we do not want to use Minecraft's built-in combat system, we will
		//create our own.
		double basedmg = 0.0;
		if (weapon!=null) {
			switch (weapon.getType()) {
				case WOOD_SWORD:{
					basedmg = 3.0;
				}break;
				case STONE_SWORD:{
					basedmg = 4.0;
				}break;
				case GOLD_SWORD:{
					basedmg = 10.0;
				}break;
				case IRON_SWORD:{
					basedmg = 7.0;
				}break;
				case DIAMOND_SWORD:{
					basedmg = 9.0;
				}break;
				case WOOD_SPADE:{
					basedmg = 1.5;
				}break;
				case STONE_SPADE:{
					basedmg = 2.5;
				}break;
				case GOLD_SPADE:{
					basedmg = 8.0;
				}break;
				case IRON_SPADE:{
					basedmg = 5.0;
				}break;
				case DIAMOND_SPADE:{
					basedmg = 7.0;
				}break;
				case WOOD_PICKAXE:{
					basedmg = 1.25;
				}break;
				case STONE_PICKAXE:{
					basedmg = 2.0;
				}break;
				case GOLD_PICKAXE:{
					basedmg = 7.5;
				}break;
				case IRON_PICKAXE:{
					basedmg = 4.5;
				}break;
				case DIAMOND_PICKAXE:{
					basedmg = 6.0;
				}break;
				case WOOD_AXE:{
					basedmg = 4.0;
				}break;
				case STONE_AXE:{
					basedmg = 5.0;
				}break;
				case GOLD_AXE:{
					basedmg = 11.0;
				}break;
				case IRON_AXE:{
					basedmg = 8.0;
				}break;
				case DIAMOND_AXE:{
					basedmg = 9.0;
				}break;
				default:{
					basedmg = 1.0;
				}
			}
		} else {
			basedmg=1.0;
		}
		
		if (GenericFunctions.isHardenedItem(weapon)) {
			basedmg*=2;
		}

		//Enchantments!
		
		//Check for Protection enchantment.
		int protectionlevel = 0;
		
		//Calculate damage reduction based on armor.
		double dmgreduction = 0.0; //The percent of damage reduction applied by armor.
		ItemStack[] monsterEquipment = target.getEquipment().getArmorContents();
		boolean isMonster = target instanceof Monster;
		for (int i=0;i<monsterEquipment.length;i++) {
			if (monsterEquipment[i]!=null) {
				
				if (monsterEquipment[i].getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL)>0) {
					log("Monster "+target.getEntityId()+" has Protection "+monsterEquipment[i].getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL),4);
					protectionlevel+=monsterEquipment[i].getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL);
					//Protection is 1% damage reduction per level of protection.
				}
				
				switch (monsterEquipment[i].getType()) {
					case LEATHER_HELMET:{
						dmgreduction+=3.0*((isMonster)?2:1); //We multiply it all by 2 since we are giving them the "block" version of the armor.
					}break;
					case LEATHER_CHESTPLATE:{
						dmgreduction+=3.0*((isMonster)?2:1);
					}break;
					case LEATHER_LEGGINGS:{
						dmgreduction+=3.0*((isMonster)?2:1);
					}break;
					case LEATHER_BOOTS:{
						dmgreduction+=3.0*((isMonster)?2:1);
					}break;
					case IRON_HELMET:{
						dmgreduction+=5.0*((isMonster)?2:1);
					}break;
					case IRON_CHESTPLATE:{
						dmgreduction+=5.0*((isMonster)?2:1);
					}break;
					case IRON_LEGGINGS:{
						dmgreduction+=5.0*((isMonster)?2:1);
					}break;
					case IRON_BOOTS:{
						dmgreduction+=5.0*((isMonster)?2:1);
					}break;
					case GOLD_HELMET:{
						dmgreduction+=10.0*((isMonster)?2:1);
					}break;
					case GOLD_CHESTPLATE:{
						dmgreduction+=10.0*((isMonster)?2:1);
					}break;
					case GOLD_LEGGINGS:{
						dmgreduction+=10.0*((isMonster)?2:1);
					}break;
					case GOLD_BOOTS:{
						dmgreduction+=10.0*((isMonster)?2:1);
					}break;
					case DIAMOND_HELMET:{
						dmgreduction+=8.0*((isMonster)?2:1);
					}break;
					case DIAMOND_CHESTPLATE:{
						dmgreduction+=8.0*((isMonster)?2:1);
					}break;
					case DIAMOND_LEGGINGS:{
						dmgreduction+=8.0*((isMonster)?2:1);
					}break;
					case DIAMOND_BOOTS:{
						dmgreduction+=8.0*((isMonster)?2:1);
					}break;
					default: {
						dmgreduction+=0.0*((isMonster)?2:1);
					}
				}
			}
		}
		
		//Now apply resistances if any.
		//Resistance effect reduces damage by 10% per level of resistance.
		int resistlevel = 0;
		int strengthlevel = 0;
		int weaknesslevel = 0;
		int partylevel = 0;
		
		Collection<PotionEffect> target_effects = target.getActivePotionEffects();
		for (int i=0;i<target_effects.size();i++) {
			if (Iterables.get(target_effects, i).getType().equals(PotionEffectType.DAMAGE_RESISTANCE)) {
				resistlevel = Iterables.get(target_effects, i).getAmplifier()+1;
				log("Found resistance on this mob. Resistance level: "+(resistlevel),5);
			}
		}
		
		//Finally, apply a strength buff if the player has one.
		//Strength effect increases damage by 10% per level of strength.
		//Apply weakness if the player has it for some reason as well.
		//Weakness effect decreases damage by 10% per level of weakness.
		Collection<PotionEffect> player_effects = p.getActivePotionEffects();
		for (int i=0;i<player_effects.size();i++) {
			log("Found an effect: "+Iterables.get(player_effects, i).getType(),5);
			if (Iterables.get(player_effects, i).getType().equals(PotionEffectType.INCREASE_DAMAGE)) {
				strengthlevel = Iterables.get(player_effects, i).getAmplifier()+1;
				log("Found strength on this player. Strength level: "+(strengthlevel),5);
			} else
			if (Iterables.get(player_effects, i).getType().equals(PotionEffectType.WEAKNESS)) {
				weaknesslevel = Iterables.get(player_effects, i).getAmplifier()+1;
				log("Found strength on this player. Strength level: "+(strengthlevel),5);
			}
		}
		
		for (int j=0;j<playerdata.size();j++) {
			if (playerdata.get(j).name.equalsIgnoreCase(p.getName()) && playerdata.get(j).partybonus>0) {
				partylevel = playerdata.get(j).partybonus;
				log("Party level is "+partylevel,5);
				if (partylevel>9) {partylevel=9;}
			}
		}
		
		int sharpnesslevel=0;
		//Apply player enchantments next.
		//Each sharpness level increases damage by 0.5.
		//Both Smite and Bane of Arthropods increases damage by 1.0 per level.
		if (p.getEquipment().getItemInMainHand()!=null) {
			if (p.getEquipment().getItemInMainHand().getEnchantmentLevel(Enchantment.DAMAGE_ALL)>0) {
				sharpnesslevel+=p.getEquipment().getItemInMainHand().getEnchantmentLevel(Enchantment.DAMAGE_ALL);
				log("Player "+p.getName()+" has Sharpness "+p.getEquipment().getItemInMainHand().getEnchantmentLevel(Enchantment.DAMAGE_ALL)+".",5);
			} else
			if ((target.getType()==EntityType.ZOMBIE || target.getType()==EntityType.PIG_ZOMBIE ||
					target.getType()==EntityType.WITHER || target.getType()==EntityType.SKELETON) &&
					p.getEquipment().getItemInMainHand().getEnchantmentLevel(Enchantment.DAMAGE_UNDEAD)>0) {
				sharpnesslevel+=p.getEquipment().getItemInMainHand().getEnchantmentLevel(Enchantment.DAMAGE_ALL)*2;
				log("Player "+p.getName()+" has Smite "+p.getEquipment().getItemInMainHand().getEnchantmentLevel(Enchantment.DAMAGE_ALL)+".",5);
			} else
				if ((target.getType()==EntityType.SPIDER || target.getType()==EntityType.CAVE_SPIDER ||
					target.getType()==EntityType.SILVERFISH || target.getType()==EntityType.ENDERMITE) &&
					p.getEquipment().getItemInMainHand().getEnchantmentLevel(Enchantment.DAMAGE_UNDEAD)>0) {
				sharpnesslevel+=p.getEquipment().getItemInMainHand().getEnchantmentLevel(Enchantment.DAMAGE_ALL)*2;
				log("Player "+p.getName()+" has Bane of Arthropods "+p.getEquipment().getItemInMainHand().getEnchantmentLevel(Enchantment.DAMAGE_ALL)+".",5);
			}
		}
		
		boolean hasShield=false;
		
		//Check if our enemy has a shield.
		//Shields reduce damage by another 5%.
		if (target.getEquipment().getItemInOffHand().getType()==Material.SHIELD) {
			hasShield=true;
		}
		
		final double dmgamt = (
					basedmg
					+sharpnesslevel*0.5
					-(basedmg*(dmgreduction/100.0d))
				)
				*((10-resistlevel)*0.1)
				/((10-strengthlevel)*0.1)
				*((10-weaknesslevel)*0.1)
				/((10-partylevel)*0.1)
				*((100-protectionlevel)*0.01)
				*((hasShield)?0.95:1.00); //Calculated damage amount.
		
		log("Final damage is "+dmgamt,3);
		
		final LivingEntity pp = p;
		final LivingEntity m = target;

		if (m.getHealth()>dmgamt) {
			m.setHealth(m.getHealth()-dmgamt);
		} else {
			m.setHealth(0.0);
		}
	}
	
	public double CalculateDamageReduction(double basedmg,LivingEntity target,Entity damager) {
		ItemStack[] armor = target.getEquipment().getArmorContents();
		double dmgreduction = 0.0;
		
		int protectionlevel = 0;
		int resistlevel = 0;
		int partylevel = 0;
		
		for (int i=0;i<armor.length;i++) {
			if (armor[i]!=null) {
				//Check for Protection enchantment.
				//Each Protection level gives 1% extra damage reduction.
				if (armor[i].getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL)>0) {
					protectionlevel+=armor[i].getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL);
				}
				if ((damager instanceof Arrow) && armor[i].getEnchantmentLevel(Enchantment.PROTECTION_PROJECTILE)>0) {
					protectionlevel+=armor[i].getEnchantmentLevel(Enchantment.PROTECTION_PROJECTILE);
				}
				if ((damager instanceof Creeper) && armor[i].getEnchantmentLevel(Enchantment.PROTECTION_EXPLOSIONS)>0) {
					protectionlevel+=armor[i].getEnchantmentLevel(Enchantment.PROTECTION_EXPLOSIONS);
				}
				
				boolean isBlockArmor = GenericFunctions.isHardenedItem(armor[i]);
				switch (armor[i].getType()) {
					case LEATHER_BOOTS:
					case LEATHER_LEGGINGS:
					case LEATHER_CHESTPLATE:
					case LEATHER_HELMET: {
						dmgreduction+=3*((isBlockArmor)?2:1);
					}break;
					case IRON_BOOTS:
					case IRON_LEGGINGS:
					case IRON_CHESTPLATE:
					case IRON_HELMET: {
						dmgreduction+=5*((isBlockArmor)?2:1);
					}break;
					case GOLD_BOOTS:
					case GOLD_LEGGINGS:
					case GOLD_CHESTPLATE:
					case GOLD_HELMET: {
						dmgreduction+=10*((isBlockArmor)?2:1);
					}break;
					case DIAMOND_BOOTS:
					case DIAMOND_LEGGINGS:
					case DIAMOND_CHESTPLATE:
					case DIAMOND_HELMET: {
						dmgreduction+=8*((isBlockArmor)?2:1);
					}break;
				}
			}
		}
		
		//Check for resistance effect.
		Collection<PotionEffect> target_effects = target.getActivePotionEffects();
		for (int i=0;i<target_effects.size();i++) {
			if (Iterables.get(target_effects, i).getType().equals(PotionEffectType.DAMAGE_RESISTANCE)) {
				resistlevel = Iterables.get(target_effects, i).getAmplifier()+1;
				log("Resistance level is "+resistlevel,5);
			}
		}
		
		if (target instanceof Player) {
			Player p = (Player)target;
			for (int j=0;j<playerdata.size();j++) {
				if (playerdata.get(j).name.equalsIgnoreCase(p.getName()) && playerdata.get(j).partybonus>0) {
					partylevel = playerdata.get(j).partybonus;
					if (partylevel>9) {partylevel=9;}
				}
			}
		}
		
		//Blocking: -((p.isBlocking())?ev.getDamage()*0.33:0) //33% damage will be reduced if we are blocking.
		//Shield: -((p.getEquipment().getItemInOffHand()!=null && p.getEquipment().getItemInOffHand().getType()==Material.SHIELD)?ev.getDamage()*0.05:0) //5% damage will be reduced if we are holding a shield.
		

		double finaldmg=(basedmg-(basedmg*(dmgreduction/100.0d)))
				*((10-resistlevel)*0.1)
				*((100-protectionlevel)*0.01)
				*((10-partylevel)*0.1)
				*((target instanceof Player && ((Player)target).isBlocking())?(GenericFunctions.isDefender((Player)target))?0.30:0.50:1)
				*((GenericFunctions.isDefender(target))?0.9:(target.getEquipment().getItemInOffHand()!=null && target.getEquipment().getItemInOffHand().getType()==Material.SHIELD)?0.95:1);
		
		log(finaldmg+" damage calculated for: "+target.getName()+".",5);
		return finaldmg;
	}
	
	public boolean hasShopSignAttached(Block b) {
		//Returns true if there is a shop sign attached to this block.
		//Look on all four sides relative to this block.
		log("Reference block is "+b.getLocation().toString()+" of type "+b.getType(),5);
		for (int x=-1;x<=1;x++) {
			for (int z=-1;z<=1;z++) {
				if ((x!=0 || z!=0) && Math.abs(x)!=Math.abs(z)) {
					log("This is a "+b.getLocation().add(x,0.5,z).getBlock().getType(),5);
					if (b.getLocation().add(x,0,z).getBlock().getType()==Material.WALL_SIGN) {
						Sign s = (Sign)(b.getLocation().add(x,0,z).getBlock().getState());
						//See if this sign is a world shop.
						if (WorldShop.IsWorldSign(s)) {
							return true;
						}
					}
				}
			}
		}
		return false;
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
	
	public ChatColor GetHeartColor(int colorval) {
		switch (colorval) {
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
			switch (loglv) {
				case 0: {
					//Only game breaking messages appear in level 0.
					Bukkit.getLogger().severe(logmessage);
				}break;
				case 1: {
					//Only warning messages appear in level 1.
					Bukkit.getLogger().warning(logmessage);
				}break;
				case 2: {
					//Regular Gameplay information can appear here.
					Bukkit.getLogger().info(logmessage);
				}break;
				case 3: {
					//Debug messages that generalize the events happening in the world.
					Bukkit.getLogger().info(logmessage);
				}break;
				case 4: {
					//Debug messages that define the specifics of an event happening, including logic / number calculations.
					Bukkit.getLogger().info(logmessage);
				}break;
				case 5: {
					//All messages possible. This may include messages that have very distinct purposes.
					Bukkit.getLogger().info(logmessage);
				}break;
			}
		}
	}
}