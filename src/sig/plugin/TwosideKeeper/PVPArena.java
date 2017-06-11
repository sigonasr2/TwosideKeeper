package sig.plugin.TwosideKeeper;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class PVPArena {
	Location startCorner;
	Location endCorner;
	String name;
	String desc;
	
	public PVPArena(Location startCorner, Location endCorner, String arenaName, String desc) {
		this.startCorner = new Location(startCorner.getWorld(),Math.min(startCorner.getBlockX(), endCorner.getBlockX()),Math.min(startCorner.getBlockY(), endCorner.getBlockY()),Math.min(startCorner.getBlockZ(), endCorner.getBlockZ()));
		this.endCorner = new Location(startCorner.getWorld(),Math.max(startCorner.getBlockX(), endCorner.getBlockX()),Math.max(startCorner.getBlockY(), endCorner.getBlockY()),Math.max(startCorner.getBlockZ(), endCorner.getBlockZ()));
		this.name=arenaName;
		this.desc=desc;
	}
	
	public Location getStartCorner() {
		return startCorner;
	}
	public Location getEndCorner() {
		return endCorner;
	}
	public String getArenaName() {
		return name;
	}
	public boolean insideBounds(Location loc) {
		return (loc.getBlockX()>startCorner.getBlockX() &&
				loc.getBlockX()<endCorner.getBlockX() &&
				loc.getBlockY()>startCorner.getBlockY() &&
				loc.getBlockY()<endCorner.getBlockY() &&
				loc.getBlockZ()>startCorner.getBlockZ() &&
				loc.getBlockZ()<endCorner.getBlockZ());
	}
	
	public Location pickRandomLocation() {
		//Pick a random point.
		int tries=50; //Number of tries before we give up and drop them in.

		int randomx = ((int)(Math.random()*(endCorner.getBlockX()-startCorner.getBlockX()))) + 1;
		int randomz = ((int)(Math.random()*(endCorner.getBlockZ()-startCorner.getBlockZ()))) + 1;
		int y = endCorner.getBlockY()-startCorner.getBlockY()-1;
		Location finalloc = null;
		while (tries>0) {
			//Find a safe Y Location
			int ytries=50;
			while (ytries>0) {
				Block testBlock = startCorner.clone().add(randomx,y-1,randomz).getBlock();
				if (testBlock.isLiquid() || !testBlock.getType().isSolid()) {
					y--;
					ytries--;
				} else {
					break;
				}
			}
			finalloc = new Location(startCorner.getWorld(),
					startCorner.getBlockX()+randomx,
					startCorner.getBlockY()+y,
					startCorner.getBlockZ()+randomz);
			if (!finalloc.getBlock().isLiquid() &&
					insideBounds(finalloc)) {
				return finalloc.clone();
			} else {
				tries--;
				randomx = ((int)(Math.random()*(endCorner.getBlockX()-startCorner.getBlockX()))) + 1;
				randomz = ((int)(Math.random()*(endCorner.getBlockZ()-startCorner.getBlockZ()))) + 1;
				y = endCorner.getBlockX()-startCorner.getBlockX()-1;
			}
		}
		TwosideKeeper.log("WARNING! Could not find a safe random location. Dropping them in with what we got...", 1);
		return finalloc.clone();
	}
	
	public String getDescription() {
		return desc;
	}
	
	public TextComponent getComponent(int index) {
		TextComponent tc = new TextComponent("["+name+"]");
		tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/pvp _ARENA_ "+(9000+index)+""));
		tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ComponentBuilder(desc).create()));
		return tc;
	}
}
