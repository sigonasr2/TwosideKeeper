package sig.plugin.TwosideKeeper.HelperStructures.Effects;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;

import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.HelperStructures.Common.BlockModifyQueue;

public class TemporaryLava {
	Block b;
	int ttl; //Time-to-Live. When this expires it dies.
	public TemporaryLava(Block b, int timer) {
		this.b=b;
		this.ttl=timer;
	}
	public TemporaryLava(Block b, int timer, boolean convert) {
		if (convert) {
			if (b.getType()==Material.AIR) {
				//b.setType(Material.LAVA);
				TwosideKeeper.blockqueue.add(new BlockModifyQueue(b,Material.AIR,Material.LAVA));
				//b.setData((byte)8);
			}
		}
		this.b=b;
		this.ttl=timer;
	}
	public boolean runTick() {
		this.ttl--;
		if (this.ttl<=0) {
			ResetBlock();
			return false;
		} else {
			return true;
		}
	}
	private void ResetBlock() {
		if (b.getType()==Material.LAVA || b.getType()==Material.STATIONARY_LAVA) {
			TwosideKeeper.blockqueue.add(new BlockModifyQueue(b,b.getType(),Material.AIR));
		}
	}
	public void Cleanup() {
		ResetBlock();
	}
}
