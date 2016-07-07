package sig.plugin.TwosideKeeper;

import java.text.DecimalFormat;

public class Lag
{
	public long LAST_TICK= System.currentTimeMillis();
	public double lastTPS = 20.0;
	
	public void updateTPS()
	{
		long calctime = (long) ((System.currentTimeMillis()-LAST_TICK));
		TwosideKeeper.log("Ticks were "+LAST_TICK+":::"+System.currentTimeMillis(), 5);
		LAST_TICK = System.currentTimeMillis();
		lastTPS = 20*1000/(calctime / 60d);
	}
	public double getTPS() {
		DecimalFormat df = new DecimalFormat("0.00");
		return Double.parseDouble(df.format(lastTPS));
	}
}