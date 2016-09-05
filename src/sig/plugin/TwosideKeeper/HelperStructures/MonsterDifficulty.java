package sig.plugin.TwosideKeeper.HelperStructures;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
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

public enum MonsterDifficulty {
	
	NORMAL( //Contains info about loot for everything.
		new LootStructure[]{ //Regular Loot
				
		},
		new LootStructure[]{ //Rare Loot
			new LootStructure(Material.STONE_SWORD, false),
			new LootStructure(Material.IRON_INGOT),
			new LootStructure(Material.DIAMOND),
			new LootStructure(Material.GOLD_NUGGET),
			new LootStructure(Material.LEATHER_HELMET,1),
			new LootStructure(Material.LEATHER_CHESTPLATE,1),
			new LootStructure(Material.LEATHER_LEGGINGS,1),
			new LootStructure(Material.LEATHER_BOOTS,1),
		},
		new LootStructure[]{ //Legendary Loot
			new LootStructure(Material.STONE_SWORD, true),
			new LootStructure(Material.IRON_INGOT,(int)((Math.random()*3)+1)),
			new LootStructure(Material.DIAMOND,(int)((Math.random()*3)+1)),
			new LootStructure(Material.GOLD_NUGGET,(int)((Math.random()*3)+1)),
			new LootStructure(Material.ENDER_PEARL,(int)((Math.random()*3)+1)),
			new LootStructure(Material.ENDER_CHEST),
		}
	),
	DANGEROUS(
			new LootStructure[]{ //Regular Loot
				new LootStructure(Material.IRON_INGOT,false),
			},
			new LootStructure[]{ //Rare Loot
				new LootStructure(Material.IRON_BLOCK,false),
				new LootStructure(Material.IRON_SWORD, false),
				new LootStructure(Material.IRON_CHESTPLATE, false),
				new LootStructure(Material.IRON_LEGGINGS, false),
				new LootStructure(Material.IRON_BOOTS, false),
				new LootStructure(Material.IRON_HELMET, false),
				new LootStructure(Material.STONE_AXE, false),
				new LootStructure(Material.STONE_PICKAXE, false),
				new LootStructure(Material.STONE_HOE, false),
				new LootStructure(Material.STONE_SPADE, false),
				new LootStructure(Material.FISHING_ROD, false),
			},
			new LootStructure[]{ //Legendary Loot
				new LootStructure(Material.IRON_SWORD, true),
				new LootStructure(Material.IRON_CHESTPLATE, true),
				new LootStructure(Material.IRON_LEGGINGS, true),
				new LootStructure(Material.IRON_BOOTS, true),
				new LootStructure(Material.IRON_HELMET, true),
				new LootStructure(Material.STONE_AXE, true),
				new LootStructure(Material.STONE_PICKAXE, true),
				new LootStructure(Material.STONE_HOE, true),
				new LootStructure(Material.STONE_SPADE, true),
				new LootStructure(Material.FISHING_ROD, true),
				new LootStructure(Material.LEATHER_HELMET),
				new LootStructure(Material.LEATHER_CHESTPLATE),
				new LootStructure(Material.LEATHER_LEGGINGS),
				new LootStructure(Material.LEATHER_BOOTS),
			}
		),
	DEADLY(
			new LootStructure[]{ //Regular Loot
				new LootStructure(Material.IRON_INGOT,(int)((Math.random()*2)+1)),
				new LootStructure(Material.DIAMOND),
				new LootStructure(Material.GOLD_NUGGET,(int)((Math.random()*3)+1)),
			},
			new LootStructure[]{ //Rare Loot
				new LootStructure(Material.IRON_BLOCK,(int)((Math.random()*2)+1)),
				new LootStructure(Material.DIAMOND_BLOCK),
				new LootStructure(Material.GOLD_INGOT,(int)((Math.random()*3)+1)),
				new LootStructure(Material.DIAMOND_SWORD, false),
				new LootStructure(Material.IRON_AXE, false),
				new LootStructure(Material.IRON_PICKAXE, false),
				new LootStructure(Material.IRON_HOE, false),
				new LootStructure(Material.IRON_SPADE, false),
				new LootStructure(Material.DIAMOND_CHESTPLATE, false),
				new LootStructure(Material.DIAMOND_LEGGINGS, false),
				new LootStructure(Material.DIAMOND_BOOTS, false),
				new LootStructure(Material.DIAMOND_HELMET, false),
				new LootStructure(Material.FISHING_ROD, false),
				new LootStructure(Material.BOW, false),
				new LootStructure(Material.LEATHER_HELMET,2),
				new LootStructure(Material.LEATHER_CHESTPLATE,2),
				new LootStructure(Material.LEATHER_LEGGINGS,2),
				new LootStructure(Material.LEATHER_BOOTS,2),
			},
			new LootStructure[]{ //Legendary Loot
				new LootStructure(Material.DIAMOND_SWORD, true),
				new LootStructure(Material.IRON_AXE, true),
				new LootStructure(Material.IRON_PICKAXE, true),
				new LootStructure(Material.IRON_HOE, true),
				new LootStructure(Material.IRON_SPADE, true),
				new LootStructure(Material.FISHING_ROD, true),
				new LootStructure(Material.DIAMOND_CHESTPLATE, true),
				new LootStructure(Material.DIAMOND_LEGGINGS, true),
				new LootStructure(Material.DIAMOND_BOOTS, true),
				new LootStructure(Material.DIAMOND_HELMET, true),
				new LootStructure(Material.FISHING_ROD, true),
				new LootStructure(Material.LEATHER_HELMET),
				new LootStructure(Material.LEATHER_CHESTPLATE),
				new LootStructure(Material.LEATHER_LEGGINGS),
				new LootStructure(Material.LEATHER_BOOTS),
			}
		),
	HELLFIRE(
			new LootStructure[]{ //Regular Loot
				new LootStructure(Material.EMERALD,(int)((Math.random()*3)+1)),
				new LootStructure(Material.DIAMOND,(int)((Math.random()*3)+1)),
				new LootStructure(Material.GOLD_INGOT,(int)((Math.random()*3)+1)),
			},
			new LootStructure[]{ //Rare Loot
				new LootStructure(Material.EMERALD_BLOCK),
				new LootStructure(Material.DIAMOND_BLOCK,(int)((Math.random()*2)+1)),
				new LootStructure(Material.GOLD_BLOCK),
				new LootStructure(Material.DIAMOND_SWORD, false),
				new LootStructure(Material.GOLD_SWORD, false),
				new LootStructure(Material.DIAMOND_AXE, false),
				new LootStructure(Material.DIAMOND_PICKAXE, false),
				new LootStructure(Material.DIAMOND_HOE, false),
				new LootStructure(Material.DIAMOND_SPADE, false),
				new LootStructure(Material.DIAMOND_CHESTPLATE, false),
				new LootStructure(Material.DIAMOND_LEGGINGS, false),
				new LootStructure(Material.DIAMOND_BOOTS, false),
				new LootStructure(Material.DIAMOND_HELMET, false),
				new LootStructure(Material.GOLD_CHESTPLATE, false),
				new LootStructure(Material.GOLD_LEGGINGS, false),
				new LootStructure(Material.GOLD_BOOTS, false),
				new LootStructure(Material.GOLD_HELMET, false),
				new LootStructure(Material.BOW, false),
				new LootStructure(Material.FISHING_ROD, false),
				new LootStructure(Material.LEATHER_HELMET,3),
				new LootStructure(Material.LEATHER_CHESTPLATE,3),
				new LootStructure(Material.LEATHER_LEGGINGS,3),
				new LootStructure(Material.LEATHER_BOOTS,3),
			},
			new LootStructure[]{ //Legendary Loot
				new LootStructure(Material.GOLD_SWORD, true),
				new LootStructure(Material.DIAMOND_SWORD, true),
				new LootStructure(Material.DIAMOND_AXE, true),
				new LootStructure(Material.DIAMOND_PICKAXE, true),
				new LootStructure(Material.DIAMOND_HOE, true),
				new LootStructure(Material.DIAMOND_SPADE, true),
				new LootStructure(Material.DIAMOND_CHESTPLATE, true),
				new LootStructure(Material.DIAMOND_LEGGINGS, true),
				new LootStructure(Material.DIAMOND_BOOTS, true),
				new LootStructure(Material.DIAMOND_HELMET, true),
				new LootStructure(Material.GOLD_CHESTPLATE, true),
				new LootStructure(Material.GOLD_LEGGINGS, true),
				new LootStructure(Material.GOLD_BOOTS, true),
				new LootStructure(Material.GOLD_HELMET, true),
				new LootStructure(Material.BOW, true),
				new LootStructure(Material.FISHING_ROD, true),
				new LootStructure(Material.LEATHER_HELMET),
				new LootStructure(Material.LEATHER_CHESTPLATE),
				new LootStructure(Material.LEATHER_LEGGINGS),
				new LootStructure(Material.LEATHER_BOOTS),
			}
		),
	ELITE(
			new LootStructure[]{ //Common Loot
					new LootStructure(Material.EMERALD),
					new LootStructure(Material.DIAMOND),
					new LootStructure(Material.GOLD_INGOT),
					new LootStructure(Material.REDSTONE),
					new LootStructure(Material.IRON_INGOT),
					new LootStructure(Material.LEATHER_HELMET,3),
					new LootStructure(Material.LEATHER_CHESTPLATE,3),
					new LootStructure(Material.LEATHER_LEGGINGS,3),
					new LootStructure(Material.LEATHER_BOOTS,3),
				},
			new LootStructure[]{ //Rare Loot
					new LootStructure(Material.EMERALD_BLOCK),
					new LootStructure(Material.DIAMOND_BLOCK),
					new LootStructure(Material.GOLD_BLOCK),
					new LootStructure(Material.REDSTONE_BLOCK),
					new LootStructure(Material.IRON_BLOCK),
					new LootStructure(Material.LAPIS_BLOCK),
					new LootStructure(Material.LEATHER_HELMET),
					new LootStructure(Material.LEATHER_CHESTPLATE),
					new LootStructure(Material.LEATHER_LEGGINGS),
					new LootStructure(Material.LEATHER_BOOTS),
				},
			new LootStructure[]{ //Legendary Loot
					new LootStructure(Material.END_CRYSTAL),
				}
			),
	END(
			new LootStructure[]{ //Common Loot
					new LootStructure(Material.EMERALD),
				},
			new LootStructure[]{ //Rare Loot
					new LootStructure(Material.EMERALD_BLOCK),
				},
			new LootStructure[]{ //Legendary Loot
					new LootStructure(Material.END_CRYSTAL),
				}
		);

