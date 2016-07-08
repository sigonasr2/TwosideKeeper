package sig.plugin.TwosideKeeper;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.chat.TextComponent;
import sig.plugin.TwosideKeeper.HelperStructures.ArtifactItemType;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;

public class AwakenedArtifact {
	int experience_points=0;
	private static String drawEXPMeter(int exp) {
		String bar ="";
		for (int i=0;i<exp/100;i++) {
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
			String expline = artifact.getItemMeta().getLore().get(4);
			String exp = (expline.split("\\(")[1]).split("/")[0];
			int expval = Integer.parseInt(exp);
			return expval;
		}
		TwosideKeeper.log("Could not retrieve EXP value for artifact "+artifact.toString(), 1);
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
			lore.set(3, ChatColor.YELLOW+"EXP"+ChatColor.RESET+" ["+drawEXPMeter(amt)+"] "+df.format((((double)amt/1000)*100))+"%"); //Update the EXP bar.
			lore.set(4, ChatColor.BLUE+"  ("+amt+"/1000) "+"Potential: "+drawPotential(artifact));
			m.setLore(lore);
			artifact.setItemMeta(m);
			return artifact;
		}
		TwosideKeeper.log("Could not set the EXP value for artifact "+artifact.toString(), 1);
		return artifact;
	}
	public static ItemStack addEXP(ItemStack artifact, int amt, Player p) {
		int totalval = getEXP(artifact)+amt;
		if (totalval>1000) {
			//LEVEL UP!
			ItemStack item = addLV(artifact,totalval/1000, p);
			item = setEXP(item,totalval%1000);
			item = addAP(item,1);
			p.sendMessage("Your "+artifact.getItemMeta().getDisplayName()+ChatColor.RESET+" has upgraded to "+ChatColor.YELLOW+"Level "+getLV(artifact)+"!");
			p.sendMessage("You have "+getAP(item)+" Ability Points to spend!");
			p.spigot().sendMessage(new TextComponent("Test"));
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
			String lvline = artifact.getItemMeta().getLore().get(5);
			String lv = lvline.split(" ")[1];
			int LV = Integer.parseInt(lv);
			return LV;
		}
		TwosideKeeper.log("Could not retrieve LV value for artifact "+artifact.toString(), 1);
		return -1; //If we got here, something bad happened.
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
			lore.set(5, ChatColor.GRAY+"Level "+df.format(amt));
			String apline = lore.get(6);
			lore.set(6, ChatColor.GOLD+"Ability Points: "+getAP(artifact)+"/"+amt);
			m.setLore(lore);
			artifact.setItemMeta(m);
			return artifact;
		}
		TwosideKeeper.log("Could not set the LV value for artifact "+artifact.toString(), 1);
		return artifact;
	}
	public static ItemStack addAP(ItemStack artifact, int amt) {
		if (artifact!=null &&
			artifact.getType()!=Material.AIR &&
			artifact.hasItemMeta() &&
			artifact.getItemMeta().hasLore() &&
			Artifact.isArtifact(artifact)) {
			ItemMeta m = artifact.getItemMeta();
			List<String> lore = m.getLore();
			DecimalFormat df = new DecimalFormat("000");
			String apline = lore.get(6);
			int currentAP = Integer.parseInt(((apline.split("/")[0]).split(": ")[1]));
			lore.set(6, ChatColor.GOLD+"Ability Points: "+(currentAP+amt)+"/"+getLV(artifact));
			m.setLore(lore);
			artifact.setItemMeta(m);
			return artifact;
		}
		TwosideKeeper.log("Could not get the AP value for artifact "+artifact.toString(), 1);
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
			DecimalFormat df = new DecimalFormat("000");
			String apline = lore.get(6);
			return Integer.parseInt(((apline.split("/")[0]).split(": ")[1]));
		}
		TwosideKeeper.log("Could not get the AP value for artifact "+artifact.toString(), 1);
		return -1;
	}
	public static ItemStack addLV(ItemStack artifact, int amt, Player p) {
		return setLV(artifact,getLV(artifact)+amt,p);
	}
	public static ItemStack addPotentialEXP(ItemStack artifact,int exp,Player p) {
		//Adds experience, but only based on the potential of the item.
		int missingdurability = artifact.getDurability();
		if (missingdurability!=0) {
			double mult = Math.min(10d/missingdurability, 1);
			//Multiply the value. If it's less than 1, there is only a chance exp will be added.
			double expadded = exp*mult;
			if (expadded<1 &&
					Math.random()<=expadded) {
				return addEXP(artifact,1,p);
			} else {
				return addEXP(artifact,(int)expadded,p);
			}
		} else {
			return addEXP(artifact,exp,p);
		}
	}
	public static String drawPotential(ItemStack artifact) { 
		int missingdurability = artifact.getDurability();
		double mult = Math.min(10d/missingdurability, 1);
		int potential = (int)(mult*100);
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
		item = Artifact.convert(item,true);
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
		lore.add(ChatColor.BLUE+"  (0/1000) "+"Potential: "+drawPotential(artifact));
		df = new DecimalFormat("000");
		lore.add(ChatColor.GRAY+"Level "+df.format(0));
		lore.add(ChatColor.GOLD+"Ability Points: 0/0");
		m.setLore(lore);
		item.setItemMeta(m);
		item.addUnsafeEnchantment(Enchantment.LUCK, tier);
		return item;
	}
	
}
