package sig.plugin.TwosideKeeper;

import java.util.Collection;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import sig.plugin.TwosideKeeper.HelperStructures.ArtifactAbility;
import sig.plugin.TwosideKeeper.HelperStructures.ArtifactItem;
import sig.plugin.TwosideKeeper.HelperStructures.CubeType;
import sig.plugin.TwosideKeeper.HelperStructures.ItemSet;
import sig.plugin.TwosideKeeper.HelperStructures.LivingEntityDifficulty;
import sig.plugin.TwosideKeeper.HelperStructures.Loot;
import sig.plugin.TwosideKeeper.HelperStructures.MonsterDifficulty;
import sig.plugin.TwosideKeeper.HelperStructures.MonsterType;
import sig.plugin.TwosideKeeper.HelperStructures.PlayerMode;
import sig.plugin.TwosideKeeper.HelperStructures.ServerType;
import sig.plugin.TwosideKeeper.HelperStructures.WorldShop;
import sig.plugin.TwosideKeeper.HelperStructures.Common.ArrowQuiver;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.ItemCubeUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.ItemUtils;

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
	/**
	 * Use spawnAdjustedLivingEntity() instead.
	 */
	@Deprecated
	public static Monster spawnAdjustedMonster(MonsterType mt,Location loc) {
		return MonsterController.spawnAdjustedMonster(mt,loc);
	}
	/**
	 * Use autoAdjustLivingEntity() instead.
	 */
	@Deprecated
	public static Monster autoAdjustMonster(Monster m) {
		return MonsterController.convertMonster(m);
	}
	/**
	 * Use adjustLivingEntityDifficulty() instead.
	 */
	@Deprecated
	public static Monster adjustMonsterDifficulty(Monster m, MonsterDifficulty newdiff) {
		return MonsterController.convertMonster(m,newdiff);
	}
	/**
	 * Use getLivingEntityDifficulty() instead.
	 */
	@Deprecated
	public static MonsterDifficulty getMonsterDifficulty(Monster m) {
		return MonsterController.getMonsterDifficulty(m);
	}
	public static LivingEntity spawnAdjustedLivingEntity(EntityType ent,Location loc) {
		return MonsterController.spawnAdjustedLivingEntity(ent,loc);
	}
	public static LivingEntity autoAdjustLivingEntity(LivingEntity m) {
		return MonsterController.convertLivingEntity(m);
	}
	public static LivingEntity adjustLivingEntityDifficulty(LivingEntity m, LivingEntityDifficulty newdiff) {
		return MonsterController.convertLivingEntity(m,newdiff);
	}
	public static LivingEntityDifficulty getLivingEntityDifficulty(LivingEntity m) {
		return MonsterController.getLivingEntityDifficulty(m);
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
	public static double getArtifactAbilityValue(ArtifactAbility ability, ItemStack item) {
		return GenericFunctions.getAbilityValue(ability, item);
	}
	public static ItemStack removeAllArtifactAbilityPoints(ItemStack item) {
		return ArtifactAbility.removeAllEnchantments(item);
	}
	/**
	 * 
	 * @param item
	 * @return -1 if the item is not a valid artifact. The Maximum AP otherwise.
	 */
	public static int getArtifactMaxAP(ItemStack item) {
		return AwakenedArtifact.getMaxAP(item);
	}
	/**
	 * 
	 * @param item
	 * @return The current AP remaining on an artifact. -1 if the item is not a valid artifact.
	 */
	public static int getArtifactAP(ItemStack item) {
		return AwakenedArtifact.getAP(item);
	}
	/**
	 * @return The updated item.
	 */
	public static ItemStack setArtifactAP(ItemStack item, int newap) {
		return AwakenedArtifact.setAP(item, newap);
	}
	/**
	 * Sets the current AP amount. Equivalent of doing setArtifactAP(item,getArtifactAP(item)+ap);
	 * @param item
	 * @param newap
	 * @return The updated item.
	 */
	public static ItemStack addArtifactAP(ItemStack item, int ap) {
		return AwakenedArtifact.addAP(item, ap);
	}
	/**
	 * @return The updated item.
	 */
	public static ItemStack setArtifactMaxAP(ItemStack item, int newmaxap) {
		return AwakenedArtifact.setMaxAP(item, newmaxap);
	}
	/**
	 * Adds to the current Max AP amount. Equivalent of doing setArtifactMaxAP(item,getArtifactMaxAP(item)+ap);
	 * @param item
	 * @param newap
	 * @return The updated item.
	 */
	public static ItemStack addArtifactMaxAP(ItemStack item, int maxap) {
		return AwakenedArtifact.addMaxAP(item, maxap);
	}
	
	//Artifact Dust Commands.
	/**
	 * Returns true if the item is an Artifact Dust. False otherwise.
	 */
	public static boolean isArtifactDust(ItemStack item) {
		return ItemUtils.isArtifactDust(item);
	}
	/**
	 * Returns the amount of time in ticks remaining before the Artifact Dust will
	 * successfully convert back to an item.
	 */
	public static long getArtifactDustTimeRemaining(ItemStack item) {
		return ItemUtils.getArtifactDustTimeRemaining(item);
	}
	/**
	 * Sets the amount of time in ticks remaining before the Artifact Dust will
	 * successfully convert back to an item.
	 * <br><br>
	 * If <b>newtime</b> is set to 0 or less, the item will automatically convert
	 * back to its original artifact state.
	 * 
	 * @return The ItemStack modified.
	 */
	public static ItemStack setArtifactDustTimeRemaining(ItemStack item, long newtime) {
		return ItemUtils.setArtifactDustTimeRemaining(item, newtime);
	}
	/**
	 * Adjusts the amount of time in ticks remaining before the Artifact Dust will
	 * successfully convert back to an item.
	 * <br><br>
	 * If the value of <b>amt</b> is negative and <b>Math.abs(amt)</b> is greater than the
	 * amount of dust time currently on the item, it will automatically convert back
	 * to its original artifact state.
	 * @return The ItemStack modified.
	 */
	public static ItemStack addArtifactDustTimeRemaining(ItemStack item, long amt) {
		return ItemUtils.addArtifactDustTime(item, amt);
	}
	
	//Time Commands.
	public static long getServerTickTime() {
		return TwosideKeeper.getServerTickTime();
	}
	
	//Item Cube Commands.
	public static boolean isItemCube(ItemStack item) {
		return ItemCubeUtils.isItemCube(item);
	}
	/**
	 * Returns the CubeType of a given Item Cube ID.
	 * CubeType contains the number of slots each type is supposed to have
	 * via CubeType.getSize().
	 */
	public static CubeType getCubeType(int id) {
		return ItemCubeUtils.getCubeType(id);
	}
	public static ItemStack removeItemFromSlot(int id,int slot) {
		return ItemCubeUtils.removeItemFromSlot(id,slot);
	}
	public static void clearItems(int id) {
		ItemCubeUtils.clearItems(id);
	}
	public static List<ItemStack> getItemCubeContents(int id) {
		return ItemCubeUtils.getItemCubeContents(id);
	}
	/**
	 * Handles everything regarding inserting items into an Item Cube including saving
	 * it and updating it in all open players' InventoryViews.
	 * @return Returns items that could not fit in the Item Cube. It's your responsibility
	 * 		to properly handle what did not fit.
	 */
	public static Collection<ItemStack> insertItemsIntoItemCube(int id, ItemStack...items) {
		return ItemCubeUtils.addItems(id, items);
	}
	public static List<ItemStack> removeItems(int id,ItemStack...items) {
		return ItemCubeUtils.removeItems(id, items);
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
	public static ItemStack generateMegaPiece(Material item, boolean hardened, boolean isSetPiece) {
		return Loot.GenerateMegaPiece(item, hardened, isSetPiece);
	}
	public static ItemStack generateMegaPiece(Material item, boolean hardened, boolean isSetPiece, int basetier) {
		return Loot.GenerateMegaPiece(item, hardened, isSetPiece, basetier);
	}
	public static ItemStack generateSetPiece(Material item, ItemSet set, boolean hardened, int basetier) {
		return Loot.GenerateSetPiece(item, set, hardened, basetier);
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
		return PartyManager.getPartyMembers(p); //LEGACY CODE.
	}
	public static boolean IsInSameParty(Player p, Player p2) {
		return PartyManager.IsInSameParty(p, p2);
	}

	//Combat COMMANDS.
	/** MAIN METHOD TO DEAL ALL DAMAGE WITH. DO NOT USE OTHER METHODS UNLESS YOU HAVE A VERY SPECIFIC PURPOSE.<br><br>
	 * Attempts to apply damage to a target. This method factors in all possibilities of dodging or damage not being applied,
	 * such as dodge chance, iframes, or not being allowed to be hit by the target due to nodamageticks, etc.<br><br>
	 * 
	 * Once the invulnerable check is successful, it proceeds to calculate the damage. If a weapon is provided, the DAMAGE is
	 * ignored and instead the WEAPON is used to calculate the damage instead. If a weapon is NOT provided, the DAMAGE value will
	 * be used unless it's 0. If the damage value is 0, this attack will automatically deal 1 damage (a punch).<br><br>
	 * 
	 * Finally, this method actually applies the damage properly by calling the correct event and dealing the correct damage. If a
	 * damager is not specified, the attack directly subtracts from the entity's health with .damage(double). Otherwise, .damage(double,Entity)
	 * is used to apply a proper damage event.<br><br>
	 * 
	 * If you want to ignore the dodge chance (an "always hit" attack) or you only want to calculate damage but not deal it, or
	 * you want to deal damage with no additional buffs, debuffs, multipliers, or damage reductions, you
	 * can call the separate pieces this method is composed of: InvulnerableCheck(), CalculateDamage(), and DealDamageToEntity()
	 * @param damage
	 * @param damager
	 * @param target
	 * @param weapon
	 * @param reason
	 * @param flags Specifies additional flags which modify the behavior of applying damage.
	 * 		Valid flags are:<br>
	 * &nbsp;&nbsp;&nbsp;&nbsp;NONE - Just a human-readable version of the value 0.<br>
	 * &nbsp;&nbsp;&nbsp;&nbsp;CRITICALSTRIKE - Force a Critical Strike.<br>
	 * &nbsp;&nbsp;&nbsp;&nbsp;IGNOREDODGE - Ignores all Dodge and invulnerability checks.<br>
	 * &nbsp;&nbsp;&nbsp;&nbsp;TRUEDMG - Ignores all additional calculations/reductions, applying the damage directly.<br>
	 * <br><b>Combining flags example:</b> CRITICALSTRIKE|IGNOREDODGE (Force a critical strike AND ignore invulnerability check)
	 * @return Whether or not this attack actually was applied. Returns false if it was dodged, nodamageticks, etc.
	 */
	public static boolean applyDamage(double damage, Entity damager, LivingEntity target, ItemStack weapon, String reason, int flags) {
		return CustomDamage.ApplyDamage(damage, damager, target, weapon, reason, flags);
	}
	
	/**
	 * Returns how much damage would be dealt with this particular calculation of damage dealt. Uses same exact arguments as applyDamage.
	 * @param damage
	 * @param damager
	 * @param target
	 * @param weapon
	 * @param reason
	 * @param flags
	 * @return
	 */
	public static double CalculateDamage(double damage, Entity damager, LivingEntity target, ItemStack weapon, String reason, int flags) {
		return CustomDamage.CalculateDamage(damage, damager, target, weapon, reason, flags);
	}
	
	/**
	 * Returns the string of the last damage reason the player took. All attacks in the game except
	 * for basic attacks from monsters and projectiles (which makes this return null) have a reason.
	 * @param p
	 * @return Returns the string containing the reason, or null otherwise.
	 * Be sure to check for null! This CAN and probably WILL return null.
	 */
	public static String getLastDamageReason(Player p) {
		return CustomDamage.getLastDamageReason(p);
	}
	
	
	/**
	 * Determines if the target is invulnerable.
	 * @param damager
	 * @param target
	 * @return Returns true if the target cannot be hit. False otherwise.
	 */
	public boolean InvulnerableCheck(Entity damager, LivingEntity target) {
		return CustomDamage.InvulnerableCheck(damager, target);
	}
	/**
	 * Returns how much damage comes from the WEAPON, and no other sources.
	 * @param damager The entity dealing the actual damage. Can be null if you just want base damage from the weapon. This is used for determining if we should calculate Power for a Projectile.
	 * @param target Can be set to null if you just want general damage.
	 * @param weapon
	 * @return
	 */
	public double getBaseWeaponDamage(ItemStack weapon, Entity damager, LivingEntity target) {
		return CustomDamage.getBaseWeaponDamage(weapon, damager, target);
	}
	/**
	 * Does the actual damage application to the entity. Technically you can use this method to deal true damage
	 * as opposed to ApplyDamage(), but it ignores dodge chance and this method may not remain consistent with the
	 * damage dealing properties applied by other damage calculations. It is recommended to simply use ApplyDamage()
	 * with the TRUEDMG flag set instead.
	 * @param damage
	 * @param damager
	 * @param target
	 * @param weapon
	 * @param reason
	 * @param flags
	 */
	public void DealDamageToEntity(double damage, Entity damager, LivingEntity target, ItemStack weapon,
			String reason, int flags) {
		CustomDamage.DealDamageToEntity(damage, damager, target, weapon, reason, flags);
	}
	/**
	 * Makes the target vulnerable to the damager again by removing their last hit time.
	 * @param damager The damager that will have their no damage tick flag removed.
	 * @param target The target that will be vulnerable to the damager again.
	 */
	public static void removeNoDamageTick(LivingEntity damager, LivingEntity target) {
		GenericFunctions.removeNoDamageTick(target, damager);
	}
	public static void addIframe(int ticks, Player p) {
		CustomDamage.addIframe(ticks, p);
	}
	public static void removeIframe(Player p) {
		CustomDamage.removeIframe(p);
	}
	public static boolean isInIframe(Player p) {
		return CustomDamage.isInIframe(p);
	}

	//Message COMMANDS.
	public static void playMessageNotification(Player sender) {
		TwosideKeeper.playMessageNotification(sender);
	}
	public static void notifyBrokenItemToPlayer(ItemStack item, Player p) {
		TwosideKeeper.breakdownItem(item,p);
	}
	
	//Spleef COMMANDS.
	public static boolean isPlayingSpleef(Player p) {
		return SpleefManager.playerIsPlayingSpleef(p);
	}
	
	//Breaking COMMANDS.
	public static boolean hasPermissionToBreakSign(Sign s, Player p) {
		return GenericFunctions.hasPermissionToBreakSign(s,p);
	}
	
	//World Shop COMMANDS.
	public static boolean isWorldShop(Block b) {
		return WorldShop.shopSignExists(b);
	}
	public static boolean isWorldShopSign(Block b) {
		return WorldShop.isWorldShopSign(b);
	}
	public static boolean hasPermissionToBreakWorldShopSign(Sign s, Player p) {
		return WorldShop.hasPermissionToBreakWorldShopSign(s,p);
	}
	public static void removeWorldShopDisplayItem(Sign s) {
		WorldShop.removeShopItem(s);
	}
	public static boolean canPlaceShopSignOnBlock(Block block) {
		return WorldShop.canPlaceShopSignOnBlock(block);
	}
	public static double getWorldShopItemBasePrice(ItemStack item) {
		return WorldShop.getBaseWorldShopPrice(item);
	}
	
	//Recycling Center COMMANDS.
	public static boolean isRecyclingCenter(Block b) {
		return RecyclingCenter.isRecyclingCenter(b); 
	}
	public static void addItemToRecyclingCenter(ItemStack item) {
		TwosideKeeper.TwosideRecyclingCenter.AddItemToRecyclingCenter(item);
	}
	
	//Item Set COMMANDS.
	public static boolean isSetItem(ItemStack item) {
		return ItemSet.isSetItem(item);
	}
	/**
	 * 
	 * @param item
	 * @return The Item Set, or null if none found.
	 */
	public static ItemSet getItemSet(ItemStack item) {
		return ItemSet.GetSet(item);
	}
	public static int getItemTier(ItemStack item) {
		return ItemSet.GetTier(item);
	}
	public static void setItemTier(ItemStack item,int tier) {
		ItemSet.SetTier(item, tier);
	} 
	public static void setItemSet(ItemStack item, ItemSet set) {
		ItemSet.SetItemSet(item, set);
	} 
	public static boolean isUpgradeShard(ItemStack item) {
		return GenericFunctions.isUpgradeShard(item);
	}
	public static int getUpgradeShardTier(ItemStack item) {
		return GenericFunctions.getUpgradeShardTier(item);
	}
	public static void setUpgradeShardTier(ItemStack item, int tier) {
		GenericFunctions.setUpgradeShardTier(item, tier);
	}
	
	//Arrow Quiver COMMANDS.
	public static boolean isArrowQuiver(ItemStack i) {
		return ArrowQuiver.isValidQuiver(i);
	}
	public static List<ItemStack> getArrowQuiverContents(ItemStack i) {
		return ArrowQuiver.getContentsAPI(i);
	}
	
	//Localization COMMANDS.
	public static String getLocalizedItemName(ItemStack i) {
		return GenericFunctions.UserFriendlyMaterialName(i);
	}
	public static String getLocalizedItemName(Material i) {
		return GenericFunctions.UserFriendlyMaterialName(i);
	}
	/**
	 * Returns a localized standard name of the entity, including the shooter
	 * in parenthesis if it's a projectile shot at someone.
	 * @param ent
	 * @return
	 */
	public static String getEntityName(Entity ent) {
		return GenericFunctions.GetEntityDisplayName(ent);
	} 
	/**
	 * Returns the actual custom name of the monster without the suffix/buff bar
	 * attached to it. This also returns the basic name of an entity if it does
	 * not have a custom name already.
	 * @param ent
	 * @return
	 */
	public static String getActualCustomName(LivingEntity ent) {
		return GenericFunctions.getDisplayName(ent);
	}
	public static String getActionBarSuffix(LivingEntity ent) {
		return ActionBarBuffUpdater.getActionBarPrefix(ent);
	}
	public static String getCurrentActionBarText(Player p) {
		return GenericFunctions.getLastActionBarMessage(p);
	}
	public static void setActionBarLastImportantMessageTime(Player p, long last_important_msg_time) {
		GenericFunctions.sendLastImportantActionBarMsgTime(p, last_important_msg_time);
	}
	/**
	 * @deprecated Use the version that requires a short. Using a byte makes no sense
	 * as durability can be larger than 256.
	 */
	@Deprecated
	public static String getLocalizedItemName(Material i, byte data) {
		return GenericFunctions.UserFriendlyMaterialName(i,data);
	}
	public static String getLocalizedItemName(Material i, short data) {
		return GenericFunctions.UserFriendlyMaterialName(i,data);
	}
	public static ItemStack convertPotionEffectsToLore(ItemStack item) {
		return GenericFunctions.AddCustomPotionTag(item);
	}
	
	//Player COMMANDS.
	public static double getPlayerVelocity(Player p) {
		return GenericFunctions.GetPlayerVelocity(p);
	}
	public static PlayerMode getPlayerMode(Player p) {
		return PlayerMode.getPlayerMode(p);
	}
	/**Returns the amount of cooldown reduction the player has.
		 0.0 meaning cooldowns are not reduced at all. 1.0 meaning cooldowns should be non-existent.
	 * @param p
	 * @return
	 */
	public static double getCooldownReduction(Player p) {
		return CustomDamage.calculateCooldownReduction(p);
	}
}
