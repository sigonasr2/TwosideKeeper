package sig.plugin.TwosideKeeper.HelperStructures;

public enum BowMode {
	CLOSE("Close Range"),
	SNIPE("Sniping"),
	DEBILITATION("Debilitation");
	
	String coolname;
	
	BowMode(String coolname) {
		this.coolname=coolname;
	}
	
	public String GetCoolName() {
		return this.coolname;
	}
}
