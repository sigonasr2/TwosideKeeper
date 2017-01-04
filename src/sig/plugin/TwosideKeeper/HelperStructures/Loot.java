package sig.plugin.TwosideKeeper.HelperStructures;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import aPlugin.DropItem;
import aPlugin.DropMaterial;
import aPlugin.API.Chests;
import sig.plugin.TwosideKeeper.Artifact;
import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.Drops.SigDrop;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;
import sig.plugin.TwosideKeeper.HolidayEvents.Christmas;

public class Loot {
	
	static double HARDENED_ENCHANT_MULT = 1.4;
	static int MAX_ENCHANT_LEVEL = 10;
	
	//NORMAL: 100% Rate. (<=1000)
	//RARE: 7.8% Rate. (<=78) 
	//LEGENDARY: 1.9% Rate. (<=19) 
	
	public static void DefineLootChests() {
		aPlugin.API.Chests.LOOT_NORMAL.addDrop(new DropMaterial(Material.STONE_SWORD,8));
		aPlugin.API.Chests.LOOT_NORMAL.addDrop(new DropItem(Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE),11));
		//aPlugin.API.Chests.LOOT_NORMAL.addDrop(new DropMaterial(Material.AIR,101));
		aPlugin.API.Chests.LOOT_NORMAL.addDrop(new DropMaterial(Material.COAL,20));
		aPlugin.API.Chests.LOOT_NORMAL.addDrop(new DropMaterial(Material.IRON_INGOT,1,17));
		aPlugin.API.Chests.LOOT_NORMAL.addDrop(new DropMaterial(Material.DIAMOND,1,18));
		aPlugin.API.Chests.LOOT_NORMAL.addDrop(new DropMaterial(Material.GOLD_NUGGET,1,17));
		aPlugin.API.Chests.LOOT_NORMAL.addDrop(new DropMaterial(Material.ENDER_PEARL,1,8));
		aPlugin.API.Chests.LOOT_NORMAL.addDrop(new DropMaterial(Material.ENDER_CHEST,4));
		aPlugin.API.Chests.LOOT_NORMAL.addDrop(new DropItem(Artifact.createArtifactItem(ArtifactItem.ARTIFACT_ESSENCE),4));
		aPlugin.API.Chests.LOOT_NORMAL.addDrop(new SigDrop(1,18,"[Normal] Mega Armor",SigDrop.NONHARDENED,SigDrop.NONSET,SigDrop.ARMOR,LivingEntityDifficulty.NORMAL));
		aPlugin.API.Chests.LOOT_NORMAL.addDrop(new SigDrop(1,19,"[Normal] Mega Set Armor",SigDrop.NONHARDENED,SigDrop.SET,SigDrop.ARMOR,LivingEntityDifficulty.NORMAL));
		aPlugin.API.Chests.LOOT_NORMAL.addDrop(new SigDrop(1,1,"[Normal] Mega Tool",SigDrop.NONHARDENED,SigDrop.NONSET,SigDrop.TOOL,LivingEntityDifficulty.NORMAL));
		aPlugin.API.Chests.LOOT_NORMAL.addDrop(new SigDrop(1,1,"[Normal] Mega Weapon",SigDrop.NONHARDENED,SigDrop.NONSET,SigDrop.WEAPON,LivingEntityDifficulty.NORMAL));
		aPlugin.API.Chests.LOOT_NORMAL.addDrop(new SigDrop(1,2,"[Normal] Mega Set Weapon",SigDrop.NONHARDENED,SigDrop.SET,SigDrop.WEAPON,LivingEntityDifficulty.NORMAL));
		if (TwosideKeeper.CHRISTMASEVENT_ACTIVATED || TwosideKeeper.CHRISTMASLINGERINGEVENT_ACTIVATED) {
			aPlugin.API.Chests.LOOT_NORMAL.addDrop(new DropItem(Christmas.getCookieItem(),1,3,10));
			aPlugin.API.Chests.LOOT_NORMAL.addDrop(new DropItem(Christmas.getSmallCandyItem(),1,3,10));
			aPlugin.API.Chests.LOOT_NORMAL.addDrop(new DropItem(Christmas.getLargeCandyItem(),10));
			aPlugin.API.Chests.LOOT_NORMAL.addDrop(new DropItem(Christmas.getSourCandyItem(),10));
			aPlugin.API.Chests.LOOT_NORMAL.addDrop(new DropItem(Christmas.getMysteryFlavorLollipopItem(),10));
			aPlugin.API.Chests.LOOT_NORMAL.addDrop(new DropItem(Christmas.getChristmasEventToken(),4));
			aPlugin.API.Chests.LOOT_NORMAL.addDrop(new DropItem(Christmas.getChristmasBox(),1));
		}
		/*aPlugin.API.Chests.LOOT_NORMAL.addDrop(new SigDrop(1,1,"[Normal] Hardened Mega Armor",SigDrop.HARDENED,SigDrop.NONSET,SigDrop.ARMOR,LivingEntityDifficulty.NORMAL));
		aPlugin.API.Chests.LOOT_NORMAL.addDrop(new SigDrop(1,2,"[Normal] Hardened Mega Set Armor",SigDrop.HARDENED,SigDrop.SET,SigDrop.ARMOR,LivingEntityDifficulty.NORMAL));
		aPlugin.API.Chests.LOOT_NORMAL.addDrop(new SigDrop(1,1,"[Normal] Hardened Mega Tool",SigDrop.HARDENED,SigDrop.NONSET,SigDrop.TOOL,MonsterDifficulty.NORMAL));
		aPlugin.API.Chests.LOOT_NORMAL.addDrop(new SigDrop(1,1,"[Normal] Hardened Mega Weapon",SigDrop.HARDENED,SigDrop.NONSET,SigDrop.WEAPON,MonsterDifficulty.NORMAL));
		aPlugin.API.Chests.LOOT_NORMAL.addDrop(new SigDrop(1,1,"[Normal] Hardened Mega Set Weapon",SigDrop.HARDENED,SigDrop.SET,SigDrop.ARMOR,MonsterDifficulty.NORMAL));*/
		
		/*//LOOT TEMPLATE. Cut Normal loot to 25% of this. Dangerous loot to 50% of this. Deadly to 75% of this. And Hellfires at 100% of this.
		aPlugin.API.Chests.LOOT_NORMAL.addDrop(new SigDrop(1,18,"Mega Armor",SigDrop.NONHARDENED,SigDrop.NONSET,SigDrop.ARMOR));
		aPlugin.API.Chests.LOOT_NORMAL.addDrop(new SigDrop(1,78,"Mega Set Armor",SigDrop.NONHARDENED,SigDrop.SET,SigDrop.ARMOR));
		aPlugin.API.Chests.LOOT_NORMAL.addDrop(new SigDrop(1,2,"Mega Tool",SigDrop.NONHARDENED,SigDrop.NONSET,SigDrop.TOOL));
		aPlugin.API.Chests.LOOT_NORMAL.addDrop(new SigDrop(1,2,"Mega Weapon",SigDrop.NONHARDENED,SigDrop.NONSET,SigDrop.WEAPON));
		aPlugin.API.Chests.LOOT_NORMAL.addDrop(new SigDrop(1,6,"Mega Set Weapon",SigDrop.NONHARDENED,SigDrop.SET,SigDrop.WEAPON));
		aPlugin.API.Chests.LOOT_NORMAL.addDrop(new SigDrop(1,1,"Hardened Mega Armor",SigDrop.HARDENED,SigDrop.NONSET,SigDrop.ARMOR));
		aPlugin.API.Chests.LOOT_NORMAL.addDrop(new SigDrop(1,8,"Hardened Mega Set Armor",SigDrop.HARDENED,SigDrop.SET,SigDrop.ARMOR));
		aPlugin.API.Chests.LOOT_NORMAL.addDrop(new SigDrop(1,1,"Hardened Mega Tool",SigDrop.HARDENED,SigDrop.NONSET,SigDrop.TOOL));
		aPlugin.API.Chests.LOOT_NORMAL.addDrop(new SigDrop(1,1,"Hardened Mega Weapon",SigDrop.HARDENED,SigDrop.NONSET,SigDrop.WEAPON));
		aPlugin.API.Chests.LOOT_NORMAL.addDrop(new SigDrop(1,1,"Hardened Mega Set Weapon",SigDrop.HARDENED,SigDrop.SET,SigDrop.ARMOR));
		*/
		/*
		aPlugin.API.Chests.LOOT_NORMAL.addDrop(new DropMaterial(Material.IRON_INGOT,78));
		aPlugin.API.Chests.LOOT_NORMAL.addDrop(new DropMaterial(Material.DIAMOND,78));
		aPlugin.API.Chests.LOOT_NORMAL.addDrop(new DropMaterial(Material.GOLD_NUGGET,78));
		aPlugin.API.Chests.LOOT_NORMAL.addDrop(new DropMaterial(Material.SKULL_ITEM,8));
		aPlugin.API.Chests.LOOT_NORMAL.addDrop(new DropMaterial(Material.SKULL_ITEM,8));
		aPlugin.API.Chests.LOOT_NORMAL.addDrop(new DropMaterial(Material.SKULL_ITEM,8));
		aPlugin.API.Chests.LOOT_NORMAL.addDrop(new DropMaterial(Material.SKULL_ITEM,8));*/
		aPlugin.API.Chests.LOOT_NORMAL.printDrops();
		
