package sig.plugin.TwosideKeeper.Logging;

public class Average {
	int avg;
	int count;
	int high;
	int low;
	int trend;
	int last;
	public Average() {
		this.avg=0;
		this.count=0;
		this.high=0;
		this.low=Integer.MAX_VALUE;
		this.trend=0;
		this.last=0;
	}
	
	public void add(int value) {
		if (value>high) {
			high=value;
		} else
		if (value<low) {
			low=value;
		}
		last=value;
		int curravg = avg;
		avg = (((count<60?count:60) * avg) + value) / ((count<60?count:60) + 1);
		trend += Math.signum(avg-curravg);
		if (trend>60) {trend=60;}
		if (trend<-60) {trend=-60;}
		count++;
	}
}
