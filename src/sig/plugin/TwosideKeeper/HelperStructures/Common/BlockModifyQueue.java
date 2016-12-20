package sig.plugin.TwosideKeeper.HelperStructures.Common;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class BlockModifyQueue{
	Material checktype; //The material the block should be before converting.
	Material finaltype; //The material the block is converting into.
	byte checkdata; //Data the block should be before converting.
	byte finaldata; //Data the block should be after converting.
	boolean usedata=false;
	Block b; //The block we are converting.
	
	public BlockModifyQueue(Block b, Material checktype, Material finaltype) {
		this.b=b;
		this.checktype=checktype;
		this.finaltype=finaltype;
		this.checkdata=0;
		this.finaldata=0;
		this.usedata=false;
	}
	
	public BlockModifyQueue(Block b, Material checktype, byte checkdata, Material finaltype, byte finaldata) {
		this.b=b;
		this.checktype=checktype;
		this.finaltype=finaltype;
		this.checkdata=checkdata;
		this.finaldata=finaldata;
		this.usedata=true;
	}

	public void run() {
		if ((TypeException(b) || b.getType()==checktype) && (!usedata || b.getData()==checkdata)) {
			b.setType(finaltype);
			if (usedata) {b.setData(finaldata);}
		}
	}
	
	private boolean TypeException(Block b) {
		if (b.getType()==Material.STATIONARY_LAVA || b.getType()==Material.LAVA ||
				b.getType()==Material.STATIONARY_WATER || b.getType()==Material.WATER) {
			return true;
		} else {
			return false;
		}
	}

	public static void Cleanup(List<BlockModifyQueue> queue) {
		for (BlockModifyQueue bmq: queue) {
			bmq.run();
		}
	}

}
