package sig.plugin.TwosideKeeper.HelperStructures.Utils;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import sig.plugin.TwosideKeeper.TwosideKeeper;

public class MovementUtils {
	public static Vector moveTowardsLocation(Location currloc, Location targetloc, double spd) {
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
		double angle2 = Math.atan2(c, a);
		double velz = spd * Math.sin(angle);
		double velx = spd * Math.cos(angle);
		double vely = spd * Math.sin(angle2);
		//TwosideKeeper.log("New angle is "+angle, 0);
		return new Vector(-velx,-vely,-velz);
	}
}
