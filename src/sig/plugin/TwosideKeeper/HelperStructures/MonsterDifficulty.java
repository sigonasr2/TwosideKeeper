package sig.plugin.TwosideKeeper.HelperStructures;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import aPlugin.API.Chests;
import net.md_5.bungee.api.ChatColor;
import sig.plugin.TwosideKeeper.CustomDamage;
import sig.plugin.TwosideKeeper.MonsterController;
import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;

/**
 * Use LivingEntityDifficulty instead.
 */
@Deprecated
public enum MonsterDifficulty {
	
	NORMAL(LivingEntityDifficulty.NORMAL),
	DANGEROUS(LivingEntityDifficulty.DANGEROUS),
	DEADLY(LivingEntityDifficulty.DEADLY),
	HELLFIRE(LivingEntityDifficulty.HELLFIRE),
	ELITE(LivingEntityDifficulty.ELITE),
	END(LivingEntityDifficulty.END);
	
	LivingEntityDifficulty diff;
	
	MonsterDifficulty(LivingEntityDifficulty diff) {
		this.diff=diff;
	}

	/*private ItemStack Artifact() {
		sig.plugin.TwosideKeeper.Artifact.createArtifactItem(ArtifactItem.ARTIFACT_ESSENCE,3);
		return null;
	}*/
	
	public LivingEntityDifficulty getLivingEntityDifficultyEquivalent() {
		return this.diff;
	}

	public List<ItemStack> RandomizeDrops(double dropmult, boolean isBoss, boolean isRanger, Entity damager, Monster m) {
		return RandomizeDrops(dropmult,isBoss,false,isRanger,damager,m);
	}
	
	public List<ItemStack> RandomizeDrops(double dropmult, boolean isBoss, boolean isElite, boolean isRanger, Entity damager, Monster m) {
		
		MonsterDifficulty diff = MonsterController.getMonsterDifficulty(m);
		
		TwosideKeeper.log(ChatColor.AQUA+"->Entering RandomizeDrops()", 5); 
		List<ItemStack> droplist = new ArrayList<ItemStack>();
		dropmult += 1; //Base dropmult is 1.0.
		if (Math.random() < dropmult % 1)
		{
			dropmult++; 
		}
		
		//Basically for each additional dropmult integer value, the
		//amount of rolls increases. (A dropmult of 1.0 is required for
		//an additional roll.)
		Player p = (Player)CustomDamage.getDamagerEntity(damager);
		for (int i=0;i<dropmult;i++) {
			TwosideKeeper.Loot_Logger.AddLootRoll();
			TwosideKeeper.log("Attempting a roll...", TwosideKeeper.LOOT_DEBUG); 
			ItemStack goodie = null;
			if (Math.random()<=0.1 || isBoss) {
				TwosideKeeper.log("Inside!", 5);
				switch (diff) {
					case DANGEROUS:{
						goodie=aPlugin.API.Chests.LOOT_DANGEROUS.getSingleDrop(p);
						KeepRollingForBosses(isBoss, droplist, goodie, aPlugin.API.Chests.LOOT_DANGEROUS, p);
					}break;
					case DEADLY:{
						goodie=aPlugin.API.Chests.LOOT_DEADLY.getSingleDrop(p);
						KeepRollingForBosses(isBoss, droplist, goodie, aPlugin.API.Chests.LOOT_DEADLY, p);
					}break;
					case HELLFIRE:{
						goodie=aPlugin.API.Chests.LOOT_HELLFIRE.getSingleDrop(p);
						KeepRollingForBosses(isBoss, droplist, goodie, aPlugin.API.Chests.LOOT_HELLFIRE, p);
					}break;
					case END:{
						goodie=aPlugin.API.Chests.LOOT_CUSTOM.getSingleDrop(p);
						KeepRollingForBosses(isBoss, droplist, goodie, aPlugin.API.Chests.LOOT_CUSTOM, p);
					}break;
					case ELITE:{
						
					}break;
					default:{
						if (Math.random()<=0.4) {
							goodie=aPlugin.API.Chests.LOOT_NORMAL.getSingleDrop(p);
						}
						KeepRollingForBosses(isBoss, droplist, goodie, aPlugin.API.Chests.LOOT_NORMAL, p);
					}
				}
				TwosideKeeper.Loot_Logger.AddCommonLoot();
				ModifyAndAddDropToList(droplist,goodie,damager);
			}
		}
		TwosideKeeper.log("New Droplist: "+droplist.toString(), 5); 
		return droplist;
	}

	public void KeepRollingForBosses(boolean isBoss, List<ItemStack> droplist, ItemStack goodie, Chests chest, Player damager) {
		int roll=0;
		while (isBoss && !isValidSetItem(goodie) && roll<50) {
			goodie=chest.getSingleDrop(damager);
			ModifyAndAddDropToList(droplist,goodie,damager);
			roll++;
			TwosideKeeper.Loot_Logger.AddCommonLoot();
		}
	}

	private void ModifyAndAddDropToList(List<ItemStack> droplist, ItemStack goodie, Entity damager) {
		/*LivingEntity shooter = CustomDamage.getDamagerEntity(damager);
		if (shooter instanceof Player) {
			Player p = (Player)shooter;
			if (isValidSetItem(goodie)) {
				if (Math.random()<0.8) {
					//Convert it to a set piece.
					PlayerMode pm = PlayerMode.getPlayerMode(p);
					if (AllowedToConvert(pm,goodie)) {
						ItemSet set = PickAnItemSet(pm,goodie);
						goodie = ConvertSetPieceIfNecessary(goodie, set);
						goodie = Loot.GenerateSetPiece(goodie.getType(), set, (Math.random()<0.1)?true:false, 0, false);
					}
				} else {
					//Convert it to a mega piece.
					PlayerMode pm = PlayerMode.getPlayerMode(p);
					if (AllowedToConvert(pm,goodie)) {
						goodie = Loot.GenerateMegaPiece(goodie.getType(), (Math.random()<0.1)?true:false);
					}
				}
			}
		}
		TwosideKeeper.log("Adding item "+goodie, 2);*/ //LEGACY CODE.
		droplist.add(goodie);
	}

