package sig.plugin.TwosideKeeper;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import sig.plugin.TwosideKeeper.HelperStructures.ArtifactItem;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.SoundUtils;

public class ChallengeReward {
    public static boolean hasRewards(Player p) {
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		return pd.rewards.length()>0;
	}

	public static void rewardAwards(Player p) {
		PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
		GlobalLoot gl = GlobalLoot.spawnGlobalLoot(p.getLocation(), ChatColor.GREEN+"Daily Challenge Loot");
		String[] awards = pd.rewards.split(",");
		for (String s : awards) {
			int rank = Integer.parseInt(s);
			if (pd.isFirstReward) {
				gl.addNewDropInventory(p.getUniqueId(), getAward(rank));
				pd.isFirstReward=false;
			}
			gl.addNewDropInventory(p.getUniqueId(), getAward(rank));	
		}
		pd.rewards="";
		SoundUtils.playLocalSound(p, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.2f);
	}
	
	public static void provideAwards() {
		provideAwards(TwosideKeeper.dpschallenge_records);
		provideHOFAwards(TwosideKeeper.dpschallenge_recordsHOF);
		provideAwards(TwosideKeeper.tankchallenge_records);
		provideHOFAwards(TwosideKeeper.tankchallenge_recordsHOF);
		provideAwards(TwosideKeeper.parkourchallenge_records);
		provideHOFAwards(TwosideKeeper.parkourchallenge_recordsHOF);
	}
	
	private static void provideAwards(RecordKeeping r) {
		for (int i=0;i<r.recordlist.size();i++) {
			Record rec = r.recordlist.get(i);
			int rank = i+1;
			Player p = Bukkit.getPlayer(rec.getName());
			if (p!=null && p.isOnline()) {
				PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
				if (hasRewards(p)) {
					pd.rewards+=","+rank;
				} else {
					pd.rewards=Integer.toString(rank);
				}
			} else {
				File config;
				config = new File(TwosideKeeper.filesave,"users/"+Bukkit.getOfflinePlayer(rec.getName()).getUniqueId()+".data");
				FileConfiguration workable = YamlConfiguration.loadConfiguration(config);
		
				if (!config.exists()) {
					//Something bad happened if we got here.
					TwosideKeeper.log("[WARNING] Could not find the correct player data file for "+p+" to get money data from.",1);
				} else {
					
					String rewards = workable.getString("rewards");
					if (rewards.length()>0) {
						rewards+=","+rank;
					} else {
						rewards=Integer.toString(rank);
					}
					
					workable.set("rewards", rewards);
					
					try {
						workable.save(config);
					} catch (IOException e) {
						TwosideKeeper.log("[WARNING] Could not find the correct player data file for "+p+" to get money data from.",1);
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	private static void provideHOFAwards(RecordKeeping r) {
		if (r.recordlist.size()>0) {
			Record rec = r.recordlist.get(0);
			Player p = Bukkit.getPlayer(rec.getName());
			if (p!=null && p.isOnline()) {
				PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
				if (hasRewards(p)) {
					pd.rewards+=","+438190894;
				} else {
					pd.rewards=Integer.toString(438190894);
				}
			} else {
				File config;
				config = new File(TwosideKeeper.filesave,"users/"+Bukkit.getOfflinePlayer(rec.getName()).getUniqueId()+".data");
				FileConfiguration workable = YamlConfiguration.loadConfiguration(config);
		
				if (!config.exists()) {
					//Something bad happened if we got here.
					TwosideKeeper.log("[WARNING] Could not find the correct player data file for "+p+" to get money data from.",1);
				} else {
					
					String rewards = workable.getString("rewards");
					if (rewards.length()>0) {
						rewards+=","+438190894;
					} else {
						rewards=Integer.toString(438190894);
					}
					
					workable.set("rewards", rewards);
					
					try {
						workable.save(config);
					} catch (IOException e) {
						TwosideKeeper.log("[WARNING] Could not find the correct player data file for "+p+" to get money data from.",1);
						e.printStackTrace();
					}
				}
			}
		}
	}

	public static ItemStack[] getAward(int rank) {
		switch (rank) {
			case 1:{
				return new ItemStack[]{
						Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE,5),
						Artifact.createArtifactItem(ArtifactItem.ARTIFACT_ESSENCE,8),
						Artifact.createArtifactItem(ArtifactItem.ARTIFACT_CORE,4),
						Artifact.createArtifactItem(ArtifactItem.ARTIFACT_BASE,2),
						};
			}
			case 2:{
				return new ItemStack[]{
						Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE,3),
						Artifact.createArtifactItem(ArtifactItem.ARTIFACT_ESSENCE,4),
						Artifact.createArtifactItem(ArtifactItem.ARTIFACT_CORE,2),
						Artifact.createArtifactItem(ArtifactItem.ARTIFACT_BASE,1),
						};
			}
			case 3:{
				return new ItemStack[]{
						Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE,3),
						};
			}
			case 438190894:{
				return new ItemStack[]{
						Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE,5),
						};
			}
			default:{
				return new ItemStack[]{
						Artifact.createArtifactItem(ArtifactItem.MYSTERIOUS_ESSENCE),
						};
			}
		}
	}
}
