package sig.plugin.TwosideKeeper.HelperStructures;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import sig.plugin.TwosideKeeper.LivingEntityStructure;
import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.MovementUtils;

public class Pet {
	final static public int LEASHRANGE = 16;
	final static public int LEASHRANGE_SQUARED = LEASHRANGE*LEASHRANGE;
	Player owner;
	Location targetLoc;
	double moveSpd = 1;
	LivingEntity ent;
	String name="";
	
	public Pet(Player owner, EntityType ent, String nickname) {
		try {
			this.ent = (LivingEntity)owner.getLocation().getWorld().spawnEntity(owner.getLocation(), ent);
			this.owner = owner;
			this.name = nickname;
			this.setNickname(nickname);
			LivingEntityStructure les = LivingEntityStructure.GetLivingEntityStructure(this.ent);
			les.isPet=true;
			les.petOwner=owner;
		} catch (ClassCastException e) {
			TwosideKeeper.log("ERROR! Specified invalid Entity Type when creating Pet!", 0);
			e.printStackTrace();
		}
	}
	
	public void run() {
		if (owner==null || !owner.isValid()) {
			cleanup();
			return;
		}
		if (owner.getLocation().distanceSquared(ent.getLocation())>LEASHRANGE_SQUARED) {
			ent.teleport(owner.getLocation());
		}
		if (targetLoc!=null && ent.getLocation().distanceSquared(targetLoc)>1) {
			Vector dirToMove = MovementUtils.getVelocityTowardsLocation(ent.getLocation(), targetLoc, moveSpd);
			ent.setVelocity(dirToMove);
		}
	}
	
	public void setTargetLocation(Location loc) {
		this.targetLoc=loc;
	}
	
	public void setMoveSpeed(double spd) {
		this.moveSpd = spd;
	}
	
	public void setNickname(String nickname) {
		this.name=nickname;
		this.ent.setCustomName(nickname);
	}
	
	public String getNickname() {
		return name;
	}
	
	public Location getTargetLocation() {
		return targetLoc;
	}
	
	public LivingEntity getEntity() {
		return ent;
	}
	
	public void cleanup() {
		ent.remove();
	}
}
