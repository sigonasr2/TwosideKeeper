package sig.plugin.TwosideKeeper.HelperStructures;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import aPlugin.API;
import sig.plugin.TwosideKeeper.PlayerStructure;
import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.HelperStructures.Common.BaublePouch;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.DebugUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.TextUtils;

public enum ItemSet {
	PANROS(1,1, 6,4, 10,10, 20,10),
	SONGSTEEL(4,2, 6,2, 8,8, 20,10),
	DAWNTRACKER(2,1, 20,10, 10,5, 10,5),
	LORASYS(2,2, 0,0, 0,0, 0,0),
	JAMDAK(3,3, 5,1, 10,1, 10,2), //Graceful Dodge is in ticks.
	DARNYS(2,1, 10,5, 20,5, 1,1),
	ALIKAHN(3,1, 15,6, 30,10, 1,1),
	LORASAADI(4,1, 4,2, 8,6, 8,3),
	MOONSHADOW(6,3, 1,1, 8,8, 15,7),
	GLADOMAIN(1,1, 12,4, 8,4, 1,1),
	WOLFSBANE(3,2, 15,10, 10,5, 15,10),
	ALUSTINE(3,2, 300,-30, 50,-5, 6,2),
	DASHER(5,5, 3,3, 5,5, 0,0),
	DANCER(5,1, 3,3, 5,5, 0,0),
	PRANCER(5,5, 3,3, 5,5, 0,0),
	VIXEN(5,4, 3,3, 5,5, 0,0),
	COMET(10,10, 10,10, 2,1, 0,0),
	CUPID(10,5, 10,10, 2,1, 0,0),
	DONNER(5,5, 10,10, 2,1, 0,0),
	BLITZEN(10,10, 3,3, 5,5, 0,0),
	RUDOLPH(5,5, 10,10, 2,1, 0,0),
	OLIVE(3,2, 10,10, 2,1, 0,0);
	
	public static ItemSet[] bauble_sets;
	int baseval;
	int increase_val;
	int baseval_bonus2;
	int increase_val_bonus2;
	int baseval_bonus3;
	int increase_val_bonus3;
	int baseval_bonus4;
	int increase_val_bonus4;
	
    public static final ItemSet[] RANGER = new ItemSet[]{
            ItemSet.JAMDAK, 
            ItemSet.DARNYS, 
            ItemSet.ALIKAHN, 
            ItemSet.LORASAADI,
            };
    public static final ItemSet[] MELEE = new ItemSet[]{
            ItemSet.DAWNTRACKER, 
            ItemSet.PANROS, 
            ItemSet.SONGSTEEL,
            };
    public static final ItemSet[] TRINKET = new ItemSet[]{
            ItemSet.GLADOMAIN, 
            ItemSet.ALUSTINE, 
            ItemSet.MOONSHADOW,
            ItemSet.WOLFSBANE,
            };
    public static final ItemSet[] HOLIDAY = new ItemSet[]{
            ItemSet.BLITZEN,
            ItemSet.COMET,
            ItemSet.CUPID,
            ItemSet.DANCER,
            ItemSet.DASHER,
            ItemSet.DONNER,
            ItemSet.OLIVE,
            ItemSet.PRANCER,
            ItemSet.RUDOLPH,
            ItemSet.VIXEN,
            };
    
    public static final ItemSet[][] REROLLABLE_ITEM_SETS = new ItemSet[][]{
    	RANGER, 
    	MELEE, 
    	TRINKET, 
    	HOLIDAY};
    
	ItemSet(int baseval,int increase_val,
			int baseval2,int increase_val2,
			int baseval3,int increase_val3,
			int baseval4,int increase_val4) {
		this.baseval=baseval;
		this.increase_val=increase_val;
		this.baseval_bonus2=baseval2;
		this.increase_val_bonus2=increase_val2;
		this.baseval_bonus3=baseval3;
		this.increase_val_bonus3=increase_val3;
		this.baseval_bonus4=baseval4;
		this.increase_val_bonus4=increase_val4;
	} 
	
	public static boolean isSetItem(ItemStack item) {
		return GetItemSet(item)!=null;
	}

	public static ItemSet GetItemSet(ItemStack item) {
		if ((GenericFunctions.isEquip(item) || GenericFunctions.isSkullItem(item)) &&
				!GenericFunctions.isArtifactEquip(item) &&
				item.getItemMeta().hasLore()) {
			List<String> lore = item.getItemMeta().getLore();
			for (int i=0;i<lore.size();i++) {
				if (lore.get(i).contains(ChatColor.GOLD+""+ChatColor.BOLD+"T") && !lore.get(i).contains("Recipe")) {
					//This is the tier line.
					return ItemSet.valueOf(lore.get(i).replace(ChatColor.GOLD+""+ChatColor.BOLD+"T", "").split(" ")[1].toUpperCase());
				}
			}
		} 
		return null;
	}
	
	public static int GetItemTier(ItemStack item) {
		if (isSetItem(item) &&
				item.getItemMeta().hasLore()) {
			List<String> lore = item.getItemMeta().getLore();
			for (int i=0;i<lore.size();i++) {
				if (lore.get(i).contains(ChatColor.GOLD+""+ChatColor.BOLD+"T")) {
					//This is the tier line.
					return Integer.parseInt(lore.get(i).replace(ChatColor.GOLD+""+ChatColor.BOLD+"T", "").split(" ")[0]);
				}
			}
			TwosideKeeper.log(ChatColor.RED+"[ERROR] Could not detect proper tier of "+item.toString()+"!", 1);
		} 
		return 0;
	}
	
	public static void SetTier(ItemStack item, int tier) {
		boolean found=false;
		if (isSetItem(item) &&
				item.getItemMeta().hasLore()) {
			List<String> lore = item.getItemMeta().getLore();
			for (int i=0;i<lore.size();i++) {
				if (lore.get(i).contains(ChatColor.GOLD+""+ChatColor.BOLD+"T")) {
					//This is the tier line.
					int oldtier=GetItemTier(item);
					//TwosideKeeper.log("In lore: "+lore.get(i)+". Old tier: "+oldtier,2);
					lore.set(i, lore.get(i).replace("T"+oldtier, "T"+tier));
					found=true;
					break;
				}
			}
			ItemMeta m = item.getItemMeta();
			m.setLore(lore);
			item.setItemMeta(m);
			GenericFunctions.UpdateItemLore(item); //Update this item now that we upgraded the tier.
			GenericFunctions.ConvertSetColor(item, GetItemSet(item));
			if (!found) {
				TwosideKeeper.log(ChatColor.RED+"[ERROR] Could not detect proper tier of "+item.toString()+"!", 1);
			}
		} 
	}

	public static int GetBaseAmount(ItemSet set, int tier, int stat) {
		//stat will be 1 for the base value, 2 for the 2-piece set bonus, 3 for the 3-piece set bonus, and 4 for the 4-piece set bonus.
		switch (stat) {
			case 1:{
				return set.baseval+((tier-1)*set.increase_val);
			}
			case 2:{
				return set.baseval_bonus2+((tier-1)*set.increase_val_bonus2);
			}
			case 3:{
				return set.baseval_bonus3+((tier-1)*set.increase_val_bonus3);
			}
			case 4:{
				return set.baseval_bonus4+((tier-1)*set.increase_val_bonus4);
			}
		}
		TwosideKeeper.log(ChatColor.RED+"Error occurred while attempting to grab the Base Amount!!!", 1);
		return -1;
	}
	
