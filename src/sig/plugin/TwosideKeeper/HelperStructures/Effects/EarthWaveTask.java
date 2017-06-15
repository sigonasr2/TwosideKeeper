package sig.plugin.TwosideKeeper.HelperStructures.Effects;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import sig.plugin.TwosideKeeper.PVP;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;

public class EarthWaveTask implements Runnable{
	Location centerpoint;
	int radius;
	double vel;
	double dmg;
	Player damager;
	
	public EarthWaveTask(Location center, int radius, double vel, double dmg, Player damager) {
		this.centerpoint=center;
		this.radius=radius;
		this.vel=vel;
		this.dmg=dmg;
		this.damager=damager;
	}

	@Override
	public void run() {
		if (!damager.isDead()) {
			for (int x=-radius;x<=radius;x++) { //Start at the top y.
				Block b = centerpoint.getBlock().getRelative(x, 0, -radius);
				if (GenericFunctions.isSoftBlock(b) && b.getRelative(0, 1, 0).getType()==Material.AIR && !PVP.isPvPing(damager)) {
					FallingBlock fb = centerpoint.getWorld().spawnFallingBlock(b.getLocation().add(0,0,0), b.getType(), b.getData());
					fb.setVelocity(new Vector(0,vel,0));
					b.setType(Material.AIR);
					aPlugin.API.sendSoundlessExplosion(b.getLocation().add(0,1,0), 1f);
					GenericFunctions.DealDamageToNearbyMobs(b.getLocation(), dmg*2, 2, true, vel/1.5d, damager, damager.getEquipment().getItemInMainHand(), false, "Earth Wave");
				} else {
					aPlugin.API.sendSoundlessExplosion(b.getLocation().add(0,1,0), 0.1f);
					GenericFunctions.DealDamageToNearbyMobs(b.getLocation(), dmg, 2, true, vel/4d, damager, damager.getEquipment().getItemInMainHand(), false, "Earth Wave");	
				}
			}
			for (int x=-radius;x<=radius;x++) { //Start at the top y.
				Block b = centerpoint.getBlock().getRelative(x, 0, radius);
				if (GenericFunctions.isSoftBlock(b) && b.getRelative(0, 1, 0).getType()==Material.AIR && !PVP.isPvPing(damager)) {
					FallingBlock fb = centerpoint.getWorld().spawnFallingBlock(b.getLocation().add(0,0,0), b.getType(), b.getData());
					fb.setVelocity(new Vector(0,vel,0));
					b.setType(Material.AIR);
					aPlugin.API.sendSoundlessExplosion(b.getLocation().add(0,1,0), 1f);
					GenericFunctions.DealDamageToNearbyMobs(b.getLocation(), dmg*2, 2, true, vel/1.5d, damager, damager.getEquipment().getItemInMainHand(), false, "Earth Wave");
				} else {
					aPlugin.API.sendSoundlessExplosion(b.getLocation().add(0,1,0), 0.1f);
					GenericFunctions.DealDamageToNearbyMobs(b.getLocation(), dmg, 2, true, vel/4d, damager, damager.getEquipment().getItemInMainHand(), false, "Earth Wave");	
				}
			}
			for (int y=-radius+1;y<radius;y++) { //Start at the top y.
				Block b = centerpoint.getBlock().getRelative(radius, 0, y);
				if (GenericFunctions.isSoftBlock(b) && b.getRelative(0, 1, 0).getType()==Material.AIR && !PVP.isPvPing(damager)) {
					FallingBlock fb = centerpoint.getWorld().spawnFallingBlock(b.getLocation().add(0,0,0), b.getType(), b.getData());
					fb.setVelocity(new Vector(0,vel,0));
					b.setType(Material.AIR);
					aPlugin.API.sendSoundlessExplosion(b.getLocation().add(0,1,0), 1f);
					GenericFunctions.DealDamageToNearbyMobs(b.getLocation(), dmg*2, 2, true, vel/1.5d, damager, damager.getEquipment().getItemInMainHand(), false, "Earth Wave");
				} else {
					aPlugin.API.sendSoundlessExplosion(b.getLocation().add(0,1,0), 0.1f);
					GenericFunctions.DealDamageToNearbyMobs(b.getLocation(), dmg, 2, true, vel/4d, damager, damager.getEquipment().getItemInMainHand(), false, "Earth Wave");	
				}
			}
			for (int y=-radius+1;y<radius;y++) { //Start at the top y.
				Block b = centerpoint.getBlock().getRelative(-radius, 0, y);
				if (GenericFunctions.isSoftBlock(b) && b.getRelative(0, 1, 0).getType()==Material.AIR && !PVP.isPvPing(damager)) {
					FallingBlock fb = centerpoint.getWorld().spawnFallingBlock(b.getLocation().add(0,0,0), b.getType(), b.getData());
					fb.setVelocity(new Vector(0,vel,0));
					b.setType(Material.AIR);
					aPlugin.API.sendSoundlessExplosion(b.getLocation().add(0,1,0), 1f);
					GenericFunctions.DealDamageToNearbyMobs(b.getLocation(), dmg*2, 2, true, vel/1.5d, damager, damager.getEquipment().getItemInMainHand(), false, "Earth Wave");
				} else {
					aPlugin.API.sendSoundlessExplosion(b.getLocation().add(0,1,0), 0.1f);
					GenericFunctions.DealDamageToNearbyMobs(b.getLocation(), dmg, 2, true, vel/4d, damager, damager.getEquipment().getItemInMainHand(), false, "Earth Wave");	
				}
			}
		}
	}

}
