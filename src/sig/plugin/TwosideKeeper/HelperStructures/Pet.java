package sig.plugin.TwosideKeeper.HelperStructures;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import sig.plugin.TwosideKeeper.CustomDamage;
import sig.plugin.TwosideKeeper.LivingEntityStructure;
import sig.plugin.TwosideKeeper.PlayerStructure;
import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.MovementUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.PlayerUtils;

public class Pet {
	final static public int LEASHRANGE = 16;
	final static public int LEASHRANGE_SQUARED = LEASHRANGE*LEASHRANGE;
	Player owner;
	Location targetLoc;
	Location lastPos;
	double moveSpd = 1;
	double jumpHeight = 3.0;
	int attackRate = 10; 
	long lastAttacked = 0;
	LivingEntity ent;
	String name="";
	int stuckTimer=0;
	LivingEntity myTarget=null;
	
	PetState myState = PetState.PASSIVE;
	double myDamage = 5;
	
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
		if (owner==null || !owner.isValid() || ent==null || !ent.isValid()) {
			cleanup();
			return;
		}
		
		grabEnemyTarget();
		
		switch (myState) {
			case PASSIVE:{
				if (myTarget!=null && !myTarget.isValid()) {
					myTarget=null;
					//TwosideKeeper.log("My Target is now "+GenericFunctions.GetEntityDisplayName(myTarget), 1);
				} else 
				if (myTarget!=null) { //This is a valid target. If we are too far away, move towards it. Perform an attack when close enough if possible.
					if (ent.getLocation().distanceSquared(myTarget.getLocation())>2) {
						setTargetLocation(myTarget.getLocation());
						setState(PetState.MOVING);
					} else {
						//We are in attack range. Prepare an attack.
						if (lastAttacked+attackRate<=TwosideKeeper.getServerTickTime()) {
							myState=PetState.ATTACKING;
							lastAttacked=TwosideKeeper.getServerTickTime();
						}
					}
				}
				if (owner.getLocation().distanceSquared(ent.getLocation())>LEASHRANGE_SQUARED) {
					ent.teleport(owner.getLocation());
				}
			}break;
			case MOVING:{
				if (targetLoc!=null && ent.getLocation().distanceSquared(targetLoc)>2) {
					Location loc = MoveTowardsPoint(targetLoc);
					if (lastPos!=null) {
						if (lastPos.distanceSquared(ent.getLocation())<4) {
							stuckTimer++;
							//TwosideKeeper.log("Stuck Timer: "+stuckTimer, 1);
						} else {
							stuckTimer=0;
							lastPos=null;
							setState(PetState.PASSIVE);
						}
					} else {
						lastPos = loc.clone();
					}
				}
				if (stuckTimer==20) {
					//Try jumping.
					ent.setVelocity(new Vector(0,jumpHeight,0));
					stuckTimer++;
				} else
				if (stuckTimer==50) {
					ent.teleport(owner);
					stuckTimer=0;
					lastPos=null;
					targetLoc=null;
					setState(PetState.PASSIVE);
				}
				if (targetLoc!=null && ent.getLocation().distanceSquared(targetLoc)<=2) {
					targetLoc=null;
					setState(PetState.PASSIVE);
				}
				if (targetLoc==null) {
					setState(PetState.PASSIVE);
				}
			}break;
			case ATTACKING:{
				if (myTarget!=null) {
					MoveTowardsPoint(myTarget.getLocation(),0.4);
					//Deal damage.
					CustomDamage.ApplyDamage(myDamage, ent, myTarget, null, "Pet Attack");
				}
				setState(PetState.PASSIVE);
			}break;
		}
	}

	private Location MoveTowardsPoint(Location targetLoc) {
		return MoveTowardsPoint(targetLoc,1);
	}
	
	private Location MoveTowardsPoint(Location targetLoc, double mult) {
		Vector dirToMove = MovementUtils.getVelocityTowardsLocation(ent.getLocation(), targetLoc, moveSpd*mult);
		Location loc = ent.getLocation();
		loc.setDirection(dirToMove);
		ent.setVelocity(dirToMove);
		ent.teleport(loc);
		return loc;
	}
	
	public void setTarget(LivingEntity target) {
		setTarget(target,false);
	}
	
	public void setTarget(LivingEntity target, boolean force) {
		if (myTarget==null || myTarget.getLocation().distanceSquared(ent.getLocation())>64 || force) {
			myTarget = target;
		}
	}

	private void grabEnemyTarget() {
		if (myTarget!=null){
			if (myTarget.isValid()) {
				if (!myTarget.getWorld().equals(ent.getWorld())) {
					myTarget=null;
					return;
				}
				if (myTarget.getLocation().distanceSquared(ent.getLocation())>64) {
					myTarget=null;
					return;
				}
			}
		}
		if (ent.hasAI() && isFighting()) {
			ent.setAI(false);
			if (myTarget==null || !myTarget.isValid()) {
				//Attempt to find a target. Try the owner's target first.
				PlayerStructure pd = PlayerStructure.GetPlayerStructure(owner);
				myTarget = pd.target;
				if (myTarget==null) {
					//Try to find a nearby target.
					List<LivingEntity> ents = GenericFunctions.getNearbyMonsters(ent.getLocation(), 4);
					double closestrange = Integer.MAX_VALUE;
					LivingEntity selectedent = null;
					for (int i=0;i<ents.size();i++) {
						LivingEntity ent = ents.get(i);
						if (ent==this.ent || LivingEntityStructure.isFriendly(owner, ent)) {
							ents.remove(i--);
						} else {
							double dist = ent.getLocation().distanceSquared(this.ent.getLocation());
							if (closestrange>dist) {
								closestrange = dist;
								selectedent = ent;
							}
						}
					}
					if (selectedent!=null) {
						myTarget=selectedent;
					}
				}
				//TwosideKeeper.log("My Target is now "+GenericFunctions.GetEntityDisplayName(myTarget), 1);
			}
		} else
		if (!ent.hasAI() && !isFighting()) {
			ent.setAI(true);
		}
	}
	
	public PetState getState() {
		return myState;
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
	
	public boolean isFighting() {
		if (owner!=null && PlayerUtils.PlayerIsInCombat(owner)) {
			return true;
		} else {
			return false;
		}
	}

	public void setState(PetState state) {
		myState = state;
	}
}