	@SuppressWarnings("unused")
	private boolean AllowedToConvert(PlayerMode pm, ItemStack goodie) {
		if (goodie.getType()==Material.SKULL_ITEM && pm!=PlayerMode.NORMAL && pm!=PlayerMode.SLAYER) {
			goodie.setDurability((short)3);
			SkullMeta sm = (SkullMeta)goodie.getItemMeta();
			sm.setOwner(Bukkit.getOfflinePlayers()[(int)(Math.random()*Bukkit.getOfflinePlayers().length)].getName());
			goodie.setItemMeta(sm);
			return false;
		}
		return true;
	}

	public static ItemStack ConvertSetPieceIfNecessary(ItemStack goodie, ItemSet set) {
		if ((set==ItemSet.JAMDAK ||
				set==ItemSet.ALIKAHN ||
				set==ItemSet.DARNYS ||
				set==ItemSet.LORASAADI) &&
				!goodie.getType().name().contains("LEATHER") &&
				GenericFunctions.isArmor(goodie)) {
			goodie.setType(Material.valueOf("LEATHER_"+goodie.getType().name().split("_")[1]));
		} else 
		if (goodie.getType().name().contains("LEATHER") &&
				!(set==ItemSet.JAMDAK ||
				set==ItemSet.ALIKAHN ||
				set==ItemSet.DARNYS ||
				set==ItemSet.LORASAADI) &&
				GenericFunctions.isArmor(goodie)) {
			goodie.setType(Material.valueOf("IRON_"+goodie.getType().name().split("_")[1]));
		} else 
		if (goodie.getType()!=Material.SKULL_ITEM &&
				(set==ItemSet.MOONSHADOW ||
				set==ItemSet.GLADOMAIN ||
				set==ItemSet.WOLFSBANE ||
				set==ItemSet.ALUSTINE)) {
			goodie.setType(Material.SKULL_ITEM);
		}
		return goodie;
	}

	private boolean isValidSetItem(ItemStack goodie) {
		if (goodie!=null) {
			return TwosideKeeper.validsetitems.contains(goodie.getType());
		} else {
			return false;
		}
	}

	public static ItemSet PickAnItemSet(PlayerMode pm, MonsterDifficulty md) {
		ItemSet set;
		switch (pm) {
			case STRIKER:{
				set = ItemSet.PANROS;
			}break;
			case DEFENDER:{
				set = ItemSet.SONGSTEEL;
			}break;
			case BARBARIAN:{
				set = ItemSet.DAWNTRACKER;
			}break;
			case RANGER:{
				final int NUMBER_OF_MODES=4;
				int totalweight=50*NUMBER_OF_MODES; //50 for each mode.
				int selectweight=(int)(Math.random()*totalweight); 
				if (selectweight<50) {
					set = ItemSet.JAMDAK;
				} else
				if (selectweight<100) {
					set = ItemSet.ALIKAHN;
				} else
				if (selectweight<150) {
					set = ItemSet.DARNYS;
				} else
				{
					set = ItemSet.LORASAADI;
				}
			}break;
			case SLAYER:{
				final int NUMBER_OF_MODES=3;
				int totalweight=50*NUMBER_OF_MODES; //50 for each mode.
				int selectweight=(int)(Math.random()*totalweight); 
				if (selectweight<10) {
					set = ItemSet.LORASYS;
				} else
				switch (md) {
					case DANGEROUS:{
						set = ItemSet.ALUSTINE;
					}
					case DEADLY:{
						set = ItemSet.MOONSHADOW;
					}
					case HELLFIRE:
					case END:{
						set = ItemSet.GLADOMAIN;
					}
					default:{
						set = ItemSet.WOLFSBANE;
					}
				}
			}break;
			default:{
				set = PickRandomSet(md);
			}
		}
		return set;
	}

	public static ItemSet PickRandomSet(MonsterDifficulty md) {
		final int NUMBER_OF_MODES=5;
		int totalweight=50*NUMBER_OF_MODES; //50 for each mode.
		int selectweight=(int)(Math.random()*totalweight); 
		if (selectweight<50) {
			return ItemSet.PANROS;
		} else
		if (selectweight<100) {
			return ItemSet.SONGSTEEL;
		} else
		if (selectweight<150) {
			return ItemSet.DAWNTRACKER;
		} else
		if (selectweight<200) {
			//12.5 per set type.
			if (selectweight<162.5) {
				return ItemSet.JAMDAK;
			} else
			if (selectweight<175) {
				return ItemSet.ALIKAHN;
			} else
			if (selectweight<187.5) {
				return ItemSet.DARNYS;
			} else
			if (selectweight<200) {
				return ItemSet.LORASAADI;
			}
		} else
		if (selectweight<250) {
			if (selectweight<205) {
				return ItemSet.LORASYS;
			} else
			switch (md) {
				case DANGEROUS:{
					return ItemSet.ALUSTINE;
				}
				case DEADLY:{
					return ItemSet.MOONSHADOW;
				}
				case HELLFIRE:
				case END:{
					return ItemSet.GLADOMAIN;
				}
				default:{
					return ItemSet.WOLFSBANE;
				}
			}
		} 
		return ItemSet.PANROS;
	}
}
