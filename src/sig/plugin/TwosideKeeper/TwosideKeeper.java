package sig.plugin.TwosideKeeper;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Achievement;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.Statistic;
import org.bukkit.WorldCreator;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.Hopper;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EnderDragon.Phase;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Style;
import org.bukkit.entity.Horse.Variant;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.entity.Slime;
import org.bukkit.entity.SmallFireball;
import org.bukkit.entity.Item;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Shulker;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.Snowman;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.entity.TippedArrow;
import org.bukkit.entity.Witch;
import org.bukkit.entity.Wither;
import org.bukkit.entity.WitherSkull;
import org.bukkit.entity.Zombie;
import org.bukkit.entity.minecart.HopperMinecart;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.entity.EntityPortalExitEvent;
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
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerAchievementAwardedEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupArrowEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.util.Vector;
import org.inventivetalent.glow.GlowAPI;

import com.google.common.collect.ImmutableSet;

//import com.google.common.graph.GraphBuilder;
//import com.google.common.graph.MutableGraph;

import aPlugin.API.Chests;
import events.PlayerGainItemEvent;
import events.PluginLoadEvent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_9_R1.EnumParticle;
import net.minecraft.server.v1_9_R1.MinecraftServer;
import sig.plugin.AutoPluginUpdate.AnnounceUpdateEvent;
import sig.plugin.TwosideKeeper.Events.EntityDamagedEvent;
import sig.plugin.TwosideKeeper.HelperStructures.AnvilItem;
import sig.plugin.TwosideKeeper.HelperStructures.ArtifactAbility;
import sig.plugin.TwosideKeeper.HelperStructures.ArtifactItem;
import sig.plugin.TwosideKeeper.HelperStructures.ArtifactItemType;
import sig.plugin.TwosideKeeper.HelperStructures.BankSession;
import sig.plugin.TwosideKeeper.HelperStructures.BowMode;
import sig.plugin.TwosideKeeper.HelperStructures.CubeType;
import sig.plugin.TwosideKeeper.HelperStructures.CustomItem;
import sig.plugin.TwosideKeeper.HelperStructures.CustomPotion;
import sig.plugin.TwosideKeeper.HelperStructures.CustomRecipe;
import sig.plugin.TwosideKeeper.HelperStructures.ItemCube;
import sig.plugin.TwosideKeeper.HelperStructures.ItemSet;
import sig.plugin.TwosideKeeper.HelperStructures.LivingEntityDifficulty;
import sig.plugin.TwosideKeeper.HelperStructures.Loot;
import sig.plugin.TwosideKeeper.HelperStructures.MalleableBaseQuest;
import sig.plugin.TwosideKeeper.HelperStructures.MonsterDifficulty;
import sig.plugin.TwosideKeeper.HelperStructures.PlayerMode;
import sig.plugin.TwosideKeeper.HelperStructures.Pronouns;
import sig.plugin.TwosideKeeper.HelperStructures.QuestStatus;
import sig.plugin.TwosideKeeper.HelperStructures.ServerType;
import sig.plugin.TwosideKeeper.HelperStructures.SessionState;
import sig.plugin.TwosideKeeper.HelperStructures.SpleefArena;
import sig.plugin.TwosideKeeper.HelperStructures.WorldShop;
import sig.plugin.TwosideKeeper.HelperStructures.WorldShopSession;
import sig.plugin.TwosideKeeper.HelperStructures.Common.ArrowQuiver;
import sig.plugin.TwosideKeeper.HelperStructures.Common.BlockModifyQueue;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;
import sig.plugin.TwosideKeeper.HelperStructures.Common.Habitation;
import sig.plugin.TwosideKeeper.HelperStructures.Common.JobRecipe;
import sig.plugin.TwosideKeeper.HelperStructures.Common.RecipeCategory;
import sig.plugin.TwosideKeeper.HelperStructures.Common.RecipeLinker;
import sig.plugin.TwosideKeeper.HelperStructures.Effects.EarthWaveTask;
import sig.plugin.TwosideKeeper.HelperStructures.Effects.LavaPlume;
import sig.plugin.TwosideKeeper.HelperStructures.Effects.TemporaryIce;
import sig.plugin.TwosideKeeper.HelperStructures.Effects.TemporaryLava;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.BlockUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.EntityUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.InventoryUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.ItemCubeUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.ItemUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.SoundUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.TimeUtils;
import sig.plugin.TwosideKeeper.HolidayEvents.Christmas;
import sig.plugin.TwosideKeeper.HolidayEvents.TreeBuilder;
import sig.plugin.TwosideKeeper.Logging.BowModeLogger;
import sig.plugin.TwosideKeeper.Logging.LootLogger;
import sig.plugin.TwosideKeeper.Logging.MysteriousEssenceLogger;
import sig.plugin.TwosideKeeper.Monster.HellfireGhast;
import sig.plugin.TwosideKeeper.Monster.HellfireSpider;


public class TwosideKeeper extends JavaPlugin implements Listener {

	public final static int CUSTOM_DAMAGE_IDENTIFIER = 500000;
	
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
	public static int ARROWQUIVERID=0; //The current number of Arrow Quivers in existence.
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
	public static int LAST_DEAL = 0;
	public static Location ELITE_LOCATION = null;
	public static boolean LOOT_TABLE_NEEDS_POPULATING=true;
	public static List<ArtifactAbility> TEMPORARYABILITIES = new ArrayList<ArtifactAbility>();
	public static Set<String> notWorldShop = new HashSet<String>();
	public static List<Entity> suppressed_entities = new ArrayList<Entity>();
	public static List<LavaPlume> lavaplume_list = new ArrayList<LavaPlume>();
	
	public static CustomItem HUNTERS_COMPASS;
	public static CustomItem UPGRADE_SHARD;
	public static CustomItem ITEM_CUBE;
	public static CustomItem LARGE_ITEM_CUBE;
	public static CustomItem ENDER_ITEM_CUBE;
	public static CustomItem DUPLICATE_ENDER_ITEM_CUBE;
	public static CustomItem VACUUM_CUBE;
	public static CustomItem ARROW_QUIVER;
	public static CustomItem HARDENED_IRON_HELMET;
	public static CustomItem HARDENED_IRON_CHESTPLATE;
	public static CustomItem HARDENED_IRON_LEGGINGS;
	public static CustomItem HARDENED_IRON_BOOTS;
	public static CustomItem HARDENED_DIAMOND_HELMET;
	public static CustomItem HARDENED_DIAMOND_CHESTPLATE;
	public static CustomItem HARDENED_DIAMOND_LEGGINGS;
	public static CustomItem HARDENED_DIAMOND_BOOTS;
	public static CustomItem HARDENED_GOLD_HELMET;
	public static CustomItem HARDENED_GOLD_CHESTPLATE;
	public static CustomItem HARDENED_GOLD_LEGGINGS;
	public static CustomItem HARDENED_GOLD_BOOTS;
	public static CustomItem WOOL_RECOLOR;
	public static CustomItem SLAB_RECONSTRUCTION;
	public static ShapelessRecipe ARTIFACT_RECIPE_T1;
	public static ShapelessRecipe ARTIFACT_RECIPE_T2;
	public static ShapelessRecipe ARTIFACT_RECIPE_T3;
	public static ShapelessRecipe ARTIFACT_RECIPE_T4;
	public static ShapelessRecipe ARTIFACT_RECIPE_T5;
	public static ShapelessRecipe ARTIFACT_RECIPE_T6;
	public static ShapelessRecipe ARTIFACT_RECIPE_T7;
	public static ShapelessRecipe ARTIFACT_RECIPE_T8;
	public static ShapelessRecipe ARTIFACT_RECIPE_T9;
	public static ShapelessRecipe ARTIFACT_RECIPE_T10;
	public static ShapelessRecipe ARTIFACT_RECIPE_T11;
	public static ShapelessRecipe ARTIFACT_RECIPE_T12;
	public static ShapelessRecipe ARTIFACT_RECIPE_T13;
	public static ShapelessRecipe ARTIFACT_RECIPE_T14;
	public static ShapelessRecipe ARTIFACT_RECIPE_T15;
	public static CustomItem INCREASE_ARTIFACT_CRAFTING_TIER;
	public static CustomItem DECREASE_ARTIFACT_CRAFTING_TIER;
	public static CustomItem EMPOWER_ARTIFACT_CRAFTING_ITEM;
	public static CustomItem MONEY_CHECK;
	public static CustomItem HANDMADE_ARROW;
	public static CustomItem DIAMONDTIPPED_ARROW;
	public static CustomItem POISON_ARROW;
	public static CustomItem TRAPPING_ARROW;
	public static CustomItem EXPLODING_ARROW;
	public static ShapedRecipe ITEM_CUBE_RECIPE;
	public static ShapedRecipe LARGE_ITEM_CUBE_RECIPE;
	public static ShapedRecipe ENDER_ITEM_CUBE_RECIPE;
	public static ShapelessRecipe DUPLICATE_ENDER_ITEM_CUBE_RECIPE;
	public static ShapelessRecipe VACUUM_CUBE_RECIPE;
	public static ShapelessRecipe FILTER_CUBE_RECIPE;
	public static ShapedRecipe ARROW_QUIVER_RECIPE;
	public static ShapedRecipe HARDENED_IRON_HELMET_RECIPE;
	public static ShapedRecipe HARDENED_IRON_CHESTPLATE_RECIPE;
	public static ShapedRecipe HARDENED_IRON_LEGGINGS_RECIPE;
	public static ShapedRecipe HARDENED_IRON_BOOTS_RECIPE;
	public static ShapedRecipe HARDENED_DIAMOND_HELMET_RECIPE;
	public static ShapedRecipe HARDENED_DIAMOND_CHESTPLATE_RECIPE;
	public static ShapedRecipe HARDENED_DIAMOND_LEGGINGS_RECIPE;
	public static ShapedRecipe HARDENED_DIAMOND_BOOTS_RECIPE;
	public static ShapedRecipe HARDENED_GOLD_HELMET_RECIPE;
	public static ShapedRecipe HARDENED_GOLD_CHESTPLATE_RECIPE;
	public static ShapedRecipe HARDENED_GOLD_LEGGINGS_RECIPE;
	public static ShapedRecipe HARDENED_GOLD_BOOTS_RECIPE;
	public static ShapedRecipe WOOL_RECOLOR_RECIPE;
	public static ShapelessRecipe SLAB_RECONSTRUCTION_RECIPE;
	public static ShapedRecipe ARTIFACT_RECIPE_T1_SWORD_RECIPE;
	public static ShapedRecipe ARTIFACT_RECIPE_T1_AXE_RECIPE;
	public static ShapedRecipe ARTIFACT_RECIPE_T1_PICKAXE_RECIPE;
	public static ShapedRecipe ARTIFACT_RECIPE_T1_SCYTHE_RECIPE;
	public static ShapedRecipe ARTIFACT_RECIPE_T1_BOW_RECIPE;
	public static ShapedRecipe ARTIFACT_RECIPE_T1_SHOVEL_RECIPE;
	public static ShapedRecipe ARTIFACT_RECIPE_T1_HELMET_RECIPE;
	public static ShapedRecipe ARTIFACT_RECIPE_T1_CHESTPLATE_RECIPE;
	public static ShapedRecipe ARTIFACT_RECIPE_T1_LEGGINGS_RECIPE;
	public static ShapedRecipe ARTIFACT_RECIPE_T1_BOOTS_RECIPE;
	public static ShapedRecipe ARTIFACT_RECIPE_T1_FISHING_ROD_RECIPE;
	public static ShapelessRecipe ARTIFACT_RECIPE_T2_RECIPE;
	public static ShapelessRecipe ARTIFACT_RECIPE_T3_RECIPE;
	public static ShapelessRecipe ARTIFACT_RECIPE_T4_RECIPE;
	public static ShapelessRecipe ARTIFACT_RECIPE_T5_RECIPE;
	public static ShapelessRecipe ARTIFACT_RECIPE_T6_RECIPE;
	public static ShapelessRecipe ARTIFACT_RECIPE_T7_RECIPE;
	public static ShapelessRecipe ARTIFACT_RECIPE_T8_RECIPE;
	public static ShapelessRecipe ARTIFACT_RECIPE_T9_RECIPE;
	public static ShapelessRecipe ARTIFACT_RECIPE_T10_RECIPE;
	public static ShapelessRecipe ARTIFACT_RECIPE_T11_RECIPE;
	public static ShapelessRecipe ARTIFACT_RECIPE_T12_RECIPE;
	public static ShapelessRecipe ARTIFACT_RECIPE_T13_RECIPE;
	public static ShapelessRecipe ARTIFACT_RECIPE_T14_RECIPE;
	public static ShapelessRecipe ARTIFACT_RECIPE_T15_RECIPE;
	public static ShapelessRecipe INCREASE_ARTIFACT_CRAFTING_TIER_RECIPE;
	public static ShapelessRecipe DECREASE_ARTIFACT_CRAFTING_TIER_RECIPE;
	public static ShapelessRecipe EMPOWER_ARTIFACT_CRAFTING_ITEM_RECIPE;
	public static ShapelessRecipe MONEY_CHECK_RECIPE;  
	public static ShapelessRecipe HANDMADE_ARROW_RECIPE;
	public static ShapelessRecipe DIAMONDTIPPED_ARROW_RECIPE;
	public static ShapelessRecipe POISON_ARROW_RECIPE;
	public static ShapelessRecipe TRAPPING_ARROW_RECIPE;
	public static ShapelessRecipe EXPLODING_ARROW_RECIPE;
	public static ShapelessRecipe PIERCING_ARROW_RECIPE;
	public static ShapelessRecipe WORLD_SHOP_RECIPE;
	public static ShapelessRecipe WORLD_SHOP2_RECIPE;
	public static CustomPotion STRENGTHENING_VIAL;
	public static CustomPotion LIFE_VIAL;
	public static CustomPotion HARDENING_VIAL;
	public static ItemStack DEAL_OF_THE_DAY_ITEM;
	
	public static final int POTION_DEBUG_LEVEL=5; 
	public static final int SPAWN_DEBUG_LEVEL=5; 
	public static final int LAVA_PLUME_COOLDOWN=60;
	
	public static final int SNOW_GOLEM_COOLDOWN=20*60;
	
	public static final int DODGE_COOLDOWN=100;
	public static final int DEATHMARK_COOLDOWN=240;
	public static final int EARTHWAVE_COOLDOWN=100;
	public static final int ERUPTION_COOLDOWN=100;
	public static final int LINEDRIVE_COOLDOWN=240;
	public static final int REJUVENATE_COOLDOWN=2400;
	public static final int ASSASSINATE_COOLDOWN=200;
	public static final int LIFESAVER_COOLDOWN=6000;
	public static final int ARROWBARRAGE_COOLDOWN=2400;
	public static final int SIPHON_COOLDOWN = 700;
	public static final int MOCK_COOLDOWN = 400;
	public static final int ICEWAND_COOLDOWN = 1200;
	
	public static final Material[] ClearFallingBlockList = {Material.REDSTONE_BLOCK};
	
	public static List<String> SnowmanHuntList = new ArrayList<String>();
	public static long LastSnowmanHunt = 0;
	public static String HuntingForSnowman = "";
	
	public static Location TWOSIDE_LOCATION;

	public static final int CLEANUP_DEBUG = 2;
	public static final int LOOT_DEBUG = 3;
	public static final int COMBAT_DEBUG = 3;
	public static double worldShopDistanceSquared = 1000000;
	public static double worldShopPriceMult = 2.0; //How much higher the price increases for every increment of worlsShopDistanceSquared.
	
	public static String lastActionBarMessage="";
	public static long last_snow_golem = 0;
	public static File filesave;
	public static HashMap<UUID,PlayerStructure> playerdata;	
	public static HashMap<UUID,LivingEntityStructure> livingentitydata;	
	public static SpleefManager TwosideSpleefGames;
	public static WorldShopManager TwosideShops;
	public static MysteriousEssenceLogger EssenceLogger; //The logger for Essences.
	public static BowModeLogger BowLogger; //The logger for Bow Modes.
	public static LootLogger Loot_Logger; //The logger for Loot.
	//public static AutoUpdatePlugin pluginupdater;
	public static boolean restarting_server=false;
	public static List<String> log_messages=new ArrayList<String>();
	public static List<TemporaryLava> temporary_lava_list = new ArrayList<TemporaryLava>();
	public static List<TemporaryIce> temporary_ice_list = new ArrayList<TemporaryIce>();
	public static List<Chunk> temporary_chunks = new ArrayList<Chunk>();
	public static List<BlockModifyQueue> blockqueue = new ArrayList<BlockModifyQueue>();
	public static List<JobRecipe> jobrecipes = new ArrayList<JobRecipe>();
	long LastClearStructureTime = 0;
	
    public static final Set<Material> isNatural = ImmutableSet.of(Material.CLAY, Material.DIRT, Material.GRASS,
            Material.GRASS_PATH, Material.GRAVEL, Material.MYCEL, Material.SAND, Material.SNOW, Material.SNOW_BLOCK,
            Material.SOUL_SAND, Material.STONE, Material.COBBLESTONE, Material.DOUBLE_PLANT, Material.LONG_GRASS, 
            Material.RED_ROSE, Material.YELLOW_FLOWER, Material.STATIONARY_WATER, Material.STATIONARY_LAVA,
            Material.MOSSY_COBBLESTONE, Material.COAL_ORE, Material.IRON_ORE, Material.GOLD_ORE, Material.REDSTONE_ORE,
            Material.GLOWING_REDSTONE_ORE, Material.LAPIS_ORE, Material.DIAMOND_ORE, Material.EMERALD_ORE,
            Material.OBSIDIAN, Material.ENDER_STONE, Material.NETHERRACK, Material.NETHER_BRICK, Material.HARD_CLAY,
            Material.STAINED_CLAY, Material.SANDSTONE, Material.LOG, Material.LOG_2, Material.WOOD,
            Material.FENCE, Material.PUMPKIN, Material.MELON_BLOCK, Material.LEAVES, Material.LEAVES_2);
	 
	public int TeamCounter = 0; 
	public static int time_passed = 0; //The total amount of time lost due to modifications to FullTime().
	public List<Integer> colors_used = new ArrayList<Integer>();
	public static HashMap<UUID,ChargeZombie> chargezombies = new HashMap<UUID,ChargeZombie>();
	public static HashMap<UUID,CustomMonster> custommonsters = new HashMap<UUID,CustomMonster>();
	public static List<EliteMonster> elitemonsters = new ArrayList<EliteMonster>();
	
	public static RecyclingCenter TwosideRecyclingCenter;
	
	//Bank timers and users.
	public static HashMap<UUID,BankSession> banksessions;
	public static Habitation habitat_data;
	public static boolean last_announced_storm = false; //Whether or not the last announcement was about a storm.
	public static long lastTimingReport=0;
	
	public static List<String> weather_watch_users = new ArrayList<String>();

	public static Plugin plugin;
	public int sleepingPlayers=0;
	public static List<Material> validsetitems = new ArrayList<Material>();

	public static double DEAL_OF_THE_DAY_PCT=0.2;
	
	public final static boolean CHRISTMASEVENT_ACTIVATED=false;
	public final static boolean CHRISTMASLINGERINGEVENT_ACTIVATED=false; //Limited Christmas drops/functionality remain while the majority of it is turned off.
	
	public final static boolean ELITEGUARDIANS_ACTIVATED=true;
	
	public static final Set<EntityType> LIVING_ENTITY_TYPES = ImmutableSet.of(
			EntityType.BAT,EntityType.BLAZE,EntityType.CAVE_SPIDER,EntityType.CHICKEN,
			EntityType.COW,EntityType.CREEPER,EntityType.HORSE,EntityType.GUARDIAN,EntityType.ENDER_DRAGON,
			EntityType.ENDERMAN,EntityType.ENDERMITE,EntityType.GHAST,EntityType.GIANT,
			EntityType.IRON_GOLEM,EntityType.PLAYER,EntityType.MAGMA_CUBE,EntityType.MUSHROOM_COW,
			EntityType.OCELOT,EntityType.PIG,EntityType.PIG_ZOMBIE,EntityType.RABBIT,
			EntityType.SHEEP,EntityType.SHULKER,EntityType.SILVERFISH,EntityType.SKELETON,
			EntityType.SLIME,EntityType.SNOWMAN,EntityType.SPIDER,EntityType.SQUID,
			EntityType.VILLAGER,EntityType.WITCH,EntityType.WOLF,EntityType.ZOMBIE);
	
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


	private final class GivePlayerPurchasedItems implements Runnable {
		private final Chest cc;
		private final AsyncPlayerChatEvent ev;
		private final ItemStack dropitem;

		private GivePlayerPurchasedItems(Chest cc, AsyncPlayerChatEvent ev, ItemStack dropitem) {
			this.cc = cc;
			this.ev = ev;
			this.dropitem = dropitem;
		}

		@Override
		public void run() {
			//ev.getPlayer().getWorld().dropItem(ev.getPlayer().getLocation(), dropitem).setPickupDelay(0);
			GenericFunctions.giveItem(ev.getPlayer(), dropitem);
			cc.getInventory().removeItem(dropitem);
		}
	}

	private final class CreateWorldPurchaseShop implements Runnable {
		private final WorldShopSession current_session;
		private final double amt;
		private final DecimalFormat df;
		private final AsyncPlayerChatEvent ev;

		private CreateWorldPurchaseShop(WorldShopSession current_session, double amt, DecimalFormat df,
				AsyncPlayerChatEvent ev) {
			this.current_session = current_session;
			this.amt = amt;
			this.df = df;
			this.ev = ev;
		}

		@Override
		public void run() {
			WorldShop newshop = TwosideShops.CreateWorldShop(current_session.GetSign(), current_session.getItem(), current_session.getAmt(), Double.parseDouble(df.format(amt)), ev.getPlayer().getName(),true);
			TwosideShops.SaveWorldShopData(newshop);
			WorldShop.spawnShopItem(current_session.GetSign().getLocation(), newshop);
			Chest c = (Chest)WorldShop.getBlockShopSignAttachedTo(current_session.GetSign()).getState();
			notWorldShop.remove(InventoryUtils.getInventoryHash(c.getInventory()));
			TwosideShops.RemoveSession(ev.getPlayer());
		}
	}

	private final class CreateWorldShop implements Runnable {
		private final DecimalFormat df;
		private final double amt;
		private final AsyncPlayerChatEvent ev;
		private final WorldShopSession current_session;

		private CreateWorldShop(DecimalFormat df, double amt, AsyncPlayerChatEvent ev,
				WorldShopSession current_session) {
			this.df = df;
			this.amt = amt;
			this.ev = ev;
			this.current_session = current_session;
		}

		@Override
		public void run() {
			WorldShop newshop = TwosideShops.CreateWorldShop(current_session.GetSign(), current_session.getItem(), current_session.getAmt(), Double.parseDouble(df.format(amt)), ev.getPlayer().getName());
			WorldShop.spawnShopItem(current_session.GetSign().getLocation(), newshop);
			Chest c = (Chest)WorldShop.getBlockShopSignAttachedTo(current_session.GetSign()).getState();
			notWorldShop.remove(InventoryUtils.getInventoryHash(c.getInventory()));
			TwosideShops.SaveWorldShopData(newshop);
			//RemoveItemAmount(ev.getPlayer(), current_session.getItem(), current_session.getAmt()); //We now handle items via chest.
			TwosideShops.RemoveSession(ev.getPlayer());
		}
	}

	private final class WriteAndSignCheck implements Runnable {
		private final ItemStack finalcheck;
		private final ItemStack check;
		private final AsyncPlayerChatEvent ev;

		private WriteAndSignCheck(ItemStack finalcheck, ItemStack check, AsyncPlayerChatEvent ev) {
			this.finalcheck = finalcheck;
			this.check = check;
			this.ev = ev;
		}

		@Override
		public void run() {
			if (check.getAmount()>1) {
				check.setAmount(check.getAmount()-1);
				//ev.getPlayer().getLocation().getWorld().dropItem(ev.getPlayer().getLocation(), finalcheck);
				GenericFunctions.giveItem(ev.getPlayer(), finalcheck);
			} else {
				ev.getPlayer().getEquipment().setItemInMainHand(finalcheck);
			}
		}
	}

	private final class ShutdownServerForUpdate implements Runnable {
		@Override
		public void run() {
			if (Bukkit.getOnlinePlayers().size()==0 && restarting_server) {
				Bukkit.savePlayers();
				aPlugin.API.discordSendRawItalicized("All players have disconnected. Server is shutting down...");
				for (int i=0;i<Bukkit.getWorlds().size();i++) {
					Bukkit.getWorlds().get(i).save();
				}
				Bukkit.shutdown();
			}
		}
	}

	private final class ReapplyAbsorptionHeartsFromSet implements Runnable {
		public void run(){
			for (Player p : Bukkit.getOnlinePlayers()) {
				double absorption_amt = ItemSet.TotalBaseAmountBasedOnSetBonusCount(GenericFunctions.getEquipment(p), p, ItemSet.SONGSTEEL, 3, 3)-4;
				if (absorption_amt>0) {
					if (p.hasPotionEffect(PotionEffectType.ABSORPTION)) {
						int oldlv = GenericFunctions.getPotionEffectLevel(PotionEffectType.ABSORPTION, p);
						GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.ABSORPTION,599,(int)(absorption_amt/4)+oldlv,p);
					} else {
						GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.ABSORPTION,599,(int)(absorption_amt/4),p);
					}
				}
				if (p.hasPotionEffect(PotionEffectType.ABSORPTION)) {
					CustomDamage.setAbsorptionHearts(p, Math.max(CustomDamage.getAbsorptionHearts(p),4*(GenericFunctions.getPotionEffectLevel(PotionEffectType.ABSORPTION, p)+1)));
				}
			}
		}
	}

	private final class ControlChargeZombies implements Runnable {
		public void run(){
			//Control charge zombies..
			for (ChargeZombie cz : chargezombies.values()) {
				if (cz.m==null || !cz.m.isValid() || !cz.isAlive() || !cz.hasTarget() || (cz.GetZombie().getWorld().getName().equalsIgnoreCase("world") && cz.GetZombie().getLocation().getY()>32)) {
					//This has to be removed...
					ScheduleRemoval(chargezombies,cz.m.getUniqueId());
				} else {
					//This is fine! Clear away blocks.
					Monster m = cz.GetZombie();

					if (cz.lastLoc!=null && cz.lastLoc.distance(m.getLocation())<=0.4) {
						cz.stuckTimer++;
						//TwosideKeeper.log("Stuck. "+stuckTimer, 0);
					} else {
						cz.stuckTimer=0;
					}
					cz.lastLoc = m.getLocation().clone();
					if (cz.stuckTimer>5) {
						//Teleport randomly.
						double numb = Math.random();
						if (numb<=0.33) {
							SoundUtils.playGlobalSound(m.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 1.0f, 1.0f);
							m.teleport(m.getLocation().add(Math.random()*6-3,0,0));
						} else
						if (numb<=0.5) {
							SoundUtils.playGlobalSound(m.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 1.0f, 1.0f);
							m.teleport(m.getLocation().add(0,0,Math.random()*6-3));
						} else
						{
							SoundUtils.playGlobalSound(m.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 1.0f, 1.0f);
							m.teleport(m.getLocation().add(0,Math.random()*6-3,0));
						}
						cz.stuckTimer=0;
					}
					if (m.getTarget().getLocation().getY()>=m.getLocation().getY()+2 &&
							Math.abs(m.getTarget().getLocation().getX()-m.getLocation().getX())<3 &&
							Math.abs(m.getTarget().getLocation().getZ()-m.getLocation().getZ())<3) {
						//This target is higher than we can reach... Let's pillar.
						Random r = new Random();
						r.setSeed(m.getUniqueId().getMostSignificantBits());
						//Block type is chosen based on the seed. Will be cobblestone, dirt, or gravel.
						for (int x=-1;x<2;x++) {
							for (int z=-1;z<2;z++) {
								if (m.getLocation().add(x,-1,z).getBlock().getType()==Material.AIR || 
										m.getLocation().add(x,-1,z).getBlock().isLiquid()) {
									m.setVelocity(new Vector(0,0.5,0));
									if (m.getLocation().getWorld().getName().equalsIgnoreCase("world_nether")) {
										m.getLocation().getBlock().getRelative(x,0,z).setType(Material.NETHERRACK);
										SoundUtils.playGlobalSound(m.getLocation(), Sound.BLOCK_STONE_PLACE, 1.0f, 1.0f);
									} else {
										switch (r.nextInt(3)) {
											case 0:{
												m.getLocation().getBlock().getRelative(x,0,z).setType(Material.DIRT);
												SoundUtils.playGlobalSound(m.getLocation().add(x,0,z), Sound.BLOCK_GRAVEL_PLACE, 1.0f, 1.0f);
											}break;
											case 1:{
												m.getLocation().getBlock().getRelative(x,0,z).setType(Material.COBBLESTONE);
												SoundUtils.playGlobalSound(m.getLocation().add(x,0,z), Sound.BLOCK_STONE_PLACE, 1.0f, 1.0f);
											}break;
											case 2:{
												m.getLocation().getBlock().getRelative(x,0,z).setType(Material.GRAVEL);
												SoundUtils.playGlobalSound(m.getLocation().add(x,0,z), Sound.BLOCK_GRAVEL_PLACE, 1.0f, 1.0f);
											}break;
										}
									}
								}
							}
						}
						//Command all nearby entities to jump too.
						List<LivingEntity> ents = GenericFunctions.getNearbyMonsters(m.getLocation(), 2);
						for (LivingEntity ent : ents) {
							if (!ent.equals(m)) {
								ent.setVelocity(new Vector(0,0.5,0));
							}
						}
					}
					ChargeZombie.BreakBlocksAroundArea(cz.m,1);
				}
			}
			for (CustomMonster cs : custommonsters.values()) {
				if (cs.m==null || !cs.m.isValid() || !cs.isAlive()) {
					//This has to be removed...
					if (cs instanceof sig.plugin.TwosideKeeper.Monster.Wither) {
						sig.plugin.TwosideKeeper.Monster.Wither w = (sig.plugin.TwosideKeeper.Monster.Wither)cs;
						w.Cleanup();
					}
					ScheduleRemoval(custommonsters,cs.m.getUniqueId());
				} else {
					cs.runTick();
				}
			}
			//Control elite monsters.
			for (EliteMonster em : elitemonsters) {
				if (!em.m.isValid()) {
					em.Cleanup();
			    	ScheduleRemoval(elitemonsters,em);
				} else {
					em.runTick();
				}
			}
			for (Entity e : suppressed_entities) {
				if (e==null || !e.isValid() ||
						GenericFunctions.getSuppressionTime(e)<=0) {
					if (e!=null && e.isValid() && e instanceof LivingEntity) {
						LivingEntityStructure les = LivingEntityStructure.getLivingEntityStructure((LivingEntity)e);
						((LivingEntity)e).getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(les.original_movespd);
						((LivingEntity)e).setAI(true);
					}
					ScheduleRemoval(suppressed_entities,e);
				}
			}
			for (LavaPlume lp : lavaplume_list) {
				if (!lp.runTick()) {
					ScheduleRemoval(lavaplume_list,lp);
				}
			}
			for (Player p : Bukkit.getOnlinePlayers()) {
				runServerHeartbeat.runVacuumCubeSuckup(p);
				runServerHeartbeat.runFilterCubeCollection(p);
				/*if (p.getVehicle() instanceof EnderDragon) {
					EnderDragon ed = (EnderDragon)p.getVehicle();
					ed.setVelocity(p.getLocation().getDirection().multiply(2.0f));
					ed.teleport(ed.getLocation().setDirection(p.getLocation().getDirection()));
				}*/
			}
			for (TemporaryLava tl : temporary_lava_list) {
				if (!tl.runTick()) {
					ScheduleRemoval(temporary_lava_list,tl);
				}
			}
			for (TemporaryIce tl : temporary_ice_list) {
				if (!tl.run()) {
					ScheduleRemoval(temporary_ice_list,tl);
				}
			}
		}

		private void UpdateLavaBlock(Block lavamod) {
			if (lavamod.getType()==Material.AIR) {lavamod.setType(Material.LAVA);lavamod.setData((byte)9);BlockState state = lavamod.getState();state.update(true,true);}
		}
	}

	public static void ScheduleRemoval(Set<? extends Object> list, Object remove) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(TwosideKeeper.plugin,new ThreadSafeCollection(list,remove),1);
	}
	public static void ScheduleRemoval(HashMap<? extends Object,? extends Object> map, Object remove) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(TwosideKeeper.plugin,new ThreadSafeCollection(map,remove),1);
	}
	public static void ScheduleRemoval(Collection<? extends Object> list, Object remove) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(TwosideKeeper.plugin,new ThreadSafeCollection(list,remove),1);
	}
	public static void ScheduleRemoval(List<? extends Object> list, Object remove) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(TwosideKeeper.plugin,new ThreadSafeCollection(list,remove),1);
	}

	private final class SetupPlayerMode implements Runnable {
		public void run(){
			for (Player p : Bukkit.getOnlinePlayers()) {
				PlayerMode.getPlayerMode(p);
			}
		}
	}

	@Override
    public void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, this);
		
		plugin=this;
		
		loadConfig();

		CustomItem.InitializeItemRecipes();
		Recipes.Initialize_ItemCube_Recipes();
		Recipes.Initialize_ArrowQuiver_Recipe();
		Recipes.Initialize_BlockArmor_Recipes();
		//Recipes.Initialize_ItemDeconstruction_Recipes();
		Recipes.Initialize_WoolRecolor_Recipes();
		Recipes.Initialize_SlabReconstruction_Recipes();
		Recipes.Initialize_Artifact_Recipes();
		Recipes.Initialize_ArtifactHelper_Recipes();
		Recipes.Initialize_Check_Recipe();
		//Recipes.Initialize_HunterCompass_Recipe();
		Recipes.Initialize_CustomArrow_Recipes();
		Recipes.Initialize_NotchApple_Recipe();
		Recipes.Initialize_NewRedstoneLamp_Recipe();
		
		Bukkit.createWorld(new WorldCreator("FilterCube"));
		
		filesave=getDataFolder(); //Store the location of where our data folder is.
		log("Data folder at "+filesave+".",3);
		//log("Spawn Radius is "+Bukkit.getServer().getSpawnRadius(),0);
		
		time_passed+=-Bukkit.getWorld("world").getFullTime();
		LASTSERVERCHECK=getServerTickTime();
		
		EssenceLogger = new MysteriousEssenceLogger();
		BowLogger = new BowModeLogger();
		Loot_Logger = new LootLogger();
		habitat_data = new Habitation();
		habitat_data.loadLocationHashesFromConfig();
		
		LastClearStructureTime = getServerTickTime();
		
		TwosideRecyclingCenter = new RecyclingCenter();
		TwosideRecyclingCenter.loadConfig();
		TwosideRecyclingCenter.populateItemListFromAllNodes();
		log("Recycling Centers Loaded: "+TwosideRecyclingCenter.getNumberOfNodes(),3);
		
		/*pluginupdater = new AutoUpdatePlugin(this);
		pluginupdater.AddPlugin("TwosideKeeper", "https://dl.dropboxusercontent.com/s/z5ram6vi3jipiit/TwosideKeeper.jar");
		pluginupdater.AddPlugin("aPlugin", "https://dl.dropboxusercontent.com/u/62434995/aPlugin.jar");*/
		
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
		TwosideShops.LoadShopPurchases();
		
		//Initialize Player Data structure.
		playerdata = new HashMap<UUID,PlayerStructure>();
		banksessions = new HashMap<UUID,BankSession>();
		livingentitydata = new HashMap<UUID,LivingEntityStructure>(); 
		
		validsetitems.add(Material.LEATHER_BOOTS);
		validsetitems.add(Material.LEATHER_CHESTPLATE);
		validsetitems.add(Material.LEATHER_HELMET);
		validsetitems.add(Material.LEATHER_LEGGINGS);
		validsetitems.add(Material.IRON_BOOTS);
		validsetitems.add(Material.IRON_CHESTPLATE);
		validsetitems.add(Material.IRON_HELMET);
		validsetitems.add(Material.IRON_LEGGINGS);
		validsetitems.add(Material.DIAMOND_BOOTS);
		validsetitems.add(Material.DIAMOND_CHESTPLATE);
		validsetitems.add(Material.DIAMOND_HELMET);
		validsetitems.add(Material.DIAMOND_LEGGINGS);
		validsetitems.add(Material.GOLD_BOOTS);
		validsetitems.add(Material.GOLD_CHESTPLATE);
		validsetitems.add(Material.GOLD_HELMET);
		validsetitems.add(Material.GOLD_LEGGINGS);
		validsetitems.add(Material.STONE_SWORD);
		validsetitems.add(Material.IRON_SWORD);
		validsetitems.add(Material.DIAMOND_SWORD);
		validsetitems.add(Material.GOLD_SWORD);
		validsetitems.add(Material.SKULL_ITEM);
		
		TEMPORARYABILITIES.add(ArtifactAbility.GREED);
		TEMPORARYABILITIES.add(ArtifactAbility.SURVIVOR);
		
		HUNTERS_COMPASS = CustomRecipe.DefineHuntersCompass();
		UPGRADE_SHARD = CustomRecipe.DefineUpgradeShard();
		STRENGTHENING_VIAL = CustomRecipe.DefineStrengtheningVial();
		LIFE_VIAL = CustomRecipe.DefineLifeVial();
		HARDENING_VIAL = CustomRecipe.DefineHardeningVial();
		
		TWOSIDE_LOCATION = new Location(Bukkit.getServer().getWorld("world"),1630,65,-265);
		//tpstracker = new Lag();
		
		//Let's not assume there are no players online. Load their data.
    	for (int i=0;i<Bukkit.getOnlinePlayers().toArray().length;i++) {
    		playerdata.put(((Player)Bukkit.getOnlinePlayers().toArray()[i]).getUniqueId(), new PlayerStructure((Player)Bukkit.getOnlinePlayers().toArray()[i],getServerTickTime()));
        	//playerdata.add(new PlayerStructure((Player)Bukkit.getOnlinePlayers().toArray()[i],getServerTickTime()));
    	}
    	//Announce the server has restarted soon after.
    	
    	WorldShop.createWorldShopRecipes();
    	WorldShop.loadShopPrices();
    	TwosideKeeper.DEAL_OF_THE_DAY_ITEM = WorldShop.generateItemDealOftheDay(1);
    	TwosideKeeper.DEAL_OF_THE_DAY_PCT = WorldShop.generatePercentOffForDealOftheDay();
    	log("Deal of the day loaded successfully: "+GenericFunctions.UserFriendlyMaterialName(TwosideKeeper.DEAL_OF_THE_DAY_ITEM),2);

    	if (!LOOT_TABLE_NEEDS_POPULATING) {
    		Loot.DefineLootChests();
        	Christmas.SetupChristmas();
    	}
    	
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new SetupPlayerMode(),0l,10l);
		
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new ControlChargeZombies(), 5l, 5l);
		
		/*if (SERVER_TYPE==ServerType.MAIN) { //Only perform this on the official servers. Test servers do not require constant updating.
			//Every 5 minutes, check for a plugin update.
			if (!restarting_server) {
				Bukkit.getScheduler().runTaskTimerAsynchronously(this, pluginupdater, 6000l, 6000l);
			}
		}*/
		
	    getServer().getScheduler().runTaskLaterAsynchronously(this, new DiscordStatusUpdater(), 300l);

		getServer().getScheduler().scheduleSyncRepeatingTask(this, new ReapplyAbsorptionHeartsFromSet(),0l,600l);
		
		//This is the constant timing method.
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new runServerHeartbeat(this), 20l, 20l);
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new runServerTick(), 1l, 1l);
		
		//log(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)+"",0);
    }

	@Override
    public void onDisable() {
    	//Clear out remaining parties.
    	for (int i=0;i<TeamCounter;i++) {
    		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "scoreboard objectives remove Party"+i);
    	}
    	saveOurData(); //Saves all of our server variables and closes down.
    	PerformCleanupMaintenance();
    }
    
    private void PerformCleanupMaintenance() {
    	long starttime = System.currentTimeMillis();
    	log("Starting Cleanup Maintenance Procedures...",CLEANUP_DEBUG);
		log("Cleaning up Lava Plumes ["+lavaplume_list.size()+"]",CLEANUP_DEBUG);
		for (LavaPlume lp : lavaplume_list) {
			lp.Cleanup();
		}
		log(ChatColor.YELLOW+"    "+(System.currentTimeMillis()-starttime)+"ms",CLEANUP_DEBUG);
		long betweentime = System.currentTimeMillis();
		log("Cleaning up Temporary Lava ["+temporary_lava_list.size()+"]",CLEANUP_DEBUG);
		for (TemporaryLava tl : temporary_lava_list) {
			tl.Cleanup();
		}
		log(ChatColor.YELLOW+"    "+(System.currentTimeMillis()-betweentime)+"ms",CLEANUP_DEBUG);
		betweentime = System.currentTimeMillis();
		log("Cleaning up Temporary Ice ["+temporary_ice_list.size()+"]",CLEANUP_DEBUG);
		for (TemporaryIce tl : temporary_ice_list) {
			tl.Cleanup();
		}
		log(ChatColor.YELLOW+"    "+(System.currentTimeMillis()-betweentime)+"ms",CLEANUP_DEBUG);
		betweentime = System.currentTimeMillis();
		log("Cleaning up Open Player Death Inventories ["+Bukkit.getOnlinePlayers().size()+"]",CLEANUP_DEBUG);
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (p.getOpenInventory()!=null && p.getOpenInventory().getTopInventory()!=null &&
					p.getOpenInventory().getTopInventory().getTitle()!=null &&
					DeathManager.deathStructureExists(p) && p.getOpenInventory().getTopInventory().getTitle().contains("Death Loot")) {
        		Location deathloc = DeathManager.getDeathStructure(p).deathloc;
        		DropDeathInventoryContents(p, deathloc);
			}
		}
		log(ChatColor.YELLOW+"    "+(System.currentTimeMillis()-betweentime)+"ms",CLEANUP_DEBUG);
		betweentime = System.currentTimeMillis();
		log("Cleaning up Temporary Chunks ["+temporary_chunks.size()+"]",CLEANUP_DEBUG);
		temporary_chunks.clear();
		log(ChatColor.YELLOW+"    "+(System.currentTimeMillis()-betweentime)+"ms",CLEANUP_DEBUG);
		betweentime = System.currentTimeMillis();
		log("Cleaning up Block Queue ["+blockqueue.size()+"]",CLEANUP_DEBUG);
		BlockModifyQueue.Cleanup(blockqueue);
		log(ChatColor.YELLOW+"    "+(System.currentTimeMillis()-betweentime)+"ms",CLEANUP_DEBUG);
		betweentime = System.currentTimeMillis();
		long endtime = System.currentTimeMillis();
		log("Cleanup Maintenance completed. Total Time: "+(endtime-starttime)+"ms.",CLEANUP_DEBUG);
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
		if (cmd.getName().equalsIgnoreCase("debugreport")) {
    		sender.sendMessage(ChatColor.YELLOW+"Server Structure Statistics");
    		if (args.length==0) {
	    		double tps = MinecraftServer.getServer().recentTps[0];
	    		int sno = SnowmanHuntList.size();
	    		int pla = playerdata.size();
	    		int liv = livingentitydata.size();
	    		int log = log_messages.size();
	    		int tem = temporary_lava_list.size();
	    		int tem2 = temporary_ice_list.size();
	    		int tem3 = temporary_chunks.size();
	    		int blo = blockqueue.size();
	    		int job = jobrecipes.size();
	    		int col = colors_used.size();
	    		int cha = chargezombies.size();
	    		int cus = custommonsters.size();
	    		int eli = elitemonsters.size();
	    		int ban = banksessions.size();
	    		int wea = weather_watch_users.size();
	    		int val = validsetitems.size();
	    		int tem4 = TEMPORARYABILITIES.size();
	    		int not = notWorldShop.size();
	    		int sup = suppressed_entities.size();
	    		int lav = lavaplume_list.size();
	    		int loc = habitat_data.locationhashes.size();
	    		int sta = habitat_data.startinglocs.size();
	    		int spl = TwosideSpleefGames.spleef_game_list.size();
	    		int nod = TwosideRecyclingCenter.nodes.size();
	    		int ite = TwosideRecyclingCenter.itemmap.size();
	    		int pri = WorldShop.pricelist.size();
	    		int ope = GetFullStructureMap("ope"); 
	    		int dam = GetFullStructureMap("dam"); 
	    		int dea = GetFullStructureMap("dea"); 
	    		int hit = GetFullStructureMap("hit"); 
	    		int ite2 = GetFullStructureMap("ite"); 
	    		int las = GetFullStructureMap("las"); 
	    		int blo2 = GetFullStructureMap("blo2"); 
	    		DecimalFormat df = new DecimalFormat("0.00");
	    		sender.sendMessage(ChatColor.WHITE+"TPS: "+GetTPSColor(tps)+df.format(tps));
	    		sender.sendMessage(ChatColor.WHITE+Display("SNO",sno)+Display("PLA",pla)+Display("LIV",liv));
	    		sender.sendMessage(ChatColor.WHITE+Display("LOG",log)+Display("TEM",tem)+Display("TEM2",tem2));
	    		sender.sendMessage(ChatColor.WHITE+Display("TEM3",tem3)+Display("BLO",blo)+Display("JOB",job));
	    		sender.sendMessage(ChatColor.WHITE+Display("COL",col)+Display("CHA",cha)+Display("CUS",cus));
	    		sender.sendMessage(ChatColor.WHITE+Display("ELI",eli)+Display("BAN",ban)+Display("WEA",wea));
	    		sender.sendMessage(ChatColor.WHITE+Display("VAL",val)+Display("TEM4",tem4)+Display("NOT",not));
	    		sender.sendMessage(ChatColor.WHITE+Display("SUP",sup)+Display("LAV",lav)+Display("LOC",loc));
	    		sender.sendMessage(ChatColor.WHITE+Display("STA",sta)+Display("SPL",spl)+Display("NOD",nod));
	    		sender.sendMessage(ChatColor.WHITE+Display("ITE",ite)+Display("PRI",pri)+Display("P-OPE",ope));
	    		sender.sendMessage(ChatColor.WHITE+Display("P-DEA",dea)+Display("P-HIT",hit)+Display("P-ITE2",ite2));
	    		sender.sendMessage(ChatColor.WHITE+Display("P-LAS",las)+Display("P-BLO2",blo2)+Display("P-DAM",dam));
	    		sender.sendMessage(ChatColor.WHITE+DisplayPlayerBar());
	    		sender.sendMessage(ChatColor.WHITE+"To view a specific player's usage, use "+ChatColor.GREEN+"\"/debugreport <name>\"");
	    		sender.sendMessage(ChatColor.WHITE+"To view specific entities' usage, use "+ChatColor.GREEN+"\"/debugreport ALLENTS\"");
    		} else {
    			if (args[0].equalsIgnoreCase("ALLENTS")) {
    				sender.sendMessage("Individual Structures for all Living Entities:");
    				int count=0;
    				StringBuilder line = new StringBuilder("");
    				for (LivingEntityStructure ent : TwosideKeeper.livingentitydata.values()) {
    					line.append("["+count+"]"+GenericFunctions.GetEntityDisplayName(ent.m)+":"+Display("H",ent.hitlist.size())+Display("G",ent.glowcolorlist.size())+" ");
    					count++;
    					if (count % 3 == 0) {
    						sender.sendMessage(line.toString());
    						line = new StringBuilder("");
    					}
    				}
    			} else {
    				if (Bukkit.getPlayer(args[0])!=null) {
    					Player pl = Bukkit.getPlayer(args[0]);
    					PlayerStructure pd = PlayerStructure.GetPlayerStructure(pl);
    					sender.sendMessage("Individual Structures for player "+ChatColor.YELLOW+pl.getName()+ChatColor.RESET+":");
    					sender.sendMessage(ChatColor.WHITE+Display("OPE",pd.openeditemcube.size())+Display("DAM",pd.damagedata.breakdownlist.size())+Display("DEA",pd.deathloot.size()));
    					sender.sendMessage(ChatColor.WHITE+Display("HIT",pd.hitlist.size())+Display("ITE",pd.itemcubelist.size())+Display("LAS",pd.lasteffectlist.size()));
    					sender.sendMessage(ChatColor.WHITE+Display("BLO",pd.blockscanlist.size()));
    				} else {
    					sender.sendMessage("Could not find player "+ChatColor.YELLOW+args[0]+ChatColor.RESET+"!");
    				}
    			}
    		}
    		return true;
    	}
		else
		if (cmd.getName().equalsIgnoreCase("stats")) {
			if (args.length>=1) {
				if (args[0].equalsIgnoreCase("equip")) {
					showPlayerStats((Player)sender,"equip");
				} else
				if (args[0].equalsIgnoreCase("all")) {
					showPlayerStats((Player)sender,"all");
				} else
				if (args.length>=2) {
					if (Bukkit.getPlayer(args[0])!=null) {
	    				//If we can grab their stats, then calculate it.
	    				Player p = Bukkit.getPlayer(args[0]);
	    				sender.sendMessage("Displaying stats for "+ChatColor.YELLOW+p.getName());
	    				if (args[1].equalsIgnoreCase("equip")) {
	    					showPlayerStats(p,sender,"equip");
	    				} else
	    				if (args[1].equalsIgnoreCase("all")) {
	    					showPlayerStats(p,sender,"all");
	    				} else {
	    					showPlayerStats(p,sender);
	    				}
	    			} else {
	    				sender.sendMessage("Player "+ChatColor.YELLOW+args[0]+" is not online!");
	    			}
				} else
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
				if (args.length>0) {
					ItemStack item = new ItemStack(p.getEquipment().getItemInMainHand().getType(),1,p.getEquipment().getItemInMainHand().getDurability());
					if (GenericFunctions.isEquip(item)) {
						item.setDurability((short) 0);
					}
					switch (args[0]) {
						case "price":{
							if (args.length<2) { //Display the price of the item in hand.
								if (item!=null && item.getType()!=Material.AIR) {
									double price = WorldShop.getBaseWorldShopPrice(item);
									p.sendMessage("The base shop price of "+ChatColor.YELLOW+GenericFunctions.UserFriendlyMaterialName(item)+ChatColor.RESET+" is "+ChatColor.GREEN+"$"+df.format(price)+ChatColor.RESET+".");
								} else {
									p.sendMessage("That is an invalid item!");
								}
							} else {
								if (p.getName().equalsIgnoreCase("ishiyama") || p.isOp()) {
									if (isNumeric(args[1])) {
										double newprice = Double.parseDouble(args[1]);
										if (item!=null && item.getType()!=Material.AIR) {
											double price = WorldShop.getBaseWorldShopPrice(item);
											Bukkit.broadcastMessage(ChatColor.YELLOW+"The base cost of "+ChatColor.GREEN+GenericFunctions.UserFriendlyMaterialName(item)+ChatColor.YELLOW+" has been updated!");
											Bukkit.broadcastMessage("   "+ChatColor.GRAY+ChatColor.STRIKETHROUGH+"$"+df.format(price)+ChatColor.RESET+" -> "+ChatColor.BLUE+ChatColor.BOLD+"$"+df.format(newprice));
											aPlugin.API.discordSendRawItalicized(ChatColor.YELLOW+"The base cost of **"+ChatColor.GREEN+GenericFunctions.UserFriendlyMaterialName(item)+ChatColor.YELLOW+"** has been updated!");
											aPlugin.API.discordSendRawItalicized("   ~~"+ChatColor.GRAY+ChatColor.STRIKETHROUGH+"$"+df.format(price)+ChatColor.RESET+"~~ -> **"+ChatColor.BLUE+ChatColor.BOLD+"$"+df.format(newprice)+"**");
											String searchstring = item.getType().name();
											if (item.getDurability()!=0) {
												searchstring = item.getType().name()+","+item.getDurability();
											}
											WorldShop.pricelist.put(searchstring, newprice);
											WorldShop.SaveAllPriceEntriesToFile();
										} else {
											p.sendMessage("That is an invalid item!");
										}
									} else {
										p.sendMessage("That is an invalid price!");
									}
								} else {
									p.sendMessage("No permission!");
								}
							}
						}break;
					}
				}
    			if (p.isOp()) {
    				/*PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
    				pd.swordcombo=20;*/
    				/*float f = ((org.bukkit.craftbukkit.v1_9_R1.entity.CraftLivingEntity)p).getHandle().getAbsorptionHearts();
    				log("Absorption Hearts: "+f,2);
    				if (args.length>0) {
    					((org.bukkit.craftbukkit.v1_9_R1.entity.CraftLivingEntity)p).getHandle().setAbsorptionHearts(Float.valueOf(args[0]));
    				}*/
    				if (args.length>0) {
    					switch (args[0]) {
    						case "ADD":{
    							ItemStack quiver = p.getInventory().getExtraContents()[0];
    							ArrowQuiver.addContents(ArrowQuiver.getID(quiver), p.getInventory().getItemInMainHand());
    							ArrowQuiver.updateQuiverLore(quiver);
    						}break;
    						case "REMOVE":{
    							ItemStack quiver = p.getInventory().getExtraContents()[0];
    							ArrowQuiver.removeContents(ArrowQuiver.getID(quiver), p.getInventory().getItemInMainHand());
    							ArrowQuiver.updateQuiverLore(quiver);
    						}break;
    						case "GET":{
    							ItemStack quiver = p.getInventory().getExtraContents()[0];
    							p.sendMessage("Quiver Mode: "+ArrowQuiver.getArrowQuiverMode(quiver));
    							ArrowQuiver.updateQuiverLore(quiver);
    						}break;
    						case "SET":{
    							ItemStack quiver = p.getInventory().getExtraContents()[0];
    							p.sendMessage("Quiver Mode: "+ArrowQuiver.setArrowQuiverMode(quiver, Integer.parseInt(args[1])));
    							p.sendMessage("Updated Quiver Mode: "+ArrowQuiver.getArrowQuiverMode(quiver));
    							ArrowQuiver.updateQuiverLore(quiver);
    						}break;
    						case "WITHER":{
    							//LivingEntity m = MonsterController.convertMonster((Monster)p.getWorld().spawnEntity(p.getLocation(),EntityType.WITHER), MonsterDifficulty.ELITE);
    							Wither w = (Wither)p.getWorld().spawnEntity(p.getLocation(), EntityType.WITHER);
    							w.setHealth(10);
    						}break;
    						case "ELITE":{
    							LivingEntity m = MonsterController.convertLivingEntity((LivingEntity)p.getWorld().spawnEntity(p.getLocation(),EntityType.ZOMBIE), LivingEntityDifficulty.ELITE);
    						}break;
    						case "VACUUM":{
    							ItemStack[] remaining = InventoryUtils.insertItemsInVacuumCube(p, new ItemStack(Material.ENDER_PEARL,16), new ItemStack(Material.IRON_PICKAXE,1), new ItemStack(Material.GOLDEN_APPLE,64));
    							for (ItemStack items : remaining) {
    								if (items!=null) {
    									p.getWorld().dropItemNaturally(p.getLocation(), items);
    								}
    							}
    						}break;
    						case "GLOWNEARBY":{
    							List<Entity> nearby = p.getNearbyEntities(10, 10, 10);
    							for (Entity e : nearby) {
    								if (!(e instanceof Player)) {
    									if (e.getCustomName()!=null) {
    										e.setCustomName(ChatColor.AQUA+e.getCustomName());
    									}
    									GlowAPI.setGlowing(e, GlowAPI.Color.AQUA, p);
    								}
    							}
    						}break;
    						case "SPAWN":{
    							Location loc = new Location(Bukkit.getWorld(args[1]),Double.parseDouble(args[2]),Double.parseDouble(args[3]),Double.parseDouble(args[4]));
    							
    							Item it = p.getWorld().dropItemNaturally(loc, new ItemStack(Material.DIAMOND));
    							if (it.isValid()) {
    								log("Spawned a "+it.getItemStack(),0);
    							} else {
    								log("Failed to spawn "+it.getItemStack(),0);
    							}
    						}break;
    						case "RECYCLE":{
    							Item it = p.getWorld().dropItemNaturally(p.getLocation(), new ItemStack(p.getEquipment().getItemInMainHand()));
    							it.setPickupDelay(100);
    							it.setTicksLived(28000);
    						}break;
    						case "STORM":{
    							TwosideKeeper.log("Storm: "+p.getWorld().hasStorm()+". Thunder: "+p.getWorld().isThundering(), 1);
    						}break;
    						case "SETRAIN":{
    							p.getWorld().setStorm(true);
    							//TwosideKeeper.log("Storm: "+p.getWorld().hasStorm()+". Thunder: "+p.getWorld().isThundering(), 1);
    						}break;
    						case "SETTHUNDER":{
    							p.getWorld().setThundering(true);
    							//TwosideKeeper.log("Storm: "+p.getWorld().hasStorm()+". Thunder: "+p.getWorld().isThundering(), 1);
    						}break;
    						case "BUILDTREE":{
    							if (args.length==4) {
    								TreeBuilder builder = new TreeBuilder(p.getLocation(),Integer.parseInt(args[1]),Integer.parseInt(args[2]),Integer.parseInt(args[3]));
    								builder.BuildTree();
    							} else {
    								p.sendMessage("/fix BUILDTREE <height> <radius> <baseradius>");
    							}
    						}break;
    						case "GIVESCHEMATIC":{
    							if (args.length==1) {
    								GenericFunctions.giveItem(p, Christmas.getChristmasTreeSchematic());
    							} else
    							if (args.length==2) {
    								ItemStack schematic = Christmas.getChristmasTreeSchematic();
    								schematic.setAmount(Integer.parseInt(args[1]));
    								GenericFunctions.giveItem(p, schematic);
    							} else {
    								p.sendMessage("/fix GIVESCHEMATIC [amount]");
    							}
    						}break;
    						case "FORCEBLOCKQUEUE":{
    							BlockModifyQueue.Cleanup(blockqueue);
    						}break;
    						case "COOKIE":{
    							if (args.length==1) {
    								GenericFunctions.giveItem(p, Christmas.getCookieItem());
    							} else
    							if (args.length==2) {
    								ItemStack schematic = Christmas.getCookieItem();
    								schematic.setAmount(Integer.parseInt(args[1]));
    								GenericFunctions.giveItem(p, schematic);
    							} else {
    								p.sendMessage("/fix COOKIE [amount]");
    							}
    						}break;
    						case "CHRISTMASBOX":{
    							if (args.length==1) {
    								GenericFunctions.giveItem(p, Christmas.getChristmasBox());
    							} else
    							if (args.length==2) {
    								ItemStack schematic = Christmas.getChristmasBox();
    								schematic.setAmount(Integer.parseInt(args[1]));
    								GenericFunctions.giveItem(p, schematic);
    							} else {
    								p.sendMessage("/fix "+args[0]+" [amount]");
    							}
    						}break;
    						case "CHRISTMASDECORATIONBOX":{
    							if (args.length==1) {
    								GenericFunctions.giveItem(p, Christmas.getChristmasDecorationBox());
    							} else
    							if (args.length==2) {
    								ItemStack schematic = Christmas.getChristmasDecorationBox();
    								schematic.setAmount(Integer.parseInt(args[1]));
    								GenericFunctions.giveItem(p, schematic);
    							} else {
    								p.sendMessage("/fix "+args[0]+" [amount]");
    							}
    						}break;
    						case "FIREWORKSHOOTER":{
    							if (args.length==1) {
    								GenericFunctions.giveItem(p, Christmas.getFireworkShooterBox());
    							} else
    							if (args.length==2) {
    								ItemStack schematic = Christmas.getFireworkShooterBox();
    								schematic.setAmount(Integer.parseInt(args[1]));
    								GenericFunctions.giveItem(p, schematic);
    							} else {
    								p.sendMessage("/fix "+args[0]+" [amount]");
    							}
    						}break;
    						case "ROCKETBOOSTER":{
    							if (args.length==1) {
    								GenericFunctions.giveItem(p, Christmas.getRocketBoosterItem());
    							} else
    							if (args.length==2) {
    								ItemStack schematic = Christmas.getRocketBoosterItem();
    								schematic.setAmount(Integer.parseInt(args[1]));
    								GenericFunctions.giveItem(p, schematic);
    							} else {
    								p.sendMessage("/fix "+args[0]+" [amount]");
    							}
    						}break;
    						case "CHRISTMASTOKEN":{
    							if (args.length==1) {
    								GenericFunctions.giveItem(p, Christmas.getChristmasEventToken());
    							} else
    							if (args.length==2) {
    								ItemStack schematic = Christmas.getChristmasEventToken();
    								schematic.setAmount(Integer.parseInt(args[1]));
    								GenericFunctions.giveItem(p, schematic);
    							} else {
    								p.sendMessage("/fix "+args[0]+" [amount]");
    							}
    						}break;
    						case "PARTICLE":{
    							aPluginAPIWrapper.sendParticle(p.getLocation(), EnumParticle.valueOf(args[1]), 0, 1, 0, 1, 50);
    						}break;
    						case "SPECIALHORSE":{
    		    				Horse h = (Horse)p.getWorld().spawnEntity(p.getLocation(), EntityType.HORSE);
    		    				h.setVariant(Variant.HORSE);
    		    				h.setColor(Horse.Color.WHITE);
    		    				h.setStyle(Style.values()[(int)(Math.random()*Style.values().length)]);
    		    				h.setTamed(true);
    		    				h.setAdult();
    		    				h.setJumpStrength(0.43+(0.13*8));
    		    				h.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.395f);
    		    				h.setInvulnerable(true);
    						}break;
    						case "GIVEFULLSET":{
    							for (ItemStack item : new ItemStack[]{
    									new ItemStack(Material.GOLD_HELMET),
    									new ItemStack(Material.GOLD_CHESTPLATE),
    									new ItemStack(Material.GOLD_LEGGINGS),
    									new ItemStack(Material.GOLD_BOOTS),
    									}) {
	    			    			List<String> lore = new ArrayList<String>();
	    			    			lore.add(ChatColor.GOLD+""+ChatColor.BOLD+"T"+Integer.parseInt(args[2])+" "+ItemSet.valueOf(args[1])+" Set");
	    			    			ItemMeta m = item.getItemMeta();
	    			    			m.setLore(lore);
	    			    			item.setItemMeta(m);
	    			    			p.getWorld().dropItemNaturally(p.getLocation(), item);
    							}
    						}break;
    						case "ICEWAND":{
    							GenericFunctions.giveItem(p, Christmas.getWinterSolsticeAugury());
    						}break;
    						case "SENDTREERULES":{
    							for (Player pl : Bukkit.getOnlinePlayers()) {
    								pl.sendMessage(ChatColor.GREEN+"Tree Climb Event - ");
    								pl.sendMessage(ChatColor.AQUA+"GOAL: "+ChatColor.RESET+"Get to the top Log of the Tree.");
    								pl.sendMessage(ChatColor.YELLOW+"RULES: ");
    								pl.sendMessage(ChatColor.WHITE+" - Only Holiday Cookies are allowed. All other items must be stored away!");
    								pl.sendMessage(ChatColor.WHITE+" - Leaves and Snow blocks may be broken. Logs CANNOT be broken.");
    								pl.sendMessage(ChatColor.WHITE+" - You can bring 5 Dirt Blocks with you to place during any portion of the Tree Climb. You may not recollect the dirt after placing it.");
    								pl.sendMessage(ChatColor.YELLOW+"PRIZES: ");
    								pl.sendMessage(ChatColor.WHITE+" - 1st Place: 3 Christmas Tokens");
    								pl.sendMessage(ChatColor.WHITE+" - 2nd/3rd Place: 2 Christmas Tokens");
    								pl.sendMessage(ChatColor.WHITE+" - Others: 1 Christmas Token");
    								pl.sendMessage("");
    								pl.sendMessage(ChatColor.WHITE+"Please proceed with emptying your inventories now and type\n"+ChatColor.GREEN+"/ready "+ChatColor.WHITE+"when you are ready!");
    							}
    						}break;
    						case "SENDRACERULES":{
    							for (Player pl : Bukkit.getOnlinePlayers()) {
    								pl.sendMessage(ChatColor.GREEN+"Tree Climb Event - ");
    								pl.sendMessage(ChatColor.AQUA+"GOAL: "+ChatColor.RESET+"Finish one lap around the icy plains of New World 2.");
    								pl.sendMessage(ChatColor.YELLOW+"RULES: ");
    								pl.sendMessage(ChatColor.WHITE+" - Only Holiday Cookies are allowed. All other items must be stored away!");
    								pl.sendMessage(ChatColor.WHITE+" - Only Swords/Bows may be kept on you for mobility. All other items are not permitted.");
    								pl.sendMessage(ChatColor.WHITE+" - You may not cut through the track or leave the track at any point in time.");
    								pl.sendMessage(ChatColor.YELLOW+"PRIZES: ");
    								pl.sendMessage(ChatColor.WHITE+" - 1st Place: 3 Christmas Tokens");
    								pl.sendMessage(ChatColor.WHITE+" - 2nd/3rd Place: 2 Christmas Tokens");
    								pl.sendMessage(ChatColor.WHITE+" - Others: 1 Christmas Token");
    								pl.sendMessage("");
    								pl.sendMessage(ChatColor.WHITE+"Please proceed with emptying your inventories now and type\n"+ChatColor.GREEN+"/red "+ChatColor.WHITE+"when you are ready!");
    							}
    						}break;
    						case "FINALGIFTS":{
    							for (Player pl : Bukkit.getOnlinePlayers()) {
    								GenericFunctions.giveItem(pl, Christmas.getSantaDimensionalBox());
    								GenericFunctions.giveItem(pl, Christmas.getWinterSolsticeAugury());
    							}
    						}break;
    						case "AWARDTOKEN":{
    							if (args.length==1) {
	    							for (Player pl : Bukkit.getOnlinePlayers()) {
	    								GenericFunctions.giveItem(pl, Christmas.getChristmasEventToken());
	    								pl.sendMessage("You've been awarded with "+ChatColor.LIGHT_PURPLE+"1"+ChatColor.RESET+" token!");
	    							}
    							} else {
    								ItemStack item = Christmas.getChristmasEventToken();
    								if (args.length==3) {
    									item.setAmount(Integer.parseInt(args[2]));
    								}
    								Bukkit.getPlayer(args[1]).sendMessage("You've been awarded with "+ChatColor.LIGHT_PURPLE+item.getAmount()+ChatColor.RESET+" token"+((item.getAmount()==1)?"":"s")+"!");
    								GenericFunctions.giveItem(Bukkit.getPlayer(args[1]), item);
    							}
    						}break;
    						case "CLEARQUARRY":{
    							Location startblock = p.getLocation().clone();
    							Block b = startblock.getBlock();
    							int delay=0;
								for (int y=0;y<10;y++) {
    							for (int x=0;x<340;x++) {
    								for (int z=0;z<30;z++) {
    									final int xer = x;
    									final int yer = y;
    									final int zer = z;
    									Bukkit.getScheduler().scheduleSyncDelayedTask(this, ()->{
    										b.getRelative(xer, yer, zer).setType(Material.AIR);},delay);
    									}
    								}	
									delay++;
								}
    						}break;
    						case "ADDCUBE":{
    							Collection<ItemStack> remaining = ItemCubeUtils.addItems(Integer.parseInt(args[1]), p.getEquipment().getItemInMainHand());
    							if (remaining.size()>0) {
    								for (ItemStack item : remaining) {
    									p.sendMessage("Could not fit "+GenericFunctions.UserFriendlyMaterialName(item)+" "+((item.getAmount()>1)?"x"+item.getAmount():""));
    								}
    							}
    						}break;
    						case "ADDHOTBARCUBE":{
    							Collection<ItemStack> remaining = ItemCubeUtils.addItems(Integer.parseInt(args[1]), GenericFunctions.getHotbarItems(p));
    							if (remaining.size()>0) {
    								for (ItemStack item : remaining) {
    									p.sendMessage("Could not fit "+GenericFunctions.UserFriendlyMaterialName(item)+" "+((item.getAmount()>1)?"x"+item.getAmount():""));
    								}
    							}
    						}break;
    						case "REMOVECUBE":{
    							Collection<ItemStack> remaining = ItemCubeUtils.removeItems(Integer.parseInt(args[1]), p.getEquipment().getItemInMainHand());
    							if (remaining.size()>0) {
    								for (ItemStack item : remaining) {
    									p.sendMessage("Could not remove "+GenericFunctions.UserFriendlyMaterialName(item)+" "+((item.getAmount()>1)?"x"+item.getAmount():""));
    								}
    							}
    						}break;
    						case "CLEARCUBE":{
    							ItemCubeUtils.clearItems(Integer.parseInt(args[1]));
    						}break;
    						case "REMOVESLOTCUBE":{
    							ItemStack remaining = ItemCubeUtils.removeItemFromSlot(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
    							p.sendMessage("Removed "+GenericFunctions.UserFriendlyMaterialName(remaining));
    						}break;
    						case "ADDDUSTTIME":{
    							ItemUtils.addArtifactDustTime(p.getEquipment().getItemInMainHand(), Integer.parseInt(args[1]));
    						}break;
    						case "SETDUSTTIME":{
    							ItemUtils.setArtifactDustTimeRemaining(p.getEquipment().getItemInMainHand(), Integer.parseInt(args[1]));
    						}break;
    						case "GETDUSTTIME":{
    							p.sendMessage(""+ItemUtils.getArtifactDustTimeRemaining(p.getEquipment().getItemInMainHand()));
    						}break;
    						case "GLOWINGBLOCK":{
    							FallingBlock fb = p.getWorld().spawnFallingBlock(p.getLocation(), p.getLocation().getBlock().getRelative(0, -1, 0).getType(), p.getLocation().getBlock().getRelative(0, -1, 0).getData());
								GlowAPI.setGlowing(fb, GlowAPI.Color.BLUE, Bukkit.getOnlinePlayers());
								fb.setMetadata("BREAKDOWN", new FixedMetadataValue(TwosideKeeper.plugin,true));
								fb.setDropItem(false);
    						}break;
    					}
    				}
    				//LivingEntity m = MonsterController.convertMonster((Monster)p.getWorld().spawnEntity(p.getLocation(),EntityType.ZOMBIE), MonsterDifficulty.ELITE);
    				/*
    				StackTraceElement[] stacktrace = new Throwable().getStackTrace();
    				StringBuilder stack = new StringBuilder("Mini stack tracer:");
    				for (int i=0;i<Math.min(10, stacktrace.length);i++) {
    					stack.append("\n"+stacktrace[i].getClassName()+": **"+stacktrace[i].getFileName()+"** "+stacktrace[i].getMethodName()+"():"+stacktrace[i].getLineNumber());
    				}
    				DiscordMessageSender.sendToSpam(stack.toString());*/
					/*Monster m = MonsterController.convertMonster((Monster)p.getWorld().spawnEntity(p.getLocation(),EntityType.ZOMBIE), MonsterDifficulty.ELITE);
					m.setHealth(m.getMaxHealth()/16);*/
    				//GenericFunctions.sendActionBarMessage(p, "Testing/nMultiple Lines.\nLolz");
    				//TwosideKeeperAPI.setItemSet(p.getEquipment().getItemInMainHand(), ItemSet.PANROS);
    				//p.getWorld().dropItemNaturally(p.getLocation(), TwosideKeeperAPI.generateMegaPiece(Material.LEATHER_CHESTPLATE, true, true, 5));
    				//p.getWorld().dropItemNaturally(p.getLocation(), HUNTERS_COMPASS.getItemStack());
    				//AwakenedArtifact.setEXP(p.getEquipment().getItemInMainHand(), 999);
    				/*p.getWorld().dropItemNaturally(p.getLocation(), UPGRADE_SHARD.getItemStack());
    				ItemStack upgrade = UPGRADE_SHARD.getItemStack();
    				GenericFunctions.setUpgradeShardTier(upgrade,3);
    				p.getWorld().dropItemNaturally(p.getLocation(), upgrade);*/
    				//log("In here",2);
    				/* RESULTS:
    				 * 	[15:42:16 INFO]: Inventory Contents (41 Items):
							Index 0-8: Hotbar
							Index 9-35: Inventory
							Index 36-39: Armor
							Index 40: Shield
						[15:42:16 INFO]: Armor Contents (4 Items)):
							Index 0-3: Armor from Helmet->Chestplate->Leggings->Boots
						[15:42:16 INFO]: Extra Contents (1 Item):
							Index 0: Shield Slot
						[15:42:16 INFO]: Storage Contents  (36 Items):
							Index 0-8: Hotbar
							Index 9-35: Inventory
    				 * 
    				 * log("Inventory Contents:",2);
    				ItemStack[] inv = p.getInventory().getContents();
    				for (int i=0;i<inv.length;i++) {
    					ItemStack item = inv[i];
    					if (item!=null) {
    						log("  "+inv[i],2);
    					}
    				}
    				log("Armor Contents:",2);
    				inv = p.getInventory().getArmorContents();
    				for (int i=0;i<inv.length;i++) {
    					ItemStack item = inv[i];
    					if (item!=null) {
    						log("  "+inv[i],2);
    					}
    				}
    				log("Extra Contents:",2);
    				inv = p.getInventory().getExtraContents();
    				for (int i=0;i<inv.length;i++) {
    					ItemStack item = inv[i];
    					if (item!=null) {
    						log("  "+inv[i],2);
    					}
    				}
    				log("Storage Contents:",2);
    				inv = p.getInventory().getStorageContents();
    				for (int i=0;i<inv.length;i++) {
    					ItemStack item = inv[i];
    					if (item!=null) {
    						log("  "+inv[i],2);
    					}
    				}*/
    				//GenericFunctions.addStackingPotionEffect(p, PotionEffectType.ABSORPTION, 40, 100);
    				//ArtifactAbility.removeAllEnchantments(p.getEquipment().getItemInMainHand());
    				//p.sendMessage("This is tier "+GenericFunctions.getUpgradeShardTier(p.getEquipment().getItemInMainHand()));
    				//ItemSet.SetTier(p.getEquipment().getItemInMainHand(), 7);
    				//p.getWorld().dropItemNaturally(p.getLocation(), STRENGTHENING_VIAL.getItemStack(50));
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
        			*/
    				/*TwosideKeeper.log("Suppressed: "+GenericFunctions.isSuppressed(p),1);
    				TwosideKeeper.log("Suppression Time: "+GenericFunctions.getSuppressionTime(p), 1);
    				GenericFunctions.setSuppressionTime(p, 20);
    				TwosideKeeper.log("Suppression Time: "+GenericFunctions.getSuppressionTime(p), 1);
    				GenericFunctions.addSuppressionTime(p, 30);
    				TwosideKeeper.log("Suppression Time: "+GenericFunctions.getSuppressionTime(p), 1);
    				TwosideKeeper.log("Suppressed: "+GenericFunctions.isSuppressed(p),1);*/
    				//ItemStack item = p.getEquipment().getItemInMainHand();
        			//AwakenedArtifact.addPotentialEXP(item, 50000, p);
    				/*FallingBlock fb = p.getWorld().spawnFallingBlock(p.getLocation(), Material.REDSTONE_BLOCK, (byte)0);
    				fb.setMetadata("DESTROY", new FixedMetadataValue(this,true));
    				GlowAPI.setGlowing(fb, GlowAPI.Color.YELLOW, Bukkit.getOnlinePlayers());*/
    				/*log("Start Time: "+System.currentTimeMillis(),0);
    				HashMap<Material,Block> blocklist = GenericFunctions.GetPlayerBlockList(p);
    				log("Finish Time: "+System.currentTimeMillis(),0);*/
    				//p.getEquipment().getItemInMainHand().setType(Material.SULPHUR);
    				//if (GenericFunctions.isSuppressed(p)
    				//TwosideKeeperAPI.removeAllArtifactAbilityPoints(p.getEquipment().getItemInMainHand());
        			//p.sendMessage(tpstracker.getTPS()+"");
        			//GenericFunctions.addObscureHardenedItemBreaks(p.getEquipment().getItemInMainHand(), 4);
    			}
	    		return true;
	    	} else
	    	if (cmd.getName().equalsIgnoreCase("money")) {
    			Player p = (Player)sender;
	    		sender.sendMessage("You are currently holding "+ChatColor.GREEN+"$"+df.format(getPlayerMoney(p)));
	    		sender.sendMessage(ChatColor.DARK_AQUA+"  Your bank balance is "+ChatColor.GREEN+"$"+df.format(getPlayerBankMoney(p)));
	    		return true;
	    	} else 
    		if (cmd.getName().equalsIgnoreCase("enchant_advanced")) {
	    		//Make sure we are holding an item.
    			Player p = (Player)sender;
    			if (p.getEquipment().getItemInMainHand()!=null) {
    				//sender.sendMessage(args[0]);
    				if (args.length==2) {
    					//p.getEquipment().getItemInMainHand().addUnsafeEnchantment(Enchantment.getById(Integer.parseInt(args[0])), Integer.parseInt(args[1]));
    					p.getEquipment().getItemInMainHand().addUnsafeEnchantment(Enchantment.getByName(args[0]), Integer.parseInt(args[1]));
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
	    					p.spigot().sendMessage(ArtifactAbility.GenerateMenu(ArtifactItemType.getArtifactItemTypeFromItemStack(p.getInventory().getArmorContents()[Integer.parseInt(args[1])-900]).getUpgradePath(), CustomDamage.getBaseWeaponDamage(p.getInventory().getArmorContents()[Integer.parseInt(args[1])-900], p, null), p.getInventory().getArmorContents()[Integer.parseInt(args[1])-900],Integer.parseInt(args[1])));
	    				}
	    			} else {
	    				if (p.getEquipment().getItemInMainHand()!=null && GenericFunctions.isArtifactEquip(p.getEquipment().getItemInMainHand())) {
	    					p.spigot().sendMessage(ArtifactAbility.GenerateMenu(ArtifactItemType.getArtifactItemTypeFromItemStack(p.getEquipment().getItemInMainHand()).getUpgradePath(), CustomDamage.getBaseWeaponDamage(p.getEquipment().getItemInMainHand(), p, null), p.getEquipment().getItemInMainHand()));
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
						p.spigot().sendMessage(ArtifactAbility.GenerateMenu(ArtifactItemType.getArtifactItemTypeFromItemStack(p.getEquipment().getItemInMainHand()).getUpgradePath(), CustomDamage.CalculateDamageReduction(1,p,null), p.getEquipment().getItemInMainHand()));
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
    		} else 
    		if (cmd.getName().equalsIgnoreCase("craft")) {
    			Player p = (Player)sender;
    			if (args.length==0) {
    				DisplayArguments(p);
    			} else {
    				if (args.length==1) {
    					DisplayArguments(p,args[0]);
    				} else {
    					DisplayCraftingRecipe(p,args[0]);
    				}
    			}
    			return true;
    		} else
	    	if (cmd.getName().equalsIgnoreCase("weather")) {
    			Player p = (Player)sender;
    			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
	    		if (args.length==0) {
	    			if (!pd.weatherwatch_user.isEmpty() && !pd.weatherwatch) {
		    			p.sendMessage("Turned "+ChatColor.GREEN+"ON"+ChatColor.RESET+" weather warning for Discord user "+pd.weatherwatch_user+".");
		    			p.sendMessage(ChatColor.ITALIC+"  To disable them, type /weather");
		    			pd.weatherwatch=true;
		    			AddUserToWeatherWatch(p);
	    			} else {
		    			p.sendMessage("Weather warnings are turned "+ChatColor.RED+"OFF"+ChatColor.RESET+".");
		    			p.sendMessage(ChatColor.ITALIC+"  To enable them, type /weather <DISCORD NAME>");
		    			p.sendMessage(ChatColor.ITALIC+"  and replace <DISCORD NAME> with your name.");
		    			pd.weatherwatch=false;
		    			RemoveUserFromWeatherWatch(p);
	    			}
	    		} else {
	    			p.sendMessage("Turned "+ChatColor.GREEN+"ON"+ChatColor.RESET+" weather warning for Discord user "+args[0]+".");
	    			p.sendMessage(ChatColor.ITALIC+"  To disable them, type /weather");
	    			pd.weatherwatch_user = args[0];
	    			pd.weatherwatch=true;
	    			AddUserToWeatherWatch(p);
	    		}
	    		return true;
	    	} else
	    	if (cmd.getName().equalsIgnoreCase("ready")) {
    			Player p = (Player)sender;
    			if (CHRISTMASEVENT_ACTIVATED) {
		    		switch (InventoryUtils.onlyHoldingFiveDirtBlocks(p)) {
			    		case HOLDING5DIRT:{
			    			Bukkit.broadcastMessage(p.getName()+" is "+ChatColor.GREEN+"READY"+".");
			    		}break;
						case NOTEMPTYINVENTORY:
			    			Bukkit.broadcastMessage(p.getName()+" is "+ChatColor.RED+"NOT READY"+".");
			    			p.sendMessage(" You need to get rid of "+ChatColor.RED+"ALL ITEMS"+ChatColor.RESET+" to play.");
							break;
						case NOTENOUGHDIRT:
			    			Bukkit.broadcastMessage(p.getName()+" is "+ChatColor.RED+"NOT READY"+".");
			    			p.sendMessage(" You need to have exactly "+ChatColor.YELLOW+"5 DIRT BLOCKS"+ChatColor.RESET+" to play.");
							break;
						case TOOMUCHDIRT:
			    			Bukkit.broadcastMessage(p.getName()+" is "+ChatColor.RED+"NOT READY"+".");
			    			p.sendMessage(" You need to have exactly "+ChatColor.YELLOW+"5 DIRT BLOCKS"+ChatColor.RESET+" to play.");
							break;
		    		}
    			} else {
    				if (args.length>0) {
    					Bukkit.broadcastMessage(p.getName()+" has issued a ready check. Type "+ChatColor.YELLOW+"/ready"+ChatColor.RESET+" to announce when you are ready.");
    				} else {
    					Bukkit.broadcastMessage(p.getName()+" is "+ChatColor.GREEN+"READY"+".");
    				}
    			}
	    		return true;
	    	} else
	    	if (cmd.getName().equalsIgnoreCase("red")) {
    			Player p = (Player)sender;
    			if (CHRISTMASEVENT_ACTIVATED) {
		    		if (InventoryUtils.onlyHoldingRacingItems(p)) {
		    			Bukkit.broadcastMessage(p.getName()+" is "+ChatColor.GREEN+"READY"+".");
		    		} else {
		    			Bukkit.broadcastMessage(p.getName()+" is "+ChatColor.RED+"NOT READY"+".");
		    			p.sendMessage(" You need to get rid of "+ChatColor.RED+"ALL ITEMS"+ChatColor.RESET+" to play.");
		    		}
    			}
	    		return true;
	    	} else
	    	if (cmd.getName().equalsIgnoreCase("habitat")) {
    			Player p = (Player)sender;
	    		p.sendMessage(aPlugin.API.getHabitatMap(p, 7));
	    		return true;
	    	}
    	} else {
    		//Implement console/admin version later (Let's you check any name's money.)
    	}
    	return false; 
    }
	
	private String DisplayPlayerBar() {
		StringBuilder str = new StringBuilder();
		for (Player p : Bukkit.getOnlinePlayers()) {
			str.append(GDC(GetTotalStructureSize(p))+p.getName().substring(0, 1).toUpperCase());
		}
		return str.toString();
	}
	private int GetTotalStructureSize(Player p) {
		int totalsize = 0;
		totalsize+=PlayerStructure.GetPlayerStructure(p).openeditemcube.size();
		totalsize+=PlayerStructure.GetPlayerStructure(p).damagedata.breakdownlist.size();
		totalsize+=PlayerStructure.GetPlayerStructure(p).deathloot.size();
		totalsize+=PlayerStructure.GetPlayerStructure(p).hitlist.size();
		totalsize+=PlayerStructure.GetPlayerStructure(p).itemcubelist.size();
		totalsize+=PlayerStructure.GetPlayerStructure(p).lasteffectlist.size();
		totalsize+=PlayerStructure.GetPlayerStructure(p).blockscanlist.size();
		return totalsize;
	}
	
	private int GetFullStructureMap(String string) {
		int total = 0;
		for (Player p : Bukkit.getOnlinePlayers()) {
			switch (string) {
				case "ope":{
					total+=PlayerStructure.GetPlayerStructure(p).openeditemcube.size();
				}break;
				case "dam":{
					total+=PlayerStructure.GetPlayerStructure(p).damagedata.breakdownlist.size();
				}break;
				case "dea":{
					total+=PlayerStructure.GetPlayerStructure(p).deathloot.size();
				}break;
				case "hit":{
					total+=PlayerStructure.GetPlayerStructure(p).hitlist.size();
				}break;
				case "ite":{
					total+=PlayerStructure.GetPlayerStructure(p).itemcubelist.size();
				}break;
				case "las":{
					total+=PlayerStructure.GetPlayerStructure(p).lasteffectlist.size();
				}break;
				case "blo":{
					total+=PlayerStructure.GetPlayerStructure(p).blockscanlist.size();
				}break;
			}
		}
		return total;
	}
	
	
	private String Display(String symbol,int val) {
		return symbol+":"+GDC(val)+val+ChatColor.RESET+" ";
	}

	//Stands for "Get Display Color"
    private ChatColor GDC(int val) {
		if (val>=10000) {
			return ChatColor.DARK_RED;
		} else
		if (val>=2500) {
			return ChatColor.RED;
		} else
		if (val>=1000) {
			return ChatColor.GOLD;
		} else
		if (val>=250) {
			return ChatColor.YELLOW;
		} else
		if (val>=100) {
			return ChatColor.GREEN;
		} else
		{
			return ChatColor.AQUA;
		} 
	}
	private ChatColor GetTPSColor(double tps) {
		if (tps>=19) {
			return ChatColor.DARK_GREEN;
		} else
		if (tps>=17) {
			return ChatColor.GREEN;
		} else
		if (tps>=14) {
			return ChatColor.YELLOW;
		} else
		if (tps>=11) {
			return ChatColor.GOLD;
		} else
		if (tps>=8) {
			return ChatColor.RED;
		} else
		{
			return ChatColor.DARK_RED;
		}
	}
	private void RemoveUserFromWeatherWatch(Player p) {
		weather_watch_users.remove(p.getName());
	}
	private void AddUserToWeatherWatch(Player p) {
		if (!weather_watch_users.contains(p.getName())) {
			weather_watch_users.add(p.getName());
		}
	}
	
	private void DisplayCraftingRecipe(Player p, String string) {
    	RecipeLinker l = RecipeLinker.valueOf(string);
    	ItemStack[] newarray = Arrays.copyOfRange(l.getRec(), 1, l.getRec().length);
    	if (p.hasPermission("createViaCraftMenu") && (getServerType()==ServerType.TEST || getServerType()==ServerType.QUIET)) {
    		GenericFunctions.giveItem(p, l.getRec()[0]);
    	} else {
    		aPlugin.API.viewRecipe(p, l.getRec()[0], newarray);
    	}
	}
	private void DisplayArguments(Player p) {
		p.sendMessage(ChatColor.GREEN+"Choose a category to view:");
		int j=0;
		TextComponent fin = new TextComponent("");
		for (int i=0;i<RecipeCategory.values().length;i++) {
			j++;
			RecipeCategory val = RecipeCategory.values()[i];
			/*
			TextComponent tc = new TextComponent(ChatColor.values()[j+2]+"["+val.getColor()+val.getName()+ChatColor.values()[j+2]+"] ");
			tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ComponentBuilder("Click to view the recipe for "+val.getColor()+val.getName()).create()));
			tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/craft "+val.name()));
			if (j>2) {
				tc.addExtra("\n");
				j=0;
			}
			fin.addExtra(tc);
			*/
			String cat = ChatColor.GOLD+""+ChatColor.BOLD+GenericFunctions.CapitalizeFirstLetters(val.name().replace("_", " "));
			TextComponent tc = new TextComponent(ChatColor.GREEN+"["+cat+ChatColor.GREEN+"] ");
			tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ComponentBuilder("Click to view the recipes in the "+cat+ChatColor.RESET+" category.").create()));
			tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/craft "+val.name()));
			if (j>1) {
				tc.addExtra("\n");
				j=0;
			}
			fin.addExtra(tc);
		}
		p.spigot().sendMessage(fin);
	}
	
	private void DisplayArguments(Player p,String arg) {
		p.sendMessage(ChatColor.GREEN+"Choose a recipe to view:");
		int j=0;
		TextComponent fin = new TextComponent("");
		for (int i=0;i<RecipeLinker.values().length;i++) {
			RecipeLinker val = RecipeLinker.values()[i];
			if (val.getCategory().equals(RecipeCategory.valueOf(arg))) {
				j++;
				TextComponent tc = new TextComponent(ChatColor.values()[j+2]+"["+val.getColor()+val.getName()+ChatColor.values()[j+2]+"] ");
				if (p.hasPermission("createViaCraftMenu") && (getServerType()==ServerType.TEST || getServerType()==ServerType.QUIET)) {
					tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ComponentBuilder("Click to be given a "+val.getColor()+val.getName()).create()));
				} else {
					tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ComponentBuilder("Click to view the recipe for "+val.getColor()+val.getName()).create()));
				}
				tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/craft "+val.name()+" view"));
				if (j>2) {
					tc.addExtra("\n");
					j=0;
				}
				fin.addExtra(tc);
			}
		}
		/*if (RecipeCategory.valueOf(arg)==RecipeCategory.MISC_ITEMS) {
			//Display the Custom Recipes.
			j++;
			JobRecipe jr = 
			TextComponent tc = new TextComponent(ChatColor.values()[j+2]+"["+val.getColor()+val.getName()+ChatColor.values()[j+2]+"] ");
			if (p.hasPermission("createViaCraftMenu") && (getServerType()==ServerType.TEST || getServerType()==ServerType.QUIET)) {
				tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ComponentBuilder("Click to be given a "+val.getColor()+val.getName()).create()));
			} else {
				tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ComponentBuilder("Click to view the recipe for "+val.getColor()+val.getName()).create()));
			}
			tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/craft "+val.name()+" view"));
			if (j>2) {
				tc.addExtra("\n");
				j=0;
			}
			fin.addExtra(tc);
		}*/
		p.spigot().sendMessage(fin);
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
				}
	    	},1);
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
        		aPlugin.API.discordSendRawItalicized(SERVER_TYPE.GetServerName()+msg);
        	}
			Bukkit.broadcastMessage(msg);
    	}
    }
    
    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void onServerCommand(PluginLoadEvent ev) {
    	//log("Called",2);
    	LOOT_TABLE_NEEDS_POPULATING=false;
    	Loot.DefineLootChests();
    	Christmas.SetupChristmas();
    }
    
    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void onWorldSave(WorldSaveEvent ev) {
    	if (ev.getWorld().getName().equalsIgnoreCase("world")) {
    		saveOurData();
    	}
    }
    
    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent ev) {
    	
    	for (int i=0;i<Bukkit.getOnlinePlayers().toArray().length;i++) {
    		Player p = (Player)Bukkit.getOnlinePlayers().toArray()[i];
    		if (p!=null) {
    			SoundUtils.playLocalSound(p, Sound.BLOCK_NOTE_PLING, 10, 1);
    			//LEAVE: Sound.NOTE_PLING, 8, 0.7f);
    			//MESSAGE: Sound.NOTE_STICKS, 0.6f, 0.85f);
    		}
    	}  
    	
    	/*if (SERVER_TYPE==ServerType.MAIN && !restarting_server) {
    		Bukkit.getScheduler().runTaskAsynchronously(this, pluginupdater);
    	}*/
    	
    	AnnounceDealOfTheDay(ev.getPlayer());
    	playerdata.put(ev.getPlayer().getUniqueId(), new PlayerStructure(ev.getPlayer(),getServerTickTime()));
    	log("[TASK] New Player Data has been added. Size of array: "+playerdata.size(),4);
    	
    	GenericFunctions.updateSetItemsInInventory(ev.getPlayer().getInventory());
    	ev.getPlayer().setCollidable(true);
    	
		GenericFunctions.logAndRemovePotionEffectFromEntity(PotionEffectType.LEVITATION,ev.getPlayer());
		GenericFunctions.logAndRemovePotionEffectFromEntity(PotionEffectType.JUMP,ev.getPlayer());
		ev.getPlayer().setVelocity(new Vector(0,0,0));
		CustomDamage.removeIframe(ev.getPlayer());
    	
    	//Update player max health. Check equipment too.
    	setPlayerMaxHealth(ev.getPlayer());
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "scoreboard players set "+ev.getPlayer().getName()+" Deaths "+ev.getPlayer().getStatistic(Statistic.DEATHS));
		GenericFunctions.logAndRemovePotionEffectFromEntity(PotionEffectType.GLOWING,ev.getPlayer());
		GenericFunctions.logAndRemovePotionEffectFromEntity(PotionEffectType.NIGHT_VISION,ev.getPlayer());
    	ev.getPlayer().getScoreboard().getTeam(ev.getPlayer().getName().toLowerCase()).setSuffix(createHealthbar(((ev.getPlayer().getHealth())/ev.getPlayer().getMaxHealth())*100,ev.getPlayer()));
    	ev.getPlayer().getScoreboard().getTeam(ev.getPlayer().getName().toLowerCase()).setPrefix(GenericFunctions.PlayerModePrefix(ev.getPlayer()));
		ev.getPlayer().getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4.0d);
    }
    
    public static void AnnounceDealOfTheDay(Player p) {
    	p.sendMessage("--------------------");
		p.sendMessage(ChatColor.DARK_AQUA+""+ChatColor.BOLD+"Deal of the Day:");
		DecimalFormat df = new DecimalFormat("0.00");
		DecimalFormat df2 = new DecimalFormat("0");
		p.sendMessage("   "+ChatColor.GREEN+""+ChatColor.BOLD+""+GenericFunctions.UserFriendlyMaterialName(TwosideKeeper.DEAL_OF_THE_DAY_ITEM)+" "+ChatColor.RESET+ChatColor.STRIKETHROUGH+"$"+df.format(TwosideKeeperAPI.getWorldShopItemBasePrice(TwosideKeeper.DEAL_OF_THE_DAY_ITEM))+ChatColor.RESET+ChatColor.GOLD+""+ChatColor.BOLD+" $"+df.format(TwosideKeeperAPI.getWorldShopItemBasePrice(TwosideKeeper.DEAL_OF_THE_DAY_ITEM)*(1-TwosideKeeper.DEAL_OF_THE_DAY_PCT))+"  "+ChatColor.DARK_GREEN+ChatColor.BOLD+""+df2.format(TwosideKeeper.DEAL_OF_THE_DAY_PCT*100)+"% Off");
		p.sendMessage("  "+ChatColor.RED+ChatColor.BOLD+"TODAY ONLY!"+ChatColor.RESET+ChatColor.YELLOW+" Find the offer at your local world shops!");
    	p.sendMessage("--------------------");
	}
    
	@EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void onPlayerLeave(PlayerQuitEvent ev) {
		Player p = ev.getPlayer();
    	TwosideSpleefGames.PassEvent(ev);
    	for (EliteMonster em : elitemonsters) {
    		em.runPlayerLeaveEvent(ev.getPlayer());
    	}
    	
    	for (UUID id : livingentitydata.keySet()) {
    		LivingEntityStructure les = LivingEntityStructure.getLivingEntityStructure(livingentitydata.get(id).m);
    		les.setGlow(ev.getPlayer(), null);
    	}
    	
    	for (Player pl :Bukkit.getOnlinePlayers()) {
    		if (pl!=null) {
    			SoundUtils.playLocalSound(pl, Sound.BLOCK_NOTE_PLING, 8, 0.7f);
    		}
    	}
    	
    	//Bukkit.getScheduler().scheduleSyncDelayedTask(this, new ShutdownServerForUpdate(),5);
    	
    	//Find the player that is getting removed.
    	PlayerStructure pd = (PlayerStructure)playerdata.get(ev.getPlayer().getUniqueId());
		//Make sure to save the config for this player.
		pd.saveConfig();
    	playerdata.remove(ev.getPlayer().getUniqueId());
		//Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "scoreboard players reset "+ev.getPlayer().getName().toLowerCase());
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
		        				thisp.sendMessage(ChatColor.RED+"You do not have that many experience points. You can convert up to "+ChatColor.WHITE+aPlugin.API.getTotalExperience(thisp)+ChatColor.RED+" experience points.");
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
			    					double bankmoney = getPlayerBankMoney(ev.getPlayer());
			    					if (bankmoney<value) {
			    						final ItemStack finalcheck = Check.createSignedBankCheckItem(value, ev.getPlayer().getName(),false);
										Bukkit.getScheduler().scheduleSyncDelayedTask(this, new WriteAndSignCheck(finalcheck, check, ev),1);
			    					} else {
			    						givePlayerBankMoney(ev.getPlayer(),-value);
			    						final ItemStack finalcheck = Check.createSignedBankCheckItem(value, ev.getPlayer().getName(),true);
										Bukkit.getScheduler().scheduleSyncDelayedTask(this, new WriteAndSignCheck(finalcheck, check, ev),1);
			    					}
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
	    			default:{
	    				
	    			}
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
								Bukkit.getScheduler().scheduleSyncDelayedTask(this, new CreateWorldShop(df, amt, ev, current_session),1);
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
								Bukkit.getScheduler().scheduleSyncDelayedTask(this, new CreateWorldPurchaseShop(current_session, amt, df, ev),1);
								
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
					case UPDATE:
						if (isNumeric(ev.getMessage())) {
							double amt = Double.parseDouble(ev.getMessage());
							WorldShop shop = TwosideShops.LoadWorldShopData(TwosideShops.GetShopID(current_session.GetSign())); 
							if (amt>=0.01 && amt<=999999999999.99) {
								shop.UpdateUnitPrice(amt);
								TwosideShops.SaveWorldShopData(shop);
								WorldShopManager.UpdateSign(shop, TwosideShops.GetShopID(current_session.GetSign()), current_session.GetSign(),false);
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
								WorldShopManager.UpdateSign(shop, TwosideShops.GetShopID(current_session.GetSign()), current_session.GetSign(),true);
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
											Bukkit.getScheduler().scheduleSyncDelayedTask(this, new GivePlayerPurchasedItems(cc, ev, dropitem),1);
											dropAmt-=shop.GetItem().getMaxStackSize();
											} else {
												shopItem.setAmount(dropAmt);
												final ItemStack dropitem = shopItem.clone();
												Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
													@Override
													public void run() {
														//ev.getPlayer().getWorld().dropItem(ev.getPlayer().getLocation(), dropitem).setPickupDelay(0);
														GenericFunctions.giveItem(ev.getPlayer(), dropitem);
														cc.getInventory().removeItem(dropitem);
													}
												},1);
												dropAmt=0;
											}
										}
										WorldShopManager.UpdateSign(shop, shop.getID(), current_session.GetSign(),false);
										TwosideShops.SaveWorldShopData(shop);
										TwosideShops.AddNewPurchase(shop.GetOwner(), ev.getPlayer().getName(), shop.GetItem(), amt*shop.GetUnitPrice(), amt);
										final int ID = shopID;
										Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
											@Override
											public void run() {
												WorldShop shop = TwosideShops.SetupNextItemShop(TwosideShops.LoadWorldShopData(ID), cc, current_session.GetSign());
												WorldShopManager.UpdateSign(shop, shop.getID(), current_session.GetSign(),false);
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
											WorldShopManager.UpdateSign(shop, shopID, current_session.GetSign(),true);
											TwosideShops.SaveWorldShopData(shop);
											TwosideShops.RemoveSession(ev.getPlayer());
											givePlayerMoney(ev.getPlayer(), amt*shop.GetUnitPrice());
											givePlayerBankMoney(shop.GetOwner(), -amt*shop.GetUnitPrice());
											TwosideShops.AddNewPurchase(shop.GetOwner(), ev.getPlayer().getName(), shop.GetItem(), amt*shop.GetUnitPrice(), amt, false);
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
	        		//aPlugin.API.discordSendRaw(("**"+ev.getPlayer().getName()+"** "+ev.getMessage().substring(0, pos)+"**["+ChatColor.stripColor(GenericFunctions.GetItemName(ev.getPlayer().getEquipment().getItemInMainHand()))+((ev.getPlayer().getEquipment().getItemInMainHand().getAmount()>1)?" x"+ev.getPlayer().getEquipment().getItemInMainHand().getAmount():"")+"]**"+"\n```"+WorldShop.GetItemInfo(ev.getPlayer().getEquipment().getItemInMainHand())+" ```\n"+ev.getMessage().substring(pos)));
	        		aPlugin.API.discordSendChat(ev.getPlayer().getName(), ev.getMessage().substring(0, pos)+"**["+ChatColor.stripColor(GenericFunctions.GetItemName(ev.getPlayer().getEquipment().getItemInMainHand()))+((ev.getPlayer().getEquipment().getItemInMainHand().getAmount()>1)?" x"+ev.getPlayer().getEquipment().getItemInMainHand().getAmount():"")+"]**"+"\n```\n"+WorldShop.GetItemInfo(ev.getPlayer().getEquipment().getItemInMainHand())+"\n```\n"+ev.getMessage().substring(pos));
	    			Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"tellraw @a [\"\",{\"text\":\"<"+ev.getPlayer().getName()+"> \"},{\"text\":\""+ev.getMessage().substring(0, pos)+"\"},{\"text\":\""+ChatColor.GREEN+"["+GenericFunctions.GetItemName(ev.getPlayer().getEquipment().getItemInMainHand())+ChatColor.RESET+ChatColor.YELLOW+((ev.getPlayer().getEquipment().getItemInMainHand().getAmount()>1)?" x"+ev.getPlayer().getEquipment().getItemInMainHand().getAmount():"")+ChatColor.GREEN+"]"+ChatColor.WHITE+"\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\""+GenericFunctions.GetItemName(ev.getPlayer().getEquipment().getItemInMainHand())+""+WorldShop.GetItemInfo(ev.getPlayer().getEquipment().getItemInMainHand()).replace("\"", "\\\"")+"\"}},{\"text\":\""+ev.getMessage().substring(pos)+"\"}]");
	    			
	    			ev.setCancelled(true);
	    		}
	    		//if (ev.getMessage().matches("[0]"))
	    		//Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"tellraw @a [\"\",{\"text\":\""+ChatColor.GREEN+"[Item]"+ChatColor.WHITE+"\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\""+(ev.getPlayer().getEquipment().getItemInMainHand().getType())+"\"}},{\"text\":\" "+ev.getMessage().substring(0, pos)+" \"}]");
    		}
    	}
    }
    
    public static void playMessageNotification(Player player) {
    	for (int i=0;i<Bukkit.getOnlinePlayers().toArray().length;i++) {
    		Player p = (Player)Bukkit.getOnlinePlayers().toArray()[i];
        	PlayerStructure pd = (PlayerStructure)playerdata.get(p.getUniqueId());
			if (pd.sounds_enabled) {
    			SoundUtils.playLocalSound(p, Sound.BLOCK_NOTE_BASEDRUM, 0.6f, 0.85f);
			}
    	}
	}

	@EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void onArrowHitBlock(ProjectileHitEvent ev) {
		if (ev.getEntity() instanceof Arrow) {
			Arrow a = (Arrow)ev.getEntity();
			a.setCustomName("HIT");
			return;
		}
		if (ev.getEntity() instanceof SmallFireball) {
			SmallFireball sf = (SmallFireball)ev.getEntity();
			if (sf.getShooter() instanceof Blaze) {
				Blaze b = (Blaze)sf.getShooter();
				LivingEntityDifficulty led = MonsterController.getLivingEntityDifficulty(b);
				switch (led) {
					case DANGEROUS:{
						GenericFunctions.DealExplosionDamageToEntities(ev.getEntity().getLocation(), 100f, 2, b);
						aPlugin.API.sendSoundlessExplosion(ev.getEntity().getLocation(), 2);
						SoundUtils.playGlobalSound(ev.getEntity().getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 0.6f, 0.5f);
					}break;
					case DEADLY:{
						GenericFunctions.DealExplosionDamageToEntities(ev.getEntity().getLocation(), 250f, 3, b);
						aPlugin.API.sendSoundlessExplosion(ev.getEntity().getLocation(), 3);
						SoundUtils.playGlobalSound(ev.getEntity().getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 0.6f, 0.5f);
					}break;
					case HELLFIRE:{
						GenericFunctions.DealExplosionDamageToEntities(ev.getEntity().getLocation(), 500f, 4, b);
						aPlugin.API.sendSoundlessExplosion(ev.getEntity().getLocation(), 4);
						SoundUtils.playGlobalSound(ev.getEntity().getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 0.6f, 0.5f);
						temporary_lava_list.add(new TemporaryLava(ev.getEntity().getLocation().getBlock().getRelative(0, 1, 0),4,true));
					}break;
					default:{
						
					}
				}
			}
		}
		if (ev.getEntity() instanceof Snowball) {
			Snowball sb = (Snowball)ev.getEntity();
			if (sb.hasMetadata("SPIDERBALL")) {
				GenericFunctions.createRandomWeb(sb.getLocation(),2);
			}
			return;
		}
	}
	
	@EventHandler(priority=EventPriority.LOW)
    public void onPlayerInteract(PlayerInteractEntityEvent ev) {
		log("Clicked with "+ ev.getHand().name(),5);
		log("Clicked on: "+ev.getRightClicked().getName(),5);
		Player p = ev.getPlayer();
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(ev.getPlayer());
		if (ev.getRightClicked() instanceof LivingEntity &&
				!(ev.getRightClicked() instanceof Player)) {
			LivingEntity ent = (LivingEntity)ev.getRightClicked();
			if (Christmas.isWinterSolsticeAugury(p.getEquipment().getItemInMainHand()) &&
					pd.icewandused+GenericFunctions.GetModifiedCooldown(TwosideKeeper.ICEWAND_COOLDOWN,ev.getPlayer())<getServerTickTime()) {
				//Freeze the entity in the nearest grid-locked square and set the AI to false.
				ent.setAI(false);
				ent.teleport(ent.getLocation().getBlock().getLocation().add(0.5,0,0.5));
				ent.setVelocity(new Vector(0,0,0));
				for (int y=0;y<3;y++) {
					temporary_ice_list.add(new TemporaryIce(20,ent.getLocation().getBlock().getRelative(0, y, 0),ent));
				}
				SoundUtils.playGlobalSound(p.getLocation(), Sound.BLOCK_PORTAL_TRIGGER, 0.7f, 1.6f);
	    		aPlugin.API.sendCooldownPacket(p, p.getEquipment().getItemInMainHand(), GenericFunctions.GetModifiedCooldown(TwosideKeeper.ICEWAND_COOLDOWN,p));
	    		pd.icewandused=TwosideKeeper.getServerTickTime();
				return;
			}
		}
		if (ev.getPlayer().getEquipment().getItemInMainHand().getType()==Material.NAME_TAG && (ev.getRightClicked() instanceof LivingEntity)) {
			//TwosideKeeper.log("Check this out.", 2);
			LivingEntity m = (LivingEntity)ev.getRightClicked();
			//MonsterController.convertMonster(m,md);
			final String oldname = m.getCustomName();
			Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
				@Override
				public void run() {
					if (MonsterController.getLivingEntityDifficulty(m)!=LivingEntityDifficulty.NORMAL) {
						log("It's not normal!",5);
						m.setCustomName(oldname);
						ev.getPlayer().getEquipment().getItemInMainHand().setAmount(ev.getPlayer().getEquipment().getItemInMainHand().getAmount()+1);
						}
				}
			},1);
		}
		
		if (PlayerMode.getPlayerMode(p)==PlayerMode.BARBARIAN && ev.getRightClicked() instanceof LivingEntity) {
			aPlugin.API.swingOffHand(p);
			if (pd.weaponcharges>=10 && (pd.weaponcharges<30 || !p.isSneaking())) {
				//Apply a stronger attack.
				CustomDamage.ApplyDamage(0, p, (LivingEntity)ev.getRightClicked(), p.getInventory().getExtraContents()[0], "Power Swing");
			} else
			if (pd.weaponcharges>=30 && p.isSneaking()) {
				//Apply Sweep Up Attack.
				pd.weaponcharges-=30;
				CustomDamage.IncreaseLifestealStacks(p, pd.lifestealstacks);
				GenericFunctions.sendActionBarMessage(p, "");
				//CustomDamage.ApplyDamage(0, p, (LivingEntity)ev.getRightClicked(), p.getInventory().getExtraContents()[0], "Sweep Up");
				SoundUtils.playLocalSound(p, Sound.ENTITY_HOSTILE_SWIM, 3.0f, 2.0f);
				GenericFunctions.DealDamageToNearbyMobs(p.getLocation(), 0, 4, true, 0.9, p, p.getInventory().getExtraContents()[0], false, "Sweep Up");
			} else
			{
				CustomDamage.ApplyDamage(0, p, (LivingEntity)ev.getRightClicked(), p.getInventory().getItem(40), null);
				if (p.getInventory().getItem(40).getDurability()>p.getInventory().getItem(40).getType().getMaxDurability()) {
					p.getInventory().setItem(40, new ItemStack(Material.AIR));
				}
			}
		}
		
		if (pd.lastrightclick+20<=getServerTickTime() && EntityUtils.ProperlyStoreEnderCrystal(ev.getRightClicked())) {
			pd.lastrightclick=getServerTickTime();
			ev.setCancelled(true);
			return;
		}

		if ((ev.getRightClicked() instanceof LivingEntity) && (ev.getHand()==EquipmentSlot.OFF_HAND) &&
				GenericFunctions.isArtifactEquip(ev.getPlayer().getEquipment().getItemInMainHand())) {
			boolean bursted=false;
			bursted = performDeathMark(ev.getPlayer(), bursted);
			if (bursted) {
				//Cancel this then, because we decided to burst our stacks instead.
				ev.setCancelled(true);
			}
		}
		if (PlayerMode.getPlayerMode(p)==PlayerMode.BARBARIAN && ev.getRightClicked() instanceof LivingEntity) {
			aPlugin.API.swingOffHand(p);
			if (pd.weaponcharges>=10 && (pd.weaponcharges<30 || !p.isSneaking())) {
				//Apply a stronger attack.
				CustomDamage.ApplyDamage(0, p, (LivingEntity)ev.getRightClicked(), p.getInventory().getExtraContents()[0], "Power Swing");
			} else
			{
				CustomDamage.ApplyDamage(0, p, (LivingEntity)ev.getRightClicked(), p.getInventory().getItem(40), null);
				if (p.getInventory().getItem(40).getDurability()>p.getInventory().getItem(40).getType().getMaxDurability()) {
					p.getInventory().setItem(40, new ItemStack(Material.AIR));
				}
			}
		}
		if (p.getHealth()>p.getMaxHealth()*0.1 && ItemSet.HasSetBonusBasedOnSetBonusCount(GenericFunctions.getArmor(p), p, ItemSet.COMET, 4)) {
			if (ev.getRightClicked() instanceof Player) {
				Player pl = (Player)ev.getRightClicked();
				if (pl.getHealth()<pl.getMaxHealth()) {
					p.setHealth(p.getHealth()-(p.getMaxHealth()*0.1));
					aPlugin.API.sendEntityHurtAnimation(p);
					pl.setHealth(Math.min(pl.getMaxHealth(), pl.getHealth()+(pl.getMaxHealth()*0.2)));
					GenericFunctions.addIFrame(pl, 5);
					SoundUtils.playGlobalSound(p.getLocation(), Sound.BLOCK_REDSTONE_TORCH_BURNOUT, 1.0f, 1.0f);
					SoundUtils.playGlobalSound(pl.getLocation(), Sound.BLOCK_BREWING_STAND_BREW, 1.0f, 1.6f);
					return;
				}
			}
		}
		if (ItemSet.HasSetBonusBasedOnSetBonusCount(GenericFunctions.getArmor(p), p, ItemSet.CUPID, 4)) {
			if (ev.getRightClicked() instanceof Player) {
				Player pl = (Player)ev.getRightClicked();
				LinkPlayerToOtherPlayer(p,pl);
			}
		}
		/*if (ev.getRightClicked() instanceof Monster) {
			TwosideKeeperAPI.DealDamageToEntity(TwosideKeeperAPI.getFinalDamage(500.0, ev.getPlayer(), (Monster)ev.getRightClicked(),  true, "ROFL"), (Monster)ev.getRightClicked(), ev.getPlayer());
		}*/
		///if (ev.getHand()==EquipmentSlot.OFF_HAND) {aPlugin.API.swingOffHand(ev.getPlayer());};
	}

	private void LinkPlayerToOtherPlayer(Player p, Player pl) {
		SoundUtils.playGlobalSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 1.3f);
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, ()->{SoundUtils.playGlobalSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 1.8f);},5);
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		PlayerStructure pld = PlayerStructure.GetPlayerStructure(pl);
		if (pd.linkplayer!=null) {
			GlowAPI.setGlowing(pd.linkplayer, false, p);
		}
		if (pld.linkplayer!=null) {
			GlowAPI.setGlowing(pld.linkplayer, false, pl);
		}
		pd.linkplayer=pl;
		pld.linkplayer=p;
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler(priority=EventPriority.LOW,ignoreCancelled=true)
    public void onPlayerSneak(PlayerToggleSneakEvent ev) {
		Player p = ev.getPlayer();
		if (PlayerMode.getPlayerMode(p)==PlayerMode.SLAYER) {
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			if (ev.isSneaking()) {	
				pd.turnedonsneak=getServerTickTime();
				pd.lastsneak=getServerTickTime();
				//TwosideKeeper.log("Turned on sneak SET "+getServerTickTime(), 1);
			}
			if (!ev.isSneaking() && p.isOnGround() && pd.turnedonsneak+10>getServerTickTime()) {
				if (!GenericFunctions.hasStealth(p)) {
					GenericFunctions.applyStealth(p,true);
				} else {
					GenericFunctions.removeStealth(p);
				}
			}
		}
	}
	
	@EventHandler(priority=EventPriority.LOW)
    public void onPlayerInteract(PlayerInteractEvent ev) {
	  if (ev.getHand()==EquipmentSlot.OFF_HAND) {return;}
	  if (ev.isCancelled() && ev.getAction() == Action.RIGHT_CLICK_BLOCK) {
            return;
        } else {
        	Block b = ev.getClickedBlock();
	    	log("Interaction type: "+ev.getAction().toString(),5);
	    	
	    	//Pass along this event to Spleef Games.
	    	TwosideSpleefGames.PassEvent(ev);
	    	
	    	final Player p = ev.getPlayer();
			Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
				@Override
				public void run() {
			    	setPlayerMaxHealth(p);
				}
			},1);
			
			if (!Christmas.RunPlayerInteractEvent(ev)) {return;}
			
			if (ev.getClickedBlock()!=null && ev.getClickedBlock().getType()==Material.CHEST &&
					TwosideRecyclingCenter.isChoosingRecyclingCenter() &&
					ev.getPlayer().hasPermission("TwosideKeeper.recyclingcenter")) {
				TwosideRecyclingCenter.setChoosingRecyclingCenter(false);
				//Create a new Recycling Center.
				TwosideRecyclingCenter.AddNode(ev.getClickedBlock().getWorld(), ev.getClickedBlock().getLocation().getBlockX(), ev.getClickedBlock().getLocation().getBlockY(), ev.getClickedBlock().getLocation().getBlockZ());
				TwosideRecyclingCenter.populateItemListFromNode(ev.getClickedBlock().getLocation());
				ev.getPlayer().sendMessage(ChatColor.DARK_BLUE+"New Recycling Center successfully created at "+ev.getClickedBlock().getLocation().toString());
				ev.setCancelled(true);
				return;
			}
			
			if ((ev.getAction()==Action.LEFT_CLICK_AIR || ev.getAction()==Action.LEFT_CLICK_BLOCK) && p.isSneaking() &&
					PlayerMode.getPlayerMode(p)==PlayerMode.BARBARIAN) {
				PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
				if (pd.weaponcharges>=30) {
					SoundUtils.playGlobalSound(p.getLocation(), Sound.BLOCK_WOOD_BUTTON_CLICK_ON, 3.0f, 0.6f);
					//Apply 10 strikes across the field.
					//dmg*=2;
					ItemStack weapon = p.getEquipment().getItemInMainHand();
					double xspd=p.getLocation().getDirection().getX();
					double zspd=p.getLocation().getDirection().getZ();
					Location attackloc = p.getLocation().clone();
					for (int i=0;i<10;i++) {
						attackloc = attackloc.add(xspd,0,zspd);
						SoundUtils.playGlobalSound(p.getLocation(), Sound.ENTITY_ENDERDRAGON_FIREBALL_EXPLODE, 0.1f, 1.4f);
						aPlugin.API.sendSoundlessExplosion(attackloc, 0.6f);
						GenericFunctions.DealDamageToNearbyMobs(attackloc, 0, 1, true, 0.6, p, weapon, false, "Forceful Strike");
					}
					pd.weaponcharges-=30;
					GenericFunctions.sendActionBarMessage(p, "");
				}
			}
			
			if ((ev.getAction()==Action.RIGHT_CLICK_AIR || ev.getAction()==Action.RIGHT_CLICK_BLOCK) && p.isSneaking() &&
					PlayerMode.getPlayerMode(p)==PlayerMode.BARBARIAN) {
				PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
				if (pd.weaponcharges>=30) {
					//Apply Sweep Up Attack.
					pd.weaponcharges-=30;
					CustomDamage.IncreaseLifestealStacks(p, pd.lifestealstacks);
					GenericFunctions.sendActionBarMessage(p, "");
					//CustomDamage.ApplyDamage(0, p, (LivingEntity)ev.getRightClicked(), p.getInventory().getExtraContents()[0], "Sweep Up");
					SoundUtils.playLocalSound(p, Sound.ENTITY_HOSTILE_SWIM, 3.0f, 2.0f);
					GenericFunctions.DealDamageToNearbyMobs(p.getLocation(), 0, 4, true, 0.9, p, p.getInventory().getExtraContents()[0], false, "Sweep Up");
				}
			}
			
			if (ItemUtils.isArtifactDust(p.getEquipment().getItemInMainHand()) && (ev.getAction()==Action.RIGHT_CLICK_AIR || ev.getAction()==Action.RIGHT_CLICK_BLOCK) &&
					ev.useInteractedBlock()==Result.DENY) {
				long time = TwosideKeeper.getServerTickTime();
				List<String> oldlore = p.getEquipment().getItemInMainHand().getItemMeta().getLore();
				for (int i=0;i<oldlore.size();i++) {
					if (oldlore.get(i).contains(ChatColor.BLUE+""+ChatColor.MAGIC)) {
						//See what the previous time was.
						time = Long.parseLong(ChatColor.stripColor(oldlore.get(i)));
						TwosideKeeper.log("Time is "+time, 5);
					}
				}
				long tickdiff = (time+12096000)-TwosideKeeper.getServerTickTime();
				TwosideKeeper.log("tickdiff is "+tickdiff, 5);
				DecimalFormat df = new DecimalFormat("00");
				if (tickdiff<0) {
					GenericFunctions.convertArtifactDustToItem(p.getEquipment().getItemInMainHand());
				} else {
					p.sendMessage("Your "+GenericFunctions.UserFriendlyMaterialName(p.getEquipment().getItemInMainHand())+ChatColor.RESET+" will reactivate its energy in "+ChatColor.AQUA+DisplayTimeDifference(tickdiff));
				}
			}
			
			if ((ev.getAction()==Action.RIGHT_CLICK_AIR || ev.getAction()==Action.RIGHT_CLICK_BLOCK) &&
					PlayerMode.getPlayerMode(p)==PlayerMode.BARBARIAN) {
				aPlugin.API.swingOffHand(p);
			}
			
			//Check for a Hunter's Compass right-click.
			if ((ev.getAction()==Action.RIGHT_CLICK_AIR || ev.getAction()==Action.RIGHT_CLICK_BLOCK) && GenericFunctions.isHunterCompass(p.getEquipment().getItemInMainHand())) {
				PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
				if (pd.lastrightclick+100<=getServerTickTime()) {
					pd.lastrightclick=getServerTickTime();
					p.sendMessage("Calibrating "+p.getEquipment().getItemInMainHand().getItemMeta().getDisplayName()+ChatColor.WHITE+"...");
					String name = p.getEquipment().getItemInMainHand().getItemMeta().getDisplayName();
			    	if (Math.random()<=0.5) {
			    		if (p.getEquipment().getItemInMainHand().getAmount()<=1) {
			    			p.getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
			    			SoundUtils.playLocalSound(p, Sound.BLOCK_METAL_BREAK, 1.0f, 1.0f);
			    		} else {
			    			p.getEquipment().getItemInMainHand().setAmount(p.getEquipment().getItemInMainHand().getAmount()-1);
			    		}
						Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
							@Override
							public void run() {
								p.sendMessage("The "+name+ChatColor.WHITE+" is now...");
							}
						},15);
						Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
							@Override
							public void run() {
								SoundUtils.playLocalSound(p, Sound.BLOCK_METAL_BREAK, 1.0f, 1.0f);
							}
						},20);
						Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
							@Override
							public void run() {
								p.sendMessage(ChatColor.ITALIC+"  Oh my! It appears to have broke!");
							}
						},45);
			    	} else {
						boolean pointToExistingElite=false;
						for (int i=0;i<elitemonsters.size();i++) {
							if (Math.random()<=0.5) {
								TwosideKeeper.ELITE_LOCATION = elitemonsters.get(i).m.getLocation();
								pointToExistingElite=true;
								p.sendMessage("The "+name+ChatColor.WHITE+" is now properly calibrated!");
								p.sendMessage(ChatColor.ITALIC+"  Good luck on your adventure!");
								p.setCompassTarget(TwosideKeeper.ELITE_LOCATION);
								break;
							}
						}
						if (!pointToExistingElite) {
							pd.lastcompassnotification=getServerTickTime();
							GenericFunctions.generateNewElite(p,name);
						}
			    	}
			    	ev.setCancelled(true);
			    	return;
				}
			}
			
			//Check for a bow shift-right click.
			if (ev.getAction()==Action.RIGHT_CLICK_AIR || ev.getAction()==Action.RIGHT_CLICK_BLOCK
					 || ev.getAction()==Action.LEFT_CLICK_BLOCK
					 || ev.getAction()==Action.LEFT_CLICK_AIR) {
				if (PlayerMode.isRanger(p) && p.isSneaking() && p.getEquipment().getItemInMainHand().getType()==Material.BOW) {
					//Rotate Bow Modes.
					GenericFunctions.logAndRemovePotionEffectFromEntity(PotionEffectType.SLOW,p);
					BowMode mode = GenericFunctions.getBowMode(p);
					PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
					if (ev.getAction().name().contains("RIGHT")) {
						if (pd.lastbowmodeswitch+6>=getServerTickTime()) {
							return;
						}
						switch (mode) {
							case CLOSE:{
								SoundUtils.playLocalSound(p, Sound.ENTITY_ZOMBIE_INFECT, 0.5f, 0.1f);
								GenericFunctions.setBowMode(p,BowMode.SNIPE);
								aPlugin.API.sendCooldownPacket(p, p.getEquipment().getItemInMainHand(), GenericFunctions.GetRemainingCooldownTime(p, pd.last_arrowbarrage, ARROWBARRAGE_COOLDOWN));
							}break;
							case SNIPE:{
								SoundUtils.playLocalSound(p, Sound.BLOCK_BREWING_STAND_BREW, 0.5f, 0.1f);
								GenericFunctions.setBowMode(p,BowMode.DEBILITATION);
								aPlugin.API.sendCooldownPacket(p, p.getEquipment().getItemInMainHand(), GenericFunctions.GetRemainingCooldownTime(p, pd.last_siphon, SIPHON_COOLDOWN));
							}break;
							case DEBILITATION:{
								SoundUtils.playLocalSound(p, Sound.BLOCK_CHEST_LOCKED, 0.5f, 3.5f);
								GenericFunctions.setBowMode(p,BowMode.CLOSE);
								aPlugin.API.sendCooldownPacket(p, p.getEquipment().getItemInMainHand(), GenericFunctions.GetRemainingCooldownTime(p, pd.last_dodge, DODGE_COOLDOWN));
							}break;
						}
						pd.lastbowmodeswitch=getServerTickTime();
					} else {
						if (pd.lastbowmodeswitch+6>=getServerTickTime()) {
							return;
						}
						switch (mode) {
							case CLOSE:{
								SoundUtils.playLocalSound(p, Sound.BLOCK_BREWING_STAND_BREW, 0.5f, 0.1f);
								GenericFunctions.setBowMode(p,BowMode.DEBILITATION);
								//GenericFunctions.applyModeName(p.getEquipment().getItemInMainHand());
								p.updateInventory();
								aPlugin.API.sendCooldownPacket(p, p.getEquipment().getItemInMainHand(), GenericFunctions.GetRemainingCooldownTime(p, pd.last_siphon, SIPHON_COOLDOWN));
							}break;
							case SNIPE:{
								SoundUtils.playLocalSound(p, Sound.BLOCK_CHEST_LOCKED, 0.5f, 3.5f);
								GenericFunctions.setBowMode(p,BowMode.CLOSE);
								//GenericFunctions.applyModeName(p.getEquipment().getItemInMainHand());
								p.updateInventory();
								aPlugin.API.sendCooldownPacket(p, p.getEquipment().getItemInMainHand(), GenericFunctions.GetRemainingCooldownTime(p, pd.last_dodge, DODGE_COOLDOWN));
							}break;
							case DEBILITATION:{
								SoundUtils.playLocalSound(p, Sound.ENTITY_ZOMBIE_INFECT, 0.5f, 0.1f);
								GenericFunctions.setBowMode(p,BowMode.SNIPE);
								//GenericFunctions.applyModeName(p.getEquipment().getItemInMainHand());
								p.updateInventory();
								aPlugin.API.sendCooldownPacket(p, p.getEquipment().getItemInMainHand(), GenericFunctions.GetRemainingCooldownTime(p, pd.last_arrowbarrage, ARROWBARRAGE_COOLDOWN));
							}break;
						}
						pd.lastbowmodeswitch=getServerTickTime();
					}
					ev.setCancelled(true);
					return;
				}
			}
			
			//Check for Earth Wave attack.
			if ((ev.getAction()==Action.RIGHT_CLICK_AIR || ev.getAction()==Action.RIGHT_CLICK_BLOCK) && !p.isOnGround()) {
				ItemStack weapon = p.getEquipment().getItemInMainHand();
				PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
				double dmg = 0;
				if (GenericFunctions.isArtifactEquip(weapon) &&
						weapon.toString().contains("SPADE")) {
					if (ArtifactAbility.containsEnchantment(ArtifactAbility.EARTHWAVE, weapon) &&
							pd.lastusedearthwave+10<TwosideKeeper.getServerTickTime()) {
						dmg = GenericFunctions.getAbilityValue(ArtifactAbility.EARTHWAVE, weapon);
						int falldist = 0;
						Location checkloc = p.getLocation().clone();
						while (checkloc.add(0,-1,0).getBlock().getType()==Material.AIR) {
							falldist++;
						}
						if (falldist>1) {
							//Now that we have the fall distance, create an Earth Wave around us the size of falldist/2.
							//Teleport the player to the location we'd fall from.
							Location snaploc = p.getLocation().getBlock().getLocation().clone();
							snaploc.setPitch(p.getLocation().getPitch());
							snaploc.setYaw(p.getLocation().getYaw());
							p.teleport(snaploc.add(0.5,0,0.5));
							p.setFlying(false);
							GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.LEVITATION, falldist, -124, p, true);
							p.setVelocity(new Vector(0,-50,0));
							double vel = Math.pow(falldist, 0.2);
							int counter=0;
							falldist = Math.min(falldist, 20); //Limit the maximum distance to 20 blocks out.
							while (falldist>0) {
								Bukkit.getScheduler().scheduleSyncDelayedTask(this, new EarthWaveTask(checkloc,counter+1,vel,dmg,p), counter*4);
								falldist--;
								counter++;
								SoundUtils.playGlobalSound(checkloc, Sound.BLOCK_CHORUS_FLOWER_DEATH, 1.0f, 1.0f);
							}
							SoundUtils.playLocalSound(p, Sound.ENTITY_FIREWORK_LARGE_BLAST, 1.0f, 1.0f);
							
							/*aPlugin.API.sendCooldownPacket(p, p.getEquipment().getItemInMainHand(), GenericFunctions.GetModifiedCooldown(TwosideKeeper.ERUPTION_COOLDOWN,p));
							pd.last_shovelspell=TwosideKeeper.getServerTickTime()+GenericFunctions.GetModifiedCooldown(TwosideKeeper.ERUPTION_COOLDOWN,p);*/
							pd.lastusedearthwave=TwosideKeeper.getServerTickTime();
							aPlugin.API.damageItem(p, weapon, (int) (weapon.getType().getMaxDurability()*0.05+5));
							for (int x=-1;x<2;x++) {
								for (int z=-1;z<2;z++) {
									if (x!=0 && z!=0) {
										Location newblock = checkloc.clone();
										if (!GenericFunctions.isSoftBlock(newblock.getBlock().getRelative(x, 0, z).getType())) {
											TwosideKeeper.log("NOT SOFT!", 0);
											aPlugin.API.damageItem(p, weapon, (int) (weapon.getType().getMaxDurability()*0.01+1));
										}
									}
								}
							}
						}
					}
				}
			}
			
			//Check for a Sword left click.
			if (ev.getAction()==Action.LEFT_CLICK_AIR || ev.getAction()==Action.LEFT_CLICK_BLOCK) {
				//log("SHOP? "+TwosideKeeperAPI.isWorldShop(ev.getClickedBlock()),0);
	    		if (PlayerMode.isStriker(p)) {
	    			//Check for nearby arrows to deflect.
	    			List<Entity> nearby = p.getNearbyEntities(3.5, 3.5, 3.5);
	    			for (int i=0;i<nearby.size();i++) {
	    				if (nearby.get(i) instanceof Arrow &&
	    						((Arrow)nearby.get(i)).getCustomName()==null) {
							/*int currentStrengthLevel = -1;
							for (int j=0;j<p.getActivePotionEffects().size();j++) {
								if (Iterables.get(p.getActivePotionEffects(), j).getType().equals(PotionEffectType.INCREASE_DAMAGE)) {
									//Get the level.
									currentStrengthLevel = Iterables.get(p.getActivePotionEffects(), j).getAmplifier();
									p.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
									log("Resistance level is "+currentStrengthLevel,5);
									break;
								}
							}
							p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,100,(currentStrengthLevel+1<5)?currentStrengthLevel+1:4));*/
	    					GenericFunctions.addStackingPotionEffect(p, PotionEffectType.INCREASE_DAMAGE, 20*5, 4);
							SoundUtils.playLocalSound(p, Sound.ITEM_SHIELD_BLOCK, 1.0f, 3.0f);
							Arrow a = (Arrow)nearby.get(i);
							a.setCustomName("HIT");
							a.setVelocity(new Vector(0,0,0));
	    				} 
	    			}
	    		}
			}
			
			//Check for a Scythe right click here.
			if ((ev.getAction()==Action.RIGHT_CLICK_AIR || ev.getAction()==Action.RIGHT_CLICK_BLOCK) &&
					GenericFunctions.isArtifactEquip(p.getEquipment().getItemInMainHand())) {
				boolean bursted=false;
				bursted = performDeathMark(p, bursted);
				if (bursted) {
					//Cancel this then, because we decided to burst our stacks instead.
					ev.setCancelled(true);
					return;
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
					ev.setCancelled(true);
					return;
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
						if (p.isBlocking()) {
							//Give absorption hearts.
							if (PlayerMode.isDefender(p)) {
								GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.ABSORPTION,200,1,p);
								List<Entity> entities = p.getNearbyEntities(16, 16, 16);
								for (int i=0;i<entities.size();i++) { 
									if (entities.get(i) instanceof Monster) {
										Monster m = (Monster)(entities.get(i));
										m.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING,100,5,true,true,Color.NAVY));
										m.setTarget(p);
									}
								}
							} else {
								GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.ABSORPTION,200,0,p);
							}
							DecimalFormat df = new DecimalFormat("0.0");
							PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
							if (pd.lastblock+20*5<=getServerTickTime()) {
								p.sendMessage(ChatColor.GRAY+"Damage Reduction: "+ChatColor.DARK_AQUA+df.format(((1-CustomDamage.CalculateDamageReduction(1,p,null))*100))+"%  "+ChatColor.GRAY+"Block Chance: "+ChatColor.DARK_AQUA+df.format(((CustomDamage.CalculateDodgeChance(p))*100))+"%  ");
								pd.lastblock=getServerTickTime();
							}
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
						return;
					}
				}
			}
			
			//Check for a Malleable Base right-click.
			if ((ev.getAction()==Action.RIGHT_CLICK_AIR ||
					ev.getAction()==Action.RIGHT_CLICK_BLOCK) &&
					Artifact.isMalleableBase(ev.getPlayer().getEquipment().getItemInMainHand())) {
				//Start a Malleable Base quest.
				if (MalleableBaseQuest.getStatus(ev.getPlayer().getEquipment().getItemInMainHand())==QuestStatus.UNFORMED) {
					if (ev.getAction()==Action.RIGHT_CLICK_AIR) {
						StartMalleableBaseQuest(p);
					}
				} else {
					//If quest is in progress, we will check if the item we need is in our inventory.
					//0-8 are the hotbar slots.
					p.getInventory().setItemInMainHand(ProceedWithMalleableBaseQuest(p,p.getEquipment().getItemInMainHand()));
				}
				ev.setCancelled(true);
				return;
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
					ev.getPlayer().getInventory().getItemInMainHand().getItemMeta().getLore().size()>=4 &&
					ev.getPlayer().getInventory().getItemInMainHand().getItemMeta().getLore().get(3).contains(ChatColor.DARK_PURPLE+"ID#")) {
				ev.setCancelled(true); //Do not place minecarts on rails -.-
				ev.getPlayer().updateInventory();
				return;
			}
	    	if (ev.getAction()==Action.RIGHT_CLICK_AIR || (ev.getPlayer().isSneaking() && ev.getAction()==Action.RIGHT_CLICK_AIR) || (ev.getPlayer().isSneaking() && ev.getAction()==Action.RIGHT_CLICK_BLOCK && !GenericFunctions.isDumpableContainer(ev.getClickedBlock().getType()))) {
	    		if (ev.getPlayer().getInventory().getItemInMainHand().hasItemMeta() &&
	    				ev.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasLore() &&
	    				ev.getPlayer().getInventory().getItemInMainHand().getItemMeta().getLore().size()>=4 &&
	    				ev.getPlayer().getInventory().getItemInMainHand().getItemMeta().getLore().get(3).contains(ChatColor.DARK_PURPLE+"ID#")) {
	    			//This is an item cube.
	    			log("In we are",5);
	    			ev.setCancelled(true);
	    			int itemcube_id=Integer.parseInt(ev.getPlayer().getInventory().getItemInMainHand().getItemMeta().getLore().get(3).split("#")[1]);
	    			int size=0;
	    			if (ev.getPlayer().getInventory().getItemInMainHand().getType()==Material.CHEST) {
	    				size=9;
	    			} else
	    			if (CustomItem.isVacuumCube(ev.getPlayer().getInventory().getItemInMainHand())) {
	    				size=54;
	    			} else {
	    				size=27;
	    			}
	    			if (!ItemCube.isSomeoneViewingItemCube(itemcube_id,ev.getPlayer())) {
	    				Inventory temp = Bukkit.getServer().createInventory(ev.getPlayer(), size, "Item Cube #"+itemcube_id);
	    				openItemCubeInventory(temp);
		    			ev.getPlayer().openInventory(temp);
		    			PlayerStructure pd = (PlayerStructure) playerdata.get(ev.getPlayer().getUniqueId());
		    			pd.isViewingItemCube=true;
		    			SoundUtils.playLocalSound(ev.getPlayer(), Sound.BLOCK_CHEST_OPEN, 1.0f, 1.0f);
	    			} else {
	    				//ItemCube.displayErrorMessage(ev.getPlayer());
	    				ev.getPlayer().openInventory(ItemCube.getViewingItemCubeInventory(itemcube_id, ev.getPlayer()));
	    				PlayerStructure pd = (PlayerStructure) playerdata.get(ev.getPlayer().getUniqueId());
	    				pd.isViewingItemCube=true;
		    			SoundUtils.playLocalSound(ev.getPlayer(), Sound.BLOCK_CHEST_OPEN, 1.0f, 1.0f);
	    			}
	    			ev.setCancelled(true);
	    			return;
	    		}
	    	} else
	    	if (ev.getPlayer().isSneaking() && ev.getAction()==Action.RIGHT_CLICK_BLOCK && GenericFunctions.isDumpableContainer(ev.getClickedBlock().getType())) {
	    		//This is an attempt to insert an item cube into a container. See what item cube we're holding.
	    		if (ev.getPlayer().getInventory().getItemInMainHand().hasItemMeta() &&
	    				ev.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasLore() &&
	    				ev.getPlayer().getInventory().getItemInMainHand().getItemMeta().getLore().size()>=4 &&
	    				ev.getPlayer().getInventory().getItemInMainHand().getItemMeta().getLore().get(3).contains(ChatColor.DARK_PURPLE+"ID#")) {
	
	        		ev.setCancelled(true);
	        		ev.getPlayer().updateInventory();
					int itemcube_id=Integer.parseInt(ev.getPlayer().getInventory().getItemInMainHand().getItemMeta().getLore().get(3).split("#")[1]);
					int size=0;
					if (ev.getPlayer().getInventory().getItemInMainHand().getType()==Material.CHEST) {
						size=9;
					} else
	    			if (CustomItem.isVacuumCube(ev.getPlayer().getInventory().getItemInMainHand())) {
	    				size=54;
	    			} else {
						size=27;
					}
					CubeType cub = (size==9)?CubeType.NORMAL:(size==54)?CubeType.VACUUM:CubeType.LARGE;
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
								HashMap<Integer,ItemStack> result = chest_inventory.addItem(virtualinventory.getItem(i));
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
							PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
							if (pd.usetimer+5<TwosideKeeper.getServerTickTime()) {
								ev.getPlayer().sendMessage(ChatColor.RED+"Attempted to store your items, not all of them could fit!"+ChatColor.WHITE+" Stored "+ChatColor.AQUA+count+ChatColor.WHITE+" items.");
								pd.usetimer=TwosideKeeper.getServerTickTime();
							}
						} else {
							if (count>0) {
								ev.getPlayer().sendMessage("Stored "+ChatColor.AQUA+count+ChatColor.WHITE+" item"+(count==1?"":"s")+" inside the chest.");
							}
						}
						virtualinventory.clear();
						
						
						//Save the Item Cube.
						itemCube_saveConfig(itemcube_id,save_items,cub);
						//This may have been a shop. Update the shop too.
						WorldShop.updateShopSign(ev.getClickedBlock());
						ev.setCancelled(true);
						return;
					} else {
						ev.getPlayer().sendMessage("This shop is owned by "+ChatColor.LIGHT_PURPLE+owner+ChatColor.WHITE+". You cannot dump item cubes into others' shops!");
						ev.setCancelled(true);
						return;
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
			    				ItemStack item = p.getEquipment().getItemInMainHand();
			    				if (item.getType()!=Material.AIR) {
			        				WorldShopSession ss = TwosideShops.AddSession(SessionState.PRICE, p, s);
			        				TextComponent message1 = new TextComponent("Creating a shop to sell ");
									TextComponent message2 = new TextComponent(ChatColor.GREEN+"["+GenericFunctions.GetItemName(item)+ChatColor.RESET+""+ChatColor.GREEN+"]");
									message2.setHoverEvent(new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(GenericFunctions.GetItemName(item)+WorldShop.GetItemInfo(item)).create()));
									TextComponent message3 = new TextComponent(".");
									TextComponent finalmsg = message1;
									finalmsg.addExtra(message2);
									finalmsg.addExtra(message3);
									ev.getPlayer().spigot().sendMessage(finalmsg);
			    					int totalcount = 0;
			    					totalcount = GenericFunctions.CountItems(p, item);
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
									ev.setCancelled(true);
									return;
			    				} else {
			    					p.sendMessage(ChatColor.RED+"Cannot create a shop with nothing! "+ChatColor.WHITE+"Right-click the sign"
			    							+ " with the item you want to sell in your hand.");
									ev.setCancelled(true);
									return;
			    				}
		    				} else {
		    					p.sendMessage(ChatColor.RED+"Sorry! "+ChatColor.WHITE+" A shop has already been setup here!");
								ev.setCancelled(true);
								return;
		    				}
		    			} else 
		    			if (s.getLine(0).equalsIgnoreCase(ChatColor.BLUE+"-- SHOP --")) {
		        			log("This is a buy shop sign.",5);
		    				int shopID = TwosideShops.GetShopID(s);
		        			WorldShop shop = TwosideShops.LoadWorldShopData(shopID);
	    					Chest c = (Chest)chest.getState();
		        			shop.UpdateAmount(GenericFunctions.CountItems(c.getInventory(), shop.GetItem()));
		        			WorldShopManager.UpdateSign(shop, shop.getID(),s,WorldShop.isPurchaseShopSign(s));
							TwosideShops.SaveWorldShopData(shop);
		        			Location newloc = ev.getClickedBlock().getLocation().add(-ev.getBlockFace().getModX()+0.5, -ev.getBlockFace().getModY()+1.5, -ev.getBlockFace().getModZ()+0.5);
		        			
		        			WorldShop.spawnShopItem(ev,newloc,shop);
		        			
		        			if (shop.GetOwner().equalsIgnoreCase(ev.getPlayer().getName())) {
		        				p.sendMessage(ChatColor.DARK_PURPLE+"Editing shop...");
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
			    				TwosideShops.AddSession(SessionState.UPDATE, p, s);
								ev.setCancelled(true);
								return;
		        			} else {
			        			if (shop.GetAmount()>0) {
				        			//player.sendMessage("How many "+Cha tColor.GREEN+shop.GetItemName()+ChatColor.WHITE+" would you like to buy? "+ChatColor.GREEN+"(MAX: "+((getPlayerMoney(player)<(shop.GetAmount()*shop.GetUnitPrice()))?(int)(getPlayerMoney(player)/shop.GetUnitPrice()):shop.GetAmount())+")");
				        			TextComponent message1 = new TextComponent("How many ");
									TextComponent message2 = new TextComponent(ChatColor.GREEN+"["+shop.GetItemName()+ChatColor.RESET+""+ChatColor.GREEN+"]");
									message2.setHoverEvent(new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(shop.GetItemName()+WorldShop.GetItemInfo(shop.GetItem())).create()));
									TextComponent message3 = new TextComponent(ChatColor.WHITE+" would you like to buy? "+ChatColor.GREEN+"(MAX: "+((getPlayerMoney(p)<(shop.GetAmount()*shop.GetUnitPrice()))?(int)(getPlayerMoney(p)/shop.GetUnitPrice()):shop.GetAmount())+")");
									TextComponent finalmsg = message1;
									finalmsg.addExtra(message2);
									finalmsg.addExtra(message3);
									ev.getPlayer().spigot().sendMessage(finalmsg);
				    				
				    				//Initiate buying session.
				    				TwosideShops.AddSession(SessionState.PURCHASE, p, s);
				        			log("Added a shop session for "+p.getName()+".",4);
				        			//shop.sendItemInfo(player);
									ev.setCancelled(true);
									return;
			        			} else {
			        				p.sendMessage(ChatColor.GOLD+"Sorry! "+ChatColor.WHITE+"This shop is sold out! Let "+ChatColor.LIGHT_PURPLE+shop.GetOwner()+ChatColor.WHITE+" know to restock the shop!");
									ev.setCancelled(true);
									return;
			        			}
		        			}
		    			} else
		    			if (s.getLine(0).equalsIgnoreCase("buyshop")) {
		    				if (!WorldShop.shopSignExists(chest)) {
			    				//Create a new buy shop.
			    				ItemStack item = p.getEquipment().getItemInMainHand();
			    				if (item.getType()!=Material.AIR) {
			        				WorldShopSession ss = TwosideShops.AddSession(SessionState.BUY_PRICE, p, s);
			        				TextComponent message1 = new TextComponent("Creating a shop to buy ");
									TextComponent message2 = new TextComponent(ChatColor.GREEN+"["+GenericFunctions.GetItemName(item)+ChatColor.RESET+""+ChatColor.GREEN+"]");
									message2.setHoverEvent(new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(GenericFunctions.GetItemName(item)+WorldShop.GetItemInfo(item)).create()));
									TextComponent message3 = new TextComponent(".");
									TextComponent finalmsg = message1;
									finalmsg.addExtra(message2);
									finalmsg.addExtra(message3);
									ev.getPlayer().spigot().sendMessage(finalmsg);
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

									ev.setCancelled(true);
									return;
			    				} else {
			    					p.sendMessage(ChatColor.RED+"Cannot create a shop with nothing! "+ChatColor.WHITE+"Right-click the sign"
			    							+ " with the item you want to buy in your hand.");
									ev.setCancelled(true);
									return;
			    				}
		    				} else {
		    					p.sendMessage(ChatColor.RED+"Sorry! "+ChatColor.WHITE+" A shop has already been setup here!");
								ev.setCancelled(true);
								return;
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
		        			WorldShopManager.UpdateSign(shop, shop.getID(),s,WorldShop.isPurchaseShopSign(s));
							TwosideShops.SaveWorldShopData(shop);
		        			Location newloc = ev.getClickedBlock().getLocation().add(-ev.getBlockFace().getModX()+0.5, -ev.getBlockFace().getModY()+1.5, -ev.getBlockFace().getModZ()+0.5);
		    				WorldShop.spawnShopItem(ev,newloc,shop);
		    				
		
		        			if (shop.GetOwner().equalsIgnoreCase(ev.getPlayer().getName())) {
		        				p.sendMessage(ChatColor.DARK_PURPLE+"Editing shop...");
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
			    				TwosideShops.AddSession(SessionState.BUY_UPDATE, p, s);
								ev.setCancelled(true);
								return;
		        			} else {
			        			if (shop.GetAmount()>0) {
				        			//player.sendMessage(+ChatColor.GREEN+shop.GetItemName()+ChatColor.WHITE+);
	
			        				TextComponent message1 = new TextComponent("How many ");
									TextComponent message2 = new TextComponent(ChatColor.GREEN+"["+shop.GetItemName()+ChatColor.RESET+""+ChatColor.GREEN+"]");
									message2.setHoverEvent(new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(shop.GetItemName()+WorldShop.GetItemInfo(shop.GetItem())).create()));
									TextComponent message3 = new TextComponent(" would you like to sell? "+ChatColor.GREEN+"(MAX: "+(shop.GetUnitPrice()*GenericFunctions.CountItems(p, shop.GetItem())<=getPlayerBankMoney(shop.GetOwner())?((GenericFunctions.CountItems(p, shop.GetItem())<=shop.GetAmount())?(GenericFunctions.CountItems(p, shop.GetItem())):shop.GetAmount()):(int)(getPlayerBankMoney(shop.GetOwner())/shop.GetUnitPrice()))+")");
									TextComponent finalmsg = message1;
									finalmsg.addExtra(message2);
									finalmsg.addExtra(message3);
									ev.getPlayer().spigot().sendMessage(finalmsg);
				    				//Initiate buying session.
				    				TwosideShops.AddSession(SessionState.SELL, p, s);
				        			log("Added a shop session for "+p.getName()+".",4);
				        			//shop.sendItemInfo(player);
									ev.setCancelled(true);
									return;
			        			} else {
			        				p.sendMessage(ChatColor.GOLD+"Sorry! "+ChatColor.WHITE+"This shop is not buying anymore items! "+ChatColor.LIGHT_PURPLE+shop.GetOwner()+ChatColor.WHITE+" needs to edit the shop!");
									ev.setCancelled(true);
									return;
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
						ev.setCancelled(true);
						return;
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
						ev.setCancelled(true);
						return;
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
						ev.setCancelled(true);
						return;
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
						ev.setCancelled(true);
						return;
	    			} else
					if (s.getLine(1).equalsIgnoreCase(ChatColor.DARK_GREEN+"CASH CHECK")) {
						if (Check.isSignedBankCheck(ev.getPlayer().getEquipment().getItemInMainHand())) {
							//Make sure the player that signed has enough money for this check. Otherwise don't allow it!
							Check c = new Check(ev.getPlayer().getEquipment().getItemInMainHand());
							if (c.player!=null) {
								//We found a player for this check. See if they have enough money.
								if (!Check.isVerifiedBankCheck(ev.getPlayer().getEquipment().getItemInMainHand())) {
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
										ev.setCancelled(true);
										return;
									} else {
										DecimalFormat df = new DecimalFormat("0.00");
										ev.getPlayer().sendMessage(ChatColor.RED+"We're sorry! "+ChatColor.WHITE+"But the check cannot be processed since the check signer, "+ChatColor.LIGHT_PURPLE+c.player+ChatColor.WHITE+" has poor money management skills and does not have "+ChatColor.YELLOW+"$"+df.format(c.amt)+ChatColor.WHITE+" available in their account!");
										ev.getPlayer().sendMessage(ChatColor.AQUA+"We are sorry about this inconvenience. "+ChatColor.WHITE+"Have a nice day!");
										ev.setCancelled(true);
										return;
									}
								}
								else {
									//This is verified. Just give the player their money.
									givePlayerBankMoney(ev.getPlayer(),c.amt);
									DecimalFormat df = new DecimalFormat("0.00");
									ev.getPlayer().sendMessage(ChatColor.AQUA+"Cashed in the check for "+ChatColor.YELLOW+"$"+df.format(c.amt)+ChatColor.WHITE+".");
			    					ev.getPlayer().sendMessage("  Now In Bank: "+ChatColor.BLUE+"$"+df.format(getPlayerBankMoney(ev.getPlayer())));
									if (ev.getPlayer().getEquipment().getItemInMainHand().getAmount()>1) {
										ev.getPlayer().getEquipment().getItemInMainHand().setAmount(ev.getPlayer().getEquipment().getItemInMainHand().getAmount()-1);
									} else {
										ev.getPlayer().getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
									}
									ev.setCancelled(true);
									return;
								}
							} else {
								GenericFunctions.produceError(1,ev.getPlayer());
								ev.setCancelled(true);
								return;
							}
						} else {
							ev.getPlayer().sendMessage(ChatColor.YELLOW+"You are not holding a properly signed check!");
							ev.setCancelled(true);
							return;
						}
	    			} else
					if (s.getLine(1).equalsIgnoreCase(ChatColor.YELLOW+"TOKEN EXCHANGE")) {
						if (Christmas.isChristmasEventToken(ev.getItem())) {
							ItemStack item = ev.getItem();
							item.setAmount(item.getAmount()-1);
							if (item.getAmount()>0) {
								p.getEquipment().setItemInMainHand(item);
							} else {
								p.getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
							}
							SoundUtils.playGlobalSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 0.7f);
							ItemStack prize = Christmas.GeneratePrize();
							if (prize.getType()==Material.PAPER) {
								int moneyamt = ((int)(Math.random()*10)*50)+50;
								TwosideKeeperAPI.givePlayerMoney(p, moneyamt);
								p.sendMessage("You exchange a Christmas Token for "+ChatColor.GREEN+"$"+moneyamt+ChatColor.RESET+"!");
							} else {
								GenericFunctions.giveItem(p, prize);
								p.sendMessage("You exchange a Christmas Token for "+ChatColor.GREEN+GenericFunctions.UserFriendlyMaterialName(prize)+((prize.getAmount()>1)?" x"+prize.getAmount():"")+ChatColor.RESET+"!");
							}
						} else {
							p.sendMessage("You must collect Christmas Tokens. Then return here to exchange them!");
						}
					}
	    		}
	    	}
        }
    }
	private String DisplayTimeDifference(long tickdiff) {
		int seconds = (int)(tickdiff/20);
		int minutes = (int)(seconds/60);
		int hours = (int)(minutes/60);
		int days = (int)(hours/24);
		StringBuilder string = new StringBuilder();
		TwosideKeeper.log(seconds+","+minutes+","+hours+","+days, 5);
		if (days>0) {
			string.append(days+" Days");
		}
		if (hours>0) {
			string.append(((string.length()>0)?", ":" ")+(hours%24)+" Hours");
		}
		if (minutes>0) {
			string.append(((string.length()>0)?", ":" ")+(minutes%60)+" Minutes");
		}
		if (seconds>0) {
			string.append(((string.length()>0)?", ":" ")+(seconds%60)+" Seconds");
		}
		return string.toString();
	}
	public ItemStack ProceedWithMalleableBaseQuest(final Player p, ItemStack base) {
		for (int i=0;i<=8;i++) {
			if (p.getInventory().getItem(i)!=null) {
				log("Malleable Base Quest: Comparing "+GenericFunctions.UserFriendlyMaterialName(p.getInventory().getItem(i).getType())+" to "+MalleableBaseQuest.getItem(base),2);
			}
			if (p.getInventory().getItem(i)!=null && GenericFunctions.hasNoLore(p.getInventory().getItem(i)) && !Artifact.isArtifact(p.getInventory().getItem(i)) && GenericFunctions.UserFriendlyMaterialName(p.getInventory().getItem(i).getType()).equalsIgnoreCase(MalleableBaseQuest.getItem(base))) {
				//This is good. Take one away from the player to continue the quest.
				log(ChatColor.YELLOW+"Success! Next Item...",5);
				ItemStack newitem = p.getInventory().getItem(i);
				newitem.setAmount(p.getInventory().getItem(i).getAmount()-1);
				p.getInventory().setItem(i, newitem);
				//Check if we have completed all the quests. Otherwise, generate the next quest.
				ItemStack newbase = MalleableBaseQuest.advanceQuestProgress(base);
				if (MalleableBaseQuest.getCurrentProgress(newbase)==30) {
					//The quest is completed. Proceed to turn it into a Base.
					newbase = MalleableBaseQuest.completeQuest(newbase);
					if (!Artifact.isMalleableBase(newbase)) {
						p.sendMessage(ChatColor.DARK_BLUE+"Quest Complete! "+ChatColor.GREEN+"You obtained "+newbase.getItemMeta().getDisplayName()+ChatColor.GREEN+"!");
					} else {
						p.sendMessage(ChatColor.DARK_RED+"Quest Failed! "+ChatColor.RED+"You did not successfully mold the Malleable Base. You will have to re-activate it by right-clicking it again.");
					}
					return newbase;
				} else {
					//The quest is in progress. Announce the next item to the player.
					MalleableBaseQuest.announceQuestItem(this,p,newbase);
				}
				return newbase;
			}
		}
		return base;
	}
	public void StartMalleableBaseQuest(final Player p) {
		ItemStack MalleableBase = p.getEquipment().getItemInMainHand();
		p.getEquipment().setItemInMainHand(MalleableBaseQuest.startQuest(MalleableBase));
		//Start the quest.
		p.sendMessage(ChatColor.YELLOW+"Malleable Base Forming Quest has begun!");
		MalleableBaseQuest.announceQuestItem(this,p,MalleableBase);
	}

	public boolean performDeathMark(final Player player, boolean bursted) {
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(player); //Make sure it's off cooldown.
		if (pd.last_deathmark+GenericFunctions.GetModifiedCooldown(TwosideKeeper.DEATHMARK_COOLDOWN,player)<getServerTickTime()) {
			if (ArtifactAbility.getEnchantmentLevel(ArtifactAbility.DEATHMARK, player.getEquipment().getItemInMainHand())>0) {
				double dmg = GenericFunctions.getAbilityValue(ArtifactAbility.DEATHMARK, player.getEquipment().getItemInMainHand());
				//Look for nearby mobs up to 10 blocks away.
				List<Entity> nearby = player.getNearbyEntities(10, 10, 10);
				boolean reset=false;
				for (int i=0;i<nearby.size();i++) {
					if (nearby.get(i) instanceof LivingEntity) {
						LivingEntity m = (LivingEntity)nearby.get(i);
						if (m.hasPotionEffect(PotionEffectType.UNLUCK) && !m.isDead()) {
							//This has stacks, burst!
							bursted=true;
							aPlugin.API.sendCooldownPacket(player, player.getEquipment().getItemInMainHand(), 240);
							pd.last_deathmark = getServerTickTime();
							int stackamt = GenericFunctions.GetDeathMarkAmt(m);
							//GenericFunctions.DealDamageToMob(stackamt*dmg, m, player, null, "Death Mark");
			    			GenericFunctions.removeNoDamageTick(m, player);
			    			CustomDamage.ApplyDamage(stackamt*dmg, player, m, null, "Death Mark", CustomDamage.TRUEDMG);
			    			if (m.getHealth()<=0 || m.isDead()) {
			    				reset=true;
			    			}
			    			if (stackamt<5) {
			    				m.removePotionEffect(PotionEffectType.UNLUCK);
			    			} else {
			    				m.addPotionEffect(new PotionEffect(PotionEffectType.UNLUCK,99,stackamt/2),true);
			    				GenericFunctions.ApplyDeathMark(m);
			    			}
							//player.playSound(m.getLocation(), Sound.ENTITY_ZOMBIE_BREAK_DOOR_WOOD, 1.0f, 1.0f);
			    			SoundUtils.playGlobalSound(m.getLocation(), Sound.ENTITY_ZOMBIE_BREAK_DOOR_WOOD, 1.0f, 1.0f);
						}
					}
				}
				if (reset) {
					pd.last_deathmark = getServerTickTime()-GenericFunctions.GetModifiedCooldown(TwosideKeeper.DEATHMARK_COOLDOWN,player)+20;
					aPlugin.API.sendCooldownPacket(player, player.getEquipment().getItemInMainHand(), 10);
				}
			}
		}
		return bursted;
	}
    
    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent ev) {
    	
    	TwosideSpleefGames.PassEvent(ev);
    	
    	Christmas.FillChristmasBox(ev.getPlayer(), ev.getItemInHand(), ev.getBlockPlaced());
    	if (!Christmas.ChristmasPlaceEvent(ev)) {
    		return;
    	}
    	
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
    		    				return;
    						}
    					}
    				}
    			}
    		}
    	}
    	
    	if (WorldShop.isPlaceableWorldShop(ev.getItemInHand())) {
    		if (BlockUtils.LocationInFrontOfBlockIsFree(ev.getBlockPlaced())) {
    			//ev.getPlayer().sendMessage("ALLOWED!");
    			WorldShop.CreateNewWorldShop(ev.getBlockPlaced(),WorldShop.ExtractPlaceableShopMaterial(ev.getItemInHand()));
    		} else {
    			//ev.getPlayer().sendMessage("DENIED!");
        		ev.setCancelled(true);
    		}
    		return;
    	}
    	
    	if (GenericFunctions.isArtifactEquip(ev.getItemInHand()) &&
    			ev.getItemInHand().getType().toString().contains("HOE")) {
			AwakenedArtifact.addPotentialEXP(ev.getItemInHand(), 4, ev.getPlayer());
    	}
    	
    	if (ev.getItemInHand().hasItemMeta() &&
    			ev.getItemInHand().getItemMeta().hasLore() &&
    			ev.getItemInHand().getItemMeta().getLore().size()>=4 &&
    			ev.getItemInHand().getItemMeta().getLore().get(3).contains(ChatColor.DARK_PURPLE+"ID#")) {
    		//This is an item cube.
    		ev.setCancelled(true);
    		return;
    	}
    	
    	if (Artifact.isArtifact(ev.getItemInHand()) && !GenericFunctions.isArtifactEquip(ev.getItemInHand())) {
    		ev.setCancelled(true);
    		return;
    	}
    	
    	if (ItemSet.isSetItem(ev.getItemInHand())) {
    		ev.setCancelled(true);
    		return;
    	}
    }
    
    @EventHandler(priority=EventPriority.LOWEST,ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent ev) {
    	//Modify the death message. This is a fix for getting rid of the healthbar from the player name.
    	final Player p = ev.getEntity();
    	if (!DeathManager.deathStructureExists(p)) {
	    	PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
	    	pd.playermode_on_death=pd.lastmode;
	    	if (pd.target!=null &&
	    			pd.target.getCustomName()!=null) {
	    		ev.setDeathMessage(ev.getDeathMessage().replace(pd.target.getCustomName(), GenericFunctions.getDisplayName(pd.target)));
	    	}
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
	    	
	    	if (pd.lasthitdesc!=null) {
		    	log("Death Description: "+pd.lasthitdesc,5);
	    		newDeathMsg = getFancyDeathMessage(p);
	    	}
	    	newDeathMsg=p.getName()+" "+newDeathMsg;
	    	ev.setDeathMessage(newDeathMsg); 
	    	log("Death Message: "+ev.getDeathMessage(),5);
			DecimalFormat df = new DecimalFormat("0.00");
	    	if (p!=null) {
	    		p.sendMessage(ChatColor.GRAY+"Due to death, you lost "+DEATHPENALTY+"% of your holding money. ");
	    		givePlayerMoney(p,-(getPlayerMoney(p)/2));
	    		p.sendMessage("  Now Holding: "+ChatColor.GREEN+"$"+df.format(getPlayerMoney(p)));
	    	}
	    	
	    	p.sendMessage("You took "+ChatColor.RED+df.format(pd.lastdamagetaken)+" damage"+ChatColor.WHITE+" from the last attack "+((pd.lasthitdesc!=null)?"("+pd.lasthitdesc+")":""+"!"));
	    	
	    	log("Y position is "+p.getLocation().getY(), 4);
	    	DeathManager.addNewDeathStructure(ev.getDrops(), (p.getLocation().getY()<0)?p.getLocation().add(0,-p.getLocation().getY()+256,0) //This means they fell into the void. Might as well put it way higher.
	    			:p.getLocation(), p);
	    	pd = PlayerStructure.GetPlayerStructure(p);
	    	pd.hasDied=true;
	    	pd.vendetta_amt=0.0;
	    	//p.getInventory().clear();
    	}
    	for (int i=0;i<elitemonsters.size();i++) {
    		EliteMonster em = elitemonsters.get(i);
    		em.targetlist.remove(p);
    	}
    	ev.setKeepInventory(true);
    }
    
    private String getFancyDeathMessage(Player p) {
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		switch (pd.lasthitdesc) {
			case "LAVA": {
				if (Math.random()<=0.2) {
					return "tried to swim in... Oh who am I kidding, you were just being dumb.";
				} else {
					return Pronouns.ChoosePronoun(6);
				}
			}
			case "FALL": {
				return Pronouns.ChoosePronoun(2)+" and died.";
			}
			case "FIRE_TICK":
			case "FIRE": {
				if (Math.random()<0.5) {
					return "could not handle the "+Pronouns.ChoosePronoun(3)+" flames.";
				} else {
					return "was "+Pronouns.ChoosePronoun(4)+" by the flames.";
				}
			}
			case "DROWNING": {
				return Pronouns.ChoosePronoun(9);
			}
			case "CONTACT": {
				return "could not resist "+Pronouns.ChoosePronoun(1)+" cacti.";
			}
			case "FALLING_BLOCK": {
				return "got "+Pronouns.ChoosePronoun(10)+" by a falling block.";
			}
			case "LIGHTNING": {
				return Pronouns.ChoosePronoun(11);
			}
			case "FLY_INTO_WALL": {
				return "was flying too fast. SPLAT!";
			}
			case "Explosion":
			case "BLOCK_EXPLOSION":
			case "MELTING":{
				return Pronouns.ChoosePronoun(5);
			}
			case "Leap": {
				return Pronouns.ChoosePronoun(7);
			}
			case "Stored Energy": {
				return Pronouns.ChoosePronoun(8);
			}
			case "POISON": {
				return Pronouns.ChoosePronoun(12)+" Poison.";
			}
			case "WITHER": {
				return Pronouns.ChoosePronoun(13);
			}
			case "STARVATION":{
				return Pronouns.ChoosePronoun(14);
			}
			case "SUFFOCATION":{
				return Pronouns.ChoosePronoun(16);
			}
			case "VOID": {
				return Pronouns.ChoosePronoun(0)+" into the void.";
			}
			case "DRAGON_BREATH": {
				return Pronouns.ChoosePronoun(0)+" to the breath of the Ender Dragon.";
			}
			case "Spider Ball": {
				return Pronouns.ChoosePronoun(17);
			}
			case "Defender Tank": 
			case "Cupid Set Tank": {
				return "died trying to save teammates from imminent death...";
			}
			case "Damage Pool": {
				return Pronouns.ChoosePronoun(18);
			}
			case "Orni": {
				return "was killed by merely existing.";
			}
			default:{
				return "has died by "+pd.lasthitdesc;
			}
		}
	}

	@EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void onSignChange(SignChangeEvent ev) {
    	Player p = ev.getPlayer(); 
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
    		} else
			if (line1.equalsIgnoreCase("-- bank --") &&
    				line2.equalsIgnoreCase("exchange")) {
    			//Turn it into a bank sign.
    			ev.setLine(0, ChatColor.AQUA+"-- BANK --");
    			ev.setLine(1, ChatColor.YELLOW+"TOKEN EXCHANGE");
    			ev.setLine(2, "Right-Click");
    			ev.setLine(3, "to use");
    			p.sendMessage("Successfully created a Christmas Token Exchange Sign.");
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
    		SoundUtils.playLocalSound(p, Sound.BLOCK_ANVIL_USE, 1.0f, 1.0f);
    		return;
    	}
    	if (ArrowQuiver.isValidQuiver(ev.getCurrentItem())) {
    		if (ev.isShiftClick()) {
    			ev.setCancelled(true);
    		} else {
        		//Set the ID to the correct value.
        		ArrowQuiver.setID(ev.getCurrentItem());
        		ArrowQuiver.updateQuiverLore(ev.getCurrentItem());
    		}
    	}
    	if (ev.getCurrentItem().hasItemMeta()) {
	    	ItemMeta item_meta = ev.getCurrentItem().getItemMeta();
	    	if (item_meta.getDisplayName()!=null && 
	    			(item_meta.getDisplayName().contains("Item Cube") || item_meta.getDisplayName().contains("Vacuum Cube")
	    					 || item_meta.getDisplayName().contains("Filter Cube"))) {
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
			    		CubeType cubetype;
			    		if (ev.getCurrentItem().getItemMeta().getDisplayName().contains("Filter Cube")) {
			    			cubetype=CubeType.FILTER;
			    			ItemCubeUtils.createNewFilterCube(ITEMCUBEID);
			    		} else 
			    		if (ev.getCurrentItem().getItemMeta().getDisplayName().contains("Vacuum Cube")) {
			    			cubetype=CubeType.VACUUM;
			    		} else 
			    		if (ev.getCurrentItem().getItemMeta().getDisplayName().contains("Ender Item Cube")) {
			    			ev.getCurrentItem().setAmount(2);
			    			cubetype=CubeType.ENDER;
			    		} else if (ev.getCurrentItem().getItemMeta().getDisplayName().contains("Large Item Cube")) {
			    			cubetype=CubeType.LARGE;
			    		} else {
			    			cubetype=CubeType.NORMAL;
			    		}
			    		itemCube_saveConfig(ITEMCUBEID, new ArrayList<ItemStack>(), cubetype);
			    		ITEMCUBEID++;
		    		}
		    		return;
	    		} 
	    	}
    	}
    }
    
    
    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void onFallingBlock(EntityChangeBlockEvent ev) {
    	if (ev.getEntity() instanceof FallingBlock) {
    		FallingBlock fb = (FallingBlock)ev.getEntity();
			//TwosideKeeper.log("In here.", 0);
    		if (fb.hasMetadata("BREAKDOWN")) {
    			ev.setCancelled(true);
    			FallingBlock fb2 = fb.getWorld().spawnFallingBlock(fb.getLocation().getBlock().getLocation(), fb.getBlockId(), fb.getBlockData());
				GlowAPI.setGlowing(fb2, GlowAPI.Color.BLUE, Bukkit.getOnlinePlayers());
				fb2.setMetadata("BREAKDOWN", new FixedMetadataValue(TwosideKeeper.plugin,true));
				fb2.setDropItem(false);
				//TwosideKeeper.log("Spawn new block", 0);
    		}
    		if (fb.hasMetadata("FAKE")) {
    			final Block b = ev.getBlock();
    			Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
					@Override
					public void run() {
						SoundUtils.playGlobalSound(b.getLocation(), Sound.BLOCK_CHORUS_FLOWER_DEATH, 1.0f, 1.0f);
						b.breakNaturally();
					}
				},1);
    		} else
			if (fb.hasMetadata("DESTROY")) {
				ev.setCancelled(true);
			}
    	}
    }

    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void togglesprint(PlayerToggleSprintEvent ev) {
    	//log("Toggled: "+ev.isSprinting(),2);
    	if (ev.isSprinting()) {
    		PlayerStructure pd = PlayerStructure.GetPlayerStructure(ev.getPlayer());
    		pd.lastsprintcheck=getServerTickTime();
    	}
    }

    @SuppressWarnings("deprecation")
	@EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void onPlayerSwapItem(PlayerSwapHandItemsEvent ev) {
		Player p = ev.getPlayer();
    	if (ev.getOffHandItem()!=null &&
    			ev.getOffHandItem().getType()==Material.BOW && ArrowQuiver.isValidQuiver(ev.getMainHandItem())) {
    		ev.setCancelled(true);
    		//Swap forward or backward modes, depending on whether we are sneaking of not.
    		ItemStack quiver = p.getEquipment().getItemInOffHand();
			int mode = ArrowQuiver.getArrowQuiverMode(quiver);
    		if (p.isSneaking()) {
    			if (mode==0) {
    				mode=ArrowQuiver.getContents(ArrowQuiver.getID(quiver)).size()-1;
    			} else {
    				mode-=1;
    			}
    		} else {
    			if(ArrowQuiver.getContents(ArrowQuiver.getID(quiver)).size()>mode+1) {
    				mode++;
    			} else {
    				mode=0;
    			}
    		}
			ArrowQuiver.setArrowQuiverMode(quiver, mode);
			ArrowQuiver.updateQuiverLore(quiver);
			List<ItemStack> contents = ArrowQuiver.getContents(ArrowQuiver.getID(quiver));
			if (contents.size()>0) {
				String message = ChatColor.GOLD+"Now Firing "+ChatColor.YELLOW+GenericFunctions.UserFriendlyMaterialName(contents.get(mode))+ChatColor.GOLD+" ["+contents.get(mode).getAmount()+"]";
				GenericFunctions.sendActionBarMessage(p, message, true);
			} else {
				String message = ChatColor.RED+"Quiver is empty!";
				GenericFunctions.sendActionBarMessage(p, message, true);
			}
			return;
    	}
    	if (PlayerMode.getPlayerMode(p)==PlayerMode.BARBARIAN) {
    		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
    		if (pd.weaponcharges>=100) {
    			//Perform Barbarian's Rage!
    			SoundUtils.playLocalSound(p, Sound.ENTITY_GHAST_DEATH, 1.0f, 0.3f);
    			int rage_duration = (20*(pd.weaponcharges/10));
    			pd.rage_time=getServerTickTime() + rage_duration;
    			pd.rage_amt=pd.weaponcharges;
    			int str_lv = (pd.weaponcharges/10)-1;
    			if (p.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
    				if (GenericFunctions.getPotionEffectLevel(PotionEffectType.INCREASE_DAMAGE, p)<str_lv) {
    					GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.INCREASE_DAMAGE, rage_duration, str_lv, p, true);
    				}
    			} else {
    				GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.INCREASE_DAMAGE, rage_duration, str_lv, p, true);
    			}
    			if (p.hasPotionEffect(PotionEffectType.SPEED)) {
    				if (GenericFunctions.getPotionEffectLevel(PotionEffectType.SPEED, p)<4) {
    					GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.SPEED, rage_duration, 4, p, true);
    				}
    			} else {
    				GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.SPEED, rage_duration, 4, p, true);
    			}
    			GenericFunctions.addIFrame(p, 40*(pd.weaponcharges/100));
    			pd.weaponcharges=0;
    			double missinghealth = p.getMaxHealth()-p.getHealth();
    			CustomDamage.setAbsorptionHearts(p, (float)(CustomDamage.getAbsorptionHearts(p)+missinghealth));
    		}
    		ev.setCancelled(true);
    		return;
    	}
    }
    
    @SuppressWarnings("deprecation")
	@EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void onPlayerDropItem(PlayerDropItemEvent ev) {
    	
    	if (GenericFunctions.isArtifactEquip(ev.getItemDrop().getItemStack())) {
    		ev.getItemDrop().setInvulnerable(true);
    	}
    	
    	if (ev.getItemDrop().getItemStack().hasItemMeta()) {
    		if (ev.getItemDrop().getItemStack().getItemMeta().hasLore()) {
    			if (ev.getItemDrop().getItemStack().getItemMeta().getLore().size()>=4) {
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
    						itemCube_saveConfig(itemcube_id,itemlist,((ev.getItemDrop().getItemStack().getType()==Material.CHEST)?CubeType.NORMAL:(ev.getItemDrop().getItemStack().getType()==Material.STORAGE_MINECART)?CubeType.LARGE:((CustomItem.isVacuumCube(ev.getItemDrop().getItemStack())))?CubeType.VACUUM:CubeType.ENDER));
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
    	
    	if (PlayerMode.isSlayer(ev.getPlayer()) && !GenericFunctions.isViewingInventory(ev.getPlayer())) {
    		ev.setCancelled(true);
    		PlayerStructure pd = PlayerStructure.GetPlayerStructure(ev.getPlayer());
    		if (pd.lastassassinatetime+GenericFunctions.GetModifiedCooldown(TwosideKeeper.ASSASSINATE_COOLDOWN,ev.getPlayer())<=TwosideKeeper.getServerTickTime()) {
    			//ev.getPlayer().getEquipment().setItemInMainHand(ev.getItemDrop().getItemStack());
    			GenericFunctions.PerformAssassinate(ev.getPlayer(),ev.getItemDrop().getItemStack().getType());
	    		//ev.getPlayer().getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
    		}
    		return;
    	}
    	
    	if (ev.getItemDrop().getItemStack().getType()==Material.SHIELD && !GenericFunctions.isViewingInventory(ev.getPlayer())) {
    		ev.setCancelled(true);
    		if (ev.getPlayer().getEquipment().getItemInMainHand()==null || ev.getPlayer().getEquipment().getItemInMainHand().getType()==Material.AIR) {
	    		ev.getPlayer().getEquipment().setItemInMainHand(ev.getItemDrop().getItemStack());
	    		PlayerStructure pd = PlayerStructure.GetPlayerStructure(ev.getPlayer());
	    		if (pd.last_rejuvenate+GenericFunctions.GetModifiedCooldown(TwosideKeeper.REJUVENATE_COOLDOWN,ev.getPlayer())<=TwosideKeeper.getServerTickTime() && PlayerMode.isDefender(ev.getPlayer())) {
	    			GenericFunctions.PerformRejuvenate(ev.getPlayer());
	    			pd.last_rejuvenate = TwosideKeeper.getServerTickTime();
	    			aPlugin.API.damageItem(ev.getPlayer(), ev.getItemDrop().getItemStack(), 400);
	    		}
	    		ev.getPlayer().getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
    		}
    		return;
    	}
    	
    	if (ev.getItemDrop().getItemStack().getType().name().contains("_AXE") && !GenericFunctions.isViewingInventory(ev.getPlayer())) {
    		ev.setCancelled(true);
    		if (ev.getPlayer().getEquipment().getItemInMainHand()==null || ev.getPlayer().getEquipment().getItemInMainHand().getType()==Material.AIR) {
	    		ev.getPlayer().getEquipment().setItemInMainHand(ev.getItemDrop().getItemStack());
	    		PlayerStructure pd = PlayerStructure.GetPlayerStructure(ev.getPlayer());
	    		Player p = ev.getPlayer();
	    		boolean hasFullSet = ItemSet.hasFullSet(GenericFunctions.getEquipment(p,true), p, ItemSet.DAWNTRACKER);
	    		if (PlayerMode.getPlayerMode(p)==PlayerMode.BARBARIAN &&
	    				((hasFullSet && pd.last_mock+GenericFunctions.GetModifiedCooldown(TwosideKeeper.MOCK_COOLDOWN/2,ev.getPlayer())<=TwosideKeeper.getServerTickTime()) || pd.last_mock+GenericFunctions.GetModifiedCooldown(TwosideKeeper.MOCK_COOLDOWN,ev.getPlayer())<=TwosideKeeper.getServerTickTime())) {
	    			pd.last_mock=getServerTickTime();
	    			List<LivingEntity> le = GenericFunctions.getNearbyMobs(p.getLocation(), 12);
	    			for (LivingEntity ent : le) {
	    				if (!(ent instanceof Player)) {
	    					if (ent instanceof Monster) {
	    						CustomDamage.provokeMonster((Monster)ent, p, ev.getItemDrop().getItemStack());
	    						CustomDamage.setAggroGlowTickTime((Monster)ent, 20*15);
	    						GenericFunctions.addStackingPotionEffect(ent, PotionEffectType.WEAKNESS, 20*15, 5, 2);
	    					}
	    				}
	    			}
	    			SoundUtils.playLocalSound(p, Sound.ENTITY_VILLAGER_AMBIENT, 1.0f, 0.3f);
					aPlugin.API.displayEndRodParticle(p.getLocation(), 0, 0f, 0f, 5, 20);
	    			if (hasFullSet) {
	    				aPlugin.API.sendCooldownPacket(p, ev.getItemDrop().getItemStack().getType(), GenericFunctions.GetModifiedCooldown(TwosideKeeper.MOCK_COOLDOWN/2,ev.getPlayer()));
	    			} else {
	    				aPlugin.API.sendCooldownPacket(p, ev.getItemDrop().getItemStack().getType(), GenericFunctions.GetModifiedCooldown(TwosideKeeper.MOCK_COOLDOWN,ev.getPlayer()));
	    			}
	    		}
	    		ev.getPlayer().getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
    		}
    		return;
    	}
    	
    	if (ev.getItemDrop().getItemStack().getType()==Material.BOW && !GenericFunctions.isViewingInventory(ev.getPlayer())) {
    		ev.setCancelled(true);
    		if (ev.getPlayer().getEquipment().getItemInMainHand()==null || ev.getPlayer().getEquipment().getItemInMainHand().getType()==Material.AIR) {
	    		ev.getPlayer().getEquipment().setItemInMainHand(ev.getItemDrop().getItemStack());
	    		GenericFunctions.PerformDodge(ev.getPlayer());
	    		GenericFunctions.PerformArrowBarrage(ev.getPlayer());
	    		GenericFunctions.PerformSiphon(ev.getPlayer());
	    		ev.getPlayer().getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
    		}
    		return;
    	}
    	
    	if (GenericFunctions.holdingNoShield(ev.getPlayer()) &&
    			ev.getItemDrop().getItemStack().getType().toString().contains("SWORD") &&
    			!GenericFunctions.isViewingInventory(ev.getPlayer())) {
    		ev.setCancelled(true);
    		PlayerStructure pd = (PlayerStructure)playerdata.get(ev.getPlayer().getUniqueId());
			boolean second_charge = (CustomDamage.isInIframe(ev.getPlayer()) || (ev.getPlayer().hasPotionEffect(PotionEffectType.SLOW) && GenericFunctions.getPotionEffectLevel(PotionEffectType.SLOW, ev.getPlayer())==20));
			//ev.getPlayer().getEquipment().setItemInMainHand(ev.getItemDrop().getItemStack());
    		if ((ev.getPlayer().isOnGround() || second_charge) &&
    				pd.last_strikerspell+GenericFunctions.GetModifiedCooldown(TwosideKeeper.LINEDRIVE_COOLDOWN,ev.getPlayer())<getServerTickTime()) {
    			if (pd.target!=null &&
    					!pd.target.isDead()) {
    				pd.target.setNoDamageTicks(0);
    			}
    			ev.getPlayer().getEquipment().setItemInMainHand(ev.getItemDrop().getItemStack());
    			
    			if (PlayerMode.isStriker(ev.getPlayer())) {
    				ev.getItemDrop().setPickupDelay(0);
	    			GenericFunctions.PerformLineDrive(ev.getPlayer(), ev.getItemDrop().getItemStack(), second_charge);
    			}

	    		ev.getPlayer().getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
    		}
    	} else {
    		if (ev.getItemDrop().getItemStack().getType().toString().contains("SWORD") &&
        			!GenericFunctions.isViewingInventory(ev.getPlayer())) {
    			ev.setCancelled(true);
    		}
    	}
		return;
    }
    
	@EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void onInventoryOpen(InventoryOpenEvent ev) {
    	PlayerStructure pd = (PlayerStructure)playerdata.get(ev.getPlayer().getUniqueId());
    	pd.isViewingInventory=true;
    	//GenericFunctions.updateSetItemsInInventory(ev.getInventory());
		GenericFunctions.updateSetItemsInInventory(ev.getView().getBottomInventory());
		GenericFunctions.updateSetItemsInInventory(ev.getView().getTopInventory());
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
        		DropDeathInventoryContents(p, deathloc, 0);
        	}
        	
    		PlayerStructure pd = (PlayerStructure) playerdata.get(p.getUniqueId());
        	pd.isViewingInventory=false;
        	log("Closed Inventory.",5);
        	if (ev.getInventory().getHolder() instanceof Hopper &&
        			((Hopper)(ev.getInventory().getHolder())).getWorld().getName().equalsIgnoreCase("FilterCube")) {
        		SoundUtils.playLocalSound(p, Sound.BLOCK_CHEST_LOCKED, 0.6f, 0.4f);
        	}
        	else
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
        		CubeType cub = p.getOpenInventory().getTopInventory().getSize()==9?CubeType.NORMAL:p.getOpenInventory().getTopInventory().getSize()==54?CubeType.VACUUM:CubeType.LARGE;
        		SoundUtils.playLocalSound(p, Sound.BLOCK_CHEST_CLOSE, 1.0f, 1.0f);
        		itemCube_saveConfig(id,itemcube_contents,cub);
        		if (!pd.opened_another_cube) {
        			ItemCubeWindow.removeAllItemCubeWindows(p);
        		}
        		pd.isViewingItemCube=false;
        	}
        	if (ev.getInventory().getHolder() instanceof Chest) {
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
							WorldShopManager.UpdateSign(shop, shop.getID(), s,WorldShop.isPurchaseShopSign(s));
							TwosideShops.SaveWorldShopData(shop);
						}},1);
        			}
        		}
        	}
    	}
    }
	public void DropDeathInventoryContents(Player p, Location deathloc) {
		List<ItemStack> list = PrepareDropItems(p);
		deathloc.getWorld().loadChunk(deathloc.getChunk());
		while (list.size()>0) {
			if (deathloc.getChunk().isLoaded()) {
				Item it = null;
				do {
					deathloc.getWorld().loadChunk(deathloc.getChunk());
					it=deathloc.getWorld().dropItemNaturally(deathloc, list.get(0));
					it.setInvulnerable(true);
					TwosideKeeper.temporary_chunks.add(deathloc.getChunk());
					} while (it==null || !it.isValid());
				TwosideKeeper.log("Dropping "+list.get(0).toString()+" at Death location "+deathloc,2);
				list.remove(0);
			} else {
				deathloc.getWorld().loadChunk(deathloc.getChunk());
			}
		}
		for (Chunk c : TwosideKeeper.temporary_chunks) {
			c.unload(true);
		}
		TwosideKeeper.temporary_chunks.clear();
	}
	public void DropDeathInventoryContents(Player p, Location deathloc, int tickdelay) {
		List<ItemStack> list = PrepareDropItems(p);
		Bukkit.getScheduler().scheduleSyncDelayedTask(this,new DropDeathItems(p,list,deathloc),tickdelay);
	}
	public List<ItemStack> PrepareDropItems(Player p) {
		Inventory contents = Bukkit.createInventory(p, 45); 
		log("Contents list includes ",2);
		for (int i=0;i<p.getOpenInventory().getTopInventory().getSize();i++) {
			if (p.getOpenInventory().getTopInventory().getItem(i)!=null) {
				//p.sendMessage("Saving item "+p.getOpenInventory().getTopInventory().getItem(i).toString()+" in slot "+i);
				log(p.getOpenInventory().getTopInventory().getItem(i).toString(),2);
				contents.addItem(p.getOpenInventory().getTopInventory().getItem(i));
			} else {
				//p.sendMessage("Saving item AIR in slot "+i);
				contents.addItem(new ItemStack(Material.AIR));
			}
		}
		log("-------",2);
		List<ItemStack> list=new ArrayList<ItemStack>();
		for (int i=0;i<contents.getSize();i++) {
			if (contents.getItem(i)!=null &&
					contents.getItem(i).getType()!=Material.AIR) {
				list.add(contents.getItem(i));
			}
		}
		return list;
	}

    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void onItemChange(PlayerItemHeldEvent ev) {
    	final Player player = ev.getPlayer();
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			@Override
			public void run() {
		    	setPlayerMaxHealth(player,player.getHealth()/player.getMaxHealth());
			}
		},1);
		Christmas.RunPlayerItemHeldEvent(ev);
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
    	if (ev.getEntity() instanceof Player) {
	    	Player p = (Player)ev.getEntity();
	    	if (PlayerMode.getPlayerMode(p)==PlayerMode.SLAYER) {
	    		ev.setCancelled(true);
	    		return;
	    	}
			if (p!=null) {
				p.getScoreboard().getTeam(p.getName().toLowerCase()).setSuffix(createHealthbar(((p.getHealth())/p.getMaxHealth())*100,p));
				p.getScoreboard().getTeam(p.getName().toLowerCase()).setPrefix(GenericFunctions.PlayerModePrefix(p));
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
		}
    }
    
    @SuppressWarnings("deprecation")
	@EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent ev) {
    	final Player player = (Player)ev.getWhoClicked();
    	PlayerStructure pd = (PlayerStructure)playerdata.get(player.getUniqueId());
    	pd.isViewingInventory=true;
    	log("Raw Slot Clicked: "+ev.getRawSlot(),5); //5,6,7,8 for gear slots.
    	log("Slot Type: "+ev.getSlotType().name(),5); //5,6,7,8 for gear slots.
    	
    	if (!Christmas.runInventoryClickEvent(ev)) {
    		return;
    	}
    	
    	if (ev.getSlotType()==SlotType.ARMOR || ev.getSlotType()==SlotType.QUICKBAR) {
        	log("Triggered.",5); //5,6,7,8 for gear slots.
			Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
				@Override
				public void run() {
			    	setPlayerMaxHealth(player);
				}
			},1);
    	}
    	
    	//Check for a shift-right click for Filter Cubes.
    	if (ev.getClick()==ClickType.SHIFT_RIGHT) {
    		ItemStack item = ev.getCurrentItem();
    		if (CustomItem.isFilterCube(item)) {
    			int cubeid = ItemCubeUtils.getItemCubeID(item);
    			Hopper targethopper = ItemCubeUtils.getFilterCubeHopper(cubeid);
    			targethopper.getChunk().load();
    			ev.getWhoClicked().openInventory(targethopper.getInventory());
    			SoundUtils.playLocalSound((Player)ev.getWhoClicked(), Sound.BLOCK_CHEST_LOCKED, 1.0f, 1.0f);
    			ev.setCancelled(true);
    			return;
    		}
    	}
    	
    	if ((ev.getClick()==ClickType.SHIFT_LEFT || ev.getClick()==ClickType.SHIFT_RIGHT) &&
    			ev.getWhoClicked().getInventory().getExtraContents()[0]==null && GenericFunctions.AllowedToBeEquippedToOffHand((Player)ev.getWhoClicked(),ev.getCurrentItem(),ev.getRawSlot()) &&
        				((ev.getInventory().getType()!=InventoryType.WORKBENCH && ev.getRawSlot()>=0) ||
        	    		    	(ev.getInventory().getType()==InventoryType.WORKBENCH && ev.getRawSlot()>9))) {
    		//Move the item into that slot instead.
    		ev.getWhoClicked().getEquipment().setItemInOffHand(ev.getCurrentItem().clone());
    		ev.setCurrentItem(new ItemStack(Material.AIR));
    		ev.setResult(Result.DENY);
    		ev.setCancelled(true);
    		return;
    	}
		
		if (DeathManager.deathStructureExists(player) && ev.getInventory().getTitle().equalsIgnoreCase("Death Loot")) {
			//See how many items are in our inventory. Determine final balance.
			//Count the occupied slots.
			if (ev.getRawSlot()<45) {
				if (getPlayerMoney(player)+getPlayerBankMoney(player)-DeathManager.CalculateDeathPrice(player)*DeathManager.CountOccupiedSlots(player.getInventory())>=DeathManager.CalculateDeathPrice(player)) {
					//player.getInventory().addItem(ev.getCurrentItem());
					if (ev.getCurrentItem()!=null &&
							ev.getCurrentItem().getType()!=Material.AIR) {
						//player.getLocation().getWorld().dropItemNaturally(player.getLocation(), ev.getCurrentItem()).setPickupDelay(0);
						boolean equipped = AutoEquipItem(ev.getCurrentItem(),player);
						if (!equipped) {
							GenericFunctions.giveItem(player, ev.getCurrentItem());
						}
						ev.setCurrentItem(new ItemStack(Material.AIR));
			
						final DecimalFormat df = new DecimalFormat("0.00");
						Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
							@Override
							public void run() {
								player.sendMessage(ChatColor.BLUE+"New Balance: "+ChatColor.GREEN+"$"+df.format((getPlayerMoney(player)+getPlayerBankMoney(player)-DeathManager.CalculateDeathPrice(player)*DeathManager.CountOccupiedSlots(player.getInventory()))));
							}
						},1);
					}
				} else {
					player.sendMessage(ChatColor.RED+"You cannot afford to salvage any more items!");
				}
			}
			ev.setCancelled(true);
			return;
		}
		
		if (ev.getClick()==ClickType.RIGHT && Artifact.isMalleableBase(ev.getCurrentItem())) {
			if (MalleableBaseQuest.getStatus(ev.getCurrentItem())==QuestStatus.UNFORMED) {
				StartMalleableBaseQuest((Player)ev.getWhoClicked());
				ev.setCancelled(true);
				return;
			} else {
				ev.setCurrentItem(ProceedWithMalleableBaseQuest((Player)ev.getWhoClicked(),ev.getCurrentItem()));
				ev.setCancelled(true);
				return;
			}
		}
		
		//Left-Click for an Arrow Quiver.
		if (ev.getClick()==ClickType.LEFT && ev.getCursor()!=null && ev.getCursor().getAmount()>0 &&
    				GenericFunctions.isValidArrow(ev.getCursor()) &&
    				ArrowQuiver.isValidQuiver(ev.getCurrentItem()) &&
    				((ev.getInventory().getType()!=InventoryType.WORKBENCH && ev.getRawSlot()>=0) ||
    		    	(ev.getInventory().getType()==InventoryType.WORKBENCH && ev.getRawSlot()>9))) {
			//Insert the item into there.
			ArrowQuiver.addContents(ev.getCurrentItem(), ev.getCursor());
			ArrowQuiver.updateQuiverLore(ev.getCurrentItem());
			ev.setCursor(new ItemStack(Material.AIR));
			//Cancel this click event.
			ev.setCancelled(true);
			ev.setResult(Result.DENY);
			return;
		}
		
		//Right-Click for an Arrow Quiver.
		if (ev.getClick()==ClickType.RIGHT && (ev.getCursor()==null || ev.getCursor().getType()==Material.AIR) && ev.getCursor().getAmount()==0 &&
				ArrowQuiver.isValidQuiver(ev.getCurrentItem()) &&
				((ev.getInventory().getType()!=InventoryType.WORKBENCH && ev.getRawSlot()>=0) ||
		    	(ev.getInventory().getType()==InventoryType.WORKBENCH && ev.getRawSlot()>9))) {
			//Try to pull out items from the Quiver from the current mode.
			List<ItemStack> contents = ArrowQuiver.getContents(ArrowQuiver.getID(ev.getCurrentItem()));
				if (contents.size()>0) {
				ItemStack modeitems = contents.get(ArrowQuiver.getArrowQuiverMode(ev.getCurrentItem())).clone();
				if (modeitems.getAmount()>64) {
					modeitems.setAmount(64);
				}
				ArrowQuiver.removeContents(ev.getCurrentItem(), modeitems);
				ArrowQuiver.updateQuiverLore(ev.getCurrentItem());
				ev.setCursor(modeitems);
			}
			ev.setCancelled(true);
			ev.setResult(Result.DENY);
			return;
		}
		
    	//Check for a left click for an arrow quiver.
    	/*if (ev.getClick()==ClickType.LEFT) { //LEGACY CODE.
    		//Tries to take out 1 stack of arrows.
    		//We're going to try to deposit arrows.
    		if (ev.getCursor()!=null && ev.getCursor().getAmount()>0 &&
    				ev.getCursor().getType()==Material.ARROW) {
	    		Player p = (Player)ev.getWhoClicked();
	    		if (playerHasArrowQuiver(p)) {
	    			boolean foundquiver=false;
	    			int slot=-1;
					if (ev.getSlot()>=0 && p.getInventory().getSize()>ev.getSlot()-1 &&
							p.getInventory().getItem(ev.getSlot())!=null &&
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
	    				
	    				playerInsertArrowQuiver(p, slot , ev.getCursor().getAmount());
						p.sendMessage(ChatColor.DARK_GRAY+""+ev.getCursor().getAmount()+" arrow"+((ev.getCursor().getAmount()==1)?"":"s")+" "+((ev.getCursor().getAmount()==1)?"was":"were")+" added to your arrow quiver. Arrow Count: "+ChatColor.GRAY+playerGetArrowQuiverAmt(p,playerGetArrowQuiver(p)));
						ev.setCursor(new ItemStack(Material.AIR));
						//Cancel this click event.
						ev.setCancelled(true);
						ev.setResult(Result.DENY);
						return;
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
				if (ev.getSlot()>=0 && p.getInventory().getSize()>ev.getSlot()-1 &&
						p.getInventory().getItem(ev.getSlot())!=null &&
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
					return;
    			}
    		}
    	}*/
    	
    	if (ev.getInventory().getTitle().contains("Item Cube #") &&
    			ev.getRawSlot()==-999) {
    		//log("Cursor: "+ev.getCursor().toString(),2);
    		ItemStack item = ev.getCursor();
    		if (item.getType()==Material.AIR) {
    			//ItemCubeWindow.removeAllItemCubeWindows((Player)ev.getWhoClicked());
    			Player p = (Player)ev.getWhoClicked();
    			pd = PlayerStructure.GetPlayerStructure(p);
    			//
    			pd.opened_another_cube=true;
    			if (pd.itemcubelist.size()==0) {ev.getWhoClicked().closeInventory();}
    			pd.opened_another_cube=false;
    			ItemCubeWindow.popItemCubeWindow((Player)ev.getWhoClicked());
    		}
    	}
    	
    	//Check for a Vacuum Cube. If this is a Vacuum Cube and we're trying to do something with the item, then don't allow it.
    	//PerformVacuumCubeChecks(ev);

    	//LEFT CLICK STUFF.
    	//WARNING! This only happens for ITEM CUBES! Do not add other items in here!
    	pd = (PlayerStructure) playerdata.get(ev.getWhoClicked().getUniqueId());
    	if (ev.getClick()==ClickType.RIGHT || ev.getClick()==ClickType.SHIFT_RIGHT || (ev.getCursor()==null || ev.getCursor().getType()==Material.AIR)) {
	    	if (((ev.getInventory().getType()!=InventoryType.WORKBENCH && ev.getRawSlot()>=0) ||
	    			(ev.getInventory().getType()==InventoryType.WORKBENCH && ev.getRawSlot()>9)) && ev.getCurrentItem()!=null) {
		    	if (ev.getCurrentItem().hasItemMeta() && (ev.getCurrentItem().getType()!=Material.AIR)) {
		    		ItemMeta item_meta = ev.getCurrentItem().getItemMeta();
		    		if (item_meta.hasLore()) {
		    			List<String> item_meta_lore = item_meta.getLore();
		    			if (item_meta_lore.size()>=4 && item_meta_lore.get(3).contains(ChatColor.DARK_PURPLE+"ID#")) {
		    				int itemcubeid = -1;
		    				if (((PlayerStructure)playerdata.get(ev.getWhoClicked().getUniqueId())).isViewingItemCube &&
		    						ev.getWhoClicked().getOpenInventory().getTitle().contains("Item Cube #")) {
		    					itemcubeid = Integer.parseInt(ev.getWhoClicked().getOpenInventory().getTitle().split("#")[1]); //This is the ID of the window we are looking at, if one exists.
		    				} else {
		    					itemcubeid = -1;
		    				}
		    				//This is an Item Cube.
							//Check to see if the cursor item is an item cube.
							if ((ItemCubeUtils.isItemCubeMaterial(ev.getCurrentItem().getType()) &&
									ItemCubeUtils.isItemCube(ev.getCurrentItem()))) {
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
											//SoundUtils.playLocalSound(p, Sound.BLOCK_NOTE_HARP, 0.4f, 0.2f);
											ev.setCancelled(true);
											return;
										}
									}
								}
							}
			}}}}
    	}

    	//WARNING! This only happens for ITEM CUBES! Do not add other items in here!
    	if (((ev.getInventory().getType()!=InventoryType.WORKBENCH && ev.getRawSlot()>=0) ||
    			(ev.getInventory().getType()==InventoryType.WORKBENCH && ev.getRawSlot()>9)) && ev.isLeftClick() && ev.getCurrentItem()!=null && ev.getCursor()!=null) {
	    	if (ev.getCurrentItem().hasItemMeta() && (ev.getCursor().getType()!=Material.AIR)) {
	    		ItemMeta item_meta = ev.getCurrentItem().getItemMeta();
	    		if (item_meta.hasLore()) {
	    			List<String> item_meta_lore = item_meta.getLore();
	    			if (item_meta_lore.size()>=4 && item_meta_lore.get(3).contains(ChatColor.DARK_PURPLE+"ID#")) {
	    				int idnumb = Integer.parseInt(item_meta_lore.get(3).split("#")[1]);
	    				int itemcubeid = -1; //This is the ID of the window we are looking at, if one exists.
	    				CubeType cubetype = CubeType.NORMAL;
	    				//This is an Item Cube.
    					ev.setCancelled(true);
    					//ev.setResult(Result.DENY);
    					

    					int clicked_size;
    					if (ev.getCurrentItem().getType()==Material.CHEST) {
    						clicked_size=9;
    						cubetype=CubeType.NORMAL;
    					} else {
    		    			if (CustomItem.isVacuumCube(ev.getCurrentItem())) {
    							cubetype=CubeType.VACUUM;
    		    				clicked_size=54;
    		    			} else {
	    						clicked_size=27;
	        					if (ev.getCurrentItem().getType()==Material.STORAGE_MINECART) {
	        						cubetype=CubeType.LARGE;
	        					} else {
	        						cubetype=CubeType.ENDER;
	        					}
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
    										SoundUtils.playLocalSound(p,Sound.BLOCK_CHEST_OPEN,1.0f,1.0f);
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
    						return;
    					}
    					else
    					//Make sure we are not already inside the cube we're placing into.
    					{
    						CubeType cub = clicked_size==9?CubeType.NORMAL:clicked_size==54?CubeType.VACUUM:CubeType.LARGE;
    						if (cub==CubeType.VACUUM) {
    							//A Vacuum Cube only accepts blocks, not items.
    							TwosideKeeper.log("Cursor is "+ev.getCursor(), 5);
    							if (!ev.getCursor().getType().isBlock()) {
        							TwosideKeeper.log("Not allowed! "+ev.getCurrentItem()+","+ev.getCursor(), 5);
        							ev.getWhoClicked().sendMessage(ChatColor.RED+"You can only insert/remove Blocks in a Vacuum Cube.");
        							ev.setCursor(ev.getCursor());
    								ev.setCancelled(true);
    								return;
    							}
    						}
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
		    						HashMap<Integer,ItemStack> result = virtualinventory.addItem(ev.getCursor());
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
		    						itemCube_saveConfig(idnumb,itemslist,cub);
		    						return;
	    						} else {
		    						//Well, we're already in here, I don't know why they didn't just use the
		    						//minecraft inventory management system. Now I have to do math...
									//Add it to the inventory being viewed.
									HashMap<Integer,ItemStack> result = ev.getWhoClicked().getOpenInventory().getTopInventory().addItem(ev.getCursor());
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
		    						return;
	    					}
    					}
	    			}
	    		}
	    	}
    	}
    	
    	//RIGHT CLICK STUFF DOWN HERE.
    	log("Inventory click.",5);
    	//WARNING! This only happens for ITEM CUBES! Do not add other items in here!
    	if (((ev.getInventory().getType()!=InventoryType.WORKBENCH && ev.getRawSlot()>=0) ||
    			(ev.getInventory().getType()==InventoryType.WORKBENCH && ev.getRawSlot()>9)) && ev.isRightClick() && ev.getCurrentItem()!=null && ev.getCurrentItem().getAmount()==1) {
	    	log("Clicked Item: "+ev.getCurrentItem().toString(),5);
	    	if (ev.getCurrentItem().hasItemMeta()) {
	        	log("Item Meta: "+ev.getCurrentItem().getItemMeta().toString(),5);
	    		ItemMeta item_meta = ev.getCurrentItem().getItemMeta();
	    		if (item_meta.hasLore()) {
	    			List<String> item_meta_lore = item_meta.getLore();
	    			if (item_meta_lore.size()>=4 && item_meta_lore.get(3).contains(ChatColor.DARK_PURPLE+"ID#")) {
	    				int idnumb = Integer.parseInt(item_meta_lore.get(3).split("#")[1]);
	    				int itemcubeid = -1;
	    				if (((PlayerStructure)playerdata.get(ev.getWhoClicked().getUniqueId())).isViewingItemCube &&
	    						ev.getWhoClicked().getOpenInventory().getTitle().contains("Item Cube #")) {
	    					itemcubeid = Integer.parseInt(ev.getWhoClicked().getOpenInventory().getTitle().split("#")[1]); //This is the ID of the window we are looking at, if one exists.
	    				} else {
	    					itemcubeid = -1;
	    				}
	    				if (idnumb==itemcubeid) {
							//The inventory we are viewing is the same as the item cube we have clicked on!
							//Stop this before the player does something dumb!
							//Player p = ((Player)ev.getWhoClicked());
							//SoundUtils.playLocalSound(p, Sound.BLOCK_NOTE_HARP, 0.4f, 0.2f);
							ev.setCancelled(true);
							return;
						} else {
							TwosideKeeper.log("Got to here. ID is "+itemcubeid, 5);
		    				Player p = (Player)ev.getWhoClicked();
							if (itemcubeid!=-1 && ev.getRawSlot()<=ev.getView().getTopInventory().getSize()-1) {
								//This means we are viewing an item cube currently. Add it to our list. 
								ItemCubeWindow.addItemCubeWindow(p, itemcubeid);
							} else 
							if (itemcubeid!=-1) {
								log("Size is "+(ev.getView().getTopInventory().getSize())+", Clicked slot "+ev.getRawSlot(),5);
								ItemCubeWindow.removeAllItemCubeWindows(p); 
							}
		    				log("This is an Item Cube.",5);
	    					int inventory_size;
	    					if (ev.getCurrentItem().getType()==Material.CHEST) {
	    						inventory_size=9;
	    					} else {
	    						if (CustomItem.isVacuumCube(ev.getCurrentItem())) {
	    							inventory_size=54;
	    						} else {
	    							inventory_size=27;
	    						}
	    					}
	    					final PlayerStructure pd2 = pd;
		    				if (!ItemCube.isSomeoneViewingItemCube(idnumb,p)) {
		    		    		log("Attempting to open",5);
		    					//ev.setCancelled(true);
		    					ev.setResult(Result.DENY);
		    					//pd.itemcubeviews.add(p.getOpenInventory());
	    						pd.opened_another_cube=true;
		    					Inventory temp = Bukkit.getServer().createInventory(p, inventory_size, "Item Cube #"+idnumb);
		    					openItemCubeInventory(temp);
		    					Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {@Override public void run() {p.openInventory(temp);
		    					//TODO Implement Graphs. //BuildItemCubeGraph(p);
		    					pd2.opened_another_cube=false;
	    						pd2.isViewingItemCube=true;}},1);
	    						SoundUtils.playLocalSound(p,Sound.BLOCK_CHEST_OPEN,1.0f,1.0f);
	    						return;
							} else {
		    					//ev.setCancelled(true);
		    					ev.setResult(Result.DENY);
		    					//ItemCube.displayErrorMessage(p);
		    					//pd.itemcubeviews.add(p.getOpenInventory());
	    						pd.opened_another_cube=true;
	    						Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {@Override public void run() {p.openInventory(ItemCube.getViewingItemCubeInventory(idnumb, p));
	    						//TODO Implement Graphs. //BuildItemCubeGraph(p);
		    					pd2.opened_another_cube=false;
		        				pd2.isViewingItemCube=true;}},1);
		    	    			SoundUtils.playLocalSound(p, Sound.BLOCK_CHEST_OPEN, 1.0f, 1.0f);
		    	    			return;
							}
						}
	    			}
	    		}
	    	}
    	}
    }
    
    //TODO Implement Graphs.
    /*protected void BuildItemCubeGraph(Player p) {
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		pd.graph = GraphBuilder.undirected().build();
		for (ItemStack it : p.getInventory().getContents()) {
			if (ItemCubeUtils.isItemCube(it)) {
				int id = ItemCubeUtils.getItemCubeID(it);
				pd.graph.addNode(id);
				TwosideKeeper.log("Added Node "+id+" ["+pd.graph.nodes().size()+"]", 0);
				ContinueBuildingItemCubeGraph(id,p);
			}
		}
	}*/

    //TODO Implement Graphs.
	/*private void ContinueBuildingItemCubeGraph(int id, Player p) {
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		CubeType size = ItemCubeUtils.getCubeType(id);
		int slots = CubeType.getSlotsFromType(size);
		Inventory virtualinventory = null;
		virtualinventory = ItemCube.getViewingItemCubeInventory(id, p);
		if (virtualinventory==null) {
			virtualinventory = Bukkit.createInventory(p, slots);
			List<ItemStack> items = itemCube_loadConfig(id);
			for (int i=0;i<virtualinventory.getSize();i++) {
				if (items.get(i)!=null) {
					ItemStack testitem = items.get(i);
					if (ItemCubeUtils.isItemCube(testitem)) {
						int newid = ItemCubeUtils.getItemCubeID(testitem);
						pd.graph.addNode(newid);
						pd.graph.putEdge(id, newid);
						TwosideKeeper.log("Added Node "+newid+" ["+pd.graph.nodes().size()+"]", 0);
						ContinueBuildingItemCubeGraph(newid,p);
					}
				}
			}
		}
	}*/
	
	
	
	public void PerformVacuumCubeChecks(InventoryClickEvent ev) {
		if (((ev.getInventory().getType()!=InventoryType.WORKBENCH && ev.getRawSlot()>=0) ||
    			(ev.getInventory().getType()==InventoryType.WORKBENCH && ev.getRawSlot()>9)) && ev.getCurrentItem()!=null) {
			//int idnumb = Integer.parseInt(item_meta_lore.get(3).split("#")[1]);
			int itemcubeid = -1; //This is the ID of the window we are looking at, if one exists.
			CubeType cubetype = CubeType.NORMAL;
			//This is an Item Cube.
			//ev.setResult(Result.DENY);
			if (((PlayerStructure)playerdata.get(ev.getWhoClicked().getUniqueId())).isViewingItemCube &&
					ev.getWhoClicked().getOpenInventory().getTitle().contains("Item Cube #")) {
				itemcubeid = Integer.parseInt(ev.getWhoClicked().getOpenInventory().getTitle().split("#")[1]); //This is the ID of the window we are looking at, if one exists.

				cubetype = ItemCubeUtils.getCubeType(itemcubeid);
				if (cubetype==CubeType.VACUUM) {
					//TwosideKeeper.log(ev.getCurrentItem()+"|||"+ev.getCursor(), 0);
					if ((ev.getCurrentItem().getType()!=Material.AIR && !ev.getCurrentItem().getType().isBlock()) || (ev.getCursor().getType()!=Material.AIR && !ev.getCursor().getType().isBlock())) {
						ev.getWhoClicked().sendMessage(ChatColor.RED+"You can only insert/remove Blocks in a Vacuum Cube.");
						//TwosideKeeper.log((ev.getCurrentItem().getType()!=Material.AIR)+","+(!ev.getCurrentItem().getType().isBlock())+"|||"+(ev.getCursor().getType()!=Material.AIR)+","+(!ev.getCursor().getType().isBlock()), 0);
						ev.setCancelled(true);
						return;
					}
				}
			} else {
				itemcubeid = -1;
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
    	
    	TwosideRecyclingCenter.AddItemToRecyclingCenter(i.getItemStack());
    }
    
    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void onBlockDispense(BlockDispenseEvent ev) {
    	if (!Christmas.HandleDispenseEvent(ev)) {return;}
    }

	@EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void onChunkLoadEvent(ChunkLoadEvent ev) {
    	//Grab all entities. Create monster structures for all monsters. Detect elites and leaders and set their status accordingly.
		if (TwosideKeeper.livingentitydata!=null) {
	    	for (Entity e : ev.getChunk().getEntities()) {
	    		if (e!=null && e.isValid() && (e instanceof LivingEntity)) {
	    			LivingEntity m = (LivingEntity)e;
	    			updateMonsterFlags(m);
	    			if (e instanceof Snowman) {
	    				Snowman snowy = (Snowman)e;
	    				if (e.getCustomName()!=null) {
	    					if (!SnowmanHuntList.contains(ChatColor.stripColor(e.getCustomName()))) {
	    						SnowmanHuntList.add(ChatColor.stripColor(e.getCustomName()));
	    					}
	    				}
	    			}
	    		}
	    	}
		}
    }

	@EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void onChunkUnloadEvent(ChunkUnloadEvent ev) {
		Chunk c = ev.getChunk();
		if (c.getWorld().getName().equalsIgnoreCase("world_nether")) {
			for (Entity e : c.getEntities()) {
				if (e!=null && (e instanceof FallingBlock)) {
					FallingBlock fb = (FallingBlock)e;
					Material type = fb.getMaterial();
					if (type==Material.REDSTONE_BLOCK) {
						fb.remove();
					}
				}
			}
		}
		if (c.getWorld().getName().equalsIgnoreCase("FilterCube") &&
				ItemCubeUtils.SomeoneHasAFilterCubeOpen()) {
			ev.setCancelled(true);
		}
		if (temporary_chunks.contains(c)) {
			ev.setCancelled(true);
		}
	}

	public void updateMonsterFlags(LivingEntity m) {
		LivingEntityStructure ms = LivingEntityStructure.getLivingEntityStructure(m);
		if (m instanceof Monster) {
			MonsterDifficulty md = MonsterController.getMonsterDifficulty((Monster)m);
			if (md == MonsterDifficulty.ELITE) {
				ms.SetElite(true);
			}
			if (MonsterController.isZombieLeader(m)) {
				m = MonsterController.convertMonster((Monster)m,md);
				log("Setting a monster with Difficulty "+MonsterController.getMonsterDifficulty((Monster)m).name()+" w/"+m.getHealth()+"/"+m.getMaxHealth()+" HP to a Leader.",5);
				ms.SetLeader(true);
			}
			if (m instanceof Wither) {
				ms.SetLeader(true);
			}
			if (TwosideKeeper.ELITEGUARDIANS_ACTIVATED) {
				if (m instanceof Guardian) {
					Guardian g = (Guardian)m;
					if (g.isElder()) {
						ms.SetElite(true);
						g.setCustomName(ChatColor.LIGHT_PURPLE+"Elite Guardian");
						g.setCustomNameVisible(true);
					}
				}
			}
		}
	}
    
    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void MonsterSpawnEvent(CreatureSpawnEvent ev) {
    	if (ev.getEntity() instanceof LivingEntity) {
			LivingEntity m = ev.getEntity();
			LivingEntityStructure.getLivingEntityStructure(m);
    	}
    	if ((ev.getSpawnReason().equals(SpawnReason.DISPENSE_EGG) || 
    			ev.getSpawnReason().equals(SpawnReason.EGG)) &&
    			CustomDamage.trimNonLivingEntities(ev.getEntity().getNearbyEntities(8, 8, 8)).size()>20) {
    		ev.setCancelled(true);
    		log("Denied chicken spawn.",4);
    		return;
    	}
    	
    	if (ev.getEntity() instanceof Shulker) {
    		convertToStrongerShulker(ev.getEntity());
    	}
    	
    	if (ev.getEntity() instanceof Wither) {
    		MonsterController.HandleWitherSpawn(ev.getEntity());
    		return;
    	}
    	
    	if ((ev.getSpawnReason().equals(SpawnReason.NATURAL) ||
    			ev.getSpawnReason().equals(SpawnReason.SPAWNER_EGG) ||
    			ev.getSpawnReason().equals(SpawnReason.REINFORCEMENTS) ||
    			ev.getSpawnReason().equals(SpawnReason.VILLAGE_INVASION) ||
    			ev.getSpawnReason().equals(SpawnReason.CHUNK_GEN) ||
    			ev.getSpawnReason().equals(SpawnReason.SLIME_SPLIT) ||
    			ev.getSpawnReason().equals(SpawnReason.MOUNT) ||
    			ev.getSpawnReason().equals(SpawnReason.JOCKEY))) {
        	TwosideKeeper.log("Spawned a "+GenericFunctions.GetEntityDisplayName(ev.getEntity()),TwosideKeeper.SPAWN_DEBUG_LEVEL);
    		if (ev.getSpawnReason().equals(SpawnReason.REINFORCEMENTS) || ev.getSpawnReason().equals(SpawnReason.VILLAGE_INVASION)) {
    			//Remove this one and spawn another one.
            	TwosideKeeper.log(" This is a reinforcement.",TwosideKeeper.SPAWN_DEBUG_LEVEL);
    			Location loc = ev.getEntity().getLocation().clone();
    			Monster m = (Monster)loc.getWorld().spawnEntity(loc, ev.getEntityType());
    			m.setTarget(((Monster)ev.getEntity()).getTarget());
    			MonsterController.MobHeightControl(m,true);
    			if (m.getCustomName()!=null) {
    				m.setCustomName(GenericFunctions.getDisplayName(m)+" Minion");
    			} else {
    				m.setCustomName("Zombie Minion");
    			}
    			ev.getEntity().remove();
    		} else
    		{
            	TwosideKeeper.log(" This is a normal mob.",TwosideKeeper.SPAWN_DEBUG_LEVEL);
    			if (!ev.getSpawnReason().equals(SpawnReason.SPAWNER_EGG) && !ev.getSpawnReason().equals(SpawnReason.SLIME_SPLIT)) {
		    		if (!habitat_data.addNewStartingLocation(ev.getEntity())) {
		    			ev.getEntity().remove();
		    			ev.setCancelled(true);
		    			return;
		    		}
    			}
	    		if (!MonsterController.MobHeightControl(ev.getEntity(),false,ev.getSpawnReason())) {
	            	TwosideKeeper.log("  Not allowed by Mob Height Controller",TwosideKeeper.SPAWN_DEBUG_LEVEL);
	    			ev.setCancelled(true);
	    			return;
	    			//This spawn was not allowed by the mob height controller.
	    		}
	    		if (ev.getEntity() instanceof Skeleton) {
	    			Skeleton sk = (Skeleton)ev.getEntity();
	    			if (sk.getSkeletonType()==SkeletonType.WITHER) {
	    				if (Math.random()<=0.8) {
	    					if (Math.random()<=0.6) {
	    						MonsterController.convertMonster(sk, MonsterDifficulty.HELLFIRE);
	    					} else {
	    						MonsterController.convertMonster(sk, MonsterDifficulty.DEADLY);
	    					}
	    				} else {
	    					if (Math.random()<=0.5) {
	    						MonsterController.convertMonster(sk, MonsterDifficulty.DANGEROUS);
	    					}
	    				}
	    			}
	    		}
    		}
    	} else {
        	log("Reason for spawn: "+ev.getSpawnReason().toString()+" for Entity "+GenericFunctions.GetEntityDisplayName(ev.getEntity()),1);
    	}
    	if (ev.getSpawnReason().equals(SpawnReason.SPAWNER)) {
    		if (MonsterController.isZombieLeader(ev.getEntity())) {
    			MonsterController.removeZombieLeaderAttribute(ev.getEntity());
    		}
    	}
    	if (ev.getLocation().getWorld().getName().equalsIgnoreCase("world") &&
    			ev.getEntityType()==EntityType.HORSE) {
    		Horse h = (Horse)ev.getEntity();
    		if (h.getVariant().equals(Variant.SKELETON_HORSE)) {
    			//This is a skeleton horse in the overworld. We are going to disable these for now. Future plans for them...
    			ev.getEntity().remove();
    			log("Prevented a skeleton horse from spawning at Location "+ev.getLocation().toString()+".",3);
    			return;
    		}
    	}
    }
    
    private void convertToStrongerShulker(LivingEntity entity) {
    	if (entity.getWorld().getName().equalsIgnoreCase("world_the_end")) {
	    	entity.setFireTicks(Integer.MAX_VALUE);
	    	entity.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,Integer.MAX_VALUE,1));
	    	entity.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,Integer.MAX_VALUE,3));
	    	entity.setCustomName(ChatColor.DARK_BLUE+""+ChatColor.MAGIC+"End"+" "+GenericFunctions.CapitalizeFirstLetters(entity.getType().name()));
	    	entity.setMaxHealth(entity.getMaxHealth()*420.0);
			entity.setHealth(entity.getMaxHealth());
    	}
	}
    
	//A fix to make achievemnt announcements not show the healthbar!
    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void playerGetAchievementEvent(PlayerAchievementAwardedEvent ev) {
    	final Player p = ev.getPlayer();
    	ev.getPlayer().getScoreboard().getTeam(ev.getPlayer().getName().toLowerCase()).setSuffix("");
    	ev.getPlayer().getScoreboard().getTeam(ev.getPlayer().getName().toLowerCase()).setPrefix("");
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
			for (PotionEffect pe : ev.getPotion().getEffects()) {
				if (pe.getType().equals(PotionEffectType.POISON)) {
					isPoison=true;
					duration=pe.getDuration();
					break;
				}
			}
			if (isPoison) {
				for (LivingEntity affected : ev.getAffectedEntities()) {
					switch (MonsterController.getMonsterDifficulty(w)) {
						case DANGEROUS:{
							affected.addPotionEffect(new PotionEffect(PotionEffectType.POISON,duration+1,1)); //Poison II
						}break;
						case DEADLY:{
							affected.addPotionEffect(new PotionEffect(PotionEffectType.POISON,duration+1,2)); //Poison III
						}break;
						case HELLFIRE:{
							affected.addPotionEffect(new PotionEffect(PotionEffectType.POISON,duration+1,3)); //Poison IV
						}break;
						case ELITE:{
							affected.addPotionEffect(new PotionEffect(PotionEffectType.POISON,duration+1,4)); //Poison V
						}break;
						default:{
							affected.addPotionEffect(new PotionEffect(PotionEffectType.POISON,duration+1,0)); //Poison I
						}
					}
				}
			}
    	}
    }
    
    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void updateHealthbarDamageEvent(EntityDamageEvent ev) {
    	if (ev.getEntity().isDead()) {ev.setCancelled(true); return;}
		if (ev.getEntity() instanceof Player) {
			Player p = (Player)ev.getEntity();
			if (!p.isOnline()) {
				ev.setCancelled(true);
				return;
			}
		}
    	if (!Christmas.ChristmasDamageEvent(ev)) {return;}
    	if (EntityUtils.PreventEnderCrystalDestruction(ev.getEntity())) {
    		ev.setCancelled(true);
    		return;
    	}
    	if (ev.getCause()!=DamageCause.ENTITY_ATTACK &&
    			ev.getCause()!=DamageCause.PROJECTILE &&
    			ev.getCause()!=DamageCause.ENTITY_EXPLOSION &&
    	    	ev.getCause()!=DamageCause.THORNS &&
    			ev.getCause()!=DamageCause.MAGIC) {
    		//We handle the event inside of here. DealDamage(ev.getDamage(DamageModifier.BASE));
    		if (ev.getCause()==DamageCause.BLOCK_EXPLOSION) {
    			ev.setDamage(DamageModifier.BASE,0);
    			ev.setCancelled(true);
    		} else
    		if (ev.getEntity() instanceof LivingEntity) {
    			if (ev.getCause()==DamageCause.VOID && (ev.getEntity() instanceof Player)) {
    				CustomDamage.executeVoidSurvival((Player)ev.getEntity());
    			}
    			if (ev.getCause()!=DamageCause.CUSTOM) { //This is not handled damage, so apply it.
    				double dmgdealt = ev.getDamage(DamageModifier.BASE);
    				CustomDamage.setupTrueDamage(ev);
    				//boolean applieddmg = CustomDamage.ApplyDamage(dmgdealt, null, (LivingEntity)ev.getEntity(), null, ev.getCause().name(), CustomDamage.TRUEDMG);
    				if (!CustomDamage.InvulnerableCheck(null, dmgdealt, (LivingEntity)ev.getEntity(), null, ev.getCause().name(), CustomDamage.TRUEDMG)) {
    					boolean applieddmg=true;
						dmgdealt = CustomDamage.CalculateDamage(dmgdealt, null, (LivingEntity)ev.getEntity(), null, ev.getCause().name(), CustomDamage.TRUEDMG);
						//TwosideKeeper.log("Damage: "+dmgdealt+" Event cause: "+ev.getCause(), 0);
	    				if (ev.getCause()==DamageCause.FALL) {
	    					if (ev.getEntity() instanceof Player) {
	    						Player p = (Player)ev.getEntity();
	    						if (PlayerMode.getPlayerMode(p)==PlayerMode.BARBARIAN) {
	    							createFallShockwave(p,dmgdealt);
	    							GenericFunctions.DealDamageToNearbyMobs(p.getLocation(), dmgdealt*3, Math.min(16,dmgdealt), true, Math.min(2.0,dmgdealt/8), p, p.getEquipment().getItemInMainHand(), false, "Leaping Strike");
	    						}
	    					}
	    					dmgdealt *= GenericFunctions.CalculateFallResistance((LivingEntity)ev.getEntity());
	    				}
						//TwosideKeeper.log("Damage: "+dmgdealt, 0);
						dmgdealt = CustomDamage.subtractAbsorptionHearts(dmgdealt, (LivingEntity)ev.getEntity());
						dmgdealt = CustomDamage.applyOnHitEffects(dmgdealt,null,(LivingEntity)ev.getEntity(),null ,ev.getCause().name(),CustomDamage.TRUEDMG);
						//TwosideKeeper.log("Damage: "+dmgdealt, 0);
	    				/*if ((ev.getCause()==DamageCause.CONTACT ||
	    						ev.getCause()==DamageCause.LIGHTNING ||
	    						ev.getCause()==DamageCause.FALLING_BLOCK ||
	    						ev.getCause()==DamageCause.BLOCK_EXPLOSION ||
	    						ev.getCause()==DamageCause.FIRE ||
	    						ev.getCause()==DamageCause.LAVA) &&
	    						(ev.getEntity() instanceof Player) &&
	    						applieddmg) {
	    					Player p = (Player)ev.getEntity(); 
	    				};*/
						CustomDamage.setupTrueDamage(ev);
						ev.setDamage(DamageModifier.BASE, dmgdealt);
						log("Damage from this event is "+dmgdealt,4);

						EntityDamagedEvent event = new EntityDamagedEvent((LivingEntity)ev.getEntity(),null,dmgdealt,ev.getCause().name(),CustomDamage.TRUEDMG);
						Bukkit.getPluginManager().callEvent(event);
						if (event.isCancelled()) {
	    		            dmgdealt=0;
	    		            ev.setDamage(DamageModifier.BASE,0d);
							ev.setCancelled(true);
							return;
						} else {
						  if (dmgdealt < 1) {
		    		            ev.setDamage(DamageModifier.BASE,dmgdealt);
		    		        } else {
		    		            ev.setDamage(DamageModifier.BASE,1d);
		    		            ((LivingEntity)ev.getEntity()).setHealth(Math.max(((LivingEntity)ev.getEntity()).getHealth() - (dmgdealt - 1d), 0.5));
		    		        }
						}
					} else {
						ev.setCancelled(true);
					}
    				
    				//ev.setCancelled(true);
    				
    			} else 
    			{
    				double dmgdealt = ev.getDamage(DamageModifier.BASE);
					EntityDamagedEvent event = new EntityDamagedEvent((LivingEntity)ev.getEntity(),null,dmgdealt,ev.getCause().name(),CustomDamage.TRUEDMG);
					Bukkit.getPluginManager().callEvent(event);
					if (event.isCancelled()) {
    		            dmgdealt=0;
    		            ev.setDamage(DamageModifier.BASE,0d);
						ev.setCancelled(true);
						return;
					} else {
					  if (dmgdealt < 1) {
	    		            ev.setDamage(DamageModifier.BASE,dmgdealt);
	    		        } else {
	    		            ev.setDamage(DamageModifier.BASE,1d);
	    		            ((LivingEntity)ev.getEntity()).setHealth(Math.max(((LivingEntity)ev.getEntity()).getHealth() - (dmgdealt - 1d), 0.5));
	    		        }
					}
    			}
    		}
    	}
    }
    
	private void createFallShockwave(Player p, double dmgdealt) {
		AreaEffectCloud aec = (AreaEffectCloud)p.getWorld().spawnEntity(p.getLocation(), EntityType.AREA_EFFECT_CLOUD);
		//PARTICLE.BARRIER looks pretty good.
		aec.setDuration(20);
		aec.setParticle(Particle.DRIP_LAVA);
		aec.setRadius(Math.min(16,(float)dmgdealt));
		aec.setColor(Color.RED);
	}

	public static void outputArmorDurability(Player p) {
		outputArmorDurability(p,"");
	}
	
	public static void outputArmorDurability(Player p, String optional_string) {
		StringBuilder armorstring = new StringBuilder();
		for (ItemStack armor : GenericFunctions.getArmor(p)) {
			if (armor!=null) {
				armorstring.append(armor.getDurability()+" ");
			}
		}
		log(optional_string+"Durability is "+armorstring,0);
	}

	public static double getMaxThornsLevelOnEquipment(Entity damager) {
		int maxthornslevel = 0;
		LivingEntity shooter = CustomDamage.getDamagerEntity(damager);
		if (shooter instanceof LivingEntity) {
			ItemStack[] equipment = GenericFunctions.getEquipment(shooter,true);
			for (int i=0;i<equipment.length;i++) {
				if (equipment[i]!=null && equipment[i].getEnchantmentLevel(Enchantment.THORNS)>maxthornslevel) {
					maxthornslevel=equipment[i].getEnchantmentLevel(Enchantment.THORNS);
				}
			}
			return maxthornslevel;
		}
		log("[ERROR] Thorns level could not be found even though getMaxThornsLevelOnEquipment() was called!",0);
		return 0.01; //This should not happen unless something seriously is bugged with thorns.
	}

	@EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void entityHitEvent(EntityDamageByEntityEvent ev) {
		//DisplayPlayerDurability(ev.getEntity());
		if (ev.getEntity() instanceof Player) {
			Player p = (Player)ev.getEntity();
			if (!p.isOnline()) {
				ev.setCancelled(true);
				return;
			}
		}
    	if (ev.getEntity().isDead()) {ev.setCancelled(true); return;}
		if (ev.getEntity() instanceof LivingEntity) {
			if (ev.getCause()==DamageCause.THORNS) { //Custom thorns damage formula.
				double dmgdealt=0;
				dmgdealt=getMaxThornsLevelOnEquipment(ev.getDamager());
				if (ev.getEntity() instanceof Player) {
					if (PlayerMode.isRanger((Player)ev.getEntity())) {
		    			dmgdealt=0.25;
					} else
					if (PlayerMode.isSlayer((Player)ev.getEntity()) &&
							ItemSet.GetSetCount(GenericFunctions.getHotbarItems((Player)ev.getEntity()), ItemSet.LORASYS, (Player)ev.getEntity())>0) {
						dmgdealt=0.0;
					}
				}
				SoundUtils.playGlobalSound(ev.getEntity().getLocation(), Sound.ENCHANT_THORNS_HIT, 1.0f, 1.0f);
				CustomDamage.setupTrueDamage(ev);
				if (ev.getDamager() instanceof Player) {
					Player p = (Player)ev.getDamager();
					PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
					int piecesWthorns=getNumberofPiecesWithThorns(p);
					GenericFunctions.spawnXP(p.getLocation(), (int)(1.85d*piecesWthorns));
					//Spill some XP out of the damaged target.
					dmgdealt += pd.thorns_amt;
					pd.thorns_amt=0;
				}
				CustomDamage.ApplyDamage(dmgdealt, ev.getDamager(), (LivingEntity)ev.getEntity(), null, ev.getCause().name(), CustomDamage.TRUEDMG);
				ev.setCancelled(true);
			} else
			{
				if (ev.getDamage(DamageModifier.BASE)>=CUSTOM_DAMAGE_IDENTIFIER) {
					log("BASE damage: "+ev.getDamage(DamageModifier.BASE)+"-"+CUSTOM_DAMAGE_IDENTIFIER,5);
					double dmgdealt = ev.getDamage(DamageModifier.BASE)-CUSTOM_DAMAGE_IDENTIFIER;
					CustomDamage.setupTrueDamage(ev);
					ev.setDamage(DamageModifier.BASE, dmgdealt);
					log("BASE damage: "+ev.getDamage(DamageModifier.BASE),5);
					//Only a player can deal custom damage.
					LivingEntity l = CustomDamage.getDamagerEntity(ev.getDamager());
					if (l instanceof Player) {
						Player shooter = (Player)l;
						PlayerStructure pd = PlayerStructure.GetPlayerStructure(shooter);
						int flags = pd.lasthitproperties;
						pd.target=(LivingEntity)ev.getEntity();
						pd.damagedealt=dmgdealt;
						displayTitle(shooter,pd,flags);
					}
				} else {
					ItemStack weapon = null;
					if (CustomDamage.getDamagerEntity(ev.getDamager()) instanceof Player) {
						weapon = ((Player)(CustomDamage.getDamagerEntity(ev.getDamager()))).getEquipment().getItemInMainHand();
					}
					
					if (CustomDamage.getDamagerEntity(ev.getDamager()) instanceof Player) {
						Player p = (Player)CustomDamage.getDamagerEntity(ev.getDamager());
						PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
						if (PlayerMode.isDefender(p) && p.isSneaking() && ItemSet.HasSetBonusBasedOnSetBonusCount(GenericFunctions.getEquipment(p), p, ItemSet.SONGSTEEL,5) && pd.vendetta_amt>0.0) { //Deal Vendetta damage instead.
							SoundUtils.playLocalSound(p, Sound.BLOCK_GLASS_BREAK, 1.0f, 0.5f);
							GenericFunctions.removeNoDamageTick((LivingEntity)ev.getEntity(), ev.getDamager());
							CustomDamage.ApplyDamage(pd.vendetta_amt, ev.getDamager(), (LivingEntity)ev.getEntity(), null, "Vendetta");
							pd.vendetta_amt=0.0;
							GenericFunctions.sendActionBarMessage(p, ChatColor.YELLOW+"Vendetta: "+ChatColor.GREEN+Math.round(pd.vendetta_amt)+" dmg stored",true);
							ev.setCancelled(true);
						} else {
							CustomDamage.ApplyDamage(0, ev.getDamager(), (LivingEntity)ev.getEntity(), weapon, null);
							if (ev.getDamager() instanceof Projectile) {
								Projectile proj = (Projectile)ev.getDamager();
								proj.remove();
							}
							ev.setCancelled(true);
						}
						//Handle this manually if it's a player.
					} else {
						if (ev.getEntity() instanceof Player) { //Check for custom trigger events for elites first.
							Player p = (Player)ev.getEntity();
							CustomDamage.triggerEliteEvent(p,ev.getDamager());
						}
						if (!CustomDamage.InvulnerableCheck(ev.getDamager(), (LivingEntity)ev.getEntity())) {
							double dmgdealt = CustomDamage.CalculateDamage(0, ev.getDamager(), (LivingEntity)ev.getEntity(), null, null, CustomDamage.NONE);
							dmgdealt = CustomDamage.subtractAbsorptionHearts(dmgdealt, (LivingEntity)ev.getEntity());
							dmgdealt = CustomDamage.applyOnHitEffects(dmgdealt,ev.getDamager(),(LivingEntity)ev.getEntity(),weapon,null,CustomDamage.NONE);
							if (ev.getDamager()!=null) {
								TwosideKeeper.logHealth((LivingEntity)ev.getEntity(),((LivingEntity)ev.getEntity()).getHealth(),dmgdealt,ev.getDamager());
							}
							CustomDamage.setupTrueDamage(ev);
							ev.setDamage(DamageModifier.BASE, dmgdealt);

							EntityDamagedEvent event = new EntityDamagedEvent((LivingEntity)ev.getEntity(),ev.getDamager(),dmgdealt,ev.getCause().name(),CustomDamage.NONE);
							Bukkit.getPluginManager().callEvent(event);
							if (event.isCancelled()) {
								dmgdealt=0;
		    		            ev.setDamage(DamageModifier.BASE,0d);
								ev.setCancelled(true);
								return;
							} else {
								if (dmgdealt < 1) {
				    		        ev.setDamage(DamageModifier.BASE,dmgdealt);
			    		        } else {
			    		            ev.setDamage(DamageModifier.BASE,1d);
			    		            ((LivingEntity)ev.getEntity()).setHealth(Math.max(((LivingEntity)ev.getEntity()).getHealth() - (dmgdealt - 1d), 0.5));
			    		        }
							}
							//DisplayPlayerDurability(ev.getEntity());
						} else {
							ev.setCancelled(true);
						}
					}
				}
			}
		}
    }

	private int getNumberofPiecesWithThorns(Player p) {
		int pieces=0;
		for (ItemStack item : GenericFunctions.getArmor(p)) {
			if (item!=null && item.containsEnchantment(Enchantment.THORNS)) {
				pieces++;
			}
		}
		return pieces;
	}
	private void DisplayPlayerDurability(Entity ent) {
		if (ent instanceof Player) {
			StringBuilder armorstring = new StringBuilder("Armor Durability: {");
			Player p = (Player)ent;
			
			boolean first=false;
			for (ItemStack armor : GenericFunctions.getArmor(p)) {
				if (armor!=null) {
					armorstring.append((first)?"":","+armor.getDurability());
				} else {
					armorstring.append((first)?"":","+"X");
				}
			}
			armorstring.append("}");
			TwosideKeeper.log(armorstring.toString(), 1);
		}
	}
	
	@EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void onEliteTeleport(EntityPortalEvent ev) {
    	if (ev.getEntity() instanceof Monster && MonsterController.getMonsterDifficulty((Monster)ev.getEntity()).equals(MonsterDifficulty.ELITE)) {
    		ev.setTo(ev.getFrom());
    		ev.setCancelled(true);
    	}
    }

	@EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void HiImAPigZombie(EntityPortalExitEvent ev) {
		if (ev.getEntity() instanceof Monster) {
			Monster m = (Monster)ev.getEntity();
			updateMonsterFlags(m);
		}
	}
    
    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void onEndermanTeleport(EntityTeleportEvent ev) {
    	
    	if (GenericFunctions.isSuppressed(ev.getEntity())) {
    		ev.setTo(ev.getFrom());
    		ev.setCancelled(true);
    		return;
    	}
    	
    	if (ev.getEntity() instanceof Monster && MonsterController.getMonsterDifficulty((Monster)ev.getEntity()).equals(MonsterDifficulty.ELITE)) {
    		ev.setTo(ev.getFrom());
    		ev.setCancelled(true);
    		return;
    	}
    
    	if (ev.getEntity().isDead()) {
    		ev.setTo(ev.getFrom());
    		ev.setCancelled(true);
    		return;
    	}
    	
    	if (ev.getEntityType()==EntityType.ENDERMAN) {
    		//There is a small chance to drop a Mysterious Essence.
    		if (/*Math.random()<=0.0625*ARTIFACT_RARITY &&*/ ((Monster)ev.getEntity()).getTarget()==null &&
    				((!livingentitydata.containsKey(ev.getEntity().getUniqueId())) ||
    						livingentitydata.get(ev.getEntity().getUniqueId()).GetTarget()==null)) { //We won't drop it when they are targeting a player, only when they are doing their own thing.
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
    			if (PlayerMode.isRanger(p)) {
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
		
		if (ev.getEntity() instanceof LivingEntity) {
			LivingEntity le = (LivingEntity)ev.getEntity();
			if (!le.hasAI()) {
				ev.setCancelled(true);
				return;
			}
		}
		
		if (GenericFunctions.isSuppressed(ev.getEntity())) {
			ev.setCancelled(true);
    		return;
		}
		
    	if (EntityUtils.PreventEnderCrystalDestruction(ev.getEntity())) {
    		ev.setCancelled(true);
    		return;
    	}
		
    	if (ev.getEntity() instanceof Creeper) {
    		log("This is a creeper.",5);
    		final Creeper c = (Creeper)ev.getEntity();
    		if (c.getCustomName()!=null) {
    			log("Custom name is "+c.getCustomName(),4); 
    			if (c.getCustomName().contains("Dangerous")) {
    				log("Preparing to explode.",5);
    	    		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
    	    			public void run() {
	    					GenericFunctions.DealExplosionDamageToEntities(c.getLocation(), 16, 4);
	    					aPlugin.API.sendSoundlessExplosion(c.getLocation(), 4.0f);
	    					SoundUtils.playGlobalSound(c.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f);
    	    			}}
    	    		,10); 
    			} else 
    			if (c.getCustomName().contains("Deadly")) {
    				log("Preparing to explode.",5);
    				SoundUtils.playGlobalSound(c.getLocation(), Sound.ENTITY_CREEPER_PRIMED, 1.0f, 1.0f);
    	    		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
    	    			public void run() {
	    					GenericFunctions.DealExplosionDamageToEntities(c.getLocation(), 32, 3);
	    					aPlugin.API.sendSoundlessExplosion(c.getLocation(), 6.0f);
	    					SoundUtils.playGlobalSound(c.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f);
    	    				GenericFunctions.RandomlyCreateFire(c.getLocation(),3);
    	    			}}
    	    		,10);
    			} else 
    			if (c.getCustomName().contains("Hellfire")) {
    				log("Preparing to explode.",5);
    				SoundUtils.playGlobalSound(c.getLocation(), Sound.ENTITY_CREEPER_PRIMED, 1.0f, 1.0f);
    				Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
    	    			public void run() {
	    					GenericFunctions.DealExplosionDamageToEntities(c.getLocation(), 64, 4);
	    					aPlugin.API.sendSoundlessExplosion(c.getLocation(), 8.0f);
	    					SoundUtils.playGlobalSound(c.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f);
    	    				//c.getLocation().getWorld().createExplosion(c.getLocation().getX(),c.getLocation().getY(),c.getLocation().getZ(),8.0f,false,false);
    	    				GenericFunctions.RandomlyCreateFire(c.getLocation(),4);
    	    			}}
    	    		,30);
    			}
    		}
    	}
    }

    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void announcePluginUpdateEvent(AnnounceUpdateEvent ev) {  
    	aPlugin.API.discordSendRawItalicized(ev.getAnnouncementMessage());
    } 
    
    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void expEvent(PlayerExpChangeEvent ev) {  
    	double val = Math.random();
    	Player p = ev.getPlayer();
    	log("ExpChange event: "+val,5);
    	int amt = ev.getAmount();
    	int testamt = amt;
    	if (amt>500) {
    		testamt=500;
    	}
    	ev.setAmount((int)(ev.getAmount()+(ev.getAmount()*(ItemSet.GetTotalBaseAmount(GenericFunctions.getHotbarItems(ev.getPlayer()), ev.getPlayer(), ItemSet.ALUSTINE)/100d))));

		if (ItemSet.HasSetBonusBasedOnSetBonusCount(GenericFunctions.getHotbarItems(p), p, ItemSet.ALUSTINE, 5)) {
			if (Math.random()<=Math.min((ItemSet.TotalBaseAmountBasedOnSetBonusCount(GenericFunctions.getHotbarItems(p), p, ItemSet.ALUSTINE, 5, 4)/20d),1)) {
				PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
				if (PlayerMode.getPlayerMode(p)==PlayerMode.SLAYER) {
					if (pd.slayermodehp+2<p.getMaxHealth()) {
						pd.slayermodehp+=2;
						p.setHealth(pd.slayermodehp);
					} else {
						pd.slayermodehp=p.getMaxHealth();
						p.setHealth(pd.slayermodehp);
					}
				} else {
					if (p.getHealth()+2<p.getMaxHealth()) {
						p.setHealth(p.getHealth()+2);
					} else {
						p.setHealth(p.getMaxHealth());
					}
				}
			}
		}
    	if (val<=((double)testamt/(double)65)*(4.1666666666666666666666666666667e-4)*ARTIFACT_RARITY) {
    		Item it = ev.getPlayer().getWorld().dropItemNaturally(ev.getPlayer().getLocation(), Artifact.createArtifactItem(ArtifactItem.MALLEABLE_BASE));
    		it.setPickupDelay(0);
    		it.setInvulnerable(true);
    		ev.getPlayer().sendMessage(ChatColor.LIGHT_PURPLE+"A strange item has appeared nearby.");
    		GenericFunctions.logToFile("["+getServerTickTime()+"] A new Artifact Base was generated.\n");
    		log("A new artifact base was generated.",1);
    	}
    }
    
    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void entityTargetEvent(EntityTargetLivingEntityEvent ev) {
		if ((ev.getEntity() instanceof Monster)) {
			log("In here 1",5);
			Monster m = (Monster)ev.getEntity();
			
			if (ev.getTarget() instanceof Monster &&
					m.getTarget() instanceof Player) {
				ev.setCancelled(true); //Monsters will not target other Monsters if they are already targeting a player.
				return;
			}
			
			if (ev.getTarget() instanceof Wither) {
				ev.setCancelled(true);
				return; //Monsters will not target the Wither, even with friendly fire.
			}
			
    		if (m.hasPotionEffect(PotionEffectType.GLOWING)) {
    			ev.setCancelled(true);
    			return;
    		}
			LivingEntityStructure ms = LivingEntityStructure.getLivingEntityStructure(m);
			if (ms.getElite()) {
				log("In here 2",5);
				EliteMonster em = null;
				for (int i=0;i<elitemonsters.size();i++) {
					if (elitemonsters.get(i).m.equals(ev.getEntity())) {
						em = elitemonsters.get(i);
						break;
					}
				}
				if (em!=null && em.targetlist.size()==0) {
					if (em.targetlist.size()==0 && em.participantlist.size()==0) {
						TwosideKeeper.log("Cancel", 5);
						ev.setCancelled(true);
						ev.setTarget(null);
						return;
					}
					if ((ev.getTarget() instanceof Player) && !em.targetlist.contains((Player)ev.getTarget())) {
						Player p = (Player)ev.getTarget();
						PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
						if (pd.lastdeath+EliteMonster.WAIT_TIME<=TwosideKeeper.getServerTickTime() && !CustomDamage.isInIframe(p)) {
							em.targetlist.add((Player)ev.getTarget());
							m.setTarget(ev.getTarget());
						} else {
							log("This should trigger",5);
							em.randomlyTeleport();
							em.randomlyTeleport();
							em.randomlyTeleport();
							em.myspawn=m.getLocation();
							m.setTarget(null);
							em.targetlist.remove((Player)ev.getTarget());
							ev.setCancelled(true);
						}
					}
				} else {
					if (ev.getReason()!=TargetReason.CUSTOM &&
							ev.getReason()!=TargetReason.UNKNOWN) {
						ev.setCancelled(true);
						log("Unknown Targeting reason occurred for "+GenericFunctions.GetEntityDisplayName(m)+". Targeting: "+GenericFunctions.GetEntityDisplayName(m),5);
					}
				}
			} else {
				log("This monster is "+MonsterController.getMonsterDifficulty(m).name(),5);
				if (MonsterController.getMonsterDifficulty(m)==MonsterDifficulty.ELITE) {
					EliteMonster em = GenericFunctions.getProperEliteMonster(m);
					ms.SetElite(true);
					elitemonsters.add(em);
					ev.setCancelled(true);
					ev.setTarget(null);
				}
			}
		}
    	if (ev.getEntity() instanceof LivingEntity &&
    			ev.getReason()==TargetReason.PIG_ZOMBIE_TARGET) {
    		LivingEntity l = (LivingEntity)ev.getEntity();
    		if (l.hasPotionEffect(PotionEffectType.GLOWING)) {
    			if (livingentitydata.containsKey(l.getUniqueId())) {
    				ev.setTarget(livingentitydata.get(l.getUniqueId()).target);
    			}
    		}
    	}
    	if (ev.getTarget() instanceof Player &&
    			ev.getEntity() instanceof Monster) {
    		Player p = (Player)ev.getTarget();
    		Monster m = (Monster)ev.getEntity();
    		if (GenericFunctions.hasStealth(p) &&
    				m.getTarget()==null) {
    			ev.setCancelled(true);
    		}
    	}
    	if (ev.getTarget() instanceof Player &&
    			ev.getEntity() instanceof LivingEntity) {
    		CustomDamage.addToCustomStructures((LivingEntity)ev.getEntity());
    		CustomMonster cm = CustomMonster.getCustomMonster((LivingEntity)ev.getEntity());
    		if (cm!=null && cm instanceof HellfireGhast) {
    			HellfireGhast hg = (HellfireGhast)cm;
    			hg.setTarget((Player)ev.getTarget());
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

    @EventHandler
    public void witherBlockEatingCanceller(EntityChangeBlockEvent event)
    {
        EntityType entityType = event.getEntity().getType();
        if (entityType == EntityType.WITHER) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void monsterDeathEvent(final EntityDeathEvent ev) {
    	log("Has died.",5);
    	if (livingentitydata.containsKey(ev.getEntity().getUniqueId())){ev.setDroppedExp(ev.getDroppedExp()+5);}
    	if (ev.getEntity() instanceof Snowman) {
    		Snowman snowy = (Snowman)ev.getEntity();
    		if (snowy.getCustomName()!=null && ChatColor.stripColor(snowy.getCustomName()).equalsIgnoreCase(HuntingForSnowman)) {
        		if (livingentitydata.containsKey(snowy.getUniqueId())) {
        			LivingEntityStructure led = livingentitydata.get(snowy.getUniqueId());
        			if (led.GetTarget() instanceof Player) {
        				Player p = (Player)led.GetTarget();
						aPlugin.API.discordSendRaw(p.getName()+" has claimed the bounty of **"+TwosideKeeper.HuntingForSnowman+"** earning 2 Tokens!");
						Bukkit.broadcastMessage(ChatColor.YELLOW+p.getName()+ChatColor.RESET+" has claimed the bounty of "+ChatColor.BOLD+TwosideKeeper.HuntingForSnowman+ChatColor.RESET+" earning 2 Tokens!");
						Bukkit.broadcastMessage(ChatColor.AQUA+"   All other players have earned 1 Token!");
						GenericFunctions.giveItem(p, Christmas.getChristmasEventToken());
						for (Player pl : Bukkit.getOnlinePlayers()) {
							GenericFunctions.giveItem(pl, Christmas.getChristmasEventToken());
						}
        			}
        		}
    		}
    		if (snowy.getCustomName()!=null && SnowmanHuntList.contains(ChatColor.stripColor(snowy.getCustomName()))) {
    			SnowmanHuntList.remove(ChatColor.stripColor(snowy.getCustomName()));
    		}
    	}
    	if (ev.getEntity() instanceof Bat) {
    		//Drop an essence.
    		if (Math.random()<=0.3) {
    			//Rarely drop a lost essence.
    			ev.getEntity().getLocation().getWorld().dropItemNaturally(ev.getEntity().getLocation(), Artifact.createArtifactItem(ArtifactItem.LOST_ESSENCE));
    		}
			ev.getEntity().getLocation().getWorld().dropItemNaturally(ev.getEntity().getLocation(), Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE));
    	}
    	if (ev.getEntity() instanceof LivingEntity) {
    		List<ItemStack> droplist = ev.getDrops();
    		LivingEntity m = (LivingEntity)ev.getEntity();
    		
    		double dropmult = 0.0d;
    		boolean isBoss=false;
    		boolean isElite=false;
    		boolean killedByPlayer = false;
    		final Location deathloc = m.getLocation();
    		LivingEntityStructure ms = null;
    		if (livingentitydata.containsKey(m.getUniqueId())) {
    			ms = (LivingEntityStructure)livingentitydata.get(m.getUniqueId());
    			if (ms.hasOriginalName()) {
    				m.setCustomName(ms.getOriginalName());
    			}
    		}
    		
			if (ms!=null && (ms.GetTarget() instanceof Player)) {
				if ((m instanceof Slime) ||
						(m instanceof MagmaCube)) {
					if (m instanceof Slime) {
						Slime ss = (Slime)m;
						if (ss.getSize()>=4) {
							habitat_data.addKillToLocation(m);
						}
					} else {
						MagmaCube mm = (MagmaCube)m;
						if (mm.getSize()>=4) {
							habitat_data.addKillToLocation(m);
						}
					}
				} else {
					habitat_data.addKillToLocation(m);
				}
				habitat_data.startinglocs.remove(m.getUniqueId());
    			log("Killed by a player.",5);
    			killedByPlayer = true;
				Player p = (Player)ms.GetTarget();
	    		AwardDeathAchievements(p,ev.getEntity());
				if (p!=null) {
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
		        	
		        	if (PlayerMode.isRanger(p) &&
		        			GenericFunctions.getBowMode(p)==BowMode.CLOSE) {
		        		pd.fulldodge=true;
		        	}
		        	
					dropmult+=pd.partybonus*0.33; //Party bonus increases drop rate by 33% per party member.
					ItemStack item = p.getEquipment().getItemInMainHand();
					if (item!=null &&
							item.getType()!=Material.AIR &&
							GenericFunctions.isWeapon(item)) {
						log("Adding "+(item.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS)*0.1)+"to dropmult for Looting.",5);
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
			
    		if (m instanceof LivingEntity) {
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
				
				if (isBoss && Math.random()<=0.5) {
					LivingEntityDifficulty diff = MonsterController.getLivingEntityDifficulty(m);
					switch (diff) {
					case DANGEROUS:
						droplist.add(Artifact.createArtifactItem(ArtifactItem.ANCIENT_CORE));
						break;
					case DEADLY:
						droplist.add(Artifact.createArtifactItem(ArtifactItem.LOST_CORE));
						break;
					case ELITE:
						droplist.add(Artifact.createArtifactItem(ArtifactItem.DIVINE_CORE));
						break;
					case END:
						droplist.add(Artifact.createArtifactItem(ArtifactItem.DIVINE_CORE));
						break;
					case HELLFIRE:
						droplist.add(Artifact.createArtifactItem(ArtifactItem.DIVINE_CORE));
						break;
					default:
						droplist.add(Artifact.createArtifactItem(ArtifactItem.ARTIFACT_CORE));
					}
				}
    		}
    		
			if (killedByPlayer) {
				//Get the player that killed the monster.
				int luckmult = 0;
				int unluckmult = 0;
    			ms = (LivingEntityStructure)livingentitydata.get(m.getUniqueId());
				Player p = (Player)ms.GetTarget();
				
				boolean isRanger=PlayerMode.isRanger(p);
				boolean isSlayer=PlayerMode.isSlayer(p);
				boolean isBarbarian=PlayerMode.isBarbarian(p);
				boolean isInNether=p.getWorld().getName().equalsIgnoreCase("world_nether");
				
				GenericFunctions.knockOffGreed(p);
				
				PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
				if (PlayerMode.getPlayerMode(p)==PlayerMode.SLAYER) {
					if (pd.lastassassinatetime+20>getServerTickTime()) { //Successful Assassination.
						pd.lastassassinatetime=getServerTickTime()-GenericFunctions.GetModifiedCooldown(TwosideKeeper.ASSASSINATE_COOLDOWN,p);
						ItemStack[] inv = p.getInventory().getContents();
						for (int i=0;i<9;i++) {
							if (inv[i]!=null && (inv[i].getType()!=Material.SKULL_ITEM || pd.lastlifesavertime+GenericFunctions.GetModifiedCooldown(TwosideKeeper.LIFESAVER_COOLDOWN,p)<TwosideKeeper.getServerTickTime())) {
								aPlugin.API.sendCooldownPacket(p, inv[i], 0);
							}
						}
						GenericFunctions.addStackingPotionEffect(p, PotionEffectType.SPEED, 10*20, 4);
						if (ItemSet.HasSetBonusBasedOnSetBonusCount(GenericFunctions.getHotbarItems(p), p, ItemSet.MOONSHADOW, 7)) {
							//Apply damage to everything around the player.
							//List<Monster> mobs = GenericFunctions.getNearbyMobs(m.getLocation(), 8);
							List<Monster> mobs = CustomDamage.trimNonMonsterEntities(m.getNearbyEntities(8, 8, 8));
							for (Monster m1 : mobs) {
								if (!m1.equals(m)) {
									pd.lastassassinatetime=0;
									CustomDamage.ApplyDamage(0,p,m1,p.getEquipment().getItemInMainHand(),"AoE Damage",CustomDamage.NOAOE);
									if (m1.isDead()) {
										GenericFunctions.addStackingPotionEffect(p, PotionEffectType.INCREASE_DAMAGE, 10*20, 39, 2);
										GenericFunctions.addStackingPotionEffect(p, PotionEffectType.SPEED, 10*20, 4);
									}
								}
							}
							GenericFunctions.addStackingPotionEffect(p, PotionEffectType.INCREASE_DAMAGE, 10*20, 39, 2);
						} else {
							GenericFunctions.addStackingPotionEffect(p, PotionEffectType.INCREASE_DAMAGE, 10*20, 9);
						}
						if (ItemSet.HasSetBonusBasedOnSetBonusCount(GenericFunctions.getHotbarItems(p), p, ItemSet.GLADOMAIN, 7)) {
							pd.slayermegahit=true;
						}
						GenericFunctions.applyStealth(p, false);
					} else { //Failed Assassination.
						if (ItemSet.HasSetBonusBasedOnSetBonusCount(GenericFunctions.getHotbarItems(p), p, ItemSet.WOLFSBANE, 2)) {
							pd.lastassassinatetime-=GenericFunctions.GetModifiedCooldown(TwosideKeeper.ASSASSINATE_COOLDOWN,p)*(ItemSet.TotalBaseAmountBasedOnSetBonusCount(GenericFunctions.getHotbarItems(p), p, ItemSet.WOLFSBANE, 2, 2)/100d);
							ItemStack[] inv = p.getInventory().getContents();
							for (int i=0;i<9;i++) {
								if (inv[i]!=null && (inv[i].getType()!=Material.SKULL_ITEM || pd.lastlifesavertime+GenericFunctions.GetModifiedCooldown(TwosideKeeper.LIFESAVER_COOLDOWN,p)<TwosideKeeper.getServerTickTime())) {
									aPlugin.API.sendCooldownPacket(p, inv[i], GenericFunctions.GetRemainingCooldownTime(p, pd.lastassassinatetime, TwosideKeeper.ASSASSINATE_COOLDOWN));
								}
							}
						}
					}
				}
				
				if (isSlayer) {
					int restore_amt = (ItemSet.GetSetCount(GenericFunctions.getHotbarItems(p), ItemSet.LORASYS, p)>0)?4:2;
					if (pd.slayermodehp+restore_amt<p.getMaxHealth()) {
						pd.slayermodehp+=restore_amt;
					} else {
						pd.slayermodehp = p.getMaxHealth();
					}
					p.setHealth(pd.slayermodehp);
				}
				if (isBarbarian) {
					if (pd.damagepool>0) {
						pd.damagepool/=4;
						GenericFunctions.sendActionBarMessage(p, "");
					}
				}
				if (isRanger) {
					switch (GenericFunctions.getBowMode(p)) {
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
					for (PotionEffect pe : p.getActivePotionEffects()) {
						if (pe.getType().equals(PotionEffectType.LUCK)) {
							luckmult = pe.getAmplifier()+1;
						} else
						if (pe.getType().equals(PotionEffectType.UNLUCK)) {
							unluckmult = pe.getAmplifier()+1;
						}
					}
				}
				
				if (m instanceof Wither) {
					GenericFunctions.spawnXP(m.getLocation().add(5,0,5), 25000);
					GenericFunctions.spawnXP(m.getLocation().add(-5,0,-5), 25000);
					GenericFunctions.spawnXP(m.getLocation().add(-5,0,5), 25000);
					GenericFunctions.spawnXP(m.getLocation().add(5,0,-5), 25000);
				   
					//Spawn 8 chests at different quadrants.
					
					AttemptToPlaceChest(m.getLocation(),-1,-1,-1,aPlugin.API.Chests.LOOT_CUSTOM_5);
					AttemptToPlaceChest(m.getLocation(),1,-1,-1,aPlugin.API.Chests.LOOT_CUSTOM_5);
					AttemptToPlaceChest(m.getLocation(),1,-1,1,aPlugin.API.Chests.LOOT_CUSTOM_5);
					AttemptToPlaceChest(m.getLocation(),-1,-1,1,aPlugin.API.Chests.LOOT_CUSTOM_5);
					AttemptToPlaceChest(m.getLocation(),-1,1,-1,aPlugin.API.Chests.LOOT_CUSTOM_5);
					AttemptToPlaceChest(m.getLocation(),-1,1,1,aPlugin.API.Chests.LOOT_CUSTOM_5);
					AttemptToPlaceChest(m.getLocation(),1,1,-1,aPlugin.API.Chests.LOOT_CUSTOM_5);
					AttemptToPlaceChest(m.getLocation(),1,1,1,aPlugin.API.Chests.LOOT_CUSTOM_5);
					
					for (UUID id : custommonsters.keySet()) { 
						if (id.equals(m.getUniqueId())) { 
							sig.plugin.TwosideKeeper.Monster.Wither w = (sig.plugin.TwosideKeeper.Monster.Wither)custommonsters.get(id);
							w.DisplaySuccessfulDPSReport();
							break;
						}
					}
				}
				
				if (isElite && m instanceof Monster) {
					dropmult+=50;
					EliteMonster em = GenericFunctions.getEliteMonster((Monster)m);
					//For each target, drop additional loot and exp.
					List<Player> participants = em.getParticipantList();
					StringBuilder participants_list = new StringBuilder();
					for (int i=0;i<participants.size();i++) {
						Player pl = participants.get(i);
						if (pl!=null && pl.isOnline()) {
							ExperienceOrb exp = GenericFunctions.spawnXP(pl.getLocation(), ev.getDroppedExp()*300);
							exp.setInvulnerable(true);  
							if (m instanceof Zombie) {
								Zombie z = (Zombie)m;
								if (z.isBaby()) {
									GenericFunctions.giveItem(pl,aPlugin.API.getChestItem(Chests.ELITE));
								}
							}
							GenericFunctions.giveItem(pl,aPlugin.API.getChestItem(Chests.ELITE));
							log("Dropping "+aPlugin.API.getChestItem(Chests.ELITE).toString(),2);
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
									participants_list.append(", "+pl.getName());
								}
							}
						} else {
							Item it = m.getWorld().dropItemNaturally(m.getLocation(), aPlugin.API.getChestItem(Chests.ELITE));
			                it.setInvulnerable(true);
			                it.setPickupDelay(0);
						}
					}
					Bukkit.getServer().broadcastMessage(ChatColor.GREEN+participants_list.toString()+ChatColor.WHITE+" "+(participants_list.length()==1?"has single-handedly taken down the ":"have successfully slain ")+GenericFunctions.getDisplayName(m)+ChatColor.WHITE+"!");
					aPlugin.API.discordSendRaw(ChatColor.GREEN+participants_list.toString()+ChatColor.WHITE+" "+(participants_list.length()==1?"has single-handedly taken down the ":"have successfully slain ")+"**"+GenericFunctions.getDisplayName(m)+ChatColor.WHITE+"**!");
					m.getWorld().spawnEntity(m.getLocation(), EntityType.LIGHTNING);
					m.getWorld().setStorm(true);
					m.getWorld().setWeatherDuration(20*60*15);
					em.removeAllHealthbars();

					Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
    					public void run() {
    						Bukkit.getServer().broadcastMessage(ChatColor.YELLOW+"DPS Breakdown:");
							Bukkit.getServer().broadcastMessage(em.generateDPSReport());
							aPlugin.API.discordSendRaw(ChatColor.YELLOW+"DPS Breakdown:"+"\n```\n"+em.generateDPSReport()+"\n```");
							em.Cleanup();
							elitemonsters.remove(em);
    					}},1);
					GenericFunctions.generateNewElite(null,""); 
				}
				
				if (isInNether) {
					dropmult = dropmult + 0.3;
				}
					
				dropmult = dropmult + (luckmult * 0.5) - (unluckmult * 0.5);
				
				if (CHRISTMASEVENT_ACTIVATED) {dropmult += dropmult * 0.5;}
				
				if (luckmult>0 || unluckmult>0) {
					log("Modified luck rate is now "+dropmult,3);
				}
				
				List<ItemStack> originaldroplist = new ArrayList<ItemStack>();
				for (int i=0;i<droplist.size();i++) {
					originaldroplist.add(droplist.get(i));
					droplist.remove(i);
					i--;
				}
				
				droplist.addAll(MonsterController.getLivingEntityDifficulty(ev.getEntity()).RandomizeDrops(dropmult, isBoss, isRanger, p, m));
	    		final List<ItemStack> drop = new ArrayList<ItemStack>(); 
	    		drop.addAll(droplist);
	    		
				
				int totalexp = 0;
				
	    		//Determine EXP amount and explosion type.
	    		switch (MonsterController.getLivingEntityDifficulty(m)) {
	    			case NORMAL:
						droplist.addAll(originaldroplist);
	    			break;
					case DANGEROUS:
						totalexp=ev.getDroppedExp()*4;
						droplist.addAll(originaldroplist);
						break;
					case DEADLY:
						SoundUtils.playGlobalSound(m.getLocation(), Sound.ENTITY_CREEPER_PRIMED, 1.0f, 1.0f);
						totalexp=ev.getDroppedExp()*8;
						ev.setDroppedExp((int)(totalexp*0.75));
						final LivingEntity mer = m;
						final int expdrop = totalexp;
						droplist.clear(); //Clear the drop list. We are going to delay the drops.
						droplist.addAll(originaldroplist);
						if (!GenericFunctions.isSuppressed(m)) {
							Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
		    					public void run() {
		    	    				if (!mer.getLocation().getWorld().getName().equalsIgnoreCase("world") || mer.getLocation().getBlockY()<48) {
		    	    					mer.getWorld().createExplosion(mer.getLocation().getBlockX(), mer.getLocation().getBlockY(), mer.getLocation().getBlockZ(), 1.5f, false, true);
		    	    					aPlugin.API.sendSoundlessExplosion(mer.getLocation(), 3.0f);
		    	    					GenericFunctions.DealExplosionDamageToEntities(mer.getLocation(), 20, 3);
		    	    				} else {
		    	    					mer.getWorld().createExplosion(mer.getLocation().getBlockX(), mer.getLocation().getBlockY(), mer.getLocation().getBlockZ(), 6.0f, false, false);
		    	    					GenericFunctions.DealExplosionDamageToEntities(mer.getLocation(), 20, 6);
		    	    				}
		    					}}
		    				,30);
						}
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
						SoundUtils.playGlobalSound(m.getLocation(), Sound.ENTITY_CREEPER_PRIMED, 1.0f, 1.0f);
						totalexp=ev.getDroppedExp()*20;
						ev.setDroppedExp((int)(totalexp*0.75));
						final LivingEntity mer1 = m;
						final int expdrop1 = totalexp; 
						droplist.clear(); //Clear the drop list. We are going to delay the drops.
						droplist.addAll(originaldroplist);
						if (!GenericFunctions.isSuppressed(m)) {
							Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
		    					public void run() {
		    	    				if (!mer1.getLocation().getWorld().getName().equalsIgnoreCase("world") || mer1.getLocation().getBlockY()<48) {
		    	    					mer1.getWorld().createExplosion(mer1.getLocation().getBlockX(), mer1.getLocation().getBlockY(), mer1.getLocation().getBlockZ(), 2.0f, false, true);
		    	    					aPlugin.API.sendSoundlessExplosion(mer1.getLocation(), 5.0f);
		    	    					GenericFunctions.DealExplosionDamageToEntities(mer1.getLocation(), 36, 5);
		        	    				GenericFunctions.RandomlyCreateFire(mer1.getLocation(),2);
		    	    				} else {
		    	    					mer1.getWorld().createExplosion(mer1.getLocation().getBlockX(), mer1.getLocation().getBlockY(), mer1.getLocation().getBlockZ(), 6.0f, false, false);
		    	    					GenericFunctions.DealExplosionDamageToEntities(mer1.getLocation(), 36, 6);
		        	    				GenericFunctions.RandomlyCreateFire(mer1.getLocation(),3);
		    	    				}
		    					}}
		    				,30);
						}
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
					case END:
						SoundUtils.playGlobalSound(m.getLocation(), Sound.ENTITY_CREEPER_PRIMED, 1.0f, 1.0f);
						totalexp=ev.getDroppedExp()*40;
						ev.setDroppedExp((int)(totalexp*0.75));
						final LivingEntity mer4 = m;
						final int expdrop4 = totalexp; 
						droplist.clear(); //Clear the drop list. We are going to delay the drops.
						droplist.addAll(originaldroplist);
						if (!GenericFunctions.isSuppressed(m)) {
							Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
		    					public void run() {
		    	    				if (!mer4.getLocation().getWorld().getName().equalsIgnoreCase("world") || mer4.getLocation().getBlockY()<48) {
		    	    					mer4.getWorld().createExplosion(mer4.getLocation().getBlockX(), mer4.getLocation().getBlockY(), mer4.getLocation().getBlockZ(), 2.0f, false, true);
		    	    					aPlugin.API.sendSoundlessExplosion(mer4.getLocation(), 5.0f);
		    	    					GenericFunctions.DealExplosionDamageToEntities(mer4.getLocation(), 150, 5);
		        	    				GenericFunctions.RandomlyCreateFire(mer4.getLocation(),2);
		    	    				} else {
		    	    					mer4.getWorld().createExplosion(mer4.getLocation().getBlockX(), mer4.getLocation().getBlockY(), mer4.getLocation().getBlockZ(), 6.0f, false, false);
		    	    					GenericFunctions.DealExplosionDamageToEntities(mer4.getLocation(), 150, 6);
		        	    				GenericFunctions.RandomlyCreateFire(mer4.getLocation(),3);
		    	    				}
		    					}}
		    				,30);
						}
						Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
	    					public void run() {
			    				for (int i=0;i<drop.size();i++) {
			    					Item it = deathloc.getWorld().dropItemNaturally(mer4.getLocation(), drop.get(i));
			    					it.setInvulnerable(true);
			    				}
			    				GenericFunctions.spawnXP(mer4.getLocation(), (int)(expdrop4*0.25));
	    					}}
	    				,50);
						break;
					case ELITE:
						totalexp=ev.getDroppedExp()*300;
						final LivingEntity mer2 = m;
	    				for (int i=0;i<originaldroplist.size();i++) {
	    					Item it = deathloc.getWorld().dropItemNaturally(mer2.getLocation(), originaldroplist.get(i));
	    					it.setInvulnerable(true);
	    				}
	    				for (int i=0;i<drop.size();i++) {
	    					if (drop.get(i)!=null) {
		    					Item it = deathloc.getWorld().dropItemNaturally(mer2.getLocation(), drop.get(i));
		    					it.setInvulnerable(true);
	    					}
	    				}
						break;
	    		}
	    		log("Drop list contains "+(droplist.size()+originaldroplist.size())+" elements.",5);
	    		log("  Drops "+"["+(drop.size()+originaldroplist.size())+"]: "+ChatColor.GOLD+ChatColor.stripColor(originaldroplist.toString())+ChatColor.WHITE+","+ChatColor.LIGHT_PURPLE+ChatColor.stripColor(drop.toString()),LOOT_DEBUG);
			}
			
			livingentitydata.remove(m.getUniqueId());
			chargezombies.remove(m.getUniqueId());
			custommonsters.remove(m.getUniqueId());
    	}
    }
    
    private void AttemptToPlaceChest(Location refloc, int i, int j, int k, Chests chest) {
		int tries=0;
		while (tries<50) {
			Block rand = refloc.getBlock().getRelative((int)(i*((Math.random()*4)+1)), (int)(j*((Math.random()*4)+1)), (int)(k*((Math.random()*4)+1)));
			if (GenericFunctions.isNaturalBlock(rand)) {
				chest.placeChestAt(rand);
				for (int x=-1;x<2;x++) {
					for (int y=-1;y<2;y++) {
						for (int z=-1;z<2;z++) {
							if (x!=0 || y!=0 || z!=0) {
								if (GenericFunctions.isNaturalBlock(rand.getRelative(x, y, z))) {
									rand.getRelative(x, y, z).setType(Material.AIR);
								}
							}
						}
					}
				}
				return;
			} else {
				tries++;
			}
		}
	}
	private Chest SpawnALootChest(int i, int j, int k) {
		// TODO Auto-generated method stub
		return null;
	}
	private void AwardDeathAchievements(Player p, LivingEntity entity) {
		if (p.hasAchievement(Achievement.BUILD_SWORD) && (entity instanceof Monster) && !p.hasAchievement(Achievement.KILL_ENEMY)) {
			p.awardAchievement(Achievement.KILL_ENEMY);
		}
		if (p.hasAchievement(Achievement.KILL_ENEMY) && (entity instanceof Skeleton)&& !p.hasAchievement(Achievement.SNIPE_SKELETON) && p.getEquipment().getItemInMainHand().getType()==Material.BOW && entity.getWorld().equals(p.getWorld()) && entity.getLocation().distanceSquared(p.getLocation())>=2500) {
			p.awardAchievement(Achievement.SNIPE_SKELETON);
		}
	}
	@EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void updateHealthbarRespawnEvent(PlayerRespawnEvent ev) {
    	final Player p = ev.getPlayer();
    	//ev.setRespawnLocation(new Location(Bukkit.getWorld("world"),Math.random()*2000-1000,72,Math.random()*2000-1000));
    	//TwosideKeeper.log("Player Location: "+p.getLocation(), 0);
    	PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
    	pd.deathloc_world=p.getWorld().getName();
    	pd.deathloc_x=p.getLocation().getX();
    	pd.deathloc_y=p.getLocation().getY();
    	pd.deathloc_z=p.getLocation().getZ();
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			public void run() {
				if (p!=null) {
					p.getScoreboard().getTeam(p.getName().toLowerCase()).setSuffix(createHealthbar(((p.getHealth())/p.getMaxHealth())*100,p));
					setPlayerMaxHealth(p);
					p.getScoreboard().getTeam(p.getName().toLowerCase()).setPrefix(GenericFunctions.PlayerModePrefix(p));
				}
			}}
		,5);

		if (Christmas.NoSweetCandyInInventory(p)) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
				@Override
				public void run() {
					//Look for a death structure for this player. If found, continue.
					if (DeathManager.getDeathStructure(p)!=null) {
						DeathManager.continueAction(p);
					}
					p.setVelocity(new Vector(0,0,0));
					GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.LEVITATION,Integer.MAX_VALUE,255,p);
					CustomDamage.setAbsorptionHearts(p, 0.0f);
					GenericFunctions.addIFrame(p, Integer.MAX_VALUE);
					PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			    	pd.lastdeath=getServerTickTime();
			    	log("Last death: "+pd.lastdeath, 2);
				}
			},1);
			Location newloc = ev.getRespawnLocation();
			newloc.setY(newloc.getWorld().getHighestBlockYAt(ev.getRespawnLocation())); 
			ev.setRespawnLocation(newloc.add(0,10,0));
		}
		
    	pd.lastdeath=getServerTickTime();
		pd.hasDied=false;
		pd.slayermodehp=10;
		pd.damagepool=0;
		pd.weaponcharges=0;
		//log("Block started on is "+ev.getRespawnLocation().getBlock(),2);
		//p.teleport(GenericFunctions.FindRandomFreeLocation(p.getLocation().add(0,1,0)));
		/*Location newloc = ev.getRespawnLocation();
		newloc.setY(newloc.getWorld().getHighestBlockYAt(ev.getRespawnLocation())); 
		ev.setRespawnLocation(newloc.add(0,10,0));*/
    }
    
    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void KickEvent(PlayerKickEvent ev) {
    	if (ev.getReason()==null || (!ev.getReason().contains("ADMINKICK") && !ev.getReason().contains("Kicked by an operator."))) {
    		log("Tried to kick "+ev.getPlayer().getName()+" for reason "+ev.getReason(),1);
    		ev.setCancelled(true);
    	}
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

    @EventHandler(priority=EventPriority.LOW)
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
    		ItemStack test = GenericFunctions.breakHardenedItem(item,p);
    		if (test!=null) {
    			//We have to give this player the item!
    			if (test.getAmount()==0) {
    				test.setAmount(1); //We can't give 0 of something.
    			}
    			//Scan the inventory for this item. If it already exists do not give one.
    			boolean foundone=false;
    			for (ItemStack items : p.getInventory().getContents()) {
    				if (test.isSimilar(items)) {
    					foundone=true;
    					TwosideKeeper.log("Found one of these in the player's inventory already. Do not allow this item to be duplicated!", 0);
    					break;
    				}
    			}
    			if (!foundone) {
    				GenericFunctions.giveItem(p, test);
    			}
    		}
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
			if (pd.highwinder && pd.target!=null && !pd.target.isDead() && pd.lasthighwinderhit+15<getServerTickTime()) {
				GenericFunctions.sendActionBarMessage(ev.getPlayer(), drawVelocityBar(pd.velocity,pd.highwinderdmg),true);
			}
    	}
    	if (ItemSet.HasSetBonusBasedOnSetBonusCount(GenericFunctions.getArmor(ev.getPlayer()), ev.getPlayer(), ItemSet.DANCER, 4)) {
	    	PlayerStructure pd = (PlayerStructure)playerdata.get(ev.getPlayer().getUniqueId());
	    	int sign1 = (int) Math.signum(ev.getFrom().getX()-ev.getTo().getX());
	    	int sign2 = (int) Math.signum(ev.getFrom().getZ()-ev.getTo().getZ());
	    	if (sign1!=pd.lastxsign &&
	    			sign2!=pd.lastzsign && !CustomDamage.isInIframe(ev.getPlayer())) {
				SoundUtils.playLocalSound(ev.getPlayer(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f);
				GenericFunctions.addIFrame(ev.getPlayer(), 20);
	    	}
	    	pd.lastxsign=sign1;
	    	pd.lastzsign=sign2;
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
    public void onBlockBreak(BlockBreakEvent ev) {
    	
    	TwosideSpleefGames.PassEvent(ev);
    	
    	Player p = ev.getPlayer();
    	PlayerStructure pd = (PlayerStructure)playerdata.get(p.getUniqueId()); 
    	if (p!=null) {
    		log(p.getName()+" has broken block "+GenericFunctions.UserFriendlyMaterialName(new ItemStack(ev.getBlock().getType())),3);
    		if (GenericFunctions.isTool(p.getEquipment().getItemInMainHand())) {
    			GenericFunctions.RemovePermEnchantmentChance(p.getEquipment().getItemInMainHand(), p);
    		}
    	}
    	
    	if (ev.getBlock().getType()==Material.CHEST ||
    			ev.getBlock().getType()==Material.TRAPPED_CHEST) {
    		Chest cc = (Chest)ev.getBlock().getState();
    		TwosideKeeper.notWorldShop.remove(InventoryUtils.getInventoryHash(cc.getInventory()));
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
	    			if (owner.equalsIgnoreCase(p.getName()) || p.isOp() || owner.equalsIgnoreCase("admin")) {
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
						return;
	    			} else {
	    				//They are not the owner! Do not allow this shop to be broken.
	    				p.sendMessage("This shop belongs to "+ChatColor.LIGHT_PURPLE+owner+ChatColor.WHITE+"! You cannot break others' shops!");
	    				ev.setCancelled(true);
	    				return;
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
	    				for (Entity e : WorldShop.getBlockShopSignAttachedTo(s).getWorld().getNearbyEntities(WorldShop.getBlockShopSignAttachedTo(s).getLocation().add(0.5,0,0.5), 0.3, 1, 0.3)) {
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
	    				return;
	    			} else {
	    				//They are not the owner! Do not allow this shop to be broken.
	    				p.sendMessage("This shop belongs to "+ChatColor.LIGHT_PURPLE+owner+ChatColor.WHITE+"! You cannot break others' shops!");
	    				ev.setCancelled(true);
	    				return;
	    			}
	    		}
    		}
    	} else {
    		//Make sure there's no world sign on this block.
    		if (WorldShop.hasShopSignAttached(ev.getBlock())) {
    			//Do not allow this. The shop signs have to be destroyed first!
				p.sendMessage("This block has shops on it! The shops must be destroyed before you can break this block!");
				ev.setCancelled(true);
				return;
    		}
    	} 
    	
    	if (ev.getBlock().getType()==Material.PACKED_ICE) {
    		for (TemporaryIce ti : temporary_ice_list) {
    			if (ev.getBlock().equals(ti.getBlock())) {
    				//Deal 200 Raw Damage.
    				if (ti.getTrappedEntity()!=null && (ti.getTrappedEntity() instanceof LivingEntity)) {
    					LivingEntity le = (LivingEntity)ti.getTrappedEntity();
	    				CustomDamage.ApplyDamage(200, p, le, null, "Ice Shatter", CustomDamage.IGNORE_DAMAGE_TICK);
	    				break;
    				}
    			}
    		}
    	}
    		
    }

    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void onaPluginPickupEvent(PlayerGainItemEvent ev) {
		TwosideKeeper.log("["+TwosideKeeper.getServerTickTime()+"] PlayerGainItemEvent fired w/ "+ev.getItemStack(), 1);
    	Player p = ev.getPlayer();
    	ItemStack newstack = InventoryUtils.AttemptToFillPartialSlotsFirst(p,ev.getItemStack());
    	if (newstack==null) {
			SoundUtils.playGlobalSound(ev.getPlayer().getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.6f, SoundUtils.DetermineItemPitch(ev.getItemStack()));
			ev.setCancelled(true);return;}
    	GenericFunctions.UpdateItemLore(newstack);
    	HandlePickupAchievements(ev.getPlayer(), newstack);
    	boolean handled = AutoEquipItem(newstack, p);
    	if (handled) {
        	ev.setCancelled(handled);	
        	return;
    	}
    	if (AutoConsumeItem(p,newstack)) {
    		SoundUtils.playGlobalSound(ev.getPlayer().getLocation(), Sound.ENTITY_GENERIC_EAT, 1.0f, 1.0f);
    		ev.setCancelled(true);
    		return;
    	}
    	if (GenericFunctions.isValidArrow(newstack) && ArrowQuiver.getArrowQuiverInPlayerInventory(p)!=null) {
    		ev.setCancelled(true);
			SoundUtils.playGlobalSound(ev.getPlayer().getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.6f, SoundUtils.DetermineItemPitch(newstack));
			AddToPlayerInventory(newstack, p);
			return;
    	}
    	if (newstack.getType().isBlock() && InventoryUtils.isCarryingVacuumCube(p)) {
    		//Try to insert it into the Vacuum cube.
    		ItemStack[] remaining = InventoryUtils.insertItemsInVacuumCube(p, newstack);
    		if (remaining.length==0) {
        		ev.setCancelled(true);
    			SoundUtils.playGlobalSound(ev.getPlayer().getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.6f, SoundUtils.DetermineItemPitch(newstack));
    			return;
    		} else {
    			newstack=remaining[0];
    		}
    	}
    	
    	if (InventoryUtils.isCarryingFilterCube(p)) {
    		//Try to insert it into the Filter cube.
    		ItemStack[] remaining = InventoryUtils.insertItemsInFilterCube(p, newstack);
    		if (remaining.length==0) {
        		ev.setCancelled(true);
    			SoundUtils.playGlobalSound(ev.getPlayer().getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.6f, SoundUtils.DetermineItemPitch(newstack));
    			return;
    		} else {
    			newstack=remaining[0];
    		}
    	}

    	ev.setCancelled(true);
    	ItemStack givenitem = newstack.clone();
		GenericFunctions.giveItem(p, givenitem);
		return;
    }
    
    @EventHandler(priority=EventPriority.LOWEST,ignoreCancelled = true)
    public void onArrowPickup(PlayerPickupArrowEvent ev) {
    	if (ev.getArrow() instanceof Arrow) {
    		Arrow a = (Arrow)ev.getArrow();
			Player p = ev.getPlayer();
    		ItemStack item = ev.getItem().getItemStack();
        	if (ev.getArrow().hasMetadata("INFINITEARROW")) { //Not allowed to be picked up, this was an infinite arrow.
        		TwosideKeeper.log("INFINITE PICKUP ARROW", 5);
        		ev.setCancelled(true);
        		return;
        	} else {
        		boolean specialarrow=false;
    			if (a.hasMetadata("EXPLODE_ARR")) {item=Recipes.getArrowFromMeta("EXPLODE_ARR"); specialarrow=true;} else
    			if (a.hasMetadata("TRAP_ARR")) {item=Recipes.getArrowFromMeta("TRAP_ARR"); specialarrow=true;} else
        		if (a.hasMetadata("PIERCING_ARR")) {item=Recipes.getArrowFromMeta("PIERCING_ARR"); specialarrow=true;} else
    			if (a.hasMetadata("POISON_ARR")) {item=Recipes.getArrowFromMeta("POISON_ARR"); specialarrow=true;} else 
    			if (a.hasMetadata("QUADRUPLE_DAMAGE_ARR")) {item=Recipes.getArrowFromMeta("QUADRUPLE_DAMAGE_ARR"); specialarrow=true;} else
    			if (a.hasMetadata("DOUBLE_DAMAGE_ARR")) {item=Recipes.getArrowFromMeta("DOUBLE_DAMAGE_ARR"); specialarrow=true;}
    			if (specialarrow) {
    				ev.getItem().remove();
    				SoundUtils.playGlobalSound(ev.getPlayer().getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.6f, SoundUtils.DetermineItemPitch(item));
    				ev.setCancelled(true);
    				AddToPlayerInventory(item, p);
    				//ev.getItem().setItemStack(item);
    				return;
    			}
				ItemStack collect = CustomItem.convertArrowEntityFromMeta(ev.getArrow());
				if (collect!=null) {
					ev.getItem().remove();
					SoundUtils.playGlobalSound(ev.getPlayer().getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.6f, SoundUtils.DetermineItemPitch(item));
    				AddToPlayerInventory(collect, p);
					ev.setCancelled(true);
					return;
				}
        	}
    	}
    }
    
	public void AddToPlayerInventory(ItemStack item, Player p) {
		ItemStack arrowquiver = ArrowQuiver.getArrowQuiverInPlayerInventory(p);
		if (arrowquiver==null) {
			attemptToStackInInventory(p,item);
		} else {
			ArrowQuiver.addContents(arrowquiver, item);
			ArrowQuiver.updateQuiverLore(arrowquiver);
		}
	}
    
    private void attemptToStackInInventory(Player p, ItemStack collect) {
    	GenericFunctions.giveItem(p, collect);
	}
	
	@EventHandler(priority=EventPriority.HIGH,ignoreCancelled = true)
    public void onItemPickup(PlayerPickupItemEvent ev) {
    	//Arrow quiver code goes here.
		//TwosideKeeper.log("["+TwosideKeeper.getServerTickTime()+"] PlayerPickupItemEvent fired w/ "+ev.getItem().getItemStack(), 1);
		if (ev.isCancelled()) {
			return;
		}
    	Player p = ev.getPlayer();
    	ItemStack newstack = InventoryUtils.AttemptToFillPartialSlotsFirst(p,ev.getItem().getItemStack());
    	if (newstack==null || newstack.getType()==Material.AIR) {
			SoundUtils.playGlobalSound(ev.getPlayer().getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.6f, SoundUtils.DetermineItemPitch(ev.getItem().getItemStack()));
    		ev.getItem().remove();ev.setCancelled(true);return;}
    	ev.getItem().setItemStack(newstack);
    	log("Pickup Metadata: "+ev.getItem().getItemStack().getItemMeta().toString(),5);
    	//GenericFunctions.updateSetItems(p.getInventory());
    	GenericFunctions.UpdateItemLore(ev.getItem().getItemStack());
    	/*//LEGACY CODE
    	if (!ev.isCancelled()) {
	    	if (ev.getItem().getItemStack().getType()==Material.ARROW &&
	    			playerHasArrowQuiver(p)) {
	    			int arrowquiver_slot = playerGetArrowQuiver(p);
	    			playerInsertArrowQuiver(p, arrowquiver_slot, ev.getItem().getItemStack().getAmount());
	    			log("Added "+ev.getItem().getItemStack().getAmount()+" arrow"+((ev.getItem().getItemStack().getAmount()==1)?"":"s")+" to quiver in slot "+arrowquiver_slot+". New amount: "+playerGetArrowQuiverAmt(p,arrowquiver_slot),4);
	    			//If we added it here, we destroy the item stack.
	    			p.sendMessage(ChatColor.DARK_GRAY+""+ev.getItem().getItemStack().getAmount()+" arrow"+((ev.getItem().getItemStack().getAmount()==1)?"":"s")+" "+((ev.getItem().getItemStack().getAmount()==1)?"was":"were")+" added to your arrow quiver. Arrow Count: "+ChatColor.GRAY+playerGetArrowQuiverAmt(p,arrowquiver_slot));
	    			ev.getPlayer().playSound(ev.getPlayer().getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.6f, 1.0f);
	    			ev.getItem().remove();
	    			ev.setCancelled(true);
	    	}
    	}*/
    	
    	HandlePickupAchievements(ev.getPlayer(), ev.getItem().getItemStack());
    	
    	boolean handled = AutoEquipItem(ev.getItem().getItemStack(), p);
    	if (handled) {
    		ev.getItem().remove();
        	ev.setCancelled(handled);	
        	return;
    	}
    	
    	if (AutoConsumeItem(p,ev.getItem().getItemStack())) {
    		SoundUtils.playGlobalSound(ev.getPlayer().getLocation(), Sound.ENTITY_GENERIC_EAT, 1.0f, 1.0f);
    		ev.getItem().remove();
    		ev.setCancelled(true);
    		return;
    	}
    	
    	if (ev.getItem().hasMetadata("INFINITEARROW")) { //Not allowed to be picked up, this was an infinite arrow.
    		TwosideKeeper.log("INFINITE PICKUP", 5);
    		ev.setCancelled(true);
    		return;
    	}
    	
    	if (GenericFunctions.isValidArrow(ev.getItem().getItemStack()) && ArrowQuiver.getArrowQuiverInPlayerInventory(p)!=null) {
    		ev.setCancelled(true);
			SoundUtils.playGlobalSound(ev.getPlayer().getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.6f, SoundUtils.DetermineItemPitch(ev.getItem().getItemStack()));
			ev.getItem().remove();
			AddToPlayerInventory(ev.getItem().getItemStack(), p);
			return;
    	}
    	
    	/**
    	 * MUST BE HANDLED AFTER EVERYTHING ELSE.
    	 */
    	
    	if (ev.getItem().getItemStack().getType().isBlock() && InventoryUtils.isCarryingVacuumCube(p)) {
    		//Try to insert it into the Vacuum cube.
    		ItemStack[] remaining = InventoryUtils.insertItemsInVacuumCube(p, ev.getItem().getItemStack());
    		if (remaining.length==0) {
        		ev.setCancelled(true);
    			SoundUtils.playGlobalSound(ev.getPlayer().getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.6f, SoundUtils.DetermineItemPitch(ev.getItem().getItemStack()));
    			ev.getItem().remove();
    			return;
    		} else {
    			ev.getItem().setItemStack(remaining[0]);
    		}
    	}
    	
    	if (InventoryUtils.isCarryingFilterCube(p)) {
    		//Try to insert it into the Filter cube.
    		ItemStack[] remaining = InventoryUtils.insertItemsInFilterCube(p, ev.getItem().getItemStack());
    		if (remaining.length==0) {
        		ev.setCancelled(true);
    			SoundUtils.playGlobalSound(ev.getPlayer().getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.6f, SoundUtils.DetermineItemPitch(ev.getItem().getItemStack()));
    			ev.getItem().remove();
    			return;
    		} else {
    			ev.getItem().setItemStack(remaining[0]);
    		}
    	}
    	
    	ev.setCancelled(true);
    	ItemStack givenitem = ev.getItem().getItemStack().clone();
		GenericFunctions.giveItem(p, givenitem);
		if (ev.getRemaining()>0) {
			givenitem.setAmount(ev.getRemaining());
			GenericFunctions.giveItem(p, givenitem);
		}
		ev.getItem().remove();
		return;
    }

	private void HandlePickupAchievements(Player p, ItemStack item) {
		if (p.hasAchievement(Achievement.ACQUIRE_IRON) && item.getType()==Material.DIAMOND && !p.hasAchievement(Achievement.GET_DIAMONDS)) {
			p.awardAchievement(Achievement.GET_DIAMONDS);
		} else
		if (p.hasAchievement(Achievement.NETHER_PORTAL) && item.getType()==Material.BLAZE_ROD && !p.hasAchievement(Achievement.GET_BLAZE_ROD)) {
			p.awardAchievement(Achievement.GET_BLAZE_ROD);
		}
	}
	
	private boolean AutoConsumeItem(Player p, ItemStack item) {
		if (PlayerMode.getPlayerMode(p)==PlayerMode.BARBARIAN) {
			if (GenericFunctions.isAutoConsumeFood(item)) {
				//Consume them here.
				double basepercent = p.getMaxHealth()*0.01;
				double healamt = basepercent*item.getAmount();
				GenericFunctions.HealEntity(p,healamt);
				p.setFoodLevel(Math.min(20, p.getFoodLevel()+item.getAmount()));
				return true;
			}
		}
		return false;
	}
	
	public boolean AutoEquipItem(ItemStack item, Player p) {
		if (item.getType().toString().contains("BOOTS") ||
    			item.getType().toString().contains("LEGGINGS") ||
    			item.getType().toString().contains("CHESTPLATE") ||
    			item.getType().toString().contains("HELMET") ||
    			item.getType().toString().contains("SHIELD") ||
    			item.getType().toString().contains("TIPPED_ARROW") ||
    			item.getType().toString().contains("_AXE")) {
    		ItemStack armor = item;
    		//See if this armor type is not being worn by the player.
    		if (armor.getType().toString().contains("BOOTS") &&
    				p.getEquipment().getBoots()==null &&
    				(!PlayerMode.isRanger(p) || (armor.getType().toString().contains("LEATHER"))) &&
    				!PlayerMode.isSlayer(p)) {
    			p.getEquipment().setBoots(armor);
    			p.sendMessage(ChatColor.DARK_AQUA+"Automatically equipped "+ChatColor.YELLOW+(item.getItemMeta().hasDisplayName()?item.getItemMeta().getDisplayName():GenericFunctions.UserFriendlyMaterialName(item)));
    			SoundUtils.playLocalSound(p, Sound.ENTITY_ITEM_PICKUP, 0.6f, SoundUtils.DetermineItemPitch(armor));
    			GenericFunctions.playProperEquipSound(p,armor.getType());
    			p.updateInventory();
    			return true;
    		} else
    		if (armor.getType().toString().contains("LEGGINGS") &&
    				p.getEquipment().getLeggings()==null &&
    				(!PlayerMode.isRanger(p) || (armor.getType().toString().contains("LEATHER"))) &&
    	    		!PlayerMode.isSlayer(p)) {
    			p.getEquipment().setLeggings(armor);
    			p.sendMessage(ChatColor.DARK_AQUA+"Automatically equipped "+ChatColor.YELLOW+(item.getItemMeta().hasDisplayName()?item.getItemMeta().getDisplayName():GenericFunctions.UserFriendlyMaterialName(item)));
    			SoundUtils.playLocalSound(p, Sound.ENTITY_ITEM_PICKUP, 0.6f, SoundUtils.DetermineItemPitch(armor));
    			GenericFunctions.playProperEquipSound(p,armor.getType());
    			p.updateInventory();
    			return true;
    		} else
    		if (armor.getType().toString().contains("CHESTPLATE") &&
    				p.getEquipment().getChestplate()==null &&
    				(!PlayerMode.isRanger(p) || (armor.getType().toString().contains("LEATHER"))) &&
    	    		!PlayerMode.isSlayer(p)) {
    			p.getEquipment().setChestplate(armor);
    			p.sendMessage(ChatColor.DARK_AQUA+"Automatically equipped "+ChatColor.YELLOW+(item.getItemMeta().hasDisplayName()?item.getItemMeta().getDisplayName():GenericFunctions.UserFriendlyMaterialName(item)));
    			SoundUtils.playLocalSound(p, Sound.ENTITY_ITEM_PICKUP, 0.6f, SoundUtils.DetermineItemPitch(armor));
    			GenericFunctions.playProperEquipSound(p,armor.getType());
    			p.updateInventory();
    			return true;
    		} else
    		if (armor.getType().toString().contains("HELMET") &&
    				p.getEquipment().getHelmet()==null &&
    				(!PlayerMode.isRanger(p) || (armor.getType().toString().contains("LEATHER"))) &&
    	    		!PlayerMode.isSlayer(p)) {
    			p.getEquipment().setHelmet(armor);
    			p.sendMessage(ChatColor.DARK_AQUA+"Automatically equipped "+ChatColor.YELLOW+(item.getItemMeta().hasDisplayName()?item.getItemMeta().getDisplayName():GenericFunctions.UserFriendlyMaterialName(item)));
    			SoundUtils.playLocalSound(p, Sound.ENTITY_ITEM_PICKUP, 0.6f, SoundUtils.DetermineItemPitch(armor));
    			GenericFunctions.playProperEquipSound(p,armor.getType());
    			p.updateInventory();
    			return true;
    		} else
    		if (armor.getType().toString().contains("SHIELD") &&
    				(p.getEquipment().getItemInMainHand().getType()==Material.AIR || p.getInventory().getExtraContents()[0]==null) &&
    				!PlayerMode.isStriker(p) &&
    				(!PlayerMode.isRanger(p) || (armor.getType().toString().contains("LEATHER")))) {
    			if (p.getEquipment().getItemInMainHand().getType()==Material.AIR) {
    				p.getEquipment().setItemInMainHand(armor);
    			} else {
    				p.getInventory().setExtraContents(new ItemStack[]{armor});
    			}
    			p.sendMessage(ChatColor.DARK_AQUA+"Automatically equipped "+ChatColor.YELLOW+(item.getItemMeta().hasDisplayName()?item.getItemMeta().getDisplayName():GenericFunctions.UserFriendlyMaterialName(item)));
    			SoundUtils.playLocalSound(p, Sound.ENTITY_ITEM_PICKUP, 0.6f, SoundUtils.DetermineItemPitch(armor));
    			GenericFunctions.playProperEquipSound(p,armor.getType());
    			p.updateInventory();
    			return true;
    		} else 
    		if (armor.getType().toString().contains("_AXE") &&
    				(p.getEquipment().getItemInMainHand().getType()==Material.AIR || p.getInventory().getExtraContents()[0]==null)) {
    			if (p.getEquipment().getItemInMainHand().getType()==Material.AIR) {
    				p.getEquipment().setItemInMainHand(armor);
    			} else {
    				p.getInventory().setExtraContents(new ItemStack[]{armor});
    			}
    			p.sendMessage(ChatColor.DARK_AQUA+"Automatically equipped "+ChatColor.YELLOW+(item.getItemMeta().hasDisplayName()?item.getItemMeta().getDisplayName():GenericFunctions.UserFriendlyMaterialName(item)));
    			SoundUtils.playLocalSound(p, Sound.ENTITY_ITEM_PICKUP, 0.6f, SoundUtils.DetermineItemPitch(armor));
    			GenericFunctions.playProperEquipSound(p,armor.getType());
    			p.updateInventory();
    			return true;
    		} else 
    		if (ArrowQuiver.isValidQuiver(armor) && p.getInventory().getExtraContents()[0]==null) {
    			p.getInventory().setExtraContents(new ItemStack[]{armor});
    			p.sendMessage(ChatColor.DARK_AQUA+"Automatically equipped "+ChatColor.YELLOW+(item.getItemMeta().hasDisplayName()?item.getItemMeta().getDisplayName():GenericFunctions.UserFriendlyMaterialName(item)));
    			SoundUtils.playLocalSound(p, Sound.ENTITY_ITEM_PICKUP, 0.6f, SoundUtils.DetermineItemPitch(armor));
    			GenericFunctions.playProperEquipSound(p,armor.getType());
    			p.updateInventory();
    			return true;
    		}
    	}
		return false;
	}

    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void onHopperSuction(InventoryMoveItemEvent ev) {
    	if (notWorldShop.contains(InventoryUtils.getInventoryHash(ev.getDestination())) || notWorldShop.contains(InventoryUtils.getInventoryHash(ev.getSource()))) {
    		return;
    	}
    	Inventory source = ev.getSource();
    	Inventory destination = ev.getDestination();
    	if ((source.getHolder() instanceof HopperMinecart) || source.getLocation().getBlock().getType()==Material.HOPPER) {
    		//log("In here 1",2);
	    	if (notWorldShop.contains(InventoryUtils.getInventoryHash(destination))) {
	    		return;
	    	} else {
		    	Location l = destination.getLocation();
		    	//See if this block is a world shop.
		    	if (WorldShop.grabShopSign(l.getBlock())!=null) {
		    		//This is a world shop. DO NOT allow this to happen.
		    		ev.setCancelled(true);
		    		if (source.getHolder() instanceof HopperMinecart) {
			    			HopperMinecart veh = (HopperMinecart)source.getHolder();
			    			if (veh.isValid()) {
			    				veh.getWorld().dropItemNaturally(veh.getLocation(), new ItemStack(Material.HOPPER_MINECART));
			    			}
			    			veh.remove();
		    		} else {
			    		final Location l1=ev.getSource().getLocation();
			    		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
							public void run() {
					    		l1.getBlock().breakNaturally();
							}}
						,1);
		    		}
		    	} else {
		    		notWorldShop.add(InventoryUtils.getInventoryHash(ev.getDestination()));
		    		log("Added not world shop "+ev.getDestination().getLocation(),4);
		    	}
	    	}
    	}
    	if ((destination.getHolder() instanceof HopperMinecart) || destination.getLocation().getBlock().getType()==Material.HOPPER) {
    		//log("In here 2",2);
	    	if (notWorldShop.contains(InventoryUtils.getInventoryHash(source))) {
	    		return;
	    	} else {
		    	Location l = source.getLocation();
		    	//See if this block is a world shop.
		    	if (WorldShop.grabShopSign(l.getBlock())!=null) {
		    		//This is a world shop. DO NOT allow this to happen.
		    		ev.setCancelled(true);
		    		if (destination.getHolder() instanceof HopperMinecart) {
			    			HopperMinecart veh = (HopperMinecart)destination.getHolder();
			    			if (veh.isValid()) {
					    		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
									public void run() {
											veh.getWorld().dropItemNaturally(veh.getLocation(), new ItemStack(Material.HOPPER_MINECART));
									}},1);
			    			}
			    			veh.remove();
		    		} else {
			    		final Location l1=ev.getDestination().getLocation();
			    		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
							public void run() {
					    		l1.getBlock().breakNaturally();
							}}
						,1);
		    		}
		    	} else {
		    		notWorldShop.add(InventoryUtils.getInventoryHash(ev.getSource()));
					log("Added not world shop "+ev.getSource().getLocation(),4);
		    	}
	    	}
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
    
    @EventHandler(priority=EventPriority.LOWEST,ignoreCancelled = true)
    public void onPlayerShootProjectile(EntityShootBowEvent ev) {
    	if (ev.getProjectile() instanceof Projectile) {
    		Projectile arr = (Projectile)ev.getProjectile();
			if (arr.getShooter() instanceof Player) {
				int slot = getSlotArrowShotFrom((Player)arr.getShooter());
				if (slot!=-1) { //We have to have an arrow to continue...
					ItemStack item = ((Player)arr.getShooter()).getInventory().getContents()[slot];
					if (ArrowQuiver.isValidQuiver(item)) {
						boolean infinitearrow=false;
						int prevmode = ArrowQuiver.getArrowQuiverMode(((Player)arr.getShooter()).getInventory().getContents()[slot]);
						List<ItemStack> inv = ArrowQuiver.getContents(ArrowQuiver.getID(((Player)arr.getShooter()).getInventory().getContents()[slot]));
						int amtremaining=0;
						if (inv.size()>0) {
							amtremaining = inv.get(prevmode).clone().getAmount();
						}
						if (ArrowQuiver.isQuiverEmpty(item)) {
							((Player)arr.getShooter()).sendMessage(ChatColor.YELLOW+"Your quiver ran out of arrows! "+ChatColor.WHITE+"You will need to fill it up again!");
							ev.setCancelled(true);
						} else {
							ItemStack item2 = null;
							double infinitychance = GenericFunctions.calculateInfinityChance(((Player)arr.getShooter()).getEquipment().getItemInMainHand());
							if (Math.random()<=infinitychance) {
								item2 = ArrowQuiver.ReturnAndRemoveShotArrow(item,((Player)arr.getShooter()).getEquipment().getItemInMainHand());
								infinitearrow=true;
								arr.setMetadata("INFINITEARROW", new FixedMetadataValue(this,true));
							} else {
								item2 = ArrowQuiver.ReturnAndRemoveShotArrow(item);
							}
							//We can now do something specific to the projectile based on the item.
							ConvertToArrowWithProperties(arr, item2);
	
							if (arr.getShooter() instanceof Player &&
									arr.hasMetadata("PIERCING_ARR")) {
								Player p = (Player)arr.getShooter();
								ShootPiercingArrow(arr, p);
								ev.setCancelled(true);
							}
						}
						final int amt = amtremaining;
						//((Player)arr.getShooter()).playSound(arr.getLocation(),Sound.ENTITY_ARROW_SHOOT,1.0f,1.0f);
						//ev.setProjectile(ArrowQuiver.getProjectileFromQuiver(((Player)arr.getShooter()).getEquipment().getItemInOffHand(), arr));
						final ItemStack quiver = item.clone();
						Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> {
							((Player)arr.getShooter()).getInventory().setItem(slot, quiver);
							ArrowQuiver.updateQuiverLore(((Player)arr.getShooter()).getInventory().getContents()[slot]);
							int currmode = ArrowQuiver.getArrowQuiverMode(((Player)arr.getShooter()).getInventory().getContents()[slot]);
							int newamtremaining = 0;
							List<ItemStack> newinv = ArrowQuiver.getContents(ArrowQuiver.getID(((Player)arr.getShooter()).getInventory().getContents()[slot]));
							if (newinv.size()>0) {
								newamtremaining = newinv.get(currmode).getAmount();
							}
							TwosideKeeper.log("prevmode: "+prevmode+", currmode: "+currmode+", remaining: "+amt+", newremaining: "+newamtremaining, 5);
							if (prevmode!=currmode || newamtremaining>amt) {
								String message = ChatColor.DARK_GRAY+"Now Firing "+ChatColor.YELLOW+GenericFunctions.UserFriendlyMaterialName(ArrowQuiver.getContents(ArrowQuiver.getID(((Player)arr.getShooter()).getInventory().getContents()[slot])).get(currmode))+ChatColor.GRAY+" ["+ArrowQuiver.getContents(ArrowQuiver.getID(((Player)arr.getShooter()).getInventory().getContents()[slot])).get(currmode).getAmount()+"]";
								GenericFunctions.sendActionBarMessage((Player)arr.getShooter(), message, true);
							}
						},1);
						if (infinitearrow) {
							TwosideKeeper.log("Infinite SEt.", 5);
							arr.setMetadata("INFINITEARROW", new FixedMetadataValue(this,true));
						}
						return;
					} else {
						//Handle infinity separately if it's not a normal arrow.
						if (arr.getType()==EntityType.TIPPED_ARROW || arr.getType()==EntityType.SPECTRAL_ARROW) {
							double infinitychance = GenericFunctions.calculateInfinityChance(((Player)arr.getShooter()));
							if (Math.random()<=infinitychance) {
								item.setAmount(item.getAmount()+1);
								Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> {((Player)arr.getShooter()).getInventory().setItem(slot, item);},1);
								TwosideKeeper.log("Infinite SEt.", 5);
								arr.setMetadata("INFINITEARROW", new FixedMetadataValue(this,true));
							}
						}
					}
				}
			}
    	}
    }
	public static void ShootPiercingArrow(Projectile arr, Player p) {
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		SoundUtils.playLocalSound(p, Sound.ENTITY_ARROW_SHOOT, 1.0f, 1.6f);
		pd.lastarrowwasinrangermode=true;
		Collection<LivingEntity> targets = aPlugin.API.rayTraceTargetEntities(p, 100);
		Location arrowloc = arr.getLocation().clone();
		Vector dir = arr.getVelocity().clone();
		for (int i=0;i<100;i++) {
			aPlugin.API.displayEndRodParticle(arrowloc, (float)0.0f, (float)0.0f, (float)0.0f, 0.0f, 1);
			arrowloc=arrowloc.add(dir);
		}
		for (LivingEntity le : targets) {
			CustomDamage.ApplyDamage(0, arr, le, p.getEquipment().getItemInMainHand(), null, CustomDamage.IGNORE_DAMAGE_TICK);
			aPlugin.API.displayEndRodParticle(le.getLocation(), (float)0.05f, (float)0.05f, (float)0.05f, 0.1f, 4);
		}
		pd.lastarrowwasinrangermode=false;
	}
    
	private int getSlotArrowShotFrom(Player p) {
		if (GenericFunctions.isValidArrow(p.getInventory().getContents()[40],true)) {
			return 40;
		} else {
			ItemStack[] contents = p.getInventory().getContents();
			for (int i=0;i<contents.length;i++) {
				if (GenericFunctions.isValidArrow(contents[i],true)) {
					return i;
				}
			}
		}
		//log("DID NOT FIND A VALID ARROW! This should NOT be happening!",0);
		return -1;
	}
	public void ConvertToArrowWithProperties(Projectile arr, ItemStack item) {
		if (item!=null) {
			if (item.getType()==Material.TIPPED_ARROW) {
				PotionMeta pm = (PotionMeta)item.getItemMeta();
				boolean customarrow=false;
				for (PotionEffect pe : pm.getCustomEffects()) {
					if (pe.getDuration()==0) {
						customarrow=true;
						if (pe.getType().equals(PotionEffectType.FIRE_RESISTANCE)) {
							arr.setMetadata("DOUBLE_DAMAGE_ARR", new FixedMetadataValue(TwosideKeeper.plugin,true));
						} else
						if (pe.getType().equals(PotionEffectType.SPEED)) {
							arr.setMetadata("QUADRUPLE_DAMAGE_ARR", new FixedMetadataValue(TwosideKeeper.plugin,true));
						} else
						if (pe.getType().equals(PotionEffectType.POISON)) {
							arr.setMetadata("POISON_ARR", new FixedMetadataValue(TwosideKeeper.plugin,true));
						} else
						if (pe.getType().equals(PotionEffectType.WEAKNESS)) {
							arr.setMetadata("TRAP_ARR", new FixedMetadataValue(TwosideKeeper.plugin,true));
						} else
						if (pe.getType().equals(PotionEffectType.HEAL)) {
							arr.setMetadata("PIERCING_ARR", new FixedMetadataValue(TwosideKeeper.plugin,true));
						} else
						if (pe.getType().equals(PotionEffectType.INVISIBILITY)) {
							arr.setMetadata("EXPLODE_ARR", new FixedMetadataValue(TwosideKeeper.plugin,true));
						}
						break;
					}
				}
				if (!customarrow) {
					StringBuilder effectstring = new StringBuilder("");
					for (PotionEffect pe : pm.getCustomEffects()) {
						if (effectstring.length()==0) {
							effectstring.append(pe.getType().getName()+","+pe.getDuration()+","+pe.getAmplifier());
						} else {
							effectstring.append(";"+pe.getType().getName()+","+pe.getDuration()+","+pe.getAmplifier());
						}
					}
					log(effectstring.toString(),5);
					if (effectstring.length()>0) {
						arr.setMetadata("TIPPED_ARROW", new FixedMetadataValue(TwosideKeeper.plugin,effectstring.toString()));
					}
					String effect2string = pm.getBasePotionData().getType().name()+","+pm.getBasePotionData().isExtended()+","+pm.getBasePotionData().isUpgraded();
					TwosideKeeper.log("Base Arrow is "+effect2string, 5);
					arr.setMetadata("BASE_ARROW", new FixedMetadataValue(TwosideKeeper.plugin,effect2string.toString()));
				}
			}
			if (item.getType()==Material.SPECTRAL_ARROW) {
				arr.setMetadata("TIPPED_ARROW", new FixedMetadataValue(TwosideKeeper.plugin,"GLOWING,100,1"));
			}
		}
	}
    
    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void onArrowShoot(ProjectileLaunchEvent ev) {
    	TwosideKeeper.log("Launch event.", 5);
    	if (ev.getEntity() instanceof Projectile) {
    		Projectile arr = (Projectile)ev.getEntity();
    		//Arrow newarrow = arr.getLocation().getWorld().spawnArrow(arr.getLocation(), arr.getVelocity(), 1, 12);
    		//TwosideKeeper.log(GenericFunctions.GetEntityDisplayName(arr)+" being shot.", 0);
    		if (arr instanceof Fireball && (arr.getShooter() instanceof Ghast)) {
    			Ghast g = (Ghast)arr.getShooter();
    			Fireball fb = (Fireball)arr;
    			if (MonsterController.getLivingEntityDifficulty(g)==LivingEntityDifficulty.DANGEROUS) {
        			fb.setVelocity(fb.getDirection().multiply(5f));
    			} else
    			if (MonsterController.getLivingEntityDifficulty(g)==LivingEntityDifficulty.DEADLY) {
        			fb.setVelocity(fb.getDirection().multiply(10f));
    			} else
    			if (MonsterController.getLivingEntityDifficulty(g)==LivingEntityDifficulty.HELLFIRE) {
    				CustomMonster mon = CustomMonster.getCustomMonster(g);
        			if (mon!=null && ((HellfireGhast)mon).getLastFireball()+45<TwosideKeeper.getServerTickTime()) {
	    				Bukkit.getScheduler().scheduleSyncDelayedTask(this, ()->{FireSpecialFireball(g,fb);},15);
	    				Bukkit.getScheduler().scheduleSyncDelayedTask(this, ()->{FireSpecialFireball(g,fb);},30);
	    				Bukkit.getScheduler().scheduleSyncDelayedTask(this, ()->{FireSpecialFireball(g,fb);},45);
	    				((HellfireGhast)mon).recordLastFireball();
	        			fb.setVelocity(fb.getDirection().multiply(20f));
        			}
    			}
    			return;
    		}

    		if (arr instanceof WitherSkull && (arr.getShooter() instanceof Wither)) {
    			Wither w = (Wither)arr.getShooter();
    			WitherSkull ws = (WitherSkull)arr;
    			LivingEntity le = w.getTarget();
    			if (le!=null) {
    				CustomMonster mon = CustomMonster.getCustomMonster(w);
    				if (mon!=null && ((sig.plugin.TwosideKeeper.Monster.Wither)mon).getLastSkullShot()+20<TwosideKeeper.getServerTickTime()) {
	    				Bukkit.getScheduler().scheduleSyncDelayedTask(this, ()->{FireExtraWitherSkull(w,le,ws);},5);
	    				Bukkit.getScheduler().scheduleSyncDelayedTask(this, ()->{FireExtraWitherSkull(w,le,ws);},10);
	    				Bukkit.getScheduler().scheduleSyncDelayedTask(this, ()->{FireExtraWitherSkull(w,le,ws);},15);
	        			((sig.plugin.TwosideKeeper.Monster.Wither)mon).resetLastSkullShot();
    				}
    			}
    		}
    		
    		if (arr instanceof SmallFireball && (arr.getShooter() instanceof Blaze)) {
    			Blaze b = (Blaze)arr.getShooter();
    			SmallFireball sf = (SmallFireball)arr;
    			LivingEntity le = b.getTarget();
    			if (le!=null) {
    				CustomMonster mon = CustomMonster.getCustomMonster(b);
        			if (mon!=null && ((sig.plugin.TwosideKeeper.Monster.Blaze)mon).getLastFireball()+60<TwosideKeeper.getServerTickTime()) {
	        			if (MonsterController.getLivingEntityDifficulty(b)==LivingEntityDifficulty.DANGEROUS) {
		    				Bukkit.getScheduler().scheduleSyncDelayedTask(this, ()->{FireExtraBlazeFireball(b,le,sf);},45);
	        			} else
	        			if (MonsterController.getLivingEntityDifficulty(b)==LivingEntityDifficulty.DEADLY) {
		    				Bukkit.getScheduler().scheduleSyncDelayedTask(this, ()->{FireExtraBlazeFireball(b,le,sf);},30);
		    				Bukkit.getScheduler().scheduleSyncDelayedTask(this, ()->{FireExtraBlazeFireball(b,le,sf);},45);
	        			} else
	        			if (MonsterController.getLivingEntityDifficulty(b)==LivingEntityDifficulty.HELLFIRE) {
		    				Bukkit.getScheduler().scheduleSyncDelayedTask(this, ()->{FireExtraBlazeFireball(b,le,sf);},15);
		    				Bukkit.getScheduler().scheduleSyncDelayedTask(this, ()->{FireExtraBlazeFireball(b,le,sf);},30);
		    				Bukkit.getScheduler().scheduleSyncDelayedTask(this, ()->{FireExtraBlazeFireball(b,le,sf);},45);
	        			}
	        			((sig.plugin.TwosideKeeper.Monster.Blaze)mon).resetLastFireball();
        			}
        			sf.setVelocity(sf.getDirection().multiply(5f));
    			}
    			return;
    		}
    		
    		if (arr.getCustomName()==null && (arr instanceof Arrow)) {
				if (arr.getType()==EntityType.TIPPED_ARROW) {
					//This might be special. Let's get the potion meta.
					TippedArrow ta = (TippedArrow)arr;
					if (ta.hasMetadata("INFINITEARROW")) {
						TwosideKeeper.log("Infinite Arrow 1", 5);
					}
					if (ta.hasCustomEffects()) {
						List<PotionEffect> eff = ta.getCustomEffects();
						//This is custom! Let's see what it is.
						for (int i=0;i<eff.size();i++) {
							PotionEffect pe = eff.get(i);
							if (pe.getDuration()==0) {
								log("This is special!",5);
								if (pe.getType().equals(PotionEffectType.FIRE_RESISTANCE)) {
									if (ta.hasMetadata("INFINITEARROW")) {
										TwosideKeeper.log("Still here.", 0);
									}
									arr.setMetadata("DOUBLE_DAMAGE_ARR", new FixedMetadataValue(TwosideKeeper.plugin,true));
									if (ta.hasMetadata("INFINITEARROW")) {
										TwosideKeeper.log("Still here 2!", 0);
									}
								} else
								if (pe.getType().equals(PotionEffectType.SPEED)) {
									arr.setMetadata("QUADRUPLE_DAMAGE_ARR", new FixedMetadataValue(TwosideKeeper.plugin,true));
								} else
								if (pe.getType().equals(PotionEffectType.POISON)) {
									arr.setMetadata("POISON_ARR", new FixedMetadataValue(TwosideKeeper.plugin,true));
								} else
								if (pe.getType().equals(PotionEffectType.WEAKNESS)) {
									arr.setMetadata("TRAP_ARR", new FixedMetadataValue(TwosideKeeper.plugin,true));
								} else
								if (pe.getType().equals(PotionEffectType.HEAL)) {
									if (arr.getShooter() instanceof Player) {
										ShootPiercingArrow(arr, (Player)arr.getShooter());
										((Player)arr.getShooter()).getInventory().removeItem(Recipes.getArrowFromMeta("PIERCING_ARR"));
										ev.setCancelled(true);
									}
								} else
								if (pe.getType().equals(PotionEffectType.INVISIBILITY)) {
									arr.setMetadata("EXPLODE_ARR", new FixedMetadataValue(TwosideKeeper.plugin,true));
								}
							}
						}
					}
					if (ta.hasMetadata("INFINITEARROW")) {
						TwosideKeeper.log("Infinite Arrow 2", 5);
					}
				}
    		}
    		
    		if (arr.getShooter() instanceof Player &&
    				arr.getCustomName()==null && (arr instanceof Arrow)) {
    			Player p = (Player)arr.getShooter();
				if (arr.hasMetadata("INFINITEARROW")) {
					TwosideKeeper.log("Infinite Arrow 2>", 5);
				}
    			LivingEntity checkent = aPlugin.API.getTargetEntity(p, 100);
        		if (checkent!=null && (checkent instanceof Monster)) {
        			if (!livingentitydata.containsKey(checkent.getUniqueId())) {
        				LivingEntityStructure newstruct = new LivingEntityStructure((Monster)checkent);
        				newstruct.SetTarget(p);
        				livingentitydata.put(checkent.getUniqueId(), newstruct);
        				Monster m = (Monster)checkent;
        				if (!m.hasPotionEffect(PotionEffectType.GLOWING)) {
        					m.setTarget(p);
        				}
        			}
        			log("Setup new target: "+p.getName(),5);
        		}
    			if (PlayerMode.isRanger(p)) {
    				LivingEntity findtarget = aPlugin.API.rayTraceTargetEntity(p,100);
					if (GenericFunctions.getBowMode(p)==BowMode.SNIPE) {
	    				if (findtarget==null || !p.hasLineOfSight(findtarget)) {
	    					arr.setVelocity(arr.getVelocity().multiply(1000));
	    				} else {
	    					//We found a target, we are going to disable this arrow and create an artifical arrow hit from here.
	    					//p.getWorld().spawnArrow(aPlugin.API.getProjectedArrowHitLocation(findtarget, p), arr.get, arg2, arg3);
	            			Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
	            				public void run() {	
			    					arr.teleport(aPlugin.API.getProjectedArrowHitLocation(findtarget, p).subtract(arr.getVelocity()));
			    					log("Teleported to calculated hit location: "+arr.getLocation(),5);
	            				}},1);
	    				}
    					aPlugin.API.damageItem(p, p.getEquipment().getItemInMainHand(), 3);
					}
    			}
				PlayerStructure pd = (PlayerStructure)playerdata.get(p.getUniqueId());
				pd.lastarrowpower=arr.getVelocity().lengthSquared();
				pd.lastarrowwasinrangermode=(PlayerMode.isRanger(p)&&GenericFunctions.getBowMode(p)==BowMode.SNIPE);
				log("Arrow velocity is "+arr.getVelocity().lengthSquared(),5);
	    		arr.setCustomName("HIT");
				if (arr.hasMetadata("INFINITEARROW")) {
					TwosideKeeper.log("Infinite Arrow 3", 5);
				}
    		}
    	}
    }
    
	private void FireExtraWitherSkull(Wither w, LivingEntity le, WitherSkull ws) {
		WitherSkull skull = w.launchProjectile(WitherSkull.class);
		skull.setShooter(w);
		skull.setDirection(ws.getDirection().add(new Vector(Math.random()*0.2-0.1,Math.random()*0.2-0.1,Math.random()*0.2-0.1)));
		SoundUtils.playGlobalSound(skull.getLocation(), Sound.ENTITY_WITHER_SHOOT, 1.0f, 1.0f);
	}
	
	private void FireExtraBlazeFireball(Blaze b, LivingEntity le, SmallFireball ref) {
		SmallFireball sf = b.launchProjectile(SmallFireball.class);
		sf.setShooter(b);
		sf.setDirection(ref.getDirection());
		SoundUtils.playGlobalSound(sf.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0f, 1.0f);
	}
	
	public void FireSpecialFireball(Ghast g, Fireball fireref) {
		Fireball fireball = g.launchProjectile(Fireball.class);
		fireball.setShooter(g);
		fireball.setVelocity(fireref.getDirection().multiply(20f));
		SoundUtils.playGlobalSound(fireball.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1.0f, 1.0f);
	}
    
    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void onItemCraftEvent(PrepareItemCraftEvent ev) {
    	ItemStack result = ev.getInventory().getResult();
    	if (result.getType()==Material.TNT) {
    		result.setAmount(result.getAmount()*5); //TNT recipes are 5 times as effective.
    	}

    	//Check if we are using an item cube in a non-item cube recipe.
    	
    	if (WorldShop.isPlaceableWorldShop(result)) {
    		//Find the slot with the world shop item.
    		for (int i=1;i<ev.getInventory().getSize();i++) {
    			ItemStack item = ev.getInventory().getItem(i);
    			if (item!=null && item.getType()!=Material.SIGN &&
    					item.getType()!=Material.CHEST) {
    				//This is the item. Check for durability.
    				if (ItemUtils.isValidLoreItem(item) || (item.getDurability()!=0 && GenericFunctions.isEquip(item))) { //We cannot use this in this recipe.
    					ev.getInventory().setResult(new ItemStack(Material.AIR));
    					return;
    				}
    				if (item.getDurability()!=0 && !GenericFunctions.isEquip(item)) {
    					//Modify the final chest.
    					ItemUtils.clearLore(result);
    					ItemUtils.addLore(result,ChatColor.DARK_PURPLE+"World Shop Chest");
    					ItemUtils.addLore(result,ChatColor.BLACK+""+ChatColor.MAGIC+item.getType().name()+","+item.getDurability());
    					ItemUtils.addLore(result,ChatColor.LIGHT_PURPLE+"Place in the world to setup a");
    					ItemUtils.addLore(result,ChatColor.LIGHT_PURPLE+"world shop that sells "+ChatColor.YELLOW+GenericFunctions.UserFriendlyMaterialName(item));
    					ItemUtils.setDisplayName(result,ChatColor.YELLOW+GenericFunctions.UserFriendlyMaterialName(item)+" Shop Chest");
    					ItemUtils.hideEnchantments(result);
    					result.addUnsafeEnchantment(Enchantment.LUCK, 4);
    					ev.getInventory().setResult(result);
    					return;
    				}
    			}
    		}
    		return;
    	}

    	//Item cube should be in slot 4.
    	if (ev.getInventory().getItem(5)!=null) {
			ItemMeta inventory_itemMeta=ev.getInventory().getItem(5).getItemMeta();
			if (inventory_itemMeta.hasLore() && inventory_itemMeta.getLore().size()>=4) {
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
    		List<ItemStack> items_found = new ArrayList<ItemStack>();
    		for (int i=1;i<ev.getInventory().getSize();i++) {
    			if (ev.getInventory().getItem(i)!=null &&
    					ev.getInventory().getItem(i).getType()==Material.NETHER_STAR) {
    	    		log(" Nether Star Found.",5);
    				nether_star_found=true;
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
    		boolean hasdye=false;
    		ItemStack artifact_item=null;
    		boolean pumpkin_seeds=false;
			for (int i=1;i<ev.getInventory().getSize();i++) {
		    	if (ev.getInventory().getItem(i)!=null &&
						ev.getInventory().getItem(i).getType()!=Material.AIR &&
						ev.getInventory().getItem(i).getType()==Material.INK_SACK) {
		    		//We are not supposed to be in here!
		    		hasdye=true;
		    	}
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
				    		default: {
				    			
				    		}
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
						!Artifact.isArtifact(ev.getInventory().getItem(i)) &&
						!hasdye) {
					log("One of these is not an artifact",5);
		    		ev.getInventory().setResult(new ItemStack(Material.AIR)); //Don't allow it, an item is not an artifact!
				}
			}
			if (!hasdye) {
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
						if (newartifact.getType().name().contains("LEATHER")) {
							//Transfer over the color.
							LeatherArmorMeta lm = (LeatherArmorMeta)m;
							LeatherArmorMeta old_lm = (LeatherArmorMeta)artifact_item.getItemMeta();
							lm.setColor(old_lm.getColor());
							newartifact.setItemMeta(lm);
						}
						GenericFunctions.addObscureHardenedItemBreaks(newartifact, 5-GenericFunctions.getObscureHardenedItemBreaks(newartifact));
						//Lines can all be transferred over. No lines need to be preserved.
						
						for (Enchantment e : newartifact.getEnchantments().keySet()) {
							if (newartifact.containsEnchantment(e) && artifact_item.getEnchantmentLevel(e)>newartifact.getEnchantmentLevel(e)) {
								log("Contains "+e.toString()+" "+newartifact.getEnchantmentLevel(e), 2);
								//These are the enchantments that clash. If the resultitem ones are greater, apply them to the new item.
								newartifact.addUnsafeEnchantment(e, artifact_item.getEnchantmentLevel(e));
								log("Applied "+e.getName()+" "+artifact_item.getEnchantmentLevel(e)+" to the artifact",2);
							}
						}
						for (int i=0;i<artifact_item.getEnchantments().size();i++) {
							Enchantment e = (Enchantment)artifact_item.getEnchantments().keySet().toArray()[i];
							if (!newartifact.containsEnchantment(e)) {
								//log("Contains "+e.toString()+" "+newartifact.getEnchantmentLevel(e), 2);
								//These are the enchantments that clash. If the resultitem ones are greater, apply them to the new item.
								newartifact.addUnsafeEnchantment(e, artifact_item.getEnchantmentLevel(e));
								log("Applied "+e.getName()+" "+artifact_item.getEnchantmentLevel(e)+" to the artifact",2);
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
    		if (ev.getVehicle().isValid()) {
	    		ev.getVehicle().remove();

	    		Bukkit.getScheduler().runTaskLater(this, () -> {
	    			if (ev.getAttacker().getVehicle()==null) {
	    				switch (ev.getVehicle().getType()) {
				    		case MINECART:{
				    			ev.getVehicle().getLocation().getWorld().dropItemNaturally(ev.getVehicle().getLocation().add(0,1,0), new ItemStack(Material.MINECART));
				    		}break;
				    		case MINECART_FURNACE:{
				    			ev.getVehicle().getLocation().getWorld().dropItemNaturally(ev.getVehicle().getLocation().add(0,1,0), new ItemStack(Material.MINECART));
				    			ev.getVehicle().getLocation().getWorld().dropItemNaturally(ev.getVehicle().getLocation().add(0,1,0), new ItemStack(Material.FURNACE));
				    		}break;
				    		case MINECART_TNT:{
				    			ev.getVehicle().getLocation().getWorld().dropItemNaturally(ev.getVehicle().getLocation().add(0,1,0), new ItemStack(Material.MINECART));
				    			ev.getVehicle().getLocation().getWorld().dropItemNaturally(ev.getVehicle().getLocation().add(0,1,0), new ItemStack(Material.TNT));
				    		}break;
				    		default:{
				    			
				    		}
			    		}
	    			}
	    		}, 1);
	    		
    		}
    	}
    }
    
    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void MinecartExitEvent(VehicleExitEvent ev) {
    	if (ev.getExited()!=null) {
	    	if (ev.getExited() instanceof Player &&
	    			ev.getVehicle().getType()==EntityType.MINECART) {
	    		//p.sendMessage("Off.");
	    		//Drop a minecart at the position.
	    		if (ev.getVehicle().isValid()) {
		    		ev.getVehicle().remove();
		    		Bukkit.getScheduler().runTaskLater(this, () -> {
		    			if (ev.getExited().getVehicle()==null) {
		    				ev.getVehicle().getWorld().dropItemNaturally(ev.getExited().getLocation().add(0,1,0), new ItemStack(Material.MINECART));
		    		}},1);
	    		}
	    	}
    	}
    }
    
    @EventHandler(priority=EventPriority.LOW,ignoreCancelled = true)
    public void onTeleportEvent(PlayerTeleportEvent ev) {
    	final Player p = ev.getPlayer();
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			public void run() {
				if (p.isValid() && p.isOnline()) {
					setPlayerMaxHealth(p);
				}
			}
		},20);
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		if (pd.linkplayer!=null && pd.linkplayer.isValid() && pd.lastlinkteleport+20<TwosideKeeper.getServerTickTime()) {
			PlayerStructure pdl = PlayerStructure.GetPlayerStructure(pd.linkplayer);
			pdl.lastlinkteleport=TwosideKeeper.getServerTickTime();
			pd.lastlinkteleport=TwosideKeeper.getServerTickTime();
			pd.linkplayer.teleport(ev.getTo());
		}
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
		getConfig().set("ARROWQUIVERID", ARROWQUIVERID);
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
		getConfig().set("LAST_DEAL", LAST_DEAL);
		getConfig().set("WEATHER_WATCH_USERS", weather_watch_users);
		if (ELITE_LOCATION!=null) {
			getConfig().set("ELITE_LOCATION_X", ELITE_LOCATION.getBlockX());
			getConfig().set("ELITE_LOCATION_Z", ELITE_LOCATION.getBlockZ());
		}
		//getConfig().set("MOTD", MOTD); //It makes no sense to save the MOTD as it will never be modified in-game.
		saveConfig();
		
		TwosideRecyclingCenter.saveConfig();
		TwosideShops.SaveShopPurchases();
		
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
		getConfig().addDefault("ARROWQUIVERID", ARROWQUIVERID);
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
		getConfig().addDefault("WORLD_SHOP_DIST", worldShopDistanceSquared);
		getConfig().addDefault("WORLD_SHOP_MULT", worldShopPriceMult);
		getConfig().addDefault("LAST_DEAL", TimeUtils.GetCurrentDayOfWeek());
		getConfig().addDefault("WEATHER_WATCH_USERS", weather_watch_users);
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
		ARROWQUIVERID = getConfig().getInt("ARROWQUIVERID");
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
		worldShopDistanceSquared = getConfig().getDouble("WORLD_SHOP_DIST");
		worldShopPriceMult = getConfig().getDouble("WORLD_SHOP_MULT");
		LAST_DEAL = getConfig().getInt("LAST_DEAL");
		weather_watch_users = getConfig().getStringList("WEATHER_WATCH_USERS");
		if (getConfig().contains("ELITE_LOCATION_X")) {
			int x = getConfig().getInt("ELITE_LOCATION_X");
			int z = getConfig().getInt("ELITE_LOCATION_Z");
			TwosideKeeper.ELITE_LOCATION=new Location(Bukkit.getWorld("world"),x,0,z);
		} else {
			log("Did not find a valid Elite Location! Creating a new one!",2);
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				@Override
				public void run() {
					GenericFunctions.generateNewElite(null,"");
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
	@Deprecated
	public static List<ItemStack> itemCube_loadConfig(int id){
		return ItemCubeUtils.loadConfig(id);
	}

	@Deprecated
	public static List<ItemStack> itemCube_loadFilterConfig(int id){
		return ItemCubeUtils.loadFilterConfig(id);
	}
	
	//Item Cube Saving.
	@Deprecated
	public static void itemCube_saveConfig(int id, List<ItemStack> items){
		itemCube_saveConfig(id,items,null);
	}

	@Deprecated
	public static void itemCube_saveConfig(int id, List<ItemStack> items, CubeType cubetype){
		ItemCubeUtils.saveConfig(id, items, cubetype);
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
		for (PotionEffect pe : p.getActivePotionEffects()) {
			if (pe.getType().equals(PotionEffectType.BLINDNESS) ||
					pe.getType().equals(PotionEffectType.CONFUSION) ||
					pe.getType().equals(PotionEffectType.HARM) ||
					pe.getType().equals(PotionEffectType.POISON) ||
					pe.getType().equals(PotionEffectType.SLOW) ||
					pe.getType().equals(PotionEffectType.SLOW_DIGGING) ||
					pe.getType().equals(PotionEffectType.WEAKNESS) ||
					pe.getType().equals(PotionEffectType.WITHER) ||
					pe.getType().equals(PotionEffectType.UNLUCK)) {
				hasDebuff=true;
			}
		}
		
		String bar = " ";
		
		boolean isslayer = PlayerMode.getPlayerMode(p)==PlayerMode.SLAYER;
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		double hpval = (isslayer)?pd.slayermodehp:p.getHealth();
		if (isslayer) {p.setHealth(pd.slayermodehp);}
		
		if (pcthp==100) {bar += ((isHungry)?ChatColor.BLUE:ChatColor.AQUA)+""+Math.round(hpval)+""+Character.toString((char)0x2665);} else
		if (pcthp>66) {bar += ((isHungry)?ChatColor.DARK_GREEN:ChatColor.GREEN)+""+Math.round(hpval)+""+Character.toString((char)0x2665);}
		else if (pcthp>33) {bar += ((isHungry)?ChatColor.GOLD:ChatColor.YELLOW)+""+Math.round(hpval)+""+Character.toString((char)0x2665);}
		else {bar += ((isHungry)?ChatColor.DARK_RED:ChatColor.RED)+""+Math.round(hpval)+""+Character.toString((char)0x2665);}
		
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
			//bar+=ChatColor.DARK_PURPLE+""+"ï¿½";
		} else
		if (inEnd) {
			/* 058D:Counter-clockwise portal
			 * 058E:Clockwise portal
			 */
			bar+=ChatColor.DARK_BLUE+""+Character.toString((char)0x25CA);
			//bar+=ChatColor.DARK_PURPLE+""+"ï¿½";
		}
		
		return bar;
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
					log("[WARNING] Could not find the correct player data file for "+p+" to get money data from.",1);
					e.printStackTrace();
				}
			}
		}
	}
	public static void givePlayerBankMoney(Player p, double amt) {
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
				log("[WARNING] Could not find the correct player data file for "+p+" to get bank money data from.",1);
				e.printStackTrace();
			}
		}
	}
	
	public static void setPlayerMaxHealth(Player p) {
		setPlayerMaxHealth(p,null);
	}
	
	public static void setPlayerMaxHealth(Player p, Double ratio) {
		//Determine player max HP based on armor being worn.
		double hp=10; //Get the base max health.
		//Get all equips.
		ItemStack[] equipment = {p.getInventory().getHelmet(),p.getInventory().getChestplate(),p.getInventory().getLeggings(),p.getInventory().getBoots()};
		double maxdeduction=1;
		for (ItemStack equip : equipment) {
			if (equip!=null) {
				boolean is_block_form=false;
				//Determine if the piece is block form.
				//If this is an artifact armor, we totally override the base damage reduction.
				if (GenericFunctions.isArmor(equip) && Artifact.isArtifact(equip)) {
					//Let's change up the damage.
					log("This is getting through",5);
					/*int dmgval = ArtifactItemType.valueOf(Artifact.returnRawTool(equip.getType())).getHealthAmt(equip.getEnchantmentLevel(Enchantment.LUCK));
					if (dmgval!=-1) {
						hp += dmgval;
					}*/
				} else {
					if (equip.hasItemMeta() &&
							equip.getItemMeta().hasLore()) {
						for (int j=0;j<equip.getItemMeta().getLore().size();j++) {
							if (equip.getItemMeta().getLore().get(j).contains(ChatColor.GRAY+"Breaks Remaining:")) {
								//This is a block version.
								is_block_form=true;
								break;
							}
						}
					}
					if (equip.getType().toString().contains("LEATHER")) {
						//This is a leather piece.
						hp+=ARMOR_LEATHER_HP;
					} else if (equip.getType().toString().contains("IRON")) {
						//This is an iron piece.
						hp+=(is_block_form)?ARMOR_IRON2_HP:ARMOR_IRON_HP;
					} else  if (equip.getType().toString().contains("GOLD")) {
						//This is a gold piece.
						hp+=(is_block_form)?ARMOR_GOLD2_HP:ARMOR_GOLD_HP;
					} else  if (equip.getType().toString().contains("DIAMOND")) {
						//This is a diamond piece.
						hp+=(is_block_form)?ARMOR_DIAMOND2_HP:ARMOR_DIAMOND_HP;
					}
				}
				if (GenericFunctions.isArtifactEquip(equip)) {
					log("Add in "+GenericFunctions.getAbilityValue(ArtifactAbility.HEALTH, equip),5);
					if (PlayerMode.getPlayerMode(p)==PlayerMode.RANGER) {
						hp += (double)GenericFunctions.getAbilityValue(ArtifactAbility.HEALTH, equip)/2;
					} else {
						hp += (double)GenericFunctions.getAbilityValue(ArtifactAbility.HEALTH, equip);
					}
					
					if (ArtifactAbility.containsEnchantment(ArtifactAbility.GREED, equip)) {
						maxdeduction /= ArtifactAbility.containsEnchantment(ArtifactAbility.GREED, equip)?2:1;
					}
				}
			}
		}
		
		//Check the hotbar for set equips.
		hp+=ItemSet.GetTotalBaseAmount(GenericFunctions.getHotbarItems(p), p, ItemSet.GLADOMAIN);
		log("Health is now "+hp,5);
		if (ArtifactAbility.containsEnchantment(ArtifactAbility.GREED, p.getEquipment().getItemInMainHand())) {
			maxdeduction /= ArtifactAbility.containsEnchantment(ArtifactAbility.GREED, p.getEquipment().getItemInMainHand())?2:1;
		}
		log("maxdeduction is "+maxdeduction,5);
		
		if (PlayerMode.isDefender(p)) {
			hp+=10;
			GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.REGENERATION,60,(p.isBlocking())?1:0,p,false);
		}
		if (PlayerMode.isBarbarian(p)) {
			double red = 1-CustomDamage.CalculateDamageReduction(1,p,null);
			hp+=(red*2)*100;
		}
		

		hp+=ItemSet.TotalBaseAmountBasedOnSetBonusCount(GenericFunctions.getEquipment(p,true), p, ItemSet.DAWNTRACKER, 4, 4);
		hp+=ItemSet.TotalBaseAmountBasedOnSetBonusCount(GenericFunctions.getEquipment(p), p, ItemSet.SONGSTEEL, 2, 2);
		
		/*
		if (p.hasPotionEffect(PotionEffectType.ABSORPTION)) {
			Collection<PotionEffect> player_effects = p.getActivePotionEffects();
			for (int i=0;i<player_effects.size();i++) {
				if (Iterables.get(player_effects, i).getType().equals(PotionEffectType.ABSORPTION)) {
					hp += (Iterables.get(player_effects, i).getAmplifier()+1)*4;
				}
			}
		}*/
		hp+=ItemSet.TotalBaseAmountBasedOnSetBonusCount(GenericFunctions.getEquipment(p), p, ItemSet.ALIKAHN, 2, 2)+ItemSet.TotalBaseAmountBasedOnSetBonusCount(GenericFunctions.getEquipment(p), p, ItemSet.ALIKAHN, 3, 3);
		hp+=ItemSet.TotalBaseAmountBasedOnSetBonusCount(GenericFunctions.getEquipment(p), p, ItemSet.COMET, 2, 2);
		hp+=ItemSet.TotalBaseAmountBasedOnSetBonusCount(GenericFunctions.getEquipment(p), p, ItemSet.CUPID, 2, 2);
		hp+=ItemSet.TotalBaseAmountBasedOnSetBonusCount(GenericFunctions.getEquipment(p), p, ItemSet.DONNER, 2, 2);
		hp+=ItemSet.TotalBaseAmountBasedOnSetBonusCount(GenericFunctions.getEquipment(p), p, ItemSet.RUDOLPH, 2, 2);
		hp+=ItemSet.TotalBaseAmountBasedOnSetBonusCount(GenericFunctions.getEquipment(p), p, ItemSet.OLIVE, 2, 2);
		hp+=ItemSet.TotalBaseAmountBasedOnSetBonusCount(GenericFunctions.getEquipment(p), p, ItemSet.DASHER, 3, 3);
		hp+=ItemSet.TotalBaseAmountBasedOnSetBonusCount(GenericFunctions.getEquipment(p), p, ItemSet.DANCER, 3, 3);
		hp+=ItemSet.TotalBaseAmountBasedOnSetBonusCount(GenericFunctions.getEquipment(p), p, ItemSet.PRANCER, 3, 3);
		hp+=ItemSet.TotalBaseAmountBasedOnSetBonusCount(GenericFunctions.getEquipment(p), p, ItemSet.VIXEN, 3, 3);
		hp+=ItemSet.TotalBaseAmountBasedOnSetBonusCount(GenericFunctions.getEquipment(p), p, ItemSet.BLITZEN, 3, 3);
		/*hp+=ItemSet.TotalBaseAmountBasedOnSetBonusCount(p, ItemSet.ALIKAHN, 4, 4)+
				ItemSet.TotalBaseAmountBasedOnSetBonusCount(p, ItemSet.DARNYS, 4, 4)+
				ItemSet.TotalBaseAmountBasedOnSetBonusCount(p, ItemSet.LORASAADI, 4, 4)+
				ItemSet.TotalBaseAmountBasedOnSetBonusCount(p, ItemSet.JAMDAK, 4, 4);*/

		if (PlayerMode.getPlayerMode(p)==PlayerMode.NORMAL) {
			hp+=10;
		}
		
		hp*=maxdeduction;

		p.resetMaxHealth();
			if (p.getHealth()>=hp) {
				p.setHealth(hp);
			}
		p.setMaxHealth(hp);
		if (!p.isDead()) {
			if (ratio==null) {
				p.setHealth(p.getHealth());
			} else {
				//TwosideKeeper.log("Hp is "+hp+". Ratio is "+ratio+". Setting to "+, loglv);
				p.setHealth(ratio*p.getMaxHealth());
			}
		}
		if (PlayerMode.getPlayerMode(p)==PlayerMode.SLAYER) {
			double slayermodehp = PlayerStructure.GetPlayerStructure(p).slayermodehp;
			if (ratio==null) {
				if (slayermodehp>p.getMaxHealth()) {
					slayermodehp = PlayerStructure.GetPlayerStructure(p).slayermodehp = p.getMaxHealth();
				}
			} else {
				slayermodehp = PlayerStructure.GetPlayerStructure(p).slayermodehp = ratio*p.getMaxHealth(); 
			}
			p.setHealth(slayermodehp);
		}
		p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(hp);
		p.setHealthScaled(false);
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
			@SuppressWarnings("deprecation")
			public void run() {
				if (Bukkit.getPlayer(pd2.name)!=null && pd2.target!=null) {
					String MonsterName = pd2.target.getType().toString().toLowerCase();
					if (pd2.target.getCustomName()!=null) {
						MonsterName = GenericFunctions.getDisplayName(pd2.target);
						if (GenericFunctions.getDisplayName(pd2.target)!=null &&
								!GenericFunctions.getDisplayName(pd2.target).contains("Leader") &&
								MonsterController.isZombieLeader(pd2.target)) {
							pd2.target.setCustomName(GenericFunctions.getDisplayName(pd2.target)+" Leader");
							MonsterName = GenericFunctions.getDisplayName(pd2.target);
						}
					} else {
						MonsterName = GenericFunctions.CapitalizeFirstLetters(MonsterName.replace("_", " "));
					}
					if (MonsterName.contains(ChatColor.RESET+" ")) {
						MonsterName = MonsterName.split(ChatColor.RESET+" ")[0];
					}
					final String finalMonsterName = MonsterName;
					String heartdisplay = "", remainingheartdisplay = "";
					int color1=0,color2=1;
					double health=pd2.target.getHealth();
					double maxhealth=pd2.target.getMaxHealth();
					final double orghealth = health;
					if (health>20) {
						while (health>20) {
							color1++;
							color2++;
							health-=20;
						}
					}
					for (int i=0;i<health/2;i++) {
						remainingheartdisplay+=Character.toString('♥');
					}
					if (maxhealth>20) {
						if (orghealth>20) {
							for (int i=0;i<10;i++) {
								heartdisplay+=Character.toString('♥');
							}
						} else {
							for (int i=0;i<10;i++) {
								heartdisplay+=Character.toString('♡');
							}
						}
					} else {
						for (int i=0;i<maxhealth/2;i++) {
							heartdisplay+=Character.toString('♡');
						}
					}
					
					ChatColor finalcolor = GetHeartColor(color1);
					ChatColor finalcolor2 = GetHeartColor(color2);
					String finalheartdisplay=finalcolor2+((finalcolor2==ChatColor.MAGIC)?remainingheartdisplay.replace((char)0x2665, 'A'):remainingheartdisplay)+finalcolor+((finalcolor==ChatColor.MAGIC)?heartdisplay.substring(0, heartdisplay.length()-remainingheartdisplay.length()).replace((char)0x2665, 'A'):heartdisplay.substring(0, heartdisplay.length()-remainingheartdisplay.length()));
					TwosideKeeper.log("Message 1 is "+message1, 5);
					if (pd2.target.getHealth()>2000) {
						finalheartdisplay=GetHeartColor(GetFactorialAmt(pd2.target.getHealth()))+FinalHealthDisplay(pd2.target.getHealth())+" / "+FinalHealthDisplay(pd2.target.getMaxHealth());
						p.sendTitle(message1, finalMonsterName+" "+finalheartdisplay+" "+ChatColor.RESET);
					} else {
						p.sendTitle(message1, finalMonsterName+" "+finalheartdisplay+" "+ChatColor.RESET+ChatColor.DARK_GRAY+"x"+(int)(pd2.target.getHealth()/20+1));
					}
				}}

			private String FinalHealthDisplay(double maxHealth) {
				DecimalFormat df = new DecimalFormat("0");
				if (maxHealth>=1000000) {
					return df.format(maxHealth/1000000)+"M";
				} else {
					return df.format(maxHealth/1000)+"k";
				}
			}

			private int GetFactorialAmt(double health) {
				double startingamt = health;
				int numb = 0;
				while (startingamt>1000) {
					startingamt/=1000;
					numb++;
				}
				return numb;
			}}
		,1);
		if (Bukkit.getPlayer(pd2.name)!=null) {
			if (pd.title_task!=-1) {
				Bukkit.getScheduler().cancelTask(pd.title_task);
				pd.title_task=-1;
			}
			pd.title_task=Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("TwosideKeeper"), new Runnable() {
				@SuppressWarnings("deprecation")
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
	
	static public void openItemCubeInventory(Inventory inv) {
    	//Check if this is an Item Cube inventory.
		//p.sendMessage("This is an Item Cube inventory.");
		int id = Integer.parseInt(inv.getTitle().split("#")[1]);
		List<ItemStack> itemcube_contents = itemCube_loadConfig(id);
		for (int i=0;i<inv.getSize();i++) {
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
			//log_messages.add(ChatColor.stripColor(logmessage));
			switch (loglv) {
				case 0: {
					//Only game breaking messages appear in level 0.
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"[ERROR]"+ChatColor.RESET+logmessage);
				}break;
				case 1: {
					//Only warning messages appear in level 1.
					Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW+"[WARNING]"+ChatColor.RESET+logmessage);
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
	/*
	public static void updateServer() {
		if (Bukkit.getOnlinePlayers().size()!=0) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("TwosideKeeper"), new Runnable() {
				@Override
				public void run() {
					Bukkit.getScheduler().runTaskAsynchronously(Bukkit.getPluginManager().getPlugin("TwosideKeeper"), pluginupdater);
					aPlugin.API.discordSendRawItalicized("The server is restarting in 1 minute for a plugin update!");
					Bukkit.broadcastMessage(ChatColor.YELLOW+"The server is restarting in 1 minute for a plugin update!");
				}
			},20*120);
			Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("TwosideKeeper"), new Runnable() {
				@Override
				public void run() {
					Bukkit.getScheduler().runTaskAsynchronously(Bukkit.getPluginManager().getPlugin("TwosideKeeper"), pluginupdater);
					aPlugin.API.discordSendRawItalicized("The server is restarting in 10 seconds!");
					Bukkit.broadcastMessage(ChatColor.RED+"The server is restarting in 10 seconds!");
				}
			},20*170);
		}
		Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("TwosideKeeper"), new Runnable() {
			@Override
			public void run() {
				Bukkit.getScheduler().runTask(Bukkit.getPluginManager().getPlugin("TwosideKeeper"), pluginupdater);
				Bukkit.savePlayers();
				aPlugin.API.discordSendRawItalicized("Server is shutting down...");
				for (int i=0;i<Bukkit.getWorlds().size();i++) {
					Bukkit.getWorlds().get(i).save();
				}
				Bukkit.shutdown();
			}
		},20*180*((Bukkit.getOnlinePlayers().size()==0)?0:1)+1);
		
	}*/
	
	public void showPlayerStats(Player p) {
		showPlayerStats(p,p,"");
	}
	
	public void showPlayerStats(Player p, CommandSender receiver) {
		showPlayerStats(p,receiver,"");
	}

	public void showPlayerStats(Player p,String additional) {
		showPlayerStats(p,p,additional);
	}
	
	public void showPlayerStats(Player p, CommandSender receiver, String additional) {
		PlayerStructure pd = (PlayerStructure)playerdata.get(p.getUniqueId());
		double store1=CustomDamage.CalculateDamageReduction(1,p,null);
		double store2=CustomDamage.getBaseWeaponDamage(p.getEquipment().getItemInMainHand(), p, null);
		pd.damagedealt=store2;
		pd.damagereduction=store1;
		DecimalFormat df = new DecimalFormat("0.0");
		boolean all = (additional.equalsIgnoreCase("all"));
		boolean equip = (additional.equalsIgnoreCase("equip"));
		if (all) {receiver.sendMessage("Habitat Quality: "+habitat_data.getHabitationLevel(p.getLocation()));}
		Arrow temporaryarrow = (Arrow)p.getWorld().spawnArrow(p.getLocation().add(0,1000000,0), new Vector(1,1,1), 0.6f, 12f);
		temporaryarrow.setShooter(p);
		if (PlayerMode.getPlayerMode(p)==PlayerMode.RANGER) {
			store2=CustomDamage.getBaseWeaponDamage(p.getEquipment().getItemInMainHand(), temporaryarrow, null);
		}
		receiver.sendMessage(ChatColor.GRAY+""+ChatColor.ITALIC+"Base Damage: "+ChatColor.RESET+""+ChatColor.DARK_PURPLE+df.format(store2));
		Chicken temporarychicken = (Chicken)p.getWorld().spawnEntity(p.getLocation().add(0,1000000,0), EntityType.CHICKEN); //Why are you so cruel to the chicken sig.
		HashMap<String,Double> origmap = (HashMap<String, Double>) pd.damagedata.breakdownlist.clone();
		double origdmg = pd.damagedata.actualtotaldmg;
		if (PlayerMode.getPlayerMode(p)==PlayerMode.RANGER) {
			receiver.sendMessage(ChatColor.GRAY+""+ChatColor.ITALIC+"Applied Damage: "+ChatColor.RESET+""+ChatColor.LIGHT_PURPLE+df.format(CustomDamage.CalculateDamage(store2,temporaryarrow,temporarychicken,null, "Test Damage")));
		} else {
			receiver.sendMessage(ChatColor.GRAY+""+ChatColor.ITALIC+"Applied Damage: "+ChatColor.RESET+""+ChatColor.LIGHT_PURPLE+df.format(CustomDamage.CalculateDamage(store2,p,temporarychicken,p.getEquipment().getItemInMainHand(), "Test Damage")));
		}
		pd.damagedata.actualtotaldmg=origdmg;
		pd.damagedata.breakdownlist=origmap;
		temporarychicken.remove();
		temporaryarrow.remove();
		double damagereduction = (1.0-store1)*100;
		if (all || damagereduction>0) {receiver.sendMessage(ChatColor.GRAY+""+ChatColor.ITALIC+"Damage Reduction: "+ChatColor.RESET+""+ChatColor.DARK_AQUA+df.format((1.0-store1)*100)+"%");}
		double lifestealamt = CustomDamage.calculateLifeStealAmount(p,p.getEquipment().getItemInMainHand())*100;
		if (all || lifestealamt>0) {receiver.sendMessage(ChatColor.GRAY+""+ChatColor.ITALIC+"Life Steal: "+ChatColor.RESET+""+ChatColor.DARK_AQUA+df.format(lifestealamt)+"%");}
		double critchance = (CustomDamage.calculateCriticalStrikeChance(p.getEquipment().getItemInMainHand(), p))*100;
		if (all || critchance>0) {receiver.sendMessage(ChatColor.GRAY+""+ChatColor.ITALIC+"Critical Strike Chance: "+ChatColor.RESET+""+ChatColor.DARK_AQUA+df.format(critchance)+"%");}
		double critdamage = CustomDamage.calculateCriticalStrikeMultiplier(p, p.getEquipment().getItemInMainHand())*100+100;
		if (all || (critdamage>200 && critchance>0)) {receiver.sendMessage(ChatColor.GRAY+""+ChatColor.ITALIC+"Crit Damage: "+ChatColor.RESET+""+ChatColor.DARK_AQUA+df.format(critdamage)+"%");}
		if (PlayerMode.isDefender(p)) {
			double dodgechance=0.0;
			if (!p.isBlocking()) {
				dodgechance+=ItemSet.GetTotalBaseAmount(GenericFunctions.getEquipment(p), p, ItemSet.SONGSTEEL)/100d;
			}
			if (all || dodgechance>0) {
				receiver.sendMessage(ChatColor.GRAY+""+ChatColor.ITALIC+"Block Chance: "+ChatColor.RESET+""+ChatColor.DARK_AQUA+df.format((CustomDamage.CalculateDodgeChance(p)+dodgechance)*100)+"%");
			}
		} else {
			double dodgechance = (CustomDamage.CalculateDodgeChance(p))*100;
			if (all || dodgechance>0) {receiver.sendMessage(ChatColor.GRAY+""+ChatColor.ITALIC+"Dodge Chance: "+ChatColor.RESET+""+ChatColor.DARK_AQUA+df.format(dodgechance)+"%");}
		}
		double debuffresistchance = CustomDamage.CalculateDebuffResistance(p);
		if (all || debuffresistchance>0) {receiver.sendMessage(ChatColor.GRAY+""+ChatColor.ITALIC+"Debuff Resistance: "+ChatColor.RESET+""+ChatColor.DARK_AQUA+df.format(debuffresistchance)+"%");}
		double cooldownreduction = CustomDamage.calculateCooldownReduction(p);
		if (all || cooldownreduction>0) {receiver.sendMessage(ChatColor.GRAY+""+ChatColor.ITALIC+"Cooldown Reduction: "+ChatColor.RESET+""+ChatColor.DARK_AQUA+df.format(cooldownreduction*100)+"%");}
		TextComponent f = new TextComponent(ChatColor.GRAY+""+ChatColor.ITALIC+"Current Mode: ");
		f.addExtra(GenericFunctions.PlayerModeName(p));
		if (receiver instanceof Player) {
			((Player)receiver).spigot().sendMessage(f);
		} else {
			receiver.sendMessage(f.toPlainText());
		}
		if (equip || all) {
			TextComponent msg = DisplayPerks(p.getEquipment().getItemInMainHand(),"Weapon",p,0);if (!msg.toPlainText().equalsIgnoreCase("")) {
				if (receiver instanceof Player) {((Player)receiver).spigot().sendMessage(msg);} else {receiver.sendMessage(msg.toPlainText());}};
			msg = DisplayPerks(p.getEquipment().getHelmet(),"Helmet",p,903);if (!msg.toPlainText().equalsIgnoreCase("")) {if (receiver instanceof Player) {((Player)receiver).spigot().sendMessage(msg);} else {receiver.sendMessage(msg.toPlainText());}};
			msg = DisplayPerks(p.getEquipment().getChestplate(),"Chestplate",p,902);if (!msg.toPlainText().equalsIgnoreCase("")) {if (receiver instanceof Player) {((Player)receiver).spigot().sendMessage(msg);} else {receiver.sendMessage(msg.toPlainText());}};
			msg = DisplayPerks(p.getEquipment().getLeggings(),"Legging",p,901);if (!msg.toPlainText().equalsIgnoreCase("")) {if (receiver instanceof Player) {((Player)receiver).spigot().sendMessage(msg);} else {receiver.sendMessage(msg.toPlainText());}};
			msg = DisplayPerks(p.getEquipment().getBoots(),"Boot",p,900);if (!msg.toPlainText().equalsIgnoreCase("")) {if (receiver instanceof Player) {((Player)receiver).spigot().sendMessage(msg);} else {receiver.sendMessage(msg.toPlainText());}};
		}
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
			HashMap<ArtifactAbility,Integer> enchants = ArtifactAbility.getEnchantments(item);
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
				tc1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ComponentBuilder(WordUtils.wrap(ArtifactAbility.displayDescription(ab, item.getEnchantmentLevel(Enchantment.LUCK), (int)enchants.values().toArray()[i], CustomDamage.getBaseWeaponDamage(item, p, null)),ArtifactAbility.LINE_SIZE,"\n",true)).create()));
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

    private void displayTitle(Player shooter, PlayerStructure pd, int flags) {
    	if (!pd.damagelogging) {
			updateTitle(shooter,CustomDamage.isFlagSet(flags, CustomDamage.IS_HEADSHOT),CustomDamage.isFlagSet(flags, CustomDamage.IS_PREEMPTIVE));
		} else {
			ChatColor col = ChatColor.AQUA;
			if (CustomDamage.isFlagSet(flags, CustomDamage.IS_CRIT)) {
				col = ChatColor.YELLOW;
			} else
			if (CustomDamage.isFlagSet(flags, CustomDamage.IS_PREEMPTIVE)) {
				col = ChatColor.BLUE;
			} else 
			if (CustomDamage.isFlagSet(flags, CustomDamage.IS_HEADSHOT)) {
				col = ChatColor.DARK_RED;
			}
			DecimalFormat df = new DecimalFormat("0.00");
			updateTitle(shooter,col+""+df.format(pd.damagedealt));
		}
	}
	
	 public static void logHealth(final LivingEntity l,final double hp,final double dmg, Entity damager) {
 		Bukkit.getScheduler().scheduleSyncDelayedTask(TwosideKeeper.plugin, new Runnable() {
 			public void run() {
 				DecimalFormat df = new DecimalFormat("0.00");
 				if (l!=null && damager!=null) {
 					TwosideKeeper.log(GenericFunctions.GetEntityDisplayName(damager)+"->"+GenericFunctions.GetEntityDisplayName(l)+" dealt "+dmg+" damage."+ChatColor.AQUA+" HP: "+ChatColor.YELLOW+df.format(hp)+"->"+df.format(l.getHealth()), COMBAT_DEBUG);
 				}
 			}
 		},1);
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
    		aPlugin.API.discordSendRawItalicized(SERVER_TYPE.GetServerName()+"Server has been restarted.\nRunning v."+Bukkit.getPluginManager().getPlugin("TwosideKeeper").getDescription().getVersion()+" of TwosideKeeper\nRunning v"+Bukkit.getPluginManager().getPlugin("aPlugin").getDescription().getVersion()+" of Jobs.");
    		if (CHRISTMASEVENT_ACTIVATED) {
    			aPlugin.API.discordSendRaw("___________________");
    			aPlugin.API.discordSendRaw("**Christmas Holiday Event** is currently active! Please visit <> for full details.");
    		}
    	}
	}
	
	public static void breakdownItem(ItemStack item, Player p) {
		SoundUtils.playLocalSound(p, Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
		p.sendMessage(ChatColor.DARK_RED+"Your "+ChatColor.YELLOW+((item.hasItemMeta() && item.getItemMeta().hasDisplayName())?item.getItemMeta().getDisplayName():GenericFunctions.UserFriendlyMaterialName(item))+ChatColor.DARK_RED+" has broken!");
	}
}