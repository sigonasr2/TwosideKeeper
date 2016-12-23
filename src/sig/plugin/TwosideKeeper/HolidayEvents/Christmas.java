package sig.plugin.TwosideKeeper.HolidayEvents;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowman;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import aPlugin.API.Chests;
import aPlugin.DropItem;
import aPlugin.DropMaterial;
import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.BiomeUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.ItemUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.TextUtils;

public class Christmas {
	public final static String CHRISTMAS_TAG = ChatColor.BLUE+"Christmas Event Item"+ChatColor.RESET;
	
	public static void insertChristmasTag(ItemStack item) {
		ItemUtils.addLore(item, CHRISTMAS_TAG);
	}
	
	public static ItemStack getRocketBoosterItem() {
		ItemStack item  = new ItemStack(Material.FIREWORK);
		ItemUtils.setDisplayName(item, ChatColor.DARK_AQUA+"Rocket Booster");
		insertChristmasTag(item);
		ItemUtils.addLore(item, "A Launcher that shoots you");
		ItemUtils.addLore(item, "extremely fast in your facing");
		ItemUtils.addLore(item, "direction. Slowly recharges");
		ItemUtils.addLore(item, "over time if kept on the hotbar.");
		item.addUnsafeEnchantment(Enchantment.LUCK, 0);
		ItemUtils.hideEnchantments(item);
		return item.clone();
	}
	
	public static boolean isRocketBoosterItem(ItemStack item) {
		return ItemUtils.isValidLoreItem(item) && item.getType()==Material.FIREWORK && ItemUtils.LoreContains(item, CHRISTMAS_TAG);
	}
	
	public static ItemStack getCookieItem() {
		ItemStack item = new ItemStack(Material.COOKIE);
		ItemUtils.setDisplayName(item, ChatColor.AQUA+"Holiday Cookie");
		ItemUtils.addLore(item, ChatColor.WHITE+"Heals"+ChatColor.YELLOW+" 256 "+ChatColor.RESET+"health.");
		ItemUtils.addLore(item, ChatColor.GRAY+"- Strength II (15:00)");
		ItemUtils.addLore(item, ChatColor.GRAY+"- Resistance II (15:00)");
		ItemUtils.addLore(item, ChatColor.GRAY+"- Regeneration III (15:00)");
		ItemUtils.addLore(item, ChatColor.GRAY+"- Saturation (5:00)");
		ItemUtils.addLore(item, "");
		insertChristmasTag(item);
		ItemUtils.addLore(item, "A sweet and delicious cookie");
		ItemUtils.addLore(item, "that provides wonderful buffs.");
		return item.clone();
	}
	
	public static boolean isCookieItem(ItemStack item) {
		return ItemUtils.isValidLoreItem(item) && item.getType()==Material.COOKIE && ItemUtils.LoreContains(item, CHRISTMAS_TAG);
	}
	
	public static ItemStack getChristmasEventToken() {
		ItemStack item = new ItemStack(Material.COMMAND_REPEATING);
		ItemUtils.setDisplayName(item, ChatColor.AQUA+"Christmas Token");
		insertChristmasTag(item);
		ItemUtils.addLore(item, "A token used to obtain special");
		ItemUtils.addLore(item, "treats and goodies for the Christmas");
		ItemUtils.addLore(item, "event. Turn in tokens to the ");
		ItemUtils.addLore(item, "Twoside Bank Tree.");
		ItemUtils.addLore(item, "");
		return item.clone();
	}
	
	public static boolean isChristmasEventToken(ItemStack item) {
		return ItemUtils.isValidLoreItem(item) && item.getType()==Material.COMMAND_REPEATING && ItemUtils.LoreContains(item, CHRISTMAS_TAG);
	}
	
