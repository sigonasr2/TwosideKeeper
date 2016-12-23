package sig.plugin.TwosideKeeper.HelperStructures.Utils;

import org.bukkit.block.Biome;

public class BiomeUtils {

	public static boolean isColdBiome(Biome biome) {
		return (biome==Biome.ICE_FLATS ||
				biome==Biome.ICE_MOUNTAINS ||
				biome==Biome.MUTATED_ICE_FLATS ||
				biome==Biome.TAIGA_COLD ||
				biome==Biome.TAIGA_COLD_HILLS ||
				biome==Biome.FROZEN_RIVER ||
				biome==Biome.COLD_BEACH ||
				biome==Biome.FROZEN_OCEAN);
	}

}
