package sig.plugin.TwosideKeeper.HelperStructures.Effects;

import org.bukkit.Material;
import org.bukkit.block.Block;

import sig.plugin.TwosideKeeper.TwosideKeeper;

public class ReplaceBlockTask implements Runnable{
	String str;
	
	public ReplaceBlockTask(String str) {
		this.str=str;
	}
	
	@Override
	public void run() {
		if (TwosideKeeper.temporaryblocks.containsKey(str)) {
			TemporaryBlock tb = TwosideKeeper.temporaryblocks.get(str);
			CleanupTemporaryBlock(tb);
			TwosideKeeper.temporaryblocks.remove(TemporaryBlock.getLocationKey(tb));
			TwosideKeeper.temporaryblocks.remove(TemporaryBlock.getLocationKey(tb, false));
		}
	}

	public static void CleanupTemporaryBlock(TemporaryBlock tb) {
		if (tb.getBlock()!=null &&
				tb.getBlock().getType()==tb.getConvertedMaterial() &&
						tb.getBlock().getData()==tb.getConvertedData()) {
			tb.getBlock().setType(tb.getOriginalMaterial());
			tb.getBlock().setData(tb.getOriginalData());
		}
	}
}
