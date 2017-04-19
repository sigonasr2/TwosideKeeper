package sig.plugin.TwosideKeeper.HelperStructures.Utils.Classes;

import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

public class MovementModifier implements Runnable{
	LivingEntity l;
	Vector v;
	
	public MovementModifier(LivingEntity l, Vector newvel) {
		this.l=l;
		this.v=newvel;
	}

	@Override
	public void run() {
		l.setVelocity(v);
	}

}
