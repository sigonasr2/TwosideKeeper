package sig.plugin.TwosideKeeper;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

public class RoomWorldGenerator extends ChunkGenerator{
    @Override
    public boolean canSpawn(World world, int x, int z) {
        return true;
    }
    public int xyzToByte(int x, int y, int z) {
    return (x * 16 + z) * 256 + y;
    }

    @Override
    public byte[] generate(World world, Random rand, int chunkx, int chunkz) {
	    byte[] result = new byte[65536];
	    for(int x=0; x<16; x++){
		    for(int z=0; z<16; z++) {
		    	result[xyzToByte(x,0,z)] = (byte) Material.BEDROCK.getId();
		    	result[xyzToByte(x,255,z)] = (byte) Material.BEDROCK.getId();
		    }
	    }

		int wallslotx = Math.floorMod(chunkx,2);
		int wallslotz = Math.floorMod(chunkz,2);
	    for (int y=1;y<255;y++) {
		    for(int x=0; x<16; x++){
		    	for (int z=0;z<16;z++) {
			    	if (wallslotx==0) {
			    		result[xyzToByte(0,y,z)] = (byte) Material.BEDROCK.getId();
			    	} else {
				    	result[xyzToByte(15,y,z)] = (byte) Material.BEDROCK.getId();
			    	}
			    	if (wallslotz==0) {
			    		result[xyzToByte(x,y,0)] = (byte) Material.BEDROCK.getId();
			    	} else {
			    		result[xyzToByte(x,y,15)] = (byte) Material.BEDROCK.getId();
			    	}
		    	}
		    }
	    }
	    return result;
    }
}