	public int GetBaseAmount(int tier) {
		return baseval+((tier-1)*increase_val);
	}
	
	public static int GetSetCount(ItemSet set, Player p) {
		/*int count = 0;
		for (ItemStack item : equips) {
			ItemSet temp = ItemSet.GetSet(item);
			if (temp!=null) {
				if (temp.equals(set)) {
					count++;
				}
			}
		}
		TwosideKeeper.log("Currently have "+count+" pieces from the "+set.name()+" set.", 5);
		return count;*/

		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		int count = 0;
		if (pd.itemsets.containsKey(set.name())) {
			HashMap<Integer,Integer> tiermap = pd.itemsets.get(set.name());
			for (Integer tier : tiermap.keySet()) {
				count += tiermap.get(tier);
			}
		}
		return count;
	}
	
	public static int GetTierSetCount(ItemSet set, int tier, Player p) {
		/*int count = 0;
		for (ItemStack item : equips) {
			ItemSet temp = ItemSet.GetSet(item);
			if (temp!=null) {
				if (temp.equals(set) && GetTier(item)==tier) {
					count++;
				}
			}
		}
		TwosideKeeper.log("Currently have "+count+" pieces from the "+set.name()+" set of Tier +"+tier+".", 5);
		return count;*/

		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		int count = 0;
		if (pd.itemsets.containsKey(set.name())) {
			HashMap<Integer,Integer> tiermap = pd.itemsets.get(set.name());
			if (tiermap.containsKey(tier)) {
				count += tiermap.get(tier);
			}
		}
		return count;
	}
	
	public static int GetTotalBaseAmount(Player p, ItemSet set) {
		/*int count = 0;
		for (ItemStack item : equips) {
			ItemSet temp = ItemSet.GetSet(item);
			if (temp!=null) {
				if (temp.equals(set)) {
					count += set.GetBaseAmount(item);
				}
			}
		}
		TwosideKeeper.log("Base Total of all equipment from this set is "+count, 5);
		return count;*/
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		int val = 0;
		if (pd.itemsets.containsKey(set.name())) {
			HashMap<Integer,Integer> tiermap = pd.itemsets.get(set.name());
			for (Integer tier : tiermap.keySet()) {
				val += set.GetBaseAmount(tier)*tiermap.get(tier);
			}
		}
		return val;
	}
	
	public static boolean hasFullSet(Player p, ItemSet set) {
		//Return a mapping of all tier values that meet the count requirement for that set.
		/*for (ItemStack item : equips) {
			ItemSet temp = ItemSet.GetSet(item);
			if (temp!=null) {
				int tier = ItemSet.GetTier(item);
				int detectedsets = ItemSet.GetTierSetCount(equips, set, tier, ent);
				TwosideKeeper.log("Sets: "+detectedsets, 5);
				if (detectedsets>=5) {
					return true;
				}
			}
		}
		return false;*/
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		if (pd.itemsets.containsKey(set.name()) && pd.itemsets.get(set.name()).size()==1) { //We can only possibly have a full set if we only have one tier of that set.
			HashMap<Integer,Integer> tiermap = pd.itemsets.get(set.name());
			for (Integer tier : tiermap.keySet()) {
				if (tiermap.get(tier)>=5) {
					return true;
				}
			}
		}
		return false;
	}
	
	/*public static List<Integer> GetSetBonusCount(ItemStack[] equips, LivingEntity ent, ItemSet set, int count) {
		//Return a mapping of all tier values that meet the count requirement for that set.
		/*List<Integer> mapping = new ArrayList<Integer>();
		for (ItemStack item : equips) {
			ItemSet temp = ItemSet.GetSet(item);
			if (temp!=null) {
				int tier = ItemSet.GetTier(item);
				if (ItemSet.GetTierSetCount(equips, set, tier, ent)>=count) {
					if (!mapping.contains(tier)) {
						mapping.add(tier);
					}
				}
			}
		}
		return mapping;
	}*/

	public static double TotalBaseAmountBasedOnSetBonusCount(Player p, ItemSet set, int count, int set_bonus) {
		/*double amt = 0.0;
		List<Integer> mapping = ItemSet.GetSetBonusCount(equips, p, set, count);
		for (Integer tier : mapping) {
			amt+=ItemSet.GetBaseAmount(set, tier, set_bonus);
		}
		return amt;*/
		double amt = 0.0;
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		if (pd.itemsets.containsKey(set.name())) {
			HashMap<Integer,Integer> tiermap = pd.itemsets.get(set.name());
			for (Integer tier : tiermap.keySet()) {
				if (tiermap.get(tier)>=count) {
					amt+=ItemSet.GetBaseAmount(set, tier, set_bonus);
				}
			}
		}
		return amt;
	}

