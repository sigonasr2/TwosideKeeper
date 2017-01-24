package sig.plugin.TwosideKeeper.HelperStructures.Common;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import sig.plugin.TwosideKeeper.TwosideKeeper;

public class Camera {
	ArmorStand camera_ent;
	HashMap<UUID,Location> camera_viewerlocs;
	public Camera(Location startingloc,Player...viewers) {
		camera_ent = (ArmorStand)startingloc.getWorld().spawnEntity(startingloc, EntityType.ARMOR_STAND);
		camera_ent.setGravity(false);
		camera_ent.setVisible(false);
		camera_ent.setInvulnerable(true);
		camera_ent.setArms(false);
		for (Player p : viewers) {
			AddCameraViewer(p);
		}
	}
	public void AddCameraViewer(Player p) {
		camera_viewerlocs.put(p.getUniqueId(), p.getLocation());
		p.setGameMode(GameMode.SPECTATOR);
		p.setSpectatorTarget(camera_ent);
	}
	public void removeCameraViewer(Player p) {
		if (camera_viewerlocs.containsKey(p.getUniqueId())) {
			p.setGameMode(GameMode.SURVIVAL);
			p.teleport(camera_viewerlocs.get(p.getUniqueId()));
			camera_viewerlocs.remove(p.getUniqueId());
		}
	}
	public boolean runTick() {
		if (camera_ent==null || !camera_ent.isValid() || camera_viewerlocs.size()==0) {
			return false;
		}
		for (UUID id : camera_viewerlocs.keySet()) {
			Player p = Bukkit.getPlayer(id);
			if (p!=null && p.isValid()) {
				p.setGameMode(GameMode.SPECTATOR);
				if (p.getSpectatorTarget()==null || !(p.getSpectatorTarget() instanceof ArmorStand)) {
					//If this player is on multiple cameras for some reason, we don't want to overwrite the previous camera.
					p.setSpectatorTarget(camera_ent);
				}
			} else {
				TwosideKeeper.ScheduleRemoval(camera_viewerlocs, p);
			}
		}
		return true;
	}
	public ArmorStand getEnt() {
		return camera_ent;
	}
	public void Cleanup() {
		for (UUID id : camera_viewerlocs.keySet()) {
			Player p = Bukkit.getPlayer(id);
			if (p!=null && p.isValid()) {
				p.setGameMode(GameMode.SURVIVAL);
				p.teleport(camera_viewerlocs.get(id));
			}
		}
	}
}
