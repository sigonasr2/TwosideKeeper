package sig.plugin.TwosideKeeper.Rooms;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.potion.PotionEffectType;

import sig.plugin.TwosideKeeper.PlayerStructure;
import sig.plugin.TwosideKeeper.Room;
import sig.plugin.TwosideKeeper.Generators.DPSRoom;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;

public class DPSChallengeRoom extends Room{
	Player p;
	double dmg;
	long expireTime;
	boolean started=false;
	List<LivingEntity> mobs = new ArrayList<LivingEntity>();
	World instance;
	
	int ROOM_WIDTH;
	int ROOM_LENGTH;

	public DPSChallengeRoom(Player p, ChunkGenerator generator) {
		super(generator);
		this.p=p;
		this.dmg=0;
		this.expireTime=0;
		this.started=false;
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		pd.locBeforeInstance = p.getLocation().clone();
		instance = Bukkit.getWorld(id);
		ROOM_WIDTH=((DPSRoom)generator).getRoomWidth();
		ROOM_LENGTH=((DPSRoom)generator).getRoomLength();
		p.teleport(new Location(instance,ROOM_WIDTH/2,64,ROOM_LENGTH/2));
		GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.LEVITATION, 20*3, -30, p, true);
		setupChallengeRoom();
	}

	private void setupChallengeRoom() {
		
	}

	public boolean runTick() {
		if (p!=null && p.isValid()) {
			return true;
		} else {
			return false;
		}
	}
	
	public void cleanup() {
		super.cleanup();
		if (p!=null && p.isValid()) {
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			p.teleport(pd.locBeforeInstance);
		}
	}
}
