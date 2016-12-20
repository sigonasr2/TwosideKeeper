package sig.plugin.TwosideKeeper;

import sig.plugin.TwosideKeeper.HelperStructures.Common.BlockModifyQueue;

public class runServerTick implements Runnable{

	@Override
	public void run() {
		if (TwosideKeeper.blockqueue.size()>0) {
			BlockModifyQueue bmq = TwosideKeeper.blockqueue.remove(0);
			bmq.run();
		}
	}
	
}
