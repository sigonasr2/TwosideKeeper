package sig.plugin.TwosideKeeper.HelperStructures.Common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import net.md_5.bungee.api.ChatColor;
import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.HelperStructures.ServerType;

public class Habitation {
	public HashMap<String,Integer> locationhashes;
	public HashMap<UUID,Location> startinglocs;
	
	public Habitation() {
		locationhashes = new HashMap<String,Integer>();
		startinglocs = new HashMap<UUID,Location>();
	} 
	
	// Returns false if the new starting location is not allowed.
	public boolean addNewStartingLocation(LivingEntity l) {
		if (l instanceof LivingEntity) {
			String hash = getLocationHash(l.getLocation());
			if (locationhashes.containsKey(hash)) {
				int spawnamt = locationhashes.get(hash);
				TwosideKeeper.log("[Habitat]Spawn Amount was "+spawnamt+". "+((0.5/(spawnamt+1))*100)+"% chance to fail.",4);
				if (Math.random()>(20/((spawnamt*2)+1))) {
					TwosideKeeper.log("[Habitat]It failed.",4);
					return false;
				}
			} else {
				TwosideKeeper.log("[Habitat]No hash exists for this location yet. Created new hash: "+hash,5);
			}
			startinglocs.put(l.getUniqueId(), l.getLocation());
		}
		return true;
	}
	
	/*public void addKillToLocation(LivingEntity l) {
		String hash = getLocationHash(l.getLocation());
		if (startinglocs.containsKey(l.getUniqueId())) {
			hash = getLocationHash(startinglocs.get(l.getUniqueId()));
		}
		if (locationhashes.containsKey(hash)) {
			int spawnamt = locationhashes.get(hash);
			spawnamt+=1;
			locationhashes.put(hash,spawnamt);
			for (int x=-4;x<5;x++) {
				for (int z=-4;z<5;z++) {
					if (x!=0 && z!=0) {
						addKillToLocation(l.getLocation().add(x*16,0,z*16));
					}
				}
			}
		}
		else {
			locationhashes.put(hash,1);
		}
	}	
	
	public void addKillToLocation(Location l) {
		String hash = getLocationHash(l);
		if (locationhashes.containsKey(hash)) {
			int spawnamt = locationhashes.get(hash);
			if (Math.random()<=0.5) {
				spawnamt+=1;
			}
			locationhashes.put(hash,spawnamt);
		}
		else {
			locationhashes.put(hash,1);
		}
	}*/
	
    public void addKillToLocation(LivingEntity entityKilled) {
        for (int offsetMeters = 0; offsetMeters < 64; offsetMeters += 4) {
            // Attempt to add 1 habitat to a random chunk within offsetMeters of the death location
            // Guaranteed to add at least 1 kill to the chunk that the killed entity was in.
            addKillToLocation(getRandomLocationWithinCircle(
                    // Use entity source location, or death location if not available
                    startinglocs.getOrDefault(entityKilled.getUniqueId(), entityKilled.getLocation()),
                    offsetMeters));
        }
    }

    public void addKillToLocation(Location location) {
        String locationHash = getLocationHash(location);
        //TwosideKeeper.log("Location hash is "+locationHash, 0);
        locationhashes.put(locationHash, locationhashes.getOrDefault(locationHash, 0) + 1);
    }

    private Location getRandomLocationWithinCircle(Location sourceLocation, double radiusMultiplier) {
        final double angle = 2 * Math.PI * ThreadLocalRandom.current().nextDouble();
        final double temp = ThreadLocalRandom.current().nextDouble() + ThreadLocalRandom.current().nextDouble();
        final double radius = temp > 1 ? radiusMultiplier * (2 - temp) : radiusMultiplier * temp;
        return sourceLocation.clone().add(radius * Math.cos(angle), 0, radius * Math.sin(angle));
    }
	