	public static Collection<? extends String> GenerateLore(ItemSet set, int tier) {
		List<String> lore = new ArrayList<String>();
		switch (set) {
			case PANROS:{
				lore.add(ChatColor.LIGHT_PURPLE+"Striker Gear");
				lore.add(ChatColor.GOLD+""+ChatColor.BOLD+"T"+tier+" Panros Set");
				lore.add(ChatColor.YELLOW+"+"+ItemSet.GetBaseAmount(set, tier, 1)+" Damage");
			}break;
			case SONGSTEEL:{
				lore.add(ChatColor.LIGHT_PURPLE+"Defender Gear");
				lore.add(ChatColor.GOLD+""+ChatColor.BOLD+"T"+tier+" Songsteel Set");
				lore.add(ChatColor.YELLOW+"+"+ItemSet.GetBaseAmount(set, tier, 1)+"% Block Chance");
			}break;
			case DAWNTRACKER:{
				lore.add(ChatColor.LIGHT_PURPLE+"Barbarian Gear");
				lore.add(ChatColor.GOLD+""+ChatColor.BOLD+"T"+tier+" Dawntracker Set");
				if (((ItemSet.GetBaseAmount(set, tier, 1)+1)/3)>0) {
					lore.add(ChatColor.YELLOW+"-"+((ItemSet.GetBaseAmount(set, tier, 1)+1)/3)+" Damage taken per hit");
				}
			}break;
			case LORASYS:{
				lore.add(ChatColor.LIGHT_PURPLE+"Slayer Gear");
				lore.add(ChatColor.GOLD+""+ChatColor.BOLD+"T"+tier+" Lorasys Set");
				lore.add(ChatColor.YELLOW+"+"+ItemSet.GetBaseAmount(set, tier, 1)+" Damage");
			}break;
			case JAMDAK:{
				lore.add(ChatColor.LIGHT_PURPLE+"Ranger Gear");
				lore.add(ChatColor.GOLD+""+ChatColor.BOLD+"T"+tier+" Jamdak Set");
				lore.add(ChatColor.YELLOW+"+"+ItemSet.GetBaseAmount(set, tier, 1)+"% Dodge Chance");
			}break;
			case DARNYS:{
				lore.add(ChatColor.LIGHT_PURPLE+"Ranger Gear");
				lore.add(ChatColor.GOLD+""+ChatColor.BOLD+"T"+tier+" Darnys Set");
				lore.add(ChatColor.YELLOW+"+"+ItemSet.GetBaseAmount(set, tier, 1)+"% Dodge Chance");
			}break;
			case ALIKAHN:{
				lore.add(ChatColor.LIGHT_PURPLE+"Ranger Gear");
				lore.add(ChatColor.GOLD+""+ChatColor.BOLD+"T"+tier+" Alikahn Set");
				lore.add(ChatColor.YELLOW+"+"+ItemSet.GetBaseAmount(set, tier, 1)+"% Dodge Chance");
			}break;
			case LORASAADI:{
				lore.add(ChatColor.LIGHT_PURPLE+"Ranger Gear");
				lore.add(ChatColor.GOLD+""+ChatColor.BOLD+"T"+tier+" Lorasaadi Set");
				lore.add(ChatColor.YELLOW+"+"+ItemSet.GetBaseAmount(set, tier, 1)+"% Dodge Chance");
			}break;
			case GLADOMAIN:{
				lore.add(ChatColor.LIGHT_PURPLE+"Slayer Amulet");
				lore.add(ChatColor.GOLD+""+ChatColor.BOLD+"T"+tier+" Gladomain Set");
				lore.add(ChatColor.YELLOW+"+"+ItemSet.GetBaseAmount(set, tier, 1)+" HP");
				lore.add("");
				lore.add(ChatColor.GRAY+" Must be inserted into a "+ChatColor.BLUE+"Bauble Pouch");
				lore.add(ChatColor.GRAY+" to benefit from the effects.");
				lore.add("");
			}break;
			case MOONSHADOW:{
				lore.add(ChatColor.LIGHT_PURPLE+"Slayer Trinket");
				lore.add(ChatColor.GOLD+""+ChatColor.BOLD+"T"+tier+" Moonshadow Set");
				lore.add(ChatColor.YELLOW+"+"+ItemSet.GetBaseAmount(set, tier, 1)+"% Crit Damage");
				lore.add("");
				lore.add(ChatColor.GRAY+" Must be inserted into a "+ChatColor.BLUE+"Bauble Pouch");
				lore.add(ChatColor.GRAY+" to benefit from the effects.");
				lore.add("");
			}break;
			case WOLFSBANE:{
				lore.add(ChatColor.LIGHT_PURPLE+"Slayer Ornament");
				lore.add(ChatColor.GOLD+""+ChatColor.BOLD+"T"+tier+" Wolfsbane Set");
				lore.add(ChatColor.YELLOW+"+"+ItemSet.GetBaseAmount(set, tier, 1)+"% Critical Chance");
				lore.add("");
				lore.add(ChatColor.GRAY+" Must be inserted into a "+ChatColor.BLUE+"Bauble Pouch");
				lore.add(ChatColor.GRAY+" to benefit from the effects.");
				lore.add("");
			}break;
			case ALUSTINE:{
				lore.add(ChatColor.LIGHT_PURPLE+"Slayer Charm");
				lore.add(ChatColor.GOLD+""+ChatColor.BOLD+"T"+tier+" Alustine Set");
				lore.add(ChatColor.YELLOW+"+"+ItemSet.GetBaseAmount(set, tier, 1)+"% EXP Gain");
				lore.add("");
				lore.add(ChatColor.GRAY+" Must be inserted into a "+ChatColor.BLUE+"Bauble Pouch");
				lore.add(ChatColor.GRAY+" to benefit from the effects.");
				lore.add("");
			}break;
			case BLITZEN:
				lore.add(ChatColor.BLUE+"Holiday Gear");
				lore.add(ChatColor.GOLD+""+ChatColor.BOLD+"T"+tier+" "+GenericFunctions.CapitalizeFirstLetters(set.name())+" Set");
				lore.add(ChatColor.YELLOW+"+"+ItemSet.GetBaseAmount(set, tier, 1)+"% Attack Rate");
				break;
			case COMET:
				lore.add(ChatColor.BLUE+"Holiday Gear");
				lore.add(ChatColor.GOLD+""+ChatColor.BOLD+"T"+tier+" "+GenericFunctions.CapitalizeFirstLetters(set.name())+" Set");
				lore.add(ChatColor.YELLOW+"+"+ItemSet.GetBaseAmount(set, tier, 1)+"% Regeneration to Party Members");
				break;
			case CUPID:
				lore.add(ChatColor.BLUE+"Holiday Gear");
				lore.add(ChatColor.GOLD+""+ChatColor.BOLD+"T"+tier+" "+GenericFunctions.CapitalizeFirstLetters(set.name())+" Set");
				lore.add(ChatColor.YELLOW+"Absorbs "+ItemSet.GetBaseAmount(set, tier, 1)+"% of Damage Taken from Party Members");
				break;
			case DANCER:
				lore.add(ChatColor.BLUE+"Holiday Gear");
				lore.add(ChatColor.GOLD+""+ChatColor.BOLD+"T"+tier+" "+GenericFunctions.CapitalizeFirstLetters(set.name())+" Set");
				lore.add(ChatColor.YELLOW+"+"+ItemSet.GetBaseAmount(set, tier, 1)+" Damage per 1m of Movement Speed");
				break;
			case DASHER:
				lore.add(ChatColor.BLUE+"Holiday Gear");
				lore.add(ChatColor.GOLD+""+ChatColor.BOLD+"T"+tier+" "+GenericFunctions.CapitalizeFirstLetters(set.name())+" Set");
				lore.add(ChatColor.YELLOW+"+"+ItemSet.GetBaseAmount(set, tier, 1)+"% Movement Speed");
				break;
			case DONNER:
				lore.add(ChatColor.BLUE+"Holiday Gear");
				lore.add(ChatColor.GOLD+""+ChatColor.BOLD+"T"+tier+" "+GenericFunctions.CapitalizeFirstLetters(set.name())+" Set");
				lore.add(ChatColor.YELLOW+"Attacking aggros enemies for "+ItemSet.GetBaseAmount(set, tier, 1)+" seconds");
				break;
			case OLIVE:
				lore.add(ChatColor.BLUE+"Holiday Gear");
				lore.add(ChatColor.GOLD+""+ChatColor.BOLD+"T"+tier+" "+GenericFunctions.CapitalizeFirstLetters(set.name())+" Set");
				lore.add(ChatColor.YELLOW+"When blocking, attackers take "+ItemSet.GetBaseAmount(set, tier, 1)+" True Damage");
				break;
			case PRANCER:
				lore.add(ChatColor.BLUE+"Holiday Gear");
				lore.add(ChatColor.GOLD+""+ChatColor.BOLD+"T"+tier+" "+GenericFunctions.CapitalizeFirstLetters(set.name())+" Set");
				lore.add(ChatColor.YELLOW+"Deals +"+ItemSet.GetBaseAmount(set, tier, 1)+" Additional Damage in Mid-Air");
				break;
			case RUDOLPH:
				lore.add(ChatColor.BLUE+"Holiday Gear");
				lore.add(ChatColor.GOLD+""+ChatColor.BOLD+"T"+tier+" "+GenericFunctions.CapitalizeFirstLetters(set.name())+" Set");
				lore.add(ChatColor.YELLOW+"In Dark Areas, gain "+ItemSet.GetBaseAmount(set, tier, 1)+"% Damage Reduction");
				break;
			case VIXEN:
				lore.add(ChatColor.BLUE+"Holiday Gear");
				lore.add(ChatColor.GOLD+""+ChatColor.BOLD+"T"+tier+" "+GenericFunctions.CapitalizeFirstLetters(set.name())+" Set");
				lore.add(ChatColor.YELLOW+"+"+ItemSet.GetBaseAmount(set, tier, 1)+"% Cooldown Reduction");
				break;
			}
		
		lore.add("");
		
		switch (set) {
			case PANROS:{
				lore.add(ChatColor.GOLD+""+ChatColor.ITALIC+"Set Bonus:");
				lore.add(ChatColor.DARK_AQUA+" 2 - "+ChatColor.WHITE+" +"+ItemSet.GetBaseAmount(set, tier, 2)+" Damage");
				lore.add(ChatColor.DARK_AQUA+" 3 - "+ChatColor.WHITE+" +"+ItemSet.GetBaseAmount(set, tier, 3)+"% Dodge Chance");
				lore.add(ChatColor.DARK_AQUA+" 4 - "+ChatColor.WHITE+" +"+ItemSet.GetBaseAmount(set, tier, 4)+"% Critical Chance");
				lore.add(ChatColor.DARK_AQUA+" 5 - "+ChatColor.WHITE+" Powered Line Drive"); 
				lore.add(ChatColor.WHITE+"      +50% Armor Penetration");  
				lore.add(ChatColor.WHITE+"      +15 Damage");
				lore.add(ChatColor.GRAY+" ");
				lore.add(ChatColor.GRAY+"    Press the drop key while performing the");
				lore.add(ChatColor.GRAY+"    first line drive to line drive a second");
				lore.add(ChatColor.GRAY+"    time in another direction.");
			}break;
			case SONGSTEEL:{
				lore.add(ChatColor.GOLD+""+ChatColor.ITALIC+"Set Bonus:");
				lore.add(ChatColor.DARK_AQUA+" 2 - "+ChatColor.WHITE+" +"+ItemSet.GetBaseAmount(set, tier, 2)+" Max Health");
				lore.add(ChatColor.DARK_AQUA+" 3 - "+ChatColor.WHITE+" +"+ItemSet.GetBaseAmount(set, tier, 3)+" Absorption Health (30 seconds)");
				lore.add(ChatColor.DARK_AQUA+" 4 - "+ChatColor.WHITE+" +"+ItemSet.GetBaseAmount(set, tier, 4)+"% Damage Reduction");
				lore.add(ChatColor.DARK_AQUA+" 5 - "+ChatColor.WHITE+" Vendetta");
				lore.add(ChatColor.GRAY+"    Blocking stores 40% of mitigation damage.");
				lore.add(ChatColor.GRAY+"    1% of damage is stored as thorns true damage.");
				lore.add(ChatColor.GRAY+"    Shift+Left-Click with a shield to unleash");
				lore.add(ChatColor.GRAY+"    all of your stored mitigation damage.");
				lore.add(ChatColor.GRAY+"    Vendetta stacks wear off after 10 seconds.");
				lore.add(ChatColor.GRAY+"    Can be refreshed by basic attacking monsters.");
			}break;
			case DAWNTRACKER:{
				lore.add(ChatColor.GOLD+""+ChatColor.ITALIC+"Set Bonus:");
				lore.add(ChatColor.DARK_AQUA+" 2 - "+ChatColor.WHITE+" +"+ItemSet.GetBaseAmount(set, tier, 2)+"% Debuff Resistance");
				lore.add(ChatColor.DARK_AQUA+" 3 - "+ChatColor.WHITE+" +"+ItemSet.GetBaseAmount(set, tier, 3)+"% Lifesteal");
				lore.add(ChatColor.DARK_AQUA+" 4 - "+ChatColor.WHITE+" +"+ItemSet.GetBaseAmount(set, tier, 4)+" Max Health");
				lore.add(ChatColor.DARK_AQUA+" 5 - "+ChatColor.WHITE+" Powered Mock"); 
				lore.add(ChatColor.WHITE+"    +50% Armor Penetration");  
				lore.add(ChatColor.WHITE+"    +15 Damage");
				lore.add(ChatColor.GRAY+"    Mock cooldown decreases from");
				lore.add(ChatColor.GRAY+"    20 -> 10 seconds, making it stackable.");
				lore.add(ChatColor.GRAY+"    All Lifesteal Stacks and Weapon Charges");
				lore.add(ChatColor.GRAY+"    gained are doubled.");
				lore.add(ChatColor.DARK_AQUA+" 6 - "+ChatColor.WHITE+""); 
				lore.add(ChatColor.WHITE+"    +"+(tier*25)+"% Lifesteal");
				lore.add(ChatColor.WHITE+"    +"+(tier*25)+"% Health Regeneration");
				lore.add(ChatColor.WHITE+"    +"+(tier*25)+"% Maximum Health");
			}break;
			case LORASYS:{
				lore.add(ChatColor.GOLD+""+ChatColor.ITALIC+"Increases in power based on "+ChatColor.BOLD+"Total Tier Amount");
				lore.add(ChatColor.GOLD+""+ChatColor.ITALIC+"of all baubles in your bauble pouch."); 
				lore.add(ChatColor.DARK_AQUA+" T9 - ");
				lore.add(ChatColor.WHITE+"    +50% Armor Penetration");  
				lore.add(ChatColor.WHITE+"    +15 Damage"); 
				if (tier>=2) {
					lore.add(ChatColor.DARK_AQUA+" T18 - ");
					lore.add(ChatColor.WHITE+"    +10% Critical Chance");
					lore.add(ChatColor.WHITE+"    Hitting enemies with Thorns does not damage you.");
					lore.add(ChatColor.WHITE+"    Each kill restores 2 Hearts (4 HP) instead of 1."); 
					if (tier>=3) {
						lore.add(ChatColor.DARK_AQUA+" T27 - ");
						lore.add(ChatColor.WHITE+"    +20% Critical Chance");
						lore.add(ChatColor.WHITE+"    Stealth does not cause durability to decrease.");
						lore.add(ChatColor.WHITE+"    Each kill restores 3 Hearts (6 HP) instead of 2."); 
						if (tier>=4) {
							lore.add(ChatColor.DARK_AQUA+" T40 - ");
							lore.add(ChatColor.WHITE+"    +55 Damage"); 
							lore.add(ChatColor.WHITE+"    +45% Critical Chance");
							lore.add(ChatColor.WHITE+"    +20% Cooldown Reduction");
						}
					}
				}
			}break;
			case JAMDAK: {
				lore.add(ChatColor.GOLD+""+ChatColor.ITALIC+"Set Bonus:");
				lore.add(ChatColor.DARK_AQUA+" 2 - "+ChatColor.WHITE+" +"+ItemSet.GetBaseAmount(set, tier, 2)+"% Dodge Chance");
				lore.add(ChatColor.DARK_AQUA+" 3 - "+ChatColor.WHITE+" +"+ItemSet.GetBaseAmount(set, tier, 3)+"% Dodge Chance");
				lore.add(ChatColor.DARK_AQUA+" 4 - "+ChatColor.WHITE+" +"+(ItemSet.GetBaseAmount(set, tier, 4)/20d)+"s Graceful Dodge");
				lore.add(ChatColor.GRAY+"      Gives you invulnerability and "+(ItemSet.GetBaseAmount(set, tier, 4)/4)+" absorption");
				lore.add(ChatColor.GRAY+"      health for each successful dodge.");
				lore.add(ChatColor.DARK_AQUA+" 5 - "+ChatColor.WHITE+" Boosts All Modes of Ranger"); 
				lore.add(ChatColor.WHITE+"      +50% Armor Penetration");  
				lore.add(ChatColor.WHITE+"      +15 Damage");
				lore.add(ChatColor.YELLOW+"    Close Range Mode: "+ChatColor.WHITE+"Tumble Invincibility increased by 0.5s");
				lore.add(ChatColor.YELLOW+"    Sniper Mode: "+ChatColor.WHITE+"+100% Critical Damage");
				lore.add(ChatColor.YELLOW+"    Debilitation Mode: "+ChatColor.WHITE+"+50% Armor Penetration");
			}break;
			case DARNYS: {
				lore.add(ChatColor.GOLD+""+ChatColor.ITALIC+"Set Bonus:");
				lore.add(ChatColor.DARK_AQUA+" 2 - "+ChatColor.WHITE+" +"+ItemSet.GetBaseAmount(set, tier, 2)+"% Damage Reduction");
				lore.add(ChatColor.DARK_AQUA+" 3 - "+ChatColor.WHITE+" +"+ItemSet.GetBaseAmount(set, tier, 3)+"% Damage Reduction");
				lore.add(ChatColor.DARK_AQUA+" 4 - "+ChatColor.WHITE+" Swift Aegis "+WorldShop.toRomanNumeral(ItemSet.GetBaseAmount(set, tier, 4)));
				lore.add(ChatColor.GRAY+"      Builds "+ItemSet.GetBaseAmount(set, tier, 4)+" stack"+((ItemSet.GetBaseAmount(set, tier, 4))!=1?"s":"")+" of Resist");
				lore.add(ChatColor.GRAY+"      ("+(ItemSet.GetBaseAmount(set, tier, 4)*10)+"% Damage Reduction) every 5 seconds of sprinting,");
				lore.add(ChatColor.GRAY+"      and with every Tumble. Each hit taken removes one");
				lore.add(ChatColor.GRAY+"      stack of Resist. Caps at Resist 10. Lasts 20 seconds.");
				lore.add(ChatColor.DARK_AQUA+" 5 - "+ChatColor.WHITE+" Boosts All Modes of Ranger"); 
				lore.add(ChatColor.WHITE+"      +50% Armor Penetration");  
				lore.add(ChatColor.WHITE+"      +15 Damage");
				lore.add(ChatColor.YELLOW+"    Close Range Mode: "+ChatColor.WHITE+"Tumble Invincibility increased by 0.5s");
				lore.add(ChatColor.YELLOW+"    Sniper Mode: "+ChatColor.WHITE+"+100% Critical Damage");
				lore.add(ChatColor.YELLOW+"    Debilitation Mode: "+ChatColor.WHITE+"+50% Armor Penetration");
			}break;
			case ALIKAHN: {
				lore.add(ChatColor.GOLD+""+ChatColor.ITALIC+"Set Bonus:");
				lore.add(ChatColor.DARK_AQUA+" 2 - "+ChatColor.WHITE+" +"+ItemSet.GetBaseAmount(set, tier, 2)+" Max Health");
				lore.add(ChatColor.DARK_AQUA+" 3 - "+ChatColor.WHITE+" +"+ItemSet.GetBaseAmount(set, tier, 3)+" Max Health");
				lore.add(ChatColor.DARK_AQUA+" 4 - "+ChatColor.WHITE+" +"+ItemSet.GetBaseAmount(set, tier, 4)+" Regen / 5 seconds");
				lore.add(ChatColor.DARK_AQUA+" 5 - "+ChatColor.WHITE+" Boosts All Modes of Ranger"); 
				lore.add(ChatColor.WHITE+"      +50% Armor Penetration");  
				lore.add(ChatColor.WHITE+"      +15 Damage");
				lore.add(ChatColor.YELLOW+"    Close Range Mode: "+ChatColor.WHITE+"Tumble Invincibility increased by 0.5s");
				lore.add(ChatColor.YELLOW+"    Sniper Mode: "+ChatColor.WHITE+"+100% Critical Damage");
				lore.add(ChatColor.YELLOW+"    Debilitation Mode: "+ChatColor.WHITE+"+50% Armor Penetration");
			}break;
			case LORASAADI:{
				lore.add(ChatColor.GOLD+""+ChatColor.ITALIC+"Set Bonus:");
				lore.add(ChatColor.DARK_AQUA+" 2 - "+ChatColor.WHITE+" +"+ItemSet.GetBaseAmount(set, tier, 2)+" Damage");
				lore.add(ChatColor.DARK_AQUA+" 3 - "+ChatColor.WHITE+" +"+ItemSet.GetBaseAmount(set, tier, 3)+" Damage");
				lore.add(ChatColor.DARK_AQUA+" 4 - "+ChatColor.WHITE+" +"+ItemSet.GetBaseAmount(set, tier, 4)+" Execution Damage");
				lore.add(ChatColor.DARK_AQUA+"         per 20% Missing Health");
				lore.add(ChatColor.DARK_AQUA+" 5 - "+ChatColor.WHITE+" Boosts All Modes of Ranger"); 
				lore.add(ChatColor.WHITE+"      +50% Armor Penetration");  
				lore.add(ChatColor.WHITE+"      +15 Damage");
				lore.add(ChatColor.YELLOW+"    Close Range Mode: "+ChatColor.WHITE+"Tumble Invincibility increased by 0.5s");
				lore.add(ChatColor.YELLOW+"    Sniper Mode: "+ChatColor.WHITE+"+100% Critical Damage");
				lore.add(ChatColor.YELLOW+"    Debilitation Mode: "+ChatColor.WHITE+"+50% Armor Penetration");
			}break;
			case GLADOMAIN:{
				lore.add(ChatColor.GOLD+""+ChatColor.ITALIC+"Set Bonus:");
				lore.add(ChatColor.DARK_AQUA+" 2 - "+ChatColor.WHITE+" +"+ItemSet.GetBaseAmount(set, tier, 2)+"% Cooldown Reduction");
				lore.add(ChatColor.DARK_AQUA+" 3 - "+ChatColor.WHITE+" +"+ItemSet.GetBaseAmount(set, tier, 3)+"% Dodge Chance");
				lore.add(ChatColor.DARK_AQUA+" 5 - "+ChatColor.WHITE+" Life Saver ");
				lore.add(ChatColor.GRAY+"      When about to be killed, puts you into");
				lore.add(ChatColor.GRAY+"      stealth, applies Speed IV for 10 seconds, adds");
				lore.add(ChatColor.GRAY+"      invulnerability, and de-aggros all current");
				lore.add(ChatColor.GRAY+"      targets.");
				lore.add(ChatColor.WHITE+"        5 Minute Cooldown");
				lore.add(ChatColor.DARK_AQUA+" 7 - "+ChatColor.WHITE+" Provides the Following Bonuses:");
				lore.add(ChatColor.GRAY+"    "+ChatColor.WHITE+"A successful Assassination grants 100%");
				lore.add(ChatColor.GRAY+"    "+ChatColor.WHITE+"Critical Strike Chance and 100% Dodge");
				lore.add(ChatColor.GRAY+"    "+ChatColor.WHITE+"chance for the next hit. Dodge Chance");
				lore.add(ChatColor.GRAY+"    "+ChatColor.WHITE+"increases by +"+(5+ItemSet.GetBaseAmount(set, tier, 4))+"% per 1m/sec of movement");
				lore.add(ChatColor.GRAY+"    "+ChatColor.WHITE+"speed.");
			}break;
			case MOONSHADOW:{
				lore.add(ChatColor.GOLD+""+ChatColor.ITALIC+"Set Bonus:");
				lore.add(ChatColor.DARK_AQUA+" 2 - "+ChatColor.WHITE+" Applies Poison "+WorldShop.toRomanNumeral(ItemSet.GetBaseAmount(set, tier, 2))+ChatColor.GRAY+" (0:15)");
				lore.add(ChatColor.DARK_AQUA+" 3 - "+ChatColor.WHITE+" +"+ItemSet.GetBaseAmount(set, tier, 3)+"% Damage");
				lore.add(ChatColor.DARK_AQUA+" 5 - "+ChatColor.WHITE+" +"+ItemSet.GetBaseAmount(set, tier, 4)+"% Critical Chance");
				lore.add(ChatColor.DARK_AQUA+" 7 - "+ChatColor.WHITE+" Provides the Following Bonuses:");
				lore.add(ChatColor.GRAY+"    "+ChatColor.WHITE+"Strength Cap Increases to 40. 2 Stacks per kill.");
				lore.add(ChatColor.GRAY+"    "+ChatColor.WHITE+"Successful Assassinations apply damage");
				lore.add(ChatColor.GRAY+"      "+ChatColor.WHITE+"in an AoE Range.");
				lore.add(ChatColor.GRAY+"    "+ChatColor.WHITE+"Slayers can drop aggro by sneaking");
				lore.add(ChatColor.GRAY+"      "+ChatColor.WHITE+"for Three seconds.");
				lore.add(ChatColor.GRAY+"    "+ChatColor.WHITE+"While in Stealth Mode you gain 40%");
				lore.add(ChatColor.GRAY+"      "+ChatColor.WHITE+"Dodge Chance");
			}break;
			case WOLFSBANE:{
				lore.add(ChatColor.GOLD+""+ChatColor.ITALIC+"Set Bonus:");
				lore.add(ChatColor.DARK_AQUA+" 2 - "+ChatColor.WHITE+" Recovers "+ItemSet.GetBaseAmount(set, tier, 2)+"% Cooldown on Assassination per kill");
				lore.add(ChatColor.DARK_AQUA+" 3 - "+ChatColor.WHITE+" Applies Speed V when Assassination is casted. Suppresses");
				lore.add(ChatColor.DARK_AQUA+"     "+ChatColor.WHITE+"      the target for "+(ItemSet.GetBaseAmount(set, tier, 3)/20d)+"s");
				lore.add(ChatColor.DARK_AQUA+" 5 - "+ChatColor.WHITE+" Gain "+(ItemSet.GetBaseAmount(set, tier, 4)/20d)+" seconds of invulnerability after");
				lore.add(ChatColor.DARK_AQUA+"     "+ChatColor.WHITE+"      Assassination is casted.");
				lore.add(ChatColor.DARK_AQUA+" 7 - "+ChatColor.WHITE+" Provides the Following Bonuses:");
				lore.add(ChatColor.GRAY+"    "+ChatColor.WHITE+"Backstabs heal 2 HP (1 Heart). Assassination cooldown reduced");
				lore.add(ChatColor.GRAY+"    "+ChatColor.WHITE+"to 2 seconds when used on a target closer than 5 meters.");
			}break;
			case ALUSTINE:{
				lore.add(ChatColor.GOLD+""+ChatColor.ITALIC+"Set Bonus:");
				lore.add(ChatColor.DARK_AQUA+" 2 - "+ChatColor.WHITE+" Gain immunity to Explosions.");
				lore.add(ChatColor.DARK_AQUA+"     "+ChatColor.WHITE+"      Consumes "+ChatColor.YELLOW+ItemSet.GetBaseAmount(set, tier, 2)+" XP"+ChatColor.WHITE+" per absorbed hit.");
				lore.add(ChatColor.DARK_AQUA+"       "+ChatColor.GRAY+ChatColor.ITALIC+"Must have at least "+ChatColor.YELLOW+ChatColor.ITALIC+ItemSet.GetBaseAmount(set, tier, 2)+" XP"+ChatColor.GRAY+ChatColor.ITALIC+" to trigger.");
				lore.add(ChatColor.DARK_AQUA+" 3 - "+ChatColor.WHITE+" Resists all fire, poison, and wither damage.");
				lore.add(ChatColor.DARK_AQUA+"     "+ChatColor.WHITE+"      Consumes "+ChatColor.YELLOW+ItemSet.GetBaseAmount(set, tier, 3)+" XP"+ChatColor.WHITE+" per absorbed hit.");
				lore.add(ChatColor.DARK_AQUA+"       "+ChatColor.GRAY+ChatColor.ITALIC+"Must have at least "+ChatColor.YELLOW+ChatColor.ITALIC+ItemSet.GetBaseAmount(set, tier, 3)+" XP"+ChatColor.GRAY+ChatColor.ITALIC+" to trigger.");
				lore.add(ChatColor.DARK_AQUA+" 5 - "+ChatColor.WHITE+" Backstabs spill "+ChatColor.YELLOW+ItemSet.GetBaseAmount(set, tier, 4)+" XP"+ChatColor.WHITE+" out from the target hit.");
				lore.add(ChatColor.DARK_AQUA+"     "+ChatColor.WHITE+"      Collecting experience has a "+Math.min((ItemSet.GetBaseAmount(set, tier, 4)/20d)*100d,100)+"% chance");
				lore.add(ChatColor.DARK_AQUA+"     "+ChatColor.WHITE+"      to restore 2 HP (1 Heart).");
				lore.add(ChatColor.DARK_AQUA+" 7 - "+ChatColor.WHITE+" Provides the Following Bonuses:");
				lore.add(ChatColor.GRAY+"    "+ChatColor.WHITE+"Deals true damage equal to the number");
				lore.add(ChatColor.GRAY+"    "+ChatColor.WHITE+"of levels you have. Drains XP equal to");
				lore.add(ChatColor.GRAY+"    "+ChatColor.WHITE+"the number of levels you have per hit.");
			}break;
		case BLITZEN:
			lore.add(ChatColor.GOLD+""+ChatColor.ITALIC+"Set Bonus:");
			lore.add(ChatColor.DARK_AQUA+" 2 - "+ChatColor.WHITE+" +"+ItemSet.GetBaseAmount(set, tier, 2)+" Damage");
			lore.add(ChatColor.DARK_AQUA+" 3 - "+ChatColor.WHITE+" +"+ItemSet.GetBaseAmount(set, tier, 3)+" Health");
			lore.add(ChatColor.DARK_AQUA+" 4 - "+ChatColor.WHITE+" Storm Onward!");
			lore.add(ChatColor.GRAY+"    Attacks occasionally send Lightning bolts");
			lore.add(ChatColor.GRAY+"    down on foes dealing true damage.");
			break;
		case COMET:
			lore.add(ChatColor.GOLD+""+ChatColor.ITALIC+"Set Bonus:");
			lore.add(ChatColor.DARK_AQUA+" 2 - "+ChatColor.WHITE+" +"+ItemSet.GetBaseAmount(set, tier, 2)+" Health");
			lore.add(ChatColor.DARK_AQUA+" 3 - "+ChatColor.WHITE+" +"+ItemSet.GetBaseAmount(set, tier, 3)+" Damage");
			lore.add(ChatColor.DARK_AQUA+" 4 - "+ChatColor.WHITE+" More Health For You");
			lore.add(ChatColor.GRAY+"    Right-Clicking players will take away");
			lore.add(ChatColor.GRAY+"    10% of your health to heal 20% of");
			lore.add(ChatColor.GRAY+"    the targeted ally's health.");
			break;
		case CUPID:
			lore.add(ChatColor.GOLD+""+ChatColor.ITALIC+"Set Bonus:");
			lore.add(ChatColor.DARK_AQUA+" 2 - "+ChatColor.WHITE+" +"+ItemSet.GetBaseAmount(set, tier, 2)+" Health");
			lore.add(ChatColor.DARK_AQUA+" 3 - "+ChatColor.WHITE+" +"+ItemSet.GetBaseAmount(set, tier, 3)+" Damage");
			lore.add(ChatColor.DARK_AQUA+" 4 - "+ChatColor.WHITE+" Linked for Life");
			lore.add(ChatColor.GRAY+"    Right-Clicking players will link yourself");
			lore.add(ChatColor.GRAY+"    to them. Teleporting via any means sends");
			lore.add(ChatColor.GRAY+"    you both to the new position.");
			break;
		case DANCER:
			lore.add(ChatColor.GOLD+""+ChatColor.ITALIC+"Set Bonus:");
			lore.add(ChatColor.DARK_AQUA+" 2 - "+ChatColor.WHITE+" +"+ItemSet.GetBaseAmount(set, tier, 2)+" Damage");
			lore.add(ChatColor.DARK_AQUA+" 3 - "+ChatColor.WHITE+" +"+ItemSet.GetBaseAmount(set, tier, 3)+" Health");
			lore.add(ChatColor.DARK_AQUA+" 4 - "+ChatColor.WHITE+" Can't Catch Me!");
			lore.add(ChatColor.GRAY+"    Changing your movement direction constantly");
			lore.add(ChatColor.GRAY+"    makes you invulnerable to incoming attacks.");
			break;
		case DASHER:
			lore.add(ChatColor.GOLD+""+ChatColor.ITALIC+"Set Bonus:");
			lore.add(ChatColor.DARK_AQUA+" 2 - "+ChatColor.WHITE+" +"+ItemSet.GetBaseAmount(set, tier, 2)+" Damage");
			lore.add(ChatColor.DARK_AQUA+" 3 - "+ChatColor.WHITE+" +"+ItemSet.GetBaseAmount(set, tier, 3)+" Health");
			lore.add(ChatColor.DARK_AQUA+" 4 - "+ChatColor.WHITE+" Marathon Runner");
			lore.add(ChatColor.GRAY+"    Sprinting will restore missing hunger.");
			break;
		case DONNER:
			lore.add(ChatColor.GOLD+""+ChatColor.ITALIC+"Set Bonus:");
			lore.add(ChatColor.DARK_AQUA+" 2 - "+ChatColor.WHITE+" +"+ItemSet.GetBaseAmount(set, tier, 2)+" Health");
			lore.add(ChatColor.DARK_AQUA+" 3 - "+ChatColor.WHITE+" +"+ItemSet.GetBaseAmount(set, tier, 3)+" Damage");
			lore.add(ChatColor.DARK_AQUA+" 4 - "+ChatColor.WHITE+" Come At Me");
			lore.add(ChatColor.GRAY+"    Monsters attacking your party members");
			lore.add(ChatColor.GRAY+"    will automatically be provoked to you.");
			break;
		case OLIVE:
			lore.add(ChatColor.GOLD+""+ChatColor.ITALIC+"Set Bonus:");
			lore.add(ChatColor.DARK_AQUA+" 2 - "+ChatColor.WHITE+" +"+ItemSet.GetBaseAmount(set, tier, 2)+" Health");
			lore.add(ChatColor.DARK_AQUA+" 3 - "+ChatColor.WHITE+" +"+ItemSet.GetBaseAmount(set, tier, 3)+" Damage");
			lore.add(ChatColor.DARK_AQUA+" 4 - "+ChatColor.WHITE+" Right Back At You");
			lore.add(ChatColor.GRAY+"    Gain 20 Absorption Health each time");
			lore.add(ChatColor.GRAY+"    damage is taken. (30 sec cooldown)");
			break;
		case PRANCER:
			lore.add(ChatColor.GOLD+""+ChatColor.ITALIC+"Set Bonus:");
			lore.add(ChatColor.DARK_AQUA+" 2 - "+ChatColor.WHITE+" +"+ItemSet.GetBaseAmount(set, tier, 2)+" Damage");
			lore.add(ChatColor.DARK_AQUA+" 3 - "+ChatColor.WHITE+" +"+ItemSet.GetBaseAmount(set, tier, 3)+" Health");
			lore.add(ChatColor.DARK_AQUA+" 4 - "+ChatColor.WHITE+" Will You Just Sit Down?");
			lore.add(ChatColor.GRAY+"    Your next strike ignores 50% of the");
			lore.add(ChatColor.GRAY+"    target's armor. (10 sec cooldown)");
			break;
		case RUDOLPH:
			lore.add(ChatColor.GOLD+""+ChatColor.ITALIC+"Set Bonus:");
			lore.add(ChatColor.DARK_AQUA+" 2 - "+ChatColor.WHITE+" +"+ItemSet.GetBaseAmount(set, tier, 2)+" Health");
			lore.add(ChatColor.DARK_AQUA+" 3 - "+ChatColor.WHITE+" +"+ItemSet.GetBaseAmount(set, tier, 3)+" Damage");
			lore.add(ChatColor.DARK_AQUA+" 4 - "+ChatColor.WHITE+" Light the Way");
			lore.add(ChatColor.GRAY+"    You and your party gain Permanent");
			lore.add(ChatColor.GRAY+"    Night Vision.");
			break;
		case VIXEN:
			lore.add(ChatColor.GOLD+""+ChatColor.ITALIC+"Set Bonus:");
			lore.add(ChatColor.DARK_AQUA+" 2 - "+ChatColor.WHITE+" +"+ItemSet.GetBaseAmount(set, tier, 2)+" Damage");
			lore.add(ChatColor.DARK_AQUA+" 3 - "+ChatColor.WHITE+" +"+ItemSet.GetBaseAmount(set, tier, 3)+" Health");
			lore.add(ChatColor.DARK_AQUA+" 4 - "+ChatColor.WHITE+" Untouchable, Unkillable");
			lore.add(ChatColor.GRAY+"    Increases Dodge Chance by 20%. Dodging");
			lore.add(ChatColor.GRAY+"    successfully restores 10% of your max");
			lore.add(ChatColor.GRAY+"    health.");
			break;
		}
		return lore;
	}

