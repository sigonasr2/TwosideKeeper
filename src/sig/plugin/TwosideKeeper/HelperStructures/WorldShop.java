package sig.plugin.TwosideKeeper.HelperStructures;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.block.banner.Pattern;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.Repairable;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import sig.plugin.TwosideKeeper.Artifact;
import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.WorldShopManager;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.BlockUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.ItemUtils;

public class WorldShop {
	ItemStack item;
	String owner;
	double price;
	int amt;
	int storedamt = 0;
	int id;
	Location loc;
	public static final double DEFAULTPRICE = 99.99;
	public static HashMap<String,Double> pricelist = new HashMap<String,Double>();
	public static String price_file = TwosideKeeper.plugin.getDataFolder()+"/ShopPrices.data";
	
	public WorldShop (ItemStack i, int amt, int storedamt, double p, String player, int shopID, Location shopLoc) {
		this.item=i;
		this.price=p;
		this.owner=player;
		this.amt = amt;
		this.storedamt = storedamt;
		this.id = shopID;
		this.loc = shopLoc;
	}
	
	public String GetItemName() {
		if (this.item.hasItemMeta() &&
				this.item.getItemMeta().hasDisplayName()) {
			return this.item.getItemMeta().getDisplayName();
		} else {
			return GenericFunctions.UserFriendlyMaterialName(this.item);
		}
	}
	public int GetStoredAmount() {
		return this.storedamt;
	}
	public void UpdateAmount(int amt) {
		this.amt=amt;
	}
	public void UpdateStoredAmount(int newamt) {
		this.storedamt=newamt;
	}
	public void UpdateUnitPrice(double price) {
		this.price=price;
	}
	public void UpdateItem(ItemStack item) {
		this.item=item;
	}
	public void UpdateLoc(Location loc) {
		this.loc=loc;
	}

	public Location getLoc() {
		return loc;
	}
	public String GetLocString() {
		return loc.getWorld().getName()+","+loc.getBlockX()+","+loc.getBlockY()+","+loc.getBlockZ();
	}
	
	public ItemStack GetItem() {
		return item;
	}
	public double GetUnitPrice() {
		if (owner.equalsIgnoreCase("admin")) {
			return GetWorldShopPrice(item);
		} else {
			return price;
		}
	}
	private double GetWorldShopPrice(ItemStack item) {
		String searchstring = item.getType().name();
		if (item.getDurability()!=0) {
			searchstring = item.getType().name()+","+item.getDurability();
		}
		if (!pricelist.containsKey(searchstring)) {
			//Create a new key for this item.
			TwosideKeeper.log("Item "+ChatColor.YELLOW+item.toString()+ChatColor.RESET+" does not have a price set yet! Adding to price list!", 1);
			AddEntryToFile(item);
		}
		double price = 0.0;
		if (item.getDurability()!=0) {
			price = pricelist.get(searchstring);
		} else {
			price = pricelist.get(item.getType().name());
		}
		if (TwosideKeeper.DEAL_OF_THE_DAY_ITEM.isSimilar(item)) {
			return price*0.8;
		}
		return ModifyPriceBasedOnLocation(price);
	}
	
	public static double getBaseWorldShopPrice(ItemStack item) {
		String searchstring = item.getType().name();
		if (item.getDurability()!=0) {
			searchstring = item.getType().name()+","+item.getDurability();
		}
		if (!pricelist.containsKey(searchstring)) {
			//Create a new key for this item.
			TwosideKeeper.log("Item "+ChatColor.YELLOW+item.toString()+ChatColor.RESET+" does not have a price set yet! Adding to price list!", 1);
			AddEntryToFile(item);
		}
		double price = 0.0;
		if (item.getDurability()!=0) {
			price = pricelist.get(searchstring);
		} else {
			price = pricelist.get(item.getType().name());
		}
		return Math.round(price*100)/100d;
	}

	private double ModifyPriceBasedOnLocation(double price) {
		if (!loc.getWorld().equals(TwosideKeeper.TWOSIDE_LOCATION.getWorld())) {
			//This is in another world. Automatically increase price by x4.
			price *= 4;
		} else {
			//Price based on distance from town.
			double dist = (TwosideKeeper.TWOSIDE_LOCATION.distance(loc));
			if (dist>1000) {
				double mult = dist/10000d;
				price += price*mult;
			}
		}
		if (loc.getWorld().getName().equalsIgnoreCase("world")) {
			if (loc.getBlockY()<=48) {
				price *= 1.5;
			}
			if (loc.getBlockY()<=32) {
				price *= 1.5;
			}
			if (loc.getBlockY()<=16) {
				price *= 1.5;
			}
		}
		return Math.round(price*100)/100d;
	}