		aPlugin.API.Chests.LOOT_DANGEROUS.addDrop(new DropMaterial(Material.IRON_INGOT,91800));
		aPlugin.API.Chests.LOOT_DANGEROUS.addDrop(new DropMaterial(Material.IRON_BLOCK,7800));
		aPlugin.API.Chests.LOOT_DANGEROUS.addDrop(new SigDrop(1,1800,"[Dangerous] Mega Armor",SigDrop.NONHARDENED,SigDrop.NONSET,SigDrop.ARMOR,LivingEntityDifficulty.DANGEROUS));
		aPlugin.API.Chests.LOOT_DANGEROUS.addDrop(new SigDrop(1,7800,"[Dangerous] Mega Set Armor",SigDrop.NONHARDENED,SigDrop.SET,SigDrop.ARMOR,LivingEntityDifficulty.DANGEROUS));
		aPlugin.API.Chests.LOOT_DANGEROUS.addDrop(new SigDrop(1,200,"[Dangerous] Mega Tool",SigDrop.NONHARDENED,SigDrop.NONSET,SigDrop.TOOL,LivingEntityDifficulty.DANGEROUS));
		aPlugin.API.Chests.LOOT_DANGEROUS.addDrop(new SigDrop(1,200,"[Dangerous] Mega Weapon",SigDrop.NONHARDENED,SigDrop.NONSET,SigDrop.WEAPON,LivingEntityDifficulty.DANGEROUS));
		aPlugin.API.Chests.LOOT_DANGEROUS.addDrop(new SigDrop(1,600,"[Dangerous] Mega Set Weapon",SigDrop.NONHARDENED,SigDrop.SET,SigDrop.WEAPON,LivingEntityDifficulty.DANGEROUS));
		aPlugin.API.Chests.LOOT_DANGEROUS.addDrop(new SigDrop(1,180,"[Dangerous] Hardened Mega Armor",SigDrop.HARDENED,SigDrop.NONSET,SigDrop.ARMOR,LivingEntityDifficulty.DANGEROUS));
		aPlugin.API.Chests.LOOT_DANGEROUS.addDrop(new SigDrop(1,780,"[Dangerous] Hardened Mega Set Armor",SigDrop.HARDENED,SigDrop.SET,SigDrop.ARMOR,LivingEntityDifficulty.DANGEROUS));
		aPlugin.API.Chests.LOOT_DANGEROUS.addDrop(new SigDrop(1,20,"[Dangerous] Hardened Mega Tool",SigDrop.HARDENED,SigDrop.NONSET,SigDrop.TOOL,LivingEntityDifficulty.DANGEROUS));
		aPlugin.API.Chests.LOOT_DANGEROUS.addDrop(new SigDrop(1,20,"[Dangerous] Hardened Mega Weapon",SigDrop.HARDENED,SigDrop.NONSET,SigDrop.WEAPON,LivingEntityDifficulty.DANGEROUS));
		aPlugin.API.Chests.LOOT_DANGEROUS.addDrop(new SigDrop(1,60,"[Dangerous] Hardened Mega Set Weapon",SigDrop.HARDENED,SigDrop.SET,SigDrop.ARMOR,LivingEntityDifficulty.DANGEROUS));
		aPlugin.API.Chests.LOOT_DANGEROUS.addDrop(new DropItem(Artifact.createArtifactItem(ArtifactItem.ANCIENT_ESSENCE),400));
		if (TwosideKeeper.CHRISTMASEVENT_ACTIVATED || TwosideKeeper.CHRISTMASLINGERINGEVENT_ACTIVATED) {
			aPlugin.API.Chests.LOOT_DANGEROUS.addDrop(new DropItem(Christmas.getCookieItem(),1,3,46600));
			aPlugin.API.Chests.LOOT_DANGEROUS.addDrop(new DropItem(Christmas.getSmallCandyItem(),1,3,46600));
			aPlugin.API.Chests.LOOT_DANGEROUS.addDrop(new DropItem(Christmas.getLargeCandyItem(),46600));
			aPlugin.API.Chests.LOOT_DANGEROUS.addDrop(new DropItem(Christmas.getSourCandyItem(),1,3,46600));
			aPlugin.API.Chests.LOOT_DANGEROUS.addDrop(new DropItem(Christmas.getMysteryFlavorLollipopItem(),46600));
			aPlugin.API.Chests.LOOT_DANGEROUS.addDrop(new DropItem(Christmas.getHolidayRageCandyBarItem(),4900));
			aPlugin.API.Chests.LOOT_DANGEROUS.addDrop(new DropItem(Christmas.getSweetCandyItem(),4900));
			aPlugin.API.Chests.LOOT_DANGEROUS.addDrop(new DropItem(Christmas.getChristmasEventToken(),400));
			aPlugin.API.Chests.LOOT_DANGEROUS.addDrop(new DropItem(Christmas.getChristmasBox(),100));
		}
		aPlugin.API.Chests.LOOT_DANGEROUS.printDrops();

		aPlugin.API.Chests.LOOT_DEADLY.addDrop(new DropMaterial(Material.IRON_INGOT,1,2,59800));
		aPlugin.API.Chests.LOOT_DEADLY.addDrop(new DropMaterial(Material.DIAMOND,7800));
		aPlugin.API.Chests.LOOT_DEADLY.addDrop(new DropMaterial(Material.GOLD_NUGGET,1,3,7800));
		aPlugin.API.Chests.LOOT_DEADLY.addDrop(new DropMaterial(Material.IRON_BLOCK,1,2,7800));
		aPlugin.API.Chests.LOOT_DEADLY.addDrop(new DropMaterial(Material.DIAMOND_BLOCK,7800));
		aPlugin.API.Chests.LOOT_DEADLY.addDrop(new DropMaterial(Material.GOLD_INGOT,7800));
		aPlugin.API.Chests.LOOT_DEADLY.addDrop(new DropMaterial(Material.DIAMOND_SWORD,800));
		aPlugin.API.Chests.LOOT_DEADLY.addDrop(new SigDrop(1,1800,"[Deadly] Mega Armor",SigDrop.NONHARDENED,SigDrop.NONSET,SigDrop.ARMOR,LivingEntityDifficulty.DEADLY));
		aPlugin.API.Chests.LOOT_DEADLY.addDrop(new SigDrop(1,7800,"[Deadly] Mega Set Armor",SigDrop.NONHARDENED,SigDrop.SET,SigDrop.ARMOR,LivingEntityDifficulty.DEADLY));
		aPlugin.API.Chests.LOOT_DEADLY.addDrop(new SigDrop(1,200,"[Deadly] Mega Tool",SigDrop.NONHARDENED,SigDrop.NONSET,SigDrop.TOOL,LivingEntityDifficulty.DEADLY));
		aPlugin.API.Chests.LOOT_DEADLY.addDrop(new SigDrop(1,200,"[Deadly] Mega Weapon",SigDrop.NONHARDENED,SigDrop.NONSET,SigDrop.WEAPON,LivingEntityDifficulty.DEADLY));
		aPlugin.API.Chests.LOOT_DEADLY.addDrop(new SigDrop(1,600,"[Deadly] Mega Set Weapon",SigDrop.NONHARDENED,SigDrop.SET,SigDrop.WEAPON,LivingEntityDifficulty.DEADLY));
		aPlugin.API.Chests.LOOT_DEADLY.addDrop(new SigDrop(1,360,"[Deadly] Hardened Mega Armor",SigDrop.HARDENED,SigDrop.NONSET,SigDrop.ARMOR,LivingEntityDifficulty.DEADLY));
		aPlugin.API.Chests.LOOT_DEADLY.addDrop(new SigDrop(1,1560,"[Deadly] Hardened Mega Set Armor",SigDrop.HARDENED,SigDrop.SET,SigDrop.ARMOR,LivingEntityDifficulty.DEADLY));
		aPlugin.API.Chests.LOOT_DEADLY.addDrop(new SigDrop(1,40,"[Deadly] Hardened Mega Tool",SigDrop.HARDENED,SigDrop.NONSET,SigDrop.TOOL,LivingEntityDifficulty.DEADLY));
		aPlugin.API.Chests.LOOT_DEADLY.addDrop(new SigDrop(1,40,"[Deadly] Hardened Mega Weapon",SigDrop.HARDENED,SigDrop.NONSET,SigDrop.WEAPON,LivingEntityDifficulty.DEADLY));
		aPlugin.API.Chests.LOOT_DEADLY.addDrop(new SigDrop(1,120,"[Deadly] Hardened Mega Set Weapon",SigDrop.HARDENED,SigDrop.SET,SigDrop.ARMOR,LivingEntityDifficulty.DEADLY));
		aPlugin.API.Chests.LOOT_DEADLY.addDrop(new DropItem(Artifact.createArtifactItem(ArtifactItem.LOST_ESSENCE),400));
		if (TwosideKeeper.CHRISTMASEVENT_ACTIVATED || TwosideKeeper.CHRISTMASLINGERINGEVENT_ACTIVATED) {
			aPlugin.API.Chests.LOOT_DEADLY.addDrop(new DropItem(Christmas.getHolidayRageCandyBarItem(),3900));
			aPlugin.API.Chests.LOOT_DEADLY.addDrop(new DropItem(Christmas.getSweetCandyItem(),1,3,3900));
			aPlugin.API.Chests.LOOT_DEADLY.addDrop(new DropItem(Christmas.getChristmasEventToken(),400));
			aPlugin.API.Chests.LOOT_DEADLY.addDrop(new DropItem(Christmas.getChristmasBox(),100));
		}
		aPlugin.API.Chests.LOOT_DEADLY.printDrops(); 
		
