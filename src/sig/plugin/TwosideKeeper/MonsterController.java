package sig.plugin.TwosideKeeper;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Zombie;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import sig.plugin.TwosideKeeper.HelperStructures.ItemRarity;
import sig.plugin.TwosideKeeper.HelperStructures.MonsterDifficulty;
import sig.plugin.TwosideKeeper.HelperStructures.MonsterType;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;

public class MonsterController {
	/**
	 * @return Returns false if this spawn is not allowed.
	 */
	public static boolean MobHeightControl(LivingEntity ent) {
		//Modify spawning algorithm.
		int ylv = ent.getLocation().getBlockY();
		if (isZombieLeader(ent)) {
			//Zombie leaders have faster movement.
			ent.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,999999,1));
		}
		if (ylv>=128) {
			//This is a 95% chance this will despawn.
			if (Math.random()<=0.95) {
				ent.remove();
				return false;
			} else {
				if (isZombieLeader(ent)) {
					Monster m = (Monster)ent;
					m.setCustomName(ChatColor.WHITE+"Zombie Leader");
				}
				return true;
			}
		} else 
		if (ylv>=64) {
			//This is a 90% chance this will despawn.
			if (Math.random()<=0.90) {
				ent.remove();
				return false;
			} else {
				if (isZombieLeader(ent)) {
					Monster m = (Monster)ent;
					m.setCustomName(ChatColor.WHITE+"Zombie Leader");
				}
				return true;
			}
		} else
		if (ylv>=48) {
			//"Normal" spawn rate. We're going to decrease it a bit for the time being.
			//This is a 50% chance this will despawn.
			if (Math.random()<=0.50) {
				ent.remove();
				return false;
			} else {
				if (isZombieLeader(ent)) {
					Monster m = (Monster)ent;
					m.setCustomName(ChatColor.WHITE+"Zombie Leader");
				}
				return true;
			}
		} else
		if (ylv>=32) {
			//Change mobs in this range to 'Dangerous' versions. Zombies and skeletons also get armor.
			Monster m = (Monster)(ent);
			MonsterDifficulty md = MonsterDifficulty.DANGEROUS;
			convertMonster(m,md);
			return true;
		} else
		if (ylv>=16) {
			MonsterDifficulty md = MonsterDifficulty.DEADLY;
			//Change mobs in this range to 'Dangerous' versions. Zombies and skeletons also get armor.
			Monster m = (Monster)(ent);
			convertMonster(m,md);
			return true;
		} else
		{
			//Change mobs in this range to 'Dangerous' versions. Zombies and skeletons also get armor.
			MonsterDifficulty md = MonsterDifficulty.HELLFIRE;
			Monster m = (Monster)(ent);
			convertMonster(m,md);
			return true;
		}
	}
	private static void RandomizeEquipment(Monster m, int lv) {
		/*
		 * Lv1: Leather/Iron Armor.
		 * Lv2: Iron/Diamond Armor.
		 * Lv3: Diamond Armor.
		 */
		switch (lv) {
			case 1:{
				if (Math.random()<0.5) {
					m.getEquipment().setHelmet(new ItemStack(Material.LEATHER_HELMET));
					m.getEquipment().getHelmet().setDurability((short)Math.round(Math.random()*128));
				} else {
					m.getEquipment().setHelmet(new ItemStack(Material.IRON_HELMET));
					m.getEquipment().getHelmet().setDurability((short)Math.round(Math.random()*128));
				}
				if (Math.random()<0.5) {
					m.getEquipment().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
					m.getEquipment().getChestplate().setDurability((short)Math.round(Math.random()*128));
				} else {
					m.getEquipment().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
					m.getEquipment().getChestplate().setDurability((short)Math.round(Math.random()*128));
				}
				if (Math.random()<0.5) {
					m.getEquipment().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
					m.getEquipment().getLeggings().setDurability((short)Math.round(Math.random()*128));
				} else {
					m.getEquipment().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
					m.getEquipment().getLeggings().setDurability((short)Math.round(Math.random()*128));
				}
				if (Math.random()<0.5) {
					m.getEquipment().setBoots(new ItemStack(Material.LEATHER_BOOTS));
					m.getEquipment().getBoots().setDurability((short)Math.round(Math.random()*128));
				} else {
					m.getEquipment().setBoots(new ItemStack(Material.IRON_BOOTS));
					m.getEquipment().getBoots().setDurability((short)Math.round(Math.random()*128));
				}
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
							weapon = new ItemStack(Material.STONE_AXE);
						}
						m.getEquipment().setItemInMainHand(weapon);
					} else {
						if (Math.random()<0.5) {
							weapon = new ItemStack(Material.WOOD_SWORD);
						} else {
							weapon = new ItemStack(Material.STONE_SWORD);
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
					m.getEquipment().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
					m.getEquipment().getHelmet().setDurability((short)Math.round(Math.random()*128));
				} else {
					m.getEquipment().setHelmet(new ItemStack(Material.IRON_HELMET));
					m.getEquipment().getHelmet().setDurability((short)Math.round(Math.random()*128));
				}
				if (Math.random()<0.5) {
					m.getEquipment().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
					m.getEquipment().getChestplate().setDurability((short)Math.round(Math.random()*128));
				} else {
					m.getEquipment().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
					m.getEquipment().getChestplate().setDurability((short)Math.round(Math.random()*128));
				}
				if (Math.random()<0.5) {
					m.getEquipment().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
					m.getEquipment().getLeggings().setDurability((short)Math.round(Math.random()*128));
				} else {
					m.getEquipment().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
					m.getEquipment().getLeggings().setDurability((short)Math.round(Math.random()*128));
				}
				if (Math.random()<0.5) {
					m.getEquipment().setBoots(new ItemStack(Material.DIAMOND_BOOTS));
					m.getEquipment().getBoots().setDurability((short)Math.round(Math.random()*128));
				} else {
					m.getEquipment().setBoots(new ItemStack(Material.IRON_BOOTS));
					m.getEquipment().getBoots().setDurability((short)Math.round(Math.random()*128));
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
						if (Math.random()<0.4) {
							weapon = new ItemStack(Material.STONE_AXE);
						} if (Math.random()<0.4) {
							weapon = new ItemStack(Material.IRON_AXE);
						} else {
							weapon = new ItemStack(Material.DIAMOND_AXE);
						}
						m.getEquipment().setItemInMainHand(weapon);
					} else {
						if (Math.random()<0.4) {
							weapon = new ItemStack(Material.STONE_SWORD);
						} if (Math.random()<0.4) {
							weapon = new ItemStack(Material.IRON_SWORD);
						} else {
							weapon = new ItemStack(Material.DIAMOND_SWORD);
						}
						m.getEquipment().setItemInMainHand(weapon);
					}
					if (Math.random()<0.2) {
						m.getEquipment().setItemInOffHand(new ItemStack(Material.SHIELD));
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
			case 3:{
				if (Math.random()<0.5) {
					m.getEquipment().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
					m.getEquipment().getHelmet().setDurability((short)Math.round(Math.random()*128));
				} else {
					m.getEquipment().setHelmet(new ItemStack(Material.GOLD_HELMET));
					m.getEquipment().getHelmet().setDurability((short)Math.round(Math.random()*128));
				}
				if (Math.random()<0.5) {
					m.getEquipment().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
					m.getEquipment().getChestplate().setDurability((short)Math.round(Math.random()*128));
				} else {
					m.getEquipment().setChestplate(new ItemStack(Material.GOLD_CHESTPLATE));
					m.getEquipment().getChestplate().setDurability((short)Math.round(Math.random()*128));
				}
				if (Math.random()<0.5) {
					m.getEquipment().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
					m.getEquipment().getLeggings().setDurability((short)Math.round(Math.random()*128));
				} else {
					m.getEquipment().setLeggings(new ItemStack(Material.GOLD_LEGGINGS));
					m.getEquipment().getLeggings().setDurability((short)Math.round(Math.random()*128));
				}
				if (Math.random()<0.5) {
					m.getEquipment().setBoots(new ItemStack(Material.DIAMOND_BOOTS));
					m.getEquipment().getBoots().setDurability((short)Math.round(Math.random()*128));
				} else {
					m.getEquipment().setBoots(new ItemStack(Material.GOLD_BOOTS));
					m.getEquipment().getBoots().setDurability((short)Math.round(Math.random()*128));
				}

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
						if (Math.random()<0.6) {
							weapon = new ItemStack(Material.DIAMOND_AXE);
						} else {
							weapon = new ItemStack(Material.GOLD_AXE);
						}
						m.getEquipment().setItemInMainHand(weapon);
					} else {
						if (Math.random()<0.6) {
							weapon = new ItemStack(Material.DIAMOND_SWORD);
						} else {
							weapon = new ItemStack(Material.GOLD_SWORD);
						}
						m.getEquipment().setItemInMainHand(weapon);
					}
					if (Math.random()<0.5) {
						m.getEquipment().setItemInOffHand(new ItemStack(Material.SHIELD));
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
			default:{
				if (Math.random()<0.1) {
					if (Math.random()<0.5) {
						m.getEquipment().setHelmet(new ItemStack(Material.LEATHER_HELMET));
						m.getEquipment().getHelmet().setDurability((short)Math.round(Math.random()*128));
						m.getEquipment().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
						m.getEquipment().getChestplate().setDurability((short)Math.round(Math.random()*128));
						m.getEquipment().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
						m.getEquipment().getLeggings().setDurability((short)Math.round(Math.random()*128));
						m.getEquipment().setBoots(new ItemStack(Material.LEATHER_BOOTS));
						m.getEquipment().getBoots().setDurability((short)Math.round(Math.random()*128));
					} else {
						m.getEquipment().setHelmet(new ItemStack(Material.IRON_HELMET));
						m.getEquipment().getHelmet().setDurability((short)Math.round(Math.random()*128));
						m.getEquipment().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
						m.getEquipment().getChestplate().setDurability((short)Math.round(Math.random()*128));
						m.getEquipment().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
						m.getEquipment().getLeggings().setDurability((short)Math.round(Math.random()*128));
						m.getEquipment().setBoots(new ItemStack(Material.IRON_BOOTS));
						m.getEquipment().getBoots().setDurability((short)Math.round(Math.random()*128));
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
						if (Math.random()<0.5) {
							weapon = new ItemStack(Material.WOOD_AXE);
						} else {
							weapon = new ItemStack(Material.STONE_AXE);
						}
						m.getEquipment().setItemInMainHand(weapon);
					} else {
						if (Math.random()<0.5) {
							weapon = new ItemStack(Material.WOOD_SWORD);
						} else {
							weapon = new ItemStack(Material.STONE_SWORD);
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
	

	private static ItemStack RandomizeEnchantments(ItemStack item) {
		//Have a small chance to randomize every enchant with a random value too.
		return RandomizeEnchantments(item, ItemRarity.VANILLA);
	}
	private static ItemStack RandomizeEnchantments(ItemStack item, ItemRarity rarity) {
		//Have a small chance to randomize every enchant with a random value too.
		switch (rarity) {
			case RARE:
			{
				if (Math.random()<=0.08) {item.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, (int)(Math.random()*4)+2);}
				if (Math.random()<=0.08) {item.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, (int)(Math.random()*4)+2);}
				if (Math.random()<=0.08) {item.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, (int)(Math.random()*4)+2);}
				if (Math.random()<=0.08) {item.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, (int)(Math.random()*4)+2);}
				if (Math.random()<=0.08) {item.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, (int)(Math.random()*4)+2);}
				if (Math.random()<=0.08) {item.addUnsafeEnchantment(Enchantment.OXYGEN, (int)(Math.random()*2)+2);}
				if (Math.random()<=0.08) {item.addUnsafeEnchantment(Enchantment.WATER_WORKER, (int)(Math.random()*1)+1);}
				if (Math.random()<=0.08) {item.addUnsafeEnchantment(Enchantment.THORNS, (int)(Math.random()*2)+2);}
				if (Math.random()<=0.08) {item.addUnsafeEnchantment(Enchantment.DEPTH_STRIDER, (int)(Math.random()*2)+2);}
				if (Math.random()<=0.08) {item.addUnsafeEnchantment(Enchantment.FROST_WALKER, (int)(Math.random()*1)+2);}
				if (Math.random()<=0.08) {item.addUnsafeEnchantment(Enchantment.DURABILITY, (int)(Math.random()*2)+2);}
				if (Math.random()<=0.08) {item.addUnsafeEnchantment(Enchantment.MENDING, (int)(Math.random()*1)+1);}
			}break;
			case EPIC:
			{
				if (Math.random()<=0.12) {item.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, (int)(Math.random()*7)+2);}
				if (Math.random()<=0.12) {item.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, (int)(Math.random()*7)+2);}
				if (Math.random()<=0.12) {item.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, (int)(Math.random()*7)+2);}
				if (Math.random()<=0.12) {item.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, (int)(Math.random()*7)+2);}
				if (Math.random()<=0.12) {item.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, (int)(Math.random()*7)+2);}
				if (Math.random()<=0.12) {item.addUnsafeEnchantment(Enchantment.OXYGEN, (int)(Math.random()*7)+2);}
				if (Math.random()<=0.12) {item.addUnsafeEnchantment(Enchantment.WATER_WORKER, (int)(Math.random()*7)+2);}
				if (Math.random()<=0.12) {item.addUnsafeEnchantment(Enchantment.THORNS, (int)(Math.random()*7)+2);}
				if (Math.random()<=0.12) {item.addUnsafeEnchantment(Enchantment.DEPTH_STRIDER, (int)(Math.random()*7)+2);}
				if (Math.random()<=0.12) {item.addUnsafeEnchantment(Enchantment.FROST_WALKER, (int)(Math.random()*7)+2);}
				if (Math.random()<=0.12) {item.addUnsafeEnchantment(Enchantment.DURABILITY, (int)(Math.random()*7)+2);}
				if (Math.random()<=0.12) {item.addUnsafeEnchantment(Enchantment.MENDING, (int)(Math.random()*1)+1);}
			}break;
			case LEGENDARY:
			{
				if (Math.random()<=0.16) {item.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, (int)(Math.random()*6)+5);}
				if (Math.random()<=0.16) {item.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, (int)(Math.random()*6)+5);}
				if (Math.random()<=0.16) {item.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, (int)(Math.random()*6)+5);}
				if (Math.random()<=0.16) {item.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, (int)(Math.random()*6)+5);}
				if (Math.random()<=0.16) {item.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, (int)(Math.random()*6)+5);}
				if (Math.random()<=0.16) {item.addUnsafeEnchantment(Enchantment.OXYGEN, (int)(Math.random()*6)+5);}
				if (Math.random()<=0.16) {item.addUnsafeEnchantment(Enchantment.WATER_WORKER, (int)(Math.random()*6)+5);}
				if (Math.random()<=0.16) {item.addUnsafeEnchantment(Enchantment.THORNS, (int)(Math.random()*6)+5);}
				if (Math.random()<=0.16) {item.addUnsafeEnchantment(Enchantment.DEPTH_STRIDER, (int)(Math.random()*6)+5);}
				if (Math.random()<=0.16) {item.addUnsafeEnchantment(Enchantment.FROST_WALKER, (int)(Math.random()*6)+5);}
				if (Math.random()<=0.16) {item.addUnsafeEnchantment(Enchantment.DURABILITY, (int)(Math.random()*6)+5);}
				if (Math.random()<=0.16) {item.addUnsafeEnchantment(Enchantment.MENDING, (int)(Math.random()*1)+1);}
			}break;
			default: //Vanilla.
			{
				if (Math.random()<=0.08) {item.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, (int)(Math.random()*5)+1);}
				if (Math.random()<=0.08) {item.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, (int)(Math.random()*5)+1);}
				if (Math.random()<=0.08) {item.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, (int)(Math.random()*5)+1);}
				if (Math.random()<=0.08) {item.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, (int)(Math.random()*5)+1);}
				if (Math.random()<=0.08) {item.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, (int)(Math.random()*5)+1);}
				if (Math.random()<=0.08) {item.addUnsafeEnchantment(Enchantment.OXYGEN, (int)(Math.random()*3)+1);}
				if (Math.random()<=0.08) {item.addUnsafeEnchantment(Enchantment.WATER_WORKER, (int)(Math.random()*1)+1);}
				if (Math.random()<=0.08) {item.addUnsafeEnchantment(Enchantment.THORNS, (int)(Math.random()*3)+1);}
				if (Math.random()<=0.08) {item.addUnsafeEnchantment(Enchantment.DEPTH_STRIDER, (int)(Math.random()*3)+1);}
				if (Math.random()<=0.08) {item.addUnsafeEnchantment(Enchantment.FROST_WALKER, (int)(Math.random()*2)+1);}
				if (Math.random()<=0.08) {item.addUnsafeEnchantment(Enchantment.DURABILITY, (int)(Math.random()*3)+1);}
				if (Math.random()<=0.08) {item.addUnsafeEnchantment(Enchantment.MENDING, (int)(Math.random()*1)+1);}
			}break;
		}
		return item;
	}
	
	public static boolean isZombieLeader(LivingEntity ent) {
		if (ent instanceof Zombie) {
			MonsterDifficulty md = getMonsterDifficulty((Monster)ent);
			if
					(
							(md==MonsterDifficulty.NORMAL && ent.getMaxHealth()>20) ||
							(md==MonsterDifficulty.DANGEROUS && ent.getMaxHealth()>20*2) ||
							(md==MonsterDifficulty.DEADLY && ent.getMaxHealth()>20*2) ||
							(md==MonsterDifficulty.HELLFIRE && ent.getMaxHealth()>20*4)
					)
				 {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	} 

	public static Monster convertMonster(Monster m) {
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
	
	public static MonsterDifficulty getMonsterDifficulty(Monster m) {
		if (m.getCustomName()!=null) {
			if (m.getCustomName().contains("Dangerous")) {
				return MonsterDifficulty.DANGEROUS;
			} else
			if (m.getCustomName().contains("Deadly")) {
				return MonsterDifficulty.DEADLY;
			} else
			if (m.getCustomName().contains("Hellfire")) {
				return MonsterDifficulty.HELLFIRE;
			} else
			{
				return MonsterDifficulty.NORMAL;
			}
		} else {
			return MonsterDifficulty.NORMAL;
		}
	}
	
	public static Monster convertMonster(Monster m, MonsterDifficulty md) {
		switch (md) {
			case DANGEROUS: {
				String MonsterName = m.getType().toString().toLowerCase();
				m.setCustomName(ChatColor.DARK_AQUA+"Dangerous "+GenericFunctions.CapitalizeFirstLetters(MonsterName.replaceAll("_", " ")+(isZombieLeader(m)?" Leader":"")));
				m.setMaxHealth(m.getMaxHealth()*2.0);
				m.setHealth(m.getMaxHealth());
				if (isAllowedToEquipItems(m)) {
					m.getEquipment().clear();
					RandomizeEquipment(m,1);
				}
			}break;
			case DEADLY: {
				String MonsterName = m.getType().toString().toLowerCase();
				m.setCustomName(ChatColor.GOLD+"Deadly "+GenericFunctions.CapitalizeFirstLetters(MonsterName.replaceAll("_", " ")+(isZombieLeader(m)?" Leader":"")));
				m.setMaxHealth(m.getMaxHealth()*2.0);
				m.setHealth(m.getMaxHealth());
				if (isAllowedToEquipItems(m)) {
					m.getEquipment().clear();
					RandomizeEquipment(m,2);
				}
				m.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,99999,1));
			}break;
			case HELLFIRE:{
				String MonsterName = m.getType().toString().toLowerCase();
				m.setCustomName(ChatColor.DARK_RED+"Hellfire "+GenericFunctions.CapitalizeFirstLetters(MonsterName.replaceAll("_", " ")+(isZombieLeader(m)?" Leader":"")));
				//m.setCustomName(ChatColor.DARK_AQUA+"Dangerous Mob");
				//m.setCustomNameVisible(true);
				m.setMaxHealth(m.getMaxHealth()*4.0);
				m.setHealth(m.getMaxHealth());
				m.setFireTicks(999999);
				if (isAllowedToEquipItems(m)) {
					m.getEquipment().clear();
					RandomizeEquipment(m,3);
				}
				m.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,99999,1));
				m.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,99999,1));
				if (Math.random()<=0.2) {m.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,99999,1));}
			}break;
			default: {
				if (isAllowedToEquipItems(m)) {
					m.getEquipment().clear();
					RandomizeEquipment(m,0);
				}
				if (isZombieLeader(m)) {
					m.setCustomName(ChatColor.WHITE+"Zombie Leader");
				}
			}break;
		}
		return m;
	}
	
	private static boolean isAllowedToEquipItems(Monster m) {
		if (m.getType()==EntityType.ZOMBIE ||
			m.getType()==EntityType.PIG_ZOMBIE ||
			m.getType()==EntityType.SKELETON ||
			m.getType()==EntityType.GIANT) {
			return true;
		} else {
			return false;
		}
	}
	
	public static Monster spawnAdjustedMonster(MonsterType mt,Location loc) {
		EntityType et;
		switch(mt) {
			case BLAZE:
				et=EntityType.BLAZE;
				break;
			case CAVESPIDER:
				et=EntityType.CAVE_SPIDER;
				break;
			case CREEPER:
				et=EntityType.CREEPER;
				break;
			case ENDERMAN:
				et=EntityType.ENDERMAN;
				break;
			case GIANT:
				et=EntityType.GIANT;
				break;
			case GUARDIAN:
				et=EntityType.GUARDIAN;
				break;
			case PIGZOMBIE:
				et=EntityType.PIG_ZOMBIE;
				break;
			case SILVERFISH:
				et=EntityType.SILVERFISH;
				break;
			case SKELETON:
				et=EntityType.SKELETON;
				break;
			case SPIDER:
				et=EntityType.SPIDER;
				break;
			case WITCH:
				et=EntityType.WITCH;
				break;
			case WITHER:
				et=EntityType.WITHER;
				break;
			case ZOMBIE:
				et=EntityType.ZOMBIE;
				break;
			default:
				et=EntityType.ZOMBIE;
		}
		Monster m = (Monster)loc.getWorld().spawnEntity(loc, et);
		return MonsterController.convertMonster(m);
	}
}