	private static void AddEntryToFile(ItemStack item) {
		File file = new File(price_file);
		
		if (!file.exists()) {
			PopulateNewFile(file);
		}
		try(
				FileWriter fw = new FileWriter(price_file, true);
			    BufferedWriter bw = new BufferedWriter(fw);)
			{
				if (item.getDurability()!=0) {
					bw.write(item.getType().name()+","+item.getDurability()+","+DEFAULTPRICE);
					bw.newLine();
					pricelist.put(item.getType().name()+","+item.getDurability(), DEFAULTPRICE);
					bw.close();
				} else {
					bw.write(item.getType().name()+","+DEFAULTPRICE);
					bw.newLine();
					pricelist.put(item.getType().name(), DEFAULTPRICE);
					bw.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	public int getID() {
		return id;
	}
	public int GetAmount() {
		if (owner.equalsIgnoreCase("admin")) {
			return 10000;
		} else {
			return amt;
		}
	}
	public String GetOwner() {
		return owner;
	}
	
	public String toString() {
		return "WorldShop:{Item:"+item.toString()+",Price:"+price+",Amount:"+amt+",Stored Amount:"+storedamt+",Owner:"+owner+"}";
	}
	
	public static String GetItemInfo(ItemStack item) {
		//Gets all the info about this item in one gigantic string. (Separated by new lines. Useful for tellraw()).
		String message = "";
		if (GenericFunctions.isArtifactEquip(item) && !GenericFunctions.isArtifactArmor(item) /*Artifact armor already has this info displayed.*/) {
			if (item.hasItemMeta() &&
					item.getItemMeta().hasDisplayName()) {
					message+="\n"+ChatColor.GOLD+ChatColor.BOLD+"T"+item.getEnchantmentLevel(Enchantment.LUCK)+ChatColor.RESET+ChatColor.GOLD+" "+GenericFunctions.UserFriendlyMaterialName(item.getType())+" Artifact \n";
			}
		} else
		if (!GenericFunctions.isArtifactArmor(item) && item.hasItemMeta() &&
				item.getItemMeta().hasDisplayName()) {
				message+="\n"+ChatColor.DARK_GRAY+"Item Type: "+ChatColor.ITALIC+ChatColor.GRAY+GenericFunctions.UserFriendlyMaterialName(item.getType(),item.getDurability())+"\n";
		}
		if (item.hasItemMeta() && !item.getItemMeta().getItemFlags().contains(ItemFlag.HIDE_ENCHANTS) && !GenericFunctions.isArtifactArmor(item)) {
			for (int i=0;i<Enchantment.values().length;i++) {
				if (item.containsEnchantment(Enchantment.values()[i])) {
					message+="\n"+ChatColor.GRAY+getRealName(Enchantment.values()[i])+" "+toRomanNumeral(item.getEnchantmentLevel(Enchantment.getByName(Enchantment.values()[i].getName()))); //This is an enchantment we have.
				}
			}
		}
		if (item.getType()==Material.ENCHANTED_BOOK) {
			if (item.getItemMeta() instanceof EnchantmentStorageMeta) {
				EnchantmentStorageMeta e = (EnchantmentStorageMeta)item.getItemMeta();
				if (!e.getItemFlags().contains(ItemFlag.HIDE_ENCHANTS)) {
					for (int i=0;i<Enchantment.values().length;i++) {
						if (e.hasStoredEnchant((Enchantment.values()[i]))) {
							message+="\n"+ChatColor.GRAY+getRealName(Enchantment.values()[i])+" "+toRomanNumeral(e.getStoredEnchantLevel(Enchantment.getByName(Enchantment.values()[i].getName()))); //This is an enchantment we have.
						}
					}
				}
			}
		}
		if (item.getType()==Material.WRITTEN_BOOK) {
			if (item.getItemMeta() instanceof BookMeta) {
				BookMeta book = (BookMeta)item.getItemMeta();
				message+="\n"+ChatColor.WHITE+book.getTitle();
				message+="\n"+ChatColor.GRAY+"by "+book.getAuthor();
				message+="\n"+ChatColor.GRAY+book.getPageCount()+" page"+(book.getPageCount()!=1?"s":"");
			}
		}
		if (item.getType()==Material.POTION || item.getType()==Material.SPLASH_POTION || item.getType()==Material.LINGERING_POTION || item.getType()==Material.TIPPED_ARROW) {
			if (item.getItemMeta() instanceof PotionMeta) {
				PotionMeta pot = (PotionMeta)item.getItemMeta();
				if (!pot.getItemFlags().contains(ItemFlag.HIDE_POTION_EFFECTS)) {
					List<PotionEffect> effects = pot.getCustomEffects();
					PotionData baseeffects = pot.getBasePotionData();

					if (baseeffects!=null) { //Try this instead. It might be a legacy potion.
						int duration = GenericFunctions.getBasePotionDuration(pot.getBasePotionData());
						String power = (pot.getBasePotionData().isUpgraded()?"II":"");
						if (item.getType()==Material.TIPPED_ARROW) {
							duration/=8;
						}
						switch (pot.getBasePotionData().getType()) {
							case FIRE_RESISTANCE:
								message+="\n"+ChatColor.BLUE+"Fire Resistance ("+toReadableDuration(duration)+")";
								break;
							case INSTANT_DAMAGE:
								message+="\n"+ChatColor.RED+"Instant Damage "+power;
								break;
							case INSTANT_HEAL:
								message+="\n"+ChatColor.BLUE+"Instant Health "+power;
								break;
							case INVISIBILITY:
								message+="\n"+ChatColor.BLUE+"Invisibility ("+toReadableDuration(duration)+")";
								break;
							case JUMP:
								message+="\n"+ChatColor.BLUE+"Leaping "+power+" ("+toReadableDuration(duration)+")";
								break;
							case LUCK:
								message+="\n"+ChatColor.BLUE+"Luck ("+toReadableDuration(duration)+")";
								break;
							case NIGHT_VISION:
								message+="\n"+ChatColor.BLUE+"Night Vision ("+toReadableDuration(duration)+")";
								break;
							case POISON:
								message+="\n"+ChatColor.RED+"Poison "+power +" ("+toReadableDuration(duration)+")";
								break;
							case REGEN:
								message+="\n"+ChatColor.BLUE+"Regeneration "+power+" ("+toReadableDuration(duration)+")";
								break;
							case SLOWNESS:
								message+="\n"+ChatColor.RED+"Slowness ("+toReadableDuration(duration)+")";
								break;
							case SPEED:
								message+="\n"+ChatColor.BLUE+"Speed "+power +" ("+toReadableDuration(duration)+")";
								break;
							case STRENGTH:
								message+="\n"+ChatColor.BLUE+"Strength "+power +" ("+toReadableDuration(duration)+")";
								break;
							case WATER_BREATHING:
								message+="\n"+ChatColor.BLUE+"Water Breathing ("+toReadableDuration(duration)+")";
								break;
							case WEAKNESS:
								message+="\n"+ChatColor.RED+"Weakness ("+toReadableDuration(duration)+")";
								break;
						default:
							break;
						}
					}
					
					for (int i=0;i<effects.size();i++) {
						int duration = effects.get(i).getDuration();
						if (item.getType()==Material.TIPPED_ARROW) {
							duration/=8;
						}
						message+="\n"+(isBadPotionEffect(effects.get(i).getType())?ChatColor.RED:ChatColor.BLUE)+GenericFunctions.UserFriendlyPotionEffectTypeName(effects.get(i).getType())+" "+toRomanNumeral(effects.get(i).getAmplifier()+1)+ ((effects.get(i).getAmplifier()+1>0)?" ":"")+"("+toReadableDuration(duration)+")";
					}
				}
			}
		}
		
		if (item.getType()==Material.BANNER || item.getType()==Material.SHIELD) {
			ItemMeta meta = item.getItemMeta();
			
			if (item.getType()==Material.SHIELD) {
				BlockStateMeta bmeta = (BlockStateMeta) meta;
                Banner banner = (Banner) bmeta.getBlockState();
                List<Pattern> patterns = banner.getPatterns();
                DyeColor color = banner.getBaseColor();
                ItemStack newbanner = new ItemStack(Material.BANNER);
                BannerMeta newban = (BannerMeta)(newbanner.getItemMeta());
                newban.setBaseColor(color);
                newban.setPatterns(patterns);
                newbanner.setItemMeta(newban);
                meta = newbanner.getItemMeta();
			}
			
			if (meta instanceof BannerMeta) {
				BannerMeta banner = (BannerMeta)meta;
				/* Code to generate banners with ENUMs listed respective to the patterns applied in the lore.
				for (int j=0;j<PatternType.values().length;j+=6) {
					ItemStack newbanner = new ItemStack(Material.BANNER);
					BannerMeta banner_m = (BannerMeta)newbanner.getItemMeta();
					banner_m.addPattern(new Pattern(DyeColor.WHITE,PatternType.values()[j]));
					banner_m.addPattern(new Pattern(DyeColor.WHITE,PatternType.values()[j+1]));
					banner_m.addPattern(new Pattern(DyeColor.WHITE,PatternType.values()[j+2]));
					if (j+3<PatternType.values().length) {
						banner_m.addPattern(new Pattern(DyeColor.WHITE,PatternType.values()[j+3]));
						banner_m.addPattern(new Pattern(DyeColor.WHITE,PatternType.values()[j+4]));
						banner_m.addPattern(new Pattern(DyeColor.WHITE,PatternType.values()[j+5]));
					}
					List<String> lore = new ArrayList<String>();
					lore.add(PatternType.values()[j].name());
					lore.add(PatternType.values()[j+1].name());
					lore.add(PatternType.values()[j+2].name());
					if (j+3<PatternType.values().length) {
						lore.add(PatternType.values()[j+3].name());
						lore.add(PatternType.values()[j+4].name());
						lore.add(PatternType.values()[j+5].name());
					}
					banner_m.setLore(lore);
					newbanner.setItemMeta(banner_m);
					Bukkit.getPlayer("sigonasr2").getInventory().addItem(newbanner);
				}*/
				for (int i=0;i<banner.getPatterns().size();i++) {
					String color = GenericFunctions.CapitalizeFirstLetters(banner.getPatterns().get(i).getColor().name().replace("_", " "));
					String pattern_name = "";
					switch (banner.getPatterns().get(i).getPattern()) {
						case BORDER:
							pattern_name="Bordure";
							break;
						case BRICKS:
							pattern_name="Field Masoned";
							break;
						case CIRCLE_MIDDLE:
							pattern_name="Roundel";
							break;
						case CREEPER:
							pattern_name="Creeper Charge";
							break;
						case CROSS:
							pattern_name="Saltire";
							break;
						case CURLY_BORDER:
							pattern_name="Bordure Indented";
							break;
						case DIAGONAL_LEFT:
							pattern_name="Per Bend Sinister";
							break;
						case DIAGONAL_LEFT_MIRROR:
							pattern_name="Per Bend Inverted";
							break;
						case DIAGONAL_RIGHT:
							pattern_name="Per Bend Sinister Inverted";
							break;
						case DIAGONAL_RIGHT_MIRROR:
							pattern_name="Per Bend";
							break;
						case FLOWER:
							pattern_name="Flower Charge";
							break;
						case GRADIENT:
							pattern_name="Gradient";
							break;
						case GRADIENT_UP:
							pattern_name="Base Gradient";
							break;
						case HALF_HORIZONTAL:
								pattern_name="Per Fess";
							break;
						case HALF_HORIZONTAL_MIRROR:
							pattern_name="Per Fess Inverted";
							break;
						case HALF_VERTICAL:
							pattern_name="Per Pale";
							break;
						case HALF_VERTICAL_MIRROR:
							pattern_name="Per Pale Inverted";
							break;
						case MOJANG:
							pattern_name="Thing";
							break;
						case RHOMBUS_MIDDLE:
							pattern_name="Lozenge";
							break;
						case SKULL:
							pattern_name="Skull Charge";
							break;
						case SQUARE_BOTTOM_LEFT:
							pattern_name="Base Dexter Canton";
							break;
						case SQUARE_BOTTOM_RIGHT:
							pattern_name="Base Sinister Canton";
							break;
						case SQUARE_TOP_LEFT:
							pattern_name="Chief Dexter Canton";
							break;
						case SQUARE_TOP_RIGHT:
							pattern_name="Chief Sinister Canton";
							break;
						case STRAIGHT_CROSS:
							pattern_name="Cross";
							break;
						case STRIPE_BOTTOM:
								pattern_name="Base Fess";
							break;
						case STRIPE_CENTER:
							pattern_name="Pale";
							break;
						case STRIPE_DOWNLEFT:
							pattern_name="Bend Sinister";
							break;
						case STRIPE_DOWNRIGHT:
							pattern_name="Bend";
							break;
						case STRIPE_LEFT:
							pattern_name="Pale Dexter Fess";
							break;
						case STRIPE_MIDDLE:
								pattern_name="Fess";
							break;
						case STRIPE_RIGHT:
								pattern_name="Pale Sinister";
							break;
						case STRIPE_SMALL:
							pattern_name="Paly";
							break;
						case STRIPE_TOP:
							pattern_name="Chief Fess";
							break;
						case TRIANGLES_BOTTOM:
							pattern_name="Base Indented";
							break;
						case TRIANGLES_TOP:
							pattern_name="Chief Indented";
							break;
						case TRIANGLE_BOTTOM:
							pattern_name="Chevron";
							break;
						case TRIANGLE_TOP:
							pattern_name="Inverted Chevron";
							break;
						default:
							pattern_name=GenericFunctions.CapitalizeFirstLetters(banner.getPatterns().get(i).getPattern().name().replace("_", " "));
							break;
					
					}
					message+="\n"+ChatColor.GRAY+color+" "+pattern_name;
				}
			}
		}
		
		if (item.hasItemMeta() &&
				item.getItemMeta().hasLore()) {
			for (int i=0;i<item.getItemMeta().getLore().size();i++) {
				message+="\n"+item.getItemMeta().getLore().get(i);
			}
		}
		
		if (item.getType().toString().contains("RECORD")) {
			switch (item.getType()) {
				case GREEN_RECORD:{
					message+="\n"+ChatColor.GRAY+"C418 - cat";
				}break;
				case GOLD_RECORD:{
					message+="\n"+ChatColor.GRAY+"C418 - 13";
				}break;
				case RECORD_3:{
					message+="\n"+ChatColor.GRAY+"C418 - blocks";
				}break;
				case RECORD_4:{
					message+="\n"+ChatColor.GRAY+"C418 - chirp";
				}break;
				case RECORD_5:{
					message+="\n"+ChatColor.GRAY+"C418 - far";
				}break;
				case RECORD_6:{
					message+="\n"+ChatColor.GRAY+"C418 - mall";
				}break;
				case RECORD_7:{
					message+="\n"+ChatColor.GRAY+"C418 - melohi";
				}break;
				case RECORD_8:{
					message+="\n"+ChatColor.GRAY+"C418 - stal";
				}break;
				case RECORD_9:{
					message+="\n"+ChatColor.GRAY+"C418 - strad";
				}break;
				case RECORD_10:{
					message+="\n"+ChatColor.GRAY+"C418 - ward";
				}break;
				case RECORD_11:{
					message+="\n"+ChatColor.GRAY+"C418 - 11";
				}break;
				case RECORD_12:{
					message+="\n"+ChatColor.GRAY+"C418 - wait";
				}break;
				default:{
					
				}
			}
		}
		if (item.getType().getMaxDurability()>0) {
			message+="\n\n"+ChatColor.GRAY+"Durability: "+(item.getType().getMaxDurability()-item.getDurability()+1)+"/"+(item.getType().getMaxDurability()+1);
		}
		if (item.getItemMeta() instanceof Repairable &&
				GenericFunctions.isEquip(item)) {
			Repairable rep = (Repairable)item.getItemMeta();
			int repairs = 0;
			if (rep.hasRepairCost()) {
				int cost = rep.getRepairCost();
				while (cost>0) {
					cost/=2;
					repairs++;
				}
			} else {
				repairs=0;
			}
			message+="\n"+ChatColor.GRAY+"Repairs Left: "+(6-repairs);
		}
		//If there is a newline at the very end, cut it out.
		if (message.endsWith("\n")) {
			message.substring(0, message.length()-1);
		}
		
		message=obfuscateAllMagicCodes(message);
		
		return message;
	}

	private static String divideDurationByEight(String dur) {
		return dur.replace("0:22", "0:02").replace("0:45", "0:05").replace("1:00", "0:07").replace("1:15", "0:09").replace("1:30", "0:11").replace("2:00", "0:15").replace("3:00", "0:22").replace("4:00", "0:30").replace("5:00", "0:37").replace("8:00", "1:00");
	}

	private static boolean isBadPotionEffect(PotionEffectType type) {
		if (type.equals(PotionEffectType.ABSORPTION) ||
			type.equals(PotionEffectType.DAMAGE_RESISTANCE) ||
			type.equals(PotionEffectType.FAST_DIGGING) ||
			type.equals(PotionEffectType.FIRE_RESISTANCE) ||
			type.equals(PotionEffectType.GLOWING) ||
			type.equals(PotionEffectType.HEAL) ||
			type.equals(PotionEffectType.HEALTH_BOOST) ||
			type.equals(PotionEffectType.INCREASE_DAMAGE) ||
			type.equals(PotionEffectType.INVISIBILITY) ||
			type.equals(PotionEffectType.JUMP) ||
			type.equals(PotionEffectType.LUCK) ||
			type.equals(PotionEffectType.NIGHT_VISION) ||
			type.equals(PotionEffectType.REGENERATION) ||
			type.equals(PotionEffectType.SATURATION) ||
			type.equals(PotionEffectType.SPEED) ||
			type.equals(PotionEffectType.WATER_BREATHING)) {
			return false;
		} else {
			return true;
		}
	}

	public static String toReadableDuration(int duration) {
		DecimalFormat df = new DecimalFormat("00");
		return duration/1200+":"+df.format((duration/20)%60);
	}

	private static String obfuscateAllMagicCodes(String message) {
		StringBuilder newstring = new StringBuilder("");
		boolean isMagic=false;
		boolean WillBeMagic=false;
		int linenumb = 0;
		int charnumb = 0;
		boolean isColorCode=false;
		for (int i=0;i<message.length();i++) {
			ChatColor col = null;
			if (WillBeMagic) {
				isMagic=true;
				WillBeMagic=false;
			}
			if (isColorCode) {
				col=ChatColor.getByChar(message.charAt(i));
				isColorCode=false;
			}
			if (col!=null) {
				TwosideKeeper.log("Col is "+col.name()+", char is "+message.charAt(i), 5);
			}
			if (col!=null &&
					col == ChatColor.MAGIC) {
				TwosideKeeper.log("Found a Magic Char at Line "+(linenumb+1)+", Character "+(charnumb+1), 5);
				WillBeMagic=true;
			}
			if (col!=null &&
					col == ChatColor.RESET && isMagic) {
				isMagic=!isMagic;
			}
			if (message.charAt(i)==ChatColor.COLOR_CHAR) {
				isColorCode=true;
			}
			if (message.charAt(i)=='\n' && isMagic) {
				isMagic=!isMagic;
			}
			if (message.charAt(i)=='\n') {
				linenumb++;
				charnumb=0;
			}
			if (isMagic) {
				newstring.append(Character.toChars('z'-(int)((Math.random()*57))));
			} else {
				newstring.append(message.charAt(i));
			}
			charnumb++;
		}
		return newstring.toString();
	}

	public void sendItemInfo(Player player) {
		//Returns all the lore and enchantments for this particular item to the player, so they know what this item is.
		String[] temp = GetItemInfo(item).split("\n");
		for (int i=0;i<temp.length;i++) {
			player.sendMessage("  "+temp[i]);
		}
	}
	
	public static String getRealName(Enchantment enchant) {
		if (enchant.getName().equalsIgnoreCase("arrow_damage")) {return "Power";}
		if (enchant.getName().equalsIgnoreCase("arrow_fire")) {return "Flame";}
		if (enchant.getName().equalsIgnoreCase("arrow_infinite")) {return "Infinity";}
		if (enchant.getName().equalsIgnoreCase("arrow_knockback")) {return "Punch";}
		if (enchant.getName().equalsIgnoreCase("damage_all")) {return "Sharpness";}
		if (enchant.getName().equalsIgnoreCase("damage_arthropods")) {return "Bane of Arthropods";}
		if (enchant.getName().equalsIgnoreCase("damage_undead")) {return "Smite";}
		if (enchant.getName().equalsIgnoreCase("depth_strider")) {return "Depth Strider";}
		if (enchant.getName().equalsIgnoreCase("dig_speed")) {return "Efficiency";}
		if (enchant.getName().equalsIgnoreCase("durability")) {return "Unbreaking";}
		if (enchant.getName().equalsIgnoreCase("fire_aspect")) {return "Fire Aspect";}
		if (enchant.getName().equalsIgnoreCase("frost_walker")) {return "Frost Walker";}
		if (enchant.getName().equalsIgnoreCase("knockback")) {return "Knockback";}
		if (enchant.getName().equalsIgnoreCase("loot_bonus_blocks")) {return "Fortune";}
		if (enchant.getName().equalsIgnoreCase("loot_bonus_mobs")) {return "Looting";}
		if (enchant.getName().equalsIgnoreCase("luck")) {return "Luck of the Sea";}
		if (enchant.getName().equalsIgnoreCase("lure")) {return "Lure";}
		if (enchant.getName().equalsIgnoreCase("mending")) {return "Mending";}
		if (enchant.getName().equalsIgnoreCase("oxygen")) {return "Respiration";}
		if (enchant.getName().equalsIgnoreCase("protection_environmental")) {return "Protection";}
		if (enchant.getName().equalsIgnoreCase("protection_explosions")) {return "Blast Protection";}
		if (enchant.getName().equalsIgnoreCase("protection_fall")) {return "Feather Falling";}
		if (enchant.getName().equalsIgnoreCase("protection_fire")) {return "Fire Protection";}
		if (enchant.getName().equalsIgnoreCase("protection_projectile")) {return "Projectile Protection";}
		if (enchant.getName().equalsIgnoreCase("silk_touch")) {return "Silk Touch";}
		if (enchant.getName().equalsIgnoreCase("thorns")) {return "Thorns";}
		if (enchant.getName().equalsIgnoreCase("water_worker")) {return "Aqua Affinity";}
		return "";
	}
	
	/**
     * 
     * @param numb The number to convert to Roman Numerals.
     * @return A String version of the number converted in Roman Numeral Format.
     */
	 public static String toRomanNumeral(int Int) {
	    LinkedHashMap<String, Integer> roman_numerals = new LinkedHashMap<String, Integer>();
	    roman_numerals.put("M", 1000);
	    roman_numerals.put("CM", 900);
	    roman_numerals.put("D", 500);
	    roman_numerals.put("CD", 400);
	    roman_numerals.put("C", 100);
	    roman_numerals.put("XC", 90);
	    roman_numerals.put("L", 50);
	    roman_numerals.put("XL", 40);
	    roman_numerals.put("X", 10);
	    roman_numerals.put("IX", 9);
	    roman_numerals.put("V", 5);
	    roman_numerals.put("IV", 4);
	    roman_numerals.put("I", 1);
	    String res = "";
	    for(Map.Entry<String, Integer> entry : roman_numerals.entrySet()){
	      int matches = Int/entry.getValue();
	      res += repeat(entry.getKey(), matches);
	      Int = Int % entry.getValue();
	    }
	    return res;
	  }
	  public static String repeat(String s, int n) {
	    if(s == null) {
	        return null;
	    }
	    final StringBuilder sb = new StringBuilder();
	    for(int i = 0; i < n; i++) {
	        sb.append(s);
	    }
	    return sb.toString();
	  }

	public static boolean isPurchaseShopSign(Sign s) {
		if (isWorldShopSign(s) &&
				s.getLine(0).equalsIgnoreCase(ChatColor.BLUE+"- BUYING SHOP -") ||
				s.getLine(0).equalsIgnoreCase(ChatColor.YELLOW+""+ChatColor.BOLD+"-BUYING SHOP-") ||
				s.getLine(0).equalsIgnoreCase(ChatColor.GREEN+""+ChatColor.BOLD+"-BUYING SHOP-")) {
			return true;
		} else {
			return false;
		}
	}
		
	public static boolean isWorldShopSign(Sign s) {
		if (s.getLine(0).equalsIgnoreCase(ChatColor.BLUE+"-- SHOP --") ||
				s.getLine(0).equalsIgnoreCase(ChatColor.BLUE+"- BUYING SHOP -") ||
				s.getLine(0).equalsIgnoreCase(ChatColor.YELLOW+""+ChatColor.BOLD+"-BUYING SHOP-") ||
				s.getLine(0).equalsIgnoreCase(ChatColor.GREEN+""+ChatColor.BOLD+"-BUYING SHOP-")) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isWorldShopSign(Block b) {
		if (b!=null && (b.getType()==Material.SIGN || b.getType()==Material.WALL_SIGN || b.getType()==Material.SIGN_POST) && b.getState() instanceof Sign) {
			Sign s = (Sign)b.getState();
			return isWorldShopSign(s);
		} else {
			return false;
		}
	}
	
	public static boolean hasShopSignAttached(Block b) {
		//Returns true if there is a shop sign attached to this block.
		//Look on all four sides relative to this block.
		TwosideKeeper.log("Reference block is "+b.getLocation().toString()+" of type "+b.getType(),5);
		for (int x=-1;x<=1;x++) {
			for (int z=-1;z<=1;z++) {
				if ((x!=0 || z!=0) && Math.abs(x)!=Math.abs(z)) {
					Block checkblock = b.getRelative(x, 0, z);
					TwosideKeeper.log("This is a "+checkblock.getType(),5);
					if (checkblock.getType()==Material.WALL_SIGN) {
						org.bukkit.material.Sign s = (org.bukkit.material.Sign)(checkblock.getState().getData());
						//See if this sign is "facing" this block.
						TwosideKeeper.log("Checked block is a "+checkblock.getRelative(s.getAttachedFace()).getType()+" at Loc "+checkblock.getRelative(s.getAttachedFace()).getLocation().toString(), 4);
						if (checkblock.getRelative(s.getAttachedFace()).equals(b)) {
							TwosideKeeper.log("This is the block on the sign! Proceed.",5);
							//See if this sign is a world shop.
							if (WorldShop.isWorldShopSign((Sign)checkblock.getState())) {
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	public static boolean shopSignExists(Block block) {
		return isWorldShopSign(block) || grabShopSign(block)!=null;
	}
	
	public static Sign grabShopSign(Block block) {
		Block signblock = null;
		Block signblock2 = null;
		if (block==null) {return null;}
		for (int i=-1;i<2;i++) {
			for (int j=-1;j<2;j++) {
				if (i!=0^j!=0) {
					Block testblock = block.getRelative(i,0,j);
					if (testblock.getType().equals(block.getType())) {
						//We found a double chest.
						signblock2=testblock;
						TwosideKeeper.log("Found a double chest @ "+i+","+j,5);
					}
					if (testblock.getType()==Material.WALL_SIGN) {
						TwosideKeeper.log("This might be a shop sign "+i+","+j,5);
						//This might be a world shop sign. Check.
						Sign s = (Sign)(testblock.getState());
						//See if the attached block is this block.
						org.bukkit.material.Sign s1 = (org.bukkit.material.Sign)(testblock.getState().getData());
		    			if (testblock.getRelative(s1.getAttachedFace()).equals(block) //We want to make sure the sign is attached to this block.
		    					&& WorldShop.isWorldShopSign(s)) {
		    				//This is a shop sign. We found it.
		    				signblock = testblock;
		    			}
					}
				}
			}
		}
		if (signblock!=null) {
			//We will now return it.
			TwosideKeeper.log("------------",5);
			return (Sign)signblock.getState();
		} else if (signblock2!=null) {
			//Check in all directions of the connected double chest for a shop sign.
			for (int i=-1;i<2;i++) {
				for (int j=-1;j<2;j++) {
					if (i!=0^j!=0) {
						Block testblock = signblock2.getRelative(i,0,j);
						if (testblock.getType()==Material.WALL_SIGN) {
							TwosideKeeper.log("(2) This might be a shop sign "+i+","+j,5);
							//This might be a world shop sign. Check.
							Sign s = (Sign)(testblock.getState());
							org.bukkit.material.Sign s1 = (org.bukkit.material.Sign)(testblock.getState().getData());
			    			if (testblock.getRelative(s1.getAttachedFace()).getLocation().equals(signblock2.getLocation()) //We want to make sure the sign is attached to this block.
			    					&& WorldShop.isWorldShopSign(s)) {
			    				//This is a shop sign. We found it.
			    				TwosideKeeper.log("------------",5);
			    				return (Sign)testblock.getState();
			    			}
						}
					}
				}
			}
		}
		TwosideKeeper.log("------------",5);
		return null;
	}
	
	public static Block getBlockShopSignAttachedTo(Sign s) {
		org.bukkit.material.Sign s1 = (org.bukkit.material.Sign)(s.getBlock().getState().getData());
		return s.getBlock().getRelative(s1.getAttachedFace());
	}
	
	public static void updateShopSign(Block shopblock) {
		//This will first attempt to grab the shop sign.
		//Upon finding it, we will load up the shop and update it to the correct value inside the chest inventory.
		Sign s = grabShopSign(shopblock);
		if (s!=null) {
			TwosideKeeper.log("There is a shop sign here",5);
			//Load up the shop.
			WorldShop shop = TwosideKeeper.TwosideShops.LoadWorldShopData(s);
			//Now detect the amount inside the double chest.
			Chest c = (Chest)shopblock.getState();
			Inventory chest_inventory = c.getInventory();
			int amt = 0;
			if (isPurchaseShopSign(s)) {
				amt = GenericFunctions.CountEmptySpace(chest_inventory, shop.GetItem());
				shop.amt = amt;
				shop.storedamt = 0;
			} else {
				amt = GenericFunctions.CountItems(chest_inventory, shop.GetItem());
				shop.amt = amt;
			}
			TwosideKeeper.TwosideShops.SaveWorldShopData(shop);
			TwosideKeeper.log("There are "+amt+" of "+shop.GetItem().toString(),5);
			WorldShopManager.UpdateSign(shop, s);
		}
	}

	public static void spawnShopItem(Location signloc, WorldShop shop) {
		org.bukkit.material.Sign s = (org.bukkit.material.Sign)signloc.getBlock().getState().getData();
		Block shopblock = signloc.getBlock().getRelative(s.getAttachedFace());
		//See if there's already a shop item here.

		boolean item_here=false;
		Collection<Entity> entities = signloc.getWorld().getNearbyEntities(signloc, 0.2, 0.2, 0.2);
		for (Entity e : entities) {
			if (e.getType()==EntityType.DROPPED_ITEM) {
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
				if (
						it.getItemStack().isSimilar(shop.GetItem())
						) {
					item_here=true;
				}
			}
		}
		if (!item_here) {
			TwosideKeeper.log("Spawning item!",5);
			ItemStack i = shop.GetItem().clone();
			ItemStack drop = Artifact.convert(i);
			drop.removeEnchantment(Enchantment.LUCK);
			ItemMeta m = drop.getItemMeta();
			List<String> lore = m.getLore();
			lore.add("WorldShop Display Item");
			m.setLore(lore);
			drop.setItemMeta(m);
			Item it = signloc.getWorld().dropItem(shopblock.getLocation().add(0.5, 1.5, 0.5), drop);
			it.setPickupDelay(999999999);
			it.setVelocity(new Vector(0,0,0));
			it.setCustomName(""+shop.getID());
			it.setCustomNameVisible(false);
			it.setInvulnerable(true);
			//it.setGlowing(true);
			//it.teleport(ev.getClickedBlock().getLocation().add(-ev.getBlockFace().getModX()+0.5, -ev.getBlockFace().getModY()+1.5, -ev.getBlockFace().getModZ()+0.5));
		}
	}

	public static void removeShopItem(Sign s) {
		if (isWorldShopSign(s)) {
			removeShopItem(s, TwosideKeeper.TwosideShops.LoadWorldShopData(s));
		}
	}
	
	public static void removeShopItem(Sign s, WorldShop shop) {
		if (isWorldShopSign(s)) {
			Collection<Entity> nearby = WorldShop.getBlockShopSignAttachedTo(s).getWorld().getNearbyEntities(WorldShop.getBlockShopSignAttachedTo(s).getLocation().add(0.5,0,0.5), 0.3, 1, 0.3);
			for (Entity e : nearby) {
				if (e.getType()==EntityType.DROPPED_ITEM) {
					TwosideKeeper.log("Found a drop.",5);
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
	
					TwosideKeeper.log("Comparing item "+it.getItemStack().toString()+" to "+checkdrop.toString(),5);
					if (it.getItemStack().isSimilar(checkdrop) &&
							Artifact.isArtifact(it.getItemStack())) {
						TwosideKeeper.log("Same type.",5);
						e.remove();
						e.setCustomNameVisible(false);
						e.setCustomName(null);
					}
				}
			}
		}
	}
	
	public static void spawnShopItem(PlayerInteractEvent ev, Location loc, WorldShop shop) {
		//See if a drop entity is already here.
		boolean item_here=false;
		Collection<Entity> entities = ev.getPlayer().getLocation().getWorld().getNearbyEntities(loc, 1, 1, 1);
		for (Entity e : entities) {
			TwosideKeeper.log("Entity Location:"+e.getLocation().toString(),5);
			TwosideKeeper.log("Comparing locations: "+e.getLocation().toString()+":::"+loc.toString(),5);
			if (e.getType()==EntityType.DROPPED_ITEM) {
				Item it = (Item)e;
				if (
						it.getItemStack().getType()==shop.GetItem().getType() &&
						it.getItemStack().getDurability()==shop.GetItem().getDurability() &&
						it.getCustomName()!=null &&
						it.getCustomName().equalsIgnoreCase(""+shop.getID()) &&
						Math.abs(e.getLocation().getX()-loc.getX()) <= 1 &&
						Math.abs(e.getLocation().getZ()-loc.getZ()) <= 1 &&
						Math.abs(e.getLocation().getY()-loc.getY())<=1
						) {
					item_here=true;
				}
			}
		}
		if (!item_here) {
			TwosideKeeper.log("Spawning item!",5);
			ItemStack i = shop.GetItem().clone();
			ItemStack drop = Artifact.convert(i);
			drop.removeEnchantment(Enchantment.LUCK);
			ItemMeta m = drop.getItemMeta();
			List<String> lore = m.getLore();
			lore.add("WorldShop Display Item");
			m.setLore(lore);
			drop.setItemMeta(m);
			Item it = ev.getPlayer().getWorld().dropItem(ev.getClickedBlock().getLocation().add(-ev.getBlockFace().getModX()+0.5, -ev.getBlockFace().getModY()+1.5, -ev.getBlockFace().getModZ()+0.5), drop);
			it.setPickupDelay(999999999);
			it.setVelocity(new Vector(0,0,0));
			it.setCustomName(""+shop.getID());
			it.setCustomNameVisible(false);
			it.setInvulnerable(true);
			//it.setGlowing(true);
			it.teleport(ev.getClickedBlock().getLocation().add(-ev.getBlockFace().getModX()+0.5, -ev.getBlockFace().getModY()+1.5, -ev.getBlockFace().getModZ()+0.5));
		}
	}

	public static boolean hasPermissionToBreakWorldShopSign(Sign s, Player p) {
		if (WorldShop.isWorldShopSign(s)) {
			WorldShop shop = TwosideKeeper.TwosideShops.LoadWorldShopData(s);
			if (shop.GetOwner().equalsIgnoreCase(p.getName()) || p.isOp()) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}

	public static boolean canPlaceShopSignOnBlock(Block block) {
		return (!shopSignExists(block) && GenericFunctions.isDumpableContainer(block.getType()));
	}

	public static void createWorldShopRecipes() {
		for (Material mat : Material.values()) {
			ItemStack result = new ItemStack(Material.CHEST);
			ItemUtils.addLore(result,ChatColor.DARK_PURPLE+"World Shop Chest");
			ItemUtils.addLore(result,ChatColor.BLACK+""+ChatColor.MAGIC+mat.name());
			ItemUtils.addLore(result,ChatColor.LIGHT_PURPLE+"Place in the world to setup a");
			ItemUtils.addLore(result,ChatColor.LIGHT_PURPLE+"world shop that sells "+ChatColor.YELLOW+GenericFunctions.UserFriendlyMaterialName(mat));
			ItemUtils.setDisplayName(result,ChatColor.YELLOW+GenericFunctions.UserFriendlyMaterialName(mat)+" Shop Chest");
			ItemUtils.hideEnchantments(result);
			result.addUnsafeEnchantment(Enchantment.LUCK, 4);
			ShapelessRecipe rec = new ShapelessRecipe(result);
			rec.addIngredient(mat, -1);
			rec.addIngredient(Material.CHEST);
			rec.addIngredient(Material.SIGN);
			Bukkit.addRecipe(rec);
			result = new ItemStack(Material.TRAPPED_CHEST);
			ItemUtils.addLore(result,ChatColor.DARK_PURPLE+"World Shop Chest");
			ItemUtils.addLore(result,ChatColor.BLACK+""+ChatColor.MAGIC+mat.name());
			ItemUtils.addLore(result,ChatColor.LIGHT_PURPLE+"Place in the world to setup a");
			ItemUtils.addLore(result,ChatColor.LIGHT_PURPLE+"world shop that sells "+ChatColor.YELLOW+GenericFunctions.UserFriendlyMaterialName(mat));
			ItemUtils.setDisplayName(result,ChatColor.YELLOW+GenericFunctions.UserFriendlyMaterialName(mat)+" Shop Chest");
			ItemUtils.hideEnchantments(result);
			result.addUnsafeEnchantment(Enchantment.LUCK, 4);
			rec = new ShapelessRecipe(result);
			rec.addIngredient(mat, -1);
			rec.addIngredient(Material.TRAPPED_CHEST);
			rec.addIngredient(Material.SIGN);
			Bukkit.addRecipe(rec);
			Bukkit.addRecipe(rec);
		}
	}

	public static void loadShopPrices() {
		File file = new File(price_file);
		
		if (file.exists()) {
			LoadPricesIntoPriceList(file);
		} else {
			try {
				TwosideKeeper.log(ChatColor.GOLD+"No World Shop Price file detected. Creating a new one...", 1);
				long start_time = System.currentTimeMillis();
				file.createNewFile();
				PopulateNewFile(file);
				TwosideKeeper.log(ChatColor.AQUA+"Finished creating World Shop Price file with "+Material.values().length+" entries. Elapsed Time: "+(System.currentTimeMillis()-start_time)+"ms", 1);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void LoadPricesIntoPriceList(File file) {
		try(
				FileReader fw = new FileReader(price_file);
			    BufferedReader bw = new BufferedReader(fw);)
			{
				String readline = bw.readLine();
				int lines = 0;
				do {
					if (readline!=null) {
						lines++;
						String[] split = readline.split(",");
						if (split.length==2) {
							double price = Double.parseDouble(split[1]);
							if (pricelist.containsKey(split[0])) {
								PossibleDuplicateWarning("Possible Duplicate World Shop Price Found for "+split[0]+"! At Line: "+lines,1);
							}
							pricelist.put(split[0], price);
						} else {
							//3 means there's a data value there too.
							double price = Double.parseDouble(split[2]);
							if (pricelist.containsKey(split[0]+","+split[1])) {
								PossibleDuplicateWarning("Possible Duplicate World Shop Price Found for "+split[0]+","+split[1]+"! At Line: "+lines,1);
							}
							pricelist.put(split[0]+","+split[1], price);
						}
						readline = bw.readLine();
					}} while (readline!=null);
				TwosideKeeper.log("[WorldShop]Loaded "+lines+" shop entries successfully.",2);
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	private static void PossibleDuplicateWarning(String string, int loglv) {
		TwosideKeeper.log(string, loglv);
	}

	private static void PopulateNewFile(File file) {
		try(
			FileWriter fw = new FileWriter(price_file, false);
		    BufferedWriter bw = new BufferedWriter(fw);)
		{
			for (Material mat : Material.values()) {
				bw.write(mat.name()+","+DEFAULTPRICE);
				bw.newLine();
				pricelist.put(mat.name(), DEFAULTPRICE);
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean isPlaceableWorldShop(ItemStack item) {
		if (ItemUtils.isValidLoreItem(item) &&
				ItemUtils.LoreContains(item,ChatColor.DARK_PURPLE+"World Shop Chest")) {
			return true;
		}
		return false;
	}

	public static void CreateNewWorldShop(Block b, ItemStack item) {
		Chest c = (Chest)b.getState();
		c.getBlockInventory().addItem(item);
		//From block B, add a Wall Sign attached to this block.
		BlockFace bf = BlockUtils.GetBlockFacingDirection(b);
		Block wallsign = b.getRelative(bf); //This will be the sign.
		wallsign.setType(Material.WALL_SIGN);
		//Make it face the opposite way of the chest.
		org.bukkit.material.Sign sign = (org.bukkit.material.Sign)(wallsign.getState().getData());
		sign.setFacingDirection(bf);
		DecimalFormat df = new DecimalFormat("0.00");
		wallsign.setData(sign.getData());
		Sign s = (Sign)wallsign.getState();
		s.setLine(0,"shop");
		WorldShop shop = TwosideKeeper.TwosideShops.CreateWorldShop(s, item, 10000, DEFAULTPRICE, "admin");
		/*s.setLine(0, ChatColor.BLUE+"-- SHOP --");
		s.setLine(1, GenericFunctions.UserFriendlyMaterialName(item));
		s.setLine(2, "$"+df.format(GetWorldShopPrice(item))+ChatColor.DARK_BLUE+" [x10000]");
		DecimalFormat df2 = new DecimalFormat("000000");
		s.setLine(3, ChatColor.DARK_GRAY+df2.format(TwosideKeeper.WORLD_SHOP_ID));
		TwosideKeeper.WORLD_SHOP_ID++;*/
		shop.UpdateUnitPrice(shop.GetUnitPrice());
		WorldShop.spawnShopItem(s.getLocation(), shop);
		TwosideKeeper.TwosideShops.SaveWorldShopData(shop);
	}

	public static ItemStack ExtractPlaceableShopMaterial(ItemStack item) {
		if (isPlaceableWorldShop(item)) {
			String[] split = ItemUtils.GetLoreLine(item, 1).replace(ChatColor.BLACK+""+ChatColor.MAGIC, "").split(","); 
			if (split.length>1) {
				return new ItemStack(Material.valueOf(split[0]),1,Short.parseShort(split[1]));
			} else {
				Material mat = Material.valueOf(split[0]);
				return new ItemStack(mat);
			}
		} else {
			TwosideKeeper.log("THIS SHOULD NOT BE HAPPENING! Trying to extract from a non-world shop item!", 0);
			return new ItemStack(Material.AIR);
		}
	}
	
	public static ItemStack generateItemDealOftheDay(int iter) {
		Calendar cal = Calendar.getInstance();
		int seed = cal.get(Calendar.YEAR)*cal.get(Calendar.DAY_OF_YEAR)*iter;
		Random r = new Random();
		r.setSeed(seed);
		Set<String> items = WorldShop.pricelist.keySet();
		int rand = r.nextInt(items.size());
		if (iter>50) { //A fail-safe so we don't end up in an endless loop.
			TwosideKeeper.log("Could not define a specific deal of the day! Please check the ShopPrices.data file for valid prices!", 1);
			return new ItemStack(Material.DIRT);
		}
		for (String s : items) {
			if (rand>0) {
				rand--;
			} else {
				double price = WorldShop.pricelist.get(s);
				if (price==DEFAULTPRICE || price>1000000) {
					//This is bad, this is not an item we can actually purchase. We will default to the first entry that we know works.
					//TwosideKeeper.log("Price for "+s+" was "+price, 0);
					return generateItemDealOftheDay(iter+1);
				} else {
					String[] split = s.split(",");
					if (split.length==1) {
						return new ItemStack(Material.valueOf(split[0]));
					} else {
						return new ItemStack(Material.valueOf(split[0]),1,Short.parseShort(split[1]));
					}
				}
			}
		}
		TwosideKeeper.log("COULD NOT GET A DEAL OF THE DAY! RAN OUT OF ENTRIES!!! THIS SHOULD NOT BE HAPPENING.", 0);
		return null;
	}
}