	public static void SetItemSet(ItemStack item, ItemSet set) {
		//Convert this item to a different set.
		boolean found=false;
		if (isSetItem(item) &&
				item.getItemMeta().hasLore()) {
			List<String> lore = item.getItemMeta().getLore();
			for (int i=0;i<lore.size();i++) {
				if (lore.get(i).contains(ChatColor.GOLD+""+ChatColor.BOLD+"T")) {
					//This is the tier line.
					ItemSet oldset=GetItemSet(item);
					//TwosideKeeper.log("In lore: "+lore.get(i)+". Old tier: "+oldtier,2);
					//lore.set(i, lore.get(i).replace("T"+oldtier, "T"+tier));
					lore.set(i, lore.get(i).replace(GenericFunctions.CapitalizeFirstLetters(oldset.name()), GenericFunctions.CapitalizeFirstLetters(set.name())));
					found=true;
					break;
				}
			}
			ItemMeta m = item.getItemMeta();
			m.setLore(lore);
			item.setItemMeta(m);
			GenericFunctions.UpdateItemLore(item); //Update this item now that we upgraded the tier.
			if (!found) {
				TwosideKeeper.log(ChatColor.RED+"[ERROR] Could not detect proper tier of "+item.toString()+"!", 1);
			}
		} 
	}

