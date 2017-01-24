package sig.plugin.TwosideKeeper;

import sig.plugin.TwosideKeeper.HelperStructures.Common.BlockModifyQueue;

public class runServerTick implements Runnable{
	final int queuespd = 3;

	@Override
	public void run() {
		for (int i=queuespd;i>0;i--) {
			if (TwosideKeeper.blockqueue.size()>0) {
				BlockModifyQueue bmq = TwosideKeeper.blockqueue.remove(0);
				bmq.run();
			}
		}
		runServerHeartbeat.resetDamageQueue();
	}
	
}
