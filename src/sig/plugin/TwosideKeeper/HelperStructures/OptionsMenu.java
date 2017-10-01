package sig.plugin.TwosideKeeper.HelperStructures;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import sig.plugin.TwosideKeeper.PlayerStructure;
import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.ItemUtils;

public class OptionsMenu {
	Player p;
	Inventory inv;
	List<Option> options;
	
	final int NUMBER_OF_ROWS = (OptionName.values().length/3)+(((OptionName.values().length%3)!=0)?1:0);
	
	public OptionsMenu(Player p) {
		this.p=p;
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		pd.optionsmenu=this;
		inv = Bukkit.createInventory(p, NUMBER_OF_ROWS*9, ChatColor.BLUE+"Options Menu");
		this.options = new ArrayList<Option>();
		
		PopulateInterface(p);
		
		//inv.setItem(arg0, arg1);
		
		p.openInventory(inv);
	}

	private void PopulateInterface(Player p) {
		for (int i=0;i<OptionName.values().length;i++) {
			Option o = new Option(OptionName.values()[i],i*3,(i*3)+1);
			ItemStack it = o.getOption().getMaterialData().toItemStack();
			SetupDescriptions(o, it);
			ItemStack it2 = new ItemStack(o.getTorchIcon(p));
			it2.setAmount(0);
			SetupDescriptions(o, it2);
			//TwosideKeeper.log(o+";;"+it+";;"+it2, 0);
			
			inv.setItem(o.getPositions()[0], it);
			inv.setItem(o.getPositions()[1], it2);
			options.add(o);
		}
	}

	private void SetupDescriptions(Option o, ItemStack it) {
		ItemUtils.setDisplayName(it, o.getOption().getTitle());
		ItemUtils.addLore(it, o.getOption().getDescription());
		ItemUtils.addFlag(it, ItemFlag.values());
	}

	public static boolean runOptionsMenuClick(InventoryClickEvent ev) {
		InventoryHolder holder = ev.getInventory().getHolder();
		if (holder!=null && holder instanceof Player) {
			PlayerStructure pd = PlayerStructure.GetPlayerStructure((Player)holder);
			if (pd.optionsmenu!=null) {
				if (ev.getClickedInventory().equals(pd.optionsmenu.getInventory())) {
					pd.optionsmenu.checkForClick(ev.getSlot());
				}
				return false;
			}
		}
		return true;
	}

	private void checkForClick(int slot) {
		for (Option o : options) {
			for (Integer i : o.getPositions()) {
				if (i==slot) {
					PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
					if (o.getOption().getTitle().equalsIgnoreCase("DPS Tracking") && pd.dpstrackinglocked) {
						return;
					}
					boolean val = o.getOption().isOptionEnabled(p, true);
					doExtraThings(o.getOption());
					p.sendMessage(o.getOption().getTitle()+" is now turned "+(val?ChatColor.GREEN+"ON":ChatColor.RED+"OFF")+ChatColor.RESET+".");
					PopulateInterface(p);
					return;
				}
			}
		}
	}

	private void doExtraThings(OptionName o) {
		if (o==OptionName.DPS && !o.isOptionEnabled(p, false)) {
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			p.sendMessage(pd.damagedata.OutputResults());
		}
	}
	
	private Inventory getInventory() {
		return inv;
	}
}
class Option{
	int iconpos;
	int torchpos;
	OptionName option;
	
	Option(OptionName option, int iconslot, int torchslot) {
		this.option=option;
		this.iconpos=iconslot;
		this.torchpos=torchslot;
	}
	
	OptionName getOption() {
		return option;
	}
	int[] getPositions() {
		return new int[]{iconpos,torchpos};
	}
	Material getTorchIcon(Player p) {
		return (getOption().isOptionEnabled(p,false)?Material.SLIME_BALL:Material.FIREBALL);
	}
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Option(option=");
		sb.append(option);
		sb.append(",iconpos=");
		sb.append(iconpos);
		sb.append(",torchpos=");
		sb.append(torchpos);
		sb.append(")");
		return sb.toString();
	}
}

enum OptionName{
	DPSDISPLAY("Damage Number Display",ChatColor.GRAY+"Toggles the display of Damage Numbers on/off.",Material.SKULL_ITEM,(byte)0),
	DPS("DPS Tracking",ChatColor.GRAY+"Toggles the tracking of damage stats on/off.\nTurning it off reports the last damage session breakdown.",Material.WRITTEN_BOOK,(byte)0),
	HEALTHBARDISPLAY("Healthbar Display",ChatColor.GRAY+"Toggles the healthbar near the player's cursor\nwhen attacking or getting hit by mobs.",Material.BED,(byte)0),
	AUTOEQUIPARMOR("Auto-Equip Armor",ChatColor.GRAY+"Toggles automatically equipping appropriate armor.",Material.LEATHER_CHESTPLATE,(byte)0),
	AUTOEQUIPWEAPON("Auto-Equip Weapon",ChatColor.GRAY+"Toggles automatically equipping appropriate weapons.",Material.IRON_SWORD,(byte)0),
	SOUNDS("Login/Logout Sounds",ChatColor.GRAY+"Toggles the playing of login/logout sound\nnotifications as well as message sound notifications.",Material.RECORD_7,(byte)0),
	MOUSEOVERHEALTHBAR("Mouseover Healthbar",ChatColor.GRAY+"Toggles the display of a mob's healthbar without having to attack it.",Material.WATCH,(byte)0);
	
	String title;
	String description;
	MaterialData data;
	
	OptionName(String title, String desc, Material icon, byte data) {
		this.data = new MaterialData(icon,data);
		this.title=title;
		this.description=desc;
	}
	
	MaterialData getMaterialData() {
		return data;
	}
	
	boolean isOptionEnabled(Player p, boolean toggle) {
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		switch (this) {
			case DPSDISPLAY:{
				if (toggle) {
					pd.damagenumbers=!pd.damagenumbers;
				}
				return pd.damagenumbers;
			}
			case DPS:{
				if (toggle) {
					pd.damagelogging=!pd.damagelogging;
				}
				return pd.damagelogging;
			}
			case AUTOEQUIPARMOR:{
				if (toggle) {
					pd.equiparmor=!pd.equiparmor;
				}
				return pd.equiparmor;
			}
			case AUTOEQUIPWEAPON:{
				if (toggle) {
					pd.equipweapons=!pd.equipweapons;
				}
				return pd.equipweapons;
			}
			case SOUNDS:{
				if (toggle) {
					pd.sounds_enabled=!pd.sounds_enabled;
				}
				return pd.sounds_enabled;
			}
			case HEALTHBARDISPLAY:{
				if (toggle) {
					pd.healthbardisplay=!pd.healthbardisplay;
				}
				return pd.healthbardisplay;
			}
			case MOUSEOVERHEALTHBAR:{
				if (toggle) {
					pd.mouseoverhealthbar=!pd.mouseoverhealthbar;
				}
				return pd.mouseoverhealthbar;
			}
		}
		TwosideKeeper.log("WARNING! Value for Option "+title+" does not exist!", 1);
		return false;
	}
	
	String getTitle() {
		return title;
	}
	
	String getDescription() {
		return description;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("OptionName(title=");
		sb.append(title);
		sb.append(",description=");
		sb.append(description);
		sb.append(",data=");
		sb.append(data);
		sb.append(")");
		return sb.toString();
	}
}