		aPlugin.API.Chests.LOOT_HELLFIRE.addDrop(new DropMaterial(Material.EMERALD,1,3,60200));
		aPlugin.API.Chests.LOOT_HELLFIRE.addDrop(new DropMaterial(Material.DIAMOND,1,3,7800));
		aPlugin.API.Chests.LOOT_HELLFIRE.addDrop(new DropMaterial(Material.GOLD_INGOT,1,3,7800));
		aPlugin.API.Chests.LOOT_HELLFIRE.addDrop(new DropMaterial(Material.EMERALD_BLOCK,7800));
		aPlugin.API.Chests.LOOT_HELLFIRE.addDrop(new DropMaterial(Material.DIAMOND_BLOCK,1,2,7800));
		aPlugin.API.Chests.LOOT_HELLFIRE.addDrop(new DropMaterial(Material.GOLD_BLOCK,7800));
		aPlugin.API.Chests.LOOT_HELLFIRE.addDrop(new SigDrop(1,1800,"[Hellfire] Mega Armor",SigDrop.NONHARDENED,SigDrop.NONSET,SigDrop.ARMOR,LivingEntityDifficulty.HELLFIRE));
		aPlugin.API.Chests.LOOT_HELLFIRE.addDrop(new SigDrop(1,7800,"[Hellfire] Mega Set Armor",SigDrop.NONHARDENED,SigDrop.SET,SigDrop.ARMOR,LivingEntityDifficulty.HELLFIRE));
		aPlugin.API.Chests.LOOT_HELLFIRE.addDrop(new SigDrop(1,200,"[Hellfire] Mega Tool",SigDrop.NONHARDENED,SigDrop.NONSET,SigDrop.TOOL,LivingEntityDifficulty.HELLFIRE));
		aPlugin.API.Chests.LOOT_HELLFIRE.addDrop(new SigDrop(1,200,"[Hellfire] Mega Weapon",SigDrop.NONHARDENED,SigDrop.NONSET,SigDrop.WEAPON,LivingEntityDifficulty.HELLFIRE));
		aPlugin.API.Chests.LOOT_HELLFIRE.addDrop(new SigDrop(1,600,"[Hellfire] Mega Set Weapon",SigDrop.NONHARDENED,SigDrop.SET,SigDrop.WEAPON,LivingEntityDifficulty.HELLFIRE));
		aPlugin.API.Chests.LOOT_HELLFIRE.addDrop(new SigDrop(1,540,"[Hellfire] Hardened Mega Armor",SigDrop.HARDENED,SigDrop.NONSET,SigDrop.ARMOR,LivingEntityDifficulty.HELLFIRE));
		aPlugin.API.Chests.LOOT_HELLFIRE.addDrop(new SigDrop(1,2340,"[Hellfire] Hardened Mega Set Armor",SigDrop.HARDENED,SigDrop.SET,SigDrop.ARMOR,LivingEntityDifficulty.HELLFIRE));
		aPlugin.API.Chests.LOOT_HELLFIRE.addDrop(new SigDrop(1,60,"[Hellfire] Hardened Mega Tool",SigDrop.HARDENED,SigDrop.NONSET,SigDrop.TOOL,LivingEntityDifficulty.HELLFIRE));
		aPlugin.API.Chests.LOOT_HELLFIRE.addDrop(new SigDrop(1,60,"[Hellfire] Hardened Mega Weapon",SigDrop.HARDENED,SigDrop.NONSET,SigDrop.WEAPON,LivingEntityDifficulty.HELLFIRE));
		aPlugin.API.Chests.LOOT_HELLFIRE.addDrop(new SigDrop(1,180,"[Hellfire] Hardened Mega Set Weapon",SigDrop.HARDENED,SigDrop.SET,SigDrop.ARMOR,LivingEntityDifficulty.HELLFIRE));
		aPlugin.API.Chests.LOOT_HELLFIRE.addDrop(new DropItem(Artifact.createArtifactItem(ArtifactItem.DIVINE_ESSENCE),400));
		aPlugin.API.Chests.LOOT_HELLFIRE.addDrop(new DropItem(TwosideKeeper.HUNTERS_COMPASS.getItemStack(),400));
		if (TwosideKeeper.CHRISTMASEVENT_ACTIVATED || TwosideKeeper.CHRISTMASLINGERINGEVENT_ACTIVATED) {
			aPlugin.API.Chests.LOOT_HELLFIRE.addDrop(new DropItem(Christmas.getHolidayRageCandyBarItem(),1,3,3900));
			aPlugin.API.Chests.LOOT_HELLFIRE.addDrop(new DropItem(Christmas.getSweetCandyItem(),1,3,3900));
			aPlugin.API.Chests.LOOT_HELLFIRE.addDrop(new DropItem(Christmas.getChristmasEventToken(),400));
			aPlugin.API.Chests.LOOT_HELLFIRE.addDrop(new DropItem(Christmas.getChristmasBox(),100));
		} 
		aPlugin.API.Chests.LOOT_HELLFIRE.printDrops();
		