	public static int GetBaubleTier(Player p) {
		int tier = 0;
		if (BaublePouch.isBaublePouch(p.getEquipment().getItemInOffHand())) {
			int id = BaublePouch.getBaublePouchID(p.getEquipment().getItemInOffHand());
			/*List<ItemStack> contents = BaublePouch.getBaublePouchContents(id);
			for (ItemStack item : contents) {
				tier += ItemSet.GetTier(item);
			}*/
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			for (ItemSet set : bauble_sets) {
				TwosideKeeper.log("Checking for set "+set.name(), 5);
				if (pd.itemsets.containsKey(set.name())) {
					tier+=GetTotalBaubleTier(pd.itemsets,set.name());
				}
			}
		}
		return tier;
	}
	
	private static int GetTotalBaubleTier(HashMap<String, HashMap<Integer, Integer>> itemsets, String name) {
		if (itemsets.containsKey(name)) {
			//TwosideKeeper.log("Found key "+name, 0);
			HashMap<Integer,Integer> tiermap = itemsets.get(name);
			int tiers = 0;
			for (Integer tier : tiermap.keySet()) {
				tiers += tier*tiermap.get(tier);
				TwosideKeeper.log("Tiers increased by: "+(tier*tiermap.get(tier))+". Total: "+tiers, 5);
			}
			return tiers;
		}
		TwosideKeeper.log("Set does not exist in map! Could not get tier! THIS SHOULD NOT BE HAPPENING!!", 0);
		DebugUtils.showStackTrace();
		return -1;
	}

