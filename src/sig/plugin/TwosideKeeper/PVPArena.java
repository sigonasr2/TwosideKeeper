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
	Span arenabounds;
	String name;
	String desc;
	List<Location> safelocs;
	List<Span> spawnlocs;
	List<Span> team1spawnlocs;
	List<Span> team2spawnlocs;
	
	public PVPArena(Span bounds, String arenaName, String desc) {
		this.arenabounds=bounds;
		this.name=arenaName;
		this.desc=desc;
		this.safelocs = new ArrayList<Location>();
		this.spawnlocs = new ArrayList<Span>();
		this.team1spawnlocs = new ArrayList<Span>();
		this.team2spawnlocs = new ArrayList<Span>();
	}
	
	public Location getStartCorner() {
		return arenabounds.getStartCorner();
	}
	public Location getEndCorner() {
		return arenabounds.getEndCorner();
	}
	public String getArenaName() {
		return name;
	}
	public void addSpawnLocation(Span loc) {
		spawnlocs.add(loc);
	}
	public void addTeamSpawnLocation(Span loc, int team) {
		if (team==1) {
			team1spawnlocs.add(loc);
		} else {
			team2spawnlocs.add(loc);
		}
	}
	public boolean insideBounds(Location loc) {
		return arenabounds.insideBounds(loc);
	}
	
	public Location pickRandomLocation() {
		if (spawnlocs.size()>0) {
			return spawnlocs.get((int)(Math.random()*spawnlocs.size())).pickRandomLocation();
		} else {
			return arenabounds.pickRandomLocation();
		}
	}
	
	public Location pickRandomTeamLocation(int team) {
		if (team==1) {
			if (team1spawnlocs.size()>0) {
				return team1spawnlocs.get((int)(Math.random()*team1spawnlocs.size())).pickRandomLocation();
			}
		} else {
			if (team2spawnlocs.size()>0) {
				return team2spawnlocs.get((int)(Math.random()*team2spawnlocs.size())).pickRandomLocation();
			}
		}
		if (spawnlocs.size()>0) {
			return spawnlocs.get((int)(Math.random()*spawnlocs.size())).pickRandomLocation();
		} else {
			return arenabounds.pickRandomLocation();
		}
	}
	
	public String getDescription() {
		return desc;
	}
	
	public TextComponent getComponent(int index) {
		TextComponent tc = new TextComponent("["+name+"]");
		tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/pvp _ARENA_ "+(9000-index)+""));
		tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ComponentBuilder(desc).create()));
		return tc;
	}
	
	public String getDataString() {
		StringBuilder sb = new StringBuilder("");
		sb.append(arenabounds.getDataString());
		sb.append(",");
		sb.append(name);
		sb.append(",");
		sb.append(desc);
		for (Span s : spawnlocs) {
			sb.append(",");
			sb.append(s.getDataString());
			sb.append(",");
			sb.append("SPAWN");
		}
		for (Span s : team1spawnlocs) {
			sb.append(",");
			sb.append(s.getDataString());
			sb.append(",");
			sb.append("TEAM1");
		}
		for (Span s : team2spawnlocs) {
			sb.append(",");
			sb.append(s.getDataString());
			sb.append(",");
			sb.append("TEAM2");
		}
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
		sb.append("arenabounds.getStartCorner()=");
		sb.append(arenabounds.getStartCorner());
		sb.append(",");
		sb.append("arenabounds.getEndCorner()=");
		sb.append(arenabounds.getEndCorner());
		sb.append("}");
		return sb.toString();
	}
}

class Span {
	Location startCorner;
	Location endCorner;
	List<Location> safelocs;
	Span(Location startCorner, Location endCorner) {
		this.startCorner = new Location(startCorner.getWorld(),Math.min(startCorner.getBlockX(), endCorner.getBlockX()),Math.min(startCorner.getBlockY(), endCorner.getBlockY()),Math.min(startCorner.getBlockZ(), endCorner.getBlockZ()));
		this.endCorner = new Location(startCorner.getWorld(),Math.max(startCorner.getBlockX(), endCorner.getBlockX()),Math.max(startCorner.getBlockY(), endCorner.getBlockY()),Math.max(startCorner.getBlockZ(), endCorner.getBlockZ()));
		this.safelocs = new ArrayList<Location>();
	}
	