	public void increaseHabitationLevels() {
		for(String hash : locationhashes.keySet()) {
			int spawnamt = locationhashes.get(hash);
			TwosideKeeper.log("[Habitat]Habitat Location "+hash+" has Spawn Amont "+spawnamt+".",4);
			if (spawnamt<20) {
				Bukkit.getScheduler().scheduleSyncDelayedTask(TwosideKeeper.plugin, 
	            () -> {
					locationhashes.remove(hash);
					TwosideKeeper.log("[Habitat]It's now clear~!",4);
	            }, 1); 
			} else {
				if (Math.random()<=0.5) {
					locationhashes.put(hash, (int)(spawnamt*0.9));
					TwosideKeeper.log("[Habitat]It's now "+(int)(spawnamt*0.9)+"!",4);
				}
			}
		}
	}
	
	public void removeStartingLocation(LivingEntity l) {
		if (startinglocs.containsKey(l.getUniqueId())) {
			startinglocs.remove(l.getUniqueId());
		}
	}
	
    public String getLocationHash(Location location) {
        if (location != null) {
            return location.getChunk().getX() + ' ' + String.valueOf((int)location.getY() / 16) + ' ' + location.getChunk().getZ() + ' ' + location.getWorld().getName();
        } else {
            TwosideKeeper.log(
                    "[ERROR][Habitat]Could not get Location Hash!!! Probably undefined Player->Enemy hit interaction!",
                    1);
            return "";
        }
    }
	
	public void saveLocationHashesToConfig() {
		File file = new File(TwosideKeeper.plugin.getDataFolder()+"/locationhashes.data");

		// if file doesnt exists, then create it
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try(
			FileWriter fw = new FileWriter(TwosideKeeper.plugin.getDataFolder()+"/locationhashes.data", false);
		    BufferedWriter bw = new BufferedWriter(fw);)
		{
			for(int i=0;i<locationhashes.keySet().toArray().length;i++) {
				bw.write((String)locationhashes.keySet().toArray()[i]+","+locationhashes.get((String)locationhashes.keySet().toArray()[i]));
				bw.newLine();
			}
			if (locationhashes.keySet().toArray().length>0) {
				TwosideKeeper.log("[Habitat]Saved "+(locationhashes.keySet().toArray().length)+" habitat"+((locationhashes.keySet().toArray().length)!=1?"s":"")+" successfully.",2);
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		    //exception handling left as an exercise for the reader
		}
	}

	public void loadLocationHashesFromConfig() {
		File file = new File(TwosideKeeper.plugin.getDataFolder()+"/locationhashes.data");

		if (file.exists()) {
			try(
					FileReader fw = new FileReader(TwosideKeeper.plugin.getDataFolder()+"/locationhashes.data");
				    BufferedReader bw = new BufferedReader(fw);)
				{
					String readline = bw.readLine();
					int lines = 0;
					do {
						if (readline!=null) {
							lines++;
							String[] split = readline.split(",");
							String hash = split[0];
							int amt = Integer.parseInt(split[1]);
							if (locationhashes.containsKey(hash)) {
								int spawnamt = locationhashes.get(hash);
								locationhashes.put(hash,spawnamt+amt);
							}
							else {
								locationhashes.put(hash,amt);
							}
							TwosideKeeper.log("[Habitat]Loaded Habitat "+hash+" with spawn amount "+locationhashes.get(hash),4);
							readline = bw.readLine();
						}} while (readline!=null);
					TwosideKeeper.log("[Habitat]Loaded "+lines+" habitats successfully.",2);
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
	
	public String getHabitationStatus(int amt) {
		if (amt<=10) {
			return ChatColor.DARK_GREEN+"Fruitful";
		} else
		if (amt<=25) {
			return ChatColor.GREEN+"Habitable";
		} else
		if (amt<=40) {
			return ChatColor.YELLOW+"Moderate";
		} else
		if (amt<=80) {
			return ChatColor.GOLD+"Poor";
		} else
		if (amt<=240) {
			return ChatColor.RED+"Sparse";
		} else
		{
			return ChatColor.DARK_RED+"Unhabitable";
		}
	}
	
	public String getHabitationLevel(Location l) {
		String hash = getLocationHash(l);
		if (locationhashes.containsKey(hash)) {
			return getHabitationStatus(locationhashes.get(hash))+((TwosideKeeper.SERVER_TYPE!=ServerType.MAIN)?locationhashes.get(hash):"");
		} else {
			return getHabitationStatus(0)+((TwosideKeeper.SERVER_TYPE!=ServerType.MAIN)?locationhashes.get(hash):"");
		}
	}
}