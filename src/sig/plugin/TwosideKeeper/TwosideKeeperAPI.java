package sig.plugin.TwosideKeeper;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import sig.plugin.TwosideKeeper.HelperStructures.MonsterDifficulty;
import sig.plugin.TwosideKeeper.HelperStructures.MonsterType;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;

public final class TwosideKeeperAPI {
	//MONEY COMMANDS.
	public static void givePlayerMoney(Player p, double amt) {
		TwosideKeeper.givePlayerMoney(p, amt);
	}
	public static void givePlayerMoney(String p, double amt) {
		TwosideKeeper.givePlayerMoney(p, amt);
	}
	public static double getPlayerMoney(Player p) {
		return TwosideKeeper.getPlayerMoney(p);
	}
	public static double getPlayerMoney(String p) {
		return TwosideKeeper.getPlayerMoney(p);
	}

	//BANK COMMANDS.
	public static void givePlayerBankMoney(Player p, double amt) {
		TwosideKeeper.givePlayerBankMoney(p, amt);
	}
	public static void givePlayerBankMoney(String p, double amt) {
		TwosideKeeper.givePlayerBankMoney(p, amt);
	}
	public static double getPlayerBankMoney(Player p) {
		return TwosideKeeper.getPlayerBankMoney(p);
	}
	public static double getPlayerBankMoney(String p) {
		return TwosideKeeper.getPlayerBankMoney(p);
	}
	
	//MONSTER COMMANDS.
	public static Monster spawnAdjustedMonster(MonsterType mt,Location loc) {
		return MonsterController.spawnAdjustedMonster(mt,loc);
	}
	
	public static Monster autoAdjustMonster(Monster m) {
		return MonsterController.convertMonster(m);
	}

	public static Monster adjustMonsterDifficulty(Monster m, MonsterDifficulty newdiff) {
		return MonsterController.convertMonster(m,newdiff);
	}
	
	public static MonsterDifficulty getMonsterDifficulty(Monster m) {
		return MonsterController.getMonsterDifficulty(m);
	}

	//Hardened Item Commands.
	public static boolean isHardenedItem(ItemStack i) {
		return GenericFunctions.isHardenedItem(i);
	}
	public static int getHardenedItemBreaks(ItemStack i) {
		return GenericFunctions.getHardenedItemBreaks(i);
	}
	public static ItemStack breakHardenedItem(ItemStack i) {
		return GenericFunctions.breakHardenedItem(i);
	}

	//Friendly Name COMMANDS.
	public static String getLocalizedItemName(ItemStack i) {
		return GenericFunctions.UserFriendlyMaterialName(i);
	}
}
