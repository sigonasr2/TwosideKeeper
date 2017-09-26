package sig.plugin.TwosideKeeper.HelperStructures.Utils;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

public class MovementUtils {
	public static Vector getVelocityTowardsLocation(Location currloc, Location targetloc, double spd) {
		/*double deltax = currloc.getX()-targetloc.getX();
		double deltay = currloc.getY()-targetloc.getY();
		double deltaz = currloc.getZ()-targetloc.getZ();
		//Move at max speed in each direction until it matches the delta.
		double velx = Math.min(Math.abs(deltax), spd)*Math.signum(deltax);
		double vely = Math.min(Math.abs(deltax), spd)*Math.signum(deltay);
		double velz = Math.min(Math.abs(deltax), spd)*Math.signum(deltaz);
		return new Vector(-velx,-vely,-velz); //Flip the sign so we're moving towards the point instead of away from it.*/
		double a = currloc.getX()-targetloc.getX();
		double c = currloc.getY()-targetloc.getY();
		double b = currloc.getZ()-targetloc.getZ();
		double angle = Math.atan2(b, a);
		double angle2 = Math.atan2(c, Math.sqrt(Math.pow(a,2)+Math.pow(b, 2)));
		double velz = spd * Math.sin(angle);
		double velx = spd * Math.cos(angle);
		double vely = spd * Math.sin(angle2);
		//TwosideKeeper.log("New angle is "+angle, 0);
		return new Vector(-velx,-vely,-velz);
	}
	/**
	 * Returns a vector pointing from the given location to the target location.
	 */
	public static Vector pointTowardsLocation(Location currloc, Location targetloc) {
		/*double deltax = currloc.getX()-targetloc.getX();
		double deltay = currloc.getY()-targetloc.getY();
		double deltaz = currloc.getZ()-targetloc.getZ();
		//Move at max speed in each direction until it matches the delta.
		double velx = Math.min(Math.abs(deltax), spd)*Math.signum(deltax);
		double vely = Math.min(Math.abs(deltax), spd)*Math.signum(deltay);
		double velz = Math.min(Math.abs(deltax), spd)*Math.signum(deltaz);
		return new Vector(-velx,-vely,-velz); //Flip the sign so we're moving towards the point instead of away from it.*/
		double a = currloc.getX()-targetloc.getX();
		double c = currloc.getY()-targetloc.getY();
		double b = currloc.getZ()-targetloc.getZ();
		double angle = Math.atan2(b, a);
		double angle2 = Math.atan2(c, Math.sqrt(Math.pow(a,2)+Math.pow(b, 2)));
		double velz = Math.sin(angle);
		double velx = Math.cos(angle);
		double vely = Math.sin(angle2);
		//TwosideKeeper.log("New angle is "+angle, 0);
		return new Vector(-velx,-vely,-velz);
	}
	
	/**
	 * Returns an array with a size of two elements:
	 * One pointing 45 degrees to the right of the specified direction.
	 * One pointing 45 degrees to the left of the specified direction.
	 */
	public static BlockFace[] get45DegreeDirections(BlockFace dir) {
		int slotfound = 0;
		for (int i=0;i<EntityUtils.faces.length;i++) {
			if (EntityUtils.faces[i].equals(dir)) {
				slotfound=i;
				break;
			}
		}
		return new BlockFace[]{EntityUtils.faces[(slotfound+1)%EntityUtils.faces.length],EntityUtils.faces[Math.floorMod((slotfound-1),EntityUtils.faces.length)]};
	}
	
	/**
	 * Returns an array with a size of two elements:
	 * One pointing 90 degrees to the right of the specified direction.
	 * One pointing 90 degrees to the left of the specified direction.
	 */
	public static BlockFace[] get90DegreeDirections(BlockFace dir) {
		int slotfound = 0;
		for (int i=0;i<EntityUtils.faces.length;i++) {
			if (EntityUtils.faces[i].equals(dir)) {
				slotfound=i;
				break;
			}
		}
		return new BlockFace[]{EntityUtils.faces[(slotfound+2)%EntityUtils.faces.length],EntityUtils.faces[Math.floorMod((slotfound-2),EntityUtils.faces.length)]};
	}
}
