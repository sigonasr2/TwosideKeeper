package sig.plugin.TwosideKeeper.Drops;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import aPlugin.Drop;
import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.HelperStructures.ItemSet;
import sig.plugin.TwosideKeeper.HelperStructures.LivingEntityDifficulty;
import sig.plugin.TwosideKeeper.HelperStructures.Loot;
import sig.plugin.TwosideKeeper.HelperStructures.MonsterDifficulty;
import sig.plugin.TwosideKeeper.HelperStructures.PlayerMode;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;

public class SigDrop extends Drop{
	
	final public static int ARMOR=0;
	final public static int WEAPON=1;
	final public static int TOOL=2;
	final public static boolean HARDENED=true;
	final public static boolean NONHARDENED=false;
	final public static boolean SET=true;
	final public static boolean NONSET=false;
	boolean isHardened; //If set to false, it has no breaks remaining.
	boolean isSet; //If set to false, it's non-set.
	int isWeapon; //0: Armor, 1: Weapon, 2: Tool
	LivingEntityDifficulty diff;

	public SigDrop(int amount, int weight, String description, boolean isHardened, boolean isSet, int isWeapon, LivingEntityDifficulty normal) {
		super(amount, weight, 
				"["+GenericFunctions.CapitalizeFirstLetters(normal.name().replace("_", " "))+"]"+
				((isHardened)?" Hardened":"")+
				" Mega"+
				(isSet?" Set":"")+
				(isWeapon==0?" Armor":isWeapon==1?" Weapon":" Tool")
		);
		this.isHardened=isHardened;
		this.isSet=isSet;
		this.isWeapon=isWeapon;
		this.diff=normal;
	}

	@Override
	public ItemStack getItemStack(Player p) {
		ItemStack item;
		String toolprefix = "";
		String armorprefix = "";
		String armorsuffix = "";
		String toolsuffix = "";
		
		if (Math.random()<=0.33) {
			armorsuffix="BOOTS";
		} else 
		if (Math.random()<=0.33) {
			armorsuffix="HELMET";
		} else
		if (Math.random()<=0.33) {
			armorsuffix="LEGGINGS";
		} else
		{
			armorsuffix="CHESTPLATE";
		}
		
		if (Math.random()<=0.33) { //Funny how this works, the rarest tool is pickaxe, the most common is hoe
			toolsuffix="HOE";  
		} else 
		if (Math.random()<=0.33) {
			toolsuffix="SPADE";
		} else
		if (Math.random()<=0.33) {
			toolsuffix="PICKAXE";
		} else
		{
			toolsuffix="AXE";
		}
		//hoe is 0.33ish, spade is 0.2178ish, pickaxe is 0.143748ish, axe is 0.287496ish
		//Same ones apply to boots,helm,leggings,chest? Maybe im wrong
		
		switch (diff) {
			case NORMAL:{ //We are stone/wood tier.
				if (Math.random()<=0.3) {
					toolprefix = "WOOD";
					armorprefix = "LEATHER";
				} else {
					toolprefix = "STONE";
					armorprefix = "IRON";
				}
			}break;
			case DANGEROUS:{
				toolprefix = "IRON";
				armorprefix = "DIAMOND";
			}break;
			case DEADLY:{
				if (Math.random()<=0.3) {
					toolprefix = "DIAMOND";
				} else {
					toolprefix = "IRON";
				}
				armorprefix = "GOLD";
			}break;
			case HELLFIRE:{
				if (Math.random()<=0.8) {
					toolprefix = "DIAMOND";
				} else {
					toolprefix = "IRON";
				}
				armorprefix = "GOLD";
			}break;
			case END:{
				toolprefix = "DIAMOND";
				armorprefix = "GOLD";
			}break;
			default: {
				if (Math.random()<=0.3) {
					toolprefix = "WOOD";
					armorprefix = "LEATHER";
				} else {
					toolprefix = "STONE";
					armorprefix = "IRON";
				}
			}
		}
		
		switch (isWeapon) {
			case ARMOR: {
				item = new ItemStack(Material.valueOf(armorprefix+"_"+armorsuffix));
				item = CreateModifiedLootPiece(p, item, diff);
			}break;
			case WEAPON: {
				item = new ItemStack(Material.valueOf(toolprefix+"_SWORD"));
				item = CreateModifiedLootPiece(p, item, diff);
			}break;
			case TOOL: {
				item = new ItemStack(Material.valueOf(toolprefix+"_"+toolsuffix));
				item = Loot.GenerateMegaPiece(item.getType(), isHardened);
			}break;
			default:{
				TwosideKeeper.log("Something went terribly wrong generating the item! DIRT is being returned! Check your switch statement for 'isWeapon'.", 0);
				item = new ItemStack(Material.DIRT);
			}
		}
		return item;
	}

	public ItemStack CreateModifiedLootPiece(Player p, ItemStack item, LivingEntityDifficulty diff2) {
		if (isSet) {
			ItemSet set = ItemSet.PANROS;
			if (TwosideKeeper.CHRISTMASEVENT_ACTIVATED || TwosideKeeper.CHRISTMASLINGERINGEVENT_ACTIVATED) {
				if (Math.random()<=0.01) {
					set = LivingEntityDifficulty.PickAHolidayItemSet(PlayerMode.getPlayerMode(p),diff2); //This is the set we have to generate.
				} else {
					set = LivingEntityDifficulty.PickAnItemSet(PlayerMode.getPlayerMode(p),diff2); //This is the set we have to generate.
				}
			} else {
				set = LivingEntityDifficulty.PickAnItemSet(PlayerMode.getPlayerMode(p),diff2); //This is the set we have to generate.
			}
			TwosideKeeper.log("Set Chosen: "+set, 5);
			//Turn it into the appropriate piece if necessary.
			item = LivingEntityDifficulty.ConvertSetPieceIfNecessary(item, set);
			
			int tierbonus=0;
			if (item.getType().name().contains("LEATHER")) {
				GetTierBonusBasedOnDifficulty(diff);
			}
			
			item = Loot.GenerateSetPiece(item, set, isHardened, tierbonus);
			TwosideKeeper.log("Final Item: "+item, 5);
		} else {
			item = Loot.GenerateMegaPiece(item.getType(), isHardened);
		}
		return item;
	}
	
	private int GetTierBonusBasedOnDifficulty(LivingEntityDifficulty diff2) {
		switch (diff2) {
			case DANGEROUS:{ 
				if (Math.random()<=1/3d) {
					return 1;
				} else {
					return 0;
				}
			}
			case DEADLY:{ 
				if (Math.random()<=2/3d) {
					return 1;
				} else {
					return 0;
				}
			}
			case HELLFIRE:{ 
				return 1;
			}
			case END:{ 
				if (Math.random()<=1/3d) {
					return 2;
				} else {
					return 1;
				}
			}
			default:{
				return 0;
			}
		}
	}

	@Override
	public ItemStack getItemStack() {
		if (Bukkit.getOnlinePlayers().size()>0) {
			int random = (int)(Math.random()*(Bukkit.getOnlinePlayers().size()));
			Player picked = (Player)(Bukkit.getOnlinePlayers().toArray()[random]);
			return getItemStack(picked);
		} else {
			return null;
		}
	}
}
