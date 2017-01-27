package sig.plugin.TwosideKeeper.HelperStructures.Common;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.MovementUtils;

public class Camera {
	//ArmorStand camera_ent;
	double degrees=0;
	HashMap<UUID,Location> camera_viewerlocs = new HashMap<UUID,Location>();
	Chunk startingchunk;
	Location startedat;
	public Camera(Location startingloc,Player...viewers) {
		startingchunk = startingloc.getChunk();
		startedat=startingloc.clone();
		TwosideKeeper.temporary_chunks.add(startingchunk);
		startingchunk.load();
		for (Player p : viewers) {
			AddCameraViewer(p);
		}
	}
	public boolean containsViewer(Player p) {
		return (camera_viewerlocs.containsKey(p.getUniqueId()));
	}
	public void AddCameraViewer(Player p) {
		camera_viewerlocs.put(p.getUniqueId(), p.getLocation().clone());
		//TwosideKeeper.log("Set starting loc of "+p.getName()+" to "+camera_viewerlocs.get(p.getUniqueId()), 1);
		p.setGameMode(GameMode.SPECTATOR);
		p.setSpectatorTarget(null);
	}
	public Location getStartingLoc(Player p) {
		if (containsViewer(p)) {
			return camera_viewerlocs.get(p.getUniqueId());
		} else {
			return null;
		}
	}
	public void removeCameraViewer(Player p) {
		if (camera_viewerlocs.containsKey(p.getUniqueId())) {
			p.setSpectatorTarget(null);
			p.setGameMode(GameMode.SURVIVAL);
			p.teleport(camera_viewerlocs.get(p.getUniqueId()));
			//TwosideKeeper.log("Teleporting player "+p.getName()+" to "+camera_viewerlocs.get(p.getUniqueId()), 1);
			camera_viewerlocs.remove(p.getUniqueId());
		}
	}
	public boolean runTick() {
		if (camera_viewerlocs.size()==0) {
			return false;
		}
		for (UUID id : camera_viewerlocs.keySet()) {
			Player p = Bukkit.getPlayer(id);
			if (p!=null && p.isValid()) {
				p.setGameMode(GameMode.SPECTATOR);
				p.setFlying(true);
				p.setSpectatorTarget(p);
			} else {
				TwosideKeeper.ScheduleRemoval(camera_viewerlocs, p);
			}
			//TwosideKeeper.log("offset: "+camera_ent.getLocation().subtract(startedat)+" || Player offset: "+p.getLocation().subtract(startedat), 0);
		}
		return true;
	}
	public void Cleanup() {
		Cleanup(false);
	}
	public void Cleanup(boolean isShuttingDown) {
		for (UUID id : camera_viewerlocs.keySet()) {
			Player p = Bukkit.getPlayer(id);
			if (p!=null && p.isValid()) {
				p.setSpectatorTarget(null);
				p.setGameMode(GameMode.SURVIVAL);
				p.teleport(camera_viewerlocs.get(id));
				if (!isShuttingDown) {
					TwosideKeeper.ScheduleRemoval(camera_viewerlocs, p);
				}
			} else {
				if (!isShuttingDown) {
					TwosideKeeper.ScheduleRemoval(camera_viewerlocs, p);
				}
			}
		}
		if (!isShuttingDown) {
			TwosideKeeper.temporary_chunks.remove(startingchunk);
		}
		removeAllCameraViewers();
	}
	
	private void removeAllCameraViewers() {
		camera_viewerlocs.clear();
	}
	public void pointCameraToLocation(Location fromLoc, Location toLoc) {
		//Points a camera to the target location.
		if (fromLoc!=null && toLoc!=null && toLoc.getWorld().equals(fromLoc.getWorld())) {
			Vector dir = MovementUtils.pointTowardsLocation(fromLoc, toLoc);
			Location currloc = fromLoc.clone();
			currloc.setDirection(dir);
			teleportViewers(currloc);
		}
	}
	
	private void teleportViewers(Location loc) {
		for (UUID id : camera_viewerlocs.keySet()) {
			Player p = Bukkit.getPlayer(id);
			if (p!=null && p.isValid()) {
				p.teleport(loc);
			}
		}
	}
	public void setCameraAroundCircle(double radius, double degrees, Location loc, double yoffset) {
		double velx = Math.cos(Math.toRadians(degrees));
		//double vely = Math.sin(Math.toRadians(degrees));
		double velz = Math.sin(Math.toRadians(degrees));
		Vector newvector = new Vector(-velx,0,-velz);
		newvector.multiply(radius*2);
		Location finalloc = loc.clone().add(newvector).add(0,yoffset,0);
		//TwosideKeeper.log("offset: "+camera_ent.getLocation().subtract(startedat)+" -> "+finalloc.subtract(startedat),0);
		//camera_ent.teleport(finalloc);
		teleportViewers(finalloc);
		pointCameraToLocation(finalloc,loc);
	}
	
	public void rotateAroundPoint(double spd, int duration, Location loc, double radius, double yoffset) {
		duration--;
		setCameraAroundCircle(radius,degrees,loc,yoffset);
		degrees+=spd;
		if (duration>0) {
			final int dur = duration;
			Bukkit.getScheduler().runTaskLater(TwosideKeeper.plugin, ()->{rotateAroundPoint(spd,dur,loc,radius,yoffset);}, 1);
		}
	}
	
	public void rotateAroundPointWithZoom(double spd, int duration, Location loc, double startzoom, double zoomspd, double maxzoom, double yoffset) {
		duration--;
		setCameraAroundCircle(startzoom,degrees,loc,yoffset);
		degrees+=spd;
		startzoom = Math.max(maxzoom,startzoom-zoomspd);
		yoffset = Math.min(yoffset,startzoom);
		if (duration>0) {
			final int dur = duration;
			final double zoom = startzoom;
			final double y = yoffset;
			Bukkit.getScheduler().runTaskLater(TwosideKeeper.plugin, ()->{rotateAroundPointWithZoom(spd,dur,loc,zoom,zoomspd,maxzoom,y);}, 1);
		}
	}
}
