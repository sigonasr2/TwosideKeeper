package sig.plugin.TwosideKeeper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Monster;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.entity.Slime;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import sig.plugin.TwosideKeeper.HelperStructures.ItemRarity;
import sig.plugin.TwosideKeeper.HelperStructures.LivingEntityDifficulty;
import sig.plugin.TwosideKeeper.HelperStructures.Loot;
import sig.plugin.TwosideKeeper.HelperStructures.MonsterDifficulty;
import sig.plugin.TwosideKeeper.HelperStructures.MonsterType;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;

public class MonsterController {
	/**
	 * @return Returns false if this spawn is not allowed.
	 */
	public static boolean MobHeightControl(LivingEntity ent, boolean minion) {
		return MobHeightControl(ent,minion,SpawnReason.DEFAULT);
	}
	public static boolean MobHeightControl(LivingEntity ent, boolean minion, SpawnReason reason) {
		
		if (ent instanceof Monster) {
			Monster m = (Monster)ent;
			m.setAI(true);
		}
		
		//Modify spawning algorithm.
		int ylv = ent.getLocation().getBlockY();
		if (minion) {
			if (meetsConditionsToSpawn(ent)) {
	        	TwosideKeeper.log(" Minion modifier",TwosideKeeper.SPAWN_DEBUG_LEVEL);
				ylv+=16;
				ent.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,Integer.MAX_VALUE,1));
				ent.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,Integer.MAX_VALUE,0));
				ent.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,Integer.MAX_VALUE,3));
				ent.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,Integer.MAX_VALUE,4));
				if (isZombieLeader(ent)) { //Not allowed. We do not want more leaders from Minions to be spawning.
					ent.remove();
					return false;
				}
			}
		} else
		if (!meetsConditionsToSpawn(ent) &&
				reason!=SpawnReason.SPAWNER_EGG &&
				reason!=SpawnReason.SPAWNER &&
				reason!=SpawnReason.SLIME_SPLIT &&
				reason!=SpawnReason.SILVERFISH_BLOCK &&
				reason!=SpawnReason.REINFORCEMENTS) {
        	TwosideKeeper.log(" Does not meet spawning requirements.",TwosideKeeper.SPAWN_DEBUG_LEVEL);
			return false;
		}
		if (isZombieLeader(ent)) {
        	TwosideKeeper.log("  Is considered a leader!",TwosideKeeper.SPAWN_DEBUG_LEVEL);
			//Zombie leaders have faster movement.
			ent.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,Integer.MAX_VALUE,1));
			//Monster m = (Monster)ent;
			LivingEntityStructure ms = TwosideKeeper.livingentitydata.get(ent.getUniqueId());
			if (ms!=null) {
				LivingEntityDifficulty led = getLivingEntityDifficulty(ent);
				ms.SetLeader(true);
				convertLivingEntity(ent,led);
	        	TwosideKeeper.log("  Converted "+GenericFunctions.GetEntityDisplayName(ent)+" to Leader!",TwosideKeeper.SPAWN_DEBUG_LEVEL);
			}
			//Set the HP of the leader to a more proper amount.
		} else
		if (meetsConditionsToBeElite(ent) && !minion) {
			LivingEntityDifficulty md = LivingEntityDifficulty.ELITE;
			TwosideKeeper.log(ChatColor.DARK_PURPLE+"Converting to Elite.", 2);
			convertLivingEntity(ent,md);
			return true;
		}
		if (ent.getWorld().getName().equalsIgnoreCase("world_the_end")) {
			
			convertLivingEntity(ent,LivingEntityDifficulty.END);
			return true;
		} else 
		if (ent.getWorld().getName().equalsIgnoreCase("world")) {
			if (ylv>=128) {
				//This is a 95% chance this will despawn.
				if (Math.random()<=0.95 && !ent.getWorld().hasStorm() &&
						ent.getWorld().getName().equalsIgnoreCase("world")) {
					ent.remove();
					return false;
				} else {
					if (isZombieLeader(ent)) {
						convertLivingEntity(ent,LivingEntityDifficulty.NORMAL);
					}
					return true;
				}
			} else 
			if (ylv>=64) {
				//This is a 90% chance this will despawn.
				if (Math.random()<=0.90 && !ent.getWorld().hasStorm() &&
						ent.getWorld().getName().equalsIgnoreCase("world")) {
					ent.remove();
					return false;
				} else {
					if (isZombieLeader(ent)) {
						
						convertLivingEntity(ent,LivingEntityDifficulty.NORMAL);
					}
					return true;
				}
			} else
			if (ylv>=48) {
				//"Normal" spawn rate. We're going to decrease it a bit for the time being.
				//This is a 50% chance this will despawn.
				if (Math.random()<=0.50 && !ent.getWorld().hasStorm()) {
					ent.remove();
					return false;
				} else {
					if (isZombieLeader(ent)) {
						convertLivingEntity(ent,LivingEntityDifficulty.NORMAL);
					}
					return true;
				}
			} else
			if (ylv>=32) {
				//Change mobs in this range to 'Dangerous' versions. Zombies and skeletons also get armor.
				
				LivingEntityDifficulty led = LivingEntityDifficulty.DANGEROUS;
				convertLivingEntity(ent,led);
				return true;
			} else
			if (ylv>=16) {
				LivingEntityDifficulty led = LivingEntityDifficulty.DEADLY;
				//Change mobs in this range to 'Dangerous' versions. Zombies and skeletons also get armor.
				
				convertLivingEntity(ent,led);
				return true;
			} else
			{
				//Change mobs in this range to 'Dangerous' versions. Zombies and skeletons also get armor.
				LivingEntityDifficulty led = LivingEntityDifficulty.HELLFIRE;
				
				convertLivingEntity(ent,led);
				return true;
			}
		} else 
		if (ent.getWorld().getName().equalsIgnoreCase("world_nether")) {
			//Difficulty is based on distance away from center.
        	TwosideKeeper.log(" "+GenericFunctions.GetEntityDisplayName(ent)+" in Nether World.",TwosideKeeper.SPAWN_DEBUG_LEVEL);
			modifyNetherMonsterHealth(ent);
			final Location center = new Location(ent.getWorld(),0,64,0);
			double chancer = ent.getLocation().distanceSquared(center);
			if ((Math.random()*chancer)<1024) {
				if (isZombieLeader(ent)) {
		        	TwosideKeeper.log(" Converting to leader!",TwosideKeeper.SPAWN_DEBUG_LEVEL);
					convertLivingEntity(ent,LivingEntityDifficulty.NORMAL);
		        	TwosideKeeper.log("  Converted "+GenericFunctions.GetEntityDisplayName(ent)+" to Leader!",TwosideKeeper.SPAWN_DEBUG_LEVEL);
				}
				return true;
			} else 
			if ((Math.random()*chancer)<65536) {
				
				convertLivingEntity(ent,LivingEntityDifficulty.DANGEROUS);
	        	TwosideKeeper.log("  Converted "+GenericFunctions.GetEntityDisplayName(ent)+" to Dangerous.",TwosideKeeper.SPAWN_DEBUG_LEVEL);
				return true;
			} else
			if ((Math.random()*chancer)<1048576) {
				
				convertLivingEntity(ent,LivingEntityDifficulty.DEADLY);
	        	TwosideKeeper.log("  Converted "+GenericFunctions.GetEntityDisplayName(ent)+" to Deadly.",TwosideKeeper.SPAWN_DEBUG_LEVEL);
				return true;
			} else {
				//Change mobs in this range to 'Dangerous' versions. Zombies and skeletons also get armor.
				LivingEntityDifficulty led = LivingEntityDifficulty.HELLFIRE;
	        	TwosideKeeper.log("  Converted "+GenericFunctions.GetEntityDisplayName(ent)+" to Hellfire.",TwosideKeeper.SPAWN_DEBUG_LEVEL);
				
				convertLivingEntity(ent,led);
				return true;
			}
		}
		return true;
	}
	
	private static void modifyNetherMonsterHealth(LivingEntity ent) {
		double hpincrease = 20;
		switch (ent.getType()) {
			case MAGMA_CUBE:{
				MagmaCube cube = (MagmaCube)ent;
				hpincrease+=cube.getSize();
			}break;
			case SKELETON:{
				hpincrease+=20;
			}break;
			default:
				break;
		}
		ent.setMaxHealth(ent.getMaxHealth()+hpincrease);
		ent.setHealth(ent.getMaxHealth());
	}

	private static boolean meetsConditionsToSpawn(LivingEntity ent) {
		double dist = 999999999;
		int nearbyplayers=0;
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (ent.getWorld().equals(p.getWorld()) && !aPlugin.API.isAFK(p)) {
				double temp = ent.getLocation().distanceSquared(p.getLocation());
				if (Math.abs(ent.getLocation().getY()-p.getLocation().getY())<=30) {
					if (temp<4096) {nearbyplayers++;}
					dist = (temp<dist)?temp:dist;
				}
			}
		}
		return (/*dist<4096*/ dist<2304 && (GenericFunctions.getNearbyMobs(ent.getLocation(), 16).size()<(nearbyplayers*2)+1));
	}

	private static boolean meetsConditionsToBeElite(LivingEntity ent) {
		if (Math.random()<=TwosideKeeper.ELITE_MONSTER_CHANCE && TwosideKeeper.LAST_ELITE_SPAWN+(72000*4)<TwosideKeeper.getServerTickTime() &&
				((ent instanceof Zombie) || ((ent instanceof Skeleton) && ((Skeleton)ent).getSkeletonType()==SkeletonType.WITHER))
				&& ent.getWorld().equals(Bukkit.getWorld("world"))) {
			TwosideKeeper.log("Trying for an elite monster.", 4);
			if (GenericFunctions.PercentBlocksAroundArea(ent.getLocation().getBlock(),Material.AIR,16,8,16)>=75 &&
					GenericFunctions.AllNaturalBlocks(ent.getLocation().getBlock(),16,8,16) &&
					ent.getNearbyEntities(64, 32, 64).size()<=3) {
						TwosideKeeper.LAST_ELITE_SPAWN=TwosideKeeper.getServerTickTime();
					return true;
			}
		}
		return false;
	}

	private static void RandomizeEquipment(LivingEntity m, int lv) {
		/*
		 * Lv1: Leather/Iron Armor.
		 * Lv2: Iron/Diamond Armor.
		 * Lv3: Diamond Armor.
		 */
		m.setCanPickupItems(false);
		switch (lv) {
			case 1:{
				if (Math.random()<0.5) {
					ItemStack helm = new ItemStack(Material.LEATHER_HELMET);
					m.getEquipment().setHelmet(helm);
				} else {
					ItemStack helm = new ItemStack(Material.IRON_HELMET);
					m.getEquipment().setHelmet(helm);
				}
				if (Math.random()<0.5) {
					ItemStack helm = new ItemStack(Material.LEATHER_CHESTPLATE);
					m.getEquipment().setChestplate(helm);
				} else {
					ItemStack helm = new ItemStack(Material.IRON_CHESTPLATE);
					m.getEquipment().setChestplate(helm);
				}
				if (Math.random()<0.5) {
					ItemStack helm = new ItemStack(Material.LEATHER_LEGGINGS);
					m.getEquipment().setLeggings(helm);
				} else {
					ItemStack helm = new ItemStack(Material.IRON_LEGGINGS);
					m.getEquipment().setLeggings(helm);
				}
				if (Math.random()<0.5) {
					ItemStack helm = new ItemStack(Material.LEATHER_BOOTS);
					m.getEquipment().setBoots(helm);
				} else {
					ItemStack helm = new ItemStack(Material.IRON_BOOTS);
					m.getEquipment().setBoots(helm);
				}
				TwosideKeeper.log("Helmet durability set to "+m.getEquipment().getHelmet().getDurability(), 5);
				TwosideKeeper.log("Chestplate durability set to "+m.getEquipment().getChestplate().getDurability(), 5);
				TwosideKeeper.log("Leggings durability set to "+m.getEquipment().getLeggings().getDurability(), 5);
				TwosideKeeper.log("Boots durability set to "+m.getEquipment().getBoots().getDurability(), 5);
				if (m.getEquipment().getHelmet()!=null && Math.random()<0.3) {
					m.getEquipment().setHelmet(RandomizeEnchantments(m.getEquipment().getHelmet(),ItemRarity.RARE));
					if (Math.random()<TwosideKeeper.RARE_DROP_RATE && m.getEquipment().getHelmet().getType()==Material.IRON_HELMET) {
						ItemStack helm = m.getEquipment().getHelmet();
						m.getEquipment().setHelmet(GenericFunctions.convertToHardenedPiece(helm, 1));
					}
				}

				if (m.getEquipment().getChestplate()!=null && Math.random()<0.3) {
					m.getEquipment().setChestplate(RandomizeEnchantments(m.getEquipment().getChestplate(),ItemRarity.RARE));
					if (Math.random()<TwosideKeeper.RARE_DROP_RATE && m.getEquipment().getChestplate().getType()==Material.IRON_CHESTPLATE) {
						ItemStack helm = m.getEquipment().getChestplate();
						m.getEquipment().setChestplate(GenericFunctions.convertToHardenedPiece(helm, 1));
					}
				}

				if (m.getEquipment().getLeggings()!=null && Math.random()<0.3) {
					m.getEquipment().setLeggings(RandomizeEnchantments(m.getEquipment().getLeggings(),ItemRarity.RARE));
					if (Math.random()<TwosideKeeper.RARE_DROP_RATE && m.getEquipment().getLeggings().getType()==Material.IRON_LEGGINGS) {
						ItemStack helm = m.getEquipment().getLeggings();
						m.getEquipment().setLeggings(GenericFunctions.convertToHardenedPiece(helm, 1));
					}
				}
				if (m.getEquipment().getBoots()!=null && Math.random()<0.3) {
					m.getEquipment().setBoots(RandomizeEnchantments(m.getEquipment().getBoots(),ItemRarity.RARE));
					if (Math.random()<TwosideKeeper.RARE_DROP_RATE && m.getEquipment().getBoots().getType()==Material.IRON_BOOTS) {
						ItemStack helm = m.getEquipment().getBoots();
						m.getEquipment().setBoots(GenericFunctions.convertToHardenedPiece(helm, 1));
					}
				}
				m.getEquipment().setBootsDropChance(0.3f);
				m.getEquipment().setChestplateDropChance(0.3f);
				m.getEquipment().setLeggingsDropChance(0.3f);
				m.getEquipment().setHelmetDropChance(0.3f);
				if ((m.getType()==EntityType.ZOMBIE &&
						!((Zombie)m).isBaby()) ||
						m.getType()==EntityType.GIANT ||
						(m.getType()==EntityType.SKELETON &&
						((Skeleton)m).getSkeletonType()==SkeletonType.WITHER)) {
					//Equip a sword or rarely, an axe.
					ItemStack weapon;
					if (Math.random()<0.03) {
						if (Math.random()<0.5) {
							weapon = new ItemStack(Material.WOOD_AXE);
						} else {
							weapon = new ItemStack(Material.AIR);
						}
						m.getEquipment().setItemInMainHand(weapon);
					} else {
						if (Math.random()<0.5) {
							weapon = new ItemStack(Material.WOOD_SWORD);
						} else {
							weapon = new ItemStack(Material.AIR);
						}
						m.getEquipment().setItemInMainHand(weapon);
					}
				} else {
					ItemStack weapon = new ItemStack(Material.BOW);
					m.getEquipment().setItemInMainHand(weapon);
				}
				if (m.getType()==EntityType.PIG_ZOMBIE) {
					ItemStack weapon = new ItemStack(Material.GOLD_SWORD);
					m.getEquipment().setItemInMainHand(weapon);
				}
			}break;
			case 2:{
				if (Math.random()<0.5) {
					ItemStack helm = new ItemStack(Material.DIAMOND_HELMET);
					m.getEquipment().setHelmet(helm);
				} else {
					ItemStack helm = new ItemStack(Material.DIAMOND_HELMET);
					m.getEquipment().setHelmet(helm);
				}
				if (Math.random()<0.5) {
					ItemStack helm = new ItemStack(Material.DIAMOND_CHESTPLATE);
					m.getEquipment().setChestplate(helm);
				} else {
					ItemStack helm = new ItemStack(Material.IRON_CHESTPLATE);
					m.getEquipment().setChestplate(helm);
				}
				if (Math.random()<0.5) {
					ItemStack helm = new ItemStack(Material.DIAMOND_LEGGINGS);
					m.getEquipment().setLeggings(helm);
				} else {
					ItemStack helm = new ItemStack(Material.IRON_LEGGINGS);
					m.getEquipment().setLeggings(helm);
				}
				if (Math.random()<0.5) {
					ItemStack helm = new ItemStack(Material.DIAMOND_BOOTS);
					m.getEquipment().setBoots(helm);
				} else {
					ItemStack helm = new ItemStack(Material.IRON_BOOTS);
					m.getEquipment().setBoots(helm);
				}

				if (m.getEquipment().getHelmet()!=null && Math.random()<0.3) {
					m.getEquipment().setHelmet(RandomizeEnchantments(m.getEquipment().getHelmet(),ItemRarity.EPIC));
					if (Math.random()<TwosideKeeper.RARE_DROP_RATE) {
						ItemStack helm = m.getEquipment().getHelmet();
						m.getEquipment().setHelmet(GenericFunctions.convertToHardenedPiece(helm, (int)(Math.random()*4)+2));
					}
				}

				if (m.getEquipment().getChestplate()!=null && Math.random()<0.3) {
					m.getEquipment().setChestplate(RandomizeEnchantments(m.getEquipment().getChestplate(),ItemRarity.EPIC));
					if (Math.random()<TwosideKeeper.RARE_DROP_RATE) {
						ItemStack helm = m.getEquipment().getChestplate();
						m.getEquipment().setChestplate(GenericFunctions.convertToHardenedPiece(helm, (int)(Math.random()*4)+2));
					}
				}

				if (m.getEquipment().getLeggings()!=null && Math.random()<0.3) {
					m.getEquipment().setLeggings(RandomizeEnchantments(m.getEquipment().getLeggings(),ItemRarity.EPIC));
					if (Math.random()<TwosideKeeper.RARE_DROP_RATE) {
						ItemStack helm = m.getEquipment().getLeggings();
						m.getEquipment().setLeggings(GenericFunctions.convertToHardenedPiece(helm, (int)(Math.random()*4)+2));
					}
				}

				if (m.getEquipment().getBoots()!=null && Math.random()<0.3) {
					m.getEquipment().setBoots(RandomizeEnchantments(m.getEquipment().getBoots(),ItemRarity.EPIC));
					if (Math.random()<TwosideKeeper.RARE_DROP_RATE) {
						ItemStack helm = m.getEquipment().getBoots();
						m.getEquipment().setBoots(GenericFunctions.convertToHardenedPiece(helm, (int)(Math.random()*4)+2));
					}
				}
				if ((m.getType()==EntityType.ZOMBIE &&
						!((Zombie)m).isBaby()) ||
						m.getType()==EntityType.GIANT ||
						(m.getType()==EntityType.SKELETON &&
						((Skeleton)m).getSkeletonType()==SkeletonType.WITHER)) {
					//Equip a sword or rarely, an axe.
					ItemStack weapon;
					if (Math.random()<0.03) {
						if (Math.random()<0.8) {
							weapon = new ItemStack(Material.WOOD_AXE);
						} else if (Math.random()<0.8) {
							weapon = new ItemStack(Material.STONE_AXE);
						} else {
							weapon = new ItemStack(Material.DIAMOND_AXE);
						}
						m.getEquipment().setItemInMainHand(weapon);
					} else {
						if (Math.random()<0.8) {
							weapon = new ItemStack(Material.WOOD_SWORD);
						} else if (Math.random()<0.8) {
							weapon = new ItemStack(Material.STONE_SWORD);
						} else {
							weapon = new ItemStack(Material.IRON_SWORD);
						}
						m.getEquipment().setItemInMainHand(weapon);
					}
					if (Math.random()<0.2) {
						ItemStack shield = new ItemStack(Material.SHIELD,1,(short)((Math.random()*DyeColor.values().length)));
						int patterns = (int)(Math.random()*7)+1;
						List<Pattern> patternlist = new ArrayList<Pattern>();
						for (int i=0;i<patterns;i++) {
							int patternnumb = (int)(Math.random()*PatternType.values().length);
							PatternType type = PatternType.values()[patternnumb];
							if (type!=PatternType.BASE) {
								patternlist.add(new Pattern(DyeColor.values()[(int)(Math.random()*DyeColor.values().length)],
										type));
							}
						}
						aPlugin.API.setShieldBannerPattern(shield, DyeColor.values()[(int)(Math.random()*DyeColor.values().length)], patternlist);
						m.getEquipment().setItemInOffHand(shield);
					}
				} else {
					ItemStack weapon = new ItemStack(Material.BOW);
					m.getEquipment().setItemInMainHand(weapon);
				}
				if (m.getType()==EntityType.PIG_ZOMBIE) {
					ItemStack weapon = new ItemStack(Material.GOLD_SWORD);
					m.getEquipment().setItemInMainHand(weapon);
				}
				TwosideKeeper.log("Helmet durability set to "+m.getEquipment().getHelmet().getDurability(), 5);
				TwosideKeeper.log("Chestplate durability set to "+m.getEquipment().getChestplate().getDurability(), 5);
				TwosideKeeper.log("Leggings durability set to "+m.getEquipment().getLeggings().getDurability(), 5);
				TwosideKeeper.log("Boots durability set to "+m.getEquipment().getBoots().getDurability(), 5);
				m.getEquipment().setBootsDropChance(0.3f);
				m.getEquipment().setChestplateDropChance(0.3f);
				m.getEquipment().setLeggingsDropChance(0.3f);
				m.getEquipment().setHelmetDropChance(0.3f);
			}break;
			case 3:{
				if (Math.random()<0.5) {
					ItemStack helm = new ItemStack(Material.DIAMOND_HELMET);
					m.getEquipment().setHelmet(helm);
				} else {
					ItemStack helm = new ItemStack(Material.GOLD_HELMET);
					m.getEquipment().setHelmet(helm);
				}
				if (Math.random()<0.5) {
					ItemStack helm = new ItemStack(Material.DIAMOND_CHESTPLATE);
					m.getEquipment().setChestplate(helm);
				} else {
					ItemStack helm = new ItemStack(Material.GOLD_CHESTPLATE);
					m.getEquipment().setChestplate(helm);
				}
				if (Math.random()<0.5) {
					ItemStack helm = new ItemStack(Material.DIAMOND_LEGGINGS);
					m.getEquipment().setLeggings(helm);
				} else {
					ItemStack helm = new ItemStack(Material.GOLD_LEGGINGS);
					m.getEquipment().setLeggings(helm);
				}
				if (Math.random()<0.5) {
					ItemStack helm = new ItemStack(Material.DIAMOND_BOOTS);
					m.getEquipment().setBoots(helm);
				} else {
					ItemStack helm = new ItemStack(Material.GOLD_BOOTS);
					m.getEquipment().setBoots(helm);
				}
				TwosideKeeper.log("Helmet durability set to "+m.getEquipment().getHelmet().getDurability(), 5);
				TwosideKeeper.log("Chestplate durability set to "+m.getEquipment().getChestplate().getDurability(), 5);
				TwosideKeeper.log("Leggings durability set to "+m.getEquipment().getLeggings().getDurability(), 5);
				TwosideKeeper.log("Boots durability set to "+m.getEquipment().getBoots().getDurability(), 5);

				if (m.getEquipment().getHelmet()!=null && Math.random()<0.3) {
					m.getEquipment().setHelmet(RandomizeEnchantments(m.getEquipment().getHelmet(),ItemRarity.LEGENDARY));
					if (Math.random()<TwosideKeeper.RARE_DROP_RATE) {
						ItemStack helm = m.getEquipment().getHelmet();
						m.getEquipment().setHelmet(GenericFunctions.convertToHardenedPiece(helm, (int)(Math.random()*8)+3));
					}
				}

				if (m.getEquipment().getChestplate()!=null && Math.random()<0.3) {
					m.getEquipment().setChestplate(RandomizeEnchantments(m.getEquipment().getChestplate(),ItemRarity.LEGENDARY));
					if (Math.random()<TwosideKeeper.RARE_DROP_RATE) {
						ItemStack helm = m.getEquipment().getChestplate();
						m.getEquipment().setChestplate(GenericFunctions.convertToHardenedPiece(helm, (int)(Math.random()*8)+3));
					}
				}

				if (m.getEquipment().getLeggings()!=null && Math.random()<0.3) {
					m.getEquipment().setLeggings(RandomizeEnchantments(m.getEquipment().getLeggings(),ItemRarity.LEGENDARY));
					if (Math.random()<TwosideKeeper.RARE_DROP_RATE) {
						ItemStack helm = m.getEquipment().getLeggings();
						m.getEquipment().setLeggings(GenericFunctions.convertToHardenedPiece(helm, (int)(Math.random()*8)+3));
					}
				}

				if (m.getEquipment().getBoots()!=null && Math.random()<0.3) {
					m.getEquipment().setBoots(RandomizeEnchantments(m.getEquipment().getBoots(),ItemRarity.LEGENDARY));
					if (Math.random()<TwosideKeeper.RARE_DROP_RATE) {
						ItemStack helm = m.getEquipment().getBoots();
						m.getEquipment().setBoots(GenericFunctions.convertToHardenedPiece(helm, (int)(Math.random()*8)+3));
					}
				}
				if ((m.getType()==EntityType.ZOMBIE &&
						!((Zombie)m).isBaby()) ||
						m.getType()==EntityType.GIANT ||
						(m.getType()==EntityType.SKELETON &&
						((Skeleton)m).getSkeletonType()==SkeletonType.WITHER)) {
					//Equip a sword or rarely, an axe.
					ItemStack weapon;
					if (Math.random()<0.03) {
						if (Math.random()<0.8) {
							weapon = new ItemStack(Material.IRON_AXE);
						} else 
						if (Math.random()<0.8) {
							weapon = new ItemStack(Material.DIAMOND_AXE);
						} else {
							weapon = new ItemStack(Material.GOLD_AXE);
						}
						m.getEquipment().setItemInMainHand(weapon);
					} else {
						if (Math.random()<0.8) {
							weapon = new ItemStack(Material.IRON_SWORD);
						} else 
						if (Math.random()<0.8) {
							weapon = new ItemStack(Material.DIAMOND_SWORD);
						} else {
							weapon = new ItemStack(Material.GOLD_SWORD);
						}
						m.getEquipment().setItemInMainHand(weapon);
					}
					if (Math.random()<0.5) {
						ItemStack shield = new ItemStack(Material.SHIELD,1,(short)((Math.random()*DyeColor.values().length)));
						int patterns = (int)(Math.random()*7)+1;
						List<Pattern> patternlist = new ArrayList<Pattern>();
						for (int i=0;i<patterns;i++) {
							int patternnumb = (int)(Math.random()*PatternType.values().length);
							PatternType type = PatternType.values()[patternnumb];
							if (type!=PatternType.BASE) {
								patternlist.add(new Pattern(DyeColor.values()[(int)(Math.random()*DyeColor.values().length)],
										type));
							}
						}
						aPlugin.API.setShieldBannerPattern(shield, DyeColor.values()[(int)(Math.random()*DyeColor.values().length)], patternlist);
						m.getEquipment().setItemInOffHand(shield);
					}
				} else {
					ItemStack weapon = new ItemStack(Material.BOW);
					m.getEquipment().setItemInMainHand(weapon);
				}
				if (m.getType()==EntityType.PIG_ZOMBIE) {
					ItemStack weapon = new ItemStack(Material.GOLD_SWORD);
					m.getEquipment().setItemInMainHand(weapon);
				}
				m.getEquipment().setBootsDropChance(0.3f);
				m.getEquipment().setChestplateDropChance(0.3f);
				m.getEquipment().setLeggingsDropChance(0.3f);
				m.getEquipment().setHelmetDropChance(0.3f);
			}break;
			case 4:{
				ItemStack helm = new ItemStack(Material.GOLD_HELMET);
				m.getEquipment().setHelmet(helm);
				m.getEquipment().setHelmet(Loot.GenerateMegaPiece(helm.getType(), true, true, 1));
				helm = new ItemStack(Material.GOLD_CHESTPLATE);
				m.getEquipment().setChestplate(helm);
				m.getEquipment().setChestplate(Loot.GenerateMegaPiece(helm.getType(), true, true, 1));
				helm = new ItemStack(Material.GOLD_LEGGINGS);
				m.getEquipment().setLeggings(helm);
				m.getEquipment().setLeggings(Loot.GenerateMegaPiece(helm.getType(), true, true, 1));
				helm = new ItemStack(Material.GOLD_BOOTS);
				m.getEquipment().setBoots(helm);
				m.getEquipment().setBoots(Loot.GenerateMegaPiece(helm.getType(), true, true, 1));
				TwosideKeeper.log("Helmet durability set to "+m.getEquipment().getHelmet().getDurability(), 5);
				TwosideKeeper.log("Chestplate durability set to "+m.getEquipment().getChestplate().getDurability(), 5);
				TwosideKeeper.log("Leggings durability set to "+m.getEquipment().getLeggings().getDurability(), 5);
				TwosideKeeper.log("Boots durability set to "+m.getEquipment().getBoots().getDurability(), 5);
				if ((m.getType()==EntityType.ZOMBIE &&
						!((Zombie)m).isBaby()) ||
						m.getType()==EntityType.GIANT ||
						(m.getType()==EntityType.SKELETON &&
						((Skeleton)m).getSkeletonType()==SkeletonType.WITHER)) {
					//Equip a sword or rarely, an axe.
					ItemStack weapon;
					if (Math.random()<0.03) {
						weapon = new ItemStack(Material.GOLD_AXE);
						m.getEquipment().setItemInMainHand(Loot.GenerateMegaPiece(weapon.getType(), true, true, 1));
					} else {
						weapon = new ItemStack(Material.GOLD_SWORD);
						m.getEquipment().setItemInMainHand(Loot.GenerateMegaPiece(weapon.getType(), true, true, 1));
					}
					if (Math.random()<0.5) {
						ItemStack shield = new ItemStack(Material.SHIELD,1,(short)((Math.random()*DyeColor.values().length)));
						int patterns = (int)(Math.random()*7)+1;
						List<Pattern> patternlist = new ArrayList<Pattern>();
						for (int i=0;i<patterns;i++) {
							int patternnumb = (int)(Math.random()*PatternType.values().length);
							PatternType type = PatternType.values()[patternnumb];
							if (type!=PatternType.BASE) {
								patternlist.add(new Pattern(DyeColor.values()[(int)(Math.random()*DyeColor.values().length)],
										type));
							}
						}
						aPlugin.API.setShieldBannerPattern(shield, DyeColor.values()[(int)(Math.random()*DyeColor.values().length)], patternlist);
						m.getEquipment().setItemInOffHand(shield);
					}
				} else {
					ItemStack weapon = new ItemStack(Material.BOW);
					m.getEquipment().setItemInMainHand(Loot.GenerateMegaPiece(weapon.getType(), true, true, 1));
				}
				if (m.getType()==EntityType.PIG_ZOMBIE) {
					ItemStack weapon = new ItemStack(Material.GOLD_SWORD);
					m.getEquipment().setItemInMainHand(Loot.GenerateMegaPiece(weapon.getType(), true, true, 1));
				}
				m.getEquipment().setBootsDropChance(1.0f);
				m.getEquipment().setChestplateDropChance(1.0f);
				m.getEquipment().setLeggingsDropChance(1.0f);
				m.getEquipment().setHelmetDropChance(1.0f);
				m.getEquipment().setItemInMainHandDropChance(1.0f);
			}break;
			default:{
				if (Math.random()<0.1) {
					if (Math.random()<0.5) {
						ItemStack helm = new ItemStack(Material.LEATHER_HELMET);
						m.getEquipment().setHelmet(helm);
						helm = new ItemStack(Material.LEATHER_CHESTPLATE);
						m.getEquipment().setChestplate(helm);
						helm = new ItemStack(Material.LEATHER_LEGGINGS);
						m.getEquipment().setLeggings(helm);
						helm = new ItemStack(Material.LEATHER_BOOTS);
						helm.setDurability((short)Math.round(Math.random()*64));
						m.getEquipment().setBoots(helm);
					} else {
						ItemStack helm = new ItemStack(Material.IRON_HELMET);
						m.getEquipment().setHelmet(helm);
						helm = new ItemStack(Material.IRON_CHESTPLATE);
						helm.setDurability((short)Math.round(Math.random()*128));
						helm = new ItemStack(Material.IRON_LEGGINGS);
						helm.setDurability((short)Math.round(Math.random()*128));
						helm = new ItemStack(Material.IRON_BOOTS);
						helm.setDurability((short)Math.round(Math.random()*128));
						m.getEquipment().setBoots(helm);
					}
					TwosideKeeper.log("Helmet durability set to "+m.getEquipment().getHelmet().getDurability(), 5);
					TwosideKeeper.log("Chestplate durability set to "+m.getEquipment().getChestplate().getDurability(), 5);
					TwosideKeeper.log("Leggings durability set to "+m.getEquipment().getLeggings().getDurability(), 5);
					TwosideKeeper.log("Boots durability set to "+m.getEquipment().getBoots().getDurability(), 5);
				}
				if ((m.getType()==EntityType.ZOMBIE &&
						!((Zombie)m).isBaby()) ||
						m.getType()==EntityType.GIANT ||
						(m.getType()==EntityType.SKELETON &&
						((Skeleton)m).getSkeletonType()==SkeletonType.WITHER)) {
					//Equip a sword or rarely, an axe.
					ItemStack weapon;
					if (Math.random()<0.03) {
						if (Math.random()<0.2) {
							weapon = new ItemStack(Material.WOOD_AXE);
						} else {
							weapon = new ItemStack(Material.AIR);
						}
						m.getEquipment().setItemInMainHand(weapon);
					} else {
						if (Math.random()<0.5) {
							weapon = new ItemStack(Material.WOOD_SWORD);
						} else {
							weapon = new ItemStack(Material.AIR);
						}
						m.getEquipment().setItemInMainHand(weapon);
					}
				} else {
					ItemStack weapon = new ItemStack(Material.BOW);
					m.getEquipment().setItemInMainHand(weapon);
				}
				if (m.getType()==EntityType.PIG_ZOMBIE) {
					ItemStack weapon = new ItemStack(Material.GOLD_SWORD);
					m.getEquipment().setItemInMainHand(weapon);
				}
			}
		}
	}
	
	private static ItemStack RandomizeEnchantments(ItemStack item, ItemRarity rarity) {
		return Loot.addEnchantments(item, false);
	}
	
	public static boolean isZombieLeader(LivingEntity ent) {
		if ((ent instanceof PigZombie) && ent.getWorld().getName().equalsIgnoreCase("world_nether")) {
			MonsterDifficulty md = getMonsterDifficulty((Monster)ent);
			if
					(
							(md==MonsterDifficulty.NORMAL && ent.getMaxHealth()>40) ||
							(md==MonsterDifficulty.DANGEROUS && ent.getMaxHealth()>40*2) ||
							(md==MonsterDifficulty.DEADLY && ent.getMaxHealth()>40*3) ||
							(md==MonsterDifficulty.HELLFIRE && ent.getMaxHealth()>40*4) ||
							(md==MonsterDifficulty.END && ent.getMaxHealth()>40*80)
					)
				 {
				return true;
			} else {
				return false;
			}
		} else 
		if ((ent instanceof Zombie)) {
			MonsterDifficulty md = getMonsterDifficulty((Monster)ent);
			if
					( ent.getWorld().getName().equalsIgnoreCase("world") &&
							((md==MonsterDifficulty.NORMAL && ent.getMaxHealth()>20) ||
							(md==MonsterDifficulty.DANGEROUS && ent.getMaxHealth()>20*2) ||
							(md==MonsterDifficulty.DEADLY && ent.getMaxHealth()>20*3) ||
							(md==MonsterDifficulty.HELLFIRE && ent.getMaxHealth()>20*4) ||
							(md==MonsterDifficulty.END && ent.getMaxHealth()>20*80))
					)
				 {
				return true;
			} else {
				return false;
			}
		} else 
		{
			return false;
		}
	} 
	

	public static LivingEntity convertLivingEntity(LivingEntity m) {
		if (m.getWorld().getName().equalsIgnoreCase("world_the_end")) {
			convertLivingEntity(m,LivingEntityDifficulty.END);
		}
		if (m.getWorld().getName().equalsIgnoreCase("world")) {
			if (m.getLocation().getY()<48) {
				if (m.getLocation().getY()>=32)
				return convertLivingEntity(m,LivingEntityDifficulty.DANGEROUS);
				else if (m.getLocation().getY()>=16)
				return convertLivingEntity(m,LivingEntityDifficulty.DEADLY);
				else
				return convertLivingEntity(m,LivingEntityDifficulty.HELLFIRE);
			} else {
				return convertLivingEntity(m,LivingEntityDifficulty.NORMAL);
			}
		}
		if (m.getWorld().getName().equalsIgnoreCase("world_nether")) {
			//Difficulty is based on distance away from center.
			final Location center = new Location(m.getWorld(),0,64,0);
			double chancer = m.getLocation().distanceSquared(center);
			TwosideKeeper.log("Chance: "+chancer, 1);
			if ((Math.random()*chancer)<1024) {
				return convertLivingEntity(m,LivingEntityDifficulty.NORMAL);
			} else 
			if ((Math.random()*chancer)<65536) {
				return convertLivingEntity(m,LivingEntityDifficulty.DANGEROUS);
			} else
			if ((Math.random()*chancer)<1048576) {
				return convertLivingEntity(m,LivingEntityDifficulty.DEADLY);
			} else {
				//Change mobs in this range to 'Dangerous' versions. Zombies and skeletons also get armor.
				LivingEntityDifficulty led = LivingEntityDifficulty.HELLFIRE;
				return convertLivingEntity(m,led);
			}
		}
		return m;
	}

	/**
	 * Use convertLivingEntity() instead.
	 */
	@Deprecated
	public static Monster convertMonster(Monster m) {
		if (m.getWorld().getName().equalsIgnoreCase("world_the_end")) {
			convertMonster(m,MonsterDifficulty.END);
		}
		if (m.getLocation().getY()<48) {
			if (m.getLocation().getY()>=32)
			return convertMonster(m,MonsterDifficulty.DANGEROUS);
			else if (m.getLocation().getY()>=16)
			return convertMonster(m,MonsterDifficulty.DEADLY);
			else
			return convertMonster(m,MonsterDifficulty.HELLFIRE);
		} else {
			return convertMonster(m,MonsterDifficulty.NORMAL);
		}
	}
	
	public static LivingEntityDifficulty getLivingEntityDifficulty(LivingEntity m) {
		/*if (m.getCustomName()!=null) {
			if (m.getCustomName().contains("Dangerous")) {
				return LivingEntityDifficulty.DANGEROUS;
			} else
			if (m.getCustomName().contains("Deadly")) {
				return LivingEntityDifficulty.DEADLY;
			} else
			if (m.getCustomName().contains("Hellfire")) {
				return LivingEntityDifficulty.HELLFIRE;
			} else
			if (m.getCustomName().contains("Elite")) {
				return LivingEntityDifficulty.ELITE;
			} else
			if (m.getCustomName().contains("End ")) {
				return LivingEntityDifficulty.END;
			} else
			{
				return LivingEntityDifficulty.NORMAL;
			}
		} else {
			return LivingEntityDifficulty.NORMAL;
		}*/
		LivingEntityStructure les = LivingEntityStructure.GetLivingEntityStructure(m);
		String difficulty_modifier = les.difficulty_modifier;
		if (difficulty_modifier.contains("Dangerous")) {
			return LivingEntityDifficulty.DANGEROUS;
		} else
		if (difficulty_modifier.contains("Deadly")) {
			return LivingEntityDifficulty.DEADLY;
		} else
		if (difficulty_modifier.contains("Hellfire")) {
			return LivingEntityDifficulty.HELLFIRE;
		} else
		if (difficulty_modifier.contains("Elite")) {
			return LivingEntityDifficulty.ELITE;
		} else
		if (difficulty_modifier.contains("End ")) {
			return LivingEntityDifficulty.END;
		} else
		{
			return LivingEntityDifficulty.NORMAL;
		}
	}
	
	@Deprecated
	public static LivingEntityDifficulty getOldLivingEntityDifficulty(LivingEntity m) {
		if (m.getCustomName()!=null) {
			if (m.getCustomName().contains("Dangerous")) {
				return LivingEntityDifficulty.DANGEROUS;
			} else
			if (m.getCustomName().contains("Deadly")) {
				return LivingEntityDifficulty.DEADLY;
			} else
			if (m.getCustomName().contains("Hellfire")) {
				return LivingEntityDifficulty.HELLFIRE;
			} else
			if (m.getCustomName().contains("Elite")) {
				return LivingEntityDifficulty.ELITE;
			} else
			if (m.getCustomName().contains("End ")) {
				return LivingEntityDifficulty.END;
			} else
			{
				return null;
			}
		} else {
			return null;
		}
	}
	
	@Deprecated
	public static MonsterDifficulty getMonsterDifficulty(Monster m) {
		/*if (m.getCustomName()!=null) {
			if (m.getCustomName().contains("Dangerous")) {
				return MonsterDifficulty.DANGEROUS;
			} else
			if (m.getCustomName().contains("Deadly")) {
				return MonsterDifficulty.DEADLY;
			} else
			if (m.getCustomName().contains("Hellfire")) {
				return MonsterDifficulty.HELLFIRE;
			} else
			if (m.getCustomName().contains("Elite")) {
				return MonsterDifficulty.ELITE;
			} else
			if (m.getCustomName().contains("End ")) {
				return MonsterDifficulty.END;
			} else
			{
				return MonsterDifficulty.NORMAL;
			}
		} else {
			return MonsterDifficulty.NORMAL;
		}*/
		LivingEntityStructure les = LivingEntityStructure.GetLivingEntityStructure(m);
		String difficulty_modifier = les.difficulty_modifier;
		if (difficulty_modifier.contains("Dangerous")) {
			return MonsterDifficulty.DANGEROUS;
		} else
		if (difficulty_modifier.contains("Deadly")) {
			return MonsterDifficulty.DEADLY;
		} else
		if (difficulty_modifier.contains("Hellfire")) {
			return MonsterDifficulty.HELLFIRE;
		} else
		if (difficulty_modifier.contains("Elite")) {
			return MonsterDifficulty.ELITE;
		} else
		if (difficulty_modifier.contains("End ")) {
			return MonsterDifficulty.END;
		} else
		{
			return MonsterDifficulty.NORMAL;
		}
	}
	
	public static void SetupCustomName(LivingEntityDifficulty diff, LivingEntity m) {
		String MonsterName = m.getType().toString().toLowerCase();
		if (m.getType()==EntityType.SKELETON) {
			Skeleton ss = (Skeleton)m;
			if (ss.getSkeletonType()==SkeletonType.WITHER) {
				MonsterName = "wither skeleton";
			}
		}
		if (m.getType()==EntityType.GUARDIAN) {
			Guardian gg = (Guardian)m;
			if (gg.isElder()) {
				MonsterName = "guardian boss";
			}
		}
		//m.setCustomName(prefix.equalsIgnoreCase("")?"":(prefix+" ")+GenericFunctions.CapitalizeFirstLetters(MonsterName.replaceAll("_", " ")+(isZombieLeader(m)?" Leader":"")));
		LivingEntityStructure les = LivingEntityStructure.GetLivingEntityStructure(m);
		les.difficulty_modifier = diff.getDifficultyString();
		les.suffix = (isZombieLeader(m)?"Leader":"");
	}
	
	public static LivingEntity convertLivingEntity(LivingEntity m, LivingEntityDifficulty led) {
		switch (led) {
			case DANGEROUS: {
				if (isAllowedToEquipItems(m)) {
					m.getEquipment().clear();
					RandomizeEquipment(m,1);
				} else {
					m.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,Integer.MAX_VALUE,1));
				}
				SetupCustomName(led,m);
				if(isZombieLeader(m))
				{
		        	TwosideKeeper.log("   Converting "+GenericFunctions.GetEntityDisplayName(m)+" to Leader.",TwosideKeeper.SPAWN_DEBUG_LEVEL);
					m.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,Integer.MAX_VALUE,8));
					//GlowAPI.setGlowing(m, Color.DARK_RED, Bukkit.getOnlinePlayers());
					m.setMaxHealth(800); //Target is 800 HP.
					m.setHealth(m.getMaxHealth());
					TwosideKeeper.log(m.getCustomName()+" health is "+m.getMaxHealth(), 5);
					LivingEntityStructure ms = LivingEntityStructure.GetLivingEntityStructure(m);
					ms.SetLeader(true);
					ms.UpdateGlow();
    				TwosideKeeper.log("->Setting an entity with Difficulty "+led.name()+" w/"+m.getHealth()+"/"+m.getMaxHealth()+" HP to a Leader.",5);
				} else {
					m.setMaxHealth(m.getMaxHealth()*2.0);
					m.setHealth(m.getMaxHealth());
				}
				if (!GenericFunctions.isArmoredMob(m)) {
					m.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,Integer.MAX_VALUE,1));
				}
				m.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(32.0);
			}break;
			case DEADLY: {
				if (isAllowedToEquipItems(m)) {
					m.getEquipment().clear();
					RandomizeEquipment(m,2);
				} else {
					m.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,Integer.MAX_VALUE,4));
				}
				SetupCustomName(led,m);
				if(isZombieLeader(m))
				{
		        	TwosideKeeper.log("   Converting "+GenericFunctions.GetEntityDisplayName(m)+" to Leader.",TwosideKeeper.SPAWN_DEBUG_LEVEL);
					m.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,Integer.MAX_VALUE,8));
					m.setMaxHealth(1200); //Target is 1200 HP.
					m.setHealth(m.getMaxHealth());
					LivingEntityStructure ms = LivingEntityStructure.GetLivingEntityStructure(m);
					ms.SetLeader(true);
					ms.UpdateGlow();
    				TwosideKeeper.log("->Setting an entity with Difficulty "+led.name()+" w/"+m.getHealth()+"/"+m.getMaxHealth()+" HP to a Leader.",5);
				} else {
					m.setMaxHealth(m.getMaxHealth()*3.0);
					m.setHealth(m.getMaxHealth());
				}
				if (!GenericFunctions.isArmoredMob(m)) {
					m.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,Integer.MAX_VALUE,3));
				}
				m.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(48.0);
			}break;
			case HELLFIRE:{
				//m.setCustomName(ChatColor.DARK_AQUA+"Dangerous Mob");
				//m.setCustomNameVisible(true);
				if (m.getType()!=EntityType.ENDERMAN) {
					m.setFireTicks(Integer.MAX_VALUE);
				} 
				if (isAllowedToEquipItems(m)) {
					m.getEquipment().clear();
					RandomizeEquipment(m,3);
				} else {
					m.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,Integer.MAX_VALUE,8));
				}
				m.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,Integer.MAX_VALUE,1));
				if (Math.random()<=0.2) {m.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,Integer.MAX_VALUE,1));}
				SetupCustomName(led,m);
				if(isZombieLeader(m))
				{
		        	TwosideKeeper.log("   Converting "+GenericFunctions.GetEntityDisplayName(m)+" to Leader.",TwosideKeeper.SPAWN_DEBUG_LEVEL);
					m.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,Integer.MAX_VALUE,8));
					LivingEntityStructure ms = LivingEntityStructure.GetLivingEntityStructure(m);
					ms.SetLeader(true);
					ms.UpdateGlow();
					m.setMaxHealth(1600); //Target is 1600 HP.
					m.setHealth(m.getMaxHealth());
					LivingEntityStructure.GetLivingEntityStructure(m).SetLeader(true);
    				TwosideKeeper.log("->Setting an entity with Difficulty "+led.name()+" w/"+m.getHealth()+"/"+m.getMaxHealth()+" HP to a Leader.",5);
				} else {
					m.setMaxHealth(m.getMaxHealth()*4.0);
					m.setHealth(m.getMaxHealth());
				}
				if (!GenericFunctions.isArmoredMob(m)) {
					m.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,Integer.MAX_VALUE,5));
				}
				m.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(64.0);
			}break;
			case ELITE:{
				SetupCustomName(led,m);
				//m.setCustomName(ChatColor.DARK_AQUA+"Dangerous Mob");
				//m.setCustomNameVisible(true);
				m.setMaxHealth(48000);
				m.setHealth(m.getMaxHealth());
				if (isAllowedToEquipItems(m)) {
					m.getEquipment().clear();
					RandomizeEquipment(m,4);
				}
				//m.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,Integer.MAX_VALUE,8));
				m.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,Integer.MAX_VALUE,8));
				if (!GenericFunctions.isArmoredMob(m)) {
					//m.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,Integer.MAX_VALUE,8));
					m.setMaxHealth(m.getMaxHealth()*2.0);
				}
				m.setCustomNameVisible(true);
				m.setRemoveWhenFarAway(false);
				LivingEntityStructure ms = LivingEntityStructure.GetLivingEntityStructure(m);
				ms.SetElite(true);
				ms.UpdateGlow();
				m.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(72.0);
			}break;
			default: {
				if (isAllowedToEquipItems(m)) {
					m.getEquipment().clear();
					RandomizeEquipment(m,0);
				} else {
					m.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,Integer.MAX_VALUE,0));
				}
				SetupCustomName(led,m);
				if(isZombieLeader(m))
				{
					m.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,Integer.MAX_VALUE,8));
					m.setMaxHealth(400);
					m.setHealth(m.getMaxHealth());
					m.setCustomName("Zombie Leader");
					LivingEntityStructure ms = LivingEntityStructure.GetLivingEntityStructure(m);
					ms.SetLeader(true);
					ms.UpdateGlow();
    				TwosideKeeper.log("->Setting an entity with Difficulty "+led.name()+" w/"+m.getHealth()+"/"+m.getMaxHealth()+" HP to a Leader.",5);
				} else {
					m.setMaxHealth(m.getMaxHealth()*1.0);
					m.setHealth(m.getMaxHealth());
				}
				m.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(24.0);
			}break;
			case END:{
				//m.setCustomName(ChatColor.DARK_AQUA+"Dangerous Mob");
				//m.setCustomNameVisible(true);
				if (m.getType()!=EntityType.ENDERMAN) {
					m.setFireTicks(Integer.MAX_VALUE);
				} 
				if (isAllowedToEquipItems(m)) {
					m.getEquipment().clear();
					RandomizeEquipment(m,0);
				}
				m.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,Integer.MAX_VALUE,1));
				m.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,Integer.MAX_VALUE,3));
				SetupCustomName(led,m);
				if(isZombieLeader(m))
				{
					m.setMaxHealth(32000); //Target is 1600 HP.
					m.setHealth(m.getMaxHealth());
					LivingEntityStructure ms = LivingEntityStructure.GetLivingEntityStructure(m);
					ms.SetLeader(true);
					ms.UpdateGlow();
    				TwosideKeeper.log("->Setting an entity with Difficulty "+led.name()+" w/"+m.getHealth()+"/"+m.getMaxHealth()+" HP to a Leader.",5);
				} else {
					m.setMaxHealth(m.getMaxHealth()*80.0);
					m.setHealth(m.getMaxHealth());
				}
				m.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(64.0);
			}break;
		}
		removeZombieLeaderAttribute(m);
		return m;
	}

	/**
	 * Use convertLivingEntity() instead.
	 */
	@Deprecated
	public static Monster convertMonster(Monster m, MonsterDifficulty md) {
		LivingEntityDifficulty led = md.getLivingEntityDifficultyEquivalent();
		switch (led) {
			case DANGEROUS: {
				if (isAllowedToEquipItems(m)) {
					m.getEquipment().clear();
					RandomizeEquipment(m,1);
				} else {
					m.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,Integer.MAX_VALUE,1));
				}
				SetupCustomName(led,m);
				if(isZombieLeader(m))
				{
					m.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,Integer.MAX_VALUE,8));
					//GlowAPI.setGlowing(m, Color.DARK_RED, Bukkit.getOnlinePlayers());
					m.setMaxHealth(800); //Target is 800 HP.
					m.setHealth(m.getMaxHealth());
					TwosideKeeper.log(m.getCustomName()+" health is "+m.getMaxHealth(), 5);
					LivingEntityStructure ms = LivingEntityStructure.GetLivingEntityStructure(m);
					ms.SetLeader(true);
					ms.UpdateGlow();
    				TwosideKeeper.log("->Setting a monster with Difficulty "+md.name()+" w/"+m.getHealth()+"/"+m.getMaxHealth()+" HP to a Leader.",5);
				} else {
					m.setMaxHealth(m.getMaxHealth()*2.0);
					m.setHealth(m.getMaxHealth());
				}
				if (!GenericFunctions.isArmoredMob(m)) {
					m.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,Integer.MAX_VALUE,1));
				}
				m.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(32.0);
			}break;
			case DEADLY: {
				if (isAllowedToEquipItems(m)) {
					m.getEquipment().clear();
					RandomizeEquipment(m,2);
				} else {
					m.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,Integer.MAX_VALUE,4));
				}
				SetupCustomName(led,m);
				if(isZombieLeader(m))
				{
					m.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,Integer.MAX_VALUE,8));
					m.setMaxHealth(1200); //Target is 1200 HP.
					m.setHealth(m.getMaxHealth());
					LivingEntityStructure ms = LivingEntityStructure.GetLivingEntityStructure(m);
					ms.SetLeader(true);
					ms.UpdateGlow();
    				TwosideKeeper.log("->Setting a monster with Difficulty "+md.name()+" w/"+m.getHealth()+"/"+m.getMaxHealth()+" HP to a Leader.",5);
				} else {
					m.setMaxHealth(m.getMaxHealth()*3.0);
					m.setHealth(m.getMaxHealth());
				}
				if (!GenericFunctions.isArmoredMob(m)) {
					m.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,Integer.MAX_VALUE,3));
				}
				m.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(48.0);
			}break;
			case HELLFIRE:{
				//m.setCustomName(ChatColor.DARK_AQUA+"Dangerous Mob");
				//m.setCustomNameVisible(true);
				if (m.getType()!=EntityType.ENDERMAN) {
					m.setFireTicks(Integer.MAX_VALUE);
				} 
				if (isAllowedToEquipItems(m)) {
					m.getEquipment().clear();
					RandomizeEquipment(m,3);
				} else {
					m.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,Integer.MAX_VALUE,8));
				}
				m.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,Integer.MAX_VALUE,1));
				if (Math.random()<=0.2) {m.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,Integer.MAX_VALUE,1));}
				SetupCustomName(led,m);
				if(isZombieLeader(m))
				{
					m.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,Integer.MAX_VALUE,8));
					LivingEntityStructure ms = LivingEntityStructure.GetLivingEntityStructure(m);
					ms.SetLeader(true);
					ms.UpdateGlow();
					m.setMaxHealth(1600); //Target is 1600 HP.
					m.setHealth(m.getMaxHealth());
					LivingEntityStructure.GetLivingEntityStructure(m).SetLeader(true);
    				TwosideKeeper.log("->Setting a monster with Difficulty "+md.name()+" w/"+m.getHealth()+"/"+m.getMaxHealth()+" HP to a Leader.",5);
				} else {
					m.setMaxHealth(m.getMaxHealth()*4.0);
					m.setHealth(m.getMaxHealth());
				}
				if (!GenericFunctions.isArmoredMob(m)) {
					m.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,Integer.MAX_VALUE,5));
				}
				m.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(64.0);
			}break;
			case ELITE:{
				SetupCustomName(led,m);
				//m.setCustomName(ChatColor.DARK_AQUA+"Dangerous Mob");
				//m.setCustomNameVisible(true);
				m.setMaxHealth(4800);
				m.setHealth(m.getMaxHealth());
				if (isAllowedToEquipItems(m)) {
					m.getEquipment().clear();
					RandomizeEquipment(m,4);
				}
				m.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,Integer.MAX_VALUE,8));
				m.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,Integer.MAX_VALUE,8));
				if (!GenericFunctions.isArmoredMob(m)) {
					m.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,Integer.MAX_VALUE,8));
					m.setMaxHealth(m.getMaxHealth()*2.0);
				}
				m.setCustomNameVisible(true);
				m.setRemoveWhenFarAway(false);
				LivingEntityStructure ms = LivingEntityStructure.GetLivingEntityStructure(m);
				ms.SetElite(true);
				ms.UpdateGlow();
				m.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(72.0);
			}break;
			default: {
				if (isAllowedToEquipItems(m)) {
					m.getEquipment().clear();
					RandomizeEquipment(m,0);
				} else {
					m.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,Integer.MAX_VALUE,0));
				}
				SetupCustomName(led,m);
				if(isZombieLeader(m))
				{
					m.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,Integer.MAX_VALUE,8));
					m.setMaxHealth(400);
					m.setHealth(m.getMaxHealth());
					m.setCustomName("Zombie Leader");
					LivingEntityStructure ms = LivingEntityStructure.GetLivingEntityStructure(m);
					ms.SetLeader(true);
					ms.UpdateGlow();
    				TwosideKeeper.log("->Setting a monster with Difficulty "+md.name()+" w/"+m.getHealth()+"/"+m.getMaxHealth()+" HP to a Leader.",5);
				} else {
					m.setMaxHealth(m.getMaxHealth()*1.0);
					m.setHealth(m.getMaxHealth());
				}
				m.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(24.0);
			}break;
			case END:{
				//m.setCustomName(ChatColor.DARK_AQUA+"Dangerous Mob");
				//m.setCustomNameVisible(true);
				if (m.getType()!=EntityType.ENDERMAN) {
					m.setFireTicks(Integer.MAX_VALUE);
				} 
				if (isAllowedToEquipItems(m)) {
					m.getEquipment().clear();
					RandomizeEquipment(m,0);
				}
				m.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,Integer.MAX_VALUE,1));
				m.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,Integer.MAX_VALUE,3));
				SetupCustomName(led,m);
				if(isZombieLeader(m))
				{
					m.setMaxHealth(32000); //Target is 1600 HP.
					m.setHealth(m.getMaxHealth());
					LivingEntityStructure ms = LivingEntityStructure.GetLivingEntityStructure(m);
					ms.SetLeader(true);
					ms.UpdateGlow();
    				TwosideKeeper.log("->Setting a monster with Difficulty "+md.name()+" w/"+m.getHealth()+"/"+m.getMaxHealth()+" HP to a Leader.",5);
				} else {
					m.setMaxHealth(m.getMaxHealth()*80.0);
					m.setHealth(m.getMaxHealth());
				}
				m.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(64.0);
			}break;
		}
		removeZombieLeaderAttribute(m);
		return m;
	}
	
	public static void removeZombieLeaderAttribute(LivingEntity m) {
		final AttributeInstance attribute = m.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        final Collection<AttributeModifier> modifiers = attribute.getModifiers();
        for (AttributeModifier modifier : modifiers) {
            if (modifier.getName().equals("Leader zombie bonus")) {
                attribute.removeModifier(modifier);
            }
        }
	}

	private static boolean isAllowedToEquipItems(LivingEntity m) {
		if (m.getType()==EntityType.ZOMBIE ||
			m.getType()==EntityType.PIG_ZOMBIE ||
			m.getType()==EntityType.SKELETON ||
			m.getType()==EntityType.GIANT) {
			return true;
		} else {
			return false;
		}
	}
	
	public static LivingEntity spawnAdjustedLivingEntity(EntityType et, Location loc) {
		if (TwosideKeeper.LIVING_ENTITY_TYPES.contains(et)) {
			LivingEntity le = (LivingEntity)loc.getWorld().spawnEntity(loc, et);
			return MonsterController.convertLivingEntity(le);
		} else {
			TwosideKeeper.log("Tried to spawn an entity that is NOT LIVING! ("+et.name()+") Do not do this!", 0);
			return null;
		}
	}
	
	/**
	 * Use spawnAdjustedLivingEntity() instead.
	 */
	@Deprecated
	public static Monster spawnAdjustedMonster(MonsterType mt,Location loc) {
		Monster m = (Monster)loc.getWorld().spawnEntity(loc, mt.getEntityType());
		if (mt.equals(MonsterType.WITHER_SKELETON)) {
			Skeleton sk = (Skeleton)m;
			sk.setSkeletonType(SkeletonType.WITHER);
		}
		return MonsterController.convertMonster(m);
	}
	
	public static boolean isChargeZombie(LivingEntity m) {
		if ((m.getType()==EntityType.ZOMBIE || m.getType()==EntityType.PIG_ZOMBIE) &&
				MonsterController.getMonsterDifficulty((Monster)m)==MonsterDifficulty.HELLFIRE) {
			return true;
		}
		return false;
	}
	
	public static boolean isUndead(LivingEntity m) {
		if (m.getType()==EntityType.ZOMBIE ||
			m.getType()==EntityType.PIG_ZOMBIE ||
			m.getType()==EntityType.GIANT ||
			m.getType()==EntityType.SKELETON ||
			m.getType()==EntityType.WITHER
			) {
			return true;
		}
		return false;
	}

	public static boolean isHellfireSpider(LivingEntity m) {
		if ((m.getType()==EntityType.SPIDER || m.getType()==EntityType.CAVE_SPIDER) &&
				MonsterController.getMonsterDifficulty((Monster)m)==MonsterDifficulty.HELLFIRE) {
			return true;
		}
		return false;
	}
	
	public static boolean isHellfireGhast(LivingEntity m) {
		if (m.getType()==EntityType.GHAST &&
				MonsterController.getLivingEntityDifficulty(m)==LivingEntityDifficulty.HELLFIRE) {
			return true;
		}
		return false;
	}
	public static void HandleWitherSpawn(LivingEntity ent) {
		//TODO Make Elites spawn at this Y level later.
		/*if (ent.getLocation().getY()<54) {
			
		} else */ {
			LivingEntityStructure les = LivingEntityStructure.GetLivingEntityStructure(ent);
			les.SetLeader(true);
			les.m.setMaxHealth(480000);
			les.m.setCustomName(ChatColor.RED+"Leader Wither");
			les.m.setCustomNameVisible(true);
			les.m.setHealth(les.m.getMaxHealth());
			if (les.m.getLocation().getY()>=128) {
				les.m.teleport(les.m.getLocation().add(0,-32,0));
			}
		}
	}
}