	Location getStartCorner() {
		return startCorner.clone();
	}
	Location getEndCorner() {
		return endCorner.clone();
	}
	boolean insideBounds(Location loc) {
		return insideBounds(loc,false);
	}
	boolean insideBounds(Location loc, boolean includeEdges) {
		if (includeEdges) {
			return (loc.getBlockX()>=startCorner.getBlockX() &&
					loc.getBlockX()<=endCorner.getBlockX() &&
					loc.getBlockY()>=startCorner.getBlockY() &&
					loc.getBlockY()<=endCorner.getBlockY() &&
					loc.getBlockZ()>=startCorner.getBlockZ() &&
					loc.getBlockZ()<=endCorner.getBlockZ());
		} else {
			return (loc.getBlockX()>startCorner.getBlockX() &&
					loc.getBlockX()<endCorner.getBlockX() &&
					loc.getBlockY()>startCorner.getBlockY() &&
					loc.getBlockY()<endCorner.getBlockY() &&
					loc.getBlockZ()>startCorner.getBlockZ() &&
					loc.getBlockZ()<endCorner.getBlockZ());
		}
	}
	public String toString() {
		StringBuilder sb = new StringBuilder("");
		sb.append("Span{");
		sb.append("startCorner=");
		sb.append(getStartCorner());
		sb.append(",");
		sb.append("endCorner()=");
		sb.append(getEndCorner());
		sb.append("}");
		return sb.toString();
	}
	String getDataString() {
		StringBuilder sb = new StringBuilder("");
		sb.append(getStartCorner().getWorld().getName());
		sb.append(",");
		sb.append(getStartCorner().getX());
		sb.append(",");
		sb.append(getStartCorner().getY());
		sb.append(",");
		sb.append(getStartCorner().getZ());
		sb.append(",");
		sb.append(getEndCorner().getWorld().getName());
		sb.append(",");
		sb.append(getEndCorner().getX());
		sb.append(",");
		sb.append(getEndCorner().getY());
		sb.append(",");
		sb.append(getEndCorner().getZ());
		return sb.toString();
	}

	Location pickRandomLocation() {
		//Pick a random point.
		int tries=500; //Number of tries before we give up and drop them in.

		int randomx = ((int)(Math.random()*(getEndCorner().getBlockX()-getStartCorner().getBlockX()))) + 1;
		int randomz = ((int)(Math.random()*(getEndCorner().getBlockZ()-getStartCorner().getBlockZ()))) + 1;
		int y = getEndCorner().getBlockY()-getStartCorner().getBlockY()-2;
		Location finalloc = null;
		while (tries>0) {
			//Find a safe Y Location
			int ytries=50;
			while (ytries>0) {
				Block testBlock = getStartCorner().clone().add(randomx+0.5,y-1,randomz+0.5).getBlock();
				if (testBlock.isLiquid() || !testBlock.getType().isOccluding()) {
					y--;
					ytries--;
				} else {
					break;
				}
			}
			finalloc = new Location(getStartCorner().getWorld(),
					getStartCorner().getBlockX()+randomx+0.5,
					getStartCorner().getBlockY()+y,
					getStartCorner().getBlockZ()+randomz+0.5);
			if (!finalloc.getBlock().isLiquid() &&
					finalloc.getBlock().getRelative(0, 1, 0).getType()==Material.AIR &&
					insideBounds(finalloc,true)) {
				/*TwosideKeeper.log("Final Block is "+finalloc.getBlock(), 1);
				TwosideKeeper.log("Final Block Above is "+finalloc.getBlock().getRelative(0, 1, 0), 1);
				TwosideKeeper.log("Final Block Below is "+finalloc.getBlock().getRelative(0, -1, 0), 1);*/
				if (safelocs.size()<20) {
					safelocs.add(finalloc.clone());
				}
				return finalloc.clone();
			} else {
				tries--;
				randomx = ((int)(Math.random()*(getEndCorner().getBlockX()-getStartCorner().getBlockX()))) + 1;
				randomz = ((int)(Math.random()*(getEndCorner().getBlockZ()-getStartCorner().getBlockZ()))) + 1;
				y = getEndCorner().getBlockX()-getStartCorner().getBlockX()-1;
			}
		}
		if (safelocs.size()>0) {
			finalloc = safelocs.get((int)(Math.random()*safelocs.size()));
		} else {
			TwosideKeeper.log("WARNING! Could not find a safe random location. Dropping them in with what we got...", 1);
		}
		return finalloc.clone();
	}
}

enum SpawnType{
	NORMAL,
	TEAM1,
	TEAM2
}