package sig.plugin.TwosideKeeper;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
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
	List<Location> safelocs;
	List<Span> spawnlocs;
	
	public PVPArena(Location startCorner, Location endCorner, String arenaName, String desc) {
		this.startCorner = new Location(startCorner.getWorld(),Math.min(startCorner.getBlockX(), endCorner.getBlockX()),Math.min(startCorner.getBlockY(), endCorner.getBlockY()),Math.min(startCorner.getBlockZ(), endCorner.getBlockZ()));
		this.endCorner = new Location(startCorner.getWorld(),Math.max(startCorner.getBlockX(), endCorner.getBlockX()),Math.max(startCorner.getBlockY(), endCorner.getBlockY()),Math.max(startCorner.getBlockZ(), endCorner.getBlockZ()));
		this.name=arenaName;
		this.desc=desc;
		this.safelocs = new ArrayList<Location>();
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
		int tries=500; //Number of tries before we give up and drop them in.

		int randomx = ((int)(Math.random()*(endCorner.getBlockX()-startCorner.getBlockX()))) + 1;
		int randomz = ((int)(Math.random()*(endCorner.getBlockZ()-startCorner.getBlockZ()))) + 1;
		int y = endCorner.getBlockY()-startCorner.getBlockY()-2;
		Location finalloc = null;
		while (tries>0) {
			//Find a safe Y Location
			int ytries=50;
			while (ytries>0) {
				Block testBlock = startCorner.clone().add(randomx+0.5,y-1,randomz+0.5).getBlock();
				if (testBlock.isLiquid() || !testBlock.getType().isOccluding()) {
					y--;
					ytries--;
				} else {
					break;
				}
			}
			finalloc = new Location(startCorner.getWorld(),
					startCorner.getBlockX()+randomx+0.5,
					startCorner.getBlockY()+y,
					startCorner.getBlockZ()+randomz+0.5);
			if (!finalloc.getBlock().isLiquid() &&
					finalloc.getBlock().getRelative(0, 1, 0).getType()==Material.AIR &&
					insideBounds(finalloc)) {
				/*TwosideKeeper.log("Final Block is "+finalloc.getBlock(), 1);
				TwosideKeeper.log("Final Block Above is "+finalloc.getBlock().getRelative(0, 1, 0), 1);
				TwosideKeeper.log("Final Block Below is "+finalloc.getBlock().getRelative(0, -1, 0), 1);*/
				if (safelocs.size()<20) {
					safelocs.add(finalloc.clone());
				}
				return finalloc.clone();
			} else {
				tries--;
				randomx = ((int)(Math.random()*(endCorner.getBlockX()-startCorner.getBlockX()))) + 1;
				randomz = ((int)(Math.random()*(endCorner.getBlockZ()-startCorner.getBlockZ()))) + 1;
				y = endCorner.getBlockX()-startCorner.getBlockX()-1;
			}
		}
		if (safelocs.size()>0) {
			finalloc = safelocs.get((int)(Math.random()*safelocs.size()));
		} else {
			TwosideKeeper.log("WARNING! Could not find a safe random location. Dropping them in with what we got...", 1);
		}
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
	
	public String getDataString() {
		StringBuilder sb = new StringBuilder("");
		sb.append(startCorner.getWorld().getName());
		sb.append(",");
		sb.append(startCorner.getX());
		sb.append(",");
		sb.append(startCorner.getY());
		sb.append(",");
		sb.append(startCorner.getZ());
		sb.append(",");
		sb.append(endCorner.getWorld().getName());
		sb.append(",");
		sb.append(endCorner.getX());
		sb.append(",");
		sb.append(endCorner.getY());
		sb.append(",");
		sb.append(endCorner.getZ());
		sb.append(",");
		sb.append(name);
		sb.append(",");
		sb.append(desc);
		return sb.toString();
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder("");
		sb.append("PVPArena{");
		sb.append("name=");
		sb.append(name);
		sb.append(",");
		sb.append("desc=");
		sb.append(desc);
		sb.append(",");
		sb.append("startCorner=");
		sb.append(startCorner);
		sb.append(",");
		sb.append("endCorner=");
		sb.append(endCorner);
		sb.append("}");
		return sb.toString();
	}
}

class Span {
	Location startCorner;
	Location endCorner;
	Span(Location startCorner, Location endCorner) {
		this.startCorner = new Location(startCorner.getWorld(),Math.min(startCorner.getBlockX(), endCorner.getBlockX()),Math.min(startCorner.getBlockY(), endCorner.getBlockY()),Math.min(startCorner.getBlockZ(), endCorner.getBlockZ()));
		this.endCorner = new Location(startCorner.getWorld(),Math.max(startCorner.getBlockX(), endCorner.getBlockX()),Math.max(startCorner.getBlockY(), endCorner.getBlockY()),Math.max(startCorner.getBlockZ(), endCorner.getBlockZ()));
	}
	
	Location getStartCorner() {
		return startCorner.clone();
	}
	Location getEndCorner() {
		return endCorner.clone();
	}
}
