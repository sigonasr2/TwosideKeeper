package sig.plugin.TwosideKeeper.Generators;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

public class Room extends ChunkGenerator{
	int ROOM_WIDTH = 32;
	int ROOM_LENGTH = 32;
	
	public Room() {
		
	}
	
	public Room(int width, int length) {
		ROOM_WIDTH=width;
		ROOM_LENGTH=length;
	}
	
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
	    if (chunkx<ROOM_WIDTH/16 &&
	    		chunkz<ROOM_LENGTH/16 &&
	    		chunkx>=0 &&
	    		chunkz>=0) {
	    	for(int x=0; x<16; x++){
			    for(int z=0; z<16; z++) {
			    	result[xyzToByte(x,0,z)] = (byte) Material.BEDROCK.getId();
			    	result[xyzToByte(x,255,z)] = (byte) Material.BEDROCK.getId();
			    }
		    }
			GenerateOuterWalls(chunkx, chunkz, result);
	    }
	    /*for(int x=0; x<16; x++){
		    for(int z=0; z<16; z++) {
		    	result[xyzToByte(x,0,z)] = (byte) Material.BEDROCK.getId();
		    	result[xyzToByte(x,255,z)] = (byte) Material.BEDROCK.getId();
		    }
	    }*/
	    return result;
    }
    
	private void GenerateOuterWalls(int chunkx, int chunkz, byte[] result) {
		int wallslotx = Math.floorMod(chunkx,ROOM_WIDTH/16);
		int wallslotz = Math.floorMod(chunkz,ROOM_LENGTH/16);
	    for (int y=1;y<255;y++) {
		    for(int x=0; x<16; x++){
		    	for (int z=0;z<16;z++) {
			    	if (wallslotx==0) {
			    		result[xyzToByte(0,y,z)] = (byte) Material.BEDROCK.getId();
			    	} else 
				    if (wallslotx==(ROOM_WIDTH/16)-1){
				    	result[xyzToByte(15,y,z)] = (byte) Material.BEDROCK.getId();
			    	}
			    	if (wallslotz==0) {
			    		result[xyzToByte(x,y,0)] = (byte) Material.BEDROCK.getId();
			    	} else 
			    	if (wallslotz==(ROOM_LENGTH/16)-1){
			    		result[xyzToByte(x,y,15)] = (byte) Material.BEDROCK.getId();
			    	}
		    	}
		    }
	    }
	}
	
	public int getRoomWidth() {
		return ROOM_WIDTH;
	}
	public int getRoomLength() {
		return ROOM_LENGTH;
	}
}
