package sig.plugin.TwosideKeeper.Drops;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import aPlugin.Drop;
import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.HelperStructures.ItemSet;
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
	MonsterDifficulty diff;

	public SigDrop(int amount, int weight, String description, boolean isHardened, boolean isSet, int isWeapon, MonsterDifficulty diff) {
		super(amount, weight, 
				"["+GenericFunctions.CapitalizeFirstLetters(diff.name().replace("_", " "))+"]"+
				((isHardened)?" Hardened":"")+
				" Mega"+
				(isSet?" Set":"")+
				(isWeapon==0?" Armor":isWeapon==1?" Weapon":" Tool")
		);
		this.isHardened=isHardened;
		this.isSet=isSet;
		this.isWeapon=isWeapon;
		this.diff=diff;
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
		
		if (Math.random()<=0.33) {
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
				item = CreateModifiedLootPiece(p, item);
			}break;
			case WEAPON: {
				item = new ItemStack(Material.valueOf(toolprefix+"_SWORD"));
				item = CreateModifiedLootPiece(p, item);
			}break;
			case TOOL: {
				item = new ItemStack(Material.valueOf(toolprefix+"_"+toolsuffix));
				item = Loot.GenerateMegaPiece(item.getType(), isHardened);
			}break;
			default:{
				TwosideKeeper.log("Something went terrible wrong generating the item! DIRT is being returned! Check your switch statement for 'isWeapon'.", 0);
				item = new ItemStack(Material.DIRT);
			}
		}
		return item;
	}

	public ItemStack CreateModifiedLootPiece(Player p, ItemStack item) {
		if (isSet) {
			ItemSet set = MonsterDifficulty.PickAnItemSet(PlayerMode.getPlayerMode(p)); //This is the set we have to generate.
			//Turn it into the appropriate piece if necessary.
			item = MonsterDifficulty.ConvertSetPieceIfNecessary(item, set);
			
			int tierbonus=0;
			if (item.getType().name().contains("LEATHER")) {
				GetTierBonusBasedOnDifficulty(diff);
			}
			
			item = Loot.GenerateSetPiece(item, set, isHardened, tierbonus);
		} else {
			item = Loot.GenerateMegaPiece(item.getType(), isHardened);
		}
		return item;
	}
	
	private int GetTierBonusBasedOnDifficulty(MonsterDifficulty dif) {
		switch (dif) {
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
		TwosideKeeper.log("Something went terribly wrong with getItemStack() call. Check to make sure you are using getSingleDrop(Player) and not getSingleDrop()!!!", 0);
		return null;
	}
}