	LootStructure[] loot_regular;
	LootStructure[] loot_rare;
	LootStructure[] loot_legendary;
	
	private MonsterDifficulty(LootStructure[] loot_regular, LootStructure[] loot_rare, LootStructure[] loot_legendary) {
		this.loot_regular=loot_regular;
		this.loot_rare=loot_rare;
		this.loot_legendary=loot_legendary;
	}

	/*private ItemStack Artifact() {
		sig.plugin.TwosideKeeper.Artifact.createArtifactItem(ArtifactItem.ARTIFACT_ESSENCE,3);
		return null;
	}*/

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
		for (int i=0;i<dropmult;i++) {
			TwosideKeeper.Loot_Logger.AddLootRoll();
			TwosideKeeper.log("Attempting a roll...", 2); 
			ItemStack goodie = null;
			if (Math.random()<=0.1 || isBoss) {
				TwosideKeeper.log("Inside!", 5);
				switch (diff) {
					case DANGEROUS:{
						goodie=aPlugin.API.Chests.LOOT_DANGEROUS.getSingleDrop();
						KeepRollingForBosses(isBoss, droplist, goodie, aPlugin.API.Chests.LOOT_DANGEROUS, damager);
					}break;
					case DEADLY:{
						goodie=aPlugin.API.Chests.LOOT_DEADLY.getSingleDrop();
						KeepRollingForBosses(isBoss, droplist, goodie, aPlugin.API.Chests.LOOT_DEADLY, damager);
					}break;
					case HELLFIRE:{
						goodie=aPlugin.API.Chests.LOOT_HELLFIRE.getSingleDrop();
						KeepRollingForBosses(isBoss, droplist, goodie, aPlugin.API.Chests.LOOT_HELLFIRE, damager);
					}break;
					case END:{
						goodie=aPlugin.API.Chests.LOOT_CUSTOM.getSingleDrop();
						KeepRollingForBosses(isBoss, droplist, goodie, aPlugin.API.Chests.LOOT_CUSTOM, damager);
					}break;
					case ELITE:{
						
					}break;
					default:{
						goodie=aPlugin.API.Chests.LOOT_NORMAL.getSingleDrop();
						KeepRollingForBosses(isBoss, droplist, goodie, aPlugin.API.Chests.LOOT_NORMAL, damager);
					}
				}
				TwosideKeeper.Loot_Logger.AddCommonLoot();
				ModifyAndAddDropToList(droplist,goodie,damager);
			}
		}
		TwosideKeeper.log("New Droplist: "+droplist.toString(), 5); 
		return droplist;
	}

	public void KeepRollingForBosses(boolean isBoss, List<ItemStack> droplist, ItemStack goodie, Chests chest, Entity damager) {
		int roll=0;
		while (isBoss && !GenericFunctions.isEquip(goodie) && roll<50) {
			goodie=chest.getSingleDrop();
			ModifyAndAddDropToList(droplist,goodie,damager);
			roll++;
			TwosideKeeper.Loot_Logger.AddCommonLoot();
		}
	}

	private void ModifyAndAddDropToList(List<ItemStack> droplist, ItemStack goodie, Entity damager) {
		LivingEntity shooter = CustomDamage.getDamagerEntity(damager);
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
		TwosideKeeper.log("Adding item "+goodie, 2);
		droplist.add(goodie);
	}

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

	private ItemStack ConvertSetPieceIfNecessary(ItemStack goodie, ItemSet set) {
		/*if ((set==ItemSet.JAMDAK ||
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
		}*/
		return goodie;
	}

	private boolean isValidSetItem(ItemStack goodie) {
		if (goodie!=null) {
			return TwosideKeeper.validsetitems.contains(goodie.getType());
		} else {
			return false;
		}
	}

	public ItemSet PickAnItemSet(PlayerMode pm, ItemStack item) {
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
				if (selectweight<80) {
					set = ItemSet.MOONSHADOW;
				} else
				{
					set = ItemSet.GLADOMAIN;
				}
			}break;
			default:{
				set = PickRandomSet();
			}
		}
		return set;
	}

	private ItemSet PickRandomSet() {
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
			if (selectweight<223) {
				return ItemSet.GLADOMAIN;
			} else {
				return ItemSet.MOONSHADOW;
			}
		} 
		return ItemSet.PANROS;
	}
}