	public static void updateItemSets(Player p) {
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		HashMap<String,HashMap<Integer,Integer>> map = pd.itemsets;
		map.clear();
		for (ItemStack item : GenericFunctions.getEquipment(p, true)) {
			if (isSetItem(item) && item.getType()!=Material.SKULL_ITEM) {
				ItemSet set = GetItemSet(item);
				Integer tier = GetItemTier(item);
				insertSetIntoMap(map,set,tier);
			} else
			if (p.getInventory().getExtraContents()[0]!=null && p.getInventory().getExtraContents()[0].equals(item) && BaublePouch.isBaublePouch(item)) {
				List<ItemStack> contents = BaublePouch.getBaublePouchContents(BaublePouch.getBaublePouchID(item));
				for (ItemStack it : contents) {
					if (isSetItem(it)) {
						ItemSet set = GetItemSet(it);
						Integer tier = GetItemTier(it);
						insertSetIntoMap(map,set,tier);	
					}
				}
			}
			TwosideKeeper.log("Offhand: "+p.getInventory().getExtraContents()[0]+";;"+item+ChatColor.RESET+"Is equal? "+p.getInventory().getExtraContents()[0].equals(item)+";; Is Bauble Pouch? "+BaublePouch.isBaublePouch(item), 5);
		}
		TwosideKeeper.log("Updated HashMap for player "+p.getName()+". New Map: \n"+TextUtils.outputHashmap(map), 5);
	}

