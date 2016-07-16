package sig.plugin.TwosideKeeper;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import sig.plugin.TwosideKeeper.HelperStructures.ArtifactAbility;
import sig.plugin.TwosideKeeper.HelperStructures.ArtifactItem;
import sig.plugin.TwosideKeeper.HelperStructures.Loot;
import sig.plugin.TwosideKeeper.HelperStructures.MonsterDifficulty;
import sig.plugin.TwosideKeeper.HelperStructures.MonsterType;
import sig.plugin.TwosideKeeper.HelperStructures.ServerType;
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

	//Artifact Commands.
	public static boolean isArtifactItem(ItemStack item) {
		return Artifact.isArtifact(item);
	}
	public static boolean isArtifactEquip(ItemStack item) {
		return GenericFunctions.isArtifactEquip(item);
	}
	public static ItemStack dropArtifactItem(ArtifactItem type) {
		return Artifact.createArtifactItem(type);
	}
	public static ItemStack dropArtifactItem(ArtifactItem type,int amt) {
		return Artifact.createArtifactItem(type,amt);
	}
	public static ItemStack addArtifactEXP(ItemStack item, int amt, Player p) {
		return AwakenedArtifact.addPotentialEXP(item, amt, p);
	}
	public static boolean hasArtifactAbility(ArtifactAbility ability, ItemStack item) {
		return ArtifactAbility.containsEnchantment(ability, item);
	}
	public static int getArtifactAbilityLevel(ArtifactAbility ability, ItemStack item) {
		return ArtifactAbility.getEnchantmentLevel(ability, item);
	}
	
	//Time Commands.
	public static long getServerTickTime() {
		return TwosideKeeper.getServerTickTime();
	}
	
	//Hardened Item Commands.
	public static boolean isHardenedItem(ItemStack i) {
		return GenericFunctions.isHardenedItem(i);
	}
	public static boolean isObscureHardenedItem(ItemStack i) {
		return GenericFunctions.isObscureHardenedItem(i);
	}
	public static int getHardenedItemBreaks(ItemStack i) {
		return GenericFunctions.getHardenedItemBreaks(i);
	}
	public static ItemStack addHardenedItemBreaks(ItemStack i, int breaks) {
		return GenericFunctions.addHardenedItemBreaks(i, breaks);
	}
	public static ItemStack addObscureHardenedItemBreaks(ItemStack i, int breaks) {
		return GenericFunctions.addObscureHardenedItemBreaks(i, breaks);
	}
	public static ItemStack breakHardenedItem(ItemStack i) {
		return GenericFunctions.breakHardenedItem(i,null);
	}
	public static ItemStack breakHardenedItem(ItemStack i, Player p) {
		return GenericFunctions.breakHardenedItem(i,p);
	}
	
	//Loot Commands.
	public static ItemStack generateMegaPiece(Material item, boolean hardened) {
		return Loot.GenerateMegaPiece(item, hardened);
	}
	
	//Server COMMANDS.
	public static ServerType getServerType() {
		return TwosideKeeper.getServerType();
	}
	public static void announcePluginVersions() {
		TwosideKeeper.announcePluginVersions();
	}
	
	//Party COMMANDS.
	public static List<Player> getPartyMembers(Player p) {
		return Party.getPartyMembers(p);
	}

	//Combat COMMANDS.
	public static double getModifiedDamage(double dmg_amt, LivingEntity p) {
		return TwosideKeeper.CalculateDamageReduction(dmg_amt, p, p);
	}
	public static void DealModifiedDamageToEntity(int dmg, LivingEntity damager, LivingEntity target) {
		GenericFunctions.DealDamageToMob(dmg, target, damager, false);
	}
	public static void DealTrueDamageToEntity(int dmg, LivingEntity damager, LivingEntity target) {
		GenericFunctions.DealDamageToMob(dmg, target, damager, true);
	}
	public static void DealModifiedDamageToEntity(ItemStack weapon, LivingEntity damager, LivingEntity target) {
		TwosideKeeper.DealDamageToMob(weapon, damager, target);
	}

	//Message COMMANDS.
	public static void playMessageNotification(Player sender) {
		TwosideKeeper.playMessageNotification(sender);
	}
	
	//Spleef COMMANDS.
	public static boolean isPlayingSpleef(Player p) {
		return SpleefManager.playerIsPlayingSpleef(p);
	}
	
	//Localization COMMANDS.
	public static String getLocalizedItemName(ItemStack i) {
		return GenericFunctions.UserFriendlyMaterialName(i);
	}
	public static String getLocalizedItemName(Material i) {
		return GenericFunctions.UserFriendlyMaterialName(i);
	}
	public static String getLocalizedItemName(Material i, byte data) {
		return GenericFunctions.UserFriendlyMaterialName(i,data);
	}
}
