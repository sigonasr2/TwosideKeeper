package sig.plugin.TwosideKeeper;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.generator.ChunkGenerator;

public class Room {
	public String id;
	ChunkGenerator generator;
	
	public Room(ChunkGenerator generator) {
		id = "Instance"+(TwosideKeeper.ROOM_ID++);
		WorldCreator room = new WorldCreator(id);
		room.generator(generator);
		Bukkit.createWorld(room);
		TwosideKeeper.roominstances.add(this);
	}
	
	public void killWorld() {
		Bukkit.unloadWorld(id, false);
		Bukkit.getScheduler().runTaskLater(TwosideKeeper.plugin, ()->{
			File world = new File(TwosideKeeper.plugin.getDataFolder()+"/../../"+id);
			FileUtils.deleteQuietly(world);
		}, 100);
	}
	
	public boolean runTick() {
		return true;
	}

	public void cleanup() {
		killWorld();
	}
}
