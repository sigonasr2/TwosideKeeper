package sig.plugin.TwosideKeeper.HelperStructures;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.md_5.bungee.api.ChatColor;
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
			);

	LootStructure[] loot_regular;
	LootStructure[] loot_rare;
	LootStructure[] loot_legendary;
	
	private MonsterDifficulty(LootStructure[] loot_regular, LootStructure[] loot_rare, LootStructure[] loot_legendary) {
		this.loot_regular=loot_regular;
		this.loot_rare=loot_rare;
		this.loot_legendary=loot_legendary;
	}

	private ItemStack Artifact() {
		sig.plugin.TwosideKeeper.Artifact.createArtifactItem(ArtifactItem.ARTIFACT_ESSENCE,3);
		return null;
	}

	public List<ItemStack> RandomizeDrops(double dropmult, boolean isBoss, boolean isRanger, Entity damager, Monster m) {
		return RandomizeDrops(dropmult,isBoss,false,isRanger,damager,m);
	}
	
	public List<ItemStack> RandomizeDrops(double dropmult, boolean isBoss, boolean isElite, boolean isRanger, Entity damager, Monster m) {
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
			//First do a common roll.
			if (Math.random()<TwosideKeeper.COMMON_DROP_RATE &&
					this.loot_regular.length>0) {
				TwosideKeeper.log(">Attempting Common roll.", 4);
				//This is a common roll.
				ItemStack gen_loot = DistributeRandomLoot(this.loot_regular, isRanger, damager, m);
				TwosideKeeper.log("Adding "+gen_loot.toString()+" to loot table.", 4);
				droplist.add(gen_loot);
				TwosideKeeper.Loot_Logger.AddCommonLoot();
			}
			//Rare Loot roll.
			if (Math.random()<TwosideKeeper.RARE_DROP_RATE &&
					this.loot_rare.length>0) {
				TwosideKeeper.log(">Attempting Rare roll.", 3);
				//This is a common roll.
				ItemStack gen_loot = DistributeRandomLoot(this.loot_rare, isRanger, damager, m);
				TwosideKeeper.log("Adding "+gen_loot.toString()+" to loot table.", 4);
				droplist.add(gen_loot);
				double randomness = Math.random();
				TwosideKeeper.log(ChatColor.DARK_GREEN+"  Randomness is "+randomness, 4);
				if (randomness<=0.2) {
					TwosideKeeper.log(ChatColor.DARK_GREEN+"  Spawn an essence!", 4);
					switch (this) {
						case DANGEROUS:
							droplist.add(sig.plugin.TwosideKeeper.Artifact.createArtifactItem(ArtifactItem.ANCIENT_ESSENCE));
							break;
						case DEADLY:
							droplist.add(sig.plugin.TwosideKeeper.Artifact.createArtifactItem(ArtifactItem.LOST_ESSENCE));
							break;
						case HELLFIRE:
							droplist.add(sig.plugin.TwosideKeeper.Artifact.createArtifactItem(ArtifactItem.DIVINE_ESSENCE));
							break;
						case ELITE:
							droplist.add(sig.plugin.TwosideKeeper.Artifact.createArtifactItem(ArtifactItem.DIVINE_ESSENCE));
							break;
						case NORMAL:
								droplist.add(sig.plugin.TwosideKeeper.Artifact.createArtifactItem(ArtifactItem.ARTIFACT_ESSENCE));
							break;
						default: {
							TwosideKeeper.log("Invalid Monster Type!", 1);
						}
					}
				}
				TwosideKeeper.Loot_Logger.AddRareLoot();
			}
			//Legendary Loot roll. 
			if (Math.random()<TwosideKeeper.LEGENDARY_DROP_RATE &&
					this.loot_legendary.length>0) {
				TwosideKeeper.log(">Attempting Legendary roll.", 3);
				//This is a common roll.
				ItemStack gen_loot = DistributeRandomLoot(this.loot_legendary, isRanger, damager, m);
				TwosideKeeper.log("Adding "+gen_loot.toString()+" to loot table.", 4);
				droplist.add(gen_loot);
				double randomness = Math.random();
				if (this.equals(MonsterDifficulty.HELLFIRE) || this.equals(MonsterDifficulty.ELITE)) {
					if (randomness<=0.5) {
						ItemStack hunters_compass = TwosideKeeper.HUNTERS_COMPASS.getItemStack();
						droplist.add(hunters_compass);
					}
				}
				randomness = Math.random();
				TwosideKeeper.log(ChatColor.DARK_GREEN+"  Randomness is "+randomness, 4);
				if (randomness<=0.2) {
					TwosideKeeper.log(ChatColor.DARK_GREEN+"  Spawn a Core!", 4);
					switch (this) {
						case DANGEROUS:
								droplist.add(sig.plugin.TwosideKeeper.Artifact.createArtifactItem(ArtifactItem.ANCIENT_CORE));
							break;
						case DEADLY:
								droplist.add(sig.plugin.TwosideKeeper.Artifact.createArtifactItem(ArtifactItem.LOST_CORE));
							break;
						case HELLFIRE:
								droplist.add(sig.plugin.TwosideKeeper.Artifact.createArtifactItem(ArtifactItem.DIVINE_CORE));
							break;
						case ELITE:
							droplist.add(sig.plugin.TwosideKeeper.Artifact.createArtifactItem(ArtifactItem.DIVINE_CORE));
						break;
						case NORMAL:
								droplist.add(sig.plugin.TwosideKeeper.Artifact.createArtifactItem(ArtifactItem.ARTIFACT_CORE));
							break;
						default: {
							TwosideKeeper.log("Invalid Monster Type!", 1);
						}
					}
				}
				randomness = Math.random();
				TwosideKeeper.log(ChatColor.DARK_GREEN+"  Randomness is "+randomness, 4);
				if (randomness<=0.6) {
					switch (this) { 
						case NORMAL:
								TwosideKeeper.log(ChatColor.DARK_GREEN+"  Spawn a Mysterious Essence!", 4);
								droplist.add(sig.plugin.TwosideKeeper.Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE));
							break;
					}
				}
				TwosideKeeper.Loot_Logger.AddLegendaryLoot();
			}
			if (isBoss) { //50% of the time, we drop something great.
				if (Math.random()<=0.5 && this.loot_legendary.length>0) {
					TwosideKeeper.log(">Boss Legendary roll.", 1);
					ItemStack gen_loot = DistributeRandomLoot(this.loot_legendary, isRanger, damager, m);
					TwosideKeeper.log("Adding "+gen_loot.toString()+" to loot table.", 4);
					droplist.add(gen_loot);
					TwosideKeeper.Loot_Logger.AddLegendaryLoot();
				}
				else
				if (this.loot_rare.length>0) { //Consolation Prize.
					TwosideKeeper.log(">Boss Rare roll.", 1);
					ItemStack gen_loot = DistributeRandomLoot(this.loot_rare, isRanger, damager, m);
					TwosideKeeper.log("Adding "+gen_loot.toString()+" to loot table.", 4);
					droplist.add(gen_loot);
					TwosideKeeper.Loot_Logger.AddRareLoot();
				}
			}
		}
		TwosideKeeper.log("  Drop List "+"["+(droplist.size())+"]: "+ChatColor.LIGHT_PURPLE+ChatColor.stripColor(droplist.toString()),5);
		return droplist;
	}
	
	private ItemStack DistributeRandomLoot(LootStructure[] lootlist, boolean isRanger, Entity damager, Monster m) {
		//Choose an item randomly from the loot list.
		if (lootlist.length>0) {
			//Choose an element.
			LootStructure ls = lootlist[(int)((Math.random())*lootlist.length)];
			if (ls.GetMaterial()==Material.PRISMARINE_SHARD) {
				ItemStack item = TwosideKeeper.UPGRADE_SHARD.getItemStack();
				return item;
			}
			if (ls.GetMaterial()==Material.POTION) {
				//Create a Strengthing Vial.
				if (Math.random()<=0.85) {
					ItemStack item = TwosideKeeper.STRENGTHENING_VIAL.getItemStack();
					return item;
				} else if (Math.random()<=0.85) {
					ItemStack item = TwosideKeeper.LIFE_VIAL.getItemStack();
					return item;
				} else {
					ItemStack item = TwosideKeeper.HARDENING_VIAL.getItemStack();
					return item;
				}
			}
			if (ls.GetMinSetLevel()>0) {
				//Make a set piece.
				return Loot.GenerateMegaPiece(ls.GetMaterial(), ls.GetHardened(),true,ls.GetMinSetLevel());
			}
			if (GenericFunctions.isEquip(new ItemStack(ls.GetMaterial()))) {
				//Turn it into a Mega Piece.
				if (GenericFunctions.isTool(new ItemStack(ls.GetMaterial()))) {
					if (Math.random()<=0.1) {
						if (Math.random()<=0.8) {
							return Loot.GenerateMegaPiece(ls.GetMaterial(), ls.GetHardened(), true, 0, damager, m);
						} else {
							return Loot.GenerateMegaPiece(ls.GetMaterial(), ls.GetHardened(),false);
						}
					} else {
						return new ItemStack(ls.GetMaterial(),1,(short)(Math.random()*ls.GetMaterial().getMaxDurability()));
					}
				} else {
					/*if (ls.GetMaterial().toString().contains("LEATHER")) { //LEGACY CODE.
						if (isRanger) {
							return Loot.GenerateRangerPiece(ls.GetMaterial(), ls.GetHardened(), ls.GetAmount());
						} else {
							//Re-roll if a ranger did not kill, as we cannot reward ranger armor to non-rangers.
							return DistributeRandomLoot(lootlist,isRanger);
						}
					} else {*/
						if (Math.random()<=0.8) {
							return Loot.GenerateMegaPiece(ls.GetMaterial(), ls.GetHardened(), true, 0, damager, m);
						} else {
							return Loot.GenerateMegaPiece(ls.GetMaterial(), ls.GetHardened(),false);
						}
					//} //LEGACY CODE.
				}
			} else {
				//Turn it into a normal item.
				return new ItemStack(ls.GetMaterial(),ls.GetAmount());
			}
		} else { //Something bad happened if we got here...
			return new ItemStack(Material.AIR);
		}
	}
}