		aPlugin.API.Chests.LOOT_CUSTOM.addDrop(new SigDrop(1,1800,"[End] Mega Armor",SigDrop.NONHARDENED,SigDrop.NONSET,SigDrop.ARMOR,LivingEntityDifficulty.END));
		aPlugin.API.Chests.LOOT_CUSTOM.addDrop(new SigDrop(1,7800,"[End] Mega Set Armor",SigDrop.NONHARDENED,SigDrop.SET,SigDrop.ARMOR,LivingEntityDifficulty.END));
		aPlugin.API.Chests.LOOT_CUSTOM.addDrop(new SigDrop(1,200,"[End] Mega Tool",SigDrop.NONHARDENED,SigDrop.NONSET,SigDrop.TOOL,LivingEntityDifficulty.END));
		aPlugin.API.Chests.LOOT_CUSTOM.addDrop(new SigDrop(1,200,"[End] Mega Weapon",SigDrop.NONHARDENED,SigDrop.NONSET,SigDrop.WEAPON,LivingEntityDifficulty.END));
		aPlugin.API.Chests.LOOT_CUSTOM.addDrop(new SigDrop(1,600,"[End] Mega Set Weapon",SigDrop.NONHARDENED,SigDrop.SET,SigDrop.WEAPON,LivingEntityDifficulty.END));
		aPlugin.API.Chests.LOOT_CUSTOM.addDrop(new SigDrop(1,540,"[End] Hardened Mega Armor",SigDrop.HARDENED,SigDrop.NONSET,SigDrop.ARMOR,LivingEntityDifficulty.END));
		aPlugin.API.Chests.LOOT_CUSTOM.addDrop(new SigDrop(1,2340,"[End] Hardened Mega Set Armor",SigDrop.HARDENED,SigDrop.SET,SigDrop.ARMOR,LivingEntityDifficulty.END));
		aPlugin.API.Chests.LOOT_CUSTOM.addDrop(new SigDrop(1,60,"[End] Hardened Mega Tool",SigDrop.HARDENED,SigDrop.NONSET,SigDrop.TOOL,LivingEntityDifficulty.END));
		aPlugin.API.Chests.LOOT_CUSTOM.addDrop(new SigDrop(1,60,"[End] Hardened Mega Weapon",SigDrop.HARDENED,SigDrop.NONSET,SigDrop.WEAPON,LivingEntityDifficulty.END));
		aPlugin.API.Chests.LOOT_CUSTOM.addDrop(new SigDrop(1,180,"[End] Hardened Mega Set Weapon",SigDrop.HARDENED,SigDrop.SET,SigDrop.ARMOR,LivingEntityDifficulty.END));
		aPlugin.API.Chests.LOOT_CUSTOM.addDrop(new DropItem(Artifact.createArtifactItem(ArtifactItem.DIVINE_ESSENCE),400));
		if (TwosideKeeper.CHRISTMASEVENT_ACTIVATED || TwosideKeeper.CHRISTMASLINGERINGEVENT_ACTIVATED) {
			aPlugin.API.Chests.LOOT_CUSTOM.addDrop(new DropItem(Christmas.getHolidayRageCandyBarItem(),1,3,1400));
			aPlugin.API.Chests.LOOT_CUSTOM.addDrop(new DropItem(Christmas.getSweetCandyItem(),1,3,1400));
			aPlugin.API.Chests.LOOT_CUSTOM.addDrop(new DropItem(Christmas.getChristmasEventToken(),200));
			aPlugin.API.Chests.LOOT_CUSTOM.addDrop(new DropItem(Christmas.getChristmasBox(),50));
		}
		aPlugin.API.Chests.LOOT_CUSTOM.printDrops();

		aPlugin.API.Chests.LOOT_CUSTOM_5.setName(ChatColor.RED+"Leader Wither Loot Box");
		aPlugin.API.Chests.LOOT_CUSTOM_5.addDrop(new SigDrop(1,1000,"[Leader Wither] Armor",SigDrop.NONHARDENED,SigDrop.SET,SigDrop.ARMOR,LivingEntityDifficulty.DANGEROUS));
		aPlugin.API.Chests.LOOT_CUSTOM_5.addDrop(new SigDrop(1,1000,"[Leader Wither] Armor",SigDrop.NONHARDENED,SigDrop.SET,SigDrop.ARMOR,LivingEntityDifficulty.DEADLY));
		aPlugin.API.Chests.LOOT_CUSTOM_5.addDrop(new DropMaterial(Material.NETHER_STAR,70));
		aPlugin.API.Chests.LOOT_CUSTOM_5.addDrop(new SigDrop(1,70,"[Leader Wither] Hardened Armor",SigDrop.HARDENED,SigDrop.SET,SigDrop.ARMOR,LivingEntityDifficulty.DANGEROUS));
		aPlugin.API.Chests.LOOT_CUSTOM_5.addDrop(new SigDrop(1,70,"[Leader Wither] Hardened Armor",SigDrop.HARDENED,SigDrop.SET,SigDrop.ARMOR,LivingEntityDifficulty.DEADLY));
		aPlugin.API.Chests.LOOT_CUSTOM_5.addDrop(new SigDrop(1,40,"[Leader Wither] Set Weapon",SigDrop.NONHARDENED,SigDrop.SET,SigDrop.WEAPON,LivingEntityDifficulty.DEADLY));
		aPlugin.API.Chests.LOOT_CUSTOM_5.addDrop(new SigDrop(1,40,"[Leader Wither] Tool",SigDrop.NONHARDENED,SigDrop.SET,SigDrop.TOOL,LivingEntityDifficulty.DANGEROUS));
		aPlugin.API.Chests.LOOT_CUSTOM_5.addDrop(new SigDrop(1,5,"[Leader Wither] Hardened Set Weapon",SigDrop.HARDENED,SigDrop.SET,SigDrop.WEAPON,LivingEntityDifficulty.DEADLY));
		aPlugin.API.Chests.LOOT_CUSTOM_5.addDrop(new SigDrop(1,5,"[Leader Wither] Hardened Tool",SigDrop.HARDENED,SigDrop.SET,SigDrop.TOOL,LivingEntityDifficulty.DANGEROUS));
		aPlugin.API.Chests.LOOT_CUSTOM_5.addDrop(new DropItem(aPlugin.API.getChestItem(Chests.ELITE),5));
		aPlugin.API.Chests.LOOT_CUSTOM_5.addDrop(new DropItem(Christmas.getChristmasBox(),5));
		aPlugin.API.Chests.LOOT_CUSTOM_5.setProbability(0.2);
		aPlugin.API.Chests.LOOT_CUSTOM_5.printDrops();

