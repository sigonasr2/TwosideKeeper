package sig.plugin.TwosideKeeper;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import sig.plugin.TwosideKeeper.HelperStructures.ArtifactAbility;
import sig.plugin.TwosideKeeper.HelperStructures.ArtifactItemType;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;

public class AwakenedArtifact {
	int experience_points=0;
	public static HashMap<ArtifactAbility,String> ability_map = new HashMap<ArtifactAbility,String>();
	public static HashMap<String,ArtifactAbility> name_map = new HashMap<String,ArtifactAbility>();
	private static String drawEXPMeter(int exp) {
		String bar ="";
		for (int i=0;i<((exp%1000)/100);i++) {
			bar+=Character.toString((char)0x2588);
		}
		if (exp%100>=50) {
			bar+=Character.toString((char)0x258C);
		}
		int extraspace=10-bar.length();
		for (int i=0;i<extraspace;i++) {
			bar+=Character.toString((char)0x2500);
		}
		return bar;
	}
	public static int getEXP(ItemStack artifact) {
		if (artifact!=null &&
				artifact.getType()!=Material.AIR &&
				artifact.hasItemMeta() &&
				artifact.getItemMeta().hasLore() &&
				Artifact.isArtifact(artifact)) {
			String expline = artifact.getItemMeta().getLore().get(findPotentialLine(artifact.getItemMeta().getLore()));
			String exp = (expline.split("\\(")[1]).split("/")[0];
			int expval = Integer.parseInt(exp);
			return expval;
		}
		//TwosideKeeper.log("Could not retrieve EXP value for artifact "+artifact.toString(), 1);
		return -1; //If we got here, something bad happened.
	}
	public static ItemStack setEXP(ItemStack artifact, int amt) {
		if (artifact!=null &&
				artifact.getType()!=Material.AIR &&
				artifact.hasItemMeta() &&
				artifact.getItemMeta().hasLore() &&
				Artifact.isArtifact(artifact)) {
			ItemMeta m = artifact.getItemMeta();
			List<String> lore = m.getLore();
			DecimalFormat df = new DecimalFormat("00");
			lore.set(findEXPBarLine(lore), ChatColor.YELLOW+"EXP"+ChatColor.RESET+" ["+drawEXPMeter(amt)+"] "+df.format((((double)amt/1000)*100))+"%"); //Update the EXP bar.
			lore.set(findPotentialLine(lore), ChatColor.BLUE+"  ("+amt+"/1000) "+"Potential: "+drawPotential(artifact,getPotential(artifact)));
			m.setLore(lore);
			artifact.setItemMeta(m);
			return artifact;
		}
		//TwosideKeeper.log("Could not set the EXP value for artifact "+artifact.toString(), 1);
		return artifact;
	}
	private static int findPotentialLine(List<String> lore) {
		for (int i=0;i<lore.size();i++) {
			if (lore.get(i).contains(ChatColor.BLUE+"  (")) {
				return i;
			}
		}
		TwosideKeeper.log("Could not find the Potential line for this item!!! This should never happen!\nLore Set: "+lore.toString(), 0);
		return -1;
	}
	private static int findEXPBarLine(List<String> lore) {
		for (int i=0;i<lore.size();i++) {
			if (lore.get(i).contains(ChatColor.YELLOW+"EXP"+ChatColor.RESET+" [")) {
				return i;
			}
		}
		TwosideKeeper.log("Could not find the EXP line for this item!!! This should never happen!\nLore Set: "+lore.toString(), 0);
		return -1;
	}
	public static ItemStack addEXP(ItemStack artifact, int amt, Player p) {
		int totalval = getEXP(artifact)+amt;
		if (totalval>=1000) {
			//LEVEL UP!
			ItemStack item = addLV(artifact,totalval/1000, p);
			item = addMaxAP(item,totalval/1000);
			item = setEXP(item,totalval%1000);
			item = addAP(item,totalval/1000);
			double potentialred = 10.0d;
			potentialred *= 1 - GenericFunctions.getAbilityValue(ArtifactAbility.PRESERVATION, artifact)/100d;
			TwosideKeeper.log("Potential reduction is reduced by "+(10-potentialred), 4);
			if (getPotential(item)>potentialred) {
				item = setPotential(item,(int)(getPotential(item)-potentialred));
				if (Math.random() < (potentialred % 1)) {
					item = addPotential(item,1);
				}
			} else {
				if (Math.random()<=getPotential(item)/potentialred) {
					item = addPotential(item,-1);
				}
			}
			p.sendMessage("Your "+artifact.getItemMeta().getDisplayName()+ChatColor.RESET+" has upgraded to "+ChatColor.YELLOW+"Level "+getLV(artifact)+"!");
			TextComponent tc1 = new TextComponent("You have "+getAP(item)+" Ability Point"+((getAP(item)==1)?"":"s")+" to spend! ");

			TextComponent tc = new TextComponent(ChatColor.GREEN+"["+Character.toString((char)0x25b2)+"]");
			tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ComponentBuilder("Click to upgrade abilities on this artifact. "+ChatColor.GREEN+"Available AP: "+ChatColor.BLUE+AwakenedArtifact.getAP(item)).create()));
			tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/awakenedartifact menu "+GenericFunctions.CalculateSlot(item,p)));
			/*TextComponent tc = new TextComponent("Click ");
			TextComponent ac = new TextComponent(ChatColor.GREEN+"[HERE]"+ChatColor.WHITE);
			ac.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ComponentBuilder(ChatColor.ITALIC+"Click to add another skill point!").create()));
			ac.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/awakenedartifact"));
			tc.addExtra(ac);
			tc.addExtra(" to open up the ability upgrade menu.");*/
			tc1.addExtra(tc);
			p.spigot().sendMessage(tc1);
			//p.spigot().sendMessage(ArtifactAbility.GenerateMenu(ArtifactItemType.getArtifactItemTypeFromItemStack(p.getInventory().getItem(GenericFunctions.CalculateSlot(artifact,p))).getUpgradePath(), CustomDamage.getBaseWeaponDamage(artifact, p, null), artifact,GenericFunctions.CalculateSlot(artifact,p)));
			return item;
		} else {
			return setEXP(artifact,totalval);
		}
	}
	public static int getLV(ItemStack artifact) {
		if (artifact!=null &&
				artifact.getType()!=Material.AIR &&
				artifact.hasItemMeta() &&
				artifact.getItemMeta().hasLore() &&
				Artifact.isArtifact(artifact)) {
			String lvline = artifact.getItemMeta().getLore().get(findLVLine(artifact.getItemMeta().getLore()));
			String lv = lvline.split(" ")[1];
			int LV = Integer.parseInt(lv);
			return LV;
		}
		//TwosideKeeper.log("Could not retrieve LV value for artifact "+artifact.toString(), 1);
		return -1; //If we got here, something bad happened.
	}
	private static int findLVLine(List<String> lore) {
		for (int i=0;i<lore.size();i++) {
			if (lore.get(i).contains(ChatColor.GRAY+"Level ")) {
				return i;
			}
		}
		TwosideKeeper.log("Could not find the Level line for this item!!! This should never happen!\nLore Set: "+lore.toString(), 0);
		return -1;
	}
	public static ItemStack setLV(ItemStack artifact, int amt, Player p) {
		if (artifact!=null &&
				artifact.getType()!=Material.AIR &&
				artifact.hasItemMeta() &&
				artifact.getItemMeta().hasLore() &&
				Artifact.isArtifact(artifact)) {
			ItemMeta m = artifact.getItemMeta();
			List<String> lore = m.getLore();
			DecimalFormat df = new DecimalFormat("000");
			lore.set(findLVLine(lore), ChatColor.GRAY+"Level "+df.format(amt));
			//lore.set(findAPLine(lore), ChatColor.GOLD+"Ability Points: "+getAP(artifact)+"/"+amt);
			m.setLore(lore);
			artifact.setItemMeta(m);
			return artifact;
		}
		//TwosideKeeper.log("Could not set the LV value for artifact "+artifact.toString(), 1);
		return artifact;
	}
	public static int findAPLine(List<String> lore) {
		for (int i=0;i<lore.size();i++) {
			if (lore.get(i).contains(ChatColor.GOLD+"Ability Points: ")) {
				return i;
			}
		}
		TwosideKeeper.log("Could not find the AP line for this item!!! This should never happen!\nLore Set: "+lore.toString(), 0);
		return -1;
	}
	public static ItemStack addAP(ItemStack artifact, int amt) {
		if (artifact!=null &&
			artifact.getType()!=Material.AIR &&
			artifact.hasItemMeta() &&
			artifact.getItemMeta().hasLore() &&
			Artifact.isArtifact(artifact)) {
			ItemMeta m = artifact.getItemMeta();
			List<String> lore = m.getLore();
			int currentAP = getAP(artifact);
			lore.set(findAPLine(lore), ChatColor.GOLD+"Ability Points: "+(currentAP+amt)+"/"+getMaxAP(artifact));
			m.setLore(lore);
			artifact.setItemMeta(m);
			return artifact;
		}
		//TwosideKeeper.log("Could not get the AP value for artifact "+artifact.toString(), 1);
		return null;
	}
	public static ItemStack setAP(ItemStack artifact, int newamt) {
		if (artifact!=null &&
			artifact.getType()!=Material.AIR &&
			artifact.hasItemMeta() &&
			artifact.getItemMeta().hasLore() &&
			Artifact.isArtifact(artifact)) {
			ItemMeta m = artifact.getItemMeta();
			List<String> lore = m.getLore();
			lore.set(findAPLine(lore), ChatColor.GOLD+"Ability Points: "+(newamt)+"/"+getMaxAP(artifact));
			m.setLore(lore);
			artifact.setItemMeta(m);
			return artifact;
		}
		//TwosideKeeper.log("Could not get the AP value for artifact "+artifact.toString(), 1);
		return null;
	}
	public static int getAP(ItemStack artifact) {
		if (artifact!=null &&
			artifact.getType()!=Material.AIR &&
			artifact.hasItemMeta() &&
			artifact.getItemMeta().hasLore() &&
			Artifact.isArtifact(artifact)) {
			ItemMeta m = artifact.getItemMeta();
			List<String> lore = m.getLore();
			String apline = lore.get(findAPLine(lore));
			/*int level = getLV(artifact); //This is how many total we have.
			int apused = 0;
			HashMap<ArtifactAbility,Integer> enchants = ArtifactAbility.getEnchantments(artifact);
			for (int i=0;i<enchants.values().size();i++) {
				apused += Iterables.get(enchants.values(), i); //Counts how many levels of each enchantment was applied. This correlates directly with how much AP was used.
			}
			return level-apused;*/
			return Integer.parseInt(((apline.split("/")[0]).split(": ")[1]));
		}
		//TwosideKeeper.log("Could not get the AP value for artifact "+artifact.toString(), 1);
		return -1;
	}
	public static ItemStack addMaxAP(ItemStack artifact, int amt) {
		if (artifact!=null &&
			artifact.getType()!=Material.AIR &&
			artifact.hasItemMeta() &&
			artifact.getItemMeta().hasLore() &&
			Artifact.isArtifact(artifact)) {
			ItemMeta m = artifact.getItemMeta();
			List<String> lore = m.getLore();
			int currentMaxAP = getMaxAP(artifact);
			lore.set(findAPLine(lore), ChatColor.GOLD+"Ability Points: "+getAP(artifact)+"/"+(currentMaxAP+amt));
			m.setLore(lore);
			artifact.setItemMeta(m);
			return artifact;
		}
		//TwosideKeeper.log("Could not get the AP value for artifact "+artifact.toString(), 1);
		return null;
	}
	public static ItemStack setMaxAP(ItemStack artifact, int newamt) {
		if (artifact!=null &&
			artifact.getType()!=Material.AIR &&
			artifact.hasItemMeta() &&
			artifact.getItemMeta().hasLore() &&
			Artifact.isArtifact(artifact)) {
			ItemMeta m = artifact.getItemMeta();
			List<String> lore = m.getLore();
			lore.set(findAPLine(lore), ChatColor.GOLD+"Ability Points: "+getAP(artifact)+"/"+(newamt));
			m.setLore(lore);
			artifact.setItemMeta(m);
			return artifact;
		}
		//TwosideKeeper.log("Could not get the AP value for artifact "+artifact.toString(), 1);
		return null;
	}
	public static int getMaxAP(ItemStack artifact) {
		if (artifact!=null &&
			artifact.getType()!=Material.AIR &&
			artifact.hasItemMeta() &&
			artifact.getItemMeta().hasLore() &&
			Artifact.isArtifact(artifact)) {
			ItemMeta m = artifact.getItemMeta();
			List<String> lore = m.getLore();
			String apline = lore.get(findAPLine(lore));
			/*int level = getLV(artifact); //This is how many total we have.
			int apused = 0;
			HashMap<ArtifactAbility,Integer> enchants = ArtifactAbility.getEnchantments(artifact);
			for (int i=0;i<enchants.values().size();i++) {
				apused += Iterables.get(enchants.values(), i); //Counts how many levels of each enchantment was applied. This correlates directly with how much AP was used.
			}
			return level-apused;*/
			return Integer.parseInt(((apline.split("/")[1])));
		}
		//TwosideKeeper.log("Could not get the Max AP value for artifact "+artifact.toString(), 1);
		return -1;
	}
	public static ItemStack addLV(ItemStack artifact, int amt, Player p) {
		return setLV(artifact,getLV(artifact)+amt,p);
	}
	public static int getPotential(ItemStack artifact) {
		if (GenericFunctions.isArtifactEquip(artifact)) {
			ItemMeta m = artifact.getItemMeta();
			List<String> lore = m.getLore();
			String potentialline = lore.get(findPotentialLine(lore));
			return Integer.parseInt(ChatColor.stripColor(potentialline.split("Potential: ")[1].replace("%", "")));
		} else {
			//TwosideKeeper.log("Could not get the Potential value for artifact "+artifact.toString(), 1);
			return -1;
		}
	}
	public static ItemStack setPotential(ItemStack artifact, int amt) {
		if (GenericFunctions.isArtifactEquip(artifact)) {
			ItemMeta m = artifact.getItemMeta();
			List<String> lore = m.getLore();
			int potential = amt;
			String potentialline = lore.get(findPotentialLine(lore)).split("Potential: ")[0]+"Potential: "+drawPotential(artifact,potential);
			lore.set(findPotentialLine(lore), potentialline);
			m.setLore(lore);
			artifact.setItemMeta(m);
			return artifact;
		} else {
			//TwosideKeeper.log("Could not get the Potential value for artifact "+artifact.toString(), 1);
			return artifact;
		}
	}
	public static ItemStack addPotential(ItemStack artifact, int amt) {
		if (GenericFunctions.isArtifactEquip(artifact)) {
			ItemMeta m = artifact.getItemMeta();
			List<String> lore = m.getLore();
			int potential = getPotential(artifact)+amt;
			String potentialline = lore.get(findPotentialLine(lore)).split("Potential: ")[0]+"Potential: "+drawPotential(artifact,potential);
			lore.set(findPotentialLine(lore), potentialline);
			m.setLore(lore);
			artifact.setItemMeta(m);
			return artifact;
		} else {
			//TwosideKeeper.log("Could not get the Potential value for artifact "+artifact.toString(), 1);
			return artifact;
		}
	}
	public static ItemStack addPotentialEXP(ItemStack artifact,int exp,Player p) {
		//Adds experience, but only based on the potential of the item.
		if (GenericFunctions.isArtifactEquip(artifact)) {
			double mult = getPotential(artifact)/100d;
			//Multiply the value. If it's less than 1, there is only a chance exp will be added.
			double expadded = exp*mult;
			TwosideKeeper.log("Added EXP.", 5);
			if (expadded<1 &&
					Math.random()<=expadded) {
				return addEXP(artifact,1,p);
			} else {
				return addEXP(artifact,(int)expadded,p);
			}
		} else {
			return artifact;
		}
	}
	public static String drawPotential(ItemStack artifact, int potentialamt) { 
		int potential = potentialamt;
		ChatColor color = null;
		if (potential>75) {
			color = ChatColor.DARK_GREEN;
		} else 
		if (potential>50) {
			color = ChatColor.GREEN;
		} else 
		if (potential>33) {
			color = ChatColor.YELLOW;
		} else 
		if (potential>25) {
			color = ChatColor.GOLD;
		} else 
		if (potential>10) {
			color = ChatColor.RED;
		} else {
			color = ChatColor.DARK_RED;
		}
		return color+""+potential+"%";
	}
	public static ItemStack convertToAwakenedArtifact(ItemStack artifact, int tier, int dataval) {
		ItemStack item = artifact.clone();
		TwosideKeeper.log("Trying to create data "+dataval, 5);
		item = Artifact.convert(item,false);
		GenericFunctions.addObscureHardenedItemBreaks(item, 5);
		ItemMeta m = item.getItemMeta();
		m.setDisplayName(ChatColor.GOLD+""+ChatColor.BOLD+"T"+tier+" "+GenericFunctions.CapitalizeFirstLetters(ArtifactItemType.getTypeFromData(dataval).getItemName())+" Artifact");
		m.setDisplayName(m.getDisplayName().replace("Hoe", "Scythe"));
		List<String> lore = new ArrayList<String>();
		if (m.hasLore()) {
			lore = m.getLore();
		}
		DecimalFormat df = new DecimalFormat("00");
		lore.add(ChatColor.YELLOW+"EXP"+ChatColor.RESET+" ["+drawEXPMeter(0)+"] "+df.format(((0d/1000)*100))+"%");
		lore.add(ChatColor.BLUE+"  (0/1000) "+"Potential: "+drawPotential(artifact,100));
		df = new DecimalFormat("000");
		lore.add(ChatColor.GRAY+"Level "+df.format(0));
		lore.add(ChatColor.GOLD+"Ability Points: 0/0"); 
		//AP is on line 7 (element 6)
		m.setLore(lore);
		item.setItemMeta(m);
		item.addUnsafeEnchantment(Enchantment.LUCK, tier);
		return item;
	}
	
}
