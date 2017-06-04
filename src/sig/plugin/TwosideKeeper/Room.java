package sig.plugin.TwosideKeeper;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.generator.ChunkGenerator;

import sig.plugin.TwosideKeeper.Drops.SigDrop;
import sig.plugin.TwosideKeeper.Generators.DPSRoom;
import sig.plugin.TwosideKeeper.HelperStructures.ArtifactItem;
import sig.plugin.TwosideKeeper.HelperStructures.CustomItem;
import sig.plugin.TwosideKeeper.HelperStructures.LivingEntityDifficulty;

public class Room {
	public String id;
	ChunkGenerator generator;
	protected World instance;
	
	protected int ROOM_WIDTH;
	protected int ROOM_LENGTH;
	
	protected Random r = new Random(Calendar.getInstance().get(Calendar.DAY_OF_YEAR)+Calendar.getInstance().get(Calendar.YEAR));
	
	public Room(ChunkGenerator generator) {
		id = "Instance"+(TwosideKeeper.ROOM_ID++);
		WorldCreator room = new WorldCreator(id);
		room.generator(generator);
		Bukkit.createWorld(room);
		TwosideKeeper.roominstances.add(this);
		instance = Bukkit.getWorld(id);
		ROOM_WIDTH=((sig.plugin.TwosideKeeper.Generators.Room)generator).getRoomWidth();
		ROOM_LENGTH=((sig.plugin.TwosideKeeper.Generators.Room)generator).getRoomLength();
	}
	
	public void killWorld() {
		Bukkit.unloadWorld(id, false);
		/*Bukkit.getScheduler().runTaskLater(TwosideKeeper.plugin, ()->{
			File world = new File(TwosideKeeper.plugin.getDataFolder()+"/../../"+id);
			FileUtils.deleteQuietly(world);
		}, 100);*/
	}
	
	public static void awardSuccessfulClear(Player p, String challengename) {
		GlobalLoot gl = GlobalLoot.spawnGlobalLoot(p.getLocation(), challengename+" Clear Box");
		gl.addNewDropInventory(p.getUniqueId(), Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE, (int)(Math.random()*3)+1));
		gl.addNewDropInventory(p.getUniqueId(), Artifact.createArtifactItem(ArtifactItem.ARTIFACT_ESSENCE, (int)(Math.random()*3)+1));
		gl.addNewDropInventory(p.getUniqueId(), new SigDrop(1,0,"",(Math.random()<=0.1)?true:false,true,(Math.random()<=0.05)?1:0,LivingEntityDifficulty.DEADLY).getItemStack());
		if (Math.random()<=0.05) {
			gl.addNewDropInventory(p.getUniqueId(), CustomItem.DailyToken());
		}
	}
	
	public boolean runTick() {
		return true;
	}

	public void cleanup() {
		killWorld();
	}

	public void runInteractEvent(PlayerInteractEvent ev) {
		
	}

	public void onHitEvent(Player p, double dmg) {
	}
	public void onLeaveEvent(Player p) {
	}

	public boolean onPlayerDeath(Player p) {
		return false;
	}

	public double getTankRoomMultiplier() {
		return 0;
	}

	public double getTankRoomBaseDamage() {
		return 0;
	}

	public double getTankRoomDamageReduction() {
		return 0;
	}
	public double getTankRoomTrueDamage() {
		return 0;
	}
	public double getTankRoomTruePctDamage() {
		return 0;
	}
}