	public static ItemStack getChristmasTreeSchematic() {
		ItemStack item = new ItemStack(Material.PAPER);
		ItemUtils.setDisplayName(item, ChatColor.AQUA+"Christmas Tree Schematic");
		insertChristmasTag(item);
		ItemUtils.addLore(item, "A blueprint for the creation of an");
		ItemUtils.addLore(item, "excellent Christmas Tree. Must be");
		ItemUtils.addLore(item, "placed on a proper Tree plot to");
		ItemUtils.addLore(item, "grow.");
		return item.clone();
	}
	
	public static boolean isChristmasTreeSchematic(ItemStack item) {
		return ItemUtils.isValidLoreItem(item) && item.getType()==Material.PAPER && ItemUtils.LoreContains(item, CHRISTMAS_TAG);
	}
	
	public static void InitializeChristmasBox() {
		Chests c = aPlugin.API.Chests.LOOT_CUSTOM_2;
		c.addDrop(new DropMaterial(Material.COAL,1,4,10));
		c.addDrop(new DropMaterial(Material.IRON_INGOT,1,4,10));
		c.addDrop(new DropMaterial(Material.GOLD_INGOT,1,4,10));
		c.addDrop(new DropMaterial(Material.REDSTONE,1,4,10));
		c.addDrop(new DropMaterial(Material.GLOWSTONE_DUST,1,4,10));
		c.addDrop(new DropMaterial(Material.QUARTZ,1,4,10));
		c.addDrop(new DropMaterial(Material.INK_SACK,1,4,10,(short)4));
		c.addDrop(new DropMaterial(Material.DIAMOND,1,4,10));
		c.addDrop(new DropMaterial(Material.EMERALD,1,4,10));
		c.addDrop(new DropMaterial(Material.COAL_BLOCK,1,4,5));
		c.addDrop(new DropMaterial(Material.IRON_BLOCK,1,4,5));
		c.addDrop(new DropMaterial(Material.GOLD_BLOCK,1,4,5));
		c.addDrop(new DropMaterial(Material.REDSTONE_BLOCK,1,4,5));
		c.addDrop(new DropMaterial(Material.GLOWSTONE,1,4,5));
		c.addDrop(new DropMaterial(Material.QUARTZ_BLOCK,1,4,5));
		c.addDrop(new DropMaterial(Material.LAPIS_BLOCK,1,4,5));
		c.addDrop(new DropMaterial(Material.DIAMOND_BLOCK,1,4,5));
		c.addDrop(new DropMaterial(Material.EMERALD_BLOCK,1,4,5));
		c.addDrop(new DropItem(getCookieItem(),1,3,5));
		c.addDrop(new DropItem(getChristmasEventToken(),10));
		//aPlugin.API.getChestItem(c);
	}
	
	public static boolean RunPlayerInteractEvent(PlayerInteractEvent ev) {
		if (TwosideKeeper.CHRISTMASEVENT_ACTIVATED) {
			Player p = ev.getPlayer();
			if (ev.getAction()==Action.RIGHT_CLICK_BLOCK) {
				Block b = ev.getClickedBlock();
				if (b.getType()==Material.SMOOTH_BRICK && isChristmasTreeSchematic(p.getEquipment().getItemInMainHand())) {
					//Remove the item from the player.
					p.getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
					//Plant a tree.
					TreeBuilder.BuildNewTree(b.getLocation().add(15,1,15), 12, 13, 0);
					p.sendMessage(ChatColor.AQUA+"You place the schematic down...");
					Bukkit.getScheduler().scheduleSyncDelayedTask(TwosideKeeper.plugin, ()->{p.sendMessage(ChatColor.GREEN+" Magic seems to activate inside the ground.");},5);
					Bukkit.getScheduler().scheduleSyncDelayedTask(TwosideKeeper.plugin, ()->{b.setType(Material.HOPPER);},2);
					Block babove = b.getRelative(0, 1, 0);
					Block bbelow = b.getRelative(0, -1, 0);
					bbelow.setType(Material.CHEST);
					babove.setType(Material.SIGN_POST);
					Sign s = (Sign)babove.getState();
					s.setLine(1, RandomDarkColor()+p.getName()+"'s");
					s.setLine(2, "Tree");
					s.setRawData((byte)4);
					s.update();
					ev.setUseInteractedBlock(Result.DENY);
					ev.setCancelled(true);
					return false;
				}
			}
		}
		return true;
	}