	private static void insertSetIntoMap(HashMap<String, HashMap<Integer,Integer>> map, ItemSet set, Integer tier) {
		String keystring = set.name();
		if (map.containsKey(keystring)) {
			HashMap<Integer,Integer> innermap = map.get(keystring);
			if (innermap.containsKey(tier)) {
				innermap.put(tier, innermap.get(tier)+1);
			} else {
				innermap.put(tier, 1);
			}
			//map.put(keystring, innermap);
		} else {
			HashMap<Integer,Integer> innermap = new HashMap<Integer,Integer>();
			innermap.put(tier, 1);
			map.put(keystring, innermap);
			//map.put(keystring, 1);
		}
	}

	public static boolean HasSetBonusBasedOnSetBonusCount(Player player, ItemSet set, int count) {
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(player);
		if (pd.itemsets.containsKey(set.name())) {
			HashMap<Integer,Integer> tiermap = pd.itemsets.get(set.name());
			for (Integer tier : tiermap.keySet()) {
				if (tiermap.get(tier)>=count) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean meetsLorasysSwordConditions(int baubletier, int swordtier, Player p) {
		//TwosideKeeper.log("["+baubletier+"||"+swordtier+"] Is a Lorasys Set? "+ItemSet.HasSetBonusBasedOnSetBonusCount(p, ItemSet.LORASYS, 1)+";;Bauble Tier: "+(ItemSet.GetBaubleTier(p))+"/"+baubletier+";;Meets Sword Requirement? "+((swordtier==1 || ItemSet.GetItemTier(p.getEquipment().getItemInMainHand())>=swordtier)), 0);
		return ItemSet.HasSetBonusBasedOnSetBonusCount(p, ItemSet.LORASYS, 1) && ItemSet.GetBaubleTier(p)>=baubletier && (swordtier==1 || ItemSet.GetItemTier(p.getEquipment().getItemInMainHand())>=swordtier);
	}
	
	/**
	 * Purely for API support. DO NOT USE OTHERWISE!!
	 */
	public static ItemSet GetSet(ItemStack item) {
		return GetItemSet(item);
	}

	/**
	 * Purely for API support. DO NOT USE OTHERWISE!!
	 */
	public static int GetTier(ItemStack item) {
		return GetItemTier(item);
	}
}
