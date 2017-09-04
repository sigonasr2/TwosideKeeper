package sig.plugin.TwosideKeeper.HelperStructures;

import sig.plugin.TwosideKeeper.TwosideKeeper;

public enum CubeType {
	NORMAL(0,9),LARGE(1,27),ENDER(2,27),VACUUM(3,54),FILTER(4,27),MAGICWAND(5,2);
	
	int id=0;
	int size=9;
	
	private CubeType(int id, int size) {
		this.id=id;
		this.size=size;
	}
	
	public int getID() {
		return this.id;
	}
	
	public int getSize() {
		return this.size;
	}
	
	public static CubeType getCubeTypeFromID(int id) {
		for (CubeType ct : CubeType.values()) {
			if (ct.getID()==id) {
				return ct;
			}
		}
		TwosideKeeper.log("INVALID CUBE ID SPECIFIED: "+id+". THIS SHOULD NOT BE HAPPENING!", 0);
		return null;
	}

	public static int getSlotsFromType(CubeType size) {
		switch (size) {
		case ENDER:
			return 27;
		case FILTER:
			return 27;
		case LARGE:
			return 27;
		case NORMAL:
			return 9;
		case VACUUM:
			return 54;
		case MAGICWAND:
			return 2;
		default:
			return 27;
		}
	}
	
}