	private static ChatColor RandomDarkColor() {
		ChatColor[] choices = new ChatColor[]{ChatColor.DARK_AQUA,ChatColor.DARK_BLUE,ChatColor.DARK_GRAY,ChatColor.DARK_GREEN,ChatColor.DARK_PURPLE,ChatColor.DARK_RED,ChatColor.GOLD};
		return choices[(int)(Math.random()*choices.length)];
	}

	public static void ChristmasHeartbeat() {
		int range = 8;
		for (Player p : Bukkit.getOnlinePlayers()) {
			//Check a random block around the player.
			int blockx = (int)(Math.random()*(range*2))-range;
			int blockz = (int)(Math.random()*(range*2))-range;
			Block b = p.getWorld().getHighestBlockAt(p.getLocation().getBlockX()+blockx, p.getLocation().getBlockZ()+blockz);
			Block bbelow = b.getRelative(0, -1, 0);
			if (TwosideKeeper.CHRISTMASEVENT_ACTIVATED) {
				if (b.getType()==Material.AIR && bbelow.getType().isSolid() && !bbelow.getType().name().contains("STEP") && b.getTemperature()<=0.95) {
					b.setType(Material.SNOW);
					b.setData((byte)0);
					if (TwosideKeeper.last_snow_golem+TwosideKeeper.SNOW_GOLEM_COOLDOWN<TwosideKeeper.getServerTickTime()) {
						//There might be a chance to spawn one of these.
						List<Entity> nearbyents = p.getNearbyEntities(32, 24, 32);
						boolean snowmannearby=false;
						for (Entity ent : nearbyents) {
							if (ent instanceof Snowman) {
								snowmannearby=true;
								break;
							}
						}
						if (!snowmannearby) {
							//Spawn one on the snow.
							Snowman snowy = (Snowman)p.getWorld().spawnEntity(b.getLocation().add(0,2,0), EntityType.SNOWMAN);
							snowy.setCustomName(RandomSnowmanName());
							snowy.setMaxHealth(Math.random()*100+20);
							snowy.setHealth(snowy.getMaxHealth());
							snowy.setAI(true);
						}
					}
				} else
				if (b.getType()==Material.SNOW && b.getData()<7) {
					b.setData((byte)(b.getData()+1));
				}
			}/* else { //Unnecessary. Snow will automatically melt.
				if (b.getType()==Material.SNOW && b.getTemperature()>0.15) {
					b.setType(Material.AIR);
				}
			}*/
		}
	}

	private static String RandomSnowmanName() {
		String[] name1 = {"Jingle","Merry","Bells",
				"Tinkle","Angel","Twinkle",
				"Rosie","Holly","Berry",
				"Festive","Candy","Magic",
				"Sparkle","Sugarplum","Joy",
				"Tinsel","Robin","Cookie",
				"Hope","Sweetie","Teddy",
				"Jolly","Cosy","Sherry",
				"Eve","Pinky"};
		String[] name2 = {"McSnowy","McSlushy","McChilly",
				"McGlisten","McSparkle","McFrosty",
				"McFreeze","McSnowballs","McIcicles",
				"McBlizzard","McSparkles","McSnowflake"};
		
		ChatColor col = TextUtils.RandomColor();
		
		return col+name1[(int)(Math.random()*name1.length)]+" "+name2[(int)(Math.random()*name2.length)];
	}

	public static boolean ChristmasDamageEvent(EntityDamageEvent ev) {
		if (TwosideKeeper.CHRISTMASEVENT_ACTIVATED) {
			if (ev.getCause()==DamageCause.MELTING && (ev.getEntity() instanceof Snowman)) {
				ev.setCancelled(true);
				return false;
			}
		}
		return true;
	}
}