package sig.plugin.TwosideKeeper.HelperStructures.Common;



public class PVPValue {
	int points; //Number of points in this value.
	double baseval; //The base value of this ability.
	
	public PVPValue(int points, double baseval) {
		this.points=points;
		this.baseval=baseval;
	}
	
	public int getPointValue() {
		return points;
	}
	public double getBaseValue() {
		return baseval;
	}
}