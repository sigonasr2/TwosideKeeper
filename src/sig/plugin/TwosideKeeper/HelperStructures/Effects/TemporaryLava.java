package sig.plugin.TwosideKeeper.HelperStructures.Effects;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;

import sig.plugin.TwosideKeeper.TwosideKeeper;

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
				b.setType(Material.LAVA);
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
			b.setType(Material.AIR);
		}
	}
	private void ClearLavaBlock(Block b2) {
		if (b.getType()==Material.LAVA || b.getType()==Material.STATIONARY_LAVA) {
			b.setType(Material.AIR);
		}
	}
	public void Cleanup() {
		ResetBlock();
	}
}