		//aPlugin.API.Chests..addDrop(new DropItem(TwosideKeeper.HUNTERS_COMPASS.getItemStack(),10));
	}

	public static ItemStack GenerateMegaPiece(Material mat_type, boolean hardened) {
		return GenerateMegaPiece(mat_type, hardened, false);
	}

	public static ItemStack GenerateMegaPiece(Material mat_type, boolean hardened, boolean setitem) {
		return GenerateMegaPiece(mat_type,hardened,setitem,0);
	}
	
	public static ItemStack GenerateMegaPiece(Material mat_type, boolean hardened, boolean setitem, int settier) {
		return GenerateMegaPiece(mat_type,hardened,setitem,settier,null,null);
	}
	
	public static ItemStack GenerateMegaPiece(Material mat_type, boolean hardened, boolean setitem, int settier, Entity damager, Monster m) {
		TwosideKeeper.log("Calling this with "+mat_type.name()+","+hardened+","+setitem+".", 5);
		ItemStack raresword = new ItemStack(mat_type);
		ItemMeta sword_meta = raresword.getItemMeta();
		sword_meta.setDisplayName(ChatColor.AQUA+""+ChatColor.BOLD+"Mega "+GenericFunctions.UserFriendlyMaterialName(mat_type));
		List<String> fakelore = new ArrayList<String>();
		fakelore.add(" ");
		sword_meta.setLore(fakelore);
		raresword.setItemMeta(sword_meta);
		raresword = addEnchantments(raresword,false);
		if (hardened) {
			sword_meta.setDisplayName(ChatColor.LIGHT_PURPLE+""+ChatColor.BOLD+"Hardened Mega "+GenericFunctions.UserFriendlyMaterialName(mat_type));
			List<String> lore = new ArrayList<String>();
			lore.add(ChatColor.GRAY+"Breaks Remaining: "+ChatColor.YELLOW+GetHardenedBreaks(mat_type));
			sword_meta.setLore(lore);
			raresword.setItemMeta(sword_meta);
			raresword = addEnchantments(raresword,true);
		}
		/* //LEGACY CODE.
		if (setitem && (raresword.getType().toString().contains("SWORD") || GenericFunctions.isArmor(raresword))) {
			
			if (damager==null && m==null) {
				raresword = GenerateSetPiece(raresword,hardened,settier); 
			} else {
				LivingEntity shooter = CustomDamage.getDamagerEntity(damager);
				Player p = null;
				if (shooter instanceof Player) {
					p = (Player)shooter;
				} else {
					if (shooter!=null) {
						TwosideKeeper.log("Something went terribly wrong trying to give a set item! Shooter was "+shooter.toString(), 0);
					} else {
						TwosideKeeper.log("Something went terribly wrong trying to give a set item! Shooter was null.", 0);
					}
				}
				MonsterDifficulty md = MonsterController.getMonsterDifficulty(m);
				ItemSet set = null;
				if (p!=null) {
					if (PlayerMode.isStriker(p)) {
						set=ItemSet.PANROS;
					} else
					if (PlayerMode.isRanger(p)) {
						set = PickRandomRangerSet();
					} else
					if (PlayerMode.isDefender(p)) {
						set=ItemSet.SONGSTEEL;
					} else {
						//RANDOM SET! because we are not a mode of any sort.
						if (Math.random()<0.25) {
							set=ItemSet.PANROS;
						} else
						if (Math.random()<0.25) {
							set=PickRandomRangerSet();
						} else
						if (Math.random()<0.25) {
							set=ItemSet.DAWNTRACKER;
						} else {
							set=ItemSet.SONGSTEEL;
						}
					}
				}
				switch (md) {
					case NORMAL:{
						raresword = GenerateSetPiece(new ItemStack(Material.STONE_SWORD),hardened,settier+2);
					}break;
					case DANGEROUS:{
						if (Math.random()<=0.1) {
							if (PlayerMode.isStriker(p)) { //Only do this weapon for all tiers since the other weapons don't have tiers. Those require hellfires.
								raresword = GenerateSetPiece(new ItemStack(Material.IRON_SWORD),set,(Math.random()<=0.1)?true:false,settier);
							}
						} else {
							if (Math.random()<=0.25) {
								if (PlayerMode.isRanger(p)) {
									raresword = GenerateSetPiece(new ItemStack(Material.LEATHER_HELMET),set,hardened,settier);
								} else {
									raresword = GenerateSetPiece(new ItemStack(Material.IRON_HELMET),set,hardened,settier+1);
								}
							} else
							if (Math.random()<=0.25) {
								if (PlayerMode.isRanger(p)) {
									raresword = GenerateSetPiece(new ItemStack(Material.LEATHER_CHESTPLATE),set,hardened,settier);
								} else {
									raresword = GenerateSetPiece(new ItemStack(Material.IRON_CHESTPLATE),set,hardened,settier+1);
								}
							} else
							if (Math.random()<=0.25) {
								if (PlayerMode.isRanger(p)) {
									raresword = GenerateSetPiece(new ItemStack(Material.LEATHER_LEGGINGS),set,hardened,settier);
								} else {
									raresword = GenerateSetPiece(new ItemStack(Material.IRON_LEGGINGS),set,hardened,settier+1);
								}
							} else {
								if (PlayerMode.isRanger(p)) {
									raresword = GenerateSetPiece(new ItemStack(Material.LEATHER_BOOTS),set,hardened,settier);
								} else {
									raresword = GenerateSetPiece(new ItemStack(Material.IRON_BOOTS),set,hardened,settier+1);
								}
							}
						}
					}break;
					case DEADLY:{
						if (Math.random()<=0.1) {
							if (PlayerMode.isStriker(p)) { //Only do this weapon for all tiers since the other weapons don't have tiers. Those require hellfires.
								raresword = GenerateSetPiece(new ItemStack(Material.DIAMOND_SWORD),set,(Math.random()<=0.1)?true:false,settier);
							}
						} else {
							if (Math.random()<=0.25) {
								if (PlayerMode.isRanger(p)) {
									raresword = GenerateSetPiece(new ItemStack(Material.LEATHER_HELMET),set,hardened,settier);
								} else {
									raresword = GenerateSetPiece(new ItemStack(Material.DIAMOND_HELMET),set,hardened,settier);
								}
							} else
							if (Math.random()<=0.25) {
								if (PlayerMode.isRanger(p)) {
									raresword = GenerateSetPiece(new ItemStack(Material.LEATHER_CHESTPLATE),set,hardened,settier);
								} else {
									raresword = GenerateSetPiece(new ItemStack(Material.DIAMOND_CHESTPLATE),set,hardened,settier);
								}
							} else
							if (Math.random()<=0.25) {
								if (PlayerMode.isRanger(p)) {
									raresword = GenerateSetPiece(new ItemStack(Material.LEATHER_LEGGINGS),set,hardened,settier);
								} else {
									raresword = GenerateSetPiece(new ItemStack(Material.DIAMOND_LEGGINGS),set,hardened,settier);
								}
							} else {
								if (PlayerMode.isRanger(p)) {
									raresword = GenerateSetPiece(new ItemStack(Material.LEATHER_BOOTS),set,hardened,settier);
								} else {
									raresword = GenerateSetPiece(new ItemStack(Material.DIAMOND_BOOTS),set,hardened,settier);
								}
							}
						}
					}break;
					case HELLFIRE:{
						if (Math.random()<=0.1) {
							raresword = GenerateSetPiece(new ItemStack(Material.GOLD_SWORD),set,(Math.random()<=0.1)?true:false,settier);
						} else {
							if (Math.random()<=0.25) {
								if (PlayerMode.isRanger(p)) {
									raresword = GenerateSetPiece(new ItemStack(Material.LEATHER_HELMET),set,hardened,settier+1);
								} else {
									raresword = GenerateSetPiece(new ItemStack(Material.GOLD_HELMET),set,hardened,settier);
								}
							} else
							if (Math.random()<=0.25) {
								if (PlayerMode.isRanger(p)) {
									raresword = GenerateSetPiece(new ItemStack(Material.LEATHER_CHESTPLATE),set,hardened,settier+1);
								} else {
									raresword = GenerateSetPiece(new ItemStack(Material.GOLD_CHESTPLATE),set,hardened,settier);
								}
							} else
							if (Math.random()<=0.25) {
								if (PlayerMode.isRanger(p)) {
									raresword = GenerateSetPiece(new ItemStack(Material.LEATHER_LEGGINGS),set,hardened,settier+1);
								} else {
									raresword = GenerateSetPiece(new ItemStack(Material.GOLD_LEGGINGS),set,hardened,settier);
								}
							} else {
								if (PlayerMode.isRanger(p)) {
									raresword = GenerateSetPiece(new ItemStack(Material.LEATHER_BOOTS),set,hardened,settier+1);
								} else {
									raresword = GenerateSetPiece(new ItemStack(Material.GOLD_BOOTS),set,hardened,settier);
								}
							}
						}
					}break;
					default:{
						raresword = GenerateSetPiece(raresword,hardened,settier);
					}break;
				}
			}
		}
		raresword = addEnchantments(raresword,hardened);*/
		return raresword;
	}

	public static ItemSet PickRandomRangerSet() {
		ItemSet set;
		if (Math.random()<0.25) {
			set=ItemSet.JAMDAK;
		} else
		if (Math.random()<0.25) {
			set=ItemSet.DARNYS;
		} else
		if (Math.random()<0.25) {
			set=ItemSet.ALIKAHN;
		} else
		{
			set=ItemSet.LORASAADI;
		}
		return set;
	}
	
	public static ItemStack GenerateRangerPiece(Material mat_type, boolean hardened, int tier) {
		ItemStack raresword = new ItemStack(mat_type);
		ItemMeta sword_meta = raresword.getItemMeta();
		List<String> fakelore = new ArrayList<String>();
		//Choose a green/yellow/white/brown color.
		// Brown - Low Tier (3% - 62% max): Jamdak
		// White - Middle Tier (5% - 70% max): Darnys
		// Yellow - High Tier (8% - 82% max): Alikahn
		// Green - Max Tier (11% - 94% max): Lorasaadi
		switch (tier) {
			default: {
				sword_meta.setDisplayName(ChatColor.AQUA+""+ChatColor.BOLD+"Mega Jamdak Ranger "+GenericFunctions.UserFriendlyMaterialName(mat_type).replace("Leather ", ""));
				if (mat_type.toString().contains("LEATHER")) {
					fakelore.add(ChatColor.LIGHT_PURPLE+"Ranger Gear");
					fakelore.add(ChatColor.GOLD+""+ChatColor.BOLD+"Jamdak Set");
					fakelore.add(ChatColor.YELLOW+"+3% Dodge Chance");
					fakelore.add("");
					fakelore.add(ChatColor.GOLD+""+ChatColor.ITALIC+"Set Bonus:");
					fakelore.add(ChatColor.DARK_AQUA+" 4 - "+ChatColor.WHITE+" Increases duration of Tumble to 3 seconds.");
					fakelore.add(ChatColor.DARK_AQUA+" 4 - "+ChatColor.WHITE+" Increases Damage Reduction by 20%.");
					fakelore.add(ChatColor.DARK_AQUA+" 4 - "+ChatColor.WHITE+" Increases Maximum Health by 20.");
					LeatherArmorMeta lm = (LeatherArmorMeta)sword_meta;
					lm.setColor(Color.fromRGB(128, 64, 0));
				}
			}break;
			case 2: {
				sword_meta.setDisplayName(ChatColor.AQUA+""+ChatColor.BOLD+"Mega Darnys Ranger "+GenericFunctions.UserFriendlyMaterialName(mat_type).replace("Leather ", ""));
				if (mat_type.toString().contains("LEATHER")) {
					fakelore.add(ChatColor.LIGHT_PURPLE+"Ranger Gear");
					fakelore.add(ChatColor.GOLD+""+ChatColor.BOLD+"Darnys Set");
					fakelore.add(ChatColor.YELLOW+"+5% Dodge Chance");
					fakelore.add("");
					fakelore.add(ChatColor.GOLD+""+ChatColor.ITALIC+"Set Bonus:");
					fakelore.add(ChatColor.DARK_AQUA+" 4 - "+ChatColor.WHITE+" Increases duration of Tumble to 3 seconds.");
					fakelore.add(ChatColor.DARK_AQUA+" 4 - "+ChatColor.WHITE+" Increases Damage Reduction by 20%.");
					fakelore.add(ChatColor.DARK_AQUA+" 4 - "+ChatColor.WHITE+" Increases Maximum Health by 20.");
					LeatherArmorMeta lm = (LeatherArmorMeta)sword_meta;
					lm.setColor(Color.fromRGB(224, 224, 224));
				}
			}break;
			case 3: {
				sword_meta.setDisplayName(ChatColor.LIGHT_PURPLE+""+ChatColor.BOLD+"Hardened Mega Alikahn Ranger "+GenericFunctions.UserFriendlyMaterialName(mat_type).replace("Leather ", ""));
				if (mat_type.toString().contains("LEATHER")) {
					fakelore.add(ChatColor.LIGHT_PURPLE+"Ranger Gear");
					fakelore.add(ChatColor.GOLD+""+ChatColor.BOLD+"Alikahn Set");
					fakelore.add(ChatColor.YELLOW+"+8% Dodge Chance");
					fakelore.add(ChatColor.GRAY+"Breaks Remaining: "+ChatColor.YELLOW+GetHardenedBreaks(mat_type));
					fakelore.add("");
					fakelore.add(ChatColor.GOLD+""+ChatColor.ITALIC+"Set Bonus (Ranger Only):");
					fakelore.add(ChatColor.DARK_AQUA+" 4 - "+ChatColor.WHITE+" Increases duration of Tumble to 3 seconds.");
					fakelore.add(ChatColor.DARK_AQUA+" 4 - "+ChatColor.WHITE+" Increases Damage Reduction by 20%.");
					fakelore.add(ChatColor.DARK_AQUA+" 4 - "+ChatColor.WHITE+" Increases Maximum Health by 20.");
					LeatherArmorMeta lm = (LeatherArmorMeta)sword_meta;
					lm.setColor(Color.fromRGB(64, 0, 64));
				}
			}break;
			case 4: {
				sword_meta.setDisplayName(ChatColor.LIGHT_PURPLE+""+ChatColor.BOLD+"Hardened Mega Lorasaadi Ranger "+GenericFunctions.UserFriendlyMaterialName(mat_type).replace("Leather ", ""));
				if (mat_type.toString().contains("LEATHER")) {
					fakelore.add(ChatColor.LIGHT_PURPLE+"Ranger Gear");
					fakelore.add(ChatColor.GOLD+""+ChatColor.BOLD+"Lorasaadi Set");
					fakelore.add(ChatColor.YELLOW+"+11% Dodge Chance");
					fakelore.add(ChatColor.GRAY+"Breaks Remaining: "+ChatColor.YELLOW+GetHardenedBreaks(mat_type));
					fakelore.add("");
					fakelore.add(ChatColor.GOLD+""+ChatColor.ITALIC+"Set Bonus:");
					fakelore.add(ChatColor.DARK_AQUA+" 4 - "+ChatColor.WHITE+" Increases duration of Tumble to 3 seconds.");
					fakelore.add(ChatColor.DARK_AQUA+" 4 - "+ChatColor.WHITE+" Increases Damage Reduction by 20%.");
					fakelore.add(ChatColor.DARK_AQUA+" 4 - "+ChatColor.WHITE+" Increases Maximum Health by 20.");
					LeatherArmorMeta lm = (LeatherArmorMeta)sword_meta;
					lm.setColor(Color.fromRGB(0, 64, 0));
				}
			}break;
		}
		sword_meta.setLore(fakelore);
		raresword.setItemMeta(sword_meta);
		raresword = addEnchantments(raresword,false);
		return raresword;
	}

	/**
	 * @deprecated This generates a random piece based on an item. This is very old and is not recommended to be used anymore.
	 * <b>Use the other GenerateSetPiece() combinations instead!</b>
	 */
	@Deprecated
	public static ItemStack GenerateSetPiece(ItemStack item, boolean hardened) {
		return GenerateSetPiece(item,hardened,0);
	}
 
	/**
	 * @deprecated This generates a random piece based on an item. This is very old and is not recommended to be used anymore.
	 * <b>Use the other GenerateSetPiece() combinations instead!</b>
	 */
	@Deprecated
	public static ItemStack GenerateSetPiece(ItemStack item, boolean hardened, int tierbonus) {
		int type = (int)(Math.random()*3);
		if (item.getType().name().contains("LEATHER") || item.getType()==Material.BOW) {
			type=4+(int)(Math.random()*4);
		}
		ItemSet set = null;
		switch (type) {
			case 0:{
				set = ItemSet.PANROS;
			}break;
			case 1:{
				set = ItemSet.SONGSTEEL;
			}break;
			case 2:{
				set = ItemSet.DAWNTRACKER;
			}break;
			case 3:{
				set = ItemSet.LORASYS;
			}break;
			case 4:{
				set = ItemSet.JAMDAK;
			}break;
			case 5:{
				set = ItemSet.DARNYS;
			}break;
			case 6:{
				set = ItemSet.ALIKAHN;
			}break;
			case 7:{
				set = ItemSet.LORASAADI;
			}break;
			default: 
				set = ItemSet.PANROS;
		}
		return GenerateSetPiece(item,set,hardened,tierbonus);
	}
	
	public static ItemStack GenerateSetPiece(Material item, ItemSet set, boolean hardened, int tierbonus) {
		return GenerateSetPiece(new ItemStack(item),set,hardened,tierbonus,true);
	}
	public static ItemStack GenerateSetPiece(ItemStack item, ItemSet set, boolean hardened, int tierbonus) {
		return GenerateSetPiece(item,set,hardened,tierbonus,true);
	}
	public static ItemStack GenerateSetPiece(Material item, ItemSet set, boolean hardened, int tierbonus, boolean custom) {
		return GenerateSetPiece(new ItemStack(item),set,hardened,tierbonus,custom);
	}
	
	public static ItemStack GenerateSetPiece(ItemStack item, ItemSet set, boolean hardened, int tierbonus, boolean custom) {
		item = GenerateMegaPiece(item.getType(),hardened).clone();
		List<String> lore = new ArrayList<String>();
		String set_name = "";
		String prefix = "";
		boolean allowed=true; //Setting this to false will not convert it to a set piece.
		prefix = (hardened)?(ChatColor.LIGHT_PURPLE+""+ChatColor.BOLD+"Hardened Mega "):(ChatColor.AQUA+""+ChatColor.BOLD+"Mega ");
		switch (set) {
			case PANROS:{
				if (item.getType().toString().contains("SWORD")) {
					
				} else
				if (!GenericFunctions.isEquip(item) ||
						item.getType().name().contains("LEATHER")) {
					allowed = false;
				}
				tierbonus = (custom)?tierbonus:modifyTierBonus(item,tierbonus);
				set_name = prefix+"Panros Striker "+GenericFunctions.UserFriendlyMaterialName(item.getType()); //Striker set.
			}break;
			case SONGSTEEL:{
				if (item.getType().toString().contains("SWORD")) {
					item.setType(Material.SHIELD);
					tierbonus/=(custom)?1:2;
				} else
				if (!GenericFunctions.isEquip(item) ||
						item.getType().name().contains("LEATHER")) {
					allowed = false;
				}
				tierbonus = (custom)?tierbonus:modifyTierBonus(item,tierbonus);
				set_name = prefix+"Songsteel Defender "+GenericFunctions.UserFriendlyMaterialName(item.getType()); //Defender set.
			}break;
			case DAWNTRACKER:{
				if (item.getType().toString().contains("SWORD")) {
					item.setType(Material.valueOf(item.getType().toString().replace("SWORD","")+"AXE"));
				} else
				if (!GenericFunctions.isEquip(item) ||
						item.getType().name().contains("LEATHER")) {
					allowed = false;
				}
				tierbonus = (custom)?tierbonus:modifyTierBonus(item,tierbonus);
				set_name = prefix+"Dawntracker Barbarian "+GenericFunctions.UserFriendlyMaterialName(item.getType());
			}break;
			case LORASYS:{
				if (!item.getType().toString().contains("SWORD")) {
					allowed = false;
				}
				tierbonus = (custom)?tierbonus:modifyTierBonus(item,tierbonus);
				set_name = prefix+"Lorasys Slayer "+GenericFunctions.UserFriendlyMaterialName(item.getType());
			}break;
			case JAMDAK:{
				if (item.getType().toString().contains("SWORD")) {
					item.setType(Material.BOW);
				} else 
				if (!item.getType().toString().contains("LEATHER")) {
					allowed = false;
				}
				set_name = prefix+"Jamdak Ranger "+GenericFunctions.UserFriendlyMaterialName(item.getType());
			}break;
			case DARNYS:{
				if (item.getType().toString().contains("SWORD")) {
					item.setType(Material.BOW);
				} else 
				if (!item.getType().toString().contains("LEATHER")) {
					allowed = false;
				}
				set_name = prefix+"Darnys Ranger "+GenericFunctions.UserFriendlyMaterialName(item.getType());
			}break;
			case ALIKAHN:{
				if (item.getType().toString().contains("SWORD")) {
					item.setType(Material.BOW);
				} else 
				if (!item.getType().toString().contains("LEATHER")) {
					allowed = false;
				}
				set_name = prefix+"Alikahn Ranger "+GenericFunctions.UserFriendlyMaterialName(item.getType());
			}break;
			case LORASAADI:{
				if (item.getType().toString().contains("SWORD")) {
					item.setType(Material.BOW);
				} else 
				if (!item.getType().toString().contains("LEATHER")) {
					allowed = false;
				}
				set_name = prefix+"Lorasaadi Ranger "+GenericFunctions.UserFriendlyMaterialName(item.getType());
			}break;
			case GLADOMAIN:{
				//item.setType(Material.SKULL_ITEM); else 
				if (!item.getType().toString().contains("SKULL_ITEM")) {
					allowed = false;
					break;
				}
				item.setDurability((short)5);
				ItemMeta m = item.getItemMeta();
				m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
				item.setItemMeta(m);
				set_name = prefix+"Gladomain Slayer Amulet";
			}break;
			case MOONSHADOW:{
				if (!item.getType().toString().contains("SKULL_ITEM")) {
					allowed = false;
					break;
				}
				//item.setType(Material.SKULL_ITEM);
				item.setDurability((short)4);
				ItemMeta m = item.getItemMeta();
				m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
				item.setItemMeta(m);
				set_name = prefix+"Moonshadow Slayer Trinket";
			}break;
			case WOLFSBANE:{
				if (!item.getType().toString().contains("SKULL_ITEM")) {
					allowed = false;
					break;
				}
				//item.setType(Material.SKULL_ITEM);
				item.setDurability((short)0);
				ItemMeta m = item.getItemMeta();
				m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
				item.setItemMeta(m);
				set_name = prefix+"Wolfsbane Slayer Ornament";
			}break;
			case ALUSTINE:{
				if (!item.getType().toString().contains("SKULL_ITEM")) {
					allowed = false;
					break;
				}
				//item.setType(Material.SKULL_ITEM);
				item.setDurability((short)2);
				ItemMeta m = item.getItemMeta();
				m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
				item.setItemMeta(m);
				set_name = prefix+"Alustine Slayer Charm";
			}break;
			case BLITZEN:
			case COMET:
			case CUPID:
			case DANCER:
			case DASHER:
			case DONNER:
			case OLIVE:
			case PRANCER:
			case RUDOLPH:
			case VIXEN: {
				tierbonus = (custom)?tierbonus:modifyTierBonus(item,tierbonus);
				set_name = prefix+"Holiday "+GenericFunctions.CapitalizeFirstLetters(set.name())+" "+GenericFunctions.UserFriendlyMaterialName(item.getType()); //Striker set.
			}break;
		}
		if (item.getItemMeta().hasLore()) {
			lore = item.getItemMeta().getLore();
		}
		int tier = tierbonus;
		do {tier++;} while(Math.random()<=0.25);
		if (allowed) {
			lore.addAll(ItemSet.GenerateLore(set,tier));
			ItemMeta m = item.getItemMeta();
			m.setLore(lore);
			m.setDisplayName(set_name);
			item.setItemMeta(m);
		}
		GenericFunctions.ConvertSetColor(item, set);
		item = addEnchantments(item,hardened);
		return item;
	}
	
	private static int modifyTierBonus(ItemStack item, int tierbonus) {
		/*if (item.getType().name().contains("IRON")) {
			if (Math.random()<=0.5 && tierbonus<2) {
				tierbonus+=2;
			}
		} else
		if (item.getType().name().contains("DIAMOND")) {
			if (Math.random()<=0.5 && tierbonus<1) {
				tierbonus+=1;
			}
		} else
		if (item.getType().name().contains("GOLD")) {
			if (tierbonus>0 && Math.random()<=0.5) {
				tierbonus=0;
			}
		}*/
		return tierbonus;
	}

	private static int GetHardenedBreaks(Material type) {
		if (type.toString().contains("STONE")) {
			return (int)((Math.random()*3)+2); 
		} else
		if (type.toString().contains("IRON")) {
			return (int)((Math.random()*4)+3); 
		} else
		if (type.toString().contains("DIAMOND")) {
			return (int)((Math.random()*6)+5); 
		} else
		if (type.toString().contains("GOLD")) {
			return (int)((Math.random()*12)+3); 
		} else
		if (type.toString().contains("LEATHER")) {
			return (int)((Math.random()*7)+6); 
		} else
		{
			return 5;
		}
	}
	
	private static int GetEnchantmentLevels(Material type, boolean hardened) {
		int enchantment_level = 0;
		if (type.toString().contains("STONE")) {
			enchantment_level = (int)(((Math.random()*3)+2)*((hardened)?HARDENED_ENCHANT_MULT:1)); 
		} else
		if (type.toString().contains("IRON")) {
			enchantment_level = (int)(((Math.random()*4)+3)*((hardened)?HARDENED_ENCHANT_MULT:1)); 
		} else
		if (type.toString().contains("DIAMOND")) {
			enchantment_level = (int)(((Math.random()*4)+5)*((hardened)?HARDENED_ENCHANT_MULT:1)); 
		} else
		if (type.toString().contains("GOLD")) {
			enchantment_level = (int)(((Math.random()*5)+6)*((hardened)?HARDENED_ENCHANT_MULT:1)); 
		} else
		if (type.toString().contains("BOW")) {
			enchantment_level = (int)(((Math.random()*5)+6)*((hardened)?HARDENED_ENCHANT_MULT:1)); 
		} else
		{
			enchantment_level = (int)(((Math.random()*7)+2)*((hardened)?HARDENED_ENCHANT_MULT:1)); 
		}
		
		if (enchantment_level>MAX_ENCHANT_LEVEL) {
			enchantment_level = 10;
		}
		
		return enchantment_level;
	}
	
	public static ItemStack addEnchantments(ItemStack item, boolean hardened) {
		if (GenericFunctions.isHarvestingTool(item)) {
			if (Math.random()<0.5*HARDENED_ENCHANT_MULT) {item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, GetEnchantmentLevels(item.getType(),hardened));}
			item.addUnsafeEnchantment(Enchantment.DIG_SPEED, GetEnchantmentLevels(item.getType(),hardened));
			item.addUnsafeEnchantment(Enchantment.DURABILITY, GetEnchantmentLevels(item.getType(),hardened));
			if (Math.random()<0.5*HARDENED_ENCHANT_MULT) {item.addUnsafeEnchantment(Enchantment.KNOCKBACK, 2);}
			if (item.getType().toString().contains("HOE")) {item.addUnsafeEnchantment(Enchantment.KNOCKBACK, 10);}
			if (Math.random()<0.2*HARDENED_ENCHANT_MULT) {item.addUnsafeEnchantment(Enchantment.SILK_TOUCH, GetEnchantmentLevels(item.getType(),hardened));}
			if (Math.random()<0.001*HARDENED_ENCHANT_MULT) {item.addUnsafeEnchantment(Enchantment.MENDING, GetEnchantmentLevels(item.getType(),hardened));}
			if (Math.random()<0.001*HARDENED_ENCHANT_MULT) {item.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, GetEnchantmentLevels(item.getType(),hardened));}
		} else 
		if (item.getType()==Material.BOW) {
			item.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, GetEnchantmentLevels(item.getType(),hardened));
			item.addUnsafeEnchantment(Enchantment.ARROW_FIRE, GetEnchantmentLevels(item.getType(),hardened));
			if (Math.random()<0.5*HARDENED_ENCHANT_MULT) {item.addUnsafeEnchantment(Enchantment.DURABILITY, GetEnchantmentLevels(item.getType(),hardened));}
			item.addUnsafeEnchantment(Enchantment.ARROW_KNOCKBACK, 2);
			if (Math.random()<0.2*HARDENED_ENCHANT_MULT) {item.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, GetEnchantmentLevels(item.getType(),hardened));}
			if (Math.random()<0.001*HARDENED_ENCHANT_MULT) {item.addUnsafeEnchantment(Enchantment.MENDING, GetEnchantmentLevels(item.getType(),hardened));}
		} else 
		if (GenericFunctions.isWeapon(item)) {
			item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, GetEnchantmentLevels(item.getType(),hardened));
			if (Math.random()<0.5*HARDENED_ENCHANT_MULT) {item.addUnsafeEnchantment(Enchantment.DAMAGE_ARTHROPODS, GetEnchantmentLevels(item.getType(),hardened));}
			if (Math.random()<0.5*HARDENED_ENCHANT_MULT) {item.addUnsafeEnchantment(Enchantment.DAMAGE_UNDEAD, GetEnchantmentLevels(item.getType(),hardened));}
			if (Math.random()<0.5*HARDENED_ENCHANT_MULT) {item.addUnsafeEnchantment(Enchantment.DURABILITY, GetEnchantmentLevels(item.getType(),hardened));}
			if (Math.random()<0.5*HARDENED_ENCHANT_MULT) {item.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, GetEnchantmentLevels(item.getType(),hardened));}
			if (Math.random()<0.5*HARDENED_ENCHANT_MULT) {item.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);}
			if (Math.random()<0.001*HARDENED_ENCHANT_MULT) {item.addUnsafeEnchantment(Enchantment.MENDING, GetEnchantmentLevels(item.getType(),hardened));}
			if (Math.random()<0.001*HARDENED_ENCHANT_MULT) {item.addUnsafeEnchantment(Enchantment.LOOT_BONUS_MOBS, GetEnchantmentLevels(item.getType(),hardened));}
		} else 
		if (item.getType()==Material.SHIELD) {
			if (Math.random()<0.5*HARDENED_ENCHANT_MULT) {item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, GetEnchantmentLevels(item.getType(),hardened));}
			if (Math.random()<0.5*HARDENED_ENCHANT_MULT) {item.addUnsafeEnchantment(Enchantment.DURABILITY, GetEnchantmentLevels(item.getType(),hardened));}
			if (Math.random()<0.08*HARDENED_ENCHANT_MULT) {item.addUnsafeEnchantment(Enchantment.KNOCKBACK, 2);}
			if (Math.random()<0.2*HARDENED_ENCHANT_MULT) {item.addUnsafeEnchantment(Enchantment.SILK_TOUCH, GetEnchantmentLevels(item.getType(),hardened));}
			item.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, GetEnchantmentLevels(item.getType(),hardened));
			if (Math.random()<0.5*HARDENED_ENCHANT_MULT) {item.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, GetEnchantmentLevels(item.getType(),hardened));}
			if (Math.random()<0.5*HARDENED_ENCHANT_MULT) {item.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, GetEnchantmentLevels(item.getType(),hardened));}
			if (Math.random()<0.5*HARDENED_ENCHANT_MULT) {item.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, GetEnchantmentLevels(item.getType(),hardened));}
			if (Math.random()<0.5*HARDENED_ENCHANT_MULT) {item.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, GetEnchantmentLevels(item.getType(),hardened));}
			if (Math.random()<0.08*HARDENED_ENCHANT_MULT) {item.addUnsafeEnchantment(Enchantment.THORNS, GetEnchantmentLevels(item.getType(),hardened));}
			//item.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
			if (Math.random()<0.001*HARDENED_ENCHANT_MULT) {item.addUnsafeEnchantment(Enchantment.MENDING, GetEnchantmentLevels(item.getType(),hardened));}
		} else 
		if (GenericFunctions.isArmor(item)) {
			item.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, GetEnchantmentLevels(item.getType(),hardened));
			if (Math.random()<0.5*HARDENED_ENCHANT_MULT) {item.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, GetEnchantmentLevels(item.getType(),hardened));}
			if (Math.random()<0.5*HARDENED_ENCHANT_MULT) {item.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, GetEnchantmentLevels(item.getType(),hardened));}
			if (Math.random()<0.5*HARDENED_ENCHANT_MULT) {item.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, GetEnchantmentLevels(item.getType(),hardened));}
			if (Math.random()<0.5*HARDENED_ENCHANT_MULT) {item.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, GetEnchantmentLevels(item.getType(),hardened));}
			if (Math.random()<0.2*HARDENED_ENCHANT_MULT && item.getType().toString().contains("BOOTS")) {item.addUnsafeEnchantment(Enchantment.DEPTH_STRIDER, GetEnchantmentLevels(item.getType(),hardened));}
			if (Math.random()<0.2*HARDENED_ENCHANT_MULT && item.getType().toString().contains("HELMET")) {item.addUnsafeEnchantment(Enchantment.WATER_WORKER, 1);}
			if (Math.random()<0.2*HARDENED_ENCHANT_MULT && item.getType().toString().contains("HELMET")) {item.addUnsafeEnchantment(Enchantment.OXYGEN, 3);}
			if (Math.random()<0.2*HARDENED_ENCHANT_MULT && item.getType().toString().contains("BOOTS")) {item.addUnsafeEnchantment(Enchantment.FROST_WALKER, GetEnchantmentLevels(item.getType(),hardened));}
			if (Math.random()<0.08*HARDENED_ENCHANT_MULT) {item.addUnsafeEnchantment(Enchantment.THORNS, GetEnchantmentLevels(item.getType(),hardened));}
			//item.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
			if (Math.random()<0.001*HARDENED_ENCHANT_MULT) {item.addUnsafeEnchantment(Enchantment.DURABILITY, GetEnchantmentLevels(item.getType(),hardened));}
			if (Math.random()<0.001*HARDENED_ENCHANT_MULT) {item.addUnsafeEnchantment(Enchantment.MENDING, GetEnchantmentLevels(item.getType(),hardened));}
		} else {
			//Generic Random Enchantments.
			for (int i=0;i<Enchantment.values().length;i++) {
				if (Math.random()<1.0/Enchantment.values().length*HARDENED_ENCHANT_MULT) {
					item.addUnsafeEnchantment(Enchantment.values()[i], GetEnchantmentLevels(item.getType(),hardened));
				}
			}
		}
		if (item.getType()==Material.FISHING_ROD) {
			item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, GetEnchantmentLevels(item.getType(),hardened));
			item.addUnsafeEnchantment(Enchantment.DURABILITY, GetEnchantmentLevels(item.getType(),hardened));
			item.addUnsafeEnchantment(Enchantment.LUCK, GetEnchantmentLevels(item.getType(),hardened));
			item.addUnsafeEnchantment(Enchantment.LURE, (int)(((Math.random()*3)+2)*((hardened)?HARDENED_ENCHANT_MULT:1)));
		}
		return item;
	}

	/*
	public static ItemStack DropProperSetPiece(Entity damager, Monster monster) {
		//Alright. Let's make a set piece.
		MonsterDifficulty md = MonsterController.getMonsterDifficulty(monster);
		//Now determine the type of piece it will be.
		ItemStack[] randomlist = null;
		switch (md) {
			case NORMAL:{
				//Drop a stone item.
				randomlist=new ItemStack[]{
					new ItemStack(Material.STONE_SWORD)
				};
			}break;
			case DANGEROUS:{
				//Drop a stone item.
				randomlist=new ItemStack[]{
					new ItemStack(Material.IRON_SWORD),
					new ItemStack(Material.IRON_CHESTPLATE),
					new ItemStack(Material.IRON_HELMET),
					new ItemStack(Material.IRON_LEGGINGS),
					new ItemStack(Material.IRON_BOOTS),
				};
			}break;
			case DEADLY:{
				//Drop a stone item.
				randomlist=new ItemStack[]{
					new ItemStack(Material.DIAMOND_SWORD),
					new ItemStack(Material.DIAMOND_CHESTPLATE),
					new ItemStack(Material.DIAMOND_HELMET),
					new ItemStack(Material.DIAMOND_LEGGINGS),
					new ItemStack(Material.DIAMOND_BOOTS),
				};
			}break;
			case HELLFIRE:{
				//Drop a stone item.
				randomlist=new ItemStack[]{
					new ItemStack(Material.GOLD_SWORD),
					new ItemStack(Material.GOLD_CHESTPLATE),
					new ItemStack(Material.GOLD_HELMET),
					new ItemStack(Material.GOLD_LEGGINGS),
					new ItemStack(Material.GOLD_BOOTS),
				};
			}break;
		}
	}*/
}
