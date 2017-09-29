package sig.plugin.TwosideKeeper.HelperStructures.Utils;

import org.bukkit.util.EulerAngle;

public class MathUtils {
	public static EulerAngle getEulerAngleDegrees(double degX,double degY,double degZ) {
		return new EulerAngle(Math.toRadians(degX),Math.toRadians(degY),Math.toRadians(degZ));
	}
}
